package traore.com.system_gestion_ega.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traore.com.system_gestion_ega.Model.Compte;

import java.util.Optional;

public interface CompteRepository extends JpaRepository<Compte, Long> {
    Optional<Compte> findCompteById(Long id);
    Optional<Compte> findByNumCompte(String code);

}
