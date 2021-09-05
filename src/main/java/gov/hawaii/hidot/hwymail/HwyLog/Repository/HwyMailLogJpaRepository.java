package gov.hawaii.hidot.hwymail.HwyLog.Repository;

import gov.hawaii.hidot.hwymail.HwyLog.Entity.TBL20MAILLOG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class HwyMailLogJpaRepository {
    private final EntityManager em;
    @Autowired
    public HwyMailLogJpaRepository(EntityManager em) {
        this.em = em;
    }

    public long findMaxlogID() {
        long result=((Number) em.createQuery("select COALESCE(MAX(logID), 0) from TBL20MAILLOG m")
                .getSingleResult()).longValue();
        return result;
    }
    public void saveCurrentAction(String actionTakenView,long logID) {
        em.createQuery("update TBL20MAILLOG m  set m.currentAction=:actionTakenView where logID=:logID")
                .setParameter("actionTakenView",actionTakenView)
                .setParameter("logID",logID)
                .executeUpdate();
    }
    public TBL20MAILLOG findById(long logID) {
        TBL20MAILLOG psscode=em.find(TBL20MAILLOG.class,logID);
        return psscode;
    }

    public TBL20MAILLOG save(TBL20MAILLOG pssdata) {
        em.persist(pssdata);
        return pssdata;
    }

    public TBL20MAILLOG update(TBL20MAILLOG pssdata) {
        em.merge(pssdata);
        return pssdata;
    }

    public void deleteById(Long logID) {
        TBL20MAILLOG pssdata = em.find(TBL20MAILLOG.class, logID);
        em.remove(pssdata);
    }
}
