package com.giants3.hd.server.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 读取android apk 版本号 名
 * Created by davidleen29 on 2016/10/16.
 */
@XmlRootElement(name="manifest")
public class AndroidManifest {


    @XmlAttribute
    public String versionName;

    @XmlAttribute
    public int versionCode;
}
