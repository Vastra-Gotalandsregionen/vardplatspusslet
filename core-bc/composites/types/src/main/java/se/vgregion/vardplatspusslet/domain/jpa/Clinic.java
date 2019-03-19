package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.*;

/**
 * @author Patrik Björk
 */
@Entity
@Table(name = "clinic")
public class Clinic implements Comparable<Clinic>{

    @Id
    private String id;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Management management;

    public Clinic() {
    }

    public Clinic(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Management getManagement() {
        return management;
    }

    public void setManagement(Management management) {
        this.management = management;
    }

    @Override
    public int compareTo(Clinic o) {
        return this.getId().compareTo(o.getId());
    }
}
