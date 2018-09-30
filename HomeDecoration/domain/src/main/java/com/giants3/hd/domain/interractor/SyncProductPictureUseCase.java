package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.FileRepository;
import rx.Observable;

/**从指定资源服务库中 同步当前库产品图片
 * Created by davidleen29 on 2018/8/22.
 */
public class SyncProductPictureUseCase extends DefaultUseCase {
    private final String remoteResource;
    private String filterKey;
    private final FileRepository fileRepository;

    public SyncProductPictureUseCase(String remoteResource,String filterKey, FileRepository fileRepository) {
        super();
        this.remoteResource = remoteResource;
        this.filterKey = filterKey;
        this.fileRepository = fileRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {

          return    fileRepository.syncProductPicture(remoteResource,  filterKey);

    }
}
