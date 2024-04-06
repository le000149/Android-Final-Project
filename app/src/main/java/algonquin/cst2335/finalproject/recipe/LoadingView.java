package algonquin.cst2335.finalproject.recipe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import algonquin.cst2335.finalproject.R;

/**
 * A custom ImageView that displays a rotating loading indicator.
 * This view rotates a specified drawable, giving the visual effect of a loading spinner.
 */
@SuppressLint("AppCompatCustomView")
public class LoadingView extends ImageView {

    /**
     * The degree to which the image should be rotated. This value is updated periodically to animate rotation.
     */
    private int rotateDegree = 0;

    /**
     * Flag indicating whether the view should continue rotating. This is set to false when the view is detached from the window.
     */
    private boolean mNeedRotate = false;

    /**
     * Constructs a new LoadingView with the specified context.
     *
     * @param context The Context the view is running in, through which it can access the current theme, resources, etc.
     */
    public LoadingView(Context context) {
        this(context, null);
    }

    /**
     * Constructs a new LoadingView with the specified context and attribute set.
     *
     * @param context The Context the view is running in.
     * @param attrs The attributes of the XML tag that is inflating the view.
     */
    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructs a new LoadingView with the specified context, attribute set, and default style attribute.
     *
     * @param context The Context the view is running in.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource that supplies default values for the view. Can be 0 to not look for defaults.
     */
    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // Sets the image resource to the specified drawable. This drawable visually represents the loading indicator.
        setImageResource(R.mipmap.loading);
    }

    /**
     * Called when the view is attached to a window. At this point it has a Surface and will start drawing.
     * Here we start the rotation animation.
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mNeedRotate = true;
        post(new Runnable() {
            @Override
            public void run() {
                rotateDegree += 30;
                rotateDegree = rotateDegree <= 360 ? rotateDegree : 0;
                invalidate(); // Invokes onDraw to redraw the view with the updated rotation.

                // Continue rotating if needed.
                if (mNeedRotate) {
                    postDelayed(this, 50);
                }
            }
        });
    }

    /**
     * Called when the view is detached from its window. At this point it no longer has a surface for drawing.
     * Here we stop the rotation animation.
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mNeedRotate = false;
    }

    /**
     * Draws the content of this view on the provided Canvas.
     *
     * @param canvas The canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // Rotate the canvas around the center of the view.
        canvas.rotate(rotateDegree, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }

    // Placeholder method, potentially for setting the image dynamically in the future.
    public void setImage() {
        // Implementation for dynamically setting the image can be added here.
    }
}
