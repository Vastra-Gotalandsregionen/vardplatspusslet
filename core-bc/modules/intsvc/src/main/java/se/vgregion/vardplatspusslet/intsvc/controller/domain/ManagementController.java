package se.vgregion.vardplatspusslet.intsvc.controller.domain;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.vgregion.vardplatspusslet.domain.jpa.Management;
import se.vgregion.vardplatspusslet.repository.ManagementRepository;
import se.vgregion.vardplatspusslet.service.ManagementService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/management")
public class ManagementController extends BaseController{

    @Autowired
    private ManagementService managementService;

    @Autowired
    private ManagementRepository managementRepository;


    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Management> getManagements(HttpServletRequest request)
    {
        String userId = getRequestUserId(request);
        if(userId == null){
            return Collections.emptyList();
        }
        return managementService.getManagements(userId);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Management getManagement(@PathVariable("id") String id){
        return managementRepository.findOne(id);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Management> saveManagemenet(@RequestBody Management management){
        return ResponseEntity.ok(managementRepository.save(management));
    }

    @RequestMapping(value = "/{managementId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteManagement(@PathVariable("id") String managementId){
        managementRepository.delete(managementId);
        return ResponseEntity.noContent().build();
    }
}
