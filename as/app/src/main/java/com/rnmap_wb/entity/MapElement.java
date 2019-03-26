package com.rnmap_wb.entity;

import java.util.UUID;

public class MapElement {
   public  String uuid=UUID.randomUUID().toString();

    /**
     * 标注
     */
   public  static final int TYPE_MARKER=1;
    /**
     * 线段
     */
   public  static final int TYPE_POLYLINE=2;
    /**
     * 多边形(n>=3)
     */
   public  static final int TYPE_POLYGON=3;
    /**
     * 圆形
     */
   public  static final int TYPE_CIRCLE=4;
    /**
     * 测量线段距离。 每个线段点上显示跟上一个点的距离
     */
   public  static final int TYPE_MAPPING_LINE=5;
    /**
     * 测量角度线条  线段上的夹角显示度数
     */
   public  static final int TYPE_MAPPING_LINE_DEGREE=6;
    /**
     * 轨迹
     */
   public  static final int TYPE_TRACK_LINE=7;


    public static final String PICTURE_REGEX = ";";


    /**
     * kml上的marker类型。
     */
   public  static final int TYPE_KML_MARK=11;
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
     * 备注
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
