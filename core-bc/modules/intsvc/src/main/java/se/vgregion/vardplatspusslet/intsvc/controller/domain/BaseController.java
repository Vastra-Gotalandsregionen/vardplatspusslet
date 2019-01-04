package se.vgregion.vardplatspusslet.intsvc.controller.domain;

import se.vgregion.vardplatspusslet.intsvc.controller.util.HttpUtil;

import javax.servlet.http.HttpServletRequest;

abstract class BaseController {

    protected String getRequestUserId(HttpServletRequest request) {
        return HttpUtil.getUserIdFromRequest(request).orElse(null);
    }

}
