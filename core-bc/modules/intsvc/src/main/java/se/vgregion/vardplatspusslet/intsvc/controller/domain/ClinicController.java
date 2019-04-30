package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.service.ClinicService;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/clinic")
public class ClinicController extends BaseController {

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private ClinicRepository clinicRepository;

    @RequestMapping(value = "/management", method = RequestMethod.GET)
    @ResponseBody
    public List<Clinic> getManagementClinics(@RequestParam(value = "management", required = false) String managementId,
                                 HttpServletRequest request) {

        String userId = getRequestUserId(request);

        if (userId == null) {
            return new ArrayList<>();
        }

        return clinicService.getManagementClinics(managementId, userId);
    }

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
    @PreAuthorize("@authService.hasRole(authentication, 'ADMIN')")
    public ResponseEntity<Object> saveClinic(@RequestBody Clinic clinic,
                                             @RequestParam(value = "newClinic", required = false) Boolean newClinic
    ) throws UnsupportedEncodingException {

        String id = clinic.getId().trim();
        clinic.setId(id);
        if (!id.equals(URLEncoder.encode(id, "UTF-8"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiError("Endast a-z, siffror, - och _ är tillåtna tecken för ID."));
        }

        if (Boolean.TRUE.equals(newClinic) && clinicService.clinicExists(id)) {

            return new ResponseEntity<>(
                    new ApiError("En klinik med ID=" + id + " finns redan."),
                    new HttpHeaders(),
                    HttpStatus.BAD_REQUEST
            );
        }

        return ResponseEntity.ok(clinicRepository.save(clinic));
    }

    @RequestMapping(value = "/{clinicId}", method = RequestMethod.DELETE)
    @PreAuthorize("@authService.hasRole(authentication, 'ADMIN')")
    public ResponseEntity deleteClinic(@PathVariable("clinicId") String clinicId) {
        clinicRepository.delete(clinicId);

        return ResponseEntity.noContent().build();
    }
}
