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

    @Override
    public int compareTo(Message o) {
        if (this == o) {
            return 0;
        }

        Comparator<Message> dayOfWeek = Comparator.comparing(m1 -> m1.dayOfWeek, Comparator.nullsFirst(Comparator.naturalOrder()));
        Comparator<Message> date = Comparator.comparing(m1 -> m1.date, Comparator.nullsFirst(Comparator.naturalOrder()));
        Comparator<Message> heading = Comparator.comparing(m1 -> m1.heading, Comparator.nullsFirst(Comparator.naturalOrder()));
        Comparator<Message> id = Comparator.comparing(m1 -> m1.id, Comparator.nullsFirst(Comparator.naturalOrder()));

        return dayOfWeek
                .thenComparing(date)
                .thenComparing(heading)
                .thenComparing(id)
                .compare(this, o);
    }
}
