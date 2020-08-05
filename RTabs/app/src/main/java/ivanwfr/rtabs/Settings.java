package ivanwfr.rtabs; // {{{

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.text.format.DateUtils;
import android.text.TextUtils;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.CookieManager;
import android.webkit.MimeTypeMap;
import android.webkit.ValueCallback;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// }}}
// ========================================================================
// Settings_TAG (200728:17h:35) =========================== [KEY_VAL pairs]
// ========================================================================
@SuppressWarnings("StringEquality")
public class Settings
{
    /** SYMBOLS */
    //{{{
/* DOC ONLINE {{{

:!start explorer "http://apps.timwhitlock.info/emoji/tables/unicode"
:!start explorer "http://www.fileformat.info/"
:!start explorer "http://www.fileformat.info/info/unicode/block/emoticons/list.htm"
:!start explorer "http://www.fileformat.info/info/unicode/char/search.htm?q=alpha&preview=entity"

/BROKEN_HEART\|CONFUSED_FACE\|CRYING_FACE\|FLUSHED_FACE\|ONE_HAND\|SLEEPING_FACE\|SMOKING_SYMBOL\|WORRIED_FACE
/\<BROKEN_HEART\>\|\<CONFUSED_FACE\>\|\<CRYING_FACE\>\|\<FLUSHED_FACE\>\|\<ONE_HAND\>\|\<SLEEPING_FACE\>\|\<SMOKING_SYMBOL\>\|\<WORRIED_FACE\>

*/ //}}}
    // UNICODE {{{

    public  static final String SYMBOL_FLAG                = "U+1F6A9";

    public  static final String SYMBOL_FIVE_LINE_STAFF     = "U+1D11A";

    public  static final String SYMBOL_ARROW_PAIRED_UP     = "\u21C8" ;
    public  static final String SYMBOL_ARROW_SINGLE_UPDOWN = "\u21C5" ;
    public  static final String SYMBOL_ARROW_PAIRED_DOWN   = "\u21CA" ;
    public  static final String SYMBOL_FLAG_CHEQUERED      = "U+1F3C1";
    public  static final String SYMBOL_DIVISION_SIGN       = "\u00F7" ;
    public  static final String SYMBOL_PLAY_PAUSE          = "\u23EF" ;

    public  static final String SYMBOL_CROSS_MARK          = "\u274C";
    public  static final String SYMBOL_UNDERSCORE          = "\u005F";

    public  static final String SYMBOL_ZERO_WIDTH_JOINER   = "\u200D";

    public  static final String SYMBOL_SMALL_PERCENT       = "\uFE6A";

    public  static final String SYMBOL_FISHEYE             = "\u25C9";
    public  static final String SYMBOL_EMPTY               = "\u23D8";
    public  static final String STRING_EMPTY               = "\u23D8";
    public  static final String SYMBOL_RELOAD              = "\u21BB";
    public  static final String SYMBOL_DOCK_HIDE           = "\u27EA";
    public  static final String SYMBOL_DOCK_SHOW           = "\u27EB";
    public  static final String SYMBOL_GUI_TYPE            = "\u26AB";
    public  static final String SYMBOL_MENU                = "\u2630";
    public  static final String SYMBOL_PRNEXT              = "\u27EB";
    public  static final String SYMBOL_PRPREV              = "\u27EA";

    public  static final String SYMBOL_BOX_VERTICAL        = "\u2503";
    public  static final String SYMBOL_FULL_BLOCK          = "\u2588";

    public  static final String SYMBOL_BLACK_CIRCLE        = "\u26AB" + SYMBOL_ZERO_WIDTH_JOINER;
    public  static final String SYMBOL_WHITE_CIRCLE        = "\u25CB" + SYMBOL_ZERO_WIDTH_JOINER;
    public  static final String SYMBOL_BLACK_STAR          = "\u2605" + SYMBOL_ZERO_WIDTH_JOINER;
    public  static final String SYMBOL_GLOBE               = "U+1F310";
    public  static final String SYMBOL_HOME                = "\u2302" + SYMBOL_ZERO_WIDTH_JOINER;
    public  static final String SYMBOL_INDEX               = "\u24D8" + SYMBOL_ZERO_WIDTH_JOINER;
    public  static final String SYMBOL_KEYBOARD            = "\u2328" + SYMBOL_ZERO_WIDTH_JOINER;
    public  static final String SYMBOL_NOTE                = "\u24DD" + SYMBOL_ZERO_WIDTH_JOINER;
    public  static final String SYMBOL_PENCIL              = "\u270F" + SYMBOL_ZERO_WIDTH_JOINER;
    public  static final String SYMBOL_WHITE_STAR          = "\u2606" + SYMBOL_ZERO_WIDTH_JOINER;

    public  static final String SYMBOL_DISK                = "\uD83D\uDCBE";
    public  static final String SYMBOL_FOLDER              = "\uD83D\uDCC2";
    private static final String SYMBOL_MOBILE              = "U+1F4F1";
    public  static final String SYMBOL_COMPUTER            = "U+1F4BB";
    public  static final String SYMBOL_INFORMATION         = "\u24D8";

    public  static final String SYMBOL_CHECK               = "\u2713";
    public  static final String SYMBOL_CHECK_HEAVY         = "\u2714";
    public  static final String SYMBOL_WASTEBASKET         = "U+1F5D1";

    public  static final String SYMBOL_GEAR                = "\u2699";
    public  static final String SYMBOL_SUN                 = "\u2600";

    public  static final String SYMBOL_ALPHA               = "\u03B1";
    public  static final String SYMBOL_MICRO               = "\u00B5";
    public  static final String SYMBOL_ALMOST_EQUAL_TO     = "\u2248";
    public  static final String SYMBOL_SIGMA               = "\u03A3";
    public  static final String SYMBOL_MAGNIFY_LEFT        = "\uD83D\uDD0D"; // U+1F50D
    public  static final String SYMBOL_MAGNIFY_RIGHT       = "\uD83D\uDD0E"; // U+1F50E
    public  static final String SYMBOL_MAGNIFYING_GLASS_L  = "U+1F50D";
    public  static final String SYMBOL_MAGNIFYING_GLASS_R  = "U+1F50E";

    public  static final String SYMBOL_RECTANGLE           = "\u2588";

    public  static final String SYMBOL_BLACK_LARGE_SQUARE  = "\u2B1B";
    public  static final String SYMBOL_DOUBLE_VERTICAL_BAR = "\u23F8";
    public  static final String SYMBOL_PLUS                = "\u2795";

    // MATH
    public  static final String SYMBOL_MATH_IDENTICAL      = "\u2261";

    public  static final String SYMBOL_F_CIRCLED           = "\u24BB";
    public  static final String SYMBOL_L_CIRCLED           = "\u24C1";

    public  static final String SYMBOL_S_CIRCLED           = "\u24C8";
    public  static final String SYMBOL_C_CIRCLED           = "\u24B8";
    public  static final String SYMBOL_ARROW_TWISTED_RIGHT = "\uD83D\uDD00"; //U+1F500
    public  static final String SYMBOL_ROCKET              = "\uD83D\uDE80"; //U+1F680

    // TRIANGLE
    public  static final String SYMBOL_UP_BLACK_TRIANGLE   = "\u25B2"; // [U]p
    public  static final String SYMBOL_RIGHT_BLACK_TRIANGLE= "\u25B6"; // [R]ight
    public  static final String SYMBOL_DOWN_BLACK_TRIANGLE = "\u25BC"; // [D]own
    public  static final String SYMBOL_LEFT_BLACK_TRIANGLE = "\u25C0"; // [L]eft

    public  static final String SYMBOL_LR_BLACK_TRIANGLE   = "\u25E2"; // [L]ower[R]ight
    public  static final String SYMBOL_LL_BLACK_TRIANGLE   = "\u25E3"; // [L]ower[L]eft
    public  static final String SYMBOL_UL_BLACK_TRIANGLE   = "\u25E4"; // [U]p   [L]eft
    public  static final String SYMBOL_UR_BLACK_TRIANGLE   = "\u25E5"; // [U]p   [R]ight
    public  static final String SYMBOL_DOWN_UP_TRIANGLE    = SYMBOL_DOWN_BLACK_TRIANGLE+"\n"+SYMBOL_UP_BLACK_TRIANGLE;

    public  static final String SYMBOL_LEFT_TRIANGLE       = "\u25C1";
    public  static final String SYMBOL_LIGHT_SHADE         = "\u2591";

    // POINTERS
    public  static final String SYMBOL_ARROW_DOWN_PAIR     = "\u21CA";
    public  static final String SYMBOL_ARROW_HEAVY         = "\u2794";
    public  static final String SYMBOL_ARROW_RIGHT         = "\u2192";
    public  static final String SYMBOL_ARROW_R_OVER_L      = "\u21C4";
    public  static final String SYMBOL_ARROW_SOUTH_EAST    = "\u27B7";
    public  static final String SYMBOL_ARROW_UP_PAIR       = SYMBOL_ARROW_PAIRED_UP;
    public  static final String SYMBOL_ARROW_UP_RIGHT      = "\u21B1";
    public  static final String SYMBOL_ARROW_WEDGE_RIGHT   = "\u27BD";
    public  static final String SYMBOL_LEFT_RIGHT          = "\uD83D\uDD1B";
    public  static final String SYMBOL_LEFT_RIGHT_ARROW    = "\u2194";
    public  static final String SYMBOL_NW_ARROW            = "\u2196";
    public  static final String SYMBOL_POINTING_LEFT       = "\u261A";
    public  static final String SYMBOL_POINTING_RIGHT      = "\u261B";
    public  static final String SYMBOL_RAISED_HAND         = "\u270B";
    public  static final String SYMBOL_UP                  = "\uD83C\uDD99";
    public  static final String SYMBOL_UP_DOWN_ARROW       = "\u2195";

    public  static final String SYMBOL_MATH_UP_ARROW       = "\u2191";           // [math] (narrow)
    public  static final String SYMBOL_MATH_RIGHT_ARROW    = "\u2192";//"\u21AA"; // [math] (narrow)
    public  static final String SYMBOL_MATH_DOWN_ARROW     = "\u2193";            // [math] (narrow)
    public  static final String SYMBOL_MATH_LEFT_ARROW     = "\u2190";//"\u21A9"; // [math] (narrow)

    public  static final String SYMBOL_UP_ARROW            = "\u2B06";
    public  static final String SYMBOL_RIGHT_ARROW         = "\u27A1";
    public  static final String SYMBOL_DOWN_ARROW          = "\u2B07";
    public  static final String SYMBOL_LEFT_ARROW          = "\u2B05";

    public  static final String SYMBOL_ROUND_PUSHPIN       = "\uD83D\uDCCC";
    private static final String SYMBOL_STAR                = "\uD83C\uDF20";
    public  static final String SYMBOL_PHONING             = "\uD83D\uDCF2";
    private static final String SYMBOL_BACK                = "\uD83D\uDD19";
    public  static final String SYMBOL_BROKEN_HEART        = "\uD83D\uDC94";
    public  static final String SYMBOL_BUG                 = "\uD83D\uDC1B";
    private static final String SYMBOL_CINEMA              = "\uD83C\uDFA6";
    private static final String SYMBOL_CONFUSED_FACE       = "\uD83D\uDE15";
    public  static final String SYMBOL_CONSTRUCTION        = "\uD83D\uDEA7";
    public  static final String SYMBOL_SNOW_FLAKE          = "\u2744";
    private static final String SYMBOL_CRYING_FACE         = "\uD83D\uDE22";
    private static final String SYMBOL_FLUSHED_FACE        = "\uD83D\uDE33";
    private static final String SYMBOL_INBOX               = "\uD83D\uDCE5";
    public  static final String SYMBOL_LARGE_RED_CIRCLE    = "\uD83D\uDD34";
    public  static final String SYMBOL_LARGE_BLUE_CIRCLE   = "\uD83D\uDD35";
    public  static final String SYMBOL_LARGE_BLACK_CIRCLE  = "\u2B24";
    public  static final String SYMBOL_CHECK_MARK          = "\u2714";
    public  static final String SYMBOL_WHITE_CHECK_MARK    = "\u2705";
    public  static final String SYMBOL_WARNING_SIGN        = "\u26A0";
    private static final String SYMBOL_WATCH               = "\u231A";
    public  static final String SYMBOL_LINK                = "\u1F517";

    public  static final String SYMBOL_AC_CURRENT          = "\u23E6";
    public  static final String SYMBOL_BATTERY             = "\uD83D\uDD0B"; // 1F50B
    public  static final String SYMBOL_POWER               = "\uD83D\uDD0C"; // 1F50C

    public  static final String SYMBOL_DEGREE              = "\u00B0";
    public  static final String SYMBOL_LOG                 = "\u33d2";
    public  static final String SYMBOL_LINK_DIAERESIS      = "\uD83D\uDD17";
    public  static final String SYMBOL_LOLLIPOP            = "\uD83C\uDF6D";
    private static final String SYMBOL_MAILBOX             = "\uD83D\uDCEC";
    private static final String SYMBOL_MONKEY              = "\uD83D\uDE4A";
    private static final String SYMBOL_NEW                 = "\uD83C\uDD95";
//  private static final String SYMBOL_NO_ENTRY            = "\uD83D\uDCF5";
    public  static final String SYMBOL_NO_ENTRY            = "\u26D4";
    private static final String SYMBOL_OK                  = "\uD83C\uDD97";
    public  static final String SYMBOL_ELLIPSIS            = "\u2026";
    public  static final String SYMBOL_ONE_HAND            = "\uD83D\uDE4B";
    public  static final String SYMBOL_PALETTE             = "\uD83C\uDFA8";
    private static final String SYMBOL_RABBIT              = "\uD83D\uDC30";
    private static final String SYMBOL_SLEEPING            = "\uD83D\uDCA4";
    private static final String SYMBOL_SLEEPING_FACE       = "\uD83D\uDE34";
    private static final String SYMBOL_SMOKING_SYMBOL      = "\uD83D\uDEAC";
    public  static final String SYMBOL_TRAFFIC             = "\uD83D\uDEA6";
    private static final String SYMBOL_UNAMUSED            = "\uD83D\uDE12";
    private static final String SYMBOL_WITH_HORN           = "\uD83D\uDE08";
    private static final String SYMBOL_WORRIED_FACE        = "\uD83D\uDE1F";

    public  static final String SYMBOL_no_connect_task  = SYMBOL_NO_ENTRY; //SYMBOL_LINK; // SYMBOL_UNAMUSED; // SYMBOL_FACE_HORN; // SYMBOL_CRYING_FACE;
    public  static final String SYMBOL_no_read_task     = SYMBOL_SLEEPING;
    public  static final String SYMBOL_polling          = SYMBOL_SMOKING_SYMBOL;
    public  static final String SYMBOL_timeout          = SYMBOL_MONKEY;
    public  static final String SYMBOL_expecting_reply  = SYMBOL_BACK;
    public  static final String SYMBOL_confused         = SYMBOL_CONFUSED_FACE;

    public  static final String SYMBOL_freezed          = SYMBOL_SNOW_FLAKE; //SYMBOL_CONSTRUCTION; //SYMBOL_NO_ENTRY; //SYMBOL_ONE_HAND;
    public  static final String SYMBOL_offline          = SYMBOL_NO_ENTRY;

    public  static final String SYMBOL_new_data         = SYMBOL_NEW;
    public  static final String SYMBOL_cooking_data     = SYMBOL_LARGE_RED_CIRCLE; // SYMBOL_INBOX;
    public  static final String SYMBOL_forging_data     = SYMBOL_LARGE_BLUE_CIRCLE;
    public  static final String SYMBOL_got_data         = SYMBOL_OK;

    public  static final String SYMBOL_KEY_VAL          = SYMBOL_LEFT_RIGHT_ARROW;

    public  static final String SYMBOL_CART             = SYMBOL_LARGE_RED_CIRCLE; // FIXME
    public  static final String SYMBOL_CTRL             = SYMBOL_BLACK_LARGE_SQUARE;
    public  static final String SYMBOL_DOCK             = SYMBOL_RECTANGLE;
    public  static final String SYMBOL_PROF             = SYMBOL_STAR;
    public  static final String SYMBOL_TABS             = SYMBOL_CINEMA;

    public  static final String SYMBOL_WARN             = SYMBOL_WARNING_SIGN;

    public  static final String SYMBOL_GOOD             = SYMBOL_CHECK_MARK;
    public  static final String SYMBOL_IDLE             = SYMBOL_WHITE_CHECK_MARK;

    public  static final String SYMBOL_TIME             = SYMBOL_WATCH;

    public  static final String SYMBOL_O_LANDSCAPE      = SYMBOL_LEFT_RIGHT_ARROW;
    public  static final String SYMBOL_O_PORTRAIT       = SYMBOL_ARROW_UP_PAIR  ;

    //}}}
    // UNICODE (aggregated) {{{
//  public  static final String SYMBOL_BROWSER             = SYMBOL_INFORMATION                   + SYMBOL_COMPUTER;
//  public  static final String SYMBOL_SENDKEY             = SYMBOL_ARROW_UP_RIGHT                + SYMBOL_COMPUTER;
//  public  static final String SYMBOL_WEBVIEW             = SYMBOL_INFORMATION                   + SYMBOL_MOBILE;
    public  static final String SYMBOL_BROWSER             = SYMBOL_GLOBE                         + SYMBOL_COMPUTER;
    public  static final String SYMBOL_WEBVIEW             = SYMBOL_GLOBE                         + SYMBOL_MOBILE;
    public  static final String SYMBOL_SENDKEY             = SYMBOL_KEYBOARD + SYMBOL_ARROW_RIGHT + SYMBOL_COMPUTER;
    //}}}
    //}}}
    // SYMBOL_0_TO_50 {{{
    public static final String SYMBOL_0_TO_50[]
        = {   "\u24EA" //  0
            , "\u2460" //  1
            , "\u2461" //  2
            , "\u2462" //  3
            , "\u2463" //  4
            , "\u2464" //  5
            , "\u2465" //  6
            , "\u2466" //  7
            , "\u2467" //  8
            , "\u2468" //  9
            , "\u2469" // 10
            , "\u246A" // 11
            , "\u246B" // 12
            , "\u246C" // 13
            , "\u246D" // 14
            , "\u246E" // 15
            , "\u246F" // 16
            , "\u2470" // 17
            , "\u2471" // 18
            , "\u2472" // 19
            , "\u2473" // 20
            , "\u3251" // 21
            , "\u3252" // 22
            , "\u3253" // 23
            , "\u3254" // 24
            , "\u3255" // 25
            , "\u3256" // 26
            , "\u3257" // 27
            , "\u3258" // 28
            , "\u3259" // 29
            , "\u325A" // 30
            , "\u325B" // 31
            , "\u325C" // 32
            , "\u325D" // 33
            , "\u325E" // 34
            , "\u325F" // 35
            , "\u32B1" // 36
            , "\u32B2" // 37
            , "\u32B3" // 38
            , "\u32B4" // 39
            , "\u32B5" // 40
            , "\u32B6" // 41
            , "\u32B7" // 42
            , "\u32B8" // 43
            , "\u32B9" // 44
            , "\u32BA" // 45
            , "\u32BB" // 46
            , "\u32BC" // 47
            , "\u32BD" // 48
            , "\u32BE" // 49
            , "\u32BF" // 50
        };

    //}}}
    // Get_DIGIT_SYMBOL {{{
    public  static final String SYMBOL_DIGIT_1             = "\u278A";
    public  static final String SYMBOL_DIGIT_2             = "\u278B";
    public  static final String SYMBOL_DIGIT_3             = "\u278C";
    public  static final String SYMBOL_DIGIT_4             = "\u278D";
    public  static final String SYMBOL_DIGIT_5             = "\u278E";
    public  static final String SYMBOL_DIGIT_6             = "\u278F";
    public  static final String SYMBOL_DIGIT_7             = "\u2790";
    public  static final String SYMBOL_DIGIT_8             = "\u2791";
    public  static final String SYMBOL_DIGIT_9             = "\u2792";
    public  static final String SYMBOL_DIGIT_10            = "\u2793";
    public static String Get_DIGIT_SYMBOL(int digit)
    {
        switch( digit ) {
            case  1: return SYMBOL_DIGIT_1;
            case  2: return SYMBOL_DIGIT_2;
            case  3: return SYMBOL_DIGIT_3;
            case  4: return SYMBOL_DIGIT_4;
            case  5: return SYMBOL_DIGIT_5;
            case  6: return SYMBOL_DIGIT_6;
            case  7: return SYMBOL_DIGIT_7;
            case  8: return SYMBOL_DIGIT_8;
            case  9: return SYMBOL_DIGIT_9;
            case 10: return SYMBOL_DIGIT_10;
            default: return "("+digit+")";
        }
    }
    //}}}
    // ParseUnicode {{{
    private static final Pattern UTFPattern = Pattern.compile("\\{*\\\\u[0-9A-Fa-f]{4}\\}*");
    private static final String  UTFCHARS   =                             "0123456789AaBbCcDdEeFf";
    public  static       String  ParseUnicode(String text)
    {
        if(text.equals("")) return "";

        String result = text;

        // UTF PREFIX NORMALIZE .. (u+ U+ 0x) {{{
        String s = result;
        s        = s.replace("u+", "\\u");
        s        = s.replace("U+", "\\u");
        s        = s.replace("0x", "\\u");
        //}}}
        // "\u0041\u0041\u0041\u0041\u0041" {{{
        Matcher matcher = UTFPattern.matcher( s );
        if( matcher.find() )
        {
            try {
                result   = "";
                int   udx  = s.indexOf("\\u");
                while(udx >= 0)
                {
                    // HEAD [{] {{{
                    int end = udx;
                    if((udx  > 0) && (s.charAt(udx-1) == '{'))          // optional (UTF-ENCAPSULATION-OPENING-BRACE)
                        end -= 1;                                       // ...discard brace from header

                    if(end > 0)
                        result += s.substring(0, end);

                    //}}}
                    // REMAINDER UTFCHARS [}] {{{
                    s   = s.substring(udx + 2); // SKIP [\\u]
                    udx = 0;

                    int          max = Math.min(8, s.length());
                    while((udx < max) && (UTFCHARS.indexOf( s.charAt(udx) ) >= 0))
                        ++udx;

                    String utf = s.substring(0, udx);
//SETTINGS*/Settings.MOM(TAG_SETTINGS, "...utf=["+utf+"]");

                    if((udx < s.length()) && (s.charAt(udx) == '}'))    // UTF-ENCAPSULATION-CLOSING-BRACE
                        udx += 1;                                       // ...discard brace from tail

                    //}}}
                    // TAIL {{{
                    if(udx >= s.length()) s = "";                       // END OF STRING
                    else                  s = s.substring(udx);         // SKIP UTF .. and optional (UTF-ENCAPSULATION-OPENING-BRACE)

                    // }}}
                    // UTF {{{
                    int codePoint = Integer.parseInt(utf , 16);
                    if     ( !Character.isValidCodePoint( codePoint ) ) { result += "!V"; } // MIN = 0 .. MAX_CODE_POINT = 0x10FFFF;
                //  else if( !Character.isDefined       ( codePoint ) ) { result += "!D"; } // use "" instead
                    else                                                { result += new String( Character.toChars( codePoint ) ); }

                    // }}}
                    // LOOP ALONG TAIL
                    udx    = s.indexOf("\\u");
                }
                // TAIL UNPARSED REMAINDER
                if(s.length() > 0)
                    result += s;

// {{{
/*
                String[] a = str.split("\\u");
                for(int i=1; i < a.length; i++){
                    int hexVal = Integer.parseInt(a[i], 16);
                    result    += (char)hexVal;
                }
*/
/*
                byte[] b = result.getBytes("UTF-8");
                result   = "getBytes("+ new String(b) +")";
*/
/*
                Matcher matcher = UTFPattern.matcher( result );
                if( matcher.find() )
                {
                    String utfchar = matcher.group();
//SETTINGS//Settings.MON(TAG_SETTINGS, "...UTFPattern.matcher(result).group()=["+ utfchar +"]");
                    byte[] b = utfchar.getBytes("UTF-8");
                    result   = new String(b);
                }
*/
// }}}

            } catch (Exception ex) {
                result = ex.getMessage();
            }
        }
        //}}}

        return result;
    }
    //}}}
//    // Encode_UTF16_tokens {{{
//    public static String Encode_UTF16_tokens(String str)
//    {
///*SETTINGS*/Settings.MOC(TAG_SETTINGS, "Encode_UTF16_tokens:");
///*SETTINGS*/Settings.MOM(TAG_SETTINGS, "FROM: ["+str+"]");
//
//            str
//                = str.replace('\u2022', '*')
//                ;
//
///*SETTINGS*/Settings.MOM(TAG_SETTINGS, "..TO: ["+str+"]");
//        return str;
//    }
//    //}}}

    /** SOFTWARE */
    //{{{

    // FLAGS (global soft directives)
    public  static boolean SCROLLBAR_USE_LAYOUT_PARAMS =  true; // wether to get-set LAYOUT through "LayoutParams" or "field"
    public  static boolean SCROLLBAR_USE_TRANSLATION   =  true; // wether to get-set LAYOUT through setTranslation(X|Y)

    public  static final String EMPTY_STR      = "";
    public  static final String TRACE_OPEN     = " {{{ ("+RTabs.get_APK_VERSION()+")";
    public  static final String TRACE_CLOSE    = " }}}";

    // MONITOR TAGS RTabs .. (eventually add "final" modifier to satisfy instant-run)
    private static final String SYMBOL_EV_1SEP = SYMBOL_LIGHT_SHADE;
    private static final String SYMBOL_EV_2SEP = SYMBOL_LIGHT_SHADE + SYMBOL_LIGHT_SHADE;
    private static final String SYMBOL_EV_3SEP = SYMBOL_LIGHT_SHADE + SYMBOL_LIGHT_SHADE + SYMBOL_LIGHT_SHADE;

    private static final String SYMBOL_EV_IN   = SYMBOL_UR_BLACK_TRIANGLE + SYMBOL_LL_BLACK_TRIANGLE;
    private static final String SYMBOL_EV_OK   = SYMBOL_LR_BLACK_TRIANGLE + SYMBOL_UL_BLACK_TRIANGLE;
    private static final String SYMBOL_EV_CB   = SYMBOL_EV_2SEP           + SYMBOL_FULL_BLOCK;
    private static final String SYMBOL_EV_FL   = SYMBOL_EV_3SEP           + SYMBOL_FULL_BLOCK + SYMBOL_ROCKET;
    private static final String SYMBOL_EV_SC   = SYMBOL_EV_3SEP           + SYMBOL_FULL_BLOCK + SYMBOL_ARROW_TWISTED_RIGHT;

    public  static final String TAG_EV0_RT_DP  = "EV0_RT_DP"+ SYMBOL_NO_ENTRY;
    public  static final String TAG_EV1_RT_IN  = "EV1_RT_IN"+ SYMBOL_EV_IN;
    public  static final String TAG_EV1_RT_OK  = "EV1_RT_OK"+ SYMBOL_EV_OK;
    public  static final String TAG_EV2_RT_CB  = "EV2_RT_CB"+ SYMBOL_EV_CB;
    public  static final String TAG_EV3_RT_SC  = "EV3_RT_SC"+ SYMBOL_EV_SC;
    public  static final String TAG_EV7_RT_FL  = "EV7_RT_FL"+ SYMBOL_EV_FL;

    public  static final String TAG_EV0_WV_DP  = "EV0_WV_DP"+ SYMBOL_NO_ENTRY;
    public  static final String TAG_EV1_WV_IN  = "EV1_WV_IN"+ SYMBOL_EV_IN;
    public  static final String TAG_EV1_WV_OK  = "EV1_WV_OK"+ SYMBOL_EV_OK;
    public  static final String TAG_EV2_WV_CB  = "EV2_WV_CB"+ SYMBOL_EV_CB;
    public  static final String TAG_EV3_WV_SC  = "EV3_WV_SC"+ SYMBOL_EV_SC;
    public  static final String TAG_EV7_WV_FL  = "EV7_WV_FL"+ SYMBOL_EV_FL;

    public  static final String TAG_ACTIVITY   = "ACTIVITY";
    public  static final String TAG_ANIM       = "ANIM";
    public  static final String TAG_BAND       = "BAND";
    public  static final String TAG_CART       = "CART";
    public  static final String TAG_CLAMP      = "CLAMP";
    public  static final String TAG_CLIENT     = "CLIENT";
    public  static final String TAG_CLIPBOARD  = "CLIPBOARD";
    public  static final String TAG_COMM       = "COMM";
    public  static final String TAG_CONNECT    = "CONNECT";
    public  static final String TAG_COOKIE     = "COOKIE";
    public  static final String TAG_DASH       = "DASH";
    public  static final String TAG_DATA       = "DATA";
    public  static final String TAG_DIALOG     = "DIALOG";
    public  static final String TAG_EXPAND     = "EXPAND";
    public  static final String TAG_FS_SEARCH  = "FS_SEARCH";
    public  static final String TAG_FULLSCREEN = "FULLSCREEN";
    public  static final String TAG_GLOW       = "GLOW";
    public  static final String TAG_GUI        = "GUI";
    public  static final String TAG_HANDLE     = "HANDLE";
    public  static final String TAG_HISTORY    = "HISTORY";
    public  static final String TAG_JAVASCRIPT = "JAVASCRIPT";
    public  static final String TAG_KEYBOARD   = "KEYBOARD";
    public  static final String TAG_LOG        = "LOG";
    public  static final String TAG_MAGNET     = "MAGNET";
    public  static final String TAG_MK0_MARK   = "MK0_MARK";
    public  static final String TAG_MK1_COOK   = "MK1_COOK";
    public  static final String TAG_MK2_SYNC   = "MK2_SYNC";
    public  static final String TAG_MK3_PIN    = "MK3_PIN";
    public  static final String TAG_MK4_CB     = "MK4_CB";
    public  static final String TAG_MK5_FLOAT  = "MK5_FLOAT";
    public  static final String TAG_MK6_MAP    = "MK6_MAP";
    public  static final String TAG_MK7_MAGNET = "MK7_MAGNET";
    public  static final String TAG_MOVE       = "MOVE";
    public  static final String TAG_ONFLING    = "ONFLING";
    public  static final String TAG_POLL       = "POLL";
    public  static final String TAG_PROFILE    = "PROFILE";
    public  static final String TAG_PROPERTY   = "PROPERTY";
    public  static final String TAG_READ       = "READ";
    public  static final String TAG_SANDBOX    = "SANDBOX";
    public  static final String TAG_SCALE      = "SCALE";
    public  static final String TAG_SCROLLBAR  = "SCROLLBAR";
    public  static final String TAG_SETTINGS   = "SETTINGS";
    public  static final String TAG_SOUND      = "SOUND";
    public  static final String TAG_SPREAD     = "SPREAD";
    public  static final String TAG_STORAGE    = "STORAGE";
    public  static final String TAG_TAB        = "TAB";
    public  static final String TAG_TABGET     = "TABGET";
    public  static final String TAG_TOOL       = "TOOL";
    public  static final String TAG_TOUCH      = "TOUCH";
    public  static final String TAG_URL        = "URL";
    public  static final String TAG_WEBGROUP   = "WEBGROUP";
    public  static final String TAG_WEBVIEW    = "WEBVIEW";
    public  static final String TAG_WVTOOLS    = "WVTOOLS";

  //public  static final String TAG_MEASURE    = "MEASURE";
  //public  static final String TAG_SCROLL     = "SCROLL";

    public  static final String EMPTY_STRING   = "";

    public  static final String FREE_TAG       = "#";

