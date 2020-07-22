package ivanwfr.rtabs; // {{{

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.support.annotation.ColorInt;

// COMPAT
import android.support.v4.view.ViewCompat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.os.Looper;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

// }}}

class NpButton extends Button implements Settings.FromToInterface
{
    // MONITOR TAGS
    private static       String TAG_LOG        = Settings.TAG_LOG;

    // Constructors {{{
    public NpButton(Context context                                      ) { super(context                     );                                 }
    public NpButton(Context context, String shape                        ) { super(context                     ); set_shape(shape, true        ); }
    public NpButton(Context context, String shape,   boolean with_outline) { super(context                     ); set_shape(shape, with_outline); }
    public NpButton(Context context, AttributeSet attrs                  ) { super(context, attrs              );                                 }
    public NpButton(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr);                                 }

    //}}}
    // CloneInstance {{{
    //public NpButton clone() { return CloneInstance( new NpButton(getContext(), shape, true) ); }
    public static NpButton CloneInstance(NpButton source)
    {
        NpButton clone = new NpButton( source.getContext() );

        // Text {{{
        clone.setTypeface     ( source.getTypeface()     );
        clone.setEllipsize    ( TextUtils.TruncateAt.END );
        clone.setText         ( source.getText()         );

        // }}}
        // Colors {{{
        clone.setBackgroundColor( source.getBackgroundColor () );
        clone.setTextColor      ( source.getCurrentTextColor() );

        //}}}
        // shape {{{
        clone.shape             = source.shape       ;
        clone.with_outline      = source.with_outline;

        clone.tl                = source.tl;
        clone.lt                = source.lt;
        clone.bl                = source.bl;
        clone.rt                = source.rt;
        clone.tr                = source.tr;
        clone.lb                = source.lb;
        clone.br                = source.br;
        clone.rb                = source.rb;

        //}}}
        // scrollbar specific {{{
        clone.w_max             = source.w_max  ;
        clone.w_min             = source.w_min  ;
        clone.at_left           = source.at_left;
        clone.at_top            = source.at_top ;

        //}}}
        // LayoutParams {{{
        clone.setLayoutParams( source.getLayoutParams() );

        // }}}
//clone.requestLayout();
        // xy wh translation pivot gravity {{{
        //clone.setX            ( source.getX           () );
        //clone.setY            ( source.getY           () );
        //clone.setWidth        ( source.getWidth       () );
        //clone.setHeight       ( source.getHeight      () );
        clone.setTranslationX ( source.getTranslationX() );
        clone.setTranslationY ( source.getTranslationY() );
        clone.setPivotX       ( source.getPivotX      () );
        clone.setPivotY       ( source.getPivotY      () );
        clone.setGravity      ( source.getGravity     () );

        // }}}
//clone.requestLayout();
        // per-instance dynamic state {{{
        //    action_down_in_margin

        clone.lockedElevation = source.lockedElevation;

        clone.mPaint          = source.mPaint         ;
        clone.oPaint          = source.oPaint         ;
        clone.path            = source.path           ;

        clone.fit_ts_on_vh    = source.fit_ts_on_vh   ;
        clone.may_overflow    = source.may_overflow   ;
        clone.fixedTextSize   = source.fixedTextSize  ;
        clone.fixedGravity    = source.fixedGravity   ;

        //}}}
//clone.invalidate();
//clone.requestLayout();
        return clone;
    }
    //}}}
/*
    // BITMAPS {{{
    private static NinePatchDrawable ninePatchDrawable;

    private static int current_bitmap_num = 0;
    private static int[] bitmap_ids = {
          R.drawable.grid_512
        , R.drawable.logo
        , R.drawable.white
    };

    //}}}
*/
    // vars {{{
    private final boolean ELEVATION_SUPPORTED   = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    private       boolean OUTLINING_SUPPORTED   = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    private final boolean SHADOWLAYER_SUPPORTED = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2);

    private static final int    OVERRIDE_COLOR  = Color.parseColor("#D0444444");
    private static final int    DARK_COLOR      = Color.parseColor("#FF000000");
    private static final int    DIMM_COLOR      = Color.parseColor("#D0000000");
    private static final int    LIGHT_COLOR     = Color.parseColor("#FFffffff");
//  private static final int    STROKE_COLOR    = Color.parseColor("#AAff0000");
//  private static final int    STROKE_WIDTH    = 3;

    private Paint               mPaint          = null;
    private Paint               oPaint          = null;
    private Path                path            = null;
    private float               fit_ts_on_vh    = 0;    // TEXT-SIZE on VIEW-HEIGHT
    private boolean             may_overflow    = true; // smaller geometry must be readable - while full screen must show everything
//  public  boolean             scalable        = false;// whether Settings.DEV_SCALE applies or not
    public  boolean             fixedTextSize   = false;
    public  boolean             fixedGravity    = false;

    // SCROLLBAR SPECIFICS {{{
    private int                 w_max                 = 0;
    public  int                 w_min                 = 0;
    public  boolean             at_left               = false;
    private boolean             at_top                = false;
    public  boolean             action_down_in_margin = false;
    //}}}

    // neighborhood (left right top bottm) .. i.e. true stans for square-joining-corner
    public  boolean tl = false;
    public  boolean lt = false;
    public  boolean bl = false;
    public  boolean rt = false;
    public  boolean tr = false;
    public  boolean lb = false;
    public  boolean br = false;
    public  boolean rb = false;

    // cast shadow or not .. f(_selectOutlineProvider)
    public  boolean with_outline    = false;

    private final int ORIENTATION_HORIZONTAL = 0;
    private final int ORIENTATION_VERTICAL   = 1;
    private final int ORIENTATION_TOP_DOWN   = 2;
    private final int ORIENTATION_DOWN_TOP   = 4;
    public        int orientation            = ORIENTATION_HORIZONTAL;

    // defaults to current value of Settings.GUI_TYPE
    public        String shape               = Settings.EMPTY_STRING;

    //}}}

    // FromToInterface
    // SAVE FROM {{{
    private float x_from, y_from,   s_from,   xrfrom, yrfrom, zrfrom;
    private float x_to  , y_to  ,   s_to  ,   xrto  , yrto  , zrto;

    public  void  save_from(String caller)
    {
        save_from_caller = caller;

        x_from = x_to = getX();
        y_from = y_to = getY();

        s_from = s_to = getScaleY();

        xrfrom = xrto = getRotationX();
        yrfrom = yrto = getRotationY();
        zrfrom = zrto = getRotation (); // Z
    }

    private String save_from_caller = "";

    public  String get_save_from_caller() { return save_from_caller; }

    //}}}
    // SET TO {{{
    public  void set_x_to  (float x) { x_to = x; }
    public  void set_y_to  (float y) { y_to = y; }
    public  void set_s_to  (float s) { s_to = s; }

    public  void set_xrto  (float y) { xrto = y; } // setRotationY
    public  void set_yrto  (float y) { yrto = y; } // setRotationX
    public  void set_zrto  (float y) { zrto = y; } // setRotationZ
    //}}}
    // GET FROM {{{
    public  float get_x_from()       { return x_from; }
    public  float get_y_from()       { return y_from; }
    public  float get_s_from()       { return s_from; }

    public  float get_xrfrom()       { return xrfrom; }
    public  float get_yrfrom()       { return yrfrom; }
    public  float get_zrfrom()       { return zrfrom; }

    //}}}
    // GET TO {{{
    public float get_x_to  ()        { return x_to  ; }
    public float get_y_to  ()        { return y_to  ; }
    public float get_s_to  ()        { return s_to  ; }

    public float get_xrto  ()        { return xrto  ; }
    public float get_yrto  ()        { return yrto  ; }
    public float get_zrto  ()        { return zrto  ; }
    //}}}

    // SHAPE
    // set_shape {{{
    public void set_shape(String shape                      ) { set_shape(shape, with_outline); } // .. [with_outline] (unchanged)
    public void set_shape(String shape, boolean with_outline)
    {
        this.shape        = shape.intern();
        this.with_outline = with_outline;

        if( this.with_outline )
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                if(getOutlineProvider() == ViewOutlineProvider.BACKGROUND)
                    _selectOutlineProvider();
                else
                    invalidateOutline();
            }
        }

        if     (this.shape == NotePane.SHAPE_TAG_PADD_R) set_round_corners_right();
        else if(this.shape == NotePane.SHAPE_TAG_PADD_L) set_round_corners_left ();
        else                                             clear_round_corners();

        invalidate();
    }
    //}}}
    // getLayoutHitRect {{{
    public Rect getLayoutHitRect(Rect hr)
    {
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)getLayoutParams();
        hr.left   = mlp.leftMargin;
        hr.top    = mlp. topMargin;
        hr.right  = mlp.leftMargin + mlp.width;
        hr.bottom = mlp. topMargin + mlp.height;
        return hr;
    }
    //}}}

    // SCROLLBAR
    // scroll_nb_shrink_to_shape {{{
    public void scroll_nb_shrink_to_shape(          ) { scroll_nb_shrink_to_shape(1); }
    public void scroll_nb_shrink_to_shape(int shrink)
    {
//System.err.println("scroll_nb_shrink_to_shape: SCROLLBAR_SHAPE_W=["+Settings.SCROLLBAR_SHAPE_W+"] getWidth=["+getWidth()+"]");
//System.err.println("scroll_nb_shrink_to_shape(shrink "+shrink+")");

        shrink = Math.min(Math.abs(shrink), Settings.SCROLLBAR_SHRINK_MAX);

        if(getWidth() > Settings.SCROLLBAR_SHAPE_W)
        {
            if( Settings.SCROLLBAR_USE_LAYOUT_PARAMS )
            {
                FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams)getLayoutParams();
                flp.width = Settings.SCROLLBAR_SHAPE_W - shrink; // will require user action to make room for WVTOOLS
                setLayoutParams(flp);
            }
            //else {
                setWidth(   Settings.SCROLLBAR_SHAPE_W - shrink);
            //  invalidate();
            //}
        }
//System.err.println("scroll_nb_shrink_to_shape: Settings.SCROLLBAR_SHAPE_W=["+Settings.SCROLLBAR_SHAPE_W+"]");
//System.err.println("scroll_nb_shrink_to_shape: getWidth()=["+getWidth()+"]");
            setRight(getLeft() + Settings.SCROLLBAR_SHAPE_W - shrink);
