package traore.com.system_gestion_ega.controller;

import org.springframework.web.bind.annotation.*;
import traore.com.system_gestion_ega.Model.Compte;
import traore.com.system_gestion_ega.Model.Transaction;
import traore.com.system_gestion_ega.Service.Implementation.CompteServiceImplementation;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("api/compte")
public class CompteController {
    private final CompteServiceImplementation compteServiceImplementation;

    public CompteController(CompteServiceImplementation compteServiceImplementation) {
        this.compteServiceImplementation = compteServiceImplementation;
    }

    @PostMapping("/create")
    public Compte createCompte(@RequestBody Compte compte) {
        return compteServiceImplementation.createCompte(compte);
    }

    @PutMapping("/update/{id}")
    public Compte updateCompteById(@PathVariable Long id, @RequestBody Compte compte) {
        return compteServiceImplementation.updateCompteById(id, compte);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCompte(@PathVariable Long id) {
        compteServiceImplementation.deleteCompteById(id);
    }


    @GetMapping("/comptes")
    public List<Compte> getAllCompte() {
        return compteServiceImplementation.getAllCompte();
    }

    @PostMapping("/depot")
    public Transaction depot(
            @RequestParam String numCompte,
            @RequestParam Double montant,
            @RequestParam String description) {
        return compteServiceImplementation.effectuerDepot(numCompte, montant, description);
    }

    @PostMapping("/retrait")
    public Transaction retrait(
            @RequestParam String numCompte,
            @RequestParam Double montant,
            @RequestParam String description) {
        return compteServiceImplementation.effectuerRetrait(numCompte, montant, description);
    }

    @PostMapping("/virement")
    public Transaction virement(@RequestParam String numCompteSource,
                                @RequestParam String numCompteDest,
                                @RequestParam Double montant,
                                @RequestParam String description) {
        return compteServiceImplementation.effectuerVirement(numCompteSource, numCompteDest, montant, description);
    }



}