    // INIT
    // set_RTabs {{{
/*
    public  static RTabs mRTabs;

    public static void set_RTabs(RTabs RTabs_instance)
    {
        mRTabs = RTabs_instance;
        Settings_class_init();
    }
*/
    //}}}

    //}}}

    /** INFO */
    //{{{
    // WEBVIEW {{{
    public  static final String           LOCALHOST_URL = "http://localhost";

    public  static final String FS_TITLE_INFO           = "\n...CURRENT PAGE TITLE"
    /*-----------------------------------------------*/ + "\n...CLICK......: to RELOAD"
    /*-----------------------------------------------*/ + "\n...SWIPE-LEFT.: to SPLIT"
    /*-----------------------------------------------*/ + "\n...SWIPE-RIGHT: to ENLARGE";
    public  static final String FS_BROWSE_TEXT          = "B\nr\no\nw\ns\ne";
    public  static final String FS_BROWSE_INFO          = "\n...open this page in the browser";
    public  static final String FS_SWAP_TEXT            = SYMBOL_LEFT_BLACK_TRIANGLE + SYMBOL_RIGHT_BLACK_TRIANGLE;
/*
    public  static final String FS_SWAP_TEXT
        = SYMBOL_LEFT_BLACK_TRIANGLE  +"\n"
        + SYMBOL_LEFT_BLACK_TRIANGLE  +"\n"
        + SYMBOL_LEFT_BLACK_TRIANGLE  +"\n"
        +"\n"
        + SYMBOL_RIGHT_BLACK_TRIANGLE +"\n"
        + SYMBOL_RIGHT_BLACK_TRIANGLE +"\n"
        + SYMBOL_RIGHT_BLACK_TRIANGLE;
*/

    public  static final String FS_SWAP_INFO            = "\n...swap views";
    public  static final String FS_BOOKMARK_TEXT        = "B\no\no\nk\nm\na\nr\nk";
    public  static final String FS_BOOKMARK_INFO        = "\n...save this page URL\n- in a free TAB\n- of the current profile";
    public  static       String FS_GOBACK_TEXT          = Settings.SYMBOL_PRPREV;
    public  static final String FS_GOBACK_INFO          = "\n...Goes back in the history of this WebView";
    public  static       String FS_GOFORWARD_TEXT       = Settings.SYMBOL_PRNEXT;
    public  static final String FS_GOFORWARD_INFO       = "\n...Goes forward in the history of this WebView";


    public  static       String FS_SEARCH_TEXT_3        = Settings.SYMBOL_WHITE_CIRCLE+" "+Settings.SYMBOL_WHITE_CIRCLE+" "+Settings.SYMBOL_WHITE_CIRCLE; // - - -
    public  static       String FS_SEARCH_TEXT_2        = Settings.SYMBOL_WHITE_CIRCLE+" "+Settings.SYMBOL_WHITE_CIRCLE                                 ; // - -

    public  static       String FS_SEARCH_TEXT_1_2      = Settings.SYMBOL_BLACK_CIRCLE+" "+Settings.SYMBOL_WHITE_CIRCLE                                 ; // O -
    public  static       String FS_SEARCH_TEXT_2_2      = Settings.SYMBOL_WHITE_CIRCLE+" "+Settings.SYMBOL_BLACK_CIRCLE                                 ; // - O

    public  static       String FS_SEARCH_TEXT_1_3      = Settings.SYMBOL_BLACK_CIRCLE+" "+Settings.SYMBOL_WHITE_CIRCLE+" "+Settings.SYMBOL_WHITE_CIRCLE; // O - -
    public  static       String FS_SEARCH_TEXT_2_3      = Settings.SYMBOL_WHITE_CIRCLE+" "+Settings.SYMBOL_BLACK_CIRCLE+" "+Settings.SYMBOL_WHITE_CIRCLE; // - O -
    public  static       String FS_SEARCH_TEXT_3_3      = Settings.SYMBOL_WHITE_CIRCLE+" "+Settings.SYMBOL_WHITE_CIRCLE+" "+Settings.SYMBOL_BLACK_CIRCLE; // - - O


    public  static final String FS_SEARCH_INFO          = "\n...floating search tool";

    public  static       String FS_SCROLL_TEXT          = "";//Settings.SYMBOL_FULL_BLOCK;
    public  static final String FS_SCROLL_INFO          = "\n...WebView scroll handle";

    public  static       String FS_SELECT_TEXT          = Settings.SYMBOL_MAGNIFY_RIGHT;
    public  static final String FS_SELECT_INFO          = "\n...WebView select tool";

    public  static final float  NB_TO_SLEEP_OPACITY     = 0.5f;
    public  static final float  NB_WAKE_UP_OPACITY      = 0.8f;
    public  static final float  WV_ADJUSTING_SB_OPACITY = 0.6f;
    public  static final float  FS_SCROLL_OPACITY       = 1.0f;
    public  static final int    FS_SELECT_BORDER        = 16;

//  public  static final int    FS_SELECT_BACKCOLOR     = Color.WHITE;
//  public  static final int    FS_SELECT_TEXTCOLOR     = Color.RED;
    // }}}
    // HIST {{{
    public  static final String BACK_NB_INFO            = "\n...Profile BACK navigation\n...(alphabetic order)";
    public  static final String FORE_NB_INFO            = "\n...Profile FORWARD navigation\n...(alphabetic order)";
    public  static final String PROF_NB_INFO            = "\n...Profile LIST";

    // }}}
    // CART {{{
    public  static final String  DEL_NB_INFO            = "\n...EXTRACT FROM CART";
    public  static final String  SEE_NB_INFO            = "\n...CURRENT CART CONTENT\n- RED = ADD to cart\n- GREEN = EXTRACT from cart";
    public  static final String  ADD_NB_INFO            = "\n...ADD TO CART";
    public  static final String  END_NB_INFO            = "\n...EXIT CART OPERATIONS";

    // }}}
    //}}}

    /** APP */
    //{{{
    // {{{
    public  static final String         APP_NAME	        = "RTabs";
    public  static final String         PERSONAL_NOTE_FILENAME  = "PersonalNote.txt";
    public  static       String         SETTINGS_FILENAME       = "RTabsSettings.txt";
    public  static final String         COOKIES_FILENAME        = "RTabsCookies.txt";

    public  static String               DEVICE	                = "";
    public  static boolean              FREEZED	                = false;
    public  static boolean              OFFLINE	                = false;

  //public  static boolean              WV_TOOLS_VIS            = false; // TODO (make it a session-saved param)
    public  static boolean              WV_TOOL_MISC_VIS        = false;
    public  static boolean              WV_TOOL_JSNP_VIS        = false;

    // }}}
    // get_APP_NAME {{{
    public static String get_APP_NAME()
    {
        if     (is_ktabs() ) return "KTabs";
        else if(is_ntabs() ) return "NTabs";
        else                 return APP_NAME;
    }
    //}}}
    // is_err_redirection_required {{{
    public  static boolean is_err_redirection_required()
    {
        boolean diag
            =  get_APP_NAME().equals("NTabs")
            || get_APP_NAME().equals("KTabs")
            || get_APP_NAME().equals(APP_NAME)  // FIXME
            ;
        return diag;
    }
    //}}}
    // is_dock_required {{{
    public  static boolean is_dock_required()
    {
        boolean diag
            =  get_APP_NAME().equals("NTabs")
            || get_APP_NAME().equals("KTabs")
            || get_APP_NAME().equals(APP_NAME)  // FIXME
            ;
        return diag;
    }
    //}}}
    public  static boolean is_rtabs()    { return RTabs.activity.getPackageName().toLowerCase().equals("ivanwfr.rtabs"); }
    private static boolean is_ntabs()    { return RTabs.activity.getPackageName().toLowerCase().equals("ivanwfr.ntabs"); }
    private static boolean is_ktabs()    { return RTabs.activity.getPackageName().toLowerCase().equals("ivanwfr.ktabs"); }
    //}}}
    // log_state {{{
    public static String log_state()
    {
        Log_init("");
        //...........................=
        Log_append("          FREEZED=["+ FREEZED +"]");
        Log_append("          OFFLINE=["+ OFFLINE +"]");
        return Log_toString();
    }
    //}}}

    /** FOLDERS */
    //{{{
    // Check_Profiles_dir {{{
    public  static void Check_Profiles_dir(boolean for_logging)
    {
        Get_storage_app_dir(Settings.EXTERNAL_FILES_DIR, "Profiles$", 0, for_logging);
        Get_storage_app_dir(Settings.EMULATED_FILES_DIR, "Profiles$", 0, for_logging);
        Get_storage_app_dir(       _Get_Downloads_dir(), "Profiles$", 0, for_logging);
        Get_storage_app_dir(        Get_App_dir      (), "Profiles$", 0, for_logging);

        // dir=[/storage/9016-4EF8/Android/data/ivanwfr.rtabs/files ]
        // dir=[/storage/emulated/0/Android/data/ivanwfr.rtabs/files]
        // dir=[/storage/emulated/0/Download                        ]
        // dir=[/data/user/0/ivanwfr.rtabs/files                    ]
    }
    //}}}
    // Get_Profiles_dir .. (download .. fallback to app data) {{{
    private static File     Profiles_dir = null;
    public  static File Get_Profiles_dir()
    {
        if(Profiles_dir != null) return Profiles_dir; // GOT IT ALREADY
//*SETTINGS*/Check_Profiles_dir(false);//TAG_SETTINGS
        // 1/4 [EXTERNAL_FILES_DIR] {{{
        if((Profiles_dir == null) && (Settings.EXTERNAL_FILES_DIR != null))
        {
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "SEARCHING SDCARD EXTERNAL PROFILE DIR");
            Profiles_dir      = Get_storage_app_dir(Settings.EXTERNAL_FILES_DIR, "Profiles$", 0, false);
            if(Profiles_dir  == null)
                Profiles_dir  = Profiles_dir_mkdirs( Settings.EXTERNAL_FILES_DIR );
        }
        //}}}
        // 2/4 [EMULATED_FILES_DIR] {{{
        if((Profiles_dir == null) && (Settings.EMULATED_FILES_DIR != null))
        {
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "SEARCHING EMULATED EXTERNAL PROFILE DIR");
            Profiles_dir      = Get_storage_app_dir(Settings.EMULATED_FILES_DIR, "Profiles$", 0, false);
            if(Profiles_dir  == null)
                Profiles_dir  = Profiles_dir_mkdirs( Settings.EMULATED_FILES_DIR );
        }
        //}}}
        // 3/4 [_Get_Downloads_dir] {{{
        if(Profiles_dir == null)
        {
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "SEARCHING DOWNLOAD DIR");
            File download_dir = _Get_Downloads_dir();
            Profiles_dir      = Get_storage_app_dir(download_dir, "Profiles$", 0, false);
            if(Profiles_dir  == null)
                Profiles_dir  = Profiles_dir_mkdirs( download_dir );
        }
        //}}}
        // 4/4 [Get_App_dir] {{{
        if(Profiles_dir == null)
        {
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "SEARCHING APP DIR");
            File app_dir      = Get_App_dir();
            Profiles_dir      = Get_storage_app_dir(app_dir, "Profiles$", 0, false);
            if(Profiles_dir  == null)
                Profiles_dir  = Profiles_dir_mkdirs( app_dir );
        }
        //}}}
//*SETTINGS*/System.err.println("Get_Profiles_dir: ...return Profiles_dir=["+ Profiles_dir +"].isDirectory=["+ Profiles_dir.isDirectory() +"]");//TAG_SETTINGS
        return Profiles_dir;
    }
    //}}}
    // Profiles_dir_mkdirs {{{
    private static File Profiles_dir_mkdirs(File parentDir)
    {
//*SETTINGS*/Settings.MON(TAG_SETTINGS, "Profiles_dir_mkdirs", "CREATING PROFILES SUB DIR in ["+parentDir+"]");

//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "...parentDir.canExecute()  ["+ parentDir.canExecute()  +"]");
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "...parentDir.canRead()     ["+ parentDir.canRead()     +"]");
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "...parentDir.canWrite()    ["+ parentDir.canWrite()    +"]");
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "...parentDir.exists()      ["+ parentDir.exists()      +"]");
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "...parentDir.isAbsolute()  ["+ parentDir.isAbsolute()  +"]");
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "...parentDir.isDirectory() ["+ parentDir.isDirectory() +"]");
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "...parentDir.isHidden()    ["+ parentDir.isHidden()    +"]");

        File dir = new File(parentDir.getPath()+"/Profiles");
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, ".........dir.exists()      ["+       dir.exists()      +"]");
        if( !dir.exists() ) dir = null;

        return dir;
    }
    //}}}
    // _Get_Settings_dir {{{
    @SuppressWarnings("CanBeFinal")
    private static File Settings_dir = null;
    private static File _Get_Settings_dir()
    {
        // GOT IT ALREADY
        if(Settings_dir != null) return Settings_dir;

        // USE PROFILES DIR
        File Settings_dir = Get_Profiles_dir();

//*SETTINGS*/Settings.MON(TAG_SETTINGS, "_Get_Settings_dir", "Settings_dir=["+ Settings_dir +"].exists=["+ Settings_dir.exists() +"]");
        return Settings_dir;
    }
    //}}}
    // _Get_Downloads_dir {{{
    private static File _Get_Downloads_dir()
    {
        String       type = Environment.DIRECTORY_DOWNLOADS;
        File download_dir = Environment.getExternalStoragePublicDirectory( type ); // (191028)
      //File download_dir = Environment.getExter.buildExternalStoragePublicDirs(type)[0];

//*SETTINGS*/Settings.MOC(TAG_SETTINGS, "_Get_Downloads_dir: Environment.getExternalStoragePublicDirectory("+ type +")=["+ download_dir +"].exists=["+ download_dir.exists() +"]");
        return download_dir;
    }
    //}}}
    // Get_App_dir {{{
    public static File Get_App_dir()
    {
    //  String           dirName = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOCUMENTS );
    //  return new File( dirName, Settings.APP_NAME+"/Pofiles");
    //  return RTabs.activity.getDir("Settings", RTabs.activity.MODE_WORLD_WRITEABLE );
        File files_dir = RTabs.activity.getFilesDir();
//*SETTINGS*/Settings.MON(TAG_SETTINGS, "Get_App_dir", "files_dir=["+ files_dir +"].exists=["+ files_dir.exists() +"]");
        return files_dir;
    }
    //}}}
    // _mk_sub_dir {{{
    private File _mk_sub_dir(File dir, String sub_dir_name)
    {
        File sub_dir = new File(dir.getPath() +"/"+ sub_dir_name);
        //noinspection ResultOfMethodCallIgnored
        sub_dir.mkdirs();

        if(D) log("RTabs_dir.mkdirs()=["+ sub_dir +"].exists=["+ sub_dir.exists() +"]");
        return sub_dir;
    }
    //}}}
    // Get_storage_app_dir {{{
    private static File Get_storage_app_dir(File dir, String pattern, int max_subdir_level, boolean for_logging)
    {
        File first_storage_app_dir = null;

        if( dir.exists() )
        {
            try {
                List<File>  fList = Settings.dir_list(dir, max_subdir_level, pattern);
                File[]      files =  fList.toArray(new File[fList.size()]);
                Arrays.sort(files);

                if(files.length > 0)
                    first_storage_app_dir = files[0];

                if(for_logging) {
                    String        dp = dir.getPath();
                //  int           dl = dp.length();
                    for(int i=0;   i < files.length; i++) {
                        String    fp = files[i].getPath();//.substring(dl+1);
                        String     q = dp +"["+ pattern +"] ["+i+"]";
                        String     a = "("+(files[i].isDirectory() ? "DIR " : "file") + ") ["+fp+"]";
                        Log_append(String.format("%64s == %s", q, a));
                    }
                }
            }
            catch(Exception ex) {
                System.err.println("*** Get_storage_app_dir.dir_list("+dir+","+ max_subdir_level +","+ pattern +"): Exception:\n"+ ex.toString());
                ex.printStackTrace();
            }
        }
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "Get_storage_app_dir: dir=["+String.format("%-64s",dir)+"] level=["+max_subdir_level+"] pattern=["+String.format("%-32s",pattern)+"] ...return ["+ first_storage_app_dir +"]");
        return first_storage_app_dir;
    }
    //}}}

    // log_folders {{{
    @SuppressLint("SdCardPath")
    public static String log_folders()
    {
/*
:!start explorer "http://developer.android.com/reference/android/os/Environment.html#getDataDirectory()"
*/

        Log_init("");

//*SETTINGS*/Check_Profiles_dir(true); // for_logging

        String dd = Environment.DIRECTORY_DOWNLOADS;
        String q; // .. (question)
        String a; // .. (answer)

        String    s = Environment.getExternalStorageState()                  ; q = "Environment.getExternalStorageState()"                                ; a = "["+ s +"]"                                                             ; Log_append(String.format("%64s == %s", q, a));
        File      d = RTabs.activity.getFilesDir()                                 ; q = "RTabs.activity.getFilesDir()"                                               ; a = "["+ d +"].exists=["+ d_exists(d) +"]"                                  ; Log_append(String.format("%64s == %s", q, a));
        /**/      d = Environment.getExternalStorageDirectory(              ); q = "Environment.getExternalStorageDirectory()"                            ; a = "["+ d +"].exists=["+ d_exists(d) +"] path=["+ d.getAbsolutePath() +"]" ; Log_append(String.format("%64s == %s", q, a));
        /**/      d = Environment.getExternalStoragePublicDirectory( dd     ); q = "Environment.getExternalStoragePublicDirectory("+ dd +")"              ; a = "["+ d +"].exists=["+ d_exists(d) +"]"                                  ; Log_append(String.format("%64s == %s", q, a));
        /**/      d = RTabs.activity.getDir             (dd, Context.MODE_PRIVATE ); q = "RTabs.activity.getDir("+                               dd +", MODE_PRIVATE)"; a = "["+ d +"].exists=["+ d_exists(d) +"]"                                  ; Log_append(String.format("%64s == %s", q, a));
        /**/      d = RTabs.activity.getExternalFilesDir(dd                       ); q = "RTabs.activity.getExternalFilesDir("+                  dd +")"              ; a = "["+ d +"].exists=["+ d_exists(d) +"] .. (deleted on app uninstall)"    ; Log_append(String.format("%64s == %s", q, a));

        File[] dirs = RTabs.activity.getExternalFilesDirs(null); for(int i=0; i<dirs.length; ++i) { d = dirs[i]; q = "getExternalFilesDirs(null): ["+i+"]"      ; a = "["+ d +"].exists=["+ d_exists(d) +"]"                                  ; Log_append(String.format("%64s == %s", q, a)); }
        /**/   dirs = RTabs.activity.getObbDirs()              ; for(int i=0; i<dirs.length; ++i) { d = dirs[i]; q =               "getObbDirs(): ["+i+"]"      ; a = "["+ d +"].exists=["+ d_exists(d) +"]"                                  ; Log_append(String.format("%64s == %s", q, a)); }
        /**/   dirs = RTabs.activity.getExternalCacheDirs()    ; for(int i=0; i<dirs.length; ++i) { d = dirs[i]; q =     "getExternalCacheDirs(): ["+i+"]"      ; a = "["+ d +"].exists=["+ d_exists(d) +"]"                                  ; Log_append(String.format("%64s == %s", q, a)); }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        /**/   dirs = RTabs.activity.getExternalMediaDirs()    ; for(int i=0; i<dirs.length; ++i) { d = dirs[i]; q =     "getExternalMediaDirs(): ["+i+"]"      ; a = "["+ d +"].exists=["+ d_exists(d) +"]"                                  ; Log_append(String.format("%64s == %s", q, a)); }

        /**/      d = Get_Profiles_dir()                              ; q = "Profiles and Personal Note are saved in "                                    ; a = "["+ d +"].exists=["+ d_exists(d) +"]"                                  ; Log_append(String.format("%64s == %s", q, a));
        File      f = new File(_Get_Settings_dir(), SETTINGS_FILENAME); q = "Settings are saved in "                                                      ; a = "["+ f +"].exists=["+ d_exists(d) +"]"                                  ; Log_append(String.format("%64s == %s", q, a));


        /**/      s = "/sdcard"         ; d = new File(s)             ; q = s                                                                             ; a = "["+ d +"].exists=["+ d_exists(d) +"]"                                  ; Log_append(String.format("%64s == %s", q, a));
        /**/      s = "/sdcard/download"; d = new File(s)             ; q = s                                                                             ; a = "["+ d +"].exists=["+ d_exists(d) +"]"                                  ; Log_append(String.format("%64s == %s", q, a));
        /**/      s = "/storage"        ; d = new File(s)             ; q = s                                                                             ; a = "["+ d +"].exists=["+ d_exists(d) +"]"                                  ; Log_append(String.format("%64s == %s", q, a));

        return Log_toString();
    }
    private static boolean d_exists(File d) { try { return d.exists(); } catch(Exception ignored) { return false; } }
    //}}}
    //}}}

    /** COMM */
    //{{{
    // {{{
    // KEYWORDS
    private static final String    TESTS_DASH   = "TESTS_DASH";
    public  static final String    ACK          = "ACK";
    public  static       String    OOB_CMD      = "";

    // IP
    public static  final String  DEFAULT_SERVER_IP_24        = "10.0.0.1";       // 24-bit block
    public static  final String  DEFAULT_SERVER_IP_20        = "172.16.0.1";     // 20-bit block
    public static  final String  DEFAULT_SERVER_IP_16        = "192.168.0.2";    // 16-bit block
    public static        String          SERVER_IP           = DEFAULT_SERVER_IP_16;

    // PORT
    private static final int     DEFAULT_SERVERPORT          =  4001;
    public static        int             SERVER_PORT         = DEFAULT_SERVERPORT;

    // PASS
    private static final String  DEFAULT_SERVER_PASS         = "pass";
    public static        String          SERVER_PASS         = DEFAULT_SERVER_PASS;

    // SERVER
    public static        String          SERVER_MAC          = "";
    public static        String          SERVER_SUBNET       = "";

    // TOOL UNICODE
    public static        String          UNICODE_SHEET       = "";
    public static        String          UNICODE_PRESET      = "";

    // WEB
    public static        String          WEBGROUPS           = "";

    // TOOL URL
    public static  final String  TOOL_URL_ACTION_BROWSER     = "BROWSER";
    public static  final String  TOOL_URL_ACTION_SENDKEY     = "SENDKEY";
    public static  final String  TOOL_URL_ACTION_WEBVIEW     = "WEBVIEW";
    public static        String  TOOL_URL_ACTION             = TOOL_URL_ACTION_WEBVIEW;

    // Volatile
    public static        String          ServerID            = "";

    // }}}
    // selectNextPortInRange {{{
    public static final int SERVER_PORT_RANGE = 10;

    public static void selectNextPortInRange()
    {
        SERVER_PORT = ((SERVER_PORT / SERVER_PORT_RANGE) * SERVER_PORT_RANGE) + (SERVER_PORT+1) % SERVER_PORT_RANGE;
        String  msg = SYMBOL_NO_ENTRY +" selectNextPortInRange: switching to SERVER_PORT["+ SERVER_PORT +"] "+ SYMBOL_NO_ENTRY;
        if(D) log( msg );
    }
    //}}}
    // Send_SIGNIN {{{
    public static void Send_SIGNIN(String cmdLine, String caller)
    {
        CmdParser.parse( cmdLine );

        String ip   = CmdParser.getArgValue("IP"      ,    SERVER_IP  );
        String pass = CmdParser.getArgValue("PASSWORD",    SERVER_PASS);
        String port = CmdParser.getArgValue("PORT"    , ""+SERVER_PORT);

        try { port = ""+Integer.parseInt( port ); } catch(Exception ignored) {}

        Settings.SERVER_IP   = ip;
        Settings.SERVER_PORT = Integer.parseInt( port );
        Settings.SERVER_PASS = pass;
    }
    //}}}
    //}}}
    // log_server {{{
    public static String log_server()
    {
        Log_init("");
        //...........................=
        Log_append("      SERVER_IP..=["+ SERVER_IP   +"]");
        Log_append("      SERVER_PORT=["+ SERVER_PORT +"]");
        Log_append("      SERVER_PASS=["+ SERVER_PASS +"]");
        Log_append("      SERVER_MAC.=["+ SERVER_MAC  +"]");
        Log_append("      ServerID...=["+ ServerID    +"]");
        return Log_toString();
    }
    //}}}

    /** SOUND */
    //{{{

    // FOLDER
    public static       String SOUND_DIR            = null;

    // NAME_ID and FILES
    public static final String SOUND_KEYB_Delete    = "Delete.ogg";
    public static final String SOUND_KEYB_Keypress  = "Keypress.ogg";
    public static final String SOUND_KEYB_Return    = "Return.ogg";
    public static final String SOUND_KEYB_Spacebar  = "Spacebar.ogg";
    public static final String SOUND_KEYB_step      = "step.ogg";

    public static final String SOUND_BELL_airplane  = "bell/airplane.ogg";
    public static final String SOUND_BELL_bronze    = "bell/bronze.ogg";
    public static final String SOUND_BELL_desk      = "bell/desk.ogg";
    public static final String SOUND_BELL_ding      = "bell/ding.ogg";
    public static final String SOUND_BELL_dong      = "bell/dong.ogg";
    public static final String SOUND_BELL_door      = "bell/door.ogg";
    public static final String SOUND_BELL_goblet    = "bell/goblet.ogg";
    public static final String SOUND_BELL_reception = "bell/reception.ogg";
    public static final String SOUND_BELL_ship      = "bell/ship.ogg";
    public static final String SOUND_BELL_triangle  = "bell/triangle.ogg";

    public static       String SOUND_CLICK          = SOUND_KEYB_Delete;
    public static       String SOUND_DING           = SOUND_BELL_triangle;

    public static final float  SOUND_ALARM_VOLUME   = 0.2f;
    public static final String SOUND_ALARM_0        = SOUND_BELL_airplane;
    public static final String SOUND_ALARM_1        = SOUND_BELL_ship;
    public static final String SOUND_ALARM_2        = SOUND_BELL_goblet;
    public static final String SOUND_ALARM_3        = SOUND_BELL_ding;
    public static final String SOUND_ALARM_4        = SOUND_BELL_dong;
    public static final String SOUND_ALARM_5        = SOUND_BELL_door;

    // NAME_ID and FILES COLLECTION
    public static final String[] SOUND_FILES =
    {
        /*.*/ SOUND_KEYB_Delete
            , SOUND_KEYB_Keypress
            , SOUND_KEYB_Return
            , SOUND_KEYB_Spacebar
            , SOUND_KEYB_step
            , SOUND_BELL_airplane
            , SOUND_BELL_bronze
            , SOUND_BELL_desk
            , SOUND_BELL_goblet
            , SOUND_BELL_ding
            , SOUND_BELL_dong
            , SOUND_BELL_reception
            , SOUND_BELL_door
            , SOUND_BELL_ship
            , SOUND_BELL_triangle
    };

    //}}}
    // log_sounds {{{
    public static String log_sounds()
    {
        Log_init("");
        //...........................=
        Log_append("      SOUND_CLICK=["+ SOUND_CLICK       +"]");
        Log_append("      SOUND_CLICK=["+ SOUND_DING        +"]");
        return Log_toString();
    }
    //}}}

    /** COLORS */
    //{{{
    // COLORS {{{
/* // doc {{{
:!start explorer "https://en.wikipedia.org/wiki/List_of_colors_(compact)"
:!start explorer "http://www.keller.com/html-quickref/4a.html"
*/ // }}}
    public static final int[] COLORS_W8 = { // {{{
        Color.parseColor("#F0A30A"),
        Color.parseColor("#82592C"),
        Color.parseColor("#0050EF"),
        Color.parseColor("#A20025"),
        Color.parseColor("#1BA1E2"),
        Color.parseColor("#D80073"),
        Color.parseColor("#A3C300"),
        Color.parseColor("#6A00FF"),
        Color.parseColor("#5FA917"),
        Color.parseColor("#008A00"),
        Color.parseColor("#765F89"),
        Color.parseColor("#6C8764"),
        Color.parseColor("#FA6800"),
        Color.parseColor("#F471D0"),
        Color.parseColor("#E51300"),
        Color.parseColor("#7A3B3F"),
        Color.parseColor("#647687"),
        Color.parseColor("#00ABA9"),
        Color.parseColor("#AA00FF"),
        Color.parseColor("#D7C000")
    };
    // }}}
    public static final int[] COLORS_SCREEN_TILE = { // {{{
        Color.parseColor("#F0A30A"),
        Color.parseColor("#543A24"),
        Color.parseColor("#61292B"),
        Color.parseColor("#662C58"),
        Color.parseColor("#4C2C66"),
        Color.parseColor("#423173"),
        Color.parseColor("#2C4566"),
        Color.parseColor("#306772"),
        Color.parseColor("#2D652B"),
        Color.parseColor("#3A9548"),
        Color.parseColor("#C27D4F"),
        Color.parseColor("#AA4344"),
        Color.parseColor("#AA4379"),
        Color.parseColor("#7F6E94"),
        Color.parseColor("#6E7E94"),
        Color.parseColor("#6BA5E7"),
        Color.parseColor("#439D9A"),
        Color.parseColor("#94BD4A"),
        Color.parseColor("#CEA539"),
        Color.parseColor("#E773BD")
    };
    // }}}
    public static final int[] COLORS_TILE = { // {{{
        Color.parseColor("#F3B200"),
        Color.parseColor("#77B900"),
        Color.parseColor("#2572EB"),
        Color.parseColor("#AD103C"),
        Color.parseColor("#632F00"),
        Color.parseColor("#B01E00"),
        Color.parseColor("#C1004F"),
        Color.parseColor("#7200AC"),
        Color.parseColor("#4617B4"),
        Color.parseColor("#006AC1"),
        Color.parseColor("#008287"),
        Color.parseColor("#199900"),
        Color.parseColor("#00C13F"),
        Color.parseColor("#FF981D"),
        Color.parseColor("#FF2E12"),
        Color.parseColor("#FF1D77"),
        Color.parseColor("#AA40FF"),
        Color.parseColor("#1FAEFF"),
        Color.parseColor("#56C5FF"),
        Color.parseColor("#00D8CC"),
        Color.parseColor("#91D100"),
        Color.parseColor("#E1B700"),
        Color.parseColor("#FF76BC"),
        Color.parseColor("#00A3A3"),
        Color.parseColor("#FE7C22")
    };
    // }}}
    public static final int[] COLORS_ECC = { // {{{
        Color.parseColor("#ff000000"), //  0
        Color.parseColor("#ff964B00"), //  1
        Color.parseColor("#ffFF0000"), //  2
        Color.parseColor("#ffFFA500"), //  3
        Color.parseColor("#ffFFFF00"), //  4
        Color.parseColor("#ff9ACD32"), //  5
        Color.parseColor("#ff6495ED"), //  6
        Color.parseColor("#ffEE82EE"), //  7
        Color.parseColor("#ffA0A0A0"), //  8
        Color.parseColor("#ffFFFFFF"), //  9
//      Color.parseColor("#ffCFB53B"), // 10
//      Color.parseColor("#ffC0C0C0")  // 11
    };
    // }}}
    // CUSTOM COLORS  {{{
    public  static final int COLOR_BLUE          = Color.parseColor("#ff0000ff");
    public  static final int COLOR_BROWN         = Color.parseColor("#ff964b00");
    public  static final int COLOR_DARK_GREY     = Color.parseColor("#ff666677");
    public  static final int COLOR_FIREBRICK     = Color.parseColor("#ffb22222");
    public  static final int COLOR_FORESTGREEN   = Color.parseColor("#ff228b22");
    public  static final int COLOR_GOLD          = Color.parseColor("#ffffd700");
    public  static final int COLOR_IMPERIAL_BLUE = Color.parseColor("#ff6e6ef9");
    public  static final int COLOR_LIGHT_GREY    = Color.parseColor("#ffccccdd");
    public  static final int COLOR_ORANGE        = Color.parseColor("#ffffa500");
    public  static final int COLOR_CARROT_ORANGE = Color.parseColor("#ffe9692c");
    public  static final int COLOR_INDIGO        = Color.parseColor("#ff4b0082");
    public  static final int COLOR_NAVY          = Color.parseColor("#ff000080");

    // Google, #008744,#0057e7,#d62d20,#ffa700,#ffffff
    // .......  GREEN   BLUE    RED     ORANGE  WHITE
    public  static final int GOOGLE_GREEN       = Color.parseColor("#ff008744");
    public  static final int GOOGLE_BLUE        = Color.parseColor("#ff0057e7");
    public  static final int GOOGLE_RED         = Color.parseColor("#ffd62d20");
    public  static final int GOOGLE_ORANGE      = Color.parseColor("#ffffa700");
    public  static final int GOOGLE_WHITE       = Color.parseColor("#ffffffff");

    // FIRE, #fdcf58,#757676,#f27d0c,#800909,#f07f13
    // .....  YELLOW  GRAY    ORANGE  DKRED   ORANGE
    private static final int DOCK_GRAY_P = Color.parseColor("#ffeeeeee");
    private static final int DOCK_GRAY_S = Color.parseColor("#ff757676");
    private static final int DOCK_ORANGE3= Color.parseColor("#fff27d0c");
    private static final int DOCK_DKRED  = Color.parseColor("#ff800909");
    private static final int DOCK_ORANGE5= Color.parseColor("#fff07f13");

    // }}}
    // COLOR ARRAYS -------------------------- PRIMARY     , SECONDARY  , ACCENT             , BG_1           , BG_2 {{{
    public  static final int[] COLORS_PROF = { Color.WHITE , GOOGLE_BLUE, GOOGLE_ORANGE      , COLOR_NAVY     , COLOR_INDIGO      };
