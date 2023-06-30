package edu.ifmg.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class MetaCategoria {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double limite;

    @Column(nullable = false)
    private Boolean controle;

    @OneToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;


}
