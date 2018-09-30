package com.giants3.hd.logic;


import com.giants3.hd.entity.Customer;
import com.giants3.hd.entity.Product;
import com.giants3.hd.entity.User;
import com.giants3.hd.entity.app.Quotation;
import com.giants3.hd.entity.app.QuotationItem;
import com.giants3.hd.noEntity.app.QuotationDetail;
import com.giants3.hd.utils.FloatHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 报价单的逻辑
 * Created by davidleen29 on 2018/5/20.
 */
public class AppQuotationAnalytics {


    public static void updateQuotationField(QuotationDetail quotationDetail, String field, Object value) {


    }


    public static void updateQuotationSalesMan(QuotationDetail quotationDetail, User user) {


    }

    public static void updateQuotationCustomer(QuotationDetail quotationDetail, Customer customer) {

        setCustomerToQuotation(quotationDetail.quotation, customer);
    }


    public static void setCustomerToQuotation(Quotation quotation, Customer customer) {

        quotation.customerId = customer.id;
        quotation.customerCode = customer.code;
        quotation.customerName = customer.name;
        quotation.customerAddress = customer.addr;
    }

    public static void setSaleManToQuotation(Quotation quotation, User user) {

        quotation.saleId = user.id;
        quotation.salesman = user.toString();
        quotation.email = user.email;
    }


    public static void removeItem(QuotationDetail quotationDetail, int[] itemIndexs) {


        final List<QuotationItem> items = quotationDetail.items;


        List<QuotationItem>removedItems=new ArrayList<>();
        for(int itemIndex:itemIndexs)
        {
            removedItems.add(items.get(itemIndex));
        }
        items.removeAll(removedItems);
        int newItemIndex = 0;
        for (QuotationItem item : items) {
            item.itm = newItemIndex++;
        }

        updateTotalMessage(quotationDetail);


    }



    public static void discountItem(QuotationDetail quotationDetail, int[] itemIndexs,float discount) {


        final List<QuotationItem> items = quotationDetail.items;

        for(int itemIndex:itemIndexs) {
            QuotationItem quotationItem = items.get(itemIndex);
            quotationItem.price = FloatHelper.scale(quotationItem.priceOrigin * discount);
            quotationItem.amountSum = FloatHelper.scale(quotationItem.price * quotationItem.qty);
        }

        updateTotalMessage(quotationDetail);


    }



    public static void discountAll(QuotationDetail quotationDetail ,float discount) {


        final List<QuotationItem> items = quotationDetail.items;
        for (QuotationItem quotationItem:items)
        {
            quotationItem.price = FloatHelper.scale(quotationItem.priceOrigin * discount);
            quotationItem.amountSum = FloatHelper.scale(quotationItem.price * quotationItem.qty);
        }



        updateTotalMessage(quotationDetail);


    }

    public static void addItem(QuotationDetail quotationDetail, int itemIndex, Product product) {


        QuotationItem quotationItem
                = new QuotationItem();

        quotationItem.quotationId = quotationDetail.quotation.id;
        quotationItem.qty = 1;


        bindProductToQuotationItem(quotationItem, product);


        if (itemIndex == -1) {
            quotationDetail.items.add(quotationItem);
        } else
            quotationDetail.items.add(itemIndex, quotationItem);

        int newIndex = 0;
        for (QuotationItem item : quotationDetail.items) {
            item.itm = newIndex++;
        }

        updateTotalMessage(quotationDetail);

    }


    /**
     * 更新汇总数据
     */
    private static void updateTotalMessage(QuotationDetail quotationDetail) {
        List<QuotationItem> quotationItems = quotationDetail.items;
        Quotation quotation = quotationDetail.quotation;

        float totalAmount = 0;
        float totalVolume = 0;
        float totalWeight = 0;


        for (QuotationItem aItem : quotationItems) {

            totalAmount += aItem.amountSum;
            totalVolume += aItem.volumeSum;
            totalWeight += aItem.weightSum;

        }
        quotation.totalAmount = FloatHelper.scale(totalAmount, 2);
        quotation.totalVolume = totalVolume;
        quotation.totalWeight = totalWeight;
        quotation.itemCount = quotationItems.size();

    }


    /**
     * 重新绑定产品数据
     *
     * @param quotationItem
     * @param product
     */
    public static void bindProductToQuotationItem(QuotationItem quotationItem, Product product) {


        quotationItem.productId = product.id;
        quotationItem.productName = product.name;
        quotationItem.pVersion = product.pVersion;
        quotationItem.price = product.fob;
        quotationItem.priceOrigin = product.fob;
        quotationItem.inBoxCount = product.insideBoxQuantity;
        quotationItem.packQuantity = product.packQuantity;
        quotationItem.packageSize=product.packLong+"*"+product.packWidth+"*"+product.packHeight;
        quotationItem.amountSum = quotationItem.price * quotationItem.qty;
        quotationItem.weight = product.weight;
        quotationItem.weightSum = quotationItem.weight * quotationItem.qty;
        quotationItem.volumePerBox = product.packVolume;
        quotationItem.volumeSize = product.packVolume;
        quotationItem.volumeSum = quotationItem.volumePerBox * quotationItem.qty;
        quotationItem.thumbnail = product.thumbnail;
        quotationItem.photoUrl = product.url;
        quotationItem.unit = product.getpUnitName();
        quotationItem.spec = product.getSpec();
        quotationItem.boxLong = product.packLong;
        quotationItem.boxWidth = product.packWidth;
        quotationItem.boxHeight = product.packHeight;
        quotationItem.weightPerBox = product.weight * product.insideBoxQuantity;
        quotationItem.weight = product.weight;


    }

    public static void moveDownItem(QuotationDetail quotationDetail, int itemIndex) {

        if(itemIndex<0) return;

        if(itemIndex>=quotationDetail.items.size()-1) return;


        QuotationItem item= quotationDetail.items.remove(itemIndex);

        quotationDetail.items.add( itemIndex+1,item);



    }

    public static void moveUpItem(QuotationDetail quotationDetail, int itemIndex) {

        if(itemIndex<=0) return;

        if(itemIndex>=quotationDetail.items.size()) return;


       QuotationItem item= quotationDetail.items.remove(itemIndex);

        quotationDetail.items.add( itemIndex-1,item);



    }

    public static void updateQuotationItemPrice(QuotationDetail quotationDetail, int itemIndex, float newValue) {

        final QuotationItem quotationItem = quotationDetail.items.get(itemIndex);
        quotationItem.price=newValue;
        quotationItem.amountSum = FloatHelper.scale(quotationItem.price * quotationItem.qty);
        updateTotalMessage(quotationDetail);
    }

    public static void updateQuotationItemQty(QuotationDetail quotationDetail, int itemIndex, int newQty) {


        final QuotationItem quotationItem = quotationDetail.items.get(itemIndex);
        quotationItem.qty=newQty;
        quotationItem.amountSum = FloatHelper.scale(quotationItem.price * quotationItem.qty);
        updateTotalMessage(quotationDetail);
    }

    public static void updateQuotationItemMemo(QuotationDetail quotationDetail, int itemIndex, String memo) {


        final QuotationItem quotationItem = quotationDetail.items.get(itemIndex);
        quotationItem.memo=memo;

    }
}
