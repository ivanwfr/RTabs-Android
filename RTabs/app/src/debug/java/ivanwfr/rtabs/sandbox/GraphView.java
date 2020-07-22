package ivanwfr.rtabs.sandbox; // {{{

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

// COMPAT
//import android.support.v4.os.ParcelableCompat;
//import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.EdgeEffectCompat;

import ivanwfr.rtabs.util.OverScrollerCompat;
import ivanwfr.rtabs.util.ScaleGestureDetectorCompat;
import ivanwfr.rtabs.util.Zoomer;
import ivanwfr.rtabs.Settings;

// }}}
/** Comment {{{
 * A view representing a simple yet interactive line chart for the function x^3 - x/4.
 * 
 * This view isn't all that useful on its own; rather it serves as an example of how to correctly
 * implement these types of gestures to perform zooming and scrolling with interesting content
 * types.
 * 
 * The view is interactive in that it can be zoomed and panned using typical gestures such
 * as double-touch, drag, pinch-open, and pinch-close. This is done using the
 * ScaleGestureDetector, GestureDetector, and OverScroller classes. Note
 * that the platform-provided view scrolling behavior (e.g. View#scrollBy(int, int) is NOT
 * used.
 * 
 * The view also demonstrates the correct use of touch feedback to
 * indicate to users that they've reached the content edges after a pan or fling gesture. This
 * is done using the EdgeEffectCompat class.
 * 
 * Finally, this class demonstrates the basics of creating a custom view, including support for
 * custom attributes (see the constructors), a simple implementation for
 * #onMeasure(int, int), an implementation for #onSaveInstanceState() and a fairly
 * straightforward Canvas-based rendering implementation in
 * #onDraw(android.graphics.Canvas).
 * 
 * Note that this view doesn't automatically support directional navigation or other accessibility
 * methods. Activities using this view should generally provide alternate navigation controls.
 * Activities using this view should also present an alternate, text-based representation of this
 * view's content for vision-impaired users.
 */
// }}}
public class GraphView extends RelativeLayout
{
    // Constructor
    // {{{
    private boolean work_as_a_container = false;
/*
/Override\|work_as_a_container
*/

    public GraphView(Context context                                  ) { this(context,  null,        0,               false); }
    public GraphView(Context context, AttributeSet attrs              ) { this(context, attrs,        0,               false); }
    public GraphView(Context context, AttributeSet attrs, int defStyle) { this(context, attrs, defStyle,               false); }
    public GraphView(Context context,      boolean work_as_a_container) { this(context,  null,        0, work_as_a_container); }
    private GraphView(Context context, AttributeSet attrs, int defStyle, boolean work_as_a_container) {
        super(context, attrs, defStyle);

        if(!work_as_a_container)
            init_model(context, attrs, defStyle);

        init_detectors( context );

        this.work_as_a_container = work_as_a_container;
    }
    //}}}

    // GESTURES
    // VIEWPORT {{{

    // This rectangle represents the currently visible chart domain
    // and range. The currently visible chart X values are from this rectangle's left to its right.
    // The currently visible chart Y values are from this rectangle's top to its bottom.
    // Note:
    // This rectangle's top is actually the smaller Y value, and its bottom is the larger
    // Y value. Since the chart is drawn onscreen in such a way that chart Y values increase
    // towards the top of the screen (decreasing pixel Y positions), this rectangle's "top" is drawn
    // above this rectangle's "bottom" value.
    private static final float AXIS_X_MIN = -1f;
    private static final float AXIS_X_MAX =  1f;
    private static final float AXIS_Y_MIN = -1f;
    private static final float AXIS_Y_MAX =  1f;
    private              RectF mCurrentViewport = new RectF(AXIS_X_MIN, AXIS_Y_MIN, AXIS_X_MAX, AXIS_Y_MAX);

    // The current destination rectangle (in pixel coordinates) into which the chart data should be drawn.
    // Chart labels are drawn outside this area.
    private final Rect mContentRect = new Rect();

