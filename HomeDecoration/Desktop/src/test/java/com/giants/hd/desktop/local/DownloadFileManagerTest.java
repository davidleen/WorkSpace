//package com.giants.hd.desktop.local;
//
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.IOException;
//
///**
// * Created by david on 2015/10/19.
// */
//public class DownloadFileManagerTest {
//
//
//    @Test
//    public void cacheUrl() throws IOException {
//
//
//        DownloadFileManager downloadFileManager=new DownloadFileManager();
//        String url="http://localhost:8080/api/file/download/product/10X1234/308118/1407115010000.jpg?type=jpg";
//        for(int i=0;i<1000;i++) {
//            String fileName=downloadFileManager.map(url);
//            int formalHitCount= downloadFileManager.getHitCount(fileName);
//
//            downloadFileManager.cacheFile(url);
//           int hitCount= downloadFileManager.getHitCount(fileName);
//
//            Assert.assertEquals(formalHitCount+1,hitCount);
//
//
//
//        }
//
//
//
//
//    }
//
//    @Before
//    public void setUp() throws Exception {
//
//    }
//
//    @After
//    public void tearDown() throws Exception {
//
//    }
//
//
//}