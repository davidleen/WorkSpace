package com.giants3.hd.server.service;

import com.giants3.hd.entity.HdTask;
import com.giants3.hd.entity.HdTaskLog;
import com.giants3.hd.utils.DateFormats;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Date;
import java.util.TimerTask;

/**
 * Created by davidleen29 on 2018/6/6.
 */
public class TransactionTimerTask extends TimerTask {


    private final PlatformTransactionManager transactionManager;
    private final Runnable runnable;

    public TransactionTimerTask(PlatformTransactionManager transactionManager, Runnable runnable)
    {

        this.transactionManager = transactionManager;
        this.runnable = runnable;
    }
    @Override
    public void run() {


        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
// explicitly setting the transaction name is something that can only be done programmatically
        def.setName("syncERP");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus status = transactionManager.getTransaction(def);

        long startTime =System.currentTimeMillis();
        String errorMessage=null;
        try {
            // execute your business logic here
            runnable.run();
            //   Thread.sleep(10000);
            //   throw new RuntimeException("error occur  do on task");
        }
        catch (Throwable ex) {

            errorMessage=ex.getMessage();

        }

        if(errorMessage!=null)
            transactionManager.rollback(status);
        else
            transactionManager.commit(status);


//        status = transactionManager.getTransaction(def);
//        //计算耗时， 以秒为单位
//        long spendTime=   (System.currentTimeMillis()-startTime)/1000;
//
//
//
//        final HdTask hdTask=this.hdTask;
//        //执行次数加1；
//        hdTask.executeCount++;
//
//
//
//        HdTaskLog log=new HdTaskLog();
//
//        log.errorMessage=errorMessage;
//        log.executeTime=startTime;
//        log.executeTimeString= DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(new Date(startTime));
//        log.taskId=hdTask.id;
//        log.taskTypeName=hdTask.taskName;
//        log.timeSpend=spendTime;
//
//
//
//        if(errorMessage==null)
//        {
//            //同步成功
//            log.state=HdTaskLog.STATE_SUCCESS;
//            log.stateName="执行成功！";
//
//        }else{
//            log.errorMessage=errorMessage;
//            log.state=HdTaskLog.STATE_FAIL;
//            log.stateName="执行失败！";
//        }
//
//        taskRepository.save(hdTask);
//        taskLogRepository.save(log);
//        transactionManager.commit(status);
    }
}
