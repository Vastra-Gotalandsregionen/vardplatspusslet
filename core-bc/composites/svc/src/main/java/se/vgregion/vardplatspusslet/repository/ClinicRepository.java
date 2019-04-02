package se.vgregion.vardplatspusslet.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Management;

import java.util.Collection;
import java.util.List;

public interface ClinicRepository extends JpaRepository<Clinic, String> {

    @EntityGraph(
            attributePaths = {"management"},
            type = EntityGraph.EntityGraphType.LOAD
    )
    Clinic findOne(String id);

    @EntityGraph(
            attributePaths = {"management"},
            type = EntityGraph.EntityGraphType.LOAD
    )
    List<Clinic> findAllByOrderById();
    List<Clinic> findDistinctByManagementIsLike(Management management);

    @EntityGraph(
            attributePaths = {"management"},
            type = EntityGraph.EntityGraphType.LOAD
    )
    List<Clinic> findDistinctByIdIn(Collection<String> ids, Sort sort);
}
