package traore.com.system_gestion_ega.controller;

import org.springframework.web.bind.annotation.*;
import traore.com.system_gestion_ega.Model.Client;
import traore.com.system_gestion_ega.Service.Implementation.ClientServiceImplementation;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/client")
public class ClientController {
    private final ClientServiceImplementation clientServiceImplementation;

    public ClientController(ClientServiceImplementation clientServiceImplementation) {
        this.clientServiceImplementation = clientServiceImplementation;
    }


    @PostMapping("/create")
    public Client createClient(@RequestBody Client client) {
        return clientServiceImplementation.createClient(client);
    }

    @PutMapping("/update/{id}")
    public Client updateClientById(@PathVariable Long id, @RequestBody Client client) {
        return clientServiceImplementation.updateClientById(id, client);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteClient(@PathVariable Long id) {
        clientServiceImplementation.deleteClientById(id);
    }


    @GetMapping
    public List<Client> getAllClient() {
        return clientServiceImplementation.getAllClients();
    }

}
