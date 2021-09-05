package gov.hawaii.hidot.hwymail.HwyLog.Repository;

import gov.hawaii.hidot.hwymail.HwyLog.Entity.TBL20MAILLOGACTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class HwyMailLogActionJpaRepository {
    private final EntityManager em;
    @Autowired
    public HwyMailLogActionJpaRepository(EntityManager em) {
        this.em = em;
    }

    public List<TBL20MAILLOGACTION> findactionTakenbylogID(long logID) {
        List<TBL20MAILLOGACTION> result=em.createQuery("select m from TBL20MAILLOGACTION m where m.logID!=0 and m.logID=:logID", TBL20MAILLOGACTION.class)
                .setParameter("logID",logID)
                .getResultList();
        return result;
    }
    public List<String> findactionTakenStringbylogID(long logID) {
        List<String> result=em.createQuery("select m.actionTaken from TBL20MAILLOGACTION m where m.logID!=0 and  m.Deleted=0 and m.logID=:logID", String.class)
                .setParameter("logID",logID)
                .getResultList();
        return result;
    }

    public TBL20MAILLOGACTION findById(long logActionId) {
        TBL20MAILLOGACTION pssdata=em.find(TBL20MAILLOGACTION.class,logActionId);
        return pssdata;
    }
    public long exitById(long logActionId) {
        return (long) em.createQuery("select count(m) from TBL20MAILLOGACTION m where m.logActionId=:logActionId")
                .setParameter("logActionId",logActionId)
                .getSingleResult();
    }
    public long exitBylogID(long logID) {
        return (long) em.createQuery("select count(m) from TBL20MAILLOGACTION m where m.logID!=0 and  m.Deleted=0 and  m.logID=:logID")
                .setParameter("logID",logID)
                .getSingleResult();
    }
    public TBL20MAILLOGACTION save(TBL20MAILLOGACTION pssdata) {
        em.persist(pssdata);
        return pssdata;
    }

    public TBL20MAILLOGACTION update(TBL20MAILLOGACTION pssdata) {
        em.merge(pssdata);
        return pssdata;
    }


    public void deleteBylogId(Long logID) {
        em.createQuery("delete from TBL20MAILLOGACTION m  where m.logID!=0 and  m.logID=:logID")
                .setParameter("logID",logID)
                .executeUpdate();
    }

}