//System.err.println("scroll_nb_shrink_to_shape: getWidth()=["+getWidth()+"]");
        scroll_nb_adjust_shape();
    }
    // }}}
    // scroll_nb_set_w_min_w_max_at_left_at_top {{{
    public void scroll_nb_set_w_min_w_max_at_left_at_top(int w_min, int w_max, boolean at_left, boolean at_top)
    {
//System.err.println("scroll_nb_set_w_min_w_max_at_left_at_top(w_min=["+w_min+"] w_max=["+w_max+"] at_left=["+at_left+"] at_top=["+at_top+"])");
        this.w_min   = w_min;
        this.w_max   = w_max;

        this.at_left = at_left;
        this.at_top  = at_top;

        scroll_nb_adjust_shape();
    }
    //}}}
    // scroll_nb_adjust_shape {{{
    private void scroll_nb_adjust_shape()
    {

    //  int wh_gap   = get_wh_gap();
        int      w   = getWidth () ;//- wh_gap;

        if(w <= Settings.SCROLLBAR_SHAPE_W)
        {
            set_shape( NotePane.Get_current_shape() );

            if(at_left) set_round_corners_right();
            else        set_round_corners_left ();
        }
        else {
            set_shape( NotePane.SHAPE_TAG_SCROLL    );
        }

//System.err.println("scroll_nb_adjust_shape: getWidth()=["+getWidth()+"]");
    }
    //}}}

    // SPREAD
    // {{{
    private boolean                             spread_state = false;

    private FrameLayout.LayoutParams flp_saved          = null;
    private String                   shape_saved        = null;
    private CharSequence             text_saved         = null;
    private float                    translationX_saved = 0f;
    private float                    translationY_saved = 0f;
    private float                               X_saved = 0f;
    private float                               Y_saved = 0f;
    private float                          scaleX_saved = 0f;
    private float                          scaleY_saved = 0f;

    public boolean is_spread           () { return spread_state; }
    public boolean has_save_attributes () { return (flp_saved != null); }
    public void    has_clear_attributes() {         flp_saved  = null; }
    // }}}
    // get_saved_y {{{
    public float get_saved_y()
    {
        return (shape_saved != null) ? Y_saved          : getY();
    }
    //}}}
    // get_saved_x {{{
    public float get_saved_x()
    {
        return (shape_saved != null) ? X_saved          : getX();
    }
    //}}}
