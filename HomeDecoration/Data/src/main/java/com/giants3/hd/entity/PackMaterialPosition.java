package com.giants3.hd.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 包装材质位置
 *
 */


@Entity(name="T_PackMaterialPosition")
public class PackMaterialPosition   implements Serializable {



    public  static final String FRONT="前后";
    public  static final String BETWEEN="中间";

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;


    @Basic
    public String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PackMaterialPosition)) return false;

        PackMaterialPosition position = (PackMaterialPosition) o;

        if (id != null ? !id.equals(position.id) : position.id != null) return false;
        return !(name != null ? !name.equals(position.name) : position.name != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
