package ivanwfr.rtabs; // {{{

// COMPAT
import android.support.v4.view.ViewCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

// }}}
class Handle extends RelativeLayout
{
    // MONITOR TAGS Handle
    private static       String TAG_HANDLE     = Settings.TAG_HANDLE;

    // members {{{
    private final boolean ANIM_SUPPORTED      = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2);
    private final boolean ELEVATION_SUPPORTED = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP     );

    private static final long         ACTIVATION_COOL_DOWN_TIME = 500L;//1000L; //250L;
    private static final long           ACTIVATION_INTERVAL_MIN = 100L;
    private static final long                   HANDLE_ANIMTIME = 250L;//500L;
    private static final long           UNDERTAKER_COMMING_TIME = 500L;
    private static final long           UNDERTAKER_LEAVING_TIME = 50L;//100L;
    public  static final long                PARK_HANDLES_DELAY = UNDERTAKER_COMMING_TIME + UNDERTAKER_LEAVING_TIME;
    public  static final long              ADJUST_HANDLES_DELAY = 300L;

    public  static final int                      STANDBY_WIDTH = 25;//8;
    public  static final int                       TUCKED_WIDTH = STANDBY_WIDTH / 5;
    public  static final int                    DOCK_HIDE_WIDTH = STANDBY_WIDTH;

//  public  static final int                       DOCKED_WIDTH = 40;//25;

    private static final Handler          mHandler              = new Handler();
    private static final List<Handle>    instanceList           = new ArrayList<>();

    private static Handle          _cur_handle                  = null;

    public  String name = "";
    //}}}
    // Get_PARK_X {{{
    public  static int Get_PARK_X()
    {
        return (Get_STANDBY_WIDTH() - TUCKED_WIDTH); // container scrollX .. just tinny visible portion (most part hidden to the left)
    }
    //}}}
    // Get_TUCKED_WIDTH {{{
    public  static int Get_TUCKED_WIDTH()
    {
        return TUCKED_WIDTH;
        //return STANDBY_WIDTH; // XXX
    }
    //}}}
    // Get_STANDBY_WIDTH {{{
    public  static int Get_STANDBY_WIDTH()
    {
        return Settings.is_GUI_TYPE_HANDLES()
            ?   STANDBY_WIDTH
            :   Get_DOCK_STANDBY_WIDTH();
    }
    //}}}
    // Get_DOCK_STANDBY_WIDTH {{{
    public  static int Get_DOCK_STANDBY_WIDTH()
    {
        return NpButtonGridLayout.Get_SINGLE_COL_H() * Settings.TAB_GRID_S;
    }
    //}}}
    // Get_DOCK_WIDTH {{{
    public  static int Get_DOCK_WIDTH()
    {
        return NpButtonGridLayout.Get_SINGLE_COL_W() * Settings.TAB_GRID_S;
    }
    //}}}

    // LAYOUT
    // constructors {{{
    public Handle(Context context)                                                         { super(context); init(context); }
    public Handle(Context context,String name)                                             { super(context); init(context); this.name = name; }
    public Handle(Context context, AttributeSet attrs)                                     { super(context, attrs); init(context); }
