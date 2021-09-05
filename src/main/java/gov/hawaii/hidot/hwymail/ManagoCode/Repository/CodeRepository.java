package gov.hawaii.hidot.hwymail.ManagoCode.Repository;
import gov.hawaii.hidot.hwymail.ManagoCode.Entity.TBL20CODE;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
public interface CodeRepository  extends PagingAndSortingRepository<TBL20CODE,Long> {
    Page<TBL20CODE>  findAllByCode1(long searchCodeGroup, Pageable pageable);
    Page<TBL20CODE>  findAllByCodenameContainingIgnoreCase(String searchName, Pageable pageable);
    Page<TBL20CODE>  findAllByCode1AndCodenameContainingIgnoreCase(long searchCodeGroup,String searchName, Pageable pageable);
}



