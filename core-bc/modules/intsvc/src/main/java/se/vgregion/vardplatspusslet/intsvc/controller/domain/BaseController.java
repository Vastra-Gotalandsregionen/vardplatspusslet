package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import se.vgregion.vardplatspusslet.domain.jpa.User;
import se.vgregion.vardplatspusslet.intsvc.controller.util.HttpUtil;
import se.vgregion.vardplatspusslet.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

abstract class BaseController {

    abstract UserRepository getUserRepository();

    protected User getRequestUser(HttpServletRequest request) {
        Optional<String> userIdFromRequest = HttpUtil.getUserIdFromRequest(request);

        return userIdFromRequest.map(s -> getUserRepository().findOne(s)).orElse(null);
    }

}
