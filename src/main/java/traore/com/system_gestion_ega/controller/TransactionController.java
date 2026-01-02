package traore.com.system_gestion_ega.controller;

import org.springframework.web.bind.annotation.*;
import traore.com.system_gestion_ega.Model.Transaction;
import traore.com.system_gestion_ega.Service.Implementation.TransactionServiceImplementation;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionServiceImplementation transactionServiceImplementation;

    public TransactionController(TransactionServiceImplementation transactionServiceImplementation) {
        this.transactionServiceImplementation = transactionServiceImplementation;
    }

    @PostMapping("/create")
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionServiceImplementation.createTransaction(transaction);
    }

    @PutMapping("/update/{id}")
    public Transaction updateTransactionById(@PathVariable Long id, @RequestBody Transaction transaction) {
        return transactionServiceImplementation.updateTransactionById(id, transaction);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionServiceImplementation.deleteTransactionById(id);
    }


    @GetMapping
    public List<Transaction> getAllTransaction() {
        return transactionServiceImplementation.getAllTransaction();
    }
}
