package com.nexdecade.demoapp.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.util.Log;

import com.nexdecade.demoapp.R;


/**
 * Created by Himel on 3/25/2017.
 */

public class ToggleBtn extends AppCompatImageButton {

    private static boolean checked;

    public ToggleBtn(Context context) {
        super(context);
        checked = true;
    }

    public ToggleBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToggleBtn(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setChecked(boolean c){
        checked = c;
    }

    public boolean isChecked(){
        return checked;
    }

    public void showPlayButton(){
        setBackground(getResources().getDrawable(R.drawable.ic_play_arrow_white_24dp,null));
    }

    public void showPauseButton(){
        setBackground(getResources().getDrawable(R.drawable.ic_pause_white_24dp,null));
    }




}
