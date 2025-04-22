package ma.staage.gestiondtv.dto;

import java.util.Date;

public class OperationDTO {
    private Long id;
    private Date dateOperation;
    private Integer connaissementsCount;
    private Integer carCount;
    private NavireDTO navire;
    private ClientDTO client;

    // Default constructor needed by MapStruct
    public OperationDTO() {
    }

    // Constructor with all fields
    public OperationDTO(Long id, Date dateOperation, Integer connaissementsCount, Integer carCount, NavireDTO navire, ClientDTO client) {
        this.id = id;
        this.dateOperation = dateOperation;
        this.connaissementsCount = connaissementsCount;
        this.carCount = carCount;
        this.navire = navire;
        this.client = client;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(Date dateOperation) {
        this.dateOperation = dateOperation;
    }

    public Integer getConnaissementsCount() {
        return connaissementsCount;
    }

    public void setConnaissementsCount(Integer connaissementsCount) {
        this.connaissementsCount = connaissementsCount;
    }

    public Integer getCarCount() {
        return carCount;
    }

    public void setCarCount(Integer carCount) {
        this.carCount = carCount;
    }

    public NavireDTO getNavire() {
        return navire;
    }

    public void setNavire(NavireDTO navire) {
        this.navire = navire;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }
}