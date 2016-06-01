package com.binteng.activity;

import android.Manifest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.view.View;
import android.widget.Button;

import com.binteng.AbsActivity;
import com.binteng.R;
import com.binteng.mview.AudioFrequencyView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import butterknife.BindView;

public class AudioFrequencyActivity extends AbsActivity {

    @BindView(R.id.frequently)
    AudioFrequencyView frequently;

    @BindView(R.id.reset_bt)
    Button resetBt;

    private MediaPlayer mMediaPlayer;
    private Visualizer mVisualizer;

    @Override
    public void initData() {
        super.initData();

        PermissionListener feedbackViewPermissionListener = new CompositePermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                super.onPermissionGranted(response);
                init();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                super.onPermissionDenied(response);
                finish();
            }
        };

        PermissionListener dialogOnDeniedPermissionListener =
                DialogOnDeniedPermissionListener.Builder.withContext(this)
                        .withTitle(R.string.audio_permission_denied_dialog_title)
                        .withMessage(R.string.audio_permission_denied_dialog_feedback)
                        .withButtonText(android.R.string.ok)
                        .withIcon(R.mipmap.ic_launcher)
                        .build();

        PermissionListener audioPermissionListener = new CompositePermissionListener(feedbackViewPermissionListener,
                dialogOnDeniedPermissionListener);

        if (Dexter.isRequestOngoing()) {
            return;
        }

        Dexter.checkPermission(audioPermissionListener, Manifest.permission.RECORD_AUDIO);

    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_frequently;
    }


    public void resetMediaPlayer() {
        mMediaPlayer.seekTo(0);
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }


    private void init() {
        // Create the MediaPlayer
        mMediaPlayer = MediaPlayer.create(this, R.raw.a);

        mMediaPlayer.setLooping(true);

        setupVisualizerFxAndUI();

        mVisualizer.setEnabled(true);

        // 设置了均衡器就与音量大小无关拉
        Equalizer mEqualizer = new Equalizer(0, mMediaPlayer.getAudioSessionId());
        mEqualizer.setEnabled(true);

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                mVisualizer.setEnabled(false);
                setVolumeControlStream(AudioManager.STREAM_SYSTEM);

            }
        });

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mMediaPlayer.start();// 开始播放

        resetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetMediaPlayer();
            }
        });

    }

    private void setupVisualizerFxAndUI() {

        final int maxCR = Visualizer.getMaxCaptureRate();
        // 实例化Visualizer，参数SessionId可以通过MediaPlayer的对象获得
        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
        // 设置需要转换的音乐内容长度，专业的说这就是采样,该采样值一般为2的指数倍
        mVisualizer.setCaptureSize(256);
        // 接下来就好理解了设置一个监听器来监听不断而来的所采集的数据。一共有4个参数，第一个是监听者，第二个单位是毫赫兹，表示的是采集的频率，第三个是是否采集波形，第四个是是否采集频率
        mVisualizer.setDataCaptureListener(
                // 这个回调应该采集的是波形数据
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        frequently.updateVisualizer(bytes);
                    }

                    // 这个回调应该采集的是快速傅里叶变换有关的数据
                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] fft, int samplingRate) {
                        frequently.updateVisualizer(fft);
                    }
                }, maxCR / 2, false, true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isFinishing() && mMediaPlayer != null) {
            mVisualizer.release();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        frequently.dispose();
    }


}