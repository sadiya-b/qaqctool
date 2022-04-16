package corecentra.qaqctool.project;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelSheet;
import lombok.AllArgsConstructor;

@ExcelSheet("Projects")
@AllArgsConstructor
public class Project {
/*
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
    String budgetApproved;*/

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
    @ExcelCell(6)
    int reportingYear;
    @ExcelCell(7)
    double beneficiariesToDate;
    @ExcelCell(18)
    int projectPercentComplete;
    @ExcelCell(19)
    String projectStartDate;
    @ExcelCell(20)
    String projectEndDate;
    @ExcelCell(21)
    String originalEndDate;
    @ExcelCell(27)
    String budgetApproved;

    public Project(){}
    public Project(int nodeID, String organizationName, String theme, String program, String projectID,String projectName) {
        this.nodeID = nodeID;
        this.organizationName = organizationName;
        this.theme = theme;
        this.program = program;
        this.projectID = projectID;
        this.projectName = projectName;
    }
}
