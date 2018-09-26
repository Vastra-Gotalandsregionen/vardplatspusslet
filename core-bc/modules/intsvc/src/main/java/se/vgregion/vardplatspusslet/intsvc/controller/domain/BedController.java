package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.vardplatspusslet.domain.jpa.Bed;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/bed")
public class BedController {

     @RequestMapping(value = "", method = RequestMethod.GET)
     @ResponseBody
     public List<Bed> getBeds() {
         return Arrays.asList(new Bed());
     }

}
