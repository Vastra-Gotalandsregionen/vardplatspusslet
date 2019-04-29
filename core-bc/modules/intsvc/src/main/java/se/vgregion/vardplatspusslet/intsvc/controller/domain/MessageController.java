package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.vardplatspusslet.domain.jpa.Message;
import se.vgregion.vardplatspusslet.domain.jpa.Role;
import se.vgregion.vardplatspusslet.domain.jpa.User;
import se.vgregion.vardplatspusslet.repository.MessageRepository;
import se.vgregion.vardplatspusslet.service.MessageService;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Controller
@RequestMapping("/message")
public class MessageController extends BaseController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @RequestMapping(value = "/{unitId}", method = RequestMethod.GET)
    @ResponseBody
//    @PreAuthorize("@authService.hasRole(authentication, 'ADMIN')")
    public ResponseEntity<Set<Message>> findMessagesByUnit(@PathVariable("unitId") String unitId) {

        TreeSet<Message> messages = messageService.findByUnit(unitId);

        return ResponseEntity.ok(messages);
    }

    @RequestMapping(value = "/{unitId}/today", method = RequestMethod.GET)
    @ResponseBody
//    @PreAuthorize("@authService.hasRole(authentication, 'ADMIN')")
    public ResponseEntity<Set<Message>> findMessagesByUnitToday(@PathVariable("unitId") String unitId) {

        TreeSet<Message> messages = messageService.findByUnitToday(unitId);

        return ResponseEntity.ok(messages);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResponseEntity saveMessage(@RequestBody Message message) {

        Optional<User> user = getUser();

        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError("Du måste vara inloggad."));
        }

        if (!user.get().getRole().equals(Role.ADMIN) && !user.get().getUnits().contains(message.getUnit())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiError("Du har inte behörighet till avdelningen.")
            );
        }

        messageRepository.save(message);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteMessage(@PathVariable("messageId") Long messageId) {

        Optional<User> user = getUser();

        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError("Du måste vara inloggad."));
        }

        Message message = messageRepository.findByIdWithUnit(messageId);
        if (!user.get().getRole().equals(Role.ADMIN) && !user.get().getUnits().contains(message.getUnit())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiError("Du har inte behörighet till avdelningen.")
            );
        }

        messageRepository.delete(messageId);

        return ResponseEntity.noContent().build();
    }
}
