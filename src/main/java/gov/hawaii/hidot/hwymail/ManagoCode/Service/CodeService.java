package gov.hawaii.hidot.hwymail.ManagoCode.Service;

import gov.hawaii.hidot.hwymail.ManagoCode.Entity.TBL20CODE;
import gov.hawaii.hidot.hwymail.ManagoCode.Form.CodeForm;
import gov.hawaii.hidot.hwymail.ManagoCode.Form.CodeSearchForm;
import gov.hawaii.hidot.hwymail.ManagoCode.Repository.CodeRepository;
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
public class CodeService {

    private final CodeJpaRepository controlRepository;
    private final CodeRepository SearchRepository;
    @Autowired
    public CodeService(CodeJpaRepository controlRepository, CodeRepository SearchRepository) {
        this.controlRepository = controlRepository;
        this.SearchRepository = SearchRepository;
    }
    /**
     * Add Data
     */
    public Long add(CodeForm pssinputForm){
        TBL20CODE pssView=new TBL20CODE();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username="";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        Date date = new Date(System.currentTimeMillis());
        BeanUtils.copyProperties(pssinputForm, pssView);
        pssView.setCode2(findMaxCode2(pssinputForm.getCode1())+1);
        pssView.setLastUpdated(date);
        pssView.setUserNameID(username);
        controlRepository.save(pssView);
        return pssView.getCodeSeq();
    }


    /** Edit Data */
    public Long edit(CodeForm pssinputForm){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username="";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        TBL20CODE pssView=new TBL20CODE();
        Date date = new Date(System.currentTimeMillis());
        BeanUtils.copyProperties(pssinputForm, pssView);
        pssView.setLastUpdated(date);
        pssView.setUserNameID(username);
        controlRepository.update(pssView);
        return pssView.getCodeSeq();
    }
    /** List Data */
    public Page<CodeForm> findCodes(CodeSearchForm psssearchForm, Pageable pageableRequest){
        Page<TBL20CODE> psslistpage;
        if (psssearchForm.getSearchCodeGroup()!=-1 && psssearchForm.getSearchName().equals("")){
            psslistpage=SearchRepository.findAllByCode1(psssearchForm.getSearchCodeGroup(),pageableRequest);
        } else if (psssearchForm.getSearchCodeGroup()==-1 && !psssearchForm.getSearchName().equals("")) {
            psslistpage=SearchRepository.findAllByCodenameContainingIgnoreCase(psssearchForm.getSearchName(),pageableRequest);
        } else if (psssearchForm.getSearchCodeGroup()!=-1 && !psssearchForm.getSearchName().equals("")) {
            psslistpage=SearchRepository.findAllByCode1AndCodenameContainingIgnoreCase(psssearchForm.getSearchCodeGroup(),psssearchForm.getSearchName(),pageableRequest);
        } else {
            psslistpage=SearchRepository.findAll(pageableRequest);
        }
        Page<CodeForm> pssFinalList=psslistpage.map(new Function<TBL20CODE, CodeForm>() {
                    @Override
                    public CodeForm apply(TBL20CODE pssList) {
                        CodeForm replaceform=new ModelMapper().map(pssList, CodeForm.class);
                        replaceform.setCodegroupname(controlRepository.findCodename(0,pssList.getCode1()));
                        return replaceform;
                    }
       });
        return pssFinalList;
    }
    /** Search One Data */
    public CodeForm findone(long codeSeq){
        ModelMapper modelMapper=new ModelMapper();
        CodeForm PssView = modelMapper.map(controlRepository.findById(codeSeq), CodeForm.class);
        return PssView;
    }

    /** Search Code Name  */
    public String findCodename(Long code1,Long code2){
        String codename = controlRepository.findCodename(code1,code2);
        return codename;
    }

    /** Search CodeGroup  */
    public List<CodeForm> findgroup(long code1){
        ModelMapper modelMapper=new ModelMapper();
        List<CodeForm> codepart = controlRepository.findGroupCode(code1)
                .stream()
                .map(m ->modelMapper.map(m, CodeForm.class))
                .collect(Collectors.toList());
        return codepart;
    }
    /** Search String CodeGroup  */
    public List<String> findgroupOnlyValue(long code1,String codepart){
        List<String> codepartList=null;
        if (codepart==""){
            codepartList= controlRepository.findGroupStringCode(code1);
        } else {
            codepartList= controlRepository.findGroupStringPartCode(code1,codepart);
        }
        return codepartList;
    }


    /** Delete Data */
    public void findDeleteone(long codeSeq){
        ModelMapper modelMapper=new ModelMapper();
        CodeForm PssView = modelMapper.map(controlRepository.findById(codeSeq), CodeForm.class);
        if (PssView.getCode1()==0){
            controlRepository.deleteById(codeSeq);
            controlRepository.deleteByCodeId(PssView.getCode2());
        } else {
            controlRepository.deleteById(codeSeq);
        }

    }
    /** Search Max Id */
    public long findMaxCode2(long code1){

        return controlRepository.findMaxCode2(code1);
    }


}
