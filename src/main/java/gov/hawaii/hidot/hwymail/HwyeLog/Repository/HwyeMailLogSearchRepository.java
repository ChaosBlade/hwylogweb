package gov.hawaii.hidot.hwymail.HwyeLog.Repository;

import gov.hawaii.hidot.hwymail.HwyeLog.Entity.TBL20HWYEMAILLOG;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HwyeMailLogSearchRepository extends PagingAndSortingRepository<TBL20HWYEMAILLOG,Long> {
    @Query("select c from TBL20HWYEMAILLOG c  where c.LetterNumber like CONCAT('%', ?1, '%') and InputDate >= ?2 and InputDate <= ?3")
    Page<TBL20HWYEMAILLOG> findAllByLetterNumber(String searchName, Date getSearchsdate, Date getSearchedate, Pageable pageable);
    @Query("select c from TBL20HWYEMAILLOG c  where c.Originator like CONCAT('%', ?1, '%') and InputDate >= ?2 and InputDate <= ?3")
    Page<TBL20HWYEMAILLOG> findAllByOriginator(String searchName,Date getSearchsdate,Date getSearchedate, Pageable pageable);
    @Query("select c from TBL20HWYEMAILLOG c  where c.Subject like CONCAT('%', ?1, '%') and InputDate >= ?2 and InputDate <= ?3")
    Page<TBL20HWYEMAILLOG> findAllBySubject(String searchName,Date getSearchsdate,Date getSearchedate, Pageable pageable);

    @Query("select c from TBL20HWYEMAILLOG c  where LetterNumber like CONCAT('%', ?1, '%') and expirationdate < ?2 and year(InputDate) >=2018 and completed=0 order by InputDate desc")
    List<TBL20HWYEMAILLOG> findAllbyLetterno(String searchname,Date currentdate);

    @Query("select c from TBL20HWYEMAILLOG c  where logID in  ?1")
    List<TBL20HWYEMAILLOG> findAllbyselected(Long[] logIDList);


    @Query("select c.logID from TBL20HWYEMAILLOG c  where c.LetterNumber like CONCAT('%', ?1, '%') and InputDate >= ?2 and InputDate <= ?3")
    List<Long> findAllallhwyeByLetterNumber(String searchName, Date getSearchsdate, Date getSearchedate);
    @Query("select c.logID from TBL20HWYEMAILLOG c  where c.Originator like CONCAT('%', ?1, '%') and InputDate >= ?2 and InputDate <= ?3")
    List<Long> findAllallhwyeByOriginator(String searchName,Date getSearchsdate,Date getSearchedate);
    @Query("select c.logID from TBL20HWYEMAILLOG c  where c.Subject like CONCAT('%', ?1, '%') and InputDate >= ?2 and InputDate <= ?3")
    List<Long> findAllallhwyeBySubject(String searchName,Date getSearchsdate,Date getSearchedate);





}
