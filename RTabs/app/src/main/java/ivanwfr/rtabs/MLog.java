package ivanwfr.rtabs; // {{{

import android.graphics.Color;
import android.os.Looper;
import android.view.MotionEvent;
import android.widget.TextView;

import java.io.File;

// }}}
public class MLog
{
    public static final boolean LOG_PROFILE = false;

    // {{{
    // MONITOR TAGS MLog
    private static       String TAG_LOG        = Settings.TAG_LOG;

    public static final int     Log_ForeColor_Scroll_OFF    = Color.parseColor("#FFffff00"); // [FG] scroll auto      (log_text)
    public static final int     Log_ForeColor_Scroll_ON     = Color.parseColor("#FFffe000"); // [FG] user is using fg (log_text)

    public static final int     Log_BackColor_Scroll_ON     = Color.parseColor("#40000000"); // [BG] user is using bg (log_text & log_container)
    public static final int     Log_BackColor_STANDBY       = Color.parseColor("#A0ffe000"); // [BG] log_text empty   (log_container)
    public static final int     Log_BackColor_Scroll_OFF    = Color.parseColor("#D0220000"); // [BG] auto scroll      (log_text & log_container)

    public static final int     Log_BackColor_FREEZED       = Color.parseColor("#A00000ff"); // [BG] checkBox_freeze.isChecked
    // }}}

    // ACTIVITY {{{
    private static RTabs mRTabs = null;

    public static void set_RTabs(RTabs RTabs_instance)
    {
        mRTabs = RTabs_instance;
    }

    private static String   MLog_dump_DEVICE()                    { return mRTabs.dump_DEVICE(); }
    private static String   MLog_dump_KEY_VALS()                  { return mRTabs.dump_KEY_VALS(); }
    private static String   MLog_dump_PALETTES()                  { return mRTabs.dump_PALETTES(); }
    private static String   MLog_dump_TABS_LIST()                 { return mRTabs.dump_TABS_LIST(); }
    private static String   MLog_get_current_stage()              { return mRTabs.get_current_stage(); }
    private static String   MLog_log_STATUS_CLIENT()              { return mRTabs.dump_STATUS_CLIENT(); }
    private static String   MLog_log_app_version()                { return mRTabs.log_app_version(); }
    private static String   MLog_log_life_cycle_call_chain()      { return mRTabs.log_life_cycle_call_chain(); }
    private static String   MLog_log_text_getText()               { return mRTabs.log_text_getText();        }

    private static TextView MLog_get_log_text()                   { return mRTabs.get_log_text(); }

    private static boolean  MLog_log_container_is_null()          { return mRTabs.log_container_is_null(); }
    private static boolean  MLog_log_text_is_null()               { return mRTabs.log_text_is_null(); }

    private static int      MLog_ctrl_container_getChildCount()   { return mRTabs.ctrl_container_getChildCount(); }
    private static int      MLog_log_text_getText_length()        { return mRTabs.log_text_getText_length(); }
    private static int      MLog_prof_container_getChildCount()   { return mRTabs.prof_container_getChildCount(); }

    private static void     MLog_apply_GUI_STATE_INFO()           {        mRTabs.apply_GUI_STATE_INFO("MLog"); }
    private static void     MLog_hide_INFO_PANEL()                {        mRTabs.hide_INFO_PANEL("MLog"); }
    private static void     MLog_log_container_set_bg(int color)  {        mRTabs.log_container_set_freezed_color(); }
    private static void     MLog_log_text_clear()                 {        mRTabs.log_text_clear(); }
    private static void     MLog_log_text_setText(String s)       {        mRTabs.log_text_setText(s);    }
    private static void     MLog_set_LOGGING(boolean state)       {        mRTabs.set_LOGGING(state, false, "MLog"); }
    private static void     MLog_update_log_container_scrolling() {        mRTabs.schedule_update_log_container_scrolling(); }

