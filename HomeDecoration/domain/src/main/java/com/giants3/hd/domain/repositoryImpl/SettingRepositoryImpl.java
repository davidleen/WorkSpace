package com.giants3.hd.domain.repositoryImpl;

import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.repository.AuthRepository;
import com.giants3.hd.domain.repository.SettingRepository;
import com.giants3.hd.entity.*;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.RemoteData;
import com.google.inject.Inject;
import rx.Observable;
import rx.Subscriber;

import java.util.List;

/**设置相关接口
 * Created by david on 2015/10/6.
 */
public class SettingRepositoryImpl extends  BaseRepositoryImpl implements SettingRepository {
    @Inject
    ApiManager apiManager;


    @Override
    public Observable<RemoteData<Company>> updateCompany(final Company company) {

        return crateObservable(  new ApiCaller<Company>() {
            @Override
            public RemoteData<Company> call() throws HdException {
                return apiManager.updateCompany(  company );
            }
        });

    }
}
