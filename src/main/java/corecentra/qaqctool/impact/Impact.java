package corecentra.qaqctool.impact;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelSheet;
import lombok.AllArgsConstructor;

@ExcelSheet("Impacts")
@AllArgsConstructor
public class Impact {
    @ExcelCell(0)
    String projectID;

    @ExcelCell(1)
    String projectName;

    @ExcelCell(2)
    String impactID;

    @ExcelCell(3)
    String impact;

    @ExcelCell(4)
    double impactYield;

    @ExcelCell(5)
    double impactPercent;

    @ExcelCell(6)
    int reportingYear;

    @ExcelCell(7)
    String dateCreated;

    @ExcelCell(8)
    String region;

    public Impact(){}

    public Impact(String projectID, String projectName, String impactID, String impact) {
        this.projectID = projectID;
        this.projectName = projectName;
        this.impactID = impactID;
        this.impact = impact;
    }
}
