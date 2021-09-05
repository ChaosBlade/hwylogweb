package gov.hawaii.hidot.hwymail.ManageUsers.Form;

import lombok.Data;

@Data
public class UserSearchForm {
    private long searchComid=-1;
    private long searchBranch=-1;
    private String searchName="";
}
