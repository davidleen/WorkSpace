package com.giants3.reader.server.controller;


import com.giants3.reader.entity.WxTxtMsg;
import com.giants3.reader.server.service.WeixinService;
import com.giants3.reader.server.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 产品类别
 */
@Controller
@RequestMapping("/weixin")
public class WeixinController extends BaseController {

    @Value("${filepath}")
    private String productFilePath;


    @Autowired
    private WeixinService weixinService;


    @RequestMapping(value = "/getProduct", method = RequestMethod.GET)
    public
    @ResponseBody
    String getProduct(String message) {

        message = "<xml>\n" +
                " <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                " <FromUserName><![CDATA[fromUser]]></FromUserName> \n" +
                " <CreateTime>1348831860</CreateTime>\n" +
                " <MsgType><![CDATA[text]]></MsgType>\n" +
                " <Content><![CDATA[this is a test]]></Content>\n" +
                " <MsgId>1234567890123456</MsgId>\n" +
                " </xml>";


        WxTxtMsg msg = weixinService.handleWxTxt(message, WxTxtMsg.class);
        msg.content = "很好";

        String[] content = msg.content.split("\\s+|,");


        String filePath = FileUtils.getProductPicturePath(productFilePath, content[0], content.length > 1 ? content[1] : "");

        String text = weixinService.toXmlString(msg);

        return text;


    }


    @RequestMapping(value = "/testEncode", method = RequestMethod.GET)
    public
    @ResponseBody
    String testEncode() {

        String message = "<xml>\n" +
                " <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                " <FromUserName><![CDATA[fromUser]]></FromUserName> \n" +
                " <CreateTime>1348831860</CreateTime>\n" +
                " <MsgType><![CDATA[text]]></MsgType>\n" +
                " <Content><![CDATA[this is a test]]></Content>\n" +
                " <MsgId>1234567890123456</MsgId>\n" +
                " </xml>";


        WxTxtMsg msg = weixinService.handleWxTxt(message, WxTxtMsg.class);
        msg.content = "很好";

        String[] content = msg.content.split("\\s+|,");


        String filePath = FileUtils.getProductPicturePath(productFilePath, content[0], content.length > 1 ? content[1] : "");

        String text = weixinService.toXmlString(msg);

        return text;


    }


}
