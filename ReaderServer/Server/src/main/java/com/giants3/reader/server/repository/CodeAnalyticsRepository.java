package com.giants3.reader.server.repository;
//

import com.giants3.reader.entity.CodeAnalytics;
import com.giants3.reader.entity.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 口令统计
 * Created by davidleen29 on 2014/9/17.
 */
public interface CodeAnalyticsRepository<T extends CodeAnalytics> extends JpaRepository<T, Long> {



        public T findFirstByDateStringEquals(String date);
}
