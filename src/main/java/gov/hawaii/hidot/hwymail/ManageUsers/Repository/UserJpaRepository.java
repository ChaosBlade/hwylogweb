package gov.hawaii.hidot.hwymail.ManageUsers.Repository;
import gov.hawaii.hidot.hwymail.ManageUsers.Entity.TBL10USERNAME;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class UserJpaRepository {
    private final EntityManager em;
    @Autowired
    public UserJpaRepository(EntityManager em) {
        this.em = em;
    }

    public TBL10USERNAME findById(String UserID) {
        TBL10USERNAME pssdata=em.find(TBL10USERNAME.class,UserID);
        return pssdata;
    }
    public TBL10USERNAME save(TBL10USERNAME pssdata) {
        em.persist(pssdata);
        return pssdata;
    }

    public TBL10USERNAME update(TBL10USERNAME pssdata) {
        em.merge(pssdata);
        return pssdata;
    }

    public void deleteById(String UserID) {
        TBL10USERNAME pssdata = em.find(TBL10USERNAME.class, UserID);
        em.remove(pssdata);
    }
    public String findUsername(String UserID) {
        String result=em.createQuery("select CONCAT(FirstName,' ',LastName) from TBL10USERNAME m where m.userID=:UserID ")
                .setParameter("UserID",UserID)
                .getSingleResult().toString();
        return result;
    }



    public List<String> findGroupString() {
        List<String> result=em.createQuery("select CONCAT(FirstName,' ',LastName) from TBL10USERNAME m ", String.class)
                .getResultList();
        return result;
    }



}
