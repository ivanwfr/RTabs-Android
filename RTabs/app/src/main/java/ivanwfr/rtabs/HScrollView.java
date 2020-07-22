package ivanwfr.rtabs; // {{{

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

// }}}
public class HScrollView extends HorizontalScrollView
{
    // VScrollView INSTANCE {{{
    public  HScrollView(Context context)                                   { super(context);                  }
    public  HScrollView(Context context, AttributeSet attrs)               { super(context, attrs);           }
    public  HScrollView(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); }


//  public  HScrollView             hsv = this;
//  public  VScrollView             vsv = null;
//  public  FullscreenActivity activity = null;

    // }}}

    // freeze {{{
    private boolean freezed = false;



    public  boolean isfreezed() { return  freezed; }
    public  void      freeze () { freezed =  true; }
    public  void    unfreeze () { freezed = false; }
    //}}}



    // onTouchEvent {{{
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // LOCK SUPPORT
        if(freezed) return false;

        super.onTouchEvent( event );

        //return false;
        return super.onTouchEvent( event ); // TODO is this right .. (instead of previous line)
    }
    //}}}
}

