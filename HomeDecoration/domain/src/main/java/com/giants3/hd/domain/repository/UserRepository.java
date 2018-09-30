package com.giants3.hd.domain.repository;

import com.giants3.hd.entity.User;
import rx.Observable;

import java.util.List;

/**   用户处理
 * Created by david on 2015/10/6.
 */
public interface UserRepository {


    /**
      登录
     *
     */
    Observable<User> login(String name,String password );


    /**
     *
     *   获取用户列表
     *
     */
    Observable<List<User>> getUserList(  );

}
