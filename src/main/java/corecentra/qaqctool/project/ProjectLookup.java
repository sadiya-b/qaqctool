package corecentra.qaqctool.project;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelSheet;
import lombok.AllArgsConstructor;

@ExcelSheet("Project")
@AllArgsConstructor
public class ProjectLookup {
    @ExcelCell(0)
    int nodeID;

    @ExcelCell(1)
    String organizationName;

    @ExcelCell(2)
    String theme;

    @ExcelCell(3)
    String program;

    @ExcelCell(4)
    String projectID;

    @ExcelCell(5)
    String projectName;

    public ProjectLookup(){}

    //region - getters setters
    public int getNodeID() {
        return nodeID;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    //endregion

}
