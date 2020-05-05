package com.giants3.hd.server.repository;

import com.giants3.hd.entity.ErpWorkFlowReport;
import com.giants3.hd.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by davidleen29 on 2017/4/8.
 */
public interface ErpWorkFlowReportRepository extends JpaRepository<ErpWorkFlowReport, Long> {

    @Query(value = "SELECT   distinct    a.*    FROM ( select * from T_ErpWorkFlowReport  where    workFlowStep=:currentUnCompletedStep and produceType=:produceType and percentage<1  ) a   inner join ( select osNo,itm from T_ErpWorkFlowReport   where  workFlowStep=:previousCompletedStep and produceType=:produceType and percentage>=1 )b  on a.osNo=b.osNo and a.itm=b.itm",nativeQuery = true)
   List<ErpWorkFlowReport> findWorkingStep( @Param("previousCompletedStep") int previousCompletedStep  , @Param("currentUnCompletedStep") int currentUnCompletedStep,@Param("produceType") int produceType);

    @Query(value = "SELECT  P from T_ErpWorkFlowReport    P where    p.workFlowStep=:currentUnCompletedStep and p.produceType=:produceType and p.percentage<1  and p.startDate>0   " )
   List<ErpWorkFlowReport> findUnCompletedStep(  @Param("currentUnCompletedStep") int currentUnCompletedStep,@Param("produceType") int produceType);


  @Query(value = "SELECT  P from T_ErpWorkFlowReport    P where    p.workFlowStep=:currentUnCompletedStep  and p.percentage<1  and p.startDate>0   " )
  List<ErpWorkFlowReport> findUnCompletedStep(  @Param("currentUnCompletedStep") int currentUnCompletedStep );


    List<ErpWorkFlowReport> findByOsNoEqualsAndPrdNoEquals(String os_no, String prd_no);

    List<ErpWorkFlowReport> findByOsNoEqualsAndPrdNoEqualsAndPVersionEquals(String os_no, String prd_no, String pVersion);

    List<ErpWorkFlowReport> findByOsNoEqualsAndItmEquals(String os_no, int itm);

    List<ErpWorkFlowReport> findByOsNoEqualsAndItmEqualsOrderByWorkFlowStepAsc(String os_no, int itm);

    List<ErpWorkFlowReport> findByPercentageLessThanAndStartDateGreaterThan(float percentage, long createTime);
    @Query(value = "SELECT  P from T_ErpWorkFlowReport    P where    p.state=:state  and  ( p.osNo like :key or p.prdNo like :key or p.pVersion like :key)   " )
    Page<ErpWorkFlowReport> findAllMonitoredWorkFlowReports( @Param("key") String key,@Param("state") int state, Pageable pageable);


    @Query(value = "SELECT  P from T_ErpWorkFlowReport    P where    p.state=:state  and  ( p.osNo like :key or p.prdNo like :key or p.pVersion like :key)   and  p.workFlowStep in  (select distinct  workFlowStep from T_WorkFlowWorker where userId=:user_id )" )
    Page<ErpWorkFlowReport> findMonitoredWorkFlowReports(@Param("key") String key, @Param("state") int state, @Param("user_id") long  userId, Pageable pageable);
    ErpWorkFlowReport findFirstByOsNoEqualsAndItmEqualsAndWorkFlowStepEquals(String osNo, int itm, int fromFlowStep);

    @Modifying
    @Query("delete T_ErpWorkFlowReport p where   p.osNo=:os_no and p.itm=:itm   ")
    int deleteByOsNoAndItm(@Param("os_no") String os_no, @Param("itm") int itm);

    @Modifying
    @Query("update T_ErpWorkFlowReport p set p.itm=:itm where   p.osNo=:os_no and p.prdNo=:prd_no  and p.pVersion=:pVersion   ")
    int  updateItmByOsNoAndPrdNo(@Param("os_no") String osNo, @Param("prd_no") String prdNo, @Param("pVersion") String pVersion, @Param("itm") int itm);


  @Query(value = "SELECT  a.* from (select * from T_ErpWorkFlowReport where  workFlowStep=:flowstep)  as a inner join (select * from T_OrderItemWorkState where workFlowState=:orderState) as e on a.osNo=e.osNo and a.itm=e.itm  " ,nativeQuery = true)//and a.osNo='19YF499' and a.itm=2
   List<ErpWorkFlowReport> findAllByOrderStateEqualsAndFlowStepEquals(@Param("orderState")int orderItemState,@Param("flowstep") int flowStep);
}
