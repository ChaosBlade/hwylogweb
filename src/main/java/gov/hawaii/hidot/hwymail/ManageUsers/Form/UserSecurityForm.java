package gov.hawaii.hidot.hwymail.ManageUsers.Form;
import lombok.Data;

@Data
public class UserSecurityForm {
    private boolean hwylogAuthR;
    private boolean hwylogAuthA;
    private boolean hwylogAuthE;
    private boolean hwylogAuthD;

    private boolean signaturelogAuthR;
    private boolean signaturelogAuthA;
    private boolean signaturelogAuthE;
    private boolean signaturelogAuthD;

    private boolean hwyelogAuthR;
    private boolean hwyelogAuthA;
    private boolean hwyelogAuthE;
    private boolean hwyelogAuthD;


    private boolean manageuserauthR;
    private boolean manageuserauthA;
    private boolean manageuserauthE;
    private boolean manageuserauthD;

    private boolean managecompanyauthR;
    private boolean managecompanyauthA;
    private boolean managecompanyauthE;
    private boolean managecompanyauthD;


    private boolean managereportauthR;
    private boolean managereportauthA;
    private boolean managereportauthE;
    private boolean managereportauthD;


    private boolean managecodeauthR;
    private boolean managecodeauthA;
    private boolean managecodeauthE;
    private boolean managecodeauthD;
}
