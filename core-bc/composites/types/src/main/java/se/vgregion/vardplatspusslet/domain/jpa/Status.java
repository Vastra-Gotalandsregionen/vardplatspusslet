package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "status")
public class Status {

    @Id
    private String name;
}
