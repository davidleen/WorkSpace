package com.giants3.hd.entity;


import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by davidleen29 on 2015/8/14.
 */
@Entity(name="T_Company")
public class Company implements Serializable{


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;


    public String name;
    public String eName;
    public String address;
    public String eAddress;
    public String tel;
    public String fax;
    public String email;


    /**
     * 公司展位号
     */
    public String boothNo;

}
