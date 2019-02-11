package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.*;

@Entity
@Table(name = "careBurdenValue")

public class CareBurdenValue {

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
}
