package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import static java.util.Comparator.*;

@Entity
@Table(name = "careBurdenValue")

public class CareBurdenValue implements Comparable<CareBurdenValue> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column
    private String name;

    @Column
    private Boolean countedIn;

    @Enumerated
    private Color color;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String burdenValue) {
        this.name = burdenValue;
    }

    public Color getColor() {
        return color;
    }

    public Boolean getCountedIn() {
        return countedIn;
    }

    public void setCountedIn(Boolean countedIn) {
        this.countedIn = countedIn;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public int compareTo(CareBurdenValue o) {
        return comparing(CareBurdenValue::getName, nullsLast(naturalOrder())).compare(this, o);
    }
}
