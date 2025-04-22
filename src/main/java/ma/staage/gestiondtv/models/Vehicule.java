package ma.staage.gestiondtv.models;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "vehicules")
public class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 14, nullable = false, unique = true)
    private String numeroIdentification;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateArrivee;

    private String portArrivee;

    private String statut;

    @ManyToOne
    @JoinColumn(name = "navire_id")
    private Navire navire;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    // Explicit getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroIdentification() {
        return numeroIdentification;
    }

    public void setNumeroIdentification(String numeroIdentification) {
        this.numeroIdentification = numeroIdentification;
    }

    public Date getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(Date dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public String getPortArrivee() {
        return portArrivee;
    }

    public void setPortArrivee(String portArrivee) {
        this.portArrivee = portArrivee;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Navire getNavire() {
        return navire;
    }

    public void setNavire(Navire navire) {
        this.navire = navire;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}