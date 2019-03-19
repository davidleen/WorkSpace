package com.giants3.hd.server.repository;
//

import com.giants3.hd.entity.app.PushErrorReport;
import com.giants3.hd.entity.app.QuotationItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 推送错误报告
 * Created by davidleen29 on 2017/9/17.
 */
public interface PushErrorReportRepository extends JpaRepository<PushErrorReport, Long> {


}
