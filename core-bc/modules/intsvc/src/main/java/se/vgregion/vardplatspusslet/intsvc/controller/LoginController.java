package se.vgregion.vardplatspusslet.intsvc.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import se.vgregion.vardplatspusslet.domain.jpa.Role;
import se.vgregion.vardplatspusslet.domain.jpa.User;
import se.vgregion.vardplatspusslet.domain.json.LoginRequest;
import se.vgregion.vardplatspusslet.domain.json.TokenResponse;
import se.vgregion.vardplatspusslet.intsvc.controller.domain.ApiError;
import se.vgregion.vardplatspusslet.repository.UserRepository;
import se.vgregion.vardplatspusslet.service.JwtUtil;
import se.vgregion.vardplatspusslet.service.LdapLoginService;
import se.vgregion.vardplatspusslet.util.CommonUtil;

import javax.security.auth.login.FailedLoginException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Patrik Björk
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LdapLoginService ldapLoginService;

    @Autowired
    private UserRepository userRepository;

//    @Value("${impersonate.enabled}")
//    private boolean impersonateEnabled;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            User user = null;
            if (request.getHeader("iv-user") != null) {
                user = ldapLoginService.loginOffline(request.getHeader("iv-user"));
            } else {
                user = ldapLoginService.login(loginRequest.getUsername(), loginRequest.getPassword());
                if (user.getInactivated() != null && user.getInactivated()) {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            }

            String[] roles = getRoles(user);

            String token = JwtUtil.createToken(user.getId(), user.getName(), roles, CommonUtil.getUnitIds(user));

            return ResponseEntity.ok(new TokenResponse(token));
        } catch (FailedLoginException e) {
            LOGGER.warn(e.getClass().getCanonicalName() + " - " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError(e.getMessage()));
        }
    }

    @RequestMapping(value = "/renew", method = RequestMethod.POST)
    public ResponseEntity renewJwt(@RequestBody String jwt) {

        try {
            DecodedJWT decodedJWT = JwtUtil.verify(jwt);

            User user = userRepository.findOne(decodedJWT.getSubject());

            String[] roles = getRoles(user);

            String token = JwtUtil.createToken(user.getId(), user.getName(), roles, CommonUtil.getUnitIds(user));

            return ResponseEntity.ok(new TokenResponse(token));
        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiError(e.getMessage()));
        }

    }

    String[] getRoles(User user) {
        String roleName = user.getRole().name();

        String[] roles;
        if (Role.ADMIN.name().equals(roleName) /*&& impersonateEnabled*/) {
            roles = new String[]{roleName, Role.IMPERSONATE.name()};
        } else {
            roles = new String[]{roleName};
        }
        return roles;
    }

    @RequestMapping(value = "/impersonate", method = RequestMethod.POST)
    public ResponseEntity<String> impersonate(@RequestBody User userToImpersonate, HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String jwtToken = authorizationHeader.substring("Bearer".length()).trim();

        DecodedJWT jwt;
        try {
            jwt = JwtUtil.verify(jwtToken);

            List<String> roles = jwt.getClaim("roles").asList(String.class);

            if (roles.contains(Role.IMPERSONATE.name())) {
                User impersonated = ldapLoginService.loginWithoutPassword(userToImpersonate.getId());

                String[] impersonatedRoles = getRoles(impersonated);

                String[] unitIds = CommonUtil.getUnitIds(impersonated);

                String token = JwtUtil.createToken(impersonated.getId(), impersonated.getName(),
                        impersonatedRoles, unitIds);

                return ResponseEntity.ok(token);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (FailedLoginException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
