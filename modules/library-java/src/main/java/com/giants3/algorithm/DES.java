package com.giants3.algorithm;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

public class DES {
	public static String DES_STRING = "444553";  //"DES"
	private static String strDefaultKey = "national";

	private Cipher encryptCipher = null;

	private Cipher decryptCipher = null;

	public static String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	public static byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	public DES() throws Exception {
		this(strDefaultKey);
	}

	public DES(String strKey) throws Exception {

		Key key = getKey(strKey.getBytes());

		encryptCipher = Cipher.getInstance("DES");
		encryptCipher.init(Cipher.ENCRYPT_MODE, key);

		decryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(strKey.getBytes("UTF-8"));     
		decryptCipher.init(Cipher.DECRYPT_MODE, key, iv);
	}

	public byte[] encrypt(byte[] arrB) throws Exception {
		return encryptCipher.doFinal(arrB);
	}

	public String encrypt(String strl) throws Exception {
		String strResult = "";

		byte[] temp=encrypt(strl.getBytes());
		strResult=Encode(temp,temp.length);
		strResult=Encode(strResult.getBytes(),strResult.getBytes().length);
		return strResult;
	}

	public byte[] decrypt(byte[] arrB) throws Exception {
		return decryptCipher.doFinal(arrB);
	}

	public String decrypt(String strl) throws Exception {
		String strResult = "";
		byte[] szBuffer = new byte[8];
		byte[] szData = Decode(strl.getBytes(), strl.getBytes().length).getBytes();
		szData=Decode(szData,szData.length).getBytes();
		int pos = 0, len = szData.length;

		while (pos < len) {
			if (pos + 8 <= len) {
				for (int i = 0; i < 8; i++)
					szBuffer[i] = szData[i + pos];
			} else {
				for (int i = 0; i < len - pos; i++)
					szBuffer[i] = szData[i + pos];

			}

			byte[] s = decrypt(szBuffer);
			strResult += new String(s);
			pos += 8;
		}
		return strResult;

	}


	private Key getKey(byte[] arrBTmp) throws Exception {

		byte[] arrB = new byte[8];

		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}
		Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

		return key;
	}

	public static String Encode(byte[] Str, int len) {
		char[] Hexcode = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		String szResultStr = "";
		if (Str == null) {
			return szResultStr;
		}
		for (int i = 0; i < len; i++) {
			int c = Str[i];
			if (c < 0)
				c += 256;
			if ((c >= 'a' && c <= 'z'))
			{
				szResultStr += (char) (c - 'a' + ('a'));
			} else if (c >= 'A' && c <= 'Z') {
				szResultStr += (char) (c - 'A' + ('A'));
			} else if (c >= '0' && c <= '9') {
				szResultStr += (char) (c - '0' + ('0'));
			} else {
				szResultStr += ('%');
				szResultStr += Hexcode[c / 16];
				szResultStr += Hexcode[c % 16];
			}
		}
		return szResultStr;
	}

	public static String Decode(byte[] Str, int len) {
		if (Str == null) {
			return "";
		}
		byte[] szBuffer = new byte[len + 2];
		for (int i = 0; i < len + 2; i++) {
			szBuffer[i] = 0;
		}
		int i, j;
		for (i = 0, j = 0; i < len; i++, ++j) {
			byte c = Str[i];
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
					|| (c >= '0' && c <= '9'||c=='.')) {
				szBuffer[j] += c;
			} else if (Str[i] == 0) {
				break;
			} else {
				c = Str[++i];
				if (c >= '0' && c <= '9')
					c = (byte) (c - '0');
				else if (c >= 'a' && c <= 'z')
					c = (byte) (c - 'a' + 10);
				else
					c = (byte) (c - 'A' + 10);
				szBuffer[j] += 16 * c;
				c = Str[++i];
				if (c >= '0' && c <= '9')
					c = (byte) (c - '0');
				else if (c >= 'a' && c <= 'z')
					c = (byte) (c - 'a' + 10);
				else
					c = (byte) (c - 'A' + 10);
				szBuffer[j] += c;
			}
		}
		szBuffer[j] = 0;
		String ret = new String(szBuffer);
		return ret;
	}
}
