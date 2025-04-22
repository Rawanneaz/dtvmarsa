package ma.staage.gestiondtv.Controller;

import ma.staage.gestiondtv.dto.ClientDTO;
import ma.staage.gestiondtv.mapper.EntityMapper;
import ma.staage.gestiondtv.models.Client;
import ma.staage.gestiondtv.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EntityMapper entityMapper;

    @GetMapping
    public List<ClientDTO> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(entityMapper::clientToClientDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ClientDTO createClient(@RequestBody ClientDTO clientDTO) {
        // Convert DTO to entity
        Client client = entityMapper.clientDTOToClient(clientDTO);
        // Save the entity
        Client savedClient = clientRepository.save(client);
        // Convert saved entity back to DTO
        return entityMapper.clientToClientDTO(savedClient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        // Check if client exists
        if (!clientRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Convert DTO to entity and set ID
        Client client = entityMapper.clientDTOToClient(clientDTO);
        client.setId(id);

        // Save the updated entity
        Client updatedClient = clientRepository.save(client);

        // Convert entity back to DTO
        ClientDTO updatedClientDTO = entityMapper.clientToClientDTO(updatedClient);

        return ResponseEntity.ok(updatedClientDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        // Check if client exists
        if (!clientRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        clientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        Optional<Client> client = clientRepository.findById(id);

        return client.map(value -> ResponseEntity.ok(entityMapper.clientToClientDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}