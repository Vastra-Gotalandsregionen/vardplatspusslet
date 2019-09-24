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
import java.util.HashMap;
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

        // Query for patients who aren't in any bed nor any unit
        List<Long> orphanPatients = jdbcTemplate.queryForList("select p.id from patient p where p.id not in " +
                "(select distinct patient_id from bed where patient_id is not null) and p.id not in " +
                "(select patients_id from unit_patient)", Long.class);

        if (orphanPatients.size() > 0) {

            // Remove patientCareBurdenChoiceJoins
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("ids", orphanPatients);

            int update1 = namedParameterJdbcTemplate.update(
                    "delete from patient_careburdenchoice pc where pc.patient_id in (:ids)",
                    parameters
            );

            // Remove patientExaminations
            namedParameterJdbcTemplate.update(
                    "delete from patient_patientexamination ppe where ppe.patient_id in (:ids)",
                    parameters
            );
            namedParameterJdbcTemplate.update(
                    "delete from patientexamination pe where pe.id not in " +
                            "(select distinct patientexaminations_id from patient_patientexamination)",
                    new HashMap<>()
            );

            // Remove patientEvents
            namedParameterJdbcTemplate.update(
                    "delete from patient_patientevent ppe where ppe.patient_id in (:ids)",
                    parameters
            );
            namedParameterJdbcTemplate.update(
                    "delete from patientevent pe where pe.id not in " +
                            "(select distinct patientevents_id from patient_patientevent)",
                    new HashMap<>()
            );

            // Remove patients
            int update = namedParameterJdbcTemplate.update("delete from patient where id in (:ids)", parameters);

            LOGGER.info("Deleted " + update + " orphan patient entries and " + update1 + " patient_careburdenchoices");
        }

        // Select all careburdenchoices which have no connection to patient.
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
