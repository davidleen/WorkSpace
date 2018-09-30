package com.giants3.hd.server.repository;


import com.giants3.hd.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
*  产品资源库
*/
public interface ProductRepository extends JpaRepository<Product,Long> {


    @Query(value = "select p from  T_Product  p where  p.name like :key  and (p.pVersion is null or p.pVersion='')   order by p.name desc "
            , countQuery = "select count(p.id) from  T_Product  p where  p.name like :key  and (p.pVersion is null or p.pVersion='') "
    )
    Page<Product> findByKeyWithoutCopy(@Param("key") String key, Pageable pageable);

    Page<Product> findByNameLikeOrPVersionLikeOrderByNameDesc(String proName,String pVersion, Pageable pageable);
    Page<Product> findByNameLikeOrPVersionLikeOrderByNameAsc(String proName,String pVersion, Pageable pageable);
    Page<Product> findByNameLikeAndConceptusCostEqualsOrderByNameAsc(String proName, float value, Pageable pageable);
    Page<Product> findByNameLikeAndAssembleCostEqualsOrderByNameAsc(String likeValue, float value, Pageable pageable);
    Page<Product> findByNameLikeAndPaintCostEqualsOrderByNameAsc(String likeValue, float value, Pageable pageable);
    Page<Product> findByNameLikeAndPackCostEqualsOrderByNameAsc(String likeValue, float value, Pageable pageable);


    /**
     *
     * @param startName
     * @param endName
     * @return
     */
    List<Product> findByNameBetweenOrderByName(String startName, String endName);

    /**
     * 产品由产品名称与 产品版本号  共同作为识别码
     * @param proName
     * @param version
     * @return
     */
    Product findFirstByNameEqualsAndPVersionEquals(String proName, String version);

    /**
     * 查找相同产品品名的货物
     * @param proName
     * @return
     */
    List<Product> findByNameEquals(String proName);
/**
     * 查找相同产品品名的货物
     * @param proName
     * @return
     */
    List<Product> findByNameLike(String proName);


    /**
     * 更新所有产品的生产流程数据， 仅仅在生产流程数据初始化时候 执行。
     * @param workFlowSteps
     */
    @Modifying
    @Query("update T_Product p set    p.workFlowSteps=:workFlowSteps ,p.workFlowNames=:workFlowNames")
    public void setDefaultWorkFlowIds(@Param("workFlowSteps") String workFlowSteps ,@Param("workFlowNames") String workFlowNames );

    /**
     *   查询制定厂家的产品
     */

    List<Product> findByFactoryCodeEquals(String s);


    List<Product> findByIdIn(long[] productIdArray);
}
