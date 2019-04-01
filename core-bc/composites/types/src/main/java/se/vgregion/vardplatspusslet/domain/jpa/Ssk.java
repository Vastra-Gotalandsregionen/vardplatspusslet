package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Comparator;

/**
 * SSK is synomymous with "vårdlag".
 *
 * @author Patrik Björk
 */
@Entity
@Table(name = "ssk")
public class Ssk implements Comparable<Ssk> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String label;

    @Enumerated
    private Color color;

    public Ssk() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public int compareTo(Ssk o) {
        return Comparator.nullsLast(Comparator.comparing(o2 -> ((Ssk) o2).label)).compare(this, o);
    }
}
