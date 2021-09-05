package gov.hawaii.hidot.hwymail.ManageCompany.Form;

import lombok.Data;

@Data
public class CompanyForm {
    private long CompanyID;
    private long comType;
    private String comTypename;
    private long comIsland;
    private String comIslandname;
    private String comName;
    private String comBranch;
    private String comAddress;
    private String comCity;
    private String comState;
    private String comZip;
    private String comTel;
    private String comEmail;

    private String officeDetail;
    private String OfficeSectionHeadName;
    private String OfficeSectionHeadPhone;
    private String OfficeSectionHeadEmail;




    private String UserNameID;
    private String lastUpdated;
}
