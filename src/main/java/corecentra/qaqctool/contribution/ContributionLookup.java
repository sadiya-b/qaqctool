package corecentra.qaqctool.contribution;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelSheet;
import lombok.AllArgsConstructor;

@ExcelSheet("Donor Contribution")
@AllArgsConstructor
public class ContributionLookup {
    @ExcelCell(0)
    String sponsorID;

    @ExcelCell(1)
    String companyName;

    @ExcelCell(2)
    String contributionID;


    public ContributionLookup(){}

    //region - getter setter

    public String getSponsorID() {
        return sponsorID;
    }

    public void setSponsorID(String sponsorID) {
        this.sponsorID = sponsorID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContributionID() {
        return contributionID;
    }

    public void setContributionID(String contributionID) {
        this.contributionID = contributionID;
    }

    //endregion
}
