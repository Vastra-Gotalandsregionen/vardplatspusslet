package se.vgregion.vardplatspusslet.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.service.InitService;

/**
 * @author Patrik Björk
 */
@Configuration
//@Import({RepositoryRestMvcConfiguration.class})
@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableJpaRepositories(basePackageClasses = ClinicRepository.class, transactionManagerRef = "transactionManager")
@ComponentScan(basePackageClasses = {InitService.class})
public class AppConfigIT {

    @Bean
    public AuthenticationManager authenticationManager() {
        return authentication -> authentication;
    }
}
