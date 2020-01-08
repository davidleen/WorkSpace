package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.WorkFlowMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 流程传递处理信息
 */
public interface WorkFlowMessageRepository extends JpaRepository<WorkFlowMessage, Long> {
    /**
     * 查询未设置produceType的消息记录
     * @return
     */
    List<WorkFlowMessage> findByProduceTypeNameIsNull();

    List<WorkFlowMessage> findByMrpTypeIsNullOrPrdTypeIsNull();

    List<WorkFlowMessage> findByStateInAndToFlowStepInAndReceiverIdEquals(int[] i, int[] flowSteps, long userId);

    List<WorkFlowMessage> findByFromFlowStepInOrderByCreateTimeDesc(int[] flowSteps);

    List<WorkFlowMessage> findByOrderNameEqualsAndItmEqualsOrderByCreateTimeDesc(String osNo, int itm);

    List<WorkFlowMessage> findByToFlowStepEqualsAndOrderNameEqualsAndProductNameEqualsAndPVersionEqualsOrderByCreateTimeDesc(int flowStep, String os_no, String prd_no, String pVersion);

    List<WorkFlowMessage> findByToFlowStepEqualsAndOrderNameEqualsAndItmEqualsOrderByCreateTimeDesc(int flowStep, String os_no, int itm);
    List<WorkFlowMessage> findByToFlowStepEqualsAndOrderNameEqualsAndItmEqualsOrderByReceiveTimeDesc(int flowStep, String os_no, int itm);
    List<WorkFlowMessage> findByFromFlowStepEqualsAndOrderNameEqualsAndItmEqualsOrderByReceiveTimeDesc(int flowStep, String os_no, int itm);
    List<WorkFlowMessage> findByFromFlowStepEqualsAndOrderNameEqualsAndItmEqualsOrderByCreateTimeDesc(int flowStep, String os_no, int itm);

    List<WorkFlowMessage> findByFromFlowStepNotAndReceiverIdEqualsOrderByCreateTimeDesc(int flowStep, long receiverId);

//    /**
//     *  删除指定订单的流程消息
//     * @param orderItemId
//     * @return
//     */
//    int deleteByOrderItemIdEquals(long orderItemId  );

//    @Modifying
//    @Query("update T_WorkFlowMessage p set   p.url=:photoUrl , p.thumbnail=:thumbnail  WHERE p.productId =   :productId ")
//    int updateUrlByProductId(@Param("thumbnail") String thumbnail, @Param("photoUrl") String url,@Param("productId")  long productId);

//    /**
//     * 查询处理超过指定时间的流程处理信息
//     * @param overHour 超期时间。
//     * @param pageable
//     * @return
//     */
//    @Query("select  p from T_WorkFlowMessage  p WHERE p.productId =   :productId ")
//    Page<WorkFlowMessage> findByHandleTimeOver(@Param("overHour") int  overHour,  Pageable pageable );


    List<WorkFlowMessage> findByReceiverIdEqualsOrSenderIdEqualsOrderByReceiveTimeDesc(long userId, long userId2);


    @Query(value="select p from  T_WorkFlowMessage  p where  (p.senderId=:userId or receiverId=:userId ) and  (p.orderName like :key or p.productName like :key )  order by p.receiveTime desc "
    ,countQuery = "select count(p.id) from  T_WorkFlowMessage  p where  (p.senderId=:userId or receiverId=:userId ) and  (p.orderName like :key or p.productName like :key ) "
    )
    Page<WorkFlowMessage> findMyWorkFlowMessages(@Param("userId") long userId, @Param("key") String key , Pageable pageable );

    @Query(value="select p from  T_WorkFlowMessage  p where   (p.orderName like :key or p.productName like :key )  order by p.receiveTime desc "
    ,countQuery = "select count(p.id) from  T_WorkFlowMessage  p where    (p.orderName like :key or p.productName like :key ) "
    )
    Page<WorkFlowMessage> findAllWorkFlowMessages(  @Param("key") String key , Pageable pageable );


    @Modifying
    @Query("delete T_WorkFlowMessage p where   p.orderName=:os_no and p.itm=:itm   ")
    int deleteByOsNoAndItm(@Param("os_no") String os_no, @Param("itm") int itm);


    List<WorkFlowMessage> findByFromFlowStepEqualsAndOrderNameEqualsAndItmEqualsOrderByCreateTimeAsc(int workFlowStep, String osNo, int itm);




    /**
     * ToFlowStep  (2000,3000) 白胚 与颜色阶段 要根据当前用户的铁木权限进行分配
     * @param states
     * @param workflowSteps
     * @param userId
     * @param key
     * @return
     */

    @Query(value= " select  a.* from ( select * from  t_workflowmessage   where  state in :states and toFlowStep in :workflowSteps    and receiverId =0 and ( orderName like :key or productName like :key )  ) as a  \n" +
            " \n" +
            "left outer  join (select   UserId, mu,tie ,ProduceType,WorkFlowStep from T_WorkflowWorker where userId= :userId) e on  e.ProduceType=a.produceType and e.WorkFlowStep=a.toFlowStep \n" +
            "\n" +
            "where    ToFlowStep not in (2000,3000) \n" +
            "  or (    not (((a.mrpType='T' or (a.mrpType<>'M' and a.mrpType<>'T' and a.prdType='TJ')) and e.tie=0) or ((a.mrpType='M' or   (a.mrpType<>'M' and a.mrpType<>'T' and a.prdType='MJ')) and e.mu=0)  ) )   order by orderName desc ,itm   " ,nativeQuery = true

    )

    //a.mrpType='T' or (a.mrpType<>'M' and a.mrpType<>'T' and a.prdType='TJ')  铁件类型判断
    //a.mrpType='M' or (a.mrpType<>'M' and a.mrpType<>'T' and a.prdType='MJ')  木件类型判断
    //优先判断mrpType，当mrpType 未设置（非M T） 时候 判断TJ MJ

    List<WorkFlowMessage> findMyUnHandleWorkFlowMessages2( @Param("states") int[]  states , @Param("workflowSteps") int[]  workflowSteps , @Param("userId") long  userId , @Param("key") String  key );






            @Query(value =" select p from  T_WorkFlowMessage  p  where p.fromFlowStep<>1000 and p.createTimeString >:dateStart and p.createTimeString <:dateEnd   " +

                    //判断是否未完成的消息
                    "and( :unhandle = false or  (p.receiveTime is null or p.receiveTime=0 )) " +
                    //超期判断
                    " and ( :overDue=false or ( ((p.receiveTime is null or p.receiveTime=0) and :currentTimeMillis-p.createTime>:limitMillis) or (p.receiveTime-p.createTime>:limitMillis)  ) )" +

                    " order by p.orderName desc,p.itm asc " )
    List<WorkFlowMessage> queryWorkFlowMessageReport(@Param("dateStart") String dateStart,@Param("dateEnd")  String dateEnd, @Param("unhandle") boolean unhandle, @Param("overDue") boolean overDue, @Param("currentTimeMillis") long currentTimeMillis,@Param("limitMillis") long limitMillis);//, boolean overDue, long currentTime

    @Modifying
    @Query("update T_WorkFlowMessage p set p.itm=:itm where   p.orderName=:os_no and p.productName=:prd_no  and p.pVersion=:pVersion  ")
    int  updateItmByOsNoAndPrdNo(@Param("os_no") String osNo, @Param("prd_no") String prdNo, @Param("pVersion") String pVersion, @Param("itm") int itm);

    List<WorkFlowMessage> findBySenderIdEqualsAndStateEquals(long senderId, int state);
}