    // }}}
    // computeScroll {{{
    @Override
    public void computeScroll()
    {
//System.err.println("XXX computeScroll:");
        super.computeScroll();
        boolean needsInvalidate = false;
        // SCROLL {{{
        if( mScroller.computeScrollOffset() )
        {
            // The scroller isn't finished, meaning a fling or programmatic pan operation is currently active.

            computeScrollablePixelSize(); // updates mScrollablePixelSize
            int currX = mScroller.getCurrX();
            int currY = mScroller.getCurrY();

            set_xy_min_max();
            boolean canScrollX = (mCurrentViewport.left > x_min || mCurrentViewport.right  < x_max);
            boolean canScrollY = (mCurrentViewport.top  > y_min || mCurrentViewport.bottom < y_max);

            // LEFT {{{
            if(         canScrollX
                    &&  currX < 0
                    &&  mEdgeEffectLeft.isFinished()
                    && !mEdgeEffectLeftActive
              ) {
                mEdgeEffectLeft.onAbsorb((int) OverScrollerCompat.getCurrVelocity(mScroller));
                mEdgeEffectLeftActive   = true;
                needsInvalidate         = true;
              }
            // }}}
            // RIGHT {{{
            else if(    canScrollX
                    &&  currX > (mScrollablePixelSize.x - mContentRect.width())
                    &&  mEdgeEffectRight.isFinished()
                    && !mEdgeEffectRightActive
                   ) {
                mEdgeEffectRight.onAbsorb((int) OverScrollerCompat.getCurrVelocity(mScroller));
                mEdgeEffectRightActive  = true;
                needsInvalidate         = true;
                   }
            // }}}
            // DOWN {{{
            if(         canScrollY
                    &&  currY < 0
                    &&  mEdgeEffectTop.isFinished()
                    && !mEdgeEffectTopActive
              ) {
                mEdgeEffectTop.onAbsorb((int) OverScrollerCompat.getCurrVelocity(mScroller));
                mEdgeEffectTopActive    = true;
                needsInvalidate         = true;
              }
            // }}}
            // UP {{{
            else if(    canScrollY
                    &&  currY > (mScrollablePixelSize.y - mContentRect.height())
                    &&  mEdgeEffectBottom.isFinished()
                    && !mEdgeEffectBottomActive
                   ) {
                mEdgeEffectBottom.onAbsorb((int) OverScrollerCompat.getCurrVelocity(mScroller));
                mEdgeEffectBottomActive = true;
                needsInvalidate = true;
                   }
            // }}}

            //.........................(viewport_____)  (part of which scrolled (hidden) to the left)
            float vp_left = x_min + (x_max - x_min) * currX / mScrollablePixelSize.x;

            //.........................(viewport_____)  (part of which scrolled (hidden) to the left)
            float vp_top  = y_min + (y_max - y_min) * currY / mScrollablePixelSize.y;
            setViewportTopLeft(vp_left, vp_top, "computeScroll");
        }
        // }}}
        // ZOOM {{{
        if( mZoomer.computeZoom() )
        {
            // Performs the zoom since a zoom is in progress
            // (either programmatically or via double-touch)
            float newWidth             = (1f - mZoomer.getCurrZoom()) * mScrollerStartViewport.width();
            float newHeight            = (1f - mZoomer.getCurrZoom()) * mScrollerStartViewport.height();
            float pointWithinViewportX = (mZoomFocalPoint.x - mScrollerStartViewport.left) / mScrollerStartViewport.width();
            float pointWithinViewportY = (mZoomFocalPoint.y - mScrollerStartViewport.top) / mScrollerStartViewport.height();
            mCurrentViewport_set( mZoomFocalPoint.x - newWidth  *      pointWithinViewportX
                    ,             mZoomFocalPoint.y - newHeight *      pointWithinViewportY
                    ,             mZoomFocalPoint.x + newWidth  * (1 - pointWithinViewportX)
                    ,             mZoomFocalPoint.y + newHeight * (1 - pointWithinViewportY));
            needsInvalidate = true;
        }
        //}}}
        if( needsInvalidate ) ViewCompat.postInvalidateOnAnimation(this);
report("computeScroll");
    }
    //}}}
    // setViewportTopLeft {{{
    // Sets the current viewport (defined by mCurrentViewport) to the given X and Y positions.
    // Note that the Y value represents the topmost pixel position,
    // and thus the bottom of the mCurrentViewport rectangle.
    private void setViewportTopLeft(float vp_left, float vp_top, String caller)
    {
        // Constrains within the scroll range. The scroll range is simply the viewport extremes
        // (AXIS_X_MAX, etc.) minus the viewport size. For example, if the extrema were 0 and 10,
        // and the viewport size was 2, the scroll range would be 0 to 8.

        float vp_width  = mCurrentViewport.width();
        float vp_height = mCurrentViewport.height();

        set_xy_min_max();

        vp_left = Math.max(x_min, Math.min(vp_left, x_max - vp_width ));
        vp_top  = Math.max(y_min, Math.min(vp_top , y_max - vp_height));
        mCurrentViewport_set(vp_left   , vp_top, vp_left + vp_width, vp_top + vp_height);

        ViewCompat.postInvalidateOnAnimation( this );

report("setViewportTopLeft("+caller+")");

    }
    //}}}
    // mCurrentViewport_set {{{
    private void mCurrentViewport_set(float left, float top, float right, float bottom)
    {
        mCurrentViewport.left   = left;
        mCurrentViewport.top    = top;
        mCurrentViewport.right  = right;
        mCurrentViewport.bottom = bottom;

        // Ensures that current viewport is inside the viewport extremes.
        //constrainViewport();
        set_xy_min_max();
        mCurrentViewport.left   = Math.max(                            x_min ,                 mCurrentViewport.left   );
        mCurrentViewport.top    = Math.max(                            y_min ,                 mCurrentViewport.top    );
        mCurrentViewport.bottom = Math.max(Math.nextUp(mCurrentViewport.top ), Math.min(y_max, mCurrentViewport.bottom));
        mCurrentViewport.right  = Math.max(Math.nextUp(mCurrentViewport.left), Math.min(x_max, mCurrentViewport.right ));

        if(work_as_a_container)
        {
            //float scale = Settings.VIEWPORT_W / (float)mCurrentViewport.width();
            float scale = (float)mContentRect.width() / mCurrentViewport.width();

            if(Settings.DEV_SCALE != scale) {
                setScaleX( scale );
                setScaleY( scale );
                Settings.DEV_SCALE  = scale;
            }
            int x_scroll = (int)(mCurrentViewport.left * scale);
            int y_scroll = (int)(mCurrentViewport.top  * scale);
            scrollTo(x_scroll, y_scroll);
        }

    }
    //}}}
    // releaseEdgeEffects {{{
    private void releaseEdgeEffects()
    {
        mEdgeEffectLeftActive
            = mEdgeEffectTopActive
            = mEdgeEffectRightActive
            = mEdgeEffectBottomActive
            = false;
        mEdgeEffectLeft  .onRelease();
        mEdgeEffectTop   .onRelease();
        mEdgeEffectRight .onRelease();
        mEdgeEffectBottom.onRelease();
    }
    //}}}
    // hitTest {{{
    private boolean hitTest(float x, float y, PointF dest)
    {
        // Finds the chart point (i.e. within the chart's domain and range)
        // represented by the given pixel coordinates,
        // if that pixel is within the chart region described by mContentRect.
        // If the point is found:
        // the "dest" argument is set to the point // and this function returns true.
        // Otherwise, this function returns false and "dest" is unchanged.

        if( !mContentRect.contains((int)x, (int)y) )
            return false;

        dest.set( mCurrentViewport.left + mCurrentViewport.width () * (x - mContentRect.left  ) /  mContentRect.width()
                , mCurrentViewport.top  + mCurrentViewport.height() * (y - mContentRect.bottom) / -mContentRect.height());
        return true;
    }
    //}}}

