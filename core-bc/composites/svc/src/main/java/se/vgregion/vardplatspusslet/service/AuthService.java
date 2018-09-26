package se.vgregion.vardplatspusslet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
 import se.vgregion.vardplatspusslet.domain.jpa.Role;
 import se.vgregion.vardplatspusslet.domain.jpa.User;
 import se.vgregion.vardplatspusslet.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public boolean isLoggedIn(Authentication authentication) {
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public boolean hasRole(Authentication authentication, String role) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()).contains(role);
    }


}
