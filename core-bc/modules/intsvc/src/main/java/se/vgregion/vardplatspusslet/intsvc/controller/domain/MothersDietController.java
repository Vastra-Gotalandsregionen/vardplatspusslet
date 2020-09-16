package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.vardplatspusslet.domain.jpa.MothersDiet;
import se.vgregion.vardplatspusslet.repository.MothersDietRepository;

import java.util.List;

@Controller
@RequestMapping("/mothersdiet")
public class MothersDietController {

    @Autowired
    MothersDietRepository mothersDietRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<MothersDiet> getMotherDiet(){
        return mothersDietRepository.findAll();
    }
}
