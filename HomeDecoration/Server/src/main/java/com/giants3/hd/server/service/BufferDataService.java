package com.giants3.hd.server.service;

import com.giants3.hd.entity.OrderAuth;
import com.giants3.hd.entity.QuoteAuth;
import com.giants3.hd.entity.StockOutAuth;
import com.giants3.hd.entity.User;
import com.giants3.hd.noEntity.BufferData;
import com.giants3.hd.noEntity.ProductDetail;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.repository_wrapper.ProductRepositoryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by davidleen29 on 2018/6/12.
 */
@Service
public class BufferDataService extends AbstractService {
    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PackMaterialClassRepository packMaterialClassRepository;

    @Autowired
    private PackMaterialPositionRepository packMaterialPositionRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MaterialTypeRepository materialTypeRepository;

    @Autowired
    private PackMaterialTypeRepository packMaterialTypeRepository;
    @Autowired
    GlobalDataRepository globalDataRepository;
    @Autowired
    private PackRepository packRepository;

    @Autowired
    private ProductClassRepository productClassRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    ProductRepositoryWrapper productWrapperRepository;

    @Autowired
    private MaterialClassRepository materialClassRepository;
    @Autowired
    private WorkFlowSubTypeRepository workFlowSubTypeRepository;


    @Autowired
    private FactoryRepository factoryRepository;
    @Autowired
    private QuoteAuthRepository quoteAuthRepository;


    @Autowired
    private WorkFlowWorkerRepository workFlowWorkerRepository;

    @Autowired
    private OrderAuthRepository orderAuthRepository;

    @Autowired
    private StockOutAuthRepository stockOutAuthRepository;

    public RemoteData<BufferData> getConfigData(long userId, boolean fromDesk) {

        User user = userRepository.findOne(userId);

        if (user == null) {
            return wrapError("未找到用户");
        }

        BufferData bufferData = new BufferData();


        bufferData.packMaterialClasses = packMaterialClassRepository.findAll();
        bufferData.packMaterialPositions = packMaterialPositionRepository.findAll();
        bufferData.packMaterialTypes = packMaterialTypeRepository.findAll();


        QuoteAuth quoteAuth = quoteAuthRepository.findFirstByUser_IdEquals(user.id);
        if (quoteAuth == null) {
            quoteAuth = new QuoteAuth();
            quoteAuth.user = user;
        }
        bufferData.quoteAuth = quoteAuth;

        bufferData.packs = packRepository.findAll();


        //读取第一条数据   总共就一条
        bufferData.globalData = globalDataRepository.findFirstByOrderByUpdateTimeDesc( );

        if (fromDesk) {
            bufferData.customers = customerRepository.findAll();

            bufferData.workFlowSubTypes = workFlowSubTypeRepository.findAll();


            bufferData.materialClasses = materialClassRepository.findAll();
            bufferData.materialTypes = materialTypeRepository.findAll();

            bufferData.pClasses = productClassRepository.findAll();
            bufferData.company = companyRepository.findAll().get(0);


            bufferData.authorities = authorityRepository.findByUser_IdEquals(user.id);


            bufferData.salesmans = userRepository.findByIsSalesman(true);
            ;

            OrderAuth orderAuth = orderAuthRepository.findFirstByUser_IdEquals(user.id);
            if (orderAuth == null) {
                orderAuth = new OrderAuth();
                orderAuth.user = user;
            }
            bufferData.orderAuth = orderAuth;


            StockOutAuth stockOutAuth = stockOutAuthRepository.findFirstByUser_IdEquals(user.id);
            if (stockOutAuth == null) {
                stockOutAuth = new StockOutAuth();
                stockOutAuth.user = user;
            }
            bufferData.stockOutAuth = stockOutAuth;


            bufferData.factories = factoryRepository.findAll();


            List<ProductDetail> demos = productWrapperRepository.getProductDemos();
            bufferData.demos = demos;

        }

        //读取流程节点工作人员
        bufferData.workFlowWorkers = workFlowWorkerRepository.findByUserIdEquals(user.id);


        return wrapData(bufferData);
    }

}
