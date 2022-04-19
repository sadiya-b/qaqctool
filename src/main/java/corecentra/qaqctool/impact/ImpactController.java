package corecentra.qaqctool.impact;

import com.poiji.bind.Poiji;
import corecentra.qaqctool.CustomError;
import corecentra.qaqctool.fields.ImpactFieldRepository;
import corecentra.qaqctool.fields.ImpactFields;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@AllArgsConstructor
@Controller
public class ImpactController {

    private String fileLocation = "src/main/java/corecentra/qaqctool/storage/";

    @Autowired
    FileUploadController fileUploadController = new FileUploadController();

    @Autowired
    ImpactFieldRepository impactFieldRepository;

    public ImpactController() {}

    public ArrayList<ImpactLookup> getLookupData(){
        String filePath =
                fileLocation + "lookupfile_" + fileUploadController.getCurrentUser() + ".xlsx";

        ArrayList<ImpactLookup> lookupData =
                (ArrayList<ImpactLookup>) Poiji.fromExcel(new File(filePath), ImpactLookup.class);

        return lookupData;
    }

    public boolean contains(ArrayList<ImpactLookup> list , ImpactLookup b){
        return list.stream().anyMatch(x->x.getProjectID().equals(b.projectID)
                && x.getProjectName().equals(b.projectName)
                && x.getImpactID().equals(b.impactID)
                && x.getImpact().equals(b.impact));
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

    @GetMapping("/result/impact")
    public String evaluateProject(Model model) throws Exception {
        String filename =  "impact_" + fileUploadController.getCurrentUser() + ".xlsx";

        List<ImpactFields> impactFields = impactFieldRepository.findAll();

        //getting all the column indices present in the db
        List<Integer> columnIndex = new ArrayList<>();
        for (ImpactFields p : impactFields) {
            int id = (int)p.getId();
            columnIndex.add(id);
        }

        List<CustomError> result = new ArrayList<>();

        //check if file exists
        if(!Files.exists(Path.of(fileLocation.concat(filename)))){
            throw new Exception("no such file exists");
        }

        ArrayList<ImpactLookup> requiredFieldData =
                (ArrayList<ImpactLookup>) Poiji.fromExcel(new File(fileLocation.concat(filename)), ImpactLookup.class);

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
                        String columnName = impactFieldRepository.getById((long)col).getFieldname();
                        String fieldtype = impactFieldRepository.getById((long)col).getFieldtype();
                        long fieldLimit = impactFieldRepository.getById((long)col).getFieldlimit();

                        switch (col) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                                if(cellValue.isEmpty() || cellValue.isBlank()){
                                    result.add(new CustomError(columnName,rowNum,"required field blank"));
                                }
                                else {
                                    if (cellValue.length()>fieldLimit) {
                                        result.add(new CustomError(columnName,rowNum,"char limit"));
                                    }
                                }
                                break;
                            case 4:
                            case 5:
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(!checkRegex("decimal",cellValue) || cellValue.length()>fieldLimit){
                                        result.add(new CustomError(columnName,rowNum," decimal format"));
                                    }
                                }
                                break;
                            case 6:
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(!checkRegex(fieldtype,cellValue) || cellValue.length()>4){
                                        result.add(new CustomError(columnName,rowNum," should be digit"));
                                    }
                                }
                                break;
                            case 7:
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(!checkRegex(fieldtype,cellValue) || cellValue.length()>fieldLimit){
                                        result.add(new CustomError(columnName,rowNum," incorrect Date"));
                                    }
                                }
                                break;
                            case 8:
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(cellValue.length()>fieldLimit){
                                        result.add(new CustomError(columnName,rowNum," limit"));
                                    }
                                }
                                break;
                        }
                    }
                }
            }

        }

        if(result.isEmpty()){
            ArrayList<ImpactLookup> lookupData = this.getLookupData();
            int index = 0;
            for(ImpactLookup i: requiredFieldData){
                if(!contains(lookupData,i)){
                    result.add(new CustomError("",index,"Please check lookup data"));
                }
                index++;
            }
        }

        model.addAttribute("error",result);

        return "impactResult";
    }

}
