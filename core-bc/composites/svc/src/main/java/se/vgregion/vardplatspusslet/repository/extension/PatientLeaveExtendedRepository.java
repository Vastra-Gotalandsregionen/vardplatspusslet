package se.vgregion.vardplatspusslet.repository.extension;

import se.vgregion.vardplatspusslet.domain.jpa.PatientLeave;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;

import java.util.Date;
import java.util.List;

public interface PatientLeaveExtendedRepository {

    List<PatientLeave> findPatientLeaves(Unit unit, Date actualFromDate, Date actualToDate);
}
