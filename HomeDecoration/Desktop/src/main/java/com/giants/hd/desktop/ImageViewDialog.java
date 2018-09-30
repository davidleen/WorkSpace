package com.giants.hd.desktop;

import com.giants.hd.desktop.interf.Iconable;
import com.giants.hd.desktop.local.ImageLoader;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 图片显示对话框  只接受绝对路径处理
 */
public class ImageViewDialog extends JDialog {
    private JPanel contentPane;
    private JLabel picture;

    private JLabel message;
    private JButton previous;
    private JButton next;

    private int currentIndex=-1;

    public String[] urls;

    final Dimension maxSize=new Dimension();

    private ImageViewDialog(Window frame) {

        super(frame);
        setContentPane(contentPane);

        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setMinimumSize(new Dimension(600, 200));
        setLocation((dimension.width - 600) / 2, (dimension.height - 200) / 2);

        int maxWidth= (int) (dimension.getWidth()*9/10);
        int maxHeight= (int) (dimension.getHeight()*9/10);
        maxSize.width=maxWidth;
        maxSize.height=maxHeight;

        picture.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
              //  System.out.println("keyTyped:"+e);

            }


            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
               // System.out.println("keyPressed:"+e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
            //    System.out.println("keyReleased:"+e);
                switch ( e.getKeyCode())
                {
                    case KeyEvent.VK_LEFT:
                        if(canShowPrevious()) {
                            currentIndex--;
                            updateUi();
                        }
                        break;
                    case KeyEvent.VK_RIGHT:


                        if(canShowNext()) {
                            currentIndex++;
                            updateUi();
                        }

                        break;

                }
            }
        });

     previous.setVisible(false);
       next.setVisible(false);
         message.setVisible(false);
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(canShowPrevious()) {
                    currentIndex--;
                    updateUi();
                }
            }
        });
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(canShowNext()) {
                    currentIndex++;
                    updateUi();
                }
            }
        });
        picture.setFocusable(true);


    }
    /**
     * 是否可以显示下一张
     * @return
     */
    boolean canShowNext()
    {
        return urls!=null&&currentIndex>=0&&currentIndex<urls.length-1;
    }

    /**
     * 是否可以显示上一张
     * @return
     */
    boolean canShowPrevious()
    {
        return urls!=null&&currentIndex>=1&&currentIndex<urls.length;
    }

    /**
     * 是否多图显示
     * @return
     */
    boolean hasMorePicture()
    {
     return    urls!=null&& urls.length>1;
    }

    public void loadImageAndShow(final String url) {

        loadImageAndShow(url,0);

    }

    public void loadImageAndShow(final String url,int index) {
        urls =StringUtils.split(url);
        currentIndex=index;
        updateUi();

        setVisible(true);
    }

    /**
     *
     */
    private void updateUi()
    {

        //控制上一张 下一张 等显示
        previous.setEnabled(canShowPrevious());
        next.setEnabled(canShowNext());

        previous.setVisible(hasMorePicture());
        next.setVisible(hasMorePicture());
        message.setVisible(urls!=null);
        if(urls!=null&& currentIndex>=0&&currentIndex<urls.length)

             message.setText(urls[currentIndex]+"     ("+(currentIndex+1)+"/"+urls.length+")");
        revalidate();


        loadImage((urls!=null&& currentIndex>=0&&currentIndex<urls.length)? urls[currentIndex]:"");
    }

    /**
     * 读取图片并显示
     * @param url
     */
    private void loadImage(String url)
    {
        picture.setText("正在加载图片....");
        final Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        //加载图片为最大为屏幕3/4

        ImageLoader.getInstance().displayImage(new Iconable() {
            @Override
            public void setIcon(ImageIcon icon, String url) {
                picture.setText("");
                picture.setIcon(icon);
                picture.requestFocusInWindow();

                   Dimension toShow=new Dimension(  icon.getIconWidth() ,icon.getIconHeight() );

                setSize(toShow);
                setLocation((int) Math.max(0,(dimension.width - toShow.getWidth()) / 2), (int) Math.max(0, (dimension.height - toShow.getHeight()) / 2));

            }

            @Override
            public void onError(String message) {
                picture.setText(message);
            }
        }, url, maxSize.getWidth(), maxSize.getHeight());

    }

    /**
     * 显示图片显示框体框
     *
     * @param materialUrl
     */
    public static void showMaterialDialog(Window frame, String title, String materialUrl) {


        String url = HttpUrl.loadMaterialPicture(materialUrl);
        showDialog(frame, url, title);

    }

    /**
     * 显示图片显示框体框
     *
     * @param productName
     */
    public static void showProductDialog(Window frame, String productName, String version, String productUrl) {

        String url = HttpUrl.loadProductPicture(productUrl);
        showDialog(frame, url, StringUtils.isEmpty(version) ? productName : (productName + "-" + version));
    }


    /**
     * 显示图片显示框体框
     *
     * @param
     */
    public static void showImage(Window frame, String url, String title) {

        String destUrl = HttpUrl.loadPicture(url);


        showDialog(frame, destUrl, title);
    }

    /**
     * 显示图片显示框体框
     *
     * @param url
     */
    public static void showDialog(Window frame, String url) {
        showDialog(frame, url, url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".")));

    }
    /**
     * 显示图片显示框体框
     * url 为绝对路径  duotu以分号分开。
     *
     * @param url
     */
    public static void showDialog(Window frame, String url, String title) {
        showDialog(frame,url,title,0);

    }
    /**
     * 显示图片显示框体框
     * url 为绝对路径  duotu以分号分开。
     *
     * @param url
     * @param  index  当前显示索引
     */
    public static void showDialog(Window frame, String url, String title,int index) {
        ImageViewDialog dialog = new ImageViewDialog(frame);
        dialog.setTitle(title);
        dialog.loadImageAndShow(url,index);


    }

//    /**
//     * 显示图片显示框体框
//     * @param url
//     */
//    public static void showDialog(Window frame,String url,String title) {
//        ImageViewDialog dialog=new ImageViewDialog(frame);
//        dialog.setTitle(title);
//        dialog.loadImageAndShow(url);
//
//
//    }


}
