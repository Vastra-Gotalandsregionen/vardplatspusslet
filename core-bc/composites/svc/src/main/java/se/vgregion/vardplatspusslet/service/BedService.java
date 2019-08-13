package se.vgregion.vardplatspusslet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vgregion.vardplatspusslet.domain.jpa.*;
import se.vgregion.vardplatspusslet.repository.BedRepository;
import se.vgregion.vardplatspusslet.repository.ClinicRepository;
import se.vgregion.vardplatspusslet.repository.PatientLeaveRepository;
import se.vgregion.vardplatspusslet.repository.PatientRepository;
import se.vgregion.vardplatspusslet.repository.UnitRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BedService {

    @Autowired
    private BedRepository bedRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientLeaveRepository patientLeaveRepository;

    public void deleteBed(String clinicId, String unitId, Long bedId) {
        Clinic clinicRef = clinicRepository.getOne(clinicId);

        Unit unit = unitRepository.findUnitByIdIsLikeAndClinicIsLike(unitId, clinicRef);

        List<Bed> beds = unit.getBeds();

        beds.remove(bedRepository.getOne(bedId));

        Bed[] bedsArrays = beds.toArray(new Bed[]{});

        // Need to do this way or otherwise Hibernate will fail with a unique constraint exception.
        unit.getBeds().clear();
        unitRepository.flush();
        unit.getBeds().addAll(Arrays.asList(bedsArrays));
        unitRepository.flush();

        unitRepository.save(unit);

        bedRepository.delete(bedId);
    }

    public void save(String clinicId, String unitId, Bed bed) {
        if (bed.getId() == null) {
            // New. Not assigned to unit yet.
            Clinic clinicRef = clinicRepository.getOne(clinicId);
            Unit unit = unitRepository.findUnitByIdIsLikeAndClinicIsLike(unitId, clinicRef);
            unit.getBeds().add(bed);

            bedRepository.save(bed);
            unitRepository.save(unit);
        } else {
            Patient patient = bed.getPatient();
            Unit unit = unitRepository.findOne(unitId);
            if(bed.getPatientWaits() == null || !bed.getPatientWaits())
                bed.setServingClinic(null);
            if (patient != null) {
                if (bed.getBedStatus() == null || bed.getBedStatus() != BedStatus.OCCUPIED) {
                    // If there is a patient but it is set as unoccupied, the patient should be deleted.
                    bed.setPatient(null);
                    patientRepository.delete(patient);
                } else if (patient.getLeaveStatus() != null) {
                    // Put on leave means removing from bed and attaching to unit instead.
                    bed.setPatient(null);
                    bed.setBedStatus(BedStatus.VACANT);
                    bedRepository.save(bed);

                    unit.getPatients().add(patient);
                    unitRepository.save(unit);
                } else {
                    // Put back from leave means attaching to bed (they already come attached) and removing from unit.
                    unit.getPatients().remove(patient);
                    unitRepository.save(unit);
                }
            }
            bedRepository.save(bed);
        }
    }

    public void patientHasLeft(Bed bed) {
        Patient patient = bed.getPatient();

        bed.setPatient(null);
        bed.setBedStatus(BedStatus.VACANT);

        if (Boolean.TRUE.equals(bed.getUnit().getResetSskOnHasLeft())) {
            bed.setSsk(null);
        }

        bedRepository.save(bed);

        patientRepository.delete(patient);

        PatientLeave patientLeave = new PatientLeave(patient.getPlannedLeaveDate(), new Date(), bed.getUnit());
        patientLeaveRepository.save(patientLeave);
    }
}
