package com.giants3.hd.server.controller;

import com.giants3.hd.entity.GlobalData;
import com.giants3.hd.server.service.*;
import com.giants3.report.ResourceUrl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 应用程序启动初始化
 * Created by davidleen29 on 2015/8/7.
 */
@Component
public class InitData implements ApplicationListener<ContextRefreshedEvent> {


    private static final Logger logger = Logger.getLogger(InitData.class);
    private static boolean isStart = false;


    @Autowired
    GlobalDataService globalDataService;

    @Autowired
    AuthorityService authorityService;


    @Autowired
    SettingService settingService;


    @Autowired
    WorkFlowService workFlowService;
    @Autowired
    TableRestoreService tableRestoreService;
    @Autowired
    UserService userService;

    @Autowired
    ScheduleService scheduleService;


    @Autowired
    AppService appService;

    @Autowired
    ErpWorkService erpWorkService;


    @Autowired
    MaterialService materialService;
    @Value("${PICTURE_BASE_URL}")
    private String PICTURE_BASE_URL;

    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!isStart) {

            final String applicationName = event.getApplicationContext().getApplicationName();

//            ResourceUrl.setBaseUrl("http://127.0.0.1/" + applicationName + "/");
            ResourceUrl.setBaseUrl(PICTURE_BASE_URL);

//
//            if(true)
//            return;

            //logs debug message
            if (logger.isDebugEnabled()) {
                logger.debug("getWelcome is executed!");
            }
//
////            tableRestoreService.restoreTable();
//            if (serverVersion== TargetVersion.VERSION_RESTORE_PACK_FROM_QUOTATION)
//            {
//
//                tableRestoreService.restoreOutFactoryProductPackInfoFromQuotation(globalDataService.findCurrentGlobalData());
//            }


            appService.updateModuleIfNeeded();


            appService.checkVersions();


            //检查全局项目数据 不存在 则构建一份数据（使用默认值）
            GlobalData globalData = globalDataService.findCurrentGlobalData();
            if (globalData == null) {
                globalData = new GlobalData();
                globalDataService.save(globalData);
            }


            workFlowService.initData();
            settingService.initCompany();

            userService.adjustPasswordToMd5();


            workFlowService.initWorkFlowLimit();

            workFlowService.adjustWorkFlowMessage();

            scheduleService.initTask();


             authorityService.initAppQuoteAuths();
            authorityService.initQuoteAuths();

//            erpWorkService.updateWorkFlowReportSendingQty();
            //erpWorkService.correctAllWorkFlowReportData();
//          erpWorkService.updateAllProducingWorkFlowReports();


        }


        //开启检查是否有未完成的产品统计数据计算
        materialService.updateProductData();


        isStart = true;


     //   pushService.sendBroadcastMessage();

        System.out.println("spring 容器初始化完毕================================================");

    }


}
