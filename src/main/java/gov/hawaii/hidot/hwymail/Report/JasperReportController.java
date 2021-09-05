package gov.hawaii.hidot.hwymail.Report;

import gov.hawaii.hidot.hwymail.HwyLog.Service.HwyLogService;
import gov.hawaii.hidot.hwymail.Report.Form.HwySignReportForm;
import gov.hawaii.hidot.hwymail.HwySignLog.Service.HwySignLogService;
import gov.hawaii.hidot.hwymail.HwyeLog.Service.HwyeLogService;
import gov.hawaii.hidot.hwymail.ManageReport.Form.ReportForm;
import gov.hawaii.hidot.hwymail.ManageReport.Service.ReportService;
import gov.hawaii.hidot.hwymail.ManagoCode.Service.CodeService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class JasperReportController {
    final Logger log = LoggerFactory.getLogger(this.getClass());
    private final HwyLogService logservice;
    private final HwyeLogService hwyelogservice;
    private final HwySignLogService signlogservice;
    private final CodeService codeservice;
    private final ReportService reportservice;



    @Autowired
    public JasperReportController(HwyLogService logservice, HwyeLogService hwyelogservice, HwySignLogService signlogservice, CodeService codeservice, ReportService reportservice) {
        this.logservice = logservice;
        this.hwyelogservice = hwyelogservice;
        this.signlogservice = signlogservice;
        this.codeservice = codeservice;
        this.reportservice = reportservice;
    }

    @GetMapping(value = "/report/{reportID}")
    @ResponseStatus(value = HttpStatus.OK)
    public void viewReport(@PathVariable("reportID")  long reportID, HttpServletResponse response) {

        ReportForm reportform= reportservice.findone(reportID);
        String Searchname="";

        if (reportform.getReportId()==1) {
            //OutStanding GOV.
            Searchname = "GOV";
        } else if (reportform.getReportId()==2){
            //OutStanding DIR.
            Searchname="DIR";
        } else if (reportform.getReportId()==3){
            //OutStanding HWY.
            Searchname="HWY";
        } else {
            //OutStanding MAIL.
            Searchname="HWY";
        }
        String methodXml=reportform.getMethodName();
        log.info("Preparing the pdf report.");
        try {
            if (reportform.getReportId()==4) {
                //OutStanding MAIL.
                createPdfReport(logservice.findAllMailLogReport(Searchname),methodXml,response);
            } else {
                createPdfReport(signlogservice.findAllSignLogReport(Searchname),methodXml,response);
            }
            log.info("File successfully saved at the given path.");
        } catch (final Exception e) {
            log.error("Some error has occurred while preparing the pdf report.");
            e.printStackTrace();
        }
    }


    @GetMapping(value = "/mailreport/{logIDList}")
    @ResponseStatus(value = HttpStatus.OK)
    public void viewMailReport(@PathVariable("logIDList")  Long[] logIDList, HttpServletResponse response) {
        log.info("Preparing the pdf report.");
        try {
            createPdfReport(logservice.findReportselected(logIDList),"maillogReport.jrxml",response);
            log.info("File successfully saved at the given path.");
        } catch (final Exception e) {
            log.error("Some error has occurred while preparing the pdf report.");
            e.printStackTrace();
        }
    }


    @GetMapping(value = "/signreport/{logIDList}")
    @ResponseStatus(value = HttpStatus.OK)
    public void viewSignReport(@PathVariable("logIDList")  Long[] logIDList, HttpServletResponse response) {
        log.info("Preparing the pdf report.");
        try {
            createPdfReport(signlogservice.findReportselected(logIDList),"signlogReport.jrxml",response);
            log.info("File successfully saved at the given path.");
        } catch (final Exception e) {
            log.error("Some error has occurred while preparing the pdf report.");
            e.printStackTrace();
        }
    }


    @GetMapping(value = "/hwyemailreport/{logIDList}")
    @ResponseStatus(value = HttpStatus.OK)
    public void viewhwyeMailReport(@PathVariable("logIDList")  Long[] logIDList, HttpServletResponse response) {
        log.info("Preparing the pdf report.");
        try {
            createPdfReport(hwyelogservice.findReportselected(logIDList),"hwyemaillogReport.jrxml",response);
            log.info("File successfully saved at the given path.");
        } catch (final Exception e) {
            log.error("Some error has occurred while preparing the pdf report.");
            e.printStackTrace();
        }
    }






    private void createPdfReport(final List<HwySignReportForm> maillog,String methodXml,HttpServletResponse response ) throws JRException {
        // Fetching the .jrxml file from the resources folder.
        final InputStream stream = this.getClass().getResourceAsStream("/report/"+methodXml);

        // Compile the Jasper report from .jrxml to .japser
        final JasperReport report = JasperCompileManager.compileReport(stream);

        // Fetching the employees from the data source.
        final JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(maillog);

        // Adding the additional parameters to the pdf.
        final Map<String, Object> parameters = new HashMap<>();
        //parameters.put("ReportTitle", reportName);
        // Filling the report with the employee data and additional parameters information.
        final JasperPrint print = JasperFillManager.fillReport(report, parameters, source);

        try {
            ServletOutputStream streamDeSaida = response.getOutputStream();
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "inline; filename=\"report.pdf\"");
            JasperExportManager.exportReportToPdfStream(print,streamDeSaida);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
