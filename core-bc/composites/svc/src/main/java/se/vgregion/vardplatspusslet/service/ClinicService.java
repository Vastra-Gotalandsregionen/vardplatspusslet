package se.vgregion.vardplatspusslet.service;

import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Role;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.domain.jpa.User;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.repository.ManagementRepository;
import se.vgregion.vardplatspusslet.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClinicService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private ManagementRepository managementRepository;

    public List<Clinic> getClinics(String userId) {

        User user = userRepository.findUserById(userId);

        if (user == null) {
            return Collections.emptyList();
        }

        if (user.getRole().equals(Role.USER)) {
            List<Unit> usersUnits = new ArrayList<>(user.getUnits());

            return usersUnits.stream()
                    .filter(unit -> unit.getClinic() != null)
                    .map(Unit::getClinic)
                    .distinct()
                    .collect(Collectors.toList());

        } else if (user.getRole().equals(Role.ADMIN)) {
            return clinicRepository.findAllByOrderById();
        } else {
            throw new RuntimeException("Unexpected role: " + user.getRole().name());
        }
    }

    public List<Clinic> getManagementClinics(String managementId, String userId) {
        User user = userRepository.findOne(userId);

        if (user.getRole().equals(Role.USER)) {

            List<String> usersClinicIds = user.getUnits().stream()
                    .map(Unit::getClinic)
                    .map(Clinic::getId).
                    collect(Collectors.toList());

            List<Clinic> clinics = clinicRepository.findDistinctByIdIn(usersClinicIds, new Sort("management.name", "name"));

            if (managementId == null) {
                return new ArrayList<>(clinics);
            } else {
                return clinics.stream()
                        .filter(clinic -> clinic.getManagement() != null && clinic.getManagement().getId().equals(managementId))
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());
            }

        } else if (user.getRole().equals(Role.ADMIN)) {
            List<Clinic> clinics;
            if (managementId == null) {
                List<String> clinicIds = clinicRepository.findAll().stream().map(Clinic::getId).collect(Collectors.toList());
                clinics = clinicRepository.findDistinctByIdIn(clinicIds, new Sort("management.name", "name"));
            } else {
                // To sort according to id.
                clinics = clinicRepository.findDistinctByManagementIsLike(managementRepository.getOne(managementId))
                        .stream()
                        .sorted()
                        .collect(Collectors.toList());
            }
            return clinics;

        } else {
        throw new RuntimeException("Unexpected role: " + user.getRole().name());
        }
    }

    public boolean clinicExists(String id) {
        return clinicRepository.findOne(id) != null;
    }
}
