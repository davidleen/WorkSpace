package com.giants3.hd.entity;

import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.interf.Valuable;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * 客户表
 * Created by davidleen29 on 2015/7/1.
 */
@Entity(name = "T_Customer")
public class Customer implements Serializable ,Valuable{
    public static final String CODE_TEMP = "000";
    /**
     * 单位 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;




    public String code;

    public String name;
    public String tel;
    public String fax;

    public String addr;
    public   String nation;
    public Customer()
    {}

    public Customer(String code, String name, String tel) {
        this.code = code;
        this.name = name;
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "["+code+"]"+name;
    }

    @Override
    public boolean isEmpty() {
        return StringUtils.isEmpty(code)&&StringUtils.isEmpty(name
        );
    }
}
