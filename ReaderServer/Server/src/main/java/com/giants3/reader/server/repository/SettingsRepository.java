package com.giants3.reader.server.repository;
//

import com.giants3.reader.entity.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户
 * Created by davidleen29 on 2014/9/17.
 */
public interface SettingsRepository<T extends Settings> extends JpaRepository<T, Long> {




}
