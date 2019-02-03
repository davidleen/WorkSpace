package com.rnmap_wb.entity;

import java.util.UUID;

public class MapElement {
   public  String uuid=UUID.randomUUID().toString();

   public  static final int TYPE_MARKER=1;
   public  static final int TYPE_POLYLINE=2;
   public  static final int TYPE_POLYGON=3;
   public  static final int TYPE_CIRCLE=4;
    /**
     * 标记点 1 线 2 多边形 3
     */
    public int type;


    /**
     *半径  type=4时候 这个字段必须有数据
     */
    public double radius;

    /**
     * 经纬度值  分号  多个经纬度值隔开，逗号 隔开经度 维度。
     */
    public String latLngs;

    /**
     * 名称  类型标记时候有数据
     */
    public String name;
  /**
     * 备注  类型标记时候有数据
     */
    public String memo;

    /**
     * 图片地址，  ;号隔开不同地址。最多三张
     */
    public String picture;
    /**
     * 图片本地文件地址，当这个有数据时候表示未上传图片，没有生成url;
     */
    public String filePath;



}