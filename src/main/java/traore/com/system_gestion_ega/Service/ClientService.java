package traore.com.system_gestion_ega.Service;

import traore.com.system_gestion_ega.Model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    List<Client> getAllClients();
    Optional<Client> getClientById(Long id);
    void deleteClientById(Long id);
    Client updateClientById(Long id, Client client);
    Client createClient(Client client);
}
