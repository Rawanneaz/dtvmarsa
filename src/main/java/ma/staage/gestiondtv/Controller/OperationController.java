package ma.staage.gestiondtv.Controller;

import ma.staage.gestiondtv.dto.OperationDTO;
import ma.staage.gestiondtv.mapper.EntityMapper;
import ma.staage.gestiondtv.models.Client;
import ma.staage.gestiondtv.models.Navire;
import ma.staage.gestiondtv.models.Operation;
import ma.staage.gestiondtv.repositories.ClientRepository;
import ma.staage.gestiondtv.repositories.NavireRepository;
import ma.staage.gestiondtv.repositories.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/operations")
public class OperationController {

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private NavireRepository navireRepository;

    @Autowired
    private EntityMapper entityMapper;

    @PostMapping
    public ResponseEntity<?> createOperation(@RequestBody OperationDTO operationDTO) {
        try {
            // Verify navire exists
            if (operationDTO.getNavire() == null || operationDTO.getNavire().getId() == null) {
                return ResponseEntity.badRequest().body("Navire is required");
            }

            // Get navire to check numeroEscale
            Navire navire = navireRepository.findById(operationDTO.getNavire().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Navire not found"));

            // Check if an operation with this numeroEscale already exists
            if (operationRepository.existsByNavireNumeroEscale(navire.getNumeroEscale())) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Operation with numero d'escale '" + navire.getNumeroEscale() + "' already exists");
            }

            Operation operation = new Operation();

            // Handle date conversion properly
            operation.setDateOperation(operationDTO.getDateOperation());
            operation.setConnaissementsCount(operationDTO.getConnaissementsCount());
            operation.setCarCount(operationDTO.getCarCount());

            // Set Client
            if (operationDTO.getClient() != null && operationDTO.getClient().getId() != null) {
                Client client = clientRepository.findById(operationDTO.getClient().getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
                operation.setClient(client);
            }

            // Set Navire
            operation.setNavire(navire);

            // Save operation
            Operation savedOperation = operationRepository.save(operation);
            return ResponseEntity.ok(entityMapper.operationToOperationDTO(savedOperation));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<OperationDTO> getAllOperations() {
        List<Operation> operations = operationRepository.findAll();
        return operations.stream()
                .map(entityMapper::operationToOperationDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperationDTO> getOperationById(@PathVariable Long id) {
        return operationRepository.findById(id)
                .map(entityMapper::operationToOperationDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOperation(@PathVariable Long id, @RequestBody OperationDTO operationDTO) {
        return operationRepository.findById(id)
                .map(operation -> {
                    try {
                        // Handle date conversion properly
                        operation.setDateOperation(operationDTO.getDateOperation());
                        operation.setConnaissementsCount(operationDTO.getConnaissementsCount());
                        operation.setCarCount(operationDTO.getCarCount());

                        // Update Client
                        if (operationDTO.getClient() != null && operationDTO.getClient().getId() != null) {
                            Client client = clientRepository.findById(operationDTO.getClient().getId())
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
                            operation.setClient(client);
                        }

                        // Update Navire
                        if (operationDTO.getNavire() != null && operationDTO.getNavire().getId() != null) {
                            Navire navire = navireRepository.findById(operationDTO.getNavire().getId())
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Navire not found"));

                            // Check if changing to a numeroEscale that already exists in another operation
                            if (!navire.getNumeroEscale().equals(operation.getNavire().getNumeroEscale()) &&
                                    operationRepository.existsByNavireNumeroEscale(navire.getNumeroEscale())) {
                                return ResponseEntity
                                        .status(HttpStatus.CONFLICT)
                                        .body("Operation with numero d'escale '" + navire.getNumeroEscale() + "' already exists");
                            }

                            operation.setNavire(navire);
                        }

                        Operation updatedOperation = operationRepository.save(operation);
                        return ResponseEntity.ok(entityMapper.operationToOperationDTO(updatedOperation));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ResponseEntity.badRequest().body(e.getMessage());
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOperation(@PathVariable Long id) {
        return operationRepository.findById(id)
                .map(operation -> {
                    operationRepository.delete(operation);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}