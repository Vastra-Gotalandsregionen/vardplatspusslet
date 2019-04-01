package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.*;
import java.util.Comparator;

@Entity
@Table(name = "careBurdenValue")

public class CareBurdenValue implements Comparable<CareBurdenValue> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column
    private String name;

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

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public int compareTo(CareBurdenValue o) {
        return Comparator.nullsLast(Comparator.comparing(o2 -> ((CareBurdenValue) o2).name)).compare(this, o);
    }
}
