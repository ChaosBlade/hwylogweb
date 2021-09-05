package gov.hawaii.hidot.hwymail.ManageCompany.Service;

import gov.hawaii.hidot.hwymail.ManageCompany.Entity.TBL20USERCOMPANY;
import gov.hawaii.hidot.hwymail.ManageCompany.Form.CompanyForm;
import gov.hawaii.hidot.hwymail.ManageCompany.Form.CompanyGroupForm;
import gov.hawaii.hidot.hwymail.ManageCompany.Form.CompanySearchForm;
import gov.hawaii.hidot.hwymail.ManageCompany.Repository.CompanyJpaRepository;
import gov.hawaii.hidot.hwymail.ManageCompany.Repository.CompanyRepository;
import gov.hawaii.hidot.hwymail.ManagoCode.Repository.CodeJpaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompanyService {

    private final CompanyJpaRepository controlRepository;
    private final CompanyRepository searchRepository;
    private final CodeJpaRepository coderepository;

    @Autowired
    public CompanyService(CompanyJpaRepository controlRepository, CodeJpaRepository coderepository, CompanyRepository searchRepository) {
        this.controlRepository = controlRepository;
        this.coderepository = coderepository;
        this.searchRepository = searchRepository;
    }



    /** List Data*/
    public Page<CompanyForm> findCodes(CompanySearchForm psssearchForm, Pageable pageableRequest){

        Page<TBL20USERCOMPANY> psslistpage;
        if (psssearchForm.getSearchIsland()!=-1 && psssearchForm.getSearchType()==-1 && psssearchForm.getSearchName().equals("")){
            psslistpage=searchRepository.findAllByComIsland(psssearchForm.getSearchIsland(),pageableRequest);
        } else if (psssearchForm.getSearchIsland()==-1 && psssearchForm.getSearchType()!=-1 && psssearchForm.getSearchName().equals("")){
            psslistpage=searchRepository.findAllByComType(psssearchForm.getSearchType(),pageableRequest);
        } else if (psssearchForm.getSearchIsland()==-1 && psssearchForm.getSearchType()==-1 && !psssearchForm.getSearchName().equals("")){
            psslistpage=searchRepository.findAllByComNameContainingIgnoreCase(psssearchForm.getSearchName(),psssearchForm.getSearchName(),pageableRequest);
        } else if (psssearchForm.getSearchIsland()!=-1 && psssearchForm.getSearchType()==-1 && !psssearchForm.getSearchName().equals("")){
            psslistpage=searchRepository.findAllByComIslandAndComNameContainingIgnoreCase(psssearchForm.getSearchIsland(),psssearchForm.getSearchName(),psssearchForm.getSearchName(),pageableRequest);
        } else if (psssearchForm.getSearchIsland()==-1 && psssearchForm.getSearchType()!=-1 && !psssearchForm.getSearchName().equals("")){
            psslistpage=searchRepository.findAllByComTypeAndComNameContainingIgnoreCase(psssearchForm.getSearchType(),psssearchForm.getSearchName(),psssearchForm.getSearchName(),pageableRequest);
        } else if (psssearchForm.getSearchIsland()!=-1 && psssearchForm.getSearchType()!=-1 && !psssearchForm.getSearchName().equals("")){
            psslistpage=searchRepository.findAllByComIslandAndComTypeAndComNameContainingIgnoreCase(psssearchForm.getSearchIsland(),psssearchForm.getSearchType(),psssearchForm.getSearchName(),psssearchForm.getSearchName(),pageableRequest);
        } else {
            psslistpage=searchRepository.findAll(pageableRequest);
        }

        Page<CompanyForm> pssFinalList=psslistpage.map(new Function<TBL20USERCOMPANY, CompanyForm>() {
            @Override
            public CompanyForm apply(TBL20USERCOMPANY pssList) {
                CompanyForm replaceform=new ModelMapper().map(pssList, CompanyForm.class);
                replaceform.setComIslandname(coderepository.findCodename(1,pssList.getComIsland()));
                replaceform.setComTypename(coderepository.findCodename(5,pssList.getComType()));
                return replaceform;
            }
        });
        return pssFinalList;
    }

    /**
     * Add Data
     */
    public Long add(CompanyForm pssinputForm){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username="";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        TBL20USERCOMPANY pssView=new TBL20USERCOMPANY();
        Date date = new Date(System.currentTimeMillis());
        BeanUtils.copyProperties(pssinputForm, pssView);
        pssView.setLastUpdated(date);
        pssView.setUserNameID(username);
        controlRepository.save(pssView);
        return pssView.getCompanyID();
    }

    /** Edit Data */
    public Long edit(CompanyForm pssinputForm){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username="";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        TBL20USERCOMPANY pssView=new TBL20USERCOMPANY();
        Date date = new Date(System.currentTimeMillis());
        BeanUtils.copyProperties(pssinputForm, pssView);
        pssView.setLastUpdated(date);
        pssView.setUserNameID(username);
        controlRepository.update(pssView);
        return pssView.getCompanyID();
    }



    /** Search One Data */
    public CompanyForm findone(long CompanyID){
        ModelMapper modelMapper=new ModelMapper();
        CompanyForm PssView = modelMapper.map(controlRepository.findById(CompanyID), CompanyForm.class);
        return PssView;
    }


    /** Delete Data */
    public void findDeleteone(long CompanyID){
        controlRepository.deleteById(CompanyID);
    }


    /** Search CompanyGroup  */
    public List<CompanyGroupForm> findCompanygroupList(String code1){
        ModelMapper modelMapper=new ModelMapper();
        List<TBL20USERCOMPANY> psslistpage;

         psslistpage=searchRepository.findAll();

        List<CompanyGroupForm> CompanyGrouppart = psslistpage.stream()
                .map(m ->modelMapper.map(m, CompanyGroupForm.class))
                .collect(Collectors.toList());
        return CompanyGrouppart;
    }



}
