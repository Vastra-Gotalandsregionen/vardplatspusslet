package se.vgregion.vardplatspusslet.domain.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Patrik Björk
 */
@Entity
@Table(name = "unit")
public class Unit {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Clinic clinic;

    @Column
    private String name;

    @JsonIgnore
    @OrderColumn(name = "order_")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Bed> beds = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    private List<Patient> patients = new ArrayList<>();

    @Column
    private Boolean hasLeftDateFeature;

    public Unit() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Bed> getBeds() {
        return beds;
    }

    public void setBeds(List<Bed> beds) {
        this.beds = beds;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> vacantPatients) {
        this.patients = vacantPatients;
    }

    public Boolean getHasLeftDateFeature() {
        return hasLeftDateFeature;
    }

    public void setHasLeftDateFeature(Boolean hasLeftDateFeature) {
        this.hasLeftDateFeature = hasLeftDateFeature;
    }
}
