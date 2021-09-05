package gov.hawaii.hidot.hwymail.Report.Form;

import lombok.Data;

import java.util.Date;

@Data
public class HwySignReportForm {
    private long logID;
    private Date inputDate;
    private Date expirationDate;
    private String letterNumber;
    private String Originator;
    private String subject;
    private String currentAction;

    /**HWY-E MAIL Only*/
    private Date letternDate;
    private String finalAction;
    private String suspenddate;
    private String actionrequested;
    private String datefiled;
    private String placefiled;


}
