package com.giants3.hd.entity;


import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by davidleen29 on 2015/8/14.
 */
@Entity(name="T_AppVersion")
public class AppVersion implements Serializable{


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;


    @Basic
    public String versionName;

    @Basic
    public int versionCode;

    @Basic
    public String memo ;

    @Basic
    public long updateTime;
    @Basic
    public String appName  ;

    @Basic
    public long fileSize;
    public String timeString;
}
