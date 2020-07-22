package ivanwfr.rtabs; // {{{

import android.graphics.Point;
import android.graphics.Rect;

import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

//import android.support.v4.view.VelocityTrackerCompat;

// }}}
// COMMENT {{{

/** Drag touched tab on ACTION_DOWN with inertia and an optional grid-magnetism.
 *
 *  1. STOP current moving_np.
 *  2. DETECT NotePane at xy.
 *  3. Init BOUNCE-FRAME and left margin.
 *  5. DRAG touched tab to finger xy.
 *  1. Magnetize on GRID (optional).
 *  7. Terminate with inertia on ACTION_UP .. f(Velocity > INERTIA_VELOCITY_MIN).
 *  8. CLAMP_NP_IN_PLACE when done .. (update MODEL-OBJECT attributes).
 *  9. Calls clampedListener.handle_not_moved on ACTION_UP.
 * 10. calls ClampListener.onClamped method when all done .. (call MODEL callback).
 *
 */

// }}}
public class Clamp
{
    // {{{
    public static        String CLAMP_JAVA_TAG = "CLAMP (180511:14h)";
    private static       String   TAG_CLAMP           = Settings.TAG_CLAMP;

    private static final float    DEFAULT_FLING_DX_DY =   80f;

    public               NotePane moving_np           = null;   // TODO public->private
    private              float    b_h                 = 0;
    private              float    b_w                 = 0;
    public               int      margin_left         = 0;      // TODO public->private

    private Settings.ClampListener clampedListener    = null; // to report to about:
    //....................................................... // - [clamp3_handle_not_moved] or [clamp6_run_move_inertia]
    //....................................................... // - [clamp1_has_a_grid_for]
    //....................................................... // - [clamp10_onClamped]
    //....................................................... // - [clamp2_get_np_to_grid_xy]
    //....................................................... // - [clamp5_get_gravityPoint] .. (clamp6_run_move_inertia) .. (direction_changed)
    //....................................................... // - [clamp7_is_dragging_something] [clamp8_drag]
    //....................................................... // - [clamp9_bounced]
    private                boolean clampedListener_is_dragging_something = false;

    // catching
    private              float   view_to_finger_x = 0f; // from where the view was at that time to touch-point (finger will drag this point)
    private              float   view_to_finger_y = 0f; // from where the view was at that time to touch-point (finger will drag this point)
    private              float   offset_fx        = 0f; // kick view_to_finger_x away from touch point
    private              float   offset_fy        = 0f; // kick view_to_finger_x away from touch point
    private              boolean has_a_grid       = false;
    private              float   grid_w           = 0f;
    private              float   grid_h           = 0f;
    private              float   grid_s           = 1f;

    // tracking
    private      VelocityTracker mVelocityTracker = null;
    private        final float[] dx_dy            = new float[2]; // last move delta xy
    private        final float[] px_py            = new float[2]; // prev move delta xy
    private              float   last_x           = 0f;           // tracking each event dx
    private              float   last_y           = 0f;           // tracking each event dy
    private              boolean has_moved;

    // friction
    private static final   float DEFAULT_FRICTION = 0.02f;
    private static final   float GRAVITY_FRICTION = 0.05f;//0.05f;
    private                float  friction_fx     = 0;

    // inertia
    private static final   float INERTIA_VELOCITY_MIN = 2.00f;

    // bounce fx
    private static final   float DEFAULT_BOUNCE_FX = 0.20f;
    private static final   float LOW_BOUNCE_FX     = 0.95f;
    private                float  bounce_fx        = 0;
    private                float  bounce_x_min     = 0;
    private                float  bounce_x_max     = 0;
    private                float  bounce_y_min     = 0;
    private                float  bounce_y_max     = 0;
    private              boolean  has_bounced      = false;

    // bounce direction
    private static final   int BOUNCED_LEFT   = 1;
    private static final   int BOUNCED_RIGHT  = 2;
    private static final   int BOUNCED_TOP    = 3;
    private static final   int BOUNCED_BOTTOM = 4;
    private                int  prev_bounced_x;
    private                int  prev_bounced_y;

    // gravity point
    private static final int   GRAVITY_NONE   =   -1;
    private              Point  gravityPoint  = null;

    // gravity fx
    public  static final float LOW_GRAVITY_FX       = 5000f;
    public  static final float LIGHT_GRAVITY_FX     = 1000f;
    public  static final float DEFAULT_GRAVITY_FX   =  500f; // tic factor towards gravity point (the distance fraction)
    public  static final float MUST_FALL_GRAVITY_FX =  250f;
    public  static final float MAX_GRAVITY_FX       =   10f;
    private              float  gravity_fx          = DEFAULT_GRAVITY_FX;

