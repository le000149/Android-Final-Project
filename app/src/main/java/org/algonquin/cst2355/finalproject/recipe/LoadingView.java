package org.algonquin.cst2355.finalproject.recipe;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import org.algonquin.cst2355.finalproject.R;


@SuppressLint("AppCompatCustomView")
public class LoadingView extends ImageView {
    //旋转的角度
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
        //设置图标
        setImageResource(R.mipmap.loading);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //绑定到window时
        mNeedRotate = true;
        post(new Runnable() {
            @Override
            public void run() {
                rotateDegree +=30;
                rotateDegree = rotateDegree <= 360 ? rotateDegree:0;
                invalidate();//该操作调用ondraw
                //是否继续旋转
                if (mNeedRotate) {
                    postDelayed(this,50);
                }

            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //从window解绑时
        mNeedRotate = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /**
         * 第一个参数旋转角度
         * 第二个参数旋转x坐标
         * 第三个参数旋转的y坐标
         */

        canvas.rotate(rotateDegree,getWidth()/2,getHeight()/2);
        super.onDraw(canvas);
    }

    public void setImage(){

    }
}
