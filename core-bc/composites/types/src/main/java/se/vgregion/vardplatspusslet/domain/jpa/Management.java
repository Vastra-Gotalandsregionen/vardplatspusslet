package se.vgregion.vardplatspusslet.domain.jpa;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static java.util.Comparator.*;

@Entity
@Table(name = "management")
public class Management implements Comparable<Management> {
    @Id
    private String id;

    @Column
    private String name;

    public Management() {
    }

    public Management(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Management o) {
        return comparing(Management::getName, nullsLast(naturalOrder())).compare(this, o);
    }
}