//    // set_saved_y {{{
//    public void set_saved_y(float y)
//    {
//        Y_saved = y;
//    }
//    //}}}
    // set_saved_x {{{
    public void set_saved_x(float x)
    {
        X_saved                  = x;
        if(flp_saved != null)
            flp_saved.leftMargin = (int)x;
        // USED WHEN CHANGING WEBVIEW MARKERS MARGIN SIDE (WVTools.do_marker_sync_changed_side)
    }
    //}}}
    // get_saved_h {{{
    public float get_saved_h()
    {
        return (  flp_saved != null) ? flp_saved.height :      0;
    }
    //}}}
    // get_saved_w {{{
    public float get_saved_w()
    {
        return (  flp_saved != null) ? flp_saved.width  :      0;
    }
    //}}}
    // save_attributes {{{
    public void save_attributes()
    {
        text_saved                      =         getText();
        scaleX_saved                    =       getScaleX();
        scaleY_saved                    =       getScaleY();
        X_saved                         =            getX();
        Y_saved                         =            getY();
        translationX_saved              = getTranslationX();
        translationY_saved              = getTranslationY();

        FrameLayout.LayoutParams    flp = (FrameLayout.LayoutParams)getLayoutParams();

        if(flp_saved == null) flp_saved = new FrameLayout.LayoutParams(1,1);
        flp_saved.leftMargin            = flp.leftMargin;
        flp_saved.topMargin             = flp.topMargin ;
        flp_saved.height                = flp.height    ;
        flp_saved.width                 = flp.width     ;

if(scaleY_saved != 1) System.err.println("*** NpButton.save_attributes: CALLED WHILE (SCALE == "+scaleY_saved+") ***");

        shape_saved = shape;    // [attributes have been saved once]
    }
    //}}}
    // spread_start {{{
    public  void spread_start(String text_and_info)
    {
        if(shape_saved == null) // (attributes have been saved once)
            save_attributes();

        spread_state = true;

        // LAYOUT SPEAD .. f(DISPLAY_H DISPLAY_W TOOL_BADGE_SIZE) {{{
        FrameLayout.LayoutParams    flp = (FrameLayout.LayoutParams)getLayoutParams();
        flp.leftMargin                  = Settings.TOOL_BADGE_SIZE;
        flp.topMargin                   = Settings.TOOL_BADGE_SIZE;
        flp.height                      = Settings.DISPLAY_H - 2*Settings.TOOL_BADGE_SIZE;
        flp.width                       = Settings.DISPLAY_W - 2*Settings.TOOL_BADGE_SIZE;
        setLayoutParams(            flp);

        // }}}
        // SET [SHAPE_TAG_ONEDGE] [TRANSLATION 0,0] [text_and_info] {{{

        set_shape(NotePane.SHAPE_TAG_ONEDGE, true);

        setTranslationX(  0  );
        setTranslationY(  0  );

        setText( text_and_info );
        //}}}
    }
    //}}}
    // spread_stop {{{
    public  void spread_stop()
    {
        spread_state = false;

        if(shape_saved == null)
        {
System.err.println("*** NpButton.spread_stop: CALLED WITH (shape_saved == null) ***");
            return;
        }

        // requires UI Thread:
        // android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
        if(Looper.myLooper() == Looper.getMainLooper())
        {
            if(flp_saved != null)
            {
                FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams)getLayoutParams();
                flp.leftMargin               = flp_saved.leftMargin;
                flp.topMargin                = flp_saved.topMargin ;
                flp.height                   = flp_saved.height    ;
                flp.width                    = flp_saved.width     ;
                setLayoutParams( flp );
            }
            else {
System.err.println("*** NpButton.spread_stop: CALLED WITH (flp_saved == null) ***");
            }
            set_shape(          shape_saved , true);
            setText(             text_saved );
        }
        else {
System.err.println("*** NpButton.spread_stop: NOT CALLED ON UI THREAD ***");
        }

        // TODO: try setLeft etc. with non UI Thread

        setScaleX      (       scaleX_saved );
        setScaleY      (       scaleY_saved );

        setX           (            X_saved );
        setY           (            Y_saved );
        setTranslationX( translationX_saved );
        setTranslationY( translationY_saved );
    }
    //}}}

    // API LEVEL DEPENDENT
    // _setForegroundGravity {{{
    public void _setForegroundGravity(int gravity)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            super.setForegroundGravity( gravity );
    }
    //}}}

    // setOpacity .. (the alpha component of the 0xAArrggbb Paint color) {{{
    public void setOpacity(int opacity_255)
    {
        if(mPaint == null) _init();

        mPaint.setAlpha( opacity_255 );

        super.getBackground().setAlpha( opacity_255 );

        invalidate();

    }
    //}}}

    // is_a_visited_profile_button {{{
    public boolean is_a_visited_profile_button()
    {
        return (instance_bg_color == Settings.BG_COLOR_DOCK_VISITED);
    }
    //}}}
    // is_an_index_profile_button {{{
    public boolean is_an_index_profile_button()
    {
        return (instance_bg_color == Settings.BG_COLOR_DOCK_INDEX);
    }
    //}}}

    // background color {{{
    private int     blurred_color = -1;
    public  int   pushed_bg_color = -1;
    public  int instance_bg_color = Color.LTGRAY;
    //}}}
    // push-pop for glowing background color property animation {{{
    public void pushBackgroundColor() {                            pushed_bg_color  = instance_bg_color  ; }
    public void popBackgroundColor () { if(pushed_bg_color != -1) setBackgroundColor(   pushed_bg_color ); }

    // }}}
    // setBackgroundColor {{{
    public void setBackgroundColor(@ColorInt int color)
    {
        if(mPaint == null) _init();

        instance_bg_color = color;
        mPaint.setColor( instance_bg_color );

        if(blurred_color != -1)
        {
            setBlurred( true ); // with new instance_bg_color
        }
        else {
            if( may_see_through() )
            {
                // FILL CANVAS
                super.setBackgroundColor( Color.TRANSPARENT  ); // BACKGROUND FRAME NOT FILLED
            //  super.setBackgroundColor( color & 0xAAFFFFFF ); // BACKGROUND FRAME TRANSLUCENT
            //  mPaint.setShadowLayer(  10, -10, -10, instance_bg_color);// & 0xAAFFFFFF); // radius, dx, dy, shadowColor
            //  ViewCompat.setBackgroundTintList(this, ColorStateList.valueOf( color & 0xAAFFFFFF ));

                // DROP COLORED OUTLINE SHADOW
                // XXX ???

                invalidate();
            }
            else {
                super.setBackgroundColor( color );
            }
        }
    }
    //}}}
    // getBackgroundColor {{{
    @ColorInt public int getBackgroundColor()
    {
        return mPaint.getColor();
    }
    //}}}

    // get_effective_shape {{{
    private String get_effective_shape()
    {
        return (shape != Settings.EMPTY_STRING)
            ?   shape
            :   NotePane.Get_current_shape();
    }
    //}}}
    // get_wh_gap {{{
    private int get_wh_gap()
    {
        String effective_shape  = get_effective_shape();
        int wh_gap;
        switch(effective_shape)
        {
            case NotePane.SHAPE_TAG_SQUARE:
            case NotePane.SHAPE_TAG_TILE  : wh_gap =  4;
            default                       : wh_gap =  2; // will be devided by 2
        }
        return wh_gap;
    }
    //}}}
    // may_see_through {{{
    private boolean may_see_through()
    {
        String effective_shape  = get_effective_shape();
        switch(effective_shape)
        {
            case NotePane.SHAPE_TAG_CIRCLE:               // YES
            case NotePane.SHAPE_TAG_SATIN :               // YES
            case NotePane.SHAPE_TAG_MARK_L:               // YES
            case NotePane.SHAPE_TAG_MARK_P:               // YES
            case NotePane.SHAPE_TAG_PADD_R:               // YES
            case NotePane.SHAPE_TAG_PADD_L:               // YES
            case NotePane.SHAPE_TAG_RING  :               // YES
            case NotePane.SHAPE_TAG_SQUARE: return  true; // MARGINS
            case NotePane.SHAPE_TAG_TILE  : return false; // (padding is part of view's background)
            case NotePane.SHAPE_TAG_SCROLL: return  true; //false; // Todo: (cannot keep it opaque... why?)
            default:
            case NotePane.SHAPE_TAG_ONEDGE: return  true; //has_some_round_corner(); // MAYBE
        }
    }
    //}}}
    // round corners {{{

    public void clear_round_corners()
    {
        tl = true;              /*    tl       (tr)    */
        lt = true;              /* lt -ttttttttt- (rt) */
        bl = true;              /*    l---------r      */
        rt = true;              /*    l---------r      */
        tr = true;              /*    l---------r      */
        lb = true;              /*    l---------r      */
        br = true;              /* lb -bbbbbbbbb- (rb) */
        rb = true;              /*    bl       (br)    */
    }

    public void set_round_corners()
    {
        tl = false;              /*    tl       (tr)    */
        lt = false;              /* lt -ttttttttt- (rt) */
        bl = false;              /*    l---------r      */
        rt = false;              /*    l---------r      */
        tr = false;              /*    l---------r      */
        lb = false;              /*    l---------r      */
        br = false;              /* lb -bbbbbbbbb- (rb) */
        rb = false;              /*    bl       (br)    */
    }

    public  void set_round_corners_right()
    {
        /**/  tl =  true;       /*    tl       (tr)    */ tr  = false;
        /**/ lt  =  true;       /* lt -ttttttttt- (rt) */  rt = false;
        /**/                    /*    l---------r      */
        /**/                    /*    l---------r      */
        /**/                    /*    l---------r      */
        /**/                    /*    l---------r      */
        /**/ lb  =  true;       /* lb -bbbbbbbbb- (rb) */  rb = false;
        /**/  bl =  true;       /*    bl       (br)    */ br  = false;
    }

    public  void set_round_corners_left ()
    {
        /**/  tl = false;       /*    tl       (tr)    */ tr  =  true;
        /**/ lt  = false;       /* lt -ttttttttt- (rt) */  rt =  true;
        /**/                    /*    l---------r      */
        /**/                    /*    l---------r      */
        /**/                    /*    l---------r      */
        /**/                    /*    l---------r      */
        /**/ lb  = false;       /* lb -bbbbbbbbb- (rb) */  rb =  true;
        /**/  bl = false;       /*    bl       (br)    */ br  =  true;
    }

    //------------------------------------------------ (NORTH-WEST) || (SOUTH-WEST) || (NORTH-EAST) || (SOUTH-EAST)
    public boolean is_neighbor_phobic()
    {
        String effective_shape  = get_effective_shape();
        if(    effective_shape == NotePane.SHAPE_TAG_CIRCLE) return  true; // return early on first match
        if(    effective_shape == NotePane.SHAPE_TAG_RING  ) return  true; // ..
        if(    effective_shape == NotePane.SHAPE_TAG_SATIN ) return  true; // ..
        if(    effective_shape == NotePane.SHAPE_TAG_MARK_L) return  true; // ..
        if(    effective_shape == NotePane.SHAPE_TAG_MARK_P) return  true; // ..
        /*................................................*/ return false; // ..
    }
    public  boolean binds_with_neighbors   () { if( is_neighbor_phobic() ) return false;
        /*...................................*/ return ( tl ||  lt  ||   bl ||  lb  ||   rt ||  tr  ||   br ||  rb);
    }
    private boolean has_some_round_corner  () { return (!tl || !lt) || (!bl || !lb) || (!rt || !tr) || (!br || !rb); }

    private boolean has_left_round_corner  () { return (!tl && !lt) || (!bl && !lb)                                ; }
    private boolean has_right_round_corner () { return                                 (!rt || !tr) || (!br || !rb); }

    private boolean has_top_round_corner   () { return (!tl || !lt) ||                 (!rt || !tr)                ; }
    private boolean has_bottom_round_corner() { return                 (!bl || !lb) ||                 (!br || !rb); }

    //}}}

    // setElevation {{{
    public void setElevation(float elevation)
    {
        if( ELEVATION_SUPPORTED ) super     .setElevation(      elevation);
        else                      ViewCompat.setElevation(this, elevation);
        //bringToFront();
    }
    //}}}
    // lockElevation {{{
    private float lockedElevation = -1f;
    public void lockElevation(float elevation)
    {
        lockedElevation = elevation;
        setElevation( elevation );
        if(elevation == 0) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                setOutlineProvider(null);
        }
    }
    //}}}

    // setActive setEnabled setBlurred {{{
    // setActive {{{
    private boolean _active = true;
    public void setActive(boolean active)
    {
        _active = active;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) invalidateOutline();
        invalidate();
    }
    //}}}
    // isActive {{{
    public boolean isActive()
    {
        return _active;
    }
    //}}}

    // setEnabled {{{
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(  enabled );
        //setBlurred      ( !enabled ); // do not display text content
        invalidate();
    }
    //}}}

    // setBlurredWithColor {{{
    public void setBlurredWithColor(@ColorInt int color)
    {
        blurred_color = color;
        if( may_see_through() ) invalidate();
        else                     super.setBackgroundColor( blurred_color );
    }
    //}}}
    // setBlurredLighter {{{
    public void setBlurredLighter()
    {
        int color = ColorPalette.GetColorLighter(instance_bg_color, .05);
        setBlurredWithColor( color );
    }
    //}}}
    // setBlurredDarker {{{
    public void setBlurredDarker()
    {
        int color = ColorPalette.GetColorDarker(instance_bg_color, .09);
        setBlurredWithColor( color );
    }
    //}}}
    // setBlurred {{{
    public void setBlurred(boolean state)
    {
        if(state) {
            if(ColorPalette.GetBrightness( instance_bg_color ) > 192)
                setBlurredDarker();
            else
                setBlurredLighter();
        }
        else {
            blurred_color = -1;
            if( may_see_through() )   invalidate();
            else                       super.setBackgroundColor( instance_bg_color );
        }
    }
    //}}}
    // isBlurred {{{
    public boolean isBlurred()
    {
        return (blurred_color != -1);
    }
    //}}}
    //}}}

    // setTextNoFit {{{
    public void setTextNoFit(String _text)
    {
        super.setText( _text );
    }
    //}}}
    // setText {{{
    private static final String FIRST_LINE_PREFIX = "_-[";
    public void setText(String _text)
    {
        // text_vertical {{{
        boolean text_vertical = Is_text_vertical( _text );
        //  if(len < 2) orientation = ORIENTATION_VERTICAL; // all the way down
        // }}}
        // has_first_line_prefix {{{
        boolean has_first_line_prefix = false;
        if( !text_vertical )
        {
            if(_text.length() > 0)
            {
                if(_text.charAt(0) == '_') {
                    has_first_line_prefix = true;
                    _text = _text.substring(1);   // drop a leading underscore
                }
                else {
                    has_first_line_prefix = (FIRST_LINE_PREFIX.indexOf( _text.charAt(0) ) >= 0);
                }
            }

        }
        //}}}
        // has_multi_line_prefix {{{
        boolean has_multi_line_prefix = false;
        if( !has_first_line_prefix )
        {
            // [leading-dash] or [leading-spaces]
            has_multi_line_prefix = (_text.indexOf ("\n-" ) > 0) || (_text.indexOf ("\n " ) > 0) || (_text.indexOf ("\n..." ) > 0);
            if( !has_multi_line_prefix && (_text.length() > 2))
            {
                // [leading-2-chars]
                String prefix   =  _text.substring(0,2);
                has_multi_line_prefix = (_text.indexOf ("\n"+prefix) > 0);
            }
        }
        // }}}
        // has_more_than_3_lines {{{
        boolean has_more_than_3_lines = false;
        if(!has_first_line_prefix && !has_multi_line_prefix)
        {
            int idx1 = _text.indexOf("\n");
            int idx2 = (idx1 < 0) ? -1 : _text.indexOf("\n", idx1+1);
            int idx3 = (idx2 < 0) ? -1 : _text.indexOf("\n", idx2+1);

            has_more_than_3_lines = (idx3 > 0);
        }
        // }}}
        // alignment {{{
/* // MON {{{
        if(Settings.M) {
            if(text_vertical        ) Settings.MON(TAG_LOG, "        text_vertical ["+Settings.ellipsis(_text, 32)+"]");
            if(has_first_line_prefix) Settings.MON(TAG_LOG, "has_first_line_prefix ["+Settings.ellipsis(_text, 32)+"]");
            if(has_multi_line_prefix) Settings.MON(TAG_LOG, "has_multi_line_prefix ["+Settings.ellipsis(_text, 32)+"]");
            if(has_more_than_3_lines) Settings.MON(TAG_LOG, "has_more_than_3_lines ["+Settings.ellipsis(_text, 32)+"]");
        }
*/
// }}}
        if( text_vertical ) {
            _setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        }
        else if(   has_first_line_prefix
                || has_multi_line_prefix
                || has_more_than_3_lines
          ) {
            _setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
          }
        else {
            _setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        }

        //}}}
        // overflow {{{
    //  if(_text.length() < 32     ) may_overflow = false;
        if( !has_more_than_3_lines ) may_overflow = false;

        //}}}
        // invalidate {{{
        fit_ts_on_vh = 0; // (i.e. induce layout) 

        //}}}
        super.setText( _text );
    }
    //}}}
     // fitText {{{
    public void fitText()
    {
        fit_ts_on_vh = Get_Fit_TextSize_On_ViewHeight( this );
        String effective_shape  = get_effective_shape();
        if(    (effective_shape == NotePane.SHAPE_TAG_CIRCLE) // do not write in corners
            || (effective_shape == NotePane.SHAPE_TAG_RING  ) // ............ in corners
            || (effective_shape == NotePane.SHAPE_TAG_SQUARE) // ............ in padding
            || (effective_shape == NotePane.SHAPE_TAG_TILE  ) // ............ in padding
          ) {
            //fit_ts_on_vh *= 0.8;
            _setGravity( Gravity.CENTER );
            //bringToFront(); //XXX
          }
    }
    public void fitText(int vw, int vh)
    {
        fit_ts_on_vh = Get_Fit_TextSize_On_ViewHeight(this, vw, vh);
    }
    // }}}
    // toString {{{
    @Override public String toString()
    {
        Object o = getTag();

        String s
        = (o instanceof String  ) ?    (String)o    // NpButton [ tag   ]
        : (o instanceof NotePane) ? o.toString()    // NotePane [ name  ]
        :                   getText().toString();   // NotePane [ label ]

        return Settings.ellipsis(s.replace("\n","") , 32);
    }
    //}}}
    // toStringCorners {{{
    public String toStringCorners()
    {
        // [lt tl tr rt] [lb bl br rb] ... [ALL] corners occupied
        // [.. .. tr rt] [lb bl .. ..] ... [RIGHT-TOP] and [RIGHT-BOTTOM] corners occupied

        // top
        String s_lt = lt ? "lt" : "--";
        String s_tl = tl ? "tl" : "--";
        String s_tr = tr ? "tr" : "--";
        String s_rt = rt ? "rt" : "--";

        // bot
        String s_lb = lb ? "lb" : "--";
        String s_bl = bl ? "bl" : "--";
        String s_br = br ? "br" : "--";
        String s_rb = rb ? "rb" : "--";

        return  "["+s_lt+" "+s_tl+" "+s_tr+" "+s_rt+"] ["+s_lb+" "+s_bl+" "+s_br+" "+s_rb+"] "+toString();
    }
    //}}}
    // description {{{
    public String description()
    {
        return  String.format("NpButton: (CURRENT) xy=[%4d %4d] wh=[%4d %4d] scale=[%2.1f] %s"
                ,                                      (int)getX()
                ,                                          (int)getY()
                ,                                                   getWidth()
                ,                                                       getHeight()
                ,                                                                   getScaleX()
                , this.toString());
    }
    //}}}
    // description_saved {{{
    public String description_saved()
    {
        return (flp_saved == null)
            ?   "NpButton: NOT SAVED YET"
            :   String.format("NpButton: ( SAVED ) xy=[%4d %4d] wh=[%4d %4d] scale=[%2.1f] %s"
                    ,                                  (int)X_saved
                    ,                                      (int)Y_saved
                    ,                                               flp_saved.width
                    ,                                                   flp_saved.height
                    ,                                                               scaleX_saved
                    , this.toString());
    }
    //}}}
    // description_from {{{
    public String description_from()
    {
        return (flp_saved == null)
            ?   "NpButton: NOT SAVED YET"
            :   String.format("NpButton: (  FROM ) xy=[%4d %4d] scale=[%2.1f] xr=[%2.1f] yr=[%2.1f] zr=[%2.1f] %s"
                    ,                                  (int)x_from
                    ,                                      (int)y_from
                    ,                                                  s_from
                    ,                                                             xrfrom
                    ,                                                                        yrfrom
                    ,                                                                                   zrfrom
                    , this.toString());
    }
    //}}}
    // description_to   {{{
    public String description_to  ()
    {
        return (flp_saved == null)
            ?   "NpButton: NOT SAVED YET"
            :   String.format("NpButton: (    TO ) xy=[%4d %4d] scale=[%2.1f] xr=[%2.1f] yr=[%2.1f] zr=[%2.1f] %s"
                    ,                                  (int)x_to
                    ,                                      (int)y_to
                    ,                                                  s_to
                    ,                                                             xrto
                    ,                                                                        yrto
                    ,                                                                                   zrto
                    , this.toString());
    }
    //}}}

    // release {{{
    private float tss         = 1;          // [Text Size Shadow]
    private int   shadowColor = Color.BLACK;

    public  void release() { _release_fts( getTextSize() ); }

    // _release_fts .. (Font Text Size) {{{
    private void _release_fts(float fts)
    {
        if( !SHADOWLAYER_SUPPORTED ) return;

        // recompute [OUTLINE] .. f(shape)
        if(Settings.GUI_STYLE_HAS_CHANGED) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                invalidateOutline();
        }

        // [shadow parameters dependencies]
        boolean         small_text = (fts <= (6*Settings.FONT_SIZE_MIN)); // .. (6*4)
        boolean making_things_flat = !Settings.is_GUI_STYLE_ROUND();
        boolean  cornerless_shaped = (shape == NotePane.SHAPE_TAG_CIRCLE) || (shape == NotePane.SHAPE_TAG_RING  );
        boolean     curved_surface = Settings.is_GUI_STYLE_TILE();

    //  if(                         small_text) tss = 0; else tss  = 2     ; // shadow size ..  (small text shadow is messy)
        if(                         small_text) tss = 0; else tss  = fts/10; // shadow size ..  (small text shadow is messy)
        if(                 making_things_flat)               tss *= 3     ; //       extra .. f(flat surface)
        if(curved_surface && cornerless_shaped)               tss *= 2     ; //       extra .. f(no corners)

        tss = Math.min(tss, 12); // some kind of magic []

        // [BALANCE] .. f(color brightness size)
        if((tss != 0) && (ColorPalette.GetBrightness( getCurrentTextColor() ) < 128))
    //  if((tss != 0) && (ColorPalette.GetBrightness( getBackgroundColor()  ) > 192))
    //  if((tss != 0) && (ColorPalette.GetBrightness( getBackgroundColor()  ) > 128))
        {
            shadowColor = Color.WHITE;
            tss /= 3; // white stands out too much
            setShadowLayer(  tss, -tss, -tss, shadowColor);
        }
        else {
            shadowColor = Color.DKGRAY;
            setShadowLayer(2*tss,  tss,  tss, shadowColor); // radius, dx, dy, shadowColor
        }
    }
    //}}}
    //}}}
    // press {{{
    public void press()
    {
        if( !SHADOWLAYER_SUPPORTED ) return;

        setShadowLayer(25,  0,  0, Color.RED);
    }
    //}}}

    // Suspend-Resume onDraw Layout {{{
    private static boolean                SuspendedLayout = false;
    public  static void SuspendLayout() { SuspendedLayout =  true; }
    public  static void ResumeLayout () { SuspendedLayout = false; }
    //}}}
    // set_shape_circle_RectF {{{
    private final RectF mRectF = new RectF();
    private void  set_shape_circle_RectF(int w, int h, int min, int max)
    {
        // BIG CIRCLE
    //  float x = (w > h) ?            0 : -(max-min)/2;
    //  float y = (w > h) ? -(max-min)/2 : 0;
    //  mRectF.set(   x,   y, x+max, y+max); // left, top, right, bottom

        // SMALL CIRCLE
        float x = (w > h) ? (max-min)/2 : 0;
        float y = (w > h) ?           0 : (max-min)/2;
        mRectF.set(   x,   y, x+min, y+min); // left, top, right, bottom

    }
    //}}}
    // SHAPE VOLUME {{{

