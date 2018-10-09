package se.vgregion.vardplatspusslet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.vardplatspusslet.domain.jpa.Bed;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.repository.UnitRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UnitService {

    @Autowired
    private UnitRepository unitRepository;
    public Unit save(Unit unit, Boolean keepBeds) {
        // We need to remove all beds from the unit first, or we will get a constraint exception.
        List<Bed> beds = new ArrayList<>();
        if (keepBeds != null && keepBeds) {
            // Take currently persisted, i.e. keep persisted.
            Unit found = unitRepository.findOne(unit.getId());
            if (found != null) {
                beds = found.getBeds();
            }
        } else {
            // Take from the request.
            beds = unit.getBeds();
        }

        unit.setBeds(null);
        unitRepository.save(unit);
        unit.setBeds(beds);

        return unitRepository.save(unit);
    }
}
