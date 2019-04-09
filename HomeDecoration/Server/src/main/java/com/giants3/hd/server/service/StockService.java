package com.giants3.hd.server.service;

import com.giants3.hd.entity.*;
import com.giants3.hd.entity_erp.ErpStockOut;
import com.giants3.hd.entity_erp.ErpStockOutItem;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.noEntity.ErpStockOutDetail;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.server.repository.*;
import com.giants3.hd.server.repository_erp.ErpStockOutRepository;
import com.giants3.hd.server.repository_erp.ErpStockSubmitRepository;
import com.giants3.hd.server.utils.AttachFileUtils;
import com.giants3.hd.server.utils.FileUtils;
import com.giants3.hd.utils.ArrayUtils;
import com.giants3.hd.utils.FloatHelper;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 业务处理 类
 * Created by david on 2016/2/15.
 */
@Service
public class StockService extends AbstractService {


    @Autowired
    ErpStockOutRepository repository;


    @Autowired
    ProductRepository productRepository;

    @Autowired
    StockOutItemRepository stockOutItemRepository;
    @Autowired
    GlobalDataService globalDataService;
    @Autowired
    StockOutAuthRepository stockOutAuthRepository;

    @Autowired
    StockOutRepository stockOutRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    //临时文件夹
    @Value("${tempfilepath}")
    private String tempFilePath;

    //附件文件夹
    @Value("${attachfilepath}")
    private String attachfilepath;


    @Autowired
    ErpStockSubmitRepository erpStockSubmitRepository;

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {


    }


    /**
     * 查询出库列表
     *
     * @param loginUser
     * @param key
     * @param salesId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public RemoteData<ErpStockOut> search(User loginUser, String key, long salesId, int pageIndex, int pageSize) {


        List<String> salesNos = null;
        //查询所有
        StockOutAuth stockOutAuth = stockOutAuthRepository.findFirstByUser_IdEquals(loginUser.id);
        if (stockOutAuth != null && !StringUtils.isEmpty(stockOutAuth.relatedSales)) {
            salesNos = userService.findUserCodes(loginUser.id, salesId, stockOutAuth.relatedSales);
        }


        if (salesNos == null || salesNos.size() == 0) return wrapData();


        List<ErpStockOut> erpStockOuts = repository.stockOutList(key, salesNos, pageIndex, pageSize);
        for (ErpStockOut stockOut : erpStockOuts) {
            attachData(stockOut);
            attachSaleData(stockOut);
        }


        RemoteData<ErpStockOut> remoteData = new RemoteData<>();


        remoteData.datas = erpStockOuts;
        int totalCount = repository.getRecordCountByKey(key, salesNos);
        return wrapData(pageIndex, pageSize, (totalCount - 1) / pageSize + 1, totalCount, erpStockOuts);

    }


    /**
     * 查询出库列表
     *
     * @param ck_no
     * @return
     */
    public RemoteData<ErpStockOutDetail> findDetail(String ck_no) {


        List<ErpStockOutItem> erpStockOuts = repository.stockOutItemsList(ck_no);


        //存放 拆分出来的单项数据
        List<ErpStockOutItem> additionalItems = new ArrayList<>();
        for (ErpStockOutItem item : erpStockOuts) {
            String productCode = item.prd_no;
            String pVersion = "";
            try {
                pVersion = item.id_no.substring(productCode.length() + 2);
            } catch (Throwable t) {
                t.printStackTrace();
            }

            //数据校正
            if (item.qty <= 0)
                item.qty = item.qty2;
            //数据校正
            if (item.so_zxs <= 0)
                item.so_zxs = item.so_zxs2;

            item.pVersion = pVersion;

            item.thumbnail = item.url = FileUtils.getErpProductPictureUrl(item.id_no, "");
            item.specCm = StringUtils.convertInchStringToCmString(item.hpgg);
            item.specInch = item.hpgg;

            //更新相关联的产品信息
            Product product = productRepository.findFirstByNameEqualsAndPVersionEquals(productCode, pVersion);
            if (product != null) {


                item.unit = product.pUnitName;

            }


            StockOutItem localItem = stockOutItemRepository.findFirstByCkNoEqualsAndItmEqualsAndPsNoEquals(item.ck_no, item.itm, item.ps_no);
            if (localItem != null) {

                item.describe = localItem.describe;
                item.stockOutQty = item.qty != 0 ? item.qty : item.qty2;


            } else {


                //附件封签号 柜号 描述数据


                List<StockOutItem> stockOutItems = stockOutItemRepository.findByCkNoEqualsAndItmEquals(item.ck_no, item.itm);

                if (stockOutItems != null && stockOutItems.size() > 0) {

                    int size = stockOutItems.size();
                    StockOutItem stockOutItem = stockOutItems.get(0);
                    item.describe = stockOutItem.describe;
                    item.fengqianhao = stockOutItem.fengqianhao;
                    item.guihao = stockOutItem.guihao;
                    item.guixing = stockOutItem.guixing;
                    item.subRecord = stockOutItem.subRecord;

                    //保证 出库的数量有数据
                    item.stockOutQty = stockOutItem.stockOutQty == 0 ? item.qty : stockOutItem.stockOutQty;
                    item.id = stockOutItem.id;
                    //描述取值
                    if (!StringUtils.isEmpty(stockOutItem.describe)) {
                        item.describe = stockOutItem.describe;
                    } else {
                        item.describe = item.idx_name;
                    }

                    for (int i = 1; i < size; i++) {
                        ErpStockOutItem additionItem = GsonUtils.fromJson(GsonUtils.toJson(item), ErpStockOutItem.class);
                        stockOutItem = stockOutItems.get(i);
                        additionItem.describe = stockOutItem.describe;
                        additionItem.fengqianhao = stockOutItem.fengqianhao;
                        additionItem.guihao = stockOutItem.guihao;
                        additionItem.guixing = stockOutItem.guixing;
                        additionItem.stockOutQty = stockOutItem.stockOutQty;
                        item.subRecord = stockOutItem.subRecord;
                        additionItem.id = stockOutItem.id;

                        //描述取值
                        if (!StringUtils.isEmpty(stockOutItem.describe)) {
                            additionItem.describe = stockOutItem.describe;
                        } else {
                            additionItem.describe = additionItem.idx_name;
                        }

                        additionalItems.add(additionItem);
                    }
                } else {
                    item.stockOutQty = item.qty;
                }
            }


        }

        //汇总拆分的数据
        erpStockOuts.addAll(additionalItems);

        //排序
        ArrayUtils.SortList(erpStockOuts, new Comparator<ErpStockOutItem>() {
            @Override
            public int compare(ErpStockOutItem o1, ErpStockOutItem o2) {

                int value = o1.ck_no.compareTo(o2.ck_no);
                if (value != 0) return value;
                value = Integer.compare(o1.itm, o2.itm);
                if (value != 0) return value;
                return Boolean.compare(o2.subRecord, o1.subRecord);

            }
        });


        ErpStockOutDetail detail = new ErpStockOutDetail();
        ArrayList<ErpStockOutDetail> list = new ArrayList();
        list.add(detail);


        ErpStockOut erpStockOut = repository.findStockOut(ck_no);
        attachData(erpStockOut);
        attachSaleData(erpStockOut);
        RemoteData<ErpStockOutDetail> remoteData = new RemoteData<>();

        detail.erpStockOut = erpStockOut;
        detail.items = erpStockOuts;
        remoteData.datas = list;
        return remoteData;
    }


