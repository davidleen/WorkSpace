package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.PackMaterialType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
* 包装材料类型
 *
*/
public interface PackMaterialTypeRepository extends JpaRepository<PackMaterialType,Long> {

}
