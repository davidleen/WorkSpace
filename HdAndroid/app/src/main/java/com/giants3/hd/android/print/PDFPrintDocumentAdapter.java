package com.giants3.hd.android.print;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;

import com.giants3.android.frame.util.BitmapHelper;
import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.StorageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PDFPrintDocumentAdapter extends PrintDocumentAdapter {

    private Context context;
    private int pageHeight;
    private int pageWidth;
    private PdfDocument mPdfDocument;
    private int totalpages = 1;
    private String pdfPath;
    int scaleSize;

    List<Bitmap> bitmaps;

    public PDFPrintDocumentAdapter(Context context, String pdfPath) {
        this.context = context;
        this.pdfPath = pdfPath;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal,
                         LayoutResultCallback callback,
                         Bundle metadata) {

        mPdfDocument = new PrintedPdfDocument(context, newAttributes); //创建可打印PDF文档对象

        int dpi=context.getResources().getDisplayMetrics().densityDpi;
       // int dpi=300;
          scaleSize=4;


        pageWidth = newAttributes.getMediaSize().getWidthMils() * 72 / 1000;
        pageHeight = newAttributes.getMediaSize().getHeightMils() * 72 / 1000;  //设置尺寸

        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }

        ParcelFileDescriptor mFileDescriptor = null;
        PdfRenderer pdfRender = null;
        PdfRenderer.Page page = null;

        Bitmap bmp=null ;
        try {
            mFileDescriptor = ParcelFileDescriptor.open(new File(pdfPath), ParcelFileDescriptor.MODE_READ_ONLY);
            if (mFileDescriptor != null)
                pdfRender = new PdfRenderer(mFileDescriptor);





            if (pdfRender.getPageCount() > 0) {


                totalpages = pdfRender.getPageCount();
                bitmaps=new ArrayList<>();
                for (int i = 0; i < totalpages; i++) {
                    if(null != page)
                        page.close();
                    page = pdfRender.openPage(i);
                    bmp= Bitmap.createBitmap(pageWidth*scaleSize,pageHeight*scaleSize, Bitmap.Config.ARGB_8888);
                    bitmaps.add(bmp);
                  //  canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
                    page.render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT);
                   // BitmapHelper.bitmapSaveToFile(bmp,   getPrintBitmapPath(i),true);

                }
            }
            if(null != page)
                page.close();
            if(null != mFileDescriptor)
                mFileDescriptor.close();
            if (null != pdfRender)
                pdfRender.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
//            BitmapHelper.recycleBitmap(bmp);

        }

        if (totalpages > 0) {
            PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                    .Builder(pdfPath.hashCode()+".pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(totalpages);  //构建文档配置信息

            PrintDocumentInfo info = builder.build();
            callback.onLayoutFinished(info, true);
        } else {
            callback.onLayoutFailed("Page count is zero.");
        }
    }

    @Override
    public void onWrite(final PageRange[] pageRanges, final ParcelFileDescriptor destination, final CancellationSignal cancellationSignal,
                        final WriteResultCallback callback) {



        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);


//        Bitmap bitmap =Bitmap.createBitmap(pageWidth*scaleSize,pageHeight*scaleSize, Bitmap.Config.ARGB_8888);


        for (int i = 0; i < totalpages; i++) {
            if (pageInRange(pageRanges, i)) //保证页码正确
            {
                PageInfo newPage = new PageInfo.Builder(pageWidth,
                        pageHeight, i).create();
                PdfDocument.Page page =
                        mPdfDocument.startPage(newPage);  //创建新页面

                if (cancellationSignal.isCanceled()) {  //取消信号
                    callback.onWriteCancelled();
                    mPdfDocument.close();
                    mPdfDocument = null;
                    return;
                }
//              Bitmap    result=  BitmapHelper.fromFile( getPrintBitmapPath(i),bitmap);
                Bitmap result=bitmaps.get(i);
                drawPage(page, i,result,paint);  //将内容绘制到页面Canvas上
                mPdfDocument.finishPage(page);
            }
        }

        BitmapHelper.recycleBitmap(bitmaps);
        bitmaps=null;
        try {
            mPdfDocument.writeTo(new FileOutputStream(
                    destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            mPdfDocument.close();
            mPdfDocument = null;
        }

        callback.onWriteFinished(pageRanges);
    }

    private boolean pageInRange(PageRange[] pageRanges, int page) {
        for (int i = 0; i < pageRanges.length; i++) {
            if ((page >= pageRanges[i].getStart()) &&
                    (page <= pageRanges[i].getEnd()))
                return true;
        }
        return false;
    }

    //页面绘制（渲染）
    private void drawPage(PdfDocument.Page page,int pageIndex,Bitmap bitmap,Paint paint) {
        Canvas canvas = page.getCanvas();

            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            // 计算缩放比例
            float scale = (float)pageWidth/(float)bitmapWidth;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            Log.e("scale:"+scale);
            canvas.drawColor(
                    Color.TRANSPARENT,
                    PorterDuff.Mode.CLEAR);
            canvas.drawBitmap(bitmap,matrix,paint);





       // canvas.drawText("what is the fuck....",300,300,paint);
    }


    private String getPrintBitmapPath(int pageIndex)
    {
        return  StorageUtils.getFilePath("print/"+pageIndex+".jpg");
    }
}

