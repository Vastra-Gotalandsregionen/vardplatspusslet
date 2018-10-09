package se.vgregion.vardplatspusslet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;

import java.util.List;

public interface ClinicRepository extends JpaRepository<Clinic, String> {

    List<Clinic> findAllByOrderById();
}