    // }}}
    // Clamp {{{
    public Clamp(Settings.ClampListener l)
    {
        clampedListener = l;
        if(clampedListener != null)
            clampedListener_is_dragging_something = clampedListener.clamp7_is_dragging_something();
    }
    //}}}
    // toString {{{
    public String toString()
    {
        View   view  = NotePane.Get_view( moving_np );
        String view_to_string
            = (view == null)
            ?                "null"
            :  String.format("xy=[%4d %4d] wh=[%4d %4d]", (int)view.getX(), (int)view.getY(), view.getWidth(), view.getHeight());

        return "@ CLAMP @@@\n"
            +  "@ CLAMP [ Clamp.moving_np=["+ moving_np                         +"]\n"
            +  "@ CLAMP [ LOOPING........=["+ (move_inertia_timerTask != null)  +"]\n"
            +  "@ CLAMP [ ..........dx_dy=["+ dx_dy[0]+" "+dx_dy[1]             +"]\n"
            +  "@ CLAMP [ ...gravityPoint=["+ ((gravityPoint==null) ? "null" : gravityPoint.x+" "+gravityPoint.y) +"]\n"
            +  "@ CLAMP [ ....friction_fx=["+ friction_fx                       +"]\n"
            +  "@ CLAMP [ ......bounce_fx=["+ bounce_fx                         +"]\n"
            +  "@ CLAMP [ .....gravity_fx=["+ gravity_fx                        +"]\n"
            +  "@ CLAMP [ ...........view=["+ view_to_string                    +"]\n"
            +  "@ CLAMP @@@"
            ;
    }
    //}}}
    /** EVENTS */
    //{{{
    // onTouchEvent  {{{
    public boolean onTouchEvent(MotionEvent event) {
        return     onTouchEvent(  moving_np,             event,            0f,            0f);
    }
    public boolean onTouchEvent(NotePane np, MotionEvent event) {
        return     onTouchEvent(         np,             event,            0f,            0f);
    }
    public boolean onTouchEvent(NotePane np, MotionEvent event, float scrollX, float scrollY)
    {
        // x y action {{{
        String caller = "onTouchEvent";

//*CLAMP*/caller +="."+Settings.Get_action_name(event);//TAG_CLAMP
        float x =           (int)event.getRawX();
        float y =           (int)event.getRawY();

//*CLAMP*/Settings.MOM(TAG_CLAMP, caller+": x@y=["+ x +"@"+ y +"]");
//System.err.println(caller+": moving_np=["+moving_np+"]");
        // }}}
        switch( event.getActionMasked() ) {
            case MotionEvent.ACTION_DOWN  : // {{{
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller);
                // CATCH [new np] {{{
                if( !_catch_moving_np(np, event, x, y, 0, 0, caller) )
                    break;

                //}}}
                break;
                // }}}
            case MotionEvent.ACTION_MOVE  : // {{{
if(moving_np == null) Settings.MOC(TAG_CLAMP, caller, "CALLED WHILE (moving_np == null):\n"+this.toString());
                // IGNORE .. f(move_inertia_timerTask) {{{
                if(move_inertia_timerTask != null)
                    break;

                //}}}
                // ?(moving_np view still there) {{{
                if(moving_np == null)    break;
                View    view  = NotePane.Get_view( moving_np );
                if(     view == null)    break;
                //}}}
                // TRACK {{{

                // cumulate move history
                mVelocityTracker.addMovement( event );

                // move to be reported when released
                has_moved = true;

                // keep same offset to top-left corner while moving finger
                x -= view_to_finger_x; // view x
                y -= view_to_finger_y; // view y

                // eval this move delta
                dx_dy[0] = x - last_x;
                dx_dy[1] = y - last_y;

                // save xy as the next move start point
                last_x   = x;
                last_y   = y;

                //}}}
                // MAGNETIZE ON GRID {{{
                if( has_a_grid )
                {
                    int dxa = (int)Math.abs( dx_dy[0] );
                    int dya = (int)Math.abs( dx_dy[1] );

                    // MAGNETIZE SMALL MOVES ONLY
                    if((dxa < 10) && (dya < 10)) // XXX MAGIC NUMBER! XXX
                    {
                        float gx =      ( x - margin_left) / grid_s ;              // [x - margin] unscaled
                        float gy =      ( y              ) / grid_s ;              // [y - margin] unscaled

                        gx       = (int)(gx + grid_w/2   ) / (int)grid_w;               // [x modulo grid]
                        gy       = (int)(gy + grid_h/2   ) / (int)grid_h;               // [y modulo grid]

                        gx       = (int)(gx * grid_w     ) * grid_s + margin_left; // [x rescaled] + margin_left
                        gy       = (int)(gy * grid_h     ) * grid_s ;              // [y rescaled]

                        dx_dy[0] = (gx - view.getX()) / 3; // [x drag] .. (with some lag)
                        dx_dy[1] = (gy - view.getY()) / 3; // [y drag] .. (with some lag)
                    }
                }
                // }}}
                // DRAG POSITION {{{
                _drag_moving_np( view );

                // }}}
                // DRAG LISTENER {{{
                if( clampedListener_is_dragging_something )
                    clampedListener.clamp8_drag(view.getX(), view.getY());

                //}}}
//CLAMP//Settings.MON(TAG_CLAMP, caller, "dx_dy=["+dx_dy[0]+"@"+dx_dy[1]+"]");

                break;
                // }}}
            case MotionEvent.ACTION_UP    : // {{{
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller);
if(moving_np == null) Settings.MOC(TAG_CLAMP, caller, "CALLED WHILE (moving_np == null):\n"+this.toString());
                // IGNORE .. f(move_inertia_timerTask) {{{
                if(move_inertia_timerTask != null)
                    break;
                //}}}
                // VELOCITY {{{
                mVelocityTracker.computeCurrentVelocity(10);

