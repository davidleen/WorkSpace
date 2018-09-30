package com.giants.hd.desktop.utils;

import com.giants.hd.desktop.local.LocalFileHelper;
import com.giants.hd.desktop.local.PropertyWorker;
import com.giants3.hd.utils.StringUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by davidleen29 on 2018/3/25.
 */
public class FileChooserHelper {

    private  static  String lastSelectDirectory="";

    private static final String KEY="LAST_SELECTED_DIRECTORY";
    static {
        lastSelectDirectory= LocalFileHelper.read(KEY);
    }

    public static File   chooseFile(int selectionMode,boolean acceptFileFilter, FileFilter fileFilter )
    {


        File file=null;
        String directory=getLastSelectDirectory();
        JFileChooser fileChooser = getjFileChooser(directory, selectionMode, acceptFileFilter, fileFilter,false);


        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            file= fileChooser.getSelectedFile();
        }

        saveLastSelectDiretory(file);


        return file;
    }


    public static File[]   chooseFiles(  int selectionMode, boolean acceptFileFilter , FileFilter fileFilter)
    {

        String directory=getLastSelectDirectory();
        JFileChooser fileChooser = getjFileChooser(directory, selectionMode, acceptFileFilter, fileFilter,true);
        int result = fileChooser.showOpenDialog(null);
          File[] selectedFiles=null;
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFiles = fileChooser.getSelectedFiles();

        }
        saveLastSelectDiretory(selectedFiles);

        return selectedFiles;
    }

    private static String getLastSelectDirectory() {


        Config.log(lastSelectDirectory);

        return lastSelectDirectory;
    }

    private  static  void saveLastSelectDiretory(File... files)
    {

        if(files!=null&&files.length>0&&files[0]!=null)
        {

            final File file = files[0];
            lastSelectDirectory= file.isDirectory()?file.getAbsolutePath():file.getParent();

            LocalFileHelper.write(KEY,lastSelectDirectory);

        }


    }

    private static JFileChooser getjFileChooser(String directory, int selectionMode, boolean acceptFileFilter, FileFilter fileFilter,boolean multiSelectEnable) {
        if(StringUtils.isEmpty(directory)||!new File(directory).exists())
        {
            directory=".";
        }
        JFileChooser fileChooser = new JFileChooser(directory);
        //下面这句是去掉显示所有文件这个过滤器。
        fileChooser.setAcceptAllFileFilterUsed(acceptFileFilter);
        fileChooser.setFileSelectionMode(selectionMode);
        fileChooser.setMultiSelectionEnabled(multiSelectEnable);
        if(fileFilter!=null)
          fileChooser.addChoosableFileFilter(fileFilter);
        return fileChooser;
    }
}
