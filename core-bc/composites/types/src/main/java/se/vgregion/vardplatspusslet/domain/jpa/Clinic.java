package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Patrik Björk
 */
@Entity
@Table(name = "clinic")
public class Clinic {

    @Id
    private String id;

    @Column
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "clinic")
    private Set<Unit> units = new HashSet<>();

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

    public Set<Unit> getUnits() {
        return units;
    }

    public void setUnits(Set<Unit> units) {
        this.units = units;
    }
}
