package com.giants3.hd.app;

import com.giants3.hd.entity.Authority;
import com.giants3.hd.entity.QuoteAuth;

import java.io.Serializable;
import java.util.List;

/**
 * Created by david on 2016/1/2.
 */
public class AUser   implements Serializable {



    public long id;


    public String code;


    public String name;


    public String chineseName;




    public boolean isSalesman;



    public String  email;


    public String  tel;
    public String token;

    public String positionName;
    public int position;


    public List<Authority> authorities;
    public QuoteAuth quoteAuth;


}
