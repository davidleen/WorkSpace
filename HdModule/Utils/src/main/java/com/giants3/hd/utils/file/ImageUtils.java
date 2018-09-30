package com.giants3.hd.utils.file;

import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.exception.HdException;
import de.greenrobot.common.io.IoUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Iterator;

/**
 * 图片处理的功能
 */

public class ImageUtils {

    public static final String TAG = "ImageUtils";

    //产品展示默认图尺寸
    public static final int MAX_PRODUCT_MINIATURE_WIDTH = 100;
    public static final int MAX_PRODUCT_MINIATURE_HEIGHT = 100;



    public static final int MAX_PRODUCT_THUMBNAIL_WIDTH = 300;
    public static final int MAX_PRODUCT_THUMBNAIL_HEIGHT = 300;
    //材料微缩图尺寸
    public static final int MAX_MATERIAL_MINIATURE_WIDTH = 50;
    public static final int MAX_MATERIAL_MINIATURE_HEIGHT = 50;


    public static ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    public static MemoryCacheImageOutputStream memoryCacheImageOutputStream = new MemoryCacheImageOutputStream(buffer);

    /**
     * 微缩产品图片
     *
     * @param path
     * @return
     * @throws HdException
     */
    public static final byte[] scaleProduct(String path) throws HdException {

        return scale(path, MAX_PRODUCT_THUMBNAIL_WIDTH, MAX_PRODUCT_THUMBNAIL_HEIGHT);


    }


    /**
     * 微缩材料图片
     *
     * @param path
     * @return
     * @throws HdException
     */
    public static final byte[] scaleMaterial(String path) throws HdException {

        return scale(path, MAX_MATERIAL_MINIATURE_WIDTH, MAX_MATERIAL_MINIATURE_HEIGHT);


    }

    public static final byte[] scale(String path, int maxWidth, int maxHeight) throws HdException {

        return scale(path, maxWidth, maxHeight, false);


    }


    /**
     * 生成缩略图的字节流
     * <p/>
     * 设定最高宽高， 等比例压缩
     *
     * @param filePath
     * @param maxWidth
     * @param maxHeight
     * @return
     * @throws HdException
     */
    public static final byte[] scale(String filePath, int maxWidth, int maxHeight, boolean preserveAlpha) throws HdException {

        File file = new File(filePath);
        if (!file.exists()) return null;

        byte[] result = null;
        FileInputStream fileInputStream = null;
        try {


            fileInputStream = new FileInputStream(filePath);
            result = scale(fileInputStream, maxWidth, maxHeight, preserveAlpha);


        } catch (IOException e) {
            throw HdException.create(HdException.FAIL_SCALE_IMAGE, e);
        } finally {

            IoUtils.safeClose(fileInputStream);
        }

        return result;
    }


    /**
     * 生成缩略图的字节流
     * <p/>
     * 设定最高宽高， 等比例压缩
     *
     * @param url
     * @param maxWidth
     * @param maxHeight
     * @return
     * @throws HdException
     */
    public static final byte[] scale(URL url, int maxWidth, int maxHeight, boolean preserveAlpha) throws HdException {


        byte[] result = null;
        InputStream inputStream = null;

        try {


            inputStream = url.openStream();
            result = scale(inputStream, maxWidth, maxHeight, preserveAlpha);


        } catch (IOException e) {
            throw HdException.create(HdException.FAIL_SCALE_IMAGE, e);
        } finally {
            IoUtils.safeClose(inputStream);

        }

        return result;
    }


    /**
     * 读取缩略图
     * <p/>
     * 提取抽样参数  最小为1
     * <p/>
     * 抽样读图
     *
     * @param inputStream
     * @return
     */
    public static BufferedImage readThumbnailFromFile(InputStream inputStream, int maxWidth, int maxHeight) {


        ImageInputStream stream = null; // File or input stream
        try {
            stream = ImageIO.createImageInputStream(inputStream);

            Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);

            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                reader.setInput(stream);


                float ratio = reader.getAspectRatio(0);
                float width = reader.getWidth(0);
                float height = reader.getHeight(0);
                int sampleSize = Math.max((int) (ratio > 1 ? width / maxWidth : height / maxHeight), 1);

                ImageReadParam param = reader.getDefaultReadParam();
                param.setSourceSubsampling(sampleSize, sampleSize, 0, 0); // Set region
                BufferedImage bufferedImage = reader.read(0, param);
                return bufferedImage;

            }

        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println("================================");

