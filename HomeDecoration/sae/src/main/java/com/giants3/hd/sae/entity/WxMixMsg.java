package com.giants3.hd.sae.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by david on 2016/1/18.
 */
@XmlRootElement(name = "xml")
public class WxMixMsg {


    @XmlElement(name = "CreateTime")
    public long createTime;

    @XmlElement(name = "MsgType")
    public String msgType="news";
    @XmlElement(name = "FromUserName")
    public String fromUserName;
    @XmlElement(name = "ToUserName")
    public String toUserName;


    @XmlElement(name = "ArticleCount")
    public int articleCount;
    @XmlElement(name="Articles")
    public WxArticleList articles;



}