    // VIEWPORT
    // computeScrollablePixelSize {{{
    private final Point mScrollablePixelSize = new Point();
    private void computeScrollablePixelSize()
    {
        // Computes the current scrollable surface size, in pixels.
        // For example, if the entire chart area is visible,
        // this is simply the current size of mContentRect.
        // If the chart is zoomed in 200% in both directions,
        // the returned size will be twice as large horizontally and vertically.
        set_xy_min_max();
        mScrollablePixelSize.set((int)(mContentRect.width () * (x_max - x_min) / mCurrentViewport.width ())
                ,                (int)(mContentRect.height() * (y_max - y_min) / mCurrentViewport.height()));
    }
    //}}}
    // set_xy_min_max {{{
    private float x_min, x_max, y_min, y_max;
    private void set_xy_min_max()
    {
        if(work_as_a_container)
        {
            x_min = -Settings.VIEWPORT_L;
            y_min = -Settings.VIEWPORT_T;
            x_max =  Settings.VIEWPORT_W + Settings.VIEWPORT_L;
            y_max =  Settings.VIEWPORT_H + Settings.VIEWPORT_T;
        }
        else {
            x_min = AXIS_X_MIN;
            y_min = AXIS_Y_MIN;
            x_max = AXIS_X_MAX;
            y_max = AXIS_Y_MAX;
        }
    }
    //}}}
    // getCurrentViewport {{{
    public RectF getCurrentViewport()
    {
        // Returns the current viewport (visible extremes for the chart domain and range.)
        return new RectF(mCurrentViewport);
    }
    //}}}
    // setCurrentViewport {{{
    public void setCurrentViewport(RectF viewport)
    {
        mCurrentViewport_set(viewport.left, viewport.top, viewport.right, viewport.bottom);
        ViewCompat.postInvalidateOnAnimation(this);
    }
    //}}}
    // getDrawX {{{
    private float getDrawX(float x)
    {
        // Computes the pixel offset for the given X chart value. This may be outside the view bounds.
        return mContentRect.left
            +  mContentRect.width()
            *  (x - mCurrentViewport.left)
            /  mCurrentViewport.width();
    }
    //}}}
    // getDrawY {{{
    private float getDrawY(float y)
    {
        // Computes the pixel offset for the given Y chart value. This may be outside the view bounds.
        return mContentRect.bottom
            -  mContentRect.height()
            *  (y - mCurrentViewport.top)
            /  mCurrentViewport.height();
    }
    //}}}
    // fling {{{
    private void fling(int velocityX, int velocityY)
    {
        // Flings use math in pixels (as opposed to math based on the viewport).

        releaseEdgeEffects();

        computeScrollablePixelSize(); // updates mScrollablePixelSize

        mScrollerStartViewport.set( mCurrentViewport   );

        int startX = (int) (mScrollablePixelSize.x * (mScrollerStartViewport.left - x_min  ) / ( x_max - x_min));
        int startY = (int) (mScrollablePixelSize.y * (y_max - mScrollerStartViewport.bottom) / ( y_max - y_min));

        mScroller.forceFinished( true );
        mScroller.fling(    startX                 ,    startY
                ,        velocityX                 , velocityY
                ,        0                         , mScrollablePixelSize.x - mContentRect.width()
                ,        0                         , mScrollablePixelSize.y - mContentRect.height()
                ,        mContentRect.width () / 2 , mContentRect.height() / 2);

        ViewCompat.postInvalidateOnAnimation( this );
    }
    //}}}

    // EVENTS
    // onTouchEvent {{{
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean detect_S = mScaleGestureDetector.onTouchEvent( event );
        boolean detect_G =      mGestureDetector.onTouchEvent( event );
        boolean detect_U =                 super.onTouchEvent( event );
        boolean   retVal = detect_S || detect_G || detect_U;
/*
        System.err.println("XXX "+ 
                String.format("onTouchEvent: SCALE GESTURE SUPER=[%c] [%c] [%c] ...return "+retVal
                    ,        (detect_S ? 'X' : ' ')
                    ,        (detect_G ? 'X' : ' ')
                    ,        (detect_U ? 'X' : ' ')
                    ));
*/
report("onTouchEvent");
        return retVal;
    }
    //}}}
    // onSizeChanged {{{
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        if(work_as_a_container)
            mContentRect.set( getPaddingLeft()
                    ,         getPaddingTop()
                    ,         getWidth () - getPaddingRight()
                    ,         getHeight() - getPaddingBottom());
        else
            mContentRect.set( getPaddingLeft() + mMaxLabelWidth + mLabelSeparation
                    ,         getPaddingTop()
                    ,         getWidth () - getPaddingRight()
                    ,         getHeight() - getPaddingBottom() - mLabelHeight - mLabelSeparation);
