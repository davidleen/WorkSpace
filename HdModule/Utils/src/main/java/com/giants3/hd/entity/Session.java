package com.giants3.hd.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 登录状态记录表
 */
@Entity(name="T_Session")
public class Session implements Serializable{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;


    @ManyToOne
    public User user;

    @Basic
    public  String token;
    @Basic
    public long loginTime;
    public String loginTimeString;
    @Basic
    public String loginIp;


    public String client;
    public String device_token;
}