//  float[] SHAPE_TAG_DOME_STOPSARRAY   = { .0f, .01f,                             .90f,             1f };
//  float[] SHAPE_TAG_BADGE_STOPSARRAY  = {                                              .92f, .98f, 1f };
//  float[] SHAPE_TAG_BADGE_STOPSARRAY  = { .2f,                                         .90f, .98f, 1f };
//  float[] SHAPE_TAG_BADGE_STOPSARRAY  = { .4f,                                   .92f, .95f, .97f, 1f };
//  float[] SHAPE_TAG_BADGE_STOPSARRAY  = { .4f,                                   .90f, .92f, .95f, 1f };
//  float[] SHAPE_TAG_BADGE_STOPSARRAY  = {  0f,                 .30f, .60f,       .92f,             1f };
//  float[] SHAPE_TAG_EDGE0_STOPSARRAY  = {  0f, .02f, .10f,                                         1f }; //  LEFT-ONLY
//////////////////TAG_PLATE_STOPSARRAY  = {                                        .92f,       .98f, 1f };

    //...................................... START_________      __MIDDLE__        ________________STOP
    final float[] SHAPE_TAG_DOME_STOPSARRAY   = {  0f,                                   .90f,       1f }; // [ x3 ]  
    final float[] SHAPE_TAG_RING_STOPSARRAY   = {  0f,                       .80f,                   1f }; // [ x3 ]
    final float[] SHAPE_TAG_SATIN_STOPSARRAY  = {  0f,           .20f,                               1f }; // [ x3 ]
    final float[] SHAPE_TAG_MARK_L_STOPSARRAY = {  0f,                 .50f,                         1f }; // [ x3 ]
    final float[] SHAPE_TAG_MARK_P_STOPSARRAY = {  0f,           .30f,                               1f }; // [ x3 ]
    final float[] SHAPE_TAG_CUP_STOPSARRAY    = {  0f,                       .80f,                   1f }; // [ x3 ]  
    final float[] SHAPE_TAG_SPHERE_STOPSARRAY = null;

    final float[] SHAPE_TAG_EDGES_STOPSARRAY  = {  0f, .02f, .10f,           .80f,       .90f,       1f }; // [ x6 ]  
    final float[] SHAPE_TAG_EDGE0_STOPSARRAY  = {  0f, .02f, .10f,                                   1f }; // [ x4 ]   LEFT-ONLY
    final float[] SHAPE_TAG_EDGE1_STOPSARRAY  = {  0f,                       .85f,       .96f,       1f }; // [ x4 ]  RIGHT-ONLY
    final float[] SHAPE_TAG_BADGE_STOPSARRAY  = {  0f,       .10f,           .80f,       .95f,       1f }; // [ x5 ]  

    //}}}

    // XXX
    // onDraw {{{
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@ onDraw @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas)
    {
        // {{{
        if(SuspendedLayout) return;//{ super.onDraw( canvas ); return; }
        if(mPaint == null) _init();
        mPaint.setShader(null);
        path.reset();

        //}}}
        // [HACK] Path Too Large To Be Rendered To a Texture {{{

        // SHAPE (instance or from current Settings)
        String effective_shape  = get_effective_shape();

        // SQUARE GAP or TILE PADDING
        int wh_gap = get_wh_gap();
        int      h = getHeight() - wh_gap;
        int      w = getWidth () - wh_gap;

        // HUGE SHAPE .. NOT FILLED
        boolean toobig = false;
        if(w > Settings.GRAPHIC_PATH_W_MAX) { /* w = Settings.GRAPHIC_PATH_W_MAX; */ toobig = true; }
        if(h > Settings.GRAPHIC_PATH_H_MAX) { /* h = Settings.GRAPHIC_PATH_H_MAX; */ toobig = true; }
        if( toobig ) {
            super.onDraw( canvas ); // RENDER TEXT CONTENT ONLY
            return;
        }
        //}}}

        // SHADER {{{
        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@ SHADER @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        float      r = _get_scaled_radius();
        // COLORS {{{
        int bg_color = getBackgroundColor();

        int  color_1 = LIGHT_COLOR;
        int  color_2 = bg_color;
        int  color_3;//ColorPalette.GetColorDarker(color_2, .08);
        int  color_4 = DARK_COLOR;
        int  color_5 = LIGHT_COLOR;
        int  color_6 = LIGHT_COLOR;
        // }}}

        // DEPENDENCIES {{{
        boolean Settings_is_GUI_STYLE_SATIN  = Settings.is_GUI_STYLE_SATIN ();
        boolean Settings_is_GUI_STYLE_ONEDGE = Settings.is_GUI_STYLE_ONEDGE();
        boolean Settings_is_GUI_STYLE_ROUND  = Settings.is_GUI_STYLE_ROUND ();
        boolean Settings_is_GUI_STYLE_TILE   = Settings.is_GUI_STYLE_TILE  ();

        boolean may_see_through_corners
            =  (effective_shape == NotePane.SHAPE_TAG_CIRCLE)
            || (effective_shape == NotePane.SHAPE_TAG_RING  )
            || (effective_shape == NotePane.SHAPE_TAG_SATIN )
            || (effective_shape == NotePane.SHAPE_TAG_MARK_L)
            || (effective_shape == NotePane.SHAPE_TAG_MARK_P);

        boolean drawing_a_scrollbar_frame
            =  (effective_shape == NotePane.SHAPE_TAG_SCROLL)
            && (       (w_min > 0)                            // THUMB_WIDTH MIN has effectively been set
                    || (w     > Settings.SCROLLBAR_SHAPE_W)); // THUMB_WIDTH MAX exceeded

        //}}}

        // [EFFECTIVE_SHAPE] .. f(tag_shape .. defaulting to gui_style)

        // GUI_STYLE_ONEDGE 
        // GUI_STYLE_ROUND  
        // GUI_STYLE_SQUARE 
        // GUI_STYLE_TILE   

        // [     ROUND] TAG_CIRCLE TAG_RING   TAG_SATIN
        // [SEMI_ROUND] TAG_ONEDGE TAG_PADD_R TAG_MARK
        // [ NOT_ROUND] TAG_SCROLL TAG_SQUARE TAG_TILE

        // CORNERS / NO-CORNERS
        if( may_see_through_corners ) {
            // COMPACT (CIRCLE) (SQUARE) {{{
            int min = (w > h) ? h : w;
            int max = (w > h) ? w : h;
            float elongation = (float)max / (float)min;
            if(elongation < 1.5)
            {
                // {{{
                int     centerX;
                int     centerY;
                float   radius = r; // FIXME: IllegalArgumentException: radius must be > 0 (line 906 setShader RadialGradient)

                //}}}
                // [radius] .. f(ONEDGE BADGE) {{{
                if(          Settings_is_GUI_STYLE_ONEDGE
                        && !(effective_shape == NotePane.SHAPE_TAG_RING  )
                        && !(effective_shape == NotePane.SHAPE_TAG_MARK_L)
                        && !(effective_shape == NotePane.SHAPE_TAG_MARK_P)
                        ) {
                    // [radius] .. f(BADGE) {{{
                    color_1 = bg_color;
                    color_2 = ColorPalette.GetColorDarker (color_1, 0.05);
                    color_3 = ColorPalette.GetColorLighter(color_1, 0.05);
                    color_4 = ColorPalette.GetColorDarker (color_1, 0.05);
                    color_5 = ColorPalette.GetColorLighter(color_1, 0.25);
                    centerX = (int)  (w * 0.505);
                    centerY = (int)  (h * 0.505);
                    radius  = (float)(r * 0.950);

                    //}}}
                    // [stopsArray] .. f(BADGE) {{{
                    float[] stopsArray = SHAPE_TAG_BADGE_STOPSARRAY;

                    //}}}
                    // [setShader] {{{
                    int[] colorArray = { color_1, color_2, color_3, color_4, color_5 };
                    mPaint.setShader(new RadialGradient(centerX, centerY, radius, colorArray, stopsArray, Shader.TileMode.CLAMP));

                    //}}}
                }
                // }}}
                else {
                    // [SATIN] or [CIRCLE + STYLE_SATIN] {{{
                    if(         (effective_shape == NotePane.SHAPE_TAG_SATIN )
                            || ((effective_shape == NotePane.SHAPE_TAG_CIRCLE) && Settings_is_GUI_STYLE_SATIN))
                    {
                        color_1  = bg_color    & 0xFFFFFFFF;
                        color_2  = bg_color    & 0xD0606060;
                        color_3  = bg_color    & 0xD0A0A0A0;

                        int[] colorArray = { color_1, color_2, color_3 };
                        float[] stopsArray = SHAPE_TAG_SATIN_STOPSARRAY;

                        float x0, x1, y0, y1;
                        x0 = w * 0.5f;   x1 = w; // center to right
                        y0 = h * 0.5f;   y1 = h; // middle to bottom
                        Shader shader1 = new LinearGradient(x0, y0, x1, y1, colorArray, stopsArray, Shader.TileMode.MIRROR);
                        mPaint.setShader( shader1 );

/*
                        x0 = w * 0.5f;   x1 = w;
                        y0 = h * 0.5f;   y1 = h;
                        Shader shader2 = new LinearGradient(x0, y0, x1, y1, colorArray, stopsArray, Shader.TileMode.MIRROR);

                        ComposeShader composeShader = new ComposeShader(shader1, shader2, PorterDuff.Mode.DST_OVER);
                        mPaint.setShader( composeShader );
*/
/*
:!start explorer "https://chiuki.github.io/android-shaders-filters/#/24"
*/
                    }
                    //}}}
                    else {
                        // [radius] .. f(RING) {{{
                        if(effective_shape == NotePane.SHAPE_TAG_RING)
                        {
                            int fg_color = getCurrentTextColor();
                            color_1 = bg_color    & 0xF0FFFFFF;
                            color_2 = bg_color    & 0xA0FFFFFF;
                            color_3 = fg_color    & 0xF0FFFFFF;
                            centerX = (int)  (w * 0.50);
                            centerY = (int)  (h * 0.50);
                            radius  = (float)(r * 0.95);
                        }
                        //}}}
                        // [radius] .. f(TILE or ROUND) {{{
                        else if(Settings_is_GUI_STYLE_TILE || Settings_is_GUI_STYLE_ROUND)
                        {
                            color_1 =                              bg_color     ;
                            color_2 = ColorPalette.GetColorDarker (bg_color, .4);
                            color_3 = ColorPalette.GetColorLighter(bg_color, .1);
                            centerX = w / 3   ;
                            centerY = h / 3   ;
                            radius  = r * 1.8f;
                        }
                        // }}}
                        // [radius] .. f(SQUARE FLAT CUP) {{{
                        else {
                            color_1 = bg_color; //ColorPalette.GetColorDarker (bg_color, .4 );
                            color_2 = bg_color; //ColorPalette.GetColorDarker (bg_color, .2 );
                            color_3 = bg_color; //                             bg_color      ;
                            centerX = w / 5   ;
                            centerY = h / 5   ;
                            radius  = r * 1.8f;
                        }
                        // }}}
                        // [stopsArray] .. f(ROUND TILE RING] {{{
                        float stopsArray[];
                        if     ( (effective_shape == NotePane.SHAPE_TAG_RING) ) stopsArray = SHAPE_TAG_RING_STOPSARRAY;
                        else if(  Settings_is_GUI_STYLE_ROUND                 ) stopsArray = SHAPE_TAG_SPHERE_STOPSARRAY;
                        else if(  Settings_is_GUI_STYLE_TILE                  ) stopsArray = SHAPE_TAG_DOME_STOPSARRAY;
                        else                                                    stopsArray = SHAPE_TAG_CUP_STOPSARRAY;

                        //}}}
                        // [setShader] {{{
                        int[] colorArray = { color_1, color_2, color_3 };

//try {
                        mPaint.setShader(new RadialGradient(centerX, centerY,       radius, colorArray, stopsArray, Shader.TileMode.MIRROR));
//}
//catch(Exception ex) {
//  System.err.println("r.....=["+r     +"]");
//  System.err.println( this.toString() );
//  System.err.println( this.description() );
//  System.err.println(ex);
//  System.err.println("radius=["+radius+"]");
//  ex.printStackTrace();
//}

                        //}}}
                    }
                }
            }
            //}}}
            // [MARK] {{{
            else if(   (effective_shape == NotePane.SHAPE_TAG_MARK_L)
                    || (effective_shape == NotePane.SHAPE_TAG_MARK_P))
            {
//if(effective_shape == NotePane.SHAPE_TAG_MARK_L) { System.err.println( this.description() ); }
//if(effective_shape == NotePane.SHAPE_TAG_MARK_P) { System.err.println( this.description() ); }
                color_1  = bg_color    & 0xFFFFFFFF;
                color_2  = bg_color    & 0x40000000;
                color_3  = Color.WHITE & 0xD0FFFFFF;

                int[]   colorArray = { color_1, color_2, color_3 };
                float[] stopsArray = (effective_shape == NotePane.SHAPE_TAG_MARK_L)
                    ?                                             SHAPE_TAG_MARK_L_STOPSARRAY
                    :                                             SHAPE_TAG_MARK_P_STOPSARRAY;

                float x0, x1, y0, y1;
                if(effective_shape == NotePane.SHAPE_TAG_MARK_L)
                {
                    x0 = w/2;   x1 = w *.6f; // left to right
                    y0 = 0  ;   y1 = h * 1f; // top to bottom
                }
                else {
                    x0 = w/2;   x1 = w     ; // left to right
                    y0 = h/2;   y1 = h     ; // top to bottom
                }

            //  Shader.TileMode mTileMode = (effective_shape == NotePane.SHAPE_TAG_MARK_L) ?  Shader.TileMode.CLAMP :  Shader.TileMode.MIRROR;
                Shader.TileMode mTileMode = Shader.TileMode.MIRROR;

                Shader shader1 = new LinearGradient(x0, y0, x1, y1, colorArray, stopsArray, mTileMode);
                mPaint.setShader( shader1 );
            }
            //}}}
            // OVALE {{{
            else {
                // [setShader] .. f( ROUND) {{{
                if( Settings_is_GUI_STYLE_ROUND ) {
                    int color_light = bg_color;
                    int color_dark  = DARK_COLOR;
                    if(w > h) mPaint.setShader(new LinearGradient( 0f,    0f,  0f, h*2f, color_light, color_dark, Shader.TileMode.CLAMP));
                    else      mPaint.setShader(new LinearGradient( 0f,    0f,w*2f,   0f, color_light, color_dark, Shader.TileMode.CLAMP));
                } // }}}
                // [setShader] .. f(!ROUND) {{{
                else {
                    int color_light = ColorPalette.GetColorLighter(bg_color ,.2);
                    int color_dark  = ColorPalette.GetColorDarker (bg_color ,.2);
                    if(w > h) mPaint.setShader(new LinearGradient( 0f,    0f,  0f, h*1f, color_dark , color_light, Shader.TileMode.CLAMP)); //  TOP -> DOWN
                    else      mPaint.setShader(new LinearGradient( 0f,    0f,w*1f,   0f, color_dark , color_light, Shader.TileMode.CLAMP)); // LEFT -> RIGHT
                } // }}}
/* // {{{
                    int color_2    = bg_color;
                    int color_3    = ColorPalette.GetColorDarker (color_2,   .08);
                    int color_4    = ColorPalette.GetColorLighter(color_2,   .09);
                    int centerX = (int)  (w * 0.505);
                    int centerY = (int)  (h * 0.505);
                    int[] colorArray = { color_2, color_3, color_4 };
                    mPaint.setShader(new SweepGradient(centerX, centerY, colorArray, SHAPE_TAG_PLATE_STOPSARRAY));
*/ // }}}
            }
            //}}}
        }
        else {
            // SHAPE_TAG_PADD_R SHAPE_TAG_PADD_L {{{
            if(        (effective_shape == NotePane.SHAPE_TAG_PADD_R)
                    || (effective_shape == NotePane.SHAPE_TAG_PADD_L)
            ) {
                int color_light = bg_color;
                int color_dark  = bg_color & DIMM_COLOR;
            //  int color_dark  = DARK_COLOR;
                mPaint.setShader(           new LinearGradient( 0f,    0f,w*1f, 0f, color_dark , color_light, Shader.TileMode.CLAMP));
            } // }}}
            // SCROLLBAR {{{
            else if( drawing_a_scrollbar_frame ) {
                // [left] [right] [body] {{{
                float left            = 0;
                float right           = w;
            //  float body            = Math.min(w*4/5, w_min);                  // up to 80% .. (clipped to w_min)
                float body            =                 w_min ;                  // colored surface

                //}}}
                // [colors] {{{
                int white_alpha       = (w_max <= 0) ?  0x40 : (0xFF * w/w_max); // a large bit of white frame

                int black_left_alpha  = (w_max <= 0) ?  0x40 : (0xAA * w/w_max);
                int black_right_alpha =                 0x20;

                int tint_right_alpha  =                 0xFF;
                int tint_left_alpha   =                 0x80;

                int inner_mask        = 0x666666 | white_alpha       << 24;

                int black_mask_end    = 0x222222 | black_left_alpha  << 24;     // a tiny bit of bg_color
                int black_mask_start  = 0x222222 | black_right_alpha << 24;     // a tiny bit of bg_color

                int tint_mask_start   = 0xffffff | tint_right_alpha  << 24;
                int tint_mask_end     = 0xffffff | tint_left_alpha   << 24;

                //}}}
                // {{{
                float start       ;
                float end         ;
                int   color_start ;
                int   color_end   ;

                //}}}
                // WHITE [OUTER_FRAME] {{{
                start       = at_left  ? left      : right     ;
                end         = at_left  ? left+body : right-body; // keep clamped up to left
                if(action_down_in_margin) color_start = Color.BLUE  & 0x20ffffff; // blueprint .. (layout in progress)
                else                      color_start = Color.WHITE & inner_mask;
                color_end   = color_start;

                if( at_left ) mRectF.set(left+body+1, 0, right       , h); // [tint-area] RIGHT_OUTER_FRAME
                else          mRectF.set(left       , 0, right-body-1, h); // LEFT_OUTER_FRAME [tint-area]
                path.addRect(mRectF, Path.Direction.CW);
                mPaint.setShader( new LinearGradient(start,0 , end,0, color_start, color_end, Shader.TileMode.CLAMP));
                canvas.drawPath(path, mPaint);
                path.reset();

                // }}}
                // BLACK [INNER_FRAME] (border inset) {{{
                start       = at_left  ? left+body : right-body;
                end         = at_left  ? right     : left      ;
                if(action_down_in_margin) color_start = Color.BLUE  & 0x80ffffff; // blueprint .. (layout in progress)
                else                      color_start = bg_color & black_mask_start;
                if(action_down_in_margin) color_end   = color_start;
                else                      color_end   = bg_color & black_mask_end;

                int       b = Settings.FS_SELECT_BORDER;
                if( at_left ) mRectF.set(left+body+b, b, right     -b, h-b); // [tint-area] RIGHT_INNER_FRAME
                else          mRectF.set(left     +b, b, right-body-b, h-b); // LEFT_INNER_FRAME [tint-area]
                path.addRect(mRectF, Path.Direction.CW);
                mPaint.setShader( new LinearGradient(start,0 , end,0, color_start, color_end, Shader.TileMode.CLAMP));
                canvas.drawPath(path, mPaint);
                path.reset();

                // }}}
                // TINT {{{
                start       = at_left  ? left      : right     ;
                end         = at_left  ? left+body : right-body;
                color_start = bg_color & tint_mask_start;
                color_end   = bg_color & tint_mask_end;

                if( at_left ) mRectF.set(left      , 0, left+body , h);  // LEFT_FRAMES <== [tint-area]
                else          mRectF.set(right-body, 0, right     , h); //  [tint-area] ==> RIGHT_FRAMES
                path.addRect(mRectF, Path.Direction.CW);
                mPaint.setShader( new LinearGradient(start,0 , end,0, color_start, color_end, Shader.TileMode.CLAMP));
                canvas.drawPath(path, mPaint);
                path.reset();

                // }}}
            } // }}}
            // [TILE] [ONEDGE] {{{
            else if((effective_shape == NotePane.SHAPE_TAG_TILE) || (effective_shape == NotePane.SHAPE_TAG_ONEDGE))
            {
                // [GUI_STYLE_ONEDGE] {{{
                if( Settings_is_GUI_STYLE_ONEDGE ) {
                    color_1 = bg_color;
                    color_2 = ColorPalette.GetColorLighter(color_1, 0.30);
                    color_3 = ColorPalette.GetColorDarker (color_1, 0.20);
                    color_4 = ColorPalette.GetColorLighter(color_1, 0.10);
                    color_5 = ColorPalette.GetColorLighter(color_1, 0.30);
                    color_6 = ColorPalette.GetColorDarker (color_1, 0.30);

                    boolean has_both  = false;
                    boolean has_start = false;
                    boolean has_end   = false;
                    if(w >= h) {
                        if     (has_left_round_corner () && has_right_round_corner  ()) has_both  = true;
                        else if(has_left_round_corner ()                              ) has_start = true;
                        else if(has_right_round_corner()                              ) has_end   = true;
                    }
                    else {
                        if     (has_top_round_corner   () && has_bottom_round_corner()) has_both  = true;
                        else if(has_top_round_corner   ()                             ) has_start = true;
                        else if(has_bottom_round_corner()                             ) has_end   = true;
                    }

                    float[] positions = null;
                    int[]  colorArray = null;
                    if( has_both ) {
                        positions  = SHAPE_TAG_EDGES_STOPSARRAY;
                        colorArray = new int[] { color_1, color_2, color_3, color_4, color_5, color_6 };
                    }
                    else if( has_start ) {
                        positions  = SHAPE_TAG_EDGE0_STOPSARRAY;
                        colorArray = new int[] { color_1, color_2, color_3,                   color_6 };
                    }
                    else if( has_end ) {
                        positions  = SHAPE_TAG_EDGE1_STOPSARRAY;
                        colorArray = new int[] { color_1,                   color_4, color_5, color_6 };
                    }

                    if(positions != null) {
                        if(w >  h) mPaint.setShader(new LinearGradient( 0f,    0f,  0f, h*1f, colorArray ,  positions, Shader.TileMode.CLAMP));
                        else       mPaint.setShader(new LinearGradient( 0f,    0f,w*1f,   0f, colorArray ,  positions, Shader.TileMode.CLAMP));
                    }

                }
                // }}}
                // [GUI_STYLE_ROUND] {{{
                else if( Settings_is_GUI_STYLE_ROUND  ) {
                    int color_light = ColorPalette.GetColorLighter(bg_color ,.2);
                    int color_dark  = ColorPalette.GetColorDarker (bg_color ,.2);
                    if(w >= h) mPaint.setShader(new LinearGradient( 0f,    0f,  0f, h*1f, color_dark , color_light, Shader.TileMode.CLAMP));
                    else       mPaint.setShader(new LinearGradient( 0f,    0f,w*1f,   0f, color_dark , color_light, Shader.TileMode.CLAMP));
                }
                // }}}
                // [GUI_STYLE_TILE] .. (default: GUI_STYLE_SQUARE) {{{
                else {
                    int color_light = bg_color;
                    int color_dark  = DARK_COLOR;
                    if(w >= h) mPaint.setShader(new LinearGradient( 0f,    0f,  0f, h*2f, color_light, color_dark, Shader.TileMode.CLAMP));
                    else       mPaint.setShader(new LinearGradient( 0f,    0f,w*2f,   0f, color_light, color_dark, Shader.TileMode.CLAMP));
                }
                // }}}
            }
            //}}}
            // SHAPE_TAG_SQUARE .. (no shader) {{{
            //}}}
        }
        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@ SHADER @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        //}}}

        // BLURRED or PHANTOM CONTENT .. (hide text with a blank blurred background) {{{
        boolean is_a_phantom_button = (mPaint.getAlpha() <= (0xff * Settings.OPACITY_HIDDEN_PERCENT/100F));

        if     (is_a_phantom_button) mPaint.setColor( Settings.PHANTOM_COLOR ); // has precedence
        else if(blurred_color != -1) mPaint.setColor( blurred_color          ); // over blurred

        // }}}
        // INACTIVE CONTENT {{{
        int current_alpha = mPaint.getAlpha();
        if( !_active )
            mPaint.setAlpha(isEnabled() ? 128 :  64);   // activation incentive...

        // }}}
        // TEXT SIZE .. fit with current geometry {{{
        float fts = getTextSize();
        if( !fixedTextSize ) {
            if(   fit_ts_on_vh == 0) fitText();                       // first time evaluation
            fts = fit_ts_on_vh * getMeasuredHeight();                 // magnified by height changed .. statring from last evaluation
            if(fts > Settings.FONT_SIZE_MAX) fts = Settings.FONT_SIZE_MAX;  // system issue with unreasonable large scaled text size
        }
        //}}}
        // OVERFLOW .. [keep font big enough to be readable] .. [only when not maximized] {{{
        boolean overflow = false;
        if((may_overflow) && (fts < (3*Settings.FONT_SIZE_MIN)))
        {
            fts = (3*Settings.FONT_SIZE_MIN);
            overflow = true;
        }

        if(getTextSize() != fts) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, fts);
            _release_fts( fts );
        }

        // }}}

        // CORNERS {{{
        //if( may_see_through() )
        {
            // CORNERS RADII {{{

            float[]  radii = { r,r , r,r , r,r , r,r };

            // }}}
            // JOIN TOUCHING BUTTONS {{{
/*
/\<\([tlbr]\)\{2\}\>
/\<\([tlbr]\)\{1,\}\>
/\<\(\wl\|l\w\)\>
/\<\(\wr\|r\w\)\>
*/
            //      X,Y      X,Y
            //     0,1       2,3    ...radii (clockwise)
            //      tl       tr
            //   lt -ttttttttt- rt
            //      l---------r   
            //      l---------r   
            //      l---------r   
            //      l---------r   
            //   lb -bbbbbbbbb- rb
            //      bl       br
            //     6,7       4,5    ...radii (clockwise)
            //      X,Y      X,Y

            // }}}
            if( may_see_through_corners )
            {
                // CIRCLE/RING {{{
                int          min = (w > h) ? h : w;
                int          max = (w > h) ? w : h;
                float elongation = (float)max / (float)min;
                if(elongation < 1.5)
                {
                    set_shape_circle_RectF(w, h, min, max);
                    path.addOval(mRectF, Path.Direction.CW);
                }
                // }}}
                // OR CAPROUND {{{
                else {
                    mRectF.set(0,0,w,h);
                    path.addRoundRect(mRectF, radii, Path.Direction.CW);
                }
                // }}}
            }
            else if(effective_shape == NotePane.SHAPE_TAG_ONEDGE)
            {
                // apply neighboring
                if(lt||tl) { radii[0]= 0; radii[1]= 0; } // top-left
                if(tr||rt) { radii[2]= 0; radii[3]= 0; } // top-right
                if(rb||br) { radii[4]= 0; radii[5]= 0; } // bot-right
                if(bl||lb) { radii[6]= 0; radii[7]= 0; } // bot-left

                // CAPROUND {{{
                mRectF.set(0,0,w,h);
                path.addRoundRect(mRectF, radii, Path.Direction.CW);
                // }}}
            }
            else {
                // PADD_R {{{
                if(        (effective_shape == NotePane.SHAPE_TAG_PADD_R)
                        || (effective_shape == NotePane.SHAPE_TAG_PADD_L)
                ) {
                    // apply neighboring
                    if(lt||tl) { radii[0]= 0; radii[1]= 0; } // top-left
                    if(tr||rt) { radii[2]= 0; radii[3]= 0; } // top-right
                    if(rb||br) { radii[4]= 0; radii[5]= 0; } // bot-right
                    if(bl||lb) { radii[6]= 0; radii[7]= 0; } // bot-left

                    mRectF.set(0,0,w,h);    // round rectangle
                    path.addRoundRect(mRectF, radii, Path.Direction.CW);
                }
                //}}}
                // INSET RECTANGLE over (may be TRANSPARENT) VIEW'S BACKGROUND (as enforced in may_see_through)
                else if(    drawing_a_scrollbar_frame
                        || (effective_shape == NotePane.SHAPE_TAG_SATIN )
                        || (effective_shape == NotePane.SHAPE_TAG_MARK_L)
                        || (effective_shape == NotePane.SHAPE_TAG_MARK_P))
                {
                    // all has been done above
                    path.reset();
                }
                //else if(effective_shape == NotePane.SHAPE_TAG_SQUARE)
                //else if(effective_shape == NotePane.SHAPE_TAG_TILE)
                else {
                    mRectF.set  (wh_gap/2, wh_gap/2, w, h);
                    path.addRect(mRectF  , Path.Direction.CW);
                }
            }
        }
        // }}}

        // DRAW SOME SHAPE OR SURFACE {{{
        if(!path.isEmpty() || (mPaint.getShader() != null))
        {
            canvas.drawPath(path, mPaint);
        }
        //}}}
        // INACTIVE CONTENT {{{
        if( !_active ) {
            mPaint.setAlpha( current_alpha );
        }
        // }}}
        // restore blurred and phantom button color {{{
        if((blurred_color != -1) || is_a_phantom_button)
        {
            mPaint.setColor( instance_bg_color );
            return; // skip super.onDraw
        }
        //}}}
        // [Settings.SIZE_MIN_FOR_TEXT] .. (no text for small horizontally-squeezed shape) {{{
        if(h <= Settings.SIZE_MIN_FOR_TEXT)
        {
            if(getScaleX() < 1f)
                return; // skip super.onDraw
        }
        //}}}
        // VERTICAL {{{
        if(orientation == ORIENTATION_VERTICAL) {
        //  String o = "";
            if(      has_left_round_corner()  ) orientation = ORIENTATION_DOWN_TOP;
            else if( has_right_round_corner() ) orientation = ORIENTATION_TOP_DOWN;
            else                                orientation = ORIENTATION_TOP_DOWN;
            super.setText( getText().toString().replace("\n","") );
        }
        //}}}
        // NOT HORIZONTAL {{{
        if(orientation != ORIENTATION_HORIZONTAL) {
        //  onDrawRotation( canvas );
            onDrawPath    ( canvas );
        }
        else {
            super.onDraw( canvas );
        }
        //}}}
        // OVERFLOW MARKER {{{
        if( overflow )
        {
            mRectF.set(0, getHeight()-(int)(2*fts), getWidth(), getHeight());
            canvas.drawRect(mRectF, oPaint);
        }
        //}}}
    }
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@ onDraw @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //}}}
    // onDrawRotation {{{
    @SuppressLint("WrongCall")
    private void onDrawRotation(Canvas canvas)
    {
        if(orientation == ORIENTATION_TOP_DOWN)
        {
            int w = getWidth();
            int h = getHeight();
            canvas.rotate(90,w/2,h/2);
            //canvas.translate(w,0);
            //canvas.clipRect(0, 0, w, h, android.graphics.Region.Op.REPLACE);
            super.onDraw( canvas );
        }
        else if(orientation == ORIENTATION_DOWN_TOP)
        {
            int w = getWidth();
            int h = getHeight();
            canvas.rotate(-90,w/2,h/2);
            //canvas.translate(0,-w);
            //canvas.clipRect(0, 0, w, h, android.graphics.Region.Op.REPLACE);
            super.onDraw( canvas );
        }
    }
    //}}}
    private void onDrawPath(Canvas canvas) // {{{
    {
        if(orientation == ORIENTATION_TOP_DOWN)
        {
            int w = getWidth();
            int h = getHeight();
            Path p = new Path();
            int  x = w/2 - (int)(getTextSize()/2);
            p.moveTo(x,0);
            p.lineTo(x,h);
            setTextAlignment( TEXT_ALIGNMENT_CENTER );
            _setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            float hOffset = 0;
            float vOffset = 0;
            canvas.drawTextOnPath(getText().toString(), p, hOffset, vOffset, getPaint());
        }
        else if(orientation == ORIENTATION_DOWN_TOP)
        {
            int w = getWidth();
            int h = getHeight();
            Path p = new Path();
            int  x = w/2 + (int)(getTextSize()/2);
            p.moveTo(x,h);
            p.lineTo(x,0);
            setTextAlignment( TEXT_ALIGNMENT_CENTER );
            _setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            float hOffset = 0;
            float vOffset = 0;
            canvas.drawTextOnPath(getText().toString(), p, hOffset, vOffset, getPaint());
        }
    }
    //}}}
    // onMeasure .. FAKE WIDTH FOR HEIGHT f(ORIENTATION) {{{
