package gov.hawaii.hidot.hwymail.HwyeLog.Service;

import gov.hawaii.hidot.hwymail.Report.Form.HwySignReportForm;
import gov.hawaii.hidot.hwymail.HwyeLog.Entity.TBL20HWYEMAILLOG;
import gov.hawaii.hidot.hwymail.HwyeLog.Form.*;
import gov.hawaii.hidot.hwymail.HwyeLog.Form.HwyeMailLogForm;
import gov.hawaii.hidot.hwymail.HwyeLog.Form.HwyeMailLogSearchForm;
import gov.hawaii.hidot.hwymail.HwyeLog.Repository.HwyeMailLogJpaRepository;
import gov.hawaii.hidot.hwymail.HwyeLog.Repository.HwyeMailLogSearchRepository;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class HwyeLogService {
    private final HwyeMailLogJpaRepository controlRepository;
    private final HwyeMailLogSearchRepository searchRepository;

    @Autowired
    public HwyeLogService(HwyeMailLogJpaRepository controlRepository,  HwyeMailLogSearchRepository searchRepository) {
        this.controlRepository = controlRepository;
        this.searchRepository = searchRepository;
    }
    /** List Data*/
    public Page<HwyeMailLogForm> findAllMailLog(HwyeMailLogSearchForm psssearchForm, Pageable pageableRequest){

        Page<TBL20HWYEMAILLOG> psslistpage;

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
        Page<HwyeMailLogForm> pssFinalList=psslistpage.map(new Function<TBL20HWYEMAILLOG, HwyeMailLogForm>() {
            @Override
            public HwyeMailLogForm apply(TBL20HWYEMAILLOG pssList) {
                HwyeMailLogForm replaceform=new ModelMapper().map(pssList, HwyeMailLogForm.class);
                return replaceform;
            }
        });
        return pssFinalList;
    }


    /** List Data*/
    public List<HwyelogReportForm> findAllMailLogReport(String Searchname){
        ModelMapper modelMapper=new ModelMapper();
        Date currentdate = new Date();

        List<HwyelogReportForm> pssFinalList = searchRepository.findAllbyLetterno(Searchname,currentdate)
                .stream()
                .map(m ->modelMapper.map(m, HwyelogReportForm.class))
                .collect(Collectors.toList());
        return pssFinalList;
    }



    /**
     * Add Data
     */
    public Long add(HwyeMailLogForm pssinputForm){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username="";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        TBL20HWYEMAILLOG pssView=new TBL20HWYEMAILLOG();
        Date date = new Date(System.currentTimeMillis());
        BeanUtils.copyProperties(pssinputForm, pssView);
        pssView.setLastUpdated(date);
        pssView.setUserNameID(username);
        controlRepository.save(pssView);


        return pssView.getLogID();
    }

    /** Edit Data */
    public Long edit(HwyeMailLogForm pssinputForm){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username="";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        TBL20HWYEMAILLOG pssView=new TBL20HWYEMAILLOG();
        Date date = new Date(System.currentTimeMillis());
        BeanUtils.copyProperties(pssinputForm, pssView);
        pssView.setLastUpdated(date);
        pssView.setUserNameID(username);
        controlRepository.update(pssView);
        return pssView.getLogID();
    }



    /** Search One Data */
    public HwyeMailLogForm findone(long logID){
        ModelMapper modelMapper=new ModelMapper();
        HwyeMailLogForm PssView = modelMapper.map(controlRepository.findById(logID), HwyeMailLogForm.class);
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
        HwyeMailLogForm PssView = modelMapper.map(controlRepository.findById(logID), HwyeMailLogForm.class);
        if (PssView.getUserNameID().equals(username)  || (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))){
            controlRepository.deleteById(logID);
        }
    }


    public List<HwySignReportForm> findReportselected(Long[] logIDList){
        ModelMapper modelMapper=new ModelMapper();
        List<HwySignReportForm> pssFinalList = searchRepository.findAllbyselected(logIDList)
                .stream()
                .map(m ->modelMapper.map(m, HwySignReportForm.class))
                .collect(Collectors.toList());
        return pssFinalList;
    }

    public List<Long> findallhwyemaillogList(String searchsdate,String searchedate,long searchType,String searchName){
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Date getSearchsdate=null;
        Date getSearchedate=null;
        if (!searchsdate.equals("")){
            try {
                getSearchsdate = format.parse(searchsdate);
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
        if (!searchedate.equals("")){
            try {
                getSearchedate = format.parse(searchedate);
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

        ModelMapper modelMapper=new ModelMapper();
        List<Long> pssFinalList=new ArrayList<Long>();
        if (searchType==1) {
            //  Letter Number
            pssFinalList = searchRepository.findAllallhwyeByLetterNumber(searchName,getSearchsdate,getSearchedate);
        } else if (searchType==2) {
            //  Originator
            pssFinalList = searchRepository.findAllallhwyeByOriginator(searchName,getSearchsdate,getSearchedate);

        } else {
            //  Subject
            pssFinalList = searchRepository.findAllallhwyeBySubject(searchName,getSearchsdate,getSearchedate);
        }

         pssFinalList = pssFinalList
                .stream()
                .map(m ->modelMapper.map(m, Long.class))
                .collect(Collectors.toList());
        return pssFinalList;
    }

}
