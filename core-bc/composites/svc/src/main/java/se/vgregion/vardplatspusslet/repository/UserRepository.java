package se.vgregion.vardplatspusslet.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.vgregion.vardplatspusslet.domain.jpa.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    List<User> findAllByOrderById();

    /**
     * This method is useful when the clinics are sought which the user is associated to.
     *
     * @param userId The userId
     * @return The {@link User}
     */
    @EntityGraph(
            attributePaths = {"units.clinic.management"},
            type = EntityGraph.EntityGraphType.LOAD
    )
    User findUserById(String userId);

    @Query("select u from User u where lower(u.id) = lower(:userId)")
    User findUserByIdIgnoreCase(@Param("userId") String userId);
}
