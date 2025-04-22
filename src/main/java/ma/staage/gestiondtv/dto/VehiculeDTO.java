package ma.staage.gestiondtv.dto;


import lombok.Data;
import java.util.Date;

@Data
public class VehiculeDTO {
    private Long id;
    private String numeroIdentification;
    private Date dateArrivee;
    private String portArrivee;
    private String statut;
    private NavireDTO navire;
    private ClientDTO client;
}