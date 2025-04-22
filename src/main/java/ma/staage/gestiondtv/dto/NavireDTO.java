package ma.staage.gestiondtv.dto;

public class NavireDTO {
    private Long id;
    private String name;
    private String numeroEscale;

    // Constructor
    public NavireDTO() {
    }

    public NavireDTO(Long id, String name, String numeroEscale) {
        this.id = id;
        this.name = name;
        this.numeroEscale = numeroEscale;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumeroEscale() {
        return numeroEscale;
    }

    public void setNumeroEscale(String numeroEscale) {
        this.numeroEscale = numeroEscale;
    }
}