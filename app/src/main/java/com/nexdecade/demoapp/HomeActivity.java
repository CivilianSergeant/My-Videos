package com.nexdecade.demoapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;


/**
 * Created by Himel on 3/29/2017.
 */

public class HomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        imageView = (ImageView)findViewById(R.id.videoThumbnail);
        setSupportActionBar(toolbar);

        

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
