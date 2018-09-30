package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.OrderRepository;
import rx.Observable;

import java.io.File;

/**
 * Created by davidleen29 on 2017/7/31.
 */
public class UpdateMaitouFileUseCase extends DefaultUseCase {
    private final String os_no;
    private final File file;
    private final OrderRepository orderRepository;

    public UpdateMaitouFileUseCase(String os_no, File file, OrderRepository orderRepository) {
        super();
        this.os_no = os_no;
        this.file = file;
        this.orderRepository = orderRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return orderRepository.updateMaitouFile(os_no,file);
    }
}
