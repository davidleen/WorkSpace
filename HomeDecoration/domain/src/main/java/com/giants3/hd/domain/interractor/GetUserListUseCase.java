package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.UserRepository;
import com.giants3.hd.entity.User;
import rx.Observable;
import rx.Scheduler;

import java.util.List;

/**
 * 获取生产进度元数据
 * Created by david on 2015/10/7.
 */
public class GetUserListUseCase extends UseCase {

    private UserRepository userRepository;

    protected GetUserListUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, UserRepository userRepository) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
    }


    @Override
    protected Observable<List<User>> buildUseCaseObservable() {
        return userRepository.getUserList();
    }
}
