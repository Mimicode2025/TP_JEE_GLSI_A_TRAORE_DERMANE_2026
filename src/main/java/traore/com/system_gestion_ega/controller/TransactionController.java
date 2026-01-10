package traore.com.system_gestion_ega.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import traore.com.system_gestion_ega.Model.Transaction;
import traore.com.system_gestion_ega.Service.Implementation.CompteServiceImplementation;
import traore.com.system_gestion_ega.Service.Implementation.PdfServiceImplementation;
import traore.com.system_gestion_ega.Service.Implementation.TransactionServiceImplementation;
import traore.com.system_gestion_ega.dto.ReleveBancaireDTO;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("api/transaction")
public class TransactionController {
    private final TransactionServiceImplementation transactionServiceImplementation;
    private final PdfServiceImplementation pdfServiceImplementation;
    private final CompteServiceImplementation compteServiceImplementation;

    public TransactionController(TransactionServiceImplementation transactionServiceImplementation, PdfServiceImplementation pdfServiceImplementation, CompteServiceImplementation compteServiceImplementation) {
        this.transactionServiceImplementation = transactionServiceImplementation;
        this.pdfServiceImplementation = pdfServiceImplementation;
        this.compteServiceImplementation = compteServiceImplementation;
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


    @GetMapping("/transactions")
    public List<Transaction> getAllTransaction() {
        return transactionServiceImplementation.getAllTransaction();
    }

    @GetMapping("/releve/pdf/{numCompte}")
    public ResponseEntity<byte[]> downloadRelevePdf(@PathVariable String numCompte) {
        try {
            // Utilisation du nom exact de ton service injecté
            ReleveBancaireDTO releve = compteServiceImplementation.genererReleve(numCompte);

            // Appel via "pdfServiceImplementation" comme déclaré en haut
            byte[] pdfBytes = pdfServiceImplementation.genererRelevePdf(releve);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            // On personnalise le nom du fichier avec le numéro de compte
            headers.setContentDispositionFormData("attachment", "releve_" + numCompte + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            // Affiche l'erreur dans la console pour t'aider à débugger si besoin
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/releve/json/{numCompte}")
    public ResponseEntity<ReleveBancaireDTO> getReleveJson(@PathVariable String numCompte) {
        try {
            // On récupère les données structurées
            ReleveBancaireDTO releve = compteServiceImplementation.genererReleve(numCompte);

            // Spring transforme automatiquement "releve" en JSON
            return ResponseEntity.ok(releve);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/releve/periode/{numCompte}")
    public ResponseEntity<ReleveBancaireDTO> getRelevePeriode(
            @PathVariable String numCompte,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(compteServiceImplementation.genererReleveParPeriode(numCompte, debut, fin));
    }

    @GetMapping("/periode/{numCompte}")
    public ResponseEntity<List<Transaction>> getTransactionsByPeriode(
            @PathVariable String numCompte,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        try {
            List<Transaction> transactions = transactionServiceImplementation.findTransactionsByPeriode(numCompte, debut, fin);

            if (transactions.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