                int index     = event.getActionIndex();
                int pointerId = event.getPointerId(index);

              //dx_dy[0]      = VelocityTrackerCompat.getXVelocity(mVelocityTracker, pointerId);
              //dx_dy[1]      = VelocityTrackerCompat.getYVelocity(mVelocityTracker, pointerId);
                dx_dy[0]      = mVelocityTracker.getXVelocity( pointerId );
                dx_dy[1]      = mVelocityTracker.getYVelocity( pointerId );

                //XXX mVelocityTracker.recycle(); // Return a VelocityTracker object back to be re-used by others.

                // }}}
                // [ has_moved]  // {{{
//*CLAMP*/Settings.MON(TAG_CLAMP, caller, (has_moved ? " has_moved .. dx_dy=["+dx_dy[0]+","+dx_dy[1]+"]" : " has not moved"));
                if( has_moved )
                {
                    // 1/2 [clamp6_run_move_inertia] .. f(INERTIA_VELOCITY_MIN) .. f(MUST_FALL_GRAVITY_FX) {{{
                    if(        (Math.abs(dx_dy[0]) > INERTIA_VELOCITY_MIN) || (Math.abs(dx_dy[1]) > INERTIA_VELOCITY_MIN)
                            || (gravity_fx <= MUST_FALL_GRAVITY_FX)
                      )
                    {
                        clampedListener.clamp6_run_move_inertia(this);
                    }
                    //}}}
                    // 2/2 [clamp_moving_np_in_place] {{{
                    else {
                        clamp_moving_np_in_place(caller);
                    }
                    //}}}
                }
                // }}}
                // [clamp3_handle_not_moved] [moving_np = null] {{{
                else {
                    // call listener
                    if(moving_np != null)
                        clampedListener.clamp3_handle_not_moved();

                    // then de-reference [moving_np]
                    standby(caller);
                }
                // }}}

                break;
                // }}}
            case MotionEvent.ACTION_CANCEL: // {{{
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller);
if(moving_np == null) Settings.MOC(TAG_CLAMP, caller, "CALLED WHILE (moving_np == null):\n"+this.toString());
                // RELEASE [current moving_np] {{{
                if(move_inertia_timerTask != null)
                    release_moving_np();

                //}}}

                break;
                // }}}
        }
        return (moving_np != null); // keep on handling following events for a selected tab
    }
    //}}}
    //}}}
    /** CATCH */
    //{{{
    // catch_moving_np_at_xy {{{
    private boolean _catch_moving_np(NotePane np, MotionEvent event, float x, float y, float w, float h, String caller)
    {
        // RELEASE CURRENT [moving_np] {{{
        caller = caller+"->_catch_moving_np."+ Settings.Get_action_name(event);

        if(move_inertia_timerTask != null)
        {
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller);
            boolean same_np = (np == moving_np);
            release_moving_np();
            if(same_np) {
//*CLAMP*/Settings.MON(TAG_CLAMP, caller, "(same_np)");
                return false;
            }
        }
        //}}}
        // VelocityTracker {{{
        if(mVelocityTracker == null) mVelocityTracker = VelocityTracker.obtain();
        else                         mVelocityTracker.clear();

        if(event != null)
            mVelocityTracker.addMovement( event );
        // }}}
        // CLICK VS. DRAG {{{
        has_moved = false;
        dx_dy[0]  = 0;
        dx_dy[1]  = 0;

        // }}}
        // [moving_np] {{{
        // new moving_np selected (possibly the same ... what to do differently in that case)
        moving_np     = np;
        if(moving_np == null)
        {
//*CLAMP*/Settings.MON(TAG_CLAMP, caller, "(moving_np == null)");
            standby(caller);

            return false;
        }
        //}}}
        // GRID {{{
        float[] grid = new float[3];
        has_a_grid   = (clampedListener == null) ? false : clampedListener.clamp1_has_a_grid_for(moving_np, grid);
        if(has_a_grid) { grid_w = grid[0]; grid_h = grid[1]; grid_s = grid[2]; }
        else           { grid_w =      1 ; grid_h =      1 ; grid_s =      1 ; }

        //}}}
        // DRAG {{{
        clampedListener_is_dragging_something = (clampedListener == null) ? false : clampedListener.clamp7_is_dragging_something();

        //}}}
        // VIEW {{{
        View view  = NotePane.Get_view( moving_np );
        if(  view == null) {
//*CLAMP*/Settings.MON(TAG_CLAMP, caller, "(view == null)");
            return false;
        }
        b_w = (w > 0) ? w : view.getWidth ();
        b_h = (h > 0) ? w : view.getHeight();
      //b_w = view.getRight() - view.getLeft();
      //b_h = view.getBottom() - view.getTop();

        view.bringToFront();

        //}}}
        // MARGIN] PLAYGROUND] {{{
        get_bounce_playground();

        margin_left = Settings.Get_clamp_playground_margin_left();

        //}}}
        // VIEW TO FINGER OFFSET {{{
        // (finger includes parent-chain translation)
        set_fingerXY(x, y);

        //}}}
        // LAST TRACKED POS {{{
        last_x        = x - view_to_finger_x;//+ view.getX();
        last_y        = y - view_to_finger_y;//+ view.getY();

         dx_dy[0]     = x - last_x;
         dx_dy[1]     = y - last_y;

        // }}}
