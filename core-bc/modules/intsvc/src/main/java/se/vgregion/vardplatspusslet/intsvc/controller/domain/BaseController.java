package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import org.springframework.beans.factory.annotation.Autowired;
import se.vgregion.vardplatspusslet.domain.jpa.User;
import se.vgregion.vardplatspusslet.intsvc.controller.util.HttpUtil;
import se.vgregion.vardplatspusslet.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

abstract class BaseController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpServletRequest request;

    protected String getRequestUserId(HttpServletRequest request) {
        return HttpUtil.getUserIdFromRequest(request).orElse(null);
    }

    protected Optional<User> getUser() {
        Optional<String> userIdFromRequest = HttpUtil.getUserIdFromRequest(request);

        if (userIdFromRequest.isPresent()) {
            return Optional.ofNullable(userRepository.findUserById(userIdFromRequest.get()));
        } else {
            return Optional.empty();
        }
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
