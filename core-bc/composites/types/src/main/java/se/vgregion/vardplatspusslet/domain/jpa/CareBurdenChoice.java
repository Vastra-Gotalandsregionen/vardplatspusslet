package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.*;

@Entity
@Table(name= "careBurdenChoice")
public class CareBurdenChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    private CareBurdenCategory careBurdenCategory;

    @ManyToOne(fetch = FetchType.EAGER)
    private CareBurdenValue careBurdenValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CareBurdenCategory getCareBurdenCategory() {
        return careBurdenCategory;
    }

    public void setCareBurdenCategory(CareBurdenCategory careBurdenKategori) {
        this.careBurdenCategory = careBurdenKategori;
    }

    public CareBurdenValue getCareBurdenValue() {
        return careBurdenValue;
    }

    public void setCareBurdenValue(CareBurdenValue careBurdenValue) {
        this.careBurdenValue = careBurdenValue;
    }
}
