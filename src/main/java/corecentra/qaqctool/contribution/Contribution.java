package corecentra.qaqctool.contribution;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelSheet;
import lombok.AllArgsConstructor;

@ExcelSheet("Donor Contribution")
@AllArgsConstructor
public class Contribution {
    @ExcelCell(0)
    String sponsorID;

    @ExcelCell(1)
    String companyName;

    @ExcelCell(2)
    Integer contributionID;

    @ExcelCell(3)
    int reportingYear;

    @ExcelCell(4)
    double amountContributed;

    @ExcelCell(5)
    String dateExpected;

    @ExcelCell(6)
    String dateContributed;

    @ExcelCell(7)
    String contributionStatus;

    @ExcelCell(8)
    Enum<Restriction> restrictionEnum;

    @ExcelCell(9)
    Enum<Payment> paymentEnum;

    public Contribution(){}

    public Contribution(String sponsorID, String companyName, Integer contributionID) {
        this.sponsorID = sponsorID;
        this.companyName = companyName;
        this.contributionID = contributionID;
    }
}

