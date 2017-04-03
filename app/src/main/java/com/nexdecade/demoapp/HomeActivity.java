package com.nexdecade.demoapp;


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;


import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.Menu;

import android.widget.ListView;



/**
 * Created by Himel on 3/29/2017.
 */

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,CustomAdapter.ListItemListener {
    private Toolbar toolbar;
    private CustomAdapter customAdapter;
    private RecyclerView videoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        toolbar   = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        videoList = (RecyclerView) findViewById(R.id.videoList);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        videoList.setLayoutManager(lm);

        customAdapter = new CustomAdapter(this);
        videoList.setAdapter(customAdapter);

        getSupportLoaderManager().initLoader(0,null,this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.DATA,

        };
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;


        return new CursorLoader(
                this,
                MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(customAdapter != null)
            customAdapter.changeCursor(data);
    }

    
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(customAdapter != null)
            customAdapter.changeCursor(null);
    }

    @Override
    public void onClick(Uri uri) {
        Intent intent = new Intent(this,com.nexdecade.demoapp.PlayerActivity.class);
        intent.setData(uri);
        startActivity(intent);
    }
}