//  public  static final int[] COLORS_CTRL = { Color.WHITE , Color.BLACK, COLOR_CARROT_ORANGE, COLOR_NAVY     , COLOR_INDIGO      };
    public  static final int[] COLORS_DFLT = { Color.YELLOW, COLOR_BROWN, Color.BLUE         , COLOR_FIREBRICK, COLOR_FIREBRICK   };
//  public  static final int[] COLORS_DOCK = { DOCK_GRAY_P , DOCK_GRAY_S, DOCK_ORANGE3       , DOCK_DKRED     , DOCK_ORANGE5      };
    public  static final int[] COLORS_DOCK = COLORS_ECC;

    public  static final int[] COLORS_CTRL = COLORS_W8;

    public  static final int BG_COLOR_DOCK         = COLORS_CTRL[0];
    public  static final int BG_COLOR_DOCK_VISITED = COLORS_CTRL[2];
    public  static final int BG_COLOR_DOCK_INDEX   = Color.RED;
    public  static final int BG_COLOR_DOCK_CURRENT = Color.DKGRAY;
    public  static final int BG_COLOR_PROF         = COLORS_CTRL[2];
    public  static final int BG_COLOR_CTRL         = COLORS_CTRL[3];
    public  static final int BG_COLOR_GUI          = COLORS_CTRL[4];
    public  static final int BG_COLOR_INFO         = COLORS_CTRL[5];
    public  static final int BG_COLOR_LOG          = COLORS_CTRL[6];
    public  static final int BG_COLOR_SERVER       = COLORS_CTRL[7];
    public  static final int BG_COLOR_WEB          = COLORS_CTRL[8];
/*
    public  static final int FG_COLOR_CTRL         = COLORS_TILE[0];
    public  static final int FG_COLOR_DOCK         = COLORS_TILE[1];
    public  static final int FG_COLOR_PROF         = COLORS_TILE[3];
*/
    public  static final int FG_COLOR_DOCK_NEW     = COLOR_FIREBRICK;
    public  static final int FG_COLOR_PROF_NEW     = COLOR_FIREBRICK;

    //}}}
    //}}}

    public  static final int WV_TOOL_ACTIVITY_COLOR = Color.parseColor("#602020"); // DARK RED
    public  static final int WV_TOOL_SETTINGS_COLOR = Color.parseColor("#204020"); // DARK GREEN
    public  static final int WV_TOOL_PROFILES_COLOR = Color.parseColor("#202060"); // DARK BLUE

    // BG_COLORS {{{
    public  static final int BG_ALPHA_BRIGHT           =                  0xFFFFFFFF  ; // vvvv
    public  static final int BG_ALPHA_DARK             =                  0x2FFFFF88  ; // 0010 + dimm the nasty blue
    public  static final int DCK_HANDLE_COLOR          = Color.parseColor("#604400FF"); // 0*10

    public  static       int BG_ALPHA                  = BG_ALPHA_BRIGHT;

    public  static       int BAND_HIDE_WIDTH           = 10;
    public  static     float MARK_SCALE_GROW           = 3f;
    public  static       int MARK_EDIT_DELAY           = 3000;
    public  static       int BG_VIEW_DEFAULT           = Color.WHITE;

    public  static final int BG_VIEW_DIMMED            = Color.DKGRAY;
    public  static final int BG_VIEW_LOGGING           = Color.LTGRAY;
    public  static final int BG_VIEW_NOT_CONNECTED     = Color.parseColor("#44ff0000");
    public  static final int BG_VIEW_OPACITY_BELOW_95  = Color.TRANSPARENT;
    public  static final int BG_VIEW_RESCALING         = Color.parseColor("#88aaaaff");
    public  static final int BG_VIEW_TABS_DIMMED       = Color.parseColor("#22aaaaff");
    public  static final int BG_VIEW_ZOOMED_OUT        = Color.parseColor("#88222222");

    public  static String Get_bg_color_name(int color)
    {
         if     (color == BG_VIEW_DEFAULT          ) return "BG_VIEW_DEFAULT";
         else if(color == BG_VIEW_DIMMED           ) return "BG_VIEW_DIMMED";
         else if(color == BG_VIEW_LOGGING          ) return "BG_VIEW_LOGGING";
         else if(color == BG_VIEW_NOT_CONNECTED    ) return "BG_VIEW_NOT_CONNECTED";
         else if(color == BG_VIEW_OPACITY_BELOW_95 ) return "BG_VIEW_OPACITY_BELOW_95";
         else if(color == BG_VIEW_RESCALING        ) return "BG_VIEW_RESCALING";
         else if(color == BG_VIEW_TABS_DIMMED      ) return "BG_VIEW_TABS_DIMMED";
         else if(color == BG_VIEW_ZOOMED_OUT       ) return "BG_VIEW_ZOOMED_OUT";
         else                                        return "<unknown_color>";
    }

    //}}}
    // PALETTE {{{
    public  static String    PALETTE   = "";
    public  static int	     OPACITY   = 100;
    public  static int       MAXCOLORS =  0;
    public  static int       PALCOLORS =  0;
    public  static int       RESCOLORS =  0;
    // get_palette_description {{{
    public static String get_palette_description()
    {
        return         SYMBOL_PALETTE
            +          PALETTE
            +        ((OPACITY < 100) ? " "+ SYMBOL_ALPHA + OPACITY : "")
            +"\nUse" + RESCOLORS
            + " max" + MAXCOLORS
            +   " #" + PALCOLORS
            ;
    }
    //}}}
    //}}}
    //}}}
    // log_palette {{{
    public static String log_palette()
    {
        Log_append("get_HANDLE_HEIGHT=["+ get_HANDLE_HEIGHT() +"]");
        return     "  Working_profile=["+ Working_profile +"]\n"
            +      "          PALETTE=["+ PALETTE         +"]\n"
            +      "          OPACITY=["+ OPACITY         +"]\n"
            +      "        RESCOLORS=["+ RESCOLORS       +"] (reserved for TABS with a set [color] attribute)\n"
            +      "        MAXCOLORS=["+ MAXCOLORS       +"] (current user cap'ed number of colors)\n"
            +      "        PALCOLORS=["+ PALCOLORS       +"] (current palette defined colors)"
            ;
    }
    //}}}

    /** DISPLAY */
    // {{{
    //{{{
    public  static int	        GRAPHIC_PATH_W_MAX  = 800; //  SET by init_DIP .. USED in NpButton
    public  static int	        GRAPHIC_PATH_H_MAX  = 600; //  SET by init_DIP .. USED in NpButton
    public  static final float  GRAPHIC_PATH_FACTOR =  2f; // USED by init_DIP

    public  static int	        DISPLAY_ASPECT      = Configuration.ORIENTATION_LANDSCAPE;
    public  static int	        DISPLAY_H           = 800;
    public  static int	        DISPLAY_W           = 600;

    public  static int	        SCREEN_W            = DISPLAY_W;
    public  static int	        SCREEN_H            = DISPLAY_H;
    public  static int          PROFILE_BAND_SIZE   = 100;

    public  static int          SCREEN_SPLIT        = 1;

    public  static final String SECTION_BOUNDARY    = "*";

    public  static float        DEV_SCALE	    =  1;
    public  static final float  DEV_ZOOM	    =  1;

    public static float         DEV_RATIO           =  1F;
    public static float         TABS_RATIO          =  1F;

    public  static String       SAVE_CALLER         = "";
    //}}}
    // MONITOR {{{
    private static float   MON_SCALE	    = 0;
    private static int	   MON_DPI	    = 0;
    public  static float   TXT_ZOOM	    = 1;
    private static int	   DEV_H	    = 0;
    public  static int	   DEV_W	    = 0;

    //}}}
    // DISPLAY STYLE - HANDLE COLORS {{{
    public  static final int LFT_HANDLE_COLOR = Color.BLACK;

    public  static       int TOP_HANDLE_COLOR = Settings.GOOGLE_BLUE;
    public  static       int MID_HANDLE_COLOR = Settings.GOOGLE_RED;
    public  static       int BOT_HANDLE_COLOR = Settings.GOOGLE_GREEN;

    public  static final int DOCK_COLOR       = Color.parseColor("#FFaaaaff"); // BLUISH
    public  static final int  DOCK_HIDE_COLOR = Color.parseColor("#FF444466"); // DARK

    public  static final int HIST_COLOR       = Color.parseColor("#FF00ff00"); // GREEN
    public  static final int  PROF_NP_COLOR   = Color.parseColor("#FFffffff"); // WHITE
    public  static final int  BACK_NP_COLOR   = Color.parseColor("#FFff0000"); // RED
    public  static final int  FORE_NP_COLOR   = Color.parseColor("#FF0000ff"); // BLUE

    public  static final int CART_COLOR       = Color.parseColor("#FFff00ff"); // MAGENTA
    public  static final int  END_NP_COLOR    = Color.parseColor("#ddbbbbbb"); // LTGRAY
    public  static final int  DEL_NP_COLOR    = Color.parseColor("#ddff0000"); // RED
    public  static final int  SEE_NP_COLOR    = Color.parseColor("#dd666666"); // DKGRAY
    public  static final int  ADD_NP_COLOR    = Color.parseColor("#dd00ff00"); // GREEN

    // GUI CONTEXT
    public  static final int FG_COLOR_OFF     = Color.DKGRAY;
    public  static final int FG_COLOR_RDY     = Color.YELLOW;
    public  static final int FG_COLOR_ON      = COLOR_FIREBRICK;

    // HIDE RIBBONS
    public  static final int HIST_SHOW_WEIGHT = 1;
    public  static final int CART_SHOW_WEIGHT = 1;

    public  static final String DEL_NP_DESCRIPTION    = "click the tag you want add a copy in the cart";
    public  static final String SEE_NP_DESCRIPTION    = "check cart's content";
    public  static final String ADD_NP_DESCRIPTION    = "click the tag you want to replace with one from the cart";

    public  static final String PROF_NP_DESCRIPTION   = "current profile";
    public  static final String BACK_NP_DESCRIPTION   = "Go to previous selected profile";
    public  static final String FORE_NP_DESCRIPTION   = "Go to next selected profile";

    //}}}
    // DISPLAY STYLE - DEVICE INDEPENDANT PIXELS [dip] [dp] {{{
/*
:!start explorer "http://www.google.com/design/spec/what-is-material/elevation-shadows.html#elevation-shadows-shadows"
*/
    // ELEVATIONS (DIP) {{{
    public  static final int        DIALOG_ELEVATION = 24; // [Dialog] [Picker]
    private static final int        DRAWER_ELEVATION = 16; // [Nav drawer] [Right drawer] [Modal bottom Sheet]
    private static final int   PRESSED_FAB_ELEVATION = 12; // [Floating action button (FAB - pressed)]
    private static final int      SUB_MENU_ELEVATION =  9; // [Sub menu (+1dp for each sub menu)]
    private static final int       PRESSED_ELEVATION =  8; // [Bottom navigation bar] [Menu] [Card (picked up state)] [Raised button (pressed state)]
    private static final int  RELEASED_FAB_ELEVATION =  6; // [Floating action button (FAB - resting elevation)] [Snackbar]
    private static final int       APP_BAR_ELEVATION =  4; // [App Bar]
    private static final int REFRESH_ENTRY_ELEVATION =  3; // [Refresh indicator] [Quick entry / Search bar (scrolled state)]
    private static final int      RELEASED_ELEVATION =  2; // [Card (resting elevation) *] [Raised button (resting elevation)*] [Quick entry / Search bar (resting elevation)] private static final int        SWITCH_ELEVATION =  1; // [Switch]

    public  static final int  MAX_ELEVATION          = DRAWER_ELEVATION+4;
    public  static final int  WV_TOOL_ELEVATION      = DRAWER_ELEVATION+3; // 19
    public  static final int  FS_WSWAP_ELEVATION     = DRAWER_ELEVATION+2; // 18
    public  static final int  FS_SCROLL_ELEVATION    = DRAWER_ELEVATION+1; // 17
    public  static final int  WV_BUTTON_ELEVATION    = DRAWER_ELEVATION  ; // 16

    public  static final int  WEBVIEW_ELEVATION      = SUB_MENU_ELEVATION+1;
    public  static final int  WVCONTAINER_ELEVATION  = SUB_MENU_ELEVATION;

    public  static final int  FS_BUTTON_ELEVATION    = APP_BAR_ELEVATION ;

    public  static       int  DOCK_MENUBAR_ELEVATION = DRAWER_ELEVATION  ;
    public  static final int  DOCK_VISITED_ELEVATION = DRAWER_ELEVATION  ;

    public  static final int  DCK_HANDLE_ELEVATION   = DRAWER_ELEVATION-1;
    public  static final int  TOP_HANDLE_ELEVATION   = DRAWER_ELEVATION-2;
    public  static final int  MID_HANDLE_ELEVATION   = DRAWER_ELEVATION-3;
    public  static final int  BOT_HANDLE_ELEVATION   = DRAWER_ELEVATION-4;
    public  static final int  LFT_HANDLE_ELEVATION   = DRAWER_ELEVATION-5;

    //}}}
    // DIMENSIONS f(DISPLAY SIZE) {{{

    public  static final int  TOOL_BADGE_SIZE      = 96; // .. (badge diameter)
    public  static final int  FINGER_CLUTTER_OFFSET= TOOL_BADGE_SIZE;
    public  static final int  TOOL_BADGE_BIG_SIZE  =128;

    public  static       int  WEBVIEW_MARGIN       = 40; // 160dpi ==  100 240dpi  [DISPLAY_MIN / 12] [1200==100] [480=40]
    public  static       int  WEBVIEW_GAP          = 13; // 160dpi ==   20 240dpi  [DISPLAY_MIN / 60] [1200== 20] [480=13]
    public  static       int  WEBVIEW_OVER         =  2; // 160dpi ==    4 240dpi  [DISPLAY_MIN /300] [1200== 20] [480= 2]
    public  static       int  WEBVIEW_WIDTH        =  0; // ... set by RTabs.adjust_webView_geometry

    public  static       int  SCROLLBAR_W_MIN      =      WEBVIEW_GAP;
    public  static       int  SCROLLBAR_H_MIN      =  4 * WEBVIEW_GAP;
    public  static       int  SCROLLBAR_BORDER_IN  =  2 * WEBVIEW_GAP;
    public  static       int  SCROLLBAR_BORDER_OUT =      WEBVIEW_GAP / 2;
    public  static       int  SCROLLBAR_SHAPE_W    =      WEBVIEW_GAP + TOOL_BADGE_SIZE; // FRAME >= (BADGE + BODY)
    public static        int  SCROLLBAR_SHRINK_MAX = SCROLLBAR_SHAPE_W - SCROLLBAR_W_MIN -1;

    private static float DensityOn160 = 1f; // dp = [pixels] * (160 / screen density)
    public  static float  get_DIP(float dp) { return dp / DensityOn160; }
    private static void  init_DIP()
    {
      //DensityOn160 = RTabs.activity.getResources().getDisplayMetrics().density;
        DensityOn160 = RTabs.activity.getResources().getDisplayMetrics().densityDpi / 160f; // i.e. (240f / 160f)

        //...........................................................................[ 240     ] [160   ]
    //  WEBVIEW_MARGIN = get_DIP( WEBVIEW_MARGIN );
    //  WEBVIEW_GAP    = get_DIP( WEBVIEW_GAP    );
    //  WEBVIEW_OVER   = get_DIP( WEBVIEW_OVER   );
        //...............................................................................................

        Display     display = RTabs.activity.getWindowManager().getDefaultDisplay();
        Point             p = new Point(); display.getRealSize(p);
        int       display_W = p.x;
        int       display_H = p.y;

        GRAPHIC_PATH_W_MAX  = (int)(display_W * GRAPHIC_PATH_FACTOR);
        GRAPHIC_PATH_H_MAX  = (int)(display_H * GRAPHIC_PATH_FACTOR);
//Settings.MON(TAG_SETTINGS, "init_DIP", "GRAPHIC_PATH MAX=["+GRAPHIC_PATH_W_MAX+" x "+GRAPHIC_PATH_H_MAX+"]");

        set_DISPLAY_WH(display_W, display_H);

        int display_min = Math.min(Settings.DISPLAY_W, Settings.DISPLAY_H);

    //  WEBVIEW_MARGIN  = display_min / 12; // 1200==100] [480=40]
    //  WEBVIEW_GAP     = display_min / 60; // 1200== 20] [480= 9]
    //  WEBVIEW_OVER    = display_min /240; // 1200== 20] [480= 1]

    //  WEBVIEW_MARGIN  = display_min / 20; // 1200== 60] [480=24]
        WEBVIEW_MARGIN  = display_min / 30; // 1200== 40] [480=16]
    //  WEBVIEW_GAP     = display_min / 60; // 1200== 20] [480= 9]
        WEBVIEW_GAP     = display_min / 40; // 1200== 30] [480=12]
        WEBVIEW_OVER    = display_min /240; // 1200== 20] [480= 1]

        SCROLLBAR_W_MIN      =     WEBVIEW_GAP;
        SCROLLBAR_H_MIN      = 4 * WEBVIEW_GAP;
        SCROLLBAR_BORDER_IN  = 2 * WEBVIEW_GAP;
        SCROLLBAR_BORDER_OUT =     WEBVIEW_GAP / 2;
        SCROLLBAR_SHAPE_W    =     WEBVIEW_GAP + TOOL_BADGE_SIZE;

        TAB_MARK_H           =     WEBVIEW_GAP;
        SIZE_MIN_FOR_TEXT    =     WEBVIEW_GAP;
    }

    //}}}
    //}}}
    // DISPLAY STYLE - TABS OPACITY {{{

    public static final int OPACITY_HIDDEN_PERCENT = 10;
    public static final int OPACITY_DIMMED_PERCENT = 50;

    public static       int Get_OPACITY_HIDDEN() { return (int)(0xff * Settings.OPACITY/100F * OPACITY_HIDDEN_PERCENT/100F); }
    public static       int Get_OPACITY_DIMMED() { return (int)(0xff * Settings.OPACITY/100F * OPACITY_DIMMED_PERCENT/100F); }
    public static       int Get_OPACITY       () { return (int)(0xff * Settings.OPACITY/100F                              ); }

    public static final int PHANTOM_COLOR  = Color.parseColor("#"+ (int)(0xff * OPACITY_HIDDEN_PERCENT/100F) +"FFFFFF");
    //}}}
    // BAND {{{
    public static final int CART_STATE_DEFAULT = 0;
    public static final int CART_STATE_DEL     = 1;
    public static final int CART_STATE_ADD     = 2;

    //}}}
    // set_DISPLAY_WH {{{
    public static void set_DISPLAY_WH(int display_W, int display_H)
    {
//*SETTINGS*/Settings.MOC(TAG_SETTINGS, "set_DISPLAY_WH("+ display_W +"x"+ display_H +")");

        SCREEN_W            = display_W;
        SCREEN_H            = display_H;

        DISPLAY_H           = SCREEN_H;
        DISPLAY_W           = SCREEN_W;//XXX - get_DOCK_WIDTH();

        DEV_RATIO           = (float)DISPLAY_W / (float)DISPLAY_H;

        int display_min     = Math.min(DISPLAY_W, DISPLAY_H);
        PROFILE_BAND_SIZE   = display_min / 12; // 1200==100] [480=40]

        FONT_SIZE_MAX       = Math.min(DISPLAY_W, DISPLAY_H) / 3;

        TO_GUARD_RIGHT      = DISPLAY_W - GUARD_WIDTH;

//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "...........DEV_RATIO=["+ DEV_RATIO     +"]");
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, ".......FONT_SIZE_MAX=["+ FONT_SIZE_MAX +"]");
    }
    //}}}
    // set_XY_WH {{{
    public  static final float  DEV_SCALE_MAX       = 10;
    public  static float        DEV_SCALE_MIN       =  1;

    public  static void set_XY_WH(int left, int top, int width, int height)
    {
//*SETTINGS*/Settings.MOC(TAG_SETTINGS, "set_XY_WH("+ left +"@"+ top +", "+ width +"x"+ height +")");

        float w_scale
            = (width > 0)
            ? (float)(DISPLAY_W - 2*PROFILE_BAND_SIZE) / (float)(width  + 2*left)
            : 1F;

        float h_scale = (height > 0)
            ? (float)(DISPLAY_H - 2*PROFILE_BAND_SIZE) / (float)(height + 2* top)
            : 1F;

        DEV_SCALE_MIN = Math.min(w_scale, h_scale);

        if((width > 0) && (height > 0))
            TABS_RATIO = (float)(width + 2*left) / (float)(height + 2*top);
        else
            TABS_RATIO = 1f;

//*SETTINGS*/Settings.MOM(TAG_SETTINGS, ".......DEV_SCALE_MIN=["+ DEV_SCALE_MIN     +"]");
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "...PROFILE_BAND_SIZE=["+ PROFILE_BAND_SIZE +"]");
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "..........TABS_RATIO=["+ TABS_RATIO        +"]");
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "..........DISPLAY_WH=["+ DISPLAY_W +"x"+ DISPLAY_H +"]");
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, ".........TABS_MIN_WH=["+ (int)(width*DEV_SCALE_MIN) +"x"+ (int)(height*DEV_SCALE_MIN) +"]");
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, ".........TABS_CRNTWH=["+ (int)(width*DEV_SCALE    ) +"x"+ (int)(height*DEV_SCALE    ) +"]");
    }
    //}}}
    // set_VIEWPORT {{{
    public static  int     VIEWPORT_T = 0;
    public static  int     VIEWPORT_L = 0;
    public static  int     VIEWPORT_W = 0;
    public static  int     VIEWPORT_H = 0;

    public static void set_VIEWPORT(int left, int top, int width, int height)
    {
//*SETTINGS*/Settings.MOC(TAG_SETTINGS, "set_VIEWPORT("+ left +"@"+ top +", "+ width +"x"+ height +")");

        VIEWPORT_L = left;
        VIEWPORT_T = top;
        VIEWPORT_W = width;
        VIEWPORT_H = height;
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, ".........VIEWPORT [L@T]=["+ VIEWPORT_L +"@"+ VIEWPORT_T +"] [B@R]=["+ VIEWPORT_W +"@"+ VIEWPORT_H +"]");
    }

    //}}}
    // }}}
    // log_display {{{
    public static String log_display()
    {
        Log_init("");
        //...........................=
        Log_append("        DISPLAY_W=["+ DISPLAY_W           +"]");
        Log_append("        DISPLAY_H=["+ DISPLAY_H           +"]");
        Log_append("        DEV_RATIO=["+ DEV_RATIO           +"]");

        Log_append("       VIEWPORT_T=["+ VIEWPORT_T          +"]");
        Log_append("       VIEWPORT_L=["+ VIEWPORT_L          +"]");
        Log_append("       VIEWPORT_W=["+ VIEWPORT_W          +"]");
        Log_append("       VIEWPORT_H=["+ VIEWPORT_H          +"]");

        Log_append("    WEBVIEW_GAP..=["+ WEBVIEW_GAP        +"]");
        Log_append("    WEBVIEW_OVER.=["+ WEBVIEW_OVER       +"]");

        return Log_toString();
    }
    //}}}

    /** GUI */
    //{{{
    // GUI_STYLE {{{
    // {{{
    public  static final String   GUI_STYLE_ONEDGE        =         "ONEDGE";
    public  static final String   GUI_STYLE_ROUND         =          "ROUND";
    public  static final String   GUI_STYLE_SQUARE        =         "SQUARE";
    public  static final String   GUI_STYLE_TILE          =           "TILE";
    public  static final String   GUI_STYLE_SATIN         =           "SATIN";

    public  static       String   GUI_STYLE               =  GUI_STYLE_ROUND;
    public  static       boolean  GUI_STYLE_HAS_CHANGED   =            false;
    // }}}
    // set_GUI_STYLE {{{
    public static void set_GUI_STYLE(String gui_style)
    {
        gui_style = gui_style.toUpperCase();
        if     (TextUtils.equals(gui_style, GUI_STYLE_ONEDGE)) GUI_STYLE = GUI_STYLE_ONEDGE;
        else if(TextUtils.equals(gui_style, GUI_STYLE_ROUND )) GUI_STYLE = GUI_STYLE_ROUND ;
        else if(TextUtils.equals(gui_style, GUI_STYLE_SQUARE)) GUI_STYLE = GUI_STYLE_SQUARE;
        else if(TextUtils.equals(gui_style, GUI_STYLE_TILE  )) GUI_STYLE = GUI_STYLE_TILE  ;
        else if(TextUtils.equals(gui_style, GUI_STYLE_SATIN )) GUI_STYLE = GUI_STYLE_SATIN ;
    }
    //}}}
    // toggle_GUI_STYLE {{{
    public static void toggle_GUI_STYLE()
    {
        if     ( GUI_STYLE == GUI_STYLE_ONEDGE  ) GUI_STYLE = GUI_STYLE_ROUND ;
        else if( GUI_STYLE == GUI_STYLE_ROUND   ) GUI_STYLE = GUI_STYLE_SATIN ;
        else if( GUI_STYLE == GUI_STYLE_SATIN   ) GUI_STYLE = GUI_STYLE_TILE  ;
        else if( GUI_STYLE == GUI_STYLE_TILE    ) GUI_STYLE = GUI_STYLE_SQUARE;
        else /*  GUI_STYLE == GUI_STYLE_SQUARE */ GUI_STYLE = GUI_STYLE_ONEDGE; // .. (start over)
        GUI_STYLE_HAS_CHANGED = true;
    }
    //}}}
    public static boolean  is_GUI_STYLE_SQUARE() { return (GUI_STYLE == GUI_STYLE_SQUARE); }
    public static boolean  is_GUI_STYLE_TILE  () { return (GUI_STYLE == GUI_STYLE_TILE  ); }
    public static boolean  is_GUI_STYLE_ROUND () { return (GUI_STYLE == GUI_STYLE_ROUND ); }
    public static boolean  is_GUI_STYLE_ONEDGE() { return (GUI_STYLE == GUI_STYLE_ONEDGE); }
    public static boolean  is_GUI_STYLE_SATIN () { return (GUI_STYLE == GUI_STYLE_SATIN ); }
    // }}}
    // GUI_TYPE {{{
    // {{{
    public  static final String   GUI_TYPE_HANDLES        =    "GUI_HANDLES";
    public  static final String   GUI_TYPE_DOCKING        =    "GUI_DOCKING";
    public  static       String   GUI_TYPE                = GUI_TYPE_DOCKING;

    // }}}
    // set_GUI_TYPE {{{
    public static void set_GUI_TYPE(String gui_type)
    {
        gui_type = gui_type.toUpperCase();
        if     (TextUtils.equals(gui_type, GUI_TYPE_DOCKING)) GUI_TYPE = GUI_TYPE_DOCKING;
        else if(TextUtils.equals(gui_type, GUI_TYPE_HANDLES)) GUI_TYPE = GUI_TYPE_HANDLES;
    }
    //}}}
    // get_GUI_TYPE_NAME {{{
    public static String get_GUI_TYPE_NAME(String type)
    {
        if( type.equals(GUI_TYPE_DOCKING )) return "dock";
        else         /* GUI_TYPE_HANDLES */ return "handles";
    }
    //}}}
    // is_GUI_TYPE_HANDLES {{{
    public static boolean is_GUI_TYPE_HANDLES()
    {
        if(GUI_TYPE == GUI_TYPE_HANDLES)    return  true;
        if(GUI_TYPE == GUI_TYPE_DOCKING)    return false;
        // setup identity-check as a replacement for equality-check
        GUI_TYPE
            =  GUI_TYPE.equals( GUI_TYPE_DOCKING )
            ?  GUI_TYPE_DOCKING
            :  GUI_TYPE_HANDLES;
        return GUI_TYPE == GUI_TYPE_HANDLES;
    }
    //}}}
    // Get_clamp_playground_margin_left {{{
    public static int Get_clamp_playground_margin_left()
    {
        return is_GUI_TYPE_HANDLES()
            ?  Handle.Get_STANDBY_WIDTH()
            :  Handle.Get_DOCK_STANDBY_WIDTH()
            ;
    }
    //}}}
    // }}}
    // FONT {{{
    // {{{
    private static final String   FONT_NOTO_TTF     = "NotoSansSymbols-Regular.ttf";
    public  static final String   GUI_FONT_DEFAULT  = "DEFAULT_FONT";
    public  static final String   GUI_FONT_NOTO     = "NOTO_FONT";

    private static       Typeface NotoTypeface;
    public  static       String   GUI_FONT          =    GUI_FONT_NOTO;
    // }}}
    // set_GUI_FONT {{{
    public static void set_GUI_FONT(String gui_font)
    {
        gui_font = gui_font.toUpperCase();
        if     (TextUtils.equals(gui_font, GUI_FONT_DEFAULT)) GUI_FONT = GUI_FONT_DEFAULT;
        else if(TextUtils.equals(gui_font, GUI_FONT_NOTO   )) GUI_FONT = GUI_FONT_NOTO;
    }
    //}}}
    // is_GUI_FONT_DEFAULT {{{
    public static boolean is_GUI_FONT_DEFAULT()
    {
        if(GUI_FONT == GUI_FONT_DEFAULT)    return  true;
        if(GUI_FONT == GUI_FONT_NOTO   )    return false;
        // setup identity-check as a replacement for equality-check
        GUI_FONT
            =  GUI_FONT.equals( GUI_FONT_NOTO )
            ?  GUI_FONT_NOTO
            :  GUI_FONT_DEFAULT;
        return GUI_FONT == GUI_FONT_DEFAULT;
    }
    //}}}
    // getTypeface {{{
    public static Typeface getTypeface()
    {
        if( is_GUI_FONT_DEFAULT() ) return Typeface.DEFAULT;
        else                        return getNotoTypeface();
    }
    //}}}
    // getNotoTypeface {{{
    public static Typeface getNotoTypeface() {
        if(NotoTypeface == null)
            NotoTypeface = Typeface.createFromAsset(RTabs.activity.getAssets(), "fonts/"+ Settings.FONT_NOTO_TTF);
        return NotoTypeface;
    }
    //}}}
    //}}}
    // has_label_markup {{{
    // {{{
    private static String CHARS_SYMBOL_BLACK_STAR;
    private static String CHARS_SYMBOL_GEAR;
    private static String CHARS_SYMBOL_HOME;
    private static String CHARS_SYMBOL_INDEX;
    private static String CHARS_SYMBOL_WHITE_STAR;
    private static String CHARS_SYMBOL_BLACK_CIRCLE;
    private static String CHARS_SYMBOL_KEYBOARD;
    private static String CHARS_SYMBOL_PENCIL;
    private static String CHARS_SYMBOL_COMPUTER;
    // }}}
    // has_label_markup {{{
    public static boolean has_label_markup(String s)
    {
        // CHARS LOOKUP
        //int end = s.indexOf(NotePane.INFO_SEP);
        //if(end < 0) end = s.length();
        //for (int i = 0; i < end; ++i) if(s.charAt(i) == ch)

        // STRINGS LOOKUP
        boolean  contains_markup_symbol
            =    (TextUtils.indexOf(s, CHARS_SYMBOL_BLACK_STAR  ,0, 20) >= 0) // "favorites"
            ||   (TextUtils.indexOf(s, CHARS_SYMBOL_GEAR        ,0, 20) >= 0) // "CONOTROLS"
            ||   (TextUtils.indexOf(s, CHARS_SYMBOL_HOME        ,0, 20) >= 0) // "home"
            ||   (TextUtils.indexOf(s, CHARS_SYMBOL_INDEX       ,0, 20) >= 0) // "index"
            ||   (TextUtils.indexOf(s, CHARS_SYMBOL_WHITE_STAR  ,0, 20) >= 0) // "PROFILES"
            ||   (TextUtils.indexOf(s, CHARS_SYMBOL_BLACK_CIRCLE,0, 20) >= 0) // '/'
            ||   (TextUtils.indexOf(s, CHARS_SYMBOL_COMPUTER    ,0, 20) >= 0) // TAG: RUN BROWSE SHELL
            ||   (TextUtils.indexOf(s, CHARS_SYMBOL_KEYBOARD    ,0, 20) >= 0) // TAG: SENDKEY SENDINPUT
            ||   (TextUtils.indexOf(s, CHARS_SYMBOL_PENCIL      ,0, 20) >= 0) // TAG: note'
            ;

//Settings.MON(TAG_SETTINGS, "has_label_markup("+ Settings.ellipsis(s,24) +") ...return "+ contains_markup_symbol);
        return contains_markup_symbol;
    }