/*
   @Override
   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      if(orientation == ORIENTATION_HORIZONTAL) {
          super.onMeasure     (widthMeasureSpec,            heightMeasureSpec);
          setMeasuredDimension(getMeasuredWidth()         , getMeasuredHeight());
      }
      else {
          super.onMeasure     (heightMeasureSpec,            widthMeasureSpec);
          setMeasuredDimension(getMeasuredHeight()         , getMeasuredWidth());
      }
   }
*/ // }}}
    // XXX

    // IMPLEMENTATION (i.e. private)
    // _init {{{
    private void _init()
    {
        // instance init {{{
        path  = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias   ( true                       ); // set anti alias to make it smooth

        //mPaint.setColor       ( STROKE_COLOR               ); // set the color
        //mPaint.setStrokeWidth ( STROKE_WIDTH               ); // set the size
        //mPaint.setDither      ( true                       ); // set the dither to true
    //  mPaint.setStyle         ( Paint.Style.STROKE         ); // set to STOKE
    //  mPaint.setStyle         ( Paint.Style.FILL_AND_STROKE);
        //mPaint.setStyle       ( Paint.Style.FILL           );
        //mPaint.setStrokeJoin  ( Paint.Join.ROUND           ); // set the join to round you want
        //mPaint.setStrokeCap   ( Paint.Cap.ROUND            ); // set the paint cap to round too
        //mPaint.setPathEffect  ( new CornerPathEffect(10)   ); // set the path effect when they join.
    //  mPaint.setXfermode      (new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    //  mPaint.setXfermode      (new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));

    //mPaint.setShadowLayer     (20f, 10f, 2f, Color.BLACK);
    //mPaint.setShadowLayer     (32 ,  5 , 5 , Color.BLACK);

        oPaint = new Paint();
        oPaint.setStyle( Paint.Style.FILL );
        oPaint.setColor( OVERRIDE_COLOR   );


        // mPaint mode {{{
        /*
           :!start explorer "http://stackoverflow.com/questions/8280027/what-does-porterduff-mode-mean-in-android-graphics-what-does-it-do"
           :!start explorer "http://ssp.impulsetrain.com/porterduff.html"
         */
        //  }}}

        //}}}

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) invalidateOutline();

    }
    //}}}
    // _setGravity {{{
    private void _setGravity(int gravity)
    {
        if( !fixedGravity )
            super.setGravity( gravity );
    }
    //}}}
    // _selectOutlineProvider {{{
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void _selectOutlineProvider()
    {
        //if( OUTLINING_SUPPORTED ) return;
        setOutlineProvider(
            new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline)
                {
                    // [w] [h] [wh_gap] {{{
                    int wh_gap = get_wh_gap();
                    int      w = getWidth () - wh_gap;
                    int      h = getHeight() - wh_gap;
                    //}}}
                    // RECTANGLE {{{
                    String   effective_shape  = get_effective_shape();
                    if     ((effective_shape == NotePane.SHAPE_TAG_SCROLL))
                    {
                        setElevation( Settings.FS_SCROLL_ELEVATION );
                    //  outline.setRect(0, 0, w, h);
                        outline.setEmpty(); // no outline when may_see_through .. (would show as a trapezoidal phantom shape)
                    }
                    else if(   (effective_shape == NotePane.SHAPE_TAG_TILE  )
                            || (effective_shape == NotePane.SHAPE_TAG_SQUARE))
                    {
                        setElevation( _get_scaled_Elevation() );
                    //  outline.setRect(0, 0, w, h);          // too sharp
                    //  outline.setRoundRect(0, 0, w, h, 10);
                        outline.setRect(wh_gap/2, wh_gap/2, w, h);
                    }
                    // }}}
                    // ROUND {{{
                    else {
                        setElevation( _get_scaled_Elevation() );
                        // CIRCLE {{{
                        int          min = (w > h) ? h : w;
                        int          max = (w > h) ? w : h;
                        float elongation = (float)max / (float)min;
                        if(elongation < 1.5)
                        {
                            set_shape_circle_RectF(w, h, min, max);
                            outline.setOval( (int)mRectF.left
                                    ,        (int)mRectF.top
                                    ,        (int)mRectF.right
                                    ,        (int)mRectF.bottom);
                        }
                        //}}}
                        // CAPROUND {{{
                        else {
                            outline.setRoundRect(0, 0, w, h, _get_scaled_radius());
                        }
                        // }}}
                    }
                    //}}}
//                    // alpha {{{
//                    outline.setAlpha( mPaint.getAlpha() );
//
//                    //}}}
                }
            }
        );
    }
    //}}}
    // _get_scaled_Elevation {{{
    private float _get_scaled_Elevation()
    {
        float elevation;
        if(lockedElevation >= 0) {
            elevation = lockedElevation;
        }
        else {
            float radius = _get_scaled_radius();
            elevation    = may_see_through() ? radius/4 : radius/2;
        }

        if( !_active ) elevation /= 4;

        return elevation;
    }
    //}}}
    // _get_scaled_radius {{{
    private float _get_scaled_radius()
    {
        // corner .. f(small side)
        int   w = getWidth();
        int   h = getHeight();
        int   s = Math.min(w,h);
        float r = 0.5f * s;

        // (UNCLIPED) ROUND CORNERS FOR:
        String effective_shape = get_effective_shape();
        if(        (effective_shape == NotePane.SHAPE_TAG_PADD_R)
                || (effective_shape == NotePane.SHAPE_TAG_PADD_L)
                || (effective_shape == NotePane.SHAPE_TAG_SCROLL)
                || (effective_shape == NotePane.SHAPE_TAG_CIRCLE)
                || (effective_shape == NotePane.SHAPE_TAG_SATIN )
                || (effective_shape == NotePane.SHAPE_TAG_MARK_L)
                || (effective_shape == NotePane.SHAPE_TAG_MARK_P)
          ) {
            return r;
          }
        else {
            // [CLIPED] CORNERS FOR:
            // . (TAG_ONEDGE)
            // . (TAG_RING)
            // . (TAG_SQUARE)
            // . (TAG_TILE)
            // . (TAG_MARK)
            r = Math.max(r, Settings.TAB_GRID_S    );   // at least .. (small tabs:   FAT CORNERS)
            r = Math.min(r, Settings.TAB_GRID_S * 2);   // at most  .. (large tabs: SHARP CORNERS) .. (somewhat) larger than small tabs
        }
        return Math.max(r, 1f);
    }
    //}}}
    // _get_scaled_TAB_GRID_S {{{
