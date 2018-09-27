package se.vgregion.vardplatspusslet.domain.util;

import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.domain.json.UnitDTO;

public class DTOUtil {

    public static UnitDTO toDTO(Unit unit) {
        Clinic clinic = unit.getClinic();

        UnitDTO unitDTO = new UnitDTO();
        unitDTO.setId(unit.getId());
        unitDTO.setClinic(new Clinic(clinic.getId(), clinic.getName()));
        unitDTO.setHasLeftDateFeature(unit.getHasLeftDateFeature());
        unitDTO.setName(unit.getName());
        unitDTO.setVacantPatients(unit.getPatients());
        unitDTO.setBeds(unit.getBeds());
        return unitDTO;
    }

}
