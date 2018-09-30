package com.giants3.hd.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 流程跳转确认信
 * <p/>
 * Created by davidleen29 on 2016/9/3.
 */
@Entity(name = "T_WorkFlowMessage")
public class WorkFlowMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;


    public int fromFlowStep;
    public String fromFlowName;
    public String fromFlowCode;


    /**
     * 接受流程id
     */
    public int toFlowStep;
    /**
     *
     */
    public String toFlowName;
    public String toFlowCode;


    /**
     * 订单数量
     */
    public int orderItemQty;


    public String orderName;

    /**
     * 产品在订单中序号
     */
    public int itm;
    public String productName;
    public String pVersion;
    /**
     * 排厂单号
     */
    public String mrpNo;


    /**
     * 移交数量
     */
    public int transportQty;


    /**
     * 消息内容
     */
    public String name;


    /**
     * 当前状态  0 未处理  1 已经接受  2 审核通过 3  审核拒绝  4 返工
     */
    public int state;


    public String createTimeString;
    public long createTime;

    public long receiveTime;
    public String receiveTimeString;




     public long senderId;
     public String senderName;

     public long receiverId;
     public String receiverName;



    public long checkTime;
    public String checkTimeString;
    public String thumbnail;
    public String url;


    /**
     * 提交备注
     */
    public String sendMemo;




    public String memo;


    /**
     * 订单项目流程状态id
     */
    public long orderItemProcessId;
    ;


    //冗余字段
    public String factoryName;


    /**
     * 流程递交的图片信息， 三张图片， 由流程接收者拍摄
     */
    public String pictures;


    /**
     * 交接区域
     */
    public String area;


    /**
     * 返工
     */
    public static final int STATE_REWORK = 4;

    /**
     * 审核不通过
     */
    public static final int STATE_REJECT = 3;
    /**
     * 已接收
     */
    public static final int STATE_PASS = 2;

    /**
     * 已接收
     */
    public static final int STATE_RECEIVE = 1;

    /**
     * 发送
     */
    public static final int STATE_SEND = 0;


    public static final String NAME_SUBMIT = "提交";
    public static final String NAME_REWORK = "返工";


    /**
     * 客号
     */
    public String bat_no;

    /**
     * 客户代号
     */
    public String cus_no;

    /**
     * M T 其他  制令单类型  AM AT 等等
     */
    public String mrpType;


    /**
     * MJ  TJ 其他
     */
    public String prdType;


//    /**
//     *   mrpType='T' or (mrpType<>'M' and mrpType<>'T' and prdType='TJ')  铁件类型判断
//     *mrpType='M' or (mrpType<>'M' and mrpType<>'T' and prdType='MJ')  木件类型判断
//     * 优先判断mrpType，当mrpType 未设置（非M T） 时候 判断TJ MJ
//     */
//   public String  mrp_prd_typ;




    /**
     * 设定好的产品生产类型 -1 未设定 0 自制 1  外购
     */
    public int produceType;

    public String produceTypeName;


}
