package gov.hawaii.hidot.hwymail.ManageReport.Repository;

import gov.hawaii.hidot.hwymail.ManageReport.Entity.TBL20USERREPORTS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.List;

public interface ReportRepository extends PagingAndSortingRepository<TBL20USERREPORTS,Long> {
    Page<TBL20USERREPORTS> findAllByReportGroup(long searchReportGroup, Pageable pageable);
    Page<TBL20USERREPORTS> findAllByReportType(long searchReportType, Pageable pageable);
    Page<TBL20USERREPORTS> findAllByReportNameContainingIgnoreCase(String searchName, Pageable pageable);
    Page<TBL20USERREPORTS> findAllByReportGroupAndReportNameContainingIgnoreCase(long searchReportGroup,String searchName, Pageable pageable);
    Page<TBL20USERREPORTS> findAllByReportTypeAndReportNameContainingIgnoreCase(long searchReportType,String searchName, Pageable pageable);
    Page<TBL20USERREPORTS> findAllByReportGroupAndReportTypeAndReportNameContainingIgnoreCase(long searchReportGroup,long searchReportType,String searchName, Pageable pageable);
    List<TBL20USERREPORTS> findAll();
   List<TBL20USERREPORTS> findAllByReportGroupAndReportType(long searchReportGroup,long searchReportType);

}
