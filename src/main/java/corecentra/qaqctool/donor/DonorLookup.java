package corecentra.qaqctool.donor;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelSheet;
import lombok.AllArgsConstructor;

@ExcelSheet("Donors")
@AllArgsConstructor
public class DonorLookup {
    @ExcelCell(0)
    String sponsorID;

    @ExcelCell(1)
    String companyName;

    public DonorLookup(){}

    //region - getters setters

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

    //endregion

}
