package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.Factory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
*  客户信息
 *
*/
public interface FactoryRepository extends JpaRepository<Factory,Long> {


    public Factory findFirstByCodeEquals(String code);

}
