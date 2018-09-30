package com.giants3.hd.server.service;

import com.giants3.hd.entity.Customer;
import com.giants3.hd.entity.User;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.NameCard;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.repository.CustomerRepository;
import com.giants3.hd.server.repository.QuotationRepository;
import com.giants3.hd.server.service_third.NameCardService;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 客户  业务处理 类
 * Created by david on 2016/2/15.
 */
@Service
public class CustomerService extends AbstractService {


    public static final String CATEGORY="namecards";
    @Value("${rootpath}")
    private String rootpath;

    @Value("${rootUrl}")
    private String rootUrl;

    @Autowired
    NameCardService nameCardService;

    @Autowired
    private CustomerRepository customerRepository;


    @Autowired
    private QuotationRepository quotationRepository;


    /**
     * 保存用户列表
     * 添加新增的
     * 修改存在的值
     * 不存在的，删除
     *
     * @param newCustomers
     * @return
     */
    @Transactional(rollbackFor = {HdException.class})
    public List<Customer> saveCustomers(List<Customer> newCustomers) throws HdException {

        List<Customer> oldCustomers = customerRepository.findAll();

        List<Customer> removed = new ArrayList<>();

        for (Customer oldCustomer : oldCustomers) {
            boolean foundInNew = false;
            for (Customer newUser : newCustomers) {

                if (oldCustomer.id == newUser.id) {
                    foundInNew = true;
                }


            }
            if (!foundInNew) {
                removed.add(oldCustomer);
            }

        }

        for (Customer customer : newCustomers) {


            Customer oldData = customerRepository.findFirstByCodeEquals(customer.code);
            if (oldData == null) {
                customer.id = -1;


            } else {
                customer.id = oldData.id;

            }
            customerRepository.save(customer);


            /**
             *  删除不存在item   标记delete
             */
            for (Customer deletedCustomer : removed) {

                if (quotationRepository.findFirstByCustomerIdEquals(deletedCustomer.id) != null) {
                    throw HdException.create("不能删除客户【" + deletedCustomer.name + "】,目前有报价单关联着");
                }
                customerRepository.delete(deletedCustomer);


            }

        }


        return list("");
    }


    /**
     * 查询用户列表
     *
     * @return
     */
    public List<Customer> list(String key) {




        String keyLike= StringUtils.sqlLike(key);
       return  customerRepository.findByCodeLikeOrNameLikeOrderByCodeAsc(keyLike,keyLike);

    }


    /**
     * 查询是否可以删除   是否有关联的数据
     *
     * @param customerId
     * @return
     */
    public boolean canDelete(long customerId) {


        return false
                ;
    }


    public RemoteData<Customer> add(String name, String code, String tel) {


        Customer customer = new Customer(name, code, tel);

        customer = customerRepository.save(customer);

        return wrapData(customer);
    }

    /**
     * @param id
     * @param name
     * @param code
     * @param tel
     * @return
     */
    public RemoteData<Customer> modify(long id, String name, String code, String tel) {


        return wrapData(customerRepository.findOne(id));
    }


    /**
     * @param id

     * @return
     */
    public RemoteData<Void> delete(long id) {

        customerRepository.delete(id);
        return wrapData();
    }

    public RemoteData<Customer> saveOne(Customer customer) {


        Customer oldCustomer =customerRepository.findOne(customer.id);

        if(oldCustomer==null)
        {
            customer=      customerRepository.saveAndFlush(customer);
        }else {

            //可能修改相关联的数据
            customer = customerRepository.saveAndFlush(customer);
        }





        return wrapData(customer);
    }

    public RemoteData<String> newCustomerCode() {


        String result="";
        Pageable pageable=constructPageSpecification(0,1);

      Page< Customer> customers= customerRepository.findFirstOrderByCodeDesc(pageable);
        Customer customer=customers.getTotalElements()>0?customers.getContent().get(0):null;
        if(customer==null)
        {
            result="1";

        }else
        {
            int intValue=Integer.valueOf(customer.code);
            result=String.valueOf(intValue+1);

        }

        return wrapData(result);

    }

    public RemoteData<NameCard> scanNameCard(User user, MultipartFile[] files) {

        if(files==null||files.length!=1) return wrapError("名片文件数量有错:"+files==null?"null":String.valueOf(files.length));
        MultipartFile file=files[0];




            String fileName = Calendar.getInstance().getTimeInMillis() +"_"+   FileUtils.SUFFIX_JPG;
            final String absoluteFilePath =FileUtils.combinePath(rootpath,CATEGORY,fileName)  ;
            String url = null;
            try {
                FileUtils.copy(file, absoluteFilePath);
                url = FileUtils.combineUrl(rootUrl,CATEGORY,fileName);
            } catch (IOException e) {
                e.printStackTrace();

                logger.error(e.getMessage());
                final RemoteData<NameCard> nameCardRemoteData = wrapError(e.getMessage());
                final NameCard failCard = new NameCard();
                failCard.pictureUrl=url;
                nameCardRemoteData.datas.add(failCard);
                return nameCardRemoteData;

            }


        final RemoteData<NameCard> nameCardRemoteData = nameCardService.requestScanNameCard(absoluteFilePath );

        if(nameCardRemoteData.isSuccess())
        {
            nameCardRemoteData.datas.get(0).pictureUrl=url;
        }





        return nameCardRemoteData;
//        final RemoteData<NameCard> nameCardRemoteData = wrapError("测试错误");
//        final NameCard failCard = new NameCard();
//        failCard.pictureUrl=url;
//        nameCardRemoteData.datas.add(failCard);
//        return nameCardRemoteData;



    }

    public RemoteData<NameCard> scanResourceUrl(User user, String resourceUrl) {


        String absoluteFilePath= FileUtils.convertUrlToPath(rootpath,resourceUrl);
        final RemoteData<NameCard> nameCardRemoteData = nameCardService.requestScanNameCard(absoluteFilePath );
//
        if(nameCardRemoteData.isSuccess())
        {
            nameCardRemoteData.datas.get(0).pictureUrl=resourceUrl;
        }

        return nameCardRemoteData;

    }
}
