package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.OrderItem;
import com.giants3.hd.noEntity.IOrderReportItem;
import com.giants3.hd.noEntity.OrderReportItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 出库附加数据细项
* Created by davidleen29 on 2014/9/17.
*/
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {


   OrderItem findFirstByOsNoEqualsAndItmEquals(String os_no, int item);
   //public Page<OrderItem> findByVerifyDateGreaterThan(String startDate , Pageable pageable);
  // public Page<OrderItem> findByVerifyDateGreaterThanEqual(String startDate , Pageable pageable);




//    @Query(value = " select p.osNo,o.cus_no,p.prdNo, o.sal_name,p.batNo,p.ut,p.qty,p.verifyDate,p.sendDate, pos.id_no from T_OrderItem p inner join T_Order  o  on o.osNo=p.osNo left outer join  (select itm,os_no,id_no from  DB_YF01.dbo.tf_pos    where  os_id='SO' ) pos on p.osNo=pos.os_no collate Chinese_PRC_90_CI_AI  and p.itm=pos.itm   where p.verifyDate >= :startDate and p.verifyDate <= :endDate and (  1=:matched or o.sal_no in (:sales )  )   order by p.verifyDate desc "
//         //  ,countQuery = " select count(p.id) from T_OrderItem p inner join T_Order  o  on o.osNo=p.osNo     where p.verifyDate >= :startDate and p.verifyDate <= :endDate and (  1=:matched or o.sal_no in (:sales )    )   "
//            ,nativeQuery = true
//    )//
//     public  List<Object> findByVerifyDateGreaterThanEqualAndVerifyDateLessThanEqual(@Param("startDate")  String   startDate , @Param("endDate")  String     endDate ,   @Param("matched") int matched, @Param("sales")  String[]  sales  );//,Pageable pageable


   @Query(value = " select p.osNo,o.cus_no,p.prdNo, o.sal_name,p.batNo,p.ut,p.qty,p.verifyDate,p.sendDate, pos.id_no,ois.maxWorkFlowName,mf_mo_C.jgh+'-'+mf_mo_C.name as workerC,mf_mo_D.jgh+'-'+mf_mo_D.name as workerD\n" +
           "  from T_OrderItem p inner join T_Order  o  on o.osNo=p.osNo left outer join  (select itm,os_no,id_no from  DB_YF01.dbo.tf_pos    where  os_id='SO' ) pos on p.osNo=pos.os_no collate Chinese_PRC_90_CI_AI  and p.itm=pos.itm  \n" +
           " left outer join (select itm,osNo,maxWorkFlowName from T_OrderItemWorkState ) as ois on p.itm=ois.itm and p.osNo=ois.osNo\n" +
           "\n" +
           "left outer join\n" +
           "   (     \n" +
           "      select    a1.so_no,a1.est_itm  , a2.jgh ,a3.name from  DB_YF01.dbo.mf_mo    a1    inner join  DB_YF01.dbo.mf_mo_z  a2 on a1.mo_no=a2.mo_no inner join DB_YF01.dbo.dept a3 on a2.jgh=a3.dep   where a1.bil_Id = upper('MP') and a1.mrp_no like 'C-%' and a3.MAKE_ID=3 )\n" +
           "      \n" +
           "       as mf_mo_C on  p.itm=mf_mo_C.est_itm and p.osNo=mf_mo_C.so_no collate Chinese_PRC_90_CI_AI \n" +
           "\n" +
           "\n" +
           "left outer join\n" +
           "   (     \n" +
           "      select    a1.so_no,a1.est_itm  , a2.jgh ,a3.name from  DB_YF01.dbo.mf_mo    a1    inner join  DB_YF01.dbo.mf_mo_z  a2 on a1.mo_no=a2.mo_no inner join DB_YF01.dbo.dept a3 on a2.jgh=a3.dep   where a1.bil_Id = upper('MP') and a1.mrp_no like 'D-%' and a3.MAKE_ID=3 )\n" +
           "      \n" +
           "       as mf_mo_D on  p.itm=mf_mo_D.est_itm and p.osNo=mf_mo_D.so_no collate Chinese_PRC_90_CI_AI \n" +
           " \n" +
           "\n" +
           "  where p.verifyDate >= :startDate and p.verifyDate <= :endDate and (  1=:matched or o.sal_no in (:sales )  ) \n" +
           "\n" +
           "    order by p.verifyDate desc \n "
         //  ,countQuery = " select count(p.id) from T_OrderItem p inner join T_Order  o  on o.osNo=p.osNo     where p.verifyDate >= :startDate and p.verifyDate <= :endDate and (  1=:matched or o.sal_no in (:sales )    )   "
            ,nativeQuery = true
    )//
     public  List<Object> findByVerifyDateGreaterThanEqualAndVerifyDateLessThanEqual(@Param("startDate")  String   startDate , @Param("endDate")  String     endDate ,   @Param("matched") int matched, @Param("sales")  String[]  sales  );//,Pageable pageable








    List<OrderItem> findByOsNoEqualsOrderByItm(String osNo);

    Page<OrderItem> findByOsNoLikeOrPrdNoLikeOrderByOsNoDesc(String key,String key1,Pageable pageable);

    @Modifying
    @Query("update T_OrderItem p set   p.url=:photoUrl , p.thumbnail=:thumbnail  WHERE p.prdNo =   :prd_no  and  p.pVersion= :pVersion")
    int updateUrlByProductInfo(@Param("thumbnail") String thumbnail,@Param("photoUrl") String url, @Param("prd_no")  String  prd_no,@Param("pVersion")  String  pVersion);
}
