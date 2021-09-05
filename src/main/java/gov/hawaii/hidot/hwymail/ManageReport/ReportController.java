package gov.hawaii.hidot.hwymail.ManageReport;

import gov.hawaii.hidot.hwymail.Common.PageModel;
import gov.hawaii.hidot.hwymail.Common.TopTitle;
import gov.hawaii.hidot.hwymail.ManagoCode.Form.CodeForm;
import gov.hawaii.hidot.hwymail.ManageReport.Form.ReportForm;
import gov.hawaii.hidot.hwymail.ManageReport.Form.ReportSearchForm;
import gov.hawaii.hidot.hwymail.ManagoCode.Service.CodeService;
import gov.hawaii.hidot.hwymail.ManageReport.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ReportController {

    private final ReportService reportservice;
    private final CodeService codeservice;
    private final PageModel pageModel;
    private TopTitle toptitle=new TopTitle();
    @Autowired
    public ReportController(ReportService reportservice, CodeService codeservice, PageModel pageModel) {
        this.reportservice = reportservice;
        this.codeservice = codeservice;
        this.pageModel = pageModel;
    }

    /* list */
    @RequestMapping("/report/list")
    public String listView(Model model,@ModelAttribute("pssSearch") ReportSearchForm psssearchForm){
        pageModel.initPageAndSize();
        pageModel.setSIZE(15);
        List<CodeForm> reportGroup = codeservice.findgroup(3);  /*Filter reportGroup List*/
        List<CodeForm> reportType = codeservice.findgroup(4); /*Filter report Type List*/

        /*Paging Data */
        Page<ReportForm> listFormpage=reportservice.findCodes(psssearchForm, PageRequest.of(pageModel.getPAGE(), pageModel.getSIZE(), Sort.by("lastUpdated").descending()));
        model.addAttribute("reportGroup",reportGroup);
        model.addAttribute("reportType",reportType);
        model.addAttribute("results",listFormpage);
        model.addAttribute("filterForm", psssearchForm);
        toptitle.setTitleName("Manage Reports");
        toptitle.setTitleLink("#");
        model.addAttribute("topTitle",toptitle);return "Report/list";
    }

    /**
     * Active View
     */
    @RequestMapping("/report/active/")
    public String addView(Model model){
        ReportForm pssView=new ReportForm();

        List<CodeForm> reportGroup = codeservice.findgroup(3);/*reportGroup List*/
        List<CodeForm> reportType = codeservice.findgroup(4);/*report Type List*/
        List<CodeForm> reportUserLevel = codeservice.findgroup(2);/*report Level Type List*/
        model.addAttribute("reportGroup",reportGroup);
        model.addAttribute("reportType",reportType);
        model.addAttribute("pssView",pssView);
        model.addAttribute("reportUserLevel",reportUserLevel);
        toptitle.setTitleName("Manage Reports");
        toptitle.setTitleLink("/report/list");
        model.addAttribute("topTitle",toptitle);
        return "Report/active";
    }
    @RequestMapping("/report/active/{reportId}")
    public String activeView(@PathVariable("reportId") long reportId, Model model){
        ReportForm pssView=reportservice.findone(reportId);

        List<CodeForm> reportGroup = codeservice.findgroup(3);/*reportGroup List*/
        List<CodeForm> reportType = codeservice.findgroup(4);/*report Type List*/
        List<CodeForm> reportUserLevel = codeservice.findgroup(2);/*report Level Type List*/
        model.addAttribute("reportGroup",reportGroup);
        model.addAttribute("reportType",reportType);
        model.addAttribute("pssView",pssView);
        model.addAttribute("reportUserLevel",reportUserLevel);
        toptitle.setTitleName("Manage Reports");
        toptitle.setTitleLink("/report/list");
        model.addAttribute("topTitle",toptitle);
        return "Report/active";
    }

    /**
     * Write  View
     */
    @RequestMapping(value = "/report/write", method = RequestMethod.POST)
    public String writeEnd(@ModelAttribute("pssView") ReportForm pssinputForm){
        reportservice.add(pssinputForm);
        return "redirect:/report/list";
    }
    /**
     * Edit End View
     */
    @RequestMapping(value = "/report/edit", method = RequestMethod.POST)
    public String editEnd(@ModelAttribute("pssView") ReportForm psseditForm){
        reportservice.edit(psseditForm);
        return "redirect:/report/list";
    }

    /**
     * Delete End View
     */
    @RequestMapping("/report/delete/{reportId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteEnd(@PathVariable("reportId") long reportId){
        reportservice.findDeleteone(reportId);
    }






}
