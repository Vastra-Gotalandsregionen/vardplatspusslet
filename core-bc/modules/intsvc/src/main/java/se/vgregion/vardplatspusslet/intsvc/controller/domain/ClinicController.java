package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;

import java.util.List;

@Controller
@RequestMapping("/clinic")
public class ClinicController {

    @Autowired
    private ClinicRepository clinicRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Clinic> getClinics() {
//         PageRequest pageRequest = new PageRequest(0, Integer.MAX_VALUE, new Sort(new Sort.Order("verksamhettext").ignoreCase()));

        List<Clinic> all = clinicRepository.findAll();
        /*List<ClinicDTO> dtos = new ArrayList<>();

        for (Clinic clinic : all) {
            ClinicDTO clinicDTO = new ClinicDTO(clinic.getId(), clinic.getName());
            dtos.add(clinicDTO);
        }*/
        return all;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Clinic getClinic(@PathVariable("id") String id) {
        Clinic clinic = clinicRepository.findOne(id);

        for (Unit unit : clinic.getUnits()) {
            unit.setBeds(null); // Don't need these here. Also avoid lazy initialization exception on json serialization.
        }

       return clinic;
    }

}