//}}}
    //}}}
    //}}}
    // log_gui {{{
    public static String log_gui()
    {
        Log_init("");
        //...........................=
        Log_append("         GUI_FONT=["+ GUI_FONT        +"]");
        Log_append("         GUI_TYPE=["+ GUI_TYPE        +"]");
        Log_append("  MARK_SCALE_GROW=["+ MARK_SCALE_GROW +"]");
        return Log_toString();
    }
    //}}}
    // DIALOG {{{
    public  static String Get_CANCEL_LABEL         () { return                 SYMBOL_LEFT_ARROW +" CANCEL";          }
    public  static String Get_DISCARD_CHANGES_LABEL() { return                 SYMBOL_CROSS_MARK +" DISCARD CHANGES"; }
    public  static String Get_SAVE_CHANGES_LABEL   () { return "SAVE CHANGES "+SYMBOL_DISK;                           }
    public  static String Get_SAVE_PROFILE_LABEL   () { return "SAVE PROFILE "+SYMBOL_DISK;                           }
    //}}}

    /** DEVICE */
    // [DPI] [FONT] {{{
    private static int	      DEV_DPI	    = 0;

    public static final float FONT_SIZE_DEF =  12;
    public static final float FONT_SIZE_MIN =   4;
    public static int	      FONT_SIZE_MAX = 400; // ...default to be adjusted by real display measurements

    //}}}
    // log_font {{{
    public static String log_font()
    {
        Log_init("");
        //...........................=
        Log_append("    FONT_SIZE_MAX=["+ FONT_SIZE_MAX       +"]");
        return Log_toString();
    }
    //}}}

    /** HANDLES */
    //{{{
    // {{{
    public  static final String	CHILD_IGNORE_VISIBILITY  = "CHILD_IGNORE_VISIBILITY";
    public  static final String	CHILD_ALWAYS_VISIBLE     = "CHILD_ALWAYS_VISIBLE";
    public  static final String	CHILD_SHOWN_HIDDEN       = "CHILD_SHOWN_HIDDEN";
    public  static final String	HANDLE_HIDDEN_IN_STANDBY = "HANDLE_HIDDEN_IN_STANDBY";

    public static final int       GUARD_WIDTH    = 100;
    public static final int       GUARD_LEFT     = GUARD_WIDTH;
    public static       int    TO_GUARD_RIGHT    = DISPLAY_W-GUARD_WIDTH;

    public  static final String CARTTABS_TABLE           = "CARTTABS_TABLE";
    public  static final String CONTROLS_TABLE           = "CONTROLS_TABLE";
    public  static final String DOCKINGS_TABLE           = "DOCKINGS_TABLE";
    public  static final String FREETEXT_TABLE           = "FREETEXT_TABLE";
    public  static final String PROFHIST_TABLE           = "PROFHIST_TABLE";
    public  static final String PROFILES_TABLE           = "PROFILES_TABLE";
    public  static final String SOUNDS_TABLE             = "SOUNDS_TABLE";

    // }}}
    // get_HANDLE_HEIGHT {{{
    public static int get_HANDLE_HEIGHT()
    {
        return Math.min(Settings.SCREEN_W, Settings.SCREEN_H) / 4;
    }
    //}}}
    // get_DOCK_WIDTH {{{
    public static int get_DOCK_WIDTH()
    {
        int margin
            = is_GUI_TYPE_HANDLES()
            ?  Handle.Get_TUCKED_WIDTH()
            :  Handle.Get_STANDBY_WIDTH();
//*SETTINGS*/Settings.MOC(TAG_SETTINGS, "get_DOCK_WIDTH", "...return ["+ margin +"]");
        return margin;
    }
    //}}}
    //}}}
    // log_handles {{{
    public static String log_handles()
    {
        Log_init("");
        //...........................=
        Log_append("PROFILE_BAND_SIZE=["+ PROFILE_BAND_SIZE   +"]");
        Log_append("   get_DOCK_WIDTH=["+ get_DOCK_WIDTH()    +"]");
        Log_append("get_HANDLE_HEIGHT=["+ get_HANDLE_HEIGHT() +"]");
        return Log_toString();
    }
    //}}}

    /** INTERFACES */
    // ClampListener {{{
    public  interface ClampListener
    {
        boolean clamp1_has_a_grid_for        (NotePane moving_np, float[] grid_w_h_s);
        void    clamp2_get_np_to_grid_xy     (View view, int[] grid_xy);
        void    clamp3_handle_not_moved      ();
        boolean clamp4_get_bounce_playground (NotePane moving_np, Rect  playground_rect);
        boolean clamp5_get_gravityPoint      (NotePane moving_np, Point gravityPoint);
        void    clamp6_run_move_inertia      (Clamp clamp); // .. runOnUiThread
        boolean clamp7_is_dragging_something ();
        void    clamp8_drag                  (float x, float y);
        void    clamp9_bounced               (NotePane moving_np);
        boolean clamp10_onClamped            (NotePane moving_np);
    }
    //}}}
    // MagnetListener {{{
    public  interface MagnetListener
    {
        boolean magnet_freezed ();
        void    magnet_1_progress(float progress);
        boolean magnet_2_tracked ();
        boolean magnet_3_reached ();
        boolean magnet_4_checked ();
    }
/*

:grep '[^0-9]_(progress\|tracked\|reached\|checked)'
/[^0-9]_\(progress\|tracked\|reached\|checked\)/

:grep '[0-9]_(progress\|tracked\|reached\|checked)'
/[0-9]_\(progress\|tracked\|reached\|checked\)/

*/
    //}}}
    // FromToInterface {{{
    public  interface FromToInterface
    {
        // SAVE FROM
        void  save_from (String caller);

        String get_save_from_caller();

        // SET TO
        void  set_x_to  (float x );
        void  set_y_to  (float y );
        void  set_s_to  (float s );
        void  set_xrto  (float xr);
        void  set_yrto  (float yr);
        void  set_zrto  (float sr);

        // GET FROM
        float get_x_from(       );
        float get_y_from(       );
        float get_s_from(       );
        float get_xrfrom(       );
        float get_yrfrom(       );
        float get_zrfrom(       );


        // GET TO
        float get_x_to  (       );
        float get_y_to  (       );
        float get_s_to  (       );
        float get_xrto  (       );
        float get_yrto  (       );
        float get_zrto  (       );
    }
    //}}}

    /** PROFILE */
    //{{{
    // {{{
    public  static final String  PROFILES_ZIP_FILE_NAME = "Profiles.zip";
    public  static final String  USER_INDEX             = "/index";
    public  static final String  FINISH_APP             = "FINISH";
    public  static final String  COMMENT_STRING         = "#";
    public  static final String  ENTRY_PALETTE          = "ENTRY_PALETTE";

    public  static       Profile LoadedProfile          = new Profile("");
    public  static       String  PROFILE	        = "";
    public  static       String  SOURCE	                = "";
    public  static       int	 PRODATE	        = 0;

    public  static       String  Working_profile                      = "";
    public  static       boolean Working_profile_has_SECTION_BOUNDARY = false;
    public  static       int     Working_profile_prodate              =  0;
    // }}}
    // SetLoadedProfile {{{
    public  static void SetLoadedProfile(Profile profile)
    {
        if(D) log("Settings.SetLoadedProfile("+ profile.name +"):");

        LoadedProfile = profile;

        PROFILE = TextUtils.isEmpty( LoadedProfile.name )
            ?      PROFILES_TABLE
            :      LoadedProfile.name
            ;

        Settings.SOURCE = Settings.DEVICE;

        Track_last_Working_profile();

        if(D) log("LoadedProfile: "+ LoadedProfile);
    }
    // }}}
    // SetLoadedProfileWebGroupsChecked {{{
    public  static void SetLoadedProfileWebGroupsChecked(boolean state)
    {
        if(D) log("Settings.SetLoadedProfileWebGroupsChecked("+ state +"):");
        LoadedProfile.webGroup_checked = state;
    }
    // }}}
    // GetLoadedProfileWebGroupsChecked {{{
    public  static boolean GetLoadedProfileWebGroupsChecked()
    {
        if(D) log("GetLoadedProfileWebGroupsChecked ...return "+ LoadedProfile.webGroup_checked);
        return LoadedProfile.webGroup_checked;
    }
    // }}}
    // ReLoadProfile {{{
    private static void ReLoadProfile()
    {
        if(D) log("Settings.ReLoadProfile: "+ LoadedProfile);

        LoadedProfile.reload();
        Track_last_Working_profile();

        if(D) log("=== LoadedProfile: "+ LoadedProfile);
    }
    // }}}
    // Track_last_Working_profile {{{
    public static Profile Working_profile_instance = null;
    private static void Track_last_Working_profile()
    {
        if(        !(LoadedProfile.name.equals("")             )
                && ! is_a_dynamic_profile_entry( LoadedProfile.name  )
                &&   SOURCE.equals( DEVICE )
          ) {
            Working_profile         = LoadedProfile.name;
            Working_profile_instance= LoadedProfile;
            Working_profile_prodate = PRODATE;
//*SETTINGS*/Settings.MON(TAG_SETTINGS, "Track_last_Working_profile", "Working_profile_instance set to ["+Working_profile_instance+"]");
          }
/*
Settings.MON(TAG_SETTINGS, "Track_last_Working_profile():\n"
        +"  Working_profile=["+ Working_profile          +"]\n"
        +" Settings.PROFILE=["+ Settings.PROFILE         +"]\n"
        +"    UNICODE_SHEET=["+ Settings.UNICODE_SHEET   +"]\n"
        +"   UNICODE_PRESET=["+ Settings.UNICODE_PRESET  +"]\n"
        +"  TOOL_URL_ACTION=["+ Settings.TOOL_URL_ACTION +"]\n"
        ); // XXX
*/
    }
    //}}}
    // DisplayWorkingProfileBuffers {{{
    public static String DisplayWorkingProfileBuffers()
    {
        if(LoadedProfile == null) {
            String s = SYMBOL_KEY_VAL+SYMBOL_PALETTE+SYMBOL_TABS;
            return s+"NO CURRENTLY LOADED PROFILE"+s+"\n";
        }

        Log_init  ("Loaded profile: "+ LoadedProfile.toString() + Settings.TRACE_OPEN);
        LoadedProfile.log_buffers(Log_sb);
        Log_append("Loaded profile: "+ LoadedProfile.toString() + Settings.TRACE_CLOSE);

        return Log_toString();
    }
    //}}}
    // Delete_WORKING_PROFILE {{{
    public static void Delete_WORKING_PROFILE()
    {
        if(D) log("Delete_WORKING_PROFILE()");

        String s = SYMBOL_KEY_VAL+SYMBOL_PALETTE+SYMBOL_TABS;

        if(Working_profile.equals("")) {
            if(D) log(s+"NO CURRENT WORKINNG PROFILE"+s);
        }
        else {
            Delete_PROFILE( Working_profile );

            Working_profile    = "";
            Working_profile_prodate = 0;
        }
    }
    //}}}
    // Delete_PROFILE {{{
    public static void Delete_PROFILE(String profile_name)
    {
        String msg = Profile.DeleteProfile( profile_name );
        String s   = SYMBOL_KEY_VAL+SYMBOL_PALETTE+SYMBOL_TABS+" ";
        if(D) log(s + "PROFILE ["+ profile_name +"]"
                + s + msg);
    }
    //}}}
    // is_a_dynamic_profile_entry {{{
    public static boolean is_a_dynamic_profile_entry(String profile_name)
    {
        return        !TextUtils.isEmpty      ( profile_name   )
            && (       profile_name.equals    ( CONTROLS_TABLE )
                    || profile_name.equals    ( DOCKINGS_TABLE )
                    || profile_name.equals    ( FINISH_APP     ) // extra (i.e. not a profile but may appear amongst profiles tables)
                    || profile_name.equals    ( PROFHIST_TABLE )
                    || profile_name.equals    ( PROFILES_TABLE )
                    || profile_name.equals    ( SOUNDS_TABLE   )
                    || profile_name.startsWith( FREETEXT_TABLE )
                    // profile_name.equals    ( USER_INDEX     ) // extra (i.e. not a dynamic profile .. only a privileged one in some cases)
               );
    }
    //}}}
    // get_profile_name_from_tag {{{
    public static String get_profile_name_from_tag(String tag)
    {
        if(! tag.startsWith("PROFILE ") ) return "";

        //..........................PROFILE NAME
        //..........................012345678...
        String profile_name = tag.substring(8);
        profile_name = Settings.Del_color_hex_from_text( profile_name );

//System.err.println("get_profile_name_from_tag("+tag+") ...return ["+profile_name+"]");
        return profile_name;
    }
    //}}}
    //}}}

    // TABS {{{
    public  static final int    TAB_GRID_MIN            =  8;
    private static final int    TAB_MIN_TXT_H           =  5;
    private static final int    TAB_MIN_TXT_W           = 10;
    public  static final int    TAB_GRID_S              = 20;
    public  static final int    TAB_MIN_BTN_H           =  2;
    public  static final int    TAB_MIN_BTN_W           =  3;
    public  static       int    TAB_MARK_H              =  1; // dynamically adjusted
    public  static       int    SIZE_MIN_FOR_TEXT       =  1; // dynamically adjusted

    //}}}
    // WEB {{{
    public  static final int SWAP_0 = 0;
    public  static final int SWAP_L = 1;
    public  static final int SWAP_C = 2;
    public  static final int SWAP_R = 3;

    public  static int     WORKBENCH_TOOL  =  0;
    public  static String  BOOKMARK_TITLE  = "";
    public  static String  BOOKMARK_URL    = "";
    public  static String  REQUESTED_TITLE = "";
    public  static String  REQUESTED_URL   = "";

    public  static void   set_BOOKMARK_URL   (String   url) { BOOKMARK_URL    = (  url != null) ?   url : ""; }
    public  static void   set_BOOKMARK_TITLE (String title) { BOOKMARK_TITLE  = (title != null) ? title : ""; }
    public  static void   set_REQUESTED_URL  (String   url) { REQUESTED_URL   = (  url != null) ?   url : ""; }
    public  static void   set_REQUESTED_TITLE(String title) { REQUESTED_TITLE = (title != null) ? title : ""; }

    // }}}

    /** KEY_VAL */
    // set_KEY_VAL {{{
    @SuppressWarnings("ConstantConditions")
    public static List<String> set_KEY_VAL(String argLine, String caller)
    {
//SETTINGS*/Settings.MOC(TAG_SETTINGS, "set_KEY_VAL(argLine=["+argLine+"], caller=["+caller+"]):");
/* @SEE C# VERSION {{{
:vnew /LOCAL/DATA/ANDROID/PROJECTS/RTabs/app/src/main/java/ivanwfr/rtabs/Settings.java
:vnew /LOCAL/STORE/DEV/PROJECTS/RTabs/Util/src/Settings.cs
: new /LOCAL/STORE/DEV/PROJECTS/RTabs/Util/src/Settings.cs
*/ // }}}
        // RESET DISPATCHING-APPLICATION SIGNATURE {{{
        // - before sharing these changes,
        // - a dispatching source will add ITS SOURCE=ID KEY_VAL-SIGNATURE
        // - (...would it be to recognize one of its bouncing-back messages)
        Settings.SOURCE = "";

        // }}}
        // SPLIT [argline] INTO KEY=VALUE PAIRS {{{

        // get rid of empty fields by squeezing extra contiguous inner spaces sequences into a single one
        while(argLine.indexOf("  ") > 0) argLine = argLine.replace("  ", " ");
        /*............................*/ argLine = argLine.replace("\n",  "");

        String[] args = argLine.split(" ");

        if(D) log("ooo set_KEY_VAL(args#="+args.length+"), caller=["+caller+"]:");

        //}}}
        boolean D_was_set=D;        // save  DEBUGGING state
/*SETTINGS..TAG_SETTINGS*/D=true;   // force DEBUGGING state

        // VARS {{{
        String  source      = null;
        String  profile     = null;

        String  bm_title    = null;
        String  bm_url      = null;

        String  lp_title    = null;
        String  lp_url      = null;

        String  gui_style   = null;
        String  gui_font    = null;
        String  gui_type    = null;

        int     prodate     = 0;

    //  float   dev_zoom    = 0;
        int     dev_dpi     = 0;
        int     dev_h       = 0;
        int     dev_w       = 0;

        float   mon_scale   = 0;
        float   txt_zoom    = 0;
        int     mon_dpi     = 0;

        int     opacity     = -1;   // 0 is for transparent
        int     maxcolors   = -1;   // 0 is for maximum not set
        String  palette     = null;

        //}}}
        // LOOP ON KEY=VAL pairs {{{

///SETTINGS*/ boolean was_logging = (LOGGING && (LOG_FLT != 0));// TAG_SETTINGS

        del_changes();

        String[] kv={"",""};

        for(int i=0; i<args.length; ++i)
        {
            // [kv[0] kv[1] {{{
//SETTINGS*/Settings.MOM(TAG_SETTINGS, "...args["+i+"]=["+args[i]+"]");

            int idx = args[i].indexOf('=');
            if(idx < 0) {
//SETTINGS*/Settings.MOM(TAG_SETTINGS, "xxx [CONTAINS_NON_EQUAL_SIGN]");
                continue;
            }

            kv[0]= args[i].substring(0, idx  ).trim();
            kv[1]= args[i].substring(   idx+1).trim();
//SETTINGS*/Settings.MOM(TAG_SETTINGS, " kv[0]=["+kv[0]+"]");
//SETTINGS*/Settings.MOM(TAG_SETTINGS, " kv[1]=["+kv[1]+"]");

            //}}}
            String key = kv[0].toUpperCase();
            String val = kv[1];
            try {
                switch( key )
                {
                    // PROFILE {{{
                    case "PROFILE"         : profile       =                      val ; add_change(key , profile       ); break;
                    case "PRODATE"         : prodate       = Integer.parseInt    (val); add_change(key , prodate       ); break;
                    case "SOURCE"          : source        =                      val ; add_change(key , source        ); break;
                    case "PALETTE"         : palette       =                      val ; add_change(key , palette       ); break;
                    case "OPACITY"         : opacity       = Integer.parseInt    (val); add_change(key , opacity       ); break;
                    case "MAXCOLORS"       : maxcolors     = Integer.parseInt    (val); add_change(key , maxcolors     ); break;
                    // }}}
                    // GEOMETRY {{{
                    case "DEV_DPI"         : dev_dpi       = Integer.parseInt    (val); add_change(key , dev_dpi       ); break;
                    case "DEV_H"           : dev_h         = Integer.parseInt    (val); add_change(key , dev_h         ); break;
                    case "DEV_W"           : dev_w         = Integer.parseInt    (val); add_change(key , dev_w         ); break;
                    case "MON_DPI"         : mon_dpi       = Integer.parseInt    (val); add_change(key , mon_dpi       ); break;
                    case "MON_SCALE"       : mon_scale     = Float  .parseFloat  (val); add_change(key , mon_scale     ); break;
                    case "TXT_ZOOM"        : txt_zoom      = Float  .parseFloat  (val); add_change(key , txt_zoom      ); break;
                 // case "DEV_ZOOM"        : dev_zoom      = Float  .parseFloat  (val); add_change(key , dev_zoom      ); break;
                    // }}}
                    // SERVER {{{
                    case "SERVER_IP"      : SERVER_IP      =                      val ; add_change(key , SERVER_IP      ); break;
                    case "SERVER_MAC"     : SERVER_MAC     =                      val ; add_change(key , SERVER_MAC     ); break;
                    case "SERVER_PASS"    : SERVER_PASS    =                      val ; add_change(key , SERVER_PASS    ); break;
                    case "SERVER_PORT"    : SERVER_PORT    = Integer.parseInt    (val); add_change(key , SERVER_PORT    ); break;
                    case "SERVER_SUBNET"  : SERVER_SUBNET  =                      val ; add_change(key , SERVER_SUBNET  ); break;
                    // }}}
                    // TOOLS {{{
                    case "UNICODE_SHEET"  : UNICODE_SHEET  =                      val ; add_change(key , UNICODE_SHEET  ); break;
                    case "UNICODE_PRESET" : UNICODE_PRESET =                      val ; add_change(key , UNICODE_PRESET ); break;
                    case "TOOL_URL_ACTION": TOOL_URL_ACTION=                      val ; add_change(key , TOOL_URL_ACTION); break;
                    // }}}
                    // LOGGING {{{
                    case "LOGGING"        : LOGGING        = Boolean.parseBoolean(val); add_change(key , LOGGING        ); break;
                    case "LOG_CAT"        : LOG_CAT        = Boolean.parseBoolean(val); add_change(key , LOG_CAT        ); break;
                    case "LOG_FLT"        : LOG_FLT        = Integer.parseInt    (val); add_change(key , LOG_FLT        ); break;
                    case "MONITOR"        : MONITOR        = Boolean.parseBoolean(val); add_change(key , MONITOR        ); break;
                //  case "SAVE_CALLER"    : SAVE_CALLER    =                      val ; add_change(key , SAVE_CALLER    ); break;
                //  .... [SAVE_CALLER] - (    set by SaveSettings)
                //  .... [SAVE_CALLER] - (cleared by LoadSettings)
                    // }}}
                    // WEB {{{
                    case "BOOKMARK_TITLE" : bm_title       =                      val ; add_change(key , bm_title       ); break;
                    case "BOOKMARK_URL"   : bm_url         =                      val ; add_change(key , bm_url         ); break;
                    case "REQUESTED_TITLE": lp_title       =                      val ; add_change(key , lp_title       ); break;
                    case "REQUESTED_URL"  : lp_url         =                      val ; add_change(key , lp_url         ); break;
                    case "WEBGROUPS"      : WEBGROUPS      =                      val ; add_change(key , WEBGROUPS      ); break;
                    // }}}
                    // GUI {{{
                    case "GUI_STYLE"      : gui_style      =                      val ; add_change(key , gui_style      ); break;
                    case "GUI_FONT"       : gui_font       =                      val ; add_change(key , gui_font       ); break;
                    case "GUI_TYPE"       : gui_type       =                      val ; add_change(key , gui_type       ); break;
                    case "BAND_HIDE_WIDTH": BAND_HIDE_WIDTH= Integer.parseInt    (val); add_change(key , BAND_HIDE_WIDTH); break;
                    case "MARK_SCALE_GROW": MARK_SCALE_GROW= Float  .parseFloat  (val); add_change(key , MARK_SCALE_GROW); break;
                    case "MARK_EDIT_DELAY": MARK_EDIT_DELAY= Integer.parseInt    (val); add_change(key , MARK_EDIT_DELAY); break;
                    case "BG_VIEW_DEFAULT": BG_VIEW_DEFAULT= Color.parseColor    (val); add_change(key , BG_VIEW_DEFAULT); break;
                    case "SOUND_CLICK"    : SOUND_CLICK    =                      val ; add_change(key , SOUND_CLICK    ); break;
                    case "SOUND_DING"     : SOUND_DING     =                      val ; add_change(key , SOUND_DING     ); break;
                    // }}}
                }
            } catch(Exception ex) { add_changex( args[i], ex.getMessage()); }

        }
        //}}}

        // ENFORCE UPPER MANAGEMENT DIRECTIVES  {{{

        // .. i.e. install-level logging exclusion
        //if(!D)  LOGGING = false;
        // TODO find those flags: "release" / "debug" / "alpha" / "beta"

///SETTINGS*/ if(was_logging) {//TAG_SETTINGS
///SETTINGS*/  Settings.MON(TAG_SETTINGS,           "M=["+ M           +"]");
///SETTINGS*/  Settings.MON(TAG_SETTINGS, "was_logging=["+ was_logging +"]");
///SETTINGS*/  Settings.MON(TAG_SETTINGS,      "caller=["+ caller      +"]");
///SETTINGS*/ }//TAG_SETTINGS

        //}}}
        // POST KEY=VAL FORMATTING {{{

        // profile names may contain spaces, file won't (because I'm not working at MS! .. not looking for trouble)
        if(profile  != null) profile  = profile .replace(" ","_"); // normalize keyword syntax
        if(bm_title != null) bm_title = bm_title.replace("_"," "); // restore effective syntax
        if(lp_title != null) lp_title = lp_title.replace("_"," "); // restore effective syntax

        //}}}
        // REPORT WHAT HAS CHANGED {{{

        if(profile   != null) Settings.PROFILE        = profile  ;
        if(prodate   !=    0) Settings.PRODATE        = prodate  ;
        if(source    != null) Settings.SOURCE         = source   ;
        if(palette   != null) Settings.PALETTE        = palette  ;
        if(opacity   !=   -1) Settings.OPACITY        = opacity  ;
        if(maxcolors !=   -1) Settings.MAXCOLORS      = maxcolors;

        if(dev_dpi   !=    0) Settings.DEV_DPI        = dev_dpi  ;
    //  if(dev_zoom  !=    0) Settings.DEV_ZOOM       = dev_zoom ;
        if(txt_zoom  !=    0) Settings.TXT_ZOOM       = txt_zoom ;
        // dev_scale
        // dev_x
        // dev_y
        if(dev_w     !=    0) Settings.DEV_W          = dev_w    ;
        if(dev_h     !=    0) Settings.DEV_H          = dev_h    ;
        if(mon_dpi   !=    0) Settings.MON_DPI        = mon_dpi  ;
        if(mon_scale !=    0) Settings.MON_SCALE      = mon_scale;

        if(bm_title  != null) Settings.BOOKMARK_TITLE = bm_title ;
        if(bm_url    != null) Settings.BOOKMARK_URL   = bm_url   ;
        if(lp_title  != null) Settings.REQUESTED_TITLE= lp_title ;
        if(lp_url    != null) Settings.REQUESTED_URL  = lp_url   ;

        if(gui_style != null)          set_GUI_STYLE( gui_style );
        if(gui_font  != null)          set_GUI_FONT ( gui_font  );
        if(gui_type  != null)          set_GUI_TYPE ( gui_type  );

        //}}}
        // LOCK ON ADVERTISED PROFILE .. only if no working profile in effect on the receiving device {{{
        Track_last_Working_profile();

        //}}}
        D = D_was_set; // restore DEBUGGING state
        if(D) log( get_change_logs(caller) );
//*SETTINGS*/else Settings.MOM(TAG_SETTINGS, get_change_logs(caller));

        return get_change_list();
    }
    //}}}
    // [set_KEY_VAL] .. (track changes made by each call) {{{
    private                       static StringBuilder key_logs = new StringBuilder();
