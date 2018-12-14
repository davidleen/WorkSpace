package com.giants3.android.player.exo;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

/**
 * Created by davidleen29 on 2018/11/13.
 */

public class ExoPlayerActivity extends Activity {


    public static final String PARAM_URL = "PARAM_URL";
    private DefaultTrackSelector trackSelector;
    private SimpleExoPlayer player;
    SimpleExoPlayerView simpleExoPlayerView;

    private String url;
    private static final String TAG = "ExoPlayerActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_exo_player);
        url = getIntent().getStringExtra(PARAM_URL);
        Log.e(TAG, url);
        //url="http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";


    }


    private void releasePlayer() {
        if (player != null) {

            long currentPosition = player.getCurrentPosition();
            setSeekPosition(url, currentPosition);
            //释放播放器对象
            player.release();
            player = null;
            trackSelector = null;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        initPlayer();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.setPlayWhenReady(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.setPlayWhenReady(false);

    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void initPlayer() {
        simpleExoPlayerView = findViewById(R.id.player_view);
        simpleExoPlayerView.requestFocus();


//        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(VUtil.getApplication(),
//                Util.getUserAgent(VUtil.getApplication(), VUtil.getApplication().getString(R.string.app_name)));
//
//        File cacheFile = new File(VUtil.getApplication().getExternalCacheDir().getAbsolutePath(), "video");
//        VLog.d(TAG, "PlayerWrapper()  cache file:" + cacheFile.getAbsolutePath());
//        SimpleCache simpleCache = new SimpleCache(cacheFile, new LeastRecentlyUsedCacheEvictor(512 * 1024 *     1024)); // 本地最多保存512M, 按照LRU原则删除老数据
//        CacheDataSourceFactory cachedDataSourceFactory = new CacheDataSourceFactory(simpleCache,    dataSourceFactory);


        //创建带宽对象
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        //根据当前宽带来创建选择磁道工厂对象
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        //传入工厂对象，以便创建选择磁道对象
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        //根据选择磁道创建播放器对象
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        //将player和view绑定
        simpleExoPlayerView.setPlayer(player);


        //定义数据源工厂对象
        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getPackageName()));
        //创建Extractor工厂对象，用于提取多媒体文件
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        //创建数据源
        MediaSource mediaSource = new ExtractorMediaSource(
                Uri.parse(url),
                mediaDataSourceFactory, extractorsFactory, null, new ExtractorMediaSource.EventListener() {
            @Override
            public void onLoadError(IOException error) {
                Log.e(TAG, "onLoadError" + error);
            }
        });


//        player.setVideoListener(new SimpleExoPlayer.VideoListener() {
//            @Override
//            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
//
//
//                Log.e(TAG,"onVideoSizeChanged");
//            }
//
//            @Override
//            public void onRenderedFirstFrame() {
//                Log.e(TAG,"onRenderedFirstFrame");
//
//            }
//        });

        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
                Log.e(TAG, "onTimelineChanged");
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.e(TAG, "onTracksChanged");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.e(TAG, "onLoadingChanged:" + isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.e(TAG, "onPlayerStateChanged====playWhenReady:" + playWhenReady + ",playbackState" + playbackState);
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                Log.e(TAG, "onRepeatModeChanged:" + repeatMode);
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.e(TAG, "onPlayerError:" + error);
            }

            @Override
            public void onPositionDiscontinuity() {
                Log.e(TAG, "onPositionDiscontinuity:");
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });
        //添加数据源到播放器中
        player.prepare(mediaSource);

        long seekPosition = getSeekPosition(url);
        if (seekPosition > 0)
            player.seekTo(seekPosition);
    }

    private long getSeekPosition(String url) {


        long position = getPreferences(Context.MODE_PRIVATE).getLong(String.valueOf(url.hashCode()), 0);
        return position;
    }


    private void setSeekPosition(String url, long currentPosition) {

        getPreferences(Context.MODE_PRIVATE).edit().putLong(String.valueOf(url.hashCode()), currentPosition).apply();

    }


}
