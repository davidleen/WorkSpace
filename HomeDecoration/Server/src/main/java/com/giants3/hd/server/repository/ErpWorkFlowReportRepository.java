package com.giants3.hd.server.repository;

import com.giants3.hd.entity.ErpWorkFlowReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by davidleen29 on 2017/4/8.
 */
public interface ErpWorkFlowReportRepository extends JpaRepository<ErpWorkFlowReport, Long> {


    List<ErpWorkFlowReport> findByOsNoEqualsAndPrdNoEquals(String os_no, String prd_no);

    List<ErpWorkFlowReport> findByOsNoEqualsAndPrdNoEqualsAndPVersionEquals(String os_no, String prd_no, String pVersion);

    List<ErpWorkFlowReport> findByOsNoEqualsAndItmEquals(String os_no, int itm);

    List<ErpWorkFlowReport> findByOsNoEqualsAndItmEqualsOrderByWorkFlowStepAsc(String os_no, int itm);

    List<ErpWorkFlowReport> findByPercentageLessThanAndStartDateGreaterThan(float percentage, long createTime);

    ErpWorkFlowReport findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(String osNo, int itm, int fromFlowStep);

    @Modifying
    @Query("delete T_ErpWorkFlowReport p where   p.osNo=:os_no and p.itm=:itm   ")
    int deleteByOsNoAndItm(@Param("os_no") String os_no, @Param("itm") int itm);

    @Modifying
    @Query("update T_ErpWorkFlowReport p set p.itm=:itm where   p.osNo=:os_no and p.prdNo=:prd_no   ")
    int  updateItmByOsNoAndPrdNo(@Param("os_no") String osNo, @Param("prd_no") String prdNo, @Param("itm") int itm);
}