report("onSizeChanged");
    }
    //}}}
    // SimpleOnScaleGestureListener {{{
    // The scale listener, used for handling multi-finger scale gestures.
    private final ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener()
    {
        // {{{
        /**
         * This is the active focal point in terms of the viewport.
         * Could be a local variable but kept here to minimize per-frame allocations.
         */
        private final PointF viewportFocus = new PointF();
        private  float lastSpanX;
        private  float lastSpanY;
        private  float lastSpan;

        // }}}
        // onScaleBegin {{{
        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector)
        {
            lastSpanX = ScaleGestureDetectorCompat.getCurrentSpanX( scaleGestureDetector );
            lastSpanY = ScaleGestureDetectorCompat.getCurrentSpanY( scaleGestureDetector );
            lastSpan  = ScaleGestureDetectorCompat.getCurrentSpan ( scaleGestureDetector );
report("onScaleBegin");
            return true;
        }
        //}}}
        // onScale {{{
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector)
        {
//System.err.println("XXX onScale:");
            float newWidth, newHeight;
            if( work_as_a_container ) {
                float span  = ScaleGestureDetectorCompat.getCurrentSpan (scaleGestureDetector);
                newWidth    = lastSpan  / span  * mCurrentViewport.width();
                newHeight   = lastSpan  / span  * mCurrentViewport.height();
                lastSpan    =             span ;
            }
            else {
                float spanX = ScaleGestureDetectorCompat.getCurrentSpanX(scaleGestureDetector);
                float spanY = ScaleGestureDetectorCompat.getCurrentSpanY(scaleGestureDetector);
                newWidth    = lastSpanX / spanX * mCurrentViewport.width();
                newHeight   = lastSpanY / spanY * mCurrentViewport.height();
                lastSpanX   =             spanX;
                lastSpanY   =             spanY;
            }

            float    focusX = scaleGestureDetector.getFocusX();
            float    focusY = scaleGestureDetector.getFocusY();

            hitTest(focusX, focusY, viewportFocus);

            float left   = viewportFocus.x - newWidth  * (focusX   - mContentRect.left) / mContentRect.width ();
            float top    = viewportFocus.y - newHeight * (mContentRect.bottom - focusY) / mContentRect.height();
            float right  = left + newWidth;
            float bottom = top  + newHeight;

            mCurrentViewport_set(left, top, right, bottom);

            ViewCompat.postInvalidateOnAnimation(GraphView.this);


//System.err.println("XXX onScale: mCurrentViewport=["+mCurrentViewport+"]");
report("onScale");

            return true;
        }
        //}}}
    };
    //}}}
    // SimpleOnGestureListener {{{
    // The gesture listener, used for handling simple gestures such as double touches, scrolls, and flings.
    private final RectF  mScrollerStartViewport = new RectF(); // Used only for zooms and flings.
    private final PointF mZoomFocalPoint        = new PointF();
    private final  GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener()
    {
        // onDown {{{
        @Override
        public boolean onDown(MotionEvent e)
        {
//System.err.println("XXX onDown:");
            releaseEdgeEffects();
            mScrollerStartViewport.set( mCurrentViewport );
            mScroller.forceFinished(true);
            ViewCompat.postInvalidateOnAnimation(GraphView.this);
report("onDown");
            return true;
        }
        //}}}
        // onDoubleTap {{{
        private static final float ZOOM_AMOUNT = 0.25f; // The scaling factor for a single zoom 'step'.

        @Override
        public boolean onDoubleTap(MotionEvent e)
        {
//System.err.println("XXX onDoubleTap:");
            mZoomer.forceFinished(true);
            if (hitTest(e.getX(), e.getY(), mZoomFocalPoint)) {
                mZoomer.startZoom(ZOOM_AMOUNT);
            }
            ViewCompat.postInvalidateOnAnimation(GraphView.this);
report("onDoubleTap");
            return true;
        }
        //}}}
        // onScroll {{{
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
        {
//System.err.println("XXX onScroll:");
            super.onScroll(e1, e2, distanceX, distanceY); // XXX
            // SCROLL {{{
            // Scrolling uses math based on the viewport (as opposed to math using pixels).
            /**
             * Pixel offset is the offset in screen pixels, while viewport offset is the
             * offset within the current viewport.
             */
            float viewportOffsetX = distanceX * mCurrentViewport.width()  / mContentRect.width();
            float viewportOffsetY = distanceY * mCurrentViewport.height() / mContentRect.height();
            if( !work_as_a_container ) viewportOffsetY *= -1;

            computeScrollablePixelSize(); // updates mScrollablePixelSize

            set_xy_min_max();
            int scrolledX
                = (int)(   mScrollablePixelSize.x
                        * (mCurrentViewport.left + viewportOffsetX - x_min)
                        / (x_max - x_min));

            int scrolledY
                = (int)(   mScrollablePixelSize.y
                        * (y_max - mCurrentViewport.bottom - viewportOffsetY)
                        / (y_max - y_min));

            boolean canScrollX
                =  (mCurrentViewport.left   > x_min)
                || (mCurrentViewport.right  < x_max);

            boolean canScrollY
                =  (mCurrentViewport.top    > y_min)
                || (mCurrentViewport.bottom < y_max);

            setViewportTopLeft( mCurrentViewport.left+ viewportOffsetX
                    ,           mCurrentViewport.top + viewportOffsetY
                    , "onScroll");

            //}}}
            // EDGE EFFECT // {{{
            if(canScrollX && scrolledX < 0) {
                mEdgeEffectLeft.onPull(scrolledX / (float) mContentRect.width(), 0); mEdgeEffectLeftActive = true;
            }

            if(canScrollY && scrolledY < 0) {
                mEdgeEffectTop.onPull(scrolledY / (float) mContentRect.height(), 0);
                mEdgeEffectTopActive = true;
            }

            if (canScrollX && scrolledX > mScrollablePixelSize.x - mContentRect.width()) {
                mEdgeEffectRight.onPull( (scrolledX - mScrollablePixelSize.x + mContentRect.width())
                        /         (float)mContentRect.width(), 0);
                mEdgeEffectRightActive = true;
            }

            if(canScrollY && scrolledY > mScrollablePixelSize.y - mContentRect.height()) {
                mEdgeEffectBottom.onPull( (scrolledY - mScrollablePixelSize.y + mContentRect.height())
                        /          (float)mContentRect.height(), 0);
                mEdgeEffectBottomActive = true;
            }
            // }}}
report("onScroll");
            return true;
        }
        //}}}
        // onFling {{{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
//System.err.println("XXX onFling:");
            super.onFling(e1, e2, velocityX, velocityY); // XXX
            fling((int) -velocityX, (int) -velocityY);
report("onFling");
            return true;
        }
        //}}}
    };
    //}}}
    // init_detectors {{{
    private ScaleGestureDetector  mScaleGestureDetector;
    private GestureDetectorCompat mGestureDetector;
    private OverScroller          mScroller;
    private Zoomer                mZoomer;

    private EdgeEffectCompat mEdgeEffectTop;
    private EdgeEffectCompat mEdgeEffectBottom;
    private EdgeEffectCompat mEdgeEffectLeft;
    private EdgeEffectCompat mEdgeEffectRight;
    private boolean          mEdgeEffectTopActive;
    private boolean          mEdgeEffectBottomActive;
    private boolean          mEdgeEffectLeftActive;
    private boolean          mEdgeEffectRightActive;

    private void init_detectors(Context context)
    {
        mScaleGestureDetector   = new ScaleGestureDetector ( context, mScaleGestureListener);
        mGestureDetector        = new GestureDetectorCompat( context, mGestureListener     );
        mScroller               = new OverScroller         ( context                       );
        mZoomer                 = new Zoomer               ( context                       );

        mEdgeEffectLeft         = new EdgeEffectCompat(      context );
        mEdgeEffectTop          = new EdgeEffectCompat(      context );
        mEdgeEffectRight        = new EdgeEffectCompat(      context );
        mEdgeEffectBottom       = new EdgeEffectCompat(      context );
    }
    //}}}

    // MODEL
    // DRAW  {{{
    // onDraw {{{
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

