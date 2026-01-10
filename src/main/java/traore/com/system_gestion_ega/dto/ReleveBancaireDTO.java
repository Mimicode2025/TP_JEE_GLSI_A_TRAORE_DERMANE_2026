package traore.com.system_gestion_ega.dto;

import lombok.*;
import traore.com.system_gestion_ega.Enum.Sexe;
import traore.com.system_gestion_ega.Model.Transaction;

import java.util.List;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReleveBancaireDTO {
    private String numCompte;
    private String nomClient;
    private String prenomClient;
    private Sexe sexe;
    private String telephoneClient;
    private String email;
    private Double solde;
    private List<Transaction> transactions;
}
