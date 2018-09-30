package com.giants.hd.desktop.interf;

import javax.swing.*;

/**  远程读取字节流数据
 * Created by david on 2015/9/22.
 */
public interface ImageByteDataReader {


      void setData(byte[] icon);
      void onError(String message);
}
