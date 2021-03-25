package com.giants3.android.compress;

import com.giants3.ByteArrayPool;
import com.giants3.android.frame.util.Log;
import com.giants3.io.FileUtils;
import com.giants3.tool.zip.ZipJNIInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipHelper {
    public static void unZip(String zipFilePath,String outputFilePath)
    {
        ZipInputStream zis=null;
        try {


              zis=new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry zipEntry       = null;
            while ((zipEntry=zis.getNextEntry())!=null){
                if(zipEntry.isDirectory()){//若是目录
                    File file=new File(outputFilePath+File.separator+zipEntry.getName());
                    if(!file.exists()){
                        file.mkdirs();
                        Log.e("mkdirs:"+file.getCanonicalPath());
                    }
                    continue;
                }//若是文件
                File file = new File(outputFilePath,zipEntry.getName());
                OutputStream os=null ;
                try {
                    if(!file.exists()) file.createNewFile();
                    os=new FileOutputStream(file);
                    FileUtils.copyStream(zis, os);
                }catch (Throwable t)
                {
                    Log.e(t);
                }
                finally {
                    FileUtils.safeClose(os);
                }
                Log.e("file created: " + file.getCanonicalPath());

                Log.e("file uncompressed: " + file.getCanonicalPath());
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {


            FileUtils.safeClose(zis);
        }
    }


}
