package se.vgregion.vardplatspusslet.domain.json;

import se.vgregion.vardplatspusslet.domain.jpa.Role;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Patrik Björk
 */
public class UserSaveRequest {

    private String id;

    private String name;

    private Role role;

    private Set<String> unitIds = new LinkedHashSet<>();

//    @Column
//    private Boolean inactivated = false;

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<String> getUnitIds() {
        return unitIds;
    }

    public void setUnitIds(Set<String> unitIds) {
        this.unitIds = unitIds;
    }

    /*public Boolean getInactivated() {
        return inactivated;
    }

    public void setInactivated(Boolean inactivated) {
        this.inactivated = inactivated;
    }*/
}