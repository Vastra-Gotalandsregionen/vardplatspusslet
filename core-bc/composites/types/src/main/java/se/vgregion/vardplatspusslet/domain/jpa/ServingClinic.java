package se.vgregion.vardplatspusslet.domain.jpa;


import javax.persistence.*;

@Entity
@Table(name = "servingclinic")
public class ServingClinic implements Comparable<ServingClinic> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String name;

    public ServingClinic() {
    }

    public ServingClinic(Long id, String name) {
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

    @Override
    public int compareTo(ServingClinic o) {
        return this.name.compareTo(o.name);
    }
}
