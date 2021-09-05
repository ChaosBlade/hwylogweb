package gov.hawaii.hidot.hwymail.ManageUsers.Repository;
import gov.hawaii.hidot.hwymail.ManageUsers.Entity.TBL10USERNAME;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<TBL10USERNAME,String> {

    Page<TBL10USERNAME> findAllByComID(long searchComid, Pageable pageable);


    @Query("select c from TBL10USERNAME c  where (c.userID like CONCAT('%', ?1, '%') or c.FirstName  like CONCAT('%', ?2, '%') or c.LastName  like CONCAT('%', ?2, '%') ) ")
    Page<TBL10USERNAME> findAllByFirstNameContainingIgnoreCase(String searchName01, String searchName02, String searchName03, Pageable pageable);
    Page<TBL10USERNAME> findAllByComIDAndFirstNameContainingIgnoreCase(long searchComid,String searchName, Pageable pageable);
    List<TBL10USERNAME> findAll();

}
