package com.giants3.hd.server.repository;

import com.giants3.hd.entity.ProductValueHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductValueHistoryRepository extends JpaRepository<ProductValueHistory,Long> {
    List<ProductValueHistory> findByProductId(long productId);
    Page<ProductValueHistory> findByNameEqualsAndPVersionEqualsOrderByDateTimeDesc(String name, String pVersion, Pageable pageable);

    Page<ProductValueHistory> findByNameLikeOrPVersionLikeOrderByDateTimeDesc(String name,String pversion ,Pageable pageable);
}
