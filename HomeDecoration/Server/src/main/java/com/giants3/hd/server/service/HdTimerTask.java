package com.giants3.hd.server.service;

import com.giants3.hd.entity.HdTask;
import com.giants3.hd.entity.HdTaskLog;
import com.giants3.hd.server.repository.TaskLogRepository;
import com.giants3.hd.server.repository.TaskRepository;
import com.giants3.hd.utils.DateFormats;
import com.giants3.hd.utils.ObjectUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by david on 2015/12/10.
 */
public class HdTimerTask extends TimerTask {

    public HdTask hdTask;
    private PlatformTransactionManager transactionManager;
    private MaterialService materialService;
    private TaskRepository taskRepository;
    private TaskLogRepository taskLogRepository;


    private List<HdTimerTask> taskList;
    private Timer timer;

    public HdTimerTask(HdTask hdTask, PlatformTransactionManager transactionManager, MaterialService materialService, TaskLogRepository taskLogRepository, List<HdTimerTask> taskList, TaskRepository taskRepository, Timer timer) {
        this.hdTask = hdTask;
        this.transactionManager = transactionManager;
        this.materialService = materialService;
        this.taskLogRepository = taskLogRepository;
        this.taskList = taskList;
        this.taskRepository = taskRepository;
        this.timer = timer;
    }

    @Override
    public void run() {

        taskList.remove(this);
        if (hdTask.taskType != HdTask.TYPE_SYNC_ERP) {

            // only support for syncERP task for now

            return;
        }

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
// explicitly setting the transaction name is something that can only be done programmatically
        def.setName("syncERP");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus status = transactionManager.getTransaction(def);

        long startTime = System.currentTimeMillis();
        String errorMessage = null;
        try {
            // execute your business logic here
            materialService.syncERP();
            //   Thread.sleep(10000);
            //   throw new RuntimeException("error occur  do on task");
        } catch (Throwable ex) {

            errorMessage = ex.getMessage();

        }

        if (errorMessage != null)
            transactionManager.rollback(status);
        else
            transactionManager.commit(status);


        status = transactionManager.getTransaction(def);


        //计算耗时， 以秒为单位
        long spendTime = (System.currentTimeMillis() - startTime) / 1000;


        final HdTask hdTask = this.hdTask;
        //执行次数加1；
        hdTask.executeCount++;


        HdTaskLog log = new HdTaskLog();

        log.errorMessage = errorMessage;
        log.executeTime = startTime;
        log.executeTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(new Date(startTime));
        log.taskId = hdTask.id;
        log.taskTypeName = hdTask.taskName;
        log.timeSpend = spendTime;


        if (errorMessage == null) {
            //同步成功
            log.state = HdTaskLog.STATE_SUCCESS;
            log.stateName = "执行成功！";

        } else {
            log.errorMessage = errorMessage;
            log.state = HdTaskLog.STATE_FAIL;
            log.stateName = "执行失败！";
        }

        taskRepository.save(hdTask);
        taskLogRepository.save(log);


        transactionManager.commit(status);


        taskList.remove(this);
        //重复次数  每日  或者固定重复次数
        if (hdTask.repeatCount > hdTask.executeCount) {

            long timeInADay = 24l * 60 * 60 * 1000;
            HdTask newHhdTask = (HdTask) ObjectUtils.deepCopy(hdTask);
            newHhdTask.dateString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(new Date(startTime + timeInADay));
            HdTimerTask hdTimerTask = new HdTimerTask(hdTask, transactionManager, materialService, taskLogRepository, taskList, taskRepository, timer);
            timer.schedule(hdTimerTask, new Date(startTime + timeInADay));

        }


    }
}
