package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.vardplatspusslet.domain.jpa.Bed;
import se.vgregion.vardplatspusslet.repository.BedRepository;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.repository.UnitRepository;
import se.vgregion.vardplatspusslet.service.BedService;

@Controller
@RequestMapping("/bed")
public class BedController {

    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private BedService bedService;

     /*@RequestMapping(value = "", method = RequestMethod.GET)
     @ResponseBody
     public List<Bed> getBeds() {
         return Arrays.asList(new Bed());
     }*/

    @RequestMapping(value = "/{clinicId}/{unitId}", method = RequestMethod.PUT)
    @ResponseBody
//    @PreAuthorize("@authService.hasRole(authentication, 'ADMIN')")
    public ResponseEntity<Bed> saveBed(@PathVariable("clinicId") String clinicId,
                                       @PathVariable("unitId") String unitId,
                                       @RequestBody Bed bed) {

        bedService.save(clinicId, unitId, bed);

        return ResponseEntity.ok(bed);
    }

    @RequestMapping(value = "/{clinicId}/{unitId}/{bedId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteBed(@PathVariable("clinicId") String clinicId,
                                    @PathVariable("unitId") String unitId,
                                    @PathVariable("bedId") Long bedId) {

        bedService.deleteBed(clinicId, unitId, bedId);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/patientHasLeft", method = RequestMethod.POST)
    @ResponseBody
//    @PreAuthorize("@authService.hasRole(authentication, 'ADMIN')")
    public ResponseEntity<?> patientHasLeft(@RequestBody Bed bed) {

        if (bed.getPatient() == null) {
            return ResponseEntity.badRequest().build();
        }

        bedService.patientHasLeft(bed);

        return ResponseEntity.noContent().build();
    }
}
