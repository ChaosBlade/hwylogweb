package gov.hawaii.hidot.hwymail.HwyLog;

import gov.hawaii.hidot.hwymail.Common.PageModel;
import gov.hawaii.hidot.hwymail.Common.TopTitle;
import gov.hawaii.hidot.hwymail.HwyLog.Form.HwyMailLogForm;
import gov.hawaii.hidot.hwymail.HwyLog.Form.HwyMailLogListForm;
import gov.hawaii.hidot.hwymail.HwyLog.Form.HwyMailLogSearchForm;
import gov.hawaii.hidot.hwymail.HwyLog.Service.HwyLogService;
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

@Controller
public class HwyLogMailController {
    private final HwyLogService logservice;
    private final CodeService codeservice;
    private final PageModel pageModel;
    private TopTitle toptitle=new TopTitle();

    @Autowired
    public HwyLogMailController(HwyLogService logservice, CodeService codeservice, PageModel pageModel) {
        this.logservice = logservice;
        this.codeservice = codeservice;
        this.pageModel = pageModel;
    }

    /* list */
    @RequestMapping("/hwymaillog/list")
    public String listView(Model model, @ModelAttribute("pssSearch") HwyMailLogSearchForm psssearchForm){
        pageModel.initPageAndSize();
        pageModel.setSIZE(15);
        psssearchForm.setSearchTypeList(codeservice.findgroup(8));/*Filter Search Type List*/
        /*Paging Data */
        Page<HwyMailLogListForm> listFormpage=logservice.findAllMailLog(psssearchForm, PageRequest.of(pageModel.getPAGE(), pageModel.getSIZE(), Sort.by("InputDate").descending()));
        model.addAttribute("results",listFormpage);
        model.addAttribute("filterForm", psssearchForm);
        toptitle.setTitleName("HWY MAIL LOG");
        toptitle.setTitleLink("#");
        model.addAttribute("topTitle",toptitle);return "HwyLogMail/list";
    }

    /**
     * Input & Edit View
     */
    @RequestMapping("/hwymaillog/active/")
    public String addView(Model model){
        HwyMailLogListForm pssView=new HwyMailLogListForm();
        model.addAttribute("pssView",pssView);
        toptitle.setTitleName("HWY MAIL LOG");
        toptitle.setTitleLink("/hwymaillog/list");
        model.addAttribute("topTitle",toptitle);
        return "HwyLogMail/active";
    }

    @RequestMapping("/hwymaillog/active/{logID}")
    public String activeView(@PathVariable("logID") long logID, Model model){
        HwyMailLogForm pssView=logservice.findone(logID);
        model.addAttribute("pssView",pssView);
        toptitle.setTitleName("HWY MAIL LOG");
        toptitle.setTitleLink("/hwymaillog/list");
        model.addAttribute("topTitle",toptitle);
        return "HwyLogMail/active";
    }

    /**
     * Write  View
     */
    @RequestMapping(value = "/hwymaillog/write", method = RequestMethod.POST)
    public String writeEnd(@ModelAttribute("pssView") HwyMailLogForm pssinputForm){
        if (pssinputForm.getExpirationDate().equals("")){
            pssinputForm.setExpirationDate(pssinputForm.getInputDate());
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long logID=0;
        if (auth != null && (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("HW_A")))) {
            logID=logservice.add(pssinputForm);
        }
        if (logID==0){
            return "redirect:/hwymaillog/active";
        } else {
            return "redirect:/hwymaillog/active/"+logID;
        }

    }
    /**
     * Edit End View
     */
    @RequestMapping(value = "/hwymaillog/edit", method = RequestMethod.POST)
    public String editEnd(@ModelAttribute("pssView") HwyMailLogForm psseditForm){
        if (psseditForm.getExpirationDate().equals("")){
            psseditForm.setExpirationDate(psseditForm.getInputDate());
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("HW_E")))) {
            logservice.edit(psseditForm);
        }

        return "redirect:/hwymaillog/list";
    }

    /**
     * Delete End View
     */
    @RequestMapping("/hwymaillog/delete/{logID}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteEnd(@PathVariable("logID") long logID){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("HW_D")))) {
            logservice.findDeleteone(logID);
        }

    }
}