/*SETTINGS..TAG_SETTINGS*/private static String get_change_logs(String caller) { return (key_list.size() > 1) ? "["+caller+"]...\n"+key_logs.toString() : key_logs.toString(); }

    private static List<String>         key_list   = new ArrayList<>();
    private static List<String>  get_change_list() { return key_list; }
    private static void del_changes()                        { key_list.clear ();                          if(D) key_logs.delete(0, key_logs.length()); }
    private static void add_change (String key,     int val) { key_list.add(key); int c = key_list.size(); if(D) key_logs.append( String.format("o %2d o %16s = %d\n", c, key, val) ); }
    private static void add_change (String key,   float val) { key_list.add(key); int c = key_list.size(); if(D) key_logs.append( String.format("o %2d o %16s = %f\n", c, key, val) ); }
    private static void add_change (String key,  String val) { key_list.add(key); int c = key_list.size(); if(D) key_logs.append( String.format("o %2d o %16s = %s\n", c, key, val) ); }
    private static void add_change (String key, boolean val) { key_list.add(key); int c = key_list.size(); if(D) key_logs.append( String.format("o %2d o %16s = %b\n", c, key, val) ); }
    private static void add_changex(String arg,  String msg) {                                             if(D) key_logs.append( String.format("*** %16s : %s\n", arg, msg) ); }

    //}}}
    // can_parse_KEY_VAL {{{
    public static boolean can_parse_KEY_VAL(String argLine)
    {
        if( !argLine.contains("=") ) return false;

        boolean diag
            =  argLine.contains("DEV_DPI="        )
            || argLine.contains("DEV_H="          )
            || argLine.contains("DEV_W="          )
            || argLine.contains("DEV_ZOOM="       )

            || argLine.contains("MAXCOLORS="      )

            || argLine.contains("MON_DPI="        ) // DESIGNER SIDE ONLY
            || argLine.contains("MON_SCALE="      ) // DESIGNER SIDE ONLY

            || argLine.contains("GUI_STYLE="      ) // DEVICE SIDE ONY
            || argLine.contains("GUI_TYPE="       ) // DEVICE SIDE ONY
            || argLine.contains("GUI_FONT="       ) // DEVICE SIDE ONY

            || argLine.contains("OOB_CMD="        )
            || argLine.contains("OPACITY="        )

            || argLine.contains("PALETTE="        )
            || argLine.contains("PROFILE="        )

            || argLine.contains("TXT_ZOOM="       )

            || argLine.contains("BAND_HIDE_WIDTH=")
            || argLine.contains("MARK_SCALE_GROW=")
            || argLine.contains("MARK_EDIT_DELAY=")
            || argLine.contains("BG_VIEW_DEFAULT=")
            || argLine.contains("SOUND_CLICK="    )
            || argLine.contains("SOUND_DING="     )

            ;

        if(D) log("Settings.can_parse_KEY_VAL("+ argLine +") ...return "+ diag);
        return diag;
    }
    //}}}
    // parse_OOB_CMD {{{
    public static void parse_OOB_CMD(String argLine)
    {
        while(argLine.indexOf("  ") > 0) argLine = argLine.replace("  ", " ");

        String[] args = argLine.split(" ");
        //if(D) log("Settings.parse_OOB_CMD(args#="+ args.length +"):");

        for(int i=0; i<args.length; ++i)
        {
            String[] kv = args[i].split("=");
            if(kv.length == 2)
                try{ if(kv[0].toUpperCase().equals("OOB_CMD")) { OOB_CMD=kv[1]; } } catch(Exception ignored) {}
        }
        //if(D) log("...OOB_CMD=["+OOB_CMD+"]");
    }
    //}}}
    // Get_APP_KEY_VAL {{{
    private static String Get_APP_KEY_VAL()
    {

        return
            /**/       "# SAVE_CALLER="+ SAVE_CALLER                            +"\n"
            /**/ +          "# SOURCE="+ get_APP_NAME()                         +"\n"

            // PROFILE
            /**/ +         "# OPACITY="+ OPACITY                                +"\n"
            /**/ +         "# PALETTE="+ PALETTE                                +"\n"
            /**/ +         "# PRODATE="+ Working_profile_prodate                +"\n"
            /**/ +         "# PROFILE="+ Working_profile                        +"\n"

            // SOUND
            /**/ +      "# SOUND_DING="+ SOUND_DING     .replace(" ", "_")      +"\n"
            /**/ +     "# SOUND_CLICK="+ SOUND_CLICK    .replace(" ", "_")      +"\n"

            // GUI
            /**/ +        "# GUI_FONT="+ GUI_FONT                               +"\n"
            /**/ +        "# GUI_TYPE="+ GUI_TYPE                               +"\n"
            /**/ +       "# GUI_STYLE="+ GUI_STYLE                              +"\n"
            /**/ + "# BAND_HIDE_WIDTH="+ BAND_HIDE_WIDTH                        +"\n"
            /**/ + "# MARK_SCALE_GROW="+ String.format("%.1f" ,MARK_SCALE_GROW) +"\n"
            /**/ + "# MARK_EDIT_DELAY="+ MARK_EDIT_DELAY                        +"\n"
            /**/ + "# BG_VIEW_DEFAULT="+ String.format("#%06x",BG_VIEW_DEFAULT) +"\n"

            // DEV
            /**/ +           "# DEV_H="+ DEV_H                                  +"\n"
            /**/ +           "# DEV_W="+ DEV_W                                  +"\n"
            /**/ +         "# DEV_DPI="+ DEV_DPI                                +"\n"
            /**/ +        "# DEV_ZOOM="+ DEV_ZOOM                               +"\n"
            /**/ +       "# DEV_SCALE="+ DEV_SCALE                              +"\n"

            // DESIGNER
            /**/ +         "# MONITOR="+ MONITOR                                +"\n"
            /**/ +         "# MON_DPI="+ MON_DPI                                +"\n"
            /**/ +        "# TXT_ZOOM="+ TXT_ZOOM                               +"\n"
            /**/ +       "# MON_SCALE="+ MON_SCALE                              +"\n"

            // LOG
            /**/ +         "# LOGGING="+ LOGGING                                +"\n"
            /**/ +         "# LOG_CAT="+ LOG_CAT                                +"\n"
            /**/ +         "# LOG_FLT="+ LOG_FLT                                +"\n"

            // TOOL-UNICODE
            /**/ +  "# UNICODE_PRESET="+ UNICODE_PRESET                         +"\n"
            /**/ +   "# UNICODE_SHEET="+ UNICODE_SHEET                          +"\n"

            // SERVER
            /**/ +       "# SERVER_IP="+ SERVER_IP                              +"\n"
            /**/ +      "# SERVER_MAC="+ SERVER_MAC                             +"\n"
            /**/ +     "# SERVER_PASS="+ SERVER_PASS                            +"\n"
            /**/ +     "# SERVER_PORT="+ SERVER_PORT                            +"\n"
            /**/ +   "# SERVER_SUBNET="+ SERVER_SUBNET                          +"\n"

            // NAV
            /**/ + "# TOOL_URL_ACTION="+ TOOL_URL_ACTION                        +"\n"
            /**/ +   "# REQUESTED_URL="+ REQUESTED_URL                          +"\n"
            /**/ + "# REQUESTED_TITLE="+ REQUESTED_TITLE.replace(" ", "_")      +"\n"
            /**/ +    "# BOOKMARK_URL="+ BOOKMARK_URL                           +"\n"
            /**/ +  "# BOOKMARK_TITLE="+ BOOKMARK_TITLE .replace(" ", "_")      +"\n"
            /**/ +       "# WEBGROUPS="+ WebGroup.ToCSV()                       +"\n"

            ;
    }
    //}}}

    /** BROWSER */
    // BROWSER nicknames {{{
    public static final String BROWSER_CHROME_NICKNAME  =  "CHROME";
    public static final String BROWSER_DOLPHIN_NICKNAME = "DOLPHIN";
    public static final String BROWSER_FLASH_NICKNAME   =   "FLASH";
    public static final String BROWSER_OPERA_NICKNAME   =   "OPERA";

    //}}}
    // Get_key_value_from_text {{{
    public  static String Get_key_value_from_text(String argLine, String key)
    {
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "Get_key_value_from_text");
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "...argLine=["+argLine+"]");
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, ".......key=["+key+"]");

        String val = null;

        //............................// argLine = argLine.replace("\n", " ");
        //............................// argLine = argLine.replace("(" , " ");
        //............................// argLine = argLine.replace(")" , " ");
        //............................// argLine = argLine.replace("." , " ");
        /*............................*/ argLine = argLine.replaceAll("[^a-z=A-Z0-9]", " ");
        /*............................*/ argLine = argLine.replace("\n", " ");
        while(argLine.indexOf("  ") > 0) argLine = argLine.replace("  ", " ");
//*WEBVIEW*/Settings.MOC(TAG_WEBVIEW, "...argLine=["+argLine+"]");

        String[] args = argLine.split(" ");
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "args#="+args.length+"");

        String[] kv={"",""};
        for(int i=0; i<args.length; ++i)
        {
            // [kv[0] kv[1] {{{
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "...args["+i+"]=["+args[i]+"]");

            int idx = args[i].indexOf('=');
            if(idx < 0) {
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "xxx [CONTAINS_NON_EQUAL_SIGN]");
                continue;
            }

            kv[0]= args[i].substring(0, idx  ).trim();
            kv[1]= args[i].substring(   idx+1).trim();
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, " kv[0]=["+kv[0]+"]");
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, " kv[1]=["+kv[1]+"]");

            //}}}
            String k = kv[0].toUpperCase();
            String v = kv[1];
            if( k.startsWith(key.toUpperCase()) )
            {
                val = v;
                break;
            }
        }
//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "Get_key_value_from_text(["+argLine+"] , ["+key+"]): ...return ["+ val +"]");
        return val;
    }
    //}}}
    // Expand_packageName {{{
    public static String Expand_packageName(String nickname)
    {
        String val = nickname;
        nickname   = nickname.toUpperCase();
        if     (nickname.contains( BROWSER_CHROME_NICKNAME  )) val = "com.android.chrome"     ;
        else if(nickname.contains( BROWSER_DOLPHIN_NICKNAME )) val = "mobi.mgeek.TunnyBrowser";
        else if(nickname.contains( BROWSER_FLASH_NICKNAME   )) val = "mobi.browser.flashfox"  ;
        else if(nickname.contains( BROWSER_OPERA_NICKNAME   )) val = "com.opera.mini.native"  ;

//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "Expand_packageName("+nickname+") ...return ["+val+"]");
        return val.trim();
    }
    //}}}
    // Expand_className {{{
    public static String Expand_className(String nickname)
    {
        String val = nickname;
        nickname   = nickname.toUpperCase();
        if     (nickname.contains( BROWSER_CHROME_NICKNAME  )) val = "com.google.android.apps.chrome.Main"    ;
        else if(nickname.contains( BROWSER_DOLPHIN_NICKNAME )) val = "mobi.mgeek.TunnyBrowser.BrowserActivity";
        else if(nickname.contains( BROWSER_FLASH_NICKNAME   )) val = "mobi.browser.flashfox.App"              ;
        else if(nickname.contains( BROWSER_OPERA_NICKNAME   )) val = "com.opera.mini.android.Browser"         ;

//*WEBVIEW*/Settings.MOM(TAG_WEBVIEW, "Expand_className("+nickname+") ...return ["+val+"]");
        return val.trim();
    }
    //}}}

    /** SETTINGS */
    //{{{
    // {{{
    public  static boolean is_Build_VERSION_SDK_HONEYCOMB     = false;
    public  static boolean is_Build_VERSION_SDK_HONEYCOMB_MR2 = false;
    public  static boolean is_Build_VERSION_SDK_KITKAT        = false;
    public  static boolean is_Build_VERSION_SDK_LOLLIPOP      = false;
    public  static boolean is_Build_VERSION_SDK_M             = false;
    public  static boolean is_Build_VERSION_SDK_N             = false;
    public  static boolean is_Build_VERSION_SDK_N_MR1         = false;
    public  static boolean is_Build_VERSION_SDK_O             = false;
    public  static boolean is_Build_VERSION_SDK_O_MR1         = false;
    public  static boolean is_Build_VERSION_SDK_P             = false;
    public  static boolean is_Build_VERSION_SDK_Q             = false;

    // }}}
    // Settings_class_init {{{
    public  static     int SCALED_TOUCH_SLOP        = 0;
    private static boolean Settings_class_init_done = false;
    public  static    File EXTERNAL_FILES_DIR;
    public  static    File EMULATED_FILES_DIR;

    public static void    Settings_class_init()
    {
//*SETTINGS*/String caller = "Settings_class_init";//TAG_SETTINGS

        if(  Settings_class_init_done ) return;
        else Settings_class_init_done = true;

        // INIT: [BUILD SDK VERSION AWARENESS]
        Settings.is_Build_VERSION_SDK_HONEYCOMB     = Settings.is_Build_VERSION_SDK_HONEYCOMB     ();
        Settings.is_Build_VERSION_SDK_HONEYCOMB_MR2 = Settings.is_Build_VERSION_SDK_HONEYCOMB_MR2 ();
        Settings.is_Build_VERSION_SDK_KITKAT        = Settings.is_Build_VERSION_SDK_KITKAT        ();
        Settings.is_Build_VERSION_SDK_LOLLIPOP      = Settings.is_Build_VERSION_SDK_LOLLIPOP      ();
        Settings.is_Build_VERSION_SDK_M             = Settings.is_Build_VERSION_SDK_M             ();
        Settings.is_Build_VERSION_SDK_N             = Settings.is_Build_VERSION_SDK_N             ();
        Settings.is_Build_VERSION_SDK_N_MR1         = Settings.is_Build_VERSION_SDK_N_MR1         ();
        Settings.is_Build_VERSION_SDK_O             = Settings.is_Build_VERSION_SDK_O             ();
        Settings.is_Build_VERSION_SDK_O_MR1         = Settings.is_Build_VERSION_SDK_O_MR1         ();
        Settings.is_Build_VERSION_SDK_P             = Settings.is_Build_VERSION_SDK_P             ();
        Settings.is_Build_VERSION_SDK_Q             = Settings.is_Build_VERSION_SDK_Q             ();

        // INIT: [DEVICE SPECIFICS]
        init_DIP();

        // INIT: UTF CONSTANTS
        CHARS_SYMBOL_BLACK_STAR     = new String( Character.toChars( 0x2605) );
        CHARS_SYMBOL_GEAR           = new String( Character.toChars( 0x2699) );
        CHARS_SYMBOL_HOME           = new String( Character.toChars( 0x2302) );
        CHARS_SYMBOL_INDEX          = new String( Character.toChars( 0x24D8) );
        CHARS_SYMBOL_WHITE_STAR     = new String( Character.toChars( 0x2606) );
        CHARS_SYMBOL_BLACK_CIRCLE   = new String( Character.toChars( 0x26AB) );
        CHARS_SYMBOL_KEYBOARD       = new String( Character.toChars( 0x2328) );
        CHARS_SYMBOL_PENCIL         = new String( Character.toChars( 0x270F) );
        CHARS_SYMBOL_COMPUTER       = new String( Character.toChars(0x1F4BB) );

        // DEVICE DEPENDENT INFORMATION
        SCALED_TOUCH_SLOP           = ViewConfiguration.get( RTabs.activity ).getScaledTouchSlop();
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "...SCALED_TOUCH_SLOP=["+SCALED_TOUCH_SLOP+"]");

        // EXTERNAL STORAGE DIRS
        File[] dirs = RTabs.activity.getExternalFilesDirs(null);
        for(int i=0; i<dirs.length; ++i) {
            if( dirs[i].getPath().contains("emulated") ) EMULATED_FILES_DIR = dirs[i];
            else                                         EXTERNAL_FILES_DIR = dirs[i];
        }

        SOUND_DIR                   = Get_Profiles_dir() +"/SOUNDS/";
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "...SOUND_DIR=["+SOUND_DIR+"]");
    }
    //}}}
    // [Build.VERSION_CODES] {{{

    public static boolean is_Build_VERSION_SDK_BASE()                   { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.BASE;                   } //  1
    public static boolean is_Build_VERSION_SDK_BASE_1_1()               { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.BASE_1_1;               } //  2
    public static boolean is_Build_VERSION_SDK_CUPCAKE()                { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.CUPCAKE;                } //  3
    public static boolean is_Build_VERSION_SDK_DONUT()                  { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.DONUT;                  } //  4
    public static boolean is_Build_VERSION_SDK_ECLAIR()                 { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.ECLAIR;                 } //  5
    public static boolean is_Build_VERSION_SDK_ECLAIR_0_1()             { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.ECLAIR_0_1;             } //  6
    public static boolean is_Build_VERSION_SDK_ECLAIR_MR1()             { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.ECLAIR_MR1;             } //  7
    public static boolean is_Build_VERSION_SDK_FROYO()                  { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.FROYO;                  } //  8
    public static boolean is_Build_VERSION_SDK_GINGERBREAD()            { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.GINGERBREAD;            } //  9
    public static boolean is_Build_VERSION_SDK_GINGERBREAD_MR1()        { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.GINGERBREAD_MR1;        } // 10
    public static boolean is_Build_VERSION_SDK_HONEYCOMB()              { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.HONEYCOMB;              } // 11
    public static boolean is_Build_VERSION_SDK_HONEYCOMB_MR1()          { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.HONEYCOMB_MR1;          } // 12
    public static boolean is_Build_VERSION_SDK_HONEYCOMB_MR2()          { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.HONEYCOMB_MR2;          } // 13
    public static boolean is_Build_VERSION_SDK_ICE_CREAM_SANDWICH()     { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.ICE_CREAM_SANDWICH;     } // 14
    public static boolean is_Build_VERSION_SDK_ICE_CREAM_SANDWICH_MR1() { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1; } // 15
    public static boolean is_Build_VERSION_SDK_JELLY_BEAN()             { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.JELLY_BEAN;             } // 16
    public static boolean is_Build_VERSION_SDK_JELLY_BEAN_MR1()         { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.JELLY_BEAN_MR1;         } // 17
    public static boolean is_Build_VERSION_SDK_JELLY_BEAN_MR2()         { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.JELLY_BEAN_MR2;         } // 18
    public static boolean is_Build_VERSION_SDK_KITKAT()                 { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.KITKAT;                 } // 19
    public static boolean is_Build_VERSION_SDK_KITKAT_WATCH()           { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.KITKAT_WATCH;           } // 20
    public static boolean is_Build_VERSION_SDK_LOLLIPOP()               { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.LOLLIPOP;               } // 21
    public static boolean is_Build_VERSION_SDK_LOLLIPOP_MR1()           { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.LOLLIPOP_MR1;           } // 22
    public static boolean is_Build_VERSION_SDK_M()                      { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.M;                      } // 23
    public static boolean is_Build_VERSION_SDK_N()                      { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.N;                      } // 24
    public static boolean is_Build_VERSION_SDK_N_MR1()                  { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.N_MR1;                  } // 25
    public static boolean is_Build_VERSION_SDK_O()                      { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.O;                      } // 26
    public static boolean is_Build_VERSION_SDK_O_MR1()                  { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.O_MR1;                  } // 27
    public static boolean is_Build_VERSION_SDK_P()                      { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.P;                      } // 28
    public static boolean is_Build_VERSION_SDK_Q()                      { return Build.VERSION.SDK_INT >=  Build.VERSION_CODES.Q;                      } // 29

/*
:!start explorer "http://developer.android.com/reference/android/os/Build.VERSION_CODES.html"
:!start explorer "https://source.android.com/source/build-numbers.html"
:!start explorer "http://developer.android.com/guide/topics/graphics/prop-animation.html#views"
:!start explorer "http://developer.android.com/guide/topics/graphics/prop-animation.html#value-animator"
:!start explorer "http://developer.android.com/guide/topics/graphics/prop-animation.html#object-animator"
:!start explorer "http://cogitolearning.co.uk/?p=1451"
*/

//}}}
    // SaveSettings {{{
    public static void SaveSettings(String caller)
    {
        boolean logging = (D || LOG_CAT);

        if(logging) log("Settings.SaveSettings: caller=["+caller+"]"+ Settings.TRACE_OPEN);

        // Jul 19, 2016 1:12:43 PM
        String prodate_str      = DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()));

        SAVE_CALLER = caller +" ("+prodate_str+")"; // [LoadSettings] make it empty .. until current session calls [SaveSettings]

        // LOOK FOR UNSAVED CHANGES {{{
        String s0 = ReadLastSavedSettings( caller );
        String s1 = Get_APP_KEY_VAL();
        if( s1.equals(s0) )
        {
            if(logging) MLog.log("NO SETTINGS CHANGE TO COMMIT .. (since ["+LastSavedSettingsTime+"])");
            return;
        }
        // REPORT FIRST CHANGED SEGMENT
        else if(logging)
        {
            boolean something_to_commit = TextUtils.isEmpty(s0); // sure, that's a first!

            String sub0, sub1;
            int    idx , idx0, idx1;

            int bol0 = 0;
            int bol1 = 0;
            int len0 = s0.length();
            int len1 = s1.length();

            for(idx0=0, idx1=0; idx0<len0 && idx1<len1; ++idx0, ++idx1)
            {
                // TRACK BEGINNING OF CURRENT LINES
                if(s0.charAt(idx0) == '#') bol0 = idx0;
                if(s1.charAt(idx1) == '#') bol1 = idx1;

                if(s0.charAt(idx0) != s1.charAt(idx1))
                {
                    // LOG DIFFERENCES {{{
                    idx = s0.indexOf('\n', idx0); if(idx < 0) idx = len0; sub0 = s0.substring(bol0, idx);
                    idx = s1.indexOf('\n', idx1); if(idx < 0) idx = len1; sub1 = s1.substring(bol1, idx);

                    //}}}
                    // INTERRUPT AT FIRST NOT TRANSCIENT DIFFERENCE  {{{
                    // Todo: (consider adding more acceptable settings differences)
                    if(        !sub0.startsWith("# SAVE_CALLER")
                            && !sub0.startsWith("# DEV_"       )
                            && !sub0.startsWith("# PALETTE"    )
                            && !sub0.startsWith("# PRODATE"    )
                            && !sub0.startsWith("# MON_"       )
                            && !sub0.startsWith("# BOKMARK_"   )
                            && !sub0.startsWith("# REQUESTED_" )
                      ) {
                        MLog.log("@@@ FOUND FUNCTIONAL SETTINGS CHANGE TO COMMIT .. (since ["+LastSavedSettingsTime+"]):");
                        MLog.log("@@@ FROM: ["+ sub0 +"]");
                        MLog.log("@@@   TO: ["+ sub1 +"]");
                        //break;
                        something_to_commit = true;
                      }
                    //}}}
                    // ...but skip acceptable differences {{{
                    MLog.log("<<<<      ["+ sub0 +"]");
                    MLog.log("     >>>> ["+ sub1 +"]");
                    idx = s0.indexOf('#', idx0+1); idx0 = (idx > idx0) ? idx : len0; // next record or EOF0
                    idx = s1.indexOf('#', idx1+1); idx1 = (idx > idx1) ? idx : len1; // next record or EOF1
                    bol0 = idx0;
                    bol1 = idx1;
                    //}}}
                }
            }
            // found no functional change to commit till the end of both settings records {{{
            if( !something_to_commit ) //if((idx0 == len0) && (idx1 == len1))
            {
                if(logging) MLog.log("NO FUNCTIONAL SETTINGS CHANGE TO COMMIT .. (since ["+LastSavedSettingsTime+"])");
                return;
            }
            //}}}
        }
        //}}}

        //if(logging) log(s1);

        try {
            File            file = new File(_Get_Settings_dir(), SETTINGS_FILENAME);
            FileOutputStream fos = new FileOutputStream( file );
            fos.write( s1.getBytes() );
            fos.close();
//*SETTINGS*/Settings.MON(TAG_SETTINGS, caller, "...Settings have been saved in ["+file+"]");
        }
        catch(Exception ex) { Settings.log_ex(ex, caller); }

        if(logging) log("----------------------------------------------"+ Settings.TRACE_CLOSE);
    }
    //}}}
    // LoadSettings {{{
    public static boolean LoadSettings(String caller)
    {
        caller += "->LoadSettings";
      //if(M) Settings.MON_IN("LoadSettings", caller);

        if(M) Settings.MOM(TAG_SETTINGS, caller + Settings.TRACE_OPEN);
        if(D) log("Settings.LoadSettings("+ caller +"):");

        // LOAD SAVED SETTINGS FROM FILE // {{{
        String saved_settings = ReadLastSavedSettings( caller );

        String[] lines = saved_settings.split("\n");
        for(int i=0; i < lines.length; ++i)
            set_KEY_VAL(lines[i], caller);

        WebGroup.FromCSV( WEBGROUPS );

        // }}}
        // THOSE WERE SYNCHRONIZED AT SAVE TIME // {{{
        Working_profile         = PROFILE;
        Working_profile_prodate = PRODATE;

        if(D || LOG_CAT) {
            log("Settings.LoadSettings("+ caller +"):");
            log( Settings.PrettyPrint() );
            if(M || LOGGING)
                MON_LOG("LoadSettings");
        }

        // }}}
        // gracefully terminate logging if logging has been changed as the result of this parsing // {{{
        apply_MONITOR();
        apply_LOG_FILTERS();
        // }}}

        if(M) Settings.MOM(TAG_SETTINGS, caller + Settings.TRACE_CLOSE);

      //if(M) Settings.MON_OUT("LoadSettings");
        SAVE_CALLER = ""; // [LoadSettings] make it empty .. until current session calls [SaveSettings]

        boolean first_app_launch = (saved_settings == "");
//*SETTINGS*/Settings.MON(TAG_SETTINGS, caller, "...returning first_app_launch["+first_app_launch+"]");
        return first_app_launch;
    }
    //}}}
    // ReadLastSavedSettings {{{
    public  static String LastSavedSettingsTime = "";
    public  static String ReadLastSavedSettings(String caller)
    {
        if(D) log("Settings.ReadLastSavedSettings("+ caller +"):");
        // ACCESS SETTINGS FILE {{{
        File file = new File(_Get_Settings_dir(), SETTINGS_FILENAME);
        if( !file.exists() ) {
            log("*** No "+ SETTINGS_FILENAME +" saved yet *");
            return "";
        }

        LastSavedSettingsTime = DateFormat.getDateTimeInstance().format(new Date( file.lastModified() ));
        //}}}
        // LOAD FILE LINES [# KEY_VAL] [PALETTES] [TABS) {{{
        InputStream  is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is                          = new BufferedInputStream(new FileInputStream(file));
            InputStreamReader   isr     = new InputStreamReader  ( is  );
            BufferedReader      br      = new BufferedReader     ( isr );
            String s;
            do {
                s = br.readLine();
                if(s != null)
                    sb.append(s).append("\n");
            }
            while(s != null);
            if(D) log("...Settings have been loaded from [" + file +"]");
        }
        catch(Exception ex) {
            log("*** ReadLastSavedSettings: Exception:\n"+ ex.toString());
        }
        finally {
            try { if(is != null) is.close(); } catch(Exception ignored) {}
        }

        //}}}
        return sb.toString();
    }
    //}}}
    //}}}

    /** COOKIE */
    //{{{
    // {{{
    private static final   long                     EXPIRES_MS = 24L*3600L*1000L*365L;
    private static       String mCookieManager_last_cookie_url = null;

    // }}}
    // SaveCookies {{{
    public  static void SaveCookies(String caller)
    {
        boolean logging = (D || LOG_CAT); if(logging) log("Settings.SaveCookies: caller=["+caller+"]"+ Settings.TRACE_OPEN);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            _get_CookieManager().flush();

        // SAVE COOKIE SORTED LINES TO FILE
        String     url = Settings.LOCALHOST_URL;
        String[] lines = _get_cookie_lines( url );
        if(lines == null) {
            if(logging) log("*** (lines == null)");
        }
        else {
            StringBuilder sb = new StringBuilder();
            String      date = DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()));
            sb.append(Settings.FREE_TAG +" "+ caller +": "+date+"\n");                     // 012345678
            for(int i=0;      i <  lines.length; ++i)
            { //_____________________________________________________ // _KEY=P_KEY=P_VAL // CCC=PPP=Value
                String line = lines[i].trim();
                if( line.endsWith("=") ) continue;
                int idx         =  line.indexOf("=");             // =value
                if((idx>0)     && (line.indexOf('=', idx+1) > 0)) // =p_key_=p_val
                        sb.append(Settings.FREE_TAG +" "+ line +"\n");
            }
            try {
                File            file = new File(_Get_Settings_dir(), COOKIES_FILENAME);
                FileOutputStream fos = new FileOutputStream( file );
                fos.write( sb.toString().getBytes() );
                fos.close();
//*COOKIE*/Settings.MON(TAG_COOKIE, "SaveCookies", "...Cookies have been saved in ["+file+"]");
            }
            catch(Exception ex) {
                MLog.log("*** SaveCookies:\n"+ ex.getMessage());
            }
        }
        if(logging) log("Settings.SaveCookies("+ caller +")"+TRACE_CLOSE);
    }
    //}}}
    // LoadCookies {{{
    public static void LoadCookies(String caller)
    {
        caller += "->LoadCookies";
        boolean logging = (D || LOG_CAT); if(logging) log("Settings.LoadCookies: caller=["+caller+"]"+ Settings.TRACE_CLOSE);

        // LOAD SAVED COOKIE FROM FILE // {{{
        String   line_header = Settings.FREE_TAG+" "; int line_header_length = line_header.length();
        String saved_cookies = ReadLastSavedCookies( caller );
        String[]       lines = saved_cookies.split("\n");
        for(int i=0; i < lines.length; ++i)
        {
            String    line = lines[i];
            if(line.startsWith( line_header ) )
                line       = line.substring( line_header_length );

            int        idx = line.indexOf  (   '='    );
            if(idx > 0) {
                String key = line.substring(0, idx    );
                String val = line.substring(   idx + 1);
                if(logging) log( String.format("%32s = [%s]"     , key     , val));
                _mCookieManager_set_cookie(Settings.LOCALHOST_URL, key +"="+ val );
            }
        }
        // }}}
        if(logging) log("Settings.LoadCookies("+ caller +")"+TRACE_CLOSE);
    }
    //}}}
    // ReadLastSavedCookies {{{
    public  static String LastSavedCookiesTime = "";
    public  static String ReadLastSavedCookies(String caller)
    {
        if(D) log("Settings.ReadLastSavedCookies("+ caller +"):");
        // ACCESS COOKIE FILE {{{
        File file = new File(_Get_Settings_dir(), COOKIES_FILENAME);
        if( !file.exists() ) {
            log("*** No "+ COOKIES_FILENAME +" saved yet *");
            return "";
        }

        LastSavedCookiesTime = DateFormat.getDateTimeInstance().format(new Date( file.lastModified() ));
        //}}}
        // LOAD FILE LINES [# KEY_VAL] {{{
        InputStream  is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is                          = new BufferedInputStream(new FileInputStream(file));
            InputStreamReader   isr     = new InputStreamReader  ( is  );
            BufferedReader      br      = new BufferedReader     ( isr );
            String s;
            do {
                s = br.readLine();
                if(s != null)
                    sb.append(s).append("\n");
            }
            while(s != null);
            if(D) log("...Cookies have been loaded from [" + file +"]");
        }
        catch(Exception ex) {
            log("*** ReadLastSavedCookies: Exception:\n"+ ex.toString());
        }
        finally {
            try { if(is != null) is.close(); } catch(Exception ignored) {}
        }

        //}}}
        return sb.toString();
    }
    //}}}
    // (set_cookie) (get_cookie) (removeAllCookies {{{
    public  static void   set_cookie_param      (String url, String key, String  p_key, String p_val) {        _mCookieManager_set_cookie_param      (url, key, p_key, p_val); }
    public  static String get_cookie_param      (String url, String key, String  p_key              ) { return _mCookieManager_get_cookie_param      (url, key, p_key       ); }
    public  static String get_cookies_html_table(String url                                         ) { return                _get_cookies_html_table(url                   ); }
    public  static void   removeAllCookies      (                                                   ) {        _mCookieManager_removeAllCookies      (                      ); }
    // }}}
    // del_cookie {{{
    public static void del_cookie(String url, String cookie_key)
    {
        String caller  = "del_cookie("+url+", cookie_key=["+cookie_key+"])";
//*COOKIE*/Settings.MOM(TAG_COOKIE, caller);

        String cookie = cookie_key+"=";
        _mCookieManager_set_cookie(url, cookie);
    }
    //}}}
    // _get_p_key_val_Array {{{
    private static String[] _get_p_key_val_Array(String p_key_val_line)
    {
        String caller =            "_get_p_key_val_Array("+p_key_val_line+")";
//COOKIE//Settings.MOC(TAG_COOKIE, caller);

        if(p_key_val_line == null)
        {
//COOKIE//Settings.MOM(TAG_COOKIE, caller+" (p_key_val_line == null) ...return null");
            return null;
        }

        ArrayList<String>kv_list = new ArrayList<>();

        String[] args = p_key_val_line.split(" "); // .. (key=key1=val1 key2=val2)
        for(int i=0; i<args.length; ++i)
        {
            int idx = args[i].indexOf('=');
            if(idx < 0) continue;
            String kv = args[i];//.replaceFirst("|", "=");
//COOKIE//Settings.MOM(TAG_COOKIE, String.format(". %2d [%s]", i+1, kv));
            kv_list.add( kv );
        }

//*COOKIE*/Settings.MOM(TAG_COOKIE, caller+" ...return a list of "+kv_list.size()+" KEY_VAL pairs");
        return kv_list.toArray(new String[kv_list.size()]);
    }
    //}}}
    // _encode_p_val {{{
    private static String _encode_p_val(String _p_val)
    {
        String p_val= _p_val
            .replace( "\r" , "\\r" )
            .replace( "\n" , "\\n" )
            ;

        try {  p_val = URLEncoder.encode(p_val, "UTF-8"); } catch(Exception ignored) {}

//*COOKIE*/if(p_val != null) Settings.MOM(TAG_COOKIE, "_encode_p_val("+_p_val+") ...return ["+p_val+"]");
        return p_val;
    }
    //}}}
    // _decode_p_val {{{
    private static String _decode_p_val(String _p_val)
    {
        String p_val= _p_val;
        try {  p_val = URLDecoder.decode(p_val, "UTF-8"); } catch(Exception ignored) {}

        p_val= p_val
            .replace( "\\r" , "\r" )
            .replace( "\\n" , "\n" )
            ;

//*COOKIE*/if(p_val != null) Settings.MOM(TAG_COOKIE, "_decode_p_val("+_p_val+") ...return ["+p_val+"]");
        return p_val;
    }
    //}}}
    //* CookieManager */
    // _get_CookieManager {{{
    private static CookieManager _get_CookieManager()
    {
//COOKIE//Settings.MOC(TAG_COOKIE, "_get_CookieManager");
        //if(mCookieManager != null) return;

        CookieManager mCookieManager = CookieManager.getInstance();
        mCookieManager.setAcceptCookie            ( true );

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            CookieManager.setAcceptFileSchemeCookies ( true );

        return mCookieManager;
    }
    //}}}
    // _mCookieManager_ValueCallback {{{
    private static ValueCallback<Boolean> _mCookieManager_ValueCallback = new ValueCallback<Boolean>()
    {
        @Override public void onReceiveValue(Boolean cookie_has_been_set_successfully) {
//COOKIE//Settings.MOM(TAG_COOKIE, "onReceiveValue: cookie_has_been_set_successfully=["+cookie_has_been_set_successfully+"]");
            CookieManager mCookieManager = _get_CookieManager();
//COOKIE//Settings.MOM(TAG_COOKIE, "getCookie("+mCookieManager_last_cookie_url+")=["+mCookieManager.getCookie(mCookieManager_last_cookie_url)+"]");
        }
    };
    // }}}
    // _get_cookie_lines {{{
    private  static String[] _get_cookie_lines(String url)
    {
        // SORTED LINES OF [key=val]
        String cookies = _get_CookieManager().getCookie( url );
        if( TextUtils.isEmpty( cookies ) )
            return null;

        String[] lines = _get_CookieManager().getCookie( url ).split("; ");
        Arrays.sort( lines );

        return lines;
    }
    //}}}
    // _get_cookies_html_table {{{
    private static String _get_cookies_html_table(String url)
    {
        // [_get_cookie_lines] {{{
        String[] lines = _get_cookie_lines( url );
        if(lines == null)
            return "<b style='color:red;font-size:300%;'>NO COOKIES YET<br>for <u>"+url+"</u></b>";

        //}}}
        // return HTML.TABLE of [key val] lines {{{
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < lines.length; ++i)
        {
            sb.append("<tr><th>");
            sb.append( lines[i].replace("=", "</th><td>").replace("_"," ").replace("+"," "));
            sb.append("</td></tr>\n" );
        }

        return "<b><u>Cookies currently used by "+Settings.APP_NAME+" WebView instances:</u><b><br>"
            + "<table style='border:1px;'>\n"
            +  sb.toString()
            + "</table>";
        // }}}
    }
    //}}}
    // _mCookieManager_set_cookie {{{
    private static void _mCookieManager_set_cookie(String url, String cookie)
    {
        // EXPIRES .. f(val empty) {{{
        String caller  = "_mCookieManager_set_cookie("+url+", cookie=["+cookie+"])";
//*COOKIE*/Settings.MOM(TAG_COOKIE, caller);

        boolean key_has_no_val = cookie.trim().endsWith("=");
//*COOKIE*/if(key_has_no_val) Settings.MOM(TAG_COOKIE, "...key_has_no_val=["+key_has_no_val+"]");

        String expires
            = (TextUtils.isEmpty(cookie) || key_has_no_val)
            ? "; expires="+DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()))
            : "; expires="+DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis() + EXPIRES_MS));

        cookie += expires;

        //}}}
        // STORE COOKIE {{{
        mCookieManager_last_cookie_url = url; // TODO? ValueCallback joiner...

        CookieManager mCookieManager = _get_CookieManager();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) mCookieManager.setCookie(url, cookie, _mCookieManager_ValueCallback);
        else                                                      mCookieManager.setCookie(url, cookie);

