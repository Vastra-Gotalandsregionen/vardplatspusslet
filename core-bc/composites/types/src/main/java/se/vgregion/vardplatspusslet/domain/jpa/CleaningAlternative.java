package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.*;

import static java.util.Comparator.*;


@Entity
@Table(name = "cleaningalternative")
public class CleaningAlternative implements Comparable<CleaningAlternative> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String description;

    @Enumerated
    private Color color;

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

    public Color getColor() { return color; }

    public void setColor(Color color) { this.color = color; }

    @Override
    public int compareTo(CleaningAlternative o) {
        return comparing(CleaningAlternative::getDescription, nullsLast(naturalOrder())).compare(this, o);
    }
}
