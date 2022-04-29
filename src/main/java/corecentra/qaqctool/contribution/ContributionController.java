package corecentra.qaqctool.contribution;

import com.poiji.bind.Poiji;
import corecentra.qaqctool.CustomError;
import corecentra.qaqctool.fields.ContributionFieldRepository;
import corecentra.qaqctool.fields.ContributionFields;
import corecentra.qaqctool.fields.ImpactFields;
import corecentra.qaqctool.fileupload.FileUploadController;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.yaml.snakeyaml.util.EnumUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@AllArgsConstructor
@Controller
public class ContributionController {

    private String fileLocation = "src/main/java/corecentra/qaqctool/storage/";

    @Autowired
    FileUploadController fileUploadController = new FileUploadController();

    @Autowired
    ContributionFieldRepository contributionFieldRepository;

    public ContributionController() {}

    public ArrayList<ContributionLookup> getLookupData(){
        String filePath =
                fileLocation + "lookupfile_" + fileUploadController.getCurrentUser() + ".xlsx";

        ArrayList<ContributionLookup> lookupData =
                (ArrayList<ContributionLookup>) Poiji.fromExcel(new File(filePath), ContributionLookup.class);

        return lookupData;
    }

    public boolean contains(ArrayList<ContributionLookup> list , ContributionLookup b){
        return list.stream().anyMatch(x->x.getSponsorID().equals(b.sponsorID)
                && x.getCompanyName().equals(b.companyName)
                && x.getContributionID().equals(b.contributionID));
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
            case "restriction":
                isMatch = Arrays.stream(Restriction.values()).anyMatch((t) -> t.name().equalsIgnoreCase(value));
                break;
            case "payment":
                isMatch = Arrays.stream(Payment.values()).anyMatch((t) -> t.name().equalsIgnoreCase(value));
                break;
            default:
                break;

        }
        return isMatch;
    }

    @GetMapping("/result/contribution")
    public String evaluateProject(Model model) throws Exception {
        String filename =  "contribution_" + fileUploadController.getCurrentUser() + ".xlsx";

        List<ContributionFields> contributionFields = contributionFieldRepository.findAll();

        //getting all the column indices present in the db
        List<Integer> columnIndex = new ArrayList<>();
        for (ContributionFields p : contributionFields) {
            int id = (int)p.getId();
            columnIndex.add(id);
        }

        List<CustomError> result = new ArrayList<>();

        //check if file exists
        if(!Files.exists(Path.of(fileLocation.concat(filename)))){
            throw new Exception("no such file exists");
        }

        ArrayList<ContributionLookup> requiredFieldData =
                (ArrayList<ContributionLookup>) Poiji.fromExcel(new File(fileLocation.concat(filename)), ContributionLookup.class);

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
                        String columnName = contributionFieldRepository.getById((long)col).getFieldname();
                        String fieldtype = contributionFieldRepository.getById((long)col).getFieldtype();
                        long fieldLimit = contributionFieldRepository.getById((long)col).getFieldlimit();

                        switch (col) {
                            case 0:
                            case 1:
                                if(cellValue.isEmpty() || cellValue.isBlank()){
                                    result.add(new CustomError(columnName,rowNum,String.format("%s field is blank, %s is a required field.", columnName, columnName)));
                                }
                                else {
                                    if (cellValue.length()>fieldLimit) {
                                        result.add(new CustomError(columnName,rowNum,String.format("%s field can not exceed %s chars.", columnName, fieldLimit)));
                                    }
                                }
                                break;
                            case 2:
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(!checkRegex(fieldtype,cellValue)){
                                        result.add(new CustomError(columnName,rowNum,String.format("%s must be a digit value.", columnName)));
                                    }
                                }
                                break;
                            case 3:
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(!checkRegex(fieldtype,cellValue)){
                                        result.add(new CustomError(columnName,rowNum,String.format("%s must be a digit value.", columnName)));
                                    }
                                    if (checkRegex(fieldtype,cellValue) && cellValue.length() > fieldLimit) {
                                        result.add(new CustomError(columnName,rowNum,String.format("%s field can not exceed %s chars.", columnName, fieldLimit)));
                                    }
                                }
                                break;
                            case 4:
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(!checkRegex(fieldtype,cellValue) || cellValue.length()>fieldLimit){
                                        result.add(new CustomError(columnName,rowNum,String.format("%s must be a decimal format value.", columnName)));
                                    }
                                }
                                break;
                            case 5:
                            case 6:
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(!checkRegex(fieldtype,cellValue) || cellValue.length()>fieldLimit){
                                        result.add(new CustomError(columnName,rowNum,String.format("%s has incorrect date format.", columnName)));
                                    }
                                }
                                break;
                            case 7:
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(cellValue.length()>fieldLimit){
                                        result.add(new CustomError(columnName,rowNum,String.format("%s field can not exceed %s chars.", columnName, fieldLimit)));
                                    }
                                }
                                break;
                            case 8:
                            case 9:
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(!checkRegex(fieldtype,cellValue)){
                                        result.add(new CustomError(columnName,rowNum,String.format("%s has enum error.", columnName)));
                                    }
                                }
                                break;
                        }
                    }
                }
            }

        }

        if(result.isEmpty()){
            ArrayList<ContributionLookup> lookupData = this.getLookupData();
            int index = 0;
            for(ContributionLookup i: requiredFieldData){
                if(!contains(lookupData,i)){
                    result.add(new CustomError("",index+1,"Please check lookup data"));
                }
                index++;
            }
        }

        model.addAttribute("error",result);

        return "contributionResult";
    }

}
