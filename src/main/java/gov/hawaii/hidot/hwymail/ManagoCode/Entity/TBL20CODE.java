package gov.hawaii.hidot.hwymail.ManagoCode.Entity;


import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Table(name="tbl20code")
@Entity
@Data
public class TBL20CODE {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CODESEQ")
    private Long codeSeq;
    @Column(name="CODE1")
    private long code1;
    @Column(name="CODE2")
    private long code2;
    @Column(name="CODENAME")
    private String codename;
    @Column(name="DESCIPTION")
    private String Desciption;
    @Column(name="CERTDURATION")
    private String CertDuration;
    @Column(name="CODEORD")
    private long codeord;
    @Column(name="USERNAMEID")
    private String UserNameID;
    @Column(name="LASTUPDATED")
    private Date lastUpdated;
}
