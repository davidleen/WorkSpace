package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * 功能模块
 *
* Created by davidleen29 on 2014/9/17.
*/
public interface ModuleRepository extends JpaRepository<Module,Long> {




    public Module findFirstByNameEquals(String name);

}
