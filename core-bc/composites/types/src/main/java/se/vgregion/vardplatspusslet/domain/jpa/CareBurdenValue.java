package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.*;

@Entity
@Table(name = "careBurdenValue")

public class CareBurdenValue {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column
    private String burdenValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBurdenValue() {
        return burdenValue;
    }

    public void setBurdenValue(String burdenValue) {
        this.burdenValue = burdenValue;
    }
}
