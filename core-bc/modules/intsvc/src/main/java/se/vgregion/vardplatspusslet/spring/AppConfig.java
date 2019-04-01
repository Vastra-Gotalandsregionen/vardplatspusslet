package se.vgregion.vardplatspusslet.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.service.InitService;

import javax.sql.DataSource;

/**
 * @author Patrik Björk
 */
@Configuration
@EnableScheduling
@EnableJpaRepositories(basePackageClasses = ClinicRepository.class, transactionManagerRef = "transactionManager")
@ComponentScan(basePackageClasses = {InitService.class})
public class AppConfig {

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
