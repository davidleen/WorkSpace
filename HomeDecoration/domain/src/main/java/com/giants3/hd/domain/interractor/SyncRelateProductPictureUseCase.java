package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.ProductRepository;
import rx.Observable;
import rx.Scheduler;

   /**
 * 同步所有产品关联的图片
 * Created by davidleen29 on 2017/4/13.
 */

public class SyncRelateProductPictureUseCase extends UseCase {
       private final ProductRepository productRepository;

       public SyncRelateProductPictureUseCase(Scheduler scheduler, Scheduler immediate, ProductRepository productRepository) {
        super(scheduler,immediate);
           this.productRepository = productRepository;
       }

       /**
        * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
        */
       @Override
       protected Observable buildUseCaseObservable() {
           return productRepository.syncRelateProductPicture();
       }
   }
