package edu.ifmg.api.controll;


import edu.ifmg.domain.model.MetaCategoria;
import edu.ifmg.domain.model.Transacao;
import edu.ifmg.domain.repository.TransacaoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

        @Autowired
        private TransacaoRepository transacaoRepository;

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





}
