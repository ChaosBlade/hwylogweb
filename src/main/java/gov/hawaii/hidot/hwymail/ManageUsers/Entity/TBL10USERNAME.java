package gov.hawaii.hidot.hwymail.ManageUsers.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name="tbl10username")
@Data
@Entity
public class TBL10USERNAME {
    @Id
    @Column(name="USERID")
    private String userID;
    @Column(name="FIRSTNAME")
    private String FirstName;
    @Column(name="MIDNAME")
    private String MidName;
    @Column(name="LASTNAME")
    private String LastName;
    @Column(name="POSITION")
    private String Position;
    @Column(name="PASSWORD")
    private String Password;
    @Column(name="PHONENUMBER")
    private String PhoneNumber;
    @Column(name="EMAIL")
    private String Email;
    @Column(name="ADMINISTRATOR")
    private boolean Administrator;
    @Column(name="EXPIREDUSER")
    private boolean ExpiredUser;
    @Column(name="COMID")
    private long comID;
    @Column(name="REPORTUSERLEVEL")
    private long reportuserLevel;
    @Column(name="HWYLOGAUTH")
    private String hwylogAuth;
    @Column(name="HWYELOGAUTH")
    private String hwyelogAuth;
    @Column(name="SIGNATURELOGAUTH")
    private String signaturelogAuth;

    @Column(name="MANAGEUSERAUTH")
    private String manageuserauth;
    @Column(name="MANAGECOMPANYAUTH")
    private String managecompanyauth;
    @Column(name="MANAGEREPORTAUTH")
    private String managereportauth;
    @Column(name="MANAGECODEAUTH")
    private String managecodeauth;

    @Column(name="USERNAMEID")
    private String UserNameID;
    @Column(name="LASTUPDATED")
    private Date lastUpdated;
}
