package se.vgregion.vardplatspusslet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.vardplatspusslet.domain.jpa.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    List<User> findAllByOrderById();
}