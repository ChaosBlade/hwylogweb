package gov.hawaii.hidot.hwymail.ManageCompany.Form;

import lombok.Data;

@Data
public class CompanySearchForm {
    private long searchIsland=-1;
    private long searchType=-1;
    private String searchName="";
}
