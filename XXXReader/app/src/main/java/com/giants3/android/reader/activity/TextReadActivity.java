package com.giants3.android.reader.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.giants3.android.adapter.AbstractRecyclerAdapter;
import com.giants3.android.convert.JsonFactory;
import com.giants3.android.frame.util.FragmentForResult;
import com.giants3.android.frame.util.Log;
import com.giants3.android.frame.util.StringUtil;
import com.giants3.android.reader.R;
import com.giants3.android.reader.adapter.TextSchemeAdapter;
import com.giants3.android.reader.databinding.ActivityTextReaderBinding;
import com.giants3.android.reader.scheme.TypefaceEntity;
import com.giants3.android.reader.scheme.TypefaceHelper;
import com.giants3.android.reader.window.BooksPopupWindow;
import com.giants3.android.reader.window.TypesetPopupWindow;
import com.giants3.android.service.ServiceBinderHelper;
import com.giants3.android.window.WindowHelper;
import com.giants3.reader.entity.Book;
import com.giants3.yourreader.text.BookPlayService;
import com.xxx.reader.text.page.ParaTypeset;
import com.xxx.reader.TextScheme;
import com.giants3.android.reader.vm.TextReadViewModel;
import com.xxx.reader.BackgroundManager;
import com.giants3.yourreader.text.BookService;
import com.giants3.yourreader.text.TextPageBitmap;
import com.giants3.yourreader.text.TextPageInfo;
import com.xxx.reader.ReaderView;
import com.xxx.reader.TextSchemeContent;
import com.xxx.reader.ThreadConst;
import com.xxx.reader.Utils;
import com.xxx.reader.book.IBook;
import com.xxx.reader.book.IChapter;
import com.xxx.reader.core.DrawParam;
import com.xxx.reader.core.IPageTurner;
import com.xxx.reader.core.PageInfo;
import com.xxx.reader.core.PageSwitchListener;
import com.xxx.reader.prepare.DrawLayer;
import com.xxx.reader.prepare.PageBitmapCreator;
import com.xxx.reader.prepare.PagePlayer;
import com.xxx.reader.prepare.PagePlayerBuilder;
import com.xxx.reader.prepare.PrepareListener;
import com.xxx.reader.prepare.PreparePageInfo;
import com.xxx.reader.turnner.ScrollPageTurner;
import com.xxx.reader.turnner.SimulatePageTurner;
import com.xxx.reader.turnner.sim.SettingContent;
import com.xxx.reader.turnner.slide.SlidePageTurner;

import java.util.Calendar;
import java.util.List;


public class TextReadActivity extends BaseViewModelActivity<ActivityTextReaderBinding, TextReadViewModel> implements PrepareListener {
    BookService.BookReadController bookReadController;
    PagePlayer<IChapter, TextPageInfo, DrawParam, TextPageBitmap> prepareLayer;

    private IBook iBook;
    DrawParam drawParam;
    private PreparePageInfo preparePageInfo;
    private  DrawLayer drawLayer;
    Paint paint;

    private

    IPageTurner pageTurner = null;


    private boolean isBookServiceBound;
    private ServiceBinderHelper bookServiceBinder;

    PageSwitchListener pageSwitchListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        getViewModel().getBookInfo().observe(this, new Observer<IBook>() {


            @Override
            public void onChanged(IBook iBook) {
                TextReadActivity.this.iBook = iBook;


                if (bookReadController != null) {
                    bookReadController.updateBook(iBook);
                }

            }
        });
        getViewModel().schemeChange.observe(this, new Observer<Boolean>() {


            @Override
            public void onChanged(Boolean  schemeChanged) {

                if (schemeChanged) {

                    BackgroundManager.getInstance().reset();
                    prepareLayer.updateCache();

                }




            }
        });
        getViewModel().dayModeLiveData.observe(this, new Observer<Boolean>() {


            @Override
            public void onChanged(Boolean  schemeChanged) {

                if (schemeChanged) {

                    BackgroundManager.getInstance().reset();
                    prepareLayer.updateCache();

                }

               updateStyleModeView();




            }
        });








