package se.vgregion.vardplatspusslet.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Management;
import se.vgregion.vardplatspusslet.domain.jpa.Role;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.domain.jpa.User;
import se.vgregion.vardplatspusslet.repository.ManagementRepository;
import se.vgregion.vardplatspusslet.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ManagementService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ManagementRepository managementRepository;

    public List<Management> getManagements(String userId){
        User user = userRepository.findUserById(userId);

        if (user.getRole().equals(Role.USER) || user.getRole().equals(Role.UNIT_ADMIN)) {
            return user.getUnits().stream()
                    .map(Unit::getClinic)
                    .filter(clinic -> clinic.getManagement() != null)
                    .map(Clinic::getManagement)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());

        } else if(user.getRole().equals(Role.ADMIN)){
            return  managementRepository.findAllByOrderById();
        } else  {
            throw new RuntimeException("Unexpected role: " + user.getRole().name());
        }
    }

    public boolean managementExists(String id) {
        return managementRepository.findOne(id) != null;
    }
}
