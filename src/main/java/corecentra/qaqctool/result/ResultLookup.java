package corecentra.qaqctool.result;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelSheet;
import lombok.AllArgsConstructor;

@ExcelSheet("Results")
@AllArgsConstructor
public class ResultLookup {
    @ExcelCell(0)
    String projectID;

    @ExcelCell(1)
    String projectName;

    @ExcelCell(2)
    String resultID;

    @ExcelCell(3)
    String result;

    public ResultLookup(){}

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

    public String getResultID() {
        return resultID;
    }

    public void setResultID(String resultID) {
        this.resultID = resultID;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    //endregion
}
