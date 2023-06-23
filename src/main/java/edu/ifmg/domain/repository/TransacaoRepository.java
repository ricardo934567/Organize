package edu.ifmg.domain.repository;



import edu.ifmg.domain.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
	

}
