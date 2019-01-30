package se.vgregion.vardplatspusslet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.SevenDaysPlaningUnit;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.domain.jpa.UnitPlannedIn;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.repository.SevenDaysPlaningRepository;
import se.vgregion.vardplatspusslet.repository.UnitRepository;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class SevenDaysPlanningUnitService {

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private SevenDaysPlaningRepository sevenDaysPlaningRepository;

    public void save(String clinicId, String unitId, Collection<SevenDaysPlaningUnit> sevenDaysPlaningUnits) {

        Clinic clinicRef = clinicRepository.getOne(clinicId);

        Unit unit = unitRepository.findUnitByIdIsLikeAndClinicIsLike(unitId, clinicRef);

        for (SevenDaysPlaningUnit sevenDaysPlaningUnit1 : sevenDaysPlaningUnits) {
            sevenDaysPlaningUnit1.setUnit(unit);
           // removeItemsNotInIncomingCollection(sevenDaysPlaningRepository, sevenDaysPlaningUnit1, unit.getSevenDaysPlaningUnits());
            sevenDaysPlaningRepository.save(sevenDaysPlaningUnit1);
        }
    }


    void removeItemsNotInIncomingCollection(JpaRepository repository, Object example, Collection incomingCollection) {
        List currentlySaved = repository.findAll(Example.of(example));
        // Any items not in the incoming collection is to be removed
        currentlySaved.removeAll(incomingCollection);
        repository.delete(currentlySaved);
    }
}


