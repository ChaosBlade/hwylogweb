package gov.hawaii.hidot.hwymail.HwySignLog.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name="tbl30signlogaction")
@Data
@Entity
public class TBL30SIGNLOGACTION {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="LOGACTIONID")
    private long logActionId;
    @Column(name="LOGID")
    private long logID;
    @Column(name="ACTIONTYPE")
    private String ActionType;
    @Column(name="FORMDATE")
    private Date formDate;
    @Column(name="ACTIONTAKEN")
    private String actionTaken;
    @Column(name="DELETED")
    private boolean Deleted;
    @Column(name="USERNAMEID")
    private String UserNameID;
    @Column(name="LASTUPDATED")
    private Date lastUpdated;
}
