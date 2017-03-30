package com.nexdecade.demoapp;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.File;


/**
 * Created by Himel on 3/29/2017.
 */

public class HomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imageView;
    private Handler handler;
    CustomAdapter myArrAdapter;
    private ListView videoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        toolbar   = (Toolbar)findViewById(R.id.app_bar);

        setSupportActionBar(toolbar);
        handler = new Handler();
        myArrAdapter = new CustomAdapter(this);
        videoList = (ListView)findViewById(R.id.videoList);
        handler.post(new Runnable() {
            @Override
            public void run() {
                String path = Environment.getExternalStorageDirectory().toString()+"/dcim/camera";
                Log.d("Files", "Path: " + path);
                File directory = new File(path);
                File[] files = directory.listFiles();

                for (int i = 0; i < files.length; i++)
                {
                    if(files[i].getName().contains("VID")){

                        myArrAdapter.add(files[i].getName());
                    }

                }
                videoList.setAdapter(myArrAdapter);


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
