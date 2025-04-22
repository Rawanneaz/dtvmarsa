package ma.staage.gestiondtv.Controller;

import ma.staage.gestiondtv.dto.VehiculeArriveeDTO;
import ma.staage.gestiondtv.dto.VehiculeArriveeDTO;
import ma.staage.gestiondtv.dto.VehiculeDTO;
import ma.staage.gestiondtv.mapper.EntityMapper;
import ma.staage.gestiondtv.models.Vehicule;
import ma.staage.gestiondtv.repositories.VehiculeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vehicules")
@CrossOrigin(origins = "*")
public class VehiculeController {

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private EntityMapper entityMapper;

    @PostMapping("/arrivee")
    public ResponseEntity<?> enregistrerArrivee(@RequestBody VehiculeArriveeDTO arriveeDTO) {
        try {
            // Validation de l'identifiant à 14 caractères
            if (arriveeDTO.getNumeroIdentification() == null ||
                    arriveeDTO.getNumeroIdentification().length() != 14) {
                return ResponseEntity.badRequest().body("L'identifiant du véhicule doit comporter 14 caractères");
            }

            // Vérifier si le véhicule existe déjà
            Optional<Vehicule> existingVehicule = vehiculeRepository.findByNumeroIdentification(arriveeDTO.getNumeroIdentification());
            if (existingVehicule.isPresent()) {
                return ResponseEntity.badRequest().body("Un véhicule avec ce numéro d'identification existe déjà");
            }

            // Créer et enregistrer le nouveau véhicule
            Vehicule vehicule = entityMapper.vehiculeArriveeToVehicule(arriveeDTO);
            vehicule.setStatut("ARRIVÉ");

            if (vehicule.getDateArrivee() == null) {
                vehicule.setDateArrivee(new Date());
            }

            Vehicule savedVehicule = vehiculeRepository.save(vehicule);
            VehiculeDTO vehiculeDTO = entityMapper.vehiculeToVehiculeDTO(savedVehicule);

            return ResponseEntity.ok(vehiculeDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'enregistrement de l'arrivée: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<VehiculeDTO>> getAllVehicules() {
        List<VehiculeDTO> vehicules = vehiculeRepository.findAll().stream()
                .map(entityMapper::vehiculeToVehiculeDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(vehicules);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<VehiculeDTO>> getVehiculesPage(Pageable pageable) {
        Page<VehiculeDTO> vehiculesPage = vehiculeRepository.findAll(pageable)
                .map(entityMapper::vehiculeToVehiculeDTO);
        return ResponseEntity.ok(vehiculesPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVehiculeById(@PathVariable Long id) {
        Optional<Vehicule> vehicule = vehiculeRepository.findById(id);
        if (vehicule.isPresent()) {
            return ResponseEntity.ok(entityMapper.vehiculeToVehiculeDTO(vehicule.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Véhicule non trouvé avec l'ID: " + id);
        }
    }

    @GetMapping("/numero/{numeroIdentification}")
    public ResponseEntity<?> getVehiculeByNumero(@PathVariable String numeroIdentification) {
        Optional<Vehicule> vehicule = vehiculeRepository.findByNumeroIdentification(numeroIdentification);
        if (vehicule.isPresent()) {
            return ResponseEntity.ok(entityMapper.vehiculeToVehiculeDTO(vehicule.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Véhicule non trouvé avec le numéro: " + numeroIdentification);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVehicule(@PathVariable Long id, @RequestBody VehiculeDTO vehiculeDTO) {
        try {
            Optional<Vehicule> optionalVehicule = vehiculeRepository.findById(id);
            if (!optionalVehicule.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Véhicule non trouvé avec l'ID: " + id);
            }

            Vehicule vehicule = optionalVehicule.get();

            // Si le numéro d'identification a changé, vérifier l'unicité
            if (!vehicule.getNumeroIdentification().equals(vehiculeDTO.getNumeroIdentification())) {
                Optional<Vehicule> existingWithSameNum = vehiculeRepository.findByNumeroIdentification(vehiculeDTO.getNumeroIdentification());
                if (existingWithSameNum.isPresent()) {
                    return ResponseEntity.badRequest().body("Un véhicule avec ce numéro d'identification existe déjà");
                }
            }

            entityMapper.updateVehiculeFromDTO(vehiculeDTO, vehicule);
            Vehicule updatedVehicule = vehiculeRepository.save(vehicule);

            return ResponseEntity.ok(entityMapper.vehiculeToVehiculeDTO(updatedVehicule));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour du véhicule: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVehicule(@PathVariable Long id) {
        try {
            if (!vehiculeRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Véhicule non trouvé avec l'ID: " + id);
            }

            vehiculeRepository.deleteById(id);
            return ResponseEntity.ok().body("Véhicule supprimé avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression du véhicule: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<VehiculeDTO>> searchVehicules(
            @RequestParam(required = false) String numeroIdentification,
            @RequestParam(required = false) String portArrivee,
            @RequestParam(required = false) String statut) {

        List<Vehicule> vehicules;

        if (numeroIdentification != null && !numeroIdentification.isEmpty()) {
            vehicules = vehiculeRepository.findByNumeroIdentificationContaining(numeroIdentification);
        } else if (portArrivee != null && !portArrivee.isEmpty()) {
            vehicules = vehiculeRepository.findByPortArriveeContaining(portArrivee);
        } else if (statut != null && !statut.isEmpty()) {
            vehicules = vehiculeRepository.findByStatut(statut);
        } else {
            vehicules = vehiculeRepository.findAll();
        }

        List<VehiculeDTO> result = vehicules.stream()
                .map(entityMapper::vehiculeToVehiculeDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<?> updateStatut(@PathVariable Long id, @RequestParam String nouveauStatut) {
        try {
            Optional<Vehicule> optionalVehicule = vehiculeRepository.findById(id);
            if (!optionalVehicule.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Véhicule non trouvé avec l'ID: " + id);
            }

            // Validation du statut
            List<String> statutsValides = List.of("ARRIVÉ", "EN_TRAITEMENT", "EN_ATTENTE", "INSPECTÉ", "LIBÉRÉ", "SORTI");
            if (!statutsValides.contains(nouveauStatut)) {
                return ResponseEntity.badRequest().body("Statut invalide. Les statuts valides sont: " + String.join(", ", statutsValides));
            }

            Vehicule vehicule = optionalVehicule.get();
            vehicule.setStatut(nouveauStatut);

            Vehicule updatedVehicule = vehiculeRepository.save(vehicule);
            return ResponseEntity.ok(entityMapper.vehiculeToVehiculeDTO(updatedVehicule));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour du statut: " + e.getMessage());
        }
    }

    @GetMapping("/stats/port")
    public ResponseEntity<?> getStatsByPort() {
        try {
            List<Object[]> stats = vehiculeRepository.countVehiculesByPort();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des statistiques: " + e.getMessage());
        }
    }

    @GetMapping("/stats/statut")
    public ResponseEntity<?> getStatsByStatut() {
        try {
            List<Object[]> stats = vehiculeRepository.countVehiculesByStatut();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des statistiques: " + e.getMessage());
        }
    }
}