package com.nexdecade.demoapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Himel on 4/1/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{

    private Cursor mMediaStorageCursor;
    private static Activity context;
    private static ListItemListener listItemListener = null;

    public interface ListItemListener{
        void onClick(Uri uri);
    }

    public CustomAdapter(Activity c) {
        context = c;
        this.listItemListener = (ListItemListener) c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Bitmap bitmap = getBitmapFromMediaStorage(position);
        if(bitmap != null){
            holder.getThumbnail().setImageBitmap(bitmap);
        }

        int titleIndex = mMediaStorageCursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE);
        holder.getFilename().setText(mMediaStorageCursor.getString(titleIndex));
        holder.setCursor(mMediaStorageCursor);

    }

    @Override
    public int getItemCount() {
        return (mMediaStorageCursor == null)? 0 : mMediaStorageCursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView thumbnail;
        private TextView filename;
        private Cursor cursor;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            filename  = (TextView)  itemView.findViewById(R.id.filename);

            itemView.setOnClickListener(this);
        }

        public ImageView getThumbnail(){
            return thumbnail;
        }

        public TextView getFilename(){
            return filename;
        }


        public Cursor getCursor() {
            return cursor;
        }

        public void setCursor(Cursor cursor) {
            this.cursor = cursor;
        }

        @Override
        public void onClick(View v) {
            cursor.moveToPosition(getAdapterPosition());
            Uri  uri = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATA)));
            if(listItemListener != null){
                listItemListener.onClick(uri);
            }
        }
    }

    public Cursor swapCursor(Cursor c){
        if (mMediaStorageCursor == c) {
            return null;
        }
        Cursor oldCursor = mMediaStorageCursor;
        mMediaStorageCursor = c;
        notifyDataSetChanged();
        return oldCursor;
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    private Bitmap getBitmapFromMediaStorage(int position){
        int idIndex = mMediaStorageCursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
        int mediaTypeIndex = mMediaStorageCursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);


        /*Log.d("HIMEL","Media Cursor Long: "+mMediaStorageCursor.getLong(idIndex));*/

        mMediaStorageCursor.moveToPosition(position);
        if(mMediaStorageCursor != null) {
            switch (mMediaStorageCursor.getInt(mediaTypeIndex)) {
                case MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE:
                    return MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(),
                            mMediaStorageCursor.getLong(idIndex), MediaStore.Images.Thumbnails.MICRO_KIND, null);


                case MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO:
                    return MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(),
                            mMediaStorageCursor.getLong(idIndex), MediaStore.Video.Thumbnails.MICRO_KIND, null);

                default:
                    return null;
            }
        }
        return null;
    }


}
