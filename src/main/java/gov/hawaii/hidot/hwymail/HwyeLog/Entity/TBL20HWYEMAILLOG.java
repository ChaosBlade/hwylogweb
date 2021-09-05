package gov.hawaii.hidot.hwymail.HwyeLog.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name="tbl20hwyemaillog")
@Data
@Entity
public class TBL20HWYEMAILLOG {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="LOGID")
    private long logID;
    @Column(name="INPUTDATE")
    private Date InputDate;
    @Column(name="LETTERDATE")
    private Date letternDate;
    @Column(name="LETTERNUMBER")
    private String LetterNumber;
    @Column(name="ORIGINATOR")
    private String Originator;
    @Column(name="SUBJECT")
    private String Subject;
    @Column(name="CURRENTACTION")
    private String currentAction;
    @Column(name="FINALACTION")
    private String finalAction;

    @Column(name="SUSPENDDATE")
    private String suspenddate;
    @Column(name="ACTIONREQUESTED")
    private String actionrequested;

    @Column(name="DATEFILED")
    private String datefiled;
    @Column(name="PLACEFILED")
    private String placefiled;


    @Column(name="USERNAMEID")
    private String UserNameID;
    @Column(name="LASTUPDATED")
    private Date lastUpdated;
}
