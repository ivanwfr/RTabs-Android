package ivanwfr.rtabs; // {{{

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ScrollView;

// }}}
// {{{
/**
 * A ScrollView subclass with a listener extention
 * http://www.nicholasmelnick.com/entries/104
 */

// }}}
public class LScrollView extends ScrollView
{
    public interface OnScrollViewListener {
	void onScrollChanged(LScrollView v, int l, int t, int oldl, int oldt);
    }
    // CONSTRUCTORS {{{
    public LScrollView(Context context)                                                        { super(context); }
    public LScrollView(Context context, AttributeSet attrs)                                    { super(context, attrs); }
    public LScrollView(Context context, AttributeSet attrs, int defStyleAttr)                  { super(context, attrs, defStyleAttr); }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public LScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) { super(context, attrs, defStyleAttr, defStyleRes); }
    //}}}
    // EVENT LISTENER {{{
    private OnScrollViewListener svl = null;
    public void setOnScrollViewListener(OnScrollViewListener listener)
    {
	svl = listener;
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
	if(isInEditMode()) return;
	if(svl != null)
	    svl.onScrollChanged(this, l, t, oldl, oldt );
	super.onScrollChanged(l, t, oldl, oldt );

	/*// {{{

	   protected void onScrollChanged (int l, int t, int oldl, int oldt)

	   Added in API level 1
	   This is called in response to an internal scroll in this view
	   (i.e., the view scrolled its own contents).
	   
	   This is typically as a result of scrollBy(int, int) or scrollTo(int, int) having been called.

	   Parameters
	   l	Current horizontal scroll origin.
	   t	Current vertical scroll origin.
	   oldl	Previous horizontal scroll origin.
	   oldt	Previous vertical scroll origin.

	 */ // }}}
    }
    //}}}
}

