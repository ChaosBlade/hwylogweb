package gov.hawaii.hidot.hwymail.HwyLog.Form;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class HwyMailLogListForm {
    private long logID;
    private Date InputDate;
    private Date expirationDate;
    private long NumberofDay;
    private String LetterNumber;
    private String Originator;
    private String Subject;
    private String currentAction;
    private boolean Completed;
    private String UserNameID;
    private Date lastUpdated;
}
