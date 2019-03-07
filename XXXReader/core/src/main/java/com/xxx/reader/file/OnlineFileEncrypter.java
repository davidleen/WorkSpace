package com.xxx.reader.file;

import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.StorageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;


public class OnlineFileEncrypter {
    private static final byte[] Key = {0, 0, 0, 0, 1, 2, 1, 2, 7, 5, 3, 3, 4, 0, 6, 7,
            0, 5, 3, 2, 4, 0, 2, 7, 0, 5, 0, 3, 1, 4, 6, 7,
            7, 5, 1, 4, 4, 5, 6, 4, 5, 5, 3, 6, 4, 0, 6, 2,
            6, 5, 3, 4, 4, 4, 6, 7, 6, 5, 1, 3, 7, 0, 4, 7,
            1, 5, 3, 3, 6, 0, 7, 1, 1, 2, 6, 4, 4, 4, 1, 4,
            2, 5, 6, 3, 3, 0, 1, 7, 3, 5, 4, 3, 3, 0, 7, 4,
            3, 2, 3, 7, 7, 5, 6, 7, 7, 5, 6, 3, 2, 5, 6, 3,
            5, 0, 7, 2, 6, 2, 1, 3, 7, 5, 7, 3, 4, 0, 6, 7,
            5, 1, 2, 7, 4, 4, 6, 2, 7, 5, 3, 6, 3, 2, 1, 2,
            5, 3, 2, 3, 4, 4, 6, 2, 1, 2, 6, 4, 5, 4, 1, 4,
            2, 3, 3, 3, 3, 0, 6, 7, 2, 4, 4, 3, 4, 0, 4, 4,
            3, 2, 7, 7, 7, 5, 6, 1, 7, 3, 6, 3, 4, 7, 6, 3,
            5, 7, 3, 2, 4, 2, 4, 3, 2, 6, 7, 3, 5, 0, 6, 7,
            5, 5, 2, 3, 1, 4, 6, 2, 7, 7, 3, 6, 4, 2, 1, 6,
            5, 7, 2, 0, 1, 4, 6, 2, 1, 2, 6, 4, 3, 4, 1, 4,
            2, 4, 3, 7, 3, 0, 6, 7, 7, 5, 4, 6, 4, 0, 4, 4,
            3, 2, 3, 2, 7, 5, 6, 7, 7, 5, 6, 3, 4, 5, 6, 3,
            5, 5, 3, 2, 4, 2, 4, 3, 6, 5, 7, 3, 4, 0, 6, 4,
            7, 5, 0, 4, 4, 4, 6, 2, 7, 7, 3, 6, 3, 2, 1, 6,
            5, 5, 2, 3, 6, 4, 6, 2, 1, 2, 6, 4, 5, 4, 1, 4,
            2, 7, 3, 4, 3, 0, 1, 7, 2, 4, 4, 3, 4, 0, 4, 1,
            3, 2, 3, 2, 7, 5, 6, 7, 7, 5, 6, 3, 3, 5, 6, 3,
            5, 5, 7, 2, 1, 2, 4, 3, 2, 5, 7, 4, 4, 6, 6, 7,
            4, 4, 5, 3, 7, 4, 6, 2, 7, 4, 3, 6, 1, 2, 1, 6,
            5, 5, 2, 3, 6, 4, 6, 2, 1, 2, 6, 4, 3, 7, 1, 4,
            2, 1, 0, 7, 1, 0, 1, 7, 7, 5, 4, 3, 4, 0, 4, 4,
            3, 2, 3, 2, 7, 5, 6, 7, 4, 5, 1, 3, 3, 5, 6, 3,
            5, 7, 3, 2, 4, 0, 4, 3, 7, 5, 7, 4, 4, 0, 6, 7,
            5, 5, 2, 3, 6, 4, 6, 2, 4, 5, 3, 6, 7, 2, 1, 6,
            5, 5, 2, 5, 4, 4, 6, 2, 1, 2, 6, 4, 5, 4, 1, 4,
            2, 6, 3, 5, 3, 0, 1, 7, 7, 5, 4, 1, 4, 0, 4, 4,
            3, 2, 3, 2, 7, 5, 6, 7, 7, 5, 6, 0, 2, 5, 6, 3,
            5, 5, 3, 1, 6, 2, 4, 3, 7, 5, 7, 3, 2, 0, 6, 7,
            3, 5, 7, 3, 4, 4, 6, 2, 7, 3, 3, 6, 1, 2, 1, 6,
            1, 0, 2, 3, 4, 4, 6, 2, 1, 2, 6, 4, 4, 4, 1, 4,
            2, 0, 3, 1, 3, 0, 1, 7, 7, 5, 4, 3, 5, 0, 5, 4,
            3, 2, 3, 2, 7, 5, 6, 7, 4, 5, 6, 3, 4, 5, 6, 3,
            5, 5, 3, 2, 4, 2, 4, 3, 7, 5, 7, 3, 3, 0, 6, 7,
            5, 5, 2, 3, 4, 4, 6, 2, 7, 7, 3, 6, 4, 2, 1, 6,
            4, 5, 2, 3, 4, 4, 6, 2, 1, 2, 6, 4, 4, 4, 1, 4,
            2, 5, 6, 3, 3, 0, 1, 7, 5, 5, 4, 3, 1, 0, 4, 4,
            3, 2, 3, 2, 7, 5, 6, 5, 7, 5, 6, 3, 4, 5, 6, 3,
            5, 6, 3, 2, 4, 5, 0, 3, 4, 5, 7, 3, 1, 0, 6, 4,
            5, 3, 2, 7, 4, 4, 6, 2, 7, 5, 3, 6, 2, 2, 1, 6,
            5, 5, 2, 1, 4, 4, 6, 2, 1, 2, 3, 4, 7, 4, 1, 4,
            2, 5, 3, 3, 3, 0, 1, 7, 7, 5, 4, 3, 4, 0, 4, 4,
            3, 2, 6, 7, 7, 5, 6, 7, 5, 5, 6, 3, 3, 3, 6, 3,
            1, 6, 3, 2, 4, 2, 4, 3, 7, 6, 7, 3, 3, 0, 6, 5,
            5, 5, 2, 3, 0, 4, 6, 2, 4, 5, 3, 6, 4, 2, 1, 6,
            1, 7, 2, 7, 3, 4, 6, 2, 6, 3, 6, 3, 0, 6, 1, 6,
            5, 6, 3, 2, 4, 5, 0, 3, 4, 5, 7, 3, 1, 0, 6, 4,
            5, 3, 2, 7, 4, 4, 6, 2, 7, 3, 3, 6, 2, 2, 1, 6,
            3, 5, 2, 1, 4, 4, 6, 2, 1, 2, 3, 4, 7, 4, 1, 4,
            2, 4, 3, 3, 3, 0, 1, 7, 7, 2, 4, 3, 4, 0, 4, 4,
            3, 2, 6, 3, 7, 5, 6, 7, 5, 5, 6, 3, 3, 2, 6, 3,
            1, 6, 3, 2, 4, 5, 4, 3, 7, 6, 0, 3, 3, 0, 6, 2,
            5, 5, 2, 7, 0, 4, 6, 2, 7, 6, 3, 6, 4, 2, 1, 6,
            1, 7, 4, 7, 3, 4, 6, 2, 6, 4, 3, 3, 0, 6, 1, 1};

