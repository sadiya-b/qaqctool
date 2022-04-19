package corecentra.qaqctool.task;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelSheet;
import lombok.AllArgsConstructor;

@ExcelSheet("Tasks")
@AllArgsConstructor
public class TaskLookup {
    @ExcelCell(0)
    String projectID;

    @ExcelCell(1)
    String projectName;

    @ExcelCell(2)
    String taskID;

    @ExcelCell(3)
    String task;

    public TaskLookup(){}

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

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
    //endregion
}
