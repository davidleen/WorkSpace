package com.giants3.hd.sae.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by david on 2016/1/18.
 */


@XmlRootElement(name = "Article")
public class WxArticleMsg {


    @XmlElement(name = "Title")
    public String title;
    @XmlElement(name = "Description")
    public String description;
    @XmlElement(name = "PicUrl")
    public String picUrl;
    @XmlElement(name = "Url")
    public String url;
}
