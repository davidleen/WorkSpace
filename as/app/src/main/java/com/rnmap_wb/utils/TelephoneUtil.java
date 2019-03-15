package com.rnmap_wb.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.lang.reflect.Method;

import static com.rnmap_wb.MainApplication.baseContext;

//import org.apache.commons.codec.digest.DigestUtils;


public class TelephoneUtil {
	public static final String SIM_STATE_READY	= "1";
	public static final String SIM_STATE_ABSENT	= "0";
	

	/**
	 * 获取真实imei
	 * @return
	 */
	public static String getFixIMEI() {

	return 	PhoneStateHandler.getDeviceId(baseContext);

	}
	
	public static String getIMSI() {

		return PhoneStateHandler.getSubscriberId(baseContext);
	}

//	/**
//	 * 对字符串进行MD5摘要并将结果值以HEX（十六进制）进行编码
//	 * @param coder
//	 * @return
//	 */
//	public static String getMD5Hex(String coder){
//		String sign = DigestUtils.md5Hex(coder + "!!)@)^@$");
//		return sign;
//	}
	
	/**
	 * 取得XML的sessionid
	 * @param xml
	 * @return
	 */
	public static String getSessionId(String xml){
		return getXMLValueByTag(xml, "sessionid");
	}
	
	/**
	 * 取得xml中tag对应的Value值
	 * @param xml
	 * @param tag
	 * @return
	 */
	public static String getXMLValueByTag(String xml, String tag){
		if(xml == null || xml.equals(""))
			return null;
		int start = xml.toLowerCase().indexOf("<"+tag.toLowerCase() + ">") + tag.length()+ 2 ;
		int end = xml.toLowerCase().indexOf("</"+tag.toLowerCase() +">", start);
		String str = xml.substring(start, end);
		return str;
	}
	



	

	
	/**
	 * wifi是否启动
	 * @param
	 * @return
	 */
	public static boolean isWifiEnable(){
		WifiManager wf = (WifiManager)baseContext.getSystemService(Context.WIFI_SERVICE);
		if(wf.getWifiState() == WifiManager.WIFI_STATE_ENABLED){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否通话中
	 * @return
	 */
	public static boolean isPhoneInUse() {
		boolean phoneInUse = false;
		try {

            if (Build.VERSION.SDK_INT >= 23) {//android6.0 使用这个方法获取手机使用状态
                Object tm =  baseContext.getSystemService("telecom");
                Method isInCall =  tm.getClass().getDeclaredMethod("isInCall");
                isInCall.setAccessible(true);
                Object result = isInCall.invoke(tm);
                if (result instanceof Boolean){
                    phoneInUse = (Boolean)result;
                }
            }
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return phoneInUse;
	}
}
