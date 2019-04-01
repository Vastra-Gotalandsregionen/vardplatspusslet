package se.vgregion.vardplatspusslet.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Service
@Transactional
public class PatientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientService.class);

    @Autowired
    private DataSource dataSource;

    public void removeOrphanPatientsWithCareBurdenChoices() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        List<Long> orphanPatients = jdbcTemplate.queryForList("select p.id from patient p where p.id not in " +
                "(select distinct patient_id from bed where patient_id is not null)", Long.class);

        if (orphanPatients.size() > 0) {

            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("ids", orphanPatients);

            int update1 = namedParameterJdbcTemplate.update("delete from patient_careburdenchoice pc" +
                    " where pc.patient_id in (:ids)", parameters);

            int update = namedParameterJdbcTemplate.update("delete from patient where id in (:ids)", parameters);

            LOGGER.info("Deleted " + update + " orphan patient entries and " + update1 + " patient_careburdenchoices");
        }

        List<Long> orphanCareBurdenChoices = jdbcTemplate.queryForList("select id from careburdenchoice cbc" +
                " where cbc.id not in " +
                "(select distinct careburdenchoices_id from patient_careburdenchoice)",
                Long.class);

        if (orphanCareBurdenChoices.size() > 0) {

            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("ids", orphanCareBurdenChoices);

            int update = namedParameterJdbcTemplate.update("delete from careburdenchoice where id in (:ids)", parameters);

            LOGGER.info("Deleted " + update + " orphan careburdenchoice entries.");
        }
    }
}
