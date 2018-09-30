package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.QuotationLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户
* Created by davidleen29 on 2014/9/17.
*/
public interface QuotationLogRepository extends JpaRepository<QuotationLog,Long> {




    public QuotationLog findFirstByQuotationIdEquals(long quotationId);
}
