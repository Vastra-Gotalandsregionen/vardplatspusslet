package se.vgregion.vardplatspusslet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.vardplatspusslet.domain.jpa.Bed;

public interface BedRepository extends JpaRepository<Bed, Integer> {

}
