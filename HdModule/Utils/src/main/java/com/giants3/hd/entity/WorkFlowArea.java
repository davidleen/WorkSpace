package com.giants3.hd.entity;

import javax.persistence.*;

/**
 * 交接区域数据
 * Created by davidleen29 on 2017/5/25.
 */
@Entity(name = "T_WorkFlowArea")
public class  WorkFlowArea {


    /**
     * 单位 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    //编号
    public String code;
    //名称
    public String name;
    //描述
    public String description;



    @Override
    public String toString() {
        return code+name;
    }
}
