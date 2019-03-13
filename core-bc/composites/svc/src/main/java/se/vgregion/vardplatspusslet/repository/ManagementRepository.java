package se.vgregion.vardplatspusslet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vgregion.vardplatspusslet.domain.jpa.Management;

import java.util.List;

public interface ManagementRepository extends JpaRepository<Management, String> {

    List<Management> findAllByOrderById();
}
