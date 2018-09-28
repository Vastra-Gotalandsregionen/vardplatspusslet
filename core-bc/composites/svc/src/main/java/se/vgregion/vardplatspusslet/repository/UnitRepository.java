package se.vgregion.vardplatspusslet.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;

public interface UnitRepository extends JpaRepository<Unit, String> {

    @EntityGraph(value = "Unit.beds", type = EntityGraph.EntityGraphType.LOAD)
    Unit findUnitByIdIsLikeAndClinicIsLike(String id, Clinic clinic);
}
