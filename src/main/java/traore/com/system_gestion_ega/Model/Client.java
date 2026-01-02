package traore.com.system_gestion_ega.Model;

import jakarta.persistence.*;
import lombok.*;
import traore.com.system_gestion_ega.Enum.Sexe;

import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    @Enumerated(EnumType.STRING)
    private Sexe sexe;

    private String adresse;

    @Column(unique = true)
    private String email;

    private String telephone;
    private LocalDate dateNaissance;
    private String nationalite;

    @OneToMany(mappedBy = "proprietaireCompte", cascade = CascadeType.ALL)
    private List<Compte> comptes;
}
