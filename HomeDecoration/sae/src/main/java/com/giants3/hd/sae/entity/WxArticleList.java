package com.giants3.hd.sae.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by david on 2016/1/18.
 */


@XmlRootElement(name = "Articles")
public class WxArticleList {


    @XmlElement(name = "item")
  public List<WxArticleMsg> articles;
}
