package se.vgregion.vardplatspusslet.intsvc.controller.domain;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.vardplatspusslet.domain.jpa.ChildrensDiet;
import se.vgregion.vardplatspusslet.repository.ChildrensDietRepository;

import java.util.List;

@Controller
@RequestMapping("/childrensdiet")
public class ChildrensDietController {

    @Autowired
    ChildrensDietRepository childrensDietRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<ChildrensDiet> getChildrensDiet()
    {
        return childrensDietRepository.findAll();
    }
}
