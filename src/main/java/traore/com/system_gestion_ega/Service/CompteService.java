package traore.com.system_gestion_ega.Service;

import traore.com.system_gestion_ega.Model.Compte;
import traore.com.system_gestion_ega.Model.Transaction;

import java.util.List;
import java.util.Optional;

public interface CompteService {
    List<Compte> getAllCompte();
    Optional<Compte> getCompteById(Long id);
    void deleteCompteById(Long id);
    Compte updateCompteById(Long id, Compte compte);
    Compte createCompte(Compte compte);
    Optional<Compte> findByNumCompte(String numCompte);
    String genererNumCompte();
    Transaction effectuerDepot(String numCompte, Double montant, String description);
    Transaction effectuerRetrait(String numCompte, Double montant, String description);
    Transaction effectuerVirement(String numCompteSource, String numCompteDest, Double montant, String description );
}
