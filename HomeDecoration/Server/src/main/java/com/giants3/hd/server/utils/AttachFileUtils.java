package com.giants3.hd.server.utils;

import com.giants3.hd.utils.*;

import java.io.File;
import java.util.Calendar;

/**
 * Created by davidleen29 on 2016/7/23.
 */
public class AttachFileUtils {


    public static final String JPG = ".jpg";
    /**
     *  自定义文件的路径间隔符，在url中使用， 在文件路径中 转化成 {@see File#pathSeparator}
     */

    public static final String FILE_SEPARATOR="_";
    public static final String TEMP_FILE_PREFIX = "TEMP"+FILE_SEPARATOR;


    private static final String TEMP_API_HEAD= FileUtils.DOWNLOAD_TEMP_PATH;
    /**
     * 产品附件图片路径
     */
    public static final String PRODUCT_ATTACH_PREFIX = "Product"+FILE_SEPARATOR;
    /**
     * 产品包装附件图片路径
     */
    public static final String PRODUCT_PACK_ATTACH_PREFIX = "Product"+FILE_SEPARATOR+"Pack"+FILE_SEPARATOR;

    /**
     * 订单包装图片路径
     */
    public static final String ORDER_ATTACH_PREFIX = "Order"+FILE_SEPARATOR;

    /**
     * 附件文件处理   临时文件复制到附件文件夹中， 被移除的文件 删除
     * @param newAttacheFiles    新的附件列表
     * @param oldAttachFiles   旧的附件列表
     * @param filePrefix  新文件名前缀
     *                    @param destFileDirectory  新文件存放位置。
     * @return
     */
    public static  String  updateProductAttaches(String newAttacheFiles, String oldAttachFiles,String filePrefix,String destFileDirectory,String tempFileDirectory) {



         return newAttacheFiles;


    }



    /**   转换旧的临时文件，到 附件文件夹下。 图片归档处理。
     *
     * @param oldAttaches       旧的附件数据
     * @param attachFilePath   //附件文件夹存放路径
     * @param tempFilePath     临时文件夹存放路径
     * @param fileNamePrefix  文件前前缀  产品
     */
    public static  String   getNewAttaches(String oldAttaches,String attachFilePath,String tempFilePath,String fileNamePrefix)
    {

        String[] attachFiles = StringUtils.split(oldAttaches);

        final int length = attachFiles.length;

        for (int i = 0; i < length; i++) {
            String oldAttach = attachFiles[i];

            String tempFileFullPath = FileUtils.getTempFileName(oldAttach, tempFilePath);
            if (!StringUtils.isEmpty(tempFileFullPath)) {

                //临时文件处理


                //移动至附件文件夹
                File sourceFile = new File(tempFileFullPath);
                if (sourceFile.exists()) {

                    String url =fileNamePrefix+"-"+Calendar.getInstance().getTimeInMillis()+JPG;
                    String filePath = attachFilePath + url.replace(FILE_SEPARATOR,File.pathSeparator);

                    boolean copied = com.giants3.hd.utils.FileUtils.copyFile(new File(filePath), sourceFile);
                    if (copied) {
                        sourceFile.delete();
                        attachFiles[i] = FileUtils.DOWNLOAD_ATTACH_PATH+url;
                    }
                }

            }
        }
        return StringUtils.combine(attachFiles);


    }



}
