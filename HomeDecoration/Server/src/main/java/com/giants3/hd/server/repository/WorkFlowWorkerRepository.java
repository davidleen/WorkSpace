package com.giants3.hd.server.repository;

import com.giants3.hd.entity.WorkFlowWorker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by davidleen29 on 2017/4/8.
 */
public interface WorkFlowWorkerRepository extends JpaRepository<WorkFlowWorker, Long> {

      List<WorkFlowWorker>  findByUserIdEquals(long userId);
    List<WorkFlowWorker> findByUserNameLikeOrderByUserNameAscWorkFlowStepAsc( String key);
    /**
     * 查找指定流程指定工作人的记录
     * @param userId
     * @param workFlowCode
     * @return
     */
//      List<WorkFlowWorker>  findByUserIdEqualsAndWorkFlowCodeEquals(long userId,String  workFlowCode);
     //  WorkFlowWorker   findFirstByUserIdEqualsAndWorkFlowCodeEqualsAndReceiveEquals(long userId,String  workFlowCode,boolean canReceive);
       WorkFlowWorker   findFirstByUserIdEqualsAndProduceTypeEqualsAndWorkFlowCodeEqualsAndReceiveEquals(long userId,int produceType,String  workFlowCode,boolean canReceive);
      List< WorkFlowWorker>   findByUserIdEqualsAndReceiveEquals(long userId, boolean canReceive);
     WorkFlowWorker   findFirstByUserIdEqualsAndWorkFlowCodeEqualsAndSendEquals(long userId,String  workFlowCode,boolean canSend);
     WorkFlowWorker   findFirstByUserIdEqualsAndProduceTypeEqualsAndWorkFlowCodeEqualsAndSendEquals(long userId,int produceType,String  workFlowCode,boolean canSend);
     WorkFlowWorker   findFirstByUserIdEqualsAndWorkFlowStepEquals (long userId,int   workFlowStep );
     WorkFlowWorker   findFirstByUserIdEqualsAndProduceTypeEqualsAndWorkFlowStepEquals (long userId,int produceType,int   workFlowStep );
     List<WorkFlowWorker>   findByUserIdEqualsAndWorkFlowStepEquals (long userId,int   workFlowStep );

    List<WorkFlowWorker> findByUserIdEqualsAndSendEquals(long userId, boolean canSend);
    List<WorkFlowWorker> findByWorkFlowStepEqualsAndProduceTypeEqualsAndReceiveEquals(int workFlowStep,int produceType, boolean canReceive);
}
