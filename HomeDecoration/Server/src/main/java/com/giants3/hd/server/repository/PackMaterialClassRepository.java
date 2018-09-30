package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.PackMaterialClass;
import org.springframework.data.jpa.repository.JpaRepository;

/**
* 包装材料类型
 *
*/
public interface PackMaterialClassRepository extends JpaRepository<PackMaterialClass,Long> {


    public PackMaterialClass findFirstByNameEquals(String name);

}
