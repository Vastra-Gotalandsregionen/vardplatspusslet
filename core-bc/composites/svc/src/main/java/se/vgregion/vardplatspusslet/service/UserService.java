package se.vgregion.vardplatspusslet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.vardplatspusslet.domain.jpa.Role;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.domain.jpa.User;
import se.vgregion.vardplatspusslet.repository.UnitRepository;
import se.vgregion.vardplatspusslet.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UnitRepository unitRepository;

    public UserService(UserRepository userRepository, UnitRepository unitRepository) {
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
    }

    public List<User> getUsers(String userId) {
        User user = userRepository.findOne(userId);

        if (user.getRole().equals(Role.USER)) {
            List<Unit> requestingUsersUnits = new ArrayList<>(user.getUnits());

            return userRepository.findAllByOrderById().stream().filter(u -> {
                Set<Unit> userUnits = new HashSet<>(u.getUnits());
                userUnits.retainAll(requestingUsersUnits);
                return userUnits.size() > 0; // Then overlapping units were found.
            }).collect(Collectors.toList());

        } else if (user.getRole().equals(Role.ADMIN)) {
            return userRepository.findAllByOrderById();
        } else {
            throw new RuntimeException("Unexpected role: " + user.getRole().name());
        }
    }

    public Set<Unit> getAuthorizedUnits(String userId) {
        List<Unit> allUnits = unitRepository.findAll();

        // Find out about the saving user to determine which units he/she is authorized to.
        User user = userRepository.findOne(userId);

        Set<Unit> authorizedUnits;

        if (user.getRole().equals(Role.ADMIN)) {
            // All.
            authorizedUnits = new HashSet<>(allUnits);
        } else {
            authorizedUnits = user.getUnits();
        }

        return authorizedUnits;
    }
}
