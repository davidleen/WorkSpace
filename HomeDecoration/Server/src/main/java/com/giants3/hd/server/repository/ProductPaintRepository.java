package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.ProductPaint;
import com.giants3.hd.entity.ProductWage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
* 产品油漆信息资源 。
 *
 * 提供根据产品id查询相关信息
 *
*/
public interface ProductPaintRepository extends JpaRepository<ProductPaint,Long> {


    public List<ProductPaint> findByProductIdEqualsOrderByItemIndexAsc(long productId);

    public List<ProductPaint>   findByMaterialIdEquals(long materialId);

    public ProductPaint   findFirstByMaterialIdEquals(long materialId);

    public int deleteByProductIdEquals(long productId);


    @Modifying
    @Query("update T_ProductPaint p set    p.memo=:memo    WHERE p.materialId =   :materialId  and ( p.memo <> :memo or p.memo is null) ")
    public void updateMemoOnMaterialId(@Param("memo") String memo ,  @Param("materialId") long materialId);

    ProductPaint findFirstByProcessIdEquals(long processId);
}
