package gov.hawaii.hidot.hwymail.ManageReport.Form;
import lombok.Data;
@Data
public class ReportForm {
    private long reportId;
    private String reportName;
    private long reportGroup;
    private String reportGroupName;
    private long reportType;
    private String reportTypeName;
    private int reportOrder;
    private boolean  divid;
    private boolean  Available;
    private long reportUserLevel;
    private String methodName;
    private String userNameId;
    private String UserName;
    private String lastUpdated;

}
