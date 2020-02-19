package com.giants3.reader.server.repository;
//

import com.giants3.reader.entity.AuthCodes;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 用户
* Created by davidleen29 on 2014/9/17.
*/
public interface AuthCodesRepository<T extends AuthCodes> extends JpaRepository<T ,Long> {



        public List<AuthCodes> findByCodeLikeAndPlatformEquals(String key, String  platform, Sort sort);
        public List<AuthCodes> findByCodeLike(String key,  Sort sort);

}
