package com.nexdecade.demoapp.component;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.nexdecade.demoapp.R;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by Himel on 3/27/2017.
 */

public class CustomMediaController extends MediaController {

    private static final String APP_SETTINGS = "SETTINGS";
    private MediaPlayerControl mPlayer;
    private final Context mContext;
    private View mRoot;
    private static final int sDefaultTimeout = 10000;

    private ImageButton mPauseButton;
    private ImageButton mPlayButton;
    private ImageButton mSoundButton;
    private ToggleBtn toggleBtn;
    private SeekBar volControl;
    private CustomMediaControllerHQListner customMediaControllerHQListner;
    private AudioManager audioManager;

    public interface CustomMediaControllerHQListner{
        void setHQUri(ToggleBtn toggleBtn);
    };

    public CustomMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public CustomMediaController(Context context, boolean useFastForward) {
        super(context, useFastForward);
        mContext = context;
    }

    public CustomMediaController(Context context) {
        super(context);
        mContext = context;
    }

    protected View makeControllerView(){
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflate.inflate(com.nexdecade.demoapp.R.layout.media_controller, null);

        initControllerView(mRoot);
        return mRoot;
    }

    private void initControllerView(View v) {
        /*Resources res = mContext.getResources();
        mPlayDescription = res
                .getText(com.android.internal.R.string.lockscreen_transport_play_description);*/
        /*mPauseDescription = res
                .getText(com.android.internal.R.string.lockscreen_transport_pause_description);*/
        mPauseButton = (ImageButton) v.findViewById(R.id.pauseBtn);
        if (mPauseButton != null) {
            //mPauseButton.requestFocus();
            mPauseButton.setOnClickListener(mPauseListener);
        }

        mPlayButton = (ImageButton) v.findViewById(R.id.playBtn);
        if(mPlayButton != null){
            //mPlayButton.requestFocus();
            mPlayButton.setOnClickListener(mPlayListener);
        }

        toggleBtn = (ToggleBtn) v.findViewById(R.id.toggleBtn);
        if(toggleBtn != null){
            toggleBtn.setOnClickListener(toggleBtnListener);
            toggleBtn.setVisibility(INVISIBLE);
        }

        mSoundButton = (ImageButton)v.findViewById(R.id.soundControl);
        if(mSoundButton != null){
            mSoundButton.setOnClickListener(mSoundButtonListener);
            if(toggleBtn.getVisibility() == View.INVISIBLE){
                mSoundButton.setTranslationX((float)60.0);
            }
        }



        audioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volControl = (SeekBar)v.findViewById(R.id.volBar);
        volControl.setVisibility(INVISIBLE);
        //volControl.getThumb().setColorFilter(0xff00ffff,PorterDuff.Mode.MULTIPLY);

        if(toggleBtn.getVisibility() == View.INVISIBLE){
            volControl.setTranslationX(115);

        }

        volControl.setMax(maxVolume);
        volControl.setProgress(curVolume);
        volControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                volControl.setVisibility(INVISIBLE);
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, arg1, 0);
            }
        });

        mPauseButton.setVisibility(VISIBLE);
        mPlayButton.setVisibility(INVISIBLE);



        /*mFfwdButton = (ImageButton) v.findViewById(com.android.internal.R.id.ffwd);
        if (mFfwdButton != null) {
            mFfwdButton.setOnClickListener(mFfwdListener);
            if (!mFromXml) {
                mFfwdButton.setVisibility(mUseFastForward ? View.VISIBLE : View.GONE);
            }
        }

        mRewButton = (ImageButton) v.findViewById(com.android.internal.R.id.rew);
        if (mRewButton != null) {
            mRewButton.setOnClickListener(mRewListener);
            if (!mFromXml) {
                mRewButton.setVisibility(mUseFastForward ? View.VISIBLE : View.GONE);
            }
        }*/

        // By default these are hidden. They will be enabled when setPrevNextListeners() is called
        /*mNextButton = (ImageButton) v.findViewById(com.android.internal.R.id.next);
        if (mNextButton != null && !mFromXml && !mListenersSet) {
            mNextButton.setVisibility(View.GONE);
        }
        mPrevButton = (ImageButton) v.findViewById(com.android.internal.R.id.prev);
        if (mPrevButton != null && !mFromXml && !mListenersSet) {
            mPrevButton.setVisibility(View.GONE);
        }*/

        /*mProgress = (ProgressBar) v.findViewById(com.android.internal.R.id.mediacontroller_progress);
        if (mProgress != null) {
            if (mProgress instanceof SeekBar) {
                SeekBar seeker = (SeekBar) mProgress;
                seeker.setOnSeekBarChangeListener(mSeekListener);
            }
            mProgress.setMax(1000);
        }*/

        /*mEndTime = (TextView) v.findViewById(com.android.internal.R.id.time);
        mCurrentTime = (TextView) v.findViewById(com.android.internal.R.id.time_current);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        installPrevNextListeners();*/
    }

    private final View.OnClickListener mPauseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPlayer.pause();
            v.setVisibility(INVISIBLE);
            mPlayButton.setVisibility(VISIBLE);
        }
    };

    private final View.OnClickListener mPlayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPlayer.start();
            v.setVisibility(INVISIBLE);
            mPauseButton.setVisibility(VISIBLE);
        }
    };

    private final View.OnClickListener toggleBtnListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            if(customMediaControllerHQListner != null) {
                ToggleBtn toggleBtn = (ToggleBtn) v;
                mPlayer.pause();
                customMediaControllerHQListner.setHQUri(toggleBtn);
                mPlayer.start();
                show(10000);
            }

        }
    };

    private final View.OnClickListener mSoundButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(volControl.getVisibility() == View.VISIBLE) {
                volControl.setVisibility(INVISIBLE);
            }else{
                volControl.setVisibility(VISIBLE);
            }
        }
    };

    public void setOnToggleHQListener(CustomMediaControllerHQListner hqListener){
        customMediaControllerHQListner = hqListener;
    }

    public void setMediaPlayer(VideoView player) {
        mPlayer = player;
        updatePausePlay();
    }

    private void updatePausePlay() {
        if (mRoot == null || mPauseButton == null)
            return;
        if (mPlayer.isPlaying()) {
            mPauseButton.setVisibility(VISIBLE);
            mPlayButton.setVisibility(INVISIBLE);
        }else{
            mPauseButton.setVisibility(INVISIBLE);
            mPlayButton.setVisibility(VISIBLE);
        }



    }

    public void setToggleHQButtonState(boolean checked){
        SharedPreferences.Editor editor = mContext.getSharedPreferences(APP_SETTINGS, mContext.MODE_PRIVATE).edit();
        editor.putBoolean("checked",checked);
        editor.commit();

        if(checked){
            toggleBtn.setAlpha((float)1.0);
        }else{
            toggleBtn.setAlpha((float)0.5);
        }
    }

    private void toggleHQButton(boolean checked){
        if(checked){
            toggleBtn.setAlpha((float)1.0);
        }else{
            toggleBtn.setAlpha((float)0.5);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        updatePausePlay();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP:
                show(10000); // start timeout
                break;
            case MotionEvent.ACTION_CANCEL:
                hide();
                break;
            default:
                break;
        }
        return true;
    }

    public void setChecked(boolean checked){

        /*toggleHQButton(c);
        SharedPreferences.Editor editor = mContext.getSharedPreferences(APP_SETTINGS, mContext.MODE_PRIVATE).edit();
        editor.putBoolean("checked",c);
        editor.commit();
*/

        setToggleHQButtonState(checked);


    }

    public boolean isChecked(){
        SharedPreferences prefs = mContext.getSharedPreferences(APP_SETTINGS, mContext.MODE_PRIVATE);
        boolean checked = prefs.getBoolean("checked",false);
        return checked;
    }
}
