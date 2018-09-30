package com.giants3.hd.server.service;

import com.giants3.hd.entity.Customer;
import com.giants3.hd.entity.OperationLog;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.repository.CustomerRepository;
import com.giants3.hd.server.repository.OperationLogRepository;
import com.giants3.hd.server.repository.QuotationRepository;
import com.giants3.hd.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录处理 类
 * Created by david on 2016/2/15.
 */
@Service
public class LoggerService extends AbstractService {


    @Autowired
    private OperationLogRepository operationLogRepository;


    public List<OperationLog> search(String className, long recordId) {
        return   operationLogRepository.findByTableNameEqualsAndRecordIdEqualsOrderByTimeDesc(className,recordId);
    }
}
