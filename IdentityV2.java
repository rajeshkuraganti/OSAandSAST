package com.cx.automation.installation.old.tfsManagment.dto;

/**
 * Created by: iland.
 * Date: 2/20/2017.
 */
public class IdentityV2 {

    private String id = null;
    private String displayName = null;
    private String uniqueName = null;
    private String email = null;

    public IdentityV2() {
    }

    public IdentityV2(String id, String displayName, String uniqueName, String email) {
        this.id = id;
        this.displayName = displayName;
        this.uniqueName = uniqueName;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Identity{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", uniqueName='" + uniqueName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdentityV2 identity = (IdentityV2) o;

        if (id != null ? !id.equals(identity.id) : identity.id != null) return false;
        if (displayName != null ? !displayName.equals(identity.displayName) : identity.displayName != null) return false;
        if (uniqueName != null ? !uniqueName.equals(identity.uniqueName) : identity.uniqueName != null) return false;
        return email != null ? email.equals(identity.email) : identity.email == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (uniqueName != null ? uniqueName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

}
