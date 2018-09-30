package com.giants.hd.desktop.filters;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Excel文件过滤类  文件选择中使用
 */
public class ExcelFileFilter
  extends FileFilter {

    String ext;

    //构造方法的参数是我们需要过滤的文件类型。比如excel文件就是 xls,exe文件是exe.

    public ExcelFileFilter(String ext) {

        this.ext = ext;
    }

    public boolean accept(File file) {

        //首先判断该目录下的某个文件是否是目录，如果是目录则返回true，即可以显示在目录下。

        if (file.isDirectory()) {
            return true;
        }


        String fileName = file.getName();
        int index = fileName.lastIndexOf('.');

        if (index > 0 && index < fileName.length() - 1) {
            String extension = fileName.substring(index + 1).toLowerCase();
            if (extension.equals(ext))
                return true;
        }
        return false;
    }


      /*
      *
      * 这个方法也是重写FileFilter的方法，作用是在过滤名那里显示出相关的信息。这个与我们过滤的文件类型想匹配，通过这些信息，可以让用户更清晰的明白需要过滤什么类型的文件。
      */

    public String getDescription() {

        if (ext.equals("xls")) {
            return "Microsoft Excel文件(*.xls)";
        }

        return "";
    }
}