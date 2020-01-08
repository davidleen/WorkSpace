package com.giants3.hd.entity;

import com.giants3.hd.noEntity.ModuleConstant;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统模块表
 */
@Entity(name = "T_Module")
public class Module implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;


    @Basic
    public String name;

    @Basic
    public String title;

    @Override
    public String toString() {

        return   title+"[" + name + "] " ;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Module)) return false;

        Module module = (Module) o;

        if (id != module.id) return false;
        if (name != null ? !name.equals(module.name) : module.name != null) return false;
        return !(title != null ? !title.equals(module.title) : module.title != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }


    public static List<Module> getInitDataList() {


        List<Module> data = new ArrayList<>();
        Module module;

        int size = ModuleConstant.NAMES.length;
        for (int i = 0; i < size; i++) {


            module = new Module();
            module.name = ModuleConstant.NAMES[i];
            module.title = ModuleConstant.TITLES[i];
            data.add(module);
        }

        return data;


    }


}
