package gov.hawaii.hidot.hwymail.HwyeLog;

import gov.hawaii.hidot.hwymail.Common.PageModel;
import gov.hawaii.hidot.hwymail.Common.TopTitle;
import gov.hawaii.hidot.hwymail.HwyeLog.Form.HwyeMailLogForm;
import gov.hawaii.hidot.hwymail.HwyeLog.Form.HwyeMailLogSearchForm;
import gov.hawaii.hidot.hwymail.HwyeLog.Service.HwyeLogService;
import gov.hawaii.hidot.hwymail.ManagoCode.Service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HwyeLogMailController {
    private final HwyeLogService logservice;
    private final CodeService codeservice;
    private final PageModel pageModel;
    private TopTitle toptitle=new TopTitle();

    @Autowired
    public HwyeLogMailController(HwyeLogService logservice, CodeService codeservice, PageModel pageModel) {
        this.logservice = logservice;
        this.codeservice = codeservice;
        this.pageModel = pageModel;
    }

    /* list */
    @RequestMapping("/hwyemaillog/list")
    public String listView(Model model, @ModelAttribute("pssSearch") HwyeMailLogSearchForm psssearchForm){
        pageModel.initPageAndSize();
        pageModel.setSIZE(15);
        psssearchForm.setSearchTypeList(codeservice.findgroup(8));/*Filter Search Type List*/
        /*Paging Data */
        Page<HwyeMailLogForm> listFormpage=logservice.findAllMailLog(psssearchForm, PageRequest.of(pageModel.getPAGE(), pageModel.getSIZE(), Sort.by("InputDate").descending()));
        model.addAttribute("results",listFormpage);
        model.addAttribute("filterForm", psssearchForm);
        toptitle.setTitleName("HWY-E MAIL LOG");
        toptitle.setTitleLink("#");
        model.addAttribute("topTitle",toptitle);return "HwyeLogMail/list";
    }

    /**
     * Input & Edit View
     */
    @RequestMapping("/hwyemaillog/active/")
    public String addView(Model model){
        HwyeMailLogForm pssView=new HwyeMailLogForm();
        model.addAttribute("pssView",pssView);
        toptitle.setTitleName("HWY-E MAIL LOG");
        toptitle.setTitleLink("/hwyemaillog/list");
        model.addAttribute("topTitle",toptitle);
        return "HwyeLogMail/active";
    }

    @RequestMapping("/hwyemaillog/active/{logID}")
    public String activeView(@PathVariable("logID") long logID, Model model){
        HwyeMailLogForm pssView=logservice.findone(logID);
        model.addAttribute("pssView",pssView);
        toptitle.setTitleName("HWY-E MAIL LOG");
        toptitle.setTitleLink("/hwyemaillog/list");
        model.addAttribute("topTitle",toptitle);
        return "HwyeLogMail/active";
    }

    /**
     * Write  View
     */
    @RequestMapping(value = "/hwyemaillog/write", method = RequestMethod.POST)
    public String writeEnd(@ModelAttribute("pssView") HwyeMailLogForm pssinputForm){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("HWE_A")))) {
            logservice.add(pssinputForm);
        }
        return "redirect:/hwyemaillog/list";

    }
    /**
     * Edit End View
     */
    @RequestMapping(value = "/hwyemaillog/edit", method = RequestMethod.POST)
    public String editEnd(@ModelAttribute("pssView") HwyeMailLogForm psseditForm){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("HWE_E")))) {
            logservice.edit(psseditForm);
        }

        return "redirect:/hwyemaillog/list";
    }

    /**
     * Delete End View
     */
    @RequestMapping("/hwyemaillog/delete/{logID}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteEnd(@PathVariable("logID") long logID){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("HWE_D")))) {
            logservice.findDeleteone(logID);
        }

    }




    /**
     * HWY-E Mail Print
     * List
     */
    @RequestMapping(value = "/hwyemaillog/allhwyemaillog")
    @ResponseBody
    public List<Long> allhwyemaillogList(@RequestParam("searchsdate") String searchsdate,@RequestParam("searchedate") String searchedate,@RequestParam("searchType") long searchType,@RequestParam("searchName") String searchName){
        List<Long> rs=null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("HWE_R"))) {
            rs=logservice.findallhwyemaillogList(searchsdate,searchedate,searchType,searchName);
        }
        return rs;
    }







}
