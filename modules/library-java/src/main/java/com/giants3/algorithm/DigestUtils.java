package com.giants3.algorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**数字签名工具类
 * Created by davidleen29 on 2015/8/13.
 */
public class DigestUtils {

    public static String md5(String inputString)
    {
      return new MD5().md5String(inputString);
    }
}
