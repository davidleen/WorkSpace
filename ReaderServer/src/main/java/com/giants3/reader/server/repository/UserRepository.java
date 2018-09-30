package com.giants3.reader.server.repository;
//

import com.giants3.reader.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 用户
* Created by davidleen29 on 2014/9/17.
*/
public interface UserRepository extends JpaRepository<User,Long> {


    List<User> findByNameEquals(String name);

    User  findFirstByCodeEqualsAndNameEquals(String code, String name);
    User  findFirstByCodeEquals(String code );
    List<User> findByIsSalesman(boolean isSalesman);

    User findFirstByIsSalesmanAndCodeEquals(boolean isSalman, String sal_no);

    List<User> findByDeletedEqualsOrderByCode(boolean deleted  );
}
