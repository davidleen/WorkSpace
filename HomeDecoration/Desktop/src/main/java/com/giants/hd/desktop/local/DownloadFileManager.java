package com.giants.hd.desktop.local;

import com.giants.hd.desktop.utils.AccumulateMap;
import com.giants3.hd.utils.StringUtils;
import com.google.inject.Singleton;
import de.greenrobot.common.io.FileUtils;
import de.greenrobot.common.io.IoUtils;
import org.apache.commons.collections.map.LRUMap;

import java.io.*;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 文件下载管理器
 */
@Singleton
public class DownloadFileManager {

    private static final String TAG="DownloadFileManager";
   public static  String localFilePath=LocalFileHelper.path+"/temp/files/";
    String localCachePath=LocalFileHelper.path+"/temp/";

    public static final int CACHE_MAX_SIZE=5000;
    public static final int MAX_HIT_COUNT=10000;


    private AccumulateMap cacheTable ;


     private Map<String,String> lruMap;


    public DownloadFileManager() {


        LRUMap map;
        File f=  new  File(localFilePath);
        if(!f.exists())
        {
            f.mkdirs();
        }

        //初始化cacheTable
        cacheTable=LocalFileHelper.get(AccumulateMap.class);
        if(cacheTable==null)
        {
            cacheTable=new AccumulateMap();
        }


//        LRUMap lruMap;
//        lruMap=Collections.synchronizedMap(new LRUMap(CACHE_MAX_SIZE));

    }

    /**
     * 缓存文件至本地路径
     * @param url
     * @return   文件路径
     */
    public  String cacheFile(String url) throws IOException {



            if(StringUtils.isEmpty(url))
            {
                throw new IOException("file no found");
            }
           String fileName=map(url);



        boolean cached=new File(fileName).exists();

        Logger.getLogger(TAG).info("url:"+url+",fileName:"+fileName+",cached:"+cached);
        //找到缓存
        if(cached)
        {

            accumulateCache(fileName);
            return fileName;
        }


        download(url,fileName);
        accumulateCache(fileName);
        return fileName;

    }


    public void accumulateCache(String fileName)
    {

        cacheTable.accumulate(fileName);
        int cacheSize = cacheTable.size();
        boolean needDownCacheCount=cacheTable.get(fileName)>=MAX_HIT_COUNT ;

        boolean overCount=cacheSize>CACHE_MAX_SIZE;
        //淘汰算法
       if(overCount||needDownCacheCount)
       {





            Iterator iterable = cacheTable.keySet().iterator();
           int minCacheCount=Integer.MAX_VALUE;
           String outKey=null;
            while (iterable.hasNext())
            {
                String key= (String) iterable.next();
                int cacheCount=cacheTable.get(key);

                if(needDownCacheCount)
                {
                    //所有命中开平方
                    cacheTable.put(key,(int)Math.sqrt(cacheCount));
                }
                if(overCount)
                {
                    if(cacheCount<minCacheCount)
                    {
                        minCacheCount=cacheCount;
                        outKey=key;
                    }
                }



            }
           if(overCount&&outKey!=null)
           {

               //removetempFile;

               File file=   new     File(outKey);
               if(file.exists())
               {
                   file.delete();
               }
              // remove this key;
               cacheTable.remove(outKey);




           }

        }







    }




    public  String  map(String url)
    {



        return localFilePath+url.hashCode();

    }


    private  static  void makeDir(String  absoluteFilePath)
    { File newFile = new File(absoluteFilePath);
        File parentFile = newFile.getParentFile();
        if (!parentFile.exists())
            parentFile.mkdirs();

    }

    /**
     * 下载文件 由url 获取文件流 至文件中
     * @param remoteUrl
     * @param filePath
     * @throws IOException
     */
    public  static void download(String remoteUrl,String filePath) throws IOException {




        InputStream is = null;


        BufferedInputStream bis = null;
        FileOutputStream fos = null;


        //打开URL通道
        try {

            makeDir(filePath);
            File newFile = new File(filePath);


            URL url = new URL(remoteUrl);
            is = url.openStream();


            byte[] buffer = new byte[1024];
            int size = 0;


            bis = new BufferedInputStream(is);
            fos = new FileOutputStream(newFile);


            // IoUtils.copyAllBytes(bis,fos);

            //保存文件


            while ((size = bis.read(buffer)) != -1) {
                //读取并刷新临时保存文件
                fos.write(buffer, 0, size);
                fos.flush();

            }

        } catch (Throwable t)
        {

            LocalFileHelper.printThrowable(t);

        }


        finally {

            try {
                if(fos!=null)
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(bis!=null)
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(is!=null)
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }






    }


    /**
     * 清楚全部图片缓存
     */
    public void clearCache() {


        File f=new File(localFilePath);
        if(f.isDirectory())
        {
            for(File childFile:f.listFiles())
            {
                childFile.delete();
            }
        }
    }

    public int getHitCount(String fileName) {

      return   cacheTable.get(fileName);
    }

    public void close() {


        LocalFileHelper.set(cacheTable);

    }
}
