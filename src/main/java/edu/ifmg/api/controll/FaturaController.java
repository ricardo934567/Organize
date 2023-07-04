package edu.ifmg.api.controll;

import edu.ifmg.domain.model.Fatura;
import edu.ifmg.domain.model.Transacao;
import edu.ifmg.domain.repository.FaturaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/faturas")
public class FaturaController {

    @Autowired
    private FaturaRepository faturaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Fatura> listar(){
        return faturaRepository.findAll();
    }

    @GetMapping("/{fatura_Id}")
    public ResponseEntity<Fatura> buscar(@PathVariable long faturaId) {
        Optional<Fatura> c = faturaRepository.findById(faturaId);
        if(c.isPresent()) {
            return ResponseEntity.ok(c.get());
        }
        return ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Fatura salvar(@RequestBody Fatura fatura) {

        return faturaRepository.save(fatura);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/insert")
    public Fatura salvarTeste(@RequestBody Fatura fatura) {

        Long idCategoria = fatura.getCategoria().getId();
        Double vrTotal = fatura.getValorTotal();

       if (verificarLimite(idCategoria, vrTotal).equals(true)) {
            // Salva a fatura
            Fatura faturaSalva = faturaRepository.save(fatura);

            // Gera as transações
            List<Transacao> transacoes = new ArrayList<>();
            for (int i = 1; i <= fatura.getParcelas(); i++) {
                Transacao transacao = new Transacao();
                transacao.setValor( (fatura.getValorTotal() / fatura.getParcelas()));
                transacao.setParcela((long) i);
                transacao.setFatura(faturaSalva);
                transacoes.add(transacao);
                System.out.println("quero isso: " + transacao);
            }

            // Salva as transações no banco de dados usando instruções SQL
            for (Transacao transacao : transacoes) {
                String sql = "INSERT INTO transacao (data_transacao, data_vencimento, parcela, valor, fatura_id) VALUES (?, ?, ?, ?, ?)";
                Object[] params = {
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(30),
                        transacao.getParcela(),
                        transacao.getValor(),
                        transacao.getFatura().getId()
                };
                jdbcTemplate.update(sql, params);
            }

            return faturaSalva;

        } else {
            return null;
        }
    }

    @DeleteMapping("/{fatura_Id}")
    public ResponseEntity<Fatura> remover(@PathVariable Long faturaId) {
        try {
            Optional<Fatura> fatura = faturaRepository.findById(faturaId);
            if (fatura.isPresent()) {

                faturaRepository.deleteById(faturaId);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{fatura_Id}")
    public ResponseEntity<Fatura> atualizar(@PathVariable Long faturaId, @RequestBody Fatura fatura) {
        Optional<Fatura> faturaAtual = faturaRepository.findById(faturaId);
        if(faturaAtual.isPresent()) {
            BeanUtils.copyProperties(fatura, faturaAtual.get(),"id");
            Fatura faturaSalva = faturaRepository.save(faturaAtual.get());

            return ResponseEntity.ok(faturaSalva);
        }

        return ResponseEntity.notFound().build();
    }

    private Boolean verificarLimite(Long id_categoria, Double novoValor) {
        Double valorRestante = 0.00;

        String sql1 =
                "SELECT sum(vrTotal) AS vrTotal, limite                                     "+
                        "FROM (                                                             "+
                        "  SELECT sum(f.valor_total) AS vrTotal, c.limite                   "+
                        "  FROM fatura f                                                    "+
                        "  INNER JOIN meta_categoria c ON c.categoria_id = f.categoria_id   "+
                        "  WHERE f.faturado = True AND f.categoria_id = ?                   "+
                        "  GROUP BY limite                                                  "+
                        "  UNION                                                            "+
                        "  SELECT sum(t.valor) AS vrTotal, c.limite                         "+
                        "  FROM fatura f                                                    "+
                        "  INNER JOIN meta_categoria c ON c.categoria_id = f.categoria_id   "+
                        "  INNER JOIN transacao t ON t.fatura_id = f.fatura_id              "+
                        "  WHERE f.faturado = False AND data_pagamento <> null AND          "+
                        "        f.categoria_id = ?                                         "+
                        "  GROUP BY limite                                                  "+
                        ") v                                                                "+
                        "GROUP BY limite                                                    ";

        Object[] params = {
                id_categoria,
                id_categoria
        };

        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(sql1, params);

            Double totalPago = (Double) result.get("vrTotal");
            Double limite = (Double) result.get("limite");

            valorRestante = (Double) (limite - (totalPago + novoValor));

        } catch (EmptyResultDataAccessException erroValorFaturado) {
            // Trate o caso de nenhum resultado sobre valores faturados.

            String sql2 =
                    "SELECT limite FROM  meta_categoria";
            Object[] param2 = {

            };

            try {
                Map<String, Object> result = jdbcTemplate.queryForMap(sql2, param2);

                Double limite = (Double) result.get("limite");

                valorRestante = (Double) (limite - novoValor);

            } catch (EmptyResultDataAccessException erroValorLimite) {
                // Trate o caso de nenhum resultado sobre o limite da categoria.
            }
        }

        return valorRestante > 0;
    }

    @GetMapping("/emaberto")
    public List<Fatura> getFaturasEmAberto() {
        List<Fatura> faturas = faturaRepository.findAll();
        List<Fatura> faturasEmAberto = new ArrayList<>();

        for (Fatura fatura : faturas) {
            List<Transacao> transacoes = fatura.getTransacoes();
            long parcelasPagas = transacoes.stream()
                    .filter(transacao -> !transacao.getDataPagamento().equals(LocalDateTime.of(1969, 12, 31, 21, 0)))
                    .count();

            if (parcelasPagas < fatura.getParcelas()) {
                faturasEmAberto.add(fatura);
            }
        }

        return faturasEmAberto;
    }

    @GetMapping("/vencidas")
    public List<Fatura> getFaturasVencidas() {
        List<Fatura> faturasVencidas = new ArrayList<>();

        List<Fatura> faturasAbertas = faturaRepository.findAll();

        LocalDate dataAtual = LocalDate.now();

        for (Fatura fatura : faturasAbertas) {
            List<Transacao> transacoes = fatura.getTransacoes();

            boolean vencida = false;
            for (Transacao transacao : transacoes) {
                LocalDate dataVencimento = LocalDate.from(transacao.getDataVencimento());

                if (dataVencimento != null && dataAtual.isAfter(dataVencimento)) {
                    vencida = true;
                    break;
                }
            }

            if (vencida) {
                faturasVencidas.add(fatura);
            }
        }

        return faturasVencidas;
    }

    @GetMapping("/fechadas")
    public List<Fatura> getFaturasFechadas() {
        List<Fatura> faturasFechadas = new ArrayList<>();

        List<Fatura> faturasAbertas = faturaRepository.findAll();

        for (Fatura fatura : faturasAbertas) {
            List<Transacao> transacoes = fatura.getTransacoes();

            boolean todasParcelasPagas = transacoes.stream()
                    .allMatch(transacao -> !transacao.getDataPagamento().equals(LocalDateTime.of(1969, 12, 31, 21, 0)));

            if (todasParcelasPagas) {
                faturasFechadas.add(fatura);
            }
        }

        return faturasFechadas;
    }
}
