package com.giants3.hd.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 产品公式更新，当开启时候， 所有产品的id保存到这个表上。
 * 更新完成一款，则删除这个表中的这个id
 *
 * 一般情况下 这个表 记录为空
 * Created by davidleen29 on 2017/3/12.
 */
@Entity(name = "T_ProductEquationUpdateTemp")
public class ProductEquationUpdateTemp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public long productId;
}
