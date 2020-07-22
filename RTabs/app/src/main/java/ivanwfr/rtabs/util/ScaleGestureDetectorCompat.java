package ivanwfr.rtabs.util; // {{{

import android.annotation.TargetApi;
import android.os.Build;
import android.view.ScaleGestureDetector;

// }}}
/**
 * A utility class for using {@link android.view.ScaleGestureDetector} in a backward-compatible
 * fashion.
 */
public class ScaleGestureDetectorCompat
{
    // ScaleGestureDetectorCompat {{{
    /**
     * Disallow instantiation.
     */
    private ScaleGestureDetectorCompat()
    {
    }
    //}}}
    // getCurrentSpanX {{{
    /**
     * @see android.view.ScaleGestureDetector#getCurrentSpanX()
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static float getCurrentSpanX(ScaleGestureDetector scaleGestureDetector)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return scaleGestureDetector.getCurrentSpanX();
        } else {
            return scaleGestureDetector.getCurrentSpan();
        }
    }
    //}}}
    // getCurrentSpanY {{{
    /**
     * @see android.view.ScaleGestureDetector#getCurrentSpanY()
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static float getCurrentSpanY(ScaleGestureDetector scaleGestureDetector)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return scaleGestureDetector.getCurrentSpanY();
        } else {
            return scaleGestureDetector.getCurrentSpan();
        }
    }
    //}}}
    // getCurrentSpan {{{
    /**
     * @see android.view.ScaleGestureDetector#getCurrentSpan()
     */
    public static float getCurrentSpan(ScaleGestureDetector scaleGestureDetector)
    {
        return scaleGestureDetector.getCurrentSpan();
    }
    //}}}
}

