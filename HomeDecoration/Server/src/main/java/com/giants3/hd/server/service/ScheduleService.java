package com.giants3.hd.server.service;

import com.giants3.hd.entity.ErpWorkFlow;
import com.giants3.hd.entity.ErpWorkFlowReport;
import com.giants3.hd.entity.HdTask;
import com.giants3.hd.entity.HdTaskLog;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.controller.MaterialController;
import com.giants3.hd.server.interf.Job;
import com.giants3.hd.server.repository.ErpWorkFlowReportRepository;
import com.giants3.hd.server.repository.QuotationRepository;
import com.giants3.hd.server.repository.TaskLogRepository;
import com.giants3.hd.server.repository.TaskRepository;
import com.giants3.hd.utils.DateFormats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by davidleen29 on 2016/8/15.
 */
@Service
public class ScheduleService extends AbstractService {
    private final AtomicInteger counter = new AtomicInteger();
    @Autowired
    private QuotationRepository quotationRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    ErpService erpService;

    @Autowired
    ErpWorkService erpWorkService;


    @Autowired
    TaskLogRepository taskLogRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    Job taskJob;


    @Autowired
    ErpWorkFlowReportRepository erpWorkFlowReportRepository;
    /**
     * 更新附件路径  定时将附件
     */
    // @Scheduled(fixedDelay = 5000)
    public void test() {
        System.out.println("processing next 10 at " + new Date());
        for (int i = 0; i < 10; i++) {
            taskJob.doTask(counter.incrementAndGet());
        }

    }

    /**
     * 每天4点触发 更新附件图片
     */
    //@Scheduled(fixedDelay = 50000)
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateAttaches() {


        executeTask(HdTask.NAME_UPDATE_ATTACH, new Runnable() {
            @Override
            public void run() {
                productService.updateAttachFiles();
                erpService.updateAttachFiles();
            }
        });


    }

//    /**
//     * 定时一分钟触发一次
//     */
//    @Scheduled(fixedDelay = 60000)
//    public void test1() {
//      //  updateAttaches();
//
//    }

    /**
     * 每日4点执行 未完成货款的进度数据的更新
     */
    @Scheduled(cron = "0 30 1 * * ?")
    public void updateOrderItemWorkFlowState() {
        executeTask(HdTask.NAME_UPDATE_WORK_FLOW_STATE, new Runnable() {
            @Override
            public void run() {
                erpWorkService.updateAllProducingWorkFlowReports();
            }
        });


    }
    /**
     * 每日4点执行 未完成货款的进度数据的更新
     */
//
    @Scheduled(fixedDelay = 2*60*60*1000l)
//  @Scheduled(fixedDelay =  60*1000l)
//    @Scheduled(cron = "0 0/120 9-17 * * ?")
//    @Scheduled(fixedDelay = 20*1000l)
    public void autoCompleteWorkFlowFromErp() {
        executeTask(HdTask.NAME_AUTO_COMPLETE_ERP_STEP, new Runnable() {
            @Override
            public void run() {
                int i = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                if(i>7&&i<21) {
                    erpWorkService.autoCompleteSelfMadeStockIn();
                    erpWorkService.autoCompletePurchaseStockIn();
                   erpWorkService.autoCompleteStockOut();


                }
            }
        });


    }

    /**
     * 执行任务， 统计任务耗时
     *
     * @param taskName
     * @param executeCode
     */
    private void executeTask(String taskName, Runnable executeCode) {

        HdTask task = taskRepository.findFirstByTaskNameEquals(taskName);
        if (task == null) return;
        if (task.state == HdTask.STATE_PAUSED) return;

        logger.info("执行任务======"+taskName);

        task.executeCount++;
        task = taskRepository.save(task);


        HdTaskLog taskLog = new HdTaskLog();
        taskLog.taskTypeName = task.taskName;
        taskLog.taskId = task.id;
        taskLog.taskTypeName = task.taskName;
        taskLog.state = HdTaskLog.STATE_SUCCESS;
        taskLog.stateName = "执行成功！";
        long time = System.currentTimeMillis();
        taskLog.executeTime = time;
        taskLog.executeTimeString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(new Date(time));
        try {


            executeCode.run();

        } catch (Throwable t) {


            logger.info("执行失败======"+taskName);
            logger.error(this, t);
            taskLog.stateName = "执行失败！";
            taskLog.state = HdTaskLog.STATE_FAIL;
            taskLog.errorMessage = t.getMessage();
        }

        //计算耗时， 以秒为单位
        taskLog.timeSpend = (System.currentTimeMillis() - time) / 1000;
        taskLogRepository.save(taskLog);

    }

