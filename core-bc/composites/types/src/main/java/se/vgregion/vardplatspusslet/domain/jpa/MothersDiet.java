package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "mothersdiet")
public class MothersDiet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MothersDiet diet = (MothersDiet) o;
        return Objects.equals(id, diet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
