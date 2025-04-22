package ma.staage.gestiondtv.dto;

import lombok.Data;
import java.util.Date;

@Data
public class VehiculeArriveeDTO {
    private String numeroIdentification;
    private Date dateArrivee;
    private String portArrivee;
    private Long navireId;
    private Long clientId;
}