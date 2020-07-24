package ivanwfr.rtabs; // {{{

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;

// }}}
public class RTabsClient
{
    //{{{
    public static        String RTABSCLIENT_JAVA_TAG = "RTabsClient (200724:17h:17)";
    // LOGGING
    public  static boolean  D = Settings.D;
    public  static void Set_D(boolean state) { D = state; }

    // MONITOR
    private static boolean  M = Settings.M;
    public  static void Set_M(boolean state) { M = state; }

    // MONITOR TAGS RTabsClient
    private static       String TAG_CART       = Settings.TAG_CART;
    private static       String TAG_CLIENT     = Settings.TAG_CLIENT;
    private static       String TAG_COMM       = Settings.TAG_COMM;
    private static       String TAG_CONNECT    = Settings.TAG_CONNECT;
    private static       String TAG_GLOW       = Settings.TAG_GLOW;
    private static       String TAG_HISTORY    = Settings.TAG_HISTORY;
    private static       String TAG_POLL       = Settings.TAG_POLL;
    private static       String TAG_PROFILE    = Settings.TAG_PROFILE;
    private static       String TAG_READ       = Settings.TAG_READ;
    private static       String TAG_TAB        = Settings.TAG_TAB;
    private static       String TAG_TABGET     = Settings.TAG_TABGET;
    private static       String TAG_TOOL       = Settings.TAG_TOOL;
    private static       String TAG_TOUCH      = Settings.TAG_TOUCH;
    private static       String TAG_URL        = Settings.TAG_URL;
    private static       String TAG_WEBGROUP   = Settings.TAG_WEBGROUP;

    //}}}
    /** RTabsClient */
    // CONST {{{

    // COMM - TODO GET USER INVOLVED

    private static       int    MAX_CONNECTION_ATTEMPTS_COUNT   = Settings.SERVER_PORT_RANGE;
    private static final int    MAX_READ_ATTEMPTS_COUNT         = 10;
    private static final int    MAX_SEND_ATTEMPTS_COUNT         = 10;

    private static final int    CONNECT_TIMEOUT         = 1000;
    private static final int    SOCKET_SETSOTIMEOUT     = 5000;

    // DATA
    private static final String CMD_PROFILE_DOWNLOAD    = "PROFILE_DOWNLOAD";
    private static final String CMD_PROFILE_UPLOAD      = "PROFILE_UPLOAD";
    public  static       String CMD_PROFILE_UPDATE      = "";

    public  static final String  CMD_SIGNIN             = "SIGNIN";
    private static final String  CMD_SIGNOUT            = "SIGNOUT";
    public  static final String  CMD_TABS_GET           = "TABS_GET";
    public  static final String  CMD_PALETTES_GET       = "PALETTES_GET";
    private static final String  CMD_POLL               = "POLL";
    public  static final String  CMD_DEV_DPI            = "DEV_DPI"; // only relevent between designer and server (...as a payload for dispatched POLL message)
    public  static final String  CMD_SAVE               = "SAVE";

    private static final String  CMD_ACK                = " ACK";
    private static final String  CMD_CANCELED           = " CANCELED";

    private static final int     DashForeColor          = Color.parseColor("#ffff4444"); // "#ffff4444"
    private static final int     DashBackColor          = Color.parseColor("#ff222222"); // "#ff222222"

    private static final int     NoteForeColor          = Color.parseColor("#ffcccccc"); // "#ffcccccc"
    private static final int     DashLockColor          = Color.parseColor("#ff440000"); // "#ff440000"

    private static final int     ViewportColor          = Color.parseColor("#08ddffaa");

    //}}}
    // CLASS {{{

    // DATA
    public  static final LinkedHashMap<String, Object> CART_Map     = new LinkedHashMap<>();
    public  static final LinkedHashMap<String, Object> CTRL_Map     = new LinkedHashMap<>();
    public  static final LinkedHashMap<String, Object> DOCK_Map     = new LinkedHashMap<>();
    public  static final LinkedHashMap<String, Object> PROF_Map     = new LinkedHashMap<>();
    public  static final LinkedHashMap<String, Object> AUTO_Map     = new LinkedHashMap<>();
    public  static final LinkedHashMap<String, Object> TABS_Map     = new LinkedHashMap<>();

    private static final LinkedHashMap<String, Object> PALETTES_Map = new LinkedHashMap<>();

    private static final Rect                HookRect      = new Rect();

    private static       NotePane            DashNotePane  = null;
    private static       String              DashMsg_cmd   = "";
    private static       String              DashMsgPoll   = "";


    // COMM
    private static final Object Mutex_SOCKET    = new Object();
    private static String       StoredReply     = "";           // only parser function should consume from this buffer
    public  static String       READ_CHUNKS     = "";

    // }}}
    // INSTANCE {{{

    // MEMBERS
    private final   TextView            log_text;
    private         Socket              socket          = null;

    // INSTANCES
    public static   RTabsClient         RTabsClient_instance;
    public          RTabs               mRTabs          = null;
    public          ViewGroup           tabs_container  = null;

    // WORKER TASKS
    private static  AsyncConnectTask    ConnectTask;
    private static  AsyncReadTask       ReadTask;
    private static  AsyncProfileTask    ProfileTask;

    // CONSTRUCTOR
    public RTabsClient(RTabs mRTabs, TextView log_text)
    {
        RTabsClient_instance = this;

        this.mRTabs     = mRTabs;
        this.log_text   = log_text;
        tabs_container  = new RelativeLayout( RTabs.activity );
    }

    // get_hashMap_name {{{
    private static String get_hashMap_name(HashMap<String, Object> hashMap)
    {
        if     (hashMap ==         CART_Map) return "CART_Map";
        else if(hashMap ==         CTRL_Map) return "CTRL_Map";
        else if(hashMap ==         DOCK_Map) return "DOCK_Map";
        else if(hashMap ==         AUTO_Map) return "AUTO_Map";
        else if(hashMap ==         PROF_Map) return "PROF_Map";
        else if(hashMap ==         TABS_Map) return "TABS_Map";
        else if(hashMap == WVTools.TOOL_Map) return "TOOL_Map";
        else                                 return         "";
    }
    //}}}
    // has_tab_np_button_view {{{
    public static boolean has_tab_np_button_view(View view)
    {
        return (RTabsClient_instance         != null)
            && (view                 instanceof NpButton)
            && (RTabsClient_instance.get_np_for_button( (NpButton)view ) != null)
            ;
    }
    //}}}
    // get_np_for_button {{{
    public NotePane get_np_for_button(NpButton button)
    {
        for(Map.Entry<String, Object> entry : TABS_Map.entrySet())
        {
            if(entry.getKey() == Settings.ENTRY_PALETTE) continue;
            NotePane np = (NotePane)entry.getValue();
            if(np.button == button)
                return np;
        }
        return null;
    }
    //}}}
    // get_DOCK_Map_NpButton {{{
    public NpButton get_DOCK_Map_NpButton(String profile_name)
    {
        String np_button_text = "PROFILE "+ profile_name;
        for(Map.Entry<String, Object> entry : DOCK_Map.entrySet())
        {
            if(entry.getKey() == Settings.ENTRY_PALETTE) continue;
            NotePane np = (NotePane)entry.getValue();
            if((np.button != null) && np.tag.startsWith( np_button_text ))
                    return np.button;
        }
        return null;
    }
    //}}}
    //}}}
    /** REQUEST-REPLY */
    //{{{
    // DATA SOURCE - FROM PROFILE or from SERVER_REPLY
    // 1/2 send_cmd won't be sending the request to the server
    // 2/2 ReadTask will deliver a reply build from current PROFILE local file
    // have_answer_to {{{
    private boolean have_answer_to(String request, String caller)
    {
        request                 = request.trim();
        boolean question_asked  = last_sent_cmd.startsWith( request );
        boolean diag            = false;
        String sym              = Settings.SYMBOL_polling;

        // [THE REQUESTED INFORMATION] can be found in the [CURRENTLY LOADED PROFILE]
        if     (request.equals( CMD_PALETTES_GET )) { diag = question_asked && Settings.LoadedProfile.have_PROFILE_PALETTES(); sym=Settings.SYMBOL_PALETTE; }
        else if(request.equals( CMD_TABS_GET     )) { diag = question_asked && Settings.LoadedProfile.have_PROFILE_TABS();     sym=Settings.SYMBOL_TABS   ; }

        // [LOCAL PROFILES FOLDER SCAN]
        //else if(request.equals( CMD_PROFILE_UPDATE +" "+ Settings.PROFILES_TABLE) ) diag = true;;
        //else if(request.equals( CMD_PROFILE_UPDATE +" "+ Settings.DOCKINGS_TABLE) ) diag = true;;
        // should not get here for that!

        if(diag) { if(D) log_request(sym, "have_answer_to("+request+") ...return  true\n", caller); }
    //  else     { if(D) log_request(sym, "have_answer_to("+request+") ...return false\n", caller); }

        return diag;
    }
    //}}}
    // build_answer_to {{{
    private boolean build_answer_to(String request, String caller)
    {
        boolean diag = false;

        // StoredReply
        if     (request.equals( CMD_PALETTES_GET ))  diag = build_StoredReply_to_PALETTES_GET(caller);
        else if(request.equals( CMD_TABS_GET     ))  diag = build_StoredReply_to_TABS_GET    (caller);

        if(diag) { if(D) log(Settings.SYMBOL_LEFT_ARROW+"build_answer_to("+request+") ...return  true\n"); }
    //  else     { if(D) log(Settings.SYMBOL_LEFT_ARROW+"build_answer_to("+request+") ...return false\n"); }
        return diag;
    }
    //}}}

    // SIGNIN {{{
    public boolean check_SIGNIN_reply(String caller)
    {
        boolean diag = StoredReply.startsWith( CMD_SIGNIN );

        if(diag) if(D) log_request(Settings.SYMBOL_PHONING, "check_SIGNIN_reply: ...return  true\n", caller);
        return diag;
    }

    //}}}
    // PALETTES {{{
    // have_PALETTES_MISSING {{{
    public boolean have_PALETTES_MISSING(String caller)
    {
        boolean diag = (PALETTES_Map.size() ==  0);

        if(diag) if(D) log_request(Settings.SYMBOL_PALETTE, "have_PALETTES_MISSING: ...return  true\n", caller);
        return diag;
    }
    //}}}
    // check_PALETTES_GET_reply {{{
    public boolean check_PALETTES_GET_reply(String caller)
    {
        boolean diag = (StoredReply.startsWith(CMD_PALETTES_GET) );

        if(diag) if(D) log_request(Settings.SYMBOL_PALETTE, "check_PALETTES_GET_reply ...return  true\n", caller);
        return diag;
    }
    //}}}
    // build_StoredReply_to_PALETTES_GET {{{
    private boolean build_StoredReply_to_PALETTES_GET(String caller)
    {
        boolean diag = false;
        String profile_palettes = Settings.LoadedProfile.get_PROFILE_PALETTES();

        if(Settings.LoadedProfile.have_PROFILE_PALETTES())
        {

            // PROFILE PALETTES
            add_last_sent_cmd_ack();
            StoredReply
                = CMD_PALETTES_GET +"\n"
                + profile_palettes // has terminating newline
                + last_sent_cmd
                ;

            diag = true;
        }

        if(diag) { if(D) log_request(Settings.SYMBOL_PALETTE,     "build_StoredReply_to_PALETTES_GET ...return  true ("+ profile_palettes.length()+" bytes)\n", caller); }
        else     { if(D) log_request(Settings.SYMBOL_PALETTE, "*** build_StoredReply_to_PALETTES_GET ...return false REQUIRED DATA NOT FOUND\n"               , caller); }
        return diag;
    }
    //}}}
    //}}}
    // TABS {{{
    // have_TABS_MISSING {{{
    public boolean have_TABS_MISSING(String caller)
    {
        // Currently no tabs to work with .. while loading a user profile
        boolean diag = (TABS_Map.size() ==  0)
            &&        !Settings.is_a_dynamic_profile_entry( Settings.PROFILE )
            ;

        if(diag) if(D) log_request(Settings.SYMBOL_TABS, "have_TABS_MISSING: ...return  true\n", caller);
        return diag;
    }
    //}}}
    // check_TABS_GET_reply {{{
    public boolean check_TABS_GET_reply(String caller)
    {
        boolean diag = (StoredReply.startsWith(CMD_TABS_GET));

        if(diag) if(D) log_request(Settings.SYMBOL_TABS, "check_TABS_GET_reply ...return  true\n", caller);
        return diag;
    }
    //}}}
    // build_StoredReply_to_TABS_GET {{{
    private boolean build_StoredReply_to_TABS_GET(String caller)
    {
        boolean diag = false;
        String profile_tabs = Settings.LoadedProfile.get_PROFILE_TABS();

        // SEARCH CURRENTLY LOADED PROFILE
        if(Settings.LoadedProfile.have_PROFILE_TABS())
        {
            // PROFILE KEY_VAL
            String one_liner_key_val = Settings.LoadedProfile.get_PROFILE_KV_LINE();
        //  Settings.set_KEY_VAL(one_liner_key_val, "build_StoredReply_to_TABS_GET"); // about to be handled by parse_TABS

            // PROFILE TABS
            add_last_sent_cmd_ack();
            StoredReply
                = CMD_TABS_GET
                +" SOURCE="    + Settings.DEVICE +" "+ one_liner_key_val +"\n"
                + profile_tabs // has terminating newline
                + last_sent_cmd
                ;

            diag = true;
        }

        if(diag) { if(D) log_request(Settings.SYMBOL_TABS,     "build_StoredReply_to_TABS_GET ...return  true ("+ profile_tabs.length()+" bytes)\n", caller); }
        else     { if(D) log_request(Settings.SYMBOL_TABS, "*** build_StoredReply_to_TABS_GET ...return false REQUIRED DATA NOT FOUND"        +"\n", caller); }
        return diag;
    }
    //}}}
    //}}}
    // PROFILES {{{
    public boolean have_PROFILE_UPDATE(String caller)
    {
        boolean diag = (CMD_PROFILE_UPDATE != "");

        if(diag) if(D) log_request(Settings.SYMBOL_PROF, "have_PROFILE_UPDATE ...return  true CMD_PROFILE_UPDATE["+ CMD_PROFILE_UPDATE +"]\n", caller);
        return diag;
    }
    public boolean check_PROFILE_UPDATE_reply(String caller)
    {
        boolean wrong_answer = !StoredReply.startsWith( CMD_PROFILE_DOWNLOAD );

        boolean diag
            =  !wrong_answer
            && has_last_sent_cmd_ack()
            ;

        if( wrong_answer )
            discard_received_message("check_PROFILE_UPDATE_reply wrong_answer");

        if(D) log_request(Settings.SYMBOL_PROF, "check_PROFILE_UPDATE_reply ...return "+diag+"\n", caller);
        return diag;
    }
    //}}}
    // POLL {{{
    public boolean check_POLL_message(String caller)
    {
        boolean diag
            =   (StoredReply.startsWith( CMD_POLL           ))
            &&  (StoredReply.length() > (CMD_POLL.length()+1));

        if(diag) if(D) log_request(Settings.SYMBOL_polling, "check_POLL_message: ...return  true\n", caller);
        return diag;
    }

    //}}}

    // DISCARD UNPARSED SERVER MESSAGES {{{
    public void    discard_received_message(String caller)
    {
        if(StoredReply.equals("")) return;

        if(D) log_center("discard_received_message"+Settings.SYMBOL_LEFT_ARROW+caller);
        if(D) log       ("StoredReply=("+StoredReply.length()+"b)["+ Settings.ellipsis(StoredReply, 80) +"]");
        StoredReply = "";
    }
    //}}}
    //}}}
    /** SEND-READ */
    //{{{
    // WOL {{{
/*{{{*/
    private AsyncWorkerTask WorkerTask = null;
    public static       long WOL_SERVER_WAKEUP_DELAY_MS = 120 * 1000L; /* 2 minutes .. server wakeup delay */
    public              long last_sendWOLtime           = 0L;

//:!start explorer "https://en.wikipedia.org/wiki/Wake-on-LAN"
/*}}}*/
    /* sendWOL {{{*/
    public void sendWOL(String cmdLine)
    {
        String caller = "sendWOL("+cmdLine+")";
//*COMM*/Settings.MOC(TAG_COMM, caller);

        String words[] = cmdLine.split(" ");
        String ip      = (words.length > 1) ? words[1] : Settings.SERVER_IP;    // PCIVAN
        String subnet  = (words.length > 2) ? words[2] : Settings.SERVER_SUBNET;// SUBNET
        String mac     = (words.length > 3) ? words[3] : Settings.SERVER_MAC;   // MAC

//*COMM*/Settings.MOM(TAG_COMM, "ip=["+ ip +"] subnet=["+ subnet +"] mac=["+ mac +"]");

        if(        TextUtils.isEmpty( ip     )
                || TextUtils.isEmpty( mac    )
                || TextUtils.isEmpty( subnet )
          ) {
            mRTabs.toast_long(Settings.SYMBOL_COMPUTER+" LAST SERVER 3 PARAMS NOT ALL STORED YET "+Settings.SYMBOL_COMPUTER+"\n"
                    +     "ip=["+     ip +"]\n"
                    +    "mac=["+    mac +"]\n"
                    + "subnet=["+ subnet +"]"
                    );
          }
        else {
            mRTabs.toast_again_clear();
            if(WorkerTask == null) {
                WorkerTask = new AsyncWorkerTask();

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    WorkerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "WOL", ip, subnet, mac);
                else
                    WorkerTask.execute          (                                "WOL", ip, subnet, mac);

                last_sendWOLtime = System.currentTimeMillis();
            }
            else {
                if(WorkerTask.getStatus() == AsyncTask.Status.PENDING ) if(D) log("WorkerTask PENDING" );
                if(WorkerTask.getStatus() == AsyncTask.Status.RUNNING ) if(D) log("WorkerTask RUNNING" );
                if(WorkerTask.getStatus() == AsyncTask.Status.FINISHED) if(D) log("WorkerTask FINISHED");
            }
        }
    }
    /*}}}*/
    /* get_WOL_label {{{*/
    public String get_WOL_label()
    {
        String caller = "get_WOL_label";

        String label = "WOL";

        if(last_sendWOLtime > 0)
        {
            long      time_elapsed  = System.currentTimeMillis() - last_sendWOLtime;
            label += (time_elapsed <= WOL_SERVER_WAKEUP_DELAY_MS ? Settings.SYMBOL_CHECK_HEAVY : "")
                + "\n( "+ Settings.Get_time_elapsed( last_sendWOLtime ) +" ago)"
                ;
        }

        return label;
    }
    /*}}}*/
    //}}}
    // last_sent_cmd {{{
    private String  last_sent_cmd       = "";
    private int     last_connect_count  = 0; // initial number of retries for the next connection attempt
    private int     last_read_count     = 0;
    private int     last_send_count     = 0;

    public  int     get_last_sent_cmd_count() { return last_send_count; }

    private boolean has_last_sent_cmd_ack() {
        return (last_sent_cmd == "") || last_sent_cmd.endsWith(CMD_ACK);
    }

    private void add_last_sent_cmd_ack() {
        if((last_sent_cmd != "") && !last_sent_cmd.endsWith( CMD_ACK ))
            last_sent_cmd += CMD_ACK;
    }

    private void log_request(String sym, String msg, String caller) {
        String sym3 = sym+sym+sym;
        log(       sym  + "\n"
                +  sym3 + "CALLER: " + caller + "\n"
                +  sym3 + ".last_sent_cmd=[" + last_sent_cmd + "]\n"
                +  sym3 + "...StoredReply=[" + Settings.ellipsis(StoredReply, 80) + "]\n"
                +  sym3 + ".......PROFILE=[" + Settings.PROFILE + "]\n"
                +  sym  + Settings.SYMBOL_LEFT_ARROW + msg
           );
    }

    //}}}
    // send {{{
    public void sendCmd(String txt, String caller)
    {
        caller += "] [sendCmd("+txt+")";
//*COMM*/Settings.MOC(TAG_COMM, caller);

        // FREEZED-LOCK {{{
        if( Settings.FREEZED ) {
            mRTabs.toast_short(Settings.SYMBOL_freezed+" FREEZED CONNECTION "+Settings.SYMBOL_freezed+"\n...WILL NOT SEND COMMAND:"+"\n"+txt);
            return;
        }
        //}}}
        // OFFLINE {{{
        if( Settings.OFFLINE ) {
            mRTabs.toast_short(Settings.SYMBOL_offline+" OFFLINE "           +Settings.SYMBOL_offline+"\n...WILL NOT SEND COMMAND:"+"\n"+txt);
            return;
        }
        //}}}
        mRTabs.toast_again_clear();

        StringBuilder sb = new StringBuilder();
        int              len = txt.length();
        for(int i=0; i < len; ++i)
        {
            Character c = txt.charAt(i);
            if((int)c <= 127) sb.append(c);
        //  else              sb.append(String.format( "@+%04x", (int)c));  // Français
        //  else              sb.append(String.format("\\u%04x", (int)c));
            else              sb.append(String.format( "U+%04x", (int)c));  // Français
        }
        String enc = sb.toString();

        send(enc, caller);
    }

    public void send(String new_cmd,              String caller) { send(new_cmd, "", caller); }

    public void send(String new_cmd, String args, String caller)
    {
        if(D) log("send("+ new_cmd+" "+args+")"+ Settings.SYMBOL_LEFT_ARROW + caller +":");

        // OFFLINE-LOCK {{{
        if( Settings.OFFLINE ) {
            mRTabs.toast_short(Settings.SYMBOL_offline+" OFFLINE "+Settings.SYMBOL_offline+"\n...WILL NOT SEND COMMAND:"+"\n"+new_cmd+"\n"+args);
            return;
        }
        //}}}
        mRTabs.toast_again_clear();

        if( new_cmd.equals(CMD_SIGNIN) ) clear_SIGNIN_ERROR_MESSAGE();

        // NEW CONNECTION REQUIRED @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        //{{{
        if(        !have_answer_to(new_cmd, "send")
                && !isConnected()
          ) {
            if(D) log("NEW CONNECTION REQUIRED");
            // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            // 1/2 - BUSY CONNECT TASK ... LOG PROGRESS THEN RETURN @@@
            // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            // {{{
            if(ConnectTask != null)
            {
                if(ConnectTask.getStatus() == AsyncTask.Status.PENDING ) if(D) log("ConnectTask PENDING" );
                if(ConnectTask.getStatus() == AsyncTask.Status.RUNNING ) if(D) log("ConnectTask RUNNING" );
                if(ConnectTask.getStatus() == AsyncTask.Status.FINISHED) if(D) log("ConnectTask FINISHED");

                if(last_connect_count != 0)
                {
                    if(D) log(String.format(" %s STILL NOT CONNECTED: #%d on %s:%d"
                                , Settings.SYMBOL_BROKEN_HEART
                                , last_connect_count
                                , Settings.SERVER_IP
                                , Settings.SERVER_PORT
                                ));
                }
                else {
                    if(D) log_nonl( get_connection_state_symbol() );
                }
            }

            //}}}

            // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            // 2/2 - NO CONNECT TASK .. CREATE A NEW ONE @@@@@@@@@@@@@@
            // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            // {{{
            if(ConnectTask == null)
            {
                if(D) log("NO ConnectTask WORKING");

                if( !has_max_connection_failed() )
                {
                    connect(new_cmd, args);
                }
                else {
                    if(D) log(String.format(" %s NOT CONNECTED after "+ last_connect_count +" attempts", Settings.SYMBOL_BROKEN_HEART));
                    String syms = Settings.SYMBOL_NO_ENTRY + Settings.SYMBOL_NO_ENTRY + Settings.SYMBOL_NO_ENTRY;
                    String msg = "*** ["+ new_cmd +"] ***\n"
                        + syms +" ===========================================\n"
                        + syms +" CONNECTION PROGRESS LOCKED:             ===\n"
                        + syms +" . An explicit user [SIGNIN] or [SERVER] ===\n"
                        + syms +" . for a disconnect-call is now required ===\n"
                        + syms +" ===========================================\n"
                        ;
                    log( msg );
                }
            }
            //}}}

            // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            // CONNECTION REQUIRED CHECKPOINT @@@@@@@@@@@@@@@@@@@@@@@@@
            // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            if( !socket_isReady() )
                return;
          }
        // @@@@@@@@@ REACHING THIS POINT IMPLIES THAT THE FOLLOWING @@@
        // @@@@@@@@@ CONDITIONS HAVE BEEN MET:                      @@@
        // @@@@@@@@@ 1/2 - CONNECTION IS NOT REQUIRED               @@@
        // @@@@@@@@@ 2/2 - AN ACTIVE SERVER CONNECTION IS IN EFFECT @@@
        //}}}
        // NEW CONNECTION REQUIRED @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        // REPLY PROGRESS @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        //{{{
        if(D) {
            String msg
                = "@@@ send: ...new_cmd=[" + new_cmd + "] args=[" + args + "]\n"
                + "@@@ ..........caller=[" + caller + "]\n"
                + "@@@ ...last_sent_cmd=[" + last_sent_cmd + "] " + get_send_status() + "\n"
                + "@@@ .....StoredReply=(" + StoredReply.length() + "b) [" + Settings.ellipsis(StoredReply, 80) + "]\n"
                ;
            log( msg );
        }
        //}}}
        // REPLY PROGRESS @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        // WRITE @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        // CMD already SENT == NO ACK/cancel .. broken connection? {{{
        if     (    (last_sent_cmd.startsWith(new_cmd))
                && !(last_sent_cmd.endsWith  (CMD_ACK))
               )
        {
            if((++last_send_count % MAX_SEND_ATTEMPTS_COUNT) == 0)
            {
                if(D) log_center("send("+new_cmd+") GOT STUCK "+ last_send_count +" times ...CLOSING");
                disconnect("send("+new_cmd+") (#"+last_send_count+")");
                return;
            }
        }
        // WRITE DONE =============================================}}}
        // CMD ACK ================================================{{{
        else if(   (last_sent_cmd.startsWith(new_cmd))
                && (last_sent_cmd.endsWith  (CMD_ACK))
               ) {
            // clean up
            if(D) log_center("send("+new_cmd+") GOT ACK ...DONE");
            last_send_count     = 0;
            last_sent_cmd       = "";
            new_cmd             = "";
               }
        // ========================================================}}}
        // CMD CANCELED ==========================================={{{
        else if    (last_sent_cmd.equals(new_cmd + CMD_CANCELED))
        {
            // restart
            if(D) log_center("send("+new_cmd+") GOT CANCELED ...RETRYING");
            last_sent_cmd       = "";

        }
        // NOT ACK'ed .. NOT CANCELED =============================}}}
        // CMD NOT SENT YET ======================================={{{
        else
        {
            last_sent_cmd = new_cmd+" "+args;
            last_sent_cmd = last_sent_cmd.trim();
            send_cmd(       last_sent_cmd );
        }
        // CMD SENT ===============================================}}}
        // WRITE @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        // READ @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        // READTASK ================= ...new for a reply or an ack {{{
        if(!last_sent_cmd.equals(""))
        {
            if(ReadTask == null) {
                if(D) log_right("new ReadTask for a reply to ["+ last_sent_cmd +"]");
                last_send_count  = 1;
                ReadTask = new AsyncReadTask();

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    ReadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                else
                    ReadTask.execute();
            }
            // else .. the default polling ReadTask to catch the reply!

            if(D) log_nonl( get_connection_state_symbol() ); //log("r");
        }
        // ========================================================}}}
        // READ @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        // CLOSE @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        // CLOSE =================================== broken socket {{{
        if( !socket_isReady() )
        {
            if(D) log_center("send(" + new_cmd + ") CONNECTION BROKEN ...CLOSING");
            disconnect("send: !socket_isReady");

        }
        // ========================================================}}}
        // CLOSE @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    }
    // }}}
    // ReadTask {{{
    private class AsyncReadTask extends AsyncTask<String, String, String>
    {
        @Override protected String doInBackground(String... params)// {{{
        {
            String result = "";
            last_read_count = 0;

            // shunt server request-reply with a short answer data circuit coming from profile storage {{{
            if(have_answer_to(  last_sent_cmd, "ReadTask")) {
                build_answer_to(last_sent_cmd, "ReadTask");
                return result;
            }
            //}}}
            // read server reply {{{
//*READ*/Settings.MON(TAG_READ, "READTASK"                    //TAG_READ
//*READ*/            , (isConnected()    ? "[CONNECTED]" : "")//TAG_READ
//*READ*/            + (isCancelled()    ? "[CANCELLED]" : "")//TAG_READ
//*READ*/            + " last_sent_cmd=["+ last_sent_cmd.replace("\n","\\n") +"]" //TAG_READ
//*READ*/            +   " StoredReply=["+ StoredReply   +"]" //TAG_READ
//*READ*/            );                                       //TAG_READ

            try {
                while(      isConnected()
                        && !isCancelled()
                        && !(result.contains("***")) // failed to read from socket
                        && !last_sent_cmd.endsWith  ( CMD_ACK  ) // cmd reply
                        && !StoredReply  .startsWith( CMD_POLL ) // dispatched data
                     )
                {
                    result += read_reply();

                    if((++last_read_count % MAX_READ_ATTEMPTS_COUNT) == 0)
                    {
                        if(        !last_sent_cmd.equals(CMD_POLL  )
                                && !last_sent_cmd.equals(CMD_SIGNIN)
                          ) {
                            result += "@@@ ReadTask no answer to ["+ last_sent_cmd +"] after "+ last_read_count +" attempts\n";
                            last_read_count = 0;
                            break;
                          }
                    }
                }
            }
            catch(Exception ex) { result += "*** ReadTask    ["+ex.getMessage()+"]\n"; }
            //}}}
            // answer may come for some [last_sent_cmd] that may have been changed after call time
//*READ*/Settings.MON(TAG_READ, "READTASK"                      //TAG_READ
//*READ*/            , (isConnected()    ? "[CONNECTED]" : "")//TAG_READ
//*READ*/            + (isCancelled()    ? "[CANCELLED]" : "")//TAG_READ
//*READ*/            + " last_sent_cmd=["+ last_sent_cmd.replace("\n","\\n")  +"]" //TAG_READ
//*READ*/            +        " result=["+ result        +"]" //TAG_READ
//*READ*/            );                                       //TAG_READ
            return result;
        }
        //}}}
        //      @Override protected void onPreExecute() { }
        @Override protected void onPostExecute(String result)// {{{
        {
            ReadTask = null;

            // disconnect
            if(result.contains("***")) {
                disconnect("ReadTask ["+result+"]");
            }
            else if( !socket_isReady() ) {
                disconnect("ReadTask !socket_isReady");
            }

            // result diag
            if(result.contains("***")) {
                mRTabs.sync_notify_from("ReadTask("+last_sent_cmd+"): ["+result+"]");
            }
            // cmd reply
            else if( has_last_sent_cmd_ack() ) {
                mRTabs.sync_notify_from("ReadTask("+last_sent_cmd+") ACK RECEIVED");
                last_sent_cmd = ""; // ACK REPORTED ONCE
                // CLEANED ACK UP SO THAT SENDING THE SAME COMMAND AGAIN
                // WILL NOT BE DISCARDED .. AS BEING ACKNOWLEGED EVEN BEFORE BEING SENT
            }
            // polled data
            else if( StoredReply.startsWith( CMD_POLL )) {
                Settings.parse_OOB_CMD(StoredReply);
                mRTabs.sync_notify_from("ReadTask("+last_sent_cmd+"): ["+StoredReply+"] OOB=["+Settings.OOB_CMD+"]");
            }
        }
        //}}}
        @Override protected void onCancelled() // {{{
        {
            ReadTask    = null;
            if(D) log_center("*** ReadTask canceled ***\n");
            last_sent_cmd   += CMD_CANCELED;
        }
        //}}}
    }
    //  }}}
    // WorkerTask {{{
    private class AsyncWorkerTask extends AsyncTask<String, String, String>
    {
        @Override protected String doInBackground(String... params)
        {
            String result = "*** TODO: AsyncWorkerTask ("+ params[0]+") NOT SUPPORTED YET";
            try {
                if( params[0].equals("WOL") )
                {
                    //..................................IP,    SUBNET,       MAC
                    result = WOL.SendMagicPacket(params[1], params[2], params[3]);
                }
            }
            catch(Exception ex) { result += "*** WOL.SendMagicPacket: ["+ex.getMessage()+"]"; }
            return result;
        }
        @Override protected void onPostExecute(String result)
        {
            WorkerTask = null;
            log( result );
            mRTabs.toast_long( result );
        }
    }
    //  }}}
    // send_cmd {{{
    private void send_cmd(String msg)
    {
        String result   = "";

        // no need to ask server
        if(have_answer_to(msg, "send_cmd")) {
            result = "REQUEST ["+ msg +"] REDIRECTED TO USING ["+Settings.PROFILE+"]";
        }
        else {
            try {
                synchronized(Mutex_SOCKET)
                {
                    if(D) log_right("SENDING REQUEST: ["+ msg +"]");
                    PrintWriter socket_out =
                        new PrintWriter(
                                new BufferedWriter(
                                    new OutputStreamWriter( socket.getOutputStream()))
                                , true); // autoflush!

                    socket_out.println(msg);
                    socket_out.flush();
                }
                result = "REQUEST SENT: ["+ msg +"]";
            }
            catch(Exception ex) { result += "*** send_cmd    ["+ex.getMessage()+"]"; }
        }

        if(D) log( result );
        //warn_to_dash("send_cmd", result);
    }
    // }}}
    // send_ACK {{{
    private void send_ACK(String cmd)
    {
        send_cmd(cmd +" "+ Settings.ACK);
    }
    //}}}
    //}}}
    /** BATTERY */
    //{{{
    private static final int      POWER_SUPPLY_BATTERY_REPORT_COUNT_MAX = 5;
    private static String         POWER_SUPPLY_BATTERY_UEVENT_FILE_PATH = "/sys/class/power_supply/battery/device/power_supply/battery/uevent";
    private static String         SYMBOL_THREE_BATTERIES = Settings.SYMBOL_BATTERY +Settings.SYMBOL_BATTERY +Settings.SYMBOL_BATTERY;

    private        String         Battery_alarm       =   "";
    private        BatteryPoll    mBatteryPoll        = null;
    private        BatteryManager mBatteryManager     = null;
    private static Intent         sticky_intent       = null;
    private static Activity       sticky_activity     = null;

    private  class BatteryPoll extends BroadcastReceiver
    {
        // BroadcastReceiver {{{
        /*{{{*/
        private String  status_message    =    "";
        private boolean registered        = false;
        private int     registered_count  =     0;
      //private int     reports_remaining =     0;

        /*}}}*/
        /* registerReceiver {{{*/
        public boolean registerReceiver()
        {
//*POLL*/Settings.MOC(TAG_POLL, "...registerReceiver");
            if(!registered) {

                if( sticky_intent == null) {
                    sticky_activity = RTabs.activity;
                    sticky_intent   = sticky_activity.registerReceiver(this, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                }
                registered         = true;

                registered_count  += 1;
              //reports_remaining  = POWER_SUPPLY_BATTERY_REPORT_COUNT_MAX;
                status_message     = "Battery monitor started at "+get_POLL_timeStamp();

//*POLL*/Settings.MOC(TAG_POLL, "BatteryPoll: ..REGISTERED x"+registered_count);

                if(sticky_intent  != null)
                    onReceive(sticky_activity, sticky_intent);
            }
            else {
//*POLL*/Settings.MOC(TAG_POLL, "BatteryPoll: ..REGISTERED ALREADY");
            }
            return registered;
        }
        /*}}}*/
        /* unregisterReceiver {{{*/
        public void unregisterReceiver()
        {
            if(Battery_alarm != "")
            {
//*POLL*/Settings.MOC(TAG_POLL, "BatteryPoll: NOT UNREGISTERED DURING A Battery_alarm: ["+Battery_alarm+"]");
            }
            else if(registered) {
                try {
                    sticky_activity.unregisterReceiver(this);
                }
                catch(Exception ex) { System.err.println("BatteryPoll: "+ex.getMessage()); }

                registered        = false;
                status_message    = "Battery monitor stopped at "+get_POLL_timeStamp()+" (click to resume)";

//*POLL*/Settings.MOC(TAG_POLL, "BatteryPoll: UNREGISTERED x"+registered_count);
            }
            else {

//*POLL*/Settings.MOC(TAG_POLL, "BatteryPoll: UNREGISTERED ALREADY");
            }
        }
        /*}}}*/
        /* onReceive {{{*/
        private int onReceive_count = 0;

        public void onReceive(Context context, Intent intent)
        {
            ++onReceive_count;
            battery_changed( intent );
        }
        /*}}}*/
        //}}}
        // battery_changed {{{
        private String battery_changed_results = "";

        private void   battery_changed(Intent intent_battery)
        {
            // EXTRA {{{
            // public static final String EXTRA_CHARGE_COUNTER          = "charge_counter";
            // public static final String EXTRA_HEALTH                  = "health";
            // public static final String EXTRA_ICON_SMALL              = "icon-small";
            // public static final String EXTRA_INVALID_CHARGER         = "invalid_charger";
            // public static final String EXTRA_LEVEL                   = "level";
            // public static final String EXTRA_MAX_CHARGING_CURRENT    = "max_charging_current";
            // public static final String EXTRA_MAX_CHARGING_VOLTAGE    = "max_charging_voltage";
            // public static final String EXTRA_PLUGGED                 = "plugged";
            // public static final String EXTRA_PRESENT                 = "present";
            // public static final String EXTRA_SCALE                   = "scale";
            // public static final String EXTRA_STATUS                  = "status";
            // public static final String EXTRA_TECHNOLOGY              = "technology";
            // public static final String EXTRA_TEMPERATURE             = "temperature";
            // public static final String EXTRA_VOLTAGE                 = "voltage";

            int                    status = intent_battery.getIntExtra(    BatteryManager.EXTRA_STATUS            , -1);
            int               temperature = intent_battery.getIntExtra(    BatteryManager.EXTRA_TEMPERATURE       , -1);
            int                   voltage = intent_battery.getIntExtra(    BatteryManager.EXTRA_VOLTAGE           , -1);
            int                chargePlug = intent_battery.getIntExtra(    BatteryManager.EXTRA_PLUGGED           , -1);
            int                     level = intent_battery.getIntExtra(    BatteryManager.EXTRA_LEVEL             , -1);
            int                     scale = intent_battery.getIntExtra(    BatteryManager.EXTRA_SCALE             , -1);
            float         battery_percent = (100F * level) / (float)scale;
            //}}}
            // BATTERY_PLUGGED {{{
            // public static final int BATTERY_PLUGGED_AC                = 1;
            // public static final int BATTERY_PLUGGED_USB               = 2;
            // public static final int BATTERY_PLUGGED_WIRELESS          = 4;

            boolean             usbCharge = (chargePlug ==                 BatteryManager.BATTERY_PLUGGED_USB         );
            boolean             acCharge  = (chargePlug ==                 BatteryManager.BATTERY_PLUGGED_AC          );
            //}}}
            // BATTERY_STATUS {{{
            // public static final int BATTERY_STATUS_UNKNOWN            = 1;
            // public static final int BATTERY_STATUS_CHARGING           = 2;
            // public static final int BATTERY_STATUS_DISCHARGING        = 3;
            // public static final int BATTERY_STATUS_NOT_CHARGING       = 4;
            // public static final int BATTERY_STATUS_FULL               = 5;
            boolean       battery_is_full = (    status ==                 BatteryManager.BATTERY_STATUS_FULL         );
            boolean          not_charging = (    status ==                 BatteryManager.BATTERY_STATUS_NOT_CHARGING );
            boolean           is_charging = (    status ==                 BatteryManager.BATTERY_STATUS_CHARGING     );
            boolean           discharging = (    status ==                 BatteryManager.BATTERY_STATUS_DISCHARGING  );

            //}}}
            // BATTERY_PROPERTY {{{
            // public static final int BATTERY_PROPERTY_CAPACITY         = 4;
            // public static final int BATTERY_PROPERTY_CHARGE_COUNTER   = 1;
            // public static final int BATTERY_PROPERTY_CURRENT_AVERAGE  = 3;
            // public static final int BATTERY_PROPERTY_ENERGY_COUNTER   = 5;

            //long           current_now = intent_battery.getLongProperty( BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
            //long  max_charging_current = intent_battery.getLongProperty( BatteryManager.EXTRA_MAX_CHARGING_CURRENT  );
            //long  max_charging_voltage = intent_battery.getLongProperty( BatteryManager.EXTRA_MAX_CHARGING_VOLTAGE  );
            //}}}
            //BATTERY_HEALTH {{{
            // public static final int BATTERY_HEALTH_COLD               = 7;
            // public static final int BATTERY_HEALTH_DEAD               = 4;
            // public static final int BATTERY_HEALTH_GOOD               = 2;
            // public static final int BATTERY_HEALTH_OVERHEAT           = 3;
            // public static final int BATTERY_HEALTH_OVER_VOLTAGE       = 5;
            // public static final int BATTERY_HEALTH_UNKNOWN            = 1;
            // public static final int BATTERY_HEALTH_UNSPECIFIED_FAILURE= 6;

            //}}}
            // [battery_changed_results] [Charge Percent Volt Temp] {{{
            String sep = " "+Settings.SYMBOL_ELLIPSIS+" ";

            String plug_state
                =        usbCharge ? " (USB)"
                :         acCharge ?  " (AC)"
                :                          "";

            String c
                =  battery_is_full ?         "FULL" + plug_state
                :     not_charging ? "NOT CHARGING" + plug_state
                :      is_charging ?     "CHARGING" + plug_state
                :      discharging ?  "DISCHARGING" + plug_state
                :                        "CHARGING_STATE_UNKNOWN";

            String p = String.format("%d%%"
                    ,                 (int)battery_percent
                    );

            String v = String.format("%d%s%d"
                    ,                 (voltage / 1000)
                    ,                   "v"
                    ,                     (voltage % 1000 + 50) / 100
                    );

            String t = String.format("%d%s"
                    ,                 ((temperature+5) / 10)
                    ,                   Settings.SYMBOL_DEGREE
                    );

            battery_changed_results
                = Settings.SYMBOL_POWER
                + " "+ Settings.SYMBOL_GEAR+Settings.SYMBOL_0_TO_50[onReceive_count % 50]
                + " " + c
                + sep + p
                + sep + v
                + sep + t
                ;

            //}}}
//*POLL*/Settings.MOC(TAG_POLL, "....battery_changed: ["+Battery_alarm+"] .. battery_percent=["+battery_percent+"]");
            // BATTERY ALARM DONE WHEN FULLY CHARGED {{{
            if( battery_is_full )
            {
                if( !isConnected() )
                    stop_POLL("battery_changed: disconnected and battery_is_full");
            }
            //}}}
        }
        //}}}
        // get_battery_changed_results_and_properties {{{

        public String get_battery_changed_results_and_properties()
        {
//System.err.println(TAG_POLL, "..get_battery_changed_results_and_properties: registered=["+registered+"]");
            if( mBatteryManager == null)
                mBatteryManager = (BatteryManager)RTabs.activity.getSystemService(Context.BATTERY_SERVICE);

            // Remaining battery capacity as an integer percentage
            int  bp_CAPACITY_percent   = mBatteryManager.getIntProperty( BatteryManager.BATTERY_PROPERTY_CAPACITY       );

            // Instantaneous battery current in microampere
            int  bp_CURRENT_NOW_uA     = mBatteryManager.getIntProperty( BatteryManager.BATTERY_PROPERTY_CURRENT_NOW    );

            // Average battery current in microamperes
            int  bp_CURRENT_AVERAGE_uA = mBatteryManager.getIntProperty( BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE);

            // Remaining battery capacity in microampere/hour
            int  bp_CHARGE_COUNTER_uAH = mBatteryManager.getIntProperty( BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER );

            // Remaining energy in nanowatt-hours
            int  bp_ENERGY_COUNTER_nWH = mBatteryManager.getIntProperty( BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER );

            String sym = Settings.SYMBOL_BATTERY;
            String avg = Settings.SYMBOL_SIGMA;
            String battery_properties
                =       String.format("%s %s:"         , sym, get_POLL_timeStamp() + " PROPERTIES")
                + "\n"+ String.format("%s %16s = %s"   , sym, "Capacity"    , (bp_CAPACITY_percent  ==  100) ? SYMBOL_THREE_BATTERIES : (bp_CAPACITY_percent+"%"))
                + "\n"+ String.format("%s %16s = %dmA" , sym, "Current"     , (bp_CURRENT_NOW_uA     / 1000))
                + "\n"+ String.format("%s %16s = %dmA" , sym, "Current "+avg, (bp_CURRENT_AVERAGE_uA / 1000))
                + "\n"+ String.format("%s %16s = %dmAH", sym, "Charge"      , (bp_CHARGE_COUNTER_uAH / 1000))
                + "\n"+ String.format("%s %16s = %dmWH", sym, "Energy"      , (bp_ENERGY_COUNTER_nWH / 1000))
                ;
            //________________________SYM_KEY___VAL_____SYM___123456789_12____VAL____________________________

            String report
                =      battery_changed_results + (registered ? " registered" : " NOT registered")
                +"\n"+ battery_properties
                +"\n"+ get_battery_uevent_log()
                ;

            // SELF REGISTER .. f(FULLY CHARGED)
//          if(registered && (Battery_alarm != ""))
//          {
//              reports_remaining   -= 1;
//              if(reports_remaining < 1) unregisterReceiver();
//          }

            if( !registered )
                mBatteryPoll.registerReceiver();

            // REPORT CURRENT ALARM SOUND {{{
            if(bp_CAPACITY_percent  ==  100) Battery_alarm = "";
            else                             Battery_alarm = bp_CAPACITY_percent+"%";

            mRTabs.play_sound_alarm("get_battery_changed_results_and_properties", Battery_alarm);
            //}}}

            return report;
        }
        //}}}
        // parse_battery_uevent_line {{{
/* UEVENT {{{

:!start explorer "https://source.android.com/devices/tech/power/device"

// ----------------------------------------------------------------------
// PS_FILE: [/sys/class/power_supply/battery/power_supply/battery/uevent]
//      STATUS: [STATUS         ] .. [Charging          ]
// CHARGE_TYPE: [CHARGE_TYPE    ] .. [Fast              ]
//     VOLTAGE: [MIN < NOW < MAX] .. [4.20 < 4.29 < 4.35]
//     CURRENT: [      NOW      ] .. [490.8             ]
// ----------------------------------------------------------------------

POWER_SUPPLY_BATT_AGING=0
POWER_SUPPLY_BATT_ID=-22
POWER_SUPPLY_BYPASS_VCHG_LOOP_DEBOUNCER=0
POWER_SUPPLY_CHARGING_ENABLED=1
POWER_SUPPLY_CYCLE_COUNT=441
POWER_SUPPLY_ENABLE_LLK=0
POWER_SUPPLY_ENABLE_SHUTDOWN_AT_LOW_BATTERY=1
POWER_SUPPLY_HEALTH=Good
POWER_SUPPLY_INPUT_CURRENT_SETTLED=1
POWER_SUPPLY_INPUT_CURRENT_TRIM=33
POWER_SUPPLY_INPUT_VOLTAGE_REGULATION=0
POWER_SUPPLY_LLK_SOCMAX=0
POWER_SUPPLY_LLK_SOCMIN=0
POWER_SUPPLY_NAME=battery
POWER_SUPPLY_ONLINE=1
POWER_SUPPLY_PRESENT=1
POWER_SUPPLY_SYSTEM_TEMP_LEVEL=0
POWER_SUPPLY_VOLTAGE_OCV=-22

}}}*/
        //{{{
        private String b_status      = "";
        private String b_charge_type = "";
        private String b_charge_full = "";

        private String b_supV_min    = "";
        private String b_supV        = "";
        private String b_supV_max    = "";

        private String b_supI        = "";
        private String b_supI_max    = "";

        private String b_temp_cool   = "";
        private String b_temp        = "";
        private String b_temp_warm   = "";

        private String b_capacity    = "";
        private String b_technology  = "";

        //}}}
        private void parse_battery_uevent_line(String line)
        {
            /* key val {{{*/
            String[] kv = line.split("=");
            String  key = kv[0];
            String  val = kv[1];

            /*}}}*/
            switch(key)
            {
                // CHARGE: [STATUS FULL TYPE] {{{
                // . POWER_SUPPLY_STATUS                       = Charging
                // . POWER_SUPPLY_CHARGE_TYPE                  = Fast
                // . POWER_SUPPLY_CHARGE_FULL                  = 6684000 .. 6684
                // . POWER_SUPPLY_CHARGE_FULL_DESIGN           = 6680000
                case "POWER_SUPPLY_STATUS"     : b_status      =  val;                                                        break;
                case "POWER_SUPPLY_CHARGE_TYPE": b_charge_type = ((val.equals("N/A")) ? "" : val);                            break;
                case "POWER_SUPPLY_CHARGE_FULL": b_charge_full =  String.format("(Max %.0f)", Integer.parseInt(val) / 1000F); break;

               //}}}
               // VOLTAGE: [MIN MAX NOW] {{{
               // .  POWER_SUPPLY_VOLTAGE_MIN                      = 4200000 .. 4.200000
               // .  POWER_SUPPLY_VOLTAGE_NOW                      = 4298826 .. 4.298826
               // .  POWER_SUPPLY_VOLTAGE_MIN_DESIGN               = 4200000 .. 4.200000
               // .  POWER_SUPPLY_VOLTAGE_MAX_DESIGN               = 4350000 .. 4.350000
                case "POWER_SUPPLY_VOLTAGE_MIN"       : b_supV_min = String.format("%.2f", Integer.parseInt(val) / 1000000F).replace(".","v"); break;
                case "POWER_SUPPLY_VOLTAGE_NOW"       : b_supV     = String.format("%.2f", Integer.parseInt(val) / 1000000F).replace(".","v"); break;
                case "POWER_SUPPLY_VOLTAGE_MAX_DESIGN": b_supV_max = String.format("%.2f", Integer.parseInt(val) / 1000000F).replace(".","v"); break;

                //}}}
                // CURRENT: [NOW MAX] {{{
                // . POWER_SUPPLY_CURRENT_NOW                        = 490891 ..  490
                // . POWER_SUPPLY_INPUT_CURRENT_MAX                  = 100000 .. 1000
                case "POWER_SUPPLY_CURRENT_NOW"      : b_supI      = String.format("%.0fmA", Integer.parseInt(val) / 1000F); break;
                case "POWER_SUPPLY_INPUT_CURRENT_MAX": b_supI_max  = String.format("%.0fmA", Integer.parseInt(val) /  100F); break;

                //}}}
                // TEMP: [COOL TEMP WARM] {{{
                // . POWER_SUPPLY_TEMP                        = 261
                // . POWER_SUPPLY_TEMP_COOL                   = 100
                // . POWER_SUPPLY_TEMP_WARM                   = 450
                case "POWER_SUPPLY_TEMP_COOL"  : b_temp_cool  = String.format("%d%s", Integer.parseInt(val) / 10 , Settings.SYMBOL_DEGREE); break;
                case "POWER_SUPPLY_TEMP"       : b_temp       = String.format("%.1f", Integer.parseInt(val) / 10F, Settings.SYMBOL_DEGREE).replace(".",Settings.SYMBOL_DEGREE); break;
                case "POWER_SUPPLY_TEMP_WARM"  : b_temp_warm  = String.format("%d%s", Integer.parseInt(val) / 10 , Settings.SYMBOL_DEGREE); break;

                //}}}
                // CAPACITY {{{
                // .  POWER_SUPPLY_CAPACITY                   = 98
                case "POWER_SUPPLY_CAPACITY"   : b_capacity   = val.equals("100") ? SYMBOL_THREE_BATTERIES : val+"%"; break;

                //}}}
                // TECHNOLOGY {{{
                // . POWER_SUPPLY_TECHNOLOGY                  = Li-ion
                case "POWER_SUPPLY_TECHNOLOGY" : b_technology = val; break;

                //}}}
            }
        }
        //}}}
        // get_battery_uevent_log {{{
        private String uevent_log_buffer = "";
        private String get_battery_uevent_log()
        {
            if(!registered) {
                if( uevent_log_buffer.startsWith(    Settings.SYMBOL_AC_CURRENT) ) {
                    uevent_log_buffer
                        = uevent_log_buffer.replace( Settings.SYMBOL_AC_CURRENT, Settings.SYMBOL_DOUBLE_VERTICAL_BAR)
                        + "\n"+Settings.SYMBOL_DOUBLE_VERTICAL_BAR +" "+ status_message
                        ;
                }
                return uevent_log_buffer;
            }

            String file_path = POWER_SUPPLY_BATTERY_UEVENT_FILE_PATH;
            ArrayList<String> lines = Settings.get_file_lines( file_path );
            if(lines.size() < 1)
            {
                return "could not read "+file_path;

            }

            for(int i=0; i < lines.size(); ++i)
                parse_battery_uevent_line( lines.get(i) );

            String sym = Settings.SYMBOL_AC_CURRENT;

            uevent_log_buffer
                =       String.format("%s %s:"        , sym, get_POLL_timeStamp() + " SYSTEM LOGS")
                + "\n"+ String.format("%s %16s = %s"  , sym,     b_status  , b_capacity   +" "  + b_charge_type/*+" "+ b_charge_full*/) // Charging 98% Fast (Max 6684000)
                + "\n"+ String.format("%s %16s = %s"  , sym,    "Supply V" , b_supV_min   +" < "+ b_supV        +" < "+ b_supV_max    ) // 4200000 4298826 4350000
                + "\n"+ String.format("%s %16s = %s"  , sym,    "Supply I" , b_supI       +" < "+ b_supI_max                          ) // 490891 100000
                + "\n"+ String.format("%s %16s = %s"  , sym,        "Temp" , b_temp_cool  +" < "+ b_temp        +" < "+ b_temp_warm   ) // 100 < 261 < 450
                + "\n"+ String.format("%s %16s = %s"  , sym,  "Technology" , b_technology                                             )
                ;

            return        uevent_log_buffer
                +         "\n"+Settings.SYMBOL_AC_CURRENT +" "+ status_message
                ;
        }
        //}}}
    }
    //}}}
    /** POLL */
    //{{{
    // {{{
    private static final int POLL_STATUS_UNDEFINED     =  0;
    private static final int POLL_STATUS_NOT_CONNECTED =  1;
    private static final int POLL_STATUS_NOT_READING   =  2;
    private static final int POLL_STATUS_READING_POLL  =  3;
    private static final int POLL_STATUS_READING_REPLY =  4;
    private static final int POLL_STATUS_FREEZED       =  5;
    private static final int POLL_STATUS_OFFLINE       =  6;
    //}}}
    // start_BatteryPoll {{{
    public void start_BatteryPoll(String caller)
    {
//*POLL*/Settings.MOC(TAG_POLL, "start_BatteryPoll("+caller+")");

        if(mBatteryPoll == null) mBatteryPoll = new BatteryPoll();
        mBatteryPoll.registerReceiver();
    }
    //}}}
    // stop_BatteryPoll {{{
    public void stop_BatteryPoll(String caller)
    {
//*POLL*/Settings.MOC(TAG_POLL, "stop_BatteryPoll("+caller+")");

        if(mBatteryPoll != null) mBatteryPoll.unregisterReceiver();
    }
    //}}}
    // start_POLL {{{
    private int  poll_loop_count = 0;

    private void start_POLL(String caller)
    {
        if( !isConnected() ) {
            DashMsgPoll = "*** NOT CONNECTED ***";
            update_dash();
        }

        if(D) log_right("start_POLL("+ caller +"): ...last_sent_cmd=["+ last_sent_cmd +"]");

        // generic poll cmd
        if( has_last_sent_cmd_ack() )
            last_sent_cmd = CMD_POLL;

//      poll_loop_count = 0;

        if(ReadTask == null) {
            if(D) log_right("@@@ new ReadTask starting POLL for ["+caller+"]");
            ReadTask = new AsyncReadTask();

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                ReadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                ReadTask.execute();
        }
        DashMsgPoll = "START";

        update_dash();
    }
    //}}}
    // stop_POLL {{{
    public void stop_POLL(String caller)
    {
        if(D) log_right(caller+".stop_POLL:");

        if(!last_sent_cmd.equals(CMD_POLL))
            return;

        DashMsgPoll = "STOPPED BY "+caller;

        stop_BatteryPoll( caller );

        update_dash();
    }
    //}}}
    // progress_POLL {{{
    public String progress_POLL()
    {
      //if(DashNotePane == null) return Settings.SYMBOL_freezed;
      //else                     return progress_POLL_report();
        return progress_POLL_LOOP();
    }
    //}}}
    // get_battery_fully_charged_SYMBOL {{{
    public String get_battery_fully_charged_SYMBOL()
    {
        if(mBatteryManager == null)        return "";
        int bp_CAPACITY_percent   = mBatteryManager.getIntProperty( BatteryManager.BATTERY_PROPERTY_CAPACITY       );

        String s = Settings.SYMBOL_BATTERY;

        return (bp_CAPACITY_percent >= 99) ?        (s +s +s +s +s)
             : (bp_CAPACITY_percent >= 98) ? "98% "+(   s +s +s +s)
             : (bp_CAPACITY_percent >= 97) ? "97% "+(      s +s +s)
             : (bp_CAPACITY_percent >= 96) ? "96% "+(         s +s)
             : (bp_CAPACITY_percent >= 95) ? "95% "+(            s)
             :                               ""
            ;
    }
    //}}}
    // progress_POLL_LOOP {{{
    private String progress_POLL_LOOP()
    {
//System.err.println(TAG_POLL, "o");
        // POLL COUNT {{{
        poll_loop_count += 1;

        String poll_count = Settings.SYMBOL_0_TO_50[poll_loop_count % 50]+" ";
        //}}}
        /* POLL STATUS .. (OFFLINE FREEZED) {{{*/
        int poll_status         = Settings.OFFLINE ? POLL_STATUS_OFFLINE
            :                     Settings.FREEZED ? POLL_STATUS_FREEZED
            :                                        POLL_STATUS_UNDEFINED
            ;

        /*}}}*/
        // POLL STATUS .. (NOT_CONNECTED NOT_READING) {{{
        String       oo = "";
        if(ReadTask == null)
        {
            if( !isConnected() ) {
                poll_status = POLL_STATUS_NOT_CONNECTED;
                oo          = ((poll_loop_count % 2) == 0) ? " Xx " : "  xX";
            }
            else {
                poll_status = POLL_STATUS_NOT_READING;
                oo          = ((poll_loop_count % 2) == 0) ? " Cc " : "  cC";
                start_POLL("progress_POLL_LOOP");
            }

        }
        //}}}
        if(DashNotePane == null) return oo;
        // POLL STATUS .. (READING_POLL READING_REPLY) {{{
        else {
            if(last_sent_cmd.equals(CMD_POLL)) {
                poll_status = POLL_STATUS_READING_POLL;
                oo          = ((poll_loop_count % 2) == 0) ? " Oo " : "  oO";
            }
            else {
                poll_status = POLL_STATUS_READING_REPLY;
                oo          = ((poll_loop_count % 2) == 0) ? " Rr " : "  rR";
        /*XXX*/ oo         += " ["+last_sent_cmd+"]";
            }

        }
        //}}}
        /* POLL REPORT {{{*/
        String sym_poll_status = (poll_status == POLL_STATUS_OFFLINE      ) ? get_offline_state_symbol() /* OFFLINE OR NOT */
            :                    (poll_status == POLL_STATUS_FREEZED      ) ? get_freezed_state_symbol() /* FREEZED OR NOT */
            :                    (poll_status == POLL_STATUS_NOT_CONNECTED) ? Settings.SYMBOL_polling    /* POLLING */
            :                    (poll_status == POLL_STATUS_NOT_READING  ) ? Settings.SYMBOL_polling    /* ------- */
            :                    (poll_status == POLL_STATUS_READING_POLL ) ? Settings.SYMBOL_polling    /* ------- */
            :                    (poll_status == POLL_STATUS_READING_REPLY) ? Settings.SYMBOL_polling    /* ------- */
            :                                                                 Settings.SYMBOL_polling    /* ------- */
            ;

        String poll_loop_duration = " ("+Settings.Get_time_elapsed(0L, 1000L * poll_loop_count)+")";

        oo
            = poll_count
            + sym_poll_status
            + oo
            + sym_poll_status
            + poll_loop_duration
            ;
        /*}}}*/
        /* BATTERY REPORT {{{*/
        String bb = "";
        if(mBatteryPoll != null)
        {
            bb= mBatteryPoll.get_battery_changed_results_and_properties();
        }
        /*}}}*/
        /* DASH UPDATE {{{*/
        DashMsgPoll = oo + "\n"+bb;

        update_dash();
        /*}}}*/
        /* [MLog.check_log_auto_toggle_off] {{{*/
        if(Settings.LOGGING && !Settings.LOG_CAT)
            MLog.check_log_auto_toggle_off();

        /*}}}*/
        // RETURN ANOMALIES AT END OF BUNCH {{{
        if((poll_loop_count % 10) == 1) {
            switch(poll_status) {
                case POLL_STATUS_NOT_CONNECTED   :  return (String.format(" %s *** POLL NOT CONNECTED\n"              , Settings.SYMBOL_BROKEN_HEART                           ));
                case POLL_STATUS_NOT_READING     :  return (String.format(" %s ooo POLL NOT READING\n  "              , Settings.SYMBOL_no_read_task                           ));
                case POLL_STATUS_READING_POLL    :  return (String.format(" %sx%-3d while polling\n"                  , Settings.SYMBOL_timeout, last_read_count               ));
                case POLL_STATUS_READING_REPLY   :  return (String.format(" %sx%-3d while expecting a reply to [%s]\n", Settings.SYMBOL_timeout, last_read_count, last_sent_cmd.replace("\n","\\n") ));
                case POLL_STATUS_FREEZED         :  return (String.format(" %s *** POLL FREEZED\n"                    , Settings.SYMBOL_freezed                                ));
                case POLL_STATUS_OFFLINE         :  return (String.format(" %s *** POLL OFFLINE\n"                    , Settings.SYMBOL_offline                                ));
                default                          :  return (String.format(" %s *** POLL INITIALIZING\n"               , Settings.SYMBOL_confused                               ));
            }
        }
        else {                                      return( Settings.SYMBOL_polling ); }

        /*}}}*/
    }
    //}}}
    // parse_POLL [DEV_SCALE] {{{
    public void parse_POLL()
    {
        //if(D) log("@@@ parse_POLL:");
        // CHECK CURRENT REPLY CONTENTS {{{
        if( !StoredReply.startsWith(CMD_POLL) )
        {
            if(D) log("*** parse_POLL: StoredReply should contain a ["+ CMD_POLL +"] first-line:");
            if(D) log("*** StoredReply=("+StoredReply.length()+"b) ["+ Settings.ellipsis(StoredReply, 80) +"]");
            return;
        }

        //}}}
        // PARSE POLL REPLY [key=val] {{{
        String[] lines = StoredReply.split("\n");
        int             len = lines.length;
        for(int i=0; i< len; ++i)
        {
            //if(D) log("...lines["+ i +"]=["+lines[i]+"]");

            if(        (lines[i].startsWith(CMD_POLL))
                    && (lines[i].endsWith  (CMD_ACK ))
              )
                break;

            // APPLY POLLED SERVER KEY_VAL CHANGES TO CURRENT WORKING PROFILE
            if(Settings.Working_profile_instance != null)
                parse_KEY_VAL(lines[i], "parse_POLL");
        }
        //}}}
        // CONSUME PARSED DATA {{{
        StoredReply = ""; // CONSUME PARSED DATA

        DashMsgPoll = "KEY_VAL PARSED";
        update_dash();

        //}}}
        mRTabs.sync_notify_from("POLL ...DONE");
    }
    //}}}
    /*_ get_POLL_timeStamp {{{*/
    private String get_POLL_timeStamp()
    {
        return "POLL" + Settings.SYMBOL_0_TO_50[poll_loop_count % 50];
    }
    /*}}}*/
    /*_ battery_poll_resume {{{*/
    private boolean battery_poll_resume()
    {
        if(    DashNotePane == null   ) return false;
        if(    mBatteryPoll == null   ) return false;
        if(    mBatteryPoll.registered) return false;

        return mBatteryPoll.registerReceiver();
    }
    /*}}}*/
    //}}}
    /** CONNECT */
    // {{{ // http://developer.android.com/reference/android/os/AsyncTask.html
    // ConnectTask {{{
    private class AsyncConnectTask extends AsyncTask<String, String, String>
    {
        // data {{{
        // http://developer.android.com/reference/android/os/AsyncTask.html
        private String unsent_cmd;
        private String unsent_args;
        private String last_attempted_IP   = "";
        private int    last_attempted_port = 0;

        @Override public String toString() { return "ConnectTask IP=["+ last_attempted_IP +"] Port=["+ last_attempted_port +"]"; }
        // }}}
        // doInBackground {{{
        @Override protected String doInBackground(String... params)
        {
            String caller = "ConnectTask";
//*CONNECT*/Settings.MOC(TAG_CONNECT, caller);
//*CONNECT*/Settings.MOM(TAG_CONNECT, "["+Settings.SERVER_IP+":"+Settings.SERVER_PORT+"]");

            unsent_cmd  = params[0];
            unsent_args = params[1];

            String result = "ConnectTask:";

/*
            // XXX simulating_unreachable_network {{{
            {
                try { Thread.sleep(500); } catch(Exception ignored) { }
                return "*** [SIMULATING_UNREACHABLE_NETWORK] ***";
            }
            // XXX }}}
*/

            //socket          = null;
            last_connect_count = 0;
            while(last_connect_count < MAX_CONNECTION_ATTEMPTS_COUNT)
            {
                Socket mSocket  = new Socket();

                // TRY CURRENT IP PORT {{{
                last_attempted_IP       = Settings.SERVER_IP;
                last_attempted_port     = Settings.SERVER_PORT;
                InetAddress serverAddr  = null;
                try {
                    serverAddr  = InetAddress.getByName( last_attempted_IP );
                }
                catch(UnknownHostException ex) { result += " ["+ex+"]\n"; ex.printStackTrace(); }
                if(serverAddr == null) {
//*CONNECT*/Settings.MON(TAG_CONNECT, caller, "(serverAddr == null): "+result);
                    return result;
                }

                //log("...serverAddr=["+ serverAddr +"]");
                //}}}

                // CONNECT ATTEMPT {{{
                long connect_time = System.currentTimeMillis();

                boolean mSocket_isConnected = false;
                try {
                    mSocket.connect(new InetSocketAddress(serverAddr, last_attempted_port), CONNECT_TIMEOUT);
                    mSocket_isConnected     = true;
                }
            //  catch(SocketException ex) { /* result += " ["+ex+"]\n"; ex.printStackTrace(); */ }
                catch(IOException     ex) { /* result += " ["+ex+"]\n"; ex.printStackTrace(); */ }

                last_connect_count         += 1;

                //log("mSocket_isConnected=["+ mSocket_isConnected +"]");

                // NO HURRY
                if((System.currentTimeMillis() - connect_time) < 500) try { Thread.sleep( 500 ); } catch(Exception ignored) { }
                //}}}
                // ALREADY CONNECTED, DONE - possibly by another task ? {{{
                if(socket != null)
                {
                    log("*** CONNECTION ALREADY ESTABLISHED AT THIS POINT .. CLOSING EXTRA SOCKET ***");
                    try { mSocket.close(); } catch(Exception ignored) { }
                    this.cancel(false);
                    publishProgress("connection already established");
                    break;
                }
                //}}}
                // CONNECTED, DONE {{{
                if( mSocket_isConnected )
                {
                    socket = mSocket;
                    try {
                        socket.setSoTimeout( SOCKET_SETSOTIMEOUT );
                    }
                    catch(SocketException ex) { result += " ["+ex+"]\n"; ex.printStackTrace(); }
                    result += " socket="+ socket.toString() + "... SoTimeout="+ SOCKET_SETSOTIMEOUT;
                    publishProgress("connection done");
                    break;
                }
                //}}}
                // NEXT PORT IN RANGE - MAY ALSO HAVE BEEN CHANGED BY USER {{{
                else {
                    // 1/2 USER MAY HAVE CHANGED SERVER PORT DURING LAST CONNECTION TIMOUT
                    String progress_msg
                        = "ConnectTask #"+last_connect_count
                        +" COULD NOT REACH SERVER .. ON "+Settings.SERVER_IP+":"+last_attempted_port
                        ;

                    //if((Settings.SERVER_PORT != last_attempted_port) || (Settings.SERVER_IP != last_attempted_IP)) {
                        //last_connect_count = 0;
                    //}
                    //// 2/2 SELECT ANOTHER PORT IN THE RANGE
                    //else {
                        Settings.selectNextPortInRange();
                        progress_msg += ".. SWITCHING TO PORT "+Settings.SERVER_PORT;
                    //}

                    // UI UPDATE
//*CONNECT*/Settings.MOM(TAG_CONNECT, progress_msg );
                    publishProgress( progress_msg );
                }
                //}}}
            }

//*CONNECT*/Settings.MON(TAG_CONNECT, caller, result);
            return result;
        }
        //}}}
        // onProgressUpdate {{{
        protected void onProgressUpdate(String... progress_msg)
        {
            //log("ConnectTask.onProgressUpdate:"+ progress_msg[0] );
        //  mRTabs.show_np_server(); // wrong thread teaser XXX
            mRTabs.sync_np_server(); // TODO (way to go!)
        }
        //}}}
        // onPostExecute {{{
        @Override protected void onPostExecute(String result)
        {
            String caller = "ConnectTask.onPostExecute";
//*CONNECT*/Settings.MOC(TAG_CONNECT, caller);
//*CONNECT*/Settings.MOM(TAG_CONNECT, "IP=["+ last_attempted_IP +"] Port=["+ last_attempted_port +"]");
            //  if(D) log_center( this.toString() );

            if(result.contains("***"))
                result = result.replace(":",":\n*** ");
//*CONNECT*/Settings.MOM(TAG_CONNECT, result);

            // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            // WORKING CONNECTION NOT WORKING ANYMORE
            // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            boolean success = socket_isReady();
//*CONNECT*/Settings.MOM(TAG_CONNECT, "success=["+success+"]");

            if( !success )
            {
                int port_from = (Settings.SERVER_PORT / 10 * 10);
                String msg =
                    String.format("ConnectTask: NO SERVER ON PORT-RANGE %d-%d"
                            , port_from
                            , port_from + 10
                            );
                disconnect( msg );
                mRTabs.sync_notify_from("ConnectTask("+last_sent_cmd+"): *** "+ msg +" ***");

            }
            else if( !unsent_cmd.equals("") )
            {
//*CONNECT*/Settings.MOM(TAG_CONNECT, "unsent_cmd=["+ unsent_cmd +"] unsent_args=["+ unsent_args +"]");
                /*
                   send(CMD_SIGNIN, Settings.DEVICE, "ConnectTask");
                 */
                send(unsent_cmd, unsent_args    , "ConnectTask");
            }
            // we are connected .. set green working context condition start point
            else {
                mRTabs.sync_GUI_colors("ConnectTask.onPostExecute");
                //send(CMD_SIGNIN, Settings.DEVICE, "ConnectTask");
                //start_POLL("Called by ConnectTask");
                mRTabs.sync_notify_from("ConnectTask("+last_sent_cmd+"): "+socket.toString()+"]");
            }

            // may launch another
            ConnectTask = null;

            // save successful connection parameter settings
            if( success ) Settings.SaveSettings("ConnectTask.onPostExecute");

            // CONNECTED: synchronize current working profile with server
//*CONNECT*/Settings.MOM(TAG_CONNECT, "CONNECTED: ...Working_profile=["+Settings.Working_profile+"]");
/*
                if( !TextUtils.isEmpty( Settings.Working_profile ) )
                {
//CONNECT//Settings.MOM(TAG_CONNECT, "CONNECTED: ...calling sync_PROFILE("+Settings.Working_profile+")");
                    sync_PROFILE( Settings.Working_profile );
                }
*/
        }
        //}}}
        // onCancelled {{{
        @Override protected void onCancelled()
        {
//*CONNECT*/Settings.MOM(TAG_CONNECT, "*** CANCELED ConnectTask IP=["+ last_attempted_IP +"] Port=["+ last_attempted_port +"]");
            ConnectTask = null;
        }
        //}}}
    }
    // }}}
    // connect {{{
    private void connect(String unsent_cmd, String unsent_args)
    {
        if(D) {
            String syms = Settings.SYMBOL_NO_ENTRY + Settings.SYMBOL_NO_ENTRY + Settings.SYMBOL_NO_ENTRY;

            log("@@@ "+ syms +" connect: ["+ Settings.SERVER_IP +":"+ Settings.SERVER_PORT +"] last_connect_count=["+ last_connect_count +"]");

            log_right(""
                    +"new ConnectTask #"+ last_connect_count +":"
                    +" ["+ Settings.SERVER_IP + ":" + Settings.SERVER_PORT +"]"
                    +" ["+         unsent_cmd +"]+["+ unsent_args          +"]"
                    );
        }

        if(ConnectTask != null) {
            ConnectTask.cancel(true);   // mayInterruptIfRunning
            try { Thread.sleep( 1000 ); } catch(Exception ignored) { }
        }
        ConnectTask = new AsyncConnectTask();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            ConnectTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, unsent_cmd, unsent_args);
        else
            ConnectTask.execute          (                                unsent_cmd, unsent_args);

        mRTabs.sync_GUI_colors("connect");
    }
    // }}}
    // disconnect {{{
    public void disconnect(String caller)
    {
        caller += "] [disconnect";
//*CONNECT*/Settings.MOC(TAG_CONNECT, caller);

        String diag = "";
        // POLL
        if( last_sent_cmd.equals(CMD_POLL) ) {
            stop_POLL(caller);
            diag += " stop_POLL called";
        }

        // SEND
        last_sent_cmd   = "";           // nothing to be considered sent past this point

        // CONNECT
        if(ConnectTask != null) {
            ConnectTask.cancel(true);   // mayInterruptIfRunning
            diag += ConnectTask.toString() +" canceled";
            ConnectTask = null;         // forget this one so we can launch another
        }
        if(Settings.ServerID != "")
            diag += " Settings.ServerID ["+ Settings.ServerID +"] cleared";
        Settings.ServerID        = "";           // signed in

        // READ
        if(ReadTask != null) {
            ReadTask.cancel(true);      // mayInterruptIfRunning
            diag += " ReadTask canceled";
        }

        // SOCKET
        if((socket != null) && isConnected()) {
            try {
                send_cmd(CMD_SIGNOUT +" "+ Settings.ServerID + " ["+caller+"]");
                socket.close();
                diag += " "+CMD_SIGNOUT+" sent .. socket closed";
            }
            catch(Exception ex) {
                diag += " "+ex.getMessage();
//*CONNECT*/Settings.MON(TAG_CONNECT, caller, diag);
            }
            socket = null;
        }

        mRTabs.sync_GUI_colors("disconnect");

        if(diag != "")
            mRTabs.sync_notify_from("DISCONNECTED BY "+caller+":"+ diag);
        else
            mRTabs.sync_notify_from("NOT CONNECTED ON "+caller);
    }
    //}}}
    public boolean isConnected()// {{{
    {
        return socket_isReady();// && (ConnectTask == null);
    }
    // }}}
    public boolean is_signed_in()// {{{
    {
        return isConnected() && (!Settings.ServerID.equals(""));
    }
    // }}}
    // parse_SIGNIN {{{
    private String SIGNIN_ERROR_MESSAGE = "offline";
    public  String get_SIGNIN_ERROR_MESSAGE()   { return SIGNIN_ERROR_MESSAGE;       }
    private   void clear_SIGNIN_ERROR_MESSAGE() {        SIGNIN_ERROR_MESSAGE = "";  }

    public void parse_SIGNIN()
    {
        //if(D) log("@@@ parse_SIGNIN:");
        if( !StoredReply.startsWith(CMD_SIGNIN) ) {
            if(D) log("*** parse_SIGNIN: StoredReply should contain a ["+ CMD_SIGNIN +"] first-line:");
            if(D) log("*** StoredReply=("+StoredReply.length()+"b) ["+ Settings.ellipsis(StoredReply, 80) +"]");
            return;
        }

        // 000000 111111 2222222222222 33333333333333
        // SIGNIN PCIVAN 255.255.255.0 CB-4E-8D-91-BB          ACK   -- success
        // SIGNIN SGP512 password      WRONG          PASSWORD ACK   -- failure
        String         lines[] = StoredReply.split("\n");

        String         words[] = lines[0].split(" ");
        String arg1 = (words.length > 1) ? words[1] : "";    // PCIVAN
        String arg2 = (words.length > 2) ? words[2] : "";    // SUBNET
        String arg3 = (words.length > 3) ? words[3] : "";    // MAC     || [WRONG] for a rejected password

        if(        arg1.toLowerCase().startsWith("wrong") || arg1.startsWith("***")
                || arg2.toLowerCase().startsWith("wrong") || arg2.startsWith("***")
                || arg3.toLowerCase().startsWith("wrong") || arg3.startsWith("***")
          ) {
            SIGNIN_ERROR_MESSAGE   = arg1 +" "+ arg2 +" "+ arg3;
          }
        else if(   !arg1.equals("")
                || !arg2.equals("")
                || !arg3.equals("")
          ) {
            Settings.ServerID      = arg1;
            Settings.SERVER_SUBNET = arg2;
            Settings.SERVER_MAC    = arg3;
            SIGNIN_ERROR_MESSAGE   = "";
        }

        if(D) log("parse_SIGNIN: [ServerID]-[SUBNET]-[MAC]=["+ Settings.ServerID +"]-["+ Settings.SERVER_SUBNET +"]-["+ Settings.SERVER_MAC +"]");
        if(D) log("parse_SIGNIN: [SIGNIN_ERROR_MESSAGE]=["+ SIGNIN_ERROR_MESSAGE +"]");

        StoredReply = ""; // CONSUME PARSED DATA

        //warn_to_dash("parse_SIGNIN", "SIGNED IN ON ["+ Settings.ServerID +"] ["+ Settings.SERVER_IP +":"+ Settings.SERVER_PORT +"]");
    }
    //}}}
    // has_max_connection_failed {{{
    public boolean has_max_connection_failed()
    {
        return (last_connect_count >= MAX_CONNECTION_ATTEMPTS_COUNT);
    }
    public void clear_max_connection_failed(String caller)
    {
        if(D) log("clear_max_connection_failed"+Settings.SYMBOL_LEFT_ARROW+caller);
        last_connect_count = 0;
    }
    //}}}
    //}}}
    /** SOCKET */
    //{{{
    private String read_reply()// {{{
    {
        String result = "[byte "+StoredReply.length()+"] ";
        try {
            InputStream         is  = socket.getInputStream(     );
            InputStreamReader   isr = new InputStreamReader( is  );
            BufferedReader      br  = new BufferedReader   ( isr );

            String s = null;
            do {
                s = br.readLine();
                if(s != null)
                {
                    result = ""; // clear failed to read report

                    if(!StoredReply.equals(""))
                        StoredReply += "\n";
                    StoredReply += s;

                    if(        s.contains  ( CMD_ACK  )
                            || s.startsWith( CMD_POLL )  // dispatched data (expected to be a one-liner)
                      ) {
                        add_last_sent_cmd_ack();
                        break;
                      }

                }
            }
            while(s != null);   // not a timeout as it is handled by a catch below
            if(s == null)
                result += "*** SOCKET READ FAILED BEFORE TIMEOUT ***";
        }
        catch(SocketTimeoutException ex) {
            if(StoredReply.length() > 0) // missing EOT
                result += "*** timeout on ["+ Settings.ellipsis(StoredReply, 80) +"]\n";
            else
                result = "";    // not an error for an empty StoredReply
        }
        catch(Exception ex) {
            result += "*** ["+ex.getMessage()+"]\n";
            try { socket.close(); } catch(Exception ignored) { }
            socket = null;
        }

        //if(result != "") result = "*** read_reply "+result;
        return result;
    }
    // }}}
    private boolean socket_isReady() // {{{
    {
        return  (socket != null)
            &&  socket.isBound()
            &&  socket.isConnected()
            && !socket.isClosed()
            && !socket.isInputShutdown()
            && !socket.isOutputShutdown()
            ;

    }
    //}}}
    private String socket_log() // {{{
    {
        String                                state = "";
        if     ( socket == null             ) state = "null";
        else if(!socket.isBound()           ) state = "not bound";
        else if( socket.isConnected()       ) state = "not Connected";
        else if(!socket.isClosed()          ) state =     "Closed";
        else if(!socket.isInputShutdown()   ) state =     "InputShutdown";
        else if(!socket.isOutputShutdown()  ) state =     "OutputShutdown";
        else                                  state =     "Connected";

        return "Socket is "+state;
    }
    //}}}
    //}}}
    /** KEY_VAL  */
    // parse_KEY_VAL {{{
    private void parse_KEY_VAL(String argLine, String caller)
    {
        // 1/2 - PROFILE SYNC [DEVICE] <=> [SERVER] {{{
        caller += "] [parse_KEY_VAL("+argLine+")";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);

        if( check_profile_device_server_sync(argLine, caller) )
            return;

        //}}}
        // 2/2 - SET KEY_VAL .. possibly [SENT TO] or [RECEIVED FROM] the SERVER {{{
        List<String> changedSettingsList = Settings.set_KEY_VAL(argLine, caller);

        //}}}
        // APPLY [DEV_W] .. (adjust Settings.DEV_SCALE) {{{
        if( changedSettingsList.contains("DEV_W") )
        {
            // DISPLAY_W and DISPLAY_H .. f(DEV_W) {{{
            DisplayMetrics displaymetrics = new DisplayMetrics();
            Display        display        = RTabs.activity.getWindowManager().getDefaultDisplay();
            display.getMetrics( displaymetrics );

            Point p = new Point(); display.getRealSize(p);
            Settings.set_DISPLAY_WH(p.x, p.y);

            //}}}
            // DEV_SCALE .. f(DEV_W and DISPLAY_W) {{{
            if(Settings.DEV_W  != 0)
            {
                Settings.DEV_SCALE  = (float)(Settings.DISPLAY_W) / (float)Settings.DEV_W;
                if(D) log("DEV_SCALE=["+ Settings.DEV_SCALE +"]");
            }
            //}}}
            // LOG {{{
            if(D) {
                log(String.format("=== %16s = %d"     ,      "DEV_W", Settings.DEV_W                         ));
                log(String.format("=== %16s = %d"     , "densityDpi", displaymetrics.densityDpi              ));
                log(String.format("=== %16s = %g"     ,  "DEV_SCALE", Settings.DEV_SCALE                     ));
                log(String.format("=== %16s = %d x %d", "DISPLAY_WH", Settings.DISPLAY_W, Settings.DISPLAY_H ));
            }

            // warn_to_dash("parse_KEY_VAL", "DISPLAY ["+ Settings.DISPLAY_W+"x"+Settings.DISPLAY_H +"] SCALE=["+ Settings.DEV_SCALE +"]");

            //}}}
            // LAYOUT {{{
            if(!Settings.Working_profile.equals("WORKBENCH/WORKBENCH")) // .. (do not apply while editing)
            {
                if(D) log("@@@ APPLYING argLine=["+ argLine +"]:");

                mRTabs.request_TABS_LAYOUT("parse_KEY_VAL("+argLine+")");
            }
            //}}}
        }
        //}}}
        // APPLY [PALETTE] [OPACITY] [MAXCOLORS] [GUI_STYLE] [GUI_FONT] [GUI_TYPE] {{{
        if(        (PALETTES_Map.size() > 0)
                && (   changedSettingsList.contains("PALETTE"  )
                    || changedSettingsList.contains("OPACITY"  )
                    || changedSettingsList.contains("MAXCOLORS")
                    || changedSettingsList.contains("GUI_STYLE")
                    || changedSettingsList.contains("GUI_FONT" )
                    || changedSettingsList.contains("GUI_TYPE" )
                   )
          ) {
            if(D) log("=== PALETTE=["+Settings.PALETTE+"] ["+Settings.SYMBOL_ALPHA+Settings.OPACITY+"]");
            TABS_Map.put(Settings.ENTRY_PALETTE, PALETTES_Map.get( Settings.PALETTE ));
            apply_SETTINGS_PALETTE(TABS_Map, "parse_KEY_VAL");
          }
        //}}}
        // APPLY [SOUND_CLICK] [SOUND_DING] {{{
        if( changedSettingsList.contains("SOUND_CLICK") )
        {
            mRTabs.set_PROPERTY_SOUND_CLICK(Settings.SOUND_CLICK, caller);
        }
        //}}}
        // APPLY [SOUND_CLICK] [SOUND_DING] {{{
        if( changedSettingsList.contains("SOUND_DING") )
        {
            mRTabs.set_PROPERTY_SOUND_DING(Settings.SOUND_DING, caller);
        }
        //}}}
        // APPLY [BAND_HIDE_WIDTH] {{{
        if( changedSettingsList.contains("BAND_HIDE_WIDTH") )
        {
            mRTabs.set_PROPERTY_BAND_HIDE_WIDTH(""+Settings.BAND_HIDE_WIDTH, caller);
        }
        //}}}
        // APPLY [MARK_SCALE_GROW] {{{
        if( changedSettingsList.contains("MARK_SCALE_GROW") )
        {
            mRTabs.set_PROPERTY_MARK_SCALE_GROW(""+Settings.MARK_SCALE_GROW, caller);
        }
        //}}}
        // APPLY [MARK_EDIT_DELAY] {{{
        if( changedSettingsList.contains("MARK_EDIT_DELAY") )
        {
            mRTabs.set_PROPERTY_MARK_EDIT_DELAY(""+Settings.MARK_EDIT_DELAY, caller);
        }
        //}}}
        // APPLY [BG_VIEW_DEFAULT] {{{
        if( changedSettingsList.contains("BG_VIEW_DEFAULT") )
        {
            mRTabs.set_PROPERTY_BG_VIEW_DEFAULT(String.format("#%06x", Settings.BG_VIEW_DEFAULT), caller);
        }
        //}}}
        // (wether to be shared between Monitor and Device) {{{
        //                   cmd.equals(CMD_PROFILE_UPLOAD  ) .. nothing supposed to change when server asks for a local profile upload
        //                   cmd.equals(Settings.CMD_PROFILE) .. would be waste of time as parse_PROFILE_cmdLine is about to load another profile
        // changedSettingsList.contains("DEV_DPI"           ) // meaningful on monitor side only
        // changedSettingsList.contains("DEV_H"             ) // meaningful on monitor side only
        // changedSettingsList.contains("MON_DPI"           ) // meaningful on monitor side only
        // changedSettingsList.contains("MON_SCALE"         ) // meaningful on monitor side only
        // changedSettingsList.contains("OOB_CMD"           ) // (reserved)
        // changedSettingsList.contains("PROFILE"           ) // not a shared display paramater

        //}}}
    }
    //}}}
    // check_profile_device_server_sync {{{
    private boolean check_profile_device_server_sync(String argLine, String caller)
    {
        // SYNC DEVICE-SERVER PROFILES {{{
        caller += "] [check_profile_device_server_sync("+argLine+")";
//*PROFILE*/Settings.MON(TAG_PROFILE, caller, "isConnected: "+isConnected());
        if( !isConnected() ) return false;

// SERVER-SIDE-SOURCE-CODE
//:vnew /LOCAL/STORE/DEV/PROJECTS/RTabs/RTabsServer/src/Server.cs

        CmdParser.parse( argLine ); // (e.g. argLine=[POLL SOURCE=RTabsServer PROFILE=ProfilesCmd PRODATE=1444148022])

        String   profile_name = CmdParser.getArgValue("PROFILE", "");
        if( TextUtils.isEmpty(profile_name) ) return false;
//*PROFILE*/Settings.MOM(TAG_PROFILE, "profile_name=["+profile_name+"]");

        if( Settings.is_a_dynamic_profile_entry( profile_name ) ) return false;

        String         source = CmdParser.getArgValue("SOURCE", "");
        if( TextUtils.isEmpty(source) ) return false;
//*PROFILE*/Settings.MOM(TAG_PROFILE, "source=["+source+"]");

        long   device_prodate = 0;
        long   server_prodate = 0;
        //if(source.equals(Settings.DEVICE))
            device_prodate = Settings.Working_profile_prodate;
        //else
            try { server_prodate = Integer.parseInt( CmdParser.getArgValue("PRODATE", "0") ); } catch(Exception ex) { log("*** check_profile_device_server_sync PRODATE "+ ex.toString()); }
        //}}}
        // PROFILE SOURCE {{{
        String server_source = "";
        String device_source = "";
        if(source.equals(Settings.DEVICE))
            device_source = source;
        else
            server_source = source;

    //  sym = (source.equals(Settings.DEVICE))
    //      ? (Settings.SYMBOL_forging_data + Settings.SYMBOL_forging_data + Settings.SYMBOL_forging_data)
    //      : (Settings.SYMBOL_cooking_data + Settings.SYMBOL_cooking_data + Settings.SYMBOL_cooking_data);

        //}}}
        // (DEVICE-SIDE) .. (SERVER-SIDE) PROFILE {{{
        String server_profile
            = source.equals(Settings.DEVICE)
            ? ""
            : profile_name;

        String device_profile
            = Settings.Working_profile;

        boolean working_on_same_profile = (server_profile != "") &&  device_profile.equals( server_profile );
        boolean server_some_device_none = (server_profile != "") && (device_profile == "");
        boolean server_none_device_some = (device_profile != "") && (server_profile == "");
        // LOG-REPORT // {{{
        String sym  = Settings.SYMBOL_PROF;
        String sym3 = sym+sym+sym;
        StringBuilder sb_log = new StringBuilder();
        if(D) {
            sb_log.append(sym ).append(caller+"\n");
            sb_log.append(sym3).append("..........profile_name=[").append(profile_name).append("]\n");
            sb_log.append(sym3).append("................source=[").append(source).append("]\n");
            sb_log.append(sym3).append(".........LoadedProfile=[").append(Settings.LoadedProfile.name).append("]\n");
            sb_log.append(sym3).append("==== ...device_profile=[").append(device_profile).append("]\n");
            sb_log.append(sym3).append("==== ...server_profile=[").append(server_profile).append("]\n");
            sb_log.append(sym3).append("working_on_same_profile ").append(working_on_same_profile).append("]\n");
            sb_log.append(sym3).append("server_some_device_none ").append(server_some_device_none).append("]\n");
            sb_log.append(sym3).append("server_none_device_some ").append(server_none_device_some).append("]\n");
            sb_log.append(sym).append("\n");
            log( sb_log.toString() );
        }
        // }}}
        //}}}
        // SYNCHRONIZABLE PROFILE .. i.e. loaded from local storage {{{

        if(        working_on_same_profile
                || server_some_device_none
                // server_none_device_some
          ) {
            // SYNC PROFILES WHEN KEY-VAL COMES FROM THE SERVER {{{
            if(D) {
                sb_log.delete(0, sb_log.length());
                sb_log.append(sym).append("\n");
                sb_log.append( String.format("calling sync_PROFILE: %s %16s --- %16s --- %-10s %s\n", sym,      "SOURCE",      "PROFILE",      "PRODATE", sym) );
                sb_log.append( String.format("calling sync_PROFILE: %s %16s --- %16s --- %-10d %s\n", sym, device_source, device_profile, device_prodate, sym) );
                sb_log.append( String.format("calling sync_PROFILE: %s %16s --- %16s --- %-10d %s\n", sym, server_source, server_profile, server_prodate, sym) );
                sb_log.append(sym).append("\n");
                log( sb_log.toString() );
            }

            // SYNC WORKING PROFILE .. (DOWNLOAD OR UPLOAD)
            Settings.SOURCE = source;
            if( sync_PROFILE(profile_name) ) {
                // ...stop here if a profile download or upload is scheduled
                if(D) log(sym+" check_profile_device_server_sync("+argLine+") WILL WAIT FOR AN UPDATED VERSION OF ["+ profile_name +"] "+sym);
                return true;
            }
            // }}}
          }
        //}}}
        // IGNORE PARAMETERS IF THEY ARE NOT THOSE OF THE CURRENTLY LOADED PROFILE {{{
        if(        (server_profile != "")
                && !server_profile.equals( device_profile )
          ) {
            if(D) log(sym+" check_profile_device_server_sync("+argLine+") YIELD ON PROFILE KEY_VAL SYNC (client or server not involved) "+sym);
          }
        // }}}
        return false;
    }
    //}}}
    // parse_OOB_CMD {{{
    public void parse_OOB_CMD(String settings_source)
    {
        //if(D) log("@@@ parse_OOB_CMD("+ settings_source +"):");
        Settings.OOB_CMD = ""; // consume as soon as processsed
        StoredReply      = ""; // consume as soon as processsed
    }
    //}}}
    /** PROFILES */
    //{{{
    // ProfileTask {{{
    private class AsyncProfileTask extends AsyncTask<String, String, String>
    {
        private String profile_name;

        @Override protected String doInBackground(String... params)
        {
            String caller = "ProfileTask";
//*COMM*/Settings.MOC(TAG_COMM, caller);
            profile_name    = params[0];
//*COMM*/Settings.MOM(TAG_COMM, "PROFILE=["+profile_name+"]");
            String result = "upload_PROFILE("+profile_name+"):\n";
            try {
                Thread.sleep( 500 );            // allow some amount of KEY_VAL dispating cool down
                result += upload_PROFILE( profile_name );
            }
            catch(Exception ex) { result += "\n*** ProfileTask ["+ex.getMessage()+"]\n"; }
//*COMM*/Settings.MOM(TAG_COMM, result);
            return result;
        }

        @Override protected void onPostExecute(String result)
        {
            ProfileTask = null;
            mRTabs.sync_notify_from("ProfileTask.upload_PROFILE("+profile_name+") ...DONE");
            if(D) log(result);

        }
        @Override protected void onCancelled()
        {
            ProfileTask = null;
            if(D) log_center("*** ProfileTask.upload_PROFILE("+ profile_name +") ...CANCELED");
        }
    }
    // }}}
    // sync_PROFILE {{{
    private boolean sync_PROFILE(String profile_name)
    {
        if(D) log("sync_PROFILE: sync_PROFILE("+ profile_name +"):");

        // CHECK PROFILE NAME AND VERSION {{{
        long  device_prodate = Settings.PROFILE.equals(profile_name) ? Settings.PRODATE : 0;
        long  server_prodate = 0;

        if(D) log("sync_PROFILE: ...Settings.SOURCE.=["+ Settings.SOURCE  +"]");
        if(!(Settings.SOURCE == "") && !Settings.SOURCE.equals(Settings.DEVICE))
            try { server_prodate = Integer.parseInt( CmdParser.getArgValue("PRODATE", "0") ); } catch(Exception ex) { log("*** sync_PROFILE("+ profile_name +") PRODATE "+ ex.toString()); }

        // }}}
        // LOAD LOCAL VERSION .. NOTE: Designer and SERVER are using the same files) {{{
        // Settings.LoadProfile(profile_name, server_prodate);

        // }}}
        // COMPARE VERSIONS {{{
        // XXX DESIGNER AND SERVER MAY SIMULATE A DELTA-TIME FOR TESTING PURPOSE
        /* XXX */ //server_prodate += 120;
        String s = Settings.SYMBOL_TIME;

        String device_age = (device_prodate != 0)
            ?                Settings.Get_time_elapsed(1000*device_prodate)
            :                "unknown";
        String server_age = (server_prodate != 0)
            ?                Settings.Get_time_elapsed(1000*server_prodate)
            :                "unknown";

        // COMPARE EXISTING PROFILE VERSIONS
        boolean local_old = false;
        boolean local_new = false;
        if((server_prodate > 0) || (device_prodate > 0)) { // accept one == 0 as the result of a missing file
            local_old = (server_prodate > device_prodate);
            local_new = (server_prodate < device_prodate);
        }
        // OR REQUEST MISSING PROFILE
        else if(device_prodate == 0) {
            local_old = true;
        }

        if(D) log("sync_PROFILE: .  PROFILE=["+ profile_name     +"] local_old=["+ local_old +"] local_new=["+ local_new +"]");
        if(D) log("sync_PROFILE: .  ["        + server_prodate   +"]=[server_prodate] "+s+" "+ server_age);
        if(D) log("sync_PROFILE: .  ["        + device_prodate   +"]=[device_prodate] "+s+" "+ device_age);

        // }}}
        // IF BOTH VERSION ARE THE SAME .. nothing happens {{{
        if(!local_new && !local_old) {
            if((device_prodate != 0) && (server_prodate != 0)) {
                if(D) log("sync_PROFILE: . [SERVER] and [SAVED] PROFILES ARE THE SAME VERSION");
            }
            else {
                if(D) log("sync_PROFILE: . [SERVER] and [SAVED] VERSION CANNOT BE SYNCHRONIZED THIS TIME");
            }
            CMD_PROFILE_UPDATE = "";
            return false;
        }
        // }}}
        // IF NOT ...SCHEDULE A DOWNLOAD OR AN UPLOAD TASK {{{
        else {
            String      newer     = local_new ? "Saved profile" : "Server profile";
            String      time_diff = Settings.Get_time_elapsed(1000*device_prodate, 1000*server_prodate);

            if(D) log("sync_PROFILE: . ["+newer+"] has been updated "+time_diff+" later");

            if( local_old )
            {
                if(D) log("sync_PROFILE:SYNC DOWNLOAD REQUIRED");
                CMD_PROFILE_UPDATE = CMD_PROFILE_DOWNLOAD+" "+profile_name;
                mRTabs.profile_download("sync_PROFILE("+profile_name+")");

                return true;
            }
            // local_new .. INITIATE AN UPLOAD TASK
            else {
                //if(D) log("sync_PROFILE:SYNC UPLOAD REQUIRED (NO-OP) ...anyway, we should wait for the server to ask for it");
                // CMD_PROFILE_UPDATE = CMD_PROFILE_UPLOAD;
                // mRTabs.upload_profile("sync_PROFILE("+profile_name+")");

                // TODO confirm that this is the solution
                if(D) log("sync_PROFILE:SYNC UPLOAD with a new AsyncProfileTask:");
                ProfileTask = new AsyncProfileTask();

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    ProfileTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, profile_name);
                else
                    ProfileTask.execute          (                                profile_name);

                mRTabs.sync_notify_from("PROFILE UPLOAD TASK POSTED...");
                // TODO confirm that this is the solution

                return false;
            }
        }
        // }}}
    }
    //}}}
    // parse_PROFILE {{{
    public void parse_PROFILE(String caller)
    {
        if(D) log("@@@ parse_PROFILE:");
        // CHECK StoredReply {{{
        if(        !StoredReply.startsWith( CMD_PROFILE_DOWNLOAD )
                && !StoredReply.startsWith( CMD_PROFILE_UPLOAD   )
          ) {
            if(D) log("*** parse_PROFILE: StoredReply should contain a ["+ CMD_PROFILE_DOWNLOAD +" or "+ CMD_PROFILE_UPLOAD +"] first-line:");
            if(D) log("*** StoredReply=("+StoredReply.length()+"b) ["+ Settings.ellipsis(StoredReply, 80) +"]");
            return;
          }

        //}}}
        // CMD_PROFILE_DOWNLOAD {{{
        if( StoredReply.startsWith(CMD_PROFILE_DOWNLOAD) )
        {
            if(D) log("");

            // STILL RECEIVING PROFILE DATA .. (waiting for ACK) // {{{
            if( !StoredReply.endsWith(CMD_PROFILE_DOWNLOAD + CMD_ACK) )
            {
                if(D) {
                    String msg = Settings.SYMBOL_WARN+" *** ["+caller+"] *** PROFILE "+ Settings.PROFILE +": ...receiving data ***";

                    int idx = StoredReply.lastIndexOf('\n');
                    if( idx >= 0) idx += 1;
                    else          idx  = StoredReply.length() -40;
                    if( idx <  0) idx  = 0;
                    msg += "\nLAST LINE=["+StoredReply.substring( idx ) +"]";
                    //warn_to_dash    ("parse_PROFILE", msg);
                    if(D) log("parse_PROFILE: parse_PROFILE"+ msg);
                }
            }
            // }}}
            // GOT PROFILE DATA .. (ACK received)
            else {
                // SAVE PROFILE INTO LOCAL STORAGE {{{
                if(D) log("parse_PROFILE: parse_PROFILE SAVE @@@");
                // PROFILE NAME .. (first key=value) {{{
                int    idx0         = StoredReply.indexOf("=");
                int    idx1         = StoredReply.indexOf("\n");
                String profile_name = ((idx0<0) || (idx1<0)) ? "" : StoredReply.substring(idx0+1, idx1);

                if(D) log("parse_PROFILE: ...profile_name=["+ profile_name +"]");

                // }}}
                // PROFILE-BUFFER-START (at first key=val # line) {{{
                idx0 = StoredReply.indexOf("#");
                if(idx0 < 0) idx0 = 0;

                // }}}
                // PROFILE-BUFFER-END .. (at the [may-be-missing] ACK-SEQUENCE-TERMINATOR) {{{
                idx1 = StoredReply.indexOf("\n"+CMD_PROFILE_DOWNLOAD + CMD_ACK);
                if(idx1 < 0) idx1 = StoredReply.length();

                // }}}
                // SAVE PROFILE STORAGE-FILE .. (serialized) {{{
                String file_text = StoredReply.substring(idx0, idx1);

                Profile.Save(profile_name, file_text);
                //warn_to_dash("parse_PROFILE", "PROFILE ["+ Settings.PROFILE +"] --- FILE SAVED");

                //}}}
                // SIGNAL TRANSITION TO ACTIVITY {{{
                mRTabs.profile_saved("parse_PROFILE("+profile_name+")");

                // CONSUME REPLY DATA AND CLEAR REQUEST STATE
                CMD_PROFILE_UPDATE  = "";
                StoredReply         = "";
                if(D) log("");

                // }}}
                //}}}
                // ...THEN LOAD PROFILE {{{
                if(D) log("parse_PROFILE: parse_PROFILE LOAD @@@");

                if(          load_PROFILE(                profile_name )
                        && layout_PROFILE( Settings.LoadedProfile.name )
                  ) {
                    String msg = "--- PROFILE ["+profile_name+"] --- LOADED";
                    //warn_to_dash("parse_PROFILE", msg);
                    mRTabs.sync_notify_from("parse_PROFILE: "+ msg);
                  }
                else {
                    String msg = "*** COULD NOT LOAD PROFILE ["+ profile_name +"]";
                    mRTabs.sync_notify_from("parse_PROFILE: "+ msg);
                }
                //}}}
            }
        }
        // }}}
        // CMD_PROFILE_UPLOAD {{{
        else if( StoredReply.startsWith(CMD_PROFILE_UPLOAD) )
        {
            if(D) log("parse_PROFILE: parse_PROFILE UPLOAD TASK @@@");

            // GET THE PROFILE NAME THE SERVER IS REQUESTING AN UPDATED VERSION FROM THE DEVICE
            String[] lines = StoredReply.split("\n");

            CmdParser.parse( lines[0] );
            String profile_name = CmdParser.getArgValue("PROFILE", "");

            if(profile_name != "")
            {
                ProfileTask = new AsyncProfileTask();

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    ProfileTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, profile_name);
                else
                    ProfileTask.execute          (                                profile_name);

                mRTabs.sync_notify_from("parse_PROFILE: ["+ CMD_PROFILE_UPLOAD +"] UPLOAD TASK POSTED...");
            }
        }
        // }}}
    }
    //}}}
    // clear_CART_Map {{{
    public void clear_CART_Map(String caller)
    {
        caller += "] [clear_CART_Map";
//*PROFILE*/Settings.MON(TAG_PROFILE, caller, "CART_Map=["+CART_Map.size() +" PFOFILES]");

        if(CART_Map.size() < 1) return;

        //String msg = CART_Map.size() +" CARTILES cleared by "+caller;

        NotePane.RecycleMap( CART_Map );
        CART_Map.clear();

        //warn_to_dash("clear_CART_Map", msg);
    }
    //}}}
    // clear_PROF_Map {{{
    public void clear_PROF_Map(String caller)
    {
        caller += "] [clear_PROF_Map";
//*PROFILE*/Settings.MON(TAG_PROFILE, caller, "PROF_Map=["+PROF_Map.size() +" PFOFILES]");

        if(PROF_Map.size() < 1) return;

        //String msg = PROF_Map.size() +" PROFILES cleared by "+caller;

        NotePane.RecycleMap( PROF_Map );
        PROF_Map.clear();

        //warn_to_dash("clear_PROF_Map", msg);
    }
    //}}}
    // clear_AUTO_Map {{{
    public void clear_AUTO_Map(String caller)
    {
        caller += "] [clear_AUTO_Map";
//*PROFILE*/Settings.MON(TAG_PROFILE, caller, "AUTO_Map=["+AUTO_Map.size() +" PFOFILES]");

        if(AUTO_Map.size() < 1) return;

        //String msg = AUTO_Map.size() +" PROFILES cleared by "+caller;

        NotePane.RecycleMap( AUTO_Map );
        AUTO_Map.clear();

        //warn_to_dash("clear_AUTO_Map", msg);
    }
    //}}}
    // clear_DOCK_Map {{{
    public void clear_DOCK_Map(String caller)
    {
        caller += "] [clear_DOCK_Map";
//*PROFILE*/Settings.MON(TAG_PROFILE, caller, "DOCK_Map=["+DOCK_Map.size() +" PFOFILES]");

        if(DOCK_Map.size() < 1) return;

        //String msg = DOCK_Map.size() +" PROFILES cleared by "+caller;

        NotePane.RecycleMap( DOCK_Map );
        DOCK_Map.clear();

        //warn_to_dash("clear_DOCK_Map", msg);
    }
    //}}}
    // upload_PROFILE {{{
    private String upload_PROFILE(String profile_name)
    {
        String result = "@@@ upload_PROFILE("+ profile_name +"):\n";

        // GET PROFILE DATA
        File file = Profile.GetProfileFile( profile_name );
        if((file == null) || !file.exists()) {
            result += "*** upload_PROFILE("+ profile_name +"): ...FILE NOT FOUND\n";
            send_cmd( result );
        }
        // SEND PROFILE LINES
        else {
            ArrayList<String> lines = Profile.GetProfileLines( profile_name );
            if(lines == null) {
                result += "*** Profile.GetProfileLine  ==  null\n";
            }
            else {
                result += "@@@ "+ lines.size() +" lines\n";
                try {
                    synchronized(Mutex_SOCKET)
                    {
                        if(D) result += "@@@ upload_PROFILE("+ profile_name +")";
                        PrintWriter socket_out =
                            new PrintWriter(
                                    new BufferedWriter(
                                        new OutputStreamWriter( socket.getOutputStream()))
                                    , true); // autoflush!
                        for(int i=0; i< lines.size(); ++i)
                            socket_out.println( lines.get(i) );
                        socket_out.flush();
                    }
                    result += "@@@ "+ profile_name +" records sent\n";
                }
                catch(Exception ex) { result += "*** upload_PROFILE("+ profile_name +") ["+ex.getMessage()+"]\n"; }
            }
        }

        // SEND EXPECTED ACK
        send_ACK( Settings.CMD_POLL );

        return result;
    }
    // }}}
    //}}}
    /** LOAD & LAYOUT */
    // {{{
    // load_DOCKINGS_TABLE {{{
    public void load_DOCKINGS_TABLE(String caller)
    {
        if(D) log("@@@ "+caller+".load_DOCKINGS_TABLE:");
        load_PROFILE  (Settings.DOCKINGS_TABLE);
        layout_PROFILE(Settings.DOCKINGS_TABLE);
    }
    //}}}
    // load_PROFILES_TABLE {{{
    public void load_PROFILES_TABLE(String caller)
    {
        caller += "] [load_PROFILES_TABLE";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);
        load_PROFILE  (Settings.PROFILES_TABLE);
        layout_PROFILE(Settings.PROFILES_TABLE);
    }
     //}}}
    // load_PROFHIST_TABLE {{{
    public void load_PROFHIST_TABLE(String caller)
    {
        caller += "] [load_PROFHIST_TABLE";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);
        load_PROFILE  (Settings.PROFHIST_TABLE);
        layout_PROFILE(Settings.PROFHIST_TABLE);
    }
     //}}}
    // load_SOUNDS_TABLE {{{
    public void load_SOUNDS_TABLE(String caller)
    {
        caller += "] [load_SOUNDS_TABLE";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);
        load_PROFILE  (Settings.SOUNDS_TABLE);
        layout_PROFILE(Settings.SOUNDS_TABLE);
    }
     //}}}
    // load_CONTROLS_TABLE {{{
    public void load_CONTROLS_TABLE(String caller)
    {
        caller += "] [load_CONTROLS_TABLE";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);
        load_PROFILE  (Settings.CONTROLS_TABLE);
        layout_PROFILE(Settings.CONTROLS_TABLE);
    }
    //}}}
    // load_USER_PROFILE {{{

    public  boolean load_USER_PROFILE(String profile_name, String caller)
    {
        return _load_USER_PROFILE(profile_name, HISTORY_NEW, caller);
    }

    private boolean _load_USER_PROFILE(String profile_name, String nav_task, String caller)
    {
        caller += "] [_load_USER_PROFILE("+profile_name+", "+nav_task+")";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);

        String previous_Working_profile = Settings.Working_profile;
//*PROFILE*/Settings.MOM(TAG_PROFILE, "...previous_Working_profile=["+previous_Working_profile+"]");

        // LOAD REQUESTED PROFILE FROM LOCAL STORAGE (palettes and tabs) {{{
        if(          load_PROFILE(                profile_name )
                && layout_PROFILE( Settings.LoadedProfile.name )
          ) {
            // DO NOT DISPATCH DEVICE-ONLY PROFILES REQUEST {{{
            if( Settings.is_a_dynamic_profile_entry( profile_name ) )
            {
//*PROFILE*/Settings.MON(TAG_PROFILE, caller, "DO NOT DISPATCH REQUEST:...return false");
                return false;
            }
            //}}}
            // COMMUNITATE PROFILE PRODATE TO SERVER {{{
            else {
                // DONE SELECTING WORKBENCH PROFILE
                if( is_in_WORKBENCH() ) {
//*PROFILE*/Settings.MON(TAG_PROFILE, caller, "WORKBENCH Working_profile=["+ Settings.Working_profile +"]");
                    clear_PROF_Map(caller);
                }
                // HISTORY: REMEMBER PREVIOUS PROFILE
                else if( !TextUtils.isEmpty(previous_Working_profile) )
                {
                    if( !Settings.Working_profile.equals( previous_Working_profile ) )
                        history_push(previous_Working_profile, nav_task);
                }

                // UPDATE DOCK BUTTON
                NpButton np_button = get_DOCK_Map_NpButton( Settings.Working_profile );
                if(np_button != null)
                {
                    int bg_color = Settings.BG_COLOR_DOCK_VISITED;
                    int fg_color = getTextColor(COLOR_DOCK, bg_color);
                    np_button.setBackgroundColor( bg_color );
                    np_button.setTextColor      ( fg_color );
                }

                // WORKING PROFILE NAME UPDATE
                mRTabs.update_histBand(caller);

                // UPDATE DOCK DISPLAY
                mRTabs.dock_scroll_to_working_profile((caller));

                // MAKE UP FOR PROFILES DISPLAY UPDATE
                //mRTabs.invalidate_profile_handles(caller); // would recurse here

                mRTabs.toast_again_clear();
//*PROFILE*/Settings.MON(TAG_PROFILE, caller, "...done: return true");
                return true;
            }
            //}}}
          }
        //}}}
        // DOWNLOAD INVALID OR UNKNOWN PROFILE {{{
        else {
            String msg = "COULD NOT LOAD PROFILE ["+profile_name+"]";
            mRTabs.toast_long( msg );
//*PROFILE*/Settings.MON(TAG_PROFILE, caller, msg);
            return false;
        }
        //}}}
    }
    //}}}
    // load_PROFILE {{{
    private boolean load_PROFILE(String profile_name)
    {
        String caller = "load_PROFILE("+ profile_name +")";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);

        boolean loading_carttabs_table = (profile_name.equals( Settings.CARTTABS_TABLE ));
        boolean loading_controls_table = (profile_name.equals( Settings.CONTROLS_TABLE ));
        boolean loading_dockring_table = (profile_name.equals( Settings.DOCKINGS_TABLE ));
        boolean loading_profhist_table = (profile_name.equals( Settings.PROFHIST_TABLE ));
        boolean loading_sounds_table   = (profile_name.equals( Settings.SOUNDS_TABLE   ));
        boolean loading_profiles_table = (profile_name.equals( Settings.PROFILES_TABLE ));

        // SEARCH PROFILES STORAGE {{{
        if( !Profile.Is_in_store( profile_name ) )
        {
//*PROFILE*/Settings.MON(TAG_PROFILE, caller, "(NOT IN STORE) ...return false");
            return false;
        }

        //}}}
        // LOAD AND VALIDATE {{{

        Profile profile = new Profile( profile_name );
        String result   = profile.validate_or_delete();
        if(result != "") {
            //warn_to_dash(caller, result);
//*PROFILE*/Settings.MON(TAG_PROFILE, caller,  "("+result+") ...return false");
            return false;
        }
        else if(profile.name != profile_name)
        {
            caller = "load_PROFILE("+ profile.name +")";
        }

        Settings.SetLoadedProfile( profile );

        //}}}
        // PARSE PALETTES {{{
        if(        !loading_carttabs_table
                && !loading_controls_table
                && !loading_dockring_table
                && !loading_profhist_table
                && !loading_profiles_table
                && !loading_sounds_table
          ) {
            if( build_answer_to(CMD_PALETTES_GET, caller) )
                parse_PALETTES();
          }
        //}}}
        // PARSE TABS {{{
        if( build_answer_to(CMD_TABS_GET, caller) )
        {
            if     ( loading_carttabs_table ) parse_TABS(CART_Map, caller);
            else if( loading_controls_table ) parse_TABS(CTRL_Map, caller);
            else if( loading_dockring_table ) parse_TABS(DOCK_Map, caller);
            else if( loading_profhist_table ) parse_TABS(AUTO_Map, caller);
            else if( loading_sounds_table   ) parse_TABS(AUTO_Map, caller);
            else if( loading_profiles_table ) parse_TABS(PROF_Map, caller);
            else                              parse_TABS(TABS_Map, caller);
        }

        //}}}

//*PROFILE*/Settings.MON(TAG_PROFILE, caller, " ...return true");
        return true;
    }
    //}}}
    // reload_Working_profile {{{
    public  void reload_Working_profile()
    {
        String caller = "reload_Working_profile";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);

        if(Settings.Working_profile_instance == null) {
//*PROFILE*/Settings.MOM(TAG_PROFILE, caller+": (Settings.Working_profile_instance == null)");
            return;
        }

        String one_liner_key_val = Settings.Working_profile_instance.get_PROFILE_KV_LINE();
        String      profile_tabs = Settings.Working_profile_instance.get_PROFILE_TABS();

        add_last_sent_cmd_ack();

        StoredReply
            = CMD_TABS_GET
            +" SOURCE="+ Settings.DEVICE +" "+ one_liner_key_val +"\n"
            + profile_tabs
            + last_sent_cmd
            ;

         parse_TABS(TABS_Map, caller);

         layout_PROFILE( Settings.Working_profile_instance.name );

//*PROFILE*/Settings.MON(TAG_PROFILE, caller, " ...done");
    }
    //}}}
    // layout_PROFILE {{{
    private boolean layout_PROFILE(String profile_name)
    {
        String caller = "layout_PROFILE("+profile_name+")";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);

        boolean loading_carttabs_table = (profile_name.equals( Settings.CARTTABS_TABLE ));
        boolean loading_controls_table = (profile_name.equals( Settings.CONTROLS_TABLE ));
        boolean loading_dockring_table = (profile_name.equals( Settings.DOCKINGS_TABLE ));
        boolean loading_profhist_table = (profile_name.equals( Settings.PROFHIST_TABLE ));
        boolean loading_sounds_table   = (profile_name.equals( Settings.SOUNDS_TABLE   ));
        boolean loading_profiles_table = (profile_name.equals( Settings.PROFILES_TABLE ));

        // LAYOUT TABS {{{
        if     ( loading_carttabs_table ) apply_TABS_LAYOUT(CART_Map, mRTabs.get_prof_container(), caller);
        else if( loading_controls_table ) apply_TABS_LAYOUT(CTRL_Map, mRTabs.get_ctrl_container(), caller);
        else if( loading_dockring_table ) apply_TABS_LAYOUT(DOCK_Map, mRTabs.get_dock_container(), caller);
        else if( loading_profhist_table ) apply_TABS_LAYOUT(AUTO_Map, mRTabs.get_hist_container(), caller);
        else if( loading_sounds_table   ) apply_TABS_LAYOUT(AUTO_Map, mRTabs.get_hist_container(), caller);
        else if( loading_profiles_table ) apply_TABS_LAYOUT(PROF_Map, mRTabs.get_prof_container(), caller);
        else                              apply_TABS_LAYOUT(TABS_Map, tabs_container             , caller);

        //}}}
        // APPLY CURRENT PALETTE {{{

        if(Settings.PALETTE == "") Settings.PALETTE = "W8";

        if     ( loading_carttabs_table ) apply_SETTINGS_PALETTE(CART_Map, caller);
        else if( loading_controls_table ) apply_SETTINGS_PALETTE(CTRL_Map, caller);
        else if( loading_dockring_table ) apply_SETTINGS_PALETTE(DOCK_Map, caller);
        else if( loading_profhist_table ) apply_SETTINGS_PALETTE(AUTO_Map, caller);
        else if( loading_sounds_table   ) apply_SETTINGS_PALETTE(AUTO_Map, caller);
        else if( loading_profiles_table ) apply_SETTINGS_PALETTE(PROF_Map, caller);
        else                              apply_SETTINGS_PALETTE(TABS_Map, caller);

        //}}}
        // UPDATE UI {{{
        mRTabs.sync_np( caller );

        //}}}

//*PROFILE*/Settings.MON(TAG_PROFILE, caller, " ...return true\n");
        return true;
    }
    //}}}
    // }}}
    /** HISTORY STACK  */
    // {{{
    //{{{
    public   static final String       HISTORY_BAK = "HISTORY_BAK";
    public   static final String       HISTORY_FWD = "HISTORY_FWD";
    public   static final String       HISTORY_NEW = "HISTORY_NEW";

    private  static final Stack<String> back_stack = new Stack<>();
    private  static final Stack<String> frwd_stack = new Stack<>();

    //}}}
    // history_get_profile_names {{{
    public ArrayList<String> history_get_profile_names()
    {
        String caller = "history_get_profile_names";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);

        ArrayList<String>    al = new ArrayList<>();

        //  Iterator i;
        String profile_name = "";
        Stack s;

        // from backward stack .. (from PREVIOUS to VERY FIRST)
        s     = (Stack)back_stack.clone();
        while( !s.isEmpty() ) {
            profile_name = (String)s.pop();
//*PROFILE*/Settings.MOM(TAG_PROFILE, String.format("%s: ...from back_stack: %2d=[%s]", caller, al.size()+1, profile_name));
            al.add(0, profile_name); // head-insert
        }

        // from current profile
        if( !TextUtils.isEmpty( Settings.Working_profile ) ) {
            profile_name =  Settings.Working_profile;
//*PROFILE*/Settings.MOM(TAG_PROFILE, String.format("%s: ...Working_profile: %2d=[%s]", caller, al.size()+1, profile_name));
            al.add(   profile_name); // tail-append
        }

        // from forward stack .. (from NEXT to VERY LAST)
        s     = (Stack)frwd_stack.clone();
        while( !s.isEmpty() ) {
            profile_name = (String)s.pop();
//*PROFILE*/Settings.MOM(TAG_PROFILE, String.format("%s: ...from frwd_stack: %2d=[%s]", caller, al.size()+1, profile_name));
            al.add(   profile_name); // tail-append
        }

//*PROFILE*/Settings.MOM(TAG_PROFILE, caller+": ...return "+al.size()+" names == "+ Settings.al_toString(al));
        return  al;
    }
    //}}}
    // history_count_steps_to_profile {{{
    private int history_count_steps_to_profile(String profile_name)
    {
        //  Iterator i;
        int steps;
        Stack s;

        // NOTE! Stack Iterator does not go from top to bottom ... it works on insertion order
        //  i = frwd_stack.iterator(); while( i.hasNext() ) {++steps; if(((String)i.next()).equals(profile_name)) return steps; }
        //  i = back_stack.iterator(); while( i.hasNext() ) {--steps; if(((String)i.next()).equals(profile_name)) return steps; }

        steps = 0;
        s     = (Stack)frwd_stack.clone();
        while(!s.isEmpty()) { ++steps; if(s.pop().equals( profile_name )) return steps; }

        steps = 0;
        s     = (Stack)back_stack.clone();
        while(!s.isEmpty()) { --steps; if(s.pop().equals( profile_name )) return steps; }

        return  0;
    }
    //}}}
    // history_push {{{
    private void history_push(String previous_Working_profile, String nav_task)
    {
        String caller = "history_push("+previous_Working_profile+","+nav_task+")";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);

        // STACK PREVIOUS WORKING PROFILE SOMEWHERE // {{{
        int steps_to_previous_Working_profile = history_count_steps_to_profile( previous_Working_profile );
//*PROFILE*/Settings.MOM(TAG_PROFILE, "...steps_to_previous_Working_profile=["+steps_to_previous_Working_profile+"]");

        if(steps_to_previous_Working_profile == 0)
        {
//*PROFILE*/Settings.MOM(TAG_PROFILE, "PUSHING "+nav_task);
            if     (nav_task == HISTORY_BAK) frwd_stack.push( previous_Working_profile );
            else if(nav_task == HISTORY_FWD) back_stack.push( previous_Working_profile );
            else /* HANG NEW BRANCH HERE */  back_stack.push( previous_Working_profile );
        }
        else {
//*PROFILE*/Settings.MOM(TAG_PROFILE, "FETCHING HISTORY");
        }

        // }}}

        int steps_to_this_Working_profile = history_count_steps_to_profile( Settings.Working_profile );
//*PROFILE*/Settings.MOM(TAG_PROFILE, "...steps_to_this_Working_profile=["+steps_to_this_Working_profile+"]");

        // GET TO CURRENT WORKING PROFILE OUT FROM BOTH STACK ( back_stack_            _frwd_stack )
        //................................................... (            \_ current_/            )
        while(steps_to_this_Working_profile < 0) { ++steps_to_this_Working_profile; frwd_stack.push( back_stack.pop() ); }
        while(steps_to_this_Working_profile > 0) { --steps_to_this_Working_profile; back_stack.push( frwd_stack.pop() ); }

        // WORKING_PROFILE_NAME HOLDS ONE CELL OF A CHAIN OF NON-OVERLAPPING history CELLS:
        // o==============================================================================o
        // | [B] [B] [B] [B] [B] [B] [B]  [WORKINGPROFILE]  [F] [F] [F] [F] ............. |
        // o==============================================================================o
        // EXTRACT CURRENT WORKING PROFILE FROM BOTH STACKS
        if(!back_stack.isEmpty() && back_stack.peek().equals( Settings.Working_profile )) back_stack.pop();
        if(!frwd_stack.isEmpty() && frwd_stack.peek().equals( Settings.Working_profile )) frwd_stack.pop();

//*PROFILE*/history_get_profile_names();//TAG_PROFILE
    }
    //}}}
    // history_remove {{{
    public void history_remove(String profile_name)
    {
        String caller = "history_remove("+profile_name+")";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);

        // keep all but profile_name in both stacks
        Stack s; String e;
        s     = (Stack)frwd_stack.clone(); frwd_stack.clear();
        while(!s.isEmpty()) { e = (String)s.pop(); if(!e.equals( profile_name ) ) frwd_stack.push(e); }

        s     = (Stack)back_stack.clone(); back_stack.clear();
        while(!s.isEmpty()) { e = (String)s.pop(); if(!e.equals( profile_name ) ) back_stack.push(e); }

//*PROFILE*/history_get_profile_names();//TAG_PROFILE
    }
    //}}}
    //* back * frwd * size * empty {{{ */

    public    void history_back      () { if( !back_stack.isEmpty() ) _load_USER_PROFILE( back_stack.pop(), HISTORY_BAK, "history_back"); }
    public    void history_frwd      () { if( !frwd_stack.isEmpty() ) _load_USER_PROFILE( frwd_stack.pop(), HISTORY_FWD, "history_frwd"); }

    public    void history_frwd_clear() { frwd_stack.clear(); }
    public    void history_back_clear() { back_stack.clear(); }

    public     int history_back_size () { return back_stack.size(); }
    public     int history_frwd_size () { return frwd_stack.size(); }
    public boolean history_is_empty  () { return (frwd_stack.size() == 0) && (back_stack.size() == 0); }

    //}}}

//    // history_get_profile_num {{{
//    private String history_get_profile_num(int profile_num)
//    {
//        //  Iterator i;
//        String profile_name = "";
//        Stack s;
//
//        if     (profile_num > 0) { // from forward stack
//            s     = (Stack)frwd_stack.clone();
//            while(!s.isEmpty() && (profile_num-- > 0)) { profile_name = (String)s.pop(); }
//        }
//        else if(profile_num < 0) { // from backward stack
//            s     = (Stack)back_stack.clone();
//            while(!s.isEmpty() && (profile_num++ < 0)) { profile_name = (String)s.pop(); }
//        }
//
//        return  profile_name;
//    }
//    //}}}
//    // history_get_visited_profile_num_np  {{{
//    private NpButton history_get_visited_profile_num_np(int profile_num)
//    {
//        NpButton np_button = null;
//        for(Map.Entry<String, Object> entry : PROF_Map.entrySet())
//        {
//            if(entry.getKey() == Settings.ENTRY_PALETTE) continue;
//            NotePane np = (NotePane)entry.getValue();
//
//            // track visited entries count-down...
//            if( is_a_visited_profile_np( np ) ) --profile_num;
//
//            // ...until zero .. (at which point it is the requested one)
//            if(profile_num == 0)
//            {
//                np_button = np.button;
//                break;
//            }
//        }
////*PROFILE*/Settings.MOM(TAG_PROFILE, history_get_visited_profile_num_np("+profile_num+")": ...return ["+np_button+"]");
//        return  np_button;
//    }
//    //}}}
    //}}}
    /** CART STACK */
    //{{{
    private static final String   CART_ADD   = "CART_ADD";
    private static final String   CART_DEL   = "CART_DEL";
    private final Stack<NotePane> cart_stack = new Stack<>();

    // cart_save_from  {{{
    public void cart_save_from (NotePane np)
    {
        if(np != null) {
//XXX*/System.err.println("cart_save_from:");
//XXX*/System.err.println("...np=["+ np +"]");
            cart_stack.push( np.clone() ); // allows mutated source at paste time
        }

        mRTabs.pulse_np( np );
        mRTabs.sync_cartBand("cart_save_from");

    }
    //}}}
    // cart_extract_to {{{
    public void cart_extract_to(NotePane to_np)
    {
        if( cart_stack.isEmpty() ) return;

//XXX*/System.err.println("cart_extract_to:");
//XXX*/System.err.println("...to_np=["+ to_np.toString() +"]");
        try {
            NotePane  cart_np = cart_stack.pop();

            to_np.setTextAndInfo( cart_np.text  );  // cart_np.button update
            to_np.set_tag       ( cart_np.tag   );  // text first .. (avoid tag copy to empty label)
            to_np.set_shape     ( cart_np.shape );
            to_np.set_tt        ( cart_np.tt    );

            // UPDATE PROFILE FILE
            Profile.Update_TAB(Settings.Working_profile_instance, to_np); // not committed to file ... will be checked before calling load_USER_PROFILE
        }
        catch(Exception ex) { System.err.println("cart_extract_to Exception:\n"+ex); }

        mRTabs.pulse_np( to_np );
        mRTabs.sync_cartBand("cart_extract_to");

//XXX*/System.err.println("...cart_np.tag =["+cart_np.tag +"]");
//XXX*/System.err.println("...cart_np.text=["+cart_np.text+"]");
    }
//}}}
    // cart_clear {{{
    public void cart_clear()
    {
//*CART*/Settings.MON(TAG_CART, "cart_clear", "["+cart_stack.size()+" TABS]");
        cart_stack.clear();
    }
    //}}}
    // get_cart_size {{{
    public int get_cart_size() {
        return cart_stack.size();
//}}}
    }

    // ==================================================================== }}}
    /** TABS */
    // PARSE {{{
    // parse_TABS {{{
    public void parse_TABS(HashMap<String, Object> hashMap, String caller)
    {
        caller += "] [@@@ parse_TABS("+ get_hashMap_name(hashMap) +")";
//*TAB*/Settings.MOC(TAG_TAB, caller);
        // CHECK CURRENT REPLY CONTENTS {{{
        if( !StoredReply.startsWith( CMD_TABS_GET) ) {
//*TAB*/Settings.MOC(TAG_TAB, caller);
            log("*** parse_TABS: StoredReply should contain a ["+ CMD_TABS_GET +"] first-line:");
            log("*** StoredReply=("+StoredReply.length()+"b) ["+ Settings.ellipsis(StoredReply, 80) +"]");
            return;
        }

        //}}}
        // CLEAR OBSOLETE DATA {{{
        clear_TABS_Map(hashMap, "parse_TABS");

        //}}}
        // CMD_TABS_GET embeded KEY_VAL {{{
        String[] lines = StoredReply.split("\n");
        if(D) log("...lines.length=["+ lines.length +"]");

        parse_KEY_VAL(lines[0], "parse_TABS("+caller+")");

        //}}}
        // PARSE TABS LINES {{{
//*TAB*/Settings.MOM(TAG_TAB, "@@@ parse_TABS INTO ["+ get_hashMap_name(hashMap) +"]");
//*TAB*/Settings.MOM(TAG_TAB, "...tracing details on those with a (*) in their tooltip):");
        int x_min = 1024;
        int y_min = 1024;
        int x_max = 1;
        int y_max = 1;

        // Register session dashboard [NotePane with tag=DASH] and [UNICODE BLOCK TOOLS]
        if(hashMap == TABS_Map) {
            DashNotePane = null;
            clear_BLOCK_np("parse_TABS");
        }

        // VALIDATE NEXT LAYOUT
        if(hashMap == TABS_Map)
            TABS_Map_Has_Changed = true;

        // USER TABS PROFILE HIGHEST RESERVED COLOR INDEX
        if(hashMap == TABS_Map)
            Settings.RESCOLORS = 0;

        NotePane  np = null;

        // WORKBENCH HOOK
        if(hashMap == TABS_Map)
            HookRect.left = HookRect.top = 0;

        int             len = lines.length;
        for(int i=1; i< len; ++i)
        {
            if(lines[i].startsWith( CMD_TABS_GET         )) break; // CMD_ACK last line
            if(lines[i].startsWith( CMD_POLL             )) break; // CMD_ACK last line
            if(lines[i].startsWith( CMD_PROFILE_DOWNLOAD )) break; // CMD_ACK last line

            // KEY_VAL LINES
            if(lines[i].startsWith( "#" )) {
                Settings.set_KEY_VAL(lines[i], caller);
            }
            else {
                // NEW NotePane: [x_max y_max] [RESERVED] [DashNotePane] {{{
                try{
                    np = parse_TABS_line(hashMap, lines[i]);
                    if(np != null) hashMap.put(np.name, np);
                }
                catch(Exception ex) {
                    log("*** parse_TABS_line("+ lines[i] +") Exception:\n"+ ex.toString());
                    if( Settings.LOG_CAT ) ex.printStackTrace();
                }

                if(np != null)
                {
                    // x_max y_max
                    if((np.x       ) < x_min) x_min = (np.x       );
                    if((np.y       ) < y_min) y_min = (np.y       );
                    if((np.x + np.w) > x_max) x_max = (np.x + np.w);
                    if((np.y + np.h) > y_max) y_max = (np.y + np.h);

                    // USER TABS
                    if(hashMap == TABS_Map)
                    {
                        // DASH
                        if((DashNotePane == null) && (np.tag.startsWith("DASH"))) { DashNotePane = np; /*np.tag = "#";*/ }

                        // TOOLS
                        if( NotePane.isAToolHook(np) ) // WORKBENCH TOOLS
                        {
                            HookRect.left   = np.x;
                            HookRect.top    = np.y;
                            HookRect.right  = np.x + np.w;
                            HookRect.bottom = np.y + np.h;
                        }
                        check_BLOCK_np(np); // UNICODE BLOCK TOOLS
                    //  check_TOOL_URL_np (np); // WEB URL TOOL

                        // RESERVED COLOR INDEX
                    //  if(!NotePane.isATool(np) && (np != TOOL_URL_np) && (np.color > Settings.RESCOLORS))
                        if(!NotePane.isATool(np)                        && (np.color > Settings.RESCOLORS))
                            Settings.RESCOLORS = np.color;

                    }
                }
                //}}}
            }
        }
        //}}}
        if(D) log("...HookRect=["+ HookRect +"]");

        // DEV_SCALE_MAX DEV_SCALE_MIN {{{
        if(hashMap == TABS_Map)
        {

            //  Settings.DEV_SCALE_MAX = 10.0 * x_max / Settings.DISPLAY_W;
            //  Settings.DEV_SCALE_MIN =  1.0 * x_max / Settings.DISPLAY_W;

            // so we can zoom back enough to see everything
            //if(D) log(" |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  | ");
            //if(D) log("DISPLAY_W=["+ Settings.DISPLAY_W +"]");
            //if(D) log("DISPLAY_H=["+ Settings.DISPLAY_H +"]");
            //if(D) log("DEV_SCALE_MIN=["+ Settings.DEV_SCALE_MIN +"]");
            x_min *= Settings.TAB_GRID_S;
            y_min *= Settings.TAB_GRID_S;
            x_max *= Settings.TAB_GRID_S;
            y_max *= Settings.TAB_GRID_S;
            int dx = x_max - x_min;
            int dy = y_max - y_min;
            //if(D) log("x_min=["+ x_min +"]");
            //if(D) log("y_min=["+ y_min +"]");
            //if(D) log("x_max=["+ x_max +"]");
            //if(D) log("y_max=["+ y_max +"]");
            //if(D) log("   dx=["+ dx    +"]");
            //if(D) log("   dy=["+ dy    +"]");

            Settings.set_XY_WH(x_min, y_min, dx, dy);
            //parse_tabs_container_frame(x_min, y_min, x_max, y_max); // XXX

            if(Settings.TABS_RATIO >= Settings.DEV_RATIO)
                mRTabs.fit_scale_to_window(RTabs.FIT_W, caller);
            else
                mRTabs.fit_scale_to_window(RTabs.FIT_H, caller);

        }
        //}}}
        // CONSUME PARSED DATA {{{
        StoredReply = ""; // CONSUME PARSED DATA
        if(Settings.OOB_CMD.equals( Settings.CMD_RELOAD ))
            Settings.OOB_CMD = "";

        //}}}
        mRTabs.sync_notify_from("parse_TABS: ["+ Settings.LoadedProfile.name +"] "+ hashMap.size() +" TABS PARSED INTO "+ get_hashMap_name(hashMap));
//*TAB*/Settings.MOM(TAG_TAB, "parse_TABS ...done");
    }
    //}}}
    // parse_TABS_line {{{
    private NotePane parse_TABS_line(String line) { return parse_TABS_line(null, line); }

    @SuppressWarnings("ConstantConditions")
    private NotePane parse_TABS_line(HashMap<String, Object> hashMap, String line)
    {
        // XXX TEMPORARY EXTRA LOG {{{
        //if(D) log("parse_TABS_line("+ line +")");
        //if((hashMap!= null) && (hashMap.size() < 3)) if(D) log("@@@ parse_TABS_line("+ line +"):");

        //}}}
        // CUT SHORT WHEN NOT A TAB LINE {{{
        if(!line.contains("xy_wh"))
        {
            if(D) log("XXX NOT A TAB LINE: ["+line+"]");
            return null;
        }
        //}}}
        // VARS {{{
        String[] args;
        String   name;
    //  String   value;

        NotePane np = null;
        String   type;
        String   tag;
        float    z;
        String   xy_wh;
        int      x, y, w, h;

        String   text;
        int      color;
        String   shape;
        String   tt;

        //}}}
        // SPLIT [line] INTO KEY=VALUE PAIRS .. (turn windows-registry key from "KEY.VALUE" into "KEY=VALUE") {{{
        /* ------------------------------- */ //if(D) log("@@@ line=["+ line +"]");
        line= line.replaceFirst(  "=" , "|"); //if(D) log("@@@ line=["+ line +"]"); // TAB=panel_usr4|type=SHORTCUT|tag=...
        line= line.replaceFirst("\\." , "="); //if(D) log("@@@ line=["+ line +"]"); // TAB=panel_usr4=type=SHORTCUT|tag=...
        args= line.split("\\|");              //if(D) log("args=[ #"+args.length +"]");

        //}}}
        // TAB SAMPLE {{{
        //
        // TAB.panel_usr3=type=SHORTCUT|tag=PROFILE ProfileDPI|zoom=1.5|xy_wh=13,6,16,5|text=PROFILE_ProfileDPI|color=1|shape=circle|tt=
        //
        // 0 TAB.panel_usr4=
        // 1           type=SHORTCUT
        // 2            tag=PROFILE ProfileLeft
        // 3           zoom=1.5
        // 4          xy_wh=13,12,16,4
        // 5           text=PROFILE_ProfileLeft
        // 6          color=1
        // 7          shape=circle
        // 8             tt=
        // }}}
        // LOOP ON KEY=VAL pairs {{{

        name=""; type=""; tag=""; z=0; xy_wh=""; x=0; y=0; w=0; h=0; text=""; color=0; shape=""; tt="";

        int              len = args.length;
        for(int i=0; i < len; ++i)
        {
            // [KEY=VAL] .. (look for the '=' separator) {{{
            int idx = args[i].indexOf('=');
            if(idx < 0)
            {
                //if(D) log("*** args["+i+"]=["+ args[i] +"]: key has no '=' separator");
                continue;
            }
            //}}}
            // [VAL=EMPTY] .. (ends right after the '=' separator) {{{
            if(args[i].length() == (idx+1))
            {
                //if(D) log("*** args["+i+"]=["+ args[i] +"]: key has no value");
                continue;
            }
            //}}}
            // SPLIT [KEY]=[VAL] {{{
            String[] kv = new String[] { args[i].substring(0,idx), args[i].substring(idx+1) };

            //if(D) log("@@@ args["+i+"]=["+ args[i] +"]");
            //if(D) log("    key=["+kv[0]+"] val=["+kv[1]+"]");
            //}}}

        //  name = "tab"+hashMap.size(); // ? name autonumber ?

            try{ if(kv[0].toUpperCase().equals(   "TAB" )) {  name =                    kv[1] ; /*if(D) log( String.format("ooo %16s = %s",  "name" , name ));*/ } } catch(Exception ex) { log( String.format("*** %16s : %s\n", args[i] +"]: ", ex.getMessage()+"\n")); }
            try{ if(kv[0].toLowerCase().equals(  "type" )) {  type =                    kv[1] ; /*if(D) log( String.format("ooo %16s = %s",  "type" , type ));*/ } } catch(Exception ex) { log( String.format("*** %16s : %s\n", args[i] +"]: ", ex.getMessage()+"\n")); }
            try{ if(kv[0].toLowerCase().equals(   "tag" )) {   tag =                    kv[1] ; /*if(D) log( String.format("ooo %16s = %s",   "tag" , tag  ));*/ } } catch(Exception ex) { log( String.format("*** %16s : %s\n", args[i] +"]: ", ex.getMessage()+"\n")); }
            try{ if(kv[0].toLowerCase().equals(     "z" )) {     z = Float  .parseFloat(kv[1]); /*if(D) log( String.format("ooo %16s = %f",     "z" , z    ));*/ } } catch(Exception ex) { log( String.format("*** %16s : %s\n", args[i] +"]: ", ex.getMessage()+"\n")); }
            try{ if(kv[0].toLowerCase().equals( "xy_wh" )) { xy_wh =                    kv[1] ; /*if(D) log( String.format("ooo %16s = %s", "xy_wh" , xy_wh));*/ } } catch(Exception ex) { log( String.format("*** %16s : %s\n", args[i] +"]: ", ex.getMessage()+"\n")); }
            try{ if(kv[0].toLowerCase().equals(  "text" )) {  text =                    kv[1] ; /*if(D) log( String.format("ooo %16s = %s",  "text" , text ));*/ } } catch(Exception ex) { log( String.format("*** %16s : %s\n", args[i] +"]: ", ex.getMessage()+"\n")); }
            try{ if(kv[0].toLowerCase().equals( "color" )) { color = Integer.parseInt  (kv[1]); /*if(D) log( String.format("ooo %16s = %d", "color" , color));*/ } } catch(Exception ex) { log( String.format("*** %16s : %s\n", args[i] +"]: ", ex.getMessage()+"\n")); }
            try{ if(kv[0].toLowerCase().equals( "shape" )) { shape =                    kv[1] ; /*if(D) log( String.format("ooo %16s = %s", "shape" , shape));*/ } } catch(Exception ex) { log( String.format("*** %16s : %s\n", args[i] +"]: ", ex.getMessage()+"\n")); }
            try{ if(kv[0].toLowerCase().equals(    "tt" )) {    tt =                    kv[1] ; /*if(D) log( String.format("ooo %16s = %s",    "tt" , tt   ));*/ } } catch(Exception ex) { log( String.format("*** %16s : %s\n", args[i] +"]: ", ex.getMessage()+"\n")); }

        }
        // }}}
        // NOTEPANE {{{
        // xy_wh {{{
        try {
            String[] a = xy_wh.split(",");
            x          = Integer.parseInt( a[0].trim() );
            y          = Integer.parseInt( a[1].trim() );
            w          = Integer.parseInt( a[2].trim() );
            h          = Integer.parseInt( a[3].trim() );
        } catch(Exception ex) {
            if(D) {
                log("*** parse_TABS_line xy_wh: "+ex.toString());
                log("*** ["+line+"] ***");
            }
        }

        //}}}
        // log(tt*) {{{
        if(D) {
            if(tt.contains("*"))
                log(     "args=[ #"+args.length +"]\n"
                        +" name=["+ name        +"]\n"
                        +" type=["+ type        +"]\n"
                        +"  tag=["+ tag         +"]\n"
                        +"    x=["+ x           +"] y=["+ y +"] w=["+ w +"] h=["+ h +"]\n"
                        +"    z=["+ z           +"]\n"
                        +" text=["+ text        +"]\n"
                        +"color=["+ color       +"]\n"
                        +"shape=["+ shape       +"]\n"
                        +"   tt=["+ tt          +"]\n"
                   );
        }

        // }}}
        // instanciate valid tab .. f(name type geometry) {{{
        if(        (!name.equals("")) && (!type.equals(""))
                && (w>0)
                && (h>0)
                && (type.equals("SHORTCUT") || type.equals("DASH") || type.equals("CONTROL") || type.equals("TOOL"))
          ) {

            // per-item color-index {{{
            String color_hex = Settings.Get_color_hex_from_text( tag );

            //}}}
            // DECODE [tag] {{{
            // ...restore standard character .. (from profile entry line encoded substitute)
//*TAB*/Settings.MOM(TAG_TAB, "decode_tag  ("+tag +")"); // ... involves some UTF
            tag  = NotePane.decode_tag  ( tag  );                   // encapsulate:  |=\n

//*TAB*/Settings.MOM(TAG_TAB, ".......tag =["+tag +"]");
            //}}}
            // DECODE [text] {{{
//*TAB*/Settings.MOM(TAG_TAB, "decode_label("+text+")"); // ... involves some UTF
            text = NotePane.decode_label( text );                   // ... replace: "_" -> " " .. (only for the label part)

            if( !text.startsWith("0x") )
            {
//*TAB*/Settings.MOM(TAG_TAB, "ParseUnicode("+text+")");
            text = Settings.ParseUnicode( text );                   // ... expand: "U+XXXX"    .. (parse UTF)
            }

//*TAB*/Settings.MOM(TAG_TAB, ".......text=["+text+"]");
            //}}}
            // DECODE [tt] {{{

            tt   = NotePane.decode_text ( tt   );                   // ... replace: "_" -> " "


            //}}}
            // DECORATE [decoded text] {{{
            // ...replace some keywords with a symbol
//text = text_markdown(tag, text); // FIXME: (temporary cleanup for IN-PROFILE-MISSED markdown symbols)
            boolean sep_vertical
                =  shape.equals(NotePane.SHAPE_TAG_CIRCLE) && (w/h < 2)
                || NpButton.Is_text_vertical( text )
                ;
            text = Settings.text_markup(tag, text, sep_vertical);
//TAB*/Settings.MOM(TAG_TAB, "after text_markup: ["+text+"]");

            //}}}
            // NotePane instance {{{
        //  np   =         new NotePane(name, type, x, y, w, h, z, tag, text, color, shape, tt);
            np   = NotePane.GetInstance(name, type, x, y, w, h, z, tag, text, color, shape, tt);
//TAB*/Settings.MOM(TAG_TAB, "np=["+np+"]");

            if(!TextUtils.isEmpty( color_hex )) {
                try {
                    np.bg_color =  Color.parseColor( color_hex );
                    np.fg_color = (ColorPalette.GetBrightness( np.bg_color ) < 128) ? Color.WHITE : Color.BLACK;
                } catch(Exception ignored) {
                    if(D) log("*** [XXX] DISCARDING UNKNOWN COLOR color_hex=["+ color_hex +"]");
                }
            }

            //}}}
            // WORKBENCH TOOLS RELOCATION {{{
            if(np.type == NotePane.TYPE_TOOL)
            {
                if(!NotePane.isAToolHook(np) ) np.y = 0; // dock tools near the top

                //if(D) log("WORKBENCH: "+np);
            }
            // }}}
          }
        //}}}
        //}}}
        return np;
    }
    //}}}

    // clear_TABS_Map {{{
    private void clear_TABS_Map(HashMap<String, Object> hashMap, String caller)
    {
        caller += "] [clear_TABS_Map("+get_hashMap_name(hashMap)+")";
//*TAB*/Settings.MOC(TAG_TAB, caller);
//*TAB*/Settings.MOM(TAG_TAB, "...TABS=["+hashMap.size()+" TABS]");

        if(hashMap.size() < 1) return;

        //String msg = hashMap.size() +" TABS cleared by "+ caller;

        // DISCARD ENTRIES
        NotePane.RecycleMap( hashMap );
        hashMap.clear();

        //warn_to_dash("clear_TABS_Map", msg);

        // FLAG DEPENDENCIES
        if(hashMap == TABS_Map) {
            DashNotePane                = null;
            TABS_Map_Has_Changed        = true;
        }
    }
    //}}}
    // needs_TABS_Map_ENTRY_PALETTE {{{
    public boolean needs_TABS_Map_ENTRY_PALETTE()
    {
        return (TABS_Map.get( Settings.ENTRY_PALETTE ) == null);
    }
    //}}}
    // get_np_cmd {{{
    private String get_np_cmd(NotePane np)
    {
        // Remove tag's color suffix
        String      np_cmd = Settings.Del_color_hex_from_text( np.tag );
        int            idx = np_cmd.lastIndexOf(" "); // i.e. tag may have some command arguments
        if(idx > 0) np_cmd = np_cmd.substring(0,idx).trim();
        return np_cmd;
    }
    //}}}
    //}}}
    // LAYOUT {{{
    // apply_TABS_LAYOUT .. (add and display buttons to notepanes) {{{
    public  static boolean TABS_Map_Has_Changed   = true;
    private        float   APPLIED_DEV_SCALE	  = 1F;
    private        float   APPLIED_WORKBENCH_TOOL = 0;

    @SuppressWarnings("ConstantConditions")
    public void apply_TABS_LAYOUT(HashMap<String, Object> hashMap, ViewGroup container, String caller)
    {
        caller += "] [apply_TABS_LAYOUT("+get_hashMap_name(hashMap)+" ["+hashMap.size()+" TABS]";
//*TAB*/Settings.MOC(TAG_TAB, caller);
        // NOTHING TO DISPLAY {{{
        if(hashMap.size() < 1) {
            mRTabs.sync_notify_from(caller+": NOTHING TO DISPLAY");
            return;
        }
        //}}}
        // UNCHANGED {{{
//*TAB*/Settings.MOM(TAG_TAB, "...TABS_Map_Has_Changed=["+TABS_Map_Has_Changed+"]");
        if(         (hashMap == TABS_Map)                                // USER TABS:
                && container.getVisibility() == View.VISIBLE
                && !TABS_Map_Has_Changed                                 // + TABS UNCHANGED
                && (APPLIED_DEV_SCALE        == Settings.DEV_SCALE     ) // + SCALE APPLIED
                && (APPLIED_WORKBENCH_TOOL   == Settings.WORKBENCH_TOOL) // + SELECTED TOOL OK
          )
        {
            mRTabs.sync_notify_from(caller+": UNCHANGED");
            return;
        }
        //}}}
        // TAB [NotePane button-view] {{{
        container.removeAllViews();

        // UI VIEWS PARAMS {{{
        //  boolean rescale_state   = mRTabs.get_rescale_state();
        int     buttons_count   = 0;
        int     dashes_count    = 0;
        int     tab_num         = 0;

        float   dev_scale;
        if     (hashMap == TABS_Map) { dev_scale = Settings.DEV_SCALE; APPLIED_DEV_SCALE = Settings.DEV_SCALE; }
        else if(hashMap != DOCK_Map)   dev_scale = get_fit_scale(hashMap);
        else                           dev_scale = 1; // not scaled scrollable list

        APPLIED_WORKBENCH_TOOL = Settings.WORKBENCH_TOOL;

        int x_min = 1024;
        int y_min = 1024;
        int x_max = 1;
        int y_max = 1;

    //  boolean  new_NpButton_added = false;
        boolean  hook_used          = false;
        NotePane hook_np            = null;

        //}}}

        // TABS LAYOUT {{{
    //  int wh_gap = Settings.is_GUI_STYLE_ONEDGE() ? (Settings.TAB_GRID_S / 2) : 1;
    //  int wh_gap = Settings.is_GUI_STYLE_SQUARE() ? (Settings.TAB_GRID_S / 2) : 1;
        int wh_gap = 0; // XXX
        int dock_standby_width
            = Settings.is_GUI_TYPE_HANDLES()
            ?  Handle.Get_STANDBY_WIDTH()
            :  Handle.Get_DOCK_STANDBY_WIDTH()
            ;
        boolean loading_dockring_table = (hashMap == DOCK_Map);
        for(Map.Entry<String, Object> entry : hashMap.entrySet())
        {
            if(entry.getKey() == Settings.ENTRY_PALETTE) continue;

            // [create_BLOCK_TOOL] {{{
            NotePane np = (NotePane)entry.getValue();
            if((hashMap == TABS_Map) && create_BLOCK_TOOL( np ))
            {
                if((HookRect.left != 0) || (HookRect.top != 0))
                {
                    np.x += HookRect.left;
                    np.y += HookRect.top;
                }
            }
            // }}}
            // [np.button] OR [np.textView] {{{
            else if((np.button == null) && (np.textView == null))
            {
                // DASH TABS {{{
                if( isADash(np) )
                {
                    np.set_textView( new TextView( RTabs.activity ) );

                    np. textView.setCursorVisible(false);
                    np. textView.setHint(np.name);
                    np. textView.setHorizontallyScrolling(true);
                    np. textView.setScrollbarFadingEnabled(true);
                    np. textView.setTypeface( Typeface.MONOSPACE );

                    /*
                       np. textView.setPivotY      ( 0F);  // FIXME setText() invalidate() not working with that
                       np. textView.setRotationY   (30F);
                     */
                    //  np. textView.setRotation    (10F);
                    //  np. textView.setTranslationX(20F);
                    //  np. textView.setTranslationZ(20F);

                    np. textView.setPadding(0, 0, 0, 0);

                    np. textView.setTextColor      ( DashForeColor );
                    np. textView.setBackgroundColor( DashBackColor );

                    np. textView.setOnTouchListener    ( dash_OnTouchListener     );
                //  np. textView.setOnLongClickListener( dash_OnLongClickListener );

                    // PROFILE HANGED ON HOOK
                    if(!NotePane.isATool(np) && ((HookRect.left != 0) || (HookRect.top != 0)))
                    {
                        np.x += HookRect.left;
                        np.y += HookRect.top;
                        //log("HookRect=["+ HookRect +"] used on "+ np);
                    }
                }
                //}}}
                // BUTTON TABS {{{
                else
                {
                    boolean with_outline = !loading_dockring_table;
                    layout_np_set_button(np, with_outline);

                    // TOOL URL {{{
/*
                    if(np == TOOL_URL_np) {
                        // APPLY LAST SELECTED ACTION COLOR
                        if     ( Settings.TOOL_URL_ACTION.equals(Settings.TOOL_URL_ACTION_BROWSER) ) TOOL_URL_np.setText( Settings.SYMBOL_BROWSER );
                        else if( Settings.TOOL_URL_ACTION.equals(Settings.TOOL_URL_ACTION_SENDKEY) ) TOOL_URL_np.setText( Settings.SYMBOL_SENDKEY );
                        else if( Settings.TOOL_URL_ACTION.equals(Settings.TOOL_URL_ACTION_WEBVIEW) ) TOOL_URL_np.setText( Settings.SYMBOL_WEBVIEW );
                    }
*/
                    // }}}
                }
                //}}}
            }
            //}}}

            // DISPLAY VIEW
            // SeekBar {{{
            if((hashMap == TABS_Map) && layout_BLOCK_TOOL(np, container))
            {
//*TAB*/Settings.MOM(TAG_TAB, "BLOCK_TOOL: "+ np.toString());
            }
            // }}}
            // NpButton {{{
            else if(np.button != null) {
                // TODO ... CLEAN UP THE MESS BELOW! (with this getParent hack)
                // SHOW TOOLS INFO .. (only when workbench is empty) {{{
                if(np.button.getParent() == null)
                {
                    if(        (hashMap != TABS_Map)
                            || (np.type != NotePane.TYPE_TOOL)
                            || (np.color                == 0)
                            || (Settings.WORKBENCH_TOOL == 0)
                            || (Settings.WORKBENCH_TOOL == np.color)
                      ) {
                        if(!NotePane.isAToolInfo(np) || isShowingEmptyWorkbench()) {
//TAB//Settings.MON(TAG_TAB, caller, ": container.addView( np.button )");
                            container.addView( np.button );
                        }
                      }
                }
                // }}}
                // SPOT HOOK TOOL {{{
                if( NotePane.isAToolHook(np) ) {
                    hook_np = np;
//log("hook_np=["+ hook_np +"]");
                }
                // }}}
                ++buttons_count;
            }
            //}}}
            // textView {{{
            else if(np.textView != null)
            {
                // was already parented {{{
                ViewGroup np_parent = (ViewGroup)np.textView.getParent();
                if(np_parent != null) {
//*TAB*/Settings.MON(TAG_TAB, caller, ": np_parent.removeView("+np.tag+")");
                    np_parent.removeView(np.textView);
                }
                //}}}

                container.addView( np.textView );

                ++dashes_count;
            }
            // }}}
            // hook_used {{{
            if((np.type != NotePane.TYPE_TOOL) && ((HookRect.left != 0) || (HookRect.top != 0)))
            {
                hook_used = true;
//log("hook_used=["+ hook_used +"] on "+ np);
            }
            //}}}
            // GEOMETRY {{{
            if((np.x       ) < x_min) x_min = (np.x       );
            if((np.y       ) < y_min) y_min = (np.y       );
            if((np.x + np.w) > x_max) x_max = (np.x + np.w);
            if((np.y + np.h) > y_max) y_max = (np.y + np.h);

            int x = (np.x * Settings.TAB_GRID_S);
            int y = (np.y * Settings.TAB_GRID_S);
            int w = (np.w * Settings.TAB_GRID_S);
            int h = (np.h * Settings.TAB_GRID_S);

            x *= dev_scale;
            y *= dev_scale;
            w *= dev_scale;
            h *= dev_scale;

            // dock margin
            if(hashMap == TABS_Map)
                x += dock_standby_width;

            // }}}
            // LAYOUT - BLOCK_SB_np {{{
            if(np == BLOCK_SB_np)
            {
                setLayoutParams_xy_wh(BLOCK_SB, x, y, w-wh_gap, h-wh_gap);
/*
                if(hashMap == TABS_Map)
                {
                    BLOCK_SB.setX     ( x   );
                    BLOCK_SB.setY     ( y   );
                //  BLOCK_SB.setWidth ( w-wh_gap );
                //  BLOCK_SB.setHeight( h-wh_gap );
                }
*/
            }
            //}}}
            // LAYOUT - np.button {{{
            else if(np.button != null)
            {
                if(hashMap == TABS_Map)
                {
                    np.button.setX            ( x        );
                    np.button.setY            ( y        );
                    np.button.setWidth        ( w-wh_gap );
                    np.button.setHeight       ( h-wh_gap );
                    np.button.setMinimumWidth ( w-wh_gap );  // required to override min=max constraints of TextView
                    np.button.setMinimumHeight( h-wh_gap );  // required to override min=max constraints of TextView
                //  np.button.scalable = true;
                }
                else {
                    setLayoutParams_xy_wh(np.button, x, y, w-wh_gap, h-wh_gap);
                }

                // PROFILE HISTORY
                if((hashMap == DOCK_Map) || (hashMap == PROF_Map))
                    if( is_a_visited_profile_np( np ) )
                        highlight_visited_profile_np( np );

            }
            //}}}
            // LAYOUT - np.textView {{{
            else if(np.textView != null)
            {
                if(hashMap == TABS_Map)
                {
                    np.textView.setX     ( x   );
                    np.textView.setY     ( y   );
                    np.textView.setWidth ( w-wh_gap );
                    np.textView.setHeight( h-wh_gap );
                }
                else {
                    setLayoutParams_xy_wh(np.textView, x, y, w-wh_gap, h-wh_gap);

                }
            }
            //}}}
            ++tab_num;
        }
        // }}}
//*TAB*/Settings.MOM(TAG_TAB, "...buttons_count=["+ buttons_count +"]");
//*TAB*/Settings.MOM(TAG_TAB, "...dashes_count.=["+ dashes_count  +"]");
        //}}}
        // USER TABS PADDING {{{
        if(hashMap == TABS_Map)
        {
            Rect r   = new Rect();
            r.left   = x_min;
            r.top    = y_min;
            r.right  = x_max;
            r.bottom = y_max;
            NotePane.check_neighbors(hashMap, r);

            x_min *= Settings.TAB_GRID_S;
            y_min *= Settings.TAB_GRID_S;
            x_max *= Settings.TAB_GRID_S;
            y_max *= Settings.TAB_GRID_S;
            layout_tabs_container_frame(x_min, y_min, x_max, y_max, dock_standby_width);
/*
            NotePane.check_neighbors(hashMap, NotePane.get_tabs_perimeter_rect( hashMap ));
*/
        }
        //}}}
        // USER and DOCK TABS CORNERS {{{
        //if(new_NpButton_added || (hashMap == DOCK_Map)) {
        //if(hashMap != DOCK_Map)
        else {
            NotePane.check_neighbors(hashMap, NotePane.get_tabs_perimeter_rect( hashMap ));
        }
        //}
        //}}}
        // USER TABS SCALE {{{
        if(hashMap == TABS_Map)
        {
            //log("hook_used=["+ hook_used +"]");
            if( hook_used )
                container.removeView( hook_np.button );

            TABS_Map_Has_Changed = false;

            if( container.getVisibility() != View.VISIBLE) /* (200704 .. solved tabs disappearance when calling show_cartBand with low scale) */
                container.setVisibility(     View.VISIBLE);

            container.bringToFront();

            APPLIED_DEV_SCALE    =      Settings.DEV_SCALE;
            mRTabs.set_layout_dev_scale(Settings.DEV_SCALE);

            mRTabs.magnify_np_invalidate(caller);
        }
        //}}}
        container.invalidate();
        dash_touched_down_time = 0;
        mRTabs.sync_notify_from(caller);
//HookRect  = new Rect(); // consumed
//*TAB*/Settings.MON(TAG_TAB, caller, "...done");
    }
    //}}}
    // setLayoutParams_xy_wh {{{
    private void setLayoutParams_xy_wh(View view, int x, int y, int w, int h)
    {
//*TAB*/Settings.MOC(TAG_TAB, "setLayoutParams_xy_wh("+view+")");

        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) view.getLayoutParams();
        if(rlp != null) {
            rlp.width  = w;
            rlp.height = h;
        }
        else {
            rlp = new RelativeLayout.LayoutParams(w, h);
        }

        rlp.topMargin  = y;
        rlp.leftMargin = x;

        view.setLayoutParams( rlp );
    }
    //}}}
    // layout_np_set_button {{{
    private void layout_np_set_button(NotePane np, boolean with_outline)
    {
//*TAB*/Settings.MOC(TAG_TAB, "layout_np_set_button("+np+", with_outline=["+with_outline+"])");

        if(np.button == null) np.set_button(new NpButton(RTabs.activity, np.shape, with_outline));
        else                  np.    button.set_shape(                   np.shape, with_outline);

        np.button.setTag            ( np );
        np.button.setPadding        ( 2,2,2,2 );
        np.button.setTextColor      ( NoteForeColor );
        np.button.setBackgroundColor( DashBackColor );
        np.button.setEllipsize      ( TextUtils.TruncateAt.END );

        np.button.setOnClickListener    ( np_OnClickListener     );
        np.button.setOnTouchListener    ( np_OnTouchListener     );
        np.button.setOnLongClickListener( np_OnLongClickListener );
//*XXX*/ np.button.setIncludeFontPadding(false);

        //  if( np.text.equals( Settings.APP_NAME) ) np.button.setBackgroundResource( R.drawable.logo );
        //  new_NpButton_added = true;

        // PROFILE HANGED ON HOOK
        if(!NotePane.isATool(np) && ((HookRect.left != 0) || (HookRect.top != 0)))
        {
            np.x += HookRect.left;
            np.y += HookRect.top;
            //log("HookRect=["+ HookRect +"] used on "+ np);
        }
        // WORKBENCH INFO HANGED AT HOOK VERTICAL POSITION
        else if( NotePane.isAToolInfo(np) && (HookRect.top != 0))
        {
            np.y  = HookRect.top;
            np.h  = HookRect.height();
            //log("HookRect=["+ HookRect +"] used on "+ np);
        }
    }
    //}}}
    // get_fit_scale {{{
    private float get_fit_scale(HashMap<String, Object> hashMap)
    {
        String caller = "get_fit_scale("+get_hashMap_name(hashMap)+")";

        float dev_scale = 1f;

        // GET TABS PERIMETER IN GRID UNITS
        Rect r = NotePane.get_tabs_perimeter_rect(hashMap);

        int  l = (r.left           ) * Settings.TAB_GRID_S;
        int  t = (r.top            ) * Settings.TAB_GRID_S;
        int  w = (r.right  - r.left) * Settings.TAB_GRID_S;
        int  h = (r.bottom - r.top ) * Settings.TAB_GRID_S;

        if((w != 0) && (h != 0))
        {
            // INCLUDE MARGINS
            w += 2*Settings.TAB_GRID_S;
            h += 2*Settings.TAB_GRID_S;

            // [TABS PERIMETER] / [SCREEN RATIO]
            float d_w = Settings.DISPLAY_W;// - Handle.Get_DOCK_STANDBY_WIDTH();
            float d_h = Settings.DISPLAY_H;

            dev_scale = Math.min(d_w / (float)(w), d_h / (float)(h));
        }
//*TAB*/Settings.MON(TAG_TAB, caller, "dev_scale=["+dev_scale+"]");
return dev_scale;

    }
    //}}}
    // layout_tabs_container_frame {{{
    private View       tabs_container_frame = null;
    private void layout_tabs_container_frame(int x_min, int y_min, int x_max, int y_max, int dock_standby_width)
    {
        if(tabs_container_frame == null) {
            tabs_container_frame = new View( RTabs.activity );
            tabs_container_frame.setBackgroundColor( ViewportColor  );
        }

        // MARGINS
        int margin_l = dock_standby_width;
        int margin_t = 0;
        int margin_r = (int)(Settings.DEV_SCALE * Settings.TAB_GRID_S);
        int margin_b = (int)(Settings.DEV_SCALE * Settings.TAB_GRID_S) * 2;

        // PAYLOAD
        int w        = (int)(Settings.DEV_SCALE * (x_max - x_min)) + margin_l;
        int h        = (int)(Settings.DEV_SCALE * (y_max - y_min)) + margin_b;

        // VIEWPORT AT: LEFT & TOP MARGIN
        Settings.set_VIEWPORT(margin_l, margin_t, w, h); // see also mRTabs.fit_scale_to_window


        // FRAME ADDS: RIGHT & BOTTOM MARGINS
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(1,1);
        rlp.leftMargin =     margin_l;
        rlp.topMargin  =     margin_t;
        rlp.width      = w + margin_r;
        rlp.height     = h + margin_b;
        tabs_container_frame.setLayoutParams( rlp );

        tabs_container_frame.setX     (margin_l   );
        tabs_container_frame.setY     (margin_t   );
    //  tabs_container_frame.setWidth ((float)(w + margin_r));
    //  tabs_container_frame.setHeight((float)(h + margin_b));

        tabs_container.addView( tabs_container_frame ); // removeAllViews has to have been called at each time!

//{{{
    //  if(x_min == 0) tabs_container_frame.setScrollX(-l);
    //  if(y_min == 0) tabs_container_frame.setScrollY(-t);
    //  if(x_min == 0) tabs_container_frame.setTranslationX(-l);
    //  if(y_min == 0) tabs_container_frame.setTranslationY(-t);
    //  if(x_min == 0) tabs_container_frame.setTranslationX(-4*l);
    //  if(y_min == 0) tabs_container_frame.setTranslationY(-4*t);
    //  tabs_container.setPadding(2*l,2*t,0,0);
    //  if(x_min == 0) tabs_container_frame.setTranslationX(-4*l);
    //  if(y_min == 0) tabs_container_frame.setTranslationY(-4*t);
        //int x = (x_min > 0) ? 0 : -l;
        //int y = (y_min > 0) ? 0 : -t;
        //tabs_container_frame.scrollTo(x,y);
        //tabs_container_frame.scrollTo(100,100);
        //tabs_container_frame.setTranslationX(-100 );
        //tabs_container_frame.setTranslationY(-100 );

        //tabs_container.setTranslationX(Settings.DISPLAY_W/2 - w/2);
        //tabs_container.setTranslationY(Settings.DISPLAY_H/2 - h/2);

        //tabs_container.setPivotX(Settings.DISPLAY_W/2 - w/2);
        //tabs_container.setPivotY(Settings.DISPLAY_H/2 - h/2);
        //tabs_container.setPivotX(                       w/2);
        //tabs_container.setPivotY(                       h/2);
//}}}

    }
    //}}}
    // update_TABS_LAYOUT {{{
    public void update_TABS_LAYOUT(String profile_name, HashMap<String, Object> hashMap)
    {
//*TAB*/Settings.MOC(TAG_TAB, "update_TABS_LAYOUT("+get_hashMap_name(hashMap)+"): size=["+hashMap.size()+"]");

        boolean loading_carttabs_table = (profile_name.equals( Settings.CARTTABS_TABLE ));
        boolean loading_controls_table = (profile_name.equals( Settings.CONTROLS_TABLE ));
        boolean loading_dockring_table = (profile_name.equals( Settings.DOCKINGS_TABLE ));
        boolean loading_profhist_table = (profile_name.equals( Settings.PROFHIST_TABLE ));
        boolean loading_sounds_table   = (profile_name.equals( Settings.SOUNDS_TABLE   ));
        boolean loading_profiles_table = (profile_name.equals( Settings.PROFILES_TABLE ));

        // ResetAutoPlace {{{
        if     ( loading_carttabs_table ) NpButtonGridLayout.ResetAutoPlace(NpButtonGridLayout.GRID_MULTI_COL , hashMap.size());
        else if( loading_controls_table ) NpButtonGridLayout.ResetAutoPlace(NpButtonGridLayout.GRID_MULTI_COL , hashMap.size());
        else if( loading_dockring_table ) NpButtonGridLayout.ResetAutoPlace(NpButtonGridLayout.GRID_SINGLE_COL, hashMap.size());
        else if( loading_profiles_table ) NpButtonGridLayout.ResetAutoPlace(NpButtonGridLayout.GRID_MULTI_COL , hashMap.size());
        else if( loading_profhist_table ) NpButtonGridLayout.ResetAutoPlace(NpButtonGridLayout.GRID_MULTI_COL , hashMap.size(), 3); // cols_min

        // }}}
        // [xy_wh] [typeface] [visited] {{{
        Typeface settings_typeface = Settings.getTypeface();
        for(Map.Entry<String, Object> entry : hashMap.entrySet())
        {
            if(entry.getKey() == Settings.ENTRY_PALETTE) continue;

            NotePane np = (NotePane)entry.getValue();

            if(np.button != null)
            {
                String xy_wh = NpButtonGridLayout.Get_free_xy_wh();

                // skip one slot
                if( np.tt.startsWith(Settings.SECTION_BOUNDARY) )
                    xy_wh = NpButtonGridLayout.Get_free_xy_wh();

                // xy_wh
                np.set_xy_wh( xy_wh );

                // FONT {{{
                //if(np.button.getTypeface() != settings_typeface)
                    set_np_typeface(np, settings_typeface);

                //}}}
                // visited profile history
                if((hashMap == DOCK_Map) || (hashMap == PROF_Map))
                    if( is_a_visited_profile_np( np ) )
                        highlight_visited_profile_np( np );

            }
        }
        //}}}

        // layout tabs
        layout_PROFILE( profile_name );

    }
    //}}}
    // dim_TABS undim_TABS set_TABS_dim_state {{{
    public void   dim_TABS( HashMap<String, Object> hashMap) { set_TABS_dim_state(hashMap, true ); }
    public void undim_TABS( HashMap<String, Object> hashMap) { set_TABS_dim_state(hashMap, false); }
    public void   set_TABS_dim_state(HashMap<String, Object> hashMap, boolean dim_state)
    {
//*GLOW*/Settings.MOC(TAG_GLOW, "set_TABS_dim_state("+get_hashMap_name(hashMap)+", "+dim_state+"): size=["+hashMap.size()+"]");

        // the alpha component of the 0xAArrggbb Paint color
        int opacity         = Settings.Get_OPACITY       ();
        int opacity_hidden  = Settings.Get_OPACITY_HIDDEN();
        int opacity_dimmed  = Settings.Get_OPACITY_DIMMED();

        for(Map.Entry<String, Object> entry : hashMap.entrySet())
        {
            if(entry.getKey() == Settings.ENTRY_PALETTE) continue;

            NotePane np = (NotePane)entry.getValue();
            if(np.button == null) continue;

            // DIM LEVEL
            if(dim_state) {
                Matcher matcher = Settings.URLPattern.matcher( np.tag );
                opacity
                    = (matcher.find())
                    ? opacity_dimmed
                    : opacity_hidden;
                //np.button.setBlurred(true);
            }
            else {
                //np.button.setBlurred(false);
            }
            np.button.setOpacity( opacity );
        }

    }
    //}}}
    // reparent_tabs_container {{{
//  public RelativeLayout reparent_tabs_container(ViewGroup new_parent)
    public ViewGroup      reparent_tabs_container(ViewGroup new_parent)
    {
        ViewGroup parent = (ViewGroup)tabs_container.getParent();
        if(parent != null) {
//TAB//Settings.MON(TAG_TAB, "reparent_tabs_container: parent.removeView()");
            parent.removeView( tabs_container );
        }
        new_parent.addView( tabs_container );

        return tabs_container;
    }
    // }}}
    //}}}
    // GET {{{
    // get_np_free {{{
    public NotePane get_np_free(String caller)
    {
        caller += "] [get_np_free";

        NotePane np_free = get_np_with_tag_and_free_label("", caller);

//*TABGET*/Settings.MON(TAG_TABGET, caller, "...return "+ np_free);
        return np_free;
    }
    //}}}
    // get_np_with_tag_and_free_label {{{
    public NotePane get_np_with_tag_and_free_label(String tag, String caller)
    {
        caller += "] [get_np_with_tag_and_free_label("+tag+")";
        if(TABS_Map.size() < 1)
        {
//*TABGET*/Settings.MON(TAG_TABGET, caller, "TABS_Map IS EMPTY");
            return null;
        }

        NotePane np_with_tag_and_free_label = null;
        for(Map.Entry<String, Object> entry : TABS_Map.entrySet())
        {
            if(entry.getKey() == Settings.ENTRY_PALETTE) continue;
            NotePane np = (NotePane)entry.getValue();
            if(        Settings.TAG_equals(tag, np.tag) // with_tag
                    && np.has_empty_label()              // free_label
              ) {
                np_with_tag_and_free_label = np;
                break;
              }
        }
//*TABGET*/Settings.MON(TAG_TABGET, caller, "...return "+ np_with_tag_and_free_label);
        return np_with_tag_and_free_label;
    }
    //}}}
    // get_np_with_tag {{{
    public static NotePane get_np_with_tag(String tag_pattern, HashMap<String, Object> hashMap)
    {
        String hashMap_name = get_hashMap_name(hashMap);
        String       caller = "get_np_with_tag("+tag_pattern+", "+hashMap_name+")";

        if(hashMap.size() < 1) {
//*TABGET*/Settings.MOC(TAG_TABGET, "get_np_with_tag("+tag_pattern+"): "+hashMap_name+" IS EMPTY");
            return null;
        }

        boolean fully_qualified = false;
        if( tag_pattern.endsWith("$") ) {
            fully_qualified = true;
            tag_pattern = tag_pattern.substring(0, tag_pattern.length()-1);
        }

        for(Map.Entry<String, Object> entry : hashMap.entrySet())
        {
            if(entry.getKey() == Settings.ENTRY_PALETTE) continue;
            NotePane np     = (NotePane)entry.getValue();
            String   np_tag = Settings.Del_color_hex_from_text( np.tag );
            if(        (!fully_qualified && np_tag.startsWith(tag_pattern+" ")) // i.e. tag may have some command arguments
                    ||                      np_tag.equals(    tag_pattern    )
              ) {
//*TABGET*/Settings.MOC(TAG_TABGET, "get_np_with_tag: ...return "+ np.toString());
                return np;
            }
        }

//TABGET//Settings.MON(TAG_TABGET, "get_np_with_tag("+tag_pattern+" IN "+hashMap_name+") ...return null");
        return null;
    }
    //}}}
    // get_np_with_url {{{
    public NotePane get_np_with_url(String url, String caller)
    {
        caller += "] [get_np_with_url("+url+")";
        // EMPTY TABS_Map {{{
        if(TABS_Map.size() < 1) {
//*TABGET*/Settings.MON(TAG_TABGET, caller, "TABS_Map IS EMPTY");

            return null;
        }
        //}}}
        for(Map.Entry<String, Object> entry : TABS_Map.entrySet())
        {
            if(entry.getKey() == Settings.ENTRY_PALETTE) continue;
            NotePane  np       = (NotePane)entry.getValue();
            String    np_url   = get_tag_url( np.tag );
            if( Settings.URL_equals(np_url, url) )
            {
//*TABGET*/Settings.MON(TAG_TABGET, caller, " ...return "+ np.toString()+"\n");
                return np;
            }
        }
//*TABGET*/Settings.MON(TAG_TABGET, caller, "...URL not found in ["+Settings.Working_profile+"] PROFILE");
        return null;
    }
    //}}}
/*
    // get_np_at_xy {{{
    public NotePane get_np_at_xy(int x,int y)
    {
//:!start explorer "http://stackoverflow.com/questions/10959400/how-to-find-element-in-view-by-coordinates-x-y-android"
        NotePane np = null;
        Rect r = new Rect();
        for(Map.Entry<String, Object> entry : TABS_Map.entrySet())
        {
            if(entry.getKey() == Settings.ENTRY_PALETTE) continue; // SKIP PROFILE PALETTE

            np = (NotePane)entry.getValue();
            View          v   = np.button;
            if(v == null) v = np.textView;
            if(v != null) {
                v.getHitRect( r );
                if( r.contains(x, y) ) break;       // TODO collect & select
                else                   np = null;
            }
        }
//TABGET//Settings.MON(TAG_TABGET, "get_np_at_xy("+x+","+y+")", "...return "+ np);
        return np;
    }
    //}}}
*/
    // get_hmap_np_at_xy_closest {{{
    public NotePane get_TABS_np_at_xy_closest(                                 int x,int y, String caller) { return get_hmap_np_at_xy_closest(TABS_Map, x, y, caller); }
    public NotePane get_AUTO_np_at_xy_closest(                                 int x,int y, String caller) { return get_hmap_np_at_xy_closest(AUTO_Map, x, y, caller); }
    public NotePane get_hmap_np_at_xy_closest(HashMap<String, Object> hashMap, int x,int y, String caller)
    {
        caller += "] [get_hmap_np_at_xy_closest("+get_hashMap_name(hashMap)+", "+x+", "+y+")";
//*TABGET*/Settings.MOC(TAG_TABGET, caller);

        NotePane np_found   = null;
        Rect            r = new Rect();
        for(Map.Entry<String, Object> entry : hashMap.entrySet())
        {
            if(entry.getKey() == Settings.ENTRY_PALETTE) continue; // SKIP PROFILE PALETTE

            NotePane np   = (NotePane)entry.getValue();

        //  NpButton  nb  = np.button;
            View             view = np.button;
            if(view == null) view = np.textView;
            if(view != null)
            {
                // VIEW DOES NOT CONTAINS [x,y]
                view.getHitRect( r );
                if( !r.contains(x, y) )
                    continue;

                // FOUND CONTAINING [x,y]
                if(np_found == null) {
                    np_found = np;
                }
                // FOUND ANOTHER ... SELECT f(center-offset / view size)
                else {
                    View                     view_found = np_found.button;
                    if(  view_found == null) view_found = np_found.textView;

                    view = select_nb_closest(x, y, view_found, view, caller);

                    if(view_found != view) np_found = np;
                }
            }
        }
//*TABGET*/Settings.MON(TAG_TABGET, caller, "...return "+np_found);
        return np_found;
    }
    //}}}
    // select_nb_closest {{{
    private View select_nb_closest(int x, int y, View nb1, View nb2, String caller)
    {
        caller += "] [select_nb_closest("+x+","+y+")";
//*TABGET*/Settings.MOC(TAG_TABGET, caller);
//*TABGET*/Settings.MOM(TAG_TABGET, "nb1=["+nb1+"]");
//*TABGET*/Settings.MOM(TAG_TABGET, "nb2=["+nb2+"]");

        // SELECT CLOSEST BUTTON-CENTER .. F(TOUCH OFFSET-TO-CENTER / BUTTON SIZE)

        float f1 = get_off_center_factor(x, y, nb1, caller);
        float f2 = get_off_center_factor(x, y, nb2, caller);

        View nb_closest = (f1 <= f2) ? nb1 : nb2;

//*TABGET*/Settings.MOM(TAG_TABGET, String.format("...return [%1.1f] %s [%1.1f] %s", f1, ((f1<=f2) ? "<=" : "> "), f2, nb_closest));
        return nb_closest;
    }
    //}}}
    // get_off_center_factor {{{
    private float get_off_center_factor(int x, int y, View nb, String caller)
    {
        caller += "] [get_off_center_factor("+x+","+y+","+nb+")";
//*TABGET*/Settings.MOC(TAG_TABGET, caller);

        // return 0(plain-center) to 1f(plain-edge) ... (>1f would be outside)

        float w  = nb.getWidth () / 2;
        float h  = nb.getHeight() / 2;
        float cx = nb.getX()      + w;
        float cy = nb.getY()      + h;

        float o  =    (h <= w)
            ? Math.abs(y - cy)  // (  wide rectangle ..   vertical offset to center)
            : Math.abs(x - cx); // (narrow rectangle .. horizontal offset to center)

        float f = (h < w)
            ?     (o / h)       //   wide rectangle .. f(offset / height/2)
            :     (o / w);      // narrow rectangle .. f(offset / width /2)

//*TABGET*/Settings.MON(TAG_TABGET, caller, String.format("...return %1.1f", f));
        return f;
    }
    //}}}
    // clone_np_at_bottom_left {{{
    public NotePane clone_np_at_bottom_left()
    {
        String caller = "clone_np_at_bottom_left()";
        // [np] .. (bottom-most of the left-most np.button) {{{
        NotePane    np = null;  // the lowest among the left-most
        int       left = NotePane.get_tabs_perimeter_rect(TABS_Map).right;
        int     bottom = 0;
        int bottom_max = 0;     // the lowest boundary below which to add one
        int np_count   = 0;
        for(Map.Entry<String, Object> entry : TABS_Map.entrySet())
        {
            if(entry.getKey() == Settings.ENTRY_PALETTE) continue; // SKIP PROFILE PALETTE

            np_count += 1;
            NotePane entry_np        = (NotePane)entry.getValue();
            int      entry_np_bottom = entry_np.y + entry_np.h;
            // bottom of all NotePanes (np.button or np.textView) {{{
            if(bottom_max  < entry_np_bottom)
                bottom_max = entry_np_bottom;

            // }}}
            // lower-left with a button .. (i.e. not a textView) {{{
            if(entry_np.button != null) {
                if(         (entry_np.x  < left)                                  // has to be the most to the left
                        || ((entry_np.x == left) && (entry_np_bottom >= bottom))) // ...same vertical position or lower
                {
                    left   = entry_np.x     ;
                    bottom = entry_np_bottom;
                    np     = entry_np       ; // the choosen one to clone so far
                }
            }
            //}}}
        }
        // }}}
        // [np.clone] .. (return a clone of the bottom-left np, moved below all others) {{{
        if(np != null)
        {
            np       = np.clone();
            np.y     = bottom_max;
            np.tag   =       "";
            np.text  =       "";
            np.name += ".clone";
//*TABGET*/Settings.MON(TAG_TABGET, caller, "...return A CLONE: "+ np.full_description());
        }
        //}}}
        // [new NotePane] .. f(nothing to clone from){{{
        else {
            np = new NotePane();
//*TABGET*/Settings.MON(TAG_TABGET, caller, "...return A BLANK: "+ np.full_description());
        }
// }}}
        np.name = Profile.Add_TAB(Settings.Working_profile_instance, np); // auto-renamed by Profile.store_TABS_line
        TABS_Map.put(np.name, np);
        TABS_Map_Has_Changed = true; // forget cached attributes .. (i.e. new np added)
        return np;
    }
    //}}}
    // is_an_index_profile_np {{{
    private boolean is_an_index_profile_np(NotePane np)
    {
        return np.tag.toUpperCase().contains("INDEX"   )
            || np.tag.toUpperCase().contains("CONTROLS")
            ;
    }
    //}}}
    // is_a_visited_profile_np {{{
    private boolean is_a_visited_profile_np(NotePane np)
    {
        boolean visited = false;
        // 1/2 check (Working_profile) {{{
        //...........................// 01234567
        //...........................// PROFILE 
        String       np_tag = Settings.Del_color_hex_from_text( np.tag );
        String profile_name = np_tag.substring(8);
        if(  Settings.Working_profile.equals( profile_name ) ) {
            visited = true;
//*PROFILE*/Settings.MOM(TAG_PROFILE, "is_a_visited_profile_np(np): ...return "+visited+" (Working_profile)");
        }
        //}}}
        // 2/2 check backward or forward (history stack) {{{
        else {
            int steps_to_this_Working_profile = history_count_steps_to_profile( profile_name );
            if( steps_to_this_Working_profile != 0)
            {
                visited = true;
//*PROFILE*/Settings.MOM(TAG_PROFILE, "is_a_visited_profile_np(np): ...return "+visited+" (steps_to_this_Working_profile="+steps_to_this_Working_profile+")");
            }
        }
        //}}}
        return  visited;
    }
    //}}}
    // is_a_current_profile_np {{{
    private boolean is_a_current_profile_np(NotePane np)
    {
        if( np.tag.startsWith("PROFHIST_TABLE") )
            return true;

        boolean current = false;
        try {
            String       np_tag = Settings.Del_color_hex_from_text( np.tag );
            //.....................................01234567v
            //.....................................PROFILE profile_name
            String profile_name = np_tag.substring(8);
            current
                =  Settings.Working_profile.equals( profile_name )          // current profile
                ;
        } catch(Exception ignored) { }
        return current;
    }
    //}}}
    //}}}
    // SET {{{

    // highlight_visited_profile_np {{{
    private void highlight_visited_profile_np(NotePane np)
    {
        // HIGHLIGHT RECENTLY USED PROFILES
    //  boolean visited = is_a_visited_profile_np( np );
    //  np.button.set_shape(visited ? NotePane.SHAPE_TAG_CIRCLE : NotePane.SHAPE_TAG_SQUARE);
    //  np.button.set_shape(          NotePane.SHAPE_TAG_CIRCLE                            );
        np.button.lockElevation( Settings.DOCK_VISITED_ELEVATION );
    //  np.button.setPadding(5,5,5,5);
    //  np.button.fitText();
    //  np.button.invalidate();
    }
    //}}}
    //}}}
    /** PALETTES */
    // PARSE {{{
    // parse_PALETTES {{{
    public void parse_PALETTES()
    {
//*TAB*/Settings.MOC(TAG_TAB, "@@@ parse_PALETTES:");
        // CHECK CURRENT REPLY CONTENTS {{{
        if( !StoredReply.startsWith( CMD_PALETTES_GET) ) {
            log("*** parse_PALETTES: StoredReply should contain a ["+ CMD_PALETTES_GET +"] first-line:");
            log("*** StoredReply=("+StoredReply.length()+"b) ["+ Settings.ellipsis(StoredReply, 80) +"]");
            return;
        }

        //}}}
        // CLEAR OBSOLETE DATA {{{
        clear_PALETTES_Map("parse_PALETTES");

        //}}}
        // PARSE PALETTES LINES {{{
        try {
            String[] lines = StoredReply.split("\n");

            //PALETTES_Map                    = new HashMap<>();
            PALETTES_Map.clear();
            int             len = lines.length;
            for(int i=1; i< len; ++i)
            {
                if(lines[i].startsWith( CMD_PALETTES_GET )) break;    // CMD_ACK last line

                parse_PALETTES_line( lines[i] );
            }
            /* //{{{
               if(D) {
               for(Map.Entry<String, Object> entry : PALETTES_Map.entrySet()) {
               name   = entry.getKey();
               log_nonl("["+ name +"]");

               colors = (int[])entry.getValue();
               int             colors_len = colors.length;
               for(int c=0; c< colors_len; ++c)
               log(" ["+ String.format("#%06X", (0xFFFFFF & colors[c])) +"]");
               }
               }
             */ //}}}

        }
        catch(Exception   ex) { log_ex(ex, "parse_PALETTES"); }

        //}}}
        // CONSUME PARSED DATA {{{
        StoredReply = "";

        mRTabs.set_palette_max(PALETTES_Map.size());

        //}}}
        mRTabs.sync_notify_from("parse_PALETTES(): "+ PALETTES_Map.size() +" colors ...DONE");
//*TAB*/Settings.MOM(TAG_TAB, "parse_PALETTES ...done");
    }
    //}}}
    // parse_PALETTES_line {{{
    private void parse_PALETTES_line(String line)
    {
        //if(D) log("@@@ parse_PALETTES_line("+line+"):");

        // PALETTE.9=Blues, #011f4b,#03396c,#005b96,#6497b1,#b3cde0
        int idx = line.indexOf("="); if(idx > 0) line = line.substring(idx+1).trim();

        // FIRE, #fdcf58,#757676,#f27d0c,#800909,#f07f13

        String[] words  = line.split(",");

        int[]   colors  = new int[words.length-1];

        String  name    = words[0].trim();

        if(PALETTES_Map.get(name) != null) {
            log("*** parse_PALETTES_line("+line+") ...palette=["+ name +"] already defined");
            return;
        }

        int             len = words.length;
        for(int c=1; c< len; ++c) {
            try {
                colors[c-1]     = Color.parseColor(  words[c].trim() );
            } catch(Exception   ex) {
                log_ex(ex, "parse_PALETTES_line: ["+ name +"] #"+ c +": ["+words[c]+"]");
            }
        }

        if(colors.length > 0) {
            PALETTES_Map.put(name, colors);
        }
        else {
            if(D) log("parse_PALETTES_line("+line+") ...line contains no colors");
        }

    }
    //}}}
    // clear_PALETTES_Map {{{
    private void clear_PALETTES_Map(String caller)
    {
        caller += "] [clear_PALETTES_Map";
//*TAB*/Settings.MON(TAG_TAB, caller, "PALETTES_Map=["+PALETTES_Map.size() +" PALETTES]");

        if(PALETTES_Map.size() < 1) return;

        //String msg = PALETTES_Map.size() +" PALETTES cleared by "+ caller;

        PALETTES_Map.clear();

        //warn_to_dash("clear_PALETTES_Map", msg);

        mRTabs.set_palette_max(0);
    }
    //}}}

    //}}}
    // LAYOUT [setTypeface] {{{
    // palette_prev() {{{
    public void palette_prev()
    {
        if(D) log("@@@ palette_prev():");

        // NEXT PALETTE
        if(PALETTES_Map.size() < 1)
        {
            log("*** palette_prev PALETTES("+PALETTES_Map.size()+")");
            return;
        }

        int palette_num = get_PALETTE_NUM( Settings.PALETTE );
        palette_num     = (PALETTES_Map.size()*2 + palette_num - 1) % PALETTES_Map.size(); // wrap

        apply_PALETTE_NUM(TABS_Map, palette_num, "palette_prev");
    }
    //}}}
    // palette_next() {{{
    public void palette_next()
    {
        if(D) log("@@@ palette_next():");

        // NEXT PALETTE
        if(PALETTES_Map.size() < 1)
        {
            log("*** palette_next PALETTES("+PALETTES_Map.size()+")");
            return;
        }

        int palette_num = get_PALETTE_NUM( Settings.PALETTE );
        palette_num     = (PALETTES_Map.size()   + palette_num + 1) % PALETTES_Map.size(); // wrap

        apply_PALETTE_NUM(TABS_Map, palette_num, "palette_next");
    }
    //}}}
    // apply_PALETTE_NUM(palette_num) {{{
    public void apply_PALETTE_NUM(HashMap<String, Object> hashMap, int palette_num, String caller)
    {
        //if(D) log("@@@ "+caller+".apply_PALETTE_NUM("+ palette_num +"):");
        if(PALETTES_Map.size() > 0)
        {
            Settings.PALETTE = get_PALETTE_NAME( palette_num );

            // OVERRIDE PROFILE ASSOCIATED PALETTE (160212)
            TABS_Map.put(Settings.ENTRY_PALETTE, PALETTES_Map.get( Settings.PALETTE ));
//int[] pc = (int[])TABS_Map.get( Settings.ENTRY_PALETTE );
//TAB//Settings.MON(TAG_TAB, "OVERRIDE PROFILE ASSOCIATED PALETTE .. ("+ pc.length +" color)");

            // UPDATE DISPLAY
            apply_SETTINGS_PALETTE(hashMap, caller);
        }
    }
    //}}}
    // apply_SETTINGS_PALETTE() {{{
    public void apply_SETTINGS_PALETTE(HashMap<String, Object> hashMap, String caller)
    {
        caller += "] [apply_SETTINGS_PALETTE("+get_hashMap_name(hashMap)+")";
//*TAB*/Settings.MOC(TAG_TAB, caller);

        // ? PALETTE NOT SET {{{
        if(D) log("... PALETTE=["+ Settings.PALETTE +"] ["+ Settings.SYMBOL_ALPHA+Settings.OPACITY +"]");
        if(Settings.PALETTE.equals(""))
        {
            log("*** "+caller+": PALETTE NAME NOT SET");
            return;
        }
        // }}}

        boolean loading_carttabs_table = (hashMap == CART_Map);
        boolean loading_controls_table = (hashMap == CTRL_Map);
        boolean loading_dockring_table = (hashMap == DOCK_Map);
        boolean loading_profhist_table = (hashMap == AUTO_Map);
        boolean loading_profiles_table = (hashMap == PROF_Map);

        // INITIALIZE PALETTE-COLORS..USER-TABS ASSOCIATION {{{
        if(!loading_carttabs_table && !loading_controls_table && !loading_dockring_table && !loading_profiles_table)
        {
            // ASSOCIATE CURRENT PALETTE COLORS TO PROFILE TABS
            if(TABS_Map.get( Settings.ENTRY_PALETTE ) == null) {
                TABS_Map.put(Settings.ENTRY_PALETTE, PALETTES_Map.get( Settings.PALETTE ));
//int[] pc = (int[])TABS_Map.get( Settings.ENTRY_PALETTE );
//TAB//Settings.MON(TAG_TAB, "INITIALIZE PALETTE-COLORS..USER-TABS ASSOCIATION .. ("+ pc.length +" color)");
            }
        }
        //}}}
        // SELECT PALETTE COLORS .. [CONTROL] [PROFILE] [USER-TABS] {{{
        if     (loading_carttabs_table) use_palette_colors( COLOR_TABS );
        else if(loading_controls_table) use_palette_colors( COLOR_CTRL );
        else if(loading_dockring_table) use_palette_colors( COLOR_DOCK );
        else if(loading_profiles_table) use_palette_colors( COLOR_PROF );
        else                            use_palette_colors( COLOR_TABS );

        //}}}
        // TABS COLORS - f(Settings.PALETTE) {{{
        // colors opacity tab_num {{{

    //  mRTabs.bg_view.getBackground().setAlpha( opacity_255 );
    //  mRTabs.bg_view                .setAlpha((float)Settings.OPACITY / 100f);

        int grp_num     = 0;
        int tab_num     = 0;
        int opacity_255 = (int)(255 * Settings.OPACITY / 100.0); // the 0xffxxxxxx color component

        // }}}
        // FONT [setTypeface] {{{
        Typeface settings_typeface = Settings.getTypeface();

        //}}}
        int fg_color, bg_color;
        for(Map.Entry<String, Object> entry : hashMap.entrySet())
        {
            // SKIP PROFILE PALETTE
            if(entry.getKey() == Settings.ENTRY_PALETTE) continue;

            // DEFAULT COLOR {{{
            bg_color = DashBackColor;
            fg_color = DashForeColor;
            //}}}
            // RENDER PROFILE TABS
            NotePane np = (NotePane)entry.getValue();
            if((np == BLOCK_SB_np) && (BLOCK_SB != null))
            {
                // ITEM-SPECIFIC COLOR {{{
                if(        (np.bg_color != NotePane.NO_COLOR)
                        || (np.fg_color != NotePane.NO_COLOR)
                ) {
                    if(     np.bg_color != NotePane.NO_COLOR) bg_color = np.bg_color;
                    if(     np.fg_color != NotePane.NO_COLOR) fg_color = np.fg_color;
                }
                //}}}
                // PALETTE-ALLOCATED COLOR {{{
                else {
                    // getBackColor {{{
                    if(np.color != 0) { bg_color = get_tabs_bg_color( np.color-1                  ); }
                    else              { bg_color = get_tabs_bg_color( tab_num + Settings.RESCOLORS); }
                    // }}}
                    // setForeColor {{{

                    fg_color = getTextColor(COLOR_TABS, bg_color);

                    //BLOCK_SB.setTextColor( fg_color );
                    //LayerDrawable ld = (LayerDrawable) BLOCK_SB.getProgressDrawable();
                    //ClipDrawable  d1 = (ClipDrawable)ld.findDrawableByLayerId(R.id.progressshape);
                    //d1.setColorFilter(fg_color, PorterDuff.Mode.SRC_IN);
                    // https://android-arsenal.com/details/1/2084
                    //BLOCK_SB.setProgressColor          ( Color.CYAN );
                    //BLOCK_SB.setThumbColor             ( Color.RED  );
                    //BLOCK_SB.setProgressBackgroundColor( Color.BLUE );
                    //BLOCK_SB.setThumbAlpha             ( 128        ); // you can also set alpha value for the thumb

                    // }}}
                }
                BLOCK_SB.setBackgroundColor                  ( bg_color );
                BLOCK_SB.getProgressDrawable().setColorFilter( fg_color , PorterDuff.Mode.SRC_IN);
                // }}}
                // OPACITY {{{
            //  BLOCK_SB.setOpacity( opacity_255 );

                // }}}
            }
            else if(np.button != null) {
                // ITEM SPECIFIED COLORS {{{
                if(        (np.bg_color != NotePane.NO_COLOR)
                        || (np.fg_color != NotePane.NO_COLOR)
                  )
                {
                    if(     np.bg_color != NotePane.NO_COLOR) bg_color = np.bg_color;
                    if(     np.fg_color != NotePane.NO_COLOR) fg_color = np.fg_color;
                }
                else if( NotePane.isATool(np) )
                {
                    if(D) log("NotePane.isATool: KEEP TOOL DEFAULT COLORS FOR "+ np.toString());
                }
                //}}}
                // PALETTE COLORS {{{
                else {
                    // np_cmd {{{
                    String np_cmd = get_np_cmd( np );

                    //}}}
                    // bg_color {{{
                    // DOCKINGS_TABLE {{{
                    if(      loading_dockring_table ) {

                    //  if( (np.shape ==     NotePane.SHAPE_TAG_SQUARE) && (tab_num > 0)) /*...................*/ grp_num += 1;
                        if( np.tt.startsWith(Settings.SECTION_BOUNDARY) && (tab_num > 0)) /*...................*/ grp_num += 1;

                        if     ( is_an_index_profile_np ( np ) )        bg_color = Settings.BG_COLOR_DOCK_INDEX;
                        else if( is_a_visited_profile_np( np ) )        bg_color = Settings.BG_COLOR_DOCK_VISITED;
                        else                                            bg_color = get_dock_bg_color(             grp_num    );
                    }
                    //}}}
                    // PROFILES_TABLE {{{
                    else if( loading_profiles_table ) {

                        if     ( is_an_index_profile_np ( np ) )        bg_color = Settings.BG_COLOR_DOCK_INDEX;
                        else if( is_a_visited_profile_np( np ) )        bg_color = Settings.BG_COLOR_DOCK_VISITED;
                        else                                            bg_color = Settings.BG_COLOR_PROF;
                    }

                    // }}}
                    // PROFHIST_TABLE {{{
                    else if( loading_profhist_table ) {

                        if     ( is_a_current_profile_np( np ) )        bg_color = Settings.BG_COLOR_DOCK_CURRENT;
                        else if( is_an_index_profile_np ( np ) )        bg_color = Settings.BG_COLOR_DOCK_INDEX;
                        else if( is_a_visited_profile_np( np ) )        bg_color = Settings.BG_COLOR_DOCK_VISITED;
                        else                                            bg_color = Settings.BG_COLOR_PROF;
                    }

                    // }}}
                    // CARTTABS_TABLE {{{
                    else if( loading_carttabs_table ) {                 bg_color = Settings.BG_COLOR_PROF; } // should use clone color

                    // }}}
                    // CONTROLS_TABLE {{{
                    else if( loading_controls_table ) {
                        // CONTROLS PROFILES - PRPREV PRPREV INVENTORY PROFILETABS {{{
                        if     ( np_cmd .equals(  "PROFILES_TABLE"))    bg_color = Settings.BG_COLOR_PROF; // show PROFILES_TABLE (bridging)
                        else if( np_cmd .equals(  "PROFHIST_TABLE"))    bg_color = Settings.BG_COLOR_PROF;
                        else if( np_cmd .equals(    "PRPREV"      ))    bg_color = Settings.BG_COLOR_PROF;
                        else if( np_cmd .equals(    "PRNEXT"      ))    bg_color = Settings.BG_COLOR_PROF;
                        else if( np_cmd .equals(    "INVENTORY"   ))    bg_color = Settings.BG_COLOR_PROF;
                        else if( np_cmd .equals(    "PROFILETABS" ))    bg_color = Settings.BG_COLOR_PROF;

                        //}}}
                        // CONTROLS SERVER - SIGNIN SERVER FREEZED OFFLINE FINISH - HIDE LOGGING CLEAR {{{
                        else if( np_cmd .equals(    "SIGNIN"      ))    bg_color = Settings.BG_COLOR_SERVER;
                        else if( np_cmd .equals(    "SERVER"      ))    bg_color = Settings.BG_COLOR_SERVER; // SERVER CONNEXION (dynamically overriden)
                        else if( np_cmd .equals(    "FREEZED"     ))    bg_color = Settings.BG_COLOR_SERVER;
                        else if( np_cmd .equals(    "OFFLINE"     ))    bg_color = Settings.BG_COLOR_SERVER;
                        else if( np_cmd .equals(    "FINISH"      ))    bg_color = Settings.BG_COLOR_SERVER;

                        else if( np_cmd .equals(    "HIDE"        ))    bg_color = Settings.BG_COLOR_SERVER;
                        else if( np_cmd .equals(    "LOGGING"     ))    bg_color = Settings.BG_COLOR_SERVER;
                        else if( np_cmd .equals(    "CLEAR"       ))    bg_color = Settings.BG_COLOR_SERVER;

                        // }}}
                        // CONTROLS INFO - STATUS MEMORY SAVE NOTE {{{
                        else if( np_cmd .equals(    "STATUS"      ))    bg_color = Settings.BG_COLOR_INFO;
                        else if( np_cmd .equals(    "MEMORY"      ))    bg_color = Settings.BG_COLOR_INFO;
                        else if( np_cmd .equals(    "SAVE"        ))    bg_color = Settings.BG_COLOR_INFO;
                        else if( np_cmd .equals(    "NOTE"        ))    bg_color = Settings.BG_COLOR_INFO;

                        // }}}
                        // CONTROLS WEB - TOOL_URL WEBVIEW BOOKMARK {{{
                        else if( np_cmd .equals(    "TOOL_URL"    ))    bg_color = Settings.BG_COLOR_WEB; // WEB ACTION (dynamically overriden)
                        else if( np_cmd .equals(    "WEBVIEW"     ))    bg_color = Settings.BG_COLOR_WEB; // last visited
                        else if( np_cmd .equals(    "BOOKMARK"    ))    bg_color = Settings.BG_COLOR_WEB; // last bookmarked
                        else if( np_cmd .equals(    "DEL_COOKIES" ))    bg_color = Settings.BG_COLOR_WEB; // remove all cookies
                        else if( np_cmd .equals(    "SAVE_COOKIES" ))   bg_color = Settings.BG_COLOR_WEB; // save cookies to file
                        else if( np_cmd .equals(    "LOAD_COOKIES" ))   bg_color = Settings.BG_COLOR_WEB; // save cookies to file

                        // }}}
                        // CONTROLS PALETTE - GUI_STYLE GUI_FONT GUI_TYPE - PLNEXT PLPREV FIT_W FIT_H {{{
                        else if( np_cmd .equals(    "GUI_STYLE"      )) bg_color = Settings.BG_COLOR_GUI;
                        else if( np_cmd .equals(    "GUI_FONT"       )) bg_color = Settings.BG_COLOR_GUI;
                        else if( np_cmd .equals(    "GUI_TYPE"       )) bg_color = Settings.BG_COLOR_GUI;
                        else if( np_cmd .equals(    "MARK_SCALE_GROW")) bg_color = Settings.BG_COLOR_GUI;
                        else if( np_cmd .equals(    "PLNEXT"         )) bg_color = Settings.BG_COLOR_GUI;
                        else if( np_cmd .equals(    "PLPREV"         )) bg_color = Settings.BG_COLOR_GUI;
                        else if( np_cmd .equals(    "FIT_W"          )) bg_color = Settings.BG_COLOR_GUI;
                        else if( np_cmd .equals(    "FIT_H"          )) bg_color = Settings.BG_COLOR_GUI;

                        else if( np_cmd .equals(    "LANDSCAPE"      )) bg_color = Settings.BG_COLOR_GUI;
                        else if( np_cmd .equals(    "PORTRAIT"       )) bg_color = Settings.BG_COLOR_GUI;
                        else if( np_cmd .equals(    "SCREEN_ROTATION")) bg_color = Settings.BG_COLOR_GUI;
                        // }}}
                        // CONTROLS LOG - MONITOR LOG* ERRLOGGED {{{
                        else if( np_cmd .equals(    "MONITOR"      ))   bg_color = Settings.BG_COLOR_LOG;
                        else if( np_cmd .equals(    "LOG"          ))   bg_color = Settings.BG_COLOR_LOG;
                        else if( np_cmd .equals(    "LOG_ACTIVITY" ))   bg_color = Settings.BG_COLOR_LOG;
                        else if( np_cmd .equals(    "LOG_CAT"      ))   bg_color = Settings.BG_COLOR_LOG;
                        else if( np_cmd .equals(    "LOG_CLIENT"   ))   bg_color = Settings.BG_COLOR_LOG;
                        else if( np_cmd .equals(    "LOG_PARSER"   ))   bg_color = Settings.BG_COLOR_LOG;
                        else if( np_cmd .equals(    "LOG_PROFILE"  ))   bg_color = Settings.BG_COLOR_LOG;
                        else if( np_cmd .equals(    "LOG_SETTINGS" ))   bg_color = Settings.BG_COLOR_LOG;
                        else if( np_cmd .equals(    "ERRLOGGED"    ))   bg_color = Settings.BG_COLOR_LOG;

                        // }}}
                        else if( np_cmd .equals(    "COMMAND"     ))    bg_color = Settings.BG_COLOR_CTRL;
                        // CATCH SOME
                        else if( np.text.contains(  "PROFILE"      ))   bg_color = Settings.BG_COLOR_PROF;
                        // CATCH ALL
                        else                                            bg_color = Settings.BG_COLOR_CTRL;
                    }
                    // }}}
                    // TABS {{{
                    else if(np.color != 0)                    {         bg_color = get_tabs_bg_color( np.color-1                  ); }
                    else                                      {         bg_color = get_tabs_bg_color( tab_num + Settings.RESCOLORS); }
                    //}}}
/*              // TOOL URL ACTION COLOR OVERRIDE {{{
                    if(np == TOOL_URL_np) {
                        if     ( Settings.TOOL_URL_ACTION.equals(Settings.TOOL_URL_ACTION_BROWSER) ) bg_color = Color.RED;
                        else if( Settings.TOOL_URL_ACTION.equals(Settings.TOOL_URL_ACTION_WEBVIEW) ) bg_color = Color.BLUE;
                    }
*/ //}}}
                    // }}}
                    // fg_color {{{
                    if(     loading_carttabs_table) {
                        /*...................................*/ fg_color = getTextColor(COLOR_TABS, bg_color);
                    }
                    else if(loading_profiles_table) {
                        if     ( np.getLabel().endsWith  ("*")) fg_color = Settings.FG_COLOR_PROF_NEW;
                        else                                    fg_color = getTextColor(COLOR_PROF, bg_color);//Settings.FG_COLOR_PROF    ;
                    }
                    else if(loading_dockring_table) {
                        if     ( np.getLabel().endsWith  ("*")) fg_color = Settings.FG_COLOR_DOCK_NEW;
                        else                                    fg_color = getTextColor(COLOR_DOCK, bg_color);//Settings.FG_COLOR_DOCK;
                    }
                    else if(loading_controls_table) {
                        /*...................................*/ fg_color = getTextColor(COLOR_CTRL, bg_color);//Settings.FG_COLOR_CTRL;
                    }
                    else {
                        /*...................................*/ fg_color = getTextColor(COLOR_TABS, bg_color);
                    }

                    // }}}
                }
                np.button.setBackgroundColor( bg_color );
                np.button.setTextColor      ( fg_color );

                //}}}
                // SHADOW {{{
                np.button.release(); // time to check Settings.GUI_STYLE_HAS_CHANGED and call invalidateOutline

                //}}}
                // OPACITY {{{
                if(!loading_carttabs_table && !loading_controls_table && !loading_dockring_table && !loading_profiles_table)
                {
                    np.button.setOpacity( opacity_255 );
                }

                // }}}
/* @see NpButton._selectOutlineProvider
                // ELEVATION {{{
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    // same elevation for same color (better fit for adjacent surfaces)
                    if(np.color != 0) {
                        np.elevation = (6-np.color) * 3;    // i.e. 5 for the first color
                    }
                    else if( loading_dockring_table ) {
                        np.elevation = Settings.TAB_GRID_S/5;
                    }
                    else {
                        int d_factor = (int)Math.sqrt(np.w*np.w + np.h*np.h) % 8;
                        int x_factor = np.x                                  % 3;
                        int y_factor = np.y                                  % 3;
                        int n_factor = tab_num                               % 5;
                        np.elevation = d_factor + x_factor + y_factor + n_factor;
                    }

                    //np.button.setElevation( np.elevation );
                }
                //}}}
*/
                // FONT {{{
                //if(np.button.getTypeface() != settings_typeface)
                    set_np_typeface(np, settings_typeface);

                //}}}
//                // hasGlyph .. (for single character or a ligature) {{{
//
//                // java.lang.NoSuchMethodError:
//                // No virtual method hasGlyph(Ljava/lang/String;)
//                // in class Landroid/text/TextPaint;
//                // or its super classes
//                // (declaration of 'android.text.TextPaint' appears in /system/framework/framework.jar:classes2.dex)
//
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//                {
//                    if(!np.button.getPaint().hasGlyph( np.getLabel() ))
//                    {
////*TAB*/Settings.MON(TAG_TAB, "apply_SETTINGS_PALETTE", "!hasGlyph("+ Settings.ellipsis(np.getLabel(), 32) +")");
//                        np.button.setBackgroundColor( Settings.COLOR_NAVY   );
//                        np.button.setTextColor      ( Settings.COLOR_INDIGO );
//                    }
//                }
//                //}}}
            }
            ++tab_num;
        }
        //}}}
        // NOTIFY PALETTE SETTINGS {{{
        if(!loading_profiles_table && !loading_profhist_table && !loading_dockring_table && !loading_controls_table)
        {
            // SHOW CURRENTLY SELECTED PALETTE
            int palette_num               = get_PALETTE_NUM( Settings.PALETTE );
            mRTabs.set_palette_num_name ( palette_num,     Settings.PALETTE );
            mRTabs.sync_notify_from("apply_SETTINGS_PALETTE("+Settings.PALETTE +"): OPACITY=["+ Settings.SYMBOL_ALPHA+Settings.OPACITY +"] ...APPLIED");
        }
        //}}}
    }
    //}}}

    // COLORS {{{

    private static final int COLOR_TABS         = 0;
    private static final int COLOR_DOCK         = 1;
    private static final int COLOR_PROF         = 2;
    private static final int COLOR_CTRL         = 3;

    private int[] palette_colors;
    private int    color_primary;
    private int    color_secondary;
    private int    color_accent;
    private int    color_bg_1;
    private int    color_bg_2;
    // }}}
    // use_palette_colors {{{
    private void use_palette_colors(int color_category)
    {
        // DOCK PROF CTRL {{{
        palette_colors = null;
        if(     color_category == COLOR_DOCK) palette_colors  = Settings.COLORS_DOCK;
        else if(color_category == COLOR_PROF) palette_colors  = Settings.COLORS_PROF;
        else if(color_category == COLOR_CTRL) palette_colors  = Settings.COLORS_CTRL;
        if(palette_colors != null)
        {
            color_primary   = palette_colors[0];
            color_secondary = palette_colors[1];
            color_accent    = palette_colors[2];
            color_bg_1      = palette_colors[3];
            color_bg_2      = palette_colors[4];
            if(ColorPalette.GetBrightness( color_primary   ) >  32) color_primary   = Color.BLACK;
            if(ColorPalette.GetBrightness( color_secondary ) < 224) color_secondary = Color.WHITE;
        }
        //}}}
        // TABS {{{
        else {

            // PROFILE PALETTE {{{
            palette_colors = (int[])TABS_Map.get( Settings.ENTRY_PALETTE );
//TAB//Settings.MON(TAG_TAB, "use_palette_colors: PROFILE'S PALLETTE COLORS=["+ ((palette_colors==null) ? "null" : palette_colors.length+" colors") +"]");

            // }}}
            // DEFAULT TO CURRENT PALETTE {{{
            if((palette_colors == null) || (palette_colors.length < 2)) {
                palette_colors = (int[])PALETTES_Map.get( Settings.PALETTE       );
//TAB//Settings.MON(TAG_TAB, "use_palette_colors: DEFAULT TO CURRENT Settings.PALETTE=["+ Settings.PALETTE +"] COLORS=["+ ((palette_colors==null) ? "null" : palette_colors.length+" colors") +"]");
            }

            // }}}
            // FALLBACK TO BUILT-IN PALETTE {{{
            if((palette_colors == null) || (palette_colors.length < 2)) {
                palette_colors = Settings.COLORS_DFLT;
//TAB//Settings.MON(TAG_TAB, "use_palette_colors: DEFAULT TO DEFAULT PALETTE COLORS=["+ ((palette_colors==null) ? "null" : palette_colors.length+" colors") +"]");
            }

            // }}}

            Settings.PALCOLORS = palette_colors.length;

            if((Settings.OPACITY <= 25))
            {
                color_primary       = Color.BLACK;
                color_secondary     = Color.GRAY;
                color_accent        = Settings.COLOR_ORANGE    ;
                color_bg_1          = Settings.COLOR_FIREBRICK ;
                color_bg_2          = Settings.COLOR_BROWN     ;
            }
            else {

                if(palette_colors.length >= 2) {
                    color_primary   = ColorPalette.GetDarkestColor  ( palette_colors );
                    color_secondary = ColorPalette.GetBrightestColor( palette_colors );
                }
                color_accent        = Settings.COLOR_ORANGE;
                color_bg_1          = Settings.COLOR_BROWN ;

                if(ColorPalette.GetBrightness( color_primary   ) >  32) color_primary   = Color.BLACK;
                if(ColorPalette.GetBrightness( color_secondary ) < 224) color_secondary = Color.WHITE;
            }
        }
        // }}}
    }
    //}}}
    // get_tabs_bg_color get_dock_bg_color {{{
    private int get_tabs_bg_color(int color_idx) { return _get_bg_color(COLOR_TABS, color_idx); }
    private int get_dock_bg_color(int color_idx) { return _get_bg_color(COLOR_DOCK, color_idx); }
    private int _get_bg_color(int color_category_type, int color_idx)
    {
        // reserved_colors are only for for COLOR_TABS with a non-null color field
        int color;
        // COLOR_TABS COLOR_DOCK {{{
        if(palette_colors.length > 0)
        {
            // one of the reserved colors
            if(color_idx < Settings.RESCOLORS) {

                // may be clipped by Settings
                if((Settings.MAXCOLORS > 0) && (Settings.MAXCOLORS < (color_idx+1)))
                    color_idx = Settings.MAXCOLORS -1;

                // within current palette range
                color_idx %= palette_colors.length;
            }
            else {
                // use full color ranges by default
                int maxcolors = palette_colors.length;

                // unless clipped by Settings
                if((Settings.MAXCOLORS > 0) && (Settings.MAXCOLORS < maxcolors))
                    maxcolors = Settings.MAXCOLORS;

                // and try to skip reserved colors
                if(maxcolors  > Settings.RESCOLORS) color_idx = Settings.RESCOLORS + (color_idx % (maxcolors - Settings.RESCOLORS));
                else                                color_idx = maxcolors-1;

                // within current palette range
                color_idx %= palette_colors.length;
            }

            color = palette_colors[ color_idx ];

            //np.button.setText(String.format("%2d / %2d (-%d)", color_idx, maxcolors, Settings.RESCOLORS));
        }
        else {
            color = color_primary;
        }
        // }}}
        return color;
    }
    //}}}
    // getTextColor {{{
    private int getTextColor(int color_category_type, int bg_color)
    {
        int color;
        if     (color_category_type == COLOR_TABS) {        // f(palette)
            if(ColorPalette.GetBrightness( bg_color ) < 128) color = color_secondary;
            else                                             color = color_primary;
        }
    //  else if(color_category_type == COLOR_PROF        ) { color = Settings.FG_COLOR_PROF;     }
    //  else if(color_category_type == COLOR_DOCK        ) { color = Settings.FG_COLOR_DOCK;     }
    //  else                        /* COLOR_CTRL       */ { color = Settings.FG_COLOR_CTRL;     }
        else {
            if(ColorPalette.GetBrightness( bg_color ) < 128) color = color_secondary;
            else                                             color = color_primary;
        }
        return color;
    }
    //}}}
    // get_PALETTE_NAME(palette_num) {{{
    private String get_PALETTE_NAME(int palette_num)
    {
        if(PALETTES_Map.size() > 0)
        {
            int entry_num = 0;
            for(Map.Entry<String, Object> entry : PALETTES_Map.entrySet())
            {
                if(++entry_num == palette_num)
                    return entry.getKey();
            }
        }
        return "";
    }
    //}}}
    // get_PALETTE_NUM(palette_name) {{{
    private int get_PALETTE_NUM(String palette_name)
    {
        int palette_num = 0;
        for(Map.Entry<String, Object> entry : PALETTES_Map.entrySet())
        {
            ++palette_num;
            if( entry.getKey().equals( palette_name ) )
                return( palette_num );
        }
        return 0;
    }
    //}}}
    // get_PALETTE_COUNT {{{
    public int get_PALETTE_COUNT()
    {
        return PALETTES_Map.size();
    }
    //}}}
    /*
    // get_PALETTES_Color {{{
    private int get_PALETTES_Color(String palette_name, int index)
    {
    int[] colors = (int[])PALETTES_Map.get( palette_name );
    if(colors == null) return DashBackColor;

    int   color  = colors[index % colors.length];

    //if(D) log(Settings.SYMBOL_LEFT_ARROW+"@@@ get_PALETTES_Color(palette_name=["+ palette_name +"], index=["+ index +"]): ...return color=["+ color +"]\n");

    return color;
    }
    //}}}
     */
    /** FONT */
    // set_np_typeface {{{
    private static Typeface NotoTypeface;

    private void set_np_typeface(NotePane np, Typeface settings_typeface)
    {
/*
            np.button.setTypeface( settings_typeface );
*/
        if(NotoTypeface == null)
            NotoTypeface = Settings.getNotoTypeface();

        boolean np_has_markup       = Settings.has_label_markup( np.button.getText().toString() );
        boolean typeface_has_markup = (settings_typeface == NotoTypeface);

        // CANNOT USE Typeface .. (UN)TRIMM displayed text
        if( np_has_markup )
        {
            // ADD OR CLEAR MISSING SYMBOLS MARKER
        //  if( !typeface_has_markup ) np.button.setText("["+ np.getLabel() +"]");
        //  else                       np.button.setText(     np.getLabel()     );
        //  if( !typeface_has_markup ) np.button.setSelected( true);
        //  else                       np.button.setSelected(false);
        //  if( !typeface_has_markup ) np.button.setPadding(5,5,5,5);
        //  else                       np.button.setPadding(2,2,2,2);
            if( !typeface_has_markup ) np.button.press();
            else                       np.button.release();
        //  if( !typeface_has_markup ) np.button.setBackgroundColor( Settings.COLOR_NAVY   );
        //  else                       np.button.setTextColor      ( Settings.COLOR_INDIGO );
        }
        // CAN USE Typeface
        if(!np_has_markup || typeface_has_markup)
        {
            np.button.setTypeface( settings_typeface );
            np.needs_fitText         = true;
        }
//boolean np_text_showing_utf = (np.text.startsWith("0x") && (np.text.length() >= 6));
//TAB//if(np_text_showing_utf) log("np_text_showing_utf:("+ Settings.ellipsis(np.text,24) +")");

    }
    //}}}
    // }}}
    /** CONTROLS */
    // clear_CTRL_Map {{{
    public void clear_CTRL_Map(String caller)
    {
        caller += "] [clear_CTRL_Map";
//*TAB*/Settings.MON(TAG_TAB, caller, "CTRL_Map=["+CTRL_Map.size() +" CONTROLS]");

        if(CTRL_Map.size() < 1) return;

        //String msg = CTRL_Map.size() +" CONTROLS cleared by "+caller;

        NotePane.RecycleMap( CTRL_Map );
        CTRL_Map.clear();

        //warn_to_dash("clear_CTRL_Map", msg);
    }
    //}}}
    /** LOG */
    //{{{
    // LOG {{{
    // Private Log StringBuilder {{{
    private static final  StringBuilder   Log_sb = new StringBuilder();
    private static void   Log_init  (String msg) { Log_sb.delete(0, Log_sb.length()); Log_append   ( msg); }
    private static void   Log_append(String msg) { Log_sb.append(msg);                Log_sb.append("\n"); }
    private static String Log_toString()         { return Log_sb.toString(); }
    //}}}
    // log {{{
    private void log(String msg) {
        if(Looper.myLooper() == Looper.getMainLooper())    MLog.log_red(            msg);
        else                                               Settings.MOM(TAG_CLIENT, msg);
    }
    private void log_nonl  (String msg)                  { MLog.log_nonl  ( msg ); }
    private void log_left  (String msg)                  { MLog.log_left  ( msg ); }
    private void log_center(String msg)                  { MLog.log_center( msg ); }
    private void log_right (String msg)                  { MLog.log_right ( msg ); }
    private void log_status(String msg)                  { MLog.log_status( msg ); }
    private void log_ex    (Exception ex, String caller) { MLog.log_ex(ex, caller); }

    //}}}
    //}}}
    // comm_status_toString {{{
    private String comm_status_toString()
    {

        /* 1 */ boolean    connecting = (         ConnectTask != null         );
        /* 2 */ boolean     connected = (       isConnected()                 );

        /* 3 */ boolean       polling = (       last_sent_cmd.equals(CMD_POLL));

        /* 4 */ boolean       reading = (            ReadTask != null         );
        /* 5 */ boolean      answered = (     has_last_sent_cmd_ack()) || (polling);

        /* 6 */ boolean have_palettes = ( PALETTES_Map.size()  > 0            ); String p = PALETTES_Map.size() + Settings.SYMBOL_PALETTE;
        /* 7 */ boolean     have_tabs = (     TABS_Map.size()  > 0            ); String t = TABS_Map    .size() + Settings.SYMBOL_TABS;

        /* 7 */ boolean       freezed = (              Settings.FREEZED  );
        /* 8 */ boolean       offline = (              Settings.OFFLINE  );

        String w = Settings.SYMBOL_WARN;
        String g = Settings.SYMBOL_GOOD;
        String i = Settings.SYMBOL_IDLE;
        String f = Settings.SYMBOL_freezed;

        return "#  c                                          \n"
            +  "#  o  c                                       \n"
            +  "#  n  o                a      P               \n"
            +  "#  n  n     p     r    n      A             f \n"
            +  "#  e  n     o     e    s      L             r \n"
            +  "#  c  e     l     a    w      E             e \n"
            +  "#  t  c     l     d    e      T    T        e \n"
            +  "#  i  t     i     i    r      T    A        z \n"
            +  "#  n  e     n     n    e      E    B        e \n"
            +  "#  g  d     g     g    d      S    S        d \n"
            +  "#\n"
            +  String.format(
                    "#  %s %s     %s    %s   %s   %3s   %3s   %3s\n"
                    /* 1 */, connecting ? w : i
                    /* 2 */, connected ? g : w

                    /* 3 */, polling ? g : i

                    /* 4 */, reading ? g : w
                    /* 5 */, answered ? g : w

                    /* 6 */, have_palettes ? p : w
                    /* 7 */, have_tabs ? t : w

                    /* 8 */, freezed ? f : i
                    )
            +  "\n"
            ;
    }
    //}}}
    // get_send_status {{{
    private String get_send_status()
    {
        String c = (ConnectTask != null) ? "C" : "c";
        String r = (ReadTask    != null) ? "R" : "r";
        String s = (ReadTask    != null) ? "S" : "s";

        c = " ["+c+"onnect# "+ last_connect_count +"]";
        r = " ["+r+"ead# "   + last_read_count    +"]";
        s = " ["+s+"end# "   + last_send_count    +"]";

        return c + s + r;
    }
    //}}}
    // get_connection_state_symbol {{{
    private String get_connection_state_symbol()
    {
        boolean expecting_a_reply =
            (    !last_sent_cmd.equals("")
                 && !last_sent_cmd.equals(CMD_POLL)
                 && !has_last_sent_cmd_ack()
            );

        String symbol = "";

        if( isConnected()) {        symbol += Settings.SYMBOL_LEFT_RIGHT     ;
            if( ReadTask == null  ) symbol += Settings.SYMBOL_no_read_task   ;
            if( expecting_a_reply ) symbol += "["+ last_sent_cmd +" "+ Settings.SYMBOL_expecting_reply +"]";
        }
        else {                      symbol += Settings.SYMBOL_BROKEN_HEART   ;
            if(ConnectTask == null) symbol += Settings.SYMBOL_no_connect_task;
        }

        return symbol;
    }
    //}}}
    // get_freezed_state_symbol {{{
    private String get_freezed_state_symbol()
    {
        return Settings.FREEZED ? Settings.SYMBOL_freezed : " ";
    }
    //}}}
    // get_offline_state_symbol {{{
    private String get_offline_state_symbol()
    {
        return Settings.OFFLINE ? Settings.SYMBOL_offline : " ";
    }
    //}}}

    // dump_STATUS_CLIENT {{{
    public  String dump_STATUS_CLIENT()
    {
        Log_init("");
        try {

            // CONNECTION
            Log_append( comm_status_toString() );

            if(socket != null) Log_append("Signed in on ["+ Settings.ServerID +"]: "+socket.toString() +"\n");
            else               Log_append(Settings.SYMBOL_WARN+" NOT CONNECTED\n");

            // REQUESTS
            Log_append("last_sent_cmd=["+last_sent_cmd+"] "+get_send_status()+"\n");

            // WORKING PROFILE
            Log_append("Working profile=["+ Settings.Working_profile +"]\n");

            // WORKBENCH TOOLS
            int tools_count = 0;
            for(Map.Entry<String, Object> entry : TABS_Map.entrySet()) {
                if(entry.getKey() == Settings.ENTRY_PALETTE) continue;
                NotePane np = (NotePane)entry.getValue();
                if(np.type == NotePane.TYPE_TOOL)
                    ++tools_count;
            }
            Log_append("HookRect=["+ HookRect.left +"@"+ HookRect.top +"] .. tools_count=["+ tools_count +"]\n");

            // RESOURCES
            Log_append(String.format("# %16s = %2d %s", "PALETTES", PALETTES_Map.size(), Settings.SYMBOL_PALETTE));
            Log_append(String.format("# %16s = %2d %s",     "TABS",     TABS_Map.size(), Settings.SYMBOL_TABS   ));
            Log_append("#");
            Log_append(String.format("# %16s = %2d %s",     "CART",     CART_Map.size(), Settings.SYMBOL_CART   ));
            Log_append(String.format("# %16s = %2d %s",     "CTRL",     CTRL_Map.size(), Settings.SYMBOL_CTRL   ));
            Log_append(String.format("# %16s = %2d %s",     "DOCK",     DOCK_Map.size(), Settings.SYMBOL_DOCK   ));
            Log_append(String.format("# %16s = %2d %s",     "HIST",     AUTO_Map.size(), Settings.SYMBOL_PROF   ));
            Log_append(String.format("# %16s = %2d %s",     "PROF",     PROF_Map.size(), Settings.SYMBOL_PROF   ));
            Log_append("#");

        }
        catch(Exception ex) { log_ex(ex, "dump_STATUS_CLIENT"); }

        return Log_toString();
    }
    //}}}
    // dump_DEVICE {{{
    public String dump_DEVICE()
    {
        Log_init("DEVICE ["+ android.os.Build.DEVICE +"]:");

        String s;
        try {
            // Display {{{
            Log_append("DISPLAY:");

            Display display = RTabs.activity.getWindowManager().getDefaultDisplay();
            Point p = new Point();

            s       = Settings.listify( display.toString() );
            Log_append(String.format("%16s :%s"    , "Default"       , s));

            Log_append(String.format("%16s = %d"   , "Id"            , display.getDisplayId()  ));
            Log_append(String.format("%16s = %s"   , "Name"          , display.getName()       ));
            Log_append(String.format("%16s = %d"   , "Orientation"   , display.getRotation()   ));
            display.getSize(p);
            Log_append(String.format("%16s = %dx%d", "Size"          , p.x, p.y                ));
            display.getRealSize(p);
            Log_append(String.format("%16s = %dx%d", "RealSize"      , p.x, p.y                ));
            Log_append(String.format("%16s = %f"   , "RefreshRate"   , display.getRefreshRate()));
            Log_append(String.format("%16s = %d"   , "Rotation"      , display.getRotation()   ));
            //}}}
            // DisplayMetrics {{{
            Log_append("SCREEN:");

            DisplayMetrics displaymetrics = new DisplayMetrics();
            display.getMetrics( displaymetrics );

            s       = Settings.listify( displaymetrics.toString() );
            Log_append(String.format("%16s :%s"    , "DisplayMetrics", s));

            Log_append(String.format("%16s = %dx%d", "pixels"        , displaymetrics.widthPixels, displaymetrics.heightPixels));
            Log_append(String.format("%16s = %.2g" , "density"       , displaymetrics.density));
            Log_append(String.format("%16s = %d"   , "dotsDpi"       , displaymetrics.densityDpi));
            Log_append(String.format("%16s = %.0f" , "scaledDensity" , displaymetrics.scaledDensity));
            Log_append(String.format("%16s = %.0f" , "xdpi"          , displaymetrics.xdpi));
            Log_append(String.format("%16s = %.0f" , "ydpi"          , displaymetrics.ydpi));

            //}}}
        } catch(Exception ex) { log_ex(ex, "dump_DEVICE"); }
        Log_append("o--");

        return Log_toString();
    }
    //}}}
    // dump_PALETTES {{{
    public String dump_PALETTES()
    {
        String s = "PALETTES x"+ PALETTES_Map.size();
        String t = "----------------------------------------";
        t = t.substring(0, s.length());
        Log_init( " o" + t + "o\n"
                + " |" + s + "|\n"
                + " o" + t + "o"
           );

        int count = 0;
        String palette_name;
        for(Map.Entry<String, Object> entry : PALETTES_Map.entrySet())
        {
            palette_name = entry.getKey();
            int[] colors = (int[])PALETTES_Map.get( palette_name );

            Log_append(String.format("%3d %24s (%2d colors)", ++count, palette_name, colors.length));
        }

        Log_append(" o" + t + "o");

        return Log_toString();
    }
    //}}}
    // dump_KEY_VALS {{{
    public String dump_KEY_VALS()
    {
        String one_liner_key_val = Settings.LoadedProfile.get_PROFILE_KV_LINE();
        Settings.set_KEY_VAL(one_liner_key_val, "dump_KEY_VALS: LoadedProfile=["+Settings.LoadedProfile.name+"]");

        CmdParser.parse( one_liner_key_val ); // key1=XXXX key2=XXXX (...)

        String s = "KEY_VALS (made of "+ CmdParser.args.length +" words)";
        String t = "----------------------------------------";
        t = t.substring(0, s.length());
        Log_init( " o" + t + "o\n"
                + " |" + s + "|\n"
                + " o" + t + "o"
                );

        String[] kv;
        String key;
        String val;

        int count = 0;
        for(int i=0; i < CmdParser.args.length; ++i)
        {
            kv  = CmdParser.args[i].split("=");
            if(kv.length != 2) continue;

            key = kv[0];
            val = kv[1];

            // COLLECT POSSIBLY SPACE-SEPARATED VAL WORDS .. (I.E. NOT A KEY=VAL) -----------------------------------
            // ------------------------------------------------------------------------------------------------------
            // i.e. [PROFILE=bookmarks_3 PRODATE=1468926763 Jul 19, 2016 1:12:43 PM (by SGP512) PALETTES=Screen-Tile]
            // ..........................kkkkkkk=vvvvvvvvvv ^^^ ^^^ ^^^^ ^^^^^^^ ^^ ^^^ ^^^^^^^ ........=............
            // ------------------------------------------------------------------------------------------------------
            while((i+1) <  CmdParser.args.length) {
                if(        CmdParser.args[ i+1 ].indexOf("=") > 0) break; // until (next key=val)
                val += " "+CmdParser.args[ ++i ];                         // ...consume trailing word
            }

            Log_append( String.format("%3d %16s = %s", ++count, key, val) );
        }

        Log_append(" o" + t + "o");

        return Log_toString();
    }
    //}}}
    // dump_TABS_LIST {{{
    public String dump_TABS_LIST()
    {
        int size
            = TABS_Map.size()
            - ((TABS_Map.get( Settings.ENTRY_PALETTE ) != null) ? 1 : 0);

        String s = "TABS x"+ size;
        String t = "----------------------------------------";
        t = t.substring(0, s.length());
        Log_init(" o"+ t +"o\n"
                +" |"+ s +"|\n"
                +" o"+ t +"o"
           );

        int count = 0;
        NotePane np;
        for(Map.Entry<String, Object> entry : TABS_Map.entrySet())
        {
            if(entry.getKey() == Settings.ENTRY_PALETTE) continue;
            np = (NotePane)entry.getValue();
            Log_append(String.format("%3d", ++count) + np.printLayout());
        }

        Log_append(" o"+ t +"o");

        return Log_toString();
    }
    //}}}
    //}}}
    /** DASH */
    //{{{
    // has_DashNotePane {{{
    public boolean has_DashNotePane()
    {
        return (DashNotePane != null) && (DashNotePane.textView != null);
    }
    //}}}
    // replace_dash {{{
    private final boolean DashNotePane_freezed         = false;
    private final boolean DashNotePane_in_replace_mode = false;
    public void replace_dash(String text)
    {
        //DashNotePane_in_replace_mode = (!text.equals(""));

        //if( DashNotePane_in_replace_mode ) {
        if((DashNotePane != null) && (DashNotePane.textView != null))
            DashNotePane.textView.setText( text );
        //}
    }
    //}}}
    // error_to_dash {{{
    public void error_to_dash(String msg)
    {
        update_dash("***", msg);
    }
    //}}}
    // warn_to_dash {{{
    public void warn_to_dash(String key, String msg)
    {
        if(msg == "")   update_dash();
        else {
            if( key.equals("STAGE") ) {
                String s = "STAGE "+ msg + " -------------------------------------------------------------------------------";
                update_dash("---", String.format("%.24s-:", s));
            }
            else
                update_dash("---", String.format( "%24s : %s", key, msg));
        }
    }
    //}}}
    // update_dash {{{
    public void update_dash() {
        update_dash("","");
    }

    private final StringBuffer Dash_StringBuffer = new StringBuffer();

    boolean hold_DashMsg_cmd = false;
    @SuppressWarnings("ConstantConditions")
    private void update_dash(String key, String text)
    {
        if(D) if(text!="") log(text);

        // KEY [***]=[text] {{{
        if(key.equals("***") || key.equals("---"))
        {
            if(text != "")
                DashMsg_cmd = text +"\n"+ DashMsg_cmd;
        }
        ///}}}
        /*
        // [KEY]=[REPLACEMENT] {{{
        else if( !key.equals("") )
        {
        String  pattern         = key+".*";
        String  replacement = (key +": "+ text).replace("\n"," ").trim();
        Pattern p       = Pattern.compile(pattern);
        Matcher m       = p.matcher( Dash_StringBuffer.toString() );

        Dash_StringBuffer.setLength(0);
        if(m.find()) Dash_StringBuffer.append(m.replaceFirst(replacement));
        else         Dash_StringBuffer.append(replacement + "\n");

        }
        //}}}
        // NO [KEY]=[REPLACEMENT] .. START FROM SCRATCH {{{
        else {
        Dash_StringBuffer.setLength(0);
        }
        //}}}
         */
        Dash_StringBuffer.setLength(0);

        // DASH {{{
        if(DashNotePane == null) {
            /*
               if(!DashMsg_cmd.equals("")) {
                   if(D) log("DASH: "+ DashMsg_cmd);
                   DashMsg_cmd = "";
               }
             */
            return;
        }
        //}}}

        // HOLD CURRENT MESSAGE {{{
        if((dash_touched_down_time > 0) && ((System.currentTimeMillis() - dash_touched_down_time    ) > 500))
        {
            // SHOW AS HELD
            if(!hold_DashMsg_cmd ) {
                hold_DashMsg_cmd = true;
                DashNotePane.textView.setBackgroundColor( DashLockColor );
                DashNotePane.textView.setText( DashNotePane.textView.getText().toString().replace("\n", "\n"+Settings.SYMBOL_ROUND_PUSHPIN+" ") );
            }
            // ...AND HOLD
            return;
        }
        else
        {
            // SHOW AS RELEASED
            if( hold_DashMsg_cmd ) {
                hold_DashMsg_cmd = false;
                DashNotePane.textView.setBackgroundColor( DashBackColor );
            }
            // THEN UPDATE...
        }
        //}}}

        // CONSUME DASHMSG_CMD {{{
        boolean needs_fitness = false;
        if(!hold_DashMsg_cmd && (DashMsg_cmd != ""))
        {
            if     ( DashMsg_cmd.endsWith("...................."))  { DashMsg_cmd = "";                needs_fitness = true; }
            else if(!DashMsg_cmd.endsWith(                   "."))  { DashMsg_cmd = DashMsg_cmd+"\n."; }
            else                                                    { DashMsg_cmd = DashMsg_cmd+  "."; }
        }
        // }}}



        if( DashNotePane          == null) return;
        if( DashNotePane.textView == null) return;
        if( DashNotePane_in_replace_mode ) return;
        if( DashNotePane_freezed         ) return;

        Dash_StringBuffer.append( DashMsg_cmd );

        if(Settings.LOGGING)
            Dash_StringBuffer.append(Settings.SYMBOL_CHECK_MARK + " LOGGING "+ Settings.SYMBOL_CHECK_MARK +"\n");

        // Settings KEY VAL {{{
        Dash_StringBuffer.append(mRTabs.get_current_stage()).append("\n");

        Dash_StringBuffer.append("CNX " ).append(get_connection_state_symbol()).append("\n");
        Dash_StringBuffer.append("POLL ").append(DashMsgPoll                  ).append("\n");

        Dash_StringBuffer.append( String.format("- %16s = %s\n"                 , "PROFILE"   , Settings.Working_profile));
        Dash_StringBuffer.append( String.format("- %16s = x%d (#%d = %s)\n"     , "PALETTES"  , PALETTES_Map.size()  , get_PALETTE_NUM(Settings.PALETTE), Settings.PALETTE));
        Dash_StringBuffer.append( String.format("- %16s = x%d\n"                , "TABS  "    , TABS_Map.size()));
        Dash_StringBuffer.append( String.format("- %16s = %.2f (%.2f < %.2f)\n" , "DEV_SCALE" , Settings.DEV_SCALE   , Settings.DEV_SCALE_MIN, Settings.DEV_SCALE_MAX));
        Dash_StringBuffer.append( String.format("- %16s = %s%d\n"               , "OPACITY"   , Settings.SYMBOL_ALPHA, Settings.OPACITY));
        Dash_StringBuffer.append( String.format("- %16s = %.2f (TXT:%.2f)\n"    , "ZOOM"      , Settings.DEV_ZOOM    , Settings.TXT_ZOOM));

        Dash_StringBuffer.append( String.format( "BOOKMARK: %s\n %s\n"           , Settings.BOOKMARK_TITLE , Settings.BOOKMARK_URL ));
        Dash_StringBuffer.append( String.format("REQUESTED: %s\n %s\n"           , Settings.REQUESTED_TITLE, Settings.REQUESTED_URL));
        Dash_StringBuffer.append( String.format("CLIPBOARD: %s\n [%s]\n"         , Clipboard.getType()     , Clipboard.getText()   ));

    //  Dash_StringBuffer.append( String.format("- %16s = %d@%d %dx%d\n"        , "CENTER_WH" , Settings.CENTER_X, Settings.CENTER_Y, Settings.DEV_W, Settings.DEV_H));
    //  Dash_StringBuffer.append( String.format("- %16s = %d\n"                 , "TAB_GRID_S", Settings.TAB_GRID_S   ));
    //  Dash_StringBuffer.append( String.format("- %16s = %d\n"                 , "DEV_DPI"   , Settings.DEV_DPI      ));
    //  Dash_StringBuffer.append( String.format("- %16s = %d\n"                 , "MON_DPI"   , Settings.MON_DPI      ));
    //  Dash_StringBuffer.append( String.format("- %16s = %.2f\n"               , "MON_SCALE" , Settings.MON_SCALE    ));


        //}}}
        DashNotePane.textView.setText( Dash_StringBuffer.toString() );

        if(hold_DashMsg_cmd && needs_fitness)
            NpButton.FitText(DashNotePane.textView, 0.6F);
    }
    //}}}
    // isADash {{{
    private static boolean isADash(NotePane np)
    {
        return
            (np.tag.startsWith("DASH"))
            ;
    }
    //}}}
    // WORKBENCH ACTIVITY AND TOOL TYPES {{{
    private static boolean isShowingEmptyWorkbench() { return Settings.Working_profile.equals("WORKBENCH/WORKBENCH"); }

    private boolean is_in_WORKBENCH()
    {
        // WETHER LAST PARSE_TABS ON TABS_MAP CONTAINS A HOOK TO HANG PROFILES TO CUSTOMIZE ON
        return (HookRect.left != 0) || (HookRect.top != 0);
    }
    //}}}
    //}}}
    /** TOOL - WORKBENCH */
    // {{{
    // select_tool {{{
    private void select_tool(String cmdLine)
    {
        CmdParser.parse( cmdLine );
        Settings.WORKBENCH_TOOL = Integer.parseInt( CmdParser.arg1.trim() );

//TOOL//Settings.MON(TAG_TOOL, "select_tool("+ Settings.WORKBENCH_TOOL +")\n");

        apply_TABS_LAYOUT(TABS_Map, tabs_container, "select_tool");

        mRTabs.sync_np("select_tool");
    }
    //}}}
    // }}}
    /** TOOL - URL */
    // {{{
    // WebGroup interface {{{
    private static final int WEBGROUP_BOTH_FOUND = WebGroup.BOTH_FOUND;
    private static final int WEBGROUP_FULL_FOUND = WebGroup.FULL_FOUND;
    private static final int WEBGROUP_URL1_FOUND = WebGroup.URL1_FOUND;
    private static final int WEBGROUP_URL2_FOUND = WebGroup.URL2_FOUND;
    private static final int WEBGROUP_NONE_FOUND = WebGroup.NONE_FOUND;

    private              int  add_profile_url_pair     (String profile_key, String url1, String url2, String caller) { if( !Settings.GetLoadedProfileWebGroupsChecked() ) check_profile_webGroups(); return WebGroup.add_profile_url_pair     ( profile_key, url1, url2, caller); }
    private              int  del_profile_url          (String profile_key, String url, String caller              ) { if( !Settings.GetLoadedProfileWebGroupsChecked() ) check_profile_webGroups(); return WebGroup.del_profile_url          ( profile_key, url       , caller); }
    private              int  get_group_size           (String profile_key, String url                             ) { if( !Settings.GetLoadedProfileWebGroupsChecked() ) check_profile_webGroups(); return WebGroup.get_group_size           ( profile_key, url               ); }
    private           String  get_profile_key          (String profile_key, String url, String caller              ) { if( !Settings.GetLoadedProfileWebGroupsChecked() ) check_profile_webGroups(); return WebGroup.get_profile_key          ( profile_key, url       , caller); }
    private ArrayList<String> get_profile_url_ArrayList(String profile_key                                         ) { if( !Settings.GetLoadedProfileWebGroupsChecked() ) check_profile_webGroups(); return WebGroup.get_profile_url_ArrayList( profile_key                    ); }
    private ArrayList<String> get_profile_url_ArrayList(String profile_key, String url                             ) { if( !Settings.GetLoadedProfileWebGroupsChecked() ) check_profile_webGroups(); return WebGroup.get_profile_url_ArrayList( profile_key, url               ); }

    // check_profile_webGroups {{{
    private void check_profile_webGroups()
    {
        // get the list of all WEBGROUP URLs for Working_profile {{{
        String caller      = "check_profile_webGroups";
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "Settings.Working_profile=["+Settings.Working_profile+"]");

        String profile_key = Settings.Working_profile;
        ArrayList<String> all = WebGroup.get_profile_url_ArrayList( profile_key );
        if(all == null) {
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "...PROFILE ["+profile_key+"] (HAS NO WEBGROUP URLs)");
            return;
        }
        //}}}
        // SYNC WEBGROUP WITH ACTUAL PROFILE CONTENTS {{{
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "...PROFILE ["+profile_key+"] (HAS "+all.size()+" WEBGROUP URLs)");
        for(int i= 0; i < all.size(); ++i)
        {
            // look for a tab with that url in the current working profile
            String          url = all.get(i);
            NotePane np_for_url = get_np_with_url(url, caller);
            if(np_for_url == null)
            {
                // remove this url from its group if there is no tab for it (anymore) in this profile
                int group_size = WebGroup.del_profile_url(profile_key, url, caller);
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "...removing UNTAGGED URL=["+url+"]");
            }
        }
        //}}}
        Settings.SetLoadedProfileWebGroupsChecked( true );
    }
    //}}}
    //}}}
    // TOOL_URL_np {{{
//  private static       NotePane               TOOL_URL_np  = null;   // WEB PROPERTIES
//  public NotePane get_TOOL_URL_np() { return  TOOL_URL_np; }
    //}}}
    // select_TOOL_URL_action {{{
    private static final String[] WEBGROUP_SHELL_LAYOUT_SFX = { "SHELL_L", "SHELL_C", "SHELL_R" }; // i.e. left, center, right
    private boolean select_TOOL_URL_action(NotePane np)
    {
        String caller = "select_TOOL_URL_action(tag=["+np.tag+"])";
//*TOOL*/Settings.MOC(TAG_TOOL, caller);
        // CURRENT URL ACTION ..(SENDINPUT .. WEBVIEW.. BROWSER) {{{
        boolean do_browser = Settings.TOOL_URL_ACTION.equals(Settings.TOOL_URL_ACTION_BROWSER);
        boolean do_sendkey = Settings.TOOL_URL_ACTION.equals(Settings.TOOL_URL_ACTION_SENDKEY);
        boolean do_webview = Settings.TOOL_URL_ACTION.equals(Settings.TOOL_URL_ACTION_WEBVIEW);
//*TOOL*/Settings.MOM(TAG_TOOL, "...do_browser=["+ do_browser +"]");
//*TOOL*/Settings.MOM(TAG_TOOL, "...do_sendkey=["+ do_sendkey +"]");
//*TOOL*/Settings.MOM(TAG_TOOL, "...do_webview=["+ do_webview +"]");
        if(!do_browser && !do_sendkey) {
            do_webview = true;
//*TOOL*/Settings.MOM(TAG_TOOL, "...do_webview=["+ do_webview +"] ... SET AS A FALLBACK DEFAULT");
        }

        // }}}
/* //XXX TRYING!
        // YIELD ON [SENDINPUT] .. f(!WEBVIEW and !BOOKMARK) {{{
        if(         do_sendkey
                && !np.tag.equals("WEBVIEW" )
                && !np.tag.equals("BOOKMARK")
          )
            return false;

        //}}}
*/
        // YIELD ON BUILT-IN COMMANDS .. (sendTabCommand will do the job) {{{
        if( np.tag.startsWith("TOOL_URL" ) )
            return false;

        ///}}}
        // PICK URL FROM [WEBVIEW_URL] or [BOOKMARK_URL] or [TAG URL] {{{
        String  np_cmd = get_np_cmd(np);
        String     url = get_np_url(np, caller);

        // LOCALLY EXPAND BUILTIN DIRS
        if( do_webview ) {
            url = Settings.Expand_dirs( url );
//*TOOL*/Settings.MOM(TAG_TOOL, "...Expand_dirs: ["+url+"]");
        }

        if( !TextUtils.isEmpty(url) )
        {
//*TOOL*/Settings.MON(TAG_TOOL, caller, "using  get_np_url=["+ url +"]");
        }
        else {
            switch( np_cmd )
            {
                case "WEBVIEW" : url = Settings.REQUESTED_URL ;
//*TOOL*/Settings.MON(TAG_TOOL, caller, "using  WEBVIEW_URL=["+ url +"]");
                                 break;
                case "BOOKMARK": url = Settings.BOOKMARK_URL;
//*TOOL*/Settings.MON(TAG_TOOL, caller, "using BOOKMARK_URL=["+ url +"]");
                                 break;
                default:         url = get_np_url(np, caller);
//*TOOL*/Settings.MON(TAG_TOOL, caller, "using  get_np_url=["+ url +"]");
                                 break;
            }
        }

        //}}}
        // 1. AN URL TO BROWSE or SEND {{{
        if( !url.equals("") )
        {
//*TOOL*/    if(np_cmd.equals("WEBVIEW" )) Settings.MON(TAG_TOOL, caller, "[ WEBVIEW_URL]=["+url+"]");
//*TOOL*/    if(np_cmd.equals("BOOKMARK")) Settings.MON(TAG_TOOL, caller, "[BOOKMARK_URL]=["+url+"]");
//*TOOL*/    else                          Settings.MON(TAG_TOOL, caller, "[     TAB_URL]=["+url+"]");

            if(do_webview)
            {
//*TOOL*/Settings.MON(TAG_TOOL, caller, "[WEBVIEW TAB URL]");
                // EXPAND data scheme content built-in keywords {{{
                if( url.startsWith("data:text/html;") ) {
                    String np_label = np.getLabel();
                    if( np_label.equalsIgnoreCase("cookies") )
                    {
                        url = ""
                            + "data:text/html;charset=utf-8,<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01//EN' 'http://www.w3.org/TR/html4/strict.dtd'>\n"
                            + "<html>\n"
                            + " <head>\n"
                            + "  <title>"+np_label+"</title>\n"
                            + "  <meta http-equiv='content-type' content='text/html; charset=ISO-8859-1'>\n"
                            + "   <style>\n"
                            + "    td { border:1px  inset; }\n"
                            + "    td { padding:1em; }\n"
                            + "    td { vertical-align:top; }\n"
                            + "    th { border:2px outset; }\n"
                            + "    th { vertical-align:top; }\n"
                            + "   </style>\n"
                            + " </head>\n"
                            + " <body>\n"
                            + "  <div\n"
                            + "   style='\n"
                            + "         color:navy;\n"
                            + "    text-align:left;\n"
                            + "         width:95%;\n"
                            + "        border:2px ridge;\n"
                            + "        margin:2px;\n"
                            + "     font-size:12px;\n"
                            + "    background-color:orange;\n"
                            + "    text-align:center;\n"
                            + "  '>\n"
                            +      Settings.get_cookies_html_table( Settings.LOCALHOST_URL )
                            + "  </div>\n"
                            + " </body>\n"
                            + "</html>\n"
                            ;
                    }
                }
                //}}}
                mRTabs.magnify_np_url(np, url);
                return true; // handled
            }
            if((do_browser) || (do_sendkey))
            {
                CmdParser.parse( np.tag );
                String cmd = CmdParser.cmd;
//*TOOL*/Settings.MOM(TAG_TOOL, "...cmd=["+cmd+"]");

                // SINGLE URL {{{
                String profile_key  = get_profile_key(Settings.Working_profile, url, caller);
                if(profile_key == null)
                {
                    // SENDKEYS ENCODED URL {{{
                    if(do_sendkey) {
                        url = Settings.Get_SENDKEYS_ENCODED( url );

//*TOOL*/Settings.MON(TAG_TOOL, caller, "SENDKEYS ENCODED url=["+url+"]");
                    }
                    // }}}
                    // URL TO DEFAULT BROWSER SHELL COMMAND
                    if(do_browser)
                    {
                        // (replace) [SENDINPUT] [SENDKEYS]
                        if( !TextUtils.equals(cmd, Settings.CMD_BROWSE) )
                            cmd = Settings.CMD_SHELL;

//*TOOL*/Settings.MON(TAG_TOOL, caller, "DEFAULT BROWSER SHELL COMMAND: ...cmd=["+cmd+"]");
                    }
                    // SEND URL KEYBOARD INPUT
                    else {
                        // SELECTING SENDKEYS COMMAND .. (one of) [RUN] [BROWSE] [SHELL] [SENDKEYS] [SENDINPUT]
                        if(        !cmd.startsWith("BROWSE")
                                && !cmd.startsWith(   "RUN")
                                && !cmd.startsWith( "SHELL")
                                && !cmd.startsWith(  "SEND")
                          )
                            cmd = do_browser ? "SHELL " : "SENDINPUT ";

//*TOOL*/Settings.MON(TAG_TOOL, caller, "SELECTING SENDKEYS COMMAND: ...cmd=["+cmd+"]");
                    }

                    // SEND PREFIX CMD SUFFIX
                    String pfx="";
                    String sfx="";
                    if( cmd.startsWith("SEND" ) ) {
                        // ............................................. // tag=SENDINPUT ^Lhttp://remotetabs.com/#top{ENTER}^R
                        pfx = do_browser ? "" : "^L"                  ; // browser address bar focus
                        sfx = do_browser ? "" : "{ENTER}{SLEEP 500}^R"; // browser wakeup
                    }
                    String cmdLine = cmd +" "+ pfx+url+sfx;

                    String msg =  "calling sendCmd("+cmdLine+")";
//*TOOL*/Settings.MON(TAG_TOOL, caller, msg);
                    sendCmd(cmdLine, caller);
                    mRTabs.toast_long( msg );
                }
                //}}}
                // WEBGROUP {{{
                else {
                    ArrayList<String> al = get_profile_url_ArrayList(profile_key, url);
//*TOOL*/Settings.MON(TAG_TOOL, caller, "@@@ LOADING WEBGROUP ["+profile_key+"] (x"+al.size()+")");

                    // LAYOUT MULTIPLE URLS
                    for(int i= 0; i < al.size(); ++i) {
                        url = al.get(i);
                        String cmdLine
                            = WEBGROUP_SHELL_LAYOUT_SFX[i % WEBGROUP_SHELL_LAYOUT_SFX.length]
                            +" "+ url;
//*TOOL*/Settings.MON(TAG_TOOL, caller, "calling sendCmd("+cmdLine+")");
                        sendCmd(cmdLine, caller);
                    }
                }
                // }}}

                return true; // handled
            }
        }
        //}}}
        // 2. [NP UNICODE] ++ [TOOL_URL_NP COMMAND] {{{
        else if(    np.tag.startsWith ("SENDINPUT")
                &&  np.text.startsWith("0x")        // ...one of dynamically text=0x... SYMBOL TABS
                && (np.text.length() >= 6)
          ) {
            // .........np has: TAG=[SENDINPUT UNICODE 0xNNNNNN]
            // TOOL_URL_np has: TAG=[TOOL_URL http://unicode.org/cldr/utility/character.jsp?a=0075]
            NotePane TOOL_URL_np = get_np_with_tag("TOOL_URL", TABS_Map);
            if(TOOL_URL_np != null)
            {
                // HTTP REQUEST codePoint param: ...^XXXX
                String cmdLine = TOOL_URL_np.tag;
                if(D) log("cmdLine=["+ cmdLine +"]");

                // REQUEST PARAM UNICODE {{{
                int idx = cmdLine.lastIndexOf("=");
                if(idx > 0) {
                    String request   = cmdLine.substring(0, idx).replace("TOOL_URL","SHELL");
                    String codePoint = np.text.substring(2);    // 0x(...)
                    cmdLine          = String.format("%s=%s", request, codePoint);
                }
                if(D) log("cmdLine=["+ cmdLine +"]");

                // }}}
                // BROWSE ON COMPUTER {{{
                if(do_browser) {
                    sendCmd(cmdLine, caller);
                    return true;
                }

                // }}}
                // BROWSE ON DEVICE {{{
                if(do_webview) {
                    //............................  0123456
                    //............................ "SHELL http://"
                    url = cmdLine.substring(6);
                    if(D) log("url=["+ url +"]");

                    mRTabs.magnify_np_url(np, url);
                    return true; // consumed
                }
                // }}}
            }
        }
        //}}}
        // 3. NOT CONSUMED {{{
        return false;
        // }}}
    }
    //}}}
/*
    // check_TOOL_URL_np {{{
    private void check_TOOL_URL_np(NotePane np)
    {
        // CHECK URL ACTION TOOL
        if(        (TOOL_URL_np == null)
                && (((String)np.text).startsWith("TOOL_URL"))
        ) {
            TOOL_URL_np  = np;
            np.setText(Settings.SYMBOL_SENDKEY);
        }
        if(TOOL_URL_np == null) return;

        // CURRENT CONSTRAINTS ARE:
        // .. text startsWith("TOOL_URL")
        // .. should have some working tag cmdLine for the server (SHELL or SENDINPUT)

        // NEEDS A COLOR TO RESTORE
        if(TOOL_URL_np.color < 1) TOOL_URL_np.color = 1;
    }
    //}}}
*/
    // }}}
    // get_np_url {{{
    private String get_np_url(NotePane np, String caller)
    {
        caller += "] [get_np_url("+np+")";
//*URL*/Settings.MOC(TAG_URL, caller);

        String url = get_tag_url( np.tag );
        if( url.startsWith( "data:" ) )
        {
//*URL*/Settings.MOM(TAG_URL, "url=["+url+"]");

            // TITLE = LABEL
            String     title = np.getLabel();

            // BODY = INFO
            String  textInfo = np.getTextInfo().trim();
            if( TextUtils.isEmpty(textInfo) ) textInfo = title;

            // HTML = HEADER + BODY
            String      html = textInfo.startsWith("<!DOCTYPE")
                ?              textInfo
                :              Settings.Get_data_scheme_html(title, textInfo);
            try {
                url = url
                    + ","
                    + URLEncoder.encode(html, "UTF-8");
            }
            catch(Exception ex) { Settings.log_ex(ex, "URLEncoder.encode("+url+")"); }
            url = url.replace("+","%20");
//*URL*/try { String s = URLDecoder.decode(url, "UTF-8"); Settings.MOM(TAG_URL, "...URLDecoder.decode returned:\n"+s+"\n"); } catch(Exception ex) { Settings.log_ex(ex, "URLEncoder.encode("+url+")"); }//TAG_URL
        }
//*URL*/Settings.MOM(TAG_URL, "...return url=["+url+"]");
        return url;
    }
    //}}}
    // get_tag_url {{{
    public  String get_tag_url(String tag)
    {
        // SHELL & SENDINPUT URL ARGUMENT // {{{
        String url = "";
        tag = Settings.Del_color_hex_from_text( tag );
        CmdParser.parse( tag );
        if(        CmdParser.cmd.equals("SHELL"    )
                || CmdParser.cmd.equals("SENDINPUT")
                || CmdParser.cmd.equals("SENDKEYS" )
                || CmdParser.cmd.equals("WEBVIEW"  )
          ) {
            url = CmdParser.argLine;
//*URL*/Settings.MOM(TAG_URL, "...argLine URL=["+url+"]");
//...............................argLine URL=["https://thepiratebay.cr/search/The king's speech/0/7/0"{ENTER}]
          }
        // }}}
        // BARE URL // {{{
        else {
            url = tag;
//*URL*/Settings.MOM(TAG_URL, "...cmdLine URL=["+url+"]");
        }
        // }}}
        // NORMALIZED URL {{{
        if( !url.startsWith( "data:") ) {
            url = Settings.Get_normalized_url( url );
        }
        //}}}
//*URL*/Settings.MON(TAG_URL, "get_tag_url("+tag+")", "...return url=["+url+"]");
        return url;
    }
    //}}}
    /** TOOL - UNICODE BLOCKS */
    // {{{
    // select_BLOCK_np {{{
    private boolean select_BLOCK_np(NotePane np, boolean store_preset)
    {
//*TOOL*/String caller ="select_BLOCK_np";//TAG_TOOL
//*TOOL*/Settings.MON(TAG_TOOL, caller, "("+np+", store_preset="+ store_preset +")");
        // BLOCK SeekBar has its own events listener {{{
        if(np == BLOCK_SB_np) {
//*TOOL*/Settings.MON(TAG_TOOL, caller, "(np == BLOCK_SB_np) ...return true");
            return true;
        }

        // }}}
        // BLOCK_PS .. STORE/USE PRESET-SHEET {{{
        if( np.tag.startsWith(BLOCK_PS) )
        {
            // STORE PRESET
            if( store_preset )
            {
                int    cell = get_sheet_PG_R0_offset( 0 ) << 4;
                String text = String.format("0x%04X", cell);

            //  if( np.text.startsWith(text) ) np.setText( "-"  );
            //  else                           np.setText( text );

                int   preset_num = 0;
                try { preset_num = Integer.valueOf( np.tag.substring(8) ); } catch(Exception ignored) {}
                if(preset_num > 0) {
                    store_UNICODE_PRESET(preset_num, text);
                    load_UNICODE_PRESET();
                }
//*TOOL*/Settings.MON(TAG_TOOL, caller, "(BLOCK_PS .. STORE/USE PRESET-SHEET) ...return true");
                return true; // consumed
            }
            // USE PRESET
            if(np.text.startsWith("0x") && (np.text.length() >= 6))
            {
                int sheet = 0;
                try {
                    sheet = Integer.valueOf(np.text.substring(2), 16) >> 4;  // block of 128
                } catch(Exception ignored) {}
if(D) log("USE PRESET=["+ np.text +"] sheet=["+ sheet +"]");

                select_UNICODE_SHEET( sheet );
//*TOOL*/Settings.MON(TAG_TOOL, caller, "(USE PRESET) ...return true");
                return true; // consumed
            }
        }
        //}}}
        // BLOCKPAGE .. 1K-BLOCK {{{
        int k_block = -1;
        if(np.tag.startsWith("BLOCKPAGE"))
        {
            // ............................ 0123456789 .. 0123456789
            // ............................ BLOCKPAGE1 .. BLOCKPAGE100
            // ......................................^.............^.
            k_block = Integer.valueOf( np.tag.substring(9), 16);

            if(k_block > 0x1000) k_block /= 0x1000;
        }
        // }}}
        // update BLOCK_PG_np {{{
        if(k_block >= 0)
        {
            if(BLOCK_PG_np != null)
                BLOCK_PG_np.setText( String.format("%X", k_block) );

            // START OF SELECTED BLOCK .. DROP CURRENT FIRST ROW OFFSET
            if(BLOCK_R0_np != null) BLOCK_R0_np.text = "";
        }

        // }}}
        // BLOCK_[UD][23] .. OFFSET FROM CURRENT BLOCK {{{
        int offset =  0;
        if(k_block < 0)
        {
            //......................................[321]
            if     (np == BLOCK_U3_np) { offset =  0x100; }
            else if(np == BLOCK_D3_np) { offset = -0x100; }
            else if(np == BLOCK_U2_np) { offset =   0x08; }
            else if(np == BLOCK_D2_np) { offset =  -0x08; }

        }
        // }}}
        // ...WAS NOT A BLOCK NOTEPANE TOOL {{{
        if(D) log("...k_block=["+ k_block +"] offset=["+ offset +"]");
        if((k_block < 0) && (offset == 0)) {
//*TOOL*/Settings.MON(TAG_TOOL, caller, "((k_block < 0) && (offset == 0)) ...return false");
            return false;   // not consumed
        }
        // }}}
        // BLOCK_PG + BLOCK_R0 .. [SHEET 0-7] / [SHEET 8-F] {{{
        int sheet = get_sheet_PG_R0_offset( offset );
        // }}}
        // ..slip into the next defined BLOCKPAGE (one with a NotePane's tag defined) {{{
        if((offset != 0) && (sheet != 0))
        {
            int   sheet_page  = sheet >> 8;
            int defined_page  = get_next_defined_BLOCKPAGE(sheet_page, (offset > 0)); // codePoint, [forward or backwoard]
            if( defined_page != sheet_page)
            {
if(D) log("select_BLOCK_np("+ np.tag +"): sheet_page=["+ String.format("%X", sheet_page) +"] using defined_page=["+ String.format("%X", defined_page) +"]");
                sheet = defined_page << 8; // use first sheet
            }
        }
        // }}}
        // SELECT REQUESTED BLOCK {{{
    //  if(sheet != 0) {
            select_UNICODE_SHEET( sheet );
//*TOOL*/Settings.MON(TAG_TOOL, caller, "(sheet != 0) ...return true");
            return true; // consumed
    //  }

        // }}}
///*TOOL*/Settings.MON(TAG_TOOL, caller, "...return false");
//        return false; // not consumed
    }
    //}}}
    // select_UNICODE_SHEET {{{
    public void select_UNICODE_SHEET(int sheet)
    {
        // [!working_profile_contains_a_BLOCK_TOOL] .. (return) {{{
        String caller = "select_UNICODE_SHEET("+ String.format("%X", sheet) +")";
//*TOOL*/Settings.MOC(TAG_TOOL, caller);

        if( !working_profile_contains_a_BLOCK_TOOL() )
        {
            String msg = Settings.Working_profile+"\nis missing some required\nUNICODE BLOCK TOOLS";
//*TOOL*/Settings.MOM(TAG_TOOL, msg);

            mRTabs.toast_long( msg );
            return;
        }

        //}}}
        // UNICODE PRESETS (profile init time evaluation) {{{
        if(blockPages == null)
        {
            load_UNICODE_PRESET();
            //TABS_Map_Has_Changed = true;
        }

        //}}}
        // FOLLOW SESSION SHEET .. ->[BLOCK_PG_np]  {{{
        Settings.UNICODE_SHEET  = String.format("%X", sheet);

        // SEEKBAR SHEET
        if(BLOCK_SB    != null) BLOCK_SB.setProgress((sheet & 0xFF) / 0x8);

        // BLOCK PAGE LABEL
    //  int k_block       = (sheet << 4);
        int k_block       = (sheet >> 8);
        if(BLOCK_PG_np != null) BLOCK_PG_np.setText( String.format("%X", k_block) );

        // }}}
        // BLOCK ROWS LABEL {{{
        BLOCK_R0_np.setText( String.format("%02X", (sheet & 0xFF)  ) );                     // bytes 3 2
        BLOCK_R1_np.setText( String.format("%02X", (sheet & 0xFF)+1) );                     // bytes 3 2
        BLOCK_R2_np.setText( String.format("%02X", (sheet & 0xFF)+2) );                     // bytes 3 2
        BLOCK_R3_np.setText( String.format("%02X", (sheet & 0xFF)+3) );                     // bytes 3 2
        BLOCK_R4_np.setText( String.format("%02X", (sheet & 0xFF)+4) );                     // bytes 3 2
        BLOCK_R5_np.setText( String.format("%02X", (sheet & 0xFF)+5) );                     // bytes 3 2
        BLOCK_R6_np.setText( String.format("%02X", (sheet & 0xFF)+6) );                     // bytes 3 2
        BLOCK_R7_np.setText( String.format("%02X", (sheet & 0xFF)+7) );                     // bytes 3 2

        // }}}
        // BLOCK ROWS COLORS {{{
        int[] page_07_color_idx = { 9, 0, 1, 2, 3, 4, 5, 6 };
        int[] page_8F_color_idx = { 7, 8, 9, 0, 1, 2, 3, 4 };
        int[] color_idx
            = ((sheet & 0x8) == 0)
            ? page_07_color_idx
            : page_8F_color_idx;

        use_palette_colors( COLOR_TABS );

        if( BLOCK_R0_np.bg_color == NotePane.NO_COLOR)
            BLOCK_R0_np.button.setBackgroundColor( get_tabs_bg_color( color_idx[0]) );  // 10 : 8
        BLOCK_R1_np.button.setBackgroundColor    ( get_tabs_bg_color( color_idx[1]) );  //  1 : 9
        BLOCK_R2_np.button.setBackgroundColor    ( get_tabs_bg_color( color_idx[2]) );  //  2 : 0
        BLOCK_R3_np.button.setBackgroundColor    ( get_tabs_bg_color( color_idx[3]) );  //  3 : 1
        BLOCK_R4_np.button.setBackgroundColor    ( get_tabs_bg_color( color_idx[4]) );  //  4 : 2
        BLOCK_R5_np.button.setBackgroundColor    ( get_tabs_bg_color( color_idx[5]) );  //  5 : 3
        BLOCK_R6_np.button.setBackgroundColor    ( get_tabs_bg_color( color_idx[6]) );  //  6 : 4
        BLOCK_R7_np.button.setBackgroundColor    ( get_tabs_bg_color( color_idx[7]) );  //  7 : 5

        // }}}
        // CELL VALUES [ROW & COL] {{{
        int sheet_codePoint = sheet << 4;

        String block_name = String.format("BLOCKPAGE%03X", sheet_codePoint >> 12);
//*TOOL*/Settings.MOM(TAG_TOOL, "...block_name=["+block_name+"]");

        for(Map.Entry<String, Object> entry : TABS_Map.entrySet())
        {
            // SYNC UNICODE TABLE {{{
            if(entry.getKey() == Settings.ENTRY_PALETTE) continue;

            NotePane np = (NotePane)entry.getValue();

            if(         np.tag.startsWith(BLOCK_TOOL) // skip TOOLS
                    ||  np.tag.startsWith(BLOCK_PS  ) // PRESET SHEET TOOLS
                //  || (np == TOOL_URL_np)
              )
            {
                continue;
            }
            //}}}
            // UPDATE text=0x... SYMBOL TABS {{{
            String text = np.text;
//*TOOL*/Settings.MOM(TAG_TOOL, "text=["+ text +"]");
            if(text.startsWith("0x") && text.length() >= 6)
            {
                int cell = 0;
                try {                                                                   // 012345
                    cell = 0x7F & Integer.valueOf(text.substring(text.length()-2), 16); // 0x0000
                } catch(Exception ignored) {}

                String s = String.format("0x%04X", sheet_codePoint + cell);
//*TOOL*/Settings.MOM(TAG_TOOL, "text=["+ text +"] s=["+ s +"]");
                np.setText( s );
                //np.button.fitText();
                continue;
            }
            // }}}
            // HIGHLIGHT SELECT BLOCK TAB {{{
            if(np.tag.startsWith( "BLOCKPAGE" ))
            {
                if(np.tag.equals( block_name ))
                {
                    //np.setText( "XXX" );
                    np.button.setBackgroundColor( Color.BLACK );
                    np.button.setTextColor      ( Color.WHITE );
                }
                else {
                    int bg_color;
                    if(np.color != 0)             bg_color = get_tabs_bg_color( np.color-1 );
                    else                          bg_color = DashBackColor;
                    np.button.setBackgroundColor( bg_color );
                    np.button.setTextColor( getTextColor(COLOR_TABS, bg_color) );
                }
            }
            // }}}
        }
        // }}}

        //if(TABS_Map_Has_Changed)
        //TABS_Map_Has_Changed = true;
        //apply_TABS_LAYOUT(TABS_Map, tabs_container, "select_UNICODE_SHEET("+ sheet_codePoint +")");

/*
// no font for run
try {
} catch (Exception ex) {
    if(BLOCK_PG_np != null) BLOCK_PG_np.setText( ex.getMessage() );
}
*/

    }
    //}}}
    // BLOCK_SB_OnSeekBarChangeListener {{{
    private final OnSeekBarChangeListener BLOCK_SB_OnSeekBarChangeListener = new OnSeekBarChangeListener()
    {
	@Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
//TOOL//Settings.MON(TAG_TOOL, "onProgressChanged(progress="+progress+", fromUser="+fromUser+"):");
            if(fromUser)
            {
                int pg = 0;
                try {
                    if(BLOCK_PG_np != null) pg = Integer.valueOf(BLOCK_PG_np.getLabel(), 16);
                } catch(Exception ignored) {}
            //  int sheet = ((pg / 0x1000) * 0x100) + progress * 0x8;
                int sheet = ((pg         ) * 0x100) + progress * 0x8;
                mRTabs.select_UNICODE_SHEET( sheet );
            //  seekBar.setLabel( String.format("%03X", sheet) );
            }
	}
	@Override public void onStartTrackingTouch(SeekBar seekBar) { }
	@Override public void onStopTrackingTouch (SeekBar seekBar) { }
    };

    //}}}

/* DOC .. online {{{
:    UNICODE 29215 characters:
:new ../RTabsDoc/UnicodeData.txt
:new http://www.unicode.org/Public/8.0.0/ucd/UnicodeData.txt
:!start explorer "http://developer.android.com/reference/android/os/AsyncTask.html"
:!start explorer "http://unicode.org/charts"
:!start explorer "http://unicode.org/cldr/utility/character.jsp?a=0075"
EURO:
     20A0;EURO-CURRENCY SIGN     ;Sc;0;ET;;;;;N;;;;;
     20AC;EURO SIGN              ;Sc;0;ET;;;;;N;;;;;
    1F4B6;BANKNOTE WITH EURO SIGN;So;0;ON;;;;;N;;;;;
*/ // }}}
    // UNICODE TOOL TAGS {{{

    private static       NotePane            BLOCK_PG_np  = null;   // BLOCK PLANE-BLOCK
    private static       NotePane            BLOCK_SB_np  = null;   // BLOCK SEEKBAR
    private              SeekBar	     BLOCK_SB     = null;


    private static       NotePane            BLOCK_U2_np  = null;   // COL2 UP
    private static       NotePane            BLOCK_D2_np  = null;   // COL2 DOWN

    private static       NotePane            BLOCK_U3_np  = null;   // COL3 UP
    private static       NotePane            BLOCK_D3_np  = null;   // COL3 DOWN

    private static       NotePane            BLOCK_R0_np  = null;   // ROW0
    private static       NotePane            BLOCK_R1_np  = null;   // ROW1
    private static       NotePane            BLOCK_R2_np  = null;   // ROW2
    private static       NotePane            BLOCK_R3_np  = null;   // ROW3
    private static       NotePane            BLOCK_R4_np  = null;   // ROW4
    private static       NotePane            BLOCK_R5_np  = null;   // ROW5
    private static       NotePane            BLOCK_R6_np  = null;   // ROW6
    private static       NotePane            BLOCK_R7_np  = null;   // ROW7

    private static final String              BLOCK_TOOL   = "#BLOCK_TOOL ";
    private static final String              BLOCK_PS     =  "BLOCK_PS";
    //}}}
    // clear_BLOCK_np {{{
    private void clear_BLOCK_np(String caller)
    {
        caller += "] [clear_BLOCK_np";
//*TOOL*/Settings.MOC(TAG_TOOL, caller);

        blockPages   = null;    // GOTO BLOCK PAGE buttons

        BLOCK_PG_np  = null;    // PAGE BLOCK hex byte 5 and 4

        BLOCK_SB_np  = null;
        BLOCK_SB     = null;    // SEEKBAR BLOCK hex byte 1 and 0

    //  TOOL_URL_np  = null;

        BLOCK_U3_np  = null;
        BLOCK_D3_np  = null;

        BLOCK_U2_np  = null;
        BLOCK_D2_np  = null;

        BLOCK_R0_np  = null;
        BLOCK_R1_np  = null;
        BLOCK_R2_np  = null;
        BLOCK_R3_np  = null;
        BLOCK_R4_np  = null;
        BLOCK_R5_np  = null;
        BLOCK_R6_np  = null;
        BLOCK_R7_np  = null;
    }
    //}}}
    // check_BLOCK_np {{{
    private void check_BLOCK_np(NotePane np)
    {
        if(     (BLOCK_SB_np == null) && (np.tag.startsWith("BLOCK_SB"))) { BLOCK_SB_np  = np; np.tag  = BLOCK_TOOL+np.tag; }
        else if((BLOCK_D2_np == null) && (np.tag.startsWith("BLOCK_D2"))) { BLOCK_D2_np  = np; np.tag  = BLOCK_TOOL+np.tag; }
        else if((BLOCK_D3_np == null) && (np.tag.startsWith("BLOCK_D3"))) { BLOCK_D3_np  = np; np.tag  = BLOCK_TOOL+np.tag; }
        else if((BLOCK_PG_np == null) && (np.tag.startsWith("BLOCK_PG"))) { BLOCK_PG_np  = np; np.tag  = BLOCK_TOOL+np.tag; }
        else if((BLOCK_R0_np == null) && (np.tag.startsWith("BLOCK_R0"))) { BLOCK_R0_np  = np; np.tag  = BLOCK_TOOL+np.tag; }
        else if((BLOCK_R1_np == null) && (np.tag.startsWith("BLOCK_R1"))) { BLOCK_R1_np  = np; np.tag  = BLOCK_TOOL+np.tag; }
        else if((BLOCK_R2_np == null) && (np.tag.startsWith("BLOCK_R2"))) { BLOCK_R2_np  = np; np.tag  = BLOCK_TOOL+np.tag; }
        else if((BLOCK_R3_np == null) && (np.tag.startsWith("BLOCK_R3"))) { BLOCK_R3_np  = np; np.tag  = BLOCK_TOOL+np.tag; }
        else if((BLOCK_R4_np == null) && (np.tag.startsWith("BLOCK_R4"))) { BLOCK_R4_np  = np; np.tag  = BLOCK_TOOL+np.tag; }
        else if((BLOCK_R5_np == null) && (np.tag.startsWith("BLOCK_R5"))) { BLOCK_R5_np  = np; np.tag  = BLOCK_TOOL+np.tag; }
        else if((BLOCK_R6_np == null) && (np.tag.startsWith("BLOCK_R6"))) { BLOCK_R6_np  = np; np.tag  = BLOCK_TOOL+np.tag; }
        else if((BLOCK_R7_np == null) && (np.tag.startsWith("BLOCK_R7"))) { BLOCK_R7_np  = np; np.tag  = BLOCK_TOOL+np.tag; }
        else if((BLOCK_U2_np == null) && (np.tag.startsWith("BLOCK_U2"))) { BLOCK_U2_np  = np; np.tag  = BLOCK_TOOL+np.tag; }
        else if((BLOCK_U3_np == null) && (np.tag.startsWith("BLOCK_U3"))) { BLOCK_U3_np  = np; np.tag  = BLOCK_TOOL+np.tag; }

    }
    //}}}
    // create_BLOCK_TOOL {{{
    private boolean create_BLOCK_TOOL(NotePane np)
    {
        if((np != BLOCK_SB_np) || (BLOCK_SB != null))
            return false;

        // SeekBar
        BLOCK_SB = new SeekBar( RTabs.activity );

        // BLOCK VALUE MAX
        BLOCK_SB.setMax(0xFF / 0x8);    // bites 1 and up by sheet-halves (0-7 .. 8-F) .. (byte0=col)

        // SeekBar events
        BLOCK_SB.setOnSeekBarChangeListener( BLOCK_SB_OnSeekBarChangeListener );
        BLOCK_SB.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent p_event) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        //  float tss = 1;
        //  BLOCK_SB.setBackgroundColor(                 DashBackColor);
        //  BLOCK_SB.setShadowLayer    (3*tss, tss, tss, TxShadowColor);

        // START WITH LAST SELECTED
        int   sheet = 0;
        try { sheet = Integer.valueOf(Settings.UNICODE_SHEET, 16); } catch(Exception ignored) {}
        if(sheet != 0) mRTabs.select_UNICODE_SHEET( sheet );

        return true;
    }
    //}}}
    // working_profile_contains_a_BLOCK_TOOL {{{
    private boolean working_profile_contains_a_BLOCK_TOOL()
    {
        if(BLOCK_SB_np == null) return false;
        if(BLOCK_D2_np == null) return false;
        if(BLOCK_D3_np == null) return false;
        if(BLOCK_PG_np == null) return false;
        if(BLOCK_R0_np == null) return false;
        if(BLOCK_R1_np == null) return false;
        if(BLOCK_R2_np == null) return false;
        if(BLOCK_R3_np == null) return false;
        if(BLOCK_R4_np == null) return false;
        if(BLOCK_R5_np == null) return false;
        if(BLOCK_R6_np == null) return false;
        if(BLOCK_R7_np == null) return false;
        if(BLOCK_U2_np == null) return false;
        if(BLOCK_U3_np == null) return false;

//*TOOL*/Settings.MOM(TAG_TOOL, "working_profile_contains_a_BLOCK_TOOL: ...return true");
        return true;
    }
//}}}
        // layout_BLOCK_TOOL {{{
        private boolean layout_BLOCK_TOOL(NotePane np, ViewGroup container)
        {
            if((BLOCK_SB == null) || (np != BLOCK_SB_np))
                return false;

            // was already parented
            ViewGroup np_parent = (ViewGroup)BLOCK_SB.getParent();
            if(np_parent != null) {
    //*TOOL*/Settings.MON(TAG_TOOL, "layout_BLOCK_TOOL", "np_parent.removeView("+np.tag+")");
                np_parent.removeView( BLOCK_SB);
            }

            container.addView( BLOCK_SB );

            return true;
        }
        //}}}
    // listBlockPages {{{
    private Integer[]     blockPages = null;
    @SuppressWarnings("ConstantConditions")
    private Integer[] get_blockPages()
    {
        // RETURN THE SORTED COLLECTION OF "BLOCKPAGE" NAMES TAGGED IN THE CURRENT PROFILE

        if(blockPages != null)
            return blockPages;

        // COLLECT BLOCKPAGE TAGS
        List<Integer> list = new ArrayList<>();
        for(Map.Entry<String, Object> entry : TABS_Map.entrySet())
        {
            if(entry.getKey() == Settings.ENTRY_PALETTE) continue;
            NotePane np = (NotePane)entry.getValue();
            if( np.tag.startsWith("BLOCKPAGE") ) {
                //......................................0123456789
                //......................................BLOCKPAGE000 .. BLOCKPAGE100
                try { list.add( Integer.valueOf(np.tag.substring(9), 16) ); } catch(Exception ignored) {}
            }
        }

        // RETURN A SORTED STRING ARRAY
        blockPages =  list.toArray(new Integer[ list.size() ]);
        if(blockPages != null) {
            Arrays.sort( blockPages );
if(D) log("...get_blockPages ...found "+ blockPages.length +" BLOCKPAGES");
        }
        else {
if(D) log("...get_blockPages ...found NO BLOCKPAGES");
        }
        return blockPages;
    }
    //}}}
    // clear_BLOCK_ROWS_LABEL {{{
    public void clear_BLOCK_ROWS_LABEL()
    {
    //  BLOCK_R0_np.setText("...");
        BLOCK_R1_np.setText("...");
        BLOCK_R2_np.setText("...");
        BLOCK_R3_np.setText("...");
        BLOCK_R4_np.setText("...");
        BLOCK_R5_np.setText("...");
        BLOCK_R6_np.setText("...");
        BLOCK_R7_np.setText("...");
    }
    //}}}
    // get_next_defined_BLOCKPAGE {{{
    private int get_next_defined_BLOCKPAGE(int page, boolean forward)
    {
if(D) log("get_next_defined_BLOCKPAGE("+ String.format("%X", page) +", "+ forward +"):");


        // LOOK FOR THE TAG DEFINING THIG PAGE IN ITS TAB'S TAG
        Integer[] pages = get_blockPages();
        if(pages == null)
        {
if(D) log(" get_next_defined_BLOCKPAGE [none defined] ...return=["+ String.format("%X", page) +"]");
            return page;
        }

        // NOT FOUND, LOOK FOR THE CLOSEST ONE
        int prev = pages[0];                // min
        int next = pages[pages.length-1];   // max
        int              len = pages.length;
        for(int i=0; i < len; ++i)
        {
            int value  = pages[i];
            if     (value <  page) { prev = value;                      }
            else if(value  > page) { next = value;               break; }
            else /*(value == page*/{ prev = value; next = value; break; }
        }

        page = (forward) ? next : prev;

if(D) log(" get_next_defined_BLOCKPAGE ...returning ["+ String.format("%X", page) +"]");
        return page;
    }
    //}}}
    // get_sheet_PG_R0_offset {{{
    private int get_sheet_PG_R0_offset(int offset)
    {
        int r0 = 0;
        int pg = 0;
        try {
            // ................................................................................. //   543210
            if(BLOCK_PG_np != null) pg = Integer.valueOf(BLOCK_PG_np.getLabel(), 16); // 0xXXX--- .. PAGE (PAGE 0..16)
            if(BLOCK_R0_np != null) r0 = Integer.valueOf(BLOCK_R0_np.text, 16); // 0x---XX- ..  ROW (+00 or +08)
        } catch(Exception ignored) {}                                                                 // 0x-----X ..  COL (0..F)
        // ..................................................................................... //     1000 .. PAGE  0
        // ..................................................................................... //   100000 .. PAGE 16

        int sheet = pg*0x100 + r0 + offset;
        if(sheet < 0) sheet = 0;

        return sheet;
    }
    // }}}
    // store_UNICODE_PRESET {{{
    private void store_UNICODE_PRESET(int preset_num, String value)
    {
if(D) log("store_UNICODE_PRESET(preset_num=["+preset_num+"], value=["+value+"]):");
if(D) log("=== UNICODE_PRESET=["+ Settings.UNICODE_PRESET +"]");

        CmdParser.parse( Settings.UNICODE_PRESET ); // 1=XXXX 2=XXXX (...)

        StringBuilder sb = new StringBuilder();
        sb.append("UNICODE_PRESET");

        String preset_key = ""+preset_num;

        String value_to_be_stored = value;
        int              len = CmdParser.args.length;
        for(int i=1; i < len; ++i)
        {
            String[] kv = CmdParser.args[i].split("=");
            if(kv.length != 2) continue;

            // update existing preset
            if( kv[0].equals( preset_key ) )
            {
                if(!value.equals( kv[1] ))
                    sb.append(",").append(preset_num).append("=").append(value);
                value_to_be_stored = null; // consumed
            }
            // copy other preset .. but discard more of the same preset
            else if( !kv[1].equals( value ) )
            {
                sb.append(",").append(CmdParser.args[i]);
            }
        }
        // add new preset
        if(value_to_be_stored != null)
            sb.append(",").append(preset_num).append("=").append(value);

        Settings.UNICODE_PRESET = sb.toString().trim();
if(D) log(">>> UNICODE_PRESET=["+ Settings.UNICODE_PRESET +"]");

        //Settings.SaveSettings("store_UNICODE_PRESET");
    }
    //}}}
    // load_UNICODE_PRESET {{{
    private void load_UNICODE_PRESET()
    {
if(D) log("load_UNICODE_PRESET(): UNICODE_PRESET=["+ Settings.UNICODE_PRESET +"]");

        CmdParser.parse( Settings.UNICODE_PRESET ); // 1=XXXX 2=XXXX (...)

        for(Map.Entry<String, Object> entry : TABS_Map.entrySet())
        {
            if(entry.getKey() == Settings.ENTRY_PALETTE) continue;
            NotePane np = (NotePane)entry.getValue();

            // look for preset NotePanes
            if( np.tag.startsWith(BLOCK_PS) ) // PRESET SHEET TOOL
            {
                int preset_num = 0;
                try { preset_num = Integer.valueOf( np.tag.substring(8) ); } catch(Exception ignored) {}
                if(preset_num > 0) {
                    np.setText( CmdParser.getArgValue(""+preset_num, "-") );
                    np.button.fitText();
if(D) log("LOAD PRESET["+ preset_num +"]=["+ np.text +"]");
                }
            }
        }
    }
    //}}}
    // }}}
    /** COMMAND */
    // sendTabCommand {{{
    public void sendTabCommand(NpButton np_button                   ) { sendTabCommand(np_button, false); }

    public void sendTabCommand(NpButton np_button, boolean longClick)
    {
        // [cmdLine] .. f(np.tag) {{{
        set_last_sendTabCommand_np_button( np_button );

        // [np]
        NotePane np = (NotePane)np_button.getTag();
        if(np     == null) return;

        // [tag]
        if(np.tag == null) return;
        String  cmdLine = Settings.Del_color_hex_from_text( np.tag );
        if(cmdLine == "") return;

        // [cmdLine]
        String caller = "sendTabCommand("+cmdLine+")";
//*TOUCH*/Settings.MOC(TAG_TOUCH, caller);
//*TOUCH*/Settings.MOM(TAG_TOUCH, "...longClick=["+longClick+"])");

        CmdParser.parse( cmdLine );
        String cmd = CmdParser.cmd;
        // }}}
        // [raise_np] [pulse_np] {{{
        if( longClick )
            mRTabs.raise_np( np );
        else
            mRTabs.pulse_np( np );

        //}}}
        // [longClick]->return .. (unicode block preset tool) {{{
        if( np.tag.startsWith(BLOCK_PS) ) {
            if( longClick ) select_BLOCK_np(np, true ); //  store_preset);
            else            select_BLOCK_np(np, false); // !store_preset);
            return;
        }
        // }}}
        // [magnify_np]->return .. f(longClick or #) {{{
        if(longClick || (cmdLine.startsWith("#") && !cmdLine.startsWith( BLOCK_TOOL )))
        {
            mRTabs.magnify_np(np, caller);

            return;
        }
        // }}}
        // [magnify_np]->return .. (personal-note) {{{
        if( cmdLine.equals("NOTE") )
        {
            mRTabs.magnify_np(np, caller);

            return;
        }
        //}}}
        // [select_TOOL_URL_action]->return {{{
        if( select_TOOL_URL_action(np) )
            return;

        // }}}
        // [select_BLOCK_np]->return {{{
        if( select_BLOCK_np(np, false) ) // !store_preset
            return;

        //}}}
        // [select_tool]->return .. (WORKBENCH) {{{
        if( cmdLine.startsWith( "TOOL " )) {
            select_tool( cmdLine );

            return;
        }
        //}}}
        // [CMD_SENDKEYSTEXT] or [CMD_SENDINPUTTEXT]->[sendCmd]->do-not-return {{{
        if(        cmdLine.equals( Settings.CMD_SENDKEYSTEXT  )
                || cmdLine.equals( Settings.CMD_SENDINPUTTEXT )
          ) {
            cmdLine += " "+ np.text.replace("\n","\\n");

            sendCmd(cmdLine, caller);
          }
        //}}}
        // [parse_KEY_VAL]->return {{{
        if(Settings.can_parse_KEY_VAL(cmdLine))
        {
            parse_KEY_VAL(cmdLine, caller);
            mRTabs.sync_np(caller);
            return;
        }
        //}}}
        // [parse_PROFILE_cmdLine]->return .. f(CMD_PROFILE) {{{
        if(cmd.equals( Settings.CMD_PROFILE ))
        {
            cmdLine = parse_PROFILE_cmdLine( cmdLine );
            if(cmdLine == "")
                return;
        }
        //}}}
    //  if( cmd.equals("WEBVIEW" ) )    mRTabs.magnify_np( np );
    //  if( cmd.equals("BOOKMARK") )    mRTabs.magnify_np( np );
        // [send_cmd_text] .. f(not cumsumed at this point) {{{
        CmdParser.parse( cmdLine );
        if     ( Settings.IsADashCmdLine     ( cmdLine ) )   mRTabs.send_cmd_text("SendDash "+ cmdLine +" "+ np.text);
        else if( mRTabs  .is_ACTIVITY_BUILTIN( cmd     ) )   mRTabs.send_cmd_text(             cmdLine);
        else if( Settings.is_SETTINGS_BUILTIN( cmdLine ) )   mRTabs.send_cmd_text(             cmdLine);
        else if( Profile .is_PROFILES_BUILTIN( cmd     ) )   mRTabs.send_cmd_text(             cmdLine);
        else if( Settings.can_parse_KEY_VAL  ( cmdLine ) )   mRTabs.send_cmd_text("SENDKEYS "+ cmdLine);
        else if(                     "" !=   ( cmdLine ) )   mRTabs.send_cmd_text("SENDKEYS "+ cmdLine); // FIXME COULD DEFAULT TO [SENDINPUT]
        //}}}
    }
    //}}}
    // onDown_forget_last_sendTabCommand_np_button {{{
    public  static /*............................................*/ NpButton               last_sendTabCommand_np_button = null;
    private static void           set_last_sendTabCommand_np_button(NpButton nb) {         last_sendTabCommand_np_button =   nb; }
    public  static NpButton       get_last_sendTabCommand_np_button(           ) {  return last_sendTabCommand_np_button       ; }
    public  static void onDown_forget_last_sendTabCommand_np_button(           ) {         last_sendTabCommand_np_button = null; }
    //}}}
    public String parse_PROFILE_cmdLine(String cmdLine) //{{{
    {
        String caller = "parse_PROFILE_cmdLine("+cmdLine+")";
        if(D) log(caller);
//*PROFILE*/Settings.printStackTrace(caller);//PROFILE

        mRTabs.hide_PROFILE_HANDLES(caller);
        // CONTROLS_TABLE {{{
        CmdParser.parse( cmdLine );
      //String      profile_name = CmdParser.arg1;
        String      profile_name = CmdParser.argLine; // [Profile.FREETEXT_TABLE] .. (with file_name argument)
        if(         profile_name.equals(""))
        {
            return cmdLine;
        }
        if(         profile_name.equals( Settings.CONTROLS_TABLE ))
        {
            mRTabs.show_CONTROLS_TABLE(caller);
            return "";
        }
        //}}}
        // PROFILES_TABLE {{{
        else if(    profile_name.equals( Settings.PROFILES_TABLE ))
        {
            mRTabs.show_PROFILES_TABLE(caller);
            return "";
        }
        //}}}
        // PROFHIST_TABLE {{{
        else if(    profile_name.equals( Settings.PROFHIST_TABLE ))
        {
            mRTabs.show_PROFHIST_TABLE(caller);
            return "";
        }
        //}}}
        // SOUNDS_TABLE {{{
        else if(    profile_name.equals( Settings.SOUNDS_TABLE ))
        {
            mRTabs.show_SOUNDS_TABLE(caller);
            return "";
        }
        //}}}
        // DOCKINGS_TABLE {{{
        else if(    profile_name.equals( Settings.DOCKINGS_TABLE ))
        {
            mRTabs.show_DOCKINGS_TABLE(caller);
            return "";
        }
        //}}}
        // USER PROFILE {{{
        else if(mRTabs.get_Working_profile_pending_changes() > 0)
        {
            mRTabs.check_current_profile_pending_changes_then_load_profile( profile_name );
            return "";
        }
        else if( load_USER_PROFILE(profile_name, caller) )
        {
            if( !isConnected() )
                return "";

            // SHARE LOADED PROFILE AND TIMESTAMP WITH SERVER
            cmdLine = "PROFILE "+ Settings.LoadedProfile.name +" PRODATE="+ Settings.PRODATE;
            if(D) log(caller+": ...forwarding ["+ cmdLine +"])");
            return cmdLine;
        }
        //}}}
        // DOWNLOAD UNKNOWN PROFILE {{{
        else {
            CMD_PROFILE_UPDATE = CMD_PROFILE_DOWNLOAD+" "+profile_name;
            mRTabs.profile_download(caller);
            return "";
        }
        //}}}
    }
    //}}}
    /** BUTTON */
    // CLICKING  {{{
    private static final String TAB_GESTURE_handled_by_CLICK     = "CLICK";
    private static final String TAB_GESTURE_handled_by_FLING     = "FLING";
    private static final String TAB_GESTURE_handled_by_LONGCLICK = "LONGCLICK";
    private static final String TAB_GESTURE_handled_by_MOVE      = "MOVE";
    private static       String TAB_GESTURE_handled_by           = null;

    //}}}
    // MOVING {{{
//  private static final int MOVE_DISTANCE_MIN = 10; // discard only very slight moves

    private static       int np_ACTION_UP_x    = 0;
    private static       int np_ACTION_UP_y    = 0;
    //}}}
    // FLINGING {{{
    // set_TAB_GESTURE_handled_by_fling {{{
    public static void set_TAB_GESTURE_handled_by_fling()
    {
        TAB_GESTURE_handled_by = TAB_GESTURE_handled_by_FLING;
//*TOUCH*/Settings.MON(TAG_TOUCH, "set_TAB_GESTURE_handled_by_fling", "...TAB_GESTURE_handled_by: "+TAB_GESTURE_handled_by);
    }
    //}}}
    //}}}
    // GROUPING  {{{
     // {{{
    private static    long grouping_start_time          = 0;
    private static  String last_touched_url             = null;
    private static  String profile_key                  = null;
    public  static boolean Grouping_ACTION_UP_handled = false;

    // }}}
    // stop_GROUPING {{{
    public void stop_GROUPING(String caller)
    {
        caller += "] [stop_GROUPING";
//*WEBGROUP*/Settings.MOC(TAG_WEBGROUP, caller);

        last_touched_url           = null; // start from scratch
        profile_key                = null; // start from scratch
        Grouping_ACTION_UP_handled = true; // resume normal tabs click handing
    }
    public boolean is_GROUPING()
    {
        return last_touched_url != null;
    }
    //}}}
    //}}}
    // np_OnClickListener {{{
    private final View.OnClickListener np_OnClickListener = new View.OnClickListener()
    {
        @Override public void onClick(View np_button)
        {
            // TAB_GESTURE_handled_by {{{
            String caller = "np_OnClickListener";
//*TOUCH*/Settings.MOC(TAG_TOUCH, caller);
            if(TAB_GESTURE_handled_by != null)
            {
//*TOUCH*/Settings.MON(TAG_TOUCH, caller, "...TAB_GESTURE_handled_by=["+TAB_GESTURE_handled_by+"] ...return");
                return;
            }
            else {
                TAB_GESTURE_handled_by = TAB_GESTURE_handled_by_CLICK;
//*TOUCH*/Settings.MON(TAG_TOUCH, caller, "...TAB_GESTURE_handled_by: "+TAB_GESTURE_handled_by +" ...fall through...");
                // fall through...
            }
            //}}}
            // STOP GROUPING {{{
            if( is_GROUPING() )
            {
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "Grouping_ACTION_UP_handled=["+Grouping_ACTION_UP_handled+"]");
                Grouping_ACTION_UP_handled = true;
                stop_GROUPING(caller);
                mRTabs.stop_GLOWING(caller);
                long grouping_duration = System.currentTimeMillis() - grouping_start_time;
                if(grouping_duration > 1000) return;
            }
            //}}}
            // CLICK STANDARD HANDLING {{{
            int cart_state =  mRTabs.get_cart_state(caller);
            if( cart_state == Settings.CART_STATE_DEFAULT)
            {
//*TOUCH*/Settings.MON(TAG_TOUCH, caller, String.format("%s CLICKED [%3d @ %3d]", np_button, np_ACTION_UP_x, np_ACTION_UP_y));

                // SELECT CLOSEST BUTTON .. f(closest touch offset to center) .. based on last np_OnTouchListener.ACTION_UP event coordinates
                NotePane np_at_xy_closest
                    = (np_button.getParent() == tabs_container)
                    ?  get_TABS_np_at_xy_closest(np_ACTION_UP_x, np_ACTION_UP_y, caller)
                    :  null;
                if(        (np_at_xy_closest        != null     )
                        && (np_at_xy_closest.button != np_button))
                {
//*TOUCH*/Settings.MON(TAG_TOUCH, caller, np_button +"...REDIRECTED TO "+ np_at_xy_closest.button);
                    sendTabCommand(np_at_xy_closest.button, false); // !longClick
                }
                else {
//*TOUCH*/Settings.MON(TAG_TOUCH, caller, np_button +"...NOT REDIRECTED TO NEIGHBORING BUTTON");
                    sendTabCommand((NpButton)np_button, false); // !longClick
                }
            }
            // }}}
            // CLICK CART HANDLING {{{
            else {
                NotePane np = get_np_for_button( (NpButton)np_button );
                if(np != null) {
                    switch( cart_state ) {
                        case Settings.CART_STATE_DEL:
                            cart_save_from ( np );
                            break;

                        case Settings.CART_STATE_ADD:
                            cart_extract_to( np );
                            break;
                    }
                }
            }
            // }}}
        }
    };
     //}}}
    // np_OnLongClickListener{{{
    private final View.OnLongClickListener np_OnLongClickListener = new View.OnLongClickListener()
    {
        @Override public boolean onLongClick(View np_button)
        {
            // TAB_GESTURE_handled_by {{{
            String caller = "np_OnLongClickListener";
            if(TAB_GESTURE_handled_by != null)
            {
//*TOUCH*/Settings.MON(TAG_TOUCH, caller, "...TAB_GESTURE_handled_by=["+TAB_GESTURE_handled_by+"]");
                return false; // not consumed
            }
            else {
                TAB_GESTURE_handled_by = TAB_GESTURE_handled_by_LONGCLICK; // .. (state flag only .. nothing could possibly happen next, right?)
//*TOUCH*/Settings.MON(TAG_TOUCH, caller, "...TAB_GESTURE_handled_by: "+TAB_GESTURE_handled_by);
                // fall through...
            }
            //}}}
            // STOP GROUPING {{{
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "Grouping_ACTION_UP_handled=["+Grouping_ACTION_UP_handled+"]");
            if( is_GROUPING() ) {
                Grouping_ACTION_UP_handled = true;
                stop_GROUPING(caller);
                mRTabs.stop_GLOWING(caller);
                long grouping_duration = System.currentTimeMillis() - grouping_start_time;
                if(grouping_duration > 1000) return false;
            }
            //}}}
            // LONG CLICK STANDARD HANDLING {{{
            sendTabCommand((NpButton)np_button, true); // longClick
            // }}}
            return true; // consumed
        }
    };
    //}}}
    // np_OnTouchListener {{{
    private final View.OnTouchListener np_OnTouchListener = new View.OnTouchListener()
    {
        @Override public boolean onTouch(View np_button, MotionEvent event)
        {
            String caller = "np_OnTouchListener."+ Settings.Get_action_name(event);
            int action = event.getActionMasked();
            switch( action )
            {
            // ACTION_DOWN .. (reset TAB_GESTURE_handled_by on first gesture event) {{{
                case MotionEvent.ACTION_DOWN:
                        TAB_GESTURE_handled_by = null;
//*TOUCH*/Settings.MON(TAG_TOUCH, caller, "...TAB_GESTURE_handled_by set to null");
                        // RESET EVENT HANDLING FLAGS
                        init_has_moved_enough(event.getRawX(), event.getRawY(), caller);

                        // RESET [Grouping_ACTION_UP_handled] {{{
                        Grouping_ACTION_UP_handled        = false;
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "Grouping_ACTION_UP_handled=["+Grouping_ACTION_UP_handled+"]");
                        //}}}
                        // FILTER GUI STATE {{{
                        if( !mRTabs.check_scrolling_allowed(caller) )
                        {
//*TOUCH*/Settings.MON(TAG_TOUCH, caller, "...done [FILTER GUI STATE] !mRTabs.check_scrolling_allowed()");
                            return false;
                        }
                        //}}}
                        // FILTER NotePane f(NpButton) {{{
                        NotePane np        = get_np_for_button( (NpButton)np_button );
                        if(np == null)
                        {
//*TOUCH*/Settings.MON(TAG_TOUCH, caller, "...done [FILTER NotePane f(NpButton)]: (np == null)");
                            return false;

                        }
                        // }}}
                        // FILTER URL f(NotePane tag) {{{
                        String this_touched_url = get_np_url(np, caller);
                        if( TextUtils.isEmpty(this_touched_url) )
                        {
//*TOUCH*/Settings.MON(TAG_TOUCH, caller, "...done [FILTER URL f(NotePane tag)]: this_touched_url isEmpty");
                            return false;
                        }
                        // }}}
                        // TRACK WEBGROUP SELECTION {{{

                        // RESOURCES ASSERTED AT THIS POINT:
                        // + (Working_profile)
                        // + (last_touched_url)
                        // + (this_touched_url)

                        // GET [this_touched_url] UPDATED WEBGROUP (key & urls)
                        String url_profile_key = null;
                        ArrayList<String>   al = null;
                        if(profile_key != null) // i.e. pointless before first WEBGROUP search
                        {
                            url_profile_key = get_profile_key          (Settings.Working_profile, this_touched_url, caller);
                            al              = get_profile_url_ArrayList(Settings.Working_profile, this_touched_url);
                        }
                        // 1/4 - FIRST URL: LOOK FOR A WEBGROUP[profile_key] {{{
                        if(last_touched_url == null)
                        {
                            last_touched_url     = this_touched_url;
                            grouping_start_time  = System.currentTimeMillis();
                            // GLOW WEBGROUP {{{
                            if((profile_key = get_profile_key(Settings.Working_profile, this_touched_url, caller)) != null)
                            {
                                al = get_profile_url_ArrayList(profile_key, this_touched_url);
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "@@@ GLOW WEBGROUP SELECTION: ["+profile_key+"] (x"+al.size()+")");

                                for(int i=0; i < al.size(); ++i) {
                                    String url = al.get(i);
                                    np = get_np_with_url(url, caller);
                                    if((np != null) && (np.button != null))
                                        mRTabs.glow_button(np.button, caller);
                                }
                            }
                            //}}}
                            // OR GLOW SINGLE {{{
                            else {
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "@@@ GLOW SINGLE SELECTION");
                                mRTabs.glow_button(np.button, caller);
                            }
                            //}}}
                        }
                        // }}}
                        // 3/4 - (IN WEBGROUP) TOGGLE ONE FROM A TUPLE (GROUPED & GLOWING) {{{
                        else if((profile_key != null) && (profile_key == url_profile_key))
                        {
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "@@@ TOGGLE TUPLE SELECTION [IN WEBGROUP["+profile_key+"] (x"+al.size()+")]: ...REMOVING ["+np.name+"] FROM THE WEBGROUP");
                            // DELETE THIS ENTRY FROM ITS WEBGROUP  // {{{
                            int group_size = del_profile_url(profile_key, this_touched_url, caller);

//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "...REMOVING ["+np.name+"] FROM THE GLOWING QUEUE");
                            mRTabs.glow_button_remove((NpButton)np_button, caller);

                            // }}}
                            // 1/2 PICK WITH ONE THE REMAINING URLS {{{
                            if((al != null) && (al.size() > 1)) // NOTE: al may have been dereferenced by WEBGROUP when down to 2 entries
                            {
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "...WORKING FROM ONE OF REMAINING ("+al.size()+") WEBGROUP ENTRIES");
                                for(int i=0; i < al.size(); ++i) {
                                    String url = al.get(i);
                                    if( !TextUtils.equals(this_touched_url, url) )
                                    {
                                        last_touched_url = url;
                                        np = get_np_with_url(url, caller);
                                        if((np != null) && (np.button != null))
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "...last_touched_url picked from ["+np.name+"] (i.e. not the one removed)");
                                        break;
                                    }
                                }
                            }
                            //}}}
                            // 2/2 STOP GROUPING WHEN THIS WEBGROUP ENTRY HAS BEEN DELETED {{{
                            else {
                                if( TextUtils.equals(last_touched_url, this_touched_url) ) {
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "...WEBGROUP HAS BEEN DELETED ... WAITING FOR A NEW FIRST URL");
                                    mRTabs.stop_GLOWING(caller);
                                    stop_GROUPING(caller);
                                }
                            }
                            //}}}
                        }
                        // }}}
                        // 2/4 - (NO WEBGROUP) TOGGLE SINGLE SELECTION (ONLY GLOWING) {{{
                        else if( TextUtils.equals(last_touched_url, this_touched_url) )
                        {
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "@@@ TOGGLE SINGLE SELECTION [NO WEBGROUP]: ...REMOVING ["+np.name+"] FROM THE GLOWING QUEUE");
                            mRTabs.glow_button_remove((NpButton)np_button, caller);

                            mRTabs.stop_GLOWING(caller);
                            stop_GROUPING(caller);
                        }
                        //}}}
                        // 4/4 - (ADD THIS ONE) TO THE SELECTED WEBGROUP {{{
                        else {

                            // CHECK SELECTED GROUP SIZE {{{
                            // USE CURRENT PROFILE NAME AS A THE FIRST KEY ... (will be suffixed for more than one tupple in the same profile)
                            if(profile_key == null)
                            {
                                profile_key = Settings.Working_profile;
                                al = get_profile_url_ArrayList(profile_key, this_touched_url);
                            }

                            if((al != null) && (al.size() >= 3))
                            {
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "@@@ CANNOT ADD TO FULL WEBGROUP ["+profile_key+"] (x"+al.size()+")");
                            }
                            //}}}
                            // ADD URL TUPPLE TO A WEBGROUP {{{
                            else {

                                // CHECK IF SOME OTHER WEBGROUP CONTAINS THIS URL {{{
                                if((url_profile_key != null) && !TextUtils.equals(profile_key, url_profile_key))
                                {
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "@@@ THIS URL IS ALREADY IN WEBGROUP["+url_profile_key+"]");
                                }
                                //}}}
                                // ADD TUPPLE {{{
                                else {
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "@@@ ADDING A PAIR TO WEBGROUP ["+profile_key+"]");
                                    int      result = add_profile_url_pair(profile_key, last_touched_url, this_touched_url, caller);

                                    switch( result ) {
                                        // 1/3 - ALREADY GROUPED ... (should not happen: handled by STEP 2)
                                        case WEBGROUP_BOTH_FOUND:
                                            //{{{
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "@@@ BOTH WERE ALREADY IN THE WEBGROUP: ...REMOVING ["+np.name+"] FROM THE WEBGROUP");
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "*** SHOULD NOT HAPPEN ... TO BE HANDLED IN STEP 2: TOGGLE SELECTION ***");

                                            break;
                                            //}}}
                                            // 2/3 - NO ROOM FOR MORE THAN 3 ENTRIES .. (max number of MWebView on screen)
                                        case WEBGROUP_FULL_FOUND:
                                            //{{{
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "@@@ WEBGROUP ["+profile_key+"] IS FULL: ...NULL EFFECT");

                                            break;
                                            //}}}
                                            // 3/3 - HAS BEEN ADDED TO A WEBGROUP
                                        default:
                                        case WEBGROUP_URL1_FOUND:
                                        case WEBGROUP_URL2_FOUND:
                                        case WEBGROUP_NONE_FOUND:
                                            //{{{

                                            // get used [profile_key] (possibly suffixed when adding a new group)
                                            profile_key    = get_profile_key(Settings.Working_profile, this_touched_url, caller);

                                            int group_size = get_group_size (profile_key, this_touched_url);
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, caller, "@@@ ADDED TO WEBGROUP["+profile_key+"] (x"+group_size+"): ["+np.name+"]");

                                            mRTabs.glow_button((NpButton)np_button, caller); // GLOW ADDED URL

                                            last_touched_url = this_touched_url;    // TRACK URL ADDED

                                            break;
                                            //}}}
                                    }
                                }
                                //}}}
                            }
                            // }}}
                        }
                        //}}}
                        //}}}
                    break;
                    // }}}
            // ACTION_MOVE .. if(TAB_GESTURE_handled_by == null) {{{
            case MotionEvent.ACTION_MOVE:
                // TAB_GESTURE_handled_by {{{
                if(TAB_GESTURE_handled_by != null)
                {
//TOUCH//Settings.MON(TAG_TOUCH, caller, "...TAB_GESTURE_handled_by=["+TAB_GESTURE_handled_by+"]");
                    return false; // not consumed
                }
                //}}}
                // CHECK [has_moved_enough] {{{
                if( !has_moved_enough ) {
                    track_has_moved_enough(event.getRawX(), event.getRawY(), caller);

                    // STILL NOT MOVED ENOUGH
                    if( !has_moved_enough )
                        return false; // not consumed
                }
                //}}}
                // MOTION DETECTED .. (inhibits onClick and onLongClick) {{{
                ViewGroup np_parent = (ViewGroup)np_button.getParent();
                if(np_parent  != tabs_container)
                {
//*TOUCH*/Settings.MON(TAG_TOUCH, caller, "MOVING AN ["+ mRTabs.get_view_name( np_parent )+"] CHILD");
                    TAB_GESTURE_handled_by = TAB_GESTURE_handled_by_MOVE;

//*TOUCH*/Settings.MON(TAG_TOUCH, caller, "...TAB_GESTURE_handled_by: "+TAB_GESTURE_handled_by);
                }
                //}}}
                return false;
            //  }}}
            // ACTION_UP   .. (not handled here) {{{
            case MotionEvent.ACTION_UP:
                track_has_moved_enough(event.getRawX(), event.getRawY(), caller);

                if(np_button.getParent() == tabs_container)
                {
                    np_ACTION_UP_x = (int)event.getX() + (int)np_button.getX(); // in parent coordinate
                    np_ACTION_UP_y = (int)event.getY() + (int)np_button.getY(); // in parent coordinate
//*TOUCH*/Settings.MON(TAG_TOUCH, caller, String.format("%s TOUCHED [%3d @ %3d]", np_button.toString(), np_ACTION_UP_x, np_ACTION_UP_y));
                }
//*TOUCH*/Settings.MON(TAG_TOUCH, caller, "...done (ACTION_UP ...not handled here)");

                return false;
            // }}}
            // ACTION_CANCEL {{{
            case MotionEvent.ACTION_CANCEL:
//*TOUCH*/Settings.MON(TAG_TOUCH, caller, "...done: ");
                return false;
            // }}}
            }
//*TOUCH*/Settings.MON(TAG_TOUCH, "np_OnTouchListener", "...done");
            return false;
        }
    };
    //}}}
    // has_moved_enough {{{
    // {{{
    private static   float dx, dy;  // gesture distance
    private static   float px, py;  // gesture prevpoint
    private static boolean has_moved_enough;
    // }}}
    // init_has_moved_enough {{{
    private static void init_has_moved_enough(float x, float y, String caller)
    {
        // initialize moves sensing
        has_moved_enough = false;
        px = x; py = y; // gesture start prevpoint
        dx = 0; dy = 0; // gesture distance amount

//*TOUCH*/Settings.MOM(TAG_TOUCH, "] [init_has_moved_enough(x, y): ...clearing has_moved_enough=["+has_moved_enough+"]");
    }
    //}}}
    // track_has_moved_enough {{{
    private static boolean track_has_moved_enough(float x, float y, String caller)
    {
        // cumulate moves
        dx += Math.abs(x-px);   px = x;
        dy += Math.abs(y-py);   py = y;

        // assess distance threshold
        float               dxy = (float)Math.sqrt(dx*dx + dy*dy);
        has_moved_enough = (dxy >= Settings.SCALED_TOUCH_SLOP);//XXX MOVE_DISTANCE_MIN);

//*TOUCH*/Settings.MOM(TAG_TOUCH, "] [track_has_moved_enough("+ String.format("%3.1f %3.1f", x, y) +"): ...has_moved_enough=["+has_moved_enough+"] .. (dxy=["+dxy+"] < "+Settings.SCALED_TOUCH_SLOP+")");
        return has_moved_enough;
    }
//}}}
    // }}}
    /** DASH */
    // dash_OnTouchListener {{{
    private boolean dash_click_handled     = false;
    private    long dash_touched_down_time = 0;

    private final View.OnTouchListener dash_OnTouchListener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View view, MotionEvent event)
        {
            switch( event.getActionMasked() )
            {
                case MotionEvent.ACTION_DOWN:
//*TOUCH*/Settings.MON(TAG_TOUCH, "dash_OnTouchListener", "ACTION_DOWN");
                    dash_touched_down_time  = System.currentTimeMillis();
                    dash_click_handled      = false;
                    break;

                case MotionEvent.ACTION_UP:
//*TOUCH*/Settings.MON(TAG_TOUCH, "dash_OnTouchListener", "ACTION_UP: dash_click_handled=["+dash_click_handled+"]");

                    if(   !dash_click_handled                                            // .. (not handled by dash_OnLongClickListener)
                       && ((System.currentTimeMillis() - dash_touched_down_time) < 500)) // not held down
                    {
//  if     ( Settings.FREEZED                         ) { DashMsgPoll =      "UNFREEZING..."; update_dash(); mRTabs.set_APP_freezed_state(false ,"dash_OnTouchListener"); }
    if     ( Settings.OFFLINE                         ) { DashMsgPoll =    "GOING ONLINE..."; update_dash(); mRTabs.set_APP_offline_state(false ,"dash_OnTouchListener"); }
//  else if(!isConnected()                            ) { DashMsgPoll = "SIGNIN REQUIRED..."; update_dash(); } /* USER RESPONSIBILITY */
    else if(!Settings.FREEZED && battery_poll_resume()) { /* ONE-STEP-ONLY-BATTERY-REPORT */  progress_POLL_LOOP(); }
    else if(!Settings.FREEZED && !isConnected()       ) { DashMsgPoll = socket_log();         update_dash(); }
  /*else*/                                              { dash_adjust_textSize((TextView)view, event.getX(), event.getY(), view.getHeight()); }

    dash_click_handled  = true;
                    }
                    dash_touched_down_time  = 0;
                    break;

                case MotionEvent.ACTION_CANCEL:
//*TOUCH*/Settings.MON(TAG_TOUCH, "dash_OnTouchListener", "ACTION_CANCEL: dash_click_handled=["+dash_click_handled+"]");

                    dash_click_handled      = true;
                    dash_touched_down_time  = 0;
                    break;
            }
            return true; // consumed
        }
    };
    //}}}
    // dash_OnLongClickListener{{{
    private final View.OnLongClickListener dash_OnLongClickListener = new View.OnLongClickListener()
    {
        // XXX
        // XXX
        // XXX
        // XXX NOT USED .. (as of 160905)
        // XXX
        // XXX
        // XXX
        @Override public boolean onLongClick(View view)
        {
//*TOUCH*/Settings.MON(TAG_TOUCH, "dash_OnLongClickListener", "dash_click_handled=["+dash_click_handled+"]");

            if(dash_click_handled)  return false;

            dash_click_handled = true;
            NpButton.FitText((TextView)view, 0.8F);

            return false; // not consumed
        }
    };
    //}}}
    // dash_adjust_textSize {{{
    private static final float TEXT_SIZE_BIGGER  = 1.2f;
    private static final float TEXT_SIZE_FIT     = 1.0f;
    private static final float TEXT_SIZE_SMALLER = 0.8f;

    private void dash_adjust_textSize(TextView textView, float x, float y, float h)
    {
        float               factor = TEXT_SIZE_FIT    ; // MID .. FIT .. (default)
        if     (y <   h/3)  factor = TEXT_SIZE_SMALLER; // TOP .. SHRINK TEXT TO TOP
        else if(y > 2*h/3)  factor = TEXT_SIZE_BIGGER ; // BOT .. EXPAND TEXT TO BOTTOM

        if(factor == TEXT_SIZE_FIT)
        {
//*TOUCH*/Settings.MOC(TAG_TOUCH, "dash_adjust_textSize: ...FitText");
            NpButton.FitText(textView, 1.0F);
            return;
        }

        // adjust text size
        float textSize = textView.getTextSize();
        textSize *= factor;
//*TOUCH*/Settings.MOC(TAG_TOUCH, "dash_adjust_textSize(textView, factor=["+ factor +"] textSize=["+ textSize +"] ... x=["+x+"], y=["+y+"]): h/3=["+ h/3 +"]");

        // caps
        if     (textSize <  8   ) textSize = 8;
        else if(textSize > (h/4)) textSize = h/4;    // maxed out to 4 lines

        // apply
    //  textView.setTextSize( textSize );
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

/*
                // EVAL TEXT SIZE F(Y) {{{
                TextView editText   = (TextView)view;

                int this_y      = (int)event.getY();
                int linesCount  = editText.getText().toString().split("\n").length;

                //  int textSize    = editText.getHeight() / (1+linesCount);
                //  int textSize    = this_y / 5;

                int textSize    = (int)(this_y / (2 * linesCount));

                //if( textSize > 50) textSize    = 50;
                //}}}
                // ADJUST TEXT SIZE {{{

                // freeze f(tiny size)
                if(textSize > 8) {
                DashNotePane_freezed = false;
                editText.setTextColor( DashForeColor );
                Handle.Set_cur_handle( null );
                }
                else {
                textSize = editText.getWidth() / 10;
                DashNotePane_freezed = true;
                editText.setText( "DASHBOARD\nFREEZED" );
                editText.setTextColor( NoteForeColor );
                Handle.Hide_all();
                }

                // apply text size
                //  editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                //  update_dash("TextSize", "" + (int) Math.floor(editText.getTextSize() + 0.5));

                //}}}
*/
    }
    //}}}
}

/* // {{{

:let @p="CART"
:let @p="COMM"
:let @p="CONNECT"
:let @p="HISTORY"
:let @p="PROFILE"
:let @p="POLL"
:let @p="READ"
:let @p="TAB"
:let @p="TABGET"
:let @p="TAG"
:let @p="TOOL"
:let @p="TOUCH"
:let @p="URL"
:let @p="WEBGROUP"
:let @p="WEBVIEW"
:let @p="\\(TABGET\\|TOUCH\\|WEBVIEW\\|GLOW\\|TOUCH\\)"
:let @p="\\(TOUCH\\|SETTINGS\\|CART\\|COMM\\|CONNECT\\|PROFILE\\|READ\\)"

:let @p="\\w\\+"

" activated
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

