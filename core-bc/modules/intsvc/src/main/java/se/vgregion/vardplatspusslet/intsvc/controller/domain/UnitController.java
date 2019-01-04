package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.intsvc.pdf.PdfGenerating;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.repository.UnitRepository;
import se.vgregion.vardplatspusslet.service.UnitService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/unit")
public class UnitController extends BaseController {

    @Autowired
    private UnitService unitService;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<Unit> getUnits(@RequestParam(value = "clinic", required = false) String clinicId,
                               HttpServletRequest request) {

        String userId = getRequestUserId(request);

        return unitService.getUnits(clinicId, userId);
    }

    @RequestMapping(value = "/{clinicId}/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Unit> getUnit(@PathVariable("clinicId") String clinicId, @PathVariable("id") String id) {
        Clinic clinic = clinicRepository.getOne(clinicId);

        Unit unit = unitRepository.findUnitByIdIsLikeAndClinicIsLike(id, clinic);

        if (unit != null) {
            return ResponseEntity.ok(unit);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{clinicId}/{id}/kostLista", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> getUnitForPdf(@PathVariable("clinicId") String clinicId, @PathVariable("id") String id) throws  DocumentException {
        Clinic clinic = clinicRepository.getOne(clinicId);
        Unit unit = unitRepository.findUnitByIdIsLikeAndClinicIsLike(id, clinic);
        if (unit != null) {
            PdfGenerating pfg = new PdfGenerating();
            byte[] content = pfg.createPdf(unit);
            HttpHeaders headers = new HttpHeaders();
            headers.put("Content-Disposition", Collections.singletonList("inline"));
            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Unit> saveUnit(@RequestBody Unit unit,
                                         @RequestParam(value = "keepBeds", required = false) Boolean keepBeds) {
        unitService.save(unit, keepBeds);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{unitId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUnit(@PathVariable("unitId") String unitId) {

        unitRepository.delete(unitId);

        return ResponseEntity.noContent().build();
    }

}
