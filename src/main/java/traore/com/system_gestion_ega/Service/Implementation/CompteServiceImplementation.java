package traore.com.system_gestion_ega.Service.Implementation;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import traore.com.system_gestion_ega.Enum.TypeTransaction;
import traore.com.system_gestion_ega.Model.Compte;
import traore.com.system_gestion_ega.Model.Transaction;
import traore.com.system_gestion_ega.Repository.ClientRepository;
import traore.com.system_gestion_ega.Repository.TransactionRepository;
import traore.com.system_gestion_ega.Repository.CompteRepository;
import traore.com.system_gestion_ega.Service.CompteService;
import traore.com.system_gestion_ega.dto.ReleveBancaireDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompteServiceImplementation implements CompteService {
    private final CompteRepository compteRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;

    public CompteServiceImplementation(CompteRepository compteRepository, ClientRepository clientRepository, TransactionRepository transactionRepository) {
        this.compteRepository = compteRepository;
        this.clientRepository = clientRepository;
        this.transactionRepository = transactionRepository;
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

    public String genererNumCompte() {
        String pays = "TG";
        int annee = java.time.LocalDate.now().getYear();
        // Génère un nombre entre 10000000 et 99999999
        long randomPart = (long) (Math.random() * 90000000L) + 10000000L;
        return pays + annee + randomPart;
    }

    @Override
    public Compte createCompte(Compte compte) {
        // Vérification du propriétaire
        Long clientId = compte.getProprietaireCompte().getId();
        clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        // 2. Appel de la fonction pour le numéro de compte
        String nouveauNumero;
        do {
            nouveauNumero = genererNumCompte();
        } while (compteRepository.findByNumCompte(nouveauNumero).isPresent());

        compte.setNumCompte(nouveauNumero);
        compte.setSolde(0.0);
        compte.setDateCreation(LocalDate.now());

        return compteRepository.save(compte);
    }

    @Override
    @Transactional
    public Transaction effectuerVirement(String numCompteSource, String numCompteDest, Double montant, String description) {
        // 1. Récupération des deux comptes
        Compte source = compteRepository.findByNumCompte(numCompteSource)
                .orElseThrow(() -> new RuntimeException("Compte source introuvable"));

        Compte destination = compteRepository.findByNumCompte(numCompteDest)
                .orElseThrow(() -> new RuntimeException("Compte destination introuvable"));

        // 2. LA VÉRIFICATION CRITIQUE
        if (source.getSolde() < montant) {
            throw new RuntimeException("Solde insuffisant pour le virement");
        }

        if (montant <= 0) {
            throw new RuntimeException("Le montant doit être positif");
        }

        if (numCompteSource.equals(numCompteDest)) {
            throw new RuntimeException("Virement impossible vers le même compte");
        }

        // 3. Mise à jour des soldes dans les objets
        source.setSolde(source.getSolde() - montant);
        destination.setSolde(destination.getSolde() + montant);

        // 4. Sauvegarde des comptes mis à jour
        compteRepository.save(source);
        compteRepository.save(destination);

        // 5. Création et enregistrement de la transaction
        Transaction virement = Transaction.builder()
                .description(description)
                .typeTransaction(TypeTransaction.VIREMENT)
                .transactionDate(LocalDate.now())
                .montant(montant)
                .compteEnvoie(source)
                .compteDestination(destination)
                .build();

        return transactionRepository.save(virement);
    }

    @Override
    @Transactional
    public Transaction effectuerDepot(String numCompte, Double montant, String description) {

        // 1. Verification critique
        if (montant <= 0) {
            throw new RuntimeException("Le montant doit être positif");
        }

        // 2. Trouver le compte
        Compte compte = compteRepository.findByNumCompte(numCompte)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        // 3. Augmenter le solde
        compte.setSolde(compte.getSolde() + montant);
        compteRepository.save(compte);

        // 4. Créer l'historique (Note : compteEnvoie est null ici)
        Transaction depot = Transaction.builder()
                .description(description)
                .typeTransaction(TypeTransaction.DEPOT)
                .transactionDate(LocalDate.now())
                .montant(montant)
                .compteDestination(compte)
                .build();

        return transactionRepository.save(depot);
    }

    @Override
    @Transactional
    public Transaction effectuerRetrait(String numCompte, Double montant, String description) {

        // 1. Verification critique
        if (montant <= 0) {
            throw new RuntimeException("Le montant doit être positif");
        }

        // 2. Trouver le compte
        Compte compte = compteRepository.findByNumCompte(numCompte)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        // 3. LA SÉCURITÉ : Vérifier si le retrait est possible
        if (compte.getSolde() < montant) {
            throw new RuntimeException("Solde insuffisant pour effectuer ce retrait");
        }

        // 4. Mise à jour du solde
        compte.setSolde(compte.getSolde() - montant);
        compteRepository.save(compte);

        // 5. Enregistrement de la transaction (Ici, c'est un compte d'envoi/source)
        Transaction retrait = Transaction.builder()
                .description(description)
                .typeTransaction(TypeTransaction.RETRAIT)
                .transactionDate(LocalDate.now())
                .montant(montant)
                .compteEnvoie(compte)
                .build();

        return transactionRepository.save(retrait);
    }

    public ReleveBancaireDTO genererReleve(String numCompte) {

        // 1. Récupérer le compte
        Compte compte = compteRepository.findByNumCompte(numCompte)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        // 2. Récupérer les transactions (Assure-toi que cette méthode existe dans le Repository)
        List<Transaction> transactions = transactionRepository
                .findByCompteEnvoie_NumCompteOrCompteDestination_NumCompte(numCompte, numCompte);

        // 3. Construction du DTO avec le Builder (plus sécurisé)
        return ReleveBancaireDTO.builder()
                .numCompte(compte.getNumCompte())
                .nomClient(compte.getProprietaireCompte().getNom())
                .prenomClient(compte.getProprietaireCompte().getPrenom())
                .email(compte.getProprietaireCompte().getEmail())
                .telephoneClient(compte.getProprietaireCompte().getTelephone())
                .sexe(compte.getProprietaireCompte().getSexe())
                .solde(compte.getSolde())
                .transactions(transactions)
                .build();
    }

    public ReleveBancaireDTO genererReleveParPeriode(String numCompte, LocalDateTime debut, LocalDateTime fin) {
        // 1. On récupère le compte
        Compte compte = compteRepository.findByNumCompte(numCompte)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        // 2. On récupère les transactions filtrées
        List<Transaction> transactions = transactionRepository.findTransactionsByPeriode(numCompte, debut, fin);

        // 3. On construit le DTO
        return ReleveBancaireDTO.builder()
                .numCompte(compte.getNumCompte())
                .nomClient(compte.getProprietaireCompte().getNom())
                .prenomClient(compte.getProprietaireCompte().getPrenom())
                .solde(compte.getSolde())
                .transactions(transactions)
                .build();
    }

}
