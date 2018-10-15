package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.Objects;

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

    @Enumerated
    private LeaveStatus leaveStatus;

    @Column
    @Temporal(TemporalType.DATE) // TODO räcker dag?
    private Date plannedLeaveDate;

    @Column
    @Temporal(TemporalType.DATE)
    private Date leftDate;

    @Column
    private String carePlan;

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

    public LeaveStatus getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(LeaveStatus status) {
        this.leaveStatus = status;
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

    public String getCarePlan() {
        return carePlan;
    }

    public void setCarePlan(String carePlan) {
        this.carePlan = carePlan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(id, patient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
