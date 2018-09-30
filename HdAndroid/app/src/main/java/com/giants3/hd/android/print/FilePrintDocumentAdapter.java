package com.giants3.hd.android.print;

import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;

import com.giants3.ByteArrayPool;
import com.giants3.android.frame.util.FileUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件形式打印
 */
public class FilePrintDocumentAdapter extends PrintDocumentAdapter {
    private String mFilePath;
 
    public FilePrintDocumentAdapter(String file) {
        this.mFilePath = file;
    }
 
    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes,
                         CancellationSignal cancellationSignal,
                         LayoutResultCallback callback, Bundle extras) {
        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }
        PrintDocumentInfo info = new PrintDocumentInfo.Builder("name")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .build();
        callback.onLayoutFinished(info, true);
    }
 
    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination,
                        CancellationSignal cancellationSignal,
                        WriteResultCallback callback) {
        InputStream input = null;
        OutputStream output = null;
        PdfDocument document = new PdfDocument();
        byte[] buf = ByteArrayPool.getInstance().getBuf(1024);
        try {
 
            input = new FileInputStream(mFilePath);
            output = new FileOutputStream(destination.getFileDescriptor());


            int bytesRead;
 
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
 
            callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

            FileUtils.safeClose(input);
            FileUtils.safeClose(output);
            ByteArrayPool.getInstance().returnBuf(buf);


        }
    }
}
