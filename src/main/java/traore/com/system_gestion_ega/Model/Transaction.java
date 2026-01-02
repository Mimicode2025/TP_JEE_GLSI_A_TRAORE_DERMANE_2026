package traore.com.system_gestion_ega.Model;

import jakarta.persistence.*;
import lombok.*;
import traore.com.system_gestion_ega.Enum.TypeTransaction;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Enumerated(EnumType.STRING)
    private TypeTransaction typeTransaction;

    private LocalDate transactionDate;
    private Double Montant;

    @ManyToOne
    @JoinColumn(name = "compte_envoie")
    private Compte compteEnvoie;

    @ManyToOne
    @JoinColumn(name = "compte_dstination")
    private Compte compteDestination;

}
