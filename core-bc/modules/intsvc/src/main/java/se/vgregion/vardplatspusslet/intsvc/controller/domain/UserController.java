package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.vgregion.vardplatspusslet.domain.jpa.User;
import se.vgregion.vardplatspusslet.repository.UserRepository;
import se.vgregion.vardplatspusslet.service.LdapLoginService;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LdapLoginService ldapLoginService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUsers() {
        return userRepository.findAllByOrderById();
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<User> saveUser(@RequestBody User user) {

        ldapLoginService.synchronizeUserFieldsFromLdap(user);

        return ResponseEntity.ok(userRepository.save(user));
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable("userId") String userId) {
        userRepository.delete(userId);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{userId}/thumbnailPhoto", method = RequestMethod.GET, produces = "image/jpg")
    public ResponseEntity<byte[]> getUserThumbnailPhoto(@PathVariable("userId") String userId) {
        User user = userRepository.findOne(userId);

        return ResponseEntity.ok(user.getThumbnailPhoto());
    }

}
