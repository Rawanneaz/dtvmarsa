package ma.staage.gestiondtv.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "operations")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_operation")
    private Date dateOperation;

    @Column(name = "connaissements_count")
    private Integer connaissementsCount;

    @Column(name = "car_count")
    private Integer carCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "navire_id")
    private Navire navire;

    // Constructeurs
    public Operation() {
    }

    public Operation(Date dateOperation, Integer connaissementsCount, Integer carCount, Client client, Navire navire) {
        this.dateOperation = dateOperation;
        this.connaissementsCount = connaissementsCount;
        this.carCount = carCount;
        this.client = client;
        this.navire = navire;
    }

    // Getters et Setters
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Navire getNavire() {
        return navire;
    }

    public void setNavire(Navire navire) {
        this.navire = navire;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", dateOperation=" + dateOperation +
                ", connaissementsCount=" + connaissementsCount +
                ", carCount=" + carCount +
                ", client=" + (client != null ? client.getName() : "null") +
                ", navire=" + (navire != null ? navire.getName() : "null") +
                '}';
    }
}