package se.vgregion.vardplatspusslet.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.vgregion.vardplatspusslet.domain.jpa.Bed;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Patient;
import se.vgregion.vardplatspusslet.domain.jpa.Ssk;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.repository.BedRepository;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.repository.PatientRepository;
import se.vgregion.vardplatspusslet.repository.UnitRepository;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test.xml")
@Transactional
public class BedServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BedServiceTest.class);

    @Autowired
    private BedService bedService;

    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private PatientRepository patientRepository;

    private Ssk ssk = new Ssk();

    @Before
    public void setup() {
        Clinic clinic = new Clinic("clinic1", "Klinik 1");
        clinicRepository.save(clinic);

        Unit unit = new Unit();
        unit.setId("unit1");
        unit.setClinic(clinic);
        unit.getSsks().add(ssk);

        unit = unitRepository.save(unit);
        ssk = unit.getSsks().iterator().next();

        Patient patient = new Patient();
        patient.setLabel("pat1");

        Bed bed = new Bed();
        bed.setPatient(patient);
        bed.setUnit(unit);
        bed.setSsk(ssk);

        bedService.save("clinic1", "unit1", bed);
    }

    @Test
    public void save() {
        bedService.save("clinic1", "unit1", new Bed());

        assertEquals(2, bedRepository.findAll().size());
    }

    @Test
    public void patientHasLeft() {
        assertEquals(1, patientRepository.findAll().size());

        Bed bed = bedRepository.findAll().get(0);
        bedService.patientHasLeft(bed);

        assertNull(bed.getPatient());
        assertEquals(0, patientRepository.findAll().size());

        assertEquals(ssk, bed.getSsk());
    }

    @Test
    public void patientHasLeftResetSsk() {
        assertEquals(1, patientRepository.findAll().size());

        Bed bed = bedRepository.findAll().get(0);
        bed.getUnit().setResetSskOnHasLeft(true);

        bedService.patientHasLeft(bed);

        assertNull(bed.getPatient());
        assertEquals(0, patientRepository.findAll().size());

        assertNull(bed.getSsk());
    }

}
