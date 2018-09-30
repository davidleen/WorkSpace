package com.giants3.hd.domain.interractor;

import com.giants3.hd.domain.repository.FileRepository;
import com.giants3.hd.domain.repository.QuotationRepository;
import rx.Observable;

/**打印报价单，
 * Created by davidleen29 on 2018/7/26.
 */
public class PrintAppQuotationUseCase extends DefaultUseCase {
    private final long quotationId;
    private final String filePath;
    private final QuotationRepository quotationRepository;

    /**
     *
     * @param quotationId 报价单id
     * @param filePath    本地文件路径
     * @param quotationRepository
     */
    public PrintAppQuotationUseCase(long quotationId, String filePath, QuotationRepository quotationRepository) {
        super();
        this.quotationId = quotationId;
        this.filePath = filePath;
        this.quotationRepository = quotationRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return quotationRepository.printQuotationToFile(quotationId,filePath);
    }
}
