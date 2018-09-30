package com.giants3.hd.server.utils;

import com.giants3.hd.entity.*;
import com.giants3.hd.noEntity.ProductAgent;
import com.giants3.hd.noEntity.ProductDetail;
import com.giants3.hd.noEntity.QuotationDetail;
import com.giants3.hd.utils.GsonUtils;
import com.giants3.hd.exception.HdException;

import java.io.*;
import java.util.Calendar;

/**
 * Created by davidleen29 on 2015/8/26.
 */
public class BackDataHelper {


    public static <T> void back(T data, String fileFilePath) {


        //备份数据
        try {


            String gsonString = GsonUtils.toJson(data);

            File file =new File(fileFilePath);
            if(!file.exists())
            {
                file.getParentFile().mkdirs();
            }
            //文件形式保存
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(gsonString);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            fileOutputStream.close();


        } catch (IOException io) {
            io.printStackTrace();
        }


    }


    public static <T> T read(String fileFilePath, Class<T> tClass) {


        //备份产品数据
        T result = null;
        try {

            //文件形式保存
            FileInputStream fileInputStream = new FileInputStream(fileFilePath);
            Reader reader = new InputStreamReader(fileInputStream);
            result = GsonUtils.fromReader(reader, tClass);

            reader.close();
            fileInputStream.close();


        } catch (IOException io) {
            io.printStackTrace();
        } catch (HdException e) {
            e.printStackTrace();
        }
        return result;


    }

    public static boolean delete(String fileFilePath) {


        return new File(fileFilePath).delete();


    }


    public static void backProduct(ProductDetail data, String path, ProductDelete deleteMessage) {


        //备份产品数据
        back(data, path + deleteMessage.toString());


    }


    public static void backQuotation(QuotationDetail data, String path, QuotationDelete deleteMessage) {


        //备份产品数据
        back(data, path + deleteMessage.toString());


    }


    public static void backMaterial(Material data, String path, MaterialDelete deleteMessage) {


        //备份产品数据
        back(data, path + deleteMessage.toString());


    }


    public static QuotationDetail getQuotationDetail(String fileDirectory, QuotationDelete quotationDelete) {


        return read(fileDirectory + quotationDelete.toString(), QuotationDetail.class);

    }

    public static ProductDetail getProductDetail(String deleteProductPath, ProductDelete productDelete) {

        return read(deleteProductPath + productDelete.toString(), ProductDetail.class);
    }

    public static void deleteProduct(String deleteProductPath, ProductDelete productDelete) {


        delete(deleteProductPath + productDelete.toString());

    }

    public static void deleteQuotation(String deleteQuotationFilePath, QuotationDelete quotationDelete) {

        delete(deleteQuotationFilePath + quotationDelete.toString());
    }


    /**
     * 保存產品分析表的數據到文件 做备份
     * @param productDetail
     * @param operationLog
     * @param fileDirectory
     */
    public static void productBackFileRenameTo(String tempFile, ProductDetail productDetail, OperationLog operationLog, String fileDirectory)
    {

      File file=  new File(tempFile);


        String destPath=fileDirectory+ ProductAgent.getFullName(productDetail.product)+File.separator+operationLog.id;

        file.renameTo(new File(destPath));


    }

    public static String backProductModifyData(ProductDetail productDetail,String fileDirectory)
    {

        String filePath=fileDirectory+ ProductAgent.getFullName(productDetail.product)+File.separator+ Calendar.getInstance().getTimeInMillis();

        back(productDetail,filePath);
        return filePath;


    }

    /**
     * 恢复指定產品分析表的數據
     * @param productName
     * @param operationLogId
     * @param fileDirectory
     */
    public static ProductDetail   restoreProductModifyData(String productName , long operationLogId, String fileDirectory)
    {


        String filePath=fileDirectory+productName+File.separator+ operationLogId;


      ProductDetail productDetail=read(filePath,ProductDetail.class);

        return productDetail;




    }

}
