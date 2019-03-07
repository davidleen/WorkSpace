package com.xxx.reader.file;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BaseRandomAccessFile extends RandomAccessFile {
	private boolean isEntryFile = false;
	private int keyIndex = 0;

	public BaseRandomAccessFile(String name, String mode) throws IOException {
		super(name, mode);
		if (OnlineFileEncrypter.isEncryptFile(name)) {
			File file = new File(name);
//			if (OnlineFileEncrypter.isUseNewKey(file)) {   //daiyf 安卓读书 极品小流氓 98章 乱码， 因为这个name.lenth(). 暂时都当做UseNewKey
			int nKeyIndex = (int) (((new File(name)).length()) % 8);
//			} else {
//				keyIndex = (int) (((new File(name)).length() + name.length()) % 8);
//			}
			int checkLength = 100;
			keyIndex = OnlineFileEncrypter.getKeyIndex(file, nKeyIndex, checkLength);
			if (keyIndex == -1) { //特殊章节加大数组长度
				keyIndex = OnlineFileEncrypter.getKeyIndex(file, nKeyIndex, 400);
			}
			/**
			 * {@link OnlineFileEncrypter.encrypt(long fileLength, byte[] in, int begin, int end, int beginFilePos)}
			 * 用的都是文件长度mode%8 作为keyindex
			 */
			if (keyIndex == -1  )
			{
				keyIndex =nKeyIndex;
			}

			isEntryFile = keyIndex != -1;
		}
	}
	
	@Override
	public int read(byte buffer[], int offset, int count) throws IOException {
		long pos = super.getFilePointer();
		int len = super.read(buffer, offset, count);
		if (isEntryFile) {
			OnlineFileEncrypter.decrypt(keyIndex, buffer, 0, len, (int)pos);
		}
		return len;
	}
}
