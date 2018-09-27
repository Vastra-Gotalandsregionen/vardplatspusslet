package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.vardplatspusslet.domain.jpa.Bed;
import se.vgregion.vardplatspusslet.repository.BedRepository;

@Controller
@RequestMapping("/bed")
public class BedController {

    @Autowired
    private BedRepository bedRepository;

     /*@RequestMapping(value = "", method = RequestMethod.GET)
     @ResponseBody
     public List<Bed> getBeds() {
         return Arrays.asList(new Bed());
     }*/

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
//    @PreAuthorize("@authService.hasRole(authentication, 'ADMIN')")
    public ResponseEntity<Bed> saveBed(@RequestBody Bed bed) {
        return ResponseEntity.ok(bedRepository.save(bed));
    }
}
