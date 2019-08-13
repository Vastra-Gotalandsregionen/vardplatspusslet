package se.vgregion.vardplatspusslet.domain.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.Date;

import static java.util.Comparator.*;

@Entity
@Table(name = "message")
public class Message implements Comparable<Message> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String heading;

    @Column(length = 8192)
    private String text;

    @Column
    @Temporal(TemporalType.DATE)
    private Date date;

    @Enumerated
    private DayOfWeek dayOfWeek;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Unit unit;

    @Column
    private Boolean pinned;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @JsonIgnore
    public Unit getUnit() {
        return unit;
    }

    @JsonProperty
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Boolean getPinned() {
        return pinned;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }

    @Override
    public int compareTo(Message o) {
        if (this == o) {
            return 0;
        }

        Comparator<Message> pinned = comparing(m1 -> m1.pinned, nullsLast(naturalOrder()));
        Comparator<Message> dayOfWeek = comparing(m1 -> m1.dayOfWeek, nullsFirst(naturalOrder()));
        Comparator<Message> date = comparing(m1 -> m1.date, nullsFirst(naturalOrder()));
        Comparator<Message> heading = comparing(m1 -> m1.heading, nullsFirst(naturalOrder()));
        Comparator<Message> id = comparing(m1 -> m1.id, nullsFirst(naturalOrder()));

        return dayOfWeek
                .thenComparing(pinned)
                .thenComparing(date)
                .thenComparing(heading)
                .thenComparing(id)
                .compare(this, o);
    }
}
