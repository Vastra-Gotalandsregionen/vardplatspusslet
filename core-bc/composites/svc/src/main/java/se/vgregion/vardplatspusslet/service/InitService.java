package se.vgregion.vardplatspusslet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@Transactional
public class InitService {

    @Autowired
    private PatientService patientService;

    @PostConstruct
    public void init() {
        patientService.removeOrphanPatientsWithCareBurdenChoices();
    }
}
