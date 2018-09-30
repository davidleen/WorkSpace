package com.giants3.hd.utils;

import java.io.*;

/**文件辅助类
 * Created by davidleen29 on 2015/8/14.
 */
public class FileUtils {



    public static boolean copyFile(File destFile, File sourceFile)
    {



        if(!destFile.exists())
        {
            //确保文件夹建立
            destFile.getParentFile().mkdirs();

        }
        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            if(destFile.exists()){
                destFile.delete();
            }
            in = new FileInputStream(sourceFile);
            out = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024 * 5];
            int size;
            while ((size = in.read(buffer)) != -1) {
                out.write(buffer, 0, size);
                out.flush();
            }
            return true;
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException ex1) {
            }
        }


        return false;


    }

    /**
     * 根據文件全路徑 構建文件夾
     *
     * @param absoluteFilePath
     */
    public static void makeDirs(String absoluteFilePath) {
        File newFile = new File(absoluteFilePath);
        File parentFile = newFile.getParentFile();
        if (!parentFile.exists())
            parentFile.mkdirs();
//        int
//                index = absoluteFilePath.lastIndexOf(FileUtils.SEPARATOR);
//        if (index > -1) {
//
//            String directoryPath = absoluteFilePath.substring(0, index);
//
//            File file = new File(directoryPath);
//            if (!file.exists()) {
//                file.mkdirs();
//            }
//        }

    }

    public static final String[] PICTURE_APPENDIX=new String[]{"jpg","JPEG","JPG"};


    /**
     * 判断文件是否图片文件
     * @param fileName
     * @return
     */
    public static boolean isPictureFile(String fileName)
    {


        if(fileName!=null)
        {


            for(String appendix:PICTURE_APPENDIX)
            {
                if(fileName.toLowerCase().lastIndexOf(appendix.toLowerCase())>1)
                    return true;
            }


        }
        return false;


    }

    public static void safeClose(InputStream inputStream) {

        if(inputStream!=null)
        {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
}
