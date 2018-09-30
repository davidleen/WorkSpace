package com.giants3.hd.server.utils;

import com.giants3.hd.noEntity.ProductAgent;
import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.entity.Product;
import de.greenrobot.common.io.IoUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;

/**
 * 文件处理类
 */

public class FileUtils {


    public static final String IMAGE_JPG = "jpg";
    public static final String SUFFIX_JPG = ".jpg";
    public static final String DOWNLOAD_PATH = "api/file/download/";
    public static final String DOWNLOAD_MATERIAL_CODE = DOWNLOAD_PATH + "material/%s/%s.%s?type=%s&mClass=%s";
    public static final String DOWNLOAD_PRODUCT_NAME_P_VERSION = DOWNLOAD_PATH + "product/%s/%s/%s.%s?type=%s";
    public static final String DOWNLOAD_TEMP_PATH = DOWNLOAD_PATH + "temp/";
    public static final String DOWNLOAD_TEMP_NAME = DOWNLOAD_TEMP_PATH + "%s." + IMAGE_JPG;
    public static final String DOWNLOAD_ATTACH_NAME = "/download/attach/{name}";
    public static final String DOWNLOAD_ATTACH_PATH = DOWNLOAD_PATH + "attach/";
    public static final String DOWNLOAD_PRODUCT_PATH = DOWNLOAD_PATH + "product/";
    public static final String DOWNLOAD_ERP_PRODUCT_PHOTO = DOWNLOAD_PATH +"erpProduct/id_no/%s?upateTime=%s";
    public static final String DOWNLOAD_ORDER_MAITOU = DOWNLOAD_PATH +"order/maitou?os_no=%s";

    public static final String DOWNLOAD_PRODUCT_THUMBNAIL = DOWNLOAD_PRODUCT_PATH + "thumbnail/";
    public static final String SEPARATOR = "/";


    /**
     * url 中 对路径的表示， 获取文件资源时， 将这个标识改成  SEPARATOR
     */
    public static final String URL_PATH_SEPARATOR = "___";

    /**
     * /**获取图片路径 url  相对路径 容器根目录
     *
     * @param productName
     * @param pVersion
     * @param updateTime  更新时间
     * @return
     */
    public static final String getProductPictureURL(String productName, String pVersion, long updateTime) {


        return updateTime <= 0 ? "" : String.format(DOWNLOAD_PRODUCT_NAME_P_VERSION, productName, pVersion, updateTime, IMAGE_JPG, IMAGE_JPG);
        //   return updateTime<=0?"":API+DOWNLOAD_PRODUCT_NAME_P_VERSION.replace("{name}",productName).replace("{pVersion}", StringUtils.isEmpty(pVersion)?"":pVersion).replace("{updateTime}",String.valueOf(updateTime));

    }


    /**
     * 构成Erp图片地址
     *
     */

