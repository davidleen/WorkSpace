package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.QuotationDelete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *  被删除的产品数据记录
* Created by davidleen29 on 2014/9/17.
*/
public interface QuotationDeleteRepository extends JpaRepository<QuotationDelete,Long> {




    public Page<QuotationDelete> findByQNumberLikeOrSaleManLikeOrCustomerLikeOrderByTimeDesc(String qnumber, String saleman,String customer,Pageable pageable);
}
