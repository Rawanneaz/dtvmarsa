package ma.staage.gestiondtv.repositories;

import ma.staage.gestiondtv.models.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
    Optional<Vehicule> findByNumeroIdentification(String numeroIdentification);
    List<Vehicule> findByNumeroIdentificationContaining(String numeroIdentification);
    List<Vehicule> findByPortArriveeContaining(String portArrivee);
    List<Vehicule> findByStatut(String statut);

    @Query("SELECT v.portArrivee, COUNT(v) FROM Vehicule v GROUP BY v.portArrivee")
    List<Object[]> countVehiculesByPort();

    @Query("SELECT v.statut, COUNT(v) FROM Vehicule v GROUP BY v.statut")
    List<Object[]> countVehiculesByStatut();
}