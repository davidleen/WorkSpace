package com.giants3.hd.entity;

import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.interf.Valuable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 客户表
 * Created by davidleen29 on 2015/7/1.
 */
@Entity(name = "T_Customer")
public class Customer implements Serializable, Valuable {
    public static final String CODE_TEMP = "000";
    /**
     * 单位 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;


    public String code;

    public String name;

    public String company;

    public String tel;
    public String fax;
    public String email;


    public String addr;

    public String nation;

    /**
     * 头衔
     */
    public String title;
    /**
     * 网站地址等
     */
    public String homePage;


 /**
     *  名片信息内容（扫描名片获得）
     */
    @Lob
    public String orignData;
    /**
     * 名片图片 url
     */
    public String nameCardFileUrl;





    public Customer() {
    }

    public Customer(String code, String name, String tel) {
        this.code = code;
        this.name = name;
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "[" + code + "]" + name;
    }

    @Override
    public boolean isEmpty() {
        return StringUtils.isEmpty(code) && StringUtils.isEmpty(name
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (id != customer.id) return false;
        if (code != null ? !code.equals(customer.code) : customer.code != null) return false;
        if (name != null ? !name.equals(customer.name) : customer.name != null) return false;
        if (tel != null ? !tel.equals(customer.tel) : customer.tel != null) return false;
        if (fax != null ? !fax.equals(customer.fax) : customer.fax != null) return false;
        if (email != null ? !email.equals(customer.email) : customer.email != null) return false;
        if (addr != null ? !addr.equals(customer.addr) : customer.addr != null) return false;
        if (nation != null ? !nation.equals(customer.nation) : customer.nation != null) return false;
        if (title != null ? !title.equals(customer.title) : customer.title != null) return false;
        if (homePage != null ? !homePage.equals(customer.homePage) : customer.homePage != null) return false;
        if (orignData != null ? !orignData.equals(customer.orignData) : customer.orignData != null) return false;
        return nameCardFileUrl != null ? nameCardFileUrl.equals(customer.nameCardFileUrl) : customer.nameCardFileUrl == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (tel != null ? tel.hashCode() : 0);
        result = 31 * result + (fax != null ? fax.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (addr != null ? addr.hashCode() : 0);
        result = 31 * result + (nation != null ? nation.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (homePage != null ? homePage.hashCode() : 0);
        result = 31 * result + (orignData != null ? orignData.hashCode() : 0);
        result = 31 * result + (nameCardFileUrl != null ? nameCardFileUrl.hashCode() : 0);
        return result;
    }
}
