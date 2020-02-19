package com.giants3.reader.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by davidleen29 on 2018/5/13.
 */
@Entity(name = "T_Settings")
public class Settings {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;


    /**
     * 几率， 【0，100】
     */
    public int rate;
}
