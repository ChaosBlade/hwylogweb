package gov.hawaii.hidot.hwymail.HwySignLog.Form;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class HwySignLogListForm {
    private long logID;
    private Date inputDate;
    private Date expirationDate;
    private long NumberofDay;
    private String LetterNumber;
    private String Originator;
    private String Subject;
    private String FinalAction;
    private String currentAction;
    private boolean Completed;
    private String UserNameID;
    private Date lastUpdated;
}
