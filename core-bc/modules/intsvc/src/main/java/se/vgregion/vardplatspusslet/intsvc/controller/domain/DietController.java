package se.vgregion.vardplatspusslet.intsvc.controller.domain;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.vardplatspusslet.domain.jpa.Diet;
import se.vgregion.vardplatspusslet.repository.DietRepository;

import java.util.List;

@Controller
@RequestMapping("/diet")
public class DietController {

    @Autowired
    DietRepository dietRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Diet> getDiet()
    {
        return dietRepository.findAll();
    }

}
