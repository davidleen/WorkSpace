package com.giants.hd.desktop.mvp;

import java.awt.*;

/**
 * Interface representing a View that will use to load data.
 */
public interface IViewer {


        /**
         * Show a viewer with a progress bar indicating a loading process.
         */
        void showLoadingDialog();

        void showLoadingDialog(String hint);



        /**
         * Show a retry viewer in case of an error when retrieving data.
         */
        void showRetry();

        /**
         * Hide a retry viewer shown if there was an error when retrieving data.
         */
        void hideRetry();

        /**
         * Show an error message
         *
         * @param message A string representing an error.
         */
        void showError(String message);

        /**
         * Get a {@link java.awt.Window}.
         */
        Window getWindow();

        Container getRoot();

        void hideLoadingDialog();
        void showMesssage(String message);

        boolean showConfirmMessage(String message);

    /**
     * 显示等待框处理 ，只有在一些需要长时间加载的界面上使用
     * @deprecated
     *
     */
    void showLoadingDialogCarfully();
}
