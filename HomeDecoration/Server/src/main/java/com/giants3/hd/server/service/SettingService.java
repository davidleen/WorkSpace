package com.giants3.hd.server.service;

import com.giants3.hd.entity.Company;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 客户  业务处理 类
 * Created by david on 2016/2/15.
 */
@Service
public class SettingService extends AbstractService {


    @Autowired
    private CompanyRepository companyRepository;


    public Company getCompany() {
        Company company=null;
        List<Company> result = companyRepository.findAll();
        if (result == null || result.size() == 0) {


        } else {
            company = result.get(0);
        }

        return company;
    }

    @Transactional
    public RemoteData<Company> update(Company company) {


        Company old = companyRepository.findOne(company.id);
        if (old == null)
            wrapError("数据异常");

        return wrapData(companyRepository.save(company));


    }



    @Transactional
    public void initCompany()
    {

        List<Company> result = companyRepository.findAll();
        if (result == null || result.size() == 0) {
            Company    company = new Company();
              companyRepository.save(company);

        }


    }
}
