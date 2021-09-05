package gov.hawaii.hidot.hwymail.HwyeLog.Form;

import lombok.Data;

import java.util.Date;

@Data
public class HwyeMailLogForm {
    private long logID;
    private Date InputDate;
    private Date letternDate;
    private String LetterNumber;
    private String Originator;
    private String Subject;
    private String currentAction;
    private String finalAction;
    private String suspenddate;
    private String actionrequested;
    private String datefiled;
    private String placefiled;
    private String UserNameID;
    private Date lastUpdated;
}
