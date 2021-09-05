package gov.hawaii.hidot.hwymail.HwyLog.Repository;

import gov.hawaii.hidot.hwymail.HwyLog.Entity.TBL20MAILLOG;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HwyMailLogSearchRepository extends PagingAndSortingRepository<TBL20MAILLOG,Long> {
    @Query("select c from TBL20MAILLOG c  where c.LetterNumber like CONCAT('%', ?1, '%') and InputDate >= ?2 and InputDate <= ?3")
    Page<TBL20MAILLOG> findAllByLetterNumber(String searchName, Date getSearchsdate, Date getSearchedate, Pageable pageable);
    @Query("select c from TBL20MAILLOG c  where c.Originator like CONCAT('%', ?1, '%') and InputDate >= ?2 and InputDate <= ?3")
    Page<TBL20MAILLOG> findAllByOriginator(String searchName,Date getSearchsdate,Date getSearchedate, Pageable pageable);
    @Query("select c from TBL20MAILLOG c  where c.Subject like CONCAT('%', ?1, '%') and InputDate >= ?2 and InputDate <= ?3")
    Page<TBL20MAILLOG> findAllBySubject(String searchName,Date getSearchsdate,Date getSearchedate, Pageable pageable);

    @Query("select c from TBL20MAILLOG c  where LetterNumber like CONCAT('%', ?1, '%') and expirationdate < ?2 and year(InputDate) >=2018 and completed=0 order by InputDate desc")
    List<TBL20MAILLOG> findAllbyLetterno(String searchname,Date currentdate);

    @Query("select c from TBL20MAILLOG c  where logID in  ?1")
    List<TBL20MAILLOG> findAllbyselected(Long[] logIDList);
}
