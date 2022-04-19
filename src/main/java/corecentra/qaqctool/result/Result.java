package corecentra.qaqctool.result;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelSheet;
import lombok.AllArgsConstructor;

@ExcelSheet("Results")
@AllArgsConstructor
public class Result {

    @ExcelCell(0)
    String projectID;
    @ExcelCell(1)
    String projectName;
    @ExcelCell(2)
    String resultID;
    @ExcelCell(3)
    String result;
    @ExcelCell(4)
    String region;
    @ExcelCell(5)
    String resultYield;
    @ExcelCell(6)
    int reportingYear;
    @ExcelCell(7)
    String dateCreated;

    public Result(){}

    public Result(String projectID, String projectName, String resultID, String result) {
        this.projectID = projectID;
        this.projectName = projectName;
        this.resultID = resultID;
        this.result = result;
    }
}
