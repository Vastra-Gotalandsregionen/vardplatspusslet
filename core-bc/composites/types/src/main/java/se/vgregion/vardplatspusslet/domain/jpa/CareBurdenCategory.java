package se.vgregion.vardplatspusslet.domain.jpa;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Comparator;

@Entity
@Table(name= "careBurdenCategory")
public class CareBurdenCategory implements Comparable<CareBurdenCategory> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String kategori) {
        this.name = kategori;
    }

    @Override
    public int compareTo(CareBurdenCategory o) {
        return Comparator.nullsLast(Comparator.comparing(o2 -> ((CareBurdenCategory) o2).name)).compare(this, o);
    }
}
