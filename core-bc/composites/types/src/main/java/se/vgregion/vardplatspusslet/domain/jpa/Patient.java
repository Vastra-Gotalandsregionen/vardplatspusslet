package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String label;

    @Enumerated
    private Gender gender;

    @Enumerated
    private PatientCategory patientCategory; // TODO för "Ny patient väntar". Ha här?

    @OneToOne
    private Patient awaitingPatient;

    @ManyToOne
    private Status status;

    @Column
    @Temporal(TemporalType.DATE) // TODO räcker dag?
    private Date plannedLeaveDate;

    @Column
    @Temporal(TemporalType.DATE)
    private Date leftDate;

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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public PatientCategory getPatientCategory() {
        return patientCategory;
    }

    public void setPatientCategory(PatientCategory patientCategory) {
        this.patientCategory = patientCategory;
    }

    public Patient getAwaitingPatient() {
        return awaitingPatient;
    }

    public void setAwaitingPatient(Patient awaitingPatient) {
        this.awaitingPatient = awaitingPatient;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getPlannedLeaveDate() {
        return plannedLeaveDate;
    }

    public void setPlannedLeaveDate(Date plannedLeaveDate) {
        this.plannedLeaveDate = plannedLeaveDate;
    }

    public Date getLeftDate() {
        return leftDate;
    }

    public void setLeftDate(Date leftDate) {
        this.leftDate = leftDate;
    }
}
