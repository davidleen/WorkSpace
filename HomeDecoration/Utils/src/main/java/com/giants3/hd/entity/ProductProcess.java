package com.giants3.hd.entity;

import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.interf.Valuable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 工序表
 */

@Entity(name="T_ProductProcess")
public class ProductProcess implements Serializable,Valuable {


    public static final String XISHUA="洗枪";

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;


    @Basic
    public String code;


    @Basic
    public String name;


    @Basic
    public String memo;


    @Override
    public boolean isEmpty() {


        return StringUtils.isEmpty(code)&&StringUtils.isEmpty(name)&&StringUtils.isEmpty(memo
        );


    }
}
