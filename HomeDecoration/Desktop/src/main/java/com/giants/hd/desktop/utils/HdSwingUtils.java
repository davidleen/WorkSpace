package com.giants.hd.desktop.utils;

import com.giants.hd.desktop.frames.ProductDetailFrame;
import com.giants3.hd.entity.Product;
import com.giants3.hd.entity.ProductDelete;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.noEntity.ProductDetail;

import javax.swing.*;
import java.awt.*;

/**
 *  功能辅助类。
 */
public class HdSwingUtils {



    /**
     * 显示产品详情
     * @param product
     * @Param component
     *
     */
    public static  void showDetailPanel(Product product , Component component)
    {


        JFrame frame =new ProductDetailFrame(product);
        frame.setLocationRelativeTo(component);
        frame.setVisible(true);
    }


    /**
     * 显示产品详情
     * @param productDetail
     * @Param component
     *
     */
    public static  void showDetailPanel(Window component,ProductDetail productDetail )
    {

        showDetailPanel(component,productDetail,null);

    }


    /**
     * 显示产品详情
     * @param productDetail
     * @Param component
     *
     */
    public static  void showDetailPanel(final Window component, final ProductDetail productDetail, final ProductDelete productDelete )
    {



        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame =new ProductDetailFrame(productDetail,productDelete);
                frame.setLocationRelativeTo(component);

                frame.setVisible(true);

            }
        });

    }

    /**
     * 生成适合JLable 展示的 多行文本
     * @param sourceString
     * @return
     */
    public static String multiLineForLabel(String sourceString)
    {

        String[] valueArray=   sourceString.split(StringUtils.row_separator);

        return multiLineForLabel(valueArray);

    }


    /**
     * 生成适合JLable 展示的 多行文本
     * @param arrayString
     * @return
     */
    public static String multiLineForLabel(String... arrayString)
    {
        String resultToString="<html>";
        for (int i = 0; i < arrayString.length; i++) {
            resultToString+=arrayString[i]+"<br>";
        }
        resultToString+="</html>";

        return  resultToString;

    }
}
