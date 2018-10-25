package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "patientexamination")
public class PatientExamination {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String examination;

    @Column
    private Date examinationDate;

    @Column
    private String  examinationtime;

    public PatientExamination() {
    }

    public PatientExamination(Long id, String examination, Date examinationDate, String examinatioTime) {
        this.id = id;
        this.examination = examination;
        this.examinationDate = examinationDate;
        this.examinationtime = examinatioTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExamination() {
        return examination;
    }

    public void setExamination(String examination) {
        this.examination = examination;
    }

    public Date getExaminationDate() {
        return examinationDate;
    }

    public void setExaminationDate(Date examinationDate) {
        this.examinationDate = examinationDate;
    }
    public String getExaminationtime() {
        return examinationtime;
    }

    public void setExaminationtime(String examinationtime) {
        this.examinationtime = examinationtime;
    }
}