    /**
     * 附加数据
     *
     * @param erpStockOut
     */
    private void attachData(ErpStockOut erpStockOut) {


        if (erpStockOut != null) {


            StockOut stockOut = stockOutRepository.findFirstByCkNoEquals(erpStockOut.ck_no);
            if (stockOut != null) {
                erpStockOut.cemai = stockOut.cemai;
                erpStockOut.zhengmai = stockOut.zhengmai;
                erpStockOut.neheimai = stockOut.neheimai;
                erpStockOut.memo = stockOut.memo;

                erpStockOut.attaches = stockOut.attaches;
            }

        }
    }

    /**
     * 附加业务员数据
     *
     * @param erpStockOut
     */
    private void attachSaleData(ErpStockOut erpStockOut) {


        if (erpStockOut != null) {


            User user = userRepository.findFirstByCodeEquals(erpStockOut.sal_no);
            if (user != null) {
                erpStockOut.sal_name = user.name;
                erpStockOut.sal_cname = user.chineseName;

            }

        }
    }

    /**
     * 剥离数据
     *
     * @param erpStockOut
     */
    private void detachData(ErpStockOut erpStockOut, StockOut stockOut) {
        if (erpStockOut == null || stockOut == null) {
            return;
        }


        stockOut.cemai = erpStockOut.cemai;
        stockOut.zhengmai = erpStockOut.zhengmai;
        stockOut.neheimai = erpStockOut.neheimai;
        stockOut.memo = erpStockOut.memo;
        stockOut.ckNo = erpStockOut.ck_no;
        stockOut.attaches = erpStockOut.attaches;


    }


