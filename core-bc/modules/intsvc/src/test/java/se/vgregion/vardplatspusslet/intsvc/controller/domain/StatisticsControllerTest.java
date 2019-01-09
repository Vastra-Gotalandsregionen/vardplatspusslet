package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import org.junit.Test;
import se.vgregion.vardplatspusslet.domain.jpa.PatientLeave;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class StatisticsControllerTest {

    @Test
    public void createMatrix() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        PatientLeave patientLeave = new PatientLeave();
        patientLeave.setActualDate(new Date());
        patientLeave.setPlannedDate(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)));

        List<PatientLeave> patientLeaves = new ArrayList<>();
        patientLeaves.add(patientLeave);

        List<String[]> matrix = StatisticsController.createMatrix(sdf, patientLeaves);

        // One for headings, one the the single PatientLeave, and one for summary.
        assertEquals(3, matrix.size());
    }
}