package com.giants3.hd.sae.controller;


import com.giants3.hd.app.AProduct;
import com.giants3.hd.sae.entity.WxArticleList;
import com.giants3.hd.sae.entity.WxArticleMsg;
import com.giants3.hd.sae.entity.WxMixMsg;
import com.giants3.hd.sae.entity.WxTxtMsg;
import com.giants3.hd.sae.service.WeixinService;
import com.giants3.hd.utils.RemoteData;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
* 产品类别
*/
@Controller
@RequestMapping("/weixin")
public class WeixinController   implements InitializingBean,DisposableBean {



    @Autowired
    WeixinService weixinService;










    @RequestMapping(value="/message", method = {RequestMethod.POST,RequestMethod.GET})
    public
    @ResponseBody
    String message(@RequestParam(value = "signature",required = false  ) String signature,@RequestParam(value = "echostr",required = false ) String echostr,@RequestParam(value="timestamp",required = false) String timestamp,@RequestParam(value="nonce",required = false) String nonce,@RequestBody String weixinMessage)   {

        // 微信加密签名
//        String signature = request.getParameter("signature");
        // 随机字符串
//        String echostr = request.getParameter("echostr");
        // 时间戳
//        String timestamp = request.getParameter("timestamp");
        // 随机数
//        String nonce = request.getParameter("nonce");



        if(!StringUtils.hasLength(weixinMessage)) {



          return  weixinService.checkToken(signature,echostr,timestamp,nonce);

        }else
        {

            //信息处理


            WxTxtMsg msg=   weixinService.handleWxTxt(weixinMessage,WxTxtMsg.class);



       //     String[] content=msg.content.split( "\\s+|,");


            String text;

            if(msg.content.trim().equals("?")){



                msg.content="输入P+货号查询，例（P14B）";
                String from=msg.fromUserName;
                String toUser=msg.toUserName;
                msg.fromUserName=toUser;
                msg.toUserName=from;
                text= weixinService.toXmlString(msg );

            }else if(msg.content.toLowerCase().startsWith("p")){



                text = weixinService.getProduct(msg.content.substring("p".length()).trim(),msg.toUserName,msg.fromUserName);

            }else

           if(msg.content.toLowerCase().startsWith("p")){



                text = weixinService.getProduct(msg.content.substring("p".length()),msg.toUserName,msg.fromUserName);

            }else
            if(msg.content.equals("1"))
            {

                text=weixinService.toXmlString(constructMixMsg(msg.toUserName,msg.fromUserName));
            }else
            {
                msg.content="很好";
                String from=msg.fromUserName;
                String toUser=msg.toUserName;
                msg.fromUserName=toUser;
                msg.toUserName=from;
                text= weixinService.toXmlString(msg );
        }
           return text;
           // return "";


        }




    }



    private  WxMixMsg constructMixMsg(String fromName,String toName)
    {
        WxMixMsg wxMixMsg=new WxMixMsg();
        wxMixMsg.fromUserName=fromName;
        wxMixMsg.toUserName=toName;
        wxMixMsg.articleCount=3;
        wxMixMsg.createTime= Calendar.getInstance().getTimeInMillis();
        wxMixMsg.articles=new WxArticleList();
        wxMixMsg.articles.articles=new ArrayList<WxArticleMsg>(3);

        WxArticleMsg msg=new WxArticleMsg();
        msg.title="title1";
        msg.description="description1";
        msg.picUrl="http://59.56.182.132:8079/Server/api/file/download/material/C01030005/1435911625795.jpg?type=jpg&mClass=0103";
        msg.url="http://59.56.182.132:8079/Server/api/file/download/material/C01030005/1435911625795.jpg?type=jpg&mClass=0103";
        wxMixMsg.articles.articles.add(msg);
          msg=new WxArticleMsg();
        msg.title="title1";
        msg.description="description1";
        msg.picUrl="http://59.56.182.132:8079/Server/api/file/download/material/C01030006/1435911625904.jpg?type=jpg&mClass=0103";
        msg.url="http://www.csdn.net/";
        wxMixMsg.articles.articles.add(msg);

          msg=new WxArticleMsg();
        msg.title="title3";
        msg.description="description3";
        msg.picUrl="http://59.56.182.132:8079/Server/api/file/download/material/C01030013/1435911628310.jpg?type=jpg&mClass=0103";
        msg.url="https://github.com/";
        wxMixMsg.articles.articles.add(msg);

        return wxMixMsg;
    }
    private  void handleMessage(WxTxtMsg wxTxtMsg)
    {





    }



    @RequestMapping(value="/getProduct", method = RequestMethod.GET)
    public
    @ResponseBody
   String getProduct( @RequestParam(value = "name"  ) String prdName )   {

         return   weixinService.getProduct(prdName,"aaaa","bbb");
    }




    @RequestMapping(value="/testEncode", method = RequestMethod.GET)
    public
    @ResponseBody
    String testEncode(  )   {

        String message="<xml>\n" +
                " <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                " <FromUserName><![CDATA[fromUser]]></FromUserName> \n" +
                " <CreateTime>1348831860</CreateTime>\n" +
                " <MsgType><![CDATA[text]]></MsgType>\n" +
                " <Content><![CDATA[this is a test]]></Content>\n" +
                " <MsgId>1234567890123456</MsgId>\n" +
                " </xml>";



        WxTxtMsg msg=   weixinService.handleWxTxt(message,WxTxtMsg.class);
        msg.content="很好";






        String text= weixinService.toXmlString(msg        );


        WxMixMsg wxMixMsg=    constructMixMsg(msg.fromUserName,msg.toUserName);
        text= weixinService.toXmlString(wxMixMsg );



        return text;


    }

    @Override
    public void destroy() throws Exception {
        weixinService.stopFetchTask();
    }

    @Override
    public void afterPropertiesSet() throws Exception {



        weixinService.startFetchToken();

    }
}
