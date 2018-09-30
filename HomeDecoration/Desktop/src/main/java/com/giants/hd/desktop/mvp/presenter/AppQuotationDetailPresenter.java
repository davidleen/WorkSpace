package com.giants.hd.desktop.mvp.presenter;

import com.giants.hd.desktop.mvp.IPresenter;
import com.giants3.hd.entity.Customer;
import com.giants3.hd.entity.User;
import com.giants3.hd.entity.app.Quotation;

/**
 * Created by davidleen29 on 2017/4/7.
 */
public interface AppQuotationDetailPresenter extends IPresenter {
    /**
     * 对指定行的卖价进行打折处理
     * @param selectRow
     * @param v
     */
    void discountItem(int[] selectRow, float v);

    void addItem(int itemIndex);

    void deleteItem(int[] itemIndexs);

    void moveUpItem(int itemIndex);

    void moveDownItem(int itemIndex);

    void save();

    void setCustomer(Customer customer);

    void setSaleman(User user);

    void updateQNumber(String text);

    void updateQDate(String date);

    void updateVDate(String date);

    void updateMemo(String text);

    void deleteQuotation();

    void print();

    void updateItemPrice(int itemIndex, float newValue);

    void updateItemQty(int itemIndex, int newQty);

    void updateItemMemo(int itemIndex, String newValue);

    void viewProduct(long productId);
}