/*
    public Handle(Context context, AttributeSet attrs, int defStyleAttr)                   { super(context, attrs, defStyleAttr); init(context); }
    public Handle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)  { super(context, attrs, defStyleAttr, defStyleRes); init(context); }
*/
    private void init(Context context)
    {
        instanceList.add( this );
    //  init_button(context);
    //  setLayoutTransition(new LayoutTransition());
    }
    //}}}

    // LAYOUT
    //{{{
    private static final int LAYOUT_AS_SET = 0;
    private static final int LAYOUT_SCREEN = 1;
    private static final int LAYOUT_DOCK   = 2;

    private final RelativeLayout.LayoutParams           docked_rlp = new RelativeLayout.LayoutParams(100,100);
    private boolean                      has_been_layed_once = false;
    private boolean                     thats_the_undertaker = false;
    private int                           handle_layout_type = LAYOUT_AS_SET;

    //}}}
    // set_LAYOUT_SCREEN {{{
    public void set_LAYOUT_SCREEN()
    {
        handle_layout_type         = LAYOUT_SCREEN;
        if(docked_rlp != null)
            setDockedLayout(docked_rlp.topMargin, docked_rlp.width, docked_rlp.height);
    }
    //}}}
    // set_LAYOUT_DOCK {{{
    public void set_LAYOUT_DOCK()
    {
        handle_layout_type         = LAYOUT_DOCK;
        if(docked_rlp != null)
            setDockedLayout(docked_rlp.topMargin, docked_rlp.width, docked_rlp.height);
    }
    //}}}
    // setDockedLayout {{{
    public void setDockedLayout(int top, int width, int height)
    {
        // [X] [Y]
        docked_rlp.topMargin  = top;

        // [HEIGHT]
        docked_rlp.height     = height;

        // [WIDTH] f(handle_layout_type)
        if(     handle_layout_type == LAYOUT_SCREEN)
            docked_rlp.width    = Settings.SCREEN_W - Get_STANDBY_WIDTH();
        else if(handle_layout_type == LAYOUT_DOCK)
            docked_rlp.width    = Get_DOCK_WIDTH();
        else {
/*
            docked_rlp.width
                =     (width > Get_STANDBY_WIDTH())
                ?      width
                :      width + Get_STANDBY_WIDTH()
                ;
*/
            docked_rlp.width = width;
        }

        has_been_layed_once = false; // no animation for first layout

        // GEOMETRY IDENTIFIES THE THE WIPER INSTANCE (undertaker)
        thats_the_undertaker
            =  (handle_layout_type   == LAYOUT_AS_SET)  // UNDERTAKER IS NOT FULL DISPLAY WIDTH
            && (docked_rlp.height == Settings.DISPLAY_H   ); // UNDERTAKER IS ... FULL DISPLAY HEIGHT
    }
    //}}}

    // LISTENERS
    // OnDraggedListener {{{
    public  interface OnDraggedListener { void onDragged(View view, float x, float y); }
    private           OnDraggedListener                         mOnDraggedListener;
    public  void   setOnDraggedListener( OnDraggedListener l) { mOnDraggedListener = l; }
    private           boolean                                   mOnDraggedListener_has_been_called;

    //}}}
    // OnActivatedListener {{{
    public  interface OnActivatedListener { void onActivated(View v, boolean expanded); }
    public  void   setOnActivatedListener( OnActivatedListener l) { mOnActivatedListener = l; }
    private           OnActivatedListener                           mOnActivatedListener;

    //}}}

    // SELECT-HIDE
    // Set_cur_handle {{{
    public  static void Set_cur_handle(Handle handle, String caller)
    {
//*HANDLE*/Settings.MOC(TAG_HANDLE, caller+"->Set_cur_handle("+_Get_handle_name(handle) +")");
        if(handle == null) _fold_all_instances();
        _cur_handle = handle;
        expand_current_collapse_others(caller);
    }
    //}}}
    // Get_cur_handle {{{
    public  static Handle Get_cur_handle()
    {
//HANDLE//Settings.MOC(TAG_HANDLE, "Get_cur_handle() ...return ["+ _Get_handle_name(_cur_handle) +"]");
        return _cur_handle;
        //return Get_expanded_handle();
    }
    //}}}
    // Get_expanded_handle {{{
    public  static Handle Get_expanded_handle()
    {
        for(int i = 0; i < instanceList.size(); i++)
        {
            Handle mHandle = instanceList.get(i);
            if(mHandle.isExpanded())
                return mHandle;
        }
        return null;
    }
    //}}}
    // _fold_all_instances {{{
    private static void _fold_all_instances()
    {
        for(int i = 0; i < instanceList.size(); i++)
        {
            Handle mHandle = instanceList.get(i);
            mHandle.setVisibility( View.VISIBLE );
            if(mHandle.isExpanded())
                mHandle.display_instant();
        }
    }
    //}}}

    // EVENTS setOnActivatedListener(mOnActivatedListener) setOnDraggedListener(mOnDraggedListener]
    // onTouchEvent {{{
    // {{{
    private final int REPORT_DELTA_MIN           = 5;
    private       int last_reported_event_x      = 0;
    private       int last_reported_event_y      = 0;
    private   boolean has_been_handled_at_ACTION_DOWN = false;

    // }}}
    private boolean was_animation_running_on_ACTION_DOWN;

    @Override public boolean onTouchEvent(MotionEvent event)
    {
        if(getTag() == Settings.HANDLE_HIDDEN_IN_STANDBY)
        {
//*HANDLE*/Settings.MOC(TAG_HANDLE, name+".onTouchEvent: HANDLE_HIDDEN_IN_STANDBY .. onTouchEvent ignored ");
            return true; // consumed
        }
        //if( !Has_cooled_down()   ) return true; // consumed

        int action = event.getActionMasked();
/*
        // UNDERTAKER {{{
        if( thats_the_undertaker )
        {
            if(action == MotionEvent.ACTION_DOWN) {
                mHandler.removeCallbacks(hr_activateCB);
                mHandler.post       (hr_activateCB                                ); // open
                mHandler.postDelayed(hr_activateCB, (int)(UNDERTAKER_COMMING_TIME)); // ...then fold
            }
            return true; // consumed
        }
        //}}}
*/
        // pass through empty dock
        //if(!isExpanded() && !Settings.is_GUI_TYPE_HANDLES()) return false;

        // DRAG ON ACTION_DOWN // {{{
        if(action == MotionEvent.ACTION_DOWN)
        {
            was_animation_running_on_ACTION_DOWN = is_animation_running();
//*HANDLE*/Settings.MOC(TAG_HANDLE, name+".onTouchEvent: was_animation_running_on_ACTION_DOWN");

            // DEPLOY ON ACTION_DOWN (FIRST EVENT) // {{{
            if( !isExpanded() ) {
                if(!has_been_handled_at_ACTION_DOWN )
                {
                    has_been_handled_at_ACTION_DOWN = true; // consume
                    if(!was_animation_running_on_ACTION_DOWN)
                        expand_toggle("onTouchEvent");
//*HANDLE*/if(was_animation_running_on_ACTION_DOWN)Settings.MOM(TAG_HANDLE, name+".expand_toggle [DEPLOY ON ACTION_DOWN] not called: [was_animation_running_on_ACTION_DOWN]");
                }
            }
            // }}}
            // [DRAG DOWN] {{{
            mOnDraggedListener_has_been_called = false;
            float  x = event.getRawX();
            if( is_within_slider_area(x) ) last_ACTION_DOWN_time  = System.currentTimeMillis();
            else                           last_ACTION_DOWN_time  = 0;

            //}}}
            return  true;
        }
        // }}}
        // DRAG ON ACTION_MOVE // {{{
        if(action == MotionEvent.ACTION_MOVE)
        {
//*HANDLE*/Settings.MOC(TAG_HANDLE, name+".onTouchEvent: ACTION_MOVE");
//*HANDLE*/Settings.MOM(TAG_HANDLE, "......(mOnDraggedListener!=null)="+ (mOnDraggedListener!=null) );
//*HANDLE*/Settings.MOM(TAG_HANDLE, ".......(last_ACTION_DOWN_time>0)="+ (last_ACTION_DOWN_time>0)  );
            // [DRAG MOVE] {{{
            if((mOnDraggedListener != null) && (last_ACTION_DOWN_time > 0))
            {
                float  x = event.getRawX();
                float  y = event.getRawY();
                int   dx = Math.abs(last_reported_event_x-(int)x);
                int   dy = Math.abs(last_reported_event_y-(int)y);

                boolean significant_move = (dx >  REPORT_DELTA_MIN) || (dy >  REPORT_DELTA_MIN);
//*HANDLE*/Settings.MOM(TAG_HANDLE, "................significant_move="+ significant_move           );
//*HANDLE*/Settings.MOM(TAG_HANDLE, "....is_within_slider_area("+x+")="+ is_within_slider_area(x)   );

                if(significant_move && is_within_slider_area(x))
                {
                    last_ACTION_MOVE_time = System.currentTimeMillis();
                    last_reported_event_x = (int)x;
                    last_reported_event_y = (int)y;
mOnDraggedListener_has_been_called = true;
setVisibility( View.INVISIBLE);
                    mOnDraggedListener.onDragged(this, x, y);// + docked_rlp.topMargin);
                }
            }
            // }}}
            return true;    // keep on swiping in a fullscreen dropdown
        }

        // }}}
        // DOCK ON ACTION_UP {{{
        if((action == MotionEvent.ACTION_UP))
        {
if( mOnDraggedListener_has_been_called ) setVisibility( View.  VISIBLE);
            // .. UNLESS IT WAS JUST OPENED
            // .. ALWAYS HIDE THE UNDERTAKER
            if(        !has_been_handled_at_ACTION_DOWN
                    && !was_animation_running_on_ACTION_DOWN
                    && !mOnDraggedListener_has_been_called
              )
            {
                long down_to_move_delay = last_ACTION_MOVE_time - last_ACTION_DOWN_time;
                int de_activate_delay
                    = (down_to_move_delay <= 0)
                    ?  (int)(HANDLE_ANIMTIME / 4)
                    :  (int)(HANDLE_ANIMTIME    );

                hr_activateCB_time = System.currentTimeMillis();
                mHandler.removeCallbacks(hr_activateCB);
                mHandler.postDelayed    (hr_activateCB, de_activate_delay);
            }
//*HANDLE*/if(was_animation_running_on_ACTION_DOWN)Settings.MOC(TAG_HANDLE, name+".hr_activateCB.expand_toggle [DOCK ON ACTION_UP] not called: [was_animation_running_on_ACTION_DOWN]");

            has_been_handled_at_ACTION_DOWN      = false;
            was_animation_running_on_ACTION_DOWN = false;

            return  true;
        }

        // }}}
        // KEEP ON WAITING
        return  true;
    }
    //}}}
    // is_within_slider_area {{{
    private boolean is_within_slider_area(float x)
    {
        boolean result
            =  ((x >= Settings.GUARD_LEFT) && (x <= Settings.TO_GUARD_RIGHT))
            //  || (handle_layout_type != LAYOUT_AS_SET)
            ;
//*HANDLE*/if(!result)Settings.MOC(TAG_HANDLE, "is_within_slider_area(["+Settings.GUARD_LEFT+"] < ["+ (int)x +"] < ["+Settings.TO_GUARD_RIGHT+"]) ...return "+result);
            return result;
    }
            //}}}
    // hr_activateCB {{{
    private static long hr_activateCB_time      = 0;
    private static long last_ACTION_DOWN_time   = 0;
    private static long last_ACTION_MOVE_time   = 0;

    private final Runnable hr_activateCB = new Runnable()
    {
        @Override
        public void run()
        {
            long motion_event_since_scheduled_activation_delay
                = (last_ACTION_DOWN_time > last_ACTION_MOVE_time)
                ?  last_ACTION_DOWN_time - hr_activateCB_time
                :  last_ACTION_MOVE_time - hr_activateCB_time;

            // canceled by a new gesture
            if(motion_event_since_scheduled_activation_delay > 0)
                return;

            expand_toggle("hr_activateCB");
        }
    };

    private void call_OnActivatedListener()
    {
        // LAYOUT (AFTER ANIM)
        adjust_after_anim_layout();

        if(mOnActivatedListener != null)
            mOnActivatedListener.onActivated(this, isExpanded());
    }

    //}}}
        // Animation_running {{{
        private static boolean Animation_running = false;

        public     void set_animation_running() {
//*HANDLE*/Settings.MOC(TAG_HANDLE, name+".set_animation_running");
            Animation_running = true;
        }
        public  boolean  is_animation_running() {
//*HANDLE*/Settings.MOC(TAG_HANDLE, name+".is_animation_running: ...return "+ Animation_running);
            return Animation_running;
        }

        private final AnimatorListenerAdapter animation_end_listener = new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