        getViewModel().textSchemes.observe(this, new Observer<List<TextScheme>>() {


            @Override
            public void onChanged(List<TextScheme> themes) {
                RecyclerView.LayoutManager layoutManager = getViewBinding().themes.getLayoutManager();
                if(layoutManager instanceof GridLayoutManager
                )
                {
                    ((GridLayoutManager)layoutManager).setSpanCount(Math.max(1,themes.size()));
                }
              AbstractRecyclerAdapter adapter= (AbstractRecyclerAdapter) getViewBinding().themes.getAdapter();
                adapter.setDataArray(themes);

            }
        });


        getViewBinding().switchBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final BooksPopupWindow window=new BooksPopupWindow(TextReadActivity.this ,new BooksPopupWindow.BookSelectListener(){
                    @Override
                    public void onBookPick(final Book book) {


                        getViewModel().updateBook(book.id,book.name,book.url);




                    }
                });
                window.show();

                hideSettingPanel();



            }
        });
        getViewBinding().menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getViewModel().goChapterList();


                hideSettingPanel();



            }
        });

//        Intent intent = new Intent(this, BookService.class);
//        conn = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//
//                bookReadController = (BookService.BookReadController) service;
//                if (drawParam != null)
//                    bookReadController.updateDrawParam(drawParam);
//                bookReadController.setPrepareListener(TextReadActivity.this);
//                if (iBook != null)
//                    bookReadController.updateBook(iBook);
////                getViewModel().getBookInfo().observe(TextReadActivity.this, new Observer<IBook>() {
////                    @Override
////                    public void onChanged(IBook iBook) {
////
////
////
////                        if (iBook != null)
////                            bookReadController.updateBook(iBook);
////                    }
////                });
//
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//
//
//            }
//        };
        if (!isBookServiceBound) {
            bookServiceBinder.bindService(new ServiceBinderHelper.BindResultListener() {
                @Override
                public void onBindResult(boolean bindResult) {

                    isBookServiceBound = bindResult;
                }

                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {

                    bookReadController = (BookService.BookReadController) service;
                    handleOnServiceBinder();

                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    //do nothing

                }
            });
        }else
        {
            handleOnServiceBinder();
        }


    }

    private void handleOnServiceBinder()
    {
        if(bookReadController!=null) {
            if (drawParam != null)
                bookReadController.updateDrawParam(drawParam);
            bookReadController.setPrepareListener(TextReadActivity.this);
            if (iBook != null)
                bookReadController.updateBook(iBook);
        }
    }

    private void updateStyleModeView() {

        getViewBinding().styleMode.setImageResource(TextSchemeContent.getDayMode()?R.mipmap.text_bg_night:R.mipmap.text_bg_day);
    }

    @Override
    protected Class<TextReadViewModel> getViewModelClass() {
        return TextReadViewModel.class;
    }

    @Override
    protected ActivityTextReaderBinding createViewBinding() {
        return ActivityTextReaderBinding.inflate(getLayoutInflater());
    }


    private Runnable updateProgressRunnable;

    private void init() {


        getSupportActionBar().hide();
        paint=new Paint();
        paint.setSubpixelText(true);
        paint.setStrokeWidth(1);
        paint.setColor(TextSchemeContent.getTextColor());
        paint.setTextSize(SettingContent.getInstance().getTextSize());
        bookServiceBinder=new ServiceBinderHelper(this,BookService.class);
        updateTypeface();
        updateStyleModeView();
        getViewBinding().themes.setLayoutManager(new GridLayoutManager(this,1));
        final TextSchemeAdapter textSchemeAdapter=new TextSchemeAdapter(this);
        getViewBinding().themes.setAdapter(textSchemeAdapter);
        textSchemeAdapter.setOnItemClickListener(new AbstractRecyclerAdapter.OnItemClickListener<TextScheme>() {
            @Override
            public void onItemClick(TextScheme item, int position) {



                getViewModel().updateScheme(item);
                textSchemeAdapter.notifyDataSetChanged();



            }
        });


        getViewBinding().styleMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                Context context=v.getContext();

                final Animation outAni = AnimationUtils.loadAnimation(context,
                        R.anim.scale_out_from_center);
                final Animation inAni = AnimationUtils.loadAnimation(context,
                        R.anim.scale_in_from_center);


                if (TextSchemeContent.getDayMode()) {// 白天模式
                    v.startAnimation(outAni);
                    v.setVisibility(View.GONE);
                    outAni.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation arg0) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {

                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            getViewBinding().styleMode.setImageResource(R.mipmap.text_bg_day);
                            v.startAnimation(inAni);
                            v.setVisibility(View.VISIBLE);
                        }
                    });



                } else {

                    v.startAnimation(outAni);
                    v.setVisibility(View.GONE);
                    outAni.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation arg0) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {

                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            getViewBinding().styleMode.setImageResource(R.mipmap.text_bg_night);
                            v.startAnimation(inAni);
                            v.setVisibility(View.VISIBLE);
                        }
                    });



                }

                getViewModel().changeStyleMode();




            }
        });

        getViewBinding().panelSetting.setVisibility(View.GONE);
        getViewBinding().panelSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                hideSettingPanel();
            }
        });

        getViewBinding().progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {

                if (fromUser) {


                    if (updateProgressRunnable != null) {
                        getViewBinding().progress.removeCallbacks(updateProgressRunnable);
                        updateProgressRunnable = null;
                    }
                    updateProgressRunnable = new Runnable() {
                        @Override
                        public void run() {
                            bookReadController.jump(progress / 100f);
                        }
                    };
                    getViewBinding().progress.postDelayed(updateProgressRunnable, 100);


                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        getViewBinding().next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bookReadController.nextChapter();
            }
        });
        getViewBinding().previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bookReadController.previousChapter();
            }
        });
        getViewBinding().fontAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bookReadController.fontSizeAdd();

                bindSettingValue();


            }
        });
        getViewBinding().fontReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bookReadController.fontSizeReduce();
                bindSettingValue();

            }
        });
