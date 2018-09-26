package se.vgregion.vardplatspusslet.domain.util;

import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.domain.json.UnitDTO;

public class DTOUtil {

    public static UnitDTO toDTO(Unit unit) {
        UnitDTO unitDTO = new UnitDTO();
        unitDTO.setId(unit.getId());
//        unitDTO.setClinic(unit.getClinic());
        unitDTO.setHasLeftDateFeature(unit.getHasLeftDateFeature());
        unitDTO.setName(unit.getName());
        unitDTO.setVacantPatients(unit.getPatients());
        unitDTO.setBeds(unit.getBeds());
        return unitDTO;
    }

}