if(work_as_a_container) {
    System.err.println("XXX onDraw: work_as_a_container");
    return;
}

        // Draws axes and text labels
        drawAxes(canvas);

        // Clips the next few drawing operations to the content area
        int clipRestoreCount = canvas.save();
        //canvas.clipRect(mContentRect);

        drawDataSeriesUnclipped ( canvas );

        drawEdgeEffectsUnclipped( canvas );

        // Removes clipping rectangle
        canvas.restoreToCount(clipRestoreCount);

        // Draws chart container
        canvas.drawRect(mContentRect, mAxisPaint);
    }
    //}}}
    // drawAxes {{{
    // {{{
    // Buffers for storing current X and Y stops. See the computeAxisStops method for more details.
    private final char[]    mLabelBuffer  = new char[100];
    private final AxisStops mXStopsBuffer = new AxisStops();
    private final AxisStops mYStopsBuffer = new AxisStops();

    // Buffers used during drawing. These are defined as fields to avoid allocation during draw calls.
    private      float[] mAxisXPositionsBuffer = new float[]{};
    private      float[] mAxisYPositionsBuffer = new float[]{};
    private      float[] mAxisXLinesBuffer     = new float[]{};
    private      float[] mAxisYLinesBuffer     = new float[]{};

    // The number of individual points (samples) in the chart series to draw onscreen.
    private static final int DRAW_STEPS = 30;
    private final float[] mSeriesLinesBuffer    = new float[(DRAW_STEPS + 1) * 4];

    // }}}
    // formatFloat {{{
    /**
     * Formats a float value to the given number of decimals. Returns the length of the string.
     * The string begins at out.length - [return value].
     */
    private static final int POW10[] = {1, 10, 100, 1000, 10000, 100000, 1000000};

    private static int formatFloat(final char[] out, float val, int digits)
    {
        boolean negative = false;
        if (val == 0) {
            out[out.length - 1] = '0';
            return 1;
        }
        if (val < 0) {
            negative = true;
            val = -val;
        }
        if (digits > POW10.length) {
            digits = POW10.length - 1;
        }
        val *= POW10[digits];
        long lval = Math.round(val);
        int index = out.length - 1;
        int charCount = 0;
        while (lval != 0 || charCount < (digits + 1)) {
            int digit = (int) (lval % 10);
            lval = lval / 10;
            out[index--] = (char) (digit + '0');
            charCount++;
            if (charCount == digits) {
                out[index--] = '.';
                charCount++;
            }
        }
        if (negative) {
            out[index--] = '-';
            charCount++;
        }
        return charCount;
    }
    //}}}
    // drawAxes {{{
    // Draws the chart axes and labels onto the canvas.
    private void drawAxes(Canvas canvas)
    {
        if(mCurrentViewport == null)
            mCurrentViewport = new RectF(AXIS_X_MIN, AXIS_Y_MIN, AXIS_X_MAX, AXIS_Y_MAX);

        // Computes axis stops (in terms of numerical value and position on screen)
        int i;

        computeAxisStops( mCurrentViewport.left
                ,         mCurrentViewport.right
                ,         mContentRect.width() / mMaxLabelWidth / 2
                ,         mXStopsBuffer);

        computeAxisStops( mCurrentViewport.top
                ,         mCurrentViewport.bottom
                ,         mContentRect.height() / mLabelHeight / 2
                ,         mYStopsBuffer);

        // Avoid unnecessary allocations during drawing. Re-use allocated
        // arrays and only reallocate if the number of stops grows.
        if (mAxisXPositionsBuffer.length < mXStopsBuffer.numStops) {
            mAxisXPositionsBuffer = new float[mXStopsBuffer.numStops];
        }
        if (mAxisYPositionsBuffer.length < mYStopsBuffer.numStops) {
            mAxisYPositionsBuffer = new float[mYStopsBuffer.numStops];
        }
        if (mAxisXLinesBuffer.length < mXStopsBuffer.numStops * 4) {
            mAxisXLinesBuffer = new float[mXStopsBuffer.numStops * 4];
        }
        if (mAxisYLinesBuffer.length < mYStopsBuffer.numStops * 4) {
            mAxisYLinesBuffer = new float[mYStopsBuffer.numStops * 4];
        }

        // Compute positions
        for (i = 0; i < mXStopsBuffer.numStops; i++) {
            mAxisXPositionsBuffer[i] = getDrawX(mXStopsBuffer.stops[i]);
        }
        for (i = 0; i < mYStopsBuffer.numStops; i++) {
            mAxisYPositionsBuffer[i] = getDrawY(mYStopsBuffer.stops[i]);
        }

        // Draws grid lines using drawLines (faster than individual drawLine calls)
        for (i = 0; i < mXStopsBuffer.numStops; i++) {
            mAxisXLinesBuffer[i * 4    ] = (float) Math.floor(mAxisXPositionsBuffer[i]);
            mAxisXLinesBuffer[i * 4 + 1] = mContentRect.top;
            mAxisXLinesBuffer[i * 4 + 2] = (float) Math.floor(mAxisXPositionsBuffer[i]);
            mAxisXLinesBuffer[i * 4 + 3] = mContentRect.bottom;
        }
        canvas.drawLines(mAxisXLinesBuffer, 0, mXStopsBuffer.numStops * 4, mGridPaint);

        for (i = 0; i < mYStopsBuffer.numStops; i++) {
            mAxisYLinesBuffer[i * 4    ] = mContentRect.left;
            mAxisYLinesBuffer[i * 4 + 1] = (float) Math.floor(mAxisYPositionsBuffer[i]);
            mAxisYLinesBuffer[i * 4 + 2] = mContentRect.right;
            mAxisYLinesBuffer[i * 4 + 3] = (float) Math.floor(mAxisYPositionsBuffer[i]);
        }
        canvas.drawLines(mAxisYLinesBuffer, 0, mYStopsBuffer.numStops * 4, mGridPaint);

        // [0,0]
        float x_zero = getDrawX(0F);
        float y_zero = getDrawY(0F);
        canvas.drawLine(mContentRect.left,           y_zero, mContentRect.right,              y_zero, mZeroPaint); // RED
        canvas.drawLine(           x_zero, mContentRect.top,             x_zero, mContentRect.bottom, mZeroPaint); // RED
        //                         startX,           startY,              stopX,               stopY,      paint

        // Draws X labels
        int labelOffset;
        int labelLength;
        mLabelTextPaint.setTextAlign(Paint.Align.CENTER);
        for (i = 0; i < mXStopsBuffer.numStops; i++) {
            // Do not use String.format in high-performance code such as onDraw code.
            labelLength = formatFloat(mLabelBuffer, mXStopsBuffer.stops[i], mXStopsBuffer.decimals);
            labelOffset = mLabelBuffer.length - labelLength;
            canvas.drawText(
                    mLabelBuffer, labelOffset, labelLength,
                    mAxisXPositionsBuffer[i],
                    mContentRect.bottom + mLabelHeight + mLabelSeparation,
                    mLabelTextPaint);
        }

        // Draws Y labels
        mLabelTextPaint.setTextAlign(Paint.Align.RIGHT);
        for (i = 0; i < mYStopsBuffer.numStops; i++) {
            // Do not use String.format in high-performance code such as onDraw code.
            labelLength = formatFloat(mLabelBuffer, mYStopsBuffer.stops[i], mYStopsBuffer.decimals);
            labelOffset = mLabelBuffer.length - labelLength;
            canvas.drawText(
                    mLabelBuffer, labelOffset, labelLength,
                    mContentRect.left - mLabelSeparation,
                    mAxisYPositionsBuffer[i] + mLabelHeight / 2,
                    mLabelTextPaint);
        }
    }
    //}}}
    //}}}
    // drawEdgeEffectsUnclipped {{{
    /**
     * Draws the overscroll "glow" at the four edges of the chart region, if necessary. The edges
     * of the chart region are stored in {@link #mContentRect}.
     *
     * @see EdgeEffectCompat
     */
    @SuppressWarnings("JavadocReference")
    private void drawEdgeEffectsUnclipped(Canvas canvas)
    {
        // The methods below rotate and translate the canvas as needed before drawing the glow,
        // since EdgeEffectCompat always draws a top-glow at 0,0.

        boolean needsInvalidate = false;

        if (!mEdgeEffectTop.isFinished())
        {
            final int restoreCount = canvas.save();
            canvas.translate(mContentRect.left, mContentRect.top);
            mEdgeEffectTop.setSize(mContentRect.width(), mContentRect.height());
            if (mEdgeEffectTop.draw(canvas))
            {
                needsInvalidate = true;
            }
            canvas.restoreToCount(restoreCount);
        }

        if (!mEdgeEffectBottom.isFinished())
        {
            final int restoreCount = canvas.save();
            canvas.translate(2 * mContentRect.left - mContentRect.right, mContentRect.bottom);
            canvas.rotate(180, mContentRect.width(), 0);
            mEdgeEffectBottom.setSize(mContentRect.width(), mContentRect.height());
            if (mEdgeEffectBottom.draw(canvas))
            {
                needsInvalidate = true;
            }
            canvas.restoreToCount(restoreCount);
        }

        if (!mEdgeEffectLeft.isFinished())
        {
            final int restoreCount = canvas.save();
            canvas.translate(mContentRect.left, mContentRect.bottom);
            canvas.rotate(-90, 0, 0);
            mEdgeEffectLeft.setSize(mContentRect.height(), mContentRect.width());
            if (mEdgeEffectLeft.draw(canvas))
            {
                needsInvalidate = true;
            }
            canvas.restoreToCount(restoreCount);
        }

        if (!mEdgeEffectRight.isFinished())
        {
            final int restoreCount = canvas.save();
            canvas.translate(mContentRect.right, mContentRect.top);
            canvas.rotate(90, 0, 0);
            mEdgeEffectRight.setSize(mContentRect.height(), mContentRect.width());
            if (mEdgeEffectRight.draw(canvas))
            {
                needsInvalidate = true;
            }
            canvas.restoreToCount(restoreCount);
        }

        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
    //}}}
    // drawDataSeriesUnclipped {{{
    /**
     * Draws the currently visible portion of the data series defined by fun(float) to the canvas.
     * This method does not clip its drawing, so users should call Canvas#clipRect before calling this method.
     */
    private void drawDataSeriesUnclipped(Canvas canvas)
    {
        mSeriesLinesBuffer[0] = mContentRect.left;
        mSeriesLinesBuffer[1] = getDrawY(fun(mCurrentViewport.left));
        mSeriesLinesBuffer[2] = mSeriesLinesBuffer[0];
        mSeriesLinesBuffer[3] = mSeriesLinesBuffer[1];
        float x;
        for (int i = 1; i <= DRAW_STEPS; i++)
        {
            mSeriesLinesBuffer[i * 4    ] = mSeriesLinesBuffer[(i - 1) * 4 + 2];
            mSeriesLinesBuffer[i * 4 + 1] = mSeriesLinesBuffer[(i - 1) * 4 + 3];

            x = (mCurrentViewport.left + (mCurrentViewport.width() / DRAW_STEPS * i));
            mSeriesLinesBuffer[i * 4 + 2] = getDrawX(x);
            mSeriesLinesBuffer[i * 4 + 3] = getDrawY(fun(x));
        }
        canvas.drawLines(mSeriesLinesBuffer, mDataPaint);
    }

    private static float fun(float x) {
        return (float) Math.pow(x, 3) - x / 4;
    }
    //}}}

    // computeAxisStops {{{
    /**
     * Computes the set of axis labels to show given start and stop boundaries and an ideal number
     * of stops between these boundaries.
     *
     * @param start The minimum extreme (e.g. the  left edge) for the axis.
     * @param stop  The maximum extreme (e.g. the right edge) for the axis.
     * @param steps The     ideal number of stops to create.
     * This should be based on available screen space; the more space there is, the more stops should be shown.
     * @param outStops The destination {@link AxisStops} object to populate.
     */
    private static void computeAxisStops(float start, float stop, int steps, AxisStops outStops)
    {
        double range = stop - start;
        if (steps == 0 || range <= 0) {
            outStops.stops = new float[]{};
            outStops.numStops = 0;
            return;
        }

        double rawInterval = range / steps;
        double interval = roundToOneSignificantFigure(rawInterval);
        double intervalMagnitude = Math.pow(10, (int) Math.log10(interval));
        int intervalSigDigit = (int) (interval / intervalMagnitude);
        if (intervalSigDigit > 5) {
            // Use one order of magnitude higher, to avoid intervals like 0.9 or 90
            interval = Math.floor(10 * intervalMagnitude);
        }

        double first = Math.ceil(start / interval) * interval;
        double last = Math.nextUp(Math.floor(stop / interval) * interval);

        double f;
        int i;
        int n = 0;
        for (f = first; f <= last; f += interval) {
            ++n;
        }

        outStops.numStops = n;

        if (outStops.stops.length < n) {
            // Ensure stops contains at least numStops elements.
            outStops.stops = new float[n];
        }

        for (f = first, i = 0; i < n; f += interval, ++i) {
            outStops.stops[i] = (float) f;
        }

        if (interval < 1) {
            outStops.decimals = (int) Math.ceil(-Math.log10(interval));
        } else {
            outStops.decimals = 0;
        }
    }
    //}}}
    // roundToOneSignificantFigure {{{
    /**
     * Rounds the given number to the given number of significant digits.
     * Based on an answer on Stack Overflow.
     */
    private static float roundToOneSignificantFigure(double num)
    {
        final float         d = (float) Math.ceil((float) Math.log10(num < 0 ? -num : num));
        final int       power = 1 - (int) d;
        final float magnitude = (float) Math.pow(10, power);
        final long    shifted = Math.round(num * magnitude);
        return shifted / magnitude;
    }
    //}}}
    // }}}
    // onMeasure {{{
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
if(work_as_a_container) { super.onMeasure(widthMeasureSpec, heightMeasureSpec); return; } // XXX
        int minChartSize = 100;//getResources().getDimensionPixelSize(R.dimen.min_chart_size);
        setMeasuredDimension(
                Math.max  ( getSuggestedMinimumWidth()
                    ,       resolveSize(minChartSize + getPaddingLeft() + mMaxLabelWidth + mLabelSeparation + getPaddingRight() , widthMeasureSpec))
                , Math.max( getSuggestedMinimumHeight()
                    ,       resolveSize(minChartSize +  getPaddingTop() +   mLabelHeight + mLabelSeparation + getPaddingBottom(), heightMeasureSpec))
                );
report("onMeasure");
    }
    //}}}
    // init_model {{{

    private int   mLabelSeparation;

    private int   mLabelTextColor;
    private int   mAxisColor;
    private int   mZeroColor;
    private int   mDataColor;
    private int   mGridColor;

    private float mAxisThickness;
    private float mZeroThickness;
    private float mDataThickness;
    private float mGridThickness;
    private float mLabelTextSize;

    private void init_model(Context context, AttributeSet attrs, int defStyle)
    {
        //TypedArray a            = context.getTheme().obtainStyledAttributes( attrs, R.styleable.GraphView, defStyle, defStyle);
        //try {
            mLabelTextColor     = Color.WHITE;//a.getColor             ( R.styleable.GraphView, mLabelTextColor);
            mLabelTextSize      = 32;//a.getDimension         ( R.styleable.GraphView, mLabelTextSize);
            mLabelSeparation    = 10;//a.getDimensionPixelSize( R.styleable.GraphView, mLabelSeparation);

            mGridThickness      = 1;//a.getDimension         ( R.styleable.GraphView, mGridThickness);
            mGridColor          = Color.LTGRAY;//a.getColor             ( R.styleable.GraphView, mGridColor);

            mAxisThickness      = 2;//a.getDimension         ( R.styleable.GraphView, mAxisThickness);
            mAxisColor          = Color.RED;//a.getColor             ( R.styleable.GraphView, mAxisColor);

            mZeroThickness      = 3;
            mZeroColor          = Color.YELLOW;

            mDataThickness      = 8;//a.getDimension         ( R.styleable.GraphView, mDataThickness);
            mDataColor          = Color.BLUE;//a.getColor             ( R.styleable.GraphView, mDataColor);
        //}
        //finally {
            //a.recycle();
        //}

        initPaints();
    }
    //}}}
    // initPaints {{{
    private Paint mAxisPaint;
    private Paint mZeroPaint;
    private Paint mDataPaint;
    private Paint mGridPaint;
    private Paint mLabelTextPaint;

    private int   mMaxLabelWidth;
    private int   mLabelHeight;

    // (Re)initializes {@link Paint} objects based on current attribute values.
    private void initPaints()
    {
        mLabelTextPaint = new Paint();
        mLabelTextPaint.setAntiAlias(true);
        mLabelTextPaint.setTextSize(mLabelTextSize);
        mLabelTextPaint.setColor(mLabelTextColor);

    //  mLabelHeight   = (int)Math.abs(mLabelTextPaint.getFontMetrics().top);
    //  mMaxLabelWidth = (int)mLabelTextPaint.measureText("0000");
        mLabelHeight   = Settings.DISPLAY_H / 20;
        mMaxLabelWidth = Settings.DISPLAY_W / 20;

        mGridPaint = new Paint();
        mGridPaint.setStrokeWidth(mGridThickness);
        mGridPaint.setColor(mGridColor);
        mGridPaint.setStyle(Paint.Style.STROKE);

        mAxisPaint = new Paint();
        mAxisPaint.setStrokeWidth(mAxisThickness);
        mAxisPaint.setColor(mAxisColor);
        mAxisPaint.setStyle(Paint.Style.STROKE);

        mZeroPaint = new Paint();
        mZeroPaint.setStrokeWidth(mZeroThickness);
        mZeroPaint.setColor(mZeroColor);
        mZeroPaint.setStyle(Paint.Style.STROKE);

        mDataPaint = new Paint();
        mDataPaint.setStrokeWidth(mDataThickness);
        mDataPaint.setColor(mDataColor);
        mDataPaint.setStyle(Paint.Style.STROKE);
        mDataPaint.setAntiAlias(true);
    }
    //}}}
    // Attributes {{{
    // getLabelTextSize {{{
    public float getLabelTextSize()
    {
        return mLabelTextSize;
    }
    //}}}
    // setLabelTextSize {{{
    public void setLabelTextSize(float labelTextSize)
    {
        mLabelTextSize = labelTextSize;
        initPaints();
        ViewCompat.postInvalidateOnAnimation(this);
    }
    //}}}
    // getLabelTextColor {{{
    public int getLabelTextColor()
    {
        return mLabelTextColor;
    }
    //}}}
    // setLabelTextColor {{{
    public void setLabelTextColor(int labelTextColor)
    {
        mLabelTextColor = labelTextColor;
        initPaints();
        ViewCompat.postInvalidateOnAnimation(this);
    }
    //}}}
    // getGridThickness {{{
    public float getGridThickness()
    {
        return mGridThickness;
    }
    //}}}
    // setGridThickness {{{
    public void setGridThickness(float gridThickness)
    {
        mGridThickness = gridThickness;
        initPaints();
        ViewCompat.postInvalidateOnAnimation(this);
    }
    //}}}
    // getGridColor {{{
    public int getGridColor()
    {
        return mGridColor;
    }
    //}}}
    // setGridColor {{{
    public void setGridColor(int gridColor)
    {
        mGridColor = gridColor;
        initPaints();
        ViewCompat.postInvalidateOnAnimation(this);
    }
    //}}}
    // getAxisThickness {{{
    public float getAxisThickness()
    {
        return mAxisThickness;
    }
    //}}}
    // setAxisThickness {{{
    public void setAxisThickness(float axisThickness)
    {
        mAxisThickness = axisThickness;
        initPaints();
        ViewCompat.postInvalidateOnAnimation(this);
    }
    //}}}
    // getAxisColor {{{
    public int getAxisColor()
    {
        return mAxisColor;
    }
    //}}}
    // setAxisColor {{{
    public void setAxisColor(int axisColor)
    {
        mAxisColor = axisColor;
        initPaints();
        ViewCompat.postInvalidateOnAnimation(this);
    }
    //}}}
    // getDataThickness {{{
    public float getDataThickness()
    {
        return mDataThickness;
    }
    //}}}
    // setDataThickness {{{
    public void setDataThickness(float dataThickness)
    {
        mDataThickness = dataThickness;
    }
    //}}}
    // getDataColor {{{
    public int getDataColor()
    {
        return mDataColor;
    }
    //}}}
    // setDataColor {{{
    public void setDataColor(int dataColor)
    {
        mDataColor = dataColor;
    }
    //}}}
    // }}}
    // SavedState {{{
    // onSaveInstanceState {{{
    @Override
    public Parcelable onSaveInstanceState()
    {
        Parcelable superState = super.onSaveInstanceState();
        SavedState         ss = new SavedState( superState );
        ss.viewport           = mCurrentViewport;
        return ss;
    }
    //}}}
    // onRestoreInstanceState {{{
    @Override
    public void onRestoreInstanceState(Parcelable state)
    {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        mCurrentViewport = ss.viewport;
    }
    //}}}
    // SavedState {{{
    /**
     * Persistent state that is saved by GraphView.
     */
    public static class SavedState extends BaseSavedState
    {
        private RectF viewport;
        // SavedState {{{
        public SavedState(Parcelable superState)
        {
            super(superState);
        }
        //}}}
        // writeToParcel {{{
        @Override public void writeToParcel(Parcel out, int flags)
        {
            super.writeToParcel(out, flags);
            out.writeFloat(viewport.left);
            out.writeFloat(viewport.top);
            out.writeFloat(viewport.right);
            out.writeFloat(viewport.bottom);
        }
        //}}}
        // toString {{{
        @Override public String toString()
        {
            return "GraphView.SavedState{"
                + Integer.toHexString(System.identityHashCode(this))
                + " viewport=" + viewport.toString() + "}";
        }
        //}}}
//        // newCreator {{{
//        public static final Parcelable.Creator<SavedState> CREATOR
//            = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
//                @Override public SavedState createFromParcel(Parcel in, ClassLoader loader) { return new SavedState( in ); }
//                @Override public SavedState[]       newArray(int size)                      { return new SavedState[size]; }
//            });
//        //}}}

        // SavedState {{{
        SavedState(Parcel in)
        {
            super(in);
            viewport = new RectF(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat());
        }
        //}}}
    }
    // }}}
    // AxisStops {{{
    /**
     * A simple class representing axis label values.
     *
     * @see #computeAxisStops
     */
    private static class AxisStops
    {
        float[] stops = new float[]{};
        int numStops;
        int decimals;
    }
    // }}}
    // }}}

    // LOG
    // report {{{
    private float mCurrentViewport_left;
    private float mCurrentViewport_top;
    private float mCurrentViewport_width;
    private float mCurrentViewport_height;
    private int   mContentRect_left;
    private int   mContentRect_top;
    private int   mContentRect_width;
    private int   mContentRect_height;

    private void report(String caller)
    {
        //if(work_as_a_container) ((RelativeLayout)this).requestLayout();

        if(        (mCurrentViewport.left     == mCurrentViewport_left  )
                && (mCurrentViewport.top      == mCurrentViewport_top   )
                && (mCurrentViewport.width()  == mCurrentViewport_width )
                && (mCurrentViewport.height() == mCurrentViewport_height)
                && (mContentRect.left         == mContentRect_left      )
                && (mContentRect.top          == mContentRect_top       )
                && (mContentRect.width()      == mContentRect_width     )
                && (mContentRect.height()     == mContentRect_height    )
          )
            return;

        mCurrentViewport_left   = mCurrentViewport.left;
        mCurrentViewport_top    = mCurrentViewport.top;
        mCurrentViewport_width  = mCurrentViewport.width();
        mCurrentViewport_height = mCurrentViewport.height();
        mContentRect_left       = mContentRect.left;
        mContentRect_top        = mContentRect.top;
        mContentRect_width      = mContentRect.width();
        mContentRect_height     = mContentRect.height();
/*
        System.err.println(
                String.format("XXX %45s: VIEWPORT=[%.2f %.2f] [%.2f %.2f] CONTENT=[%4d %4d] [%4d %4d]"
                    , caller
                    , mCurrentViewport_left  , mCurrentViewport_top
                    , mCurrentViewport_width , mCurrentViewport_height
                    , mContentRect_left      , mContentRect_top
                    , mContentRect_width     , mContentRect_height
                    ));

*/
    }
    //}}}

