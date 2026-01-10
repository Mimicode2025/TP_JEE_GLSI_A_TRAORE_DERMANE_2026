package traore.com.system_gestion_ega.Service.Implementation;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import traore.com.system_gestion_ega.Model.Transaction;
import traore.com.system_gestion_ega.Repository.TransactionRepository;
import traore.com.system_gestion_ega.Service.TransactionServices;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionServiceImplementation implements TransactionServices {
    private final TransactionRepository transactionRepository;

    public TransactionServiceImplementation(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
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

    public List<Transaction> getTransactionsByNumCompte(String numCompte) {
        return transactionRepository
                .findByCompteEnvoie_NumCompteOrCompteDestination_NumCompte(
                        numCompte,
                        numCompte
                );
    }

    public List<Transaction> findTransactionsByPeriode(String numCompte, LocalDateTime dateDebut, LocalDateTime dateFin) {
        // On appelle le repository avec le numéro de compte et les deux dates
        return transactionRepository.findTransactionsByPeriode(
                numCompte,
                dateDebut,
                dateFin
        );
    }
}
