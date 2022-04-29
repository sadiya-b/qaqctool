package corecentra.qaqctool.donor;

import com.poiji.bind.Poiji;
import corecentra.qaqctool.CustomError;
import corecentra.qaqctool.fields.DonorFieldRepository;
import corecentra.qaqctool.fields.DonorFields;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@AllArgsConstructor
@Controller
public class DonorController {

    private String fileLocation = "src/main/java/corecentra/qaqctool/storage/";

    @Autowired
    FileUploadController fileUploadController = new FileUploadController();

    @Autowired
    DonorFieldRepository donorFieldRepository;

    public DonorController() {}

    public ArrayList<DonorLookup> getLookupData(){
        String filePath =
                fileLocation + "lookupfile_" + fileUploadController.getCurrentUser() + ".xlsx";

        ArrayList<DonorLookup> lookupData =
                (ArrayList<DonorLookup>) Poiji.fromExcel(new File(filePath), DonorLookup.class);

        return lookupData;
    }

    public boolean contains(ArrayList<DonorLookup> list , DonorLookup b){
        return list.stream().anyMatch(x->x.getSponsorID() == b.sponsorID
               && x.getCompanyName().equals(b.companyName));
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
            case "phone":
                String phonePattern = "[\\(]?\\d{3}[\\)]?([-.]?)\\s*\\d{3}\\1\\s*\\d{4}";
                isMatch = value.matches(phonePattern);
                /*approves all the following:
                (123).123.1234 -- true
                 1234567890 -- true
                (123)1231234 -- true
                (123)-456-7890 -- true
                (123) 123 1234 -- true
                123   123    1234 -- true
                (123). 456. 7890 -- true
                (123)- 456- 7890 -- true.
                 */
            break;
            case "zipcode":
                String zip = "^[0-9]{5}(?:-[0-9]{4})?$";
                isMatch = value.matches(zip);
                break;
            case "email":
                String emailPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
                isMatch = value.matches(emailPattern);
                break;
            default:
                break;

        }
        return isMatch;
    }

    @GetMapping("/result/donor")
    public String evaluateProject(Model model) throws Exception {
        String filename =  "donor_" + fileUploadController.getCurrentUser() + ".xlsx";

        List<DonorFields> donorFields = donorFieldRepository.findAll();

        //getting all the column indices present in the db
        List<Integer> columnIndex = new ArrayList<>();
        for (DonorFields p : donorFields) {
            int id = (int)p.getId();
            columnIndex.add(id);
        }

        List<CustomError> result = new ArrayList<>();

        //check if file exists
        if(!Files.exists(Path.of(fileLocation.concat(filename)))){
            throw new Exception("no such file exists");
        }

        ArrayList<DonorLookup> requiredFieldData =
                (ArrayList<DonorLookup>) Poiji.fromExcel(new File(fileLocation.concat(filename)), DonorLookup.class);

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
                        String columnName = donorFieldRepository.getById((long)col).getFieldname();
                        String fieldtype = donorFieldRepository.getById((long)col).getFieldtype();
                        long fieldLimit = donorFieldRepository.getById((long)col).getFieldlimit();

                        switch (col) {
                            case 0:
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(cellValue.length()>fieldLimit){
                                        result.add(new CustomError(columnName,rowNum,String.format("%s field can not exceed %s chars.", columnName, fieldLimit)));
                                    }
                                }
                                break;
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
                            case 3:
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(!checkRegex(fieldtype,cellValue) || cellValue.length()>fieldLimit){
                                        result.add(new CustomError(columnName,rowNum,String.format("%s has incorrect date format.", columnName)));
                                    }
                                }
                            case 4:
                            case 20:
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(!checkRegex(fieldtype,cellValue) || cellValue.length()>fieldLimit){
                                        result.add(new CustomError(columnName,rowNum,String.format("%s has incorrect phone format.", columnName)));
                                    }
                                }
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 10:
                            case 11:
                            case 12:
                            case 13:
                            case 14:
                            case 15:
                            case 16:
                            case 18:
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if(cellValue.length()>fieldLimit){
                                        result.add(new CustomError(columnName,rowNum,String.format("%s field can not exceed %s chars.", columnName, fieldLimit)));
                                    }
                                }
                                break;
                            case 9:
                            case 17:
                            case 19:
                                //zip code
                                if(!cellValue.isEmpty() && !cellValue.isBlank()) {
                                    if (checkRegex(fieldtype,cellValue) && cellValue.length() > fieldLimit) {
                                        result.add(new CustomError(columnName,rowNum,String.format("%s field can not exceed %s chars.", columnName, fieldLimit)));
                                    }
                                    if(!checkRegex(fieldtype,cellValue)){
                                        result.add(new CustomError(columnName,rowNum,String.format("%s has incorrect field type.", columnName)));
                                    }
                                }
                                break;
                        }
                    }
                }
            }

        }

        if(result.isEmpty()){
            ArrayList<DonorLookup> lookupData = this.getLookupData();
            int index = 0;
            for(DonorLookup i: requiredFieldData){
                if(!contains(lookupData,i)){
                    result.add(new CustomError("",index+1,"Please check lookup data"));
                }
                index++;
            }
        }

        model.addAttribute("error",result);

        return "donorResult";
    }

}