/*
    // MENU ZOOM PAN
    //{{{
    // {{{
    // Initial fling velocity for pan operations, in screen widths (or heights) per second.
    private static final float PAN_VELOCITY_FACTOR = 2f;
    // }}}
    // zoomIn {{{
    // Smoothly zooms the chart in one step.
    public void zoomIn()
    {
    mScrollerStartViewport.set(mCurrentViewport);
    mZoomer.forceFinished(true);
    mZoomer.startZoom(ZOOM_AMOUNT);
    mZoomFocalPoint.set(
    (mCurrentViewport.right + mCurrentViewport.left) / 2,
    (mCurrentViewport.bottom + mCurrentViewport.top) / 2);
    ViewCompat.postInvalidateOnAnimation(this);
    }
    //}}}
    // zoomOut {{{
    // Smoothly zooms the chart out one step.
    public void zoomOut()
    {
    mScrollerStartViewport.set(mCurrentViewport);
    mZoomer.forceFinished(true);
    mZoomer.startZoom(-ZOOM_AMOUNT);
    mZoomFocalPoint.set(
    (mCurrentViewport.right + mCurrentViewport.left) / 2,
    (mCurrentViewport.bottom + mCurrentViewport.top) / 2);
    ViewCompat.postInvalidateOnAnimation(this);
    }
    //}}}
    // panLeft {{{
    // Smoothly pans the chart left one step.
    public void panLeft()
    {
    fling((int) (-PAN_VELOCITY_FACTOR * getWidth()), 0);
    }
    //}}}
    // panRight {{{
    // Smoothly pans the chart right one step.
    public void panRight()
    {
    fling((int) (PAN_VELOCITY_FACTOR * getWidth()), 0);
    }
    //}}}
    // panUp {{{
    // Smoothly pans the chart up one step.
    public void panUp()
    {
    fling(0, (int) (-PAN_VELOCITY_FACTOR * getHeight()));
    }
    //}}}
    // panDown {{{
    // Smoothly pans the chart down one step.
    public void panDown()
    {
    fling(0, (int) (PAN_VELOCITY_FACTOR * getHeight()));
    }
    //}}}
    // }}}
*/

}

