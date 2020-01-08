package com.giants3.hd.entity;

import javax.persistence.*;

/**
 * 公式定义  定额计算公式
 */
@Entity(name="T_MaterialEquation")
public class MaterialEquation {


    /**
     * 流程id
     */
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;


    /**
     * 公式编号
     */
    @Basic
    public int equationId;


    /**
     * 公式描述
     */
    @Basic
    public String equationName;


    @Override
    public String toString() {
        return
                " [" +equationId+"]    "+ equationName  ;
    }
}
