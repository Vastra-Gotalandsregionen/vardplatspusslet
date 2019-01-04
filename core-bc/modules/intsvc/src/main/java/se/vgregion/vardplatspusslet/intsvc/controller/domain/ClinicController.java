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
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.service.ClinicService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/clinic")
public class ClinicController extends BaseController {

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private ClinicRepository clinicRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Clinic> getClinics(HttpServletRequest request) {

        String userId = getRequestUserId(request);

        if (userId == null) {
            return Collections.emptyList();
        }

        return clinicService.getClinics(userId);
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
}
