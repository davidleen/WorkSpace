package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.ProductRepository;
import com.giants3.hd.domain.repositoryImpl.QuotationRepositoryImpl;
import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by david on 2015/10/7.
 */
public class ProductUseCase extends UseCase {


    private String startName;
    private String endName;
    private boolean withCopy;
    private ProductRepository productRepository;

    public ProductUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, String startName, String endName, boolean withCopy, ProductRepository productRepository) {
        super(threadExecutor, postExecutionThread);
        this.startName = startName;
        this.endName = endName;
        this.withCopy = withCopy;
        this.productRepository = productRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return productRepository.loadByProductNameBetween(startName,endName,withCopy);
    }
}