/*
    private int _get_scaled_TAB_GRID_S()
    {
        //if(scalable) return Settings.TAB_GRID_S * Settings.DEV_SCALE;
        //else         return Settings.TAB_GRID_S;
        return Settings.TAB_GRID_S;
    }
*/
    //}}}

    // PUBLIC STATIC
    public static void FitText(TextView tv) // {{{
    {
        FitText(tv, 1F);
    }
    // }}}
    public static void FitText(TextView tv, float adjustment) // {{{
    {
        float       fit_ts_on_vh = Get_Fit_TextSize_On_ViewHeight( tv );

        float fts = fit_ts_on_vh * tv.getMeasuredHeight() * adjustment;

        if(fts > Settings.FONT_SIZE_MAX) fts = Settings.FONT_SIZE_MAX;

        if(tv.getTextSize() != fts)
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, fts);
    }
    //}}} 
    public static void FitText(TextView tv, int vw, int vh) // {{{
    {
        float fts = Get_Fit_TextSize_On_ViewHeight(tv, vw, vh);

        if(fts > Settings.FONT_SIZE_MAX) fts = Settings.FONT_SIZE_MAX;

        if(tv.getTextSize() != fts)
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, fts);
    }
    //}}} 
    public static boolean Is_text_vertical(String text) // {{{
    {
        int len_to_check  = text.indexOf( NotePane.INFO_SEP ); // exclude INFO
        if( len_to_check <= 0)
            len_to_check  = Math.min(32, text.length());
        int len = 0;
        for(int i=0; i<len_to_check; ++i)
        {
            if(text.charAt(i) != '\n') len += 1;        // cumulate
            else                       len  = 0;        // reset
            if(len > 1) break;
            if(  i > 4) return true;
        }
        return false;
    }
    //}}}
    // Make_text_horizontal {{{
    public static String Make_text_horizontal(String text)
    {
        //return text.replace("\n","").replace(" ","\n");
        int len_to_check  = text.indexOf( NotePane.INFO_SEP ); // exclude INFO
        if( len_to_check <= 0)
            len_to_check  = Math.min(32, text.length());

        StringBuilder sb = new StringBuilder();
        for(int i=0; i<len_to_check; ++i)
        {
            char c = text.charAt(i);
            if(c != '\n') sb.append(c);
        }
        return sb.toString() + text.substring(len_to_check);
    }
