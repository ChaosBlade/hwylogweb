package gov.hawaii.hidot.hwymail.ManageReport.Entity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name="tbl20userreports")
@Entity
@Data
public class TBL20USERREPORTS {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="REPORTID")
    private long reportId;

    @Column(name="REPORTNAME")
    private String reportName;
    @Column(name="REPORTGROUP")
    private long reportGroup;
    @Column(name="REPORTTYPE")
    private long reportType;
    @Column(name="SORTORDER")
    private int reportOrder;
    @Column(name="DIVID")
    private boolean  divid;
    @Column(name="AVAILABLE")
    private boolean  Available;
    @Column(name="REPORTUSERLEVEL")
    private long reportUserLevel;
    @Column(name="METHODNAME")
    private String methodName;
    @Column(name="USERNAMEID")
    private String userNameID;
    @Column(name="LASTUPDATED")
    private Date lastUpdated;
}
