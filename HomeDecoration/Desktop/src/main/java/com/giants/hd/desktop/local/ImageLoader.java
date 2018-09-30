package com.giants.hd.desktop.local;

import com.giants.hd.desktop.interf.Iconable;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Guice;
import de.greenrobot.common.io.IoUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片加载类
 */
public class ImageLoader {
    private static ImageLoader ourInstance = null;

    public static synchronized ImageLoader getInstance() {

        if (ourInstance == null) {
            ourInstance = new ImageLoader();
        }
        return ourInstance;
    }

    DownloadFileManager manager;

    private ImageLoader() {


        manager = Guice.createInjector().getInstance(DownloadFileManager.class);

    }

    public void displayImage(final Iconable iconable, final String url) {


        displayImage(iconable, url, 120, 120);
    }


    public void displayImage(final Iconable iconable, final String url, final double maxWidth, final double maxHeight) {


        Observable.create(new Observable.OnSubscribe<ImageIcon>() {
            @Override
            public void call(Subscriber<? super ImageIcon> subscriber) {

                try {
                    String fileName = manager.cacheFile(url);
                    BufferedImage bufferedImage=null;
                    InputStream in=null;
                    try {
                        in = new ByteArrayInputStream(ImageUtils.scale(fileName, (int) maxWidth, (int) maxHeight, true));
                        bufferedImage = ImageIO.read(in);

                    }catch (Throwable t)
                    {
                        t.printStackTrace();
                    }finally {
                        IoUtils.safeClose(in);
                    }

                    byte[] result = ImageUtils.scale(bufferedImage, (int) maxWidth, (int) maxHeight, false);
                    ImageIcon imageIcon = new ImageIcon(result);

                    subscriber.onNext(imageIcon);
                    subscriber.onCompleted();
                } catch (IOException e) {

                    subscriber.onError(e);
                } catch (HdException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
                catch(Throwable t)
                {
                    t.printStackTrace();
                    subscriber.onError(t);
                }


            }
        })
//                .map(new Func1<ImageIcon, ImageIcon>() {
//            @Override
//            public ImageIcon call(ImageIcon imageIcon) {
//
//
//            }
//        })
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.immediate()).subscribe(new Observer<ImageIcon>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                e.printStackTrace();
                iconable.onError("图片加载失败");
            }

            @Override
            public void onNext(ImageIcon bufferedImage) {
                iconable.setIcon(bufferedImage,url );
            }
        });


    }
    private BufferedImage scaleImage(BufferedImage bufferedImage, double maxWidth, double maxHeight) {
        if (bufferedImage == null) return null;
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();


        while (!(width < maxWidth && height < maxHeight)) {
            width = (int) (width / 1.1f);
            height = (int) (height / 1.1f);
        }


        BufferedImage newImage = null;
        try {
            newImage = ImageUtils.resizeImage(bufferedImage, width, height, bufferedImage.getType());

            bufferedImage.flush();
            return newImage;

        } catch (IOException e) {
            e.printStackTrace();
            return bufferedImage;

        }
    }


    public String cacheFile(String url) throws IOException {


        return manager.cacheFile(url);

    }


    public BufferedImage loadImage(String url) throws IOException {
        return loadImage(url, -1, -1);
    }


    public BufferedImage loadImage(String url, final double maxWidth, final double maxHeight) throws IOException {
        String fileName = manager.cacheFile(url);

        if (maxHeight <= 0 || maxWidth <= 0)
            return ImageIO.read(new File(fileName));

        return scaleImage(ImageIO.read(new File(fileName)), maxWidth, maxHeight);


    }


    public void clearCache() {
        manager.clearCache();
    }


    public void close() {


        manager.close();
    }
}
