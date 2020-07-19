package com.giants3.reader.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "T_CodeAnalytics")
public class CodeAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    /**
     * 请求次数
     */
    public int requestTime;
    /**
     * 日期 2020-01-01
     */
    public String dateString;

}
