package edu.ifmg.api.controll;


import edu.ifmg.domain.model.MetaCategoria;
import edu.ifmg.domain.repository.MetaCategoriaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/metaCategorias")
public class MetaCategoriaController {

        @Autowired
        private MetaCategoriaRepository metaCategoriaRepository;

        @ResponseStatus(HttpStatus.OK)
        @GetMapping
        public List<MetaCategoria> listar(){
                return metaCategoriaRepository.findAll();
        }


        @GetMapping("/{metaCategoriaId}")
        public ResponseEntity<MetaCategoria> buscar(@PathVariable  long metaCategoriaId) {
                Optional<MetaCategoria> c = metaCategoriaRepository.findById(metaCategoriaId);
                return c.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }

        @ResponseStatus(HttpStatus.CREATED)
        @PostMapping
        public MetaCategoria salvar(@RequestBody MetaCategoria metaCategoria) {
                return metaCategoriaRepository.save(metaCategoria);
        }


        @DeleteMapping("/{metaCategoriaId}")
        public ResponseEntity<MetaCategoria> remover(@PathVariable Long metaCategoriaId) {
                try {
                        Optional<MetaCategoria> metaCategoria = metaCategoriaRepository.findById(metaCategoriaId);
                        if (metaCategoria.isPresent()) {

                                metaCategoriaRepository.deleteById(metaCategoriaId);
                                return ResponseEntity.noContent().build();
                        }
                        return ResponseEntity.notFound().build();

                } catch (DataIntegrityViolationException e) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
        }

        @PutMapping("/{metaCategoriaId}")
        public ResponseEntity<MetaCategoria> atualizar(@PathVariable Long metaCategoriaId,@RequestBody MetaCategoria metaCategoria) {
                Optional<MetaCategoria> metaCategoriaAtual = metaCategoriaRepository.findById(metaCategoriaId);
                if(metaCategoriaAtual.isPresent()) {
                        BeanUtils.copyProperties(metaCategoria, metaCategoriaAtual.get(),"id");
                        MetaCategoria metaCategoriaSalva =metaCategoriaRepository.save(metaCategoriaAtual.get());
                        return ResponseEntity.ok(metaCategoriaSalva);
                }
                return ResponseEntity.notFound().build();
        }





}
