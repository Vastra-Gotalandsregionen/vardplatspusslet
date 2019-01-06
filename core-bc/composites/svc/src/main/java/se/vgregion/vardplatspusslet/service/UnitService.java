package se.vgregion.vardplatspusslet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.vardplatspusslet.domain.jpa.Bed;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.DietForChild;
import se.vgregion.vardplatspusslet.domain.jpa.DietForMother;
import se.vgregion.vardplatspusslet.domain.jpa.DietForPatient;
import se.vgregion.vardplatspusslet.domain.jpa.Role;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.domain.jpa.User;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.repository.DietForChildRepository;
import se.vgregion.vardplatspusslet.repository.DietForMotherRepository;
import se.vgregion.vardplatspusslet.repository.DietForPatientRepository;
import se.vgregion.vardplatspusslet.repository.UnitRepository;
import se.vgregion.vardplatspusslet.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UnitService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private DietForChildRepository dietForChildRepository;

    @Autowired
    private DietForMotherRepository dietForMotherRepository;

    @Autowired
    private DietForPatientRepository dietForPatientRepository;

    public UnitService(UserRepository userRepository,
                       UnitRepository unitRepository,
                       ClinicRepository clinicRepository,
                       DietForChildRepository dietForChildRepository,
                       DietForMotherRepository dietForMotherRepository,
                       DietForPatientRepository dietForPatientRepository) {
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.clinicRepository = clinicRepository;
        this.dietForChildRepository = dietForChildRepository;
        this.dietForMotherRepository = dietForMotherRepository;
        this.dietForPatientRepository = dietForPatientRepository;
    }

    public Unit save(Unit unit, Boolean keepBeds) {
        List<Bed> beds = new ArrayList<>();
        if (keepBeds != null && keepBeds) {
            // Take currently persisted, i.e. keep persisted.
            Unit found = unitRepository.findOne(unit.getId());
            if (found != null) {
                beds = found.getBeds();
            }
        } else {
            // Take from the request.
            beds = unit.getBeds();
        }

        // We need to remove all beds from the unit first, or we may get a constraint exception (at least when we change order of beds).
        unit.setBeds(null);
        unitRepository.save(unit);
        unit.setBeds(beds);

        // Save the transient collections, but first remove all items not in the incoming collection
        DietForChild example1 = new DietForChild();
        DietForMother example2 = new DietForMother();
        DietForPatient example3 = new DietForPatient();

        example1.setUnit(unit);
        example2.setUnit(unit);
        example3.setUnit(unit);

        removeItemsNotInIncomingCollection(dietForChildRepository, example1, unit.getDietForChildren());
        removeItemsNotInIncomingCollection(dietForMotherRepository, example2, unit.getDietForMothers());
        removeItemsNotInIncomingCollection(dietForPatientRepository, example3, unit.getDietForPatients());

        dietForChildRepository.save(unit.getDietForChildren());
        dietForMotherRepository.save(unit.getDietForMothers());
        dietForPatientRepository.save(unit.getDietForPatients());

        return unitRepository.save(unit);
    }

    @SuppressWarnings("unchecked")
    void removeItemsNotInIncomingCollection(JpaRepository repository, Object example, Collection incomingCollection) {
        List currentlySaved = repository.findAll(Example.of(example));
        // Any items not in the incoming collection is to be removed
        currentlySaved.removeAll(incomingCollection);
        repository.delete(currentlySaved);
    }

    public List<Unit> getUnits(String clinicId, String userId) {
        User user = userRepository.findOne(userId);

        if (user == null) {
            return Collections.emptyList();
        }

        if (user.getRole().equals(Role.USER)) {
            List<String> usersUnitIds = user.getUnits().stream().map(Unit::getId).collect(Collectors.toList());
            List<Unit> units = unitRepository.findDistinctByIdIn(usersUnitIds, new Sort("clinic.name", "name"));

            for (Unit unit : units) {
                populateWithDiets(unit);
            }

            if (clinicId == null) {
                return new ArrayList<>(units);
            } else {
                return units.stream()
                        .filter(unit -> unit.getClinic() != null && unit.getClinic().getId().equals(clinicId))
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());
            }

        } else if (user.getRole().equals(Role.ADMIN)) {

            List<Unit> units;
            if (clinicId == null) {
                List<String> unitIds = unitRepository.findAll().stream().map(Unit::getId).collect(Collectors.toList());
                units = unitRepository.findDistinctByIdIn(unitIds, new Sort("clinic.name", "name"));
            } else {
                // To sort according to id.
                units = unitRepository.findDistinctByClinicIsLike(clinicRepository.getOne(clinicId))
                        .stream()
                        .sorted()
                        .collect(Collectors.toList());
            }

            for (Unit unit : units) {
                populateWithDiets(unit);
            }

            return units;
        } else {
            throw new RuntimeException("Unexpected role: " + user.getRole().name());
        }
    }

    public Unit findUnitByIdAndClinic(String id, Clinic clinic) {
        Unit unit = unitRepository.findUnitByIdIsLikeAndClinicIsLike(id, clinic);

        populateWithDiets(unit);

        return unit;
    }

    private void populateWithDiets(Unit unit) {
        DietForMother example1 = new DietForMother();
        DietForChild example2 = new DietForChild();
        DietForPatient example3 = new DietForPatient();

        example1.setUnit(unit);
        example2.setUnit(unit);
        example3.setUnit(unit);

        List<DietForMother> dietForMothers = dietForMotherRepository.findAll(Example.of(example1));
        List<DietForChild> dietForChildren = dietForChildRepository.findAll(Example.of(example2));
        List<DietForPatient> dietForPatients = dietForPatientRepository.findAll(Example.of(example3));

        unit.setDietForMothers(dietForMothers);
        unit.setDietForChildren(dietForChildren);
        unit.setDietForPatients(dietForPatients);
    }
}
