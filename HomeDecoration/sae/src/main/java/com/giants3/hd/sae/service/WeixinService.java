package com.giants3.hd.sae.service;


import com.giants3.hd.sae.api.ApiManager;
import com.giants3.hd.sae.api.HttpUrl;
import com.giants3.hd.sae.entity.*;
import com.giants3.hd.sae.entity.json.WxMixSend;
import com.giants3.hd.utils.ConstantData;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.exception.HdException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by david on 2016/1/16.
 */
@Service
public class WeixinService implements InitializingBean,DisposableBean {






    @Autowired
    ApiManager apiManager;
    private Timer timer=new Timer();
    private Random random=new Random();
    private WxToken currentToken;

    public   <T>  T handleWxTxt(String message,Class<T> tClass)
    {


        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(tClass);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            StringReader reader = new StringReader(message);
            T person = (T) unmarshaller.unmarshal(reader);
            return person;

        } catch (JAXBException e) {
            e.printStackTrace();
        }



        return  null;
    }


    public   <T> String toXmlString(T object)
    {


        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(object.getClass());
            Marshaller unmarshaller = jaxbContext.createMarshaller();

            StringWriter writer = new StringWriter( );
             unmarshaller.marshal(object,writer);
            return writer.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }



        return  null;
    }


    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public void startFetchToken() {


        try {
            currentToken=apiManager.fetchToken(ConstantData.WX_APP_ID,ConstantData.WX_APP_SECRET);

        } catch (HdException e) {
            currentToken=null;
            e.printStackTrace();
        }

        Logger.getLogger(WeixinService.class.getName()).info("currentToken:"+String.valueOf(currentToken));

        scheduleFetchTask((currentToken==null?1:(currentToken.expires_in-100))*1000l);



    }

    public String checkToken(String signature, String echostr, String timestamp, String nonce) {



        //空消息 验证接口
        // SHA1加密
        String tmpStr = null;
        try {
            tmpStr = getSHA1(ConstantData.WX_TOKEN, timestamp, nonce);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        // 确认请求来至微信
        if (signature.equals(tmpStr)) {
            return echostr;
        }else
        {
            return "";
        }
    }

    public void stopFetchTask() {


        if(timer!=null)
        {
            timer.cancel();
            timer=null;
        }

    }


    /**
     * 延后回复用户信息
     * @param msg
     */
    public void replyLater( final WxTxtMsg msg) {



        new Thread()
        {
            @Override
            public void run() {

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                WxMixSend send=    WxMixSend.objectFromData(WxMixSend.testString);



                send.touser=msg.fromUserName;
                while (currentToken==null)
                {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    apiManager.sendMessage(GsonUtils.toJson(send),currentToken.access_token);
                } catch (HdException e) {
                    e.printStackTrace();
                }

            }



        }.start();

    }

    /**
     * 获取
     * @param product
     * @return
     */
    public String getProduct(String product,String fromName,String toName) {


        List<AProduct> products =null;
        String message=null;
        try {
             products= apiManager.getProductList(product  );




        } catch (HdException e) {
            e.printStackTrace();
            message=e.message;
        }
            if(products==null||products.size()==0)
        {
            WxTxtMsg msg=new WxTxtMsg();
            msg.content=message==null?("未查询到数据"):("读取数据失败:"+message);
            msg.fromUserName=fromName;
            msg.toUserName=toName;
            msg.msgId=random.nextLong();
            msg.createTime=Calendar.getInstance().getTimeInMillis();
          return  toXmlString(msg );


        }else
        {
            final int size = products.size();
            WxMixMsg wxMixMsg=new WxMixMsg();
            wxMixMsg.fromUserName=fromName;
            wxMixMsg.toUserName=toName;
            wxMixMsg.articleCount=size>10?10:size;
            wxMixMsg.createTime= Calendar.getInstance().getTimeInMillis();
            wxMixMsg.articles=new WxArticleList();
            List<WxArticleMsg> articles=new ArrayList<WxArticleMsg>(wxMixMsg.articleCount);
            wxMixMsg.articles.articles=articles;


            for(int i = 0; i< size; i++)
            {
                if(i>=10) break;
                AProduct aProduct=products.get(i);

                WxArticleMsg msg=new WxArticleMsg();
                msg.title=aProduct.name+ (StringUtils.isEmpty(aProduct.pVersion)?"":("-"+aProduct.pVersion));
                msg.description=aProduct.pClassName+"        单位：" +aProduct.pUnitName;
                msg.picUrl= HttpUrl.URL_APP_BASE+aProduct.url;
                msg.url=HttpUrl.URL_APP_BASE+aProduct.url;
                articles.add(msg);






            }

           return  toXmlString(wxMixMsg);



        }







    }


    private static class FetchTokenTask extends TimerTask
    {
        WeakReference<WeixinService> ref;
        public  FetchTokenTask(WeixinService service)
        {

            ref=new WeakReference<WeixinService>(service);

        }
        @Override
        public void run() {

            WeixinService service=ref.get();

            if(service==null) return ;

            service.startFetchToken();



        }
    }

    private void scheduleFetchTask(long timeDelay) {
        timer.schedule(new FetchTokenTask(this),timeDelay);
    }


    /**
     * 用SHA1算法生成安全签名
     * @param token 票据
     * @param timestamp 时间戳
     * @param nonce 随机字符串
     * @return 安全签名
     * @throws NoSuchAlgorithmException
     */
    public  String getSHA1(String token, String timestamp, String nonce) throws NoSuchAlgorithmException {
        String[] array = new String[] { token, timestamp, nonce };
        StringBuffer sb = new StringBuffer();
        // 字符串排序
        Arrays.sort(array);
        for (int i = 0; i < 3; i++) {
            sb.append(array[i]);
        }
        String str = sb.toString();
        // SHA1签名生成
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(str.getBytes());
        byte[] digest = md.digest();

        StringBuffer hexstr = new StringBuffer();
        String shaHex = "";
        for (int i = 0; i < digest.length; i++) {
            shaHex = Integer.toHexString(digest[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexstr.append(0);
            }
            hexstr.append(shaHex);
        }
        return hexstr.toString();
    }
}
