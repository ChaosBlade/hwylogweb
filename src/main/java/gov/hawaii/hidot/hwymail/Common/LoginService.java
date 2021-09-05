package gov.hawaii.hidot.hwymail.Common;


import gov.hawaii.hidot.hwymail.ManageUsers.Form.UserForm;
import gov.hawaii.hidot.hwymail.ManageUsers.Repository.UserJpaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class LoginService implements UserDetailsService {

    @Autowired
    UserJpaRepository SearchSepository;

    @Override
    public UserDetails loadUserByUsername(String UserID) throws UsernameNotFoundException {
        ModelMapper modelMapper=new ModelMapper();
        UserForm PssView = modelMapper.map(SearchSepository.findById(UserID), UserForm.class);
        if (SearchSepository.findById(UserID) == null) {
            throw new UsernameNotFoundException(UserID);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (PssView.isAdministrator()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USERS"));
        }
        authorities.add(new SimpleGrantedAuthority("REPORT_"+PssView.getReportuserLevel()));

        if (PssView.getHwylogAuth()!="") {
            String[] arrOfStr = PssView.getHwylogAuth().split("");
            for (String PermissionChar : arrOfStr)
                authorities.add(new SimpleGrantedAuthority("HW_" + PermissionChar));
        }
        if (PssView.getSignaturelogAuth()!="") {
            String[] arrOfStr = PssView.getSignaturelogAuth().split("");
            for (String PermissionChar : arrOfStr)
                authorities.add(new SimpleGrantedAuthority("SI_" + PermissionChar));
        }
        if (PssView.getHwyelogAuth()!="") {
            String[] arrOfStr = PssView.getHwyelogAuth().split("");
            for (String PermissionChar : arrOfStr)
                authorities.add(new SimpleGrantedAuthority("HWE_" + PermissionChar));
        }
        if (PssView.getManageuserauth()!="") {
            String[] arrOfStr = PssView.getManageuserauth().split("");
            for (String PermissionChar : arrOfStr)
                authorities.add(new SimpleGrantedAuthority("USER_" + PermissionChar));
        }
        if (PssView.getManagecompanyauth()!="") {
            String[] arrOfStr = PssView.getManagecompanyauth().split("");
            for (String PermissionChar : arrOfStr)
                authorities.add(new SimpleGrantedAuthority("COM_" + PermissionChar));
        }

        if (PssView.getManagereportauth()!="") {
            String[] arrOfStr = PssView.getManagereportauth().split("");
            for (String PermissionChar : arrOfStr)
                authorities.add(new SimpleGrantedAuthority("REP_" + PermissionChar));
        }

        if (PssView.getManagecodeauth()!="") {
            String[] arrOfStr = PssView.getManagecodeauth().split("");
            for (String PermissionChar : arrOfStr)
                authorities.add(new SimpleGrantedAuthority("COD_" + PermissionChar));
        }
        UserDetails user= new User(PssView.getUserID(), PssView.getPassword(),authorities);
        return user;
    }
}

