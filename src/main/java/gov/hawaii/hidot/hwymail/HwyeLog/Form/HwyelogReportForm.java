package gov.hawaii.hidot.hwymail.HwyeLog.Form;

import lombok.Data;

@Data
public class HwyelogReportForm {
    private long logID;
    private String inputDate;
    private String expirationDate;
    private String letterNumber;
    private String Originator;
    private String subject;
    private String currentAction;
}
