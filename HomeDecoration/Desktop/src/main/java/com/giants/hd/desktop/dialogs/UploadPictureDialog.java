package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.filters.ExcelFileFilter;
import com.giants.hd.desktop.filters.PictureFileFilter;
import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.utils.FileChooserHelper;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.exception.HdException;
import com.giants3.hd.utils.file.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public abstract class UploadPictureDialog extends BaseDialog {
    protected JPanel contentPane;
    protected JButton uploadPicture;
    protected JButton syncPicture;
    protected JCheckBox pictureOverride;
    protected JButton asyncRelate;
    protected JLabel asyncRelateMessage;

    public UploadPictureDialog(Window window) {
        super(window,"图片管理");
        setContentPane(contentPane);
        asyncRelate.setVisible(false);
        asyncRelateMessage.setVisible(false);



        uploadPicture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                final File file = getSelectedFile();


                if (file == null) {

                } else {


                    final boolean doesOverride = pictureOverride.isSelected();


                    new HdSwingWorker<Void, String>(UploadPictureDialog.this) {

                        @Override
                        protected RemoteData<Void> doInBackground() throws Exception {

                            uploadFile(file, 1, doesOverride, this);

                            return new RemoteData<Void>();


                        }

                        @Override
                        protected void process(java.util.List<String> chunks) {

                            dialog.setMessage(chunks.get(0));
                        }

                        @Override
                        public void onResult(RemoteData<Void> data) {

                            JOptionPane.showMessageDialog(UploadPictureDialog.this, "图片上传完毕");

                            //清除本地缓存
                           // ImageLoader.getInstance().clearCache();

                        }
                    }.go();

                }

            }
        });


        syncPicture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new HdSwingWorker<Void,Object>(UploadPictureDialog.this)
                {

                    @Override
                    protected RemoteData<Void> doInBackground() throws Exception {


                      return  syncPicture();

                    }

                    @Override
                    public void onResult(RemoteData<Void> data) {

                        JOptionPane.showMessageDialog(UploadPictureDialog.this,data.message);

                    }
                }.go();
            }
        });
        asyncRelate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new HdSwingWorker<Void,Object>(UploadPictureDialog.this)
                {

                    @Override
                    protected RemoteData<Void> doInBackground() throws Exception {


                        return  syncRelatePicture();

                    }

                    @Override
                    public void onResult(RemoteData<Void> data) {

                        JOptionPane.showMessageDialog(UploadPictureDialog.this,data.message);

                    }
                }.go();


            }
        });


    }


    protected RemoteData<Void> syncRelatePicture() throws HdException {

            return null;


    }


    private File getSelectedFile()
    {

        final File file = FileChooserHelper.chooseFile(JFileChooser.FILES_AND_DIRECTORIES, false,new PictureFileFilter());


        return file;

    }


    /**
     *递归上传文件方法
     * @param file
     * @throws HdException
     */
    private void uploadFile(File file,int type,boolean doesOverride,HdSwingWorker<Void,String> swingWorker) throws HdException {
        if(file.isDirectory())
        {

            File[] childFiles=file.listFiles();

            //每张图片独立上传
            for (int i = 0; i < childFiles.length; i++) {






                if(ImageUtils.isPictureFile(childFiles[i].getName()))
                    uploadFile(childFiles[i],type,doesOverride,swingWorker);

            }

        }else
        {
            RemoteData result=uploadPicture(file,doesOverride);


            swingWorker.publishMessage(result.message);


        }


    }



    protected abstract RemoteData<Void> uploadPicture(File file,boolean doesOverride) throws HdException;


    protected abstract  RemoteData<Void> syncPicture() throws HdException;

}
