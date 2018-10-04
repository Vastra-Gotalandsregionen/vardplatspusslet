package se.vgregion.vardplatspusslet.domain.json;

import se.vgregion.vardplatspusslet.domain.jpa.Bed;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Patient;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UnitDTO {

    private String id;

    private Clinic clinic;

    private String name;

    private List<Bed> beds = new ArrayList<>();

    private Set<Patient> vacantPatients = new LinkedHashSet<>();

    private Boolean hasLeftDateFeature;

    public UnitDTO() {
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

    public Set<Patient> getVacantPatients() {
        return vacantPatients;
    }

    public void setVacantPatients(Set<Patient> vacantPatients) {
        this.vacantPatients = vacantPatients;
    }

    public Boolean getHasLeftDateFeature() {
        return hasLeftDateFeature;
    }

    public void setHasLeftDateFeature(Boolean hasLeftDateFeature) {
        this.hasLeftDateFeature = hasLeftDateFeature;
    }
}
