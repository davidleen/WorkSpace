package com.giants.hd.desktop.utils;

import com.giants.hd.desktop.filters.ExcelFileFilter;

import javax.swing.*;
import java.io.File;

/**
 * Created by david on 2015/10/7.
 */
public class SwingFileUtils {


    public static File getSelectedDirectory()
    {



        return getSelectedFile(JFileChooser.DIRECTORIES_ONLY);
    }



    private static File getSelectedFile(int mode)
    {

        final File file = FileChooserHelper.chooseFile(mode, false, null);

        return file;

    }


    /**
     *
     *
     * @return
     */
    public  static File getSelectedFile( )
    {


        return getSelectedFile(JFileChooser.FILES_ONLY);
    }



}
