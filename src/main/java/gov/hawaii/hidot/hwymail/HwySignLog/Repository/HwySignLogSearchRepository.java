package gov.hawaii.hidot.hwymail.HwySignLog.Repository;

import gov.hawaii.hidot.hwymail.HwySignLog.Entity.TBL30SIGNLOG;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Date;
import java.util.List;

public interface HwySignLogSearchRepository extends PagingAndSortingRepository<TBL30SIGNLOG,Long> {

    @Query("select c from TBL30SIGNLOG c  where c.LetterNumber like CONCAT('%', ?1, '%') and InputDate >= ?2 and InputDate <= ?3")
    Page<TBL30SIGNLOG> findAllByLetterNumber(String searchName,Date getSearchsdate,Date getSearchedate, Pageable pageable);
    @Query("select c from TBL30SIGNLOG c  where c.Originator like CONCAT('%', ?1, '%') and InputDate >= ?2 and InputDate <= ?3")
    Page<TBL30SIGNLOG> findAllByOriginator(String searchName,Date getSearchsdate,Date getSearchedate, Pageable pageable);
    @Query("select c from TBL30SIGNLOG c  where c.Subject like CONCAT('%', ?1, '%') and InputDate >= ?2 and InputDate <= ?3")
    Page<TBL30SIGNLOG> findAllBySubject(String searchName,Date getSearchsdate,Date getSearchedate, Pageable pageable);
    @Query("select c from TBL30SIGNLOG c  where LetterNumber like CONCAT(?1, '%') and expirationdate < ?2 and completed=0  order by InputDate desc")
    List<TBL30SIGNLOG> findAllbyLetterno(String searchname,Date currentdate);
    @Query("select c from TBL30SIGNLOG c  where logID in  (?1)")
    List<TBL30SIGNLOG> findAllbyselected(Long[] logIDList);
}
