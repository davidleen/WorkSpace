package com.giants3.hd.sae.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by david on 2016/1/14.
 */
@XmlRootElement(name = "xml")
public class WxTxtMsg implements Serializable{

    @XmlElement(name = "CreateTime")
    public long createTime;

    @XmlElement(name = "MsgType")
    public String msgType="text";
    @XmlElement(name = "FromUserName")
    public String fromUserName;
    @XmlElement(name = "ToUserName")
    public String toUserName;
    @XmlElement(name = "Content")
    public String content;

    @XmlElement(name = "MsgId")
    public long msgId;
}
