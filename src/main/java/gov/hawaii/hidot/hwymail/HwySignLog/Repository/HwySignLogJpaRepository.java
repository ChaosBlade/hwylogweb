package gov.hawaii.hidot.hwymail.HwySignLog.Repository;

import gov.hawaii.hidot.hwymail.HwySignLog.Entity.TBL30SIGNLOG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class HwySignLogJpaRepository {
    private final EntityManager em;
    @Autowired
    public HwySignLogJpaRepository(EntityManager em) {
        this.em = em;
    }
    public List<TBL30SIGNLOG> findReportbylogID(String searchname) {

        List<TBL30SIGNLOG> result=em.createQuery("select m,(select GROUP_CONCAT(d.actionTaken) from TBL30SIGNLOGACTION d where d.logID=m.logID) as actionTakenList from TBL30SIGNLOG m where LetterNumber like CONCAT(:searchname, '%') and expirationdate < now() and completed=0  order by InputDate desc", TBL30SIGNLOG.class)
                .setParameter("searchname",searchname)
                .getResultList();
        return result;
    }
    public long findMaxlogID() {
        long result=((Number) em.createQuery("select COALESCE(MAX(logID), 0) from TBL30SIGNLOG m")
                .getSingleResult()).longValue();
        return result;
    }
    public void saveCurrentAction(String actionTakenView,long logID) {
        em.createQuery("update TBL30SIGNLOG m  set m.currentAction=:actionTakenView where logID=:logID")
                .setParameter("actionTakenView",actionTakenView)
                .setParameter("logID",logID)
                .executeUpdate();
    }
    public TBL30SIGNLOG findById(long codeSeq) {
        TBL30SIGNLOG psscode=em.find(TBL30SIGNLOG.class,codeSeq);
        return psscode;
    }

    public TBL30SIGNLOG save(TBL30SIGNLOG pssdata) {
        em.persist(pssdata);
        return pssdata;
    }

    public TBL30SIGNLOG update(TBL30SIGNLOG pssdata) {
        em.merge(pssdata);
        return pssdata;
    }

    public void deleteById(Long logID) {
        TBL30SIGNLOG pssdata = em.find(TBL30SIGNLOG.class, logID);
        em.remove(pssdata);
    }
}
