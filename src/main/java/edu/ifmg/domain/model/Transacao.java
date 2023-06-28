package edu.ifmg.domain.model;

        import lombok.Data;
        import lombok.EqualsAndHashCode;
        import org.hibernate.annotations.CreationTimestamp;
        import org.hibernate.annotations.UpdateTimestamp;

        import javax.persistence.*;

        import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Transacao {


    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valor",nullable = false)
    private Long valor;

    @Column(name = "parcela",nullable = false)
    private Long parcela;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime dataTransacao;

    @UpdateTimestamp
    @Column(nullable = true, columnDefinition = "datetime")
    private LocalDateTime dataPagamento;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime dataVencimento;

    @ManyToOne
    @JoinColumn(name = "fatura_id", nullable = false)
    private Fatura fatura;

    @PrePersist
    private void prePersist() {
        LocalDateTime currentDate = LocalDateTime.now();
        dataPagamento = (currentDate.plusDays(30));
        dataVencimento = (currentDate.plusDays(45));
    }
}
