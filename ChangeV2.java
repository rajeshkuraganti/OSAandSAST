package com.cx.automation.installation.old.tfsManagment.dto;

import java.util.List;

/**
 * Created by: iland.
 * Date: 2/20/2017.
 */
public class ChangeV2 {

    private Integer id = null;
    private String message = null;
    private String type = null;
    private IdentityV2 auther = null;
    private List<String> changedItems = null;
    private List<WorkItemV2> assoWorkItems = null;

    public ChangeV2() {
    }

    public ChangeV2(Integer id, String message, String type, IdentityV2 auther,
                    List<String> items, List<WorkItemV2> assoWorkItems) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.auther = auther;
        this.changedItems = items;
        this.assoWorkItems = assoWorkItems;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public IdentityV2 getAuther() {
        return auther;
    }

    public void setAuther(IdentityV2 auther) {
        this.auther = auther;
    }

    public List<String> getChangedItems() {
        return changedItems;
    }

    public void setChangedItems(List<String> changedItems) {
        this.changedItems = changedItems;
    }

    public List<WorkItemV2> getAssoWorkItems() {
        return assoWorkItems;
    }

    public void setAssoWorkItems(List<WorkItemV2> assoWorkItems) {
        this.assoWorkItems = assoWorkItems;
    }

    @Override
    public String toString() {
        return "Change{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", auther=" + auther +
                ", changedItems=" + changedItems +
                ", assoWorkItems=" + assoWorkItems +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChangeV2 changeV2 = (ChangeV2) o;

        if (id != null ? !id.equals(changeV2.id) : changeV2.id != null) return false;
        if (message != null ? !message.equals(changeV2.message) : changeV2.message != null) return false;
        if (type != null ? !type.equals(changeV2.type) : changeV2.type != null) return false;
        if (auther != null ? !auther.equals(changeV2.auther) : changeV2.auther != null) return false;
        if (changedItems != null ? !changedItems.equals(changeV2.changedItems) : changeV2.changedItems != null) return false;
        return assoWorkItems != null ? assoWorkItems.equals(changeV2.assoWorkItems) : changeV2.assoWorkItems == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (auther != null ? auther.hashCode() : 0);
        result = 31 * result + (changedItems != null ? changedItems.hashCode() : 0);
        result = 31 * result + (assoWorkItems != null ? assoWorkItems.hashCode() : 0);
        return result;
    }

}
