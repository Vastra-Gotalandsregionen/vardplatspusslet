package se.vgregion.vardplatspusslet.intsvc.controller.domain;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.vardplatspusslet.domain.jpa.Management;
import se.vgregion.vardplatspusslet.repository.ManagementRepository;
import se.vgregion.vardplatspusslet.service.ManagementService;

import javax.servlet.http.HttpServletRequest;
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
    @PreAuthorize("@authService.hasRole(authentication, 'ADMIN')")
    public ResponseEntity<Object> saveManagemenet(@RequestBody Management management,
                                                  @RequestParam(value = "newManagement", required = false) Boolean newManagement){

        if (Boolean.TRUE.equals(newManagement) && managementService.managementExists(management.getId())) {

            return new ResponseEntity<>(
                    new ApiError("En förvaltning med ID=" + management.getId() + " finns redan."),
                    new HttpHeaders(),
                    HttpStatus.BAD_REQUEST
            );
        }


        return ResponseEntity.ok(managementRepository.save(management));
    }

    @RequestMapping(value = "/{managementId}", method = RequestMethod.DELETE)
    @PreAuthorize("@authService.hasRole(authentication, 'ADMIN')")
    public ResponseEntity deleteManagement(@PathVariable("managementId") String managementId){
        managementRepository.delete(managementId);
        return ResponseEntity.noContent().build();
    }
}
