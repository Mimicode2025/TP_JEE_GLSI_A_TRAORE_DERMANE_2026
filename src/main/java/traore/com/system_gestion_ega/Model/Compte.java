package traore.com.system_gestion_ega.Model;

import jakarta.persistence.*;
import lombok.*;
import traore.com.system_gestion_ega.Enum.TypeComptes;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numCompte;

    @Enumerated(EnumType.STRING)
    private TypeComptes typeCompte;

    private LocalDate dateCreation;

    @Column(nullable = true)
    private Double solde = 0.00;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client proprietaireCompte;
}