    /**
     * 保存出库单
     *
     * @param stockOutDetail
     * @return
     */
    @Transactional(rollbackFor = {HdException.class})
    public RemoteData<ErpStockOutDetail> saveOutDetail(ErpStockOutDetail stockOutDetail) throws HdException {


        StockOut stockOut = stockOutRepository.findFirstByCkNoEquals(stockOutDetail.erpStockOut.ck_no);
        if (stockOut == null) {
            stockOut = new StockOut();
        }


        String oldAttaches = stockOut.attaches;

        detachData(stockOutDetail.erpStockOut, stockOut);
        //附件处理
        stockOut.attaches = AttachFileUtils.updateProductAttaches(stockOut.attaches, oldAttaches, "StockOut_" + stockOut.ckNo, attachfilepath, tempFilePath);
        stockOutRepository.save(stockOut);


        List<ErpStockOutItem> newStockOutItems = stockOutDetail.items;


        for (ErpStockOutItem item : newStockOutItems) {
            if (StringUtils.isEmpty(item.ps_no)) {

                throw HdException.create("出库单" + stockOut.ckNo + "的款项：" + item.id_no + "未有对应销库数据（封签号，柜号），不能保存");
            }
            StockOutItem stockOutItem = stockOutItemRepository.findFirstByCkNoEqualsAndItmEqualsAndPsNoEquals(item.ck_no, item.itm, item.ps_no);
            if (stockOutItem == null) {
                stockOutItem = new StockOutItem();
            }
            stockOutItem.ckNo = item.ck_no;
            stockOutItem.itm = item.itm;
            stockOutItem.psNo = item.ps_no;
            stockOutItem.describe = item.describe;
            stockOutItemRepository.saveAndFlush(stockOutItem);

        }

//        List<StockOutItem> oldItems = stockOutItemRepository.findByCkNoEquals(stockOutDetail.erpStockOut.ck_no);
//        List<Long> oldIds = new ArrayList<>();
//        for (StockOutItem item : oldItems) {
//            oldIds.add(item.id);
//        }
//        for (ErpStockOutItem item : newStockOutItems) {
//            oldIds.remove(item.id);
//            StockOutItem stockOutItem = stockOutItemRepository.findOne(item.id);
//            if (stockOutItem == null) {
//                stockOutItem = new StockOutItem();
//            }
//            stockOutItem.ckNo = item.ck_no;
//            stockOutItem.itm = item.itm;
//            stockOutItem.describe = item.describe;
//            stockOutItem.fengqianhao = item.fengqianhao;
//            stockOutItem.guihao = item.guihao;
//            stockOutItem.guixing = item.guixing;
//            stockOutItem.subRecord = item.subRecord;
//            stockOutItem.stockOutQty = item.stockOutQty;
//            stockOutItemRepository.save(stockOutItem);
//        }
//
//        //删除被删除的记录  不存在新的列表中
//        for (Long id : oldIds) {
//            stockOutItemRepository.delete(id);
//        }


        return findDetail(stockOutDetail.erpStockOut.ck_no);


    }


    /**
     * 查询出 进货与缴库 列表
     *
     * @return
     */
    public RemoteData<StockSubmit> getStockInAndSubmitList(String startDate, String endData) {


        final List<StockSubmit> stockSubmitList = erpStockSubmitRepository.getStockSubmitList(startDate, endData);
        updateStockSubmitList(stockSubmitList);
        return wrapData(stockSubmitList);
    }

    /**
     * 根据日期 查询 销库明细列表
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public RemoteData<StockSubmit> getStockXiaokuItemList(String key, String startDate, String endDate) {


        final List<StockSubmit> stockXiaokuItemList = erpStockSubmitRepository.getStockXiaokuItemList(key, startDate, endDate);

        updateStockSubmitList(stockXiaokuItemList);
        return wrapData(stockXiaokuItemList);
    }

    /**
     * 获取销库单列表
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public RemoteData<StockXiaoku> getStockXiaokuList(String key, int pageIndex, int
            pageSize) {

        List<StockXiaoku> xiaokus = erpStockSubmitRepository.getStockXiaokuList(key, pageIndex, pageSize);


        int totalCount = erpStockSubmitRepository.getXiaokuRecordCount(key);
        return wrapData(pageIndex, pageSize, (totalCount - 1) / pageSize + 1, totalCount, xiaokus);
    }

    /**
     * 获取销库单明细数据列表
     *
     * @return
     */
    public RemoteData<StockSubmit> getStockXiaokuItemList(String ps_no) {

        List<StockSubmit> submits = erpStockSubmitRepository.getStockXiaokuItemList(ps_no);
        updateStockSubmitList(submits);

        return wrapData(submits);
    }


    /**
     * 更新搬运库相关单价区域数据
     *
     * @param stockSubmits
     */
    private void updateStockSubmitList(List<StockSubmit> stockSubmits) {


        GlobalData globalData = globalDataService.findCurrentGlobalData();
        if (globalData == null) return;
        Map<Integer, Float> typePriceMap = new HashMap<>();

        //厂家入库
        typePriceMap.put(2, globalData.priceOfStockProductFactoryIn);
        //缴库
        typePriceMap.put(1, globalData.priceOfStockProductIn);
        //出库到货柜
        typePriceMap.put(3, globalData.priceOfStockProductOutToTrunk);

        Map<Integer, String> typeAreaMap = new HashMap<>();
        //厂家入库
        typeAreaMap.put(2, "厂家到仓库");
        //缴库
        typeAreaMap.put(1, "车间到仓库");
        //出库到货柜
        typeAreaMap.put(3, "仓库到装柜");


        for (StockSubmit submit : stockSubmits) {


            submit.url = FileUtils.getErpProductPictureUrl(submit.id_no, "");

            submit.dd = StringUtils.clipSqlDateData(submit.dd);
            float price = typePriceMap.get(submit.type);
            submit.area = typeAreaMap.get(submit.type);

            submit.xs = submit.qty / submit.so_zxs;
            submit.zxgtj = submit.xgtj * submit.xs;
            submit.price = price;
            submit.cost = FloatHelper.scale(submit.price * submit.zxgtj, 2);

        }
    }


}
