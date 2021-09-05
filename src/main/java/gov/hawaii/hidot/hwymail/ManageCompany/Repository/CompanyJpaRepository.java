package gov.hawaii.hidot.hwymail.ManageCompany.Repository;

import gov.hawaii.hidot.hwymail.ManageCompany.Entity.TBL20USERCOMPANY;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
@Repository
public class CompanyJpaRepository {
    private final EntityManager em;
    @Autowired
    public CompanyJpaRepository(EntityManager em) {
        this.em = em;
    }

    public TBL20USERCOMPANY findById(long CompanyID) {
        TBL20USERCOMPANY pssdata=em.find(TBL20USERCOMPANY.class,CompanyID);
        return pssdata;
    }

    public TBL20USERCOMPANY save(TBL20USERCOMPANY pssdata) {
        em.persist(pssdata);
        return pssdata;
    }

    public TBL20USERCOMPANY update(TBL20USERCOMPANY pssdata) {
        em.merge(pssdata);
        return pssdata;
    }

    public void deleteById(Long CompanyID) {
        TBL20USERCOMPANY pssdata = em.find(TBL20USERCOMPANY.class, CompanyID);
        em.remove(pssdata);
    }
    public long findByTypeID(String comName) {
        return (long) em.createQuery("select m.comType from TBL20USERCOMPANY m where m.comName=:comName")
                .setParameter("comName",comName)
                .getSingleResult();
    }
    public String findByTypeName(String comName) {
        return (String) em.createQuery("select i.codename from TBL20USERCOMPANY m  join  TBL20CODE  i on m.comType=i.code2 and i.code1=42 where m.comName=:comName")
                .setParameter("comName",comName)
                .getSingleResult();
    }
}
