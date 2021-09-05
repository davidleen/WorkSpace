package com.xxx.reader.text.page;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.xxx.reader.turnner.sim.SettingContent;

import java.util.List;

public class PageDrawer {

    private PageData pageData;
     Paint.FontMetrics fontMetrics=new Paint.FontMetrics();
    public  PageDrawer(PageData pageData)
    {

        this.pageData = pageData;
    }


    public void onDraw(Canvas canvas,Paint paint)
    {
        paint.setStrokeWidth(3);
        paint.setTextSize(SettingContent.getInstance().getTextSize());
        float baseLine=paint.getFontMetrics().descent;
        canvas.save();
        canvas.translate(pageData.x,pageData.y);
        for (ParaData paraData: pageData.paraDataList)
        {
            canvas.save();
            canvas.translate(paraData.x,paraData.y);
        //  Log.e("paraData Y:"+paraData.y);
            List<LineData> lineDataList = paraData.lineDataList;
            for (int i = paraData.startLineIndex; i <=paraData.endLineIndex; i++) {
                LineData lineData = lineDataList.get(i);
                canvas.save();
                canvas.translate(lineData.x, lineData.y);
              //  Log.e("line Y:"+lineData.y);
                for (WordData wordData : lineData.wordDataList) {
//                    canvas.save();
//                    float orginSize = paint.getTextSize();
                    paint.setTextSize(wordData.textSize);
//                     paint.setStyle(wordData.textStyle);
                    paint.getFontMetrics(fontMetrics);

//                     int textColor=paint.getColor();
//                     paint.setColor(Color.RED);
//                     canvas.drawLine(wordData.x,fontMetrics.top,wordData.width,fontMetrics.top,paint);
//
//                     paint.setColor(Color.BLUE);
//                     canvas.drawLine(wordData.x,fontMetrics.ascent,wordData.width,fontMetrics.ascent,paint);
//
//                     paint.setColor(Color.YELLOW);
//
//                     canvas.drawLine(wordData.x,fontMetrics.leading,wordData.width,fontMetrics.leading,paint);
//                     paint.setColor(Color.GREEN);
//                     canvas.drawLine(wordData.x,fontMetrics.descent,wordData.width,fontMetrics.descent,paint);
//                     paint.setColor(Color.DKGRAY);
//                     canvas.drawLine(wordData.x,fontMetrics.bottom,wordData.width,fontMetrics.bottom,paint);
//                     paint.setColor(textColor);
                    canvas.drawText( wordData.word, wordData.x, wordData.y + wordData.height-baseLine , paint);
//                    paint.setTextSize(orginSize);
//                    canvas.restore();
                }
//                 Paint.Style originStyle=paint.getStyle();
//                 paint.setStyle(Paint.Style.STROKE);
//                 canvas.drawRect(0,0, lineData.width,lineData.height ,paint);
//                 paint.setStyle(originStyle);

                canvas.restore();

            }

//            int textColor=paint.getColor();
//             int colorRando=new Random().nextInt();
//            Paint.Style originStyle=paint.getStyle();
//            paint.setStyle(Paint.Style.STROKE);
//             paint.setColor(Color.argb(3,colorRando,colorRando,colorRando));
//             canvas.drawRect(0,0,paraData.width,paraData.height,paint);
//            paint.setColor(textColor);
//            paint.setStyle(originStyle);


            canvas.restore();
        }
        canvas.restore();

    }
}
