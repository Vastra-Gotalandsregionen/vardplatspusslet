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
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.LinkedHashSet;
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
public class Unit implements Comparable<Unit> {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Clinic clinic;

    @OneToMany(fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ServingClinic> servingClinics = new TreeSet<>();

    @OneToMany(fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CleaningAlternative> cleaningAlternatives= new LinkedHashSet<>();

    @Column
    private String name;

    @Column
    private String careBurden;

    @OrderColumn(name = "order_")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Bed> beds = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Patient> patients = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ssk> ssks = new TreeSet<>();

    @OrderColumn(name = "order_")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CareBurdenCategory> careBurdenCategories = new ArrayList<>();

    // TODO Remove the @OneToMany relationships and fetch these separately, and fetch (over http) containers (DTO) including all these, and make requests in a similar way.
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CareBurdenValue> careBurdenValues = new LinkedHashSet<>();

    // TODO Remove the transients here and instead fetch containers (DTO) containing e.g. both the unit and the diets separately, and make requests in a similar way.
    @Transient
    private List<UnitPlannedIn> unitsPlannedIn = new ArrayList<>();

    @Transient
    private List<SevenDaysPlaningUnit> sevenDaysPlaningUnits = new ArrayList<>();

    /*@Transient
    private List<DietForMother> dietForMothers = new ArrayList<>();

    @Transient
    private List<DietForChild> dietForChildren = new ArrayList<>();

    @Transient
    private List<DietForPatient> dietForPatients = new ArrayList<>();*/

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
    private Boolean hasCleaningFeature;

   /* @Column
    private Boolean hasPalFeature;*/

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


    @Column
    private Boolean hasMorKostFeature;

    @Column
    private Boolean hasBarnKostFeature;

    @Column
    private Boolean hasKostFeature;

    @Column
    private  Boolean hasMotherChildDietFeature;

    @Column
    private Boolean hasUnitPlannedInFeature;

    @Column
    private Boolean hasPatientFromClinicFeature;

    @Column
    private Boolean hasGenderFeature;

    @Column
    private Boolean hasPatientWaitsFeature;

    @Column
    private Boolean hasBackToHomeAlternativFeature;

    @Column
    private Boolean hasDatedBackHomeFeature;

    @Column
    @OneToMany(fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AllowedBedName> allowedBedNames = new TreeSet<>();

    @Column
    private Boolean resetSskOnHasLeft;

    @Column
    private Boolean hasDetailedMotherChildDietFeature;

    @Column
    private Boolean hasAllergiFeature;

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

    public Boolean getHasVuxenPatientFeature() {
        return hasVuxenPatientFeature;
    }

    public void setHasVuxenPatientFeature(Boolean hasVuxenPatientFeature) {
        this.hasVuxenPatientFeature = hasVuxenPatientFeature;
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

    /*public Boolean getHasPalFeature() {
        return hasPalFeature;
    }

    public void setHasPalFeature(Boolean hasPalFeature) {
        this.hasPalFeature = hasPalFeature;
    }*/

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

    public List<CareBurdenCategory> getCareBurdenCategories() {
        return careBurdenCategories;
    }

    public void setCareBurdenCategories(List<CareBurdenCategory> burdenKategories) {
        this.careBurdenCategories = burdenKategories;
    }

    public Set<CareBurdenValue> getCareBurdenValues() {
        return careBurdenValues;
    }

    public void setCareBurdenValues(Set<CareBurdenValue> burdenValues) {
        this.careBurdenValues = burdenValues;
    }

    public Boolean getHasMorKostFeature() {
        return hasMorKostFeature;
    }

    public void setHasMorKostFeature(Boolean hasMorKostFeature) {
        this.hasMorKostFeature = hasMorKostFeature;
    }

    public Boolean getHasBarnKostFeature() {
        return hasBarnKostFeature;
    }

    public void setHasBarnKostFeature(Boolean hasBarnKostFeature) {
        this.hasBarnKostFeature = hasBarnKostFeature;
    }

    public Boolean getHasKostFeature() {
        return hasKostFeature;
    }

    public void setHasKostFeature(Boolean hasKostFeature) {
        this.hasKostFeature = hasKostFeature;
    }

    /*public List<DietForMother> getDietForMothers() {
        return dietForMothers;
    }

    public void setDietForMothers(List<DietForMother> dietForMothers) {
        this.dietForMothers = dietForMothers;
        for (DietForMother dietForMother : dietForMothers) {
            dietForMother.setUnit(this);
        }
    }

    public List<DietForChild> getDietForChildren() {
        return dietForChildren;
    }

    public void setDietForChildren(List<DietForChild> dietForChildren) {
        this.dietForChildren = dietForChildren;
        for (DietForChild dietForChild : dietForChildren) {
            dietForChild.setUnit(this);
        }
    }

    public List<DietForPatient> getDietForPatients() {
        return dietForPatients;
    }

    public void setDietForPatients(List<DietForPatient> dietForPatients) {
        this.dietForPatients = dietForPatients;
        for (DietForPatient dietForPatient : dietForPatients) {
            dietForPatient.setUnit(this);
        }
    }*/

    public List<UnitPlannedIn> getUnitsPlannedIn() {
        return unitsPlannedIn;
    }

    public void setUnitsPlannedIn(List<UnitPlannedIn> unitsPlannedIn) {
        this.unitsPlannedIn = unitsPlannedIn;
        for(UnitPlannedIn  unitPlannedIn : unitsPlannedIn){
            unitPlannedIn.setUnit(this);
        }
    }

    public Boolean getHasMotherChildDietFeature() {
        return hasMotherChildDietFeature;
    }

    public void setHasMotherChildDietFeature(Boolean hasMotherChildDietFeature) {
        this.hasMotherChildDietFeature = hasMotherChildDietFeature;
    }

    public Boolean getHasUnitPlannedInFeature() {
        return hasUnitPlannedInFeature;
    }

    public void setHasUnitPlannedInFeature(Boolean hasUnitPlannedInFeature) {
        this.hasUnitPlannedInFeature = hasUnitPlannedInFeature;
    }

    public List<SevenDaysPlaningUnit> getSevenDaysPlaningUnits() {
        return sevenDaysPlaningUnits;
    }

    public void setSevenDaysPlaningUnits(List<SevenDaysPlaningUnit> sevenDaysPlaningUnits) {
        this.sevenDaysPlaningUnits = sevenDaysPlaningUnits;
        for(SevenDaysPlaningUnit sevenDaysPlaningUnit : sevenDaysPlaningUnits)
        {
            sevenDaysPlaningUnit.setUnit(this);
        }
    }

    public String getCareBurden() {
        return careBurden;
    }

    public void setCareBurden(String careBurden) {
        this.careBurden = careBurden;
    }

    public Boolean getHasPatientFromClinicFeature() {
        return hasPatientFromClinicFeature;
    }

    public void setHasPatientFromClinicFeature(Boolean hasPatientFromClinicFeature) {
        this.hasPatientFromClinicFeature = hasPatientFromClinicFeature;
    }

    public Boolean getHasGenderFeature() {
        return hasGenderFeature;
    }

    public void setHasGenderFeature(Boolean hasGenderFeature) {
        this.hasGenderFeature = hasGenderFeature;
    }

    public Boolean getHasPatientWaitsFeature() {
        return hasPatientWaitsFeature;
    }

    public void setHasPatientWaitsFeature(Boolean hasPatientWaitsFeature) {
        this.hasPatientWaitsFeature = hasPatientWaitsFeature;
    }

    public Boolean getHasBackToHomeAlternativFeature() {
        return hasBackToHomeAlternativFeature;
    }

    public void setHasBackToHomeAlternativFeature(Boolean hasBackToHomeAlternativFeature) {
        this.hasBackToHomeAlternativFeature = hasBackToHomeAlternativFeature;
    }

    public Boolean getHasDatedBackHomeFeature() {
        return hasDatedBackHomeFeature;
    }

    public void setHasDatedBackHomeFeature(Boolean hasDatedBackHomeFeature) {
        this.hasDatedBackHomeFeature = hasDatedBackHomeFeature;
    }

    public Set<AllowedBedName> getAllowedBedNames() {
        return allowedBedNames;
    }

    public void setAllowedBedNames(Set<AllowedBedName> allowedBedNames) {
        this.allowedBedNames = allowedBedNames;
    }

    public Boolean getResetSskOnHasLeft() {
        return resetSskOnHasLeft;
    }

    public void setResetSskOnHasLeft(Boolean resetSskOnHasLeft) {
        this.resetSskOnHasLeft = resetSskOnHasLeft;
    }

    public Boolean getHasDetailedMotherChildDietFeature() {
        return hasDetailedMotherChildDietFeature;
    }

    public void setHasDetailedMotherChildDietFeature(Boolean hasDetailedMotherChildDietFeature) {
        this.hasDetailedMotherChildDietFeature = hasDetailedMotherChildDietFeature;
    }

    public Boolean getHasAllergiFeature() {
        return hasAllergiFeature;
    }

    public void setHasAllergiFeature(Boolean hasAllergiFeature) {
        this.hasAllergiFeature = hasAllergiFeature;
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

    @Override
    public int compareTo(Unit o) {
        return this.getId().compareTo(o.getId());
    }
}
