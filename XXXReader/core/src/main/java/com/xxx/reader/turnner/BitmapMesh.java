package com.xxx.reader.turnner;

import android.graphics.PointF;

import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.MathUtils;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.turnner.sim.PageTurnHelper;

import java.util.Arrays;

public class BitmapMesh {

    public static int WIDTH = 40;
    public static final int HEIGHT = 40;
    //共21*21个坐标点
    private final int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    //坐标点共21*21*2 数组个数 ， 因为一个坐标点有x和y所以乘以2
    public final float[] verts = new float[COUNT * 2];
    //定义原本不变的坐标数组
    public final float[] orig = new float[COUNT * 2];

    private int minSubWidthStep = 0;
    private int minSubHeightStep = 0;

    private int mViewHeight;
    private int mViewWidth;

    public void init(DrawParam drawParam) {
        //获取图片的宽度，高度
        int bitmapwidth = drawParam.width;
        int bitmapheight = drawParam.height;
        mViewWidth=bitmapwidth;
        mViewHeight=bitmapheight;
        minSubHeightStep = (bitmapheight / HEIGHT);
        minSubWidthStep = (bitmapwidth / WIDTH);
        int index = 0;
        //将图片分割，然后保存21*21个坐标点
        for (int y = 0; y <= HEIGHT; y++) {
            float fy = minSubHeightStep * y;
            for (int x = 0; x <= WIDTH; x++) {
                float fx = minSubWidthStep * x;
                //用数组保存坐标点fx , fy
                orig[index * 2 + 0] = verts[index * 2 + 0] = fx;
                orig[index * 2 + 1] = verts[index * 2 + 1] = fy;
                index++;
            }
        }
    }


    /**
     * 弯曲率
     */
    public float curvature = 4f;
    private float k = 0f;
    private int sizeShort = 300;
    private int sizeLong = 300;



    // 短边偏移倍增
    float mulOffsetShort = 1.0F;

    public void calculate(PageTurnHelper.Bezier horizontal,  PageTurnHelper.Bezier vertical) {

          sizeLong= (int) MathUtils.getLength(vertical.end,vertical.control);
          sizeShort= (int) MathUtils.getLength(horizontal.end,horizontal.control);
        // 长边偏移
        float offsetLong = curvature / 2F * sizeLong;

        // 长边偏移倍增
        float mulOffsetLong = 1.0F;

        // 短边偏移
        float offsetShort = curvature / 2F * sizeShort;
        // 计算底部扭曲的起始细分下标
        int mSubWidthStart = Math.round((horizontal.end.x / minSubWidthStep)) - 1;
        int mSubWidthEnd = Math.round(((horizontal.end.x + curvature * sizeShort) / minSubWidthStep)) + 1;

// 计算右侧扭曲的起始细分下标
        int mSubHeightStart = (int) (vertical.end.y / minSubHeightStep) - 1;
        int mSubHeightEnd = Math.round(((vertical.end.y + curvature * sizeLong) / minSubHeightStep)) + 1;


        for (int i = 0; i < orig.length; i++) {

            verts[i]=orig[i];
        }


        int index=0;
        for (int y = 0; y <= HEIGHT; y++) {
            float fy = mViewHeight * y / HEIGHT;
            for (int x = 0; x <= WIDTH; x++) {
                float fx = mViewWidth * x / WIDTH;

                /*
                 * 右侧扭曲
                 */
                if (x == WIDTH) {
                    if (y >= mSubHeightStart && y <= mSubHeightEnd) {
//                        fx = mViewWidth * x / WIDTH + offsetLong * mulOffsetLong;
                        fx/=2;
                        mulOffsetLong = mulOffsetLong / 1.5F;


                    }
                }

                /*
                 * 底部扭曲
                 */
                if (y == HEIGHT) {
                    if (x >= mSubWidthStart && x <= mSubWidthEnd) {
//                        fy = mViewHeight * y / HEIGHT + offsetShort * mulOffsetShort;
                        fy/=2;
                        mulOffsetShort = mulOffsetShort / 1.5F;
                    }
                }


                if(x == WIDTH||y == HEIGHT)
                {
                    Log.e("oldFx:"+(mViewWidth * x / WIDTH)+",oldFY:"+(mViewHeight * y / HEIGHT)+"fx:"+fx+",fy:"+fy);
                }
                verts[index * 2 + 0] = fx;
                verts[index * 2 + 1] = fy;

                index += 1;
            }
        }


    }

    private void calculate()
    {
        for(int i=0;i<HEIGHT+1;i++){
            for(int j=0;j<WIDTH+1;j++){
                //x坐标不变
                verts[(i*(WIDTH+1)+j)*2+0]+=0;
                //增加k值是为了让相位产生移动，从而可以飘动起来
                float offset=(float)Math.sin((float)j/WIDTH*2*Math.PI+k);
                //y坐标改变，呈现正弦曲线
                verts[(i*(WIDTH+1)+j)*2+1]=orig[(i*(WIDTH+1)+j)*2+1]+offset*50;
            }
        }


        k+=0.4f;
    }

    public void calculateHorizontal(PointF  dragTop  , PointF foldTop) {
        for (int i = 0; i < orig.length; i++) {

            verts[i]=orig[i];
        }

        int x= (int) (mViewWidth-(foldTop.x-dragTop.x));

        int endX=Math.min(WIDTH,Math.round(x/minSubWidthStep)+1);
        int length=WIDTH/10;
        int startX=Math.max(endX-length,0);
        for (int j = 0; j <= HEIGHT; j++) {
        for (int i = endX; i >= startX; i--) {




                int endIndex=((j*(WIDTH+1))+endX)*2;

                int index = ((j * (WIDTH+1)) + i) * 2  ;

                float rad= ((float)endX-i)/length;
                verts[index] = (float) (orig[index]);
                verts[index+1] = (float) (orig[index+1]+minSubWidthStep*Math.sin(rad) );
//                verts[index] = (float) (orig[index]  +minSubWidthStep);
//                verts[index+1] = (float) (orig[index+1]  +minSubHeightStep);

                Log.e("orig[endIndex]:"+ orig[endIndex]+",orig[endIndex+1:"+orig[endIndex+1 ]);


                Log.e("i:"+i+",j:"+j+"verts[index]:"+verts[index]+","+verts[index+1]);


            }

        }



    }
}
