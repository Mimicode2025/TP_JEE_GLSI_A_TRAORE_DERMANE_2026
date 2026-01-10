package traore.com.system_gestion_ega.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import traore.com.system_gestion_ega.Model.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findTransactionById(Long id);

    List<Transaction> findByCompteEnvoie_NumCompteOrCompteDestination_NumCompte(
            String source,
            String destination
    );

    @Query("SELECT t FROM Transaction t WHERE " +
            "(t.compteEnvoie.numCompte = :numCompte OR t.compteDestination.numCompte = :numCompte) " +
            "AND t.transactionDate BETWEEN :debut AND :fin " +
            "ORDER BY t.transactionDate DESC")
    List<Transaction> findTransactionsByPeriode(
            @Param("numCompte") String numCompte,
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin);

}
