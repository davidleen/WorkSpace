package com.giants3.hd.domain.interractor;

import com.google.inject.Guice;
import rx.schedulers.Schedulers;

/**
 * 默认的用例
 * Created by davidleeen29 on 2015/10/7.
 */
public abstract class DefaultUseCase extends UseCase {


    public DefaultUseCase() {
        super(Schedulers.newThread(), Schedulers.immediate());
        Guice.createInjector().injectMembers(this);

    }


}
