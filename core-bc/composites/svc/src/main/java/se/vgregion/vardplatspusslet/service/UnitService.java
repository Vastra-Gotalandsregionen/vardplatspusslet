package se.vgregion.vardplatspusslet.service;

import com.sun.istack.internal.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.vardplatspusslet.domain.jpa.Bed;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Role;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.domain.jpa.User;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.repository.UnitRepository;
import se.vgregion.vardplatspusslet.repository.UserRepository;

import java.util.ArrayList;
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

    public UnitService(UserRepository userRepository,
                       UnitRepository unitRepository,
                       ClinicRepository clinicRepository) {
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.clinicRepository = clinicRepository;
    }

    public Unit save(Unit unit, Boolean keepBeds) {
        // We need to remove all beds from the unit first, or we will get a constraint exception.
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

        unit.setBeds(null);
       // unitRepository.save(unit);
        unit.setBeds(beds);

        return unitRepository.save(unit);
    }

    public List<Unit> getUnits(@Nullable String clinicId, String userId) {
        User user = userRepository.findOne(userId);

        if (user == null) {
            return Collections.emptyList();
        }

        if (user.getRole().equals(Role.USER)) {
            List<String> usersUnitIds = user.getUnits().stream().map(Unit::getId).collect(Collectors.toList());
            List<Unit> units = unitRepository.findDistinctByIdIn(usersUnitIds, new Sort("clinic.name", "name"));

/*
            for (Unit unit : usersUnits) {
//                initializeAndUnproxy(unit);
                unit.setDietForChildren(initializeAndUnproxySet(unit.getDietForChildren()));
                Collection<String> sss = new ArrayList<>();
                Set<String> setsss = new LinkedHashSet<>();
                sss = setsss;
            }
*/

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
//                units = initializeAndUnproxy(unitRepository.findAll(new Sort("clinic.name", "name")));
            } else {
                // To sort according to id.
                units = unitRepository.findDistinctByClinicIsLike(clinicRepository.getOne(clinicId))
                        .stream()
                        .sorted()
                        .collect(Collectors.toList());
            }

            // TODO Why? Performance.
            /*for (Unit unit : units) {
                unit.setBeds(null);
            }*/

            return units;
        } else {
            throw new RuntimeException("Unexpected role: " + user.getRole().name());
        }
    }
}
