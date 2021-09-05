package gov.hawaii.hidot.hwymail.HwySignLog.Form;

import lombok.Data;

import java.util.Date;
@Data
public class HwySignLogActionForm {
    private long logActionId;
    private long logID;
    private String ActionType;
    private String formDate;
    private String actionTaken;
    private boolean Deleted;
    private String UserNameID;
    private Date lastUpdated;
}
