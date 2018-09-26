package se.vgregion.vardplatspusslet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.vardplatspusslet.domain.jpa.Bed;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;

public interface ClinicRepository extends JpaRepository<Clinic, String> {

}