            try {
                BufferedImage bufferedImage = ImageIO.read(inputStream);
                return bufferedImage;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 从大图片中读取指定的区域的图片
     *
     * @return
     */
    public static BufferedImage readRegionFromFile(InputStream inputStream, int x, int y, int w, int h) {


        Rectangle sourceRegion = new Rectangle(x, y, w, h); // The region you want to extract

        ImageInputStream stream = null; // File or input stream
        try {
            stream = ImageIO.createImageInputStream(inputStream);

            Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);

            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                reader.setInput(stream);

                ImageReadParam param = reader.getDefaultReadParam();
                param.setSourceRegion(sourceRegion); // Set region

                BufferedImage image = null; // Will read only the region specified

                image = reader.read(0, param);


                return image;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取图片的类型。如果是 gif、jpg、png、bmp 以外的类型则返回null。
     *
     * @param imageBytes 图片字节数组。
     * @return 图片类型。
     * @throws java.io.IOException IO异常。
     */
    public static String getImageType(final byte[] imageBytes)
            throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(imageBytes);
        ImageInputStream imageInput = ImageIO.createImageInputStream(input);
        Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInput);
        String type = null;
        if (iterator.hasNext()) {
            ImageReader reader = iterator.next();
            type = reader.getFormatName().toUpperCase();
        }

        try {
            return type;
        } finally {
            if (imageInput != null) {
                imageInput.close();
            }
        }
    }

    /**
     * 压缩大图  并转byte
     *
     * @param inputStream
     * @param maxWidth
     * @param maxHeight
     * @param preserveAlpha
     * @return
     */
    public static byte[] scaleLargePictureFile(InputStream inputStream, int maxWidth, int maxHeight, boolean preserveAlpha) {


        BufferedImage bufferedImage = readThumbnailFromFile(inputStream, maxWidth, maxHeight);

        if (bufferedImage == null) return null;
        try {
            try {
                return bufferImageToByte(bufferedImage, preserveAlpha);
            } catch (IOException e) {
                e.printStackTrace();
                throw HdException.create("error occur on buffered image to byte[] ");
            }
        } catch (HdException e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * 图片压缩并转换字节流
     *
     * @param img
     * @param maxWidth
     * @param maxHeight
     * @param preserveAlpha
     * @return
     * @throws HdException
     */
    public static final byte[] scale(BufferedImage img, int maxWidth, int maxHeight, boolean preserveAlpha) throws HdException {


        try {
            int sourceWidth = img.getWidth();
            int sourceHeight = img.getHeight();
            //计算缩放比例
            float ratio = Math.max((float) sourceWidth / maxWidth, (float) sourceHeight / maxHeight);

            ratio = Math.max(ratio, 1);
            int newWidth = (int) (sourceWidth / ratio);
            int newHeight = (int) (sourceHeight / ratio);


            // Logger.getLogger(TAG).info("scaleProduct Image----newWidth:"+newWidth+",newHeight:"+newHeight);
            int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;

            BufferedImage imageBuff = new BufferedImage(newWidth, newHeight, imageType);

            Graphics2D g = imageBuff.createGraphics();
            if (preserveAlpha)
                g.setComposite(AlphaComposite.Src);

            //   g.drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);
            g.drawImage(img, 0, 0, newWidth, newHeight, null);
            g.dispose();
            img.flush();


            return bufferImageToByte(imageBuff, preserveAlpha);

        } catch (IOException e) {
            e.printStackTrace();

            throw HdException.create("图片读取失败");
        }


    }


    /**
     * 图片转换字节流。
     *
     * @param bufferedImage
     * @param preserveAlpha
     * @return
     * @throws IOException
     */
    private static byte[] bufferImageToByte(BufferedImage bufferedImage, boolean preserveAlpha) throws IOException {
        byte[] result = null;
        synchronized (memoryCacheImageOutputStream) {
            memoryCacheImageOutputStream.reset();
            buffer.reset();
            ImageIO.write(bufferedImage, preserveAlpha ? "png" : "jpg", memoryCacheImageOutputStream);
            bufferedImage.flush();
            memoryCacheImageOutputStream.flush();
            memoryCacheImageOutputStream.reset();

            result = buffer.toByteArray();
            buffer.flush();

            buffer.reset();
        }
        return result;
    }

    /**
     * 生成缩略图的字节流
     * <p/>
     * 设定最高宽高， 等比例压缩
     *
     * @param inputStream
     * @param maxWidth
     * @param maxHeight
     * @return
     * @throws HdException
     */
    public static final byte[] scale(InputStream inputStream, int maxWidth, int maxHeight, boolean preserveAlpha) throws HdException, IOException {


        return scaleLargePictureFile(inputStream, maxWidth, maxHeight, preserveAlpha);

        //return scale(ImageIO.read(inputStream), maxWidth, maxHeight, preserveAlpha);


    }

    public static boolean isPictureFile(String fileName) {


        if (StringUtils.isEmpty(fileName)) return false;
        String lowerCaseValue = fileName.toLowerCase();

        return lowerCaseValue.endsWith(".jpg") || lowerCaseValue.endsWith(".jpeg") || lowerCaseValue.endsWith(".png");
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height, int type) throws IOException {
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }
}
