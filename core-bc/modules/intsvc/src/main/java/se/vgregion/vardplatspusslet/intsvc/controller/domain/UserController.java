package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.vardplatspusslet.domain.jpa.Role;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.domain.jpa.User;
import se.vgregion.vardplatspusslet.domain.json.UserSaveRequest;
import se.vgregion.vardplatspusslet.repository.UnitRepository;
import se.vgregion.vardplatspusslet.repository.UserRepository;
import se.vgregion.vardplatspusslet.service.LdapLoginService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private LdapLoginService ldapLoginService;

    public UserController(UserRepository userRepository,
                          UnitRepository unitRepository,
                          LdapLoginService ldapLoginService) {
        this.userRepository = userRepository;
        this.unitRepository = unitRepository;
        this.ldapLoginService = ldapLoginService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUsers(HttpServletRequest request) {

        User requestingUser = getRequestUser(request);

        if (requestingUser == null) {
            return Collections.emptyList();
        }

        if (requestingUser.getRole().equals(Role.USER)) {
            List<Unit> requestingUsersUnits = new ArrayList<>(requestingUser.getUnits());

            return userRepository.findAllByOrderById().stream().filter(u -> {
                Set<Unit> userUnits = new HashSet<>(u.getUnits());
                userUnits.retainAll(requestingUsersUnits);
                return userUnits.size() > 0; // Then overlapping units were found.
            }).collect(Collectors.toList());

        } else if (requestingUser.getRole().equals(Role.ADMIN)) {
            return userRepository.findAllByOrderById();
        } else {
            throw new RuntimeException("Unexpected role: " + requestingUser.getRole().name());
        }
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<User> saveUser(@RequestBody UserSaveRequest userSaveRequest,
                                         HttpServletRequest request) {

        Set<Unit> authorizedUnits = getAuthorizedUnits(request);

        List<Unit> allUnits = unitRepository.findAll();

        Set<Unit> unitsInSaveRequest = allUnits.stream()
                .filter(unit -> userSaveRequest.getUnitIds().contains(unit.getId()))
                .collect(Collectors.toSet());

        // Units both authorized and in save request
        Set<Unit> authorizedUnitsInSaveRequest = unitsInSaveRequest.stream()
                .filter(authorizedUnits::contains)
                .collect(Collectors.toSet());

        User user = userRepository.findOne(userSaveRequest.getId());

        if (user == null) {
            user = new User();
            user.setId(userSaveRequest.getId());
            user.setRole(userSaveRequest.getRole());
            user.setUnits(authorizedUnitsInSaveRequest);
        } else {
            // Remove the units which the saving user is authorized too and which is missing in the save request.
            HashSet<Unit> cloneAllUnits = new HashSet<>(allUnits);
            cloneAllUnits.removeAll(unitsInSaveRequest);
            Set<Unit> allMissingUnits = cloneAllUnits;
            authorizedUnits.retainAll(allMissingUnits);
            Set<Unit> toRemove = authorizedUnits; // authorized units which is also missing
            user.getUnits().removeAll(toRemove);

            // And add all in the save request (which the user is authorized too).
            user.getUnits().addAll(authorizedUnitsInSaveRequest);
        }

        ldapLoginService.synchronizeUserFieldsFromLdap(user);

        return ResponseEntity.ok(userRepository.save(user));
    }

    private Set<Unit> getAuthorizedUnits(HttpServletRequest request) {
        List<Unit> allUnits = unitRepository.findAll();

        // Find out about the saving user to determine which units he/she is authorized to.
        User savingUser = getRequestUser(request);
        Set<Unit> authorizedUnits;

        if (savingUser.getRole().equals(Role.ADMIN)) {
            // All.
            authorizedUnits = new HashSet<>(allUnits);
        } else {
            authorizedUnits = savingUser.getUnits();
        }
        return authorizedUnits;
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable("userId") String userId, HttpServletRequest request) {
        Set<Unit> authorizedUnits = getAuthorizedUnits(request);

        User userToDelete = userRepository.findOne(userId);

        // authorizedUnits must include all user's units
        if (authorizedUnits.containsAll(userToDelete.getUnits())) {
            userRepository.delete(userId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @RequestMapping(value = "/{userId}/thumbnailPhoto", method = RequestMethod.GET, produces = "image/jpg")
    public ResponseEntity<byte[]> getUserThumbnailPhoto(@PathVariable("userId") String userId) {
        User user = userRepository.findOne(userId);

        return ResponseEntity.ok(user.getThumbnailPhoto());
    }

    @Override
    UserRepository getUserRepository() {
        return userRepository;
    }
}
