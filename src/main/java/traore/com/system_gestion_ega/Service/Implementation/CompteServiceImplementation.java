package traore.com.system_gestion_ega.Service.Implementation;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import traore.com.system_gestion_ega.Model.Compte;
import traore.com.system_gestion_ega.Repository.ClientRepository;
import traore.com.system_gestion_ega.Repository.CompteRepository;
import traore.com.system_gestion_ega.Service.CompteService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompteServiceImplementation implements CompteService {
    private final CompteRepository compteRepository;
    private final ClientRepository clientRepository;

    public CompteServiceImplementation(CompteRepository compteRepository, ClientRepository clientRepository) {
        this.compteRepository = compteRepository;
        this.clientRepository = clientRepository;
    }


    @Override
    public List<Compte> getAllCompte() {
        return compteRepository.findAll();
    }

    @Override
    public Optional<Compte> getCompteById(Long id) {
        return compteRepository.findById(id);
    }

    public Optional<Compte> findByNumCompte(String numCompte) {
        return compteRepository.findByNumCompte(numCompte);
    }

    @Override
    public void deleteCompteById(Long id) {
        compteRepository.deleteById(id);
    }

    @Override
    public Compte updateCompteById(Long id, Compte compte) {
        Compte compteSave = compteRepository.findById(id).orElseThrow(()-> new RuntimeException("Compte Not Found"));
        compteSave.setNumCompte(compte.getNumCompte());
        compteSave.setProprietaireCompte(compte.getProprietaireCompte());
        compteSave.setTypeCompte(compte.getTypeCompte());
        return compteRepository.save(compteSave);
    }

    @Override
    public Compte createCompte(Compte compte) {

        Long clientId = compte.getProprietaireCompte().getId();
        clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Le client avec l'ID " + clientId + " n'existe pas."));

        // Vérifier si le numéro de compte est déjà attribué
        if (compteRepository.findByNumCompte(compte.getNumCompte()).isPresent()) {
            throw new RuntimeException("Erreur : Ce numéro de compte appartient déjà à un autre utilisateur.");
        }

        compte.setSolde(0.0);
        compte.setDateCreation(LocalDate.now());
        return compteRepository.save(compte);
    }



}
