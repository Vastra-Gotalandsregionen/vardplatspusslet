package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.*;
import java.util.List;

import static java.util.Comparator.*;

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
    @Column
    private Boolean showCareBurden;

    @OneToMany(mappedBy = "ssk", fetch = FetchType.LAZY)
    private List<Bed> beds;

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
        return comparing(Ssk::getLabel, nullsLast(naturalOrder())).compare(this, o);
    }

    public Boolean getShowCareBurden() {
        return showCareBurden;
    }

    public void setShowCareBurden(Boolean showCareBurden) {
        this.showCareBurden = showCareBurden;
    }

    public List<Bed> getBeds() {
        return beds;
    }

    public void setBeds(List<Bed> beds) {
        this.beds = beds;
    }

    @PreRemove
    public void decoupleBeds() {
        for (Bed bed : getBeds()) {
            bed.setSsk(null);
        }
    }
}
