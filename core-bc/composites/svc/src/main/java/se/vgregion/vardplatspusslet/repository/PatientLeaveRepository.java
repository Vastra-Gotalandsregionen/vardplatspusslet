package se.vgregion.vardplatspusslet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.vardplatspusslet.domain.jpa.PatientLeave;
import se.vgregion.vardplatspusslet.repository.extension.PatientLeaveExtendedRepository;

public interface PatientLeaveRepository extends JpaRepository<PatientLeave, Long>, PatientLeaveExtendedRepository {

}
