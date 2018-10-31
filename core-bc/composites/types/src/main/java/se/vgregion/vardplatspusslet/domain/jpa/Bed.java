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

    @OneToOne(cascade = CascadeType.ALL)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    private Ssk ssk;

    @ManyToOne(fetch = FetchType.EAGER)
    private ServingClinic servingClinic;

    @Column
    private Boolean patientWaits;

    @Column
    private String relatedInformation;


    public Bed() {
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

    public String getRelatedInformation() {
        return relatedInformation;
    }

    public void setRelatedInformation(String relatedInformation) {
        this.relatedInformation = relatedInformation;
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
