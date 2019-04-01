package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.*;
import java.util.Comparator;


@Entity
@Table(name = "cleaningalternative")
public class CleaningAlternative implements Comparable<CleaningAlternative> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String description;

    public CleaningAlternative() {
    }

    public CleaningAlternative(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(CleaningAlternative o) {
        return Comparator.nullsLast(Comparator.comparing(o2 -> ((CleaningAlternative) o2).description)).compare(this, o);
    }
}
