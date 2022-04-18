package corecentra.qaqctool.impact;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelSheet;
import lombok.AllArgsConstructor;

@ExcelSheet("Impacts")
@AllArgsConstructor
public class ImpactLookup {
    @ExcelCell(0)
    String projectID;

    @ExcelCell(1)
    String projectName;

    @ExcelCell(2)
    String impactID;

    @ExcelCell(3)
    String impact;

    public ImpactLookup(){}

    //region - getter setter
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

    public String getImpactID() {
        return impactID;
    }

    public void setImpactID(String impactID) {
        this.impactID = impactID;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }
    //endregion
}
