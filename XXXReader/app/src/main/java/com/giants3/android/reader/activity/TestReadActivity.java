package com.giants3.android.reader.activity;

import android.app.Activity;
import android.os.Bundle;

import com.giants3.android.frame.util.StorageUtils;
import com.giants3.android.frame.util.Utils;
import com.giants3.android.reader.view.TestReadView;
import com.giants3.io.FileUtils;
import com.giants3.reader.book.EpubBook;
import com.giants3.reader.book.EpubChapter;
import com.xxx.reader.text.page.PageData;
import com.xxx.reader.text.page.PageDrawer;
import com.xxx.reader.text.page.TypeParam;
import com.xxx.reader.text.page.Typing;
import com.xxx.reader.turnner.sim.SettingContent;

import java.io.File;
import java.io.IOException;

public class TestReadActivity  extends Activity {

    @Override
    protected void onCreate(  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        TestReadView testReadView=new TestReadView(this);
        setContentView(testReadView);


        String filePath=getIntent().getStringExtra("filePath");

        TypeParam typeParam=new TypeParam();
        int[] screenWH = Utils.getScreenWH();
        typeParam.width=screenWH[0];
        typeParam.height= screenWH[1];
        typeParam.textSize= (int) 60;
        typeParam.lineSpace= 10;
        typeParam.wordSpace= (int) SettingContent.getInstance().getWordSpace();
        typeParam.paragraphSpace= 30;
        typeParam.includeFontPadding= false;
        typeParam.padding= new float[]{10,10,10,10};



        EpubBookFactory  bookFactory = new EpubBookFactory();
        EpubBook epubBook = bookFactory.create(filePath);

        EpubChapter chapter = epubBook.getChapter(0);
        String destFilePath = StorageUtils.getFilePath( chapter.getFilePath());
        if (!new File(filePath).exists()) {
            try {

                FileUtils.writeToFile(destFilePath, ((EpubChapter) chapter).getData());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        PageData pageData=null;

           pageData=   Typing.typePage( null, chapter,300,typeParam);
//           pageData=   Typing.typePage( pageData,destFilePath,0,typeParam);

              pageData = Typing.typePrePage(pageData, chapter, typeParam);




        PageDrawer pageDrawer=new PageDrawer(pageData);

        testReadView.setDrawer(pageDrawer);

    }
}
