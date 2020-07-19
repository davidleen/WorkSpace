package com.giants3.hd.server.service_third;

import com.giants3.hd.entity.ErpWorkFlow;
import com.giants3.hd.entity.Session;
import com.giants3.hd.entity.WorkFlowMessage;
import com.giants3.hd.entity.WorkFlowWorker;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.noEntity.app.PushMessage;
import com.giants3.hd.server.repository.SessionRepository;
import com.giants3.hd.server.repository.UserRepository;
import com.giants3.hd.server.repository.WorkFlowWorkerRepository;
import com.giants3.hd.server.service.AbstractService;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by davidleen29 on 2018/6/26.
 */
@Service
public class MessagePushService extends AbstractService {
    public static final String ANDROID = "ANDROID";
    @Autowired
    WorkFlowWorkerRepository workFlowWorkerRepository;
    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    PushService pushService;
    @Autowired
    private UserRepository userRepository;

    /**
     * 消息通知处理， 找出负责这个消息的推送
     * @param message
     */
    public void pushMessage(WorkFlowMessage message) {
        String combineTokens;

        //找出消息的负责人，

        final List<WorkFlowWorker> workers = workFlowWorkerRepository.findByWorkFlowStepEqualsAndProduceTypeEqualsAndReceiveEquals(message.toFlowStep, message.produceType, true);

        final List<WorkFlowWorker> selectWorkers =new ArrayList<>();
        if(message.toFlowStep== ErpWorkFlow.STEP_PEITI||message.toFlowStep==ErpWorkFlow.STEP_YANSE)
        {

            //a.mrpType='T' or (a.mrpType<>'M' and a.mrpType<>'T' and a.prdType='TJ')  铁件类型判断
            //a.mrpType='M' or (a.mrpType<>'M' and a.mrpType<>'T' and a.prdType='MJ')  木件类型判断
            //优先判断mrpType，当mrpType 未设置（非M T） 时候 判断TJ MJ
            boolean tiejian=message.mrpType=="T" || (message.mrpType!="M" && message.mrpType!="T" && message.prdType=="TJ");
            boolean mujian=message.mrpType=="M" || (message.mrpType!="M" && message.mrpType!="T" && message.prdType=="MJ");
            //这两个流程要区分铁木
            for(WorkFlowWorker worker:workers)
            {
                if(worker.tie&&tiejian)
                {
                    selectWorkers.add(worker);
                }else
                if(worker.mu&&mujian)
                {
                    selectWorkers.add(worker);
                }

                if(!tiejian&&!mujian)
                {
                    selectWorkers.add(worker);
                }
            }



        }else
        {
            selectWorkers.addAll(workers);
        }


        List<String> tokens=new ArrayList<>();

        List<String> workerString=new ArrayList<>();
        for(WorkFlowWorker workFlowWorker:selectWorkers)
        {
            Session session=      sessionRepository.findFirstByUserIdEqualsAndClientEqualsOrderByLoginTimeDesc(workFlowWorker.userId, ANDROID);
            if(session!=null&&!StringUtils.isEmpty(session.device_token))
            {
                tokens.add(session.device_token);
                workerString.add(workFlowWorker.userName);
            }
        }


        if (tokens.size()>0)
        {

            combineTokens = StringUtils.combine(tokens,StringUtils.STRING_SPLIT_COMMA);

            logger.info("setting message"+message.orderName+message.productName+" to "+StringUtils.combine(workerString)+",deviceToken:"+combineTokens);
            sendMessageToDevice(combineTokens, message);

        }
    }

    public void sendMessageToDevice(String combineTokens, WorkFlowMessage message) {


        long time= Calendar.getInstance().getTimeInMillis();
       boolean isOVerTime=time-message.createTime>24l*60*60*1000;


        PushMessage pushMessage=new PushMessage();
        pushMessage.custom=String.valueOf(message.id);
        pushMessage.messageType=isOVerTime?PushMessage.TYPE_MESSAGE_RECEIVE_ALERT:PushMessage.TYPE_MESSAGE_RECEIVE;
        pushMessage.title="待办任务";
        pushMessage.content=(isOVerTime?"你有一条超过24小时未处理的待办任务":"你有新的待办任务")+"，订单："+message.orderName+",货号:"+message.productName+",来自流程:"+message.fromFlowName+","+message.senderName+"的交接单";

        pushMessage.icon=message.thumbnail;


        if(isOVerTime)
        {
            pushMessage.ticker="你有一条超过24小时未处理的待办任务";

        }else
             pushMessage.ticker="你有一条新的待办任务";
        sendMessage(pushMessage,combineTokens);
    }



    public void  sendMessage(PushMessage pushMessage,String tokens)
    {

        pushService.sendUnicastMessage(GsonUtils.toJson(pushMessage), tokens);

    }


    public RemoteData<Void> sendMessageToUser(long userId, String message)
    {

        Session session=sessionRepository.findFirstByUserIdEqualsAndClientEqualsOrderByLoginTimeDesc(userId,ANDROID);
        if(session!=null)
        {

            PushMessage pushMessage=new PushMessage();
            pushMessage.custom=String.valueOf(message.hashCode());
            pushMessage.messageType=0;
            pushMessage.title="测试消息";
            pushMessage.content=StringUtils.isEmpty(message)?"xxxxxxxxxxxx":message;
            sendMessage(pushMessage,session.device_token);

            return wrapData();

        }

        return wrapError("session:"+session);





    }
}
