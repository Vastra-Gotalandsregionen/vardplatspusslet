package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import se.vgregion.vardplatspusslet.domain.jpa.Clinic;
import se.vgregion.vardplatspusslet.domain.jpa.Role;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.domain.jpa.User;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.repository.UnitRepository;
import se.vgregion.vardplatspusslet.repository.UserRepository;
import se.vgregion.vardplatspusslet.testrepository.TestClinicRepository;
import se.vgregion.vardplatspusslet.testrepository.TestUnitRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UnitControllerTest {

    @InjectMocks
    private UnitController unitController = new UnitController();

    private UnitRepository unitRepository = new TestUnitRepository();

    @Mock
    private UserRepository userRepository;

    private ClinicRepository clinicRepository = new TestClinicRepository();

    @Before
    public void setup() {

        ReflectionTestUtils.setField(unitController, "unitRepository", unitRepository);
        ReflectionTestUtils.setField(unitController, "clinicRepository", clinicRepository);

        Clinic clinic1 = new Clinic();
        Clinic clinic2 = new Clinic();

        clinic1.setId("c1");
        clinic2.setId("c2");

        clinicRepository.save(clinic1);
        clinicRepository.save(clinic2);

        Unit unit11 = new Unit();
        Unit unit12 = new Unit();
        Unit unit21 = new Unit();
        Unit unit22 = new Unit();

        unit11.setId("u11");
        unit12.setId("u12");
        unit21.setId("u21");
        unit22.setId("u22");

        unit11.setClinic(clinic1);
        unit12.setClinic(clinic1);
        unit21.setClinic(clinic2);
        unit22.setClinic(clinic2);

        unitRepository.save(unit11);
        unitRepository.save(unit12);
        unitRepository.save(unit21);
        unitRepository.save(unit22);
    }

    @Test
    public void getAllUnits() {

        // Given
        User user = new User();
        user.setRole(Role.ADMIN);
        when(userRepository.findOne((String) any())).thenReturn(user);

        // When
        List<Unit> units = unitController.getUnits(null, mock(HttpServletRequest.class));

        // Then
        assertEquals(4, units.size());
    }

    @Test
    public void getUnitsForClinic() {

        // Given
        User user = new User();
        user.setRole(Role.ADMIN);
        when(userRepository.findOne((String) any())).thenReturn(user);

        // When
        List<Unit> units = unitController.getUnits("c1", mock(HttpServletRequest.class));

        // Then
        assertEquals(2, units.size());
    }

    @Test
    public void getUnitsForUser() {

        // Given
        User user = new User();
        user.setUnits(new HashSet<>(unitRepository.findAll().subList(0, 2)));
        user.setRole(Role.USER);
        when(userRepository.findOne((String) any())).thenReturn(user);

        // When
        List<Unit> units = unitController.getUnits(null, mock(HttpServletRequest.class));

        // Then
        assertEquals(2, units.size());
    }

    @Test
    public void getUnitsNoUser() {

        // Given
        when(userRepository.findOne((String) any())).thenReturn(null);

        // When
        List<Unit> units = unitController.getUnits(null, mock(HttpServletRequest.class));

        // Then
        assertEquals(0, units.size());
    }

    @Test
    public void getUnit() {
    }

    @Test
    public void saveUnit() {
    }

    @Test
    public void deleteUnit() {
    }
}