package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.SettingRepository;
import com.giants3.hd.domain.repository.WorkFlowRepository;
import com.giants3.hd.entity.Company;
import rx.Observable;

/**
 * Created by davidleen29 on 2018/3/20.
 */
public class UpdateCompanyUseCase extends DefaultUseCase {
    private final Company company;
    private final SettingRepository settingRepository;

    public UpdateCompanyUseCase(Company company, SettingRepository settingRepository) {
        super();
        this.company = company;
        this.settingRepository = settingRepository;
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
     */
    @Override
    protected Observable buildUseCaseObservable() {

      return   settingRepository.updateCompany(company);

    }
}
