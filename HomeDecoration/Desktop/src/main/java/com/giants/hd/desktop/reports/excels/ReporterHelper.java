package com.giants.hd.desktop.reports.excels;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by davidleen29 on 2018/9/26.
 */
public class ReporterHelper {

    public static void openFileHint(Window window,File file)
    {
        // 导出成功提示框
        int dialog = JOptionPane.showConfirmDialog(window, "导出成功！是否现在打开？", "提示", JOptionPane.YES_NO_OPTION);
        if (dialog == JOptionPane.YES_OPTION) {
            try {
                Runtime.getRuntime().exec("cmd /c start \"\" \"" + file + "\"");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
