package edu.ifmg.api.controll;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.ifmg.domain.model.Categoria;
import edu.ifmg.domain.repository.CategoriaRepository;
@RestController
@RequestMapping("/categorias")
public class CategoriaController {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	public List<Categoria> listar(){
		return categoriaRepository.findAll();
	}
	
	
	@GetMapping("/{categoriaId}")
	public ResponseEntity<Categoria> buscar(@PathVariable long categoriaId) {
		Optional<Categoria> c = categoriaRepository.findById(categoriaId);
		if(c.isPresent()) {
			return ResponseEntity.ok(c.get());
		}
		return ResponseEntity.notFound().build();
		
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public Categoria salvar(@RequestBody Categoria categoria) {
		return categoriaRepository.save(categoria);
	}
	
	@DeleteMapping("/{categoriaId}")
	public ResponseEntity<Categoria> remover(@PathVariable Long categoriaId) {
		
		try {
			Optional<Categoria> categoria = categoriaRepository.findById(categoriaId);
			if (categoria.isPresent()) {
				
				categoriaRepository.deleteById(categoriaId);
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.notFound().build();
			
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		
		
	}
	
	@PutMapping("/{categoriaId}")
	public ResponseEntity<Categoria> atualizar(@PathVariable Long categoriaId, @RequestBody Categoria categoria) {
		Optional<Categoria> categoriaAtual = categoriaRepository.findById(categoriaId);
		if(categoriaAtual.isPresent()) {
			BeanUtils.copyProperties(categoria, categoriaAtual.get(),"id");
			Categoria categoriaSalva = categoriaRepository.save(categoriaAtual.get());
			return ResponseEntity.ok(categoriaSalva);
		}
		return ResponseEntity.notFound().build();
	}
}
