package se.vgregion.vardplatspusslet.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.vardplatspusslet.domain.jpa.Bed;
import se.vgregion.vardplatspusslet.domain.jpa.BedStatus;
import se.vgregion.vardplatspusslet.repository.BedRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Transactional
public class InitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitService.class);

    @Autowired
    private PatientService patientService;

    @Autowired
    private BedRepository bedRepository;

    @PostConstruct
    public void init() {
        try {
            patientService.removeOrphanPatientsWithCareBurdenChoices();

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
