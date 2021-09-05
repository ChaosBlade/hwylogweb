package gov.hawaii.hidot.hwymail.ManageReport.Repository;

import gov.hawaii.hidot.hwymail.ManageReport.Entity.TBL20USERREPORTS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ReportJpaRepository {
    private final EntityManager em;
    @Autowired
    public ReportJpaRepository(EntityManager em) {
        this.em = em;
    }

    public TBL20USERREPORTS findById(long reportId) {
        TBL20USERREPORTS pssdata=em.find(TBL20USERREPORTS.class,reportId);
        return pssdata;
    }

    public TBL20USERREPORTS save(TBL20USERREPORTS pssdata) {
        em.persist(pssdata);
        return pssdata;
    }

    public TBL20USERREPORTS update(TBL20USERREPORTS pssdata) {
        em.merge(pssdata);
        return pssdata;
    }

    public void deleteById(Long reportId) {
        TBL20USERREPORTS pssdata = em.find(TBL20USERREPORTS.class, reportId);
        em.remove(pssdata);
    }


    public List<Long> findByDistinctReportType(long reportGroup) {
        return em.createQuery("select distinct reportType from TBL20USERREPORTS where reportGroup=:reportGroup", Long.class)
                .setParameter("reportGroup",reportGroup)
                .getResultList();
    }


}
