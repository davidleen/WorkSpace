package com.giants3.hd.server.filter;

import com.giants3.hd.noEntity.ConstantData;
import com.giants3.crypt.CryptUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 对所有请求带来的输入流进行解密处理
 * Created by david on 2016/2/12.
 */
@Deprecated
public class DecryptServletInputStream extends ServletInputStream {


    public static ByteArrayOutputStream bos = new ByteArrayOutputStream();

    public ByteArrayInputStream byteArrayInputStream;

    public DecryptServletInputStream(ServletInputStream inputStream) {

        try {


            synchronized (bos) {
                bos.reset();
                IOUtils.copy(inputStream, bos);

                inputStream.close();

                byte[] data = bos.toByteArray();
                bos.reset();




                data = CryptUtils.decryptDES(data, ConstantData.DES_KEY);
                byteArrayInputStream = new ByteArrayInputStream(data);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int read() throws IOException {
        if (byteArrayInputStream != null)
            return byteArrayInputStream.read();
        return -1;
    }

    @Override
    public void close() throws IOException {
        if (byteArrayInputStream != null)
            byteArrayInputStream.close();
        super.close();
    }
}
