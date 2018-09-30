package com.giants.hd.desktop.widget;

import com.giants.hd.desktop.ImageViewDialog;
import com.giants.hd.desktop.interf.Iconable;
import com.giants.hd.desktop.local.ImageLoader;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 附件列表图片
 * Created by david on 2015/9/22.
 */
public class AttachPanel extends JPanel {

    /**
     * 默认缩略图尺寸
     */
    public static final int DEFAULT_THUMB_SIZE = 120;

    List<JLabel> labelList;

    List<String> pictureUrls;


    private String title;

    private MouseAdapter mouseAdapter;

    private OnAttachFileDeleteListener listener;
    //缩略图最大尺寸
    private int maxSize = DEFAULT_THUMB_SIZE;


    public AttachPanel() {


        mouseAdapter = new PanelItemMouseAdapter();
        labelList = new ArrayList<>();
        pictureUrls = new ArrayList<>();
    }


    public List<String> getAttachFiles() {
        return pictureUrls;
    }


    public void addUrl(String fileName) {


        final JLabel jLabel = new JLabel();
        showPicture(jLabel, fileName);


    }


    public void setAttachFiles(String[] urls) {


        labelList.clear();
        pictureUrls.clear();

        this.removeAll();

        for (String s : urls) {
            final JLabel jLabel = new JLabel();
//            jLabel.setMinimumSize(new Dimension(maxSize,maxSize));
            showPicture(jLabel, s);
        }


    }


    /**
     * 显示图片
     *
     * @param jLable
     * @param fileName
     */
    private void showPicture(final JLabel jLable, final String fileName) {


        jLable.addMouseListener(mouseAdapter);
        labelList.add(jLable);
        pictureUrls.add(fileName);
        String url = getUrl(fileName);
        jLable.setMinimumSize(new Dimension(80,80));
        jLable.setText(fileName);
//        final ImageIcon icon1 = new ImageIcon(AttachPanel.class.getClassLoader().getResource("icons/logo.jpg"));
//        jLable.setIcon(icon1);


        ImageLoader.getInstance().displayImage(new Iconable() {
            @Override
            public void setIcon(ImageIcon icon, String url) {
                jLable.setIcon(icon);
                jLable.setText("");
//                final ImageIcon icon1 = new ImageIcon(AttachPanel.class.getClassLoader().getResource("icons/logo.jpg"));
//                 jLable.setIcon(icon1);


            }

            @Override
            public void onError(String message) {
                jLable.setText(message);
            }
        }, url, maxSize, maxSize);



        add(jLable);
        revalidate();
        repaint();




    }

    public void setListener(OnAttachFileDeleteListener listener) {
        this.listener = listener;
    }


    private class PanelItemMouseAdapter extends MouseAdapter {


        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            showMenu(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            showMenu(e);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

            JLabel label = (JLabel) e.getSource();
            final int index = labelList.indexOf(label);


            if (e.getClickCount() == 2) {






                //拼接成url连续绝对路径串
                String destUrl="";
                for(String s:pictureUrls)
                {
                    destUrl+=getUrl(s);
                    destUrl+=StringUtils.STRING_SPLIT_SEMICOLON;
                }




                String dialogTitle=StringUtils.isEmpty(title)?"附件":title;
                ImageViewDialog.showDialog(SwingUtilities.getWindowAncestor(AttachPanel.this), destUrl,dialogTitle,index);
                // ImageViewDialog.showDialog(SwingUtilities.getWindowAncestor(this),);
            }


        }



    }

    /**
     * 设置标题
     */
    public void setTitle( String title)
    {
        this.title=title;
    }

    /**
     * 弹出删除菜单
     *
     * @param e
     */
    private void showMenu(MouseEvent e) {
        final JLabel label = (JLabel) e.getSource();
        final int index = labelList.indexOf(label);
        //弹出删除菜单。
        if (e.isPopupTrigger()) {


            JPopupMenu menu = new JPopupMenu();
            JMenuItem delete = new JMenuItem("删除");
            menu.add(delete);
            delete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    String url=pictureUrls.get(index);
                    if (listener != null) {

                            listener.onDelete(index,url);
                    }

                    labelList.remove(index);
                    pictureUrls.remove(index);
                    AttachPanel.this.remove(label);

                    revalidate();
                    getParent().revalidate();
                    repaint();

                }
            });

            menu.show(e.getComponent(), e.getX(), e.getY());


        }
    }


    private String getUrl(String relativeUrl) {
        return  HttpUrl.loadPicture(relativeUrl);
    }

    /**
     * 删除回调接口
     */
    public interface OnAttachFileDeleteListener {
        public void onDelete(int index, String url);
    }


    /**
     * 设置缩略图展示的最大尺寸 px
     *
     * @param maxSize
     */
    public void setMaxSize(int maxSize) {

        this.maxSize = maxSize;

    }




}
