package com.giants3.hd.entity;

/**
 * 加工户数据  外厂家数据
 * Created by davidleen29 on 2017/1/14.
 */

public class OutFactory {


    //部门
    public String dep;
    public String name;
    public String manager;
    public String telephone;
    public String address;



    @Override
    public String toString() {
        return dep+" 【"+name+"】";
    }
}
