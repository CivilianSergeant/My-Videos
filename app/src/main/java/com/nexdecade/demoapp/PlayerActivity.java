package com.nexdecade.demoapp;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.nexdecade.demoapp.component.CustomMediaController;
import com.nexdecade.demoapp.component.ToggleBtn;


public class PlayerActivity extends AppCompatActivity {

    private static int REQUEST_CODE = 0;
    private VideoView videoView;
    private String VIDEO_URI = null;
    private ProgressBar progressBar;
    private CustomMediaController mediaController;
    private Handler handler;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();


        videoView    = (VideoView) findViewById(R.id.videoView);
        progressBar  = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF00FFFF, android.graphics.PorterDuff.Mode.MULTIPLY);
        handler = new Handler();

        if(intent != null){
            videoView.setVideoURI(intent.getData());
        }else{
            videoView.setVideoURI(Uri.parse(VIDEO_URI));
        }


        mediaController = new CustomMediaController(this);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);


        /*mediaController.setOnToggleHQListener(new CustomMediaController.CustomMediaControllerHQListner() {
            @Override
            public void setHQUri(ToggleBtn toggleBtn) {
                progressBar.setVisibility(View.VISIBLE);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            videoView.setVideoURI(Uri.parse(VIDEO_URI));
                            mediaController.setChecked(true);
                        }
                    });
            }
        });*/

        videoView.setMediaController(mediaController);
        progressBar.setVisibility(View.VISIBLE);
        videoView.start();






        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                Log.d("HIMEL","WHAT = "+ what+ " Extra = "+extra);
                if(what == MediaPlayer.MEDIA_INFO_BUFFERING_START){
                    progressBar.setVisibility(View.VISIBLE);
                } else if(what == MediaPlayer.MEDIA_INFO_BUFFERING_END){
                    progressBar.setVisibility(View.INVISIBLE);
                } else if(what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START){
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d("HIMEL","ELSE "+what);
                }


                return false;
            }
        });



        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("HIMEL","ERROR_WHAT = "+ what+ " Extra = "+extra);
                return false;
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if(mp.isPlaying()){
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d("HIMEL","Playing");
                }
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                    Log.d("HIMEL","ON COMPLETE PLAYING");
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    );

        }


    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        REQUEST_CODE = requestCode;
    }

    @Override
    public void onBackPressed() {
        if(videoView != null && videoView.isPlaying()){
            videoView.pause();
        }
        finishActivity(REQUEST_CODE);
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        mediaController.removeAllViews();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
