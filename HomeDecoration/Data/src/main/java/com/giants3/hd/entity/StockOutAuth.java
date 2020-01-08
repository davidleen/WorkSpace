package com.giants3.hd.entity;

import com.giants3.hd.utils.interf.Valuable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 报价权限
 */
@Entity(name="T_StockOutAuth")
public class StockOutAuth implements Serializable,Valuable{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;




    @ManyToOne
    public User user;
    /**
     * 是否可以查看FOB单价
     */
    public boolean fobVisible;


    /**
     * 可以查看的业务员的编码  字符串组， 以 逗号隔开。
     */
    public String relatedSales;



    @Override
    public boolean isEmpty() {
        return user==null;
    }
}