    public static final String  getErpProductPictureUrl(String id_no,String photoUpdateTime)
    {
        String encode="";
        try {
            encode = URLEncoder.encode(id_no, "utf-8");
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
        return String.format(FileUtils.DOWNLOAD_ERP_PRODUCT_PHOTO,encode,photoUpdateTime) ;


    }
    /**
     * /**获取材料图片路径 url  相对路径 容器根目录
     *
     * @param materialCode
     * @param lastUpdateTime 更新时间
     * @return
     */
    public static final String getMaterialPictureURL(String materialCode, String mClass, long lastUpdateTime) {
        return lastUpdateTime <= 0 ? "" : String.format(DOWNLOAD_MATERIAL_CODE, materialCode, lastUpdateTime, IMAGE_JPG, IMAGE_JPG, mClass);
        // return lastUpdateTime<=0?"":DOWNLOAD_MATERIAL_CODE.replace("{materialCode}", materialCode).replace("{updateTime}", String.valueOf(lastUpdateTime));

    }

    /**
     * 获取图片路径   根据规则  （第一个英文字母为之前的字符串 作为文件夹）   默认是jpg文件
     *
     * @param filePath    文件根目录
     * @param productName 产品名称
     * @return
     */
    public static final String getProductPicturePath(String filePath, String productName, String pVersion) {

        return getProductPicturePath(filePath, productName, pVersion, "jpg");

    }


    /**
     * 获取材料图片路径      默认是jpg文件
     *
     * @param filePath 文件根目录
     * @param code     材料编码
     * @param mClass   材料类型 即子文件夹
     * @return
     */
    public static final String getMaterialPicturePath(String filePath, String code, String mClass) {

        return getMaterialPicturePath(filePath, code, mClass, "jpg");

    }


    /**
     * 获取临时图片路径      默认是jpg文件
     *
     * @return
     */
    public static final String getDownloadTempUrl(String fileName) {

        return String.format(DOWNLOAD_TEMP_NAME, fileName);

    }

    /**
     * 获取材料图片路径   根据规则    默认是jpg文件
     *
     * @param filePath 文件根目录
     * @param code     材料编码
     * @param mClass   材料类型 即子文件夹
     * @return
     */
    public static final String getMaterialPicturePath(String filePath, String code, String mClass, String type) {


        if (code.startsWith("C") || code.startsWith("c")) {

            mClass = "C";
        }
        if (StringUtils.isEmpty(mClass)) {

            return filePath + code + "." + type;
        } else
            return filePath + mClass + File.separator + code + "." + type;

    }


    /**
     * 根据产品名称 获取对应的归类文件夹名称
     *
     * @return
     */
    public static final String getProductDirectoryName(String productName) {

        //找到文件夹
        //找到第一个英文单词。
        int len = productName.length();
        int firstCharIndex = -1;
        for (int i = 0; i < len; i++) {
            if (Character.isLetter(productName.charAt(i))) {
                firstCharIndex = i;
                break;
            }
        }


        //文件夹
        return (firstCharIndex >= 0 ? productName.substring(0, firstCharIndex + 1) : "");
    }

    /**
     * 获取图片路径   根据规则  （第一个英文字母为之前的字符串 作为文件夹）
     *
     * @param filePath    文件根目录
     * @param productName 产品名称
     * @param type        文件类型
     * @return
     */
    public static final String getProductPicturePath(String filePath, String productName, String pVersion, String type) {


        //文件夹
        String directory = getProductDirectoryName(productName);


        //無版本號  或者版本號不規範 直接匹配原貨號
        if (StringUtils.isEmpty(pVersion) || pVersion.length() != 6) {
            return filePath + directory + File.separator + productName + "." + type;
        }


        //匹配的文件名
        String fullFilePath = filePath + directory + File.separator + productName + "-" + pVersion + "." + type;
        //精確匹配文件存在  直接使用
        if (new File(fullFilePath).exists()) {
            return
                    fullFilePath;
        }


        //適配處理
        //第四五位 如果为0 直接匹配原来的货号。
        String temp = "";
        if (pVersion.charAt(3) == '0' && pVersion.charAt(4) == '0') {
            temp = productName;
        } else {
            //第六位都化为0 为基准  最后一位不影响图片
            temp = productName + "-" + pVersion.substring(0, 5) + "0";
        }


        return filePath + directory + File.separator + temp + "." + type;


    }


    /**
     * 获取文件的最后更新时间  如果文件不存在 返回-1；
     *
     * @param file
     * @return
     */
    public static long getFileLastUpdateTime(File file) {

        if (!file.exists())
            return -1;
        BasicFileAttributes attributes =
                null;
        try {
            attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);

        } catch (IOException e) {
            e.printStackTrace();

        }


        if (null != attributes) {

            FileTime lastModifiedTime = attributes.lastModifiedTime();

            return lastModifiedTime.toMillis();

        }
        return 0;
    }


    /**
     * 根据文件名 获取路径
     *
     * @param materialFilePath
     * @param fileName         文件名 带后缀
     */
    public static String getMaterialPicturePath(String materialFilePath, String fileName) {


        int indexOfDot = fileName.indexOf(".");
        return getMaterialPicturePath(materialFilePath, fileName.substring(0, indexOfDot), fileName.substring(0, 4), fileName.substring(indexOfDot + 1));

    }


    /**
     * 根据文件名 获取模板文件。
     *
     * @param quotationfilepath
     * @param name
     * @return
     */
    public static String getQuotationFile(String quotationfilepath, String name, String appendix) {

        return quotationfilepath + File.separator + name + "." + appendix;
    }


