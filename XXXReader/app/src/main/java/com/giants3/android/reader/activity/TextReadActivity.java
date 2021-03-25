package com.giants3.android.reader.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.giants3.android.adapter.AbstractRecyclerAdapter;
import com.giants3.android.frame.util.Log;
import com.giants3.android.reader.R;
import com.giants3.android.reader.databinding.ActivityTextReaderBinding;
import com.xxx.reader.TextScheme;
import com.giants3.android.reader.vm.TextReadViewModel;
import com.giants3.yourreader.text.BackgroundManager;
import com.giants3.yourreader.text.BookService;
import com.giants3.yourreader.text.TextPageBitmap;
import com.giants3.yourreader.text.TextPageInfo;
import com.xxx.reader.ReaderView;
import com.xxx.reader.TextSchemeContent;
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
import com.xxx.reader.turnner.SimulatePageTurner;
import com.xxx.reader.turnner.sim.SettingContent;

import java.util.Calendar;
import java.util.List;


public class TextReadActivity extends BaseViewModelActivity<ActivityTextReaderBinding, TextReadViewModel> implements PrepareListener {
    BookService.BookReadController bookReadController;
    PagePlayer<IChapter, TextPageInfo, DrawParam, TextPageBitmap> prepareLayer;

    private IBook iBook;
    ServiceConnection conn;
    DrawParam drawParam;
    private PreparePageInfo preparePageInfo;

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

        Intent intent = new Intent(this, BookService.class);
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                bookReadController = (BookService.BookReadController) service;
                if (drawParam != null)
                    bookReadController.updateDrawParam(drawParam);
                bookReadController.setPrepareListener(TextReadActivity.this);
                if (iBook != null)
                    bookReadController.updateBook(iBook);
//                getViewModel().getBookInfo().observe(TextReadActivity.this, new Observer<IBook>() {
//                    @Override
//                    public void onChanged(IBook iBook) {
//
//
//
//                        if (iBook != null)
//                            bookReadController.updateBook(iBook);
//                    }
//                });

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {


            }
        };
        bindService(intent, conn, Service.BIND_AUTO_CREATE);


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



                getViewBinding().styleMode.clearAnimation();
                getViewBinding().styleMode.startAnimation(AnimationUtils.loadAnimation(TextReadActivity.this, R.anim.hide_right_anim));
               getViewBinding().panelSetting.setVisibility(View.GONE);
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
        getViewBinding().lineSpaceReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(preparePageInfo!=null)
                {
                    PageInfo currentPageInfo = preparePageInfo.getCurrentPageInfo();
                    if(currentPageInfo instanceof  TextPageInfo)
                    {
                        ((TextPageInfo) currentPageInfo).updateElements();
                    }


                    prepareLayer.onPagePrepared(preparePageInfo);
                }





                bookReadController.lineSpaceReduce();
                bindSettingValue();

            }
        });
        getViewBinding().lineSpaceAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bookReadController.lineSpaceAdd();
                bindSettingValue();

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
                return new TextPageBitmap(context, wh[0], wh[1], readerView);
            }
        });
        builder.setiDrawable(readerView);


        prepareLayer = builder.createPrepareLayer();

        drawParam = new DrawParam();
        drawParam.width = wh[0];
        drawParam.height = wh[1];
        prepareLayer.updateDrawParam(drawParam);
        readerView.setOnSizeChangeListener(new ReaderView.onSizeChangeLister() {
            @Override
            public void onSizeChanged(int width, int height) {

                BackgroundManager.getInstance().update(width,height);
                DrawParam drawParam = new DrawParam();
                drawParam.width = width;
                drawParam.height = height;
                prepareLayer.updateDrawParam(drawParam);
                TextReadActivity.this.drawParam = drawParam;
                if (bookReadController != null)
                    bookReadController.updateDrawParam(drawParam);
            }
        });

        DrawLayer drawLayer = new DrawLayer(this, prepareLayer, readerView);
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
        PageSwitchListener pageSwitchListener = new PageSwitchListener() {
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


        IPageTurner pageTurner = null;
        //  pageTurner = new ScrollPageTurner(this, pageSwitchListener, activityTextReaderBinding.reader, prepareLayer);
//        pageTurner=new SimPageTurner(this,pageSwitchListener,activityTextReaderBinding.reader,prepareLayer);
        pageTurner = new SimulatePageTurner(this, pageSwitchListener, readerView, prepareLayer);
        //   pageTurner = new SlidePageTurner(this, pageSwitchListener, activityTextReaderBinding.reader, prepareLayer);

        drawLayer.setPageTurner(pageTurner);
        readerView.setDrawLayer(drawLayer);


    }

    @Override
    protected void onDestroy() {
        if (prepareLayer != null) {
            prepareLayer.onDestroy();
        }
        if (conn != null)
            unbindService(conn);


        super.onDestroy();
    }

    @Override
    public void onPagePrepared(PreparePageInfo preparePageInfo) {
        this.preparePageInfo = preparePageInfo;

        if (preparePageInfo != null) {

            PageInfo currentPageInfo = preparePageInfo.getCurrentPageInfo();


            getViewBinding().progress.setProgress(currentPageInfo == null ? 0 : (int) (currentPageInfo.startPos * 100/currentPageInfo.fileSize));

            prepareLayer.onPagePrepared(preparePageInfo);

        }

    }


    public void bindSettingValue()
    {
        getViewBinding().fontSize.setText(String.valueOf(SettingContent.getInstance().getTextSize()));
        getViewBinding().lineSpace.setText(String.valueOf(SettingContent.getInstance().getLineSpace()));
    }
}
