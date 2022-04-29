package corecentra.qaqctool.result;

import com.poiji.bind.Poiji;
import corecentra.qaqctool.CustomError;
import corecentra.qaqctool.fields.ProjectFields;
import corecentra.qaqctool.fields.ResultFieldRepository;
import corecentra.qaqctool.fields.ResultFields;
import corecentra.qaqctool.fileupload.FileUploadController;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@AllArgsConstructor
@Controller
public class ResultController {

    private String fileLocation = "src/main/java/corecentra/qaqctool/storage/";

    @Autowired
    FileUploadController fileUploadController = new FileUploadController();

    @Autowired
    ResultFieldRepository resultFieldRepository;

    public ResultController() {}

    public ArrayList<ResultLookup> getLookupData(){
        String filePath =
                fileLocation + "lookupfile_" + fileUploadController.getCurrentUser() + ".xlsx";

        ArrayList<ResultLookup> lookupData =
                (ArrayList<ResultLookup>) Poiji.fromExcel(new File(filePath), ResultLookup.class);

        return lookupData;
    }

    public boolean contains(ArrayList<ResultLookup> list , ResultLookup b){
        return list.stream().anyMatch(x->x.getProjectID().equals(b.projectID)
               && x.getProjectName().equals(b.projectName)
               && x.getResultID().equals(b.resultID)
               && x.getResult().equals(b.result));
    }

     private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();

            case BOOLEAN:
                return cell.getBooleanCellValue();

            case NUMERIC:
                if(DateUtil.isCellDateFormatted(cell)){
                    DataFormatter df = new DataFormatter();
                    String abc = df.formatCellValue(cell);
                    return abc;
                }
                return cell.getNumericCellValue();
        }

        return "";
    }


    private boolean checkRegex(String check, String value){
        boolean isMatch = true;
        switch (check){
            case "digit":
                isMatch = value.matches("[0-9]+");
                break;
            case "letters":
                break;
            case "date":
                String strPattern = "^\\d{1,2}/\\d{1,2}/\\d{2,4}$";
                isMatch = value.matches(strPattern);
                break;
            case "decimal":
                isMatch = false;
                if(value.contains("."))
                {
                    String[] splitNum = value.split("\\.");
                    if(splitNum[1].length()<=2){
                        if(splitNum[0].replace(",","").matches("[0-9]+")){
                            isMatch = true;
                        }
                    }
                }
                else if(value.contains(",")){
                    if(value.replace(",","").matches("[0-9]+")){
                        isMatch = true;
                    }
                }
                else{
                    isMatch = value.matches("[0-9]+");
                }
                break;
            default:
                break;

        }
        return isMatch;
    }

    @GetMapping("/result/results")
    public String evaluateResult(Model model) throws Exception {
        String filename =  "result_" + fileUploadController.getCurrentUser() + ".xlsx";

        List<ResultFields> resulttFields = resultFieldRepository.findAll();

        //getting all the column indices present in the db
        List<Integer> columnIndex = new ArrayList<>();
        for (ResultFields p : resulttFields) {
            int id = (int)p.getId();
            columnIndex.add(id);
        }

        List<CustomError> result = new ArrayList<>();

        //check if file exists
        if(!Files.exists(Path.of(fileLocation.concat(filename)))){
            throw new Exception("no such file exists");
        }

        ArrayList<ResultLookup> requiredFieldData =
                (ArrayList<ResultLookup>) Poiji.fromExcel(new File(fileLocation.concat(filename)), ResultLookup.class);

        //region - excel intialization
        FileInputStream inputStream = new FileInputStream(new File(fileLocation.concat(filename)));

        Workbook workbook = new XSSFWorkbook(inputStream);

        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> iterator = sheet.iterator();
        //endregion

        while(iterator.hasNext()){
            Row nextRow = iterator.next();
            int rowNum = nextRow.getRowNum();

            Iterator<Cell> cellIterator = nextRow.cellIterator();

            if(rowNum !=0) {
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int col = nextCell.getColumnIndex();

                    if(columnIndex.contains(col)) {
                        String cellValue = (String)getCellValue(nextCell);
                        String columnName = resultFieldRepository.getById((long)col).getFieldname();
                        String fieldtype = resultFieldRepository.getById((long)col).getFieldtype();
                        long fieldLimit = resultFieldRepository.getById((long)col).getFieldlimit();

                        switch (col) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                                if(cellValue.isEmpty() || cellValue.isBlank()){
                                    result.add(new CustomError(columnName,rowNum,String.format("%s field is blank, %s is a required field.", columnName, columnName)));
                                }
                                else {
                                    if (cellValue.length()>fieldLimit) {
                                        result.add(new CustomError(columnName,rowNum,String.format("%s field can not exceed %s chars.", columnName, fieldLimit)));
                                    }
                                }
                                break;
                            case 4:
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(cellValue.length()>fieldLimit){
                                        result.add(new CustomError(columnName,rowNum,String.format("%s field can not exceed %s chars.", columnName, fieldLimit)));
                                    }
                                }
                                break;
                            case 5:
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(!checkRegex("decimal",cellValue) || cellValue.length()>fieldLimit){
                                        result.add(new CustomError(columnName,rowNum,String.format("%s must be a decimal format value.", columnName)));
                                    }
                                }
                                break;
                            case 6:
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(!checkRegex(fieldtype,cellValue) || cellValue.length()>4){
                                        result.add(new CustomError(columnName,rowNum,String.format("%s must be a digit value.", columnName)));
                                    }
                                }
                                break;
                            case 7:
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(!checkRegex(fieldtype,cellValue) || cellValue.length()>fieldLimit){
                                        result.add(new CustomError(columnName,rowNum,String.format("%s has incorrect date format.", columnName)));
                                    }
                                }
                                break;
                        }
                    }
                }
            }

        }

        if(result.isEmpty()){
            ArrayList<ResultLookup> lookupData = this.getLookupData();
            int index = 0;
            for(ResultLookup i: requiredFieldData){
                if(!contains(lookupData,i)){
                    result.add(new CustomError("",index,"Please check lookup data"));
                }
                index++;
            }
        }

        model.addAttribute("error",result);

        return "resultsResult";
    }

}
