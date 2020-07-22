// {{{
package ivanwfr.rtabs.sandbox.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.TouchDelegate;
import android.widget.CheckBox;
import android.widget.FrameLayout;

// }}}
public class TouchDelegateLayout extends FrameLayout
{
    // TouchDelegateLayout {{{
    public TouchDelegateLayout(Context context)
    {
        super(context);
        init();
    }
    //}}}
    // TouchDelegateLayout {{{
    public TouchDelegateLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }
    //}}}
    // TouchDelegateLayout {{{
    public TouchDelegateLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }
    //}}}
    // init {{{
    private CheckBox mButton;

    private void init()
    {
        mButton = new CheckBox(getContext());
        mButton.setText("Click Anywhere On Screen");

        addView(mButton, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER));
    }
    //}}}
    // onMeasure {{{
    /*
     * TouchDelegate is applied to this view (parent) to delegate all touches
     * within the specified rectangle to the CheckBox (child).  Here, the rectangle
     * is the entire size of this parent view.
     * 
     * This must be done after the view has measured itself so we know how big to make the rect,
     * thus we've chosen to add the delegate in onMeasure()
     */
    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //Apply the whole area of this view as the delegate area
        @SuppressLint("DrawAllocation") Rect bounds = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
        @SuppressLint("DrawAllocation") TouchDelegate delegate = new TouchDelegate(bounds, mButton);
        setTouchDelegate(delegate);
    }
    //}}}
}
