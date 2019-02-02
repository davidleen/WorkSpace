package com.giants3.android.frame.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class FileUtils {
    public static void safeClose(Closeable closeable) {


        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }


    public static final String DEFAULT_CHARSET = "UTF-8";

    public static void safeClose(InputStream inputStream) {

        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public static void safeClose(OutputStream outputStream) {

        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    public static void writeStringToFile(String data, String filePath) throws Exception {
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(filePath);
            if (!file.isFile()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(data.getBytes());
        } catch (FileNotFoundException e) {
            throw new Exception(e);

        } catch (IOException e) {
            throw new Exception(e);

        } catch (Throwable t) {
            throw t;

        }

        FileUtils.safeClose(fileOutputStream);

    }

    public static String readStringFromFile(String filePath) {


        String result = null;


        byte[] bytes = readByteFromFile(filePath);
        try {
            result = new String(bytes, DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return result;
    }


    public static boolean copyFile(File destFile, File sourceFile) {


        if (!destFile.exists()) {
            //确保文件夹建立
            destFile.getParentFile().mkdirs();

        }
        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            if (destFile.exists()) {
                destFile.delete();
            }
            in = new FileInputStream(sourceFile);
            out = new FileOutputStream(destFile);

            copyStream(in, out);

            return true;
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } finally {

            safeClose(in);
            safeClose(out);

        }


        return false;


    }


    public static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[0];
        try {
            buffer = ByteArrayPool.getInstance().getBuf(1024 * 5);
            int size;
            while ((size = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, size);
                outputStream.flush();
            }

        } finally {
            ByteArrayPool.getInstance().returnBuf(buffer);
        }
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

    public static final String[] PICTURE_APPENDIX = new String[]{"jpg", "JPEG", "JPG"};


    /**
     * 判断文件是否图片文件
     *
     * @param fileName
     * @return
     */
    public static boolean isPictureFile(String fileName) {


        if (fileName != null) {


            for (String appendix : PICTURE_APPENDIX) {
                if (fileName.toLowerCase().lastIndexOf(appendix.toLowerCase()) > 1)
                    return true;
            }


        }
        return false;


    }

    public static byte[] readByteFromFile(String filePath) {


        byte[] bytes = ByteArrayPool.getInstance().getBuf(1024);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {


            FileInputStream fileInputStream = new FileInputStream(filePath);
            int leng = 0;
            while ((leng = fileInputStream.read(bytes)) > 0) {


                byteArrayOutputStream.write(bytes, 0, leng);

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        ByteArrayPool.getInstance().returnBuf(bytes);
        return byteArrayOutputStream.toByteArray();
    }
}
