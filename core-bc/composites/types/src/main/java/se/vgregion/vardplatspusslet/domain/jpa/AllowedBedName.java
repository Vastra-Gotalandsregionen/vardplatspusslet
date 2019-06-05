package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.*;

@Entity
@Table(name = "allowedBedName")
public class AllowedBedName {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String name;

    public AllowedBedName() {
    }

    public AllowedBedName(Long id, String name) {
        this.id = id;
        this.name = name;
    }

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
}
