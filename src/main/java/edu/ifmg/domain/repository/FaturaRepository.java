package edu.ifmg.domain.repository;

import edu.ifmg.domain.model.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FaturaRepository extends JpaRepository<Fatura, Long> {


}
