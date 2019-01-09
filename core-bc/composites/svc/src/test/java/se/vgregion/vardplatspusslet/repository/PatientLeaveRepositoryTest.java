package se.vgregion.vardplatspusslet.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.PatientLeave;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test.xml")
@Transactional
public class PatientLeaveRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientLeaveRepositoryTest.class);

    @Autowired
    private PatientLeaveRepository patientLeaveRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Before
    public void setup() {
        Clinic clinic = new Clinic("clinic1", "Klinik 1");
        clinicRepository.save(clinic);

        Unit unit = new Unit();
        unit.setId("unit1");
        unit.setName("Avdelning 1");
        unit.setClinic(clinic);

        unitRepository.save(unit);
    }

    @Test
    public void save() {
        patientLeaveRepository.save(new PatientLeave(new Date(), new Date(), unitRepository.findOne("unit1")));

        assertEquals(1, patientLeaveRepository.findAll().size());
    }

    @Test
    public void findPatientLeaves() {

        save();

        Date from = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));
        Date to = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        Unit unit = unitRepository.findOne("unit1");

        List<PatientLeave> patientLeaves = patientLeaveRepository.findPatientLeaves(unit, from, to);

        assertNotNull(patientLeaves.get(0).getId());
        assertEquals("Avdelning 1", patientLeaves.get(0).getUnit().getName());
    }

}