//COOKIE//cookie = mCookieManager.getCookie( url );//TAG_COOKIE
//COOKIE//Settings.MOM(TAG_COOKIE, ":\n@@@ COOKIE"+TRACE_OPEN+"\n@@@ "+cookie.replace("; ","\n@@@ ")+"\n@@@ COOKIE"+TRACE_CLOSE);
        //}}}
    }
    //}}}
    // _mCookieManager_get_cookie {{{
    private static String _mCookieManager_get_cookie(String url, String key)
    {
        String caller  = "_mCookieManager_get_cookie("+url+", key=["+key+"])";
//COOKIE//Settings.MOM(TAG_COOKIE, caller);

        // access cookie store {{{
        CookieManager mCookieManager = _get_CookieManager();

        String cookie = mCookieManager.getCookie( url );

        //}}}
        String val = null;
        if( !TextUtils.isEmpty(cookie) )
        {
//COOKIE//Settings.MOM(TAG_COOKIE, caller+":\n...cookie:\n"+cookie.replace("; ","\n"));
            // split cookie {{{
            String[] args = cookie.split("; ");
//COOKIE//Settings.MOM(TAG_COOKIE, "args#="+args.length+"");

            String[] kv={"",""};
            //}}}
            for(int i=0; i<args.length; ++i)
            {
                // [kv[0] kv[1] {{{
//COOKIE//Settings.MOM(TAG_COOKIE, "...args["+i+"]=["+args[i]+"]");

                int idx = args[i].indexOf('=');
                if(idx < 0) {
//COOKIE//Settings.MOM(TAG_COOKIE, "xxx [CONTAINS_NON_EQUAL_SIGN]");
                    continue;
                }

                kv[0]= args[i].substring(0, idx  ).trim();
                kv[1]= args[i].substring(   idx+1).trim();
//COOKIE//Settings.MOM(TAG_COOKIE, " kv[0]=["+kv[0]+"]");
//COOKIE//Settings.MOM(TAG_COOKIE, " kv[1]=["+kv[1]+"]");

                //}}}
                // update key=val {{{
                String k = kv[0].toUpperCase();
                String v = kv[1];
                if( k.equals(key.toUpperCase()) ) {
                    val = kv[1];
                    //break;
                }
                //}}}
            }
        }
//*COOKIE*/if(val != null) Settings.MOM(TAG_COOKIE, caller+" ...return ["+val+"]");
        return val;
    }
    //}}}
    // _mCookieManager_get_cookie_param {{{
    private static String  _mCookieManager_get_cookie_param(String url, String cookie_key, String p_key)
    {
        // [cookie_found] <- [p_key] {{{
        String caller  = "_mCookieManager_get_cookie_param("+url+", cookie_key=["+cookie_key+"], p_key=["+p_key+"])";
//COOKIE//Settings.MOM(TAG_COOKIE, caller);

        // [cookie_found] <- [p_key]
        String        cookie_val  = _mCookieManager_get_cookie(url, cookie_key);
        String[] p_key_val_Array  = _get_p_key_val_Array( cookie_val );
        boolean     cookie_found  = false;
        int          param_found  = 0;
        if(      p_key_val_Array != null) {
            for( ;   param_found  < p_key_val_Array.length; ++param_found) {
                if( p_key_val_Array[param_found].startsWith(p_key+"=") ) {
                    cookie_found = true;
                    break;
                }
            }
        }
        //}}}
        // [p_key]+[p_val] {{{
        String p_val = null;
        if( cookie_found )
        {
            int   idx = p_key_val_Array[param_found].indexOf  ('='    );
            if(idx > 0)
                p_val = p_key_val_Array[param_found].substring(idx + 1);
        }
//*COOKIE*/if(p_val!=null)Settings.MOM(TAG_COOKIE, "...found (encoded) p_val=["+p_val+"]");
        // }}}
        // [p_val] .. (decoded from this point on)
        //{{{
        if(p_val != null)
            p_val = _decode_p_val( p_val );

//*COOKIE*/if(p_val!=null)Settings.MOM(TAG_COOKIE, caller+" ...return (decoded) ["+p_val+"]");
        //}}}
        return p_val;
    }
    //}}}
    // _mCookieManager_set_cookie_param {{{
    private static void  _mCookieManager_set_cookie_param(String url, String cookie_key, String p_key, String p_val)
    {
        // [cookie_found] <- [p_key] {{{
        String caller  = "_mCookieManager_set_cookie_param("+url+", cookie_key=["+cookie_key+"], p_key=["+p_key+"], p_val=["+p_val+"])";
//*COOKIE*/Settings.MOM(TAG_COOKIE, caller);

        String        cookie_val  = _mCookieManager_get_cookie(url, cookie_key);
        String[] p_key_val_Array  = _get_p_key_val_Array( cookie_val );
        boolean     cookie_found  = false;
        int          param_found  = 0;
        if(      p_key_val_Array != null) {
            for( ;   param_found  < p_key_val_Array.length; ++param_found) {
                if( p_key_val_Array[param_found].startsWith(p_key+"=") ) {
                    cookie_found = true;
                    break;
                }
            }
        }
        //}}}
        // [p_val] .. (encoded from this point on)
        //{{{
        p_val = _encode_p_val( p_val.trim() );

        //}}}
        // [p_key]+[p_val]<-[cookie_found] {{{
        boolean  val_changed = false;
        if( cookie_found )
        {
            int                  idx = p_key_val_Array[param_found].indexOf  ('='    );
            if(idx > 0)
            {
                String current_p_val = p_key_val_Array[param_found].substring(idx + 1).trim();

                // if changed, replace [current_p_val] by [val]
                if( !TextUtils.equals(p_val, current_p_val) )
                {
                    p_key_val_Array[param_found] = p_key+"="+p_val;
                    val_changed = true;
                }
            }
        }
        // }}}
        // ...return (NO CHANGE) {{{
        if(cookie_found && !val_changed)
        {
//*COOKIE*/Settings.MOM(TAG_COOKIE, "COOKIE UNCHANGED");
            return;
        }
        //}}}
        // [cookie_val] (recollect) [p_key=p_val] {{{
        StringBuilder kv_sb = new StringBuilder();
        if(p_key_val_Array != null) {
            for(int i=0; i < p_key_val_Array.length; ++i)
                kv_sb.append(" "+p_key_val_Array[i]); //  (space separated pairs)
        }
        //}}}
        // [cookie_val] (add) [p_key=p_val] f(val not empty) {{{
        if( !cookie_found && !TextUtils.isEmpty(p_val.trim()) )
        {
            kv_sb.append(" "+ p_key.trim() +"="+ p_val.trim());

        }
        //}}}
        // COOKIE UPDATED .. [key] [params] {{{
        /*...*/ p_val = kv_sb.toString().trim(); // strip leading extra separator space
        String cookie = cookie_key+"="+p_val;

        _mCookieManager_set_cookie(url, cookie);
        //}}}
    }
    //}}}
    // _mCookieManager_removeAllCookies {{{
    private static void _mCookieManager_removeAllCookies()
    {
        String caller = "_mCookieManager_removeAllCookies";
//*COOKIE*/Settings.MOM(TAG_COOKIE, caller);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            CookieManager mCookieManager = _get_CookieManager();
            mCookieManager.removeAllCookies(null);
            mCookieManager.flush();
        }
    }
    //}}}

    //}}}

    /** CACHE */
    // clearCache {{{
    public static void ClearCache(           ) { ClearCache(0); }
    public static void ClearCache(int daysOld)
    {
        Settings.MOC(TAG_SETTINGS, "Deleting cache files"+((daysOld == 0) ? "" : (" older than "+ daysOld+" days")));

        int count = ClearCacheFolder(RTabs.activity.getCacheDir(), daysOld);

        Settings.MOC(TAG_SETTINGS, "..."+count+" files deleted");
    }
    //}}}
    // ClearCacheFolder {{{
    static int ClearCacheFolder(final File dir, final int numDays)
    {
        int count = 0;
        try {
            for(File child:dir.listFiles())
            {
                if( child.isDirectory() ) {
                    count += ClearCacheFolder(child, numDays); // recurse
                }
                if(child.lastModified() < new Date().getTime() - numDays * DateUtils.DAY_IN_MILLIS)
                {
                    Settings.MOM(TAG_SETTINGS, " ...deleting ["+child+"]");
                    if( child.delete() )
                        count += 1;
                }
            }
        }
        catch(Exception ex) { MLog.log("*** ClearCacheFolder: "+ ex.getMessage()); }

        return count;
    }
    //}}}

        // }}}

    /** NOTE */
    // LoadPersonalNote {{{
    public static String LoadPersonalNote()
    {
        // NOTE (from-to-file-only .. not cache anywhere)
        if(D) log("Settings.LoadPersonalNote:");
        // ACCESS PERSONAL_NOTE_FILENAME {{{
        File file = new File(Get_Profiles_dir(), PERSONAL_NOTE_FILENAME);
        if( !file.exists() ) {
            log("*** No "+ PERSONAL_NOTE_FILENAME +" saved yet *");
            return "";
        }

        //}}}
        // LOAD FILE LINES {{{
        InputStream is      = null;
        StringBuilder sb = new StringBuilder();
        try {
            is                          = new BufferedInputStream(new FileInputStream(file));
            InputStreamReader   isr     = new InputStreamReader  ( is  );
            BufferedReader      br      = new BufferedReader     ( isr );
            String s;
            do {
                s = br.readLine();
                if(s != null)
                    sb.append(s).append("\n");
            }
            while(s != null);
        }
        catch(Exception ex) {
            log("*** LoadPersonalNote: Exception:\n"+ ex.toString());
        }
        finally {
            try { if(is != null) is.close(); } catch(Exception ignored) {}
        }

        //}}}
        return sb.toString();
    }
    //}}}
    // SavePersonalNote {{{
    public static void SavePersonalNote(String user_note)
    {
        boolean logging = (D);

        if(logging) log("Settings.SavePersonalNote"+ Settings.TRACE_OPEN);

        if(logging) log(user_note);

        try {
            File            file = new File(Get_Profiles_dir(), PERSONAL_NOTE_FILENAME);
            FileOutputStream fos = new FileOutputStream( file );
            fos.write( user_note.getBytes() );
            fos.close();
        }
        catch(Exception ex) {
            log("*** SavePersonalNote ***\n" + ex.getMessage());
        }

        if(logging) log("----------------------------------------------"+ Settings.TRACE_CLOSE);
    }
    //}}}

    /** BUILTINS */
    // IsASettingsLine {{{
    private static boolean   IsASettingsLine(String argLine)
    {
        String      s = argLine;

        boolean  diag = false;
        if(         s.startsWith("#"       )) diag = true;
        if(!diag && s.startsWith("["       )) { int idx = s.indexOf("]"); if(idx > 0) s = s.substring(idx+1).trim(); }
        if(!diag && s.startsWith("PALETTE.")) diag = true;
        if(!diag && s.startsWith("TAB."    )) diag = true;

        if(D) log("IsASettingsLine("+ argLine +") ...return "+ diag);
        return diag;
    }
    //}}}
    // is_SETTINGS_BUILTIN {{{
    public static boolean is_SETTINGS_BUILTIN(String np_tag)
    {
        boolean diag = false;

        // APP
        if     (np_tag.equals    ( "FINISH"                )) diag = true;
        else if(np_tag.startsWith( CMD_LOGGING             )) diag = true;
        else if(np_tag.startsWith( CMD_SAVE                )) diag = true;

        // PROPERTIES .. more todo
        else if(np_tag.startsWith( RTabs.PROPERTY_MARK_SCALE_GROW)) diag = true;
        else if(np_tag.startsWith( RTabs.PROPERTY_MARK_EDIT_DELAY)) diag = true;

        // SERVER
        else if(np_tag.equals    ( "FREEZED"               )) diag = true;
        else if(np_tag.equals    ( "OFFLINE"               )) diag = true;
        else if(np_tag.equals    ( "SERVER"                )) diag = true;
        else if(np_tag.equals    ( "SIGNIN"                )) diag = true;
        else if(np_tag.equals    ( CMD_CLEAR               )) diag = true;
        else if(np_tag.equals    ( CMD_HIDE                )) diag = true;
        else if(np_tag.equals    ( CMD_OK                  )) diag = true;
        else if(np_tag.startsWith( CMD_PASSWORD            )) diag = true;
        else if(np_tag.startsWith( CMD_SIGNIN              )) diag = true;

        // COMM
        else if(np_tag.startsWith( "WOL"                   )) diag = true;

        // WEB
        else if(np_tag.equals    ( "DEL_COOKIES"           )) diag = true;
        else if(np_tag.equals    ( "SAVE_COOKIES"          )) diag = true;
        else if(np_tag.equals    ( "LOAD_COOKIES"          )) diag = true;

        // PALETTES
        else if(np_tag.equals    ( CMD_PALETTES_CLEAR      )) diag = true;
        else if(np_tag.equals    ( CMD_PALETTES_GET        )) diag = true;
        else if(np_tag.equals    ( CMD_PALETTES_LOAD       )) diag = true;
        else if(np_tag.startsWith( CMD_PALETTES_SETTINGS   )) diag = true;

        // TABS
        else if(np_tag.equals    ( CMD_TABS_CLEAR          )) diag = true;
        else if(np_tag.equals    ( CMD_TABS_GET            )) diag = true;
        else if(np_tag.equals    ( CMD_TABS_LOAD           )) diag = true;
        else if(np_tag.startsWith( CMD_TABS_SETTINGS       )) diag = true;

        // PROFILE
        else if(np_tag.equals    ( CMD_RELOAD              )) diag = true;
        else if(np_tag.startsWith( CMD_PROFILE+" "         )) diag = true;
        else if(np_tag.startsWith( "PROHIST "              )) diag = true;

        // DESKTOP
        else if(np_tag.startsWith( CMD_BROWSE              )) diag = true;
        else if(np_tag.startsWith( CMD_RUN                 )) diag = true;
        else if(np_tag.startsWith( CMD_SHELL               )) diag = true;

        else if(np_tag.startsWith( CMD_SENDKEYS            )) diag = true;
        else if(np_tag.startsWith( CMD_SENDINPUT           )) diag = true;

        // STATES
        else if(np_tag.equals    ( CMD_BEEP                )) diag = true;
        else if(np_tag.equals    ( CMD_CLOSE               )) diag = true;
        else if(np_tag.equals    ( CMD_STOP                )) diag = true;

        // handled by RTabs request_cmd_text
        // {{{

        else if(np_tag.startsWith("xxx"                    )) diag = true;

        // DYNAMIC TABLES
        else if(np_tag.equals    ( "DOCKINGS_TABLE"        )) diag = true;
        else if(np_tag.equals    ( "PROFHIST_TABLE"        )) diag = true;
        else if(np_tag.equals    ( "PROFILES_TABLE"        )) diag = true; // CONTROLS_TABLE TO PROFILES_TABLE
        else if(np_tag.equals    ( "SOUNDS_TABLE"          )) diag = true;

        // PROFILE
        else if(np_tag.equals    ( "PROFILES"              )) diag = true;
        else if(np_tag.equals    ( "PROFILETABS"           )) diag = true;
        else if(np_tag.equals    ( "PROFILE_DELETE"        )) diag = true;
        else if(np_tag.equals    ( "PROFILE_UNZIP"         )) diag = true;
        else if(np_tag.equals    ( "PROFILE_UNZIP_DEFAULTS")) diag = true;
        else if(np_tag.equals    ( "PROFILE_ZIP"           )) diag = true;
        else if(np_tag.equals    ( "PROFILE_ZIP_DELETE"    )) diag = true;
        else if(np_tag.equals    ( "PROFILE_SYNC"          )) diag = true;
        else if(np_tag.equals    ( "INVENTORY"             )) diag = true;
        else if(np_tag.equals    ( "WORKBENCH"             )) diag = true;

        // LOG
        else if(np_tag.equals    ( "LOG"                   )) diag = true;
        else if(np_tag.equals    ( "LOG_CAT"               )) diag = true;
        else if(np_tag.equals    ( "MONITOR"               )) diag = true;
        else if(np_tag.startsWith( "LOG_"                  )) diag = true;

        // GUI
        else if(np_tag.startsWith( CMD_GUI                 )) diag = true;
        else if(np_tag.equals    ( "FIT_W"                 )) diag = true;
        else if(np_tag.equals    ( "FIT_H"                 )) diag = true;
        else if(np_tag.equals    ( "LANDSCAPE"             )) diag = true;
        else if(np_tag.equals    ( "PORTRAIT"              )) diag = true;
        else if(np_tag.equals    ( "SCREEN_ROTATION"       )) diag = true;
        else if(np_tag.equals    ( "BGNEXT"                )) diag = true;
        else if(np_tag.equals    ( "TXNEXT"                )) diag = true;
        else if(np_tag.equals    ( "PLNEXT"                )) diag = true;
        else if(np_tag.equals    ( "PLPREV"                )) diag = true;
        else if(np_tag.equals    ( "PRNEXT"                )) diag = true;
        else if(np_tag.equals    ( "PRPREV"                )) diag = true;

        // INFO
        else if(np_tag.equals    ( "STATUS"                )) diag = true;
        else if(np_tag.equals    ( "SAVE"                  )) diag = true;
        else if(np_tag.equals    ( "NOTE"                  )) diag = true;

        // }}}

    //  else if(np_tag.startsWith(CMD_IMPORT               )) diag = true;
    //  else if(np_tag.startsWith(CMD_RESOLUTION           )) diag = true;

        if(D) log("is_SETTINGS_BUILTIN("+ np_tag +") ...return "+ diag);
        return diag;
    }
    //}}}
    // IsADashCmdLine {{{
    public static boolean IsADashCmdLine(String cmdLine)
    {
        boolean diag = false;
        if(cmdLine.startsWith(TESTS_DASH)) diag = true;

        return diag;
    }
    //}}}
    // CLIENT-SERVER INTERNAL COMMANDS {{{
    private static final String CMD_BEEP               = "BEEP";
    private static final String CMD_OK                 = "OK";

    private static final String CMD_CLEAR              = "CLEAR";               // SERVER TODO
    private static final String CMD_CLOSE              = "CLOSE";               // SERVER TODO
    private static final String CMD_HIDE               = "HIDE";                // SERVER TODO
    private static final String CMD_LOGGING            = "LOGGING";             // SERVER TODO
    private static final String CMD_STOP               = "STOP";                // SERVER TODO
    public  static final String CMD_POLL               = "POLL";                // SERVER TODO
    private static final String CMD_PASSWORD           = "PASSWORD";            // SERVER TODO
    private static final String CMD_SIGNIN             = "SIGNIN";              // SERVER TODO

    private static final String CMD_PALETTES_CLEAR     = "PALETTES_CLEAR";      // PALETTE TODO
    private static final String CMD_PALETTES_GET       = "PALETTES_GET";        // PALETTE TODO
    private static final String CMD_PALETTES_LOAD      = "PALETTES_LOAD";       // PALETTE TODO
    private static final String CMD_PALETTES_SETTINGS  = "PALETTES_SETTINGS";   // PALETTE TODO


    public  static final String CMD_PROFILE            = "PROFILE";             // PROFILE TODO
    public  static final String CMD_RELOAD             = "RELOAD";              // PROFILE TODO

    public  static final String CMD_BROWSE             = "BROWSE";              // SYMBOL_COMPUTER
    public  static final String CMD_RUN                =    "RUN";              // SYMBOL_COMPUTER
    public  static final String CMD_SHELL              =  "SHELL";              // SYMBOL_COMPUTER

    public  static final String CMD_SENDINPUT          = "SENDINPUT";           // SYMBOL_KEYBOARD
    public  static final String CMD_SENDINPUTTEXT      = "SENDINPUTTEXT";       // SYMBOL_KEYBOARD
    public  static final String CMD_SENDKEYS           = "SENDKEYS";            // SYMBOL_KEYBOARD
    public  static final String CMD_SENDKEYSTEXT       = "SENDKEYSTEXT";        // SYMBOL_KEYBOARD

    private static final String CMD_SAVE               = "SAVE";
    private static final String CMD_GUI                = "GUI_";

    private static final String CMD_TABS_CLEAR         = "TABS_CLEAR";          // SETTINGS TODO
    private static final String CMD_TABS_GET           = "TABS_GET";            // SETTINGS TODO
    private static final String CMD_TABS_LOAD          = "TABS_LOAD";           // SETTINGS TODO
    private static final String CMD_TABS_SETTINGS      = "TABS_SETTINGS";       // SETTINGS TODO

//  public  static final String CMD_IMPORT             = "IMPORT";
//  public  static final String CMD_RESOLUTION         = "RESOLUTION";

    //}}}

    /** UTIL */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ {{{
    //* DATE-TIME */
    //{{{
    private static int M_SEC = 60;         // seonds per minute
    private static int H_SEC = 60 * M_SEC; // seonds per hour
    private static int D_SEC = 24 * H_SEC; // seonds per day

    public  static String Get_time_elapsed(long ms_since_1970) // {{{
    {
        long ms_to = System.currentTimeMillis();

        return            Get_time_elapsed(     ms_since_1970 ,       ms_to);
    }
    //}}}
    public  static String Get_time_elapsed(long ms_from, long ms_to) // {{{
    {
        int s       = (int)(((ms_to > ms_from) ? (ms_to - ms_from) : (ms_from - ms_to)) / 1000);

        int d       =  s / D_SEC; s -= d * D_SEC;   // consume days    .. remains hours
        int h       =  s / H_SEC; s -= h * H_SEC;   // consume hours   .. remains minutes
        int m       =  s / M_SEC; s -= m * H_SEC;   // consume minutes .. remains seconds

        if(h==1) { h=0; m+=60; }                    // "1 min" .. "119 min"

        return Get_span_elapsed(d, h, m, s);
    }
    //}}}
    private static String Get_span_elapsed(int d, int h, int m, int s) // {{{
    {
        StringBuilder sb = new StringBuilder();

        if     (d > 730) sb.append(d / 365).append(" years " );
        else if(d > 365) sb.append(d / 365).append(" year "  );

        else if(d >  60) sb.append(d / 30 ).append(" months ");
        else if(d >  30) sb.append(d / 30 ).append(" month  ");

        else if(d >   1) sb.append(d      ).append(" days "  );
        else if(d >   0) sb.append(d      ).append(" day "   );

        else if(h >   1) sb.append(h      ).append(" hours " );
        else if(h >   0) sb.append(h      ).append(" hour "  );

        else if(m >   0) sb.append(m      ).append(" min "   );

        else             sb.append(s      ).append(" sec "   );

        return sb.toString().trim();
    }
    //}}}
    public  static String get_file_path_lastModified_date_str(String file_path) //{{{
    {
        File file = new File(file_path);
        return DateFormat.getDateTimeInstance().format(new Date( file.lastModified() ));
    }
    //}}}
    //}}}
    //* TIMERS */
    //{{{
    private static final HashMap<String, Long> Timer_Dict = new HashMap<>();
    // clearTimers {{{
    public static void clearTimers()
    {
        Timer_Dict.clear();
    }
    //}}}
    // startTimer {{{
    public static void startTimer(String name)
    {
        if( !Timer_Dict.containsKey(name) )
        {
            Timer_Dict.put(name          , 0L);
            Timer_Dict.put(name +"_start", 0L);
            Timer_Dict.put(name +"_stop" , 0L);
            Timer_Dict.put(name +"_cnt"  , 0L);
        }
        Timer_Dict.put(name +"_start", System.currentTimeMillis());
    }
    //}}}
    // stopTimer {{{
    public static void stopTimer(String name)
    {
        if( !Timer_Dict.containsKey(name) ) return;

        Timer_Dict.put(name+"_stop", System.currentTimeMillis());

        long start_time = Timer_Dict.get(name + "_start");
        long stop_time  = Timer_Dict.get(name + "_stop");

        long value = Timer_Dict.get(name);
        if(value != 0) {
            value += (stop_time - start_time);
            Timer_Dict.put(name, value);
        }
        else {
            Timer_Dict.put(name, (stop_time - start_time));
        }

        Timer_Dict.put(name +"_cnt", Timer_Dict.get(name + "_cnt") + 1);
    }
    //}}}
    // traceTimer {{{
    public static String traceTimer(String name)
    {
        if( !Timer_Dict.containsKey(name) )   return "";
        if(Timer_Dict.get(name +"_cnt") != 1) return name +" : "+ Timer_Dict.get(name) +" (for "+ Timer_Dict.get(name +"_cnt") +" calls)";
        else                                  return name +" : "+ Timer_Dict.get(name);
    }
    //}}}
    // }}}
    //* COLOR */
    // color_hex from tag {{{
    private static final Pattern ColorPattern = Pattern.compile("#[\\dA-Fa-f]{6,}");
    // Get_color_hex_from_text {{{
    public static String Get_color_hex_from_text(String text)
    {
        String color_hex = ""; // would have to be "0" if it were to comply with parseColor(@Size(min=1) String colorString)
        if(text.indexOf('#') >= 0)
        {
            Matcher  matcher = ColorPattern.matcher( text );
            if( matcher.find() )
            {
                color_hex = matcher.group();
                if(D) log("...Get_color_hex_from_text("+text+") ...return ["+color_hex+"]");
            }
        }
        return color_hex;
    }
    //}}}
    // Del_color_hex_from_text {{{
    public static String Del_color_hex_from_text(String text)
    {
        // make an exception with key-value kind of commands
        if( text.startsWith("PROPERTY")      ) return text;
        if( Settings.can_parse_KEY_VAL(text) ) return text;

        String color_hex = Get_color_hex_from_text( text );
        if(color_hex.length() > 0)
            text = text.replace(color_hex, "").trim();
        return text.trim();
    }
    //}}}
    //}}}
    // SetTextCursorColor {{{
    public static void SetEditTextCursorColor(TextView textView, int color)
    {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;
        try {
            Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);

            int mCursorDrawableRes = fCursorDrawableRes.getInt(textView);

            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);

            Object editor = fEditor.get(textView);
            Class<?> editor_class = editor.getClass();
            Field fCursorDrawable = editor_class.getDeclaredField("mCursorDrawable");

            fCursorDrawable.setAccessible(true);

            Drawable[] drawables = new Drawable[2];
            drawables[0] = textView.getContext().getResources().getDrawable(mCursorDrawableRes, null);
            drawables[1] = textView.getContext().getResources().getDrawable(mCursorDrawableRes, null);

            drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);

            fCursorDrawable.set(editor, drawables);
        }
        catch(Throwable ex) {
            log("*** SetEditTextCursorColor: ***\n"+ ex.getMessage());
        }
    }
    //}}}
    // Get_ECC_color_hex_from_text {{{
//{{{
/*

j0"*y$
\b[A-Z_a-z0-9]+([0-9])[A-Z_a-z]+
/\<[A-Z_a-z0-9]\+\([0-9]\)[A-Z_a-z]\+

*/
//}}}
    private static final Pattern WordDigitPattern = Pattern.compile("\\b[A-Z_a-z0-9]+([0-9])[A-Z_a-z0-9]+");
    private static final Pattern LoneDigitPattern = Pattern.compile("[ _]*([0-9])[0-9 _]*");
//----------------------------------------------------------------- start   head   digit   tail
    public static int Get_ECC_color_for_text(String text, String caller)
    {
        // EMBEDDED OR LONE DIGIT {{{
        Matcher matcher = WordDigitPattern.matcher( text );
        boolean   found = matcher.find();
        if(!found ) {
            matcher     = LoneDigitPattern.matcher( text );
            found       = matcher.find();
        }
        //}}}
        // [digit] [color] {{{
        int digit = 0;
        int color = 0;
        String matcher_group_1 = "";
        if( found ) {
            matcher_group_1    =                   matcher.group(1);
            try {      digit   = Integer.parseInt( matcher_group_1 ); } catch(Exception ex) { digit = 0; }
            color = COLORS_ECC[digit];
        }
        //}}}
//*SETTINGS*/if(digit != 0) Settings.MOC(TAG_SETTINGS, caller+"->Get_ECC_color_for_text("+text.replace("\n","\\n")+"): matcher_group_1=["+matcher_group_1+"] ...return COLORS_ECC["+digit+"]");
        return color;
    }
    //}}}
    //* STRING */
    // Expand_dirs {{{
    public static String Expand_dirs(String text)
    {
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "Expand_dirs("+text+")");
        if( text.contains("PROFILES_DIR") ) text = text.replace("PROFILES_DIR", Get_Profiles_dir().toString());

