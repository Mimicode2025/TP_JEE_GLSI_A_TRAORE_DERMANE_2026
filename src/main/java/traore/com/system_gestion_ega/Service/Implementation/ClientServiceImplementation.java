package traore.com.system_gestion_ega.Service.Implementation;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import traore.com.system_gestion_ega.Enum.Sexe;
import traore.com.system_gestion_ega.Model.Client;
import traore.com.system_gestion_ega.Repository.ClientRepository;
import traore.com.system_gestion_ega.Service.ClientService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientServiceImplementation implements ClientService {
    private final ClientRepository clientRepository;

    public ClientServiceImplementation(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    public void deleteClientById(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public Client updateClientById(Long id, Client client) {
        Client clientSave = clientRepository.findById(id).orElseThrow(()-> new RuntimeException("Client not found"));
        clientSave.setNom(client.getNom());
        clientSave.setPrenom(client.getPrenom());
        clientSave.setSexe(client.getSexe());
        clientSave.setEmail(client.getEmail());
        clientSave.setTelephone(client.getTelephone());
        clientSave.setAdresse(client.getAdresse());
        clientSave.setDateNaissance(client.getDateNaissance());
        clientSave.setNationalite(client.getNationalite());
        return clientRepository.save(clientSave);
    }

    @Override
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public List<Client> findByNomOrPrenom(String query) {
        return clientRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(query, query);
    }

    @Override
    public List<Client> findByNationalite(String nationalite) {
        return clientRepository.findByNationaliteIgnoreCase(nationalite);
    }

    @Override
    public List<Client> findBySexe(String sexe) {
        try {
            // Convertit le String en Enum, insensible à la casse
            Sexe sexeEnum = Sexe.valueOf(sexe.toUpperCase());
            return clientRepository.findBySexe(sexeEnum);
        } catch (IllegalArgumentException e) {
            // Si la valeur passée n'existe pas dans l'enum, retourne une liste vide
            return List.of();
        }
    }

}
