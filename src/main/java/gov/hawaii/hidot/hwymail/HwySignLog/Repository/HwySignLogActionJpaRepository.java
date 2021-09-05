package gov.hawaii.hidot.hwymail.HwySignLog.Repository;

import gov.hawaii.hidot.hwymail.HwySignLog.Entity.TBL30SIGNLOGACTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class HwySignLogActionJpaRepository {
    private final EntityManager em;
    @Autowired
    public HwySignLogActionJpaRepository(EntityManager em) {
        this.em = em;
    }

    public List<TBL30SIGNLOGACTION> findactionTakenbylogID(long logID) {
        List<TBL30SIGNLOGACTION> result=em.createQuery("select m from TBL30SIGNLOGACTION m where m.logID!=0 and  m.logID=:logID", TBL30SIGNLOGACTION.class)
                .setParameter("logID",logID)
                .getResultList();
        return result;
    }


    public List<String> findactionTakenStringbylogID(long logID) {
        List<String> result=em.createQuery("select m.actionTaken from TBL30SIGNLOGACTION m where m.logID!=0 and  m.Deleted=0 and m.logID=:logID", String.class)
                .setParameter("logID",logID)
                .getResultList();
        return result;
    }


    public TBL30SIGNLOGACTION findById(long logActionId) {
        TBL30SIGNLOGACTION pssdata=em.find(TBL30SIGNLOGACTION.class,logActionId);
        return pssdata;
    }
    public long exitById(long logActionId) {
        return (long) em.createQuery("select count(m) from TBL30SIGNLOGACTION m where m.logID!=0 and  m.logActionId=:logActionId")
                .setParameter("logActionId",logActionId)
                .getSingleResult();
    }
    public long exitBylogID(long logID) {
        return (long) em.createQuery("select count(m) from TBL30SIGNLOGACTION m where m.logID!=0 and  m.logID=:logID")
                .setParameter("logID",logID)
                .getSingleResult();
    }
    public TBL30SIGNLOGACTION save(TBL30SIGNLOGACTION pssdata) {
        em.persist(pssdata);
        return pssdata;
    }

    public TBL30SIGNLOGACTION update(TBL30SIGNLOGACTION pssdata) {
        em.merge(pssdata);
        return pssdata;
    }


    public void deleteBylogId(Long logID) {
        em.createQuery("delete from TBL30SIGNLOGACTION m  where m.logID!=0 and  m.logID=:logID")
                .setParameter("logID",logID)
                .executeUpdate();
    }

}
