package gov.hawaii.hidot.hwymail.HwyLog.Service;

import gov.hawaii.hidot.hwymail.HwyLog.Entity.TBL20MAILLOG;
import gov.hawaii.hidot.hwymail.HwyLog.Entity.TBL20MAILLOGACTION;
import gov.hawaii.hidot.hwymail.HwyLog.Form.HwyMailLogActionForm;
import gov.hawaii.hidot.hwymail.HwyLog.Repository.HwyMailLogActionJpaRepository;
import gov.hawaii.hidot.hwymail.HwyLog.Repository.HwyMailLogJpaRepository;
import gov.hawaii.hidot.hwymail.HwyLog.Repository.HwyMailLogSearchRepository;
import gov.hawaii.hidot.hwymail.Report.Form.HwySignReportForm;
import gov.hawaii.hidot.hwymail.HwyLog.Form.HwyMailLogForm;
import gov.hawaii.hidot.hwymail.HwyLog.Form.HwyMailLogListForm;
import gov.hawaii.hidot.hwymail.HwyLog.Form.HwyMailLogSearchForm;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class HwyLogService {
    private final HwyMailLogJpaRepository controlRepository;
    private final HwyMailLogActionJpaRepository actionRepository;
    private final HwyMailLogSearchRepository searchRepository;

    @Autowired
    public HwyLogService(HwyMailLogJpaRepository controlRepository, HwyMailLogActionJpaRepository actionRepository, HwyMailLogSearchRepository searchRepository) {
        this.controlRepository = controlRepository;
        this.actionRepository = actionRepository;
        this.searchRepository = searchRepository;
    }
    /** List Data*/
    public Page<HwyMailLogListForm> findAllMailLog(HwyMailLogSearchForm psssearchForm, Pageable pageableRequest){

        Page<TBL20MAILLOG> psslistpage;

        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Date getSearchsdate=null;
        Date getSearchedate=null;
        if (!psssearchForm.getSearchsdate().equals("")){
            try {
                getSearchsdate = format.parse(psssearchForm.getSearchsdate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                getSearchsdate = format.parse("01/01/1900");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!psssearchForm.getSearchedate().equals("")){
            try {
                getSearchedate = format.parse(psssearchForm.getSearchedate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                getSearchedate = format.parse("12/31/2500");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (psssearchForm.getSearchType()==1) {
            //  Letter Number
            psslistpage = searchRepository.findAllByLetterNumber(psssearchForm.getSearchName(),getSearchsdate,getSearchedate, pageableRequest);
        } else if (psssearchForm.getSearchType()==2) {
            //  Originator
            psslistpage = searchRepository.findAllByOriginator(psssearchForm.getSearchName(),getSearchsdate,getSearchedate, pageableRequest);

        } else {
            //  Subject
            psslistpage = searchRepository.findAllBySubject(psssearchForm.getSearchName(),getSearchsdate,getSearchedate, pageableRequest);
        }

        Date date = new Date(System.currentTimeMillis());
        ModelMapper modelMapper=new ModelMapper();
        Page<HwyMailLogListForm> pssFinalList=psslistpage.map(new Function<TBL20MAILLOG, HwyMailLogListForm>() {
            @Override
            public HwyMailLogListForm apply(TBL20MAILLOG pssList) {
                HwyMailLogListForm replaceform=new ModelMapper().map(pssList, HwyMailLogListForm.class);
                replaceform.setNumberofDay(TimeUnit.DAYS.convert(pssList.getExpirationDate().getTime()-date.getTime(), TimeUnit.MILLISECONDS));
                editactiontaken(pssList.getLogID());
                if (actionRepository.exitBylogID(pssList.getLogID())>0) {
                    replaceform.setCurrentAction(pssList.getCurrentAction());
                } else {
                    replaceform.setCurrentAction(pssList.getFinalAction());
                }

                return replaceform;
            }
        });
        return pssFinalList;
    }


    /** List Data*/
    public List<HwySignReportForm> findAllMailLogReport(String Searchname){
        ModelMapper modelMapper=new ModelMapper();
        Date currentdate = new Date();



        List<HwySignReportForm> pssFinalList = searchRepository.findAllbyLetterno(Searchname,currentdate)
                .stream()
                .map(m ->modelMapper.map(m, HwySignReportForm.class))
                .collect(Collectors.toList());
        return pssFinalList;
    }

    /** List Data*/
    public List<HwySignReportForm> findReportselected(Long[] logIDList){
        ModelMapper modelMapper=new ModelMapper();
        List<HwySignReportForm> pssFinalList = searchRepository.findAllbyselected(logIDList)
                .stream()
                .map(m ->modelMapper.map(m, HwySignReportForm.class))
                .collect(Collectors.toList());
        return pssFinalList;
    }




    /**
     * Add Data
     */
    public Long add(HwyMailLogForm pssinputForm){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username="";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        TBL20MAILLOG pssView=new TBL20MAILLOG();
        Date date = new Date(System.currentTimeMillis());
        BeanUtils.copyProperties(pssinputForm, pssView);

        pssView.setLogID(controlRepository.findMaxlogID()+1);
        pssView.setLastUpdated(date);
        pssView.setUserNameID(username);
        controlRepository.save(pssView);


        return pssView.getLogID();
    }

    /** Edit Data */
    public Long edit(HwyMailLogForm pssinputForm){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username="";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        TBL20MAILLOG pssView=new TBL20MAILLOG();
        Date date = new Date(System.currentTimeMillis());
        BeanUtils.copyProperties(pssinputForm, pssView);
        pssView.setLastUpdated(date);
        pssView.setUserNameID(username);
        controlRepository.update(pssView);
        return pssView.getLogID();
    }



    /** Search One Data */
    public HwyMailLogForm findone(long logID){
        ModelMapper modelMapper=new ModelMapper();
        HwyMailLogForm PssView = modelMapper.map(controlRepository.findById(logID), HwyMailLogForm.class);
        return PssView;
    }




    /** Delete Data */
    public void findDeleteone(long logID){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        ModelMapper modelMapper = new ModelMapper();
        HwyMailLogForm PssView = modelMapper.map(controlRepository.findById(logID), HwyMailLogForm.class);
        if (PssView.getUserNameID().equals(username)  || (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))){
            controlRepository.deleteById(logID);
            actionRepository.deleteBylogId(logID);
        }
    }



    /**
     * Action Taken List
     */
    public List<HwyMailLogActionForm> actionTakenList(long logID){
        ModelMapper modelMapper=new ModelMapper();
        List<HwyMailLogActionForm> projectroute = actionRepository.findactionTakenbylogID(logID)
                .stream()
                .map(m ->modelMapper.map(m, HwyMailLogActionForm.class))
                .collect(Collectors.toList());
        return projectroute;
    }
    /**
     * Action Taken input & Edit
     */
    public void addactiontaken(HwyMailLogActionForm inputForm){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username="";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Date date = new Date(System.currentTimeMillis());
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        TBL20MAILLOGACTION newpssView=new TBL20MAILLOGACTION();


        if (actionRepository.exitById(inputForm.getLogActionId())>0) {
            BeanUtils.copyProperties(inputForm, newpssView);
            newpssView.setUserNameID(username);
            newpssView.setLastUpdated(date);
            try {
                newpssView.setFormDate(format.parse(inputForm.getFormDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            actionRepository.update(newpssView);
            editactiontaken(newpssView.getLogID());
        } else {
            BeanUtils.copyProperties(inputForm, newpssView);
            newpssView.setUserNameID(username);
            newpssView.setLastUpdated(date);
            try {
                newpssView.setFormDate(format.parse(inputForm.getFormDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            actionRepository.save(newpssView);
            editactiontaken(newpssView.getLogID());
        }
    }
    public void editactiontaken(long logID) {
        if (actionRepository.exitBylogID(logID) > 0) {
            String actionTakenView = actionRepository.findactionTakenStringbylogID(logID)
                    .stream().map(Object::toString).collect(Collectors.joining("  "));
            controlRepository.saveCurrentAction(actionTakenView,logID);
        }
    }
}