//}}}

    // PRIVATE STATIC
    private static float Get_Fit_TextSize_On_ViewHeight(TextView tv) // {{{
    {
        int vw = tv.getMeasuredWidth();
        int vh = tv.getMeasuredHeight();
    //  int vh = tv.getMeasuredHeight() - tv.getCompoundPaddingBottom() - tv.getCompoundPaddingTop  ();
    //  int vw = tv.getMeasuredWidth () - tv.getCompoundPaddingLeft  () - tv.getCompoundPaddingRight();
        return Get_Fit_TextSize_On_ViewHeight(tv, vw, vh);
    }
    //}}}
    private static float Get_Fit_TextSize_On_ViewHeight(TextView tv, int vw, int vh) // {{{
    {
//if(vh > vw) { int i = vh; vh = vw; vw = i; }
        // text {{{
        CharSequence text = tv.getText();
        if((text == null) || (text.length() == 0))
            return Settings.FONT_SIZE_DEF;

        //}}}
        // view and text size {{{
        TextPaint textPaint = new TextPaint( tv.getPaint() );
//textPaint.setTypeface( Typeface.DEFAULT ); // XXX ignore NOTO while sizing
        //int     vw  = tv.getMeasuredWidth();
        //int     vh  = tv.getMeasuredHeight();
        float   top = Settings.FONT_SIZE_MAX;
        float   bot = Settings.FONT_SIZE_MIN;
        vw -= (tv.getPaddingLeft() + tv.getPaddingRight ());
        vh -= (tv.getPaddingTop () + tv.getPaddingBottom());
        if(vw < 10) vw = 10;
        if(vh < 10) vh = 10;
        //}}}
        // text padding to prevent small word break (not for multi-line) {{{
    //  String padding =   ""; if(s.indexOf("\n") < 0) padding = "W";
    //  String sample_text = "W" + text;
        boolean is_GUI_FONT_DEFAULT = Settings.is_GUI_FONT_DEFAULT();
    //  String sample_text = text.toString();
        String sample_text = (is_GUI_FONT_DEFAULT ? "WWW" : "WWW") + text;

    //  float spacingmult = is_GUI_FONT_DEFAULT ? 1f : 0.8f;
    //  float spacingadd  = is_GUI_FONT_DEFAULT ? 0f : 4f;//1.0f;
    //  float spacingadd_factor  = is_GUI_FONT_DEFAULT ? 0f : 0.9f;
    //  if( !is_GUI_FONT_DEFAULT ) { vh *= 2; }

    //  float tw_factor = is_GUI_FONT_DEFAULT ? 1f : 1.2f;
    //  float th_factor = is_GUI_FONT_DEFAULT ? 1f : 1f;//.05f;
        //}}}
        // max fitting text size (dichotomic search) {{{
        float mid, tw=0, th=0;
        do {
            mid = (bot+top) / 2;                    // pick mid-range

            textPaint.setTextSize( mid );

            StaticLayout layout
                = new StaticLayout( sample_text // text
                        , textPaint
                        , vw
                        , Layout.Alignment.ALIGN_CENTER // align
                        , 1.0f                          // spacingmult
                        , 0.0f                          // spacingadd (line spacing)
                        , true                          // includepad
                        );

            tw = layout.getWidth ();// * tw_factor;
            th = layout.getHeight();// * th_factor;

            if((tw>vw ) || (th>vh)) top = mid;          // capped top
            else                    bot = mid;          // capped bot
        }
        while((top-bot) > 1);
        if(mid == top)
            mid = bot; // safety margin for some extra space

        //}}}
        // apply resulting text size {{{
    //  if(is_GUI_FONT_DEFAULT) return        (mid-2) / vh;
    //  else                    return 1.3f * (mid-2) / vh;
    //  if(is_GUI_FONT_DEFAULT) return        (mid-2) / vh;
    //  else                    return 1.3f * (mid-2) / vh;
        return                                (mid  ) / vh;

        //}}}
    }
    //}}}

}

/* //{{{
" Nine-patch:
:!start explorer "http://developer.android.com/guide/topics/graphics/2d-graphics.html#nine-patch"

" NinePatchDrawable:
:!start explorer "http://developer.android.com/guide/topics/graphics/2d-graphics.html"

" Draw 9-patch:
:!start explorer "http://developer.android.com/tools/help/draw9patch.html"

*/ //}}}
