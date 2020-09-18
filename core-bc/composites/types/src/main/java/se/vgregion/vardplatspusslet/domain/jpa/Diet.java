package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="diet")
public class Diet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

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
        Diet diet = (Diet) o;
        return Objects.equals(id, diet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
