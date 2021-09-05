package gov.hawaii.hidot.hwymail.ManageReport.Service;

import gov.hawaii.hidot.hwymail.ManageReport.Entity.TBL20USERREPORTS;
import gov.hawaii.hidot.hwymail.ManageReport.Form.ReportForm;
import gov.hawaii.hidot.hwymail.ManageReport.Form.ReportGroupForm;
import gov.hawaii.hidot.hwymail.ManageReport.Form.ReportSearchForm;
import gov.hawaii.hidot.hwymail.ManagoCode.Repository.CodeJpaRepository;
import gov.hawaii.hidot.hwymail.ManageReport.Repository.ReportJpaRepository;
import gov.hawaii.hidot.hwymail.ManageReport.Repository.ReportRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportService {

    private final ReportJpaRepository controlRepository;
    private final CodeJpaRepository coderepository;
    private final ReportRepository SearchRepository;
    @Autowired
    public ReportService(ReportJpaRepository controlRepository, CodeJpaRepository coderepository, ReportRepository SearchRepository) {
        this.controlRepository = controlRepository;
        this.coderepository = coderepository;
        this.SearchRepository = SearchRepository;
    }



    /** List Data*/
    public Page<ReportForm> findCodes(ReportSearchForm psssearchForm, Pageable pageableRequest){

        Page<TBL20USERREPORTS> psslistpage;
        if (psssearchForm.getSearchReportGroup()!=-1 && psssearchForm.getSearchReportType()==-1 && psssearchForm.getSearchName().equals("")){
            psslistpage=SearchRepository.findAllByReportGroup(psssearchForm.getSearchReportGroup(),pageableRequest);
        } else if (psssearchForm.getSearchReportGroup()==-1 && psssearchForm.getSearchReportType()!=-1 && psssearchForm.getSearchName().equals("")){
            psslistpage=SearchRepository.findAllByReportType(psssearchForm.getSearchReportType(),pageableRequest);
        } else if (psssearchForm.getSearchReportGroup()==-1 && psssearchForm.getSearchReportType()==-1 && !psssearchForm.getSearchName().equals("")){
            psslistpage=SearchRepository.findAllByReportNameContainingIgnoreCase(psssearchForm.getSearchName(),pageableRequest);
        } else if (psssearchForm.getSearchReportGroup()!=-1 && psssearchForm.getSearchReportType()==-1 && !psssearchForm.getSearchName().equals("")){
            psslistpage=SearchRepository.findAllByReportGroupAndReportNameContainingIgnoreCase(psssearchForm.getSearchReportGroup(),psssearchForm.getSearchName(),pageableRequest);
        } else if (psssearchForm.getSearchReportGroup()==-1 && psssearchForm.getSearchReportType()!=-1 && !psssearchForm.getSearchName().equals("")){
            psslistpage=SearchRepository.findAllByReportTypeAndReportNameContainingIgnoreCase(psssearchForm.getSearchReportType(),psssearchForm.getSearchName(),pageableRequest);
        } else if (psssearchForm.getSearchReportGroup()!=-1 && psssearchForm.getSearchReportType()!=-1 && !psssearchForm.getSearchName().equals("")){
            psslistpage=SearchRepository.findAllByReportGroupAndReportTypeAndReportNameContainingIgnoreCase(psssearchForm.getSearchReportGroup(),psssearchForm.getSearchReportType(),psssearchForm.getSearchName(),pageableRequest);
        } else {
            psslistpage=SearchRepository.findAll(pageableRequest);
        }

        Page<ReportForm> pssFinalList=psslistpage.map(new Function<TBL20USERREPORTS, ReportForm>() {
            @Override
            public ReportForm apply(TBL20USERREPORTS pssList) {
                ReportForm replaceform=new ModelMapper().map(pssList, ReportForm.class);
                replaceform.setReportGroupName(coderepository.findCodename(3,pssList.getReportGroup()));
                replaceform.setReportTypeName(coderepository.findCodename(4,pssList.getReportType()));
                return replaceform;
            }
        });
        return pssFinalList;
    }

    /**
     * Add Data
     */
    public Long add(ReportForm pssinputForm){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username="";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        TBL20USERREPORTS pssView=new TBL20USERREPORTS();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        BeanUtils.copyProperties(pssinputForm, pssView);
        pssView.setLastUpdated(date);
        pssView.setUserNameID(username);
        controlRepository.save(pssView);
        return pssView.getReportId();
    }

    /** Edit Data */
    public Long edit(ReportForm pssinputForm){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username="";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        TBL20USERREPORTS pssView=new TBL20USERREPORTS();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        BeanUtils.copyProperties(pssinputForm, pssView);
        pssView.setLastUpdated(date);
        pssView.setUserNameID(username);
        controlRepository.update(pssView);
        return pssView.getReportId();
    }



    /** Search One Data */
    public ReportForm findone(long reportId){
        ModelMapper modelMapper=new ModelMapper();
        ReportForm PssView = modelMapper.map(controlRepository.findById(reportId), ReportForm.class);
        return PssView;
    }

    /** Delete Data */
    public void findDeleteone(long reportId){
        controlRepository.deleteById(reportId);
    }


    /** Report GroupList  */
    public List<ReportGroupForm> findreportgroupList(long code1){
       ModelMapper modelMapper=new ModelMapper();
       List<Long> ReportType = controlRepository.findByDistinctReportType(code1);

       ArrayList<ReportGroupForm> arraylist = new ArrayList<ReportGroupForm>();
        ReportType.stream()
                .forEach(m->{
                    List<TBL20USERREPORTS> psslistpage;
                    psslistpage=SearchRepository.findAllByReportGroupAndReportType(code1,m.longValue());
                    List<ReportForm> UserGrouppart = psslistpage.stream()
                            .map(s ->modelMapper.map(s, ReportForm.class))
                            .collect(Collectors.toList());
                    arraylist.add(new ReportGroupForm(m.longValue(), coderepository.findCodename(4,m.longValue()), UserGrouppart));
                });
        return arraylist;
    }





}
