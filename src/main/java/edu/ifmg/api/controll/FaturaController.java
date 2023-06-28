package edu.ifmg.api.controll;

import edu.ifmg.domain.model.Fatura;
import edu.ifmg.domain.repository.FaturaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.*;

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

    public Double verificarLimite(int id_categoria) {
        Double valorTotal = 0.00;

        String sql = "SELECT sum(vrTotal) AS vrTotal                        "+
                     "FROM (                                                "+
                     "  SELECT sum(f.valorTotal) AS vrTotal                 "+
                     "  FROM fatura f                                       "+
                     "  WHERE f.faturado = True AND f.categoria_id = ?      "+
                     "  UNION                                               "+
                     "  SELECT sum(t.valor) AS vrTotal                      "+
                     "  FROM fatura f                                       "+
                     "  INNER JOIN transacao t ON t.fatura_id = f.fatura_id "+
                     "  WHERE faturado = False AND categoria_id = ?         "+
                     ")                                                     ";
        //try {
            //PreparedStatement ps = conexao.prepareStatement(sql);
            //ps.setInt(1, id_categoria);
            //ps.setInt(2, id_categoria);
            //ResultSet rs = ps.executeQuery();
            //valorTotal = rs.getDouble("vrTotal");
            //rs.close();
            //ps.close();

            return valorTotal;

        //} catch (SQLException e) {
        //    System.err.println("Erro ao verificar o valor gasto pela categoria: " + e.getMessage());
        //}
    }
}
