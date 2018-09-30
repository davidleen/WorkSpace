package com.giants3.hd.controller;

/**
 * Created by davidleen29 on 2016/7/23.
 */
public class AttachFileUtils {


    public static final String JPG = ".jpg";
    /**
     * 自定义文件的路径间隔符，在url中使用， 在文件路径中 转化成 {@see File#pathSeparator}
     */

    public static final String FILE_SEPARATOR = "_";
    public static final String TEMP_FILE_PREFIX = "TEMP" + FILE_SEPARATOR;


    /**
     * 产品附件图片路径
     */
    public static final String PRODUCT_ATTACH_PREFIX = "Product" + FILE_SEPARATOR;
    /**
     * 产品包装附件图片路径
     */
    public static final String PRODUCT_PACK_ATTACH_PREFIX = "Product" + FILE_SEPARATOR + "Pack" + FILE_SEPARATOR;

    /**
     * 订单包装图片路径
     */
    public static final String ORDER_ATTACH_PREFIX = "Order" + FILE_SEPARATOR;

    /**
     * 附件文件处理   临时文件复制到附件文件夹中， 被移除的文件 删除
     *
     * @param newAttacheFiles   新的附件列表
     * @param oldAttachFiles    旧的附件列表
     * @param filePrefix        新文件名前缀
     * @param destFileDirectory 新文件存放位置。
     * @return
     */
    public static String updateProductAttaches(String newAttacheFiles, String oldAttachFiles, String filePrefix, String destFileDirectory, String tempFileDirectory) {


        return newAttacheFiles;


    }


}
