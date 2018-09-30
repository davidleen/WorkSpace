package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.ProductMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 产品材料资源 。
 * <p/>
 * 提供根据产品id查询相关信息
 */
public interface ProductMaterialRepository extends JpaRepository<ProductMaterial, Long> {


    public List<ProductMaterial> findByProductIdEqualsOrderByItemIndexAsc(long productId);

    public List<ProductMaterial> findByProductIdEqualsAndFlowIdEquals(long productId, long flowId);


    public List<ProductMaterial> findByMaterialIdEquals(long materialId);


    public ProductMaterial findFirstByMaterialIdEquals(long materialId);


    public int deleteByProductIdEquals(long productId);


    @Modifying
    @Query("update T_ProductMaterial p set    p.memo=:memo    WHERE p.materialId =   :materialId  and ( p.memo <> :memo or p.memo is null)  ")
    public void updateMemoOnMaterialId(@Param("memo") String memo, @Param("materialId") long materialId);

    @Modifying
    @Query("update T_ProductMaterial p set    p.unit2=:unit2  , p.price2=:price2   WHERE p.materialId =   :materialId   ")
    void updateUnitPrice2ByMaterialId(@Param("unit2") String unit2, @Param("price2") float price2, @Param("materialId") long materialId);
}
