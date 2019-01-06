package se.vgregion.vardplatspusslet.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;

import java.util.Collection;
import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, String> {

    @EntityGraph(
            attributePaths = {"clinic", "ssks",
                    "servingClinics", "cleaningAlternatives", "careBurdenCategories", "careBurdenValues"},
            type = EntityGraph.EntityGraphType.LOAD
    )
    List<Unit> findDistinctByClinicIsLike(Clinic clinic);

    @EntityGraph(
            attributePaths = {"beds", "patients", "ssks",
                    "servingClinics", "cleaningAlternatives", "careBurdenCategories", "careBurdenValues"},
            type = EntityGraph.EntityGraphType.LOAD
    )
    Unit findUnitByIdIsLikeAndClinicIsLike(String id, Clinic clinic);

    @EntityGraph(
            attributePaths = {"clinic", "ssks",
                    "servingClinics", "cleaningAlternatives", "careBurdenCategories", "careBurdenValues"},
            type = EntityGraph.EntityGraphType.LOAD
    )
    List<Unit> findDistinctByIdIn(Collection<String> ids, Sort sort);
}
