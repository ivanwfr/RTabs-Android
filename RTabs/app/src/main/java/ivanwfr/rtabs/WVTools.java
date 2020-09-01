package ivanwfr.rtabs; // {{{

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
//port android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Property;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
//port android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

// }}}
// Comment {{{


// }}}
public class WVTools implements Settings.ClampListener
{
    /** VAR */
    private static       String WVTools_tag    = "(200901:17h:31)";
    //{{{
    // MONITOR TAGS {{{
    private static       String TAG_EV0_WV_DP  = Settings.TAG_EV0_WV_DP;
    private static       String TAG_EV1_WV_IN  = Settings.TAG_EV1_WV_IN;
    private static       String TAG_EV1_WV_OK  = Settings.TAG_EV1_WV_OK;
    private static       String TAG_EV2_WV_CB  = Settings.TAG_EV2_WV_CB;
    private static       String TAG_EV3_WV_SC  = Settings.TAG_EV3_WV_SC;
    private static       String TAG_EV7_WV_FL  = Settings.TAG_EV7_WV_FL;

    private static       String TAG_CLAMP      = Settings.TAG_CLAMP;
    private static       String TAG_COOKIE     = Settings.TAG_COOKIE;
    private static       String TAG_EXPAND     = Settings.TAG_EXPAND;
    private static       String TAG_FS_SEARCH  = Settings.TAG_FS_SEARCH;
    private static       String TAG_GUI        = Settings.TAG_GUI;
    private static       String TAG_JAVASCRIPT = Settings.TAG_JAVASCRIPT;
    private static       String TAG_MAGNET     = Settings.TAG_MAGNET;
    private static       String TAG_MK0_MARK   = Settings.TAG_MK0_MARK;
    private static       String TAG_MK1_COOK   = Settings.TAG_MK1_COOK;
    private static       String TAG_MK2_SYNC   = Settings.TAG_MK2_SYNC;
    private static       String TAG_MK3_PIN    = Settings.TAG_MK3_PIN;
    private static       String TAG_MK4_CB     = Settings.TAG_MK4_CB;
    private static       String TAG_MK5_FLOAT  = Settings.TAG_MK5_FLOAT;
    private static       String TAG_MK6_MAP    = Settings.TAG_MK6_MAP;
    private static       String TAG_MK7_MAGNET = Settings.TAG_MK7_MAGNET;
    private static       String TAG_PROPERTY   = Settings.TAG_PROPERTY;
    private static       String TAG_SCROLLBAR  = Settings.TAG_SCROLLBAR;
    private static       String TAG_SPREAD     = Settings.TAG_SPREAD;

    //}}}
    // ANIMATION DELAY {{{
    private static final int SB_MAGNETIZE_TRACK_DELAY           =  250;
    private static final int SB_MAGNETIZE_ANIM_DURATION         = 1000;

  //private static final int MARKER_SPREAD_HOLD_DELAY           = 2000; // BEFORE MAGNIFICATION FULLSCREEN
  //private static final int MARKER_MAGNETIZE_CLAMP_DELAY       = 1000;
  //private static final int MARKER_MAGNETIZE_DECLUTTER_DELAY   =  250;
  //private static final int MARKER_MAGNETIZE_RECHECK_DELAY     =  500;
    private static final int MARKER_MAGNETIZE_TRACK_DELAY       =  250; // MAGNETIZING AUTO LAYOUT LAG

    private static final int MARKER_REDISPLAY_DELAY             = 1000; // WEBVIEW SYNC BURST
    private static final int MARKER_SYNC_LONG_DELAY             =  500; // WEBVIEW SYNC BURST
    private static final int MARKER_SYNC_SHORT_DELAY            =  250; // WEBVIEW SYNC BURST

    private static final int SWING_MAGNET_TRACK_DURATION        =  250;
    private static final int VISUALIZE_DRAG_RECT_DURATION       =  250;


    //}}}
    //{{{
    public  static WVTools   WVTools_instance;
    public         RTabs     mRTabs    = null;

    public  static final LinkedHashMap<String, Object> TOOL_Map = new LinkedHashMap<>();
    public  static final LinkedHashMap<String, Object> JSNP_Map = new LinkedHashMap<>();

    //}}}
    //}}}
    /** WV_TOOL */
    // Constructor {{{
    public WVTools(RTabs mRTabs)
    {
        WVTools_instance = this;
        this.mRTabs      = mRTabs;
        // [TOOL_Map] & [JSNP_Map] {{{
        create_TOOL_Map();
        create_JSNP_Map();

        //}}}
        // [SCROLLBARS] {{{
        String name, text;
        //   = Settings.FS_SCROLL_TEXT  + NotePane.INFO_SEP+"\n"+Settings.FS_SCROLL_INFO;
        text = "";
        name = WV_TOOL_SB  ; sb  = create_sb(name, text);
        name = WV_TOOL_SB2 ; sb2 = create_sb(name, text);
        name = WV_TOOL_SB3 ; sb3 = create_sb(name, text);

        //}}}
        // [sbX_np] {{{
        text = Settings.SYMBOL_WHITE_CIRCLE;
        //............................name, type, x, y, w, h, z,  tag, text, bg_color, shape, tt);
        sbX_np = NotePane.GetInstance(name,   "", 0, 0, 1, 1, 0, "sb", text,        0,    "", "");

        TOOL_Map.put("sb", sbX_np); // .. check_neighbors_with_cell_size
        JSNP_Map.put("sb", sbX_np); // .. check_neighbors_with_cell_size
        //}}}
    }
    //}}}
    /** ACTIVITY */
    // {{{
    // (REPARENTING REPLACED WITH ENCAPSULATION IN [wvcontainer])
    // set_typeface {{{
    public  void set_typeface(Typeface typeface)
    {
        if(      sb  != null)       sb .setTypeface( typeface );
        if(      sb2 != null)       sb2.setTypeface( typeface );
        if(      sb3 != null)       sb3.setTypeface( typeface );
        if(fs_search != null) fs_search.setTypeface( typeface );

        _set_typeface(typeface, TOOL_Map);
        _set_typeface(typeface, JSNP_Map);
        _set_typeface(typeface, MARK_Map);
    }
    //}}}
    // _set_typeface {{{
    private void _set_typeface(Typeface typeface, LinkedHashMap<String, Object> hashMap)
    {
        for(Map.Entry<String, Object> entry : hashMap.entrySet())
        {
            if( !(entry.getValue() instanceof NotePane) ) continue;
            NotePane np = (NotePane)entry.getValue(); if(np == sbX_np) continue;
            if(np.button != null)
                np.button.setTypeface( typeface );
        }
    }
    //}}}
    // DELEGATION {{{
    private void    activity_pulse_np_button(NpButton nb                           ) {        mRTabs.pulse_np_button ( nb        ); }
    private void    activity_set_builtin_nb_OnTouchListener(NpButton nb            ) { nb.setOnTouchListener(  RTabs.builtin_nb_OnTouchListener ); }

    private void    activity_play_sound_click(String caller                        ) {        mRTabs.play_sound_click(caller); }
    private void    activity_play_sound_ding (String caller                        ) {        mRTabs.play_sound_ding (caller); }

    private void    activity_anim_free_fs_webView(String caller                    ) {        mRTabs.anim_free_fs_webView(caller); }
    private void    activity_anim_grab_fs_webView(String caller                    ) {        mRTabs.anim_grab_fs_webView(caller); }

    private int     activity_get_fs_webView_count(                                 ) { return mRTabs.get_fs_webView_count(); }

    private void    activity_blink_rect(Rect r, int color, int delay, int duration, String caller) { mRTabs.blink_rect(r, color, delay, duration, caller); }
    private void    activity_blink_rect_hide(                                                    ) { mRTabs.blink_rect_hide(); }

    private void    activity_schedule_sb_maxed_cooldown(NpButton sbX, long delay   ) {        mRTabs.schedule_sb_maxed_cooldown(sbX, delay); }
    private void    activity_clear_sb_maxed_cooldown   (NpButton sbX               ) {        mRTabs.clear_sb_maxed_cooldown   (sbX       ); }
    private boolean activity_has_sb_on_cooldown        (NpButton sbX               ) { return mRTabs.has_sb_on_cooldown        (sbX       ); }

    //}}}
    //}}}
    /** CREATE */
    // {{{
    // container_addView {{{
    private void container_addView(NpButton np_button)
    {
        ViewGroup parent = (ViewGroup)np_button.getParent();
        if(parent == null) {
            mRTabs.wvContainer.addView( np_button );
        }
        else {
            if(parent != mRTabs.wvContainer) {
                parent.removeView( np_button );
                mRTabs.wvContainer.addView( np_button );
            }
        }
    }
    //}}}
    // [fs_search] [sbX] {{{

    public  static final String  WV_TOOL_SB  = "sb";
    public  static final String  WV_TOOL_SB2 = "sb2";
    public  static final String  WV_TOOL_SB3 = "sb3";

    public  static     NpButton sb ;
    public  static     NpButton sb2;
    public  static     NpButton sb3;
    private static     NotePane sbX_np;

    private static     NotePane fs_search_np;
    public  static     NpButton fs_search;

    //}}}
    // SYMBOLS {{{
    private static       String PROPERTY_CHECKED_SYMBOL   = "\n"+Settings.SYMBOL_CHECK_MARK;

    private static       String CHECK_FLAG_SYMBOL         = Settings.SYMBOL_FLAG;
    private static       String CHECK_LOCK_SYMBOL         = Settings.SYMBOL_PLAY_PAUSE;

    //}}}
    // COLORS {{{
    private static final    int   FLAG_BG_COLOR = Color.YELLOW ;
    private static final    int   LOCK_BG_COLOR = Color.MAGENTA;
    private static final    int   TOOL_BG_COLOR = Color.WHITE  ;

    private static          int[] MARKER_COLORS = Settings.COLORS_ECC;

    //}}}
    // TOOL {{{
    // NAMES & TEXT {{{
    private static final String  WV_TOOL_fs_search          = "fs_search";

    //.........................  (javascript)
    public  static final String  WV_TOOL_JS0_LOGGING        = "JS_LOGGING";
    public  static final String  WV_TOOL_JS0_LOGGING_text   = "JS_LOGGING" + NotePane.INFO_SEP + "Javascript logging";
    public  static final String  WV_TOOL_JS1_SELECT         = "JS_SELECT";
    public  static final String  WV_TOOL_JS1_SELECT_text    = "JS_SELECT"  + NotePane.INFO_SEP + "WebView JS Selection";
    public  static final String  WV_TOOL_JS2_DOM_LOAD       = "dom_load";
    public  static final String  WV_TOOL_JS2_DOM_LOAD_text  = "DOM_LOAD"   + NotePane.INFO_SEP + "Load DOM Tools";
    public  static final String  WV_TOOL_JS3_black          = "black";

    //.........................  (properties)
    private static final String  WV_TOOL_track              = "sb-track";
    private static final String  WV_TOOL_silent             = "silent";
    public  static final String  WV_TOOL_grab               = "grab";
    public  static final String  WV_TOOL_zoom               = "zoom";
    public  static       String  WV_TOOL_zoom_text          = WV_TOOL_zoom + NotePane.INFO_SEP+"Toggles WebSettings.setSupportZoom()";

    //.........................  (markers)
    private static final String  WV_TOOL_mark               = "mark";
    private static final String  WV_TOOL_markV              = "markV";
    private static       String  WV_TOOL_mark_text          = Settings.SYMBOL_CHECK_HEAVY;
    private static final String  WV_TOOL_cut_mark           = "cut_mark";
    private static       String  WV_TOOL_cut_mark_free      = Settings.SYMBOL_WASTEBASKET;
    private static       String  WV_TOOL_cut_mark_full      = WV_TOOL_cut_mark_free + Settings.SYMBOL_WHITE_CIRCLE;

    //.........................  (cookies)
    private static final String  WV_TOOL_DEL_COOKIES        = "DEL_COOKIES";
    private static final String  WV_TOOL_SAVE_COOKIES       = "SAVE_COOKIES";
    private static final String  WV_TOOL_LOAD_COOKIES       = "LOAD_COOKIES";

    //.........................  (tabs)
    private static final String  WV_TOOL_GUI_STYLE          = "GUI_STYLE";
    private static final String  WV_TOOL_PLNEXT             = "PLNEXT";
    private static final String  WV_TOOL_PLPREV             = "PLPREV";

    private static final String  WV_TOOL_PALETTE            = "PALETTE";
    private static final String  WV_TOOL_TOOL_URL           = "TOOL_URL";

    private static final String  WV_TOOL_STATUS             = "STATUS";
    private static final String  WV_TOOL_FINISH             = "FINISH";
    private static final String  WV_TOOL_PROFILE_SAVE       = "PROFILE_SAVE";

    private static final String  WV_TOOL_grow4              = "PROPERTY "+RTabs.PROPERTY_MARK_SCALE_GROW+" 4";
    private static final String  WV_TOOL_grow4_text         = "Mark grow 4";
    private static final String  WV_TOOL_grow5              = "PROPERTY "+RTabs.PROPERTY_MARK_SCALE_GROW+" 5";
    private static final String  WV_TOOL_grow5_text         = "Mark grow 5";
    private static final String  WV_TOOL_grow6              = "PROPERTY "+RTabs.PROPERTY_MARK_SCALE_GROW+" 6";
    private static final String  WV_TOOL_grow6_text         = "Mark grow 6";

    //.........................  (place holders)
    private static final String  WV_TOOL_empty              = "_";

    //.........................  (FLAG)
    public  static final String  WV_TOOL_FLAG_ADJUSTED      = CHECK_FLAG_SYMBOL +"\nADJUSTED";
    public  static final String  WV_TOOL_FLAG_COLORED       = CHECK_FLAG_SYMBOL +"\nCOLORED" ;
    public  static final String  WV_TOOL_FLAG_LAYOUT        = CHECK_FLAG_SYMBOL +"\nLAYOUT\n(markers)";
    public  static final String  WV_TOOL_FLAG_MEASURED      = CHECK_FLAG_SYMBOL +"\nMEASURED";
    public  static final String  WV_TOOL_FLAG_PAGE_FIN      = CHECK_FLAG_SYMBOL +"\nPAGE_FIN";
    public  static final String  WV_TOOL_FLAG_RESIZED       = CHECK_FLAG_SYMBOL +"\nRESIZED" ;
    public  static final String  WV_TOOL_FLAG_SCROLLED      = CHECK_FLAG_SYMBOL +"\nSCROLLED";
    public  static final String  WV_TOOL_FLAG_SYNC          = CHECK_FLAG_SYMBOL +"\nSYNC"    ;
    public  static final String  WV_TOOL_FLAG_TOOLS         = CHECK_FLAG_SYMBOL +"\nTOOLS"   ;

    public  static final String  WV_TOOL_FLAG_WEIGHT        = "WEIGHT";
    public  static final String  WV_TOOL_FLAG_SCALE         = "SCALE";
    public  static final String  WV_TOOL_FLAG_X             = "X";
    public  static final String  WV_TOOL_FLAG_Y             = "Y";
    public  static final String  WV_TOOL_FLAG_CENTER        = "CENTER";
    public  static final String  WV_TOOL_FLAG_5             = "5";
    public  static final String  WV_TOOL_FLAG_6             = "6";
    public  static final String  WV_TOOL_FLAG_7             = "7";
    public  static final String  WV_TOOL_FLAG_8             = "8";
    public  static final String  WV_TOOL_FLAG_9             = "9";


    //.........................  (DO)
    private static final String  WV_TOOL_MARK_LOCK          = "NO_MARKERS";
    private static final String  WV_TOOL_MARK_LOCK_text     = "No markers"    + NotePane.INFO_SEP + "Do not show markers";

    public  static final String  WV_TOOL_MDRAG_LOCK         = "MOVING_MARKERS";
    public  static final String  WV_TOOL_MDRAG_LOCK_text    = "Moving markers"+ NotePane.INFO_SEP + "May move markers around";

    public  static final String  WV_TOOL_finish             = "FINISH";
    public  static final String  WV_TOOL_finish_text        = "Finish"        + NotePane.INFO_SEP + "exit application";

    public  static final String  WV_TOOL_log_cat            = "LOG_CAT";
    public  static final String  WV_TOOL_log_cat_text       = "LOG_CAT"       + NotePane.INFO_SEP + "ADB LOG FILTER";

    public  static final String  WV_TOOL_save               = "SAVE";
    public  static final String  WV_TOOL_save_text          = "Save"          + NotePane.INFO_SEP + "save current parameters";

    public  static final String  WV_TOOL_memory             = "MEMORY";
    public  static final String  WV_TOOL_memory_text        = "MEMORY"         + NotePane.INFO_SEP + "calls handle_MEMORY";

    public  static final String  WV_TOOL_command            = "COMMAND";
    public  static final String  WV_TOOL_command_text       = "COMMAND"        + NotePane.INFO_SEP + "calls handle_COMMAND";

    //}}}
    private static String[]   WV_TOOL_ON_BY_DEFAULT
        = {   WV_TOOL_MARK_LOCK
            , WV_TOOL_zoom
        };

    private static String[][] WV_TOOL_NAME_TEXT
        = {
            //.......................  (fs_search)
            { WV_TOOL_fs_search    , Settings.FS_SEARCH_TEXT_3 + NotePane.INFO_SEP+"\n"+Settings.FS_SEARCH_INFO } // (BUILTIN TOOL    )

            //.......................  (javascript)
            , { WV_TOOL_JS0_LOGGING  , WV_TOOL_JS0_LOGGING_text     }
            , { WV_TOOL_JS1_SELECT   , WV_TOOL_JS1_SELECT_text      }
            , { WV_TOOL_JS2_DOM_LOAD , WV_TOOL_JS2_DOM_LOAD_text    }
            , { WV_TOOL_JS3_black    , WV_TOOL_JS3_black            }

            //.......................  (SCROLLBAR)
            , { WV_TOOL_track        , WV_TOOL_track                }

            //.......................  (TOOLS)
            , { WV_TOOL_silent       , WV_TOOL_silent               }
            , { WV_TOOL_grab         , WV_TOOL_grab                 }
            , { WV_TOOL_zoom         , WV_TOOL_zoom_text            }

            //.......................  (app)
            , { WV_TOOL_finish       , WV_TOOL_finish_text          }
            , { WV_TOOL_log_cat      , WV_TOOL_log_cat_text         }
            , { WV_TOOL_save         , WV_TOOL_save_text            }
            , { WV_TOOL_memory       , WV_TOOL_memory_text          }

            , { WV_TOOL_grow4        , WV_TOOL_grow4_text           }
            , { WV_TOOL_grow5        , WV_TOOL_grow5_text           }
            , { WV_TOOL_grow6        , WV_TOOL_grow6_text           }

            //.......................  (markers)
            , { WV_TOOL_mark         , WV_TOOL_mark_text            }
            , { WV_TOOL_cut_mark     , WV_TOOL_cut_mark_free        }

            //.......................  (locks)
            , { WV_TOOL_MARK_LOCK    , WV_TOOL_MARK_LOCK_text       }
            , { WV_TOOL_MDRAG_LOCK   , WV_TOOL_MDRAG_LOCK_text      }

            //.......................  (flags)
            , { WV_TOOL_FLAG_ADJUSTED, WV_TOOL_FLAG_ADJUSTED        }
            , { WV_TOOL_FLAG_COLORED , WV_TOOL_FLAG_COLORED         }
            , { WV_TOOL_FLAG_LAYOUT  , WV_TOOL_FLAG_LAYOUT          }
            , { WV_TOOL_FLAG_MEASURED, WV_TOOL_FLAG_MEASURED        }
            , { WV_TOOL_FLAG_PAGE_FIN, WV_TOOL_FLAG_PAGE_FIN        }
            , { WV_TOOL_FLAG_RESIZED , WV_TOOL_FLAG_RESIZED         }
            , { WV_TOOL_FLAG_SCROLLED, WV_TOOL_FLAG_SCROLLED        }
            , { WV_TOOL_FLAG_SYNC    , WV_TOOL_FLAG_SYNC            }
            , { WV_TOOL_FLAG_TOOLS   , WV_TOOL_FLAG_TOOLS           }

            , { WV_TOOL_FLAG_WEIGHT  , WV_TOOL_FLAG_WEIGHT          }
            , { WV_TOOL_FLAG_SCALE   , WV_TOOL_FLAG_SCALE           }
            , { WV_TOOL_FLAG_X       , WV_TOOL_FLAG_X               }
            , { WV_TOOL_FLAG_Y       , WV_TOOL_FLAG_Y               }
            , { WV_TOOL_FLAG_CENTER  , WV_TOOL_FLAG_CENTER          }
            , { WV_TOOL_FLAG_5       , WV_TOOL_FLAG_5               }
            , { WV_TOOL_FLAG_6       , WV_TOOL_FLAG_6               }
            , { WV_TOOL_FLAG_7       , WV_TOOL_FLAG_7               }
            , { WV_TOOL_FLAG_8       , WV_TOOL_FLAG_8               }
            , { WV_TOOL_FLAG_9       , WV_TOOL_FLAG_9               }

            //.......................  (tabs)
            , { WV_TOOL_DEL_COOKIES  , WV_TOOL_DEL_COOKIES          }
            , { WV_TOOL_SAVE_COOKIES , WV_TOOL_SAVE_COOKIES         }
        //  , { WV_TOOL_GUI_STYLE    , WV_TOOL_GUI_STYLE            }
        //  , { WV_TOOL_PLPREV       , WV_TOOL_PLPREV               }
        //  , { WV_TOOL_PLNEXT       , WV_TOOL_PLNEXT               }
        //  , { WV_TOOL_PALETTE      , WV_TOOL_PALETTE              }
        //  , { WV_TOOL_TOOL_URL     , WV_TOOL_TOOL_URL             }
        //  , { WV_TOOL_STATUS       , WV_TOOL_STATUS               }
        //  , { WV_TOOL_FINISH       , WV_TOOL_FINISH               }
        //  , { WV_TOOL_PROFILE_SAVE , WV_TOOL_PROFILE_SAVE         }

/*
            // [WV_TOOL_empty] .. (TEST-PLACEHOLDER) {{{
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }

            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }

            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }

            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }

            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }
            , { WV_TOOL_empty     , Settings.SYMBOL_WHITE_CIRCLE }

            //}}}
*/
        };
    //}}}
    // JSNP {{{
    // NAMES & TEXT {{{
    public  static final String  WV_JSNP_select_find         = "find";
    public  static final String  WV_JSNP_select_find_text    = Settings.SYMBOL_MAGNIFY_LEFT  + NotePane.INFO_SEP +"find / found";

    public  static final String  WV_JSNP_select_next         = "next";
    public  static final String  WV_JSNP_select_next_text    = Settings.SYMBOL_UP_DOWN_ARROW + NotePane.INFO_SEP +"next up or down";

    public  static final String  WV_JSNP_select_percent      = "percent";
    public  static final String  WV_JSNP_select_percent_text = Settings.SYMBOL_MAGNIFY_RIGHT + NotePane.INFO_SEP +"%";

  //public  static final String  WV_JSNP_select_prev         = "prev";
  //public  static final String  WV_JSNP_select_prev_text    = Settings.SYMBOL_UP_ARROW      + NotePane.INFO_SEP +"previous find";
  //public  static final String  WV_JSNP_select_next         = "next";
  //public  static final String  WV_JSNP_select_next_text    = Settings.SYMBOL_DOWN_ARROW    + NotePane.INFO_SEP +"next find";
    //}}}
    private static String[][] WV_JSNP_NAME_TEXT
        = {
            // ... (selection)
            {   WV_JSNP_select_percent, WV_JSNP_select_percent_text }
            , { WV_JSNP_select_next   , WV_JSNP_select_next_text    }
            , { WV_JSNP_select_find   , WV_JSNP_select_find_text    }

            //{ WV_JSNP_select_prev   , WV_JSNP_select_prev_text    }
            //{ WV_JSNP_select_next   , WV_JSNP_select_next_text    }
        };
    //}}}
    // create_TOOL_Map {{{
    private void create_TOOL_Map()
    {
        // [WV_TOOL_NAME_TEXT] {{{
        String caller = "create_TOOL_Map";

        String  name, text, tag, shape;
        int fg_color, bg_color;
        NotePane  np;

        for(int i=0; i < WV_TOOL_NAME_TEXT.length; ++i)
        {
            // [name] [text] [tag] [shape] [colors] {{{

            name    = WV_TOOL_NAME_TEXT[i][0];

            text    = WV_TOOL_NAME_TEXT[i][1];

            tag     = mRTabs   .is_ACTIVITY_BUILTIN( name )
                ||    Settings .is_SETTINGS_BUILTIN( name )
                ||    Profile  .is_PROFILES_BUILTIN( name )
                ?      name               // name as a link to a builtin TAG
                :      Settings.FREE_TAG; // no tag

            shape    = get_shape_for_name   ( name );

            bg_color = get_bg_color_for_name( name );
            fg_color = NotePane.NO_COLOR; // auto .. f(bg_color)

            //}}}
            // [clones] {{{
            {
                int j=1;
                while(TOOL_Map.get(name) != null)
                    name = WV_TOOL_NAME_TEXT[i][0] + (++j);         // first clone gets #2

                if(j>1) {
                    text += " "+Settings.Get_DIGIT_SYMBOL(j);       // add the one not found .. (after an effective iteration)
                    if( WVTools.is_a_marker_name( name ) )
                        bg_color = Settings.COLORS_ECC[(j) % Settings.COLORS_ECC.length];
                }
            }
            //}}}
            // [create_np] .. [TOOL_Map] {{{
            np= create_np(name, text, tag, shape, bg_color, fg_color, Settings.TOOL_BADGE_SIZE, Settings.TOOL_BADGE_SIZE);

            TOOL_Map.put(name, np);
            //}}}
            // [fs_search] .. (floating search tool) {{{
            if( name.equals( WV_TOOL_fs_search ) )
            {
                fs_search_np = np;
                fs_search    = fs_search_np.button;
                fs_search_to_sleep(fs_search, caller);
            }
            //}}}
        }
        //}}}
        property_check_defaults();
    }
    //}}}
    // property_check_defaults {{{
    public  void property_check_defaults()
    {
//*PROPERTY*/Settings.MOM(TAG_PROPERTY, "property_check_defaults");

        for(int i= 0; i < WV_TOOL_ON_BY_DEFAULT.length; ++i)
            property_set( WV_TOOL_ON_BY_DEFAULT[i], true);
    }
    //}}}
    // create_JSNP_Map {{{
    private void create_JSNP_Map()
    {
        // [WV_JSNP_NAME_TEXT] {{{
        String caller = "create_JSNP_Map";

        String  name, text, tag, shape;
        int fg_color, bg_color;
        NotePane  np;

        for(int i=0; i < WV_JSNP_NAME_TEXT.length; ++i)
        {
            // [name] [text] [tag] [shape] [colors] {{{

            name    = WV_JSNP_NAME_TEXT[i][0];

            text    = WV_JSNP_NAME_TEXT[i][1];

            tag     = mRTabs   .is_ACTIVITY_BUILTIN( name )
                ||    Settings .is_SETTINGS_BUILTIN( name )
                ||    Profile  .is_PROFILES_BUILTIN( name )
                ?      name               // name as a link to a builtin TAG
                :      Settings.FREE_TAG; // no tag

            shape    = get_shape_for_name   ( name );

            bg_color = get_bg_color_for_name( name );
            fg_color = NotePane.NO_COLOR; // auto .. f(bg_color)

            //}}}
            // [create_np] .. [JSNP_Map] {{{
            np= create_np(name, text, tag, shape, bg_color, fg_color, Settings.TOOL_BADGE_SIZE, Settings.TOOL_BADGE_SIZE);

            JSNP_Map.put(name, np);
            //}}}
        }
        //}}}
    }
    //}}}
    // create_np {{{
    private NotePane create_np(String name, String text, String tag, String shape, int bg_color, int fg_color, int w, int h)
    {
        // NpButton {{{
        NpButton np_button = new NpButton(RTabs.activity);

        // SHAPE
    //  int shape arg
        np_button.set_shape         (                    shape ,true); // with_outline

        // COLOR
        np_button.setAlpha          (Settings.FS_SCROLL_OPACITY     );

        if(fg_color == NotePane.NO_COLOR)
            fg_color = (ColorPalette.GetBrightness( bg_color ) < 128) ? Color.WHITE : Color.BLACK;

        np_button.setBackgroundColor( bg_color );
        np_button.setTextColor      ( fg_color );

        // TEXT
        np_button.setText           (Settings.FS_SELECT_TEXT);
        np_button.setEllipsize      ( TextUtils.TruncateAt.END      );
        np_button.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL); // override NpButton.setText
    //  np_button.setTextColor...
        np_button.setTypeface       ( Settings.getTypeface()        );

        // GEOMETRY
        np_button.lockElevation     ( Settings.WV_TOOL_ELEVATION    ); // just above WEBVIEW buttons
        np_button.setPadding        ( 0, 0, 0, 0                    );
    //  np_button.setPivotY...
        if( Settings.SCROLLBAR_USE_LAYOUT_PARAMS ) {
            FrameLayout.LayoutParams   flp = new FrameLayout.LayoutParams(w, h);
            np_button.setLayoutParams( flp );
        } else {
            np_button.setWidth ( w );
            np_button.setHeight( h );
        }
    //  np_button.setZ              ( WSEARCH_SET_Z                 );

        // BUTTON BEHAVIOR
    //  np_button.setTag            (Settings.FS_SELECT_INFO);
        activity_set_builtin_nb_OnTouchListener( np_button );

        // BUTTON DISPLAY
        np_button.setVisibility( View.GONE );

        container_addView( np_button );
        //}}}
        // NotePane {{{
        String tt    = "";
        String type  = "";
        int    x     =  0;
        int    y     =  0;
        float  z     =  0;

    //  NotePane np = new         NotePane(name, type, x, y, w, h, z, tag, text, bg_color, shape, tt);
        NotePane np = NotePane.GetInstance(name, type, x, y, w, h, z, tag, text, bg_color, shape, tt);
        np.bg_color = bg_color;
        np.fg_color = fg_color;

        np.set_button( np_button );
        np.button.setEllipsize      ( TextUtils.TruncateAt.END );

        np.button.setTag( np ); // .. (NotePane<=NpButton backward link)

        //}}}

        return np;
    }
    //}}}
    // create_sb {{{
    //{{{
    public  static final    int   FS_SCROLL_BACKCOLOR       = Color.WHITE;
//  private static final    int   FS_SCROLL_TEXTCOLOR       = Color.RED;

    //}}}
    private NpButton create_sb(String name, String text)
    {
        // NpButton {{{
        NpButton np_button      = new NpButton(RTabs.activity);

        // SHAPE
        String shape = NotePane.SHAPE_TAG_SCROLL;
        np_button.set_shape         (                    shape ,true); // with_outline

        // COLOR
    //  np_button.setAlpha          (Settings.FS_SCROLL_OPACITY     );
        int bg_color = FS_SCROLL_BACKCOLOR;

        np_button.setBackgroundColor( bg_color                      );
    //  np_button.setTextColor...

        // TEXT
    //  np_button.setText           (text); // keep it blank
        np_button.setEllipsize      ( TextUtils.TruncateAt.END      );
        np_button.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL); // override NpButton.setText
    //  np_button.setTextColor      ( FS_SCROLL_TEXTCOLOR           );
    //  np_button.setTypeface       ( Settings.getTypeface()        );
        np_button.setTypeface       ( Typeface.DEFAULT              );

        // GEOMETRY
        np_button.lockElevation     ( Settings.FS_SCROLL_ELEVATION  ); // just above select tools
        np_button.setPadding        ( 0, 0, 0, 0                    );
        np_button.setPivotY         ( 0                             ); // scrollbar top
        if( Settings.SCROLLBAR_USE_LAYOUT_PARAMS ) {
            FrameLayout.LayoutParams  flp = new FrameLayout.LayoutParams(Settings.SCROLLBAR_W_MIN, Settings.SCROLLBAR_H_MIN);
            np_button.setLayoutParams(flp);
        } else {
            np_button.setWidth ( Settings.SCROLLBAR_W_MIN );
            np_button.setHeight( Settings.SCROLLBAR_H_MIN );
        }
    //  np_button.setZ              ( WSEARCH_SET_Z                 );

        // SCROLLBAR BEHAVIOR
        np_button.setTag            (Settings.FS_SCROLL_INFO);
        activity_set_builtin_nb_OnTouchListener( np_button );

        // SCROLLBAR DISPLAY
        np_button.setVisibility( View.GONE );

        container_addView( np_button );
        //}}}
        return np_button;
    }
    //}}}
    // get_hashMap_name {{{
    private String get_hashMap_name(LinkedHashMap<String, Object> hashMap)
    {
        return (hashMap == TOOL_Map) ? "TOOL_Map"
            :  (hashMap == JSNP_Map) ? "JSNP_Map"
            :                          null
            ;
    }
    //}}}
    // }}}
    /** VIEW */
    // {{{
    //* VIEW (showing) */
    // is_view_showing {{{
    public boolean is_view_showing(View view)
    {
//if(view != null) System.err.println("XXXXXXXXXXXXXXXXXXXX is_view_showing("+view+".getVisibility()=["+view.getVisibility()+"])");
        return (view != null) && (view.getVisibility() == View.VISIBLE);
    }
//}}}
    //* VIEW (name) */
    // get_view_name {{{
    public  String get_view_name(View view)
    {
        // search all known classes views
        String                                  name =    this._get_view_name( view ); // call ( this instance) built-in namer
        if((name == null) && (mRTabs  != null)) name =  mRTabs._get_view_name( view ); // call (RTabs instance) built-in namer
        if( name == null)                       name = view.toString();
        return                                  name.replace("\n", " ");
    }
    //}}}
    // _get_view_name {{{
    public  String _get_view_name(View view)
    {
        // search this class views only
        String name = null;
        if     (view ==         sb       ) name =       "sb" ;
        else if(view ==         sb2      ) name =       "sb2";
        else if(view ==         sb3      ) name =       "sb3";
        else if(view ==         fs_search) name = "fs_search";
        else if(view instanceof NpButton ) name = Settings.get_NpButton_name( (NpButton)view );

//GUI//Settings.MOM(TAG_GUI, "get_view_name("+view+"): ...return ["+name+"]");
        return name;
    }
    //}}}
    //* VIEW (hit-rectangle) */
    // [hr] {{{
    Rect hr = new Rect();

    //}}}
    // is_view_atXY {{{
    public  boolean is_view_atXY(View view, int x, int y)
    {
        if( !is_view_showing(view) ) return false;
        view.getHitRect( hr );
        return hr.contains(x, y);
    }
    //}}}
    // is_view_atY {{{
    public  boolean is_view_atY(View view, int y)
    {
        if( !is_view_showing(view) ) return false;
        view.getHitRect( hr );
        return hr.top == y;
    }
    //}}}
    // is_sb_atXY {{{
    private boolean is_sb_atXY(NpButton sbX, int x, int y)
    {
        if(sbX == null) return false;

        sbX.getHitRect( hr );

        return hr.contains(x, y);
    }
    //}}}
    // is_sb_middle_atXY {{{
    private boolean is_sb_middle_atXY(NpButton sbX, int x, int y)
    {
        if(sbX == null) return false;

        sbX.getHitRect( hr );
        int w     = hr.right - hr.left;
        int h     = hr.bottom- hr.top ;

        // squeeze height near middle
        int mid   = (hr.top + hr.bottom) / 2;
        hr.top    = mid - Settings.SCALED_TOUCH_SLOP;
        hr.bottom = mid + Settings.SCALED_TOUCH_SLOP;

        return hr.contains(x, y);
    }
    //}}}
    // is_sb_aroundXY {{{
    private boolean is_sb_aroundXY(NpButton sbX, int x, int y)
    {
        if( !is_view_showing(sbX) ) return false;

        // remove inner margins .. (substract border_in)
        sbX.getHitRect( hr );
        get_sb_hotRect(sbX, hr);

        // within inner margins
        if( hr.contains(x, y) )
        {
            sbX.action_down_in_margin = false;
            return true;
        }

        // add outer margins
        sbX.getHitRect( hr );
        get_sbX_expandHitRect(sbX, hr);

        // within outer margins
        if( hr.contains(x, y) )
        {
            sbX.action_down_in_margin = true;
          //activity_pulse_np_button( sbX );
            return true;
        }
        // outside margins
        else {
            return false;
        }
    }
    //}}}
    // is_marker_nearXY {{{
    private boolean is_marker_nearXY(NotePane marker_np       , int x, int y)
    {
        return (marker_np != null) && is_marker_nearXY(marker_np.button, x, y);
    }
    private boolean is_marker_nearXY(NpButton marker_np_button, int x, int y)
    {
        if( !is_view_showing(marker_np_button) ) return false;

        if( Settings.SCROLLBAR_USE_LAYOUT_PARAMS )
        {
            if( Settings.SCROLLBAR_USE_TRANSLATION )
                marker_np_button.getHitRect( hr );
            else
                marker_np_button.getLayoutHitRect( hr ); // unscaled layout
        }

        int w     = hr.right - hr.left;
        int h     = hr.bottom- hr.top ;

        float scaleY = marker_np_button.getScaleY();

        // cancel spread markers scale
        h = (int)(h / scaleY);
        int mid   = (hr.top + hr.bottom) / 2;
        hr.top    = mid - h/2;
        hr.bottom = mid + h/2;

        // expand hit rectangle (width x1, left and right) .. (height x1/8, up and down)

        // width
    //  int dx   =   0; // strict width
    //  int dx   = w/8; // small horizontal magnetisms
    //  int dx   = w  ; // ...more
        int dx   = w*2; // ...more!

        // height
        int dy   =   0; // strict height
    //  int dy   = h/8; // stick with current
    //  int dy   = h/2; // ...more

        //_____________________________________________ //
        /*__________*/ hr.top    -= dy; //_____________ //
        hr.left -= dx;                  hr.right += dx; //
        /*__________*/ hr.bottom += dy; //_____________ //
        //_____________________________________________ //

        return hr.contains(x, y);
    }
    //}}}
    // get_view_at_XY {{{
    public  View get_view_at_XY(int x, int y)
    {
        View view;
        view = get_fs_search_at_XY(x, y); if(view != null) return view;
        view = get_tool_at_XY     (x, y); if(view != null) return view;
        view = get_sb_at_XY       (x, y); if(view != null) return view;

        NotePane marker_np = marker_get_np_at_XY(x, y); if(marker_np != null) return marker_np.button;

        return null;
    }
    //}}}
    // get_hr_wv_at_XY {{{
    private RTabs.MWebView get_hr_wv_at_XY(int x, int y)
    {
//*GUI*/String caller = "get_hr_wv_at_XY("+x+","+y+")";//TAG_GUI
//*GUI*/Settings.MOM(TAG_GUI, caller);
//*GUI*/Settings.MOM(TAG_GUI, "is_view_showing( fs_webView  )=["+is_view_showing( mRTabs.fs_webView )+"]");
//*GUI*/Settings.MOM(TAG_GUI, "is_view_showing( fs_webView2 )=["+is_view_showing( mRTabs.fs_webView2)+"]");
//*GUI*/Settings.MOM(TAG_GUI, "is_view_showing( fs_webView3 )=["+is_view_showing( mRTabs.fs_webView3)+"]");

        RTabs.MWebView wv = mRTabs.fs_webView ; if( mRTabs.is_fs_webViewX_in_screen( wv )) { wv.getHitRect( hr ); if( hr.contains(x, y) ) return wv; }
        /*..........*/ wv = mRTabs.fs_webView2; if( mRTabs.is_fs_webViewX_in_screen( wv )) { wv.getHitRect( hr ); if( hr.contains(x, y) ) return wv; }
        /*..........*/ wv = mRTabs.fs_webView3; if( mRTabs.is_fs_webViewX_in_screen( wv )) { wv.getHitRect( hr ); if( hr.contains(x, y) ) return wv; }
        return null;
    }
//}}}
    // get_hr_wv_at_XY_hidden_or_not {{{
    private RTabs.MWebView get_hr_wv_at_XY_hidden_or_not(int x, int y)
    {
        RTabs.MWebView wv = mRTabs.fs_webView ; if( wv != null           ) { wv.getHitRect( hr ); if( hr.contains(x, y) ) return wv; }
        /*..........*/ wv = mRTabs.fs_webView2; if( wv != null           ) { wv.getHitRect( hr ); if( hr.contains(x, y) ) return wv; }
        /*..........*/ wv = mRTabs.fs_webView3; if( wv != null           ) { wv.getHitRect( hr ); if( hr.contains(x, y) ) return wv; }
        return null;
    }
//}}}
    // get_fs_search_at_XY {{{
    private  View get_fs_search_at_XY(int x, int y)
    {
        if( is_view_atXY(fs_search,x,y) ) return fs_search;
        else                              return null;
    }
    //}}}
    // visualize_SEEK_rect {{{
    private void visualize_SEEK_rect(int x, int y, String caller)
    {
        // IN-MARGIN LOOKUP-RECTANGLE
        // [XY not in the markers column] .. (return) {{{
        if((marker_expand_x_min >= x) || (x >= marker_expand_x_max)) return; // not in the markers column

//*EXPAND*/Settings.MOC(TAG_EXPAND, caller+"->visualize_SEEK_rect");
        //}}}
        // [activity_blink_rect] {{{
    //  int    w = marker_expand_x_max -marker_expand_x_min;
        hr.   top = y - Settings.TAB_MARK_H/2;//w/2; // see is_marker_nearXY
        hr.  left = marker_expand_x_min;
        hr. right = marker_expand_x_max;
        hr.bottom = y + Settings.TAB_MARK_H/2;//w/2; // see is_marker_nearXY

        // VISUALIZE CAPTURING HIT RECTANGLE
        activity_blink_rect(hr, (Color.RED  ) & 0x80FFFFFF, 0, 0, caller); // delay, duration

        //}}}
    }
    //}}}
    // visualize_SLIDE_rect .. (MARKER-TO-MARGIN) {{{
    private void visualize_SLIDE_rect(NotePane marker_np, String caller)
    {
        // MARGIN TO MARKER {{{
//*EXPAND*/Settings.MOC(TAG_EXPAND, caller+"->visualize_SLIDE_rect");
        NpButton sbX  = get_sb_for_wv_or_sb(gesture_down_wv);
        if(      sbX == null) return;

        int w = marker_np.button.getWidth();

        boolean at_left = !is_sb_at_left();
        if(at_left) {
            hr.left  = marker_expand_x_min;
            hr.right = (int)marker_np.button.getX()     + 2;
        }
        else {
            hr.left  = (int)marker_np.button.getX() + w - 2;
            hr.right = marker_expand_x_max;
        }
        //}}}
        // [activity_blink_rect] {{{
        hr.top    = (int)sbX.getY();
        hr.bottom = hr.top + Settings.TAB_MARK_H;

        activity_blink_rect(hr, (Color.CYAN) & 0xA0FFFFFF, 0, 0, caller); // delay, duration
        //}}}
    }
    // }}}
    // visualize_DRAG_rect .. (MARKER-TO-SCROLLBAR) {{{
    private void visualize_DRAG_rect()
    {
        // FROM SCROLLBAR TO FLOATING MARKER
        // [TO  ] (SCROLLBAR THUMB) {{{
        if(gesture_down_wv == null) return;
        NpButton          sbX  = get_sb_for_wv_or_sb(gesture_down_wv);
        if(               sbX == null) return;

        float thumb_p
            = get_wv_thumb_p(gesture_down_wv);

        int wv_y
            = (int) gesture_down_wv.getY()
            + (int)(gesture_down_wv.getHeight() * thumb_p / 100f * gesture_down_wv.getScaleY());

        //}}}
        // [FROM] (FLOATING-MARKER THUMB) {{{
        int thumb_y
            = (floating_marker_np != null)
            ?  marker_get_np_thumb_y( floating_marker_np )
            :  wv_y;

        //}}}
        // MOVING COLOR:[BOT-GREEN] [TOP-RED] {{{
        int top, bot, col;
        if(thumb_y <= wv_y)
        {
            col = (Color.GREEN) & 0x40FFFFFF;
            top = thumb_y;
            bot = wv_y;
        }
        else {
            col = (Color.RED  ) & 0x40FFFFFF;
            top = wv_y;
            bot = thumb_y;
        }

        boolean at_left = is_sb_at_left();
        int         lft = (int)(at_left ?             sbX.getX() +             sbX.getWidth() : gesture_down_wv.getX());
        int         rgt = (int)(at_left ? gesture_down_wv.getX() + gesture_down_wv.getWidth() :             sbX.getX());

        // FOUR PIXEL LINE
        bot = Math.max(bot, top+4);

        //}}}
        // [activity_blink_rect] {{{

        hr.   top = top;
        hr.  left = lft;
        hr. right = rgt;
        hr.bottom = bot;
//*EXPAND*/Settings.MOC(TAG_EXPAND, "visualize_DRAG_rect("+hr+")");

        activity_blink_rect(hr, col, 0, VISUALIZE_DRAG_RECT_DURATION, "visualize_DRAG_rect"); // delay, duration
        //}}}
    }
    //}}}
    // visualize_MARGIN_rect {{{
    private void visualize_MARGIN_rect(RTabs.MWebView wv, String margin_URDL)
    {
        // MARGIN RECTANGLE
        // (gesture_down_wv) {{{
        if(gesture_down_wv == null) return;
        if(margin_URDL     == null) return;

        //}}}
        // (MARGIN_L) (MARGIN_R) {{{
        int wv_x = (int) wv.getX();
        int wv_y = (int) wv.getY();
        int wv_w = (int)(wv.getWidth () * wv.getScaleX());
        int wv_h = (int)(wv.getHeight() * wv.getScaleY());

        switch(margin_URDL)
        {
            case MARGIN_L:
                hr.left  =            wv_x;
                hr.right = hr.left  + Settings.TOOL_BADGE_SIZE;
                break;

            case MARGIN_R:
                hr.right = wv_x     + wv_w;
                hr.left  = hr.right - Settings.TOOL_BADGE_SIZE;
                break;
            default:
                return;
        }
        //}}}
        // [activity_blink_rect] {{{
        hr.top    = wv_y;
        hr.bottom = wv_y + wv_h;

      //activity_blink_rect(hr, (Color.GRAY) & 0x80FFFFFF, 0, 200, "visualize_MARGIN_rect"); // delay, duration
        activity_blink_rect(hr, (Color.GRAY) & 0x80FFFFFF, 0,   0, "visualize_MARGIN_rect"); // delay, duration
        //}}}
    }
    //}}}
    // visualize_CLEAR_rect .. (CLEAR VISUALIZED RECTANGLE) {{{
    private void visualize_CLEAR_rect()
    {
        // [activity_blink_rect_hide] {{{
//*EXPAND*/Settings.MOM(TAG_EXPAND, "visualize_CLEAR_rect");

        activity_blink_rect_hide(); // erase seek area highlighting
        //}}}
    }
    //}}}
    // }}}
    /** GUI */
    //{{{

    // hide (markers) (tools) {{{
    public void hide(String caller)
    {
//*GUI*/Settings.MOC(TAG_GUI, caller+"->hide");

        // hide [TOOL_Map np] and [JSNP_Map np]
        hide_tools(caller);

        // [sb frame area] .. (clear cached tools) .. (forget about avoiding useless redraw)
        sb_layout_invalidate(caller); // after hiding tools (that wont be kept in sync with their frame when hidden)

        // [wv geometry] .. (set to be re-evaluated)
        marker_wv_sync_invalidate_layout();

        // enforce marks lock
        if( property_get_WV_TOOL_MARK_LOCK )
            marker_hide_pinned_markers(caller);
    }
    //}}}

    // gui_cycle {{{
    private String gui_cycle(String caller)
    {
/* [BEHAVIOR] {{{

 O SCROLLBAR CLICKED
 -> TOGGLE TOOLS VISIBILITY:
 =   (MISC) -> [NONE] (or [JSNP])
 =   (JSNP) -> [MISC]
 =   (NONE) -> [MISC]

 O MARGIN CLICKED:
 + AND (MAGIN-SIDE == SCOLLBAR SIDE)
 = toggle tools visibility

 O MARGIN CLICKED:
 + AND (MAGIN-SIDE != SCOLLBAR SIDE)
 = toggle markers visibility

 + [TODO]
 + see RTabs.fs_webView_session_cycle_grab_collapse_or_hide
 + ...[hide-show-markers]
 + ...[also have to in parallel with WebView click]

*/ //}}}

        caller += "->gui_cycle";
//*GUI*/Settings.MOC(TAG_GUI, caller);
        String consumed_by = null;

        // TOGGLE SCROLLBAR TOOLS VISIBILITY .. f(SHOWING) .. (SB-CLICKED) .. (MARGIN-CLICKED) {{{
        if( sb_clicked_or_margin_clicked_on_sb_side(caller) )
        {
            // TOGGLE TOOLS VIS .. [MISC] [JSNP] [HIDE]
            sb_tools_toggle(caller);
            sb_tools_sync  (caller);
            consumed_by = "margin_clicked_on_sb_side";
        }
        //}}}
        // TOGGLE MARKERS VISIBILITY .. f(marker_pin_count_onDown) {{{
        else if(marker_pin_count_onDown > 0)
        {
//*GUI*/Settings.MOC(TAG_GUI, "", "MARKER TOGGLE VISIBILITY .. ("+marker_pin_count_onDown+" MARKERS)");

            property_toggle( WV_TOOL_MARK_LOCK );

            if(onDown_URDL != null) visualize_MARGIN_rect(gesture_down_wv, onDown_URDL);
            consumed_by = "WV_TOOL_MARK_LOCK";
        }
        //}}}
        // [return consumed_by] {{{
//*GUI*/if(consumed_by != null) Settings.MOC(TAG_GUI, caller, "...return consumed_by=["+consumed_by+"]");
        return consumed_by;
        //}}}
    }
    //}}}
    // sb_clicked_or_margin_clicked_on_sb_side {{{
    private boolean sb_clicked_or_margin_clicked_on_sb_side(String caller)
    {
        if(gesture_down_wv == null) return false;
        NpButton       sbX  = get_sb_for_wv_or_sb(gesture_down_wv);
        if(            sbX == null) return false;

//*GUI*/Settings.MOC(TAG_GUI, "sb_clicked_or_margin_clicked_on_sb_side(caller=["+caller+"])");
//*GUI*/Settings.MOM(TAG_GUI, "...........gesture_down_wv..=["+ get_view_name(gesture_down_wv) +"]");
//*GUI*/Settings.MOM(TAG_GUI, "...........gesture_down_sb..=["+ get_view_name(gesture_down_sb) +"]");

        boolean     sb_clicked = (gesture_down_sb != null);
        boolean margin_clicked = (onDown_URDL == MARGIN_L) || (onDown_URDL == MARGIN_R);
        boolean margin_clicked_on_sb_side
            =   margin_clicked
            && (       (onDown_URDL == MARGIN_L) && ( sbX.at_left)
                    || (onDown_URDL == MARGIN_R) && (!sbX.at_left));
        //*GUI*/Settings.MOM(TAG_GUI, ".......sb_clicked...........=["+ sb_clicked                     +"]");
//*GUI*/Settings.MOM(TAG_GUI, "...margin_clicked...........=["+ margin_clicked                 +"]");
//*GUI*/Settings.MOM(TAG_GUI, "...margin_clicked_on_sb_side=["+ margin_clicked_on_sb_side      +"]");

        return sb_clicked || margin_clicked_on_sb_side;
    }
//}}}

    // sb_tools_toggle {{{
    private void sb_tools_toggle(String caller)
    {
        // (tools_hidden) (js_selecting) {{{
        caller += "->sb_tools_toggle";
//*GUI*/Settings.MOC(TAG_GUI, caller);

        boolean tools_hidden = (!Settings.WV_TOOL_MISC_VIS && !Settings.WV_TOOL_JSNP_VIS);
        boolean js_selecting = property_get( WV_TOOL_JS1_SELECT );

//*GUI*/Settings.MOC(TAG_GUI, "", "WV_TOOL_MISC_VIS=("+ Settings.WV_TOOL_MISC_VIS +")");
//*GUI*/Settings.MOC(TAG_GUI, "", "WV_TOOL_JSNP_VIS=("+ Settings.WV_TOOL_JSNP_VIS +")");
//*GUI*/Settings.MOC(TAG_GUI, "", "....tools_hidden=("+ tools_hidden              +")");
//*GUI*/Settings.MOC(TAG_GUI, "", "....js_selecting=("+ js_selecting              +")");
        //}}}
        // [TOGGLE] TOOLS VIS .. [MISC] [JSNP] [HIDE] {{{
        if( tools_hidden                   ) {                                                          // ========== FROM == (HIDDEN          ) .. (when no tools are showing)
            if(js_selecting) { Settings.WV_TOOL_MISC_VIS = false; Settings.WV_TOOL_JSNP_VIS =  true; }  // ( js_selecting) -> [WV_TOOL_JSNP_VIS] .. (show selection tools if appropriate)
            else             { Settings.WV_TOOL_MISC_VIS =  true; Settings.WV_TOOL_JSNP_VIS = false; }  // (!js_selecting) -> [WV_TOOL_MISC_VIS] .. (or other tools when not selecting)
        }
        else if( Settings.WV_TOOL_JSNP_VIS ) {                                                          // ========== FROM == (WV_TOOL_JSNP_VIS) .. (when selection tools are showing)
            /*............*/ { Settings.WV_TOOL_MISC_VIS =  true; Settings.WV_TOOL_JSNP_VIS = false; }  // ( alternate to) -> [WV_TOOL_MISC_VIS] .. (alternate with other tools on click)
        }
        else if( Settings.WV_TOOL_MISC_VIS ) {                                                          // ========== FROM == (WV_TOOL_MISC_VIS) .. (when misc tools are showing)
            if(js_selecting) { Settings.WV_TOOL_MISC_VIS = false; Settings.WV_TOOL_JSNP_VIS =  true; }  // ( js_selecting) -> [WV_TOOL_JSNP_VIS] .. (show selection tools if appropriate)
            else             { Settings.WV_TOOL_MISC_VIS = false; Settings.WV_TOOL_JSNP_VIS = false; }  // (!js_selecting) -> (HIDE TOOLS      ) .. (hide all tools if not)
        }

//*GUI*/Settings.MOC(TAG_GUI, "", "WV_TOOL_MISC_VIS=["+ Settings.WV_TOOL_MISC_VIS +"]");
//*GUI*/Settings.MOC(TAG_GUI, "", "WV_TOOL_JSNP_VIS=["+ Settings.WV_TOOL_JSNP_VIS +"]");
        //}}}
    }
    //}}}
    // sb_tools_hide {{{
    private void sb_tools_hide(String caller)
    {
        // WV_TOOL_MISC_VIS WV_TOOL_JSNP_VIS {{{
        caller += "->sb_tools_toggle";
//*GUI*/Settings.MOC(TAG_GUI, caller);

//*GUI*/Settings.MOC(TAG_GUI, "", "WV_TOOL_MISC_VIS=("+ Settings.WV_TOOL_MISC_VIS +")");
//*GUI*/Settings.MOC(TAG_GUI, "", "WV_TOOL_JSNP_VIS=("+ Settings.WV_TOOL_JSNP_VIS +")");
        //}}}
        // [OFF] TOOLS VIS .. [MISC] [JSNP] [HIDE] {{{

        Settings.WV_TOOL_MISC_VIS = false;
        Settings.WV_TOOL_JSNP_VIS = false;

//*GUI*/Settings.MOC(TAG_GUI, "", "WV_TOOL_MISC_VIS=["+ Settings.WV_TOOL_MISC_VIS +"]");
//*GUI*/Settings.MOC(TAG_GUI, "", "WV_TOOL_JSNP_VIS=["+ Settings.WV_TOOL_JSNP_VIS +"]");
        //}}}
    }
    //}}}
    // sb_tools_sync {{{
    private void sb_tools_sync(String caller)
    {
        caller += "->sb_tools_sync";
//*GUI*/Settings.MOC(TAG_GUI, caller);

        // HIDE
        if     (!Settings.WV_TOOL_MISC_VIS ) hide_MISC_tools(caller);                 // [HIDE  ] (DISMISSED TOOLS)
        else if(!Settings.WV_TOOL_JSNP_VIS ) hide_JSNP_tools(caller);                 // [HIDE  ] (DISMISSED TOOLS)

        // SHAPE
        if     ( Settings.WV_TOOL_JSNP_VIS ) sb_shape_tools( JSNP_Map );
        else if( Settings.WV_TOOL_MISC_VIS ) sb_shape_tools( TOOL_Map );

        // LAYOUT
        if(Settings.WV_TOOL_MISC_VIS || Settings.WV_TOOL_JSNP_VIS)
        {
            sb_layout_tools_from_scratch(caller);
        }
        else if(sbX_np.button != null)
        {
            sbX_np.button.set_round_corners(); // Attempt to invoke virtual method 'void ivanwfr.rtabs.NpButton.set_round_corners()' on a null object reference
        }
    }
    //}}}

    //}}}
    /** SCROLLBAR (LAYOUT) */
    // {{{

    //* SCROLLBAR (LAYOUT) */
    // sb_shape_tools {{{
    private void sb_shape_tools(LinkedHashMap<String, Object> hashMap)
    {
        String shape = NotePane.Get_current_shape();
        for(Map.Entry<String,Object> entry : hashMap.entrySet())
        {
            if(                    !(entry.getValue() instanceof NotePane) ) continue;
            NotePane np = (NotePane) entry.getValue();

            if((np != null) && (np.button != null))
                np.button.set_shape( shape );
        }
    }
    //}}}
    // sb_layout_shrink_to_shape {{{
    public void sb_layout_shrink_to_shape()
    {
        if(sb  != null) sb .scroll_nb_shrink_to_shape();
        if(sb2 != null) sb2.scroll_nb_shrink_to_shape();
        if(sb3 != null) sb3.scroll_nb_shrink_to_shape();
    }
//}}}
    // sb_layout_tools_from_scratch {{{
    public  void sb_layout_tools_from_scratch(String caller)
    {
        caller += "->sb_layout_tools_from_scratch";
//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, caller);

        // after (gui cycle call  )
        // after (  toolset change)
        // after (fs_search parked)

        // [sb frame area] .. (clear cached tools) .. (forget about avoiding useless redraw)
        sb_layout_invalidate(caller); // after explicit sync request
        sb_layout_tools     (caller); // after sb layout from scratch
    }
    //}}}
    // sb_layout_invalidate {{{
    private Rect last_sb_adjust_tools_rect = new Rect();

    private void sb_layout_invalidate(String caller)
    {
//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, caller+"->sb_layout_invalidate");

        // (forget about avoiding useless redraw) // (clear cached tools)
        // after hiding tools (that wont be kept in sync with their frame when hidden)
        // after adjusting scrollbar frame
        // after explicit sync request

        last_sb_adjust_tools_rect.left   = 0;
        last_sb_adjust_tools_rect.top    = 0;
        last_sb_adjust_tools_rect.right  = 0;
        last_sb_adjust_tools_rect.bottom = 0;
    }
    //}}}
    // sb_layout_tools {{{
    public  void sb_layout_tools(String caller)
    {
        NpButton        sbX  = get_sb_for_wv_or_sb(gesture_down_wv);
        sb_layout_tools(sbX, caller);
    }
    public  void sb_layout_tools(NpButton sbX, String caller)
    {
        // [SHAPE_TAG_SCROLL] .. f(NO TOOLS)
        if(sbX_np.button       == null) sbX_np.button = sbX;     // (has happened) .. FIXME
        if(sbX_np.button       == null) sbX_np.button = get_sb_for_wv_or_sb(gesture_down_wv); // FIXME .. Attempt to read from field 'java.lang.String ivanwfr.rtabs.NpButton.shape' on a null object reference
        if(sbX_np.button       == null) return;
        if(sbX_np.button.shape != NotePane.SHAPE_TAG_SCROLL)
        {
            if(        !Settings.WV_TOOL_JSNP_VIS                // NOT SHOWING MISC TOOLS
                    && !Settings.WV_TOOL_MISC_VIS                // NOT SHOWING MISC TOOLS
                    && (sbX_np.w > (3*Settings.TOOL_BADGE_SIZE)) // WIDE SCROLLBAR
              )
                sbX_np.button.set_shape(NotePane.SHAPE_TAG_SCROLL);

            if(sbX_np.w < Settings.TOOL_BADGE_SIZE)
            {
                sbX_np.w = Settings.TOOL_BADGE_SIZE;
                sbX_np.button.setWidth( sbX_np.w );
                sbX_np.button.invalidate();
            }
        }

        // LAYOUT TOOLSET
        if     ( Settings.WV_TOOL_MISC_VIS ) sb_layout_tools_hashMap(sbX, TOOL_Map, caller);
        else if( Settings.WV_TOOL_JSNP_VIS ) sb_layout_tools_hashMap(sbX, JSNP_Map, caller);

    }
    //}}}
    // sb_layout_tools_hashMap {{{
    public  void sb_layout_tools_hashMap(NpButton sbX, LinkedHashMap<String, Object> hashMap, String caller)
    {
        // LAYOUT OR HIDE {{{
        caller += "->sb_layout_tools_hashMap("+get_view_name(sbX) +", "+get_hashMap_name(hashMap)+")";
//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, caller);
        if(sbX == null) return;

        //}}}
        // FRAME GEOMETRY {{{
        boolean  at_left   = sbX.at_left;

        int frame_y;
        int frame_x;
        int frame_w;
        int frame_h;
        // XXX{{{
        if( Settings.SCROLLBAR_USE_LAYOUT_PARAMS ) {
            FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams)sbX.getLayoutParams();
            frame_y        = flp.topMargin;
            frame_x        = flp.leftMargin;
            frame_w        = flp.width;
            frame_h        = flp.height;
            if( Settings.SCROLLBAR_USE_TRANSLATION ) {
                frame_x    = (int)sbX.getTranslationX();
                frame_y    = (int)sbX.getTranslationY();
            }
            else {
                frame_x    = (int)sbX.getX();
                frame_y    = (int)sbX.getY();
            }
        } else {
            frame_y        = (int)sbX.getY();
            frame_x        = (int)sbX.getX();
            frame_w        =      sbX.getWidth ();
            frame_h        =      sbX.getHeight();
        }
        // XXX}}}
        int frame_margin   = 0;//Settings.SCROLLBAR_W_MIN; // tinted body
        int frame_right    = frame_x+frame_w;

        int slot_size      = Settings.TOOL_BADGE_SIZE;
        int bottom_max     = Settings.DISPLAY_H - slot_size;

        int x_min = (at_left) ? frame_x     + frame_margin              // may not overlap scrollbar body to the left
            :                   frame_x                               ; // may touch left  frame border
        int x_max = (at_left) ? frame_right                - slot_size  // may touch right frame border
            :                   frame_right - frame_margin - slot_size; // may not overlap scrollbar body to the right
      //int y_min =             frame_y                    + slot_size; // below       top frame border + sb label height
      //int y_max =             frame_y     + frame_h      - slot_size; // above    bottom frame border
        int y_min =             frame_y                               ; // above frame top    .. (for tools bottom)
        int y_max =             frame_y     + frame_h                 ; // below frame bottom .. (for tools top   )

      //y_min    +=             Settings.FS_SELECT_BORDER;
      //y_max    -=             Settings.FS_SELECT_BORDER;
      //x_min    +=             Settings.FS_SELECT_BORDER;
      //x_max    -=             Settings.FS_SELECT_BORDER;

        //}}}
        // FRAME UNCHANGED {{{
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, String.format("SB FRAME LAYOUT XY=[%4d %4d] WH=[%4d %4d]",x_min,y_min, x_max-x_min,y_max-y_min));
        if(        (last_sb_adjust_tools_rect.left   == x_min) && (last_sb_adjust_tools_rect.right  == x_max)
                && (last_sb_adjust_tools_rect.top    == y_min) && (last_sb_adjust_tools_rect.bottom == y_max)
        ) {
//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, caller, "@@@ FRAME UNCHANGED @@@");
            return;
        }
        else {
            last_sb_adjust_tools_rect.left   = x_min;
            last_sb_adjust_tools_rect.top    = y_min;
            last_sb_adjust_tools_rect.right  = x_max;
            last_sb_adjust_tools_rect.bottom = y_max;
        }
        //}}}
        // TRACK VISIBILITY CHANGES .. f(frame boundaries) {{{
        String some_tool_shown  = "";
        String some_tool_hidden = "";

        // }}}
        // [cols]x[rows] .. (to contain all defined tools) {{{
        int tools_count = hashMap.size();
        int col_max     = Math.max(frame_w / slot_size, 1); // [tools cols] limited to (scrollbar frame width)
        int row_max     =      tools_count / col_max      ; // (with this many cols), [how many rows] (to show all tools)

    //  float rows_on_cols_ratio = (float)row_max / (float)col_max;

      // [PLAN A] .. (squared frame)
      //row_max = col_max;

        //}}}
        // TOOLS (frame-top to screen-top) -OR- (frame-bottom to screen-bottom) {{{
        int  available_above_sb  =                       frame_y           ; // space above  scrollbar top
        int  available_below_sb  = Settings.DISPLAY_H - (frame_y + frame_h); // space below scrollbar bottom

        int          rows_above  = available_above_sb  / slot_size -1      ; // how many rows would fit above
        int          rows_below  = available_below_sb  / slot_size -1      ; // how many rows would fit below

        boolean           at_top = (rows_above >= row_max    )       ? true  // [requested] (fit above)
            :                      (rows_above >= rows_below)              ; //      [best] (fit above) or (fit below)

        row_max = Math.min(row_max  , (at_top ? rows_above : rows_below))  ; // cap rows to available tools slot
        //............... (request) , (best available)

        if((col_max * row_max) < tools_count) row_max += 1;
        //}}}
        // FIRST TOOL POSITION .. f(at_left) {{{
        int      y = at_top  ? y_min : y_max;
        int      x = at_left ? x_min : x_max;

        int wrap_x = x;
        //}}}
        // START SITUATION {{{
        int col                = 1; // [at_left] .. (or not)
        int row                = 1;

        int this_row_height    = slot_size;
        int prev_rows_height   = 0;
        int  all_rows_height   = this_row_height;

        ArrayList<NpButton> nb_al = new ArrayList<>();

        int hide_count = 0;
        int show_count = 0;
        //}}}
        for(Map.Entry<String,Object> entry : hashMap.entrySet())
        {
            // BADGE CENTER POSITION {{{
            if(                   !( entry.getValue() instanceof NotePane) ) continue;
            NotePane np = (NotePane) entry.getValue(); if(np == sbX_np) continue;
//SCROLLBAR//Settings.MOM(TAG_SCROLLBAR, "..np=["+np+"]");

            // SKIP CURRENTLY DRAGGED TOOLS
            boolean user_is_clamping
                =  (  np.button    ==  fs_search )
                && is_clamping_button( fs_search );

            int w = 0;
            if( !user_is_clamping)
            {
                int h = 0;
                // XXX{{{
                if( Settings.SCROLLBAR_USE_LAYOUT_PARAMS ) {
                    FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams)np.button.getLayoutParams();
                    h                 = flp.height;
                    w                 = flp.width;
                    flp.leftMargin    = x;
                    int             t = y - (at_top ? h : 0);
                    flp.topMargin     = t;
                    if( Settings.SCROLLBAR_USE_TRANSLATION ) {
                        flp.leftMargin           = 0;
                        flp.topMargin            = 0;
                        np.button.setTranslationX( x );
                        np.button.setTranslationY( t );
                    }
                    else {
                        np.button.setX( x );
                        np.button.setY( t );
                    }
                    np.x = x;
                    np.y = t;
                    np.button.setLayoutParams( flp );
                } else {
                    h                 = np.button.getHeight();
                    w                 = np.button.getWidth ();
                    int             t = y - (at_top ? h : 0);
                    np.button.setX( x );
                    np.button.setY( t );
                }
                // XXX}}}

                // used slot
                nb_al.add( np.button );
                this_row_height = Math.max(this_row_height, h);
            }
            //}}}
            // SHOW-HIDE (not empty slots) {{{
            if( !user_is_clamping)
            {
                // vis {{{
                boolean vis = (row <= row_max); // (there may be not enough room for this one, at the moment)

                //}}}
                // SHOW {{{
//SCROLLBAR//Settings.MOM(TAG_SCROLLBAR, "..vis=["+vis+"]");
                if( vis ) {
                    if( !is_view_showing( np.button ) )
                    {
//SCROLLBAR//Settings.MOM(TAG_SCROLLBAR, "...SHOWING");
                        np.button.setVisibility( View.VISIBLE );
                        np.button.bringToFront();
                      //some_tool_shown += " "+get_view_name(np.button);
                        ++show_count;
                        some_tool_shown += show_count+" [tag="+np.tag+", name="+np.name+"]";
                    }
                }
                //}}}
                // OR HIDE {{{
                else {
                    if( is_view_showing( np.button ) )
                    {
//SCROLLBAR//Settings.MOM(TAG_SCROLLBAR, "...HIDING");
                        np.button.setVisibility( View.GONE );
                        some_tool_hidden += " "+get_view_name(np.button);
                        ++hide_count;
                    }
                }
                //}}}
            }
            //}}}
            // NEXT TOOL SLOT .. (with reserved empty slots) {{{
            // same row, next column
            x     += (at_left) ? w  : -w;
            if((x >= x_min) && (x <= x_max))
            {
                col += 1;
            }
            // next row .. f(row_max)
            else {
                row += 1;
                if(row <= row_max)
                {
                    prev_rows_height += this_row_height                    ; // cumulate used row heights so far
                    y                += this_row_height * (at_top ? -1 : 1); // next row above or below
                //  this_row_height   = Settings.TOOL_BADGE_SIZE           ; // reset to nominal height
                    this_row_height   = Settings.TAB_MARK_H                ; // reset to minimal height
                }
                // left column
                col = 1;
                x   = wrap_x;
            }

            // (y_max - y_min) may need to grow .. f(each time this_row_height may have changed)
            all_rows_height = Math.max(all_rows_height, prev_rows_height + this_row_height);

            // LAYOUT TIPS {{{
            //
            // we must [see everything] when we have [col==col_max]
            //
            // .. (as this is the best we can do)
            //
            // ...so: we must have [row=row_max] at that point to get the job done
            //
            // HOW TO ACHIEVE :
            // ...[row = row_max] when we get to [col==col_max]
            //
            // everytime a col is added
            // ...row must be adjusted to maintain:
            // ...[row/col] == [col * rows_on_cols_ratio]


            // }}}
            //}}}
        }
//        // HIT BOTTOM .. (TranslationY) {{{
//        y_max          = frame_y + all_rows_height;
//        int y_tr       = 0;
//      //if(y_max > bottom_max) y_tr = -(y_max - bottom_max);          // move up
//        if(y_max > bottom_max) y_tr = -(slot_size + all_rows_height); // move above sb
//        if(y_tr != 0) {
//            for(int i= 0; i < nb_al.size(); ++i) {
//                NpButton nb = nb_al.get(i);
//                // XXX{{{
//                if( Settings.SCROLLBAR_USE_LAYOUT_PARAMS ) {
//                    FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams)nb.getLayoutParams();
//                    flp.topMargin     += y_tr;
//                    if( Settings.SCROLLBAR_USE_TRANSLATION ) {
//                        flp.topMargin     = 0;
//                        nb.setTranslationY(y_tr + nb.getTranslationY());
//                    }
//                    else {
//                        nb.setY(y_tr + nb.getY());
//                    }
//                    nb.setLayoutParams( flp );
//                } else {
//                    nb.setY(y_tr + nb.getY());
//                }
//                // XXX}}}
//            }
//        }
//        //}}}
        // CORNERS {{{
        NotePane.check_neighbors_with_cell_size(hashMap, slot_size);

        //}}}
        // SOUND .. (tools change) {{{
//SCROLLBAR//Settings.MOM(TAG_SCROLLBAR, "...show_count=["+show_count+"]");
//SCROLLBAR//Settings.MOM(TAG_SCROLLBAR, "...hide_count=["+hide_count+"]");
//SCROLLBAR//Settings.MOM(TAG_SCROLLBAR, "...some_tool_shown =["+some_tool_shown +"]");
//SCROLLBAR//Settings.MOM(TAG_SCROLLBAR, "...some_tool_hidden=["+some_tool_hidden+"]");

        boolean clicking = !TextUtils.isEmpty( some_tool_shown  );
        boolean dingging = !TextUtils.isEmpty( some_tool_hidden );

        if(clicking || dingging)
        {
            if( !property_get( WV_TOOL_silent ) )
            {
                if( clicking ) activity_play_sound_click(caller);
                if( dingging ) activity_play_sound_ding (caller);
            }
        }
        //}}}
    }
    //}}}
    // sb_layout_frame {{{
    // {{{
/*
    private static final long CHANGE_SIDE_COOLDOWN           =  500;
    private static final long CHANGE_SIDE_PERIOD             = 2000;
    private              long last_change_side_cooldown_time =    0;
*/
    // }}}
    private void sb_layout_frame(NpButton sbX, RTabs.MWebView wv, int dx)
    {
        String caller = "sb_layout_frame";
//*SCROLLBAR*/  caller = "sb_layout_frame("+get_view_name(wv)+", dx=["+dx+"])";//TAG_SCROLLBAR

        // ASSOCIATE [sbX] to its [wv] {{{
        sbX.setTag( wv );

        // }}}
        // WEBVIEW [w_min] [w_max] {{{
        int    w_max  = wv.getWidth() - Settings.SCROLLBAR_SHAPE_W;
        int    w_min  = Settings.SCROLLBAR_W_MIN;

        // }}}
        // SCROLLBAR CURRENT LAYOUT {{{
       FrameLayout.LayoutParams flp = null;
       int flp_leftMargin           = 0;
       int flp_topMargin            = 0;
       int flp_width                = 0;
       int flp_height               = 0;

       // XXX{{{
       if( Settings.SCROLLBAR_USE_LAYOUT_PARAMS ) {
           flp = (FrameLayout.LayoutParams)sbX.getLayoutParams();
           flp_leftMargin       = flp.leftMargin;
           flp_topMargin        = flp.topMargin ;
           flp_width            = flp.width     ;
           flp_height           = flp.height    ;
           if( Settings.SCROLLBAR_USE_TRANSLATION ) {
                flp_leftMargin  = (int)(sbX.getTranslationX());
                flp_topMargin   = (int)(sbX.getTranslationY());
           }
           else {
                flp_leftMargin  = (int)(sbX.getX());
                flp_topMargin   = (int)(sbX.getY());
           }
       } else {
           flp_leftMargin       = (int)sbX.getX     ();
           flp_topMargin        = (int)sbX.getY     ();
           flp_width            =      sbX.getWidth ();
           flp_height           =      sbX.getHeight();
       }
       // XXX}}}

//SCROLLBAR//Settings.MON(TAG_SCROLLBAR, caller, "...flp_width=["+flp_width+"]");
        // }}}
        // FRAME CURRENT GEOMETRY [at_top] [at_left] {{{
        int     wv_left   = (int)(wv .getLeft() + wv.getTranslationX());
        int     wv_right  = (int)(wv_left       + wv.getWidth() * wv.getScaleX());

      //int     sb_left   = flp_leftMargin    ; int sb_right = flp_leftMargin + flp_width;
      //int     dx_left   =  sb_left - wv_left; int dx_right = wv_right       - sb_right ;
      //boolean at_left
      //    =   (dx==0) // .. (does not come from user)
      //    ?     sbX.at_left                              // initially false
      //    :    (Math.abs(dx_left) < Math.abs(dx_right)); // may be reconsidered when moved

        boolean at_left = sbX.at_left;

    //  int     wv_top  = wv .getTop ()    ; int wv_bottom = wv.getBottom();
        int     wv_top    = (int)(wv .getTop() + wv.getTranslationY());
        int     wv_bottom = (int)(wv_top       + wv.getHeight() * wv.getScaleY());
        int     sb_top    = flp_topMargin  ; int sb_bottom = flp_leftMargin + flp_width;
        int     dx_top    = sb_top - wv_top; int dx_bottom = sb_bottom      - wv_bottom;

        boolean at_top    = (Math.abs(dx_top) < Math.abs(dx_bottom));
        // }}}
        // [sbX] [w] [w_delta] {{{
        int w = flp_width + dx * (at_left ? -1 : 1);
        /**/w = Math.max(w_min, w       );
        /**/w = Math.min(       w, w_max);

//*SCROLLBAR*/Settings.MON(TAG_SCROLLBAR, caller, "...(w_min "+w_min+") <= [w "+w+"] <= (w_max "+w_max+")");
        // }}}

        // 1/2 [CHANGE SIDE] .. f(w == w_max) {{{
        if((flp_width < w) && (w == w_max))
        {
//            // PREPARE TO CHANGE SIDE {{{
//
//            long    time_elapsed_since_last_pulse = System.currentTimeMillis() - last_change_side_cooldown_time;
//            boolean     too_soon  = (time_elapsed_since_last_pulse < CHANGE_SIDE_COOLDOWN);
//            boolean     too_late  = (time_elapsed_since_last_pulse > CHANGE_SIDE_PERIOD  );
//            boolean moved_enough  = (Math.abs(dx) > Settings.SCALED_TOUCH_SLOP);
//            boolean on_cooldown   = activity_has_sb_on_cooldown(sbX);
//            if(    !moved_enough || too_soon || too_late) {
//                last_change_side_cooldown_time = System.currentTimeMillis();
//                activity_schedule_sb_maxed_cooldown(sbX, CHANGE_SIDE_PERIOD);
//                return;
//            }
//            activity_clear_sb_maxed_cooldown( sbX );
//            last_change_side_cooldown_time = 0; // .. (next cycle starts from scratch)
//            // }}}
            // DO CHANGE SIDE (user insists)
            at_left   = !at_left;

            int grid_w_max = Settings.TOOL_BADGE_SIZE * (w_max / Settings.TOOL_BADGE_SIZE); // [number of badges] that fit withing [w_max]
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, "grid_w_max=["+grid_w_max+"]");

            flp_width = grid_w_max;
//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, caller, "[CHANGE SIDE]: flp_width=["+flp_width+"] (at_left "+at_left+") (w_max "+w_max+")");

            sbX.at_left = at_left;
            marker_sync_sb_side(caller);                  // after adjusting frame on the other side
        }
        //}}}
        // 2/3 [WIDTH SHAPE] .. f(shape) {{{
        else if(  (dx != 0)
                && (Settings.WV_TOOL_MISC_VIS || Settings.WV_TOOL_JSNP_VIS)
                // (sbX.shape != NotePane.SHAPE_TAG_SCROLL)
        ) {
            // TOOLS GRID VISIBLE {{{
            {
                // [INTEGER MULTIPLE] of [TOOL_BADGE_SIZE]

                // [flp_width] [+/-] (number of badges width)
                int badge_cnt  = (sbX_np.w + Settings.TOOL_BADGE_SIZE/2) / Settings.TOOL_BADGE_SIZE;
                boolean larger = at_left ? (dx < 0) : (dx > 0);
                badge_cnt     += larger ? 1 : -1;
                flp_width      = badge_cnt * Settings.TOOL_BADGE_SIZE;

                // [grid clipping] 
                int grid_cols  = 1; //get_current_tools_cols();      // [number of badges] smallest tools grid
                int grid_cmax  = (w_max / Settings.TOOL_BADGE_SIZE); // [number of badges] that fit withing [w_max]

                flp_width      = Math.max(flp_width , grid_cols * Settings.TOOL_BADGE_SIZE); // not smaller than [grid_cols]
                flp_width      = Math.min(flp_width , grid_cmax * Settings.TOOL_BADGE_SIZE); // not larger  than [grid_cmax]

//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, caller, "[WIDTH SHAPE]: (integer multiple of TOOL_BADGE_SIZE) = ["+flp_width+"] .. ("+((float)flp_width/(float)Settings.TOOL_BADGE_SIZE)+" * TOOL_BADGE_SIZE=["+Settings.TOOL_BADGE_SIZE+"]) (at_left "+at_left+")");
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR,         "flp_width=["+ flp_width +"]");
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR,         "grid_cols=["+ grid_cols +"]");
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR,         "grid_cmax=["+ grid_cmax +"]");
            }
            //}}}
          }
        //}}}
        // 3/2 [WIDTH FRAME] .. f(dx) {{{
        else {
            flp_width = w;        // [ADJUST WIDTH]

        //  activity_clear_sb_maxed_cooldown( sbX ); // CLEAR UNFINISHED SIDE CHANGE
//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, caller, "[WIDTH FRAME]: flp_width=["+flp_width+"] (at_left "+at_left+")");
        }
        //}}}

        // [flp_leftMargin] .. f(at_left) {{{
        if( at_left   ) flp_leftMargin = wv_left             ;
        else            flp_leftMargin = wv_right - flp_width;
        //}}}
        // [xy_wh] {{{
        // XXX{{{
        if( Settings.SCROLLBAR_USE_LAYOUT_PARAMS ) {
            flp.leftMargin     = flp_leftMargin ;
            flp.topMargin      = flp_topMargin  ;
            flp.width          = flp_width      ;
            flp.height         = flp_height     ;
            if( Settings.SCROLLBAR_USE_TRANSLATION ) {
                flp.leftMargin = 0;
                flp.topMargin  = 0;
                sbX.setTranslationX(flp_leftMargin);
                sbX.setTranslationY(flp_topMargin);
            }
            else {
                sbX.setX(flp_leftMargin);
                sbX.setY(flp_topMargin);
            }
            sbX.setLayoutParams( flp           );
        } else {
            sbX.setWidth       ( flp_width     );
            sbX.setX           ( flp_leftMargin);
            sbX.setHeight      ( flp_height    );
            sbX.setY           ( flp_topMargin );
        }
        sbX_np.x = flp_leftMargin;
        sbX_np.y = flp_topMargin;
        sbX_np.w = flp_width;
        sbX_np.h = flp_height;
        // XXX}}}
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, String.format("FRAME LAYOUT XY=[%4d @ %4d] WH=[%4d x %4d]",flp_leftMargin,flp_topMargin , flp_width,flp_height));

        //}}}
        // [FRAME] or [SHAPE] {{{
      //if( !property_get( WV_TOOL_JS1_SELECT ) )
        if(sbX.shape == NotePane.SHAPE_TAG_SCROLL)
        {
            if(flp_width < (3*Settings.SCROLLBAR_SHAPE_W))
            {
                sbX.set_shape( NotePane.Get_current_shape() );
                if( sbX_np.button != null)
                    sbX_np.button.set_round_corners();
            }
            else {
                sbX.scroll_nb_set_w_min_w_max_at_left_at_top(w_min, w_max, at_left, at_top);
            }
        }
        //}}}
        // [tools] (invalidate frame) {{{
        sb_layout_invalidate(caller); // after adjusting scrollbar frame

        //}}}
        // [setVisibility] {{{
        sbX.setVisibility( View.VISIBLE );
        sbX.bringToFront();
        sb_sync_LABEL();
        //}}}
    }
    //}}}

    //* SCROLLBAR (SHOW-HIDE TOOLS) */
    // sb_is_frame_showing {{{
    public  boolean sb_is_frame_showing()
    {
        return ( is_view_showing(sb ) && (sb .shape == NotePane.SHAPE_TAG_SCROLL))
            || ( is_view_showing(sb2) && (sb2.shape == NotePane.SHAPE_TAG_SCROLL))
            || ( is_view_showing(sb3) && (sb3.shape == NotePane.SHAPE_TAG_SCROLL))
            ;
    }
    //}}}
    // sb_is_frame_showing {{{
    private boolean sb_is_frame_showing(NpButton sbX)
    {
        return ( is_view_showing(sb ) && (sb .shape == NotePane.SHAPE_TAG_SCROLL));
    }
    //}}}
    // sb_hide_tools {{{
    public void sb_hide_tools(String caller)
    {
        caller += "->sb_hide_tools";
//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, caller);

        if((gesture_down_sb != null) && gesture_down_sb.action_down_in_margin)
        {
//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, caller, "DO NOT HIDE A RESIZING SCROLLBAR");
            return;
        }

        boolean some_tools_hidden = hide_tools(caller);

        if(some_tools_hidden && !property_get(WV_TOOL_silent))
            activity_play_sound_ding (caller);
    }
    //}}}
    // hide_tools {{{
    private boolean hide_tools(String caller)
    {
        caller += "->hide_tools";
//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, caller);

        boolean some_MISC_tools_hidden = hide_MISC_tools(caller);
        boolean some_JNSP_tools_hidden = hide_JSNP_tools(caller);

        return (some_MISC_tools_hidden || some_MISC_tools_hidden);
    }
    //}}}
    // hide_MISC_tools {{{
    private boolean hide_MISC_tools(String caller)
    {
        // [TOOL_Map] {{{
        caller += "->hide_MISC_tools";
//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, caller);

        boolean some_tool_hidden = false;
        for(Map.Entry<String, Object> entry : TOOL_Map.entrySet())
        {
            if( !(entry.getValue() instanceof NotePane) ) continue;
            NotePane np = (NotePane)entry.getValue(); if(np == sbX_np) continue;

            if(!some_tool_hidden) some_tool_hidden = (np.button.getVisibility() != View.GONE);

            np.button.setVisibility( View.GONE );
        }
        //}}}
        return some_tool_hidden;
    }
    //}}}
    // hide_JSNP_tools {{{
    private boolean hide_JSNP_tools(String caller)
    {
        // [JSNP_Map] {{{
        caller += "->hide_JSNP_tools";
//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, caller);

        boolean some_tool_hidden = false;
        for(Map.Entry<String, Object> entry : JSNP_Map.entrySet())
        {
            if( !(entry.getValue() instanceof NotePane) ) continue;
            NotePane np = (NotePane)entry.getValue(); if(np == sbX_np) continue;

            if(!some_tool_hidden) some_tool_hidden = (np.button.getVisibility() != View.GONE);

            np.button.setVisibility( View.GONE );
        }
        //}}}
        return some_tool_hidden;
    }
    //}}}
    // get_current_tools_cols {{{
    private int get_current_tools_cols()
    {
        int slots;
        if     ( Settings.WV_TOOL_JSNP_VIS ) slots = JSNP_Map.size();
        else if( Settings.WV_TOOL_MISC_VIS ) slots = TOOL_Map.size();
        else                                 slots = TOOL_Map.size(); // fallback

        int cols = (int)Math.sqrt( (float)slots );  // how many cols to show all slots within a square grid

        if((cols*cols) < slots) cols += 1;          // add one if clipped .. (5 slots)=>(2*2 < 5)

        return cols;
    }
    //}}}

    //* SCROLLBAR (THUMB) */
    // sb_adjust_thumb {{{
    public void sb_adjust_thumb(NpButton sbX, RTabs.MWebView wv)
    {
        // DO NOT MOVE MOVED SCROLLBAR .. f(gesture_down_sb) {{{
        if(        !scroll_wv_can_follow_finger()
                && (gesture_down_sb != null)
              //&& !sb_check_done
                )
        {
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, "sb_adjust_thumb: NOT MOVING MOVED SCROLLBAR");

            return;
        }
        //}}}
        // WEBVIEW PAGE VIEWPORT  {{{

        int[]  thumb_YH   = new  int[2];
        get_wv_thumb_YH(wv, thumb_YH);
        int    thumb_y    = thumb_YH[0]; // [sb] view Y
        int    thumb_h    = thumb_YH[1]; // [sb] view Height

        //}}}
        // LAYOUT [sbX] {{{
        if( Settings.SCROLLBAR_USE_LAYOUT_PARAMS )
        {
            FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams)sbX.getLayoutParams();
            flp.topMargin                = thumb_y;
            if( Settings.SCROLLBAR_USE_TRANSLATION )
            {
                flp.topMargin            = 0;
                sbX.setTranslationY(       thumb_y);
            }
            else {
                sbX.setY(                  thumb_y);
            }
            flp.height                   = thumb_h ;
            sbX.setLayoutParams(flp);

        }
        else {
            sbX.setHeight(                 thumb_h);
            sbX.setY     (                 thumb_y);

        }
        sbX_np.y = thumb_y;
        //}}}
        // [is_floating_marker] .. (along with scrollbar) {{{
        if( is_floating_marker() )
            marker_float_drag( (int)sbX.getY() );

        visualize_DRAG_rect();
        // }}}
        // SCROLLBAR THUMB COLOR {{{
        int bg_color = mRTabs.get_wv_bg_color( wv );
        int fg_color = (ColorPalette.GetBrightness( bg_color ) < 128) ? Color.WHITE : Color.BLACK;

        sbX.setBackgroundColor(bg_color | 0xff000000); // opaque
        sbX.setAlpha(1f);

        fs_search_set_colors(fg_color, bg_color);

        //}}}
        sb_layout_frame(sbX, wv, 0); // .. (keep current width)
    }
    //}}}

    //}}}
    /** WEBVIEW */
    // {{{
    // wv_getUrl {{{
    private static String wv_getUrl(WebView wv)
    {
        // filter data scheme URL
        String url = wv.getUrl();
        if(url == null)             return null;
        if(url.startsWith("data:")) return null;
        else                        return url;
    }
    //}}}
    // get_wv_containing_view {{{
    public  RTabs.MWebView get_wv_containing_view(View view)
    {
        return (view instanceof NpButton)
            ?   get_wv_containing_np_button( (NpButton)view )
            :   null;
    }
    //}}}
    // get_wv_containing_np_button {{{
    public  RTabs.MWebView get_wv_containing_np_button(NpButton np_button)
    {
        if(np_button == null) return null;

        String caller = "get_wv_containing_np_button";
//*GUI*/caller += "("+get_view_name(np_button)+")";//TAG_GUI
//*GUI*/Settings.MOM(TAG_GUI, caller);

        // WEBVIEW <= [np_button == sbX] {{{
        RTabs.MWebView wv = null;
        if(        (np_button == sb )
                || (np_button == sb2)
                || (np_button == sb3)
          ) {
            Object o = np_button.getTag();   // [wv]<->[sbX] .. f(association in sb_layout_frame)
            if(o instanceof RTabs.MWebView)
                wv = (RTabs.MWebView)o;
//GUI//Settings.MOM(TAG_GUI, caller+" ...(np_button == sbX) ["+get_view_name(wv)+"]");
          }
        // }}}
        // WEBVIEW CONTAINING [np_button-center] {{{
        else if( has_view(np_button, caller) )
        {
//*GUI*/FrameLayout.LayoutParams flp =  (FrameLayout.LayoutParams)np_button.getLayoutParams();//TAG_GUI
//*GUI*/Settings.MOM(TAG_GUI, "flp.leftMargin.............=["+flp.leftMargin             +"]");
//*GUI*/Settings.MOM(TAG_GUI, "np_button.getLeft()........=["+np_button.getLeft()        +"]");
//*GUI*/Settings.MOM(TAG_GUI, "np_button.getX()...........=["+np_button.getX()           +"]");
//*GUI*/Settings.MOM(TAG_GUI, "np_button.getTranslationX()=["+np_button.getTranslationX()+"]");

        //  int  x = (int)np_button.getX           () + np_button.getWidth () / 2;
        //  int  y = (int)np_button.getY           () + np_button.getHeight() / 2;
            int  x = (int)np_button.getTranslationX() + np_button.getWidth () / 2;
            int  y = (int)np_button.getTranslationY() + np_button.getHeight() / 2;
//*GUI*/Settings.MOM(TAG_GUI, "x=["+x+"]");
//*GUI*/Settings.MOM(TAG_GUI, "y=["+y+"]");

            // or [marker bottom hot spot]

            if( is_a_pinned_marker( Settings.get_np_for_button(np_button) ) )
                y += np_button.getHeight() / 2;

            wv     = get_hr_wv_at_XY(x, y);
            if(wv != null) {
//*GUI*/Settings.MOM(TAG_GUI, caller+" ...get_hr_wv_at_XY(x,y) ["+get_view_name(wv)+"]");
            }
            else {
                wv     = get_hr_wv_at_XY_hidden_or_not(x, y);
//*GUI*/Settings.MOM(TAG_GUI, caller+" ...get_hr_wv_at_XY_hidden_or_not(x,y) ["+get_view_name(wv)+"]");
            }

        }
        //}}}
//*GUI*/Settings.MOM(TAG_GUI, caller+": ...return ["+get_view_name(wv)+"]");
        return wv;
    }
//}}}
    //* WEBVIEW (THUMB) */
    // get_wv_thumb_YH {{{
    private void get_wv_thumb_YH(RTabs.MWebView wv, int[] thumb_YH)
    {
        // VISUALISATION OF CURRENT WEBVIEW PAGE VIEWPORT .. (scale correction applied)
        float wv_page_height = Math.max(wv.computeVerticalScrollRange (), 1f);
        float    wv_page_top = Math.max(wv.computeVerticalScrollOffset(), 0f);

        int             wv_y = (int) wv.getY     ();
        int             wv_h =       wv.getHeight();
        float          wv_sY = 1;//  wv.getScaleY(); // (200831) scrollbar is not translated on tilted View

        int             wv_b = (int)(wv_y + wv_h * wv_sY);

        int          thumb_y = (int)(wv_y + wv_h * wv_page_top / wv_page_height * wv_sY); // THUMB-TOP     [visible] / [whole] (scaled)
        int          thumb_h = (int)(       wv_h * wv_h        / wv_page_height * wv_sY); // THUMB-HEIGHT  [visible] / [whole] (scaled)

        thumb_h = Math.max(Settings.SCROLLBAR_H_MIN, thumb_h); // not smaller than [SCROLLBAR_H_MIN]
        thumb_h = Math.min(      8 * wv_h / 10     , thumb_h); // not taller  than [80% wv height  ]
        thumb_y = Math.max(          wv_y, thumb_y);           // not above        [    wv    top  ]
        thumb_y = Math.min(wv_b - thumb_h, thumb_y);           // not below        [    wv    bot  ]

        thumb_YH[0] = thumb_y;
        thumb_YH[1] = thumb_h;
    }
    //}}}
    // get_wv_thumb_p {{{
    private float get_wv_thumb_p(RTabs.MWebView wv)
    {
        if(wv == null) return 0f;

        float wv_page_height = Math.max(wv.computeVerticalScrollRange (), 1f);
        float    wv_page_top = Math.max(wv.computeVerticalScrollOffset(), 0f);
        float        thumb_p = 100f * wv_page_top / wv_page_height; // what's hidden above wv current sroll-top
        return thumb_p;
    }
    //}}}
    // get_wv_thumb_p_str {{{
    private String get_wv_thumb_p_str(RTabs.MWebView wv)
    {
        if(          wv == null) return Settings.EMPTY_STRING;

        float   thumb_p  =              get_wv_thumb_p( wv );
        if((int)thumb_p == 0   ) return Settings.EMPTY_STRING;


        float   scroll_offset = wv.computeVerticalScrollOffset();
        float       wv_height = wv.getHeight();
        int          page_num = 1 + (int)(scroll_offset / wv_height);

        return String.format(" %2.1f%s%s", thumb_p, Settings.SYMBOL_SMALL_PERCENT, " p"+page_num);
    }
    //}}}
    // get_wv_scrollY_for_f_y {{{
    private int get_wv_scrollY_for_f_y(RTabs.MWebView wv, int f_y, String caller)
    {
        float   range = wv.computeVerticalScrollRange();
        float visible = wv.getHeight();
        float  factor = range / visible;
        float     w_y = wv.getY();              // [wv] vertical screen offset
        float thumb_y = f_y - w_y;              // how far from [wv] top (hotspot)
        int       toY = (int)(thumb_y * factor);

        toY = Math.max(0, toY            );
        toY = Math.min(   toY, (int)range);
        return    toY;
    }
    //}}}
    //* WEBVIEW (MARGIN) */
    // get_onDown_URDL {{{
    // {{{
    private static final String MARGIN_U = "MARGIN_U";
    private static final String MARGIN_R = "MARGIN_R";
    private static final String MARGIN_D = "MARGIN_D";
    private static final String MARGIN_L = "MARGIN_L";

    // }}}
    private String get_onDown_URDL(int x, int y, RTabs.MWebView wv)
    {
        // {{{
//*GUI*/String caller = "get_onDown_URDL("+x+","+y+","+get_view_name(wv)+")";//TAG_GUI

//*GUI*/Settings.MOM(TAG_GUI, caller+": (wv == null)");
        if(wv == null) return null;

//*GUI*/String msg = "";//TAG_GUI
        String    result = null;
        int         wv_x = (int)wv.getX();
        //}}}
        // TOP MARGIN {{{
        if(result == null)
        {
            // todo: when required

        }
        //}}}
        // RIGHT MARGIN {{{
        if(result == null)
        {
            int  wv_w  = (int)(wv.getWidth() * wv.getScaleX());
            int r_max  = wv_x + wv_w;
            int r_min  = r_max - Settings.TOOL_BADGE_SIZE;
            if((r_min <= x) && (x <= r_max))
                result = MARGIN_R;
        }
        // }}}
        // BOTTOM MARGIN {{{
        if(result == null)
        {
            // todo: when required

        }
        //}}}
        // LEFT MARGIN {{{
        if(result == null)
        {
            int l_min  = wv_x;
            int l_max  = l_min + Settings.TOOL_BADGE_SIZE;
            if((l_min <= x) && (x <= l_max))
                result = MARGIN_L;
        }
        //}}}
        // NOT IN MARGINS {{{
        if(result == null)
        {
//*GUI*/msg = "[NOT IN MARGINS]";

        }
        //}}}
//*GUI*/msg = String.format("%s: ...return [%s] .. %s", caller, result, msg);
//*GUI*/Settings.MOM(TAG_GUI, msg);
        return result;
    }
    //}}}
    // }}}
    /** SB Tools */
    // {{{
    // get_sbX_containing_tool_button {{{
    public  NpButton get_sbX_containing_tool_button(NpButton np_button)
    {
        if(np_button == null     ) return null;

        // SCROLLBAR
        if(np_button == sb       ) return  sb ;
        if(np_button == sb2      ) return  sb2;
        if(np_button == sb3      ) return  sb3;

        // WEBVIEW .. (behind moving button x,y)
        if(np_button == fs_search)
        {
            RTabs.MWebView wv = get_wv_containing_np_button( fs_search );
            return              get_sb_for_wv_or_sb( wv );
        }

        return null;
    }
    //}}}
    // get_tool_np_for_name {{{
    private NotePane get_tool_np_for_name(String name)
    {
//GUI//String caller = "get_tool_np_for_name("+name+")";//TAG_GUI
        NotePane np = null;

        np = get_tool_np_for_name(name, TOOL_Map);
        if(np != null) {
//GUI//Settings.MOM(TAG_GUI, caller+": [TOOL_Map] ...return ["+np+"]");
            return np;
        }
        np = get_tool_np_for_name(name, JSNP_Map);
        if(np != null) {
//GUI//Settings.MOM(TAG_GUI, caller+": [JSNP_Map] ...return ["+np+"]");
            return np;
        }
//GUI//Settings.MOM(TAG_GUI, caller+": NOT FOUND IN [TOOL_Map] OR [JSNP_Map] ...return ["+np+"]");
        return np;
   }
    //}}}
    // get_tool_np_for_name {{{
    private NotePane get_tool_np_for_name(String name, LinkedHashMap<String, Object> hashMap)
    {
//GUI//String caller = "get_tool_np_for_name("+name+")";//TAG_GUI
        NotePane np = null;
        // [hashMap] {{{
        for(Map.Entry<String,Object>    entry : hashMap.entrySet())
        {
            if(                       !(entry.getValue() instanceof NotePane) ) continue;
            if( !TextUtils.equals(name, entry.getKey())                       ) continue;

            np = (NotePane)entry.getValue(); if(np == sbX_np) continue;
            if(np.tag == Settings.FREE_TAG)                                        break; // a tool .. (not a TAB-LINK)
            else                                                               np = null; // .. try next
        }
        //}}}
//GUI//Settings.MOM(TAG_GUI, caller+": ...return ["+np+"]");
        return np;
    }
    //}}}
    // get_tool_at_XY {{{
    private  View get_tool_at_XY(int x, int y)
    {
        View     view;
        view = _get_tool_at_XY(x, y, TOOL_Map); if(view != null) return view;
        view = _get_tool_at_XY(x, y, JSNP_Map); if(view != null) return view;
        return null;
    }
    //}}}
    // _get_tool_at_XY {{{
    private View _get_tool_at_XY(int x, int y, LinkedHashMap<String, Object> hashMap)
    {
        for(Map.Entry<String,Object> entry : hashMap.entrySet()) {
            if(                    !(entry.getValue() instanceof NotePane) ) continue;
            NotePane np = (NotePane) entry.getValue(); if(np == sbX_np) continue;
            if( is_view_atXY(np.button,x,y) ) return np.button;
        }
        return null;
    }
    //}}}
    // }}}
    /** FS_SEARCH */
     // {{{
    // {{{
    private static final int REST_ZONE_NONE = 0;
    private static final int REST_ZONE_TOPL = 1;
    private static final int REST_ZONE_TOPR = 2;

    public  boolean was_fs_search_in_a_rest_zone_on_ACTION_DOWN = false;

    //}}}
    //* FS_SEARCH (DISPLAY) */
    // fs_search_show_webView_swap_side {{{
    //{{{
    private static int FS_SEARCH_NP_FLAGGED_TO_WEBVIEW_SWAP_SIDE = 1;
    private        int                 fs_search_np_current_flag = 0;
    private        int                   fs_search_drag_offset_x = 0;
    private        int                   fs_search_drag_offset_y = 0;

    //}}}
    // fs_search_show_webView_swap_side {{{
    public  void fs_search_show_webView_swap_side(int swap_side)
    {
//*FS_SEARCH*/Settings.MOC(TAG_FS_SEARCH, "fs_search_show_webView_swap_side("+swap_side+")");

        if(fs_search == null) return;
        int split_count = activity_get_fs_webView_count();

        String text;
        if(split_count > 2)
            text
                = (swap_side == Settings.SWAP_L) ? Settings.FS_SEARCH_TEXT_1_3
                : (swap_side == Settings.SWAP_C) ? Settings.FS_SEARCH_TEXT_2_3
                : (swap_side == Settings.SWAP_R) ? Settings.FS_SEARCH_TEXT_3_3
                :                                  Settings.FS_SEARCH_TEXT_3  ; // SWAP_0
        else
            text
                = (swap_side == Settings.SWAP_L) ? Settings.FS_SEARCH_TEXT_1_2
                : (swap_side == Settings.SWAP_C) ? Settings.FS_SEARCH_TEXT_2_2
                :                                  Settings.FS_SEARCH_TEXT_2  ; // SWAP_0

        _fs_search_np_setText_and_flag(FS_SEARCH_NP_FLAGGED_TO_WEBVIEW_SWAP_SIDE, text);
    }
    //}}}
    // fs_search_np_setText {{{
    private void fs_search_np_setText(String text)
    {
        _fs_search_np_setText_and_flag(0, text); // (i.e. not WEBVIEW SWAP SIDE oriented)
    }
    //}}}
    // _fs_search_np_setText_and_flag {{{
    private void _fs_search_np_setText_and_flag(int flag, String text)
    {
//*FS_SEARCH*/Settings.MOC(TAG_FS_SEARCH, "_fs_search_np_setText_and_flag("+flag+")", text.replace("\n",""));
        fs_search_np_current_flag = flag;
        fs_search_np.setTextAndInfo( text );
    }
    //}}}
    // is_fs_search_np_flagged_to_webview_swap_side {{{
    public boolean is_fs_search_np_flagged_to_webview_swap_side()
    {
//*FS_SEARCH*/Settings.MOC(TAG_FS_SEARCH, "is_fs_search_np_flagged_to_webview_swap_side", "...return "+(fs_search_np_current_flag == FS_SEARCH_NP_FLAGGED_TO_WEBVIEW_SWAP_SIDE));
        return (fs_search_np_current_flag == FS_SEARCH_NP_FLAGGED_TO_WEBVIEW_SWAP_SIDE);
    }
    //}}}
    // clear_fs_search_np_flagged_to_webview_swap_side {{{
    public void clear_fs_search_np_flagged_to_webview_swap_side()
    {
//*FS_SEARCH*/Settings.MOC(TAG_FS_SEARCH, "clear_fs_search_np_flagged_to_webview_swap_side");
        fs_search_np_current_flag = 0;
    }
    //}}}
    //}}}
    // fs_search_set_colors {{{
    private void fs_search_set_colors(int fg_color, int bg_color)
    {
        if(fs_search != null) {
            fs_search.setTextColor      ( fg_color );
            fs_search.setBackgroundColor( bg_color );
        }
    }
    //}}}
    // fs_search_wake_up {{{
    private void fs_search_wake_up(String caller)
    {
//*GUI*/ caller += "->fs_search_wake_up";//TAG_FS_SEARCH
//*FS_SEARCH*/Settings.MOC(TAG_FS_SEARCH, caller);

        fs_search.setAlpha      ( Settings.NB_WAKE_UP_OPACITY );
        fs_search.setVisibility ( View.VISIBLE );
        fs_search.bringToFront  ();
    }
    //}}}
    // fs_search_to_sleep {{{
    public  void fs_search_to_sleep(NpButton nb, String caller)
    {
//*GUI*/ caller += "] [fs_search_to_sleep("+get_view_name(nb)+")";//TAG_FS_SEARCH
//*FS_SEARCH*/Settings.MOC(TAG_FS_SEARCH, caller);

        nb.setAlpha( Settings.NB_TO_SLEEP_OPACITY );
    }
    //}}}
    // fs_search_hide_if_not_clamping {{{
    public  void fs_search_hide_if_not_clamping()
    {
        if(fs_search == null) return;

        if( !is_clamping_button( fs_search ) )
        {
//*FS_SEARCH*/Settings.MOC(TAG_FS_SEARCH, "fs_search_hide_if_not_clamping", "...fs_search.setVisibility( View.GONE )");
            fs_search.setVisibility( View.GONE );
            fs_search.setScaleX    ( 1f );
            fs_search.setScaleY    ( 1f );
        }
    }
    //}}}
    //* FS_SEARCH (MOVE) */
    // fs_search_park {{{
    public  void fs_search_park(String caller)
    {
        caller += "->fs_search_park";
//*FS_SEARCH*/Settings.MOC(TAG_FS_SEARCH, caller);

        if(fs_search == null) return;

        // SCALE
        fs_search.setScaleX( 1f );
        fs_search.setScaleY( 1f );

        // SHOW [fs_search] IN SCROLLBAR
        if( sb_is_frame_showing() )
        {
//*FS_SEARCH*/Settings.MOC(TAG_FS_SEARCH, caller, "...SHOW [fs_search] IN SCROLLBAR");
            fs_search.setVisibility( View.VISIBLE );
            sb_layout_tools_from_scratch(caller);               // after fs_search parked
        }
        // OR HIDE [fs_search]
        else {
//*FS_SEARCH*/Settings.MOC(TAG_FS_SEARCH, caller, "...HIDE [fs_search]");
            fs_search.setVisibility( View.GONE );
        }

        if( is_fs_search_in_a_rest_zone() )
            fs_search_to_sleep(fs_search_np.button, caller);
    }
    //}}}
    //* FS_SEARCH (CB) */
    // fs_search_CB {{{
    private void fs_search_CB()
    {
        String caller = "fs_search_CB";
//*FS_SEARCH*/Settings.MOC(TAG_FS_SEARCH, caller);

        activity_pulse_np_button( fs_search );

        float x = fs_search.getX() + fs_search.getWidth () / 2;
        float y = fs_search.getY() + fs_search.getHeight() / 2;

        // TEMPORARY HIDE MARKERS .. f(layout change)
        marker_set_someone_has_called_hide_marks(caller);

        // CLIK: (WEBVIEW FULLSCREEN TOGGLE)
      //mRTabs.expand_fs_webView_fullscreen_select_atX(x, true); // NOT toggle-off
        mRTabs.expand_fs_webView_fullscreen_toggle(x);
    }
    //}}}
    //* FS_SEARCH (REST ZONE) */
    // fs_search_show_nearest_rest_zone {{{
    private Rect last_blinked_rest_zone = null;
    private void fs_search_show_nearest_rest_zone(NpButton nb)
    {
        String caller = "fs_search_show_nearest_rest_zone";
//*FS_SEARCH*/Settings.MOC(TAG_FS_SEARCH, "fs_search_show_nearest_rest_zone");
        if(nb == null) return;

        Rect r = get_nearest_rest_zone( nb );

        if(  r.equals(last_blinked_rest_zone) ) return; // blink only once

        // visualize rest zone
        activity_blink_rect(r, (Color.MAGENTA) & 0x80FFFFFF, 0, 250, caller); // delay, duration
        last_blinked_rest_zone = r;
    }
    //}}}
    // is_fs_search_in_a_rest_zone {{{
    public  boolean is_fs_search_in_a_rest_zone()
    {
        // REST_ZONE_TOPR {{{
        Rect r;
        if(((r = get_TOPR_rest_zone(fs_search)) != null) && r.contains((int)fs_search.getX(), (int)fs_search.getY())) {
//*FS_SEARCH*/Settings.MON(TAG_FS_SEARCH, "is_fs_search_in_a_rest_zone", "...return REST_ZONE_TOPR");
            return true;
        }
        //}}}
        // REST_ZONE_TOPL  {{{
        if(((r = get_TOPL_rest_zone(fs_search)) != null) && r.contains((int)fs_search.getX(), (int)fs_search.getY())) {
//*FS_SEARCH*/Settings.MON(TAG_FS_SEARCH, "is_fs_search_in_a_rest_zone", "...return REST_ZONE_TOPL");
            return true;
        }
        //}}}
        // REST_ZONE_NONE  {{{
//*FS_SEARCH*/Settings.MON(TAG_FS_SEARCH, "is_fs_search_in_a_rest_zone", "...return REST_ZONE_NONE");
        return false;
        //}}}
    }
//}}}
    // get_nearest_rest_zone {{{
    public  Rect get_nearest_rest_zone(NpButton nb)
    {
        // button {{{
        int nb_w = nb.getWidth();
        int nb_h = nb.getHeight();

        //}}}
        // rest zone rectangles {{{
        Rect r1 = get_TOPL_rest_zone( nb );
        Rect r2 = get_BOTC_rest_zone( nb );
        Rect r3 = get_TOPR_rest_zone( nb );

        //}}}
        // does one of them contains [nb] already ? {{{
        // button
        int nb_l = (int)nb.getX();
        int nb_t = (int)nb.getY();

        Rect  r = null;
        if( r1.contains(nb_l, nb_t) ) r = r1;
        if( r2.contains(nb_l, nb_t) ) r = r2;
        if( r3.contains(nb_l, nb_t) ) r = r3;

        //}}}
        // if not pick the one with the nearest center {{{
        if(r == null)
        {
            // button center
            int button_center_x = nb_l + nb_w / 2;
            int button_center_y = nb_t + nb_h / 2;

            // [r1] left area center
            int r1_center_x     = (r1.left + r1.right ) / 2;
            int r1_center_y     = (r1.top  + r1.bottom) / 2;
            int dx              = button_center_x - r1_center_x;
            int dy              = button_center_y - r1_center_y;
            int r1_dist         = (int)Math.sqrt((double)(dx*dx + dy*dy));

            // [r2] center area center
            int r2_center_x     = (r2.left + r2.right ) / 2;
            int r2_center_y     = (r2.top  + r2.bottom) / 2;
            dx                  = button_center_x - r2_center_x;
            dy                  = button_center_y - r2_center_y;
            int r2_dist         = (int)Math.sqrt((double)(dx*dx + dy*dy));

            // [r3] right area center
            int r3_center_x     = (r3.left + r3.right ) / 2;
            int r3_center_y     = (r3.top  + r3.bottom) / 2;
            dx                  = button_center_x - r3_center_x;
            dy                  = button_center_y - r3_center_y;
            int r3_dist         = (int)Math.sqrt((double)(dx*dx + dy*dy));

            // pick the short one
            r =   (r1_dist < r2_dist)           // .... r1 or r2  (which is the nearest)
                ? (r1_dist < r3_dist) ? r1 : r3 // ...against r3
                : (r2_dist < r3_dist) ? r2 : r3;// ...against r3
        }
        //}}}
        // log {{{
//*FS_SEARCH*/String result = (r==r1)                                              //TAG_FS_SEARCH
//*FS_SEARCH*/    /**/            ? "top-left"                                     //TAG_FS_SEARCH
//*FS_SEARCH*/    /**/            : (r==r2)                                        //TAG_FS_SEARCH
//*FS_SEARCH*/    /**/                  ? "bot-center"                             //TAG_FS_SEARCH
//*FS_SEARCH*/    /**/                  : (r==r3)                                  //TAG_FS_SEARCH
//*FS_SEARCH*/    /**/                        ? "top-right"                        //TAG_FS_SEARCH
//*FS_SEARCH*/    /**/                        : (r1==null) ? "null" : r.toString();//TAG_FS_SEARCH
//*FS_SEARCH*/Settings.MOM(TAG_FS_SEARCH, "get_nearest_rest_zone("+ get_view_name(nb) +"): ...r=["+result+"]");
        return r;
        //}}}
    }
    //}}}
    // get_TOPL_rest_zone {{{
    private Rect topl_r = new Rect();
    private Rect get_TOPL_rest_zone(NpButton nb)
    {
        if(nb == null) return null;

        // TOPL RECT .. f(nb dimensions)

        int   nb_w = nb.getWidth ();
        int   nb_h = nb.getHeight();
        int  size = (int)(nb_w * 1.5);

        topl_r.top    = 0;
        topl_r.left   = 0;
        topl_r.right  = topl_r.left + size + 1; // (for Rect.contains to cooperate)
        topl_r.bottom = topl_r.top  + size + 1;

//*FS_SEARCH*/Settings.MON(TAG_FS_SEARCH, "get_TOPL_rest_zone", "...return "+topl_r);
        return topl_r;
    }
    //}}}
    // get_BOTC_rest_zone {{{
    private Rect botc_r = new Rect();
    private Rect get_BOTC_rest_zone(NpButton nb)
    {
        if(nb == null) return null;

        // BOTC RECT .. f(nb dimensions)
        int   s_w = Settings.SCREEN_W;
        int   s_h = Settings.SCREEN_H;
        int   nb_w = nb.getWidth ();
        int   nb_h = nb.getHeight();
        int  size = (int)(nb_w * 1.5);

        botc_r.top    = s_h    - size;
        botc_r.left   = s_w/2  - size / 2;
        botc_r.right  = botc_r.left + size + 1; // (for Rect.contains to cooperate)
        botc_r.bottom = botc_r.top  + size + 1;

//*FS_SEARCH*/Settings.MON(TAG_FS_SEARCH, "get_BOTC_rest_zone", "...return "+botc_r);
        return botc_r;
    }
    //}}}
    // get_TOPR_rest_zone {{{
    private Rect topr_r = new Rect();
    private Rect get_TOPR_rest_zone(NpButton nb)
    {
        if(nb == null) return null;

        // TOPR RECT .. f(nb dimensions)
        int   s_w = Settings.SCREEN_W;
        int   nb_w = nb.getWidth ();
        int   nb_h = nb.getHeight();
        int  size = (int)(nb_w * 1.5);

        topr_r.top    = 0;
        topr_r.left   = s_w    - size    ;
        topr_r.right  = topr_r.left + size + 1; // (for Rect.contains to cooperate)
        topr_r.bottom = topr_r.top  + size + 1;

//*FS_SEARCH*/Settings.MON(TAG_FS_SEARCH, "get_TOPR_rest_zone", "...return "+topr_r);
        return topr_r;
    }
    //}}}
//    // get_hmap_np_at_xy_closest {{{
//    private NotePane get_hmap_np_at_xy_closest(HashMap<String, Object> hashMap, int x,int y, String caller)
//    {
//        return this_RTabsClient.get_hmap_np_at_xy_closest(hashMap, x,y, caller);
//    }
//    //}}}
    // }}}
    /** ATTRIBUTES */
    //* {{{
    //* wvTools */
    // has_view {{{
    public  boolean has_view(View view, String caller)
    {
        // search prominent ones first {{{
//*GUI*/caller += "->has_view("+get_view_name(view)+")";//TAG_GUI

        String result
            =   (view ==       sb ) ? "sb"
            :   (view ==       sb2) ? "sb2"
            :   (view ==       sb3) ? "sb3"
            :   (view == fs_search) ? "fs_search"
            :    null;

        //}}}
        if((result == null) && (view instanceof NpButton))
        {
            NpButton np_button = (NpButton)view;
            if((result == null) && is_a_flag  (np_button)) result = "is_a_flag_name";
            if((result == null) && is_a_lock  (np_button)) result = "is_a_lock_name";
          //if((result == null) && is_a_tool  (np_button)) result = "is_a_tool";
            if((result == null) && in_TOOL_Map(np_button)) result = "in_TOOL_Map";
            if((result == null) && in_JSNP_Map(np_button)) result = "in_JSNP_Map";
          //if((result == null) && is_a_marker(np_button)) result = "is_a_marker";
            if((result == null) && in_MARK_Map(np_button)) result = "in_MARK_Map";
        }
//*GUI*/Settings.MOC(TAG_GUI, caller, "...return "+(result != null)+" ["+ result +"]");
        return (result != null);
    }
    //}}}
    //* TOOL */
    // in_TOOL_Map {{{
    private boolean in_TOOL_Map(NpButton np_button)
    {
        for(Map.Entry<String,Object> entry : TOOL_Map.entrySet()) {
            if(                    !(entry.getValue() instanceof NotePane) ) continue;
            NotePane np = (NotePane) entry.getValue(); if(np == sbX_np) continue;
            if(np.button == np_button) return true;
        }
        return false;
    }
    //}}}
    // in_JSNP_Map {{{
    private boolean in_JSNP_Map(NpButton np_button)
    {
        for(Map.Entry<String,Object> entry : JSNP_Map.entrySet()) {
            if(                    !(entry.getValue() instanceof NotePane) ) continue;
            NotePane np = (NotePane) entry.getValue(); if(np == sbX_np) continue;
            if(np.button == np_button) return true;
        }
        return false;
    }
    //}}}
    // in_MARK_Map {{{
    private boolean in_MARK_Map(NpButton np_button)
    {
        for(Map.Entry<String,Object> entry : MARK_Map.entrySet()) {
            if(                    !(entry.getValue() instanceof NotePane) ) continue;
            NotePane np = (NotePane) entry.getValue(); if(np == sbX_np) continue;
            if(np.button == np_button) return true;
        }
        return false;
    }
    //}}}
    //* name */
    // is_a_(name tag np_button np view) {{{

    // is_a_(name)
    private static  boolean is_a_flag_name  (String   name     ) { return name.contains  ( CHECK_FLAG_SYMBOL ); }
    private static  boolean is_a_lock_name  (String   name     ) { return name.contains  ( CHECK_LOCK_SYMBOL ); }
    private static  boolean is_a_marker_name(String   name     ) { return name.startsWith( WV_TOOL_mark      ); }

    // is_a_(tag)
    public  static  boolean is_a_tool_tag   (String   tag      ) {
        return (Settings.FREE_TAG          == tag )
            ||  RTabs   .is_ACTIVITY_BUILTIN( tag )
            ||  Settings.is_SETTINGS_BUILTIN( tag )
            ||  Profile .is_PROFILES_BUILTIN( tag )
            ;
    }

    // is_a_(np_button)
    private static  boolean is_a_flag         (NpButton np_button) { NotePane np = get_np_button_np( np_button ); return (np != null) && is_a_flag_name  ( np.name ); }
    private static  boolean is_a_lock         (NpButton np_button) { NotePane np = get_np_button_np( np_button ); return (np != null) && is_a_lock_name  ( np.name ); }
    public  static  boolean is_a_marker       (NpButton np_button) { NotePane np = get_np_button_np( np_button ); return (np != null) && is_a_marker_name( np.name ); }
    private static  boolean is_a_tool         (NpButton np_button) { NotePane np = get_np_button_np( np_button ); return (np != null) && is_a_tool_tag   ( np.tag  ); }

    // is_a_(np)
    public  static  boolean is_a_marker       (NotePane np       ) { return (np != null) && is_a_marker( np.button ); }
    private static  boolean is_a_pinned_marker(NotePane np       ) { return is_a_marker( np ) && (np.name.length() > WV_TOOL_mark.length()); }// .. (WV_TOOL_mark + mark_id)

    // is_a_(view)
    public  static  boolean is_a_marker       (View     view     ) { return (view != null) && (view instanceof NpButton) && is_a_marker( (NpButton)view ); }
    public  static  boolean is_a_tool         (View     view     ) { return (view != null) && (view instanceof NpButton) && is_a_tool  ( (NpButton)view ); }

    // is_a_(marker)
    public          boolean is_a_spread_marker(View     view     ) { return is_a_marker( view ) && is_marker_np_button_spread((NpButton)view); }

    // [np]<-[np_button]
    private static NotePane get_np_button_np(NpButton np_button)
    {
        Object        o               = np_button.getTag();
        if(           o              == null             ) return null;
        if(         !(o      instanceof NotePane)        ) return null;
        NotePane np = (NotePane)o; /*...................*/ return   np;
    }
    //}}}
    //* gui */
    // is_a_silent_button {{{
    public boolean is_a_silent_button(NpButton np_button)
    {
        if( property_get(WV_TOOL_silent) ) return  true; // all buttons silenced
        if(       sb   == np_button      ) return  true; // silence scrollbars
        if(       sb2  == np_button      ) return  true; // silence scrollbars
        if(       sb3  == np_button      ) return  true; // silence scrollbars
        if( is_a_marker ( np_button )    ) return  true; // silence markers
        if( is_a_flag   ( np_button )    ) return false;
        if( is_a_lock   ( np_button )    ) return false;
        if( is_a_tool   ( np_button )    ) return false;
        /*..............................*/ return false; // ...can't say, that's not a WVTools button
    }
    //}}}
    //* color */
    // get_bg_color_for_name {{{
    private int get_bg_color_for_name(String name)
    {
        return    RTabs.is_ACTIVITY_BUILTIN( name ) ? Settings.WV_TOOL_ACTIVITY_COLOR
            :  Settings.is_SETTINGS_BUILTIN( name ) ? Settings.WV_TOOL_SETTINGS_COLOR
            :   Profile.is_PROFILES_BUILTIN( name ) ? Settings.WV_TOOL_PROFILES_COLOR
            :   WVTools.is_a_marker_name   ( name ) ? MARKER_COLORS[1] // (ECC 1-based)
            :   WVTools.is_a_flag_name     ( name ) ? FLAG_BG_COLOR
            :   WVTools.is_a_lock_name     ( name ) ? LOCK_BG_COLOR
            :                                         TOOL_BG_COLOR
            ;
    }
    //}}}
    //* shape */
    // get_shape_for_name {{{
    private String get_shape_for_name(String name)
    {
        //turn is_a_marker_name( name ) ? NotePane.SHAPE_TAG_MARK_L // MARK  WTOOLS BUTTON
        //  :                             NotePane.SHAPE_TAG_SATIN  // OTHER WTOOLS BUTTON
        //  ;
        return    RTabs.is_ACTIVITY_BUILTIN( name ) ? NotePane.SHAPE_TAG_ONEDGE // NotePane.SHAPE_TAG_CIRCLE
            :  Settings.is_SETTINGS_BUILTIN( name ) ? NotePane.SHAPE_TAG_ONEDGE // NotePane.SHAPE_TAG_CIRCLE
            :   Profile.is_PROFILES_BUILTIN( name ) ? NotePane.SHAPE_TAG_ONEDGE // NotePane.SHAPE_TAG_CIRCLE
            :   WVTools.is_a_marker_name   ( name ) ? NotePane.SHAPE_TAG_ONEDGE // NotePane.SHAPE_TAG_MARK_L
            :   WVTools.is_a_flag_name     ( name ) ? NotePane.SHAPE_TAG_ONEDGE // NotePane.SHAPE_TAG_SATIN
            :   WVTools.is_a_lock_name     ( name ) ? NotePane.SHAPE_TAG_ONEDGE // NotePane.SHAPE_TAG_SATIN
            :                                         NotePane.SHAPE_TAG_ONEDGE // NotePane.SHAPE_TAG_SATIN
            ;
    }
    //}}}
    // get_marker_shape_for_current_display_orientation {{{
    private String get_marker_shape_for_current_display_orientation(boolean at_left)
    {
      //return (Settings.DISPLAY_ASPECT == Configuration.ORIENTATION_LANDSCAPE)
      //    ?   NotePane.SHAPE_TAG_MARK_L
      //    :   NotePane.SHAPE_TAG_MARK_P;
        return at_left ? NotePane.SHAPE_TAG_PADD_R : NotePane.SHAPE_TAG_PADD_L;
    }
    //}}}
    //* spread */
    // is_marker_np_button_spread {{{
    private boolean is_marker_np_button_spread(NpButton np_button)
    {
      //return (np_button.shape != NotePane.SHAPE_TAG_MARK_L) // LANDSCAPE NOT [SPREAD]
      //    && (np_button.shape != NotePane.SHAPE_TAG_MARK_P) // PORTRAIT  NOT [SPREAD]
      //    ;
        return np_button.is_spread();
    }
    //}}}
    // }}}
    /** MARKERS */
    //{{{
    //{{{
    private static       String MARKER_TAG_PREFIX = Settings.FREE_TAG +" ";
    private static final  String COOKIE_PARAM_MARK           = "mark";
    private static final  String COOKIE_PARAM_NOTE           = "note";
    private static           int SB_THUMB_ON_MARKER_DY_MAX   = Settings.TAB_MARK_H / 2;

    // }}}
    //* MARKERS (CREATE) {{{ */
    // create_mark {{{
    private NpButton create_mark(String url, String wv_slice, int mark_id)
    {
        // [NEXT FREE mark_id] .. f(clones) {{{
        String caller = "create_mark(wv_slice=["+wv_slice+"], mark_id=["+mark_id+"])";

        mark_id = marker_get_free_slice_mark_id(wv_slice, mark_id); // (ECC 1-based)
//*MK0_MARK*/Settings.MON(TAG_MK0_MARK, caller, "NEXT FREE mark_id=["+mark_id+"]");

        // [create_np] .. [store] {{{
        String  name = WV_TOOL_mark + mark_id; // [1 .. MARKERS_MAX]
        String   tag = marker_get_np_tag_from_url_mark_id(url, ""+mark_id);
        String shape = NotePane.SHAPE_TAG_CIRCLE;
        String  text = WV_TOOL_mark_text;
        if(mark_id>1)
            text += " "+Settings.Get_DIGIT_SYMBOL(mark_id); // add the one not found .. (after an effective iteration)

        int bg_color = MARKER_COLORS[(mark_id) % MARKER_COLORS.length];
        int fg_color = (ColorPalette.GetBrightness( bg_color ) < 128) ? Color.WHITE : Color.BLACK;

        NotePane  np = create_np(name, text, tag, shape, bg_color, fg_color, Settings.TOOL_BADGE_SIZE, Settings.TAB_MARK_H);
        np.setTextAndInfo(name + NotePane.INFO_SEP + tag);

        marker_map(wv_slice, ""+mark_id, np);

        //}}}
//*MK0_MARK*/Settings.MON(TAG_MK0_MARK, caller, "...return np.name=["+np.name+"]");
        return np.button;
    }
    //}}}
    // add_marker_for_text {{{
    public  void add_marker_for_text(String text)
    {
        // [sbX] .. (must be opened for marker creation from clipboard change) {{{
        String caller = "add_marker_for_text("+text+")";
//*MK0_MARK*/Settings.MOC(TAG_MK0_MARK, caller);

        NpButton sbX
            = (gesture_down_sb != null)
            ?  gesture_down_sb
            :  get_sb_for_wv_or_sb(gesture_down_wv);
//*MK0_MARK*/Settings.MOM(TAG_MK0_MARK, "..................sbX=["+ get_view_name(sbX)   +"]");
        if(sbX == null) return;

//*MK0_MARK*/Settings.MOM(TAG_MK0_MARK, "............sbX.shape=["+ sbX.shape            +"]");
        //}}}
        // 1/2 [floating_marker_np] .. (relabelling from clipboard)
        // 2/2 [create_mark]        .. f(scrollbar should be deployed to add a maker)
        //{{{
//*MK0_MARK*/Settings.MOM(TAG_MK0_MARK, "...is_floating_marker=["+ is_floating_marker() +"]");

        boolean ready_for_a_new_marker_from_clipboard
            =   sb_is_frame_showing(sbX)
            ||  is_floating_marker()
            ;
//*MK0_MARK*/Settings.MOM(TAG_MK0_MARK, "ready_for_a_new_marker_from_clipboard=["+ready_for_a_new_marker_from_clipboard+"]");

        if(ready_for_a_new_marker_from_clipboard)
        {
            handle_marker_to_add(null, text);
        }
        //}}}
    }
    //}}}
    // handle_marker_to_add {{{
    private void handle_marker_to_add(RTabs.MWebView wv, String text)
    {
        // DOUBLE CLIPBOARD CALL WORKAROUNG {{{
             String caller = "handle_marker_to_add";
//*MK0_MARK*/       caller = "handle_marker_to_add("+get_view_name(wv)+", text=["+text+"])";//TAG_MK0_MARK
        if((text != null) && TextUtils.equals(last_marked_text_since_onDown, text))
        {
//*MK0_MARK*/Settings.MOC(TAG_MK0_MARK, caller, "DISCARDING .. (SAME AS LAST ADDED MARK)");
            return;
        }
        last_marked_text_since_onDown = text;

        //}}}
        // create_mark<-[wv]+[text] [available marker_id]  // {{{

        if(wv == null) wv = gesture_down_wv;
        if(wv == null) return;

        NpButton sbX = get_sb_for_wv_or_sb(wv);
        //}}}
        // [marker_np] .. (relabel floating_marker_np) {{{
        NotePane marker_np;
        if( is_floating_marker() )
        {
            marker_np = floating_marker_np;
            if( TextUtils.equals(marker_np.getLabel().trim(), text) )
            {
//*MK0_MARK*/Settings.MOC(TAG_MK0_MARK, caller, "SAME LABEL: floating_marker_np=["+ marker_np +"]");
                return;
            }
//*MK0_MARK*/Settings.MOC(TAG_MK0_MARK, caller, "RELABELLING floating_marker_np=["+ marker_np +"]");
        }
        //}}}
        // OR (create_mark) {{{
        else {
            marker_np = marker_get_np_at_Y( (int)sbX.getY() );
            if(marker_np != null)
            {
//*MK0_MARK*/Settings.MOC(TAG_MK0_MARK, caller, "SAME NEAR SCROLLBAR: marker_np=["+ marker_np +"]");
                return;
            }
//*MK0_MARK*/Settings.MOC(TAG_MK0_MARK, caller, "ADDING NEW MARKER");
            String         url = wv_getUrl(wv);
            int             id = 1;
            String    wv_slice = marker_get_wv_slice( wv );

            NpButton np_button = create_mark(url, wv_slice, id);
            marker_np = (NotePane)np_button.getTag();
//*MK0_MARK*/Settings.MOC(TAG_MK0_MARK, caller, "...NEW marker_np=["+ marker_np +"]");
        }
        // }}}
        // (text == null)<=(last_cut_marker_text] {{{
        if((text == null) && (last_cut_marker_text != null))
        {
            text = last_cut_marker_text; // (trashed remains from an unpinned marker)
            marker_empty_trash();
        }
        // [marker_np.text] .. (cookie note param)
        if(text != null)
        {
            marker_add_cookie_note(marker_np, text);
        }
        else {
            // use a default note param as the number of the available free slot just created
            String mark_id = marker_get_mark_id_from_button_name( marker_np.button );
            marker_add_cookie_note(marker_np, mark_id);
        }
        //}}}
        // [TEXT ECC COLOR] {{{
        if(text != null)
        {
            int bg_color = Settings.Get_ECC_color_for_text(text, caller);
            int fg_color = (ColorPalette.GetBrightness( bg_color ) < 128) ? Color.WHITE : Color.BLACK;
            marker_np.button.setBackgroundColor( bg_color );
            marker_np.button.setTextColor      ( fg_color );
        }
        //}}}
        // todo: add offset from [wv-top] to [text-selection] (bring selection to the top)
        // [cookie] .. (add new) (update floating) {{{
        float thumb_p = marker_add_cookie_mark(wv, marker_np);

        //}}}
        // [show] [scroll-to] [select current] {{{
        if(thumb_p >= 0)
        {
            // show [marker_np]
            String      shape  = get_marker_shape_for_current_display_orientation( !sbX.at_left );
            marker_np.button.set_shape(shape ,true); // with_outline

            marker_pin_mark_at_thumb_p(wv, marker_np, !sbX.at_left, thumb_p);

            // scroll-to [thumb_p]
            marker_scroll_wv_to_thumb_p(wv, thumb_p);
        }
        //}}}
        // [marker_unwrap] .. (is_floating_marker) {{{
        if( is_floating_marker( marker_np ) )
        {
            marker_unwrap( marker_np );
            marker_float_DRAGLOCK(marker_np, true, caller); // back_lock
        }
        //}}}
    }
     //}}}
    // marker_add_cookie_mark {{{
    private float marker_add_cookie_mark(RTabs.MWebView wv, NotePane marker_np)
    {
        // [thumb_p]<-[wv] {{{
//*MK1_COOK*/String caller = "marker_add_cookie_mark("+get_view_name(wv)+", "+marker_np+")";//TAG_MK1_COOK
//*MK1_COOK*/Settings.MOC(TAG_MK1_COOK, caller);

        if((wv == null) || (wv_getUrl(wv) == null )) return -1; // (no url => no marker)

        float  wv_page_height = Math.max(wv.computeVerticalScrollRange (), 1f);
        float     wv_page_top = Math.max(wv.computeVerticalScrollOffset(), 0f);
        float         thumb_p = 100f * wv_page_top / wv_page_height; // what's hidden above wv current sroll-top

        String        mark_id = marker_get_mark_id_from_button_name( marker_np.button );

        String        cookie_val = String.format("%.4f", marker_get_cookie_thumb_p(wv, mark_id ));
        String    new_cookie_val = String.format("%.4f", thumb_p);

//*MK1_COOK*/Settings.MOM(TAG_MK1_COOK, "...wv_page_top=["+ wv_page_top    +"]");
//*MK1_COOK*/Settings.MOM(TAG_MK1_COOK, "wv_page_height=["+ wv_page_height +"]");
//*MK1_COOK*/Settings.MOM(TAG_MK1_COOK, "....cookie_val=["+ cookie_val     +"]");
//*MK1_COOK*/Settings.MOM(TAG_MK1_COOK, "new_cookie_val=["+ new_cookie_val +"]");
        //}}}
        // (new_cookie_val==cookie_val) {{{
        if( TextUtils.equals(new_cookie_val, cookie_val) )
        {
//*MK1_COOK*/Settings.MOM(TAG_MK1_COOK, "[set_cookie_param] NOT REQUIRED .. (new_cookie_val==cookie_val)");

        }
        //}}}
        // [set_cookie_param] .. [cookie_key]-[new_cookie_val]<-[thumb_p] {{{
        else {
            String cookie_key = marker_get_cookie_key_from_wv_mark_id(      wv, mark_id );
//*MK1_COOK*/Settings.MOM(TAG_MK1_COOK, "...calling set_cookie_param(cookie_key=["+cookie_key+"], ...)");

            Settings.set_cookie_param(Settings.LOCALHOST_URL, cookie_key, COOKIE_PARAM_MARK, new_cookie_val);
        }
        //}}}
        // return [thumb_p] .. (to pin marker to) {{{
//*MK1_COOK*/Settings.MOM(TAG_MK1_COOK, caller+": ...return thumb_p=["+thumb_p+"]");

        return thumb_p;
        //}}}
    }
    //}}}
    // marker_add_cookie_note {{{
    public  void marker_add_cookie_note(NotePane marker_np, String text_and_info)
    {
//*MK1_COOK*/String caller = "marker_add_cookie_note("+marker_np+", ["+text_and_info+"])";//TAG_MK1_COOK
//*MK1_COOK*/Settings.MOC(TAG_MK1_COOK, caller);
        // [setTextAndInfo] {{{
        _marker_np_setTextAndInfo(marker_np, text_and_info);

        //}}}
        // [cookie_key]<-[marker_np.tag] {{{
        String cookie_key = _marker_get_cookie_key_from_marker_np_tag( marker_np );
//*MK1_COOK*/Settings.MOM(TAG_MK1_COOK, "cookie_key=["+cookie_key+"]");
        if(cookie_key == null) {
            return;

        }
        //}}}
        // [cookie_val]<-[marker_np.text] {{{
        String cookie_val = text_and_info.trim();
//*MK1_COOK*/Settings.MOM(TAG_MK1_COOK, "cookie_val=["+cookie_val+"]");

        // }}}
        // COOKIE_PARAM_NOTE: [cookie_key]-[cookie_val] {{{
        Settings.set_cookie_param(Settings.LOCALHOST_URL, cookie_key, COOKIE_PARAM_NOTE, cookie_val);

        // }}}
    }
    //}}}
    // _marker_get_cookie_key_from_marker_np_tag {{{
    private static String _marker_get_cookie_key_from_marker_np_tag(NotePane marker_np)
    {
        if( marker_np.tag.startsWith(MARKER_TAG_PREFIX) ) return marker_np.tag.substring( MARKER_TAG_PREFIX.length() );
        else                                              return null;
    }
    //}}}
    //}}}
    //* MARKERS (COOKIE) {{{ */
    // marker_del_cookie {{{
    private void marker_del_cookie(RTabs.MWebView wv, NotePane marker_np)
    {
//*MK1_COOK*/Settings.MOM(TAG_MK1_COOK, "_marker_del_cookie("+get_view_name(wv)+","+ marker_np+")");

        // URL
        if((wv == null) || (wv_getUrl(wv) == null)) return; // cannot build a cookie_key

        // cookie key
        String    mark_id = marker_get_mark_id_from_button_name( marker_np.button );
        String cookie_key = marker_get_cookie_key_from_wv_mark_id(wv, mark_id);

    //  String cookie_val = "";

        // delete cookie .. (erase by storing an empty value)
        Settings.del_cookie(Settings.LOCALHOST_URL, cookie_key);
    }
    //}}}
    // marker_load_np_from_cookie {{{
    private void marker_load_np_from_cookie(NotePane marker_np)
    {
//*MK1_COOK*/String caller = "marker_load_np_from_cookie("+marker_np.name+")";//TAG_MK1_COOK

        String mark_id = marker_get_mark_id_from_button_name( marker_np.button );
//*MK1_COOK*/Settings.MOC(TAG_MK1_COOK, caller, "...mark_id=["+mark_id+"]");

        marker_load_mark_id_from_cookie(gesture_down_wv, mark_id, !is_sb_at_left());
    }
    //}}}
    // marker_load_markers_from_cookie {{{
    private void marker_load_markers_from_cookie(RTabs.MWebView wv, boolean at_left)
    {
        String caller = "marker_load_markers_from_cookie";
//*MK1_COOK*/Settings.MOC(TAG_MK1_COOK, caller+"("+get_view_name(wv)+", at_left=["+at_left+"])");
//*MK1_COOK*/Settings.MOM(TAG_MK1_COOK, "...CURRENT MARK_Map.size()=["+MARK_Map.size() +" MARKS]");

        // LOAD WEBVIEW MARKERS FROM COOKIE
        for(int j=1; j < MARKERS_MAX; ++j)
        {
            marker_load_mark_id_from_cookie(wv, ""+j, at_left);
        }

        // LOAD FLOATING MARKER FROM COOKIE
        if(floating_marker_np != null)
            marker_load_np_from_cookie( floating_marker_np );

//MK1_COOK//Settings.MOM(TAG_MK1_COOK, caller+": ....NEW MARK_Map.size()=["+MARK_Map.size() +" MARKS]");
    }
    //}}}
    // marker_load_mark_id_from_cookie {{{
    private void marker_load_mark_id_from_cookie(RTabs.MWebView wv, String mark_id, boolean at_left)
    {
//*MK1_COOK*/String caller = "marker_load_mark_id_from_cookie("+get_view_name(wv)+", mark_id=["+ mark_id +"])";//TAG_MK1_COOK
        // marker_get_cookie_thumb_p {{{
//*MK1_COOK*/Settings.MOM(TAG_MK1_COOK, caller);
        float thumb_p = marker_get_cookie_thumb_p(wv, mark_id);
        if(thumb_p < 0) {
//MK1_COOK//Settings.MOM(TAG_MK1_COOK, caller+": ...return");
            return;
        }
        //}}}
        // [marker_np]<-[wv] {{{

        String    wv_slice = marker_get_wv_slice( wv );
        NotePane marker_np = marker_get_np_for_wv_slice_mark_id(wv_slice, ""+mark_id);

        //}}}
        // create_mark<-[wv] [marker_id] {{{
        if(marker_np == null)
        {
//*MK1_COOK*/Settings.MOM(TAG_MK1_COOK, "THUMB-MARKER FROM COOKIE");
            String url = wv_getUrl(wv);
            int     id = 1;
            try {   id = Integer.parseInt(mark_id); } catch(Exception ignored) {}
            NpButton np_button = create_mark(url, wv_slice, id);
            String      shape  = get_marker_shape_for_current_display_orientation( at_left );
            np_button.set_shape(shape ,true); // with_outline

            marker_np          = (NotePane)np_button.getTag();
        }
        //}}}
        // re-use [marker_np]<-[tag]..f(url+mark_id) [text]..f(name) [shape]..f(orientation) {{{
        else {
            marker_np.tag = marker_get_np_tag_from_url_mark_id(wv_getUrl(wv), mark_id);

            String shape  = get_marker_shape_for_current_display_orientation( at_left );
            marker_np.button.set_shape(shape ,true); // with_outline

        }
        //}}}
        // [text_and_info] {{{
        String text_and_info = marker_get_cookie_note(wv, mark_id);
//*MK1_COOK*/Settings.MOM(TAG_MK1_COOK, "text_and_info=["+ text_and_info +"]");
        if(text_and_info != null)
            _marker_np_setTextAndInfo(marker_np, text_and_info);
        else
            _marker_np_setTextAndInfo(marker_np, marker_np.name + NotePane.INFO_SEP + marker_np.tag);

        //}}}
        // DISPLAY [thumb layout] .. f(wv, marker_np, at_left, thumb_p) {{{
        marker_pin_mark_at_thumb_p(wv, marker_np, at_left, thumb_p);

        //}}}
//*MK1_COOK*/Settings.MOM(TAG_MK1_COOK, caller+" ...done");
    }
    //}}}
    // marker_get_cookie_key_from_wv_mark_id {{{
    private String marker_get_cookie_key_from_wv_mark_id(WebView wv, String mark_id)
    {
        String    cookie_key = _marker_get_cookie_key_from_url_mark_id(wv_getUrl(wv), mark_id, false); // !vertical

//MK1_COOK//Settings.MOM(TAG_MK1_COOK, "marker_get_cookie_key_from_wv_mark_id("+ get_view_name(wv) +", "+mark_id+") ...return ["+cookie_key+"]");
        return cookie_key;
    }
    //}}}
    // marker_get_cookie_note {{{
    private String marker_get_cookie_note(RTabs.MWebView wv, String mark_id)
    {
        // [wv] [url] {{{
//*MK1_COOK*/String caller = "marker_get_cookie_note("+get_view_name(wv)+", mark_id=["+ mark_id +"])";//TAG_MK1_COOK
        if((wv == null) || (wv_getUrl(wv) == null))
        {
//MK1_COOK//Settings.MOM(TAG_MK1_COOK, caller+": ((wv == null) || (wv_getUrl(wv) == null)): ...return -1f");

            return Settings.EMPTY_STRING;
        }
        //}}}
        // Settings.get_cookie_param<-[cookie_val]<-[cookie_key] {{{

        String cookie_key = marker_get_cookie_key_from_wv_mark_id(wv, mark_id);
        String cookie_val = Settings.get_cookie_param(Settings.LOCALHOST_URL, cookie_key, COOKIE_PARAM_NOTE);

//MK1_COOK//if(cookie_key != null) Settings.MOM(TAG_MK1_COOK, "...cookie_val=["+cookie_val+"]");
        //}}}
        // [note]<-[cookie_val] {{{
        String note = cookie_val;

//*MK1_COOK*/Settings.MOM(TAG_MK1_COOK, caller+": note=["+note+"]");
        //}}}
        // return [note] {{{
//*MK1_COOK*/Settings.MOM(TAG_MK1_COOK, caller+": note=["+note+"] .. f(cookie_val)");

        return note;
        //}}}
    }
    //}}}
    // marker_get_np_tag_from_url_mark_id {{{
    private static String marker_get_np_tag_from_url_mark_id(String url, String mark_id)
    {
        return MARKER_TAG_PREFIX+ _marker_get_cookie_key_from_url_mark_id(url, mark_id, false); // !vertical
    }
    //}}}
    // marker_get_mark_id_from_button_name {{{
    private String marker_get_mark_id_from_button_name(NpButton marker_np_button)
    {
        // mark_id .. f(marker_np_button) {{{
        String mark_id         = ""; // .. (as a fallback default)
        if(marker_np_button != null)
        {
            Object           o = marker_np_button.getTag(); // use: (NotePane<=NpButton backward link)
            if(o instanceof NotePane) {
                NotePane    np = (NotePane)o;
                try {  mark_id = np.name.substring( WV_TOOL_mark.length() ); } catch(Exception ignored) {}
            }
            if( TextUtils.isEmpty(mark_id) ) mark_id = "1"; // first marker may have no number suffix
        }
        //}}}
//*MK1_COOK*/Settings.MOM(TAG_MK1_COOK, "marker_get_mark_id_from_button_name("+marker_np_button+"): ...return mark_id=["+mark_id+"]");
        return mark_id;
    }
    //}}}
    // marker_get_cookie_pfx_from_url {{{
    private static String marker_get_cookie_pfx_from_url(String url)
    {
        String url_keywords = Settings.Parse_keywords_from_url( url ).replace(" ", "_");
        String cookie_pfx   = Settings.FREE_TAG +" "+ url_keywords;
        return cookie_pfx;
    }
    //}}}
    // marker_get_mark_id_from_cookie_key {{{
    private String marker_get_mark_id_from_cookie_key(String cookie_key)
    {
        String      mark_id = "";
        int             idx = cookie_key.lastIndexOf(       WV_TOOL_mark          );
        if(idx > 0) mark_id = cookie_key.  substring( idx + WV_TOOL_mark.length() );
        if( TextUtils.isEmpty(mark_id) )    mark_id = "1";
//*MK1_COOK*/Settings.MOM(TAG_MK1_COOK, "marker_get_mark_id_from_cookie_key("+cookie_key+"): ...return mark_id=["+mark_id+"]");
        return mark_id;
    }
    //}}}
    // marker_get_cookie_thumb_p_ {{{
    private float marker_get_cookie_thumb_p(RTabs.MWebView wv, String mark_id)
    {
        // [wv] [url] {{{
//*MK1_COOK*/String caller = "marker_get_cookie_thumb_p("+get_view_name(wv)+", mark_id=["+ mark_id +"])";//TAG_MK1_COOK
        if((wv == null) || (wv_getUrl(wv) == null))
        {
//MK1_COOK//Settings.MOM(TAG_MK1_COOK, caller+": ((wv == null) || (wv_getUrl(wv) == null)): ...return -1f");

            return -1f;
        }
        //}}}
        // Settings.get_cookie_param<-[cookie_val]<-[cookie_key] {{{

        String cookie_key = marker_get_cookie_key_from_wv_mark_id(wv, mark_id);
        String cookie_val = Settings.get_cookie_param(Settings.LOCALHOST_URL, cookie_key, COOKIE_PARAM_MARK);
//MK1_COOK//if(cookie_key != null) Settings.MOM(TAG_MK1_COOK, "...cookie_val=["+cookie_val+"]");
        if(        (cookie_val       ==         null)
                || TextUtils.equals (cookie_val,"-1")
                || TextUtils.isEmpty(cookie_val     )
          ) {
//MK1_COOK//Settings.MOM(TAG_MK1_COOK, caller+": (cookie_val empty): ...return -1f");

            return -1f;
          }
        //}}}
        // [thumb_p]<-[cookie_val] {{{

        float thumb_p = 0f;
        try { thumb_p = Float.parseFloat(cookie_val); } catch(Exception ignored) { }

        //}}}
        // return [thumb_p] {{{
//*MK1_COOK*/Settings.MON(TAG_MK1_COOK, caller, "...return thumb_p=["+thumb_p+"] .. f(cookie_val)");

        return thumb_p;
        //}}}
    }
    //}}}
    // _marker_get_cookie_key_from_url_mark_id {{{
    private static String _marker_get_cookie_key_from_url_mark_id(String url, String mark_id, boolean vertical)
    {
        String cookie_key = null;

        if(url != null)
        {
            cookie_key
                = Settings.Parse_keywords_from_url( url ).replace(" ", "_")
                + "_" + (vertical ? WV_TOOL_markV : WV_TOOL_mark)
                + mark_id;
        }
//MK1_COOK//Settings.MOM(TAG_MK1_COOK, "_marker_get_cookie_key_from_url_mark_id(url, "+mark_id+") ...return ["+cookie_key+"]");
        return cookie_key;
    }
    //}}}
    // _marker_np_setTextAndInfo {{{
    private void _marker_np_setTextAndInfo(NotePane marker_np, String text_and_info)
    {
        String caller = "_marker_np_setTextAndInfo";
        marker_np.setTextAndInfo( text_and_info );

//        // ECC COLOR {{{
//        int     bg_color  = Settings.Get_ECC_color_for_text(text_and_info, caller);
//        if(     bg_color != 0)
//        {
//            int fg_color  = (ColorPalette.GetBrightness( bg_color ) < 128)
//                ?            Color.WHITE
//                :            Color.BLACK;
//
//            marker_np.button.setBackgroundColor( bg_color );
//            marker_np.button.setTextColor      ( fg_color );
//        }
//        //}}}

        // delegated to [mm_colorize]
        marker_set_someone_has_invalidated_marker_colors(caller);
    }
    //}}}
    //}}}
    //* MARKERS (MARK_Map) {{{ */
    //{{{
    private static final LinkedHashMap<String, Object> MARK_Map = new LinkedHashMap<>();

    private static final                  int       MARKERS_MAX = 128;

// [fs_webView .url] <-> [cookies]<->[marks][sb ]
// [fs_webView2.url] <-> [cookies]<->[marks][sb2]
// [fs_webView3.url] <-> [cookies]<->[marks][sb3]

    // }}}
    // clear_MARK_Map {{{
    public static void clear_MARK_Map()
    {
//*MK6_MAP*/Settings.MOM(TAG_MK6_MAP, "clear_MARK_Map("+MARK_Map.size() +" MARKS]");

        if(MARK_Map.size() < 1) return;

        marker_unpin_markers();

        NotePane.RecycleMap( MARK_Map );

        MARK_Map.clear();
    }
    //}}}
    // _marker_get_slice_mark_id {{{
    private String _marker_get_slice_mark_id(String wv_slice, String mark_id)
    {
        return WV_TOOL_mark +"."+ wv_slice +"."+ mark_id;
    }
    //}}}
    // marker_map {{{
    private void marker_map(String wv_slice, String mark_id, NotePane np)
    {
        String key = _marker_get_slice_mark_id(wv_slice, mark_id);

//*MK6_MAP*/Settings.MOM(TAG_MK6_MAP, "MARK_Map.put(key=["+key+"])");
        MARK_Map.put(key, np);
    }
    //}}}
    // marker_map_swap_slice_1_2 {{{
    public  void marker_map_swap_slice_1_2()
    {
//*MK6_MAP*/Settings.MOC(TAG_MK6_MAP, "marker_map_swap_slice_1_2");

        for(int j=1; j < MARKERS_MAX; ++j)
        {
            String mark_id = ""+j;

            String   key_1 = _marker_get_slice_mark_id("1", mark_id);
            String   key_2 = _marker_get_slice_mark_id("2", mark_id);

            Object     o_1 = MARK_Map.get( key_1     );
            Object     o_2 = MARK_Map.get( key_2     );

            /*............*/ MARK_Map.put( key_1, o_2);
            /*............*/ MARK_Map.put( key_2, o_1);
        }
    }
    //}}}
    // marker_map_swap_slice_2_3 {{{
    public  void marker_map_swap_slice_2_3()
    {
//*MK6_MAP*/Settings.MOC(TAG_MK6_MAP, "marker_map_swap_slice_2_3");

        for(int j=1; j < MARKERS_MAX; ++j)
        {
            String mark_id = ""+j;

            String   key_2 = _marker_get_slice_mark_id("2", mark_id);
            String   key_3 = _marker_get_slice_mark_id("3", mark_id);

            Object     o_2 = MARK_Map.get( key_2     );
            Object     o_3 = MARK_Map.get( key_3     );

            /*............*/ MARK_Map.put( key_2, o_3);
            /*............*/ MARK_Map.put( key_3, o_2);
        }
    }
    //}}}
    // marker_map_swap_slice_1_3 {{{
    public  void marker_map_swap_slice_1_3()
    {
//*MK6_MAP*/Settings.MOC(TAG_MK6_MAP, "marker_map_swap_slice_1_3");

        for(int j=1; j < MARKERS_MAX; ++j)
        {
            String mark_id = ""+j;

            String   key_1 = _marker_get_slice_mark_id("1", mark_id);
            String   key_3 = _marker_get_slice_mark_id("3", mark_id);

            Object     o_1 = MARK_Map.get( key_1     );
            Object     o_3 = MARK_Map.get( key_3     );

            /*............*/ MARK_Map.put( key_1, o_3);
            /*............*/ MARK_Map.put( key_3, o_1);
        }
    }
    //}}}
    // marker_get_np_for_wv_slice_mark_id {{{
    private NotePane marker_get_np_for_wv_slice_mark_id(String wv_slice, String mark_id)
    {
        String key = _marker_get_slice_mark_id(wv_slice, mark_id);
//*MK6_MAP*/Settings.MOM(TAG_MK6_MAP, "MARK_Map.get(key=["+key+"])");

        NotePane np = null;
        for(Map.Entry<String,Object> entry : MARK_Map.entrySet()) {
            if(                    !(entry.getValue() instanceof NotePane) ) continue;
            np =           (NotePane)entry.getValue(); if(np == sbX_np) continue;
            if(TextUtils.equals(key, entry.getKey()) ) break;
            else                                          np = null;
        }
//*MK6_MAP*/if(np != null) Settings.MOM(TAG_MK6_MAP, "marker_get_np_for_wv_slice_mark_id: key=["+key+"] ...return ["+np+"]");
        return np;
    }
    //}}}
    // marker_get_free_slice_mark_id {{{
    private int marker_get_free_slice_mark_id(String wv_slice, int mark_id)
    {
        mark_id  = Math.max(mark_id, 1          ); // (ECC 1-based)
        mark_id  = Math.min(mark_id, MARKERS_MAX);

        String key = _marker_get_slice_mark_id(wv_slice, ""+mark_id);
        Object o;
        while(     (mark_id < MARKERS_MAX)
                && (     (o = MARK_Map.get(key)) != null))
        {
            if(((NotePane)o).tag == Settings.FREE_TAG)
                break;
            mark_id += 1;
            key = _marker_get_slice_mark_id(wv_slice, ""+mark_id);
        }
        return mark_id;
    }
    //}}}
    // marker_get_np_at_XY {{{
    private NotePane marker_get_np_at_XY(int x, int y)
    {
        for(Map.Entry<String,Object> entry : MARK_Map.entrySet()) {
            if(                    !(entry.getValue() instanceof NotePane) ) continue;
            NotePane np = (NotePane) entry.getValue(); if(np == sbX_np) continue;
            if( is_view_atXY(np.button,x,y) ) return np;
        }
        return null;
    }
    //}}}
    // marker_get_np_at_Y {{{
    private NotePane marker_get_np_at_Y(int y)
    {
        for(Map.Entry<String,Object> entry : MARK_Map.entrySet()) {
            if(                    !(entry.getValue() instanceof NotePane) ) continue;
            NotePane np = (NotePane) entry.getValue();
            if( is_view_atY(np.button,y - np.button.getHeight()) ) return np;   // markers are bottom-aligned
        }
        return null;
    }
    //}}}
    // marker_get_marker_near_XY {{{
    private NotePane marker_get_marker_near_XY(RTabs.MWebView wv, int x, int y)
    {
        for(Map.Entry<String,Object> entry : MARK_Map.entrySet()) {
            if(                    !(entry.getValue() instanceof NotePane) ) continue;
            NotePane  np = (NotePane)entry.getValue();
            if( is_marker_nearXY(np.button, x, y) )    return np; // return first np near xy
        }
        return null;
    }
    //}}}
    // marker_get_pinned_entry {{{
    private NotePane marker_get_pinned_entry(String wv_key_pfx, Map.Entry<String, Object> entry)
    {
        Object value = entry.getValue();
        if(  !(value instanceof NotePane)            ) return null;

        if( !entry.getKey().startsWith( wv_key_pfx ) ) return null;

        NotePane np = (NotePane)value;
        if( !marker_is_pinned( np )                  ) return null;

        return np;
    }
    //}}}
//    // marker_get_np_for_view {{{
//    private NotePane marker_get_np_for_view(View view)
//    {
//        for(Map.Entry<String, Object> entry : MARK_Map.entrySet())
//        {
//            if( !(entry.getValue() instanceof NotePane) ) continue;
//            NotePane np = (NotePane)entry.getValue();
//            if(np.button == view) return np;
//        }
//        return null;
//    }
//    //}}}
    //}}}
    //* MARKERS (TRASH) {{{ */
    //{{{
    private String last_cut_marker_text = null;

    //}}}
    // move_marker_text_to_trash {{{
    private void move_marker_text_to_trash(NotePane marker_np)
    {
        if( TextUtils.isEmpty(marker_np.text) )
            marker_empty_trash();

        // MOVE TO TRASH .. (store cut text)
        last_cut_marker_text    = marker_np.text;
        NotePane trash_tool_np  = get_tool_np_for_name( WV_TOOL_cut_mark );
        if(      trash_tool_np != null)
            /**/ trash_tool_np.setTextAndInfo(WV_TOOL_cut_mark_full +NotePane.INFO_SEP+ marker_np.tag);

        // SHOW WHAT AS BEEN CUT
        sb_show_TRASH_MARKER( marker_np.getLabel() );
    }
    //}}}
    // marker_empty_trash {{{
    private void marker_empty_trash()
    {
        // EMPTY TRASH .. (clear text)
        NotePane trash_tool_np  = get_tool_np_for_name( WV_TOOL_cut_mark );
        if(      trash_tool_np != null)
            /**/ trash_tool_np.setTextAndInfo( WV_TOOL_cut_mark_free );

        // SHOW EMPTY TRASH CLEAR
        sb_show_TRASH_EMPTY();
    }
    //}}}
    // is_trash_empty {{{
    private boolean is_trash_empty()
    {
        return (last_cut_marker_text == null);
    }
    //}}}
    //}}}
    //* MARKERS (SYNC) {{{ */
    //{{{
    private static final RTabs.MWebView[]         wv =   new RTabs.MWebView[3];
    private static final         String[] pinned_url =   new String[3];
    private static final            int[] wv_aspect  =   new    int[3];
    private static final          float[] wv_scaleY  =   new  float[3];
    private static final           Rect[] wv_hitRect = { new Rect(), new Rect(), new Rect() };

    private static final           Rect   wv_hr      =   new Rect();


    //}}}
    // marker_wv_sync {{{
    private int  marker_wv_sync_calls_count = 0;
    public  void marker_wv_sync            (String caller) { marker_wv_sync_with_delay(caller, MARKER_SYNC_LONG_DELAY ); }
    public  void marker_wv_sync_short_delay(String caller) { marker_wv_sync_with_delay(caller, MARKER_SYNC_SHORT_DELAY); }
    public  void marker_wv_sync_no_delay   (String caller) { marker_wv_sync_with_delay(caller, 0                      ); }
    public  void marker_wv_sync_with_delay (String caller, int delay)
    {
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, "marker_wv_sync_with_delay(delay="+delay+"ms) #"+ (marker_wv_sync_calls_count+1), "caller=["+caller+"]");

        // TEMPORARY HIDE MARKERS .. f(webview swap start)
      //marker_set_someone_has_called_hide_marks(caller);

        marker_wv_sync_calls_count += 1;
        mRTabs.handler.re_postDelayed(hr_marker_wv_sync, delay);
    }
    //}}}
    // marker_wv_sync_invalidate_layout {{{
    private void marker_wv_sync_invalidate_layout()
    {
        if(wv_hitRect[0] != null) wv_hitRect[0].setEmpty();
        if(wv_hitRect[1] != null) wv_hitRect[1].setEmpty();
        if(wv_hitRect[2] != null) wv_hitRect[2].setEmpty();
    }
    //}}}
    // marker_wv_sync_invalidate_pinned_url {{{
    private void marker_wv_sync_invalidate_pinned_url()
    {
        String caller = "marker_wv_sync_invalidate_pinned_url";
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, caller);

        pinned_url[0] = null;
        pinned_url[1] = null;
        pinned_url[2] = null;
    }
    //}}}
    // hr_marker_wv_sync {{{
    private Runnable hr_marker_wv_sync = new Runnable() {
        @Override public void run() {
            do_marker_wv_sync();
            marker_wv_sync_calls_count = 0;
        }
    };

    //}}}
    // do_marker_wv_sync {{{
    private void do_marker_wv_sync()
    {
        // CURRENT WEBVIEWS SPLIT LAYOUT {{{
        String caller = "do_marker_wv_sync";
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, caller, Settings.TRACE_OPEN);

        wv[0] = mRTabs.fs_webView ;
        wv[1] = mRTabs.fs_webView2;
        wv[2] = mRTabs.fs_webView3;

         //}}}
        // (scroll_wv_anim_is_running) (marks_locked) {{{
//*MK2_SYNC*/if(someone_has_called_hide_marks         != null) Settings.MOC(TAG_MK2_SYNC, caller, "someone_has_called_hide_marks........=["+someone_has_called_hide_marks        +"]");
//*MK2_SYNC*/if(someone_has_invalidated_marker_colors != null) Settings.MOC(TAG_MK2_SYNC, caller, "someone_has_invalidated_marker_colors=["+someone_has_invalidated_marker_colors+"]");

        boolean scroll_wv_anim_is_running
            = (scroll_wv_AnimatorSet != null);

        boolean marks_locked
            =    property_get_WV_TOOL_MARK_LOCK
            ||   scroll_wv_anim_is_running
            ||  (someone_has_called_hide_marks         != null)
            ||  (someone_has_invalidated_marker_colors != null);

//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, "marks_locked=["+marks_locked+"]");
        //}}}
        for(int i=0; i<3; ++i)
        {
            // [wv] [url] [pfx] {{{

            if(wv[i] == null) continue;

            String  url = wv_getUrl(wv[i]);
            if(url == null  ) continue;

            String pfx
                = (i==0) ?   "1.."
                : (i==1) ?   ".2."
                :            "..3";
            pfx = "wv["+pfx+"]";

            //}}}
            // [url_changed] {{{
            boolean url_changed = !Settings.URL_keywords_equals(pinned_url[i], url);

//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, pfx+"...........url_changed=["+ url_changed  +"]");
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, pfx+"..................FROM ["+ pinned_url[i]+"]");
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, pfx+"....................TO ["+        url   +"]");
            //}}}
            // UNPIN [pinned_url] {{{
            // [orientation_changed] {{{
            boolean orientation_changed = (wv_aspect[i] != Settings.DISPLAY_ASPECT);

//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, pfx+"...orientation_changed=["+ orientation_changed     +"]");
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, pfx+"..................FROM=["+ wv_aspect[i]            +"]");
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, pfx+"....................TO ["+ Settings.DISPLAY_ASPECT +"]");

            /*..........................*/ wv_aspect[i]  = Settings.DISPLAY_ASPECT;
            //}}}
            if(        (url_changed || orientation_changed)
                    && (pinned_url[i] != null)
              ) {
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, pfx+"=>......MARKERS  UNPIN ["+ pinned_url[i] +"]");
                marker_unpin_markers_with_url(                         pinned_url[i]     );
                pinned_url[i] = null;
              }
            //}}}
            // HIDE  [pinned_url] {{{
            if(         marks_locked
                    || !mRTabs.is_fs_webViewX_in_screen(wv[i])
              ) {
                if(wv[i] != null)
                {
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, pfx+"=>......MARKERS   HIDE");

                    marker_hide_pinned_markers_on_wv( wv[i] );
                }
            }
            //}}}
            // SHOW [pinned_url] or LOAD [changed url] {{{
            else {
                // SHOW  [pinned_url] {{{
                // [at_left] .. f(sbX) {{{

                NpButton     sbX = get_sb_for_wv_or_sb( wv[i] );
                boolean  at_left = (sbX != null) ? !sbX.at_left : !is_sb_at_left();

                //}}}
                // [layout_changed] {{{
                wv[i].getHitRect(wv_hr);
                boolean layout_changed = !wv_hitRect[i].equals(wv_hr);

//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, pfx+"........layout_changed=["+ layout_changed +"]");
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, pfx+"..................FROM ["+ wv_hitRect[i]  +"]");
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, pfx+"....................TO ["+ wv_hr          +"]");

                wv_hitRect[i].set( wv_hr );
                //}}}
                // [scale_changed] {{{
                float           scaleY =  wv[i].getScaleY();
                boolean  scale_changed = (wv_scaleY[i] != scaleY);

//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, pfx+".........scale_changed=["+ scale_changed  +"]");
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, pfx+"..................FROM ["+  wv_scaleY[i]  +"]");
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, pfx+"....................TO ["+     scaleY     +"]");

                wv_scaleY [i] = scaleY;
                //}}}
                if(pinned_url[i] != null)
                {
                    if(layout_changed || scale_changed)
                    {
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, pfx+"=>......MARKERS LAYOUT");
                        marker_hook_pinned_markers_on_wv(wv[i], at_left);
                    }
                    else {
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, pfx+"=>......MARKERS   SHOW");
                        marker_show_pinned_markers_on_wv(wv[i], at_left);
                    }
                }
                //}}}
                // LOAD  [changed url] {{{
                else if(url != null)
                {
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, pfx+"=>......MARKERS   LOAD ["+ url +"]");
                    marker_load_markers_from_cookie(wv[i], at_left);
                    pinned_url[i] = url;
                }
                //}}}
            }
            //}}}
        }
        // [marker_magnetize_pause] .. f(marks_locked) {{{
        if(marks_locked)
        {
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, caller, "MARKER MAGNETIZE: PAUSE .. (marks_locked)");
            marker_magnetize_pause(gesture_down_wv, caller);
        }
        //}}}
        marker_redisplay_temporarily_hidden_marks(caller);

//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, caller, Settings.TRACE_CLOSE);
    }
    //}}}
    // marker_hook_pinned_markers_on_wv {{{
    private void marker_hook_pinned_markers_on_wv(RTabs.MWebView wv, boolean  at_left)
    {
        String caller = "marker_hook_pinned_markers_on_wv";
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, caller);

        synchronized( MARK_Map )
        {
            String wv_key_pfx = WV_TOOL_mark +"."+ marker_get_wv_slice( wv );
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, "wv_key_pfx=["+wv_key_pfx+"]");
            NotePane       np = null;
            for(Map.Entry<String, Object> entry : MARK_Map.entrySet())
            {
                // SELECT ALL [SLICE MARK_MAP] MARKERS {{{
                if( !(entry.getValue() instanceof NotePane  )) continue;
                if( ! entry.getKey  ().startsWith(wv_key_pfx)) continue;
                np = (NotePane)entry.getValue();
                if(                 np == floating_marker_np ) continue;

                //}}}
                // PIN SELECTED MARKERS {{{
                String mark_id = marker_get_mark_id_from_button_name( np.button );
                float  thumb_p = marker_get_cookie_thumb_p(wv, mark_id);

                marker_pin_mark_at_thumb_p(wv, np, at_left, thumb_p); // account for current wv layout and scale

                //}}}
            }
        }
    }
    //}}}
    // marker_show_pinned_markers_on_wv {{{
    private void marker_show_pinned_markers_on_wv(RTabs.MWebView wv, boolean  at_left)
    {
        String caller = "marker_show_pinned_markers_on_wv";
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, caller);

        synchronized( MARK_Map )
        {
            String wv_key_pfx = WV_TOOL_mark +"."+ marker_get_wv_slice( wv );
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, "wv_key_pfx=["+wv_key_pfx+"]");
            float         m_x =           marker_get_wv_mark_x(wv);
            NotePane       np = null;
            for(Map.Entry<String, Object> entry : MARK_Map.entrySet())
            {
              //// SELECT ALL [SLICE MARK_MAP] MARKERS {{{
              //if( !(entry.getValue() instanceof NotePane  )     ) continue;
              //if( ! entry.getKey  ().startsWith(wv_key_pfx)     ) continue;
              //np = (NotePane)entry.getValue();

              ////}}}
                // SELECT slice pinned markers {{{
                np = marker_get_pinned_entry(wv_key_pfx, entry);
                if(np == sbX_np) continue;
                if(np == null              ) continue;
                if(np == floating_marker_np) continue;

                //}}}
                if( is_floating_marker( np )                      ) continue;

                // 1/2 SHOW PINNED MARKERS [m_x] [saved_y] [scale=1]
              //if( marker_is_pinned( np ) )
              //{
                    int m_y = (int)np.button.get_saved_y();
                    int m_w = (int)np.button.get_saved_w();

                    np.button.setRotationX(  0f );
                    np.button.setRotationY(  0f );
                    np.button.setPivotX(at_left ? 0 : m_w);

                    np.button.setX        ( m_x );
                    np.button.setY        ( m_y );
                    np.button.setScaleX   (  1f );
                    np.button.setScaleY   (  1f );

                    np.button.setVisibility( View.VISIBLE );
              //}
              //// 2/2 LOAD UNPINNED MARKERS
              //else {
              //    String mark_id = marker_get_mark_id_from_button_name( np.button );
              //    marker_load_mark_id_from_cookie(wv, mark_id, at_left);
              //}
            }
        }
    }
    //}}}
    // marker_hide_pinned_markers_on_wv {{{
    private void marker_hide_pinned_markers_on_wv(RTabs.MWebView wv)
    {
        String caller = "marker_hide_pinned_markers_on_wv";
//MK2_SYNC//Settings.MOC(TAG_MK2_SYNC, caller);

        synchronized( MARK_Map )
        {
            int np_button_hidden_count = 0;
            String wv_key_pfx = WV_TOOL_mark +"."+ marker_get_wv_slice( wv );
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, "wv_key_pfx=["+wv_key_pfx+"]");
            NotePane       np = null;
            for(Map.Entry<String, Object> entry : MARK_Map.entrySet())
            {
                // SELECT slice pinned markers {{{
                np = marker_get_pinned_entry(wv_key_pfx, entry);
                if(np == null              ) continue;
                if(np == sbX_np) continue;
                if(np == floating_marker_np) continue;

                //}}}
                if( is_floating_marker( np )                      ) continue;
                // HIDE selected markers {{{
                np.button.setVisibility( View.GONE );
                np_button_hidden_count += 1;

                //}}}
            }
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, caller, "..."+np_button_hidden_count+" MARKERS HIDDEN");
        }
    }
    //}}}
    // marker_hide_pinned_markers {{{
    private void marker_hide_pinned_markers(String caller)
    {
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, "marker_hide_pinned_markers", "caller=["+someone_has_called_hide_marks+"]");

        RTabs.MWebView wv;
        wv = mRTabs.fs_webView ; if( mRTabs.is_fs_webViewX_in_screen(wv) ) { marker_hide_pinned_markers_on_wv( wv ); }
        wv = mRTabs.fs_webView2; if( mRTabs.is_fs_webViewX_in_screen(wv) ) { marker_hide_pinned_markers_on_wv( wv ); }
        wv = mRTabs.fs_webView3; if( mRTabs.is_fs_webViewX_in_screen(wv) ) { marker_hide_pinned_markers_on_wv( wv ); }
    }
    //}}}
    // marker_hide_all_markers {{{
    public void marker_hide_all_markers(String caller)
    {
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, caller+"->marker_hide_all_markers");

        synchronized( MARK_Map )
        {
            int np_button_hidden_count = 0;
            NotePane       np = null;
            for(Map.Entry<String, Object> entry : MARK_Map.entrySet())
            {
                if( !(entry.getValue() instanceof NotePane  )     ) continue;
                np = (NotePane)entry.getValue();
                if(np.button.getVisibility() != View.GONE)
                {
                    np.button.setVisibility( View.GONE );
                    np_button_hidden_count += 1;
                }
            }
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, caller, "..."+np_button_hidden_count+" MARKERS HIDDEN");
        }
    }
    //}}}
    //}}}
    //* MARKERS (VALIDATION) {{{ */
    //{{{
    private       String someone_has_called_hide_marks         = null;
    private       String someone_has_invalidated_marker_colors = null;

    //}}}
    // marker_clear_pending_updates {{{
    public void marker_clear_pending_updates(String caller)
    {
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, caller+"->marker_clear_pending_updates");

//*MK2_SYNC*/if(someone_has_called_hide_marks         != null) Settings.MOC(TAG_MK2_SYNC, caller, "someone_has_called_hide_marks........=["+someone_has_called_hide_marks        +"]");
//*MK2_SYNC*/if(someone_has_invalidated_marker_colors != null) Settings.MOC(TAG_MK2_SYNC, caller, "someone_has_invalidated_marker_colors=["+someone_has_invalidated_marker_colors+"]");

        someone_has_called_hide_marks         = null;
        someone_has_invalidated_marker_colors = null;
    }
    //}}}
    // marker_set_someone_has_invalidated_marker_colors {{{
    public void marker_set_someone_has_invalidated_marker_colors(String caller)
    {
        if(someone_has_invalidated_marker_colors == null)
        {
            // RETAIN FIRST CALLER
            someone_has_invalidated_marker_colors = caller;
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, caller+"->marker_set_someone_has_invalidated_marker_colors", "INITIAL CALL FROM ["+someone_has_invalidated_marker_colors+"]");

          //marker_set_someone_has_called_hide_marks(caller);
        }
        else {
//MK2_SYNC//Settings.MOC(TAG_MK2_SYNC, caller+"->marker_set_someone_has_invalidated_marker_colors", "PENDING CALL FROM ["+someone_has_invalidated_marker_colors+"]");
        }
    }
    //}}}
    // marker_set_someone_has_called_hide_marks {{{
    public void marker_set_someone_has_called_hide_marks(String caller)
    {
        if(someone_has_called_hide_marks == null)
        {
            // RETAIN FIRST CALLER
            someone_has_called_hide_marks = caller;
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, "marker_set_someone_has_called_hide_marks", "INITIAL CALL FROM ["+someone_has_called_hide_marks+"]");

            // HIDE ON FIRST CALL
            if( !property_get_WV_TOOL_MARK_LOCK )
                marker_hide_pinned_markers(caller);
        }
        else {
//MK2_SYNC//Settings.MOC(TAG_MK2_SYNC, caller+"->marker_set_someone_has_called_hide_marks", "PENDING CALL FROM ["+someone_has_called_hide_marks+"]");
        }
    }
    //}}}
    // marker_redisplay_temporarily_hidden_marks {{{
//  public  void marker_redisplay_temporarily_hidden_marks_no_delay(String caller) { marker_redisplay_temporarily_hidden_marks(caller, 0); }
    private void marker_redisplay_temporarily_hidden_marks         (String caller) { marker_redisplay_temporarily_hidden_marks(caller, MARKER_REDISPLAY_DELAY); }
    private void marker_redisplay_temporarily_hidden_marks         (String caller, int delay)
    {
        if(        (someone_has_called_hide_marks         == null)
                && (someone_has_invalidated_marker_colors == null)
          )
            return;

//*MK2_SYNC*/caller += "->marker_redisplay_temporarily_hidden_marks";//TAG_MK2_SYNC
//*MK2_SYNC*/if(someone_has_called_hide_marks         != null) Settings.MOC(TAG_MK2_SYNC, caller, "someone_has_called_hide_marks........=["+someone_has_called_hide_marks        +"]");
//*MK2_SYNC*/if(someone_has_invalidated_marker_colors != null) Settings.MOC(TAG_MK2_SYNC, caller, "someone_has_invalidated_marker_colors=["+someone_has_invalidated_marker_colors+"]");

        mRTabs.handler.re_postDelayed(hr_marker_redisplay_temporarily_hidden_marks, MARKER_REDISPLAY_DELAY);
    }
    //}}}
    // hr_marker_redisplay_temporarily_hidden_marks {{{
    private Runnable hr_marker_redisplay_temporarily_hidden_marks = new Runnable() {
        @Override public void run() { do_marker_redisplay_temporarily_hidden_marks(); }
    };
    //}}}
    // do_marker_redisplay_temporarily_hidden_marks {{{
    private void do_marker_redisplay_temporarily_hidden_marks()
    {
        // (!WV_TOOL_MARK_LOCK) {{{
        String caller = "do_marker_redisplay_temporarily_hidden_marks";

        if( property_get_WV_TOOL_MARK_LOCK )
        {
//*MK2_SYNC*/Settings.MON(TAG_MK2_SYNC, caller, "property_get_WV_TOOL_MARK_LOCK=["+property_get_WV_TOOL_MARK_LOCK+"]");
            return;
        }
        //}}}
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, caller, Settings.TRACE_OPEN);
        // REORDER INVALIDATED MARKERS COLOR {{{
        if(someone_has_invalidated_marker_colors != null)
        {
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, caller, "REORDER  INVALIDATED MARKERS COLOR FOR ["+someone_has_invalidated_marker_colors+"]");


            RTabs.MWebView wv; NpButton sbX;
            wv = mRTabs.fs_webView ; if( mRTabs.is_fs_webViewX_in_screen(wv) ) { sbX = get_sb_for_wv_or_sb(wv); if(sbX != null) mm_colorize( wv ); }
            wv = mRTabs.fs_webView2; if( mRTabs.is_fs_webViewX_in_screen(wv) ) { sbX = get_sb_for_wv_or_sb(wv); if(sbX != null) mm_colorize( wv ); }
            wv = mRTabs.fs_webView3; if( mRTabs.is_fs_webViewX_in_screen(wv) ) { sbX = get_sb_for_wv_or_sb(wv); if(sbX != null) mm_colorize( wv ); }

            // REDISPLAY VISIBLE WEBVIEWS MARKERS .. f(mm_colorize)
            //someone_has_called_hide_marks = "mm_colorize";

            // CONSUME POSTER FLAG
            someone_has_invalidated_marker_colors = null;
        }
        //}}}
        // REDISPLAY VISIBLE WEBVIEWS MARKERS .. f(fs_webView fs_webView2 fs_webView3) {{{
        if(someone_has_called_hide_marks != null)
        {
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, caller, "REDISPLAY VISIBLE WEBVIEWS MARKERS FOR ["+someone_has_called_hide_marks+"]");

            RTabs.MWebView wv; NpButton sbX;
            wv = mRTabs.fs_webView ; if( mRTabs.is_fs_webViewX_in_screen(wv) ) { sbX = get_sb_for_wv_or_sb(wv); if(sbX != null) marker_hook_pinned_markers_on_wv(wv, !sbX.at_left); }
            wv = mRTabs.fs_webView2; if( mRTabs.is_fs_webViewX_in_screen(wv) ) { sbX = get_sb_for_wv_or_sb(wv); if(sbX != null) marker_hook_pinned_markers_on_wv(wv, !sbX.at_left); }
            wv = mRTabs.fs_webView3; if( mRTabs.is_fs_webViewX_in_screen(wv) ) { sbX = get_sb_for_wv_or_sb(wv); if(sbX != null) marker_hook_pinned_markers_on_wv(wv, !sbX.at_left); }

            // CONSUME POSTER FLAG
            someone_has_called_hide_marks = null;
        }
        //}}}
        // [RESUME MAGNETIZE] .. f(marker_magnetized_np_count) {{{
        if( may_resume_magnetize(caller) )
        {
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, caller, "MAGNETIZE: RESUME");

            marker_magnetize_resume(caller);
        }
        //}}}
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, caller, Settings.TRACE_CLOSE);
    }
//}}}
    // mm_colorize {{{
    //{{{
    private static NotePane marker_np_array[] = new NotePane[ MARKERS_MAX ];

    //}}}
    private void mm_colorize(RTabs.MWebView wv)
    {
        // [MARKERS   SELECT] .. f(wv) {{{
        String caller = "mm_colorize";
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, caller, "...wv=["+wv+"]");

        int marker_np_count = 0;

        String wv_key_pfx = WV_TOOL_mark +"."+ marker_get_wv_slice( wv );
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, "wv_key_pfx=["+wv_key_pfx+"]");
        for(Map.Entry<String, Object> entry : MARK_Map.entrySet())
        {
            // SELECT slice pinned markers {{{
            NotePane np = marker_get_pinned_entry(wv_key_pfx, entry);
            if(np == null              ) continue;
            if(np == sbX_np) continue;
            if(np == floating_marker_np) continue;

            //}}}

            marker_np_array[marker_np_count]  = np;
            marker_np_count                  += 1;
        }
//*MK2_SYNC*/Settings.MOC(TAG_MK2_SYNC, caller, "[SELECT]: marker_np_count=["+marker_np_count+"]");
        if(marker_np_count < 1) return;

        //}}}
        // [MARKERS    ORDER] .. f(saved_y) {{{
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, "[ORDER]");
        Arrays.sort( marker_np_array
                , new Comparator<NotePane>() {
                    @Override
                    public int compare(NotePane lhs, NotePane rhs)
                    {
                        if     ((lhs  == null) && (rhs == null)) return  0;
                        else if(                  (rhs == null)) return -1; // ..lhs (exists)
                        else if((lhs  == null)                 ) return  1; // ..rhs (exists)
                        float   lhs_y  = lhs.button.get_saved_y();          // how hight they are on screen
                        float   rhs_y  = rhs.button.get_saved_y();          // how hight they are on screen
                        return (lhs_y == rhs_y) ?                        0  // same distance
                            :  (lhs_y <= rhs_y) ?                       -1  // ..lhs (first)
                            :                                            1; // ..rhs (next)

                    }
                });

        //}}}
        // [COLORING] .. f(order) {{{
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, "[COLORING]");
        int             bg_color = marker_np_array[0].button.getBackgroundColor(); // pick first color
        boolean bg_color_is_dark = (ColorPalette.GetBrightness( bg_color ) < 128);

        for(int i=0; i < marker_np_count; ++i)
        {
            // [text_and_info] {{{
            NotePane   marker_np = marker_np_array[i];
            String text_and_info = marker_np.text;//marker_np.button.getText().toString();

            //}}}
            // 1/2 [TEXT ECC COLOR] {{{
            int ecc_color = Settings.Get_ECC_color_for_text(text_and_info, caller);
            if(ecc_color != 0)
            {
//MK2_SYNC//Settings.MOM(TAG_MK2_SYNC, "...NEW ECC COLOR FOR ["+text_and_info.replace("\n","\\n")+"]");
                bg_color         = ecc_color;
                bg_color_is_dark = (ColorPalette.GetBrightness( bg_color ) < 128);
            }
            //}}}
            // 2/2 [GRADED PREVIOUS COLOR] {{{
            else {
//*MK2_SYNC*/Settings.MOM(TAG_MK2_SYNC, "..."+ (bg_color_is_dark ? "DARKER  " : "LIGHTER") +" COLOR FOR ["+text_and_info.replace("\n","\\n")+"]");

                bg_color
                    = bg_color_is_dark
                    ?  ColorPalette.GetColorDarker (bg_color, 0.05)
                    :  ColorPalette.GetColorLighter(bg_color, 0.05);
            }
            //}}}
            // [fg_color] .. f(bg_color_is_dark) {{{
            int fg_color = bg_color_is_dark ? Color.WHITE : Color.BLACK;

            //}}}
            marker_np.button.setBackgroundColor( bg_color );
            marker_np.button.setTextColor      ( fg_color );
        }
        //}}}
    }
    //}}}
    //}}}
    //* MARKERS (PIN) {{{ */
    // marker_pin_mark_at_thumb_p {{{
    private void marker_pin_mark_at_thumb_p(RTabs.MWebView wv, NotePane marker_np, boolean at_left, float thumb_p)
    {
//*MK3_PIN*/Settings.MOM(TAG_MK3_PIN, "marker_pin_mark_at_thumb_p: wv=["+wv+"] marker_np=["+marker_np+"] at_left=["+at_left+"] thumb_p=["+thumb_p+"]");
        if(thumb_p < 0) return;

        int[] xy = new int[2];
        marker_get_mark_xy_at_thumb_p(wv, marker_np, at_left, thumb_p, xy);

        container_addView( marker_np.button );

        marker_np.button.setVisibility( View.VISIBLE );
        marker_np.button.bringToFront();

        marker_pin_mark_at_x_y(marker_np, at_left, xy[0], xy[1]);
    }
    //}}}
    // marker_pin_mark_at_x_y {{{
    private void marker_pin_mark_at_x_y(NotePane marker_np, boolean at_left, float x, float y)
    {
        // [marker top-left]<-[xy] {{{
        String caller = "marker_pin_mark_at_x_y";
//*MK3_PIN*/Settings.MOM(TAG_MK3_PIN, caller+": marker_np=["+marker_np+"] at_left=["+at_left+"] x=["+x+"] y=["+y+"]");
        int h, w;
        if( Settings.SCROLLBAR_USE_LAYOUT_PARAMS )
        {
            FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams)marker_np.button.getLayoutParams();
            w                  = flp.width;
            h                  = flp.height;
            flp.leftMargin     = (int)x;
            flp.topMargin      = (int)y-h; // bottom aligned

            if( Settings.SCROLLBAR_USE_TRANSLATION )
            {
                flp.leftMargin = 0;
                flp.topMargin  = 0;
                marker_np.button.setTranslationX(x  );
                marker_np.button.setTranslationY(y-h); // bottom aligned
            }
            else {
                marker_np.button.setX(x  );
                marker_np.button.setY(y-h); // bottom aligned
            }
          //marker_np    .button.setLayoutParams( flp );

            marker_np    .button.setPivotX(at_left ? 0 : flp.width     );
            marker_np    .button.setPivotY(              flp.height    ); // BOTTOM HOT SPOT

          //marker_np    .button.setPivotY(              flp.height / 2);
          //marker_np    .button.setPivotY(0);

            marker_np    .button.setRotationX( 0f );
            marker_np    .button.setRotationY( 0f );
        }
        else {
            marker_np    .button.setX( x );
            marker_np    .button.setY( y );
            w = marker_np.button.getWidth();
            h = marker_np.button.getHeight();

            marker_np    .button.setPivotX(at_left ? 0 :            w      );
            marker_np    .button.setPivotY(marker_np.button.getHeight()    ); // BOTTOM HOT SPOT

          //marker_np    .button.setPivotY(marker_np.button.getHeight() / 2);
          //marker_np    .button.setPivotY(0);

            marker_np    .button.setRotationX( 0f );
            marker_np    .button.setRotationY( 0f );
        }

        // save layout-time attributes
        marker_np.button.setScaleX(1f);
        marker_np.button.setScaleY(1f);
        marker_np.button.save_attributes();

        // XXX}}}
        // unset_marker_wrapped] .. (some unwrapped marker is showing) {{{
        unset_marker_wrapped(caller);

        //}}}
        marker_set_someone_has_invalidated_marker_colors(caller);
    }
    //}}}
    // marker_unpin_markers {{{
    private static void marker_unpin_markers()
    {
        marker_unpin_markers_with_cookie_pfx(null);
    }
    //}}}
    // marker_unpin_marker_np {{{
    private static void marker_unpin_marker_np(NotePane np)
    {
        String caller = "marker_unpin_marker_np";
        np.tag  = Settings.FREE_TAG;          // np attributes

        np.button.setVisibility( View.GONE ); // np.button
        np.button.has_clear_attributes();     // np.button layout attributes

        //ViewGroup parent = (ViewGroup)np.button.getParent();
        //if(parent != null) parent.removeView( np.button );

        if( is_floating_marker( np ) )
        {
            WVTools_instance.marker_float3_BAK_LOCK(caller); // ->[swing_3_reached]->[marker_float_onStageChanged]
        }
    }
    //}}}
    // marker_unpin_markers_with_cookie_pfx {{{
    private static void marker_unpin_markers_with_cookie_pfx(String cookie_pfx)
    {
//*MK3_PIN*/Settings.MOC(TAG_MK3_PIN, "marker_unpin_markers_with_cookie_pfx("+cookie_pfx+") MARK_Map.size=["+MARK_Map.size()+"]");
        int unpinned_count = 0;
        for(Map.Entry<String, Object> entry : MARK_Map.entrySet())
        {
            if( !(entry.getValue() instanceof NotePane) ) continue;
            NotePane np = (NotePane)entry.getValue();
            if(       (                   cookie_pfx == null        ) // delete all marks
                    || np.tag.startsWith( cookie_pfx                ) // delete all this url marks
                    || TextUtils.equals ( np.tag, Settings.FREE_TAG ) // ...
                    || TextUtils.isEmpty( np.tag                    ) // ...
              ) {
//*MK3_PIN*/Settings.MOM(TAG_MK3_PIN, "....unpinned: np.tag=["+np.tag+"]");
                marker_unpin_marker_np( np );
                ++unpinned_count;
              }
            else {
//*MK3_PIN*/Settings.MOM(TAG_MK3_PIN, "not unpinned: np.tag=["+np.tag+"]");
            }

        }
//*MK3_PIN*/Settings.MOM(TAG_MK3_PIN, "...unpinned_count=["+unpinned_count+"]");
    }
    //}}}
    // marker_unpin_markers_with_url {{{
    private static void marker_unpin_markers_with_url(String url)
    {
//*MK3_PIN*/Settings.MOC(TAG_MK3_PIN, "marker_unpin_markers_with_url("+url+")");
        String cookie_pfx = marker_get_cookie_pfx_from_url( url );
        marker_unpin_markers_with_cookie_pfx( cookie_pfx );
    }
    //}}}
    // marker_pin_get_count {{{
    private int marker_pin_get_count(RTabs.MWebView wv)
    {
        // SELECT .. f(wv_key_pfx) {{{
        String wv_key_pfx = WV_TOOL_mark +"."+ marker_get_wv_slice( wv );
//*MK3_PIN*/Settings.MOM(TAG_MK3_PIN, "wv_key_pfx=["+wv_key_pfx+"]");

        int marker_count   = 0;
        for(Map.Entry<String, Object> entry : MARK_Map.entrySet())
        {
            // SELECT slice pinned markers {{{
            NotePane np = marker_get_pinned_entry(wv_key_pfx, entry);
            if(np == null) continue;

            //}}}
            marker_count += 1;
        }

//*MK3_PIN*/Settings.MOM(TAG_MK3_PIN, "marker_pin_get_count("+get_view_name(wv)+"): ...return "+marker_count+" MARKERS");
        return marker_count;
        //}}}
    }
    //}}}
    // marker_is_pinned {{{
    private boolean marker_is_pinned(NotePane marker_np)
    {
        return marker_np.button.has_save_attributes();
    }
    //}}}
    //}}}
    //* MARKERS (GEOMETRY) {{{ */
    // marker_get_wv_mark_x .. f(gesture_down_wv) .. f(get_sb_for_wv_or_sb) {{{
    private static int marker_get_wv_mark_x(RTabs.MWebView wv)
    {
        if(wv == null) return 0;
        NpButton       sbX  = get_sb_for_wv_or_sb( wv );
        if(            sbX == null) return 0;

        return marker_get_mark_x(wv, sbX.at_left);
    }
    //}}}
    // marker_get_mark_x .. f(wv) .. f(at_left) {{{
    private static int marker_get_mark_x(RTabs.MWebView wv, boolean at_left)
    {
        if(wv == null) return 0;

        float    wv_x = wv.getX();
        float   wv_w  = wv.getWidth();
        float   wv_sX = wv.getScaleX();
        float       x = at_left ? wv_x + Settings.TAB_MARK_H/2 + wv_w * wv_sX - Settings.TOOL_BADGE_SIZE
            :                     wv_x - Settings.TAB_MARK_H/2;
//MK3_PIN//Settings.MOM(TAG_MK3_PIN, "marker_get_wv_mark_x: ...return x=["+x+"]");
        return (int)x;
    }
    //}}}
    // marker_get_mark_xy_at_thumb_p {{{
    private boolean marker_get_mark_xy_at_thumb_p(RTabs.MWebView wv, NotePane marker_np, boolean at_left, float thumb_p, int[] xy)
    {
        // [thumb_y]<-[wv][thumb_p] {{{

        float thumb_y = wv.getHeight() * thumb_p / 100f;
        //}}}
        // [wv] (xy_wh) (scale) {{{
        float wv_y  = wv.getY();
        float wv_x  = wv.getX();
        float wv_w  = wv.getWidth();

        float wv_sX = wv.getScaleX();
        float wv_sY = wv.getScaleY();

        //}}}
        // [xy] [wv][thumb_y] [at_left] {{{
        float x = at_left ? wv_x - Settings.TAB_MARK_H/2
            :               wv_x + Settings.TAB_MARK_H/2 +    wv_w * wv_sX - Settings.TOOL_BADGE_SIZE;
        float y =           wv_y                         + thumb_y * wv_sY;

        // }}}
        // [xy] .. f(is_floating_marker) {{{
//*MK3_PIN*/Settings.MOM(TAG_MK3_PIN , String.format("[%s] .. MARK  LAYOUT XY=[%4d @ %4d]", marker_get_mark_id_from_button_name(marker_np.button), (int)x, (int)y));
        if( is_floating_marker( marker_np ) )
        {
            x = marker_float_get_unlocked_x(wv);
            y = marker_np.button.getTranslationY()+marker_np.button.getHeight(); // hotspot Y
        }
        //}}}
        xy[0] = (int)x;
        xy[1] = (int)y;
        return true;
    }
    //}}}
    // marker_get_gesture_down_marker_np_atXY {{{
    private NotePane marker_get_gesture_down_marker_np_atXY(int x, int y)
    {
//*MK3_PIN*/String caller = "marker_get_gesture_down_marker_np_atXY";//TAG_MK3_PIN
//*MK3_PIN*/Settings.MOM(TAG_MK3_PIN, caller+": gesture_down_SomeView_atXY=["+get_view_name(mRTabs.gesture_down_SomeView_atXY)+"]");

        NotePane np = null;

        if( is_a_marker( mRTabs.gesture_down_SomeView_atXY ) )
        {
//*MK3_PIN*/Settings.MOM(TAG_MK3_PIN, "...is_a_marker("+mRTabs.gesture_down_SomeView_atXY+")");

            np  = Settings.get_np_for_button( (NpButton)mRTabs.gesture_down_SomeView_atXY );
        }

        if( np == null)
        {
//*MK3_PIN*/Settings.MOM(TAG_MK3_PIN, "...calling marker_get_np_at_XY(x,y)");

            np  = marker_get_np_at_XY(x,y);
        }

        // ... explain why [gesture_down_SomeView_atXY would be null]
        // ... when (marker_get_np_at_XY would come up with something)

//*MK3_PIN*/Settings.MOM(TAG_MK3_PIN, caller+": ...return np=["+np+"]");
        return np;
    }
            //}}}
    // marker_get_visible_np_near_sb() {{{
    private NotePane marker_get_visible_np_near_sb(RTabs.MWebView wv, NpButton sbX)
    {
        NotePane  np = null;
        for(Map.Entry<String, Object> entry : MARK_Map.entrySet())
        {
            if( !(entry.getValue() instanceof NotePane) ) continue;
            np = (NotePane)entry.getValue();
            if(np.button.getVisibility() != View.VISIBLE  ) continue;
            if(marker_is_sb_thumb_on_button(wv, sbX, np.button)) break; // return first found
            else                                              np = null; // try next
        }
//*MK3_PIN*/Settings.MOM(TAG_MK3_PIN, "marker_get_visible_np_near_sb: ...return ["+np+"]");
        return np;
    }
    //}}}
    // marker_get_np_thumb_y {{{
    private int marker_get_np_thumb_y(NotePane marker_np)
    {
        String mark_id = marker_get_mark_id_from_button_name( marker_np.button );
        float  thumb_p = marker_get_cookie_thumb_p(gesture_down_wv, mark_id);
        int    thumb_y = (int)(gesture_down_wv.getHeight() * thumb_p / 100f * gesture_down_wv.getScaleY());
        return thumb_y + (int) gesture_down_wv.getY();
    }
    //}}}
    // marker_is_sb_thumb_on_button {{{
    private boolean marker_is_sb_thumb_on_button(RTabs.MWebView wv, NpButton sbX, NpButton marker_np_button)
    {
//*MK3_PIN*/String caller = "marker_is_sb_thumb_on_button("+marker_np_button+")";//TAG_MK3_PIN

        if(marker_np_button.getVisibility() != View.VISIBLE) {
//*MK3_PIN*/Settings.MOM(TAG_MK3_PIN, caller+": ...return false .. (marker not VISIBLE)");
            return false;
        }

        int s_t = (int)(             sbX.getY() - wv.getY());
        int m_t = (int)(marker_np_button.getY() - wv.getY());

        int m_b = m_t + marker_np_button.getHeight();

        int  dy = Math.abs(m_b - s_t);

//MK3_PIN//Settings.MOM(TAG_MK3_PIN, caller+": m_b=["+m_b+"] ["+get_view_name(marker_np_button)+"].getHeight()=["+marker_np_button.getHeight()+"]");
//MK3_PIN//Settings.MOM(TAG_MK3_PIN, caller+": s_t=["+s_t+"]");

        boolean result = (dy <= SB_THUMB_ON_MARKER_DY_MAX);

//*MK3_PIN*/Settings.MOM(TAG_MK3_PIN, caller+": ...return "+result+": .. ("+dy +" <= "+ SB_THUMB_ON_MARKER_DY_MAX +") .. (dy <= SB_THUMB_ON_MARKER_DY_MAX)");
        return result;
    }
    //}}}
    // marker_get_wv_slice .. fs_webViewX .. f(wv) {{{
    private String marker_get_wv_slice(RTabs.MWebView wv)
    {
        return (wv == mRTabs.fs_webView3) ? "3"
            :  (wv == mRTabs.fs_webView2) ? "2"
            :                               "1";
    }
    //}}}
    // marker_get_url_slice .. fs_webViewX .. f(wv) {{{
    private String marker_get_url_slice(String url)
    {
        return Settings.URL_keywords_equals(mRTabs.fs_webView3.getUrl(), url) ? "3"
            :  Settings.URL_keywords_equals(mRTabs.fs_webView2.getUrl(), url) ? "2"
            :                                                                   "1";
    }
    //}}}
    //}}}
    //* MARKERS (CB) {{{ */
    // marker_CB {{{
    private void marker_CB(NotePane marker_np)
    {
        // [wv]<-[marker_np.button] {{{
        RTabs.MWebView wv = get_wv_containing_np_button( marker_np.button );
//*MK4_CB*/Settings.MOM(TAG_MK4_CB, "marker_CB("+ marker_np +"): wv=["+get_view_name(wv)+"])");
        if(wv == null) wv = gesture_down_wv;
//*MK4_CB*/Settings.MOM(TAG_MK4_CB, "...using gesture_down_wv=["+get_view_name(wv)+"])");
        if(wv == null) return;

        NpButton sbX = get_sb_for_wv_or_sb(wv);
//*MK4_CB*/Settings.MOM(TAG_MK4_CB, "...sbX=["+sbX+"])");
        //}}}
        // [gesture_down_marker_np_to_CB] {{{
        if(marker_np == gesture_down_marker_np_to_CB) {
//gesture_down_marker_np_to_CB = null;
///*MK4_CB*/Settings.MOM(TAG_MK4_CB, "...gesture_down_marker_np_to_CB set to ["+gesture_down_marker_np_to_CB+"] .. f(marker_CB)");
        }
        //}}}
        // ? [have_to_do] {{{
        boolean  touched_a_pinned_marker     = is_a_pinned_marker( marker_np );
      //if(      touched_a_pinned_marker)      marker_unwrap( marker_np ); // leaves marker at margin when clicked while magnetized

        boolean  touched_tool_mark           = (marker_np.name == WV_TOOL_mark       );
        boolean  touched_tool_cut_mark       = (marker_np.name == WV_TOOL_cut_mark   );
        boolean  touched_an_empty_trash      = (touched_tool_cut_mark &&  is_trash_empty());
        boolean  touched_a_loaded_trash      = (touched_tool_cut_mark && !is_trash_empty());

        NotePane near_thumb_pinned_marker_np = marker_get_visible_np_near_sb(wv, sbX);

//      boolean  sb_thumb_on_marker          = marker_is_sb_thumb_on_button(wv, sbX, marker_np.button);

        boolean  near_thumb_is_showing
            =     (near_thumb_pinned_marker_np != null)
            &&    is_view_showing( near_thumb_pinned_marker_np.button );

        boolean have_a_scroll_to_target
            =   touched_a_pinned_marker
            // !sb_thumb_on_marker
            ;

        boolean have_marker_to_cut
            =  (          touched_tool_mark || touched_an_empty_trash)
            && (near_thumb_pinned_marker_np != null                  )
            && (      near_thumb_is_showing                          );   // i.e. !not a hidden bastard!

        boolean have_marker_to_add
            =    touched_tool_mark
            &&  !have_marker_to_cut;

//*MK4_CB*/Settings.MOM(TAG_MK4_CB, "1. touched_a_pinned_marker..........=["+ touched_a_pinned_marker          +"]");
//*MK4_CB*/Settings.MOM(TAG_MK4_CB, "2. touched_tool_mark................=["+ touched_tool_mark                +"]");
//*MK4_CB*/Settings.MOM(TAG_MK4_CB, "3. near_thumb_pinned_marker_np......=["+ near_thumb_pinned_marker_np      +"]");
//*MK4_CB*/Settings.MOM(TAG_MK4_CB, "4. near_thumb_is_showing............=["+ near_thumb_is_showing            +"]");
//*MK4_CB*/Settings.MOM(TAG_MK4_CB, "5. have_a_scroll_to_target..........=["+ have_a_scroll_to_target          +"]");
//*MK4_CB*/Settings.MOM(TAG_MK4_CB, "6. have_marker_to_cut...............=["+ have_marker_to_cut               +"]");
//*MK4_CB*/Settings.MOM(TAG_MK4_CB, "7. have_marker_to_add...............=["+ have_marker_to_add               +"]");
//MK4_CB//Settings.MOM(TAG_MK4_CB, "8. sb_thumb_on_marker...............=["+ sb_thumb_on_marker               +"]");

        //}}}
        // [have_a_scroll_to_target] .. (SCROLL) {{{
        if( have_a_scroll_to_target )
        {
//*MK4_CB*/Settings.MOM(TAG_MK4_CB, "[SCROLL-TO MARKER]");
            activity_pulse_np_button( marker_np.button );
            String mark_id = marker_get_mark_id_from_button_name( marker_np.button );
            marker_scroll_wv_to_mark(wv, mark_id);
        }
        //}}}
        // 1/4 [touched_a_loaded_trash] .. (DISCARD TRASHED MARKERS) {{{
        else if( touched_a_loaded_trash )
        {
            activity_pulse_np_button( marker_np.button );
//*MK4_CB*/Settings.MOM(TAG_MK4_CB, "[EMPTY TRASH]");
            marker_empty_trash();
        }
        // }}}
        // 2/4 [have_marker_to_cut] {{{
        else if( have_marker_to_cut )
        {
//*MK4_CB*/Settings.MOM(TAG_MK4_CB, "[TOUCHED NEARBY PINNED MARKER] .. (CUT)");

            // save a text copy from this [soon-to-be-deleted-marker]
            move_marker_text_to_trash( near_thumb_pinned_marker_np );

            // clear [cookie]
            marker_del_cookie(wv, near_thumb_pinned_marker_np);

            // remove [near_thumb_pinned_marker_np]
            marker_unpin_marker_np( near_thumb_pinned_marker_np );
        }
        //}}}
        // 3/4 [have_marker_to_add] {{{
        else if( have_marker_to_add )
        {
//*MK4_CB*/Settings.MOM(TAG_MK4_CB, "[NEW THUMB-MARKER FROM USER] .. (ADD)");
            handle_marker_to_add(wv, null); // use last_cut_marker_text
        }
        //}}}
        // 4/4 HAVE NOTHING TO DO {{{
        else {
//*MK4_CB*/Settings.MOM(TAG_MK4_CB, "HAD NOTHING MORE TO DO FOR THIS CB FROM ["+marker_np+"]");
        }
        //}}}
//        // [marker_magnetized_sync] {{{
//        marker_magnetized_sync("marker_CB("+marker_np+")");
//
//        //}}}
    }
    //}}}
    // marker_scroll_wv_to_mark {{{
    private void marker_scroll_wv_to_mark(RTabs.MWebView wv, String mark_id)
    {
//*MK4_CB*/String caller = "marker_scroll_wv_to_mark("+get_view_name(wv)+", mark_id=["+mark_id+"])";//TAG_MK4_CB
//*MK4_CB*/Settings.MOC(TAG_MK4_CB, caller);

        float thumb_p  = marker_get_cookie_thumb_p  (wv, mark_id);

        if(thumb_p >= 0) marker_scroll_wv_to_thumb_p(wv, thumb_p);
    }
    //}}}
    // marker_scroll_wv_to_thumb_p {{{
    private static void marker_scroll_wv_to_thumb_p(RTabs.MWebView wv, float thumb_p)
    {
        // wv.scrollY .. f(thumb_p) {{{
        if(thumb_p < 0) return;

//*MK4_CB*/String caller = "marker_scroll_wv_to_thumb_p("+WVTools_instance.get_view_name(wv)+", "+thumb_p+")";//TAG_MK4_CB

        float wv_page_height = Math.max(wv.computeVerticalScrollRange (), 1);

        int       scrollY = (int)(wv_page_height * thumb_p / 100f);
        int       scrollX = wv.getScrollX(); // no change

//*MK4_CB*/Settings.MOC(TAG_MK4_CB, String.format("%s: thumb_p=[%2.2f], wv.scrollTo(%d, %d/%d)", caller, thumb_p, scrollX, scrollY, (int)wv_page_height));
        wv.scrollTo(scrollX, scrollY);
        WVTools_instance.sb_sync_LABEL();
        //}}}
    }
    //}}}
    //}}}
    //* MARKERS (MAGNETIZE) {{{ */
    // {{{
    private static final int MARKER_MAGNETIZED_CAPACITY          =   64;
    private static final int MARKER_MAGNETIZE_ANIM_DURATION      =  250;

  //private static final     int MARKER_MAGNETIZED_FAN_OUT_FX    =    16;
    private static final     int MARKER_SAMPLE_MAX               =     9;

    private static      NotePane marker_magnetized_np_array[]    = new NotePane[MARKER_MAGNETIZED_CAPACITY];
    private static           int marker_magnetized_np_count      =     0;
    private static      NotePane marker_magnetized_np_closest    =  null;

    private              boolean  marker_magnetized_START_DONE   = false;

    private             NotePane  magnetized_marker_np_onDown    =  null; // .. (touched while shown magnetized)

    // }}}
//    // is_magnetizing {{{
//    private boolean is_magnetizing()
//    {
//        boolean result
//            =  !property_get_WV_TOOL_MARK_LOCK    // NOT explicitly locked out by user
//            &&  is_clamping_button(     fs_search) // currently magnetizing with [fs_search]
//            && (marker_magnetized_np_count > 0   ) // currently showing some magnetized marker
//            ;
///*CLAMP*/Settings.MOM(TAG_CLAMP, ".1.....!property_get_WV_TOOL_MARK_LOCK=["+ property_get_WV_TOOL_MARK_LOCK +"]");
///*CLAMP*/Settings.MOM(TAG_CLAMP, ".2........is_clamping_button(fs_search)=["+ is_clamping_button(fs_search)   +"]");
///*CLAMP*/Settings.MOM(TAG_CLAMP, ".3...........marker_magnetized_np_count=["+ marker_magnetized_np_count      +"]");
///*CLAMP*/Settings.MOC(TAG_CLAMP, ".==return is_magnetizing()=["+result+"]");
//
//        return result;
//    }
//    //}}}
    // may_resume_magnetize {{{
    private boolean may_resume_magnetize(String caller)
    {
//*CLAMP*/caller += "->may_resume_magnetize";//TAG_CLAMP
      //boolean may_resume_magnetize
      //    =    ((floating_marker_np != null))// && (marker_magnetized_np_count > 0)) // trying not to interrupt magnetize when mm1_pick_nearby returns [0 MARKERS]
      //    ||   marker_magnetize_can_use_last_checked();

        boolean result
            =   marker_magnetize_can_use_last_checked() // last magnetize not terminated .. f(fs_search in margins)
            && !property_get_WV_TOOL_MARK_LOCK         // explicitly locked out by user
            //  is_clamping_button(     fs_search     ) // currently magnetizing with [fs_search]
            ;
//*CLAMP*/Settings.MON(TAG_CLAMP, caller, ".1........marker_magnetize_can_use_last_checked()=["+ marker_magnetize_can_use_last_checked() +"]");
//*CLAMP*/Settings.MON(TAG_CLAMP, caller, ".2...............!property_get_WV_TOOL_MARK_LOCK=["+ !property_get_WV_TOOL_MARK_LOCK        +"]");
//CLAMP//Settings.MON(TAG_CLAMP, caller, "....................is_clamping_button(fs_search)=["+ is_clamping_button( fs_search )         +"]");
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller, ".==return "+result);

        return result;
    }
    //}}}
    //* MM_1_magnetize */
    // marker_magnetize {{{
    //{{{
    private static  Magnet mm_drag_Magnet = null;
    private static     int mm_hotspot_f_x =    0;
    private static     int mm_hotspot_f_y =    0;
    private static     int mm_checked_f_x =    0;
    private static     int mm_checked_f_y =    0;

    //}}}
    // marker_magnetize {{{
    private void marker_magnetize(int f_x, int f_y, String caller)
    {
        // [hotspot_changed] .. f(animation still running) {{{
//*MK7_MAGNET*/caller += "->marker_magnetize("+f_x+", "+f_y+")";//TAG_MK7_MAGNET
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller);

        if(mm_drag_Magnet == null) mm_drag_Magnet = new Magnet( mmMagnetListener );

        if(mm_drag_Magnet.is_animating())
        {
            mm_hotspot_f_x = f_x;
            mm_hotspot_f_y = f_y;
            return;
        }
        //}}}
        // DISCARD HOTSPOT DELAYED TRACKING {{{
        else {
            mm_hotspot_f_x = 0;
            mm_hotspot_f_y = 0;

        }
        //}}}
        // [marker_magnetize_last_checked] {{{
        mm_checked_f_x = f_x;
        mm_checked_f_y = f_y;
        mm_check_done  = false;

        marker_magnetize_last_checked();
        //}}}
    }
    //}}}
    // marker_magnetize_can_use_last_checked {{{
    private boolean marker_magnetize_can_use_last_checked()
    {
        return (mm_checked_f_x != 0) || (mm_checked_f_y != 0);
    }
    //}}}
    // marker_magnetize_invalidate_last_checked {{{
    private void marker_magnetize_invalidate_last_checked(String caller)
    {
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller+"->marker_magnetize_invalidate_last_checked");
        mm_checked_f_x = 0;
        mm_checked_f_y = 0;
    }
    //}}}
    // marker_magnetize_resume {{{
    private void marker_magnetize_resume(String caller)
    {
//*MK7_MAGNET*/caller += "->XXX";//TAG_MK7_MAGNET
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller);

        marker_set_wrap_state(false);

        fs_search_wake_up(caller);
        marker_magnetize_last_checked();

        // MAKE [clamp8_drag] HAPPY .. (called from onMove_11_MARKER_MAGNETIZE_CLAMP)
        gesture_down_marker_np_to_CB   = null;

        //mm_resume(caller);
    }
    //}}}
    // marker_magnetize_last_checked {{{
    private void marker_magnetize_last_checked()
    {
        String caller = "marker_magnetize_last_checked";
//*MK7_MAGNET*/ caller = "marker_magnetize_last_checked("+mm_checked_f_x+", "+mm_checked_f_y+")";//TAG_MK7_MAGNET
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller);
        // REQUIRES UI THREAD {{{
        if(Looper.myLooper() != Looper.getMainLooper())
        {
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, "*** NOT ON UI Thread ***");

            return;
        }
        //}}}
        // MAGNETIZE {{{
        if(gesture_down_wv == null) return;
        synchronized( MARK_Map )
        {
            boolean at_left = !is_sb_at_left(); int dy_max= mm0_get_dy_max        (gesture_down_wv,                    mm_checked_f_y                  );
            /*...........................................*/ mm1_pick_nearby       (gesture_down_wv,                    mm_checked_f_y,  dy_max         ); // PICK (mag-range)
            if(marker_magnetized_np_count < 1) return;
            /*...........................................*/ mm1_sort_on_finger_dy (                                    mm_checked_f_y                  ); // ORDER (f_y->saved_y)
            /*...........................................*/ mm1_pick_cap_to       (gesture_down_wv, MARKER_SAMPLE_MAX                                  ); // CAPTO (MARKER_SAMPLE_MAX)
            /*...........................................*/ mm1_sort_on_saved_y   (                                    mm_checked_f_y                  ); // ORDER (from top to bottom)
            if(  property_get(WVTools.WV_TOOL_FLAG_WEIGHT) ) mm2_set_weight       (                                                     dy_max         );
            if( !property_get(WVTools.WV_TOOL_FLAG_SCALE ) ) mm3_set_s_to         (gesture_down_wv, mm_checked_f_x,                     dy_max, at_left);
            if( !property_get(WVTools.WV_TOOL_FLAG_X     ) ) mm4_set_x_to         (gesture_down_wv, mm_checked_f_x,                     dy_max, at_left);
            if( !property_get(WVTools.WV_TOOL_FLAG_Y     ) ) mm5_set_y_to         (                                    mm_checked_f_y                  );
            if( !property_get(WVTools.WV_TOOL_FLAG_CENTER) ) mm6_recenter         (gesture_down_wv);

            if(mm_drag_Magnet == null) mm_drag_Magnet = new Magnet( mmMagnetListener );

            fs_search_wake_up(caller);
            mm_drag_Magnet.animate( MARKER_MAGNETIZE_ANIM_DURATION );
        }
        //}}}
    }
    //}}}
    // mm_drag_Magnet_is_animating {{{
    private boolean mm_drag_Magnet_is_animating()
    {
        return (mm_drag_Magnet == null) ? false : mm_drag_Magnet.is_animating();
    }
    //}}}
    //}}}
    // mm1_pick_nearby .. (save_from) {{{
    private void mm1_pick_nearby(RTabs.MWebView wv, int f_y, int dy_max)
    {
        // [xy] [wv layout] [markers border] {{{
        String caller = "mm1_pick_nearby(f_y="+f_y+")";

        int            margin_x =  marker_get_wv_mark_x(wv); // markers parked x

        //}}}
        // [marker_magnetized_np_count] [marker_magnetized_np_closest] {{{
        marker_magnetized_np_count   = 0;
        marker_magnetized_np_closest = null;
        int     closest_marker_np_dy = dy_max;

        //}}}
        // [wv] [visible markers] {{{
        String wv_key_pfx = WV_TOOL_mark +"."+ marker_get_wv_slice( wv );
//*MAGNET*/Settings.MOM(TAG_MAGNET, "wv_key_pfx=["+wv_key_pfx+"]");
        for(Map.Entry<String, Object> entry : MARK_Map.entrySet())
        {
            // SELECT slice pinned markers {{{
            NotePane np = marker_get_pinned_entry(wv_key_pfx, entry);
            if(np == null              ) continue;
            if(np == floating_marker_np) continue;

            //}}}
            // FIXME .. (should not be required .. see why someone_has_called_hide_marks is not acted upon)
            // ..XXX .. added a call to [marker_wv_sync_no_delay] in [onClamped_3_FLOAT_MARKER]
            // ..XXX .. not good: wrong saved scale and fs_search pos not right
            np.button.setVisibility( View.VISIBLE );
            // PICK nearby markers {{{
            int                     saved_y = (int)np.button.get_saved_y();
            float     dy = Math.abs(saved_y - f_y);
            if((dy <= dy_max) && (marker_magnetized_np_count < MARKER_MAGNETIZED_CAPACITY))
            {
                np.set_weight(saved_y - f_y);

//*MAGNET*/Settings.MON(TAG_MAGNET, "", "SAVING ATTITUDE AND LOCATION OF ["+np.button+"]:");
//*MAGNET*/Settings.MOM(TAG_MAGNET, np.button.description      ());
//*MAGNET*/Settings.MOM(TAG_MAGNET, np.button.description_saved());
                np.button.save_from(caller);
//*MAGNET*/Settings.MOM(TAG_MAGNET, np.button.description_from());

                marker_magnetized_np_array[marker_magnetized_np_count]  = np;
                marker_magnetized_np_count                             += 1;

                if(dy < closest_marker_np_dy)
                {
                    closest_marker_np_dy    = (int)dy;
                    marker_magnetized_np_closest = np;
                }
            }
            //}}}
            // PARK others (possibly magnetized) {{{
            else {
              np.button.setX     ( margin_x );
              np.button.setY     (  saved_y );
              np.button.setScaleX(       1f );
              np.button.setScaleY(       1f );
            }
            //}}}
        }
        //}}}
        // [marker_magnetized_np_closest] (bringToFront) {{{
        if(marker_magnetized_np_closest != null)
            marker_magnetized_np_closest.button.bringToFront();

        //}}}
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, "["+marker_magnetized_np_count+" MARKERS] CLOSEST=["+marker_magnetized_np_closest+"]");
    }
    //}}}
    // mm1_sort_on_finger_dy {{{
    private void mm1_sort_on_finger_dy(final int f_y)
    {
//*MK7_MAGNET*/String caller = "mm1_sort_on_finger_dy(f_y="+f_y+")";//TAG_MK7_MAGNET
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, "["+marker_magnetized_np_count+" MARKERS]");
        // ORDER MAGNETIZED MARKERS ON VERTICAL-DISTANCE-TO-FINGER [f_y] {{{
        for(int i = marker_magnetized_np_count; i < MARKER_MAGNETIZED_CAPACITY; ++i)
        {
            marker_magnetized_np_array[i] = null;
        }
        Arrays.sort( marker_magnetized_np_array
                , new Comparator<NotePane>() {
                    @Override
                    public int compare(NotePane lhs, NotePane rhs)
                    {
                        if     ((lhs   == null) && (rhs == null))        return  0;
                        else if(                   (rhs == null))        return -1; // ..lhs (exists)
                        else if((lhs   == null)                 )        return  1; // ..rhs (exists)
                        float lhs_dy    = Math.abs(lhs.button.get_saved_y() - f_y); // how far they are from finger hot spot
                        float rhs_dy    = Math.abs(rhs.button.get_saved_y() - f_y); // how far they are from finger hot spot
                        return (lhs_dy == rhs_dy) ?                              0  // same distance
                            :  (lhs_dy <= rhs_dy) ?                             -1  // ..lhs (first)
                            :                                                    1; // ..rhs (next)

                    }
                });

        //}}}
    }
    //}}}
    // mm1_pick_cap_to {{{
    private void mm1_pick_cap_to(RTabs.MWebView wv, int max)
    {
//*MK7_MAGNET*/String caller = "mm1_pick_cap_to(max="+max+")";//TAG_MK7_MAGNET
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller,"["+marker_magnetized_np_count+" MARKERS]");

        if(max >= marker_magnetized_np_count)
        {
//*MK7_MAGNET*/Settings.MOM(TAG_MK7_MAGNET, "..................keeping all:["+ marker_magnetized_np_count       +" MARKERS]");
            return;
        }

//*MK7_MAGNET*/Settings.MOM(TAG_MK7_MAGNET, "...................discarding:["+(marker_magnetized_np_count - max)+" MARKERS]");
        int margin_x = marker_get_wv_mark_x(wv); // markers parked x
        for(int i = max; i < marker_magnetized_np_count; ++i)
        {
            // PARK discarded (possibly magnetized)
            NotePane np = marker_magnetized_np_array[i];
            int saved_y = (int)np.button.get_saved_y();

            np.button.setX     ( margin_x );
            np.button.setY     (  saved_y );
            np.button.setScaleX(       1f );
            np.button.setScaleY(       1f );

            marker_magnetized_np_array[i] = null;
        }

        marker_magnetized_np_count = max;
//*MK7_MAGNET*/Settings.MOM(TAG_MK7_MAGNET, ".................maxed out to:["+ marker_magnetized_np_count       +" MARKERS]");
    }
    //}}}
    // mm1_sort_on_saved_y {{{
    private void mm1_sort_on_saved_y(final int f_y)
    {
        // ORDER MAGNETIZED MARKERS ON VERTICAL-POSITION [m_y] {{{
//*MK7_MAGNET*/String caller = "mm1_sort_on_saved_y(f_y="+f_y+")";//TAG_MK7_MAGNET
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller,"["+marker_magnetized_np_count+" MARKERS]");

        Arrays.sort( marker_magnetized_np_array
                , new Comparator<NotePane>() {
                    @Override
                    public int compare(NotePane lhs, NotePane rhs)
                    {
                        if     ((lhs  == null) && (rhs == null)) return  0;
                        else if(                  (rhs == null)) return -1; // ..lhs (exists)
                        else if((lhs  == null)                 ) return  1; // ..rhs (exists)
                        float lhs_y    = lhs.button.get_saved_y();          // how hight they are on screen
                        float rhs_y    = rhs.button.get_saved_y();          // how hight they are on screen
                        return (lhs_y == rhs_y) ?                        0  // same distance
                            :  (lhs_y <= rhs_y) ?                       -1  // ..lhs (first)
                            :                                            1; // ..rhs (next)

                    }
                });

        //}}}
    }
    //}}}
    // mm0_get_dy_max {{{
    private int mm0_get_dy_max(RTabs.MWebView wv, int f_y)
    {
        // MARKERS PICK LOOKUP DY
//*MK7_MAGNET*/String caller = "mm0_get_dy_max(f_y="+f_y+")";//TAG_MK7_MAGNET
//        // METHOD #1 .. f(FIXED # TOOL_BADGE_SIZE) {{{
//        int dy_max =  2 * Settings.TOOL_BADGE_SIZE; // magnetized (detection) away from finger
///*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller,"...dy_max=["+dy_max+"]");
//      //return        4 * Settings.TOOL_BADGE_SIZE; // magnetized (rendering) away from finger
//      //return       20 * Settings.TOOL_BADGE_SIZE; // magnetized (detection) away from finger
//
//        //}}}
//        // METHOD #2 .. [top-most]->[bot-most] {{{
//        int  top = f_y;
//        int  bot = f_y;
//        for(int i=0; i < marker_magnetized_np_count; ++i)
//        {
//            int  y = f_y + marker_magnetized_np_array[i].get_weight();
//            if(top > y) top = y; // smallest Y
//            if(bot < y) bot = y; // biggest  Y
//        }
//
//        return Math.max(bot - top, Settings.TOOL_BADGE_SIZE);
//        //}}}
//        // METHOD #3 .. f(CAPPED [top-most]->[bot-most] {{{
//        marker_magnetized_np_closest = null;
//        int     closest_marker_np_dy = dy_max;
//        String wv_key_pfx = WV_TOOL_mark +"."+ marker_get_wv_slice( wv );
///*MK7_MAGNET*/Settings.MOM(TAG_MK7_MAGNET, "wv_key_pfx=["+wv_key_pfx+"]");
//        for(Map.Entry<String, Object> entry : MARK_Map.entrySet())
//        {
//            // SELECT slice pinned markers {{{
//            np = marker_get_pinned_entry(wv_key_pfx, entry);
//            if(np == null              ) continue;
//            if(np == floating_marker_np) continue;
//
//            //}}}
//            int                     saved_y = (int)np.button.get_saved_y();
//            float     dy = Math.abs(saved_y - f_y);
//            if((dy <= dy_max) && (marker_magnetized_np_count < MARKER_MAGNETIZED_CAPACITY))
//                if(dy < closest_marker_np_dy)
//                {
//                    closest_marker_np_dy    = (int)dy;
//                    marker_magnetized_np_closest = np;
//                }
//        }
//
//        return Math.max(bot - top, Settings.TOOL_BADGE_SIZE);
//        //}}}
    // METHOD #5 .. f(webview height) .. (capped by mm1_pick_cap_to)
        int    dy_max =  Settings.DISPLAY_H;
        return dy_max;
    }
    //}}}
    //* MM_2_weight */
    // mm2_set_weight {{{
    private void mm2_set_weight(int dy_max)
    {
        // SPLIT Y EQUALLY {{{
//*MK7_MAGNET*/String caller = "mm2_set_weight(dy_max="+dy_max+")";//TAG_MK7_MAGNET

        int   dy = dy_max / MARKER_SAMPLE_MAX;
      //int   dy = (int)(dy_max / (marker_magnetized_np_count-1));

        //}}}
        // [mid] {{{
        int  mid =      (marker_magnetized_np_count+1) / 2;
        for(int i=0; i < marker_magnetized_np_count; ++i)
        {
            if(marker_magnetized_np_array[i] == marker_magnetized_np_closest) {
                mid = i;
                break;
            }
        }
        //}}}
        // .top...[mid]->[bot] {{{
        int next_dy = 0;
        for(int   i = mid+0; i < marker_magnetized_np_count; ++i)
        {
            NpButton np_button = marker_magnetized_np_array[i].button;
            marker_magnetized_np_array[i].set_weight( next_dy );
            next_dy += dy; // post-increment
        }
        //}}}
        // [top]<-[mid]...bot. {{{
        next_dy     = 0;
        for(int   i = mid-1; i >= 0; --i)
        {
            NpButton np_button =  marker_magnetized_np_array[i].button;
            next_dy -= dy; // pre-increment
            marker_magnetized_np_array[i].set_weight( next_dy );
        }
        //}}}
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, "["+marker_magnetized_np_count+" MARKERS] mid=["+mid+"] ("+marker_magnetized_np_array[mid]+")");
    }
    //}}}
    //* MM_3_scale */
    // mm3_set_s_to {{{
    private void mm3_set_s_to(RTabs.MWebView wv,int f_x, int dy_max, boolean at_left)
    {
        // [dx_max] {{{
//*MK7_MAGNET*/String caller = "mm3_set_s_to(f_x="+f_x+", dy_max="+dy_max+", at_left="+at_left+")";//TAG_MK7_MAGNET

        int        wv_x = (int)wv.getX();                   // [wv] left x
        int        wv_w =      wv.getWidth();               // [wv] width

        int      dx_max = at_left ? (f_x - wv_x) : (wv_x + wv_w - f_x); // finger distance to [wv] markers border

        //}}}
        // [scale_max] {{{
        float scale_max = (float)dx_max / (float)wv_w * Settings.MARK_SCALE_GROW * 2;
        //}}}
        // SCALE MARKERS {{{
        for(int i= 0; i < marker_magnetized_np_count; ++i) // from closest to farthest
        {
            NpButton    np_button = marker_magnetized_np_array[i].button;

            // [dy]
            float              dy = marker_magnetized_np_array[i].get_weight();

            // [scale]
            float     distance_fx = Math.abs(dy / dy_max);
            distance_fx           = Math.min(1.2f, distance_fx);
            float        scale_fx = 1f - distance_fx;
            float           scale = 1f + Math.abs(scale_max * scale_fx);

            // SCALE UPPER CAP .. (capped so that spread XY will separate markers)
            //scale = Math.min(scale, Settings.MARK_SCALE_GROW);
            // SCALE WRAP
            if( scale > Settings.MARK_SCALE_GROW)
                scale = Settings.MARK_SCALE_GROW - (scale - Settings.MARK_SCALE_GROW);

            // SCALE LOWER CAP
            scale = Math.max(scale, 1f);

            // [scale XY]
            np_button.set_s_to( scale );
        }
        //}}}
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, "["+marker_magnetized_np_count+" MARKERS] scale_max=["+scale_max+"]");
    }
    //}}}
    //* MM_4_x */
    // mm4_set_x_to {{{
    private void mm4_set_x_to(RTabs.MWebView wv,int f_x, int dy_max, boolean at_left)
    {
//      // [offsetX_max] .. f(f_x) {{{
//      int        wv_x = (int)wv.getX();                  // [wv] left x
//      int        wv_w =      wv.getWidth();              // [wv] width
//      int offsetX_max = at_left ? (f_x - wv_x) : (wv_x + wv_w - f_x); // finger distance to [wv] markers border
//      //}}}
        // [offsetX_max] .. f(f_x) {{{
//*MK7_MAGNET*/String caller = "mm4_set_x_to(f_x="+f_x+", dy_max="+dy_max+", at_left="+at_left+")";//TAG_MK7_MAGNET

        int        wv_x = (int)wv.getX();                  // [wv] left x
        int        wv_w =      wv.getWidth();              // [wv] width
        int offsetX_max = at_left ? (f_x - wv_x) : (wv_x + wv_w - f_x); // finger distance to [wv] markers border
        int         m_x = at_left ? (      wv_x) : (wv_x + wv_w      );
        //}}}
        // [set_x_to] {{{
        float m_w = 0;
        for(int i=0; i < marker_magnetized_np_count; ++i)
        {
            NpButton np_button = marker_magnetized_np_array[i].button;
            if(m_w == 0) m_w = np_button.get_saved_w();

            // [offset_x]
            float          dy = Math.abs(marker_magnetized_np_array[i].get_weight());
            float distance_fx =          dy / dy_max;
            float    offset_x = m_w + offsetX_max * (1f - distance_fx);
//offset_x = 100;//XXX
            // f(side offset)
            if( !at_left ) {
                offset_x  *=  -1;
                offset_x  -= m_w;
            }

            // [set_x_to]
            np_button.set_x_to(m_x + offset_x);
//np_button.set_x_to(f_x + offset_x);
        }
        //}}}
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, "["+marker_magnetized_np_count+" MARKERS] offsetX_max=["+offsetX_max+"]");
    }
    //}}}
    //* MM_5_y */
    // mm5_set_y_to {{{
    private void mm5_set_y_to(int f_y)
    {
        // [mid] {{{
//*MK7_MAGNET*/String caller = "mm5_set_y_to(f_y="+f_y+")";//TAG_MK7_MAGNET

        int  mid =      (marker_magnetized_np_count+1) / 2;
        for(int i=0; i < marker_magnetized_np_count; ++i)
        {
            if(marker_magnetized_np_array[i] == marker_magnetized_np_closest) {
                mid = i;
                break;
            }
        }
      //mid = 0;//XXX
      //int mid_to_finger_dy = marker_magnetized_np_array[mid].get_weight();
        int mid_to_finger_dy = 0;

        //}}}
        // [set_y_to] .. (mid -> bot) {{{
        int next_dy = 0;
        for(int   i = mid+0; i < marker_magnetized_np_count; ++i)
        {
            NpButton np_button = marker_magnetized_np_array[i].button;

            int m_h  = (int)(np_button.getHeight() * np_button.get_s_to());
            np_button.set_y_to(f_y + next_dy);
            next_dy += m_h; // post-increment
        }
        //}}}
        // [set_y_to] .. (top <- mid) {{{
        next_dy = 0;
        for(int   i = mid-1; i >= 0; --i)
        {
            NpButton np_button = marker_magnetized_np_array[i].button;

            int m_h  = (int)(np_button.getHeight() * np_button.get_s_to());
            next_dy -= m_h; // pre-increment
            np_button.set_y_to(f_y + next_dy);
        }
        //}}}
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, "["+marker_magnetized_np_count+" MARKERS] mid=["+mid+"] ("+marker_magnetized_np_array[mid]+")");
    }
    //}}}
    //* MM_6_center */
    // mm6_recenter .. (set_x_to) (set_y_to) {{{
    private void mm6_recenter(RTabs.MWebView wv)
    {
        // webview boundaries {{{
//*MK7_MAGNET*/String caller = "mm6_recenter";//TAG_MK7_MAGNET
        int wv_top    =      (int)wv.getY     ();
        int wv_left   =      (int)wv.getX     ();
        int wv_right  = wv_left + wv.getWidth ();
        int wv_bottom = wv_top  + wv.getHeight();

        //}}}
        // RECTANGLE CONTAINING BUTTONS OVERFLOWING WEBVIEW BOUNDARIES {{{
        int y_min    = wv_top   ;
        int x_min    = wv_left  ;
        int x_max    = wv_right ;
        int y_max    = wv_bottom;

        for(int i=0; i < marker_magnetized_np_count; ++i) // move all buttons down (positive dy)
        {
            NpButton np_button = marker_magnetized_np_array[i].button;

            int              x = (int)np_button.get_x_to ();
            int              y = (int)np_button.get_y_to ();
            int              w =      np_button.getWidth ();
            int              h =      np_button.getHeight();

            float        scale = np_button.get_s_to();

            x_min              = Math.min(x_min, x                   );
            y_min              = Math.min(y_min, y - (int)(h * scale));
            x_max              = Math.max(x_max, x + (int)(w * scale));
          //y_max              = Math.max(y_max, y + (int)(h * scale));
            y_max              = Math.max(y_max, y                   );

        }
        //}}}
        // [x_drift] [y_drift] {{{
        int x_drift
            = (x_max > wv_right ) ? (wv_right  - x_max)  // (negative) drift left
            : (x_min < wv_left  ) ? (wv_left   - x_min)  // (positive) drift right
            :                       (0                ); // ........no drift required (all buttons within wv boundaries)

        int y_drift
            = (y_max > wv_bottom) ? (wv_bottom - y_max)  // (negative) drift top
            : (y_min < wv_top   ) ? (wv_top    - y_min)  // (positive) drift bottom
            :                       (0                ); // ........no drift required (all buttons within wv boundaries)

        //}}}
        // RECENTER OVERFLOWING BUTTONS {{{
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, "drift XY=["+x_drift+" "+y_drift+"]");
        if((x_drift != 0) || (y_drift != 0))
        {
            for(int i=0; i < marker_magnetized_np_count; ++i)
            {
                NpButton np_button = marker_magnetized_np_array[i].button;

                int              x = (int)np_button.get_x_to();
                int              y = (int)np_button.get_y_to();

                np_button.set_x_to(x + x_drift);
                np_button.set_y_to(y + y_drift);
            }
        }
        //}}}
    }
    //}}}
    //* MM_7_park */
    // marker_magnetize_park {{{
    private void marker_magnetize_park(                   String caller) { marker_magnetize_park(gesture_down_wv, caller); }
    private void marker_magnetize_park(RTabs.MWebView wv, String caller)
    {
        // TODO: ANIMATE {{{
        caller  += "->marker_magnetize_park";
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller);

// TODO ANIMATE .. (requires a substitute to marker_magnetized_np_count which have been set to 0)
//  XXX WORKS BADLY .. WHY?
//if(Looper.myLooper() == Looper.getMainLooper())
//    mm_drag_animate( MARKER_MAGNETIZE_ANIM_DURATION );
//XXX WORKS BADLY .. WHY?

        //}}}
        _mm_set_all_parked(wv, caller);
    }
    //}}}
    // _mm_set_all_parked {{{
    private void _mm_set_all_parked(RTabs.MWebView wv, String caller)
    {
        // PARK MARKERS .. f(visible markers) {{{

        caller += "->_mm_set_all_parked";
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, "...wv=["+wv+"]");
        if(wv == null)
        {
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, "...mRTabs.fs_webView=["+mRTabs.fs_webView+"]");
            wv = mRTabs.fs_webView;
        }
        if(wv == null) return;

        synchronized( MARK_Map )
        {
            boolean   at_left = !is_sb_at_left();
            String wv_key_pfx = WV_TOOL_mark +"."+ marker_get_wv_slice ( wv );
//*MK7_MAGNET*/Settings.MOM(TAG_MK7_MAGNET, "wv_key_pfx=["+wv_key_pfx+"]");
            float         m_x =                    marker_get_wv_mark_x( wv );
            NotePane       np = null;
            for(Map.Entry<String, Object> entry : MARK_Map.entrySet())
            {
                // SELECT slice pinned markers {{{
                np = marker_get_pinned_entry(wv_key_pfx, entry);
                if(np == null              ) continue;
                if(np == floating_marker_np) continue;

                //}}}
                if( is_floating_marker( np )                      ) continue;
                // PARK selected markers [m_x] [saved_y] [scale=1] {{{
                int m_y = (int)np.button.get_saved_y();
                int m_w = (int)np.button.get_saved_w();

                boolean park_animated = false;

                np.button.setRotationX(  0f );
                np.button.setRotationY(  0f );
                np.button.setPivotX(at_left ? 0 : m_w);

                //for(int i=0; i < marker_magnetized_np_count; ++i)
                //{
                //    if( marker_magnetized_np_array[i] == np)
                //    {
                //        np.button.set_x_to ( m_x );
                //        np.button.set_y_to ( m_y );
                //        np.button.set_s_to (  1f );
                //        break;
                //    }
                //}

                if( !park_animated )
                {
                    np.button.setX        ( m_x );
                    np.button.setY        ( m_y );
                    np.button.setScaleX   (  1f );
                    np.button.setScaleY   (  1f );
                }

                //}}}
            }
        }
        //}}}
        marker_magnetized_np_count   = 0;
        marker_magnetized_np_closest = null;
    }
    //}}}
    //}}}
    //* MARKERS (FLOAT) {{{ */
    // FLOAT MARKER STAGE {{{
    // {{{
    private static final int MARKER_STAGE0_STANDBY       = 0;
    private static final int MARKER_STAGE1_UNLOCKING     = 1;
    private static final int MARKER_STAGE2_UNLOCKED      = 2;
    private static final int MARKER_STAGE3_BAK_LOCK      = 3;
    private static final int MARKER_STAGE4_NEW_LOCK      = 4;
    private static final int MARKER_STAGE5_LOCKED        = 5;

    private static       int floating_marker_stage      = MARKER_STAGE0_STANDBY;
    private static  NotePane floating_marker_np         = null;

    //}}}
    // marker_float_get_stage_str {{{
    private String marker_float_get_stage_str(int marker_stage)
    {
        switch(marker_stage)
        {
            case MARKER_STAGE0_STANDBY  : return "STAGE0_STANDBY"  ;
            case MARKER_STAGE1_UNLOCKING: return "STAGE1_UNLOCKING";
            case MARKER_STAGE2_UNLOCKED : return "STAGE2_UNLOCKED" ;
            case MARKER_STAGE3_BAK_LOCK : return "STAGE3_BAK_LOCK" ;
            case MARKER_STAGE4_NEW_LOCK : return "STAGE4_NEW_LOCK" ;
            case MARKER_STAGE5_LOCKED   : return "STAGE5_LOCKED"   ;
            default:                      return "STAGE6_ERROR***" ;
        }
    }
    //}}}
    // marker_float_get_stage_label {{{
    private String marker_float_get_stage_label(int marker_stage)
    {
        switch( marker_stage )
        {
            case MARKER_STAGE0_STANDBY  : return "STANDBY"        ;
            case MARKER_STAGE1_UNLOCKING: return "UNLOCKING"      ;
            case MARKER_STAGE2_UNLOCKED : return "UNLOCKED"       ;
            case MARKER_STAGE3_BAK_LOCK : return "BAK_LOCK"       ;
            case MARKER_STAGE4_NEW_LOCK : return "NEW_LOCK"       ;
            case MARKER_STAGE5_LOCKED   : return "LOCKED"         ;
            default:                      return "STAGE_ERROR***" ;
        }
    }
    //}}}
    // marker_float_SET_STAGE {{{
    private void marker_float_SET_STAGE(int marker_stage, NotePane marker_np, String reason, String caller)
    {
        floating_marker_stage   = marker_stage;

        String  stage_label = marker_float_get_stage_label( marker_stage );

        String marker_label = (marker_np != null) ? "["+marker_np.getLabel()+"]" : Settings.EMPTY_STRING;

        String         text = String.format("%10s%s floating=[%s] pending=[%s]", stage_label, marker_label, floating_marker_np, floating_marker_np_pending);

//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT, caller, reason);
//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT, ""    , text);

        sb_show_text( text );
    }
    //}}}
    // marker_float_onStageChanged {{{
    //{{{
    // - called by [onClamped_3_FLOAT_MARKER]
    // -        or [swing_3_reached]
    // - when they're done with their animation.
    // - next step depends on [floating_marker_stage]
    //
    //}}}
    private void marker_float_onStageChanged(String caller)
    {
//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT, caller+"->marker_float_onStageChanged", marker_float_get_stage_str(floating_marker_stage));
        caller = "marker_float_onStageChanged"; // (truncate callers chain)

        switch( floating_marker_stage ) {
            case MARKER_STAGE0_STANDBY  : marker_float0_STANDBY  (                    caller ); break;
            case MARKER_STAGE1_UNLOCKING: marker_float1_UNLOCKING(floating_marker_np, caller ); break;
            case MARKER_STAGE2_UNLOCKED : marker_float2_UNLOCKED (                    caller ); break;
            case MARKER_STAGE3_BAK_LOCK : marker_float3_BAK_LOCK (                    caller ); break;
            case MARKER_STAGE4_NEW_LOCK : marker_float4_NEW_LOCK (                    caller ); break;
            case MARKER_STAGE5_LOCKED   : marker_float5_LOCKED   (                    caller ); break;
        }
    }
    //}}}
    // marker_float0_STANDBY {{{
    private void marker_float0_STANDBY(String caller)
    {
        // LOG CURRENT STATE PARAMETERS {{{
        caller += "->marker_float0_STANDBY";

//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT, caller, "CURRENT STATE"+ Settings.TRACE_OPEN);

//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT, "SCROLLBAR");
//*MK5_FLOAT*/if( sb_CB_has_been_called_since_onDown          ) Settings.MOC(TAG_MK5_FLOAT, ""    , "sb_CB_has_been_called_since_onDown");
//*MK5_FLOAT*/if( sb_follow_finger_anim_called                ) Settings.MOC(TAG_MK5_FLOAT, ""    , "sb_follow_finger_anim_called");
//*MK5_FLOAT*/if( sb_has_paged_up_or_down                     ) Settings.MOC(TAG_MK5_FLOAT, ""    , "sb_has_paged_up_or_down");
//*MK5_FLOAT*/if( sb_has_called_findNext                      ) Settings.MOC(TAG_MK5_FLOAT, ""    , "sb_has_called_findNext");
//*MK5_FLOAT*/if( sb_anim_interrupted                         ) Settings.MOC(TAG_MK5_FLOAT, ""    , "sb_anim_interrupted");
//MK5_FLOAT//if( sb_follow_finger_clamped                    ) Settings.MOC(TAG_MK5_FLOAT, ""    , "sb_follow_finger_clamped");
//MK5_FLOAT//if( sb_check_done                               ) Settings.MOC(TAG_MK5_FLOAT, ""    , "sb_check_done");

//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT, "MARKERS");
//*MK5_FLOAT*/if( marker_has_expanded_since_onDown            ) Settings.MOC(TAG_MK5_FLOAT, ""    , "marker_has_expanded_since_onDown");
//*MK5_FLOAT*/if( marker_has_spread_since_onDown              ) Settings.MOC(TAG_MK5_FLOAT, ""    , "marker_has_spread_since_onDown");
//*MK5_FLOAT*/if( marker_magnetized_START_DONE                ) Settings.MOC(TAG_MK5_FLOAT, ""    , "marker_magnetized_START_DONE");
//*MK5_FLOAT*/if( marker_wrap_state                           ) Settings.MOC(TAG_MK5_FLOAT, ""    , "marker_wrap_state");
//*MK5_FLOAT*/if( mm_check_done                               ) Settings.MOC(TAG_MK5_FLOAT, ""    , "mm_check_done");
//*MK5_FLOAT*/if( mm_check_pending                            ) Settings.MOC(TAG_MK5_FLOAT, ""    , "mm_check_pending");
//*MK5_FLOAT*/if(floating_marker_np                    != null) Settings.MOC(TAG_MK5_FLOAT, ""    , "...................floating_marker_np=["+floating_marker_np        +"]");
//*MK5_FLOAT*/if(marker_magnetized_np_count            >  0   ) Settings.MOC(TAG_MK5_FLOAT, ""    , "...........marker_magnetized_np_count=["+marker_magnetized_np_count+"]");
//*MK5_FLOAT*/if(marker_magnetize_can_use_last_checked()      ) Settings.MOC(TAG_MK5_FLOAT, ""    , "marker_magnetize_can_use_last_checked");

//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT, "EVENTS");
//*MK5_FLOAT*/if( opted_to_steal_events                       ) Settings.MOC(TAG_MK5_FLOAT, ""    , "opted_to_steal_events");
//*MK5_FLOAT*/if( has_moved_since_onDown                      ) Settings.MOC(TAG_MK5_FLOAT, ""    , "has_moved_since_onDown");
//*MK5_FLOAT*/if( waiting_for_ACTION_POINTER_UP               ) Settings.MOC(TAG_MK5_FLOAT, ""    , "waiting_for_ACTION_POINTER_UP");
//*MK5_FLOAT*/if(someone_has_called_hide_marks         != null) Settings.MOC(TAG_MK5_FLOAT, ""    , "someone_has_called_hide_marks........=["+someone_has_called_hide_marks        +"]");
//*MK5_FLOAT*/if(someone_has_invalidated_marker_colors != null) Settings.MOC(TAG_MK5_FLOAT, ""    , "someone_has_invalidated_marker_colors=["+someone_has_invalidated_marker_colors+"]");

//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT, ""    , "CURRENT STATE"+ Settings.TRACE_CLOSE);
        //}}}
        // [RESUME MAGNETIZE] .. f(marker_magnetized_np_count) {{{
        if( may_resume_magnetize(caller) )
        {
//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT, caller, "MAGNETIZE: RESUME");

            marker_magnetize_resume(caller);
        }
        //}}}
//        // [CANCEL ANY RUNNING ANIMATION] {{{
//        if((swing_Magnet != null) && swing_Magnet_is_animating())
//            swing_Magnet_cancel_animation(floating_marker_np.button, caller);
//
//        //}}}
//        // [SET MARKER_STAGE0_STANDBY] {{{
//        caller += "->marker_float0_STANDBY";
//
//        marker_float_SET_STAGE(MARKER_STAGE0_STANDBY, floating_marker_np, marker_float_get_stage_str(floating_marker_stage)+" DONE", caller);
//        //}}}
    }
    //}}}
    // marker_float1_UNLOCKING {{{
    private static  NotePane floating_marker_np_pending = null;
    private void marker_float1_UNLOCKING(NotePane marker_np, String caller)
    {
        // SET MARKER_STAGE1_UNLOCKING .. (RELOCK PREVIOUS) {{{
        caller += "->marker_float1_UNLOCKING("+marker_np+")";

        if(        (floating_marker_np_pending == null     )
                && (floating_marker_np         != null     )
                && (floating_marker_np         != marker_np)
          )
        {
            // [floating_marker_np_pending] (PUT ON HOLD PENDING MARKER) {{{
            floating_marker_np_pending = marker_np;
//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT, caller, "..PUT ON HOLD PENDING  MARKER ["+floating_marker_np_pending+"]");

            //}}}
            marker_float_SET_STAGE(MARKER_STAGE1_UNLOCKING, marker_np, "RELOCK PREVIOUS", caller);
            // SWING FLOATING MARKER TO LOCKED LOCATION {{{
            swing_np_3_to_saved_xy(); // ->[swing_3_reached]->[marker_float_onStageChanged]

            //}}}
        }
        //}}}
        // SET MARKER_STAGE2_UNLOCKED .. (UNLOCK NEW) {{{
        else {
            // [RELOCK PREVIOUS MARKER DONE] {{{
            if(floating_marker_np_pending != null)
            {
//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT, caller, "RELOCK PREVIOUS MARKER DONE ["+floating_marker_np+"]");

                floating_marker_np         = floating_marker_np_pending;
                floating_marker_np_pending = null;
            }
            else {
                floating_marker_np         = marker_np;

            }
            //}}}
            marker_float_SET_STAGE(MARKER_STAGE2_UNLOCKED, floating_marker_np, "MARKER_STAGE1_UNLOCKING: DONE", caller);
            marker_float_onStageChanged(caller);
        }
        //}}}
    }
    //}}}
    // marker_float2_UNLOCKED {{{
    private void marker_float2_UNLOCKED(String caller)
    {
        // SET MARKER_STAGE2_UNLOCKED .. (EXPAND AND SWING FLOATING MARKER TO UNLOCK LOCATION) {{{
        caller += "->marker_float2_UNLOCKED";

        if((floating_marker_np != null) && !marker_is_expanded( floating_marker_np ))
        {
            marker_float_SET_STAGE(MARKER_STAGE2_UNLOCKED, floating_marker_np, "UNLOCK MARKER", caller);
            // EXPAND FLOATING MARKER {{{
            if(floating_marker_np != null)
            {
//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT , caller, "MARKER_STAGE2_UNLOCKED: EXPAND FLOATING MARKER");

                boolean  at_left = !is_sb_at_left();
                float scale_grow = Settings.MARK_SCALE_GROW;
                marker_expand(floating_marker_np, scale_grow, at_left);
            }
            //}}}
            // SWING FLOATING MARKER TO UNLOCK LOCATION {{{
            if(floating_marker_np != null)
            {
//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT , caller, "MARKER_STAGE2_UNLOCKED: SWING FLOATING MARKER TO UNLOCK LOCATION");

                swing_np_1_to_unlocked(); // ->[swing_3_reached]->[marker_float_onStageChanged]
            }
            //}}}
        }
        //}}}
        // SET MARKER_STAGE0_STANDBY {{{
        else {
            marker_float_SET_STAGE(MARKER_STAGE0_STANDBY, floating_marker_np, "MARKER_STAGE2_UNLOCKED: DONE", caller);

            marker_float_onStageChanged(caller);
        }
        //}}}
    }
    //}}}
    // marker_float3_BAK_LOCK {{{
    private void marker_float3_BAK_LOCK(String caller)
    {
        // SET MARKER_STAGE3_BAK_LOCK .. (SWING FLOATING MARKER TO SAVED SCALE) {{{
        caller += "->marker_float3_BAK_LOCK";

        if((floating_marker_np != null) && !marker_at_saved_scale( floating_marker_np ))
        {
            marker_float_SET_STAGE(MARKER_STAGE3_BAK_LOCK, floating_marker_np, "SWING FLOATING MARKER TO SAVED SCALE ["+floating_marker_np+"]", caller);

            swing_np_2_to_saved_scale(); // ->[swing_3_reached]->[marker_float_onStageChanged]
        }
        //}}}
        else {
            // SET MARKER_STAGE5_LOCKED  {{{
            marker_float_SET_STAGE(MARKER_STAGE5_LOCKED, floating_marker_np, "MARKER_STAGE3_BAK_LOCK: DONE", caller);

            //}}}
            // CLAMP FLOATING MARKER TO ITS PREVIOUS LOCATION {{{
            if(floating_marker_np != null)
            {
//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT , caller, "MARKER_STAGE3_BAK_LOCK: CLAMP MARKER TO ITS PREVIOUS LOCATION");

                int toX = marker_get_wv_mark_x(gesture_down_wv);
                int toY = (int)floating_marker_np.button.get_saved_y();
                mClamp_np_drop_toXY(floating_marker_np, toX, toY); // ->[onClamped_3_FLOAT_MARKER]->[marker_float_onStageChanged]
            }
            // }}}
        }
    }
    //}}}
    // marker_float4_NEW_LOCK {{{
    private void marker_float4_NEW_LOCK(String caller)
    {
        // SET MARKER_STAGE4_NEW_LOCK .. (SWING FLOATING MARKER TO SAVED SCALE) {{{
        caller += "->marker_float4_NEW_LOCK";

        if((floating_marker_np != null) && !marker_at_saved_scale( floating_marker_np ))
        {
            marker_float_SET_STAGE(MARKER_STAGE4_NEW_LOCK, floating_marker_np, "SWING FLOATING MARKER TO SAVED SCALE ["+floating_marker_np+"]", caller);

            swing_np_2_to_saved_scale(); // ->[swing_3_reached]->[marker_float_onStageChanged]
        }
        //}}}
        else {
            // SET MARKER_STAGE5_LOCKED {{{
            marker_float_SET_STAGE(MARKER_STAGE5_LOCKED, floating_marker_np, "MARKER_STAGE4_NEW_LOCK: DONE", caller);

            // }}}
            // UPDATE COOKIE {{{
            if(floating_marker_np != null)
            {
//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT , ""    , "MARKER_STAGE4_NEW_LOCK: UPDATE COOKIE");

                marker_float_SAVELOCK();
            }
            //}}}
            // CLAMP MARKER ON SCROLLBAR THUMB OFFSET {{{
            if(floating_marker_np != null)
            {
//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT , ""    , "MARKER_STAGE4_NEW_LOCK: CLAMP MARKER ON SCROLLBAR");

                int toX = marker_get_wv_mark_x(gesture_down_wv);
                marker_float_clamp_toX_sbY(toX); // ->[onClamped_3_FLOAT_MARKER]->[marker_float_onStageChanged]
            }
            //}}}
        }
    }
    //}}}
    // marker_float5_LOCKED {{{
    private void marker_float5_LOCKED(String caller)
    {
        // CLEAR LOCKED FLOATING MARKER {{{
        caller += "->marker_float5_LOCKED";
        if(floating_marker_np != null)
        {
//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT , caller, "CLEAR FLOATING MARKER ["+floating_marker_np+"]");

            floating_marker_np = null;
        }
        //}}}
        // SET MARKER_STAGE0_STANDBY {{{
        marker_float_SET_STAGE(MARKER_STAGE0_STANDBY, floating_marker_np, "MARKER_STAGE5_LOCKED: DONE", caller);

        marker_float_onStageChanged(caller);
        //}}}
    }
    //}}}

    // is_floating_marker {{{
    private static boolean is_floating_marker()
    {
        boolean result
            =  (        floating_marker_np    != null)
          //&& (       (floating_marker_stage == MARKER_STAGE1_UNLOCKING)
          //        || (floating_marker_stage == MARKER_STAGE2_UNLOCKED ))
            ;

//MK5_FLOAT//if(result) Settings.MOM(TAG_MK5_FLOAT, "is_floating_marker ...return "+result+": floating_marker_np=["+floating_marker_np+"]");
        return result;
    }
    //}}}
    // is_floating_marker {{{
    private static boolean is_floating_marker(NotePane np)
    {
        boolean result
            =  (        floating_marker_np    != null)
            && (        floating_marker_np    == np  )
          //&& (       (floating_marker_stage == MARKER_STAGE1_UNLOCKING)
          //        || (floating_marker_stage == MARKER_STAGE2_UNLOCKED ))
               ;

//MK5_FLOAT//if(result) Settings.MOM(TAG_MK5_FLOAT, "is_floating_marker("+np+") ...return "+result+": floating_marker_np=["+floating_marker_np+"]");
        return result;
    }
    //}}}
    //}}}
    // FLOAT LOCATION {{{
    // marker_float_DRAGLOCK {{{
    private String marker_float_DRAGLOCK(NotePane marker_np, boolean back_lock, String caller)
    {
//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT, caller+"->marker_float_DRAGLOCK");
        caller = "marker_float_DRAGLOCK(back_lock="+back_lock+")"; // (truncate callers chain)

        String consumed_by = null;

        if( !is_floating_marker( marker_np ) )
        {
            /*.............*/ marker_float1_UNLOCKING(marker_np, caller); consumed_by = "marker_float1_UNLOCKING";
        }
        else {
            if( back_lock ) { marker_float3_BAK_LOCK (           caller); consumed_by = "marker_float3_BAK_LOCK" ; }
            else            { marker_float4_NEW_LOCK (           caller); consumed_by = "marker_float4_NEW_LOCK" ; }
        }

        return consumed_by;
    }
    //}}}
    // marker_float_drag {{{
    private void marker_float_drag(int thumb_y)
    {
        if(floating_marker_np == null) return;

        String caller = "marker_float_drag";

        int b_h = floating_marker_np.button.getHeight();
        int b_y = thumb_y - b_h;

        int top_scaled = thumb_y - (int)(b_h * Settings.MARK_SCALE_GROW);
        if(top_scaled < 0)
            b_y -= top_scaled;

        int m_y  = (int)floating_marker_np.button.getY();
        if( m_y != b_y) {
//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT, caller, "......FLOATING MARKER SB-DRAG ["+floating_marker_np+"] MOVE (m_y -> b_y): ("+m_y+" -> "+b_y+") ...");
            floating_marker_np.button.setY( b_y );
        }
    }
    //}}}
    // marker_float_getY_on_sb {{{
    private int marker_float_getY_on_sb(NotePane marker_np)
    {
        NpButton sbX
            = (gesture_down_sb != null)
            ?  gesture_down_sb
            :  get_sb_for_wv_or_sb(gesture_down_wv);

        if(sbX == null)
            return (int)marker_np.button.getY();

        int    thumb_y = (int)sbX.getY();
        int        b_h = marker_np.button.getHeight();
        int        b_y = thumb_y - b_h;

        int top_scaled = thumb_y - (int)(b_h * Settings.MARK_SCALE_GROW);
        if( top_scaled < 0)
            b_y -= top_scaled;

        return b_y;
    }
    //}}}
    // marker_float_clamp_toX_sbY {{{
    private void marker_float_clamp_toX_sbY(int toX)
    {
        // (sbX) {{{
        if(floating_marker_np == null) return;
        if(gesture_down_wv    == null) return;

        String caller = "marker_float_clamp_toX_sbY("+toX+")";
//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT, caller);

        NpButton           sbX = get_sb_for_wv_or_sb( gesture_down_wv );
        if(sbX == null) return;

        int                m_h = (int)floating_marker_np.button.get_saved_h();
        int                toY = (int)sbX.getY() - m_h;

        //}}}
        // [mClamp_np_drop_toXY] {{{
        mClamp_np_drop_toXY(floating_marker_np, toX, toY);

        //}}}
    }
    //}}}
    // marker_float_get_unlocked_x {{{
    private float marker_float_get_unlocked_x() { return marker_float_get_unlocked_x(null); }
    private float marker_float_get_unlocked_x(RTabs.MWebView wv)
    {
        if(wv == null)     wv = gesture_down_wv;
        if(wv != null) return wv.getX() + wv.getWidth() / 2;
        else           return Settings.DISPLAY_W / 2;
    }
    //}}}
    // marker_at_saved_scale {{{
    private boolean marker_at_saved_scale(NotePane marker_np)
    {
        return (marker_np                    != null)
            && (marker_np.button             != null)
            && (marker_np.button.getScaleY() ==   1f);
    }
    //}}}
    //}}}
    // FLOAT MARKER COOKIE {{{
    // marker_float_SAVELOCK {{{
    private void marker_float_SAVELOCK()
    {
        // (floating_marker_np) {{{
        String caller = "marker_float_SAVELOCK";
//*MK5_FLOAT*/Settings.MOC(TAG_MK5_FLOAT, caller, ".....floating_marker_np=["+floating_marker_np+"]");

        if(floating_marker_np == null                    ) return;

        //}}}
        // UPDATE MARKER COOKIE .. f(floating_marker_np) {{{

        float   thumb_p = marker_add_cookie_mark(gesture_down_wv, floating_marker_np);
        boolean at_left = !is_sb_at_left();

        marker_pin_mark_at_thumb_p(gesture_down_wv, floating_marker_np, at_left, thumb_p); // FIXME: (does not retain new y in marker_magnetize)

        marker_load_np_from_cookie( floating_marker_np );

        //}}}
    }
    //}}}
    //}}}
    //}}}
    /** MARKERS (Magnet) {{{ */
    //* mmMagnetListener */
    //{{{
    private       Settings.MagnetListener mmMagnetListener = new Settings.MagnetListener()
    {
        public  void    magnet_1_progress(float progress) { if( magnet_freezed() ) return;              mm_1_progress( progress ); }
        public  boolean magnet_2_tracked               () { if( magnet_freezed() ) return false; return mm_2_tracked ();           } // called by Magnet.onAnimationEnd
        public  boolean magnet_3_reached               () { if( magnet_freezed() ) return false; return mm_3_reached ();           }
        public  boolean magnet_4_checked               () { if( magnet_freezed() ) return false; return mm_4_checked ();           }
        public  boolean magnet_freezed                 () {                        return waiting_for_ACTION_POINTER_UP;           }
    };
    //}}}
    //* MM_1_progress */
    // mm_1_progress{{{
    private void mm_1_progress(float progress)
    {
        // MOVE CURRENT SET OF MAGNETIZED MARKERS .. (does not change during animation period) {{{
//*MK7_MAGNET*/String caller = String.format("mm_1_progress(%1.2f)", progress);//TAG_MK7_MAGNET
//*MK7_MAGNET*/Settings.MOM(TAG_MK7_MAGNET, caller);

        for(int i=0;  i < marker_magnetized_np_count; ++i)
        {
            NpButton nn = marker_magnetized_np_array[i].button;

            float x_from = nn.get_x_from();                       // anim    start [x]
            float y_from = nn.get_y_from();                       // anim    start [y]
            float s_from = nn.get_s_from();                       // anim    start [scale]

            float x_to   = nn.get_x_to  ();                       // anim      end [x]
            float y_to   = nn.get_y_to  ();                       // anim      end [y]
            float s_to   = nn.get_s_to  ();                       // anim      end [scale]

            float x      = (x_from + (x_to - x_from) * progress); // anim     step [x]
            float y      = (y_from + (y_to - y_from) * progress); // anim     step [y]
            float s      = (s_from + (s_to - s_from) * progress); // anim     step [scale]

            nn.setX     ( x );                                    // anim progress [x]
            nn.setY     ( y );                                    // anim progress [y]
            nn.setScaleX( s ); nn.setScaleY( s );                 // anim progress [scale]
        }
        //}}}
    }
    //}}}
    //* MM_2_tracked */
    // mm_2_tracked {{{
    private boolean mm_2_tracked()
    {
        // TRACK DONE .. f(!hotspot_changed) {{{
        String caller = "mm_2_tracked";
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller);

        boolean hotspot_changed = ((mm_hotspot_f_x > 0) || (mm_hotspot_f_y > 0));
//*MK7_MAGNET*/Settings.MOM(TAG_MK7_MAGNET, "...hotspot_changed=["+hotspot_changed+"] ("+mm_hotspot_f_x+" "+mm_hotspot_f_y+")");

        if( !hotspot_changed )
        {
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, "TRACK ANIM DONE .. (NOT hotspot_changed)");

            return true;
        }
        //}}}
        // NO: MAGNETIZE POST {{{
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, "TRACK ANIM POST .. (hotspot_changed)");

        // MAGNET CYCLE STEP VISUAL FEEDBACK
        int up_unknown_or_down = 0;
        if( may_resume_magnetize(caller) )
            up_unknown_or_down = (mm_hotspot_f_y < fs_search.getY())
                ?          -1  // UP
                :          +1; // DOWN
        mm_2_visualize_track( up_unknown_or_down );

        // POST A STEP HANDLER
        mRTabs.handler.re_postDelayed(hr_mm_2_track, MARKER_MAGNETIZE_TRACK_DELAY);

        return false;
        //}}}
    }
    //}}}
    // hr_mm_2_track {{{
    private Runnable hr_mm_2_track = new Runnable() {
        @Override public void run()
        {
            // (mm_hotspot_f_x) [mm_hotspot_f_y] {{{
            if((mm_hotspot_f_x <= 0) && (mm_hotspot_f_y <= 0)) return;

            //}}}
            // (magnet_freezed) {{{
            String caller = "hr_mm_2_track";

            if( mmMagnetListener.magnet_freezed() )
            {
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, "TRACK ANIM STOP .. (magnet_freezed)");
                return;
            }
            //}}}
            // (marker_magnetized_np_count) {{{
            if(marker_magnetized_np_count < 1)
            {
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, "TRACK ANIM STOP .. (NOT marker_magnetized_np_count)");
                return;
            }
            //}}}
            marker_magnetize(mm_hotspot_f_x, mm_hotspot_f_y, caller);
        }
    };
    //}}}
    //* MM_3_reached */
    // mm_3_reached {{{
    private boolean mm_3_reached()
    {
        if( property_get(WVTools.WV_TOOL_FLAG_5) ) return true;
        // REACH DONE .. f(hotspot_reached) {{{
        String caller ="mm_3_reached";
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller);

// NOTE (it looks like conditions are always met at this point)
// ...
// as if all [x y scale] are as they should be
// when mm_2_tracked returns true

        boolean hotspot_reached = true;
        for(int i=0;  i < marker_magnetized_np_count; ++i)
        {
            NpButton nn = marker_magnetized_np_array[i].button;

            float  x_to = nn.get_x_to ();
            float  y_to = nn.get_y_to ();
            float  s_to = nn.get_s_to ();

            float  x    = nn.getX     ();
            float  y    = nn.getY     ();
            float  s    = nn.getScaleY();

            int   dx    = (int)    (x_to - x) ;
            int   dy    = (int)    (y_to - y) ;
            int   ds    = (int)(10*(s_to - s));

            if((dx!=0) || (dy!=0) || (ds!=0))
                hotspot_reached = false;
        }

        if( hotspot_reached )
        {
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, "REACH ANIM DONE .. (hotspot_reached)");

            if( mm_check_pending ) mm_check_exec(caller);

//        // [VISUALIZE MAGNETIZE IN MARGIN STOP-CONDITION] {{{
//        if( is_fs_search_in_margins() )
//        {
///*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, "", "VISUALIZE MAGNETIZE IN MARGIN STOP-CONDITION");
//
//            visualize_MARGIN_rect(gesture_down_wv, is_sb_at_left() ? MARGIN_R : MARGIN_L);
//        }
//        //}}}

            return true;
        }

        //}}}
        // NO: ANIM RESTART {{{
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, "REACH ANIM POST .. (NOT hotspot_reached)");

        // MAGNET CYCLE STEP VISUAL FEEDBACK
        if(mm_drag_Magnet == null) mm_drag_Magnet = new Magnet( mmMagnetListener );

        mm_drag_Magnet.animate( MARKER_MAGNETIZE_ANIM_DURATION );

        mm_visualize_3_reach();
        return false;
        //}}}
    }
    //}}}
    //* MM_4_checked */
    //{{{
    //{{{
    private static   boolean mm_check_pending                 = false;
    private static   boolean mm_check_done                    =  true;
    //}}}
    // mm_4_checked {{{
    // mm_4_checked {{{
    private boolean mm_4_checked()
    {
        if( mm_check_done ) return true;

        mm_recheck(false, "mm_4_checked"); // (will either call marker_magnetize) or (set mm_check_done to true)

        return false;
    }
    //}}}
    // mm_resume {{{
    private void mm_resume(String caller)
    {
//*MK7_MAGNET*/caller += "->mm_resume";//TAG_MK7_MAGNET
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller);

        // look for running [mm_check_exec] callers
        boolean can_be_queued
            =            mClamp_is_looping  ()
            ||     swing_Magnet_is_animating()
            ||   mm_drag_Magnet_is_animating()
            ;
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, ".1............mClamp_is_looping  ()=["+         mClamp_is_looping  () +"]");
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, ".2......swing_Magnet_is_animating()=["+   swing_Magnet_is_animating() +"]");
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, ".2....mm_drag_Magnet_is_animating()=["+ mm_drag_Magnet_is_animating() +"]");
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, ".=>...................can_be_queued=["+ can_be_queued                 +"]");

        mm_recheck(can_be_queued, caller);
    }
    //}}}
    // mm_recheck {{{
    private void mm_recheck(                       String caller) { mm_recheck(false, caller); }
    private void mm_recheck(boolean can_be_queued, String caller)
    {
//*MK7_MAGNET*/caller += "->mm_recheck(can_be_queued="+can_be_queued+")";//TAG_MK7_MAGNET
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller);

        mm_check_done = false;

        if     ( can_be_queued     )                            mm_check_pending =  true; // expect a call to [   mm_check_exec]
        else if( !mm_check_pending ) mRTabs.handler.re_post( hr_mm_check               ); // instant  call to [hr_mm_check     ]

//*MK7_MAGNET*/Settings.MOM(TAG_MK7_MAGNET, "...mm_check_pending=["+mm_check_pending+"]");

        mm_visualize_4_check();
    }
    //}}}
    // mm_check_exec {{{
    private void mm_check_exec(String caller)
    {
//*MK7_MAGNET*/caller += "->mm_check_exec";//TAG_MK7_MAGNET
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, "CALLING re_post(hr_mm_check)");
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, "...marker_magnetized_np_count=["+marker_magnetized_np_count+"]");

        mRTabs.handler.re_post( hr_mm_check );
    }
    //}}}
    // hr_mm_check {{{
    private Runnable hr_mm_check = new Runnable()
    {
        @Override public void run()
        {
            String caller = "hr_mm_check";
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, "...mm_check_done=["+mm_check_done+"]");

            if( mmMagnetListener.magnet_freezed() ) return;
            if( mm_check_done                     ) return;

            property_set(WV_TOOL_FLAG_WEIGHT, true); // (force spreading overlapping markers option)

            int f_x = (int)fs_search.getX() + fs_search_drag_offset_x;
            int f_y = (int)fs_search.getY() + fs_search_drag_offset_y;

            marker_magnetize(f_x, f_y, caller);

            mm_check_pending = false;
            mm_check_done    =  true;

            mm_visualize_5_done();
        }
    };
    //}}}
    //}}}
    //}}}
    //* MM_Events */
    //{{{
    // VISUALIZE (track reach check done) {{{
    private void mm_2_visualize_track(int up_unknown_or_down)
    {
        fs_search_set_colors(Color.BLACK , Color.YELLOW);
        fs_search_np_setText( (up_unknown_or_down < 0) ?        Settings.SYMBOL_DOWN_BLACK_TRIANGLE
                :             (up_unknown_or_down > 0) ?        Settings.SYMBOL_UP_BLACK_TRIANGLE
                :           /* up or down not specified */      Settings.SYMBOL_ARROW_SINGLE_UPDOWN
                );
    }
    private void mm_visualize_3_reach() { fs_search_np_setText( Settings.SYMBOL_FLAG_CHEQUERED     ); fs_search_set_colors(Color.RED  , Color.BLACK); }
    private void mm_visualize_4_check() { fs_search_np_setText( Settings.SYMBOL_DOWN_UP_TRIANGLE   ); fs_search_set_colors(Color.GREEN, Color.BLACK); }
    private void mm_visualize_5_done () { fs_search_np_setText( Settings.SYMBOL_PLAY_PAUSE         ); fs_search_set_colors(Color.WHITE, Color.BLACK); }
    //}}}
    // marker_magnetized_sync {{{
    private void marker_magnetized_sync(String caller)
    {
        // [!may_resume_magnetize] .. (return) {{{
        if( !may_resume_magnetize(caller) )  return;

//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller+"->marker_magnetized_sync");
        //}}}
        // [mm_drag_Magnet.animate] {{{

        mm_drag_Magnet.animate( MARKER_MAGNETIZE_ANIM_DURATION );
        //}}}
    }
    //}}}
    // marker_sync_sb_side {{{
    private void marker_sync_sb_side(String caller)
    {
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller+"->marker_sync_sb_side");
        mRTabs.handler.re_post( hr_marker_sync_sb_side );
    }
    private Runnable hr_marker_sync_sb_side = new Runnable() {
        @Override public void run() { do_marker_sync_sb_side(); }
    };
    //}}}
    // do_marker_sync_sb_side {{{
    private void do_marker_sync_sb_side()
    {
        // f(markers not locked) {{{
        if(gesture_down_wv          == null) return;
        if( property_get_WV_TOOL_MARK_LOCK ) return;

        String caller = "do_marker_sync_sb_side()";
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller);

        //}}}
        // [magnetizing] [f_x] [f_y] {{{
        boolean can_magnetize
          = may_resume_magnetize(caller);
//*MK7_MAGNET*/Settings.MOM(TAG_MK7_MAGNET, "can_magnetize=["+can_magnetize+"]");

        // move fs_search to the other wv half {{{
        int f_x = 0;
        int f_y = 0;
        if( can_magnetize )
        {
            f_x                 = (int)fs_search.getX();
            f_y                 = (int)fs_search.getY();
            int wv_x_mid        = (int)(gesture_down_wv.getX() + gesture_down_wv.getWidth() / 2);
            int wv_x_mid_to_f_x = f_x - wv_x_mid;
            f_x                 = wv_x_mid - wv_x_mid_to_f_x;
            fs_search.setX( f_x );
        }
        //}}}
        //}}}
        // [shape] [margin] .. f(at_left) {{{

        boolean at_left = !is_sb_at_left();

        String  shape = get_marker_shape_for_current_display_orientation( at_left );

        float       x = at_left
            ? gesture_down_wv.getX() - Settings.TAB_MARK_H/2
            : gesture_down_wv.getX() + Settings.TAB_MARK_H/2 + gesture_down_wv.getWidth() * gesture_down_wv.getScaleX() - Settings.TOOL_BADGE_SIZE;

        NotePane       np = null;
        String wv_key_pfx = WV_TOOL_mark +"."+ marker_get_wv_slice( gesture_down_wv );
//*MK7_MAGNET*/Settings.MOM(TAG_MK7_MAGNET, "wv_key_pfx=["+wv_key_pfx+"]");
        for(Map.Entry<String, Object> entry : MARK_Map.entrySet())
        {
            // SELECT slice pinned markers {{{
            np = marker_get_pinned_entry(wv_key_pfx, entry);
            if(np == null              ) continue;
          //if(np == floating_marker_np) continue;

            //}}}
            //if(np.button.getVisibility() != View.VISIBLE  ) continue;
            // [set_shape] [set_saved_x] [setPivotX] .. f(at_left) {{{
            np.button.set_shape  ( shape ,true                          ); // with_outline
            np.button.setPivotX  ( at_left ? 0 : np.button.get_saved_w());
            np.button.set_saved_x( x                                    );
            //}}}
        }
        //}}}
        // (can_magnetize) -> [marker_magnetize] {{{
        if( can_magnetize )
        {
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, "", "...MARKERS MAGNETIZE");

            marker_magnetize(f_x, f_y, caller);
        }
        //}}}
        // (NOT can_magnetize) -> [marker_wv_sync_no_delay] {{{
        else {
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, "", "...MARKERS SYNC");
            marker_wv_sync_no_delay(caller);
        }
        //}}}
    }
    //}}}
    // marker_magnetize_stop{{{
    private void marker_magnetize_stop(RTabs.MWebView wv, String caller)
    {
        caller += "->marker_magnetize_stop";
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller);

//*MK7_MAGNET*/if(floating_marker_np != null) Settings.MOC(TAG_MK7_MAGNET , caller, "CLEAR FLOATING MARKER ["+floating_marker_np+"]");
        floating_marker_np = null;

        marker_magnetize_pause(wv, caller);

        marker_magnetize_invalidate_last_checked(caller);
    }
    //}}}
    // marker_magnetize_pause {{{
    private void marker_magnetize_pause(RTabs.MWebView wv, String caller)
    {
        caller += "->marker_magnetize_pause";
//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller, "("+marker_magnetized_np_count+" MARKERS)");

        mClamp_standby(caller);

        fs_search_park(caller);

        if(marker_magnetized_np_count > 0)
            marker_magnetize_park(wv, "marker_magnetize_init");

        mm_check_pending               = false;
        mm_check_done                  =  true;

        marker_magnetized_START_DONE   = false;
    }
    //}}}
    // marker_magnetized_onPropertyChanged {{{
    private void marker_magnetized_onPropertyChanged(String caller)
    {
        if( !marker_magnetized_START_DONE )  return; // (do not pop markers up when paused)
      //if( !may_resume_magnetize(caller) )  return;

        marker_magnetize_park(gesture_down_wv, "marker_magnetized_onPropertyChanged");

//*MK7_MAGNET*/Settings.MOC(TAG_MK7_MAGNET, caller+"->marker_magnetized_onPropertyChanged");
        marker_magnetize_last_checked();
    }
    //}}}
    // is_fs_search_in_margins {{{
    private boolean is_fs_search_in_margins()
    {
        if(gesture_down_wv == null) return false;

        float                   wv_w = gesture_down_wv.getWidth();
        float                   wv_x = gesture_down_wv.getX    ();
        boolean result
            =    is_sb_at_left()
            ?     (fs_search.getX() >= (wv_x + wv_w - Settings.TOOL_BADGE_SIZE))    // within [BADGE-SIZE-WIDTH] [RIGHT-MARGIN]
            :     (fs_search.getX() <= (wv_x        + Settings.TOOL_BADGE_SIZE));   // within [BADGE-SIZE-WIDTH] [ LEFT-MARGIN]

//*MK7_MAGNET*/Settings.MON(TAG_MK7_MAGNET, "is_fs_search_in_margins", "...return ["+result+"]");
        return result;
    }
    //}}}
    // is_a_magnetized_marker_view {{{
    private boolean is_a_magnetized_marker_view(View view)
    {
        if(marker_magnetized_np_count < 1) return false;

        if( !is_a_marker( view )      ) return false;

        for(int i=0; i < marker_magnetized_np_count; ++i)
            if(view == NotePane.Get_view( marker_magnetized_np_array[i] )) return true;

        return false;
    }
    //}}}
    //}}}
    //}}}
    //}}}
    //}}}
    /** EVENTS  */
    // {{{
    //{{{
    private static final float     FLING_VELOCITY_SLOWING_FX     = 20;
    private static final long      SHOW_TOUCHED_MARKER_INTERVAL  = 50; // ms
    private              long      last_show_touched_marker_time =  0;

  //private              Clamp     sClamp;

    //}}}
    //* EV0_dispatch */
    // wants_to_steal_events {{{
    //{{{
    private boolean opted_to_steal_events = false;
    //}}}
    private void reconsider_stealing_events()
    {
        opted_to_steal_events = false;
        mRTabs.dispatchTouchEvent_invalidate();
    }
    public  boolean wants_to_steal_events(MotionEvent event)
    {
        //{{{
        String  caller = "wvTools.wants_to_steal_events";

        int action  = event.getActionMasked();
        if(( action == MotionEvent.ACTION_DOWN) || ( action == MotionEvent.ACTION_POINTER_DOWN))
            opted_to_steal_events = false; // would be reset by calls from mRTabs.dispatchTouchEvent

        //}}}
        // (!opted_to_steal_events) {{{
        boolean did_opted_to_steal_events = opted_to_steal_events;
        if( !opted_to_steal_events )
        {
            // === [consumed_by_1_WEBVIEW_SWAPING] {{{
            boolean consumed_by_1_WEBVIEW_SWAPING
                =  (mRTabs.fs_search_can_select_webview_on_ACTION_UP  () && (action == MotionEvent.ACTION_MOVE))
                // (mRTabs.fs_search_can_select_webview_on_ACTION_MOVE() && (action == MotionEvent.ACTION_MOVE))
                ;

            //}}}
            // === [consumed_by_2_SCROLLBAR] {{{
            boolean consumed_by_2_SCROLLBAR
                =  (gesture_down_sb != null)            // .. (SCROLLBAR touched at onDown)
                || (scroll_wv_AnimatorSet != null)      // .. (SCROLLBAR animation running)
                ||  sb_has_paged_up_or_down             // .. (PAGING since onDown)
                ||  sb_has_called_findNext              // .. (FNDING since onDown)
                ;

            //}}}
            // === [consumed_by_3_MARGIN] {{{
            boolean consumed_by_3_MARGIN
                =   sb_clicked_or_margin_clicked_on_sb_side(caller)           // (from margin)
                &&  (gesture_down_marker_np_to_CB == null                   ) // but (not an a marker)
                &&  (gesture_down_sb              == null                   ) // and (not an a scrollbar)
                //  (action                       == MotionEvent.ACTION_MOVE) // ACTION_MOVE
                ;

            //}}}
            // === [consumed_by_4_MARKER] {{{
            boolean consumed_by_4_MARKER
                =  (gesture_down_marker_np_to_CB != null)
                ||  marker_has_expanded_since_onDown        // .. (not having to support magnification on fingers spread)
                ;

            //}}}
            // === [consumed_by_5_MARKER_MAGNETIZE] {{{
            boolean consumed_by_5_MARKER_MAGNETIZE
                =  !property_get_WV_TOOL_MARK_LOCK
                &&  marker_magnetized_START_DONE                            // .. (while magnetizing)
                && (gesture_down_wv              != null  )                 // .. (WEBVIEW selected at onDown)
                && (marker_pin_count_onDown      >  0     )                 // .. (have some markers)
                && (may_resume_magnetize(caller) || (onDown_URDL != null))  // .. (magnetizing paused)
                ;

            //}}}
            // = [opted_to_steal_events] {{{
            if(    !mRTabs.handling_gesture_down_SomeView_atXY() && // .. (mRTabs has no specific view to handle)
                    (      consumed_by_1_WEBVIEW_SWAPING
                        || consumed_by_2_SCROLLBAR
                        || consumed_by_3_MARGIN
                        || consumed_by_4_MARKER
                        || consumed_by_5_MARKER_MAGNETIZE
                    )
              ) {
                opted_to_steal_events = true;
              }
            //}}}
// [TRACE] {{{
//*EV0_WV_DP*/if(opted_to_steal_events) {//TAG_EV0_WV_DP
//*EV0_WV_DP*/ Settings.MOC(TAG_EV0_WV_DP, caller, "STEALING EVENTS FROM WEBVIEWS: "+ Settings.Get_action_name(event));

//*EV0_WV_DP*/ Settings.MOC(TAG_EV0_WV_DP, "", ".==consumed_by_1_WEBVIEW_SWAPING...............=["+ consumed_by_1_WEBVIEW_SWAPING                        +"]");
//*EV0_WV_DP*/ Settings.MOC(TAG_EV0_WV_DP, "", ".==consumed_by_2_SCROLLBAR.....................=["+ consumed_by_2_SCROLLBAR                              +"]");
//*EV0_WV_DP*/ Settings.MOC(TAG_EV0_WV_DP, "", ".==consumed_by_3_MARGIN........................=["+ consumed_by_3_MARGIN                                 +"]");
//*EV0_WV_DP*/ Settings.MOC(TAG_EV0_WV_DP, "", ".==consumed_by_4_MARKER........................=["+ consumed_by_4_MARKER                                 +"]");
//*EV0_WV_DP*/ Settings.MOC(TAG_EV0_WV_DP, "", ".==consumed_by_5_MARKER_MAGNETIZE..............=["+ consumed_by_5_MARKER_MAGNETIZE                       +"]");
//*EV0_WV_DP*/ Settings.MOM(TAG_EV0_WV_DP,     "...(scroll_wv_AnimatorSet != null).............=["+ (scroll_wv_AnimatorSet != null)                      +"]");
//*EV0_WV_DP*/ Settings.MOM(TAG_EV0_WV_DP,     "...sb_has_paged_up_or_down.....................=["+ sb_has_paged_up_or_down                              +"]");
//*EV0_WV_DP*/ Settings.MOM(TAG_EV0_WV_DP,     "...sb_has_called_findNext......................=["+ sb_has_called_findNext                               +"]");
//*EV0_WV_DP*/ Settings.MOM(TAG_EV0_WV_DP,     "...fs_search_can_select_webview_on_ACTION_MOVE.=["+ mRTabs.fs_search_can_select_webview_on_ACTION_MOVE() +"]");
//*EV0_WV_DP*/ Settings.MOM(TAG_EV0_WV_DP,     "...fs_search_can_select_webview_on_ACTION_UP...=["+ mRTabs.fs_search_can_select_webview_on_ACTION_UP()   +"]");
//*EV0_WV_DP*/ Settings.MOM(TAG_EV0_WV_DP,     "...gesture_down_marker_np_to_CB................=["+ gesture_down_marker_np_to_CB                         +"]");
//*EV0_WV_DP*/ Settings.MOM(TAG_EV0_WV_DP,     "...gesture_down_sb.............................=["+ gesture_down_sb                                      +"]");
//*EV0_WV_DP*/ Settings.MOM(TAG_EV0_WV_DP,     "...gesture_down_wv.............................=["+ gesture_down_wv                                      +"]");
//*EV0_WV_DP*/ Settings.MOM(TAG_EV0_WV_DP,     "...marker_has_expanded_since_onDown............=["+ marker_has_expanded_since_onDown                     +"]");
//*EV0_WV_DP*/ Settings.MOM(TAG_EV0_WV_DP,     "...marker_has_spread_since_onDown..............=["+ marker_has_spread_since_onDown                       +"]");
//*EV0_WV_DP*/ Settings.MOM(TAG_EV0_WV_DP,     "...marker_magnetized_np_count..................=["+ marker_magnetized_np_count                           +"]");
//*EV0_WV_DP*/ Settings.MOM(TAG_EV0_WV_DP,     "...waiting_for_ACTION_POINTER_UP...............=["+ waiting_for_ACTION_POINTER_UP                        +"]");

//*EV0_WV_DP*/}//TAG_EV0_WV_DP
//}}}
        }
        //}}}
        // [return opted_to_steal_events] {{{
//*EV0_WV_DP*/if(did_opted_to_steal_events != opted_to_steal_events) Settings.MON(TAG_EV0_WV_DP, caller, "........return [OPTED_TO_STEAL_EVENTS]=["+ opted_to_steal_events +"]");
        return opted_to_steal_events;
        //}}}
    }
    //}}}
    //* EV1_onDown */
    //{{{
    //{{{
    private              float     onDown_x;
    private              float     onDown_y;

    private              String    onDown_URDL                            = null; // IN MARGIN .. (Up Right Down Left)

    public static  RTabs.MWebView  gesture_down_wv                        = null;
    public static        NpButton  gesture_down_sb                        = null;
    private              float     gesture_down_sbX_to_finger_y           =    0;

  //private              boolean   sbX_at_left_onDown                     = false;

    private static       NotePane  gesture_down_marker_np_to_CB           = null;

    private                    int marker_pin_count_onDown                = 0;
    //}}}
    //? onDown {{{
    public  boolean onDown(MotionEvent event)
    {
        //{{{
        String caller = "wvTools.onDown";
//*EV1_WV_IN*/Settings.MOC(TAG_EV1_WV_IN, caller, mRTabs.get_onDown_view_names());

        int x = (int)event.getRawX();
        int y = (int)event.getRawY();

        String consumed_by = null;
        //}}}

        /**/        onDown_1_init                 ( event );
        ////        onMove_2_WEBVIEW_SWAP_ON_DOWN (       );
        if(         onDown_3_WEBVIEW_SELECT       ( x, y  ))
        {
            if(     onDown_4_TOOL                 ( x, y  )) consumed_by = "onDown_4_TOOL";
            else if(onDown_5_SCROLLBAR            ( x, y  )) consumed_by = "onDown_5_SCROLLBAR";
            else if(onDown_6_MARKER               ( x, y  )) consumed_by = "onDown_6_MARKER";
            else if(onDown_7_MARGIN               ( x, y  )) consumed_by = "onDown_7_MARGIN";
        }

        // [consumed_by] .. f(MARKER) .. f(WEBVIEW-SWAP) {{{
        if(consumed_by == null)
        {
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "...fs_search_can_select_webview_on_ACTION_UP..=["+ mRTabs.fs_search_can_select_webview_on_ACTION_UP()   +"]");
///EV2_WV_CB//Settings.MON(TAG_EV2_WV_CB, caller, "...fs_search_can_select_webview_on_ACTION_MOVE=["+ mRTabs.fs_search_can_select_webview_on_ACTION_MOVE() +"]");
            if     (gesture_down_marker_np_to_CB != null                ) consumed_by = "gesture_down_marker_np_to_CB";
            else if(mRTabs.fs_search_can_select_webview_on_ACTION_UP  ()) consumed_by = "WEBVIEW-SWAP";
          //else if(mRTabs.fs_search_can_select_webview_on_ACTION_MOVE()) consumed_by = "fs_search_can_select_webview_on_ACTION_MOVE";
        }
        //}}}
        // [return consumed_by] {{{
//*EV1_WV_OK*/Settings.MOC(TAG_EV1_WV_OK, caller, "...return "+(consumed_by != null)+" .. (consumed_by="+consumed_by+")");
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, ""    , "......................onDown_URDL=["+ onDown_URDL                    +"]");
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, ""    , "...............floating_marker_np=["+ floating_marker_np             +"]");
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, ""    , "...........may_resume_magnetize()=["+ may_resume_magnetize(caller)   +"]");
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, ""    , ".......marker_magnetized_np_count=["+ marker_magnetized_np_count     +"]");
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, ""    , "......magnetized_marker_np_onDown=["+ magnetized_marker_np_onDown    +"]");
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, ""    , ".....gesture_down_marker_np_to_CB=["+ gesture_down_marker_np_to_CB   +"]");
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, ""    , ".....marker_magnetized_START_DONE=["+ marker_magnetized_START_DONE   +"]");

        return (consumed_by != null);
        //}}}
    }
    //}}}
    //. onDown_1_init {{{
    private void onDown_1_init(MotionEvent event)
    {
        // 1/3 call CLOSERS first {{{
        String caller = "wvTools.onDown_1_init";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller);

        if( !property_get( WV_TOOL_JS1_SELECT ) )
            sb_show_WAITING();
        sb_onDown                      ();
        wvTools_expanded_marker_onDown ();

        //}}}
        // 2/3 then NULLIFIERS {{{
        wvTools_pointer_onDown         ();

        //}}}
        // 3/3 and finally SETTERS {{{
        int x = (int)event.getRawX();
        int y = (int)event.getRawY();
        wvTools_onDown                 (x,y);
        //}}}
    }
    //}}}
    //. onMove_2_WEBVIEW_SWAP_ON_DOWN {{{
    private void onMove_2_WEBVIEW_SWAP_ON_DOWN()
    {
        // (wv container border) (magnetizing) {{{
        if( !mRTabs.touched_wvContainer_border ) return; // (relevant only when gesture started from webview container frame)
        // should use mRTabs.fs_search_can_swap_webview() instead ?

        String caller = "wvTools.onMove_2_WEBVIEW_SWAP_ON_DOWN";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller+": ...touched_wvContainer_border..=["+ mRTabs.touched_wvContainer_border +"]");

        //}}}
        // MARKER MAGNETIZE: STOP .. (SWAPPING WEBVIEWS) {{{
        if( may_resume_magnetize(caller) )
        {
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller, "MARKER MAGNETIZE: STOP .. (SWAPPING WEBVIEWS)");
            marker_magnetize_stop(gesture_down_wv, caller);
        }
        //}}}
        // TEMPORARY HIDE MARKERS .. f(webview swap start) {{{
        marker_set_someone_has_called_hide_marks(caller);

        //}}}
    }
    //}}}
    //! onDown_3_WEBVIEW_SELECT {{{
    private boolean onDown_3_WEBVIEW_SELECT(int x, int y)
    {
        // (wv_at_XY) {{{
        String caller = "wvTools.onDown_3_WEBVIEW_SELECT";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller);

        RTabs.MWebView wv_at_XY = get_hr_wv_at_XY(x,y);
        //}}}
        // MARKER MAGNETIZE: STOP .. (CHANGING WEBVIEW) {{{
        RTabs.MWebView fs_search_wv  = get_wv_containing_np_button( fs_search );
        if(           (fs_search_wv != null)
                &&    (wv_at_XY     != null)
                &&    (fs_search_wv != wv_at_XY))
        {
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller, "MARKER MAGNETIZE: STOP .. CHANGING FROM fs_search_wv=["+get_view_name(fs_search_wv)+"] TO wv_at_XY=["+get_view_name(wv_at_XY)+"]");
            marker_magnetize_stop(fs_search_wv, caller);
        }
        //}}}
        // [gesture_down_wv] .. (wv_at_XY) {{{
        if(wv_at_XY != null) {
            gesture_down_wv = wv_at_XY; // fallback to keeping previous webview (if any) (when clicked outside)

            // [gesture_down_wv] zoom controls settings .. f(WV_TOOL_ZOOM)
            WebSettings wv_ws    = gesture_down_wv.getSettings();
            boolean     wv_zoom  = property_get( WV_TOOL_zoom );
            if(         wv_zoom != wv_ws.getBuiltInZoomControls()) {
                wv_ws.setSupportZoom        ( wv_zoom );
                wv_ws.setBuiltInZoomControls( wv_zoom );
            }
        }

//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "...gesture_down_wv=["+ get_view_name(gesture_down_wv) +"]");
        //}}}
        // [marker_pin_count_onDown] {{{
        marker_pin_count_onDown            = marker_pin_get_count( gesture_down_wv );

//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, "...marker_pin_count_onDown=["+ marker_pin_count_onDown        +"]");
        //}}}
        return (gesture_down_wv != null);
    }
    //}}}
    //? onDown_4_TOOL {{{
    private boolean onDown_4_TOOL(int x, int y)
    {
        View tool_view = mRTabs.gesture_down_SomeView_atXY;
        if(                         tool_view == null              ) return false;
        if( !(                      tool_view instanceof NpButton) ) return false;
        if( !in_JSNP_Map( (NpButton)tool_view )                    ) return false;
        // [return consumed_by] {{{
        String caller = "wvTools.onDown_4_TOOL";
//*EV1_WV_OK*/Settings.MOC(TAG_EV1_WV_OK, caller, "...return true .. (tool_view="+get_view_name(tool_view)+")");
        return true;
        //}}}
    }
    //}}}
    //? onDown_5_SCROLLBAR {{{
    private boolean onDown_5_SCROLLBAR(int x, int y)
    {
        // [gesture_down_sb] ..f(gesture_down_SomeView_atXY) {{{
        String caller = "wvTools.onDown_5_SCROLLBAR";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller);

//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB,    "...gesture_down_SomeView_atXY=["+get_view_name(mRTabs.gesture_down_SomeView_atXY)+"]");
    //  NpButton sbX = get_sb_for_wv_or_sb( mRTabs.gesture_down_SomeView_atXY );
        NpButton sbX = get_sb_for_view    ( mRTabs.gesture_down_SomeView_atXY );
        if(sbX != null)
        {
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "TOUCHED ["+get_view_name( mRTabs.gesture_down_SomeView_atXY )+"] xy=["+ x +","+ y +"]");
//            // [sbX_at_left_onDown] .. (markers in opposite margin) {{{
//            sbX_at_left_onDown = sbX.at_left;
//
//            //}}}
// {{{
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "................gesture_down_wv=["+gesture_down_wv +"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "........marker_expand_x_min_max=["+marker_expand_x_min+"<->"+marker_expand_x_max+"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "is_clamping_button( fs_search )=["+is_clamping_button( fs_search )+"]");
// }}}
            // DRAGGING SCROLLBAR .. [gesture_down_SomeView_atXY]->[gesture_down_sb] {{{
            // [sbX]<-[tool]
            if(                     !is_clamping_button( fs_search ) // [currently], there is no [    MOVING-TOOL]
                || (mRTabs.gesture_down_SomeView_atXY != fs_search ) // ........... or TOUCHED a [NOT-MOVING-TOOL]
            ) {
                if(is_sb_aroundXY(sbX, x, y) || is_a_tool( mRTabs.gesture_down_SomeView_atXY ))
                {
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "TOUCHED the SCROLLBAR or one of the TOOLS it contains");
                    gesture_down_sb              =     sbX;
                    gesture_down_sbX_to_finger_y = y - sbX.getY();
                    reconsider_stealing_events();
                }
            }
// {{{
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "................gesture_down_sb=["+ get_view_name  ( gesture_down_sb  ) +"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, ".....is_view_showing(sbX)......=["+ is_view_showing(sbX               ) +"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, ".....is_sb_atXY.....(sbX, x, y)=["+ is_sb_atXY     (sbX, x, y         ) +"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, ".....is_sb_aroundXY.(sbX, x, y)=["+ is_sb_aroundXY (sbX, x, y         ) +"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, ".....is_a_tool("+get_view_name(mRTabs.gesture_down_SomeView_atXY)+")=["+ is_a_tool (mRTabs.gesture_down_SomeView_atXY) +"]");
// }}}

            // [take hold of the event on behalf of wvTools] .. (see: RTabs.release_dragBand)
            if(gesture_down_wv != null)
            {
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "...setting [gesture_consumed_by_onDown]");
                mRTabs.gesture_consumed_by_onDown = caller;
            }

            //}}}
        }
        //}}}
        // [sb_sync_LABEL] .. f(gesture_down_sb) {{{
        if(gesture_down_sb != null) sb_sync_LABEL();

        //}}}
        // [return consumed_by] {{{
//*EV1_WV_OK*/if(gesture_down_sb != null) Settings.MOC(TAG_EV1_WV_OK, caller, "...return true .. (gesture_down_sb="+get_view_name(gesture_down_sb)+")");
        return (gesture_down_sb != null);
        //}}}
    }
    //}}}
    // ? onDown_6_MARKER {{{
    private boolean onDown_6_MARKER(int x, int y)
    {
        // MARKER AT XY {{{
        String caller = "wvTools.onDown_6_MARKER";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller);

        gesture_down_marker_np_to_CB = marker_get_gesture_down_marker_np_atXY(x, y);

        //}}}
        // MAGNETIZE MARKER AT XY {{{
        if( is_a_magnetized_marker_view( mRTabs.gesture_down_SomeView_atXY ) )
            magnetized_marker_np_onDown = Settings.get_np_for_button((NpButton)mRTabs.gesture_down_SomeView_atXY);
        else
            magnetized_marker_np_onDown = null;

        //}}}
        // ADJUSTING FINGER OFFSET .. (no marker at xy while magnetizing) {{{
        boolean       magnetizing = ((marker_magnetized_np_count   >  0   ) ||  marker_magnetized_START_DONE        );
        boolean no_marker_touched = ((gesture_down_marker_np_to_CB == null) && (magnetized_marker_np_onDown == null));
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, ".........magnetizing=["+ magnetizing       +"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "...no_marker_touched=["+ no_marker_touched +"]");
        if(magnetizing && no_marker_touched)
        {
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, "", "ADJUSTING FINGER OFFSET .. (no marker at xy while magnetizing)");

            mClamp_np_onDown_fingerXY(fs_search_np, x, y);
marker_magnetized_START_DONE = true; //XXX
        }
        //}}}
        // [return consumed_by] {{{
//*EV1_WV_OK*/if(magnetizing) Settings.MOC(TAG_EV1_WV_OK, caller, "...return true .. (magnetizing)");
        return   magnetizing;
        //}}}
    }
    //}}}
    // onDown_7_MARGIN {{{
    private boolean onDown_7_MARGIN(int x, int y)
    {
        // {{{
        String caller = "wvTools.onDown_7_MARGIN";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller);

        if(gesture_down_wv == null) return false;

        //}}}
        // MARGIN [marker_expand_x_min marker_expand_x_max] {{{
        int            wv_w = (int)(gesture_down_wv.getWidth() * gesture_down_wv.getScaleX());
        int            wv_x = (int)(gesture_down_wv.getX    ());
        boolean     at_left = (x < (wv_x + wv_w/2));

        marker_expand_x_min = (int)(at_left ?  gesture_down_wv.getX() : gesture_down_wv.getX() + wv_w - Settings.TOOL_BADGE_SIZE);
        marker_expand_x_max =       marker_expand_x_min                                               + Settings.TOOL_BADGE_SIZE;

//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, "", "MARGIN=["+marker_expand_x_min+" <=> "+marker_expand_x_max+"]");
        //}}}
        // MARGIN [onDown_URDL] {{{

        onDown_URDL = get_onDown_URDL(x,y,gesture_down_wv);
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, "", "MARGIN=["+onDown_URDL+"]");

        //}}}
        // WRAP MARKER {{{
        boolean       magnetizing = ((marker_magnetized_np_count   >  0   ) ||  marker_magnetized_START_DONE        );
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, ".........magnetizing=["+ magnetizing       +"]");
        if(        !magnetizing
                && (               onDown_URDL == null)
                && (        floating_marker_np == null)
          )
        {
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, "", "WRAP MARKERS");

            marker_set_wrap_state( true );
        }
        else if( marker_wrap_state )
        {
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, "", "UNWRAP MARKERS");

            marker_set_wrap_state(false);
        }
        //}}}
        // MARGIN WITH NO MARKERS TO WORK WITH {{{
        if(onDown_URDL != null)
        {
            if((marker_pin_count_onDown < 1) || property_get_WV_TOOL_MARK_LOCK)
            {
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, "", "TOUCHED MARGIN WITH NO MARKERS TO WORK WITH");

                visualize_MARGIN_rect(gesture_down_wv, onDown_URDL);
            }
        }
        //}}}
        // [return consumed_by] {{{
//*EV1_WV_OK*/if(magnetizing) Settings.MOC(TAG_EV1_WV_OK, caller, "...return true .. (magnetizing)");
        return   magnetizing;
        //}}}
    }
    //}}}

    // is_onDown_URDL {{{
    public  boolean is_onDown_URDL()
    {
//EV3_WV_SC//Settings.MOC(TAG_EV3_WV_SC, "is_onDown_URDL: onDown_URDL=["+ onDown_URDL +"]");

        return (onDown_URDL != null);
    }
    //}}}
    // wvTools_onDown {{{
    private void wvTools_onDown(float x, float y)
    {
        gesture_down_sb                    = null;
      //gesture_down_wv                    = null;
        onDown_URDL                        = null;
        magnified_np_button                = null;
        onDown_x                           = x;
        onDown_y                           = y;
        sb_CB_has_been_called_since_onDown = false;
        has_moved_since_onDown             = false;
    }
    //}}}
    // }}}
    //* EV2_onMove */
    //{{{
    //{{{
    private  boolean has_moved_since_onDown = false;
    private NpButton magnified_np_button    = null;

    //}}}
    //? onMove .. (CLAMP) (SCROLL FOLLOW) (HOVER) {{{
    public boolean onMove(MotionEvent event)
    {
        // [SET has_moved_since_onDown] {{{
        String caller = "wvTools.onMove";
//*EV1_WV_IN*/Settings.MOC(TAG_EV1_WV_IN, caller);

        has_moved_since_onDown = true;

        String consumed_by = null;

        int x = (int)event.getRawX();
        int y = (int)event.getRawY();
        //}}}
//      // sampling rate control {{{
//      long time_elapsed = System.currentTimeMillis() - last_show_touched_marker_time;
//      if(  time_elapsed > SHOW_TOUCHED_MARKER_INTERVAL) {
//          last_show_touched_marker_time = System.currentTimeMillis();
//      }
//      //}}}

        if     (  onMove_1_MARKER_SPREAD_WAITING_FOR_POINTER_UP(event)) consumed_by = "onMove_1_MARKER_SPREAD_WAITING_FOR_POINTER_UP";
        else if(  onMove_2_WEBVIEW_SWAP_WAITING_FOR_UP         (     )) consumed_by = "onMove_2_WEBVIEW_SWAP_ON_UP";
        else if(  onMove_3_WEBVIEW_SWAP_TRACK                  (event)) consumed_by = "onMove_3_WEBVIEW_SWAP_TRACK";
        else if(  onMove_4_WEBVIEW_SWAP_START                  (event)) consumed_by = "onMove_4_WEBVIEW_SWAP_START";
        else if(  onMove_5_SCROLLBAR_PAGED_WAITING_FOR_UP      (     )) consumed_by = "onMove_5_SCROLLBAR_PAGED_WAITING_FOR_UP";
        else if(  onMove_6_SCROLLBAR_FOLLOW                    (     )) consumed_by = "onMove_6_SCROLLBAR_FOLLOW";
        else if(  onMove_7_SCROLLBAR_TRACK                     (x, y )) consumed_by = "onMove_7_SCROLLBAR_TRACK";
      //else if(  onMove_8_SCROLLBAR_MAGNETIZE                 (x, y )) consumed_by = "onMove_8_SCROLLBAR_MAGNETIZE";
        else if(  onMove_9_MARKER_FLOAT                        (     )) consumed_by = "onMove_9_MARKER_FLOAT";
        else if(  onMove_10_MARKER_UNLOCKING                   (     )) consumed_by = "onMove_10_MARKER_UNLOCKING";
        else if(  onMove_11_MARKER_MAGNETIZE_CLAMP             (event)) consumed_by = "onMove_11_MARKER_MAGNETIZE_CLAMP";
        else if(  onMove_12_MARKER_MAGNETIZE_START             (event)) consumed_by = "onMove_12_MARKER_MAGNETIZE_START";
        else if(  onMove_13_MARKER_MAGNETIZE_STOP              (     )) consumed_by = "onMove_13_MARKER_MAGNETIZE_STOP";
        else if(  onMove_14_marker_hover_expanded_marker       (x, y )) consumed_by = "onMove_14_marker_hover_expanded_marker";

        // [return consumed_by] {{{
//*EV1_WV_OK*/if(consumed_by != null) Settings.MOC(TAG_EV1_WV_OK, caller, "...return (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    //? onMove_1_MARKER_SPREAD_WAITING_FOR_POINTER_UP {{{
    private boolean onMove_1_MARKER_SPREAD_WAITING_FOR_POINTER_UP(MotionEvent event)
    {
        // [!waiting_for_ACTION_POINTER_UP] .. (return false) {{{
        if( !waiting_for_ACTION_POINTER_UP)     return false;

        String caller = "wvTools.onMove_1_MARKER_SPREAD_WAITING_FOR_POINTER_UP";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller);

        String consumed_by = null;
        //}}}
        // (ALREADY POSTED) {{{
        if(magnified_np_button != null)
        {
            consumed_by = "MAGNIFICATION ALREADY POSTED ON ["+magnified_np_button+"]";
        }
        //}}}
        // [post_pending_button_to_magnify] .. f(fingers spread) {{{
        else {
            // TRIGGER (MAGNIFICATION) ON [20% FINGER SPREAD] {{{
            float    distance_now = fingers_distance(event);
            boolean finger_spread = (distance_now >= (1.2 * distance_on_pointer_down));
            if( finger_spread )
            {
                // magnetized_marker_np_onDown  == null)
                // gesture_down_marker_np_to_CB == null)
                //           expanded_marker_np == null)
                // marker_magnetized_np_closest == null)

//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "onMove: .......finger_spread=["+finger_spread+"]=(distance_now="+distance_now+") >= (1.2 * distance_on_pointer_down="+distance_on_pointer_down+")");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "onMove: .............magnetized_marker_np_onDown=["+ magnetized_marker_np_onDown  +"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "onMove: ............gesture_down_marker_np_to_CB=["+ gesture_down_marker_np_to_CB +"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "onMove: ......................expanded_marker_np=["+ expanded_marker_np           +"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "onMove: ............marker_magnetized_np_closest=["+ marker_magnetized_np_closest +"]");

                if     (magnetized_marker_np_onDown  != null) magnified_np_button =  magnetized_marker_np_onDown.button;
                else if(gesture_down_marker_np_to_CB != null) magnified_np_button = gesture_down_marker_np_to_CB.button;
                else if(expanded_marker_np           != null) magnified_np_button =    expanded_marker_np       .button;
                else if(marker_magnetized_np_closest != null) magnified_np_button = marker_magnetized_np_closest.button;

                if(magnified_np_button != null) mRTabs.post_pending_button_to_magnify(magnified_np_button, 0);

                //waiting_for_ACTION_POINTER_UP = false; // NO! keep consuming

                consumed_by = "MAGNIFICATION REQUEST POSTED ON ["+magnified_np_button+"]";
            }
            //}}}
        }
        //}}}
        // [return consumed_by] {{{
//*EV2_WV_CB*/if(consumed_by != null) Settings.MOC(TAG_EV2_WV_CB, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    //? onMove_2_WEBVIEW_SWAP_WAITING_FOR_UP {{{
    private boolean onMove_2_WEBVIEW_SWAP_WAITING_FOR_UP()
    {
        // (fs_search_can_select_webview_on_ACTION_UP) {{{
        String caller = "wvTools.onMove_2_WEBVIEW_SWAP_WAITING_FOR_UP";

        String consumed_by
            =   mRTabs.fs_search_can_select_webview_on_ACTION_UP()
            ?    "fs_search_can_select_webview_on_ACTION_UP"
            :    null;

        //}}}
        // [return consumed_by] {{{
//*EV2_WV_CB*/if(consumed_by != null) Settings.MOC(TAG_EV2_WV_CB, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    //? onMove_3_WEBVIEW_SWAP_TRACK {{{
    private boolean onMove_3_WEBVIEW_SWAP_TRACK(MotionEvent event)
    {
        // (MOVE ON EXPANDED WEBVIEW) {{{
        if( !mRTabs.has_moved_enough                             ) return false;
        if( !mRTabs.fs_search_can_select_webview_on_ACTION_MOVE()) return false;
        if( !is_clamping_button( fs_search )                     ) return false;

              String caller = "wvTools.onMove_3_WEBVIEW_SWAP_TRACK";
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "..............................has_moved_enough=["+ mRTabs.has_moved_enough                              +"]");
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "...fs_search_can_select_webview_on_ACTION_MOVE=["+ mRTabs.fs_search_can_select_webview_on_ACTION_MOVE() +"]");
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, ".................is_clamping_button(fs_search)=["+ is_clamping_button(fs_search)                        +"]");

        //}}}
        // TEMPORARY HIDE MARKERS .. f(webview swap track) {{{
    //  marker_set_someone_has_called_hide_marks(caller);

        //}}}
        // MARKER MAGNETIZE: STOP (WEBVIEW SWAP) {{{
        if( marker_magnetized_START_DONE )
        {
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller, "MARKER MAGNETIZE: STOP .. (WEBVIEW SWAP)");
            marker_magnetize_stop(gesture_down_wv, caller);
        }
        //}}}
        // [mClamp_np_onMove] {{{
        mClamp_np_onMove(fs_search_np, event);

        //}}}
        // [WEBVIEW 1 2 3 SELECT] {{{
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "...calling expand_fs_webView_fullscreen_select_atX");
        mRTabs.expand_fs_webView_fullscreen_select_atX(event.getRawX(), false); // !toggle-off

        //}}}
        // [return consumed_by] {{{
//*EV2_WV_CB*/ Settings.MON(TAG_EV2_WV_CB, caller, "...return true");
        return true;
        //}}}
    }
    //}}}
    //? onMove_3_WEBVIEW_SWAP_START {{{
    //{{{
    private static final  long  SHOW_NEAREST_REST_ZONE_INTERVAL = 500; // ms
    private               long last_show_nearest_rest_zone_time =   0;
    //}}}
    private boolean onMove_4_WEBVIEW_SWAP_START(MotionEvent event)
    {
        // (is_clamping_button fs_search) {{{
        if(                     fs_search == null) return false;
        if( is_clamping_button( fs_search )      ) return false;
        if( !mRTabs.fs_search_can_swap_webview() ) return false;

        String caller = "wvTools.onMove_4_WEBVIEW_SWAP_START";
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, ".................is_clamping_button(fs_search)=["+ is_clamping_button(fs_search)                        +"]");
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "mRTabs.fs_search_can_swap_webview()=["+mRTabs.fs_search_can_swap_webview()+"]");

        //}}}
        // TEMPORARY HIDE MARKERS .. f(webview swap start) {{{
        marker_set_someone_has_called_hide_marks(caller);

        //}}}
        // EXPAND WEBVIEW AT X {{{
        int x = (int)event.getRawX();
        int y = (int)event.getRawY();
        mClamp_np_jump_toXY(fs_search_np, x, y, caller);

        mClamp_np_onMove(fs_search_np, event);

        mRTabs.expand_fs_webView_fullscreen_select_atX(x, false); // NOT toggle-off

        //}}}
        // SHOW NEAREST REST ZONE {{{
        long time_elapsed = System.currentTimeMillis() - last_show_nearest_rest_zone_time;
        if(  time_elapsed > SHOW_NEAREST_REST_ZONE_INTERVAL)
        {
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "SHOW NEAREST REST ZONE");

            last_show_nearest_rest_zone_time = System.currentTimeMillis();
            fs_search_show_nearest_rest_zone( fs_search );
        }
        //}}}
        // [return consumed_by] {{{
//*EV2_WV_CB*/ Settings.MON(TAG_EV2_WV_CB, caller, "...return true");
        return true;
        //}}}
    }
    //}}}
    //? onMove_5_SCROLLBAR_PAGED_WAITING_FOR_UP {{{
    private boolean onMove_5_SCROLLBAR_PAGED_WAITING_FOR_UP()
    {
        // (sb_has_paged_up_or_down) {{{
        if(!sb_has_paged_up_or_down && !sb_has_called_findNext) return false;

        String caller = "onMove_5_SCROLLBAR_PAGED_WAITING_FOR_UP";

        //}}}
        // [sb_has_paged_up_or_down] [sb_has_paged_up_or_down] {{{
        String consumed_by
        = (sb_has_called_findNext  ? "FINDING" : "")
        + (sb_has_paged_up_or_down ? " PAGING" : "")
        ;

        //}}}
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "...return true caller=["+consumed_by+"]");
        return true;
    }
    //}}}
    //? onMove_6_SCROLLBAR_FOLLOW {{{
    private boolean onMove_6_SCROLLBAR_FOLLOW()
    {
        // [!scroll_wv_can_follow_finger] .. (return false) {{{
        if( !scroll_wv_can_follow_finger() )  return false;

        //}}}
        // [onScroll to handle event] .. (prevent move event bubbling) .. (return true) {{{
        String caller = "wvTools.onMove_6_SCROLLBAR_FOLLOW";

//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "...return true");
        return true;
        //}}}
    }
    //}}}
    //? onMove_7_SCROLLBAR_TRACK {{{
    private boolean onMove_7_SCROLLBAR_TRACK(int x, int y)
    {
        // [!scroll_wv_can_track_finger] .. (return false) {{{
        if( !scroll_wv_can_track_finger() )  return false;

        String caller = "wvTools.onMove_7_SCROLLBAR_TRACK";
        //}}}
        // [onMove_7_scroll_track_finger] {{{

        //if( FOLLOW_FINGER_CLAMPED ) onMove_7_scroll_clamp_finger(gesture_down_wv, x, y);
        /*else*/                      onMove_7_scroll_track_finger(gesture_down_wv, x, y);

//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "...return true");
        return true;
        //}}}
    }
    //}}}
    //? onMove_8_SCROLLBAR_MAGNETIZE {{{
    private boolean onMove_8_SCROLLBAR_MAGNETIZE(int x, int y)
    {
        // [!scroll_wv_can_follow_finger] .. (return false) {{{
        if( gesture_down_sb == null       )   return false; // (no scrollbar involved)
        if( scroll_wv_can_follow_finger() )   return false; // (no tracking requested)

        String caller = "wvTools.onMove_8_SCROLLBAR_MAGNETIZE";
        //}}}
        // MAGNETIZE START {{{
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "SCROLLBAR MAGNETIZE START");

        sb_magnetize(x, y);

//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "...return true");
        return true;
        //}}}
    }
    //}}}
    //? onMove_9_MARKER_FLOAT {{{
    private boolean onMove_9_MARKER_FLOAT()
    {
        // (gesture_down_marker_np_to_CB)==(floating_marker_np) {{{
        if(gesture_down_marker_np_to_CB == null              ) return false;
        if(    floating_marker_np       == null              ) return false;
        if(gesture_down_marker_np_to_CB != floating_marker_np) return false;

        String caller = "wvTools.onMove_9_MARKER_FLOAT";
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "...floating_marker_np=["+floating_marker_np+"]");
        //}}}
        return true;
    }
    //}}}
    //? onMove_10_MARKER_UNLOCKING {{{
    private boolean onMove_10_MARKER_UNLOCKING()
    {
        // (magnetized_marker_np_onDown)=>(floating_marker_np) {{{
        if( magnetized_marker_np_onDown == null) return false;

        String caller = "wvTools.onMove_10_MARKER_UNLOCKING";
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "...magnetized_marker_np_onDown=["+magnetized_marker_np_onDown+"]");
        //}}}
        return true;
    }
    //}}}
    //? onMove_11_MARKER_MAGNETIZE_CLAMP {{{
    private boolean onMove_11_MARKER_MAGNETIZE_CLAMP(MotionEvent event)
    {
        // (marker_magnetized_START_DONE) (may_resume_magnetize) {{{
        String caller = "wvTools.onMove_11_MARKER_MAGNETIZE_CLAMP";
        if(!marker_magnetized_START_DONE   ) return false; // not started yet
      //if( property_get_WV_TOOL_MARK_LOCK ) return false; // property change may happen while magnetizing
      //if( !may_resume_magnetize(caller)  ) return false;

        //}}}
        // CLAMP MOVE {{{
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "MAGNETIZE: MOVE CLAMP");

        mClamp_np_onMove(fs_search_np, event);
        //}}}
        // MARGIN VISUALIZE [MAGNETIZE-ABOUT-TO-STOP-CONDITION] {{{
        if( is_fs_search_in_margins() )
        {
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, "", "VISUALIZE MAGNETIZE IN MARGIN STOP-CONDITION");

            visualize_MARGIN_rect(gesture_down_wv, is_sb_at_left() ? MARGIN_R : MARGIN_L);
        }
        else {
            activity_blink_rect_hide(); // erase margin area highlighting
        }
        //}}}
        // [return consumed_by] {{{
//*EV2_WV_CB*/ Settings.MOC(TAG_EV2_WV_CB, caller, "...return true");
        return true;
        //}}}
    }
    //}}}
    //? onMove_12_MARKER_MAGNETIZE_START {{{
    private boolean onMove_12_MARKER_MAGNETIZE_START(MotionEvent event)
    {
        // (property_get_WV_TOOL_MARK_LOCK) {{{
        if( property_get_WV_TOOL_MARK_LOCK     ) return false;
        if( property_get( WV_TOOL_MDRAG_LOCK ) ) return false;

        String caller = "wvTools.onMove_12_MARKER_MAGNETIZE_START";
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "...property_get_WV_TOOL_MARK_LOCK=["+property_get_WV_TOOL_MARK_LOCK  +"]");
        //}}}
        // (gesture_down_sb) {{{
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, ".........................gesture_down_sb=["+ gesture_down_sb +"]");
        if(  gesture_down_sb != null                             ) return false; // (NOT WHILE dragging scrollbar        )

        //}}}
        // (have_seek_min_max) {{{
        boolean      have_seek_min_max =       marker_expand_x_min  <   marker_expand_x_max      ;
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "........................have_seek_min_max=["+ have_seek_min_max                      +"]");
        if( !have_seek_min_max                                   ) return false; // (NOT WHILE having   no defined margin)

        //}}}
        int x = (int)event.getRawX();
        int y = (int)event.getRawY();
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "...............marker_magnetized_np_count=["+ marker_magnetized_np_count             +"]");
        if(marker_magnetized_np_count < 1)
        {
        // (onDown_URDL) {{{
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "..............................onDown_URDL=["+ onDown_URDL                            +"]");
        if((onDown_URDL != MARGIN_L) && (onDown_URDL != MARGIN_R)) return false; // (NOT AFTER finger  down not in margin)

        //}}}
        // (out_of_seek_min_max) {{{

        boolean    out_of_seek_min_max = ((x < marker_expand_x_min) || (marker_expand_x_max < x));
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "......................out_of_seek_min_max=["+ out_of_seek_min_max                    +"]");
        if( !out_of_seek_min_max                                 ) return false; // (NOT WHILE finger currently in margin)

        //}}}
        }
        // (is_floating_marker) {{{
/*EV2_WV_CB//Settings.MOM(TAG_EV2_WV_CB, "...is_floating_marker(expanded_marker_np)=["+ is_floating_marker(expanded_marker_np) +"]");
      //if(  is_floating_marker( expanded_marker_np)             ) return false; // (NOT WHILE already floating a marker )

        //}}}
        // MAGNETIZE START [marker_magnetize] {{{
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "MARKER MAGNETIZE START");

        NpButton sbX = get_sb_for_wv_or_sb( gesture_down_wv );
        if(                (sbX != null)
                && (        sbX.at_left && (onDown_URDL == MARGIN_R)    // hovering RIGHT margin (with sb at left )
                    ||     !sbX.at_left && (onDown_URDL == MARGIN_L))   // hovering  LEFT margin (with sb at right)
          )
        {
            if((expanded_marker_np != null) && !is_floating_marker( expanded_marker_np ))
            {
                marker_unexpand_np    ( "onMove.clamping" );
                set_expanded_marker_np(              null ); // cancel expand handling
            }
            marker_magnetize(x, y, caller); // markers are in opposite margin
        }
        //}}}
        // [marker_magnetized_START_DONE] .. f(marker_magnetized_np_count) {{{
        if(marker_magnetized_np_count > 0)
        {
            marker_magnetized_START_DONE = true;
            gesture_down_marker_np_to_CB = null; // (ignore some touched marker MARKER MAGNETIZE START)
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "...gesture_down_marker_np_to_CB set to ["+gesture_down_marker_np_to_CB+"] (forget about marker touched onDown when MARKER MAGNETIZE KICKS IN)");

            mClamp_np_jump_toXY(fs_search_np, x, y, caller);

//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller, "MAGNETIZE: MOVE CLAMP");
            mClamp_np_onMove(fs_search_np, event);
        }
        //}}}
        // [return marker_magnetized_START_DONE] {{{
//*EV1_WV_OK*/if(marker_magnetized_START_DONE) Settings.MOC(TAG_EV1_WV_OK, caller, "...return true (marker_magnetized_START_DONE)");
        return  marker_magnetized_START_DONE;
        //}}}
    }
    //}}}
    //? onMove_13_MARKER_MAGNETIZE_STOP {{{
    private boolean onMove_13_MARKER_MAGNETIZE_STOP()
    {
        // (marker_magnetized_np_count) {{{
        if(marker_magnetized_np_count < 1) return false;

        String caller = "wvTools.onMove_13_MARKER_MAGNETIZE_STOP";
        //}}}
        // MARKER MAGNETIZE: STOP (NO MARKERS) {{{
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller, "MARKER MAGNETIZE: STOP .. (NO MARKER)");
        marker_magnetize_stop(gesture_down_wv, caller);
        //}}}
//        // [marker_unexpand_np] .. f(out_of_seek_min_max) {{{
////EV2_WV_CB//Settings.MOM(TAG_EV2_WV_CB, caller+": FEEDBACK CLEAR");
//
//        int                        x = (int)event.getRawX();
//
//        boolean    have_seek_min_max =       marker_expand_x_min  <   marker_expand_x_max      ;
//        boolean  out_of_seek_min_max = ((x < marker_expand_x_min) || (marker_expand_x_max < x));
//        boolean nothing_to_visualize = (!have_seek_min_max || out_of_seek_min_max);
/////EV2_WV_CB//Settings.MOM(TAG_EV2_WV_CB, ".1....have_seek_min_max=["+ have_seek_min_max    +"]");
/////EV2_WV_CB//Settings.MOM(TAG_EV2_WV_CB, ".2..out_of_seek_min_max=["+ out_of_seek_min_max  +"]");
/////EV2_WV_CB//Settings.MOM(TAG_EV2_WV_CB, ".=>nothing_to_visualize=["+ nothing_to_visualize +"]");
//
//        if( nothing_to_visualize)
//        {
//            if(expanded_marker_np != null)
//                marker_unexpand_np("onMove.nothing_to_visualize");
//        }
//        //}}}
        return false; // not consumed
    }
    //}}}
    //? onMove_14_marker_hover_expanded_marker {{{
    private boolean onMove_14_marker_hover_expanded_marker(int x, int y)
    {
        // (out_of_seek_min_max) (marker_pin_count_onDown) {{{
        String caller = "wvTools.onMove_14_marker_hover_expanded_marker";

//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, caller+": ........WV_TOOL_MARK_LOCK=["+ property_get_WV_TOOL_MARK_LOCK +"]");
        if( property_get_WV_TOOL_MARK_LOCK          ) return false;

//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, caller+": ...marker_pin_count_onDown=["+ marker_pin_count_onDown                +"])");
        if(marker_pin_count_onDown < 1               ) return false;

        boolean out_of_seek_min_max = ((x < marker_expand_x_min) || (marker_expand_x_max < x));
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, caller+": .......out_of_seek_min_max=["+ out_of_seek_min_max                    +"]");
        if( out_of_seek_min_max                      ) return false;

        //}}}
        // [marker_hover_expanded_marker] .. f(finger) .. f(scrollbar top) {{{
        int hotY = (gesture_down_sb != null) ? (int)gesture_down_sb.getY()
            /*............................*/ :      y;

        // [hotY] .. (off finger clutter  VERTICAL SHIFT)
        if( hotY > (y - Settings.FINGER_CLUTTER_OFFSET))
            hotY =  y - Settings.FINGER_CLUTTER_OFFSET;

//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, caller+": calling marker_hover_expanded_marker");
        marker_hover_expanded_marker(gesture_down_wv, x, hotY);

        //}}}
        return false; // not consumed
    }
    //}}}

    // marker_hover_expanded_marker {{{
    private boolean marker_hover_expanded_marker(RTabs.MWebView wv, int x, int y)
    {
        // STICK WITH [expanded_marker_np] AS LONG IT IS THE NEAREST {{{
        String caller = "marker_hover_expanded_marker";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller);

        if(        (expanded_marker_np != null)
                && is_marker_nearXY(expanded_marker_np, x, y)
          )
        {
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "...return false .. (same expanded_marker_np=["+expanded_marker_np+"])");
            return false;
        }
        NotePane was_expanded_marker_np = expanded_marker_np;

        // }}}
        // GET NEAREST MARKER {{{
        NotePane marker_np =                  marker_get_marker_near_XY(wv, x, y); // NEAR-MARKER .. f(x,y)

        if     ( marker_np != null          ) marker_expand_np(wv, marker_np    ); // 1/3 [CHANGE] [expanded_marker_np]
        else if(!hover_between_min_max(x,y) ) marker_expand_np(wv, null         ); // 3/3 [FORGET] [expanded_marker_np]
        /*......................................................................*/ // 2/3 [  KEEP] [expanded_marker_np]

        //}}}
        // UNWRAP ALL MARKERS .. f(expanded_marker_np != null) {{{
        if(expanded_marker_np != null) {
            marker_set_wrap_state( false );
        }
        //}}}
        // OR [visualize_SEEK_rect] .. f(expanded_marker_np == null) {{{
        else {
            visualize_SEEK_rect(x, y, caller);
        }
        //}}}
        // [sb_show_MARKER_EXPAND] .. f(expanded_marker_np) {{{
        if(expanded_marker_np != null)
            sb_show_MARKER_EXPAND( expanded_marker_np.getLabel() );

        //}}}
        // [return marker_switched] {{{
        boolean marker_switched = (was_expanded_marker_np != null) && (expanded_marker_np != null);

//*EV2_WV_CB*/if(expanded_marker_np != null) Settings.MON(TAG_EV2_WV_CB, caller, "...return marker_switched=["+marker_switched+"]");
        return  marker_switched;
        //}}}
    }
    //}}}

    // scroll_wv_can_track_finger {{{
    private boolean scroll_wv_can_track_finger()
    {
        if( gesture_down_wv == null               ) return false;
        if( gesture_down_sb == null               ) return false;
        if( scroll_wv_can_follow_finger()         ) return false; // follow or track
        if( gesture_down_sb.action_down_in_margin ) return false; // (using scrollbar) .. (not ajusting scrollbar)

        return true;
    }
        //}}}
    // fingers_distance {{{
    public float fingers_distance(MotionEvent event)
    {
        if(event.getPointerCount() < 2) return 0;

        final float x = event.getX(0) - event.getX(1);
        final float y = event.getY(0) - event.getY(1);

        return (float) Math.sqrt(x * x + y * y);
    }
    //}}}
    // can_move_something {{{
    public boolean can_move_something()
    {
        boolean result
            =   (   gesture_down_sb  != null)
            ||  (   gesture_down_wv  != null)
            ||  (marker_expand_x_min <  marker_expand_x_max)
            ||   mRTabs.fs_search_can_swap_webview()
            ;
        return result;
    }
    //}}}
    // hover_between_min_max {{{
    private boolean hover_between_min_max(int x, int y)
    {
        boolean result = ((marker_expand_x_min <= x) && (x <= marker_expand_x_max)); // in the markers column
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, String.format("hover_between_min_max: ...return [%b] (%4d <= %4d <= %4d)", result, marker_expand_x_min, x, marker_expand_x_max));
        return result;
    }
    //}}}
    //}}}
    //* EV3_onScroll */
    //{{{
    // onScroll {{{
    public boolean onScroll(MotionEvent e2, float distanceX, float distanceY)
    {
        // (gesture_down_wv) {{{
        if(       waiting_for_ACTION_POINTER_UP          ) return false; // (spread) (magnify)
        if(mRTabs.gesture_down_SomeView_atXY == fs_search) return false; // (swapping_webView)
        if(       gesture_down_wv            == null     ) return false;

        String caller = "wvTools.onScroll";
//*EV1_WV_IN*/Settings.MOM(TAG_EV1_WV_IN, caller);

        int x = (int)e2.getRawX();
        int y = (int)e2.getRawY();

        String consumed_by = null;
        //}}}

        if(      onScroll_1_sb_adjust            (e2  , distanceX, distanceY) ) consumed_by = "onScroll_1_sb_adjust";
        else if( onScroll_2_sb_PageScroll        (x, y,            distanceY) ) consumed_by = "onScroll_2_sb_PageScroll";
        else if( onScroll_3_sb_follow_finger     (                 distanceY) ) consumed_by = "onScroll_3_sb_follow_finger";
        else if( onScroll_4_floating_marker_swing(x, y                      ) ) consumed_by = "onScroll_4_floating_marker_swing";

        // [return consumed_by] {{{
//*EV1_WV_OK*/if(consumed_by != null) Settings.MOC(TAG_EV1_WV_OK, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    // onScroll_1_sb_adjust {{{
    private       long last_sb_reshape_time  =   0;
    private final long SB_RESHAPING_COOLDOWN = 200;
    private boolean onScroll_1_sb_adjust(MotionEvent e2, float distanceX, float distanceY)
    {
        // (gesture_down_sb) [dx] [dy] {{{
        if(gesture_down_sb == null) return false;

        String caller = "wvTools.onScroll_1_sb_adjust";
//EV3_WV_SC//Settings.MOC(TAG_EV3_WV_SC, caller);

        int                      dx = (int)distanceX;
        int                      dy = (int)distanceY;
        boolean     horizontal_move = (Math.abs(dx) > (2 * Math.abs(dy)));
        String          consumed_by = null;
        //}}}
        // (action_down_in_margin) {{{
        if( gesture_down_sb.action_down_in_margin )
        {
            consumed_by         = "action_down_in_margin";  // CONFISCATE MOVE IN SCROLLBAR MARGIN

            gesture_down_wv.setAlpha( Settings.WV_ADJUSTING_SB_OPACITY ); // dimm webview while adjusting scrollbar
        }
        // }}}
        // (horizontal_move) {{{
        if( horizontal_move ) {
            consumed_by  += ".horizontal_move";       // CONFISCATE HORIZONTAL MOVE IN SCROLLBAR
            // [reframing] {{{
            int     width        = gesture_down_sb.getWidth();
            String  shape        = gesture_down_sb.shape;
            boolean tools_hidden = (!Settings.WV_TOOL_MISC_VIS && !Settings.WV_TOOL_JSNP_VIS);

            boolean reframing    =  tools_hidden
                ||                 (shape == NotePane.SHAPE_TAG_SCROLL   ) // [SCROLLBAR FRAME] (FINE ADJUSTMENT)
                ||                 (width <  Settings.TOOL_BADGE_SIZE    ) // [SCROLLBAR SMALL] (BELOW GRID SIZE)
                ;

//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, caller, "reframing=["+reframing+"]: shape=["+ shape +"] width=["+ width +"]"+(tools_hidden ? " [tools_hidden]": ""));
            //}}}
            // [reshaping] {{{
            boolean reshaping = false;
            if( !reframing )
            {
                boolean        large_sb_width = (width > (3 * Settings.TOOL_BADGE_SIZE));// [GRID SIZE  ] grid-resizing

                boolean is_a_large_gesture    = (Math.abs(dx) >= Settings.TOOL_BADGE_SIZE / 8);
                boolean is_a_huge_gesture     = (Math.abs(dx) >= Settings.TOOL_BADGE_SIZE / 2);

                long             time_elapsed = System.currentTimeMillis() - last_sb_reshape_time;
                boolean reshaping_on_cooldown = (time_elapsed <  SB_RESHAPING_COOLDOWN);

                reshaping
                    =   is_a_huge_gesture
                    || (is_a_large_gesture && (large_sb_width || !reshaping_on_cooldown))
                    ;

              if( reshaping ) last_sb_reshape_time = System.currentTimeMillis();

//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, caller, "reshaping=["+reshaping+"]: shape=["+ shape +"]"+(large_sb_width ? " [large_sb_width]" : "")+(reshaping_on_cooldown ? " [reshaping_on_cooldown]" : "")+(is_a_large_gesture ? " [is_a_large_gesture]" : "")+(is_a_huge_gesture ? " [is_a_huge_gesture]" : ""));
            }
            //}}}
            // [SCROLLBAR ADJUST] {{{
            if(reshaping || reframing)
            {
                sb_layout_frame(gesture_down_sb, gesture_down_wv, dx);
                sb_layout_tools(gesture_down_sb, caller); // after user scrollbar frame change
            }
            //}}}
        }
        // }}}
        // [return consumed_by] {{{
//*EV3_WV_SC*/if(consumed_by != null) Settings.MOC(TAG_EV3_WV_SC, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    // onScroll_2_sb_PageScroll {{{
    private static        int DISTANCE_PAGE_Y_MIN = Settings.SCALED_TOUCH_SLOP / 3; // i.e. (a touching finger wiggles less than a landing finger)

    private boolean onScroll_2_sb_PageScroll(int x, int y, float distanceY)
    {
        String caller = "wvTools.onScroll_2_sb_PageScroll";

        // (distanceY > DISTANCE_PAGE_Y_MIN) {{{
        if(Math.abs(distanceY) < DISTANCE_PAGE_Y_MIN)
        {
//*EV3_WV_SC*/Settings.MON(TAG_EV3_WV_SC, caller, "...([distanceY="+distanceY+"] < [DISTANCE_PAGE_Y_MIN="+DISTANCE_PAGE_Y_MIN+"])");

            return false;
        }
        //}}}
        // (scroll_wv_can_PageUp_PageDown) {{{
        if(!scroll_wv_can_PageUp_PageDown() ) return false;

        boolean page_up = (distanceY < 0);
        //}}}
        // START FINDING OR PAGING {{{
        if(!sb_has_paged_up_or_down && !sb_has_called_findNext)
        {
            // (can_call_scroll_wv_find_selection) {{{
            boolean can_call_scroll_wv_find_selection
                =    property_get( WV_TOOL_JS1_SELECT )
                &&   gesture_down_wv.has_selection_value()
                ;

//*EV3_WV_SC*/Settings.MOC(TAG_EV3_WV_SC, "", "can_call_scroll_wv_find_selection=["+can_call_scroll_wv_find_selection+"]");
            //}}}
            // (finger_has_reached_scrollbar) {{{
            if( !can_call_scroll_wv_find_selection )
            {
                NpButton                          sbX = get_sb_for_wv_or_sb( gesture_down_wv );
                boolean  finger_has_reached_scrollbar = (sbX != null) && is_sb_atXY(sbX, x, y);
//*EV3_WV_SC*/Settings.MOC(TAG_EV3_WV_SC, "", "finger_has_reached_scrollbar=["+finger_has_reached_scrollbar+"]");

                if( !finger_has_reached_scrollbar )
                {
//*EV3_WV_SC*/Settings.MOC(TAG_EV3_WV_SC, "", "STILL WAITING FOR FINGER TO REACH SCROLLBAR");

                    return false; // not consumed
                }
                // IGNORE FINGER TRAVEL ON OTHER THAN START PAGE {{{
                else {
                    distanceY = 0;
                }
                //}}}
            }
            //}}}
        }
        //}}}
        // [scroll_wv_find_selection] [scroll_wv_anim_PageUpDown] {{{
        String consumed_by
            =   scroll_wv_find_selection (gesture_down_wv, x, y, page_up, (int)distanceY) ?  "scroll_wv_find_selection"
            :   scroll_wv_anim_PageUpDown(gesture_down_wv, x, y, page_up, (int)distanceY) ?  "scroll_wv_anim_PageUpDown"
            :   null;

        //}}}
        // [return consumed_by] {{{
//*EV3_WV_SC*/if(consumed_by != null) Settings.MOC(TAG_EV3_WV_SC, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    // onScroll_3_sb_follow_finger {{{
    private boolean onScroll_3_sb_follow_finger(float distanceY)
    {
        // (scroll_wv_can_follow_finger) {{{
        if(  gesture_down_wv == null       ) return false;
        if( !scroll_wv_can_follow_finger() ) return false;

        String caller = "onScroll_3_sb_follow_finger";
//EV3_WV_SC//Settings.MOC(TAG_EV3_WV_SC, caller);

        String consumed_by = null;
        //}}}
        // [scrollBy] .. f(distanceY) {{{
        int   wv_page_height = Math.max(gesture_down_wv.computeVerticalScrollRange (), 1);
        int             wv_h = gesture_down_wv.getHeight();

        float    viewport_fx = (float)wv_page_height / (float)wv_h;
        int        scroll_dy = (int)(viewport_fx * -distanceY);

        int          scrollY = gesture_down_wv.getScrollY();

        if(    ((scrollY + scroll_dy) > (0                    ))
            && ((scrollY + scroll_dy) < (wv_page_height - wv_h))
        ) {
            gesture_down_wv.scrollBy(0, scroll_dy);
            gesture_down_wv.setAlpha( 1.0f );

            consumed_by = "...gesture_down_wv.scrollBy(0, "+scroll_dy+")";
        }
        //}}}
        // [return consumed_by] {{{
//*EV3_WV_SC*/if(consumed_by != null) Settings.MOC(TAG_EV3_WV_SC, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    // onScroll_4_floating_marker_swing {{{
    private boolean onScroll_4_floating_marker_swing(int f_x, int f_y)
    {
        // ONGOING FLOATING MARKER [swing_Magnet] (hotspot x y) {{{
        if(swing_Magnet == null) swing_Magnet = new Magnet( swingMagnetListener );

        if( swing_Magnet.is_animating() )
        {
            swing_hotspot_f_x = f_x;
            swing_hotspot_f_y = f_y;
            return true;
        }
        //}}}
        //    (floating_marker_np) (gesture_down_marker_np_to_CB) {{{
        if(    floating_marker_np        == null                 ) return false;
        if(    floating_marker_np.button == null                 ) return false;
        if(gesture_down_marker_np_to_CB  == null                 ) return false;
        if(gesture_down_marker_np_to_CB  != floating_marker_np   ) return false;
        if((swing_Magnet != null) && swing_Magnet.is_animating() ) return false;

        String caller = "wvTools.onScroll_4_floating_marker_swing";
//*EV3_WV_SC*/Settings.MOC(TAG_EV3_WV_SC, caller);
        //}}}
        // [swing_magnetize] (f_x) (f_y) {{{
        swing_magnetize(floating_marker_np.button, f_x, f_y, caller);

        return true;
        //}}}
    }
    //}}}
    //* SWING (MAGNET) */
    //{{{
    //{{{
    private static     float SWING_MAGNET_ROTATION       =  30f;
    private static    Magnet swing_Magnet                = null;
    private static       int swing_hotspot_f_x           =    0;
    private static       int swing_hotspot_f_y           =    0;
  //private static  NotePane swing_np                    = null;

    //}}}
    // swing_magnetize {{{
    private void swing_magnetize(NpButton nb, int f_x, int f_y, String caller)
    {
        caller += "->swing_magnetize("+nb+", "+f_x+","+f_y+")";
//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "...nb=["+ nb +"]");

        // (floating_marker_np) {{{
        if((swing_Magnet != null) && swing_Magnet.is_animating())
        {
            swing_hotspot_f_x = f_x;
            swing_hotspot_f_y = f_y;
            return;
        }
        //}}}
        // DISCARD HOTSPOT TRACKING {{{
        else {
            swing_hotspot_f_x = 0;
            swing_hotspot_f_y = 0;

        }
        //}}}
        // [set_x_to] [set_yrto] .. f(f_x, f_y) {{{
        float       dx = f_x - onDown_x;
        float unlock_x = marker_float_get_unlocked_x();

        float    m_y = nb.get_y_from();
        float    m_s = Settings.MARK_SCALE_GROW;
        float    m_h = Settings.TOOL_BADGE_SIZE * m_s;

        float    m_b = m_y +  m_h     ;                // [bottom] (pivotY)
        float    m_t = m_b - (m_h*m_s);                // [top]    (scaled)
      //float max_dx = Settings.TOOL_BADGE_SIZE * m_s; // [max_dx] (marker width) (scaled)
        float max_dx = marker_float_get_unlocked_x();  // [max_dx] (screen width)

        float x_to =  unlock_x;
        float yrto =  0f;
        float zrto =  0f;
        if(        (Math.abs(dx) <  max_dx)            // not to far away
                && (        f_y  >= m_t   )            // below top
                && (        f_y  <= m_b   )            // above bottom
          )
        {
            x_to = unlock_x + dx;
          //yrto = SWING_MAGNET_ROTATION   * dx / max_dx;
            zrto = SWING_MAGNET_ROTATION   * dx / max_dx;
        }

//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "SAVING ATTITUDE AND LOCATION OF ["+nb+"]:");
//*MAGNET*/Settings.MOM(TAG_MAGNET, nb.description      ());
//*MAGNET*/Settings.MOM(TAG_MAGNET, nb.description_saved());
        nb.save_from(caller);
//*MAGNET*/Settings.MOM(TAG_MAGNET, nb.description_from());

        nb.set_x_to( x_to );
        nb.set_yrto( yrto );
        nb.set_zrto( zrto );

//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "SET TO ATTITUDE AND LOCATION:");
//*MAGNET*/Settings.MOC(TAG_MAGNET, ""    , nb.description_to());

      //boolean at_left = (dx < 0);
      //nb.setPivotX(at_left ? 0 : nb.get_saved_w());
      //nb.setPivotX(at_left ? 0 : nb.getWidth());

        swing_Magnet.animate         (   SWING_MAGNET_TRACK_DURATION );

        //}}}
    }
    //}}}
    // swingMagnetListener {{{ */
    private       Settings.MagnetListener swingMagnetListener = new Settings.MagnetListener()
    {
        public  void    magnet_1_progress(float progress) { swing_1_progress(progress); }
        public  boolean magnet_2_tracked               () { return swing_2_tracked();   } // called by Magnet.onAnimationEnd
        public  boolean magnet_3_reached               () { return swing_3_reached();   } // called by Magnet.onAnimationEnd
        public  boolean magnet_4_checked               () { return false;               }
        public  boolean magnet_freezed                 () { return false;               }
    };
    //}}}
    // swing_Magnet_cancel_animation {{{
    private void swing_Magnet_cancel_animation(NpButton nb, String caller)
    {
        caller += "->swing_Magnet_cancel_animation";

//*MAGNET*/Settings.MOC(TAG_MAGNET, caller);
//*MAGNET*/Settings.MOM(TAG_MAGNET, nb.description      ());
//*MAGNET*/Settings.MOM(TAG_MAGNET, nb.description_saved());
//*MAGNET*/Settings.MOM(TAG_MAGNET, nb.description_to   ());

        swing_Magnet.cancel_animation();

        // kill tracking
        swing_hotspot_f_x = 0;
        swing_hotspot_f_y = 0;
    }
    //}}}
    // swing_Magnet_is_animating {{{
    private boolean swing_Magnet_is_animating()
    {
        return (swing_Magnet == null) ? false : swing_Magnet.is_animating();
    }
    //}}}
    // swing_1_progress {{{
    private void swing_1_progress(float progress)
    {
        // (floating_marker_np) {{{
//*MAGNET*/String caller = String.format("swing_1_progress(%1.2f)", progress);//TAG_MAGNET
//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "...floating_marker_np=["+ floating_marker_np +"]");

        if(floating_marker_np == null) return;

        NpButton nb = floating_marker_np.button;

        //}}}
        // from-to [xy] [scale] rotation[xyz] {{{

        float      x_from =  nb.get_x_from();
        float      x_to   =  nb.get_x_to  ();
        if(        x_to  !=  x_from) {
            float  x      = (x_from + (x_to - x_from) * progress);
            nb.setX         (x);
        }
        float      y_from =  nb.get_y_from();
        float      y_to   =  nb.get_y_to  ();
        if(        y_to  !=  y_from) {
            float  y      = (y_from + (y_to - y_from) * progress);
            nb.setY         (y);
        }

        float      s_from =  nb.get_s_from();
        float      s_to   =  nb.get_s_to  ();
        if(        s_to  !=  s_from) {
            float  s      = (s_from + (s_to - s_from) * progress);
            nb.setScaleX    (s);
            nb.setScaleY    (s);
        }

        float      xrfrom =  nb.get_xrfrom();
        float      xrto   =  nb.get_xrto  ();
        if(        xrto  !=  xrfrom) {
            float  xr     = (xrfrom + (xrto - xrfrom) * progress);
            nb.setRotationX (xr);
        }

        float      yrfrom =  nb.get_yrfrom();
        float      yrto   =  nb.get_yrto  ();
        if(        yrto  !=  yrfrom) {
            float  yr     = (yrfrom + (yrto - yrfrom) * progress);
            nb.setRotationY (yr);
        }

        float      zrfrom =  nb.get_zrfrom();
        float      zrto   =  nb.get_zrto  ();
        if(        zrto  !=  zrfrom) {
            float  zr     = (zrfrom + (zrto - zrfrom) * progress);
            nb.setRotation  (zr);
        }
        //}}}
    }
    //}}}
    // swing_2_tracked {{{
    private boolean swing_2_tracked()
    {
        // SWING DONE .. f(!hotspot_changed) {{{
        String caller = "swing_2_tracked";

        boolean hotspot_changed = ((swing_hotspot_f_x > 0) || (swing_hotspot_f_y > 0));
        if( !hotspot_changed )
        {
//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "SWING ANIM DONE .. (NOT hotspot_changed) "+ String.format("(%4d %4d)", swing_hotspot_f_x, swing_hotspot_f_y));
            return true;
        }
        //}}}
        // NO: SWING POST {{{
        else {
//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "SWING ANIM POST .. (hotspot_changed)");
            mRTabs.handler.re_post( hr_swing_track );
            return false;
        }
        //}}}
    }
    private Runnable hr_swing_track = new Runnable() {
        @Override public void run()
        {
            if(floating_marker_np        == null)                    return;
            if(floating_marker_np.button == null)                    return;

            if((swing_hotspot_f_x <= 0) && (swing_hotspot_f_y <= 0)) return;

            swing_magnetize(floating_marker_np.button, swing_hotspot_f_x, swing_hotspot_f_y, "hr_swing_track");
        }
    };
    //}}}
    // swing_3_reached {{{
    private boolean swing_3_reached()
    {
        String caller = "swing_3_reached";
//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "...........mm_check_pending=["+ mm_check_pending         +"]");
//*MAGNET*/Settings.MOC(TAG_MAGNET, ""    , "...floating_marker_np......=["+ floating_marker_np       +"]");

        marker_float_onStageChanged(caller);

        return true;
    }
    //}}}
    // swing_np_1_to_unlocked {{{
    private void swing_np_1_to_unlocked()
    {
        // [nb] .. f(floating_marker_np) {{{
        String caller = "swing_np_1_to_unlocked";
//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "...floating_marker_np=["+ floating_marker_np +"]");

        if(floating_marker_np == null) return;

        NpButton nb = floating_marker_np.button;
        //}}}
        // ADJUST AND SAVE FROM ATTITUDE AND LOCATION {{{
      //nb.setRotation(-180f );

        swing_np_SAVE_FROM_ATTITUDE_AND_LOCATION(nb, caller);
        //}}}
        // SET TO ATTITUDE AND LOCATION {{{

        // [default rotation]
        nb.set_xrto( 0f );
        nb.set_yrto( 0f );
        nb.set_zrto( 0f );

        // [grow scale]
        nb.set_s_to( Settings.MARK_SCALE_GROW ); // see marker_expand called from [marker_float_onStageChanged .. MARKER_STAGE2_UNLOCKED]

        // [unlocked location]
        int toX = (int)WVTools_instance.marker_float_get_unlocked_x();
        int toY = marker_float_getY_on_sb( floating_marker_np );

        nb.set_x_to( toX );
        nb.set_y_to( toY );

//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "SET TO ATTITUDE AND LOCATION:");
//*MAGNET*/Settings.MOC(TAG_MAGNET, ""    , nb.description_to());
        //}}}
        // START ANIMATION {{{
        swing_Magnet.animateOvershoot( SWING_MAGNET_TRACK_DURATION );
        //}}}
    }
    //}}}
    // swing_np_2_to_saved_scale {{{
    private void swing_np_2_to_saved_scale()
    {
        // [nb] .. f(floating_marker_np) {{{
        String caller = "swing_np_2_to_saved_scale";
//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "...floating_marker_np=["+ floating_marker_np +"]");

        if(floating_marker_np == null) return;

        NpButton nb = floating_marker_np.button;
        //}}}
        // ADJUST AND SAVE FROM ATTITUDE & LOCATION {{{
      //nb.setRotation(-180f );
        swing_np_SAVE_FROM_ATTITUDE_AND_LOCATION(nb, caller);

        //}}}
        // SET TO ATTITUDE AND LOCATION {{{

        // [default rotation]
        nb.set_xrto( 0f );
        nb.set_yrto( 0f );
        nb.set_zrto( 0f );

        // [default scale]
        nb.set_s_to( 1f );

        // [current location]

//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "SET TO ATTITUDE AND LOCATION:");
//*MAGNET*/Settings.MOC(TAG_MAGNET, ""    , nb.description_to());
        //}}}
        // START ANIMATION {{{
        swing_Magnet.animateOvershoot( SWING_MAGNET_TRACK_DURATION );

        //}}}
    }
    //}}}
    // swing_np_3_to_saved_xy {{{
    private void swing_np_3_to_saved_xy()
    {
        // [nb] .. f(floating_marker_np) {{{
        String caller = "swing_np_3_to_saved_xy";
//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "...floating_marker_np=["+ floating_marker_np +"]");

        if(floating_marker_np == null) return;

        NpButton  nb = floating_marker_np.button;
        //}}}
        // ADJUST AND SAVE FROM ATTITUDE & LOCATION {{{
        nb.setRotation(-180f );
        swing_np_SAVE_FROM_ATTITUDE_AND_LOCATION(nb, caller);

//*MAGNET*/Settings.MOM(TAG_MAGNET, nb.description_from());
        //}}}
        // SET TO ATTITUDE AND LOCATION {{{
        // [default rotation]
        nb.set_xrto( 0f );
        nb.set_yrto( 0f );
        nb.set_zrto( 0f );

        // [default scale]
        nb.set_s_to( 1f );

        // [saved location]
      //nb.set_x_to( (int)nb.get_saved_x() );
        nb.set_x_to( marker_float_get_unlocked_x() );
        nb.set_y_to( (int)nb.get_saved_y() );

//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "SET TO ATTITUDE AND LOCATION:");
//*MAGNET*/Settings.MOC(TAG_MAGNET, ""    , nb.description_to());
        //}}}
        // START ANIMATION {{{
      //swing_Magnet.animateOvershoot( 2*SWING_MAGNET_TRACK_DURATION ); // TOO MUCH
        swing_Magnet.animate         (   SWING_MAGNET_TRACK_DURATION );

        //}}}
    }
    //}}}
    // swing_np_SAVE_FROM_ATTITUDE_AND_LOCATION {{{
    private void swing_np_SAVE_FROM_ATTITUDE_AND_LOCATION(NpButton nb, String caller)
    {
        caller += "->swing_np_SAVE_FROM_ATTITUDE_AND_LOCATION("+nb+")";
//*MAGNET*/Settings.MOC(TAG_MAGNET, caller);

        if((swing_Magnet != null) && swing_Magnet_is_animating())
        {
//*MAGNET*/Settings.MOM(TAG_MAGNET, "CANCELING ANIMATION:");
//*MAGNET*/Settings.MOM(TAG_MAGNET, nb.description      ());
//*MAGNET*/Settings.MOM(TAG_MAGNET, nb.description_saved());

            swing_Magnet_cancel_animation(nb, caller);
        }

//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "SAVING ATTITUDE AND LOCATION OF ["+nb+"]:");
//*MAGNET*/Settings.MOM(TAG_MAGNET, nb.description      ());
//*MAGNET*/Settings.MOM(TAG_MAGNET, nb.description_saved());
        nb.save_from(caller);

//*MAGNET*/Settings.MOM(TAG_MAGNET, "SAVED  ATTITUDE AND LOCATION:");
//*MAGNET*/Settings.MOM(TAG_MAGNET, nb.description_from());

    }
    //}}}
    //}}}
    //}}}
    //* EV4_on_POINTER */
    //{{{
    //{{{
    private          boolean waiting_for_ACTION_POINTER_UP = false;
    private            float distance_on_pointer_down      = 0;

    //}}}
    // on_POINTER_DOWN {{{
    public boolean on_POINTER_DOWN(MotionEvent event)
    {
        // (MARKER) {{{
        String caller = "wvTools.on_POINTER_DOWN";
//*EV1_WV_IN*/Settings.MOC(TAG_EV1_WV_IN, caller);

        if(        (magnetized_marker_np_onDown  == null)
                && (gesture_down_marker_np_to_CB == null)
                && (          expanded_marker_np == null)
                && (marker_magnetized_np_closest == null)
          )
            return false;

        //}}}
        // [gesture_down_marker_np_to_CB] [waiting_for_ACTION_POINTER_UP] [distance_on_pointer_down] {{{
        int x = (int)event.getX(1); // second finger location
        int y = (int)event.getY(1); // second finger location
        NotePane np = marker_get_np_at_XY(x, y);
        if(np != null)
            gesture_down_marker_np_to_CB = np; // [new onDown marker .. touched by second finger]
        distance_on_pointer_down      = fingers_distance(event);        // ... expect finger spreading to magnify
        waiting_for_ACTION_POINTER_UP = true; // reset by (ACTION_POINTER_UP) or (onDown)

//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, ".....distance_on_pointer_down set to ["+distance_on_pointer_down+"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "gesture_down_marker_np_to_CB=["+gesture_down_marker_np_to_CB+"] f(x,y)");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "waiting_for_ACTION_POINTER_UP set to ["+waiting_for_ACTION_POINTER_UP+"]");

        String consumed_by = null;
        //}}}
        // SPREAD [magnetized_marker_np_onDown] {{{
        if     (magnetized_marker_np_onDown != null)
        {
            magnetized_marker_np_onDown.button.setScaleX( 1f );
            magnetized_marker_np_onDown.button.setScaleY( 1f );

            mRTabs.cancel_pending_button_to_magnify(caller);
            marker_spread_start( magnetized_marker_np_onDown );

            consumed_by = "SPREAD [magnetized_marker_np_onDown]";
        }
        //}}}
        // SPREAD [gesture_down_marker_np_to_CB] {{{
        else if(gesture_down_marker_np_to_CB != null)
        {
            gesture_down_marker_np_to_CB.button.setScaleX( 1f );
            gesture_down_marker_np_to_CB.button.setScaleY( 1f );

            mRTabs.cancel_pending_button_to_magnify(caller);
            marker_spread_start( gesture_down_marker_np_to_CB );

            consumed_by = "SPREAD [gesture_down_marker_np_to_CB]";
        }
        //}}}
        // SPREAD [expanded_marker_np] {{{
        else if(expanded_marker_np != null)
        {
            expanded_marker_np.button.setScaleX( 1f );
            expanded_marker_np.button.setScaleY( 1f );

            mRTabs.cancel_pending_button_to_magnify(caller);
            marker_spread_start( expanded_marker_np );

            consumed_by = "SPREAD [expanded_marker_np]";
        }
        //}}}
        // SPREAD [marker_magnetized_np_closest] {{{
        else if(marker_magnetized_np_closest != null)
        {
            marker_magnetized_np_closest.button.setScaleX( 1f );
            marker_magnetized_np_closest.button.setScaleY( 1f );

            mRTabs.cancel_pending_button_to_magnify(caller);
            marker_spread_start( marker_magnetized_np_closest );

            consumed_by = "SPREAD [marker_magnetized_np_closest]";
        }
        //}}}
        // [return consumed_by] {{{
//*EV1_WV_OK*/Settings.MOC(TAG_EV1_WV_OK, caller, "...return (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    // on_POINTER_UP {{{
    public boolean on_POINTER_UP(MotionEvent event)
    {
        // (MARKER) {{{
        String caller = "wvTools.on_POINTER_UP";
//*EV1_WV_IN*/Settings.MOM(TAG_EV1_WV_IN, caller+": expanded_marker_np ["+expanded_marker_np+"]");

        waiting_for_ACTION_POINTER_UP = false;

        if(        (          expanded_marker_np == null)
                && (marker_magnetized_np_closest == null))
            return false;

        //}}}
        // UNSPREAD [marker_spread_stop] {{{
        marker_spread_stop(caller);

        //}}}
        // UNEXPAND [marker_unexpand_np] {{{
        if(expanded_marker_np != null)
            marker_unexpand_np(caller);

        //}}}
        // [return consumed_by] {{{
//*EV1_WV_OK*/Settings.MOC(TAG_EV1_WV_OK, caller, "...return true");
        return true;
        //}}}
    }
    //}}}
    // is_waiting_for_ACTION_POINTER_UP {{{
    public boolean is_waiting_for_ACTION_POINTER_UP()
    {
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "is_waiting_for_ACTION_POINTER_UP: ...return waiting_for_ACTION_POINTER_UP=["+waiting_for_ACTION_POINTER_UP+"]");
        return waiting_for_ACTION_POINTER_UP;
    }
    //}}}
    // wvTools_pointer_onDown {{{
    private void wvTools_pointer_onDown()
    {
        waiting_for_ACTION_POINTER_UP = false;
        distance_on_pointer_down      =     0;
    }
    //}}}
    //}}}
    //* EV5_onScale */
    /// -----------
    //* EV6_on_UP */
    //{{{
    //? on_UP {{{
    public boolean on_UP(MotionEvent event)
    {
        //{{{
        String caller = "wvTools.on_UP";
//*EV1_WV_IN*/Settings.MOC(TAG_EV1_WV_IN, caller);

        int x = (int)event.getRawX();
        int y = (int)event.getRawY();

//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, ".................has_moved_since_onDown...[  MOVE]=["+ has_moved_since_onDown               +"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "........................gesture_down_wv...[    WV]=["+ get_view_name( gesture_down_wv )     +"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "........................gesture_down_sb...[    SB]=["+ get_view_name( gesture_down_sb )     +"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, ".....sb_CB_has_been_called_since_onDown...[    SB]=["+ sb_CB_has_been_called_since_onDown   +"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "....gesture_down_marker_np_to_CB..........[MARKER]=["+ gesture_down_marker_np_to_CB         +"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "........floating_marker_np................[MARKER]=["+ floating_marker_np                   +"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "...last_expanded_marker_np_since_onDown...[MARKER]=["+ last_expanded_marker_np_since_onDown +"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, ".............marker_get_np_at_XY..........[MARKER]=["+ marker_get_np_at_XY(x,y)             +"]");

        String consumed_by = null;
        //}}}

        if     (  on_UP_1_SB_CLICK     (x, y) ) consumed_by = "on_UP_1_SB_CLICK"     ; // (sb_CB)
        else if(  on_UP_2_MARKER_CLICK (x, y) ) consumed_by = "on_UP_2_MARKER_CLICK" ; // [marker_CB]
        else if(  on_UP_3_MARGIN_CLICK (    ) ) consumed_by = "on_UP_3_MARGIN_CLICK" ; // (GUI CYCLE)
        else if(  on_UP_4_FLOAT_DRAGGED(x, y) ) consumed_by = "on_UP_4_FLOAT_DRAGGED"; // (lock unlock swap park)
        else if(  on_UP_5_FLOAT_SWING  (    ) ) consumed_by = "on_UP_5_FLOAT_SWING"  ; // ...........SWING: (swing_Magnet animation)
        else if(  on_UP_6_MAGNETIZE_UP (x, y) ) consumed_by = "on_UP_6_MAGNETIZE_UP" ; // MARKER MAGNETIZE: (check comtinue stop)
        else if(  on_UP_7_FS_SEARCH_UP (    ) ) consumed_by = "on_UP_7_FS_SEARCH_UP" ; // .......FS_SEARCH: (fs_search_park)
        else if(  on_UP_8_SB_UP        (x, y) ) consumed_by = "on_UP_8_SB_UP"        ; // .......SCROLLBAR: (adjust anim page follow)
        else if(  on_UP_9_CLEAR        (    ) ) consumed_by = "on_UP_9_CLEAR"        ; // .........MARKERS: (redisplay temporarily hidden markers)

        marker_unexpand_np( caller );
        if(floating_marker_np != null) visualize_DRAG_rect ();
        else                           visualize_CLEAR_rect();

        // [return consumed_by] {{{
//*EV1_WV_OK*/Settings.MOC(TAG_EV1_WV_OK, caller, "...return (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    //? on_UP_1_SB_CLICK {{{
    private boolean on_UP_1_SB_CLICK(int x, int y)
    {
        // (gesture_down_sb CLICK) {{{
        if( gesture_down_sb == null                ) return false;
        if( has_moved_since_onDown                 ) return false;
        if( !is_sb_aroundXY(gesture_down_sb, x, y) ) return false;

        String caller = "wvTools.on_UP_1_SB_CLICK";
        //}}}
        // [sb_CB] {{{
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, caller+": ...calling sb_CB");
        sb_CB(gesture_down_sb, caller);

//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "...return true");
        return true;
        //}}}
    }
    //}}}
    //? on_UP_2_MARKER_CLICK {{{
    private boolean on_UP_2_MARKER_CLICK(int x, int y)
    {
        // (is_on_UP_CLICK_DURATION) (gesture_down_marker_np_to_CB) (marker_has_spread_since_onDown) {{{
        String caller = "wvTools.on_UP_2_MARKER_CLICK";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller);

//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "...................has_moved_since_onDown=["+ has_moved_since_onDown           +"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "...........mRTabs.is_on_UP_CLICK_DURATION=["+ mRTabs.is_on_UP_CLICK_DURATION() +"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, ".............gesture_down_marker_np_to_CB=["+ gesture_down_marker_np_to_CB     +"]");
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "...........marker_has_spread_since_onDown=["+ marker_has_spread_since_onDown   +"]");
        if( has_moved_since_onDown              ) return false; // .. (HAS  MOVED)
        if(!mRTabs.is_on_UP_CLICK_DURATION()    ) return false; // .. (LONG CLICK)
        if( gesture_down_marker_np_to_CB == null) return false; // as from (onDown_6_MARKER) or (on_POINTER_DOWN)
        if( marker_has_spread_since_onDown      ) return false; // [spread] excluded

///EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, ".......................expanded_marker_np=["+ expanded_marker_np             +"]");
///EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, ".............marker_magnetized_START_DONE=["+ marker_magnetized_START_DONE   +"]");
///EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "...............marker_magnetized_np_count=["+ marker_magnetized_np_count     +"]");
///EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "...is_floating_marker(expanded_marker_np)=["+ is_floating_marker(expanded_marker_np) +"]");
        //if( expanded_marker_np == null               ) return false; // XXX [expanded_marker_np] only
        //if( marker_magnetized_START_DONE             ) return false; // XXX ACTION_UP after magnetize layout gesture
        //if( marker_magnetized_np_count >= 0          ) return false; // XXX zoomed-in markers .. (may be clicked)
        //if( is_floating_marker( expanded_marker_np ) ) return false; // XXX ACTION_UP may mask FLING

//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "MARKER CB:");

        //}}}
        // [marker_CB] {{{
        NotePane np
            = (gesture_down_marker_np_to_CB != null)
            ?  gesture_down_marker_np_to_CB
            :  expanded_marker_np;

        if(np != null)
            marker_CB( np );

        //}}}
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "...return true");
        return true;
    }
    //}}}
    //? on_UP_3_MARGIN_CLICK {{{
    private boolean on_UP_3_MARGIN_CLICK()
    {
        // (has_moved_since_onDown) (onDown_URDL) {{{
        if(has_moved_since_onDown ) return false;
        if(onDown_URDL == null    ) return false;

      //if(        (mRTabs.gesture_down_SomeView_atXY != mRTabs.fs_webView )
      //        && (mRTabs.gesture_down_SomeView_atXY != mRTabs.fs_webView2)
      //        && (mRTabs.gesture_down_SomeView_atXY != mRTabs.fs_webView3)) return false;

        String caller = "wvTools.on_UP_3_MARGIN_CLICK";
        //}}}
        // MARGIN CLIC {{{
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, "", "GUI CYCLE: (MARGIN CLICKED)");

        String consumed_by = gui_cycle(caller);

        /* clear [wants_to_steal_events] .. f([onDown_URDL] not consumed) */
        if(consumed_by == null)
        {
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, "", "onDown_URDL=["+onDown_URDL+"] not consumed: CALLING reconsider_stealing_events()");
            onDown_URDL = null;
            reconsider_stealing_events();
        }
        //}}}
        // [return consumed_by] {{{
//*EV2_WV_CB*/if(consumed_by != null) Settings.MOC(TAG_EV2_WV_CB, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    //? on_UP_4_FLOAT_DRAGGED {{{
    private boolean on_UP_4_FLOAT_DRAGGED(int x, int y)
    {
        // ON ACTION_UP
        // ...drag floating_marker_np
        // ...only when touched at ACTION_DOWN
        // ...and finger is still on it

        // (gesture_down_marker_np_to_CB) (has_moved_since_onDown) {{{
        if( !has_moved_since_onDown                                ) return false; // marker_CB only
        if(gesture_down_marker_np_to_CB == null                    ) return false;
      //if(gesture_down_marker_np_to_CB != floating_marker_np      ) return false;

        String caller = "wvTools.on_UP_4_FLOAT_DRAGGED";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller, "...gesture_down_marker_np_to_CB=["+ gesture_down_marker_np_to_CB +"]");

        String consumed_by = null;
        //}}}
        // DRAGGED .. (FINGER STILL ON TOUCHED MARKER) {{{
        if(gesture_down_marker_np_to_CB == marker_get_np_at_XY(x,y))
        {
            boolean  back_lock = is_drag_to_margin( x );

            consumed_by = marker_float_DRAGLOCK(gesture_down_marker_np_to_CB, back_lock, caller);
        }
        //}}}
        // TO BE HANDLED BY FLING .. (FINGER NOT ON MARKER) {{{
        else {
            consumed_by = "TO BE HANDLED BY FLING";
        }
        //}}}
        // [return consumed_by] {{{
//*EV2_WV_CB*/if(consumed_by != null) Settings.MOC(TAG_EV2_WV_CB, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    //+ on_UP_5_FLOAT_SWING {{{
    private boolean on_UP_5_FLOAT_SWING()
    {
        //    (floating_marker_np) {{{
        if(    floating_marker_np       == null                                 ) return false;
        if(gesture_down_marker_np_to_CB == null                                 ) return false;
        if(gesture_down_marker_np_to_CB != floating_marker_np                   ) return false;
        if(               (swing_Magnet != null ) && swing_Magnet.is_animating()) return false;

        String caller = "on_UP_5_FLOAT_SWING";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller, "...floating_marker_np=["+floating_marker_np+"]");

        NpButton nb = floating_marker_np.button;
        //}}}
        // [swing_Magnet] {{{

//*MAGNET*/Settings.MOM(TAG_MAGNET, nb.description      ());
//*MAGNET*/Settings.MOM(TAG_MAGNET, nb.description_saved());

//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "SAVING ATTITUDE AND LOCATION OF ["+nb+"]:");
//*MAGNET*/Settings.MOM(TAG_MAGNET, nb.description      ());
//*MAGNET*/Settings.MOM(TAG_MAGNET, nb.description_saved());
        nb.save_from(caller);
//*MAGNET*/Settings.MOM(TAG_MAGNET, nb.description_from());

        nb.set_x_to( marker_float_get_unlocked_x() );
        nb.set_yrto( 0f );

//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "SET TO ATTITUDE AND LOCATION:");
//*MAGNET*/Settings.MOC(TAG_MAGNET, ""    , nb.description_to());

        swing_Magnet.animateOvershoot(   SWING_MAGNET_TRACK_DURATION ); // may cancel a running animation

        //}}}
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "...return true");
        return true;
    }
    //}}}
    //+ on_UP_6_MAGNETIZE_UP {{{
    private boolean on_UP_6_MAGNETIZE_UP(int x, int y)
    {
        // (marker_magnetized_np_count) {{{
    //  if( !may_resume_magnetize(caller)    ) return false;
        if(marker_magnetized_np_count <  1   ) return false;
        if(           gesture_down_sb != null) return false;

        String caller = "wvTools.on_UP_6_MAGNETIZE_UP";
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, ".............has_moved_since_onDown=["+ has_moved_since_onDown           +"]");
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "...mRTabs.is_on_UP_CLICK_DURATION()=["+ mRTabs.is_on_UP_CLICK_DURATION() +"]");
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "..........is_fs_search_in_margins()=["+ is_fs_search_in_margins()        +"]");
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, ".......gesture_down_marker_np_to_CB=["+ gesture_down_marker_np_to_CB     +"]");

        String consumed_by = null;
        //}}}
        // MARKER MAGNETIZE: PAUSE .. (FS_SEARCH IN MARGINS) {{{
        if( is_fs_search_in_margins() )
        {
            consumed_by = "FS_SEARCH IN MARGINS";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller, "MARKER MAGNETIZE: PAUSE .. ("+consumed_by+")");
            marker_magnetize_pause(gesture_down_wv, caller);
        }
        //}}}
        // MARKER MAGNETIZE: PAUSE .. (CLICKED ON BACKGROUND) {{{
        else if(   !has_moved_since_onDown
                &&  mRTabs.is_on_UP_CLICK_DURATION()
                && (gesture_down_marker_np_to_CB == null))
        {
            consumed_by = "CLICKED ON BACKGROUND";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller, "MARKER MAGNETIZE: PAUSE .. ("+consumed_by+")");
            marker_magnetize_pause(gesture_down_wv, caller);
        }
        //}}}
//        // (CLICKED ON MARKER) {{{
//        else if(gesture_down_marker_np_to_CB == marker_get_gesture_down_marker_np_atXY(x, y))
//        {
//            consumed_by = "CLICKED ON MARKER";
//        }
//        //}}}
        // MARKER MAGNETIZE: PAUSE (NO MARKERS) {{{
        else if(marker_magnetized_np_count < 1)
        {
            consumed_by = "NO MARKERS";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller, "MARKER MAGNETIZE: PAUSE .. ("+consumed_by+") ("+marker_magnetized_np_count+" MARKERS)");
        }
        //}}}
        // 1/2 MARKER MAGNETIZE STOP .. (consumed_by) {{{
        if(consumed_by != null)
        {
            if( !property_get( WV_TOOL_silent ) ) activity_play_sound_ding( caller );
        }
        //}}}
        // 2/2 MARKER MAGNETIZE CHECK AND CONTINUE {{{
        else {
            mm_resume(caller);
        }
        //}}}
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "...return true");
        return true;
    }
    //}}}
    //+ on_UP_7_FS_SEARCH_UP {{{
    private boolean on_UP_7_FS_SEARCH_UP()
    {
        // (may_resume_magnetize) (is_clamping_button fs_search) {{{
        String caller = "wvTools.on_UP_7_FS_SEARCH_UP";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller);

//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "...............is_clamping_button(fs_search)=["+ is_clamping_button(fs_search)                      +"]");
//EV2_WV_CB//Settings.MOM(TAG_EV2_WV_CB, "......................may_resume_magnetize()=["+ may_resume_magnetize(caller)                       +"]");
//EV2_WV_CB//Settings.MOM(TAG_EV2_WV_CB, "...fs_search_can_select_webview_on_ACTION_UP=["+ mRTabs.fs_search_can_select_webview_on_ACTION_UP() +"]");

        if( may_resume_magnetize(caller)                       ) return false; // keep [fs_search] on screen (while magnetizing)
        if(!is_clamping_button( fs_search )                    ) return false; // [fs_search]  not on screen (not currently used)
      //if(!mRTabs.fs_search_can_select_webview_on_ACTION_UP() ) return false; // 

        //}}}
        // [mClamp_standby] {{{
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "............calling mClamp_standby");

        mClamp_standby(caller);
        //}}}
        // [fs_search_park] {{{

//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "............calling fs_search_park");
        fs_search_park(caller);

        //}}}
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "...return true");
        return true;
    }
    //}}}
    //+ on_UP_8_SB_UP {{{
    private boolean on_UP_8_SB_UP(int x, int y)
    {
        // (gesture_down_sb.action_down_in_margin) {{{
        if(  gesture_down_sb == null              ) return false;
        if( !gesture_down_sb.action_down_in_margin) return false;

        //}}}
        // SCROLLBAR ADJUST: CANCEL BLUEPRINT DISPLAY MODE {{{
        String caller = "wvTools.on_UP_8_SB_UP";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller);

        gesture_down_sb.action_down_in_margin = false;
        gesture_down_sb.invalidate();
        if(gesture_down_wv != null) gesture_down_wv.setAlpha( 1f ); // (undimm webview after adjusting scrollbar layout)

        //}}}
        // SCROLLBAR ANIM: CANCEL {{{
        sb_anim_onUp();

        //}}}
        // SCROLLBAR ANIM: ADJUST PAGE DESTINATION {{{
        if( scroll_wv_can_PageUp_PageDown() )
        {
            track_finger_animation_toY = y; // last anim hotspot

            boolean page_up = false;
            int   distanceY = 0;
            scroll_wv_anim_PageUpDown(gesture_down_wv, x, y, page_up, distanceY);
        }
        //}}}
//        // SCROLLBAR ANIM: ADJUST FOLLOW DESTINATION {{{
//        else if( scroll_wv_can_follow_finger() )
//        {
//            int              distanceY = 0; // [distanceY] .. (not used in this case anyway)
//            track_finger_animation_toY = y; // last anim hotspot
//            onScroll_3_sb_follow_finger(distanceY, caller);
//        }
//        //}}}
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "...return true");
        return true;
    }
    //}}}
    //+ on_UP_9_CLEAR {{{
    private boolean on_UP_9_CLEAR()
    {
        // [gesture_down_wv.setAlpha] {{{
        String caller = "wvTools.on_UP_9_CLEAR";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller);

        if((gesture_down_wv != null) && (gesture_down_wv.getAlpha() != 1.0f)) gesture_down_wv.setAlpha( 1.0f );
        if((gesture_down_sb != null) && (gesture_down_sb.getAlpha() != 1.0f)) gesture_down_sb.setAlpha( 1.0f );

        //}}}
        // [marker_redisplay_temporarily_hidden_marks] {{{
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, caller+": ...calling marker_redisplay_temporarily_hidden_marks");

        marker_redisplay_temporarily_hidden_marks(caller);
        //}}}
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "...return false");
        return false;
    }
    //}}}
    // is_drag_to_margin {{{
    private boolean is_drag_to_margin(int x)
    {
        int             dx = mRTabs.get_moved_dx(x);
        boolean sb_at_left = is_sb_at_left();
        return   ((dx < 0) && !sb_at_left)
            ||   ((dx > 0) &&  sb_at_left);
    }
    //}}}
    //}}}
    //* EV7_onFling */
    //{{{
    //? onFling {{{
    public  boolean onFling(boolean right_fling, boolean left_fling, boolean up_fling, boolean down_fling, float velocityX, float velocityY)
    {
// TRACE {{{
        String caller = "wvTools.onFling";
//*EV1_WV_IN*/Settings.MOC(TAG_EV1_WV_IN, caller);

//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, ".............................left_fling=["+ left_fling                             +"]");
//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, "............................right_fling=["+ right_fling                            +"]");

//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, "............................onDown_URDL=["+ onDown_URDL                            +"]");

//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, "........................gesture_down_wv=["+ get_view_name(gesture_down_wv )        +"]");
//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, "........................gesture_down_sb=["+ get_view_name(gesture_down_sb)         +"]");

//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, "...last_expanded_marker_np_since_onDown=["+ last_expanded_marker_np_since_onDown   +"]");
//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, ".....................expanded_marker_np=["+ expanded_marker_np                     +"]");

//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, ".............marker_magnetized_np_count=["+ marker_magnetized_np_count             +"]");
//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, "............magnetized_marker_np_onDown=["+ magnetized_marker_np_onDown            +"]");
//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, "...........gesture_down_marker_np_to_CB=["+ gesture_down_marker_np_to_CB           +"]");
//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, "...........marker_magnetized_START_DONE=["+ marker_magnetized_START_DONE           +"]");

//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, "...................is_floating_marker()=["+ is_floating_marker()                   +"]");
//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, ".....................floating_marker_np=["+ floating_marker_np                     +"]");

//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, "....................sb_anim_interrupted=["+ sb_anim_interrupted                    +"]");
//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, "...........sb_follow_finger_anim_called=["+ sb_follow_finger_anim_called           +"]");

//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, ".....................WV_TOOL_MDRAG_LOCK=["+ property_get(WV_TOOL_MDRAG_LOCK)       +"]");

        String      consumed_by = null;
//}}}

      //if     ( onFling_1_MARKER_MAGNETIZE (right_fling, left_fling, velocityX, velocityY) ) consumed_by = "onFling_1_MARKER_MAGNETIZE";
        if     ( onFling_2_MARKER_FLOAT     (right_fling, left_fling                      ) ) consumed_by = "onFling_2_MARKER_FLOAT";
        else if( onFling_3_SCROLL_SWAP_SIDES(right_fling, left_fling           , velocityX) ) consumed_by = "onFling_3_SCROLL_SWAP_SIDES";
        else if( onFling_4_SCROLL_ANIM      (   up_fling, down_fling           , velocityY) ) consumed_by = "onFling_4_SCROLL_ANIM";

        // [return consumed_by] {{{
//*EV1_WV_OK*/Settings.MOC(TAG_EV1_WV_OK, caller, "...return (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
//    //? onFling_1_MARKER_MAGNETIZE {{{
//    private boolean onFling_1_MARKER_MAGNETIZE(boolean right_fling, boolean left_fling, float velocityX, float velocityY)
//    {
//        // (fs_search clamping) (marker_magnetized_np_count) {{{
//        if( !is_clamping_button(fs_search) ) return false;
//        if( marker_magnetized_np_count < 1 ) return false;
//
//        String caller = "wvTools.onFling_1_MARKER_MAGNETIZE";
//
//        String consumed_by = null;
//        //}}}
//        // (MARKERS-FLING TO MARGINS) {{{
//        if( is_fling_to_margin(left_fling) )
//        {
////*EV7_WV_FL*/Settings.MOC(TAG_EV7_WV_FL, caller, "MARKERS-FLING TO MARGINS ("+marker_magnetized_np_count+" MARKERS): ...calling fs_search_onFling_MAGNETIZE");
//
//            fs_search_onFling_MAGNETIZE(velocityX, velocityY);
//            consumed_by = "fs_search_onFling_MAGNETIZE";
//        }
//        //}}}
//        // MARKER MAGNETIZE: STOP .. (NOT A MARKERS-FLING TO MARGINS) {{{
//        else {
////*EV7_WV_FL*/Settings.MOC(TAG_EV7_WV_FL, caller, "MARKER MAGNETIZE: STOP .. (NOT A MARKERS-FLING TO MARGINS)");
//            marker_magnetize_stop(gesture_down_wv, caller);
//            consumed_by = "marker_magnetize_stop";
//        }
//        //}}}
//        // [return consumed_by] {{{
////*EV7_WV_FL*/if(consumed_by != null) Settings.MOC(TAG_EV7_WV_FL, caller, "...return true (consumed_by="+consumed_by+")");
//        return (consumed_by != null);
//        //}}}
//    }
//    //}}}
    //? onFling_2_MARKER_FLOAT {{{
    private boolean onFling_2_MARKER_FLOAT(boolean right_fling, boolean left_fling)
    {
        // (LEFT RIGHT MARKER) {{{
        if( !left_fling && !right_fling                      ) return false;
      //if(          floating_marker_np == null              ) return false;
      //if(gesture_down_marker_np_to_CB != floating_marker_np) return false;

        NotePane lockable_marker_np
            =     (  gesture_down_marker_np_to_CB        != null) ?  gesture_down_marker_np_to_CB        // newly touched by user
            :     (      floating_marker_np              != null) ?      floating_marker_np              // or currently floating
            : last_expanded_marker_np_since_onDown;
        if(lockable_marker_np == null) return false;

        String caller = "wvTools.onFling_2_MARKER_FLOAT";
//*EV7_WV_FL*/Settings.MOC(TAG_EV7_WV_FL, caller, "...lockable_marker_np=["+ lockable_marker_np +"]");
        //}}}
        // (UNLOCK BACK NEW) {{{
        boolean  back_lock = is_fling_to_margin( left_fling );

        String consumed_by = marker_float_DRAGLOCK(lockable_marker_np, back_lock, caller);
        //}}}
        // [return consumed_by] {{{
//*EV7_WV_FL*/if(consumed_by != null) Settings.MOC(TAG_EV7_WV_FL, caller, "...return true (consumed_by="+consumed_by+")");
        return (consumed_by != null);
        //}}}
    }
    //}}}
    //? onFling_3_SCROLL_SWAP_SIDES {{{
    private boolean onFling_3_SCROLL_SWAP_SIDES(boolean right_fling, boolean left_fling, float velocityX)
    {
        // (sbX) (right_fling) (left_fling) (fling_from_margin_or_sbX) {{{ 
        if(!right_fling && !left_fling)                            return false;
        boolean fling_from_margin_or_sbX
            =  (onDown_URDL     == MARGIN_L)
            || (onDown_URDL     == MARGIN_R)
            || (gesture_down_sb != null    )
            ;
        if( !fling_from_margin_or_sbX )                            return false;

        if(gesture_down_wv == null)                                return false;
        NpButton sbX
            = (gesture_down_sb  != null)
            ?  gesture_down_sb
            :  get_sb_for_wv_or_sb(gesture_down_wv);
        if(sbX == null)                                            return false;

        String caller = "wvTools.onFling_3_SCROLL_SWAP_SIDES(left_fling=["+left_fling+"])";
//*EV7_WV_FL*/Settings.MOC(TAG_EV7_WV_FL, caller);
//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, "...gesture_down_wv=["+ get_view_name(gesture_down_wv) +"]");
//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, "...gesture_down_sb=["+ get_view_name(gesture_down_sb) +"]");
//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, "...............sbX=["+ get_view_name(sbX)             +"]");
        // }}}
        // HIDE TOOLS {{{
        hide_tools(caller);

        sb_tools_hide(caller);
        // }}}
        // SCROLLBAR SIDE {{{
        boolean at_left
            =   (gesture_down_sb != null)
            ?    left_fling    // (scrollbar fling) change sb side
            :    !sbX.at_left; // (   margin fling) swap sides
//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, "at_left=["+at_left+"]");

      //if(sbX.at_left != at_left) marker_hide_pinned_markers(caller);
        sbX.at_left = at_left;

        // }}}
        // SCROLLBAR SHAPE {{{
        sbX.set_shape( NotePane.Get_current_shape() );

        int shrink = Math.min(Math.abs((int)velocityX/200), Settings.SCROLLBAR_SHRINK_MAX);

 /*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, ".........velocityX=["+ velocityX                      +"]");
 /*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, "............shrink=["+ shrink                         +"]");

        sbX.scroll_nb_shrink_to_shape( shrink );

        //mRTabs.handler.re_post( hr_reshape_sbX );
        // }}}
        // CLAMP SCROLLBAR {{{
        int wv_w =      gesture_down_wv.getWidth();
        int wv_x = (int)gesture_down_wv.getX    ();
        int sb_w =      Settings.TOOL_BADGE_SIZE;
        int toX
            = at_left
            ?  wv_x                // go left
            :  wv_x + wv_w - sb_w; // go right
        int toY
            = (int)sbX.getY();

        sbX_np.set_button( sbX );

        mClamp_np_drop_toXY(sbX_np, toX, toY);

        //}}}
        // [return consumed_by] {{{
//*EV7_WV_FL*/ Settings.MON(TAG_EV7_WV_FL, caller, "...return true");
        return true;
        //}}}
    }
    //}}}
    //? onFling_4_SCROLL_ANIM {{{
    private boolean onFling_4_SCROLL_ANIM(boolean up_fling, boolean down_fling, float velocityY)
    {
        // (onDown_URDL) (scroll_wv_anim_is_running) (up_fling) (down_fling) {{{
        if( !up_fling && !down_fling)                               return false;

        if( (onDown_URDL != MARGIN_L) && (onDown_URDL != MARGIN_R)) return false;

        boolean scroll_wv_anim_is_running = (scroll_wv_AnimatorSet != null);
        if( scroll_wv_anim_is_running                             ) return false;

        String caller = "wvTools.onFling_4_SCROLL_ANIM";
//*EV7_WV_FL*/Settings.MOC(TAG_EV7_WV_FL, caller);
        //}}}
        // (sbX) (from_opposite_margin) {{{
        NpButton sbX
            = (gesture_down_sb != null)
            ?  gesture_down_sb
            :  get_sb_for_wv_or_sb(gesture_down_wv);

        boolean from_opposite_margin
            =   sbX.at_left && (onDown_URDL == MARGIN_R)
            || !sbX.at_left && (onDown_URDL == MARGIN_L)
            ;

//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, "...from_opposite_margin=["+from_opposite_margin+"]");
        //}}}
        // HIDE TOOLS {{{
        hide_tools(caller);

        //}}}
        // HIDE MARKERS {{{
        if( !property_get_WV_TOOL_MARK_LOCK )
        {
            marker_hide_pinned_markers(caller);

        // TEMPORARY HIDE MARKERS .. f(webview scroll)
            marker_set_someone_has_called_hide_marks(caller);
        }
        //}}}
        // FREEZE WEBVIEW (UP-STYLE) {{{
        int          toY = -1;
        float tilt_angle = 5f;
        int   last_pageY = gesture_down_wv.computeVerticalScrollRange() - gesture_down_wv.getHeight();
        if( up_fling )
        {
            caller += "[up_fling]";
            toY = (gesture_down_sb != null)
                ? 0                          // [   SCROLLBAR UP  ] -> [CONTENT DOWN]
                : last_pageY                 // [EMPTY MARGIN UP  ] -> [CONTENT   UP]
                ;
//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, "up_fling: toY=["+toY+"]");

            freeze_wv_start(gesture_down_wv, from_opposite_margin ? FREEZE_TILT_UP_FLING : FREEZE_FLING_UP);
        }
        //}}}
        // FREEZE WEBVIEW (DOWN STYLE) {{{
        else if(down_fling)
        {
            caller += "[down_fling]";
            toY = (gesture_down_sb != null)
                ? last_pageY                 // [   SCROLLBAR DOWN] -> [CONTENT   UP]
                : 0                          // [EMPTY MARGIN DOWN] -> [CONTENT   UP]
                ;
//*EV7_WV_FL*/Settings.MOM(TAG_EV7_WV_FL, "down_fling: toY=["+toY+"]");

            freeze_wv_start(gesture_down_wv, from_opposite_margin ? FREEZE_TILT_DOWN_FLING : FREEZE_FLING_DOWN);
        }
        //}}}
        // ANIM SCROLL {{{
        long duration = (long)(1000 * (float)Math.abs(toY - gesture_down_wv.getScrollY()) / Math.abs(velocityY));
        duration     *= FLING_VELOCITY_SLOWING_FX;
        duration      = Math.max(duration, 200);
        scroll_wv_anim_toY(gesture_down_wv, toY, duration);

        //}}}
        // VISUALIZE FLING UP OR DOWN  {{{
        sb_show_FLING_UP_DOWN( up_fling );

        //}}}
        // [return consumed_by] {{{
//*EV7_WV_FL*/Settings.MON(TAG_EV7_WV_FL, caller, "...return true");
        return true;
        //}}}
    }
    //}}}
    // is_fling_to_margin {{{
    private boolean is_fling_to_margin(boolean left_fling)
    {
        return left_fling && !is_sb_at_left();
    }
    //}}}
    //}}}
    //}}}
    /** EXPAND */
    //{{{
    //{{{
    private NotePane      expanded_marker_np              = null;
    private NotePane last_expanded_marker_np_since_onDown =  null;
    private  boolean marker_has_spread_since_onDown       = false;
    private  boolean marker_has_expanded_since_onDown     = false;

    //}}}
    // wvTools_expanded_marker_onDown {{{
    // {{{
    private     int  marker_expand_x_max              =     0;
    private     int  marker_expand_x_min              =     0;
    private  String  last_marked_text_since_onDown    = null;

    // }}}
    private void wvTools_expanded_marker_onDown()
    {
//*EXPAND*/Settings.MOM(TAG_EXPAND, "wvTools_expanded_marker_onDown");
        set_expanded_marker_np( null );
        last_expanded_marker_np_since_onDown =  null;
        last_marked_text_since_onDown        =  null;
        marker_expand_x_max                  =     0;
        marker_expand_x_min                  =     0;
        marker_has_expanded_since_onDown     = false;
        marker_has_spread_since_onDown       = false;
    }
//}}}
    // marker_expand_np {{{
    private void marker_expand_np(RTabs.MWebView wv, NotePane marker_np)
    {
        // SAME AS CURRENT [expanded_marker_np] {{{
        if(marker_np == expanded_marker_np) return;

//*EXPAND*/Settings.MOM(TAG_EXPAND, "marker_expand_np(wv, "+marker_np+")");
        // }}}
        // RELEASE CURRENT [expanded_marker_np] {{{
        if(expanded_marker_np != null)
        {
            marker_unexpand_np("marker_unexpand_np.RELEASE_CURRENT");
        }
        // }}}
        // CHANGE CURRENT {{{
        set_expanded_marker_np( marker_np );

        //}}}
        // LAYOUT CURRENT {{{
        if(marker_np != null)
            marker_expand_np_layout(wv, marker_np);

        //}}}
        // SET [marker_has_expanded_since_onDown] {{{
        marker_has_expanded_since_onDown = true; // reset by onDown

        //}}}
    }
    //}}}
    // marker_unexpand_np {{{
    private void marker_unexpand_np(String caller)
    {
        if(expanded_marker_np == null) return;

        caller = caller+"->marker_unexpand_np";
//*EXPAND*/Settings.MOM(TAG_EXPAND, caller+": expanded_marker_np=["+expanded_marker_np+"]");

        marker_unexpand( expanded_marker_np );
    }
//}}}
    // set_expanded_marker_np {{{
    private void set_expanded_marker_np(NotePane marker_np)
    {
         expanded_marker_np  = marker_np;
        // keep last one ready for onFling
        if(marker_np != null)
            last_expanded_marker_np_since_onDown = marker_np;
    }
    //}}}
    // marker_expand_np_layout {{{
    private void marker_expand_np_layout(RTabs.MWebView wv, NotePane marker_np)
    {
        // SET CURRENT [expanded_marker_np] {{{
//*EXPAND*/Settings.MOM(TAG_EXPAND, "marker_expand_np_layout(wv, "+get_view_name( marker_np.button )+")");

        // }}}
        // CONTAINER {{{
        container_addView( marker_np.button );

        //}}}
        // TRANSLATE {{{
        if( !is_floating_marker( marker_np ) )
            marker_translate( marker_np );

        // }}}
        // VISUALIZE SLIDE RECT [marker_np](unexpanded)->[expanded_marker_np] {{{
        visualize_SLIDE_rect(marker_np, "marker_expand_np_layout");

        //}}}
         // 1/2 CLAMP (MDRAG) {{{
        if( property_get( WV_TOOL_MDRAG_LOCK ) )
        {
            int toX = (int)marker_np.button.getTranslationX();                                   //onExpand_x;
            int toY = (int)marker_np.button.getTranslationY() + Settings.FINGER_CLUTTER_OFFSET;  //onExpand_y
            mClamp_np_drop_toXY(marker_np, toX, toY); // [on current Y]
        }
        //}}}
        // 2/2 EXPAND {{{
        //else {
            float scale_grow = Settings.MARK_SCALE_GROW;
            boolean  at_left = !is_sb_at_left();
            marker_expand(marker_np, scale_grow, at_left);

        //}
        //}}}
//{{{
//*EXPAND*/ Settings.MOM(TAG_EXPAND, "marker_expand_np_layout("+get_view_name( marker_np.button )+")");                                          //TAG_EXPAND
//*EXPAND*/ ViewGroup.MarginLayoutParams marker_np_mlp = (ViewGroup.MarginLayoutParams)marker_np.button.getLayoutParams();                                 //TAG_EXPAND
//*EXPAND*/ System.err.println(     "marker_np=["+marker_np.toString()+"]\n"                                                                               //TAG_EXPAND
//*EXPAND*/         + String.format("[%20s %4d] [%-4d %-20s]\n",      "getX()", (int)marker_np.button.     getX(), marker_np_mlp.leftMargin, "leftMargin") //TAG_EXPAND
//*EXPAND*/         + String.format("[%20s %4d] [%-4d %-20s]\n",      "getY()", (int)marker_np.button.     getY(), marker_np_mlp.topMargin , "topMargin ") //TAG_EXPAND
//*EXPAND*/         + String.format("[%20s %4d] [%-4d %-20s]\n",  "getWidth()",      marker_np.button. getWidth(), marker_np_mlp.width     , "width     ") //TAG_EXPAND
//*EXPAND*/         + String.format("[%20s %4d] [%-4d %-20s]\n", "getHeight()",      marker_np.button.getHeight(), marker_np_mlp.height    , "height    ") //TAG_EXPAND
//*EXPAND*/         + String.format("[%20s %.2f]\n"            , "getScaleX()"      , marker_np.button.getScaleX()      ) //TAG_EXPAND
//*EXPAND*/         + String.format("[%20s %.2f]\n"            , "getScaleY()"      , marker_np.button.getScaleY()      ) //TAG_EXPAND
//*EXPAND*/         + String.format("[%20s %.2f]\n"            , "getPivotX()"      , marker_np.button.getPivotX()      ) //TAG_EXPAND
//*EXPAND*/         + String.format("[%20s %.2f]\n"            , "getTranslationX()", marker_np.button.getTranslationX()) //TAG_EXPAND
//*EXPAND*/         + String.format("[%20s %.2f]\n"            , "getElevation()"   , marker_np.button.getElevation()   ) //TAG_EXPAND
//*EXPAND*/         + String.format("[%20s %.2f]\n"            , "getAlpha()"       , marker_np.button.getAlpha()       ) //TAG_EXPAND
//*EXPAND*/         );                                                                                                                                     //TAG_EXPAND
//}}}
    }
    //}}}
    // expand_magnetized_marker_np {{{
    private void expand_magnetized_marker_np(NotePane marker_np)
    {
        boolean  at_left = !is_sb_at_left();
        float scale_grow = Settings.MARK_SCALE_GROW;
        marker_expand(marker_np, scale_grow, at_left);
    }
    //}}}
    //}}}
    /** SCROLLBAR (EVENTS) */
    //{{{
    // ... {{{
    private static final     int SCROLL_PAGE_DURATION                = 30000; // SLOW   ONE  PAGE
    private static final     int SCROLL_VIEW_DURATION                =  1000; // QUICK FULL RANGE
    private static final     int SCROLL_RECT_DURATION                =   500; // SCROLL RECT COLOR

    private static final boolean FOLLOW_FINGER_CLAMPED               = false;

    private static final boolean SCROLL_WV_FOLLOW_FINGER             = false;//true;

    private static final     int TRACK_FINGER_DELAY                  = 500; // (enough time to draw a SCOLL_AMOUNT_LINE)
    private static final    long ANIM_SCROLL_PAGE_DURATION           = 500;

  //private static final    long ANIM_FOLLOW_COOLDOWN                = 2*ANIM_SCROLL_PAGE_DURATION;
    private static final    long ANIM_PAGE_COOLDOWN                  = 2*ANIM_SCROLL_PAGE_DURATION;

    private static final    long FIND_PAGE_COOLDOWN                  = 250;

    // processing
    private              boolean sb_follow_finger_anim_called        = false;
  //private              boolean sb_follow_finger_clamped            = false;
    private              boolean sb_has_paged_up_or_down             = false;
    private              boolean sb_has_called_findNext              = false;
    private          AnimatorSet scroll_wv_AnimatorSet               =  null;

    // parameters
    private                  int track_finger_animation_toY          =    -1;

    //  }}}
    //* SB_show */
    // {{{
    private void sb_show_text            (String  text             ) { _set_sb_text(text); }

    private void sb_show_WAITING         (                         ) { _set_sb_text( Settings.SYMBOL_RAISED_HAND                                                                ); }
    private void sb_show_PAGE_UP_DOWN    (boolean page_up_or_down  ) { _set_sb_text(   page_up_or_down ? Settings.SYMBOL_UP_BLACK_TRIANGLE : Settings.SYMBOL_DOWN_BLACK_TRIANGLE); }
    private void sb_show_FLING_UP_DOWN   (boolean scroll_up_or_down) { _set_sb_text( scroll_up_or_down ? Settings.SYMBOL_ARROW_DOWN_PAIR   : Settings.SYMBOL_ARROW_UP_PAIR      ); }

    private void sb_show_TRACKED         (                         ) { _set_sb_text(    "TRACK" ); fs_search_set_colors(Color.YELLOW, Color.RED   ); }
    private void sb_show_REACHED         (                         ) { _set_sb_text(    "REACH" ); fs_search_set_colors(Color.BLACK , Color.YELLOW); }
    private void sb_show_CHECKED         (                         ) { _set_sb_text(    "CHECK" ); fs_search_set_colors(Color.RED   , Color.BLUE  ); }

    private void sb_show_MARKER_SWAP     (String  text             ) { _set_sb_text(      "SWAP ["+text+"]" ); }
    private void sb_show_MARKER_EXPAND   (String  text             ) { _set_sb_text(    "EXPAND ["+text+"]" ); }

    private void sb_show_MARKER_LOCKED   (String  text             ) { _set_sb_text(    "LOCKED ["+text+"]" ); }
    private void sb_show_MARKER_UNLOCKING(String  text             ) { _set_sb_text( "UNLOCKING ["+text+"]" ); }
    private void sb_show_MARKER_UNLOCKED (String  text             ) { _set_sb_text(  "UNLOCKED ["+text+"]" ); }
    private void sb_show_MARKER_BAK_LOCK (String  text             ) { _set_sb_text(  "BAK_LOCK ["+text+"]" ); }
    private void sb_show_MARKER_NEW_LOCK (String  text             ) { _set_sb_text(  "NEW_LOCK ["+text+"]" ); }
    private void sb_show_MARKER_RELOCKING(String  text             ) { _set_sb_text( "RELOCKING ["+text+"]" ); }

    private void sb_show_TRASH_MARKER    (String  text             ) { _set_sb_text(     "TRASH ["+text+"]" ); }
    private void sb_show_TRASH_EMPTY     (                         ) { _set_sb_text(     "TRASH EMPTY"      ); }

    // _set_sb_text {{{
    private void _set_sb_text(String text)
    {
        if(sbX_np        == null) return;

        sbX_np.button
            = (gesture_down_sb != null) ?                        gesture_down_sb
            :                               get_sb_for_wv_or_sb( gesture_down_wv );
        if(sbX_np.button == null) return;

        // [NotePane label]
        sbX_np.text = text;

        // [NPButton text]
        sb_sync_LABEL();
    }
    //}}}
    // sb_sync_LABEL {{{
    private void sb_sync_LABEL()
    {
        if(sbX_np == null)                      return;

        sbX_np.button
            = (gesture_down_sb != null) ?                        gesture_down_sb
            :                               get_sb_for_wv_or_sb( gesture_down_wv );
        if(sbX_np.button == null) return;

        if( property_get( WV_TOOL_JS1_SELECT ) )
        {
            sbX_np.button.setText( sbX_np.text );
            property_name_set_text(WV_JSNP_select_percent, get_wv_thumb_p_str(gesture_down_wv));
        }
        else {
            int                    w = sbX_np.button.getWidth ();
            int                    h = sbX_np.button.getHeight();

            boolean  sb_is_wide  =                (w > (    Settings.TOOL_BADGE_SIZE)); // wide enough
            boolean  sb_is_slim  =                (w < (    Settings.TOOL_BADGE_SIZE)); // too narrow
            boolean  sb_is_high  = !sb_is_slim && (h > (2 * Settings.TOOL_BADGE_SIZE)); // not narrow and high enough

            if(sb_is_wide || sb_is_high)
                sbX_np.button.setText( sbX_np.text + get_wv_thumb_p_str(gesture_down_wv));
            else
                sbX_np.button.setText( sbX_np.text );
        }
    }
    //}}}

    // sb_show_SELECT_find {{{
    public  void sb_show_SELECT_find()
    {
        _set_sb_text("...");

        property_name_set_text(WV_JSNP_select_find   , WV_JSNP_select_find_text   );
        property_name_set_text(WV_JSNP_select_percent, WV_JSNP_select_percent_text);
    }
    //}}}
    // sb_show_SELECT_clear {{{
    public  void sb_show_SELECT_clear()
    {
        _set_sb_text( Settings.SYMBOL_MAGNIFY_RIGHT );

        property_name_set_text     ( WV_JSNP_select_find    , WV_JSNP_select_find_text    );
        property_name_set_text     ( WV_JSNP_select_next    , WV_JSNP_select_next_text    );
        property_name_set_text     ( WV_JSNP_select_percent , WV_JSNP_select_percent_text );

        property_name_set_bg_color ( WV_JSNP_select_find    , PROPERTY_DROPPED_BG_COLOR   );
        property_name_set_bg_color ( WV_JSNP_select_next    , PROPERTY_DROPPED_BG_COLOR   );
        property_name_set_bg_color ( WV_JSNP_select_percent , PROPERTY_DROPPED_BG_COLOR   );
    }
    //}}}
    // sb_show_SELECT_received  {{{
    public  void sb_show_SELECT_received (String text, String matchNum_of_matcheCount, boolean isDoneCounting)
    {
        _set_sb_text( text );

        //(matchNum_of_matcheCount != null) _set_sb_text( text+" ("+matchNum_of_matcheCount+")");

        property_name_set_text(
                WV_JSNP_select_find
                , (matchNum_of_matcheCount != null)
                ?  matchNum_of_matcheCount
                :  WV_JSNP_select_find_text
                );

        property_name_set_text(
                WV_JSNP_select_percent
                , (matchNum_of_matcheCount != null)
                ?  get_wv_thumb_p_str(gesture_down_wv)
                :  WV_JSNP_select_percent_text
                );

        if( isDoneCounting )
            waiting_for_onFindResultReceived = false; // (has been set in scroll_wv_find_selection)
    }
    //}}}
    //}}}
    //* SB_access */
    // get_sb_hotRect {{{
    // scrollbar related:
    // . SCROLLBAR_BORDER_IN
    // . SCROLLBAR_BORDER_OUT
    // . SCROLLBAR_EXPANSION_FACTOR
    // . SCROLLBAR_H_MIN
    // . SCROLLBAR_W_MIN
    private void get_sb_hotRect(NpButton sbX, Rect r)
    {
        // [PLAIN-HIT] .. (SLIGHTLY) past the movable border
        int w = r.right - r.left;

        // [EXPAND SMALL FRAMES] .. accept hits (CLOSELY) outside the movable border
        if(sbX.shape != NotePane.SHAPE_TAG_SCROLL)
        {
            int outset = sbX.w_min;              // [EXPAND SMALL FRAMES] adjustable side
            if(sbX.at_left) r.right += outset;   // .. (RIGHT)
            else            r.left  -= outset;   // .. (LEFT)
        }
        // [SHRINK LARGE FRAMES] .. reject hits near the movable border
        else {
            int inset = w/5;                            // [SHRINK LARGE FRAMES] adjustable side
            if(sbX.at_left) r.right -= inset;    // .. (RIGHT)
            else            r.left  += inset;    // .. (LEFT)
        }
    }
    //}}}
    // get_sbX_expandHitRect {{{
    public void get_sbX_expandHitRect(NpButton sbX, Rect r)
    {
        // [BORDERLINE-HIT] .. (SIGNIFICANTLY) past the movable border
        int w = r.right - r.left;
        int outset;
        if(sbX.shape != NotePane.SHAPE_TAG_SCROLL)
            outset    = Settings.SCROLLBAR_SHAPE_W;     // [~BADGE EXTRA-WIDTH FOR SMALL FRAMES] .. accept hits [CURRENT-WIDTH-PAST] the (MOVABLE-BORDER)
        else
            outset = w/8;                               // [12.5% EXTRA-WIDTH FOR LARGE FRAMES]

        // [EXPAND FRAMES]                              // [EXPAND] adjustable side
        if(sbX.at_left) r.right += outset;       // .. (RIGHT)
        else            r.left  -= outset;       // .. (LEFT)

    }
    //}}}
    // get_sb_at_XY {{{
    private  View get_sb_at_XY(int x, int y)
    {
        if(is_sb_aroundXY(sb , x,y)) return sb ;
        if(is_sb_aroundXY(sb2, x,y)) return sb2;
        if(is_sb_aroundXY(sb3, x,y)) return sb3;
        return null;
    }
    //}}}
    // is_a_scrollbar {{{
    public boolean is_a_scrollbar(View view)
    {
        return         (view != null)
            && (       (view == sb  )
                    || (view == sb2 )
                    || (view == sb3 )
               );
    }
    //}}}
    // get_sb_for_np {{{
    private NpButton get_sb_for_np(NotePane np)
    {
        if(np        == null) return null;
        if(np.button == null) return null;

    //  int x = (int)np.button.getX();
    //  int y = (int)np.button.getY();
        int x = (int)np.button.getTranslationX();
        int y = (int)np.button.getTranslationY();

        if( is_view_atXY( mRTabs.fs_webView , x, y) ) return sb ;
        if( is_view_atXY( mRTabs.fs_webView2, x, y) ) return sb2;
        if( is_view_atXY( mRTabs.fs_webView3, x, y) ) return sb3;

        return null;
    }
    //}}}
    // get_sb_for_wv_or_sb {{{
    private static NpButton get_sb_for_wv_or_sb(View view)
    {
        NpButton sbX = null;

        if(        (    view !=         null          )
                && (   (view instanceof RTabs.MWebView)
                    || (view instanceof       NpButton))
          ) {

            if     (view ==                                 sb ) sbX =  sb ;
            else if(view ==                                 sb2) sbX =  sb2;
            else if(view ==                                 sb3) sbX =  sb3;

            else if(view == WVTools_instance.mRTabs.fs_webView ) sbX =  sb ;
            else if(view == WVTools_instance.mRTabs.fs_webView2) sbX =  sb2;
            else if(view == WVTools_instance.mRTabs.fs_webView3) sbX =  sb3;
          }

//SCROLLBAR//Settings.MOM(TAG_SCROLLBAR, "get_sb_for_wv_or_sb("+get_view_name(view)+") ...return ["+get_view_name(sbX)+"]");
        return sbX;
    }
    //}}}
    // get_sb_for_view {{{
    private NpButton get_sb_for_view(View view)
    {
        if(view == null) return null;
        // [sbX] .. f(wv or sb) // {{{
        NpButton np_button = get_sb_for_wv_or_sb( view );
        if(np_button != null)
            return np_button;

        // }}}
        // [sbX] .. f(NpButton XY [wv]) // {{{
        if(view instanceof NpButton)
        {
            Object o = view.getTag();
            if(o instanceof NotePane)
                return get_sb_for_np( (NotePane)o );
        }
        // }}}
        return null;
    }
    //}}}
    // sb_onDown {{{
    private void sb_onDown()
    {
        if(scroll_wv_AnimatorSet != null)     scroll_wv_AnimatorSet.cancel();

        track_finger_animation_toY          =    -1; // NO DESTINATION TO REACH
        sb_has_paged_up_or_down             = false;
        sb_has_called_findNext              = false;
        sb_follow_finger_anim_called        = false;
        waiting_for_onFindResultReceived    = false;
      //sb_follow_finger_clamped            = false;
    }
    //}}}
    // sb_anim_onUp {{{
    private void sb_anim_onUp()
    {
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, "wvTools_up_anim: scroll_wv_AnimatorSet.cancel()");

        if(scroll_wv_AnimatorSet != null)   scroll_wv_AnimatorSet.cancel();

    }
    //}}}
    // scroll_wv_can_PageUp_PageDown {{{
    private boolean scroll_wv_can_PageUp_PageDown()
    {
        boolean result
            =    (gesture_down_wv != null)
            &&   (gesture_down_sb == null)
          //&&  ((gesture_down_sb == null) || property_get( WV_TOOL_JS1_SELECT ))
          //&&    mRTabs.has_moved_enough
          //&&   !sb_has_paged_up_or_down   // (would work once between onDown and onUP)
          //&&   !sb_has_called_findNext    // (?)
            ;

//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, "scroll_wv_can_PageUp_PageDown: ...return "+ result);
        return result;
    }
    //}}}
    // scroll_wv_can_follow_finger {{{
    private boolean scroll_wv_can_follow_finger()
    {
        boolean result
            =    (gesture_down_sb != null)
            //   (gesture_down_wv != null)
            &&   (SCROLL_WV_FOLLOW_FINGER || !property_get( WV_TOOL_track ))
            ;

//SCROLLBAR//Settings.MOM(TAG_SCROLLBAR, "scroll_wv_can_follow_finger: ...return "+ result);
        return result;
    }
    //}}}
    // is_sb_at_left {{{
    private boolean is_sb_at_left()
    {
        NpButton sbX  = get_sb_for_wv_or_sb( gesture_down_wv );
        return  (sbX != null)
            ?    sbX.at_left
            :    false      ; // sb at_left default .. f(no sb yet) .. [scrollbar right] .. [marker left]
    }
    //}}}
    //* SB_magnet */
    //{{{
    private static       Magnet sb_drag_Magnet             =  null;
    private static          int sb_hotspot_f_x             =     0;
    private static          int sb_hotspot_f_y             =     0;
  //private static      boolean sb_check_done              = false;

    //}}}
    // sb_magnetize {{{
    private void sb_magnetize(int f_x, int f_y)
    {
        // ANIMATION STILL RUNNING .. (hotspot changed) {{{
           String caller = "sb_magnetize";
//*MAGNET*/caller += String.format("(f_x=[%4d], f_y=[%4d]", f_x, f_y);//TAG_MAGNET
//*MAGNET*/Settings.MOC(TAG_MAGNET, caller);

        if(sb_drag_Magnet == null) sb_drag_Magnet = new Magnet( sbMagnetListener );

        if( sb_drag_Magnet.is_animating() )
        {
            sb_hotspot_f_x = f_x;
            sb_hotspot_f_y = f_y;
            return;
        }
        //}}}
        // DISCARD HOTSPOT TRACKING {{{
        else {
            sb_hotspot_f_x = 0;
            sb_hotspot_f_y = 0;

        }
        //}}}
        // [sb_check_done] .. f(last check undone at each call) {{{
      //sb_check_done = false;

        //}}}
        // REQUIRES UI THREAD {{{
        if(Looper.myLooper() != Looper.getMainLooper())
        {
//*MAGNET*/Settings.MOM(TAG_MAGNET, "*** NOT ON UI Thread ***");
            return;
        }

        //}}}
        // MAGNETIZE {{2
        if(gesture_down_sb == null) return;

//*MAGNET*/Settings.MOM(TAG_MAGNET, gesture_down_sb.description      ());
//*MAGNET*/Settings.MOM(TAG_MAGNET, gesture_down_sb.description_saved());

//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "SAVING ATTITUDE AND LOCATION OF ["+gesture_down_sb+"]:");
//*MAGNET*/Settings.MOM(TAG_MAGNET, gesture_down_sb.description      ());
//*MAGNET*/Settings.MOM(TAG_MAGNET, gesture_down_sb.description_saved());
        gesture_down_sb.save_from(caller);
//*MAGNET*/Settings.MOM(TAG_MAGNET, gesture_down_sb.description_from ());

        gesture_down_sb.set_y_to(f_y - gesture_down_sbX_to_finger_y);

//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "SET TO ATTITUDE AND LOCATION:");
//*MAGNET*/Settings.MOC(TAG_MAGNET, ""    , gesture_down_sb.description_to());

        sb_drag_Magnet.animate( SB_MAGNETIZE_ANIM_DURATION );
        //}}}
    }
    //}}}
    //* sbMagnetListener */
    //{{{
    private       Settings.MagnetListener sbMagnetListener = new Settings.MagnetListener()
    {
        public  void    magnet_1_progress(float progress) { if( magnet_freezed() ) return;              sb_1_progress( progress ); }
        public  boolean magnet_2_tracked               () { if( magnet_freezed() ) return false; return sb_2_tracked ();           } // called by Magnet.onAnimationEnd
        public  boolean magnet_3_reached               () { if( magnet_freezed() ) return false; return sb_3_reached ();           }
        public  boolean magnet_4_checked               () { if( magnet_freezed() ) return false; return sb_4_checked ();           }
        public  boolean magnet_freezed                 () {                        return waiting_for_ACTION_POINTER_UP;           }
    };
    //}}}
    // sb_1_progress {{{
    private void sb_1_progress(float progress)
    {
          String caller = String.format("sb_1_progress(%1.2f)", progress);
//*MAGNET*/Settings.MOC(TAG_MAGNET, caller);

        if(gesture_down_sb == null) return;

        RTabs.MWebView wv
            = (gesture_down_sb == sb ) ? mRTabs.fs_webView
            : (gesture_down_sb == sb2) ? mRTabs.fs_webView2
            : (gesture_down_sb == sb3) ? mRTabs.fs_webView3
            : null;
        if(wv == null) return;

        float    y_from = gesture_down_sb.get_y_from();         // anim    start [y]
        float    y_to   = gesture_down_sb.get_y_to  ();         // anim      end [y]
        float    y      = (y_from + (y_to - y_from) * progress); // anim     step [y]

        float sb_h      = gesture_down_sb.getHeight();

        float wv_h      = wv             .getHeight();
        float Wv_t      = wv             .getY     ();
        float wv_b      = Wv_t + wv_h;

        int         toX = wv.getScrollX(); // no change
        int         toY = get_wv_scrollY_for_f_y(wv, (int)y, caller);
        wv.scrollTo(toX, toY);
        sb_sync_LABEL();

        y = Math.max(Wv_t       , y); // not above [wv top]
        y = Math.min(wv_b - sb_h, y); // not below [wv bot]
        gesture_down_sb.setY (    y); //           [anim y]

    }
    //}}}
    // sb_2_tracked {{{
    private boolean sb_2_tracked()
    {
        // TRACK DONE .. f(!sb_hotspot_changed) {{{
//*MAGNET*/String caller = "sb_2_tracked";//TAG_MAGNET
//*MAGNET*/Settings.MOC(TAG_MAGNET, caller);

        boolean sb_hotspot_changed = ((sb_hotspot_f_x > 0) || (sb_hotspot_f_y > 0));
//*MAGNET*/Settings.MOM(TAG_MAGNET, "...sb_hotspot_changed =["+sb_hotspot_changed+"] ("+sb_hotspot_f_x+" "+sb_hotspot_f_y+")");

        if( !sb_hotspot_changed ) return true;

        //}}}
        // NO: MAGNETIZE POST {{{
//*MAGNET*/Settings.MOM(TAG_MAGNET, caller+": POST TRACK ANIM");

        sb_show_TRACKED();

        mRTabs.handler.re_postDelayed(hr_sb_2_track, SB_MAGNETIZE_TRACK_DELAY);

        return false;
        //}}}
    }
    //}}}
    // hr_sb_2_track {{{
    private Runnable hr_sb_2_track = new Runnable() {
        @Override public void run()
        {
            if((sb_hotspot_f_x <= 0) && (sb_hotspot_f_y <= 0)) return;

//*MAGNET*/String caller = "hr_sb_2_track";//TAG_MAGNET
//*MAGNET*/Settings.MOC(TAG_MAGNET, caller+": ANIM RESTART");
                sb_magnetize(sb_hotspot_f_x, sb_hotspot_f_y);
        }
    };
    //}}}
    // sb_3_reached {{{
    private boolean sb_3_reached()
    {
        return true;
//        // REACH DONE .. f(hotspot_reached) {{{
///*MAGNET*/String caller ="sb_3_reached";//TAG_MAGNET
//        // NOTE (it looks like hotspot_reached may never be false at this point)
//
//        boolean hotspot_reached = true;
//
//        if(gesture_down_sb != null)
//        {
///*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "...calling nb.save_from(caller)");
//            gesture_down_sb.save_from(caller); // set new anim start point
//
//          //float  x_to = gesture_down_sb.get_x_to ();
//            float  y_to = gesture_down_sb.get_y_to ();
//          //float  s_to = gesture_down_sb.get_s_to ();
//
//          //float  x    = gesture_down_sb.getX     ();
//            float  y    = gesture_down_sb.getY     ();
//          //float  s    = gesture_down_sb.getScaleY();
//
//          //int   dx    = (int)    (x_to - x) ;
//            int   dy    = (int)    (y_to - y) ;
//          //int   ds    = (int)(10*(s_to - s));
//
//          //if((dx!=0) || (dy!=0) || (ds!=0))
//            if(           (dy!=0)           )
//                hotspot_reached = false;
//        }
//
//        if( hotspot_reached ) return true;
//
//        //}}}
//        // NO: ANIM RESTART {{{
///*MAGNET*/Settings.MOC(TAG_MAGNET, caller+": ANIM RESTART .. f(hotspot_reached)");
//
//        sb_show_REACHED();
//
//        if(sb_drag_Magnet == null) sb_drag_Magnet = new Magnet( sbMagnetListener );
//
//        sb_drag_Magnet.animate( SB_MAGNETIZE_ANIM_DURATION );
//
//        return false;
//        //}}}
    }
    //}}}
    // sb_4_checked {{{
    private boolean sb_4_checked()
    {
        // CHECK DONE .. f(sb_check_done) {{{
//*MAGNET*/String caller ="sb_4_checked";//TAG_MAGNET
//*MAGNET*/Settings.MOC(TAG_MAGNET, caller);
//MAGNET//Settings.MOM(TAG_MAGNET, "...sb_check_done=["+ sb_check_done +"]");

      //if( sb_check_done ) return true;
boolean fake_sb_check_done = true;
if(     fake_sb_check_done ) return true; // (quickfix for statement not reach)

        //}}}
        // NO: SCROLLBAR CHECKED VISUALIZE {{{
//*MAGNET*/Settings.MOM(TAG_MAGNET, "SCROLLBAR CHECK VISUALIZE");

        sb_show_CHECKED();

        return false;
        //}}}
    }
     //}}}
    //* SELECTION (up down)
    // scroll_wv_find_selection {{{
    private boolean waiting_for_onFindResultReceived = false;

    private boolean scroll_wv_find_selection(RTabs.MWebView wv, int x, int y, boolean page_up, int distanceY)
    {
        // (!has_selection_value) .. (WV_TOOL_JS1_SELECT) {{{
        if(!property_get( WV_TOOL_JS1_SELECT )    ) return false;
        if( gesture_down_wv == null               ) return false;
        if(!gesture_down_wv.has_selection_value() ) return false;

        String caller = "scroll_wv_find_selection";
//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, caller);

        //}}}
        // ? FIND_PAGE_COOLDOWN STILL PENDING {{{
        long time_elapsed = System.currentTimeMillis() - last_scroll_wv_time;
        if(time_elapsed < FIND_PAGE_COOLDOWN)
        {
//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, "", "...(time_elapsed=["+time_elapsed+"] < "+FIND_PAGE_COOLDOWN+"): "+(time_elapsed < FIND_PAGE_COOLDOWN));

            return true; // consume
        }
        //}}}
        // SWITCH WEBVIEW FROM DRAGGING TO FINDING .. (requests a call to wvTools.wants_to_steal_events ... that will answer Yes!) {{{
        if(!sb_has_called_findNext )
        {
//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, "", "CALLING reconsider_stealing_events() .. setting (sb_has_called_findNext = true)");

            sb_has_called_findNext = true;

            reconsider_stealing_events();
        }
        //}}}
        // NEW call_findNext .. f(!waiting_for_onFindResultReceived) {{{
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, "...waiting_for_onFindResultReceived=["+waiting_for_onFindResultReceived+"]");
        if( !waiting_for_onFindResultReceived )
        {
            last_scroll_wv_time = System.currentTimeMillis();

            property_name_set_text(
                    WV_JSNP_select_next
                    , page_up
                    ?  Settings.SYMBOL_DOWN_BLACK_TRIANGLE
                    :  Settings.SYMBOL_UP_BLACK_TRIANGLE
                    );

            property_name_set_bg_color(
                    WV_JSNP_select_next
                    , page_up
                    ?  PROPERTY_DOWN_BG_COLOR
                    :  PROPERTY_UP_BG_COLOR
                    );

//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, "", "CALLING gesture_down_wv.call_findNext("+page_up+") .. setting (waiting_for_onFindResultReceived = true)");

            waiting_for_onFindResultReceived = true; // (to be reset by a call to sb_show_SELECT_received)

            gesture_down_wv.call_findNext( page_up ); // forward
        }
        //}}}
        return true;
    }
    //}}}
    //* ANIM (PageUp PageDown) */
    // scroll_wv_anim_PageUpDown {{{
    // {{{
  //public static final float PAGE_HEIGHT_DY             =   1f;//0.95f;
    public static final float PAGE_HEIGHT_DY             = 0.5f;         // (200831) //FIXME

    // }}}
    private boolean scroll_wv_anim_PageUpDown(RTabs.MWebView wv, int x, int y, boolean page_up, int distanceY)
    {
        // ? AMIMATION STILL RUNNING {{{
        String caller = "scroll_wv_anim_PageUpDown";
//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, caller);

        boolean scroll_wv_anim_is_running = (scroll_wv_AnimatorSet != null);
        if( scroll_wv_anim_is_running )
        {
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, "...(scroll_wv_anim_is_running)");
            return true; // consume
        }
        //}}}
        // ? ANIM_PAGE_COOLDOWN STILL PENDING {{{
        long time_elapsed = System.currentTimeMillis() - last_scroll_wv_time;
        if(time_elapsed < ANIM_PAGE_COOLDOWN)
        {
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, caller+": (time_elapsed=["+time_elapsed+"] < "+ANIM_PAGE_COOLDOWN+"): "+(time_elapsed < ANIM_PAGE_COOLDOWN));
            return true; // consume
        }
        //}}}
        // SWITCH WEBVIEW FROM DRAGGING TO PAGING .. (requests a call to wvTools.wants_to_steal_events ... that will answer Yes!) {{{
        int            wv_h = wv.getHeight();
        int         page_dy = (int)(wv_h * PAGE_HEIGHT_DY);
        int finger_travel_Y = 0;

        if(!sb_has_paged_up_or_down )
        {
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, caller+": ...setting [sb_has_paged_up_or_down = true]");
            sb_has_paged_up_or_down = true;

            reconsider_stealing_events();

            // SWALLOW FINGER TRAVEL ON FIRST PAGE .. (down or up) {{{
            if(onDown_URDL == null) // unless move events have been hidden to webview (so that it did not scroll by itself)
            {
                // (it looks like this  needs some getting used to...)
                int        sign = page_up ? 1 : -1;
                finger_travel_Y = (y - (int)onDown_y) * sign;

                page_dy        -= finger_travel_Y;
             }
            //}}}
        }
        //}}}
        // NEW ANIMATION {{{
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, caller+": NEW ANIMATION");


        // PAGE_UP OR PAGE_DOWN
        if(page_up)  page_dy *= -1;

        int     wv_t        =  wv.getScrollY                ();
        int     wv_r        =  wv.computeVerticalScrollRange();
        int     wv_b        =  wv_t + wv_h;

        boolean at_top      = (wv_t <=    0);
        boolean at_bot      = (wv_b >= wv_r);
        boolean at_boundary = page_up ? at_top : at_bot;

//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, caller+": ...........page_up=["+ page_up         +"]");
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, caller+": ...........page_dy=["+ page_dy         +"]");
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, caller+": ...finger_travel_Y=["+ finger_travel_Y +"]");
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, caller+": ..............wv_t=["+ wv_t            +"]");
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, caller+": ..............wv_h=["+ wv_h            +"]");
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, caller+": ..............wv_r=["+ wv_r            +"]");
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, caller+": .......(wv_t+wv_h)=["+ (wv_t+wv_h)     +"]");
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, caller+": .=>....at_boundary=["+ at_boundary     +"]");
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, caller+": .=>....at_top.....=["+ at_top          +"]");
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, caller+": .=>....at_bot.....=["+ at_bot          +"]");

        /* BLINK AT BOUNDARY */
        if( at_boundary ) {
            mRTabs.blink_view(wv, (Color.WHITE) & 0x20FFFFFF);
            return true; // consume
        }

        /* RESET COLORS AT TOP */
        if(at_top) {
            wv.clr_page_boundary_scroll_DY();
        }

        // HIGHLIGHT PAGE BOUNDARY TO REACH
      //highlight_scrolling_viewport(wv, finger_travel_Y, !page_up);
        wv.add_page_boundary_scroll_DY(page_up ? -PAGE_HEIGHT_DY : PAGE_HEIGHT_DY);

        freeze_wv_start(wv, FREEZE_PAGE_UPDOWN);

        // LAUNCH ANIM
        scroll_wv_anim_dy_duration(wv, page_dy, ANIM_SCROLL_PAGE_DURATION, caller);

        sb_show_PAGE_UP_DOWN( page_up );

        return true; // consume
        //}}}
    }
    //}}}
//    // highlight_scrolling_viewport {{{
//    private void highlight_scrolling_viewport(RTabs.MWebView wv, int finger_travel_Y, boolean going_down)
//    {
////*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, "highlight_scrolling_viewport(wv, finger_travel_Y=["+finger_travel_Y+"], going_down=["+going_down+"]");
//
//        int wv_x = wv.getScrollX();
//        int wv_y = wv.getScrollY();
//        int wv_w = wv.getWidth ();
//        int wv_h = wv.getHeight();
//
//        if(going_down) wv_y -= finger_travel_Y;
//        else           wv_y += finger_travel_Y;
//
//        wv.set_page_boundary_rect(wv_x, wv_y, wv_x + wv_w, wv_y + wv_h);
//
//        wv.add_page_boundary_scroll_DY(going_down ? PAGE_HEIGHT_DY : -PAGE_HEIGHT_DY);
//
///*{{{
//        if(going_down) wv_y += wv_h           ;
//}}}*/
///*{{{
////      wv.set_page_boundary_rect(wv_x, wv_y, wv_x+wv_w, wv_y);
////      if(going_down) wv.set_page_boundary_rect(wv_x, wv_y     , wv_x+wv_w, wv_y + wv_h);
////      else           wv.set_page_boundary_rect(wv_x, wv_y-wv_h, wv_x+wv_w, wv_y       );
////      if(going_down) wv.set_page_boundary_rect(wv_x, wv_y, wv_x     , wv_y + wv_h);
////      else           wv.set_page_boundary_rect(wv_x, wv_y, wv_x+wv_w, wv_y - wv_h);
//}}}*/
//    }
//    //}}}
    // un_draw_something_at_page_boundary {{{
    private void un_draw_something_at_page_boundary(RTabs.MWebView wv)
    {
        if(wv == null) return;
    //  wv.clear_do_something_rect();
        mRTabs.schedule_clear_page_boundary_rect(wv, SCROLL_RECT_DURATION);
    }
    //}}}
    //* ANIM (scroll) */
    //{{{
    private                 long last_scroll_wv_time          = 0L;
    private                 long scroll_wv_anim_last_duration = 0;
    private                  int scroll_wv_anim_last_dy       = 0;
    private       RTabs.MWebView scroll_wv_anim_last_wv       = null;

    private              boolean sb_anim_interrupted          = false;
    //}}}
    // scroll_wv_anim_toY {{{
    private void scroll_wv_anim_toY(RTabs.MWebView wv, int toY, long duration)
    {
        String caller = "scroll_wv_anim_toY(toY=["+toY+"], duration=["+duration+"])";

        float                fromY = wv.getScrollY();               //  CURRENT [fromY]
        track_finger_animation_toY = toY;                           //  CURRENT [toY] .. (repeat anim until destination reached)
        float      dy = track_finger_animation_toY - fromY;         //   TRAVEL [dy]
        scroll_wv_anim_dy_duration(wv, (int)dy, duration, caller);  // NEW ANIM
    }
    //}}}
    // scroll_wv_anim_dy_duration {{{
    public void scroll_wv_anim_dy_duration(RTabs.MWebView wv, int dy, long duration, String caller)
    {
        // [duration] [wv_scroll_toY] .. (ANIM parameters) {{{
//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, "scroll_wv_anim_dy_duration(dy=["+dy+"], duration=["+duration+"], caller=["+caller+"])");
        sb_anim_interrupted             = false;
        scroll_wv_anim_last_duration    = duration;
        scroll_wv_anim_last_wv          = wv;
        int            wv_scroll_toY    = wv.getScrollY() + dy; // hotspot
        scroll_wv_anim_last_dy          =                   dy;

        wv_scroll_toY = Math.max(0, wv_scroll_toY);
        wv_scroll_toY = Math.min(   wv_scroll_toY, wv.computeVerticalScrollRange() - wv.getHeight());
        //}}}
        // 1/2 ANIM_SUPPORTED {{{
        if( mRTabs.ANIM_SUPPORTED )
        {
            // CANCEL RUNNING {{{
            if(scroll_wv_AnimatorSet != null) scroll_wv_AnimatorSet.cancel();

            //}}}
            // DURATION {{{
            scroll_wv_AnimatorSet = new AnimatorSet();
            scroll_wv_AnimatorSet.setDuration( duration );

            //}}}
            // INTERPOLATOR {{{
        //  scroll_wv_AnimatorSet.setInterpolator( new AccelerateDecelerateInterpolator() );
        //  scroll_wv_AnimatorSet.setInterpolator( new AccelerateInterpolator          () );
        //  scroll_wv_AnimatorSet.setInterpolator( new AnticipateInterpolator          () );
        //  scroll_wv_AnimatorSet.setInterpolator( new DecelerateInterpolator          () );
            scroll_wv_AnimatorSet.setInterpolator( new LinearInterpolator              () );
        //  scroll_wv_AnimatorSet.setInterpolator( new OvershootInterpolator           () );

            //}}}
            // END LISTENER {{{
            scroll_wv_AnimatorSet.addListener( scroll_wv_anim_endListener );

            //}}}
            // [SCROLL_PROGRESS_PROPERTY] {{{
          //scroll_wv_AnimatorSet.play( ObjectAnimator.ofFloat(wv , SCROLL_PROGRESS_PROPERTY, wv_scroll_toY) );

//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "SAVING ATTITUDE AND LOCATION OF ["+wv+"]:");
            wv.save_from(caller);
            wv.set_y_to (wv_scroll_toY);
            scroll_wv_AnimatorSet.play( ObjectAnimator.ofFloat(wv , SCROLL_PROGRESS_PROPERTY, 0, 1f) );
            //}}}
            // [start] {{{
            last_scroll_wv_time = System.currentTimeMillis();
            scroll_wv_AnimatorSet.start();
            //}}}
        }
        //}}}
        // 2/2 INSTANT {{{
        else {
              wv.scrollTo(wv.getScrollX(), wv_scroll_toY);
              sb_sync_LABEL();
        }
        //}}}
    }
    // }}}
    // scroll_wv_anim_endListener {{{
    private final AnimatorListenerAdapter scroll_wv_anim_endListener = new AnimatorListenerAdapter()
    {
        // onAnimationEnd {{{
        @Override public void onAnimationEnd(Animator animation)
        {
            // REDISPLAY MARKS HIDDEN BY ANIMATION {{{
            String caller = "scroll_wv_anim_endListener.onAnimationEnd";
//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, caller);

//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, "SCROLL ANIMATION END ANIM:");

            String handeled_by = null;
            //}}}
            // TERMINATE [scroll_wv_AnimatorSet] .. (done or interrupted) {{{
            if((handeled_by == null) && (scroll_wv_AnimatorSet == null))
            {
                marker_redisplay_temporarily_hidden_marks(caller);

                handeled_by = "(scroll_wv_AnimatorSet == null)";
            }
            scroll_wv_AnimatorSet      = null; // SCROLL ANIMATION DONE RUNNING
            //}}}
            // SHOW SCROLL ANIMATION END {{{
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, "track_finger_animation_toY=["+track_finger_animation_toY+"]");
            if((handeled_by == null) && (track_finger_animation_toY < 0))
            {
                marker_redisplay_temporarily_hidden_marks(caller);

                freeze_wv_stop(gesture_down_wv); // END

                handeled_by = "(track_finger_animation_toY < 0)";
            }
            //}}}
            // 1/3 (scroll_wv_anim_last_wv == null) {{{
            if((handeled_by == null) && (scroll_wv_anim_last_wv == null))
            {
                sb_show_WAITING();

                marker_redisplay_temporarily_hidden_marks(caller);

                handeled_by = "(scroll_wv_anim_last_wv == null)";
            }
            //}}}
            // 2/3 DESTINATION REACHED {{{
            if(handeled_by == null)
            {
                int   atY = scroll_wv_anim_last_wv.getScrollY(); // current scroll position
                int    dy = track_finger_animation_toY    - atY; // to hotspot
                if(Math.abs(dy) < 2)
                {
                    sb_show_REACHED();

                    marker_redisplay_temporarily_hidden_marks(caller);

                    freeze_wv_stop(gesture_down_wv); // END (reached)

                    handeled_by = "DESTINATION REACHED";

                    track_finger_animation_toY = -1; // SCROLL ANIMATION DESTINATION CLEARED
                }
                // }}}
                // 3/3 DESTINATION NOT REACHED .. RESTART ANIM {{{
                else {
                    sb_show_TRACKED();

                    scroll_wv_anim_dy_duration( scroll_wv_anim_last_wv                      // OR KEEP SCROLLING
                            ,                   dy
                            ,                   scroll_wv_anim_last_duration
                            ,                   caller);

                    handeled_by = "DESTINATION NOT REACHED (keep animating)";
                }
            }
                // }}}
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, caller+": handeled_by=["+handeled_by+"]");
        }
        // }}}
        // onAnimationCancel {{{
        @Override public void onAnimationCancel(Animator animation)
        {
            sb_anim_interrupted = true;

            // REDISPLAY MARKS HIDDEN BY ANIMATION {{{
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, "scroll_wv_anim_endListener.onAnimationCancel: track_finger_animation_toY=["+track_finger_animation_toY+"]");

            sb_show_WAITING();

            marker_redisplay_temporarily_hidden_marks("onAnimationCancel");

            //}}}
            // UNFREEZE [gesture_down_wv] {{{
            freeze_wv_stop(gesture_down_wv); // CANCELED

            //}}}
            scroll_wv_AnimatorSet      = null; // SCROLL ANIMATION DONE RUNNING
            track_finger_animation_toY =   -1; // SCROLL ANIMATION DESTINATION CLEARED
        }
        //}}}
    };
    //}}}
    // SCROLL_PROGRESS_PROPERTY {{{
    public static final Property<RTabs.MWebView, Float> SCROLL_PROGRESS_PROPERTY =
        new Property<RTabs.MWebView, Float>(Float.class, "wvScrollY") {
            // set {{{
            @Override public void  set(RTabs.MWebView wv, Float progress)
            {
                float scroll_from = wv.get_y_from();
                float scroll_to   = wv.get_y_to  ();
                float scrollY     = (scroll_from + progress * (scroll_to - scroll_from));

//*SCROLLBAR*/String caller = "SCROLL_PP.set";//TAG_SCROLLBAR
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, String.format("%s(%1.1f): [%4d => %4d => %4d]", caller, progress, (int)scroll_from, (int)scrollY, (int)scroll_to));

                wv.scrollTo(wv.getScrollX(), (int)scrollY);
                WVTools_instance.sb_sync_LABEL();
            }
            //}}}
            // get {{{
            @Override public Float get(RTabs.MWebView wv)
            {
                //return (float)wv.getScrollY();

                float scroll_to   = wv.get_y_to  ();
                float scroll_from = wv.get_y_from();
                float scroll_done = wv.getScrollY() - scroll_from;
                float scroll_todo =     scroll_to   - scroll_from;
                float progress    =     scroll_todo / scroll_done;

//*SCROLLBAR*/String caller = "SCROLL_PP.get: ...return progress=["+progress+"]";//TAG_SCROLLBAR
                return progress;
            }
            //}}}
        };
    //}}}
    //* ANIM (track) */
    // onMove_7_scroll_track_finger {{{
    private void onMove_7_scroll_track_finger(RTabs.MWebView wv, int f_x, int f_y)
    {
        // UPDATE SCROLLBAR DESTINATION {{{
        String caller = "wvTools.onMove_7_scroll_track_finger";
//*SCROLLBAR*/Settings.MOC(TAG_SCROLLBAR, caller);

        boolean scroll_wv_anim_scheduled
            =    (scroll_wv_AnimatorSet      != null)
            ||   (track_finger_animation_toY >=    0) ;
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, "scroll_wv_anim_scheduled=["+scroll_wv_anim_scheduled+"]");
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, "...(scroll_wv_AnimatorSet != null)=["+(scroll_wv_AnimatorSet != null)+"]");
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, "........track_finger_animation_toY=["+track_finger_animation_toY     +"]");

        f_y -= gesture_down_sbX_to_finger_y;
        track_finger_animation_toY = get_wv_scrollY_for_f_y(wv, f_y, caller);
        sb_show_TRACKED();

        //}}}
        // ...TRACK WITH CURRENT ANIMATION {{{
        if( scroll_wv_anim_scheduled )
        {
            gesture_down_wv.set_y_to( track_finger_animation_toY );

//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, "SCROLL ANIMATION ..set_y_to("+track_finger_animation_toY+")");
        }
        //}}}
        // ...TRACK WITH A NEW ANIMATION {{{
        else {
//*SCROLLBAR*/Settings.MOM(TAG_SCROLLBAR, caller+": POST NEW SCROLL ANIMATION: toY=["+track_finger_animation_toY+"]");

            mRTabs.handler.re_postDelayed(hr_onMove_8_scroll_track_finger, TRACK_FINGER_DELAY);

            sb_follow_finger_anim_called = true;
        }
        //}}}
    }

    private final Runnable hr_onMove_8_scroll_track_finger = new Runnable() {
        @Override public void run() { _scroll_track_finger(); }
    };
    //}}}
    // _scroll_track_finger {{{
    public void _scroll_track_finger()
    {
        // (gesture_down_wv.getScrollY) -> [track_finger_animation_toY] {{{
        String caller = "_scroll_track_finger";
//*EV3_WV_SC*/Settings.MOC(TAG_EV3_WV_SC, caller);
//*EV3_WV_SC*/Settings.MOM(TAG_EV3_WV_SC, "...........gesture_down_wv=["+ gesture_down_wv            +"]");
//*EV3_WV_SC*/Settings.MOM(TAG_EV3_WV_SC, "...........gesture_down_sb=["+ gesture_down_sb            +"]");
//*EV3_WV_SC*/Settings.MOM(TAG_EV3_WV_SC, "track_finger_animation_toY=["+ track_finger_animation_toY +"]");

        if(gesture_down_wv            == null) return; // NO WEBVIEW
        if(gesture_down_sb            == null) return; // NO SCROLLBAR
        if(track_finger_animation_toY <  0   ) return; // canceled

        //}}}
        // REACHED [track_finger_animation_toY] {{{
        int            wv_dy = track_finger_animation_toY - gesture_down_wv.getScrollY();
        boolean close_enough = ((int)(0.05f * wv_dy) == 0); // .. (within 5% near destination)

        if(     close_enough )
        {
//*EV3_WV_SC*/Settings.MOM(TAG_EV3_WV_SC, "DONE SCROLLING");
            sb_show_CHECKED();

        }
        //}}}
        // NO: KEEP SCROLLING {{{
        else {
//*EV3_WV_SC*/Settings.MOM(TAG_EV3_WV_SC, "KEEP SCROLLING");

            // 1/2 [gesture_down_wv.scrollBy] {{{
          //gesture_down_wv.scrollBy(0, (int)wv_dy);
          //mRTabs.handler.re_postDelayedhr_onMove_8_scroll_track_finger TRACK_FINGER_DELAY); // reiterate until hotspot reached

            //}}}
            // 2/2 RESTART ANIM {{{
            // duration range:
            // from [ 1s] for a (QUICK) [FULL WEBVIEW] scroll
            //   to [10s] for a (SLOW ) [SINGLE  PAGE] scroll
            float            wv_h = gesture_down_wv.getHeight();
            float            sb_h = gesture_down_sb.getHeight();

            float  wv_page_height = Math.max(gesture_down_wv.computeVerticalScrollRange (), 1);
            float     viewport_fx = wv_h / wv_page_height;
            int              f_dy = (int)(wv_dy * viewport_fx);

            boolean scroll_page   =               (      Math.abs(f_dy) <= sb_h);         // ? PAGE or FULL-RANGE SCROLL

            float   scroll_factor = scroll_page ? (float)Math.abs(f_dy)  / sb_h           // ? page factor
                :                                 (float)Math.abs(f_dy)  / wv_h;          // ? view factor

            float        duration = scroll_page ?         scroll_factor  * SCROLL_PAGE_DURATION  // = page-factor * page-duration
                :                                         scroll_factor  * SCROLL_VIEW_DURATION; // = view-factor * view-duration

//*EV3_WV_SC*/String anim_type = scroll_page ? "PAGE" : "VIEW";//TAG_EV3_WV_SC
//*EV3_WV_SC*/Settings.MOM(TAG_EV3_WV_SC, "SCROLL ANIMATION NEW ANIM: "+String.format("(%s) f_dy=[%4d] wv_dy=[%4d] duration=[%2.1fs]", anim_type, f_dy, wv_dy, (int)duration/1000f));
            scroll_wv_anim_dy_duration( gesture_down_wv
                    ,                   wv_dy
                    ,              (int)duration
                    ,                   caller);

            // }}}
        }
        //}}}
    }
    //}}}
    //}}}
    /** FREEZE */
    //{{{
    //{{{
    private static final String FREEZE_STOP            = "FREEZE_STOP";
    private static final String FREEZE_PAGE_UPDOWN     = "FREEZE_PAGE_UPDOWN";
    private static final String FREEZE_FIND_INPAGE     = "FREEZE_FIND_INPAGE";
    private static final String FREEZE_FLING_UP        = "FREEZE_FLING_UP";
    private static final String FREEZE_FLING_DOWN      = "FREEZE_FLING_DOWN";
    private static final String FREEZE_TILT_UP_FLING   = "FREEZE_TILT_UP_FLING";
    private static final String FREEZE_TILT_DOWN_FLING = "FREEZE_TILT_DOWN_FLING";

    private static final  float TILT_ANGLE_UP     =  10f;
    private static final  float TILT_ANGLE_DOWN   = -10f;

    //}}}
    // freeze_wv_start {{{
    private void freeze_wv_start(RTabs.MWebView wv, String freeze_type)
    {
        _freeze_wv_set           (wv, freeze_type);
    }
    //}}}
    // freeze_wv_stop {{{
    private void freeze_wv_stop(RTabs.MWebView wv)
    {
        _freeze_wv_set           (wv, FREEZE_STOP);
        un_draw_something_at_page_boundary(wv   );
    }
    //}}}
    // _freeze_wv_set {{{
    private void _freeze_wv_set(RTabs.MWebView wv, String freeze_type)
    {
//*EV3_WV_SC*/Settings.MOM(TAG_EV3_WV_SC, "_freeze_wv_set("+get_view_name(wv)+", "+freeze_type+")");
        if(wv == null) return;

        float scale = 1;
        float angle = 0;

        switch( freeze_type )
        {
          //case FREEZE_STOP            : break;
          //case FREEZE_FLING_DOWN      : break;
          //case FREEZE_FLING_UP        : break;
          //case FREEZE_PAGE_UPDOWN     : break;
          //case FREEZE_FIND_INPAGE     : break;
            case FREEZE_TILT_DOWN_FLING : angle = TILT_ANGLE_DOWN; scale = 1.5f; break;
            case FREEZE_TILT_UP_FLING   : angle = TILT_ANGLE_UP  ; scale = 1.5f; break;
        }

        float     h = wv.getHeight();
        float     w = wv.getWidth ();
        if(angle <= 0) { wv.setPivotY( 0 ); wv.setPivotX(w / 2); }
        else           { wv.setPivotY( h ); wv.setPivotX(w / 2); }

        wv.setScaleY   ( scale );
        wv.setRotationX( angle );
    }
    //}}}
    //}}}
    /** CLAMP */
    //{{{
    //{{{
    private              Clamp     mClamp;

    //}}}

    // 1 clamp1_has_a_grid_for {{{
    public boolean clamp1_has_a_grid_for(NotePane np, float[] grid_w_h_s)
    {
        boolean result = false;
        //         [floating_marker_np] {{{
        if(        (floating_marker_np != null)
                && (floating_marker_np == np  ))
        {
//*CLAMP*/Settings.MOM(TAG_CLAMP, "...[floating_marker_np]");
//          float  m_x = marker_float_get_unlocked_x();
//          grid_w_h_s[0] = m_x;
//          grid_w_h_s[1] = 1f;
//          grid_w_h_s[2] = 1f;
//          result        = true;
            result        = false;
        }
        //}}}
        // MAGNETIZE COORDINATES {{{
        else if(marker_magnetized_np_count > 0)
        {
//*CLAMP*/Settings.MOM(TAG_CLAMP, "...MAGNETIZE COORDINATES");
            grid_w_h_s[0] = 2 * Settings.TOOL_BADGE_SIZE; //4*Settings.TOOL_BADGE_SIZE;
            grid_w_h_s[1] =     1f;                       //  Settings.TAB_MARK_H;
            grid_w_h_s[2] =     1f;
            result        = true;
        }
        //}}}
//*CLAMP*/Settings.MOC(TAG_CLAMP, "clamp1_has_a_grid_for("+np.name+")", "...return "+result+ String.format(" %2f %2f %.1f", grid_w_h_s[0], grid_w_h_s[1], grid_w_h_s[2]));
        return result;
    }
    //}}}
    // 2 clamp2_get_np_to_grid_xy {{{
    public void clamp2_get_np_to_grid_xy(View view, int[] grid_xy)
    {
        // CLEAR IN-OUT-VALUE
        grid_xy[0] = grid_xy[1] = 0;

        if(  view == null) return;

        // CURRENT VIEW POSITION
        float v_l  = view.getX();
        float v_t  = view.getY();

        // ZOOM INDEPENDENT COORDINATES
        float x    = (v_l - mClamp.margin_left) / Settings.DEV_SCALE;
        float y    = (v_t                     ) / Settings.DEV_SCALE;

        //         [floating_marker_np] .. (in place) {{{
        if(        (floating_marker_np        != null)
                && (floating_marker_np.button == view))
        {
            grid_xy[0] = (int)marker_float_get_unlocked_x();
            grid_xy[1] = floating_marker_np.y;
        }
        //}}}
        // CLOSEST MAGNETIZE COORDINATES {{{
        else if(marker_magnetized_np_count > 0)
        {
          //int     dx = (int)(Settings.DISPLAY_W / v_l);
          //int     dy = (int)(Settings.DISPLAY_H * MARKER_MAGNETIZED_FAN_OUT_FX / MARKER_MAGNETIZED_CAPACITY);
          //int     dx = 4*Settings.TOOL_BADGE_SIZE;
          //int     dy = 4*Settings.TAB_MARK_H;
          //grid_xy[0] = (int)(x + dx / 2) / dx;
          //grid_xy[1] = (int)(y + dy / 2) / dy;

            grid_xy[0] = (int)x;
            grid_xy[1] = (int)y;
        }
        //}}}
        // CLOSEST TABS GRID COORDINATES {{{
        else {
            grid_xy[0] = (int)(x + Settings.TAB_GRID_S / 2) / Settings.TAB_GRID_S;
            grid_xy[1] = (int)(y + Settings.TAB_GRID_S / 2) / Settings.TAB_GRID_S;
        }
        //}}}
//*CLAMP*/Settings.MOC(TAG_CLAMP, "wvTools.clamp2_get_np_to_grid_xy", "...return ["+grid_xy[0]+" @ "+grid_xy[1]+"]");
    }
    //}}}
    // 3 clamp3_handle_not_moved {{{
    public void clamp3_handle_not_moved()
    {
//*CLAMP*/Settings.MOC(TAG_CLAMP, "wvTools.clamp3_handle_not_moved", "...NULL EFFECT");
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
        int        parent_w =  parent.getWidth ();
        int        parent_h =  parent.getHeight();

        // }}}
        // bouncing frame {{{
        /* x_min */ playground_rect.left   =        0;
        /* y_min */ playground_rect.top    =        0;
        /* x_max */ playground_rect.right  = parent_w;
        /* y_max */ playground_rect.bottom = parent_h;
        // }}}
        if(np.button == fs_search)
        {
            if(gesture_down_wv != null)
            {
                gesture_down_wv.getHitRect( playground_rect );
            }
            int half = (int)(fs_search.getWidth() / 2 * fs_search.getScaleX());
            playground_rect.left    -= half;
            playground_rect.top     -= half;
            playground_rect.right   += half;
            playground_rect.bottom  += half;
        }
//*CLAMP*/Settings.MOC(TAG_CLAMP, "wvTools.clamp4_get_bounce_playground("+np.name+")", "...return ["+playground_rect.toString()+"]");
        return true;
    }
    //}}}
    // 5 clamp5_get_gravityPoint {{{
    public boolean clamp5_get_gravityPoint(NotePane np, Point gravityPoint)
    {
        boolean result = false;
        // [fs_search] .. (ask mRTabs) {{{
        if(np.button == fs_search)
        {
            result = mRTabs.clamp5_get_gravityPoint(np, gravityPoint); // TODO clear this mess
        }
        //}}}
        // [sbX_np] .. (park fs_search in sbX) {{{
        else if(np == sbX_np)
        {
            gravityPoint.x = sbX_np.x;
            gravityPoint.y = sbX_np.y;
            result = true;
        }
        //}}}
        // [gesture_down_wv] .. (default to current wv markers margin) {{{
        else if(gesture_down_wv != null)
        {
            gravityPoint.x = marker_get_wv_mark_x(gesture_down_wv);//onExpand_x;
            gravityPoint.y = (int)np.button.getTranslationY();//onExpand_y;
            result = true;
        }
        //}}}

//*CLAMP*/Settings.MOC(TAG_CLAMP, "wvTools.clamp5_get_gravityPoint("+np+")", "...return "+result+" ["+gravityPoint.toString()+"]");
        return result;
    }
    //}}}
    // 6 clamp6_run_move_inertia {{{
    public void clamp6_run_move_inertia(final Clamp clamp)
    {
//*CLAMP*/String caller = "wvTools.clamp6_run_move_inertia(clamp)";//TAG_CLAMP
        //clamp.move_inertia();
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller, "CALLING re_post(hr_mClamp_move_inertia)");
        mRTabs.handler.re_post( hr_mClamp_move_inertia );
    }
    //}}}
    // 7 clamp7_is_dragging_something {{{
    public boolean clamp7_is_dragging_something()
    {
        NotePane mClamp_moving_np
            =   (mClamp != null)
            ?    mClamp.moving_np
            :    null;

        boolean result
            =  (mClamp_moving_np != null        )
            && (mClamp_moving_np == fs_search_np)
          //&& (marker_magnetized_np_count > 0)
            && !mRTabs.fs_search_can_swap_webview()
            ;

//*CLAMP*/Settings.MOC(TAG_CLAMP, "clamp7_is_dragging_something", "...return "+result+" (mClamp_moving_np="+mClamp_moving_np+")");
        return result;
    }
    //}}}
    // 8 clamp8_drag {{{
    //{{{
    private int drag_x, drag_y;
    //}}}
    // clamp8_drag {{{
    public void clamp8_drag(float x, float y)
    {
        // (waiting_for_ACTION_POINTER_UP) {{{
//*CLAMP*/String caller = String.format("clamp8_drag(%4d , %4d)", (int)x, (int)y);//TAG_CLAMP
        if( waiting_for_ACTION_POINTER_UP )
        {
//*CLAMP*/Settings.MON(TAG_CLAMP, caller, "...(waiting_for_ACTION_POINTER_UP)");
            return;
        }
        //}}}
        // (NOT marker_magnetized_START_DONE) {{{
        if( !marker_magnetized_START_DONE )
        {
//*CLAMP*/Settings.MON(TAG_CLAMP, caller, "...(NOT marker_magnetized_START_DONE)");
            return;
        }
        //}}}
        // [hr_clamp_drag] {{{
        drag_x = (int)x + fs_search_drag_offset_x;
        drag_y = (int)y + fs_search_drag_offset_y;

//*CLAMP*/Settings.MON(TAG_CLAMP , caller, "...calling re_post(hr_clamp_drag)");
        mRTabs.handler.re_post( hr_clamp_drag );
        //}}}
    }
    //}}}
    // hr_clamp_drag {{{
    private Runnable hr_clamp_drag = new Runnable() {
        @Override public void run() {
            marker_magnetize(drag_x, drag_y, "hr_clamp_drag");
        }
    };
    //}}}
    //}}}
    // 9 clamp9_bounced {{{
    public void clamp9_bounced(NotePane moving_np)
    {
//*CLAMP*/Settings.MOC(TAG_CLAMP, "wvTools.clamp9_bounced("+moving_np+")");
        // DONG
        if( !property_get( WV_TOOL_silent ) ) activity_play_sound_click("wvTools.clamp9_bounced");
    }
    //}}}
    // 10 clamp10_onClamped {{{
    //{{{
    private  NotePane hr_onClamped_moving_np = null;

    //}}}
    // clamp10_onClamped {{{
    public boolean clamp10_onClamped(NotePane moving_np)
    {
//*CLAMP*/Settings.MOC(TAG_CLAMP, "wvTools.clamp10_onClamped("+moving_np+")");
        hr_onClamped_moving_np = moving_np;
        mRTabs.handler.re_post( hr_onClamped );
        return  true; // release moving_np
    }
    //}}}
    // hr_onClamped {{{
    private final Runnable hr_onClamped = new Runnable() {
        @Override public void run()
        {
            String caller = "wvTools.hr_onClamped";
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller);
//*CLAMP*/Settings.MOC(TAG_CLAMP, ".......hr_onClamped_moving_np=["+ hr_onClamped_moving_np     +"]");
//*CLAMP*/Settings.MOC(TAG_CLAMP, "...........floating_marker_np=["+ floating_marker_np         +"]");
//*CLAMP*/Settings.MOC(TAG_CLAMP, "...marker_magnetized_np_count=["+ marker_magnetized_np_count +"]");

            if     (hr_onClamped_moving_np     == null              ) return;
            if     (hr_onClamped_moving_np     == fs_search_np      ) onClamped_1_FS_SEARCH_IN_MARGIN();
            else if(hr_onClamped_moving_np     == sbX_np            ) onClamped_2_SCROLLBAR( sbX_np.button );
            else if(hr_onClamped_moving_np     == floating_marker_np) onClamped_3_FLOAT_MARKER();
          //else if(marker_magnetized_np_count >  0                 ) onClamped_4_MAGNETIZE();

            hr_onClamped_moving_np = null;
        }
    };
    //}}}
    // onClamped_1_FS_SEARCH_IN_MARGIN {{{
    private void onClamped_1_FS_SEARCH_IN_MARGIN()
    {
        // MARKER MAGNETIZE STOP .. (FS_SEARCH IN MARGINS) {{{
        String caller = "wvTools.onClamped_1_FS_SEARCH_IN_MARGIN";

        if(is_fs_search_in_margins() && (marker_magnetized_np_count > 0))
        {
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller, "MARKER MAGNETIZE: STOP .. (FS_SEARCH IN MARGINS)");
            marker_magnetize_stop(gesture_down_wv, caller);
        }
        //}}}
    }
    //}}}
    // onClamped_2_SCROLLBAR {{{
    private void onClamped_2_SCROLLBAR(NpButton sbX)
    {
        String caller = "wvTools.onClamped_2_SCROLLBAR("+sbX+")";
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller);

        if( !property_get( WV_TOOL_silent ) ) activity_play_sound_click( caller );

        // [x] [y] {{{
        int x = (int)sbX.getX(); // clamped X
        int y = (int)sbX.getY(); // clamped Y

        int flp_leftMargin           = 0;
        int flp_topMargin            = 0;
        int flp_width                = 0;
        int flp_height               = 0;

        //}}}
        // EVAL SCROLLBAR [width] [height] {{{
        FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams)sbX.getLayoutParams();
        if( Settings.SCROLLBAR_USE_LAYOUT_PARAMS ) {
            flp = (FrameLayout.LayoutParams)sbX.getLayoutParams();
            flp_leftMargin       = x; // clamped X
            flp_topMargin        = y; // clamped Y
            /*
               ... shrink done by onFling_3_SCROLL_SWAP_SIDES
            flp_width            = Settings.TOOL_BADGE_SIZE; // .. (scroll_nb_shrink_to_shape)
            */
            flp_height           = flp.height;
            if( Settings.SCROLLBAR_USE_TRANSLATION ) {
                flp_leftMargin  = x;
                flp_topMargin   = y;
            }
        } else {
            flp_leftMargin       = (int)sbX.getX     ();
            flp_topMargin        = (int)sbX.getY     ();
            /*
               ... shrink done by onFling_3_SCROLL_SWAP_SIDES
            flp_width            = Settings.TOOL_BADGE_SIZE;
            */
            flp_height           =      sbX.getHeight();
        }
        // XXX}}}
        // ADJUST SCROLLBAR [x] [y] [width] [height] {{{
        if( Settings.SCROLLBAR_USE_LAYOUT_PARAMS ) {
            flp.leftMargin     = flp_leftMargin ;
            flp.topMargin      = flp_topMargin  ;
            flp.width          = flp_width      ;
            flp.height         = flp_height     ;
            if( Settings.SCROLLBAR_USE_TRANSLATION ) {
                flp.leftMargin = 0;
                flp.topMargin  = 0;
                sbX.setTranslationX(flp_leftMargin);
                sbX.setTranslationY(flp_topMargin);
            }
            else {
                sbX.setX(flp_leftMargin);
                sbX.setY(flp_topMargin);
            }
            sbX.setLayoutParams( flp           );
        } else {
            sbX.setWidth       ( flp_width     );
            sbX.setX           ( flp_leftMargin);
            sbX.setHeight      ( flp_height    );
            sbX.setY           ( flp_topMargin );
        }
        // XXX}}}
        // LAYOUT SCROLLBAR AND MARKERS {{{
        if(gesture_down_wv  != null)
        {
            // LAYOUT MOVED [sbX]
            sb_layout_frame(sbX, gesture_down_wv, 0);
          //sb_layout_tools(sbX, caller);                 // after scrollbar fling clamped

            // MOVE MARKERS IN THE OPPOSITE MARGIN
            if( !property_get_WV_TOOL_MARK_LOCK )
            {
            //  marker_load_markers_from_cookie(gesture_down_wv, !sbX.at_left);
            //  marker_wv_sync_no_delay(caller);
                marker_sync_sb_side(caller);              // after scrollbar fling clamped
                int f_x = (int)fs_search.getX() + fs_search_drag_offset_x;
                int f_y = (int)fs_search.getY() + fs_search_drag_offset_y;
                mClamp_np_jump_toXY(fs_search_np, f_x, f_y, caller);
            }
        }
        //}}}
    }
    //}}}
    // onClamped_3_FLOAT_MARKER {{{
    private void onClamped_3_FLOAT_MARKER()
    {
        String caller = "wvTools.onClamped_3_FLOAT_MARKER";
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller);

        if(floating_marker_np == null) return;

        if( !property_get( WV_TOOL_silent ) ) activity_play_sound_click(caller);

        marker_float_onStageChanged(caller);

      //marker_wv_sync_no_delay(caller); // this is not working
    }
    //}}}
//    // onClamped_4_MAGNETIZE {{{
//    private void onClamped_4_MAGNETIZE()
//    {
//        String caller = "wvTools.onClamped_4_MAGNETIZE";
///*CLAMP*/Settings.MOC(TAG_CLAMP, caller);
//
//    }
//    //}}}
    //}}}

    // mClamp_np_jump_toXY {{{
    private void mClamp_np_jump_toXY(NotePane np, int toX, int toY, String caller)
    {
        // [fs_search_np] {{{
//*CLAMP*/caller += "->mClamp_np_jump_toXY("+np+", "+(int)toX+", "+(int)toY+")";//TAG_CLAMP
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller);

        int  w = fs_search.getWidth ();
        int  h = fs_search.getHeight();

        fs_search_drag_offset_x = w/2;
        fs_search_drag_offset_y = h/2;

        fs_search.setX      (toX - fs_search_drag_offset_x);
        fs_search.setY      (toY - fs_search_drag_offset_y);

        fs_search.setPivotX (     w/2 );
        fs_search.setPivotY (     h/2 );

        float                   scale = 1f;//.5f;//2f;
        fs_search.setScaleX (   scale );
        fs_search.setScaleY (   scale );

        fs_search_wake_up(caller);
        //}}}
        // [mClamp_np_drop_toXY] {{{

        mClamp_np_catch_atXY_toXY(fs_search_np, toX, toY, toX, toY); // .. (TO == AT)
        //}}}
    }
    //}}}
    // mClamp_np_drop_toXY {{{
    private void mClamp_np_drop_toXY(NotePane np, int toX, int toY)
    {
        // [mClamp_np_catch_atXY_toXY] {{{
        String caller = "mClamp_np_drop_toXY("+np+")";
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller, String.format("   TO [%4d %4d]",   toX,   toY));

        mClamp_np_catch_atXY_toXY(np, toX, toY, toX, toY); // .. (AT == TO)
        //}}}
        // START ANIMATION {{{
//*EV7_WV_FL*/Settings.MOC(TAG_EV7_WV_FL, caller, "CALLING re_post(hr_mClamp_move_inertia)");

        mRTabs.handler.re_post( hr_mClamp_move_inertia );
        //}}}
    }
    //}}}
    // hr_mClamp_move_inertia {{{
    private Runnable hr_mClamp_move_inertia = new Runnable() {
        @Override public void run() {
//*CLAMP*/Settings.MOC(TAG_CLAMP, "wvTools.hr_mClamp_move_inertia");
//*CLAMP*/Settings.MOM(TAG_CLAMP, "...mClamp.moving_np=["+mClamp.moving_np+"]");
            mClamp.move_inertia();
        }
    };
    //}}}

    // mClamp_np_catch_atXY_toXY {{{
    private void mClamp_np_catch_atXY_toXY(NotePane np, int atX, int atY, int toX, int toY)
    {
        //{{{
        String caller = "mClamp_np_catch_atXY_toXY";
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller, String.format(" FROM [%4d %4d] TO [%4d %4d]", (int)atX, (int)atY, (int)toX, (int)toY));

        //}}}
        // CLAMP (np atX atY) {{{
        if(mClamp == null) mClamp = new Clamp(this); // ClampListener

        mClamp.catch_moving_np_at_xy(np, atX, atY);
        //}}}
        // FINGER OFFSET {{{
        mClamp_np_onDown_fingerXY(np, atX, atY);

        //}}}
        // GRAVITY POINT {{{
        np.set_xy              (toX, toY        ); // .. (will be reported while looping by clamp5_get_gravityPoint)
        mClamp.set_gravityPoint(toX, toY, caller);
        mClamp.set_gravity_fx  ( Clamp.MAX_GRAVITY_FX );
        //}}}
    }
    //}}}
    // mClamp_np_onDown_fingerXY {{{
    private void mClamp_np_onDown_fingerXY(NotePane np, int x, int y)
    {
        if(mClamp == null) return;

//*CLAMP*/String caller = "mClamp_np_onDown_fingerXY(np=["+np+"])";//TAG_CLAMP
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller);
//*CLAMP*/Settings.MOC(TAG_CLAMP, ""    , "FINGER XY=["+     mClamp.get_fingerX()+", "+     mClamp.get_fingerY()+"]");
//*CLAMP*/Settings.MOC(TAG_CLAMP, ""    , ">>DOWN XY=["+                      x  +", "+                      y  +"]");
//*CLAMP*/Settings.MOC(TAG_CLAMP, ""    , ".....mClamp.moving_np=["+ mClamp.moving_np +"]");

        // (mClamp.moving_np != np) [mClamp_np_jump_toXY] {{{
        if(mClamp.moving_np != np)
        {
            int atX = (int)np.button.getX();
            int atY = (int)np.button.getY();
            if(np  == fs_search_np)
            {
                atX += fs_search_drag_offset_x;
                atY += fs_search_drag_offset_y;
            }
            mClamp.catch_moving_np_at_xy(np, atX, atY);

//*CLAMP*/Settings.MOC(TAG_CLAMP, ""    , ".....mClamp.moving_np=["+ mClamp.moving_np +"]");
        }
        //}}}
//*CLAMP*/Settings.MOC(TAG_CLAMP, ""    , "...mClamp.set_fingerXY("+ x+", "+y+")"         );
        mClamp.set_fingerXY(x, y);
    }
    //}}}
    // mClamp_np_onMove {{{
    private void mClamp_np_onMove(NotePane np, MotionEvent event)
    {
        if(mClamp == null) return;

        String  caller = "mClamp_np_onMove";
//*CLAMP*/       caller = "mClamp_np_onMove("+np+")";
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller);
//*CLAMP*/Settings.MOC(TAG_CLAMP, ""    , "FINGER XY=["+     mClamp.get_fingerX()+", "+     mClamp.get_fingerY()+"]");
//*CLAMP*/Settings.MOC(TAG_CLAMP, ""    , ">>MOVE XY=["+(int)event .getRawX()     +", "+(int)event .getRawY()     +"]");

        // (mClamp.moving_np != np) [mClamp_np_jump_toXY] [set_fingerXY] {{{
        if(mClamp.moving_np != np)
        {
            int x = (int)np.button.getX();
            int y = (int)np.button.getY();
            if(np == fs_search_np)
            {
                x += fs_search_drag_offset_x;
                y += fs_search_drag_offset_y;
            }
            mClamp_np_jump_toXY(np, x, y, caller);

            x = (int)event.getRawX();
            y = (int)event.getRawY();
            mClamp.set_fingerXY(x, y);
        }
        //}}}

        if(np == fs_search_np)
            fs_search_wake_up(caller);

        mClamp.onTouchEvent(np, event);
    }
    //}}}

    // mClamp_standby {{{
    private void mClamp_standby(String caller)
    {
        caller += "->mClamp_standby";
//*CLAMP*/Settings.MOC(TAG_CLAMP, caller);

        if(mClamp == null) return;

        mClamp.standby(caller);
    }
    //}}}

    // mClamp_is_looping {{{
    private boolean mClamp_is_looping()
    {
        return (mClamp == null) ? false : mClamp.is_looping();
    }
    //}}}
    // is_clamping_button {{{
    private boolean is_clamping_button(NpButton np_button)
    {
        boolean result
            =  (mClamp                  != null     )
            && (mClamp.moving_np        != null     )
            && (mClamp.moving_np.button != null     )
            && (mClamp.moving_np.button == np_button)
            ;
//CLAMP//Settings.MOM(TAG_CLAMP, "is_clamping_button("+np_button+"): ...return "+result);
//CLAMP//Settings.MOM(TAG_CLAMP, "...mClamp=["+ mClamp +"]");
        return result;
    }
//}}}
    //}}}
    /** SPREAD */
    //{{{
    //* SPREAD (wrap) */
    //{{{
    private boolean marker_wrap_state = false;

    //}}}
    // marker_set_wrap_state {{{
    private void marker_set_wrap_state(boolean state)
    {
        // [marker_wrap_state] {{{
//*SPREAD*/Settings.MOC(TAG_SPREAD, "marker_set_wrap_state(state=["+state+"])");

        marker_wrap_state = state;

        //}}}
        // [scaleX SQUARED] .. f(marker_wrap_state) {{{
        float scaleX
            = marker_wrap_state
            ?  (float)Settings.TAB_MARK_H / (float)Settings.TOOL_BADGE_SIZE
            :  1f;

        boolean at_left = !is_sb_at_left();

        //  String shape = marker_wrap_state ? get_marker_shape_for_current_display_orientation( at_left ) : NotePane.SHAPE_TAG_PADD_R;
        String shape = get_marker_shape_for_current_display_orientation( at_left );
        //}}}
        for(Map.Entry<String, Object> entry : MARK_Map.entrySet())
        {
            if( !(entry.getValue() instanceof    NotePane)         ) continue;
            NotePane                marker_np = (NotePane)entry.getValue();
            if(                     marker_np == expanded_marker_np) continue;
            if( is_floating_marker( marker_np )                    ) continue;

            if(marker_np.button.shape       != shape ) marker_np.button.set_shape( shape  );
            if(marker_np.button.getScaleX() != scaleX) marker_np.button.setScaleX( scaleX );

            marker_np.button.invalidate(); // suppress text when too tight
        }
    }
    //}}}
    // marker_unwrap .. (shape) (unexpand) {{{
    private void marker_unwrap(NotePane marker_np)
    {
//*SPREAD*/Settings.MOM(TAG_SPREAD, "marker_unwrap(marker_np): marker_np.button.shape=["+marker_np.button.shape+"]");

        //if( !is_marker_np_button_spread( marker_np.button ) ) return;

        // RESTORE [shape] [width] [height]
        boolean at_left = !is_sb_at_left();
        String    shape = get_marker_shape_for_current_display_orientation( at_left );
        marker_np.button.set_shape( shape );

        // RESTORE [width] [height]
    //  FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams)marker_np.button.getLayoutParams();
    //  flp.width  = Settings.TOOL_BADGE_SIZE;
    //  flp.height = Settings.TAB_MARK_H;
    //  marker_np.button.setLayoutParams( flp );

        marker_unexpand( marker_np );

    }
    //}}}
    // unset_marker_wrapped {{{
    private void unset_marker_wrapped(String caller)
    {
//*SPREAD*/Settings.MOM(TAG_SPREAD, caller+"->unset_marker_wrapped");
        marker_wrap_state = false;
    }
    //}}}
    //* SPREAD (expand) */
    // marker_expand .. (scale) (shape) {{{
    private void marker_expand(NotePane marker_np, float scale_grow, boolean at_left)
    {
        marker_np.button.setVisibility( View.VISIBLE );
        marker_np.button.bringToFront();

        marker_np.button.setScaleX( scale_grow );
        marker_np.button.setScaleY( scale_grow );

        marker_np.button.set_shape( get_marker_shape_for_current_display_orientation( at_left ) );
        marker_np.button.invalidate(); // required! .. (no layout involved to do the job during event dispatch ?)

    }
    //}}}
    // marker_unexpand .. (scale) (translation) {{{
    private static void marker_unexpand(NotePane marker_np)
    {
        if( is_floating_marker( marker_np )) return;

        marker_np.button.setScaleX( 1f );
        marker_np.button.setScaleY( 1f );

        marker_untranslate( marker_np );
    }
    //}}}
    private boolean marker_is_expanded(NotePane marker_np)
    {
        return (marker_np                    != null)
            && (marker_np.button             != null)
            && (marker_np.button.getScaleY() == Settings.MARK_SCALE_GROW)
            ;
    }
    //* SPREAD (translate) */
    // marker_translate .. (translation) {{{
    private void marker_translate(NotePane marker_np)
    {
        //marker_expanded_marker_np_save_translation( marker_np );

        int           w = marker_np.button.getWidth();

        int    h_offset = 2*w;

        boolean at_left = !is_sb_at_left();

        int          dx = (at_left ?  h_offset : -h_offset);

        int           x = (int)marker_np.button.getTranslationX();

        marker_np.button.setTranslationX(x+dx);
    }

//    // marker_expanded_marker_np_save_translation {{{
//    private void marker_expanded_marker_np_save_translation(NotePane marker_np)
//    {
//        onExpand_x     = 0;
//        onExpand_y     = 0;
//        if(marker_np  != null) {
//            onExpand_x = marker_np.button.getTranslationX();
//            onExpand_y = marker_np.button.getTranslationY();
//        }
////*SPREAD*/Settings.MOM(TAG_SPREAD, "marker_expanded_marker_np_save_translation("+marker_np+"): onExpand_x=["+onExpand_x+"] onExpand_y=["+onExpand_y+"]");
//    }
////}}}

    //}}}
    // marker_untranslate .. (translation) {{{
    private static void marker_untranslate(NotePane marker_np)
    {
        if(gesture_down_wv == null) return;

        int x = marker_get_wv_mark_x(gesture_down_wv);
        int y = (int)marker_np.button.getTranslationY();

        marker_np.button.setTranslationX( x );//onExpand_x
        marker_np.button.setTranslationY( y );//onExpand_y
    }

    //}}}
    //* SPREAD (spread) */
    // marker_spread_start {{{
    private void marker_spread_start(NotePane marker_np)
    {
//*SPREAD*/String caller = "marker_spread_start("+marker_np+")";//TAG_SPREAD
//*SPREAD*/Settings.MOC(TAG_SPREAD, caller);

        if(marker_np        == null) return;
        if(marker_np.button == null) return;

        // [spread_start]
        marker_np.button.spread_start( marker_np.text ); // label + info

        marker_np.button.bringToFront();

        // [marker_has_spread_since_onDown]
        marker_has_spread_since_onDown = true; // reset by onDown
    }
    //}}}
    // marker_spread_stop {{{
    public void marker_spread_stop(String caller)
    {
        caller += "->marker_spread_stop";
//*SPREAD*/Settings.MOC(TAG_SPREAD, caller);

        NotePane was_magnetized_marker_np = null;
        NotePane was_floating_marker_np   = null;

        for(Map.Entry<String, Object> entry : MARK_Map.entrySet())
        {
            if( !(entry.getValue()           instanceof   NotePane)                              ) continue;
            NotePane                         marker_np = (NotePane)entry.getValue();
            if(                              marker_np.button.getVisibility() != View.VISIBLE    ) continue; // not visible
            if(                             !marker_np.button                   .is_spread     ()) continue; // not spread
            /*....*/                         marker_np.button                   .   spread_stop();           // spread_stop

            if( is_a_magnetized_marker_view( marker_np.button ) ) was_magnetized_marker_np = marker_np;
            if( is_floating_marker         ( marker_np        ) ) was_floating_marker_np   = marker_np;
        }

        if(was_magnetized_marker_np != null)
            marker_magnetized_sync(caller+": was_magnetized_marker_np=["+was_magnetized_marker_np+"]");

        if(was_floating_marker_np != null)
        {
            marker_float1_UNLOCKING(was_floating_marker_np, caller); // ->[marker_float_onStageChanged]
        }

    }
    //}}}
    //}}}
    /** CB */
    //{{{
    // view_has_release_callback {{{
    public  boolean view_has_release_callback(View view)
    {
        // views relying on [builtin_nb_OnTouchListener]
        String caller = "view_has_release_callback";

        boolean result
            =          ( view == sb       )
            ||         ( view == sb2      )
            ||         ( view == sb3      )
            ||         ( view == fs_search)
            || has_view( view, caller)
            ;

//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, caller+"("+ get_view_name(view) +"): ...return "+ result);
        return result;
    }
    //}}}
    // call_button_callback {{{
    public  boolean call_button_callback(NpButton nb, int action, String caller)
    {
        caller += "->wvTools.call_button_callback("+ get_view_name(nb) +")";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller);

        if     (       sb  == nb          ) {        sb_CB( nb, caller); return true; }
        else if(       sb2 == nb          ) {        sb_CB( nb, caller); return true; }
        else if(       sb3 == nb          ) {        sb_CB( nb, caller); return true; }
        else if( fs_search == nb          ) { fs_search_CB(           ); return true; }
        else if(     has_view(nb , caller)) {        nb_CB( nb        ); return true; }

        return false;
    }
    //}}}
    // sb_CB {{{
    private boolean sb_CB_has_been_called_since_onDown    = false;
    public  void sb_CB(NpButton sbX, String caller)
    {
        // [sb_CB_has_been_called_since_onDown] .. (debounce) {{{
        if( sb_CB_has_been_called_since_onDown) return;

        sb_CB_has_been_called_since_onDown = true;

        caller += "->sb_CB("+ get_view_name(sbX)+")";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller, "...sb_CB_has_been_called_since_onDown=["+sb_CB_has_been_called_since_onDown+"]");
        //}}}
        // [JS_SELECT] .. (clear selection) {{{
        if(         property_get( WV_TOOL_JS1_SELECT )
                && (gesture_down_wv != null)
                &&  gesture_down_wv.has_selection_value()
          ) {
            gesture_down_wv.clear_selection();
            return;
        }
        //}}}
        // IGNORE CLICKED FRAMED TOOL EVENT {{{
        if( is_a_tool( mRTabs.gesture_down_SomeView_atXY ) )
        {
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, "", "IGNORE CLICKED TOOL EVENT");

            return;
        }
        //}}}
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, "", "EV2_WV_CB CYCLE: (SCROLLBAR CLICKED)");
        gui_cycle(caller);

        // left to RTabs.on_UP_6_WV_TOOL_SBX .. to handle ACTION_UP after a long touch
//        // CANCEL SCROLLBAR ADJUSTING BLUEPRINT DISPLAY MODE {{{
//        if( sbX.action_down_in_margin )
//        {
//            sbX.action_down_in_margin = false;
//            sbX.invalidate();
//
//            // UNDIMM WEBVIEW AFTER ADJUSTING SCROLLBAR LAYOUT
//            RTabs.MWebView wv = get_wv_containing_np_button( sbX );
//            wv.setAlpha( 1f );
//        }
//        //}}}
    }
    //}}}
    // nb_CB {{{
    private void nb_CB(NpButton np_button)
    {
        // [np] .. f(gesture_down_marker_np_to_CB) {{{
        String caller = "nb_CB("+get_view_name(np_button)+")";
//*EV2_WV_CB*/Settings.MOC(TAG_EV2_WV_CB, caller);

        NotePane np = null;
        if((gesture_down_marker_np_to_CB != null) && (gesture_down_marker_np_to_CB.button == np_button))
        {
            np = gesture_down_marker_np_to_CB;
//gesture_down_marker_np_to_CB = null;
///*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "...gesture_down_marker_np_to_CB set to ["+gesture_down_marker_np_to_CB+"] .. f(nb_CB)");
        }
        //}}}
        // [np] .. f( np_button.getTag() ) {{{
        if(np == null)
        {
            Object o = np_button.getTag();
            if(o instanceof NotePane)
                np = (NotePane)o;

            if(np == null)
            {
//*EV2_WV_CB*/Settings.MON(TAG_EV2_WV_CB, caller, "*** ["+np_button+"] ...np_button.getTag() should return NotePane instance");
                return;
            }
        }
        //}}}
        // 1/4 [javascript] {{{
        String javascript = get_javascript( np.name );
        if( !TextUtils.isEmpty(javascript) )
        {
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "...calling evaluateJavascript( javascript )");
            // [wv] .. f(np_button) {{{
            RTabs.MWebView wv = get_wv_containing_np_button( np_button );
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "...wv=["+get_view_name(wv)+"]");
            if(wv != null)
            {
                activity_pulse_np_button( np.button );

                wv.assert_no_cache(caller);

                wv.evaluateJS( javascript );
              //wv.evaluateJavascript(javascript, null); // ValueCallback .. (implement onReceiveValue)
            }
            //}}}
            return;
        }
        //}}}
        // 2/4 [mark] [cut_mark] {{{
        else if(   np.name.    equals( WV_TOOL_mark     ) // mark tool
                || np.name.startsWith( WV_TOOL_mark     ) // pinned mark
                || np.name.    equals( WV_TOOL_cut_mark ) // TRASH
        ) {
            // [magnetized_marker_np_onDown] {{{
            if(magnetized_marker_np_onDown != null)
            {
                if(np_button == magnetized_marker_np_onDown.button)
                {
//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "...(np_button == magnetized_marker_np_onDown.button)");
                    // SCROLL
                    String mark_id = marker_get_mark_id_from_button_name( np.button );
                    marker_scroll_wv_to_mark(gesture_down_wv, mark_id);

                    // EXPAND MARKER
                    expand_magnetized_marker_np( np );

                }
            }
            //}}}
            //else {
                activity_pulse_np_button( np.button );
                marker_CB( np );
            //}
            return;
        }
        //}}}
        // 3/4 [property_np_toggle] {{{
        if(property_np_toggle( np ))
        {
            activity_pulse_np_button( np.button );
            return;
        }
        //}}}
        // 4/4 [tag] cmd .. (fallback) {{{

//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "...calling mRTabs.send_wvTools_np_cmd( np )");
        mRTabs.send_wvTools_np_cmd( np );

//*EV2_WV_CB*/Settings.MOM(TAG_EV2_WV_CB, "...RELOADING MARKERS");
        marker_wv_sync_invalidate_pinned_url();
        marker_wv_sync_no_delay(caller);

        //}}}
//        // [marker_magnetized_sync] {{{
//        marker_magnetized_sync(caller);
//
//        //}}}
    }
    //}}}
    //}}}
    /** PROPERTIES */
    //{{{
    //{{{

    private static final    int   PROPERTY_CHECKED_BG_COLOR = Color.RED;

    private static final    int   PROPERTY_DROPPED_BG_COLOR = Color.WHITE;

    private static final    int   PROPERTY_UP_BG_COLOR      = Color.RED;
    private static final    int   PROPERTY_DOWN_BG_COLOR    = Color.GREEN;

    //}}}
    // property_reset {{{
    public  void property_reset(String caller)
    {
        caller += "->property_reset";
//*PROPERTY*/Settings.MOM(TAG_PROPERTY, caller);

        for(Map.Entry<String, Object> entry : TOOL_Map.entrySet())
        {
            if( !(entry.getValue() instanceof NotePane) ) continue;
            NotePane np = (NotePane)entry.getValue();
            if(np == sbX_np) continue;
            property_set(np.name, false);
        }

        property_check_defaults();
    }
    //}}}
    // property_name_set_text {{{
    private void property_name_set_text(String name, String value)
    {
        // (get_tool_np_for_name) {{{
        String caller = "property_name_set_text("+ name.replace("\n"," ")+ ", value=["+value+"])";
//*PROPERTY*/Settings.MOC(TAG_PROPERTY, caller);

        NotePane np = get_tool_np_for_name( name );
//*PROPERTY*/Settings.MOM(TAG_PROPERTY, ".....np=["+ np   +"]");

        if(np == null) return;
        //}}}
        // [setText] .. (value) {{{

        np.setTextAndInfo( value );

        //}}}
    }
    // }}}
    // property_name_set_bg_color {{{
    private void property_name_set_bg_color(String name, int bg_color)
    {
        // (get_tool_np_for_name) {{{
        String caller = "property_name_set_bg_color("+ name.replace("\n"," ")+ ", bg_color=["+bg_color+"])";
//*PROPERTY*/Settings.MOC(TAG_PROPERTY, caller);

        NotePane np = get_tool_np_for_name( name );
//*PROPERTY*/Settings.MOM(TAG_PROPERTY, ".....np=["+ np   +"]");

        if(np == null) return;

        int fg_color = (ColorPalette.GetBrightness( bg_color ) < 128) ? Color.WHITE : Color.BLACK;
        np.button.setBackgroundColor( bg_color );
        np.button.setTextColor      ( fg_color );

        //}}}
    }
    // }}}
    // property_np_toggle {{{
    private boolean property_np_toggle(NotePane np)
    {
        String caller = "property_np_toggle("+get_view_name( np.button )+")";
//*PROPERTY*/Settings.MOC(TAG_PROPERTY, caller);

        if     ( get_tool_np_for_name( np.name ) == null)  return false;
        else if(     is_a_marker_name( np.name )        )  return false;
        /**/          property_toggle( np.name          ); return true;
    }
    // }}}
    // property_toggle {{{
    private void property_toggle(String name)
    {
        boolean state_checked = property_get( name );
//*PROPERTY*/Settings.MOC(TAG_PROPERTY, "property_toggle("+name+")", "TOGGLING FROM ["+state_checked+"] TO ["+!state_checked+"]");

        property_set(name, !state_checked);
    }
    //}}}
    // property_set {{{
    public  void property_set(String name, boolean state_checked)
    {
        // {{{
        String caller = "property_set";
//*PROPERTY*/   caller = "property_set(name=["+name.replace("\n"," ")+"], state_checked=["+state_checked+"]";
//*PROPERTY*/Settings.MOC(TAG_PROPERTY, caller);

        //}}}
        // SET STATE {{{
        _property_name_set_state(name, state_checked);

        //}}}
        // POST-PROCESSING {{{
        // [WV_TOOL_zoom] {{{
        if( name.equals( WV_TOOL_zoom ) )
        {
            if(mRTabs.fs_webView  != null) mRTabs.fs_webView .getSettings().setBuiltInZoomControls( state_checked );
            if(mRTabs.fs_webView2 != null) mRTabs.fs_webView2.getSettings().setBuiltInZoomControls( state_checked );
            if(mRTabs.fs_webView3 != null) mRTabs.fs_webView3.getSettings().setBuiltInZoomControls( state_checked );
          //mRTabs.fs_webView .getSettings().setDisplayZoomControls( state_checked ); // XXX this WAS an issue in FullscreenActivity
        }
        //}}}
        // [WV_TOOL_MDRAG_LOCK] {{{
        else if( name.equals(WV_TOOL_MDRAG_LOCK) ) {

            // SYNC .. (call locked process to re-evaluate resulting situation)
            if(state_checked) {
                marker_magnetize_stop(gesture_down_wv, caller); // (marker_magnetize_pause + marker_magnetize_invalidate_last_checked)
            }
            else {
                marker_wv_sync_invalidate_pinned_url();
                marker_wv_sync_no_delay(caller);
            }
        }
        // }}}
        // [WV_TOOL_MARK_LOCK] {{{
        else if( name.equals(WV_TOOL_MARK_LOCK) )
        {
            // CACHE (PROPERTY GET ACCELERATOR)
            property_get_WV_TOOL_MARK_LOCK = state_checked;

            // SYNC .. (call locked process to re-evaluate resulting situation)
            if(state_checked) {
                marker_magnetize_stop(gesture_down_wv, caller); // (marker_magnetize_pause + marker_magnetize_invalidate_last_checked)
                marker_hide_pinned_markers(caller);
            }
            else {
                marker_wv_sync_invalidate_pinned_url();
                marker_wv_sync_no_delay(caller);
            }
        }
        // }}}
        // [WV_TOOL_grab] {{{
        else if( name.equals( WV_TOOL_grab ) )
        {
            if     ( state_checked && !mRTabs.fs_webView_isGrabbed) activity_anim_grab_fs_webView(caller); // out of sync
            else if(!state_checked &&  mRTabs.fs_webView_isGrabbed) activity_anim_free_fs_webView(caller); // out of sync
            // fs_webview_animation_after will call sync property np
        }
        //}}}
        // [WV_TOOL_FLAG] (WEIGHT SCALE X Y CENTER) {{{
        else if(   name.equals( WV_TOOL_FLAG_SCALE  )
                || name.equals( WV_TOOL_FLAG_X      )
                || name.equals( WV_TOOL_FLAG_Y      )
                || name.equals( WV_TOOL_FLAG_CENTER )
                // name.equals( WV_TOOL_FLAG_WEIGHT ) (   state_checked would be backfiring from hr_mm_check)
                ||(name.equals( WV_TOOL_FLAG_WEIGHT ) && !state_checked)
               )
        {
            marker_magnetize_park  (caller);
            marker_magnetize_resume(caller);
            mm_recheck             (caller);
        }
        // }}}
        // [JS_SELECT] {{{
        else if( name.equals( WV_TOOL_JS1_SELECT ) )
        {
            sb_tools_toggle(caller);
            sb_tools_sync  (caller);

        }
        //}}}
        // [WV_JSNP_select_find] {{{
        else if( name.equals( WV_JSNP_select_find ) )
        {
        //  if(gesture_down_wv != null)  gesture_down_wv.callFindDialog(); // deprecated
            if(gesture_down_wv != null)  gesture_down_wv.clear_selection();
            return; // no effective proprety change
        }
        //}}}
      //// [WV_JSNP_select_prev] {{{
      //else if( name.equals( WV_JSNP_select_prev ) )
      //{
      //    if(gesture_down_wv != null) gesture_down_wv.call_findNext(false); // forward
      //    return; // no effective proprety change
      //}
      ////}}}
      //// [WV_JSNP_select_next] {{{
      //else if( name.equals( WV_JSNP_select_next ) )
      //{
      //    if(gesture_down_wv != null) gesture_down_wv.call_findNext( true); // forward
      //    return; // no effective proprety change
      //}
      ////}}}
        //}}}
    }
    //}}}
    // property_get {{{
    public  boolean property_get(String name)
    {
        if(name == _last_called_property_name)
        {
//*PROPERTY*/Settings.MOM(TAG_PROPERTY, "property_get("+name.replace("\n"," ")+"): ...return "+_last_called_property_value+" .. (_last_called_property_value)");
            return _last_called_property_value;
        }

        boolean value = false;

        NotePane np = get_tool_np_for_name( name );
        if(np != null)
            value = np.text.contains( PROPERTY_CHECKED_SYMBOL );
        else
            value = false;

        _last_called_property_name  = name;
        _last_called_property_value = value;

//*PROPERTY*/Settings.MOM(TAG_PROPERTY, "property_get("+name.replace("\n"," ")+"): ...return "+value+" .. (["+np+"] contains PROPERTY_CHECKED_SYMBOL)");
        return value;
    }
    //}}}
    // _property_name_set_state {{{
    private void _property_name_set_state(String name, boolean state_checked)
    {
        // CACHE INVALIDATION {{{
        String caller = "_property_name_set_state("+ name.replace("\n"," ")+ ", "+state_checked+")";
//*PROPERTY*/Settings.MOC(TAG_PROPERTY, caller);

        property_init_last_cached();
        //}}}
        // (get_tool_np_for_name) {{{
        NotePane np = get_tool_np_for_name( name );
//*PROPERTY*/Settings.MOM(TAG_PROPERTY, ".....np=["+ np   +"]");

        if(np == null) return;

        //}}}
        // [bg_color] [fg_color] {{{

        int bg_color
            = is_a_flag_name( np.name ) ? FLAG_BG_COLOR
            : is_a_lock_name( np.name ) ? LOCK_BG_COLOR
            : state_checked             ? PROPERTY_CHECKED_BG_COLOR
            :                             PROPERTY_DROPPED_BG_COLOR;

        int fg_color = (ColorPalette.GetBrightness( bg_color ) < 128) ? Color.WHITE : Color.BLACK;

        np.button.setBackgroundColor( bg_color );
        np.button.setTextColor      ( fg_color );
        //}}}
        // [text] {{{
        if(state_checked) np.setTextAndInfo(name + PROPERTY_CHECKED_SYMBOL);
        else              np.setTextAndInfo(name                          );

        //}}}
        // (feedback) {{{

        //activity_pulse_np_button( np.button ); // calls activity_play_sound_click

        // the feedback should better be provided
        // . by (the resulting action)
        // . instead of by the (tool actuator)

        //}}}
    }
    // }}}
    // CACHE (property_get_WV_TOOL_MARK_LOCK) {{{
    private         String _last_called_property_name     =  null;
    private        boolean _last_called_property_value    = false;
    private static boolean property_get_WV_TOOL_MARK_LOCK =  true;

    private void property_init_last_cached()
    {
        _last_called_property_name     = null ;
        _last_called_property_value    = false;
    }
    //}}}
    //}}}
    /** JAVASCRIPT */
    //{{{
    public static final String  DOM_SRC_FILE_JS = "DEV/javascript/dom_load.js";
    public static final String  DOM_CSS_JS      = "DEV/javascript/dom_css.js";
    public static final String  DOM_SELECT_JS   = "DEV/javascript/selection.js";
    // get_javascript {{{
    private StringBuilder js_sb = new StringBuilder();

    private String get_javascript(String np_name)
    {
        // [js] [logging] {{{
//*JAVASCRIPT*/String caller = "get_javascript("+np_name+")";//TAG_JAVASCRIPT
//*JAVASCRIPT*/Settings.MOC(TAG_JAVASCRIPT, caller);
        boolean logging = property_get( WVTools.WV_TOOL_JS0_LOGGING );

        // Note: (np_name will be used as an element.ID)
        String id = np_name;

        js_sb.delete(0, js_sb.length());

        String data_css = null;
        String data_js  = null;

        //}}}

        // (SCRIPT -- FILE) - WV_TOOL_JS2_DOM_LOAD {{{
        if( TextUtils.equals(np_name, WV_TOOL_JS2_DOM_LOAD) )
        {
            String file_path
                = Settings.Get_Profiles_dir().getPath()+"/"+DOM_SRC_FILE_JS;

            data_js
                = Settings.load_script_expression(file_path, logging);
        }
        //}}}
        // (CSS    BUILTIN) - WV_TOOL_JS3_black {{{
        else if( TextUtils.equals(np_name, WV_TOOL_JS3_black) )
        {
            data_css = " var data =''\n"
                +      "          +'          * {       color: #ffe              !important; }'\n"
                +      "          +'          * {   font-size: 24px              !important; }'\n"
                +      "          +'          * { font-family: \"Comic sans ms\" !important; }'\n"
                +      "          +'  a         {       color: #44f              !important; }'\n"
                +      "          +'  a:visited {       color: #a4f              !important; }'\n"
                +      "          +' body,div,p {  background: #333              !important; }'\n"
                +      "          ;\n"
                ;

        }
        //}}}

        // 1/2 CSS {{{
        if(data_css != null)
        {
            js_sb.append(" var css  = document.createElement('link');\n"
                    +    " css.id   = '"+ id +"';\n"
                    +    " css.rel  = 'stylesheet';\n"
                    +    " css.type = 'text/css';\n"
                    +    " css.href = 'data:text/css,'+escape(`"+ data_css +"`);\n"
                    +    " document.getElementsByTagName('head')[0].appendChild(css);\n"
                    );

        }
        //}}}
        // 2/2 JS {{{
        else if(data_js  != null)
        {
            js_sb.append( " var js  = document.createElement('script');\n"
                    +     " js.id   = '"+ id +"';\n"
                    +     " js.type = 'text/javascript';\n"
                    +     " js.href = 'data:application/javascript;charset=utf-8,'+ escape(`"+ data_js +"`);\n"
                    +     " document.getElementsByTagName('head')[0].appendChild(js);\n"
                  );
            return data_js;

        }
        //}}}

        // return [javascript] {{{
        String javascript = js_sb.toString();

        //if(javascript != null) javascript = "(function(){"+ javascript +"})()";

        return javascript;
        //}}}
/* DOC {{{
}!!find $LOCAL/STORE/DEV/PROJECTS/RTabs/Util/RTabs_Profiles/DEV/javascript -type f | sed -e 's;.*;:new &;'
:new    $LOCAL/STORE/DEV/PROJECTS/RTabs/Util/RTabs_Profiles/DEV/javascript
:new    $LOCAL/STORE/DEV/PROJECTS/RTabs/Util/RTabs_Profiles/DEV/stylesheet
========================================================================

=========================================================================
:!start explorer "http://unixpapa.com/js/dyna.html"
:!start explorer "https://www.html5rocks.com/en/tutorials/speed/script-loading/"
}}} */
    }
    // }}}
    //}}}
}
/* // {{{

:let @p="CLAMP"
:let @p="COOKIE"
:let @p="EV2_WV_CB"
:let @p="EV3_WV_SC"
:let @p="EXPAND"
:let @p="FS_SEARCH"
:let @p="MK1_COOK"
:let @p="MK2_SYNC"
:let @p="SPREAD"

:let @p="\\(CLAMP\\|EXPAND\\|FS_SEARCH\\|SPREAD\\)"
:let @p="\\(COOKIE\\|EXPAND\\|SPREAD\\)"
:let @p="\\(EXPAND\\|SPREAD\\)"
:let @p="\\(CLAMP\\|FS_SEARCH\\|MAGNET\\)"
:let @p="MK\\d_\\w\\+"
:let @p="MAGNET"
:let @p="CLAMP"
:let @p="\\(EV\\d_\\w\\+\\|MK\\d_\\w\\+\\|CLAMP\\|MAGNET\\)"
:let @p="SCROLLBAR"
:let @p="GUI"
:let @p="PROPERTY"
:let @p="JAVASCRIPT"
:let @p="EV0_WV_DP"
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

:!start explorer "http://patorjk.com/software/taag/\#p=display&f=Doh&t=Marker"
*/ // }}}

