package com.giants3.hd.domain.repository;

import com.giants3.hd.entity.Company;
import com.giants3.hd.entity.MaterialClass;
import com.giants3.hd.entity.OrderItemWorkFlow;
import com.giants3.hd.entity.OutFactory;
import com.giants3.hd.noEntity.RemoteData;
import rx.Observable;

import java.util.List;

/**
 *
 * 厂家数据接口
 * Created by david on 2015/10/6.
 */
public interface SettingRepository {


    Observable<RemoteData<Company>> updateCompany(Company company);
}