//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "...return ["+text+"]");
        return text.trim();
    }
    //}}}
    // ellipsis {{{
    public static String ellipsis(String msg, int length)
    {
        msg = msg.replace("\n","\\n");
        return (msg.length() <= length)
            ?   msg
            :   msg.substring(0, length-3)+"...";
    }
    //}}}
    // linify {{{
    public static String linify(String s)
    {
        String  pattern     = "[\n +]";
        String  replacement = " ";
        Pattern p = Pattern.compile( pattern );
        Matcher m = p.matcher( s );
        if(m.find())
            s = m.replaceAll( replacement );
        return s;
    }
    //}}}
    // listify {{{
    public static String listify(String s)
    {
        String  pattern     = "[{,}]";
        String  replacement = "\n| - ";
        Pattern p = Pattern.compile( pattern );
        Matcher m = p.matcher( s );
        if(m.find())
            s = "\no--\n| - "+ m.replaceAll( replacement ) +"\no--";
        return s;
    }
    //}}}
    // trim_url_anchor {{{
    public static String trim_url_anchor(String url)
    {
        String  pattern     = "#[0-9]+$";
        String  replacement = "";
        Pattern p = Pattern.compile( pattern );
        Matcher m = p.matcher( url );
        if(m.find())
            url = m.replaceAll( replacement );
        return url;
    }
    //}}}
    // trim_text {{{
    public static String trim_text(String text)
    {
        while(text.endsWith  (" ") || text.endsWith  ("\"") || text.endsWith  ("\n")) text = text.substring(0, text.length()-1);
        while(text.startsWith(" ") || text.startsWith("\"") || text.startsWith("\n")) text = text.substring(1                 );
        return text;
    }
    //}}}
    // LABEL_is_empty {{{
    public static boolean LABEL_is_empty(String text)
    {
//SETTINGS*/Settings.MOC(TAG_SETTINGS, "LABEL_is_empty("+text+")");

        // NULLITY
        if(text == null) return false;

        // STRING EMPTYNESS
        text = text.toUpperCase();
        return TextUtils.equals(text, ""            )
            || TextUtils.equals(text, SYMBOL_EMPTY  ) // ("\u23D8")
            || TextUtils.equals(text, STRING_EMPTY  ) // ("U+23D8")
            || TextUtils.equals(text, COMMENT_STRING) // ("#"     )
            ;
    }
    //}}}
    // TAG_equals {{{
    public static boolean TAG_equals(String tag1, String tag2)
    {
//*SETTINGS*/Settings.MOC(TAG_SETTINGS, "TAG_equals(["+tag1+"], ["+tag2+"])");

        // NULLITY
        if((tag1 == null) && (tag2 == null)) return  true;  // BOTH NULL
        if((tag1 == null) || (tag2 == null)) return false;  // ONLY ONE NULL

        // STRING EMPTYNESS
        tag1 = tag1.toUpperCase();
        tag2 = tag2.toUpperCase();
        boolean tag1_is_empty = TextUtils.equals(tag1, "") || TextUtils.equals(tag1, SYMBOL_EMPTY) || TextUtils.equals(tag1, STRING_EMPTY) || TextUtils.equals(tag1, COMMENT_STRING);
        boolean tag2_is_empty = TextUtils.equals(tag2, "") || TextUtils.equals(tag2, SYMBOL_EMPTY) || TextUtils.equals(tag2, STRING_EMPTY) || TextUtils.equals(tag2, COMMENT_STRING);
        //............................................("")..........................("\u23D8")..............................("U+23Di8)..............................."#".............
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "tag1_is_empty=["+tag1_is_empty+"] tag2_is_empty=["+tag2_is_empty+"]");
        if(tag1_is_empty && tag2_is_empty) return true;     // BOTH EMPTY

        // STRING EQUALITY
        return tag1.equals( tag2 );                         // SAME CONTENT
    }
    //}}}
    // rotationToString {{{
    // (hidden by class Surface)
    public static String rotationToString(int rotation)
    {
        switch( rotation )
        {
            case Surface.ROTATION_0  : return "ROTATION_0";
            case Surface.ROTATION_90 : return "ROATATION_90";
            case Surface.ROTATION_180: return "ROATATION_180";
            case Surface.ROTATION_270: return "ROATATION_270";
            default:                   return "Invalid rotation: "+ rotation;
        }
    }
    //}}}
    // text_markup {{{
    public static String text_markup(String tag, String text, boolean sep_vertical)
    {
        String caller = "text_markup  (tag=["+tag+"], text=["+text.replace("\n","\\n")+"])";
//*TAB*/Settings.MOC(TAG_TAB, caller);
        // DYNAMIC TEXT {{{
        if(TextUtils.isEmpty(text) && tag.startsWith("PROFILE "))
            text = Settings.get_profile_name_from_tag( tag );

        // }}}
        // TAG PREFIX {{{
        String sep = sep_vertical ? "\n" : " ";

        // COMPUTER
        if     ( tag.startsWith( Settings.CMD_RUN       )) { text = Settings.SYMBOL_COMPUTER   +sep+ text; }
        else if( tag.startsWith( Settings.CMD_BROWSE    )) { text = Settings.SYMBOL_COMPUTER   +sep+ text; }
        else if( tag.startsWith( Settings.CMD_SHELL     )) { text = Settings.SYMBOL_COMPUTER   +sep+ text; }

        // KEYWORD
        else if(text.indexOf(" ") > 0) // multi-words only
        {
            int idx_info_sep   = text.indexOf(NotePane.INFO_SEP);
            int idx_info_space = text.indexOf(" ");
            if(idx_info_space < idx_info_sep)
            {
                if     ( tag.startsWith( Settings.CMD_SENDKEYS  )) { text = Settings.SYMBOL_KEYBOARD   +sep+ text; }
                else if( tag.startsWith( Settings.CMD_SENDINPUT )) {
                    if(!text.startsWith("0x") || (text.length() > 7))
                        text = Settings.SYMBOL_KEYBOARD +sep+ text;
                }
            }
        }

        // }}}
        // NOTE SYMBOL {{{
        String label = text;

        if( tag.equals("NOTE") )
        {
            label = Settings.SYMBOL_PENCIL;
        }
        // }}}
        else {
            // PROFILES SYMBOLS {{{

            // if text is empty, use tag
            if(text.trim().length() == 0)
                text = Settings.Del_color_hex_from_text( tag );

            if     ( tag.contains  ("CONTROLS_TABLE") ) { label = Settings.SYMBOL_GEAR            +" CONTROLS"; }
            else if( tag.contains  ("PROFILES_TABLE") ) { label = Settings.SYMBOL_WHITE_STAR      +" PROFILES"; }
            else if( tag.startsWith("PROFILE "      ) )
            {
                // Substitue [KEYWORDS] with equivalent [SYMBOLS]
                String symbol;
                boolean done = false;

                if(!done) { label = "FAVORITES"     ; symbol = Settings.SYMBOL_BLACK_STAR; if( text.contains( label  )) { label = text.replace(label, symbol); done = true; } }
                if(!done) { label = "HOME"          ; symbol = Settings.SYMBOL_HOME      ; if( text.contains( label  )) { label = text.replace(label, symbol); done = true; } }
                if(!done) { label = "INDEX"         ; symbol = Settings.SYMBOL_INDEX     ; if( text.contains( label  )) { label = text.replace(label, symbol); done = true; } }
                if(!done) { label = "PERSO"         ; symbol = Settings.SYMBOL_HOME      ; if( text.contains( label  )) { label = text.replace(label, symbol); done = true; } }

                if(!done) { label = "favorites"     ; symbol = Settings.SYMBOL_BLACK_STAR; if( text.contains( label  )) { label = text.replace(label, symbol); done = true; } }
                if(!done) { label = "home"          ; symbol = Settings.SYMBOL_HOME      ; if( text.contains( label  )) { label = text.replace(label, symbol); done = true; } }
                if(!done) { label = "index"         ; symbol = Settings.SYMBOL_INDEX     ; if( text.contains( label  )) { label = text.replace(label, symbol); done = true; } }
                if(!done) { label = "perso"         ; symbol = Settings.SYMBOL_HOME      ; if( text.contains( label  )) { label = text.replace(label, symbol); done = true; } }

                if(!done) {                           symbol = Settings.SYMBOL_WHITE_STAR;                                label =                      symbol +sep+ text;     }
            }
            // }}}
        }
        text = label;
        // SLASH SYMBOL {{{
        if( !text.contains("</") ) // preserve HTML
            text = text.replace("/", Settings.SYMBOL_BLACK_CIRCLE);

        text = text.replace("\\", Settings.SYMBOL_ROUND_PUSHPIN);
        // }}}
        text = text.trim();
//*TAB*/Settings.MON(TAG_TAB, caller, "...return ["+text.replace("\n","\\n")+"]");
        return text;
    }
    //}}}
    // text_markdown {{{
    public static String text_markdown(String tag, String text)
    {
        String caller = "text_markdown(tag=["+tag+"], text=["+text.replace("\n","\\n")+"])";
//TAB//Settings.MON(TAG_TAB, caller);

        // Substitue [SYMBOLS] with equivalent [KEYWORDS]
        // SLASH SYMBOL {{{
        text = text.replace(Settings.SYMBOL_BLACK_CIRCLE , "/" );

        text = text.replace(Settings.SYMBOL_ROUND_PUSHPIN, "\\");
        // }}}
        // NOTE SYMBOL {{{
        String label = text;
        if( tag.equals("NOTE") ) {
            label = "NOTE";
        }
        // }}}
        else {
            // PROFILES TEXT  {{{
            if     ( tag.contains  ("CONTROLS_TABLE") ) { label = "CONTROLS_TABLE"; }
            else if( tag.contains  ("PROFILES_TABLE") ) { label = "PROFILES_TABLE"; }
            else if( tag.startsWith("PROFILE "      ) )
            {
                String symbol;
                boolean done = false;

                if(!done) { label = "FAVORITES"     ; symbol = Settings.SYMBOL_BLACK_STAR; if( text.contains( symbol )) { label = text.replace(symbol, label); done = true; } }
                if(!done) { label = "HOME"          ; symbol = Settings.SYMBOL_HOME      ; if( text.contains( symbol )) { label = text.replace(symbol, label); done = true; } }
                if(!done) { label = "INDEX"         ; symbol = Settings.SYMBOL_INDEX     ; if( text.contains( symbol )) { label = text.replace(symbol, label); done = true; } }
                if(!done) { label = "PERSO"         ; symbol = Settings.SYMBOL_HOME      ; if( text.contains( symbol )) { label = text.replace(symbol, label); done = true; } }

                if(!done) { label = "favorites"     ; symbol = Settings.SYMBOL_BLACK_STAR; if( text.contains( symbol )) { label = text.replace(symbol, label); done = true; } }
                if(!done) { label = "home"          ; symbol = Settings.SYMBOL_HOME      ; if( text.contains( symbol )) { label = text.replace(symbol, label); done = true; } }
                if(!done) { label = "index"         ; symbol = Settings.SYMBOL_INDEX     ; if( text.contains( symbol )) { label = text.replace(symbol, label); done = true; } }
                if(!done) { label = "perso"         ; symbol = Settings.SYMBOL_HOME      ; if( text.contains( symbol )) { label = text.replace(symbol, label); done = true; } }

                if(!done) {                           symbol = Settings.SYMBOL_WHITE_STAR;                                label = text.replace(symbol, ""   );                }
            }
            // }}}
        }
        text = label;
        // TAG [PREFIX-SYMBOL] SUBSTITUTIONS {{{
        if( text.startsWith(Settings.SYMBOL_COMPUTER) ) text = text.replaceFirst(Settings.SYMBOL_COMPUTER, "");
        if( text.startsWith(Settings.SYMBOL_KEYBOARD) ) text = text.replaceFirst(Settings.SYMBOL_KEYBOARD, "");

        //}}}
        // CLEAR DYNAMIC TEXT {{{
        if( tag.startsWith("PROFILE ") ) {
            if( text.equals( Settings.get_profile_name_from_tag( tag ) ) )
                text = "";
        }
        // }}}
        text = text.trim();
        if((text.length() > 0) && (text.charAt(0) == '\n')) text= text.substring(1);
//*TAB*/Settings.MON(TAG_TAB, caller, "...return ["+text.replace("\n","\\n")+"]");
        return text;
    }
    //}}}
    // get_NpButton_name {{{
    public static String get_NpButton_name(NpButton nb)
    {
        String name = null;
        NotePane np = get_np_for_button( nb );
        if(np != null)
            return np.name +"["+Settings.ellipsis(Settings.text_markdown(np.tag, np.text),24)+"]";
        else return null;
          //return nb.toString(); // [fall back to this as a last resort] (not from here .. some class instance may recognize their own)
    }
    //}}}
    // get_np_for_button {{{
    public static NotePane get_np_for_button(NpButton button)
    {
//WVTOOLS//String caller = "get_np_for_button("+button+")";//TAG_WVTOOLS

        NotePane np = null;
        Object o = button.getTag();
        if(o instanceof NotePane)
            np = (NotePane)o;

//WVTOOLS//Settings.MOM(TAG_WVTOOLS, caller+": ...return ["+np+"]");
        return np;
    }
    //}}}
    //* HTML */
    // ParseHtml {{{
    private static final Pattern HTMLPattern = Pattern.compile("<([a-zA-Z]+)[^>]*>[^<]*<(/\\1>)|(\\s+/>)");
    //..........................................................<(TAG______).....>.....<        /(TAG)> |    \s+    />
/*
/<\([a-zA-Z]\+\)[^>]*>[^<]*<\(\/\1>\)\|\(\s+\/>\)
HEAD ? symbol <u>U</u> text1 <b>B</b> text2 <i>I</i> text3 <em>EM</em> TAIL
:!start explorer "https://en.wikipedia.org/wiki/Regular_expression"
:!start explorer "http://code.tutsplus.com/tutorials/8-regular-expressions-you-should-know--net-6149"
:!start explorer "http://tutorials.jenkov.com/java-regex/matcher.html"
*/
    public  static String ParseHtml(String text)
    {
        if(text.equals("")) return "";

        // HTML decode .. (remove <> constructs) {{{
        int idx = 0;
        StringBuilder sb = new StringBuilder();

        Matcher matcher = HTMLPattern.matcher( text );
        int start, end;
        while( matcher.find() )
        {
//if(idx == 0) Settings.MON(TAG_SETTINGS, "@ text=["+text+"]");
            start       = matcher.start();
            end         = matcher.end();

            String head = (idx < start) ? text.substring(idx , start) : "";
//Settings.MON(TAG_SETTINGS, "head=["+head+"]");
            sb.append( head );

            String html = text.substring(start, end);

//Settings.MON(TAG_SETTINGS, "html=["+html+"]");
        //  html        = Html.fromHtml( html ); // *** has been deprecated ***

            sb.append( html );

            idx = end;
        }
        //}}}
        String tail = (idx < text.length()) ? text.substring(idx) : "";
//Settings.MON(TAG_SETTINGS, "tail=["+tail+"]");
        sb.append( tail );

        return sb.toString();
    }
    //}}}
    //* URL */
    // Get_data_scheme_html {{{
    public  static String Get_data_scheme_html(String title, String body)
    {
        String html
            = "<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01//EN' 'http://www.w3.org/TR/html4/strict.dtd'>\n"
            +  "<html>\n"
            +  " <head>\n"
            +  "  <title>"+title+" (JavascriptInterface)</title>\n"
            +  "  <meta http-equiv='content-type' content='text/html; charset=ISO-8859-1'>\n"
            +  " </head>\n"
            +"\n"
            +  " <body>\n"
            +     body +"\n"
            +  " </body>\n"
            +  "</html>\n"
            ;
//*URL*/Settings.MOM(TAG_URL, "Get_data_scheme_html(title["+title+"], body=["+ ellipsis(body,16) +"]): ...return:\n"+html);
        return html;
    }
    //}}}
    // URL_equals {{{
    public static boolean URL_equals(String url1, String url2)
    {
        if( url1    == null ) return false;
        if( url1.equals("") ) return false;

        if( url2    == null ) return false;
        if( url2.equals("") ) return false;

        return (url1    ).equalsIgnoreCase(url2    )
            || (url1    ).equalsIgnoreCase(url2+"/")
            || (url1+"/").equalsIgnoreCase(url2    )
            ;
    }
    //}}}
    // URL_path_equals {{{
    public static boolean URL_path_equals(String url1, String url2)
    {
//*SETTINGS*/Settings.MOC(TAG_SETTINGS, "URL_path_equals(["+url1+"], ["+url2+"])");

        //..................................................1234567
        int idx; //.........................................http://
        if(url1.length() > 8) {
            idx = url1.indexOf('/', 8); if(idx > 0) url1 = url1.substring(0, idx);
            idx = url1.indexOf('?', 8); if(idx > 0) url1 = url1.substring(0, idx);
        }
        if(url2.length() > 8) {
            idx = url2.indexOf('/', 8); if(idx > 0) url2 = url2.substring(0, idx);
            idx = url2.indexOf('?', 8); if(idx > 0) url2 = url2.substring(0, idx);
        }

//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "= url1=["+url1+"]");
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "= url2=["+url2+"]");

        return url1.equalsIgnoreCase( url2 ) ;
    }
    //}}}
    // GetMimeType {{{
    public static String GetMimeType(String url)
    {
        String mimeType = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if(extension != null)
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension( extension );

//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "GetMimeType(url) ...return mimeType=["+mimeType+"]");
        return mimeType;
    }
    //}}}
    // Parse_keywords_from_url {{{
    public static String Parse_keywords_from_url(String url                   ) { return Parse_keywords_from_url(url, false); }
    public static String Parse_keywords_from_url(String url, boolean with_info)
    {
//*SETTINGS*/Settings.MOC(TAG_SETTINGS, "Parse_keywords_from_url");

        // [https://en.m.wikipedia.org/wiki/World_War_II#/media/File%3ADestroyed_Warsaw%2C_capital_of_Poland%2C_January_1945.jpg]
        // (...wikipedia World War II Destroyed Warsaw capital Poland January 1945)
        String pattern, replacement;
        StringBuilder sb = new StringBuilder();

        // URL
        if(with_info) sb.append("..........URL: ["+url+"]");
        String keywords = url;

        // PROTOCOL ( .*// ) {{{
        pattern     = ".*//";
        replacement =     "";
        keywords    = keywords.replaceAll(pattern, replacement);

        if(with_info) sb.append(".....PROCOTOL: ["+keywords+"]");
        // }}}
        // DOMAIN ( . ) {{{
        pattern     = "\\.";
        replacement =   " ";
        keywords    = keywords.replaceAll(pattern, replacement);

        if(with_info) sb.append(".......DOMAIN: ["+keywords+"]");
        // }}}
        // DECODED ( %.. ) {{{
        try { keywords = URLDecoder.decode(keywords, "UTF-8"); } catch(Exception ignored) {}

        if(with_info) sb.append("..... DECODED: ["+keywords+"]");
        //}}}
        // PARAM=VALUE  ( key=val ) .. BUT KEEP q=(val) {{{
        pattern     = "\\?q=";
        replacement =   " ";
        keywords    = keywords.replaceAll(pattern, replacement);

        pattern     = "\\b\\w+=\\w+\\b";
        replacement =   " ";
        keywords    = keywords.replaceAll(pattern, replacement);

        if(with_info) sb.append("......KEY=VAL: ["+keywords+"]");
        //}}}
        // FOLDER ( / ) - PARAM  ( ?  & + ) SEPARATORS ( - ) ANCHOR ( # ) {{{
        pattern     = "[\\\\/\\?&\\+\\-:_,]";
        replacement =   " ";
        keywords    = keywords.replaceAll(pattern, replacement);

        if(with_info) sb.append(".......PARAMS: ["+keywords+"]");
        //}}}
        //  ANCHOR (#.*$) {{{
        pattern     = "#.*$";
        replacement =   "";
        keywords    = keywords.replaceAll(pattern, replacement);

        if(with_info) sb.append("....NO-ANCHOR: ["+keywords+"]");
        //}}}
        // LONE LOWERCASE CHARS (sinle pair) {{{
        keywords    = keywords.replaceAll("\\b[a-z]{1,2}\\b", "");

        if(with_info) sb.append(".......SINGLE: ["+keywords+"]");
        //}}}
        // TRIVIAL KEYROWDS ( www File ){{{

        //pattern     = "www";
        //replacement =   "";
        //keywords    = keywords.replaceAll(pattern, replacement);

        pattern     = "(?i)\\b(www|com|org|wiki|file|media|htm|html|jpg|gif|svg|png)\\b";
        replacement =   "";
        keywords    = keywords.replaceAll(pattern, replacement);

        if(with_info) sb.append("......TRIVIAL: ["+keywords+"]");
        //}}}
        // MULTIPLE SPACES {{{
        keywords    = keywords.replaceAll(" +", " ").trim();

        if(with_info) sb.append(".......SPACES: ["+keywords+"]");
        //}}}
/*
        // LIST {{{
        String line_head = NotePane.INFO_SEP;

        pattern     = " ";
        replacement = line_head;
        Pattern p = Pattern.compile( pattern );

        Matcher m = p.matcher( keywords );
        if(m.find())
            keywords = m.replaceAll( replacement );

        if( !TextUtils.isEmpty(keywords) ) keywords = line_head + keywords;

        //}}}
*/

        keywords = keywords.trim();

        if(with_info) sb.append("KEYWORDS:\n"+keywords);

        if( with_info ) return  sb.toString();
        else            return  keywords;
    }
    //}}}
    // URL_keywords_equals {{{
    public static boolean URL_keywords_equals(String url1, String url2)
    {
        if((url1==null) || (url2==null)) return false;

        String  url1_keywords = Parse_keywords_from_url( url1 );
        String  url2_keywords = Parse_keywords_from_url( url2 );
        boolean        result = TextUtils.equals(url1_keywords, url2_keywords);

//*URL*/Settings.MOC(TAG_URL, "URL_keywords_equals:");
//*URL*/Settings.MOM(TAG_URL, "...url1_keywords=["+url1_keywords+"]");
//*URL*/Settings.MOM(TAG_URL, "...url2_keywords=["+url2_keywords+"]");
//*URL*/Settings.MOM(TAG_URL, "...return "+result);
        return result;
    }
    //}}}
    // PERCENT PRE-ENCODING to avoid false UTF INJECTION such as: " xNNN" encoded as "%20xNNN" {{{
    //{{{
    private static final String SPACE_X_MIN         = "_2078_";
    private static final String SPACE_X_MAJ         = "_2058_";
    private static final String SPACE_X_MIN_PERCENT = "%20%78";// x
    private static final String SPACE_X_MAJ_PERCENT = "%20%58";// X

//  public  static final Pattern URLPattern = Pattern.compile("(http[^+%!^{]+)|(about:.+)|(chrome:.+)");
//  public  static final Pattern URLPattern = Pattern.compile("(https*:\\S*)|(about:.+)|(chrome:.+)");
    public  static final Pattern URLPattern = Pattern.compile("(https*:.*)|(ftp:.*)|(file:.*)|(data:.+)|(about:.+)|(chrome:.+)"); //... filter space with URLPattern

    //}}}
    // Get_normalized_url {{{
    public static String last_normalized_url_scheme = "";
    public static String Get_normalized_url(String raw_arg)
    {
//*URL*/Settings.MOC(TAG_URL, "Get_normalized_url("+raw_arg+"):");

        last_normalized_url_scheme = "";
        // REMOVE TRAILING SENDKEYS EXTRA KEYS .. (i.e. {ENTER}) {{{
        //  "https://thepiratebay.cr/search/The king_s speech/0/7/0"{ENTER}
        String url = raw_arg.trim();

        int idx = url.indexOf('{');
        if( idx > 0) {
            url = url.substring(0, idx);

//*URL*/Settings.MOM(TAG_URL, "...SENDKEYS indexOf('{') url=["+url+"]");
//...............................SENDKEYS indexOf('{') url=["https://thepiratebay.cr/search/The king's speech/0/7/0"]
        }
        //}}}
        // REMOVE SURROUNDING QUOTES {{{
        //  "https://thepiratebay.cr/search/The king_s speech/0/7/0"
        if( url.indexOf('"') >= 0)
        {
            if( url.startsWith("\"") ) url = url.substring(1);
            if( url.endsWith  ("\"") ) url = url.substring(0, url.length()-1);

//*URL*/Settings.MOM(TAG_URL, "...QUOTE TRIMMED url=["+url+"]");
//...............................QUOTE TRIMMED url=[https://thepiratebay.cr/search/The king's speech/0/7/0]
        }
        //}}}

        // PERCENT PRE-ENCODING to avoid false UTF INJECTION such as: " xNNN" encoded as "%20xNNN" {{{
        url = url.replace(" x", SPACE_X_MIN);
        url = url.replace(" X", SPACE_X_MAJ);
//*URL*/Settings.MOM(TAG_URL, "...SPACE_X: ["+url+"]");

        //}}}
        // PERCENT EMBEDED "%20" AS "+" SPACE ALTERNATIVE {{{
        url = url.replace("%20", "+");
//*URL*/Settings.MOM(TAG_URL, "...SPACE_%: ["+url+"]");
        //}}}
        // REGEX URLPattern {{{
        Matcher matcher = URLPattern.matcher( url );
        if( matcher.find() ) {
            url = matcher.group();
//URL//Settings.MON(TAG_URL, "...REGEX-OK: matcher.find("+URLPattern+") url=["+url+"]");
        }
        else {
            url = "";
//URL//Settings.MON(TAG_URL, "...REGEX-NO: matcher.find("+URLPattern+") url=["+url+"]");
        }
        //}}}
        if(        !TextUtils.isEmpty(url)
                && !url.startsWith("about:" )
                && !url.startsWith("about:" )
                && !url.startsWith("data:")
        ) {
            try {
                URL _url = new URL(url);
                // LOG URL {{{
                try {
//*URL*/Settings.MOM(TAG_URL,         "URL("+ url +")\n===["+ _url +"]");
//*URL*/Settings.MOM(TAG_URL, "...getProtocol........=["+ _url.getProtocol()      +"]");
//*URL*/Settings.MOM(TAG_URL, "...getAuthority.......=["+ _url.getAuthority()     +"]");
//*URL*/Settings.MOM(TAG_URL, "...getUserInfo........=["+ _url.getUserInfo()      +"]");
//*URL*/Settings.MOM(TAG_URL, "...getHost............=["+ _url.getHost()          +"]");
//*URL*/Settings.MOM(TAG_URL, "...getPort............=["+ _url.getPort()          +"]");
//*URL*/Settings.MOM(TAG_URL, "...getDefaultPort.....=["+ _url.getDefaultPort()   +"]");
//*URL*/Settings.MOM(TAG_URL, "...getFile............=["+ _url.getFile()          +"]");
//*URL*/Settings.MOM(TAG_URL, "...getPath............=["+ _url.getPath()          +"]");
//*URL*/Settings.MOM(TAG_URL, "...getQuery...........=["+ _url.getQuery()         +"]");
//*URL*/Settings.MOM(TAG_URL, "...getRef.............=["+ _url.getRef()           +"]");
                }
                catch(Exception ex) { Settings.log_ex(ex, "URL("+url+")"); }
                //}}}
                // 1/2 ENCODE
                // URLEncoder ( must be called before URI) {{{
                try {
                    //   Illegal character in path at index 34:
                    // ... https://thepiratebay.cr/search/The king
                    // ...(0123456789_123456789_123456789_1234)
                    url = URLEncoder.encode(url, "UTF-8");
                }
                catch(Exception ex) { Settings.log_ex(ex, "URLEncoder.encode("+url+")"); }
                //}}}
                // 2/2 URI RECONTRUCTION
                    // URI(scheme, userInfo, host, port, path, query, fragment) // {{{
                    URI uri = new URI(_url.getProtocol()    // String scheme
                            , _url.getUserInfo()        // String userInfo
                            , _url.getHost()            // String host
                            , _url.getPort()            // int port
                            , _url.getPath()            // String path
                            , _url.getQuery()           // String query
                            , _url.getRef()             // String fragment
                            );

//*URL*/Settings.MOM(TAG_URL, "URI("+ _url +")\n===["+ uri +"]");
                    // }}}
                    // LOG URI {{{
                    last_normalized_url_scheme =             uri.getScheme();
//*URL*/Settings.MOM(TAG_URL, "...getScheme..............=["+ uri.getScheme()             +"]");
//*URL*/Settings.MOM(TAG_URL, "...getSchemeSpecificPart..=["+ uri.getSchemeSpecificPart() +"]");
//*URL*/Settings.MOM(TAG_URL, "...getAuthority...........=["+ uri.getAuthority()          +"]");
//*URL*/Settings.MOM(TAG_URL, "...getRawAuthority........=["+ uri.getRawAuthority()       +"]");
//*URL*/Settings.MOM(TAG_URL, "...getUserInfo............=["+ uri.getUserInfo()           +"]");
//*URL*/Settings.MOM(TAG_URL, "...getRawUserInfo.........=["+ uri.getRawUserInfo()        +"]");
//*URL*/Settings.MOM(TAG_URL, "...getHost................=["+ uri.getHost()               +"]");
//*URL*/Settings.MOM(TAG_URL, "...getPort................=["+ uri.getPort()               +"]");
//*URL*/Settings.MOM(TAG_URL, "...getPath................=["+ uri.getPath()               +"]");
//*URL*/Settings.MOM(TAG_URL, "...getRawPath.............=["+ uri.getRawPath()            +"]");
//*URL*/Settings.MOM(TAG_URL, "...getQuery...............=["+ uri.getQuery()              +"]");
//*URL*/Settings.MOM(TAG_URL, "...getRawQuery............=["+ uri.getRawQuery()           +"]");
//*URL*/Settings.MOM(TAG_URL, "...getFragment............=["+ uri.getFragment()           +"]");
//*URL*/Settings.MOM(TAG_URL, "...getRawFragment.........=["+ uri.getRawFragment()        +"]");
//*URL*/Settings.MOM(TAG_URL, "...isAbsolute.............=["+ uri.isAbsolute()            +"]");
//*URL*/Settings.MOM(TAG_URL, "...isOpaque...............=["+ uri.isOpaque()              +"]");
//*URL*/Settings.MOM(TAG_URL, "...normalize..............=["+ uri.normalize()             +"]");
//*URL*/Settings.MOM(TAG_URL, "...parseServerAuthority...=["+ uri.parseServerAuthority()  +"]");
//*URL*/Settings.MOM(TAG_URL, "...toASCIIString..........=["+ uri.toASCIIString()         +"]");
//*URL*/Settings.MOM(TAG_URL, "...toString...............=["+ uri.toString()              +"]");
//*URL*/Settings.MOM(TAG_URL, "...toURL..................=["+ uri.toURL()                 +"]");
                    //}}}
                //  url     = uri.toASCIIString();
                    url     = _url.toString();
            }
            catch(Exception ex) { Settings.log_ex(ex, "*** URI("+url+")"); }
        }

        // .................................................................(vvvvv).......... [ x256]   ...encoded as...
        // URL    [Get_normalized_url(https://thepiratebay.cr/search/Homeland S06E01 x265/0/7/0)]
        // URL =...return url=[https://thepiratebay.cr/search/Homeland%20S06E01%20x265/0/7/0]
        // ...................................................................(^^^^^^^)...... [%20x265] ...seen as an UTF
        // .................[https://thepiratebay.cr/search/Homeland%20S06E01%2613/0/7/0] .......by C#.Server.Sendinput
        //
        // LOGGING:
        // W:         URL =...getPath................=[/search/Homeland S06E01 x265/0/7/0]
        // W:         URL =...getRawPath.............=[/search/Homeland%20S06E01%20x265/0/7/0]
        //
        // W:         URL =url = uri.getPath()========[/search/Homeland S06E01 x265/0/7/0]
        //
        // W:         URL =url = uri.toASCIIString()==[https://thepiratebay.cr/search/Homeland%20S06E01%20x265/0/7/0]

        url = url.replace(SPACE_X_MIN, SPACE_X_MIN_PERCENT);
        url = url.replace(SPACE_X_MAJ, SPACE_X_MAJ_PERCENT);
///*URL*/Settings.MOM(TAG_URL, ".....PERCENT_CHECK["+url+"]");
//        url = url.replace(     "%25", "%"                ); // i.e. %25%27 (') // FIXME

//*URL*/Settings.MOM(TAG_URL, "...return.........["+url+"]\n***\n***\n***\n***\n");
        return url;
    }
    //}}}
    //}}}
    //* EVENTS */
    // Get_action_name {{{
    public static String Get_action_name(MotionEvent event)
    {
        if(event == null)                            return "NULL_EVENT";
        switch( event.getActionMasked() )
        {
            case MotionEvent.ACTION_DOWN           : return "ACTION_DOWN";
            case MotionEvent.ACTION_UP             : return "ACTION_UP";
            case MotionEvent.ACTION_MOVE           : return "ACTION_MOVE";
            case MotionEvent.ACTION_CANCEL         : return "ACTION_CANCEL";
            case MotionEvent.ACTION_OUTSIDE        : return "ACTION_OUTSIDE";
            case MotionEvent.ACTION_POINTER_DOWN   : return "ACTION_POINTER_DOWN";
            case MotionEvent.ACTION_POINTER_UP     : return "ACTION_POINTER_UP";
            case MotionEvent.ACTION_HOVER_MOVE     : return "ACTION_HOVER_MOVE";
            case MotionEvent.ACTION_SCROLL         : return "ACTION_SCROLL";
            case MotionEvent.ACTION_HOVER_ENTER    : return "ACTION_HOVER_ENTER";
            case MotionEvent.ACTION_HOVER_EXIT     : return "ACTION_HOVER_EXIT";
            case MotionEvent.ACTION_BUTTON_PRESS   : return "ACTION_BUTTON_PRESS";
            case MotionEvent.ACTION_BUTTON_RELEASE : return "ACTION_BUTTON_RELEASE";
            default                                : return "ACTION_UNKOWN";
        }
    }
    //}}}
    //* STATES */
    // Get_cart_state_name {{{
    public static String Get_cart_state_name(int cart_state)
    {
        switch( cart_state ) {
            case CART_STATE_DEL     : return "CART_STATE_DEL";
            case CART_STATE_ADD     : return "CART_STATE_ADD";
            default:
            case CART_STATE_DEFAULT : return "CART_STATE_DEFAULT";
        }
    }
    //}}}
    //* SENDKEYS */
    // Get_SENDKEYS_ENCODED {{{
    public static String Get_SENDKEYS_ENCODED(String cmdLine)
    {
//*COMM*/Settings.MOC(TAG_COMM, "Get_SENDKEYS_ENCODED("+cmdLine+")");

        // The plus sign (+), caret (^), percent sign (%), tilde (~), and parentheses ()
        // have special meanings to SendKeys.
        // To specify one of these characters, enclose it within braces ({}). 

        // :!start explorer "https://msdn.microsoft.com/en-us/library/system.windows.forms.sendkeys(v=vs.110).aspx"

            // EXTRACT TRAILING SENDKEYS EXTRA KEYS .. (i.e. {ENTER}) {{{
            String suffix = "";
            int idx = cmdLine.indexOf('{');
            if(idx > 0) {
                suffix  = cmdLine.substring(   idx);
                cmdLine = cmdLine.substring(0, idx);
//*COMM*/Settings.MOM(TAG_COMM, "...cmdLine=["+ cmdLine +"]");
//*COMM*/Settings.MOM(TAG_COMM, "....suffix=["+ suffix  +"]");
            }
            //}}}
            cmdLine = cmdLine
                .replace("{","{{}")
                .replace("}","{}}")
                .replace("(","{(}")
                .replace(")","{)}")
                .replace("+","{+}")
                .replace("^","{^}")
                .replace("%","{%}")
                .replace("~","{~}")
                +suffix
                ;
//*COMM*/Settings.MOM(TAG_COMM, "...return ["+cmdLine+"]");
        return cmdLine;
    }
    //}}}
    //* FILES */
    // dir_list {{{
    public static List<File> dir_list(File dir                                 ) { return dir_list(dir, 0    , null); }
    public static List<File> dir_list(File dir, int level                      ) { return dir_list(dir, level, null); }
    public static List<File> dir_list(File dir, int level, String pattern) // number of recurive calls .. (0 == do not search subdirs)
    {
//*SETTINGS*/Settings.MOM(TAG_SETTINGS, "dir_list("+dir+", "+pattern+", "+level+")");
        List<File>  resultList = new ArrayList<>();

        Pattern mPattern = (pattern == null) ? null : Pattern.compile(pattern);

        File[]          fList  = dir.listFiles();
        if(fList != null) {
            for(File file : fList) {
                if( file.isDirectory() )
                {
                    // dir matching pattern
                    if( (mPattern != null) && mPattern.matcher( file.getPath() ).find()) {
                        resultList.add( file );
//*SETTINGS*/if(!file.canRead ()) Settings.MOM(TAG_SETTINGS, "*** !canRead ("+file+")");
//*SETTINGS*/if(!file.canWrite()) Settings.MOM(TAG_SETTINGS, "*** !canWrite("+file+")");
                    }
                    // +++search contents
                    if(level != 0   ) {
                        resultList.addAll( dir_list(file, level-1, pattern));
                    }
                }
                else if((mPattern != null) && mPattern.matcher( file.getPath() ).find()) resultList.add   ( file );
            }
        }

        return resultList;
    }
