package se.vgregion.vardplatspusslet.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Patrik Björk
 */
@Entity
@Table(name = "_user")
public class User {

    // Normally VGR ID
    @Id
    private String id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String mail;

    @Column
    private String displayName;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private byte[] thumbnailPhoto;

    @Column
    private Boolean inactivated = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setThumbnailPhoto(byte[] thumbnailPhoto) {
        this.thumbnailPhoto = thumbnailPhoto;
    }

    public byte[] getThumbnailPhoto() {
        return thumbnailPhoto;
    }

    public Boolean getInactivated() {
        return inactivated;
    }

    public void setInactivated(Boolean inactivated) {
        this.inactivated = inactivated;
    }
}