    // }}}
    // SETTINGS {{ {XXX
    private static String   MLog_ReadLastSavedSettings()          { return Settings.ReadLastSavedSettings("MLog"); }
    private static String   MLog_log_display()                    { return Settings.log_display(); }
    private static String   MLog_log_folders()                    { return Settings.log_folders(); }
    private static String   MLog_log_font()                       { return Settings.log_font(); }
    private static String   MLog_log_gui()                        { return Settings.log_gui(); }
    private static String   MLog_log_handles()                    { return Settings.log_handles(); }
    private static String   MLog_log_palette()                    { return Settings.log_palette(); }
    private static String   MLog_log_server()                     { return Settings.log_server(); }
    private static String   MLog_log_sounds()                     { return Settings.log_sounds(); }
    private static String   MLog_log_state()                      { return Settings.log_state(); }

    //}}}

    // LOG USER INFO
    // STATUS  {{{
    // log_STATUS {{{
    public static void log_STATUS(String caller)
    {
        caller += "] [log_STATUS";

        // LOG
        MLog_set_LOGGING(true);
        MLog_apply_GUI_STATE_INFO();

        // LOG FILTERS BYPASS
        Settings.ignore_LOG_FILTERS();

        Log_init("");
        try {

            Log_append("@@@ RESOURCES @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            Log_append( MLog_log_app_version() );
            Log_append( MLog_dump_DEVICE() );

            Log_append("@@@ FOLDERS @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            Log_append( MLog_log_folders() );
            Log_append("\n");

            Log_append("@@@ ACTIVITY - LIFE CYCLE @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            Log_append( MLog_log_life_cycle_call_chain() );
            Log_append( MLog_get_current_stage() );
            Log_append("\n");

            Log_append("@@@ SETTINGS - LAST SAVED @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            Log_append("@@@ LastSavedSettingsTime=["+Settings.LastSavedSettingsTime+"]");
            Log_append( MLog_ReadLastSavedSettings() );

            Log_append("@@@ SETTINGS - CURRENT @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            Log_append( MLog_log_display() );
            Log_append( MLog_log_font()    );
            Log_append( MLog_log_gui()     );
            Log_append( MLog_log_handles() );
            Log_append( MLog_log_palette() );
            Log_append( MLog_log_server()  );
            Log_append( MLog_log_sounds()  );
            Log_append( MLog_log_state()   );

            Log_append("@@@ PROFILE @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            Log_append( MLog_log_STATUS_CLIENT() );
            Log_append("PROFILE ["+ Settings.PROFILE +"]");
            Log_append(String.format("# %12s = %2d child#", "Handle CTRL", MLog_ctrl_container_getChildCount()));
            Log_append(String.format("# %12s = %2d child#", "Handle PROF", MLog_prof_container_getChildCount()));
            Log_append("\n");

            Log_append("@@@ WEB GROUPS @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            Log_append( WebGroup.Dump() );
            Log_append("\n");

            Log_append("@@@ LOGGING @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            long logging_duration = System.currentTimeMillis() - Settings.logging_last_user_activation_time;
            int  log_text_length  = MLog_log_text_getText_length();
            Log_append("LOGGING: ("+(log_text_length/1000)+"Kb not scrolled by user for "+ (logging_duration/1000) +"s)");
            symbols_toString();

            Log_append("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");
        } catch(Exception ex) { log_ex(ex, caller); }

        log_status( Log_toString() );

        // LOG FILTERS RESTORE
        Settings.apply_LOG_FILTERS();
    }
    // symbols_toString {{{
    private static void symbols_toString()
    {
        Log_append( String.format("%27s .. %s", "polling messages"            , Settings.SYMBOL_polling         ));
        Log_append( String.format("%27s .. %s", "timed-out"                   , Settings.SYMBOL_timeout         ));
        Log_append( String.format("%27s .. %s", "not connected"               , Settings.SYMBOL_BROKEN_HEART    ));
        Log_append( String.format("%27s .. %s", "not trying to connect"       , Settings.SYMBOL_no_connect_task ));
        Log_append( String.format("%27s .. %s", "not reading messages"        , Settings.SYMBOL_no_read_task    ));
        Log_append( String.format("%27s .. %s", "expecting reply from server" , Settings.SYMBOL_expecting_reply ));
        Log_append( "\n" );
        Log_append( String.format("%27s .. %s", "cooking data from server"    , Settings.SYMBOL_cooking_data    ));
        Log_append( String.format("%27s .. %s", "got new data from server"    , Settings.SYMBOL_new_data        ));
        Log_append( String.format("%27s .. %s", "got all data from server"    , Settings.SYMBOL_got_data        ));
        Log_append( "\n" );
        Log_append( String.format("%27s .. %s", "freezed"                   , Settings.SYMBOL_freezed           ));
        Log_append( String.format("%27s .. %s", "confused"                  , Settings.SYMBOL_confused          ));
        //------------------------------------- "123456789_123456789_1234567"
    }
    //}}}
    //}}}
    //}}}
    // INVENTORY {{{
    // log_INVENTORY {{{
    public static void log_INVENTORY(String caller)
    {
        caller += "] [log_INVENTORY";

        // LOG
        MLog_set_LOGGING(true);
        MLog_apply_GUI_STATE_INFO();

        // LOG FILTERS BYPASS
        Settings.ignore_LOG_FILTERS();

        log_center("INVENTORY:");

        // PROFILES
        Profile.Clear_Profiles_Dict(caller); // force a profiles folder fresh scan
        Profile.ListProfiles();

        // LOG FILTERS RESTORE
        Settings.apply_LOG_FILTERS();
    }
    //}}}
    // }}}
    // PROFILE {{{
    // log_PROFILE {{{
    public static void log_PROFILE(String caller)
    {
        MLog_set_LOGGING(true);
        MLog_apply_GUI_STATE_INFO();

        // BYPASS LOG FILTERS
        Settings.ignore_LOG_FILTERS();

        // PROFILE
        log_center("CURRENT PROFILE("+caller+"):");
        log( Settings.DisplayWorkingProfileBuffers() );

        log( MLog_dump_KEY_VALS () );
        log( MLog_dump_PALETTES () );
        log( MLog_dump_TABS_LIST() );

        // RESTORE LOG FILTERS
        Settings.apply_LOG_FILTERS();
    }
//}}}
    //}}}

    // LOG BY THEME
    // log_action {{{
    private static void log_action(int action, String caller)
    {
	String l = ((linefeed_required) ? "\n" : "") + caller;
        switch(action) {
            case MotionEvent.ACTION_DOWN:           log(l +" DOWN"  ); break;
            case MotionEvent.ACTION_MOVE:           log(l +" MOVE"  ); break;
            case MotionEvent.ACTION_SCROLL:         log(l +" SCROLL"); break;
            case MotionEvent.ACTION_POINTER_DOWN:   log(l +" P"     ); break;
            case MotionEvent.ACTION_POINTER_UP:     log(l +" p"     ); break;
            case MotionEvent.ACTION_CANCEL:         log(l +" CANCEL"); break;
            case MotionEvent.ACTION_UP:             log(l +" UP"    ); break;
            default:                                log(l +" ?"     ); break;
        }
    }
    //}}}
    // log_ex {{{
    public static void log_ex(Exception ex, String caller)
    {
        if(linefeed_required) log("");

        for(int i=0; i<5; ++i) log(SEP_LINE_EX);

        log(      "*** " + caller                   +  "\n"
                + "*** ["+ ex.getClass().getName()  +"]:\n"
                + "*** " + ex.getMessage()          +  "\n"
                );

        if(Settings.LOG_CAT) ex.printStackTrace();

        for(int i=0; i<5; ++i) log(SEP_LINE_EX);
    }
    //}}}

    // LOG FORMATING
    // STATE FREEZING {{{

    //}}}
    // clear_ERRLOGGED_files {{{
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public  static void clear_ERRLOGGED_files()
    {
        // OUT AND ERR FOLDER
        String dir_path = Settings.Get_Profiles_dir().getPath();
        String app_name = Settings.get_APP_NAME();
        String err_path = dir_path + "/"+ app_name +"_err.log";
        String out_path = dir_path + "/"+ app_name +"_out.log";

        // DELETE EXISTING LOG FILES
        String file_path = "";

        file_path = err_path;
        try {
            File file = new File( file_path ); if( file.exists() ) file.delete();
        }
        catch(Exception ex) { log("*** "+file_path+": "+ ex.getMessage()); }

        file_path = out_path;
        try {
            File file = new File( file_path ); if( file.exists() ) file.delete();
        }
        catch(Exception ex) { log("*** "+file_path+": "+ ex.getMessage()); }

    }
    //}}}
    // set_LOG_FILTER {{{
    public static void set_LOG_FILTER(String key)
    {
        String msg = "set_LOG_FILTER("+ key +")";
        log( msg );

        Settings.toggle_LOG_FILTER( key );
        Settings.apply_LOG_FILTERS();
        Settings.MOM(TAG_LOG, "set_LOG_FILTER("+key+")");
    }
//}}}

    // LOG DISPLAY
    // Private Log StringBuilder {{{
    private static final StringBuilder Log_sb = new StringBuilder();
    public  static void          Log_init  (String msg) { Log_sb.delete(0, Log_sb.length()); Log_append( msg ); }
    public  static void          Log_append(String msg) { Log_sb.append(msg); Log_sb.append("\n"); }
    public  static String        Log_toString()         { return Log_sb.toString(); }
    //}}}
    // log_nonl {{{
 // {{{
    private static StringBuffer                 log_sb = new StringBuffer(); // log sink used until UI is ready
    private static boolean           linefeed_required =              false; // line feed to be flushed before showing some real message after a sequence of progress markers

// }}}
    // log_nonl {{{
    public  static void log_nonl(String msg)
    {
        if(Settings.LOGGING && !Settings.LOG_CAT           ) _log_nonl( msg ); // i.e. willingly logging and not hooked on Visual Studio
        else if(                Settings.LOG_CAT           ) System.err.println("RTABS: "+ msg );
        else if(Looper.myLooper() != Looper.getMainLooper()) System.err.println(           msg );
        else                                                 System.err.println(           msg );

        // log_container .. auto-scroll {{{
	if(!Settings.LOG_CAT && !log_text_scrolled_by_user)
	{
	    scrolled_by_log_time    = System.currentTimeMillis();
	    //log_container.fullScroll( View.FOCUS_DOWN );
            MLog_update_log_container_scrolling();
	}
        //}}}
    }
    //}}}
    private static final int LOG_TEXT_TRUNCATE_LENGTH = 50000; //XXX
    private static       int log_text_truncated_count =     0;
    // _log_nonl {{{
    private static void _log_nonl(String msg)
    {
        // NOTE 1: when !Settings.LOG_CAT .. MON will call _log_nonl
        //{{{
        if( Settings.LOG_CAT ) { Settings.MOM(TAG_LOG, msg); return; }

        //}}}
        // NOTE 2: STOP HERE IF (NOT LOGGING) AND (NOT AN ERROR MESSAGE)
        //{{{
        boolean is_err =  msg.startsWith(  "***") || msg.startsWith("\n***");
	if(!is_err && !Settings.LOGGING ) return;

        //}}}
        // INFO=[@@@] {{{
    //  boolean is_inf
    //      =  msg.startsWith(  "@@@")
    //      || msg.startsWith("\n@@@");

        //}}}
        // FORK is_err to SYSTEM.ERR {{{
        if(    is_err
            // true /* ============== XXX i.e. DEBUG_MODE=DESPERATE */
          ) {
            if( !msg.endsWith("\n") ) System.err.print  ( msg );
            else                      System.err.println( msg );
          }

        //}}}
        // FORK is_inf to SYSTEM.OUT {{{
        else if(  (Settings.LOG_CAT || MLog_log_text_is_null()))
        {
            if( !msg.endsWith("\n") ) System.err.print  ( msg );    // no flush() .. adds a  a newline
            else                      System.err.println( msg );    // *** should work wish System.out -- Does not work with AVS
        }

        //}}}
        // FORK TO DASHBOARD {{{
        /*
        if(        (is_inf || is_err)
                && (this_RTabsClient != null)
                &&  this_RTabsClient.has_DashNotePane()
          )
            this_RTabsClient.error_to_dash( msg );
         */ // TODO - solve enless accumulating loop ?
        //}}}
	// NOTE 3:  ..STORE EARLY MSG .. f(log_text not created yet) .. [i.e. UI in build stages]
        // {{{
        TextView log_text = MLog_get_log_text();
	if(log_text == null)
        {
	    if(log_sb != null) log_sb.append(msg);
	    return;
	}
	//}}}
        // TRUNCATE {{{
        int log_text_length = (log_text != null) ? log_text.getText().length() : 0;
        if(log_text_length > LOG_TEXT_TRUNCATE_LENGTH)
        {
            log_text_truncated_count += log_text_length;
            log_text.setText("<<< log_text_truncated_count=["+log_text_truncated_count+"] >>>\n");
        }
        //}}}
        // NOTE 4:  RESTORE EARLY MSG .. f(log_text created)
        //{{{
	if(log_sb != null)
        {
        //  log_text.append("vvvvv UI WAS NOT READY YET vvvvv\n"+ log_sb.toString() +"^^^^^ UI WAS NOT READY YET ^^^^^");
	    log_text.append(log_sb.toString());
	    log_sb = null;
	}
	//}}}
        // log_text .. append {{{
        if( !msg.endsWith("\n") )
        {
            linefeed_required = true;
        }
        else if(linefeed_required)
        {
            // terminate a series of linefeedless msg
            if(        !msg.startsWith("\n")
                //  && !msg.startsWith(" ")
              )
                log_text.append("\n");   // unless we've got a continuation line that contains a linefeed
            linefeed_required = false;
        }
        if( !linefeed_required ) log_text.append(syms+msg);
        else                     log_text.append(     msg);

        //}}}
    }
    //}}}
    //}}}
    // check_log_auto_toggle_off {{{
    public  static boolean  log_text_scrolled_by_user   = false;

    private static boolean  _check_log_auto_toggle_off_in_progress = false;
    public  static void check_log_auto_toggle_off()
    {
        if(_check_log_auto_toggle_off_in_progress) return;

        int logging_duration_sec = (int)(System.currentTimeMillis() - Settings.logging_last_user_activation_time)/1000;

        //Settings.MON(TAG_LOG, "check_log_auto_toggle_off: "+ logging_duration_sec +"s");

        // IDLE for 60 sec
        if(logging_duration_sec < 60)
            return;

        if(!MLog_log_container_is_null() ) {
            MLog_log_container_set_bg( Log_BackColor_FREEZED );
            log_text_scrolled_by_user = false;
        }

        // LOGGING OFF
        _check_log_auto_toggle_off_in_progress = true;

        TextView log_text = MLog_get_log_text();

        int log_text_length = (log_text != null) ? log_text.getText().length() : 0;
        String msg = "=== LOGGING AUTO-TOGGLE-OFF: ("+(log_text_length/1000)+"Kb unused for "+ logging_duration_sec +"s)";
        if(log_text != null) log_text.append(msg+"\n");
        Settings.MOM(TAG_LOG, msg);

        MLog_set_LOGGING(false);
        //MLog_hide_INFO_PANEL();

        _check_log_auto_toggle_off_in_progress = false;
    }
    //}}}
    // log log_left log_center log_right {{{
    public  static void log_status(String s) { log(STATUS_PREFIX + s.replace("\n","\n||| ")); }
    public  static void log_left  (String s) { log_box(s, JUSTIFY_L, SEP_LINE_LEFT  , CORNER_SYM); }
    public  static void log_center(String s) { log_box(s, JUSTIFY_C, SEP_LINE_AROBAS, CORNER_SYM); }
    public  static void log_right (String s) { log_box(s, JUSTIFY_R, SEP_LINE_RIGHT , CORNER_SYM); }

    // }}}
    // log_box {{{
    private static void log_box(String s, int justify, String t, String o)
    {
	if( !Settings.LOGGING ) return;

	int s_length = 1+ s.length() +1;
	int t_length =    t.length()   ;

	if(s_length > t_length) s = s.substring(0, t_length-5) +"...";
	else		        t = t.substring(0, s_length  );
	s = s.replace("\n"," ");

	int margin_L = 80-t.length();
	if(margin_L < 0) margin_L = 0;

	if(linefeed_required) log("");
	String l = "";
	switch(justify)
	{
	    case JUSTIFY_R: l = SEP_LINE_SPACES.substring(0, margin_L  ); break;
	    case JUSTIFY_C: l = SEP_LINE_SPACES.substring(0, margin_L/2); break;
	    case JUSTIFY_L: l = ""                                      ; break;
	}

        log(      "\n"
                + l+   o + t + o+"\n"
                + l+"| " + s + " |\n"
                + l+   o + t + o+"\n"
                + "\n"
           );

    }
    //}}}
 // log variables {{{
    public  static long    scrolled_by_log_time    = System.currentTimeMillis();

    // TEXT LAYOUT {{{
    private static final String STATUS_PREFIX   = "||| ";
    private static final String SEP_LINE_LEFT   = "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<";
    private static final String SEP_LINE_RIGHT  = ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
//  private static final String SEP_LINE_EQUAL  = "====================================================================================================";
    private static final String SEP_LINE_AROBAS = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@";
    private static final String SEP_LINE_EX     = "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * ";
    private static final String SEP_LINE_SPACES = "                                                                                                    ";
    private static final int    JUSTIFY_L	= 0;
    private static final int    JUSTIFY_C	= 1;
    private static final int    JUSTIFY_R	= 2;
    private static final String CORNER_SYM	= " ";

    //}}}
    // COLORED LAYOUT {{{
    private  static       String                  LOG_BLUE    = Settings.SYMBOL_LARGE_BLUE_CIRCLE +" ";
    private  static       String                  LOG_HAND    = Settings.SYMBOL_ONE_HAND          +" ";
    private  static       String                  LOG_PROF    = Settings.SYMBOL_PROF              +" ";
    private  static       String                  LOG_RED     = Settings.SYMBOL_LARGE_RED_CIRCLE  +" ";
    private  static       String                  LOG_TRAFFIC = Settings.SYMBOL_TRAFFIC           +" ";
    private  static       String                  LOG_BLACK   = Settings.SYMBOL_CTRL              +" ";
    private  static       String                  syms;

    public  static void log        (String msg) { syms = ""         ; log_nonl(msg.replace("\n","\n"+syms) +"\n"); }
    public  static void log_blue   (String msg) { syms = LOG_BLUE   ; log_nonl(msg.replace("\n","\n"+syms) +"\n"); }
    public  static void log_profile(String msg) { syms = LOG_PROF   ; log_nonl(msg.replace("\n","\n"+syms) +"\n"); }
    public  static void log_red    (String msg) { syms = LOG_RED    ; log_nonl(msg.replace("\n","\n"+syms) +"\n"); }
    public  static void log_hand   (String msg) { syms = LOG_HAND   ; log_nonl(msg.replace("\n","\n"+syms) +"\n"); }
    public  static void log_traffic(String msg) { syms = LOG_TRAFFIC; log_nonl(msg.replace("\n","\n"+syms) +"\n"); }
    public  static void log_black  (String msg) { syms = LOG_BLACK  ; log_nonl(msg.replace("\n","\n"+syms) +"\n"); }

    //public  static void log_push(String sym) { syms += "sym"; }
    //public  static void log_pop (String sym) { int idx = syms.lastIndexOf(sym); syms = substring(syms, 0, idx); }
    //}}}

// }}}

}

