package traore.com.system_gestion_ega.Service;

import traore.com.system_gestion_ega.Model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionServices {
    List<Transaction> getAllTransaction();
    Optional<Transaction> getTransactionById(Long id);
    void deleteTransactionById(Long id);
    Transaction updateTransactionById(Long id, Transaction transaction);
    Transaction createTransaction(Transaction transaction);
    public Transaction effectuerDepot(String numCompte, Double montant, String description);
    public Transaction effectuerRetrait(String numCompte, Double montant, String description);
    public Transaction effectuerVirement(String numCompteSource, String numCompteDest, Double montant, String description );
}
