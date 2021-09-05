package gov.hawaii.hidot.hwymail.Controller;


import gov.hawaii.hidot.hwymail.Common.TopTitle;
import gov.hawaii.hidot.hwymail.ManageReport.Form.ReportGroupForm;
import gov.hawaii.hidot.hwymail.ManageReport.Service.ReportService;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Controller
public class MainController {
    private TopTitle toptitle=new TopTitle();
    private final ReportService reportservice;

    public MainController(ReportService reportservice) {
        this.reportservice = reportservice;
    }


    @RequestMapping("/")
    public String index(){
        return "login";
    }
    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    /* Main */
    @RequestMapping("/main")
    public String mainView(Model model){
        /*Report Group List*/
        List<ReportGroupForm> reportGroup1 = reportservice.findreportgroupList(1);
        List<ReportGroupForm> reportGroup2 = reportservice.findreportgroupList(2);
        List<ReportGroupForm> reportGroup3 = reportservice.findreportgroupList(3);
        List<ReportGroupForm> reportGroup4 = reportservice.findreportgroupList(4);
        toptitle.setTitleName("");
        toptitle.setTitleLink("#");
        model.addAttribute("topTitle",toptitle);
        model.addAttribute("reportGroup1",reportGroup1);
        model.addAttribute("reportGroup2",reportGroup2);
        model.addAttribute("reportGroup3",reportGroup3);
        model.addAttribute("reportGroup4",reportGroup4);
        return "Main/main";
    }

    /* Main */
    @RequestMapping("/loginerror")
    public String mloginerrorView(){
        return "redirect:/login";
    }


    /* Current Server Time */
    @RequestMapping("/main/currTime")
    @ResponseBody
    public String currTimeView(){
        SimpleDateFormat formatter= new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    /* Current Server Add days */
    @RequestMapping("/main/curraddDays")
    @ResponseBody
    public String currAdddaysView(@RequestParam(value = "noewdate") Date noewdate){
        SimpleDateFormat formatter= new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        c.setTime(noewdate);
        c.add(Calendar.DATE, 180); // Adding 180 days
        return formatter.format(c.getTime());
    }

}
