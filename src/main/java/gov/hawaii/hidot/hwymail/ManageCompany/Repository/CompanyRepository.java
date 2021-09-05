package gov.hawaii.hidot.hwymail.ManageCompany.Repository;

import gov.hawaii.hidot.hwymail.ManageCompany.Entity.TBL20USERCOMPANY;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends PagingAndSortingRepository<TBL20USERCOMPANY,Long> {
    Page<TBL20USERCOMPANY> findAllByComIsland(long searchIsland, Pageable pageable);
    Page<TBL20USERCOMPANY> findAllByComType(long searchType, Pageable pageable);
    @Query("select c from TBL20USERCOMPANY c  where (c.comName like CONCAT('%', ?1, '%') or c.comBranch  like CONCAT('%', ?2, '%'))  ")
    Page<TBL20USERCOMPANY> findAllByComNameContainingIgnoreCase(String searchName,String searchName02, Pageable pageable);
    @Query("select c from TBL20USERCOMPANY c  where c.comIsland=?1 and  (c.comName like CONCAT('%', ?2, '%') or c.comBranch  like CONCAT('%', ?3, '%'))  ")
    Page<TBL20USERCOMPANY> findAllByComIslandAndComNameContainingIgnoreCase(long searchIsland,String searchName,String searchName02, Pageable pageable);
    @Query("select c from TBL20USERCOMPANY c  where c.comType=?1 and (c.comName like CONCAT('%', ?2, '%') or c.comBranch  like CONCAT('%', ?3, '%'))  ")
    Page<TBL20USERCOMPANY> findAllByComTypeAndComNameContainingIgnoreCase(long searchType,String searchName,String searchName02, Pageable pageable);
    @Query("select c from TBL20USERCOMPANY c  where c.comIsland=?1 and c.comType=?2 and (c.comName like CONCAT('%', ?3, '%') or c.comBranch  like CONCAT('%', ?4, '%'))  ")
    Page<TBL20USERCOMPANY> findAllByComIslandAndComTypeAndComNameContainingIgnoreCase(long searchIsland,long searchType,String searchName,String searchName02, Pageable pageable);
    List<TBL20USERCOMPANY> findAll();
}
