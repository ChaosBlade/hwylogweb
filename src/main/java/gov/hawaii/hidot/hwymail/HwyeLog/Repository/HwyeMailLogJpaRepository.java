package gov.hawaii.hidot.hwymail.HwyeLog.Repository;

import gov.hawaii.hidot.hwymail.HwySignLog.Entity.TBL30SIGNLOG;
import gov.hawaii.hidot.hwymail.HwyeLog.Entity.TBL20HWYEMAILLOG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

@Repository
public class HwyeMailLogJpaRepository {
    private final EntityManager em;
    @Autowired
    public HwyeMailLogJpaRepository(EntityManager em) {
        this.em = em;
    }

    public TBL20HWYEMAILLOG findById(long logID) {
        TBL20HWYEMAILLOG psscode=em.find(TBL20HWYEMAILLOG.class,logID);
        return psscode;
    }

    public TBL20HWYEMAILLOG save(TBL20HWYEMAILLOG pssdata) {
        em.persist(pssdata);
        return pssdata;
    }

    public TBL20HWYEMAILLOG update(TBL20HWYEMAILLOG pssdata) {
        em.merge(pssdata);
        return pssdata;
    }

    public void deleteById(Long logID) {
        TBL20HWYEMAILLOG pssdata = em.find(TBL20HWYEMAILLOG.class, logID);
        em.remove(pssdata);
    }






}