//        getViewBinding().lineSpaceReduce.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(preparePageInfo!=null)
//                {
//                    PageInfo currentPageInfo = preparePageInfo.getCurrentPageInfo();
//                    if(currentPageInfo instanceof  TextPageInfo)
//                    {
//                        ((TextPageInfo) currentPageInfo).updateElements();
//                    }
//
//
//                    prepareLayer.onPagePrepared(preparePageInfo);
//                }
//
//
//
//
//
//                bookReadController.lineSpaceReduce();
//                bindSettingValue();
//
//            }
//        });
//        getViewBinding().lineSpaceAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                bookReadController.lineSpaceAdd();
//                bindSettingValue();
//
//            }
//        });
        getViewBinding().typeface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            final     TypesetPopupWindow window=new TypesetPopupWindow(TextReadActivity.this, new TypesetPopupWindow.TypesetUpdateListener() {
                    @Override
                    public void onNewTypeface(TypefaceEntity typeface) {

                        SettingContent.getInstance().setTypeface (JsonFactory.getInstance().toJson(typeface));
                        updateTypeface(typeface);
                    }

                    @Override
                    public void updateVGap(float vGap) {
                        bookReadController.updateLineSpace(vGap);
                        bindSettingValue();

                    }

                    @Override
                    public void updateHGap(float hGap) {
                        bookReadController.updateWordSpace(hGap);
                    }

                    @Override
                    public void updateTextStyle(Paint.Style style) {


                        paint.setStyle(style);
                        prepareLayer.updateCache();

                    }

                    @Override
                    public void goMoreTypefaces() {
                        jumpTypeface(TextReadActivity.this);

                    }
                });
                window.show();

                hideSettingPanel();




            }
        });


        bindSettingValue();
        SharedPreferences sharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
        sharedPreferences.edit().putLong("lastcliptime", Calendar.getInstance().getTimeInMillis()).apply();

        final Context context = this;
        final int[] wh = Utils.getScreenDimension(this);
        PagePlayerBuilder<IChapter, TextPageInfo, DrawParam, TextPageBitmap> builder = new PagePlayerBuilder();
        final ReaderView readerView = getViewBinding().reader;

        builder.setCreator(new PageBitmapCreator<TextPageBitmap>() {
            @Override
            public TextPageBitmap create() {
                return new TextPageBitmap(context, wh[0], wh[1], readerView,paint);
            }
        });
        builder.setiDrawable(readerView);


        prepareLayer = builder.createPrepareLayer();

        drawParam = new DrawParam();
        drawParam.width = wh[0];
        drawParam.height = wh[1];
        drawParam.padding =SettingContent.getInstance().getPaddings();
        prepareLayer.updateDrawParam(drawParam);
        final ReaderView.onSizeChangeLister onSizeChangeLister = new ReaderView.onSizeChangeLister() {
            @Override
            public void onSizeChanged(int width, int height) {

                BackgroundManager.getInstance().update(width, height);
                DrawParam drawParam = new DrawParam();
                int boxMarginH = 0;
                int boxMarginV = 0;
                if (SettingContent.getInstance().isBookSideEffect()) {
                    Rect bookSidePadding = SettingContent.getInstance().getBookSidePadding();
                    boxMarginH = bookSidePadding.left + bookSidePadding.right;
                    boxMarginV = bookSidePadding.top + bookSidePadding.bottom;


                }
//

                drawParam.width = width - boxMarginH;
                drawParam.height = height - boxMarginV;

                if(pageTurner!=null)
                {
                    pageTurner.updateDrawParam(drawParam);
                }
                prepareLayer.updateDrawParam(drawParam);
                TextReadActivity.this.drawParam = drawParam;
                if (bookReadController != null)
                    bookReadController.updateDrawParam(drawParam);
            }
        };
        readerView.setOnSizeChangeListener(onSizeChangeLister);

        drawLayer = new DrawLayer(this, prepareLayer, readerView);
        drawLayer.setClickListener(new DrawLayer.MenuClickListener() {
            @Override
            public void onMenuAreaClick() {
                Log.e("~~~~~~~~~~~~~~~~~~");

                getViewBinding().panelSetting.setVisibility(View.VISIBLE);
                getViewBinding().styleMode.clearAnimation();
                getViewBinding().styleMode.startAnimation(AnimationUtils.loadAnimation(TextReadActivity.this, R.anim.show_right_anim));
                getViewModel().onSettingPanelShow();


            }
        });
         pageSwitchListener = new PageSwitchListener() {
            @Override
            public boolean canPageChanged(int direction) {
                if (direction == PageSwitchListener.TURN_PREVIOUS)
                    return bookReadController.canTurnPrevious();
                return bookReadController.canTurnNext();
            }

            @Override
            public void beforePageChanged(int direction) {


            }

            @Override
            public void afterPageChanged(int direction) {


                Log.e("afterPageChanged:" + direction);
                if (direction == PageSwitchListener.TURN_NEXT) {

                    prepareLayer.turnNext();
                    bookReadController.turnNext();
                } else {
                    prepareLayer.turnPrevious();
                    bookReadController.turnPrevious();
                }

            }

            @Override
            public void onPageTurnFail(int turnMoveDirection) {

            }
        };

