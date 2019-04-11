package com.giants3.reader.entity;

import javax.persistence.*;

/**
 * Created by davidleen29 on 2018/5/12.
 */
@Entity(name="T_Book")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType=DiscriminatorType.INTEGER)
@DiscriminatorValue("0")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Basic
    public String name;

//    @Basic
//   public int  type;
}
