package gov.hawaii.hidot.hwymail.ManageCompany.Form;

import lombok.Data;

@Data
public class CompanyGroupForm {
    private long CompanyID;
    private String comName;
    private long comIsland;
    private String comIslandname;
    private String comBranch;
}
