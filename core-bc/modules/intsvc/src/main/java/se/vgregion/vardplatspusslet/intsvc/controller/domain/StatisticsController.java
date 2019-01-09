package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.vardplatspusslet.domain.jpa.PatientLeave;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.repository.PatientLeaveRepository;
import se.vgregion.vardplatspusslet.repository.UnitRepository;
import se.vgregion.vardplatspusslet.util.ExcelUtil;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Controller
@RequestMapping("/statistics")
public class StatisticsController extends BaseController {

    @Autowired
    private PatientLeaveRepository patientLeaveRepository;

    @Autowired
    private UnitRepository unitRepository;

    public StatisticsController(PatientLeaveRepository patientLeaveRepository) {
        this.patientLeaveRepository = patientLeaveRepository;
    }

    @RequestMapping(value = "/patientLeave", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getPatientLeaves(@RequestParam("unitId") String unitId,
                                                   @RequestParam("fromDate") String fromDate,
                                                   @RequestParam("toDate") String toDate)
            throws ParseException, IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));

        Unit unit = unitRepository.findOne(unitId);

        List<PatientLeave> patientLeaves = patientLeaveRepository.findPatientLeaves(
                unit,
                sdf.parse(fromDate),
                sdf.parse(toDate)
        );

        List<String[]> result = createMatrix(sdf, patientLeaves);

        InputStream inputStream = ExcelUtil.exportToStream(unit.getName(), result);

        byte[] bytes = IOUtils.toByteArray(inputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.put("Content-Type", Collections.singletonList("application/excel"));
        headers.put("Content-Length", Collections.singletonList(bytes.length + ""));
        String filename = unit.getName() + "_" + fromDate + "_" + toDate + ".xlsx";
        headers.put("Content-Disposition", Collections.singletonList("attachment; filename=" + filename));

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    static List<String[]> createMatrix(SimpleDateFormat sdf, List<PatientLeave> patientLeaves) {
        ArrayList<String[]> result = new ArrayList<>();

        result.add(new String[]{"Planerat datum", "Faktiskt datum", "Dagar efter planerat datum"});

        int sum = 0;

        for (PatientLeave patientLeave : patientLeaves) {
            Date plannedDate = patientLeave.getPlannedDate();
            Date actualDate = patientLeave.getActualDate();

            String[] array = new String[3];

            String actualDateString = sdf.format(actualDate);
            LocalDate endDateExclusive = LocalDate.parse(actualDateString);

            String plannedDateString;
            if (plannedDate != null) {
                plannedDateString = sdf.format(plannedDate);

                LocalDate startDateInclusive = LocalDate.parse(plannedDateString);

                int daysAfterPlannedDate = Period.between(startDateInclusive, endDateExclusive).getDays();
                sum += daysAfterPlannedDate;

                array[2] = daysAfterPlannedDate + "";
            } else {
                plannedDateString = "Ej angivet";
                array[2] = "Okänt";
            }

            array[0] = plannedDateString;
            array[1] = actualDateString;

            result.add(array);
        }

        result.add(new String[]{"", "Summa dagar efter planerat", sum + ""});

        return result;
    }

}
