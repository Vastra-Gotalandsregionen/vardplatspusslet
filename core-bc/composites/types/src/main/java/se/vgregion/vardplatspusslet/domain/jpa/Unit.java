package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Patrik Björk
 */
@Entity
@Table(name = "unit")
@NamedEntityGraph(name = "Unit.beds", attributeNodes = @NamedAttributeNode("beds"))
public class Unit {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Clinic clinic;

    @OneToMany(fetch = FetchType.EAGER,  cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ServingClinic> servingClinics = new TreeSet<>();

    @OneToMany(fetch = FetchType.EAGER,  cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CleaningAlternative> cleaningAlternatives= new TreeSet<>();;

    @Column
    private String name;

    @OrderColumn(name = "order_")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Bed> beds = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Patient> patients = new TreeSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ssk> ssks = new TreeSet<>();

    @Column
    private Boolean hasLeftDateFeature;

    @Column
    private Boolean hasCarePlan;

    @Column
    private Boolean hasAkutPatientFeature;

    @Column
    private Boolean has23oFeature;

    @Column
    private Boolean has24oFeature;

    @Column
    private Boolean hasVuxenPatientFeature;

    @Column
    private Boolean hasSekretessFeature;

    @Column
    private Boolean hasInfekteradFeature;


    @Column
    private Boolean hasInfectionSensitiveFeature;

    @Column
    private Boolean hasSmittaFeature;

    @Column
    private Boolean hasCleaningFeature;

    @Column
    private Boolean hasPalFeature;

    @Column
    private Boolean hasHendelseFeature;

    @Column
    private  Boolean hasMorRondFeature;

    @Column
    private  Boolean hasBarnRondFeature;

    @Column
    private  Boolean hasRondFeature;

    @Column
    private Boolean hasAmningFeature;

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

    public Set<Patient> getPatients() {
        return patients;
    }

    public void setPatients(Set<Patient> vacantPatients) {
        this.patients = vacantPatients;
    }

    public Set<ServingClinic> getServingClinics() {
        return servingClinics;
    }

    public void setServingClinics(Set<ServingClinic> servingClinics) {
        this.servingClinics = servingClinics;
    }

    public Set<Ssk> getSsks() {
        return ssks;
    }

    public void setSsks(Set<Ssk> ssks) {
        this.ssks = ssks;
    }

    public Boolean getHasLeftDateFeature() {
        return hasLeftDateFeature;
    }

    public void setHasLeftDateFeature(Boolean hasLeftDateFeature) {
        this.hasLeftDateFeature = hasLeftDateFeature;
    }

    public Boolean getHasCarePlan() {
        return hasCarePlan;
    }

    public void setHasCarePlan(Boolean hasCarePlan) {
        this.hasCarePlan = hasCarePlan;
    }

    public Boolean getHasAkutPatientFeature() {
        return hasAkutPatientFeature;
    }

    public void setHasAkutPatientFeature(Boolean hasAkutPatientFeature) {
        this.hasAkutPatientFeature = hasAkutPatientFeature;
    }

    public Boolean getHas23oFeature() {
        return has23oFeature;
    }

    public void setHas23oFeature(Boolean has23oFeature) {
        this.has23oFeature = has23oFeature;
    }

    public Boolean getHas24oFeature() {
        return has24oFeature;
    }

    public void setHas24oFeature(Boolean has24oFeature) {
        this.has24oFeature = has24oFeature;
    }

    public Boolean getHasSekretessFeature() {
        return hasSekretessFeature;
    }

    public void setHasSekretessFeature(Boolean hasSekretessFeature) {
        this.hasSekretessFeature = hasSekretessFeature;
    }

    public Boolean getHasVuxenPatientFeature() {
        return hasVuxenPatientFeature;
    }

    public void setHasVuxenPatientFeature(Boolean hasVuxenPatientFeature) {
        this.hasVuxenPatientFeature = hasVuxenPatientFeature;
    }

    public Boolean getHasInfekteradFeature() {
        return hasInfekteradFeature;
    }

    public void setHasInfekteradFeature(Boolean hasInfekteradFeature) {
        this.hasInfekteradFeature = hasInfekteradFeature;
    }

    public Boolean getHasInfectionSensitiveFeature() {
        return hasInfectionSensitiveFeature;
    }

    public void setHasInfectionSensitiveFeature(Boolean hasInfectionSensitiveFeature) {
        this.hasInfectionSensitiveFeature = hasInfectionSensitiveFeature;
    }

    public Boolean getHasSmittaFeature() {
        return hasSmittaFeature;
    }

    public void setHasSmittaFeature(Boolean hasSmittaFeature) {
        this.hasSmittaFeature = hasSmittaFeature;
    }

    public Set<CleaningAlternative> getCleaningAlternatives() {
        return cleaningAlternatives;
    }

    public void setCleaningAlternatives(Set<CleaningAlternative> cleaningAlternatives) {
        this.cleaningAlternatives = cleaningAlternatives;
    }

    public Boolean getHasCleaningFeature() {
        return hasCleaningFeature;
    }

    public void setHasCleaningFeature(Boolean hasCleaningFeature) {
        this.hasCleaningFeature = hasCleaningFeature;
    }

    public Boolean getHasPalFeature() {
        return hasPalFeature;
    }

    public void setHasPalFeature(Boolean hasPalFeature) {
        this.hasPalFeature = hasPalFeature;
    }

    public Boolean getHasHendelseFeature() {
        return hasHendelseFeature;
    }

    public void setHasHendelseFeature(Boolean hasHendelseFeature) {
        this.hasHendelseFeature = hasHendelseFeature;
    }

    public Boolean getHasMorRondFeature() {
        return hasMorRondFeature;
    }

    public void setHasMorRondFeature(Boolean hasMorRondFeature) {
        this.hasMorRondFeature = hasMorRondFeature;
    }

    public Boolean getHasBarnRondFeature() {
        return hasBarnRondFeature;
    }

    public void setHasBarnRondFeature(Boolean hasBarnRondFeature) {
        this.hasBarnRondFeature = hasBarnRondFeature;
    }

    public Boolean getHasRondFeature() {
        return hasRondFeature;
    }

    public void setHasRondFeature(Boolean hasRondFeature) {
        this.hasRondFeature = hasRondFeature;
    }

    public Boolean getHasAmningFeature() {
        return hasAmningFeature;
    }

    public void setHasAmningFeature(Boolean hasAmningFeature) {
        this.hasAmningFeature = hasAmningFeature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return Objects.equals(id, unit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
