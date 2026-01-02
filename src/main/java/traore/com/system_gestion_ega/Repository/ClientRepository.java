package traore.com.system_gestion_ega.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traore.com.system_gestion_ega.Model.Client;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findClientById(Long id);
}
