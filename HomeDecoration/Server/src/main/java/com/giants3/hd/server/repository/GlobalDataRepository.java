package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.GlobalData;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * 常量数据
* Created by davidleen29 on 2014/9/17.
*/
public interface GlobalDataRepository extends JpaRepository<GlobalData,Long> {






      GlobalData findFirstByOrderByUpdateTimeDesc( );



}
