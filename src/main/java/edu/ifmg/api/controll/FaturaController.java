package edu.ifmg.api.controll;

import edu.ifmg.domain.model.Fatura;
import edu.ifmg.domain.model.Transacao;
import edu.ifmg.domain.repository.FaturaRepository;
import edu.ifmg.domain.repository.TransacaoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/faturas")
public class FaturaController {

    @Autowired
    private FaturaRepository faturaRepository;

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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/insert")
    public Fatura salvarTeste(@RequestBody Fatura fatura) {


        // Salva a fatura
        Fatura faturaSalva = faturaRepository.save(fatura);

        // Gera as transações
        List<Transacao> transacoes = new ArrayList<>();
        for (int i = 1; i <= fatura.getParcelas(); i++) {
            Transacao transacao = new Transacao();
            transacao.setValor((long) (fatura.getValorTotal() / fatura.getParcelas()));
            transacao.setParcela((long) i);
            transacao.setFatura(faturaSalva);
            transacoes.add(transacao);
            System.out.println("quero isso: " + transacao);
        }

        // Salva as transações no banco de dados usando instruções SQL
        for (Transacao transacao : transacoes) {
            String sql = "INSERT INTO transacao (data_pagamento, data_transacao, data_vencimento, parcela, valor, fatura_id) VALUES (?, ?, ?, ?, ?, ?)";
            Object[] params = {
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(30),
                    transacao.getParcela(),
                    transacao.getValor(),
                    transacao.getFatura().getId()
            };
            jdbcTemplate.update(sql, params);
        }

        return faturaSalva;

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
}