//*CLAMP*/Settings.MOM(TAG_CLAMP, caller+":\n"+this.toString());
        return true;
    }
    //}}}
    // catch_moving_np_at_xy {{{
    public  void catch_moving_np_at_xy(NotePane np, /*MotionEvent   */ float x, float y)
    {
        _catch_moving_np            (         np,              null,       x,       y, 0, 0, "catch_moving_np_at_xy(np,x,y)");
    }
    //}}}
    // catch_moving_np_at_xy {{{
    public  void catch_moving_np_at_xy(NotePane np, /*MotionEvent   */ float x, float y, float w, float h)
    {
        _catch_moving_np            (         np,              null,       x,       y, w, h, "catch_moving_np_at_xy(np,x,y,w,h)");
    }
    //}}}
//  // catch_moving_np     {{{
//  public  void catch_moving_np    (NotePane np, MotionEvent event, float x, float y)
//  {
//      _catch_moving_np            (         np,              null,       x,       y, 0, 0);
//  }
//  //}}}
    //* CATCH (release) */
    // release_moving_np {{{
    private void release_moving_np()
    {
        if(move_inertia_timerTask == null) return;
        String caller = "release_moving_np";
//*CLAMP*/Settings.MOM(TAG_CLAMP, caller+":\n"+this.toString());

        move_inertia_timerTask.cancel();
        move_inertia_timerTask = null;

    //  if(moving_np != null) clamp_moving_np_in_place(caller);
        clamp_done = true; // [STOP] PER-INSTANCE INTER-THREAD-COMM FLAG
        // give time to TimerTask before switching moving_np .. (early switch would mix clamp10_onClamped np)
        try { Thread.sleep( 20 ); } catch(Exception ex) { Settings.log_ex(ex, caller); }
//*CLAMP*/Settings.MON(TAG_CLAMP, caller, "...cancel delay expired");
    }
    //}}}
    //* CATCH (standby) */
    // standby {{{
    public void standby(String caller)
    {
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller);

        moving_np              = null;
        gravityPoint           = null;
        move_inertia_timerTask = null;
    }
    //}}}
    //* CATCH (finger offset) */
    // set_next_catch_offset_fx_fy {{{
    public void set_next_catch_offset_fx_fy(float fx, float fy)
    {
      offset_fx = fx; // .. (changes applies   )
      offset_fy = fy; // .. (at next catch time)
    }
    //}}}
    // set_fingerXY {{{
    public void set_fingerXY(float f_x, float f_y)
    {
        View view  = NotePane.Get_view( moving_np );
        if(  view == null) {
//*CLAMP*/Settings.MON(TAG_CLAMP, "set_fingerXY", "(view == null)");
            return;
        }

        float dx = f_x - view.getX();
        float dy = f_y - view.getY();

        view_to_finger_x = dx;
        view_to_finger_y = dy;

//        // [offset_fx] [offset_fy] {{{
//        if((offset_fx != 0) || (offset_fy != 0))
//        {
//            int[]                xy = new int[2]; view.getLocationOnScreen( xy );
//            int parent_translationX = (int)view.getX() - xy[0];
//            int parent_translationY = (int)view.getY() - xy[1];
//
//            if(offset_fx != 0) view_to_finger_x = offset_fx * b_w - parent_translationX;
//            if(offset_fy != 0) view_to_finger_y = offset_fy * b_h - parent_translationY;
//        }
//        //}}}

        last_x = view.getX();
        last_y = view.getY();
    }
    public int get_fingerX() { View view = NotePane.Get_view( moving_np ); return (view != null) ? (int)(view.getX() + view_to_finger_x) : 0; }
    public int get_fingerY() { View view = NotePane.Get_view( moving_np ); return (view != null) ? (int)(view.getY() + view_to_finger_y) : 0; }
    //}}}
    //* CATCH (bouncing area) */
    // get_bounce_playground {{{
    private void get_bounce_playground()
    {
        // get bouncing frame
        Rect  playground_rect = new Rect(); // bouncing box

        boolean has_bounce_playground = clampedListener.clamp4_get_bounce_playground(moving_np , playground_rect);
        if(has_bounce_playground) {
            bounce_x_min = playground_rect.left  ;
            bounce_y_min = playground_rect.top   ;
            bounce_x_max = playground_rect.right ;
            bounce_y_max = playground_rect.bottom;
        }
    }
    //}}}
    //}}}
    /** CLAMP */
    //{{{
    // clamp_moving_np_in_place {{{
    public void clamp_moving_np_in_place(String caller)
    {
        // CANCEL UNFINISHED ANIMATION .. (freeze drifting to where it is now) {{{
        caller = caller+"->clamp_moving_np_in_place";
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller);

        if(move_inertia_timerTask != null) release_moving_np();
        //}}}
        // if(moving_np == null) .. (return) {{{
