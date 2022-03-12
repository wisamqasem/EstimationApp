package com.jdeco.estimationapp.objects;

public class ProjectType {

    String projectTypeName;
    String projectTypeId;

    public ProjectType(String projectTypeName, String projectTypeId) {
        this.projectTypeName = projectTypeName;
        this.projectTypeId = projectTypeId;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }



    public String getProjectTypeId() {
        return projectTypeId;
    }



    @Override
    public String toString() {
        return projectTypeName;
    }
}
