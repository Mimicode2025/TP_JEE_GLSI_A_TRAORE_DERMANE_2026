package traore.com.system_gestion_ega.controller;

import org.springframework.web.bind.annotation.*;
import traore.com.system_gestion_ega.Model.Compte;
import traore.com.system_gestion_ega.Service.Implementation.CompteServiceImplementation;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/compte")
public class CompteController {
    private final CompteServiceImplementation compteServiceImplementation;

    public CompteController(CompteServiceImplementation compteServiceImplementation) {
        this.compteServiceImplementation = compteServiceImplementation;
    }

    @PostMapping("/create")
    public Compte createClient(@RequestBody Compte compte) {
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


    @GetMapping
    public List<Compte> getAllCompte() {
        return compteServiceImplementation.getAllCompte();
    }
}
