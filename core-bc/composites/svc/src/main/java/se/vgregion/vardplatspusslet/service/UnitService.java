package se.vgregion.vardplatspusslet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.repository.BedRepository;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.repository.UnitRepository;

@Service
@Transactional
public class UnitService {

    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    public Unit save(Unit unit) {
        if (unit.getClinic() != null) {
            unit.setClinic(clinicRepository.getOne(unit.getClinic().getId()));
        }

        // Possibly keep beds
        Unit one = unitRepository.findOne(unit.getId());
        if (one != null) {
            unit.setBeds(one.getBeds());
        }

        return unitRepository.save(unit);
    }
}
