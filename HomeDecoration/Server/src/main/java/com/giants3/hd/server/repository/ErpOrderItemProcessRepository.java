package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *
 * 订单生产流程状态表
* Created by davidleen29 on 2017/06/17
*/
public interface ErpOrderItemProcessRepository extends JpaRepository<ErpOrderItemProcess,Long> {




    ErpOrderItemProcess findFirstByOsNoEqualsAndItmEqualsAndCurrentWorkFlowStepEquals(String osNo, int itm,int flowStep);
    ErpOrderItemProcess findFirstByOsNoEqualsAndItmEqualsAndMrpNoEquals(String osNo, int itm,String mrpNo);
    ErpOrderItemProcess findFirstByMrpNoEquals(String mrpNo );
    ErpOrderItemProcess findFirstByMoNoEqualsAndMrpNoEquals(String mono ,String mrpNo);
//    List<ErpOrderItemProcess> findBySentQtyLessThanQty(    );
        @Query(" select p from T_ErpOrderItemProcess  p where  sentQty< qty")
    List<ErpOrderItemProcess> findUnCompleteProcesses(    );
    @Modifying
    @Query("delete T_ErpOrderItemProcess  p where   p.osNo=:os_no and p.itm=:itm   ")
    int deleteByOsNoAndItm(@Param("os_no") String os_no, @Param("itm") int itm);

    @Modifying
    @Query("update T_ErpOrderItemProcess p set p.itm=:itm where   p.osNo=:os_no and p.prdNo=:prd_no     and p.pVersion=:pVersion   ")
    int  updateItmByOsNoAndPrdNo(@Param("os_no") String osNo, @Param("prd_no") String prdNo,@Param("pVersion") String pVersion, @Param("itm") int itm);




    List<ErpOrderItemProcess> findFirstByOsNoEqualsAndPrdNoEqualsAndPVersionEquals(String osNo, String prdNo,String pVersion);

//
//   // @Query(" select distinct o from T_ErpOrderItemProcess  p , T_OrderItem o where   o.osNo=p.osNo and o.itm=p.itm  and o.workFlowState!="+ ErpWorkFlow.STATE_COMPLETE +" ")
//    @Query(" select   o from   T_OrderItem o where     o.workFlowState="+ ErpWorkFlow.STATE_WORKING +" and (o.osNo like  :key  or  o.prdNo like  :key  )")
//    List<OrderItem> findAllProcessUnComplete(@Param("key")     String key   );
//

}
