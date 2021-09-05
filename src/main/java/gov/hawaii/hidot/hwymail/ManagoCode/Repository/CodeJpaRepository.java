package gov.hawaii.hidot.hwymail.ManagoCode.Repository;

import gov.hawaii.hidot.hwymail.ManagoCode.Entity.TBL20CODE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class CodeJpaRepository {
    private final EntityManager em;
    @Autowired
    public CodeJpaRepository(EntityManager em) {
        this.em = em;
    }

    public TBL20CODE save(TBL20CODE pssdata) {
        em.persist(pssdata);
        return pssdata;
    }

    public TBL20CODE update(TBL20CODE pssdata) {
        em.merge(pssdata);
        return pssdata;
    }


    public TBL20CODE findById(long codeSeq) {
        TBL20CODE psscode=em.find(TBL20CODE.class,codeSeq);
        return psscode;
    }


    public List<TBL20CODE> findGroupCode(long code1) {
        List<TBL20CODE> result=em.createQuery("select m from TBL20CODE m where m.code1=:code1", TBL20CODE.class)
                .setParameter("code1",code1)
                .getResultList();
        return result;
    }
    public List<String> findGroupStringCode(long code1) {
        List<String> result=em.createQuery("select m.codename from TBL20CODE m where m.code1=:code1 order by codeord asc,codename asc", String.class)
                .setParameter("code1",code1)
                .getResultList();
        return result;
    }

    public List<String> findGroupStringPartCode(long code1,String codeString) {
        List<String> result=em.createQuery("select m.codename from TBL20CODE m where m.code1=:code1 and m.CertDuration like CONCAT('%', :codeString, '%')", String.class)
                .setParameter("code1",code1)
                .setParameter("codeString",codeString)
                .getResultList();
        return result;
    }




    public String findCodename(long code1,long code2) {
        String result=em.createQuery("select codename from TBL20CODE m where m.code1=:code1 and m.code2=:code2")
                .setParameter("code1",code1)
                .setParameter("code2",code2)
                .getSingleResult().toString();
        return result;
    }


    public long findMaxCode2(long code1) {
        long result=((Number) em.createQuery("select COALESCE(MAX(code2), 0) from TBL20CODE m where m.code1=:code1")
                .setParameter("code1",code1)
                .getSingleResult()).longValue();
        return result;
    }

    public Optional<TBL20CODE> findByName(String name) {
        List<TBL20CODE> result=em.createQuery("select m from TBL20CODE m ", TBL20CODE.class)
                // .setParameter("name",name)
                .getResultList();
        return result.stream().findAny();
    }

    public void deleteById(Long codeseq) {
        TBL20CODE pssdata = em.find(TBL20CODE.class, codeseq);
        em.remove(pssdata);
    }

    public void deleteByCodeId(Long code1) {
        em.createQuery("delete from TBL20CODE m  where m.code1=:code1")
                .setParameter("code1",code1)
                .executeUpdate();
    }






}
