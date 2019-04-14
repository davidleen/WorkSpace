package com.giants3.tool.zip;



public class ZipJNIInterface {
	
	static {
		System.loadLibrary( "unzip");
	}




	 

	/*
	 * UnZip 居功返回true 反之false    
	 * strZipFile Zip文件所在的完整路径
	 * strSubFileName 要解压的文件  
	 * strSavePath 保存解压后文件的完全路径
	 * strZipencoding 新增的Zip文件里的文件名编码
	 */
	public static native boolean UnZip(String strZipFile, String strSubFileName, String strSavePath, String strZipencoding);
	
	
	/**
	 * 获取zip文件列表（包含所有的文件夹和文件）
	 * @param strZipFileName 	zip path
	 * @return
	 */
	public static native Object getZipEntries(String strZipFileName);

}
