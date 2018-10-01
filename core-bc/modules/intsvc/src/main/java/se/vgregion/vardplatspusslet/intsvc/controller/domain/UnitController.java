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
import se.vgregion.vardplatspusslet.repository.UnitRepository;

import java.util.List;

@Controller
@RequestMapping("/unit")
public class UnitController {

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Unit> getUnits() {
        List<Unit> all = unitRepository.findAll();

        for (Unit unit : all) {
            unit.setBeds(null);
        }

        return all;
    }

    @RequestMapping(value = "/{clinicId}/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Unit getUnit(@PathVariable("clinicId") String clinicId, @PathVariable("id") String id) {
        Clinic clinic = clinicRepository.getOne(clinicId);
        Unit unit = unitRepository.findUnitByIdIsLikeAndClinicIsLike(id, clinic);

        return unit;
    }

}
