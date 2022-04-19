package corecentra.qaqctool.donor;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelSheet;
import lombok.AllArgsConstructor;

@ExcelSheet("Projects")
@AllArgsConstructor
public class Donor {
    @ExcelCell(0)
    String sponsorID;

    @ExcelCell(1)
    String companyName;

    @ExcelCell(2)
    String dateCreated;

    @ExcelCell(3)
    String dateClosed;

    @ExcelCell(4)
    String companyTelephone;

    @ExcelCell(5)
    String companyAddress1;

    @ExcelCell(6)
    String CompanyAddress2;

    @ExcelCell(7)
    String companyCity;

    @ExcelCell(8)
    String companyState;

    @ExcelCell(9)
    String companyZipCode ;

    @ExcelCell(10)
    String companyCountry;

    @ExcelCell(11)
    String contactName;

    @ExcelCell(12)
    String contactTitle;

    @ExcelCell(13)
    String contactAddress1;

    @ExcelCell(14)
    String contactAddress2;

    @ExcelCell(15)
    String contactCity;

    @ExcelCell(16)
    String contactState;

    @ExcelCell(17)
    String contactZipCode;

    @ExcelCell(18)
    String contactCountry;

    @ExcelCell(19)
    String contactEmail;

    @ExcelCell(20)
    String contactTelephone;

    public Donor(){}

    public Donor(String sponsorID, String companyName) {
        this.sponsorID = sponsorID;
        this.companyName = companyName;
    }
}
