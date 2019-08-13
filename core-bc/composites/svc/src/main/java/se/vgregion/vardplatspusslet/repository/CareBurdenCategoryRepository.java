package se.vgregion.vardplatspusslet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.vardplatspusslet.domain.jpa.CareBurdenCategory;

public interface CareBurdenCategoryRepository extends JpaRepository<CareBurdenCategory, Integer> {

}
