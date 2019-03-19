package com.giants3.hd.entity.app;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by davidleen29 on 2019/3/18.
 */
@Entity(name = "T_PushErrorReport")
public class PushErrorReport {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String message;
    public String code;
    public int status;

}
