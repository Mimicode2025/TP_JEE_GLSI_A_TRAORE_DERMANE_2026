package traore.com.system_gestion_ega.Service.Implementation;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import traore.com.system_gestion_ega.Enum.TypeTransaction;
import traore.com.system_gestion_ega.Model.Transaction;
import traore.com.system_gestion_ega.Model.Compte;
import traore.com.system_gestion_ega.Repository.TransactionRepository;
import traore.com.system_gestion_ega.Repository.CompteRepository;
import traore.com.system_gestion_ega.Service.TransactionServices;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionServiceImplementation implements TransactionServices {
    private final TransactionRepository transactionRepository;
    private final CompteRepository compteRepository;

    public TransactionServiceImplementation(TransactionRepository transactionRepository, CompteRepository compteRepository) {
        this.transactionRepository = transactionRepository;
        this.compteRepository = compteRepository;

    }


    @Override
    public List<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public void deleteTransactionById(Long id) {
        transactionRepository.deleteById(id);
    }

    public Transaction updateTransactionById(Long id, Transaction transaction) {
        Transaction transactionSave = transactionRepository.findById(id).orElseThrow(()-> new RuntimeException("Transaction not found"));
        transactionSave.setTransactionDate(transaction.getTransactionDate());
        transactionSave.setTypeTransaction(transaction.getTypeTransaction());
        transactionSave.setCompteDestination(transaction.getCompteDestination());
        transactionSave.setCompteEnvoie(transaction.getCompteEnvoie());
        transactionRepository.save(transactionSave);
        return transactionSave;

    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public Transaction effectuerVirement(String numCompteSource, String numCompteDest, Double montant, String description) {
        // 1. Récupération des deux comptes
        Compte source = compteRepository.findByNumCompte(numCompteSource)
                .orElseThrow(() -> new RuntimeException("Compte source introuvable"));

        Compte destination = compteRepository.findByNumCompte(numCompteDest)
                .orElseThrow(() -> new RuntimeException("Compte destination introuvable"));

        // 2. LA VÉRIFICATION CRITIQUE : Le solde est-il suffisant ?
        if (source.getSolde() < montant) {
            throw new RuntimeException("Solde insuffisant pour le virement");
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
                .Montant(montant)
                .compteEnvoie(source)
                .compteDestination(destination)
                .build();

        return transactionRepository.save(virement);
    }

    @Override
    @Transactional
    public Transaction effectuerDepot(String numCompte, Double montant, String description) {
        // 1. Trouver le compte
        Compte compte = compteRepository.findByNumCompte(numCompte)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        // 2. Augmenter le solde
        compte.setSolde(compte.getSolde() + montant);
        compteRepository.save(compte);

        // 3. Créer l'historique (Note : compteEnvoie est null ici)
        Transaction depot = Transaction.builder()
                .description(description)
                .typeTransaction(TypeTransaction.DEPOT)
                .transactionDate(LocalDate.now())
                .Montant(montant)
                .compteDestination(compte)
                .build();

        return transactionRepository.save(depot);
    }

    @Override
    @Transactional
    public Transaction effectuerRetrait(String numCompte, Double montant, String description) {
        // 1. Trouver le compte
        Compte compte = compteRepository.findByNumCompte(numCompte)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        // 2. LA SÉCURITÉ : Vérifier si le retrait est possible
        if (compte.getSolde() < montant) {
            throw new RuntimeException("Solde insuffisant pour effectuer ce retrait");
        }

        // 3. Mise à jour du solde
        compte.setSolde(compte.getSolde() - montant);
        compteRepository.save(compte);

        // 4. Enregistrement de la transaction (Ici, c'est un compte d'envoi/source)
        Transaction retrait = Transaction.builder()
                .description(description)
                .typeTransaction(TypeTransaction.RETRAIT) // Assure-toi que RETRAIT est dans ton Enum
                .transactionDate(LocalDate.now())
                .Montant(montant)
                .compteEnvoie(compte)
                .build();

        return transactionRepository.save(retrait);
    }
}
