package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.*;


@Entity
@Table(name = "cleaningalternative")
public class CleaningAlternative {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String description;

    public CleaningAlternative() {
    }

    public CleaningAlternative(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
