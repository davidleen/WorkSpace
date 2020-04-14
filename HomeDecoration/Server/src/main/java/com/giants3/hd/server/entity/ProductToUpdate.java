package com.giants3.hd.server.entity;

import javax.persistence.*;

/**   需要进行更新的统计数据产品ids 的表
 * Created by david on 2016/4/17.
 */
@Entity(name="T_ProductToUpdate")
@Table(
        indexes = {@Index(name = "productIdToUpdate", columnList = "productId", unique = false)})
public class ProductToUpdate {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public long id;


    public long productId;
    public String updateWay;
}
