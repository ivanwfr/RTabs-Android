package ivanwfr.rtabs; // {{{

//port android.media.ToneGenerator;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
//port android.graphics.BlendModeColorFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.MailTo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.support.v4.view.ViewCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Property;
import android.view.ActionMode;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.ScaleGestureDetector;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.widget.FrameLayout;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceRequest;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import ivanwfr.rtabs.sandbox.SandBox;
import ivanwfr.rtabs.util.SystemUiHider;

// }}}
// Comment {{{

// }}}
// Rtabs_TAG = "RTabs (220927:14h:56)";
public class RTabs implements Settings.ClampListener
{
    /**:VAR */
    //{{{
    // MONITOR TAGS {{{
    private static       String TAG_EV0_RT_DP  = Settings.TAG_EV0_RT_DP;
    private static       String TAG_EV1_RT_IN  = Settings.TAG_EV1_RT_IN;
    private static       String TAG_EV1_RT_OK  = Settings.TAG_EV1_RT_OK;
    private static       String TAG_EV2_RT_CB  = Settings.TAG_EV2_RT_CB;
    private static       String TAG_EV3_RT_SC  = Settings.TAG_EV3_RT_SC;
    private static       String TAG_EV7_RT_FL  = Settings.TAG_EV7_RT_FL;

    private static       String TAG_ACTIVITY   = Settings.TAG_ACTIVITY;
    private static       String TAG_ANIM       = Settings.TAG_ANIM;
    private static       String TAG_BAND       = Settings.TAG_BAND;
    private static       String TAG_CLAMP      = Settings.TAG_CLAMP;
    private static       String TAG_COMM       = Settings.TAG_COMM;
    private static       String TAG_DATA       = Settings.TAG_DATA;
    private static       String TAG_DIALOG     = Settings.TAG_DIALOG;
    private static       String TAG_FS_SEARCH  = Settings.TAG_FS_SEARCH;
    private static       String TAG_FULLSCREEN = Settings.TAG_FULLSCREEN;
    private static       String TAG_GLOW       = Settings.TAG_GLOW;
    private static       String TAG_GUI        = Settings.TAG_GUI;
    private static       String TAG_HANDLE     = Settings.TAG_HANDLE;
    private static       String TAG_JAVASCRIPT = Settings.TAG_JAVASCRIPT;
    private static       String TAG_KEYBOARD   = Settings.TAG_KEYBOARD;
  //private static       String TAG_MARKER     = Settings.TAG_MARKER;
    private static       String TAG_POLL       = Settings.TAG_POLL;
    private static       String TAG_PROFILE    = Settings.TAG_PROFILE;
    private static       String TAG_SCALE      = Settings.TAG_SCALE;
    private static       String TAG_SCROLLBAR  = Settings.TAG_SCROLLBAR;
    private static       String TAG_SETTINGS   = Settings.TAG_SETTINGS;
    private static       String TAG_SOUND      = Settings.TAG_SOUND;
    private static       String TAG_TOOL       = Settings.TAG_TOOL;
    private static       String TAG_WEBGROUP   = Settings.TAG_WEBGROUP;
    private static       String TAG_WEBVIEW    = Settings.TAG_WEBVIEW;
    private static       String TAG_WVTOOLS    = Settings.TAG_WVTOOLS;

    // }}}
     // {{{
    public static FullscreenActivity activity;

    // LOGGING
    public  static boolean  D = Settings.D;
    public  static void Set_D(boolean state) { if(D||M) log("RTabs.Set_D("+state+")"); D = state; }

    // MONITOR
    private static boolean  M = Settings.M;
    public  static void Set_M(boolean state) { if(D||M) log("RTabs.Set_M("+state+")"); M = state; }

    // SDK VERSION DEPENDENT
    public  static final boolean ANIM_SUPPORTED = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2);

    // }}}
    // UI THREAD HANDLER (.. main tread) {{{
    public  static final MHandler  handler = new MHandler();
    public  static class MHandler extends Handler
    {
        public boolean re_post       (Runnable r                  ) { removeCallbacks( r ); return post       ( r              ); }
        public boolean re_postDelayed(Runnable r, long delayMillis) { removeCallbacks( r ); return postDelayed( r , delayMillis); }
    }

    // }}}
    // STATE MACHINE {{{
    private static final int STAGE0_SIGNIN          = 0;
    private static final int STAGE1_PALETTES_GET    = 1;
    private static final int STAGE2_PALETTES_PARSE  = 2;
    private static final int STAGE3_TABS_GET        = 3;
    private static final int STAGE4_TABS_PARSE      = 4;
    private static final int STAGE5_TABS_LAYOUT     = 5;
    private static final int STAGE6_CONNECTING      = 6;
    private static final int STAGE7_POLLING         = 7;
    private static final int STAGE8_PROFILE_UPDATE  = 8;
    private static final int STAGE9_PROFILE_PARSE   = 9;

    //}}}
    // MODULES {{{
    public static RTabsClient this_RTabsClient= null;

    //}}}
    // DURATION - DELAYS {{{
    /**
      * UI feedback and activation cooldown delays.
      */
    private static final int DATA_LOOPER_IDLE_PERIOD        = 5000;
    private static final int DATA_LOOPER_BASE_PERIOD        = 1000;

    private static final int FINISH_DELAY                   = 3000;

    private static final int POLL_CHECK_DELAY               = 1000;

    private static final int SYNCUI_VISIBILITY_DELAY        =  100;
    private static final int SYNCUI_COLORS_DELAY            =  500;

    private static final int DATA_LOOPER_DELAY              =  500;
    private static final int SYNC_DYNAMIC_TABS_DELAY        =   50;

    private static final int SET_NP_LABEL_FITTEXT_DELAY     =  250;

    private static final int PULSE_BACK_DURATION            =  150;
    private static final int PULSE_HOLD_DURATION            =   80;
    private static final int PULSE_FORE_DURATION            =   40;

    //}}}
    // UI {{{
    public  FrameLayout     frameLayout             = null; // TODO: make these private
    public  FrameLayout     wvContainer             = null; // ...
    public  Clamp           mClamp                  = null; // ...
    // bg_view {{{
//  public View             bg_view                 = null;
//  public TextView         bg_view                 = null;
    public NpButton         bg_view                 = null;

    // }}}
    // tabs_container {{{
    private VScrollView     vsv                     = null;
    private HScrollView      hsv                    = null;
    private ViewGroup        tabs_container         = null;

    // }}}
    // seekers_container {{{
    private RelativeLayout  seekers_container       = null;
    //ivate TextView         scale_textView         = null;
    private SeekBar          scale_seekBar          = null;
    //ivate TextView         palettes_textView      = null;
    private SeekBar          palettes_seekBar       = null;

    // }}}
    // controls_container {{{
    private RelativeLayout  controls_container      = null;
    private EditText         cmd_text               = null;

    private LScrollView      log_container          = null;
    public  boolean log_container_is_null()        { return (log_container == null);                                      }

    private TextView          log_text              = null;
    public  TextView get_log_text()            { return log_text; }
    public  boolean  log_text_is_null()        { return (log_text == null);                                      }
    public  String   log_text_getText()        { return (log_text != null) ? log_text.getText().toString() : ""; }
    public  int      log_text_getText_length() { return (log_text != null) ? log_text.getText().length()   :  0; }
    public  void     log_text_clear()          {      if(log_text != null)   log_text.setText("");               }
    public  void     log_text_setText(String s){      if(log_text != null)   log_text.setText(s);                }

    // }}}
    // cmd_container {{{
    private LinearLayout     cmd_container          = null;

    private Button            cmd_send              = null;

    private Button            cmd_INVENTORY         = null;
    private Button            cmd_PROFILE           = null;

    private Button            cmd_clear_log         = null;
    private CheckBox          checkBox_log          = null;
    private CheckBox          checkBox_freeze       = null;

    private Button            cmd_status            = null;

    // }}}
    // FIXME {{{
//  private TabsResizer     tabs_resizer            = null;
    //}}}
    // }}}
    // handles_container [lft dck top mid bot] {{{
    private RelativeLayout handles_container        = null;
    private Handle          lft_handle              = null;
    private Handle          dck_handle              = null;
    private ShowBand         show_band              = null;
    private HistBand          hist_band             = null;
    private DockBand          dock_band             = null;
    private CartBand          cart_band             = null;
    private Handle          top_handle              = null;
    private Handle          mid_handle              = null;
    private Handle          bot_handle              = null;

    private WVTools         wvTools                = null;

    public int ctrl_container_getChildCount() { return (mid_handle != null) ? mid_handle.getChildCount() : 0; }
    public int prof_container_getChildCount() { return (top_handle != null) ? top_handle.getChildCount() : 0; }

    // }}}
    //}}}
    /** RTABS  */
    //{{{
    // {{{
    private static RTabs RTabs_instance = null;

    // }}}
    // getInstance {{{
    public static RTabs getInstance(FullscreenActivity new_instance)
    {
        String caller = "RTabs.getInstance";

        activity = new_instance;

        if(D || Settings.LOG_CAT) log_life_cycle(caller);

        if(RTabs_instance == null)
            RTabs_instance = new RTabs();

        RTabs_instance.set_new_activity();

        return RTabs_instance;
    }
    //}}}
    // set_new_activity .. [activity]->[frameLayout] {{{
    private void set_new_activity()
    {
        String caller = "set_new_activity";
        if(D || Settings.LOG_CAT) log_life_cycle(caller);

        // old [activity] -> [frameLayout]
        ViewGroup parent = (ViewGroup)frameLayout.getParent();
        if(parent != null) parent.removeView( frameLayout );

        // new [activity] -> [frameLayout]
        activity.setContentView( frameLayout );

        // TO REBUILD FOR EACH ACTIVITY INSTANCE
        note_dialog = null;
        ip_dialog   = null;
    }
    //}}}
    // RTabs .. [frameLayout] -> [] {{{
    private RTabs()
    {
        String caller = "onCreate_set_activity";

        MLog     .set_RTabs( this );
        Settings .Settings_class_init();
        Settings .DEVICE = android.os.Build.DEVICE;

        boolean first_app_launch = Settings.LoadSettings(caller);

        Settings.LoadSettings(caller);
        if(D || Settings.LOG_CAT) log_life_cycle(caller);

        MLog.clear_ERRLOGGED_files();

        if(D || Settings.LOG_CAT) if(frameLayout == null) MLog.log("*** onCreate: (frameLayout == null");

        onCreate_RTabsClient();
        if(frameLayout == null)
        {
            if(D || Settings.LOG_CAT) MLog.log("onCreate ..(frameLayout == null):");
            onCreate_frameLayout();
            onCreate_wvContainer();
            onCreate_WVTools();
            onCreate_TABS();
            onCreate_bg_view();
            onCreate_handles();
            onCreate_seekBars();
            onCreate_controls_container();
            onCreate_log_init();
            set_listeners();

            this_RTabsClient.start_BatteryPoll("onCreate"); /* BatteryPoll */
/* {{{
:!start explorer "https://developer.android.com/guide/components/broadcasts"

    If you register a receiver in onCreate(Bundle) using the activity's context
    , you should unregister it in onDestroy()
    to prevent leaking the receiver out of the activity context.

    If you register a receiver in onResume()
    , you should unregister it in onPause()
    to prevent registering it multiple times
    (If you don't want to receive broadcasts when paused).

}}}*/
        }
        else {
            if(D || Settings.LOG_CAT) MLog.log("onCreate ..(frameLayout != null):");
        }

        /* [first_app_launch] {{{*/
        if( first_app_launch )
        {
            System.err.println("RTABS: FIRST APP LAUNCH .. INSTALLING PROFILES FACTORY DEFAULTS SAMPLES ");
            profile_unzip_factory();

            int profiles_Dict_size = Profile.Get_Profiles_Dict_size();
            if(profiles_Dict_size > 0)
            {
                String[] profiles_Dict_names = Profile.Get_Profiles_Dict_names();
                String   first_profile_name  = profiles_Dict_names[0];

                System.err.println("RTABS: Loading profile #1 of "+ profiles_Dict_size+ " ["+first_profile_name+"]");
                this_RTabsClient.load_USER_PROFILE(first_profile_name, caller);
            }
            else {
                System.err.println("RTABS: could not get Profiles_Dict");
            }
        }
        /*}}}*/
    }
    //}}}
    //}}}
    /** ACTIVITY LIFE CYCLE */
    //{{{

    // onCreate_RTabsClient {{{
    private void onCreate_RTabsClient()
    {
        if(RTabsClient.RTabsClient_instance == null)
        {
            if(D || Settings.LOG_CAT) MLog.log("new RTabsClient:");
            this_RTabsClient = new RTabsClient(this, log_text);
        }
        else {
            this_RTabsClient        = RTabsClient.RTabsClient_instance;
            this_RTabsClient.mRTabs = this;
        }
    }
    //}}}
    // onCreate_frameLayout {{{
    private void onCreate_frameLayout()
    {
        // [frameLayout] {{{
        // layout.xml {{{

        // app/src/main/res/layout/activity_fullscreen.xml

        //setContentView(R.layout.activity_fullscreen);

        //frameLayout = (FrameLayout)findViewById(R.id.frameLayout);

        // <FrameLayout
        //? tools:context=".FullscreenActivity"
        //* android:id="@+id/frameLayout"
        //* android:layout_width="match_parent"
        //* android:layout_height="match_parent"
        //* android:nestedScrollingEnabled="true"
        //* android:background="#000000"
        //? xmlns:android="http://schemas.android.com/apk/res/android"
        //? xmlns:tools="http://schemas.android.com/tools"
        //? tools:targetApi="lollipop">
        //}}}
        frameLayout = new FrameLayout( activity );
        frameLayout.setId( View.generateViewId() );
        //{{{
        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(0, 0);
        flp.height       = ViewGroup.LayoutParams.MATCH_PARENT;
        flp.width        = ViewGroup.LayoutParams.MATCH_PARENT;
        frameLayout.setLayoutParams( flp );

        frameLayout.setBackgroundColor(  Color.BLACK );

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            frameLayout.setNestedScrollingEnabled( true );

        //}}}
        // }}}
    }
    //}}}
    // onCreate_wvContainer {{{
    private void onCreate_wvContainer()
    {
        // [wvContainer] {{{
        wvContainer = new FrameLayout( activity );
        wvContainer.setId( View.generateViewId() );

        //}}}
        // LISTENERS {{{
        ViewCompat.setElevation(wvContainer, Settings.WVCONTAINER_ELEVATION);
        wvContainer.setOnClickListener    ( wvContainer_OnClickListener     );
        wvContainer.setOnLongClickListener( wvContainer_OnLongClickListener );

        // }}}
        // LAYOUT {{{
        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(0, 0);
        flp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        flp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
        wvContainer.setLayoutParams( flp );

        wvContainer.setBackgroundColor(  Color.BLACK );

        wvContainer.setVisibility( View.GONE );

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            wvContainer.setNestedScrollingEnabled( true );

        //}}}
        frameLayout.addView( wvContainer );
    }
    //}}}
    // onCreate_TABS {{{
    private void onCreate_TABS()
    {
        //{{{
        FrameLayout   .LayoutParams flp;

        //}}}
        // [frameLayout] -> [vsv] {{{
        // layout.xml {{{
        //vsv     = (VScrollView)findViewById( R.id.tabs_VScrollView );
        //*<ivanwfr.rtabs.VScrollView
        //* android:id="@+id/tabs_VScrollView"
        //* android:layout_width="match_parent"
        //* android:layout_height="match_parent"
        //x android:nestedScrollingEnabled="false"
        //x android:clickable="false"
        //x android:focusable="false"
        //x android:isScrollContainer="false"
        //x android:transitionGroup="false"
        //x android:touchscreenBlocksFocus="false"
        //x android:addStatesFromChildren="false"
        //x android:duplicateParentState="false"
        //x android:filterTouchesWhenObscured="false"
        //x android:focusableInTouchMode="false"
        //? tools:targetApi="lollipop">
        //}}}
        vsv = new VScrollView( activity );
        vsv.setId( View.generateViewId() );
        //{{{
        flp              = new FrameLayout.LayoutParams(0, 0);
        flp.height       = ViewGroup.LayoutParams.MATCH_PARENT;
        flp.width        = ViewGroup.LayoutParams.MATCH_PARENT;
        vsv.setLayoutParams( flp );

        //}}}
        // }}}
        // [frameLayout] -> [vsv] -> [hsv] {{{
        // layout.xml {{{
        //hsv     = (HScrollView)findViewById( R.id.tabs_HScrollView );
        // <ivanwfr.rtabs.HScrollView
        //* android:id="@+id/tabs_HScrollView"
        //* android:layout_width="match_parent"
        //* android:layout_height="match_parent"
        //x android:nestedScrollingEnabled="false"
        //? tools:targetApi="lollipop">
        //}}}
        hsv = new HScrollView( activity );
        hsv.setId( View.generateViewId() );
        //{{{
        flp              = new FrameLayout.LayoutParams(0, 0);
        flp.height       = ViewGroup.LayoutParams.MATCH_PARENT;
        flp.width        = ViewGroup.LayoutParams.MATCH_PARENT;
        hsv.setLayoutParams( flp );

        //}}}
        //}}}
         // [frameLayout] -> [vsv] -> [hsv] -> [tabs_container] {{{
        tabs_container = this_RTabsClient.reparent_tabs_container( hsv );

        //}}}

        frameLayout.addView( vsv );
        /*................*/ vsv.hsv      = hsv;
        /*................*/ vsv.mRTabs   = this;
        /*................*/ vsv.addView  ( hsv );
    }
    //}}}
    // onCreate_bg_view {{{
    private void onCreate_bg_view()
    {
    //  bg_view= findViewById(R.id.bg_view);
    //  bg_view= new TextView( activity );
        bg_view= new NpButton( activity );
        bg_view.setId( View.generateViewId() );

    //  bg_view.set_shape         ( NotePane.SHAPE_TAG_CIRCLE    );
        bg_view.set_shape         ( NotePane.SHAPE_TAG_ONEDGE    );
    //  bg_view.set_shape         ( NotePane.SHAPE_TAG_PADD_R    );
    //  bg_view.set_shape         ( NotePane.SHAPE_TAG_SQUARE    );
    //  bg_view.set_shape         ( NotePane.SHAPE_TAG_TILE      );

        bg_view.setActive         ( true                         );

        bg_view.fixedGravity = true; // prevent auto-layout
        bg_view._setForegroundGravity(Gravity.BOTTOM| Gravity.END);
        bg_view.setGravity          (Gravity.BOTTOM| Gravity.END);

        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(0, 0);
        flp.leftMargin  = 0;
        flp.topMargin   = 0;
        flp.width       = ViewGroup.LayoutParams.MATCH_PARENT;
        flp.height      = ViewGroup.LayoutParams.MATCH_PARENT;
        bg_view.setLayoutParams( flp );

        bg_view.setTypeface( Settings.getTypeface() );
        bg_view.setShadowLayer    ( 6,1,1,Color.BLACK       );
        bg_view.setTextColor      ( Color.WHITE | 0x88000000);
        bg_view.setText           ("");

        frameLayout.addView(bg_view, 0);
    }
    //}}}
    // onCreate_seekBars {{{
    private void onCreate_seekBars()
    {
        //{{{
        FrameLayout   .LayoutParams flp;
        RelativeLayout.LayoutParams rlp;

        //}}}
        // SEEKERS
        // [frameLayout] -> [seekers_container] {{{
        // layout.xml {{{
        //seekers_container = (RelativeLayout)findViewById(R.id.seekers_container);
        //  <RelativeLayout
        //.. android:id="@+id/seekers_container"
        //   android:layout_width="match_parent"
        //   android:layout_height="match_parent"
        //   android:layout_marginStart="50dp"
        //   >
        //}}}
        seekers_container = new RelativeLayout( activity );
        seekers_container.setId( View.generateViewId() );
        //{{{
        flp              = new FrameLayout.LayoutParams(0, 0);
        flp.height       = ViewGroup.LayoutParams.MATCH_PARENT;
        flp.width        = ViewGroup.LayoutParams.MATCH_PARENT;
        flp.leftMargin   = Settings.GUARD_LEFT;
        flp.rightMargin  = Settings.GUARD_LEFT;
        seekers_container.setLayoutParams( flp );

        //}}}
        // }}}
        // SCALE
        // [frameLayout] -> [seekers_container] -> (scale_seekBar) {{{
        // layout.xml {{{
        //scale_seekBar     = (SeekBar       )findViewById(R.id.scale_seekBar);
        // <SeekBar
        //* android:id="@+id/scale_seekBar"

        //* android:layout_height="wrap_content"
        //* android:layout_width="match_parent"
        //* android:layout_marginStart="0dp"
        //* android:layout_marginEnd="0dp"
        //* android:layout_alignParentStart="true"
        //* android:layout_alignParentEnd="true"
        //* android:layout_alignParentTop="true"
        //x android:layout_alignParentBottom="false"

        //* android:padding="0dp"

        //* android:foreground="#5cff00c3"
        //* android:background="#5e010101"

        //* android:progressTint="#ff00e0"
        //* android:thumbTint="#ffff00c3"

        //* android:progress="5"

        //? tools:targetApi="lollipop" />
        //}}}
        scale_seekBar = new SeekBar( activity );
        scale_seekBar.setId( View.generateViewId() );
        //{{{
        rlp              = new RelativeLayout.LayoutParams(1,1);
        rlp.width        = ViewGroup.LayoutParams.MATCH_PARENT;
        rlp.height       = ViewGroup.LayoutParams.WRAP_CONTENT;
        rlp.leftMargin   =  0;
        rlp.rightMargin  =  0;
        rlp.topMargin    =  2;
        rlp.addRule(       RelativeLayout.ALIGN_PARENT_LEFT                     );
        rlp.addRule(       RelativeLayout.ALIGN_PARENT_RIGHT                    );
        rlp.addRule(       RelativeLayout.ALIGN_PARENT_TOP                      );
        scale_seekBar.setLayoutParams( rlp );

        scale_seekBar.setPadding(0,0,0,0);

    //  scale_seekBar.setTextColor                        (  Color.parseColor("#5cff00c3") );
        scale_seekBar.setBackgroundColor                  (  Color.parseColor("#5e010101") );
        scale_seekBar.getProgressDrawable().setColorFilter(  Color.parseColor("#ff00e0"  ), PorterDuff.Mode.SRC_IN);
        scale_seekBar.getThumb           ().setColorFilter(  Color.parseColor("#ffff00c3"), PorterDuff.Mode.SRC_IN);
      //scale_seekBar.getProgressDrawable().setColorFilter(new BlendModeColorFilter(Color.parseColor("#ff00e0"  ), BlendMode.SRC_IN));
      //scale_seekBar.getThumb           ().setColorFilter(new BlendModeColorFilter(Color.parseColor("#ffff00c3"), BlendMode.SRC_IN));

        scale_seekBar.setProgress( 5 );

        //}}}

        //}}}
        // PALETTES
        // [frameLayout] -> [seekers_container] -> (palettes_seekBar) {{{
        // layout.xml {{{
        //palettes_seekBar  = (SeekBar       )findViewById(R.id.palettes_seekBar);
        //   <SeekBar
        //..  android:id="@+id/palettes_seekBar"

        //    android:layout_width="match_parent"
        //    android:layout_height="wrap_content"
        //    android:layout_marginStart="0dp"
        //    android:layout_marginEnd="0dp"

        //    android:layout_alignParentStart="true"
        //    android:layout_alignParentEnd="true"
        //    android:layout_alignParentBottom="true"

        //    android:padding="0dp"

        //    android:foreground="#5cff00c3"
        //    android:background="#5e010101"
        //    android:progressTint="#ff00e0"
        //    android:thumbTint="#ffff00c3"

        //    tools:targetApi="lollipop" />
        //  </RelativeLayout>
        //}}}
        palettes_seekBar = new SeekBar( activity );
        palettes_seekBar.setId( View.generateViewId() );
        //{{{
        rlp              = new RelativeLayout.LayoutParams(1,1);
        rlp.width        = ViewGroup.LayoutParams.MATCH_PARENT;
        rlp.height       = ViewGroup.LayoutParams.WRAP_CONTENT;
        rlp.leftMargin   =  0;
        rlp.rightMargin  =  0;
        rlp.addRule(       RelativeLayout.ALIGN_PARENT_LEFT                     );
        rlp.addRule(       RelativeLayout.ALIGN_PARENT_RIGHT                    );
        rlp.addRule(       RelativeLayout.ALIGN_PARENT_BOTTOM                   );
        palettes_seekBar.setLayoutParams( rlp );

        palettes_seekBar.setPadding(0,0,0,0);

    //  palettes_seekBar.setTextColor                        (  Color.parseColor("#5cff00c3") );
        palettes_seekBar.setBackgroundColor                  (  Color.parseColor("#5e010101") );
        palettes_seekBar.getProgressDrawable().setColorFilter(  Color.parseColor("#ff00e0"  ), PorterDuff.Mode.SRC_IN);
        palettes_seekBar.getThumb           ().setColorFilter(  Color.parseColor("#ffff00c3"), PorterDuff.Mode.SRC_IN);
      //palettes_seekBar.getProgressDrawable().setColorFilter(new BlendModeColorFilter(Color.parseColor("#ff00e0"  ), BlendMode.SRC_IN));
      //palettes_seekBar.getThumb           ().setColorFilter(new BlendModeColorFilter(Color.parseColor("#ffff00c3"), BlendMode.SRC_IN));

        //}}}

        //}}}
        // SEEKBARS
        frameLayout       .addView( seekers_container );
        /*.......................*/ seekers_container.addView(    scale_seekBar );
        /*.......................*/ seekers_container.addView( palettes_seekBar );

        seekers_container.setVisibility( View.GONE );
    }
     //}}}
    // onCreate_controls_container {{{
    private void onCreate_controls_container()
    {
        //{{{
        FrameLayout   .LayoutParams flp;
        LinearLayout  .LayoutParams llp;
        RelativeLayout.LayoutParams rlp;

        //}}}
        // CONTROLS
        // [frameLayout] -> [controls_container] {{{
        // layout.xml {{{
        //controls_container  = ( RelativeLayout )findViewById(R.id.controls_container);
        // <RelativeLayout
        //  android:id="@+id/controls_container"
        //  android:layout_width="match_parent"
        //  android:layout_height="match_parent"
        //  android:layout_marginStart="50dp">
        //}}}
        controls_container = new RelativeLayout( activity );
        controls_container.setId( View.generateViewId() );
        //{{{
        flp              = new FrameLayout.LayoutParams(0, 0);
        flp.height       = ViewGroup.LayoutParams.MATCH_PARENT;
        flp.width        = ViewGroup.LayoutParams.MATCH_PARENT;
        flp.leftMargin   = 50;
        controls_container.setLayoutParams( flp );

        //}}}
        // }}}
        // TEXT
        // [frameLayout] -> [controls_container] -> (cmd_text) {{{
        // layout.xml {{{
        //cmd_text            = ( EditText       )findViewById(R.id.cmd_text);
        // <EditText
        //* android:id="@+id/cmd_text"
        //* android:text="@string/initial_cmd"
        //* android:layout_width="fill_parent"
        //* android:layout_height="wrap_content"
        //* android:layout_marginTop="27dp"
        //* android:layout_gravity="center_horizontal|top"
        //* android:inputType="text"
        //* android:textColor="#000"
        //* android:background="#ff9c14"
        //* android:textSize="24sp"
        //? tools:ignore="LabelFor" />

        //}}}
        cmd_text = new  EditText( activity );
        cmd_text.setId( View.generateViewId() ); // (for addRule BELOW)
        //{{{
        rlp              = new RelativeLayout.LayoutParams(1,1);
        rlp.width        = ViewGroup.LayoutParams.MATCH_PARENT;
        rlp.height       = ViewGroup.LayoutParams.WRAP_CONTENT;
        rlp.addRule(       RelativeLayout.ALIGN_PARENT_LEFT                     );
        rlp.addRule(       RelativeLayout.ALIGN_PARENT_RIGHT                    );
        rlp.addRule(       RelativeLayout.ALIGN_PARENT_TOP                      );
        cmd_text.setLayoutParams( rlp );

        cmd_text.setGravity        (Gravity.START | Gravity.CENTER_VERTICAL);

        cmd_text.setTextColor      ( Color.BLACK );
        cmd_text.setBackgroundColor( Color.parseColor("#ff9c14") );

        cmd_text.setInputType      ( InputType.TYPE_CLASS_TEXT);
        cmd_text.setTextSize       (24);
        cmd_text.setText           ("");
        //}}}
        //}}}
        // LOG
        // [frameLayout] -> [controls_container] -> (log_container) {{{
        // layout.xml {{{
        //log_container   = ( LScrollView  )findViewById(R.id.log_container);
        // <ivanwfr.rtabs.LScrollView
        //- android:layout_width="match_parent"
        //- android:layout_height="wrap_content"
        //  android:id="@+id/log_container"
        //? android:layout_gravity="start|top"
        //x android:layout_alignParentTop="false"
        //- android:layout_alignParentBottom="true"
        //- android:layout_alignParentStart="true"
        //- android:layout_alignParentEnd="true"
        //- android:layout_below="@+id/cmd_text"
        //- android:layout_marginBottom="49dp">
        //}}}
        log_container    = new LScrollView( activity );
        log_container.setId( View.generateViewId() );
        //{{{
        rlp              = new RelativeLayout.LayoutParams(1,1);
        rlp.width        = ViewGroup.LayoutParams.MATCH_PARENT;
        rlp.height       = ViewGroup.LayoutParams.MATCH_PARENT;
        rlp.addRule(       RelativeLayout.BELOW               , cmd_text.getId());
        rlp.addRule(       RelativeLayout.ALIGN_PARENT_LEFT                     );
        rlp.addRule(       RelativeLayout.ALIGN_PARENT_RIGHT                    );
        rlp.addRule(       RelativeLayout.ALIGN_PARENT_BOTTOM                   );
        rlp.rightMargin  =  2;
        rlp.topMargin    =  2;
        log_container.setLayoutParams( rlp );

        //}}}
        //}}}
        // [frameLayout] -> [controls_container] -> (log_container) -> (log_text_hsv) {{{
        // layout.xml {{{
        // <HorizontalScrollView
        //- android:layout_width="wrap_content"
        //- android:layout_height="wrap_content"
        //- android:layout_below="@+id/cmd_text"
        //- android:layout_alignParentStart="true"
        //- android:layout_alignParentBottom="true"
        //  android:layout_toStartOf="@+id/cmd_send">
        //}}}
        HorizontalScrollView log_text_hsv = new HorizontalScrollView( activity );
        log_text_hsv.setId( View.generateViewId() );
        // {{{
        rlp              = new RelativeLayout.LayoutParams(1,1);
        rlp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
        rlp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        log_text_hsv.setLayoutParams( rlp );

        // }}}
        //}}}
        // [frameLayout] -> [controls_container] -> (log_container) -> (log_text_hsv) -> (log_text) {{{
        // layout.xml {{{
        //log_text        = ( TextView     )findViewById(R.id.log_text);
        // <TextView
        //  android:id="@+id/log_text"
        //- android:layout_width="wrap_content"
        //- android:layout_height="wrap_content"
        //- android:text="log"
        //- android:singleLine="false"
        //? android:typeface="monospace"
        //- android:focusableInTouchMode="false"
        //- android:textSize="18sp"
        //- android:layout_marginBottom="50dp"
        //- android:layout_marginTop="5dp"
        //- android:layout_marginStart="5dp"
        //- android:padding="20dp"
        //? android:nestedScrollingEnabled="true"
        //? tools:targetApi="lollipop"
        //? tools:ignore="HardcodedText" />
        //}}}
        log_text = new TextView( activity );
        log_text.setId( View.generateViewId() );
        // {{{
        rlp              = new RelativeLayout.LayoutParams(1,1);
        rlp.width        = ViewGroup.LayoutParams.MATCH_PARENT;
        rlp.height       = ViewGroup.LayoutParams.MATCH_PARENT;
        log_text.setLayoutParams( rlp );

        log_text.setPadding(20, 20, 20, 20); // left top right bottom
        log_text.setFocusableInTouchMode(false);
        log_text.setText    ("log");
        log_text.setTextSize( 18 );
        log_text.setTypeface( Typeface.MONOSPACE );
        // }}}
        //}}}
        // CMD
        // [frameLayout] -> [controls_container] -> [cmd_container] {{{
        // layout.xml {{{
        //cmd_container   = ( LinearLayout )findViewById(R.id.cmd_container);
        // <LinearLayout
        //* android:id="@+id/cmd_container"
        //* android:orientation="vertical"
        //* android:layout_centerHorizontal="true"
        //* android:layout_width="wrap_content"
        //* android:layout_height="wrap_content"
        //* android:layout_marginTop="4dp">
        //* android:layout_alignParentEnd="true"
        //* android:layout_alignParentBottom="false"
        //x android:layout_alignParentStart="false"
        //* android:layout_below="@+id/cmd_text"
        //}}}
        cmd_container = new LinearLayout( activity );
        cmd_container.setId( View.generateViewId() );
        //{{{
        rlp              = new RelativeLayout.LayoutParams(1,1);
        rlp.width        = 300;
        rlp.height       = ViewGroup.LayoutParams.WRAP_CONTENT;
        rlp.rightMargin  =  2;
        rlp.topMargin    =  2;
        rlp.addRule(       RelativeLayout.ALIGN_PARENT_END                      );
        rlp.addRule(       RelativeLayout.BELOW               , cmd_text.getId());
        cmd_container.setLayoutParams( rlp );

        cmd_container.setOrientation( LinearLayout.VERTICAL   );
        cmd_container.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        //}}}
        //}}}
        // [frameLayout] -> [controls_container] -> [cmd_container] -> (cmd_send) {{{
        // layout.xml {{{
        //cmd_send        = ( Button       )findViewById(R.id.cmd_send);
        // <Button
        //* android:id="@+id/cmd_send"
        //* android:text="Send"
        //* android:layout_width="150dp"
        //* android:layout_height="wrap_content"
        //* android:layout_marginTop="2dp"
        //* android:padding="0dp"
        //* android:layout_gravity="center_horizontal|bottom"
        //* android:textColor="#000000"
        //* android:background="#ffa500"
        //? tools:ignore="HardcodedText" />
        //}}}
        cmd_send = new  Button( activity );
        cmd_send.setId( View.generateViewId() );
        //{{{
        cmd_send.setText("Send");

        llp              = new LinearLayout.LayoutParams(1,1);
    //  llp.width        = 150;
        llp.width        = ViewGroup.LayoutParams.MATCH_PARENT;
        llp.height       = ViewGroup.LayoutParams.WRAP_CONTENT;
        cmd_send.setLayoutParams( llp );

        cmd_send.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        cmd_send.setPadding(0,0,0,0); // left top right bottom

        cmd_send.setTextColor( Color.BLACK );
        cmd_send.setBackgroundColor(  Color.parseColor("#ffa500") );
        //}}}
        //}}}
        // [frameLayout] -> [controls_container] -> [cmd_container] -> (cmd_PROFILE) {{{
        // layout.xml {{{
        //cmd_PROFILE     = ( Button       )findViewById(R.id.cmd_PROFILE);
        // <Button
        //* android:id="@+id/cmd_PROFILE"
        //* android:text="PROFILE"

        //* android:layout_width="150dp"
        //* android:layout_height="wrap_content"
        //* android:layout_marginTop="2dp"

        //* android:layout_gravity="start|bottom"
        //* android:textSize="14sp"

        //* android:background="#4b0082"

        //? tools:ignore="HardcodedText" />
        //}}}
        cmd_PROFILE = new  Button( activity );
        cmd_PROFILE.setId( View.generateViewId() );
        //{{{
        cmd_PROFILE.setText("PROFILE");

        llp              = new LinearLayout.LayoutParams(1,1);
    //  llp.width        = 150;
        llp.width        = ViewGroup.LayoutParams.MATCH_PARENT;
        llp.height       = ViewGroup.LayoutParams.WRAP_CONTENT;
        llp.topMargin    =  2;
        cmd_PROFILE.setLayoutParams( llp );

        cmd_PROFILE.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        cmd_PROFILE.setTextSize( 14 );

        cmd_PROFILE.setBackgroundColor(  Color.parseColor("#4b0082") );
        cmd_PROFILE.setTextColor( Color.WHITE );
        //}}}
        //}}}
        // [frameLayout] -> [controls_container] -> [cmd_container] -> (cmd_INVENTORY) {{{
        // layout.xml {{{
        //cmd_INVENTORY   = ( Button       )findViewById(R.id.cmd_INVENTORY);
        // <Button
        //* android:id="@+id/cmd_INVENTORY"
        //* android:text="INVENTORY"
        //* android:layout_width="150dp"
        //* android:layout_height="wrap_content"
        //* android:layout_marginTop="2dp"
        //* android:layout_gravity="end|bottom"
        //* android:textSize="14sp"
        //* android:background="#4b0082"
        //? tools:ignore="HardcodedText" />
        //}}}
        cmd_INVENTORY = new  Button( activity );
        cmd_INVENTORY.setId( View.generateViewId() );
        //{{{
        cmd_INVENTORY.setText("INVENTORY");

        llp              = new LinearLayout.LayoutParams(1,1);
    //  llp.width        = 150;
        llp.width        = ViewGroup.LayoutParams.MATCH_PARENT;
        llp.height       = ViewGroup.LayoutParams.WRAP_CONTENT;
        llp.topMargin    =  2;
        cmd_INVENTORY.setLayoutParams( llp );

        cmd_INVENTORY.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        cmd_INVENTORY.setTextSize( 14 );
        cmd_INVENTORY.setPadding(0,0,0,0); // left top right bottom

        cmd_INVENTORY.setBackgroundColor(  Color.parseColor("#4b0082") );
        cmd_INVENTORY.setTextColor( Color.WHITE );
        //}}}
        //}}}
        // [frameLayout] -> [controls_container] -> [cmd_container] -> (cmd_status) {{{
        // layout.xml {{{
        //cmd_status      = ( Button       )findViewById(R.id.cmd_status);
        // <Button
        //* android:id="@+id/cmd_status"
        //* android:layout_width="150dp"
        //* android:layout_height="wrap_content"
        //* android:layout_marginTop="2dp"
        //* android:text="Status"
        //* android:layout_gravity="bottom"
        //* android:background="#4b0082"
        //? tools:ignore="HardcodedText" />
        //}}}
        cmd_status = new  Button( activity );
        cmd_status.setId( View.generateViewId() );
        //{{{
        cmd_status.setText("Status");

        llp              = new LinearLayout.LayoutParams(1,1);
    //  llp.width        = 150;
        llp.width        = ViewGroup.LayoutParams.MATCH_PARENT;
        llp.height       = ViewGroup.LayoutParams.WRAP_CONTENT;
        llp.topMargin    =  2;
        cmd_status.setLayoutParams( llp );

        cmd_status.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        cmd_status.setTextSize( 14 );
        cmd_status.setPadding(0,0,0,0); // left top right bottom

        cmd_status.setBackgroundColor(  Color.parseColor("#4b0082") );
        cmd_status.setTextColor( Color.WHITE );
        //}}}
        //}}}
        // [frameLayout] -> [controls_container] -> [cmd_container] -> (checkBox_freeze) {{{
        // layout.xml {{{
        //checkBox_freeze = ( CheckBox     )findViewById(R.id.checkBox_freeze);
        // <CheckBox
        //* android:id="@+id/checkBox_freeze"
        //* android:text="freeze"
        //* android:checked="false"

        //* android:layout_width="match_parent"
        //* android:layout_height="wrap_content"
        //* android:layout_marginTop="2dp"
        //* android:layout_weight="0.12"

        //* android:minHeight="50dp"
        //* android:layout_gravity="center_horizontal|bottom"

        //* android:textSize="30sp"
        //* android:background="#000080"

        //? tools:ignore="HardcodedText" />
        //}}}
        checkBox_freeze = new CheckBox( activity );
        checkBox_freeze.setId( View.generateViewId() );
        //{{{
        checkBox_freeze.setText("freeze");
        checkBox_freeze.setChecked(false);

        llp              = new LinearLayout.LayoutParams(1,1);
        llp.width        = ViewGroup.LayoutParams.MATCH_PARENT;
        llp.height       = ViewGroup.LayoutParams.WRAP_CONTENT;
        llp.topMargin    =  2;
        llp.weight       = .12f;
        checkBox_freeze.setLayoutParams( llp );

        checkBox_freeze.setMinimumHeight( 50 );
        checkBox_freeze.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        checkBox_freeze.setTextSize( 30 );
        checkBox_freeze.setTextColor( Color.WHITE );
        checkBox_freeze.setBackgroundColor(  Color.parseColor("#000080") );

        //}}}
        //}}}
        // [frameLayout] -> [controls_container] -> [cmd_container] -> (checkBox_log) {{{
        // layout.xml {{{
        //checkBox_log    = ( CheckBox     )findViewById(R.id.checkBox_log);
        // <CheckBox
        //  android:id="@+id/checkBox_log"
        //  android:text="LOG"
        //  android:checked="false"

        //  android:layout_width="match_parent"
        //  android:layout_height="wrap_content"
        //  android:layout_marginTop="2dp"
        //  android:layout_weight="0.12"

        //  android:minHeight="50dp"
        //  android:layout_gravity="center_horizontal|bottom"

        //  android:textSize="30sp"
        //  android:background="#000080"

        //  tools:ignore="HardcodedText" />
        //}}}
        checkBox_log = new CheckBox( activity );
        checkBox_log.setId( View.generateViewId() );
        //{{{
        checkBox_log.setText("LOG");
        checkBox_log.setChecked(false);

        llp              = new LinearLayout.LayoutParams(1,1);
        llp.width        = ViewGroup.LayoutParams.MATCH_PARENT;
        llp.height       = ViewGroup.LayoutParams.WRAP_CONTENT;
        llp.topMargin    =  2;
        llp.weight       = .12f;
        checkBox_log.setLayoutParams( llp );

        checkBox_log.setMinimumHeight( 50 );
        checkBox_log.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        checkBox_log.setTextSize( 30 );
        checkBox_log.setTextColor( Color.WHITE );
        checkBox_log.setBackgroundColor(  Color.parseColor("#000080") );

        //}}}
        //}}}
        // [frameLayout] -> [controls_container] -> [cmd_container] -> (cmd_clear_log) {{{
        // layout.xml {{{
        //cmd_clear_log   = ( Button       )findViewById(R.id.cmd_clear_log);
        // <Button
        //* android:id="@+id/cmd_clear_log"
        //* android:text="Clear Log"

        //* android:layout_width="150dp"
        //* android:layout_height="wrap_content"
        //* android:layout_marginTop="2dp"
        //* android:layout_weight="0.12"

        //* android:layout_gravity="bottom"
        //  android:textSize="30sp"

        //  android:background="#000080"

        //  tools:ignore="HardcodedText" />
        //}}}
        cmd_clear_log = new  Button( activity );
        cmd_clear_log.setId( View.generateViewId() );
        //{{{
        cmd_clear_log.setText("Clear Log");

        llp              = new LinearLayout.LayoutParams(1,1);
    //  llp.width        = 150;
        llp.width        = ViewGroup.LayoutParams.MATCH_PARENT;
        llp.height       = ViewGroup.LayoutParams.WRAP_CONTENT;
        llp.topMargin    =  2;
        llp.weight       = .12f;
        cmd_clear_log.setLayoutParams( llp );

        cmd_clear_log.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        cmd_clear_log.setTextSize( 30 );

        cmd_clear_log.setBackgroundColor(  Color.parseColor("#4b0080") );
        cmd_clear_log.setTextColor( Color.WHITE );
        //}}}
        //}}}

        // CONTROLS
        frameLayout       .addView( controls_container                                                                 );
        /* TEXT..................*/ controls_container.addView( cmd_text                                               );
        /* LOG...................*/ controls_container.addView( log_container                                          );
        /*...................................................*/ log_container.addView(  log_text_hsv                   );
        /*...........................................................................*/ log_text_hsv.addView( log_text );
        /* CMD...................*/ controls_container.addView( cmd_container                                          );
        /*...................................................*/ cmd_container.addView(  cmd_send                       );
        /*...................................................*/ cmd_container.addView(  cmd_status                     );
        /*...................................................*/ cmd_container.addView(  cmd_PROFILE                    );
        /*...................................................*/ cmd_container.addView(  cmd_INVENTORY                  );
        /*...................................................*/ cmd_container.addView(  checkBox_freeze                );
        /*...................................................*/ cmd_container.addView(  checkBox_log                   );
        /*...................................................*/ cmd_container.addView(  cmd_clear_log                  );

        controls_container.setVisibility( View.GONE );
    }
    //}}}
    // onCreate_log_init {{{
    private void onCreate_log_init()
    {
        checkBox_log.setChecked( Settings.LOGGING );
        log_text.setText("");
        log_text.setHorizontalScrollBarEnabled(true);
        log_text.setPadding(50,5,5,5);

    }
    //}}}
    // onCreate_WVTools {{{
    private void onCreate_WVTools()
    {
        wvTools = new WVTools(this);
    }
    //}}}
//
    // onPostCreate {{{
    //  Trigger the initial hide() shortly after the activity has been
    //  created, to briefly hint to the user that UI controls
    //  are available.
    protected void onPostCreate(Bundle savedInstanceState)
    {
        String caller = "onPostCreate";

        if(D || Settings.LOG_CAT) log_life_cycle(caller);
        checkBox_log .setChecked( Settings.LOGGING );

        Clipboard.onPostCreate();
      //Clipboard.FetchClipData();

//*POLL*/Settings.MOC(TAG_POLL, "onPostCreate");
        this_RTabsClient.progress_POLL();
    }

    //}}}
    // onStart {{{
    //     Called just before the activity becomes visible to the user
    //     Called by onRestart() after being stopped
    protected void onStart()
    {
        String caller = "onStart";
        if(D || Settings.LOG_CAT) log_life_cycle(caller);
        checkBox_log .setChecked( Settings.LOGGING );
    }
    //}}}
    // onTrimMemory {{{
    //     Called when the activity is no longer visible to the user
    public void onTrimMemory(int level)
    {
        String caller = "onTrimMemory";
        if(D || Settings.LOG_CAT) log_life_cycle(caller);

        // -----------------------------------------------------------------
        // Called when the operating system has determined that it is a good
        // time for a process to trim unneeded memory from its process.
        // -----------------------------------------------------------------
        // * (LRU) least-recently used
        // -----------------------------------------------------------------

        String level_str = "<TRIM_MEMORY_XXX>";
        switch(level)
        {
            // SYSTEM
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE : level_str="RUNNING_MODERATE:  low on memory."                   ; break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW      : level_str="RUNNING_LOW.....: Mlow on memory"                    ; break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL : level_str="RUNNING_CRITICAL: Xlow on memory .. killing services"; break;
            // PROCESS
            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN        : level_str="UI_HIDDEN.......: UI not visible"                    ; break;
            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND       : level_str="BACKGROUND......:  low on memory .. process near LRU"; break;
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE         : level_str="MODERATE........: Mlow on memory .. process  mid LRU"; break;
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE         : level_str="COMPLETE........: Xlow on memory .. process head LRU"; break;
        }
        if(D || Settings.LOG_CAT) MLog.log(caller+": "+level_str);
    }
    //}}}
    // onPause {{{
    //     Called when the system is about to start another activity
    //     Save activity state (optional), for example UI values
    protected void onPause()
    {
        String caller = "onPause";
        if(D || Settings.LOG_CAT) log_life_cycle(caller);

        this_RTabsClient.clear_PROF_Map(caller);
        this_RTabsClient.clear_CTRL_Map(caller);

        mOrientationListener.disable();

        if( is_dialog_showing(note_dialog) ) note_dialog.dismiss();

        MWebView_pool_onPause(caller);
    }
    //}}}
    // onSaveInstanceState {{{
    protected void onSaveInstanceState(Bundle outState)
    {
        String caller = "onSaveInstanceState";
        if(D || Settings.LOG_CAT) log_life_cycle(caller);

        this_RTabsClient.stop_BatteryPoll(caller); /* BatteryPoll */
    }
    //}}}
    // onRestoreInstanceState {{{
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        String caller = "onRestoreInstanceState";
        if(D || Settings.LOG_CAT) log_life_cycle(caller);
    }
    //}}}
    // onStop {{{
    //     Called when the activity is no longer visible to the user
    protected void onStop()
    {
        String caller = "onStop";
        if(D || Settings.LOG_CAT) log_life_cycle(caller);

        // -----------------------------------------------------------
        // FORGET WHAT WILL NOT BE THERE WHEN RESTARTING
        // -----------------------------------------------------------
        // TODO some more checking to understand what's going on here!
        // -----------------------------------------------------------
        // (151020) data may persist with issues in tabs_container after a finish (...has something to do with the Looper ... i.e. log not working anymore)
        // (160203) tabs_container will!!! change... children will be parented with the previous instance
        //this_RTabsClient.clear_TABS_Map    (this_RTabsClient.TABS_Map, "activity.onStop");
        //this_RTabsClient.clear_PALETTES_Map(                           "activity.onStop");
        // -----------------------------------------------------------

        stop_activity(caller);

    }
    //}}}
    // onRestart {{{
    //     Called after the activity has been stopped and just prior to it being started again
    protected void onRestart()
    {
        String caller = "onRestart";

        //Settings.LoadSettings(caller);
        checkBox_log.setChecked( Settings.LOGGING );
        if(D || Settings.LOG_CAT) log_life_cycle(caller);
        checkBox_log .setChecked( Settings.LOGGING );
    }
    //}}}
    // onResume {{{
    //     Called just before the activity starts interacting with the user
    //     Restores saved information, if any
    protected void onResume()
    {
        String caller = "onResume";

        if(D || Settings.LOG_CAT) log_life_cycle(caller);

        checkBox_log .setChecked( Settings.LOGGING );

        reset_GUI_idle_timer (       caller);
        handle_hide          (       caller);
        invalidate           (       caller); // initial setup
        sync_SUI_visibility  (       caller); // onResume
        set_APP_freezed_state(false ,caller);

        hide_system_bars(caller);

        if( fs_webView           != null   ) fs_webView  .onResume();
        if( is_view_showing(fs_webView2)   ) fs_webView2 .onResume();
        if( is_view_showing(fs_webView3)   ) fs_webView3 .onResume();

        RTabsClient.TABS_Map_Has_Changed = true; // XXX .. (i.e. FitText after adding some URL while paused)

        mOrientationListener.enable();
        handler.re_postDelayed    ( hr_sync_orientation, 100);

        MWebView_pool_onResume(caller);

        enter_notification_loop(current_stage,caller);

        // cycle dock_band open-fold
        open_band(hist_band, caller);
    }
    private final Runnable hr_sync_orientation = new Runnable() {
        @Override public void run() {
            check_orientation( FROM_RESUME );
        }
    };
    //}}}
    // onDestroy {{{
    //     Called before the activity is destroyed
    //     Unbinds services
    //     This is the final call that the activity will receive
    protected void onDestroy()
    {
        String caller = "onDestroy";

        if(D || Settings.LOG_CAT) log_life_cycle(caller);

        Clipboard.onDestroy();

        this_RTabsClient.stop_BatteryPoll(caller); /* BatteryPoll */
    }
    //}}}
    // stop_activity {{{
    private void stop_activity(String caller)
    {
        caller += "->stop_activity";
        if(Settings.LOG_CAT) Settings.MOC(TAG_BAND, caller);

        this_RTabsClient.disconnect( caller );

        int changes = get_Working_profile_pending_changes();
        if(changes > 0)
        {
            if(Settings.LOG_CAT) MLog.log(caller+": Committing "+changes+" pending changes to PROFILE ["+Settings.Working_profile_instance.name+"]");

            Profile.Save_Profile( Settings.Working_profile_instance );
            caller += "("+changes+" changes saved to "+Settings.Working_profile_instance.name+")";
        }
        Settings.SaveSettings(caller);

        Settings.SaveCookies(caller);

        if( is_dialog_showing(note_dialog) ) note_dialog.dismiss();
    }
    //}}}
//
    // handle_MEMORY {{{
    private void handle_MEMORY()
    {
        // WVTOOLS {{{
        String caller = "handle_MEMORY";
//*TOOL*/Settings.MOC(TAG_TOOL, "handle_MEMORY");
        if(D || Settings.LOG_CAT) log_life_cycle( LIFE_CYCLE_GC );

        if(wvTools != null)
            wvTools.property_reset(caller);

        WVTools.clear_MARK_Map();
        // }}}
        // WEBVIEW {{{
        MWebView_pool_clear();

        Settings.ClearCache();
        // }}}
        // SOUND {{{
//*TOOL*/Settings.MOM(TAG_TOOL, "releasing SoundPool instances");
        SoundPoolManager.handle_MEMORY();

        // }}}
        // SANDBOX {{{
        SandBox         .handle_MEMORY();

        // }}}
        // GC {{{
//*TOOL*/Settings.MOM(TAG_TOOL, "calling GC");

        Runtime.getRuntime().gc();
        //}}}
    }
    //}}}

    //}}}
    /** HANDLE */
    //{{{

    // onCreate_handles {{{
    private void onCreate_handles()
    {
        //{{{
        RelativeLayout.LayoutParams rlp;

        //}}}
        // [frameLayout] -> [handles_container] {{{
        // layout.xml {{{
        //handles_container = (RelativeLayout)findViewById(R.id.handles_container);
        // <RelativeLayout
        //  android:id="@+id/handles_container">
        //  android:layout_width="match_parent"
        //  android:layout_height="match_parent"
        //  android:layout_gravity="center_horizontal|bottom"
        //}}}
        handles_container = new RelativeLayout( activity );
        //{{{
        rlp              = new RelativeLayout.LayoutParams(0, 0);
        rlp.height       = ViewGroup.LayoutParams.MATCH_PARENT;
        rlp.width        = ViewGroup.LayoutParams.MATCH_PARENT;
        handles_container.setLayoutParams( rlp );

        handles_container.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        //}}}
        // }}}
        frameLayout.addView( handles_container );
        // HANDLES {{{
        lft_handle = new Handle(activity, "lft_handle");
        dck_handle = new Handle(activity, "dck_handle");
        top_handle = new Handle(activity, "top_handle");
        mid_handle = new Handle(activity, "mid_handle");
        bot_handle = new Handle(activity, "bot_handle");

    //  lft_handle
        dck_handle.set_LAYOUT_DOCK();
        top_handle.set_LAYOUT_SCREEN();
        mid_handle.set_LAYOUT_SCREEN();
    //  bot_handle

        //}}}
        // PROFILE_DOCK {{{
        create_dock();

        // }}}
        // PACK CONTAINERS {{{
        handles_container     .addView(lft_handle  );
        handles_container     .addView( dck_handle );
        handles_container     .addView( top_handle );
        handles_container     .addView( mid_handle );
        handles_container     .addView( bot_handle );

        // }}}
        // VIEW OPACITY .. (not the alpha component of the Paint used by view's onDraw) {{{
        lft_handle.setAlpha( 0.50F );
        dck_handle.setAlpha( 0.99F );
        top_handle.setAlpha( 0.95F );
        mid_handle.setAlpha( 0.95F );
        bot_handle.setAlpha( 0.50F );

        // }}}
        // BACKGROUND {{{
        lft_handle.setBackgroundColor( Settings.LFT_HANDLE_COLOR );
        dck_handle.setBackgroundColor( Settings.DCK_HANDLE_COLOR );
        top_handle.setBackgroundColor( Settings.TOP_HANDLE_COLOR );
        mid_handle.setBackgroundColor( Settings.MID_HANDLE_COLOR );
        bot_handle.setBackgroundColor( Settings.BOT_HANDLE_COLOR );

        // }}}
        // ELEVATION {{{
        ViewCompat.setElevation(bot_handle, Settings.BOT_HANDLE_ELEVATION );
        ViewCompat.setElevation(dck_handle, Settings.DCK_HANDLE_ELEVATION );
        ViewCompat.setElevation(lft_handle, Settings.LFT_HANDLE_ELEVATION );
        ViewCompat.setElevation(mid_handle, Settings.MID_HANDLE_ELEVATION );
        ViewCompat.setElevation(top_handle, Settings.TOP_HANDLE_ELEVATION );

        // }}}
        // ALIGNMENT {{{
        dck_handle.setGravity( Gravity.TOP    | Gravity.END);
        top_handle.setGravity( Gravity.CENTER              );
        mid_handle.setGravity( Gravity.CENTER              );

        //}}}
        adjust_handles("onCreate_handles", FROM_CREATE);
    }
    //}}}
    // create_dock {{{
    private void create_dock()
    {
        if(M||D) Settings.MON(TAG_BAND, "create_dock", "create_dock");

        // 3 activation-color-stripes
        // .. (to show dock, hist or cart)
        // .. (may be hidden by: show_band_hide show_band_show)
        show_band = new ShowBand( activity );
        show_band.set_show_hist_OnTouchListener  ( show_hist_OnTouchListener  );
        show_band.set_show_dock_OnTouchListener  ( show_dock_OnTouchListener  );
        show_band.set_show_cart_OnTouchListener  ( show_cart_OnTouchListener  );

        //
        dock_band = new DockBand( activity );
    //  dock_band.set_dock_hide_OnTouchListener  ( show_hist_OnTouchListener  );

        hist_band = new HistBand( activity );
        hist_band.back_nb.setOnTouchListener     ( builtin_nb_OnTouchListener );
        hist_band.frwd_nb.setOnTouchListener     ( builtin_nb_OnTouchListener );
        hist_band.prof_nb.setOnTouchListener     ( builtin_nb_OnTouchListener );

        cart_band = new CartBand( activity );
        cart_band.see_nb.setOnTouchListener      ( builtin_nb_OnTouchListener );
        cart_band.end_nb.setOnTouchListener      ( builtin_nb_OnTouchListener );
    //  cart_band.see_nb.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { cart_see_click(); } });
    //  cart_band.end_nb.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) { cart_end_click(); } });

        // INITIALLY HIDDEN
    //  show_band.setVisibility( View.GONE );
        hist_band.setVisibility( View.GONE );
        dock_band.setVisibility( View.GONE );
        cart_band.setVisibility( View.GONE );

        // .. (see sync_dock_GUI_TYPE)
    }
    //}}}
    // create_tooltip {{{
    // {{{
    private static final   int TOOLTIP_OPACITY       = 0xf0;
    private static final   int TOOLTIP_SHOW_DELAY    =  500;
    private static final   int TOOLTIP_HIDE_DELAY    = 1000;
    private static final   int TOOLTIP_HIDE_DURATION =  500;

    private NotePane tooltip_np;
    private View     tt_for_view;
    // }}}
    // create_tooltip {{{
    private void create_tooltip()
    {
        // NotePane {{{
        String  name = "tooltip_np";
        String  type = "SHORTCUT";
        int        x = 0;
        int        y = 0;
        int        w = 1;
        int        h = 1;
        int        z = 0;
        String  tag  = "";
        String  text = "";
        int    color = 0;
        String shape = NotePane.SHAPE_TAG_CIRCLE;
        String    tt = "";
        tooltip_np = new NotePane(name, type, x, y, w, h, z, tag, text, color, shape, tt);

        //}}}
        // NpButton {{{
        tooltip_np.button = new NpButton( activity );
        tooltip_np.button.lockElevation     ( Settings.WV_BUTTON_ELEVATION );
        tooltip_np.button.set_shape         ( tooltip_np.shape      , false); // with_outline
    //  tooltip_np.button.setTypeface       ( Settings.getNotoTypeface()   );
        tooltip_np.button.setActive         ( true                         );
        tooltip_np.button.setShadowLayer    ( 6,1,1,Color.BLACK            );
        tooltip_np.button.setTextColor      ( Color.WHITE                  );

        tooltip_np.button.setText           ("");
        tooltip_np.button.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL); // override NpButton.setText

        //}}}
        // GUI
        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(0, 0);
        flp.leftMargin = 0;
        flp.topMargin  = 0;
        flp.height     = Settings.TAB_GRID_S;
        flp.width      = Settings.TAB_GRID_S;
        tooltip_np.button.setLayoutParams( flp );

        frameLayout.addView( tooltip_np.button );

        // EVENTS
        tooltip_np.button.setOnClickListener( fs_tooltip_OnClickListener );
    }
    //}}}
    // show_tooltip {{{
    private void show_tooltip(String textAndInfo, int bg_color, View view)
    {
        if(tooltip_np == null)
            create_tooltip();

        hide_tooltip();

        tt_for_view                          = view;
        tooltip_np.setTextAndInfo           ( textAndInfo );
        tooltip_np.button.setBackgroundColor( bg_color );

        handler.re_postDelayed    ( hr_show_tooltip, TOOLTIP_SHOW_DELAY);
    }
//}}}
    // hide_tooltip {{{
    private void hide_tooltip()
    {
        if(tooltip_np == null)
            return;

        tooltip_np.button.setVisibility( View.GONE       );
        tooltip_np.button.setOpacity   ( TOOLTIP_OPACITY );

        handler.removeCallbacks( hr_hide_tooltip_alpha );
        handler.removeCallbacks( hr_show_tooltip       );
    }
    //}}}
    // fs_tooltip_OnClickListener {{{
    private final View.OnClickListener fs_tooltip_OnClickListener = new View.OnClickListener()
    {
        @Override public void onClick(View view)
        {
            magnify_np(tooltip_np, "fs_tooltip_OnClickListener");
        }
    };
    //}}}
    // adjust_tooltip {{{
    private void adjust_tooltip()
    {
        //  int radius     = Math.min(Settings.SCREEN_W, Settings.SCREEN_H) / 4;

        int leftMargin = 0;
        int topMargin  = 0;
        int height     = 100;
        int width      = 100;

        if(tt_for_view != null) {
            int[]  xy  = new int[2]; tt_for_view.getLocationOnScreen( xy );
            leftMargin = xy[0] + tt_for_view.getWidth();
            topMargin  = xy[1];
        }

        FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams)tooltip_np.button.getLayoutParams();
        flp.leftMargin = leftMargin;
        flp.topMargin  = topMargin;
        flp.height     = height;
        flp.width      = width;
    }
    //}}}
    // hr_show_tooltip {{{
    private final Runnable hr_show_tooltip = new Runnable()
    {
        @Override public void run()
        {
            adjust_tooltip();

            tooltip_np.button.setOpacity( TOOLTIP_OPACITY );
            tooltip_np.button.setVisibility( View.VISIBLE );

            handler.re_postDelayed(hr_hide_tooltip_alpha, TOOLTIP_HIDE_DELAY);
        }
    };
    //}}}
    // hr_hide_tooltip_alpha {{{
    private final Runnable hr_hide_tooltip_alpha = new Runnable()
    {
        @Override public void run()
        {
            if( ANIM_SUPPORTED ) {
                AnimatorSet set = new AnimatorSet();
                set.setDuration( TOOLTIP_HIDE_DURATION );
                set.setInterpolator(new DecelerateInterpolator());
                set.play(ObjectAnimator.ofFloat(tooltip_np.button, View.ALPHA, 0f)); // view alpha
                set.start();
            }
            else {
                tooltip_np.button.setAlpha(0f); // view alpha
            }
        }
    };
    //}}}
    //}}}
    //}}}
    /** KEYBOARD */
    //{{{
    // onKeyDown {{{
    //@Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent)
    {
        // keyboard input {{{
        String caller = "onKeyDown";
//*KEYBOARD*/Settings.MON(TAG_KEYBOARD, caller, "["+KeyEvent.keyCodeToString(keyCode).substring(8)+"]");  // [KEYCODE_ESCAPE] ==> [ESCAPE]
//*KEYBOARD*/Settings.MOM(TAG_KEYBOARD, "........is_magnify_np_showing: "+ is_magnify_np_showing());
//*KEYBOARD*/Settings.MOM(TAG_KEYBOARD, "...fs_webview_session_running: "+ fs_webview_session_running);

        //  ||              ((keyCode == KeyEvent.KEYCODE_TAB       ) && (keyEvent.isCtrlPressed()))
        //  ||              ((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) && (keyEvent.isAltPressed ()))
/*
:!start C:/LOCAL/DATA/ANDROID/sdk/platform-tools/adb shell input text "FINISH"
:!start C:/LOCAL/DATA/ANDROID/sdk/platform-tools/adb shell input keyevent 187
make is /usr/bin/make
*/
        //}}}
        // STOP ACTIVITY .. (consumed) {{{
        boolean adb_is_about_to_install_apk
            //(keyCode == KeyEvent.KEYCODE_APP_SWITCH ) // =[187] Key code constant: App switch key.   * Should bring up the application switcher dialog.
            = (keyCode == KeyEvent.KEYCODE_HEADSETHOOK) // =[ 79] Key code constant: Headset Hook key. * Used to hang up calls and stop media.
            ;

        if(adb_is_about_to_install_apk)
        {
Settings.MOM(TAG_KEYBOARD, caller+": KEYCODE_HEADSETHOOK .. (adb_is_about_to_install_apk)");
            stop_activity(caller);
            activity.finish();
            return true; // consumed
        }
        //}}}
        // GO BACK .. (consumed) {{{
        boolean go_back
            =  (keyCode == KeyEvent.KEYCODE_BACK   )
            || ((keyCode == KeyEvent.KEYCODE_TAB   ) && (keyEvent.isShiftPressed()));
//*KEYBOARD*/Settings.MOM(TAG_KEYBOARD, "go_back=["+go_back+"]");

        if( go_back ) {
//*KEYBOARD*/Settings.MOM(TAG_KEYBOARD, "go_back: keyEvent=["+keyEvent+"]");
            // GUI_STATE {{{
            if(GUI_STATE != GUI_STATE_TABS)
            {
//*KEYBOARD*/Settings.MOM(TAG_KEYBOARD, "GUI_STATE_TABS");
                apply_GUI_STATE(GUI_STATE_TABS, FROM_KEYBOARD, caller);
            }
            //}}}
            // WEBVIEW: [HISTORY MASK GRAB COLLAPSE HIDE] {{{
            else if( fs_webview_session_running ) {
//*KEYBOARD*/Settings.MOM(TAG_KEYBOARD, "WEBVIEW-BACK: canGoBack=["+ fs_webView.canGoBack() +"]");
                if( fs_webView.canGoBack() )
                {
                    fs_webView.goBack();
                }
                else {
///*KEYBOARD*/Settings.MOM(TAG_KEYBOARD, "WEBVIEW-CYCLE: [show]->[expanded]->[collapsed]->[free]->[grabbed]->[hidden]");
//                    fs_webView_session_cycle_grab_collapse_or_hide(caller);
                    play_sound_click(caller);
                }
            }
            //}}}
            // DOCK BAND {{{
            else if(   (hist_band.getParent()     ==         null)
                    || (hist_band.getVisibility() != View.VISIBLE)
              ) {
//*KEYBOARD*/Settings.MOM(TAG_KEYBOARD, "hist_band");
                show_histBand(FROM_KEYBOARD, caller);
              }
            //}}}
            // FULLSCREEN DISMISS {{{
            else if( is_magnify_np_showing() )
            {
                magnify_np_hide(caller);
            }
            //}}}
            // HANDLE DISMISS {{{
            else if(Handle.Get_cur_handle() != null)
            {
                handle_hide(caller);
                //Handle.Re_post_dimm();
            }
            //}}}
            // PROFILE HISTORY BACK {{{
            else {
//*KEYBOARD*/Settings.MOM(TAG_KEYBOARD, "PROFILE-BACK");
                history_back(FROM_KEYBOARD, caller);
            }
            //}}}
            return true; // consumed
        }
        // }}}
        // GO FORWARD .. (consumed) {{{
        boolean go_forward
            =  (keyCode == KeyEvent.KEYCODE_FORWARD)
            || (keyCode == KeyEvent.KEYCODE_TAB    );
//*KEYBOARD*/Settings.MOM(TAG_KEYBOARD, "go_forward=["+go_forward+"]");

        if( go_forward ) {
//*KEYBOARD*/Settings.MOM(TAG_KEYBOARD, "go_forward: keyEvent=["+keyEvent+"]");
            // WEBVIEW HISTORY FORWARD {{{
            if( fs_webview_session_running ) {
//*KEYBOARD*/Settings.MOM(TAG_KEYBOARD, "WEBVIEW-FWD: canGoForward=["+ fs_webView.canGoForward() +"]");
                if( fs_webView.canGoForward() ) {
                    fs_webView.goForward();
                }
                else {
//*KEYBOARD*/Settings.MOM(TAG_KEYBOARD, "ToneGenerator:");
                  //ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 50);
                  //toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                    play_sound_ding(caller);
                }
            }
            //}}}
            // PROFILE HISTORY FORWARD {{{
            else {
                history_frwd(FROM_KEYBOARD, caller);
//*KEYBOARD*/Settings.MOM(TAG_KEYBOARD, "PROFILE-FWD");
            }
            //}}}
            return true; // consumed
        }
        // }}}
        // IGNORE .. (not consumed) {{{
        else if( fs_webview_session_running )
        {
            return false; // not consumed
        }
        else {
            return false;
        }
        // }}}
    }
    //}}}
    // onBackPressed  {{{
    //@Override
    public void onBackPressed ()
    {
        // The default implementation simply finishes the current activity
//*KEYBOARD*/Settings.MOC(TAG_KEYBOARD, "onBackPressed");
    }
    //}}}
    //}}}
    /** DATA */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ DATA @ {{{
    //* (fetching) */
    // enter_notification_loop {{{
    private int           current_stage = STAGE5_TABS_LAYOUT;

    private void enter_notification_loop(int stage, String caller)
    {
        caller += "->enter_notification_loop("+get_stage_name( stage )+")";
//*DATA*/Settings.MOC(TAG_DATA, caller);

        // FREEZED-LOCK {{{
        if( Settings.FREEZED ) {
            toast_short(Settings.SYMBOL_freezed+" FREEZED CONNECTION "+Settings.SYMBOL_freezed+"\n...WILL NOT PROCESS STAGE "+get_stage_name(stage));
            return;
        }
        //}}}
        // OFFLINE {{{
        if( Settings.OFFLINE ) {
            toast_short(Settings.SYMBOL_offline+" OFFLINE "+Settings.SYMBOL_offline+"\n...WILL NOT PROCESS STAGE "+get_stage_name(stage));
            return;
        }
        //}}}
        toast_again_clear();

        // START POLLING
        poll_looper_start( caller );

        // LOAD WORKING PROFILE
        if(        (RTabsClient.TABS_Map.size() == 0    )
                && (load_profile_timerTask      == null))
        {
            select_WORKING_PROFILE(caller);

            select_and_load_working_profile(caller);

        //  schedule_select_and_load_working_profile_TASK();
/*// {{{
:41.739 W: GRAPHIC_PATH MAX=[3840 x 2400]
:42.066 W: schedule_select_and_load_working_profile_TASK
:42.069 W: select_and_load_working_profile_TASK: Working_profile=[index]
:42.170 I: <qeglDrvAPI_eglInitialize:410>: EGL 1.4 QUALCOMM build: AU_LINUX_ANDROID_LA.BF.1.1.1_RB1.05.01.00.042.030_msm8974_LA.BF.1.1.1_RB1__release_AU ()
           OpenGL ES Shader Compiler Version: E031.25.03.06
           Build Date: 07/13/15 Mon
           Local Branch: mybranch11906725
           Remote Branch: quic/LA.BF.1.1.1_rb1.26
           Local Patches: NONE
           Reconstruct Branch: AU_LINUX_ANDROID_LA.BF.1.1.1_RB1.05.01.00.042.030 + 6151be1 + a1e0343 + 002d7d6 + 7d0e3f7 +  NOTHING
:42.170 I: Initialized EGL, version 1.4
:42.373 E: FATAL EXCEPTION: Timer-0
           Process: ivanwfr.rtabs, PID: 29215
           android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
               at android.view.ViewRootImpl.checkThread(ViewRootImpl.java:6373)
               at android.view.ViewRootImpl.invalidateChildInParent(ViewRootImpl.java:913)
               at android.view.ViewGroup.invalidateChild(ViewGroup.java:4691)
               at android.view.View.invalidateInternal(View.java:11877)
               at android.view.View.invalidate(View.java:11841)
               at android.view.View.invalidate(View.java:11825)
               at android.widget.TextView.checkForRelayout(TextView.java:6915)
               at android.widget.TextView.setText(TextView.java:4096)
               at android.widget.TextView.setText(TextView.java:3954)
               at android.widget.TextView.setText(TextView.java:3929)
               at ivanwfr.rtabs.NpButton.setText(NpButton.java:369)
               at ivanwfr.rtabs.FullscreenActivity.update_histBand(FullscreenActivity.java:6023)
               at ivanwfr.rtabs.RTabsClient.load_USER_PROFILE(RTabsClient.java:1994)
               at ivanwfr.rtabs.FullscreenActivity.select_and_load_working_profile(FullscreenActivity.java:956)
               at ivanwfr.rtabs.FullscreenActivity.select_and_load_working_profile_TASK(FullscreenActivity.java:939)
               at ivanwfr.rtabs.FullscreenActivity.access$2200(FullscreenActivity.java:90)
               at ivanwfr.rtabs.FullscreenActivity$5.run(FullscreenActivity.java:932)
               at java.util.Timer$TimerImpl.run(Timer.java:284)
:52.835 W: Suspending all threads took: 6.009ms

*/// }}}
/*//{{{

 java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
     at android.view.ViewGroup.addViewInner(ViewGroup.java:3937)
     at android.view.ViewGroup.addView(ViewGroup.java:3787)
     at android.view.ViewGroup.addView(ViewGroup.java:3728)
     at android.view.ViewGroup.addView(ViewGroup.java:3701)
     at ivanwfr.rtabs.RTabsClient.apply_TABS_LAYOUT(RTabsClient.java:2909)
     at ivanwfr.rtabs.FullscreenActivity.invalidate(FullscreenActivity.java:6482)

*///}}}
        }

        // data_looper .. WILL HANDLE WHAT HAPPENS NEXT
        if(current_stage != stage) {
            current_stage = stage;
            update_stage(caller);
        }

        data_looper_start(caller);
    }
    //}}}
    // schedule_select_and_load_working_profile_TASK {{{
    private TimerTask load_profile_timerTask = null;
    private void schedule_select_and_load_working_profile_TASK()
    {
//*DATA*/Settings.MOC(TAG_DATA, "schedule_select_and_load_working_profile_TASK");
        // CANCEL OLD (IF NOT DONE YET)
        if(load_profile_timerTask != null) load_profile_timerTask.cancel();

        // SCHEDULE NEW
        load_profile_timerTask = new TimerTask() { @Override public void run() { select_and_load_working_profile_TASK(); } };
        new Timer().schedule(load_profile_timerTask, 0);
    }

    private synchronized void select_and_load_working_profile_TASK()
    {
        String caller = "select_and_load_working_profile_TASK";
//*DATA*/Settings.MON(TAG_DATA, caller, "Working_profile=["+ Settings.Working_profile +"]");
        select_and_load_working_profile("select_and_load_working_profile_TASK");
        load_profile_timerTask = null;
//*DATA*/Settings.MON(TAG_DATA, caller, "...DONE");
    }
    //}}}
    // select_and_load_working_profile {{{
    private void select_and_load_working_profile(String caller)
    {
        caller += "->select_and_load_working_profile";
        if(M||D) MLog.log(caller);

        if( !TextUtils.isEmpty( Settings.Working_profile) )
        {
            Settings.PROFILE = Settings.Working_profile;
            if(D) MLog.log("LOADING PROFILE ["+ Settings.PROFILE +"]");

            this_RTabsClient.load_USER_PROFILE(Settings.PROFILE, caller);

            // PALETTES AND TABS PARSED AND LAYOUT DONE
            if( Settings.LoadedProfile.isValid() )
            {
                if( !this_RTabsClient.isConnected() )
                    set_stage(STAGE6_CONNECTING , "@@@ WORKING PROFILE LOADED");
            }
            else {
                if(Settings.PROFILE.equals( Settings.Working_profile ))
                    Settings.Working_profile = "";
                Settings.PROFILE = "";
            }
        }

        if( TextUtils.isEmpty( Settings.PROFILE ) ) {
            MLog.log("WORKING PROFILE NOT LOADED: DEFAULTING TO LOCAL STORAGE PROFILES TABLE");
            if( Settings.is_GUI_TYPE_HANDLES()) show_PROFILES_TABLE(caller);
            else                                show_DOCKINGS_TABLE(caller);
        }
    }
    //}}}
    //* (data_looper) */
    //{{{
    /* data_looper_start {{{*/
    private void data_looper_start(String caller)
    {
        data_looper_start(0, caller);
    }

    private void data_looper_start(int delay, String caller)
    {
        hr_data_looper_caller = caller;

        if(delay > 0) handler.re_postDelayed( hr_data_looper, delay);
        else          handler.re_post       ( hr_data_looper       );
    }
    /*}}}*/
    /* data_looper_stop {{{*/
    private void data_looper_stop(String caller)
    {
        handler.removeCallbacks( hr_data_looper );

    }
    /*}}}*/
    /* hr_data_looper {{{*/
    private       String   hr_data_looper_caller = "";

    private final Runnable hr_data_looper        = new Runnable()
    { @Override public void run()
        {
            //{{{
            if(D) MLog.log(""
                    + "@ @ @ [hr_data_looper]\n"
                    + "@ @ @ caller=["+ hr_data_looper_caller +"]\n"
                    + "@ @ @ [--------------]"
                    );

            //}}}
            // FREEZED {{{
            if( Settings.FREEZED ) {
                if(D) MLog.log("..."+Settings.SYMBOL_freezed+" FREEZED");

                return;
            }
            //}}}
            // OFFLINE {{{
            if( Settings.OFFLINE ) {
if(D) MLog.log("..."+Settings.SYMBOL_offline+" OFFLINE");

                return;
            }
            //}}}
            // 0 - ESTABLISH A WORKING PROFILE {{{
            String stage_name = get_stage_name( current_stage );

            if(         TextUtils.isEmpty( Settings.LoadedProfile.name )
                    && !TextUtils.isEmpty( Settings.Working_profile    )
              ) {
                if(current_stage != STAGE8_PROFILE_UPDATE) {
                    MLog.log("LoadedProfile.name not set yet");
                    set_stage(STAGE8_PROFILE_UPDATE, "hr_data_looper ["+stage_name+"]");
                }
                // ...come back here to check if done
                data_looper_start(DATA_LOOPER_BASE_PERIOD, "hr_data_looper[WORKING PROFILE]");
                return;
              }
            //}}}
            // 1 - SOME DATA TO PARSE ? {{{
            if( have_data_to_process_after("hr_data_looper ["+ stage_name +"]") )
            {
                if(D) MLog.log( Settings.SYMBOL_new_data );
                parse_received_data("hr_data_looper");

                // ...come back here to check if done
                data_looper_start(DATA_LOOPER_BASE_PERIOD, "hr_data_looper[DATA]");
                return;
            }
            // }}}
            // 2 - TRYING TO SIGNIN ? {{{
/*
            if(!this_RTabsClient.is_signed_in())
            {
                this_RTabsClient.discard_received_message("hr_data_looper TRYING TO SIGNIN");

                set_stage(STAGE0_SIGNIN, "hr_data_looper ["+stage_name+"]");

                // ...come back here to check if done
                data_looper_start(get_next_server_hook_delay(), "hr_data_looper[SIGNIN]");
                return;
            }
*/
            // }}}
            // 3 - LOOP BACK HERE ...UNTIL ALL DATA HAS BEEN PROCESSED {{{
            if(         (current_stage <  STAGE5_TABS_LAYOUT)
                    || ((current_stage == STAGE6_CONNECTING ) && !this_RTabsClient.isConnected())
                    ||  (current_stage >  STAGE7_POLLING    )
              ) {

                String caller = "hr_data_looper: ["+ stage_name +"]";
                update_stage( caller );

                // ...come back here to check if done
                int delay = get_next_server_hook_delay();
                if(D)    MLog.log(caller + " ...next loop in "+ delay +"ms");
                data_looper_start(delay, caller);

                return;
              }
            // }}}
            // 4 - DATA AVAILABLE AND PROCESSED .. STEP FROM TRANSITIONAL STAGE TO CONNECTION AND POLL STAGE {{{
            if(        (current_stage == STAGE5_TABS_LAYOUT  )
                    || (current_stage == STAGE6_CONNECTING   )
                    || (current_stage == STAGE9_PROFILE_PARSE)
              ) {
                if(M||D) MLog.log(Settings.SYMBOL_got_data+ " DATA AVAILABLE AND PROCESSED "+ Settings.SYMBOL_got_data);

                // LOAD WORKING PROFILE .. OR DEFAULT TO PROFILES_TABLE {{{
                if(D) MLog.log("...WORKING PROFILE: ["+ Settings.Working_profile +"]");
                if(D) MLog.log(".....LoadedProfile: ["+ Settings.LoadedProfile.toString() );
                if(D) MLog.log("..........TABS_Map: ["+ RTabsClient.TABS_Map.size() +" USER TABS]");
                if(RTabsClient.TABS_Map.size() == 0)
                {
                    String caller = "hr_data_looper: ["+ stage_name +"]";
                    if( Settings.is_GUI_TYPE_HANDLES()) show_PROFILES_TABLE(caller);
                    else                                show_DOCKINGS_TABLE(caller);
                }
                //}}}
                // DISPLAY DATA {{{
                else {
                    // HIDE..SHOW SYSTEM UI
                    if(D) MLog.log_center("TABS DISPLAYED STAGE=["+stage_name+"]");
                    if( !Settings.LOGGING ) hide_system_bars("hr_data_looper");
                //  else                    show_system_bars("hr_data_looper");

                    // RENDER RECEIVED TABS AND PALETTE
                    if(this_RTabsClient.needs_TABS_Map_ENTRY_PALETTE())
                        this_RTabsClient.apply_SETTINGS_PALETTE(RTabsClient.TABS_Map, "hr_data_looper ["+stage_name+"]");
                }
                //}}}

                // CONNECT TO SERVER
                if( !this_RTabsClient.isConnected() ) {
if(D) MLog.log_center("ENTERING CONNECTION STAGE AFTER ["+stage_name+"]");

                    set_stage(STAGE6_CONNECTING,      "hr_data_looper ["+stage_name+"]");
                }
                // ENTER POLL STAGE
                else {
if(D) MLog.log_center("ENTERING POLL STAGE AFTER ["+stage_name+"]");

                    set_stage(STAGE7_POLLING,   "hr_data_looper ["+stage_name+"]");
                }

                // ALWAYS SHOW SOMETHING USEFUL WHEN DONE WITH DATA PROCESSING
                if(RTabsClient.TABS_Map.size() == 0)
                {
                    String caller = "hr_data_looper: ["+ stage_name +"]";
                    if( Settings.is_GUI_TYPE_HANDLES()) show_PROFILES_TABLE(caller);
                    else                                show_DOCKINGS_TABLE(caller);
                }
                // DO NOT come back here .. data fetching is done
                // CONNECTING AND POLLING WILL INITIATE NEXT PROCESSING LOOP
                return;
              }
            // }}}
            // 5 - NOTHING happened {{{
            //... if connected: resume Polling if it has been deactivated (i.e. freezed)
if(D) MLog.log_center("REVERT TO CURRENT STAGE ["+stage_name+"]");

            if( !this_RTabsClient.isConnected() )
                set_stage(STAGE6_CONNECTING, "hr_data_looper ["+stage_name+"]");
            else
                set_stage(    current_stage, "hr_data_looper ["+stage_name+"]");

            // ALWAYS SHOW SOMETHING USEFUL WHEN DONE WITH DATA PROCESSING
            if(RTabsClient.TABS_Map.size() == 0)
            {
                String caller = "hr_data_looper: ["+ stage_name +"]";
                if( Settings.is_GUI_TYPE_HANDLES()) show_PROFILES_TABLE(caller);
                else                                show_DOCKINGS_TABLE(caller);
            }
            //}}}
            // DO NOT come back here either ... would not change anything
        }
    };
    /*}}}*/
    //}}}
    //* (transition) */
    // update_stage {{{
    private int remaining_count = 0;

    private void   update_stage(String caller)
    {
        caller += "->update_stage";
        if(D) MLog.log(caller);

        // HANDLE SERVER CONNECTION


        // ASSESS THE SITUATION AND DECIDE WHAT TO DO NEXT
        boolean connected = this_RTabsClient.isConnected();
        if(D) MLog.log("...connected=["+connected+"]");

        int /* ...default to remain on current stage.......... */         required_stage = current_stage;
        if     (!connected && (current_stage == STAGE0_SIGNIN)          ) required_stage = current_stage        ; // keep on trying to connect
        else if(!connected && (current_stage == STAGE6_CONNECTING)      ) required_stage = current_stage        ; // move along to next step
        else if( connected && (current_stage == STAGE6_CONNECTING)      ) required_stage = STAGE7_POLLING       ; // connect done, step to POLL
        else if( connected && (current_stage == STAGE5_TABS_LAYOUT)     ) required_stage = STAGE7_POLLING       ; //  layout done, step to POLL
        else if(  this_RTabsClient.have_PROFILE_UPDATE  (caller)        ) required_stage = STAGE8_PROFILE_UPDATE;
        else if(  this_RTabsClient.have_PALETTES_MISSING(caller)        ) required_stage = STAGE1_PALETTES_GET  ;
        else if(  this_RTabsClient.have_TABS_MISSING    (caller)        ) required_stage = STAGE3_TABS_GET      ;
        else if(  RTabsClient.TABS_Map_Has_Changed                      ) required_stage = STAGE5_TABS_LAYOUT   ;
        else                                                              required_stage = STAGE7_POLLING       ;

        if(!connected && this_RTabsClient.has_max_connection_failed())
            set_APP_offline_state(true, caller+": MAX_CONNECTION_ATTEMPTS_COUNT");

        // STAGE TRANSISION
        if(required_stage == current_stage) remaining_count += 1;
        else                                remaining_count  = 0;

        String stage_name   = get_stage_name( current_stage );
        @SuppressLint("DefaultLocale") String msg          = connected
            ?                 "["+ stage_name +"]"
            :   String.format("[%s %3s] [%s]:[%d]"
                    ,           stage_name
                    ,             "#"+remaining_count
                    ,                   Settings.SERVER_IP
                    ,                        Settings.SERVER_PORT)
            ;
        if(required_stage == current_stage) set_stage(current_stage , msg);
        else                                set_stage(required_stage, msg);
    }
    //}}}
    // log_stage_step_banner {{{
    private void log_stage_step_banner()
    {
        // ------------- 123456789_
        String spaces = "          ";
        String sym = Settings.SYMBOL_NO_ENTRY;

        for(int i=1; i<=5; ++i)
            MLog.log(spaces + spaces + sym+sym+sym);

        for(int i=5; i<=10; ++i)
            MLog.log( String.format(spaces
                        +"%."+(   i)+"s"+ sym
                        +"%."+(10-i)+"s"+ sym
                        +"%."+(10-i)+"s"+ sym
                        , spaces
                        , spaces
                        , spaces
                        ) );

    }
    //}}}
    // log_stage_same_banner {{{
    private void log_stage_same_banner()
    {
        // ------------- 123456789_
        String spaces = "          ";
        String sym = Settings.SYMBOL_NO_ENTRY;

        for(int i=1; i<=5; ++i)
            MLog.log(spaces + spaces + sym+sym+sym);
    }
    //}}}
    // set_stage {{{
    private void   set_stage(int stage, String caller)
    {
        String new_stage_name = get_stage_name( stage         );
        String old_stage_name = get_stage_name( current_stage );

        caller += "->set_stage("+new_stage_name+")";

        if(stage != current_stage)
        {
            if(M||D) log_stage_step_banner();
            if(M||D) MLog.log(caller+": STEPPING FROM ["+old_stage_name+"]");

            if(current_stage == STAGE7_POLLING) poll_looper_stop( caller );
            current_stage     = stage;
            //this_RTabsClient.warn_to_dash("STAGE", new_stage_name);
        }
        else {
            if(M||D) log_stage_same_banner();
            if(M||D) MLog.log(caller+": REMAINING ON ["+current_stage+"]");
        }

        // INTERRUPT DEFAULT POLL LOOP
        switch( current_stage )
        {
            case STAGE1_PALETTES_GET    : request_PALETTES_GET    ( caller ); break;
            case STAGE2_PALETTES_PARSE  : request_PALETTES_PARSE();           break;

            case STAGE3_TABS_GET        : request_TABS_GET        ( caller ); break;
            case STAGE4_TABS_PARSE      : request_TABS_PARSE();               break;

            case STAGE5_TABS_LAYOUT     : request_TABS_LAYOUT     ( caller ); break;

            case STAGE7_POLLING         : poll_looper_start       ( caller ); break;

            case STAGE8_PROFILE_UPDATE  : request_PROFILE_UPDATE  ( caller ); break;
            case STAGE9_PROFILE_PARSE   : request_PROFILE_PARSE   ( caller ); break;

            case STAGE6_CONNECTING      :
            case STAGE0_SIGNIN          :
            default                     : request_SIGNIN          ( caller ); break;
        }

    }
    //}}}
    // get_current_stage_name {{{
    public String get_current_stage()
    {
        return "STAGE "+get_stage_name( current_stage );
    }
    //}}}
    // get_stage_name {{{
    private String get_stage_name(int stage)
    {
        String stage_str = "?";
        switch(stage) {
            case STAGE0_SIGNIN          : stage_str = RTabsClient.CMD_SIGNIN        ;   break;
            case STAGE1_PALETTES_GET    : stage_str = RTabsClient.CMD_PALETTES_GET  ;   break;
            case STAGE2_PALETTES_PARSE  : stage_str = "PALETTES_PARSE"              ;   break;
            case STAGE3_TABS_GET        : stage_str = RTabsClient.CMD_TABS_GET      ;   break;
            case STAGE4_TABS_PARSE      : stage_str = "TABS_PARSE"                  ;   break;
            case STAGE5_TABS_LAYOUT     : stage_str = "TABS_LAYOUT"                 ;   break;
            case STAGE6_CONNECTING      : stage_str = "CONNECTING"                  ;   break;
            case STAGE7_POLLING         : stage_str = "POLLING"                     ;   break;
            case STAGE8_PROFILE_UPDATE  : stage_str = "PROFILE_UPDATE"              ;   break;
            case STAGE9_PROFILE_PARSE   : stage_str = "PROFILE_PARSE"               ;   break;
        }
        return stage_str;
    }
    //}}}
    //* (processing) */
    // parse_received_data {{{
    private void   parse_received_data(String caller)
    {
        caller += "->parse_received_data";
        if(M||D) MLog.log(caller+": current_stage=["+get_stage_name(current_stage)+"]");

        // got a reply to STAGE1_PALETTES_GET {{{
        if(        this_RTabsClient.have_PALETTES_MISSING   ( caller )
                && this_RTabsClient.check_PALETTES_GET_reply( caller )
          ) {
            set_stage(STAGE2_PALETTES_PARSE, caller);
            return;
          }
        // }}}
        // STAGE2_PALETTES_PARSE done ... slips into:
        // got a reply to STAGE3_TABS_GET .. STAGE4_TABS_PARSE {{{
        if(        this_RTabsClient.have_TABS_MISSING   ( caller )
                && this_RTabsClient.check_TABS_GET_reply( caller )
          ) {
            set_stage(STAGE4_TABS_PARSE, caller);
            return;
          }
        // }}}
        // STAGE4_TABS_PARSE  done ... slips into:
        // STAGE5_TABS_LAYOUT done ... slips into:
        // got a reply to STAGE7_POLLING {{{
        if( this_RTabsClient.check_POLL_message( caller ) ) {
            this_RTabsClient.parse_POLL();
            return;
        }
        // }}}
        // STAGE7_POLLING interrupt ... slips into:
        // got a reply to STAGE8_PROFILE_UPDATE .. STAGE9_PROFILE_PARSE {{{
        if(        this_RTabsClient. have_PROFILE_UPDATE      ( caller )
                && this_RTabsClient.check_PROFILE_UPDATE_reply( caller )
          ) {
            set_stage(STAGE9_PROFILE_PARSE, caller);
            return;
          }
        // }}}
        // NONE OF THE ABOVE ... resync into:
        // STAGE5_TABS_LAYOUT (current situation report) {{{
        set_stage(STAGE5_TABS_LAYOUT, caller);

        // }}}
    }
    //}}}
    // sync_notify_from {{{
    public  void   sync_notify_from(String caller)
    {
        if(M||D) MLog.log_center( "sync_notify_from("+caller+")");

        // no heavy handling while working with scale_seekBar or palettes_seekBar {{{
        if(        caller.startsWith("apply_SETTINGS_PALETTE")
                || caller.startsWith("apply_TABS_LAYOUT"     )
          ) {
            sync_GUI_colors(caller);

            return;
          }

        //}}}
        // FREEZED {{{
        if( Settings.FREEZED ) {
if(M||D) MLog.log("..."+Settings.SYMBOL_freezed+" FREEZED: "+ caller);

            return;
        }
        //}}}
        // OFFLINE {{{
        if( Settings.OFFLINE ) {
if(M||D) MLog.log("..."+Settings.SYMBOL_offline+" OFFLINE: "+ caller);

            return;
        }
        //}}}

        // SIGNED IN ON SERVER {{{
        if( this_RTabsClient.check_SIGNIN_reply( caller ) )
        {
            if(M||D) MLog.log("SIGNED IN ON SERVER");
            this_RTabsClient.parse_SIGNIN();

            sync_np( caller );

            update_stage( caller );
        }
        // }}}

        // SOME DATA TO PARSE {{{
        if( have_data_to_process_after( caller ) )
        {
            if(M||D) MLog.log("SOME DATA TO PARSE");
            if( this_RTabsClient.is_signed_in() ) if(D) MLog.log_left("=== PROCESSING: "+ caller);
            data_looper_start(DATA_LOOPER_DELAY, "have_data_to_process_after("+ caller +")");
            return;
        }
        // }}}

        // PROFILE SYNC {{{
        if( this_RTabsClient.have_PROFILE_UPDATE( caller ) )
        {
            if(M||D) MLog.log("PROFILE SYNC");
            update_stage( caller );
            data_looper_start( caller );
            return;
        }
        // }}}

        // UNEXPECTED DATA RECEIVED .. DISCARD {{{
        if(D) MLog.log("UNPARSED DATA RECEIVED .. DISCARD");
        this_RTabsClient.discard_received_message( caller );

        sync_SUI_visibility( caller ); // sync_notify_from

        //}}}

        switch( current_stage )
        {
            case STAGE6_CONNECTING : if(M||D) MLog.log("TRY-ON CONNECTING"); update_stage     ( caller ); break;
            case STAGE5_TABS_LAYOUT: if(M||D) MLog.log(    "START POLLING"); poll_looper_start( caller ); break;
            case STAGE7_POLLING    : if(M||D) MLog.log(   "RESUME POLLING"); poll_looper_start( caller ); break;
        }
    }
    //}}}
    // have_data_to_process_after {{{
    private boolean have_data_to_process_after(String caller)
    {
        caller += "->have_data_to_process_after";

        boolean signin   = this_RTabsClient.check_SIGNIN_reply        ( caller );
        boolean palettes = this_RTabsClient.check_PALETTES_GET_reply  ( caller );
        boolean tabs     = this_RTabsClient.check_TABS_GET_reply      ( caller );
        boolean poll     = this_RTabsClient.check_POLL_message        ( caller );
        boolean profile  = this_RTabsClient.have_PROFILE_UPDATE       ( caller )
            &&             this_RTabsClient.check_PROFILE_UPDATE_reply( caller );

        boolean diag
            =  signin
            || palettes
            || tabs
            || poll
            || profile
            ;

        if(diag) {
            String data = "";

            if(signin  ) data += " SIGNIN"  ;
            if(palettes) data += " PALETTES";
            if(tabs    ) data += " TABS"    ;
            if(poll    ) data += " POLL"    ;
            if(profile ) data += " PROFILE" ;

            if(D) MLog.log_center("HAVE DATA=["+data+" ] TO PROCESS AFTER ["+caller+"]");
        }
        return diag;
    }
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** POLL */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ POLL @ {{{

    //* (on) */
    //_ poll_looper_start {{{
    private void poll_looper_start(String caller)
    {
        caller += "->poll_looper_start";
if(M||D) MLog.log_center(caller);

        handler.re_postDelayed( hr_poll_looper, POLL_CHECK_DELAY);
    }
    //}}}
    //_ poll_looper_stop {{{
    private void poll_looper_stop(String caller)
    {
        caller += "->poll_looper_stop";
if(M||D) MLog.log_center(caller);

        handler.removeCallbacks( hr_poll_looper );
    }
    //}}}
    //* (loop) */
    // hr_poll_looper {{{
    private final Runnable hr_poll_looper = new Runnable()
    {
        @Override public void run()
        {
            boolean connected = ((this_RTabsClient != null) && this_RTabsClient.isConnected());

//          if( !connected ) {
//              if(M||D) MLog.log_center("hr_poll_looper CANNOT PROCEED [NOT CONNECTED]");
//          }

            if( connected && (current_stage  < STAGE5_TABS_LAYOUT))
            {
                if(M||D) MLog.log_center("hr_poll_looper INTERRUPTED AT ["+ get_stage_name(current_stage) +"] (connected "+connected+")");
//*POLL*/Settings.MOC(TAG_POLL,          "hr_poll_looper INTERRUPTED AT ["+ get_stage_name(current_stage) +"] (connected "+connected+")");
            }
            else {

                // execute this cycle job
//System.err.println("hr_poll_looper: POLLING");
                this_RTabsClient.progress_POLL();

                // i.e. runnable re-scheduling itself
                handler.re_postDelayed(this, POLL_CHECK_DELAY);
            }

        }
    };
    //}}}
    //* (off) */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** LISTEN */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ LISTEN @ {{{
    // set_listeners {{{
    private static final int ORIENTATION_CHANGE_MIN = 5;
    private OrientationEventListener mOrientationListener;
    private void set_listeners()
    {
        // Right time to instanciate [mOrientationEventListener] {{{
        // NOTE: .. (HAS TO BE INSTANTIATED HERE TO AVOID)
        // . java.lang.RuntimeException:
        // . Unable to instantiate activity ComponentInfo{ivanwfr.rtabs/ivanwfr.rtabs.FullscreenActivity}:
        // . IllegalStateException: System services not available to Activities before onCreate()
        mOrientationListener = new OrientationEventListener(activity, SensorManager.SENSOR_DELAY_NORMAL) {
            private int prev_orientation;
            @Override
            public void onOrientationChanged(int orientation) {
                if(orientation > 180) orientation -= 360; // -180 <= orientation <= 180
                if(Math.abs(orientation - prev_orientation) >= ORIENTATION_CHANGE_MIN)
                {
//GUI//Settings.MOM(TAG_GUI, String.format("onOrientationChanged: from %3d to %3d", prev_orientation, orientation));                                                                   // TAG_GUI
                    prev_orientation = orientation;
                    check_orientation( FROM_SENSOR );
                }
            }
        };
        //}}}

        SystemUiHider systemUiHider
            = SystemUiHider.getInstance(activity, frameLayout, SystemUiHider.FLAG_HIDE_NAVIGATION);
        systemUiHider.setup();
        systemUiHider.setOnVisibilityChangeListener(new systemUi_OnVisibilityChangeListener());

        // TABS
    //  tabs_container      .setOnTouchListener(handle_hide_OnTouchListener);

        // HANDLE
        handles_container   .setOnTouchListener(handles_container_OnTouchListener);
        lft_handle          .setOnActivatedListener( handle_OnActivatedListener );
        dck_handle          .setOnActivatedListener( handle_OnActivatedListener );
        top_handle          .setOnActivatedListener( handle_OnActivatedListener );
        mid_handle          .setOnActivatedListener( handle_OnActivatedListener );
        mid_handle          .setOnDraggedListener  ( handle_OnDraggedListener   );
        bot_handle          .setOnActivatedListener( handle_OnActivatedListener );

        // CONTROLS
        controls_container  .setOnTouchListener(handle_hide_OnTouchListener);
        seekers_container   .setOnTouchListener(handle_hide_OnTouchListener);
        palettes_seekBar    .setOnSeekBarChangeListener(palettes_seekBar_OnSeekBarChangeListener);
        scale_seekBar       .setOnSeekBarChangeListener(   scale_seekBar_OnSeekBarChangeListener);
    //  cmd_text            .setOnKeyListener(cmd_text_OnKeyListener);

        // LOG
        log_container       .setOnScrollViewListener(log_container_OnScrollViewListener);

        log_text            .setOnClickListener(log_text_ClickListener);

        checkBox_freeze     .setOnCheckedChangeListener(checkBox_freeze_OnCheckedChangeListener);
        checkBox_log        .setOnCheckedChangeListener(checkBox_log_OnCheckedChangeListener);
        cmd_INVENTORY       .setOnClickListener(cmd_INVENTORY_OnClickListener);
        cmd_PROFILE         .setOnClickListener(cmd_PROFILE_OnClickListener);
        cmd_clear_log       .setOnClickListener(cmd_clear_log_OnClickListener);
        cmd_send            .setOnClickListener(cmd_send_OnClickListener);
        cmd_status          .setOnClickListener(cmd_status_OnClickListener);
    }
    //}}}
    //* HANDLE */
    // handles_container_OnTouchListener {{{
    private final View.OnTouchListener handles_container_OnTouchListener = new View.OnTouchListener()
    {
        @Override public boolean onTouch(View view, MotionEvent event)
        {
            String caller = "handles_container_OnTouchListener";

            Handle cur_handle  = Handle.Get_cur_handle();
            if(    cur_handle == null) return false;

            if( is_magnify_np_showing() ) magnify_np_hide(caller);

            return false; // always pass through!
        }

    };
    //}}}
    // handle_hide_OnTouchListener {{{
    private final View.OnTouchListener handle_hide_OnTouchListener = new View.OnTouchListener()
    {
        @Override public boolean onTouch(View view, MotionEvent event)
        {
            int action = event.getActionMasked();
            if(action == MotionEvent.ACTION_DOWN)
            {
                // HANDLE HIDE
                if(Handle.Get_cur_handle() != null)
                    handle_hide("handle_hide_OnTouchListener");

                // HANDLE DIMM
                //if( is_view_showing(cart_band) ) Handle.UnscheduleDimm();
                //else                             Handle.Re_post_dimm();
            }
            return false;
        }

    };

    //}}}
    // handle_OnActivatedListener {{{
    private final Handle.OnActivatedListener handle_OnActivatedListener = new Handle.OnActivatedListener()
    {
        @Override public void onActivated(View view, boolean expanded)
        {
            Handle handle = (Handle)view;
            String caller = "handle_OnActivatedListener("+get_handle_name(handle)+", expanded="+expanded+")";

            // 1/2 (GUI_TYPE_HANDLES) .. APPLY GUI STATE ACCORDING TO CURRENTLY EXPANDED HANDLE {{{
            if( Settings.is_GUI_TYPE_HANDLES() )
            {
                int    next_GUI_STATE =  GUI_STATE; // current
                if(handle != null) {
                    if     ((handle == bot_handle) &&  expanded) next_GUI_STATE = GUI_STATE_INFO;
                    else if((handle == dck_handle) &&  expanded) next_GUI_STATE = GUI_STATE_DOCK;
                    else if((handle == top_handle) &&  expanded) next_GUI_STATE = GUI_STATE_PROF;
                    else if((handle == mid_handle) &&  expanded) next_GUI_STATE = GUI_STATE_CTRL;
                //  else if((handle == lft_handle) && !expanded) next_GUI_STATE = GUI_STATE_TABS;
                }
//*HANDLE*/Settings.MON(TAG_HANDLE, caller, "...APPLY GUI_STATE: from ["+GUI_STATE+"] to ["+next_GUI_STATE+"]");

                if(next_GUI_STATE !=     GUI_STATE) {
                    apply_GUI_STATE(next_GUI_STATE, FROM_HANDLE, caller);
                }
                else {
//*HANDLE*/Settings.MON(TAG_HANDLE, caller, "...GUI_STATE UNCHANGED");
                }
                // PARK HANDLE {{{
                if((handle == lft_handle) && expanded) {
//*HANDLE*/Settings.MON(TAG_HANDLE, caller, "(GUI_TYPE_HANDLES) .. PARK HANDLE .. (handle == lft_handle)");
                    handler.re_postDelayed( hr_sync_handles, Handle.PARK_HANDLES_DELAY);
                }
                //}}}
            }
            //}}}
            // 2/2 (GUI_TYPE_DOCKING) .. PARK HANDLE {{{
            else if(expanded)
            {
                if(handle == gesture_down_SomeView_atXY)
                {
//*HANDLE*/Settings.MON(TAG_HANDLE, caller, "(GUI_TYPE_DOCKING) .. PARK HANDLE .. (handle == gesture_down_SomeView_atXY)");
                //  handler.re_postDelayed( hr_sync_handles, Handle.PARK_HANDLES_DELAY);
                //  handle_hide(caller); // not enough ... Handle onTouchEvent will re-open it
                    Handle.Collapse_all_instances();
                    top_handle.setVisibility( View.INVISIBLE );
                    mid_handle.setVisibility( View.INVISIBLE );
                    bot_handle.setVisibility( View.INVISIBLE );
                }
            }
            //}}}
        }
    };
    //}}}
    // handle_OnDraggedListener {{{
    private final Handle.OnDraggedListener handle_OnDraggedListener = new Handle.OnDraggedListener()
    {
        //private Button profiles_button = null;
        @Override public void onDragged(View view, float x, float y) {
            // SCALE or PALETTE .. mid_handle {{{
            String caller = "onDragged";
            //if(view == mid_handle) {
                // WHERE
/* // within seekbar screen rectangle {{{
                int[] xy  = new int[2]; scale_seekBar.getLocationOnScreen( xy );
                int left  = xy[0] + scale_seekBar.getPaddingLeft();
                int right = left  + scale_seekBar.getWidth() - scale_seekBar.getPaddingRight();
                if(x > right)   x = right;
                if(x < left )   x = left;
                float fraction = (float)(x-left) / (float)(right-left);
*/ // }}}
                // thumb distance from left {{{
            //  int[]  xy      = new int[2]; scale_seekBar.getLocationOnScreen( xy );
            //  int    min     = xy[0];
                int    min     = Settings.GUARD_LEFT;
                if(x < min) return;
                if(x < min) x  = min; // clamp to min

            //  int    max     = Settings.DISPLAY_W /  2;
            //  int    max     = min + (int)(5 * (Settings.TAB_GRID_S * Settings.DEV_SCALE_MAX));
            //  int    max     = min + scale_seekBar.getWidth();
                int    max     = Settings.TO_GUARD_RIGHT;
                if(x > max) return;
                if(x > max) x  = max; // clamp to max

                // }}}
                float fraction = (x-min) / (max-min);

                // WHAT
                // SCALE (top half) {{{
                if(y < view.getHeight() / 2)
                {
                    float scale_delta   = (Settings.DEV_SCALE_MAX - Settings.DEV_SCALE_MIN);
                    Settings.DEV_SCALE  =  Settings.DEV_SCALE_MIN + fraction * scale_delta;

/*
                    String s= String.format("%2.2f\n%2.2f .. %2.0f"
                            ,        Settings.DEV_SCALE
                            , (float)Settings.DEV_SCALE_MIN
                            , (float)Settings.DEV_SCALE_MAX
                            );
*/
                }
                // }}}
                // PALETTE (bottom half) {{{
                else {
                    max             = this_RTabsClient.get_PALETTE_COUNT();
                    int palette_num = (int)(fraction * (max+1));
                    if (palette_num < 1  ) palette_num =   1;
                    if (palette_num > max) palette_num = max;

                    this_RTabsClient.apply_PALETTE_NUM(RTabsClient.TABS_Map, palette_num, caller);
/*
                    String s = String.format("%s\n%2d .. %2d"
                            , Settings.PALETTE
                            , palette_num
                            , max
                            );
*/
                }
                // }}}

                seekers_container.setVisibility( View.VISIBLE ); // show while dragging

                invalidate(caller);
            //}
            // }}}
        }
    };
    // scale_seekBar_OnSeekBarChangeListener .. [NO EFFECT: see drag listener] {{{
    private final OnSeekBarChangeListener scale_seekBar_OnSeekBarChangeListener = new OnSeekBarChangeListener()
    {
        @Override public void onProgressChanged(SeekBar seekBar, int percent, boolean fromUser)
        {
//            //{{{
//            float scale = (float)(Settings.DEV_SCALE_MIN + percent/100F * (Settings.DEV_SCALE_MAX - Settings.DEV_SCALE_MIN));
//            String s
//                = String.format(  "%.2f", (float)Settings.DEV_SCALE_MIN)
//                + String.format("< %.2f < ", scale)
//                + String.format(  "%.2f", (float)Settings.DEV_SCALE_MAX)
//                ;
//
//            scale_textView.setText( s );
//            //}}}
            //if(fromUser) if(D) MLog.log("*** scale_seekBar.onProgressChanged(percent=["+ percent +"])");
        }
        @Override public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override public void onStopTrackingTouch (SeekBar seekBar) { }
    };
    // }}}
    // palettes_seekBar_OnSeekBarChangeListener .. [NO EFFECT: see drag listener] {{{
    private final OnSeekBarChangeListener palettes_seekBar_OnSeekBarChangeListener = new OnSeekBarChangeListener()
    {
        @Override public void onProgressChanged   (SeekBar seekBar, int num, boolean fromUser) { }
        @Override public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override public void onStopTrackingTouch (SeekBar seekBar) { }
    };
    // }}}
    //}}}
    //* LOG & SYSTEM */
    // log_text_ClickListener {{{
    private final View.OnClickListener log_text_ClickListener = new View.OnClickListener()
    {
        @Override public void onClick(View view) {
            cmd_container.setVisibility((cmd_container.getVisibility() != View.VISIBLE) ? View.VISIBLE : View.GONE);
        }
    };
    //}}}
    // systemUi_OnVisibilityChangeListener {{{
    private static final int SYSTEM_UI_COOLDOWN = 500; //ms
    private boolean last_SystemUiHider_vis;
    private    long last_SystemUiHider_vis_time = 0L;

    private class systemUi_OnVisibilityChangeListener implements SystemUiHider.OnVisibilityChangeListener
    {
        @Override
        public void onVisibilityChange(boolean vis)
        {
            String caller = "SystemUiHider: vis=["+ vis +"] .. COOLDOWN=["+SYSTEM_UI_COOLDOWN+"ms]";
//*GUI*/Settings.MOC(TAG_GUI, caller);

            // UNCHANGED .. (ignore)
            if(last_SystemUiHider_vis == vis) return;

            // RECORD [LAST SYSTEM_UI TRANSITION TIME]
            if(vis) last_SystemUiHider_vis_time = System.currentTimeMillis();
            if(vis) hideSandBox();

            // INHIBIT OR RESUME [EVENTS HANDLING]
//*GUI*/if(vis) Settings.MON(TAG_GUI, caller, "dispatchTouchEvent will * IGNORE EVENT CHAIN");
//*GUI*/else    Settings.MON(TAG_GUI, caller, "dispatchTouchEvent will - HANDLE EVENT CHAIN");

            // REFLECT CURRENT [EVENTS HANDLING] STATE (GUI)
            last_SystemUiHider_vis = vis;
            do_sync_SUI_visibility("SystemUiHider.onVisibilityChange");
        }
    }
    // }}}
    // sustaining_freezed_by_system_bars {{{
    private boolean sustaining_freezed_by_system_bars()
    {
        String caller = "sustaining_freezed_by_system_bars";

        boolean still_sustaining = false;

        if( is_sysbars_visible("caller") )
        {
            long        vis_duration = System.currentTimeMillis() - last_SystemUiHider_vis_time;

            still_sustaining = (vis_duration < SYSTEM_UI_COOLDOWN);

            // confirm (auto-maintain)
            if(still_sustaining)
            {
                last_SystemUiHider_vis_time = System.currentTimeMillis();

                // schedule a GUI event to show when sustain time has elapsed
                handler.re_postDelayed( hr_sync_SUI_visibility, SYSTEM_UI_COOLDOWN);
            }
        }

//GUI//Settings.MOM(TAG_GUI, caller+": vis_duration=["+vis_duration+"] ...return "+still_sustaining);
        return still_sustaining;
    }
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** PROCESS */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ PROCESSING @ {{{
    // {{{
    private static final int FROM_CONFIG   = 0;
    private static final int FROM_CREATE   = 1;
    private static final int FROM_GESTURE  = 2;
    private static final int FROM_HANDLE   = 3;
    private static final int FROM_KEYBOARD = 4;
    private static final int FROM_LOG      = 5;
    private static final int FROM_RESUME   = 6;
    private static final int FROM_SENSOR   = 7;
    private static final int FROM_TAB      = 8;
    private static final int FROM_TOUCH    = 9;

    private String get_fromSource(int fromSource)
    {
        switch(fromSource) {
            case FROM_CONFIG    : return "FROM_CONFIG"  ;
            case FROM_CREATE    : return "FROM_CREATE"  ;
            case FROM_GESTURE   : return "FROM_GESTURE" ;
            case FROM_HANDLE    : return "FROM_HANDLE"  ;
            case FROM_KEYBOARD  : return "FROM_KEYBOARD";
            case FROM_LOG       : return "FROM_LOG"     ;
            case FROM_RESUME    : return "FROM_RESUME"  ;
            case FROM_SENSOR    : return "FROM_SENSOR"  ;
            case FROM_TAB       : return "FROM_TAB"     ;
            case FROM_TOUCH     : return "FROM_TOUCH"   ;
            default             : throw new RuntimeException("get_fromSource: Wrong fromSource enum=["+fromSource+"].");
        }
    }

    private int get_fold_band_delay(int fromSource)
    {
        switch(fromSource) {
            case FROM_CONFIG    : return CYCLE_FOLD_BAND_DELAY;
            case FROM_CREATE    : return CYCLE_FOLD_BAND_DELAY;
            case FROM_RESUME    : return CYCLE_FOLD_BAND_DELAY;
            case FROM_SENSOR    : return CYCLE_FOLD_BAND_DELAY;

            case FROM_GESTURE   : return  USER_FOLD_BAND_DELAY;
            case FROM_HANDLE    : return  USER_FOLD_BAND_DELAY;
            case FROM_KEYBOARD  : return  USER_FOLD_BAND_DELAY;
            case FROM_LOG       : return  USER_FOLD_BAND_DELAY;
            case FROM_TAB       : return  USER_FOLD_BAND_DELAY;
            case FROM_TOUCH     : return  USER_FOLD_BAND_DELAY;
            default             : return  USER_FOLD_BAND_DELAY;
        }
    }
    // }}}
    // GUI {{{
    // toggle_GUI_STYLE {{{
    private void toggle_GUI_STYLE(int fromSource)
    {
        Settings.toggle_GUI_STYLE();

        String caller = "toggle_GUI_STYLE";
        invalidate_handles(fromSource, caller);
    //RTabsClient.TABS_Map_Has_Changed = true; // XXX
    //this_RTabsClient.apply_TABS_LAYOUT     (RTabsClient.TABS_Map, tabs_container, "toggle_GUI_STYLE");
        Settings.GUI_STYLE_HAS_CHANGED = true; // forces NpButton to call invalidateOutline
        this_RTabsClient.apply_SETTINGS_PALETTE(RTabsClient.TABS_Map, caller);
        Settings.GUI_STYLE_HAS_CHANGED = false;
        //tabs_container.invalidate();
//tabs_container.requestLayout();
    }
    //}}}
    // toggle_GUI_FONT {{{
    private void toggle_GUI_FONT(int fromSource)
    {
        String caller = "toggle_GUI_FONT";

        Settings.GUI_FONT
            = Settings.is_GUI_FONT_DEFAULT()
            ? Settings.   GUI_FONT_NOTO
            : Settings.   GUI_FONT_DEFAULT;

        Typeface typeface = Settings.getTypeface();

        if(bg_view      != null) bg_view     .setTypeface ( typeface );
        if(fs_button    != null) fs_button   .setTypeface ( typeface );
        if(note_tag     != null) note_tag    .setTypeface ( typeface );
        if(note_text    != null) note_text   .setTypeface ( typeface );

        if(fs_wtitle    != null) fs_wtitle   .setTypeface ( typeface );
        if(fs_wtitle2   != null) fs_wtitle2  .setTypeface ( typeface );
        if(fs_wtitle3   != null) fs_wtitle3  .setTypeface ( typeface );

        if(fs_browse    != null) fs_browse   .setTypeface ( typeface );
        if(fs_wswapL    != null) fs_wswapL   .setTypeface ( typeface );
        if(fs_wswapR    != null) fs_wswapR   .setTypeface ( typeface );
        if(fs_bookmark  != null) fs_bookmark .setTypeface ( typeface );
        if(fs_goBack    != null) fs_goBack   .setTypeface ( typeface );
        if(fs_goForward != null) fs_goForward.setTypeface ( typeface );

        /*.........................*/ wvTools.set_typeface( typeface );

        invalidate_handles(fromSource, caller);

        this_RTabsClient.apply_SETTINGS_PALETTE(RTabsClient.TABS_Map, caller);

        //tabs_container.invalidate();
//tabs_container.requestLayout();
        //frameLayout.invalidate();
    }
    //}}}
    // toggle_GUI_TYPE {{{
    private void toggle_GUI_TYPE(int fromSource)
    {
        String caller = "toggle_GUI_TYPE("+ get_fromSource(fromSource)+")";
//*GUI*/Settings.MOC(TAG_GUI, caller);
//*GUI*/Settings.MOM(TAG_GUI, "CURRENT GUI_TYPE="+Settings.GUI_TYPE);

        Settings.GUI_TYPE
            = Settings.is_GUI_TYPE_HANDLES()
            ? Settings.   GUI_TYPE_DOCKING
            : Settings.   GUI_TYPE_HANDLES;

        this_RTabsClient.clear_DOCK_Map(caller);   // @see Profile.build_PROFILES_TABLE

        check_orientation( fromSource );
        sync_GUI(caller);
    }
    //}}}
    // invalidate {{{
    private void invalidate(String caller)
    {
        caller += "->invalidate";
//*GUI*/Settings.MOC(TAG_GUI, caller);

        // TODO make sure there is no better way to refresh the screen
        if(this_RTabsClient != null)
            this_RTabsClient.apply_TABS_LAYOUT(RTabsClient.TABS_Map, this_RTabsClient.tabs_container, caller);

        shedule_update_WEBVIEW_TITLES_AND_COLORS(caller);
    }
/*
    // post_invalidate {{{
    private String post_invalidate_caller;

    public void post_invalidate(String caller)
    {
        caller += "->post_invalidate";

        post_invalidate_caller = caller;
        handler.re_postDelayed( hr_invalidate, 250);
    }

    private Runnable hr_invalidate = new Runnable() {
        @Override public void run() { invalidate( post_invalidate_caller ); }
    };

    //}}}
*/
    //}}}

    // sync_GUI {{{
    // schedule_sync_GUI {{{
    // ...used by MotionListener.onTouchEvent.ACTION_UP .. (that happens before MGestureListener.onFling)
    private void schedule_sync_GUI(String caller)
    {
        caller += "->schedule_sync_GUI";
//*GUI*/Settings.MOC(TAG_GUI, caller);

        handler.re_postDelayed(hr_sync_GUI, 500);
    }

    private final Runnable hr_sync_GUI = new Runnable() {
        @Override public void run() { sync_GUI("hr_sync_GUI"); }
    };
    //}}}
    private void sync_GUI(String caller)
    {
        caller += "->sync_GUI";
//*GUI*/Settings.MOC(TAG_GUI, caller);
//*GUI*/ trace_views(TAG_GUI, caller);

        // CLEAR DOCK STANDBY MODE (i.e. TAG=HANDLE_HIDDEN_IN_STANDBY)
        dck_handle.setVisibility(View.VISIBLE);

        // ALL HANDLES REMAIN VISIBLE IN THE MODE
        if( Settings.is_GUI_TYPE_HANDLES() ) return;

        // SOME FULL SCREEN CONTROL VIEW ACTIVATED
        if( is_handle_showing( top_handle )) return;
        if( is_handle_showing( mid_handle )) return;
        if( is_handle_showing( bot_handle )) return;
        if( is_view_showing  ( cart_band  )) return;

        // DEFAULT TO DOCK HANDLE
        if(!is_view_showing(dck_handle) )
            handle_set(dck_handle, caller);

        // DEFAULT TO DOCK HIST
        if(        !is_view_showing(show_band)
                && !is_view_showing(hist_band)
                && !is_view_showing(dock_band)
          ) {
            show_histBand(FROM_GESTURE, caller);

            // DEFAULT TO DISPLAY SHOW-HIDE-COLORED-STRIPS
            if(!is_view_showing(show_band) && !show_band_hidden)
                show_band_show(caller);
          }
    }
    // }}}

    //}}}
    // TODO: IDENTIFY USER INTERACTIONS THAT SHOULD CALL reset_GUI_idle_timer
    // GUI IDLE TIME {{{
    // {{{
    private static final int GUI_IDLE_NOTIFICATION_DELAY = 3000;
    private             long gui_idle_timer_start_time;
    private          boolean gui_idle_timer_notification_pending;

    // }}}
    // get_GUI_idle_time {{{
    private String get_GUI_idle_time()
    {
        return Settings.Get_time_elapsed( gui_idle_timer_start_time );
    }
    //}}}
    // reset_GUI_idle_timer {{{
    private void reset_GUI_idle_timer(String caller)
    {
        caller += "->reset_GUI_idle_timer";
//*GUI*/Settings.MOC(TAG_GUI, caller);
        gui_idle_timer_start_time = System.currentTimeMillis();

        // restart a new GUI idle time notification
        _schedule_GUI_idle_notification( GUI_IDLE_NOTIFICATION_DELAY );

    }
    //}}}
    // _schedule_GUI_idle_notification {{{
    private void _schedule_GUI_idle_notification(int delay)
    {
        gui_idle_timer_notification_pending = true;
        handler.re_postDelayed( hr_GUI_idle_notification, delay);
    }
    //}}}
    // cancel_GUI_idle_notification {{{
    private void cancel_GUI_idle_notification(String caller)
    {
        if( !gui_idle_timer_notification_pending ) return;

        gui_idle_timer_notification_pending = false;

        caller += "->cancel_GUI_idle_notification";
//*GUI*/Settings.MOC(TAG_GUI, caller);

        handler.removeCallbacks( hr_GUI_idle_notification );
    }
    //}}}
    // hr_GUI_idle_notification {{{
    private final Runnable hr_GUI_idle_notification = new Runnable()
    {
        @Override
        public void run()
        {
            // CONSUME PENDING NOTIFICATION
            gui_idle_timer_notification_pending = false;

            // CHECK FOR SOMETHING TO DO
        //  boolean sync_GUI_colors_has_been_called = sync_GUI_colors_has_been_called();
            if(         time_consuming_initialisation_done
                    // !sync_GUI_colors_has_been_called()
              )
                return;

            // LOG
            String caller = "hr_GUI_idle_notification";
//*GUI*/Settings.MOM(TAG_GUI,caller+": GUI IDLE TIME ["+get_GUI_idle_time()+"]");
//GUI//Settings.MOM(TAG_GUI,"......sync_GUI_colors_has_been_called=["+ sync_GUI_colors_has_been_called()  +"]");
//*GUI*/Settings.MOM(TAG_GUI,"...time_consuming_initialisation_done=["+ time_consuming_initialisation_done +"]");

            // THEN DO IT
        //  if( sync_GUI_colors_has_been_called()  ) do_sync_GUI_colors(caller);
            if(!time_consuming_initialisation_done ) do_time_consuming_initialisation();

        }
    };
    // }}}
    // do_time_consuming_initialisation {{{
    boolean time_consuming_initialisation_done = false;
    private void do_time_consuming_initialisation()
    {
//*GUI*/Settings.MOC(TAG_GUI,"do_time_consuming_initialisation");
        time_consuming_initialisation_done = true;
        SoundPoolManager.initialize();

        // cycle dock_band open-fold
        fold_band(hist_band, "initialize");
    }
    //}}}
    // }}}
    // CONTROLS {{{
    // PROFILE {{{
    // profile_del {{{
    private void profile_del(String cmdLine, String caller)
    {
        caller += "->profile_del("+cmdLine+")";
//*DATA*/Settings.MOC(TAG_DATA, caller);

        CmdParser.parse( cmdLine );
        String profile_name = CmdParser.arg1;
//*DATA*/Settings.MOM(TAG_DATA, "profile_name=["+profile_name+"]");

        if( profile_name.equals("") )       Settings.Delete_WORKING_PROFILE();
        else                                Settings.Delete_PROFILE( profile_name );

        // INVALIDATE DEPENDENCIES
        profile_sync_storage_GUI();

        if( Settings.is_GUI_TYPE_HANDLES()) show_PROFILES_TABLE(caller);
        else                                show_DOCKINGS_TABLE(caller);
    }
    //}}}
    // profile_zip {{{
    private void profile_zip()
    {
        Profile.zip();
        profile_sync_storage_GUI();
    }
    //}}}
    // profile_unzip_factory {{{
    private void profile_unzip_factory()
    {
        Profile.unzip_factory();
        profile_sync_storage_GUI();
        this_RTabsClient.reload_Working_profile();
    }
    //}}}
    // profile_unzip {{{
    private void profile_unzip()
    {
        Profile.unzip();
        profile_sync_storage_GUI();
        this_RTabsClient.reload_Working_profile();
    }
    //}}}
    // profile_zip_del {{{
    private void profile_zip_del()
    {
        Profile.zip_del();
        profile_sync_storage_GUI();
    }
    //}}}
    // profile_sync_storage_GUI {{{
    private void profile_sync_storage_GUI()
    {
        String caller = "profile_sync_storage_GUI";
        Profile         .Clear_Profiles_Dict(caller);
        this_RTabsClient.clear_DOCK_Map     (caller);
        this_RTabsClient.clear_PROF_Map     (caller);
        dock_scroll_reset();
    }
    //}}}
    // }}}
    // PALETTES {{{
    private void request_PALETTES_GET(String caller) //{{{
    {
        caller += "->request_PALETTES_GET";
//*DATA*/Settings.MOC(TAG_DATA, caller);

        // PALETTES REQUEST
        this_RTabsClient.send(RTabsClient.CMD_PALETTES_GET, caller);
    }
    //}}}
    private void request_PALETTES_PARSE() // {{{
    {
        // DATA PARSING
        this_RTabsClient.parse_PALETTES();
    }
    //}}}
    // }}}
    // TABS {{{
    // scale_seekBar {{{
    // set_layout_dev_scale {{{
    public void set_layout_dev_scale(float scale)
    {
        int percent = (int)(100 * (scale - Settings.DEV_SCALE_MIN) / (Settings.DEV_SCALE_MAX - Settings.DEV_SCALE_MIN));
/*
        Settings.DEV_SCALE
            =  Settings.DEV_SCALE_MIN
            + (Settings.DEV_SCALE_MAX - Settings.DEV_SCALE_MIN) * percent / 100;
*/
        scale_seekBar.setProgress( percent );
        show_np_scale();
    }
    //}}}
    //}}}
    // palettes_seekBar {{{
    // set_palette_max {{{
    public void set_palette_max(int max) {
        palettes_seekBar.setMax(max-1); // !!! sets mKeyProgressIncrement to 20
        palettes_seekBar.setKeyProgressIncrement(1);
    }
    //}}}
    // set_palette_num_name {{{
    public void set_palette_num_name(int num, String name)
    {
        show_np_palette();

        //palettes_textView.setText(name +"\n"+ (num) +"/"+ palettes_seekBar.getMax());
        palettes_seekBar.setProgress(num-1); // zero-based
    }
    //}}}
    //}}}
    //}}}
    //}}}
    /* EXEC */
    // handle_COMMAND {{{
/*
:!start explorer "https://github.com/termux/termux-app"

:new C:/LOCAL/DATA/ANDROID/SAMPLES/termux-app-master/app/src/main/java/com/termux/app/BackgroundJob.java
*/
    private void handle_COMMAND(String command)
    {
        String caller = "handle_COMMAND("+command+")";
//*COMM*/Settings.MOC(TAG_COMM, caller);

        if(command.trim() == "") command = "netstat -lte";

        String result = "";

        try {
            Process process       = Runtime.getRuntime().exec( command+"\n" );
            InputStream    stdout = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }
        }
        catch (Exception ex) {
            result = command +"\n"+ ex.getMessage();
        }
//*COMM*/Settings.MOM(TAG_COMM, result);

        if( this_RTabsClient.has_DashNotePane() )
        {
/*{{{
            poll_looper_stop(caller);
            this_RTabsClient.stop_POLL(caller);
}}}*/
            set_APP_freezed_state(true ,caller);
            this_RTabsClient.replace_dash("COMMAND: "+command+"\n"+result );
        }
        else {
            set_np_label("COMMAND"       , result );
        }
    };
    //}}}
    // SYSTEM-UI {{{
    //{{{
    // SHOULD BE CALLED BY ALL SUI-RELATED-PROCESS
    // .. (i.e. all if's resulting in a required display update related to):
    // ...like [onResume]
    // ...like [GUI_STATE==GUI_STATE_INFO]
    // ...like [mid_handle hide, show]
    // ...like [bot_handle hide, show]
    // ...like [SystemUiHider.OnVisibilityChangeListener]

    private String sync_SUI_visibility_callers = "";

    //}}}
    // sync_SUI_visibility {{{
    private   void sync_SUI_visibility(String caller)
    {
        sync_SUI_visibility_callers += " "+caller;

        caller += "->sync_SUI_visibility";
//*GUI*/Settings.MOC(TAG_GUI, caller);

        handler.re_postDelayed( hr_sync_SUI_visibility, SYNCUI_VISIBILITY_DELAY);
    }
    //}}}
    // hr_sync_SUI_visibility {{{
    private final Runnable hr_sync_SUI_visibility = new Runnable() {
        @Override public void run() { do_sync_SUI_visibility( sync_SUI_visibility_callers ); }
    };
    // }}}
    // do_sync_SUI_visibility {{{
    @SuppressWarnings("StringEquality")
    private void do_sync_SUI_visibility(String caller)
    {
        // callers {{{
        if(caller != sync_SUI_visibility_callers) sync_SUI_visibility_callers += " "+caller;
//*GUI*/Settings.MOC(TAG_GUI, "do_sync_SUI_visibility");
//*GUI*/Settings.MOM(TAG_GUI, "..........................GUI_STATE=["+ get_GUI_STATE(GUI_STATE)    +"]");
//*GUI*/Settings.MOM(TAG_GUI, ".............last_SystemUiHider_vis=["+ last_SystemUiHider_vis      +"]");
//*GUI*/Settings.MOM(TAG_GUI, "........sync_SUI_visibility_callers:"+ sync_SUI_visibility_callers      );

        sync_SUI_visibility_callers = "";
        //}}}
        // [band_to_animate] {{{
        if( !slide_band_anim_queue_is_empty() )
        {
//*GUI*/Settings.MOM(TAG_GUI, "...SLIDE BAND ANIMANIMATION IN PROGRESS: calling hide_system_bars .. re-posting hr_sync_SUI_visibility");
            hide_system_bars(caller);
            handler.re_postDelayed( hr_sync_SUI_visibility, SYNCUI_VISIBILITY_DELAY);
            return;
        }
        //}}}
    //  sync_GUI(caller);
        // [seekers] [controls] [log] {{{
        boolean controls_visible = (              GUI_STATE == GUI_STATE_INFO  );
        boolean      cmd_visible = (              GUI_STATE == GUI_STATE_INFO  );
        boolean  seekers_visible = (Handle.Get_cur_handle() == mid_handle      ) && (mid_handle.getVisibility() != View.VISIBLE);
        boolean      log_visible = (Settings.LOGGING);

        seekers_container   .setVisibility (seekers_visible  ? View.VISIBLE : View.GONE);

        controls_container  .setVisibility (controls_visible ? View.VISIBLE : View.GONE);

        cmd_container       .setVisibility (cmd_visible      ? View.VISIBLE : View.GONE);
        cmd_clear_log        .setVisibility(log_visible      ? View.VISIBLE : View.GONE);
        log_container        .setVisibility(log_visible      ? View.VISIBLE : View.GONE);

        // SYSTEM_UI (HIDING) {{{
        if((GUI_STATE != GUI_STATE_INFO) && !last_SystemUiHider_vis)
        {
            hide_system_bars(caller);
            frameLayout_wake_up(caller);
        }
        //}}}
        // SYSTEM_UI (SHOWING) {{{
        else {
            long        vis_duration = System.currentTimeMillis() - last_SystemUiHider_vis_time;
            boolean still_sustaining = (vis_duration < SYSTEM_UI_COOLDOWN);
            if(still_sustaining) frameLayout_freeze (caller);
            else                 frameLayout_at_rest(caller);
        }
        //}}}

        // ALWAYS HIDE SANDBOX {{{
        //if( is_sandBox_showing ) {
            //if( is_sysbars_visible() ) hideSandBox();
        //}
        //}}}
        //}}}
        // LOG {{{
//*GUI*/Settings.MOM(TAG_GUI, "......seekers["+ seekers_visible  +"]");
//*GUI*/Settings.MOM(TAG_GUI, ".....controls["+ controls_visible +"]");
//*GUI*/Settings.MOM(TAG_GUI, "..........cmd["+  cmd_visible     +"]");
//*GUI*/Settings.MOM(TAG_GUI, "..........log["+  log_visible     +"]");
        //}}}
        sync_tabs_scrolling(caller); // (200831) CYCLE FREEZE STATE
    }
    //}}}
    // is_sysbars_visible {{{
    private boolean is_sysbars_visible(String caller)
    {
        caller += "->is_sysbars_visible";

        int                 dsv = activity.getWindow().getDecorView().getSystemUiVisibility();
//System.err.println( String.format("...dsv=%x", dsv) );

        boolean sysbars_visible = ((dsv & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0);

//*GUI*/if(sysbars_visible)Settings.MOM(TAG_GUI, caller+": ...return ["+sysbars_visible+"]");
        return sysbars_visible;
    }
            //}}}
    // hide_system_bars {{{
/* //{{{
:start explorer "https://developer.android.com/training/system-ui/immersive.html"
:start explorer "http://developer.android.com/training/system-ui/status.html"
:new /LOCAL/DATA/ANDROID/sdk/samples/android-23/ui/AdvancedImmersiveMode/Application/src/main/java/com/example/android/advancedimmersivemode/AdvancedImmersiveModeFragment.java
*/ //}}}

    private void hide_system_bars(String caller)
    {
        caller += "->hide_system_bars";
//*GUI*/Settings.MOC(TAG_GUI, caller);

        // hides the system bars
        // Set the IMMERSIVE flag
        // Set the content to appear under the system bars
        // so that it doesn't resize when the system bars hide and show

        View decorView = activity.getWindow().getDecorView();
        int        dsv = decorView.getSystemUiVisibility();
        int        msv
            = View.SYSTEM_UI_FLAG_FULLSCREEN            // hide (same visual as LayoutParams.FLAG_FULLSCREEN but system may override)
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION       // hide nav bar
            | View.SYSTEM_UI_FLAG_IMMERSIVE             // view remains interactive when hiding the navigation bar
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN     // view layout ignores system bars (i.e. using display RealSize)
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            ;
        if(Settings.OPACITY > 95) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                msv |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }

        if(dsv != msv)
        {
            if(D) {
/* SYSTEM_UI_FLAG                */                                                                                                              MLog.log("[HIDE] SYSTEM_UI_FLAG vvvvvvv (hide_system_bars)");
/* LOW_PROFILE            =    1 */ if((msv & View.SYSTEM_UI_FLAG_LOW_PROFILE           ) != (dsv & View.SYSTEM_UI_FLAG_LOW_PROFILE           )) MLog.log("++ LOW_PROFILE");
/* LOW_PROFILE            =    1 */ if((msv & View.SYSTEM_UI_FLAG_LOW_PROFILE           ) != (dsv & View.SYSTEM_UI_FLAG_LOW_PROFILE           )) MLog.log("++ LOW_PROFILE");
/* HIDE_NAVIGATION        =    2 */ if((msv & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION       ) != (dsv & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION       )) MLog.log("++ HIDE_NAVIGATION");
/* FULLSCREEN             =    4 */ if((msv & View.SYSTEM_UI_FLAG_FULLSCREEN            ) != (dsv & View.SYSTEM_UI_FLAG_FULLSCREEN            )) MLog.log("++ FULLSCREEN");
/* LAYOUT_STABLE          =  1   */ if((msv & View.SYSTEM_UI_FLAG_LAYOUT_STABLE         ) != (dsv & View.SYSTEM_UI_FLAG_LAYOUT_STABLE         )) MLog.log("++ LAYOUT_STABLE");
/* LAYOUT_HIDE_NAVIGATION =  2   */ if((msv & View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION) != (dsv & View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)) MLog.log("++ LAYOUT_HIDE_NAVIGATION");
/* LAYOUT_FULLSCREEN      =  4   */ if((msv & View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN     ) != (dsv & View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN     )) MLog.log("++ LAYOUT_FULLSCREEN");
/* IMMERSIVE              =  8   */ if((msv & View.SYSTEM_UI_FLAG_IMMERSIVE             ) != (dsv & View.SYSTEM_UI_FLAG_IMMERSIVE             )) MLog.log("++ IMMERSIVE");
/* IMMERSIVE_STICKY       = 1    */ if((msv & View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY      ) != (dsv & View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY      )) MLog.log("++ IMMERSIVE_STICKY");
/* LIGHT_STATUS_BAR       = 2    */ if((msv & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR      ) != (dsv & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR      )) MLog.log("++ LIGHT_STATUS_BAR");
/* SYSTEM_UI_FLAG                */                                                                                                              MLog.log("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            }
            decorView.setSystemUiVisibility( msv );
        }
    }

    //}}}
    // show_system_bars {{{
    private void show_system_bars(String caller)
    {
        caller += "->show_system_bars";
//*GUI*/Settings.MOC(TAG_GUI, caller);

        // shows the system bars
        // by removing all the flags
        // except for the ones that make the content appear under the system bars

/*
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(0
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    );
*/


        View decorView = activity.getWindow().getDecorView();
        int        dsv = decorView.getSystemUiVisibility();
        int        msv = 0;
        if(Settings.OPACITY > 95) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                msv |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }

        if(dsv != msv)
        {
            if(D) {
/* SYSTEM_UI_FLAG                */                                                                                                              MLog.log("[SHOW] SYSTEM_UI_FLAG vvvvvvv (show_system_bars)");
/* LOW_PROFILE            =    1 */ if((msv & View.SYSTEM_UI_FLAG_LOW_PROFILE           ) != (dsv & View.SYSTEM_UI_FLAG_LOW_PROFILE           )) MLog.log("-- LOW_PROFILE");
/* HIDE_NAVIGATION        =    2 */ if((msv & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION       ) != (dsv & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION       )) MLog.log("-- HIDE_NAVIGATION");
/* FULLSCREEN             =    4 */ if((msv & View.SYSTEM_UI_FLAG_FULLSCREEN            ) != (dsv & View.SYSTEM_UI_FLAG_FULLSCREEN            )) MLog.log("-- FULLSCREEN");
/* LAYOUT_STABLE          =  1   */ if((msv & View.SYSTEM_UI_FLAG_LAYOUT_STABLE         ) != (dsv & View.SYSTEM_UI_FLAG_LAYOUT_STABLE         )) MLog.log("-- LAYOUT_STABLE");
/* LAYOUT_HIDE_NAVIGATION =  2   */ if((msv & View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION) != (dsv & View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)) MLog.log("-- LAYOUT_HIDE_NAVIGATION");
/* LAYOUT_FULLSCREEN      =  4   */ if((msv & View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN     ) != (dsv & View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN     )) MLog.log("-- LAYOUT_FULLSCREEN");
/* IMMERSIVE              =  8   */ if((msv & View.SYSTEM_UI_FLAG_IMMERSIVE             ) != (dsv & View.SYSTEM_UI_FLAG_IMMERSIVE             )) MLog.log("-- IMMERSIVE");
/* IMMERSIVE_STICKY       = 1    */ if((msv & View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY      ) != (dsv & View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY      )) MLog.log("-- IMMERSIVE_STICKY");
/* LIGHT_STATUS_BAR       = 2    */ if((msv & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR      ) != (dsv & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR      )) MLog.log("++ LIGHT_STATUS_BAR");
/* SYSTEM_UI_FLAG                */                                                                                                              MLog.log("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            }
            decorView.setSystemUiVisibility( msv );
        }

        decorView.setSystemUiVisibility(0);
    }
    //}}}
    // frameLayout_freeze {{{
    private void frameLayout_freeze(String caller)
    {
        caller += "->frameLayout_freeze";

        boolean imm_isAcceptingText = imm_isAcceptingText();
//*GUI*/Settings.MOM(TAG_GUI, caller+": imm_isAcceptingText=["+ imm_isAcceptingText +"]");

        if( !imm_isAcceptingText() )
            frameLayout.setAlpha( 0.2f );
    }
    //}}}
    // frameLayout_at_rest {{{
    private void frameLayout_at_rest(String caller)
    {
        caller += "->frameLayout_at_rest";
//*GUI*/Settings.MOC(TAG_GUI, caller);

        frameLayout.setAlpha( 0.5f );
    }
    //}}}
    // frameLayout_wake_up {{{
    private void frameLayout_wake_up(String caller)
    {
        caller += "->frameLayout_wake_up";
//*GUI*/Settings.MOC(TAG_GUI, caller);

        frameLayout.setAlpha( 1.0f );
    }
    //}}}
    //}}}
    // InputMethodManager Configuration check_orientation {{{
    // imm_isAcceptingText {{{
    private InputMethodManager imm = null;

    private boolean imm_isAcceptingText()
    {
        String caller = "imm_isAcceptingText";

        if(imm == null)  imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean result = imm.isAcceptingText();

//*GUI*/Settings.MOM(TAG_GUI, caller+": imm_isAcceptingText=["+result+"]");
        return result;
    }
    //}}}
    // GUI_toast {{{
    private String      GUI_toast_msg = "";

    private Runnable hr_GUI_toast     = new Runnable() { @Override public void run() { toast_short( GUI_toast_msg ); } };
    //}}}
    // onConfigurationChanged .. check_orientation {{{
    //@Override
    public void onConfigurationChanged(Configuration newConfig)
    {
//*GUI*/Settings.MOC(TAG_GUI, "onConfigurationChanged");
        GUI_toast_msg = "Configuration Changed:\n"+ newConfig.toString().replace(" ","\nGUI: ");
//*GUI*/Settings.MON(TAG_GUI, "onConfigurationChanged", "GUI_toast_msg:\n"+GUI_toast_msg);
//*GUI*/handler.re_postDelayed(hr_GUI_toast, 500);//TAG_GUI

        check_orientation( FROM_CONFIG );
    }
    //}}}
    // check_orientation {{{
    private Display      display = null;
    private Configuration config = null;

    private void check_orientation(int fromSource)
    {
        // CHECK DISPLAY PARAMETERS {{{
        if(display == null) {
            display = activity.getWindowManager().getDefaultDisplay();
            config  = activity.getResources    ().getConfiguration();
        }
        boolean    orientation_landscape = (config.orientation == Configuration.ORIENTATION_LANDSCAPE); // ASPECT RATION
        int         rotation = display.getRotation();                                       // ANGLE
        Point              p = new Point(); display.getRealSize(p);                         // WIDTH x HEIGHT
        int display_W        = p.x;
        int display_H        = p.y;

        //}}}
        // DETECT CHANGE FROM-TO LANDSCAPE-PORTRAIT {{{
        boolean aspect_has_changed = false;
/*
        if((fromSource == FROM_CONFIG) || (fromSource == FROM_SENSOR)) {
            aspect_has_changed =
              (        ((Settings.DISPLAY_W > Settings.DISPLAY_H) && (display_W < display_H))
                    || ((Settings.DISPLAY_W < Settings.DISPLAY_H) && (display_W > display_H))
              );
        }
*/
        // CHECK AGAINST ASSUMED ORIENTATION
        aspect_has_changed = (config.orientation != Settings.DISPLAY_ASPECT);
        if( !aspect_has_changed ) return;

        //}}}
        // ADJUST DISPLAY GEOMETRY AND ORIENTATION DEPENDENT VIEWS {{{
        String caller = "check_orientation("+ get_fromSource(fromSource) +")";
//*GUI*/Settings.MON(TAG_GUI, caller,                                                         //TAG_GUI
//*GUI*/ String.format("%7s %13s %9s [%4d x %4d] ------> [%4d x %4d]"                         //TAG_GUI
//*GUI*/    ,           (aspect_has_changed ? "CHANGED" : "")                                 //TAG_GUI
//*GUI*/    ,               Settings.rotationToString(rotation)                               //TAG_GUI
//*GUI*/    ,                              (orientation_landscape ? "LANDSCAPE" : "PORTRAIT") //TAG_GUI
//*GUI*/    ,                                  Settings.DISPLAY_W,Settings.DISPLAY_H          //TAG_GUI
//*GUI*/    ,                                                      display_W,display_H        //TAG_GUI
//*GUI*/    )                                                                                 //TAG_GUI
//*GUI*/);                                                                                    //TAG_GUI
        reset_GUI_idle_timer("check_orientation.FROM_SENSOR");

        Settings.DISPLAY_ASPECT = config.orientation;
//*GUI*/Settings.MOC(TAG_GUI, caller);
//*GUI*/Settings.MOM(TAG_GUI, String.format("...rotation=[%3d]"      , rotation));
//*GUI*/Settings.MOM(TAG_GUI, String.format("...Settings=[%4d x %4d]", Settings.DISPLAY_W, Settings.DISPLAY_H));
//*GUI*/Settings.MOM(TAG_GUI, String.format("....display=[%4d x %4d]",          display_W,          display_H));
        Settings.set_DISPLAY_WH(display_W, display_H);

        Settings.TO_GUARD_RIGHT = Settings.DISPLAY_W - Settings.GUARD_LEFT;

        //XXX vsv.setPadding(Settings.get_DOCK_WIDTH(),0,0,0);

        if(this_RTabsClient == null)
        {
//*GUI*/Settings.MOM(TAG_GUI, "...(this_RTabsClient == null)");
            return;
        }
        //}}}
        // UPDATE DYNAMIC TAB {{{
        sync_np(caller);

        // }}}
        // LAYOUT HANDLE {{{
//*GUI*/Settings.MOM(TAG_GUI                                                   //TAG_GUI
//*GUI*/            , String.format("--- %20s [%4d x %-4d] rotation=[%3d]"     //TAG_GUI
//*GUI*/                , ((display_W > display_H) ? "landscape" : "portrait") //TAG_GUI
//*GUI*/                , Settings.DISPLAY_W                                   //TAG_GUI
//*GUI*/                , Settings.DISPLAY_H                                   //TAG_GUI
//*GUI*/                , rotation)                                            //TAG_GUI
//*GUI*/            );                                                         //TAG_GUI

        adjust_handles(caller, fromSource);

        invalidate_handles(fromSource, caller);

        // }}}
        // WVTOOLS {{{
        wvTools.fs_search_park(caller);

        wvTools.marker_wv_sync(caller);

        // }}}
        // LAYOUT TABS {{{
        fit_scale_to_window( (Settings.TABS_RATIO >= Settings.DEV_RATIO) ? FIT_W : FIT_H, caller);
        invalidate(caller);

        // }}}
        // LAYOUT TRANSCIENT VIEWS {{{
        magnify_np_layout      (caller);
        ip_dialog_layout       (caller);
        note_dialog_layout     (caller);
        update_histBand        (caller);
        adjust_WEBVIEW_LAYOUT  (Settings.SCREEN_SPLIT, caller);
        if(fs_webview_session_running) {
            if(fs_webView_isGrabbed) re_anim_grab_fs_webView(caller);
            else                     re_anim_free_fs_webView(caller);
        }

        // }}}
        // REFLOW TRANSCIENT CONTAINER {{{
        if( is_fg_view_showing() ) {
            if     ( Settings.PROFILE.equals( Settings.PROFHIST_TABLE) ) show_PROFHIST_TABLE(caller);
            else if( Settings.PROFILE.equals( Settings.SOUNDS_TABLE  ) ) show_SOUNDS_TABLE  (caller);
        }
        //}}}
    }
    //}}}
    //}}}
    // TAB SYNC {{{
    // sync_np {{{
    // {{{
    private static int  SELECTED_SHADOW_RADIUS     =  4;
    private static int  UNSELECTED_SHADOW_RADIUS   =  1;

    private static final String[] SYNCED_TAGS
    = {   "BOOKMARK"
        , "FREEZED"
        , "GUI_FONT"
        , "GUI_STYLE"
        , "GUI_TYPE"
        , "LANDSCAPE"
        , "LOG"
        , "LOG_ACTIVITY"
        , "LOG_CAT"
        , "LOG_CLIENT"
        , "LOG_PARSER"
        , "LOG_PROFILE"
        , "LOG_SETTINGS"
        , "MARK_SCALE_GROW"
        , "MEMORY"
        , "MONITOR"
        , "OFFLINE"
        , "PALETTE"
        , "PORTRAIT"
        , "PROFILETAGS"
        , "PROFILE_DELETE"
        , "PROFILE_UNZIP"
        , "PROFILE_UNZIP_DEFAULTS"
        , "PROFILE_ZIP"
        , "PROFILE_ZIP_DELETE"
        , "SANDBOX"
        , "SCALE"
        , "SCREEN_ROTATION"
        , "SERVER"
        , "STATUS"
        , "TOOL_URL"
        , "WEBVIEW"
        , "WOL"
    };
    // }}}
    /* is_a_SYNCED_TAG {{{*/
    public static boolean is_a_SYNCED_TAG(String tag)
    {
        return Arrays.asList( SYNCED_TAGS ).contains( tag );
    }
    //}}}
    public void sync_np(String caller) // {{{
    {
        caller += "->sync_np";
//*GUI*/Settings.MOC(TAG_GUI, caller);

        handler.re_postDelayed( hr_sync_np, SYNC_DYNAMIC_TABS_DELAY);
    }
    private final Runnable hr_sync_np = new Runnable() { @Override public void run() { do_sync_np(); } };

    // }}}
    // do_sync_np {{{
    private void do_sync_np()
    {
        String caller = "do_sync_np";
//*GUI*/Settings.MOC(TAG_GUI, caller);
        // PROFILES {{{
        String s;
        // ...delete {{{
        boolean profile_selected
            = (        !TextUtils.isEmpty                   ( Settings.Working_profile )
                    && !Profile.is_a_WORKBENCH_TEMPLATE_NAME( Settings.Working_profile ));

        set_np_label_fg("PROFILE_DELETE"
                ,      ( profile_selected
                    ?    "PROFILE DELETE:\n"+ Settings.Working_profile
                    +     "\n("+ Settings.get_file_path_lastModified_date_str( Profile.get_profile_file_path(Settings.Working_profile) ) +")"
                    :    "PROFILE DELETE:\n"+ "(not selected)"
                    )
                ,   (profile_selected)  ? Settings.FG_COLOR_RDY : Settings.FG_COLOR_OFF
                );

        // }}}
        // ...zip {{{
        int prof_count = Profile.Get_Profiles_Dict_size();
        set_np_label_fg("PROFILE_ZIP"
                ,      ((prof_count > 0)
                    ?    "ZIP ALL PROFILES:\n("+ prof_count +" entries)"
                    :    "NO PROFILE TO ZIP"
                    )
                ,   (prof_count > 0)    ? Settings.FG_COLOR_RDY : Settings.FG_COLOR_OFF
                );

        // }}}
        // ...unzip {{{
        int arch_size = Profile.get_ProfileArchiveList_size();
        s =   (arch_size > 0)
            ?  "UNZIP PROFILE ARCHIVE:"
            + "\n("+ arch_size +" entries)"
            + "\n("+ Settings.get_file_path_lastModified_date_str( Profile.get_zip_file_path() ) +")"
            :  "NO ARCHIVE TO UNZIP";

        set_np_label_fg("PROFILE_UNZIP"
                , s
                , (arch_size > 0) ? Settings.FG_COLOR_RDY : Settings.FG_COLOR_OFF
                );

        // }}}
        // ...zip del {{{
        set_np_label_fg("PROFILE_ZIP_DELETE"
                ,      ((arch_size > 0)
                    ?    "DELETE PROFILES ARCHIVE:\n("+ arch_size +" entries)"
                    :    "NO PROFILES ARCHIVE TO DELETE"
                    )
                , (arch_size > 0)   ? Settings.FG_COLOR_RDY : Settings.FG_COLOR_OFF
                );

        // }}}
        // ...unzip factory {{{
        s = "INSTALL PROFILE DEFAULTS\n("+get_APK_DATE()+")";
        set_np_label_fg("PROFILE_UNZIP_DEFAULTS", s, Settings.FG_COLOR_RDY);

        // }}}
        //}}}
        // LOGGING {{{
        int logging_fg_color  = Settings.LOGGING ? Settings.FG_COLOR_ON : Settings.FG_COLOR_RDY;

        // FREEZED
        if(       Settings.FREEZED )        set_np_label_fg("FREEZED"     , "FREEZED"            , Settings.FG_COLOR_ON );
        else                                set_np_label_fg("FREEZED"     , "RUNNING"            , Settings.FG_COLOR_OFF);
        set_np_label_bg(  "FREEZED"
                , Settings.FREEZED ? "FREEZED is SET"         : "FREEZED is OFF"
                , Settings.FREEZED ? Settings.COLOR_FIREBRICK : Settings.COLOR_FORESTGREEN
                );

        // OFFLINE
        if( Settings.OFFLINE )              set_np_label_fg("OFFLINE"     , "OFFLINE"            , Settings.FG_COLOR_ON );
        else                                set_np_label_fg("OFFLINE"     , "ONLINE"             , Settings.FG_COLOR_OFF);
        set_np_label_bg(  "OFFLINE"
                , Settings.OFFLINE ? "OFFLINE"                : "ONLINE"
                , Settings.OFFLINE ? Settings.COLOR_FIREBRICK : Settings.COLOR_FORESTGREEN
                );

        // MONITOR
        if( Settings.MONITOR )              set_np_label_fg("MONITOR"     , "MONITOR is ON"      , Settings.FG_COLOR_ON );
        else                                set_np_label_fg("MONITOR"     , "MONITOR is OFF"     , Settings.FG_COLOR_OFF);

        // LOGGING
        if( Settings.LOGGING )              set_np_label_fg("LOG"         , "LOG is ON"          , Settings.FG_COLOR_ON );
        else                                set_np_label_fg("LOG"         , "LOG is OFF"         , Settings.FG_COLOR_OFF);

        // ANDROID STUDIO
        if( Settings.LOG_CAT )              set_np_label_fg("LOG_CAT"     , "LOG_CAT is ON"      , logging_fg_color);
        else                                set_np_label_fg("LOG_CAT"     , "LOG_CAT is OFF"     , Settings.FG_COLOR_OFF);

        // MODULES
        if( Settings.is_LOG_ACTIVITY() )    set_np_label_fg("LOG_ACTIVITY", "LOG ACTIVITY is ON" , logging_fg_color);
        else                                set_np_label_fg("LOG_ACTIVITY", "LOG ACTIVITY is OFF", Settings.FG_COLOR_OFF);
        if( Settings.is_LOG_CLIENT()   )    set_np_label_fg("LOG_CLIENT"  , "LOG CLIENT is ON"   , logging_fg_color);
        else                                set_np_label_fg("LOG_CLIENT"  , "LOG CLIENT is OFF"  , Settings.FG_COLOR_OFF);
        if( Settings.is_LOG_PARSER()   )    set_np_label_fg("LOG_PARSER"  , "LOG PARSER is ON"   , logging_fg_color);
        else                                set_np_label_fg("LOG_PARSER"  , "LOG PARSER is OFF"  , Settings.FG_COLOR_OFF);
        if( Settings.is_LOG_PROFILE()  )    set_np_label_fg("LOG_PROFILE" , "LOG PROFILE is ON"  , logging_fg_color);
        else                                set_np_label_fg("LOG_PROFILE" , "LOG PROFILE is OFF" , Settings.FG_COLOR_OFF);
        if( Settings.is_LOG_SETTINGS() )    set_np_label_fg("LOG_SETTINGS", "LOG SETTINGS is ON" , logging_fg_color);
        else                                set_np_label_fg("LOG_SETTINGS", "LOG SETTINGS is OFF", Settings.FG_COLOR_OFF);

        // SANDBOX
        if( is_sandBox_showing )            set_np_label_fg("SANDBOX$"    , "SANDBOX is ON"      , Settings.FG_COLOR_ON );
        else                                set_np_label_fg("SANDBOX$"    , "SANDBOX is OFF"     , Settings.FG_COLOR_OFF);

        //}}}
        // TAB DYNAMIC LABELS {{{
        if((RTabsClient.CTRL_Map.size() > 0) || (RTabsClient.TABS_Map.size() > 0))
        {
            show_np_GUI_FONT        ();
            show_np_GUI_STYLE       ();
            show_np_GUI_TYPE        ();
            show_np_MARK_SCALE_GROW ();
            show_np_TOOL_URL        ();
            show_np_ORIENTATION     ();
            show_np_bookmark        ();
            show_np_memory          ();
            show_np_palette         ();
            show_np_profile         ();
            show_np_scale           ();
            show_np_server          ();
            show_np_status          ();
            show_np_webview         ();
            show_np_wol             ();
        }
        //}}}
        // DISPLAY COLORS {{{
        sync_GUI_colors(caller);

        // }}}
        // [wvTools.sb] recompute scrollbar {{{
        schedule_on_webView_sync(caller);

        // }}}
    }
    //}}}
    //}}}

    // show_np_GUI_FONT {{{
    private void show_np_GUI_FONT()  { set_np_label("GUI_FONT"   , Settings.GUI_FONT.replace("_"," ") ); }
    //}}}
    // show_np_GUI_STYLE {{{
    private void show_np_GUI_STYLE() { set_np_label("GUI_STYLE"  , Settings.GUI_STYLE                 ); }
    //}}}
    // show_np_GUI_TYPE {{{
    private void show_np_GUI_TYPE()  { set_np_label("GUI_TYPE"   , Settings.GUI_TYPE.replace("_"," ") ); }
    //}}}
    // show_np_MARK_SCALE_GROW {{{
    private void show_np_MARK_SCALE_GROW()  { set_np_label("MARK_SCALE_GROW", "MARK SCALE (x"+(int)Settings.MARK_SCALE_GROW+")"); }
    //}}}
    // show_np_profile {{{
    private void show_np_profile()
    {
        int idx = Settings.Working_profile.lastIndexOf("/");
        String profile_base_name
            = (idx >= 0)
            ?  Settings.Working_profile.substring(idx+1)
            :  Settings.Working_profile
            ;

        String number_of_tabs
            = "("+ RTabsClient.TABS_Map.size() +" Tabs)"
            ;

        String profiles_dir
            = "Profiles_dir:\n"
            + "file://"+ Settings.Get_Profiles_dir()
            ;

        String text_and_info
            = profile_base_name +"\n"
            + number_of_tabs    + NotePane.INFO_SEP +"\n"
            + profiles_dir
            ;

        set_np_label("PROFILETABS", text_and_info);
    }
    //}}}

    // show_np_ORIENTATION {{{
    private void show_np_ORIENTATION()
    {
        /* [Landscape]..[Portrait] {{{*/
        String label_LANDSCAPE     = "Landscape";
        String label_PORTRAIT      =  "Portrait";

        switch( activity.getRequestedOrientation() ) {
            case Configuration.ORIENTATION_LANDSCAPE: label_LANDSCAPE += "\n"+Settings.SYMBOL_CHECK_HEAVY; break;
            case Configuration.ORIENTATION_PORTRAIT : label_PORTRAIT  += "\n"+Settings.SYMBOL_CHECK_HEAVY; break;
        }

        set_np_label("PORTRAIT"     , label_PORTRAIT      );
        set_np_label("LANDSCAPE"    , label_LANDSCAPE     );
        /*}}}*/
        /* [Screen Auto Rotation]..[Screen Rotation Locked] {{{*/
        String label_SCREEN_ROTATION
            = (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LOCKED)
            ?  "Screen Rotation Locked"
            :  "Screen Auto Rotation\n"+Settings.SYMBOL_CHECK_HEAVY;

        set_np_label("SCREEN_ROTATION", label_SCREEN_ROTATION );
        /*}}}*/
    }
    //}}}
    // show_np_TOOL_URL {{{
    private void show_np_TOOL_URL()
    {
//Settings.MON(TAG_TOOL, "show_np_TOOL_URL");
        // CURRENT URL ACTION ..(SENDINPUT .. WEBVIEW.. BROWSER)
        boolean do_browser = Settings.TOOL_URL_ACTION.equals(Settings.TOOL_URL_ACTION_BROWSER);
    //  boolean do_webview = Settings.TOOL_URL_ACTION.equals(Settings.TOOL_URL_ACTION_WEBVIEW);
        boolean do_sendkey = Settings.TOOL_URL_ACTION.equals(Settings.TOOL_URL_ACTION_SENDKEY);
//Settings.MON(TAG_TOOL, "...do_browser=["+ do_browser +"]");
//Settings.MON(TAG_TOOL, "...do_sendkey=["+ do_sendkey +"]");
//Settings.MON(TAG_TOOL, "...do_webview=["+ do_webview +"]");

        // TOOL_URL TEXT
        String label;
        if     (do_browser) label = Settings.SYMBOL_BROWSER;
        else if(do_sendkey) label = Settings.SYMBOL_SENDKEY;
        else                label = Settings.SYMBOL_WEBVIEW;

        // TOOL_URL COLOR
        int bg_color;
        if     (do_browser) bg_color = Color.RED   ;
        else if(do_sendkey) bg_color = Color.LTGRAY;
        else                bg_color = Color.BLUE  ;

        // UPDATE TOOL_URL
        set_np_label_bg("TOOL_URL", label, bg_color);
    }
    //}}}
    // show_np_palette {{{
    private void show_np_palette()
    {
        set_np_label("PALETTE", Settings.get_palette_description());
    }
    //}}}
    // show_np_memory {{{
    private void show_np_memory()
    {
        set_np_label("MEMORY"
                , MemoryStatus()
                +"\n"
                + NotePane.PoolStatus());
    }
    private String MemoryStatus()
    {
    //  String[] units = { "b", "kb", "Mb", "Gb", "Tb" }; // ...sure!
        String[] units = { "b", "kb", "  ", "Gb", "Tb" }; // ...Mb suppressed by default
        int mu         =    0;// 1     2     3     4
        int tu         =    0;// 1     2     3     4
        int fu         =    0;// 1     2     3     4

        Runtime runtime = Runtime.getRuntime();
        long m = runtime.  maxMemory();
        long t = runtime.totalMemory();
        long f = runtime. freeMemory();

        while(m > 1000) { ++mu; m = (m+500)/1000; }
        while(t > 1000) { ++tu; t = (t+500)/1000; }
        while(f > 1000) { ++fu; f = (f+500)/1000; }

    //  return String.format("MEMORY [Max Tot. Free]\n%3d%s %3d%s %3d%s"
        return String.format("MEMORY (Mb):\n%3d%s %3d%s %3d%s"
                , m, units[mu]
                , t, units[tu]
                , f, units[fu]);
    }
    //}}}
    // show_np_status {{{
    private void show_np_status()
    {
        String s = Settings.SYMBOL_RIGHT_BLACK_TRIANGLE;
        set_np_label("STATUS"
                , String.format("STATUS:\n%s%s (%s-%s)\n%s%s\n%s(%s old)"
                    ,                     s,Settings.get_APP_NAME()
                    ,                           BuildConfig.FLAVOR
                    ,                              BuildConfig.BUILD_TYPE
                    ,                                   s,get_APK_VERSION()
                    ,                                         s, get_APK_AGE()
                    ));
    }
    //public static String Get_BuildConfig_FLAVOR() { return BuildConfig.FLAVOR; }
    //}}}
    // show_np_bookmark {{{
    private void show_np_bookmark()
    {
        String title
            = !TextUtils.isEmpty( Settings.BOOKMARK_TITLE )
            ?                     Settings.BOOKMARK_TITLE
            :                     Settings.BOOKMARK_URL;

        set_np_label("BOOKMARK"
                , "LAST BOOKMARKED: "+ title.replace("\n"," ")
                + NotePane.INFO_SEP+"\n"+Settings.BOOKMARK_URL
                );
    }
    //}}}
    // show_np_webview {{{
    private void show_np_webview() { show_np_webview( null ); }
    private void show_np_webview(NotePane np_for_url)
    {
        // UPDATE (LAST VISITED WEBVIEW TAB)
        String title
            = !TextUtils.isEmpty( Settings.REQUESTED_TITLE )
            ?  Settings.REQUESTED_TITLE
            :  Settings.REQUESTED_URL;

        if((np_for_url != null) && (np_for_url.button != null))
            set_np_label_bg("WEBVIEW$"                              // do not use [np_for_url]...
                    , "LAST VISITED: "+ title.replace("\n"," ")
                    + NotePane.INFO_SEP+"\n"+Settings.REQUESTED_URL
                    , np_for_url.button.getBackgroundColor()        // ...only use its bgcolor
                    );
        else
            set_np_label("WEBVIEW$" // .. (one that has no argument)
                    , "LAST VISITED: "+ title.replace("\n"," ")
                    + NotePane.INFO_SEP+"\n"+Settings.REQUESTED_URL
                    );
    }
    //}}}
    // show_np_scale {{{
    private void show_np_scale()
    {
        //Settings.SYMBOL_LEFT_RIGHT_ARROW
        //Settings.SYMBOL_UP_DOWN_ARROW
        String glass = (Settings.DEV_SCALE > 1)
            ? Settings.SYMBOL_MAGNIFY_RIGHT
            : Settings.SYMBOL_MAGNIFY_LEFT;

        set_np_label("SCALE"
                ,     String.format("%s %.2f %s%2.1f%s"
                        ,            glass
                        , Settings      .DEV_SCALE
                        , Settings           .SYMBOL_LEFT_RIGHT_ARROW
                        , Settings             .TABS_RATIO
                        , Settings                  .SYMBOL_UP_DOWN_ARROW
                        ));

    }
    //}}}
    // show_np_wol {{{
    private void show_np_wol()
    {
        boolean have_wol_required_params
            =  !TextUtils.isEmpty( Settings.SERVER_MAC   )
            && !TextUtils.isEmpty( Settings.SERVER_SUBNET);

                String     ip_param =     "IP=["+ Settings.SERVER_IP     +"]"+ (TextUtils.isEmpty(Settings.ServerID) ? "" : " ("+Settings.ServerID+")");
                String    mac_param =    "MAC=["+ Settings.SERVER_MAC    +"]";
                String subnet_param = "SUBNET=["+ Settings.SERVER_SUBNET +"]";

        String label = this_RTabsClient.get_WOL_label();

        set_np_label_fg("WOL$" // tag (fully_qualified)
                , label
                + NotePane.INFO_SEP+"\n(Wake On LAN)\n"
                + "LAST CONNECTED SERVER:\n"
                +     ip_param         +"\n"
                +    mac_param         +"\n"
                + subnet_param
                , have_wol_required_params ? Settings.FG_COLOR_RDY : Settings.FG_COLOR_OFF
                );
    }
    //}}}
    // show_np_server {{{
    public  void  sync_np_server()                            { handler.re_post( hr_sync_np_server ); }
    private final Runnable hr_sync_np_server = new Runnable() { @Override public void run() { show_np_server(); } };
    private void  show_np_server()
    {
//*GUI*/Settings.MON(TAG_GUI, "show_np_server", "SERVER_IP=["+Settings.SERVER_IP+"] SERVER_PORT=["+Settings.SERVER_PORT+"]");
        // SERVER [IP:PORT]
        // SIGNIN SERVER-1           255.255.255.0 CB-4E-8D-91-BB ACK   -- success
        // SIGNIN SGP512   password  WRONG         PASSWORD       ACK   -- failure
        //.......................... SUBNET------- MAC----------- ACK
        // 1/3 FAILED ATTEMPT {{{
        String       server = "";
        String signin_error = this_RTabsClient.get_SIGNIN_ERROR_MESSAGE();
        if( !signin_error.equals("") ) {
            server
                =  signin_error
                ;
        }
        // }}}
        // 2/3 NO SIGNIN ATTEMPT YET {{{
        else if(Settings.ServerID.equals("") ) {
            server
                = Settings.get_APP_NAME()+"-"
                + Settings.SERVER_IP +"\n"
                + "PORT "
                + Settings.SERVER_PORT
                ;
        }
        // }}}
        // 3/3 SIGNED IN {{{
        else {
            server
                = Settings.get_APP_NAME()+" "
                + Settings.ServerID      +"\n"
                + Settings.SERVER_SUBNET +"\n"
                + Settings.SERVER_MAC
                ;
        }

        // }}}
        // UPDATE SERVER BUTTON {{{
        boolean connected = ((this_RTabsClient != null) && this_RTabsClient.isConnected());
        set_np_label_bg("SERVER"
                ,        server
                , connected ? Settings.COLOR_FORESTGREEN : Settings.COLOR_FIREBRICK
                );

        //}}}
    }
    //}}}
    // toggle_TOOL_URL {{{
    private void toggle_TOOL_URL()
    {
        // CHOOSE WETHER TO SHOW URL IN WEBVIEW OR ON SERVER PC
//Settings.MON(TAG_TOOL, "toggle_TOOL_URL");
        // CURRENT URL ACTION ..(SENDINPUT .. WEBVIEW.. BROWSER)
        boolean do_browser = Settings.TOOL_URL_ACTION.equals(Settings.TOOL_URL_ACTION_BROWSER);
        boolean do_webview = Settings.TOOL_URL_ACTION.equals(Settings.TOOL_URL_ACTION_WEBVIEW);
        boolean do_sendkey = Settings.TOOL_URL_ACTION.equals(Settings.TOOL_URL_ACTION_SENDKEY);
//Settings.MON(TAG_TOOL, "...do_browser=["+ do_browser +"]");
//Settings.MON(TAG_TOOL, "...do_sendkey=["+ do_sendkey +"]");
//Settings.MON(TAG_TOOL, "...do_webview=["+ do_webview +"]");

        // TOGGLE TRANSITION .. (from default=[1] .. [2] .. [3] .. back to [1])
        if     (do_browser) { do_sendkey =  true; do_browser = false; /*do_webview = false;*/ }    // from 3 to 1
        else if(do_sendkey) { do_sendkey = false; do_browser = false; /*do_webview =  true;*/ }    // from 1 to 2
        else if(do_webview) { do_sendkey = false; do_browser =  true; /*do_webview = false;*/ }    // from 2 to 3

        if     (do_browser) Settings.TOOL_URL_ACTION = Settings.TOOL_URL_ACTION_BROWSER;
        else if(do_sendkey) Settings.TOOL_URL_ACTION = Settings.TOOL_URL_ACTION_SENDKEY;
        else                Settings.TOOL_URL_ACTION = Settings.TOOL_URL_ACTION_WEBVIEW;

        show_np_TOOL_URL();
    }
    //}}}
    //}}}
    // TABS {{{
    // fit_scale_to_window {{{
    public static final int FIT_H = 1;
    public static final int FIT_W = 2;
    public void fit_scale_to_window(int which, String caller)
    {
        caller += "->fit_scale_to_window";

        if(D) MLog.log(caller+"("+ ((which == FIT_W) ? "FIT_W" : "FIT_H") +"):");

        // BEFORE
        if(D) MLog.log(" SCREEN_W=["+ Settings.SCREEN_W  +"]");
        if(D) MLog.log("DISPLAY_W=["+ Settings.DISPLAY_W +"]");
        if(D) MLog.log("DISPLAY_H=["+ Settings.DISPLAY_H +"]");
        if(D) MLog.log("DEV_SCALE=["+ Settings.DEV_SCALE +"]");

        // TABS MAX IN GRID UNITS
        Rect r = NotePane.get_tabs_perimeter_rect(RTabsClient.TABS_Map);

        int l = (r.left           ) * Settings.TAB_GRID_S;
        int t = (r.top            ) * Settings.TAB_GRID_S;
        int w = (r.right  - r.left) * Settings.TAB_GRID_S;
        int h = (r.bottom - r.top ) * Settings.TAB_GRID_S;
        if((w == 0) || (h == 0)) return;

        // INCLUDE MARGINS
        w += 2*l;
        h += 2*t;

        if(mClamp == null) mClamp = new Clamp( this );
        mClamp.margin_left = Settings.Get_clamp_playground_margin_left();

        float d_w = Settings.DISPLAY_W - mClamp.margin_left;
        float d_h = Settings.DISPLAY_H;

        // SCALE TO FILL WIDTH OR HEIGHT
        if(     which == FIT_W) Settings.DEV_SCALE = d_w / (float)(w);
        else if(which == FIT_H) Settings.DEV_SCALE = d_h / (float)(h);
        if(D) MLog.log("DEV_SCALE=["+Settings.DEV_SCALE+"]");

        // ADJUST TABS DIM EFFECT
        TABS_CONTAINER_DIMMED_SCALE_MIN = 1f - (float)mClamp.margin_left / (float)Settings.DISPLAY_W; // 0.9f
        vsv.setPivotX( vsv.getWidth ()     );
        vsv.setPivotY( vsv.getHeight() / 2 );

/* //{{{
        // UPDATE VIEWPORT MIN-MAX
        //Settings.set_VIEWPORT(l, t, w, h); // see RTabsClient.layout_tabs_container_frame

//tabs_container.setBackgroundColor(Color.YELLOW); // XXX
//vsv           .setBackgroundColor(Color.BLUE  ); // XXX
//hsv           .setBackgroundColor(Color.GREEN ); // XXX

//hsv.setScrollX( tabs_container.getPaddingRight () );  // should be same as left viewport margin
//vsv.setScrollY( tabs_container.getPaddingBottom() );  // should be same as left viewport margin

//tabs_container.setTranslationX(Settings.DISPLAY_W/2 - w/2);
//tabs_container.setTranslationY(Settings.DISPLAY_H/2 - h/2);
//hsv.setScrollX( -(Settings.DISPLAY_W/2 - w/2));
//vsv.setScrollY( -(Settings.DISPLAY_H/2 - h/2));
//tabs_container.setTranslationX(Settings.DISPLAY_W/2 - w/2);
//tabs_container.setTranslationY(Settings.DISPLAY_H/2 - h/2);

RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams)tabs_container.getLayoutParams();
rlp.leftMargin = (Settings.DISPLAY_W/2 - w/2);
rlp.topMargin  = (Settings.DISPLAY_H/2 - h/2);
tabs_container.setLayoutParams(rlp);
*/ //}}}

        // DISPLAY COLORS {{{
        sync_GUI_colors(caller);

        // }}}
    }
    //}}}
    // request_orientation {{{
    public void request_orientation(String request)
    {
        String caller = "request_orientation("+request+")";

        if(D) MLog.log(caller);
        if(D) MLog.log(" ORIENTATION_LANDSCAPE...=["+ Configuration.ORIENTATION_LANDSCAPE +"]");
        if(D) MLog.log(" ORIENTATION_PORTRAIT....=["+ Configuration.ORIENTATION_PORTRAIT  +"]");
        if(D) MLog.log(" ORIENTATION_UNDEFINED...=["+ Configuration.ORIENTATION_UNDEFINED +"]");

        /* CURRENTLY REQUESTED ORIENTATION */
        int currently_requested = activity.getRequestedOrientation();
        if(D) MLog.log(" CURRENTLY REQUESTED.....=["+ currently_requested    +"]");

        /* REQUESTING ORIENTATION */
        int requesting_orientation;
        if     (request.equals("LANDSCAPE")             ) requesting_orientation = Configuration.ORIENTATION_LANDSCAPE;
        else if(request.equals("PORTRAIT" )             ) requesting_orientation = Configuration.ORIENTATION_PORTRAIT ;
        else                                              requesting_orientation = Configuration.ORIENTATION_UNDEFINED;

        /* TOGGLE BACK TO AUTO .. (undefined) */
        if(currently_requested == requesting_orientation) requesting_orientation = Configuration.ORIENTATION_UNDEFINED;

        /* SET NEW REQUESTED ORIENTATION */
        if(D) MLog.log(" REQUESTING..............=["+ requesting_orientation +"]");
        activity.setRequestedOrientation( requesting_orientation );

        sync_GUI_colors(caller);
    }
    //}}}
    // toggle_screen_orientation .. SCREEN_ROTATION {{{
    private void toggle_screen_orientation()
    {
/*{{{
:!start explorer "https://stackoverflow.com/questions/5564211/disable-screen-rotation-at-runtime-on-android"
}}}*/
/*{{{
        AsyncTask.execute(new Runnable() {
            public void run() {
                try {
                    IWindowManager   wm = IWindowManager.Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE));
                    if( autorotate ) wm.thawRotation();
                    else             wm.freezeRotation(-1);
                }
                catch(RemoteException ex) { MLog.log_ex(ex,"toggle_screen_orientation("+autorotate+")"); }
            }
        });
}}}*/
/*{{{
:!start explorer "https://inducesmile.com/android-game-development/android-screen-orientation-lock-tutorial/"
}}}*/
    if( activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LOCKED )
        activity.setRequestedOrientation(     ActivityInfo.SCREEN_ORIENTATION_LOCKED );
    else
        activity.setRequestedOrientation(     ActivityInfo.SCREEN_ORIENTATION_SENSOR );
    }
    //}}}
    // set_np_label {{{
    private void set_np_label      (String np_tag, String label                            ) { set_np_label_bg_fg(np_tag, label, Color.TRANSPARENT, Color.TRANSPARENT); }
    private void set_np_label_fg   (String np_tag, String label              , int fg_color) { set_np_label_bg_fg(np_tag, label, Color.TRANSPARENT, fg_color         ); }
    private void set_np_label_bg   (String np_tag, String label, int bg_color              ) { set_np_label_bg_fg(np_tag, label, bg_color         , Color.TRANSPARENT); }
    // set_np_label_bg_fg {{{
    private void set_np_label_bg_fg(String np_tag, String label, int bg_color, int fg_color)
    {
        // adjust colors brightness {{{
        if(this_RTabsClient == null) return;

        boolean bg_is_bright = (bg_color == Color.TRANSPARENT) || (ColorPalette.GetBrightness( bg_color ) >  96);
        boolean fg_is_bright = (ColorPalette.GetBrightness( fg_color ) > 192);
//Settings.MON("set_np_label_bg_fg("+np_tag+")", "bg_is_bright="+bg_is_bright+" fg_is_bright="+fg_is_bright);

        if     ( bg_is_bright &&  fg_is_bright) bg_color = ColorPalette.GetColorDarker (bg_color ,.5);
        else if(!bg_is_bright && !fg_is_bright) fg_color = ColorPalette.GetColorLighter(fg_color ,.5);

        //}}}

        NotePane control_np = set_np_label_bg_fg(np_tag, label, bg_color, fg_color, RTabsClient.CTRL_Map );
        NotePane    user_np = set_np_label_bg_fg(np_tag, label, bg_color, fg_color, RTabsClient.TABS_Map );
        NotePane wvtools_np = set_np_label_bg_fg(np_tag, label, bg_color, fg_color,     WVTools.TOOL_Map );

        // FORWARD CONTROLS-INFO INTO USER AND WVTOOLS TABS
        if((control_np != null) && (user_np    != null)) user_np   .setTextAndInfo( control_np.text );
        if((control_np != null) && (wvtools_np != null)) wvtools_np.setTextAndInfo( control_np.text );

        // make new label fit within button bounds
        if((control_np != null) || (user_np != null))
            handler.re_postDelayed( hr_set_np_label_fitText, SET_NP_LABEL_FITTEXT_DELAY);
    }
    //}}}
    // set_np_label_bg_fg {{{
    private NotePane set_np_label_bg_fg(String np_tag, String label, int bg_color, int fg_color, HashMap<String, Object> hashMap)
    {
        NotePane np = null;
        if(hashMap.size() > 0)
        {
            np = RTabsClient.get_np_with_tag(np_tag, hashMap); // ...only the first will be updated!
            if(np != null) {
                if(label.indexOf(NotePane.INFO_SEP) > 0) np.setTextAndInfo( label );
                else                                     np.setText       ( label );
                np.needs_fitText = true;

                // COLORS
                if(np.button != null)
                {
                    if(bg_color != Color.TRANSPARENT) {
                        np.bg_color                 = bg_color;
                        np.button.setBackgroundColor( bg_color );
                    }
                    if(fg_color != Color.TRANSPARENT) {
                        np.fg_color                 = fg_color;
                        np.button.setTextColor      ( fg_color );
                    }
                }
            }
        }
        return np;
    }
    // }}}
    // hr_set_np_label_fitText {{{
    private final Runnable hr_set_np_label_fitText = new Runnable()
    {
        @Override public void run() {

            NotePane np;

            for(Map.Entry<String, Object> entry : RTabsClient.TABS_Map.entrySet())
            {
                if(entry.getKey() == Settings.ENTRY_PALETTE) continue;
                np = (NotePane)entry.getValue();
                if(np.needs_fitText) {
                    if(np.button != null) {
                        np.button.fitText();
                        np.button.invalidate();
                        np.needs_fitText = false;
                    }
                }
            }

            for(Map.Entry<String, Object> entry : RTabsClient.CTRL_Map.entrySet())
            {
                np = (NotePane)entry.getValue();
                if(np.needs_fitText) {
                    if(np.button != null) {
                        np.button.fitText();
                        np.button.invalidate();
                        np.needs_fitText = false;
                    }
                }
            }

        }
    };
    //}}}
    //}}}

    // select_UNICODE_SHEET {{{
    private        int selected_sheet;

    public void select_UNICODE_SHEET(int sheet)
    {
    //  if(selected_sheet < 0) this_RTabsClient.clear_BLOCK_ROWS_LABEL();
        int delay = (selected_sheet < 0) ? 100 : 250;
        selected_sheet = sheet;
        handler.re_postDelayed( hr_select_UNICODE_SHEET , delay);
    }

    private final Runnable hr_select_UNICODE_SHEET = new Runnable() {
        @Override
        public void run() {
            if(selected_sheet < 0) return;
            this_RTabsClient.select_UNICODE_SHEET( selected_sheet );
            selected_sheet = -1;
        }
    };

    //}}}
    // get_NotePane_named {{{
    private NotePane get_NotePane_named(String name)
    {
        for(Map.Entry<String, Object> entry : RTabsClient.TABS_Map.entrySet())
        {
            if(entry.getKey() == Settings.ENTRY_PALETTE) continue;
            NotePane np = (NotePane)entry.getValue();
            if( np.name.equals(name) )
                return np;
        }
        return null;
    }
    //}}}
    //}}}
    // Toast {{{
    //{{{
    private  Toast mToast = null;
    private String last_toast_msg = Settings.EMPTY_STRING;

    //}}}
    public  void toast_cancel     ()     { if(mToast == null) return;                                             mToast.cancel(); }
    public  void toast_again      ()     { if(mToast == null) return; if(last_toast_msg != Settings.EMPTY_STRING) mToast.show  (); }
    public  void toast_again_clear()     { if(mToast == null) return;    last_toast_msg  = Settings.EMPTY_STRING;                  }
    public  void toast_short(String msg) { toast(msg, Toast.LENGTH_SHORT); }
    public  void toast_long (String msg) { toast(msg, Toast.LENGTH_LONG ); }
    // toast {{{
    private void toast(String msg, int duration)
    {
//*GUI*/Settings.MOC(TAG_GUI, "toast("+msg+")");
        if(mToast == null)
        {
            mToast
                = Toast.makeText( activity.getApplicationContext()
                        ,         Settings.get_APP_NAME()+":\n"+ msg
                        ,         duration);
        }
        else {
            mToast.setText      ( Settings.get_APP_NAME()+":\n"+ msg);
            mToast.setDuration  ( duration );
        }

        mToast.show();
        last_toast_msg = msg;
/*
        View decorView = activity.getWindow().getDecorView();
        Snackbar.make( decorView
                ,      Settings.get_APP_NAME()+"\n"+ msg
                ,      duration)
            .show();
*/
    }
    //}}}
//    // toast_cancel {{{
//    public  void toast_cancel()
//    {
//        if(mToast == null) return;
//
//        View view = mToast.getView();
//        if(view != null)
//        {
//            boolean vis = (view.getVisibility() == View.VISIBLE);
///*GUI*/Settings.MOC(TAG_GUI, "toast_cancel: vis=["+ vis+"]");
//
//            if( vis ) mToast.cancel();
//
//            //view.setX(100);
//            //view.setY(100);
//            //view.setVisibility( View.GONE   );
//        }
//    }
//    //}}}
    //}}}
    // assert_TOOL_URL_np {{{
/*
    private NotePane get_TOOL_URL()
    {
        NotePane np  = this_RTabsClient.get_np_with_tag_and_free_label("TOOL_URL", caller);
        if((np != null)) {
            if(M||D) Settings.MON(TAG_GUI, "get_TOOL_URL: FOUND A TOOL_URL TAB");
            return np;
        }

        np  = this_RTabsClient.get_np_free(caller);
        if(np != null) {
            if(M||D) Settings.MON(TAG_GUI, "get_TOOL_URL: CREATING A TOOL_URL TAB");
            np.set_tag       ("TOOL_URL");
            np.setTextAndInfo("TOOL_URL");
        }
        else {
            String msg = "Profile "+ Settings.Working_profile +":\n*** NO FREE TAB TO TURN INTO A TOOL_URL ***";
            if(M||D) Settings.MON(TAG_GUI, "get_TOOL_URL: "+ msg);
            toast_short( msg );
        }

        // ...still has to be saved along with new bookmark
        return np;
    }
*/
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** COMM */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ COM @ {{{
    // CONNECT {{{
    private void sync_server_connection(String caller)
    {
        caller += "->sync_server_connection";
//*COMM*/Settings.MOC(TAG_COMM, caller);

        sync_np(caller);

        this_RTabsClient.disconnect(caller);

        set_APP_freezed_state(false, caller);

        set_APP_offline_state(false, caller);

        enter_notification_loop(STAGE0_SIGNIN, caller);
    }
    //}}}
    // SEND {{{
    // cmd_send_OnClickListener {{{
    private final View.OnClickListener cmd_send_OnClickListener = new View.OnClickListener()
    { @Override public void onClick(View view)
        {
            request_cmd_text("cmd_send_OnClickListener");
        }
    };
    // }}}
    // cmd_text_OnKeyListener {{{
    private View.OnKeyListener cmd_text_OnKeyListener = new View.OnKeyListener()
    {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event)
        {
            if(event.getAction() == KeyEvent.ACTION_UP)
            {
                if(keyCode == KeyEvent.KEYCODE_ENTER)
                    request_cmd_text("cmd_text_OnKeyListener");
            }
            return true;
        }
    };
    // }}}
    // send_wvTools_np_cmd {{{
    public void send_wvTools_np_cmd(NotePane np)
    {
        String caller = "send_wvTools_np_cmd("+np+")";
//*WVTOOLS*/Settings.MOC(TAG_WVTOOLS, caller);

        if(this_RTabsClient == null) return;

        // TOOL NAME AS A TAG PATTERN {{{
        // RTabsClient.TABS_Map {{{
        String tag = np.name;
//*WVTOOLS*/Settings.MOM(TAG_WVTOOLS, "LOOKING FOR A [USER_TAB] with a tag == np.name=["+tag+"]");
        NotePane user_np = RTabsClient.get_np_with_tag(tag, RTabsClient.TABS_Map);
        if(user_np != null)
        {
//*WVTOOLS*/Settings.MOM(TAG_WVTOOLS, "...calling sendTabCommand("+ user_np.button+")");
            this_RTabsClient.sendTabCommand( user_np.button );
            return;
        }
//*WVTOOLS*/Settings.MOM(TAG_WVTOOLS, "...f(user_np == null)");
        //}}}
        // RTabsClient.CTRL_Map {{{
//*WVTOOLS*/Settings.MOM(TAG_WVTOOLS, "LOOKING FOR A [CONTROL_TAB] with a tag == np.name=["+tag+"]");
        NotePane control_np = RTabsClient.get_np_with_tag(np.name, RTabsClient.CTRL_Map);
        if(control_np != null)
        {
//*WVTOOLS*/Settings.MOM(TAG_WVTOOLS, "...calling sendTabCommand("+ control_np.button+")");
            this_RTabsClient.sendTabCommand( control_np.button );
            return;
        }
//*WVTOOLS*/Settings.MOM(TAG_WVTOOLS, "...f(control_np == null)");
        //}}}
        //}}}
        // WVTools np.button {{{
        if(np.button instanceof NpButton)
        {
//*WVTOOLS*/Settings.MOM(TAG_WVTOOLS, "...calling sendTabCommand("+ np.button+")");
            this_RTabsClient.sendTabCommand( np.button );
            return;
        }
//*WVTOOLS*/Settings.MOM(TAG_WVTOOLS, "...NOT (np.button instanceof NpButton)");
        //}}}
/*
        // this_RTabsClient.sendTabCommand( np.button ) {{{
        if(np.button.getTag() instanceof NotePane)
        {
//WVTOOLS//Settings.MOM(TAG_WVTOOLS, "USING NotePane from np.button.getTag() to call sendTabCommand("+ np +")");
            this_RTabsClient.sendTabCommand( np.button );
            return;
        }
        //}}}
*/
    }
    //}}}
    // send_cmd_text .. (called by sendTabCommand) {{{
    public void send_cmd_text(String cmdLine)
    {
//*COMM*/Settings.MOC(TAG_COMM, "send_cmd_text("+cmdLine+")");
/* NO! should be done at that time alrfeady
        if(        cmdLine.startsWith("SENDINPUT")
                || cmdLine.startsWith("SENDKEYS ")
          )
            cmdLine = Settings.Get_SENDKEYS_ENCODED( cmdLine );
*/

        cmd_text.setText( cmdLine ); // place command to send into GUI repository
        request_cmd_text("send_cmd_text");
    }
    // }}}
    // request_cmd_text {{{
    private void request_cmd_text(String caller)
    {
        caller  += "->request_cmd_text";
if(D||M) Settings.MOC(TAG_COMM, caller);

        CmdParser.parse( cmd_text.getText().toString() );
        String     cmd = CmdParser.cmd;
        String cmdLine = CmdParser.cmdLine;
if(D||M) Settings.MON(TAG_COMM, caller, "cmdLine=["+ cmdLine +"]");

        // NOT HANDLED HERE: (@see sendTabCommand)
        // FULLSCREEN TAG
        // TOOLS (WORKBENCH, BLOCK)
        // SENDKEYS & SENDINPUT
        // KEY_VAL
        // PROFILE

        // LOG & TEST
        if(      cmd.startsWith("xxx")                 ) { if(D) request_cmd_TEST( cmdLine ); }

        // INFO
        else if( cmdLine.equals(           "STATUS"   )) { MLog.log_STATUS   (cmdLine); }
        else if( cmdLine.equals(        "INVENTORY"   )) { MLog.log_INVENTORY(cmdLine); }
        else if( cmdLine.equals(      "PROFILETABS"   )) { MLog.log_PROFILE  (cmdLine); }

        // (...take care of parsing order)
        else if( cmd.equals(                  "LOG"   )) { set_LOGGING          ( !Settings.LOGGING, true, cmdLine); }
        else if( cmd.equals(              "LOG_CAT"   )) { set_LOG_CAT          ( !Settings.LOG_CAT               ); }
        else if( cmd.equals(            "ERRLOGGED"   )) { toggle_ERRLOGGED     (                          cmdLine); }
        else if( cmd.equals(              "FREEZED"   )) { set_APP_freezed_state(!Settings.FREEZED,        cmdLine); }
        else if( cmd.equals(              "OFFLINE"   )) { set_APP_offline_state(!Settings.OFFLINE,        cmdLine); }

        else if( cmd.startsWith("LOG_" )) { MLog.set_LOG_FILTER  (                          cmdLine); }

        // PROFILES
        else if( cmd.equals(        "PROFILES_TABLE"  ) || (cmd.equals("PROFILES") &&  Settings.is_GUI_TYPE_HANDLES())) {
            if(Handle.Get_cur_handle() != top_handle  )   show_PROFILES_TABLE(cmdLine);
            else                                          hide_PROFILES_TABLE(cmdLine);
        }
        else if( cmd.equals(        "DOCKINGS_TABLE"  ) || (cmd.equals("PROFILES") && !Settings.is_GUI_TYPE_HANDLES())) {
            if(Handle.Get_cur_handle() != dck_handle  )   show_DOCKINGS_TABLE(cmdLine);
            else                                          hide_DOCKINGS_TABLE(cmdLine);
        }
        else if( cmd.startsWith(   "PROFHIST_TABLE"  )) {
            if ( !is_fg_view_showing()                )   show_PROFHIST_TABLE(cmdLine);
            else                                          hide_PROFHIST_TABLE(cmdLine);
        }
        else if( cmd.startsWith(     "SOUNDS_TABLE"  )) {
            if ( !is_fg_view_showing()                )   show_SOUNDS_TABLE(cmdLine);
            else                                          hide_SOUNDS_TABLE(cmdLine);
        }
        else if( cmd.equals( "PROFILE_DELETE"        )) { profile_del(cmdLine, cmdLine);        }
        else if( cmd.equals( "PROFILE_DELETE"        )) { profile_del(cmdLine, cmdLine);        }
        else if( cmd.equals( "PROFILE_UNZIP"         )) { profile_unzip();            }
        else if( cmd.equals( "PROFILE_UNZIP_DEFAULTS")) { profile_unzip_factory();    }
        else if( cmd.equals( "PROFILE_ZIP"           )) { profile_zip();              }
        else if( cmd.equals( "PROFILE_ZIP_DELETE"    )) { profile_zip_del();          }
        else if( cmd.equals( "PROFILE_SAVE"          )) { profile_save();             }
        else if( cmd.equals( "PROFILE_RINSE"         )) { profile_rinse();            }
        else if( cmd.equals( "PROFILE_SYNC"          )) { profile_sync_storage_GUI(); }
        else if( cmd.equals( "PRNEXT"                )) { history_frwd(FROM_TAB, cmdLine); }
        else if( cmd.equals( "PRPREV"                )) { history_back(FROM_TAB, cmdLine); }

        // COMM
        else if( cmd.equals(               "FINISH"  )) { delayedFinish           ( cmdLine ); }
        else if( cmd.equals(                 "SAVE"  )) { Settings.SaveSettings   ( cmdLine ); Settings.SaveCookies( cmdLine ); }
        else if( cmd.equals(               "SERVER"  )) { show_ip_dialog();                    }
        else if( cmd.equals(               "SIGNIN"  )) { Settings.Send_SIGNIN(cmdLine, "SIGNIN"); sync_server_connection( cmdLine ); }
        else if( cmd.equals(                  "WOL"  )) { this_RTabsClient.sendWOL( cmdLine ); }
        else if( cmd.equals(             "TOOL_URL"  )) { toggle_TOOL_URL();                   }

        else if( cmd.equals(              "MONITOR"  )) { Settings.toggle_MONITOR(); }
        else if( cmd.equals(             "PROPERTY"  )) { toggle_PROPERTY(cmdLine); }
        else if( cmd.equals(               "MEMORY"  )) { handle_MEMORY(); }

        else if( cmd.equals(              "COMMAND"  )) { handle_COMMAND( CmdParser.argLine ); }

        // DISPLAY
        else if( cmd.equals(            "GUI_STYLE"  )) { toggle_GUI_STYLE(FROM_TAB);          invalidate( cmdLine ); }
        else if( cmd.equals(             "GUI_FONT"  )) { toggle_GUI_FONT (FROM_TAB);          invalidate( cmdLine ); }
        else if( cmd.equals(             "GUI_TYPE"  )) { toggle_GUI_TYPE (FROM_TAB);          invalidate( cmdLine ); }
        else if( cmd.equals(                "FIT_W"  )) { fit_scale_to_window(FIT_W, cmdLine); invalidate( cmdLine ); handle_hide( cmdLine ); }
        else if( cmd.equals(                "FIT_H"  )) { fit_scale_to_window(FIT_H, cmdLine); invalidate( cmdLine ); handle_hide( cmdLine ); }
        else if( cmd.equals(            "LANDSCAPE"  )) { request_orientation( cmdLine ); }
        else if( cmd.equals(             "PORTRAIT"  )) { request_orientation( cmdLine ); }
        else if( cmd.equals(      "SCREEN_ROTATION"  )) { toggle_screen_orientation(); }
        else if( cmd.equals(               "PLNEXT"  )) { this_RTabsClient.palette_next();     invalidate( cmdLine ); }
        else if( cmd.equals(               "PLPREV"  )) { this_RTabsClient.palette_prev();     invalidate( cmdLine ); }

        // WEBVIEW
        else if( cmd.equals(              "WEBVIEW"  )) { reload_WEBVIEW  (); }
        else if( cmd.equals(             "BOOKMARK"  )) { reload_BOOKMARK (); }

        else if( cmd.equals(          "DEL_COOKIES"  )) { Settings.removeAllCookies();     }
        else if( cmd.equals(         "SAVE_COOKIES"  )) { Settings.SaveCookies( cmdLine ); }
        else if( cmd.equals(         "LOAD_COOKIES"  )) { Settings.LoadCookies( cmdLine ); }

        // ACTIVITY BUILTIN
        else if( cmd.equals(              "SANDBOX"  )) { toggleSandBox(cmdLine); }

        // OTHERS .. (SHELL, SENDINPUT, ...) .. "HIDE","LOGGING","CLEAR", ...
        else if(!cmd.equals(""                       ))
        {
            this_RTabsClient.sendCmd(cmdLine, "request_cmd_text");
        }

        sync_np( caller );
    }

    // }}}
    // request_cmd_TEST {{{
    private void request_cmd_TEST(String cmdLine)
    {
        if(D) MLog.log("*** request_cmd_TEST: cmdLine=["+ cmdLine +"]");
        if( !TextUtils.isEmpty( Settings.LoadedProfile.name ) ) {
            if(cmdLine.equals("xxxk")) { MLog.log("*** Settings.LoadedProfile.clear_KEY_VAL_sb  ()"); Settings.LoadedProfile.clear_KEY_VAL_sb  (); }
            if(cmdLine.equals("xxxp")) { MLog.log("*** Settings.LoadedProfile.clear_PALETTES_sb ()"); Settings.LoadedProfile.clear_PALETTES_sb (); }
            if(cmdLine.equals("xxxt")) { MLog.log("*** Settings.LoadedProfile.clear_TABS_sb     ()"); Settings.LoadedProfile.clear_TABS_sb     (); }
            if(cmdLine.equals("xxxv")) { MLog.log("*** Settings.LoadedProfile.validate_or_delete()"); Settings.LoadedProfile.validate_or_delete(); }
        }
    }

    // }}}
    //}}}
    // FINISH {{{
    // delayedFinish {{{
    private void delayedFinish(String cmdLine)
    {
        int finish_delay_ms   = 0;

        CmdParser.parse( cmdLine );
        if( TextUtils.isEmpty( CmdParser.arg1 ) )
            finish_delay_ms = FINISH_DELAY;
        else
            try { finish_delay_ms = Integer.parseInt( CmdParser.arg1 ); } catch(Exception ignored) {}

        if(finish_delay_ms <= 0) {
            activity.finish();
        }
        else {
            set_np_label("FINISH", "3 ...finish in " + finish_delay_ms/1000 + "s");
            if(finish_delay_ms > 1000)
                handler.re_postDelayed( hr_finish_before, finish_delay_ms - 1000);
            handler.re_postDelayed    ( hr_finish       , finish_delay_ms       );
            handler.re_postDelayed    ( hr_finish_after , finish_delay_ms + 1000);
        }
    }
    //}}}
    // hr_finish_before {{{
    private final Runnable hr_finish_before = new Runnable()
    {
        @Override
        public void run() {
            set_np_label("FINISH",  "2 ...going to finish");

        }
    };
    // }}}
    // hr_finish {{{
    private final Runnable hr_finish = new Runnable() {
        @Override
        public void run() {
            set_np_label("FINISH", "1 ...finishing");

            activity.finish();
        }
    };
    // }}}
    // hr_finish_after {{{
    private final Runnable hr_finish_after = new Runnable() {
        @Override
        public void run() {
            set_np_label("FINISH", "0 ...finished");
        }
    };
    // }}}
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** PROFILES */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ PROFILES @ {{{
    // select_WORKING_PROFILE {{{
    private void select_WORKING_PROFILE(String caller)
    {
        caller += "->select_WORKING_PROFILE";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);

        // CURRENTLY LOADED PROFILE {{{
        if(D) MLog.log_profile(String.format(" %30s .. %s", "[Working_profile]"    , "["+ Settings.Working_profile              +"]"));
        if(D) MLog.log_profile(String.format(" %30s .. %s", "[CURRENTLY DISPLAYED]", "["+ RTabsClient.TABS_Map.size() +" USER TABS]"));
        if(D) MLog.log_profile(String.format(" %30s .. %s", "[LoadedProfile]"      ,      Settings.LoadedProfile.toString()         ));
        if(D) MLog.log_profile(String.format(" %30s .. %s", "[Settings.PROFILE]"   , "["+ Settings.PROFILE                      +"]"));

        //}}}
        // START FROM LAST SELECTED WORKING PROFILE {{{
        if( !TextUtils.isEmpty( Settings.Working_profile ) ) {
            Settings.PROFILE  = Settings.Working_profile;

        }
        // }}}
        // WE CURRENTLY HAVE A DISPAYED PROFILE {{{
        if(!TextUtils.isEmpty( Settings.PROFILE ) && (RTabsClient.TABS_Map.size() > 0))
        {
            if(D) MLog.log_profile(String.format(" %30s .. %s", "["+ Settings.PROFILE +"]"     , "...IS ALREADY DISPLAYED"));

        }
        // }}}
        // WORKING PROFILE IS LOADED AND READY TO BE PARSED {{{
        else if(   !TextUtils.isEmpty(                  Settings.PROFILE )
                &&  Settings.LoadedProfile.name.equals( Settings.PROFILE )
        ) {
            if(D) MLog.log_profile(String.format(" %30s .. %s", "["+ Settings.PROFILE +"]"     , "...IS ALREADY LOADED"));

        }
        // }}}
        // REJECT SOME OTHER CURRENTLY LOADED PROFILE {{{
        else if(   !TextUtils.isEmpty(       Settings.LoadedProfile.name )
                && !Settings.PROFILE.equals( Settings.LoadedProfile.name )
        ) {
            if(D) MLog.log_profile(String.format(" %30s .. %s", "[NOT THE WORKING PROFILE]"    , "...FALLING BACK TO ["+ Settings.PROFILES_TABLE +"]"));
            Settings.PROFILE = "";

        }
        // }}}
        // USE PROFILE OR DEFAULT TO PROFILES_TABLE {{{
        else if( !TextUtils.isEmpty( Settings.PROFILE ) ) {
            if(D) MLog.log_profile(String.format(" %30s .. %s", "[LAST SELECTED PROFILE]"      ,                    "["+ Settings.PROFILE        +"]"));

        }
        // }}}
        // DEFAULT TO PROFILES_TABLE {{{
        else {
            Settings.PROFILE = Settings.PROFILES_TABLE;
            if(D) MLog.log_profile(String.format(" %30s .. %s", "[NO WORKING PROFILE SELECTED]",      "DEFAULTING TO ["+ Settings.PROFILE        +"]"));

        }
        // }}}
    }
    //}}}
    // profile_download {{{
    public void profile_download(String caller)
    {
        caller += "->profile_download";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);

        fs_webview_session_stop(caller);

        enter_notification_loop(STAGE8_PROFILE_UPDATE, caller);
    }
    //}}}
    // profile_saved {{{
    public void profile_saved(String caller)
    {
        caller += "->profile_saved";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);

        top_handle.removeAllViews();
        dock_band.container.removeAllViews();

        sync_np( caller );
    }
    //}}}
    // get_Working_profile_pending_changes {{{
    public int get_Working_profile_pending_changes()
    {
        int value
            = (        (                                     Settings.Working_profile_instance == null)
                    || Profile.is_a_WORKBENCH_TEMPLATE_NAME( Settings.Working_profile_instance.name   )
              )
            ?  0                                                  // .. (nothing worth saving)
            :  Settings.Working_profile_instance.pending_changes; // user changed something since profile-load-from-storage-time
//*PROFILE*/Settings.MON(TAG_PROFILE, "get_Working_profile_pending_changes", "...return ["+value+"]");
        return value;
    }
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** DATA */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ DATA @ {{{
    // CMD_SIGNIN {{{

    private void request_SIGNIN(String caller) // {{{
    {
        caller += "->request_SIGNIN";
//*DATA*/Settings.MOC(TAG_DATA, caller);

        // wait for user to UNFREEZE or go ONLINE
        if(  Settings.FREEZED ) {
if(D) MLog.log("..."+Settings.SYMBOL_freezed+" FREEZED");

            return;
        }
        if(  Settings.OFFLINE ) {
if(D) MLog.log("..."+Settings.SYMBOL_offline+" OFFLINE");

            return;
        }

        // CONNECTION REQUEST
        this_RTabsClient.send(RTabsClient.CMD_SIGNIN, Settings.get_APP_NAME()+"-"+Settings.DEVICE +" "+ Settings.SERVER_PASS, caller);

/* // {{{
        //  +" BRAND=["+         android.os.Build.BRAND        +"]"
        //  +" DEVICE=["+        android.os.Build.DEVICE       +"]"
        //  +" DISPLAY=["+       android.os.Build.DISPLAY      +"]"
        //  +" HARDWARE=["+      android.os.Build.HARDWARE     +"]"
        //  +" MANUFACTURER=["+  android.os.Build.MANUFACTURER +"]"
        //  +" MODEL=["+         android.os.Build.MODEL        +"]"
        //  +" PRODUCT=["+       android.os.Build.PRODUCT      +"]"

        // :!start explorer "http://developer.android.com/reference/android/os/Build.html"

        // BRAND          = [Sony]
        // DEVICE         = [SGP512]
        // DISPLAY        = [23.4.A.0.546]
        // HARDWARE       = [qcom]
        // MANUFACTURER   = [Sony]
        // MODEL          = [SGP512]
        // PRODUCT        = [SGP512]

*/ // }}}
    }
    // }}}

    // }}}
    // PROFILE {{{
    private void request_PROFILE_UPDATE(String caller) // {{{
    {
        caller += "->request_PROFILE_UPDATE";
//*DATA*/Settings.MOC(TAG_DATA, caller);

        // transition fron request to parse stage
        if( this_RTabsClient.check_PROFILE_UPDATE_reply(caller) )
            data_looper_start(caller);
        else
            this_RTabsClient.send(RTabsClient.CMD_PROFILE_UPDATE, caller);
    }
    // }}}
    private void request_PROFILE_PARSE(String caller) // {{{
    {
        caller += "->request_PROFILE_PARSE";
//*DATA*/Settings.MOC(TAG_DATA, caller);

        this_RTabsClient.parse_PROFILE(caller);
    }
    // }}}
    //}}}
    // TABS {{{
    // request_TABS_GET {{{
    private void request_TABS_GET(String caller)
    {
        caller += "->request_TABS_GET";
//*DATA*/Settings.MOC(TAG_DATA, caller);

        // transition fron request to parse stage
        if( this_RTabsClient.check_TABS_GET_reply(caller) )
            data_looper_start(caller);
        else
            this_RTabsClient.send(RTabsClient.CMD_TABS_GET, caller);
    }
//}}}
    // request_TABS_PARSE {{{
    private void request_TABS_PARSE()
    {
        if(D) MLog.log("request_TABS_PARSE: LoadedProfile.name=["+ Settings.LoadedProfile.name +"]");

        if     ( !Settings.LoadedProfile.name.equals( Settings.PROFILES_TABLE )) this_RTabsClient.parse_TABS(RTabsClient.TABS_Map, "request_TABS_PARSE");
        else if( !Settings.LoadedProfile.name.equals( Settings.CONTROLS_TABLE )) this_RTabsClient.parse_TABS(RTabsClient.CTRL_Map, "request_TABS_PARSE");
        else                                                                     this_RTabsClient.parse_TABS(RTabsClient.PROF_Map, "request_TABS_PARSE");
    }
//}}}
    // request_TABS_LAYOUT {{{
    public void request_TABS_LAYOUT(String caller)
    {
        caller += "->request_TABS_LAYOUT";
//*DATA*/Settings.MON(TAG_DATA, caller, "Settings.PROFILE=["+Settings.PROFILE+"]");

        // 1/3 - LAYOUT CURRENT WORKING PROFILE
        if( !TextUtils.isEmpty( Settings.Working_profile ) )
        {
            Settings.PROFILE  = Settings.Working_profile;
            this_RTabsClient.apply_TABS_LAYOUT(RTabsClient.TABS_Map, tabs_container, caller);
        }
        // 2/3 - SELECT WORKING PROFILE FROM PROFILES STORAGE
        else if(Profile.Get_Profiles_Dict_size() > 0)
        {
            if( Settings.is_GUI_TYPE_HANDLES()) show_PROFILES_TABLE(caller);
            else                                show_DOCKINGS_TABLE(caller);
        }
        // 3/3 - DEFAULT TO SHOWING CONTROLS TABLE .. (TO ACESS PROFILES ARCHIVE)
        else {
            show_CONTROLS_TABLE(caller);
        }

        sync_np(caller);
    }
    //}}}
    // }}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** VIEWS */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ VIEWS @ {{{
    // get_view_name {{{
    public  String get_view_name(View view)
    {
        // search all known classes views
        String                                  name =    this._get_view_name( view ); // call (   this instance) built-in namer
        if((name == null) && (wvTools != null)) name = wvTools._get_view_name( view ); // call (WVTools instance) built-in namer

        if( name == null)                       name = view.toString();
        return                                  name.replace("\n", " ");
    }
    //}}}
    // _get_view_name {{{
    public  String _get_view_name(View view)
    {
        if     (view ==                 null) return "NULL_VIEW";

        else if(view == top_handle          ) return "top_handle";
        else if(view == mid_handle          ) return "mid_handle";
        else if(view == bot_handle          ) return "bot_handle";

        else if(view == controls_container  ) return "controls_container";
        else if(view == log_container       ) return "log_container";
        else if(view == seekers_container   ) return "seekers_container";

        else if(view == cart_band           ) return "cart_band";
        else if(view == dock_band           ) return "dock_band";
    //  else if(view == drag_band           ) return "drag_band";   // is either (null) or (one of the others)
        else if(view == hist_band           ) return "hist_band";
        else if(view == hist_band.back_nb   ) return "back_nb";
        else if(view == hist_band.frwd_nb   ) return "frwd_nb";
        else if(view == hist_band.prof_nb   ) return "prof_nb";
        else if(view == show_band           ) return "show_band";
        else if(view == show_band.hist_show ) return "hist_show";

        else if(view == bg_view             ) return "bg_view";
        else if(view == fs_button           ) return "fs_button ["+fs_button+"]";
        else if(view == note_tag            ) return "note_tag";
        else if(view == note_text           ) return "note_text";

        else if(view == wvContainer         ) return "wvContainer" ;
        else if(view == fs_webViewL         ) return "fs_webViewL" ;
        else if(view == fs_webViewC         ) return "fs_webViewC" ;
        else if(view == fs_webViewR         ) return "fs_webViewR" ;
        else if(view == fs_webView          ) return "fs_webView"  ;
        else if(view == fs_webView2         ) return "fs_webView2" ;
        else if(view == fs_webView3         ) return "fs_webView3" ;

        else if(view == fs_wtitle           ) return "fs_wtitle"   ;
        else if(view == fs_wtitle2          ) return "fs_wtitle2"  ;
        else if(view == fs_wtitle3          ) return "fs_wtitle3"  ;

        else if(view == fs_browse           ) return "fs_browse"   ;
        else if(view == fs_wswapL           ) return "fs_wswapL"   ;
        else if(view == fs_wswapR           ) return "fs_wswapR"   ;
        else if(view == fs_bookmark         ) return "fs_bookmark" ;
        else if(view == fs_goBack           ) return "fs_goBack"   ;
        else if(view == fs_goForward        ) return "fs_goForward";

        else if(view == get_fg_view()       ) return "fg_view"     ;

        else if( is_sandBox_container(view) ) return "sandBox_container";

        else if(view instanceof NpButton )    return Settings.get_NpButton_name( (NpButton)view );
        else return null; // have to call wvTools first
    }
    //}}}
    // [bg_view] {{{
    // show_tabs_container_freezed {{{
    private void show_tabs_container_freezed(String caller)
    {
        // [bg_view] (BG_VIEW_DIMMED) {{{
        caller += "->show_tabs_container_freezed";
//*GUI*/Settings.MOC(TAG_GUI, caller);
        set_bg_view_color( Settings.BG_VIEW_DIMMED );

        //}}}
        // [tabs_container] (Low Opacity) {{{
        if(tabs_container.getWidth() < (2 * Settings.DISPLAY_W)) // FIXME lower opacity makes it disappear if too big ?...
            tabs_container.setAlpha(0.5F);

        //}}}
    }
    //}}}
    // show_tabs_container_unfreezed {{{
    private void show_tabs_container_unfreezed(String caller)
    {
        // [bg_view] (BackColor) {{{
        caller += "->show_tabs_container_unfreezed";
//*GUI*/Settings.MOC(TAG_GUI, caller);
        int bg_view_color;

        boolean       do_browser = (Settings.TOOL_URL_ACTION.equals(Settings.TOOL_URL_ACTION_BROWSER));
        boolean       do_sendkey = (Settings.TOOL_URL_ACTION.equals(Settings.TOOL_URL_ACTION_SENDKEY));

        boolean connected     = ((this_RTabsClient != null) && this_RTabsClient.isConnected());

        boolean while_connecting                             // SHOW SERVER CONNECTION ATTEMPTS WHEN..
            =  !Settings.FREEZED                             // ...not currently freezed
            && !Settings.OFFLINE                             // ...not currently offline
            && !connected                                 // ...and not connected yet
            && !this_RTabsClient.has_max_connection_failed() // ...while still trying
            ;
//*GUI*/Settings.MOM(TAG_GUI, "........................do_browser ["+ do_browser                                   +"]");
//*GUI*/Settings.MOM(TAG_GUI, "........................do_sendkey ["+ do_sendkey                                   +"]");
//*GUI*/Settings.MOM(TAG_GUI, ".........................connected ["+ connected                                    +"]");
//*GUI*/Settings.MOM(TAG_GUI, ".....................is_ZOOMED_OUT ["+ is_ZOOMED_OUT()                              +"]");
//*GUI*/Settings.MOM(TAG_GUI, "....................dim_TABS_is_ON ["+ dim_TABS_is_ON                               +"]");
//*GUI*/Settings.MOM(TAG_GUI, "..................Settings.FREEZED ["+ Settings.FREEZED                             +"]");
//*GUI*/Settings.MOM(TAG_GUI, "..................Settings.LOGGING ["+ Settings.LOGGING                             +"]");
//*GUI*/Settings.MOM(TAG_GUI, "..................Settings.OFFLINE ["+ Settings.OFFLINE                             +"]");
//*GUI*/Settings.MOM(TAG_GUI, "..................Settings.OPACITY ["+ Settings.OPACITY                             +"]");
//*GUI*/Settings.MOM(TAG_GUI, "..................while_connecting ["+ while_connecting                             +"]");
//*GUI*/Settings.MOM(TAG_GUI, ".......has_max_connection_failed() ["+ this_RTabsClient.has_max_connection_failed() +"]");

        if     ( dim_TABS_is_ON                  ) bg_view_color = Settings.BG_VIEW_TABS_DIMMED      ; // (WEB GROUPING .. tabs dimmed)
        else if( while_connecting                ) bg_view_color = Settings.BG_VIEW_NOT_CONNECTED    ; // (NOT CONNECTED              )
        else if( is_ZOOMED_OUT()                 ) bg_view_color = Settings.BG_VIEW_ZOOMED_OUT       ; // (ZOOMED OUT FULL            )
    //  else if( Settings.LOGGING                ) bg_view_color = Settings.BG_VIEW_LOGGING          ; // (LOGGING IS ON              )
    //  else if( Settings.OPACITY <= 95          ) bg_view_color = Settings.BG_VIEW_OPACITY_BELOW_95 ; // (BG_VIEW_OPACITY_BELOW_95   )
        else if( Settings.OPACITY <= 95          ) bg_view_color = Settings.BG_VIEW_DEFAULT          ;
        else                                       bg_view_color = Settings.BG_VIEW_DEFAULT          ; // (BG_VIEW_DEFAULT            )

        set_bg_view_color( bg_view_color );

        //}}}
        // [bg_view] (BgSymbol) {{{
        String s;

        if     ( Settings.OFFLINE) s = Settings.SYMBOL_offline       ; // ONLINE
        else if( Settings.FREEZED) s = Settings.SYMBOL_SNOW_FLAKE    ; // LOCKED
        else if( connected       ) s = Settings.SYMBOL_LINK_DIAERESIS; // ONLINE
        else                       s = "42"                          ; // OFFLINE (... the question was: what should be displayed ?)

        if     ( do_browser       ) s = Settings.ParseUnicode(Settings.SYMBOL_GLOBE) +"\n"+s;
        else if( do_sendkey       ) s = Settings.SYMBOL_KEYBOARD                     +"\n"+s;
        if     ( Settings.LOGGING ) s = Settings.SYMBOL_LOG                          +"\n"+s;

        switch( activity.getRequestedOrientation() ) {
            case Configuration.ORIENTATION_LANDSCAPE: s = Settings.SYMBOL_O_LANDSCAPE+"\n"+s; break;
            case Configuration.ORIENTATION_PORTRAIT : s = Settings.SYMBOL_O_PORTRAIT +"\n"+s; break;
        }

/*{{{
        String battery_fully_charged_SYMBOL  =    this_RTabsClient.get_battery_fully_charged_SYMBOL();
        if(    battery_fully_charged_SYMBOL != "")            s +="\n"+battery_fully_charged_SYMBOL;
}}}*/

        bg_view.setText( s );
        // }}}
        // [bg_view] (TextColor) {{{
        int text_color;

        if     ( Settings.OFFLINE           ) text_color = Color.RED   | 0x88000000; // ONLINE
        else if( Settings.FREEZED           ) text_color = Color.BLUE  | 0x88000000; // LOCKED
        else if( connected     && do_browser) text_color = Color.RED   | 0xFF000000; // ONLINE WEB BROWSER
        else if( connected     && do_sendkey) text_color = Color.YELLOW| 0xFF000000; // ONLINE KEY SENDINPUT
        else if( connected                  ) text_color = Color.WHITE | 0x44000000; // ONLINE
        else                                  text_color = Color.WHITE | 0x88000000; // OFFLINE

        bg_view.setTextColor( text_color );
        //}}}
        // [bg_view] (Opacity) {{{
        float alpha;

        if     ( Settings.OFFLINE) alpha = 0.8f; // OFFLINE
        else if( Settings.FREEZED) alpha = 0.8f; // LOCKED
        else if( connected       ) alpha = 0.4f; // ONLINE
        else                       alpha = 0.8f; // OFFLINE

        bg_view.setAlpha( alpha );
        //}}}
        // [tabs_container] (Full Opacity) {{{
        tabs_container.setAlpha ( 1f );
        bg_view       .setAlpha ( 1f );
        vsv           .setAlpha ( 1f );
        vsv           .setScaleX( 1f );
        vsv           .setScaleY( 1f );

        // }}}
    }
    //}}}
    // set_bg_view_color {{{
    private  int     bg_view_color;
    private void set_bg_view_color(int color)
    {
//*GUI*/Settings.MOC(TAG_GUI, "set_bg_view_color("+Settings.Get_bg_color_name(color)+")");

        // [bg_view] BACKGROUND
        bg_view_color = color;
        bg_view    .setBackgroundColor( bg_view_color );

        // [frameLayout] BACKGROUND
        if(Settings.OPACITY <= 95)
            frameLayout.setBackgroundColor( Color.BLACK   );
        else
            frameLayout.setBackgroundColor( bg_view_color );

//      int opacity_max = (Settings.OPACITY <= 95) ? 32 : 255;
//      int opacity_255 = (int)(opacity_max * Settings.OPACITY / 100.0);
//      frameLayout.setBackgroundColor(bg_view_color & opacity_255<<24);

//        // [frameLayout] NIGHT-MODE OPACITY {{{
//        if(Settings.OPACITY <= 50) {
//            frameLayout.getBackground().setAlpha( 0 );
//        }
//        else {
//            frameLayout.getBackground().setAlpha( opacity_255 );
//        }
//        // }}}

        // ADJUST CONTAINERS BACKGROUND TO BRIHT OR DARK THEME
        Settings.BG_ALPHA
            = (ColorPalette.GetBrightness( bg_view_color ) >= 224)
            ? Settings.BG_ALPHA_BRIGHT
            : Settings.BG_ALPHA_DARK
            ;

        show_band.adjustBackgroundColors();

        dck_handle.setBackgroundColor   (Settings.DCK_HANDLE_COLOR & Settings.BG_ALPHA);
    }
    //}}}
    // show_GUI_rescaling {{{
    private void show_GUI_rescaling(boolean zoomed_out, boolean within_SOFT_SCALE)
    {
        int new_bg_view_color;

        if( is_view_showing(cart_band) ) {
            new_bg_view_color = Settings.BG_VIEW_RESCALING;
        }
        else {
            if( zoomed_out ) {
                new_bg_view_color = Settings.BG_VIEW_ZOOMED_OUT;
            }
            else if( within_SOFT_SCALE ) {
                if(Settings.OPACITY > 95) new_bg_view_color = Settings.BG_VIEW_DEFAULT;
                else                      new_bg_view_color = Settings.BG_VIEW_OPACITY_BELOW_95;
            }
            else {
                new_bg_view_color = Settings.BG_VIEW_RESCALING;
            }
        }

        if(new_bg_view_color != bg_view_color)
            set_bg_view_color( new_bg_view_color );
    }
    // }}}
    //}}}

    // [fg_rect] {{{
    // {{{
    private static final int         BLINK_DURATION = 200;
    private                       Rect      fg_rect = null;
    private                       View view_to_mask = null; // NOTE: can be fg_view itself (prepared offscreen over some area-to-blink)
    private                        int masked_color = Color.GREEN;
    // }}}
    // blink_crosshair {{{
    private void blink_crosshair(int x, int y)
    {
        hr.   top = y - 2;
        hr.  left = x - Settings.TAB_MARK_H/2;
        hr. right = x + Settings.TAB_MARK_H/2;
        hr.bottom = y + 2;
        blink_rect(hr, (Color.BLUE ) & 0xFFFFFFFF,   0, 0); // delay, duration

        hr.   top = y - Settings.TAB_MARK_H/2;
        hr.  left = x - 2;
        hr. right = x + 2;
        hr.bottom = y + Settings.TAB_MARK_H/2;
        handler.re_postDelayed(hr_blink_crosshair, 150); // start
    }

    private final Runnable hr_blink_crosshair = new Runnable() {
        @Override public void run() {
            blink_rect(hr, (Color.RED  ) & 0xFFFFFFFF, 0, 150); // delay, duration
        }
    };
    //}}}
    // blink_rect {{{
    public  void blink_rect_hide()
    {
        if(_fg_view == null             ) return;
        if(_fg_view.getChildCount() == 0) handler.re_post( hr_hide_fg_view );
    //  else Settings.MOM(TAG_GUI, "blink_rect_hide: called with an fg_view containing "+ _fg_view.getChildCount() +" children"); // todo: should not mix rect and view
    }
    public  void blink_rect(Rect rect, int blink_color                                        ) { blink_rect(rect, blink_color,     0, BLINK_DURATION); }
    private void blink_rect(Rect rect, int blink_color, int delay                             ) { blink_rect(rect, blink_color, delay, BLINK_DURATION); }

    public  void blink_rect(Rect rect, int blink_color, int delay, int duration, String caller) {
        caller += "->blink_rect";
//*GUI*/Settings.MOC(TAG_GUI, caller);
        blink_rect(rect, blink_color, delay, duration);
    }

    public  void blink_rect(Rect rect, int blink_color, int delay, int duration)
    {
        show_hide(rect, blink_color, delay, duration);  // start highlight
    }
    private void show_hide(Rect rect, int blink_color, int delay, int duration)
    {
        // prepare [_fg_view] to highlight the given rect
        if(_fg_view == null) create_fg_view();

        // setup the next highlight area .. f(rect) .. f(delay) {{{
        if(delay > 0) {
            fg_rect        = new Rect( rect ); // deferred layout .. (let current highlight layout alone)
        }
        else {
            fg_rect        = null; // immediate layout .. (override current highlight)
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)_fg_view.getLayoutParams();
            mlp.leftMargin = rect.left;
            mlp.topMargin  = rect.top ;
            mlp.width      = rect.right  - rect.left;
            mlp.height     = rect.bottom - rect.top;
            _fg_view.setLayoutParams( mlp );
        }
        //}}}
        // get the specified highlight color
        masked_color = blink_color;

        // schedule show-hide f(delay) and (duration)
        handler.re_postDelayed(     hr_show_fg_view, delay           ); // start
        if(duration > 0)
            handler.re_postDelayed( hr_hide_fg_view, delay + duration); // stop
    }
    //}}}
    //}}}
    // [fg_view] {{{
    //{{{
    private                  RelativeLayout _fg_view = null;

    //}}}
    // get_fg_view {{{
    private View get_fg_view()
    {
        return _fg_view;
    }
    //}}}
    // is_fg_view_showing {{{
    private boolean is_fg_view_showing()
    {
        return is_view_showing( _fg_view );
    }
    //}}}
    // blink_view .. (rect from view_to_mask) {{{
    public  void blink_view(View view, int blink_color                         ) { blink_view(view, blink_color,     0, BLINK_DURATION); }
    public  void blink_view(View view, int blink_color, int delay              ) { blink_view(view, blink_color, delay, BLINK_DURATION); }
    public  void blink_view(View view, int blink_color, int delay, int duration)
    {
        if(view_to_mask != null)       hide_fg_view("blink_view");                          // release current view_to_mask
        view_to_mask     = view;       if(view_to_mask == null)         return; // pick new view_to_mask
        Rect        rect = new Rect(); view_to_mask.getGlobalVisibleRect(rect); // adjust [_fg_view layout] from [view_to_mask layout]
        show_hide(rect, blink_color, delay, duration);                          // start highlight
    }
    //}}}
    // show_fg_view {{{
    private final Runnable hr_show_fg_view = new Runnable() { @Override public void run() { show_fg_view(); } };
    private void show_fg_view()
    {
        // release any current [view_to_mask] {{{
        String    caller = "show_fg_view";

        if(view_to_mask != null)
            hide_fg_view("show_fg_view");

        //}}}
        // [_fg_view] hide and init offscreen {{{
        if(_fg_view == null) create_fg_view();
        else                _fg_view.removeAllViews();

        //}}}
        // [_fg_view] LAYOUT .. f(view_to_mask) {{{
        if(( view_to_mask != null) && (view_to_mask != _fg_view)) // unless already prepared by [blink_rect]
        {
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)_fg_view.getLayoutParams();
            int[]       xy = new int[2]; view_to_mask.getLocationOnScreen( xy );
            mlp.leftMargin = xy[0];
            mlp.topMargin  = xy[1];
            mlp.width      = view_to_mask.getWidth();
            mlp.height     = view_to_mask.getHeight();
//*GUI*/Settings.MOM(TAG_GUI, caller+ String.format(": [%4d %4d] [%4d %4d]", mlp.leftMargin, mlp.topMargin, mlp.width, mlp.height));
            _fg_view.setLayoutParams(mlp);
        }
        // }}}
        // [_fg_view] LAYOUT .. f(fg_rect) {{{
        else if(  fg_rect != null) {
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)_fg_view.getLayoutParams();
            mlp.leftMargin = fg_rect.left;
            mlp.topMargin  = fg_rect.top ;
            mlp.width      = fg_rect.right  - fg_rect.left;
            mlp.height     = fg_rect.bottom - fg_rect.top;
//*GUI*/Settings.MOM(TAG_GUI, "blink_rect: "+String.format(": [%4d %4d] [%4d %4d]", mlp.leftMargin, mlp.topMargin, mlp.width, mlp.height));
            _fg_view.setLayoutParams(mlp);
        }
        // }}}
        // [_fg_view] color {{{
        _fg_view.setBackgroundColor( masked_color );

        // }}}
        // [_fg_view] elevation  .. (unless already prepared by [blink_rect]) {{{
        //if((view_to_mask != null) && (view_to_mask != _fg_view)) {
        //    float elevation = 1 + ViewCompat.getElevation(view_to_mask);
        //    ViewCompat.setElevation(_fg_view, elevation);
        //}
        ViewCompat.setElevation(_fg_view, Settings.MAX_ELEVATION);
        // }}}
        // [_fg_view] show {{{
        if(_fg_view.getParent() == null)
            frameLayout.addView( _fg_view );
        _fg_view.setVisibility( View.VISIBLE );
        // }}}
    }
    //}}}
    // hide_fg_view {{{
    private final Runnable hr_hide_fg_view   = new Runnable() { @Override public void run() { hide_fg_view("hr_hide_fg_view"); } };
    private void hide_fg_view(String caller)
    {
        caller += "->hide_fg_view";
//GUI//Settings.MOC(TAG_GUI, caller);

        // release [view_to_mask] {{{
        if(view_to_mask != null)
        {
//*GUI*/Settings.MOM(TAG_GUI, caller+": releasing view_to_mask=["+ get_view_name(view_to_mask) +"]");
            if(view_to_mask == tabs_container)
                tabs_container.setAlpha(1f); // restore a masked tabs_container opacity

            view_to_mask = null; // dereference previously masked view
        }
        // }}}
        // hide [_fg_view] {{{
        if( is_view_showing(_fg_view) )
        {
//*GUI*/Settings.MOM(TAG_GUI, caller+": hiding [_fg_view]");
            _fg_view.setVisibility( View.GONE );
            frameLayout.removeView( _fg_view );
          //sync_tabs_scrolling(caller);
          //set_tabs_scrolling(false, caller);
        }
        // }}}
    }
    //}}}
    // get_empty_fg_view {{{
    private ViewGroup get_empty_fg_view()
    {
        if(_fg_view == null) create_fg_view();
        else                _fg_view.removeAllViews();
_fg_view.setBackgroundColor(Settings.HIST_COLOR & 0xDD111111); // XXX THEMED DARKENED
        return _fg_view;
    }
//}}}
    // splash_screen_fg_view {{{
    private void splash_screen_fg_view()
    {
        // BAND HIDE {{{
        String caller = "splash_screen_fg_view";
        drag_band_hide( caller );

        //}}}
        // _fg_view geometry .. f(xy_wh) {{{
        frameLayout.removeView( _fg_view );
        _fg_view.setVisibility( View.GONE );

        ViewGroup.MarginLayoutParams vlp = (ViewGroup.MarginLayoutParams)_fg_view.getLayoutParams();

        vlp.leftMargin = 0;//Handle.Get_DOCK_STANDBY_WIDTH();
        vlp.topMargin  = 0;
        vlp.width      = Settings.DISPLAY_W - vlp.leftMargin;
        vlp.height     = Settings.DISPLAY_H - vlp.topMargin ;

//*GUI*/Settings.MON(TAG_GUI, caller , String.format("[%4d %4d] [%4d %4d]", vlp.leftMargin, vlp.topMargin, vlp.width, vlp.height));
        // }}}
       // TODO populate _fg_view with NpButton instances
        // _fg_view show {{{
        _fg_view.setVisibility( View.VISIBLE );
        frameLayout.addView( _fg_view );

        // }}}
    }
    //}}}
    // create_fg_view {{{
    private void create_fg_view()
    {
        if(_fg_view == null)
        {
        //  _fg_view = new View( activity );
            _fg_view = new RelativeLayout( activity ); // so that we can layout some transcient children (i.e. to navigate profile-history buttons)
            _fg_view.setLayoutParams( new RelativeLayout.LayoutParams(1,1) );
            ViewCompat.setElevation(_fg_view, Settings.MAX_ELEVATION);

            _fg_view.setOnTouchListener( fg_view_OnTouchListener );
            //see MotionListener.onTouchEvent // EVENT MASKED
        }
    }
    //}}}
    // fg_view_OnTouchListener {{{
    private final View.OnTouchListener fg_view_OnTouchListener = new View.OnTouchListener() {
        @Override public boolean onTouch(View view, MotionEvent event)
        {
            String caller   = "fg_view_OnTouchListener.onTouch";
            int    action   = event.getActionMasked();

            if(    action  == MotionEvent.ACTION_UP  )
            {
//*EV1_RT_IN*/Settings.MON(TAG_EV1_RT_IN, caller, "ACTION_UP: hide_fg_view .. sync_tabs_scrolling");

                hide_fg_view       (     caller );
                sync_tabs_scrolling(     caller );
            }
            else if(action == MotionEvent.ACTION_CANCEL  )
            {
//*EV1_RT_IN*/Settings.MON(TAG_EV1_RT_IN, caller, "ACTION_CANCEL");

            }

            return true; // keep-on handling this event chain
        }
    };
    //}}}
    //}}}

    // [PULSE] & [RAISE] {{{
    // {{{
    private final LinkedHashMap<Object, Boolean> pulsed_button_queue = new LinkedHashMap<>();
    private boolean                              push_or_raise       = true;
    private NpButton                             button_to_press     = null;
    private NpButton                             button_to_release   = null;

    // }}}
    // play_press_sound_for_np {{{
    private boolean play_press_sound_for_np(NotePane np, String caller)
    {
        caller += "->play_press_sound_for_np";
//GUI//Settings.MOC(TAG_GUI, caller);
        if( np == null               ) return false;
        if( np.is_a_PROFILE_button() ) return false;

        play_sound_ding(caller);
        return  true;
    }
    //}}}
    // play_press_sound_for_nb {{{
    private boolean play_press_sound_for_nb(NpButton button, String caller)
    {
        if(button == null) return false;

        if( wvTools.is_a_silent_button( button ) ) return false;

        play_sound_click(caller);
        return true;
    }
    //}}}
    // BUTTON PRESS & RAISE {{{
    public  void pulse_np_button (NpButton button) { _press_np_button(null,    button, true ); }
    public  void raise_np_button (NpButton button) { _press_np_button(null,    button, false); }
    public  void pulse_np        (NotePane np    ) { _press_np_button(  np, np.button, true ); }
    public  void raise_np        (NotePane np    ) { _press_np_button(  np, np.button, false); }
    private void _press_np_button(NotePane np, NpButton button, boolean push_or_raise)
    {
        String caller = "_press_np_button(np=["+np+"], button=["+button+"])";
//*GUI*/Settings.MOC(TAG_GUI, caller);

        // play sound
        if( !wvTools.is_a_silent_button( button        ) ) {
            if(!play_press_sound_for_np(     np, caller) )
                play_press_sound_for_nb( button, caller);
        }

        // add to queue
        pulsed_button_queue.put(button, push_or_raise);

        // initiate animation chain when none in progress
        if(button_to_press == null)
            pulse_next_button();
    }
    //}}}
    // pulse_next_button {{{
    private void pulse_next_button()
    {
        String caller = "pulse_next_button";

        if( pulsed_button_queue.isEmpty() ) {
            button_to_press = null;
        }
        else {
            try {
                for(Map.Entry<Object, Boolean> entry : pulsed_button_queue.entrySet()) {
                    // extract
                    button_to_press = (NpButton)entry.getKey  ();
                    push_or_raise   = entry.getValue();
                    pulsed_button_queue.remove(button_to_press);

                    String save_from_caller = button_to_press.get_save_from_caller();
                    if( TextUtils.isEmpty(save_from_caller) )
                    {
//*GUI*/Settings.MON(TAG_GUI, "press_button", "calling "+ button_to_press.toString()+".save_from("+caller+") .. (yet not called)");
                        button_to_press.save_from(caller);
                    }

                    press_button();
                }
            } catch(Exception ex) { Settings.MON(TAG_GUI, "pulsed_button_queue", "Exception:\n"+ex); }
        }
    }
    //}}}
    // press_button {{{
    private void press_button()
    {
        if(button_to_press == null) return;

        // ANIMATION THEN HANDLE HIDE {{{
        if( ANIM_SUPPORTED ) {
            // highlight buton text shadow
            button_to_press.press();

            AnimatorSet set = new AnimatorSet();

            set.setDuration( PULSE_FORE_DURATION );

            float scale = button_to_press.getScaleX() * (push_or_raise ? 0.9f : 1.1f);
//// XXX {{{
//if((button_to_press == wvTools.sb) || (button_to_press == wvTools.sb2) || (button_to_press == wvTools.sb3))
//{
//    scaleX = push_or_raise ? 0.9f : get_wvTools.sb_expanded_SCALE_X( button_to_press );
//    scaleY =                        1f; // same height
//}
//// XXX }}}

            set.setInterpolator(new DecelerateInterpolator());

            AnimatorSet.Builder builder =
            set
                .play(ObjectAnimator.ofFloat(button_to_press, View.SCALE_X, scale))
                .with(ObjectAnimator.ofFloat(button_to_press, View.SCALE_Y, scale))
            //  .with(ObjectAnimator.ofFloat(button_to_press, View.ALPHA  , alpha))
                ;
//// XXX {{{
//if((button_to_press == wvTools.sb) || (button_to_press == wvTools.sb2) || (button_to_press == wvTools.sb3))
//{
//    float transX = get_wvTools.sb_expanded_TRANSLATION_X( button_to_press ); // to the left
//builder.with(ObjectAnimator.ofFloat(button_to_press, View.TRANSLATION_X, transX));
//}
//// XXX }}}
            set.start();

            // button activation
            handler.re_postDelayed(hr_release_button, PULSE_HOLD_DURATION);
        }
        //}}}
        // OR INSTANT HANDLE HIDE {{{
        //else {
            //if     (button_to_press.getParent() == top_handle         ) handle_hide("button_to_press on top_handle");
            //else if(button_to_press.getParent() == dock_band.container) handle_hide("button_to_press on dock_band.container");
        //}
        //}}}
    }
    //}}}
    // hr_release_button {{{
    private final Runnable hr_release_button = new Runnable() {
        @Override
        public void run() {
            // overlapping animation chain
            button_to_release = button_to_press;
            release_button   (); // concurrent animation
            pulse_next_button(); // concurrent animation
        }
    };

    //}}}
    // release_button {{{
    private void release_button()
    {
        if(button_to_release == null) return;

        // ANIMATION THEN HANDLE HIDE {{{
        if( ANIM_SUPPORTED ) {
            // restore buton text shadow
            button_to_release.release();

            AnimatorSet set = new AnimatorSet();

            set.setDuration( PULSE_BACK_DURATION );

            String save_from_caller = button_to_release.get_save_from_caller();
//*GUI*/Settings.MON(TAG_GUI, "release_button", button_to_release.toString()+": save_from_caller=["+save_from_caller+"]");
            float scale
                =  TextUtils.isEmpty(save_from_caller)
                ?   1f
                :   button_to_release.get_s_from();

            set.setInterpolator(new DecelerateInterpolator());

            AnimatorSet.Builder builder =
            set
                .play(ObjectAnimator.ofFloat(button_to_release, View.SCALE_X, scale))
                .with(ObjectAnimator.ofFloat(button_to_release, View.SCALE_Y, scale))
            //  .with(ObjectAnimator.ofFloat(button_to_release, View.ALPHA  , alpha))
                ;
//// XXX {{{
//if((button_to_press == wvTools.sb) || (button_to_press == wvTools.sb2) || (button_to_press == wvTools.sb3))
//{
//    float transX = 0; // back from the left
//builder.with(ObjectAnimator.ofFloat(button_to_release, View.TRANSLATION_X, transX));
//}
//// XXX }}}
            set.start();

            // button activation done
        //  if(button_to_release.getParent() == top_handle) handle_hide("button_to_release on top_handle (reverse)");
        //  if(button_to_release.getParent() == dock_band.container) handle_hide("button_to_release on dock_band.container (reverse)");

            button_to_release = null; // cycle complete
            // non-overlapping animation chain
        //  handler.re_postDelayed(hr_pulse_next_button, PULSE_BACK_DURATION);
        }
        //}}}
    }
    //}}}
/*
    // hr_pulse_next_button {{{
    private final Runnable hr_pulse_next_button = new Runnable() {
        @Override
        public void run() {
            // proceed with animation chain
            pulse_next_button();
        }
    };
    //}}}
*/
    //}}}
    // [SOUND_CLICK] [SOUND_DING] [SOUND_ALARM]{{{
    /* play_sound_click {{{*/
    public  void play_sound_click(String caller)
    {
        caller += "->play_sound_click";
//*GUI*/Settings.MOC(TAG_GUI, caller);
        handler.re_postDelayed(hr_play_sound_click, 0);
    }
/*}}}*/
    /* play_sound_ding {{{*/
    public  void play_sound_ding (String caller)
    {
        caller += "->play_sound_ding";
//*GUI*/Settings.MOC(TAG_GUI, caller);
        handler.re_postDelayed(hr_play_sound_ding , 0);
    }
/*}}}*/
    /* play_sound_alarm {{{*/
    /*{{{*/
    private static final int    ALARM_BIRST_LENGTH          =  5;
    private static final int    ALARM_REMINDER_MOD          = 30;

    private static       int    alarm_level_last_sent_count =  0;
    private static       String last_battery_alarm          = "";
    /*}}}*/
    public  void play_sound_alarm(String caller, String battery_alarm)
    {
        // NEW [battery_alarm] .. (or same as the last received)  {{{
        if( last_battery_alarm.equals( battery_alarm ) )
        {
            alarm_level_last_sent_count += 1;
        }
        else {
            last_battery_alarm           = battery_alarm;
            alarm_level_last_sent_count  = 1;
//*POLL*/Settings.MOC(TAG_POLL, ".....play_sound_alarm: caller=["+caller+"] .. battery_alarm=["+battery_alarm+"]");
        }
        /*}}}*/
        // CLEARED ALARM SOUND {{{
        if(last_battery_alarm == "")
        {
            if(alarm_level_last_sent_count == 1)
                SoundPoolManager.play(Settings.SOUND_ALARM_0, Settings.SOUND_ALARM_VOLUME);
        }
        //}}}
        // NEW ALARM BIRST AND REMINDER {{{
        else if( (alarm_level_last_sent_count < ALARM_BIRST_LENGTH)
           ||   ((alarm_level_last_sent_count % ALARM_REMINDER_MOD) == 0)
          ) {
            switch( last_battery_alarm )
            {
                case "99%": SoundPoolManager.play(Settings.SOUND_ALARM_1, Settings.SOUND_ALARM_VOLUME); break;
                case "98%": SoundPoolManager.play(Settings.SOUND_ALARM_2, Settings.SOUND_ALARM_VOLUME); break;
                case "97%": SoundPoolManager.play(Settings.SOUND_ALARM_3, Settings.SOUND_ALARM_VOLUME); break;
                case "96%": SoundPoolManager.play(Settings.SOUND_ALARM_4, Settings.SOUND_ALARM_VOLUME); break;
                case "95%": /* fall through */
                default   : SoundPoolManager.play(Settings.SOUND_ALARM_5, Settings.SOUND_ALARM_VOLUME); break;
            }
        }
        //}}}
    }
    /*}}}*/
    //}}}
    // [GLOW] {{{
    //{{{
    private static final                long GLOW_DURATION            =   10;
    private static final                long GLOW_NEXT_INTERVAL       =   10;
    private static final                long GLOW_CYCLE_INTERVAL      = 1000;

    private static                AnimatorSet Glow_button_AnimatorSet = null;
    private static final LinkedHashMap<NpButton, Integer> glow_button_queue = new LinkedHashMap<>();
    private static                         NpButton button_to_glow    = null;
    private static                          boolean glow_loop_enabled = false;

    // }}}

    // dim_TABS {{{
    private boolean dim_TABS_is_ON = false;
    private void dim_TABS(String caller)
    {
        caller += "->dim_TABS";
//*GLOW*/Settings.MOC(TAG_GLOW, caller);

        this_RTabsClient.dim_TABS( RTabsClient.TABS_Map );

        dim_TABS_is_ON = true;
        show_tabs_container_unfreezed(caller);
    }
    //}}}
    // undim_TABS {{{
    private void undim_TABS(String caller)
    {
        caller += "->undim_TABS";
//*GLOW*/Settings.MOC(TAG_GLOW, caller);

        this_RTabsClient.undim_TABS( RTabsClient.TABS_Map );

        dim_TABS_is_ON = false;
        show_tabs_container_unfreezed(caller);
    }
    //}}}
    // stop_GLOWING {{{
    public void stop_GLOWING(String caller)
    {
        caller += "->stop_GLOWING";
//*GLOW*/Settings.MOC(TAG_GLOW, caller);

        glow_button_disable("stop_GLOWING");
    }
    //}}}

    // glow_button {{{
    public void glow_button(NpButton np_button, String caller)
    {
        caller += "->glow_button("+ get_view_name(np_button) +")";
//*GLOW*/Settings.MOC(TAG_GLOW, caller);


        // ALREADY GLOWING BUTTON .. NULL EFFECT {{{
        if(glow_loop_enabled && glow_button_queue.containsKey(np_button))
        {
//*GLOW*/Settings.MOM(TAG_GLOW, "...ALREADY IN GLOWING QUEUE");
            return;
        }
        //}}}

        // INTERRUPT CURRENT ANIMATION
        if(Glow_button_AnimatorSet != null) {
            glow_button_interrupt(caller);
        }
        else {
//*GLOW*/Settings.MOM(TAG_GLOW, "...GLOWING LOOP NOT STARTED YET");
        }

        // START GLOWING {{{
        if(!glow_loop_enabled )
            glow_loop_enabled = true;

        //}}}
        if( !glow_button_queue.containsKey(np_button) )
        {

            // DIM OTHER BUTTONS .. f(queueing first glowing button)
            if(glow_button_queue.size() == 0)
                dim_TABS(caller);

            // SAVE GLOWING BUTTON BACKGROUND COLOR PROPERTY TO RESTORE IT WHEN DONE
            np_button.setOpacity( Settings.Get_OPACITY() );
            np_button.pushBackgroundColor();

            // GET GLOWING BUTTON BACKGROUND COLOR PROPERTY
            int bg_color = np_button.instance_bg_color;
            glow_button_queue.put(np_button, bg_color);

        }
        // RESUME GLOWING THE BUTTONS QUEUE
        if(Glow_button_AnimatorSet == null) {
            handler.re_postDelayed( hr_glow_first_button, GLOW_NEXT_INTERVAL); // short delay ... but enough to queue a WEBGROUP
        }
    }
    //}}}
    // glow_button_remove {{{
    public void glow_button_remove(NpButton nb, String caller)
    {
        caller += "->glow_button_remove("+ get_view_name(nb) +")";
//*GLOW*/Settings.MOC(TAG_GLOW, caller);

        // NULL EFFECT {{{
        if( !glow_button_queue.containsKey(nb) )
        {
//*GLOW*/Settings.MOM(TAG_GLOW, "...IS NOT IN GLOWING QUEUE");
            return;
        }
        //}}}

        // INTERRUPT CURRENT ANIMATION
        glow_button_interrupt(caller);

        // SET NOT-GLOWING-ANYMORE-DIM STATE
        nb.setOpacity( Settings.Get_OPACITY_DIMMED() );
    //  nb.setOpacity( Settings.Get_OPACITY       () );

        // REMOVE THIS BUTTON FROM THE QUEUE
        glow_button_queue.remove( nb );

        // UNDIM f(NO GLOWING BUTTON)
        if(glow_button_queue.size() == 0)
            undim_TABS(caller);

        // RESUME GLOWING THE BUTTONS QUEUE
        if(button_to_glow == null)
            glow_next_button(caller);
    }
    //}}}
    // glow_button_interrupt {{{
    private void glow_button_interrupt(String caller)
    {
        caller += "->glow_button_interrupt";
//*GLOW*/Settings.MOC(TAG_GLOW, caller);

        handler.removeCallbacks( hr_glow_next_button  );
        handler.removeCallbacks( hr_glow_first_button );

        if(Glow_button_AnimatorSet != null)
        {
//*GLOW*/Settings.MOM(TAG_GLOW, "...INTERRUPTING GLOWING LOOP");
            Glow_button_AnimatorSet.cancel();
            Glow_button_AnimatorSet = null;
        }

        button_to_glow = null;

        // RESTORE BUTTONS SAVED PROPERTY
        glow_button_restore(caller);
    }
        //}}}
    // glow_button_restore {{{
    private void glow_button_restore(String caller)
    {
        if(glow_button_queue.size() < 1) return;

        caller += "->glow_button_restore";
//*GLOW*/Settings.MOC(TAG_GLOW, caller);

        int opacity
            = glow_loop_enabled
            ?  Settings.Get_OPACITY_DIMMED()
            :  Settings.Get_OPACITY();

        // RESTORE GLOWING BUTTON ANIMATED PROPERTY
        for(Map.Entry<NpButton, Integer> entry : glow_button_queue.entrySet())
        {
            NpButton  nb = entry.getKey();
        //  int bg_color = glow_button_queue.get( nb );
        //  nb.setBackgroundColor( bg_color );
            nb.popBackgroundColor();
            nb.setOpacity( opacity );
        }
    }
    //}}}
    // glow_button_disable {{{
    private void glow_button_disable(String caller)
    {
        caller += "->glow_button_disable";
//*GLOW*/Settings.MOC(TAG_GLOW, caller);

        // STOP GLOWING
        if( glow_loop_enabled )
        {
            glow_loop_enabled = false;  // ...will interrupt loop in glow_next_button
            undim_TABS(caller);
        }

    }
    //}}}
    // get_button_to_glow_after {{{
    private NpButton get_button_to_glow_after(NpButton prev_nb)
    {
        for(Map.Entry<NpButton, Integer> entry : glow_button_queue.entrySet())
        {
            if(prev_nb == null) {
                return entry.getKey();
            }
            else {
                NpButton nb = entry.getKey();
                if(nb == prev_nb)
                    prev_nb = null; // ...return the next entry
            }
        }
        return null;
    }
    //}}}
    // glow_next_button {{{
    private void glow_next_button(String caller)
    {
        caller += "->glow_next_button";
//GLOW//Settings.MON(TAG_GLOW, caller);
        // glow loop disabled {{{
        if( !glow_loop_enabled ) // ...loop interrupted by glow_button_disable
        {
            // RESTORE BUTTONS SAVED PROPERTY
            glow_button_restore(caller);

            glow_button_queue.clear();

            button_to_glow = null;
//GLOW//Settings.MON(TAG_GLOW, "...glow_loop_enabled is OFF");
            return;
        }
        //}}}
        // no more np_button to glow {{{
        if( glow_button_queue.isEmpty() )
        {
            button_to_glow = null;
//GLOW//Settings.MON(TAG_GLOW, "...glow_button_queue is Empty");
            return;
        }
//GLOW//Settings.MON(TAG_GLOW, "...glow_button_queue size=["+ glow_button_queue.size() +"]");
        //}}}
        // 1/2 GET NEXT BUTTON {{{
        button_to_glow = get_button_to_glow_after(button_to_glow);

        // }}}
        // 2/2 ...IF NONE, LOOP BACK TO FIRST {{{
        if((button_to_glow == null) && glow_loop_enabled)
        {
            // restart from the beginning a bit later
            handler.re_postDelayed( hr_glow_first_button, GLOW_CYCLE_INTERVAL);
            return;
        }

        // }}}
        // ANIMATE NEXT BUTTON QUEUE ENTRY {{{
        if(button_to_glow != null)
        {
//GLOW//Settings.MON(TAG_GLOW, "...next button to glow: ["+ button_to_glow.toString() +"]");
            glow_button_to_glow();
        }
        // }}}
        // CLEAR DISABLED BUTTON QUEUE {{{
        else {
            if( !glow_loop_enabled )
            {
                // RESTORE BUTTONS SAVED PROPERTY
                glow_button_restore(caller);
                glow_button_queue.clear();
            }
        }
        // }}}
    }
    // hr_glow_first_button {{{
    private final Runnable hr_glow_first_button = new Runnable() {
        @Override public void run() {
            glow_next_button("hr_glow_first_button");
        }
    };
    // }}}
    //}}}
    // glow_button_queue_is_empty {{{
    private boolean glow_button_queue_is_empty()
    {
        return glow_button_queue.isEmpty();
    }
    //}}}

    // glow_button_to_glow {{{
    private void glow_button_to_glow()
    {
//GLOW//Settings.MON(TAG_GLOW, "glow_button_to_glow", button_to_glow.toString());
        if( ANIM_SUPPORTED ) // {{{
        {                                                          // ...SIMPLY IGNORE UNSUPORTED ANIM
            NpButton    glow_nb = button_to_glow;                                       // GLOWING BUTTON
            int      glow_color = button_to_glow.instance_bg_color;
            int      brightness = ColorPalette.GetBrightness( glow_color );             // PROGRESS START VALUE
            int           surge = (brightness < 192) ? 255 : brightness/2;

            Glow_button_AnimatorSet = new AnimatorSet();                                // ANIM
            Glow_button_AnimatorSet.setDuration( GLOW_DURATION );                       // DURATION
            Glow_button_AnimatorSet.setInterpolator( new DecelerateInterpolator() );    // INTERPOLATOR
            Glow_button_AnimatorSet.playSequentially(
                    /*forward*/  ObjectAnimator.ofInt(glow_nb, Property_BRIGHTNESS,      surge)   // highlight
                    /*reverse*/, ObjectAnimator.ofInt(glow_nb, Property_BRIGHTNESS, brightness)); // back to current
            Glow_button_AnimatorSet.addListener( glow_button_listener );                // POST PROCESSING
            Glow_button_AnimatorSet.start();                                            // START ANIM
        }
        // }}}
        // TODO: SOMETHING WHEN !ANIM_SUPPORTED
    }
    //}}}
    // glow_button_listener .. (onAnimationCancel) (onAnimationEnd) {{{
    private final AnimatorListenerAdapter glow_button_listener = new AnimatorListenerAdapter()
    {
        // onAnimationCancel {{{
        @Override public void onAnimationCancel(Animator animation)
        {
            String caller = "onAnimationCancel";
//GLOW//Settings.MON(TAG_GLOW, caller);

            if(Glow_button_AnimatorSet != null)
                Glow_button_AnimatorSet = null;
        }
        //}}}
        // onAnimationEnd {{{
        @Override public void onAnimationEnd(Animator animation) {
            String caller = "glow_button_listener.onAnimationEnd";
//GLOW//Settings.MON(TAG_GLOW, caller);

            if(Glow_button_AnimatorSet != null) {
                glow_button_after("onAnimationEnd");
                handler.re_postDelayed( hr_glow_next_button, GLOW_NEXT_INTERVAL);
            }
        }
         //}}}
    };
    // hr_glow_next_button {{{
    private final Runnable hr_glow_next_button = new Runnable() {
        @Override public void run() {
            glow_next_button("hr_glow_next_button");
        }
    };
    // }}}
    // }}}
    // glow_button_after {{{
    private void glow_button_after(String caller)
    {
        caller += "->glow_button_after";
//GLOW//Settings.MON(TAG_GLOW, caller);

        Glow_button_AnimatorSet  = null;

        if( !glow_loop_enabled ) return; // as soon as disabled ... do not touch change anything

        if((button_to_glow != null) && glow_button_queue.containsKey(button_to_glow))
        {
            // RESTORE GLOWING BUTTON ANIMATED PROPERTY
        //  int bg_color = glow_button_queue.get( button_to_glow );
        //  button_to_glow.setBackgroundColor   ( bg_color       );
            button_to_glow.popBackgroundColor();
        }
    }
    //}}}
    // Property_BRIGHTNESS {{{
    private final Property<NpButton, Integer> Property_BRIGHTNESS =
        new Property<NpButton, Integer>(Integer.class, "viewScrollY")
        {
            @Override public void    set(NpButton np_button, Integer brightness)
            {
//GLOW//Settings.MON(TAG_GLOW, "Property.set(brightness="+brightness+")");
                if( !glow_loop_enabled ) return; // as soon as disabled ... do not touch change anything
                int bg_color  = np_button.instance_bg_color;
                int new_color = ColorPalette.GetColorLightnessTo(bg_color, brightness); // PROGRESS ANIMATION VALUE
                np_button.setBackgroundColor( new_color );
            }

            @Override public Integer get(NpButton np_button)
            {
                int bg_color   = np_button.instance_bg_color;
                int brightness = ColorPalette.GetBrightness( bg_color );
//GLOW//Settings.MON(TAG_GLOW, "Property.get: ...return brightness=["+brightness+"]");
                return brightness;
            }

        };
    // }}}
    // }}}
    // VISIBILITY {{{
    // SHOWING (NOT NULL) (VISIBLE) (AT_XY) {{{
    // is_view_showing {{{
    public boolean is_view_showing(View view)
    {
        return (view != null) && (view.getVisibility() == View.VISIBLE);
    }
//}}}
    // is_view_atXY {{{
    public  boolean is_view_atXY(View view, int x, int y)
    {
        if( !is_view_showing(view) ) return false;
        Rect r = new Rect();
        view.getHitRect( r );
        return r.contains(x, y);
    }
//}}}
    //}}}
    // DIALOG SHOWING {{{
    private boolean is_dialog_showing(Dialog dialog) { return (dialog != null) && dialog.isShowing(); }

    // }}}
    // HANDLE (SHOWING) (CURRENT) {{{
    private boolean is_lft_handle_current         () { return (Handle.Get_cur_handle() == lft_handle); }
    private boolean is_mid_handle_current         () { return (Handle.Get_cur_handle() == mid_handle); }
    private boolean is_top_handle_current         () { return (Handle.Get_cur_handle() == top_handle); }
    private boolean is_bot_handle_current         () { return (Handle.Get_cur_handle() == bot_handle); }

    private boolean is_handle_showing(Handle handle) { return (Handle.Get_cur_handle() == handle) && (handle.getVisibility() == View.VISIBLE); }

    //}}}
    // WEBVIEW (SHOWING) (SPLIT) (EXPANDED) {{{
    public  boolean is_fs_webView_expanded        () { return fs_webView_expanded; }

    private boolean is_fs_webView1_in_screen      () { return (fs_webView  != null)                               ; }
    private boolean is_fs_webView2_in_screen      () { return (fs_webView2 != null) && (Settings.SCREEN_SPLIT > 1); }
    private boolean is_fs_webView3_in_screen      () { return (fs_webView3 != null) && (Settings.SCREEN_SPLIT > 2); }

    private boolean is_fs_wtitle2_in_screen       () { return (fs_wtitle2  != null) && (Settings.SCREEN_SPLIT > 1); }
    private boolean is_fs_wtitle3_in_screen       () { return (fs_wtitle3  != null) && (Settings.SCREEN_SPLIT > 2); }

    public  boolean is_fs_webViewX_in_screen      (MWebView fs_webViewX)
    {
        if(fs_webViewX == null       ) return false;
        if(fs_webViewX == fs_webView2) return is_fs_webView2_in_screen();
        if(fs_webViewX == fs_webView3) return is_fs_webView3_in_screen();
        return                                true; // fs_webView
    }
    //}}}
    // DRAGGING STATE (SHOWING) (HIDING) {{{
    // {{{
    private static final int HSV_GESTURE_SCROLLX            =   50;

    // }}}
    // is_at_left_margin {{{
    private boolean is_at_left_margin()
    {
        return (hsv.getScrollX() <  HSV_GESTURE_SCROLLX);
    }
    //}}}
    // is_dragBand_showing {{{
    private boolean is_dragBand_showing()
    {
        return is_view_showing( drag_band ) && !is_band_fully_hidden( drag_band );
    }
    //}}}
    // is_dragBand_more_showing_than_hiding {{{
    private boolean is_dragBand_more_showing_than_hiding()
    {
        if(drag_band == null) return false;

        int vis_X = get_band_vis_X( drag_band );
        int     x = (int)drag_band.getX();
        int     w =      drag_band.getWidth();
        return (x > (vis_X - w/2));
    }
    //}}}
    // is_dragBand_in_quarter_left {{{
    private static final int SLIDING_HIDDEN  =  0; // FULLY HIDDEN
    private static final int SLIDING_LEFT    =  1; // right border showing within left third
    private static final int SLIDING_RIGHT   =  2; // right border showing within right third
    private static final int SLIDING_VISIBLE =  3; // FULLY VISIBLE

    private int get_dragBand_sliding()
    {
        int     h = get_band_hid_X(drag_band);
        int     w = drag_band.getWidth();
        int     c = h + w  /2;
        int     x = (int)drag_band.getX();
        if(x <=  h   ) return SLIDING_HIDDEN ; // HIDDEN[<-      |         ]
        if(x <   c   ) return SLIDING_LEFT   ; //       [...LEFT |         ]
        if(x <  (h+w)) return SLIDING_RIGHT  ; //       [        | RIGHT...]
        /*.fallback.*/ return SLIDING_VISIBLE; //       [        |  VISIBLE]<-
    }

    private String get_dragBand_slidingStr(int sliding)
    {
        switch( sliding ) {
            case SLIDING_HIDDEN     : return " SLIDING_HIDDEN     - FULLY   HIDDEN";
            case  SLIDING_LEFT      : return "  SLIDING_LEFT      - MOSTLY  HIDDEN";
            case   SLIDING_RIGHT    : return "   SLIDING_RIGHT    - MOSTLY SHOWING";
            case    SLIDING_VISIBLE : return "    SLIDING_VISIBLE - FULLY  VISIBLE";
            default                 : return "default: sliding="+sliding;
        }
    }

    //}}}
    // is_dragBand_hidden {{{
    private boolean is_dragBand_hidden(View band)
    {
        String caller = "is_dragBand_hidden";

        int h = get_band_hid_X( band );
        int v = get_band_vis_X( band );
        int x = (int)band.getX();
        boolean result = (x == h);

//*BAND*/String h_x_v = String.format("HID=[%4d] ... x=[%4d] ... VIS=[%4d]", h, x, v);//TAG_BAND
//*BAND*/Settings.MON(TAG_BAND, caller, "return "+result+" band=["+ get_view_name(band) +"] "+h_x_v);
        return result;
    }
    //}}}
    // is_dragBand_opened {{{
    private boolean is_dragBand_opened(View band)
    {
        String caller = "is_dragBand_opened";

        int h = get_band_hid_X( band );
        int v = get_band_vis_X( band );
        int x = (int)band.getX();
        boolean result = (x == v);

//*BAND*/String h_x_v = String.format("HID=[%4d] ... x=[%4d] ... VIS=[%4d]", h, x, v);//TAG_BAND
//*BAND*/Settings.MON(TAG_BAND, caller, "return "+result+ " band=["+ get_view_name(band) +"] "+h_x_v);
        return result;
    }
    //}}}
    //}}}
    // get_view_vibibility {{{
    private String get_view_vibibility(View view)
    {
        if     (view ==                           null) return "NULL_VIEW";
        else if(view.getVisibility() == View.GONE     ) return      "GONE";
        else if(view.getVisibility() == View.VISIBLE  ) return   "VISIBLE";
        else if(view.getVisibility() == View.INVISIBLE) return "INVISIBLE";
        else /*......................................*/ return       "???";
    }
    //}}}
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** DCK [DOCK PROF CONTROLS INFO] */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ DCK @ {{{
    // hide_PROFILE_HANDLES {{{
    public void hide_PROFILE_HANDLES(String caller)
    {
        caller += "->hide_PROFILE_HANDLES";
//*HANDLE*/Settings.MOC(TAG_HANDLE,caller);
        hide_DOCKINGS_TABLE(caller);
        hide_PROFILES_TABLE(caller);
        hide_PROFHIST_TABLE(caller);
        hide_SOUNDS_TABLE  (caller);
    }
    //}}}
    //* DOCK */
    // show_DOCKINGS_TABLE {{{
    public void show_DOCKINGS_TABLE(String caller)
    {
        caller += "->show_DOCKINGS_TABLE";
//*HANDLE*/Settings.MON(TAG_HANDLE, caller, "GUI_STATE=["+ get_GUI_STATE(GUI_STATE) +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, ".........................DOCK_Map.size=["+ RTabsClient.DOCK_Map.size()         +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, ".......................Working_profile=["+ Settings.Working_profile            +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, ".....................dock_band_showing=["+ is_view_showing(dock_band)              +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, "....................dck_handle_showing=["+ is_view_showing(dck_handle)         +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, "...............dck_handle.needs_layout=["+ dck_handle.needs_layout()           +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, ".....dock_band.container.getChildCount=["+ dock_band.container.getChildCount() +"]");

        // rebuild table when its content has changed {{{
        if(        (RTabsClient.DOCK_Map.size() == 0)
                || (dock_band.container.getChildCount() < RTabsClient.DOCK_Map.size())
                || Settings.Working_profile.startsWith("WORKBENCH")
          ) {
//*HANDLE*/Settings.MON(TAG_HANDLE, caller, "...load_DOCKINGS_TABLE:");
            this_RTabsClient.load_DOCKINGS_TABLE(caller);
            dck_handle.needs_layout(false);

            //dock_scroll_to_working_profile(caller);
          }
        //}}}
        // ...or just update its layout {{{
        else if(dck_handle.needs_layout())
        {
//*HANDLE*/Settings.MON(TAG_HANDLE, caller, "...update_TABS_LAYOUT:");
            this_RTabsClient.update_TABS_LAYOUT(Settings.DOCKINGS_TABLE, RTabsClient.DOCK_Map);
            dck_handle.needs_layout(false);

            //dock_scroll_to_working_profile(caller);
        }
        //}}}

        handle_set(dck_handle, caller);
      //sync_tabs_scrolling(caller);
        set_tabs_scrolling(false, caller);
    }
    //}}}
    // hide_DOCKINGS_TABLE {{{
    private void hide_DOCKINGS_TABLE(String caller)
    {
        caller += "->hide_DOCKINGS_TABLE";
//*HANDLE*/Settings.MOC(TAG_HANDLE, caller);

        if(D) {
            String s = Settings.SYMBOL_NO_ENTRY+Settings.SYMBOL_NO_ENTRY+Settings.SYMBOL_NO_ENTRY;
            MLog.log_black(s+caller);
        }
        if( Settings.is_GUI_TYPE_HANDLES() ) {
            dock_band.setVisibility( View.GONE );
        }
        if(Handle.Get_cur_handle() == dck_handle) {
            handle_hide(caller);
        }
    }
    //}}}
    //* PROF */
    // show_PROFILES_TABLE {{{
    public void show_PROFILES_TABLE(String caller)
    {
        caller += "->show_PROFILES_TABLE";
//*HANDLE*/Settings.MOC(TAG_HANDLE, caller);
//*HANDLE*/Settings.MOM(TAG_HANDLE, "...................GUI_STATE=["+ get_GUI_STATE(GUI_STATE)      +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, ".............PROF_Map.size()=["+ RTabsClient.PROF_Map.size()   +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, ".............Working_profile=["+ Settings.Working_profile      +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, "..........top_handle_showing=["+ is_handle_showing(top_handle) +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, "...top_handle.needs_layout()=["+ top_handle.needs_layout()     +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, "..top_handle.getChildCount()=["+ top_handle.getChildCount()    +"]");

        // rebuild table when its content has changed {{{
        if(        (RTabsClient.PROF_Map.size() == 0)
                || (top_handle.getChildCount()   <  RTabsClient.PROF_Map.size())
                || Settings.Working_profile.startsWith("WORKBENCH")
          ) {
            this_RTabsClient.load_PROFILES_TABLE(caller);
            top_handle.needs_layout(false);
          }
        //}}}
        // ...or just update its layout {{{
        else if(top_handle.needs_layout())
        {
            this_RTabsClient.update_TABS_LAYOUT(Settings.PROFILES_TABLE, RTabsClient.PROF_Map);
            top_handle.needs_layout(false);
        }
        //}}}

fold_band(dock_band, caller); // XXX
        handle_set(top_handle, caller);
        top_handle.setVisibility( View.VISIBLE );
    }
    //}}}
    // hide_PROFILES_TABLE {{{
    private void hide_PROFILES_TABLE(String caller)
    {
        caller += "->hide_PROFILES_TABLE";
//*HANDLE*/Settings.MOC(TAG_HANDLE, caller);

        if(D) {
            String s = Settings.SYMBOL_NO_ENTRY+Settings.SYMBOL_NO_ENTRY+Settings.SYMBOL_NO_ENTRY;
            MLog.log_black(s+caller);
        }
        if(Handle.Get_cur_handle() == top_handle) {
            handle_hide(caller);
            //Handle.Re_post_dimm();
        }
    }
    //}}}
    //* HIST */
    // show_PROFHIST_TABLE {{{
    public void show_PROFHIST_TABLE(String caller)
    {
        caller += "->show_PROFHIST_TABLE";
//*HANDLE*/Settings.MOC(TAG_HANDLE, caller);
//*HANDLE*/Settings.MOM(TAG_HANDLE, ".............AUTO_Map.size()=["+ RTabsClient.AUTO_Map.size() +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, ".............Working_profile=["+ Settings.Working_profile    +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, "........is_fg_view_showing()=["+ is_fg_view_showing()        +"]");

        this_RTabsClient.load_PROFHIST_TABLE(caller);

        splash_screen_fg_view();
    }
    //}}}
    // hide_PROFHIST_TABLE {{{
    private void hide_PROFHIST_TABLE(String caller)
    {
        caller += "->hide_PROFHIST_TABLE";
//*HANDLE*/Settings.MOC(TAG_HANDLE, caller);

        hide_fg_view(caller);
    }
    //}}}
    public static ArrayList<String> get_history_profile_names() // {{{
    {
        /* TAB [PROFHIST_TABLE] CLICKED...
         *  this_RTabsClient      .np_OnClickListener
         *   this_RTabsClient      .sendTabCommand
         *    this_RTabsClient      .parse_PROFILE_cmdLine
         *     this                  .request_cmd_text
         *      this                  .show_PROFHIST_TABLE
         *       Profile               .load(Settings.PROFHIST_TABLE)
         *        Profile               .build_PROFHIST_TABLE
         *         Profile               .get_history_profile_name_list
         *          this                  .get_history_profile_names
         */
        if(this_RTabsClient == null) return null;
        else                         return this_RTabsClient.history_get_profile_names();
    }
    // }}}
    //* SOUNDS */
    // show_SOUNDS_TABLE {{{
    public void show_SOUNDS_TABLE(String caller)
    {
        caller += "->show_SOUNDS_TABLE";
//*HANDLE*/Settings.MOC(TAG_HANDLE, caller);
//*HANDLE*/Settings.MOM(TAG_HANDLE, ".............AUTO_Map.size()=["+ RTabsClient.AUTO_Map.size() +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, ".............Working_profile=["+ Settings.Working_profile    +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, "........is_fg_view_showing()=["+ is_fg_view_showing()        +"]");

        this_RTabsClient.load_SOUNDS_TABLE(caller);

        splash_screen_fg_view();
    }
    //}}}
    // hide_SOUNDS_TABLE {{{
    private void hide_SOUNDS_TABLE(String caller)
    {
        caller += "->hide_SOUNDS_TABLE";
//*HANDLE*/Settings.MOC(TAG_HANDLE, caller);

        hide_fg_view(caller);
    }
    //}}}
    //* CONTROLS */
    // show_CONTROLS_TABLE {{{
    public void show_CONTROLS_TABLE(String caller)
    {
        caller += "->show_CONTROLS_TABLE";
//*HANDLE*/Settings.MOC(TAG_HANDLE, caller);
//*HANDLE*/Settings.MOM(TAG_HANDLE, "...................GUI_STATE=["+ get_GUI_STATE(GUI_STATE)      +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, ".............CTRL_Map.size()=["+ RTabsClient.CTRL_Map.size()   +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, ".............Working_profile=["+ Settings.Working_profile      +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, "..........mid_handle_showing=["+ is_handle_showing(mid_handle) +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, "...mid_handle.needs_layout()=["+ mid_handle.needs_layout()     +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, "..mid_handle.getChildCount()=["+ mid_handle.getChildCount()    +"]");

        // rebuild table when its content has changed {{{
        if(        (RTabsClient.CTRL_Map.size() == 0)
                || (mid_handle.getChildCount()   <  RTabsClient.CTRL_Map.size())
          ) {
            this_RTabsClient.load_CONTROLS_TABLE(caller);
            mid_handle.needs_layout(false);
          }
        //}}}
        // ...or just update its layout {{{
        else if(mid_handle.needs_layout())
        {
            if(D) MLog.log("mid_handle.needs_layout:");
            this_RTabsClient.update_TABS_LAYOUT(Settings.CONTROLS_TABLE, RTabsClient.CTRL_Map);
            mid_handle.needs_layout(false);
        }
        //}}}

fold_band(dock_band, caller); // XXX
        handle_set(mid_handle, caller);
        mid_handle.setVisibility( View.VISIBLE );
    }
    //}}}
    // hide_CONTROLS_TABLE {{{
    private void hide_CONTROLS_TABLE(String caller)
    {
        caller += "->hide_CONTROLS_TABLE";
//*HANDLE*/Settings.MOC(TAG_HANDLE, caller);

        if(D) {
            String s = Settings.SYMBOL_NO_ENTRY+Settings.SYMBOL_NO_ENTRY+Settings.SYMBOL_NO_ENTRY;
            MLog.log_black(s+caller);
        }
        if(Handle.Get_cur_handle() == mid_handle) {
            handle_hide(caller);
            //Handle.Re_post_dimm();
        }
    }
    //}}}
    //* INFO */
    // show_INFO_PANEL {{{
    private void show_INFO_PANEL(String caller)
    {
        caller += "->show_INFO_PANEL";
//*HANDLE*/Settings.MOC(TAG_HANDLE, caller);
//*HANDLE*/Settings.MOM(TAG_HANDLE, "...................GUI_STATE=["+ get_GUI_STATE(GUI_STATE)      +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, ".............Working_profile=["+ Settings.Working_profile      +"]");
//*HANDLE*/Settings.MOM(TAG_HANDLE, "..........bot_handle_showing=["+ is_handle_showing(bot_handle) +"]");

        handle_set(bot_handle, caller);
        bot_handle        .setVisibility( View.VISIBLE );
        controls_container.setVisibility( View.VISIBLE );
    }
    //}}}
    // hide_INFO_PANEL {{{
    public void hide_INFO_PANEL(String caller)
    {
        caller += "->hide_INFO_PANEL";
//*HANDLE*/Settings.MOC(TAG_HANDLE, caller);

        if(D) {
            String s = Settings.SYMBOL_NO_ENTRY+Settings.SYMBOL_NO_ENTRY+Settings.SYMBOL_NO_ENTRY;
            MLog.log_black(s+caller);
        }
        if(Handle.Get_cur_handle() == bot_handle) {
            handle_hide(caller);
            //Handle.Re_post_dimm();
        }
    }
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** HANDLE (BAND CONTAINERS) */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ HANDLE @ {{{
    // CONTAINERS {{{
    public ViewGroup get_dock_container() { return dock_band.container; }
    public ViewGroup get_prof_container() { return top_handle;          }
    public ViewGroup get_hist_container() { return get_empty_fg_view(); }
    public ViewGroup get_ctrl_container() { return mid_handle;          }
//  public ViewGroup get_tabs_container() { return tabs_container; } // (!) may change during activity life-cycle .. @see onCreate (!)

    //}}}
    // GUI_STATE {{{
    // {{{
    private static final int GUI_STATE_TABS = 0;
    private static final int GUI_STATE_PROF = 1;
    private static final int GUI_STATE_DOCK = 2;
    private static final int GUI_STATE_CTRL = 3;
    private static final int GUI_STATE_INFO = 4;
    private              int GUI_STATE      = GUI_STATE_TABS;
    // }}}
    // get_GUI_STATE {{{
    private String get_GUI_STATE(int state)
    {
        switch(state) {
            case GUI_STATE_PROF: return "GUI_STATE_PROF";
            case GUI_STATE_DOCK: return "GUI_STATE_DOCK";
            case GUI_STATE_CTRL: return "GUI_STATE_CTRL";
            case GUI_STATE_INFO: return "GUI_STATE_INFO";
            default:             return "GUI_STATE_TABS";
        }
    }
    //}}}
    // apply_GUI_STATE {{{
    // ...apply_GUI_STATE {{{
    private static final int    APPLY_GUI_STATE_DELAY = 100;
    private static final int SCHEDULE_GUI_STATE_DELAY = 100;

    public  void  apply_GUI_STATE_INFO(String caller) { apply_GUI_STATE(GUI_STATE_INFO, FROM_LOG, caller); }

    private void  apply_GUI_STATE(int state, int fromSource, String caller)
    {
        caller += "->apply_GUI_STATE("+get_GUI_STATE(state)+", "+get_fromSource(fromSource)+")";
//*HANDLE*/Settings.MOC(TAG_HANDLE, caller);

        pending_GUI_STATE        = state;
        pending_GUI_STATE_SOURCE = fromSource;
        handler.re_postDelayed( hr_apply_GUI_STATE, APPLY_GUI_STATE_DELAY);
    }
    //}}}
    // hr_apply_GUI_STATE {{{
    private final Runnable hr_apply_GUI_STATE = new Runnable() {
        @Override public void run() { do_apply_GUI_STATE(); }
    };

    //}}}
    // do_apply_GUI_STATE {{{
    private int pending_GUI_STATE;
    private int pending_GUI_STATE_SOURCE;

    private void do_apply_GUI_STATE()
    {
        // CHECK NEW STATE {{{
        int state      = pending_GUI_STATE;
        int fromSource = pending_GUI_STATE_SOURCE;
        String caller = "do_apply_GUI_STATE("+get_GUI_STATE(state)+", "+get_fromSource(fromSource)+")";

        if(state == GUI_STATE) {
//*HANDLE*/Settings.MON(TAG_HANDLE, caller, "(GUI_STATE UNCHANGED) for "+caller);
            return;
        }

//*HANDLE*/Settings.MOC(TAG_HANDLE, caller);
        GUI_STATE = state; // set new state

        //}}}
        // POST A HANDLE DISPLAY SYNC {{{
        handler.re_postDelayed( hr_sync_handles, Handle.PARK_HANDLES_DELAY);
        //}}}
        // CANCEL TRANSIENT STATE {{{
        if( is_magnify_np_showing() )
            magnify_np_hide(caller);

        // }}}
        // SHOW: DOCK PROF CTRL INFO TABS {{{
        if(GUI_STATE == GUI_STATE_DOCK) { show_DOCKINGS_TABLE(caller); return; }
        if(GUI_STATE == GUI_STATE_PROF) { show_PROFILES_TABLE(caller); return; }
        if(GUI_STATE == GUI_STATE_CTRL) { show_CONTROLS_TABLE(caller); return; }
        if(GUI_STATE == GUI_STATE_INFO) { show_INFO_PANEL    (caller); return; }

        //}}}
        // DEFAULT TO TABS {{{
        if(D) MLog.log("this_RTabsClient.TABS_Map.size()="+ RTabsClient.TABS_Map.size());
        if(D) MLog.log("Profile.Get_Profiles_Dict_size()="+Profile.Get_Profiles_Dict_size());
        if(RTabsClient.TABS_Map.size() > 0)
        {
            hide_DOCKINGS_TABLE(caller);
            hide_CONTROLS_TABLE(caller);
            hide_PROFILES_TABLE(caller);
            hide_INFO_PANEL    (caller);
            if( !Settings.is_GUI_TYPE_HANDLES() )
                show_histBand(fromSource, caller);
        }
        // }}}
        // LOAD TABS {{{
        else {
            if(Profile.Get_Profiles_Dict_size() > 0)
            {
                if(        (Handle.Get_cur_handle() != top_handle)
                        && (Handle.Get_cur_handle() != dck_handle)
                  ) {
                    if( Settings.is_GUI_TYPE_HANDLES()) show_PROFILES_TABLE(caller);
                    else                                show_DOCKINGS_TABLE(caller);
                  }
                else if(M||D) Settings.MON(TAG_HANDLE, caller, "...KEEPING CURRENT HANDLE");
            }
            else {
                if(Handle.Get_cur_handle() != mid_handle)
                    show_CONTROLS_TABLE(caller);
                else if(M||D) Settings.MON(TAG_HANDLE, caller, "...KEEPING CURRENT HANDLE");
            }
        }
        // }}}
    }
    //}}}
    //}}}
    //}}}
    // sync_dock_GUI_TYPE {{{
    private void sync_dock_GUI_TYPE(String caller, int fromSource)
    {
        caller += "->sync_dock_GUI_TYPE";
//*HANDLE*/Settings.MOC(TAG_HANDLE, caller);

        // in both GUI_TYPE configuration, dock_band and top_handle may be used to load user profiles
    //  if(    dock_band.getParent() == null) dck_handle .addView   ( dock_band );
        if(    dock_band.getParent() == null) frameLayout.addView   ( dock_band );

        if( Settings.is_GUI_TYPE_HANDLES() )
        {
            if(show_band.getParent() != null) frameLayout.removeView(  show_band );
            if(cart_band.getParent() != null) frameLayout.removeView(   cart_band );
            if(hist_band.getParent() != null) frameLayout.removeView(   hist_band );
        //  if(show_band.getParent() != null) dck_handle .removeView(  show_band );
        //  if(hist_band.getParent() != null) dck_handle .removeView(   hist_band );
        //  if(cart_band.getParent() != null) dck_handle .removeView(   cart_band );
        }
        else {
            if(show_band.getParent() == null) frameLayout.addView   (  show_band );
            if(hist_band.getParent() == null) frameLayout.addView   (   hist_band );
            if(cart_band.getParent() == null) frameLayout.addView   (   cart_band );
        //  if(show_band.getParent() == null) dck_handle .addView   (  show_band );
        //  if(cart_band.getParent() == null) dck_handle .addView   (   cart_band );
        //  if(hist_band.getParent() == null) dck_handle .addView   (   hist_band );
        }
    }
    //}}}
    // handle_hide {{{
    private void handle_hide(String caller)
    {
        caller += "->handle_hide";
if(D||M) Settings.MOC(TAG_HANDLE, caller);
        handle_set(null, caller);
      //sync_tabs_scrolling(caller);
      //set_tabs_scrolling( true, caller);
    }
    //}}}
    // handle_set {{{
    private void handle_set(Handle handle, String caller)
    {
        caller += "->handle_set("+get_handle_name(handle)+")";
//*HANDLE*/Settings.MON(TAG_HANDLE, caller, "cur_handle=["+get_handle_name( Handle.Get_cur_handle() )+"]");

        // dismiss full screen views
        hide_fg_view(caller);

        // expand specified handle
        Handle.Set_cur_handle(handle, caller);

        sync_SUI_visibility(caller); // Handle activity
    }
    //}}}
    // get_handle_name {{{
    private String get_handle_name(Handle handle)
    {
        String handle_name      = "null";
        if(handle != null) {
            if     (handle == lft_handle) { handle_name = "lft_handle"; }
            else if(handle == dck_handle) { handle_name = "dck_handle"; }
            else if(handle == top_handle) { handle_name = "top_handle"; }
            else if(handle == mid_handle) { handle_name = "mid_handle"; }
            else if(handle == bot_handle) { handle_name = "bot_handle"; }
        }
        return handle_name;
    }
    //}}}
    // adjust_handles {{{
    private void adjust_handles(String caller, int fromSource)
    {
        caller += "->adjust_handles";
//*HANDLE*/Settings.MOC(TAG_HANDLE, caller);

        sync_dock_GUI_TYPE(caller, fromSource);
/*
            if(        !is_view_showing(hist_band)
                    && !is_view_showing(dock_band)
                    && !is_view_showing(cart_band)
              )
                show_histBand(fromSource, caller);
*/
    //  int   w = Settings.SCREEN_W ;
        int   h = Settings.DISPLAY_H; int h4 = h/4;
        int  hk = Settings.is_GUI_TYPE_HANDLES() ? h4 : h;
        int sbw = 2*Handle.Get_STANDBY_WIDTH(); // f(GUI_TYPE)  [dock_band shows vertical profile] [handles shows parked or tucked narrow rectangles]
        int dhw = 2*Handle.DOCK_HIDE_WIDTH;

        int top, width, height;
        top =   0; width = sbw; height = h ; lft_handle.setDockedLayout(top, width, height);
        top =   0; width = sbw; height = hk; dck_handle.setDockedLayout(top, width, height);
        top =  h4; width = sbw; height = h4; top_handle.setDockedLayout(top, width, height);
        top =2*h4; width = sbw; height = h4; mid_handle.setDockedLayout(top, width, height);
        top =3*h4; width = dhw; height = h4; bot_handle.setDockedLayout(top, width, height);

        // dck_handle {{{
        // ...dock_band has no scaled down version to show when parked (YET)
        if( Settings.is_GUI_TYPE_HANDLES() ) dck_handle.setTag( null );
        else                                 dck_handle.setTag( Settings.HANDLE_HIDDEN_IN_STANDBY );

        //}}}
    }
    //}}}
    // invalidate_handles {{{
    private void invalidate_handles(int fromSource, String caller)
    {
        caller += "->invalidate_handles("+ get_fromSource(fromSource)+")";
//*HANDLE*/Settings.MOC(TAG_HANDLE, caller);
        Handle cur_handle = Handle.Get_cur_handle();
//*HANDLE*/Settings.MOM(TAG_HANDLE, "...cur_handle="+ get_handle_name(cur_handle));

        // keep in sync with Settings.is_GUI_TYPE_HANDLES
        Handle.Collapse_all_instances();

        // layout relevant handles
        dck_handle.needs_layout( true );
        top_handle.needs_layout( true );
        mid_handle.needs_layout( true );

        if     (cur_handle == dck_handle) show_DOCKINGS_TABLE(caller);
        else if(cur_handle == top_handle) show_PROFILES_TABLE(caller);
        else if(cur_handle == mid_handle) show_CONTROLS_TABLE(caller);
        else if(cur_handle == bot_handle) show_INFO_PANEL    (caller);
    //  else                              sync_handles       (caller);
    //  else if(cur_handle != null      ) Handle.Collapse_all_instances();
    }
    //}}}
    // invalidate_profile_handles {{{
    public void invalidate_profile_handles(String caller)
    {
        caller += "->invalidate_profile_handles";
        Handle cur_handle = Handle.Get_cur_handle();
//*HANDLE*/Settings.MON(TAG_HANDLE, caller, "cur_handle="+ get_handle_name(cur_handle));

        // layout relevant handles
        dck_handle.needs_layout( true );
        top_handle.needs_layout( true );

        if((cur_handle == dck_handle) ) show_DOCKINGS_TABLE(caller);
    }
    //}}}
    /** SYNC */
    // sync_handles {{{
    private void sync_handles(String caller)
    {
        caller += "->sync_handles";
//*HANDLE*/Settings.MOC(TAG_HANDLE, caller);

        // IF NO CURRENT, WAKEUP THE UNDERTAKER
        if((Handle.Get_cur_handle() == null) && Settings.is_GUI_TYPE_HANDLES())
            handle_set(lft_handle, caller);

        // reshape handles
        //Handle.expand_current_collapse_others();

        // post a handle display sync
        handler.re_postDelayed( hr_sync_handles, Handle.PARK_HANDLES_DELAY);

    }
    //}}}
    // hr_sync_handles {{{
    private final Runnable hr_sync_handles = new Runnable()
    {
        @Override public void run() {
            do_sync_handles();
        }
    };
    // }}}
    // do_sync_handles {{{
    private void do_sync_handles()
    {
        String caller = "do_sync_handles";
        Handle cur_handle       = Handle.Get_cur_handle();
//*HANDLE*/Settings.MON(TAG_HANDLE, caller, "cur_handle=["+ get_handle_name(cur_handle) +"]");

        // [HANDLE]
        if(        (cur_handle ==       null)
                || (cur_handle == lft_handle)
          ) {
            // [TABS] (park handles) {{{
            if(tabs_container.getChildCount() > 0)
            {
                // collapse handles
                handle_hide(caller);

                // park handles
                //if( Settings.is_GUI_TYPE_HANDLES() ) handles_container.setScrollX( Handle.Get_PARK_X() );
                // FIXME unplugged .. (keeps hiding with current reset)
            }
            // }}}
            // [CONTROLS] (profile zip extraction) {{{
            else {
                if(Profile.Get_Profiles_Dict_size() > 0)
                {
                    if( Settings.is_GUI_TYPE_HANDLES()) show_PROFILES_TABLE(caller);
                    else                                show_DOCKINGS_TABLE(caller);
                }
                else {
                    show_CONTROLS_TABLE(caller);
                }
            }
            //}}}
          }
    }
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** BAND */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ BAND @ {{{
    //* (show-hide) {{{ */
    //{{{
    private boolean show_band_hidden = false;

    // HIDE dck_handle
    //            .-----.
    //            B|    | <- handles_container
    //            B|    |
    //  SHOWBAND  B|    |
    //      HIDE  B|    |
    //            B|    |
    //            B|<-  |
    //            '-----'

    // SHOW dck_handle
    //            .-----.
    //            |B    | <- handles_container
    //            |B    |
    //   SHOWBAND |B    |
    //       SHOW |B    |
    //            |B    |
    //          ->|B    |
    //            '-----'

    //
    // [...to be enforced by async GUI updates]
    //
    // RESET BY:
    // - handle_set()                       .. changing current Handle may lead to nothing showing when closed (i.e. when done with CONTROLS_TABLE or PROFILES_TABLE)
    // - cart_end_click()                   .. better with hist_band hiding .. (implies show_band-showing)
    //
    // ENFORCED BY:
    // - slide_band_animation_after_chain   .. at magnify_np termination .. (synchronize may-have-been-disturbed band layout)
    // - release_dragBand                   .. at dragging termination   .. (as above)
    //
    // PROBED BY:
    // - check_scrolling_allowed            .. scrolling allowed when hist_band-showing-pinned
    // - check_scaling_alloweded            .. (as above)
    //

    //}}}
    // show_band_hide {{{
    private void show_band_hide(String caller)
    {
        show_band_hidden =  true;
    //  dck_handle.scrollTo(show_band.getWidth(), 0);
        show_band.setVisibility(View.GONE);
    }
    //}}}
    // show_band_show {{{
    private void show_band_show(String caller)
    {
        show_band_hidden = false;
    //  dck_handle.scrollTo(0, 0);
    //  dck_handle.setVisibility(View.VISIBLE);
        show_band.setVisibility(View.VISIBLE);
    }
    //}}}
    // show_dock show_histBand show_cartBand {{{
    private void show_dock    (int fromSource, String caller) { caller =     "show_dock->"+caller;                           _selectBand(dock_band, fromSource, caller); }
    private void show_histBand(int fromSource, String caller) { caller = "show_histBand->"+caller;                           _selectBand(hist_band, fromSource, caller); }
    private void show_cartBand(int fromSource, String caller) { caller = "show_cartBand->"+caller; sync_cartBand(caller+""); _selectBand(cart_band, fromSource, caller); }
    // }}}
    // _selectBand {{{
    private static final int CYCLE_FOLD_BAND_DELAY = 1000;
    private static final int USER_FOLD_BAND_DELAY  =  500;
    private static final int SELECT_BAND_COOLDOWN  =  500;

    // make_band_visible {{{
    private void make_band_visible(View selected_band)
    {
        // SET EXCLUSIVE BAND VISIBILITY
        String caller = "make_band_visible("+get_view_name(selected_band)+")";

        if(     selected_band == dock_band) //{{{
        {
            dock_band.setVisibility( View.VISIBLE );
            hist_band.setVisibility( View.GONE    );
            cart_band.setVisibility( View.GONE    );

//BAND*/Settings.MOC(TAG_BAND, caller);
            show_DOCKINGS_TABLE(caller);
            show_band.set_selected( show_band.dock_show );
        } //}}}
        else if(selected_band == hist_band) //{{{
        {
            dock_band.setVisibility( View.GONE    );
            hist_band.setVisibility( View.VISIBLE );
            cart_band.setVisibility( View.GONE    );

//*BAND*/Settings.MOC(TAG_BAND, caller);
            show_band.set_selected ( show_band.hist_show );
        } //}}}
        else if(selected_band == cart_band) //{{{
        {
            dock_band.setVisibility( View.GONE    );
            hist_band.setVisibility( View.GONE    );
            cart_band.setVisibility( View.VISIBLE );

//*BAND*/Settings.MOC(TAG_BAND, caller);
            show_band.set_selected ( show_band.cart_show );
        } //}}}
//dck_handle.requestLayout();
//handles_container.requestLayout();
    }
    //}}}

    private void _selectBand(View selected_band, int fromSource, String caller)
    {
        caller += "->_selectBand("+get_view_name(selected_band)+", "+get_fromSource(fromSource)+")";
//*BAND*/Settings.MOC(TAG_BAND, caller);
        // CANCEL PENDING MAGNIFICATION ON DISABLED VIEW {{{
        cancel_pending_button_to_magnify(caller);

        // }}}
        // GUI_TYPE_HANDLES {{{
        if( Settings.is_GUI_TYPE_HANDLES() )
        {
            show_band.set_selected( null );

            if(GUI_STATE != GUI_STATE_TABS)
                apply_GUI_STATE(GUI_STATE_TABS, fromSource, caller);
        }
        // }}}
        // GUI_TYPE_DOCKING {{{
        // CALLERS:
        //\<show_dock\>\|\<show_histBand\>\|\<show_cartBand\>
        else {
            // CANCEL BAND DRAG
            if(drag_band != null)
                clear_dragBand(caller);

            // DOCK {{{
            if     (selected_band == dock_band)
            {
                show_DOCKINGS_TABLE(caller);

                make_band_visible( dock_band );

                handler.re_post( hr_open_dock );

                // SHOWBAND {{{
                    show_band_show(caller);
                // }}}
            }
            // }}}
            // HIST {{{
            else if(selected_band == hist_band)
            {
                hide_DOCKINGS_TABLE(caller);

                make_band_visible( hist_band );

                band_anim_duration
                    = is_caller_controling_profile_history(caller)
                    ? BAND_ANIM_DURATION_HIST
                    : BAND_ANIM_DURATION_DRAG
                    ;
                handler.re_post( hr_open_histBand );

                // DELAYED HIDE ++ show_band hide-show f(thumb history navigation) {{{

                // HISTBAND UI [USING]
                if( is_caller_controling_profile_history(caller) ) {
                //  show_band_hide(caller); // hide show tools
                }
                // HISTBAND UI [DISPLAYING]
                else {
                    // delayed hist_band hiding
                    if( !is_caller_controling_histBand_fold(caller) )
                        handler.re_postDelayed(hr_fold_histBand, get_fold_band_delay( fromSource ));
                }
                // }}}
            }
            // }}}
            // CART {{{
            else if(selected_band == cart_band)
            {
                hide_DOCKINGS_TABLE(caller);

                make_band_visible( cart_band );

                handler.re_post( hr_open_cartBand );

                // SHOWBAND {{{
            //  show_band_hide(caller); // hide show tools
                // }}}
                // assert scale 1 for cart process {{{
                if(tabs_container.getScaleX() != 1F)
                {
//*BAND*/Settings.MON(TAG_BAND, caller, "XXX  ASSERT SCALE 1 FOR CART PROCESSING: scale=["+ tabs_container.getScaleX() +"]");
                    tabs_container.setScaleX(1F);
                    tabs_container.setScaleY(1F);
                    tabs_container.setVisibility( View.INVISIBLE ); // transient state
                    this_RTabsClient.apply_TABS_LAYOUT(RTabsClient.TABS_Map, tabs_container, caller);
                }
                //}}}
            }
            // }}}
        }
        // }}}
        // [FREEZED] [OFFLINE] [TABS] [BACKGROUND]
        //do_sync_GUI_colors(caller); // _selectBand
    }
    //}}}
    // is_caller_controling_histBand_fold {{{
    private boolean is_caller_controling_histBand_fold(String caller)
    {
        caller += "->is_caller_controling_histBand_fold";
        // callers not wanting hr_fold_histBand scheduled
        boolean result
            =  caller.contains("onFling")
            || caller.contains("release_dragBand")
            || caller.contains("show_hist_OnTouchListener")
            || caller.contains("show_dock_OnTouchListener")
            ;
// W:        BAND [is_caller_controling_histBand_fold(_selectBand(hist_band, FROM_TOUCH, caller=[show_dock_OnTouchListener(ACTION_DOWN)]

//*BAND*/Settings.MON(TAG_BAND, caller, "...return ["+result+"]");
        return result;
    }
    //}}}
    // is_caller_controling_profile_history {{{
    private boolean is_caller_controling_profile_history(String caller)
    {
        caller += "->is_caller_controling_profile_history";

        // callers wanting show_band_hide to be called
        boolean result
            =   caller.contains(                "onKeyDown")
            ||  caller.contains(             "history_back")
            ||  caller.contains(             "history_frwd")
            //  caller.contains(       "history_back_clear")
            //  caller.contains(       "history_frwd_clear")
            || (caller.contains("show_hist_OnTouchListener") && caller.contains("hist_show"))
            ;
        // ...all other callers will have show_band visible

//*BAND*/Settings.MON(TAG_BAND, caller, "...return ["+result+"]");
        return result;
    }
    //}}}
    // get_cart_state {{{
    public int get_cart_state(String caller)
    {
        caller += "->get_cart_state";

        boolean cartBand_enabled
            =  (cart_band.getParent()     != null        )
            && (cart_band.getVisibility() == View.VISIBLE);

        boolean cart_is_empty
            = (this_RTabsClient.get_cart_size() == 0);

        boolean cart_add_state
            = (cart_band.see_nb.getBackgroundColor() == Settings.ADD_NP_COLOR);

        int cart_state
            = (cartBand_enabled)
            ?  (cart_add_state
                    ? Settings.CART_STATE_ADD
                    : Settings.CART_STATE_DEL    )
            :         Settings.CART_STATE_DEFAULT;

//*BAND*/Settings.MON(TAG_BAND, caller, "...return ["+ Settings.Get_cart_state_name(cart_state) +"]");
        return cart_state;
    }
    //}}}
    //}}}
    //* (slide-anim) {{{ */
    // {{{
    private static final float TABS_CONTAINER_DIMMED_ALPHA_MIN = 0.30f;
    private static final float        BG_VIEW_DIMMED_ALPHA_MIN = 0.01f;
    private static       float TABS_CONTAINER_DIMMED_SCALE_MIN = 0.90f;

    private static final long BAND_ANIM_DURATION_HIST = 150L;
    private static final long BAND_ANIM_DURATION_DRAG = 250L;
    private              long band_anim_duration      =  BAND_ANIM_DURATION_DRAG;

    private       AnimatorSet Anim_band_AnimatorSet   = null;

    private LinkedHashMap<Object, Boolean>  slide_band_anim_queue  = new LinkedHashMap<>();

    private        View                            band_to_animate = null;
    private        boolean                         band_to_open_or_fold;

    //}}}
    // slide_band_anim_queue_is_empty {{{
    private boolean slide_band_anim_queue_is_empty()
    {
        return slide_band_anim_queue.isEmpty();
    }
    //}}}
    // log_slide_band_anim_queue {{{
    private void log_slide_band_anim_queue()
    {
        int z = slide_band_anim_queue.size();
Settings.MOM(TAG_ANIM, "=== slide_band_anim_queue SIZE=["+z+"]");
        int i = 0;
        for(Map.Entry<Object, Boolean> entry : slide_band_anim_queue.entrySet())
        {
            band_to_animate      = (View)   entry.getKey  ();
            band_to_open_or_fold = entry.getValue();
            i += 1;
Settings.MOM(TAG_ANIM, " "+i+"/"+z+": "+ (band_to_open_or_fold ? "OPEN" : "FOLD") +":["+ get_view_name(band_to_animate)+"]");
        }
    }
    //}}}
    // slide_band_to_animate {{{
    private void slide_band_to_animate()
    {
        if(band_to_animate == null) return;

        String caller = "slide_band_to_animate";
//*ANIM*/Settings.MON(TAG_ANIM, caller, "band_to_animate: "+ get_view_name(band_to_animate) +" "+ (band_to_open_or_fold ? "OPEN" : "FOLD"));
//*ANIM*/Settings.MON(TAG_ANIM, caller, "......drag_band: "+ get_view_name(drag_band));

        // make sure animated view is visible {{{
        band_animation_before(caller);

        //}}}
        // SLIDE BAND ON SCREEN {{{
        // BAND WIDTH AND POSITION {{{
    //  int vis_W = dck_handle     .getWidth(); // (OLD)
        int vis_W = band_to_animate.getWidth(); // (NEW)
        int bnd_W = vis_W;
    //  int bnd_W = band_to_animate.getWidth();
    //  int bnd_X = (int)band_to_animate.getX();

        // assuming dck_handle.right has been uncovered enough to show this band area
    //  int vis_X = vis_W - bnd_W;                        // visible scroll position
        int vis_X = get_band_vis_X( band_to_animate );    // visible scroll position
        int hid_X = vis_X - bnd_W;// - show_band.getWidth(); // left shift to hide this band

        // move band from within [hid_X .. vis_X] range
    //  float fromX = band_to_animate.getX();
    //  if((fromX < hid_X) || (fromX > vis_X)) fromX = band_to_open_or_fold ? hid_X : vis_X;

        //}}}
        // BAND SLIDING {{{
        // move band to where it will be either [visible] or [hidden]
        float toX = (band_to_open_or_fold) ? vis_X : hid_X;

        //}}}
/* // is_view_showing {{{
        if( is_view_showing(dock_band) ) {
//ANIM//Settings.MOM(TAG_ANIM, "BAND_DIMM.....f(maintain dimmed state when dock_band is showing)");
            toA = TABS_CONTAINER_DIMMED_ALPHA_MIN;
            toB =        BG_VIEW_DIMMED_ALPHA_MIN;
            toS = TABS_CONTAINER_DIMMED_SCALE_MIN;
        }
*/
// }}}
        //}}}
        // TABS VISIBILITY {{{
    //  boolean     tabs_container_freezed = (band_to_animate != hist_band) || !slide_band_anim_queue.isEmpty();
    //  boolean     tabs_container_freezed = band_to_open_or_fold;
        boolean     tabs_container_freezed = band_to_open_or_fold || (band_to_animate != hist_band);

        float toA = tabs_container_freezed ? TABS_CONTAINER_DIMMED_ALPHA_MIN : 1f;
        float toB = tabs_container_freezed ?        BG_VIEW_DIMMED_ALPHA_MIN : ((band_to_animate != hist_band) ? .5f : 1f);
        float toS = tabs_container_freezed ? TABS_CONTAINER_DIMMED_SCALE_MIN : 1f;

//*ANIM*/Settings.MOM(TAG_ANIM, "BAND_BACK_ALPHA toB=["+toB+"]"); // bg_view alpha
//*ANIM*/Settings.MOM(TAG_ANIM, "BAND_TABS_ALPHA toA=["+toA+"]"); //    tabs alpha
//*ANIM*/Settings.MOM(TAG_ANIM, "BAND_TABS_SCALE toS=["+toS+"]"); //    tabs scale
        //}}}
        // 1/2 - ANIMATED SLIDE {{{
        if( ANIM_SUPPORTED ) {
            Anim_band_AnimatorSet = new AnimatorSet();

            Anim_band_AnimatorSet.setDuration( band_anim_duration );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                Anim_band_AnimatorSet.setInterpolator(
                        band_to_open_or_fold
                        ? new AccelerateInterpolator()
                        : new DecelerateInterpolator()
                        );
            }

            AnimatorSet.Builder builder =
            Anim_band_AnimatorSet
            //  .play(ObjectAnimator.ofFloat(band_to_animate, View.X         , fromX, toX))
                .play(ObjectAnimator.ofFloat(band_to_animate, View.X                , toX))
                .with(ObjectAnimator.ofFloat(band_to_animate, View.ROTATION_Y       ,  0F))
                ;

            builder.with(ObjectAnimator.ofFloat(bg_view        , View.ALPHA            , toB));
            builder.with(ObjectAnimator.ofFloat(vsv            , View.ALPHA            , toA));
            builder.with(ObjectAnimator.ofFloat(vsv            , View.SCALE_X          , toS));
            builder.with(ObjectAnimator.ofFloat(vsv            , View.SCALE_Y          , toS));

            Anim_band_AnimatorSet.addListener( band_animation_end_listener ); // what to do once this animation is done

            Anim_band_AnimatorSet_interrupted = false;
            Anim_band_AnimatorSet.start();

        }
        //}}}
        // 2/2 - OR INSTANT SLIDE {{{
        else {
            band_to_animate.setX        ( toX );
            band_to_animate.setRotationY(  0F );

            bg_view.setAlpha ( toB );
            vsv    .setAlpha ( toA );
            vsv    .setScaleX( toS );
            vsv    .setScaleY( toS );

            band_animation_after(caller);
        }
        //}}}
    }
    //}}}
    // band_animation_before {{{
    private void band_animation_before(String caller)
    {
        caller += "->band_animation_before";
//*ANIM*/String band_name = get_view_name(band_to_animate);//TAG_ANIM
//*ANIM*/String     h_x_v = String.format("HID=[%4d] ... x=[%4d] ... VIS=[%4d]", get_band_hid_X( band_to_animate ), (int)band_to_animate.getX(), get_band_vis_X( band_to_animate ));//TAG_ANIM
//*ANIM*/Settings.MON(TAG_ANIM, caller, "band_to_animate=["+ band_name +"] "+h_x_v);

        band_to_animate.setVisibility( View.VISIBLE );

        if(band_to_animate == hist_band)
        {
//*ANIM*/Settings.MOM(TAG_ANIM, "...calling Handle.Collapse_all_instances()");
            Handle.Collapse_all_instances();
        }
    }
    //}}}
    // band_animation_end_listener  {{{
    private boolean Anim_band_AnimatorSet_interrupted = false;

    private final AnimatorListenerAdapter band_animation_end_listener = new AnimatorListenerAdapter()
    {
        @Override public void onAnimationEnd(Animator animation) {
//*ANIM*/Settings.MOC(TAG_ANIM, "band_animation_end_listener.onAnimationEnd");
            band_animation_after("band_animation_end_listener");
        }
        @Override public void onAnimationCancel(Animator animation)
        {
//*ANIM*/Settings.MOC(TAG_ANIM, "band_animation_end_listener.onAnimationCancel");
            Anim_band_AnimatorSet_interrupted = true;
        }
    };
    //}}}
    // band_animation_after {{{
    private void band_animation_after(String caller)
    {
        caller += "->band_animation_after";

        if( !band_to_open_or_fold ) {
//*ANIM*/Settings.MON(TAG_ANIM, caller, "HIDING BAND ["+ get_view_name( band_to_animate ) +"]");
            if(band_to_animate != null)
                band_to_animate.setVisibility( View.GONE );
        }
        handler.re_postDelayed( hr_band_animation_after, WEBVIEW_ANIMATION_TO_SYNC_VISIBILITY_DELAY);
    }

    private final Runnable hr_band_animation_after = new Runnable() {
        @Override public void run() {
            slide_band_animation_after_chain();
        }
    };
    //}}}
    // slide_band_animation_after_chain {{{
    private void slide_band_animation_after_chain()
    {
        // ALL DONE {{{
        String caller = "slide_band_animation_after_chain";
//*ANIM*/String                         band_name  = get_view_name(band_to_animate);//TAG_ANIM
//*ANIM*/String                         band_move  = band_to_open_or_fold ? "OPENED >>>"+band_name : band_name+"<<< HIDDEN";//TAG_ANIM
//*ANIM*/if(band_to_animate != null)    band_move += String.format(" HID=[%4d] ... x=[%4d] ... VIS=[%4d]", get_band_hid_X( band_to_animate ), (int)band_to_animate.getX(), get_band_vis_X( band_to_animate ));//TAG_ANIM
//*ANIM*/Settings.MON(TAG_ANIM, caller, band_move);
//*ANIM*/Settings.MOM(TAG_ANIM, "slide_band_anim_queue SIZE=["+ slide_band_anim_queue.size() +"]");

        if(band_to_animate == null)
        {
          //sync_tabs_scrolling(caller);
            sync_GUI_colors(caller);

            return;
        }
        //}}}
        // ADJUST [band_to_animate] [VIS] or [HIDDEN] position {{{
        // (i.e. parent layout may have changed since animation objective)
        if( band_to_open_or_fold )
        {
            if( !is_dragBand_opened(band_to_animate) )
            {
//*ANIM*/Settings.MOM(TAG_ANIM, "...ADJUSTING OPENED POSITION");
                band_to_animate.setX( get_band_vis_X( band_to_animate ) );
            }
        }
        else {
            if( !is_dragBand_hidden(band_to_animate) )
            {
//*ANIM*/Settings.MOM(TAG_ANIM, "...ADJUSTING HIDDEN POSITION");
                band_to_animate.setX( get_band_hid_X( band_to_animate ) );
            }
        }
        //}}}
        // [show_band_show] {{{
//*ANIM*/trace_views(TAG_ANIM, caller);
        if(        !Settings.is_GUI_TYPE_HANDLES()
                && !show_band_hidden
                && !is_view_showing(cart_band)
                && !is_view_showing(show_band)
          )
        {
//*ANIM*/Settings.MOM(TAG_ANIM, "RESTORE MISSING SHOWBAND");
            show_band_show(caller);
        }
        // }}}
        // FROM [band_to_animate] TO THE NEXT {{{
        if(band_to_animate != null)
        {
            // [Anim_band_AnimatorSet_interrupted] {{{
//*ANIM*/band_move += String.format(": X=[%3d] Y=[%3d]", (int)band_to_animate.getX(), (int)band_to_animate.getY());
            if( Anim_band_AnimatorSet_interrupted )
            {
//*ANIM*/Settings.MOM(TAG_ANIM, "NOT REMOVING INTERUPTED -- "+band_move + " from slide_band_anim_queue");
//*ANIM*/Settings.MOM(TAG_ANIM, "...as it has been interrupted by a flipped [OPEN<->CLOSE] request");
            }
            //}}}
            else {
                // [slide_band_anim_queue.remove] {{{
//*ANIM*/Settings.MOM(TAG_ANIM, "... REMOVING  DONE-BAND -- "+band_move + " from slide_band_anim_queue");
                slide_band_anim_queue.remove( band_to_animate );

                //}}}
                // [gesture_consumed_by_onFling] {{{
                if(        (gesture_consumed_by_onFling!=null)
                        && !is_fg_view_showing()
                        // !is_view_showing( mid_handle )
                        // !is_mid_handle_current()
                        )
                {
//*ANIM*/Settings.MON(TAG_ANIM,caller, "...calling show_PROFHIST_TABLE .. (gesture_consumed_by_onFling="+gesture_consumed_by_onFling+")");

                    //drag_band_hide( caller );

                    //if(is_band_fling_5_band_down) show_PROFHIST_TABLE(caller);
                    //if(is_band_fling_4_band_up  ) show_CONTROLS_TABLE(caller);

                    show_PROFHIST_TABLE(caller);
                }
                //}}}
            }
        }
        //}}}
        // LAUNCH NEXT ANIMATION IN QUEUE {{{

        slide_next_band(caller);
        //}}}
    }
    //}}}
    //}}}
    //* (anim chain) {{{ */
    // open_band {{{
    private final Runnable hr_open_dock     = new Runnable() { @Override public void run() { open_band (dock_band, "hr_open_dock"    ); } };
    private final Runnable hr_open_histBand = new Runnable() { @Override public void run() { open_band (hist_band, "hr_open_histBand"); } };
    private final Runnable hr_open_cartBand = new Runnable() { @Override public void run() { open_band (cart_band, "hr_open_cartBand"); } };

    private void open_band(View band, String caller)         {                               slide_band(     band, true , caller+"] [open_band"); }
    //}}}
    // fold_band {{{
    private final Runnable hr_fold_dock     = new Runnable() { @Override public void run() { fold_band (dock_band, "hr_fold_dock"    ); } };
    private final Runnable hr_fold_histBand = new Runnable() { @Override public void run() { fold_band (hist_band, "hr_fold_histBand"); } };
    private final Runnable hr_fold_cartBand = new Runnable() { @Override public void run() { fold_band (cart_band, "hr_fold_cartBand"); } };

    private void fold_band(View band, String caller)         {                               slide_band(     band, false, caller+"] [fold_band"); }
    //}}}
    // slide_band {{{
    private void slide_band(View band, boolean open_or_fold, String caller)
    {
        caller += "->slide_band("+ get_view_name(band)+")";
//*ANIM*/Settings.MOC(TAG_ANIM, caller);
        // ...already on it {{{
        if((band_to_animate == band) && (open_or_fold == band_to_open_or_fold))
        {
//*ANIM*/Settings.MOM(TAG_ANIM, "...already on it");
            return;
        }
        // }}}
        // cancel pending scheduled asynchronous call {{{
        un_schedule_release_dragBand(caller);

        //  }}}
        // currently ongoing animation for that same band {{{
        if((band_to_animate == band) && (Anim_band_AnimatorSet != null))
        {
            // keep on with same-direction animation {{{
            if(open_or_fold == band_to_open_or_fold) {
//*ANIM*/Settings.MOM(TAG_ANIM, "@@@ KEEP ON WITH CURRENT SAME-DIRECTION ANIMATION");
            }
            //}}}
            // reverse wrong-direction animation {{{
            else {
//*ANIM*/Settings.MOM(TAG_ANIM, "@@@ INTERRUPT CURRENT WRONG-DIRECTION ANIMATIOIN");
                Anim_band_AnimatorSet.cancel();
                Anim_band_AnimatorSet = null;

                // queue this band to animate with the (changed open_or_fold)
                slide_band_anim_queue.put(band, open_or_fold);

                // and resume the interrupted animation chain
                slide_next_band(caller+" (reverse wrong-direction animation)");
            }
            return;
        }
        //}}}
        else {
            // or queue next band to animate {{{
//*ANIM*/Settings.MOM(TAG_ANIM, "...queuing next band to animate");
            slide_band_anim_queue.put(band, open_or_fold);
            //}}}
            // ...and initiate animation chain .. if(none was in progress) {{{
            if(band_to_animate == null)
            {
//*ANIM*/Settings.MOM(TAG_ANIM, "...initiate animation chain");
                slide_next_band(caller+" (none was in progress)");
            }
            //}}}
        }
        // }}}
    }
    //}}}
    // slide_next_band {{{
    private void slide_next_band(String caller)
    {
        caller += "->slide_next_band";
//ANIM*/Settings.MOC(TAG_ANIM, caller);

        // no more band to animate {{{
        if( slide_band_anim_queue.isEmpty() )
        {
//*ANIM*/Settings.MON(TAG_ANIM, caller, "--- slide_band_anim_queue is EMPTY");
            band_to_animate = null;
          //sync_tabs_scrolling(caller);
            sync_GUI_colors(caller);
            return;
        }
//*ANIM*/log_slide_band_anim_queue();
        //}}}
        // 1/2 - next bands to fold {{{
        for(Map.Entry<Object, Boolean> entry : slide_band_anim_queue.entrySet())
        {
            band_to_animate      = (View)   entry.getKey  ();
            band_to_open_or_fold = entry.getValue();

            // pick first
            if( !band_to_open_or_fold )
            {
//*ANIM*/Settings.MOM(TAG_ANIM, "---...next bands to FOLD:["+ get_view_name(band_to_animate) +"]");
                slide_band_to_animate();
                return;
            }
        }
        //}}}
        // 2/2 - next bands to open {{{
        for(Map.Entry<Object, Boolean> entry : slide_band_anim_queue.entrySet())
        {
            band_to_animate      = (View)   entry.getKey  ();
            band_to_open_or_fold = entry.getValue();
            if( band_to_open_or_fold )
            {
//*ANIM*/Settings.MOM(TAG_ANIM, "---...next bands to OPEN:["+ get_view_name(band_to_animate) +"]");
                slide_band_to_animate();
                return;
            }
        }
        //}}}
    }
    //}}}
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** DOCK (PROFILE HIST CART) */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ DOCK @ {{{
    // {{{
    private String[] profiles_Dict_names             = null;
    private int      dock_scroll_visited_profile_num = 0;
    private int      dock_scroll_working_profile_num = 0;

    // }}}
    // dock_scroll_reset {{{
    private void dock_scroll_reset()
    {
        profiles_Dict_names             = null;
        dock_scroll_visited_profile_num = 0;
        dock_scroll_working_profile_num = 0;
    }
    //}}}
    // dock_scroll_to_working_profile {{{
    public void dock_scroll_to_working_profile(String caller)
    {
        caller += "->dock_scroll_to_working_profile";
//*BAND*/Settings.MON(TAG_BAND, caller, "Working_profile=["+Settings.Working_profile+"]");

        //noinspection PointlessBitwiseExpression
        blink_view(dock_band, (Color.WHITE) & 0x20FFFFFF);

//        // ALREADY THERE {{{
// YES BUT!!! happens to not being scrolled where it should be on startup
//        if(        (profiles_Dict_names != null)
//                && (dock_scroll_visited_profile_num == dock_scroll_working_profile_num)
//          )
//            return;
//
//        //}}}

        NpButton np_button = this_RTabsClient.get_DOCK_Map_NpButton( Settings.Working_profile );
        if(np_button != null)
            dock_band.scroll_container_sv_toY((int)np_button.getY(), 250);

        // POINT [dock_scroll_working_profile_num] TO [Working_profile] {{{
        dock_scroll_working_profile_num = 0;
        profiles_Dict_names = Profile.Get_Profiles_Dict_names();
        if(profiles_Dict_names != null)
        {
            int                  len = profiles_Dict_names.length;
            for(int num=0; num < len; ++num)
            {
                if(profiles_Dict_names[num].equals( Settings.Working_profile ))
                {
                    dock_scroll_working_profile_num = num;
                    break;
                }
            }
        }
        dock_scroll_visited_profile_num = dock_scroll_working_profile_num;
        //}}}
    }
    //}}}
    // dock_scroll_to_visited_profile {{{
    private void dock_scroll_to_visited_profile(int direction, String caller)
    {
        caller += "->dock_scroll_to_visited_profile("+direction+")";
        // CHECK NAMES AND CONTAINER {{{
        if(profiles_Dict_names == null)
        {
            dock_scroll_to_working_profile(caller);

            return;
        }
        if(dock_band.container.getChildCount() < 1)
        {
//*BAND*/Settings.MON(TAG_BAND, caller, "(dock_band.container.getChildCount() < 1)");
            return;
        }
        //}}}
        // START FROM PREVIOUS CALL POSITION {{{
        // (container dimension) (child dimension) (top-down) (bottom-up) {{{
        int    p_from = (direction < 0) ? profiles_Dict_names.length-1 : 0; // search bottom-up
        int      p_to = (direction > 0) ? profiles_Dict_names.length-1 : 0; // search top-down
        int      incr =  direction;
        int nb_height = dock_band.container.getChildAt( 0).getHeight();
        int   scrollY = dock_band.get_container_sv_ScrollY();
        int   scrollH = dock_band.container_sv.getHeight();

        //}}}
        // check top and bottom button for their visited and index qualification {{{
        int top_button_middle = scrollY +           nb_height/2;
        int bot_button_middle = scrollY + scrollH - nb_height/2;
        NpButton   top_button = dock_band.get_dock_button_at_Y( top_button_middle );
        NpButton   bot_button = dock_band.get_dock_button_at_Y( bot_button_middle );
//*BAND*/Settings.MON(TAG_BAND, caller, "top_button=["+top_button+"] bot_button=["+bot_button+"]");

        // }}}
        // TOP SEARCH & TOP_BUTTON NOT QUALIFIED .. scroll from top {{{
        int scan_belowY=0, scan_aboveY=0;
        if(direction > 0) {
            // top button qualifies .. (bunch shown DOWN FROM THE TOP)
            if(        (   top_button != null)
                    && (   top_button.is_a_visited_profile_button()
                        || top_button.is_an_index_profile_button ())
              )
                scan_belowY = scrollY + scrollH - nb_height - nb_height/2; // ...skip this bunch ...continue search below the visible bunch
            else
                scan_belowY = top_button_middle; // ....or search from the top of the visible bunch ...moving the first that qualifies to the top
        }
        else {
            // bot button qualifies .. (bunch shown UP FROM THE BOTTOM)
            if(        (   bot_button != null)
                    && (   bot_button.is_a_visited_profile_button()
                        || bot_button.is_an_index_profile_button ())
              )
                scan_aboveY  = scrollY +                       nb_height/2; // .. from within   top button
            else
                scan_aboveY  = bot_button_middle; // ............................. or search from top .. moving the first found to the top
        }

        // }}}
        // SELECT SCAN START POINT {{{
        // TOWARDS TOP: may pick the    top one .. scrolling it to the bottom
        // TOWARDS BOT: may pick the bottom one .. scrolling it to the top

        NpButton np_button = null;
        int p_num = 0;
        for(p_num = p_from; p_num != p_to; p_num += incr)
        {
            np_button = this_RTabsClient.get_DOCK_Map_NpButton( profiles_Dict_names[p_num] );
            if(        (   np_button != null)
                    && (   np_button.is_a_visited_profile_button()  // visited
                        || np_button.is_an_index_profile_button ()) // or index
                    && (  (direction < 0)
                        ? (np_button.getY() <  scan_aboveY)         // pick the first found
                        : (np_button.getY() >= scan_belowY))        // in the right direction
              )
                break;
            else
                np_button = null;                                   // this one does not qualify...
        }
        // }}}
//*BAND*/if(np_button != null) Settings.MOM(TAG_BAND, "...profile #"+p_num+" ["+profiles_Dict_names[p_num]+"]");
//*BAND*/else                  Settings.MOM(TAG_BAND, "...found no visited or index profile in that direction");

        // }}}
        // SHOW WE ARE ON CURRENT WORKING PROFILE {{{
        if(dock_scroll_visited_profile_num == dock_scroll_working_profile_num)
        {
            //noinspection PointlessBitwiseExpression
            blink_view(((np_button != null) ? np_button : dock_band), Color.WHITE & 0x40FFFFFF);

        }
        // }}}
        // SCROLL TO (VISITED) OR (INDEX BUTTON) OR (LIST END) {{{

        // when scrolling towards bot: (move found button near top)
        scrollY = (np_button != null)
            ?      (int)np_button.getY()                                    // (selected profile) MOVE np_button-top-pixel TO THE TOP
            :      ((direction < 0) ? 0 : dock_band.container.getHeight()); // OR (top) or (bottom) f(direction)

        // when scrolling towards top: (move found button near bottom)
        if((direction < 0) && (np_button != null))
            scrollY -= dock_band.container_sv.getHeight() - np_button.getHeight(); // (selected profile) MOVE np_button-bot-pixel TO THE BOTTOM

        //}}}
        // DO THE SROLLING {{{
        int scroll_duration = Math.abs(p_num - dock_scroll_visited_profile_num) / 10 * 250; // pages * per-page-scroll-time
        scroll_duration     = Math.max(scroll_duration, 100);
        scroll_duration     = Math.min(scroll_duration, 750);
        dock_band.scroll_container_sv_toY(scrollY, scroll_duration);

        //}}}
        // BLINK WHEN REACHED THE END IN THAT DIRECTION  .. (found none or the same button) {{{
        if(np_button == null)
        {
            blink_view(dock_band, ((direction < 0) ? Color.RED : Color.BLUE) & 0x60FFFFFF, 750);
        }
        else if(p_num == dock_scroll_visited_profile_num)
        {
            blink_view(((np_button != null) ? np_button : dock_band), ((direction < 0) ? Color.RED : Color.BLUE) & 0x60FFFFFF, 500);
        }
        else {
            dock_scroll_visited_profile_num = p_num; // next call will start from here
        }
        // }}}
    }
    //}}}
    /** HIST */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ {{{
    // update_histBand - create, update and display hist_band.prof_nb {{{
    public void update_histBand(String caller)
    {
        caller += "->update_histBand";
//*ANIM*/Settings.MOC(TAG_ANIM, caller);
        // CURRENT PROFILE (vertical profile name) {{{
        String text = Settings.SYMBOL_MENU      +"\n\n";
        int              len = Settings.Working_profile.length();
        for(int i=0; i < len; ++i)
            text += Settings.Working_profile.charAt(i)+"\n";
        text = text.replace("/", " "+ Settings.SYMBOL_BLACK_CIRCLE +" ");

        hist_band.prof_nb.setText( text );
        // }}}
        // PROFILE HISTORY {{{
        if(this_RTabsClient != null)
        {
            // BUTTON [back] {{{
            int back = this_RTabsClient.history_back_size();
            if( back > 0) {
                hist_band.back_nb.setActive   ( true);
                hist_band.back_nb.setTextNoFit(back + Settings.SYMBOL_PRPREV);
                hist_band.back_nb.setTag      (       Settings.BACK_NB_INFO+"\n"+this_RTabsClient.history_back_list());

if(D||M) Settings.MON(TAG_PROFILE, "hist_band.back_nb.getTag()", (String)hist_band.back_nb.getTag());
            }
            else {
                hist_band.back_nb.setActive   (false);
                hist_band.back_nb.setTextNoFit(       Settings.SYMBOL_PRPREV);
            }
            // }}}
            // BUTTON [frwd] {{{
            int frwd = this_RTabsClient.history_frwd_size();
            if( frwd > 0) {
                hist_band.frwd_nb.setActive   ( true);
                hist_band.frwd_nb.setText     (frwd + Settings.SYMBOL_PRNEXT);
                hist_band.frwd_nb.setTag      (       Settings.FORE_NB_INFO+"\n"+this_RTabsClient.history_frwd_list());

if(D||M) Settings.MON(TAG_PROFILE, "hist_band.frwd_nb.getTag()", (String)hist_band.frwd_nb.getTag());
            }
            else {
                hist_band.frwd_nb.setActive   (false);
                hist_band.frwd_nb.setText     (       Settings.SYMBOL_PRNEXT);
            }
            // }}}
            invalidate_profile_handles(caller);
        }
        //}}}
    }
    //}}}
    // history_back {{{
    private void history_back(int fromSource, String caller)
    {
        caller += "->history_back("+get_fromSource(fromSource)+")";
//*BAND*/Settings.MOC(TAG_BAND, caller);

        //Handle.UnscheduleDimm();
        pulse_np_button( hist_band.back_nb );
        if(get_Working_profile_pending_changes() > 0) {
            check_current_profile_pending_changes_then_load_profile( RTabsClient.HISTORY_BAK );
        }
        else {
            this_RTabsClient.history_back();
            show_tabs_container_unfreezed(caller);
        }
        show_histBand(fromSource, caller);
    }
    //}}}
    // history_frwd {{{
    private void history_frwd(int fromSource, String caller)
    {
        //Handle.UnscheduleDimm();
        pulse_np_button( hist_band.frwd_nb );
        if(get_Working_profile_pending_changes() > 0) {
            check_current_profile_pending_changes_then_load_profile( RTabsClient.HISTORY_FWD );
        }
        else {
            this_RTabsClient.history_frwd();
            show_tabs_container_unfreezed(caller);
        }
        show_histBand(fromSource, "history_frwd");
    }
    //}}}
    // history_back_clear {{{
    private void history_back_clear(int fromSource)
    {
        //Handle.UnscheduleDimm();
        raise_np_button          ( hist_band.back_nb );
        this_RTabsClient.history_back_clear();
        update_histBand       ("history_back_clear");
        show_histBand(fromSource, "history_back_clear");
    }
    //}}}
    // history_frwd_clear {{{
    private void history_frwd_clear(int fromSource)
    {
        //Handle.UnscheduleDimm();
        raise_np_button          ( hist_band.frwd_nb );
        this_RTabsClient.history_frwd_clear();
        update_histBand       ("history_frwd_clear");
        show_histBand(fromSource, "history_frwd_clear");
    }
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** CART */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ {{{
    // sync_cartBand {{{
    public void sync_cartBand(String caller)
    {
        caller += "->sync_cartBand";
//*BAND*/Settings.MOC(TAG_BAND, caller);

        if(this_RTabsClient == null) return;

        // UPDATE CART UI CONTENTS
        cart_band.see_nb.setTextNoFit( ""+this_RTabsClient.get_cart_size() );

        // DISABLE AND FALLBACK [ADD]->[DEL] .. f(empty cart)
        if(this_RTabsClient.get_cart_size() <= 0)
        {
            cart_band.see_nb.setEnabled(false); // no use to switch from [DEL] to [ADD] with an [ empty cart]
            cart_band.see_nb.setActive (false); // ...(show !enabled state)
            cart_band.see_nb.setBackgroundColor( Settings.DEL_NP_COLOR );
        }
        // ENABLE SWITCHING [DEL]<->[ADD] .. f(!empty cart)
        else {
            cart_band.see_nb.setEnabled( true); // OK ... to switch from [DEL] to [ADD] with an [!empty cart]
            cart_band.see_nb.setActive ( true); // ...(show +enabled state)
        }

    }
    //}}}

    /** CLICK */
    // cart_see_click {{{
    private void cart_see_click()
    {
        String caller = "cart_see_click";
//*BAND*/Settings.MOC(TAG_BAND, caller);

        pulse_np_button( cart_band.see_nb );

        // CLICK ON ENABLED BUTTON
        // NOTE:
        // only user can toggle from [DEL TO CART] to [ADD FROM CART]
        // ..... switching back from [ADD TO CART] to [ADD FROM CART]
        // ..... can be both from USER or
        // ..... from cart getting empty fallback
        // ..... when sync_cartBand is called by RTabsClient
        // ..... from cart_save_from or cart_extract_to

        boolean  may_switch_from_del_to_add = (this_RTabsClient.get_cart_size() > 0) && (cart_band.see_nb.getBackgroundColor() != Settings.ADD_NP_COLOR);
        boolean  may_switch_from_add_to_del =  !may_switch_from_del_to_add           && (cart_band.see_nb.getBackgroundColor() != Settings.DEL_NP_COLOR);

        if     ( may_switch_from_del_to_add ) cart_band.see_nb.setBackgroundColor( Settings.ADD_NP_COLOR );
        else if( may_switch_from_add_to_del ) cart_band.see_nb.setBackgroundColor( Settings.DEL_NP_COLOR );

        // UPDATE CART UI
        sync_cartBand("cart_see_click");
    }
    //}}}
    // cart_see_longClick {{{
    public void cart_see_longClick()
    {
        String caller = "cart_see_longClick";
//*BAND*/Settings.MOC(TAG_BAND, caller);

        raise_np_button( cart_band.see_nb );

        // LONG CLICK .. CLEAR-CART
        this_RTabsClient.cart_clear();

        // UPDATE CART UI
        sync_cartBand("cart_see_longClick");
    }
    //}}}
    // cart_end_click {{{
    private void cart_end_click(int fromSource)
    {
        String caller = "cart_end_click";
//*BAND*/Settings.MOC(TAG_BAND, caller);
        pulse_np_button( cart_band.end_nb );
        //show_histBand(fromSource, "cart_end_click");
        show_band_hidden = false; // TODO settle for an accessor
        fold_band(cart_band, caller);
    }
    //}}}

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** CYCLE */
    // {{{
    // band visibility {{{
    private static final int RELEASE_BAND_TO_CLOSEST_STATE = 0;
    private static final int RELEASE_BAND_TO_VISIBLE_STATE = 1;
    private static final int RELEASE_BAND_TO_HIDDEN_STATE  = 2;

    private View    drag_band;
    private boolean dragBand_moved;         // WHEN TRUE: HANDLE RELEASE TO CLOSEST STATE
    private boolean dragBand_scrolled;      // WHEN TRUE: DO NOT RELEASE ON VERTICAL GESTURE
    private boolean dragBand_was_hiding;    // WAS HIDING: EV7_RT_FL UNHIDES .. IF NOT: MAKE THE NEXT BAND VISIBLE

    // NOTE: (dck_handle is a RelativeLayout subclass, as such, its size depends on its current children layout)
    //             ooooooooooooooooooo
    //             o    SCREEN       o
    //  o==========o============o    o
    //  |          o            |    o
    //  |   BAND   o    BAND    |    o
    //  | HIDDEN <-o->  VISIBLE |    o
    //  |          o            |    o
    //  o==========o============o    o
    //             o                 o
    //             ooooooooooooooooooo

    private boolean is_band_fully_visible(View band) { return (band != null) && (band.getVisibility() == View.VISIBLE) && ((int)band.getX() >= get_band_vis_X( band )); }
    private boolean is_band_fully_hidden (View band) { return (band != null)                                           && ((int)band.getX() <= get_band_hid_X( band )); }
    private boolean is_band_visible      (View band) { return (band != null) && (band.getVisibility() == View.VISIBLE) && ((int)band.getX() >  get_band_hid_X( band )); }
    private boolean is_band_hidden       (View band) { return (band != null)                                           && ((int)band.getX() <  get_band_vis_X( band )); }

//  private int     get_band_vis_X  (View band) { return dck_handle.getWidth()  -   band.getWidth(); }
//  private int     get_band_hid_X  (View band) { return dck_handle.getWidth()  - 2*band.getWidth() - Settings.BAND_HIDE_WIDTH; }
//  private int     get_band_vis_X  (View band) { return (band.getParent() == frameLayout) ? Settings.BAND_HIDE_WIDTH : dck_handle.getWidth() -band.getWidth()  ; }
//  private int     get_band_hid_X  (View band) { return (band.getParent() == frameLayout) ? -band.getWidth()         : dck_handle.getWidth() -band.getWidth()*2; }
    private int     get_band_vis_X  (View band) { return Settings.BAND_HIDE_WIDTH                 ; }
    private int     get_band_hid_X  (View band) { return Settings.BAND_HIDE_WIDTH -band.getWidth(); }

    // get_band_vis_fraction {{{
    private float   get_band_vis_fraction(View band)
    {
        int     w = band.getWidth();            // band width
        int     x = (int)band.getX();           // current left

        int right = x + w;                      // current right
        int   v_X = get_band_vis_X(band);       // visible left

        float ratio
            =  (float)(right - v_X)             // ....  part-visible
            /  (float)(w          )             // over plain-visible ratio
            ;

        if(ratio < 0) ratio = 0;
        if(ratio > 1) ratio = 1;

        return ratio;
    }
    //}}}

    // }}}
    // drag_band_hide {{{
    private void drag_band_hide(String caller)
    {
        caller += "->drag_band_hide";
/*
        if(hist_band != null) hist_band.setX( get_band_hid_X( hist_band ) );
        if(dock_band != null) dock_band.setX( get_band_hid_X( dock_band ) );
        clear_dragBand( caller );
*/
//*BAND*/Settings.MOM(TAG_BAND, "BAND_HIDE......[both DOCK and HIST] .. f(dragBand_moved "+dragBand_moved+")");
        clear_dragBand(caller);
        if(dock_band != null) fold_band(dock_band, caller);
        if(hist_band != null) fold_band(hist_band, caller);
    }
    //}}}
    // clear_dragBand {{{
    private void clear_dragBand(String caller)
    {
        caller += "->clear_dragBand";

        // REPORT: NOTHING TO CLEAR
        if(drag_band == null)
        {
//*BAND*/Settings.MON(TAG_BAND, caller, "DRAG_CLEAR: ["+ get_view_name(drag_band) +"]");
            return;
        }

        // REPORT: LAST DRAGGING TRANSITION
        String was_state    = (dragBand_was_hiding             ? "HIDDEN" : "SHOWING");
        String cur_state    = (is_band_fully_hidden(drag_band) ? "HIDDEN" : "SHOWING");
        String result       = "DRAG_CLEAR: ["+ get_view_name(drag_band) +"]: was ["+ was_state +"] ...is ["+ cur_state +"]";

        // RESET DRAGGING DATA
        dragBand_moved      = false;
        dragBand_scrolled   = false;
        dragBand_was_hiding = false;
        drag_band           =  null;

//*BAND*/Settings.MON(TAG_BAND, caller, result);
    }
    //}}}

    // select_dragBand {{{
    private void select_dragBand(int x, String caller)
    {
        caller += "->select_dragBand(x=["+x+"])";
//*BAND*/Settings.MOC(TAG_BAND, caller);
        String result = Settings.EMPTY_STRING;
        // TOUCH-LEFT REQUIRED .. f(NO BAND SHOWING) {{{
//*BAND*/Settings.MOM(TAG_BAND, "...........drag_band="+ get_view_name(drag_band));
//*BAND*/Settings.MOM(TAG_BAND, "...dock_band_showing="+ is_view_showing(dock_band));
//*BAND*/Settings.MOM(TAG_BAND, "...hist_band_showing="+ is_view_showing(hist_band));
        if(!is_view_showing(dock_band) && !is_view_showing(hist_band))
        {
        //  if     (is_view_showing(dock_band)) band_selection_X += dock_band.getWidth();
        //  else if(is_view_showing(hist_band)) band_selection_X += hist_band.getWidth();
        //  int    band_selection_max_X = dck_handle.getWidth(); // that's too large for hist_band
        //  int    band_selection_max_X = hist_band .getWidth(); // may happen to be 0
            int    band_selection_max_X = Settings.BAND_HIDE_WIDTH + Handle.Get_DOCK_STANDBY_WIDTH();
            if(x > band_selection_max_X ) result += " OUT-OF-HIST-BAND TOUCH: (x="+x+" > band_selection_max_X="+band_selection_max_X+")";
        }
        //}}}
        // THEN CLEAR drag_band SELECTION {{{
        if(drag_band != null) {
            clear_dragBand(caller);
        }
        //}}}
        // CHECK REJECTIONS: (DOCKED_TYPE + [CART or HIST SHOWING]) or (tabs_container left-scroll possible) or (DOCK SHOWING) or (HIST SHOWING) {{{
        if( !Settings.is_GUI_TYPE_HANDLES() )
        {
            if     ( is_view_showing(cart_band)                                     ) result += " [CART_SHOWING]";
            else if( is_band_fully_visible(hist_band) && !is_view_showing(show_band)) result += " [HIST_PINNED]";
        }
        else if(!is_at_left_margin() && is_tabs_scrolling()                         ) result += " [TABS_SCROLLING]";
        else if( is_view_showing(dock_band)) { drag_band = dock_band;                 result += " [dock_band_showing]"; }
        else if( is_view_showing(hist_band)) { drag_band = hist_band;                 result += " [hist_band_showing]"; }

        // }}}
        // CHOOSE ELIGIBLE BAND .. (dock) {{{
        if(result == Settings.EMPTY_STRING)
        {
            // DRAG BAND DEFAULT: f(GUI_TYPE):
            // ... [dock_band] .. f(GUI_TYPE_HANDLES)
            // ... [hist_band] .. f(GUI_TYPE_DOCKING)
            drag_band = (Settings.is_GUI_TYPE_HANDLES() || is_band_fully_visible(dock_band))
                ?        dock_band  // dock_band ........... is     [PART OF] BOTH........ GUI TYPES
                :        hist_band; // hist_band (as of now) is NOT [PART OF] HANDLES-ONLY GUI TYPE

            // drag start-state: [HIDING] or [SHOWING]
            if(drag_band != null) {
                dragBand_was_hiding = drag_band.getX() <= get_band_hid_X(drag_band);
                result = "["+ (dragBand_was_hiding ? "IS HIDDEN" : "IS SHOWING") +"]";
            }
        }
        // }}}

//*BAND*/Settings.MOM(TAG_BAND, "...DRAG_SELECT drag_band=["+get_view_name(drag_band) +"] ..."+result);
    }

    //}}}

    // schedule_release_dragBand {{{
    // ...used by MotionListener.onTouchEvent.ACTION_UP .. (that happens before MGestureListener.onFling)
    private void schedule_release_dragBand(String caller)
    {
        caller += "->schedule_release_dragBand";
//*BAND*/Settings.MOC(TAG_BAND, caller);
        handler.re_postDelayed(hr_release_dragBand, 100);
    }

    private void un_schedule_release_dragBand(String caller)
    {
        caller += "->un_schedule_release_dragBand";
//BAND*/Settings.MOC(TAG_BAND, caller);
        handler.removeCallbacks(hr_release_dragBand     );
    }

    //}}}
    //_ hr_release_dragBand {{{
    private final Runnable hr_release_dragBand = new Runnable() {
        @Override public void run() { release_dragBand("hr_release_dragBand"); }
    };
    // }}}
    // release_dragBand {{{
    private void release_dragBand(String caller)
    {
        caller += "->release_dragBand";
//*BAND*/Settings.MOC(TAG_BAND, caller);

//*BAND*/Settings.MOM(TAG_BAND, " ......................drag_band..: "+ get_view_name(drag_band)     );
//*BAND*/Settings.MOM(TAG_BAND, " ..................dragBand_moved.: "+ dragBand_moved               );
//*BAND*/Settings.MOM(TAG_BAND, " ...............dragBand_scrolled.: "+ dragBand_scrolled            );
//*BAND*/Settings.MOM(TAG_BAND, " ...gesture_consumed_by_onDown....: "+ gesture_consumed_by_onDown   );
//*BAND*/Settings.MOM(TAG_BAND, " ...gesture_consumed_by_ACTION_UP.: "+ gesture_consumed_by_ACTION_UP);
//*BAND*/Settings.MOM(TAG_BAND, " ...gesture_consumed_by_onFling...: "+ gesture_consumed_by_onFling  );

        if(  drag_band == null                ) return;
        if(gesture_consumed_by_onDown  != null) return;
        if(gesture_consumed_by_onFling != null) return;
        if( !dragBand_moved                   ) return;

        release_dragBand(RELEASE_BAND_TO_CLOSEST_STATE, caller);
    }
    //}}}
    // release_dragBand {{{
    private void release_dragBand(int state, String caller)
    {
        if(drag_band == null) return;

        boolean show_or_hide;
        String release_state;
        switch( state ) {
            default:
            case RELEASE_BAND_TO_CLOSEST_STATE: show_or_hide = is_dragBand_more_showing_than_hiding(); release_state = "CLOSEST="+(show_or_hide ? "VISIBLE" : "HIDDEN"); break;
            case RELEASE_BAND_TO_VISIBLE_STATE: show_or_hide =  true;                                  release_state = "VISIBLE"; break;
            case RELEASE_BAND_TO_HIDDEN_STATE : show_or_hide = false;                                  release_state = "HIDDEN" ; break;
             }
        caller += "->release_dragBand("+release_state+")";
//*BAND*/Settings.MOC(TAG_BAND, caller);

        // BAND SHOW
        if( show_or_hide )
        {
            if( !is_dragBand_opened(drag_band) )
                show_drag_band(caller);
        }
        // BAND HIDE
        else {
            //if( !is_dragBand_hidden(drag_band) ) hide_drag_band(caller);
            // [dock_band] .. either swap with hist or hide .. f(dragBand_moved) {{{
            if(     drag_band == dock_band)
            {
                // BAND_SWAP DOCK->HIST
                if( dragBand_moved ) {
//*BAND*/Settings.MOM(TAG_BAND, "BAND_SWAP......[from DOCK  to HIST] .. f(dragBand_moved "+dragBand_moved+")");
                    handler.re_postDelayed(hr_show_histBand, BAND_ANIM_DURATION_DRAG);
                }
                // BAND_HIDE BOTH
                else {
//*BAND*/Settings.MOM(TAG_BAND, "BAND_HIDE......[both DOCK and HIST] .. f(dragBand_moved "+dragBand_moved+")");
                    clear_dragBand(      caller);
                    fold_band(dock_band, caller);
                    fold_band(hist_band, caller);
                }
            }
            // }}}
            // OR hist-to-clear .. (anim takes care of the hiding) {{{
            else if(drag_band == hist_band)
            {
                clear_dragBand(caller);
            }
            // }}}
        }
    }
    //}}}
    //_ hr_show_histBand {{{
    private final Runnable hr_show_histBand = new Runnable()
    {
        @Override public void run()
        {
        //  handle_hide("release_dragBand");
        //  Handle.Set_cur_handle(null, "release_dragBand");
        //  Handle.Collapse_all_instances();
        //  Handle.Set_cur_handle(dck_handle, "release_dragBand");
        //  dck_handle.needs_layout(true);
        //  dck_handle.setX(-dck_handle.getWidth());
        //  handle_hide("release_dragBand");
        //  handle_set(lft_handle, "release_dragBand");
        //  sync_handles("release_dragBand");
            Handle.Collapse_all_instances();
            show_histBand(FROM_GESTURE, "release_dragBand");
        //  open_band(hist_band, "hr_show_histBand");
        }
    };
    //}}}

    // show_drag_band {{{
    private void show_drag_band(String caller)
    {
        caller += "->show_drag_band";
//*BAND*/Settings.MON(TAG_BAND, caller, "drag_band=["+get_view_name(drag_band)+"]");
        if(drag_band == dock_band) show_dock    (FROM_GESTURE, caller);
        if(drag_band == hist_band) show_histBand(FROM_GESTURE, caller);
    }
    //}}}
    // hide_drag_band {{{
    private void hide_drag_band(String caller)
    {
        caller += "->hide_drag_band";
//*BAND*/Settings.MON(TAG_BAND, caller, "drag_band=["+get_view_name(drag_band)+"]");
        if     (drag_band == dock_band) fold_band(dock_band, caller);
        else if(drag_band == hist_band) fold_band(hist_band, caller);
    }
    //}}}
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** MAGNIFY */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ MAGNIFY @ {{{
    //* [NoteHolder] */
    // interface NoteHolder {{{
    private interface NoteHolder
    {
        NotePane get_np();
        NpButton get_np_button();
        String   get_name();
        String   get_text();
        String   get_tag();
        String   get_shape();
        String   get_xy_wh();
        int      get_bg_color();
        int      get_fg_color();
        void     setValue(String text, String tag, String shape);
    }
    //}}}
    // fs_button_NoteHolder {{{
    private final NoteHolder fs_button_NoteHolder = new NoteHolder()
    {
        // get_np_button {{{
        public NpButton get_np_button()
        {
            Object o = fs_button.getTag();
            if(    o instanceof NpButton) return  (NpButton)o;
            if(    o instanceof NotePane) return ((NotePane)o).button;
            /*neihter one of those.....*/ return                 null;
        }
        //}}}
        // get_np {{{
        public NotePane get_np()
        {
            Object o = fs_button.getTag();
            if(    o instanceof NotePane)                                                     return (NotePane)o;
            if(    o instanceof NpButton) { o = ((View)o).getTag(); if(o instanceof NotePane) return (NotePane)o; }
            /*...neither a NotePane instance ...nor an Object tagged with a NotePane.......*/ return        null;
        }
        //}}}
        // get_name {{{
        public String get_name()
        {
            NotePane np = get_np();
            if(np == null) {
                String s = Settings.EMPTY_STRING;
                if( BuildConfig.BUILD_TYPE.equals("debug") )
                {
                    NpButton nb = get_np_button();
                    if(nb != null) s = get_view_name( nb );
                }
                return s;
            }
            if(np.tag.equals("NOTE") ) return "Personal Note";
            else                       return np.name;
        }
        //}}}
        // get_text {{{
        public String get_text()
        {
            String caller ="fs_button_NoteHolder.get_text";
            // NotePane.text {{{
            String text  = "";
            NotePane np  = get_np(); // (provided one has been tagged with fs_button_setTag(passed a NotePane instance))
            if(      np != null)
            {
                if( np.tag.equals("NOTE") ) {
                    text = Settings.LoadPersonalNote();
                }
                else {
                    // EDIT: (customizable) dynamic-label (i.e. derived-default from NotePane.tag)
//DIALOG*/Settings.MON(TAG_DIALOG, caller, "XXX np.text=["+np.text+"]");
                    text = np.getLabelWithInfo();
//DIALOG*/Settings.MON(TAG_DIALOG, caller, "XXX np.getLabelWithInfo(): ["+text+"]");
                    text = Settings.text_markdown(np.tag, text);
                }
//*DIALOG*/Settings.MON(TAG_DIALOG, caller, "...return NotePane["+Settings.ellipsis(np.tag, 32)+"] text=["+Settings.ellipsis(text, 32)+"]");
            }
            // }}}
            // NpButton label {{{
            else {
                NpButton np_button  = get_np_button();
                text  = (np_button != null) ? np_button.getText().toString() : "";

                // APPEND INFO .. (provided some has been tagged with fs_button_setTag(passed a String))

                String fs_button_info   = null;
                if(np_button != null) {
                    Object o  = fs_button.getTag();
                    if(o instanceof String) fs_button_info = (String)o;
                }
                if(fs_button_info != null) text += fs_button_info;

                String np_button_info   = null;
                if(np_button != null) {
                    Object o  = np_button.getTag();
                    if(o instanceof String) np_button_info = (String)o;
                }
                if(np_button_info != null) text += np_button_info;

                text = Settings.text_markdown("", text); // a button-less NotePane has no tag
//*DIALOG*/Settings.MON(TAG_DIALOG, caller, "...return NpButton text=["+text+"]");
            }
            // }}}
            // Discard vertical-label formatting {{{
            if( NpButton.Is_text_vertical(text) ) {
                text = NpButton.Make_text_horizontal( text );
//*DIALOG*/Settings.MON(TAG_DIALOG, caller, "NpButton.Make_text_horizontal: text=["+text+"]");
            }
        // }}}
            if(D || Settings.LOG_CAT) Settings.MOM(TAG_DIALOG, caller+": text=["+text+"]");
            return text;
        }
        //}}}
        // get_tag {{{
        public String get_tag()
        {
            NotePane np  = get_np();
            return (np != null) ? np.tag  : "";
        }
        //}}}
        // get_shape {{{
        public String get_shape()
        {
            NotePane np = get_np();
            return (np != null) ? np.shape : "";
        }
        //}}}
        // get_xy_wh {{{
        public String get_xy_wh()
        {
            NotePane np = get_np();
            return (np != null) ? np.get_xy_wh() : "";
        }
        //}}}
        // get_bg_color {{{
        public int get_bg_color()
        {
            int bg_color = NotePane.NO_COLOR;
            // ...from np {{{
            NotePane np  = get_np();
            if(np != null) {
                if(   (np.bg_color != NotePane.NO_COLOR))
                {
                    if(np.bg_color != NotePane.NO_COLOR) bg_color =  np.bg_color;
                }
            }
            // }}}
            // ...from np_button {{{
            NpButton np_button = get_np_button();
            if(np_button != null) {
                if(bg_color == NotePane.NO_COLOR)
                {
                    bg_color = np_button.getBackgroundColor();
                }
            }
            // }}}
            return bg_color;
        }
        //}}}
        // get_fg_color {{{
        public int get_fg_color()
        {
            int fg_color = NotePane.NO_COLOR;
            // ...from np {{{
            NotePane np  = get_np();
            if(np != null) {
                if(np.fg_color != NotePane.NO_COLOR)
                {
                    if(np.fg_color != NotePane.NO_COLOR) fg_color =  np.fg_color;
                }
            }
            // }}}
            // ...from np_button {{{
            NpButton np_button = get_np_button();
            if(np_button != null) {
                if(fg_color == NotePane.NO_COLOR)
                {
                    fg_color = np_button.getTextColors().getDefaultColor();
                }
            }
            // }}}
            return fg_color;
        }
        //}}}
        // setValue {{{
        public void   setValue(String text_and_info, String tag, String shape)
        {
            String caller = "setValue";
//*DIALOG*/Settings.MOC(TAG_DIALOG, caller);
//*DIALOG*/Settings.MOM(TAG_DIALOG, "...............text_and_info=["+ text_and_info           +"]");
//*DIALOG*/Settings.MOM(TAG_DIALOG, ".........................tag=["+ tag                     +"]");
//*DIALOG*/Settings.MOM(TAG_DIALOG, ".......................shape=["+ shape                   +"]");

            // [fs_button] .. (update full screen text) {{{
            fs_button.setText(((text_and_info.length() < 32) || text_and_info.endsWith("\n")) ? text_and_info : text_and_info +"\n");
        //  fs_button.fitText();
        //  fs_button.invalidate();

            //}}}
            // (get_np_button) {{{
            NpButton np_button = get_np_button();
//*DIALOG*/Settings.MOM(TAG_DIALOG, "...................np_button=["+ np_button               +"]");

            if( wvTools.is_a_scrollbar( np_button ) )
            {
//*DIALOG*/Settings.MOC(TAG_DIALOG, "", "SCROLLBAR SELECTION: ["+text_and_info+"]");

                fs_button_hide(caller);
                np_button.setText( text_and_info );

                Object o = np_button.getTag();
//*DIALOG*/Settings.MOM(TAG_DIALOG, "..........np_button.getTag()=["+ o                       +"]");
                if(o instanceof MWebView)
                {
                    MWebView wv = (MWebView)o;
                    wv.find_selection( text_and_info );
                    return;
                }
            }
            //}}}
            // (get_np) {{{
            NotePane np  = get_np();
//*DIALOG*/Settings.MOC(TAG_DIALOG, "", "...np=["+np+"]");
            if(np == null)
                return;

            //}}}
            // [wvTools marker] .. (SAVE COOKIE) {{{
            if( wvTools.is_a_marker(np) )
            {
//*DIALOG*/Settings.MOC(TAG_DIALOG, "", "MARKER SAVE COOKIE: ["+np+"]");

                // VALIDATE MARK LABEL
                //if(text_and_info.indexOf( NotePane.INFO_SEP ) < 0)
                if( TextUtils.isEmpty( text_and_info ) )
                    text_and_info = np.name;
                else {
                    // check if multi-line are LABEL + INFO_SEP + INFO
                    int idx = text_and_info.indexOf( NotePane.INFO_SEP );

                    // if not, turn them into a valid construct
                    if(idx < 0) {
                        idx = text_and_info.indexOf("\n");
                        if(idx >= 0)
                            text_and_info = text_and_info.replace("\n", NotePane.INFO_SEP);
                    }
                }
                // update marker np text and cookie note param
                wvTools.marker_add_cookie_note(np, text_and_info);

                return;
            }
            //}}}
            // WORKBENCH ACTIVITY .. (ignore note taking) {{{
            if( Profile.is_a_WORKBENCH_TEMPLATE_NAME( Settings.Working_profile ) )
            {
//*DIALOG*/Settings.MOM(TAG_DIALOG, "...ignored from within ["+Settings.Working_profile+"]");

                return;
            }
            //}}}
            // [PERSONAL NOTE] .. (SavePersonalNote) {{{
            if( np.tag.equals("NOTE") )
            {
//*DIALOG*/Settings.MON(TAG_DIALOG, caller, "UPDATE [PERSONAL NOTE]");
                Settings.SavePersonalNote( text_and_info );
            }
            //}}}
            // [Working_profile np] .. (update) {{{
            // ONLY WHEN SOME EDITABLE ATTRIBUTE HAS CHANGED
            if(        !(np.text .equals( text_and_info ))
                    || !(np.tag  .equals( tag           ))
                    || !(np.shape.equals( shape         ))
              ) {
                // UPDATE [text] {{{
//*DIALOG*/Settings.MON(TAG_DIALOG, caller, "SOME EDITABLE ATTRIBUTE HAS CHANGED:");
//*DIALOG*/Settings.MOM(TAG_DIALOG, ". FROM.......np.text=["+np.text      +"]");
//*DIALOG*/Settings.MOM(TAG_DIALOG, ". TO...text_and_info=["+text_and_info+"]");

                np.setTextAndInfo( text_and_info );
                // }}}
                // UPDATE [tag] .. set-only i.e. no support for clear .. but commenting out with # works {{{
                if(!tag.equals("") && !np.tag .equals( tag  ))
                {

                    np.set_tag( tag );
                }
                // }}}
                // UPDATE [shape] {{{
                if(!np.shape .equals( shape ))// && !shape.equals("") )
                {
                    np.set_shape( shape );

                }
                // }}}
                // UPDATE DISPLAY {{{
//*DIALOG*/Settings.MOM(TAG_DIALOG, ". ...........np.text=["+ np.text                                 +"]");
//*DIALOG*/Settings.MOM(TAG_DIALOG, ". .....text_markdown=["+Settings.text_markdown(np.tag , np.text) +"]");

                np.button.fitText();
                np.button.invalidate();

                cancel_fs_button_anim();
                // }}}
                // UPDATE PROFILE .. f(NotePane text tag shape) // {{{
                profile_update_np(np, Settings.ellipsis(np.getLabel(), 80));

                // }}}
              }
            //}}}
        }
        //}}}
    };
    //}}}
    //* [fs_button] */
    //{{{
    private static       NpButton fs_button;
    private              NotePane pushed_np = null; // could be turned into a stack .. (i.e. supporting more than one level of pushing)

    // }}}
    // can_magnify_view {{{
    private boolean can_magnify_gesture_down_F_W_C_H_H_atXY()
    {
        return (gesture_down_SomeView_atXY         != null          )
            && (gesture_down_SomeView_atXY instanceof NpButton      ) // has to be
            && (gesture_down_SomeView_atXY         != fs_button     ) // (do not magnify the magnification tool itself)
            && (gesture_down_SomeView_atXY         != get_fg_view() ) // (do not magnify the temporaty foreground view)
            ;
    }
    //}}}
    // magnify_has_pushed_np {{{
    private boolean magnify_has_pushed_np()
    {
        boolean result = (pushed_np != null);
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "magnify_has_pushed_np: ...return "+result+" ["+pushed_np+"]");
        return  result;
    }
    //}}}
    // push_fs_button {{{
    private void push_fs_button(NpButton np_button, String caller)
    {
        if(np_button == null) return;

        // cache current magnified source
        // ...to revert back to
        // ...after momentarily magnifying another NpButton

        NotePane current_np = get_magnify_np();
        if(current_np == null) {
//*FULLSCREEN*/Settings.MON(TAG_FULLSCREEN, "push_fs_button", "NOTTHING TO PUSH TO MAGNIFY np_button=["+np_button+"]");
        }
        else if(current_np.button == np_button) {
//*FULLSCREEN*/Settings.MON(TAG_FULLSCREEN, "push_fs_button", "NOT PUSHING ALREADY MAGNIFIED np_button=["+np_button+"]");
        }
        else {
            pushed_np = current_np;
//*FULLSCREEN*/Settings.MON(TAG_FULLSCREEN, "push_fs_button", "PUSHING pushed_np=["+pushed_np+"]");
        }

    }
    //}}}
    // pop_fs_button {{{
    private boolean pop_fs_button(String caller)
    {
        if(pushed_np == null) return false;

        caller += "->pop_fs_button";
//*FULLSCREEN*/Settings.MON(TAG_FULLSCREEN, "pop_fs_button", "GETTING BACK TO pushed_np=["+pushed_np+"]");

        // STACK CURRENTLY MAGNIFIED SOURCE
        fs_button_setTag(pushed_np, caller); // tag fs_button with a (momentarily pushed_np) .. (to be taken as the current payload .. to magnify from)
        pushed_np = null;

        return true;
    }
    //}}}
    // magnify_np {{{
    public void magnify_np(NotePane np, String caller)
    {
        caller += "->magnify_np("+np+")";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);

        if(fs_button == null) create_fs_button();
        else                  cancel_fs_button_anim();

        fs_button_setTag(np, caller);   // source NotePane instance

        magnify_np_show(caller);

    }
    //}}}
    // magnify_np_button {{{
    private void magnify_np_button(NpButton np_button, String caller)
    {
        caller += "->magnify_np_button("+np_button+")";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);

        if(fs_button == null) create_fs_button();
        else                  cancel_fs_button_anim();

        push_fs_button  (np_button, caller); // so we can revert to a currently magnified NotePane's button
        fs_button_setTag(np_button, caller); // source NpButton instance

        magnify_np_show (caller);
    }
    //}}}
    // magnify_np_url {{{
    public void magnify_np_url(NotePane np, String url)
    {
        String caller = "magnify_np_url("+np+")";
//*FULLSCREEN*/Settings.MON(TAG_FULLSCREEN, caller, "url=["+url+"]");

        magnify_np(np, caller);

//*FULLSCREEN*/Settings.MON(TAG_FULLSCREEN, caller, "POSTING A LOAD URL TASK");
        // SCHEDULE A LOAD URL TASK
        if( !Settings.URL_equals(Settings.REQUESTED_URL, url) )
        {
            Settings.set_REQUESTED_URL  ( url );
            Settings.set_REQUESTED_TITLE(  "" );  // get a fresh one...
        }
        handler.re_postDelayed( hr_fs_button_show_url, 500);
    }
    //}}}
    // hr_fs_button_show_url {{{
    private final Runnable hr_fs_button_show_url = new Runnable()
    {
        @Override
        public void run() {
            String caller = "hr_fs_button_show_url";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);
            load_fs_webView_url(Settings.REQUESTED_URL, true, caller); // from_user
        }
    };
    //}}}
    //* CREATE */
    // create_fs_button {{{

    private void create_fs_button()
    {
    //  fs_button = new FullScreen_EditText( activity );
        fs_button = new NpButton( activity );
        fs_button.lockElevation     ( Settings.FS_BUTTON_ELEVATION  );
        fs_button.set_shape         ( NotePane.SHAPE_TAG_TILE       );
        fs_button.setTextColor      ( Color.parseColor("#ffcccccc") );
        fs_button.setBackgroundColor( Color.parseColor("#ffff4444") );

        fs_button.setTypeface       ( Settings.getNotoTypeface()    );
        fs_button.setPadding((int)fs_button.getTextSize()/2,1,1,1   ); // left top right bottom

        fs_button.setEllipsize      ( TextUtils.TruncateAt.END      );

        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(0, 0);
        flp.leftMargin = 0;
        flp.height = Settings.DISPLAY_H;
        flp.width  = Settings.SCREEN_W;
        fs_button.setLayoutParams( flp );

      //fs_button.may_overflow = false; // full screen must show everything

        fs_button.setOnClickListener    ( magnify_np_OnClickListener     );
        fs_button.setOnLongClickListener( magnify_np_OnLongClickListener );

        frameLayout.addView( fs_button );
    }

    //}}}
    // fs_button_setTag {{{
    private void fs_button_setTag(Object object_NotePane_or_NpButton, String caller)
    {
        // RELEASE CURRENTLY MAGNIFIED TAB (IF ANY) {{{
        caller += "->fs_button_setTag("+object_NotePane_or_NpButton+")";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);
        if(fs_button == null) return;

        NpButton np_button = fs_button_NoteHolder.get_np_button();

        if(np_button != null)
        {
            // if necessary, restore source visibility
            if(np_button.getVisibility() != View.VISIBLE)
            {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "XXX RESTORING "+np_button+" VISIBILITY");
                np_button.setVisibility( View.VISIBLE );
            }
        }
        //}}}
        // SWITCH TO SPECIFIED PAYLOAD (MAY BE NULL) {{{
        if(object_NotePane_or_NpButton instanceof NpButton)
        {
            NpButton nb = (NpButton)object_NotePane_or_NpButton;
            Object o = nb.getTag();
            if(o instanceof NotePane)
                object_NotePane_or_NpButton = o;
        }
        fs_button.setTag( object_NotePane_or_NpButton );

        //}}}
    }
    //}}}
    //* ACCESS */
    // get_magnify_np {{{
    private NotePane get_magnify_np()
    {
        return (fs_button_NoteHolder != null)
            ?   fs_button_NoteHolder.get_np()
            :   null;
    }
    //}}}
    // is_magnify_np_button {{{
    public boolean is_magnify_np_button(NpButton np_button)
    {
        return (np_button != null) && (np_button == get_magnify_np_button());
    }
    //}}}
    // get_magnify_np_button {{{
    private NpButton get_magnify_np_button()
    {
        return (fs_button_NoteHolder != null)
            ?   fs_button_NoteHolder.get_np_button()
            :   null;
    }
    //}}}
    //* LISTENER */
    // magnify_np_OnClickListener {{{
    private final View.OnClickListener magnify_np_OnClickListener = new View.OnClickListener()
    {
        @Override public void onClick(View view) // can be stolen by LongClickListener below
        {
            // NOT A CLICK {{{
            String caller = "magnify_np_OnClickListener";
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, caller+": has_moved_enough=["+has_moved_enough+"]");

            if( has_moved_enough ) return;

            //}}}
            // INSTANT HIDE PUSHED_NP {{{
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "...fs_webview_session_running=["+ fs_webview_session_running +"]");
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "......magnify_has_pushed_np()=["+ magnify_has_pushed_np()   +"]");

            if( magnify_has_pushed_np() )
            {
                if(pushed_np != null) {
//*FULLSCREEN*/NpButton np_button = fs_button_NoteHolder.get_np_button();//TAG_FULLSCREEN
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "INSTANT HIDE ["+ np_button +"]");
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "GETTING BACK TO pushed_np=["+ pushed_np +"]");
                    fs_button_hide(caller);
                }
                pop_fs_button(caller);
            }
            //}}}
            //XXX remnant from when a magnified button would serve as a "setBlurred" background
            // WEBVIEW CYCLE: [show]->[expanded]->[collapsed]->[free]->[grabbed]->[hidden] {{ {XXX
            //XXX else if( fs_webview_session_running )
            //XXX {
            //XXX     fs_webView_session_cycle_grab_collapse_or_hide(caller);
            //XXX }
            //}} }XXX
            // [fs_button] HIDE {{{
            else if(gesture_consumed_by_onFling==null)
            {
                magnify_np_hide(caller);

            }
            //}}}
        }
    };
    //}}}
    // magnify_np_OnLongClickListener {{{
    private final View.OnLongClickListener magnify_np_OnLongClickListener = new View.OnLongClickListener()
    {
        @Override
        public boolean onLongClick(View view)
        {
            String caller = "magnify_np_OnLongClickListener";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);
            // NOT A LONG CLICK {{{
            if(has_moved_enough ) {
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "NOT A LONG CLICK: has_moved_enough=["+has_moved_enough+"]");

                return false; // not consumed
            }
            //}}}
            // EDIT NoteHolder {{{
            if(fs_button_NoteHolder != null)
            {
                String     title;

                if( fs_button_NoteHolder.get_tag().equals("NOTE") ) {
                    title = "Personal note";
                }
                else {
                    title = fs_button_NoteHolder.get_name();
                    if( BuildConfig.BUILD_TYPE.equals("debug") )
                    {
                        String s;
                        s = fs_button_NoteHolder.get_tag  ();/* if( !s.equals("") ) */ title += " ... [tag="  + s +"]";
                        s = fs_button_NoteHolder.get_shape();/* if( !s.equals("") ) */ title += " ... [shape="+ s +"]";
                        s = fs_button_NoteHolder.get_xy_wh();/* if( !s.equals("") ) */ title += " ... [xy_wh="+ s +"]";
                    }
                }

                show_note_dialog(title, fs_button_NoteHolder);
            }
            // }}}
            return true; // consumed
        }
    };
    //}}}
    //* DISPLAY */
    // is_magnify_np_showing {{{
    private boolean is_magnify_np_showing()
    {
        return is_view_showing( fs_button );
    }
    //}}}
    // magnify_np_show {{{
    private void magnify_np_show(String caller)
    {
        caller += "->magnify_np_show";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);

        NpButton np_button = fs_button_NoteHolder.get_np_button();
        if(np_button != null)
        {
            fs_button.orientation = np_button.orientation;

            String text  = fs_button_NoteHolder.get_text();
//*DIALOG*/Settings.MON(TAG_DIALOG, caller, "XXX fs_button_NoteHolder.get_text=["+text+"]");
            int fg_color = fs_button_NoteHolder.get_fg_color();
            int bg_color = fs_button_NoteHolder.get_bg_color();
            do_magnify_np_button(np_button, text, fg_color, bg_color);
        }
    }
    //}}}
    // do_magnify_np_button {{{
    private void do_magnify_np_button(NpButton np_button, String text, int fg_color, int bg_color)
    {
        String caller = "do_magnify_np_button";
//*FULLSCREEN*/Settings.MON(TAG_FULLSCREEN, caller, "(np_button="+np_button+", text="+text+")");
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "...fs_webview_session_running=["+ fs_webview_session_running +"]");
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, ".....magnify_has_pushed_np()=["+ magnify_has_pushed_np()   +"]");
        // bg_color fg_color {{{
        fs_button.setTextColor      ( fg_color );
        fs_button.setBackgroundColor( bg_color ); // blurred_color will be updated
        //}}}
        // check display_text change {{{
        text = ((text.length() < 32) || text.endsWith("\n"))
            ?    text
            :    text+"\n";
        String   display_text = text.replace(NotePane.INFO_SEP, "\n");

        boolean  text_changed = !fs_button.getText().equals(display_text);

        //}}}
        // CHECK NULL EFFECT {{{
        if(        !text_changed
                && (fs_button.getVisibility() == View.VISIBLE)
                && (fs_button.getX         () == 0           )
                && (fs_button.getY         () == 0           )
                && (fs_button.getScaleX    () == 1F          )
                && (fs_button.getScaleY    () == 1F          )
                && (fs_button.getAlpha     () == 1F          )
          ) {
//*FULLSCREEN*/Settings.MON(TAG_FULLSCREEN, caller, "NO CHANGE");

        //  return; // wrong while swapping webviews back from full screen
        }
        //}}}
        // set fs_button full screen {{{
        FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams)fs_button.getLayoutParams();
        flp.height = Settings.DISPLAY_H;
        flp.width  = Settings.SCREEN_W;

        if(text_changed) {
            fs_button.setText(display_text);
            fs_button.fitText(flp.width, flp.height);
            fs_button.invalidate();
        }

        //}}}
        // SHOW FULLSCREEN BUTTON {{{
        fs_button_show(caller);

        //}}}
        // EXPAND TO FULL SCREEN SIZE {{{
        if(ANIM_SUPPORTED && !magnify_has_pushed_np())
        {
            // SET ANIM START STATE {{{
            int[]     xy = new int[2]; np_button.getLocationOnScreen( xy );
            int        x = xy[0];
            int        y = xy[1];
            int        w = np_button.getWidth();
            int        h = np_button.getHeight();
            float scaleX = (float)w / (float)Settings.SCREEN_W ;
            float scaleY = (float)h / (float)Settings.DISPLAY_H;

/* // {{{
Settings.MON(TAG_FULLSCREEN, "\n"
                +"@_ do_magnify_np_button - ANIM START:\n"
                +"@__ fs_button ___ VISIBLE: "+ (fs_button.getVisibility()==View.VISIBLE)          +"\n"
                +"@__ fs_button _____ alpha: "+  fs_button.getAlpha ()                             +"\n"
                +"@__ fs_button ________ wh: "+  fs_button.getWidth () +"x"+ fs_button.getHeight() +"\n"
                +"@__ fs_button ______scale: "+  fs_button.getScaleX() +"x"+ fs_button.getScaleY() +"\n"
                +"@__ ZOOM-OUT_FROM scaleXY: "+                scaleX +" x "+ scaleY               +"\n"
                +"@__ ZOOM-OUT_FROM_____ xy: "+                     x +" x "+ y                    +"\n"
                +"@__ ZOOM-OUT_FROM_____ wh: "+                     w +" x "+ h                    +"\n"
                +"@@@"
                );
*/ // }}}
        if( !fs_webview_session_running )
        {
            fs_button.setX         (      x );
            fs_button.setY         (      y );
            fs_button.setPivotX    (      0 );
            fs_button.setPivotY    (      0 );
            fs_button.setScaleX    ( scaleX );
            fs_button.setScaleY    ( scaleY );
            fs_button.setRotation  (     0F );
            fs_button.setRotationX (     0F );
            fs_button.setRotationY (     0F );
        }
/*
*/
            //}}}
            // ANIM {{{
            AnimatorSet set = new AnimatorSet();
            set.setDuration( FS_BUTTON_EXPAND_SLOW_DURATION );
            set.setInterpolator(new DecelerateInterpolator());
            set
                .play(ObjectAnimator.ofFloat(fs_button, View.X      , 0 ))
                .with(ObjectAnimator.ofFloat(fs_button, View.Y      , 0 ))
                .with(ObjectAnimator.ofFloat(fs_button, View.ALPHA  , 1F))
                .with(ObjectAnimator.ofFloat(fs_button, View.SCALE_X, 1F))
                .with(ObjectAnimator.ofFloat(fs_button, View.SCALE_Y, 1F))
                ;

            set.start();
            //}}}
        }
        else {
            fs_button.setX     (0);
            fs_button.setY     (0);
            fs_button.setAlpha (1F);
            fs_button.setScaleX(1F);
            fs_button.setScaleY(1F);
        }
/* // {{{
Settings.MON(TAG_FULLSCREEN, "\n"
                +"@_ do_magnify_np_button:\n"
                +"@_ do_magnify_np_button - INSTANT RESULT:\n"
                +"@__ fs_button ___ VISIBLE: "+ (fs_button.getVisibility()==View.VISIBLE)          +"\n"
                +"@__ fs_button _____ alpha: "+  fs_button.getAlpha ()                             +"\n"
                +"@__ fs_button ________ wh: "+  fs_button.getWidth () +"x"+ fs_button.getHeight() +"\n"
                +"@__ fs_button ______scale: "+  fs_button.getScaleX() +"x"+ fs_button.getScaleY() +"\n"
                +"@@@"
                );
*/ // }}}
        //}}}
    }
    //}}}
    // PENDING BUTTON TO MAGNIFY {{{
    //{{{
    private NpButton held_button_to_magnify;

    //}}}
    // post_pending_button_to_magnify {{{
    public  void post_pending_button_to_magnify(NpButton nb            ) { post_pending_button_to_magnify(nb, LONGPRESS_DURATION); }
    public  void post_pending_button_to_magnify(NpButton nb, long delay)
    {
        String caller = "post_pending_button_to_magnify("+nb+")";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);
        if(nb != null) {
            held_button_to_magnify = nb;
            handler.re_postDelayed ( hr_serve_pending_button_to_magnify, delay);
        }
        else {
            handler.removeCallbacks( hr_serve_pending_button_to_magnify );
        }
    }
    //}}}
    // get_pending_button_to_magnify {{{
    public NpButton get_pending_button_to_magnify()
    {
        return held_button_to_magnify;
    }
    //}}}
    // cancel_pending_button_to_magnify {{{
    public  void cancel_pending_button_to_magnify(String caller)
    {
        // NO PENDING MAGNIFICATION
        if(held_button_to_magnify == null) return;

        caller += "->cancel_pending_button_to_magnify";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "held_button_to_magnify=["+ get_view_name(held_button_to_magnify) +"]");

//        // CHECK: BUTTON MAGNIFICATION NOT CANCELED BY MOVE EVENTS
//        if( wvTools.is_a_spread_marker( held_button_to_magnify ) )
//        {
///*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, caller+": BUTTON MAGNIFICATION NOT CANCELED BY MOVE EVENTS");
//            return;
//        }

        // DO UNSCHEDULE MAGNIFICATION
        held_button_to_magnify = null; // cleared when canceled
        handler.removeCallbacks( hr_serve_pending_button_to_magnify );
    }
    //}}}
    // hr_serve_pending_button_to_magnify {{{
    private final Runnable hr_serve_pending_button_to_magnify= new Runnable() {
        @Override public void run() {
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, "hr_serve_pending_button_to_magnify.run: held_button_to_magnify=["+ get_view_name(held_button_to_magnify) +"]");
            if(held_button_to_magnify != null)
            {
                NpButton nb = held_button_to_magnify;
                held_button_to_magnify = null; // cleared when serviced

                gesture_consumed_by_magnify = "held_button_to_magnify";
                // SCROLLBAR CAPTION EDIT NoteHolder {{{
                if( wvTools.is_a_scrollbar(nb) )
                {
                    magnify_np_button(nb, "hr_serve_pending_button_to_magnify");
                    if(fs_button_NoteHolder != null)
                    {
                        String title = "SELECTION";
                        show_note_dialog(title, fs_button_NoteHolder);
                    }
                }
                // }}}
                else {
                    magnify_np_button(nb, "hr_serve_pending_button_to_magnify");
                }
            }
        }
    };
    // }}}
    //}}}
    // PENDING WEBVIEW TO MAGNIFY {{{
    // {{{
    private static final      int WEBVIEW_TO_MAGNIFY_DELAY = 50;//500;

    private              MWebView pending_fs_webView_FULLSCREEN;
    private               boolean fs_webView_expanded = false;

    // }}}
//    // post_pending_fs_webView_fullscreen {{{
//    private void post_pending_fs_webView_fullscreen(MWebView wv)
//    {
////*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, "post_pending_fs_webView_fullscreen("+wv+")");
//        if(wv != null) {
//            pending_fs_webView_FULLSCREEN = wv;
//            handler.re_postDelayed ( hr_serve_pending_fs_webView_fullscreen, WEBVIEW_TO_MAGNIFY_DELAY);
//        }
//        else {
//            handler.removeCallbacks( hr_serve_pending_fs_webView_fullscreen );
//        }
//    }
//    //}}}
    // is_fs_webView_fullscreen_still_pending {{{
    private boolean is_fs_webView_fullscreen_still_pending(String caller)
    {
        caller += "->is_fs_webView_fullscreen_still_pending";
        boolean result = (pending_fs_webView_FULLSCREEN != null);
//*FULLSCREEN*/if(result) Settings.MOM(TAG_FULLSCREEN, caller+": ...return "+result);
        return result;
    }
    //}}}
    // cancel_pending_fs_webView_fullscreen {{{
    private void cancel_pending_fs_webView_fullscreen(String caller)
    {
        caller += "->cancel_pending_fs_webView_fullscreen";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);
        pending_fs_webView_FULLSCREEN = null; // cleared when canceled
        handler.removeCallbacks( hr_serve_pending_fs_webView_fullscreen );
    }
    //}}}
    // hr_serve_pending_fs_webView_fullscreen {{{
    private final Runnable hr_serve_pending_fs_webView_fullscreen= new Runnable() {
        @Override public void run()
        {
            if(pending_fs_webView_FULLSCREEN == null) return;
            String caller = "hr_serve_pending_fs_webView_fullscreen";
            do_serve_pending_fs_webView_fullscreen(caller);
        }
    };
    // }}}
    // do_serve_pending_fs_webView_fullscreen {{{
    private void do_serve_pending_fs_webView_fullscreen(String caller)
    {
        caller += "->do_serve_pending_fs_webView_fullscreen";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);

        if(pending_fs_webView_FULLSCREEN == null) return;

        if(!fs_webView_expanded  )  expand_fs_webView_fullscreen(pending_fs_webView_FULLSCREEN, caller);
        else                        collapse_fs_webView_fullscreen(caller);
        pending_fs_webView_FULLSCREEN = null; // FORGET VIEW WHEN SERVICED
    }
    // }}}
    // expand_fs_webView_fullscreen_select_atX {{{
    // {{{
    private   int   last_expanded_swap_side;
//  private   float last_expanded_rawX;
    // }}}
    // expand_fs_webView_fullscreen_select_atX {{{
    public  void expand_fs_webView_fullscreen_select_atX(float rawX, boolean toggle_on_off)
    {
        String caller = "expand_fs_webView_fullscreen_select_atX("+rawX+", toggle_on_off=["+toggle_on_off+"])";
//        // ON COOLDOWN .. POSTPONE LAST REQUEST {{{
//        if( select_fs_webView_fullscreen_at_x_on_cooldown )
//        {
///*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, caller+": (ON COOLDOWN) rawX=["+rawX+"]");
//
//            last_expanded_rawX = rawX;
//            return;
//        }
//        //}}}
        // CHECK WEBVIEW SLICE (left center right) {{{
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "...last_expanded_swap_side=["+last_expanded_swap_side+"]");
        int      split_count =   get_fs_webView_count();
        int                w =   Settings.DISPLAY_W / split_count;
        int        swap_side =   0;

        if(     split_count ==   2) {
            if(        rawX  <   w) swap_side = Settings.SWAP_L;
            else                    swap_side = Settings.SWAP_C;
        }
        else if(split_count ==   3) {
            if(        rawX  <   w) swap_side = Settings.SWAP_L;
            else if(   rawX  < 2*w) swap_side = Settings.SWAP_C;
            else                    swap_side = Settings.SWAP_R;
        }
        //}}}
        expand_fs_webView_fullscreen_select_side(swap_side, toggle_on_off);
    }
    //}}}
    // expand_fs_webView_fullscreen_toggle {{{
    public  void expand_fs_webView_fullscreen_toggle(float rawX)
    {
        // no last expanded to toggle
//*FULLSCREEN*/String caller = "expand_fs_webView_fullscreen_toggle("+rawX+")";//TAG_FULLSCREEN
        if(last_expanded_swap_side == 0)
        {
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller, "NONE EXPANDED YET: EXPANDING AT X ["+rawX+"]");
            expand_fs_webView_fullscreen_select_atX(rawX, true);
        }
        // have last expanded to toggle
        else {
            // unexpand
            if( fs_webView_expanded )
            {
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller, "EXPANDED: ...COLLAPSING LAST SIDE ["+last_expanded_swap_side+"]");
                expand_fs_webView_fullscreen_select_side(last_expanded_swap_side, true);
            }
            // expand at X
            else {
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller, "COLLAPSED: ...EXPANDING AT X ["+rawX+"]");
                expand_fs_webView_fullscreen_select_atX(rawX, false);
            }
        }
    }
    //}}}
    // expand_fs_webView_fullscreen_select_side {{{
    private void expand_fs_webView_fullscreen_select_side(int swap_side, boolean toggle_on_off)
    {
        String caller = "expand_fs_webView_fullscreen_select_side("+swap_side+", toggle_on_off=["+toggle_on_off+"])";
        // WVTOOLS.fs_search_show_webView_swap_side {{{
        wvTools.fs_search_show_webView_swap_side( swap_side );

        //}}}
        // [!toggle_on_off] + SAME AS LAST CALL .. (ignore) {{{
        if(!toggle_on_off && (swap_side == last_expanded_swap_side))
        {
//*FULLSCREEN*/Settings.MON(TAG_FULLSCREEN, caller, "SAME SIDE");
            return;
        }
        //}}}
//        // DO THE SWAPPING AND START A COOLDOWN PERIOD {{{
///*FULLSCREEN*/Settings.MON(TAG_FULLSCREEN, caller, "swap_side=["+swap_side+"]");
//        select_fs_webView_fullscreen_at_x_on_cooldown = true;
//        handler.re_postDelayed(hr_clear_select_fs_webView_fullscreen_at_x_cooldown, SELECT_FS_WEBVIEW_COOLDOWN_DELAY);
//        //}}}
        // WEBVIEW TO BE SWAPPED TO THE LEFT {{{
//*FULLSCREEN*/Settings.MON(TAG_FULLSCREEN, caller, "swap_side=["+swap_side+"]");
        MWebView fs_webView_to_expand;
        switch(swap_side) {
            case Settings.SWAP_L: fs_webView_to_expand = fs_webViewL; break;
            case Settings.SWAP_C: fs_webView_to_expand = fs_webViewC; break;
            case Settings.SWAP_R: fs_webView_to_expand = fs_webViewR; break;
            default:              fs_webView_to_expand = null;        break;
        }

        last_expanded_swap_side = swap_side;
        //}}}
        // SOMETHING TO EXPAND .. [TO COLLAPSE] {{{
        if(fs_webView_to_expand != null)
        {
            int split_count = get_fs_webView_count();
//*FULLSCREEN*/Settings.MON(TAG_FULLSCREEN, caller, "split_count=["+split_count+"] fs_webView_to_expand=["+get_view_name(fs_webView_to_expand)+"]\n");

            // SOMETHING TO EXPAND
            if(        !fs_webView_expanded                 // none yet
                    || (fs_webView_to_expand != fs_webView) // swap currently expanded .. (always in fs_webView)
              )
            {
                expand_fs_webView_fullscreen(fs_webView_to_expand, caller);
            }

            // SOMETHING TO COLLAPSE
            else if(fs_webView_expanded && toggle_on_off)
            {
                collapse_fs_webView_fullscreen(caller);
            }
        }
        //}}}
    }
    //}}}
//    // COOLDOWN {{{
//    private static final int              SELECT_FS_WEBVIEW_COOLDOWN_DELAY = 500;
//
//    private          boolean select_fs_webView_fullscreen_at_x_on_cooldown = false;
//
//    private final Runnable hr_clear_select_fs_webView_fullscreen_at_x_cooldown = new Runnable()
//    {
//        @Override public void run()
//        {
//            // clear cooldown
//            select_fs_webView_fullscreen_at_x_on_cooldown = false;
//
//            // execute delayed request
//            if(last_expanded_rawX < 0) return;
//
//            expand_fs_webView_fullscreen_select_atX(last_expanded_rawX, false); // !toggle-off
//            last_expanded_rawX = -1;
//        }
//    };
//    //}}}
    //}}}
    // expand_fs_webView_fullscreen {{{
    private void expand_fs_webView_fullscreen(MWebView fs_webView_to_expand, String caller)
    {
        caller += "->expand_fs_webView_fullscreen("+get_view_name(fs_webView_to_expand)+")";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "...fs_webView=["+get_view_name(fs_webView)+"]");
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "...fs_webView_expanded=["+fs_webView_expanded+"]");

        // DO NOT EXPAND A SINGLE VIEW {{{
        if(        !fs_webView_expanded
                && (fs_webView  == fs_webView_to_expand)
                && (fs_webView2 == null) // nothing to swap with
          ) {
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "...NOT EXPANDING A SINGLE VIEW");

            return;
          }
        // }}}
        // DO NOT EXPAND AN ALREADY EXPANDED VIEW {{{
        if(fs_webView_expanded && (fs_webView  == fs_webView_to_expand)) {
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "...NOT AN ALREADY EXPANDED VIEW");

            return;
          }
        // }}}
        // SET AS EXPANDED
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "...fs_webView_expanded=["+fs_webView_expanded+"]");
        if( !fs_webView_expanded ) {
            fs_webView_expanded     = true;
            last_expanded_swap_side = 0;
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "...SETTINGS fs_webView_expanded=["+fs_webView_expanded+"]");
        }

        if     (fs_webView_to_expand == fs_webViewL) last_expanded_swap_side = Settings.SWAP_L;
        else if(fs_webView_to_expand == fs_webViewC) last_expanded_swap_side = Settings.SWAP_C;
        else if(fs_webView_to_expand == fs_webViewR) last_expanded_swap_side = Settings.SWAP_R;

        // SWAP SELECTED TO BE THE MAGNIFIED WEBVIEW TO THE LEFT
        if     (fs_webView_to_expand == fs_webView2) fs_wswap_1_2_CB();
        else if(fs_webView_to_expand == fs_webView3) fs_wswap_1_3_CB();

        // SHOW ONLY ONE WEBVIEW
        adjust_WEBVIEW_LAYOUT(1, caller);

        // animate to current grab-state .. (adjusting all visible WEBVIEWS geometry in the process)
        if( fs_webView_isGrabbed )  anim_grab_fs_webView(caller); // WEBVIEW (anim)
        else                        anim_free_fs_webView(caller); // WEBVIEW (anim)

//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "fs_webView_expanded.............=["+ fs_webView_expanded                 +"]");
    }
    //}}}
    // expand_fs_webView_fullscreen_next {{{
    private void expand_fs_webView_fullscreen_next(String caller) { expand_fs_webView_fullscreen_side( true, caller); }
    private void expand_fs_webView_fullscreen_prev(String caller) { expand_fs_webView_fullscreen_side(false, caller); }
    private void expand_fs_webView_fullscreen_side(boolean right_or_left, String caller)
    {
        caller += "->expand_fs_webView_fullscreen_side(next_or_prev="+right_or_left+")";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "fs_webView_expanded.............=["+ fs_webView_expanded               +"]");

        MWebView fs_webView_to_expand = get_fs_webView_to_expand_side(right_or_left, caller);
        if(fs_webView_to_expand == null) return;

        // SWAP SELECTED TO BE THE MAGNIFIED WEBVIEW TO THE LEFT
        if     (fs_webView_to_expand == fs_webView2) fs_wswap_1_2_CB();
        else if(fs_webView_to_expand == fs_webView3) fs_wswap_1_3_CB();

        // SHOW ONLY ONE WEBVIEW
        adjust_WEBVIEW_LAYOUT(1, caller);

        // animate to current grab-state .. (adjusting all visible WEBVIEWS geometry in the process)
        if( fs_webView_isGrabbed )  anim_grab_fs_webView(caller); // WEBVIEW (anim)
        else                        anim_free_fs_webView(caller); // WEBVIEW (anim)

    }
    //}}}
    // get_fs_webView_to_expand_side {{{
    private MWebView get_fs_webView_to_expand_right(String caller) { return get_fs_webView_to_expand_side( true, caller); }
    private MWebView get_fs_webView_to_expand_left (String caller) { return get_fs_webView_to_expand_side(false, caller); }
    private MWebView get_fs_webView_to_expand_side(boolean right_or_left, String caller)
    {
        caller += "->expand_fs_webView_can_swap_side(right_or_left="+right_or_left+")";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "fs_webView_expanded.............=["+ fs_webView_expanded               +"]");

        MWebView fs_webView_to_expand = null;
        if( fs_webView_expanded )
        {
            if( right_or_left ) {
                if(fs_webView == fs_webViewL      ) fs_webView_to_expand = fs_webViewC      ; // CENTER
                if(fs_webView == fs_webViewC      ) fs_webView_to_expand = fs_webViewR      ; // RIGHT
                if(fs_webView == fs_webViewR      ) fs_webView_to_expand = null             ; // nothing [to the right] of [fs_webView RIGHT]
            }
            else {
                if(fs_webView == fs_webViewL      ) fs_webView_to_expand = null             ; // nothing [to the left]  of [fs_webView LEFT ]
                if(fs_webView == fs_webViewC      ) fs_webView_to_expand = fs_webViewL      ; // LEFT
                if(fs_webView == fs_webViewR      ) fs_webView_to_expand = fs_webViewC      ; // CENTER
            }
        }

//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "fs_webView_to_expand............=["+ get_view_name(fs_webView_to_expand) +"]");
        return fs_webView_to_expand;
    }
    //}}}
    // collapse_fs_webView_fullscreen {{{
    private void collapse_fs_webView_fullscreen(String caller)
    {
        caller += "->collapse_fs_webView_fullscreen";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);

        // SET AS COLLAPSED
        if( fs_webView_expanded ) {
            fs_webView_expanded     = false;
            last_expanded_swap_side = 0;
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "...SETTINGS fs_webView_expanded=["+fs_webView_expanded+"]");
        }

        // MOVE MAGNIFIED WEBVIEW BACK WHERE IT WAS BEFORE
        if(fs_webView  != fs_webViewL) fs_wswap_1_2_CB(); // may get   LEFT or CENTER  or RICHT .. (LEFT had to be one of those 2)
        if(fs_webView  != fs_webViewL) fs_wswap_1_3_CB(); // has to be LEFT.................... .. (checked all 3 combinations)
        if(fs_webView2 != fs_webViewC) fs_wswap_2_3_CB(); // may have to swap  CENTER and RIGHT .. (ejected here by fs_webView)

        // SHOW PREVIOUSLY DISPLAYED WEBVIEWS
        adjust_WEBVIEW_LAYOUT(get_fs_webView_count(), caller);

        // animate to current grab-state .. (adjusting all visible WEBVIEWS geometry in the process)
        if( fs_webView_isGrabbed )  anim_grab_fs_webView(caller); // WEBVIEW (anim)
        else                        anim_free_fs_webView(caller); // WEBVIEW (anim)

//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "fs_webView_expanded.............=["+ fs_webView_expanded                 +"]");
    }
    //}}}
    //}}}
    // magnify_np_hide (animation) {{{
    // {{{
    private static final float TILT_ANGLE = 20f;

    private float fs_xfrom;
    private float fs_yfrom;
    float fs_zfrom;
    private float fs_xto;
    private float fs_yto;
    private float fs_rxfrom;
    private float fs_ryfrom;
    private float fs_rzfrom;
    private float fs_scaleX;
    private float fs_scaleY;

    private final int fs_delay_warp   =                                        0; private final int fs_duration_warp   = 300;
    private final int fs_delay_slide  = fs_delay_warp   + fs_duration_warp  + 10; private final int fs_duration_slide  = 200;
    private final int fs_delay_insert = fs_delay_slide  + fs_duration_slide + 10; private final int fs_duration_insert = 100;
    private final int fs_delay_alpha  = fs_delay_insert + fs_duration_insert+ 10; private final int fs_duration_alpha  = 250;
    private final int fs_delay_hide   = fs_delay_alpha  + fs_duration_alpha + 10;
    // }}}
    // magnify_np_hide {{{
    @SuppressWarnings("ConstantConditions")
    private void magnify_np_hide(String caller)
    {
        caller += "->magnify_np_hide";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);

        // BACK TO SOURCE TAB
        if( pop_fs_button("magnify_np_hide") )
            magnify_np_show(caller);

        // CANCEL ANY SCHEDULED ANIMATIONS {{{
        handler.removeCallbacks( hr_fs_button_warp   );
        handler.removeCallbacks( hr_fs_button_slide  );
        handler.removeCallbacks( hr_fs_button_insert );
        handler.removeCallbacks( hr_fs_button_alpha  );
        handler.removeCallbacks( hr_fs_button_hide   );

        //}}}
        // handle and system-bar visibility {{{
    //  boolean wvContainer_is_showing = is_view_showing( wvContainer );
//FULLSCREEN//Settings.MOM(TAG_FULLSCREEN, "wvContainer_is_showing=["+wvContainer_is_showing+"]");
        if(         (fs_button == null)
                // ((fs_button.getVisibility() != View.VISIBLE) && !wvContainer_is_showing)
          ) {
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "(fs_button == null) OR NOT VISIBLE");

            return;
          }
        //}}}
        // INSTANT HIDE WHEN MAGNIFIED BUTTON GONE {{{
        NpButton np_button = fs_button_NoteHolder.get_np_button();
        if(np_button == null)
        {
            fs_button_hide(caller);

//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "(np_button == null)");
            return;
        }
        //}}}
        // SHRINK BACK OVER CLONED NP STEM {{{
        int[]     xy = new int[2]; np_button.getLocationOnScreen( xy );
        int        x = xy[0];
        int        y = xy[1];
        float      w = np_button.getWidth();
        float      h = np_button.getHeight();
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "np_button: xy=["+x+" @ "+y+"] wh=["+w+" x "+h+"]");
        //}}}
        // CHECK NEIGHBORHOOD {{{
        boolean tl = np_button.tl;
        boolean lt = np_button.lt;
        boolean bl = np_button.bl;
        boolean rt = np_button.rt;

        boolean tr = np_button.tr;
        boolean lb = np_button.lb;
        boolean br = np_button.br;
        boolean rb = np_button.rb;

        boolean b_N;
        boolean b_E;
        boolean b_S;
        boolean b_W;
    //  boolean b_NW;
    //  boolean b_NE;
    //  boolean b_SW;
    //  boolean b_SE;

        // occupied borders and corners
        /**/                                   b_N  = tl || tr;
        /**/
       //**/            b_NW = lt || tl;                            b_NE = tr || rt;
        /**/                             /*      tl       tr     */
        /**/                             /*                      */
        /**/                             /*  lt  -ttttttttt-  rt */
        /**/                             /*      l---------r     */
        /**/            b_W  = lt || lb; /*      l---------r     */ b_E  = rt || rb;
        /**/                             /*      l---------r     */
        /**/                             /*      l---------r     */
        /**/                             /*  lb  -obbbbbbbb-  rb */
        /**/                             /*                      */
        /**/                             /*      bl       br     */
       //**/            b_SW = lb || bl;                            b_SE = rb || br;
        /**/
        /**/                                   b_S  = bl || br;

        int border_count = (b_W ? 1 : 0) + (b_N ? 1 : 0) + (b_E ? 1 : 0) + (b_S ? 1 : 0);
        //}}}
        // ANIMATION PARAMETERS
        // SCALE {{{
        fs_scaleX = w * tabs_container.getScaleX() / (float)(Settings.SCREEN_W);
        fs_scaleY = h * tabs_container.getScaleY() / (float) Settings.DISPLAY_H;

        // }}}
        // HORIZONTAL SLIDING {{{
        float dx   = Settings.TAB_GRID_S;
        if     (( b_W) && ( b_E)) dx  =  0; // no sliding
        else if((!b_W) && (!b_E)) dx  =  0; // no sliding
        else if(          ( b_E)) dx *= -1; // slide left
        fs_xfrom         = x + dx;
        fs_xto           = x;

        // }}}
        // VERTICAL SLIDING {{{
        float dy   = Settings.TAB_GRID_S; //        top neighbor  - slide down (default)
        if     (( b_N) && ( b_S)) dy  =  0; // between 2  neighbors - no sliding
        else if((!b_N) && (!b_S)) dy  =  0; // no dock-to neighbor  - no sliding
        else if(          ( b_S)) dy *= -1; //     bottom neighbor  - slide up
        fs_yfrom         = y    + dy;
        fs_yto           = y;

        // }}}
        // ROTATION X {{{
        fs_rxfrom  = TILT_ANGLE;
        if     (!b_N && !b_S) fs_rxfrom  =  0; // no north and no south neighbor - no rotation
        else if( b_N &&  b_S) fs_rxfrom  =  0; //    north and    south neighbor - no rotation
        else if(         b_S) fs_rxfrom *= -1; //                    bottom half - rotate ccw

        // }}}
        // ROTATION Y {{{
        fs_ryfrom  = TILT_ANGLE;
        if     (!b_W && !b_E) fs_ryfrom  =  0; // no west and no east neighbor - no rotation
        else if( b_W &&  b_E) fs_ryfrom  =  0; //    west and    east neighbor - no rotation
        else if(         b_E) fs_ryfrom *= -1; //                  docked left - rotate ccw

        // }}}
        // ROTATION Z {{{
        fs_rzfrom  = TILT_ANGLE;
        if     (( b_N) && ( b_S) ) fs_rzfrom  =  0; // no north and no south neighbour - no rotation (between walls)
        else if(( b_W) && ( b_E) ) fs_rzfrom  =  0; // no  west and no  east neighbour - no rotation (between walls)
        else if(border_count == 1) fs_rzfrom  =  0; //         docked by one side only - no rotation

        // }}}
        // TRANSPARENT OPACITY OF THE SOURCE BUTTON TO UNHIDE {{{
        np_button.setAlpha(0F);

        //}}}

        //XXX remnant from when a magnified button would serve as a the "setBlurred" background
        //fs_button.setBlurred(false);

        handler.postDelayed(hr_fs_button_warp  , fs_delay_warp  ); // WARP   - X+DX Y+DY SCALE=STEM ROT-XYZFROM
        handler.postDelayed(hr_fs_button_slide , fs_delay_slide ); // SLIDE  - DX=0 DY=0
        handler.postDelayed(hr_fs_button_insert, fs_delay_insert); // INSERT - ROTATION_XYZ->000 ALPHA=.5
        handler.postDelayed(hr_fs_button_alpha , fs_delay_alpha ); // ALPHA  - FS-ALPHA=0 STEM-ALPHA=1
        handler.postDelayed(hr_fs_button_hide  , fs_delay_hide  );
    }
    //}}}

    // hr_fs_button_warp {{{
    private final Runnable hr_fs_button_warp = new Runnable()
    {
        @Override public void run()
        {
            // TOP-LEFT=[0,0->DX,DY] SCALE=[FULLSCREEN-STEM] ROTATION=[]
            if( ANIM_SUPPORTED ) {
                AnimatorSet set = new AnimatorSet();
                set.setDuration  ( fs_duration_warp );
                set.setInterpolator(new AccelerateInterpolator());
                set
                    .play(ObjectAnimator.ofFloat(fs_button, View.X            ,     fs_xfrom ))
                    .with(ObjectAnimator.ofFloat(fs_button, View.Y            ,     fs_yfrom ))
                    .with(ObjectAnimator.ofFloat(fs_button, View.SCALE_X      ,     fs_scaleX))
                    .with(ObjectAnimator.ofFloat(fs_button, View.SCALE_Y      ,     fs_scaleY))
                    //  .with(ObjectAnimator.ofFloat(fs_button, View.ROTATION_X   , 0f, fs_rxfrom))
                    //  .with(ObjectAnimator.ofFloat(fs_button, View.ROTATION_Y   , 0f, fs_ryfrom))
                    //  .with(ObjectAnimator.ofFloat(fs_button, View.ROTATION     , 0f, fs_rzfrom))
                    ;
                set.start();
            }
            else {
                fs_button.setX     ( fs_xfrom  );
                fs_button.setY     ( fs_yfrom  );
                fs_button.setScaleX( fs_scaleX );
                fs_button.setScaleY( fs_scaleY );
            }
        }
    };
    // }}}
    // hr_fs_button_slide {{{
    private final Runnable hr_fs_button_slide = new Runnable()
    {
        @Override public void run()
        {
            // SLIDE - [dxy->xy]
            if( ANIM_SUPPORTED ) {
                AnimatorSet set = new AnimatorSet();
                set.setDuration  (fs_duration_slide);
                set.setInterpolator(new AccelerateInterpolator());
                set
                    .play(ObjectAnimator.ofFloat(fs_button, View.Y          , fs_yfrom , fs_yto))
                //  .play(ObjectAnimator.ofFloat(fs_button, View.ROTATION_X , fs_rxfrom, 0f    ))
                //  .with(ObjectAnimator.ofFloat(fs_button, View.ROTATION_Y , fs_ryfrom, 0f    ))
                //  .with(ObjectAnimator.ofFloat(fs_button, View.ROTATION   , fs_rzfrom, 0f    ))
                //  .with(ObjectAnimator.ofFloat(fs_button, View.ALPHA      ,           .5f    ))
                    ;
                set.start();
            }
            else {
                fs_button.setY        ( fs_yto);
            //  fs_button.setRotationX( 0F    );
            //  fs_button.setRotationY( 0F    );
            //  fs_button.setRotationZ( 0F    );
            //  fs_button.setAlpha    (.5F    );
            }
        }
    };
    // }}}
    // hr_fs_button_insert {{{
    private final Runnable hr_fs_button_insert = new Runnable()
    {
        @Override public void run()
        {
            // [ROTATION_XYZ->000] [ALPHA->.5]
            if( ANIM_SUPPORTED ) {
                AnimatorSet set = new AnimatorSet();
                set.setDuration  (fs_duration_insert);
                set.setInterpolator(new AccelerateInterpolator());
                set
                    .play(ObjectAnimator.ofFloat(fs_button, View.X    , fs_xfrom, fs_xto))
                //  .with(ObjectAnimator.ofFloat(fs_button, View.ALPHA,           .5f   ))
                    ;
                set.start();
            }
            else {
                fs_button.setX        ( fs_xto);
            //  fs_button.setAlpha    (.5F    );
            }
        }
    };
    // }}}
    // hr_fs_button_alpha {{{
    private final Runnable hr_fs_button_alpha = new Runnable()
    {
        @Override public void run()
        {
            NpButton np_button = get_magnify_np_button();
            if(np_button == null) return;

            // ALPHA - [fs_button]<-ALPHA->[np_button]
            if( ANIM_SUPPORTED ) {
                AnimatorSet set = new AnimatorSet();

                set.setInterpolator(new AccelerateInterpolator());

                set.setDuration  (fs_duration_alpha);

                set.play(ObjectAnimator.ofFloat(fs_button, View.ALPHA, 0f))
                   .with(ObjectAnimator.ofFloat(np_button, View.ALPHA, 1f));

                set.start();
            }
            else {
                fs_button.setAlpha    ( 0F    );
                np_button.setAlpha    ( 1F    );
            }
        }
    };
    // }}}
    // hr_fs_button_hide {{{
    private final Runnable hr_fs_button_hide = new Runnable()
    {
        @Override public void run()
        {
            String caller = "hr_fs_button_hide";
            // RESTORE SOURCE BUTTON VISIBILITY {{{
            NpButton np_button = get_magnify_np_button();
            if(np_button != null) {
                if(        (np_button == hist_band.back_nb )
                        || (np_button == hist_band.frwd_nb )
                        || (np_button == hist_band.prof_nb )
                        || (np_button == cart_band.see_nb  )
                        || (np_button == cart_band.end_nb  )
                        || (np_button == fs_wtitle         )
                        || (np_button == fs_wtitle2        )
                        || (np_button == fs_wtitle3        )
                        || (np_button == fs_wswapL         )
                        || (np_button == fs_wswapR         )
                        || (np_button == fs_browse         )
                        || (np_button == fs_bookmark       )
                        || (np_button == fs_goBack         )
                        || (np_button == fs_goForward      )
                        || (np_button == wvTools.sb        )
                        || (np_button == wvTools.sb2       )
                        || (np_button == wvTools.sb3       )
                        || (wvTools.get_view_name(np_button) != null)
                  ) {
//*FULLSCREEN*/Settings.MON(TAG_FULLSCREEN, caller, "NOT RESTORING MAGNIFIED BUTTON VISIBILITY OF BUILTIN [np_button]=["+ np_button +"]");
                  }
                else {
//*FULLSCREEN*/Settings.MON(TAG_FULLSCREEN, caller, "+++ RESTORING MAGNIFIED BUTTON VISIBILITY OF [np_button]=["+ np_button +"]");
                    np_button.setVisibility( View.VISIBLE );
                }
            }

            // }}}
            // DONE WITH FULLSCREEN BUTTON {{{
            fs_button_hide(caller);

            fs_button_setTag(null, caller); // release current magnification source
            // }}}
            hide_tooltip();
          //sync_tabs_scrolling(caller);
          set_tabs_scrolling( true, caller);
        }
    };
    //}}}
    // cancel_fs_button_anim {{{
    private void cancel_fs_button_anim()
    {
        String caller = "cancel_fs_button_anim";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);

        // CANCEL ANY SCHEDULED ANIMATIONS
        handler.removeCallbacks( hr_fs_button_warp   );
        handler.removeCallbacks( hr_fs_button_slide  );
        handler.removeCallbacks( hr_fs_button_insert );
        handler.removeCallbacks( hr_fs_button_alpha  );
        handler.removeCallbacks( hr_fs_button_hide   );

        NpButton np_button = get_magnify_np_button();
        if(np_button != null)
        {
            np_button.setVisibility( View.VISIBLE );
            np_button.setAlpha(1F);
        }
        fs_button_hide(caller);
    }
    //}}}
    // fs_button_hide {{{
    private void fs_button_hide(String caller)
    {
        caller += "->fs_button_hide";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);
        if(fs_button == null) return;

        fs_button.setVisibility( View.GONE );
        fs_button.setAlpha     ( 1F        ); // forget about any would-be-one-time attributes

        //XXX remnant from when a magnified button would serve as a the "setBlurred" background
        //XXX fs_button.setBlurred   ( false     ); // forget about any one-time attributes
    }
    //}}}
    // fs_button_show {{{
    private void fs_button_show(String caller)
    {
        caller += "->fs_button_show";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);
        if(fs_button == null) return;

        fs_button.setVisibility( View.VISIBLE );
        fs_button.bringToFront();
        fs_button.lockElevation( Settings.MAX_ELEVATION );
    }
    //}}}

    //}}}
    // magnify_np_layout .. (show or hide) {{{
    private void magnify_np_layout(Object np_or_np_button, String caller)
    {
        if(fs_button == null) return;

        // SAME FULL SCREEN BUTTON TAG
        if(fs_button.getTag() == np_or_np_button)
        {
//*FULLSCREEN*/Settings.MON(TAG_FULLSCREEN, "magnify_np_layout", "...np_or_np_button=["+np_or_np_button+"]: (fs_button.getTag() == np_or_np_button)");
        //  return; // wrong while swapping webviews back from full screen .. must fall through
            // TODO check for mutable attribute [text] [tag] [color] [shape]
            // ...which may justify a layout
        }

        // NEW FULL SCREEN BUTTON TAG
        fs_button_setTag(np_or_np_button, "magnify_np_layout"); // updated source button
        magnify_np_layout( caller );

    }
    private void magnify_np_layout(String caller)
    {
        caller += "->magnify_np_layout";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);

        if(fs_button != null)
        {
            FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams)fs_button.getLayoutParams();
            flp.height = Settings.DISPLAY_H;
            flp.width  = Settings.SCREEN_W;
        }

        // CURRENTLY HIDDEN XXX ... what was this all about...? (commented as it prevented fs_webView updates)
    //  if( is_view_showing(fs_button) ) return;

        handler.re_post( hr_magnify_np_layout );
    }
    private final Runnable hr_magnify_np_layout = new Runnable() {
        @Override public void run() { do_magnify_np_layout(); }
    };
    private void do_magnify_np_layout()
    {
        // INACTIVE BUTTON {{{
        String caller = "do_magnify_np_layout";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);
        if(        (fs_button == null)
            //  || (fs_button.getVisibility() != View.VISIBLE)
          ) {

//*FULLSCREEN*/Settings.MON(TAG_FULLSCREEN, caller, "fs_button == null or not visible");
            return;
          }
        //}}}
        // UPDATE TEXT AND SIZE {{{
        NpButton np_button = get_magnify_np_button();
        if(np_button != null) {
            magnify_np_show("do_magnify_np_layout");
        }
        //}}}
        // HIDE OBSOLETE BUTTON STEM {{{
        else {
            magnify_np_hide("do_magnify_np_layout(getTag failed)");
        }
        //}}}
    }
    //}}}
    // magnify_np_invalidate .. (hide) {{{
    public void magnify_np_invalidate(String caller)
    {
        // NO CURRENTLY MAGNIFIED NotePane TO REPLACE {{{
        if(fs_button == null)   /*......................................*/ return;
        NotePane magnified_np = get_magnify_np(); if(magnified_np == null) return;

        caller += "->magnify_np_invalidate";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);

        // }}}
        // update [fs_button] with new [text], [color], [zoom-out-tab]

        // LOOK FOR: a [NotePane instance] with the [same name] as the [currently magnified source]
//*FULLSCREEN*/Settings.MON(TAG_FULLSCREEN, caller, "...LOOK FOR A REPLACEMENT with the same name=["+ magnified_np.name +"] "+magnified_np);
        NotePane replacement_np = get_NotePane_named( magnified_np.name );

        // MAGNIFIED SOURCE REMOVED
        if(replacement_np != null)
        {
            // hide [removed] or [not-in-tabs] magnified tab
            magnify_np_hide(caller+": NO replacement for name=["+ magnified_np.name +"]");
        }
        // MAGNIFIED SOURCE CHANGED
        else {
            if(magnified_np != replacement_np)
                magnify_np_layout(replacement_np, caller+": OK replacement for name=["+ magnified_np.name +"] "+magnified_np);

//*FULLSCREEN*/if(magnified_np == replacement_np) Settings.MON(TAG_FULLSCREEN, caller, " ...(magnified_np == replacement_np)");
        }
    }
    //}}}

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ MAGNIFY @ }}}
    /** DIALOG */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ DIALOG @ {{{
    //* PROFILE_DIALOG */
    //{{{
    // PROFILE SAVE {{{
    // {{{
    private AlertDialog profile_save_dialog = null;

    // }}}
    // [prompt] [profile_name] [ok] [cancel] [discard] [warn] [rinse]
    // {{{
    private void profile_rinse() {
        profile_save( "SAVE RINSED CLONE AS:"
                ,      Settings.Working_profile
                ,      Settings.Get_CANCEL_LABEL()
                ,      Settings.Get_SAVE_PROFILE_LABEL()
                ,      true);   // rinse_all_tabs
    }
    private void profile_save() {
        profile_save( "SAVE PROFILE AS:"
                ,      Settings.Working_profile
                ,      Settings.Get_CANCEL_LABEL()
                ,      Settings.Get_SAVE_PROFILE_LABEL()
                ,      false);  // no rinse
    }
    private void profile_save(
            String   prompt
            , String profile_name
            , String negative_answer
            , String positive_answer
            )
    {
        profile_save(  prompt
                ,      profile_name
                ,      negative_answer
                ,      positive_answer
                ,      false);  // no rinse
    }
    //}}}
    private void profile_save(
            String          prompt
            , String        profile_name
            , String        negative_answer
            , String        positive_answer
            , final boolean rinse_all_tabs
            )
    {
        final String caller = "profile_save("+prompt+")"; // {{{
//*DIALOG*/Settings.MON(TAG_DIALOG, caller, "prompt=["+prompt+"]");
//*DIALOG*/Settings.MOM(TAG_DIALOG, "........profile_name=["+    profile_name +"]");
//*DIALOG*/Settings.MOM(TAG_DIALOG, ".....negative_answer=["+ negative_answer +"]");
//*DIALOG*/Settings.MOM(TAG_DIALOG, ".....positive_answer=["+ positive_answer +"]");
//*DIALOG*/Settings.MOM(TAG_DIALOG, "......rinse_all_tabs=["+  rinse_all_tabs +"]");
        //}}}
        // NAME HINT
        // ...You have to load a profile first: {{{
        String  title = prompt;
        String  profile_name_hint = null;
        boolean warn_only_nothing_to_load_save_or_discard = false;
        if( Profile.is_an_empty_WORKBENCH_PROFILE_NAME( profile_name ) )
        {
            title             = "You have to load a profile first";
            profile_name_hint = "...by selecting one of the available templates:\n"+ Profile.get_WORKBENCH_PROFILES_LIST();
            warn_only_nothing_to_load_save_or_discard = true;

        }
        //}}}
        // ...WORKBENCH [SAVE AS]: {{{
        else if( Profile.is_a_WORKBENCH_TEMPLATE_NAME( profile_name ) )
        {
            title = "Give a name to your new profile";

        }
        //}}}
        // ...Current profile [SAVE AS]: {{{
        else {
            if(rinse_all_tabs)
                profile_name = profile_name+"_clone"; // (for a rinsed version, suggest cloning) .. (instead of wiping the original)

            //  +"\n...special folders: "
            //  +" "+ Settings.SYMBOL_BLACK_STAR  + "FAV"
            //  +" "+ Settings.SYMBOL_HOME        + "HOME"
            //  +" "+ Settings.SYMBOL_HOME        + "PERSO"
            //  +" "+ Settings.SYMBOL_INDEX       + "index"
        }
        //}}}
        // [title] [profile_name_hint] {{{
        title = Settings.ParseUnicode( title );

        if(profile_name_hint == null)
        {
            String dir_name
                = Settings.get_dir_name ( profile_name )
                ;
//*DIALOG*/Settings.MOM(TAG_DIALOG, "............dir_name=["+ dir_name +"]");

            String base_name
                = Settings.get_base_name( profile_name )
                . replace(" ", "_")
                . toLowerCase() // TODO (state why this should not be)
                ;
//*DIALOG*/Settings.MOM(TAG_DIALOG, "...........base_name=["+ base_name +"]");

            profile_name_hint
                = TextUtils.isEmpty(dir_name)
                ?                base_name
                : dir_name +"/"+ base_name
                ;

        }
//*DIALOG*/Settings.MOM(TAG_DIALOG, "...profile_name_hint=["+ profile_name_hint +"]");
        //}}}
        // DIALOG
        // Builder .. [TITLE] [HINT] {{{
        final AlertDialog.Builder mAlertDialog_Builder = new AlertDialog.Builder( activity );

        // TITLE
        mAlertDialog_Builder.setTitle( title );

        // INPUT TEXT
        final TextView input_TextView
            = warn_only_nothing_to_load_save_or_discard
            ?  new TextView( activity )   // .. to show a readonly message
            :  new EditText( activity );  // .. to get user file name
        input_TextView.setText( profile_name_hint );
        mAlertDialog_Builder.setView( input_TextView );

        //}}}
        // DIALOG CALLBAKS
        // [CANCEL]  .. IF negative_answer != CANCEL: JUST PROCEED WITH CURRENT TASK {{{
        if( !negative_answer.toUpperCase().startsWith("CANCEL") )
        {
            final String cancel_text
                = warn_only_nothing_to_load_save_or_discard
                ?  "OK, GOT IT"
                :  "CANCEL\n(just close this dialog and proceed)";
            mAlertDialog_Builder.setNeutralButton(cancel_text, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
//*DIALOG*/Settings.MON(TAG_DIALOG, caller, "[CANCEL] .. JUST PROCEED WITH CURRENT TASK");

                    // CLOSE DIALOG
                    dialog.cancel();
                    sync_SUI_visibility(caller+": CANCEL");
                }
            });
        }

        //}}}
        // [DISCARD] .. LOAD REQUESTED PROFILE {{{
        if( !warn_only_nothing_to_load_save_or_discard) {
            final String discard_text = negative_answer;
            mAlertDialog_Builder.setNegativeButton(discard_text, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
//*DIALOG*/Settings.MON(TAG_DIALOG, caller, "[DISCARD] .. LOAD REQUESTED PROFILE ");

                    // CLOSE DIALOG
                    dialog.cancel();
                    sync_SUI_visibility(caller+": DISCARD");

                    // APPLY USER DECISION TO FORGET ABOUT ANY UNSAVED CHANGES
                    if(Settings.Working_profile_instance != null)
                        Settings.Working_profile_instance.pending_changes = 0; // on explicit user discard

                    // ...THEN [LOAD REQUESTED PROFILE]
                    load_profile_name_requested_on_check_Profile_Pending_changes();
                }
            });

        }
        //}}}
        // [OK]      .. CREATE OR UPDATE PROFILE {{{
        if( !warn_only_nothing_to_load_save_or_discard )
        {
            final String accept_text = positive_answer;
            mAlertDialog_Builder.setPositiveButton(accept_text, new DialogInterface.OnClickListener()
                    { public void onClick(DialogInterface dialog, int whichButton)
                        {
                            String profile_name = input_TextView.getText().toString().trim();
//*DIALOG*/Settings.MON(TAG_DIALOG, caller, "[OK] .. CREATE OR UPDATE PROFILE ");
//*DIALOG*/Settings.MON(TAG_DIALOG, caller, "......profile_name=["+ profile_name +"]");
                            if( !profile_name.equals("") ) {

                                // MARK PROFILES TABLE AS OBSOLETE
                                this_RTabsClient.clear_PROF_Map(caller);

                                // (UPDATE) [WORKING PROFILE NAME]
                                Settings.Working_profile = profile_name.replace(" ","_");
                                toast_long(Settings.SYMBOL_DISK+" SAVING PROFILE "+Settings.SYMBOL_DISK+"\n"+profile_name);

                                // (OPTIONALLY) [RINSE WORKING PROFILE TABS CONTENT]
                                if(rinse_all_tabs) {
                                    String err_msg = Profile.RinseAll_TABS( Settings.Working_profile_instance );
                                    if( !TextUtils.isEmpty(err_msg) ) {
                                        toast_long( err_msg ); // say more than "someting went wrong, now, you can GFY"
                                        return;
                                    }
                                }

                                // [UPDATE LOCAL STORAGE] .. f(new PROFILE)
//*DIALOG*/Settings.MON(TAG_DIALOG, caller, "[UPDATE LOCAL STORAGE]");
                                String err_msg = Profile.Save_Profile(Settings.Working_profile_instance, profile_name);
                                if( !TextUtils.isEmpty(err_msg) ) {
                                    toast_long( err_msg );
                                    return;
                                }

                                // [UPDATE PROFILES GUI]
                                profile_sync_storage_GUI();

                                // (RELOAD) AND (SYNC) [WORKING PROFILE] WITH [SERVER]
                                if(!Settings.FREEZED && !Settings.FREEZED)
                                {
                                    String cmdLine = this_RTabsClient.parse_PROFILE_cmdLine("PROFILE "+Settings.Working_profile);
                                    if( !TextUtils.isEmpty( cmdLine ) )
                                        this_RTabsClient.send(cmdLine, caller);
                                }
                                else if(Settings.OFFLINE) {
                                    toast_short(Settings.SYMBOL_offline+" OFFLINE "            +Settings.SYMBOL_offline +"\n...SERVER WILL NOT BE SYNCHRONIZED");
                                }
                                else if(Settings.FREEZED) {
                                    toast_short(Settings.SYMBOL_freezed+" FREEZED CONNECTION " +Settings.SYMBOL_freezed +"\n...SERVER WILL NOT BE SYNCHRONIZED");
                                }

                                // (OPTIONALLY) [LOAD REQUESTED PROFILE]
                                if( !Settings.Working_profile.equals(profile_name_requested_on_check_Profile_Pending_changes) )
                                    load_profile_name_requested_on_check_Profile_Pending_changes();
                            }
                        }
                    });

        }
        //}}}
        // DIALOG SHOW
        //{{{
        profile_save_dialog = mAlertDialog_Builder.create();

        profile_save_dialog.show();
        //}}}
    }
    //}}}
    // PROFILES CHANGES .. (CART EDITING) {{{
    public void check_current_profile_pending_changes_then_load_profile(String _profile_name_requested_on_check_Profile_Pending_changes)
    {
        String caller = "check_current_profile_pending_changes_then_load_profile("+_profile_name_requested_on_check_Profile_Pending_changes+")";
//*DIALOG*/Settings.MOC(TAG_DIALOG, caller);

        // REMEMBER REQUESTED PROFILE NAME
        profile_name_requested_on_check_Profile_Pending_changes = _profile_name_requested_on_check_Profile_Pending_changes;

        // ...HAVE TO COMMIT PENDING CHANGES BEFORE LOADING REQUESTED PROFILE {{{
        int changes = get_Working_profile_pending_changes();
        if(changes > 0)
        {
//*DIALOG*/Settings.MON(TAG_DIALOG, caller, "...HAVE TO COMMIT PENDING CHANGES BEFORE LOADING REQUESTED PROFILE");

            String plural = (changes > 1) ? "s" : "";
            profile_save( "Save "+changes+" Pending Change"+plural+" in "+Settings.Working_profile_instance.name+" ?"
                    ,     Settings.Working_profile_instance.name
                    ,     Settings.Get_DISCARD_CHANGES_LABEL()
                    ,     Settings.Get_SAVE_CHANGES_LABEL()
                    );
        }
        // }}}
        // NO PENDING CHANGES .. LOAD REQUESTED PROFILE {{{
        else {
//*DIALOG*/Settings.MON(TAG_DIALOG, caller, "...NO PENDING CHANGES .. LOAD REQUESTED PROFILE");
            load_profile_name_requested_on_check_Profile_Pending_changes();
        }
        // }}}
        // HIDE SHOWING PROFILES_TABLE {{{
        if(Handle.Get_cur_handle() == top_handle)
            hide_PROFILES_TABLE(caller);

        //}}}
    }
    //}}}
    // PROFILE UPDATE {{{
    private void profile_update_np(NotePane np, String msg)
    {
        String caller = "profile_update_np(np, "+msg+")";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);
        // UPDATE TAB
        Profile.Update_TAB(Settings.Working_profile_instance, np);

        // 1/2 POSTPONING UPDATE ..  (onDestroy) ..  (check_current_profile_pending_changes_then_load_profile)
        toast_short("POSTPONING PROFILE UPDATE:\n"+ Settings.Working_profile +"\n"+ msg);

// todo a context such as:
// todo ...calling this_RTabsClient.load_USER_PROFILE(Settings.Working_profile, caller);
// todo ...will get [Profile] to keep its current buffers
// todo ...instead of calling clear_profile_buffers()
// todo ..............followed by a call to load()
//  XXX this_RTabsClient.reload_Working_profile();

        // 2/2 PROFILE SAVE
    //  Profile.Save_Profile( Settings.Working_profile_instance );
    //  toast_long("Updating profile "+ Settings.Working_profile +"\n"+ msg);

        // 3/3 RELOAD AND SYNC PROFILE WITH SERVER
    //  String                  cmdLine   = this_RTabsClient.parse_PROFILE_cmdLine("PROFILE "+Settings.Working_profile);
    //  if( !TextUtils.isEmpty( cmdLine ) ) this_RTabsClient.send(cmdLine, "NotePane");

        if(this_RTabsClient != null)
        {
            this_RTabsClient.apply_TABS_LAYOUT     (RTabsClient.TABS_Map, this_RTabsClient.tabs_container, caller);
        //  this_RTabsClient.apply_SETTINGS_PALETTE(RTabsClient.TABS_Map                                 , caller);
        //  invalidate(caller);
        }
    }
    //}}}
    // load_profile_name_requested_on_check_Profile_Pending_changes {{{
    private String profile_name_requested_on_check_Profile_Pending_changes = "";

    private void load_profile_name_requested_on_check_Profile_Pending_changes()
    {
        String caller = "load_profile_name_requested_on_check_Profile_Pending_changes";
//*DIALOG*/Settings.MOC(TAG_DIALOG, caller);
//*DIALOG*/Settings.MON(TAG_DIALOG, caller, "...profile_name_requested_on_check_Profile_Pending_changes=["+ profile_name_requested_on_check_Profile_Pending_changes +"]");

        if(       profile_name_requested_on_check_Profile_Pending_changes.equals( RTabsClient.HISTORY_BAK) )
            this_RTabsClient.history_back();

        else if(  profile_name_requested_on_check_Profile_Pending_changes.equals( RTabsClient.HISTORY_FWD) )
            this_RTabsClient.history_frwd();

        else if( !profile_name_requested_on_check_Profile_Pending_changes.equals("") )
            this_RTabsClient.load_USER_PROFILE(profile_name_requested_on_check_Profile_Pending_changes, caller);

        profile_name_requested_on_check_Profile_Pending_changes = "";
    }
    //}}}
    //}}}
    //* NOTE DIALOG */
    // NOTE {{{
    // {{{
    private Dialog                note_dialog = null;

    private LinearLayout   note_dialog_layout = null; /*.......................................... */ private final int    note_dialog_layout_BGCOLOR = Color.BLACK ;

    private LinearLayout     note_text_layout = null; private final float note_text_layout_WEIGHT     = 3f; private int      note_text_layout_BGCOLOR = Color.RED   ;
    private EditText                note_text = null; private final float note_text_WEIGHT            = 1f; private final int       note_text_BGCOLOR = Color.YELLOW; private final int       note_text_FGCOLOR = Color.BLACK ;
    private EditText                note_tag  = null; private final float note_tag_WEIGHT             = 1f; private final int        note_tag_BGCOLOR = Color.DKGRAY; private final int        note_tag_FGCOLOR = Color.CYAN  ;

    private LinearLayout  note_buttons_layout = null; private final float note_buttons_layout_WEIGHT  = 0f; private int   note_buttons_layout_BGCOLOR = Color.GREEN ;

    private Button                  note_free = null; private final float note_free_WEIGHT            = 3f;                                                           private final int       note_free_FGCOLOR = Color.RED   ;
    private Button                 note_shape = null; private final float note_shape_WEIGHT           = 3f;                                                           private final int      note_shape_FGCOLOR = Color.LTGRAY;
    private Button            note_fullscreen = null; private final float note_fullscreen_WEIGHT      = 2f;                                                           private final int note_fullscreen_FGCOLOR = Color.RED   ;
    private Button                note_cancel = null; private final float note_cancel_WEIGHT          = 4f;                                                           private final int     note_cancel_FGCOLOR = Color.LTGRAY;
    private Button                  note_save = null; private final float note_save_WEIGHT            = 4f;                                                           private final int       note_save_FGCOLOR = Color.GREEN ;

    private boolean     note_fullscreen_state = false;

    //}}}
    // show_note_dialog {{{
    // {{{
    private NoteHolder current_NoteHolder;

    // }}}
    private void show_note_dialog(String title, NoteHolder noteHolder)
    {
        String caller = "show_note_dialog("+ title +")";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);
        // NoteHolder  {{{
        current_NoteHolder = noteHolder;

        if(note_dialog == null)
            create_note_dialog();

        String     text = noteHolder.get_text().replace("\\n","\n");
        String      tag = noteHolder.get_tag();
        String    shape = noteHolder.get_shape();
        NotePane     np = noteHolder.get_np();

        //}}}
        // EDITABLE FREEABLE TAGS {{{
//      boolean is_a_user_tag       =  tag.equals(Settings.FREE_TAG)
//          ||                         tag.equals(               "")
//          ||                         tag.equals(           "NOTE")
//          || !WVTools.is_a_tool_tag( tag                         );
//              Profile.is_PROFILES_BUILTIN( tag )

        boolean is_a_user_tag       =  ((np != null) && (this_RTabsClient.get_np_for_button( np.button ) != null));
        boolean is_a_changeable_tag = !is_a_SYNCED_TAG(tag);
        //}}}
        // DIALOG CONTENT {{{
        note_dialog.setTitle( title );
        note_text  .setText ( text  );
        note_tag   .setText ( tag   );
        note_shape .setText ( shape );

        note_text.setBackgroundColor((is_a_user_tag && is_a_changeable_tag) ? note_text_BGCOLOR : Color.LTGRAY);
        //}}}
        // DIALOG BUTTONS {{{
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, "", "...................tag=["+ tag               +"]");
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, "", ".........is_a_user_tag=["+ is_a_user_tag     +"]");
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, "", "...is_a_changeable_tag=["+ is_a_changeable_tag +"]");
        // EDIT DISABLED
        if( is_a_user_tag )
        {
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, "", "BUTTONS [free shape save cancel] ENABLED");
            /*.....................................*/ note_tag   .setEnabled( true                );
            /*.....................................*/ note_text  .setEnabled( is_a_changeable_tag ); // REALDONLY
             note_free  .setVisibility(View.VISIBLE); note_free  .setEnabled( true                );
             note_shape .setVisibility(View.VISIBLE); note_shape .setEnabled( true                );
             note_save  .setVisibility(View.VISIBLE); note_save  .setEnabled( true                );
             note_cancel.setVisibility(View.VISIBLE); note_cancel.setEnabled( true                );

            // WITH KEYBOARD
            note_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        // EDIT ENABLED
        else {
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, "", "BUTTONS [free shape save cancel] DISABLED");
            /*.....................................*/ note_tag   .setEnabled(false);
            /*.....................................*/ note_text  .setEnabled(false);
            note_free  .setVisibility(View.GONE    ); note_free  .setEnabled(false);
            note_shape .setVisibility(View.GONE    ); note_shape .setEnabled(false);
            note_save  .setVisibility(View.GONE    ); note_save  .setEnabled(false);
            note_cancel.setVisibility(View.GONE    ); note_cancel.setEnabled(false);

            // NO KEYBOARD
            note_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );
        }
        //}}}
        // BUILD A DEFAULT DYNAMIC INFO from KEYWORDS URL-TAGS {{{
        if( !NotePane.Text_contains_some_info( text ) )
        {
            String url      = this_RTabsClient.get_tag_url( noteHolder.get_tag() );
            String keywords = Settings.Parse_keywords_from_url( url );
            if(keywords.length() > 0)
                note_text.append(NotePane.INFO_SEP+"\n"+keywords);

            // [https://en.m.wikipedia.org/wiki/World_War_II#/media/File%3ADestroyed_Warsaw%2C_capital_of_Poland%2C_January_1945.jpg]
            // (...wikipedia World War II Destroyed Warsaw capital Poland January 1945)
        }
        //}}}
        //// XXX {{{
        //NotePane np = noteHolder.get_np();
        //if(np != null) {
        //    note_text.append("\n...np.text=["+np.text+"]");
        //}
        //// XXX }}}
        note_dialog_layout("show_note_dialog");
        note_dialog.show();
    }
    //}}}
    // note_dialog_layout {{{
    private void note_dialog_layout(String caller)
    {
        caller += "->note_dialog_layout";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);

        if(D) MLog.log(caller);
        if(note_dialog == null)
            return;

        LinearLayout.LayoutParams llp;

        boolean has_a_tag = !note_tag.getText().toString().equals("");

        // t_width t_height {{{
        int t_width   =  Settings.SCREEN_W;
        int t_height  = (Settings.DEV_RATIO > 1)
            ?            Settings.DISPLAY_H / 4   // landscape (less V-SPACE)
            :            Settings.DISPLAY_H / 2;  // portrait  (more V-SPACE)

        // [text-tag] .. share width or height
        if( has_a_tag ) {
            if(Settings.DEV_RATIO > 1)  t_width  /= 2;
            else                        t_height /= 2;
        }

        if(note_fullscreen_state) t_height *= 1.8;

        llp = (LinearLayout.LayoutParams)note_text.getLayoutParams();
        llp.width  = t_width;
        llp.height = t_height;
        note_text.setLayoutParams( llp );
        note_tag .setLayoutParams( llp );

        //}}}

        // note_text note_tag [orientation] {{{
        note_text_layout.setOrientation( (Settings.DEV_RATIO       >  1) ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
         //}}}
        // note_tag [hide or show] {{{
    //  if(!has_a_tag && note_text_layout.indexOfChild( note_tag ) >= 0)   note_text_layout.removeView( note_tag );
    //  if( has_a_tag && note_text_layout.indexOfChild( note_tag ) <  0)   note_text_layout.addView   ( note_tag );
        if(!has_a_tag                                                  )   note_tag.setVisibility(View.GONE   );
        if( has_a_tag                                                  )   note_tag.setVisibility(View.VISIBLE);

        //  }}}
        // buttons [width height ] {{{
/*
        int b_width   = 1 * Settings.SCREEN_W  /  2;
        int b_height  =     Settings.DISPLAY_H / 12;

        llp = (LinearLayout.LayoutParams)note_cancel.getLayoutParams();
        llp.width     = b_width;
        note_free   .setLayoutParams( llp );
        note_cancel .setLayoutParams( llp );
        note_save   .setLayoutParams( llp );
*/
        llp       = (LinearLayout.LayoutParams)note_buttons_layout.getLayoutParams();
        llp.width = Settings.SCREEN_W;
        note_buttons_layout.setLayoutParams( llp );

        //}}}
        // note_cancel note_save [text_size] {{{
        int text_size = Math.min(Settings.SCREEN_W, Settings.DISPLAY_H) / 20 ;
        note_cancel    .setTextSize( text_size /  2);
        note_free      .setTextSize( text_size /  2);
        note_fullscreen.setTextSize( text_size /  2);
        note_shape     .setTextSize( text_size /  2);
        note_save      .setTextSize( text_size /  2);

        //}}}

    }
    //}}}
    // create_note_dialog {{{


    private void create_note_dialog()
    {
        if(D) MLog.log("create_note_dialog");
        LinearLayout.LayoutParams llp;

        // note_dialog {{{
        note_dialog = new Dialog( activity );

        // WITH KEYBOARD
    //  note_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // note_dialog ATTRIBUTES
        WindowManager.LayoutParams wm_lp = new WindowManager.LayoutParams(0,0);
        wm_lp.copyFrom(note_dialog.getWindow().getAttributes());
        wm_lp.gravity = Gravity.TOP;
    //  wm_lp.flags   = WindowManager.LayoutParams.FLAG_FULLSCREEN;
    //  wm_lp.width   = ViewGroup.LayoutParams.MATCH_PARENT;
    //  wm_lp.height  = ViewGroup.LayoutParams.MATCH_PARENT;
        note_dialog.getWindow().setAttributes( wm_lp );

        //}}}

        // -[note_dialog_layout] {{{
        note_dialog_layout = new LinearLayout( activity );
/*
        // onMeasure {{{
        note_dialog_layout = new LinearLayout( activity ) {
            @Override
            protected void onMeasure(final int wms, final int hms) {

                int ow = getMeasuredWidth (); int nw = MeasureSpec.getSize(wms);
                int oh = getMeasuredHeight(); int nh = MeasureSpec.getSize(hms);

                toast_long("old=["+ow+"x"+oh+"]\n"
                        +  "new=["+ow+"x"+nh+"]");

                super.onMeasure(wms, hms);
            }
        };
        //}}}
*/
        // -[note_dialog_layout] PARAMS
        //llp         = new LinearLayout.LayoutParams(0,0,1);
        //llp.width   = ViewGroup.LayoutParams.MATCH_PARENT;
        //llp.height  = ViewGroup.LayoutParams.MATCH_PARENT;
        //note_dialog_layout.setLayoutParams( llp );

        // -[note_dialog_layout] ATTRIBUTES
        note_dialog_layout.setOrientation( LinearLayout.VERTICAL );
        note_dialog_layout.setBackgroundColor( note_dialog_layout_BGCOLOR );
    //  note_dialog_layout.setGravity(Gravity.TOP);
    //  note_dialog_layout.setGravity(Gravity.FILL);

        // -[note_dialog_layout] PARENTAGE
        note_dialog.setContentView( note_dialog_layout );

        //}}}

        // --[note_text_layout] .. (note_text & note_tag) {{{
        note_text_layout = new LinearLayout( activity );

        // --[note_text_layout] PARAMS
        llp         = new LinearLayout.LayoutParams(0,0,note_text_layout_WEIGHT);
        llp.width   = ViewGroup.LayoutParams.WRAP_CONTENT;
        llp.height  = ViewGroup.LayoutParams.WRAP_CONTENT;
        note_text_layout.setLayoutParams( llp );

        // --[note_text_layout] ATTRIBUTES
//note_text_layout.setBackgroundColor( note_text_layout_BGCOLOR );
        note_text_layout.setOrientation( LinearLayout.HORIZONTAL );

        // --[note_text_layout] PARENTAGE
        note_dialog_layout.addView( note_text_layout );

        //}}}
        // ---[note_text] {{{
        note_text = new  EditText( activity );

        // ---[note_text] PARAMS
        llp         = new LinearLayout.LayoutParams(0,0,note_text_WEIGHT);
        llp.width   = ViewGroup.LayoutParams.WRAP_CONTENT;
        llp.height  = ViewGroup.LayoutParams.WRAP_CONTENT;
        note_text.setLayoutParams( llp );

        // ---[note_text] ATTRIBUTES
        note_text.setTextColor      ( note_text_FGCOLOR                  );
        note_text.setBackgroundColor( note_text_BGCOLOR                  );
        note_text.setGravity        ( Gravity.TOP                        );
        note_text.setTypeface       ( Settings.getNotoTypeface()         );
        note_text.setPadding        ((int)note_text.getTextSize()/2,1,1,1); // left top right bottom
        note_text.setCursorVisible  ( true );

        Settings.SetEditTextCursorColor(note_text, Color.RED);

        // ---[note_text] PARENTAGE
        note_text_layout.addView( note_text );

        // }}}
        // ---[note_tag] {{{
        note_tag  = new  EditText( activity );
        note_tag .setCursorVisible( true );

        // ---[note_tag] PARAMS
        llp         = new LinearLayout.LayoutParams(0,0,note_tag_WEIGHT);
        llp.width   = ViewGroup.LayoutParams.WRAP_CONTENT;
        llp.height  = ViewGroup.LayoutParams.WRAP_CONTENT;
        note_tag.setLayoutParams( llp );

        // ---[note_tag] ATTRIBUTES
        note_tag .setTextColor      ( note_tag_FGCOLOR                   );
        note_tag .setBackgroundColor( note_tag_BGCOLOR                   );
        note_tag .setGravity        ( Gravity.TOP                        );
        note_tag .setTypeface       ( Settings.getNotoTypeface()         );
        note_tag .setPadding        ((int)note_tag.getTextSize()/2,1,1,1 ); // left top right bottom

        // ---[note_tag] PARENTAGE
        note_text_layout.addView( note_tag  );

        // }}}

        // --[note_buttons_layout] .. (note_free & note_cancel & note_save ) {{{
        note_buttons_layout = new LinearLayout( activity );

        // --[note_buttons_layout] PARAMS
        llp         = new LinearLayout.LayoutParams(0,0,note_buttons_layout_WEIGHT);
        llp.width   = ViewGroup.LayoutParams.WRAP_CONTENT;
        llp.height  = ViewGroup.LayoutParams.WRAP_CONTENT;
        note_buttons_layout.setLayoutParams( llp );

        // --[note_buttons_layout] ATTRIBUTES
//note_buttons_layout.setBackgroundColor  ( note_buttons_layout_BGCOLOR );
        note_buttons_layout.setOrientation( LinearLayout.HORIZONTAL     );
    //  note_buttons_layout.setGravity    ( Gravity.CENTER              );

        // --[note_buttons_layout] PARENTAGE
        note_dialog_layout.addView( note_buttons_layout );

        //}}}
        // ---[note_free] {{{
        note_free = new  Button( activity );

        // ---[note_free] PARAMS
        llp         = new LinearLayout.LayoutParams(0,0,note_free_WEIGHT);
        llp.width   = ViewGroup.LayoutParams.WRAP_CONTENT;
        llp.height  = ViewGroup.LayoutParams.WRAP_CONTENT;
        note_free.setLayoutParams( llp );

        // ---[note_free] ATTRIBUTES
        note_free.setText(  activity.getString(R.string.free_tab) );
        note_free.setTextColor      ( note_free_FGCOLOR );

        // ---[note_free] PARENTAGE
        note_buttons_layout.addView( note_free );

        // }}}
        // ---[note_shape] {{{
        note_shape = new  Button( activity );

        // ---[note_shape] PARAMS
        llp         = new LinearLayout.LayoutParams(0,0,note_shape_WEIGHT);
        llp.width   = ViewGroup.LayoutParams.WRAP_CONTENT;
        llp.height  = ViewGroup.LayoutParams.WRAP_CONTENT;
        note_shape.setLayoutParams( llp );

        // ---[note_shape] ATTRIBUTES
        note_shape.setText( activity.getString(R.string.shape) );
        note_shape.setTextColor      ( note_shape_FGCOLOR );

        // ---[note_shape] PARENTAGE
        note_buttons_layout.addView(note_shape);

        //}}}
        // ---[note_fullscreen] {{{
        note_fullscreen = new  Button( activity );

        // ---[note_fullscreen] PARAMS
        llp         = new LinearLayout.LayoutParams(0,0,note_fullscreen_WEIGHT);
        llp.width   = ViewGroup.LayoutParams.WRAP_CONTENT;
        llp.height  = ViewGroup.LayoutParams.WRAP_CONTENT;
        note_fullscreen.setLayoutParams( llp );

        // ---[note_fullscreen] ATTRIBUTES
        note_fullscreen.setText(Settings.SYMBOL_ARROW_DOWN_PAIR);
        note_fullscreen.setTextColor      ( note_fullscreen_FGCOLOR );

        // ---[note_fullscreen] PARENTAGE
        note_buttons_layout.addView(note_fullscreen);

        //}}}
        // ---[note_cancel] {{{
        note_cancel = new  Button( activity );

        // ---[note_cancel] PARAMS
        llp         = new LinearLayout.LayoutParams(0,0,note_cancel_WEIGHT);
        llp.width   = ViewGroup.LayoutParams.WRAP_CONTENT;
        llp.height  = ViewGroup.LayoutParams.WRAP_CONTENT;
        note_cancel.setLayoutParams( llp );

        // ---[note_cancel] ATTRIBUTES
        note_cancel.setText( activity.getString(R.string.cancel) );
        note_cancel.setTextColor      ( note_cancel_FGCOLOR );

        // ---[note_cancel] PARENTAGE
        note_buttons_layout.addView(note_cancel);

        //}}}
        // ---[note_save] {{{
        note_save = new  Button( activity );

        // ---[note_save] PARAMS
        llp         = new LinearLayout.LayoutParams(0,0,note_save_WEIGHT);
        llp.width   = ViewGroup.LayoutParams.WRAP_CONTENT;
        llp.height  = ViewGroup.LayoutParams.WRAP_CONTENT;
        note_save.setLayoutParams( llp );

        // ---[note_save] ATTRIBUTES
        note_save.setText( activity.getString(R.string.save_changes) );
        note_save.setTextColor      ( note_save_FGCOLOR );

        // ---[note_save] PARENTAGE
        note_buttons_layout.addView(note_save);

        // }}}

        // Free OnClickListener {{{
        note_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //  String name = (current_NoteHolder == null) ? "" : current_NoteHolder.get_name().replace("tab","");
                note_text.setText( Settings.SYMBOL_EMPTY   );//name );
                note_tag .setText( Settings.COMMENT_STRING );
            }
        });
        //}}}
        // Cancel OnClickListener {{{
        note_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( is_dialog_showing(note_dialog) ) note_dialog.dismiss();
                sync_SUI_visibility("note_cancel");
            }
        });
        //}}}
        // Fullscreen OnClickListener {{{
        note_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note_fullscreen_state = !note_fullscreen_state;
                note_fullscreen.setText( note_fullscreen_state ? Settings.SYMBOL_ARROW_UP_PAIR : Settings.SYMBOL_ARROW_DOWN_PAIR);
                note_dialog_layout("note_fullscreen");
            }
        });
        //}}}
        // Shape OnClickListener {{{
        note_shape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shape = NotePane.Get_next_shape( note_shape.getText().toString() );
                note_shape.setText( shape );
            }
        });
        //}}}
        // Done OnClickListener {{{
        note_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note_dialog_eval();
                if( is_dialog_showing(note_dialog) ) note_dialog.dismiss();
                sync_SUI_visibility("note_save");
            }
        });

        // }}}
/*
        // look for soft keyboard visibility {{{
        note_cancel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int[] xy = new int[2]; note_dialog_layout.getLocationOnScreen( xy );
                int    l = xy[0];
                int    t = xy[1];
                int    w = note_dialog_layout.getWidth();
                int    h = note_dialog_layout.getHeight();
            //  note_dialog.setTitle("current: ["+l+"@"+t+"] ["+w+"x"+h+"]");
                toast_long( "note_dialog_layout: ["+l+"@"+t+"] ["+w+"x"+h+"]");
                return true;
            }
        });
        //}}}
*/
    }
    //}}}
    // note_dialog_eval {{{
    private void note_dialog_eval()
    {
        // NOTE
        if(current_NoteHolder != null)
            current_NoteHolder.setValue( note_text .getText().toString()
                    ,                    note_tag  .getText().toString()
                    ,                    note_shape.getText().toString()
                    );
    }
    //}}}
    //}}}
    //* IP-PORT DIALOG */
    //{{{
    // show_ip_dialog .. display: [build open listen close] {{{
    private void show_ip_dialog()
    {
        ip_dialog_layout("show_ip_dialog");

        sync_ip_dialog();

        ip_dialog.show();
    }
    //}}}
    // ip_dialog_layout {{{
    // {{{
    private Dialog                ip_dialog = null;

    private LinearLayout   ip_dialog_layout = null;

    private LinearLayout       ip_po_layout = null;
    private LinearLayout          ip_layout = null;
    private TextView                 ip_Lbl = null;
    private TextView                 ip_Rbl = null;
    private NumberPicker             ip_np1 = null;
    private NumberPicker             ip_np2 = null;
    private NumberPicker             ip_np3 = null;
    private NumberPicker             ip_np4 = null;

    private LinearLayout          po_layout = null;
    private TextView                 po_Lbl = null;
    private TextView                 po_Rbl = null;
    private NumberPicker             po_np1 = null;
    private NumberPicker             po_np2 = null;
    private NumberPicker             po_np3 = null;
    private NumberPicker             po_np4 = null;
    private NumberPicker             po_np5 = null;

    private LinearLayout  ip_buttons_layout = null;
    private Button                  ip_done = null;
    private Button                ip_cancel = null;

    //}}}
    // ip_dialog_layout {{{
    private void ip_dialog_layout(String caller)
    {
        caller += "->ip_dialog_layout";
//*FULLSCREEN*/Settings.MOC(TAG_FULLSCREEN, caller);

        if(D) MLog.log(caller);

        // BUILD
        // build [ip_dialog] {{{
        if(ip_dialog == null)
            create_ip_dialog();

        // }}}

        // ORIENTATION
        // GEOMETRY f(Orientation) [LABELS] & [PICKERS] {{{
        boolean orientation_landscape = (Settings.DEV_RATIO > 1);

        int text_size       = Settings.DISPLAY_H / 20;

        int ip_po_height    = Settings.DISPLAY_H /  4;
        int ip_po_width     = Settings.SCREEN_W  /  5;

        int button_height   = Settings.DISPLAY_H / 12;
        int button_width    = Settings.SCREEN_W /   5;

        // LEFT-RIGHT
        if(orientation_landscape) {
            ip_Lbl.setVisibility( View.VISIBLE ); ip_Rbl.setVisibility( View.GONE    );
            po_Lbl.setVisibility( View.GONE    ); po_Rbl.setVisibility( View.VISIBLE );
        }
        // TOP BOTTOM
        else {
            ip_Lbl.setVisibility( View.VISIBLE ); ip_Rbl.setVisibility( View.GONE    );
            po_Lbl.setVisibility( View.VISIBLE ); po_Rbl.setVisibility( View.GONE    );
        }

        ip_po_layout.setOrientation(orientation_landscape ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
        ViewGroup.LayoutParams              vlp = ip_po_layout.getLayoutParams();
        vlp.width = Settings.SCREEN_W;

        // IP
        LinearLayout.LayoutParams  ip_label_llp = new LinearLayout.LayoutParams(ip_po_width, ip_po_height, 4);
        ip_Lbl      .setLayoutParams( ip_label_llp );
        ip_Rbl      .setLayoutParams( ip_label_llp );

        // IP PORT
        LinearLayout.LayoutParams  po_label_llp = new LinearLayout.LayoutParams(ip_po_width, ip_po_height, 5);
        po_Lbl      .setLayoutParams( po_label_llp );
        po_Rbl      .setLayoutParams( po_label_llp );

        // DONE CANCEL
        LinearLayout.LayoutParams           blp = new LinearLayout.LayoutParams(button_width, button_height, 2);
        ip_done     .setLayoutParams( blp );
        ip_cancel   .setLayoutParams( blp );

        ip_Lbl      .setTextSize( text_size    );
        ip_Rbl      .setTextSize( text_size    );
        po_Lbl      .setTextSize( text_size    );
        po_Rbl      .setTextSize( text_size    );
        ip_cancel   .setTextSize( text_size / 2);
        ip_done     .setTextSize( text_size / 2);

        //}}}

    }
//}}}
    // create_ip_dialog {{{


    private void create_ip_dialog()
    {
        if(D) MLog.log("create_ip_dialog");
        // BUILD
        // ip_dialog {{{
        ip_dialog = new Dialog( activity );

        // NO KEYBOARD
        ip_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // ip_dialog ATTRIBUTES
        //ip_dialog.getWindow().setTitleColor        ( Color.GREEN ); // no effect
        //ip_dialog.getWindow().setStatusBarColor    ( Color.GREEN ); // no effect
        //ip_dialog.getWindow().setNavigationBarColor( Color.GREEN ); // no effect
        //((View)(ip_po_layout.getParent())).setScaleX(2f);
        //((View)(ip_po_layout.getParent())).setScaleY(2f);
        WindowManager.LayoutParams wm_lp = new WindowManager.LayoutParams();
        wm_lp.copyFrom(ip_dialog.getWindow().getAttributes());
        wm_lp.flags   = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        wm_lp.gravity = Gravity.CENTER;
        ip_dialog.getWindow().setAttributes( wm_lp );

        //}}}
        // -[ip_dialog_layout] {{{
        ip_dialog_layout = new LinearLayout( activity );

        // -[ip_dialog_layout] ATTRIBUTES
        ip_dialog_layout.setGravity    ( Gravity.FILL_HORIZONTAL );
        ip_dialog_layout.setOrientation( LinearLayout.VERTICAL   );

        // -[ip_dialog_layout] PARENTAGE
        ip_dialog.setContentView( ip_dialog_layout );
        //}}}

        // --[ip_po_layout] {{{
        ip_po_layout = new LinearLayout( activity );

        // --[ip_po_layout] ATTRIBUTES
        ip_po_layout.setBackgroundColor(        Color.RED);//BLACK      ); //XXX
        ip_po_layout.setGravity        (      Gravity.CENTER     );
        ip_po_layout.setOrientation    ( LinearLayout.HORIZONTAL );

        // --[ip_po_layout] PARENTAGE
        ip_dialog_layout.addView( ip_po_layout );

        //}}}

        // ---[ip_layout] {{{
        ip_layout = new LinearLayout( activity );

        // ---[ip_layout] ATTRIBUTES
        ip_layout.setBackgroundColor( Color.parseColor("#330000") );
        ip_layout.setGravity        (      Gravity.CENTER         );
        ip_layout.setOrientation    ( LinearLayout.HORIZONTAL     );

        LinearLayout.LayoutParams llp;
        llp        = new LinearLayout.LayoutParams(1,1);
        llp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
        llp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        llp.weight = 1;
        ip_layout.setLayoutParams( llp );

        // ---[ip_layout] PARENTAGE
        ip_po_layout.addView( ip_layout );

        //}}}
        // ----[ip_lbl] [ip_np[1-4] {{{
        // LABEL


        // ----[ip_lbl] VALUES - ATTRIBUTES & PARENTAGE
        ip_Lbl = new TextView( activity );                                                      ip_layout.addView( ip_Lbl );
        ip_np1 = new NumberPicker( activity ); ip_np1.setMinValue(0 ); ip_np1.setMaxValue(255); ip_layout.addView( ip_np1 );
        ip_np2 = new NumberPicker( activity ); ip_np2.setMinValue(0 ); ip_np2.setMaxValue(255); ip_layout.addView( ip_np2 );
        ip_np3 = new NumberPicker( activity ); ip_np3.setMinValue(0 ); ip_np3.setMaxValue(255); ip_layout.addView( ip_np3 );
        ip_np4 = new NumberPicker( activity ); ip_np4.setMinValue(0 ); ip_np4.setMaxValue(255); ip_layout.addView( ip_np4 );
        ip_Rbl = new TextView( activity );                                                      ip_layout.addView( ip_Rbl );

        LinearLayout.LayoutParams np_llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        np_llp.weight = 1;
        ip_np1      .setLayoutParams( np_llp );
        ip_np2      .setLayoutParams( np_llp );
        ip_np3      .setLayoutParams( np_llp );
        ip_np4      .setLayoutParams( np_llp );
        np_llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        np_llp.weight = 4;
        ip_Lbl      .setLayoutParams( np_llp );
        np_llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        np_llp.weight = 4;
        ip_Rbl      .setLayoutParams( np_llp );

        // ----[ip_lbl] ATTRIBUTES
        ip_Lbl.setText( activity.getString(R.string.ip) );
        ip_Rbl.setText( activity.getString(R.string.ip) );

        ip_Lbl.setTextColor   ( Color.RED      );
        ip_Rbl.setTextColor   ( Color.RED      );

        ip_Lbl.setGravity     ( Gravity.CENTER );//( Gravity.START | Gravity.CENTER_VERTICAL );
        ip_Rbl.setGravity     ( Gravity.CENTER );//( Gravity.END   | Gravity.CENTER_VERTICAL );

//ip_Lbl.setBackgroundColor( Color.BLUE ); //XXX
//ip_Rbl.setBackgroundColor( Color.BLUE ); //XXX
        //}}}

        // ---[po_layout] {{{
        po_layout = new LinearLayout( activity );

        // ---[po_layout] ATTRIBUTES
        po_layout.setBackgroundColor( Color.parseColor("#000044") );
        po_layout.setGravity        (      Gravity.CENTER         );
        po_layout.setOrientation    ( LinearLayout.HORIZONTAL     );

        llp        = new LinearLayout.LayoutParams(1,1);
        llp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
        llp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        llp.weight = 1;
        po_layout.setLayoutParams( llp );

        // ---[po_layout] PARENTAGE
        ip_po_layout.addView( po_layout );

        //}}}
        // ----[po_Lbl] [po_np[1-5] [po_Rbl] {{{
        // LABEL

        // VALUES - ATTRIBUTES & PARENTAGE
        po_Lbl = new TextView    ( activity );                                                  po_layout.addView( po_Lbl );
        po_np1 = new NumberPicker( activity ); po_np1.setMinValue(0 ); po_np1.setMaxValue(  6); po_layout.addView( po_np1 );
        po_np2 = new NumberPicker( activity ); po_np2.setMinValue(0 ); po_np2.setMaxValue(  9); po_layout.addView( po_np2 );
        po_np3 = new NumberPicker( activity ); po_np3.setMinValue(0 ); po_np3.setMaxValue(  9); po_layout.addView( po_np3 );
        po_np4 = new NumberPicker( activity ); po_np4.setMinValue(0 ); po_np4.setMaxValue(  9); po_layout.addView( po_np4 );
        po_np5 = new NumberPicker( activity ); po_np5.setMinValue(0 ); po_np5.setMaxValue(  9); po_layout.addView( po_np5 );
        po_Rbl = new TextView    ( activity );                                                  po_layout.addView( po_Rbl );

        np_llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        np_llp.weight = 1;
        po_np1      .setLayoutParams( np_llp );
        po_np2      .setLayoutParams( np_llp );
        po_np3      .setLayoutParams( np_llp );
        po_np4      .setLayoutParams( np_llp );
        po_np5      .setLayoutParams( np_llp );
        np_llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        np_llp.weight = 5;
        po_Lbl      .setLayoutParams( np_llp );
        np_llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        np_llp.weight = 5;
        po_Rbl      .setLayoutParams( np_llp );

        po_Lbl.setText( activity.getString(R.string.port) );
        po_Rbl.setText( activity.getString(R.string.port) );

        po_Lbl.setTextColor   ( Color.BLUE       );
        po_Rbl.setTextColor   ( Color.BLUE       );

        po_Lbl.setGravity     ( Gravity.CENTER );//( Gravity.START | Gravity.CENTER_VERTICAL );
        po_Rbl.setGravity     ( Gravity.CENTER );//( Gravity.END   | Gravity.CENTER_VERTICAL );

//po_Lbl.setBackgroundColor( Color.RED ); //XXX
//po_Rbl.setBackgroundColor( Color.RED ); //XXX
        //}}}

        // --[ip_buttons_layout] .. (ip_done & ip_cancel) {{{
        ip_buttons_layout = new LinearLayout( activity );

        // --[ip_buttons_layout] ATTRIBUTES
        ip_buttons_layout.setBackgroundColor(        Color.BLACK      );
        ip_buttons_layout.setGravity        (      Gravity.CENTER     );
        ip_buttons_layout.setOrientation    ( LinearLayout.HORIZONTAL );

        // --[ip_buttons_layout] PARENTAGE
        ip_dialog_layout.addView( ip_buttons_layout );

        //}}}
        // ---[ip_cancel] {{{
        ip_cancel = new  Button( activity );

        // ---[ip_cancel] ATTRIBUTES
        ip_cancel.setText( activity.getString(R.string.cancel) );
    //  ip_cancel.setGravity  ( Gravity.FILL_HORIZONTAL );
        ip_cancel.setTextColor(   Color.LTGRAY          );
        //ViewGroup.MarginLayoutParams vlp = new ViewGroup.MarginLayoutParams(ip_cancel.getWidth(), ip_cancel.getHeight());
        //vlp.leftMargin = 24;
        //ip_cancel.setLayoutParams(vlp);

        // ---[ip_cancel] PARENTAGE-
        ip_buttons_layout.addView(ip_cancel);
        //}}}
        // ---[ip_done] button {{{
        ip_done = new  Button( activity );

        // ---[ip_done] ATTRIBUTES
        ip_done.setText( activity.getString(R.string.done) );
    //  ip_done.setGravity  ( Gravity.FILL_HORIZONTAL );
        ip_done.setTextColor(   Color.GREEN           );
        //  ip_done.setBackgroundColor(Color.BLACK);

        // ---[ip_done] PARENTAGE
        ip_buttons_layout.addView(ip_done);
        // }}}

        // LISTENERS
        // NumberPicker OnValueChangeListener {{{
/*
        ip_np1.setOnValueChangedListener( ip_dialog_listener );
        ip_np2.setOnValueChangedListener( ip_dialog_listener );
        ip_np3.setOnValueChangedListener( ip_dialog_listener );
        ip_np4.setOnValueChangedListener( ip_dialog_listener );

        po_np1.setOnValueChangedListener( ip_dialog_listener );
        po_np2.setOnValueChangedListener( ip_dialog_listener );
        po_np3.setOnValueChangedListener( ip_dialog_listener );
        po_np4.setOnValueChangedListener( ip_dialog_listener );
        po_np5.setOnValueChangedListener( ip_dialog_listener );
*/
        // }}}
        // Done OnClickListener {{{
        ip_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip_dialog_eval();
                ip_dialog.dismiss();
                //hide_system_bars("ip_done");
                sync_SUI_visibility("ip_done");
            }
        });

        // }}}
        // Cancel OnClickListener {{{
        ip_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Settings.SaveSettings("IP DIALOG ip_cancel");
                ip_dialog.dismiss();
                //hide_system_bars("ip_cancel");
                sync_SUI_visibility("ip_cancel");
            }
        });

        //}}}
    }
    //}}}
    //}}}
    // sync_ip_dialog .. open {{{
    private void sync_ip_dialog()
    {
        // IP
        ip_dialog.setTitle("SERVER current address is "+ Settings.SERVER_IP +" : "+ Settings.SERVER_PORT);

        String[] args = null;
        try {
            args =                             Settings.SERVER_IP           .split("\\.");
            if(D) MLog.log("Settings.SERVER_IP=["+  Settings.SERVER_IP        +"].split: args.length=["+args.length+"]");
            if(args.length != 4)        args = Settings.DEFAULT_SERVER_IP_16.split("\\.");

            ip_np1.setValue( Integer.parseInt(args[0]) );
            ip_np2.setValue( Integer.parseInt(args[1]) );
            ip_np3.setValue( Integer.parseInt(args[2]) );
            ip_np4.setValue( Integer.parseInt(args[3]) );
        }
        catch(Exception ex) { MLog.log_ex(ex, "sync_ip_dialog"); }

        // PORT
        int port= Settings.SERVER_PORT;
        int po5 = port % 10; port /= 10; //  6553[5]
        int po4 = port % 10; port /= 10; //  655[3]
        int po3 = port % 10; port /= 10; //  65[5]
        int po2 = port % 10; port /= 10; //  6[5]
        int po1 = port     ;             // [6]

        po_np1.setValue( po1 );
        po_np2.setValue( po2 );
        po_np3.setValue( po3 );
        po_np4.setValue( po4 );
        po_np5.setValue( po5 );

    }
    //}}}
    // ip_dialog_eval {{{
    private void ip_dialog_eval()
    {
        // COMMIT SOFT KEYBOARD INPUT
        ip_dialog_layout.clearFocus();

        // IP
        int ip1 = ip_np1.getValue();
        int ip2 = ip_np2.getValue();
        int ip3 = ip_np3.getValue();
        int ip4 = ip_np4.getValue();

        // PORT
        int po1 = po_np1.getValue();
        int po2 = po_np2.getValue();
        int po3 = po_np3.getValue();
        int po4 = po_np4.getValue();
        int po5 = po_np5.getValue();
        int port
            = po1 * 10000
            + po2 * 1000
            + po3 * 100
            + po4 * 10
            + po5 ;

        String cmd
            =   "IP="+ String.format("%d.%d.%d.%d", ip1, ip2, ip3, ip4)
            +" PORT="+ port;
        Settings.Send_SIGNIN(cmd, "ip_dialog_eval");

        sync_server_connection("ip_dialog_eval");

        // system-UI .. show or hide
        sync_SUI_visibility("ip_dialog_eval");

    }
    //}}}
//    // ip_dialog_listener {{{
//    private final NumberPicker.OnValueChangeListener ip_dialog_listener = new NumberPicker.OnValueChangeListener()
//    {
//        @Override
//        public void onValueChange(NumberPicker picker, int oldVal, int newVal)
//        {
//            //ip_dialog_eval();
//        }
//
//    };
//    //}}}
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ DIALOG @ }}}
    /** WEBVIEW */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ WEBVIEW @ {{{
    // {{{
  //private static final int    PAGE_BOUNDARY_COLOR     = Color.parseColor("#E0ff8080");
  //private static       int[]  PAGE_BOUNDARY_COLORS    = Settings.COLORS_ECC; // XXX MAY BE A NULL ARRAY AT RUN TIME

    private static final int    FS_WTITLE_WAITCOLOR     = Color.parseColor("#ff222222");
    private static final int    FS_WTITLE_HAVECOLOR     = Color.parseColor("#ffdddddd");
    private static final int    FS_WTITLE_TEXTCOLOR     = Color.parseColor("#ff666666");

    private static final int    FS_BROWSE_BACKCOLOR     = FS_WTITLE_HAVECOLOR;
    private static final int    FS_BROWSE_TEXTCOLOR     = FS_WTITLE_TEXTCOLOR;

    private static final int    FS_SWAP_BACKCOLOR       = FS_WTITLE_HAVECOLOR;
    private static final int    FS_SWAP_TEXTCOLOR       = FS_WTITLE_TEXTCOLOR;

    private static final int    FS_BOOKMARK_BACKCOLOR   = FS_WTITLE_HAVECOLOR;
    private static final int    FS_BOOKMARK_TEXTCOLOR   = FS_WTITLE_TEXTCOLOR;

    private static final int    FS_GOBACK_BACKCOLOR     = FS_WTITLE_HAVECOLOR;
    private static final int    FS_GOBACK_TEXTCOLOR     = FS_WTITLE_TEXTCOLOR;

    private static final int    FS_GOFORWARD_BACKCOLOR  = FS_WTITLE_HAVECOLOR;
    private static final int    FS_GOFORWARD_TEXTCOLOR  = FS_WTITLE_TEXTCOLOR;

    private static final int    FS_SEARCH_BACKCOLOR     = Color.WHITE;
    private static final int    FS_SEARCH_TEXTCOLOR     = Color.BLACK;

    private static final int    FS_ANCHOR_TAG_KEY       = 2;

    public          NpButton    fs_wtitle   ;
    public          NpButton    fs_wtitle2  ;
    public          NpButton    fs_wtitle3  ;
    private         NpButton    fs_wswapL   ;
    private         NpButton    fs_wswapR   ;
    private         NpButton    fs_browse   ;
    private         NpButton    fs_bookmark ;
    private         NpButton    fs_goBack   ;
    private         NpButton    fs_goForward;
    public          MWebView    fs_webView  ;
    public          MWebView    fs_webView2 ;
    public          MWebView    fs_webView3 ;
    private         MWebView    fs_webViewL ;
    private         MWebView    fs_webViewC ;
    private         MWebView    fs_webViewR ;
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ MWebView @ {{{
    public class MWebView
            extends WebView
            implements MJavascriptInterface, Settings.FromToInterface, ValueCallback<String>, WebView.FindListener
    {
        // {{{
        private String script_content                        = Settings.EMPTY_STRING;
        private String on_selection_script_loaded_expression = Settings.EMPTY_STRING;

        private String        anchor = "";
        private String url_requested = "";
        private String     loaded_js = "";
        private int               id = 0;
        private int      calls_count = 0;
        private float    clientScale = activity.getResources().getDisplayMetrics().density;
        private int            mLeft = 0;
        private int            mTop  = 0;

        private boolean  scroll_off  = false;
        private int      scroll_offX = 0;
        private int      scroll_offY = 0;

        public  String get_url_requested() { return (url_requested != null) ? url_requested : getUrl(); }
        public     int             getId() { return id; }
        public     int   get_calls_count() { return calls_count;      }
        public    void raise_calls_count() {        calls_count += 1; }

        // }}}

        // Constructor {{{
        public MWebView(Context context)
        {
            super(context, null);

            this.id = ++MWebView_Instance_count;
            setFindListener( this );

            setWebChromeClient(new WebChromeClient() {
                public boolean onConsoleMessage(ConsoleMessage cm)
                {
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, cm.message() +" -- line["+ cm.lineNumber() + "] of ["+ cm.sourceId() +"]");
                    return true;
                }
            });

        }
        //}}}
        //* WEBVIEW (toast) {{{ */
        private String      WEBVIEW_toast_msg = "";

        private Runnable hr_WEBVIEW_toast     = new Runnable() { @Override public void run() { toast_short( WEBVIEW_toast_msg ); } };
        //}}}
    //* WEBVIEW (protected methods) {{{ */
    //{{{
/*
:!start explorer "https://labs.mwrinfosecurity.com/blog/adventures-with-android-webviews/"
setAllowFileAccess
*/

    // hide protected void onDrawVerticalScrollBar(Canvas canvas, Drawable scrollBar, int l, int t, int r, int b)
    // hide protected boolean setFrame(int left, int top, int right, int bottom)
    //
    //      protected void onAttachedToWindow()
    //      protected void onConfigurationChanged(Configuration newConfig)
    //      protected void onDetachedFromWindowInternal()
    //      protected void onDraw(Canvas canvas)
    //      protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect)
    //      protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    //      protected void onScrollChanged(int l, int t, int oldl, int oldt)
    //      protected void onSizeChanged(int w, int h, int ow, int oh)
    //      protected void onVisibilityChanged(View changedView, int visibility)
    //      protected void onWindowVisibilityChanged(int visibility)

    //      protected void dispatchDraw(Canvas canvas)
    //      protected void encodeProperties(@NonNull ViewHierarchyEncoder encoder)

    //      protected  int computeHorizontalScrollOffset()
    //      protected  int computeHorizontalScrollRange()
    //      protected  int computeVerticalScrollExtent()
    //      protected  int computeVerticalScrollOffset()
    //      protected  int computeVerticalScrollRange()

    //}}}
//        // onDrawVerticalScrollBar {{{
//        @Override protected void onDrawVerticalScrollBar(Canvas canvas, Drawable scrollBar, int l, int t, int r, int b)
//        {
//            super.onDrawVerticalScrollBar(canvas, scrollBar, l, t, r, b);
//
//            if( !fs_webview_session_running ) return;
//
///*WVTOOLS*/String caller = "onDrawVerticalScrollBar";//TAG_WVTOOLS
///*WVTOOLS*/Settings.MOC(TAG_WVTOOLS, caller);
//            NpButton wvTools_sbX
//                = (wv == fs_webView ) ? wvTools.sb
//                : (wv == fs_webView2) ? wvTools.sb2
//                :                       wvTools.sb3;
//
//            wvTools.sb_adjust_thumb(wvTools_sbX, wv);
//            wvTools.sb_layout_tools(wvTools_sbX, caller);
//        }
//        //}}}
        // onConfigurationChanged .. check_orientation {{{
        @Override protected void onConfigurationChanged(Configuration newConfig)
        {
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "onConfigurationChanged");
            super.onConfigurationChanged( newConfig );

//*WEBVIEW*/WEBVIEW_toast_msg = "Configuration Changed:\n"+ newConfig.toString().replace(" ","\nWEBVIEW: "); //TAG_WEBVIEW
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, "onConfigurationChanged", "WEBVIEW_toast_msg:\n"+WEBVIEW_toast_msg);
//*WEBVIEW*/handler.re_postDelayed(hr_WEBVIEW_toast, 500);//TAG_WEBVIEW

            //TODO: SCROLLBAR SYNC
        }
        //}}}
        // onScrollChanged {{{
        @Override protected void onScrollChanged(int l, int t, int oldl, int oldt)
        {
if(scroll_off)
{
//WEBVIEW//Settings.MOM(TAG_WEBVIEW, "scroll_off: scrollTo("+scroll_offX+", "+scroll_offY+"):");
    //scrollTo(scroll_offX, scroll_offY);
    super.onScrollChanged(scroll_offX, scroll_offY, oldl, oldt);
    return;
}
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "onScrollChanged");
            super.onScrollChanged(l, t, oldl, oldt);
            mLeft = l;
            mTop  = t;

//*WEBVIEW*/WEBVIEW_toast_msg = String.format("WEBVIEW: onScrollChanged(l=[%4d], t=[%4d])", l, t); //TAG_WEBVIEW
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, "onConfigurationChanged", "WEBVIEW_toast_msg:\n"+WEBVIEW_toast_msg);
//*WEBVIEW*/handler.re_postDelayed(hr_WEBVIEW_toast, 500);//TAG_WEBVIEW

            on_webView_changed(ON_WEBVIEW_SCROLLED, this, "onScrollChanged");
        }
        //}}}
        // onSizeChanged {{{
        @Override protected void onSizeChanged(int w, int h, int ow, int oh)
        {
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "onSizeChanged");
            super.onSizeChanged(w, h, ow, oh);

//*WEBVIEW*/WEBVIEW_toast_msg = String.format("WEBVIEW: onSizeChanged(w=[%4d], h=[%4d])", w, h); //TAG_WEBVIEW
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, "onSizeChanged", "WEBVIEW_toast_msg:\n"+WEBVIEW_toast_msg);
//*WEBVIEW*/handler.re_postDelayed(hr_WEBVIEW_toast, 500);//TAG_WEBVIEW

            on_webView_changed(ON_WEBVIEW_RESIZED, this, "onSizeChanged");
        }
        //}}}
        // onMeasure {{{
        @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "onMeasure");
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//*WEBVIEW*/WEBVIEW_toast_msg = String.format("WEBVIEW: onMeasure(w=[%4d], h=[%4d])", widthMeasureSpec, heightMeasureSpec); //TAG_WEBVIEW
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, "onMeasure", "WEBVIEW_toast_msg:\n"+WEBVIEW_toast_msg);
//*WEBVIEW*/handler.re_postDelayed(hr_WEBVIEW_toast, 500);//TAG_WEBVIEW

            on_webView_changed(ON_WEBVIEW_MEASURED, this, "onMeasure");
        }
        // }}}
        // onLayout {{{
        @Override protected void onLayout(boolean changed, int l, int t, int r, int b)
        {
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "onLayout");
            super.onLayout(changed, l, t, r, b);

//*WEBVIEW*/WEBVIEW_toast_msg = String.format("WEBVIEW:  onLayout(changed=[%b], l=[%4d], t=[%4d], r=[%4d], b=[%4d])", changed, l, t, r, b);//TAG_WEBVIEW
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, "onLayout", "WEBVIEW_toast_msg:\n"+WEBVIEW_toast_msg);
//*WEBVIEW*/handler.re_postDelayed(hr_WEBVIEW_toast, 500);//TAG_WEBVIEW

            on_webView_changed(ON_WEBVIEW_LAYOUT, this, "onMeasure");
        }
        //}}}
        // onDraw {{{
        //{{{
        private Paint page_boundary_paint = null;
        private float STROKE_WIDTH        =   24;
        //}}}
        @Override protected void onDraw(Canvas canvas)
        {
            super.onDraw( canvas );
            // [page_boundary_paint] {{{
            if( page_boundary_paint == null)
            {
                page_boundary_paint = new Paint();
              //page_boundary_paint.setStyle      ( Paint.Style.FILL    );
                page_boundary_paint.setStyle      ( Paint.Style.STROKE  );
                page_boundary_paint.setStrokeWidth( STROKE_WIDTH        );
              //page_boundary_paint.setColor      ( PAGE_BOUNDARY_COLOR );
            }
            //}}}
            // [page_boundary_rect] {{{
            if(!page_boundary_rect.isEmpty() )
            {
                int color_num   =  page_num   % Settings.COLORS_ECC.length;
                int color_alpha = (color_num == 9) ? 0xD0FFFFFF : 0x80FFFFFF; // higher white opacity

                page_boundary_paint.setColor(Settings.COLORS_ECC[color_num] & color_alpha);

                canvas.drawRect(page_boundary_rect, page_boundary_paint);
            }
            //}}}
        }
        //}}}
        // onPause {{{
        @Override public void onPause()
        {
            super.onPause();
if( Settings.is_LOG_ACTIVITY() ) Settings.MOC(TAG_ACTIVITY, "MWebView.onPause: CALLING pauseTimers ON ["+getTitle()+"]");
            pauseTimers();
        }
        //}}}
        // onResume {{{
        @Override public void onResume()
        {
            super.onResume();
if( Settings.is_LOG_ACTIVITY() ) Settings.MOC(TAG_ACTIVITY, "MWebView.onResume: CALLING resumeTimers ON ["+getTitle()+"]");
            resumeTimers();
        }
        //}}}
        //}}}
        //* WEBVIEW (compute range offset extent) {{{ */
        public    int computeVerticalScrollRange () { return super.computeVerticalScrollRange (); }
        protected int computeVerticalScrollOffset() { return super.computeVerticalScrollOffset(); }
        protected int computeVerticalScrollExtent() { return super.computeVerticalScrollExtent(); }
        //}}}
        //* WEBVIEW (page animation) {{{ */
        //{{{
        private Rect page_boundary_rect = new Rect();
        private int  page_num           = 0;
        //}}}
        //➔ add_page_boundary_scroll_DY @see WVTools.get_wv_thumb_p_str {{{
        public void add_page_boundary_scroll_DY(float scroll_DY)
        {
            float wv_height =    getHeight ();
            float scrollY   =    getScrollY() + wv_height * scroll_DY;
            float wv_scale  =    getScale  ();
            float wv_zoom   = wv_scale / Settings.Density;

            int   page_idx  = (int)(scrollY  / wv_height  / wv_zoom);
            /**/  page_num  =   1 + page_idx;
            int   page_U    = (int)(page_idx * wv_height  * wv_zoom);
            int   page_D    = (int)(page_U   + wv_height  * wv_zoom);
            int   page_L    = (int)(0                     * wv_zoom);
            int   page_R    = (int)(getWidth()            * wv_zoom);
            set_page_boundary_URDL(page_U, page_R, page_D, page_L);


/*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "add_page_boundary_scroll_DY("+ scroll_DY +")");
/*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "...............wv_scale...=["+ wv_scale  +"]");
/*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "...............wv_zoom....=["+ wv_zoom   +"]");
        }
        //}}}
        //➔ clr_page_boundary_scroll_DY {{{
        public void clr_page_boundary_scroll_DY()
        {
            // RESET .. (first color)
            page_num = 0;
        }
        //}}}
        //_ set_page_boundary_URDL {{{
        private void set_page_boundary_URDL(int page_U, int page_R, int page_D, int page_L)
        {
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "page_U=["+page_U+"]");
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "page_R=["+page_R+"]");
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "page_D=["+page_D+"]");
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "page_L=["+page_L+"]");

            if(!page_boundary_rect.isEmpty() ) handler.removeCallbacks( hr_clear_page_boundary_rect );

            page_boundary_rect.set(
                      page_L + (int)(STROKE_WIDTH/2)
                    , page_U + (int)(STROKE_WIDTH/2)
                    , page_R - (int)(STROKE_WIDTH/2)
                    , page_D - (int)(STROKE_WIDTH/2)
                    );

            if(!page_boundary_rect.isEmpty() ) invalidate(page_boundary_rect);
            else                               invalidate();
        }
        //}}}
        //_ clr_page_boundary_rect {{{
        private void clr_page_boundary_rect()
        {
            if( page_boundary_rect.isEmpty() ) return;
            else                               set_page_boundary_URDL(0,0,0,0);
        }
        //}}}
        //}}}
        //* WEBVIEW (FromToInterface) {{{ */
        // SAVE FROM {{{
        private String save_from_caller = "";
        private  float x_from, y_from,   s_from,   xrfrom, yrfrom, zrfrom;
        private  float x_to  , y_to  ,   s_to  ,   xrto  , yrto  , zrto;

        public  void save_from(String caller)
        {
            save_from_caller = caller;

            x_from = x_to = getScrollX(); // WEBVIEW SCROLL (INSTEAD OF LOCATION)
            y_from = y_to = getScrollY(); // WEBVIEW SCROLL (INSTEAD OF LOCATION)

            s_from = s_to = getScaleY ();

            xrfrom = xrto = getRotationX();
            yrfrom = yrto = getRotationY();
            zrfrom = zrto = getRotation (); // Z
        }

        public String get_save_from_caller() { return save_from_caller; }

        //}}}
        // SET TO {{{
        public  void set_x_to  (float x) { x_to = x;      }
        public  void set_y_to  (float y) { y_to = y;      }
        public  void set_s_to  (float s) { s_to = s;      }

        public  void set_xrto  (float y) { xrto = y;      } // setRotationY
        public  void set_yrto  (float y) { yrto = y;      } // setRotationX
        public  void set_zrto  (float y) { zrto = y;      } // setRotationZ
        //}}}
        // GET FROM {{{
        public  float get_x_from()        { return x_from; }
        public  float get_y_from()        { return y_from; }
        public  float get_s_from()        { return s_from; }

        public  float get_xrfrom()        { return xrfrom; }
        public  float get_yrfrom()        { return yrfrom; }
        public  float get_zrfrom()        { return zrfrom; }

        //}}}
        // GET TO   {{{
        public  float get_x_to  ()        { return x_to  ; }
        public  float get_y_to  ()        { return y_to  ; }
        public  float get_s_to  ()        { return s_to  ; }

        public  float get_xrto  ()        { return xrto  ; }
        public  float get_yrto  ()        { return yrto  ; }
        public  float get_zrto  ()        { return zrto  ; }
        //}}}
        //}}}
        //* WEBVIEW (SELECTION) {{{ */
        //{{{
/*
:new $LOCAL/STORE/DEV/PROJECTS/RTabs/Util/RTabs_Profiles/DEV/selection.js
:new $LOCAL/DATA/ANDROID/SAMPLES/WebViewMarker-master/library/src/com/bossturban/webviewmarker/TextSelectionSupport.java
:new $LOCAL/DATA/ANDROID/SAMPLES/WebViewMarker-master/samples/demos/assets/android.selection.js
:new $LOCAL/DATA/ANDROID/SAMPLES/WebViewMarker-master/samples/demos/src/com/bossturban/webviewmarker/sample/demos/MainActivity.java
*/
        private      boolean        js_TouchPoint_set = false;

        private      boolean  selection_script_loaded = false;

//WebTextSelectionJSInterface
//setLongClickable(false) to prevent standard COPY-PASTE CAB

        //}}}
        // onEventJS {{{
        private boolean onEventJS(MotionEvent event, MWebView wv)
        {
            // (JS_SELECT) {{{
            if( !wvTools.property_get( WVTools.WV_TOOL_JS1_SELECT ) ) return false;

            String caller = "onEventJS("+get_view_name(this) +", "+Settings.Get_action_name(event)+")";
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, caller);

//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, caller, "WV_TOOL_JS1_SELECT IS SET");
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT,     "", "...js_TouchPoint_set=["+ js_TouchPoint_set +"]");
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT,     "", ".....selection_value=["+ selection_value   +"]");

            String consumed_by = null;
            //}}}
            // SCREEN COORDINATES {{{
            int ev_x = (int)event.getX();
            int ev_y = (int)event.getY();
            int wv_x = (int)   wv.getX();
            int wv_y = (int)   wv.getY();

            // FEEDBACK
            blink_crosshair(ev_x, ev_y);

//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "", "...WEBVIEW...ScrollRange =["+ wv.computeVerticalScrollRange ()+"]");
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "", "...WEBVIEW...ScrollOffset=["+ wv.computeVerticalScrollOffset()+"]");
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "", "...WEBVIEW........ScrollY=["+ wv.getScrollY()                 +"]");
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "", "...WEBVIEW........ScrollX=["+ wv.getScrollX()                 +"]");
            //}}}
            // CLIENT COORDINATES {{{
            float wc_s = wv.clientScale * wv.getScaleY();
            int   wc_x = ev_x - wv_x;
            int   wc_y = ev_y - wv_y;

//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "", "...CLIENT........wc_x....=["+ wc_x +"] = (ev_x "+ev_x+") - (wv_x "+wv_x+")");
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "", "...CLIENT........wc_y    =["+ wc_y +"] = (ev_y "+ev_y+") - (wv_y "+wv_y+")");
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "", "...CLIENT........wc_s    =["+ wc_s +"] = (wv.clientScale "+wv.clientScale+") * (wv.getScaleY "+wv.getScaleY()+")");
            //}}}
            // DEVICE INDEPENDENT PIXELS {{{
            float wc_s_DIP =       Settings.get_DIP(wc_s);
            int   wc_x_DIP = (int)(Settings.get_DIP(wc_x) / wc_s_DIP);
            int   wc_y_DIP = (int)(Settings.get_DIP(wc_y) / wc_s_DIP);

//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "", "...CLIENT........wc_s_DIP=["+ wc_s_DIP +"]");
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "", "...CLIENT........wc_x_DIP=["+ wc_x_DIP +"]");
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "", "...CLIENT........wc_y_DIP=["+ wc_y_DIP +"]");
            // }}}
            // CLIENT SCROLL [mLeft] [mTop] .. (onScrollChanged) {{{
            int   mLeft_DIP = (int)(Settings.get_DIP(wv.mLeft)); wc_x_DIP += (int)(mLeft_DIP / wc_s_DIP);
          //int   mTop_DIP  = (int)(Settings.get_DIP(wv.mTop )); wc_y_DIP += (int)(mTop_DIP  / wc_s_DIP);

//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "", "...CLIENT SCROLL.wc_x_DIP=["+ wc_x_DIP +"] .. f(wv.mLeft "+wv.mLeft+")");
//JAVASCRIPT//Settings.MOC(TAG_JAVASCRIPT, "", "...CLIENT SCROLL.wc_y_DIP=["+ wc_y_DIP +"] .. f(wv.mTop  "+wv.mTop +")");
            //}}}
            // ACTION {{{
            switch( event.getActionMasked() )
            {
                case MotionEvent.ACTION_DOWN:
                    if(     onEventJS1_onDown   (wc_x_DIP, wc_y_DIP)) consumed_by = "onEventJS1_onDown";
                    break;

                case MotionEvent.ACTION_MOVE:
                    if(     onEventJS2_onMove   (wc_x_DIP, wc_y_DIP)) consumed_by = "onEventJS2_onMove";
                    break;

                case MotionEvent.ACTION_UP:
                    if(     onEventJS3_onClick  (wc_x_DIP, wc_y_DIP)) consumed_by = "onEventJS3_onClick";
                    else if(onEventJS4_onUp     (wc_x_DIP, wc_y_DIP)) consumed_by = "onEventJS4_onUp";
                    break;

                case MotionEvent.ACTION_CANCEL:
                    if(     onEventJS5_onCancel (                  )) consumed_by = "onEventJS5_onCancel";
                    break;

              //case MotionEvent.ACTION_POINTER_DOWN:
              //       if(js_cmd( JS_CMD_SCROLLTOP              )   ) consumed_by = "JS_CMD_SCROLLTOP";
              //       break;

              //case MotionEvent.ACTION_POINTER_UP:
              //       if(js_cmd( JS_CMD_TOGGLE_DESIGNMODE      )   ) consumed_by = "JS_CMD_TOGGLE_DESIGNMODE";
              //       break;

            }
            //}}}
            // {{{
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, caller, "...return (consumed_by="+consumed_by+")");
            return (consumed_by != null);
            //}}}
        }
        //}}}
        // onEventJS1_onDown {{{
        private boolean onEventJS1_onDown(int wc_x_DIP, int wc_y_DIP)
        {
            // (!selection_value) {{{
            String caller = "MWebView.onEventJS1_onDown";

//*JAVASCRIPT*/if(selection_value != null) Settings.MOC(TAG_JAVASCRIPT, "", "PRESERVE CURRENT [selection_value]");
            if(  selection_value != null) return false;

            //}}}
            // NEW SELECTION START [setStartXY] {{{
            String logging
                = wvTools.property_get( WVTools.WV_TOOL_JS0_LOGGING ) ? "true" : "false";
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, caller, "NEW SELECTION [setStartXY] .. (logging "+logging+")");

            String js_expressions
                =  "  set_logging("  + logging                  +")"
                +  "+ setTouchPoint("+ wc_x_DIP +", " +wc_y_DIP +")"
                +  "+ setStartXY("   + wc_x_DIP +", " +wc_y_DIP +")";

            evaluateJS( js_expressions );
            //}}}
            js_TouchPoint_set = true;
            return true;
        }
        //}}}
        // onEventJS2_onMove {{{
        private boolean onEventJS2_onMove(int wc_x_DIP, int wc_y_DIP)
        {
            // (!selection_value) && (js_TouchPoint_set) {{{
            if( !js_TouchPoint_set ) return false;

            String caller = "MWebView.onEventJS2_onMove";
            if(selection_value != null)
            {
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, caller, "PRESERVE CURRENT [selection_value]");

                return false;
            }
            //}}}
            // NEW SELECTION END calling [setEndXY] {{{
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, caller, "NEW SELECTION END: calling [setEndXY]");
            String js_expressions
                =  "setEndXY("+wc_x_DIP+", "+wc_y_DIP+")";

            evaluateJS( js_expressions );

            return false;//true; // XXX leave it to wvTools.onScroll_2_sb_PageScroll
            //}}}
        }
        //}}}
        // onEventJS3_onClick {{{
        private boolean onEventJS3_onClick(int wc_x_DIP, int wc_y_DIP)
        {
            // (is_on_UP_CLICK_DURATION) .. (!has_moved_enough) {{{
            if(!RTabs_instance.is_on_UP_CLICK_DURATION() ) return false;
            if( has_moved_enough                         ) return false;

            String caller = "MWebView.onEventJS3_onClick";
            //}}}
            // CLICK: calling [selectTouchedWord] {{{
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, caller, "CLICK: calling [selectTouchedWord]");

            String js_expressions
                =  "  selectTouchedWord ("+wc_x_DIP+", "+wc_y_DIP +")"
                +  "+ highlightSelection('backColor, foreColor')"
                ;

              //=  "  test()"

            evaluateJS( js_expressions );

            //}}}
            js_TouchPoint_set = false;
            return true;
        }
        //}}}
        // onEventJS4_onUp {{{
        private boolean onEventJS4_onUp(int wc_x_DIP, int wc_y_DIP)
        {
            // (!selection_value) && (js_TouchPoint_set) {{{
            if(!js_TouchPoint_set     ) return false;
            if(selection_value != null) return false;

            String caller = "MWebView.onEventJS4_onUp";
            //}}}
            // LONG TOUCH: HIGHLIGHT SELECTION {{{
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, caller, "LONG TOUCH: HIGHLIGHT SELECTION:");

            String js_expressions
                =  "  winSelection()"
                +  "+ highlightSelection('fontName, bold')"
                ;

            evaluateJS( js_expressions );
            //}}}
            js_TouchPoint_set = false;
            return true;
        }
        //}}}
        // onEventJS5_onCancel {{{
        private boolean onEventJS5_onCancel()
        {
            // (js_TouchPoint_set) {{{
            if( !js_TouchPoint_set ) return false;

            String caller = "MWebView.onEventJS5_onCancel";
            //}}}
            // CANCEL: calling [clearSelection] {{{
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, caller, "CANCEL: calling [clearSelection]");
            String js_expressions
                =  "clearSelection()";

            evaluateJS( js_expressions );

            //}}}
            js_TouchPoint_set = false;
            return true;
        }
        //}}}

        //* JAVASCRIPT (EVAL and VALUE) */
        // evaluateJS   {{{
        public void evaluateJS(String javascript)
        {
            String   caller = "evaluateJS";
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, caller, "javascript=["+javascript+"]");

            // (evaluateJavascript) .. (onReceiveValue) {{{
//            if(selection_script_loaded ) {

            String script
                =                          javascript            ;
              //= "(function(){"+          javascript    +"})();";
              //= "(function(){ return "(+ javascript +"); })();";
              //= javascript;
              //= "wv_js_fn=function(){\n"
              //+   javascript
              //+ "};\n"
              //+ "return wv_js_fn();";


//*JAVASCRIPT*/System.err.println(caller+": script={{{\n"+script+"\n}}}\n");

              //evaluateJavascript(script, this); // ValueCallback .. (implement onReceiveValue)
                evaluateJavascript(script, null); // XXX
//            }
            //}}}
//            // [loaded_js] .. (check currently loaded javascript function set) {{{
//            else {
//                load_SELECTION_JS();
//                on_selection_script_loaded_expression = javascript; // to be evaluated later by onPageFinished
//                handler.re_postDelayed( hr_on_selection_script_loaded, WVSCRIPT_LOAD_DELAY);
//            }
//            //}}}
        }
        //}}}
        // onReceiveValue (interface ValueCallback<String>) {{{
        public void onReceiveValue(String value)
        {
Settings.MOC(TAG_JAVASCRIPT, "onReceiveValue", value.replace("\\n","\n"));

            value = Settings.trim_text( value );

            if     (value.equals("undefined")) value = null;
            else if(value.indexOf(" " ) >= 0 ) value = null;
            else if(value.indexOf("\n") >= 0 ) value = null;

            if( !TextUtils.isEmpty( value ) )
                find_selection( value );    // NEW CURRENT SELECTION
            else
                clear_selection();          // NO  CURRENT SELECTION
        }
        //}}}
//        // hr_on_selection_script_loaded {{{
//        private Runnable hr_on_selection_script_loaded = new Runnable()
//        {
//            @Override public void run() {
//                if( TextUtils.isEmpty( on_selection_script_loaded_expression) ) return;
//
//                String javascript    = on_selection_script_loaded_expression;
//                on_selection_script_loaded_expression = Settings.EMPTY_STRING;
//
//                String script = "(function() { return ("+ javascript +"); })()";
///*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "hr_on_selection_script_loaded", "calling evaluateJavascript\n{{{\n"+script+"\n}}}\n");
//
//                evaluateJavascript(script, MWebView.this); // ValueCallback .. (implement onReceiveValue)
//            }
//        };
//        //}}}

        //* WEBVIEW (FIND and RESULT) */
        // find_selection {{{
        private int last_numberOfMatches = 0;
        private int last_matchNum        = 0;

        private void find_selection(String value)
        {
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "find_selection("+value+")");

            selection_value      = value;    // RTabs    [SEARCH VALUE]
          last_numberOfMatches = 0;        // RTabs
          last_matchNum        = 0;        // RTabs

            wvTools.sb_show_SELECT_find();   // WVTools  [SEARCH IN PROGRESS]

            clearMatches();                  // MWebView [WEBVIEW CLEANUP]
            findAllAsync( selection_value ); // MWebView [SEARCH] .. (see onFindResultReceived for results)
        }
        //}}}
        // call_findNext {{{
        private int FINDNEXT_CALLS_MAX = 32;
        private int findNext_calls_max =  0;
        private int expected_matchNum  =  0;

        public void call_findNext(boolean forward)
        {
            String caller = "call_findNext("+forward+")";

            if((last_numberOfMatches > 0) && (last_matchNum > 0))
            {
                expected_matchNum
                    = last_matchNum
                    + (forward ? 1 : -1)
                    ;
                if     (expected_matchNum == 0                   ) expected_matchNum = last_numberOfMatches; // before first == last
                else if(expected_matchNum  > last_numberOfMatches) expected_matchNum = 1                   ; // after  last  == first
            }
            else {
                expected_matchNum = 0;
            }

            findNext_calls_max = Math.min(last_numberOfMatches, FINDNEXT_CALLS_MAX);

//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, caller, "expected_matchNum=["+expected_matchNum+"]");
            findNext( forward );
        }
        //}}}
        // onFindResultReceived .. (interface WebView.FindListener) {{{
        public void onFindResultReceived(int activeMatchOrdinal, int numberOfMatches, boolean isDoneCounting)
        {
            // CALLBACK FROM (findAllAsync) .. (matchNum) (numberOfMatches) {{{
            String caller = "onFindResultReceived";
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, caller, "...expected_matchNum=["+ expected_matchNum   +"]");

//*JAVASCRIPT*/Settings.MOM(TAG_JAVASCRIPT,         "...activeMatchOrdinal=["+ activeMatchOrdinal +"]"); // currently selected match (zero-based)
//*JAVASCRIPT*/Settings.MOM(TAG_JAVASCRIPT,         "...numberOfMatches...=["+ numberOfMatches    +"]"); // matches found
//*JAVASCRIPT*/Settings.MOM(TAG_JAVASCRIPT,         "...isDoneCounting....=["+ isDoneCounting     +"]"); // (find operation completed)
            //}}}
            // [matchNum] [matchNum_of_matcheCount] {{{
            int matchNum
                = ( numberOfMatches    > 0)
                ?  (activeMatchOrdinal + 1)
                :  0;

            String matchNum_of_matcheCount
                =  (numberOfMatches > 0) ? matchNum +"/"+ numberOfMatches
                :   Settings.SYMBOL_CROSS_MARK;

//*JAVASCRIPT*/Settings.MOM(TAG_JAVASCRIPT, matchNum_of_matcheCount);

            wvTools.sb_show_SELECT_received(selection_value, matchNum_of_matcheCount, isDoneCounting);

            //}}}
            // REJECT RESULT .. (!expected_matchNum) {{{
            if((isDoneCounting) && (expected_matchNum != 0) && (matchNum != expected_matchNum))
            {
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "", "REJECT RESULT matchNum=["+ matchNum +"] expected=["+expected_matchNum+"] findNext_calls_max=["+findNext_calls_max+"]");

                if(findNext_calls_max > 0)
                {
                    boolean forward = (expected_matchNum > matchNum);
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "", "...calling findNext(forward "+forward+")");
                    findNext(forward); // forward : backward
                    --findNext_calls_max;
                }
                else {
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "", "...(findNext_calls_max "+findNext_calls_max+"): FINDNEXT_CALLS_COUNT_MAX)");
                }
            }
            //}}}
            // ACCEPT RESULT {{{
            else if( isDoneCounting )
            {
                if(expected_matchNum != 0) {
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "", "ACCEPT RESULT matchNum=["+ matchNum +"] expected=["+expected_matchNum+"] findNext_calls_max=["+findNext_calls_max+"]");
                }
                last_numberOfMatches = numberOfMatches;
                last_matchNum        = matchNum;
                expected_matchNum    = 0;
            }
            //}}}
        }
        //}}}
        // has_selection_value {{{
        public boolean has_selection_value()
        {
            return (selection_value != null);
        }
        //}}}
        // clear_selection {{{
        private String selection_value = null;

        public  void clear_selection()
        {
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "clear_selection");
if(D || Settings.LOG_CAT) MLog.log("MWebView.clear_selection");

            /* RTabs    */ selection_value = null;          // [NO SEARCH VALUE]
            /* WVTools  */ wvTools.sb_show_SELECT_clear();  // [NO SEARCH IN PROGRESS]
            /* MWebView */ clearMatches();                  // [WEBVIEW CLEANUP]
        }
        //}}}

        // more... {{{

/*
:ta postVisualStateCallback
:ta pageUp
:ta pageDown
:ta clearHistory
:!start explorer "https://github.com/naoak/WebViewMarker/blob/master/library/src/com/bossturban/webviewmarker/TextSelectionSupport.java"
*/
        // }}}

        //{{{
/* DOM {{{
:!start explorer 'https://developer.android.com/guide/webapps/webview.html'
:!start explorer "https://developer.mozilla.org/en-US/docs/Web/API/Document_Object_Model/Introduction"
:!start explorer "https://developer.mozilla.org/en-US/docs/Web/API/document"

:new /LOCAL/DEV/DEVEL/EMC/Extensions/PJC_SPECS/script/PJC_SPECS.asp
:new /LOCAL/DEV/DEVEL/EMC/Extensions/PJC_SPECS/script/PJC_SPECS_input.asp

:new /LOCAL/GAMES/IVANWFR/INPUT/TWIDDLER/GitHub/Twiddler3-Layout/javascripts/twiddler.js

:new /LOCAL/STORE/DEV/PROJECTS/RTabs/Util/RTabs_Profiles/DEV/
:new /LOCAL/STORE/DEV/PROJECTS/RTabs/Util/RTabs_Profiles/DEV/MWebJS.html

}}} */
        private final    int JS_CMD_SCROLLTOP         = 0;
        private final    int JS_CMD_TOGGLE_DESIGNMODE = 1;
        //}}}
        // js_cmd {{{
        private void js_cmd(int js_cmd_id                ) { js_cmd(js_cmd_id, ""); }
        private void js_cmd(int js_cmd_id, String argLine)
        {
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "js_cmd", "(js_cmd_id=["+js_cmd_id+"], argLine=["+argLine+"])");

            switch(  js_cmd_id ) {
                case JS_CMD_SCROLLTOP        : js_cmd_TOP              ( argLine ); break;
                case JS_CMD_TOGGLE_DESIGNMODE: js_cmd_TOGGLE_DESIGNMODE( argLine ); break;
                default:                       Settings.printStackTrace(argLine);
            }
        }
        //}}}
        // js_cmd_TOP {{{
        public void js_cmd_TOP(String argLine)
        {
            String cmd
                = "(function() {"
                + " var  argLine = '"+argLine+"';"
                + " var      doc = document.documentElement;"
                + " var      top = (window.pageYOffset || doc.scrollTop)  - (doc.clientTop || 0);"
                + " var   result = '\\n@ JAVASCRIPT @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@'"
                + "              + '\\n@               argLine=['+ argLine                          +']'"
                + "              + '\\n@                   top=['+ top                              +']'"
                + "              + '\\n@    window.pageYOffset=['+ window.pageYOffset               +']'"
                + "              + '\\n@         doc.scrollTop=['+ doc.scrollTop                    +']'"
                + "              + '\\n@ window.getSelection()=['+ window.getSelection().toString() +']'"
                + "              + '\\n@ JAVASCRIPT @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@'"
                + "              ;"
                + " return result;"
                + "})()"
                ;

            evaluateJS( cmd );
        }
        //}}}
        // js_cmd_TOGGLE_DESIGNMODE {{{
        public void js_cmd_TOGGLE_DESIGNMODE(String argLine)
        {
            String cmd
                = "(function() {"
                + " var       body = document.getElementById('body');"
                + " var designMode = window.document.designMode;"
                + " var     result = '\\n@ JAVASCRIPT @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@'"
                + "                + '\\n@               argLine=['+ argLine                          +']'"
                + "                + '\\n@    current designMode=['+ designMode                       +']'"
                + "                + '\\n@ JAVASCRIPT @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@'"
                + "                ;"
                + "window.document.designMode = designMode ? true : false;"
                + "     designMode = window.document.designMode.equals('On');"
                + "    result     += '\\n@     designMode set to=['+ designMode                       +']'"
                + ""
                + " return result;"
                + "})()"
                ;

            evaluateJS( cmd );
        }
        //}}}

//        // load_SELECTION_JS {{{
//        private void load_SELECTION_JS()
//        {
//            String caller = "load_SELECTION_JS";
//
//            String script_path = Settings.Get_Profiles_dir().getPath()+"/"+WVTools.DOM_SELECT_JS;
////*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, caller, "script_path=["+script_path+"]");
//
//            load_script_path( script_path );
//
//            selection_script_loaded = true; // one time attempt per MWebView instance lifespan
//        }
//        //}}}
        // load_script_path {{{
        private void load_script_path(String script_path)
        {
            String caller = "load_script_path("+script_path+")";
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, caller);

            if( loaded_js.contains( script_path ) ) return;

            WebSettings webSettings = getSettings();
            webSettings.setJavaScriptEnabled( true ); // default = false
            webSettings.setDomStorageEnabled( true ); // default = false

//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "", "LOADING script_path=["+                           script_path  +"]");
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, "", "file_exists(script_path)=["+Settings.file_exists( script_path )+"]");

            if(TextUtils.isEmpty(loaded_js) ) loaded_js  =      script_path;
            else                              loaded_js += ","+ script_path;

            boolean logging = false;
//*JAVASCRIPT*/if( wvTools.property_get( WVTools.WV_TOOL_JS0_LOGGING ) ) logging = true;

            script_content = Settings.load_script_expression(script_path, logging);

//JAVASCRIPT//if(Settings.LOG_CAT) System.err.println("############## script_content ("+script_content.length()+" bytes) {{{");
//JAVASCRIPT//if(Settings.LOG_CAT) System.err.println(                script_content     );
//JAVASCRIPT//if(Settings.LOG_CAT) System.err.println("############## script_content }}}");

            loadUrl ("javascript: "+ script_content);
          //loadData("javascript: "+ script_content, "text/html", "UTF-8");
        }
        //}}}
        // js_init .. (arrange for a webview instance to reload JS handlers from scartch) {{{
        private void js_init()
        {
            String caller = "js_init";
//*TOOL*/Settings.MOC(TAG_TOOL, caller);

            loaded_js               = "";
            selection_script_loaded = false;
        }
        //}}}
        //}}}
        // toString {{{
        public String toString()
        {
            return "MWebView"
                + " ID=["+            id +"]"
                + " C#=["+   calls_count +"]"
                + "  U=["+ url_requested +"]"
                + "  J=["+     loaded_js +"]"
                + "  T=["+    getTitle() +"]"
                ;
        }
        //}}}
        // startActionMode {{{
        @Override
        public ActionMode startActionMode(ActionMode.Callback callback)
        {
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "startActionMode");

            return super.startActionMode( mActionMode );
        }
        //}}}
        // ActionMode.Callback {{{
        private android.view.ActionMode.Callback mActionMode = new android.view.ActionMode.Callback()
        {
            public boolean onActionItemClicked(android.view.ActionMode mode, android.view.MenuItem item)
            {
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "onActionItemClicked");

                return false;
            }
            public boolean onCreateActionMode(android.view.ActionMode mode, android.view.Menu menu    )
            {
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "onCreateActionMode");

                return false;
            }
            public boolean onPrepareActionMode(android.view.ActionMode mode, android.view.Menu menu    )
            {
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "onPrepareActionMode");

                return false;
            }
            public void    onDestroyActionMode(android.view.ActionMode mode)
            {
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "onDestroyActionMode");

            }
        };
        //}}}
        // MJavascriptInterface {{{
        // eval .. (RECEIVE a message FROM WEBVIEW) {{{
        @JavascriptInterface
        public void eval(String msg)
        {
            String caller = "MWebJS.eval";
//*JAVASCRIPT*/Settings.MON(TAG_JAVASCRIPT,caller,"...msg=["+ msg +"]");

            boolean scroll_off_new_state = msg.equals("SCROLL OFF");
            if(scroll_off == scroll_off_new_state) return;

            scroll_off  = scroll_off_new_state;
            scroll_offX = getScrollX();
            scroll_offY = getScrollY();

//System.err.println     ("MWebJS: "+ msg +" ["+scroll_offX+" "+scroll_offY+"]");
            toast_short("MWebJS: "+ msg +" ["+scroll_offX+" "+scroll_offY+"]");
        }
        //}}}
        // post .. (SEND a message TO WEBVIEW) {{{
        @JavascriptInterface
        public void post(String msg)
        {
            String caller = "MWebJS.post";
//*JAVASCRIPT*/Settings.MON(TAG_JAVASCRIPT,caller,"CALLING [Javascript: "+ msg +"]");

            loadUrl("Javascript: "+ msg);
        }
    // }}}
        //}}}
        // assert_no_cache {{{
        public void assert_no_cache(String caller)
        {
//*JAVASCRIPT*/caller += "->assert_no_cache";//TAG_JAVASCRIPT

            WebSettings webSettings = getSettings();
            int mode  = webSettings.getCacheMode();

            if( mode != WebSettings.LOAD_NO_CACHE)
            {
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, caller+": ACTIVATING: setCacheMode( WebSettings.LOAD_NO_CACHE )");
                webSettings.setCacheMode( WebSettings.LOAD_NO_CACHE );
            }
            else
            {
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, caller+": ...KEEPING: setCacheMode( WebSettings.LOAD_NO_CACHE )");
            }

//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, caller+": ...CALLING: clearCache(true)");
                clearCache( true ); // includeDiskFiles
        }
        //}}}
    }
    // }}}
    // @@@@@@@@@@@@@@@@@@@@@@@@ JAVASCRIPT (interface MJavascriptInterface) @ {{{ */
    private interface MJavascriptInterface
    {
        @JavascriptInterface
        void eval(String msg);
        @JavascriptInterface
        void post(String msg);
    }
    //}}}
    // get_wv_bg_color {{{
    public int get_wv_bg_color(MWebView wv)
    {
        if     (wv == null       ) return Color.WHITE;
        else if(wv == fs_webView ) return fs_wtitle .getBackgroundColor();
        else if(wv == fs_webView2) return fs_wtitle2.getBackgroundColor();
        else if(wv == fs_webView3) return fs_wtitle3.getBackgroundColor();
        else                       return Color.WHITE;
    }
    //}}}
    // get_wv_fg_color {{{
    public int get_wv_fg_color(MWebView wv)
    {
        if     (wv == null       ) return Color.BLACK;
        else if(wv == fs_webView ) return fs_wtitle .getCurrentTextColor();
        else if(wv == fs_webView2) return fs_wtitle2.getCurrentTextColor();
        else if(wv == fs_webView3) return fs_wtitle3.getCurrentTextColor();
        else                       return Color.BLACK;
    }
    //}}}
    // get_fs_webView_count {{{
    public  int get_fs_webView_count()
    {
        int split_count;
        if     (fs_webView3 != null) split_count = 3;
        else if(fs_webView2 != null) split_count = 2;
        else                         split_count = 1;
        return split_count;
    }
    //}}}
    // MWebView_pool {{{
    // {{{
    private static final                  int MWEBVIEW_POOL_MAX       = 50;
    private static                        int MWebView_Instance_count =  0;
    private static final LinkedList<MWebView> MWebView_pool  = new LinkedList<>();

    // }}}
    public  /*static */ void MWebView_Reset_instance_count() { MWebView_Instance_count = 0; }
    // add_fs_webView {{{
    private MWebView add_fs_webView()
    {
        String caller = "add_fs_webView";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);
        // MWebView {{{
        MWebView mWebView = new MWebView( activity );
      //MWebJS   mWebJS   = new MWebJS  ( mWebView );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            MWebView.setWebContentsDebuggingEnabled(true);

        mWebView.addJavascriptInterface(mWebView, "MWebJS");

    //  mWebView.setInitialScale(int scaleInPercent); // default = 0  : (setLoadWithOverviewMode=true) ? [zoomed out to fit] : [or not]
        ViewCompat.setElevation(mWebView, Settings.WEBVIEW_ELEVATION);
        //}}}
        // WebSettings {{{
        WebSettings webSettings = mWebView.getSettings();
        // ZOOM {{{
        webSettings.setSupportZoom            (                     true ); // The default is true .. (not required)
        webSettings.setBuiltInZoomControls    (                     true ); // The default is false
        webSettings.setDisplayZoomControls    (                    false ); // .. (solves issue in FullscreenActivity.dispatchTouchEvent) (161111)

    //  webSettings.setDefaultZoom            (     ZoomDensity zoom     ); // default = ZoomDensity#MEDIUM // KEY_VAL WEB_ZOOM=(FAR 150 240dpi)..(MEDIUM 100 160dpi)..(CLOSE 75 120dpi)
    //  webSettings.setTextZoom               (             int textZoom ); // default = 100

        //}}}
        // FONT SIZE {{{
    //  webSettings.setDefaultFixedFontSize   (             int size     ); // default = 16 .. (1..72)
    //  webSettings.setDefaultFontSize        (             int size     ); // default = 16 .. (1..72)
    //  webSettings.setMinimumFontSize        (             int size     ); // default =  8 .. (1..72)
    //  webSettings.setMinimumLogicalFontSize (             int size     ); // default =  8 .. (1..72)
    //  webSettings.setTextSize               (        TextSize t        ); // default = TextSize#NORMAL // KEY_VAL WEB_TXTSIZE=(SMALLEST=50)..(SMALLER=75)..(NORMAL=100)..(LARGER=150)..(LARGEST=200)

        //}}}
        // LAYOUT {{{
    //  webSettings.setLayoutAlgorithm        ( LayoutAlgorithm l        ); // default = LayoutAlgorithm#NARROW_COLUMNS // KEY_VAL WEB_TXTSIZE=(NORMAL)..(SINGLE_COLUMN)..(NARROW_COLUMNS)..(TEXT_AUTOSIZING)
    //  webSettings.setLoadWithOverviewMode   (         boolean overview ); // default = false
    //  webSettings.setOffscreenPreRaster     (         boolean enabled  ); // default = false .. (avoid rendering artifacts when animating an offscreen WebView on-screen)
    //  webSettings.setSupportMultipleWindows (         boolean support  ); // default = false .. (whether to suport multiple windows .. needs a WebChromeClient)
    //  webSettings.setUseWideViewPort        (         boolean use      ); // default = ?     .. (support for the viewport meta tag)

    //  webSettings.setJavaScriptCanOpenWindowsAutomatically(boolean flag); // default = false .. (to support JavaScript function window)
        //}}}
        // FONT {{{
    //  webSettings.setCursiveFontFamily      (          String font     ); // default = "cursive"
    //  webSettings.setDefaultTextEncodingName(          String encoding ); // default = "UTF-8"
    //  webSettings.setFantasyFontFamily      (          String font     ); // default = "fantasy"
    //  webSettings.setFixedFontFamily        (          String font     ); // default = "monospace"
    //  webSettings.setSansSerifFontFamily    (          String font     ); // default = "sans-serif"
    //  webSettings.setSerifFontFamily        (          String font     ); // default = "sans-serif"
    //  webSettings.setStandardFontFamily     (          String font     ); // default = "sans-serif"

        //}}}
        // Javascript - DOM Storage {{{
        webSettings.setJavaScriptEnabled( true ); // default = false
        webSettings.setDomStorageEnabled( true ); // default = false

        //}}}
        //}}}
        // TODO: check dynamic javascript statements like these: {{{
        // mWebView.loadUrl( "javascript:( document.body.style.backgroundColor = 'red'    );" );
        // mWebView.loadUrl( "javascript:( document.body.style.color           = 'yellow' );" );
        // mWebView.loadUrl( "javascript:( document.body.style.fontSize        = '20pt'   );" );

        //}}}
        // mWebViewClient {{{
        mWebView.setWebViewClient( mWebViewClient );

        // }}}
        // WEBVIEW POOL {{{
        MWebView_pool.add( mWebView );

        // }}}
        return mWebView;
    }
    //}}}
    // MWebView_pool_onPause {{{
    private void MWebView_pool_onPause(String caller)
    {
        caller += "->MWebView_pool_onPause";
//*WEBVIEW*/String s = "";//TAG_WEBVIEW
        int i;
        MWebView wv;
        for(i=0; i < MWebView_pool.size(); ++i)
        {
//*WEBVIEW*/s += " "+i;//TAG_WEBVIEW
            wv = MWebView_pool.get(i);
            wv.onPause();
if( Settings.is_LOG_ACTIVITY() ) Settings.MOC(TAG_ACTIVITY, "MWebView_pool_onPause: CALLING pauseTimers ON ["+wv.getTitle()+"]");
            wv.pauseTimers();
        }
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "PAUSING WebViews ["+s+" ]");
    }
    //}}}
    // MWebView_pool_onResume {{{
    private void MWebView_pool_onResume(String caller)
    {
        caller += "->MWebView_pool_onResume";
//*WEBVIEW*/String s = "";//TAG_WEBVIEW
        int i;
        MWebView wv;
        for(i=0; i < MWebView_pool.size(); ++i)
        {
//*WEBVIEW*/s += " "+i;//TAG_WEBVIEW
            wv = MWebView_pool.get(i);
            wv.onResume();
if( Settings.is_LOG_ACTIVITY() ) Settings.MOC(TAG_ACTIVITY, "MWebView_pool_onResume: CALLING resumeTimers ON ["+wv.getTitle()+"]");
            wv.resumeTimers();
        }
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "RESUMING WebViews ["+s+" ]");
    }
    //}}}
    // MWebView_pool_clear {{{
    private void MWebView_pool_clear()
    {
        if(M||D) Settings.MOC(TAG_WEBVIEW, "MWebView_pool_clear");

        fs_webview_session_stop("MWebView_pool_clear");

        fs_webView  = null;
        fs_webView2 = null;
        fs_webView3 = null;

        for(int i=0; i < MWebView_pool.size(); ++i)
        {
            MWebView wv = MWebView_pool.get(i);
            if(wv != null) {

if( Settings.is_LOG_ACTIVITY() ) Settings.MOC(TAG_ACTIVITY, "MWebView_pool_clear: DISABLING JS ON ["+wv.getTitle()+"]");
                wv.getSettings().setJavaScriptEnabled(false);

if( Settings.is_LOG_ACTIVITY() ) Settings.MOC(TAG_ACTIVITY, "MWebView_pool_clear", "...LOADING [about:blank]");
                wv.loadUrl("about:blank");

if( Settings.is_LOG_ACTIVITY() ) Settings.MOC(TAG_ACTIVITY, "MWebView_pool_clear:  ENABLING JS ON ["+wv.getTitle()+"]");
                wv.getSettings().setJavaScriptEnabled(true);
            }
            frameLayout.removeView( wv );
        }

        //MWebView_pool.clear();

        MWebView_Reset_instance_count();
    }
    //}}}
    // get_WebView_for_url_requested {{{
    private MWebView get_WebView_for_url_requested(String url)
    {
        // WV WITH URL
        MWebView wv = null;
        int i;
        for(i=0; i < MWebView_pool.size(); ++i)
        {
            wv = MWebView_pool.get(i);
            if( Settings.URL_equals(url, wv.get_url_requested()) )   // select on load-time-related URL
                break;
            wv = null;
        }

        // WV REUSE CLEARED
        if(wv == null)
        {
            for(i=0; i < MWebView_pool.size(); ++i)
            {
                wv = MWebView_pool.get(i);
if( Settings.is_LOG_ACTIVITY() ) Settings.MOC(TAG_ACTIVITY, "MWebView.get_WebView_for_url_requested: CONSIDER REUSING WebView ["+wv.getUrl()+"]");

                String wv_url = wv.getUrl();
                if((wv_url != null) && wv_url.equals("about:blank")) {
if( Settings.is_LOG_ACTIVITY() ) Settings.MOC(TAG_ACTIVITY, "MWebView.get_WebView_for_url_requested", "...REUSING WebView");
                    break;
                }
                wv = null;
            }
        }
        else {
            wv.raise_calls_count();
        }

        // WV ADD NEW
        if((wv == null) && (MWebView_pool.size() < MWEBVIEW_POOL_MAX))
        {
            wv = add_fs_webView();
            wvContainer.addView( wv );
if( Settings.is_LOG_ACTIVITY() ) Settings.MOC(TAG_ACTIVITY, "MWebView.get_WebView_for_url_requested", "...ADDING WebView #"+MWebView_pool.size());
        }

        if(M||D) Settings.MON(TAG_WEBVIEW, "get_WebView_for_url_requested("+url+")", "...return ["+wv+"]");
        return wv;
    }
    //}}}
    // get_WebView_for_url_path {{{
    private MWebView get_WebView_for_url_path(String url_path)
    {
        MWebView wv = null;
        int i;
        for(i=0; i < MWebView_pool.size(); ++i)
        {
            wv = MWebView_pool.get(i);
            if( Settings.URL_path_equals(url_path, wv.get_url_requested()) )
                break;
            wv = null;
        }

        if(wv != null) {
            wv.raise_calls_count();
            if(M||D) Settings.MON(TAG_WEBVIEW, "get_WebView_for_url_path("+url_path+")", "...return ["+wv+"]");
        }
        else {
            if(M||D) Settings.MON(TAG_WEBVIEW, "get_WebView_for_url_path("+url_path+")", "...return [null]");
        }

        return wv;
    }
    //}}}
    // }}}
    // is_a_webView {{{
    private boolean is_a_webView(View view)
    {
        if (view == null       ) return false;
        if (view == fs_webView ) return  true;
        if (view == fs_webView2) return  true;
        if (view == fs_webView3) return  true;
        /*....................*/ return false;
    }
    //}}}

    //* WEBVIEW CLIENT */
    // WebViewClient {{{
    private final WebViewClient mWebViewClient = new WebViewClient()
    {
        // onPageStarted .. bookmark just loaded page {{{
        @Override
        public void onPageStarted(WebView wv, String page_started_url, Bitmap favicon)
        {
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "onPageStarted: page_started_url=["+page_started_url+"]");
            if(page_started_url == null) return;

            //update_from_page_finished_url( (MWebView)wv );
        }
        //}}}
        // onPageFinished .. bookmark just loaded page {{{
        @Override
        public void onPageFinished(WebView webView, String page_finished_url)
        {
            String caller = "onPageFinished";
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "page_finished_url=["+page_finished_url+"]");

            MWebView wv = (MWebView)webView;

            if( page_finished_url == null) {
                page_finished_url  = "";
            }
            else {
                // clear cache (file-scheme-url) .. (adb pushed files readiness) {{{
                if( page_finished_url.startsWith("file") )
                    wv.assert_no_cache(caller);

                //}}}
                // handle typically missed anchor jump by first load {{{
                int idx = page_finished_url.indexOf("#");
                if( idx > 0) {
                    String                anchor = page_finished_url.substring(idx+1);
                    if(!wv.anchor.equals( anchor ))
                        wv.anchor       = anchor;
                }
                //}}}
            }
        //  mCookieManager_add_wv_url(wv, page_finished_url);

            update_from_page_finished_url(wv, page_finished_url);
        }
        //}}}
        // onScaleChanged {{{
        @Override
        public void onScaleChanged(WebView wv, float oldScale, float newScale)
        {
            ((MWebView)wv).clientScale = newScale;
/*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "onScaleChanged", "clientScale=["+newScale+"]");
        }
        //}}}
        /* shouldOverrideUrlLoading {{{*/
        @Override
        public boolean shouldOverrideUrlLoading(WebView wv, WebResourceRequest request)
        {
/*{{{*/
            String caller = "shouldOverrideUrlLoading(wv , request)";
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller,      "wv ["+wv     +"]");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "request ["+request+"]");

            Uri    uri = request.getUrl();
            String url = uri.toString();
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "url ["+url+"]");
/*}}}*/
// deprecated {{{
//            return shouldOverrideUrlLoading(wv, url);
//        }
//
//        // deprecated (200806)
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView wv, String url)
//        {
//            String caller = "shouldOverrideUrlLoading(wv , url)";
////*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller,    "wv ["+wv +"]");
//}}}
            /* mailto: .. return true {{{*/
            if( url.startsWith("mailto:") )
            {
                if(activity != null)
                {
                    MailTo  mailTo = MailTo.parse( url );
                    String      to = mailTo.getTo();
                    String subject = mailTo.getSubject();
                    String    body = mailTo.getBody();
                    String      cc = mailTo.getCc();

//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller,  "mailTo ["+ mailTo  +"]");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller,      "to ["+ to      +"]");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "subject ["+ subject +"]");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller,    "body ["+ body    +"]");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller,      "cc ["+ cc      +"]");

                    Intent  intent = _getEmailIntent(activity, to, subject, body, cc);

                    activity.startActivity( intent );

/*{{{
                    wv.reload();
}}}*/

                    return true;
                }
            }
/*{{{
            else {
                wv.loadUrl(url);
            }
}}}*/
/*}}}*/
            return false;
        }
        /*}}}*/
        // _getEmailIntent {{{
        private Intent _getEmailIntent(Context context, String address, String subject, String body, String cc)
        {
            String caller = "_getEmailIntent";

            Intent intent
                = new Intent( Intent.ACTION_SEND  );

            intent.putExtra(  Intent.EXTRA_EMAIL  , new String[] { address });
            intent.putExtra(  Intent.EXTRA_TEXT   , body                    );
            intent.putExtra(  Intent.EXTRA_SUBJECT, subject                 );
            intent.putExtra(  Intent.EXTRA_CC     , cc                      );

            intent.setType("message/rfc822");

//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "...return intent ["+intent+" ]");
            return intent;
        }
        //}}}
    };
/* // {{{
:new             $LOCAL/STORE/DEV/PROJECTS/RTabs/Util/RTabs_Profiles/DEV/javascript/dom_ipc.js
:!start explorer "https://stackoverflow.com/questions/18874282/webview-email-link-mailto"
:!start explorer "http://stackoverflow.com/questions/29592695/android-webview-wont-load-my-url-but-will-load-others"
:!start explorer "http://developer.android.com/guide/webapps/webview.html"
:!start explorer "http://stackoverflow.com/questions/8193239/how-to-get-loaded-web-page-title-in-android-webview"
:!start explorer "http://stackoverflow.com/questions/2566485/webview-and-cookies-on-android"
// WEBKIT
:!start explorer "http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/2.3.7_r1/android/webkit/"
*/ // }}}
    //}}}
    // update_webview_colors {{{
    private void update_webview_colors(MWebView wv)
    {
        String caller = "update_webview_colors("+get_view_name(wv)+")";
        String title = wv.getTitle();
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "title=["+title+"]");
        // WTITLE .. (LABEL + INFO) {{{
        boolean have_title = !TextUtils.isEmpty( title );
        String       label = have_title
            ?                   title.replace("\n"," ")
            :                   activity.getString(R.string.waiting_for_page_title);

        NpButton nb_wtitle = null;
        if     (wv == fs_webView ) nb_wtitle = fs_wtitle ;
        else if(wv == fs_webView2) nb_wtitle = fs_wtitle2;
        else if(wv == fs_webView3) nb_wtitle = fs_wtitle3;

        if(nb_wtitle != null)
        {
            // INFO {{{
            StringBuilder info_sb = new StringBuilder();
            WebBackForwardList  l = wv.copyBackForwardList();
            int            current_index = l.getCurrentIndex();
            for(int i = 0; i < l.getSize(); ++i)
            {
                WebHistoryItem item = l.getItemAtIndex( i );
                String prefix
                    = (i < current_index)
                    ? "\n < "
                    : (i == current_index)
                    ? "\n = "
                    : "\n > ";
                info_sb.append( prefix        );
                info_sb.append( item.getUrl() );
            }
            String info =         NotePane.INFO_SEP + info_sb.toString();

            nb_wtitle.setTag( info ); // .. (to be used by NoteHolder.get_text to show when magnified)

            if(nb_wtitle == fs_wtitle) {
                fs_goBack   .setTag( info );
                fs_goForward.setTag( info );
            }
            //}}}
            // LABEL {{{
            nb_wtitle.setText(            Settings.Get_DIGIT_SYMBOL( wv.getId() )
                    + " "               + label
                    + " "               + Settings.SYMBOL_RELOAD);
            nb_wtitle.fitText();
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "...nb_wtitle.getText()="+nb_wtitle.getText());
            //}}}
        }
        //}}}
        // WEBVIEW OPACITY {{{
        if( !have_title ) {
            wv.setAlpha(0.8f);
        }
        else {
            wv.setAlpha(1.0f);
        }
        // }}}
        // FG_COLOR BG_COLOR .. f(TABBED URL) {{{
        int fg_color = FS_WTITLE_TEXTCOLOR;
        int bg_color = FS_WTITLE_WAITCOLOR;
        if( have_title )
        {
            NotePane               np_for_url = this_RTabsClient.get_np_with_url(wv.getUrl()           , caller); // ...actual url f(server-side redirection)
            if(np_for_url == null) np_for_url = this_RTabsClient.get_np_with_url(wv.get_url_requested(), caller); // requested url

            if((np_for_url != null) && (np_for_url.button != null))
            {
                fg_color = np_for_url.button.getCurrentTextColor();
                bg_color = np_for_url.button.getBackgroundColor () | 0xff000000; // .. but opaque
            }
        }
        // }}}
        // WTITLE COLORS .. f(WEBVIEW 1 2 or 3) {{{
        if(nb_wtitle != null)
        {
            nb_wtitle.setTextColor( fg_color ); nb_wtitle.setBackgroundColor( bg_color );
            if     (nb_wtitle == fs_wtitle2  )  fs_wswapL.setBackgroundColor( bg_color );
            else if(nb_wtitle == fs_wtitle3  )  fs_wswapR.setBackgroundColor( bg_color );
        }

        //}}}
        // WEBVIEW BUTTONS COLORS .. f(WEBVIEW 1) {{{
        if(wv == fs_webView)
        {
            // fg_color
            fs_wswapL   .setTextColor( fg_color );
            fs_wswapR   .setTextColor( fg_color );
            fs_browse   .setTextColor( fg_color );
            fs_bookmark .setTextColor( fg_color );
            if( wv.canGoBack   () ) { fs_goBack   .setEnabled( true); fs_goBack   .setTextColor( Settings.FG_COLOR_RDY ); }
            else                    { fs_goBack   .setEnabled(false); fs_goBack   .setTextColor( Settings.FG_COLOR_OFF ); }
            if( wv.canGoForward() ) { fs_goForward.setEnabled( true); fs_goForward.setTextColor( Settings.FG_COLOR_RDY ); }
            else                    { fs_goForward.setEnabled(false); fs_goForward.setTextColor( Settings.FG_COLOR_OFF ); }

            // bg_color
            wvContainer .setBackgroundColor( bg_color );
            fs_wswapL   .setBackgroundColor( bg_color );
            fs_wswapR   .setBackgroundColor( bg_color );
            fs_browse   .setBackgroundColor( bg_color );
            fs_bookmark .setBackgroundColor( bg_color );
            fs_goBack   .setBackgroundColor( bg_color );
            fs_goForward.setBackgroundColor( bg_color );
        }
        //}}}
        on_webView_changed(ON_WEBVIEW_COLORED,wv, caller); // LAYOUT [wvTools_sbX] and tools
    }
    //}}}
    // update_from_page_finished_url {{{
    private void update_from_page_finished_url(MWebView wv, String page_finished_url)
    {
        String caller = "update_from_page_finished_url";
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "page_finished_url=["+page_finished_url+"]");
        // 1/2 TITLE OF A BOOKMARK-REQUEST-URL {{{
        String wt = wv.getTitle(); if(wt == null) wt = "";
        boolean bookmarked_url_just_loaded = Settings.URL_equals(page_finished_url, Settings.BOOKMARK_URL);
        boolean bookmarked_title_updated   = !TextUtils.isEmpty( wt ) && !Settings.BOOKMARK_TITLE.equals( wt );

//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "bookmarked_url_just_loaded...=["+bookmarked_url_just_loaded+"]");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "bookmarked_title_updated.....=["+bookmarked_title_updated  +"]");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "Settings.BOOKMARK_URL........=["+Settings.BOOKMARK_URL     +"]");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "Settings.BOOKMARK_TITLE......=["+Settings.BOOKMARK_TITLE   +"]");

        if(bookmarked_url_just_loaded && bookmarked_title_updated)
        {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "UPDATING BOOKMARK:");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "...FROM ["+ Settings.BOOKMARK_TITLE  +"]");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, ".....TO ["+ wt                       +"]");
            Settings.set_BOOKMARK_TITLE( wt );

            // [UPDATE-OR-CREATE] [THE-BOOKMARKED-TAB] (only if it is plain empty)
            if(!update_profile_tabbed_url(Settings.BOOKMARK_TITLE, Settings.BOOKMARK_URL, true, caller) ) // notify
                bookmarkToFreeTab(caller);

            // UPDATE [BOOKMARK] UI TAB
            show_np_bookmark(); // BOOKMARK .. (LAST-BOOKMARKED)

        }
        // }}}
        // 2/3 TITLE OF A NAV-TO-URL {{{
        else {
            check_page_finished_url(wv, page_finished_url, caller);

        }
        // }}}
        // UPDATE (LAST VISITED WEBVIEW TAB) {{{
        show_np_webview(); // WEBVIEW .. (LAST-VISITED)

        //}}}
        on_webView_changed(ON_WEBVIEW_PAGE_FIN, wv, caller);
    }
    //}}}
    // check_page_finished_url {{{
    private void check_page_finished_url(MWebView wv, String page_finished_url, String caller)
    {
        caller += "->check_page_finished_url";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller, "[page_finished_url]"+ Settings.TRACE_OPEN);
        // GET THIS WEBVIEW (requested url) (current url) (current title) {{{
        String wt = wv.getTitle         ();   if(wt == null) wt = "";
        String wu = wv.getUrl           ();   if(wu == null) wu = "";
        String wr = wv.get_url_requested();   if(wr == null) wr = "";

//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "......page_finished_url=["+ page_finished_url +"]");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, ".........(TITLE == URL)=["+ wt.equals(wu)     +"]");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "...wv.getTitle         =["+ wt                +"]");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "...wv.get_url_requested=["+ wr                +"]");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "...wv.getUrl           =["+ wu                +"]");

        wu         = Settings.Get_normalized_url( wu );
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "...Get_normalized_url  =["+ wu                +"]");

        // }}}
        // ...return if TITLE NOT YET RECEIVED .. f(np_title == url) {{{
       if( wt.equals(wv.getUrl()) ) {
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller,"TITLE NOT YET RECEIVED"+ Settings.TRACE_CLOSE);
           return;
       }
       //}}}
        // [np_title] .. f(view url) {{{
       // look for the requested url .. (may differ in case of server-side redirection)
       NotePane               np_for_url = this_RTabsClient.get_np_with_url(wv.getUrl()           , caller); // ...actual url f(server-side redirection)
       if(np_for_url == null) np_for_url = this_RTabsClient.get_np_with_url(wv.get_url_requested(), caller); // requested url

       String np_title
           = ((np_for_url != null) && !np_for_url.has_empty_label())
           ?   np_for_url.getLabel()
           :   wt;
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "...np_title............=["+ np_title          +"]");

        // }}}
        // APPEND CURRENT #[anchor] TO DISPLAYED TITLE {{{
        int idx;
        if(TextUtils.equals(np_title, wt) )
        {
            idx = wu.lastIndexOf("#");
            if(idx > 0) {
                String anchor = " ("+ wu.substring(idx+1) +")";
                if(!np_title.contains(anchor))
                    np_title +=     anchor;
            }
        }
        // }}}
        // CLEANUP LOADED PAGE TITLE {{{
        // CUT PROTOCOL HEADER
        if(np_title.startsWith ("http") || np_title.startsWith ("ftp") || np_title.startsWith ("file"))
        { idx = np_title.lastIndexOf(     "/"); if(idx > 0) np_title = np_title.substring(  idx+1); }
        idx   = np_title.indexOf    (     "/"); if(idx > 0) np_title = np_title.substring(0,idx);

        // CUT REQUEST TRAILER
        // https://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5.3
        idx   = np_title.indexOf    (     '|'); if(idx > 0) np_title = np_title.substring(0,idx);
    //  idx   = np_title.indexOf    (     '-'); if(idx > 0) np_title = np_title.substring(0,idx); // 0x002D
        idx   = np_title.indexOf    (     ':'); if(idx > 0) np_title = np_title.substring(0,idx);
        idx   = np_title.indexOf    ('\u2212'); if(idx > 0) np_title = np_title.substring(0,idx); // MINUS SIGN
        idx   = np_title.indexOf    ('\u2796'); if(idx > 0) np_title = np_title.substring(0,idx); // HEAVY MINUS SIGN
        idx   = np_title.indexOf    ('\u2013'); if(idx > 0) np_title = np_title.substring(0,idx); // EM DASH
        idx   = np_title.indexOf    ('\u2012'); if(idx > 0) np_title = np_title.substring(0,idx); // FIGURE DASH

        // ERASE CHARS
        idx   = np_title.indexOf    ("_"); if(idx > 0) np_title = np_title.replace("_"," ");
        np_title = np_title.trim();

//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "...np_title............=["+ np_title          +"]");
        Settings.set_REQUESTED_TITLE( np_title );

        //}}}
        // UPDATE [Settings.REQUESTED_URL] .. f(page_finished_url) {{{
        boolean should_update_requested_url
            =  !page_finished_url.equals     (""                                       )
            && !Settings         .URL_equals (Settings.REQUESTED_URL, page_finished_url)
            ;
///*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "...XXX should_update_requested_url forcibly set to false");
//should_update_requested_url = false;

        if( should_update_requested_url )
        {
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller, "...Settings.REQUESTED_URL UPDATED:");
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller, "...FROM ["+ Settings.REQUESTED_URL +"]");
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller, ".....TO ["+ page_finished_url      +"]");

            Settings.set_REQUESTED_URL( page_finished_url );
        }
        else {
            Settings.set_REQUESTED_URL( wu );
        }

//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "...TITLE=["+ Settings.REQUESTED_TITLE +"]");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, ".....URL=["+ Settings.REQUESTED_URL   +"]");

        if(np_for_url != null)
        {
            String np_tag_url  = Settings.Del_color_hex_from_text( np_for_url.tag );
            update_profile_tabbed_url(Settings.REQUESTED_TITLE, np_tag_url, false, caller);
            // ...silently ignore already tabbed with a title
        }

        // }}}
        // UPDATE WEBVIEW TITLE, COLORS AND HISTORY {{{
        update_webview_colors( wv );

        // }}}
        // GUI WEB UPDATE .. (LAST VISITED WEBVIEW TAB) {{{
        show_np_webview( np_for_url );

        // }}}
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller+ Settings.TRACE_CLOSE);
    }
    //}}}
    // update_profile_tabbed_url {{{
    private boolean update_profile_tabbed_url(String title, String url, boolean notify, String caller)
    {
        caller +=  "->update_profile_tabbed_url(title=["+title+"],url,notify="+notify+")";
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "url=["+url+"]");

        // NO TAB FOR THIS URL IN [Working_profile] .. (should have been assigned by update_from_page_finished_url) {{{
        NotePane np_for_url = this_RTabsClient.get_np_with_url(url, caller);
        if(np_for_url == null)
        {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "..."+Settings.Working_profile+" HAS NO TAB TO UPDATE FOR THAT URL");

            return false;
        }
        //}}}
        // THERE IS A TAB WITH THIS URL .. CHEKING ITS LABEL {{{
        String label    = np_for_url.getLabel();

        String keywords = Settings.Parse_keywords_from_url(url);
        if( TextUtils.isEmpty(title) ) title = keywords;

        boolean updating_title
            =     label.equals(keywords)
            &&   !title.equals(keywords);

        /*}}}*/
        /* UPDATE TITLE {{{*/
        String msg = "";

        if(np_for_url.has_empty_label() || updating_title)
        {
            msg = "SETTING TITLE TO ["+title+"]";

            // UPDATE PROFILE .. f(NotePane text)
            np_for_url.setText( title );

            profile_update_np(np_for_url, Settings.ellipsis(np_for_url.getLabel(), 80));
        }
        // }}}
        // ANNOUNCE ALREADY TABED {{{
        else {
            msg =            "ALEREADY TABBED label=["+ label +"]"
                + (!label.equals(title) ? "\n TITLE=["+ title +"]" : "")
                ;

        }
        // }}}
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, msg.replace("\n",": "));
        if(notify) toast_long( msg );

        // MATCH LEFT MOST WEBVIEW {{{
        //magnify_np_layout(np_for_url, caller);
        shedule_update_WEBVIEW_TITLES_AND_COLORS( caller);

        // }}}
        return true;
    }
    //}}}
    // bookmarkToFreeTab {{{
    private void bookmarkToFreeTab(String caller)
    {
        // FETCH BOOKMARK PARAMS {{{
        caller += "->bookmarkToFreeTab";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);
        if(this_RTabsClient == null) return;

        String url   = Settings.BOOKMARK_URL;
        String title = Settings.BOOKMARK_TITLE;
        if(url.equals("") || title.equals(""))
        {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "*** MISSING DATA:"      );
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "***   url=["+ url   +"]");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "*** title=["+ title +"]");
            return;
        }
        // }}}

        // GET OR CREATE A TAB FOR THIS URL IN THE CURRENT WORKING PROFILE {{{
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "...YET NO TAB WITH TITLE=["+ title +"] URL=["+ url +"]");

        // 1/2 POPULATE FREE BOOKMARK TAB
        NotePane np = this_RTabsClient.get_np_free(caller);

        // 2/2 OR ADD A NEW TAB TO THE WORKING PROFILE
        if(np == null) {
            np      = this_RTabsClient.clone_np_at_bottom_left();
        }

        np.set_tag( url   );
        np.setText( title );
    //  np.button.fitText();
    //  np.button.invalidate();
    //  np.needs_fitText = false;
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "...NEW BOOKMARK TAB:"+ np.full_description());

        // }}}

        // MAGNIFY NotePane .. (current webview background)
        fs_button_setTag(null, caller); // .. (do not look for a replacement during profile update)

        // UPDATE PROFILE .. f(new NotePane)
        profile_update_np(np, Settings.ellipsis(np.getLabel(), 80));

        // CONSUME BOOKMARK REQUEST
//      Settings.set_BOOKMARK_TITLE( "" );
//      Settings.set_BOOKMARK_URL  ( "" );

        // UPDATE LEFT MOST WEBVIEW
        shedule_update_WEBVIEW_TITLES_AND_COLORS(caller);
    }
    //}}}

    //* WEBVIEW SIDETOOLS */
    // fs_wtitle {{{
    // create_fs_wtitle {{{
    private NpButton create_fs_wtitle()
    {
        NpButton np_button;
        np_button = new NpButton( activity );
        np_button.lockElevation     ( Settings.WV_BUTTON_ELEVATION);
    //  np_button.set_shape         ( NotePane.SHAPE_TAG_PADD_R   );
        np_button.set_shape         ( NotePane.SHAPE_TAG_CIRCLE   );
    //  np_button.set_shape         ( NotePane.SHAPE_TAG_ONEDGE   );
    //  np_button.set_round_corners_left ();
    //  np_button.set_round_corners_right();
        np_button.setTypeface       ( Settings.getNotoTypeface()  );
        np_button.setTextColor      ( FS_WTITLE_TEXTCOLOR         );
        np_button.setBackgroundColor( FS_WTITLE_WAITCOLOR         );

        np_button.setPadding        (  0, 0, 0, 0                 );
        np_button.setTypeface       ( Settings.getNotoTypeface()  );
        np_button.setEllipsize      ( TextUtils.TruncateAt.END    );

        np_button.setText           ( activity.getString(R.string.page_title) );
        np_button.setTag            ( Settings.FS_TITLE_INFO      );
        np_button.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL); // override NpButton.setText

        wvContainer.addView( np_button );

        np_button.setOnTouchListener( builtin_nb_OnTouchListener  );

        return np_button;
    }
    //}}}
    // fs_wtitle_CB {{{
    private void fs_wtitle_CB(NpButton nb)
    {
        String caller = "fs_wtitle_CB";
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "gesture_consumed_by_onFling=["+ gesture_consumed_by_onFling +"]");
        // CANCELED {{{
        if(gesture_consumed_by_onFling!=null) return;
        // }}}
        // ACTION {{{
        nb.setText( "reloading..." );

        MWebView /*.......................................*/ wv = fs_webView ; // fallback
        if     ((nb == fs_wtitle2) && (fs_webView2 != null)) wv = fs_webView2;
        else if((nb == fs_wtitle3) && (fs_webView3 != null)) wv = fs_webView3;

        // RELOAD REQUESTED URL
        String url = wv.get_url_requested();
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "RELOAD REQUESTED URL=["+ url +"]");

        // RELOAD CURRENT URL
        //String url = wv.getUrl();
//WEBVIEW//Settings.MON(TAG_WEBVIEW, caller, "RELOAD CURRENT   URL=["+ url +"]");

        if( url.startsWith("file") ) wv.assert_no_cache(caller);

        wv.js_init();

        wv.loadUrl( url );

//WEBVIEW//Settings.MON(TAG_WEBVIEW, caller, "[goBack...] [clearHistory] [reload] ");
        //while( wv.canGoBack() ) wv.goBack();
        //wv.clearHistory();
        //wv.reload();

        if( fs_webView_isGrabbed ) re_anim_grab_fs_webView(caller);
        else                       re_anim_free_fs_webView(caller);
/*
:!start explorer "http://www.mkyong.com/android/android-button-example/"
*/
        //}}}
    }
    //}}}
    //}}}
    // fs_browse {{{
    // create_fs_browse {{{
    private NpButton create_fs_browse()
    {
        // NpButton {{{
        NpButton np_button = new NpButton( activity );
        np_button.lockElevation     ( Settings.WV_BUTTON_ELEVATION );
        np_button.set_shape         ( NotePane.SHAPE_TAG_PADD_R    );
        np_button.setTypeface       ( Settings.getNotoTypeface()   );
        np_button.setTextColor      ( FS_BROWSE_TEXTCOLOR          );
        np_button.setBackgroundColor( FS_BROWSE_BACKCOLOR          );

        np_button.setPadding        (  0, 0, 0, 0                  );
        np_button.setTypeface       ( Settings.getNotoTypeface()   );
        np_button.setEllipsize      ( TextUtils.TruncateAt.END     );

        np_button.setText           ( Settings.FS_BROWSE_TEXT      );
        np_button.setTag            ( Settings.FS_BROWSE_INFO      );
        np_button.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL); // override NpButton.setText
        //}}}
        // np_button shadow shape {{{
/*
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            GradientDrawable d = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[] {Color.BLUE, Color.RED});
            d.set_shape(GradientDrawable.RECTANGLE);
            d.setCornerRadius(5);
            np_button.setBackground(d);
        }
*/
    // }}}
        // GUI {{{
        wvContainer.addView( np_button );
        // }}}
        // EVENTS {{{
        np_button.setOnTouchListener( builtin_nb_OnTouchListener  );
        // }}}
        return np_button;
    }
    //}}}
    // fs_browse_CB {{{
    private void fs_browse_CB()
    {
        String caller = "fs_browse_CB";
        // [url] {{{
        String url = fs_webView.getUrl();
        if(url == null) return;
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, caller+": url=["+url+"]");

        //}}}
        String intent_action = null;
        String apk_nickname  = null;
        NotePane np_for_url = this_RTabsClient.get_np_with_url(fs_webView.get_url_requested(), caller);
        if(np_for_url != null) {
        // [intent_action] .. (HINT [PICKER] or [CHOOSER] [IMPLICIT]) {{{
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "np_for_url.text=["+np_for_url.text+"]");
            String   s=np_for_url.text.toUpperCase();
            if     ( s.contains(  "PICK") ) intent_action = Intent.ACTION_PICK_ACTIVITY;
            else if( s.contains("CHOOSE") ) intent_action = Intent.ACTION_CHOOSER      ;
            else if( s.contains("BROWSE") ) intent_action = Intent.ACTION_OPEN_DOCUMENT;
            else if( s.contains(  "APK=") ) intent_action = Intent.ACTION_OPEN_DOCUMENT;

//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "...intent_action: f(np_for_url)=["+intent_action+"]");
        //}}}
        // [apk_nickname] {{{
            apk_nickname = Settings.Get_key_value_from_text(np_for_url.text, "apk");
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "...apk_nickname=["+apk_nickname+"]");
        //}}}
        }
        fs_browse_intent(url, intent_action, apk_nickname);
    }
    //}}}
    // fs_browse_intent {{{
    private void fs_browse_intent(String url, String intent_action, String apk_nickname)
    {
        // TODO:
        // call this method early for schemes not supported by a WebView
        // .. (directly from within load_fs_webView_url)
        // .. (take care of calls to load_fs_webView_url from bookmark_Clipboard_URL)
        // [URL] [scheme] {{{
        String caller = "fs_browse_intent";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "fs_browse_intent");

        url     = Settings.Get_normalized_url( url );
        String scheme = Settings.last_normalized_url_scheme;

//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "...normalized url=["+ url    +"]");
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "...........scheme=["+ scheme +"]");

        //}}}
        // URL TRIMMING .. f(scheme) {{{
        String intent_type   = null;
        if( scheme.equals("file") )
        {
            // CLEANUP IRRELEVANT ANCHOR PARAM {{{
            url = Settings.trim_url_anchor(url);
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "......trimmed url=["+url+"]");

            //}}}
            // [intent_type] .. f(url.endsWith) {{{
            intent_type = Settings.GetMimeType(url);
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "......intent_type=["+intent_type+"] .. f(MimeTypeMap)");

            if(intent_type == null)
            {
                if     (   url.endsWith(".txt" )) intent_type = "text/plain";                               // Text file
                else if(   url.endsWith(".html")
                        || url.endsWith(".htm" )) intent_type = "text/html";                                // HTML
                else if(   url.endsWith(".doc" )
                        || url.endsWith(".docx")) intent_type = "application/msword";                       // Word document
                else if(   url.endsWith(".pdf" )) intent_type = "application/pdf";                          // PDF file
                else if(   url.endsWith(".ppt" )
                        || url.endsWith(".pptx")) intent_type = "application/vnd.ms-powerpoint";            // Powerpoint file
                else if(   url.endsWith(".xls" )
                        || url.endsWith(".xlsx")) intent_type = "application/vnd.ms-excel";                 // Excel file
                else if(   url.endsWith(".zip" )
                        || url.endsWith(".rar" )) intent_type = "application/zip";                          // ZIP RAR
                else if(   url.endsWith(".rtf" )) intent_type = "application/rtf";                          // RTF file
                else if(   url.endsWith(".wav" )
                        || url.endsWith(".mp3" )) intent_type = "audio/x-wav";                              // WAV audio file
                else if(   url.endsWith(".gif" )) intent_type = "image/gif";                                // GIF file
                else if(   url.endsWith(".jpg" )
                        || url.endsWith(".jpeg")
                        || url.endsWith(".png" )) intent_type = "image/jpeg";                               // JPG file
                else if(   url.endsWith(".3gp" )
                        || url.endsWith(".mpg" )
                        || url.endsWith(".mpeg")
                        || url.endsWith(".mpe" )
                        || url.endsWith(".mp4" )
                        || url.endsWith(".avi" )) intent_type = "video/*";                                  // Video files
                else if(   url.endsWith("/"    )) intent_type = DocumentsContract.Document.MIME_TYPE_DIR;   // DIRECTORY
            //  else if(   url.endsWith("/"    )) intent_type = "text/directory";                           // DIRECTORY
            //  else if(   url.endsWith("/"    )) intent_type = "text/html";                                // DIRECTORY
                else                              intent_type = "*/*";                                      // show all applications installed on the device to choose from

//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "......intent_type=["+intent_type+"] .. f(url extension parsing)");
            }

/*
:!start explorer "http://www.androidsnippets.com/open-any-type-of-file-with-default-intent.html"
*/
            //}}}
        }
        //}}}
        // [ACTION_VIEW]  .. f(intent_type) .. fallback to [text/plain] {{{
        Intent   intent_viewer = new Intent( Intent.ACTION_VIEW );

        //}}}
        // RESOLVE DATA AND TYPE {{{
        Uri uri = Uri.parse( url );
        intent_viewer.setDataAndTypeAndNormalize(uri, intent_type);
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "......intent_viewer.getType("+intent_viewer.getType()+")");

        // FALBACK
        if(intent_viewer.resolveActivity(activity.getPackageManager()) == null)
        {
            intent_type = "*/*";
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "COULD NOT RESOLVE INTENT_TYPE: switching from ["+intent_viewer.getType()+"] to ["+intent_type+"]");
        //  toast_long(Settings.SYMBOL_NO_ENTRY+" COULD NOT RESOLVE INTENT_TYPE "+Settings.SYMBOL_NO_ENTRY
        //          +"\n...switching from ["+intent_viewer.getType()+"] to ["+intent_type+"]");

            intent_viewer.setDataAndTypeAndNormalize(uri, intent_type);
        }
        //}}}
        boolean intent_started = false;
        // (ACTION_OPEN_DOCUMENT) {{{
        if(!intent_started && (intent_action == Intent.ACTION_OPEN_DOCUMENT))
        {
/*
:!start explorer "http://stackoverflow.com/questions/29668798/how-to-view-html-file-from-application-data-on-chrome-browser-in-android"
:!start explorer "https://www.exploit-db.com/exploits/37792/"
*/
            try {
                // EXPLICIT APK (somewhere within NotePane [text field])
                // [  apk=CHROME] or [apk=com.google.android.apps.chrome.Main  ]
                // [class=CHROME] or [class=com.google.android.apps.chrome.Main]

                if( TextUtils.isEmpty( apk_nickname ) ) apk_nickname = Settings.BROWSER_CHROME_NICKNAME;

                String packageName = Settings.Expand_packageName( apk_nickname );
                String className   = Settings.Expand_className  ( apk_nickname );


                intent_viewer.setClassName(packageName, className);

                intent_viewer.addCategory( Intent.CATEGORY_BROWSABLE                 );
                intent_viewer.addFlags   ( Intent.FLAG_ACTIVITY_CLEAR_TOP            );
                intent_viewer.addFlags   ( Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS );
                intent_viewer.setFlags   ( Intent.FLAG_ACTIVITY_NEW_TASK             );
            //  intent_viewer.addFlags   ( Intent.FLAG_GRANT_READ_URI_PERMISSION     );

                activity.startActivity( intent_viewer );
                intent_started = true;
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "...HANDED TO [packageName]");
            }
            catch(Exception ex) { MLog.log_ex(ex, caller); }
        }
        //}}}
        // (ACTION_PICK_ACTIVITY) {{{
        if(!intent_started && (intent_action == Intent.ACTION_PICK_ACTIVITY)) {
            try {
                Intent intent_picker   = new Intent( Intent.ACTION_PICK_ACTIVITY);
                intent_picker.putExtra ( Intent.EXTRA_TITLE, "PICK an APP to open with" );
                intent_picker.putExtra ( Intent.EXTRA_INTENT, intent_viewer);

                activity.startActivity( intent_picker );
                intent_started = true;
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "...HANDED TO [ACTION_PICK_ACTIVITY]");
            }
            catch(Exception ex) { MLog.log_ex(ex, caller); }
        }
        //}}}
        // (ACTION_CHOOSER) {{{
        // should implement startActivityForResult ?
        if(!intent_started && (intent_action == Intent.ACTION_CHOOSER)) {
            try {
                Intent intent_chooser  = new Intent( Intent.ACTION_CHOOSER );
                intent_chooser.putExtra( Intent.EXTRA_TITLE, "CHOOSE an APP to open with");
                intent_chooser.putExtra( Intent.EXTRA_INTENT, intent_viewer);

                activity.startActivity( intent_chooser );
                intent_started = true;
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "...HANDED TO [ACTION_CHOOSER]");
            }
            catch(Exception ex) { MLog.log_ex(ex, caller); }
        }
        //}}}
        // (IMPLICIT CALL TO ACTION_VIEW) {{{
        if(!intent_started) {
            try {
                activity.startActivity( intent_viewer );
                intent_started = true;
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "...HANDED TO [ACTION_VIEW]");
            }
            catch(Exception ex) { MLog.log_ex(ex, caller); }
        }
        //}}}
/*
:!start explorer "http://www.mkyong.com/android/android-button-example/"
*/
    }
    //}}}
    //}}}
    // fs_bookmark {{{
    // create_fs_bookmark {{{
    private NpButton create_fs_bookmark()
    {
        // fs_bookmark {{{
        NpButton np_button = new NpButton( activity );
        np_button.lockElevation     ( Settings.WV_BUTTON_ELEVATION );
        np_button.set_shape         ( NotePane.SHAPE_TAG_PADD_R    );
        np_button.setTypeface       ( Settings.getNotoTypeface()   );
        np_button.setTextColor      ( FS_BOOKMARK_TEXTCOLOR        );
        np_button.setBackgroundColor( FS_BOOKMARK_BACKCOLOR        );

        np_button.setPadding        (  0, 0, 0, 0                  );
        np_button.setTypeface       ( Settings.getNotoTypeface()   );
        np_button.setEllipsize      ( TextUtils.TruncateAt.END     );

        np_button.setText           ( Settings.FS_BOOKMARK_TEXT    );
        np_button.setTag            ( Settings.FS_BOOKMARK_INFO    );
        np_button.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL); // override NpButton.setText
        //}}}
        // np_button shadow shape {{{
/*
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            GradientDrawable d = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[] {Color.BLUE, Color.RED});
            d.set_shape(GradientDrawable.RECTANGLE);
            d.setCornerRadius(5);
            np_button.setBackground(d);
        }
*/
        // }}}
        // GUI {{{
        wvContainer.addView( np_button );

        // }}}
        // EVENTS {{{
        np_button.setOnTouchListener( builtin_nb_OnTouchListener  );
        // }}}
        return np_button;
    }
    //}}}
    // fs_bookmark_CB {{{
    private void fs_bookmark_CB()
    {
        String caller = "fs_bookmark_CB";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);
        // ACTION {{{

        // URL
        String url = fs_webView.getUrl();
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "...fs_webView.getUrl()=["+url+"]");
        url        = Settings.Get_normalized_url( url );
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "...normalized url=["+url+"]");

        // TITLE
        String title = fs_webView.getTitle();
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "...fs_webView.getTitle()=["+title+"]");

Settings.set_BOOKMARK_URL  ( url   );
Settings.set_BOOKMARK_TITLE( title );
        if( !update_profile_tabbed_url(Settings.BOOKMARK_TITLE, Settings.BOOKMARK_URL, true, caller) ) // notify when already tabbed
            bookmarkToFreeTab(caller);
        //}}}
    }
    //}}}
    //}}}
    // fs_goBack fs_goForward .. (Handling Page Navigation) {{{
    // create_fs_goBack {{{
/*
:!start explorer "https://developer.android.com/guide/webapps/webview.html\#HandlingNavigation"
*/
    private NpButton create_fs_goBack()
    {
        // NpButton {{{
        NpButton np_button = new NpButton( activity );
        np_button.lockElevation     ( Settings.WV_BUTTON_ELEVATION );
        np_button.set_shape         ( NotePane.SHAPE_TAG_PADD_R    );
        np_button.setTypeface       ( Settings.getNotoTypeface()   );
        np_button.setTextColor      ( FS_GOBACK_TEXTCOLOR          );
        np_button.setBackgroundColor( FS_GOBACK_BACKCOLOR          );

        np_button.setPadding        (  0, 0, 0, 0                  );
        np_button.setTypeface       ( Settings.getNotoTypeface()   );
        np_button.setEllipsize      ( TextUtils.TruncateAt.END     );

        np_button.setText           ( Settings.FS_GOBACK_TEXT      );
        np_button.setTag            ( Settings.FS_GOBACK_INFO      );
        np_button.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL); // override NpButton.setText
        //}}}
        // GUI {{{
        wvContainer.addView( np_button );

        // }}}
        // EVENTS {{{
        np_button.setOnTouchListener( builtin_nb_OnTouchListener  );

        // }}}
        return np_button;
    }
    //}}}
    // create_fs_goForward {{{
    private NpButton create_fs_goForward()
    {
        // NpButton {{{
        NpButton np_button = new NpButton( activity );
        np_button.lockElevation     ( Settings.WV_BUTTON_ELEVATION );
        np_button.set_shape         ( NotePane.SHAPE_TAG_PADD_R    );
        np_button.setTypeface       ( Settings.getNotoTypeface()   );
        np_button.setTextColor      ( FS_GOBACK_TEXTCOLOR          );
        np_button.setBackgroundColor( FS_GOBACK_BACKCOLOR          );

        np_button.setPadding        (  0, 0, 0, 0                  );
        np_button.setTypeface       ( Settings.getNotoTypeface()   );
        np_button.setEllipsize      ( TextUtils.TruncateAt.END     );

        np_button.setText           ( Settings.FS_GOFORWARD_TEXT   );
        np_button.setTag            ( Settings.FS_GOFORWARD_INFO   );
        np_button.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL); // override NpButton.setText
        //}}}
        // GUI {{{
        wvContainer.addView( np_button );

        // }}}
        // EVENTS {{{
        np_button.setOnTouchListener( builtin_nb_OnTouchListener  );

        // }}}
        return np_button;
    }
    //}}}
    // fs_goBack_CB fs_goForward_CB {{{
    private void fs_goBack_CB() {
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "fs_goBack_CB");
        if( fs_webView.canGoBack()    ) {
            fs_webView.goBack();
            disable_webView_go_tools(); // to be re-enabled when title received
        }
    }
    private void fs_goForward_CB() {
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "fs_goForward_CB");
        if( fs_webView.canGoForward() ) {
            fs_webView.goForward();
            disable_webView_go_tools(); // to be re-enabled when title received
        }
    }
    //}}}
    // disable_webView_go_tools {{{
    private void disable_webView_go_tools()
    {
        /* BACK..............*/ { fs_goBack   .setEnabled(false); fs_goBack   .setTextColor( Settings.FG_COLOR_OFF ); }
        /* FORE..............*/ { fs_goForward.setEnabled(false); fs_goForward.setTextColor( Settings.FG_COLOR_OFF ); }
    }
    //}}}
    // UPDATE
    //}}}

    //* WEBVIEW LOAD */
    // bookmark_Clipboard_URL {{{
    public static void bookmark_Clipboard_URL(String url)
    {
//*WEBVIEW*/Settings.printStackTrace( "bookmark_Clipboard_URL("+url+")" );

        if(url.length() > 0)
            RTabs_instance._bookmark_Clipboard_URL( url );
    }
    private       void _bookmark_Clipboard_URL(String url)
    {
        String caller = "_bookmark_Clipboard_URL";
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "url=["+url+"]");
        //url = Settings.Get_normalized_url( url ); // moved up into Clipboard._store
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "...normalized url=["+url+"]");
        // DEFAULT TO SHOWING TABS {{{
        if(this_RTabsClient == null)
            return;

        if( is_magnify_np_showing()      ) magnify_np_hide(caller);
        if( is_view_showing(wvContainer) ) fs_webView_hide(caller);

        // }}}
        // UPDATE AN EXISTING TAB {{{
        if( update_profile_tabbed_url("", url, true, caller) ) // notify when already tabbed
            return;

        //}}}
        // CREATE A NEW BOOKMARK {{{
        Settings.set_BOOKMARK_URL   ( url                                   );
//      Settings.set_BOOKMARK_TITLE (  ""                                   ); // updated by onPageFinished
        Settings.set_BOOKMARK_TITLE ( Settings.Parse_keywords_from_url(url) ); // updated by onPageFinished
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "BOOKMARK (NEW):");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, ".....URL=["+ Settings.BOOKMARK_URL   +"]");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "...TITLE=["+ Settings.BOOKMARK_TITLE +"]");
            bookmarkToFreeTab(caller);

        // USE WEBVIEW TO RESOLVE BOOKMARK TITLE
        if(fs_webView == null)
            add_fs_webView();

        // TOAST
        toast_long(Settings.SYMBOL_DISK+" YET NOT TABBED URL "+Settings.SYMBOL_DISK
                +                       "\n...URL=["+url+"]");

        // LOAD URL AND GET A TITLE TO CREATE A TAB IN THE CURRENT PROFILE
        load_fs_webView_url(url, false, caller); // not from_user

        //}}}
    }
    //}}}
    // load_fs_webView_url {{{
    private static final int WEBVIEW1_LOAD_DELAY =                        200;
    private static final int WEBVIEW2_LOAD_DELAY = WEBVIEW1_LOAD_DELAY + 1000;
    private static final int WEBVIEW3_LOAD_DELAY = WEBVIEW2_LOAD_DELAY + 1000;
    private static final int WVSCRIPT_LOAD_DELAY =                        500;

    private void load_fs_webView_url(String _url, boolean from_user, String caller)
    {
        caller += "->load_fs_webView_url";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);
        if( _url.equals("") ) return;
        // 1/2 - [fs_webView fs_webView2 fs_webView3] .. f(WEBGROUP) {{{

        if(from_user) fs_webView_hide(caller); // hide MWebView instance before dereferencing
        else          fs_webview_session_stop(caller);

        fs_webView                    = null;
        fs_webView2                   = null;
        fs_webView3                   = null;

        fs_webViewL                   = null;
        fs_webViewC                   = null;
        fs_webViewR                   = null;

        if( fs_webView_expanded ) {
            fs_webView_expanded       = false;
            last_expanded_swap_side   = 0;
//*FULLSCREEN*/Settings.MOM(TAG_FULLSCREEN, "...SETTINGS fs_webView_expanded=["+fs_webView_expanded+"]");
        }

        pending_fs_webView_FULLSCREEN = null;

        String url1 = _url;
        String url2 = null;
        String url3 = null;

        String profile_key
            = from_user
            ?  WebGroup.get_profile_key(Settings.Working_profile, url1, caller)
            :  null;
        if(profile_key != null)
        {
            ArrayList<String> al = WebGroup.get_profile_url_ArrayList(profile_key, url1);
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "@@@ LOADING WEBGROUP ["+profile_key+"] (x"+al.size()+")");

            for(int i= 0; i < al.size(); ++i)
            {
                String alu = al.get(i);
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "alu=["+alu+"]");
                if     (fs_webView  == null) { url1 = alu; fs_webView  = get_WebView_for_url_requested( alu ); }
                else if(fs_webView2 == null) { url2 = alu; fs_webView2 = get_WebView_for_url_requested( alu ); }
                else                         { url3 = alu; fs_webView3 = get_WebView_for_url_requested( alu ); }
            }
        }
        // }}}
        // 2/2 - SINGLE URL {{{
        else {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "@@@ LOADING SINGLE URL["+url1+"]");
            fs_webView = get_WebView_for_url_requested( url1 );
        }

        //}}}
        //  LAYOUT .. f(# URL){{{
        adjust_WEBVIEW_LAYOUT(get_fs_webView_count(), caller);
      //wvTools.marker_set_someone_has_called_hide_marks(caller);
        wvTools.hide(caller);

        //}}}
        // LOAD OR UPDATE {{{
        // fs_webView {{{
        if(fs_webView != null) {
            // 1/2 UPDATE
            if( Settings.URL_equals(url1, fs_webView.get_url_requested()) )
            {
                check_page_finished_url(fs_webView , "", caller);
            }
            // 2/2 LOAD
            else {
                fs_webView.url_requested = url1;
                fs_webView.loaded_js     = "";
                handler.re_postDelayed( hr_fs_webView1_loadUrl, WEBVIEW1_LOAD_DELAY);
            }
        }
        //}}}
        // fs_webView2 {{{
        if(fs_webView2 != null) {
            // 1/2 UPDATE
            if( Settings.URL_equals(url2, fs_webView2.get_url_requested()) )
            {
                check_page_finished_url(fs_webView2 , url2, caller);
            }
            // 2/2 LOAD (DELAYED)
            else {
                fs_webView2.url_requested = url2;
                fs_webView2.loaded_js     = "";
                handler.re_postDelayed( hr_fs_webView2_loadUrl, WEBVIEW2_LOAD_DELAY);
            }
        }
        //}}}
        // fs_webView3 {{{
        if(fs_webView3 != null) {
            // 1/2 UPDATE
            if( Settings.URL_equals(url3, fs_webView3.get_url_requested()) )
            {
                check_page_finished_url(fs_webView3 , url3, caller);
            }
            // 2/2 LOAD (DELAYED)
            else {
                fs_webView3.url_requested = url3;
                fs_webView3.loaded_js     = "";
                handler.re_postDelayed( hr_fs_webView3_loadUrl, WEBVIEW3_LOAD_DELAY);
            }
        }
        //}}}
        // INITIAL LEFT-TO-RIGH POSITION {{{
        fs_webViewL = fs_webView ; // FIRST
        fs_webViewC = fs_webView2; // SECOND
        fs_webViewR = fs_webView3; // THIRD
        //}}}
        //}}}
        // SHOW {{{
        if(from_user)
            fs_webview_session_start(caller);
        // }}}
    }
    // }}}
    // adjust_WEBVIEW_LAYOUT {{{
/*
/^[^"\/]*\zs\<\(t\|l\)\>[^!=;)]*\(\n\s*\)\?=[^=;]\+
/^[^"\/]*\zs\<\(t_w\|t_h\)\>[^!=;)]*\(\n\s*\)\?=[^=;]\+
/^[^"\/]*\zs\<\(p_w\|p_h\)\>[^!=;)]*\(\n\s*\)\?=[^=;]\+
/^[^"\/]*\zs\<\(s_w\|s_h\)\>[^!=;)]*\(\n\s*\)\?=[^=;]\+
/^[^"\/]*\zs\<\(b_w\|b_h\)\>[^!=;)]*\(\n\s*\)\?=[^=;]\+
/^[^"\/]*\zs\<\(w_w\|w_h\)\>[^!=;)]*\(\n\s*\)\?=[^=;]\+
/^[^"\/]*\zs\<\([^sbw]_w\|[^sbw]_h\)\>[^!=;)]*\(\n\s*\)\?=[^=;]\+
*/
    private void adjust_WEBVIEW_LAYOUT(int split_count, String caller)
    {
        // START ALL HIDDEN {{{
        caller += "->adjust_WEBVIEW_LAYOUT(split_count="+split_count+")";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);
        // don't create fs_webView as it should come from a pool (to be populated from an an url-aware-condition)
        if(fs_webView == null) return;

        boolean was_fs_webView_showing = fs_webview_session_running && is_view_showing(fs_webView);
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "...was_fs_webView_showing=["+was_fs_webView_showing+"]");
        fs_webView_hide(caller);
        //}}}
        // CREATE VIEWS {{{
        if(fs_wtitle == null)
        {
            fs_wtitle    = create_fs_wtitle();
            fs_wtitle2   = create_fs_wtitle();
            fs_wtitle3   = create_fs_wtitle();

            fs_wswapL    = create_fs_wswap ();
            fs_wswapR    = create_fs_wswap ();

            fs_browse    = create_fs_browse ();
            fs_bookmark  = create_fs_bookmark ();
            fs_goBack    = create_fs_goBack   ();
            fs_goForward = create_fs_goForward();

        //  wvTools.create_views();
        }
        //}}}
        // SPLIT SIZE {{{
        // SCREEN_W = MARGIN + [WEBVIEW + GAP] X [SPLIT]
        Settings.SCREEN_SPLIT  = split_count;
        int split_width        = (Settings.SCREEN_W - Settings.WEBVIEW_MARGIN) / Settings.SCREEN_SPLIT;
        Settings.WEBVIEW_WIDTH = split_width - Settings.WEBVIEW_GAP;

        //}}}
        // ...fs_wtitle[13] fs_wswap[LR] {{{
        int t_w =   Settings.WEBVIEW_WIDTH  - 2*Settings.WEBVIEW_GAP;
        int t_h = 2*Settings.WEBVIEW_MARGIN -   Settings.WEBVIEW_GAP + Settings.WEBVIEW_OVER;
        int   t = 0;
        int   l =   Settings.WEBVIEW_MARGIN +     Settings.WEBVIEW_GAP;

        // fs_wtitle
        FrameLayout.LayoutParams flp;
        flp = new FrameLayout.LayoutParams(t_w,t_h);
        flp.topMargin  = t;
        flp.leftMargin = l;
        fs_wtitle      .setLayoutParams( flp );
        fs_wtitle      .setPivotX(t_w/2);
        fs_wtitle      .setPivotY(t_h/2);

        // ...fs_wswapL
        int p_w, p_h; // WIDTH, HEIGHT
        p_w = 4*Settings.WEBVIEW_GAP;
        p_h = 2*t_h;
        flp = new FrameLayout.LayoutParams(p_w,p_h);
        flp.topMargin  = 0;//Settings.WEBVIEW_GAP;
        flp.leftMargin = Settings.WEBVIEW_MARGIN + 1*split_width - Settings.WEBVIEW_GAP - p_w/2;
        fs_wswapL      .setLayoutParams( flp );
        fs_wswapL      .setPivotX(p_w/2);
        fs_wswapL      .setPivotY(p_h/2);

        // fs_wtitle2
        l  += split_width;
        flp = new FrameLayout.LayoutParams(t_w,t_h);
        flp.topMargin  = t;
        flp.leftMargin = l;
        fs_wtitle2     .setLayoutParams( flp );
        fs_wtitle2     .setPivotX(t_w/2);
        fs_wtitle2     .setPivotY(t_h/2);

        // ...fs_wswapR
        flp = new FrameLayout.LayoutParams(p_w,p_h);
        flp.topMargin  = 0;//Settings.WEBVIEW_GAP;
        flp.leftMargin = Settings.WEBVIEW_MARGIN + 2*split_width - Settings.WEBVIEW_GAP - p_w/2;
        fs_wswapR      .setLayoutParams( flp );
        fs_wswapR      .setPivotX(p_w/2);
        fs_wswapR      .setPivotY(p_h/2);

        // fs_wtitle3
        l  += split_width;
        flp = new FrameLayout.LayoutParams(t_w,t_h);
        flp.leftMargin = l; // not animated property
        flp.topMargin  = t;
        fs_wtitle3     .setLayoutParams( flp );
        fs_wtitle3     .setPivotX(t_w/2);
        fs_wtitle3     .setPivotY(t_h/2);

        // }}}
        // WV_TOOLS {{{
        //wvTools.fs_search_park(caller);

        // }}}
        // ...fs_browse {{{
        t =    2*Settings.WEBVIEW_MARGIN +   Settings.WEBVIEW_GAP; // below fs_webView
        l   = 0;
        int b_w, b_h; // WIDTH, HEIGHT
        b_w   = Settings.WEBVIEW_MARGIN +   Settings.WEBVIEW_OVER;
        b_h = (    Settings.DISPLAY_H      - 2*Settings.WEBVIEW_MARGIN - 4*Settings.WEBVIEW_GAP) / 3;

        flp = new FrameLayout.LayoutParams(b_w,b_h);
        flp.leftMargin = l;
        flp.topMargin  = t;
        fs_browse      .setLayoutParams( flp );
        fs_browse      .setPivotX(b_w/2);

        // }}}
        // ...fs_bookmark {{{
        t  = 2*Settings.WEBVIEW_MARGIN +   Settings.WEBVIEW_GAP; // below fs_webView
        t += b_h+Settings.WEBVIEW_GAP; // below fs_browse
        l  = 0;
        b_w  =   Settings.WEBVIEW_MARGIN +   Settings.WEBVIEW_OVER;
        b_h  = ( Settings.DISPLAY_H      - 2*Settings.WEBVIEW_MARGIN - 4*Settings.WEBVIEW_GAP) / 3;

        flp = new FrameLayout.LayoutParams(b_w,b_h);
        flp.leftMargin = l; // not animated property
        flp.topMargin  = t;
        fs_bookmark    .setLayoutParams( flp );
        fs_bookmark    .setPivotX(b_w/2);

        // }}}
        // ...fs_goBack {{{
        t += b_h + Settings.WEBVIEW_GAP; // below fs_bookmark
        l  = 0;
        b_w  =   Settings.WEBVIEW_MARGIN +   Settings.WEBVIEW_OVER;
        b_h  = ( Settings.DISPLAY_H      - 2*Settings.WEBVIEW_MARGIN - 4*Settings.WEBVIEW_GAP) / 3;
        b_h /= 3; // .. smaller than above

        flp = new FrameLayout.LayoutParams(b_w,b_h);
        flp.leftMargin = l; // not animated property
        flp.topMargin  = t;
        fs_goBack      .setLayoutParams( flp );
        fs_goBack      .setPivotX(b_w/2);

        // }}}
        // ...fs_goForward {{{
        t += b_h+Settings.WEBVIEW_GAP; // below fs_goBack
        l  = 0;
        b_w  =   Settings.WEBVIEW_MARGIN +   Settings.WEBVIEW_OVER;
        b_h  = ( Settings.DISPLAY_H      - 2*Settings.WEBVIEW_MARGIN - 4*Settings.WEBVIEW_GAP) / 3;
        b_h /= 3; // .. smaller than above

        flp = new FrameLayout.LayoutParams(b_w,b_h);
        flp.leftMargin = l; // not animated property
        flp.topMargin  = t;
        fs_goForward   .setLayoutParams( flp );
        fs_goForward   .setPivotX(b_w/2);

        // }}}
        // ...fs_webView {{{
        t =    2*Settings.WEBVIEW_MARGIN;   // fixed offset from top
        l =      Settings.WEBVIEW_MARGIN;
        int w_w, w_h; // WIDTH, HEIGHT
        w_w =      Settings.WEBVIEW_WIDTH;
        w_h =      Settings.DISPLAY_H - 2*Settings.WEBVIEW_MARGIN - Settings.WEBVIEW_GAP;

        flp = new FrameLayout.LayoutParams(w_w,w_h);
        flp.leftMargin = l; // not animated property
        flp.topMargin  = t;
        fs_webView.setLayoutParams( flp );
        fs_webView.setPivotX(0);
        fs_webView.setPivotY(0);

        // }}}
        // ...fs_webView #2 {{{
        if(Settings.SCREEN_SPLIT > 1)
        {
            //fs_webView2 = get_WebView_right_of(fs_webView ); // keep loadUrl results
            if(fs_webView2 != null) {
                l  += split_width;
                flp = new FrameLayout.LayoutParams(w_w,w_h);
                flp.leftMargin = l; // not animated property
                flp.topMargin  = t;
                fs_webView2.setLayoutParams( flp );
                fs_webView2.setPivotX(0);
                fs_webView2.setPivotY(0);
            }
        }
        // }}}
        // ...fs_webView #3 {{{
        if(Settings.SCREEN_SPLIT > 2)
        {
            //fs_webView3 = get_WebView_right_of(fs_webView2); // keep loadUrl results
            if(fs_webView3 != null) {
                l  += split_width;
                flp = new FrameLayout.LayoutParams(w_w,w_h);
                flp.leftMargin = l; // not animated property
                flp.topMargin  = t;
                fs_webView3.setLayoutParams( flp );
                fs_webView3.setPivotX(0);
                fs_webView3.setPivotY(0);
            }
        }
        // }}}
        // RESTORE VISIBILITY .. (if was showing) {{{
        if( was_fs_webView_showing ) fs_webView_show(caller+": was_fs_webView_showing");

        //}}}
        //frameLayout.requestLayout();
        on_webView_changed(ON_WEBVIEW_ADJUSTED, fs_webView, caller);
    }
    //}}}
    // hr_fs_webView[123]_loadUrl {{{
    private final Runnable hr_fs_webView1_loadUrl = new Runnable()
    {
        @Override public void run() {
        String caller = "hr_fs_webView1_loadUrl";
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "DELAY=["+WEBVIEW1_LOAD_DELAY+"]");
            update_webview_colors( fs_webView );
            fs_webView .anchor = "";
            String url = fs_webView.url_requested;

//url = url.replace("ftp://", "file://");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "fs_webView .loadUrl("+url+")");
            fs_webView .loaded_js = "";

            if( url.startsWith("file") ) fs_webView.assert_no_cache(caller);
            fs_webView .loadUrl( url );
        }
    };

    private final Runnable hr_fs_webView2_loadUrl = new Runnable()
    {
        @Override public void run() {
        String caller = "hr_fs_webView2_loadUrl";
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "DELAY=["+WEBVIEW2_LOAD_DELAY+"]");
            update_webview_colors( fs_webView2 );
            fs_webView2.anchor = "";
            String url = fs_webView2.url_requested;

//url = url.replace("ftp://", "file://");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "fs_webView2.loadUrl("+url+")");
            fs_webView2.loaded_js = "";

            if( url.startsWith("file") ) fs_webView2.assert_no_cache(caller);
            fs_webView2.loadUrl( url );
        }
    };

    private final Runnable hr_fs_webView3_loadUrl = new Runnable()
    {
        @Override public void run() {
        String caller = "hr_fs_webView3_loadUrl";
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "WEBVIEW3_LOAD_DELAY=["+WEBVIEW3_LOAD_DELAY+"]");
            update_webview_colors( fs_webView3 );
            fs_webView3.anchor = "";
            String url = fs_webView3.url_requested;

//url = url.replace("ftp://", "file://");
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "fs_webView3.loadUrl("+url+")");
            fs_webView3.loaded_js = "";

            if( url.startsWith("file") ) fs_webView3.assert_no_cache(caller);
            fs_webView3.loadUrl( url );
        }
    };

    //}}}
    // reload_WEBVIEW {{{
    private void reload_WEBVIEW()
    {
        if( TextUtils.isEmpty( Settings.REQUESTED_URL  ) ) return;

        load_fs_webView_url(Settings.REQUESTED_URL, true, "reload_WEBVIEW");
    }
    // }}}
    // reload_bookmark {{{
    private void reload_BOOKMARK()
    {
        if( TextUtils.isEmpty( Settings.BOOKMARK_URL ) ) return;

        load_fs_webView_url(Settings.BOOKMARK_URL, true, "reload_BOOKMARK");
    }
    // }}}
    // schedule_sb_maxed_cooldown {{{
    // {{{
    private NpButton highlighted_sbX = null;
    private int      highlighted_bg  = 0;

    // }}}
    // schedule_sb_maxed_cooldown {{{
    public void schedule_sb_maxed_cooldown(NpButton sbX, long delay)
    {
        // restore (pending)
        if(highlighted_sbX != null) {
            highlighted_sbX.setBackgroundColor( highlighted_bg );
        }

        // sbX and its bg
        highlighted_sbX = sbX;
        highlighted_bg  = sbX.getBackgroundColor();

        // highlight
        highlighted_sbX.setBackgroundColor( Color.WHITE );

        // post a restore
        handler.re_postDelayed(hr_sb_maxed_cooldown, delay);
    }
    //}}}
    // hr_sb_maxed_cooldown {{{
    private final Runnable hr_sb_maxed_cooldown = new Runnable() {
        @Override public void run() {
            clear_sb_maxed_cooldown( highlighted_sbX );
        }
    };
    //}}}
    // has_sb_on_cooldown {{{
    public boolean has_sb_on_cooldown(NpButton sbX)
    {
        return ((sbX != null) && (highlighted_sbX == sbX));
    }
    //}}}
    // clear_sb_maxed_cooldown {{{
    public void clear_sb_maxed_cooldown(NpButton sbX)
    {
        if((sbX != null) && (highlighted_sbX == sbX))
            highlighted_sbX.setBackgroundColor( highlighted_bg );
        highlighted_sbX = null;
        highlighted_bg  = 0;
    }
    //}}}
    //}}}
    // shedule_update_WEBVIEW_TITLES_AND_COLORS {{{
    private static final int UPDATE_WEBVIEW_TITLES_AND_COLORS_DELAY = WEBVIEW_TO_MAGNIFY_DELAY + 100;
    private String update_WEBVIEW_TITLES_AND_COLORS_callers = "";

    private   void shedule_update_WEBVIEW_TITLES_AND_COLORS(String caller)
    {
        update_WEBVIEW_TITLES_AND_COLORS_callers += " "+caller;
        caller += "->shedule_update_WEBVIEW_TITLES_AND_COLORS";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);

        handler.re_postDelayed(hr_update_WEBVIEW_TITLES_AND_COLORS, UPDATE_WEBVIEW_TITLES_AND_COLORS_DELAY);
    }

    private final Runnable hr_update_WEBVIEW_TITLES_AND_COLORS = new Runnable() {
        @Override public void run() { do_update_WEBVIEW_TITLES_AND_COLORS( update_WEBVIEW_TITLES_AND_COLORS_callers ); }
    };

    private void do_update_WEBVIEW_TITLES_AND_COLORS(String caller)
    {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, "do_update_WEBVIEW_TITLES_AND_COLORS", update_WEBVIEW_TITLES_AND_COLORS_callers);
        update_WEBVIEW_TITLES_AND_COLORS_callers = "";

        if(fs_webView == null) return;

        // SHOW NOTHING IF NOT CURRENTLY SHOWING .. (i.e. adding a NEW-URL-TAB into Working_profile)
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "fs_webview_session_running=["+ fs_webview_session_running +"]");
        if( !fs_webview_session_running ) return;

        // GET THE BACKGROUND COLOR FOR THAT URL
        // ... 1/2 [(look for) [NAV-TO-URL] .. (np color from Working_profile)
        NotePane               np_for_url = this_RTabsClient.get_np_with_url(fs_webView.getUrl()           , caller);
        // ... 2/2 back to] [REQUESTED-URL] .. (np color from Working_profile)
        if(np_for_url == null) np_for_url = this_RTabsClient.get_np_with_url(fs_webView.get_url_requested(), caller);

        // OPENING WEBVIEW ANIMATION
    //  if(np_for_url != null) magnify_np_layout(np_for_url, caller);

        // MATCH WEBTOOLS .. (TITLE INFO OPACITY WEBTOOLS-colors NAV-colors)
        /* ...................................................*/ update_webview_colors( fs_webView  );
        if((Settings.SCREEN_SPLIT > 1) && (fs_webView2 != null)) update_webview_colors( fs_webView2 );
        if((Settings.SCREEN_SPLIT > 2) && (fs_webView3 != null)) update_webview_colors( fs_webView3 );
    }
    //}}}

    //* WEBVIEW DISPLAY */
    //{{{
    private boolean fs_webview_session_running = false;

    //}}}
    // fs_webview_session_start {{{
    private void fs_webview_session_start(String caller)
    {
        // START WEBVIEW SESSION {{{
        caller += "->fs_webview_session_start";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);

        if(fs_webView == null) return;

        fs_webview_session_running = true;
        //}}}
        // [fs_webViewX] .. (show) {{{
        fs_webView_show(caller);

        // }}}
        // [FREE]->[GRAB] .. (force grab) {{{
        if( !fs_webView_isGrabbed )    anim_grab_fs_webView(caller); // WEBVIEW (anim)
        else                        re_anim_grab_fs_webView(caller); // WEBVIEW (anim)

        // }}}
        // [wvTools] .. (force left strip) {{{
        wvTools.sb_layout_shrink_to_shape();            // shaped scrollbar
        wvTools.fs_search_show_webView_swap_side( Settings.SWAP_0 ); // left webview strip

        // }}}
        // [fs_button] .. (hide magnified button) {{{
        fs_button_hide(caller);
        // }}}
    }
    //}}}
    // fs_webview_session_stop {{{
    private void fs_webview_session_stop(String caller)
    {
        // STOP WEBVIEW SESSION {{{
        caller += "->fs_webview_session_stop";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);

        if(fs_webView == null) return;

        fs_webview_session_running = false;
        //}}}
        // 1. [fs_button] .. (show magnified button on top) {{{
        fs_button_show(caller);

        // }}}
        // 2. [fs_webViewX] .. (hide) {{{
        fs_webView_hide(caller); // WEBVIEW    INVISIBLE (intant)

        // }}}
        // 3. [fs_button] .. (animate back to its url source tab) {{{
        magnify_np_hide(caller);

        // }}}
        // [tabs_container] .. (restore access to) {{{
        set_tabs_scrolling( true, caller);

        // }}}
    }
//}}}
    // fs_webView_session_cycle_grab_collapse_or_hide {{{
    private void fs_webView_session_cycle_grab_collapse_or_hide(String caller)
    {
        caller += "->fs_webView_session_cycle_grab_collapse_or_hide";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "....fs_webview_session_running=["+ fs_webview_session_running  +"]");
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, ".......magnify_has_pushed_np()=["+ magnify_has_pushed_np()     +"]");
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "..........fs_webView_isGrabbed=["+ fs_webView_isGrabbed        +"]");
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "...gesture_consumed_by_onFling=["+ gesture_consumed_by_onFling +"]");

        if( fs_webView_expanded ) {
            if(  !fs_webView_isGrabbed ) anim_grab_fs_webView          (caller); // [EXPANDED]->[GRABBED]
            else                         collapse_fs_webView_fullscreen(caller); //             [GRABBED]->[COLLAPSE]
        }
        else if( !fs_webView_isGrabbed ) anim_grab_fs_webView          (caller); //             [GRABBED]->[COLLAPSE]
        else                             fs_webview_session_stop       (caller); //                        [COLLAPSE]->[HIDE]
    }
    //}}}
    // fs_webView_hide {{{
    private void fs_webView_hide(String caller)
    {
        caller += "->fs_webView_hide";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);

        // [wvContainer] (HIDE)
        if(wvContainer        != null   )
            wvContainer.setVisibility( View.GONE );

        // [tabs_container] (bringToFront)
        if(this_RTabsClient != null)
            this_RTabsClient.tabs_container.bringToFront();

        // WEBVIEWS (HIDE)
        if( fs_webView        != null   ) /*......................*/ fs_webView .setVisibility( View.GONE );
        if( is_view_showing(fs_webView2)) /*......................*/ fs_webView2.setVisibility( View.GONE );
        if( is_view_showing(fs_webView3)) /*......................*/ fs_webView3.setVisibility( View.GONE );

        // RELEASE ANY SELECTION .. (TO KILL COPY-PASTE POPUP)
        if(fs_webView  != null) fs_webView .clear_selection();
        if(fs_webView2 != null) fs_webView2.clear_selection();
        if(fs_webView3 != null) fs_webView3.clear_selection();

        wvTools.fs_search_hide_if_not_clamping();
        wvTools.marker_clear_pending_updates(caller);
        wvTools.marker_hide_all_markers     (caller);
        wvTools.marker_wv_sync              (caller);

        // WEBVIEW TOOLS
        if( wvTools.sb        != null   ) /*......................*/ wvTools.sb .setVisibility( View.GONE );
        if( wvTools.sb2       != null   ) /*......................*/ wvTools.sb2.setVisibility( View.GONE );
        if( wvTools.sb3       != null   ) /*......................*/ wvTools.sb3.setVisibility( View.GONE );

        // [fs_webViewX] (PAUSE)
        if( fs_webView        != null   ) /*......................*/ fs_webView .onPause();
        if( is_view_showing(fs_webView2)) /*......................*/ fs_webView2.onPause();
        if( is_view_showing(fs_webView3)) /*......................*/ fs_webView3.onPause();

        // [wvTools] (HIDE)
        fs_tool_hide(caller);

        //schedule_on_webView_sync(caller);
    }
    //}}}
    // fs_webView_show {{{
    private void fs_webView_show(String caller)
    {
        caller += "->fs_webView_show";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);

        // [fs_webViewX] (SHOW)
        if(    fs_webView     != null   ) /*......................*/ { fs_webView .setVisibility( View.VISIBLE ); fs_webView .bringToFront(); }
        if( is_fs_webView2_in_screen()  ) /*......................*/ { fs_webView2.setVisibility( View.VISIBLE ); fs_webView2.bringToFront(); }
        if( is_fs_webView3_in_screen()  ) /*......................*/ { fs_webView3.setVisibility( View.VISIBLE ); fs_webView3.bringToFront(); }

        wvTools.marker_wv_sync(caller);

        // [fs_webViewX] (RESUME)
        if( fs_webView        != null   ) /*......................*/ fs_webView .onResume();
        if( is_view_showing(fs_webView2)) /*......................*/ fs_webView2.onResume();
        if( is_view_showing(fs_webView3)) /*......................*/ fs_webView3.onResume();

        // [wvTools] (SHOW)
        if( fs_webView_isGrabbed ) fs_tool_show(caller);
        else                       fs_tool_hide(caller);
        schedule_on_webView_sync(caller);                   // [wvTools.sb] recompute scrollbar

        // [wvContainer] (SHOW)
        if(wvContainer != null)
        {
            wvContainer.setVisibility( View.VISIBLE );
// XXX
//if( !wvTools.property_get(WVTools.WV_TOOL_FLAG_1) )
wvContainer.bringToFront();
        }
    }
    //}}}
    // fs_tool_show {{{
    private void fs_tool_show(String caller)
    {
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);

        if(fs_wtitle    != null) /*............................*/ { fs_wtitle   .setVisibility( View.VISIBLE ); fs_wtitle   .bringToFront(); }
        if(fs_wtitle2   != null) if( is_fs_webView2_in_screen() ) { fs_wtitle2  .setVisibility( View.VISIBLE ); fs_wtitle2  .bringToFront(); }
        if(fs_wtitle3   != null) if( is_fs_webView3_in_screen() ) { fs_wtitle3  .setVisibility( View.VISIBLE ); fs_wtitle3  .bringToFront(); }
        if(fs_wswapL    != null) if( is_fs_webView2_in_screen() ) { fs_wswapL   .setVisibility( View.VISIBLE ); fs_wswapL   .bringToFront(); }
        if(fs_wswapR    != null) if( is_fs_webView3_in_screen() ) { fs_wswapR   .setVisibility( View.VISIBLE ); fs_wswapR   .bringToFront(); }
        if(fs_browse    != null) /*............................*/ { fs_browse   .setVisibility( View.VISIBLE ); fs_browse   .bringToFront(); }
        if(fs_bookmark  != null) /*............................*/ { fs_bookmark .setVisibility( View.VISIBLE ); fs_bookmark .bringToFront(); }
        if(fs_goBack    != null) /*............................*/ { fs_goBack   .setVisibility( View.VISIBLE ); fs_goBack   .bringToFront(); }
        if(fs_goForward != null) /*............................*/ { fs_goForward.setVisibility( View.VISIBLE ); fs_goForward.bringToFront(); }
    }
    //}}}
    // fs_tool_hide {{{
    private void fs_tool_hide(String caller)
    {
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);

        if(fs_wtitle    != null) /*............................*/ fs_wtitle   .setVisibility( View.GONE );
        if(fs_wtitle2   != null) /*............................*/ fs_wtitle2  .setVisibility( View.GONE );
        if(fs_wtitle3   != null) /*............................*/ fs_wtitle3  .setVisibility( View.GONE );
        if(fs_wswapL    != null) /*............................*/ fs_wswapL   .setVisibility( View.GONE );
        if(fs_wswapR    != null) /*............................*/ fs_wswapR   .setVisibility( View.GONE );
        if(fs_browse    != null) /*............................*/ fs_browse   .setVisibility( View.GONE );
        if(fs_bookmark  != null) /*............................*/ fs_bookmark .setVisibility( View.GONE );
        if(fs_goBack    != null) /*............................*/ fs_goBack   .setVisibility( View.GONE );
        if(fs_goForward != null) /*............................*/ fs_goForward.setVisibility( View.GONE );

    }
    //}}}
    // wvContainer_OnClickListener .. (free-grab-hide) {{{
    private final View.OnClickListener wvContainer_OnClickListener = new View.OnClickListener()
    {
        @Override public void onClick(View view) // can be stolen by LongClickListener below
        {
            // NOT A CLICK {{{
            String caller = "wvContainer_OnClickListener";
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, caller+": has_moved_enough=["+has_moved_enough+"]");

            if( has_moved_enough ) return;

            //}}}
            // WEBVIEW CYCLE: [show]->[expanded]->[collapsed]->[free]->[grabbed]->[hidden] {{{
            if( fs_webview_session_running )
                //XXX if((gesture_consumed_by_onFling!=null)
                    fs_webView_session_cycle_grab_collapse_or_hide(caller);

            // }}}
            // [wvContainer] HIDE .. f(gesture_consumed_by_onFling==null) {{{
            else if(gesture_consumed_by_onFling==null)
                fs_webView_hide(caller);

            // }}}
        }
    };
    //}}}
    // wvContainer_OnLongClickListener .. (free-grab) {{{
    private final View.OnLongClickListener wvContainer_OnLongClickListener = new View.OnLongClickListener()
    {
        @Override
        public boolean onLongClick(View view)
        {
            // NOT A LONG CLICK {{{
            String caller = "wvContainer_OnLongClickListener";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);
            if(has_moved_enough ) {
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "NOT A LONG CLICK: has_moved_enough=["+has_moved_enough+"]");

                return false; // not consumed
            }
            //}}}
            // WEBVIEW CYCLE [free-grab] f(fs_webview_session_running) {{{
            if(fs_webview_session_running)
            {
                if( fs_webView_isGrabbed ) anim_free_fs_webView(caller);
                else                       anim_grab_fs_webView(caller);
            }
            return true; // consumed
            //}}}
        }
    };
    //}}}

    //* WEBVIEW SWAP */
    // swap_fs_webView_list_1_2 {{{
    private void swap_fs_webView_list_1_2()
    {
        // AS OF (160928) NO FUNCTIONALITIES RELY ON ORDERING
        int location1 = MWebView_pool.indexOf( fs_webView  );
        int location2 = MWebView_pool.indexOf( fs_webView2 );
        MWebView_pool.set(location1,           fs_webView2 );
        MWebView_pool.set(location2,           fs_webView  );
    }
        //}}}
    // swap_fs_webView_list_2_3 {{{
    private void swap_fs_webView_list_2_3()
    {
        // AS OF (160928) NO FUNCTIONALITIES RELY ON ORDERING
        int location2 = MWebView_pool.indexOf( fs_webView2 );
        int location3 = MWebView_pool.indexOf( fs_webView3 );
        MWebView_pool.set(location2,           fs_webView3 );
        MWebView_pool.set(location3,           fs_webView2 );
    }
        //}}}
    // swap_fs_webView_list_1_3 {{{
    private void swap_fs_webView_list_1_3()
    {
        // AS OF (160928) NO FUNCTIONALITIES RELY ON ORDERING
        int location1 = MWebView_pool.indexOf( fs_webView  );
        int location3 = MWebView_pool.indexOf( fs_webView3 );
        MWebView_pool.set(location1,           fs_webView3 );
        MWebView_pool.set(location3,           fs_webView  );
    }
        //}}}
    // fs_wswap_1_2_CB {{{
    private void fs_wswap_1_2_CB()
    {
        String caller = "fs_wswap_1_2_CB";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);

        if((fs_webView == null) || (fs_webView2 == null))
            return;

        // POOL
        swap_fs_webView_list_1_2();

        // WEBVIEW
        MWebView wv = fs_webView;
        fs_webView  = fs_webView2;
        fs_webView2 = wv;

        // MARKERS
        wvTools.marker_map_swap_slice_1_2();

/* // {{{
        // WTITLE
        NpButton nb = fs_wtitle;
        fs_wtitle   = fs_wtitle2;
        fs_wtitle2  = nb;

        // SCROLLBAR
        nb          = wvTools.sb;
        wvTools.sb  = wvTools.sb2;
        wvTools.sb2 = nb;
*/ // }}}
        // MATCH LEFT MOST WEBVIEW
        //adjust_WEBVIEW_LAYOUT(Settings.SCREEN_SPLIT, caller);
        shedule_update_WEBVIEW_TITLES_AND_COLORS(caller);
    }
    //}}}
    // fs_wswap_2_3_CB {{{
    private void fs_wswap_2_3_CB()
    {
        String caller = "fs_wswap_2_3_CB";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);

        if((fs_webView2 == null) || (fs_webView3 == null))
            return;

        // POOL
        swap_fs_webView_list_2_3();

        // WEBVIEW
        MWebView wv = fs_webView2;
        fs_webView2 = fs_webView3;
        fs_webView3 = wv;

        // MARKERS
        wvTools.marker_map_swap_slice_2_3();

/* // {{{
        // WTITLE
        NpButton nb = fs_wtitle2;
        fs_wtitle2  = fs_wtitle3;
        fs_wtitle3  = nb;

        // SCROLLBAR
        nb          = wvTools.sb2;
        wvTools.sb2 = wvTools.sb3;
        wvTools.sb3 = nb;
*/ // }}}
        // MATCH LEFT MOST WEBVIEW
        //adjust_WEBVIEW_LAYOUT(Settings.SCREEN_SPLIT, caller);
        shedule_update_WEBVIEW_TITLES_AND_COLORS(caller);
    }
    //}}}
    // fs_wswap_1_3_CB {{{
    private void fs_wswap_1_3_CB()
    {
        String caller = "fs_wswap_1_3_CB";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);

        if((fs_webView == null) || (fs_webView3 == null))
            return;

        // POOL
        swap_fs_webView_list_1_3();

        // WEBVIEW
        MWebView wv = fs_webView;
        fs_webView  = fs_webView3;
        fs_webView3 = wv;

        // MARKERS
        wvTools.marker_map_swap_slice_1_3();

/* // {{{
        // WTITLE
        NpButton nb = fs_wtitle;
        fs_wtitle   = fs_wtitle3;
        fs_wtitle3  = nb;

        // SCROLLBAR
        nb          = wvTools.sb;
        wvTools.sb  = wvTools.sb3;
        wvTools.sb3 = nb;
*/ // }}}
        // MATCH LEFT MOST WEBVIEW
        //adjust_WEBVIEW_LAYOUT(Settings.SCREEN_SPLIT, caller);
        shedule_update_WEBVIEW_TITLES_AND_COLORS(caller);
    }
    //}}}
    // fs_wswapL_CB {{{
    private void fs_wswapL_CB()
    {
        String caller = "fs_wswapL_CB";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);
        wvTools.marker_set_someone_has_called_hide_marks(caller);

        // swap [fs_webView1] [fs_webView2] {{{
        fs_wswap_1_2_CB();
        fs_webViewL = fs_webView ; // has been swapped
        fs_webViewC = fs_webView2; // has been swapped

        //}}}
        // REORDER [fs_webView] [fs_webView2] [fs_webView3] {{{
        if( fs_webView_isGrabbed )    anim_grab_fs_webView(caller);
        else                          anim_free_fs_webView(caller);

        //}}}
        // REORDER WEBGROUP {{{
        String         url = fs_webView.get_url_requested();
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "SHIFTING  LEFT url=["+url+"]");

        String profile_key = null;
        if((profile_key = WebGroup.get_profile_key(Settings.Working_profile, url, caller)) != null)
        {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "SHIFTING  LEFT WEBGROUP ["+profile_key+"]");
                WebGroup.shift_profile_url_left (profile_key, url, caller);
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, WebGroup.Dump_profile_key( profile_key ));
        }
        //}}}
    }
    //}}}
    // fs_wswapR_CB {{{
    private void fs_wswapR_CB()
    {
        String caller = "fs_wswapR_CB";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);
        wvTools.marker_set_someone_has_called_hide_marks(caller);

        // swap [fs_webView2] [fs_webView3] {{{
        fs_wswap_2_3_CB();
        fs_webViewC = fs_webView2; // has been swapped
        fs_webViewR = fs_webView3; // has been swapped

        //}}}
        // REORDER [fs_webView] [fs_webView2] [fs_webView3] {{{
        if( fs_webView_isGrabbed )    anim_grab_fs_webView(caller);
        else                          anim_free_fs_webView(caller);

        //}}}
        // REORDER WEBGROUP {{{
        String         url = fs_webView3.get_url_requested();
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "SHIFTING RIGHT url=["+url+"]");

        String profile_key = null;
        if((profile_key = WebGroup.get_profile_key(Settings.Working_profile, url, caller)) != null)
        {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "SHIFTING RIGHT WEBGROUP ["+profile_key+"]");
                WebGroup.shift_profile_url_right(profile_key, url, caller);
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, WebGroup.Dump_profile_key( profile_key ));
        }
        //}}}
    }
    //}}}
    // fs_search_can_swap_webview {{{
    public boolean fs_search_can_swap_webview()
    {
        boolean result
            =    (get_fs_webView_count() > 1)
            &&   (touched_wvContainer_border || (gesture_down_SomeView_atXY == wvTools.fs_search))
            &&    is_fs_webView_expanded() ;

//*FS_SEARCH*/Settings.MOC(TAG_FS_SEARCH, "fs_search_can_swap_webview ...return "+result);
//*FS_SEARCH*/Settings.MOM(TAG_FS_SEARCH, " + get_fs_webView_count()......................=["+ get_fs_webView_count()                    +"]");
//*FS_SEARCH*/Settings.MOM(TAG_FS_SEARCH, " + touched_wvContainer_border..................=["+ touched_wvContainer_border                +"]");
//*FS_SEARCH*/Settings.MOM(TAG_FS_SEARCH, " + gesture_down_SomeView_atXY..................=["+ get_view_name(gesture_down_SomeView_atXY) +"]");
//*FS_SEARCH*/Settings.MOM(TAG_FS_SEARCH, " + is_fs_webView_expanded()....................=["+ is_fs_webView_expanded()                  +"]");
        return result;
    }
//}}}
    // fs_search_can_select_webview_on_ACTION_UP {{{
    public boolean fs_search_can_select_webview_on_ACTION_UP()
    {
        boolean result
            =     is_fs_webView_expanded()
            &&         (gesture_down_SomeView_atXY == wvTools.fs_search)     // TOUCHED [fs_search]
            &&          wvTools.was_fs_search_in_a_rest_zone_on_ACTION_DOWN; // WHILE IT WAS (IN A REST ZONE)

//*FS_SEARCH*/Settings.MOC(TAG_FS_SEARCH, "fs_search_can_select_webview_on_ACTION_UP ...return "+result);
//*FS_SEARCH*/Settings.MOM(TAG_FS_SEARCH, " + is_fs_webView_expanded()....................=["+ is_fs_webView_expanded()                    +"]");
//*FS_SEARCH*/Settings.MOM(TAG_FS_SEARCH, " + gesture_down_SomeView_atXY..................=["+ get_view_name(gesture_down_SomeView_atXY)   +"]");
//*FS_SEARCH*/Settings.MOM(TAG_FS_SEARCH, " + was_fs_search_in_a_rest_zone_on_ACTION_DOWN.=["+ wvTools.was_fs_search_in_a_rest_zone_on_ACTION_DOWN +"]");
        return result;
    }
//}}}
    // fs_search_can_select_webview_on_ACTION_MOVE {{{
    public boolean fs_search_can_select_webview_on_ACTION_MOVE()
    {
        boolean result
            =    is_fs_webView_expanded()
            &&   wvTools.is_fs_search_np_flagged_to_webview_swap_side()       // STARTED
            &&   touched_wvContainer_border                                   // [touched_wvContainer_border]
            ||   (     (gesture_down_SomeView_atXY == wvTools.fs_search)      // OR [fs_search]
                    && !wvTools.was_fs_search_in_a_rest_zone_on_ACTION_DOWN); // .. WHILE IT WAS (NOT IN A REST ZONE)

//*FS_SEARCH*/Settings.MOC(TAG_FS_SEARCH, "fs_search_can_select_webview_on_ACTION_MOVE ...return "+result);
//*FS_SEARCH*/Settings.MOM(TAG_FS_SEARCH, " + is_fs_webView_expanded().......................=["+ is_fs_webView_expanded()                               +"]");
//*FS_SEARCH*/Settings.MOM(TAG_FS_SEARCH, " + is_fs_search_np_flagged_to_webview_swap_side().=["+ wvTools.is_fs_search_np_flagged_to_webview_swap_side() +"]");
//*FS_SEARCH*/Settings.MOM(TAG_FS_SEARCH, " + touched_wvContainer_border.....................=["+ touched_wvContainer_border                             +"]");
//*FS_SEARCH*/Settings.MOM(TAG_FS_SEARCH, " + gesture_down_SomeView_atXY.....................=["+ get_view_name(gesture_down_SomeView_atXY)              +"]");
//*FS_SEARCH*/Settings.MOM(TAG_FS_SEARCH, " + was_fs_search_in_a_rest_zone_on_ACTION_DOWN....=["+ wvTools.was_fs_search_in_a_rest_zone_on_ACTION_DOWN    +"]");
        return result;
    }
//}}}
//    // get_WebView_right_of {{{
//    private MWebView get_WebView_right_of(MWebView wv_from)
//    {
//        boolean found_wv_from = false;
//        MWebView wv = null;
//        int i;
//        for(i=0; i < MWebView_pool.size(); ++i)
//        {
//            wv    = MWebView_pool.get(i);
//            if(found_wv_from) break;
//            if(wv == wv_from) found_wv_from = true;
//            wv    =  null;
//        }
//
//        if(wv != null) {
//            wv.raise_calls_count();
//            if(M||D) Settings.MON(TAG_WEBVIEW, "get_WebView_right_of(wv_from) ...return ["+wv+"]");
//        }
//        else {
//            if(M||D) Settings.MON(TAG_WEBVIEW, "get_WebView_right_of(wv_from) ...return [null]");
//        }
//
//        return wv;
//    }
//    //}}}
//    // get_WebView_oldest {{{
//    private MWebView get_WebView_oldest()
//    {
//        MWebView oldest = null;
//        int i;
//        for(i=0; i < MWebView_pool.size(); ++i)
//        {
//            MWebView wv = MWebView_pool.get(i);
//            if((oldest == null) || (wv.get_calls_count() < oldest.get_calls_count()))
//                oldest = wv;
//        }
//
//        if(oldest != null) {
//            oldest.raise_calls_count();
//            if(M||D) Settings.MON(TAG_WEBVIEW, "get_WebView_oldest() ...return ["+oldest+"]");
//        }
//        else {
//            if(M||D) Settings.MON(TAG_WEBVIEW, "get_WebView_oldest() ...return [null]");
//        }
//
//        return oldest;
//    }
//    //}}}

    //* WEBVIEW GRAB-FREE */
    // re_anim_grab_fs_webView {{{
    private void re_anim_grab_fs_webView(String caller)
    {
        caller += "->re_anim_grab_fs_webView";
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "XXX _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _re_anim_grab_fs_webView");

        if(fs_webView == null) return;   // got life-cycle-mixed-up-early-call from GestureDetector.onFling

        fs_tool_hide(caller);
        handler.re_postDelayed( hr_re_anim_grab_fs_webView, WEBVIEW_LAYOUT_DELAY);
    }

    private final Runnable hr_re_anim_grab_fs_webView = new Runnable()
    {
        @Override public void run() {
            String caller = "hr_re_anim_grab_fs_webView";
            instant_free_fs_webView();
            anim_grab_fs_webView(caller);
        }
    };
    //}}}
    // re_anim_free_fs_webView {{{
    private void re_anim_free_fs_webView(String caller)
    {
        caller += "->re_anim_free_fs_webView";
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "XXX _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _re_anim_free_fs_webView");

        if(fs_webView == null) return;   // got life-cycle-mixed-up-early-call from GestureDetector.onFling

        fs_tool_hide(caller);
        handler.re_postDelayed( hr_re_anim_free_fs_webView, WEBVIEW_LAYOUT_DELAY);
    }

    private final Runnable hr_re_anim_free_fs_webView = new Runnable()
    {
        @Override public void run() {
            String caller = "hr_re_anim_free_fs_webView";
        //  instant_free_fs_webView();
            anim_free_fs_webView(caller);
        }
    };
    //}}}

    //* WEBVIEW ANIMATION */
    // {{{
    // STATE {{{
    public  boolean fs_webView_isGrabbed      = false;
    //}}}
    // PROPERTIES {{{
    private static final int   WEBVIEW_ANIMATION_TO_SYNC_VISIBILITY_DELAY       =  50;
    private static final int   WEBVIEW_SYNC_VISIBILITY_TO_MAGNIFY_NP_HIDE_DELAY =  200;
    private static final int   WEBVIEW_LAYOUT_DELAY                             =  300;
//  private static final int   FS_BUTTON_EXPAND_QUICK_DURATION                  =   50;
    private static final int   FS_BUTTON_EXPAND_SLOW_DURATION                   =  300;

    private static final float WTITLE_FREE_A           =  0f;
    private static final float WTITLE_GRAB_A           =  1f;

    private static final float WV_TOOL_FREE_A           =  0f;
    private static final float WV_TOOL_GRAB_A           =  1f;

    //....................................  X ROTATION
    private static final float WTITLE_FREE_RX          = 10f;
    private static final float WTITLE_GRAB_RX          =  0f;

    //....................................  Y ROTATION
    private static final float WV_TOOL_FREE_RY         =-10f;
    private static final float WBOOKS_FREE_RY          =-10f;
    private static final float WSWAP__FREE_RY          = 70f;
    private static final float WV_TOOL_GRAB_RY          =  0f;
    private static final float WBOOKS_GRAB_RY          =  0f;
    private static final float WSWAP__GRAB_RY          =  0f;

    //.................................... SCALE
    private static final float WTITLE_FREE_SX          =.55f;
    private static final float WTITLE_FREE_SY          =.55f;
    private static final float WSWAP__FREE_SX          =.55f;
    private static final float WSWAP__FREE_SY          =.55f;
    private static final float WTITLE_GRAB_SX          =  1f;
    private static final float WTITLE_GRAB_SY          =  1f;
    private static final float WSWAP__GRAB_SX          =  1f;
    private static final float WSWAP__GRAB_SY          =  1f;

    private static final float WTITLE_FREE_Z           =  8f;
    private static final float WTITLE_GRAB_Z           = 16f;

    private static final float WV_TOOL_FREE_Z           =  8f;
    private static final float WV_TOOL_GRAB_Z           = 16f;

    private static final float WSEARCH_SET_Z           = 17f;
    // }}}
    // CHOREOGRAPHY TIMING {{{

    private static final int WV_ANIM_STEP_BASE = 100;
    private static final int WV_ANIM_STEP_PLUS = (int)(WV_ANIM_STEP_BASE * 1.5);
    private static final int WV_ANIM_STEP_HUGE = (int)(WV_ANIM_STEP_BASE * 3.0);

    // GRAB CLAMPS
    // 1 A = alpha          ... unhide
    // 2 S = scale          ... unfold
    // 3 Z = elevation      ... unpark
    // 4 T = translation    ... deploy
    // 5 R = rotation       ... apply clamps
    // 6 END pause          ... terminate animation
    private static final int WEBVIEW_GRAB_A_DELAY =                                                   0; private static final int WEBVIEW_GRAB_A_DURATION = WV_ANIM_STEP_BASE;
    private static final int WEBVIEW_GRAB_S_DELAY = WEBVIEW_GRAB_A_DELAY + WEBVIEW_GRAB_A_DURATION /  2; private static final int WEBVIEW_GRAB_S_DURATION = WV_ANIM_STEP_PLUS;
    private static final int WEBVIEW_GRAB_Z_DELAY = WEBVIEW_GRAB_S_DELAY + WEBVIEW_GRAB_S_DURATION /  2; private static final int WEBVIEW_GRAB_Z_DURATION = WV_ANIM_STEP_BASE;
    private static final int WEBVIEW_GRAB_T_DELAY = WEBVIEW_GRAB_Z_DELAY + WEBVIEW_GRAB_Z_DURATION /  5; private static final int WEBVIEW_GRAB_T_DURATION = WV_ANIM_STEP_PLUS;
    private static final int WEBVIEW_GRAB_R_DELAY = WEBVIEW_GRAB_T_DELAY + WEBVIEW_GRAB_T_DURATION /  2; private static final int WEBVIEW_GRAB_R_DURATION = WV_ANIM_STEP_HUGE;
    private static final int WEBVIEW_GRAB_E_DELAY = WEBVIEW_GRAB_R_DELAY + WEBVIEW_GRAB_R_DURATION + 10;

    // FREE CLAMPS
    // 1 R = rotation       ... free clamps
    // 2 T = translation    ... retract
    // 3 Z = elevation      ... park
    // 4 S = scale          ... fold
    // 5 A = alpha          ... hide
    // 6 END pause          ... terminate animation
    private static final int WEBVIEW_FREE_R_DELAY =                                                   0; private static final int WEBVIEW_FREE_R_DURATION = WV_ANIM_STEP_HUGE;
    private static final int WEBVIEW_FREE_T_DELAY = WEBVIEW_FREE_R_DELAY + WEBVIEW_FREE_R_DURATION /  2; private static final int WEBVIEW_FREE_T_DURATION = WV_ANIM_STEP_PLUS;
    private static final int WEBVIEW_FREE_Z_DELAY = WEBVIEW_FREE_T_DELAY + WEBVIEW_FREE_T_DURATION /  5; private static final int WEBVIEW_FREE_Z_DURATION = WV_ANIM_STEP_BASE;
    private static final int WEBVIEW_FREE_S_DELAY = WEBVIEW_FREE_Z_DELAY + WEBVIEW_FREE_Z_DURATION /  2; private static final int WEBVIEW_FREE_S_DURATION = WV_ANIM_STEP_PLUS;
    private static final int WEBVIEW_FREE_A_DELAY = WEBVIEW_FREE_S_DELAY + WEBVIEW_FREE_S_DURATION /  5; private static final int WEBVIEW_FREE_A_DURATION = WV_ANIM_STEP_BASE;
    private static final int WEBVIEW_FREE_E_DELAY = WEBVIEW_FREE_A_DELAY + WEBVIEW_FREE_A_DURATION + 10;

    // }}}
    // anim_grab_fs_webView {{{

    public  void anim_grab_fs_webView(String caller)
    {
        caller += "->anim_grab_fs_webView";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);

        //instant_free_fs_webView();
        if(fs_wtitle == null) {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "(fs_wtitle == null)");
        }

        fs_webview_animation_before(caller);

        fs_tool_show(caller);
        handler.re_postDelayed(hr_fs_webView_GRAB_A, WEBVIEW_GRAB_A_DELAY ); // Alpha
        handler.re_postDelayed(hr_fs_webView_GRAB_S, WEBVIEW_GRAB_S_DELAY ); // Scale
        handler.re_postDelayed(hr_fs_webView_GRAB_Z, WEBVIEW_GRAB_Z_DELAY ); // Z-Elevation
        handler.re_postDelayed(hr_fs_webView_GRAB_R, WEBVIEW_GRAB_R_DELAY ); // Rotation
        handler.re_postDelayed(hr_fs_webView_GRAB_T, WEBVIEW_GRAB_T_DELAY ); // Translation
        handler.re_postDelayed(hr_fs_webView_GRAB_E, WEBVIEW_GRAB_E_DELAY ); // End
    }
     //}}}
    // anim_free_fs_webView {{{
    public  void anim_free_fs_webView(String caller)
    {
        caller += "->anim_free_fs_webView";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);

        //instant_grab_fs_webView();
        if(fs_wtitle == null) {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, "(fs_wtitle == null)");
        }

        fs_webview_animation_before(caller);

        handler.re_postDelayed(hr_fs_webView_FREE_T, WEBVIEW_FREE_T_DELAY ); // Translation
        handler.re_postDelayed(hr_fs_webView_FREE_R, WEBVIEW_FREE_R_DELAY ); // Rotation
        handler.re_postDelayed(hr_fs_webView_FREE_Z, WEBVIEW_FREE_Z_DELAY ); // Z-Elevation
        handler.re_postDelayed(hr_fs_webView_FREE_S, WEBVIEW_FREE_S_DELAY ); // Scale
        handler.re_postDelayed(hr_fs_webView_FREE_A, WEBVIEW_FREE_A_DELAY ); // Alpha
        handler.re_postDelayed(hr_fs_webView_FREE_E, WEBVIEW_FREE_E_DELAY ); // End
    }
    //}}}
    // instant_free_fs_webView {{{
    private void instant_free_fs_webView()
    {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, "instant_free_fs_webView", "XXX _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ instant_free_fs_webView");
        fs_webView_FREE_A( false );
        fs_webView_FREE_Z( false );
        fs_webView_FREE_T( false );
        fs_webView_FREE_R( false );
        fs_webView_FREE_S( false );
    }
    //}}}
    // instant_grab_fs_webView {{{
    private void instant_grab_fs_webView()
    {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, "instant_grab_fs_webView", "XXX _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ instant_grab_fs_webView");
        fs_webView_GRAB_A( false );
        fs_webView_GRAB_Z( false );
        fs_webView_GRAB_T( false );
        fs_webView_GRAB_R( false );
        fs_webView_GRAB_S( false );
    }
     //}}}
    // some_missing_animated_view {{{
    private boolean some_missing_animated_view()
    {
/*
/some_missing_animated_view\|fs_bookmark\|fs_browse\|fs_goBack\|fs_goForward\|fs_webView\|fs_wswapL\|fs_wswapR\|fs_wtitle
/some_missing_animated_view\|fs_webView2\|fs_webView3\|fs_wtitle2\|fs_wtitle3
/AnimatorSet.Builder builder
            //...play.......(ObjectAnimator.ofFloat(fs_XXXXXX   , View.XXXXXXXXXX, // WTITLEXXXXX_XX,//  WTITLE_XXXX_XX))
*/
        boolean result
        =      (fs_bookmark  == null)
        ||     (fs_browse    == null)
        ||     (fs_goBack    == null)
        ||     (fs_goForward == null)
        ||     (fs_webView   == null)
        //     (fs_webView2  == null)
        //     (fs_webView3  == null)
        ||     (fs_wswapL    == null)
        ||     (fs_wswapR    == null)
        ||     (fs_wtitle    == null)
        //     (fs_wtitle2   == null)
        //     (fs_wtitle3   == null)
        ;

//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "some_missing_animated_view: ...return "+ result);
        return result;
    }
    //}}}
    //* 1 ANIM BEFORE
    // fs_webview_animation_before {{{
    private static boolean fs_webview_animation_running = false;

    private void fs_webview_animation_before(String caller)
    {
        caller += "->fs_webview_animation_before";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);

        // STARTING: [TRANSIENT ANIMATION CHANGES]
        fs_webview_animation_running = true;

        wvTools.hide     (caller);

        // FORCE WV_TOOL_MARKS_LOCK .. (to be reinstated by fs_webview_animation_before)
        //wvTools.property_set(WVTools.WV_TOOL_MARKS_LOCK, true);
        // replaced by calls to [is_anim_running]

        fs_webView_show(caller);
    }
    //}}}
    // is_anim_running {{{
    public boolean is_anim_running()
    {
        return  fs_webview_animation_running;
    }
    //}}}
    //* 2 ANIM ALPHA
    // fs_webView_FREE_A {{{
    private final Runnable hr_fs_webView_FREE_A  = new Runnable() {
        @Override public void run() {
            fs_webView_FREE_A( ANIM_SUPPORTED );
        }
    };

    private void fs_webView_FREE_A(boolean with_anim)
    {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, "fs_webView_FREE_A", "XXX fs_webView_FREE_A"+ (with_anim ? " with_anim" : ""));
        if( some_missing_animated_view() ) return;

        if( with_anim ) {
            AnimatorSet set = new AnimatorSet();

            set.setDuration( WEBVIEW_FREE_A_DURATION );

            set.setInterpolator(new DecelerateInterpolator());

            //...play.......(ObjectAnimator.ofFloat(fs_XXXXXX   , View.XXXXXXXXXX,/* WTITLEXXXXX_XX,*/  WTITLE_XXXX_XX))
            AnimatorSet.Builder builder = set
                .play       (ObjectAnimator.ofFloat(fs_wtitle   , View.ALPHA     , /* WTITLE_GRAB_A,*/  WTITLE_FREE_A ))
                .with       (ObjectAnimator.ofFloat(fs_wswapL   , View.ALPHA     , /*WV_TOOL_GRAB_A,*/ WV_TOOL_FREE_A ))
                .with       (ObjectAnimator.ofFloat(fs_wswapR   , View.ALPHA     , /*WV_TOOL_GRAB_A,*/ WV_TOOL_FREE_A ))
                .with       (ObjectAnimator.ofFloat(fs_browse   , View.ALPHA     , /*WV_TOOL_GRAB_A,*/ WV_TOOL_FREE_A ))
                .with       (ObjectAnimator.ofFloat(fs_bookmark , View.ALPHA     , /*WV_TOOL_GRAB_A,*/ WV_TOOL_FREE_A ))
                .with       (ObjectAnimator.ofFloat(fs_goBack   , View.ALPHA     , /*WV_TOOL_GRAB_A,*/ WV_TOOL_FREE_A ))
                .with       (ObjectAnimator.ofFloat(fs_goForward, View.ALPHA     , /*WV_TOOL_GRAB_A,*/ WV_TOOL_FREE_A ))
                ;

            if( is_fs_wtitle2_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle2  , View.ALPHA     , /* WTITLE_GRAB_A,*/   WTITLE_FREE_A ));
            }
            if( is_fs_wtitle3_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle3  , View.ALPHA     , /* WTITLE_GRAB_A,*/   WTITLE_FREE_A ));
            }

            set.start();
        }
        else {
            fs_wtitle       .setAlpha(  WTITLE_FREE_A );
            fs_wswapL       .setAlpha( WV_TOOL_FREE_A );
            fs_wswapR       .setAlpha( WV_TOOL_FREE_A );
            fs_browse       .setAlpha( WV_TOOL_FREE_A );
            fs_bookmark     .setAlpha( WV_TOOL_FREE_A );
            fs_goBack       .setAlpha( WV_TOOL_FREE_A );
            fs_goForward    .setAlpha( WV_TOOL_FREE_A );

            if( is_fs_wtitle2_in_screen() ) {
                fs_wtitle2  .setAlpha(  WTITLE_FREE_A );
            }
            if( is_fs_wtitle3_in_screen() ) {
                fs_wtitle3  .setAlpha(  WTITLE_FREE_A );
            }
        }
    }
    // }}}
    // fs_webView_GRAB_A {{{
    private final Runnable hr_fs_webView_GRAB_A  = new Runnable() {
        @Override public void run() {
            fs_webView_GRAB_A( ANIM_SUPPORTED );
        }
    };

    private void fs_webView_GRAB_A(boolean with_anim)
    {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, "fs_webView_GRAB_A", "XXX fs_webView_GRAB_A"+ (with_anim ? " with_anim" : ""));
        if( some_missing_animated_view() ) return;

        if( with_anim ) {
            AnimatorSet set = new AnimatorSet();

            set.setDuration( WEBVIEW_GRAB_A_DURATION );

            set.setInterpolator(new DecelerateInterpolator());

            //...play.......(ObjectAnimator.ofFloat(fs_XXXXXX   , View.XXXXXXXXXX,/* WTITLEXXXXX_XX,*/  WTITLE_XXXX_XX))
            AnimatorSet.Builder builder = set
                .play       (ObjectAnimator.ofFloat(fs_wtitle   , View.ALPHA     ,/* WTITLE_FREE_A ,*/  WTITLE_GRAB_A ))
                .with       (ObjectAnimator.ofFloat(fs_wswapL   , View.ALPHA     ,/*WV_TOOL_FREE_A ,*/ WV_TOOL_GRAB_A ))
                .with       (ObjectAnimator.ofFloat(fs_wswapR   , View.ALPHA     ,/*WV_TOOL_FREE_A ,*/ WV_TOOL_GRAB_A ))
                .with       (ObjectAnimator.ofFloat(fs_browse   , View.ALPHA     ,/*WV_TOOL_FREE_A ,*/ WV_TOOL_GRAB_A ))
                .with       (ObjectAnimator.ofFloat(fs_bookmark , View.ALPHA     ,/*WV_TOOL_FREE_A ,*/ WV_TOOL_GRAB_A ))
                .with       (ObjectAnimator.ofFloat(fs_goBack   , View.ALPHA     ,/*WV_TOOL_FREE_A ,*/ WV_TOOL_GRAB_A ))
                .with       (ObjectAnimator.ofFloat(fs_goForward, View.ALPHA     ,/*WV_TOOL_FREE_A ,*/ WV_TOOL_GRAB_A ))
                ;

            if( is_fs_wtitle2_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle2  , View.ALPHA     ,/* WTITLE_FREE_A ,*/  WTITLE_GRAB_A ));
            }
            if( is_fs_wtitle3_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle3  , View.ALPHA     ,/* WTITLE_FREE_A ,*/  WTITLE_GRAB_A ));
            }

            set.start();
        }
        else {
            fs_wtitle       .setAlpha( WTITLE_GRAB_A );
            fs_wswapL       .setAlpha( WV_TOOL_GRAB_A );
            fs_wswapR       .setAlpha( WV_TOOL_GRAB_A );
            fs_browse       .setAlpha( WV_TOOL_GRAB_A );
            fs_bookmark     .setAlpha( WV_TOOL_GRAB_A );
            fs_goBack       .setAlpha( WV_TOOL_GRAB_A );
            fs_goForward    .setAlpha( WV_TOOL_GRAB_A );

            if( is_fs_wtitle2_in_screen() ) {
                fs_wtitle2  .setAlpha( WTITLE_GRAB_A );
            }
            if( is_fs_wtitle3_in_screen() ) {
                fs_wtitle3  .setAlpha( WTITLE_GRAB_A );
            }
        }
    }
//}}}
//* 3 ANIM ELEVATION
// fs_webView_FREE_Z {{{
private final Runnable hr_fs_webView_FREE_Z  = new Runnable() {
    @Override public void run() {
        fs_webView_FREE_Z( ANIM_SUPPORTED );
    }
};

    private void fs_webView_FREE_Z(boolean with_anim)
    {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, "fs_webView_FREE_Z", "XXX fs_webView_FREE_Z"+ (with_anim ? " with_anim" : ""));
        if( some_missing_animated_view() ) return;

        if(Build.VERSION.SDK_INT <  Build.VERSION_CODES.LOLLIPOP)
            return;

        if( with_anim ) {
            AnimatorSet set = new AnimatorSet();

            set.setDuration( WEBVIEW_FREE_Z_DURATION );

            set.setInterpolator(new DecelerateInterpolator());

            AnimatorSet.Builder builder = set
                .play       (ObjectAnimator.ofFloat(fs_wtitle   , View.Z         ,/* WTITLE_GRAB_Z ,*/  WTITLE_FREE_Z ))
                .with       (ObjectAnimator.ofFloat(fs_wswapL   , View.Z         ,/*WV_TOOL_GRAB_Z ,*/ WV_TOOL_FREE_Z ))
                .with       (ObjectAnimator.ofFloat(fs_wswapR   , View.Z         ,/*WV_TOOL_GRAB_Z ,*/ WV_TOOL_FREE_Z ))
                .with       (ObjectAnimator.ofFloat(fs_browse   , View.Z         ,/*WV_TOOL_GRAB_Z ,*/ WV_TOOL_FREE_Z ))
                .with       (ObjectAnimator.ofFloat(fs_bookmark , View.Z         ,/*WV_TOOL_GRAB_Z ,*/ WV_TOOL_FREE_Z ))
                .with       (ObjectAnimator.ofFloat(fs_goBack   , View.Z         ,/*WV_TOOL_GRAB_Z ,*/ WV_TOOL_FREE_Z ))
                .with       (ObjectAnimator.ofFloat(fs_goForward, View.Z         ,/*WV_TOOL_GRAB_Z ,*/ WV_TOOL_FREE_Z ))
                ;

            if( is_fs_wtitle2_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle2  , View.Z         ,/* WTITLE_GRAB_Z ,*/  WTITLE_FREE_Z ));
            }
            if( is_fs_wtitle3_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle3  , View.Z         ,/* WTITLE_GRAB_Z ,*/  WTITLE_FREE_Z ));
            }

            set.start();
        }
        else {
            fs_wtitle       .setZ( WTITLE_FREE_Z );
            fs_wswapL       .setZ( WV_TOOL_FREE_Z );
            fs_wswapR       .setZ( WV_TOOL_FREE_Z );
            fs_browse       .setZ( WV_TOOL_FREE_Z );
            fs_bookmark     .setZ( WV_TOOL_FREE_Z );
            fs_goBack       .setZ( WV_TOOL_FREE_Z );
            fs_goForward    .setZ( WV_TOOL_FREE_Z );

            if( is_fs_wtitle2_in_screen() ) {
                fs_wtitle2  .setZ( WTITLE_FREE_Z );
            }
            if( is_fs_wtitle3_in_screen() ) {
                fs_wtitle3  .setZ( WTITLE_FREE_Z );
            }
        }
    }
//}}}
// fs_webView_GRAB_Z {{{
private final Runnable hr_fs_webView_GRAB_Z  = new Runnable() {
    @Override public void run() {
        fs_webView_GRAB_Z( ANIM_SUPPORTED );
    }
};

    private void fs_webView_GRAB_Z(boolean with_anim)
    {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, "fs_webView_GRAB_Z", "XXX fs_webView_GRAB_Z"+ (with_anim ? " with_anim" : ""));
        if( some_missing_animated_view() ) return;

        if(Build.VERSION.SDK_INT <  Build.VERSION_CODES.LOLLIPOP)
            return;

        if( with_anim ) {
            AnimatorSet set = new AnimatorSet();

            set.setDuration( WEBVIEW_GRAB_Z_DURATION );

            set.setInterpolator(new DecelerateInterpolator());

            AnimatorSet.Builder builder = set
            //...play.......(ObjectAnimator.ofFloat(fs_XXXXXX   , View.XXXXXXXXXX,/* WTITLEXXXXX_XX,*/  WTITLE_XXXX_XX))
                .play       (ObjectAnimator.ofFloat(fs_wtitle   , View.Z         ,/* WTITLE_FREE_Z ,*/  WTITLE_GRAB_Z ))
                .with       (ObjectAnimator.ofFloat(fs_wswapL   , View.Z         ,/*WV_TOOL_FREE_Z ,*/ Settings.MAX_ELEVATION))//WV_TOOL_GRAB_Z))
                .with       (ObjectAnimator.ofFloat(fs_wswapR   , View.Z         ,/*WV_TOOL_FREE_Z ,*/ Settings.MAX_ELEVATION))//WV_TOOL_GRAB_Z))
                .with       (ObjectAnimator.ofFloat(fs_browse   , View.Z         ,/*WV_TOOL_FREE_Z ,*/ WV_TOOL_GRAB_Z ))
                .with       (ObjectAnimator.ofFloat(fs_bookmark , View.Z         ,/*WV_TOOL_FREE_Z ,*/ WV_TOOL_GRAB_Z ))
                .with       (ObjectAnimator.ofFloat(fs_goBack   , View.Z         ,/*WV_TOOL_FREE_Z ,*/ WV_TOOL_GRAB_Z ))
                .with       (ObjectAnimator.ofFloat(fs_goForward, View.Z         ,/*WV_TOOL_FREE_Z ,*/ WV_TOOL_GRAB_Z ))
                ;

            if( is_fs_wtitle2_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle2  , View.Z         ,/* WTITLE_FREE_Z ,*/  WTITLE_GRAB_Z ));
            }
            if( is_fs_wtitle3_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle3  , View.Z         ,/* WTITLE_FREE_Z ,*/  WTITLE_GRAB_Z ));
            }

            set.start();
        }
        else {
            fs_wtitle       .setZ( WTITLE_GRAB_Z );
            fs_wswapL       .setZ( Settings.MAX_ELEVATION);//WV_TOOL_GRAB_Z );
            fs_wswapR       .setZ( Settings.MAX_ELEVATION);//WV_TOOL_GRAB_Z );
            fs_browse       .setZ( WV_TOOL_GRAB_Z );
            fs_bookmark     .setZ( WV_TOOL_GRAB_Z );
            fs_goBack       .setZ( WV_TOOL_GRAB_Z );
            fs_goForward    .setZ( WV_TOOL_GRAB_Z );

            if( is_fs_wtitle2_in_screen() ) {
                fs_wtitle2  .setZ( WTITLE_GRAB_Z );
            }
            if( is_fs_wtitle3_in_screen() ) {
                fs_wtitle3  .setZ( WTITLE_GRAB_Z );
            }
        }
    }
    //}}}
    //* 4 ANIM TRANSLATION
    // fs_webView_FREE_T {{{
    private final Runnable hr_fs_webView_FREE_T  = new Runnable() {
        @Override public void run() {
            fs_webView_FREE_T( ANIM_SUPPORTED );
        }
    };

    private void fs_webView_FREE_T(boolean with_anim)
    {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, "fs_webView_FREE_T", "XXX fs_webView_FREE_T"+ (with_anim ? " with_anim" : ""));
        if( some_missing_animated_view() ) return;

        // PROPERTIES DIP DEPENDENT
        float free_y    =  fs_wtitle.getTop();
    //  float grab_y    =  free_y + Settings.WEBVIEW_GAP;
        float free_l    = -Settings.WEBVIEW_MARGIN;
    //  float grab_l    =  0;
        float free_L    =  Settings.WEBVIEW_MARGIN + Settings.WEBVIEW_WIDTH + Settings.WEBVIEW_GAP/2 - fs_wswapL.getWidth()/2;
    //  float grab_L    =  Settings.WEBVIEW_MARGIN + Settings.WEBVIEW_WIDTH + Settings.WEBVIEW_GAP/2 - fs_wswapL.getWidth()/2;
        float free_R    =  free_L + Settings.WEBVIEW_WIDTH + Settings.WEBVIEW_GAP;
    //  float grab_R    =  grab_L + Settings.WEBVIEW_WIDTH + Settings.WEBVIEW_GAP;
        //............................(WV-LEFT)->|............(WV-RIGHT)->|<-......(-WEBVIEW_OVER)
        int split_width = (Settings.SCREEN_W - Settings.WEBVIEW_MARGIN) / Settings.SCREEN_SPLIT;
        float free_m    =  Settings.WEBVIEW_MARGIN/2;
    //  float grab_m    =  Settings.WEBVIEW_MARGIN;
        float free_t    =  Settings.WEBVIEW_MARGIN/2;
    //  float grab_t    =2*Settings.WEBVIEW_MARGIN;

        if( with_anim ) {
            AnimatorSet set = new AnimatorSet();

            set.setDuration( WEBVIEW_FREE_T_DURATION );

            set.setInterpolator(new DecelerateInterpolator());

            AnimatorSet.Builder builder =
            //...play.......(ObjectAnimator.ofFloat(fs_XXXXXX   , View.XXXXXXXXXX,/* WTITLEXXXXX_XX,*/  WTITLE_XXXX_XX))
            set .play       (ObjectAnimator.ofFloat(fs_wtitle   , View.Y         ,/*        grab_y ,*/         free_y ))
                .with       (ObjectAnimator.ofFloat(fs_wswapL   , View.X         ,/*        grab_L ,*/         free_L ))
                .with       (ObjectAnimator.ofFloat(fs_wswapR   , View.X         ,/*        grab_R ,*/         free_R ))
                .with       (ObjectAnimator.ofFloat(fs_browse   , View.X         ,/*        grab_l ,*/         free_l ))
                .with       (ObjectAnimator.ofFloat(fs_bookmark , View.X         ,/*        grab_l ,*/         free_l ))
                .with       (ObjectAnimator.ofFloat(fs_goBack   , View.X         ,/*        grab_l ,*/         free_l ))
                .with       (ObjectAnimator.ofFloat(fs_goForward, View.X         ,/*        grab_l ,*/         free_l ))
                .with       (ObjectAnimator.ofFloat(fs_webView  , View.X         ,/*        grab_t ,*/         free_m ))
                .with       (ObjectAnimator.ofFloat(fs_webView  , View.Y         ,/*        grab_t ,*/         free_t ))
                ;

            if( is_fs_wtitle2_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle2  , View.Y         ,/*        grab_y ,*/         free_y ));
            }
            if( is_fs_wtitle3_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle3  , View.Y         ,/*        grab_y ,*/         free_y ));
            }

            if( is_fs_webView2_in_screen() ) {
                free_m += split_width;
                builder.with(ObjectAnimator.ofFloat(fs_webView2 , View.X         , /*       grab_m ,*/         free_m ));
                builder.with(ObjectAnimator.ofFloat(fs_webView2 , View.Y         , /*       grab_t ,*/         free_t ));
            }
            if( is_fs_webView3_in_screen() ) {
                free_m += split_width;
                builder.with(ObjectAnimator.ofFloat(fs_webView3 , View.X         , /*       grab_m ,*/         free_m ));
                builder.with(ObjectAnimator.ofFloat(fs_webView3 , View.Y         , /*       grab_t ,*/         free_t ));
            }

            set.start();
        }
        else {
            fs_wtitle   .setY( free_y );
            fs_wswapL   .setX( free_L );
            fs_wswapR   .setX( free_R );
            fs_browse   .setX( free_l );
            fs_bookmark .setX( free_l );
            fs_goBack   .setX( free_l );
            fs_goForward.setX( free_l );
            fs_webView  .setX( free_m );

            if( is_fs_webView2_in_screen() ) {
                free_m += split_width;
                fs_webView2 .setX( free_m );
                fs_wtitle2  .setY( free_y );
            }
            if( is_fs_webView3_in_screen() ) {
                free_m += split_width;
                fs_webView3 .setX( free_m );
                fs_wtitle3  .setY( free_y );
            }
        }
    }
    // }}}
    // fs_webView_GRAB_T {{{
    private final Runnable hr_fs_webView_GRAB_T  = new Runnable() {
        @Override public void run() {
            fs_webView_GRAB_T( ANIM_SUPPORTED );
        }
    };

    private void fs_webView_GRAB_T(boolean with_anim)
    {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, "fs_webView_GRAB_T", "XXX fs_webView_GRAB_T"+ (with_anim ? " with_anim" : ""));
        if( some_missing_animated_view() ) return;

        // PROPERTIES DIP DEPENDENT
        float free_y    =  fs_wtitle.getTop();
        float grab_y    =  free_y + Settings.WEBVIEW_GAP;
    //  float free_l    = -Settings.WEBVIEW_MARGIN;
        float grab_l    =  0;
    //  float free_L    =  Settings.WEBVIEW_MARGIN + Settings.WEBVIEW_WIDTH + Settings.WEBVIEW_GAP/2 - fs_wswapL.getWidth()/2;
        float grab_L    =  Settings.WEBVIEW_MARGIN + Settings.WEBVIEW_WIDTH + Settings.WEBVIEW_GAP/2 - fs_wswapL.getWidth()/2;
    //  float free_R    =  free_L + Settings.WEBVIEW_WIDTH + Settings.WEBVIEW_GAP;
        float grab_R    =  grab_L + Settings.WEBVIEW_WIDTH + Settings.WEBVIEW_GAP;
        //............................(WV-LEFT)->|............(WV-RIGHT)->|<-......(-WEBVIEW_OVER)
        int split_width = (Settings.SCREEN_W - Settings.WEBVIEW_MARGIN) / Settings.SCREEN_SPLIT;
    //  float free_m    =  Settings.WEBVIEW_MARGIN/2;
        float grab_m    =  Settings.WEBVIEW_MARGIN;
    //  float free_t    =  Settings.WEBVIEW_MARGIN/2;
        float grab_t    =2*Settings.WEBVIEW_MARGIN;

        if( with_anim ) {
            AnimatorSet set = new AnimatorSet();

            set.setDuration( WEBVIEW_GRAB_T_DURATION );

            set.setInterpolator(new DecelerateInterpolator());

            AnimatorSet.Builder builder =
            //...play.......(ObjectAnimator.ofFloat(fs_XXXXXX   , View.XXXXXXXXXX,/* WTITLEXXXXX_XX,*/  WTITLE_XXXX_XX))
            set .play       (ObjectAnimator.ofFloat(fs_wtitle   , View.Y         ,/*        free_y ,*/         grab_y ))
                .with       (ObjectAnimator.ofFloat(fs_wswapL   , View.X         ,/*        free_L ,*/         grab_L ))
                .with       (ObjectAnimator.ofFloat(fs_wswapR   , View.X         ,/*        free_R ,*/         grab_R ))
                .with       (ObjectAnimator.ofFloat(fs_browse   , View.X         ,/*        free_l ,*/         grab_l ))
                .with       (ObjectAnimator.ofFloat(fs_bookmark , View.X         ,/*        free_l ,*/         grab_l ))
                .with       (ObjectAnimator.ofFloat(fs_goBack   , View.X         ,/*        free_l ,*/         grab_l ))
                .with       (ObjectAnimator.ofFloat(fs_goForward, View.X         ,/*        free_l ,*/         grab_l ))
                .with       (ObjectAnimator.ofFloat(fs_webView  , View.X         ,/*        free_m ,*/         grab_m ))
                .with       (ObjectAnimator.ofFloat(fs_webView  , View.Y         ,/*        free_t ,*/         grab_t ))
                ;

            if( is_fs_wtitle2_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle2  , View.Y         ,/*        free_y ,*/         grab_y ));
            }
            if( is_fs_wtitle3_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle3  , View.Y         ,/*        free_y ,*/         grab_y ));
            }

            if( is_fs_webView2_in_screen() ) {
                grab_m += split_width;
                builder.with(ObjectAnimator.ofFloat(fs_webView2 , View.X         ,/*        free_t ,*/         grab_m ));
                builder.with(ObjectAnimator.ofFloat(fs_webView2 , View.Y         ,/*        free_m ,*/         grab_t ));
            }
            if( is_fs_webView3_in_screen() ) {
                grab_m += split_width;
                builder.with(ObjectAnimator.ofFloat(fs_webView3 , View.X         ,/*        free_t ,*/         grab_m ));
                builder.with(ObjectAnimator.ofFloat(fs_webView3 , View.Y         ,/*        free_m ,*/         grab_t ));
            }

            set.start();
        }
        else {
            fs_wtitle       .setY( grab_y );
            fs_wswapL       .setX( grab_L );
            fs_wswapR       .setX( grab_R );
            fs_browse       .setX( grab_l );
            fs_bookmark     .setX( grab_l );
            fs_goBack       .setX( grab_l );
            fs_goForward    .setX( grab_l );
            fs_webView      .setX( grab_m );

            if( is_fs_webView2_in_screen() ) {
                grab_m += split_width;
                fs_webView2 .setX( grab_m );
                fs_wtitle2  .setY( grab_y );
            }
            if( is_fs_webView3_in_screen() ) {
                grab_m += split_width;
                fs_webView3 .setX( grab_m );
                fs_wtitle3  .setY( grab_y );
            }
        }
    }
    // }}}
    //* 5 ANIM ROTATION
    // fs_webView_FREE_R {{{
    private final Runnable hr_fs_webView_FREE_R  = new Runnable() {
        @Override public void run() {
            fs_webView_FREE_R( ANIM_SUPPORTED );
        }
    };

    private void fs_webView_FREE_R(boolean with_anim)
    {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, "fs_webView_FREE_R", "XXX fs_webView_FREE_R"+ (with_anim ? " with_anim" : ""));
        if( some_missing_animated_view() ) return;

        if( with_anim ) {
            AnimatorSet set = new AnimatorSet();

            set.setDuration( WEBVIEW_FREE_R_DURATION );

            set.setInterpolator(new DecelerateInterpolator());

            //...play.......(ObjectAnimator.ofFloat(fs_XXXXXX   , View.XXXXXXXXXX, /* WTITLEXXXXX_XX,*/  WTITLE_XXXX_XX))
            AnimatorSet.Builder builder =
            set .play       (ObjectAnimator.ofFloat(fs_wtitle   , View.ROTATION_X, /* WTITLE_GRAB_RX,*/  WTITLE_FREE_RX))
                .with       (ObjectAnimator.ofFloat(fs_wswapL   , View.ROTATION_Y, /* WSWAP__GRAB_RY,*/  WSWAP__FREE_RY))
                .with       (ObjectAnimator.ofFloat(fs_wswapR   , View.ROTATION_Y, /* WSWAP__GRAB_RY,*/  WSWAP__FREE_RY))
                .with       (ObjectAnimator.ofFloat(fs_browse   , View.ROTATION_Y, /*WV_TOOL_GRAB_RY,*/ WV_TOOL_FREE_RY))
                .with       (ObjectAnimator.ofFloat(fs_bookmark , View.ROTATION_Y, /* WBOOKS_GRAB_RY,*/  WBOOKS_FREE_RY))
                .with       (ObjectAnimator.ofFloat(fs_goBack   , View.ROTATION_Y, /* WBOOKS_GRAB_RY,*/  WBOOKS_FREE_RY))
                .with       (ObjectAnimator.ofFloat(fs_goForward, View.ROTATION_Y, /* WBOOKS_GRAB_RY,*/  WBOOKS_FREE_RY))
                ;

            if( is_fs_webView2_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle2  , View.ROTATION_X, /* WTITLE_GRAB_RX,*/  WTITLE_FREE_RX));
            }
            if( is_fs_webView3_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle3  , View.ROTATION_X, /* WTITLE_GRAB_RX,*/  WTITLE_FREE_RX));
            }

            set.start();
        }
        else {
            fs_wtitle       .setRotationX( WTITLE_FREE_RX );
            fs_wswapL       .setRotationY( WSWAP__FREE_RY );
            fs_wswapR       .setRotationY( WSWAP__FREE_RY );
            fs_browse       .setRotationY( WV_TOOL_FREE_RY );
            fs_bookmark     .setRotationY( WBOOKS_FREE_RY );
            fs_goBack       .setRotationY( WBOOKS_FREE_RY );
            fs_goForward    .setRotationY( WBOOKS_FREE_RY );

            if( is_fs_webView2_in_screen() ) {
                fs_wtitle2  .setRotationX( WTITLE_FREE_RX );
            }
            if( is_fs_webView3_in_screen() ) {
                fs_wtitle3  .setRotationX( WTITLE_FREE_RX );
            }
        }
    }
    //}}}
    // fs_webView_GRAB_R {{{
    private final Runnable hr_fs_webView_GRAB_R  = new Runnable() {
        @Override public void run() {
            fs_webView_GRAB_R( ANIM_SUPPORTED );
        }
    };

    private void fs_webView_GRAB_R(boolean with_anim)
    {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, "fs_webView_GRAB_R", "XXX fs_webView_GRAB_R"+ (with_anim ? " with_anim" : ""));
        if( some_missing_animated_view() ) return;

        if( with_anim ) {
            AnimatorSet set = new AnimatorSet();

            set.setDuration( WEBVIEW_GRAB_R_DURATION );

            set.setInterpolator(new DecelerateInterpolator());

            //...play.......(ObjectAnimator.ofFloat(fs_XXXXXX   , View.XXXXXXXXXX, /* WTITLEXXXXX_XX,*/  WTITLE_XXXX_XX))
            AnimatorSet.Builder builder =
            set .play       (ObjectAnimator.ofFloat(fs_wtitle   , View.ROTATION_X, /* WTITLE_FREE_RX,*/  WTITLE_GRAB_RX))
                .with       (ObjectAnimator.ofFloat(fs_wswapL   , View.ROTATION_Y, /* WSWAP__FREE_RY,*/  WSWAP__GRAB_RY))
                .with       (ObjectAnimator.ofFloat(fs_wswapR   , View.ROTATION_Y, /* WSWAP__FREE_RY,*/  WSWAP__GRAB_RY))
                .with       (ObjectAnimator.ofFloat(fs_browse   , View.ROTATION_Y, /*WV_TOOL_FREE_RY,*/ WV_TOOL_GRAB_RY))
                .with       (ObjectAnimator.ofFloat(fs_bookmark , View.ROTATION_Y, /* WBOOKS_FREE_RY,*/  WBOOKS_GRAB_RY))
                .with       (ObjectAnimator.ofFloat(fs_goBack   , View.ROTATION_Y, /* WBOOKS_FREE_RY,*/  WBOOKS_GRAB_RY))
                .with       (ObjectAnimator.ofFloat(fs_goForward, View.ROTATION_Y, /* WBOOKS_FREE_RY,*/  WBOOKS_GRAB_RY))
                ;

            if( is_fs_webView2_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle2  , View.ROTATION_X, /* WTITLE_FREE_RX,*/  WTITLE_GRAB_RX));
            }
            if( is_fs_webView3_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle3  , View.ROTATION_X, /* WTITLE_FREE_RX,*/  WTITLE_GRAB_RX));
            }

            set.start();
        }
        else {
            fs_wtitle       .setRotationX( WTITLE_GRAB_RX  );
            fs_wswapL       .setRotationY( WSWAP__GRAB_RY  );
            fs_wswapR       .setRotationY( WSWAP__GRAB_RY  );
            fs_browse       .setRotationY( WV_TOOL_GRAB_RY );
            fs_bookmark     .setRotationY( WBOOKS_GRAB_RY  );
            fs_goBack       .setRotationY( WBOOKS_GRAB_RY  );
            fs_goForward    .setRotationY( WBOOKS_GRAB_RY  );

            if( is_fs_webView2_in_screen() ) {
                fs_wtitle2  .setRotationX( WTITLE_GRAB_RX  );
            }
            if( is_fs_webView3_in_screen() ) {
                fs_wtitle3  .setRotationX( WTITLE_GRAB_RX  );
            }
        }
    }
    //}}}
    //* 6 ANIM SCALE
    // fs_webView_FREE_S {{{
    private final Runnable hr_fs_webView_FREE_S  = new Runnable() {
        @Override public void run() {
            fs_webView_FREE_S( ANIM_SUPPORTED );
        }
    };

    private void fs_webView_FREE_S(boolean with_anim)
    {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, "fs_webView_FREE_S", "XXX fs_webView_FREE_S"+ (with_anim ? " with_anim" : ""));
        if( some_missing_animated_view() ) return;

        // free webview larger {{{
        int split_width        = (Settings.SCREEN_W - Settings.WEBVIEW_MARGIN) / Settings.SCREEN_SPLIT;

        int grab_w = Settings.WEBVIEW_WIDTH;
        int free_w = Settings.WEBVIEW_WIDTH + Settings.WEBVIEW_GAP-2; // larger (may fill the gaps)

        int grab_h = Settings.DISPLAY_H - 2*Settings.WEBVIEW_MARGIN - Settings.WEBVIEW_GAP;
        int free_h = Settings.DISPLAY_H -   Settings.WEBVIEW_MARGIN;

        float free_sx  =  (float)free_w / (float)grab_w; // make it larger
    //  float grab_sx  =  1F;
        float free_sy  =  (float)free_h / (float)grab_h; // make it larger
    //  float grab_sy  =  1F;

        //}}}
        if( with_anim ) {
            AnimatorSet set = new AnimatorSet();

            set.setDuration( WEBVIEW_FREE_S_DURATION );

            set.setInterpolator(new DecelerateInterpolator());

            //...play.......(ObjectAnimator.ofFloat(fs_XXXXXX   , View.XXXXXXXXXX,/* WTITLEXXXXX_XX,*/  WTITLE_XXXX_XX))
            AnimatorSet.Builder builder =
            set .play       (ObjectAnimator.ofFloat(fs_wtitle   , View.SCALE_X   ,/* WTITLE_GRAB_SX,*/  WTITLE_FREE_SX))
                .with       (ObjectAnimator.ofFloat(fs_wtitle   , View.SCALE_Y   ,/* WTITLE_GRAB_SY,*/  WTITLE_FREE_SY))
                .with       (ObjectAnimator.ofFloat(fs_wswapL   , View.SCALE_X   ,/* WTITLE_GRAB_SY,*/  WSWAP__FREE_SX))
                .with       (ObjectAnimator.ofFloat(fs_wswapL   , View.SCALE_Y   ,/* WTITLE_GRAB_SY,*/  WSWAP__FREE_SY))
                .with       (ObjectAnimator.ofFloat(fs_wswapR   , View.SCALE_X   ,/* WTITLE_GRAB_SY,*/  WSWAP__FREE_SX))
                .with       (ObjectAnimator.ofFloat(fs_wswapR   , View.SCALE_Y   ,/* WTITLE_GRAB_SY,*/  WSWAP__FREE_SY))
                .with       (ObjectAnimator.ofFloat(fs_webView  , View.SCALE_X   ,/*        grab_sx,*/         free_sx))
                .with       (ObjectAnimator.ofFloat(fs_webView  , View.SCALE_Y   ,/*        grab_sy,*/         free_sy))
                ;

            if( is_fs_webView2_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle2  , View.SCALE_X   ,/* WTITLE_GRAB_SX,*/  WTITLE_FREE_SX));
                builder.with(ObjectAnimator.ofFloat(fs_wtitle2  , View.SCALE_Y   ,/* WTITLE_GRAB_SY,*/  WTITLE_FREE_SY));
            }
            if( is_fs_webView3_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle3  , View.SCALE_X   ,/* WTITLE_GRAB_SX,*/  WTITLE_FREE_SX));
                builder.with(ObjectAnimator.ofFloat(fs_wtitle3  , View.SCALE_Y   ,/* WTITLE_GRAB_SY,*/  WTITLE_FREE_SY));
            }

            if( is_fs_webView2_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_webView2 , View.SCALE_X   , /*       grab_sx,*/         free_sx));
                builder.with(ObjectAnimator.ofFloat(fs_webView2 , View.SCALE_Y   , /*       grab_sy,*/         free_sy));
            }
            if( is_fs_webView3_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_webView3 , View.SCALE_X   , /*       grab_sx,*/         free_sx));
                builder.with(ObjectAnimator.ofFloat(fs_webView3 , View.SCALE_Y   , /*       grab_sy,*/         free_sy));
            }

            set.start();
        }
        else {
            fs_wtitle       .setScaleX( WTITLE_FREE_SX );
            fs_wtitle       .setScaleY( WTITLE_FREE_SY );
            fs_wswapL       .setScaleX( WSWAP__FREE_SX );
            fs_wswapL       .setScaleY( WSWAP__FREE_SY );
            fs_wswapR       .setScaleX( WSWAP__FREE_SX );
            fs_wswapR       .setScaleY( WSWAP__FREE_SY );
            fs_webView      .setScaleX( free_sx        );
            fs_webView      .setScaleY( free_sy        );

            if( is_fs_webView2_in_screen() ) {
                fs_wtitle2  .setScaleX( WTITLE_FREE_SX );
                fs_wtitle2  .setScaleY( WTITLE_FREE_SY );
            }
            if( is_fs_webView3_in_screen() ) {
                fs_wtitle3  .setScaleX( WTITLE_FREE_SX );
                fs_wtitle3  .setScaleY( WTITLE_FREE_SY );
            }

            if( is_fs_webView2_in_screen() ) {
                fs_webView2 .setScaleX( free_sx        );
                fs_webView2 .setScaleY( free_sy        );
            }
            if( is_fs_webView3_in_screen() ) {
                fs_webView3 .setScaleX( free_sx        );
                fs_webView3 .setScaleY( free_sy        );
            }
        }
    }
    //}}}
    // fs_webView_GRAB_S {{{
    private final Runnable hr_fs_webView_GRAB_S  = new Runnable() {
        @Override public void run() {
            fs_webView_GRAB_S( ANIM_SUPPORTED );
        }
    };

    private void fs_webView_GRAB_S(boolean with_anim)
    {
//*WEBVIEW*/Settings.MON(TAG_WEBVIEW, "fs_webView_GRAB_S", "XXX fs_webView_GRAB_S"+ (with_anim ? " with_anim" : ""));
        if( some_missing_animated_view() ) return;

        // grab webview smaller {{{
        int split_width        = (Settings.SCREEN_W - Settings.WEBVIEW_MARGIN) / Settings.SCREEN_SPLIT;

        int grab_w = Settings.WEBVIEW_WIDTH;
        int free_w = Settings.WEBVIEW_WIDTH + Settings.WEBVIEW_GAP-2; // may fill the gaps

        int grab_h = Settings.DISPLAY_H - 2*Settings.WEBVIEW_MARGIN - Settings.WEBVIEW_GAP;
        int free_h = Settings.DISPLAY_H -   Settings.WEBVIEW_MARGIN;

    //  float free_sx  =  (float)free_w / (float)grab_w; // make it larger
        float grab_sx  =  1F;
    //  float free_sy  =  (float)free_h / (float)grab_h; // make it larger
        float grab_sy  =  1F;

        //}}}
        if( with_anim ) {
            AnimatorSet set = new AnimatorSet();

            set.setDuration( WEBVIEW_GRAB_S_DURATION );

            set.setInterpolator(new DecelerateInterpolator());

            //...play.......(ObjectAnimator.ofFloat(fs_XXXXXX   , View.XXXXXXXXXX,/* WTITLEXXXXX_XX,*/  WTITLE_XXXX_XX))
            AnimatorSet.Builder builder =
            set .play       (ObjectAnimator.ofFloat(fs_wtitle   , View.SCALE_X   ,/* WTITLE_FREE_SX,*/  WTITLE_GRAB_SX))
                .with       (ObjectAnimator.ofFloat(fs_wtitle   , View.SCALE_Y   ,/* WTITLE_FREE_SY,*/  WTITLE_GRAB_SY))
                .with       (ObjectAnimator.ofFloat(fs_wswapL   , View.SCALE_X   ,/* WTITLE_FREE_SY,*/  WSWAP__GRAB_SX))
                .with       (ObjectAnimator.ofFloat(fs_wswapL   , View.SCALE_Y   ,/* WTITLE_FREE_SY,*/  WSWAP__GRAB_SY))
                .with       (ObjectAnimator.ofFloat(fs_wswapR   , View.SCALE_X   ,/* WTITLE_FREE_SY,*/  WSWAP__GRAB_SX))
                .with       (ObjectAnimator.ofFloat(fs_wswapR   , View.SCALE_Y   ,/* WTITLE_FREE_SY,*/  WSWAP__GRAB_SY))
                .with       (ObjectAnimator.ofFloat(fs_webView  , View.SCALE_X,   /*        free_sx,*/         grab_sx))
                .with       (ObjectAnimator.ofFloat(fs_webView  , View.SCALE_Y,   /*        free_sy,*/         grab_sy))
                ;

            if( is_fs_webView2_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle2 , View.SCALE_X    ,/* WTITLE_FREE_SX,*/  WTITLE_GRAB_SX));
                builder.with(ObjectAnimator.ofFloat(fs_wtitle2 , View.SCALE_Y    ,/* WTITLE_FREE_SY,*/  WTITLE_GRAB_SY));
            }
            if( is_fs_webView3_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_wtitle3 , View.SCALE_X    ,/* WTITLE_FREE_SX,*/  WTITLE_GRAB_SX));
                builder.with(ObjectAnimator.ofFloat(fs_wtitle3 , View.SCALE_Y    ,/* WTITLE_FREE_SY,*/  WTITLE_GRAB_SY));
            }

            if( is_fs_webView2_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_webView2 , View.SCALE_X   ,/*        free_sx,*/         grab_sx));
                builder.with(ObjectAnimator.ofFloat(fs_webView2 , View.SCALE_Y   ,/*        free_sy,*/         grab_sy));
            }
            if( is_fs_webView3_in_screen() ) {
                builder.with(ObjectAnimator.ofFloat(fs_webView3 , View.SCALE_X   ,/*        free_sx,*/         grab_sx));
                builder.with(ObjectAnimator.ofFloat(fs_webView3 , View.SCALE_Y   ,/*        free_sy,*/         grab_sy));
            }

            set.start();
        }
        else {
            fs_wtitle       .setScaleX( WTITLE_GRAB_SX );
            fs_wtitle       .setScaleY( WTITLE_GRAB_SY );
            fs_wswapL       .setScaleX( WSWAP__GRAB_SX );
            fs_wswapL       .setScaleY( WSWAP__GRAB_SY );
            fs_wswapR       .setScaleX( WSWAP__GRAB_SX );
            fs_wswapR       .setScaleY( WSWAP__GRAB_SY );
            fs_webView      .setScaleX( grab_sx        );
            fs_webView      .setScaleY( grab_sy        );

            if( is_fs_webView2_in_screen() ) {
                fs_wtitle2  .setScaleX( WTITLE_GRAB_SX );
                fs_wtitle2  .setScaleY( WTITLE_GRAB_SY );
            }
            if( is_fs_webView3_in_screen() ) {
                fs_wtitle3  .setScaleX( WTITLE_GRAB_SX );
                fs_wtitle3  .setScaleY( WTITLE_GRAB_SY );
            }

            if( is_fs_webView2_in_screen() ) {
                fs_webView2 .setScaleX( grab_sx        );
                fs_webView2 .setScaleY( grab_sy        );
            }
            if( is_fs_webView3_in_screen() ) {
                fs_webView3 .setScaleX( grab_sx        );
                fs_webView3 .setScaleY( grab_sy        );
            }
        }
    }
    //}}}
    //* 7 ANIM END
    // fs_webView_FREE_E {{{
    private final Runnable hr_fs_webView_FREE_E  = new Runnable()
    {
        @Override public void run()
        {
        String caller = "hr_fs_webView_FREE_E";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);
            fs_webview_animation_after(false, caller);
        }
    };
    // }}}
    // fs_webView_GRAB_E {{{
    private final Runnable hr_fs_webView_GRAB_E  = new Runnable()
    {
        @Override public void run()
        {
        String caller = "hr_fs_webView_GRAB_E";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);
            fs_webview_animation_after(true, caller);
        }
    };
    // }}}
    //* 8 ANIM AFTER
    // fs_webview_animation_after {{{
    private void fs_webview_animation_after(boolean is_grabbed, String caller)
    {
        caller += "->fs_webview_animation_after(is_grabbed="+is_grabbed+")";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);

        fs_webView_isGrabbed = is_grabbed;

        // REINSTATE WV_TOOL_MARKS_LOCK .. (fORCED BY fs_webview_animation_before)
        //wvTools.property_set(WVTools.WV_TOOL_MARKS_LOCK, false);
        // replaced by calls to [is_anim_running]

        // KEEP WVTOOLS PROPERTY IN SYNC
        wvTools.property_set(WVTools.WV_TOOL_grab, fs_webView_isGrabbed);
        wvTools.marker_wv_sync(caller);

        handler.re_postDelayed( hr_fs_webview_animation_after, WEBVIEW_ANIMATION_TO_SYNC_VISIBILITY_DELAY);

        // ENDING: [TRANSIENT ANIMATION CHANGES]
        fs_webview_animation_running = false;
    }

    private final Runnable hr_fs_webview_animation_after = new Runnable()
    {
        @Override public void run() {
            do_fs_webview_animation_after();
        }
    };
    //}}}
    // do_fs_webview_animation_after {{{
    private void do_fs_webview_animation_after()
    {
        String caller = "do_fs_webview_animation_after";
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);

//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "...fs_webview_session_running=["+ fs_webview_session_running   +"]");
        if( fs_webview_session_running )
        {
            fs_webView_show(caller);
            on_webView_changed(ON_WEBVIEW_ADJUSTED, fs_webView, caller);
        }
        else {
            fs_webView_hide(caller);
            wvTools.hide   (caller);

//if( is_magnify_np_showing() ) handler.re_postDelayed(hr_magnify_np_hide, WEBVIEW_SYNC_VISIBILITY_TO_MAGNIFY_NP_HIDE_DELAY);
        }
    }

//  private final Runnable hr_magnify_np_hide = new Runnable() {
//      @Override public void run() {
//          magnify_np_hide("hr_magnify_np_hide");
//      }
//  };

    //}}}
    //}}}

    //* WEBVIEW FULLSCREEN */
//    //{{{
//    // toggle_fs_webView_fullscreen {{{
//    // schedule_toggle_fs_webView_fullscreen {{{
//    private View view_for_toggle_fs_webView_fullscreen = null;
//    private void schedule_toggle_fs_webView_fullscreen(View view)
//    {
//        view_for_toggle_fs_webView_fullscreen = view;
//        handler.re_postDelayed(hr_toggle_fs_webView_fullscreen, 50);
//    }
//    private final Runnable hr_toggle_fs_webView_fullscreen  = new Runnable() {
//        @Override public void run()
//        {
//            if(view_for_toggle_fs_webView_fullscreen == null)
//                return;
//
//        //  toggle_fs_webView_fullscreen(view_for_toggle_fs_webView_fullscreen, ANIM_SUPPORTED);
//            toggle_fs_webView_fullscreen(view_for_toggle_fs_webView_fullscreen, false         );
//        }
//    };
//    //}}}
//    // {{{
//    private static final int WEBVIEW_EXPAND_DURATION = 150;
//    private final int[] expanded_xy_rb = new int[4];
//    // }}}
//    private void toggle_fs_webView_fullscreen(View view, boolean with_anim)
//    {
//        String caller = "toggle_fs_webView_fullscreen("+get_view_name(view)+")";
///*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, caller);
///* // free_[LRw] grab[LRw] {{{
//
//   free_L = Settings.WEBVIEW_MARGIN + Settings.WEBVIEW_WIDTH - Settings.WEBVIEW_OVER;
//   free_R =                  free_L + Settings.WEBVIEW_WIDTH + Settings.WEBVIEW_GAP;
//   free_w =                           Settings.WEBVIEW_WIDTH + Settings.WEBVIEW_GAP-2;
//
//   grab_L = Settings.WEBVIEW_MARGIN + Settings.WEBVIEW_WIDTH - Settings.WEBVIEW_OVER;
//   grab_R =                  grab_L + Settings.WEBVIEW_WIDTH + Settings.WEBVIEW_GAP;
//   grab_w = Settings.WEBVIEW_WIDTH;
//
//*/ // }}}
//        // {{{
//        int       x,   y,   w,   h;
//        int     toX, toY, toW, toH;
//        ViewGroup.MarginLayoutParams vlp = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
//        boolean expanding = (expanded_xy_rb[3] == 0);
//        //}}}
//        // SAVE UNEXPANDED GEOMETRY {{{
//        if( expanding )
//        {
//
//            // FROM LAYOUT
//            expanded_xy_rb[0] = vlp.leftMargin;
//            expanded_xy_rb[1] = vlp.topMargin ;
//        //  expanded_xy_rb[2] = vlp.width     ;
//        //  expanded_xy_rb[3] = vlp.height    ;
//            // FROM VIEW
//        //  expanded_xy_rb[0]   = (int)view.getX     ();
//        //  expanded_xy_rb[1]   = (int)view.getY     ();
//            expanded_xy_rb[2]   = view.getWidth ();
//            expanded_xy_rb[3]   = view.getHeight();
//        //  expanded_xy_rb[2]   = (int)view.getRight ();
//        //  expanded_xy_rb[3]   = (int)view.getBottom();
//
//            toX                 = 0;
//            toY                 = 0;
//            toW                 = Settings.DISPLAY_W;
//            toH                 = Settings.DISPLAY_H;
//
//            //view.bringToFront(    );
//            //view.setPivotX   (  0 );
//            //view.setPivotY   (  0 );
//            //view.setScaleX   ( 1f );
//            //view.setScaleY   ( 1f );
//            /* // {{{
//               vlp.leftMargin = 0;
//               vlp.topMargin  = 0;
//            //  vlp.width      = toW;
//            //  vlp.height     = toH;
//            vlp.width      = frameLayout.getWidth ();
//            vlp.height     = frameLayout.getHeight();
//            view.setLayoutParams( vlp );
//             */ // }}}
//        }
//        //}}}
//        // RESTORE UNEXPANDED GEOMETRY {{{
//        else {
//            toX = expanded_xy_rb[0]; expanded_xy_rb[0] = 0;
//            toY = expanded_xy_rb[1]; expanded_xy_rb[1] = 0;
//            toW = expanded_xy_rb[2]; expanded_xy_rb[2] = 0;
//            toH = expanded_xy_rb[3]; expanded_xy_rb[3] = 0;
//        }
//        //}}}
///*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, String.format(    "%s=[%4d@%4d] [%4dx%4d]", (expanding ? "EXPAND":"RESTOR"), toX,toY , toW,toH));
//
//        // ANIM {{{
//        if( with_anim )
//        {
//            AnimatorSet set   = new AnimatorSet();
//            set.setDuration( WEBVIEW_EXPAND_DURATION );
//            set.setInterpolator(new DecelerateInterpolator());
//            set
//                .play(ObjectAnimator.ofFloat(view, View.X         ,    toX))
//                .with(ObjectAnimator.ofFloat(view, View.Y         ,    toY))
//                .with(ObjectAnimator.ofInt  (view, Property_WIDTH ,    toW))
//                .with(ObjectAnimator.ofInt  (view, Property_HEIGHT,    toH))
//              //.with(ObjectAnimator.ofInt  (view, Property_RIGHT ,    toR))
//              //.with(ObjectAnimator.ofInt  (view, Property_BOTTOM,    toB))
//                ;
//
//            set.start();
//        }
//        //}}}
//        // INSTANT {{{
//        else {
//
//            // LAYOUT
//            vlp.leftMargin = toX;
//            vlp.topMargin  = toY;
//        //  vlp.width      = toW;
//        //  vlp.height     = toH;
//            view.setLayoutParams( vlp );
//
//            // SET
//        //  view.setX     (     toX);
//        //  view.setY     (     toY);
//            view.setX     (     0);
//            view.setY     (     0);
//            view.setRight (     toW);
//            view.setBottom(     toH);
//        }
//        //}}}
//// 1 - RESULTING VIEW REPORT {{{
///*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, String.format("SCREEN=[%4d@%4d] [%4dx%4d]",0,0 , Settings.DISPLAY_W, Settings.DISPLAY_H));
//
//x = (int)view.getX     ();
//y = (int)view.getY     ();
//w =      view.getWidth ();
//h =      view.getHeight();
///*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, String.format("XYWH..=[%4d@%4d] [%4dx%4d]",x,y , w,h));
////}}}
//// 2 - RESULTING LAYOUT REPORT {{{
//vlp = (ViewGroup.MarginLayoutParams)view.getLayoutParams();
//x = vlp.leftMargin;
//y = vlp.topMargin ;
//w = vlp.width     ;
//h = vlp.height    ;
//view.setLayoutParams( vlp );
///*WEBVIEW*/Settings.MON(TAG_WEBVIEW, caller, String.format("LAYOUT=[%4d@%4d] [%4dx%4d]",x,y , w,h));
////}}}
//    }
//    //}}}
//    // Property_WIDTH {{{
//    private final Property<View, Integer> Property_WIDTH =
//        new Property<View, Integer>(Integer.class, "viewScrollY") {
//            @Override public void    set(View view, Integer width) {        view.setRight((int)view.getX() + width); }
//            @Override public Integer get(View view               ) { return view.getWidth(                        ); }
//        };
//    // }}}
//    // Property_HEIGHT {{{
//    private final Property<View, Integer> Property_HEIGHT =
//        new Property<View, Integer>(Integer.class, "viewScrollY") {
//            @Override public void    set(View view, Integer height) {        view.setBottom((int)view.getY()+ height); }
//            @Override public Integer get(View view                ) { return view.getHeight(                        ); }
//        };
//    // }}}
//    // Property_RIGHT {{{
//    private final Property<View, Integer> Property_RIGHT =
//        new Property<View, Integer>(Integer.class, "viewScrollY") {
//            @Override public void    set(View view, Integer right) {        view.setRight(    right ); }
//            @Override public Integer get(View view               ) { return view.getRight(          ); }
//        };
//    // }}}
//    // Property_BOTTOM {{{
//    private final Property<View, Integer> Property_BOTTOM =
//        new Property<View, Integer>(Integer.class, "viewScrollY") {
//            @Override public void    set(View view, Integer bottom) {        view.setBottom( bottom ); }
//            @Override public Integer get(View view                ) { return view.getBottom(        ); }
//        };
//    // }}}
//    //}}}

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** WVTOOLS */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ WVTOOLS @ {{{
    // new_Clipboard_TEXT {{{
    public static void new_Clipboard_TEXT(String text)
    {
        String caller = "new_Clipboard_TEXT("+text+")";
//*WVTOOLS*/Settings.MOC(TAG_WVTOOLS, caller);

        if(RTabs_instance         == null) return;
        if(RTabs_instance.wvTools == null) return;

        RTabs_instance.wvTools.add_marker_for_text(text.trim());
        RTabs_instance.schedule_on_webView_sync(caller);
    }
    //}}}
    // on_webView_changed (for visible scrollbar only: position, size, color) {{{
    //{{{
    private static final int ON_WEBVIEW_ADJUSTED = 1;
    private static final int ON_WEBVIEW_COLORED  = 2;
    private static final int ON_WEBVIEW_LAYOUT   = 3;
    private static final int ON_WEBVIEW_MEASURED = 4;
    private static final int ON_WEBVIEW_PAGE_FIN = 5;
    private static final int ON_WEBVIEW_RESIZED  = 6;
    private static final int ON_WEBVIEW_SCROLLED = 7;
    private static final int ON_WEBVIEW_SYNC     = 8;

    private static String get_on_webView_changed_reason(int reason)
    {
        switch( reason) {
            case ON_WEBVIEW_ADJUSTED : return "ADJUSTED";
            case ON_WEBVIEW_COLORED  : return "COLORED.";
            case ON_WEBVIEW_MEASURED : return "MEASURED";
            case ON_WEBVIEW_PAGE_FIN : return "PAGE_FIN";
            case ON_WEBVIEW_RESIZED  : return "RESIZED.";
            case ON_WEBVIEW_SCROLLED : return "SCROLLED";
            case ON_WEBVIEW_SYNC     : return "SYNC....";
            default                  : return "default.";
        }
    }
    //}}}
    private void on_webView_changed(int reason, MWebView wv, String caller)
    {
        // HIDDEN {{{
        caller += "->on_webView_changed("+ get_on_webView_changed_reason(reason) +": "+get_view_name(wv) +" BY ["+caller+"])";
        // [wv]
        if(wv == null) return;

        // [sb]
        NpButton wvTools_sbX
            = (wv == fs_webView ) ? wvTools.sb
            : (wv == fs_webView2) ? wvTools.sb2
            :                       wvTools.sb3;
        if(wvTools_sbX == null) return;

        if( !is_fs_webViewX_in_screen(wv) )
        {
            wvTools_sbX.setVisibility( View.GONE );
            return;
        }
        // }}}
        // CONTROL-FLAGS {{{
        boolean flagged
            =  ((reason == ON_WEBVIEW_ADJUSTED) && wvTools.property_get( WVTools.WV_TOOL_FLAG_ADJUSTED ))
            || ((reason == ON_WEBVIEW_COLORED ) && wvTools.property_get( WVTools.WV_TOOL_FLAG_COLORED  ))
            || ((reason == ON_WEBVIEW_LAYOUT  ) && wvTools.property_get( WVTools.WV_TOOL_FLAG_LAYOUT   ))
            || ((reason == ON_WEBVIEW_MEASURED) && wvTools.property_get( WVTools.WV_TOOL_FLAG_MEASURED ))
            || ((reason == ON_WEBVIEW_PAGE_FIN) && wvTools.property_get( WVTools.WV_TOOL_FLAG_PAGE_FIN ))
            || ((reason == ON_WEBVIEW_RESIZED ) && wvTools.property_get( WVTools.WV_TOOL_FLAG_RESIZED  ))
            || ((reason == ON_WEBVIEW_SCROLLED) && wvTools.property_get( WVTools.WV_TOOL_FLAG_SCROLLED ))
            || ((reason == ON_WEBVIEW_SYNC    ) && wvTools.property_get( WVTools.WV_TOOL_FLAG_SYNC     ))
            ;

        if(flagged ) {
//*WVTOOLS*/Settings.MOM(TAG_WVTOOLS, caller+": FLAGGED [" + get_on_webView_changed_reason(reason) +"]");

            return;
        }
        //}}}
        // FILTER [REAL-TIME] AND (FLAGGED DELAYED UPDATES) {{{
        boolean sync_markers = true;
            //=    wvTools.property_get( WVTools.WV_TOOL_FLAG_LAYOUT   )
            //||   wvTools.property_get( WVTools.WV_TOOL_FLAG_PAGE_FIN );

        boolean sync_tools = true;
            //=    wvTools.property_get( WVTools.WV_TOOL_FLAG_TOOLS    );

        boolean sync_scroll = true;
            //=    wvTools.property_get( WVTools.WV_TOOL_FLAG_SCROLLED );

        //}}}
        // DELAYED {{{
        if(reason == ON_WEBVIEW_SYNC)
        {
            if( sync_markers ) wvTools.marker_wv_sync(caller);
            if( sync_tools   ) wvTools.sb_layout_tools( wvTools_sbX, caller);
            if( sync_scroll  ) wvTools.sb_adjust_thumb( wvTools_sbX,     wv);
        }
        //}}}
        // SYNCHRONOUS UPDATES {{{
        else if(wv.getUrl() != null)
        {
            if     (reason == ON_WEBVIEW_PAGE_FIN)   wvTools.marker_wv_sync(caller);
            else if(reason == ON_WEBVIEW_LAYOUT  )   wvTools.marker_wv_sync(caller);
            else if(reason == ON_WEBVIEW_ADJUSTED)   wvTools.marker_wv_sync(caller);

            else if(reason == ON_WEBVIEW_COLORED )   wvTools.sb_adjust_thumb(wvTools_sbX, wv);
            else if(reason == ON_WEBVIEW_RESIZED )   wvTools.sb_adjust_thumb(wvTools_sbX, wv);
            else if(reason == ON_WEBVIEW_SCROLLED) { wvTools.sb_adjust_thumb(wvTools_sbX, wv); wvTools.sb_layout_tools(wvTools_sbX, caller); }

        //  if(reason == ON_WEBVIEW_MEASURED)
        //  if(reason == ON_WEBVIEW_SYNC    )
        }
        //}}}
    }
    //}}}
    // schedule_on_webView_sync {{{
    // ...used by MotionListener.onTouchEvent.ACTION_UP .. (that happens before MGestureListener.onFling)
    private void schedule_on_webView_sync(String caller)
    {
        caller += "->schedule_on_webView_sync";
//*WVTOOLS*/Settings.MOC(TAG_WVTOOLS, caller);

        handler.re_postDelayed(hr_on_webView_changed, 500);
    }

    private final Runnable hr_on_webView_changed = new Runnable() {
        @Override public void run() {
            if(fs_webview_session_running)
            {
                String caller = "hr_on_webView_changed";
                if( is_view_showing( fs_webView  ) ) on_webView_changed(ON_WEBVIEW_SYNC, fs_webView , caller);
                if( is_view_showing( fs_webView2 ) ) on_webView_changed(ON_WEBVIEW_SYNC, fs_webView2, caller);
                if( is_view_showing( fs_webView3 ) ) on_webView_changed(ON_WEBVIEW_SYNC, fs_webView3, caller);
            }
        }
    };
    //}}}
    // schedule_clear_page_boundary_rect {{{

    private MWebView clear_page_boundary_rect_wv = null;

    public void schedule_clear_page_boundary_rect(MWebView wv, long delay)
    {
        clear_page_boundary_rect_wv = wv;
        handler.re_postDelayed(hr_clear_page_boundary_rect, delay);
    }

    private final Runnable hr_clear_page_boundary_rect = new Runnable() {
        @Override public void run()
        {
            if(clear_page_boundary_rect_wv == null) return;
            clear_page_boundary_rect_wv.clr_page_boundary_rect();
            clear_page_boundary_rect_wv = null;
        }
    };
    //}}}
    // fs_wswapL, fs_wswapR {{{
    // create_fs_wswap {{{
    private NpButton create_fs_wswap()
    {
        // np_button   {{{
        NpButton np_button = new NpButton( activity );
        np_button.lockElevation     ( Settings.FS_WSWAP_ELEVATION );
        np_button.set_shape         ( NotePane.SHAPE_TAG_CIRCLE    );
    //  np_button.set_shape         ( NotePane.SHAPE_TAG_ONEDGE    );
    //  np_button.set_round_corners_left ();
    //  np_button.set_round_corners_right();
        np_button.setTypeface       ( Settings.getNotoTypeface()   );
        np_button.setTextColor      ( FS_SWAP_TEXTCOLOR            );
        np_button.setBackgroundColor( FS_SWAP_BACKCOLOR            );

        np_button.setPadding        (  0, 0, 0, 0                  );
        np_button.setTypeface       ( Settings.getNotoTypeface()   );
        np_button.setEllipsize      ( TextUtils.TruncateAt.END     );

        np_button.setText           ( Settings.ParseUnicode( Settings.FS_SWAP_TEXT ) );
        np_button.setTag            ( Settings.FS_SWAP_INFO                          );
        np_button.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL); // override NpButton.setText
        //}}}
        // GUI {{{
        wvContainer.addView( np_button );
        // }}}
        // EVENTS {{{
        np_button  .setOnTouchListener( builtin_nb_OnTouchListener  );
        // }}}
        return np_button;
    }
    //}}}
    //}}}
//    // schedule_wvTools_sb_layout_tools {{{
//    private void schedule_wvTools_sb_layout_tools(String caller)
//    {
//        caller = "schedule_wvTools_sb_layout_tools("+caller+")";
///*WVTOOLS*/Settings.MOC(TAG_WVTOOLS, caller);
//
//        handler.re_postDelayed(hr_wvTools_sb_layout_tools, 500);
//    }
//
//    private final Runnable hr_wvTools_sb_layout_tools = new Runnable() {
//        @Override public void run() {
//            if(fs_webview_session_running)
//            {
//                String caller = "hr_wvTools_sb_layout_tools";
//                if( is_view_showing( fs_webView  ) ) wvTools.sb_layout_tools(wvTools.sb , caller);
//                if( is_view_showing( fs_webView2 ) ) wvTools.sb_layout_tools(wvTools.sb2, caller);
//                if( is_view_showing( fs_webView3 ) ) wvTools.sb_layout_tools(wvTools.sb3, caller);
//            }
//        }
//    };
//    //}}}
//    // schedule_wv_scroll_to_thumb_o {{{
//    private final LinkedHashMap<MWebView, Integer> wv_scrollBy_queue = new LinkedHashMap<>();
//
//    public  final void schedule_wv_scroll_to_thumb_o(View view, int thumb_o)
//    {
///*WVTOOLS*/Settings.MOC(TAG_WVTOOLS, "schedule_wv_scroll_to_thumb_o("+get_view_name(view)+", thumb_o=["+thumb_o+"])");
//        wv_scrollBy_queue.put((MWebView)view, thumb_o);
//        handler.re_postDelayed(hr_wv_scrollTo, 500);
//    }
//    private final Runnable hr_wv_scrollTo = new Runnable() {
//        @Override public void run()
//        {
//            if( wv_scrollBy_queue.isEmpty() ) return;
//
//            for(Map.Entry<MWebView, Integer> entry : wv_scrollBy_queue.entrySet())
//            {
//                MWebView wv = (MWebView)entry.getKey();
//                int thumb_o = entry.getValue(); entry.setValue(-1); // consume
//                if(thumb_o >= 0) {
//                    float page_height = wv.computeVerticalScrollRange();
//                    float   wv_height = wv.getHeight();
//                    float       ratio = page_height / wv_height;
//                    int       scrollY = (int)(thumb_o * ratio);
///*WVTOOLS*/Settings.MOM(TAG_WVTOOLS, String.format("hr_wv_scrollTo: ratio=[%2f], wv.scrollTo(0, %d)", ratio, scrollY));
//                    wv.scrollTo(0, scrollY);
//                }
//            }
//        }
//    };
//    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** EVENTS*/
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ GESTURE @ {{{
    // {{{
    private static final int             LONGPRESS_DURATION = ViewConfiguration.getLongPressTimeout(); // ~500ms
    private static final int             CLICK_MIN_DURATION = LONGPRESS_DURATION;                      //  250

    private static final int DRAGFLING_GESTURE_MIN_VELOCITY =  500;
    private static final int   TABVIEW_GESTURE_MIN_VELOCITY = 1000;//5000;

    private static final String                      URDL_U = "UP"   ;
    private static final String                      URDL_R = "RIGHT";
    private static final String                      URDL_D = "DOWN" ;
    private static final String                      URDL_L = "LEFT" ;

    public              long last_ACTION_DOWN_time          = 0L;
    public               int last_ACTION_DOWN_X             = 0;
    public               int last_ACTION_DOWN_Y             = 0;

    public           boolean touched_wvContainer_border;
    private           String gesture_consumed_by_ACTION_UP = null;
    private           String gesture_consumed_by_magnify   = null;
    private           String gesture_consumed_by_onFling   = null;
    public            String gesture_consumed_by_onDown    = null;

    //}}}
    //* EV0_dispatch */
    //{{{
    // {{{
    // LISTENERS
    private MGestureListener        mGestureListener;
    private GestureDetector         mGestureDetector;
    private MotionListener          mMotionListener;

    // DETECTORS
    private ScaleGestureDetector    mScaleGestureDetector;

    // ACTORS
    private boolean             wvTools_is_stealing_events = false;
    private boolean             MWebJS_is_stealing_events  = false;
    private     int                    last_checked_action = MotionEvent.ACTION_DOWN;
    private boolean dispatchTouchEvent_have_to_check_again = true;

    // }}}
    // dispatchTouchEvent_invalidate {{{
    public  void    dispatchTouchEvent_invalidate()
    {
//*EV0_RT_DP*/Settings.MOC(TAG_EV0_RT_DP, "dispatchTouchEvent_invalidate");
        dispatchTouchEvent_have_to_check_again = true; // .. (requests a call to wvTools.wants_to_steal_events)
    }
    //}}}
    // dispatchTouchEvent {{{
    public  boolean dispatchTouchEvent(MotionEvent event)
    {
        MWebJS_is_stealing_events = false;
        // cancel_GUI_idle_notification .. f(ACTION_DOWN) {{{
        String caller = "dispatchTouchEvent";
        int action = event.getActionMasked();
//EV0_RT_DP// Settings.MON(TAG_EV0_RT_DP, caller, "["+Settings.Get_action_name(event)+"]");
        if(action == MotionEvent.ACTION_DOWN)
            cancel_GUI_idle_notification( caller );

        //}}}
        // SANDBOX .. (dismissed by showing sysbars) {{{
        if( is_sandBox_showing )
        {
//System.err.println("is_sandBox_showing");
            if( !is_sysbars_visible(caller) ) {               // showing sysbars is meant toto dismiss SandBox
//System.err.println("...sysbars_visible: false");
                return false;
            }
            else {
//System.err.println("...sysbars_visible: true");
                sync_SUI_visibility( caller );
            }
        }
        // }}}
        // [sustaining_freezed_by_system_bars] {{{
        if( sustaining_freezed_by_system_bars() ) {
//*EV0_RT_DP*/ Settings.MON(TAG_EV0_RT_DP, caller, "consumed_by=[sustaining_freezed_by_system_bars .. SKIPPING EVENT CHAIN: ("+Settings.Get_action_name(event)+")]");
            return true;
        }
        //}}}
        // Handling .. (Motion) (Gesture) (Clamp) {{{
        if(mClamp                == null) mClamp                = new Clamp( this );
        if(mMotionListener       == null) mMotionListener       = new MotionListener();
        if(mGestureListener      == null) mGestureListener      = new MGestureListener();
        if(mGestureDetector      == null) mGestureDetector      = new GestureDetector     (activity, mGestureListener);
        if(mScaleGestureDetector == null) mScaleGestureDetector = new ScaleGestureDetector(activity, new MScaleGestureListener());

        /*...........................................*/ mMotionListener       .onTouchEvent( event ); // [ACTION_DOWN] [ACTION_UP]
        /*(tabs_scrolling_was_allowed_on_ACTION_DOWN)*/ mGestureDetector      .onTouchEvent( event ); // onDown onScroll onFling
        if(tabs_scaling_was_enabled_on_ACTION_DOWN    ) mScaleGestureDetector .onTouchEvent( event ); // onScaleBegin onScroll onScaleEnd

        //}}}
        // STEALING EVENT .. (WVTOOLS may want to steal events from WEBVIEW .. and from [builtin_nb_OnTouchListener] as well! ) {{{
        // (check) {{{
        boolean is_start_event
            =   (action == MotionEvent.ACTION_DOWN);

        boolean is_action_event
            =   (action == MotionEvent.ACTION_POINTER_DOWN)
            ||  (action == MotionEvent.ACTION_POINTER_UP);

        boolean is_change_event
            =   (action != last_checked_action);

        if(!dispatchTouchEvent_have_to_check_again) {
            dispatchTouchEvent_have_to_check_again
                =  is_start_event
                || is_action_event
                || is_change_event
                ;
        }

//EV0_RT_DP*/ Settings.MOM(TAG_EV0_RT_DP, "dispatchTouchEvent_have_to_check_again.=["+ dispatchTouchEvent_have_to_check_again  +"]");
//EV0_RT_DP*/ Settings.MOM(TAG_EV0_RT_DP, ".fs_webview_session_running=["+ fs_webview_session_running +"]");
//EV0_RT_DP*/ Settings.MOM(TAG_EV0_RT_DP, ".is_start_event.=["+ is_start_event  +"]");
//EV0_RT_DP*/ Settings.MOM(TAG_EV0_RT_DP, ".is_action_event=["+ is_action_event +"]");
//EV0_RT_DP*/ Settings.MOM(TAG_EV0_RT_DP, ".is_change_event=["+ is_change_event +"]");

        if(dispatchTouchEvent_have_to_check_again && fs_webview_session_running)
        {
///EV0_RT_DP*/Settings.MOM(TAG_EV0_RT_DP, caller+": ["+Settings.Get_action_name(event)+"]");

            boolean was_wvTools_is_stealing_events = wvTools_is_stealing_events;
            wvTools_is_stealing_events = wvTools.wants_to_steal_events( event );
            last_checked_action        = action;

//*EV0_RT_DP*/ Settings.MOM(TAG_EV0_RT_DP, ".was_wvTools_is_stealing_events=["+ was_wvTools_is_stealing_events +"]");
//*EV0_RT_DP*/ Settings.MOM(TAG_EV0_RT_DP, ".wvTools_is_stealing_events=["+ wvTools_is_stealing_events +"]");

            if( wvTools_is_stealing_events != was_wvTools_is_stealing_events)
            {
//*EV0_RT_DP*/ Settings.MOM(TAG_EV0_RT_DP, caller+": wvTools_is_stealing_events set to ["+wvTools_is_stealing_events+"]");
//*EV0_RT_DP*/ Settings.MOM(TAG_EV0_RT_DP, "...is_start_event.=["+ is_start_event  +"]");
//*EV0_RT_DP*/ Settings.MOM(TAG_EV0_RT_DP, "...is_action_event=["+ is_action_event +"]");
//*EV0_RT_DP*/ Settings.MOM(TAG_EV0_RT_DP, "...is_change_event=["+ is_change_event +"]");
            }
        }
        else {
            wvTools_is_stealing_events = false;
        }
        //}}}
        // (do steal event) .. f(wvTools_is_stealing_events) {{{
        if( wvTools_is_stealing_events )
        {
//*EV0_RT_DP*/ Settings.MOM(TAG_EV0_RT_DP, caller+": EVENT CONSUMED BY: [wvTools_is_stealing_events ("+Settings.Get_action_name(event)+")]");
            return true;
        }
        //}}}
        // (do steal event) .. f(MWebJS_is_stealing_events) {{{
        if( MWebJS_is_stealing_events )
        {
//*EV0_RT_DP*/ Settings.MOM(TAG_EV0_RT_DP, caller+": EVENT CONSUMED BY: [MWebJS_is_stealing_events ("+Settings.Get_action_name(event)+")]");
            return true;
        }
        //}}}
        //}}}
//        // consumed by [WV_TOOL_JS1_SELECT] {{{
//        if(             fs_webview_session_running
//                && (   (gesture_down_SomeView_atXY == fs_webView  )
//                    || (gesture_down_SomeView_atXY == fs_webView2 )
//                    || (gesture_down_SomeView_atXY == fs_webView3 ))
//                && wvTools.property_get( WVTools.WV_TOOL_JS1_SELECT )
//          ) {
///*EV0_RT_DP*/Settings.MOC(TAG_EV0_RT_DP, caller, "consumed_by=[WV_TOOL_JS1_SELECT]");
//
//            return true;
//          }
//        //}}}
//*EV0_RT_DP*/ Settings.MOM(TAG_EV0_RT_DP, caller+": EVENT NOT CONSUMED: event=["+ Settings.Get_action_name(event) +"]");
        return false; // (not consumed) .. (see FullscreenActivity.dispatchTouchEvent)
    }
    //}}}
    //* 1_MotionListener .. (EV2_onMove EV4_on_POINTER EV6_on_UP) */
    // MotionListener {{{
    private class MotionListener
    {
        //{{{
//*EV1_RT_IN*/private boolean may_trace_next_ACTION_MOVE;//EV1_RT_IN

        //}}}
        // onTouchEvent {{{
        public boolean onTouchEvent(MotionEvent event)
        {
             // {{{
            String caller = "MotionListener.onTouchEvent("+Settings.Get_action_name(event)+")";
//*EV1_RT_IN*/Settings.MOC(TAG_EV1_RT_IN, caller);
//EV1_RT_IN//Settings.MOM(TAG_EV1_RT_IN, "fs_webview_session_running=["+fs_webview_session_running+"]");

            String consumed_by  = null;
            int action = event.getActionMasked();
            // }}}
// trace {{{
//*EV1_RT_IN*/switch( action ) {//TAG_EV1_RT_IN
//*EV1_RT_IN*/    case MotionEvent.ACTION_DOWN  : /*..........................................TAG_EV1_RT_IN........*/ may_trace_next_ACTION_MOVE =  true; break;
//*EV1_RT_IN*/    case MotionEvent.ACTION_MOVE  : if(may_trace_next_ACTION_MOVE) trace_views( TAG_EV1_RT_IN, caller); may_trace_next_ACTION_MOVE = false; break;
//*EV1_RT_IN*/    case MotionEvent.ACTION_CANCEL: /*..........................*/ trace_views( TAG_EV1_RT_IN, caller); /*...............................*/ break;
//*EV1_RT_IN*/}//TAG_EV1_RT_IN
///}}}
            switch( action ) {
                case MotionEvent.ACTION_DOWN         :      RTabs_instance.click_callback_has_been_called = false; /*...............*/ break;
                case MotionEvent.ACTION_MOVE         : if(  RTabs_instance.onMove            (event)) consumed_by = "onMove"         ; break;
                case MotionEvent.ACTION_POINTER_DOWN : if(  RTabs_instance.on_POINTER_DOWN   (event)) consumed_by = "on_POINTER_DOWN"; break;
                case MotionEvent.ACTION_POINTER_UP   : if(  RTabs_instance.on_POINTER_UP     (event)) consumed_by = "on_POINTER_UP"  ; break;
                case MotionEvent.ACTION_UP           : if(  RTabs_instance.on_UP             (event)) consumed_by = "on_UP"          ; break;
                case MotionEvent.ACTION_CANCEL       :      RTabs_instance.cancel_long_touch(caller); /*............................*/ break;
            }
            // [return consumed_by] {{{
//*EV1_RT_OK*/ Settings.MON(TAG_EV1_RT_OK, caller, "...return (consumed_by="+consumed_by+")");
            return (consumed_by != null);
            //}}}
        }
        //}}}
    }
    //}}}
    //* 2_MGestureListener .. (EV1_onDown EV3_onScroll EV7_onFling) */
    // MGestureListener {{{
    private class MGestureListener extends GestureDetector.SimpleOnGestureListener
    {
        // [onDown] [onScroll] [onFling] {{{
        @Override public boolean onDown  (MotionEvent event                                               ) { return RTabs_instance.onDown  (event                       ); }
        @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) { return RTabs_instance.onScroll(e1, e2, distanceX, distanceY); }
        @Override public boolean onFling (MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { return RTabs_instance.onFling (e1, e2, velocityX, velocityY); }
        @Override public boolean onDoubleTap(MotionEvent e)
        {
//*SCALE*/String caller = "onDoubleTap";//TAG_SCALE
//*SCALE*/Settings.MOC(TAG_SCALE, caller);
//*SCALE*/Settings.MOM(TAG_SCALE, "...MotionEvent="+ e.toString());
         // @return true if the event is consumed, else false
            return false;
        }
        @Override public boolean onDoubleTapEvent(MotionEvent e)
        {
//*SCALE*/String caller = "onDoubleTapEvent";//TAG_SCALE
//*SCALE*/Settings.MOC(TAG_SCALE, caller);
//*SCALE*/Settings.MOM(TAG_SCALE, "...MotionEvent="+ e.toString());
         // @return true if the event is consumed, else false
            return false;
        }

        //}}}
// (...more to override) {{{
//       public boolean onContextClick       (MotionEvent e) { Settings.MON(TAG_EV1_RT_IN, "MGestureListener.onContextClick"      ); return false; }
//       public boolean onDoubleTap          (MotionEvent e) { Settings.MON(TAG_EV1_RT_IN, "MGestureListener.onDoubleTap"         ); return false; }
//       public boolean onDoubleTapEvent     (MotionEvent e) { Settings.MON(TAG_EV1_RT_IN, "MGestureListener.onDoubleTapEvent"    ); return false; }
//       public boolean onSingleTapConfirmed (MotionEvent e) { Settings.MON(TAG_EV1_RT_IN, "MGestureListener.onSingleTapConfirmed"); return false; }
//       public boolean onSingleTapUp        (MotionEvent e) { Settings.MON(TAG_EV1_RT_IN, "MGestureListener.onSingleTapUp"       ); return false; }
//       public    void onLongPress          (MotionEvent e) { Settings.MON(TAG_EV1_RT_IN, "MGestureListener.onLongPress"         );               }
//       public    void onShowPress          (MotionEvent e) { Settings.MON(TAG_EV1_RT_IN, "MGestureListener.onShowPress"         );               }
//}}}
    }
    //}}}
    //* 3_MScaleGestureListener (EV5_onScale) */
    // MScaleGestureListener {{{
    private class MScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {
        // onScaleBegin {{{
        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector)
        {
            //{{{
             String caller = "MScaleGestureListener.onScaleBegin";
//*EV1_RT_IN*/Settings.MOC(TAG_EV1_RT_IN, caller);

            //}}}
            // [check_scaling_allowed] {{{
            String consumed_by
                = check_scaling_allowed(caller)
                ?  "check_scaling_allowed"
                :  null;

            //}}}
            // [return consumed_by] {{{
//*EV2_RT_CB*/if(consumed_by != null) Settings.MON(TAG_EV2_RT_CB, caller, "...return true (consumed_by="+consumed_by+")");
            return (consumed_by != null);
            //}}}
        }
        //}}}
        // onScale .. [xsv_focusX xsv_focusY] [start_RESCALING] [continue_RESCALING] {{{
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector)
        {
            //{{{
             String caller = "MScaleGestureListener.onScale";
//*EV1_RT_IN*/Settings.MOC(TAG_EV1_RT_IN, caller);

            //}}}
            // [start_RESCALING] {{{
            xsv_focusX = scaleGestureDetector.getFocusX();
            xsv_focusY = scaleGestureDetector.getFocusY();
//System.err.println("onScale: xsv_focusXY=["+xsv_focusX+" "+xsv_focusY+"]");
            if( !rescaling )
            {
                // INTERRUPT ONGOING STAGED PROCESSES
                stop_GLOWING(caller);
                this_RTabsClient.stop_GROUPING(caller);
                start_RESCALING();
            }
            //}}}
            // [continue_RESCALING] {{{
            else {
                float scale = tabs_container.getScaleX() * scaleGestureDetector.getScaleFactor();
                continue_RESCALING( scale );
            }
            //}}}
            // [return consumed_by] {{{
//EV2_RT_CB// Settings.MON(TAG_EV2_RT_CB, caller, "...return consumed_by=[onScale]");

            return true; // consume
            //}}}
        }
        //}}}
        // onScaleEnd .. [hr_end_rescaling] {{{
        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector)
        {
            //{{{
//*EV1_RT_IN*/String caller = "MScaleGestureListener.onScaleEnd";//TAG_EV1_RT_IN
//*EV1_RT_IN*/Settings.MOC(TAG_EV1_RT_IN, caller);

            //}}}
            // [hr_end_rescaling] {{{
        //  handler.re_postDelayed(hr_end_rescaling, 50);
            handler.re_post       (hr_end_rescaling    );
            //}}}
        }
        //}}}
    }
    //}}}
    //* has moved enough */
    //{{{
    // {{{
    private   float track_dx, track_dy;  // gesture distance
    private   float track_px, track_py;  // gesture prevpoint
    public  boolean has_moved_enough;
    // }}}
    // init_has_moved_enough {{{
    private void init_has_moved_enough(float x, float y, String caller)
    {
        caller += "->init_has_moved_enough ("+String.format("%3.1f %3.1f", x, y)+")";
        // init move sensor
        has_moved_enough = false;
        track_px = x; track_py = y; // gesture start prevpoint
        track_dx = 0; track_dy = 0; // gesture distance amount

//*EV2_RT_CB*/Settings.MON(TAG_EV2_RT_CB, caller, "...setting has_moved_enough=["+has_moved_enough+"]");
    }
    //}}}
    // track_has_moved_enough {{{
    private boolean track_has_moved_enough(float x, float y, String caller)
    {
        caller += "->track_has_moved_enough("+String.format("%3.1f %3.1f", x, y)+")";
        // cumulate moves
        track_dx += x-track_px; track_px = x;
        track_dy += y-track_py; track_py = y;

        // assess distance threshold
        //boolean was_moved_enough = has_moved_enough;
        float               dxy  = (float)Math.sqrt(track_dx*track_dx + track_dy*track_dy);
        has_moved_enough = (dxy >= Settings.SCALED_TOUCH_SLOP);

        if( has_moved_enough )
            cancel_long_touch(caller);

//*EV2_RT_CB*/if(has_moved_enough != has_moved_enough) Settings.MON(TAG_EV2_RT_CB, caller, "...return "+has_moved_enough+": .. f(dxy=["+dxy+"] >= "+Settings.SCALED_TOUCH_SLOP+")");
        return has_moved_enough;
    }
    //}}}
    // track_has_moved_URDL {{{
    private String track_has_moved_URDL()
    {
        float abs_dx = Math.abs( track_dx );
        float abs_dy = Math.abs( track_dy );
        float min    = 32;/*//FIXME*/
        return
          ((min > abs_dx) && (abs_dy < min)) ?                   ""
        : (       abs_dx  >   abs_dy       ) ? ((track_dx < 0) ? URDL_L : URDL_R)
        :                                      ((track_dy < 0) ? URDL_U : URDL_D)
        ;
    }
    //}}}
    // has_moved_enough_to_split_left {{{
    private boolean has_moved_enough_to_split_left()
    {
        String caller   = "has_moved_enough_to_split_left";
        boolean result;
        float      dxy  = (float)Math.sqrt(track_dx*track_dx + track_dy*track_dy);
    //  boolean enough  = (dxy >= 30*Settings.SCALED_TOUCH_SLOP);
        int split_count = get_fs_webView_count();
        int split_width = (Settings.SCREEN_W - Settings.WEBVIEW_MARGIN) / (1 + split_count);
        boolean enough  = (dxy >= split_width);
        if(enough && (track_dx < 0))
        {
        //  init_has_moved_enough(track_px+track_dx, track_py+track_dy, caller);
            track_dx = 0; track_dy = 0; // reset gesture distance
            result = true;
        }
        else {
            result = false;
        }
//EV2_RT_CB*/Settings.MON(TAG_EV2_RT_CB, caller, "...return "+result);
        return result;
    }
    //}}}
    // has_moved_enough_to_split_right {{{
    private boolean has_moved_enough_to_split_right()
    {
        String caller   = "has_moved_enough_to_split_right";
        boolean result;
        float      dxy  = (float)Math.sqrt(track_dx*track_dx + track_dy*track_dy);
    //  boolean enough  = (dxy >= 30*Settings.SCALED_TOUCH_SLOP);
        int split_count = get_fs_webView_count();
        int split_width = (Settings.SCREEN_W - Settings.WEBVIEW_MARGIN) / (1 + split_count);
        boolean enough  = (dxy >= split_width);
        if(enough && (track_dx > 0))
        {
        //  init_has_moved_enough(track_px+track_dx, track_py+track_dy, caller);
            track_dx = 0; track_dy = 0; // reset gesture distance
            result = true;
        }
        else {
            result = false;
        }
//EV2_RT_CB*/Settings.MON(TAG_EV2_RT_CB, caller, "...return "+result);
        return result;
    }
    //}}}
    // get_moved_dx get_moved_dy {{{
    public int get_moved_dx(int x) { return x - last_ACTION_DOWN_X; }
    public int get_moved_dy(int y) { return y - last_ACTION_DOWN_Y; }
    //}}}
    // has_moved_more_than_TOOL_BADGE_SIZE {{{
    private boolean has_moved_more_than_TOOL_BADGE_SIZE(float rawX, float rawY)
    {
        String caller   = "has_moved_more_than_TOOL_BADGE_SIZE";
        boolean result;
        float      dx   = rawX - last_ACTION_DOWN_X;
        float      dy   = rawY - last_ACTION_DOWN_Y;
        float      dxy  = (float)Math.sqrt(dx*dx + dy*dy);
        boolean enough  = (dxy >= Settings.TOOL_BADGE_SIZE);
        if (enough )
        {
            result = true;
//*EV2_RT_CB*/Settings.MON(TAG_EV2_RT_CB, caller, "...return "+result+" .. ["+dxy+"]");
        }
        else {
            result = false;
//EV2_RT_CB//Settings.MON(TAG_EV2_RT_CB, caller, "...return "+result+" .. ["+dxy+"]");
        }
        return result;
    }
    //}}}
    //}}}
    //* long touch */
    //{{{
    //{{{
    private static final int     LONG_TOUCH_DELAY                = ViewConfiguration.getLongPressTimeout(); // "~500ms"
    private static final float   ZOOM_SCALE_CYCLE_FACTOR_OUT_2   =   2F;
    private static final float   ZOOM_SCALE_CYCLE_FACTOR_IN_HALF = 0.5F;
    private static final int     ZOOM_SCALE_CYCLE_IN             = 0;
    private static final int     ZOOM_SCALE_CYCLE_OUT            = 1;

    private              boolean toast_again_commited            = false;
    //}}}
    // start_long_touch  {{{
    private void start_long_touch(String caller)
    {
//*EV2_RT_CB*/Settings.MOC(TAG_EV2_RT_CB,"start_long_touch: caller=["+caller+"]");

        handler.re_postDelayed ( hr_long_touch, LONG_TOUCH_DELAY);
        toast_again_commited = false;
    }
    //}}}
    // cancel_long_touch {{{
    private void cancel_long_touch(String caller)
    {
//*EV2_RT_CB*/Settings.MOC(TAG_EV2_RT_CB, "cancel_long_touch: caller=["+caller+"]");

        handler.removeCallbacks( hr_long_touch );

        // CANCEL COMMITED TOAST {{{
//*EV2_RT_CB*/Settings.MOC(TAG_EV2_RT_CB, "...toast_again_commited=["+toast_again_commited+"]");
        if( toast_again_commited )
        {
            toast_again_commited = false;
            toast_cancel();
        }
        //}}}
    }
    //}}}
    // hr_long_touch {{{
    private   final Runnable hr_long_touch = new Runnable() {
        @Override public void run()
        {
            String caller = "hr_long_touch.run";
//*EV2_RT_CB*/Settings.MOC(TAG_EV2_RT_CB,caller+": gesture_down_SomeView_atXY=["+ get_view_name( gesture_down_SomeView_atXY ) +"]");

            if(gesture_down_SomeView_atXY != null) return;

            /* toast_again {{{*/
            toast_again_commited = true;
            toast_again();

            /*}}}*/
            /* [onLongTouchScaleZoomCycle] (180718) {{{*/
//*SCALE*/Settings.MOC(TAG_SCALE, caller+": gesture_down_SomeView_atXY=["+ get_view_name( gesture_down_SomeView_atXY ) +"]");
            if( check_scaling_allowed(caller) )
            {
                rescaled_since_ondown = true;
                scaleZoomCycle();
            }
            /*}}}*/
        }
    };
    //}}}
    //}}}
    //}}}
    //* EV1_onDown */
    //{{{
    // {{{
    private View    gesture_down_DocHisCar_atX = null;
    private View    gesture_down_Wti_or_Wc_atY = null;
    public  View    gesture_down_SomeView_atXY = null;

    private boolean tabs_scaling_was_enabled_on_ACTION_DOWN;
    private boolean tabs_scrolling_was_allowed_on_ACTION_DOWN;

    //}}}
    // onDown {{{
    public boolean onDown(MotionEvent event)
    {
        // (x,y) {{{
        String caller = "onDown";
//*EV1_RT_IN*/Settings.MOC(TAG_EV1_RT_IN, caller);
        float rawX = event.getRawX();
        float rawY = event.getRawY();
        float    x = event.getX();
        float    y = event.getY();

        String consumed_by  = null;
        // }}}

        /**/         onDown_1_init                                (rawX, rawY );
        /**/         onDown_2_get_view                            (x, y       );
        /**/         onDown_3_post_pending_button_to_magnify      (           );
        /**/         onDown_4_CHECK_SCALING_AND_SCROLLING         (           );
        if(          onDown_5_handle_fs_button                    (           )) consumed_by = "onDown_5_handle_fs_button";
        else if(     onDown_6_handle_fg_view                      (x, y, event)) consumed_by = "onDown_6_handle_fg_view";
        else if(     onDown_7_handle_handle                       (x, y, event)) consumed_by = "onDown_7_handle_handle";
        else if(     onDown_8_dragBand                            (x          )) consumed_by = "onDown_8_dragBand";
        else if( fs_webview_session_running) {
            if(      onDown_9_wvTools                             (      event)) consumed_by = "onDown_9_wvTools";
            else if( onDown_10_fs_webView                         (      event)) consumed_by = "onDown_10_fs_webView";
            ////     onDown_11_post_pending_fs_webView_fullscreen (           );
        }

        // [return consumed_by] {{{
//*EV1_RT_IN*/ trace_views(TAG_EV1_RT_IN, "onDown");

//*EV1_RT_OK*/Settings.MON(TAG_EV1_RT_OK, caller, "...return (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    // onDown_1_init {{{
    private void onDown_1_init(float rawX, float rawY)
    {
        // [has_moved_enough] [last_ACTION_DOWN_time] [gesture_consumed_by_ACTION_UP] {{{
        String caller = "onDown_1_init("+(int)rawX+" , "+(int)rawY+")";

        start_long_touch(caller);

        last_ACTION_DOWN_time = System.currentTimeMillis();
        last_ACTION_DOWN_X    = (int)rawX;
        last_ACTION_DOWN_Y    = (int)rawY;

        RTabsClient.onDown_forget_last_sendTabCommand_np_button();

        init_has_moved_enough(rawX, rawY, caller);

        wvTools.clear_fs_search_np_flagged_to_webview_swap_side();
        // }}}
        // HIDE SYSTEM BARS {{{
        if(last_SystemUiHider_vis)
        {
//*EV2_RT_CB*/Settings.MOC(TAG_EV2_RT_CB, "last_SystemUiHider_vis==true");
            hide_system_bars(caller);
        }
        //}}}
        // INIT EVENT-HANDLED_BY_FLAGS ON ACTION_DOWN {{{
//*EV2_RT_CB*/Settings.MOC(TAG_EV2_RT_CB, "INIT EVENT-HANDLED_BY_FLAGS ON ACTION_DOWN ");
        gesture_consumed_by_ACTION_UP                    =  null;
        gesture_consumed_by_magnify                      =  null;
        gesture_consumed_by_onFling                      =  null;
        gesture_consumed_by_onDown                       =  null;

        tabs_scaling_was_enabled_on_ACTION_DOWN          =  true; // until further examination
        tabs_scrolling_was_allowed_on_ACTION_DOWN        =  true; // until further examination
        rescaled_since_ondown                            =  false;
        //}}}
        // INIT SCROLL PROGRESS COUNTERS ON ACTION_DOWN {{{
        onScroll_counters_reset();

        //}}}
        // CANCEL PENDING HIDE {{{
        //handler.removeCallbacks( hr_fold_dock     );
        //handler.removeCallbacks( hr_fold_histBand );
        //handler.removeCallbacks( hr_fold_cartBand );

        //}}}
    }
    //}}}
    // onDown_2_get_view {{{
    private void onDown_2_get_view(float x, float y)
    {
        // VIEWS [X Y XY] {{{
//*EV2_RT_CB*/String caller = "onDown_2_get_view("+(int)x+","+(int)y+")";//TAG_EV2_RT_CB
//*EV2_RT_CB*/Settings.MOC(TAG_EV2_RT_CB, caller);

        gesture_down_DocHisCar_atX = null ; // event target view from left to right
        gesture_down_Wti_or_Wc_atY = null ; // -or-  target view from top to bottom
        gesture_down_SomeView_atXY = null ; // -or-  target view at  XY ACTION_DOWN

        // TOUCH VIEWS CHECK [ X    ] [dock] [hist] [cart] {{{
        gesture_down_DocHisCar_atX = get_DocHisCar_atX((int)x);

//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...gesture_down_DocHisCar_atX =["+ get_view_name( gesture_down_DocHisCar_atX ) +"]");
        // }}}
        // TOUCH VIEWS CHECK [  Y   ]  [WTITLE] or [wvContainer] {{{
        gesture_down_Wti_or_Wc_atY = get_wtitle_or_container_atY((int)x, (int)y);

//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...gesture_down_Wti_or_Wc_atY =["+ get_view_name( gesture_down_Wti_or_Wc_atY ) +"]");
        //}}}
        // TOUCH VIEWS CHECK [    XY] [wvContainer] [fs_webViewX] [cart hist] [top_handle mid_handle bot_handle] {{{
        gesture_down_SomeView_atXY = get_FF_W_C_HH_T_atXY((int)x, (int)y);
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB,"get_FF_W_C_HH_T_atXY returned: ["+ get_view_name( gesture_down_SomeView_atXY ) +"]");
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB,"...is_tabs_scrolling()=["+is_tabs_scrolling()+"]");
        //}}}
        // }}}
        // WEBVIEW FRAME [wvContainer] .. (none of the contained or surrounding web tools) {{{
        touched_wvContainer_border
            =  (gesture_down_SomeView_atXY == wvContainer)
            &&  is_view_showing( wvContainer )
            ;
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "touched_wvContainer_border=["+touched_wvContainer_border+"]");

        //}}}
    }
    //}}}
    // onDown_3_post_pending_button_to_magnify {{{
    private void onDown_3_post_pending_button_to_magnify()
    {
        // [post_pending_button_to_magnify] f(can_magnify_gesture_down_F_W_C_H_H_atXY) {{{
        // Note: (OnLongClickListener of disabled button wont be called)

        if( can_magnify_gesture_down_F_W_C_H_H_atXY() )
            post_pending_button_to_magnify((NpButton)gesture_down_SomeView_atXY);

        //}}}
    }
    //}}}
    // onDown_4_CHECK_SCALING_AND_SCROLLING {{{
    private void onDown_4_CHECK_SCALING_AND_SCROLLING()
    {
        // (check_scaling_allowed) (check_scrolling_allowed) {{{
        String caller = "onDown_4_CHECK_SCALING_AND_SCROLLING";

        // ALLOW or disallow SCALING .. f(magnified view, fg_view, hist, dock, handle, seekers, SandBox)
        tabs_scaling_was_enabled_on_ACTION_DOWN   = check_scaling_allowed  (caller);

        // ALLOW or disallow SCROLLING .. f(fg_view fs_webViewX drag_band containers(controls, log, wvContainer) )
        tabs_scrolling_was_allowed_on_ACTION_DOWN = check_scrolling_allowed(caller);

//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...tabs_scrolling_was_allowed_on_ACTION_DOWN=["+ tabs_scrolling_was_allowed_on_ACTION_DOWN +"]");
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...tabs_scaling_was_enabled_on_ACTION_DOWN..=["+ tabs_scaling_was_enabled_on_ACTION_DOWN   +"]");
        //}}}
    }
    //}}}
    // onDown_5_handle_fs_button {{{
    private boolean onDown_5_handle_fs_button()
    {
        // (fs_button) {{{
        if((fs_button == null) || (gesture_down_SomeView_atXY != fs_button)) return false;

//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "onDown_5_handle_fs_button: ...return true");
        return true;
        //}}}
    }
    //}}}
    // onDown_6_handle_fg_view {{{
    private boolean onDown_6_handle_fg_view(float x, float y, MotionEvent event)
    {
        // (fg_view) (fg_view_np) {{{
        if( !is_fg_view_showing()  ) return false;

        String caller = "onDown_6_handle_fg_view";

        NotePane fg_view_np  = this_RTabsClient.get_AUTO_np_at_xy_closest((int)x, (int)y, caller);
        if(      fg_view_np == null) return false;

        //}}}
        // [mClamp.onTouchEvent] {{{

        mClamp.onTouchEvent(fg_view_np, event);

        mClamp.set_gravity_fx( Clamp.DEFAULT_GRAVITY_FX );

        gesture_consumed_by_onDown = caller;

//*EV2_RT_CB*/Settings.MON(TAG_EV2_RT_CB, caller, "...return true .. (mClamp will handle it from this point on)");
        return true;
        //}}}
    }
    //}}}
    // onDown_7_handle_handle {{{
    private boolean onDown_7_handle_handle(float x, float y, MotionEvent event)
    {
        // look for some showing [handle_to_dismiss] {{{
        String caller = "onDown_7_handle_handle";
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, caller);

//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...is_handle_showing(top_handle)=["+is_handle_showing(top_handle)+"]");
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...is_handle_showing(mid_handle)=["+is_handle_showing(mid_handle)+"]");
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...is_handle_showing(bot_handle)=["+is_handle_showing(bot_handle)+"]");

        Handle handle_to_dismiss = null;
        if     ( is_handle_showing(top_handle) ) handle_to_dismiss = top_handle;
        else if( is_handle_showing(mid_handle) ) handle_to_dismiss = mid_handle;
        else if( is_handle_showing(bot_handle) ) handle_to_dismiss = bot_handle;
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...handle_to_dismiss=["+ get_view_name(handle_to_dismiss) +"]");

        if(handle_to_dismiss != null)
        {
            Rect r = new Rect();
            handle_to_dismiss.getHitRect( r );
            if( r.contains((int)x,(int)y) )
            {
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...handle_to_dismiss=["+ get_view_name(handle_to_dismiss) +"]");
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...touching inside: DO NOT DISMISS .. (let handle do the processing by itself)");
                handle_to_dismiss = null;
            }
        }
        //}}}
        // handle_hide (found handle_to_dismiss) {{{
        String consumed_by = null;
        if(handle_to_dismiss != null)
        {
            consumed_by = "handle_to_dismiss";

//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...calling handle_hide");
            handle_hide(caller);

//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...setting [gesture_consumed_by_onDown]");
            gesture_consumed_by_onDown = caller;
        }
        //}}}
        // [return consumed_by] {{{
//*EV2_RT_CB*/if(consumed_by != null) Settings.MOC(TAG_EV2_RT_CB, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    // onDown_8_dragBand {{{
    private boolean onDown_8_dragBand(float x)
    {
        // BAND RELEASE [drag_band] <= f(touching one of the GUI view) {{{
        String caller = "onDown_8_dragBand";

        if(        (gesture_down_DocHisCar_atX != null)
                || (gesture_down_Wti_or_Wc_atY != null)
                || (gesture_down_SomeView_atXY != null)
          ) {
            if(drag_band != null)
                clear_dragBand(caller);
        }
        // }}}
        // BAND SELECT [drag_band] <= f(event x) and (WEBVIEW NOT SHOWING) {{{
        else if( !fs_webview_session_running)
        {
            select_dragBand((int)x, caller);
        }
        // }}}
        // [return consumed_by] {{{
        String consumed_by = null;
        if(drag_band != null) consumed_by = "(drag_band != null)";
//*EV2_RT_CB*/if(consumed_by != null) Settings.MON(TAG_EV2_RT_CB, caller, "...return true (consumed_by="+consumed_by+") .. drag_band=["+get_view_name(drag_band)+"]");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    // onDown_9_wvTools {{{
    private boolean onDown_9_wvTools(MotionEvent event)
    {
        // [wvTools.onDown] {{{
        String caller = "onDown_9_wvTools";
//*EV2_RT_CB*/Settings.MON(TAG_EV2_RT_CB, caller, "TOUCHED ["+get_view_name( gesture_down_SomeView_atXY )+"]");

        String consumed_by = null;
        if( wvTools.onDown( event ) ) consumed_by = "wvTools.onDown";

        //}}}
        // [return consumed_by] {{{
//*EV2_RT_CB*/if(consumed_by != null) Settings.MON(TAG_EV2_RT_CB, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    // onDown_10_fs_webView {{{
    private boolean onDown_10_fs_webView(MotionEvent event)
    {
        if( !is_a_webView(gesture_down_SomeView_atXY) ) return false;
        String caller = "onDown_10_fs_webView";
//*EV1_RT_IN*/Settings.MOC(TAG_EV1_RT_IN, caller);

        MWebView wv = (MWebView)gesture_down_SomeView_atXY;

        String consumed_by =
            wv.onEventJS(event, (MWebView)gesture_down_SomeView_atXY)
            ? "onEventJS"
            :  null;

//*EV2_RT_CB*/if(consumed_by != null) Settings.MON(TAG_EV2_RT_CB, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
    }
    //}}}
//    // onDown_11_post_pending_fs_webView_fullscreen {{{
//    private void onDown_11_post_pending_fs_webView_fullscreen()
//    {
//        // [is_a_webView] {{{
//        if( !is_a_webView( gesture_down_SomeView_atXY ) ) return;
//
//        String caller = "onDown_11_post_pending_fs_webView_fullscreen";
//        //}}}
//        // [post_pending_fs_webView_fullscreen] {{{
//        if(        (gesture_down_SomeView_atXY != null)
//                && (gesture_down_SomeView_atXY instanceof MWebView)
//          ) {
///*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, caller+": ...calling post_pending_fs_webView_fullscreen=["+ get_view_name( gesture_down_SomeView_atXY ) +"]");
//
//            post_pending_fs_webView_fullscreen((MWebView)gesture_down_SomeView_atXY);
//
//          }
//        //}}}
//    }
//    //}}}
    // get_onDown_view_names {{{
    public String get_onDown_view_names()
    {
        String xy = (gesture_down_SomeView_atXY == null) ? "" : "XY=["+ get_view_name( gesture_down_SomeView_atXY ) +"]";
        String x  = (gesture_down_DocHisCar_atX == null) ? "" : " X=["+ get_view_name( gesture_down_DocHisCar_atX ) +"]";
        String  y = (gesture_down_Wti_or_Wc_atY == null) ? "" : " Y=["+ get_view_name( gesture_down_Wti_or_Wc_atY ) +"]";
        String b  = (                 drag_band == null) ? "" : " B=["+ get_view_name( drag_band                  ) +"]";
        return xy + x + y + b;
    }
    //}}}
    // handling_gesture_down_SomeView_atXY {{{
    public boolean handling_gesture_down_SomeView_atXY()
    {
//*EV2_RT_CB*/String caller = "handling_gesture_down_SomeView_atXY";//TAG_EV2_RT_CB

        String handling_some_view
            =  (gesture_down_SomeView_atXY != null) && (gesture_down_SomeView_atXY == fs_button)
            ?   "fs_button"
            :   null;

//*EV2_RT_CB*/if(handling_some_view != null) Settings.MON(TAG_EV2_RT_CB, caller, "...return true (handling_some_view="+handling_some_view+")");
        return (handling_some_view != null);
    }
    //}}}
    //}}}
    //* EV2_onMove */
    //{{{
    // onMove {{{
    public boolean onMove(MotionEvent event)
    {
        // [rawX] [rawY] {{{
        String caller = "onMove";
//*EV1_RT_IN*/Settings.MOC(TAG_EV1_RT_IN, caller);

/*
        if( is_a_webView(gesture_down_SomeView_atXY) )
        {
            MWebView wv  = (MWebView)gesture_down_SomeView_atXY;
            if((     wv != null) && wv.scroll_off) {
//System.err.println(caller+": SCROLL OFF");
                MWebJS_is_stealing_events = true;
                return true;
            }
        }
*/
        float rawX = event.getRawX();
        float rawY = event.getRawY();

        String consumed_by = null;
        //}}}
//        /* onMove_0_rescaled_since_ondown {{{*/
//        if(rescaled_since_ondown) {
//            consumed_by = onMove_0_rescaled_since_ondown(rawX, rawY); if(consumed_by == null) consumed_by = "rescaled_since_ondown";
//
//        }
//        /*}}}*/
        if(consumed_by == null)
        {
            /**/          onMove_1_hide_system_bars      (          );
            /**/          onMove_2_track_has_moved_enough(rawX, rawY);
            /**/          onMove_3_mClamp                (event     );
            if     (      onMove_4_fs_button             (          )) consumed_by = "onMove_4_fs_button";
            else if(      fs_webview_session_running) {
//*EV1_RT_IN*/Settings.MOM(TAG_EV1_RT_IN, "...fs_webview_session_running=["+fs_webview_session_running+"]");
              //if(       onMove_5_WEBVIEW_SWAP          (event     )) consumed_by = "onMove_5_WEBVIEW_SWAP";
                if     (  onMove_6_wvTools               (event     )) consumed_by = "onMove_6_wvTools";
                else if(  onMove_7_webView_HIDE_TOOLS    (rawX, rawY)) consumed_by = "onMove_7_webView_HIDE_TOOLS";
              //else if(  onMove_8_webView_JAVASCRIPT    (event     )) consumed_by = "onMove_8_webView_JAVASCRIPT"; // TODO
            }
        }
        // [return consumed_by] {{{
//*EV1_RT_OK*/Settings.MOC(TAG_EV1_RT_OK, caller, "...return (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
//    // onMove_0_rescaled_since_ondown {{{
//    private String onMove_0_rescaled_since_ondown(float rawX, float rawY)
//    {
//        /* rescaled_since_ondown {{{*/
//        String caller = "onMove_0_rescaled_since_ondown";
//
//        /*}}}*/
//        /* last_handeled_move_time has_moved_enough {{{*/
//
//        long this_move_time = System.currentTimeMillis();
//        if(( this_move_time - last_handeled_move_time) < 2000) return null;
//
//        track_has_moved_enough(rawX, rawY, caller);
//        if( !has_moved_enough )                                                  return null;
//
//        /*}}}*/
//        /* SCALE-MIN SCALE-MAX ZOOM-IN ZOOM-OUT {{{*/
//        String consumed_by = "";
//
//        last_handeled_move_time = this_move_time;
//
//        String   has_moved_URDL = track_has_moved_URDL();
//        switch(  has_moved_URDL )
//        {
///*{{{
//            case URDL_U:
//                Settings.DEV_SCALE  =  Settings.DEV_SCALE_MIN;
//                consumed_by = "rescaled_since_ondown: SCALE MIN ["+Settings.DEV_SCALE+"]";
//
//                continue_RESCALING(1F);
//                handler.re_post( hr_end_rescaling );
//                break;
//
//            case URDL_D:
//                Settings.DEV_SCALE  =  Settings.DEV_SCALE_MAX;
//                consumed_by = "rescaled_since_ondown: SCALE MAX ["+Settings.DEV_SCALE+"]";
//
//                continue_RESCALING(1F);
//                handler.re_post( hr_end_rescaling );
//                break;
//
//}}}*/
//            case URDL_R:
//            case URDL_L:
//                zooming_in  = (has_moved_URDL == URDL_R) /* RIGHT==IN .. LEFT==OUT */
//                    ||        (has_moved_URDL == URDL_U) /*    UP==IN .. DOWN==OUT */
//                    ;
//                consumed_by = "rescaled_since_ondown: ...moving "+ has_moved_URDL +".. zooming_in "+ zooming_in;
//
//                continue_ZOOMING();
//                handler.re_post( hr_end_rescaling );
//                init_has_moved_enough(rawX, rawY, caller);
//                break;
//
//            //default: consumed_by = "rescaled_since_ondown: NOT MOVED ENOUGH";
//        }
//
//        /*}}}*/
///*SCALE*/if(consumed_by != null) Settings.MOC(TAG_SCALE, caller, "...return (consumed_by="+consumed_by+")");
//        return consumed_by;
//    }
////}}}
    // onMove_1_hide_system_bars {{{
    private void onMove_1_hide_system_bars()
    {
        // (visible system bars) .. (not freezed) {{{
        String caller = "onMove_1_hide_system_bars";

        if( !is_sysbars_visible(caller)         ) return;
        if( sustaining_freezed_by_system_bars() ) return;
        ///}}}
        // [hide_system_bars] {{{
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, caller+": ...calling hide_system_bars");

        hide_system_bars(caller);
        //}}}
    }
    //}}}
    // onMove_2_track_has_moved_enough {{{
    private void onMove_2_track_has_moved_enough(float rawX, float rawY)
    {
        // [has_moved_enough] {{{

        if( has_moved_enough ) return; // already effective

        String caller = "onMove_2_track_has_moved_enough";
        track_has_moved_enough(rawX, rawY, caller);

        if( !has_moved_enough ) return; // unchanged
        //}}}
        // ...[cancel_pending_button_to_magnify] {{{
        if(held_button_to_magnify != null)
        {
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, caller+": ...calling cancel_pending_button_to_magnify");

            cancel_pending_button_to_magnify(caller);
        }
        //}}}
        // ...[cancel_pending_fs_webView_fullscreen] {{{
        if( is_fs_webView_fullscreen_still_pending(caller))
        {
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, caller+": ...calling cancel_pending_fs_webView_fullscreen");

            cancel_pending_fs_webView_fullscreen(caller);
        }
        //}}}
    }
    //}}}
    // onMove_3_mClamp {{{
    private void onMove_3_mClamp(MotionEvent event)
    {
        // (mClamp.moving_np) {{{
        if(mClamp.moving_np == null) return;

        //}}}
        // [mClamp.onTouchEvent] {{{
//*EV2_RT_CB*/Settings.MOC(TAG_EV2_RT_CB, "onMove_3_mClamp", "mClamp.moving_np=["+mClamp.moving_np+"]");
        mClamp.onTouchEvent(event); // (ACTION_MOVE)

        //}}}
    }
    //}}}
    // onMove_4_fs_button {{{
    private boolean onMove_4_fs_button()
    {
        // TOUCHED [fs_button] {{{
        String caller = "onMove_4_fs_button";

        String consumed_by
            =  (gesture_down_SomeView_atXY != null) && (gesture_down_SomeView_atXY == fs_button)
            ?   "fs_button"
            :   null;

//*EV2_RT_CB*/if(consumed_by != null) Settings.MON(TAG_EV2_RT_CB, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
//    // onMove_5_WEBVIEW_SWAP {{{
//    private boolean onMove_5_WEBVIEW_SWAP(MotionEvent event)
//    {
//        // (MOVE ON EXPANDED WEBVIEW) {{{
//        if( !has_moved_enough                              ) return false;
//        if( !fs_search_can_select_webview_on_ACTION_MOVE() ) return false;
//
///*EV2_RT_CB*/String caller = "onMove_5_WEBVIEW_SWAP";//TAG_EV2_RT_CB
///*EV2_RT_CB*/Settings.MOC(TAG_EV2_RT_CB, caller);
///*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "..............................has_moved_enough=["+has_moved_enough+"]");
///*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...fs_search_can_select_webview_on_ACTION_MOVE=["+fs_search_can_select_webview_on_ACTION_MOVE()+"]");
//
//        //}}}
//        // [WEBVIEW 1 2 3 SELECT] {{{
///*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, caller+": ...calling expand_fs_webView_fullscreen_select_atX");
//        expand_fs_webView_fullscreen_select_atX(event.getRawX(), false); // !toggle-off
//
///*EV2_RT_CB*/Settings.MON(TAG_EV2_RT_CB, caller, "...return true");
//        return true;
//        //}}}
//    }
//    //}}}
    // onMove_6_wvTools {{{
    private boolean onMove_6_wvTools(MotionEvent event)
    {
        // (has_moved_enough) .. (wvTools.can_move_something) {{{
        if( !has_moved_enough             ) return false;
        if( !wvTools.can_move_something() ) return false;

//*EV2_RT_CB*/String caller = "onMove_6_wvTools";//TAG_EV2_RT_CB
        //}}}
        // [wvTools.onMove] {{{
        String consumed_by
            =   wvTools.onMove( event )
            ?    "wvTools.onMove"
            :    null;

//*EV2_RT_CB*/if(consumed_by != null) Settings.MON(TAG_EV2_RT_CB, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    // onMove_7_webView_HIDE_TOOLS  {{{
    private boolean onMove_7_webView_HIDE_TOOLS(float rawX, float rawY)
    {
        // (some wvtools to hide when not in use) {{{
        String caller = "onMove_7_webView_HIDE_TOOLS";//TAG_EV2_RT_CB

        if( !wvTools.sb_is_frame_showing()                           ) return false; // NO WV TOOL TO HIDE
        if( !fs_search_can_select_webview_on_ACTION_MOVE()           ) return false; // FS_SEARCH AT WORK
        if( !wvTools.has_view( gesture_down_SomeView_atXY   , caller)) return false; // NOT A WVTOOLS TOUCHED
        if( !has_moved_more_than_TOOL_BADGE_SIZE(rawX, rawY)         ) return false; // NOT MOVED ENOUGH

        //}}}
        // [wvTools.sb_hide_tools] {{{
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, caller+": ...calling wvTools.sb_hide_tools");

        wvTools.sb_hide_tools( caller );
        //}}}
        return false;
    }
    //}}}
    // onMove_8_webView_JAVASCRIPT {{{
    private boolean onMove_8_webView_JAVASCRIPT(MotionEvent event)
    {
        if( !is_a_webView(gesture_down_SomeView_atXY) ) return false;
        String caller = "onMove_8_webView_JAVASCRIPT";

        MWebView wv = (MWebView)gesture_down_SomeView_atXY;
        String consumed_by =
            wv.onEventJS(event, (MWebView)gesture_down_SomeView_atXY)
            ? "onEventJS"
            :  null;

//*EV2_RT_CB*/if(consumed_by != null) Settings.MON(TAG_EV2_RT_CB, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
    }
    //}}}
    //}}}
    //* EV3_onScroll */
    //{{{
    //{{{
    private static final  int MOVING_COUNT                      =     3;

    private static final  int DISTANCE_PICK_X_MIN               =     5;
    private static final  int DISTANCE_PICK_Y_MIN               =     5;
    private static final  int DISTANCE_DRAG_X_MIN               =    10;
    private static final  int DISTANCE_DRAG_Y_MIN               =     3;

    private static final  int DRAG_TICK_INTERVAL_MIN            =   500;
    private              long last_handeled_move_time           =    0L;

    private               int moving_y_in_same_direction_count  =     0;
    private               int moving_x_in_same_direction_count  =     0;

    private           boolean switch_to_dock                    = false;
    private           boolean switch_to_hist                    = false;
    private           boolean scroll_to_profile                 = false;
    private           boolean show_profile_hist                 = false;
    private           boolean scroll_to_visited                 = false;
    private           boolean was_fully_hidden                  = false;
    private           boolean was_fully_visible                 = false;

    //}}}
    // onScroll {{{
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
        // (rescaling) {{{
        String caller = "onScroll";
//*EV1_RT_IN*/Settings.MOC(TAG_EV1_RT_IN, caller);

        if( rescaling                                 ) return false;
    //  if( drag_band == null                         ) return false;
    //  if( tabs_scrolling_was_allowed_on_ACTION_DOWN ) return false;

//EV3_RT_SC//Settings.MOM(TAG_EV3_RT_SC, "...distanceY=["+distanceY+"]");

        String consumed_by = null;
        //}}}

        if     ( onScroll_1_WVTOOLS          (e2, distanceX, distanceY) ) consumed_by = "onScroll_1_WVTOOLS";
        else if( onScroll_2_HIST_DOCK_PROFILE(    distanceX, distanceY) ) consumed_by = "onScroll_2_HIST_DOCK_PROFILE";
        else if( onScroll_3_BAND             (    distanceX, distanceY) ) consumed_by = "onScroll_3_BAND";

        // [return consumed_by] {{{
//*EV3_RT_SC*/ Settings.MON(TAG_EV3_RT_SC, caller, "...return (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    // onScroll_counters_reset {{{
    public  void onScroll_counters_reset()
    {
        moving_x_in_same_direction_count    = 0;
        moving_y_in_same_direction_count    = 0;

        last_handeled_move_time             = System.currentTimeMillis();

        switch_to_dock                      = false;
        switch_to_hist                      = false;
        scroll_to_profile                   = false;
        show_profile_hist                   = false;
        scroll_to_visited                   = false;
    }
    //}}}
    // onScroll_parse_counters {{{
// {{{
//*EV3_RT_SC*/private final int TRACE_MASK_was_fully_hidden  = 0x001;//TAG_EV3_RT_SC
//*EV3_RT_SC*/private final int TRACE_MASK_was_fully_visible = 0x002;//TAG_EV3_RT_SC
//*EV3_RT_SC*/private final int TRACE_MASK_moving_L          = 0x004;//TAG_EV3_RT_SC
//*EV3_RT_SC*/private final int TRACE_MASK_moving_R          = 0x008;//TAG_EV3_RT_SC
//*EV3_RT_SC*/private final int TRACE_MASK_moving_Y          = 0x010;//TAG_EV3_RT_SC
//*EV3_RT_SC*/private final int TRACE_MASK_trigger_dock      = 0x020;//TAG_EV3_RT_SC
//*EV3_RT_SC*/private final int TRACE_MASK_trigger_hist      = 0x040;//TAG_EV3_RT_SC
//*EV3_RT_SC*/private final int TRACE_MASK_scroll_to_profile = 0x080;//TAG_EV3_RT_SC
//*EV3_RT_SC*/private final int TRACE_MASK_scroll_to_visited = 0x100;//TAG_EV3_RT_SC
//*EV3_RT_SC*/private final int TRACE_MASK_show_profile_hist = 0x200;//TAG_EV3_RT_SC
// }}}
    private void onScroll_parse_counters(float distanceX, float distanceY)
    {
        String caller = "onScroll_parse_counters";
//EV3_RT_SC*/Settings.MON(TAG_EV3_RT_SC, caller);
        // SENSITIVITY (LANDING .. DRAGGING) .. [moves] [distance] {{{
        int             moving_count;
        int           distance_x_min;
        int           distance_y_min;
        if( !dragBand_moved )
        {
            moving_count             = 1;
            distance_x_min           = DISTANCE_PICK_X_MIN;
            distance_y_min           = DISTANCE_PICK_Y_MIN;
        }
        else {
            moving_count             = MOVING_COUNT;
            distance_x_min           = DISTANCE_DRAG_X_MIN;
            distance_y_min           = DISTANCE_DRAG_Y_MIN;
        }
        //}}}
        // PROBE (MOVE THRESHOLDS BEFORE) {{{
        boolean moving_L =          (moving_x_in_same_direction_count  <= moving_count);
        boolean moving_R =          (moving_x_in_same_direction_count  >= moving_count);
        boolean moving_Y = (Math.abs(moving_y_in_same_direction_count) >= moving_count);

        //}}}
// TRACE (counters before) {{{
//*EV3_RT_SC*/int old_state  = 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    old_state += was_fully_hidden  ? TRACE_MASK_was_fully_hidden  : 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    old_state += was_fully_visible ? TRACE_MASK_was_fully_visible : 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    old_state += moving_L          ? TRACE_MASK_moving_L          : 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    old_state += moving_R          ? TRACE_MASK_moving_R          : 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    old_state += moving_Y          ? TRACE_MASK_moving_Y          : 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    old_state += switch_to_dock    ? TRACE_MASK_trigger_dock      : 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    old_state += switch_to_hist    ? TRACE_MASK_trigger_hist      : 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    old_state += scroll_to_profile ? TRACE_MASK_scroll_to_profile : 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    old_state += scroll_to_visited ? TRACE_MASK_scroll_to_visited : 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    old_state += show_profile_hist ? TRACE_MASK_show_profile_hist : 0;//TAG_EV3_RT_SC
// }}}
    // SENSE (BAND VISIBILITY STATE) {{{
    was_fully_hidden                  = is_band_fully_hidden (drag_band);
    was_fully_visible                 = is_band_fully_visible(drag_band);

    // }}}
    // SENSE (MOVE THRESHOLDS) .. [moving_Y] [moving_L] [moving_R] {{{
    // HORIZONTAL SAMPLES COUNT .. [LEFT] [RIGHT]
    if(Math.abs(distanceX) > distance_x_min)
    {
        boolean moving_right                                                         = (distanceX < 0);
        if(was_fully_visible || was_fully_hidden) moving_x_in_same_direction_count  += moving_right ? 1 : -1;
        else                                      moving_x_in_same_direction_count   = 0;
    }

    // VERTICAL SAMPLES COUNT .. [Y]
    if(Math.abs(distanceY) > distance_y_min)
    {
        boolean moving_down                                                          = (distanceY < 0);
        moving_y_in_same_direction_count                                            += moving_down  ? 1 : -1;
    }

    // MOVE DIRECTION
    moving_Y = (Math.abs(moving_y_in_same_direction_count) >= moving_count);
    moving_R =          (moving_x_in_same_direction_count  >= moving_count);
    moving_L =          (moving_x_in_same_direction_count  <= moving_count);

    // BAND
    if( slide_band_anim_queue_is_empty() )
    {
        switch_to_dock  = (drag_band == hist_band) && moving_R &&  was_fully_visible ;
        switch_to_hist  = (drag_band == dock_band) && moving_L &&  was_fully_hidden  ;
    }

    // PROFILE
    scroll_to_profile   = (drag_band == dock_band) && moving_R && !was_fully_hidden  ;
    scroll_to_visited   = (drag_band == dock_band) && moving_Y && !was_fully_hidden && !dock_band.is_scroll_animation_running();
//  show_profile_hist   = (drag_band == dock_band) && moving_R &&  was_fully_visible ;
//  ... disturbs scroll to current profile
    // }}}
// TRACE (counters after){{{

//{{{
//EV3_RT_SC*/Settings.MON("       DY=["+ distance_y_min                          +"]");
//EV3_RT_SC*/Settings.MON("    MOVE#=["+ moving_count                            +"]");
//EV3_RT_SC*/Settings.MON("   HIDDEN=["+ was_fully_hidden                        +"]");
//EV3_RT_SC*/Settings.MON(" moving_Y=["+ moving_Y                                +"]");
//EV3_RT_SC*/Settings.MON("     ANIM=["+ dock_band.is_scroll_animation_running() +"]");
//}}}

//*EV3_RT_SC*/int new_state  = 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    new_state += was_fully_hidden  ? TRACE_MASK_was_fully_hidden  : 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    new_state += was_fully_visible ? TRACE_MASK_was_fully_visible : 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    new_state += moving_L          ? TRACE_MASK_moving_L          : 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    new_state += moving_R          ? TRACE_MASK_moving_R          : 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    new_state += moving_Y          ? TRACE_MASK_moving_Y          : 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    new_state += switch_to_dock    ? TRACE_MASK_trigger_dock      : 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    new_state += switch_to_hist    ? TRACE_MASK_trigger_hist      : 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    new_state += scroll_to_profile ? TRACE_MASK_scroll_to_profile : 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    new_state += scroll_to_visited ? TRACE_MASK_scroll_to_visited : 0;//TAG_EV3_RT_SC
//*EV3_RT_SC*/    new_state += show_profile_hist ? TRACE_MASK_show_profile_hist : 0;//TAG_EV3_RT_SC

//*EV3_RT_SC*/ if(old_state != new_state) {//TAG_EV3_RT_SC
//EV3_RT_SC*/ trace_open(TAG_EV3_RT_SC   , caller); // ...works better without for a strike of those logs
//*EV3_RT_SC*/ _trace_boolean_transition("was_fully_hidden"                 , was_fully_hidden                 );//TAG_EV3_RT_SC
//*EV3_RT_SC*/ _trace_boolean_transition("was_fully_visible"                , was_fully_visible                );//TAG_EV3_RT_SC
//*EV3_RT_SC*/ _trace_boolean_transition("switch_to_dock"                   , switch_to_dock                   );//TAG_EV3_RT_SC
//*EV3_RT_SC*/ _trace_boolean_transition("switch_to_hist"                   , switch_to_hist                   );//TAG_EV3_RT_SC
//*EV3_RT_SC*/ _trace_boolean_transition("scroll_to_profile"                , scroll_to_profile                );//TAG_EV3_RT_SC
//*EV3_RT_SC*/ _trace_boolean_transition("show_profile_hist"                , show_profile_hist                );//TAG_EV3_RT_SC
//*EV3_RT_SC*/ _trace_boolean_transition("scroll_to_visited"                , scroll_to_visited                );//TAG_EV3_RT_SC
//EV3_RT_SC*/ trace_close(TAG_EV3_RT_SC); // ...works better without for a strike of those logs
//*EV3_RT_SC*/ }//TAG_EV3_RT_SC

//}}}
    }
    //}}}
        // onScroll_1_WVTOOLS {{{
        private boolean onScroll_1_WVTOOLS(MotionEvent e2, float distanceX, float distanceY)
        {
            // [wvTools.onScroll] {{{
            String caller = "onScroll_1_WVTOOLS";

            String consumed_by
                =   wvTools.onScroll(e2, distanceX, distanceY)
                ?    "wvTools.onScroll"
                :    null;

            //}}}
            // [return consumed_by] {{{
//*EV3_RT_SC*/if(consumed_by != null) Settings.MON(TAG_EV3_RT_SC, caller, "...return true (consumed_by="+consumed_by+")");
            return (consumed_by != null);
            //}}}
        }
        //}}}
        // onScroll_2_HIST_DOCK_PROFILE {{{
        private boolean onScroll_2_HIST_DOCK_PROFILE(float distanceX, float distanceY)
        {
            // . [onScroll_parse_counters] {{{
            String caller = "onScroll_2_HIST_DOCK_PROFILE";
            String consumed_by = null;

            // KEEP MAJOR MOVE AXIS ONLY .. (clear minor direction move)
            if(Math.abs(distanceX) < (3 * Math.abs(distanceY))) distanceX = 0;
            if(Math.abs(distanceY) < (3 * Math.abs(distanceX))) distanceY = 0;

            if(!dragBand_moved || ((System.currentTimeMillis() - last_handeled_move_time) >= DRAG_TICK_INTERVAL_MIN))
                onScroll_parse_counters(distanceX, distanceY);

            //}}}
            // 1 DRAGGING HIST -> DRAGGING DOCK {{{
            if( switch_to_dock )
            {
                consumed_by = "onScroll_drag_dock";

                onScroll_drag_dock();

                onScroll_counters_reset();
            }
            //}}}
            // 2 DRAGGING DOCK -> DRAGGING HIST {{{
            else if( switch_to_hist )
            {
                consumed_by = "switch_to_hist";

                onScroll_drag_hist();

                onScroll_counters_reset();
            }
            //}}}
            // 3 SHOW PROFHIST_TABLE  {{{
            else if( show_profile_hist )
            {
                consumed_by = "show_profile_hist";

                //onScroll_dock_show_profile_hist();

                onScroll_counters_reset();
            }
            //}}}
            // 4 SCROLL TO CURRENT PROFILE  {{{
            else if( scroll_to_profile )
            {
                consumed_by = "scroll_to_profile";

                onScroll_dock_scroll_to_working_profile();

                if((get_dragBand_sliding() != SLIDING_VISIBLE) && slide_band_anim_queue_is_empty())
                    open_band(drag_band, caller);

                onScroll_counters_reset();
            }
            //}}}
            // 5 SCROLL TO VISITED PROFILE {{{
            else if( scroll_to_visited )
            {
                consumed_by = "scroll_to_visited";

                onScroll_dock_scroll_to_visited_profile();

                if((get_dragBand_sliding() != SLIDING_VISIBLE) && slide_band_anim_queue_is_empty())
                    open_band(drag_band, caller);

                onScroll_counters_reset();
            }
            //}}}
            // [return consumed_by] {{{
//*EV3_RT_SC*/if(consumed_by != null) Settings.MON(TAG_EV3_RT_SC, caller, "...return true (consumed_by="+consumed_by+")");
            return (consumed_by != null);
            //}}}
        }
        //}}}
        // onScroll_3_BAND {{{
//*EV3_RT_SC*/private        int last_sliding                      = -1;//TAG_EV3_RT_SC
        private boolean onScroll_3_BAND(float distanceX, float distanceY)
        {
            // (drag_band) {{{
            if(drag_band == null) return false;

            String caller = "onScroll_3_BAND";
//*EV3_RT_SC*/Settings.MOC(TAG_EV3_RT_SC, caller);

            String consumed_by = null;
            //}}}
            // [BAND DRAG POSITION] .. FROM=[FULLY-HIDDEN] TO=[FULLY-VISIBLE] .. (from left to right) {{{
            String drag_band_name = get_view_name ( drag_band );
            boolean  moving_right = (distanceX < -3);
            boolean  moving_left  = (distanceX >  3);
            int                 h = get_band_hid_X( drag_band );
            int                 v = get_band_vis_X( drag_band );
            int                 x = (int)drag_band.getX();
            //}}}
            // 1/2 MOVE START {{{
            if( !dragBand_moved )
            {
                consumed_by = "DRAG BAND START";
                // [DRAG_START] {{{

//*EV3_RT_SC*/Settings.MON(TAG_EV3_RT_SC, caller, "DRAG_START (distanceX="+distanceX+") ["+drag_band_name+"] "+ String.format("HID=[%4d] ... x=[%4d] ... VIS=[%4d]", h, x, v));
                //  boolean start_unhiding = !dragBand_moved && (drag_band == hist_band) && was_fully_hidden &&  moving_right;
                //  boolean start_hiding   = !dragBand_moved && was_fully_visible && !moving_right;

//EV3_RT_SC*/if(drag_band == hist_band) Settings.MON(TAG_EV3_RT_SC, "onScroll", "dck_handle.getWidth()=["+ dck_handle.getWidth()+"]");

                // PROBE BEFORE DRAG STATE
                dragBand_was_hiding = drag_band.getX() <= h;
                dragBand_moved      = true;

                // START DRAGGING
                onScroll_counters_reset();
                //  was_fully_hidden    = is_band_fully_hidden (drag_band);
                //  was_fully_visible   = is_band_fully_visible(drag_band);

                //}}}
                // MAKE SELECTED BAND VISIBLE AND MOVE IT TO FULLY VISIBLE POSITION {{{
                make_band_visible( drag_band );

                //if( !is_dragBand_showing() )

                if( !is_dragBand_opened(drag_band) )
                {
                    if( slide_band_anim_queue_is_empty() ) open_band(drag_band, caller);

                }
                //}}}
                // DISABLE TABS SCROLLING ON FIRST MOVE {{{
                if( is_tabs_scrolling() )
                {
                    onScroll_drag_band_setX(h+1); // XXX unhide to satisfy GUI colors dependencies
                    //.................... TODO ...would be better to have some probing to account for drag start condition
                    set_tabs_scrolling(false, caller);
                }
                //}}}
            }
            //}}}
            // 2/2 MOVE CONTINUE {{{
            else {
                consumed_by = "DRAG BAND MOVE";
                // DRAG VELOCITY {{{
                // TODO: non-linearity ?
                float dx  =     (drag_band.getWidth() > 150) ? 2*distanceX : distanceX/2; // slower with narrow bands
                x        -= dx;
                if(x < h) x = h;
                if(x > v) x = v;

                // }}}
                // CLIPPING [BETWEEN FULLY HIDDEN] <= [x] <= [TO FULLY VISIBLE] {{{
                boolean moving_fully_visible = (x >= v);
                boolean moving_fully_hidden  = (x <= h);

                boolean cannot_move
                    =   (moving_fully_hidden  && was_fully_hidden ) // (fully-hidden  to the left )
                    ||  (moving_fully_visible && was_fully_visible) // (fully-visible to the right)
                    ;
/* // {{{
if(cannot_move && (drag_band == hist_band))
{
Settings.MON(TAG_EV3_RT_SC, "onScroll", ".........dck_handle.getWidth()=["+ dck_handle.getWidth()+"]");
Settings.MON(TAG_EV3_RT_SC, "onScroll", ".............show_band_showing=["+ is_view_showing(show_band) +"]");
Settings.MON(TAG_EV3_RT_SC, "onScroll", "..............show_band_hidden=["+ show_band_hidden +"]");
RelativeLayout.LayoutParams rlp;
rlp = (RelativeLayout.LayoutParams)show_band.getLayoutParams();
Settings.MON(TAG_EV3_RT_SC, "onScroll", ".....show_band.rlp.rightMargin: "+ rlp.rightMargin);
Settings.MON(TAG_EV3_RT_SC, "onScroll", "..........show_band is VISIBLE: "+ (show_band.getVisibility() == View.VISIBLE));
Settings.MON(TAG_EV3_RT_SC, "onScroll", "...show_band.getParent != null: "+ (show_band.getParent() != null));
Settings.MON(TAG_EV3_RT_SC, "onScroll", "(dck_handle.getScrollX() == 0): "+ (dck_handle.getScrollX() == 0));
}
*/ // }}}
                // }}}
                // BAND [HIDE-DRAG-SHOW] {{{
                if( !cannot_move && slide_band_anim_queue_is_empty())
                {
                    boolean   hiding = false;
                    boolean  showing = false;
                    boolean dragging = false;
                    int      sliding = get_dragBand_sliding();

                    if(moving_right) {
                        switch( sliding ) {
                            case SLIDING_HIDDEN     : dragging   = true; break; // 1        HIDDEN: (...start   unhiding) .. (follow user finger)
                            case  SLIDING_LEFT      : dragging   = true; break; // 2 MOSTLY HIDDEN: (...keep on unhiding)
                            case   SLIDING_RIGHT    : showing    = true; break; // 3  HALF SHOWING: (...animate to fully visible)
                            case    SLIDING_VISIBLE : /* DONE SHOWING */ break; // 4 FULLY VISIBLE: (all done)
                        }
                    }
                    else if(moving_left) {
                        switch( sliding ) {
                            case    SLIDING_VISIBLE : dragging   = true; break; // 4 FULLY VISIBLE: (...start   hiding) .. (follow user finger)
                            case   SLIDING_RIGHT    : dragging   = true; break; // 3  HALF SHOWING: (...keep on hiding)
                            case  SLIDING_LEFT      : hiding     = true; break; // 2 MOSTLY HIDDEN: (...animate to fully hidden)
                            case SLIDING_HIDDEN     : /* DONE HIDING  */ break; // 1        HIDDEN: (all done)
                        }
                    }
// LOG {{{
//*EV3_RT_SC*/ boolean sliding_changed = (sliding != last_sliding); last_sliding = sliding;//TAG_EV3_RT_SC
//*EV3_RT_SC*/ String s = String.format("[HID:%4d] <= [x:%4d] <= [VIS:%4d]", h, x, v)     ;//TAG_EV3_RT_SC
//EV3_RT_SC*/ if(       sliding_changed) Settings.MOM(TAG_EV3_RT_SC, drag_band_name+": "+get_dragBand_slidingStr(sliding)+": ["+(moving_left?"<<":"--")+"]--["+(moving_right?">>":"--")+"] ("+s+")");
//EV3_RT_SC//                            Settings.MOM(TAG_EV3_RT_SC, drag_band_name+": "+get_dragBand_slidingStr(sliding));
//EV3_RT_SC// if( moving_right         ) Settings.MOM(TAG_EV3_RT_SC, drag_band_name+": moving_right="+moving_right);
//EV3_RT_SC// if( moving_left          ) Settings.MOM(TAG_EV3_RT_SC, drag_band_name+": moving_left ="+moving_left );
//EV3_RT_SC// if( moving_fully_hidden  ) Settings.MOM(TAG_EV3_RT_SC, drag_band_name+": HIDDEN <<<<<<<<<<< @@@ ("+s+")");
//EV3_RT_SC// if(    was_fully_hidden  ) Settings.MOM(TAG_EV3_RT_SC, drag_band_name+": ->visible......... @@@ ("+s+")");
//EV3_RT_SC// if( moving_fully_visible ) Settings.MOM(TAG_EV3_RT_SC, drag_band_name+": >>>>>>>>>> VISIBLE @@@ ("+s+")");
//EV3_RT_SC// if(    was_fully_visible ) Settings.MOM(TAG_EV3_RT_SC, drag_band_name+": ..........hiding<- @@@ ("+s+")");
//}}}

                    // 1/2 follow user touch
                    if( dragging ) {
                        drag_band.setVisibility( View.VISIBLE );
                        onScroll_drag_band_setX(x);
                    }
                    // 2/2 or slide animate
                    else if(showing && slide_band_anim_queue_is_empty())
                    {
//*EV3_RT_SC*/Settings.MON(TAG_EV3_RT_SC, caller, drag_band_name+": "+get_dragBand_slidingStr(sliding));
                        open_band(drag_band, caller);
                    }
                    else if(hiding  && slide_band_anim_queue_is_empty())
                    {
//*EV3_RT_SC*/Settings.MON(TAG_EV3_RT_SC, caller, drag_band_name+": "+get_dragBand_slidingStr(sliding));
                        fold_band(drag_band, caller);
                        if(drag_band == dock_band) {
//*EV3_RT_SC*/Settings.MON(TAG_EV3_RT_SC, caller+" (continuation)", drag_band_name+": "+get_dragBand_slidingStr(sliding));
                            open_band(hist_band, caller);
                        }
                    }
                }
                //}}}
            }
            //}}}
            // [return consumed_by] {{{
//*EV3_RT_SC*/if(consumed_by != null) Settings.MON(TAG_EV3_RT_SC, caller, "...return true (consumed_by="+consumed_by+")");
            return (consumed_by != null);
            //}}}
        }
        //}}}

        // onScroll_drag_dock {{{
        private void onScroll_drag_dock()
        {
            // change moving drag_band
            View prev_band      = drag_band;
            drag_band           = dock_band;
            dragBand_moved      = false;
            dragBand_scrolled   = false;

            // START WITH A VISIBLE DOCK
            onScroll_drag_band_setX( get_band_hid_X( drag_band ) + hist_band.getWidth() );
            Handle.Set_cur_handle(dck_handle, "onScroll");  // [EXPAND] HANDLE TO BAND WIDTH
//open_band(drag_band, "onScroll_drag_dock");
//*EV3_RT_SC*/Settings.MON(TAG_EV3_RT_SC, "onScroll_drag_dock", "DRAG_SWITCH: FROM ["+ get_view_name(prev_band)+"] TO ["+ get_view_name(drag_band) +"]");

            //dock_scroll_to_working_profile("onScroll_drag_dock");
            handler.re_postDelayed(hr_dock_scroll_to_working_profile, 500);
        }
        private final Runnable hr_dock_scroll_to_working_profile = new Runnable() {
            @Override public void run() { dock_scroll_to_working_profile("hr_dock_scroll_to_working_profile"); }
        };
        //}}}
        // onScroll_drag_hist {{{
        private void onScroll_drag_hist()
        {
            // change moving drag_band
            View prev_band      = drag_band;
            drag_band           = hist_band;
            dragBand_moved      = false;
            dragBand_scrolled   = false;

            // START WITH A HIDDEN HIST
            //onScroll_drag_band_setX( get_band_hid_X( drag_band ) );
            Handle.Collapse_all_instances();                // [NARROW] HANDLE TO BAND WIDTH

//*EV3_RT_SC*/Settings.MON(TAG_EV3_RT_SC, "onScroll_drag_hist", "DRAG_SWITCH: FROM ["+ get_view_name(prev_band)+"] TO drag_band=["+ get_view_name(drag_band) +"]");
        }
        //}}}
        // onScroll_dock_scroll_to_working_profile {{{
        private void onScroll_dock_scroll_to_working_profile()
        {
//*EV3_RT_SC*/Settings.MOM(TAG_EV3_RT_SC, "onScroll_dock_scroll_to_working_profile");

            dragBand_scrolled = true; // vertical move check for ACTION_UP handling
            dock_scroll_to_working_profile("onScroll_dock_scroll_to_working_profile");
        }
        //}}}
        // onScroll_dock_show_profile_hist {{{
        private void onScroll_dock_show_profile_hist()
        {
            String caller = "onScroll_dock_show_profile_hist";
//*EV3_RT_SC*/Settings.MON(TAG_EV3_RT_SC,caller, "...calling show_PROFHIST_TABLE");
            show_PROFHIST_TABLE(caller);
        }
        //}}}
        // onScroll_dock_scroll_to_visited_profile {{{
        private void onScroll_dock_scroll_to_visited_profile()
        {
            int direction = (moving_y_in_same_direction_count < 0) ? 1 : -1;
//*EV3_RT_SC*/Settings.MON(TAG_EV3_RT_SC, "onScroll_dock_scroll_to_visited_profile", "direction=["+direction+"]");

            dragBand_scrolled = true; // vertical move check for ACTION_UP handling
            dock_scroll_to_visited_profile(direction, "onScroll_dock_scroll_to_visited_profile");
        }
        //}}}
    // onScroll_drag_band_setX .. follow user touch {{{
    private void onScroll_drag_band_setX(int x) {
        // dock_band {{{
        if(drag_band == dock_band)
        {
            drag_band.setX(x);
            // moving dragband (anywhere) == dimm tabs
            // TODO call that when hiding hist_band somwhere else:
            bg_view.setAlpha (       BG_VIEW_DIMMED_ALPHA_MIN);
            vsv    .setAlpha (TABS_CONTAINER_DIMMED_ALPHA_MIN);
            vsv    .setScaleX(TABS_CONTAINER_DIMMED_SCALE_MIN);
            vsv    .setScaleY(TABS_CONTAINER_DIMMED_SCALE_MIN);
        }
        //}}}
        // hist_band {{{
        else if(drag_band == hist_band)
        {
            drag_band.setX(x);
            // TABS ALPHA AND SCALE
            if((bg_view != null) && (vsv != null))
            {

                float band_visible_ratio = get_band_vis_fraction(drag_band);

                bg_view
                    .setAlpha(     BG_VIEW_DIMMED_ALPHA_MIN         //    0.1f  .. (base  visibility)
                            + (.5f-BG_VIEW_DIMMED_ALPHA_MIN      )  // +  0.9f  .. (extra visibility)
                            * ( 1f-band_visible_ratio            )  // * (1..0) .. (band-hidden .. full band-visible)
                            );

                vsv
                    .setAlpha(    TABS_CONTAINER_DIMMED_ALPHA_MIN   //   0.1f  .. (base  visibility)
                            + (1f-TABS_CONTAINER_DIMMED_ALPHA_MIN)  // +  0.9f  .. (extra visibility)
                            * (1f-band_visible_ratio             )  // * (1..0) .. (band-hidden .. full band-visible)
                            );

                vsv
                    .setScaleX( 1f                                  //    1.0f  .. (plain scale)
                            -  (1f-TABS_CONTAINER_DIMMED_SCALE_MIN) // +  0.1f  .. (downscale)
                            *  (    band_visible_ratio            ) // * (0..1) .. (band-hidden .. full band-visible)
                            );

                vsv
                    .setScaleY( 1f                                  //    1.0f  .. (plain scale)
                            -  (1f-TABS_CONTAINER_DIMMED_SCALE_MIN) // +  0.1f  .. (downscale)
                            *  (    band_visible_ratio            ) // * (0..1) .. (band-hidden .. full band-visible)
                            );


            }
        }
        // }}}
    }
    //}}}
    //}}}
    //* EV4_on_POINTER */
    //{{{
    // on_POINTER_DOWN {{{
    private boolean on_POINTER_DOWN(MotionEvent event)
    {
        // (getPointerCount) {{{
        String caller = "on_POINTER_DOWN";
//*EV2_RT_CB*/Settings.MOC(TAG_EV2_RT_CB, caller, "...getPointerCount="+ event.getPointerCount());

        String consumed_by = null;
        //}}}

        if     ( on_POINTER_DOWN_1_wvTools(event) ) consumed_by = "on_POINTER_DOWN_1_wvTools";
        else   { on_POINTER_DOWN_2_WEBVIEW(event) ; consumed_by = "on_POINTER_DOWN_2_WEBVIEW"; }

        // [return consumed_by] {{{
//*EV2_RT_CB*/if(consumed_by != null) Settings.MON(TAG_EV2_RT_CB, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    // on_POINTER_UP {{{
    private boolean on_POINTER_UP(MotionEvent event)
    {
        // (getPointerCount) {{{
        String caller = "on_POINTER_UP";
//*EV2_RT_CB*/Settings.MOC(TAG_EV2_RT_CB, caller, "...getPointerCount="+ event.getPointerCount());

        String consumed_by = null;
        //}}}

        if     ( on_POINTER_UP_1_wvTools(event) ) consumed_by = "on_POINTER_UP_1_wvTools";
        else   { on_POINTER_UP_2_WEBVIEW(event) ; consumed_by = "on_POINTER_UP_2_WEBVIEW"; }

        // [return consumed_by] {{{
//*EV2_RT_CB*/if(consumed_by != null) Settings.MON(TAG_EV2_RT_CB, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}

    // on_POINTER_DOWN_1_wvTools {{{
    private boolean on_POINTER_DOWN_1_wvTools(MotionEvent event)
    {
        String caller = "on_POINTER_DOWN_1_wvTools("+get_view_name(gesture_down_SomeView_atXY)+", "+Settings.Get_action_name(event)+")";
//*EV2_RT_CB*/Settings.MOC(TAG_EV2_RT_CB, caller, "...getPointerCount="+ event.getPointerCount());

        String consumed_by
            =   wvTools.on_POINTER_DOWN(event)
            ?    "wvTools.on_POINTER_DOWN"
            :    null;

        // [return consumed_by] {{{
//*EV2_RT_CB*/if(consumed_by != null) Settings.MON(TAG_EV2_RT_CB, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    // on_POINTER_DOWN_2_WEBVIEW {{{
    private boolean on_POINTER_DOWN_2_WEBVIEW(MotionEvent event)
    {
        if( !is_a_webView(gesture_down_SomeView_atXY) ) return false;
        String caller = "on_POINTER_DOWN_2_WEBVIEW";

        MWebView wv = (MWebView)gesture_down_SomeView_atXY;

        String consumed_by =
            wv.onEventJS(event, (MWebView)gesture_down_SomeView_atXY)
            ? "onEventJS"
            :  null;

//*EV2_RT_CB*/if(consumed_by != null) Settings.MON(TAG_EV2_RT_CB, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
    }
    //}}}

    // on_POINTER_UP_1_wvTools {{{
    private boolean on_POINTER_UP_1_wvTools(MotionEvent event)
    {
        String caller = "on_POINTER_UP_1_wvTools";
//*EV2_RT_CB*/Settings.MOC(TAG_EV2_RT_CB, caller, "...getPointerCount="+ event.getPointerCount());

        String consumed_by
            =   wvTools.on_POINTER_UP(event)
            ?    "wvTools.on_POINTER_UP"
            :    null;

        // [return consumed_by] {{{
//*EV2_RT_CB*/if(consumed_by != null) Settings.MON(TAG_EV2_RT_CB, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    // on_POINTER_UP_2_WEBVIEW {{{
    private boolean on_POINTER_UP_2_WEBVIEW(MotionEvent event)
    {
        if( !is_a_webView(gesture_down_SomeView_atXY) ) return false;
        String caller = "on_POINTER_UP_2_WEBVIEW";

        MWebView wv = (MWebView)gesture_down_SomeView_atXY;

        String consumed_by =
            wv.onEventJS(event, (MWebView)gesture_down_SomeView_atXY)
            ? "onEventJS"
            :  null;

//*EV2_RT_CB*/if(consumed_by != null) Settings.MON(TAG_EV2_RT_CB, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
    }
    //}}}
    //}}}
    /** EV5_onScale */
    //{{{
    // scaleZoomCycle {{{
    private boolean zooming_in                      = true;

    private void scaleZoomCycle()
    {
        /* 1 2 3 STEPS BEHAVIOR BORROWED FROM [MScaleGestureListener] handling */
        String caller = "scaleZoomCycle";

        // 1 [onScaleBegin]
        //check_scaling_allowed(caller)

        // 2 [onScale]

        /* STOP CURRENTLY ONGOING PROCESSES */
        stop_GLOWING(caller);
        this_RTabsClient.stop_GROUPING(caller);

        /* SET ZOOM PIVOT XY */
        xsv_focusX = last_ACTION_DOWN_X;
        xsv_focusY = last_ACTION_DOWN_Y;
//System.err.println(caller+": xsv_focusXY=["+xsv_focusX+" "+xsv_focusY+"]");
        start_RESCALING();

        /* TOGGLE ZOOM IN OR OUT AT BOUDARIES */
//{{{
//        boolean at_zoom_boundary
//            =  (Settings.DEV_SCALE >= Settings.DEV_SCALE_MAX)
//            || (Settings.DEV_SCALE <= 1)
//            ;
//        if( at_zoom_boundary ) zooming_in = !zooming_in;
///*SCALE*/Settings.MOM(TAG_SCALE, "...Settings.DEV_SCALE="+ Settings.DEV_SCALE+ " MIN=["+ Settings.DEV_SCALE_MIN+"]  MAX=["+ Settings.DEV_SCALE_MAX+"]");
///*SCALE*/Settings.MOM(TAG_SCALE, ".....at_zoom_boundary="+ at_zoom_boundary);
//}}}

        /* ZOOM-IN-OR-OUT .. (based on PREVIOUS TOGGLED-STATE -OR- REMEMBERED SCALING DIRECTION) */
        continue_ZOOMING();

        // 3 [onScaleEnd]
        handler.re_post( hr_end_rescaling );

        // COMMIT Settings.DEV_SCALE
    }
    //}}}
    // start_RESCALING {{{
    // [start_rescaling_time] {{{
    //  private int  start_rescaling_time;
    //
    //  start_rescaling_time = (int)System.currentTimeMillis();
    //
    //  int   rescaling_time = (int)System.currentTimeMillis();
    //  if((rescaling_time - start_rescaling_time) < 50)
    //}}}
    private void start_RESCALING()
    {
//*SCALE*/Settings.MON(TAG_SCALE, "...start_RESCALING", String.format("[%4.0f %4.0f]", xsv_focusX, xsv_focusY) );
        rescaling = true;
        rescaling_from_scale = Settings.DEV_SCALE;
// // {{{
//  //NpButton.SuspendLayout();
//
//  //  float mid_x = Settings.DISPLAY_W/2;
//  //  float mid_y = Settings.DISPLAY_H/2;
//  //  xsv_pivotX = hsv.getScrollX() + mid_x;
//  //  xsv_pivotY = vsv.getScrollY() + mid_y;
//
//      xsv_pivotX =                    xsv_focusX;
//      xsv_pivotY =                    xsv_focusY;
//
//      xsv_pivotX = hsv.getScrollX() + xsv_focusX;
//      xsv_pivotY = vsv.getScrollY() + xsv_focusY;
// // }}}
        // [FREEZED] [OFFLINE] [TABS] [BACKGROUND]
// // {{{
//      xsv_scrollX     = tabs_container.getScrollX() + hsv.getScrollX();
//      xsv_scrollY     = tabs_container.getScrollY() + vsv.getScrollY();
//      xsv_pivotX      = xsv_scrollX + xsv_focusX;
//      xsv_pivotY      = xsv_scrollY + xsv_focusY;
//      tabs_container.setPivotX( xsv_pivotX );
//      tabs_container.setPivotY( xsv_pivotY );
// // }}}
        xsv_scrollX     =  hsv.getScrollX() + tabs_container.getScrollX();
        xsv_scrollY     =  vsv.getScrollY() + tabs_container.getScrollY();
        xsv_pivotX      =  xsv_focusX + xsv_scrollX;
        xsv_pivotY      =  xsv_focusY + xsv_scrollY;
        tabs_container.setPivotX( xsv_pivotX );
        tabs_container.setPivotY( xsv_pivotY );
        //translX       =  0;
        //translY       =  0;
        xsv_offsetX     =  xsv_pivotX - Settings.DISPLAY_W/2;
        xsv_offsetY     =  xsv_pivotY - Settings.DISPLAY_H/2;
    }
    //}}}
    // continue_ZOOMING {{{
    private void continue_ZOOMING()
    {
//*SCALE*/Settings.MOC(TAG_SCALE, "continue_ZOOMING: ...zooming_in "+zooming_in+")");

        /* ZOOM FACTOR (IN x 2) (OUT / 2) */
        float zoom_cycle_scale_factor
            = zooming_in
            ? ZOOM_SCALE_CYCLE_FACTOR_OUT_2
            : ZOOM_SCALE_CYCLE_FACTOR_IN_HALF;

        continue_RESCALING( zoom_cycle_scale_factor );
    }
    //}}}
    // continue_RESCALING {{{
    private void continue_RESCALING(float scale)
    {
//*SCALE*/Settings.MON(TAG_SCALE, "continue_RESCALING", String.format("[%4.0f %4.0f] [%1.1f]", xsv_focusX, xsv_focusY, scale) );

        // GUI COLORS {{{
        boolean zoomed_out        = is_ZOOMED_OUT( scale );
        boolean within_SOFT_SCALE = is_within_SOFT_SCALE();

        show_GUI_rescaling(zoomed_out, within_SOFT_SCALE);
        //}}}
//  //{{{
//  //  xsv_pivotX = hsv.getScrollX() + xsv_focusX;
//  //  xsv_pivotY = vsv.getScrollY() + xsv_focusY;
//  //  tabs_container.scrollTo((int)xsv_focusX, (int)xsv_focusY);
//  //  hsv.scrollBy(x,0);
//  //  vsv.scrollBy(0,y);
//
//  //int x = (int)(xsv_pivotX-xsv_focusX); int y = (int)(xsv_pivotY-xsv_focusY);
//  //int x = (int)(xsv_focusX-xsv_pivotX); int y = (int)(xsv_focusY-xsv_pivotY);
//  //tabs_container.scrollBy(x, y);
//
//  if( keep_soft_rescale() ) {
//      xsv_pivotX =                  xsv_focusX  ;
//      xsv_pivotY =                  xsv_focusY  ;
//      tabs_container.setPivotX( xsv_pivotX );
//      tabs_container.setPivotY( xsv_pivotY );
//  }
//  //}}}
// // {{{
//      //int x = (int)(xsv_pivotX-xsv_focusX); int y = (int)(xsv_pivotY-xsv_focusY);
//      int x = (int)(xsv_focusX-xsv_pivotX); int y = (int)(xsv_focusY-xsv_pivotY);
//      tabs_container.scrollBy(x, y);
//      xsv_pivotX =                  xsv_focusX  ;
//      xsv_pivotY =                  xsv_focusY  ;
// // }}}

        /* APPLY RESULTING SCALE */
        if((Settings.DEV_SCALE * scale) > Settings.DEV_SCALE_MAX) scale = Settings.DEV_SCALE_MAX / Settings.DEV_SCALE;
        if((Settings.DEV_SCALE * scale) < Settings.DEV_SCALE_MIN) scale = Settings.DEV_SCALE_MIN / Settings.DEV_SCALE;
        if(scale < .1f) scale = .1f;
        if(scale > 10f) scale = 10f;
        tabs_container.setScaleX( scale );
        tabs_container.setScaleY( scale );

//{{{
//  tabs_container.scrollTo((int)(Settings.DISPLAY_W/2       - xsv_focusX), (int)(Settings.DISPLAY_H/2       - xsv_focusY));
//  tabs_container.scrollTo((int)(xsv_focusX       - Settings.DISPLAY_W/2), (int)(xsv_focusY       - Settings.DISPLAY_H/2));
//  tabs_container.scrollTo((int)(xsv_focusX*scale - Settings.DISPLAY_W/2), (int)(xsv_focusY*scale - Settings.DISPLAY_H/2));
//  tabs_container.scrollBy((int)((xsv_focusX-xsv_scrollX)      ), (int)((xsv_focusY-xsv_scrollY)      ));
//  tabs_container.scrollBy((int)((xsv_focusX-xsv_scrollX)/scale), (int)((xsv_focusY-xsv_scrollY)/scale));
//  //vsv.scrollBy(                   0, (int)(xsv_focusY-xsv_pivotY));
//  //hsv.scrollBy((int)(xsv_focusX-xsv_pivotX),                    0);
//tabs_container.scrollTo((int)(xsv_scrollX                 , (int)(xsv_scrollY               ))); // FIXME - SHOULD HAVE NO VISIBLE EFFECT
//tabs_container.scrollTo((int)(xsv_scrollX-(xsv_focusX-xsv_pivotX)), (int)(xsv_scrollY-(xsv_focusY-xsv_pivotY)));
//hsv           .scrollTo((int)(xsv_scrollX-(xsv_focusX-xsv_pivotX)), (int)(0                      ));
//vsv           .scrollTo((int)(0                      ), (int)(xsv_scrollY-(xsv_focusY-xsv_pivotY)));
//}}}

        int focus_downX    = (int)(xsv_pivotX  - xsv_scrollX);
        int focus_downY    = (int)(xsv_pivotY  - xsv_scrollY);
        int dx_to_top_left = (int)(xsv_focusX  - focus_downX);
        int dy_to_top_left = (int)(xsv_focusY  - focus_downY);

/*{{{//FIXME 180724
        hsv.scrollTo(     (int)(xsv_scrollX - dx_to_top_left), 0                      );
        vsv.scrollTo(                          0 , (int)(xsv_scrollY - dy_to_top_left));
}}}*/

        //translX     =  dx;
        //translY     =  dy;
    }
    //}}}
    // end_rescaling {{{
    // hr_end_rescaling {{{
    private final Runnable hr_end_rescaling = new Runnable() {
        @Override public void run() { end_rescaling(); }
    };
    //}}}
    // end_rescaling {{{
    private void end_rescaling()
    {
        rescaling = false;
        // 1/2 SOFT RESCALING .. (do not update Settings.DEV_SCALE) {{{
        String caller = "end_rescaling";

        if(           !is_view_showing( cart_band )
                && (   is_within_SOFT_SCALE()
                    || keep_soft_rescale()
                    || is_ZOOMED_OUT()
                   )
          ) {
//*SCALE*/Settings.MOC(TAG_SCALE, caller+" SOFT RESCALING ..  (do not update Settings.DEV_SCALE)");
//*SCALE*/Settings.MOM(TAG_SCALE, "...Settings.DEV_SCALE="+ Settings.DEV_SCALE);

            // ...soft scale adjusted
            // ...hard scale unchanged
            return;
          }

        // }}}
        // 2/2 HARD RESCALING {{{
//*SCALE*/Settings.MOC(TAG_SCALE, caller+" HARD RESCALING");
        float rescale_factor = tabs_container.getScaleX();
//{{{
//  // pivot to display center offset
//  int[] o_screen_xy = new int[2]; tabs_container.getLocationOnScreen( o_screen_xy );
//  int   p_screen_x  = o_screen_xy[0] + (int)(xsv_pivotX * rescale_factor);
//  int   p_screen_y  = o_screen_xy[1] + (int)(xsv_pivotY * rescale_factor);
//  int   p_offset_x  = p_screen_x - (Settings.DISPLAY_W/2);
//  int   p_offset_y  = p_screen_y - (Settings.DISPLAY_H/2);
//  //Settings.MON(TAG_SCALE, String.format("%25s: [%4d %4d]", "hr_end_rescaling", p_offset_x, p_offset_y) );
//  xsv_pivotX -= p_offset_x;// / Settings.DEV_SCALE;
//  xsv_pivotY -= p_offset_y;// / Settings.DEV_SCALE;
//}}}

        xsv_offsetX          =  (xsv_focusX - Settings.DISPLAY_W/2) / rescale_factor;
        xsv_offsetY          =  (xsv_focusY - Settings.DISPLAY_H/2) / rescale_factor;

        Settings.DEV_SCALE  *= rescale_factor;
        xsv_pivotX          *= rescale_factor;
        xsv_pivotY          *= rescale_factor;
//{{{
//      //xsv_pivotX   += translX;
//      //xsv_pivotY   += translY;
//}}}
        xsv_pivotX          -= xsv_offsetX;
        xsv_pivotY          -= xsv_offsetY;
//{{{
        //int x                = (int)(hsv.getScrollX() * rescale_factor);
        //int y                = (int)(vsv.getScrollY() * rescale_factor);
        //hsv.scrollTo(x,0);
        //vsv.scrollTo(0,y);
//}}}

//*SCALE*/Settings.MOM(TAG_SCALE, "...Settings.DEV_SCALE="+ Settings.DEV_SCALE);

        tabs_container.setScaleX(1F);
        tabs_container.setScaleY(1F);
//{{{
//int x = (int)(hsv.getScrollX() * rescale_factor);
//int y = (int)(vsv.getScrollY() * rescale_factor);
//hsv.scrollTo(x,0);
//vsv.scrollTo(0,y);
            //NpButton.ResumeLayout();
            //NpButton.SuspendLayout();
//}}}
        vsv.setVisibility( View.INVISIBLE ); // enter transient state
        this_RTabsClient.apply_TABS_LAYOUT(RTabsClient.TABS_Map, tabs_container, "hr_end_rescaling");

        // }}}
        /* SET NEXT ZOOM CYCLE DIRECTION */
        zooming_in
            = rescaled_since_ondown
            ?  !zooming_in                                 /* TOGGLE zooming direction */
            :  (Settings.DEV_SCALE > rescaling_from_scale) /* KEEP   zooming direction */
            ;

        // DISPLAY COLORS {{{
        sync_GUI_colors(caller);

        // }}}
        handler.re_post( hr_after_rescaling );
    }
    //}}}
    // keep_soft_rescale {{{
    private boolean keep_soft_rescale()
    {
        return false;
        //return true;
    }
//}}}
    //}}}
    // after_rescaling {{{
    // hr_after_rescaling {{{
    private final Runnable hr_after_rescaling = new Runnable() {
        @Override public void run() { after_rescaling(); }
    };
    //}}}
    // after_rescaling {{{
    private void after_rescaling()
    {
//{{{
//          //hsv.scrollTo(   (int)xsv_pivotX - Settings.DISPLAY_W/2, 0);
//          //vsv.scrollTo(0, (int)xsv_pivotY - Settings.DISPLAY_H/2   );
//          //  int x = tabs_container.getScrollX() + hsv.getScrollX();
//          //  int y = tabs_container.getScrollY() + vsv.getScrollY();
//          //  tabs_container.scrollTo(0,0);
//          //  hsv.scrollTo(x,0);
//          //  vsv.scrollTo(0,y);
//}}}
//            // SCROLL_INIT {{{
//            int x = tabs_container.getScrollX() + hsv.getScrollX();
//            int y = tabs_container.getScrollY() + vsv.getScrollY();
//            tabs_container.scrollTo(0,0);
//            hsv.scrollTo(x,0);
//            vsv.scrollTo(0,y);
////SCALE*/Settings.MON(TAG_SCALE, String.format(TAG_SCALE, "%25s: [%4d %4d]", "hr_after_rescaling", x, y, "SCROLL_INIT") );
//            //}}}
        hsv.scrollTo(   (int)xsv_pivotX - Settings.DISPLAY_W/2, 0);
        vsv.scrollTo(0, (int)xsv_pivotY - Settings.DISPLAY_H/2   );

        // [FREEZED] [OFFLINE] [TABS] [BACKGROUND]
        //sync_tabs_scrolling("after_rescaling"); // ...see ACTION_UP
        set_tabs_scrolling( true, "after_rescaling");

        vsv.setVisibility( View.VISIBLE ); // exit transient state
    }
    //}}}
    //}}}
    // is_ZOOMED_OUT {{{
//  private   float ZOOMED_OUT = 1.05F; // at Settings.DEV_SCALE_MIN or within 0 to 5% above
    private final float ZOOMED_OUT = 1F   ; // at Settings.DEV_SCALE_MIN
    private boolean is_ZOOMED_OUT()
    {
        return is_ZOOMED_OUT( tabs_container.getScaleX() );
    }
    private boolean is_ZOOMED_OUT(float scale)
    {
        return (     scale * Settings.DEV_SCALE    )
            <= (ZOOMED_OUT * Settings.DEV_SCALE_MIN);
    }
    //}}}
    // is_within_SOFT_SCALE {{{
    private float SOFT_SCALE_MAX = ZOOMED_OUT;//2.0F; // twice scale
    private float SOFT_SCALE_MIN = 0.5F; // half  scale
    private boolean is_within_SOFT_SCALE()
    {
/*
        // CLOSE TO 1:1 .. SMALL CHANGES
        float   tabs_scale = tabs_container.getScaleX();
        return (tabs_scale > SOFT_SCALE_MIN)
            && (tabs_scale < SOFT_SCALE_MAX);
*/
        return false;
    }
    //}}}
    //}}}
    //* EV6_on_UP */
    //{{{
    // on_UP {{{
    private boolean on_UP(MotionEvent event)
    {
        // [cancel_long_touch] {{{
        String caller = "on_UP";

//*EV1_RT_IN*/trace_views( TAG_EV1_RT_IN, caller);
        cancel_long_touch(caller);

        String consumed_by = null;
        //}}}
        //............................................................ HANDLE TRANSIENT FRONT VIEWS
        if     ( on_UP_3_mClamp       ( event ) ) consumed_by = "on_UP_3_mClamp";
        else if( on_UP_1_handle       (       ) ) consumed_by = "on_UP_1_handle";
        else if( on_UP_2_band         (       ) ) consumed_by = "on_UP_2_band";
        else if( on_UP_4_WEBVIEW_SWAP ( event ) ) consumed_by = "on_UP_4_WEBVIEW_SWAP";
        else if( on_UP_5_wvTools      ( event ) ) consumed_by = "on_UP_5_wvTools";
        else if( on_UP_7_JAVASCRIPT   ( event ) ) consumed_by = "on_UP_7_JAVASCRIPT";
        else if( on_UP_8_grouping     ( event ) ) consumed_by = "on_UP_8_grouping";
        else if( on_UP_9_onClick      (       ) ) consumed_by = "on_UP_9_onClick";
      //else if( on_UP_10_long_touch  ( event ) ) consumed_by = "on_UP_10_long_touch";

        // SYNCHRONIZE TIMERS {{{
        cancel_pending_button_to_magnify(caller);
        reset_GUI_idle_timer(caller);

        //}}}
        // HANDLE DONE MOVING VIEWS {{{
        //if( view_has_an_ACTION_UP_handler(gesture_down_SomeView_atXY) ) consumed_by = "view_has_an_ACTION_UP_handler";

        //}}}

            // [return consumed_by] {{{
//*EV1_RT_OK*/ Settings.MON(TAG_EV1_RT_OK, caller, "...return (consumed_by="+consumed_by+")");
            return (consumed_by != null);
            //}}}
    }
    //}}}
    // on_UP_1_handle {{{
    private boolean on_UP_1_handle()
    {
        //{{{
        String caller = "on_UP_1_handle";

        //}}}
        if(        is_magnify_np_showing() ) // {{{
        {
//*EV2_RT_CB*/Settings.MON(TAG_EV2_RT_CB, caller, "magnified np is showing: settings gesture_consumed_by_ACTION_UP");

            gesture_consumed_by_ACTION_UP = "is_magnify_np_showing"; // ...inhibit further onFling handling
        }
        // }}}
        else if(   is_view_showing(seekers_container) ) // {{{
        {
//*EV2_RT_CB*/Settings.MON(TAG_EV2_RT_CB, caller, "hiding seekers_container: setting gesture_consumed_by_ACTION_UP");
            seekers_container.setVisibility( View.GONE );

            gesture_consumed_by_ACTION_UP = "is_view_showing(seekers_container)"; // ...inhibit further onFling handling
        }
        // }}}
        else if(   is_mid_handle_current() // {{{
                || is_top_handle_current())
        {
//*EV2_RT_CB*/Settings.MON(TAG_EV2_RT_CB, caller, get_view_name( Handle.Get_cur_handle() )+" is showing: settings gesture_consumed_by_ACTION_UP");

            gesture_consumed_by_ACTION_UP = "is mid or top handle current"; // ...inhibit further onFling handling
        }
        // }}}
        // [return consumed_by] {{{
        String consumed_by = gesture_consumed_by_ACTION_UP;

//*EV2_RT_CB*/if(consumed_by != null) Settings.MON(TAG_EV2_RT_CB, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    // on_UP_2_band {{{
    private boolean on_UP_2_band()
    {
        // [drag_band] {{{
        if(drag_band == null) return false;

        String caller = "on_UP_2_band";
//*EV2_RT_CB*/Settings.MOC(TAG_EV2_RT_CB, caller);
//*EV2_RT_CB*/String drag_band_name = get_view_name(drag_band);//TAG_EV2_RT_CB
//*EV2_RT_CB*/String          h_x_v = String.format("HID=[%4d] ... x=[%4d] ... VIS=[%4d]", get_band_hid_X( drag_band ), (int)drag_band.getX(), get_band_vis_X( drag_band ));// TAG_EV2_RT_CB

        String consumed_by = null;
        //}}}
        // MOVED.... [drag_band] (horizontally) .. (decide for hide or show) {{{
        if( dragBand_moved )
        {
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "BAND_MOVED.....["+drag_band_name+"]: RELEASE_BAND_TO_CLOSEST_STATE "+h_x_v);
            schedule_release_dragBand(caller);

            consumed_by = caller+" (dragBand_moved)";
        }
        // }}}
        // NOT MOVED [hist_band] .. XXX EXPLAIN HIDE-ONLY ? XXX {{{
        else if(drag_band == hist_band)
        {
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "BAND_TOGGLE....["+drag_band_name+"]");
            //  if( is_dragBand_hidden(hist_band) )
            if( is_band_fully_hidden(hist_band) )
            {
                //release_dragBand(RELEASE_BAND_TO_VISIBLE_STATE, caller);
                open_band(hist_band, caller);
            }
            else {
                //release_dragBand(RELEASE_BAND_TO_HIDDEN_STATE , caller);
                fold_band(hist_band, caller);
            }
            consumed_by = caller+" (hist_band)";
        }
        // }}}
        // NOT MOVED [dock_band] .. (horizontal gesture) .. (i.e. not vertical as in SCROLLED) {{{
        // NOTE: dragBand_moved is true after a [a null-effect fling (top or bottom)]
        else if(drag_band == dock_band)
        {
            if( dragBand_scrolled ) {
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "BAND_SCROLLED..["+drag_band_name+"]: ...NO RELEASE "+h_x_v);
            }
            else {
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "BAND_DISMISSED.["+drag_band_name+"]: RELEASE_BAND_TO_HIDDEN_STATE "+h_x_v);
                release_dragBand(RELEASE_BAND_TO_HIDDEN_STATE, caller);
            }
            consumed_by = caller+" (drag_band)";
        }
        // }}}
        // [return consumed_by] {{{
//*EV2_RT_CB*/if(consumed_by != null) Settings.MON(TAG_EV2_RT_CB, caller, "...return true (consumed_by="+consumed_by+")");
            return (consumed_by != null);
        //}}}
    }
    //}}}
    // on_UP_3_mClamp {{{
    private boolean on_UP_3_mClamp(MotionEvent event)
    {
        if(mClamp.moving_np == null) return false;

        String caller = "on_UP_3_mClamp";
//*EV2_RT_CB*/Settings.MON(TAG_EV2_RT_CB, caller, "mClamp.moving_np=["+mClamp.moving_np+"]");

        mClamp.onTouchEvent(event); // (ACTION_UP)

        return false;
    }
    //}}}
    // on_UP_4_WEBVIEW_SWAP {{{
    private boolean on_UP_4_WEBVIEW_SWAP(MotionEvent event)
    {
        // (ACTION_UP ON EXPANDED WEBVIEW) {{{
        if( !fs_webView_expanded                                                        ) return false;
        if( !has_moved_enough                                                           ) return false;
        if( !fs_search_can_select_webview_on_ACTION_UP() ) return false;

//*EV2_RT_CB*/String caller = "on_UP_4_WEBVIEW_SWAP";//TAG_EV2_RT_CB
//*EV2_RT_CB*/Settings.MOC(TAG_EV2_RT_CB, caller);
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "............................has_moved_enough=["+has_moved_enough+"]");
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, ".........................fs_webView_expanded=["+fs_webView_expanded+"]");
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...fs_search_can_select_webview_on_ACTION_UP=["+fs_search_can_select_webview_on_ACTION_UP()+"]");

        //}}}
        // [WEBVIEW 1 2 3 SELECT] .. f(fs_search OR touched_wvContainer_border) {{{
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, caller+": ...calling expand_fs_webView_fullscreen_select_atX");
            expand_fs_webView_fullscreen_select_atX(event.getRawX(), false); // !toggle-off

//*EV2_RT_CB*/Settings.MON(TAG_EV2_RT_CB, caller, "...return true");
        return true;
        //}}}
    }
    //}}}
    // on_UP_5_wvTools {{{
    private boolean on_UP_5_wvTools(MotionEvent event)
    {
//*EV2_RT_CB*/Settings.MOC(TAG_EV2_RT_CB, "on_UP_5_wvTools");
        wvTools.on_UP(event);

        return false;
    }
    //}}}
    // on_UP_7_JAVASCRIPT {{{
    private boolean on_UP_7_JAVASCRIPT(MotionEvent event)
    {
        if( !is_a_webView(gesture_down_SomeView_atXY) ) return false;

        String caller = "on_UP_7_JAVASCRIPT";
        if( is_fs_webView_fullscreen_still_pending(caller) )
            cancel_pending_fs_webView_fullscreen(caller);

        MWebView wv = (MWebView)gesture_down_SomeView_atXY;

        String consumed_by =
            wv.onEventJS(event, (MWebView)gesture_down_SomeView_atXY)
            ? "onEventJS"
            :  null;

//*EV2_RT_CB*/if(consumed_by != null) Settings.MON(TAG_EV2_RT_CB, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
    }
    //}}}
    // on_UP_8_grouping {{{
    private boolean on_UP_8_grouping(MotionEvent event)
    {
        // (is_GROUPING) {{{
        if( !this_RTabsClient.is_GROUPING() ) return false;

        String caller = "on_UP_8_grouping("+Settings.Get_action_name(event)+")";
//*EV2_RT_CB*/Settings.MOC(TAG_EV2_RT_CB, caller);

        //}}}
        // [GROUP PEEK] .. (short slide over a URL-TAB)
        // - np_OnClickListener
        // - np_OnLongClickListener
        // - onScale
        // OR...
        // [GROUP STOP] .. f(background LONGPRESS) {{{
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, ".....Grouping_ACTION_UP_handled="+ this_RTabsClient.Grouping_ACTION_UP_handled);
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...this_RTabsClient.is_GROUPING="+ this_RTabsClient.is_GROUPING());
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...............has_moved_enough="+ has_moved_enough);

//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...........is_on_UP_LONGPRESS()="+ is_on_UP_LONGPRESS());
        if( is_on_UP_LONGPRESS() )
        {
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...LONGPRESS ON BACKGROUND");
            stop_GLOWING(caller);
            this_RTabsClient.stop_GROUPING(caller);
        }
        //}}}
        // [GROUP STOP] .. f(background CLICK) {{{
        else {
            float     x = (int)event.getX() + hsv.getScrollX() + vsv.getScrollX() + tabs_container.getScrollX();
            float     y = (int)event.getY() + hsv.getScrollY() + vsv.getScrollY() + tabs_container.getScrollY();
            NotePane np = this_RTabsClient.get_TABS_np_at_xy_closest((int)x,(int)y, caller);
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...get_TABS_np_at_xy_closest(x,y)=["+np+"]");

//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...CLICK ON BACKGROUND: "+(np == null));
            if(np == null)
            {
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...!Grouping_ACTION_UP_handled");

                stop_GLOWING(caller);
                this_RTabsClient.stop_GROUPING(caller);
            }
        }
        //}}}
        return false;
    }
    //}}}
    // on_UP_9_onClick {{{
    private boolean on_UP_9_onClick()
    {
        //{{{
        String caller = "on_UP_9_onClick";

        String consumed_by = null;
        //}}}
        if     ( has_moved_enough                                              )   consumed_by = "HAS_MOVED"   ;
        else if( click_callback_has_been_called                                )   consumed_by = "CB_CALLED"   ;
        else if( wvTools.is_a_marker(               gesture_down_SomeView_atXY))   consumed_by = "IS_A_MARKER" ;
        else if( RTabsClient.has_tab_np_button_view(gesture_down_SomeView_atXY))   consumed_by = "IS_A_TAB"    ;
        else if( gesture_down_SomeView_atXY                instanceof NpButton ) { consumed_by = "check_button_callback_on_ACTION_UP";
            check_button_callback_on_ACTION_UP((NpButton)gesture_down_SomeView_atXY, caller);
        }
        // [return consumed_by] {{{
//*EV2_RT_CB*/if(consumed_by != null) Settings.MON(TAG_EV2_RT_CB, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
//}}}
//    // on_UP_10_long_touch {{{
//    //{{{
//    private static final int CHECK_LAST_SENDTABCOMMAND_NP_BUTTON_DELAY = 500;
//
//    //}}}
//    private void on_UP_10_long_touch(MotionEvent event)
//    {
//        // return .. f(gesture_down_SomeView_atXY != null) {{{
//        if(gesture_down_SomeView_atXY != null)   return;
//
//        //}}}
//        // return .. (NOT A LONG CLICK) {{{
//        long time_elapsed  = System.currentTimeMillis() - last_ACTION_DOWN_time;
//        if(  time_elapsed <= CLICK_MIN_DURATION) return;
//
//        String caller = "on_UP_10_long_touch("+Settings.Get_action_name(event)+")";
//        //}}}
//        // POST CHECKING FOR NO TAB WAS CLICKED {{{
///*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "on_UP_10_long_touch: POST CHECKING FOR NO TAB WAS CLICKED");
//
//        handler.re_postDelayed( hr_toast_again, CHECK_LAST_SENDTABCOMMAND_NP_BUTTON_DELAY);
//        //}}}
//    }
//    private final Runnable hr_toast_again = new Runnable() {
//        @Override public void run() {
///*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "hr_toast_again: get_last_sendTabCommand_np_button=["+RTabsClient.get_last_sendTabCommand_np_button()+"]");
//
//            //if(RTabsClient.get_last_sendTabCommand_np_button() == null)
//                toast_again();
//        }
//    };
//    //}}}
//    // click_callback_has_been_called_by_this_RTabsClient {{{
//    public void set_click_callback_has_been_called_by_this_RTabsClient()
//    {
//        // setting click_callback_has_been_called from any other listeners
//        // a better solution would be not to need this
//        // .. see is_a_tabs_np in on_UP_9_onClick
//    }
//    //}}}
//        // [fs_webView_clear_drift] {{{
//        private void fs_webView_clear_drift()
//        {
//            String caller = "fs_webView_clear_drift";
///*EV2_RT_CB*/Settings.MOC(TAG_EV2_RT_CB, "fs_webView_clear_drift");
//
//            // WEBVIEW EXPANDED .. (drift back to initial position)
//            fs_webView.setX( Settings.WEBVIEW_MARGIN );
//            ((ViewGroup.MarginLayoutParams)fs_webView.getLayoutParams()).leftMargin = Settings.WEBVIEW_MARGIN;
//            fs_webView.requestLayout();
//            frameLayout.scrollTo(0, 0);
//
//        }
//        //}}}
    // is_on_UP_CLICK_DURATION {{{
    public boolean is_on_UP_CLICK_DURATION()
    {
        long        dt = System.currentTimeMillis() - last_ACTION_DOWN_time;
        boolean result = (dt < CLICK_MIN_DURATION);

//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "is_on_UP_CLICK_DURATION: ...return "+ result +" ("+dt+"ms < "+CLICK_MIN_DURATION+" ms)");
        return result;
    }
    //}}}
    // is_on_UP_LONGPRESS {{{
    public boolean is_on_UP_LONGPRESS()
    {
        long        dt = System.currentTimeMillis() - last_ACTION_DOWN_time;
        boolean result = (dt > LONGPRESS_DURATION);

//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "is_on_UP_LONGPRESS: ...return "+ result +" ("+dt+"ms < "+LONGPRESS_DURATION+" ms): "+(dt < LONGPRESS_DURATION));
        return result;
    }
    //}}}
    //}}}
    //* EV7_onFling */
    //{{{
    // onFling {{{
    private static final int WEBVIEW_GESTURE_MIN_VELOCITY   = 1000;

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        // (LOW VELOCITY) {{{
        String caller = "onFling";
//*EV1_RT_IN*/Settings.MOC(TAG_EV1_RT_IN, caller);

        //}}}
        // CHECK AGAINST [LOW VELOCITY] {{{
        float dx = Math.abs( velocityX );
        float dy = Math.abs( velocityY );
        float    min_gesture_velocity;

        if(drag_band == dock_band)
            min_gesture_velocity = DRAGFLING_GESTURE_MIN_VELOCITY;
        else
            min_gesture_velocity = fs_webview_session_running ? WEBVIEW_GESTURE_MIN_VELOCITY : TABVIEW_GESTURE_MIN_VELOCITY;

        if((dx < min_gesture_velocity) && (dy < min_gesture_velocity))
        {
//*EV7_RT_FL*/Settings.MOM(TAG_EV7_RT_FL, "LOW VELOCITY");

//*EV1_RT_OK*/ Settings.MON(TAG_EV1_RT_OK, caller, "...return false");
            return false;
        }
        //}}}
        // [RIGHT-LEFT-UP-DOWN] {{{
//*EV7_RT_FL*/Settings.MOM(TAG_EV7_RT_FL, "...................velocityXY=["+velocityX+" "+velocityY+"]");
//*EV7_RT_FL*/Settings.MOM(TAG_EV7_RT_FL, "...gesture_down_SomeView_atXY=["+ get_view_name( gesture_down_SomeView_atXY ) +"]");
//*EV7_RT_FL*/Settings.MOM(TAG_EV7_RT_FL, "....................drag_band=["+ get_view_name( drag_band )                  +"]");
//*EV7_RT_FL*/Settings.MOM(TAG_EV7_RT_FL, "...touched_wvContainer_border=["+ touched_wvContainer_border                  +"]");

        boolean      up_fling = (velocityY <  min_gesture_velocity) && (dy > 2*dx); // to be considered
        boolean   right_fling = (velocityX >  min_gesture_velocity) && (dx > 2*dy); // MAJOR DIRECTION velocity
        boolean    down_fling = (velocityY >  min_gesture_velocity) && (dy > 2*dx); // SIGNIFICANT enough
        boolean    left_fling = (velocityX < -min_gesture_velocity) && (dx > 2*dy); // should be twice MINOR's

        String  consumed_by  = null;
        //}}}
// [TRACE] {{{
//*EV7_RT_FL*/ trace_open   (TAG_EV7_RT_FL, "onFling");
//*EV7_RT_FL*/ trace_string (TAG_EV7_RT_FL,     "gesture_consumed_by_ACTION_UP.", gesture_consumed_by_ACTION_UP     );
//*EV7_RT_FL*/ trace_string (TAG_EV7_RT_FL,       "gesture_consumed_by_magnify.", gesture_consumed_by_magnify       );
//*EV7_RT_FL*/ trace_boolean(TAG_EV7_RT_FL,        "touched_wvContainer_border.", touched_wvContainer_border        );
//*EV7_RT_FL*/ trace_boolean(TAG_EV7_RT_FL,                          "up_fling.", up_fling                          );
//*EV7_RT_FL*/ trace_boolean(TAG_EV7_RT_FL,                        "down_fling.", down_fling                        );
//*EV7_RT_FL*/ trace_boolean(TAG_EV7_RT_FL,                        "left_fling.", left_fling                        );
//*EV7_RT_FL*/ trace_boolean(TAG_EV7_RT_FL,                       "right_fling.", right_fling                       );
//}}}

        if(           onFling_1_FG_VIEW ()                                                                    ) consumed_by = "onFling_1_FG_VIEW";
        else if(      onFling_2_WVTOOLS (right_fling, left_fling, up_fling, down_fling, velocityX, velocityY) ) consumed_by = "onFling_2_WVTOOLS";
        else if(check_onFling_enabled(caller) ) {
            if     (  onFling_3_WEBVIEW (right_fling, left_fling, up_fling, down_fling)                       ) consumed_by = "onFling_3_WEBVIEW";
            else if(  onFling_4_BAND    (right_fling, left_fling, up_fling, down_fling, velocityX, velocityY) ) consumed_by = "onFling_4_BAND"   ;
            else if(  onFling_5_SETTLE  (right_fling, left_fling, up_fling, down_fling)                       ) consumed_by = "onFling_5_SETTLE" ;
        }

        toast_again_clear();

        // [gesture_consumed_by_onFling] .. (steal event from other handlers) {{{
        gesture_consumed_by_onFling = consumed_by; // SO THAT:
        // ................................... + ..........(release_dragBand) TO IGNORE: HIDE or SHOW band
        // ................................... + ..............(fs_wtitle_CB) TO IGNORE: reload URL on ACTION_UP
        // ................................... + (magnify_np_OnClickListener) TO IGNORE: MAGNIFIED-BUTTON on ACTION_UP

//*EV7_RT_FL*/ trace_string (TAG_EV7_RT_FL,        "gesture_consumed_by_onFling", gesture_consumed_by_onFling       );
        //}}}
// [TRACE] {{{
//*EV7_RT_FL*/ trace_close  (TAG_EV7_RT_FL);

//*EV1_RT_OK*/ Settings.MON(TAG_EV1_RT_OK, caller, "...return (consumed_by="+consumed_by+")");
        return (consumed_by != null);
//}}}
    }
    //}}}
    // onFling_1_FG_VIEW {{{
    private boolean onFling_1_FG_VIEW()
    {
        // [is_fg_view_showing] [gesture_down_SomeView_atXY] {{{
        String caller = "onFling_1_FG_VIEW";
//*EV7_RT_FL*/Settings.MOC(TAG_EV7_RT_FL, caller);

        if( !is_fg_view_showing()                     ) return false;
        if(gesture_down_SomeView_atXY != get_fg_view()) return false;
        //}}}
        // INHIBIT CLICK HANDLING {{{
        RTabsClient.set_TAB_GESTURE_handled_by_fling();

        //}}}
        // FLINGING [mClamp.moving_np] {{{
        if(mClamp.moving_np != null)
        {
            String profile_entry = Settings.get_profile_name_from_tag( mClamp.moving_np.tag );
//*EV7_RT_FL*/Settings.MOM(TAG_EV7_RT_FL, "...handle fg_view fling on profile history entry=["+profile_entry+"]");
            if(        !TextUtils.isEmpty                  ( profile_entry )
                    && !Settings.is_a_dynamic_profile_entry( profile_entry )
                    && !Settings.USER_INDEX.equals         ( profile_entry )
              ) {
                // hide [mClamp.moving_np]
                View view  = NotePane.Get_view( mClamp.moving_np );
                if(view != null)
                    view.setVisibility( View.GONE );

                // stopping [mClamp.moving_np] .. (Note: mClamp will release moving_np at this point)
                mClamp.clamp_moving_np_in_place(caller);

                // remove [mClamp.moving_np] from hist entry
                this_RTabsClient.history_remove( profile_entry );

                // rebuild layout .. (or not! just leave the empty space)
                //show_PROFHIST_TABLE(caller);

                // update history back_nb and frwd_nb
                update_histBand(caller);
              }
        }
        // }}}
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "...return true");
        return true;
    }
    //}}}
    // onFling_2_WVTOOLS {{{
    private boolean onFling_2_WVTOOLS(boolean right_fling, boolean left_fling, boolean up_fling, boolean down_fling, float velocityX, float velocityY)
    {
        // [touched_wvContainer_border] {{{
        String caller = "onFling_2_WVTOOLS";
//*EV7_RT_FL*/Settings.MOC(TAG_EV7_RT_FL, caller);

        if( touched_wvContainer_border ) return false;
        //}}}
        // [wvTools.onFling] {{{
        boolean result = wvTools.onFling(right_fling, left_fling, up_fling, down_fling, velocityX, velocityY);

//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "...return "+result);
        return result;
        //}}}
    }
    //}}}
    // onFling_3_WEBVIEW .. [WEBVIEW] (swap prev<->next) .. (split +/-) (UP: hide wv BG) (DOWN: hide wv) {{{
    private boolean onFling_3_WEBVIEW(boolean right_fling, boolean left_fling, boolean up_fling, boolean down_fling)
    {
        // [fs_webview_session_running] {{{
        String caller = "onFling_3_WEBVIEW";
//*EV7_RT_FL*/Settings.MOC(TAG_EV7_RT_FL, caller);

        if( !fs_webview_session_running ) return false;

        //(gesture_down_SomeView_atXY == null        )  // ...not in a WebView component
        //(gesture_down_SomeView_atXY == wvContainer )) // ...in WebView frame
        //(gesture_down_SomeView_atXY == fs_wtitle   )  // ...in title 1
        //(gesture_down_SomeView_atXY == fs_wtitle2  )  // ...in title 2
        //(gesture_down_SomeView_atXY == fs_wtitle3  )  // ...in title 3

        String consumed_by = null;
        //}}}
        // [clamp_moving_np_in_place] {{{
        if(mClamp.moving_np != null)
        {
//*EV7_RT_FL*/Settings.MOM(TAG_EV7_RT_FL, "* STOP mClamp to avoid ViewRootImpl$CalledFromWrongThreadException *");

            mClamp.clamp_moving_np_in_place(caller);
        }
        //}}}
        // WEBVIEW SWAP {{{
        if((consumed_by == null) && fs_webView_expanded)
        {
            if(right_fling) {
                expand_fs_webView_fullscreen_prev(caller);
                consumed_by = "expand_fs_webView_fullscreen_prev";
            }
            else if(left_fling) {
                expand_fs_webView_fullscreen_next(caller);
                consumed_by = "expand_fs_webView_fullscreen_next";
            }
        }
        //}}}
        // WEBVIEW RIGHT FLING {{{
        if((consumed_by == null) && right_fling)
        {
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "RIGHT: SCREEN_SPLIT=["+ Settings.SCREEN_SPLIT +"] -1");

            if( Settings.SCREEN_SPLIT  > 1)
            {
                adjust_WEBVIEW_LAYOUT(Settings.SCREEN_SPLIT-1, caller);
                shedule_update_WEBVIEW_TITLES_AND_COLORS(caller);
            }
            else {
                if     (gesture_down_SomeView_atXY != null) blink_view(gesture_down_SomeView_atXY, Color.DKGRAY  & 0xa0FFFFFF);
                else if(gesture_down_Wti_or_Wc_atY != null) blink_view(gesture_down_Wti_or_Wc_atY, Color.DKGRAY  & 0xa0FFFFFF);
            }
        }
        // }}}
        // WEBVIEW LEFT FLING {{{
        else if((consumed_by == null) && left_fling)
        {
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "LEFT: SCREEN_SPLIT=["+ Settings.SCREEN_SPLIT +"] +1");

            if( Settings.SCREEN_SPLIT  < 3)
            {
                adjust_WEBVIEW_LAYOUT(Settings.SCREEN_SPLIT+1, caller);
                shedule_update_WEBVIEW_TITLES_AND_COLORS(caller);
            }
            else {
                if(gesture_down_SomeView_atXY != null) blink_view(gesture_down_SomeView_atXY, Color.DKGRAY  & 0xa0FFFFFF);
                if(gesture_down_Wti_or_Wc_atY != null) blink_view(gesture_down_Wti_or_Wc_atY, Color.DKGRAY  & 0xa0FFFFFF);
            }
        }
        // }}}
        // WEBVIEW UP FLING {{{
        else if((consumed_by == null) && up_fling)
        {
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "UP: calling magnify_np_hide");

            magnify_np_hide(caller);
        }
        // }}}
        // WEBVIEW DOWN FLING {{{
        else if((consumed_by == null) && down_fling)
        {
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "DOWN: calling fs_webview_session_stop");

            fs_webview_session_stop(caller+": down_fling");
        }
        // }}}
        // FLING NULL EFFECT {{{
        else if(consumed_by == null)
        {
//*EV7_RT_FL*/Settings.MOM(TAG_EV7_RT_FL, caller+": NONE OF [RIGHT, LEFT, DOWN OR UP] FLING: NULL EFFECT");

        }
        //}}}
        // [return consumed_by] {{{
//*EV7_RT_FL*/if(consumed_by != null) Settings.MON(TAG_EV7_RT_FL, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    // onFling_4_BAND {{{
    private boolean onFling_4_BAND(boolean right_fling, boolean left_fling, boolean up_fling, boolean down_fling, float velocityX, float velocityY)
    {
        //{{{
        String caller = "onFling_4_BAND";
//*EV7_RT_FL*/Settings.MOC(TAG_EV7_RT_FL, caller);

//*EV7_RT_FL*/ trace_boolean(TAG_EV7_RT_FL, "is_GUI_TYPE_HANDLES", Settings.is_GUI_TYPE_HANDLES());
        if( Settings.is_GUI_TYPE_HANDLES() ) return false; // ...no band to fling in HANDLES-MODE
        //}}}
        // [!can_band_fling] .. (return false) {{{
        boolean show_band_showing = is_view_showing(show_band);         // show bands visible
        boolean      dock_showing = is_view_showing(dock_band);         // dock       visible
        boolean      hist_showing = is_view_showing(hist_band);         // hist_band  visible
        boolean      hist_pinned  = hist_showing && !show_band_showing; // +hist_band does not auto-hide
        boolean      hist_sliding = hist_showing &&  show_band_showing; // +hist_band can be hidden by hand

        boolean can_band_fling    =                                     // can_band_fling
            (          dock_showing                                     //     HIST <- DOCK (or PROFILE SCROLLING)
                   || (is_at_left_margin() && show_band_showing)        //  NOTHING -> HIST
                   || !hist_pinned                                      //  NOTHING <- HIST -> DOCK
            );
//*EV7_RT_FL*/ trace_boolean(TAG_EV7_RT_FL,               "is_at_left_margin()."  , is_at_left_margin()               );
//*EV7_RT_FL*/ trace_boolean(TAG_EV7_RT_FL,               "dragBand_was_hiding."  , dragBand_was_hiding               );
//*EV7_RT_FL*/ trace_boolean(TAG_EV7_RT_FL,                  "can_band_fling==."  , can_band_fling                    );
//*EV7_RT_FL*/ trace_boolean(TAG_EV7_RT_FL,               "show_band_showing.1."  , show_band_showing                 );
//*EV7_RT_FL*/ trace_boolean(TAG_EV7_RT_FL,                    "dock_showing.2."  , dock_showing                      );
//*EV7_RT_FL*/ trace_boolean(TAG_EV7_RT_FL,                    "hist_showing.3."  , hist_showing                      );
//*EV7_RT_FL*/ trace_boolean(TAG_EV7_RT_FL,                     "hist_pinned.+."  , hist_pinned                       );
//*EV7_RT_FL*/ trace_boolean(TAG_EV7_RT_FL,                    "hist_sliding.+."  , hist_sliding                      );

        if( !can_band_fling )
        {
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "...return false .. (!can_band_fling)");
            return false;
        }
        //}}}
        // LIST or BAND {{{
        boolean is_band_fling_1_list_to_working_profile = false;
        boolean is_band_fling_2_list_up_or_down         = false;
        boolean is_band_fling_3_band_cycle              = false;
        boolean is_band_fling_4_band_up                 = false;
        boolean is_band_fling_5_band_down               = false;

        is_band_fling_1_list_to_working_profile
            =   right_fling                                 // [dock_band] horizontal scrolling to [Working_profile]
            &&   (drag_band == dock_band)
            &&   !dock_band.is_scroll_animation_running()
            ;

        is_band_fling_2_list_up_or_down
            =   !is_band_fling_1_list_to_working_profile
            &&   (up_fling || down_fling)                   // [dock_band] vertical scrolling
            &&   (drag_band == dock_band)
            ;

        is_band_fling_3_band_cycle
            =  !is_band_fling_2_list_up_or_down
            && (left_fling || right_fling)                  // [drag_band] horizontal swapping
            ;

        is_band_fling_4_band_up
            =  !is_band_fling_3_band_cycle
            && (drag_band != null)
            && up_fling
            ;

        is_band_fling_5_band_down
            =  !is_band_fling_3_band_cycle
            && (drag_band != null)
            && down_fling
            ;

//*EV7_RT_FL*/ trace_boolean(TAG_EV7_RT_FL, "is_band_fling_1_list_to_working_profile." , is_band_fling_1_list_to_working_profile );
//*EV7_RT_FL*/ trace_boolean(TAG_EV7_RT_FL, "is_band_fling_2_list_up_or_down."         , is_band_fling_2_list_up_or_down         );
//*EV7_RT_FL*/ trace_boolean(TAG_EV7_RT_FL, "is_band_fling_3_band_cycle."              , is_band_fling_3_band_cycle              );
//*EV7_RT_FL*/ trace_boolean(TAG_EV7_RT_FL, "is_band_fling_4_band_up..."               , is_band_fling_4_band_up                 );
//*EV7_RT_FL*/ trace_boolean(TAG_EV7_RT_FL, "is_band_fling_5_band_down."               , is_band_fling_5_band_down               );

        //}}}
        // [is_band_fling_1_list_to_working_profile ] {{{
        if( is_band_fling_1_list_to_working_profile )
        {
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "FLING LIST TO WORKING PROFILE");

            dock_scroll_to_working_profile(    "RIGHT-FLING");
        }
        // }}}
        // [is_band_fling_2_list_up_or_down] {{{
        else if( is_band_fling_2_list_up_or_down )
        {
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "FLING LIST UP OR DOWN");

            //...............................startX,               startY  ,      velocityX,      velocityY, minX, maxX, minY, maxY
            //  dock_band.container_sv.fling(0     , drag_band.getScrollY(), (int)velocityX, (int)velocityY, 0   , 0   , 0   , dock_band.getHeight());

            dock_band.container_sv.fling(-(int)velocityY);
        }
        //}}}
        // [is_band_fling_3_band_cycle] {{{
        else if( is_band_fling_3_band_cycle )
        {
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "BAND FLING CYCLE");

            on_fling_hist_dock_cycle( right_fling, left_fling
                    ,      dock_showing
                    ,      hist_pinned, hist_showing, hist_sliding
                    );
            // CLEAR BAND DRAG WHEN DONE
            if(drag_band != null) clear_dragBand(caller+": is_band_fling_3_band_cycle" );
        }
        //}}}
        // [is_band_fling_4_band_up] .. (no implementation yet) {{{
        else if( is_band_fling_4_band_up ) {
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "BAND FLING UP .. (no implementation yet) ");

        }
        //}}}
        // [is_band_fling_5_band_down] .. (no implementation yet) {{{
        else if( is_band_fling_5_band_down ) {
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "BAND FLING DOWN .. (no implementation yet) ");

        }
        //}}}
        // NO BAND EFFECT {{{
        else {
//*EV7_RT_FL*/Settings.MOM(TAG_EV7_RT_FL, caller+"BAND FLING NO EFFECT .. drag_band=["+get_view_name(drag_band)+"]");

        }

//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "...return true");
        return true;
        //}}}
    }
    //}}}
    // onFling_5_SETTLE {{{
    private boolean onFling_5_SETTLE(boolean right_fling, boolean left_fling, boolean up_fling, boolean down_fling)
    {
        //{{{
        String caller = "onFling_5_SETTLE";
//*EV7_RT_FL*/Settings.MOC(TAG_EV7_RT_FL, caller);

        String consumed_by = null;
        //}}}
        // FLING EFFECT ON A MOVED [drag_band] {{{
        if(drag_band != null)
        {
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "(dragBand_moved="+dragBand_moved+")");
            if( dragBand_moved ) {
                if( right_fling ) release_dragBand(RELEASE_BAND_TO_VISIBLE_STATE, caller); // either one or the other...
                else              release_dragBand(RELEASE_BAND_TO_HIDDEN_STATE , caller); // ...in order to avoid undetermined behaviors

                consumed_by = "dragBand_moved";
            }
        }
        //}}}
        // [!gesture_consumed_by_ACTION_UP] {{{
        if(gesture_consumed_by_ACTION_UP==null)
        {
//*EV7_RT_FL*/Settings.MOM(TAG_EV7_RT_FL, "gesture_consumed_by_ACTION_UP=["+gesture_consumed_by_ACTION_UP+"]");
            // SOMETHING TO SHOW .. f(right_fling) {{{
            if(right_fling)
            {
                if( Settings.is_GUI_TYPE_HANDLES() )
                {
                    apply_GUI_STATE(GUI_STATE_DOCK, FROM_GESTURE, caller+": right_fling");
                    consumed_by = "right_fling .. (apply_GUI_STATE)";
                }
                else if(Handle.Get_cur_handle() != dck_handle)
                {
                    show_DOCKINGS_TABLE(caller+": right_fling");
                    consumed_by = "right_fling .. (show_DOCKINGS_TABLE)";
                }

                //clear_gesture_waiting_for_ACTION_UP(caller, "right_fling");
            }
            //}}}
            // SOMETHING TO HIDE .. f(left_fling) {{{
            else if(left_fling)
            {
                if(Handle.Get_cur_handle() != null)
                {
                    handle_hide(caller+": left_fling");
                    consumed_by = "left_fling .. (handle_hide)";
                }

                //clear_gesture_waiting_for_ACTION_UP(caller, "left_fling");
            }
            //}}}
        }
        //}}}
        // [show_band_show] .. (unless otherwise handled) {{{
        if(        (drag_band == null)
                && !Settings.is_GUI_TYPE_HANDLES()
                && !is_view_showing(hist_band)
                && !is_view_showing(show_band)
          )
        {
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "RESTORE MISSING SHOWBAND");

            show_band_show(caller);
        }
        // }}}
        // [return consumed_by] {{{
//*EV7_RT_FL*/if(consumed_by != null) Settings.MON(TAG_EV7_RT_FL, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    // on_fling_hist_dock_cycle {{{
    private void on_fling_hist_dock_cycle(boolean right_fling, boolean left_fling
            , boolean dock_showing
            , boolean hist_pinned, boolean hist_showing, boolean hist_sliding)
    {
        //{{{
        String caller = "on_fling_hist_dock_cycle";
//*EV7_RT_FL*/Settings.MOC(TAG_EV7_RT_FL, caller);

        //}}}
        // SOMETHING TO SHOW .. [right_fling] .. (hist_band->>>) (dock_band->>>) (show_band_show<<<->>>hist_band) {{{
        boolean consumed = false;
        if(right_fling) {
            // HIST-RIGHT-FLING
            if(drag_band == hist_band)
            {
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "HIST-RIGHT-FLING");
                if( dragBand_was_hiding ) show_histBand(FROM_GESTURE, caller+": hist_band"); // NOTHING -> HIST
                else                      show_dock    (FROM_GESTURE, caller+": hist_band"); // ...........HIST -> DOCK
            }
            // DOCK-RIGHT-FLING
            else if((drag_band == dock_band) || (hist_sliding && !dock_showing))
            {
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "DOCK-RIGHT-FLING");
                show_dock(FROM_GESTURE, caller+": hist_sliding");                            // .......... HIST -> DOCK
            }
            // HIST_PINNED-RIGHT-FLING
            else if(hist_pinned)
            {
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "HIST_PINNED-RIGHT-FLING");
                show_band_show      (caller+": hist_pinned");                                // HIST ----> HIST.hist_sliding
                // [FREEZED] [OFFLINE] [TABS] [BACKGROUND]
                //sync_tabs_scrolling(caller+": hist_pinned"); // ...see ACTION_UP
                //set_tabs_scrolling( true, caller);
            }
            else {
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "RIGHT-FLING: NULL EFFECT");
            }
        }
        // }}}
        // SOMETHING TO HIDE .. [ left_fling] .. (<<<-fold_band) (<<<-hist_band) (<<<-dock_band) {{{
        else if(left_fling)
        {
            // DOCK-LEFT-FLING
            if( dock_showing )
            {
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "DOCK-LEFT-FLING");
                show_histBand(FROM_GESTURE, caller+": dock_showing");
            }
            // HIST-LEFT-FLING
            else if((drag_band == hist_band) || (hist_showing))// && is_at_left_margin()))
            {
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "HIST-LEFT-FLING");
                fold_band(hist_band, caller);
            }
            else {
//*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "LEFT-FLING: NULL EFFECT");
            }
        }
        // }}}
    }
    //}}}
//    // is_fling_on_some_handle {{{
//    private boolean is_fling_on_some_handle(String caller)
//    {
//        caller += "->is_fling_on_some_handle";
//        String reject_source = null;
//        if     ( is_handle_showing(top_handle       ) ) reject_source = "top_handle_showing"       ;
//        else if( is_handle_showing(mid_handle       ) ) reject_source = "mid_handle_showing"       ;
//        else if( is_handle_showing(bot_handle       ) ) reject_source = "bot_handle_showing"       ;
//        else if( is_view_showing  (seekers_container) ) reject_source = "seekers_container_showing";
////*EV7_RT_FL*/Settings.MON(TAG_EV7_RT_FL, caller, "...return "+ ((reject_source!=null) ? "YES: reject_source=["+reject_source+"]" : "NO: no reject_source found"));
//        return (reject_source != null);
//    }
//    //}}}
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ GESTURE @ }}}
    /** CLAMP */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ (Interface) ClampListener @ {{{
    // 1 clamp1_has_a_grid_for {{{
    public boolean clamp1_has_a_grid_for(NotePane np, float[] grid_w_h_s)
    {

        boolean result = false;
        if((this_RTabsClient != null) && this_RTabsClient.TABS_Map.containsValue(np))
        {
            grid_w_h_s[0] = Settings.TAB_GRID_S;
            grid_w_h_s[1] = Settings.TAB_GRID_S;
            grid_w_h_s[2] = Settings.DEV_SCALE;
            result = true;
        }
        else {
            result = wvTools.clamp1_has_a_grid_for(np, grid_w_h_s);
        }

//*CLAMP*/Settings.MOC(TAG_CLAMP, "clamp1_has_a_grid_for("+np+") ...return "+result+ String.format("%2f %2f %.1f", grid_w_h_s[0], grid_w_h_s[1], grid_w_h_s[2]));
        return result;
    }
    //}}}
    // 2 clamp2_get_np_to_grid_xy {{{
    public void clamp2_get_np_to_grid_xy(View view, int[] grid_xy)
    {
        grid_xy[0] = grid_xy[1] = 0;

        if(  view == null) return;

        // CURRENT VIEW POSITION
        float b_l  = view.getX();
        float b_t  = view.getY();

        // ZOOM INDEPENDENT COORDINATES
        float x    = (b_l - mClamp.margin_left ) / Settings.DEV_SCALE;
        float y    = (b_t                      ) / Settings.DEV_SCALE;

        // CLOSEST GRID COORDINATES
        grid_xy[0] = (int)(x + Settings.TAB_GRID_S / 2) / Settings.TAB_GRID_S;
        grid_xy[1] = (int)(y + Settings.TAB_GRID_S / 2) / Settings.TAB_GRID_S;
//*CLAMP*/Settings.MOC(TAG_CLAMP, "clamp2_get_np_to_grid_xy(view, grid_xy) ...return ["+grid_xy[0]+" @ "+grid_xy[1]+"]");
    }
    //}}}
    // 3 clamp3_handle_not_moved {{{
    public void clamp3_handle_not_moved()
    {
        String caller = "clamp3_handle_not_moved";
        if(mClamp.moving_np.button != null)
        {
            int cart_state =  get_cart_state(caller);
            switch( cart_state )
            {
                // [cart_save_from] or [cart_extract_to] {{{
                case Settings.CART_STATE_DEL:
//*CLAMP*/Settings.MON(TAG_CLAMP, caller, "calling cart_save_from");
                    this_RTabsClient.cart_save_from(  mClamp.moving_np );
                    // BUTTON PULSE
                    if(mClamp.moving_np.button != null) {
                        pulse_np_button( mClamp.moving_np.button );
                        raise_np_button(        cart_band.see_nb );
                    }
                    break;

                case Settings.CART_STATE_ADD:
//*CLAMP*/Settings.MON(TAG_CLAMP, caller, "calling cart_extract_to");
                    this_RTabsClient.cart_extract_to(  mClamp.moving_np );
                    // BUTTON PULSE
                    if(mClamp.moving_np.button != null) {
                        pulse_np_button(        cart_band.see_nb );
                        raise_np_button( mClamp.moving_np.button );
                    }
                    break;

                    // }}}
                default:
//                    // TODO should it be only handled by fs_search_CB ? XXX {{{
//                    if(mClamp.moving_np != null)
//                    {
///*CLAMP*/Settings.MON(TAG_CLAMP, caller, "toggle fs_webView_expanded");
//
///*CLAMP*/Settings.MOM(TAG_CLAMP, "......mClamp.moving_np.button=["+ get_view_name(mClamp.moving_np.button)    +"]");
///*CLAMP*/Settings.MOM(TAG_CLAMP, "...gesture_down_SomeView_atXY=["+ get_view_name(gesture_down_SomeView_atXY) +"]");
///*CLAMP*/Settings.MOM(TAG_CLAMP, ".............has_moved_enough=["+ has_moved_enough                          +"]");
///*CLAMP*/Settings.MOM(TAG_CLAMP, ".........is_on_UP_LONGPRESS()=["+ is_on_UP_LONGPRESS()                      +"]");
//
//                        // CLIK: (WEBVIEW FULLSCREEN TOGGLE)
//                        if(        (mClamp.moving_np.button == gesture_down_SomeView_atXY)
//                                && !has_moved_enough
//                                && !is_on_UP_LONGPRESS()
//                          ) {
//                            float rawX = mClamp.moving_np.button.getX() + mClamp.moving_np.button.getWidth ()/2;
//                            float rawY = mClamp.moving_np.button.getY() + mClamp.moving_np.button.getHeight()/2;
//                            expand_fs_webView_fullscreen_select_atX(rawX, true); // toggle on-off
//                          }
//
//                    }
//                    //}}}
            }
        }
    }
    //}}}
    // 4 clamp4_get_bounce_playground {{{
    public boolean clamp4_get_bounce_playground(NotePane np, Rect playground_rect)
    {
        // view {{{
        View          view  = NotePane.Get_view( np );
        if(view == null) return false;
        int        button_w =  view  .getWidth ();
        int        button_h =  view  .getHeight();

        // }}}
        // parent {{{
        ViewGroup    parent = (ViewGroup) view.getParent();
        if(parent == null) return false;
        boolean is_a_tab_np = (parent == tabs_container);
        int        parent_w =  parent.getWidth ();
        int        parent_h =  parent.getHeight();

        // }}}
        // viewport extensibility {{{
        int extra_w = is_a_tab_np ? button_w / 2 : 0;
        int extra_h = is_a_tab_np ? button_h / 2 : 0;
        //}}}
        // bouncing frame {{{
        /* x_min */ playground_rect.left   = (is_a_tab_np ? Settings.VIEWPORT_L                       :        0)          ;
        /* y_min */ playground_rect.top    = (is_a_tab_np ? Settings.VIEWPORT_T                       :        0)          ;
        /* x_max */ playground_rect.right  = (is_a_tab_np ? Settings.VIEWPORT_L + Settings.VIEWPORT_W : parent_w) + extra_w;
        /* y_max */ playground_rect.bottom = (is_a_tab_np ? Settings.VIEWPORT_T + Settings.VIEWPORT_H : parent_h) + extra_h;
        // }}}
        return true;
    }
    //}}}
    // 5 clamp5_get_gravityPoint {{{
    public boolean clamp5_get_gravityPoint(NotePane np, Point gravityPoint)
    {
        if(np        == null) return false;
        if(np.button == null) return false;

        // for [fs_search] .. (pick first wvTools layout cell) {{{
        Rect r = null;
        if(np.button == wvTools.fs_search)
        {
            NpButton wvTools_sbX = wvTools.get_sbX_containing_tool_button( wvTools.fs_search );
            if( is_view_showing( wvTools_sbX ) )
            {
                // FRAME GEOMETRY .. f(wvTools_sbX) {{{
                boolean  at_left   = wvTools_sbX.at_left;

                int frame_y        = (int)wvTools_sbX.getY();
                int frame_x        = (int)wvTools_sbX.getX();
                int frame_w        = wvTools_sbX.getWidth ();
                int frame_h        = wvTools_sbX.getHeight();
                int frame_margin   = Settings.SCROLLBAR_W_MIN; // tinted body
                int frame_right    = frame_x+frame_w;

                int cell_size      = Settings.TOOL_BADGE_SIZE;

                int x_min = (at_left) ? frame_x     + frame_margin              // may not overlap scrollbar body to the left
                    :                   frame_x                               ; // may touch left  frame border
                int x_max = (at_left) ? frame_right                - cell_size  // may touch right frame border
                    :                   frame_right - frame_margin - cell_size; // may not overlap scrollbar body to the right
                int y_min =             frame_y                               ; // below    top frame border
                int y_max =             frame_y     + frame_h      - cell_size; // above  bottom frame border

                y_min    += Settings.FS_SELECT_BORDER;
                y_max    -= Settings.FS_SELECT_BORDER;
                x_min    += Settings.FS_SELECT_BORDER;
                x_max    -= Settings.FS_SELECT_BORDER;

                //}}}
                r = at_left
                    ? new Rect(x_max, y_min , x_max+cell_size, y_min+cell_size)
                    : new Rect(x_min, y_min , x_min+cell_size, y_min+cell_size);
            }
        }
        //}}}
        // .. or pick the closest rest-zone rectangle
        else {
            r = wvTools.get_nearest_rest_zone( np.button );
        }
        if(r == null) return false;

        int      r_w = r.right  - r.left;
        int      r_h = r.bottom - r.top ;
        int center_x = r.left   + r_w/2 ;
        int center_y = r.top    + r_h/2 ;

        // where button's top-left should be confined to
        int        b_w = np.button.getWidth ();
        int        b_h = np.button.getHeight();
        gravityPoint.x = center_x - b_w/2     ;
        gravityPoint.y = center_y - b_h/2     ;

        return true;
    }
    //}}}
    // 6 clamp6_run_move_inertia {{{
    public void clamp6_run_move_inertia(final Clamp clamp)
    {
//*CLAMP*/Settings.MOC(TAG_CLAMP, "clamp6_run_move_inertia("+clamp.moving_np+")");
      //clamp.move_inertia();
        handler.re_post( hr_mClamp_move_inertia);
    }
    //}}}
    // 7 clamp7_is_dragging_something {{{
    public boolean clamp7_is_dragging_something()
    {
//*CLAMP*/Settings.MOC(TAG_CLAMP, "clamp7_is_dragging_something");
        return false; // TODO
    }
    //}}}
    // 8 drag {{{
    public void clamp8_drag(float x, float y)
    {
//*CLAMP*/Settings.MOC(TAG_CLAMP, "clamp8_drag(x,y)");
        // TODO
    }
    //}}}
    // 9 clamp9_bounced {{{
    public void clamp9_bounced(NotePane moving_np)
    {
//*CLAMP*/Settings.MOC(TAG_CLAMP, "clamp9_bounced("+moving_np+")");
        // DONG
        play_sound_click("clamp9_bounced");
    }
    //}}}
    // 10 clamp10_onClamped {{{
    public boolean clamp10_onClamped(NotePane moving_np)
    {
//*CLAMP*/Settings.MOC(TAG_CLAMP, "clamp10_onClamped("+moving_np+")");

// TODO (when clicked)
// mWebView.loadUrl( "javascript:( document.body.style.fontSize        = '20pt'   );" );
// javascript: ( function() { var styles=' * { color: #ffe !important; } * { font-family:"Comic sans ms" ! important; } * { font-size:18px ! important; } body,div,p { background: #333 !important; } a { color: #44f !important; } a:visited { color: #a4f !important; } '; var newSS = document.createElement('link'); newSS.rel='stylesheet'; newSS.href='data:text/css,'+escape(styles); document.getElementsByTagName("head")[0].appendChild(newSS); } )();

        // UPDATE PROFILE
        View        view = NotePane.Get_view( moving_np );
        ViewGroup parent = (ViewGroup)view.getParent();
        boolean is_a_tab_np = (parent == tabs_container);
        if( is_a_tab_np )
            Profile.Update_TAB(Settings.Working_profile_instance, moving_np); // .. (not committed to file: must be checked by load_USER_PROFILE before leaving current profile)

        hr_onClamped_moving_np = moving_np.button;
        handler.re_post( hr_onClamped );

        return true; // release moving_np
    }

    NpButton hr_onClamped_moving_np = null;

    private final Runnable hr_onClamped = new Runnable()
    {
        @Override
        public void run()
        {
            String caller = "hr_onClamped";
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller);

            // UPDATE DISPLAY .. (take care of shape and color adjustements) {{{
            RTabsClient.TABS_Map_Has_Changed       = true; // forget cached attributes .. (i.e. moving_np has moved)
            this_RTabsClient.apply_TABS_LAYOUT     (RTabsClient.TABS_Map, tabs_container, caller);
            this_RTabsClient.apply_SETTINGS_PALETTE(RTabsClient.TABS_Map                , caller);

            // }}}
            // DING .. (when ajusting with touching neighbor) {{{
            if(hr_onClamped_moving_np != null)
            {
                if( hr_onClamped_moving_np.binds_with_neighbors() ) // neighbor docking
                    play_sound_ding("clamped");

                hr_onClamped_moving_np = null;
            }
            //}}}
        }
    };
    //}}}
    // hr_mClamp_move_inertia {{{
    private Runnable hr_mClamp_move_inertia = new Runnable() {
        @Override public void run() { mClamp.move_inertia(); }
    };

    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** GUI */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ GUI @ {{{
    //{{{
    // check_scrolling_allowed {{{
    public boolean check_scrolling_allowed(String caller)
    {
        caller += "->check_scrolling_allowed";

//trace_views(TAG_GUI, caller);

        String                                                                            reject_source = null;

        // FULLSCREEN MAGNIFY
        if     ( is_magnify_np_showing()        && (gesture_consumed_by_ACTION_UP==null)) reject_source = "is_magnify_np_showing";

        // FULLSCREEN TRANSIENT TABLE
        else if( is_fg_view_showing()           && (gesture_consumed_by_ACTION_UP==null)) reject_source = "is_fg_view_showing";

        // WEBVIEW (left most will do)
        else if( is_view_showing( fs_webView         ) && is_view_showing( wvContainer )) reject_source = "fs_webView_showing";

        // HANDLE
        else if( is_view_showing( controls_container )                                  ) reject_source = "controls_container_showing";
//      else if( is_view_showing( log_container      )                                  ) reject_source = "log_container_showing"; // [LOGGING] WILL FREEZE this_RTabsClient VIEW SELECTION IN [get_FF_W_C_HH_T_atXY]

        // [drag_band] (dragging)
        else if((  drag_band ==   dock_band          ) && is_dragBand_showing()         ) reject_source = "dragging dock (showing)";
        else if((  drag_band ==   hist_band          ) && is_dragBand_showing()         ) reject_source = "dragging hist (showing)";

        // [dock_band]
        else if(is_view_showing(  dock_band          )                                  ) reject_source = "dock_showing";

        // [hist_band] .. f(not pinned)
        else if(is_view_showing(  hist_band          ) && !show_band_hidden             ) reject_source = "hist_showing";

//*GUI*/Settings.MON(TAG_GUI, caller, "...return "+ ((reject_source!=null) ? "FALSE: reject_source=["+reject_source+"]" : "TRUE: no reject_source found"));
        return (reject_source == null);
    }
    //}}}
    // check_scaling_allowed {{{
    private boolean check_scaling_allowed(String caller)
    {
        caller += "->check_scaling_allowed";

        // TODO ..make this smarter...
        String reject_source = null;

        // IGNORE
        if     ( fs_webview_session_running                                 ) { reject_source = "fs_webview_session_running"; }
        else if( is_sandBox_showing                                         ) { reject_source = "is_sandBox_showing"        ; }

        // FULLSCREEN MAGNIFY
        else if( is_magnify_np_showing()                                    ) { reject_source = "is_magnify_np_showing"     ; }

        // FULLSCREEN TRANSIENT TABLE
        else if( is_fg_view_showing(                  )                     ) { reject_source = "is_fg_view_showing"        ; }

        // HANDLE
        else if( is_handle_showing( top_handle        )                     ) { reject_source = "top_handle_showing"        ; }
        else if( is_handle_showing( mid_handle        )                     ) { reject_source = "mid_handle_showing"        ; }
        else if( is_handle_showing( bot_handle        )                     ) { reject_source = "bot_handle_showing"        ; }
        else if( is_view_showing  ( seekers_container )                     ) { reject_source = "seekers_container_showing" ; }

        // [dock_band]
        else if( is_view_showing  ( dock_band         )                     ) { reject_source = "dock_showing"              ; }

        // [hist_band] .. f(not pinned)
        else if( is_view_showing  ( hist_band         ) && !show_band_hidden) { reject_source = "hist_showing"              ; }

//*GUI*/Settings.MON(TAG_GUI, caller, "...return "+ ((reject_source!=null) ? "FALSE: reject_source=["+reject_source+"]" : "TRUE: no reject_source found"));
//*SCALE*/Settings.MON(TAG_SCALE, caller, "...return "+ ((reject_source!=null) ? "FALSE: reject_source=["+reject_source+"]" : "TRUE: no reject_source found"));
        return (reject_source == null);
    }
    //}}}
    // check_onFling_enabled {{{
    private boolean check_onFling_enabled(String caller)
    {
        caller += "->check_onFling_enabled";
        String reject_source = null;

        // IGNORE FLING ON [CART] [LOG] [SANDBOX] [GRAPH]
        if     ( is_view_showing  ( cart_band  ) ) { reject_source = "cart_band_showing" ; }
        else if( is_handle_showing( top_handle ) ) { reject_source = "top_handle_showing"; }
        else if( is_handle_showing( mid_handle ) ) { reject_source = "mid_handle_showing"; }
        else if( is_handle_showing( bot_handle ) ) { reject_source = "bot_handle_showing"; }
        else if( is_sandBox_showing              ) { reject_source = "sandBox_showing"   ; }

        // IGNORE [WEBVIEW .. unless on fs_wtitle]
        else if(fs_webview_session_running && is_a_webView(gesture_down_SomeView_atXY))
        {
                reject_source = "FLING IN WEBVIEW";
        }
        // HIDE [MAGNIFIED BUTTON]
        else if(         is_magnify_np_showing()   ) { reject_source = "is_magnify_np_showing"; magnify_np_hide(caller); }
        // HIDE [FOREGROUND VIEW]
    //  else if(         is_fg_view_showing(      )) { reject_source = "is_fg_view_showing"  ; hide_fg_view(caller); }

//*GUI*/Settings.MON(TAG_GUI, caller, "...return "+ ((reject_source!=null) ? "FALSE: reject_source=["+reject_source+"]" : "TRUE: no reject_source found"));
        return (reject_source == null);
    }
    //}}}
    // get_DocHisCar_atX {{{
    private View get_DocHisCar_atX(int x)
    {
//*GUI*/String caller = "get_DocHisCar_atX("+x+")";//TAG_GUI
//*GUI*/Settings.MOC(TAG_GUI, caller);
//*GUI*/Settings.MOM(TAG_GUI, "...fs_webview_session_running=["+fs_webview_session_running+"]");
//*GUI*/Settings.MOM(TAG_GUI, "...........is_fg_view_showing=["+is_fg_view_showing()      +"]");
//*GUI*/Settings.MOM(TAG_GUI, "...is_view_showing(dock_band)=["+is_view_showing(dock_band)+"]");
//*GUI*/Settings.MOM(TAG_GUI, "...is_view_showing(hist_band)=["+is_view_showing(hist_band)+"]");
//*GUI*/Settings.MOM(TAG_GUI, "...is_view_showing(cart_band)=["+is_view_showing(cart_band)+"]");

        View view = null;

        if     ( fs_webview_session_running )          view =      null; // WEB    VIEW
        else if( is_fg_view_showing()       ) view =      null; // SPLASH VIEW

        else if( get_showing_view_atX( dock_band, x) ) view = dock_band; // Doc
        else if( get_showing_view_atX( hist_band, x) ) view = hist_band; // His
        else if( get_showing_view_atX( cart_band, x) ) view = cart_band; // Car

//*GUI*/Settings.MOM(TAG_GUI, caller+": ...return=["+get_view_name(view)+"]");
        return view;
    }
    //}}}
    // get_showing_view_atX {{{
    Rect  hr = new Rect();
    private boolean get_showing_view_atX(View view, int x)
    {
        if( !is_view_showing(view) ) return false;
        view.getHitRect(hr);         return hr.contains(x, hr.top);
    }
    //}}}
    // get_wtitle_or_container_atY {{{
    private View get_wtitle_or_container_atY(int x, int y)
    {
        if(fs_webView == null)                                         return        null; // NO WEBVIEW YET

        if( !fs_webview_session_running )                              return        null; // WEBVIEW NOT REQUESTED

        if (       !is_view_showing( fs_webView  )
                && !is_view_showing( fs_webView2 )
                && !is_view_showing( fs_webView3 ))                    return        null; // ALL WEBVIEW HIDDEN

        if(        (y >= (fs_webView.getY()                        ))                      // NOT ABOVE WEBVIEW
                && (y <= (fs_webView.getY()+ fs_webView.getHeight()))) return        null; // NOT BELOW WEBVIEW

        Rect  r = new Rect();
        fs_wtitle .getHitRect( r ); if( r.contains(x, y) )             return  fs_wtitle ; // WEBVIEW TITLE1
        fs_wtitle2.getHitRect( r ); if( r.contains(x, y) )             return  fs_wtitle2; // WEBVIEW TITLE2
        fs_wtitle3.getHitRect( r ); if( r.contains(x, y) )             return  fs_wtitle3; // WEBVIEW TITLE3

        if( is_view_showing( wvContainer ))                            return wvContainer; // WEBVIEW CONTAINER

        return null;
    }
    //}}}
    // get_FF_W_C_HH_T_atXY {{{
    //.1..[F]s_button..................(fs_button)
    //.2...[F]g_view...................(fg_view)
    //.3.....[W]eb.....................(Webview title tools)
    //.4.......[C]art..................(del see add end)
    //.5.........[H]ist................(back fore prof)
    //.6..........[H]andle.............(top mid bot)
    //.7............[T]ab..............(profile)
    private View get_FF_W_C_HH_T_atXY(int x, int y)
    {
        // 1 - [F]s_button .. (temporary magnified view) {{{
        String caller = "get_FF_W_C_HH_T_atXY";

//*GUI*/Settings.MON(TAG_GUI, caller, "1 - [F]s_button .. (temporary magnified view)");
        if( is_view_showing( fs_button )) return fs_button;

        // }}}
        // 2 - fg_view .. (temporary foreground view) {{{
//*GUI*/Settings.MON(TAG_GUI, caller, "2 - fg_view .. (temporary foreground view)");
        if( is_fg_view_showing()        ) return get_fg_view();

        // }}}
        // 3 - WVTOOLS [wv] [sb] [tool] [marker] {{{
//*GUI*/Settings.MON(TAG_GUI, caller, "3 - WVTOOLS [wv] [sb] [tool] [marker]");
        Rect r = new Rect();
        if(fs_webview_session_running)
        {
            View view = wvTools.get_view_at_XY(x, y);
            if(  view != null) return view;

            // (WebViews)
            if( is_view_showing( fs_webView  )) { fs_webView .getHitRect( r ); if( r.contains(x, y) ) return fs_webView ; }
            if( is_view_showing( fs_webView2 )) { fs_webView2.getHitRect( r ); if( r.contains(x, y) ) return fs_webView2; }
            if( is_view_showing( fs_webView3 )) { fs_webView3.getHitRect( r ); if( r.contains(x, y) ) return fs_webView3; }

            // (Web tools)
            if( fs_webView_isGrabbed )
            {
                fs_wtitle   .getHitRect( r ); if( r.contains(x, y) ) return fs_wtitle   ;
                fs_wtitle2  .getHitRect( r ); if( r.contains(x, y) ) return fs_wtitle2  ;
                fs_wtitle3  .getHitRect( r ); if( r.contains(x, y) ) return fs_wtitle3  ;
                fs_wswapL   .getHitRect( r ); if( r.contains(x, y) ) return fs_wswapL   ;
                fs_wswapR   .getHitRect( r ); if( r.contains(x, y) ) return fs_wswapR   ;
                fs_browse   .getHitRect( r ); if( r.contains(x, y) ) return fs_browse   ;
                fs_bookmark .getHitRect( r ); if( r.contains(x, y) ) return fs_bookmark ;
                fs_goBack   .getHitRect( r ); if( r.contains(x, y) ) return fs_goBack   ;
                fs_goForward.getHitRect( r ); if( r.contains(x, y) ) return fs_goForward;
            }

            // (WebViews background)
            if( is_view_showing( wvContainer ))                      return wvContainer ; // WEBVIEW background
        }
        // }}}
        // 4 - [C]art .. 5 - [H]ist .. 6 - [H]andle {{{
//*GUI*/Settings.MON(TAG_GUI, caller, "4 - [C]art [H]ist [H]andle");

        // (cart tools)
//*GUI*/Settings.MON(TAG_GUI, caller, "4 - [C]art");
        if( is_view_showing( cart_band ) ) {
            x -= cart_band.getX();
            y -= cart_band.getY();
            cart_band.see_nb .getHitRect( r ); if( r.contains(x, y) ) return cart_band.see_nb;
            cart_band.end_nb .getHitRect( r ); if( r.contains(x, y) ) return cart_band.end_nb;
        }

        // (hist tools)
//*GUI*/Settings.MON(TAG_GUI, caller, "5 - [H]ist");
        if( is_view_showing( hist_band ) ) {
            x -= hist_band.getX();
            y -= hist_band.getY();
            hist_band.back_nb.getHitRect( r ); if( r.contains(x, y) ) return hist_band.back_nb;
            hist_band.frwd_nb.getHitRect( r ); if( r.contains(x, y) ) return hist_band.frwd_nb;
            hist_band.prof_nb.getHitRect( r ); if( r.contains(x, y) ) return hist_band.prof_nb;
        }

        // (handles)
//*GUI*/Settings.MON(TAG_GUI, caller, "6 - [H]andle");
        if( is_handle_showing( top_handle )) {                                                    return top_handle; }
        if( is_handle_showing( bot_handle )) {                                                    return bot_handle; }
        if( is_handle_showing( mid_handle )) { mid_handle.getHitRect( r );                        return mid_handle; }
      //if( is_handle_showing( mid_handle )) { mid_handle.getHitRect( r ); if(!r.contains(x, y) ) return mid_handle; } // i.e. touched outside of controls area

        //}}}
        // 7 - [T]ab {{{
//*GUI*/Settings.MON(TAG_GUI, caller, "7 - [T]ab");
        if((this_RTabsClient != null) && is_tabs_scrolling())
        {
            int      np_x = x + hsv.getScrollX() + vsv.getScrollX() + tabs_container.getScrollX();
            int      np_y = y + hsv.getScrollY() + vsv.getScrollY() + tabs_container.getScrollY();
            NotePane np   = this_RTabsClient.get_TABS_np_at_xy_closest(np_x, np_y, caller);
            if(        (np        != null            )
                    && (np.button != null            )
                    && (np.button instanceof NpButton)
              )
                return np.button;
        }
        else {
//*GUI*/Settings.MON(TAG_GUI, caller, "...(this_RTabsClient != null)=["+(this_RTabsClient != null)+"]");
//*GUI*/Settings.MON(TAG_GUI, caller, "..........is_tabs_scrolling()=["+is_tabs_scrolling()+"]");
        }
        //}}}
//*GUI*/Settings.MON(TAG_GUI, caller, "...return null");
        return null;
    }
    //}}}
    //}}}
    //* FREEZE {{{ */
    // sync_tabs_scrolling {{{
    private void sync_tabs_scrolling(String caller)
    {
        caller += "->sync_tabs_scrolling";
//*GUI*/Settings.MOC(TAG_GUI, caller);
        if(hsv == null) return;

        if( !is_tabs_scrolling() ) { if(  check_scrolling_allowed(caller) ) set_tabs_scrolling( true, caller); } // [FREEZED]->[UNFREEZED]
        else                       { if( !check_scrolling_allowed(caller) ) set_tabs_scrolling(false, caller); } // [FREEZED]<-[UNFREEZED]
    }
    // }}}
    // set_tabs_scrolling {{{
    private    void set_tabs_scrolling(boolean state, String caller)
    {
        caller += "->set_tabs_scrolling("+state+")";
        if(state)
        {
//*GUI*/Settings.MON(TAG_GUI, caller, "scroll.unfreeze");
            hsv.unfreeze();
            vsv.unfreeze();
            do_sync_GUI_colors(caller);
        }
        else {
//*GUI*/Settings.MON(TAG_GUI, caller, "scroll.freeze");
            hsv.freeze();
            vsv.freeze();
            do_sync_GUI_colors(caller);
        }
    }
//}}}
    // is_tabs_scrolling {{{
    private boolean is_tabs_scrolling()
    {
        return !hsv.isfreezed();
    }
//}}}
    // }}}
    //* COLORS {{{ */
    // {{{
    // .. (i.e. all if's resulting in a color selection)
    // ...like [is scrolling enabled]
    // .....or [is rescaling in effect]
    // .....or [is zoomed out full]
    // .....or [has gui-idle-notification been fired]
    // .....or [has gui-idle-notification been reset]

    // }}}
    // sync_GUI_colors {{{
    public    void sync_GUI_colors(String caller)
    {
        caller += "->sync_GUI_colors";
//*GUI*/Settings.MOC(TAG_GUI, caller);

        handler.re_postDelayed(hr_sync_GUI_colors, SYNCUI_COLORS_DELAY);
    }
    //}}}
    // hr_sync_GUI_colors {{{
    private final Runnable hr_sync_GUI_colors = new Runnable() {
        @Override public void run()
        {
            do_sync_GUI_colors("hr_sync_GUI_colors");
        }
    };

    // }}}
    // do_sync_GUI_colors {{{
    private void do_sync_GUI_colors(String caller)
    {
        // CALLERS {{{
        caller += "->do_sync_GUI_colors";
//*GUI*/Settings.MOC(TAG_GUI, caller);

        if(tabs_container == null) return;

        //}}}
      //// bg padding [FRAMED] .. f("LOGGING") {{{
      //if(Settings.LOGGING) {
      //    bg_view.setPadding(50, 50, 50, 50);
      //}
      //else {
      //    bg_view.setPadding( 0,  0,  0,  0);
      //}
      //// }}}
        // sync_tabs_scrolling {{{
        sync_tabs_scrolling(caller);

        //}}}
        // STATES - [tabs] [dock] [hist] [show] [seekers] {{{
        boolean tabs_scrolling             = is_tabs_scrolling ();
        boolean seekers_container_showing  = is_view_showing( seekers_container  );
        boolean controls_container_showing = is_view_showing( controls_container );
        boolean dck_handle_showing         = is_view_showing( dck_handle         );
        boolean  show_band_showing         = is_view_showing(  show_band         );
        boolean   hist_band_showing        = is_view_showing(   hist_band        );
        boolean   dock_band_showing        = is_view_showing(   dock_band        );
        boolean   cart_band_showing        = is_view_showing(   cart_band        );

//*GUI*/Settings.MOM(TAG_GUI, ".tabs_scrolling..............="+ tabs_scrolling             );
//*GUI*/Settings.MOM(TAG_GUI, ".show_band_hidden............="+ show_band_hidden           );
//*GUI*/Settings.MOM(TAG_GUI, ".controls_container_showing..="+ controls_container_showing );
//*GUI*/Settings.MOM(TAG_GUI, "..seekers_container_showing..="+ seekers_container_showing  );
//*GUI*/Settings.MOM(TAG_GUI, ".........dck_handle_showing..="+ dck_handle_showing         );
//*GUI*/Settings.MOM(TAG_GUI, "..........show_band_showing..="+ show_band_showing          );
//*GUI*/Settings.MOM(TAG_GUI, "...........dock_band_showing.="+ dock_band_showing          );
//*GUI*/Settings.MOM(TAG_GUI, "...........hist_band_showing.="+ hist_band_showing          );
//*GUI*/Settings.MOM(TAG_GUI, "...........cart_band_showing.="+ cart_band_showing          );
//*GUI*/Settings.MOM(TAG_GUI, "...................GUI_STATE=["+get_GUI_STATE(GUI_STATE)+"]");
//*GUI*/Settings.MOM(TAG_GUI, "..................cur_handle=["+get_handle_name( Handle.Get_cur_handle() )+"]");

        //}}}
        // [show_tabs_container_freezed] [show_band_show] {{{
        if( !tabs_scrolling    && !seekers_container_showing ) show_tabs_container_freezed  ( caller );
        if( !hist_band_showing && !dock_band_showing         ) show_tabs_container_unfreezed( caller );
        if( !show_band_showing && !show_band_hidden          ) show_band_show               ( caller );

        if(  dck_handle_showing
                && (Handle.Get_cur_handle() == dck_handle)
                && !controls_container_showing
                && !dock_band_showing
                && !hist_band_showing
                && !cart_band_showing
          )
            handle_hide(caller);

        //}}}
    }
    //}}}
    //}}}
    //* SCROLLVIEW {{{ */
    //{{{
    private boolean rescaling             = false;
    private boolean rescaled_since_ondown = false;
    private float   rescaling_from_scale  = 1F;

    private float   xsv_focusX;
    private float   xsv_focusY;
    private float   xsv_offsetX;
    private float   xsv_offsetY;
    private float   xsv_pivotX;
    private float   xsv_pivotY;
    private float   xsv_scrollX;
    private float   xsv_scrollY;

//  private float   translX;
//  private float   translY;
     //}}}
    // pick_a_movable_tab_at_xy {{{
    private NotePane tab_np_at_XY = null;

    public boolean pick_a_movable_tab_at_xy(int x, int y)
    {
        String caller = "pick_a_movable_tab_at_xy(x,y)";
        tab_np_at_XY = null;

        if(get_cart_state(caller) != Settings.CART_STATE_DEFAULT)
            tab_np_at_XY = this_RTabsClient.get_TABS_np_at_xy_closest(x, y, caller);

//*GUI*/Settings.MON(TAG_GUI, caller, "...tab_np_at_XY=["+tab_np_at_XY+"]");
        return (tab_np_at_XY != null);
    }
    //}}}
    // handle_a_movable_tab_event .. (called by VScrollView while moving a NotePane) {{{
    public boolean handle_a_movable_tab_event(MotionEvent event)
    {
        if(tab_np_at_XY == null) return false;
//*GUI*/Settings.MOC(TAG_GUI, "handle_a_movable_tab_event("+Settings.Get_action_name(event)+"): tab_np_at_XY=["+tab_np_at_XY+"]");

        // finger-offset
      //mClamp.set_next_catch_offset_fx_fy(  0  ,  0 ); // (touch point drag) .. [default]
      //mClamp.set_next_catch_offset_fx_fy(0.5f, 0.5f); // (center - middle )
      //mClamp.set_next_catch_offset_fx_fy(1.0f, 1.0f); // ( right - bottom )

        mClamp.set_gravity_fx( Clamp.LIGHT_GRAVITY_FX );

        xsv_scrollX += hsv.getScrollX() + vsv.getScrollX() + tabs_container.getScrollX();
        xsv_scrollY += hsv.getScrollY() + vsv.getScrollY() + tabs_container.getScrollY();
        return mClamp.onTouchEvent(tab_np_at_XY, event, xsv_scrollX, xsv_scrollY); // on_grid
    }
    //}}}
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ GUI @ }}}
    /** PROPERTY */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ PROPERTY @ {{{
    //{{{
    // toggle_PROPERTY {{{
    private boolean toggle_PROPERTY(String cmdLine)
    {
        String caller = "toggle_PROPERTY("+cmdLine+")";
//*SETTINGS*/Settings.MOC(TAG_SETTINGS, caller);

        CmdParser.parse( cmdLine );

        String cmd = (CmdParser.args.length > 0) ? CmdParser.args[0].intern() : "";
        String key = (CmdParser.args.length > 1) ? CmdParser.args[1].intern() : "";
        String val = (CmdParser.args.length > 2) ? CmdParser.args[2]          : "";

        if(  TextUtils.isEmpty(key)          ) { Settings.MON(TAG_SETTINGS, caller, "* missing property key"    ); return false; }
        if(  TextUtils.isEmpty(val)          ) { Settings.MON(TAG_SETTINGS, caller, "* missing property value"  ); return false; }
        if( !TextUtils.equals(cmd,"PROPERTY")) { Settings.MON(TAG_SETTINGS, caller, "* NOT A PROPERTY ["+cmd+"]"); return false; }

        // address one of the relevant class or instance parameters
        switch( key ) {
            case PROPERTY_BAND_HIDE_WIDTH: set_PROPERTY_BAND_HIDE_WIDTH(val, cmdLine);                  return true;
            case PROPERTY_MARK_SCALE_GROW: set_PROPERTY_MARK_SCALE_GROW(val, cmdLine);                  return true;
            case PROPERTY_MARK_EDIT_DELAY: set_PROPERTY_MARK_EDIT_DELAY(val, cmdLine);                  return true;
            case PROPERTY_BG_VIEW_DEFAULT: set_PROPERTY_BG_VIEW_DEFAULT(val, cmdLine);                  return true;
            case PROPERTY_SOUND_CLICK    : set_PROPERTY_SOUND_CLICK    (val, cmdLine);                  return true;
            case PROPERTY_SOUND_DING     : set_PROPERTY_SOUND_DING     (val, cmdLine);                  return true;
            default                      : Settings.MOM(TAG_SETTINGS, caller+": PROPERTY NOT HANDLED"); return false;
        }
    }
    //}}}

    // set_PROPERTY_BAND_HIDE_WIDTH {{{
    private static final String PROPERTY_BAND_HIDE_WIDTH = "BAND_HIDE_WIDTH";
    public  void set_PROPERTY_BAND_HIDE_WIDTH(String val, String caller)
    {
        caller += "->set_PROPERTY_BAND_HIDE_WIDTH("+val+")";
//*SETTINGS*/Settings.MOC(TAG_SETTINGS, caller);
        // SANITY CHECK {{{
        try { val = ""+Integer.parseInt( val ); } catch(Exception ignored) {}

        int width = Integer.parseInt( val );
        if(width <= 0)
        {
Settings.MON(TAG_SETTINGS, caller, "...width=["+width+"] NOT SUPPORTED");
            return;
        }
        //}}}
        // UPDATE PROPERTY {{{
        Settings.BAND_HIDE_WIDTH = width;

        // }}}
        // show_band [width] {{{
        FrameLayout.LayoutParams flp;
        flp             = (FrameLayout.LayoutParams)show_band.getLayoutParams();
        flp.width       = Settings.BAND_HIDE_WIDTH;
    //  flp.rightMargin = Handle.Get_DOCK_STANDBY_WIDTH() - flp.width;

        show_band.setLayoutParams(flp);
        //show_band.requestLayout();
        //}}}
        // UPDATE DEPENDENCIES {{{
/*
        // dock_band {{{
        // dock_band.container_sv [leftMargin] {{{
        rlp             = (RelativeLayout.LayoutParams)dock_band.container_sv.getLayoutParams();
        rlp.leftMargin  = Settings.BAND_HIDE_WIDTH;

        dock_band.container_sv.setLayoutParams(rlp);
        //}}}
        dock_band.requestLayout();
        //}}}
        // dck_handle [scrollTo] {{{
        if( show_band_hidden ) dck_handle.scrollTo(Settings.BAND_HIDE_WIDTH, 0);
        else                  dck_handle.scrollTo(0                       , 0);

        dck_handle.requestLayout();
        //}}}
        // hist_band [width] {{{
        rlp             = (RelativeLayout.LayoutParams)hist_band.getLayoutParams();
        rlp.width       = Handle.Get_DOCK_STANDBY_WIDTH() - Settings.BAND_HIDE_WIDTH;
        hist_band.setLayoutParams(rlp);

        // hist_band.back_nb [width] {{{
        rlp             = (RelativeLayout.LayoutParams)hist_band.back_nb.getLayoutParams();
        rlp.width       = Handle.Get_DOCK_STANDBY_WIDTH() - Settings.BAND_HIDE_WIDTH;

        hist_band.back_nb.setLayoutParams( rlp );
        //}}}
        // hist_band.frwd_nb [width] {{{
        rlp             = (RelativeLayout.LayoutParams)hist_band.frwd_nb.getLayoutParams();
        rlp.width       = Handle.Get_DOCK_STANDBY_WIDTH() - Settings.BAND_HIDE_WIDTH;

        hist_band.frwd_nb.setLayoutParams( rlp );
        //}}}
        hist_band.requestLayout();
        //}}}
        // cart_band {{{
        ViewGroup.LayoutParams [width] vlp;
        vlp             = cart_band.getLayoutParams();
        vlp.width       = Handle.Get_DOCK_STANDBY_WIDTH() - Settings.BAND_HIDE_WIDTH;

        cart_band.setLayoutParams( vlp );
        // cart_band.end_nb [width] {{{
        vlp             = cart_band.end_nb.getLayoutParams();
        vlp.width       = Handle.Get_DOCK_STANDBY_WIDTH() - Settings.BAND_HIDE_WIDTH;

        cart_band.end_nb.setLayoutParams( vlp );
        //}}}
        cart_band.requestLayout();
        //}}}
        // }}}
*/
        // UPDATE GUI {{{
        schedule_sync_GUI(caller);

        //}}}
    }
    //}}}
    // set_PROPERTY_MARK_SCALE_GROW {{{
    public  static final String PROPERTY_MARK_SCALE_GROW = "MARK_SCALE_GROW";
    public  void set_PROPERTY_MARK_SCALE_GROW(String val, String caller)
    {
        caller += "->set_PROPERTY_MARK_SCALE_GROW("+val+")";
//*SETTINGS*/Settings.MOC(TAG_SETTINGS, caller);
        // SANITY CHECK {{{
        float scale = Settings.MARK_SCALE_GROW;
        try { scale = Float.parseFloat( val ); } catch(Exception ignored) {}

        if(scale <= 0)
        {
Settings.MON(TAG_SETTINGS, caller, "...scale=["+scale+"] NOT SUPPORTED");
            return;
        }
        //}}}
        // UPDATE PROPERTY {{{
        Settings.MARK_SCALE_GROW = scale;

        // }}}
    }
    //}}}
    // set_PROPERTY_MARK_EDIT_DELAY {{{
    public  static final String PROPERTY_MARK_EDIT_DELAY = "MARK_EDIT_DELAY";
    public  void set_PROPERTY_MARK_EDIT_DELAY(String val, String caller)
    {
        caller += "->set_PROPERTY_MARK_EDIT_DELAY("+val+")";
//*SETTINGS*/Settings.MOC(TAG_SETTINGS, caller);
        // SANITY CHECK {{{
        int delay = Settings.MARK_EDIT_DELAY;
        try { delay = Integer.parseInt( val ); } catch(Exception ignored) {}

        if(delay <= 0)
        {
Settings.MON(TAG_SETTINGS, caller, "...delay=["+delay+"] NOT SUPPORTED");
            return;
        }
        //}}}
        // UPDATE PROPERTY {{{
        Settings.MARK_EDIT_DELAY = delay;

        // }}}
    }
    //}}}
    // set_PROPERTY_BG_VIEW_DEFAULT {{{
    private static final String PROPERTY_BG_VIEW_DEFAULT = "BG_VIEW_DEFAULT";

    public  void set_PROPERTY_BG_VIEW_DEFAULT(String val, String caller)
    {
        caller += "->set_PROPERTY_BG_VIEW_DEFAULT("+val+")";
//*SETTINGS*/Settings.MOC(TAG_SETTINGS, caller);
        // SANITY CHECK {{{

        String color_hex = Settings.Get_color_hex_from_text( val );
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "color_hex=["+color_hex+"]");
        if( TextUtils.isEmpty( color_hex ) ) {
Settings.MON(TAG_SETTINGS, caller, "...color_hex=["+val+"] NOT SUPPORTED");

            return;
        }
        //}}}
        // PARSE PROPERTY {{{
        int bg_color = Settings.BG_VIEW_DEFAULT;
        try {
            bg_color = Color.parseColor( color_hex );

        }
        catch(Exception ignored) {
Settings.MON(TAG_SETTINGS, caller, "...color_hex=["+val+"] NOT SUPPORTED");
            return;
        }
        //}}}
        // UPDATE PROPERTY {{{
        Settings.BG_VIEW_DEFAULT = bg_color;

        // }}}
        // UPDATE GUI {{{
        //schedule_sync_GUI(caller);
        //do_sync_GUI_colors(caller);

        //}}}
    }
    //}}}
    // set_PROPERTY_SOUND_CLICK {{{
    private static final String PROPERTY_SOUND_CLICK = "SOUND_CLICK";
    public  void set_PROPERTY_SOUND_CLICK(String val, String caller)
    {
        caller += "->set_PROPERTY_SOUND_CLICK("+val+")";
//*SETTINGS*/Settings.MOC(TAG_SETTINGS, caller);

        Settings.SOUND_CLICK = val;

        play_sound_click(caller);
        if( is_fg_view_showing(      ) )
            show_SOUNDS_TABLE(caller);
    }
    private final Runnable hr_play_sound_click = new Runnable() {
        @Override public void run() { SoundPoolManager.play(Settings.SOUND_CLICK); }
    };
    //}}}
    // set_PROPERTY_SOUND_DING {{{
    private static final String PROPERTY_SOUND_DING = "SOUND_DING";
    public  void set_PROPERTY_SOUND_DING(String val, String caller)
    {
        caller += "->set_PROPERTY_SOUND_DING("+val+")";
//*SETTINGS*/Settings.MOC(TAG_SETTINGS, caller);

        Settings.SOUND_DING = val;

        play_sound_ding(caller);
        if( is_fg_view_showing(      ) )
            show_SOUNDS_TABLE(caller);
    }
    private final Runnable hr_play_sound_ding = new Runnable() {
        @Override public void run() { SoundPoolManager.play(Settings.SOUND_DING); }
    };
    //}}}

    // }}}
    // is_ACTIVITY_BUILTIN {{{
    public static boolean is_ACTIVITY_BUILTIN(String np_tag)
    {
        boolean diag = false;

        if( np_tag.startsWith("SANDBOX"     ) ) diag = true;
        if( np_tag.startsWith("PROPERTY"    ) ) diag = true;

        if(D) MLog.log("is_ACTIVITY_BUILTIN("+ np_tag +") ...return "+ diag);
        return diag;
    }
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** LOG */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ LOG @ {{{
    // APP INFO {{{
    // get_APK_VERSION {{{
    public static String get_APK_VERSION()
    {
        long time = get_APK_TIME();
      //if(time > 0) return     SimpleDateFormat.getInstance()  .format( new Date(time) );
        if(time > 0) return new SimpleDateFormat("yyMMdd HH:mm").format( new Date(time) );
      //if(time > 0) return DateFormat.getDateTimeInstance()    .format(          time  );
        else         return "UNKNOWN VERSION";
    }
    //}}}
    // get_apk_AGE {{{
    private static String get_APK_AGE()
    {
        long    time = get_APK_TIME();
        if(time > 0) return Settings.Get_time_elapsed( time );
        else         return "UNKNOWN VERSION";
    }
    //}}}
    // get_apk_DATE {{{
    private static String get_APK_DATE()
    {
        long    time = get_APK_TIME();
      //if(time > 0) return new SimpleDateFormat("yyyy-MM-dd"        ).format( new Date(time) );
        if(time > 0) return new SimpleDateFormat("yyyy-MM-dd HH:mm"  ).format( new Date(time) );
      //if(time > 0) return           DateFormat.getDateTimeInstance().format( new Date(time) );
        else         return "UNKNOWN DATE";
    }
    //}}}
    // get_APK_TIME {{{
    private static long get_APK_TIME()
    {
        return get_APK_TIME_from_BuildConfig_TIMESTAMP    ();
    //  return get_APK_TIME_from_ApplicationInfo_sourceDir(); // *** does not work since ANDROID STUDIO 2.2
    }
    //}}}
    // get_APK_TIME_from_ApplicationInfo_sourceDir {{{
    private static long get_APK_TIME_from_ApplicationInfo_sourceDir()
    {
        String caller = "get_APK_TIME_from_ApplicationInfo_sourceDir";
        long    time    = 0;

        // (as of 160921 - Android studio upgraded to 2.2)
        // *** app-API22-debug.apk files have no Modified date anymor
        try{
            ApplicationInfo ai = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), 0);
//*SETTINGS*/Settings.MON(TAG_SETTINGS, caller, "ai.sourceDir=["+ai.sourceDir+"]");

            ZipFile     zf = new ZipFile( ai.sourceDir  ); // Full path to the base APK for this application
            ZipEntry    ze = zf.getEntry( "classes.dex" );
            time           = ze.getTime();
            zf.close();
        }
        catch(Exception ex) {
            MLog.log("*** "+caller+":\n"+ ex.getMessage());
        }
//*SETTINGS*/Settings.MON(TAG_SETTINGS, caller, "time=["+time+"]");
        return time;
    }
    //}}}
    // get_APK_TIME_from_BuildConfig_TIMESTAMP {{{
    private static long get_APK_TIME_from_BuildConfig_TIMESTAMP()
    {
        String caller = "get_APK_TIME_from_BuildConfig_TIMESTAMP";
//:!start explorer "http://stackoverflow.com/questions/7607165/how-to-write-build-time-stamp-into-apk"
        long time   = 0;
        try{
            time    = BuildConfig.TIMESTAMP;
        }
        catch(Exception ex) {
            MLog.log("*** "+caller+":\n"+ ex.getMessage());
        }
//*SETTINGS*/Settings.MON(TAG_SETTINGS, caller, "time=["+time+"]");
        return time;
    }
    //}}}
    // get_next_server_hook_delay {{{
    private int get_next_server_hook_delay()
    {
        // NO DELAY FOR A FIRST REQUEST WITH AN ESTABLISHED CONNECTION
        return this_RTabsClient.has_max_connection_failed()
            ?  DATA_LOOPER_IDLE_PERIOD
            :  DATA_LOOPER_BASE_PERIOD
            ;
    }
    // }}}
    // log_app_version {{{
    public String log_app_version()
    {
        ComponentName       callingActivity = activity.getCallingActivity();
        String   callingActivityPackageName =   (callingActivity != null) ? callingActivity.getPackageName() : "";

        Log_init("VERSION ["+ get_APK_VERSION() +"] ("+ get_APK_AGE() +" old)");

        Log_append(  "is_Build_VERSION_SDK_HONEYCOMB.....: "+ Settings.is_Build_VERSION_SDK_HONEYCOMB     );
        Log_append(  "is_Build_VERSION_SDK_HONEYCOMB_MR2.: "+ Settings.is_Build_VERSION_SDK_HONEYCOMB_MR2 );
        Log_append(  "is_Build_VERSION_SDK_KITKAT........: "+ Settings.is_Build_VERSION_SDK_KITKAT        );
        Log_append(  "is_Build_VERSION_SDK_LOLLIPOP......: "+ Settings.is_Build_VERSION_SDK_LOLLIPOP      );
        Log_append(  "is_Build_VERSION_SDK_M.............: "+ Settings.is_Build_VERSION_SDK_M             );
        Log_append(  "is_Build_VERSION_SDK_N.............: "+ Settings.is_Build_VERSION_SDK_N             );
        Log_append(  "is_Build_VERSION_SDK_N_MR1.........: "+ Settings.is_Build_VERSION_SDK_N_MR1         );
        Log_append(  "is_Build_VERSION_SDK_O.............: "+ Settings.is_Build_VERSION_SDK_O             );
        Log_append(  "is_Build_VERSION_SDK_O_MR1.........: "+ Settings.is_Build_VERSION_SDK_O_MR1         );
        Log_append(  "is_Build_VERSION_SDK_P.............: "+ Settings.is_Build_VERSION_SDK_P             );
        Log_append(  "is_Build_VERSION_SDK_Q.............: "+ Settings.is_Build_VERSION_SDK_Q             );

        Log_append(  "..........get_APP_NAME=["+ Settings.get_APP_NAME()           +"]");
        Log_append(  "........getPackageName=["+ activity.getPackageName()         +"]");
    //  Log_append(  "....getBasePackageName=["+ activity.getBasePackageName()     +"]");
    //  Log_append(  "......getOpPackageName=["+ activity.getOpPackageName()       +"]");
        Log_append(  "getPackageResourcePath=["+ activity.getPackageResourcePath() +"]");
        Log_append(  "....getPackageCodePath=["+ activity.getPackageCodePath()     +"]");
        Log_append(  ".....getCallingPackage=["+ activity.getCallingPackage()      +"]");
        Log_append(  "...Calling-Act.Package=["+ callingActivityPackageName        +"]");
        Log_append(  ".....getLocalClassName=["+ activity.getLocalClassName()      +"]");
        Log_append(  "....BuildConfig.FLAVOR=["+ BuildConfig.FLAVOR                +"]");

        return Log_toString();

        //:!start explorer "http://blog.brainattica.com/how-to-work-with-flavours-on-android"
    }
    //}}}
    // log_life_cycle {{{
//  private static       String lc_sym = Settings.SYMBOL_LOLLIPOP;
    private static final String LIFE_CYCLE_INIT = "<app_init>";
    private static final String LIFE_CYCLE_GC   = "<GC>";

    private static String life_cycle_call_chain = LIFE_CYCLE_INIT;
    private static    int life_cycle_step       = 0;

    public String log_life_cycle_call_chain() {
            return life_cycle_call_chain;
    }

    private static void log_life_cycle(String life_cycle_caller)
    {
        // build up callers chain
        if(life_cycle_caller == "onDestroy")
            life_cycle_call_chain = (++life_cycle_step)+"_"+ life_cycle_caller;
        else
            life_cycle_call_chain = (++life_cycle_step)+"_"+ life_cycle_caller + Settings.SYMBOL_LEFT_ARROW + life_cycle_call_chain;

        if(        life_cycle_caller.equals("onStop")
                || life_cycle_caller.equals( LIFE_CYCLE_GC )
          ) {
            life_cycle_call_chain = "\n   "+Settings.SYMBOL_NW_ARROW + life_cycle_call_chain;
        //  life_cycle_step       = 0;
          }

        MLog.log("|| "+life_cycle_call_chain);

/* // {{{
        MLog.log(lc_sym + lc_sym + lc_sym+"\n"
                +lc_sym + lc_sym + lc_sym+lc_sym + life_cycle_call_chain +"\n"
                +lc_sym + lc_sym + lc_sym
           );
*/ // }}}
    }

    //}}}
    // }}}
    // APP EVENTS {{{
    // cmd_status_OnClickListener {{{
    private final View.OnClickListener cmd_status_OnClickListener = new View.OnClickListener() {
        @Override public void onClick(View view) {
            MLog.log_STATUS("cmd_status_OnClickListener");
        }
    };
    //}}}
    // cmd_INVENTORY_OnClickListener {{{
    private final View.OnClickListener cmd_INVENTORY_OnClickListener = new View.OnClickListener() {
        @Override public void onClick(View view) {
            MLog.log_INVENTORY("cmd_INVENTORY_OnClickListener");
        }
    };
    //}}}
    // cmd_PROFILE_OnClickListener {{{
    private final View.OnClickListener cmd_PROFILE_OnClickListener = new View.OnClickListener() {
        @Override public void onClick(View view) {
            if( !TextUtils.equals(Settings.PROFILE, Settings.Working_profile) )
                select_and_load_working_profile("cmd_PROFILE_OnClickListener");
            MLog.log_PROFILE("cmd_PROFILE_OnClickListener");
        }
    };
    //}}}
    // checkBox_freeze_OnCheckedChangeListener {{{
    private final CompoundButton.OnCheckedChangeListener checkBox_freeze_OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            if(Settings.FREEZED != isChecked)
                set_APP_freezed_state(isChecked, "checkBox_freeze_OnCheckedChangeListener");
        }
    };
    //}}}
    // checkBox_log_OnCheckedChangeListener{{{
    private final CompoundButton.OnCheckedChangeListener checkBox_log_OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked != Settings.LOGGING) {
                set_LOGGING(isChecked, true, "checkBox_log_OnCheckedChangeListener");
                int next_GUI_STATE =        GUI_STATE; // current
                if( Settings.LOGGING ) next_GUI_STATE = GUI_STATE_INFO;
                else                   next_GUI_STATE = GUI_STATE_TABS;
                if(next_GUI_STATE !=        GUI_STATE)
                    apply_GUI_STATE(   next_GUI_STATE, FROM_LOG, "checkBox_log_OnCheckedChangeListener");
            }
        }
    };
    //}}}
    // cmd_clear_log_OnClickListener {{{
    private final View.OnClickListener cmd_clear_log_OnClickListener = new View.OnClickListener() {
        @Override public void onClick(View view) {
            log_text.setText("");
            update_log_container_scrolling();
        }
    };
    //}}}
    // log_container_OnScrollViewListener {{{
    private final LScrollView.OnScrollViewListener log_container_OnScrollViewListener = new LScrollView.OnScrollViewListener() {
        public void onScrollChanged(LScrollView sv, int l, int t, int oldl, int oldt) {
            update_log_container_scrolling();
        }
    };
    //}}}
    // LOG STATE {{{
    // set_LOG_CAT {{{
    private static final String LOG_CAT_MSG
        = "*** LOG_CAT @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n"
        + "*** LOG_CAT @ LOGGING IS REDIRECTED TO Android Monitor @\n"
        + "*** LOG_CAT @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n";

    private void set_LOG_CAT(boolean state)
    {
        Settings.LOG_CAT = state;

        String caller = "set_LOG_CAT("+ Settings.LOG_CAT +")";
Settings.MOC(TAG_GUI, caller);

        if(log_text != null)
        {
            if( Settings.LOG_CAT ) {
                log_text_setText( LOG_CAT_MSG );
                log_container_set_freezed_color();
            }
            else {
                log_text_clear();
            }
        }
        MLog.log_text_scrolled_by_user = false;
    }

    public void log_container_set_freezed_color()
    {
        if(log_container != null)
            log_container.setBackgroundColor( MLog.Log_BackColor_FREEZED );
    }

    //}}}
    // set_LOGGING {{{
    public void set_LOGGING(boolean state, boolean clear_log_text, String caller)
    {
        caller += "->set_LOGGING("+state+", clear_log_text="+clear_log_text+")";
        Settings.MOC(TAG_GUI, caller);

        // MODULES LOG SYNC
        Settings.set_LOGGING( state );

        Settings.apply_LOG_FILTERS();

        // GUI SYNC
        if(checkBox_log                != null) checkBox_log.setChecked( state );
        if(cmd_clear_log               != null) cmd_clear_log.setVisibility((Settings.LOGGING || (log_text.getText().length() > 0)) ? View.VISIBLE : View.GONE);
        if(!Settings.LOGGING && clear_log_text) log_text_clear();

        do_sync_SUI_visibility(caller);

        sync_np( caller );

        //if(this_RTabsClient != null) this_RTabsClient.warn_to_dash("LOGGING", Settings.LOGGING ? "ON" : "OFF");
    }
    //}}}
    //}}}
    // toggle_ERRLOGGED {{{
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void toggle_ERRLOGGED(String caller)
    {
        caller += "->toggle_ERRLOGGED";
Settings.MOC(TAG_GUI, caller);

        if(D) MLog.log( log_app_version() );

        // REDIRECT STANDARD ERROR AND STANDARD OUT
        if( !Settings.is_err_redirection_required() ) {
            set_np_label_fg("ERRLOGGED", Settings.get_APP_NAME() +" ("+ get_APK_AGE() +")\nERRLOGGED NOT REQUIRED", Color.LTGRAY);
            return;
        }

        // OUT AND ERR FOLDER
        String dir_path = Settings.Get_Profiles_dir().getPath();
        String app_name = Settings.get_APP_NAME();
        String err_path = dir_path + "/"+ app_name +"_err.log";
        String out_path = dir_path + "/"+ app_name +"_out.log";

        if(D) MLog.log_status("err_path=["+ err_path +"]");
        if(D) MLog.log_status("out_path=["+ out_path +"]");

        // DELETE EXISTING LOG FILES .. REDIRECT TO NEW LOG FILES
        String file_path = "";

        file_path = err_path;
        try {
            File file = new File( file_path ); if( file.exists() ) file.delete();
            PrintStream err = new PrintStream(new FileOutputStream( file_path ));
            if(M) Settings.MOM(TAG_GUI, "System.err.close()");
//*DBG*/ System.err.close();
//*DBG*/ System.setErr( err );
            if(M) Settings.MOM(TAG_GUI, "System.setErr( err )");
        }
        catch(Exception ex) { MLog.log("*** "+file_path+": "+ ex.getMessage()); }

        file_path = out_path;
        try {
            File file = new File( file_path ); if( file.exists() ) file.delete();
            PrintStream out = new PrintStream(new FileOutputStream( file_path ));
            System.out.println("System.out.close()");
            System.out.close();
            System.setOut( out );
            System.out.println("System.setOut( out )");
        }
        catch(Exception ex) { MLog.log("*** "+file_path+": "+ ex.getMessage()); }

        set_np_label_fg("ERRLOGGED", Settings.get_APP_NAME() +" ("+ get_APK_AGE() +")\nERRLOGGED is ON", Color.RED);
        Settings.ERRLOGGED = true;
    }
    //}}}
    // set_APP_freezed_state {{{
    public void set_APP_freezed_state(boolean state, String caller)
    {
        caller += "->set_APP_freezed_state("+state+")";
//*COMM*/Settings.MOC(TAG_COMM, caller);

        // toggle state
        Settings.FREEZED = state;
        checkBox_freeze.setChecked( state );

        // LOG
if(D) MLog.log(Settings.FREEZED ? Settings.SYMBOL_freezed+" FREEZED\n" : Settings.SYMBOL_freezed+" UNFREEZED");
        update_log_container_scrolling();

        // DASH
        this_RTabsClient.update_dash();

        // UI
        set_np_label_bg("FREEZED"
                , Settings.FREEZED ? "FREEZED is SET"         : "FREEZED is OFF"
                , Settings.FREEZED ? Settings.COLOR_FIREBRICK : Settings.COLOR_FORESTGREEN
                );

        // STOP CURRENT STAGE LOOPS
        String oo = this_RTabsClient.progress_POLL();
//*POLL*/Settings.MOC(TAG_POLL, caller+": "+oo);

        if( Settings.FREEZED )
        {
            data_looper_stop(caller);

            poll_looper_stop(caller);
        }
        // RESTART CURRENT STAGE LOOPS
        else {
            this_RTabsClient.clear_max_connection_failed(caller);

            if( !Settings.OFFLINE )
                enter_notification_loop(current_stage,   caller);

            poll_looper_start(caller);
        }
    }
//}}}
    // set_APP_offline_state {{{
    public void set_APP_offline_state(boolean state, String caller)
    {
        caller += "->set_APP_offline_state("+state+")";
//*COMM*/Settings.MOC(TAG_COMM, caller);

        // toggle state
        Settings.OFFLINE = state;
    //  checkBox_offline.setChecked( state );

        if(Settings.OFFLINE)
            this_RTabsClient.disconnect(caller);

        // LOG
if(D) MLog.log(Settings.OFFLINE ? Settings.SYMBOL_offline+" OFFLINE\n" : Settings.SYMBOL_offline+" ONLINE");

        update_log_container_scrolling();

        // DASH
        this_RTabsClient.update_dash();

        // UI
        set_np_label_bg(  "OFFLINE"
                , Settings.OFFLINE ? "OFFLINE"                : "ONLINE"
                , Settings.OFFLINE ? Settings.COLOR_FIREBRICK : Settings.COLOR_FORESTGREEN
                );

        // STOP CURRENT STAGE LOOPS
//*POLL*/Settings.MOC(TAG_POLL, "set_APP_offline_state");
        this_RTabsClient.progress_POLL();

        if( Settings.OFFLINE )
        {
            data_looper_stop(caller);

            // keep poll_looper running (sensing battery status)
        }
        // RESTART CURRENT STAGE LOOPS
        else if( !Settings.FREEZED )
        {
            this_RTabsClient.clear_max_connection_failed(caller);
            enter_notification_loop(STAGE6_CONNECTING,   caller);
        }
    }
//}}}
    // update_log_container_scrolling {{{
    // schedule_update_log_container_scrolling {{{
    public void schedule_update_log_container_scrolling()
    {
        handler.re_postDelayed( hr_update_log_container_scrolling, 100);
    }
    //}}}
    // hr_update_log_container_scrolling {{{
    private final Runnable hr_update_log_container_scrolling = new Runnable()
    {
        @Override
        public void run() {
            update_log_container_scrolling();
        }
    };
    // }}}
    private void update_log_container_scrolling() // {{{
    {
        LScrollView sv = log_container;

        View cv = sv.getChildAt(sv.getChildCount() - 1);
        int cvB = cv.getBottom();
        int cvH = cv.getHeight();
        int svH = sv.getHeight();
        int svY = sv.getScrollY();

        //
        //   (cv.Height ------------) == bottom
        // - (sv.Height + sv.ScrollY) == hidden above top
        // --------------------------
        // = 000000000000000000000000

        int scroll_y = (cvB - (svH + svY));
        int lineH    = log_text.getLineHeight();

        boolean page_filled = (cvH > svH) && (log_text.getText().length() > 0);

        if( !MLog.log_text_scrolled_by_user ) {
            if(page_filled) cmd_container.setVisibility( View.VISIBLE );

            log_text     .setTextColor      ( MLog.Log_ForeColor_Scroll_ON );
            log_text     .setBackgroundColor( MLog.Log_BackColor_Scroll_ON );
            log_container.setBackgroundColor( MLog.Log_BackColor_Scroll_ON );

            log_container.scrollTo(0, cvB);
        }
        else {
            Settings.set_logging_last_user_activation_time();

            if(page_filled) cmd_container.setVisibility( View.GONE );

            log_text     .setTextColor      ( MLog.Log_ForeColor_Scroll_OFF );
            log_container.setBackgroundColor( MLog.Log_BackColor_Scroll_OFF );
            log_text     .setBackgroundColor( MLog.Log_BackColor_Scroll_OFF );
        }

        if     (checkBox_freeze.isChecked()     ) log_container.setBackgroundColor( MLog.Log_BackColor_FREEZED );
        else if(log_text.getText().length() == 0) log_container.setBackgroundColor( MLog.Log_BackColor_STANDBY );

        // autoscroll unchanged unless it comes from the user
        if((System.currentTimeMillis() - MLog.scrolled_by_log_time) > 500)
            MLog.log_text_scrolled_by_user = (scroll_y > (lineH/2));

    }

    //}}}
    //}}}
    //}}}
    // APP CLIENT {{{

    public String dump_STATUS_CLIENT() { return (this_RTabsClient != null) ? this_RTabsClient.dump_STATUS_CLIENT() : ""; }
    public String dump_DEVICE()        { return (this_RTabsClient != null) ? this_RTabsClient.dump_DEVICE()        : ""; }
    public String dump_KEY_VALS()      { return (this_RTabsClient != null) ? this_RTabsClient.dump_KEY_VALS()      : ""; }
    public String dump_PALETTES()      { return (this_RTabsClient != null) ? this_RTabsClient.dump_PALETTES()      : ""; }
    public String dump_TABS_LIST()     { return (this_RTabsClient != null) ? this_RTabsClient.dump_TABS_LIST()     : ""; }

    // }}}
    // Log_init Log_append Log_toString {{{
    // -----------------------------------------------------------------------
    // NOTE: to be used by non-nested calling function in a sequence such as:
    // -----------------------------------------------------------------------
    // Log_init     ----------------------------------------------------------
    // Log_append+  ----------------------------------------------------------
    // Log_toString ----------------------------------------------------------
    // -----------------------------------------------------------------------
    private static final StringBuilder Log_sb    = new StringBuilder();
    public  static void                Log_init  (String msg) {        Log_sb.delete(0,    Log_sb.length()   ); Log_append( msg ); }
    public  static void                Log_append(String msg) {        Log_sb.append(msg); Log_sb.append("\n"); }
    public  static String              Log_toString()         { return Log_sb.toString(); }
    //}}}
    // TRACES {{{
    // log {{{
    private static void log(String msg)
    {
        MLog.log(msg);
    }
    //}}}
    // trace_views {{{
    private void trace_views(String tag, String caller)
    {
        trace_open(tag, caller);

        _trace_header      ("HANDLES");//-------------------------------------------------------------------------------
        _trace_boolean_transition     ("is_GUI_TYPE_HANDLES"              , Settings.is_GUI_TYPE_HANDLES       ());
        _trace_boolean_transition     ("...lft_handle_current"            , is_lft_handle_current              ());
        _trace_boolean_transition     ("...lft_handle_showing"            , is_handle_showing        (lft_handle));
        _trace_boolean_transition     ("...top_handle_current"            , is_top_handle_current              ());
        _trace_boolean_transition     ("...top_handle_showing"            , is_handle_showing        (top_handle));
        _trace_boolean_transition     ("...mid_handle_current"            , is_mid_handle_current              ());
        _trace_boolean_transition     ("...mid_handle_showing"            , is_handle_showing        (mid_handle));
        _trace_boolean_transition     ("...bot_handle_current"            , is_bot_handle_current              ());
        _trace_boolean_transition     ("...bot_handle_showing"            , is_handle_showing        (bot_handle));
        _trace_boolean_transition     ("...log_container_showing"         , is_view_showing       (log_container));
        _trace_boolean_transition     ("...controls_container_showing"    , is_view_showing  (controls_container));
        _trace_boolean_transition     ("...seekers_container_showing"     , is_view_showing   (seekers_container));

        _trace_header      ("DIALOG");//--------------------------------------------------------------------------------
        _trace_boolean_transition     ("ip_dialog_showing"                , is_dialog_showing          (ip_dialog));
        _trace_boolean_transition     ("note_dialog_showing"              , is_dialog_showing        (note_dialog));
        _trace_boolean_transition     ("profile_save_dialog_showing"      , is_dialog_showing(profile_save_dialog));

        _trace_header      ("FULLSCREEN");//----------------------------------------------------------------------------
        _trace_boolean_transition     ("fs_button_showing"                , is_view_showing            (fs_button));
        _trace_boolean_transition     ("is_at_left_margin"                , is_at_left_margin                   ());

        _trace_header      ("WEBVIEW");//-------------------------------------------------------------------------------
        _trace_boolean_transition     ("fs_webView_showing"               , is_view_showing          (fs_webView ));
        _trace_boolean_transition     ("fs_webView2_showing"              , is_view_showing          (fs_webView2));
        _trace_boolean_transition     ("fs_webView3_showing"              , is_view_showing          (fs_webView3));
        _trace_boolean_transition     ("fs_webView_isGrabbed"             , fs_webView_isGrabbed                  );
        _trace_boolean_transition     ("fs_webView_expanded"              , fs_webView_expanded                   );

        _trace_header      ("BAND");//----------------------------------------------------------------------------------
        _trace_boolean_transition     ("dck_handle_showing"               , is_view_showing     (dck_handle));
        _trace_boolean_transition     ("show_band_showing"                , is_view_showing      (show_band));
        _trace_boolean_transition     ("...show_band_hidden"              , show_band_hidden                );
        _trace_boolean_transition     ("dock_band_showing"                , is_view_showing      (dock_band));
        _trace_boolean_transition     ("...dock_band fully_visible"       , is_band_fully_visible(dock_band));
        _trace_boolean_transition     ("...dock_band fully_hidden"        , is_band_fully_hidden (dock_band));
        _trace_boolean_transition     ("hist_band_showing"                , is_view_showing      (hist_band));
        _trace_boolean_transition     ("...hist fully_visible"            , is_band_fully_visible(hist_band));
        _trace_boolean_transition     ("...hist fully_hidden"             , is_band_fully_hidden (hist_band));
        _trace_boolean_transition     ("cart_band_showing"                , is_view_showing      (cart_band));
        _trace_boolean_transition     ("...cart fully_visible"            , is_band_fully_visible(cart_band));
        _trace_boolean_transition     ("...cart fully_hidden"             , is_band_fully_hidden (cart_band));

        _trace_header      ("ANIM");//----------------------------------------------------------------------------------
        _trace_boolean_transition     ("...slide_band_anim_queue_is_empty", slide_band_anim_queue_is_empty());

        _trace_header      ("DRAGGING");//------------------------------------------------------------------------------
        _trace_string_transition      ("..............drag_band........." , get_view_name(drag_band)                   );
        _trace_boolean_transition     ("..............dragBand_moved...." ,    dragBand_moved                          );
        _trace_boolean_transition     ("..............dragBand_scrolled." ,    dragBand_scrolled                       );
        _trace_boolean_transition     ("..............dragBand_showing.." , is_dragBand_showing()                      );
        _trace_string_transition      ("...gesture_down_SomeView_atXY..." , get_view_name(gesture_down_SomeView_atXY  ));
        _trace_string_transition      ("...gesture_down_DocHisCar_atX..." , get_view_name(gesture_down_DocHisCar_atX  ));
        _trace_string_transition      ("...gesture_down_Wti_or_Wc_atY..." , get_view_name(gesture_down_Wti_or_Wc_atY  ));
        _trace_string_transition      ("...gesture_consumed_by_onDown..." ,               gesture_consumed_by_onDown   );
        _trace_string_transition      ("...gesture_consumed_by_magnify.." ,               gesture_consumed_by_magnify  );
        _trace_string_transition      ("...gesture_consumed_by_ACTION_UP" ,               gesture_consumed_by_ACTION_UP);
        _trace_string_transition      ("...gesture_consumed_by_onFling.." ,               gesture_consumed_by_onFling  );

        _trace_header      ("GROUPING");//------------------------------------------------------------------------------
        _trace_boolean_transition     ("...this_RTabsClient.is_GROUPING." , this_RTabsClient.is_GROUPING()             );
        _trace_boolean_transition     ("......Grouping_ACTION_UP_handled" ,      RTabsClient.Grouping_ACTION_UP_handled);

        trace_close      (tag);
    }
    //}}}
    // trace_open {{{
    private void trace_open(String tag, String caller)
    {
        /**/ caller   = " ["+caller+"] ";
        caller_length = caller.length();
        caller_length = Math.min(caller_length,128);
        caller_length = Math.max(caller_length, 64);

        String  s;

        s = String.format("@@@ o%."+caller_length+"so", SEP_TOP);
        MLog.log_nonl(s);

        s = String.format("@@@ >%"+caller_length+"s|", caller);
        MLog.log_nonl(s);
    }
    //}}}
    // trace_close {{{
    private void trace_close(String tag)
    {
        String s;
        s = String.format("@@@ o%."+caller_length+"so", SEP_BOT);
        MLog.log_nonl(s);
    }
    //}}}
    // trace_string  {{{
    private void trace_int   (String tag, String key, int    val) { trace_string(tag, key, ""+val); }
    private void trace_string(String tag, String key, String val)
    {
        // PREPEND LEADING DOTS
        int key_length = key.length();
        if( key_length < SEP_LEN) key = SEP_DOT.substring(0, SEP_LEN-key_length) + key;
        String  s;
        /*...*/ s = String.format("@@@  %s= [%s]", key, val);
        s         = String.format("@@@ |%-"+caller_length+"s|", s);
        MLog.log_nonl(s);
    }
    //}}}
    // trace_boolean {{{
    private void trace_boolean(String tag, String key, boolean val)
    {
        // PREPEND LEADING DOTS
        int key_length = key.length();
        if( key_length < SEP_LEN) key = SEP_DOT.substring(0, SEP_LEN-key_length) + key;
        String  s;
        if(val) s = String.format("@@@  %s: TRUE", key);
        else    s = String.format("@@@  %s: -"   , key);
        s         = String.format("@@@ |%-"+caller_length+"s|", s);
        MLog.log_nonl(s);

    }
    //}}}
    // {{{
    private static final LinkedHashMap<String, Object> TRACES_Map = new LinkedHashMap<>();
    private static final String       SEP_TOP = "}}}ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo";
    private static final String       SEP_BOT = "{{{_____________________________________________________________________________";
    private static final String       SEP_DOT = "................................................................................";
    private static final int          SEP_LEN = 47;
    private              int    caller_length = 72;

    // is_band_fling_1_list_to_working_profile
    // 123456789_123456789_123456789_123456789_123456
    // 111111111_222222222_333333333_444444444_555555

    // }}}
    // _trace_header {{{
    private void _trace_header(String header)
    {
        String  s;
        s         = String.format("@@@ |%-"+caller_length+"s|", header);
        MLog.log_nonl(s);
    }
    //}}}
    // _trace_string_transition  {{{
    private void _trace_int_transition   (String key, int    val) { _trace_string_transition(key, ""+val); }
    private void _trace_string_transition(String key, String val)
    {
        String old_val = (String)TRACES_Map.get(key);

        if(    old_val == val                         ) return;   // skip unchanged values
        if(   (old_val != null) && old_val.equals(val)) return;   // skip unchanged values

        TRACES_Map.put(key, val);

        trace_string("...", key, val);
    }
    //}}}
    // _trace_boolean_transition {{{
    private void _trace_boolean_transition(String key, boolean val)
    {
        String old_val = (String)TRACES_Map.get(key);
        if(Boolean.parseBoolean(old_val) == val) return;   // skip unchanged values

        TRACES_Map.put(key, (val ? "true" : "false"));

        trace_boolean("...", key, val);
    }
    //}}}
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** INNER CLASSES */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ INNER CLASSES @ {{{
    // COLORS {{{
    private static final int SHOWBAND_BG_COLOR         = /* Color.TRANSPARENT; */ /* */ Color.parseColor("#0Bff0000");
    private static final int DOCKBAND_BG_COLOR         = /* Color.TRANSPARENT; */ /* */ Color.parseColor("#0B00ff00");
    private static final int HISTBAND_BG_COLOR         = /* Color.TRANSPARENT; */ /* */ Color.parseColor("#0B0000ff");
    private static final int CARTBAND_BG_COLOR         = /* Color.TRANSPARENT; */ /* */ Color.parseColor("#0Bff00ff");

    private static final int DOCK_SHADOW_COLOR         = Color.LTGRAY;
    private static final int DOCK_TEXT_COLOR           = Color.DKGRAY;

    private static final int HIST_TEXT_COLOR           = Color.WHITE;
    private static final int CART_TEXT_COLOR           = Color.BLACK;

    //}}}
    // ShowBand {{{
    private static class ShowBand extends RelativeLayout
    {
        // VARIABLES {{{
        private View            hist_show;
        private View            dock_show;
        private View            cart_show;
        // }}}
        // CONSTRUCTOR {{{
        public ShowBand(Context context) { super(context); _init(context); }
        private void _init(Context context)
        {
            setId( View.generateViewId() );
            setTag( Settings.CHILD_IGNORE_VISIBILITY);//CHILD_SHOWN_HIDDEN ); // all children may get visible but this one keeps hiding
            layout(context);
        //  setListeners(); // turned into activiy instance responsibility
            adjustBackgroundColors();
        }
        //}}}
        // layout {{{
        private void layout(Context context)
        {
            // VIEWS {{{
            // hist_show {{{
            hist_show = new View(context);
            hist_show.setId( View.generateViewId() );
            // }}}
            // dock_show {{{
            dock_show = new View(context);
            dock_show.setId( View.generateViewId() );
            // }}}
            // cart_show {{{
            cart_show = new View(context);
            cart_show.setId( View.generateViewId() );
            // }}}
            // }}}
            // LAYOUT {{{
            // ShowBand {{{
            RelativeLayout.LayoutParams rlp;
            rlp             = new RelativeLayout.LayoutParams(1,1);
            rlp.width       = Settings.BAND_HIDE_WIDTH;
            rlp.height      = ViewGroup.LayoutParams.MATCH_PARENT;
        //  rlp.addRule( RelativeLayout.ALIGN_PARENT_RIGHT                    );
        //  rlp.rightMargin = Handle.Get_DOCK_STANDBY_WIDTH() - rlp.width;

            this.setLayoutParams(rlp);
            // }}}
            // hist_show {{{
            rlp        = new RelativeLayout.LayoutParams(1,1);
            /*X*/rlp.addRule( RelativeLayout.ALIGN_PARENT_LEFT                     );
            /*X*/rlp.addRule( RelativeLayout.ALIGN_PARENT_RIGHT                    );
            /*Y*/rlp.addRule( RelativeLayout.ALIGN_PARENT_TOP                      );
            /*W*/rlp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
            /*H*/rlp.height = Settings.get_HANDLE_HEIGHT();

            hist_show.setLayoutParams( rlp );
            // }}}
            // dock_show {{{
            rlp        = new RelativeLayout.LayoutParams(1,1);
            /*X*/rlp.addRule( RelativeLayout.ALIGN_PARENT_LEFT                     );
            /*X*/rlp.addRule( RelativeLayout.ALIGN_PARENT_RIGHT                    );
            /*Y*/rlp.addRule( RelativeLayout.BELOW               ,hist_show.getId());
            /*W*/rlp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
            /*H*/rlp.addRule( RelativeLayout.ABOVE               ,cart_show.getId());

            dock_show.setLayoutParams( rlp );
            // }}}
            // cart_show {{{
            rlp        = new RelativeLayout.LayoutParams(1,1);
            /*X*/rlp.addRule( RelativeLayout.ALIGN_PARENT_LEFT                     );
            /*X*/rlp.addRule( RelativeLayout.ALIGN_PARENT_RIGHT                    );
            /*Y*/rlp.addRule( RelativeLayout.ALIGN_PARENT_BOTTOM                   );
            /*W*/rlp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
            /*H*/rlp.height = Settings.get_HANDLE_HEIGHT();

            cart_show.setLayoutParams( rlp );
            // }}}
            // }}}
            addView( hist_show );
            addView( dock_show );
            addView( cart_show );
        }
         //}}}
        // adjustBackgroundColors {{{
        private void adjustBackgroundColors()
        {
            this          .setBackgroundColor( SHOWBAND_BG_COLOR   & Settings.BG_ALPHA);
            this.hist_show.setBackgroundColor( Settings.HIST_COLOR & Settings.BG_ALPHA);
            this.dock_show.setBackgroundColor( Settings.DOCK_COLOR & Settings.BG_ALPHA);
            this.cart_show.setBackgroundColor( Settings.CART_COLOR & Settings.BG_ALPHA);
        }
        //}}}

        // setListeners {{{
/*
        private void setListeners() {
            hist_show  .setOnTouchListener( show_hist_OnTouchListener  );
            dock_show  .setOnTouchListener( show_dock_OnTouchListener  );
            cart_show  .setOnTouchListener( show_cart_OnTouchListener  );
        }
*/
        private void set_show_hist_OnTouchListener(View.OnTouchListener listener) { hist_show.setOnTouchListener( listener  ); }
        private void set_show_dock_OnTouchListener(View.OnTouchListener listener) { dock_show.setOnTouchListener( listener  ); }
        private void set_show_cart_OnTouchListener(View.OnTouchListener listener) { cart_show.setOnTouchListener( listener  ); }
        //}}}

        // set_selected {{{
        public void set_selected(View selected_show)
        {
            if(        (selected_show == dock_show)
                    || (selected_show == hist_show)
                    || (selected_show == cart_show))
            {
                dock_show.setVisibility( View.  VISIBLE);
                hist_show.setVisibility( View.  VISIBLE);
                cart_show.setVisibility( View.  VISIBLE);
            }
            else {
                dock_show.setVisibility( View.INVISIBLE);
                hist_show.setVisibility( View.INVISIBLE);
                cart_show.setVisibility( View.INVISIBLE);
            }

        }
        //}}}
        // initTouchDelegate {{{
        private static final int HIT_WIDTH    = 50;
        private             Rect delegateArea = new Rect(); // job todo signature
        private void initTouchDelegate()
        {
//*BAND*/Settings.MOC(TAG_BAND, "ShowBand.initTouchDelegate");
            hist_show.getHitRect(delegateArea); delegateArea.right  += HIT_WIDTH; delegateArea.left -= HIT_WIDTH;
            setTouchDelegate(new TouchDelegate(delegateArea, hist_show));

            dock_show.getHitRect(delegateArea); delegateArea.right  += HIT_WIDTH; delegateArea.left -= HIT_WIDTH;
            setTouchDelegate(new TouchDelegate(delegateArea, dock_show));

            cart_show.getHitRect(delegateArea); delegateArea.right  += HIT_WIDTH; delegateArea.left -= HIT_WIDTH;
            setTouchDelegate(new TouchDelegate(delegateArea, cart_show));

            delegateArea = null; // job done signature
        }
        //}}}

    }
    //}}}
    // -DockBand {{{
    private static class DockBand extends RelativeLayout
    {
        // VARIABLES {{{

        private ScrollView      container_sv;
        private RelativeLayout   container;

        //}}}
        // CONSTRUCTOR {{{
        public DockBand(Context context)    { super(context);   _init(context); }
        private void _init(Context context)
        {
            setId( View.generateViewId() );
        //  setTag( Settings.CHILD_IGNORE_VISIBILITY );
            layout(context);
        //  setListeners(); // turned into activiy instance responsibility
            setBackgroundColors();
        }
        //}}}
        // layout {{{
        private void layout(Context context)
        {
            // VIEWS {{{
            add_container(context);
            //}}}
            // LAYOUT {{{
            RelativeLayout.LayoutParams rlp;
            rlp        = new RelativeLayout.LayoutParams(1,1);
        //  rlp.width  = ViewGroup.LayoutParams.WRAP_CONTENT;
            rlp.width  = Handle.Get_DOCK_WIDTH();
            rlp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            setLayoutParams(rlp);

            // }}}
            addView( container_sv   );
        }
        // add_container {{{
        private void add_container(Context context)
        {
            // VIEWS {{{
            // container in container_sv {{{
            container       = new RelativeLayout(context);
            container.setPadding(10, 20, 10,  80); // left top right bottom
        //  container.setPadding( 1,  2,  1, 200);
            container.setGravity( Gravity.TOP             | Gravity.START);

            container_sv    = new ScrollView(context);
            container_sv.setScrollbarFadingEnabled(false);
            container_sv.setScrollBarSize( 10 ); // TODO set a reasonable value when done checking
            container_sv.addView( container );
            //}}}
            // }}}
            // LAYOUT {{{
            // container_sv {{{
            RelativeLayout.LayoutParams rlp;
            rlp             = new RelativeLayout.LayoutParams(1,1);
            rlp.height      = ViewGroup.LayoutParams.MATCH_PARENT;
            rlp.width       = ViewGroup.LayoutParams.MATCH_PARENT;

            container_sv.setLayoutParams( rlp );
            //}}}
            // }}}
        }
        //}}}
        //}}}
        // setBackgroundColors {{{
        private void setBackgroundColors()
        {
            this              .setBackgroundColor( DOCKBAND_BG_COLOR        );
            this.container    .setBackgroundColor( Settings.DOCK_COLOR      );
        }
        //}}}
        // setListeners {{{
    //  private void set_dock_hide_OnTouchListener  (View.OnTouchListener listener) { dock_hide  .setOnTouchListener( listener  ); }

        //}}}
        public int get_container_sv_ScrollY() { return container_sv.getScrollY(); }
        // scroll_container_sv_toY [WITH PROPERTY_SCROLL ANIMATION] {{{
        public void scroll_container_sv_toY(int y, int duration)
        {
//BAND//Settings.MON(TAG_ANIM, "scroll_container_sv_toY(duration="+duration+")");
            if( ANIM_SUPPORTED )
            {
                set_scroll_animation_running();

                AnimatorSet set = new AnimatorSet();
                set.setDuration( duration );
                set.setInterpolator( new DecelerateInterpolator() );
                set.play( ObjectAnimator.ofInt(container_sv, PROPERTY_SCROLL_Y, y) );
                set.addListener( scroll_animation_end_listener );
                set.start();
            }
            else {
                container_sv.scrollTo(0, y);
            }
        }
        // }}}
        // Property {{{
        public static final Property<View, Integer> PROPERTY_SCROLL_Y =
        new Property<View, Integer>(Integer.class, "viewScrollY")
        {
            @Override public void    set(View v, Integer value) { v.scrollTo(0, value); }
            @Override public Integer get(View v               ) { return v.getScrollY();           }
        };

        //}}}
        // scroll_animation_running {{{
        private static  boolean                                         scroll_animation_running = false;
        public             void set_scroll_animation_running() {        scroll_animation_running =  true; }
        public          boolean  is_scroll_animation_running() { return scroll_animation_running        ; }
        private final AnimatorListenerAdapter scroll_animation_end_listener = new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {  scroll_animation_running = false; }
        };

        // }}}

        // get_dock_button_at_Y {{{
        public NpButton get_dock_button_at_Y(int y)
        {
            View child;
            int h=0;
            for(int i=0; i < container.getChildCount(); ++i)
            {
                child      = container.getChildAt(i);
                if(i==0) h = child.getHeight();
                int    c_y = (int)child.getY();
                if(        ((c_y    ) <= y)
                        && ((c_y + h) >= y)
                  )
                    return (NpButton)child; // this button contains the given y
            }
            return null;
        }
        //}}}
    }

    //}}}
    // -HistBand {{{
    private static class HistBand extends RelativeLayout
    {
        // VARIABLES {{{
        private NpButton         back_nb;
        private NpButton         frwd_nb;
        private NpButton         prof_nb;
        // }}}
        // CONSTRUCTOR {{{
        public HistBand(Context context)    { super(context);   _init(context); }
        private void _init(Context context)
        {
            setId( View.generateViewId() );
            setTag( Settings.CHILD_IGNORE_VISIBILITY );
            layout(context);
        //  setListeners(); // turned into activiy instance responsibility
            setBackgroundColors();
        }
        //}}}
        // layout {{{
        private void layout(Context context)
        {
            // VIEWS {{{
            // -HIST_BAK {{{
            back_nb = new NpButton(context, NotePane.SHAPE_TAG_PADD_R);
            back_nb.fixedTextSize = true;
            back_nb.setId( View.generateViewId() );

            back_nb.setGravity        ( Gravity.CENTER                );
            back_nb.setText           ( Settings.SYMBOL_PRPREV        );
            back_nb.setTag            ( Settings.BACK_NB_INFO         );
            back_nb.setTextColor      ( HIST_TEXT_COLOR               );
            back_nb.setTypeface       ( Settings.getNotoTypeface()    );
            // }}}
            // -HIST_FWD {{{
            frwd_nb = new NpButton(context, NotePane.SHAPE_TAG_PADD_R);
            frwd_nb.fixedTextSize = true;
            frwd_nb.setId( View.generateViewId() );

            frwd_nb.setGravity        ( Gravity.CENTER                );
            frwd_nb.setText           ( Settings.SYMBOL_PRNEXT        );
            frwd_nb.setTag            ( Settings.FORE_NB_INFO         );
            frwd_nb.setTextColor      ( HIST_TEXT_COLOR               );
            frwd_nb.setTypeface       ( Settings.getNotoTypeface()    );
            // }}}
            // -HIST_PROF {{{
            prof_nb = new NpButton(context, NotePane.SHAPE_TAG_PADD_R );
            prof_nb.fixedGravity = true;
            prof_nb.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            prof_nb.setId( View.generateViewId()                      );

            prof_nb.setTextColor      ( DOCK_TEXT_COLOR               );
            prof_nb.setText           ( Settings.SYMBOL_MENU          );
            prof_nb.setTag            ( Settings.PROF_NB_INFO         );
            prof_nb.setTypeface       ( Settings.getNotoTypeface()    );
            // }}}
            // }}}
            // LAYOUT {{{
            // hist at bottom .. prof get vertical remainder above hist
            // HistBand {{{
            RelativeLayout.LayoutParams rlp;
            rlp        = new RelativeLayout.LayoutParams(1,1);
            rlp.width  = Handle.Get_DOCK_STANDBY_WIDTH();// - Settings.BAND_HIDE_WIDTH;
            rlp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            rlp.addRule( RelativeLayout.ALIGN_PARENT_RIGHT );

            setLayoutParams(rlp);
            // }}}
            // -back_nb {{{
            int margin       = 8;
            int height       = (Settings.get_HANDLE_HEIGHT() - 3*margin) / 2;
            int width        = Handle.Get_DOCK_STANDBY_WIDTH();// - Settings.BAND_HIDE_WIDTH;
            width = height   = Math.max(width, height);
            rlp              = new RelativeLayout.LayoutParams(1,1);
            rlp.width        = width;
            rlp.height       = height;
            rlp.topMargin    = margin;
            rlp.addRule        (RelativeLayout.ALIGN_PARENT_LEFT                     );
            rlp.addRule        (RelativeLayout.ALIGN_PARENT_TOP                      );

            back_nb.setLayoutParams( rlp );
            // }}}
            // -frwd_nb {{{
            rlp              = new RelativeLayout.LayoutParams(1,1);
            rlp.width        = Handle.Get_DOCK_STANDBY_WIDTH();// - Settings.BAND_HIDE_WIDTH;
            rlp.height       = height;
            rlp.topMargin    = margin;
            rlp.addRule        (RelativeLayout.ALIGN_PARENT_LEFT                     );
            rlp.addRule        (RelativeLayout.BELOW              ,   back_nb.getId());

            frwd_nb.setLayoutParams( rlp );
            // }}}
            // -prof_nb {{{
            rlp              = new RelativeLayout.LayoutParams(1,1);
            rlp.width        = ViewGroup.LayoutParams.WRAP_CONTENT;
            rlp.topMargin    = margin;
            rlp.bottomMargin = margin;
            rlp.addRule        (RelativeLayout.ALIGN_PARENT_LEFT                     );
            rlp.addRule        (RelativeLayout.ALIGN_PARENT_RIGHT                    );
            rlp.addRule        (RelativeLayout.BELOW              ,   frwd_nb.getId());
            rlp.addRule        (RelativeLayout.ALIGN_PARENT_BOTTOM                   );

            prof_nb.setLayoutParams( rlp );
            // }}}
            // }}}
            addView(  prof_nb  );
            addView(  back_nb  );
            addView(  frwd_nb  );
        }
         //}}}
        // setBackgroundColors {{{
        private void setBackgroundColors()
        {
            this        .setBackgroundColor( HISTBAND_BG_COLOR       );
            this.prof_nb.setBackgroundColor( Settings.PROF_NP_COLOR  );
            this.back_nb.setBackgroundColor( Settings.BACK_NP_COLOR  );
            this.frwd_nb.setBackgroundColor( Settings.FORE_NP_COLOR  );

        }
        //}}}
        // setListeners {{{
/*
        private void setListeners()
        {
            back_nb.setOnTouchListener    ( builtin_nb_OnTouchListener );
            frwd_nb.setOnTouchListener    ( builtin_nb_OnTouchListener );
            prof_nb.setOnTouchListener    ( show_dock_OnTouchListener  );

        }
*/

        //}}}
    }
    //}}}
    // -CartBand {{{
    private static class CartBand extends RelativeLayout
    {
        // VARIABLES {{{
        private NpButton         see_nb;
        private NpButton         end_nb;
        // }}}
        // CONSTRUCTOR {{{
        public CartBand(Context context)    { super(context);   _init(context); }
        private void _init(Context context)
        {
            setId( View.generateViewId() );
            setTag( Settings.CHILD_IGNORE_VISIBILITY ); // all Handle children may get visible but this one keeps hiding
            layout(context);
        //  setListeners(); // turned into activiy instance responsibility
            setBackgroundColors();
        }
        //}}}
        // layout {{{
        private void layout(Context context)
        {
            // VIEWS {{{
            // CartBand {{{
            RelativeLayout.LayoutParams rlp;

            // }}}
            // --see_nb {{{
            see_nb = new NpButton(context, NotePane.SHAPE_TAG_PADD_R);
            see_nb.setId( View.generateViewId() );
            see_nb.fixedTextSize = true;

            see_nb.setGravity        ( Gravity.CENTER               );
            see_nb.setText           ( Settings.SYMBOL_PRPREV       );
            see_nb.setTag            ( Settings.SEE_NB_INFO         );
            see_nb.setTextColor      ( CART_TEXT_COLOR              );
        //  see_nb.setTypeface       ( Settings.getNotoTypeface()   );
            // }}}
            // --end_nb {{{
            end_nb = new NpButton(context, NotePane.SHAPE_TAG_CIRCLE);
        //  end_nb = new NpButton(context, NotePane.SHAPE_TAG_PADD_R);
            end_nb.setId( View.generateViewId() );
            end_nb.fixedTextSize = true;

            end_nb.setGravity        ( Gravity.CENTER               );
            end_nb.setText           ( Settings.SYMBOL_LEFT_ARROW   );
            end_nb.setTag            ( Settings.END_NB_INFO         );
            end_nb.setTextColor      ( CART_TEXT_COLOR              );
        //  end_nb.setTypeface       ( Settings.getNotoTypeface()   );
            // }}}
            // }}}
            // LAYOUT {{{
            // CartBand (||) {{{
            int margin       = 8;
            rlp        = new RelativeLayout.LayoutParams(1,1);
            rlp.width  = Handle.Get_DOCK_STANDBY_WIDTH();// - Settings.BAND_HIDE_WIDTH;
            rlp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            rlp.addRule( RelativeLayout.ALIGN_PARENT_RIGHT                    );

            setLayoutParams(rlp);
            // }}}
            // --see_nb (--) {{{

            rlp              = new RelativeLayout.LayoutParams(1,1);
            rlp.width        = ViewGroup.LayoutParams.WRAP_CONTENT;
            rlp.topMargin    = margin;
            rlp.height       = ViewGroup.LayoutParams.WRAP_CONTENT;
            rlp.addRule(       RelativeLayout.ALIGN_PARENT_TOP                      );
            rlp.addRule(       RelativeLayout.ALIGN_PARENT_LEFT                     );
            rlp.addRule(       RelativeLayout.ALIGN_PARENT_RIGHT                    );
            rlp.addRule(       RelativeLayout.ABOVE               ,  end_nb.getId() );

            see_nb.setLayoutParams( rlp );
            // }}}
            // --end_nb (Done) {{{
            rlp              = new RelativeLayout.LayoutParams(1,1);
            rlp.width        = ViewGroup.LayoutParams.WRAP_CONTENT;
            rlp.topMargin    = margin;
        //  rlp.height       = ViewGroup.LayoutParams.WRAP_CONTENT;
            rlp.height       = Handle.Get_DOCK_STANDBY_WIDTH();// - Settings.BAND_HIDE_WIDTH;
            rlp.bottomMargin = margin;
            rlp.addRule(       RelativeLayout.ALIGN_PARENT_LEFT                     );
            rlp.addRule(       RelativeLayout.ALIGN_PARENT_RIGHT                    );
            rlp.addRule(       RelativeLayout.ALIGN_PARENT_BOTTOM                   );

            end_nb.setLayoutParams( rlp );
            // }}}
            // }}}
            addView(  see_nb   );
            addView(  end_nb   );
        }
        //}}}
        // setBackgroundColors {{{
        private void setBackgroundColors()
        {
            this        .setBackgroundColor( CARTBAND_BG_COLOR     );
            this.see_nb .setBackgroundColor( Settings.SEE_NP_COLOR );
            this.end_nb .setBackgroundColor( Settings.END_NP_COLOR );

        }
        //}}}
        // setListeners {{{
        private void set_see_OnTouchListener    (View.OnTouchListener     listener) { see_nb.setOnTouchListener    ( listener ); }
        private void set_end_OnTouchListener    (View.OnTouchListener     listener) { end_nb.setOnTouchListener    ( listener ); }

        private void set_see_OnLongClickListener(View.OnLongClickListener listener) { see_nb.setOnLongClickListener( listener ); }

        //}}}
    }
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** SOUND */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ SOUND @ {{{
    // SoundPoolManager {{{
    private static class SoundPoolManager
    {
        // PROPERTIES
        //{{{
        private static SoundPool                    soundPool;
        private static HashMap<String, SoundSample> Sound_Dict;
        private static final float                  LEFTVOLUME  = .99f;
        private static final float                  RIGHTVOLUME = .99f;
        private static final float                  RATE        = 1.0f;

        // }}}
        // initialize {{{
        public  static void initialize()
        {
            if(Sound_Dict != null) return;
//*SOUND*/Settings.MOC(TAG_SOUND, "SoundPoolManager.initialize");
            _init();
        }
        //}}}
        // play {{{
        public  static void play(String sound_name              ) { play(sound_name, 0); }
        public  static void play(String sound_name, float volume)
        {
//*SOUND*/Settings.MOC(TAG_SOUND, "SoundPoolManager.play("+sound_name+")");

            if(Sound_Dict == null) _init(); // early initialization
            if(Sound_Dict == null) return;

            SoundSample ss = Sound_Dict.get( sound_name );
            if(  ss          == null ) _load_sound_name( sound_name, true); // NOT YET LOADED
            if(  ss          == null ) return;                              // NOT FOUND
            if(  ss.sampleId <= 0    ) return;                              // SOMETHING WRONG
            if(  ss.isLoaded         ) _play_ss(ss, volume);
        //  else ss.play_once_onLoadComplete = true;
        }

        private static void _play_ss(SoundSample ss, float volume)
        {
//*SOUND*/Settings.MOC(TAG_SOUND, "SoundPoolManager._play_ss("+ss.sampleId+", "+volume+")");

            float lv = (volume != 0) ? volume :  LEFTVOLUME;
            float rv = (volume != 0) ? volume : RIGHTVOLUME;

            int streamID = soundPool.play(ss.sampleId, lv, rv,        1,    0, RATE);
            //.............soundPool.play(    soundID, lv, rv, priority, loop, RATE)
            //@return non-zero streamID if successful, zero if failed

            // this has to be cleared by the playing thread
            if( ss.play_once_onLoadComplete)
                ss.play_once_onLoadComplete = false;
        }
        //}}}
        // stop {{{
        public static void stop()
        {
//*SOUND*/Settings.MOC(TAG_SOUND, "SoundPoolManager.stop");
            if(Sound_Dict == null) return;
            if(soundPool  == null) return;

            for(Map.Entry<String, SoundSample> entry : Sound_Dict.entrySet())
            {
                //  String   sound_name = entry.getKey  ();
                SoundSample      ss = entry.getValue();
                soundPool.stop(  ss.sampleId );
            }
        }
        //}}}
        // release {{{
        public static void release()
        {
//*SOUND*/Settings.MOC(TAG_SOUND, "SoundPoolManager.release");
            if(soundPool != null)
                soundPool.release();
            soundPool  = null;
            Sound_Dict = null;
        }
        //}}}
        // handle_MEMORY {{{
        public static void handle_MEMORY()
        {
//*SOUND*/Settings.MOC(TAG_SOUND, "SoundPoolManager.handle_MEMORY");
            release();
        }
        //}}}
        // _init {{{
        @SuppressWarnings("deprecation")
        private static void _init()
        {
//*SOUND*/Settings.MOC(TAG_SOUND, "SoundPoolManager._init");
            if(Sound_Dict != null) return;
            // SoundPool {{{
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                soundPool
                    = new SoundPool( 8                          // maxStreams
                            ,        AudioManager.STREAM_MUSIC  // streamType
                            ,        0);                        // srcQuality
            }
            else {
//              AudioAttributes audioAttributes
//                  = new AudioAttributes.Builder()
//                  .      setContentType( AudioAttributes.CONTENT_TYPE_MUSIC )
//                  .      setUsage      ( AudioAttributes.USAGE_GAME         )
//                  .      build();

                soundPool
                    = new SoundPool.Builder()
                    .setMaxStreams     ( 8               )
//                  .setAudioAttributes( audioAttributes )
                    .build();
            }

            // load completion listener
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    _set_sampleId_loaded(sampleId, (status == 0));
                }
            });

            // }}}
            // Sound_Dict {{{
            Sound_Dict= new HashMap<>();
/*
            // PRELOAD ALL [SOUND_FILES]
            for(int i=0;        i < Settings.SOUND_FILES.length; ++i) {
                String sound_name = Settings.SOUND_FILES[i];
                _load_sound_name(sound_name, false);
            }
*/
            // ...OR ONLY CURRENT [SOUND_CLICK] AND [SOUND_DING]
            _load_sound_name(Settings.SOUND_DING , false); // !play_once_onLoadComplete
            _load_sound_name(Settings.SOUND_CLICK, false); // !play_once_onLoadComplete

            //}}}
        }
        //}}}
        // _load_sound_name {{{
        private static void _load_sound_name(String sound_name, boolean play_once_onLoadComplete)
        {
//*SOUND*/Settings.MOC(TAG_SOUND, "SoundPoolManager._load_sound_name("+sound_name+")");

            String sound_path = Settings.SOUND_DIR + sound_name;
//*SOUND*/Settings.MOM(TAG_SOUND, "...sound_path=["+sound_path+"]");

            int      sampleId = soundPool.load(sound_path, 1); // prority (unused as of [160825])
            Sound_Dict.put(sound_name, new SoundSample(sampleId, play_once_onLoadComplete));
        }
        //}}}
        // _set_sampleId_loaded {{{
        private static void _set_sampleId_loaded(int sampleId, boolean _isLoaded)
        {
//*SOUND*/Settings.MOC(TAG_SOUND, "SoundPoolManager._set_sampleId_loaded("+sampleId+", "+_isLoaded+")");

            if(Sound_Dict == null) return;

            for(Map.Entry<String, SoundSample> entry : Sound_Dict.entrySet())
            {
                SoundSample ss = entry.getValue();
                if(ss.sampleId == sampleId)
                {
                    ss.isLoaded = _isLoaded;

                    if(ss.isLoaded && ss.play_once_onLoadComplete)
                    {
//*SOUND*/Settings.MOM(TAG_SOUND, "xxx handling play_once_onLoadComplete");
                        _play_ss(ss, 0f);
                    }
                    break;
                }
            }
        }
        //}}}
    }
    // }}}
    // SoundSample {{{
    private static class SoundSample
    {
        public final int     sampleId;
        public       boolean isLoaded = false;
        public       boolean play_once_onLoadComplete = false;

        public SoundSample(int sampleId, boolean play_once_onLoadComplete)
        {
//*SOUND*/Settings.MOC(TAG_SOUND, "new SoundSample(sampleId="+sampleId+", play_once_onLoadComplete="+play_once_onLoadComplete+")");
            this.sampleId                 = sampleId;
            this.play_once_onLoadComplete = play_once_onLoadComplete;
        }
    }
    // }}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** INNER LISTENERS */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ INNER LISTENERS @ {{{
    //  ShowBand Listeners {{{
    // show_hist_OnTouchListener {{{
    private final View.OnTouchListener show_hist_OnTouchListener = new View.OnTouchListener() {
        @Override public boolean onTouch(View view, MotionEvent event)
        {
            String caller = "[show_hist_OnTouchListener."+ get_view_name(view) +"."+Settings.Get_action_name(event)+"]";
//*BAND*/Settings.MON(TAG_BAND, caller, "show_band_hidden=["+show_band_hidden+"]");

            int action = event.getActionMasked();
            if(action == MotionEvent.ACTION_DOWN)
            {

                if(drag_band != null) clear_dragBand(caller);

                cancel_pending_button_to_magnify(caller);

                if( is_view_showing( hist_band )) fold_band(hist_band, caller);
                else                              show_histBand(FROM_TOUCH, caller);

                //if( !Settings.is_GUI_TYPE_HANDLES() ) show_tooltip("H\n...Profile History Navigation", Settings.HIST_COLOR, show_band.hist_show);
            }
            return true;
        }
    };
    //}}}
    // show_dock_OnTouchListener {{{
    private final View.OnTouchListener show_dock_OnTouchListener = new View.OnTouchListener() {
        @Override public boolean onTouch(View view, MotionEvent event)
        {
            int action = event.getActionMasked();
            if(action == MotionEvent.ACTION_DOWN)
            {
            String caller = "show_dock_OnTouchListener("+Settings.Get_action_name(event)+")";
//BAND*/Settings.MOC(TAG_BAND, caller);

                // CHECK IF MASKED BY FG_VIEW
                if(gesture_down_SomeView_atXY == get_fg_view()) {
//*BAND*/Settings.MOM(TAG_BAND, "...EVENT MASKED BY (gesture_down_SomeView_atXY == get_fg_view())");
                    return false; // not consumed
                }

                if(drag_band != null) clear_dragBand(caller);

                cancel_pending_button_to_magnify(caller);

                if     ( is_view_showing( hist_band ) ) show_dock    (FROM_TOUCH, caller);
                else if( is_view_showing( dock_band ) ) fold_band    ( dock_band, caller);
                else                                    show_histBand(FROM_TOUCH, caller);

                //show_tooltip("D\n...Profile Selection", Settings.DOCK_COLOR, show_band.dock_show);
            }
            return true;
        }
    };
    //}}}
    // show_cart_OnTouchListener {{{
    private final View.OnTouchListener show_cart_OnTouchListener = new View.OnTouchListener() {
        @Override public boolean onTouch(View view, MotionEvent event)
        {
            int action = event.getActionMasked();
            if(action == MotionEvent.ACTION_DOWN)
            {
                String caller = "show_cart_OnTouchListener("+Settings.Get_action_name(event)+")";
//*BAND*/Settings.MOC(TAG_BAND, caller);

                // CHECK IF MASKED BY FG_VIEW
                if(gesture_down_SomeView_atXY == get_fg_view()) {
//*BAND*/Settings.MOM(TAG_BAND, "...EVENT MASKED BY (gesture_down_SomeView_atXY == get_fg_view())");
                    return false; // not consumed
                }

                if(drag_band != null) clear_dragBand(caller);

                cancel_pending_button_to_magnify(caller);

                if( is_view_showing( cart_band ) ) fold_band(cart_band, caller);
                else                               show_cartBand(FROM_TOUCH, caller);

                //show_tooltip("D\n...TAB Edition", Settings.DOCK_COLOR, show_band.cart_show);
            }
            return true;
        }
    };
    //}}}
     //}}}
    // DockBand Listeners {{{
    // dock_toggle_OnClickListener - switch between DOCKINGS and TABS TABLE {{{
    private final View.OnClickListener dock_toggle_OnClickListener = new View.OnClickListener()
    {
        @Override public void onClick(View view)
        {
            String caller = "dock_toggle_OnClickListener()";
//*BAND*/Settings.MOC(TAG_BAND, caller);

            toggle_GUI_TYPE(FROM_TOUCH);
/*
            String tooltip
                = "G\n...GUI Toggle between:\n"
                +"- A dock of first level profiles\n"
                +"4 GUI Handles:\n"
                +"- List of all the defined Profiles\n"
                +"- Table of first-level profiles\n"
                +"- Application Controls Panel\n"
                +"- Info View"
                ;
            show_tooltip(tooltip, Settings.DOCK_COLOR, show_band.cart_show);
*/
        }
    };
    //}}}
    //}}}
    // CartBand Listeners {{{
//    // see_nb_OnTouchListener {{{
//    private final View.OnTouchListener see_nb_OnTouchListener = new View.OnTouchListener() {
//        @Override public boolean onTouch(View view, MotionEvent event)
//        {
//            int action = event.getActionMasked();
//            if(action == MotionEvent.ACTION_DOWN)
//                cart_see_click();
//            return false; // Long click listeners may take it from here as well
//        }
//    };
//    //}}}
//    // see_nb_OnClickListener - switch between [DEL to CART] and [ADD from CART]  {{{
//    private final View.OnClickListener see_nb_OnClickListener = new View.OnClickListener()
//    {
//        @Override public void onClick(View view)
//        {
//            String caller = "see_nb_OnClickListener()";
///*BAND*/Settings.MOC(TAG_BAND, caller);
//                cart_see_click();
//        }
//    };
//    //}}}
//    // end_nb_OnTouchListener {{{
//    private final View.OnTouchListener end_nb_OnTouchListener = new View.OnTouchListener() {
//        @Override public boolean onTouch(View view, MotionEvent event)
//        {
//            int action = event.getActionMasked();
//            if(action == MotionEvent.ACTION_DOWN)
//                cart_end_click(FROM_TOUCH);
//            return false; // Long click listeners may take it from here as well
//        }
//    };
//    //}}}
    // }}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** [builtin_nb_OnTouchListener] */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ [builtin_nb_OnTouchListener] @ {{{
    // onTouch {{{
    // {{{
    private static boolean click_callback_has_been_called;

    // }}}
    public static final View.OnTouchListener  builtin_nb_OnTouchListener = new View.OnTouchListener() {

        private   float down_x;
        private boolean has_moved;

        @Override public boolean onTouch(View view, MotionEvent event)
        {
            // caller {{{
            int     action     = event.getActionMasked();

            String  caller     = "builtin_nb_OnTouchListener"
                + (   (action == MotionEvent.ACTION_MOVE) ? ""      // .. no need for costly details on ACTION_MOVE
                        :      "("+Settings.Get_action_name(event)
                        +      " on ["+ RTabs_instance.get_view_name      (view)+"]"
                        +         " ["+ RTabs_instance.get_view_vibibility(view)+"]");

            //}}}
            switch(action) {
                case MotionEvent.ACTION_DOWN  :
                    // init [down_x] {{{
//*EV2_RT_CB*/Settings.MON(TAG_EV2_RT_CB, caller, "ACTION_DOWN");
                    down_x                          = event.getRawX();
                    has_moved                       = false;
                    click_callback_has_been_called  = false; // same effect for RTabs_instance.MotionListener
                    break;
                    //}}}
                case MotionEvent.ACTION_MOVE  :
                    // (MOVE callback) {{{
                    if(!has_moved) {
                        has_moved = (Math.abs(down_x- event.getRawX()) > Settings.SCALED_TOUCH_SLOP);
                        if( has_moved )
                        {
                            if( RTabs_instance.view_has_stroke_callback(view) )
                            {
//*EV2_RT_CB*/Settings.MON(TAG_EV2_RT_CB, caller, "FIRST_MOVE: (calling MOVE  callback)");
                                RTabs_instance.call_button_callback((NpButton)view, MotionEvent.ACTION_MOVE, caller);
                            }
                        }
                    }
                    break;
                    //}}}
                case MotionEvent.ACTION_UP    :
                    // (CLICK callback) {{{
//*EV2_RT_CB*/Settings.MON(TAG_EV2_RT_CB, caller, "ACTION_UP");
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "click_callback_has_been_called="+ click_callback_has_been_called);
//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, ".....................has_moved="+ has_moved);
                    if(!has_moved && !click_callback_has_been_called)
                        check_button_callback_on_ACTION_UP((NpButton)view, caller);

                    break;
                    //}}}
                case MotionEvent.ACTION_CANCEL:
                    // {{{
//*EV2_RT_CB*/Settings.MON(TAG_EV2_RT_CB, caller, "ACTION_CANCEL");

                    break;
                    // }}}
            }
            return false; // not consumed
        }
    };
    // }}}
    // check_button_callback_on_ACTION_UP {{{
    private static void check_button_callback_on_ACTION_UP(NpButton np_button, String caller)
    {
        if(!RTabs_instance.is_on_UP_CLICK_DURATION() ) return;
        if( RTabs_instance.has_moved_enough          ) return;

        caller += "->check_button_callback_on_ACTION_UP("+np_button+")";
//*EV2_RT_CB*/Settings.MON(TAG_EV2_RT_CB, caller, "(calling CLICK callback)");

        RTabs_instance.call_button_callback(np_button, MotionEvent.ACTION_UP, caller);
    }
    //}}}
    // view_has_stroke_callback {{{
    private boolean view_has_stroke_callback(View view)
    {
        // views relying on [builtin_nb_OnTouchListener]
        boolean result
            // HIST x3
            =  (view == hist_band.back_nb )
            || (view == hist_band.frwd_nb )
            || (view == hist_band.prof_nb )

            // CART x4
            || (view == cart_band.end_nb  )
            || (view == cart_band.see_nb  )

            // WEB x5
            || (view == fs_wtitle         ) // served by onFling
            || (view == fs_wtitle2        ) // served by onFling
            || (view == fs_wtitle3        ) // served by onFling
            || (view == fs_goBack         )
            || (view == fs_goForward      )
            ;

//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "view_has_stroke_callback("+ get_view_name(view) +"): ...return "+result);
        return result;
    }
    //}}}
    // view_has_release_callback {{{
    private boolean view_has_release_callback(View view)
    {
        // views relying on [builtin_nb_OnTouchListener]
        boolean result
            // (fg_view)
            = (view == get_fg_view()      )

            // HIST x3
            || (view == hist_band.back_nb )
            || (view == hist_band.frwd_nb )
            || (view == hist_band.prof_nb )

            // CART x4
            || (view == cart_band.end_nb  )
            || (view == cart_band.see_nb  )

            // WEB x8
            || (view == fs_webView        )
            || (view == fs_webView2       )
            || (view == fs_webView3       )
            || (view == fs_wtitle         )
            || (view == fs_wtitle2        )
            || (view == fs_wtitle3        )
            || (view == fs_wswapL         )
            || (view == fs_wswapR         )
            || (view == fs_browse         )
            || (view == fs_bookmark       )
            || (view == fs_goBack         )
            || (view == fs_goForward      )

            // WVTOOLS
            || wvTools.view_has_release_callback( view )
            ;

//*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "...view_has_release_callback("+ get_view_name(view) +") ...return "+result);
        return result;
    }
    //}}}
//    // view_has_an_ACTION_UP_handler {{{
//    private boolean view_has_an_ACTION_UP_handler(View view)
//    {
//        boolean result
//            =  (view != null)
//            &&  view_has_release_callback( view )
//            &&  view.isEnabled(); // i.e. a handler that has a chance to be called
///*EV2_RT_CB*/Settings.MOM(TAG_EV2_RT_CB, "view_has_an_ACTION_UP_handler("+get_view_name(view)+") ...return "+result);
//        return result;
//    }
//    //}}}
    // call_button_callback {{{
    private void call_button_callback(NpButton nb, int action, String caller)
    {
        caller += "->mRTabs.call_button_callback("+ get_view_name(nb) +")";
//*EV2_RT_CB*/Settings.MOC(TAG_EV2_RT_CB, caller);

        click_callback_has_been_called =  true; // .. (no need to call CLICK callback)

        // [fs_button] .. (magnify_np_OnClickListener)
        if     (nb == fs_button        ) return;

        // HIST
        if(nb == hist_band.back_nb) { history_back(FROM_TOUCH, caller); return; }
        if(nb == hist_band.frwd_nb) { history_frwd(FROM_TOUCH, caller); return; }
        if(nb == hist_band.prof_nb) { show_dock   (FROM_TOUCH, caller); return; }

        // CART
        if(nb == cart_band.see_nb ) { cart_see_click();           return; }
        if(nb == cart_band.end_nb ) { cart_end_click(FROM_TOUCH); return; }

        // WEBVIEW
        if(        (nb == fs_wtitle    )
                || (nb == fs_wtitle2   )
                || (nb == fs_wtitle3   ))
        {
            if(action == MotionEvent.ACTION_UP  ) fs_wtitle_CB(nb);
            // +++++++++ MotionEvent.ACTION_MOVE to be served by onFling
            return;
        }
        if(nb == fs_wswapL        ) { fs_wswapL_CB   (); return; }
        if(nb == fs_wswapR        ) { fs_wswapR_CB   (); return; }

        if(nb == fs_browse        ) { fs_browse_CB   (); return; }
        if(nb == fs_bookmark      ) { fs_bookmark_CB (); return; }

        if(nb == fs_goBack        ) { fs_goBack_CB   (); return; }
        if(nb == fs_goForward     ) { fs_goForward_CB(); return; }

        // WVTOOLS
        if( wvTools.call_button_callback(nb, action, caller) ) return;
    }
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}
    /** SANDBOX */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ LOG @ {{{
     // [toggleSandBox] [hideSandBox] {{{
    private boolean is_sandBox_showing = false;     // track state
//  private boolean is_sandBox_showing()            { return SandBox.is_sandBox_showing  ();                  }
    private void    toggleSandBox(String cmdLine)
    {
        if( SandBox.is_sandBox_implemented() )
        {
            is_sandBox_showing = (cmdLine.indexOf(" ") > 0); // .. (no argument calls for SandBox.hideSandBox)
            SandBox.toggleSandBox(activity, frameLayout, cmdLine);
        }
        else {
            toast_long("[FLAVOR "+BuildConfig.FLAVOR+"][BUILD_TYPE "+BuildConfig.BUILD_TYPE+"] - SANDBOX NOT IMPLEMENTED");
        }
    }
    private static boolean is_sandBox_container(View view) { return      SandBox.is_sandBox_container( view ); }
    private void           hideSandBox()                   { SandBox.hideSandBox(); is_sandBox_showing = false; sync_np("hideSandBox"); }

    // }}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}

}
/* // {{{

:let @p="ANIM"
:let @p="BAND"
:let @p="CLAMP"
:let @p="COMM"
:let @p="COOKIE"
:let @p="DATA"
:let @p="DIALOG"
:let @p="EV0_RT_DP"
:let @p="EV1_RT_IN"
:let @p="EV2_RT_CB"
:let @p="EV1_RT_OK"
:let @p="FS_SEARCH"
:let @p="FULLSCREEN"
:let @p="GLOW"
:let @p="GUI"
:let @p="HANDLE"
:let @p="JAVASCRIPT"
:let @p="KEYBOARD"
:let @p="MARKER"
:let @p="MOVE"
:let @p="POLL"
:let @p="PROFILE"
:let @p="SCALE"
:let @p="SETTINGS"
:let @p="SOUND"
:let @p="TAB"
:let @p="TOUCH"
:let @p="WEBVIEW"
:let @p="WVTOOLS"

:let @p="\\(CART\\|COMM\\|CONNECT\\|PROFILE\\|READ\\|SETTINGS\\)"
:let @p="\\(CLAMP\\|WEBVIEW\\)"
:let @p="\\(DIALOG\\|GUI\\|SOUND\\|TOUCH\\)"
:let @p="\\(FULLSCREEN\\|WEBVIEW\\|WVTOOLS\\)"
:let @p="\\(GLOW\\|WEBVIEW\\)"
:let @p="\\(HANDLE\\|SOUND\\)"
:let @p="\\(WEBVIEW\\|WVTOOLS\\)"
:let @p="JAVASCRIPT"
:let @p="EV\\d_\\w\\+"

:let @p="\\w\\+"

" activated
  :g/^\/\*p\w*\*\/.*p/t$
" ......... -> COMMENT
  :g/^\/\*p\w*\*\/.*p/s/^/\//

" commented
  :g/^\/\/\*p\w*\*\/.*p/t$
" ..........-> ACTIVATE
  :g/^\/\/\*p\w*\*\/.*p/s/^\///

// $LOCAL/USR/ivan/VIM/SYNTAX/java.vim
:!start explorer "http://remotetabs.com/dev/1.html"
:!start explorer "http://www.desmoulins.fr/index_us.php?pg=scripts\!online\!asciiart"
:!start explorer "http://patorjk.com/software/taag/\#p=display&f=Doh&t=Marker"
*/ // }}}

