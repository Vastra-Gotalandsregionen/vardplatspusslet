package se.vgregion.vardplatspusslet.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@Transactional
public class InitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitService.class);

    @Autowired
    private PatientService patientService;

    @PostConstruct
    public void init() {
        try {
            patientService.removeOrphanPatientsWithCareBurdenChoices();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
