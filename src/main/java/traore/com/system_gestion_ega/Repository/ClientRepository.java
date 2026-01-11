package traore.com.system_gestion_ega.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traore.com.system_gestion_ega.Enum.Sexe;
import traore.com.system_gestion_ega.Model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findClientById(Long id);
    // Chercher par nom ou prénom
    List<Client> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);

    // Chercher par nationalité
    List<Client> findByNationaliteIgnoreCase(String nationalite);

    // Chercher par sexe
    List<Client> findBySexe(Sexe sexe);
}
