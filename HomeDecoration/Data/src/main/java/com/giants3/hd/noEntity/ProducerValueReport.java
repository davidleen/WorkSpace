package com.giants3.hd.noEntity;

import com.giants3.hd.entity.ProducerValueConfig;

import java.io.Serializable;

/***
 * 加工户在产产值报表。
 */
public class ProducerValueReport implements Serializable {
    public String dept;
    public String name;
    //最大限制产值
    public double maxOutputValue;
    /**
     * 最低产值配置
     */
    public double minOutputValue;


    public double workingValue;
    public int workerNum;


}
