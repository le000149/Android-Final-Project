package org.algonquin.cst2355.finalproject.recipe;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import org.algonquin.cst2355.finalproject.R;

/**
 * Custom ImageView subclass to display a rotating loading indicator.
 */
@SuppressLint("AppCompatCustomView")
public class LoadingView extends ImageView {

    private int rotateDegree = 0;
    private  boolean mNeedRotate = false;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //set loading picture
        setImageResource(R.mipmap.loading);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //binding to window
        mNeedRotate = true;
        post(new Runnable() {
            @Override
            public void run() {
                rotateDegree +=30;
                rotateDegree = rotateDegree <= 360 ? rotateDegree:0;
                invalidate();//invoke onDraw
                //continue rotate
                if (mNeedRotate) {
                    postDelayed(this,50);
                }

            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //de binding window
        mNeedRotate = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.rotate(rotateDegree,getWidth()/2,getHeight()/2);
        super.onDraw(canvas);
    }

    public void setImage(){

    }
}
