package gov.hawaii.hidot.hwymail.ManageReport.Form;

import lombok.Data;

@Data
public class ReportSearchForm {
    private long searchReportGroup=-1;
    private long searchReportType=-1;
    private String searchName="";
}