    /**
     * 根据temp的url获取临时文件的路径
     *
     * @param url
     * @return
     */
    public static String getTempFileName(String url, String tempFilePath) {
        if (url.startsWith(FileUtils.DOWNLOAD_TEMP_PATH)) {


            String relativePath = url.substring(FileUtils.DOWNLOAD_TEMP_PATH.length());

            return tempFilePath + relativePath;
        }

        return "";
    }


    /**
     *
     */
    public static String generateAttachFileName(Product product) {
        return "PRODUCT_" + ProductAgent.getFullName(product) + "-" + Calendar.getInstance().getTimeInMillis() + ".jpg";
    }



    public static String getProductThumbnailFilePath(String productFilePath, Product product) {

        String thumbnailDirectory = productFilePath + "thumbnail" + FileUtils.SEPARATOR + FileUtils.getProductDirectoryName(product.name) + FileUtils.SEPARATOR;
        String thumbnailPath = thumbnailDirectory + product.name + (StringUtils.isEmpty(product.pVersion) ? "" : ("_" + product.pVersion)) + "_" + Calendar.getInstance().getTimeInMillis() + ".jpg";
        return thumbnailPath;

    }


    public static String getProductThumbnailUrl(Product product) {
        String thumbnailDirectory = FileUtils.getProductDirectoryName(product.name) + FileUtils.URL_PATH_SEPARATOR;
        return DOWNLOAD_PRODUCT_THUMBNAIL + thumbnailDirectory + product.name + (StringUtils.isEmpty(product.pVersion) ? "" : ("_" + product.pVersion)) + "_" + Calendar.getInstance().getTimeInMillis() + ".jpg";


    }


    /**
     * @param file
     * @param newFilePath
     */
    public static void copy(MultipartFile file, String newFilePath) throws IOException {



        FileOutputStream fileOutputStream = null;
        InputStream inputStream=null;
        try {
            com.giants3.hd.utils.FileUtils.makeDirs(newFilePath);
            fileOutputStream = new FileOutputStream(newFilePath);

            inputStream = file.getInputStream();
            IOUtils.copy(inputStream, fileOutputStream);


            fileOutputStream.flush();
        } catch (Throwable  e) {
            throw e;
        }finally {
            IoUtils.safeClose(fileOutputStream);
            IoUtils.safeClose(inputStream);
        }

    }

    /**
     * @param inputStream
     * @param newFilePath
     */
    public static void copy(InputStream inputStream, String newFilePath) throws IOException {



        FileOutputStream fileOutputStream = null;

        try {
            com.giants3.hd.utils.FileUtils.makeDirs(newFilePath);
            fileOutputStream = new FileOutputStream(newFilePath);

            IOUtils.copy(inputStream, fileOutputStream);


            fileOutputStream.flush();
        } catch (Throwable  e) {
            throw e;
        }finally {
            IoUtils.safeClose(fileOutputStream);
            IoUtils.safeClose(inputStream);
        }

    }

    public static String getMaitouFileUrl(String os_no) {




        return  String.format(DOWNLOAD_ORDER_MAITOU,os_no);
    }

    public static File getMaitouFilePath(String maitoufilepath,String os_no) {


       return   new File(maitoufilepath+os_no+".xls");


    }

    /**
     * 缩略图的url  转换成文件路劲
     *
     * @param productFilePath
     * @param thumbnailUrl 相对形式的url  api/ 卡头
     * @return
     */
    public static String convertThumbnailUrlToPath(String productFilePath, String thumbnailUrl) {
        return thumbnailUrl.replace(DOWNLOAD_PRODUCT_PATH, productFilePath).replace(URL_PATH_SEPARATOR, SEPARATOR);


    }

    /**
     * 合成路径
     * @param prePath
     * @param category
     * @param fileName
     */
    public static String combinePath(String prePath, String category, String fileName) {
        return  prePath+ File.separator+category +File.separator+ fileName;
    } public static String combineUrl(String prePath, String category, String fileName) {
        return  prePath+ "/"+category +"/"+ fileName;
    }

    public static String convertUrlToPath(String rootPath,String url)
    {
        return rootPath+url.replace(DOWNLOAD_PATH,"").replace("/",File.separator);
    }
}