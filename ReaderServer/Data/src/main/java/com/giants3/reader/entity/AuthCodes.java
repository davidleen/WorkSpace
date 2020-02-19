package com.giants3.reader.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="T_AuthCodes")
public class AuthCodes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String code;
    public int itemIndex;
    public String platform;
}
