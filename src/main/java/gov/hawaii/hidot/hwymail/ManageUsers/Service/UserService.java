package gov.hawaii.hidot.hwymail.ManageUsers.Service;



import gov.hawaii.hidot.hwymail.ManageCompany.Repository.CompanyJpaRepository;
import gov.hawaii.hidot.hwymail.ManageUsers.Entity.TBL10USERNAME;
import gov.hawaii.hidot.hwymail.ManageUsers.Form.UserForm;
import gov.hawaii.hidot.hwymail.ManageUsers.Form.UserSearchForm;
import gov.hawaii.hidot.hwymail.ManageUsers.Form.UserSecurityForm;
import gov.hawaii.hidot.hwymail.ManageUsers.Repository.UserJpaRepository;
import gov.hawaii.hidot.hwymail.ManageUsers.Repository.UserRepository;
import gov.hawaii.hidot.hwymail.ManagoCode.Repository.CodeJpaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
@Transactional
public class UserService {
    private final UserJpaRepository controlRepository;
    private final UserRepository SearchRepository;
    private final CompanyJpaRepository companyRepository;
    private final CodeJpaRepository coderepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserJpaRepository controlRepository, CodeJpaRepository coderepository, UserRepository SearchRepository, CompanyJpaRepository companyRepository) {
        this.controlRepository = controlRepository;
        this.coderepository = coderepository;
        this.SearchRepository = SearchRepository;
        this.companyRepository = companyRepository;
    }



    /** List Data*/
    public Page<UserForm> findCodes(UserSearchForm psssearchForm, Pageable pageableRequest){

        Page<TBL10USERNAME> psslistpage;

        if (psssearchForm.getSearchComid()!=-1  && psssearchForm.getSearchName().equals("")){
            psslistpage=SearchRepository.findAllByComID(psssearchForm.getSearchComid(),pageableRequest);
        } else if (psssearchForm.getSearchComid()==-1  && !psssearchForm.getSearchName().equals("")){
            psslistpage=SearchRepository.findAllByFirstNameContainingIgnoreCase(psssearchForm.getSearchName(),psssearchForm.getSearchName(),psssearchForm.getSearchName(),pageableRequest);
       } else if (psssearchForm.getSearchComid()!=-1  && !psssearchForm.getSearchName().equals("")){
            psslistpage=SearchRepository.findAllByComIDAndFirstNameContainingIgnoreCase(psssearchForm.getSearchComid(),psssearchForm.getSearchName(),pageableRequest);
        } else {
            psslistpage=SearchRepository.findAll(pageableRequest);
        }

        Page<UserForm> pssFinalList=psslistpage.map(new Function<TBL10USERNAME, UserForm>() {
            @Override
            public UserForm apply(TBL10USERNAME pssList) {
                UserForm replaceform=new ModelMapper().map(pssList, UserForm.class);
                if (pssList.getComID()!=0){
                    replaceform.setBranchName(companyRepository.findById(pssList.getComID()).getComBranch());
                } else {
                    replaceform.setBranchName("-");
                }
                return replaceform;
            }
        });
        return pssFinalList;
    }


    /**
     * Add Data
     */
    public int add(UserForm pssinputForm){
        //Duplicate UserID
        int rs=0;
        if (!SearchRepository.existsById(pssinputForm.getUserID())) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username="";
            if (principal instanceof UserDetails) {
                username = ((UserDetails)principal).getUsername();
            } else {
                username = principal.toString();
            }
            TBL10USERNAME pssView = new TBL10USERNAME();
             Date date = new Date(System.currentTimeMillis());
            BeanUtils.copyProperties(pssinputForm, pssView);
            pssView.setPassword(passwordEncoder.encode(pssinputForm.getPassword()));
            /* Concat Permission*/
            concatPermission(pssinputForm, pssView);
            pssView.setLastUpdated(date);
            pssView.setUserNameID(username);
            controlRepository.save(pssView);
            rs=1;
        }
        return rs;
    }



    /** Edit Data */
    public String edit(UserForm pssinputForm){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username="";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        TBL10USERNAME pssView=new TBL10USERNAME();
        Date date = new Date(System.currentTimeMillis());
        BeanUtils.copyProperties(pssinputForm, pssView);
        if (pssinputForm.getOldPassword()!=""){
            pssView.setPassword(passwordEncoder.encode(pssinputForm.getOldPassword()));
        }

        pssView.setLastUpdated(date);
        pssView.setUserNameID(username);
        /* Concat Permission*/
        concatPermission(pssinputForm, pssView);
        controlRepository.update(pssView);
        return pssView.getUserID();
    }


    /** Check Duplicate id */
    public boolean idDuplicatecheck(String UserID){
        return SearchRepository.existsById(UserID);
    }

    /** Search One Data */
    public UserForm findone(String UserID){
        ModelMapper modelMapper=new ModelMapper();
        UserForm PssView = modelMapper.map(controlRepository.findById(UserID), UserForm.class);
        PssView.setSecurityForm(getUserSecurityForm(PssView));
        return PssView;
    }
    /** Delete Data */
    public void findDeleteone(String UserID){
        controlRepository.deleteById(UserID);
    }



    /* Concat Permission*/
    private void concatPermission(UserForm pssinputForm, TBL10USERNAME pssView) {
        String hwylogAuth="";
        if (pssinputForm.getSecurityForm().isHwylogAuthR()) hwylogAuth+="R";
        if (pssinputForm.getSecurityForm().isHwylogAuthA()) hwylogAuth+="A";
        if (pssinputForm.getSecurityForm().isHwylogAuthE()) hwylogAuth+="E";
        if (pssinputForm.getSecurityForm().isHwylogAuthD()) hwylogAuth+="D";
        pssView.setHwylogAuth(hwylogAuth);

        String signaturelogAuth="";
        if (pssinputForm.getSecurityForm().isSignaturelogAuthR()) signaturelogAuth+="R";
        if (pssinputForm.getSecurityForm().isSignaturelogAuthA()) signaturelogAuth+="A";
        if (pssinputForm.getSecurityForm().isSignaturelogAuthE()) signaturelogAuth+="E";
        if (pssinputForm.getSecurityForm().isSignaturelogAuthD()) signaturelogAuth+="D";
        pssView.setSignaturelogAuth(signaturelogAuth);

        String hwyelogAuth="";
        if (pssinputForm.getSecurityForm().isHwyelogAuthR()) hwyelogAuth+="R";
        if (pssinputForm.getSecurityForm().isHwyelogAuthA()) hwyelogAuth+="A";
        if (pssinputForm.getSecurityForm().isHwyelogAuthE()) hwyelogAuth+="E";
        if (pssinputForm.getSecurityForm().isHwyelogAuthD()) hwyelogAuth+="D";
        pssView.setHwyelogAuth(hwyelogAuth);




        String manageuserauth="";
        if (pssinputForm.getSecurityForm().isManageuserauthR()) manageuserauth+="R";
        if (pssinputForm.getSecurityForm().isManageuserauthA()) manageuserauth+="A";
        if (pssinputForm.getSecurityForm().isManageuserauthE()) manageuserauth+="E";
        if (pssinputForm.getSecurityForm().isManageuserauthD()) manageuserauth+="D";
        pssView.setManageuserauth(manageuserauth);

        String managecompanyauth="";
        if (pssinputForm.getSecurityForm().isManagecompanyauthR()) managecompanyauth+="R";
        if (pssinputForm.getSecurityForm().isManagecompanyauthA()) managecompanyauth+="A";
        if (pssinputForm.getSecurityForm().isManagecompanyauthE()) managecompanyauth+="E";
        if (pssinputForm.getSecurityForm().isManagecompanyauthD()) managecompanyauth+="D";
        pssView.setManagecompanyauth(managecompanyauth);


        String managereportauth="";
        if (pssinputForm.getSecurityForm().isManagereportauthR()) managereportauth+="R";
        if (pssinputForm.getSecurityForm().isManagereportauthA()) managereportauth+="A";
        if (pssinputForm.getSecurityForm().isManagereportauthE()) managereportauth+="E";
        if (pssinputForm.getSecurityForm().isManagereportauthD()) managereportauth+="D";
        pssView.setManagereportauth(managereportauth);

        String managecodeauth="";
        if (pssinputForm.getSecurityForm().isManagecodeauthR()) managecodeauth+="R";
        if (pssinputForm.getSecurityForm().isManagecodeauthA()) managecodeauth+="A";
        if (pssinputForm.getSecurityForm().isManagecodeauthE()) managecodeauth+="E";
        if (pssinputForm.getSecurityForm().isManagecodeauthD()) managecodeauth+="D";
        pssView.setManagecodeauth(managecodeauth);


    }
    /*Permission*/
    private UserSecurityForm getUserSecurityForm(UserForm PssView) {
        UserSecurityForm secutiryForm=new UserSecurityForm();
        if (PssView.getHwylogAuth().contains("R")) secutiryForm.setHwylogAuthR(true);
        if (PssView.getHwylogAuth().contains("A")) secutiryForm.setHwylogAuthA(true);
        if (PssView.getHwylogAuth().contains("E")) secutiryForm.setHwylogAuthE(true);
        if (PssView.getHwylogAuth().contains("D")) secutiryForm.setHwylogAuthD(true);

        if (PssView.getHwyelogAuth().contains("R")) secutiryForm.setHwyelogAuthR(true);
        if (PssView.getHwyelogAuth().contains("A")) secutiryForm.setHwyelogAuthA(true);
        if (PssView.getHwyelogAuth().contains("E")) secutiryForm.setHwyelogAuthE(true);
        if (PssView.getHwyelogAuth().contains("D")) secutiryForm.setHwyelogAuthD(true);




        if (PssView.getSignaturelogAuth().contains("R")) secutiryForm.setSignaturelogAuthR(true);
        if (PssView.getSignaturelogAuth().contains("A")) secutiryForm.setSignaturelogAuthA(true);
        if (PssView.getSignaturelogAuth().contains("E")) secutiryForm.setSignaturelogAuthE(true);
        if (PssView.getSignaturelogAuth().contains("D")) secutiryForm.setSignaturelogAuthD(true);


        if (PssView.getManageuserauth().contains("R")) secutiryForm.setManageuserauthR(true);
        if (PssView.getManageuserauth().contains("A")) secutiryForm.setManageuserauthA(true);
        if (PssView.getManageuserauth().contains("E")) secutiryForm.setManageuserauthE(true);
        if (PssView.getManageuserauth().contains("D")) secutiryForm.setManageuserauthD(true);

        if (PssView.getManagecompanyauth().contains("R")) secutiryForm.setManagecompanyauthR(true);
        if (PssView.getManagecompanyauth().contains("A")) secutiryForm.setManagecompanyauthA(true);
        if (PssView.getManagecompanyauth().contains("E")) secutiryForm.setManagecompanyauthE(true);
        if (PssView.getManagecompanyauth().contains("D")) secutiryForm.setManagecompanyauthD(true);


        if (PssView.getManagereportauth().contains("R")) secutiryForm.setManagereportauthR(true);
        if (PssView.getManagereportauth().contains("A")) secutiryForm.setManagereportauthA(true);
        if (PssView.getManagereportauth().contains("E")) secutiryForm.setManagereportauthE(true);
        if (PssView.getManagereportauth().contains("D")) secutiryForm.setManagereportauthD(true);


        if (PssView.getManagecodeauth().contains("R")) secutiryForm.setManagecodeauthR(true);
        if (PssView.getManagecodeauth().contains("A")) secutiryForm.setManagecodeauthA(true);
        if (PssView.getManagecodeauth().contains("E")) secutiryForm.setManagecodeauthE(true);
        if (PssView.getManagecodeauth().contains("D")) secutiryForm.setManagecodeauthD(true);

        return secutiryForm;
    }


    /** Search String CodeGroup  */
    public List<String> findgroupOnlyValue(){
        List<String> codepartList=null;
        codepartList=controlRepository.findGroupString();
        return codepartList;
    }


}
