package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import se.vgregion.vardplatspusslet.config.AppTestConfig;
import se.vgregion.vardplatspusslet.domain.jpa.Message;
import se.vgregion.vardplatspusslet.domain.jpa.Role;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;
import se.vgregion.vardplatspusslet.domain.jpa.User;
import se.vgregion.vardplatspusslet.repository.MessageRepository;
import se.vgregion.vardplatspusslet.repository.UnitRepository;
import se.vgregion.vardplatspusslet.repository.UserRepository;
import se.vgregion.vardplatspusslet.service.JwtUtil;
import se.vgregion.vardplatspusslet.service.MessageService;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppTestConfig.class)
public class MessageControllerTest {

    @Autowired
    private MessageController messageController;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void init() {
        Unit unit = new Unit();
        unit.setId("unit1");
        unit.setName("The unit name");

        unitRepository.save(unit);

        Message m1 = new Message();
        m1.setText("the text");
        m1.setHeading("the heading");
        m1.setUnit(unit);

        Message m2 = new Message();
        m2.setText("the text");
        m2.setHeading("the heading");
        m2.setUnit(unit);
        m2.setDate(new Date());

        messageRepository.save(m1);
        messageRepository.save(m2);

        User user = new User();
        user.setId("user1");
        user.setRole(Role.USER);
        user.getUnits().add(unit);
        userRepository.save(user);

        JwtUtil jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSignSecret", "secret");
        jwtUtil.init();

        // Given
        String token = JwtUtil.createToken("user1", "User", new String[]{Role.USER.name()});

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        messageController.setRequest(request);
    }

    @After
    public void destroy() {
        messageRepository.deleteAll();
        userRepository.deleteAll();
        unitRepository.deleteAll();
    }

    @Test
    public void findMessagesByUnit() {
        ResponseEntity<Set<Message>> unitResponse = messageController.findMessagesByUnit("unit1");

        assertEquals(2, unitResponse.getBody().size());
    }

    @Test
    public void saveMessage() {

        // Given
        Unit unit = new Unit();
        unit.setId("unit1");

        Message message = new Message();
        message.setUnit(unit);

        messageController.saveMessage(message);

        // When
        TreeSet<Message> messages = messageService.findByUnit("unit1");

        // Then
        assertEquals(3, messages.size());
    }

    @Test
    public void deleteMessage() {

        // When
        messageController.deleteMessage(messageRepository.findAll().get(0).getId());

        // Then
        assertEquals(1, messageRepository.findAll().size());
    }

    @Test
    public void find() {
        ResponseEntity<Set<Message>> messages = messageController.findMessagesByUnitToday("unit1");

        assertEquals(1, messages.getBody().size());
    }
}