    private static final byte[][] DATA =
            {{0x00, 0x01, 0x04, 0x08, 0x10, 0x20, 0x40, 0x79},
                    {0x00, 0x55, 0x04, 0x4f, 0x4c, 0x37, 0x59, 0x1c},
                    {0x00, 0x37, 0x55, 0x07, 0x50, 0x64, 0x30, 0x66},
                    {0x00, 0x19, 0x04, 0x78, 0x10, 0x20, 0x25, 0x2c},
                    {0x00, 0x38, 0x44, 0x39, 0x73, 0x42, 0x7d, 0x79},
                    {0x00, 0x03, 0x04, 0x64, 0x22, 0x5e, 0x40, 0x0d},
                    {0x00, 0x5f, 0x07, 0x08, 0x1f, 0x04, 0x44, 0x52},
                    {0x00, 0x51, 0x04, 0x2f, 0x10, 0x4f, 0x01, 0x30}};

    public static final String ENCRYPT_FLAG = ".NDE";
    public static final String ENCRYPT_ALL_FLAG = "NDE";
    public static final String TAG_PATH = "/download/tag";
    private static long TAG_TIME = -1;

    public static final void encrypt(long fileLength, byte[] in, int begin, int end, int beginFilePos) {
        if (begin < 0) {
            return;
        }
        int keyIndex = (int) (fileLength % 8);
        for (int i = beginFilePos + begin; i < beginFilePos + end && begin < in.length; i++) {
            in[begin] = (byte) (in[begin] ^ DATA[keyIndex][Key[i % Key.length]] & 0xff);
            begin++;
        }
    }

    public static final int getEncryptLength() {
        return Key.length;
    }

