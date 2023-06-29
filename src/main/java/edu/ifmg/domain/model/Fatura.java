package edu.ifmg.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Fatura {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fatura_id")
    private Long id;

    @Column(name = "valorTotal", nullable = false)
    private Double valorTotal;

    @Column(name = "parcelas", nullable = false)
    private Integer parcelas;

    @Column(name = "faturado", nullable = false)
    private Boolean faturado;

    @JsonIgnore
    @OneToMany(mappedBy = "fatura")
    private List<Transacao> transacoes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
}
