package com.cx.automation.installation.old.tfsManagment.dto;

/**
 * Created by: iland.
 * Date: 3/2/2017.
 */
public class WorkItemV2 {

    private Integer id = null;
    private String title = null;
    private String type = null;
    private String state = null;
    private String assignedTo = null;

    public WorkItemV2() {
    }

    public WorkItemV2(Integer id, String title, String type, String state, String assignedTo) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.state = state;
        this.assignedTo = assignedTo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    @Override
    public String toString() {
        return type + " "
                + "Id: " + id + " "
                + title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkItemV2 that = (WorkItemV2) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        return assignedTo != null ? assignedTo.equals(that.assignedTo) : that.assignedTo == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (assignedTo != null ? assignedTo.hashCode() : 0);
        return result;
    }

}
