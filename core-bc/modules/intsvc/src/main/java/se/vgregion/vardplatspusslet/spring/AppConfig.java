package se.vgregion.vardplatspusslet.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.service.InitService;

/**
 * @author Patrik Björk
 */
@Configuration
@EnableScheduling
@EnableJpaRepositories(basePackageClasses = ClinicRepository.class, transactionManagerRef = "transactionManager")
@ComponentScan(basePackageClasses = {InitService.class})
public class AppConfig {

}
