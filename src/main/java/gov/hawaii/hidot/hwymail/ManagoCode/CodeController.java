package gov.hawaii.hidot.hwymail.ManagoCode;
import gov.hawaii.hidot.hwymail.Common.PageModel;
import gov.hawaii.hidot.hwymail.Common.TopTitle;
import gov.hawaii.hidot.hwymail.ManagoCode.Form.CodeForm;
import gov.hawaii.hidot.hwymail.ManagoCode.Form.CodeSearchForm;
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
public class CodeController {

    private final CodeService codeservice;
    private final PageModel pageModel;
    private TopTitle toptitle=new TopTitle();

    @Autowired
    public CodeController(CodeService codeservice, PageModel pageModel) {
        this.codeservice = codeservice;
        this.pageModel = pageModel;
    }

    /**
     * List View
     */
    @RequestMapping("/code/list")
    public String listView(Model model,@ModelAttribute("pssSearch") CodeSearchForm psssearchForm){
        pageModel.initPageAndSize();
        pageModel.setSIZE(15);
        List<CodeForm> codepart = codeservice.findgroup(0);/*Filter Group Code List*/
        /*Paging Search Data*/
        Page<CodeForm> listFormpage=codeservice.findCodes(psssearchForm,PageRequest.of(pageModel.getPAGE(), pageModel.getSIZE(), Sort.by("lastUpdated").descending()));


        toptitle.setTitleName("Manage Codes");
        toptitle.setTitleLink("#");
        model.addAttribute("topTitle",toptitle);
        model.addAttribute("results",listFormpage);
        model.addAttribute("codepart",codepart);
        model.addAttribute("filterForm", psssearchForm);
        return "Code/list";
    }
    /**
     * Active View
     */
    @RequestMapping("/code/active/")
    public String addView(Model model){
        /*Group Code List*/
        CodeForm pssView=new CodeForm();
        List<CodeForm> codepart = codeservice.findgroup(0);

        model.addAttribute("codepart",codepart);
        model.addAttribute("pssView",pssView);
        toptitle.setTitleName("Manage Codes");
        toptitle.setTitleLink("/code/list");
        model.addAttribute("topTitle",toptitle);
        return "Code/active";
    }
    @RequestMapping("/code/active/{codeSeq}")
    public String activeView(@PathVariable("codeSeq") long codeSeq, Model model){
        CodeForm pssView=codeservice.findone(codeSeq);
        List<CodeForm> codepart = codeservice.findgroup(0);
        model.addAttribute("pssView",pssView);
        model.addAttribute("codepart",codepart);
        toptitle.setTitleName("Manage Codes");
        toptitle.setTitleLink("/code/list");
        model.addAttribute("topTitle",toptitle);
        return "Code/active";
    }
    /**
     * Write  View
     */
    @RequestMapping(value = "/code/write", method = RequestMethod.POST)
    public String writeEnd(@ModelAttribute("pssView") CodeForm pssinputForm){
        codeservice.add(pssinputForm);
        return "redirect:/code/list";
    }
    /**
     * Edit End View
     */
    @RequestMapping(value = "/code/edit", method = RequestMethod.POST)
    public String editEnd(@ModelAttribute("pssView") CodeForm psseditForm){
        codeservice.edit(psseditForm);
        return "redirect:/code/list";
    }
    /**
     * Delete End View
     */
    @RequestMapping("/code/delete/{codeSeq}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteEnd(@PathVariable("codeSeq") long codeSeq){
        codeservice.findDeleteone(codeSeq);
    }

    /**
     * Code List
     */
    @RequestMapping(value = "/code/codeList")
    @ResponseBody
    public List<String> projectHistory(@RequestParam("code1") long code1,@RequestParam("codepart") String codepart){
        List<String> rs=codeservice.findgroupOnlyValue(code1,codepart);
        return rs;
    }
}
