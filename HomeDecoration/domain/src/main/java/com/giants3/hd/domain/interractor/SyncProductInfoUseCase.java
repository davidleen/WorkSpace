package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.FileRepository;
import com.giants3.hd.domain.repository.ProductRepository;
import rx.Observable;

/**从指定资源服务库中 同步当前库产品图片
 * Created by davidleen29 on 2018/8/22.
 */
public class SyncProductInfoUseCase extends DefaultUseCase {
    private final String remoteResource;
    private String filterKey;
    private boolean shouldOverride;
    private final ProductRepository productRepository;

    public SyncProductInfoUseCase(String remoteResource,String filterKey,boolean shouldOverride, ProductRepository productRepository) {
        super();
        this.remoteResource = remoteResource;
        this.filterKey = filterKey;
        this.shouldOverride = shouldOverride;
        this.productRepository = productRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {

          return    productRepository.syncProductInfo(remoteResource,  filterKey,  shouldOverride);

    }
}
