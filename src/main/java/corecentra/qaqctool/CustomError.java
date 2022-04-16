package corecentra.qaqctool;

import corecentra.qaqctool.fields.ProjectFieldRepository;
import corecentra.qaqctool.fields.ProjectFields;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;

@AllArgsConstructor
public class CustomError {
    int columnNumber;
    String columnName;
    int rowNumber;
    String errorMessage;

    @Autowired
    private ProjectFieldRepository pfrepo;

    public CustomError(String columnName, int rowNumber, String errorMessage) {
        this.columnName = columnName;
        this.rowNumber = rowNumber;
        this.errorMessage = errorMessage;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(int columnId) {
        columnId = this.columnNumber;
        ProjectFields pf= pfrepo.findById(columnId);
        this.columnName = pf.getFieldname();
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
