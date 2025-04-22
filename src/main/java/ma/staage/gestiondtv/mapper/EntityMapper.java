package ma.staage.gestiondtv.mapper;

import ma.staage.gestiondtv.dto.*;
import ma.staage.gestiondtv.models.Client;
import ma.staage.gestiondtv.models.Navire;
import ma.staage.gestiondtv.models.Operation;
import ma.staage.gestiondtv.models.Vehicule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EntityMapper {

    ClientDTO clientToClientDTO(Client client);
    Client clientDTOToClient(ClientDTO clientDTO);

    NavireDTO navireToNavireDTO(Navire navire);
    Navire navireDTOToNavire(NavireDTO navireDTO);

    OperationDTO operationToOperationDTO(Operation operation);
    Operation operationDTOToOperation(OperationDTO operationDTO);

    void updateOperationFromDTO(OperationDTO dto, @MappingTarget Operation operation);

    VehiculeDTO vehiculeToVehiculeDTO(Vehicule vehicule);
    Vehicule vehiculeDTOToVehicule(VehiculeDTO vehiculeDTO);

    // Create a default implementation for mapping VehiculeArriveeDTO to Vehicule
    default Vehicule vehiculeArriveeToVehicule(VehiculeArriveeDTO arriveeDTO) {
        if (arriveeDTO == null) {
            return null;
        }

        Vehicule vehicule = new Vehicule();
        vehicule.setNumeroIdentification(arriveeDTO.getNumeroIdentification());
        vehicule.setDateArrivee(arriveeDTO.getDateArrivee());
        vehicule.setPortArrivee(arriveeDTO.getPortArrivee());
        vehicule.setStatut("ARRIVÉ");

        if (arriveeDTO.getNavireId() != null) {
            Navire navire = new Navire();
            navire.setId(arriveeDTO.getNavireId());
            vehicule.setNavire(navire);
        }

        if (arriveeDTO.getClientId() != null) {
            Client client = new Client();
            client.setId(arriveeDTO.getClientId());
            vehicule.setClient(client);
        }

        return vehicule;
    }

    void updateVehiculeFromDTO(VehiculeDTO dto, @MappingTarget Vehicule vehicule);
}