//*CLAMP*/Settings.MOM(TAG_CLAMP, "moving_np=["+moving_np+"] ... (return)");
        if(moving_np == null) return;

        //}}}
        // NO ATTACHED LISTENER TO WORK WITH .. (return) {{{
        if(clampedListener == null)
        {
//*CLAMP*/Settings.MOM(TAG_CLAMP, "NO ATTACHED LISTENER TO WORK WITH ... (return)");
            standby(caller);
            return;
        }
        //}}}
        // VIEW IS GONE .. (return) {{{
        View view = NotePane.Get_view( moving_np );
        if(view == null)
        {
//*CLAMP*/Settings.MOC(TAG_CLAMP, "", "VIEW IS GONE ... (return)");
            if( clampedListener.clamp10_onClamped(moving_np) )
            {
                standby(caller);
            }
            return;
        }
        //}}}
        // ALL DONE WHEN NO GRID ALIGNMENT TO PROCESS .. (return) {{{
        if( !has_a_grid )
        {
//*CLAMP*/Settings.MOC(TAG_CLAMP, "", "NO GRID ALIGNMENT TO PROCESS ...NOTIFY LISTENER ... (return)");
            if( clampedListener.clamp10_onClamped(moving_np) )
            {
                standby(caller);
            }
            return;
        }
        //}}}
        // GET THE GRID FROM [clampedListener] ... {{{
        // GET [clamp2_get_np_to_grid_xy] FROM LISTENER {{{
        int[] grid_xy = new int[2];
        clampedListener.clamp2_get_np_to_grid_xy(view, grid_xy);

        //}}}
        // 1/2 MOVE [moving_np's button or textView] TO ITS NEW GRID-ALIGNED POSITION {{{
        if((moving_np.x != grid_xy[0]) || (moving_np.y != grid_xy[1]))
        {
//*CLAMP*/Settings.MOM(TAG_CLAMP, "[HAS MOVED] .. CLAMP TAB ON GRID: ["+grid_xy[0]+" "+grid_xy[1]+"]");
            moving_np.set_xy(grid_xy[0], grid_xy[1]);

            // NOTIFY LISTENER ABOUT A MOVED TAB .. (...some profile to update)
//*CLAMP*/Settings.MOC(TAG_CLAMP, "", "MOVED TAB CLAMPED: ...NOTIFY LISTENER");
            if( clampedListener.clamp10_onClamped(moving_np) )
            {
                standby(caller);
            }
        }
        // }}}
        // 2/2 OR PUT ITS VIEW BACK ON GRID PIXEL {{{
        else {
            float gx = (grid_xy[0] * grid_w) * grid_s + margin_left;
            float gy = (grid_xy[1] * grid_h) * grid_s;

//*CLAMP*/Settings.MOC(TAG_CLAMP, "", "[HAS NOT MOVED] .. VIEW BACK ON GRID: ["+gx+" "+gy+"]");
//*CLAMP*/Settings.MOM(TAG_CLAMP, ".......grid_xy=["+ grid_xy[0] +" "+ grid_xy[1]        +"]");
//*CLAMP*/Settings.MOM(TAG_CLAMP, ".......grid_wh=["+ grid_w     +" "+ grid_h            +"]");
//*CLAMP*/Settings.MOM(TAG_CLAMP, "........grid_s=["+ grid_s                             +"]");
//*CLAMP*/Settings.MOM(TAG_CLAMP, "...margin_left=["+ margin_left                        +"]");
            try {
                view.setX( gx );
                view.setY( gy );
            }
            catch(Exception ex) { Settings.log_ex(ex, caller); }
            standby(caller);
        }
        // }}}
        //}}}
    }
    //}}}
    //}}}
    /** RUN */
    //{{{
    //* SET */
    // set_gravityPoint {{{
    public void set_gravityPoint(float x, float y, String caller)
    {
        caller = caller+"->set_gravityPoint("+x+" , "+y+")";
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller);
        gravityPoint = new Point((int)x, (int)y);
    }
    //}}}
    // set_gravity_fx {{{
    public void set_gravity_fx(float fx)
    {
        gravity_fx = fx;
        gravity_fx = Math.min(gravity_fx, LOW_GRAVITY_FX);
        gravity_fx = Math.max(gravity_fx, MAX_GRAVITY_FX);
    }
    //}}}
    // set_velocity {{{
    public void set_velocity(float velocityX, float velocityY)
    {
        dx_dy[0] = velocityX;
        dx_dy[1] = velocityY;
    }
    //}}}
    //* TASK */
    // move_inertia (TIMER TASK) {{{
    //{{{
    //:!start explorer "https://www.google.fr/search?espv=2&q=android+runOnUiThread+no+display+update&oq=android+runOnUiThread+no+display+update&gs_l=serp.3...967.2059.0.2196.7.7.0.0.0.0.68.345.6.6.0....0...1c.1.64.serp..1.3.170.ykduEAYtfmM"
    //:!start explorer "http://www.techsono.com/consult/update-android-gui-timer/"
    private TimerTask move_inertia_timerTask = null;

    //}}}
    // move_inertia {{{
    public  void move_inertia()
    {
//*CLAMP*/Settings.MOC(TAG_CLAMP, "move_inertia");

        move_inertia_timerTask = new TimerTask() {
            @Override public void run()
            {
                move_inertia_loop();
            }
        };

/*
   // SHOWS NO ANIMATION:
        move_inertia_timerTask = new TimerTask() {
            @Override public void run() {
                RTabs.activity.runOnUiThread( new Runnable() {
                    @Override public void run()
                    {
                        move_inertia_loop();
                    }
                });
            }
        };
*/
        new Timer().schedule(move_inertia_timerTask, 0);
    }
    //}}}
    // is_looping {{{
    public boolean is_looping()
    {
        return (move_inertia_timerTask != null);
    }
    //}}}
    //}}}
    //}}}
    /** INERTIA */
    //{{{
    //* INERTIA (loop) */
    // move_inertia_loop {{{
    // {{{
    boolean clamp_done = false; // PER-INSTANCE INTER-THREAD-COMM FLAG

    //}}}
    private static final int DX_MIN = 16;
    private static final int DY_MIN = 0; // ..(keep around same marker)

    private synchronized void move_inertia_loop()
    {
        // [moving_np] [view] {{{
        String caller = "move_inertia_loop";

//*CLAMP*/Settings.MON(TAG_CLAMP, caller, "dx_dy=["+dx_dy[0]+","+dx_dy[1]+"]");
        if(moving_np == null) return;

        View             view = NotePane.Get_view( moving_np );
        if(view == null) {
//*CLAMP*/Settings.MOM(TAG_CLAMP, "NotePane.Get_view( moving_np ): (view == null)");

        }
        //}}}
        // [gravityPoint] .. (may have been preset at this point) {{{
        boolean gravityPoint_was_fixed = (gravityPoint != null);
        if(gravityPoint == null) {
            gravityPoint = new Point(GRAVITY_NONE, GRAVITY_NONE);      // default
            clampedListener.clamp5_get_gravityPoint(moving_np, gravityPoint); // optionally tuned by ClampListener
        }
        int gravity_x = gravityPoint.x;
        int gravity_y = gravityPoint.y;

        boolean has_gravity_x = (gravity_x     != GRAVITY_NONE );
        boolean has_gravity_y = (gravity_y     != GRAVITY_NONE );
        boolean has_gravity   = (has_gravity_x || has_gravity_y);
        //}}}
        // [dx_dy] {{{
        if((dx_dy[0] == 0f) && (dx_dy[1] == 0f))
        {
//*CLAMP*/Settings.MON(TAG_CLAMP, caller, "called with no move event to get velocity from .. (aim from ["+view.getX()+" "+view.getY()+"] towards gravityPoint):");
//*CLAMP*/Settings.MOM(TAG_CLAMP, "gravity_fx=["+gravity_fx+"]");

            float dx = (gravityPoint.x - view.getX()) / gravity_fx;//MUST_FALL_GRAVITY_FX;
            float dy = (gravityPoint.y - view.getY()) / gravity_fx;//MUST_FALL_GRAVITY_FX;

            int x_sign = (dx >= 0) ? 1 : -1;
            int y_sign = (dy >= 0) ? 1 : -1;
            if(Math.abs((int)dx) < DX_MIN) dx = DX_MIN * -x_sign; // step back for
            if(Math.abs((int)dy) < DY_MIN) dy = DY_MIN * -y_sign; // some momentum

            dx_dy[0] = (int)dx;
            dx_dy[1] = (int)dy;
        }
        //}}}
        // [fling_fx] {{{
        float fling_fx = 1f;
        fling_fx       = (float)Math.sqrt(dx_dy[0]*dx_dy[0] + dx_dy[1]*dx_dy[1]) / DEFAULT_FLING_DX_DY; // fx=(1..4) for fling(30.128)
        fling_fx       = Math.max(fling_fx, 1f);

        //}}}
        // [friction_fx] [bounce_fx] {{{
        if( !has_gravity )
        {
            friction_fx = 1.0f - DEFAULT_FRICTION  / fling_fx; // (0.98 .. 0.995) for fx=(1..4)
            bounce_fx   = 1.0f - DEFAULT_BOUNCE_FX / fling_fx; // (0.50 .. 0.885) for fx=(1..4)
        }
        else {
            friction_fx = 1.0f - GRAVITY_FRICTION  / fling_fx;
            bounce_fx   = 1.0f - LOW_BOUNCE_FX     / fling_fx;
        }
        // avoid bouncy ending
        if(gravity_fx <= MUST_FALL_GRAVITY_FX)
            friction_fx *= (gravity_fx / DEFAULT_GRAVITY_FX); // DEFAULT_GRAVITY_FX = (100%) .. (2%) for MAX_GRAVITY_FX

        //}}}
        // MOVE LOOP {{{
//*CLAMP*/Settings.MOM(TAG_CLAMP, "MOVE LOOP:\n"+ this.toString());
//*CLAMP*/Settings.MOM(TAG_CLAMP, "b_w=["+b_w+"]");
        boolean to_the_right  = (dx_dy[0] >= 0);
        boolean to_the_bottom = (dx_dy[1] >= 0);

        px_py[0]              = 0;//2*dx_dy[0]; // done to when dx_dy and px_py are equal
        px_py[1]              = 0;//2*dx_dy[1]; // meaning previous iteration resulted in not moving anymore (infinite loop catcher)

        float              dx = 0;
        float              dy = 0;

        float          dx_min = view.getWidth () / 4;
        float          dy_min = view.getHeight() / 4;

        clamp_done = false; // [START] PER-INSTANCE INTER-THREAD-COMM FLAG

        while(!clamp_done && (moving_np != null))
        {
            // FRICTION {{{
            dx_dy[0] *= friction_fx;
            dx_dy[1] *= friction_fx;

            //}}}
            // GRAVITY {{{
            dx = 0;
            if( has_gravity_x ) {
                dx = gravity_x - view.getX();
                dx_dy[0] += dx / gravity_fx;

                // augment gravity when below button dimension away from gravityPoint
                if(dx <= b_w) gravity_fx = Math.max(2f, gravity_fx * 0.99f);
            }
            dy = 0;
            if( has_gravity_y ) {
                dy = gravity_y - view.getY();
                dx_dy[1] += dy / gravity_fx;

                // augment gravity when below button dimension away from gravityPoint
                if(dy <= b_h) gravity_fx = Math.max(2f, gravity_fx * 0.99f);
            }
            // }}}
            // MOVE {{{
            _glide_moving_np( view );

            //}}}
            // DRAG {{{
            if( clampedListener_is_dragging_something )
                clampedListener.clamp8_drag(view.getX(), view.getY());

            //}}}
            // NEAREST GRAVITY POINT {{{
            boolean direction_changed = false;
            if(has_gravity && !gravityPoint_was_fixed)
            {
                if((to_the_right  != (dx_dy[0] < 0))) { to_the_right  = !to_the_right ; direction_changed = true; }
                if((to_the_bottom != (dx_dy[1] < 0))) { to_the_bottom = !to_the_bottom; direction_changed = true; }

                if(direction_changed && clampedListener.clamp5_get_gravityPoint(moving_np, gravityPoint))
                {
                    gravity_x = gravityPoint.x;
                    gravity_y = gravityPoint.y;
                }
            }
            //}}}
            // ITERATION {{{
            if(        ((dx_dy[0] == 0f) && (dx_dy[1] == 0f)              ) // not moving anymore
                    && !direction_changed
                    && ((Math.abs(dx) < dx_min) && (Math.abs(dy) < dy_min)) //  no travel distance
            )
                clamp_done = true; // NATURAL DEFAULT END OF LOOP
            else
                try { Thread.sleep( 10 ); } catch(Exception ex) { Settings.log_ex(ex, caller); }

            //}}}
        }

        move_inertia_timerTask = null;
//*CLAMP*/Settings.MON(TAG_CLAMP, caller, "[clamp_done]:\n"+this.toString());
//*CLAMP*/Settings.MOM(TAG_CLAMP, "dx=["+dx+"]");
//*CLAMP*/Settings.MOM(TAG_CLAMP, "dy=["+dy+"]");
        //}}}
        // CLAMP {{{
        clamp_moving_np_in_place(caller);

        //}}}
    }
    //}}}
    //* INERTIA (move) */
    // _move_moving_np {{{
    private void _drag_moving_np (View view                    ) { _move_moving_np(view, false); } // on_inertia
    private void _glide_moving_np(View view                    ) { _move_moving_np(view,  true); } // on_inertia
    private void _move_moving_np (View view, boolean on_inertia)
    {
            // current position {{{
            float b_x = view.getX();
            float b_y = view.getY();

            // }}}
            // current offset {{{
        //  float t_x = view.getTranslationX();
        //  float t_y = view.getTranslationY();

            // }}}
            // ON-INERTIA .. CHECK DONE MOVING {{{
            if(on_inertia)
            {
                // CLEAR BELOW PIXEL MOVES
                if(        (Math.abs(dx_dy[0]) < 0.5F)
                        && (Math.abs(dx_dy[1]) < 0.5F)
                  ) {
                    dx_dy[1] = 0;
                    dx_dy[0] = 0;
                  }
                // CLEAR WHEN NOT MOVING
                if(        (dx_dy[0] == px_py[0])
                        && (dx_dy[1] == px_py[1])
                  ) {
                    dx_dy[1] = 0;
                    dx_dy[0] = 0;
                  }

                // CLAMP WHEN DONE MOVING .. (dx_dy == 0x0)
                if((dx_dy[0] == 0f) && (dx_dy[1] == 0f))
                    return;

                px_py[0] = dx_dy[0];
                px_py[1] = dx_dy[1];
            }
            // }}}
            // MOVE WITHIN LISTENER'S BOUNCE RECTANGLE {{{

            float x   = b_x + dx_dy[0]; // left
            float y   = b_y + dx_dy[1]; // top

            float r   =   x + b_w;      // right
            float b   =   y + b_h;      // bottom

            boolean bouncing_left   = false;
            boolean bouncing_right  = false;
            boolean bouncing_top    = false;
            boolean bouncing_bottom = false;

            if     (x < bounce_x_min) { x =  bounce_x_min      ; bouncing_left   = true; if(on_inertia) dx_dy[0] *= -bounce_fx; } // change direction
            else if(r > bounce_x_max) { x =  bounce_x_max - b_w; bouncing_right  = true; if(on_inertia) dx_dy[0] *= -bounce_fx; } // change direction
            if     (y < bounce_y_min) { y =  bounce_y_min      ; bouncing_top    = true; if(on_inertia) dx_dy[1] *= -bounce_fx; } // change direction
            else if(b > bounce_y_max) { y =  bounce_y_max - b_h; bouncing_bottom = true; if(on_inertia) dx_dy[1] *= -bounce_fx; } // change direction

//System.err.println(String.format("%4f %4f", dx_dy[0], dx_dy[1]));
            try {
                view.setX(x);
                view.setY(y);

                if(bouncing_left   && (prev_bounced_x != BOUNCED_LEFT  )) { prev_bounced_x = BOUNCED_LEFT  ; clampedListener.clamp9_bounced(moving_np); }
                if(bouncing_right  && (prev_bounced_x != BOUNCED_RIGHT )) { prev_bounced_x = BOUNCED_RIGHT ; clampedListener.clamp9_bounced(moving_np); }
                if(bouncing_top    && (prev_bounced_y != BOUNCED_TOP   )) { prev_bounced_y = BOUNCED_TOP   ; clampedListener.clamp9_bounced(moving_np); }
                if(bouncing_bottom && (prev_bounced_y != BOUNCED_BOTTOM)) { prev_bounced_y = BOUNCED_BOTTOM; clampedListener.clamp9_bounced(moving_np); }

                // may bounce again only after a minimal retreat
                if((prev_bounced_x == BOUNCED_LEFT  ) && (x > (bounce_x_min +   b_w))) prev_bounced_x = 0;
                if((prev_bounced_x == BOUNCED_RIGHT ) && (x < (bounce_x_max - 2*b_w))) prev_bounced_x = 0;
                if((prev_bounced_y == BOUNCED_TOP   ) && (y > (bounce_y_min +   b_h))) prev_bounced_y = 0;
                if((prev_bounced_y == BOUNCED_BOTTOM) && (y < (bounce_y_max - 2*b_h))) prev_bounced_y = 0;
            }
            catch(Exception ex) { Settings.log_ex(ex, "Clamp._move_moving_np");
ex.printStackTrace();
                dx_dy[1] = 0;
                dx_dy[0] = 0;
            }

            // }}}
    }
    //}}}
    //}}}
}

/* // {{{

:let @p="CLAMP"

:let @p="\\w\\+"

" activated
  :g/^\/\*p\w*\*\/.*p/t$
" ......... -> COMMENT
  :g/^\/\*p\w*\*\/.*p/s/^/\//
" commented
  :g/^\/\/\*p\w*\*\/.*p/t$
" ..........-> ACTIVATE
  :g/^\/\/\*p\w*\*\/.*p/s/^\///

*/ // }}}

