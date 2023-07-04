package edu.ifmg.api.controll;


import edu.ifmg.domain.model.MetaCategoria;
import edu.ifmg.domain.model.Transacao;
import edu.ifmg.domain.repository.TransacaoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

        @Autowired
        private TransacaoRepository transacaoRepository;

        @Autowired
        private JdbcTemplate jdbcTemplate;

        @ResponseStatus(HttpStatus.OK)
        @GetMapping
        public List<Transacao> listar(){

                return transacaoRepository.findAll();
        }


        @GetMapping("/{transacaoId}")
        public ResponseEntity<Transacao> buscar(@PathVariable  long transacaoId) {
                Optional<Transacao> c = transacaoRepository.findById(transacaoId);
                return c.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

        }

        @ResponseStatus(HttpStatus.CREATED)
        @PostMapping
        public Transacao salvar(@RequestBody Transacao transacao) {
                if (transacao.getId() == null) {
                        // O campo ID é nulo, podemos ignorá-lo
                        transacao.setId((long) -1);

                }
                return transacaoRepository.save(transacao);

        }


        @DeleteMapping("/{transacaoId}")
        public ResponseEntity<Transacao> remover(@PathVariable Long transacaoId) {

                try {
                        Optional<Transacao> transacao = transacaoRepository.findById(transacaoId);
                        if (transacao.isPresent()) {

                                transacaoRepository.deleteById(transacaoId);
                                return ResponseEntity.noContent().build();
                        }
                        return ResponseEntity.notFound().build();

                } catch (DataIntegrityViolationException e) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }


        }

        @PutMapping("/{transacaoId}")
        public ResponseEntity<Transacao> atualizar(@PathVariable Long transacaoId,@RequestBody Transacao transacao) {
                Optional<Transacao> transacaoAtual = transacaoRepository.findById(transacaoId);
                if(transacaoAtual.isPresent()) {
                        BeanUtils.copyProperties(transacao, transacaoAtual.get(),"id","dataPagamento","dataTransacao","dataVencimento","fatura");
                        Transacao transacaoSalva =transacaoRepository.save(transacaoAtual.get());
                        
                        return ResponseEntity.ok(transacaoSalva);
                }
                return ResponseEntity.notFound().build();
        }

        @PutMapping("/atualizar-data-pagamento/{id}")
        public ResponseEntity<Object> atualizarDataPagamento(@PathVariable Long id, @RequestBody Transacao transacao) {



                Optional<Transacao> transacaoAtual = transacaoRepository.findById(id);
                if(transacaoAtual.isPresent()) {
                        BeanUtils.copyProperties(transacao, transacaoAtual.get(),"id","dataTransacao","valor","parcela","dataVencimento","fatura");
                        Long idFatura = transacaoAtual.get().getFatura().getId();
                        Transacao transacaoSalva =transacaoRepository.save(transacaoAtual.get());


                        verificarFaturaPaga(idFatura);

                        return ResponseEntity.ok(transacaoSalva);
                }
                return ResponseEntity.notFound().build();
        }

        private void verificarFaturaPaga(Long id_fatura) {
                String sql1 = "SELECT fatura_id FROM transacao WHERE data_pagamento = '1970-01-01 00:00:00' and fatura_id = ?" ;

                Object[] params = {
                        id_fatura
                };

                try {
                        Map<String, Object> result = jdbcTemplate.queryForMap(sql1, params);

                } catch (EmptyResultDataAccessException erroFaturaPaga) {
                        // Trate o caso de nenhum resultado sobre data de pagamento vazio da fatura.

                        String sql2 = "UPDATE fatura SET faturado = true WHERE fatura_id = ? ";
                        jdbcTemplate.update(sql2, id_fatura);
                }
        }





}
