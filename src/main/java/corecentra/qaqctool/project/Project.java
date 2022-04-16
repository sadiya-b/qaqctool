package corecentra.qaqctool.project;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Project {
    int nodeID;
    String organizationName;
    String theme;
    String program;
    String projectID;
    String projectName;
    int reportingYear;
    double beneficiariesToDate;
    int projectPercentComplete;
    String projectStartDate;
    String projectEndDate;
    String originalEndDate;
    String budgetApproved;

    public Project(int nodeID, String organizationName, String theme, String program, String projectID,String projectName) {
        this.nodeID = nodeID;
        this.organizationName = organizationName;
        this.theme = theme;
        this.program = program;
        this.projectID = projectID;
        this.projectName = projectName;
    }
}
