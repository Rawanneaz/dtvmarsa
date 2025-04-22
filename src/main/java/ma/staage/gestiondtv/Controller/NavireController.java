package ma.staage.gestiondtv.Controller;

import ma.staage.gestiondtv.dto.NavireDTO;
import ma.staage.gestiondtv.mapper.EntityMapper;
import ma.staage.gestiondtv.models.Navire;
import ma.staage.gestiondtv.models.Operation;
import ma.staage.gestiondtv.repositories.NavireRepository;
import ma.staage.gestiondtv.repositories.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/navires")
public class NavireController {

    @Autowired
    private NavireRepository navireRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private EntityMapper entityMapper;

    @GetMapping
    public List<NavireDTO> getAllNavires() {
        List<Navire> navires = navireRepository.findAll();
        return navires.stream()
                .map(entityMapper::navireToNavireDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NavireDTO> getNavireById(@PathVariable Long id) {
        Optional<Navire> navireOptional = navireRepository.findById(id);
        if(navireOptional.isPresent()) {
            NavireDTO navireDTO = entityMapper.navireToNavireDTO(navireOptional.get());
            return ResponseEntity.ok(navireDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public NavireDTO createNavire(@RequestBody NavireDTO navireDTO) {
        Navire navire = entityMapper.navireDTOToNavire(navireDTO);
        Navire savedNavire = navireRepository.save(navire);
        return entityMapper.navireToNavireDTO(savedNavire);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NavireDTO> updateNavire(@PathVariable Long id, @RequestBody NavireDTO navireDTO) {
        if (!navireRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        navireDTO.setId(id);
        Navire navire = entityMapper.navireDTOToNavire(navireDTO);
        Navire updatedNavire = navireRepository.save(navire);
        return ResponseEntity.ok(entityMapper.navireToNavireDTO(updatedNavire));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNavire(@PathVariable Long id) {
        if (!navireRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Check if navire is referenced by any operations
        List<Operation> referencingOperations = operationRepository.findByNavireId(id);
        if (!referencingOperations.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Ce navire ne peut pas être supprimé car il est utilisé dans une ou plusieurs opérations.");
        }

        try {
            navireRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression du navire: " + e.getMessage());
        }
    }
}