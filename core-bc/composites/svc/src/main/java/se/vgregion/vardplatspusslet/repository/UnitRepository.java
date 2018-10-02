package se.vgregion.vardplatspusslet.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;

import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, String> {

    List<Unit> findUnitsByClinicIsLike(Clinic clinic);

    @EntityGraph(value = "Unit.beds", type = EntityGraph.EntityGraphType.LOAD)
    Unit findUnitByIdIsLikeAndClinicIsLike(String id, Clinic clinic);
}
