package com.giants3.hd.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 加工户产值配置
 */


@Entity(name="T_ProducerValueConfig")
public class ProducerValueConfig   implements Serializable {

    /**
     * 类别id
     */
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;
    public String dept;
    public String name;
    //最大限制产值
    public  double  maxOutputValue;
    /**
     * 最低产值配置
     */
    public  double  minOutputValue;

    /**
     * 工人數量
     */
    public int workerNum;


    @Basic
    @Lob
    public String memo;
}
