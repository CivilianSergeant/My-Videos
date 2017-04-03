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
import android.widget.Toast;
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

    private ToggleBtn mToggleButton;
    private ProgressBar mProgress;
    private CustomMediaControllerHQListner customMediaControllerHQListner;


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
        mToggleButton = (ToggleBtn) v.findViewById(R.id.toggleBtn);
        if (mToggleButton != null) {

            mToggleButton.setOnClickListener(toggleBtnListener);
            mToggleButton.showPauseButton();
            mToggleButton.setChecked(true);
        }



        mProgress = (ProgressBar) v.findViewById(R.id.progressBar);
        if (mProgress != null) {
            if (mProgress instanceof SeekBar) {
                SeekBar seeker = (SeekBar) mProgress;
                /*seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(mPlayer != null && mPlayer.isPlaying()) {
                            int curPosition = mPlayer.getCurrentPosition();
                            Toast.makeText(mContext,String.valueOf(curPosition),Toast.LENGTH_SHORT).show();
                            seekBar.setProgress(curPosition);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });*/


            }
            mProgress.setMax(1000);
            updateProgressBar();
        }

    }

    private void updateProgressBar() {

        postDelayed(mShowProgress,100);
    }


    private final Runnable mShowProgress = new Runnable() {
        @Override
        public void run() {
            int pos = setProgress();
            if (mPlayer.isPlaying()) {

                postDelayed(mShowProgress, 100);
            }
        }
    };

    private int setProgress() {
        if (mPlayer == null) {
            return 0;
        }
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mProgress.setProgress( (int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }

        /*if (mEndTime != null)
            mEndTime.setText(stringForTime(duration));
        if (mCurrentTime != null)
            mCurrentTime.setText(stringForTime(position));*/

        return position;
    }



    private final View.OnClickListener toggleBtnListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            ToggleBtn toggleBtn = (ToggleBtn) v;
            if(customMediaControllerHQListner != null) {

                mPlayer.pause();
                //customMediaControllerHQListner.setHQUri(toggleBtn);
                mPlayer.start();
                show(10000);
            }
            //Toast.makeText(mContext,String.valueOf(toggleBtn.isChecked()),Toast.LENGTH_SHORT).show();
            if(toggleBtn.isChecked()){
                toggleBtn.setChecked(false);
                mPlayer.pause();
                removeCallbacks(mShowProgress);
            }else{
                toggleBtn.setChecked(true);
                mPlayer.start();
                updateProgressBar();
            }
            updatePausePlay();


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
        if (mRoot == null || mToggleButton == null)
            return;
        if (mToggleButton.isChecked()){
            mToggleButton.showPauseButton();
        }else{
            mToggleButton.showPlayButton();
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




    }

    public boolean isChecked(){
        SharedPreferences prefs = mContext.getSharedPreferences(APP_SETTINGS, mContext.MODE_PRIVATE);
        boolean checked = prefs.getBoolean("checked",false);
        return checked;
    }
}
