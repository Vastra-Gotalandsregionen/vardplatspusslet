package se.vgregion.vardplatspusslet.repository.extension;

import se.vgregion.vardplatspusslet.domain.jpa.PatientLeave;
import se.vgregion.vardplatspusslet.domain.jpa.Unit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class PatientLeaveRepositoryImpl implements PatientLeaveExtendedRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PatientLeave> findPatientLeaves(Unit unit, Date actualFromDate, Date actualToDate) {

        TypedQuery<PatientLeave> query = entityManager.createQuery(
                "select pl from PatientLeave pl" +
                        " where pl.unit = :unit and pl.actualDate >= :fromDate and pl.actualDate <= :toDate",
                PatientLeave.class);

        query.setParameter("unit", unit);
        query.setParameter("fromDate", actualFromDate);
        query.setParameter("toDate", actualToDate);

        List<PatientLeave> resultList = query.getResultList();

        for (PatientLeave patientLeave : resultList) {
            patientLeave.setUnit(unit);
        }

        return resultList;
    }
}
