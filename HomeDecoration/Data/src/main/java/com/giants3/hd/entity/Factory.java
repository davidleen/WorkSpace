package com.giants3.hd.entity;

import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.interf.Valuable;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * 厂家信息
 * Created by david on 2015/10/25.
 */

@Entity(name = "T_Factory")
public class Factory implements Serializable,Valuable {

    //内厂
    public static final String CODE_LOCAl = "001";
    //外厂
    public static final String CODE_OUT = "002";
    /**
     *  id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;


    /**
     * 厂家编号， 本厂000
     */
    @Basic
    public String code;
    @Basic
    public String name;


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
