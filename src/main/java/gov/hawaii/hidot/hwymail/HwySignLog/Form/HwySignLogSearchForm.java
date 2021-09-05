package gov.hawaii.hidot.hwymail.HwySignLog.Form;

import gov.hawaii.hidot.hwymail.ManagoCode.Form.CodeForm;
import lombok.Data;

import java.util.List;
@Data
public class HwySignLogSearchForm {
    private String searchsdate="";
    private String searchedate="";
    private long searchType=-1;
    private String searchName="";
    private List<CodeForm> searchTypeList;
}
