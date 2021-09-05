package gov.hawaii.hidot.hwymail.HwySignLog;

import gov.hawaii.hidot.hwymail.Common.PageModel;
import gov.hawaii.hidot.hwymail.Common.TopTitle;
import gov.hawaii.hidot.hwymail.HwySignLog.Form.HwySignLogForm;
import gov.hawaii.hidot.hwymail.HwySignLog.Form.HwySignLogListForm;
import gov.hawaii.hidot.hwymail.HwySignLog.Form.HwySignLogSearchForm;
import gov.hawaii.hidot.hwymail.HwySignLog.Service.HwySignLogService;
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
public class HwySignLogController {
    private final HwySignLogService logservice;
    private final CodeService codeservice;
    private final PageModel pageModel;
    private TopTitle toptitle=new TopTitle();

    @Autowired
    public HwySignLogController(HwySignLogService logservice, CodeService codeservice, PageModel pageModel) {
        this.logservice = logservice;
        this.codeservice = codeservice;
        this.pageModel = pageModel;
    }

    /* list */
    @RequestMapping("/signaturelog/list")
    public String listView(Model model, @ModelAttribute("pssSearch") HwySignLogSearchForm psssearchForm){
        pageModel.initPageAndSize();
        pageModel.setSIZE(15);
        psssearchForm.setSearchTypeList(codeservice.findgroup(8));/*Filter Search Type List*/
        /*Paging Data */
        Page<HwySignLogListForm> listFormpage=logservice.findAllSignLog(psssearchForm, PageRequest.of(pageModel.getPAGE(), pageModel.getSIZE(), Sort.by("InputDate").descending()));
        model.addAttribute("results",listFormpage);
        model.addAttribute("filterForm", psssearchForm);
        toptitle.setTitleName("HWY Signature LOG");
        toptitle.setTitleLink("#");
        model.addAttribute("topTitle",toptitle);return "HwyLogSignature/list";
    }

    /**
     * Input & Edit View
     */
    @RequestMapping("/signaturelog/active/")
    public String addView(Model model){
        HwySignLogListForm pssView=new HwySignLogListForm();
        model.addAttribute("pssView",pssView);
        toptitle.setTitleName("HWY Signature LOG");
        toptitle.setTitleLink("/signaturelog/list");
        model.addAttribute("topTitle",toptitle);
        return "HwyLogSignature/active";
    }

    @RequestMapping("/signaturelog/active/{logID}")
    public String activeView(@PathVariable("logID") long logID, Model model){
        HwySignLogForm pssView=logservice.findone(logID);
        model.addAttribute("pssView",pssView);
        toptitle.setTitleName("HWY Signature LOG");
        toptitle.setTitleLink("/signaturelog/list");
        model.addAttribute("topTitle",toptitle);
        return "HwyLogSignature/active";
    }

    /**
     * Write  View
     */
    @RequestMapping(value = "/signaturelog/write", method = RequestMethod.POST)
    public String writeEnd(@ModelAttribute("pssView") HwySignLogForm pssinputForm){
        if (pssinputForm.getExpirationDate().equals("")){
            pssinputForm.setExpirationDate(pssinputForm.getInputDate());
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long logID=0;
        if (auth != null && (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("SI_A")))) {
            logID=logservice.add(pssinputForm);
        }
        if (logID==0){
            return "redirect:/signaturelog/active";
        } else {
            return "redirect:/signaturelog/active/"+logID;
        }

    }
    /**
     * Edit End View
     */
    @RequestMapping(value = "/signaturelog/edit", method = RequestMethod.POST)
    public String editEnd(@ModelAttribute("pssView") HwySignLogForm psseditForm){
        if (psseditForm.getExpirationDate().equals("")){
            psseditForm.setExpirationDate(psseditForm.getInputDate());
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("SI_E")))) {
            logservice.edit(psseditForm);
        }
        return "redirect:/signaturelog/list";
    }

    /**
     * Delete End View
     */
    @RequestMapping("/signaturelog/delete/{logID}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteEnd(@PathVariable("logID") long logID){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("SI_D")))) {
            logservice.findDeleteone(logID);
        }
    }






}