//*HANDLE*/Settings.MOC(TAG_HANDLE, name+".animation_end_listener()");
                call_OnActivatedListener();
                Animation_running = false;
            }
        };
        // }}}
    // Has_cooled_down {{{
    private static    long Last_activation_time = 0;
    private static boolean Has_cooled_down()
    {
        long    this_time = System.currentTimeMillis();
        long    delay     = this_time - Last_activation_time;
        return              (delay >= ACTIVATION_COOL_DOWN_TIME);
    }
    // }}}

    // ACTIVE
    // expand_toggle {{{
    private void expand_toggle(String caller)
    {
//*HANDLE*/Settings.MOC(TAG_HANDLE, name+".expand_toggle("+caller+")");
        // COOLDOWN FILTER {{{
        long this_time          = System.currentTimeMillis();
        long delay              = this_time - Last_activation_time;
        Last_activation_time    = this_time;
        if(delay < ACTIVATION_INTERVAL_MIN)
            return;
        //}}}
        // HIDE CURRENT {{{
        if(this.isExpanded()) {
            _cur_handle             = null;            // hide current
        }
        //}}}
        // NEW CURRENT {{{
        else {
            hr_activateCB_time      = 0;    // new event chain history
            last_ACTION_DOWN_time   = 0;
            last_ACTION_MOVE_time   = 0;
            _cur_handle             = this;
        }
        //}}}
        expand_current_collapse_others("expand_toggle");

    }
    //}}}

    // _Get_cur_handle_name {{{
    private static String _Get_cur_handle_name()
    {
        return "_cur_handle=["+ ((_cur_handle != null) ? _cur_handle.name : "null") +"]";
    }
    private static String _Get_handle_name(Handle handle)
    {
        return "handle="+ ((handle != null) ? handle.name : "null");
    }
    //}}}

    // DISPLAY
    // expand_current_collapse_others {{{
    private  static void expand_current_collapse_others(String caller)
    {
//*HANDLE*/Settings.MOC(TAG_HANDLE, "expand_current_collapse_others("+caller+"): "+_Get_cur_handle_name());

        // CHECK: ALREADY EXPANDED {{{
        if(_cur_handle != null)
        {
            if( _cur_handle.isExpanded() )
            {
//*HANDLE*/Settings.MOC(TAG_HANDLE, "...ALREADY EXPANDED: NO CHANGE");
                return;
            }
        }
        //}}}
        // CHECK: NOTHING TO FOLD {{{
        if(_cur_handle == null)
        {
            int i;
            for(i = 0; i < instanceList.size(); i++) {
                Handle mHandle = instanceList.get(i);
                if( mHandle.isExpanded() ) break;
            }
            if(i == instanceList.size()) {
//*HANDLE*/Settings.MOC(TAG_HANDLE, "...NONE EXPANDED: NO CHANGE");
                return;
            }
        }
        // }}}

        // current visible .. others temporarily hidden {{{
        if((_cur_handle != null) && !_cur_handle.thats_the_undertaker)
        {
            // temporarily hide others
            for(int i = 0; i < instanceList.size(); i++)
            {
                Handle mHandle = instanceList.get(i);
                if((mHandle == _cur_handle))// || mHandle.thats_the_undertaker)
                    mHandle.setVisibility( View.  VISIBLE);
                else
                    mHandle.setVisibility( View.INVISIBLE);
            }
            // post a restore task
            mHandler.removeCallbacks(hr_restore_visibility);
            mHandler.postDelayed    (hr_restore_visibility, RESTORE_VISIBILITY_DELAY);
        }
         //}}}
        // unfold current .. fold others {{{
        boolean no_handle_to_expand = (_cur_handle == null) || _cur_handle.thats_the_undertaker;
        for(int i = 0; i < instanceList.size(); i++)
        {
            Handle mHandle = instanceList.get(i);
            if     ((_cur_handle == mHandle) && !mHandle.isExpanded()) mHandle.display_animate (); // ...slowly enters the selected one
            else if(( no_handle_to_expand  ) &&  mHandle.isExpanded()) mHandle.display_regress (); // ...gently push the current one with no replacement
            else if(                             mHandle.isExpanded()) mHandle.display_collapse(); // ...the new selected one kicks the current out of stage
        }
        // }}}
        // if no current .. post a dimmer task {{{
    //  if(_cur_handle == null) Re_post_dimm();

        //}}}
    }
    // Collapse_all_instances .. keep in sync with Settings.is_GUI_TYPE_HANDLES {{{
    public static void Collapse_all_instances()
    {
//*HANDLE*/Settings.MOC(TAG_HANDLE, "Collapse_all_instances");
        for(int i = 0; i < instanceList.size(); i++)
        {
            Handle mHandle = instanceList.get(i);
            mHandle.display_collapse();
        }

    }
    //}}}
