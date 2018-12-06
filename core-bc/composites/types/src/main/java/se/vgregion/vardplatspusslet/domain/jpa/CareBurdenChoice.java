package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.*;

@Entity
@Table(name= "careBurdenChoice")
public class CareBurdenChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    private CareBurdenKategori careBurdenKategori;

    @ManyToOne(fetch = FetchType.EAGER)
    private CareBurdenValue careBurdenValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CareBurdenKategori getCareBurdenKategori() {
        return careBurdenKategori;
    }

    public void setCareBurdenKategori(CareBurdenKategori careBurdenKategori) {
        this.careBurdenKategori = careBurdenKategori;
    }

    public CareBurdenValue getCareBurdenValue() {
        return careBurdenValue;
    }

    public void setCareBurdenValue(CareBurdenValue careBurdenValue) {
        this.careBurdenValue = careBurdenValue;
    }
}
