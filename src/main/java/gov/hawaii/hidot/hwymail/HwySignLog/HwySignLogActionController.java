package gov.hawaii.hidot.hwymail.HwySignLog;
import gov.hawaii.hidot.hwymail.HwySignLog.Form.HwySignLogActionForm;
import gov.hawaii.hidot.hwymail.HwySignLog.Service.HwySignLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HwySignLogActionController {
    private final HwySignLogService logservice;
    @Autowired
    public HwySignLogActionController(HwySignLogService logservice) {
        this.logservice = logservice;
    }
    /**
     * Action Taken List
     */
    @RequestMapping(value = "/signaturelog/actiontakenList/{logID}",  method = RequestMethod.GET)
    @ResponseBody
    public List<HwySignLogActionForm> actiontakenListEnd(@PathVariable("logID") long logID){
        return logservice.actionTakenList(logID);
    }

    /**
     * Action Taken Write & Edit
     */
    @RequestMapping(value = "/signaturelog/actiontakenwrite/{logID}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String actiontakenwriteEnd(@PathVariable("logID") long logID,
                                    @RequestParam("logActionId") long logActionId,
                                    @RequestParam("ActionType") String ActionType,
                                    @RequestParam("formDate") String formDate,
                                    @RequestParam("actionTaken") String actionTaken,
                                    @RequestParam("Delete") boolean Delete){

        HwySignLogActionForm inputForm=new HwySignLogActionForm();

        inputForm.setLogID(logID);
        inputForm.setLogActionId(logActionId);
        inputForm.setActionType(ActionType);
        inputForm.setFormDate(formDate);
        inputForm.setActionTaken(actionTaken);
        inputForm.setDeleted(Delete);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String rs="";
        if (auth != null && (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("SI_A")) || auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("SI_E")))) {
            logservice.addactiontaken(inputForm);
            rs="01";
        }

        return rs;
    }

}
