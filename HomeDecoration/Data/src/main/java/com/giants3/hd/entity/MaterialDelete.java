package com.giants3.hd.entity;

import com.giants3.hd.utils.DateFormats;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**被删除的产品的简要信息
 * Created by davidleen29 on 2015/8/25.
 */
@Entity(name="T_MaterialDelete")
public class MaterialDelete implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public long materialId;
    public String materialCode;
    public String materialName;
    public long userId;
    public String userName;
    public String userCName;
    public long time;
    public String timeString;
    public String url;


    public String toString() {
        return   materialCode + "_"+materialName+ "_"+userName+ "_"+userCName+ "_"+timeString;

    }



    public void setMaterial(Material material,User user)
    {


        url=material.url;
        materialId=material.id;
        materialCode= material.code;
        materialName=material.name;


        userId=user.id                ;
        userName=user.name;
        userCName=user.chineseName;

        time= Calendar.getInstance().getTimeInMillis();
        timeString= DateFormats.FORMAT_YYYY_MM_DD_HH_MM_CHINESE.format(new Date(time));

    }






}
