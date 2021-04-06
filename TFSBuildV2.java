package com.cx.automation.installation.old.tfsManagment.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by: iland.
 * Date: 2/19/2017.
 */
public class TFSBuildV2 {

    private Integer id = null;
    private Integer sourceVersion = null;
    private String status = null;
    private String result = null;
    private String buildNumber = null;
    private List<ChangeV2> associatedChanges = null;

    public TFSBuildV2() {
    }

    public TFSBuildV2(Integer id, Integer sourceVersion, String status, String result,
                      String buildNumber, List<ChangeV2> associatedChanges, List<WorkItemV2> associatedWorkItems) {
        this.id = id;
        this.sourceVersion = sourceVersion;
        this.status = status;
        this.result = result;
        this.buildNumber = buildNumber;
        this.associatedChanges = associatedChanges;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSourceVersion() {
        return sourceVersion;
    }

    public void setSourceVersion(Integer sourceVersion) {
        this.sourceVersion = sourceVersion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public List<ChangeV2> getAssociatedChanges() {
        return associatedChanges;
    }

    public void setAssociatedChanges(List<ChangeV2> associatedChanges) {
        this.associatedChanges = associatedChanges;
    }

    public Set<String> getAllAssociatedChanges() {
        Set<String> items = new HashSet<>();

        for (ChangeV2 change : associatedChanges) {
            items.addAll(change.getChangedItems());
        }

        return items;
    }

    public Set<WorkItemV2> getAllAssociatedWorkItems() {
        Set<WorkItemV2> items = new HashSet<>();

        for (ChangeV2 change : associatedChanges) {
            items.addAll(change.getAssoWorkItems());
        }

        return items;
    }

    @Override
    public String toString() {
        return "Build{" +
                "id=" + id +
                ", sourceVersion=" + sourceVersion +
                ", status='" + status + '\'' +
                ", result='" + result + '\'' +
                ", buildNumber='" + buildNumber + '\'' +
                ", associatedChanges=" + associatedChanges +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TFSBuildV2 buildV2 = (TFSBuildV2) o;

        if (id != null ? !id.equals(buildV2.id) : buildV2.id != null) return false;
        if (sourceVersion != null ? !sourceVersion.equals(buildV2.sourceVersion) : buildV2.sourceVersion != null) return false;
        if (status != null ? !status.equals(buildV2.status) : buildV2.status != null) return false;
        if (result != null ? !result.equals(buildV2.result) : buildV2.result != null) return false;
        if (buildNumber != null ? !buildNumber.equals(buildV2.buildNumber) : buildV2.buildNumber != null) return false;
        return associatedChanges != null ? associatedChanges.equals(buildV2.associatedChanges) : buildV2.associatedChanges == null;
    }

    @Override
    public int hashCode() {
        int result1 = id != null ? id.hashCode() : 0;
        result1 = 31 * result1 + (sourceVersion != null ? sourceVersion.hashCode() : 0);
        result1 = 31 * result1 + (status != null ? status.hashCode() : 0);
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        result1 = 31 * result1 + (buildNumber != null ? buildNumber.hashCode() : 0);
        result1 = 31 * result1 + (associatedChanges != null ? associatedChanges.hashCode() : 0);
        return result1;
    }

}
