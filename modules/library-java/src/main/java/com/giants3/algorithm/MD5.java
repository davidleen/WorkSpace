package com.giants3.algorithm;

import com.giants3.ByteArrayPool;
import com.giants3.io.FileUtils;
import com.giants3.utils.StringUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5 {
    private MessageDigest md5 = null;

    public MD5() {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String MD5_Encode(String string) {
        return getStringHash(string);
    }
    
    public String getStringHash(String source) {
        String hash = null;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(source
                    .getBytes());
            hash = getStreamHash(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }

    public String getFileHash(String filePath) {
        String hash = "";
        try {
        	if(!StringUtils.isEmpty(filePath)){
        		File file=new File(filePath);
        		if(file.exists() && file.isFile()){
        			hash = getStreamHash(new FileInputStream(file));//流在子方法中被关闭
        		}
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }
    
    public String getStreamHash(InputStream stream) {
        if (md5 == null) {
            return "";
        }
        String hash = null;
        byte[] buffer = new byte[1024*8];
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(stream);
            int numRead = 0;
            while ((numRead = in.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
           
            hash = toHexString(md5.digest());
        } catch (Exception e) {
             e.printStackTrace();
        }finally{
            if (in != null){
            	try {
                    in.close();
                } catch (Exception e) {
                	 e.printStackTrace();
                }
            }
        }
        
        return hash;
    }

    private String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    private char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f' };



    /**
     * 文件 md5 加密
     */
    public   String md5File(File file) {

        if (!file.isFile()) {
            return "";
        }

        FileInputStream fileInputStream=null   ;
        try {
            fileInputStream = new FileInputStream(file);
          return   md5Stream(fileInputStream);




        } catch (IOException e) {

        }catch (Throwable t)
        {
            t.printStackTrace();
        }finally {

            FileUtils.safeClose(fileInputStream);
        }

        return "";
    }


    public String md5Stream(InputStream fileInputStream) throws IOException {




            byte[] buffer = ByteArrayPool.getInstance().getBuf();
            int len = 0;

            while ((len = fileInputStream.read(buffer, 0, 1024)) != -1) {
                md5.update(buffer, 0, len);
            }


            byte[] bytes = md5.digest();
            StringBuilder stringBuffer = new StringBuilder();
            for (byte b : bytes) {
                // 获取低八位有效值
                int bt = (int) b & 0xff;
                if (bt < 16) {
                    // 如果是一位的话，补0
                    stringBuffer.append(0);
                }
                // 转化为16进制保存
                stringBuffer.append(Integer.toHexString(bt));
            }
            return stringBuffer.toString();





    }


    public  String md5String(String inputString)
    {

        md5.update(inputString.getBytes());

        byte byteData[] = md5.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        //   System.out.println("Digest(in hex format):: " + sb.toString());

        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<byteData.length;i++) {
            String hex=Integer.toHexString(0xff & byteData[i]);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();


    }
}
