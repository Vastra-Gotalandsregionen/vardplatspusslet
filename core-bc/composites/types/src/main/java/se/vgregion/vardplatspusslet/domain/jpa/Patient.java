package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

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

    @Column
    private Boolean interpreter;

    @Column
    @Temporal(TemporalType.DATE)
    private Date interpretDate;

    @Column
    private String interpretInfo;

    @Column
    private Boolean akutPatient;

    @Column
    private Boolean Electiv23O;

    @Column
    private Boolean Electiv24O;

    @Column
    private Boolean vuxenPatient;

    @Column
    private Boolean sekretess;

    @Column
    private String sekretessInfo;

    @Column
    private Boolean infekterad;

    @Column
    private Boolean infectionSensitive;

    @Column
    private Boolean smitta;

    @Column
    private String smittaInfo;

   /* @Column
    private String Pal;*/

    @Column
    private String information;

    @Column
    private String kommentar;

    @ManyToOne(fetch = FetchType.EAGER)
    private DietForMother dietMother;

    @Column
    private String infoDietMother;

    @ManyToOne(fetch = FetchType.EAGER)
    private DietForChild dietChild;

    @Column
    private String infoDietChild;

    @ManyToOne(fetch = FetchType.EAGER)
    private DietForPatient diet;

    @Column
    private String infoDiet;

    @Column
    private String relatedInformation;

    @ManyToOne(fetch = FetchType.EAGER)
    private MothersDiet  mothersDiet;

    @ManyToOne(fetch = FetchType.EAGER)
    private ChildrensDiet  childrensDiet;



    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PatientExamination> patientExaminations = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PatientEvent> patientEvents = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CareBurdenChoice> careBurdenChoices = new LinkedHashSet<>();

    @Column
    private Boolean morRond;

    @Column
    private Boolean barnRond;

    @Column
    private Boolean rond;

    @Column
    private String amning;

    @ManyToOne(fetch = FetchType.EAGER)
    private ServingClinic fromClinic;

    @Column
    private Boolean specialDietChild;

    @Column
    private Boolean specialDietMother;

    @Column
    private Boolean specialDiet;

    public MothersDiet getMothersDiet() {
        return mothersDiet;
    }

    public void setMothersDiet(MothersDiet mothersDiet) {
        this.mothersDiet = mothersDiet;
    }

    public ChildrensDiet getChildrensDiet() {
        return childrensDiet;
    }

    public void setChildrensDiet(ChildrensDiet childrensDiet) {
        this.childrensDiet = childrensDiet;
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

    public Date getInterpretDate() {
        return interpretDate;
    }

    public Boolean getInterpreter() {
        return interpreter;
    }

    public void setInterpreter(Boolean interpreter) {
        this.interpreter = interpreter;
    }

    public void setInterpretDate(Date interpretDate) {
        this.interpretDate = interpretDate;
    }

    public String getInterpretInfo() {
        return interpretInfo;
    }

    public void setInterpretInfo(String interpretInfo) {
        this.interpretInfo = interpretInfo;
    }

    public Set<PatientExamination> getPatientExaminations() {
        return patientExaminations;
    }

    public void setPatientExaminations(Set<PatientExamination> patientExaminations) {
        this.patientExaminations = patientExaminations;
    }

    public Boolean getAkutPatient() {
        return akutPatient;
    }

    public void setAkutPatient(Boolean akutPatient) {
        this.akutPatient = akutPatient;
    }

    public Boolean getElectiv23O() {
        return Electiv23O;
    }

    public void setElectiv23O(Boolean electiv23O) {
        Electiv23O = electiv23O;
    }

    public Boolean getElectiv24O() {
        return Electiv24O;
    }

    public void setElectiv24O(Boolean electiv24O) {
        Electiv24O = electiv24O;
    }

    public Boolean getVuxenPatient() {
        return vuxenPatient;
    }

    public void setVuxenPatient(Boolean vuxenPatient) {
        this.vuxenPatient = vuxenPatient;
    }

    public Boolean getSekretess() {
        return sekretess;
    }

    public void setSekretess(Boolean sekretess) {
        this.sekretess = sekretess;
    }

    public String getSekretessInfo() {
        return sekretessInfo;
    }

    public void setSekretessInfo(String sekretessInfo) {
        this.sekretessInfo = sekretessInfo;
    }

    public Boolean getInfekterad() {
        return infekterad;
    }

    public void setInfekterad(Boolean infekterad) {
        this.infekterad = infekterad;
    }

    public Boolean getInfectionSensitive() {
        return infectionSensitive;
    }

    public Boolean getSmitta() {
        return smitta;
    }

    public void setSmitta(Boolean smitta) {
        this.smitta = smitta;
    }

    public String getSmittaInfo() {
        return smittaInfo;
    }

    public void setSmittaInfo(String smittaInfo) {
        this.smittaInfo = smittaInfo;
    }

    public void setInfectionSensitive(Boolean infectionSensitive) {
        this.infectionSensitive = infectionSensitive;
    }

    /*public String getPal() {
        return Pal;
    }

    public void setPal(String pal) {
        Pal = pal;
    }*/

    public Set<PatientEvent> getPatientEvents() {
        return patientEvents;
    }

    public void setPatientEvents(Set<PatientEvent> patientEvents) {
        this.patientEvents = patientEvents;
    }

    public Boolean getMorRond() {
        return morRond;
    }

    public void setMorRond(Boolean morRond) {
        this.morRond = morRond;
    }

    public Boolean getBarnRond() {
        return barnRond;
    }

    public void setBarnRond(Boolean barnRond) {
        this.barnRond = barnRond;
    }

    public Boolean getRond() {
        return rond;
    }

    public void setRond(Boolean rond) {
        this.rond = rond;
    }

    public String getAmning() {
        return amning;
    }

    public void setAmning(String amning) {
        this.amning = amning;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public Set<CareBurdenChoice> getCareBurdenChoices() {
        return careBurdenChoices;
    }

    public void setCareBurdenChoices(Set<CareBurdenChoice> careBurdenChoices) {
        this.careBurdenChoices = careBurdenChoices;
    }

    public DietForMother getDietMother() {
        return dietMother;
    }

    public void setDietMother(DietForMother dietMother) {
        this.dietMother = dietMother;
    }

    public DietForChild getDietChild() {
        return dietChild;
    }

    public void setDietChild(DietForChild dietChild) {
        this.dietChild = dietChild;
    }

    public DietForPatient getDiet() {
        return diet;
    }

    public void setDiet(DietForPatient diet) {
        this.diet = diet;
    }

    public String getInfoDietMother() {
        return infoDietMother;
    }

    public void setInfoDietMother(String infoDietMother) {
        this.infoDietMother = infoDietMother;
    }

    public String getInfoDietChild() {
        return infoDietChild;
    }

    public void setInfoDietChild(String infoDietChild) {
        this.infoDietChild = infoDietChild;
    }

    public String getInfoDiet() {
        return infoDiet;
    }

    public void setInfoDiet(String infoDiet) {
        this.infoDiet = infoDiet;
    }

    public String getRelatedInformation() {
        return relatedInformation;
    }

    public void setRelatedInformation(String relatedInformation) {
        this.relatedInformation = relatedInformation;
    }

    public ServingClinic getFromClinic() {
        return fromClinic;
    }

    public void setFromClinic(ServingClinic fromClinic) {
        this.fromClinic = fromClinic;
    }

    public Boolean getSpecialDietChild() {
        return specialDietChild;
    }

    public void setSpecialDietChild(Boolean specialDietChild) {
        this.specialDietChild = specialDietChild;
    }

    public Boolean getSpecialDietMother() {
        return specialDietMother;
    }

    public void setSpecialDietMother(Boolean specialDietMother) {
        this.specialDietMother = specialDietMother;
    }

    public Boolean getSpecialDiet() {
        return specialDiet;
    }

    public void setSpecialDiet(Boolean specialDiet) {
        this.specialDiet = specialDiet;
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
