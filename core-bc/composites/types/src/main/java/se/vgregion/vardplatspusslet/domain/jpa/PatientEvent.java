package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "patientevent")
public class PatientEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String event;

    @Column
    private Date eventDate;

   /* @Column
    private String  eventTime;

    @Column
    private String eventInfo;*/

    public PatientEvent() {
    }

    public PatientEvent(Long id, String event, Date eventDate/*, String eventTime, String eventInfo*/) {
        this.id = id;
        this.event = event;
        this.eventDate = eventDate;
       /* this.eventTime = eventTime;
        this.eventInfo = eventInfo;*/
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    /*public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
    }*/
}

