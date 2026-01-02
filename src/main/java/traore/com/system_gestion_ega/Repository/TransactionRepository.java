package traore.com.system_gestion_ega.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import traore.com.system_gestion_ega.Model.Transaction;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findTransactionById(Long id);
}
