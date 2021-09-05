package gov.hawaii.hidot.hwymail.ManageReport.Form;

import lombok.Data;

import java.util.List;

@Data
public class ReportGroupForm {
    private long reportType;
    private String reportTypename;
    private List<ReportForm> reportform;

    public ReportGroupForm(long reportType, String reportTypename, List<ReportForm> reportform) {
        this.reportType = reportType;
        this.reportTypename = reportTypename;
        this.reportform = reportform;
    }
}