/*
:!start explorer "http://www.regular-expressions.info/modifiers.html"
*/
    //}}}
    // get_dir_name {{{
    public static String get_dir_name(String file_path)
    {
        return (file_path.indexOf(     '/') >= 0 )
            ?   file_path.replaceFirst("/[^/]*$", "")    // strip tail (base) + (ext)
            :   EMPTY_STRING;
    }
    //}}}
    // get_base_name {{{
    public static String get_base_name(String file_path)
    {
        return file_path
            .replaceFirst(".*/"  , "")                  // strip head (dir)
            .replaceFirst("\\..*", "")                  // strip ext
            ;
    }
    //}}}
    // load_script_expression {{{

    public static String load_script_expression(String file_path, boolean logging)
    {
        if(logging) log("load_script_expression("+file_path+"):");

        File file = new File( file_path );
        if( !file.exists() )
        {
            log("*** "+ file_path +" not found *");
            return "";
        }

        int lines        = 0;
        int bytes        = 0;
        InputStream   is = null;
        StringBuilder sb = new StringBuilder();
        try {
            // dom_inject = function()
            // {
            //     SCRIPT_BODY
            // };
            // dom_inject();
            is                           = new BufferedInputStream(new FileInputStream(file));
            InputStreamReader        isr = new InputStreamReader  ( is  );
            BufferedReader            br = new BufferedReader     ( isr );
            //boolean script_header_passed = true;//false;
            //boolean script_body_passed   = false;
            String s;
            do {
                s = br.readLine();
                if(s == null) break;

              //s = filter_script_line(s, logging);

                if( TextUtils.isEmpty(s) ) continue;

                // BODY: DONE - (KEEPING ALL LINES FOR DATA SCHEME INJECTION) {{{
                //if(!script_body_passed  ) script_body_passed   = s.endsWith("};"); // .. (function def closing brace)
                // BODY: DONE }}}

                // BODY LINES {{{
                if(        !TextUtils.isEmpty(s)
                        //  script_header_passed
                        // !script_body_passed
                  )
                {
                    sb.append(s).append("\n");
                  //if(logging) log(s);
                    if(logging) lines += 1;
                    if(logging) bytes += s.length()+1;
                }
                // BODY LINES }}}

                // BODY: NEXT LINE {{{
                //if(!script_header_passed) script_header_passed = s.endsWith("{"); // .. (function def opening brace)
                // BODY: NEXT LINE }}}

                //if(logging && !script_header_passed) log("!script_header_passed: ["+s+"]");
                //if(logging &&  script_body_passed  ) log(" script_body_passed..: ["+s+"]");
            }
            while(s != null);
        }
        catch(Exception ex) {
            log("*** load_script_expression: Exception:\n"+ ex.toString());
        }
        finally {
            try { if(is != null) is.close(); } catch(Exception ignored) {}
        }
        if(logging) log("load_script_expression: bytes=["+bytes+"] lines=["+lines+"]");

        return sb.toString();
    }
    //}}}
    // filter_script_line {{{
    private static String filter_script_line(String line, boolean logging)
    {
        String reject_reason = null;

        String s = line;
        while(s.indexOf(" ") > 0) s = s.replace(" ", ""); // compact

        if( s.startsWith("javascript:(function(){")) reject_reason = "BOOKMARKLET  HEADER";
        if( s.startsWith("//")                     ) reject_reason = "SINGLE-LINE COMMENT";
        if( s.startsWith("})();")                  ) reject_reason = "BOOKMARKLET TRAILER";

        if(logging && (reject_reason!= null)) log("filter_script_line: "+ reject_reason +": ["+s+"]");

        return (reject_reason == null) ? s : EMPTY_STRING;
    }
    //}}}
    // file_exists {{{
    public static boolean file_exists(String file_path)
    {
        try {
            return new File(file_path).exists();
        }
        catch(Exception ignored) { return false; }
    }
    //}}}
    // get_file_lines {{{
    public static ArrayList<String> get_file_lines(String file_path)
    {
//*SETTINGS*/Settings.MOC(TAG_STORAGE, "get_file_lines("+file_path+")");
        ArrayList<String>    al = new ArrayList<>();
        InputStream is   = null;
        try {
            File               file = new File( file_path );
            is                      = new BufferedInputStream(new FileInputStream(file));
            InputStreamReader   isr = new InputStreamReader  ( is  );
            BufferedReader      br  = new BufferedReader     ( isr );
            String               s  = null;
            do {
                s = br.readLine(); if(s != null) al.add(s);
            }
            while(s != null);
        }
        catch(Exception ex) {
            log("*** Settings.get_file_lines("+file_path+") Exception:\n"+ ex.getMessage());
        }
        finally {
            try { if(is != null) is.close(); } catch(Exception ignored) {}
        }
        return al;
    }
    //}}}
    //* ARRAY */
    // al_toString {{{
    public static String al_toString(ArrayList<String> al)
    {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < al.size(); ++i)
            sb.append(" "+(i+1) +"=["+ al.get(i) +"]");
        return sb.toString().trim();
    }
    //}}}
    //* RUNTIME */
    // printStackTrace {{{
    public static void printStackTrace(String caller)
    {
        Settings.MOC(TAG_SETTINGS, "STACK TRACE", "caller=["+caller+"]");
        new RuntimeException("STACK TRACE").printStackTrace();
    }
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}

    // Settings PrettyPrint {{{
    private static String PrettyPrint()
    {
        return                  "# TO BE SAVED Settings"+TRACE_OPEN+"\n"
                + String.format("# %s = %s\n"                    , "     SAVE_CALLER", SAVE_CALLER)
                + String.format("# %s = %s\n"                    , "               D", D ? "true" : "false")
                + String.format("# %s = %s\n"                    , "              IP", SERVER_IP)
                + String.format("# %s = %d\n"                    , "            PORT", SERVER_PORT)
                + String.format("# %s = %s\n"                    , "            PASS", SERVER_PASS)
                + String.format("# %s = %s\n"                    , "          SUBNET", SERVER_SUBNET)
                + String.format("# %s = %s\n"                    , "             MAC", SERVER_MAC)
                + String.format("# %s = %d\n"                    , "           DEV_H", DEV_H)
                + String.format("# %s = %d\n"                    , "           DEV_W", DEV_W)
                + String.format("# %s = %s\n"                    , "          SOURCE", SOURCE)
                + String.format("# %s = %d\n"                    , "         DEV_DPI", DEV_DPI)
                + String.format("# %s = %s\n"                    , "         LOGGING", LOGGING ? "true" : "false")
                + String.format("# %s = %s\n"                    , "         LOG_CAT", LOG_CAT)
                + String.format("# %s = %s\n"                    , "         MONITOR", MONITOR)
                + String.format("# %s = %s\n"                    , "         LOG_FLT", LOG_FLT)
                + String.format("# %s = %d\n"                    , "         MON_DPI", MON_DPI)
                + String.format("# %s = %d\n"                    , "         OPACITY", OPACITY)
                + String.format("# %s = %s\n"                    , "         PALETTE", PALETTE)
                + String.format("# %s = %d\n"                    , "         PRODATE", PRODATE)
                + String.format("# %s = %s\n"                    , "         PROFILE", PROFILE)
                + String.format("# %s = %f\n"                    , "        DEV_ZOOM", DEV_ZOOM)
                + String.format("# %s = %f\n"                    , "        TXT_ZOOM", TXT_ZOOM)
                + String.format("# %s = %f (%.2f < %.2f)\n"      , "       DEV_SCALE", Settings.DEV_SCALE, Settings.DEV_SCALE_MIN, Settings.DEV_SCALE_MAX)
                + String.format("# %s = %s\n"                    , "       GUI_STYLE", GUI_STYLE)
                + String.format("# %s = %s\n"                    , "       GUI_FONT ", GUI_FONT)
                + String.format("# %s = %s\n"                    , "       GUI_TYPE ", GUI_TYPE)
                + String.format("# %s = %f\n"                    , "       MON_SCALE", MON_SCALE)
                + String.format("# %s = %s\n"                    , "   UNICODE_SHEET", UNICODE_SHEET)
                + String.format("# %s = %s\n"                    , "  UNICODE_PRESET", UNICODE_PRESET)
                + String.format("# %s = %s\n"                    , "       WEBGROUPS", WEBGROUPS)
                + String.format("# %s = %s\n"                    , " TOOL_URL_ACTION", TOOL_URL_ACTION)
                + String.format("# %s = %s PRODATE=%d (%s old)\n", " WORKING PROFILE", Working_profile, Working_profile_prodate, Get_time_elapsed(1000*Working_profile_prodate))
                + String.format("# %s =[%s]\n"                   , " BOOKMARK_TITLE ", BOOKMARK_TITLE)
                + String.format("# %s =[%s]\n"                   , " BOOKMARK_URL   ", BOOKMARK_URL)
                + String.format("# %s =[%s]\n"                   , " REQUESTED_TITLE", REQUESTED_TITLE)
                + String.format("# %s =[%s]\n"                   , " REQUESTED_URL  ", REQUESTED_URL)
                +               "# TO BE SAVED Settings"+TRACE_CLOSE+"\n"
                +               "# ... Session Settings"+TRACE_OPEN +"\n"
                + String.format("# %s = %s\n"                    , "          DEVICE", DEVICE)
                + String.format("# %s = %s\n"                    , "         FREEZED", FREEZED)
                + String.format("# %s = %s\n"                    , "         OFFLINE", OFFLINE)
                + String.format("# %s = %s\n"                    , "        APP_NAME", get_APP_NAME())
                + String.format("# %s = %s\n"                    , "       DISPLAY_H", DISPLAY_H)
                + String.format("# %s = %s\n"                    , "       DISPLAY_W", DISPLAY_W)
                +               "# ... Session Settings"+TRACE_CLOSE+"\n"
                ;
    }
    //}}}

    /** LOG */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ {{{
    // {{{

    // LOG_FLT (FILTER)
    private static final    int LOG_ACTIVITY    = 0x0002;
    private static final    int LOG_CLIENT      = 0x0008;
    private static final    int LOG_PARSER      = 0x0001;
    private static final    int LOG_PROFILE     = 0x0004;
    private static final    int LOG_SETTINGS    = 0x0010;
    // CURRENT FILTER
    private static          int LOG_FLT         = 0x0000;

    // CURRENT STATES
    public  static      boolean MONITOR	        = false;
    public  static      boolean LOGGING	        = false;
    public static void set_LOGGING(boolean state)
    {
        LOGGING = state;
        if( LOGGING )
            set_logging_last_user_activation_time();
    }

    public static long     logging_last_user_activation_time   = System.currentTimeMillis();
    public static void set_logging_last_user_activation_time()
    {
        logging_last_user_activation_time = System.currentTimeMillis();
    }

    public  static      boolean LOG_CAT	        = false;
    public  static      boolean ERRLOGGED       = false;

    public  static boolean  D = false;
    private static void Set_D(boolean state) { D = state; }

    public  static boolean  M  = false;
    private static void Set_M(boolean state) { M = state; }

    // }}}
    // GETTERS {{{
    public static boolean is_LOG_ACTIVITY() { return ((LOG_FLT & LOG_ACTIVITY) != 0); }
    public static boolean is_LOG_CLIENT  () { return ((LOG_FLT & LOG_CLIENT  ) != 0); }
    public static boolean is_LOG_PARSER  () { return ((LOG_FLT & LOG_PARSER  ) != 0); }
    public static boolean is_LOG_PROFILE () { return ((LOG_FLT & LOG_PROFILE ) != 0); }
    public static boolean is_LOG_SETTINGS() { return ((LOG_FLT & LOG_SETTINGS) != 0); }
    public static boolean is_LOG_FLT_SET () { return ( LOG_FLT                 != 0); }

    //}}}
    // toggle_LOG_FILTER {{{
    public static void toggle_LOG_FILTER(String cmd)
    {
//*SETTINGS*/Settings.MOC(TAG_SETTINGS, "toggle_LOG_FILTER("+ cmd +"):");

        if(cmd.toUpperCase().equals("LOG_ACTIVITY")) LOG_FLT = !is_LOG_ACTIVITY() ? (LOG_FLT | LOG_ACTIVITY) : (LOG_FLT & ~LOG_ACTIVITY);
        if(cmd.toUpperCase().equals("LOG_CLIENT"  )) LOG_FLT = !is_LOG_CLIENT  () ? (LOG_FLT | LOG_CLIENT  ) : (LOG_FLT & ~LOG_CLIENT  );
        if(cmd.toUpperCase().equals("LOG_PARSER"  )) LOG_FLT = !is_LOG_PARSER  () ? (LOG_FLT | LOG_PARSER  ) : (LOG_FLT & ~LOG_PARSER  );
        if(cmd.toUpperCase().equals("LOG_PROFILE" )) LOG_FLT = !is_LOG_PROFILE () ? (LOG_FLT | LOG_PROFILE ) : (LOG_FLT & ~LOG_PROFILE );
        if(cmd.toUpperCase().equals("LOG_SETTINGS")) LOG_FLT = !is_LOG_SETTINGS() ? (LOG_FLT | LOG_SETTINGS) : (LOG_FLT & ~LOG_SETTINGS);

        apply_LOG_FILTERS();
    }
    //}}}
    // toggle_MONITOR {{{
    public static void toggle_MONITOR()
    {
        if(D) log("@ toggle_MONITOR():");

        MONITOR     = !MONITOR;
    //  Callers     = "";
    //  Last_MON_IN = "";

System.err.println("@ toggle_MONITOR "+ (MONITOR ? "ON" : "OFF"));

        apply_MONITOR();
    }
    //}}}
    // apply_MONITOR {{{
    private static void apply_MONITOR()
    {
        if(D) log("@ apply_MONITOR:");

        FullscreenActivity.Set_M( MONITOR );
        RTabs             .Set_M( MONITOR );
        RTabsClient       .Set_M( MONITOR );
    //  CmdParser         .Set_M( MONITOR );
        Profile           .Set_M( MONITOR );
        Clipboard         .Set_M( MONITOR );
        Settings          .Set_M( MONITOR ); // this class

    }
    //}}}
    // LOG_FILTERS {{{
    // {{{
    private static boolean LOG_FILTER_ARE_MASKED   = false;
    private static boolean MASKED_LOGGING          = LOGGING;

    // }}}
    // ignore_LOG_FILTERS {{{
    public static void ignore_LOG_FILTERS()
    {
        // TEMPORARILY FORCE LOGGING
        LOG_FILTER_ARE_MASKED   = true;
        MASKED_LOGGING          = LOGGING;

        LOGGING                 = true;
        FullscreenActivity.Set_D( true  );
        RTabs             .Set_D( true  );
        RTabsClient       .Set_D( true  );
        CmdParser         .Set_D( true  );
        Profile           .Set_D( true  );
        Settings          .Set_D( true  );

        if(M) MON_LOG("ignore_LOG_FILTERS");
    }
    //}}}
    // apply_LOG_FILTERS {{{
    public static void apply_LOG_FILTERS()
    {
        if(D) log("@ apply_LOG_FILTERS:");

        // RELEASE TEMPORARILY FORCED LOGGING
        if( LOG_FILTER_ARE_MASKED ) {
            LOGGING                 =  MASKED_LOGGING;
            LOG_FILTER_ARE_MASKED   = false;
        }

        if(LOGGING) {
            FullscreenActivity.Set_D((LOG_FLT & LOG_ACTIVITY) != 0); if(D) log("@ FullscreenActivity.D=["+ FullscreenActivity.D +"]");
            RTabs             .Set_D((LOG_FLT & LOG_ACTIVITY) != 0); if(D) log("@        RTabsClient.D=["+        RTabsClient.D +"]");
            RTabsClient       .Set_D((LOG_FLT & LOG_CLIENT  ) != 0); if(D) log("@        RTabsClient.D=["+        RTabsClient.D +"]");
            CmdParser         .Set_D((LOG_FLT & LOG_PARSER  ) != 0); if(D) log("@          CmdParser.D=["+          CmdParser.D +"]");
            Profile           .Set_D((LOG_FLT & LOG_PROFILE ) != 0); if(D) log("@            Profile.D=["+            Profile.D +"]");
            Settings          .Set_D((LOG_FLT & LOG_SETTINGS) != 0); if(D) log("@           Settings.D=["+           Settings.D +"]");
        }
        else {
            FullscreenActivity.Set_D( false );
            RTabs             .Set_D( false );
            RTabsClient       .Set_D( false );
            CmdParser         .Set_D( false );
            Profile           .Set_D( false );
            Settings          .Set_D( false );
        }
        if(M) MON_LOG("apply_LOG_FILTERS");
    }
    //}}}
    //}}}

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
    // log {{{
    public  static void log_ex    (Exception ex, String caller) { MLog.log_ex    (ex, caller); }
    public  static void log       (String msg)                  { MLog.log_hand  (msg       ); }
    private static void log_left  (String msg)                  { MLog.log_left  (msg       ); }
    private static void log_status(String msg)                  { MLog.log_status(msg       ); }
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}

    /** MON */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ {{{
    // (MON) (MOC) (MOM) {{{
    public  static void MOC(String tag, String caller            ) {             mon(tag,    caller, EMPTY_STR); }
    public  static void MOC(String tag, String caller, String msg) {             mon(tag,    caller, msg      ); }
    public  static void MON(String tag, String caller, String msg) { if(LOG_CAT) mon(tag,    caller, msg      ); }
    public  static void MOM(String tag,                String msg) { if(LOG_CAT) mon(tag, EMPTY_STR, msg      ); }

    private static void mon(String tag, String caller, String msg)
    {
        String s;
        s = String.format(" %-16s %62s : %s", tag, caller, msg); // 92 (lowered on 180718)

        if(LOGGING && !LOG_CAT)                log(s); // DEVICE
        else                    System.err.println("RTABS:"+ s); // ADB
    }
    //}}}
 // [TERMCAP] {{{
    //private static final String ANSI_RESET  = "\u001B[0m"; // not yet supported by Android Studio Console
    //private static final String ANSI_BLACK  = "\u001B[30m";
    //private static final String ANSI_RED    = "\u001B[31m";
    //private static final String ANSI_GREEN  = "\u001B[32m";
    //private static final String ANSI_YELLOW = "\u001B[33m";
    //private static final String ANSI_BLUE   = "\u001B[34m";
    //private static final String ANSI_PURPLE = "\u001B[35m";
    //private static final String ANSI_CYAN   = "\u001B[36m";
    //private static final String ANSI_WHITE  = "\u001B[37m";
// }}}
//    // (MON) (MOC) (MOM) {{{
//    // {{{
//    //ivate static         String       Last_MON_IN  = "";
//
//    private static final   String             SPACES = "                                                                                ";
//    private static final   String       SYMBOL_ENTER = SYMBOL_RIGHT_ARROW; //">";
//    private static final   String       SYMBOL_EXIT  = SYMBOL_LEFT_ARROW ; //"<";
//
//    private static         String   prev_callers_STR = EMPTY_STR;
//    private static final String[]   prev_callers_ARR = new String[10];
//    private static            int   prev_level       = 0;
//
//    // }}}
//    // [prev_callers_ARR] {{{
//    public static void MOC(String tag, String callers_str /*.......*/) { MON( tag, callers_str, EMPTY_STR); } // no message
//    public static void MOM(String tag, /*.............l.*/ String msg) { MON( tag, EMPTY_STR  , msg      ); } // no callers
//    public static void MON(String tag, String callers_str, String msg)
//    {
////System.err.println(callers_str);
//        // TRACK MOST RECENT CALLERS CHAINS .. (override or fall back) // {{{
//        if(  callers_str != EMPTY_STR) prev_callers_STR =      callers_str; // remember last caller chain to work with when not provided
//        else                                callers_str = prev_callers_STR; // or use the previous one
//
//        // }}}
//        // LEVEL AND INDENT TRACKING .. (down the callers chain) {{{
//        // [caller1]
//        // .[caller2]
//        // ..[caller3]
//        String[] callers = callers_str.split("\\] \\[");
//        String         s;
//        int        level = 0;
//        for(    ; (level < callers.length) && (level < prev_callers_ARR.length)
//                ;++level
//           ) {
//            // same caller
//            if( callers[level].equals(prev_callers_ARR[level]) )
//            {
//                continue;
//            }
//            // changed caller sync level
//            prev_callers_ARR[level] = callers[level];
//
//            String indent = SPACES.substring(0,level);
//            s = String.format(" %10s %s[%s]", tag, indent, callers[level]); // [print] each [changed caller]
//            if(LOGGING && !LOG_CAT)                log(s);                  // ...[Device]
//            else                    System.err.println(s);                  // ...[ADB]
//           }
//        //}}}
//        // PRINT MONITOR PAYLOAD {{{
//        if( msg.startsWith("\n") ) msg = msg.substring(1); // SKIP NEWLINE MESSAGE PREFIX
//        if(msg.length() > 0) {
//            String indent = SPACES.substring(0,level);
//            s = String.format(" %10s%s=%s", tag, indent, msg);
//
//            if(LOGGING && !LOG_CAT)                log(s); // Device-only
//            else                    System.err.println(s); // Android Studio-only
//        }
//        // }}}
//        // GOING BACK THE CALLERS CHAIN {{{
//        if(        (callers.length > 0    )
//                && (callers.length < prev_level))
//        {
//            if(level > 0)
//                level -= 1;
//
//            String indent = SPACES.substring(0,level);
//            s      = String.format(" %10s%s<[%s]", tag, indent, prev_callers_ARR[level]);
//
//            if(LOGGING && !LOG_CAT)                log(s); // Device-only
//            else                    System.err.println(s); // Android Studio-only
//            prev_callers_ARR[level] = null;
//
//            prev_callers_STR        = EMPTY_STR;
//        }
//        //}}}
//        // TRACK LAST CALLER LEVEL {{{
//        prev_level = level;
//
//        // }}}
//    }
//    //}}}
//    //}}}
//    // MON_IN {{{
////  private static       String         Callers      = "";
//    public static void MON_IN(String caller) { MON_IN (caller, ""); }
//    public static void MON_IN(String caller, String msg)
//    {
//        MOM("->"+caller, msg);
///* //{{{
//        Last_MON_IN =              caller;
//        Callers    += SYMBOL_ENTER+caller;
//
//        MON(msg);
//*/ //}}}
//    }
//    //}}}
//    // MON_OUT {{{
//    public static void MON_OUT(String caller) { MON_OUT(caller, ""); }
//    public static void MON_OUT(String caller, String msg)
//    {
//        MOM("<-"+caller, msg);
///* //{{{
//        if( caller.equals( Last_MON_IN ) )
//        {
//            int idx;
//
//            // SWITCH LAST CALLER SYMBOL_ENTER FOR SYMBOL_EXIT
//            idx =     Callers.lastIndexOf(       SYMBOL_ENTER);
//            Callers = Callers.substring(0,idx) + SYMBOL_EXIT + Last_MON_IN;
//            MON( msg);
//
//            // DROP LAST CALLER
//            Callers = Callers.substring(0,idx);
//
//            // PICK PREVIOUS CALLER
//            idx = Callers.lastIndexOf(SYMBOL_ENTER);
//            Last_MON_IN = (idx < 0) ? "" : Callers.substring(idx+1);
//        }
//        else if(Last_MON_IN != "") {
//            MON("@ MON_OUT("+caller+") Missed MON_OUT(Last_MON_IN="+ Last_MON_IN +") @");
//            MON( msg);
//        }
//*/ //}}}
//    }
//    //}}}
    // MON_LOG .. (shows where we stand with logging) {{{
    private static void MON_LOG(String caller)
    {
        System.err.println(
                String.format("RTABS: "
                    + "["+ caller +"]\n"
                    + "%s [%s]\n"
                    + "%s [%s]\n"
                    + "%s [%s]\n"
                    + "%s [%s]\n"
                    + "%s [%s]\n"
                    + "%s [%s]\n"
                    + "%s [%s]\n"
                    + "@ ["+ caller +"]"
                    , "         MONITOR", (MONITOR                         ? "X" : " ")
                    , "         LOGGING", (LOGGING                         ? "X" : " ")
                    , "         LOG_CAT", (LOG_CAT                         ? "X" : " ")
                    , "        ACTIVITY", (((LOG_FLT & LOG_ACTIVITY) != 0) ? "X" : " ")
                    , "          CLIENT", (((LOG_FLT & LOG_CLIENT  ) != 0) ? "X" : " ")
                    , "          PARSER", (((LOG_FLT & LOG_PARSER  ) != 0) ? "X" : " ")
                    , "         PROFILE", (((LOG_FLT & LOG_PROFILE ) != 0) ? "X" : " ")
                    , "        SETTINGS", (((LOG_FLT & LOG_SETTINGS) != 0) ? "X" : " ")
                    )
                );
    }
    //}}}
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ }}}

}

/* // {{{
// REGEX {{{
"Regex / Reference"
:!start explorer "http://www.regular-expressions.info/reference.html"
"RegExr"
:!start explorer "http://regexr.com/32oeg"
"Regex / Matcher"
:!start explorer "http://tutorials.jenkov.com/java-regex/matcher.html"

//}}}
:let @p="COMM"
:let @p="COOKIE"
:let @p="SETTINGS"
:let @p="URL"
:let @p="WEBVIEW"

:let @p="\\w\\+"

" ACTIVATED
  :g/^\/\*p\w*\*\/.*p/t$
" ......... -> COMMENT
  :g/^\/\*p\w*\*\/.*p/s/^/\//


" COMMENTED
  :g/^\/\/\*p\w*\*\/.*p/t$
" ..........-> ACTIVATE
  :g/^\/\/\*p\w*\*\/.*p/s/^\///


*/ // }}}