    public static final void decrypt(int keyIndex, byte[] in, int begin, int end, int beginFilePos) {
        if (begin < 0) {
            return;
        }

        for (int i = beginFilePos + begin; i < beginFilePos + end && begin < in.length; i++) {
            in[begin] = (byte) (in[begin] ^ DATA[keyIndex][Key[i % Key.length]] & 0xff);
            begin++;
        }
    }

    private static boolean check(int key, byte[] byteSrc, byte[] byteRet, int checkLength) {
        System.arraycopy(byteSrc, 0, byteRet, 0, checkLength);
        OnlineFileEncrypter.decrypt(key, byteRet, 0, checkLength, 0);
        return TextCharsetDetector.IsUTF8(byteRet, checkLength);
    }

    public static int getKeyIndex(File file, int currentKeyIndex, int checkLength) {
//		int checkLength = 120;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            if (file.length() < checkLength) {
                checkLength = (int) file.length();
            }
            byte[] bytes = new byte[checkLength];
            fileInputStream.read(bytes);
            byte[] tempBytes = new byte[checkLength];


            int keyIndex = currentKeyIndex;
            if (check(keyIndex, bytes, tempBytes, checkLength)) {
                return keyIndex;
            }

            for (keyIndex = 0; keyIndex < 8; keyIndex++) {
                if (check(keyIndex, bytes, tempBytes, checkLength)) {
                    return keyIndex;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    public static final void createEntryFileFlag(String path) {
        if (path.toLowerCase().endsWith(".txt")) {
            int index = path.lastIndexOf('/');
            String entryAll = path.substring(0, index + 1) + ENCRYPT_ALL_FLAG;
            File file = new File(entryAll);
            if (file.exists()) {
                return;
            }

            File dir = new File(path.substring(0, index));
            String[] list = dir.list();
            boolean hasChapterFile = false;
            for (int i = 0; i < list.length; i++) {
                if (list[i].toLowerCase().endsWith(".txt")) {
                    hasChapterFile = true;
                }
            }

            FileWriter fileWriter = null;
            if (hasChapterFile) {
                file = new File(path.replace(".txt", ENCRYPT_FLAG));
                if (file.exists()) {
                    return;
                }
            }
            try {
                file.createNewFile();
                fileWriter = new FileWriter(file);
                fileWriter.write("");
                fileWriter.close();

            } catch (IOException e) {
                Log.e(e);
            } finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException e1) {
                        Log.e(e1);
                    }
                }
            }
        }
    }

    public static boolean isUseNewKey(File file) {
        if (TAG_TIME == -1) {
            File tagFile = new File(StorageUtils.getShelfRootPath() + TAG_PATH);
            if (tagFile != null && tagFile.exists()) {
                TAG_TIME = tagFile.lastModified();
            }
        }

        return TAG_TIME > 0 && file.lastModified() > TAG_TIME;
    }

    public static void update() {
        try {
            File tagFile = new File(StorageUtils.getAbsolutePathIgnoreExist(TAG_PATH));

            if (!tagFile.exists()) {
                tagFile.getParentFile().mkdirs();
                FileWriter fileWriter = null;
                try {
                    tagFile.createNewFile();
                    fileWriter = new FileWriter(tagFile);
                    fileWriter.write("");
                    fileWriter.close();
                    TAG_TIME = tagFile.lastModified();
                    return;
                } catch (IOException e) {
                    Log.e(e);
                } finally {
                    if (fileWriter != null) {
                        try {
                            fileWriter.close();
                        } catch (IOException e1) {
                            Log.e(e1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TAG_TIME = 0;
    }

    public static boolean isEncryptFile(String filePath) {
        if (filePath.indexOf(StorageUtils.getShelfRootPath() + "/download") != -1 && filePath.toLowerCase().endsWith(".txt")) {
            int index = filePath.lastIndexOf('/');
            String entryAll = filePath.substring(0, index + 1) + ENCRYPT_ALL_FLAG;
            File file = new File(entryAll);
            if (file.exists()) {
                return true;
            }
            if (new File(filePath.toLowerCase().replace(".txt", OnlineFileEncrypter.ENCRYPT_FLAG)).exists()) {
                return true;
            }
            return checkIsEncryptFile(new File(filePath));
        }
        return false;
    }

    public static boolean checkIsEncryptFile(File file) {
        int checkLength = 100;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            if (file.length() < checkLength) {
                checkLength = (int) file.length();
            }
            byte[] bytes = new byte[checkLength];
            fileInputStream.read(bytes);

            return !TextCharsetDetector.IsUTF8(bytes, checkLength);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

}
