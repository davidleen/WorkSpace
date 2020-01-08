package com.giants.hd.desktop.dialogs;

import com.giants.hd.desktop.local.HdSwingWorker;
import com.giants.hd.desktop.local.PropertyWorker;
import com.giants3.hd.domain.api.ApiManager;
import com.giants3.hd.domain.api.HttpUrl;
import com.giants3.hd.utils.FileUtils;
import com.giants3.hd.noEntity.RemoteData;
import com.giants3.hd.entity.AppVersion;
import com.giants3.hd.exception.HdException;
import com.google.inject.Inject;
import de.greenrobot.common.io.IoUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class UpgradeDialog extends BaseDialog {
    private JPanel contentPane;
    private JTextField tf_old_version;
    private JTextField tf_new_version;
    private JButton upgrade;
    private JTextArea ta_spec;




    @Inject
    ApiManager apiManager;

    AppVersion newAppVersion;



    public UpgradeDialog(Window window) {
        super(window, "检查更新");
        setContentPane(contentPane);

        checkUpdate();

        upgrade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                upgrade();
            }
        });

    }




    private  void checkUpdate()
    {


        new HdSwingWorker<AppVersion, Void>(this) {
            @Override
            protected RemoteData<AppVersion> doInBackground() throws Exception {




                return apiManager.readAppVersion();
            }

            @Override
            public void onResult(RemoteData<AppVersion> data) {

                if(data.isSuccess())
                {
                    AppVersion version=data.datas.get(0);

                   AppVersion currentVersion = PropertyWorker.getVersion();
                    if(currentVersion.versionCode<version.versionCode)
                    {
                        initValue(currentVersion,version);
                    }else
                    {
                        JOptionPane.showMessageDialog(UpgradeDialog.this, "目前版本["+currentVersion.versionName+"]已经是最新版本");
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {

                                UpgradeDialog.this.dispose();
                            }
                        });


                    }


                }else
                {

                    JOptionPane.showMessageDialog(UpgradeDialog.this,"检查版本失败，原因："+data.message);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {

                            UpgradeDialog.this.dispose();
                        }
                    });
                }

            }
        }.go();


    }


    private  void upgrade() {


        new HdSwingWorker<Void, Void>(this) {
            @Override
            public void onResult(RemoteData<Void> data) {

                //启动应用程序
                try {
                JOptionPane.showMessageDialog(UpgradeDialog.this, "重新启动应用程序");

                 Thread.sleep(500);
                  Process p = Runtime.getRuntime().exec("java -jar " + newAppVersion.appName);

                } catch (IOException ex5) {
                } catch (InterruptedException ex) {
                }

                //退出更新程序
                System.exit(0);
            }

            @Override
            protected RemoteData<Void> doInBackground() throws Exception {



                InputStream is = null;

                File oldFile = new File(newAppVersion.appName);
                //缓存网络上下载的文件
                File newFile = new File("temp_" + newAppVersion.appName);

                BufferedInputStream bis = null;
                OutputStream fos = null;

                try {
                    //打开URL通道
                    URL url = new URL(HttpUrl.loadApp());
                        is =   url.openStream();


                    byte[] buffer = new byte[1024*16];

                    int size = 0;


                    bis = new BufferedInputStream(is);
                    fos = new FileOutputStream(newFile);

                    long totalSize=0;
                    //保存文件
                    try {

                        while ((size = bis.read(buffer)) != -1) {
                            //读取并刷新临时保存文件
                            fos.write(buffer, 0, size);
                            fos.flush();
                            totalSize+=size;

                            //System.out.println("download :"+totalSize+",allSize:"+newAppVersion.fileSize);


                        }
                    } catch (Exception ex4) {
                        System.out.println(ex4.getMessage());
                    }






                } catch (MalformedURLException ex2) {
                } catch (IOException ex) {

                } finally {
                    IoUtils.safeClose(fos);
                    IoUtils.safeClose(bis);
                    IoUtils.safeClose(is);

                }


                //检查文件大小
                if (newFile.length() == newAppVersion.fileSize) {

                    //把下载的临时文件替换原有文件
                    FileUtils.copyFile(oldFile, newFile);
                    newFile.delete();

                } else {
                    newFile.delete();
                    throw HdException.create("下载文件大小不一致 ,更新失败");
                }

                return new RemoteData<>();
            }









        }.go();
    }


    private void initValue(AppVersion currentVersion,AppVersion version) {


        newAppVersion=version;
        ta_spec.setText( version.memo);
        tf_new_version.setText(version.versionName);
        tf_old_version.setText(currentVersion.versionName);
        upgrade.setEnabled(true);


    }
}
