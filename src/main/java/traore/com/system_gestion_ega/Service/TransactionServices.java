package traore.com.system_gestion_ega.Service;

import traore.com.system_gestion_ega.Model.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionServices {
    List<Transaction> getAllTransaction();
    Optional<Transaction> getTransactionById(Long id);
    void deleteTransactionById(Long id);
    Transaction updateTransactionById(Long id, Transaction transaction);
    Transaction createTransaction(Transaction transaction);
    List<Transaction> getTransactionsByNumCompte(String numCompte);
    List<Transaction> findTransactionsByPeriode(String numCompte, LocalDateTime dateDebut, LocalDateTime dateFin);
}
