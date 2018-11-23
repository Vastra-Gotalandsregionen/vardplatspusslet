package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Role;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.domain.jpa.User;
import se.vgregion.vardplatspusslet.intsvc.controller.util.HttpUtil;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/clinic")
public class ClinicController {

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Clinic> getClinics(HttpServletRequest request) {

        User user = getUser(request);

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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Clinic getClinic(@PathVariable("id") String id) {
        return clinicRepository.findOne(id);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Clinic> saveClinic(@RequestBody Clinic clinic) {
        return ResponseEntity.ok(clinicRepository.save(clinic));
    }

    @RequestMapping(value = "/{clinicId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteClinic(@PathVariable("clinicId") String clinicId) {
        clinicRepository.delete(clinicId);

        return ResponseEntity.noContent().build();
    }

    private User getUser(HttpServletRequest request) {
        Optional<String> userIdFromRequest = HttpUtil.getUserIdFromRequest(request);

        return userIdFromRequest.map(s -> userRepository.findOne(s)).orElse(null);
    }

}
