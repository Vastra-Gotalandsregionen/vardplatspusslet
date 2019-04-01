package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Objects;

/**
 * @author Patrik Björk
 */
@Entity
@Table(name = "bed")
public class Bed {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String label;

    @Column
    private Boolean occupied;

    // Add orphanRemoval. Hope it doesn't cause any trouble.
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    private Ssk ssk;

   /* @ManyToOne(fetch = FetchType.EAGER)
    private CareBurdenKategori kategori;


    @ManyToOne(fetch = FetchType.EAGER)
    private CareBurdenValue burdenValue;*/

    @ManyToOne(fetch = FetchType.EAGER)
    private ServingClinic servingClinic;

    @ManyToOne(fetch = FetchType.EAGER)
    private CleaningAlternative cleaningAlternative;

    @Column
    private Boolean patientWaits;


    @Column
    private Boolean cleaningalternativeExists;

    @Column
    private String cleaningInfo;

    @Transient // Owned by the parent side.
    private Unit unit;

    public Bed(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Ssk getSsk() {
        return ssk;
    }

    public void setSsk(Ssk ssk) {
        this.ssk = ssk;
    }

    public ServingClinic getServingClinic() {
        return servingClinic;
    }

    public void setServingClinic(ServingClinic servingClinic) {
        this.servingClinic = servingClinic;
    }

    public Boolean getPatientWaits() {
        return patientWaits;
    }

    public void setPatientWaits(Boolean patientWaits) {
        this.patientWaits = patientWaits;
    }

    public CleaningAlternative getCleaningAlternative() {
        return cleaningAlternative;
    }

    public void setCleaningAlternative(CleaningAlternative cleaningAlternative) {
        this.cleaningAlternative = cleaningAlternative;
    }

    public Boolean getCleaningalternativeExists() {
        return cleaningalternativeExists;
    }

    public void setCleaningalternativeExists(Boolean cleaningalternativeExists) {
        this.cleaningalternativeExists = cleaningalternativeExists;
    }

    public String getCleaningInfo() {
        return cleaningInfo;
    }

    public void setCleaningInfo(String cleaningInfo) {
        this.cleaningInfo = cleaningInfo;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bed bed = (Bed) o;
        return Objects.equals(id, bed.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
