package se.vgregion.vardplatspusslet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.vardplatspusslet.domain.jpa.Bed;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.repository.BedRepository;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.repository.UnitRepository;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class BedService {

    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    public void deleteBed(String clinicId, String unitId, Integer bedId) {
        Clinic clinicRef = clinicRepository.getOne(clinicId);

        Unit unit = unitRepository.findUnitByIdIsLikeAndClinicIsLike(unitId, clinicRef);

        List<Bed> beds = unit.getBeds();

        beds.remove(bedRepository.getOne(bedId));

        Bed[] bedsArrays = beds.toArray(new Bed[]{});

        // Need to do this way or otherwise Hibernate will fail with a unique constraint exception.
        unit.getBeds().clear();
        unitRepository.flush();
        unit.getBeds().addAll(Arrays.asList(bedsArrays));
        unitRepository.flush();

        unitRepository.save(unit);

        bedRepository.delete(bedId);
    }
}
