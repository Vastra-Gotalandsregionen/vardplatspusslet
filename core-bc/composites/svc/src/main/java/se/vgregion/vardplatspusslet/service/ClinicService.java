package se.vgregion.vardplatspusslet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Role;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.domain.jpa.User;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClinicService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    public List<Clinic> getClinics(String userId) {

        User user = userRepository.findUserById(userId);

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
}
