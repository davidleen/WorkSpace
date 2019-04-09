package com.giants3.hd.entity.app;

import javax.persistence.*;

/**
 * Created by davidleen29 on 2019/3/18.
 */
@Entity(name = "T_PushErrorReport")
public class PushErrorReport {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Lob
    public String message;
    public String code;
    public int status;

}
