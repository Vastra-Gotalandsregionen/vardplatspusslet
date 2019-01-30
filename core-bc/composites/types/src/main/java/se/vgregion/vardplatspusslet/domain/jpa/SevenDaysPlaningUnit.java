package se.vgregion.vardplatspusslet.domain.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sevenDaysPlaningUnit")
public class SevenDaysPlaningUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    private UnitPlannedIn fromUnit;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Unit unit;

    @Column
    private Integer quantity;

    @Column
    private String comment;

    public SevenDaysPlaningUnit() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UnitPlannedIn getFromUnit() {
        return fromUnit;
    }

    public void setFromUnit(UnitPlannedIn fromUnit) {
        this.fromUnit = fromUnit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SevenDaysPlaningUnit)) return false;

        SevenDaysPlaningUnit that = (SevenDaysPlaningUnit) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
