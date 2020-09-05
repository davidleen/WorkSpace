package com.giants3.hd.noEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 加工户产值单项
 */

public class ProducerValueItem implements Serializable {

     public String dept;
    public String name;
    public String os_no;
    public int itm;
    public String zc_no;
    public String zc_name;
    public String mrp_no;
    public String prd_no;
    public String prd_name;
    public String os_dd;

    public  double  value;
    public String thumbnail;

    //带版本号的货号
    public String id_no;
}
