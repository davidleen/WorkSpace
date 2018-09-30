package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.MaterialType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
*  材料类型  根据统计分类进行区分
 *
*/
public interface MaterialTypeRepository extends JpaRepository<MaterialType,Long> {

}