    /**
     * 每日2 点执行  同步与erp数据库的材料
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void syncERP() {


        executeTask(HdTask.NAME_SYNC_ERP, new Runnable() {
            @Override
            public void run() {
                erpService.syncErpMaterial();
            }
        });


    }

    public void initTask() {


        createTask(HdTask.TYPE_UPDATE_WORK_FLOW_STATE, HdTask.NAME_UPDATE_WORK_FLOW_STATE, "每天凌晨一点半", "系统", 0, "定时更新在产货款的进度状态");
        createTask(HdTask.TYPE_UPDATE_ATTACH, HdTask.NAME_UPDATE_ATTACH, "每天凌晨一点", "系统", 0, "附件上传在临时文件夹，定时整理数据");
        createTask(HdTask.TYPE_SYNC_ERP, HdTask.NAME_SYNC_ERP, "每天凌晨二点", "系统", 0, "erp材料数据单价改变同步到报价系统");
        createTask(HdTask.TYPE_AUTO_COMPLETE_ERP_STEP, HdTask.NAME_AUTO_COMPLETE_ERP_STEP, "每2小时自动更新", "系统", 0, "自动抓取erp数据，完成组装包装，成品出库流程");

    }

    public void createTask(int taskType, String taskName, String executeTime, String activator, int repeatCount, String memo)

    {

        HdTask task = taskRepository.findFirstByTaskNameEquals(taskName);


        if (task == null) {
            task = new HdTask();
            task.activateTime = executeTime;
            task.activator = activator;
            task.repeatCount = repeatCount;
            task.memo = memo;
            task.startDate = System.currentTimeMillis();
            task.dateString = DateFormats.FORMAT_YYYY_MM_DD_HH_MM.format(new Date(task.startDate));
            task.taskName = taskName;
            task.taskType = taskType;
            task.executeCount = 0;
            taskRepository.save(task);
            return;

        }


    }


    public RemoteData<Void> executeTask(int taskType) {

        switch (taskType)
        {
            case HdTask.TYPE_SYNC_ERP:
                syncERP();
                break;
            case HdTask.TYPE_UPDATE_ATTACH:
                updateAttaches();
                break;
            case HdTask.TYPE_UPDATE_WORK_FLOW_STATE:
                    updateOrderItemWorkFlowState();
                break;
            case HdTask.TYPE_AUTO_COMPLETE_ERP_STEP:
                    autoCompleteWorkFlowFromErp();
                break;

        }
        return wrapData();

    }



    public RemoteData<HdTask> list(int pageIndex, int pageSize) {


        Pageable pageable = constructPageSpecification(pageIndex, pageSize);
        Page<HdTask> pageValue = taskRepository.findByTaskNameLikeOrderByStartDateDesc("%%", pageable);

        List<HdTask> haTasks = pageValue.getContent();


        return wrapData(pageIndex, pageable.getPageSize(), pageValue.getTotalPages(), (int) pageValue.getTotalElements(), haTasks);
    }

    @Transactional
    public RemoteData<HdTask> updateState(long taskId, int taskState) {

        HdTask task = taskRepository.findOne(taskId);

        if (task == null) return wrapError("未找到id=" + taskId + "的任务");
        task.state = taskState;

        taskRepository.save(task);
        return list(0, 100);
    }
}
