package com.giants3.hd.domain.api;

import java.io.*;

/**
 * Created by davidleen29 on 2018/9/28.
 */
public class FileUtils {

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

    }

    /**
     * @param inputStream
     * @param newFilePath
     */
    public static void copy(InputStream inputStream, String newFilePath) throws IOException {


        FileOutputStream fileOutputStream = null;

        try {
            FileUtils.makeDirs(newFilePath);
            fileOutputStream = new FileOutputStream(newFilePath);

            copyStream(inputStream, fileOutputStream);


            fileOutputStream.flush();
        } catch (Throwable e) {
            throw e;
        } finally {
            safeClose(fileOutputStream);
            safeClose(inputStream);
        }

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


        FileInputStream in = null;
        FileOutputStream out = null;


        byte[] buffer = new byte[1024 * 5];
        int size;
        while ((size = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, size);
            outputStream.flush();
        }


    }


    public static void safeClose(Closeable closeable) {

        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


}
