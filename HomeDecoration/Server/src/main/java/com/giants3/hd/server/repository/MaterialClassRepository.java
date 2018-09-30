package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.MaterialClass;
import org.springframework.data.jpa.repository.JpaRepository;

/**
*  材料类型 根据编码头四位进行区分
 *
*/
public interface MaterialClassRepository extends JpaRepository<MaterialClass,Long> {



    public MaterialClass findFirstByCodeEquals(String code);
}
