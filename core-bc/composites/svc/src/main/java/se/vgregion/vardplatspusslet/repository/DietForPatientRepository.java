package se.vgregion.vardplatspusslet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.vardplatspusslet.domain.jpa.DietForPatient;

public interface DietForPatientRepository extends JpaRepository<DietForPatient, Long> {

}
