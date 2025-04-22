package ma.staage.gestiondtv.repositories;

import ma.staage.gestiondtv.models.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    @Query("SELECT COUNT(o) > 0 FROM Operation o WHERE o.navire.numeroEscale = :numeroEscale")
    boolean existsByNavireNumeroEscale(@Param("numeroEscale") String numeroEscale);

    @Query("SELECT COUNT(o) > 0 FROM Operation o WHERE o.navire.numeroEscale = :numeroEscale AND o.id != :operationId")
    boolean existsByNavireNumeroEscaleAndIdNot(@Param("numeroEscale") String numeroEscale, @Param("operationId") Long operationId);

    List<Operation> findByNavireId(Long id);
}