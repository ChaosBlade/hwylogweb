package gov.hawaii.hidot.hwymail.HwyLog.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name="tbl20maillog")
@Data
@Entity
public class TBL20MAILLOG {
    @Id
    @Column(name="LOGID")
    private long logID;
    @Column(name="INPUTDATE")
    private Date InputDate;
    @Column(name="EXPIRATIONDATE")
    private Date expirationDate;
    @Column(name="LETTERNUMBER")
    private String LetterNumber;
    @Column(name="ORIGINATOR")
    private String Originator;
    @Column(name="SUBJECT")
    private String Subject;
    @Column(name="CURRENTACTION")
    private String currentAction;
    @Column(name="FINALACTION")
    private String FinalAction;
    @Column(name="COMPLETED")
    private boolean Completed;
    @Column(name="USERNAMEID")
    private String UserNameID;
    @Column(name="LASTUPDATED")
    private Date lastUpdated;
}
