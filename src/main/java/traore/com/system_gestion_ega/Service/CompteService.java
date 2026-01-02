package traore.com.system_gestion_ega.Service;

import traore.com.system_gestion_ega.Model.Compte;

import java.util.List;
import java.util.Optional;

public interface CompteService {
    List<Compte> getAllCompte();
    Optional<Compte> getCompteById(Long id);
    void deleteCompteById(Long id);
    Compte updateCompteById(Long id, Compte compte);
    Compte createCompte(Compte compte);
    Optional<Compte> findByNumCompte(String numCompte);
}
