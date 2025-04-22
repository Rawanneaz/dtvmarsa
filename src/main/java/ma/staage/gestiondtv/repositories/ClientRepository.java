package ma.staage.gestiondtv.repositories;

import ma.staage.gestiondtv.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
