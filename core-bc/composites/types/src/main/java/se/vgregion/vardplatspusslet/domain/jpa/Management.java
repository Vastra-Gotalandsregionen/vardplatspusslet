package se.vgregion.vardplatspusslet.domain.jpa;


import javax.persistence.*;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "management")
public class Management {
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

}