//    // Expand_intance {{{
//    public static void Expand_intance(Handle handle)
//    {
////*HANDLE*/Settings.MOC(TAG_HANDLE, "Collapse_all_instances");
//        for(int i = 0; i < instanceList.size(); i++)
//        {
//            Handle mHandle = instanceList.get(i);
//            if(mHandle == handle) mHandle.display_instant ();
//            else                  mHandle.display_collapse();
//        }
//
//    }
//    //}}}
    // hr_restore_visibility {{{
    private static final long           RESTORE_VISIBILITY_DELAY = 800L;
    private static final Runnable hr_restore_visibility = new Runnable()
    {
        @Override
        public void run() {
//*HANDLE*/Settings.MOC(TAG_HANDLE, "hr_restore_visibility: "+_Get_cur_handle_name());
            for(int i = 0; i < instanceList.size(); i++)
            {
                Handle mHandle = instanceList.get(i);
                if(mHandle.getTag() != Settings.HANDLE_HIDDEN_IN_STANDBY)
                    mHandle.setVisibility( View.VISIBLE );
            }
        }
    };
    //}}}
    //}}}
    // _display {{{
    // {{{
    private void display_animate () { display(true ,  true); }
    private void display_instant () { display(true , false); }
    private void display_regress () { display(false,  true); }
    private void display_collapse() { display(false, false); }

    private final AnimLayout mal = new AnimLayout();
    // }}}
    // display {{{
    private void display(boolean visible, boolean with_anim)
    {
//*HANDLE*/Settings.MOC(TAG_HANDLE, name+"._display(visible="+visible+", with_anim="+with_anim+")" );
        // UNDIMMING {{{
        if(visible)
            UnscheduleDimm();

        //}}}
        // LAYOUT (BEFORE ANIM) {{{
        adjust_before_anim_layout( visible );

        //}}}
        // LOCATION & SCALE {{{
        setPivotX(0);
        setPivotY(0);

        // }}}
        // [instant] or [animation] {{{

        // [initializing]
        if(!has_been_layed_once && thats_the_undertaker)
            has_been_layed_once = true;

        //}}}
        // INSTANT LAYOUT {{{
        if(!has_been_layed_once || !with_anim || !ANIM_SUPPORTED)
        {
            this.setX     ( mal.x );
            this.setY     ( mal.y );
            this.setScaleY( mal.sY );
            has_been_layed_once = true; // unlock anim for next layout

            call_OnActivatedListener();
        }
        //}}}
        // ANIMATED LAYOUT [X Y SCALE] {{{
        else {
            set_animation_running();

            AnimatorSet set = new AnimatorSet();

            if( thats_the_undertaker )
            {
                if(visible) {
                    set.setDuration( UNDERTAKER_COMMING_TIME );   // i.e. the undertaker on the lookout oO
                    set.setInterpolator(new DecelerateInterpolator());
                }
                else {
                    set.setDuration( UNDERTAKER_LEAVING_TIME );   // i.e. he got its victims
                    set.setInterpolator(new AccelerateInterpolator());
                }
            }
            else {
                set.setDuration( HANDLE_ANIMTIME );
                set.setInterpolator(new DecelerateInterpolator());
            }

            set
                .play(ObjectAnimator.ofFloat(this, View.X      , mal.x ))
                .with(ObjectAnimator.ofFloat(this, View.Y      , mal.y ))
                .with(ObjectAnimator.ofFloat(this, View.SCALE_Y, mal.sY))
                ;

            set.addListener( animation_end_listener ); // what to do once this animation is done

            set.start();
        }
        //}}}
        // ACTIVATION CALLBACK {{{
    //  mHandler.removeCallbacks( hr_activateChildrenCB );
        mHandler.postDelayed    ( hr_activateChildrenCB, 100); // XXX

        //}}}
    }
    //}}}
    // private class AnimLayout {{{
    private class AnimLayout
    {
//docked_X\|docked_Y\|visibleX\|visibleY\|docked_S

        boolean visible;

        int   docked_X;
        int   docked_Y;
        int   visibleX;
        int   visibleY;
        float docked_S;

        int          x;
        int          y;
        float       sY;

        public AnimLayout() { }
    }
    //}}}
    // adjust_before_anim_layout {{{
    private void adjust_before_anim_layout(boolean vis)
    {
        mal.visible  = vis;
        mal.docked_X = -(docked_rlp.width - Get_STANDBY_WIDTH());
        mal.docked_Y =   docked_rlp.topMargin;
        mal.visibleX = Get_STANDBY_WIDTH(); // slightly away from left margin .. leaving room for collapsed siblings
        mal.visibleY =  0;
        mal.docked_S = 1F;

        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams)getLayoutParams();
        switch( handle_layout_type )
        {
            case LAYOUT_DOCK  : // [dck_handle] {{{
                if( !Settings.is_GUI_TYPE_HANDLES() ) {
                    mal.visibleX= 0;                                             // DOCK HAS UNDERTAKER TO LEAVE LEFT MARGIN FOR
                }
                mal.visibleY    = 0;                                             // SCREEN TOP
                rlp.width       = Get_DOCK_WIDTH();                              // LIST ROW WIDTH
                rlp.height      = Settings.DISPLAY_H - docked_rlp.topMargin;     // LIST ROW HEIGHT
                mal.docked_S    = Settings.is_GUI_TYPE_HANDLES()
                    ?              (float)docked_rlp.height / Settings.DISPLAY_H // CURRENT SCREEN HEIGHT
                    :              1F;                                           // FULL    SCREEN HEIGHT

                break;
                // }}}
            case LAYOUT_SCREEN: // [top_handle] [mid_handle] {{{
                mal.visibleY    = 0;                                                        // TOP OF SCREEN
                rlp.width       = Settings.SCREEN_W - mal.visibleX;                         // SCREEN WIDTH - LEFT OFFSET
                rlp.height      = Settings.DISPLAY_H;                                       // CURRENT SCREEN HEIGHT
                mal.docked_S    = (float)docked_rlp.height / Settings.DISPLAY_H;            // CURRENT SCREEN WIDTH

                break;
                // }}}
            case LAYOUT_AS_SET: // [bot_handle] {{{
            default:
                mal.visibleX    = DOCK_HIDE_WIDTH;                          // past the untertaker width
                mal.visibleY    = docked_rlp.topMargin;                     // OWN VERTICAL POSITION
                rlp.width       = docked_rlp.width;                         // DOCKED OR COMPLETELY HIDDEN by DOCK
                rlp.height      = docked_rlp.height;                        // same HEIGHT for both visible and docked
                mal.docked_S    = 1F;                                       // same SCALE  for both visible and docked

                if(mal.docked_X > 0) mal.docked_X = -docked_rlp.width / 2;  // i.e. !expanded
                break;
                // }}}
        }
        setLayoutParams(rlp);
