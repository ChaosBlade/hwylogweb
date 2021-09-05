package gov.hawaii.hidot.hwymail.ManageCompany.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name="tbl20usercompany")
@Data
@Entity
public class TBL20USERCOMPANY {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="COMPANYID")
    private long CompanyID;
    @Column(name="COMTYPE")
    private long comType;
    @Column(name="COMISLAND")
    private long comIsland;
    @Column(name="COMNAME")
    private String comName;
    @Column(name="COMBRANCH")
    private String comBranch;
    @Column(name="COMADDRESS")
    private String comAddress;
    @Column(name="COMCITY")
    private String comCity;
    @Column(name="COMSTATE")
    private String comState;
    @Column(name="COMZIP")
    private String comZip;
    @Column(name="COMTEL")
    private String comTel;
    @Column(name="COMEMAIL")
    private String comEmail;

    @Column(name="OFFICEDETAIL")
    private String officeDetail;
    @Column(name="OFFICESECTIONHEADNAME")
    private String OfficeSectionHeadName;



    @Column(name="USERNAMEID")
    private String UserNameID;
    @Column(name="LASTUPDATED")
    private Date lastUpdated;
}
