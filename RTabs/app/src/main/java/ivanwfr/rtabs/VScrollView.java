package ivanwfr.rtabs; // {{{

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

// }}}
public class VScrollView extends ScrollView
{
    // VScrollView INSTANCE {{{
    public  VScrollView(Context context)                                   { super(context);                  }
    public  VScrollView(Context context, AttributeSet attrs)               { super(context, attrs);           }
    public  VScrollView(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); }
//  public  VScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) { super(context, attrs, defStyleAttr,defStyleRes); }

    public  HScrollView    hsv = null;
    public  VScrollView    vsv = this;
    public  RTabs       mRTabs = null; // TODO make it a constructor mandatory argument

    // }}}

    // freeze {{{
    private boolean freezed = false;
    private boolean canceled_by_freeze; // when set, from this point on: no more handling of a pending event chain
    private boolean moving;

    public  boolean isfreezed() { return  freezed; }
    public  void      freeze () { freezed =  true; moving = false; canceled_by_freeze = true; }
    public  void    unfreeze () { freezed = false; }
    //}}}

    // onInterceptTouchEvent {{{

    //@Override
    private boolean handling_move;
    public  boolean onInterceptTouchEvent(MotionEvent event)
    {
        if(freezed) {
            return true; // keep on trashing freezed events in onTouchEvent
        }

        if(event.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
//System.err.println("VScrollView.onInterceptTouchEvent.ACTION_DOWN: PointerCount=["+ event.getPointerCount() +"]");

            // monitor unfreezed event handling from ACTION_DOWN to ACTION_UP
            canceled_by_freeze = false;

            // MOVING A CHILD VIEW
            float x  = event.getX() + getScrollX();
            float y  = event.getY() + getScrollY();
            if(hsv != null) {
                x  += hsv.getScrollX();
                y  += hsv.getScrollY();
            }
            if( mRTabs.pick_a_movable_tab_at_xy((int)x, (int)y) ) {
                handling_move = true;
                return true;
            }
            // ZOOM-PAN-SCROLL .. (do not interfere)
            handling_move = false;
        }
        return super.onInterceptTouchEvent(event);
    }
    //}}}

    // onTouchEvent {{{
    private float   mx, my, curX, curY;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // LOCK SUPPORT
        if(freezed) {
            //if(event.getActionMasked() == MotionEvent.ACTION_UP) mRTabs.release_dragBand("VScrollView.onTouchEvent.ACTION_UP");
            return false;
        }

        // not freezed but: some part of this event chain has been freezed
        if(canceled_by_freeze) {
//System.err.println("not freezed but: some part of this event chain has been freezed");
            return false;
        }

        // MOVE A CHILD VIEW
        if(handling_move) {
            moving = false;
            return mRTabs.handle_a_movable_tab_event( event );
        }

        curX       = event.getX();
        curY       = event.getY();
        int dx     = (int)(mx - curX);
    //  int dy     = (int)(my - curY);

        switch( event.getActionMasked() )
        {
            case MotionEvent.ACTION_MOVE:
                // HANDLE HORIZONTAL SCROLL
                if(moving && (hsv != null)) hsv.scrollBy(dx, 0);
                moving = true;
                mx     = curX;
                my     = curY;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                // HANDLE HORIZONTAL SCROLL
                //if(hsv != null) hsv.scrollBy(dx, 0);

                moving = false;
                break;

            default:
                moving = false;
                break;

        }

        try {
            return super.onTouchEvent( event );
        }
        catch(Exception ex) { System.err.println("*** VScrollView.onTouchEvent PointerCount=["+ event.getPointerCount() +"]: "+ ex.getMessage()); }
        return true;
    }
    //}}}

}

