package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.QuoteAuth;
import com.giants3.hd.entity.app.AppQuoteAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 广交会报价权限
* Created by davidleen29 on 2014/9/17.
*/
public interface AppQuoteAuthRepository extends JpaRepository<AppQuoteAuth,Long> {



}
