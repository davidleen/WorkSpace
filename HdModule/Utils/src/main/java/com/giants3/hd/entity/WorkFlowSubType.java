package com.giants3.hd.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 二级流程 铁件 木件  PU，其他
 * <p/>
 * Created by davidleen29 on 2016/12/30.
 */
@Entity(name = "T_WorkFlowSubType")
public class WorkFlowSubType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public int typeId;
    public String typeName;


    static final int TYPE_TIE = 1;
    static final int TYPE_MU = 2;
    static final int TYPE_PU = 3;


   public static final String TYPE_NAME_TIE = "铁件";
    public static final String TYPE_NAME_MU = "木件";
    public static final String TYPE_NAME_PU = "PU";


    public static int[] TYPES = new int[]{TYPE_TIE, TYPE_MU, TYPE_PU};
    public static String[] TYPENAMES = new String[]{TYPE_NAME_TIE, TYPE_NAME_MU, TYPE_NAME_PU};
}
