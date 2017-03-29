package com.nexdecade.demoapp;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Himel on 3/29/2017.
 */

public class CustomAdapter implements ListAdapter {

    ArrayList<String> adapter;
    Context mContext;
    Handler handler = new Handler();
    public CustomAdapter(Context c){
        mContext = c;
        adapter = new ArrayList<>();
    }

    public void add(String s){
        adapter.add(s);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return adapter.size();
    }

    @Override
    public Object getItem(int position) {
        return adapter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            View newView = convertView;
            final ViewHolder viewHolder;

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            String itemTxt = adapter.get(position).toString();
            String path = Environment.getExternalStorageDirectory() + "/dcim/camera/" + itemTxt;
            final File imgFile = new File(path);

            if(newView == null) {
                newView = inflater.inflate(R.layout.list_item, parent, false);
                viewHolder = new ViewHolder();

                viewHolder.filename  = (TextView) newView.findViewById(R.id.filename);
                viewHolder.thumbnail = (ImageView) newView.findViewById(R.id.thumbnail);
                viewHolder.thumbnail.setTag(position);
                newView.setTag(viewHolder);

            }else{
                viewHolder = (ViewHolder) newView.getTag();
            }


            viewHolder.filename.setText(itemTxt);

        
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (imgFile.exists()) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2;
                        FileInputStream fileInputStream = null;
                        try {
                            fileInputStream = new FileInputStream(imgFile.getAbsolutePath());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } finally {
                            if (fileInputStream != null) {
                                Bitmap myBitmap = BitmapFactory.decodeStream(fileInputStream, null, options);
                                viewHolder.thumbnail.setImageBitmap(myBitmap);
                                viewHolder.thumbnail.setBackgroundColor(Color.TRANSPARENT);
                            }
                        }


                    }
                }
            });




        return newView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    static class ViewHolder {
        ImageView thumbnail;
        TextView filename;

    }
}
