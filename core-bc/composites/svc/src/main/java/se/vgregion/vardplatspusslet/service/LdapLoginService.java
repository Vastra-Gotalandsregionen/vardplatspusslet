package se.vgregion.vardplatspusslet.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.vgregion.vardplatspusslet.domain.jpa.Role;
import se.vgregion.vardplatspusslet.domain.jpa.User;
import se.vgregion.vardplatspusslet.repository.UserRepository;
import se.vgregion.vardplatspusslet.util.PasswordEncoder;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import java.util.Properties;

/**
 * @author Patrik Björk
 */
@Service
public class LdapLoginService {

    @Autowired
    private UserRepository userRepository;

    @Value("${ldap.service.user.dn}")
    private String serviceUserDN;

    @Value("${ldap.service.user.password}")
    private String serviceUserPassword;

    @Value("${ldap.search.base}")
    private String base;

    @Value("${ldap.url}")
    private String ldapUrl;

    @Value("${default.admin.username}")
    private String defaultAdminUsername;

    @Value("${default.admin.encoded.password}")
    private String defaultAdminEncodedPassword;

    private static final Logger LOGGER = LoggerFactory.getLogger(LdapLoginService.class);

    public User login(String username, String password) throws FailedLoginException {
        return login(username, password, true);
    }

    public User loginWithoutPassword(String username) throws FailedLoginException {
        try {
            return login(username, null, false);
        } catch (Exception e) {
            return userRepository.findOne(username);
        }
    }

    public User loginOffline(String username) throws FailedLoginException {
        return userRepository.findOne(username);
    }

    private User login(String username, String password, boolean verifyPassword) throws FailedLoginException {
        if (username == null || password == null) {
            throw new FailedLoginException();
        }

        username = username.trim().toLowerCase();

        if (username.equals(defaultAdminUsername)
                && PasswordEncoder.getInstance().matches(password, defaultAdminEncodedPassword)) {

            User user = new User();
            user.setId(defaultAdminUsername);
            user.setName("Admin");
            user.setRole(Role.ADMIN);

            if (userRepository.findOne(user.getId()) == null) {
                userRepository.save(user);
            }

            saveUserButKeepSystemSpecificProperties(user);

            return user;
        }

        if (userRepository.findOne(username) == null) {
            throw new FailedLoginException("Du är inte behörig till tjänsten.");
        }

        // first create the service context
        DirContext serviceCtx = null;
        SearchResult result;
        try {
            // use the service user to authenticate
            serviceCtx = createInitialDirContext();

            NamingEnumeration<SearchResult> results = findUser(username, serviceCtx);

            if (results.hasMore()) {
                result = results.next();
            } else {
                throw new AccountNotFoundException("Felaktiga inloggningsuppgifter.");
            }
        } catch (CommunicationException e) {
            throw new RuntimeException(e);
        } catch (AccountNotFoundException | AuthenticationException e) {
            throw new FailedLoginException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new FailedLoginException(e.getMessage());
        } finally {
            if (serviceCtx != null) {
                try {
                    serviceCtx.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        }

        String distinguishedName = result.getNameInNamespace();

        if (verifyPassword) {
            try {
                verifyPassword(password, distinguishedName);
            } catch (NamingException e) {
                LOGGER.error(e.getMessage(), e);
                throw new FailedLoginException("Felaktiga inloggningsuppgifter.");
            }
        }

        try {
            User user = mapToUser(username, result);

            saveUserButKeepSystemSpecificProperties(user);

            return user;
        } catch (NamingException e) {
            throw new RuntimeException("Tekniskt fel.", e);
        }
    }

    private User mapToUser(String username, SearchResult result) throws NamingException {
        User user = new User();
        user.setId(username);
        user.setName((String) (result).getAttributes().get("displayName").get());
//                user.setLastName((String) (result).getAttributes().get("sn").get());
//                user.setMail((String) (result).getAttributes().get("mail").get());
//                user.setDisplayName((String) (result).getAttributes().get("displayName").get());

        Attribute thumbnailPhoto = result.getAttributes().get("thumbnailPhoto");
        if (thumbnailPhoto != null) {
            user.setThumbnailPhoto((byte[]) thumbnailPhoto.get());
        }

        return user;
    }

    private void verifyPassword(String password, String distinguishedName) throws NamingException {
        // attempt another authentication, now with the user
        Properties authEnv = new Properties();
        authEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        authEnv.put(Context.PROVIDER_URL, ldapUrl);
        authEnv.put(Context.SECURITY_PRINCIPAL, distinguishedName);
        authEnv.put(Context.SECURITY_CREDENTIALS, password);
        new InitialDirContext(authEnv);
    }

    private NamingEnumeration<SearchResult> findUser(String username, DirContext serviceCtx) throws NamingException {
        String identifyingAttribute = "cn";
        String identifier = username;

        // we don't need all attributes, just let it generate the identifying one
        String[] attributeFilter = {identifyingAttribute, "givenName", "mail", "sn", "displayName", "thumbnailPhoto"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attributeFilter);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

        // use a search filter to find only the user we want to authenticate
        String searchFilter = "(" + identifyingAttribute + "=" + identifier + ")";
        return serviceCtx.search(base, searchFilter, sc);
    }

    private InitialDirContext createInitialDirContext() throws NamingException {
        Properties serviceEnv = new Properties();
        serviceEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        serviceEnv.put(Context.PROVIDER_URL, ldapUrl);
        serviceEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
        serviceEnv.put(Context.SECURITY_PRINCIPAL, serviceUserDN);
        serviceEnv.put(Context.SECURITY_CREDENTIALS, serviceUserPassword);
        serviceEnv.put("com.sun.jndi.ldap.connect.timeout", "5000");

        return new InitialDirContext(serviceEnv);
    }

    private User saveUserButKeepSystemSpecificProperties(User user) {

        User foundUser = userRepository.findOne(user.getId());

        if (foundUser != null) {
            // Keep these...
            user.setRole(foundUser.getRole());
            user.setInactivated(foundUser.getInactivated());
            user.setUnits(foundUser.getUnits());

            return userRepository.save(user);
        } else {
            throw new RuntimeException("Only persisted users should be able to login.");
        }
    }

    public void synchronizeUserFieldsFromLdap(User user) {
        try {
            NamingEnumeration<SearchResult> result = findUser(user.getId(), createInitialDirContext());

            if (result.hasMore()) {
                User found = mapToUser(user.getId(), result.next());

                user.setName(found.getName());
                user.setThumbnailPhoto(found.getThumbnailPhoto());
            }
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
