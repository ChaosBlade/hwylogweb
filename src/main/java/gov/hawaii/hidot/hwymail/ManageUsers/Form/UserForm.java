package gov.hawaii.hidot.hwymail.ManageUsers.Form;
import lombok.Data;

@Data
public class UserForm {
    private String UserID;
    private String FirstName;
    private String MidName;
    private String LastName;
    private String Position;
    private String Password;
    private String oldPassword;
    private String PhoneNumber;
    private String Email;
    private String BranchName;
    private boolean Administrator;
    private boolean ExpiredUser;
    private long comID;
    private long reportuserLevel;
    private String reportuserLevelname;
    private String UserNameID;
    private String lastUpdated;
    private UserSecurityForm securityForm;
    private String hwylogAuth="";
    private String hwyelogAuth="";
    private String signaturelogAuth="";
    private String manageuserauth="";
    private String managecompanyauth="";
    private String managereportauth="";
    private String managecodeauth="";


}
