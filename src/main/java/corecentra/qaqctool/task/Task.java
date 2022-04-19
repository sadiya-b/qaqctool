package corecentra.qaqctool.task;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelSheet;
import lombok.AllArgsConstructor;

@ExcelSheet("Tasks")
@AllArgsConstructor
public class Task {
    @ExcelCell(0)
    String projectID;

    @ExcelCell(1)
    String projectName;

    @ExcelCell(2)
    String taskID;

    @ExcelCell(3)
    String task;

    @ExcelCell(4)
    int percentComplete;

    @ExcelCell(5)
    String owner;

    @ExcelCell(6)
    String startDate;

    @ExcelCell(7)
    String endDate;

    public Task(){}

    public Task(String projectID, String projectName, String taskID, String task) {
        this.projectID = projectID;
        this.projectName = projectName;
        this.taskID = taskID;
        this.task = task;
    }
}
