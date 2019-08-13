package se.vgregion.vardplatspusslet.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.EnableScheduling;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.service.InitService;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Patrik Björk
 */
@Configuration
@EnableScheduling
@EnableJpaRepositories(basePackageClasses = ClinicRepository.class, transactionManagerRef = "transactionManager")
@ComponentScan(basePackageClasses = {InitService.class})
public class AppConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        initOrderColumnInUnit_CareBurdenCategory(jdbcTemplate);

        return jdbcTemplate;
    }

    // This method should be temporary. As these changes are reflected in all environments it is not necessary anymore.
    private void initOrderColumnInUnit_CareBurdenCategory(JdbcTemplate jdbcTemplate) {
        try {
            jdbcTemplate.execute("ALTER TABLE public.unit_careburdencategory" +
                    "    ADD COLUMN order_ integer ");

            List<String[]> query = jdbcTemplate.query("select * from unit_careburdencategory", new RowMapper<String[]>() {
                @Override
                public String[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                    String unitId = rs.getString("unit_id");
                    String careburdencategoriesId = rs.getString("careburdencategories_id");
                    String order = rs.getString("order_");
                    return new String[]{unitId, careburdencategoriesId, order};
                }
            });

            Map<String, List<String[]>> collect = query.stream()
                    .collect(Collectors.groupingBy(strings -> strings[0]));

            int n = 0;
            for (List<String[]> group : collect.values()) {
                for (String[] row : group) {
                    String sql = "update unit_careburdencategory set order_ = " + (n++) + " where careburdencategories_id = " + row[1] + " and unit_id='" + row[0] + "'";
                    jdbcTemplate.execute(sql);
                }
                n = 0;
            }

        } catch (DataAccessException e) {
            LOGGER.info("skipping init of order column for unit_careburdencategory...");
        }
    }
}
