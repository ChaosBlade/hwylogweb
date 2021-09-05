package gov.hawaii.hidot.hwymail.ManageUsers;

import gov.hawaii.hidot.hwymail.Common.PageModel;
import gov.hawaii.hidot.hwymail.Common.TopTitle;

import gov.hawaii.hidot.hwymail.ManageCompany.Form.CompanyGroupForm;
import gov.hawaii.hidot.hwymail.ManageCompany.Service.CompanyService;
import gov.hawaii.hidot.hwymail.ManageUsers.Form.UserForm;
import gov.hawaii.hidot.hwymail.ManageUsers.Form.UserSearchForm;
import gov.hawaii.hidot.hwymail.ManageUsers.Form.UserSecurityForm;
import gov.hawaii.hidot.hwymail.ManageUsers.Service.UserService;
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
public class UserController {
    private final UserService userservice;
    private final CompanyService companyservice;
    private final CodeService codeservice;
    private final PageModel pageModel;
    private TopTitle toptitle=new TopTitle();
    @Autowired
    public UserController(UserService userservice,  CompanyService companyservice, CodeService codeservice, PageModel pageModel) {
        this.userservice = userservice;
        this.companyservice = companyservice;
        this.codeservice = codeservice;
        this.pageModel = pageModel;
    }

    /* list */
    @RequestMapping("/members/list")
    public String listView(Model model,@ModelAttribute("pssSearch") UserSearchForm psssearchForm){
        pageModel.initPageAndSize();
        pageModel.setSIZE(15);
        List<CompanyGroupForm> CompanyType = companyservice.findCompanygroupList("all");/*Company List*/

        /*Paging Data */
        Page<UserForm> listFormpage=userservice.findCodes(psssearchForm, PageRequest.of(pageModel.getPAGE(), pageModel.getSIZE(), Sort.by("lastUpdated").descending()));
        model.addAttribute("CompanyType",CompanyType);
        model.addAttribute("results",listFormpage);
        model.addAttribute("filterForm", psssearchForm);
        toptitle.setTitleName("Manage Users");
        toptitle.setTitleLink("#");
        model.addAttribute("topTitle",toptitle);
        return "Members/list";
    }


    /**
     * Active View
     */
    @RequestMapping("/members/active/")
    public String addView(Model model){
        UserForm pssView=new UserForm();
        pssView.setSecurityForm(new UserSecurityForm());

        List<CodeForm> Reportlevel = codeservice.findgroup(2);/*Report Level Type List*/
        List<CompanyGroupForm> CompanyType = companyservice.findCompanygroupList("all");/*Company List*/
        model.addAttribute("Reportlevel",Reportlevel);
        model.addAttribute("CompanyType",CompanyType);
        model.addAttribute("pssView",pssView);
        toptitle.setTitleName("Manage Users");
        toptitle.setTitleLink("/members/list");
        model.addAttribute("topTitle",toptitle);
        return "Members/active";
    }
    @RequestMapping("/members/active/{userID}")
    public String activeView(@PathVariable("userID") String UserID, Model model){
        UserForm pssView=userservice.findone(UserID);

        List<CodeForm> Reportlevel = codeservice.findgroup(2);/*Report Level Type List*/
        List<CompanyGroupForm> CompanyType = companyservice.findCompanygroupList("all");/*Company List*/

        model.addAttribute("Reportlevel",Reportlevel);
        model.addAttribute("CompanyType",CompanyType);
        model.addAttribute("pssView",pssView);
        toptitle.setTitleName("Manage Users");
        toptitle.setTitleLink("/members/list");
        model.addAttribute("topTitle",toptitle);
        return "Members/active";
    }
    /**
     * Check Duplicate id
     */
    @RequestMapping(value = "/members/idcheck/{userID}")
    @ResponseBody
    public boolean idcheck(@PathVariable("userID") String UserID){
        return userservice.idDuplicatecheck(UserID);
    }


    /**
     * Write  View
     */
    @RequestMapping(value = "/members/write", method = RequestMethod.POST)
    public String writeEnd(@ModelAttribute("pssView") UserForm pssinputForm){
        userservice.add(pssinputForm);
        return "redirect:/members/list";
    }
    /**
     * Edit End View
     */
    @RequestMapping(value = "/members/edit", method = RequestMethod.POST)
    public String editEnd(@ModelAttribute("pssView") UserForm psseditForm){
        userservice.edit(psseditForm);
        return "redirect:/members/list";
    }

    /**
     * Delete End View
     */
    @RequestMapping("/members/delete/{userID}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteEnd(@PathVariable("userID") String userID){
        userservice.findDeleteone(userID);
    }

    /**
     * User List
     */
    @RequestMapping(value = "/members/membersList")
    @ResponseBody
    public List<String> membersjsonList(){
        List<String> rs=userservice.findgroupOnlyValue();
        return rs;
    }
}
