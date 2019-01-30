package se.vgregion.vardplatspusslet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.vardplatspusslet.domain.jpa.SevenDaysPlaningUnit;

public interface SevenDaysPlaningRepository extends JpaRepository<SevenDaysPlaningUnit, Long> {
}
