package se.vgregion.vardplatspusslet.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.vardplatspusslet.domain.jpa.Bed;
import se.vgregion.vardplatspusslet.domain.jpa.BedStatus;
import se.vgregion.vardplatspusslet.domain.jpa.CareBurdenValue;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.repository.BedRepository;
import se.vgregion.vardplatspusslet.repository.UnitRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class InitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitService.class);

    @Autowired
    private PatientService patientService;

    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private UnitRepository unitRepository;

    @PostConstruct
    public void init() {
        try {
            patientService.removeOrphanPatientsWithCareBurdenChoices();

            List<Unit> units = unitRepository.findAllWithCareBurdenValues();
            Set<CareBurdenValue> cbvs;
            for (Unit unit : units) {
                cbvs = unit.getCareBurdenValues();
                for (CareBurdenValue cbv : cbvs) {
                    if (cbv.getCountedIn() == null) {
                        cbv.setCountedIn(true);
                    }
                }
                unitRepository.save(unit);
            }

            List<Bed> all = bedRepository.findAll();

            for (Bed bed : all) {
                if (bed.getBedStatus() == null) {
                    if (isTrue(bed.getOccupied())) {
                        bed.setBedStatus(BedStatus.OCCUPIED);
                    } else {
                        bed.setBedStatus(BedStatus.VACANT);
                    }
                }
            }
            bedRepository.save(all);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }


    private boolean isTrue(Boolean b) {
        return Boolean.TRUE.equals(b);
    }
}