//          pageTurner = new ScrollPageTurner(this, pageSwitchListener, readerView, prepareLayer);
//        pageTurner=new SimPageTurner(this,pageSwitchListener,activityTextReaderBinding.reader,prepareLayer);
       // pageTurner = new SimulatePageTurner(this, pageSwitchListener, readerView, prepareLayer);
           pageTurner =create(SettingContent.getInstance().getPageTurnMode()) ;

        updatePageTurner(pageTurner);
        readerView.setDrawLayer(drawLayer);


        boolean fullScreen =SettingContent.getInstance().isFullScreen();
        getViewBinding().fullScreen.setChecked(fullScreen);
        updateFullScreen(fullScreen);
        getViewBinding().turnScroll.setChecked(SettingContent.getInstance().getPageTurnMode()== SettingContent.PageTurnMode.MODE_LR_SCROLL);
        getViewBinding().turnSim.setChecked(SettingContent.getInstance().getPageTurnMode()== SettingContent.PageTurnMode.MODE_LR_SIM);
        getViewBinding().turnSlip.setChecked(SettingContent.getInstance().getPageTurnMode()== SettingContent.PageTurnMode.MODE_LR_SLIP);
        getViewBinding().turnUpDown.setChecked(SettingContent.getInstance().getPageTurnMode()== SettingContent.PageTurnMode.MODE_UD);

        getViewBinding().turnScroll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    SettingContent.getInstance().setPageTurnMode(SettingContent.PageTurnMode.MODE_LR_SCROLL);
                    pageTurner =create(SettingContent.getInstance().getPageTurnMode())  ;
                    updatePageTurner(pageTurner);
                }
            }
        });
        getViewBinding().turnSlip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    SettingContent.getInstance().setPageTurnMode(SettingContent.PageTurnMode.MODE_LR_SLIP);
                    pageTurner =create(SettingContent.getInstance().getPageTurnMode())  ;
                    updatePageTurner(pageTurner);
                }
            }
        });
        getViewBinding().turnSim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    SettingContent.getInstance().setPageTurnMode(SettingContent.PageTurnMode.MODE_LR_SIM);
                    pageTurner =create(SettingContent.getInstance().getPageTurnMode())  ;
                    updatePageTurner(pageTurner);
                }
            }
        });


        getViewBinding().prePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readerView.prePage();
            }
        });

        getViewBinding().nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                readerView.nextPage();

            }
        });

        getViewBinding().fullScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                SettingContent.getInstance().setFullScreen(isChecked);
                updateFullScreen(isChecked);
                prepareLayer.updateCache();


            }
        });

        boolean bold = (TextSchemeContent.TEXT_STYLE_BOLD & TextSchemeContent.getTextStyle()) !=0;
        getViewBinding().bold.setChecked(bold);
        updatePaintBold(bold);

        getViewBinding().bold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                int textStyle = TextSchemeContent.getTextStyle();
                if(isChecked)
                {
                    textStyle|=TextSchemeContent.TEXT_STYLE_BOLD;
                }else
                  textStyle&=~TextSchemeContent.TEXT_STYLE_BOLD;

                TextSchemeContent.setTextStyle(textStyle);

                updatePaintBold(isChecked);


            }
        });

        boolean italic = (TextSchemeContent.TEXT_STYLE_ITALIC & TextSchemeContent.getTextStyle()) !=0;
        getViewBinding().italic.setChecked(italic);
        updatePaintItalic(italic);
        getViewBinding().italic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {




                int textStyle = TextSchemeContent.getTextStyle();
                if(isChecked)
                {
                    textStyle|=TextSchemeContent.TEXT_STYLE_ITALIC;
                }else
                    textStyle&=~TextSchemeContent.TEXT_STYLE_ITALIC;

                TextSchemeContent.setTextStyle(textStyle);
                updatePaintItalic(isChecked);


            }
        });

        boolean underLine = (TextSchemeContent.TEXT_STYLE_UNDERLINE & TextSchemeContent.getTextStyle()) !=0;
        getViewBinding().underline.setChecked(underLine);
        updatePaintUnderLine(underLine);
        getViewBinding().underline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {




                int textStyle = TextSchemeContent.getTextStyle();
                if(isChecked)
                {
                    textStyle|=TextSchemeContent.TEXT_STYLE_UNDERLINE;
                }else
                    textStyle&=~TextSchemeContent.TEXT_STYLE_UNDERLINE;

                TextSchemeContent.setTextStyle(textStyle);
                updatePaintUnderLine(isChecked);


            }
        });
        getViewBinding().sideEffect.setChecked(SettingContent.getInstance().isBookSideEffect());

        getViewBinding().sideEffect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SettingContent.getInstance().setBookSideEffect(isChecked);
                onSizeChangeLister.onSizeChanged(readerView.getWidth(),readerView.getHeight());
                bookReadController.bookSideEffectChanged();



            }
        });



        getViewBinding().tts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //弹出听书选择
                startBindBookPlayService();







            }
        });


    }

    private void startBindBookPlayService() {


        Intent intent=new Intent(this, BookPlayService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }else
        {
            startService(intent);
        }




    }

    private void updateFullScreen(boolean isChecked) {

        WindowHelper.fullScreen(this,isChecked);
    }

    private void updatePaintUnderLine(boolean underLine) {


        int newFlag = paint.getFlags();
        if (underLine) {
            newFlag |=   Paint.UNDERLINE_TEXT_FLAG;
        } else {
            newFlag &=   ~Paint.UNDERLINE_TEXT_FLAG;
        }
        paint.setFlags(newFlag);// 获取字体
        if(prepareLayer!=null)
            prepareLayer.updateCache();

    }

    private void updatePaintItalic(boolean italic) {

        if (italic) {
            paint.setTextSkewX(-0.25f);
        } else {
            paint.setTextSkewX(0);
        }

        prepareLayer.updateCache();
    }



    private void updatePageTurner(IPageTurner pageTurner)
    {



        if(pageTurner!=null)
        {


            if(drawParam!=null)
                pageTurner.updateDrawParam(drawParam);
            drawLayer.setPageTurner(pageTurner);

        }
    }

    private void updatePaintBold(boolean bold)
    {

        int newFlag = paint.getFlags();
        if (bold) {
            newFlag |=   Paint.FAKE_BOLD_TEXT_FLAG;
        } else {
            newFlag &=   ~Paint.FAKE_BOLD_TEXT_FLAG;
        }
        paint.setFlags(newFlag);
        if(prepareLayer!=null)
            prepareLayer.updateCache();

    }

    private void hideSettingPanel() {
        getViewBinding().styleMode.clearAnimation();
        getViewBinding().styleMode.startAnimation(AnimationUtils.loadAnimation(TextReadActivity.this, R.anim.hide_right_anim));
        getViewBinding().panelSetting.setVisibility(View.GONE);
    }


    private IPageTurner create(@SettingContent.PageTurnMode int pageTurnMode) {


        IPageTurner pageTurner;
        switch (pageTurnMode) {
            case SettingContent.PageTurnMode
                    .MODE_LR_SLIP:
                pageTurner = new SlidePageTurner(this, pageSwitchListener, getViewBinding().reader, prepareLayer);
                break;
            case SettingContent.PageTurnMode
                    .MODE_LR_SCROLL:
                pageTurner = new ScrollPageTurner(this, pageSwitchListener, getViewBinding().reader, prepareLayer);

                break;
            case SettingContent.PageTurnMode
                    .MODE_LR_SIM:
            default:

                pageTurner = new SimulatePageTurner(this, pageSwitchListener, getViewBinding().reader, prepareLayer);
                break;
        }


        return pageTurner;



    }

    private void updateTypeface()
    {

        String typefaceString = SettingContent.getInstance().getTypeface();
        TypefaceEntity entity= JsonFactory.getInstance().fromJson(typefaceString,TypefaceEntity.class);
        updateTypeface(entity);

    }


    private void updateTypeface(final TypefaceEntity entity)
    {



        new AsyncTask<Void,Void,Typeface>() {
            @Override
            protected Typeface doInBackground(Void[] objects) {

                Typeface typeface=null;
                if(entity!=null&& !StringUtil.isEmpty(entity.getTypeface()))
                    typeface=TypefaceHelper.createFromFile(entity.getTypeface());
                return typeface;
            }

            @Override
            protected void onPostExecute(Typeface typeface) {


                if(typeface!=null) {
                    paint.setTypeface(typeface);// 获取字体
                    ParaTypeset.setPaint(paint);
                    if(bookReadController!=null)
                        bookReadController.fontStyleChange();
                }

            }
        }.executeOnExecutor(ThreadConst.THREAD_POOL_EXECUTOR);


    }

    @Override
    protected void onDestroy() {
        if (prepareLayer != null) {
            prepareLayer.onDestroy();
        }
        if(isBookServiceBound)
            bookServiceBinder.unbindService();



        super.onDestroy();
    }

    @Override
    public void onPagePrepared(PreparePageInfo preparePageInfo) {
        this.preparePageInfo = preparePageInfo;

        if (preparePageInfo != null) {

            PageInfo currentPageInfo = preparePageInfo.getCurrentPageInfo();


            int progress=0;
            if (currentPageInfo != null&&currentPageInfo.getFileSize()>0) {
                if (currentPageInfo.isLastPage())
                    progress = 100;
                else if (currentPageInfo.isFirstPage()) {
                    progress = 0;
                } else
                {
                    progress=  (int) (currentPageInfo.getStartPos() * 100/ currentPageInfo.getFileSize());
                }
            }

            getViewBinding().progress.setProgress(progress);

            prepareLayer.onPagePrepared(preparePageInfo);

        }

    }


    public void bindSettingValue()
    {
        getViewBinding().fontSize.setText(String.valueOf(SettingContent.getInstance().getTextSize()));

    }

    private void jumpTypeface(Activity activity) {
        FragmentForResult.startForResult(activity, TypefaceActivity.class, 0, null, new FragmentForResult.ActResult() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {

                    updateTypeface();

                }

            }
        });
    }
}
