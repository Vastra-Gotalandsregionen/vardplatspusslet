package se.vgregion.vardplatspusslet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.vardplatspusslet.domain.jpa.UnitPlannedIn;

public interface UnitPlannedInRepository extends JpaRepository<UnitPlannedIn, Long> {
}
