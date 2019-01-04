package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import se.vgregion.vardplatspusslet.domain.jpa.Role;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.domain.jpa.User;
import se.vgregion.vardplatspusslet.domain.json.UserSaveRequest;
import se.vgregion.vardplatspusslet.repository.UnitRepository;
import se.vgregion.vardplatspusslet.repository.UserRepository;
import se.vgregion.vardplatspusslet.service.JwtUtil;
import se.vgregion.vardplatspusslet.service.LdapLoginService;
import se.vgregion.vardplatspusslet.service.UserService;
import se.vgregion.vardplatspusslet.testrepository.TestUnitRepository;
import se.vgregion.vardplatspusslet.testrepository.TestUserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class UserControllerTest {

    private UserRepository userRepository = new TestUserRepository();
    private UnitRepository unitRepository = new TestUnitRepository();
    private LdapLoginService ldapLoginService = mock(LdapLoginService.class);

    private UserController userController = new UserController(userRepository, unitRepository, ldapLoginService);

    @Before
    public void setup() {

        UserService userService = new UserService(userRepository, unitRepository);

        ReflectionTestUtils.setField(userController, "userService", userService);

        Unit unit1 = new Unit();
        Unit unit2 = new Unit();

        unit1.setId("unit1");
        unit2.setId("unit2");

        unitRepository.save(unit1);
        unitRepository.save(unit2);

        User admin = new User();
        admin.setId("admin1");
        admin.setRole(Role.ADMIN);

        User user1 = new User();
        user1.setId("user1");
        user1.setRole(Role.USER);
        user1.setUnits(new HashSet<>(Collections.singletonList(unit2)));

        User user2 = new User();
        user2.setId("user2");
        user2.setRole(Role.USER);
        user2.setUnits(new HashSet<>(Arrays.asList(unit1, unit2)));

        userRepository.save(admin);
        userRepository.save(user1);
        userRepository.save(user2);

        JwtUtil jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSignSecret", "secret");
        jwtUtil.init();
    }

    @Test
    public void getUsers() {

        // Given
        String token = JwtUtil.createToken("user1", "User", new String[]{Role.USER.name()});

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        // When
        List<User> users = userController.getUsers(request);

        // Then - user1 has permission to see himself and user2
        assertEquals(2, users.size());
        // Also test order by id.
        assertEquals("user1", users.get(0).getId());
        assertEquals("user2", users.get(1).getId());
    }

    @Test
    public void saveUserAsAdmin() {

        // Given
        Set<String> allUnitIds = unitRepository.findAll().stream().map(Unit::getId).collect(Collectors.toSet());

        UserSaveRequest userSaveRequest = new UserSaveRequest();
        userSaveRequest.setId("u3");
        userSaveRequest.setRole(Role.USER);
        userSaveRequest.setUnitIds(allUnitIds);

        String token = JwtUtil.createToken("admin1", "Admin", new String[]{Role.ADMIN.name()});

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        // When
        userController.saveUser(userSaveRequest, request);

        // Then - Admin has permission to save user with all two units.
        User u3 = userRepository.findOne("u3");
        assertEquals(2, u3.getUnits().size());

    }

    @Test
    public void saveUserAsUser() {

        // Given
        Set<String> allUnitIds = unitRepository.findAll().stream().map(Unit::getId).collect(Collectors.toSet());

        UserSaveRequest userSaveRequest = new UserSaveRequest();
        userSaveRequest.setId("user3");
        userSaveRequest.setRole(Role.USER);
        userSaveRequest.setUnitIds(allUnitIds);

        // user1 has permission to only unit2.
        String token = JwtUtil.createToken("user1", "User", new String[]{Role.USER.name()});

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        // When
        userController.saveUser(userSaveRequest, request);

        // Then - user1 only has permission to save user with unit2.
        User u3 = userRepository.findOne("user3");
        assertEquals(1, u3.getUnits().size());
        assertEquals("unit2", u3.getUnits().iterator().next().getId());
    }

    @Test
    public void saveUserAsAdminRemoveUnits() {

        // Given
        UserSaveRequest userSaveRequest = new UserSaveRequest();
        userSaveRequest.setId("user2");
        userSaveRequest.setRole(Role.USER);
        userSaveRequest.setUnitIds(Collections.emptySet());

        String token = JwtUtil.createToken("admin1", "Admin", new String[]{Role.ADMIN.name()});

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        // When
        userController.saveUser(userSaveRequest, request);

        // Then - Admin has permission to remove all two units from user.
        User u2 = userRepository.findOne("user2");
        assertEquals(0, u2.getUnits().size());

    }

    @Test
    public void saveUserAsUserRemoveUnits() {

        // Given
        UserSaveRequest userSaveRequest = new UserSaveRequest();
        userSaveRequest.setId("user2");
        userSaveRequest.setRole(Role.USER);
        userSaveRequest.setUnitIds(Collections.emptySet());

        String token = JwtUtil.createToken("user1", "User", new String[]{Role.USER.name()});

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        // When
        userController.saveUser(userSaveRequest, request);

        // Then - user1 has permission to remove only unit2 from user.
        User u2 = userRepository.findOne("user2");
        assertEquals(1, u2.getUnits().size());
        assertEquals("unit1", u2.getUnits().iterator().next().getId());
    }

    @Test
    public void deleteUserAllowed() {

        // Given
        String token = JwtUtil.createToken("user2", "User", new String[]{Role.USER.name()});

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        // When
        ResponseEntity responseEntity = userController.deleteUser("user1", request);

        // Then
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void deleteUserNotAllowed() {

        // Given
        String token = JwtUtil.createToken("user1", "User", new String[]{Role.USER.name()});

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        // When
        ResponseEntity responseEntity = userController.deleteUser("user2", request);

        // Then
        assertTrue(responseEntity.getStatusCode().is4xxClientError());
    }

}