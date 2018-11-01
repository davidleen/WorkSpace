package com.giants3.hd.entity;

/**
* 用户列表
 * @see com.giants3.hd.server.entity.Authority
*/
import com.giants3.hd.utils.DigestUtils;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.interf.Valuable;
import com.giants3.hd.noEntity.CompanyPosition;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name="T_User")
public class User implements Serializable,Valuable {
    public static final String ADMIN = "admin";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Basic
    public String code;

    @Basic
    public String name;

    @Basic
    public String chineseName;
    @Basic
    @Deprecated
    /**
     *  use  {@link  #passwordMD5 } instead@@@
     */
    public String password;


    @Basic
    public String passwordMD5;



    @Basic
    public boolean isSalesman;


    @Basic
    public String  email;

    @Basic
    public String  tel;

    @Basic
    public boolean deleted;


    /**
     * {@link CompanyPosition#position}
     */
    public int position;
    /**
     * {@link CompanyPosition#positionName}
     */
    public String  positionName;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (isSalesman != user.isSalesman) return false;
        if (code != null ? !code.equals(user.code) : user.code != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (chineseName != null ? !chineseName.equals(user.chineseName) : user.chineseName != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (positionName != null ? !positionName.equals(user.positionName) : user.positionName != null) return false;
        if (position != user.position) return false;
        return !(tel != null ? !tel.equals(user.tel) : user.tel != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (chineseName != null ? chineseName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (isSalesman ? 1 : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (tel != null ? tel.hashCode() : 0);
        return result;
    }

    @Override
    public boolean isEmpty() {
        return StringUtils.isEmpty(name) ;
    }

    @Override
    public String toString() {


        return "["+code+"]["+name+"]"+chineseName;
    }

    /**
     * 是否管理员
     * @return
     */
    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(name);
    }



    public  boolean  isCorrectPassword(String passwordMd5)
    {

        return passwordMd5.equals(this.passwordMD5)||  (this.password!=null&& passwordMd5.equals(DigestUtils.md5(this.password)) );
    }
}
