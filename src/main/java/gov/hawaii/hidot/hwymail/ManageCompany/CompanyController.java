package gov.hawaii.hidot.hwymail.ManageCompany;

import gov.hawaii.hidot.hwymail.Common.PageModel;
import gov.hawaii.hidot.hwymail.Common.TopTitle;
import gov.hawaii.hidot.hwymail.ManageCompany.Form.CompanyForm;
import gov.hawaii.hidot.hwymail.ManageCompany.Form.CompanyGroupForm;
import gov.hawaii.hidot.hwymail.ManageCompany.Form.CompanySearchForm;
import gov.hawaii.hidot.hwymail.ManageCompany.Service.CompanyService;
import gov.hawaii.hidot.hwymail.ManagoCode.Form.CodeForm;
import gov.hawaii.hidot.hwymail.ManagoCode.Service.CodeService;
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
public class CompanyController {
    private final CompanyService companyservice;
    private final CodeService codeservice;
    private final PageModel pageModel;
    private TopTitle toptitle=new TopTitle();

    @Autowired
    public CompanyController(CompanyService companyservice, CodeService codeservice, PageModel pageModel) {
        this.companyservice = companyservice;
        this.codeservice = codeservice;
        this.pageModel = pageModel;
    }

    /* list */
    @RequestMapping("/company/list")
    public String listView(Model model,@ModelAttribute("pssSearch") CompanySearchForm psssearchForm){
        pageModel.initPageAndSize();
        pageModel.setSIZE(15);

        List<CodeForm> islandGroup = codeservice.findgroup(1);/*Filter Island List*/
        List<CodeForm> companyType = codeservice.findgroup(5);/*Filter Company Type List*/

        /*Paging Data */
        Page<CompanyForm> listFormpage=companyservice.findCodes(psssearchForm, PageRequest.of(pageModel.getPAGE(), pageModel.getSIZE(), Sort.by("lastUpdated").descending()));
        model.addAttribute("islandGroup",islandGroup);
        model.addAttribute("companyType",companyType);
        model.addAttribute("results",listFormpage);
        model.addAttribute("filterForm", psssearchForm);

        toptitle.setTitleName("Manage Companies");
        toptitle.setTitleLink("#");
        model.addAttribute("topTitle",toptitle);return "Company/list";
    }

    /**
     * Active View
     */
    @RequestMapping("/company/active/")
    public String addView(Model model){
        CompanyForm pssView=new CompanyForm();

        List<CodeForm> islandGroup = codeservice.findgroup(1);/*Island List*/
        List<CodeForm> companyType = codeservice.findgroup(5);/*Company Type List*/
        model.addAttribute("islandGroup",islandGroup);
        model.addAttribute("companyType",companyType);
        model.addAttribute("pssView",pssView);

        toptitle.setTitleName("Manage Companies");
        toptitle.setTitleLink("/company/list");

        model.addAttribute("topTitle",toptitle);
        return "Company/active";
    }
    @RequestMapping("/company/active/{CompanyID}")
    public String activeView(@PathVariable("CompanyID") long CompanyID, Model model){
        CompanyForm pssView=companyservice.findone(CompanyID);

        List<CodeForm> islandGroup = codeservice.findgroup(1);/*Island List*/
        List<CodeForm> companyType = codeservice.findgroup(5);/*Company Type List*/
        model.addAttribute("islandGroup",islandGroup);
        model.addAttribute("companyType",companyType);
        model.addAttribute("pssView",pssView);
        toptitle.setTitleName("Manage Companies");
        toptitle.setTitleLink("/company/list");
        model.addAttribute("topTitle",toptitle);
        return "Company/active";
    }

    /**
     * Write  View
     */
    @RequestMapping(value = "/company/write", method = RequestMethod.POST)
    public String writeEnd(@ModelAttribute("pssView") CompanyForm pssinputForm){
        companyservice.add(pssinputForm);
        return "redirect:/company/list";
    }
    /**
     * Edit End View
     */
    @RequestMapping(value = "/company/edit", method = RequestMethod.POST)
    public String editEnd(@ModelAttribute("pssView") CompanyForm psseditForm){
        companyservice.edit(psseditForm);
        return "redirect:/company/list";
    }

    /**
     * Delete End View
     */
    @RequestMapping("/company/delete/{CompanyID}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteEnd(@PathVariable("CompanyID") long CompanyID){
        companyservice.findDeleteone(CompanyID);
    }


    /**
     * Company List
     */
    @RequestMapping(value = "/company/companyList")
    @ResponseBody
    public List<CompanyGroupForm> projectHistory(@RequestParam("code1") String code1){
        List<CompanyGroupForm> rs=companyservice.findCompanygroupList(code1);
        return rs;
    }

}
