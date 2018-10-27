package com.giants3.lanvideo.server.controller;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * 应用程序启动初始化
 * Created by davidleen29 on 2015/8/7.
 */
@Component
public class InitData implements ApplicationListener<ContextRefreshedEvent> {


    private static final Logger logger = Logger.getLogger(InitData.class);
    private static boolean isStart = false;


//
//    @Autowired
//    TestXmlRepository testXmlRepository;


    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!isStart) {

            final String applicationName = event.getApplicationContext().getApplicationName();

            try {
                InetAddress addr = InetAddress.getLocalHost();
                String ip = addr.getHostAddress().toString(); //获取本机ip

                logger.info("ip:" + ip);


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        System.out.println("spring 容器初始化完毕================================================");

    }


}
