package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Role;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.domain.jpa.User;
import se.vgregion.vardplatspusslet.intsvc.controller.util.HttpUtil;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.repository.UnitRepository;
import se.vgregion.vardplatspusslet.repository.UserRepository;
import se.vgregion.vardplatspusslet.service.UnitService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/unit")
public class UnitController {

    @Autowired
    private UnitService unitService;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Unit> getUnits(@RequestParam(value = "clinic", required = false) String clinicId,
                               HttpServletRequest request) {

        User user = getUser(request);

        if (user == null) {
            return Collections.emptyList();
        }

        if (user.getRole().equals(Role.USER)) {
            List<Unit> usersUnits = new ArrayList<>(user.getUnits());

            if (clinicId == null) {
                return usersUnits;
            } else {
                return usersUnits.stream()
                        .filter(unit -> unit.getClinic() != null && unit.getClinic().getId().equals(clinicId))
                        .collect(Collectors.toList());
            }

        } else if (user.getRole().equals(Role.ADMIN)) {

            List<Unit> units;
            if (clinicId == null) {
                units = unitRepository.findAll(new Sort("clinic.name", "name"));
            } else {
                units = unitRepository.findUnitsByClinicIsLike(clinicRepository.getOne(clinicId));
            }

            // TODO Why? Performance.
            for (Unit unit : units) {
                unit.setBeds(null);
            }

            return units;
        } else {
            throw new RuntimeException("Unexpected role: " + user.getRole().name());
        }
    }

    @RequestMapping(value = "/{clinicId}/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Unit getUnit(@PathVariable("clinicId") String clinicId, @PathVariable("id") String id) {
        Clinic clinic = clinicRepository.getOne(clinicId);

        return unitRepository.findUnitByIdIsLikeAndClinicIsLike(id, clinic);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Unit> saveUnit(@RequestBody Unit unit,
                                         @RequestParam(value = "keepBeds", required = false) Boolean keepBeds) {
        unitService.save(unit, keepBeds);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{unitId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUnit(@PathVariable("unitId") String unitId) {

        unitRepository.delete(unitId);

        return ResponseEntity.noContent().build();
    }

    private User getUser(HttpServletRequest request) {
        Optional<String> userIdFromRequest = HttpUtil.getUserIdFromRequest(request);

        return userIdFromRequest.map(s -> userRepository.findOne(s)).orElse(null);
    }
}
