package com.giants3.yourreader.text;

import com.giants3.yourreader.text.composite.ParaCompositor;
import com.giants3.yourreader.text.composite.TextMeasure;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);


        int indentCount = 2;
         int textSize = 10;
       int orientation=0;
        String data = "aaaaa我是谁啊啊啊啊aaaaaa我都服";
        int width = 60;
        int wordGap=3;

        testComposite(indentCount, textSize,wordGap, orientation, data, width);



          indentCount = 2;
           textSize = 24;
            orientation=0;
          data ="北京时间5月10日，勇士球星凯文-杜兰特因为伤病原因将无法出战与火箭接下来的系列赛，\n虽然这看上去很糟糕，但勇士似乎并不需要对此太过担心。\n 根据美媒统计，在勇士过去27场缺少杜兰特的比赛中，他们赢下了其中的26场，胜率达到了96.3%，在上一战中，勇士便在杜兰特离场的情况下战胜了火箭。本赛季常规赛，勇士唯一战胜火箭的那场比赛便是在杜兰特缺阵的情况下完成的。 　当然了，在季后赛中缺少杜兰特肯定会对勇士有很大的影响，在关键时刻勇士缺少了一个稳定的进攻点，但在比赛的大部分时间，勇士多年培养而成的体系并不会受到太大的影响。";

          width = 720;
          wordGap=9;

        testComposite(indentCount, textSize, wordGap,orientation, data, width);
    }

    private void testComposite(int indentCount, final int textSize,int wordGap, int orientation, String data, int width ) {


        TextMeasure textMeasure = new TextMeasure() {
            @Override
            public float getTextWidth(char c, int orientation) {

                return textSize;
            }
        };
        ParaCompositor.Compositor composite = ParaCompositor.composite(data, width, wordGap, indentCount, orientation, textMeasure);

       // assertEquals(composite.lineCount, 5);
        assertEquals(composite.positions[composite.lineIndex[0]], indentCount*textSize ,0.001f);
        for (int i = 1; i < composite.lineCount; i++) {

            int lineIndex = composite.lineIndex[i];
            //第二行起 行首判定
            assertEquals(composite.positions[lineIndex], 0,0.001f);
            //到倒数第一行行尾的位置。
            if(lineIndex-1>composite.lineIndex[i-1]&&composite.positions[lineIndex -1]!=ParaCompositor.CHAR_TAB)
                assertEquals(composite.positions[lineIndex -1]+textMeasure.getTextWidth(data.charAt(lineIndex-1),orientation), width,0.001f);
        }
    }
}