// from docked to visible
if(mal.visible) {
    if(getX() < mal.docked_X) {
//*HANDLE*/Settings.MOM(TAG_HANDLE, name+".adjust_before_anim_layout: (WARPING TO docked_X) FROM "+getX()+" TO "+mal.docked_X);
        setX(mal.docked_X);
    }
}
// from visible to docked
else {
    if(getX() > mal.visibleX) {
//*HANDLE*/Settings.MOM(TAG_HANDLE, name+".adjust_before_anim_layout: (WARPING TO visibleX) FROM "+getX()+" TO "+mal.visibleX);
        setX(mal.visibleX);
    }
}

        mal.x  = mal.visible ? mal.visibleX : mal.docked_X;
        mal.y  = mal.visible ? mal.visibleY : mal.docked_Y;
        mal.sY = mal.visible ? 1F : mal.docked_S;        // visible scale 1:1 .. usees rlp
    }
    //}}}
    // adjust_after_anim_layout {{{
    private void adjust_after_anim_layout()
    {
        // show advanced handles
        boolean advanced_handle_showing
            = (_cur_handle == this)             // .. when currently requested
            || Settings.is_GUI_TYPE_HANDLES();  // .. or when in gui_type_handles mode

        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams)getLayoutParams();
        switch( handle_layout_type )
        {
            case LAYOUT_DOCK:   // [dck_handle] {{{
                rlp.width       = Get_DOCK_WIDTH();                          // LIST ROW WIDTH
                rlp.height      = Settings.DISPLAY_H - docked_rlp.topMargin; // LIST ROW HEIGHT
                break;

                //}}}
            case LAYOUT_SCREEN: // [top_handle] [mid_handle] {{{
                rlp.width       = advanced_handle_showing ? Settings.SCREEN_W - mal.visibleX : 0;// SCREEN WIDTH - LEFT OFFSET
                break;

                //}}}
            case LAYOUT_AS_SET: // [bot_handle] {{{
            default:
                rlp.width       = advanced_handle_showing ? docked_rlp.width : 0;// DOCKED OR COMPLETELY HIDDEN by DOCK
                break;
                //}}}
        }
        setLayoutParams(rlp);
    }
    //}}}
    //}}}
    // isExpanded {{{
    private boolean isExpanded()
    {
        return getX() >= 0;
    }
    // }}}
    // needs_layout {{{
    private boolean needs_layout_state = true;
    public  boolean needs_layout()              { return needs_layout_state;         }
    public  void    needs_layout(boolean state) {        needs_layout_state = state; }

    //}}}

    // VISIBILITY
    // Re_post_dimm UnscheduleDimm {{{
    public  static void Re_post_dimm()
    {
        if(_cur_handle != null) return;

        UnscheduleDimm();

        mHandler.postDelayed(hr_dimmerCB, DIMMER_DELAY);
    }

    public  static void UnscheduleDimm()
    {
        mHandler.removeCallbacks( hr_dimmerCB );
        dimmer_set( -1F );
    }

    //}}}
    // hr_dimmerCB {{{
    private static final int DIMMER_DELAY = 5000;
    private static final Runnable hr_dimmerCB = new Runnable()
    {
        @Override
        public void run() {
            dimmer_set(0.3f);
        }
    };
    //}}}
    // dimmer_set {{{
    private float not_dimmed_alpha = 0f;
    private static void dimmer_set(float alpha)
    {
        for(int i = 0; i < instanceList.size(); i++)
        {
            Handle mHandle = instanceList.get(i);

            if( mHandle.not_dimmed_alpha == 0)
                mHandle.not_dimmed_alpha  = mHandle.getAlpha();

            if(alpha >= 0) mHandle.setAlpha(                    alpha );
            else           mHandle.setAlpha( mHandle.not_dimmed_alpha );
        }
    }
    //}}}
    // hr_activateChildrenCB {{{
    private final Runnable hr_activateChildrenCB = new Runnable()
    {
        @Override
        public void run() {
            checkChildrenVisibility();
        }
    };
    //}}}
    // checkChildrenVisibility {{{
    private void checkChildrenVisibility()
    {
        boolean visible = (this == _cur_handle);

        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if(    child.getTag() == Settings.CHILD_IGNORE_VISIBILITY) continue;

            if(    child.getTag() == Settings.CHILD_ALWAYS_VISIBLE   ) child.setVisibility( View.VISIBLE ); // sticky child

            // parent [IS IN] its full visible position
            else if(visible) {
                if(child.getTag() != Settings.CHILD_SHOWN_HIDDEN     ) child.setVisibility( View.VISIBLE ); // async child
                else                                                   child.setVisibility( View.GONE    );
            }
            // parent [IS NOT IN] its full visible position
            else {
                if(child.getTag() != Settings.CHILD_SHOWN_HIDDEN     ) child.setVisibility( View.GONE    ); // async child
                else                                                   child.setVisibility( View.VISIBLE );
            }
        }
    }
    // }}}
    // setElevation {{{
    public void setElevation(float elevation)
    {
        if( ELEVATION_SUPPORTED ) super     .setElevation(      elevation);
        else                      ViewCompat.setElevation(this, elevation);
        //bringToFront();
    }
    //}}}

}

/* // {{{
(160602:16h24)
:let @p="HANDLE"

:let @p="\\w\\+"

" ACTIVATED
  :g/^\/\*p\w*\*\/.*p/t$
" ......... -> COMMENT
  :g/^\/\*p\w*\*\/.*p/s/^/\//


" commented
  :g/^\/\/\*p\w*\*\/.*p/t$
" ..........-> ACTIVATE
  :g/^\/\/\*p\w*\*\/.*p/s/^\///



*/ // }}}

