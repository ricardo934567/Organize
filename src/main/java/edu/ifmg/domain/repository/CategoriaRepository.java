package edu.ifmg.domain.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import edu.ifmg.domain.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	

}
