package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.FileRepository;
import rx.Observable;
import rx.Scheduler;

import java.io.File;

/** 上传临时文件用例
 * Created by david on 2015/10/7.
 */
public class UploadTempFileUseCase extends UseCase {



    private FileRepository fileRepository;

    private File[] file;

    public UploadTempFileUseCase(Scheduler threadExecutor, Scheduler postExecutionThread, File[] file   , FileRepository fileRepository) {
        super(threadExecutor, postExecutionThread);
        this.file = file;


        this.fileRepository = fileRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return fileRepository.uploadTempFile(file) ;
    }
}
