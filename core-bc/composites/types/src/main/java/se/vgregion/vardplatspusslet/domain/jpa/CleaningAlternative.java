package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import static java.util.Comparator.*;


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
        return comparing(CleaningAlternative::getDescription, nullsLast(naturalOrder())).compare(this, o);
    }
}
