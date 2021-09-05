package gov.hawaii.hidot.hwymail.HwyLog;
import gov.hawaii.hidot.hwymail.HwyLog.Form.HwyMailLogActionForm;
import gov.hawaii.hidot.hwymail.HwyLog.Service.HwyLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HwyLogActionController {
    private final HwyLogService logservice;
    @Autowired
    public HwyLogActionController(HwyLogService logservice) {
        this.logservice = logservice;
    }
    /**
     * Action Taken List
     */
    @RequestMapping(value = "/hwymaillog/actiontakenList/{logID}",  method = RequestMethod.GET)
    @ResponseBody
    public List<HwyMailLogActionForm> actiontakenListEnd(@PathVariable("logID") long logID){
        return logservice.actionTakenList(logID);
    }

    /**
     * Action Taken Write & Edit
     */
    @RequestMapping(value = "/hwymaillog/actiontakenwrite/{logID}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String actiontakenwriteEnd(@PathVariable("logID") long logID,
                                  @RequestParam("logActionId") long logActionId,
                                  @RequestParam("ActionType") String ActionType,
                                  @RequestParam("formDate") String formDate,
                                  @RequestParam("actionTaken") String actionTaken,
                                  @RequestParam("Delete") boolean Delete){

        HwyMailLogActionForm inputForm=new HwyMailLogActionForm();

        inputForm.setLogID(logID);
        inputForm.setLogActionId(logActionId);
        inputForm.setActionType(ActionType);
        inputForm.setFormDate(formDate);
        inputForm.setActionTaken(actionTaken);
        inputForm.setDeleted(Delete);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String rs="";
            if (auth != null && (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("HW_A")) || auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("HW_E")))) {
               logservice.addactiontaken(inputForm);
                rs="01";
            }

        return rs;
    }

}
