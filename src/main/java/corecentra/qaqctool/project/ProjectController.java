package corecentra.qaqctool.project;

import com.poiji.bind.Poiji;
import corecentra.qaqctool.CustomError;
import corecentra.qaqctool.fields.ProjectFieldRepository;
import corecentra.qaqctool.fields.ProjectFields;
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
import java.util.*;

@AllArgsConstructor
@Controller
public class ProjectController {

    private String fileLocation = "src/main/java/corecentra/qaqctool/storage/";

    @Autowired
    FileUploadController fileUploadController = new FileUploadController();

    @Autowired
    ProjectFieldRepository projectFieldRepository;

    public ProjectController() {}

    public ArrayList<ProjectLookup> getLookupData(){
        String filePath =
                fileLocation + "lookupfile_" + fileUploadController.getCurrentUser() + ".xlsx";

        ArrayList<ProjectLookup> lookupData =
                (ArrayList<ProjectLookup>) Poiji.fromExcel(new File(filePath), ProjectLookup.class);

        return lookupData;
    }

    public boolean contains(ArrayList<ProjectLookup> list , ProjectLookup b){
        return list.stream().anyMatch(x->x.getNodeID() == b.nodeID
               && x.getOrganizationName().equals(b.organizationName)
               && x.getTheme().equals(b.theme)
               && x.getProgram().equals(b.program)
               && x.getProjectID().equals(b.projectID)
               && x.getProjectName().equals(b.projectName));
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
                else{
                    cell.setCellType(CellType.STRING);
                    return cell.getStringCellValue();
                }
                //return cell.getNumericCellValue();
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

    @GetMapping("/result/project")
    public String evaluateProject(Model model) throws Exception {
        String filename =  "project_" + fileUploadController.getCurrentUser() + ".xlsx";

        List<ProjectFields> projectFields = projectFieldRepository.findAll();

        //getting all the column indices present in the db
        List<Integer> columnIndex = new ArrayList<>();
        for (ProjectFields p : projectFields) {
            int id = (int)p.getId();
            columnIndex.add(id);
        }

        List<CustomError> result = new ArrayList<>();

        //check if file exists
        if(!Files.exists(Path.of(fileLocation.concat(filename)))){
            throw new Exception("no such file exists");
        }

        ArrayList<ProjectLookup> requiredFieldData =
                (ArrayList<ProjectLookup>) Poiji.fromExcel(new File(fileLocation.concat(filename)), ProjectLookup.class);

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
                        String columnName = projectFieldRepository.getById((long)col).getFieldname();
                        String fieldtype = projectFieldRepository.getById((long)col).getFieldtype();
                        long fieldLimit = projectFieldRepository.getById((long)col).getFieldlimit();

                        switch (col) {
                            case 0:
                                if(cellValue.isEmpty() || cellValue.isBlank()){
                                    result.add(new CustomError(columnName,rowNum,String.format("%s field is blank, %s is a required field.", columnName, columnName)));
                                }
                                else {
                                    if (!checkRegex(fieldtype, nextCell.getStringCellValue())) {
                                        result.add(new CustomError(columnName,rowNum,String.format("%s must be a digit value.", columnName)));
                                    }
                                }
                                break;
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                                if(cellValue.isEmpty() || cellValue.isBlank()) {
                                    result.add(new CustomError(columnName,rowNum,String.format("%s field is blank, %s is a required field.", columnName, columnName)));
                                }
                                else{
                                    if(cellValue.length()>fieldLimit){
                                        result.add(new CustomError(columnName,rowNum,String.format("%s field can not exceed %s chars.", columnName, fieldLimit)));
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
                            case 27:
                                //this should be a decimal, we need to first remove any additional characters, convert to decimal.
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(!checkRegex("decimal",cellValue) || cellValue.length()>fieldLimit){
                                        result.add(new CustomError(columnName,rowNum,String.format("%s must be a decimal format value.", columnName)));
                                    }
                                }
                                break;
                            case 18:
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(!checkRegex(fieldtype,cellValue) && ((Integer.parseInt(cellValue)>=1 && Integer.parseInt(cellValue)<=100))){
                                        result.add(new CustomError(columnName,rowNum,String.format("%s must be a numerical value.", columnName)));
                                    }
                                }
                                break;
                            case 19:
                            case 20:
                            case 21:
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
            ArrayList<ProjectLookup> lookupData = this.getLookupData();
            int index = 0;
            for(ProjectLookup i: requiredFieldData){
                if(!contains(lookupData,i)){
                    result.add(new CustomError("",index+1,"Please check lookup data"));
                }
                index++;
            }
        }

        model.addAttribute("error",result);

        return "projectResult";
    }

}
