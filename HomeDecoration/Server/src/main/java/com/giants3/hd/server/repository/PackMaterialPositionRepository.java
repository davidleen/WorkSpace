package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.PackMaterialPosition;
import org.springframework.data.jpa.repository.JpaRepository;

/**
* 包装材料使用位置
 *
*/
public interface PackMaterialPositionRepository extends JpaRepository<PackMaterialPosition,Long> {

}
