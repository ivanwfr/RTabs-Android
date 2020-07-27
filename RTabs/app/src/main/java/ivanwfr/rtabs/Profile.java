package ivanwfr.rtabs; // {{{

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

// }}}
// Comment {{{

// FILE SYNTAX: "PROFILE"  "FILE"  "FOLDER"  "ZIP"  "PARSE"
// (KEY_VAL) {{{

// [SAMPLE] "# PROFILE=design_webview_test"
// [FIELDS] (          ProfileName         )
// [VALUES] [          design_webview_test ]

// [SAMPLE] "# PRODATE=1469189845 Jul 22, 2016 14:17:25 (by SGP512)"
// [FIELDS] (          UNXTIMESEC DATE                  (srcDevice)
// [SAMPLE] [          1469189845 Jul 22, 2016 14:17:25 (by SGP512)

// # PALETTE=W8
// # OPACITY=95
// # DEV_W=1920
// # DEV_H=1080
// # MON_SCALE=0.75
// # DEV_DPI=120
// # MAXCOLORS=0

// }}}
// (PALETTE) {{{
// [SAMPLE] "PALETTE.9=Best-Metro, #00aba9,#ff0097,#a200ff,#1ba1e2,#f09609 "
// [FIELDS] (          NAME      ,  COLHEX, COLHE, COLHE, COLHEXXX, COLHEX )
// [VALUES] [          Best-Metro,  00aba9, ff0097, a200ff, 1ba1e2, f09609 ]

// }}}
// (TAG) {{{
// [SAMPLE] "TAB.tab068=type=SHORTCUT|xy_wh=17,07,05,09|shape=square|color=0|zoom=1|tag=http://remotetabs.com/trulyergonomic/1-9.html|text=1-9|tt=.."
// [FIELDS] (    NAME        TYPE           X , Y, W, H       SHAPE   COLOR#   ZOOM     TAG..........................................      TEXT   TT)
// [VALUES] [    tab068      SHORTCUT       17,07,05,09       square       0      1     http://remotetabs.com/trulyergonomic/1-9.html      1-9      ]

//}}}

// ACTIVITY:
// PROCESSING SUPPORT: {{{
// .enter_notification_loop
// ..hr_server_hook
// ...parse_new_data_from_server
// ...received_<...> response

// }}}
// ACTIVITY INJECTION POINTS: {{{
// ...when conditions and
// ...StoredReply are ready
// ...send calls to be intercepted
// ...used as a trigger for:
// 1. StoredReply to be stuffed with:
// - [sb_KEY_VAL + sb_TABS] or
// - [sb_PALETTES]
// 2. simulate ReadTask-done resonse
// ...last_sent_cmd.endsWith( CMD_ACK ))
// ...activity.sync_np_notify("ReadTask ACK ["+StoredReply+"]");
// 1. request_TABS_GET     CMD_TABS_GET
// 2. request_PALETTES_GET CMD_PALETTES_GET

// }}}

// CLIENT DATA SOURCE:
// FOR DYNAMIC PROFILES SUPPORT: // {{{
// ...(StoredReply) .. simulation of: f("just received from server reply situation")
// ...for parse_TABS
// ...for parse_PALETTES

// }}}

// }}}
// ========================================================================
// Profile ================================================ [KEY_VAL pairs]
// ========================================================================
class Profile
{
    // LOGGING {{{
    public static        String PROFILE_JAVA_TAG = "Profile (200727:14h:59)";

    // MONITOR TAGS
    private static       String TAG_STORAGE = Settings.TAG_STORAGE;
    private static       String TAG_PROFILE = Settings.TAG_PROFILE;

    public  static boolean  D = Settings.D;
    public  static void Set_D(boolean state) { D = state; log("Profile.Set_D("+state+")"); }//FIXME

    private static boolean  M = Settings.M;
    public  static void Set_M(boolean state) { M = state; log("Profile.Set_M("+state+")"); }//FIXME

    //}}}
    // instance members {{{
    private boolean within_workbench = false;
    public  boolean webGroup_checked = false;
    public  int      pending_changes = 0;
    private int               tabNum = 0;
//  private int              prodate = 0;
    public  String              name;

    //}}}

    /** LOAD & SAVE */
    //{{{
    //{{{
    // Profile {{{
    public Profile() { }
    public Profile(String name)
    {
        this.name = name;

        if(name != "") {
//*PROFILE*/Settings.MOC(TAG_PROFILE, "new Profile("+ name +")");
if(D||M)  Settings.MOC(TAG_PROFILE, "new Profile("+ name +")");
            load();
if(D||M)  Settings.MOM(TAG_PROFILE, "new "+ toString());
        }
    }
    //}}}
    // load() {{{
    private void load()
    {
        String caller = "load: name=["+name+"]";
if(D||M)  Settings.MOC(TAG_PROFILE, caller);
        try {
            String profile_base_name = name.replaceFirst(".*/", "");    // .. remove CONTAINING FOLDER name
if(D||M)  Settings.MOM(TAG_PROFILE, "...........profile_base_name=["+profile_base_name+"]");
if(D||M)  Settings.MOM(TAG_PROFILE, "Settings.get_base_name(name)=["+Settings.get_base_name(name)+"]");
            switch( name ) {
                // PROFILES_TABLE {{{
                case Settings.PROFILES_TABLE:
                case Settings.DOCKINGS_TABLE:
                    build_PROFILES_TABLE();
                    break;
                    //}}}
                    // PROFHIST_TABLE {{{
                case Settings.PROFHIST_TABLE:
                    build_PROFHIST_TABLE();
                    break;
                    //}}}
                    // CONTROLS_TABLE {{{
                case Settings.CONTROLS_TABLE:
                    build_CONTROLS_TABLE();
                    break;
                    //}}}
                    // SOUNDS_TABLE {{{
                case Settings.SOUNDS_TABLE:
                    build_SOUNDS_TABLE();
                    break;
                    //}}}
                    // PROFILE FROM FILE OR TEMPLATE {{{
                default:
                    boolean workbench_loaded = false; // formerly based on Profile_to_update

                    // WORKBENCH LOAD
                    //{{{
                    if( !workbench_loaded)
                    {
                        if( is_a_WORKBENCH_TEMPLATE_NAME( name ) )
                        {
if(D||M)  Settings.MON(TAG_PROFILE, caller  , "WORKBENCH: HOOKING A PROFILE-TEMPLATE:");
                            loadWorkbenchTools( WORKBENCH_BASE_NAME);

if(D||M)  Settings.MON(TAG_PROFILE, caller  , "WORKBENCH TABS: sb_TABS.length()=["+sb_TABS.length()+"]");
                            Settings.WORKBENCH_TOOL = 1;

                            // SETTING UP AN EMPTY WORKBENCH
                            if (!profile_base_name.equals(WORKBENCH_BASE_NAME))
                            {
                                loadWorkbenchModel( profile_base_name );
if(D||M)  Settings.MON(TAG_PROFILE, caller, "WORKBENCH] + ["+name+"] TABS: sb_TABS.length()=[" +sb_TABS.length()+"]");

                                // WIZARD FIRST STAGE
                            }
                            workbench_loaded = true; // formerly based on Profile_to_update
                        }
                    }
                    //}}}

                    // WORKBENCH HOOK PROFILE
                    //{{{
                    // - The loading of a USER-PROFILE on a WORKBENCH involves:
                    // ...discarding color palettes from the profile under construction
                    // ...and embedding workbench customizing tools
                    // ...into a WORKING-PROFILE-BUNDLE
if(D||M)  Settings.MON(TAG_PROFILE, caller, "...profile_base_name=["+       profile_base_name+"]");
if(D||M)  Settings.MON(TAG_PROFILE, caller, ".....Working_profile=["+Settings.Working_profile+"]");

                    if (      !workbench_loaded // formerly based on Profile_to_update
                            && Settings.Working_profile.equals( WORKBENCH_FULL_PATH ) // .. we are WORKING ON THE WORKBENCH alright
                            && !profile_base_name      .equals( WORKBENCH_BASE_NAME ) // .. NOT simply the EMPTY WORKBENCH by itself
                            && !is_a_WORKBENCH_TEMPLATE_NAME( name )
                       ) {
if(D||M)  Settings.MON(TAG_PROFILE, caller, "WORKBENCH: HOOKING A USER PROFILE:");
                        _build_Profiles_Dict();
                        String profile_name  = Get_profile_name_in_store( this.name );
                        String    file_path  = Profiles_Dict.get( profile_name );
                        if(       file_path != null)
                        {
                            this.name = profile_name;

                            // load and preserve WORKBENCH TABS and KEY_VAL {{{
                            loadWorkbenchTools(WORKBENCH_BASE_NAME);

                            String one_liner_key_val = get_PROFILE_KV_LINE();
//PROFILE// Settings.MON(TAG_PROFILE, caller, "one_liner_key_val=["+one_liner_key_val+"]");

                            within_workbench = true;
if(D||M)  Settings.MON(TAG_PROFILE, caller, "setting within_workbench=[true]");
                            // }}}
                            // load embeded profile and override PROFILE and PRODATE KEY_VAL
                            loadFromFile(file_path);

                            // OVERRIDE PROFILE [KEY_VAL] WITH (EFFECTIVE PROFILE NAME) {{{
                            String profile_key_val
                                = "# PROFILE " + name
                                + "\n" + _filter_key_vals_PROFILE_PRODATE_PALETTE_OPACITY(sb_KEY_VAL.toString());

                            // keep embeded PROFILE and PRODATE so that sync_PROFILE may work on it instead of the workbench itself
                            clear_KEY_VAL_sb();
                            sb_KEY_VAL.append(one_liner_key_val);
                            sb_KEY_VAL.append(profile_key_val);

                            String s = Settings.SYMBOL_KEY_VAL;
                            log(      s +"sb_KEY_VAL {{{\n"
                                    + s + sb_KEY_VAL.toString().replace("\n", "\n"+s)
                                    +    "sb_KEY_VAL }}}\n");
                            // }}}
                            // WIZARD FIRST STAGE
                            Settings.WORKBENCH_TOOL = 1;
                            workbench_loaded = true; // formerly based on Profile_to_update
                        }
                        // consume request
                       }
                    //}}}

                    // LOAD USER PROFILE
                    // {{{
                    if( !workbench_loaded ) // formerly based on Profile_to_update
                    {
                        Settings.WORKBENCH_TOOL = 0;
                        _build_Profiles_Dict();

                        String profile_name  = Get_profile_name_in_store( this.name );
                        if(    profile_name != null)
                        {
if(D||M)  Settings.MON(TAG_PROFILE, caller, "FROM A USER PROFILES-FOLDER FILE:");
                            this.name        = profile_name;
                            String file_path = Profiles_Dict.get( profile_name );
if(D||M)  Settings.MOM(TAG_PROFILE, "...loadFromFile("+ file_path +")");

                            loadFromFile( file_path );
                            workbench_loaded = true; // formerly based on Profile_to_update
                        }
                    }
                    //}}}
                    break;
                    //}}}
            }
        } catch(Exception ex) {
log("*** Profile("+ name +").load: ***\n"+ ex.getMessage());
//*PROFILE*/ex.printStackTrace();//TAG_PROFILE
if(D||M)  ex.printStackTrace();
        }
if(D||M)  Settings.MON(TAG_PROFILE, caller, "...done");
    }
    //}}}
    // Get_profile_name_in_store {{{
    private static String Get_profile_name_in_store(String profile_name)
    {
        String caller = "Get_profile_name_in_store("+profile_name+")";
if(D||M)  Settings.MOC(TAG_PROFILE, caller);
/*
:new /LOCAL/STORE/DEV/PROJECTS/RTabs/Util/src/Settings.cs
*/
        // PROFILE QUALIFIER {{{

        //}}}
        // 1/2 - TRY RELATIVE TO CURRENT_DIRNAME {{{
        String     file_path  = "";
        String relative_name  = Get_relative_profile_name( profile_name );
        if(    relative_name !=                            profile_name)
        {
            file_path        = Profiles_Dict.get( relative_name );
            if( !TextUtils.isEmpty(file_path) )
            {
                profile_name = relative_name;

if(D||M)  Settings.MOM(TAG_PROFILE, "...LOADING RELATIVE profile_name=["+ profile_name +"]");
                return profile_name;
            }
        }
        //}}}
        // 2/2 TRY ABSOLUTE {{{
        if( TextUtils.isEmpty(file_path) )
        {
            boolean fully_qualified = profile_name.startsWith("/") || profile_name.startsWith("\\");
            if(     fully_qualified ) profile_name = profile_name.substring(1); // strip leading slash

            file_path        = Profiles_Dict.get( profile_name );
            if( !TextUtils.isEmpty(file_path) )
            {

if(D||M)  Settings.MOM(TAG_PROFILE, "...LOADING ABSOLUTE profile_name=["+ profile_name +"]");
                return profile_name;
            }
        }
        //}}}
if(D||M)  Settings.MOM(TAG_PROFILE, caller+": ...return null");
        return null;
    }
    //}}}
    /* Get_relative_profile_name {{{*/
    public static String Get_relative_profile_name(String profile_name)
    {
        boolean fully_qualified = profile_name.startsWith("/") || profile_name.startsWith("\\");
        String  current_dirName = Settings.get_dir_name( Settings.Working_profile    );
        String  profile_dirName = Settings.get_dir_name( profile_name );

if(D||M)  Settings.MOM(TAG_PROFILE, "...Settings.LoadedProfile.name=["+ Settings.LoadedProfile.name +"]");
if(D||M)  Settings.MOM(TAG_PROFILE, "...Settings.Working_profile...=["+ Settings.Working_profile    +"]");
if(D||M)  Settings.MOM(TAG_PROFILE, "...............current_dirName=["+ current_dirName             +"]");
if(D||M)  Settings.MOM(TAG_PROFILE, "..................profile_name=["+ profile_name                +"]");
if(D||M)  Settings.MOM(TAG_PROFILE, "...............profile_dirName=["+ profile_dirName             +"]");
if(D||M)  Settings.MOM(TAG_PROFILE, "...............fully_qualified=["+ fully_qualified             +"]");

        String relative_name
            = !fully_qualified && (current_dirName != "") ? current_dirName+"/"+profile_name
            :  fully_qualified                            ?                     profile_name.substring(1) // strip leading slash
            :                                                                   profile_name
            ;

if(D||M)  Settings.MOM(TAG_PROFILE, "...return: ......relative_name=["+ relative_name               +"]");
        return relative_name;
    }
    /*}}}*/
    // _filter_key_vals_PROFILE_PRODATE_PALETTE_OPACITY {{{
    private static String _filter_key_vals_PROFILE_PRODATE_PALETTE_OPACITY(String key_val_lines)
    {
        StringBuilder sb = new StringBuilder();
        String[]   lines = key_val_lines.split("\n");
        for(int i=0; i< lines.length; ++i)
        {
        //  if(lines[i].indexOf( "PROFILE=" ) > 0) sb.append(lines[i] +"\n");
            if(lines[i].indexOf( "PRODATE=" ) > 0) sb.append(lines[i]).append("\n");
            if(lines[i].indexOf( "PALETTE=" ) > 0) sb.append(lines[i]).append("\n");
            if(lines[i].indexOf( "OPACITY=" ) > 0) sb.append(lines[i]).append("\n");
        }
        return sb.toString();
    }
    //}}}
    // reload() {{{
    public void reload()
    {
//*PROFILE*/Settings.MON(TAG_PROFILE, "reload", this.toString());
        clear_profile_buffers();
        load();
    }
    //}}}
    //}}}
    // Save {{{
    //{{{
    private static final String PRODATE_PATTERN = "PRODATE=(\\d+) ";

    // # PRODATE=1475834720 2016-10-07 12:05:20 (by RTabsDesigner)
    //}}}
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void Save(String profile_name, String file_text)
    {
        String caller = "Save("+profile_name+")";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);

        // IGNORE DYNAMICALLY BUILT PROFILE {{{
        if(        (profile_name.equals(Settings.PROFILES_TABLE))
                || (profile_name.equals(Settings.CONTROLS_TABLE))
          ) {
//*PROFILE*/Settings.MOM(TAG_PROFILE, profile_name+" does no need saving");
            return;
          }
//*PROFILE*/Settings.MOM(TAG_PROFILE,"file_text=["+ file_text.split("\n").length +" lines]=["+ file_text.length()+" b]");
//PROFILE//dump_file_text_last_line( file_text );//TAG_PROFILE
//*PROFILE*/Settings.MOM(TAG_PROFILE,"file_text=["+ file_text +"]");

        // }}}
        // WRITE AND SET [PRODATE] TIMESTAMP {{{
        try {
            // WRITE
            File file = GetProfileFile( profile_name );
            if(  file != null) {
                FileOutputStream fos = new FileOutputStream( file );
                fos.write( file_text.getBytes() );
                fos.close();
            }

            // setLastModified (PRODATE TIMESTAMP)
            if(file != null) {
                Pattern mPattern = Pattern.compile(PRODATE_PATTERN);
                Matcher  matcher = mPattern.matcher( file_text );
                if( matcher.find() )
                {
                    String prodate = matcher.group(1);
//*PROFILE*/Settings.MOM(TAG_PROFILE,"...prodate=["+prodate+"]");

//*PROFILE*/  String file_age = Settings.Get_time_elapsed(file.lastModified());// TAG_PROFILE
//*PROFILE*/Settings.MOM(TAG_PROFILE,"...file_age=["+file_age+"]");

//*PROFILE*/Settings.MOM(TAG_PROFILE,"...setLastModified("+ Long.parseLong(prodate) +")");
                    file.setLastModified( Long.parseLong(prodate) );

//*PROFILE*/         file_age = Settings.Get_time_elapsed(file.lastModified());// TAG_PROFILE
//*PROFILE*/Settings.MOM(TAG_PROFILE,"...file_age=["+file_age+"]");
                }
            }
        }
        catch(Exception ex) {
            log("*** Profile.Save(" + profile_name + ") ***\n" + ex.getMessage() + "");
        }
        // }}}
        Clear_Profiles_Dict("Save");
//*PROFILE*/Settings.MOM(TAG_PROFILE, caller+" ...done");
    }
    //}}}
    // dump_file_text_last_line {{{
    private static void dump_file_text_last_line(String file_text)
    {
        int idx = file_text.lastIndexOf('\n');          // ...before last line
        if( idx >= 0) idx += 1;                         // .start of last line
        else          idx  = file_text.length() -40;    // ...default to ending segment
        if( idx <  0) idx  = 0;                         // ...cope with short and empty content
        Settings.MOM(TAG_PROFILE, "LAST LINE=["+file_text.substring( idx ) +"]");
    }
    //}}}
    // loadFromFile {{{
    private void loadFromFile(String file_path)
    {
        String caller = "loadFromFile("+file_path+")";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);

        // PARSE PANEL CONTENT
        //if(RTabsClientInstance == null) return;  // UI NOT READY YET ?

        // ACCESS FILE
        if( !new File(file_path).exists() ) {
            log("*** Profile.loadFromFile("+file_path+") ...file not found");
            return;
        }

        // LOAD FILE LINES [# KEY_VAL] [PALETTES] [TABS]
        InputStream is      = null;
        try {
            File               file = new File( file_path );
            is                      = new BufferedInputStream(new FileInputStream(file));
            InputStreamReader   isr = new InputStreamReader  ( is  );
            BufferedReader       br = new BufferedReader     ( isr );
            String s = null;
            do {
                s = br.readLine();
                if(s != null)
                    loadSettingsLine(s);
            }
            while(s != null);
        }
        catch(Exception ex) {
            log("*** Profile.loadFromFile("+file_path+") Exception:\n"+ ex.toString());
        }
        finally {
            try { if(is != null) is.close(); } catch(Exception ignored) {}
        }

//*PROFILE*/Settings.MOM(TAG_PROFILE, caller+" ...done");
    }
    //}}}
    // LoadHeaderFromFile {{{
    private static String LoadHeaderFromFile(String file_path)
    {
        // FILE NOT FOUND {{{
        if( !new File( file_path ).exists() ) {

            log("*** Profile.LoadHeaderFromFile("+ file_path +") ...file not found");
            return "";
        }
        //}}}
        // LOAD THE FIRST 2 LINES {{{
        StringBuilder sb      = new StringBuilder();
        InputStream             is      = null;
        try {
            File                file    = new File( file_path );
            is                          = new BufferedInputStream(new FileInputStream(file));
            InputStreamReader   isr     = new InputStreamReader  ( is  );
            BufferedReader      br      = new BufferedReader     ( isr );

            // LOAD LINES 1 AND 2
            sb.append(br.readLine()).append("\n");
            sb.append(br.readLine());
        }
        catch(Exception ex) {
            log("*** Profile.LoadHeaderFromFile("+file_path+") Exception:\n"+ ex);
        }
        finally {
            try { if(is != null) is.close(); } catch(Exception ignored) {}
        }
        //}}}
        return sb.toString();
    }
    //}}}
    // Is_in_store {{{
    public static boolean Is_in_store(String profile_name)
    {
        boolean diag = false;

        // BUILTIN PROFILES
        if(        is_a_WORKBENCH_TEMPLATE_NAME       ( profile_name ) // TODO: cope with missing template-file (besides builtins like CONTROLS)
                || Settings.is_a_dynamic_profile_entry( profile_name )
          ) {
            diag = true;
          }
        // FROM FILE
        else {
            try {
                _build_Profiles_Dict();
                profile_name     = Get_profile_name_in_store( profile_name );
                String file_path = Profiles_Dict.get( profile_name );
//*PROFILE*/Settings.MOM(TAG_PROFILE, "Profiles_Dict.get("+profile_name+")=["+ file_path +"]");
if(D||M)           log(             "Profiles_Dict.get("+profile_name+")=["+ file_path +"]");
                if(file_path != null)
                    diag = true;
            }
            catch(Exception ex) {
                log("*** Profile("+ profile_name +").Is_in_store: ***\n"+ ex.getMessage());
            }
        }
//*PROFILE*/Settings.MOM(TAG_PROFILE, "--- Profile.Is_in_store("+ profile_name +") ...return "+ diag);
if(D||M)           log(             "--- Profile.Is_in_store("+ profile_name +") ...return "+ diag);
        return diag;
    }
    //}}}
    // isValid {{{
    // ...returns whether this profile contains (at least) one or more of a [KEYVAL], a [PALETTE] OR a [TAB] component
    // ...(OR means: LOOSELY-VALID)
    public boolean isValid() {
        return         (name != "")
            && (       have_PROFILE_KEY_VAL()
                    || have_PROFILE_PALETTES()
                    || have_PROFILE_TABS()
               );
    }
    //}}}
    // validate_or_delete {{{
    // 1. remove a file that does not qualify as containing a fully qualified profile
    // ...i.e. one of each of [KEYVAL], a [PALETTE] AND a [TAB] components
    // ...(AND means: NOT-LOOSELY-VALID)
    // 2. return the list of missing mandatory component
    public String validate_or_delete()
    {
        String caller = "validate_or_delete: name=["+ this.name +"]";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);
        String result = "";
        if(  (name == "")           ) { result = "*** unnamed profile" ; }
        if( !have_PROFILE_KEY_VAL ()) { result = "*** missing KEY_VAL" ; }
        if( !have_PROFILE_PALETTES()) { result = "*** missing PALETTES"; }
        if( !have_PROFILE_TABS    ()) { result = "*** missing TABS"    ; }

        String this_name = name;
        if(result != "") {
            result += " "+ DeleteProfile( name );
            name    = "";
            clear_profile_buffers();
        }

//*PROFILE*/Settings.MOM(TAG_PROFILE, "...return "+ result);
        return result;
    }
    //}}}
    //}}}

    /** BUFFERS */
    //{{{
    // STORE {{{
    private final StringBuffer sb_KEY_VAL      = new StringBuffer();
    private final StringBuffer sb_TABS         = new StringBuffer();
    private final StringBuffer sb_PALETTES     = new StringBuffer();

    private   void store_KEY_VAL_line (String argLine) { sb_KEY_VAL .append(argLine).append("\n"); }
    private   void store_PALETTES_line(String argLine) { sb_PALETTES.append(argLine).append("\n"); }
    private String store_TABS_line    (String argLine)
    {
        String np_name = String.format("tab%03d", ++tabNum);
        // ..........................................................[name]....................
        // ......................................................TAB.tab002=type...............
        argLine = argLine.replaceFirst(".*=type", String.format("TAB.%s=type", np_name));
//PROFILE//Settings.MON(TAG_PROFILE, "store_TABS_line", "argLine=["+argLine+"]");

    //  if(!argLine.contains("shape")) argLine = inject_missing_field(argLine, "shape"); // XXX early format quick-fix-patch

        sb_TABS.append(argLine).append("\n");

        return np_name;
    }
    //}}}
    // inject_missing_field {{{
    private String inject_missing_field(String argLine, String field)
    {
        //System.err.println("inject_missing_field("+ field +"):");
        //System.err.println("...argLine=["+ argLine +"]");
        try {
            int idx = argLine.indexOf("tt="); //......................................// locate last field
            argLine = argLine.substring(0,idx) + field+"=|" + argLine.substring(idx); // insert missing field (with an empty value)
        }
        catch(StringIndexOutOfBoundsException ex) {
            log("*** indexOf("+field+") StringIndexOutOfBoundsException:\n"+ ex);
        }
        //System.err.println("...argLine=["+ argLine +"]");
        return argLine;
    }
    //}}}
    // CLEAR {{{
    private void clear_profile_buffers()
    {
        clear_KEY_VAL_sb ();
        clear_TABS_sb    ();
        clear_PALETTES_sb();
    }

    // FIXME (TEST PHASE ONLY) .. SHOULD BE PRIVATE
    public  void    clear_KEY_VAL_sb () { sb_KEY_VAL .delete(0, sb_KEY_VAL .length()); }
    public  void    clear_TABS_sb    () { sb_TABS    .delete(0, sb_TABS    .length()); }
    public  void    clear_PALETTES_sb() { sb_PALETTES.delete(0, sb_PALETTES.length()); }
    //}}}
    // ACCESS {{{
//  public  String  get_PROFILE_KEY_VAL()   { return sb_KEY_VAL .toString(); }
    public  String  get_PROFILE_TABS()      { return sb_TABS    .toString(); }
    public  String  get_PROFILE_PALETTES()  { return sb_PALETTES.toString(); }

    private boolean have_PROFILE_KEY_VAL()  { return sb_KEY_VAL .length() > 0; }
    public  boolean have_PROFILE_TABS()     { return sb_TABS    .length() > 0; }
    public  boolean have_PROFILE_PALETTES() { return sb_PALETTES.length() > 0; }

    // get_key_val {{{
    private String get_key_val(String key)
    {
        String     val = null;
        String[] lines = sb_KEY_VAL.toString().split("\n");
        String pattern = key +"=";              // ("PRODATE=")

        int        idx;
        for(int i=0; i < lines.length; ++i) {
            idx = lines[i].indexOf( pattern );  // "PRODATE="
            if(idx >= 0) {
                val = lines[i].substring(idx + pattern.length()).trim();
                break;
            }
        }

//*PROFILE*/Settings.MOM(TAG_PROFILE, "get_PRODATE("+key+") ...return val=["+val+"]");
        return val;
    }
    //}}}
    // get_PROFILE_KV_LINE {{{
    public  String  get_PROFILE_KV_LINE()
    {
        // CONTACTENATE MULTI-LINE KEYVAL
        String s = sb_KEY_VAL.toString().replace("\n#", " ") ;

        while(s.indexOf("  ") > 0) s = s.replace("  ", " "); // remove double-spaces

        return s;
    }
    //}}}
    //}}}
    // loadSettingsLine {{{
    private void   loadSettingsLine(String argLine)
    {
    //  String /* ............................................................ */   s = Settings.SYMBOL_BUG    ;
        if     ( is_a_comment_line( argLine ) ) { store_KEY_VAL_line ( argLine ); /*s = Settings.SYMBOL_KEY_VAL;*/}
        else if( is_a_tab_line    ( argLine ) ) { store_TABS_line    ( argLine ); /*s = Settings.SYMBOL_TABS   ;*/}
        else if( is_a_palette_line( argLine ) ) { store_PALETTES_line( argLine ); /*s = Settings.SYMBOL_PALETTE;*/}

//PROFILE//Settings.MON(TAG_PROFILE, " ["+ argLine +"]");
    }
    //}}}
    // is_a_palette_line {{{
    private boolean is_a_palette_line(String argLine)
    {
        return !within_workbench
            &&  argLine.startsWith("PALETTE.")
            ;
    }
    //}}}
    // is_a_comment_line {{{
    private boolean is_a_comment_line(String argLine)
    {
        return  argLine.startsWith("#")
            && (argLine.indexOf   ("=") > 0)
            ;
    }
    //}}}
    // is_a_tab_line {{{
    private boolean is_a_tab_line(String argLine)
    {
        return          argLine.startsWith("TAB."         )
            && (       (argLine.indexOf   ("type=SHORTCUT") > 0)
                    || (argLine.indexOf   ("type=CONTROL" ) > 0)
               )
            ;
    }
    //}}}
    //}}}

    /** UPDATE */
    //{{{
    // RinseAll_TABS {{{
    public static String RinseAll_TABS(Profile profile)
    {
//*PROFILE*/Settings.MOC(TAG_PROFILE, "RinseAll_TABS");
        // {{{
        String error_msg = null;
        if(profile == null) {
            error_msg = "Profile.RinseAll_TABS(profile):\n* (profile == null)";
            return error_msg;
        }

        int sb_TABS_length = profile.sb_TABS.length();
//*PROFILE*/Settings.MOM(TAG_PROFILE, "...profile.sb_TABS.length()=["+ sb_TABS_length +"]");

        String text_empty = Settings.SYMBOL_EMPTY;   // DISCARD CURRENT
        String tag_empty  = Settings.COMMENT_STRING; // DISCARD CURRENT
        String xy_wh      = "";                      // KEEP    CURRENT GEOMETRY
        String shape      = "";                      // KEEP    CURRENT SHAPE
        String tt         = "";                      // DISCARD CURRENT TOOLTIP

        int idx0=0, idx1=0, idx2=0;
        // }}}
        try {
        // FREE (all is_a_rinsable_tag) profile's sb_TABS [xy_wh] [tag] [text] [shape] [tt] properties fields {{{
            while((idx0 < sb_TABS_length) && (idx0 >= 0))
            {
                idx1       = profile.sb_TABS.indexOf("tag=", idx0);
                if(idx1 < 0) break;

                // CHECK RINSABLE TABS
                idx2       = profile.sb_TABS.indexOf("|"   , idx1);
                String tag = profile.sb_TABS.substring(idx1, idx2);

                if( is_a_rinsable_tag(tag) ) {
                    idx0 = _Update_TAB_fields(profile, idx0, text_empty, tag_empty, xy_wh, shape, tt);
                }
                else {
//*PROFILE*/Settings.MOM(TAG_PROFILE, "...KEEPING TAG=["+tag+"]");
                    idx0 = profile.sb_TABS.indexOf("\n", idx0);
                    if(idx0 >= 0) idx0 += 1;
                }
            }
        // }}}
        } catch(Exception ex) {
            error_msg = "Profile.RinseAll_TABS("+profile.name+"):\n* Exception(idx0=["+idx0+"] idx1=["+idx1+"] idx2=["+idx2+"]):\n*"+ ex;
//*PROFILE*/ex.printStackTrace();//TAG_PROFILE
        }
        return error_msg;
    }

    private static boolean is_a_rinsable_tag(String tag)
    {
        tag = tag.toLowerCase();
        // NON-RINSABLE PROFILE {{{
        if(tag.contains("profile"))
        {
            if(tag.contains("index")) return false;
            if(tag.contains("profiles_table")) return false;
            if(tag.contains("controls_table")) return false;
        }
        //}}}
        // NON-RINSABLE URL {{{
        if(tag.contains("http"))
        {
            if(tag.contains("google")) return false;
        }
        //}}}
        return true;
    }
    //}}}
    // Add_TAB {{{
    public static String Add_TAB(Profile profile, NotePane np)
    {
//*PROFILE*/Settings.MOC(TAG_PROFILE, "Add_TAB("+np+")");

        return profile.store_TABS_line( np.toLine() );
    }
    //}}}
    // Update_TAB {{{
    public static void Update_TAB(Profile profile, NotePane np)
    {
//*PROFILE*/Settings.MOC(TAG_PROFILE, "Update_TAB("+ np.name +")");

        // SEARCH np's PROFILE LINE
        int idx0  = profile.sb_TABS.indexOf("TAB."+ np.name);
        if( idx0 >= 0)
            _Update_TAB_fields(profile, idx0, np.text, np.tag, np.get_xy_wh(), np.shape, np.tt);
    }
    //}}}
    // _Update_TAB_fields {{{
    private static int _Update_TAB_fields(Profile profile, int idx0, String text, String tag, String xy_wh, String shape, String tt)
    {
// MON (sb_TABS) {{{
//PROFILE//Settings.MON(TAG_PROFILE,"...sb_TABS.length()=["+ profile.sb_TABS.length() +"]");
//PROFILE//Settings.MON(TAG_PROFILE,"@@@\n"+ profile.sb_TABS.toString() +"\n@@@\n");
//}}}
// MON (ENCODED TEXT AND TAG) {{{
//*PROFILE*/Settings.MOC(TAG_PROFILE, "_Update_TAB_fields");
//*PROFILE*/Settings.MOM(TAG_PROFILE, "........text=["+                     ( text )+"]");
//*PROFILE*/Settings.MOM(TAG_PROFILE, "encode_text =["+NotePane.encode_text ( text )+"]");
//*PROFILE*/Settings.MOM(TAG_PROFILE, "encode_label=["+NotePane.encode_label( text )+"]");
//*PROFILE*/Settings.MOM(TAG_PROFILE, "........tag =["+                     ( tag  )+"]");
//*PROFILE*/Settings.MOM(TAG_PROFILE, ".encode_tag =["+NotePane.encode_tag  ( tag  )+"]");
//}}}
        // update tag=...| and text=...|
        int idx1=0, idx2=0, idx_EOL;
        try {
// MON (OLD PROFILE LINE) {{{
//*PROFILE*/                idx_EOL = profile.sb_TABS.indexOf("\n", idx0);// TAG_PROFILE
//*PROFILE*/if(idx_EOL < 0) idx_EOL = profile.sb_TABS.length();           // TAG_PROFILE
//*PROFILE*/Settings.MOM(TAG_PROFILE, "...OLD=["+ profile.sb_TABS.substring(idx0, idx_EOL) +"]");
//}}}
            // tag= (optional) {{{
            // TAB.tab2=type=SHORTCUT|xy_wh=01,03,12,02|shape=square|color=|zoom=1|tag=https://fr.m.wikipedia.org/|text=Wikipedia|tt=
            // <idx0---------------------------------------------------------idx1>|-------------------------------|<idx2
            idx1     = profile.sb_TABS.indexOf("tag="  , idx0);
            if(idx1 >= 0) {
                idx2     = profile.sb_TABS.indexOf("|"     , idx1);
                profile.sb_TABS.replace(idx1, idx2,"tag="  + NotePane.encode_tag  ( tag  ));
            }

            //}}}
            // xy_wh= (optional) {{{
            if(xy_wh != "") // left unchanged if none given [KEEP CURRENT]
            {
                idx1     = profile.sb_TABS.indexOf("xy_wh=", idx0);
                idx2     = profile.sb_TABS.indexOf("|"     , idx1);
                profile.sb_TABS.replace(idx1, idx2,"xy_wh="+ xy_wh);

            }
            //}}}
            // shape= (optional) {{{
            //if(shape != "") // left unchanged if none given [KEEP CURRENT] // may fallback to NotePane.Get_current_shape
            //{
                idx1     = profile.sb_TABS.indexOf("shape=", idx0);
                idx2     = profile.sb_TABS.indexOf("|"     , idx1);
                profile.sb_TABS.replace(idx1, idx2,"shape="+ shape);

            //}
            //}}}
            // text= (NOT OPTIONAL) .. (i.e. IF NOT FOUND: replace FROM:[text=] TO:[EOR]) {{{
            // TAB.tab2=type=SHORTCUT|xy_wh=01,03,12,02|shape=square|color=|zoom=1|tag=https://fr.m.wikipedia.org/|text=Wikipedia|tt=
            // <idx0-----------------------------------------------------------------------------------------idx1>|--------------|<idx2
            idx1     = profile.sb_TABS.indexOf("text=" , idx0);
            if(idx1  < idx0) {
                int profile_sb_TABS_length = profile.sb_TABS.length();
//*PROFILE*/Settings.MOM(TAG_PROFILE, "*** BROKEN RECORD: [text=] NOT FOUND");
                idx1 = idx0;
//*PROFILE*/Settings.MOM(TAG_PROFILE, "*** idx1 FORCED TO idx0=["+idx0+"]");
            }
            idx2     = profile.sb_TABS.indexOf("|"     , idx1);
            if(idx2 <= idx1) {
                int profile_sb_TABS_length = profile.sb_TABS.length();
//*PROFILE*/Settings.MOM(TAG_PROFILE, "*** BROKEN RECORD: text=[...]| ... idx1=["+idx1+"] .. (should be < idx2=["+idx2+"]) < profile_sb_TABS_length=["+profile_sb_TABS_length+"]");
                idx2 = profile_sb_TABS_length;
//*PROFILE*/Settings.MOM(TAG_PROFILE, "*** idx2 FORCED TO profile_sb_TABS_length=["+idx2+"]");
            }
        //  profile.sb_TABS.replace(idx1, idx2,"text=" + NotePane.encode_label( text ));
            profile.sb_TABS.replace(idx1, idx2,"text=" + NotePane.encode_text ( text )); // XXX

            //}}}
            // tt= (optional) {{{
            idx1     = profile.sb_TABS.indexOf("tt="   , idx0);
            if(idx1 >= 0) {
                idx2     = profile.sb_TABS.indexOf("\n"    , idx1);
                profile.sb_TABS.replace(idx1, idx2,"tt="   + NotePane.encode_text( tt   ));
            }

            //}}}
// MON (NEW PROFILE LINE) {{{
//*PROFILE*/                 idx_EOL = profile.sb_TABS.indexOf("\n", idx0); //TAG_PROFILE
//*PROFILE*/if( idx_EOL < 0) idx_EOL = profile.sb_TABS.length();            //TAG_PROFILE
//*PROFILE*/Settings.MOM(TAG_PROFILE, "...NEW=["+ profile.sb_TABS.substring(idx0 , idx_EOL) +"]");
//PROFILE//Settings.MOM(TAG_PROFILE, "...ENC=["+ Settings.Encode_UTF16_tokens( profile.sb_TABS.substring(idx0 , idx_EOL) ) +"]");
//}}}
        }
        catch(StringIndexOutOfBoundsException ex) {
            log("*** Profile._Update_TAB_fields(idx0="+idx0+") StringIndexOutOfBoundsException:\n"+ ex);
//*PROFILE*/ex.printStackTrace();//TAG_PROFILE
            return -1;
        }
        profile.pending_changes += 1;
//*PROFILE*/Settings.MOM(TAG_PROFILE, "_Update_TAB_fields() ...profile["+ profile.name +"]: pending_changes=["+profile.pending_changes+"]");
        return idx2+1;
    }
    //}}}
    // Update_KEY_VAL {{{
    private static void Update_KEY_VAL(Profile profile, String profile_name)
    {
//*PROFILE*/Settings.MOC(TAG_PROFILE, "Update_KEY_VAL("+ profile_name +")");
        // SELECT AND LOAD THE PROFILE TO UPDATE {{{
        if((profile == null) || !profile.name.equals( profile_name ))
            profile = new Profile( profile_name );

        // }}}
        // FETCH PROFILE'S KEYVAL UPDATE .. f(current Settings values) {{{
        Settings.PRODATE        = (int)(System.currentTimeMillis() / 1000);
        String prodate_str      = DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()));

        String profile_line     = "# PROFILE="   +  profile.name;
        String prodate_line     = "# PRODATE="   + Settings.PRODATE +" "+ prodate_str +" (by "+ Settings.DEVICE +")";
        String palette_line     = "# PALETTE="   + Settings.PALETTE;
        String opacity_line     = "# OPACITY="   + Settings.OPACITY;
        String maxcolors_line   = "# MAXCOLORS=" + Settings.MAXCOLORS;

        // }}}
        // REPLACE .. f(already existing KEYVAL) {{{
        StringBuilder sb = new StringBuilder();
        String[]  lines = profile.sb_KEY_VAL.toString().split("\n");
        for(int i=0; i< lines.length; ++i)
        {
            if     (lines[i].indexOf(  "PROFILE=") > 0) { sb.append( profile_line   ).append("\n");/*log("...OLD=["+ lines[i] +"]"); log("...NEW=["+ profile_line   +"]");*/ profile_line   = "";} // consume committed change
            else if(lines[i].indexOf(  "PRODATE=") > 0) { sb.append( prodate_line   ).append("\n");/*log("...OLD=["+ lines[i] +"]"); log("...NEW=["+ prodate_line   +"]");*/ prodate_line   = "";} // consume committed change
            else if(lines[i].indexOf(  "PALETTE=") > 0) { sb.append( palette_line   ).append("\n");/*log("...OLD=["+ lines[i] +"]"); log("...NEW=["+ palette_line   +"]");*/ palette_line   = "";} // consume committed change
            else if(lines[i].indexOf(  "OPACITY=") > 0) { sb.append( opacity_line   ).append("\n");/*log("...OLD=["+ lines[i] +"]"); log("...NEW=["+ opacity_line   +"]");*/ opacity_line   = "";} // consume committed change
            else if(lines[i].indexOf("MAXCOLORS=") > 0) { sb.append( maxcolors_line ).append("\n");/*log("...OLD=["+ lines[i] +"]"); log("...NEW=["+ maxcolors_line +"]");*/ maxcolors_line = "";} // consume committed change
            else                                        { sb.append( lines[i]       ).append("\n");/*                                                                     */                     } // keep unchanged values
        }

        // }}}
        // ADD NEW .. f(not consumed above) {{{
        if( profile_line   != "")                       { sb.append( profile_line   ).append("\n");/*                                log("...NEW=["+ profile_line   +"]");*/                     } // commit not consumed change
        if( prodate_line   != "")                       { sb.append( prodate_line   ).append("\n");/*                                log("...NEW=["+ prodate_line   +"]");*/                     } // commit not consumed change
        if( palette_line   != "")                       { sb.append( palette_line   ).append("\n");/*                                log("...NEW=["+ palette_line   +"]");*/                     } // commit not consumed change
        if( opacity_line   != "")                       { sb.append( opacity_line   ).append("\n");/*                                log("...NEW=["+ opacity_line   +"]");*/                     } // commit not consumed change
        if( maxcolors_line != "")                       { sb.append( maxcolors_line ).append("\n");/*                                log("...NEW=["+ maxcolors_line +"]");*/                     } // commit not consumed change

        // }}}
        // REPLACE PROFILE's (whole sb_KEY_VAL) {{{
        profile.clear_KEY_VAL_sb();
        profile.sb_KEY_VAL.append( sb );

        //}}}
    }
    //}}}
    // Save_Profile {{{
    public static String Save_Profile(Profile profile) { return Save_Profile(profile, profile.name); }
    public static String Save_Profile(Profile profile, String profile_name)
    {
//*PROFILE*/Settings.MOC(TAG_PROFILE, "Save_Profile(profile.name=["+profile.name+"], profile_name=["+profile_name+"])");
        //{{{
        String error_msg = null;
        if(profile == null) {
            error_msg = "Profile.Save_Profile(profile):\n* (profile == null)";
            return error_msg;
        }
        //}}}
        // RENAMING {{{
        if( !TextUtils.equals(profile.name, profile_name) )
        {
//*PROFILE*/Settings.MOM(TAG_PROFILE, "...setting profile.name=["+profile_name+"]");
            profile.name = profile_name;
        }
        //}}}
        try {
            // KEY_VAL .. [PROFILE] [PRODATE] [PALETTE] [OPACITY] {{{
            Update_KEY_VAL(profile, profile.name);

            //}}}
            // PALETTES .. SYPHON ALL WORKBENCH PALETTES {{{
            StringBuilder sb = new StringBuilder();

/*          // PALETTES: FILTER CURRENT PALETTE TO KEEP A SINGLE CHOICE {{{

            String[]  lines = profile.sb_PALETTES.toString().split("\n");

            for(int i=0; i< lines.length; ++i) {
                if(lines[i].indexOf("="+Settings.PALETTE) > 0) { sb.append(lines[i] +"\n"); log("...PALETTE=["+ lines[i] +"]"); break; }
            }

            profile.clear_PALETTES_sb();

            profile.sb_PALETTES.append( sb );
*/ // }}}

            //}}}
            // TABS .. FILTER OUT WORKBENCH AND COLOR_TOOL TABS FROM LAST LOADED AGGREGATE BUFFERS {{{
            sb.delete(0, sb.length());

            String[]  lines = profile.sb_TABS.toString().split("\n");
            for(int i=0; i< lines.length; ++i) {
                if( !lines[i].contains("type=TOOL" ))
                    sb.append(lines[i]).append("\n");
            }

            profile.clear_TABS_sb();

            profile.sb_TABS.append( sb );

            //}}}
            // SAVE TO FILE // {{{
            String file_text
                =   profile.sb_KEY_VAL .toString()
                +   profile.sb_PALETTES.toString()
                +   profile.sb_TABS    .toString()
                ;

            Profile.Save(profile.name, file_text);

            profile.pending_changes = 0;
            // }}}
        } catch(Exception ex) {
            error_msg = "Profile.Save_Profile("+profile.name+"):\n* Exception:\n*"+ ex;
//*PROFILE*/ex.printStackTrace();//TAG_PROFILE
        }
        return error_msg;
    }
    //}}}
    //}}}

    /** WORKBENCH */
    //{{{
    // WORKBENCH_TEMPLATES {{{
    // {{{
    private static final String   WORKBENCH_BASE_NAME = "WORKBENCH";
    private static final String   WORKBENCH_FULL_PATH = "WORKBENCH/WORKBENCH";
    private static final String     TEMPLATE_CONTROLS = "CONTROLS";
    private static final String     TEMPLATE_PROFILES = "PROFILES";
    private static final String     TEMPLATE_FREETEXT = "FREETEXT";

    private static final String[] WORKBENCH_TEMPLATES = { "H8" ,"H16", "G8" ,"G16" , TEMPLATE_CONTROLS , TEMPLATE_PROFILES };

    // }}}
    // get_WORKBENCH_PROFILES_LIST {{{
    public  static String get_WORKBENCH_PROFILES_LIST()
    {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < WORKBENCH_TEMPLATES.length; ++i)
            sb.append(" ").append(WORKBENCH_TEMPLATES[i]);
        return sb.toString();
    }
    //}}}
    // is_a_WORKBENCH_TEMPLATE_NAME {{{
    public static boolean is_a_WORKBENCH_TEMPLATE_NAME(String profile_name)
    {
        return profile_name.startsWith( WORKBENCH_BASE_NAME )
            || profile_name.startsWith( TEMPLATE_FREETEXT   )
            ||           Arrays.asList( WORKBENCH_TEMPLATES ).contains( profile_name )
            ;
    }
    //}}}
    // is_an_empty_WORKBENCH_PROFILE_NAME {{{
    public static boolean is_an_empty_WORKBENCH_PROFILE_NAME(String profile_name)
    {
        return profile_name.equals(WORKBENCH_FULL_PATH);
    }
    //}}}
    //}}}
    // LoadWorkbenchFromFile {{{
    private static ArrayList<String> LoadWorkbenchFromFile(String file_path)
    {
        if( !new File( file_path ).exists() ) {
            log("*** Profile.LoadWorkbenchFromFile("+ file_path +") ...file not found");
            return null;
        }

        ArrayList<String>       al      = new ArrayList<>();
        InputStream             is      = null;
        try {
            File                file    = new File( file_path );
            is                          = new BufferedInputStream(new FileInputStream(file));
            InputStreamReader   isr     = new InputStreamReader  ( is  );
            BufferedReader      br      = new BufferedReader     ( isr );
            String s = null;
            do {
                s = br.readLine();
                if(s != null) al.add(s);
            }
            while(s != null);
        }
        catch(Exception ex) {
            log("*** Profile.LoadWorkbenchFromFile("+file_path+") Exception:\n"+ ex);
        }
        finally {
            try { if(is != null) is.close(); } catch(Exception ignored) {}
        }
        return al;
    }
    //}}}
    // loadWorkbenchModel {{{
    private void loadWorkbenchModel(String profile_base_name) { loadWorkbenchItem(profile_base_name, false); }
    private void loadWorkbenchTools(String profile_base_name) { loadWorkbenchItem(profile_base_name, true ); }
    private void loadWorkbenchItem (String profile_base_name, boolean filter_tools)
    {
//*PROFILE*/Settings.MOC(TAG_PROFILE, "loadWorkbenchItem("+profile_base_name+", filter_tools="+filter_tools+")");

        ArrayList<String> lines = null;

        if     ( profile_base_name.equals    ( TEMPLATE_CONTROLS ) ) lines = build_CONTROLS_TABLE_TABS_lines();
        else if( profile_base_name.equals    ( TEMPLATE_PROFILES ) ) lines = build_PROFILES_TABLE_TABS_lines();
        else if( profile_base_name.startsWith( TEMPLATE_FREETEXT ) ) lines = build_FREETEXT_TABLE_TABS_lines( profile_base_name );
        else                                                         lines = LoadWorkbenchFromFile( Settings.Get_Profiles_dir()+"/WORKBENCH/"+profile_base_name+".txt");

        if(lines == null) return;

        for(int i=0; i < lines.size(); ++i)
        {
            // WORKBENCH PROFILES SHOULD ONLY CONTAIN working lines
            // ... "type=#"     .. stem cells to be mapped to something by the editor
            // ... "Type=TOOL"  .. workbench tool tabs

            String line = lines.get(i);
            if( is_a_tab_line( line ) )
            {
                // ACTIVE TABS ARE WORKBENCH TOOLS ONLY
                if( filter_tools ) {
                    line = line.replaceFirst("SHORTCUT", "TOOL");
//*PROFILE*/Settings.MOM(TAG_PROFILE, "line=["+ line +"]");
                }
                store_TABS_line( line );
            }
            // PALETTE LINES .. ONLY WHEN LOADING WORKBENCH ITSELF
            else if( profile_base_name.equals(WORKBENCH_BASE_NAME) )
            {
                loadSettingsLine( line );
            }
        }
        // give a default profile name
        if( !filter_tools )
            name = profile_base_name;
    }
    //}}}
    //}}}

    /** DYNAMIC PROFILES */
    //{{{
    //* PALETTES */ //{{{
    private static String DYNAMIC_TABLE_PALETTE = "W8";
    private void load_BUILTIN_PALETTES()
    {
        loadSettingsLine("PALETTE.1=W8,#F0A30A,#82592C,#0050EF,#A20025,#1BA1E2,#D80073,#A3C300,#6A00FF,#5FA917,#008A00,#765F89,#6C8764,#FA6800,#F471D0,#E51300,#7A3B3F,#647687,#00ABA9,#AA00FF,#D7C000");
        loadSettingsLine("PALETTE.2=Back,#2E1700,#4E0000,#4E0038,#2D004E,#1F0068,#001E4E,#004D60,#004A00,#15992A,#E56C19,#B81B1B,#B81B6C,#691BB8,#1B58B8,#569CE3,#00AAAA,#83BA1F,#D39D09,#E064B7");
        loadSettingsLine("PALETTE.3=Tile,#F3B200,#77B900,#2572EB,#AD103C,#632F00,#B01E00,#C1004F,#7200AC,#4617B4,#006AC1,#008287,#199900,#00C13F,#FF981D,#FF2E12,#FF1D77,#AA40FF,#1FAEFF,#56C5FF,#00D8CC,#91D100,#E1B700,#FF76BC,#00A3A3,#FE7C22");
        loadSettingsLine("PALETTE.4=Screen,#261300,#380000,#40002E,#250040,#180052,#001940,#004050,#003E00,#128425,#C35D15,#9E1716,#9E165B,#57169A,#16499A,#4294DE,#008E8E,#7BAD18,#C69408,#DE4AAD");
        loadSettingsLine("PALETTE.5=Screen-Tile,#543A24,#61292B,#662C58,#4C2C66,#423173,#2C4566,#306772,#2D652B,#3A9548,#C27D4F,#AA4344,#AA4379,#7F6E94,#6E7E94,#6BA5E7,#439D9A,#94BD4A,#CEA539,#E773BD");
        loadSettingsLine("PALETTE.6=ECC,#964B00,#FF0000,#FFA500,#FFFF00,#9ACD32,#6495ED,#EE82EE,#A0A0A0,#FFFFFF,#CFB53B,#C0C0C0");

        // hang on current PALETTE when possible
        if(sb_PALETTES.indexOf("=" + Settings.PALETTE) < 0)
            Settings.PALETTE = DYNAMIC_TABLE_PALETTE;
    }
    //}}}
    //* PROFILES_TABLE */ //{{{
    // {{{
    int     profile_section_color =       0;
    char            name_charAt_0 = (char)0;
    boolean      section_boundary =   false;
    int          section_cells_nb =       0; // avoid orphan cells .. (section of more than one items)

    // }}}
    // build_PROFILES_TABLE {{{
    // Get_Profiles_Dict_names {{{
    private static String[] Profiles_Dict_names = null;
    public  static String[] Get_Profiles_Dict_names()
    {
/* // {{{
        if(Profiles_Dict.size() == 0)
            return null;

        String[] profile_names = new String[Profiles_Dict.size()];
        int count = 0;
        for(Map.Entry<String,String> entry : Profiles_Dict.entrySet())
            profile_names[count++] = entry.getKey();

        Arrays.sort( profile_names );
*/ // }}}

        Profiles_Dict_names = new String[Profiles_Dict.size()];

        int i = 0;
        for(Map.Entry<String,String> entry : Profiles_Dict.entrySet())
            Profiles_Dict_names[i++] = entry.getKey();

        Arrays.sort( Profiles_Dict_names );

        return Profiles_Dict_names;
    }
    //}}}
    private void build_PROFILES_TABLE()
    {
//*PROFILE*/Settings.MON(TAG_PROFILE, "build_PROFILES_TABLE", "name=["+name+"]");

        _build_Profiles_Dict();

        // KEY_VAL [PROFILE] [PRODATE] {{{

        Settings.PROFILE = Settings.PROFILES_TABLE;
        Settings.PRODATE = 0;
        loadSettingsLine("# PROFILE=" + Settings.PROFILE);
        loadSettingsLine("# PRODATE=" + Settings.PRODATE + " " + DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())));

        //}}}
        // PALETTES {{{
        load_BUILTIN_PALETTES();

        // }}}
        // NO PROFILES INFO {{{
        if(Profiles_Dict.size() < 1)
        {
            // empty folder info
            String xy_wh        = "1,1,50,10";
            String text         = "No saved profile in "+ Settings.Get_Profiles_dir() +"\\n\\n"
                +                 "1. You can drop your own files in this folder.\\n\\n"
                +                 "2. Or install some builtin SAMPLE PROFILES using the control below."
                ;
            int    color        = 0; // i.e. random palette entry
            String tooltip      = "* PROFILES FOLDER FILES";
            String shape        = NotePane.SHAPE_TAG_PADD_R;
            String tab_line     = build_PROFILES_TABLE_Line_WITH_SHAPE_COLOR_TT(Settings.PROFILES_TABLE, text, xy_wh, shape, color, tooltip);

            loadSettingsLine( tab_line );
        }
        //}}}
        // TABLE OF ARCHIVED PROFILES {{{
        if(Profiles_Dict.size() < 1)
        {
            // unzip user archive
            String null_tag = null;
            if( get_ProfileArchiveList_size() > 0)
            {
                String control_name = "PROFILE_UNZIP";
                String xy_wh        = "1,12,50,10";
                String text         = control_name.replace("_"," ").trim()
                    + NotePane.INFO_SEP_ENCODED + "extract archived profiles";
                int    color        = 0; // i.e. random palette entry
                String tooltip      = "";
                loadSettingsLine( build_CONTROLS_TABLE_Line(control_name, text, null_tag, xy_wh, color, tooltip) );
            }
            // unzip factory archive
            else {
                String control_name = "PROFILE_UNZIP_DEFAULTS";
                String xy_wh        = "1,12,50,10";
                String text         = control_name.replace("_"," ").trim()
                    + NotePane.INFO_SEP_ENCODED + "extract built-in profiles";
                int    color        = 0; // i.e. random palette entry
                String tooltip      = "";
                loadSettingsLine( build_CONTROLS_TABLE_Line(control_name, text, null_tag, xy_wh, color, tooltip) );
            }

        }
        // }}}
        // LIST OF STORED PROFILES {{{
        else {
            ArrayList<String> stored_profile_name_list = get_stored_profile_name_list();
            // SHOW INDEX and ORPHAN PROFILES {{{
            // select tabs layout .. f(current GUI_TYPE) {{{
            boolean building_DOCKINGS_TABLE = name.equals(Settings.DOCKINGS_TABLE);
            boolean building_DOCKINGS_LIST  = building_DOCKINGS_TABLE && !Settings.is_GUI_TYPE_HANDLES();

            if( building_DOCKINGS_TABLE )
                NpButtonGridLayout.ResetAutoPlace(NpButtonGridLayout.GRID_SINGLE_COL, stored_profile_name_list.size());
            else
                NpButtonGridLayout.ResetAutoPlace(NpButtonGridLayout.GRID_MULTI_COL , stored_profile_name_list.size());

            //}}}
            // CHECK CURRENT ARCHIVE (for archived profiles markup) {{{
            if(ProfileArchiveList == null)
                check_ProfileArchiveList();

            //}}}
            // name format .. f(# profiles count) {{{
            String entry_name, text;
            String format = (stored_profile_name_list.size() > 99)
                ? "_%3d. %s"
                : "_%2d. %s";
            int count   = 0;
            name_charAt_0 = (char)0;
            //}}}
            profile_section_color =       0;
            name_charAt_0         = (char)0;
            section_boundary      =   false;
            section_cells_nb      =       0;
            for(int i=0; i < stored_profile_name_list.size(); ++i)
            {
                entry_name = stored_profile_name_list.get(i);

                // DOCK LAYOUT: root-profiles only {{{
                if( building_DOCKINGS_TABLE )
                {
                    text = entry_name;
                    if( !building_DOCKINGS_LIST )
                        text = text.replace("/", " "+ Settings.SYMBOL_BLACK_CIRCLE +" ");

                    // PROFILE SECTIONS MARKER
                    char           c = Character.toLowerCase( entry_name.charAt(0) );
                    section_boundary = (name_charAt_0 != (char)0) && (c != name_charAt_0);
                    name_charAt_0    =  c;

                    // BUILD AND LOAD PROFILE LINE
                    String          tab_line = build_PROFILES_TABLE_Line_WITH_SHAPE(entry_name, String.format(format, ++count, text), NotePane.SHAPE_TAG_PADD_R);
                    loadSettingsLine( tab_line );
                }
                //}}}
                // HANDLES LAYOUT: root-and-sub-folders-profiles {{{
                else {
                    int idx_base             = entry_name.lastIndexOf("/");       // remove parent folder-path
                    if( idx_base>= 0)   text = entry_name.substring(idx_base+1);  // ...select base-name
                    else                text = entry_name;                        // no parent folder-path to remove

                    // BUILD AND LOAD PROFILE LINE
                    String          tab_line = build_PROFILES_TABLE_Line_WITH_SHAPE(entry_name, text, NotePane.SHAPE_TAG_PADD_R);
                    loadSettingsLine( tab_line );
                }
                //}}}
            }
            //}}}
        }
        // }}}
//*PROFILE*/Settings.MOM(TAG_PROFILE, "build_PROFILES_TABLE ...done");
    }
    //}}}
    // build_PROFHIST_TABLE {{{
    private void build_PROFHIST_TABLE()
    {
        String caller = "build_PROFHIST_TABLE";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);
        // KEY_VAL [PROFILE] [PRODATE] {{{

        Settings.PROFILE = Settings.PROFHIST_TABLE;
        Settings.PRODATE = 0;
        loadSettingsLine("# PROFILE=" + Settings.PROFILE);
        loadSettingsLine("# PRODATE=" + Settings.PRODATE + " " + DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())));

        //}}}
        // PALETTES {{{
        load_BUILTIN_PALETTES();

        // }}}
        // GRID_MULTI_COL TABS LAYOUT FOR [history_profile_names] entries {{{
        ArrayList<String> history_profile_names = get_history_profile_names();
        NpButtonGridLayout.ResetAutoPlace(NpButtonGridLayout.GRID_MULTI_COL, history_profile_names.size(), 3); // cols_min

        //}}}
        // CHECK CURRENT ARCHIVE (for archived profiles markup) {{{
        if(ProfileArchiveList == null)
            check_ProfileArchiveList();

        //}}}
        // name format .. f(# profiles count) {{{
        String format = (history_profile_names.size() > 99)
            ? "_%3d. %s"
            : "_%2d. %s";
        int count   = 0;
        //}}}
        String entry_name, text;

        profile_section_color =       0;
        name_charAt_0         = (char)0;
        section_boundary      =   false;
        section_cells_nb      =       0;
        for(int i=-3; i < history_profile_names.size(); ++i)
        {
            // INJECT CONTROLS_TABLE AS THE FIRST ENTRIES
            switch(i) {
                case -3: entry_name = Settings.USER_INDEX    ; break;
                case -2: entry_name = Settings.CONTROLS_TABLE; break;
                case -1: entry_name = Settings.FINISH_APP    ; break;
                default:
                         entry_name = history_profile_names.get(i);
                         if(        entry_name.equals(Settings.USER_INDEX    )
                                 || entry_name.equals(Settings.CONTROLS_TABLE))
                             continue;
            }
//*PROFILE*/Settings.MOM(TAG_PROFILE, String.format("%3d entry_name=[%s]", i, entry_name));

            text = entry_name;
            text = text.replace("/", " "+ Settings.SYMBOL_BLACK_CIRCLE +" ");

            // PROFILE SECTIONS MARKER {{{
            if(i > 0) { //..................// ALPHABETIC SECION BOUNDARIES
                char           c = Character.toLowerCase( entry_name.charAt(0) );
                section_boundary = (name_charAt_0 != (char)0) && (c != name_charAt_0);
                name_charAt_0    =  c;
            }
            else if(i == 0) {
                section_boundary =  true;   // ENTERING TRUE PROFILES SECTION
            }
            else {
                section_boundary = false;   // CONTROLS SECTION
            }
            //}}}

            // BUILD AND LOAD PROFILE LINE
            String     shape = entry_name.equals( Settings.Working_profile )
                ?              NotePane.SHAPE_TAG_SQUARE
                :              NotePane.SHAPE_TAG_PADD_R;
            String          tab_line = build_PROFILES_TABLE_Line_WITH_SHAPE(entry_name, String.format(format, ++count, text), shape);
//*PROFILE*/Settings.MOM(TAG_PROFILE, "@@@ "+tab_line);
            loadSettingsLine( tab_line );
        }
//*PROFILE*/Settings.MON(TAG_PROFILE, caller, "...done");
    }
    //}}}
    // build_PROFILES_TABLE_Line_WITH_SHAPE {{{
    private String build_PROFILES_TABLE_Line_WITH_SHAPE(String entry_name, String text, String shape)
    {
        // SKIP SECTION BOUNDARY SLOT {{{
        String xy_wh;
        if(section_boundary && (section_cells_nb > 1))
        {
            section_cells_nb       = 1;
            xy_wh                  = NpButtonGridLayout.Get_free_xy_wh(1); // skip one slot
            profile_section_color += 1 ; // next dynamic color
        }
        else ++section_cells_nb;
        //}}}
        xy_wh           = NpButtonGridLayout.Get_free_xy_wh();
        // [text] [INFO] {{{
        if     ( entry_name.equals( Settings.CONTROLS_TABLE     )) text +=     NotePane.INFO_SEP_ENCODED + "Full CONTROLS_TABLE" ; // keep it first (i.e. top-left) (as a means to jump to profiles-table)
        else if( entry_name.equals( Settings.PROFILES_TABLE     )) text +=     NotePane.INFO_SEP_ENCODED + "Full PROFILES_TABLE" ; // keep it first (i.e. top-left) (as a means to jump to profiles-table)
        else if( entry_name.equals( Settings.FINISH_APP         )) text +=     NotePane.INFO_SEP_ENCODED + "Exit the application";
        else if( ProfileArchiveList.contains( entry_name+".txt" )) text +=     NotePane.INFO_SEP_ENCODED + "Has a saved version in archive";
        else                                                       text += "*"+NotePane.INFO_SEP_ENCODED + "Has NOT yet been saved in archive";

        //}}}
        int       color = profile_section_color;
        String  tooltip = "Load this profile";
        String tab_line = build_PROFILES_TABLE_Line_WITH_SHAPE_COLOR_TT(entry_name, text, xy_wh, shape, color, tooltip);
        return tab_line;
    }
    //}}}
    // build_PROFILES_TABLE_Line_WITH_SHAPE_COLOR_TT {{{
    private String build_PROFILES_TABLE_Line_WITH_SHAPE_COLOR_TT(String entry_name, String text, String xy_wh, String shape, int color, String tooltip)
    {
        String tab_name = "TAB.PROFILE_"+ entry_name;

        if(section_boundary)
            tooltip     = Settings.SECTION_BOUNDARY + tooltip;

        String tag;
        if( entry_name.equals(Settings.FINISH_APP) )
            tag = "FINISH 0";
        else
            tag = ((Settings.PROFILE == Settings.PROFHIST_TABLE) && entry_name.equals( Settings.Working_profile ))
                ?  "PROFHIST_TABLE "+ entry_name   // works as a toggle
                :  "PROFILE "       + entry_name;

        String tab_value
            = "type="   + "SHORTCUT"
            +"|tag="    + tag
            +"|zoom="   + "1"
            +"|xy_wh="  + xy_wh
            +"|text="   + text
            +"|color="  + color
            +"|shape="  + shape
            +"|tt="     + tooltip
            ;

        return tab_name + "=" + tab_value;
    }
    //}}}
    // build_PROFILES_TABLE_TABS_lines {{{
    private ArrayList<String> build_PROFILES_TABLE_TABS_lines()
    {
        String caller = "build_PROFILES_TABLE_TABS_lines";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);
        // GRID_MULTI_COL TABS LAYOUT FOR [stored_profile_name_list] entries {{{
        name = TEMPLATE_PROFILES;
        ArrayList<String> stored_profile_name_list = get_stored_profile_name_list();

        NpButtonGridLayout.ResetAutoPlace(NpButtonGridLayout.GRID_MULTI_COL, stored_profile_name_list.size());
        ArrayList<String> al = new ArrayList<>();
        String entry_name, text;

        profile_section_color =       0;
        name_charAt_0         = (char)0;
        section_boundary      =   false;
        section_cells_nb      =       0;
        for(int i=0; i < stored_profile_name_list.size(); ++i)
        {
            // TAG [label] [text] {{{
            entry_name = stored_profile_name_list.get(i);
            text       = entry_name;
            text       = text.replace("/", " "+ Settings.SYMBOL_BLACK_CIRCLE +" ");

            //}}}
            // SECTIONS MARKER .. (to skip one slot) {{{
            char           c = Character.toLowerCase( entry_name.charAt(0) );
            section_boundary = (name_charAt_0 != (char)0) && (c != name_charAt_0);
            name_charAt_0    =  c;

            //}}}
            // BUILD AND COLLECT TAG LINE {{{
            String          tab_line = build_PROFILES_TABLE_Line_WITH_SHAPE(entry_name, text, NotePane.SHAPE_TAG_PADD_R);
//*PROFILE*/Settings.MOM(TAG_PROFILE, "@@@ "+tab_line);

            al.add(         tab_line);
            // }}}
        }
        //}}}
        name = name.toLowerCase();
//*PROFILE*/Settings.MON(TAG_PROFILE, caller, "...return al[x"+al.size()+"] entries for TEMPLATE_PROFILES ["+name+"]");
        return al;
    }
    //}}}
    // get_stored_profile_name_list {{{
    private ArrayList<String> get_stored_profile_name_list()
    {
        String caller = "get_stored_profile_name_list";
//*PROFILE*/Settings.MON(TAG_PROFILE, caller, "name=["+name+"]");
        // [Profiles_Dict_names] - SORT PROFILE NAMES INTO A STRING ARRAY {{{
        Profiles_Dict_names = new String[Profiles_Dict.size()];
        int i = 0;
        for(Map.Entry<String,String> entry : Profiles_Dict.entrySet())
            Profiles_Dict_names[i++] = entry.getKey();

        // SELECT & SORT
        boolean building_TEMPLATE_PROFILES  = name.equals( TEMPLATE_PROFILES );
        boolean building_DOCKINGS_TABLE     = name.equals(Settings.DOCKINGS_TABLE);
        boolean building_DOCKINGS_LIST      = building_DOCKINGS_TABLE && !Settings.is_GUI_TYPE_HANDLES();

        boolean collect_root_files_only     = ( building_DOCKINGS_LIST || building_TEMPLATE_PROFILES);
        boolean collect_file_path           = (!building_DOCKINGS_LIST && building_DOCKINGS_TABLE   );

        // COLLECT THEM ALL WHEN CHOOSING A USER FILE TO CUSTOMIZE
        boolean collect_all_files           = Settings.Working_profile.equals(WORKBENCH_FULL_PATH) && !building_DOCKINGS_TABLE;

        if( collect_file_path )
            Arrays.sort( Profiles_Dict_names );   // on path
        else
            Arrays.sort( Profiles_Dict_names      // on name
                    , new Comparator<String>() {
                        @Override
                        public int compare(String lhs, String rhs) {
                            int idx;
                            idx = lhs.lastIndexOf("/"); if(idx >= 0) lhs = lhs.substring(idx+1);
                            idx = rhs.lastIndexOf("/"); if(idx >= 0) rhs = rhs.substring(idx+1);
                            return lhs.compareToIgnoreCase(rhs);
                        }
                    });

        // }}}
        // [indexed_folderList] - COLLECT FOLDERS CONTAINING AN INDEX PROFILE (NAMED AFTER THEIR CONTAINING FOLDER) {{{
        ArrayList<String> indexed_folderList = new ArrayList<>();
        for(i=0; i < Profiles_Dict_names.length; ++i)
        {
            // check if base folder has the same name (i.e. DOC/Doc.txt)
            int idx       = Profiles_Dict_names[i].lastIndexOf("/");
            if(idx < 0)     continue;

            String base   = Profiles_Dict_names[i].substring(   idx+1);
            String head   = Profiles_Dict_names[i].substring(0, idx);             // parent path

            idx           = head.lastIndexOf("/");                          // check direct parent name
            String parent = (idx > 0) ? head.substring(idx+1) : head;

            if(base.endsWith(".txt") ) base = base.substring(0, base.length()-4);

            if( base.equalsIgnoreCase( parent ) )
            {
                indexed_folderList.add( head );
//PROFILE//Settings.MON(TAG_PROFILE, "...indexed_folderList.add["+ head +")");
            }

        }
        // }}}
        // COLLECT INDEX and ORPHAN PROFILES {{{
        ArrayList<String> stored_profile_name_list = new ArrayList<>();
        String profile_name;
        int    first_entry = building_DOCKINGS_TABLE ? -2 : -1; // skip PROFILES_TABLE for Handle.FULLPAGEDISPLAY_LEFT
        for(i= first_entry; i < Profiles_Dict_names.length; ++i)
        {
            // INJECT PROFILES_TABLE & CONTROLS_TABLE AS THE FIRST ENTRIES
            switch(i) {
                case -2: profile_name = Settings.PROFILES_TABLE; break;
                case -1: profile_name = Settings.CONTROLS_TABLE; break;
                default: profile_name = Profiles_Dict_names[i];
            }

//PROFILE//Settings.MON(TAG_PROFILE, "XXX ...profile_name["+i+"]=["+ profile_name +"]");
            if(profile_name.equals("")) continue; // wrong file_path (i.e. ".txt")

            int idx = profile_name.lastIndexOf("/");
            // ALWAYS SHOW ROOT-FOLDER PROFILES
            if(idx < 0) {
                stored_profile_name_list.add( profile_name );
            }
            // SUB-FOLDER
            else {
                // HEAD PARENT BASE {{{
                String base   = profile_name.substring(   idx+1);
                String head   = profile_name.substring(0, idx);         // parent path

                //  idx           = head.lastIndexOf("/");                      // check direct parent name
                //  String parent = (idx > 0) ? head.substring(idx+1) : head;

                if(base.endsWith(".txt") ) base = base.substring(0, base.length()-4);

                // }}}
                // COLLECT ALL [ROOT-FILES] {{{
                if( !profile_name.contains("/") )
                {
                    stored_profile_name_list.add( profile_name );
                }
                //}}}
                // COLLECT [SUB-FOLDERS-FILES] {{{
                else if( !collect_root_files_only )
                {
                    // COLLECT ALL {{{
                    if( collect_all_files )
                    {
                        stored_profile_name_list.add( profile_name );
                    }
                    // }}}
                    // COLLECT INDEXES {{{
                    else if( base.equalsIgnoreCase( head ))
                    {
                        stored_profile_name_list.add( profile_name );       // include sub-folder files
                    }
                    // }}}
                    // SKIP INDEXED FILES {{{
                    else {
                        // SKIP PROFILES HAVING AN INDEX ALONG THEIR FOLDER PATH
                        boolean hidden_profile = false;
                        idx    = profile_name.indexOf("/");
                        while(idx >= 0)
                        {
                            String folder = profile_name.substring(0, idx);
                            if( indexed_folderList.contains( folder ) )
                            {
//PROFILE//Settings.MON(TAG_PROFILE, "...INDEXED-PROFILE=["+ profile_name +"] is part of indexed_folderList=["+ folder +"]");
                                hidden_profile = true;
                                break;
                            }
                            idx = profile_name.indexOf("/", idx+1);
                        }

                        // NO INDEX FOUND IN PARENT FOLDERS CHAIN
                        if( !hidden_profile )
                            stored_profile_name_list.add( profile_name );
                    }
                    // }}}
                }
                //}}}
            }

        }
        //}}}
//*PROFILE*/Settings.MON(TAG_PROFILE, caller, "...return stored_profile_name_list[x"+stored_profile_name_list.size()+"]");
        return stored_profile_name_list;
    }
    //}}}
    // get_history_profile_names {{{
    private ArrayList<String> get_history_profile_names()
    {
        String caller = "get_history_profile_names";

        ArrayList<String>  al = RTabs.get_history_profile_names();

//*PROFILE*/Settings.MON(TAG_PROFILE, caller, "...return al[x"+al.size()+"]");
        return al;
    }
    //}}}
    //}}}
    //* CONTROLS_TABLE */ //{{{
    // [CONTROL_BUILTINS] {{{
    // handlers are addressed by activity.request_cmd_text
    private static final String[][] CONTROL_BUILTINS = {
        // see activity.request_cmd_text
        // CONTROLS PROFILES - PROFILES_* PRNEXT PRPREV INVENTORY PROFILETABS {{{
         { "PROFILES_TABLE" ,"FULL profiles table"                 } // keep it first (i.e. top-left) (as a means to jump to profiles-table)
        ,{ "PROFHIST_TABLE" ,"FULL profiles table"                 }
        ,{ "SOUNDS_TABLE"   ,"FULL sounds table"                   }
        ,{ "PROFILE_DELETE" ,"delete CURRENT PROFILE"              }
        ,{ "PROFILE_SAVE"   ,"save CURRENT PROFILE to file"        }
        ,{ "PROFILE_RINSE"  ,"cleanup all CURRENT PROFILE tabs\\n"
            +               "(yet to be saved after confirmation)" }
        ,{ "PROFILE_SYNC"   ,"rescan profile disc-storage"         }
        ,{ "PRPREV"         ,"select previous navigated profile"   }
        ,{ "PRNEXT"         ,"select next navigated profile"       }
        ,{ "INVENTORY"      ,"show profiles storage details"       }
        ,{ "PROFILETABS"    ,"show current profile details"        }

        ,{ "DESIGN"         ,"RUN RTabsDesigner on the Desktop"    , "RUN ../RTabsDesigner/RTabsDesigner.exe" }

        //}}}
        // CONTROLS ARCHIVE - ZIP DELETE SAMPLES {{{
        ,{"PROFILE_ZIP"                  ,"save ALL current profiles into YOUR PROFILES ARCHIVE" }
        ,{"PROFILE_UNZIP"                ,"extract YOUR LAST ARCHIVED PROFILES"         }
        ,{"PROFILE_ZIP_DELETE"           ,"delete YOUR PROFILE ARCHIVE"                 }
        ,{"PROFILE_UNZIP_DEFAULTS"       ,"extract SOUND RESOURCES & SAMPLE PROFILES\\n...\\n"
            +                             "- Existing files are overridden only by a newer version.\\n"
                +                         "- You can PROFILE_ZIP current files at some point,\\n"
                +                         "- DELETE SOME FILES at any time,\\n"
                +                         "- and use PROFILE_UNZIP to restore the deleted ones."
        }

        // }}}
        // CONTROLS SERVER - SIGNIN SERVER FREEZED FINISH - HIDE LOGGING CLEAR {{{
        ,{"SIGNIN"                       ,"(re)initiate server login"                   }
        ,{"SERVER"                       ,"current server connection details"           }
        ,{"FREEZED"                      ,"DEVICE-SERVER connection\\n"
            +                             "- show state\\n"
                +                         "- click to toggle\\n"
        }
        ,{"OFFLINE"                      ,"DEVICE-SERVER connection\\n"
            +                             "- show state\\n"
                +                         "- click to toggle\\n"
        }
        ,{"FINISH"                       ,"stop this app"                               }

        ,{"WOL"                          ,"SERVER WAKE ON LAN"                          }
        ,{"HIDE"                         ,"SERVER-SIDE toggle GUI"                      }
        ,{"LOGGING"                      ,"SERVER-SIDE toggle logging"                  }
        ,{"CLEAR"                        ,"SERVER-SIDE clear logging panels content"    }
        //}}}
        // CONTROLS INFO - STATUS MEMORY SAVE NOTE {{{
        ,{"STATUS"                       ,"\\n. Application name (flavor)\\n. build date\\n. age"}
        ,{"MEMORY"                       ,"\\n"
                +                         "MEMORY (3 numbers):\\n"
                +                         "- Max  .. HEAP CAN EXPAND TO\\n"
                +                         "- Total .. CURRENT HEAP SIZE\\n"
                +                         "- Free .. BEFORE  HAVING TO EXPAND THE HEAP\\n"
                +                         "...\\n"
                +                         "POOL (4 numbers):\\n"
                +                         "- New this run .. (# of tabs created)\\n"
                +                         "- Stacked in the pool to be reused .. (# of times)\\n"
                +                         "- Recycled from the pool for reuse .. (# of times)\\n"
                +                         "- Currently in the pool .. (# of tabs not used)"
                +                         "...\\n"
                +                         "A click will:\\n"
                +                         "- uncheck WVTools properties\\n"
                +                         "- release WebView pool instances\\n"
                +                         "- release   Sound pool instances\\n"
                +                         "- release SandBox resources\\n"
                +                         "- and call the Garbage Collector\\n"

        }

        ,{"SAVE"                         ,"save current settings choices\\n .. (those may be discarded when app is forcibly stopped)" }

        ,{"NOTE"                         ,"show..edit personal note" }
        // }}}
        ,{"COMMAND"                      ,"\\n. Execute Linux command"}
        // CONTROLS WEB - TOOL_URL WEBVIEW BOOKMARK {{{
        ,{"TOOL_URL"                     ,"WEB ACTION:"
            +     "\\n[ANDROID: WEB VIEW]"
                + "\\n[PC: BROWSER]"
                + "\\n[PC: SEND WEB ADDRESS]"
        }
        ,{"WEBVIEW"                      ,"reload last visited web page"                      }
        ,{"BOOKMARK"                     ,"load last bookmarked web page"                     }
        ,{"DEL_COOKIES"                  ,"remove all webpages cookies (i.e. scroll markers)" }

        // }}}
        // CONTROLS PALETTE - GUI_STYLE GUI_FONT GUI_TYPE - PLNEXT PLPREV FIT_W FIT_H {{{
        ,{"GUI_STYLE"                    ,"swap button STYLE: ROUND\u2194SQUARE\u2194ONEDGE" }
        ,{"GUI_FONT"                     ,"swap button FONT: DEFAULT\u2194NOTO"              }
        ,{"GUI_TYPE"                     ,"swap button TYPE: DEFAULT\u2194DOCK"              }
        ,{"MARK_SCALE_GROW"              ,"Current hovering marker expand factor"            }
        ,{"PLNEXT"                       ,"select next palette"                              }
        ,{"PLPREV"                       ,"select previous palette"                          }
        ,{"PALETTE"                      ,"Current palette"
                +                         "...\\n"
                +                         "- Palette name\\n"
                +                         "- Used = TAB-RESERVED colors\\n"
                +                         "- _max = currently ALLOWED colors\\n"
                +                         "- ___# = PALETTE colors"                          }

        ,{"FIT_H"                        ,"zoom fit to display height"                       }
        ,{"FIT_W"                        ,"zoom fit to display width"                        }

        ,{"PORTRAIT"                     ,"Request screen orientation PORTRAIT"              }
        ,{"LANDSCAPE"                    ,"Request screen orientation LANDSCAPE"             }
        ,{"SCREEN_ROTATION"              ,"Toggle Screen Auto Rotation"                      }

        // }}}
        // CONTROLS LOG - MONITOR LOG* ERRLOGGED {{{
        ,{"LOG_CAT"                      ,"direct logging to Android monitor (logcat)"  }
        ,{"MONITOR"                      ,"logs key parameters"                         }
        ,{"LOG"                          ,"toggle global logging"                       }
        ,{"LOG_ACTIVITY"                 ,"activate activity logging"                   }
        ,{"LOG_CLIENT"                   ,"activate client logging"                     }
        ,{"LOG_PARSER"                   ,"activate parser logging"                     }
        ,{"LOG_PROFILE"                  ,"activate profile logging"                    }
        ,{"LOG_SETTINGS"                 ,"activate settings logging"                   }
        ,{"ERRLOGGED"                    ,"direct logging to err.log file on device"    }

        // }}}
    };
    // is_PROFILES_BUILTIN {{{
    public static boolean is_PROFILES_BUILTIN(String cmd)
    {
        boolean diag = false;
        for(int i = 0; i < CONTROL_BUILTINS.length; i++) {
            if(cmd.equals( CONTROL_BUILTINS[i][0] )) {
                diag = true;
                break;
            }
        }

//*PROFILE*/Settings.MON(TAG_PROFILE, "is_PROFILES_BUILTIN("+ cmd +")", "...return "+ diag);
        return diag;
    }
    //}}}
    //}}}
    // build_CONTROLS_TABLE {{{
    private void build_CONTROLS_TABLE()
    {
//*PROFILE*/Settings.MOC(TAG_PROFILE, "build_CONTROLS_TABLE");

        // KEY_VAL [PROFILE] [PRODATE] {{{

        Settings.PROFILE = Settings.CONTROLS_TABLE;

        //int secondsNow = (int)(System.currentTimeMillis() / 1000);
        Settings.PRODATE = 0; //secondsNow; // !!! if this gets transmitted to the server, it would kill its current profile

        loadSettingsLine("# PROFILE=" + Settings.PROFILE);
        loadSettingsLine("# PRODATE=" + Settings.PRODATE + " " + DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())));

        //}}}
        // PALETTES {{{
        load_BUILTIN_PALETTES();

        // }}}
        // TABS .. [APPLICATION CONTROLS] {{{
        NpButtonGridLayout.ResetAutoPlace(NpButtonGridLayout.GRID_MULTI_COL, CONTROL_BUILTINS.length);
        String control_name, control_info, control_tag;

        profile_section_color =       0;
        name_charAt_0         = (char)0;
        section_boundary      =   false;
        section_cells_nb      =       0;
        for(int i=0; i < CONTROL_BUILTINS.length; i++)
        {
            // TAG [label] [info] [tag] {{{
            control_name     =   CONTROL_BUILTINS[i][0];
            control_info     =   CONTROL_BUILTINS[i][1];
            control_tag      = ((CONTROL_BUILTINS[i].length) < 3) ? null : CONTROL_BUILTINS[i][2];

            //}}}
            // SECTIONS MARKER .. (to skip one slot) {{{
            //char           c = Character.toLowerCase( control_name.charAt(0) );
            //section_boundary = (name_charAt_0 != (char)0) && (c != name_charAt_0);
            //name_charAt_0    =  c;
            // XXX too much segmentation for CONTROLS_TABLE

            //}}}
            // BUILD AND LOAD TAG LINE {{{
            String  tab_line = build_CONTROLS_TABLE_Line(control_name, control_info, control_tag);
//*PROFILE*/Settings.MOM(TAG_PROFILE, "@@@ "+tab_line);

            loadSettingsLine(tab_line);
            // }}}
        }

        //}}}
//*PROFILE*/Settings.MOM(TAG_PROFILE, "build_CONTROLS_TABLE ...done");
        }
    //}}}
    // build_CONTROLS_TABLE_Line {{{
    private String build_CONTROLS_TABLE_Line(String control_name, String control_info, String control_tag)
    {
        // SKIP SECTION BOUNDARY SLOT {{{
        String xy_wh;
        if(section_boundary && (section_cells_nb > 1)) // skip one slot
        {
            section_cells_nb       = 1;
            xy_wh                  = NpButtonGridLayout.Get_free_xy_wh(1); // skip one slot
            profile_section_color += 1 ; // next dynamic color
        }
        else ++section_cells_nb;
        //}}}
        xy_wh           = NpButtonGridLayout.Get_free_xy_wh();

        String text     = control_name.replace("_"," ").trim();
        if(control_info != "")
            text       += NotePane.INFO_SEP_ENCODED + control_info;

        int    color    = 0; // i.e. random palette entry
        String tooltip  = "";
        String tab_line = build_CONTROLS_TABLE_Line(control_name, text, control_tag, xy_wh, color, tooltip);

        return tab_line;
    }

    private String build_CONTROLS_TABLE_Line(String control_name, String text, String control_tag, String xy_wh, int color, String tooltip)
    {
//PROFILE//Settings.MON(TAG_PROFILE, "build_CONTROLS_TABLE_Line:\n- control_name["+ control_name +"]\n- text=["+ text +"]");
        // pretty up control display name
        if( text.startsWith("LOF ") ) text = text.replace("LOF ","Log filter\n");

        String tab_name = "TAB.CONTROL_"+ control_name;
        String shape    = NotePane.Get_current_shape();

        if(section_boundary)
            tooltip = Settings.SECTION_BOUNDARY + tooltip;

        // add dynamic tag properties
        String tag
            =    control_name.equals("SIGNIN") ? control_name +" PASSWORD="+ Settings.SERVER_PASS
            :   (control_tag          != null) ? control_tag
            :    control_name
            ;

        String tab_value
            = "type="       + "CONTROL"
            +"|tag="        + tag
            +"|zoom="       + "1"
            +"|xy_wh="      + xy_wh
            +"|text="       + text
            +"|color="      + color
            +"|shape="      + shape
            +"|tt="         + tooltip
            ;

        return tab_name + "=" + tab_value;
    }
    //}}}
    // build_CONTROLS_TABLE_TABS_lines {{{
    private ArrayList<String> build_CONTROLS_TABLE_TABS_lines()
    {
        String caller = "build_CONTROLS_TABLE_TABS_lines";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);
        // GRID_MULTI_COL TABS LAYOUT FOR [CONTROL_BUILTINS] entries {{{
        name = TEMPLATE_CONTROLS;
//*PROFILE*/Settings.MON(TAG_PROFILE, caller, "...collecting "+CONTROL_BUILTINS.length+" CONTROL_BUILTINS entries");

        NpButtonGridLayout.ResetAutoPlace(NpButtonGridLayout.GRID_MULTI_COL, CONTROL_BUILTINS.length);

        ArrayList<String>    al = new ArrayList<>();
        String control_name, control_info, null_tag = null;

        profile_section_color =       0;
        name_charAt_0         = (char)0;
        section_boundary      =   false;
        section_cells_nb      =       0;
        for(int i=0; i < CONTROL_BUILTINS.length; i++)
        {
            // TAG [label] [info] {{{
            control_name     = CONTROL_BUILTINS[i][0];
            control_info     = CONTROL_BUILTINS[i][1];

            //}}}
            // SECTIONS MARKER .. (to skip one slot) {{{
            //char           c = Character.toLowerCase( control_name.charAt(0) );
            //section_boundary = (name_charAt_0 != (char)0) && (c != name_charAt_0);
            //name_charAt_0    =  c;
            // XXX too much segmentation for CONTROLS_TABLE

            //}}}
            // BUILD AND COLLECT TAG LINE {{{
            String  tab_line = build_CONTROLS_TABLE_Line(control_name, control_info, null_tag);
            tab_line         = tab_line.replace("CONTROL", "SHORTCUT"); // RTabsDesigner would not like it otherwise!
            al.add(    tab_line);
            // }}}
        }
        //}}}
        name = name.toLowerCase();
//*PROFILE*/Settings.MOM(TAG_PROFILE, "...return al[x"+al.size()+"] entries for TEMPLATE_CONTROLS ["+name+"]");
        return al;
    }
    //}}}
    //}}}
    //* FREETEXT_TABLE */ //{{{
    // build_FREETEXT_TABLE {{{
    private void build_FREETEXT_TABLE(String file_name)
    {
//*PROFILE*/Settings.MOC(TAG_PROFILE, "build_FREETEXT_TABLE");

        Settings.PROFILE = Settings.FREETEXT_TABLE;

        Settings.PRODATE = 0; //secondsNow; // !!! if this gets transmitted to the server, it would kill its current profile

        loadSettingsLine("# PROFILE=" + Settings.PROFILE);
        loadSettingsLine("# PRODATE=" + Settings.PRODATE + " " + DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())));

        load_BUILTIN_PALETTES();

        String file_path = get_profile_file_path( file_name );
        ArrayList<String> lines = get_file_lines( file_path );
        if(lines.size() < 1) return;

        NpButtonGridLayout.ResetAutoPlace(NpButtonGridLayout.GRID_MULTI_COL, CONTROL_BUILTINS.length);

        profile_section_color =       0;
        name_charAt_0         = (char)0;
        section_boundary      =   false;
        section_cells_nb      =       0;
        for(int i=0; i < lines.size(); ++i)
        {
            String tab_name = "tab_"+i;
            String tab_line = build_FREETEXT_TABLE_Line(tab_name, lines.get(i));
            loadSettingsLine(tab_line);
        }

//*PROFILE*/Settings.MOM(TAG_PROFILE, "build_FREETEXT_TABLE ...done");
    }
    //}}}
    // build_FREETEXT_TABLE_Line {{{
    private String build_FREETEXT_TABLE_Line(String tab_name, String text_line)
    {
//*PROFILE*/Settings.MOM(TAG_PROFILE, "build_FREETEXT_TABLE_Line:\n- tab_name["+ tab_name +"]\n- text_line=["+ text_line +"]");

        String  text = Settings.ellipsis(text_line,24);
        String label = text;

        try {
        // [RTabsCookies.txt] .. f(cookie_label mark= note=)
        //{{{
        //{{{
/*
:new C:/LOCAL/STORE/DEV/PROJECTS/RTabs/Util/RTabs_Profiles/freetext_rtabscookies.txt
*/
        //# storage_9016_4EF8_Android_data_ivanwfr_rtabs_files_Profiles_DEV_Clamp_mark10=mark=62.4026 note=INERTIA
        // url_keywords_markX=mark=62.4026 note=INERTIA
        // url_keywords_markX=mark= note=bounce+again%5Cn...in+that+direction%5Cnonly+after+a+bounce+in+the+other
        // url_keywords_markX=mark=38.6039 note=CATCH
        // url_keywords_markX=mark= note=X+X+X
        // url_keywords_markX=mark=0.0000 note=Clamp.java%5Cn...%5Cn...
        // url_keywords_markX=mark=50.7646 note=CLAMP
        // url_keywords_markX=mark=43.6009 note=TRACK
        // url_keywords_markX=mark=64.8480 note=google
        // url_keywords_markX=mark=66.8828 note=_move_inertia_loop
        // url_keywords_markX=mark=17.7378 note=onTouchEvent
        // url_keywords_markX=mark=75.7401 note=MOVE+LOOP
        // url_keywords_markX=mark=84.2980 note=_move_moving_np
        // url_keywords_markX=mark=90.8659 note=BOUNCE

        label        = Settings.EMPTY_STRING;
        String  mark = Settings.EMPTY_STRING;
        String  note = Settings.EMPTY_STRING;
        int i_s, i_e;

        text_line   += " ";
        //}}}
        // [note] {{{
        i_s = text_line.indexOf("note="); i_e = text_line.indexOf(" ", i_s); if((i_s > 0) && (i_e > i_s)) note = text_line.substring(i_s+5, i_e).trim();
        if(note != Settings.EMPTY_STRING)
        {
//*PROFILE*/Settings.MOM(TAG_PROFILE, ".....note=[" +note      +"]");
            try {  note = URLDecoder.decode(note, "UTF-8"); } catch(Exception ignored) {}
        }

        //}}}
        // [mark] {{{
        i_s = text_line.indexOf("mark="); i_e = text_line.indexOf(" ", i_s); if((i_s > 0) && (i_e > i_s)) mark = text_line.substring(i_s+5, i_e).trim();

        //}}}
        // [label] {{{

         //# storage_9016_4EF8_Android_data_ivanwfr_rtabs_files_Profiles_DEV_Clamp_mark10=mark=62.4026 note=INERTIA
         // ......................................................................^^^^^

         //# mindprod_jgloss_jcheat_mark1=mark=11.1849 note=Loops
         // .......................^^^^^

         i_e = text_line.indexOf("_mark");
         if(i_e > 0)
         {
             label = text_line.substring(0, i_e).replace("# ",""); // # (...)_mark

             if( label.contains("rtabs_files") ) {
                 for(i_s = (i_e-1); i_s > 0; --i_s)
                     if(text_line.charAt(i_s) == '_')
                         break;
                 label
                     = text_line.substring(i_s, i_e)
                     . replace("_"," ")
                     . trim();

             }
             if( label.contains("google_search") ) label = "google_search";
         }

        //}}}
         // [text] {{{
         if((label != Settings.EMPTY_STRING) || (note != Settings.EMPTY_STRING) || (mark != Settings.EMPTY_STRING))
         {
             text = String.format("%s\\n%s\\n%s", label, note, mark)
                 .replace("[]\\n","")
                 .replace("[]"  ,"")
                 ;
//*PROFILE*/Settings.MOM(TAG_PROFILE, "text_line=["+ text_line +"]");
//*PROFILE*/Settings.MOM(TAG_PROFILE, "....label=["+ label     +"]");
//*PROFILE*/Settings.MOM(TAG_PROFILE, ".....mark=["+ mark      +"]");
//*PROFILE*/Settings.MOM(TAG_PROFILE, ".....note=["+ note      +"]");
//*PROFILE*/Settings.MOM(TAG_PROFILE, ".....text=["+ text      +"]");
         }
         //}}}
        //}}}
        }
        catch(Exception ignored) { }

        // SECTIONS MARKER .. (to skip one slot) {{{
        if(label != Settings.EMPTY_STRING)
        {
            char           c = Character.toLowerCase( label.charAt(0) );
            section_boundary = (name_charAt_0 != (char)0) && (c != name_charAt_0);
            name_charAt_0    =  c;
        }
         //}}}

        // SKIP SECTION BOUNDARY SLOT {{{
        String xy_wh;
        if(section_boundary && (section_cells_nb > 1))
        {
            section_cells_nb       = 1;
            xy_wh                  = NpButtonGridLayout.Get_free_xy_wh(1); // skip one slot
            profile_section_color += 1 ; // next dynamic color
        }
        else ++section_cells_nb;
        //}}}
        xy_wh           = NpButtonGridLayout.Get_free_xy_wh();

        int       color = profile_section_color;
        String      tag = text_line;

        String tab_line = build_FREETEXT_TABLE_Line(tab_name, xy_wh, tag, text, color);

//*PROFILE*/Settings.MOM(TAG_PROFILE, "@@@ "+tab_line);
        return tab_line;
    }

    private String build_FREETEXT_TABLE_Line(String tab_name, String xy_wh, String tag, String text, int color)
    {
//*PROFILE*/Settings.MOM(TAG_PROFILE, "build_FREETEXT_TABLE_Line:\n- tab_name["+ tab_name +"]\n- text=["+ text +"]");

        String tab_value
            = "type="       + "SHORTCUT"
            +"|tag="        + tag
            +"|zoom="       + "1"
            +"|xy_wh="      + xy_wh
            +"|text="       + text
            +"|color="      + color
            +"|shape="      + NotePane.Get_current_shape()
            +"|tt="
            ;

        return "TAB."+ tab_name +"="+ tab_value;
    }
    //}}}
    // build_FREETEXT_TABLE_TABS_lines {{{
    private ArrayList<String> build_FREETEXT_TABLE_TABS_lines(String profile_base_name)
    {
        // GRID_MULTI_COL TABS LAYOUT FOR [FREETEXT_TABLE] entries {{{
        String caller = "build_FREETEXT_TABLE_TABS_lines("+profile_base_name+")";
        int idx = profile_base_name.indexOf(" ");
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);

        //}}}
        // [file_name] {{{
        String file_name = profile_base_name.substring(idx+1);
//*PROFILE*/Settings.MOM(TAG_PROFILE, "file_name=["+ file_name +"]");

        //}}}
        // [file_path] {{{
        String file_path = get_profile_file_path( file_name );
//*PROFILE*/Settings.MOM(TAG_PROFILE, "file_path=["+ file_path +"]");

        //}}}
        // [lines] {{{
        ArrayList<String> lines = get_file_lines( file_path );
        if(lines.size() < 1) return null;

        //}}}
        // TABS {{{
//*PROFILE*/Settings.MOM(TAG_PROFILE, "...collecting "+lines.size()+" lines");

        NpButtonGridLayout.ResetAutoPlace(NpButtonGridLayout.GRID_MULTI_COL, lines.size());

        ArrayList<String>    al = new ArrayList<>();

        profile_section_color =       0;
        name_charAt_0         = (char)0;
        section_boundary      =   false;
        section_cells_nb      =       0;
        for(int i=0; i < lines.size(); ++i)
        {
            String tab_name = "tab_"+i;
            String tab_line = build_FREETEXT_TABLE_Line(tab_name, lines.get(i));
            al.add(    tab_line);
        }
        //}}}
//*PROFILE*/Settings.MOM(TAG_PROFILE, "...return al[x"+al.size()+"] entries for TEMPLATE_FREETEXT ["+file_name+"]");
        return al;
    }
    //}}}
    //}}}
    //* SOUNDS_TABLE */ //{{{
    // build_SOUNDS_TABLE {{{
    private void build_SOUNDS_TABLE()
    {
        String caller = "build_SOUNDS_TABLE";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);
        // KEY_VAL [PROFILE] [PRODATE] {{{

        Settings.PROFILE = Settings.SOUNDS_TABLE;
        Settings.PRODATE = 0;
        loadSettingsLine("# PROFILE=" + Settings.PROFILE);
        loadSettingsLine("# PRODATE=" + Settings.PRODATE + " " + DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())));

        //}}}
        // PALETTES {{{
        load_BUILTIN_PALETTES();

        // }}}
        // GRID_MULTI_COL TABS LAYOUT FOR [sounds_names] entries {{{
        ArrayList<String> sounds_names = get_sounds_names();
        NpButtonGridLayout.ResetAutoPlace(NpButtonGridLayout.GRID_MULTI_COL, sounds_names.size());

        //}}}
        // CHECK CURRENT ARCHIVE (for sound files deployment) {{{
        if(ProfileArchiveList == null)
            check_ProfileArchiveList();

        //}}}
        // name format .. f(# sounds count) {{{
        String format = (sounds_names.size() > 99)
            ? "_%3d. %s"
            : "_%2d. %s";
        int count   = 0;
        //}}}
        // sound_names TABS  {{{
        String sound_name, text;

        profile_section_color =       0;
        name_charAt_0         = (char)0;
        section_boundary      =   false;
        section_cells_nb      =       0;
        for(int i= 0; i < sounds_names.size(); ++i)
        {
            sound_name = sounds_names.get(i);
//*PROFILE*/Settings.MOM(TAG_PROFILE, String.format("%3d sound_name=[%s]", i, sound_name));

            text = sound_name;
            text = text.replace("/", " "+ Settings.SYMBOL_BLACK_CIRCLE +" ");

            // SOUND SECTIONS MARKER
            char           c = Character.toLowerCase( sound_name.charAt(0) );
            section_boundary = (name_charAt_0 != (char)0) && (c != name_charAt_0);
            name_charAt_0    =  c;

            // BUILD AND LOAD SOUND LINE
            String     shape = sound_name.equals( Settings.SOUND_CLICK )
                ?              NotePane.SHAPE_TAG_SQUARE
                :              NotePane.SHAPE_TAG_PADD_R;
            String          tab_line = build_SOUNDS_TABLE_Line_WITH_SHAPE(sound_name, String.format(format, ++count, text), shape);
//*PROFILE*/Settings.MOM(TAG_PROFILE, "@@@ "+tab_line);
            loadSettingsLine( tab_line );
        }
        //}}}
//*PROFILE*/Settings.MON(TAG_PROFILE, caller, "...done");
    }
    //}}}
    // build_SOUNDS_TABLE_Line {{{
    private String build_SOUNDS_TABLE_Line_WITH_SHAPE(String sound_name, String text, String shape)
    {
        // SKIP SECTION BOUNDARY SLOT {{{
        String xy_wh;
        if(section_boundary && (section_cells_nb > 1))
        {
            section_cells_nb       = 1;
            xy_wh                  = NpButtonGridLayout.Get_free_xy_wh(1); // skip one slot
            profile_section_color += 1 ; // next dynamic color
        }
        else ++section_cells_nb;
        //}}}
        xy_wh           = NpButtonGridLayout.Get_free_xy_wh();

        int       color = profile_section_color;

        String  tooltip = "Load this profile";
        if(section_boundary)
            tooltip     = Settings.SECTION_BOUNDARY + tooltip;

        String tab_name = "TAB.PROFILE_"+ sound_name;

        String tag      = "PROPERTY SOUND_CLICK "+ sound_name;
    //  String tag      = "PROPERTY SOUND_DING " + sound_name; // TODO support both

        String tab_value
            = "type="   + "SHORTCUT"
            +"|tag="    + tag
            +"|zoom="   + "1"
            +"|xy_wh="  + xy_wh
            +"|text="   + text
            +"|color="  + color
            +"|shape="  + shape
            +"|tt="     + tooltip
            ;

        String tab_line = tab_name + "=" + tab_value;
        return tab_line;
    }
    //}}}
    // get_sounds_names TODO replace with [SOUNDS_FOLDER] files
    //{{{
    public ArrayList<String> get_sounds_names()
    {
        String caller = "get_sounds_names";
//*PROFILE*/Settings.MOC(TAG_PROFILE, caller);

        ArrayList<String> al = new ArrayList<>();

        al.add( Settings.SOUND_KEYB_Delete    );
        al.add( Settings.SOUND_KEYB_Keypress  );
        al.add( Settings.SOUND_KEYB_Return    );
        al.add( Settings.SOUND_KEYB_Spacebar  );
        al.add( Settings.SOUND_KEYB_step      );
        al.add( Settings.SOUND_BELL_airplane  );
        al.add( Settings.SOUND_BELL_bronze    );
        al.add( Settings.SOUND_BELL_desk      );
        al.add( Settings.SOUND_BELL_ding      );
        al.add( Settings.SOUND_BELL_dong      );
        al.add( Settings.SOUND_BELL_door      );
        al.add( Settings.SOUND_BELL_goblet    );
        al.add( Settings.SOUND_BELL_reception );
        al.add( Settings.SOUND_BELL_ship      );
        al.add( Settings.SOUND_BELL_triangle  );

//*PROFILE*/Settings.MON(TAG_PROFILE, caller, "...return "+al.size()+" names");
        return  al;
    }
    //}}}
    //}}}
    //* TODO [FONTS_TABLE] ... with user-specified [FONT_FOLDERS] */ //{{{
    //}}}
    //* PROFILES STORE */ //{{{
    // ListProfiles {{{
    public static void ListProfiles()
    {
        log("ListProfiles {{{\n");

        // LOCAL PROFILES {{{
        _build_Profiles_Dict();
        if(Profiles_Dict.size() > 0)
        {
            log("List of STORED PROFILES in ["+ Settings.Get_Profiles_dir() +"]:");

            int count = 0;
            for(Map.Entry<String,String> entry : Profiles_Dict.entrySet())
            {
                String profile_name = entry.getKey();
                String file_path    = entry.getValue();
                int bytes           = (int)new File(file_path).length();
                int idx             = file_path.lastIndexOf("/");
                String base_name
                    = (idx >= 0)
                    ?  file_path.substring(idx+1)
                    :  file_path;

                // file
                ++count;
                log( String.format("\n%2d %s %-32s (%d b) [%s]"
                            ,         count
                            ,             Settings.SYMBOL_DISK
                            ,                profile_name
                            ,                       bytes
                            ,                              base_name
                            ));

                // key_val header
                log( LoadHeaderFromFile(file_path) );

                // storage cleanup
                if( profile_name.equals("") )
                    delete_file( file_path );
            }
        }
        else {
            log("*** Profile.ListProfiles: LOCAL PROFILES STORAGE IS CURRENTLY EMPTY ["+ Settings.Get_Profiles_dir() +"]:");
        }

        //}}}

        // ARCHIVED PROFILES {{{
        log("\nList of ARCHIVED PROFILES:");
        if(ProfileArchiveList == null)
            check_ProfileArchiveList();

        String[] profile_names = new String[ProfileArchiveList.size()];
        for(int i=0; i < ProfileArchiveList.size(); ++i)
            profile_names[i] = ProfileArchiveList.get(i);
        Arrays.sort( profile_names );

        for(int i=0; i < profile_names.length; ++i)
        {
            log( String.format("%2d %s %s"
                        ,       (i+1)
                        ,           Settings.SYMBOL_FOLDER
                        ,              profile_names[i]
                        )
               );
        }

        //}}}

        log("\nListProfiles }}}");
    }
    //}}}
    // DeleteAllProfiles {{{
    public static void DeleteAllProfiles()
    {
//*PROFILE*/Settings.MOC(TAG_PROFILE, "DeleteAllProfiles");

        _build_Profiles_Dict();

        for(Map.Entry<String,String> entry : Profiles_Dict.entrySet())
        {
            String profile_name = entry.getKey();
            String file_path    = entry.getValue();

//*PROFILE*/Settings.MOM(TAG_PROFILE, String.format("%24s = %s", profile_name, file_path) );

            delete_file( file_path );
        }

        Clear_Profiles_Dict("DeleteAllProfiles");
    }
    //}}}
    // DeleteProfile {{{
    public static String DeleteProfile(String name)
    {
//*STORAGE*/Settings.MOC(TAG_STORAGE, "DeleteProfile("+ name +")");

        _build_Profiles_Dict();

        // file
        String file_path    = Profiles_Dict.get( name );
//*STORAGE*/Settings.MOM(TAG_STORAGE, String.format("name=[%s] file_path=[%s]", name, file_path) );
        if(file_path == null)
            return "*** FILE NOT FOUND IN LOCAL STORAGE";

        // delete file
        if( delete_file( file_path ) )
            Profiles_Dict.remove( name );

        // force folder rescan
        Clear_Profiles_Dict("DeleteProfile("+ name +")");

        return "--- FILE DELETED FROM LOCAL STORAGE";
    }
    //}}}
    // Profiles_Dict {{{
    // _build_Profiles_Dict .. [profile_mame] [file_path] {{{
    private static final HashMap<String,String> Profiles_Dict      = new HashMap<>();
    private static void _build_Profiles_Dict()
    {
        // ARCHIVE DICTIONARY .. [<profile_mame> <file_path>]
        if(Profiles_Dict.size() > 0)
            return;

        if(ProfileArchiveList == null)
            check_ProfileArchiveList();

//*STORAGE*/Settings.MOC(TAG_STORAGE, "_build_Profiles_Dict");

        File profiles_dir = Settings.Get_Profiles_dir();
//*STORAGE*/Settings.MOM(TAG_STORAGE, "...profiles_dir=[" + profiles_dir +"]");

        if( profiles_dir.exists() )
        {
            List<File> fList = Settings.dir_list(profiles_dir,  -1, "\\.txt"); // (all subdirs scan)

            File[] files =  fList.toArray(new File[fList.size()]);
            Arrays.sort( files );
//*STORAGE*/Settings.MOM(TAG_STORAGE, "@@@ _build_Profiles_Dict ("+ files.length +" files):");

            int dl = profiles_dir.getPath().length();
            for(int i=0; i < files.length; i++)
            {
                String fn = files[i].getPath().substring(dl+1);

                // IGNORE KNOWN IRRELEVANT FILES
                if(  fn.startsWith( Settings.SETTINGS_FILENAME     )) continue;
                if(  fn.startsWith( Settings.PERSONAL_NOTE_FILENAME)) continue;
                if( !fn.endsWith  ( ".txt"                         )) continue;

                String fp = files[i].getPath();
                String pn = fn.substring(0, fn.length()-4); // strip ".txt" extension

//*STORAGE*/    String ss = ProfileArchiveList.contains( fn ) ? ".." : "**";    //TAG_STORAGE
//*STORAGE*/Settings.MOM(TAG_STORAGE, String.format("@@@ %s %3d %32s %s %-24s", Settings.SYMBOL_DISK, (i+1), "["+pn+"]", ss, "["+fn+"]"));
                try {
                    Profiles_Dict.put(pn, fp);
                } catch(Exception ignored) { }
            }

        }
//*STORAGE*/Settings.MOM(TAG_STORAGE, "_build_Profiles_Dict ...done");
    }
    //}}}
    // Clear_Profiles_Dict {{{
    public static void Clear_Profiles_Dict(String caller)
    {
        caller += "] [Clear_Profiles_Dict";
//*STORAGE*/Settings.MOC(TAG_STORAGE, caller);
        Profiles_Dict.clear();
        ProfileArchiveList = null;  // forced zip archive rescan
    }
    //}}}
    // GetProfileLines {{{
    public static ArrayList<String> GetProfileLines(String profile_name)
    {
//*STORAGE*/Settings.MOC(TAG_STORAGE, "GetProfileLines("+profile_name+")");

        //  StringBuffer sb = new StringBuffer();
        ArrayList<String>    al = new ArrayList<>();
        InputStream is   = null;
        try {
            File file = GetProfileFile( profile_name );
            if((file == null) || !file.exists())
            {
                log("*** Profile.GetProfileLines("+ profile_name +") ...file not found");
                return null;
            }

            is                      = new BufferedInputStream(new FileInputStream(file));
            InputStreamReader   isr = new InputStreamReader  ( is  );
            BufferedReader      br  = new BufferedReader     ( isr );
        //  StringBuilder sb  = new StringBuilder();
            String               s  = null;
            do {
                s = br.readLine();
            //  if(s != null) sb.append(s+"\n");
                if(s != null) al.add(s);
            }
            while(s != null);
        }
        catch(Exception ex) {
            log("*** Profile.GetProfileLines("+profile_name+") Exception:\n"+ ex.getMessage());
        }
        finally {
            try { if(is != null) is.close(); } catch(Exception ignored) {}
        }
    //  return sb.toString();
        return al;
    }
    //}}}
    // GetProfileFile {{{
    public static File GetProfileFile(String profile_name)
    {
        String caller = "GetProfileFile("+profile_name+")";
//*STORAGE*/Settings.MOC(TAG_STORAGE, caller);

        File profiles_dir = Settings.Get_Profiles_dir();
        if( !profiles_dir.exists() )
        {
            log("*** Profile.GetProfileFile("+ profile_name +") ...cannot access profile folder");
            return null;
        }

//*STORAGE*/Settings.MOM(TAG_STORAGE, "profiles_dir=["+ profiles_dir.getPath() +"]");
//*STORAGE*/Settings.MOM(TAG_STORAGE, "profile_name=["+ profile_name +"]");

        // i.e. DOC/EN/FR
        int idx_base = profile_name.lastIndexOf("/");
//*STORAGE*/Settings.MOM(TAG_STORAGE, "idx_base=["+ idx_base +"]");

        // HEAD: /data/data/ivanwfr.rtabs/app_Profiles/DOC/EN
        String profile_head
            = profiles_dir.getPath() +"/"
            + ((idx_base > 0)
                    ? profile_name.substring(0, idx_base  )    // start, end
                    : "")
            ;

        // TAIL: FR.txt
        String profile_tail
            = ((idx_base > 0)
                    ? profile_name.substring(idx_base+1) // start
                    : profile_name)
            + ".txt";

//*STORAGE*/Settings.MOM(TAG_STORAGE, "profile_head=["+ profile_head +"]");
//*STORAGE*/Settings.MOM(TAG_STORAGE, "profile_tail=["+ profile_tail +"]");

        ProfileArchive.check_dir(profile_head, "");
        File file     = new File(profile_head, profile_tail);

//*STORAGE*/Settings.MOM(TAG_STORAGE, caller+" ...return file=["+file+"]");
        return file;
    }
    //}}}
    // Get_Profiles_Dict_size {{{
    public static int Get_Profiles_Dict_size()
    {
        _build_Profiles_Dict();
        int size = Profiles_Dict.size();
//*STORAGE*/Settings.MON(TAG_STORAGE, "Get_Profiles_Dict_size", "...return "+size);
        return size;
    }
    //}}}
    // }}}
    //}}}
    //* PROFILES ARCHIVE */ //{{{
    //{{{
    private static ArrayList<String>      ProfileArchiveList = null;
    //}}}
    // check_ProfileArchiveList {{{
    private static void check_ProfileArchiveList()
    {
        String caller = "check_ProfileArchiveList";
//*STORAGE*/Settings.MOC(TAG_STORAGE, caller);
        // check Profiles folder {{{
        boolean can_proceed = true;

        File profiles_dir = null;
        profiles_dir      = Settings.Get_Profiles_dir();
        if( !profiles_dir.exists() )
            can_proceed   = false;

        //}}}
        // access zip_file_path {{{
        String zip_file_path = null;
        if(can_proceed)
        {
            zip_file_path = get_zip_file_path();
            if((zip_file_path == null) || !new File( zip_file_path ).exists())
            {
                log("*** Profile.check_ProfileArchiveList: zip_file_path=["+zip_file_path+"]:\n*** no current archive to check");
                can_proceed = false;
            }
        }
        //}}}
        // check zip list {{{
        if(can_proceed)
        {
            File file = new File( zip_file_path );
            if( file.exists() ) {
                ProfileArchiveList = ProfileArchive.get_list( zip_file_path );
            }
            else {
//*STORAGE*/Settings.MON(TAG_STORAGE, "check_ProfileArchiveList", "archive not yet created [" + zip_file_path +"]");
                ProfileArchiveList = null;
            }
        }
        //}}}
        // fallback to an empty list // {{{
        if(ProfileArchiveList == null)
            ProfileArchiveList = new ArrayList<>();
        // }}}
    }
    //}}}
    //}}}
    //}}}
    /** UTIL */
    // delete_file {{{
    private static boolean delete_file(String file_path)
    {
        boolean diag = false;
        try {
            File file = new File( file_path );
            if( file.exists() )
                diag = file.delete();
            else
                log("*** Profile.delete_file("+ file_path +"): file not found");
        }
        catch(Exception ex) {
            log("*** Profile.delete_file("+file_path+") Exception:\n"+ ex);
        }

//*STORAGE*/Settings.MON(TAG_STORAGE, "Profile.delete_file("+ file_path +")", "...return "+diag);
        return diag;
    }
    //}}}
    // get_file_lines {{{
    public static ArrayList<String> get_file_lines(String file_path)
    {
//*STORAGE*/Settings.MOC(TAG_STORAGE, "get_file_lines("+file_path+")");
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
            log("*** Profile.get_file_lines("+file_path+") Exception:\n"+ ex.getMessage());
        }
        finally {
            try { if(is != null) is.close(); } catch(Exception ignored) {}
        }
        return al;
    }
    //}}}

    /** LOG */
    // log {{{
    private static void log(String msg)
    {
        MLog.log_profile(msg);
    }
    //}}}
    // log_buffers {{{
    public void log_buffers(StringBuilder sb)
    {
        String s= Settings.SYMBOL_KEY_VAL;
        sb.append(s +"sb_KEY_VAL {{{\n"
                + s + sb_KEY_VAL.toString().replace("\n", "\n"+s)
                +    "sb_KEY_VAL }}}\n");

        s       = Settings.SYMBOL_PALETTE;
        sb.append(s +"sb_PALETTES {{{\n"
                + s + sb_PALETTES.toString().replace("\n", "\n"+s)
                +    "sb_PALETTES }}}\n");

        s       = Settings.SYMBOL_TABS;
        sb.append(s + "sb_TABS {{{\n"
                + s + sb_TABS.toString().replace("\n", "\n" + s)
                +     "sb_TABS }}}\n");
    }
    //}}}
    // toString {{{
    public String toString()
    {
        if(name == "")
            return "A NEW PROFILE INSTANCE";

        else if(   (sb_KEY_VAL .length()==0)
                && (sb_PALETTES.length()==0)
                && (sb_TABS    .length()==0)
               )
            return "AN EMPTY PROFILE INSTANCE";

        else
            return "Profile("+name+")"
                +"=["+ sb_KEY_VAL .toString().split("\n").length + " KEY_VAL]"
                +"+["+ sb_PALETTES.toString().split("\n").length +" PALETTES]"
                +"+["+ sb_TABS    .toString().split("\n").length +    " TABS]"
                ;

    }
    //}}}

    /** ARCHIVE */
    //{{{
    // get_ProfileArchiveList_size {{{
    public static int get_ProfileArchiveList_size()
    {
        if(ProfileArchiveList == null)
            check_ProfileArchiveList();
        int size = ProfileArchiveList.size();
//*STORAGE*/Settings.MON(TAG_STORAGE, "get_ProfileArchiveList_size", "...return "+size);
        return size;
    }
    //}}}
    // get_zip_file_path {{{
    public static String get_zip_file_path()
    {
        File profiles_dir = Settings.Get_Profiles_dir();
        String zip_file_path
            = (profiles_dir.exists() )
            ?  profiles_dir.getPath() +"/"+ Settings.PROFILES_ZIP_FILE_NAME
            :  null;
//*STORAGE*/Settings.MON(TAG_STORAGE, "get_zip_file_path", "...return ["+zip_file_path+"]");
        return zip_file_path;
    }
    //}}}
    // get_profile_file_path {{{
    public static String get_profile_file_path(String profile_name)
    {
        String profile_base_name
            = Settings.get_base_name( profile_name )
            . replace(" ", "_")
            ;

        File profiles_dir = Settings.Get_Profiles_dir();
        String profile_file_path
            = (profiles_dir.exists() )
            ?  profiles_dir.getPath() +"/"+ profile_base_name + ".txt"
            :  null;
//*STORAGE*/Settings.MON(TAG_STORAGE, "get_profile_file_path("+profile_name+")", "...return ["+profile_file_path+"]");
        return profile_file_path;
    }
    //}}}
    // zip {{{
    public static void zip()
    {
//*STORAGE*/Settings.MOC(TAG_STORAGE, "zip");

        // check Profiles folder {{{
        boolean can_proceed = true;

        File profiles_dir = Settings.Get_Profiles_dir();
        if( !profiles_dir.exists() )
            can_proceed   = false;

        //}}}
        // check Profiles {{{
        if(can_proceed)
        {
            _build_Profiles_Dict();
            if(Profiles_Dict.size() < 1) {
                log("*** Profile.zip: LOCAL PROFILES STORAGE IS CURRENTLY EMPTY ["+ Settings.Get_Profiles_dir() +"]:");
                can_proceed = false;
            }
        }
        //}}}
        // check zip_file_path {{{
        String zip_file_path = null;
        if(can_proceed)
        {
            zip_file_path = get_zip_file_path();
            if(zip_file_path == null)
                can_proceed = false;
        }
        // archive profile files {{{
        //}}}
        if( can_proceed )
        {
//*STORAGE*/ Settings.MOM(TAG_STORAGE, "Archiving "+ Profiles_Dict.size() +" Profiles stored in ["+ Settings.Get_Profiles_dir() +"]:");
            ProfileArchive.zip_files_HashMap(Profiles_Dict, zip_file_path);
            Clear_Profiles_Dict("zip");
        }
        //}}}
//*STORAGE*/Settings.MOM(TAG_STORAGE, "zip ...done");
    }
    //}}}
    // unzip {{{
    public static void unzip()
    {
//*STORAGE*/Settings.MOC(TAG_STORAGE, "unzip");

        // check Profiles profiles_dir {{{
        boolean can_proceed = true;

        File profiles_dir = null;
        profiles_dir      = Settings.Get_Profiles_dir();
        if( !profiles_dir.exists() )
            can_proceed   = false;

        //}}}
        // access zip_file_path {{{
        String zip_file_path = null;
        if(can_proceed)
        {
            zip_file_path = get_zip_file_path();
            if( !new File( zip_file_path ).exists() )
            {
                log("*** Profile.unzip: zip_file_path=["+zip_file_path+"]:\n*** no current archive to unzip");
                return;
            }
        }
        //}}}
        // restore files from zip archive {{{
        if( can_proceed )
        {
            log("@@@ Extracting Profiles from "+ zip_file_path +" into ["+ profiles_dir +"]:");

            ProfileArchive.unzip_from_file_to_dir(zip_file_path, profiles_dir.getPath());
            Clear_Profiles_Dict("unzip"); // forced rescan
        }
        //}}}
//*STORAGE*/Settings.MOM(TAG_STORAGE, "unzip ...done");
    }
    //}}}
    // unzip_factory {{{
    public static void unzip_factory()
    {
//*STORAGE*/Settings.MOC(TAG_STORAGE, "unzip_factory");
        // check Profiles profiles_dir {{{
        boolean can_proceed = true;

        File profiles_dir = null;

        profiles_dir      = Settings.Get_Profiles_dir();
//*STORAGE*/Settings.MOM(TAG_STORAGE, "profiles_dir=["+profiles_dir+"]");
        if( !profiles_dir.exists() )
            can_proceed   = false;

        //}}}
        // restore files from resources {{{
        if( can_proceed )
        {
/*
            int samples_res_id
                = BuildConfig.FLAVOR.equals("IRun") // RESERVED
                ?  R.raw.devfiles_zip
                :  R.raw.profiles_zip;
*/
            int samples_res_id = R.raw.profiles_zip;

            InputStream ins = RTabs.activity.getResources().openRawResource( samples_res_id );

            ProfileArchive.unzip_from_stream_to_dir(ins, profiles_dir.getPath());

            Clear_Profiles_Dict("unzip_factory"); // forced rescan
        }
        //}}}
//*STORAGE*/Settings.MOM(TAG_STORAGE, "unzip_factory ...done");
    }
    //}}}
    // zip_del {{{
    public static void zip_del()
    {
//*STORAGE*/Settings.MOC(TAG_STORAGE, "zip_del");
        // access zip_file_path {{{
        String zip_file_path = null;
        zip_file_path = get_zip_file_path();
        if((zip_file_path == null) || !new File( zip_file_path ).exists())
        {
            log("*** Profile.zip_del: zip_file_path=["+zip_file_path+"]:\n*** no current archive to delete");
            return;
        }
        //}}}
        // DELETE DEVICE ZIP FILE // {{{
        try {
            if( delete_file( zip_file_path ) )
                log("@@@ ARCHIVE ["+ zip_file_path +"] DELETED FROM LOCAL STORAGE");

            ProfileArchiveList = null;
        }
        catch(Exception ex) {
            log("*** Profile.zip_del: zip_file_path=["+zip_file_path+"] Exception:\n"+ ex);
        }
        // }}}
    }
    //}}}
    //* [CLASS] ProfileArchive */
    private static class ProfileArchive // {{{
    {
        private static final int BUFFER = 2048;
        public ProfileArchive() { }
        // zip_files_HashMap .. (archive current profiles) {{{
        public static void zip_files_HashMap(HashMap<String,String> files_HashMap, String zip_file_path)
        {
            String file_path = "";
            try {
                // OUT (INIT) {{{
                FileOutputStream fos = new FileOutputStream( zip_file_path );
                ZipOutputStream  zos = new ZipOutputStream(new BufferedOutputStream( fos ));

                //}}}

                int idx = Settings.Get_Profiles_dir().getPath().length();

                // OUT FILES (COMPRESS) {{{
                for(Map.Entry<String,String> entry : files_HashMap.entrySet())
                {
                    file_path    = entry.getValue();

                    String tail_name = (idx >= 0) ?  file_path.substring(idx+1) : file_path;
//*STORAGE*/Settings.MOM(TAG_STORAGE, "STORING ["+ tail_name +"] from ["+ file_path +"]");

                    // IN (OPEN)
                    FileInputStream     fis = new FileInputStream    ( file_path  );
                    BufferedInputStream bis = new BufferedInputStream( fis, BUFFER);

                    // OUT (OPEN)
                    ZipEntry       zipEntry = new ZipEntry           ( tail_name  );
                    zos.putNextEntry( zipEntry );

                    // BUFFER
                    byte bytes[] = new byte[BUFFER];
                    int  file_size = 0;
                    int  count;
                    while((count = bis.read(bytes, 0, BUFFER)) != -1) {
                        zos.write(bytes, 0, count);
                        file_size += count;
                    }

                    // IN (CLOSE)
                    bis.close();

                    log("@@@ "+ Settings.SYMBOL_WHITE_CHECK_MARK +" PROFILE_ZIP ["+ tail_name +"] ("+ file_size +"b)");
                }
                //}}}
                // OUT (CLOSE)
                zos.close();
            }
            catch(Exception ex) {
                Settings.log_ex(ex, "ProfileArchive.zip: file_path=["+ file_path +"]");
            }

        }
        //}}}
        // unzip_from_file_to_dir {{{
        public static void unzip_from_file_to_dir(String zip_file_path, String out_dir_path)
        {
            try {
                unzip_from_stream_to_dir( new FileInputStream( zip_file_path ), out_dir_path);
            }
            catch(Exception ex) {
                Settings.log_ex(ex, "ProfileArchive.unzip_from_stream_to_dir("+ zip_file_path +", "+ out_dir_path +")");
            }
        }
        //}}}
        // unzip_from_stream_to_dir {{{
        public static void unzip_from_stream_to_dir(InputStream is, String out_dir_path)
        {
//*STORAGE*/Settings.MOM(TAG_STORAGE, ".....check_dir("+ out_dir_path +")");
            check_dir(out_dir_path, "");

            ZipInputStream zis = null;
            String entry_name  = null;
            try {
//*STORAGE*/Settings.MOM(TAG_STORAGE, "......ZIP......: [ lastModified in Profiles_dir........]");
                zis = new ZipInputStream( is );
                ZipEntry ze = null;
                while((ze = zis.getNextEntry()) != null)
                {
                    // ZIP ENTRY {{{
                    entry_name = ze.getName();
//STORAGE//Settings.MOM(TAG_STORAGE, "...entry_name=["+ entry_name +"]");
                    //if( !entry_name.endsWith(".txt") ) continue;

                    // }}}
                    // DIR {{{
                    //if( ze.isDirectory() ) {
//STORAGE//Settings.MOM(TAG_STORAGE, ".....check_dir("+ entry_name +")");
                        check_dir(out_dir_path, entry_name);
                    //}
                    // }}}
                    // FILE {{{
                    //else {
                        // SKIP OLDER ZIP ENTRIES {{{
                        long   zip_entry_last_modified = ze  .getTime();
                        String               file_path = out_dir_path +"/"+ entry_name;
                        File                      file = new File(file_path);
                        if( file.exists() ) // filter removed: if( file_path.endsWith(".txt") )
                        {
                            // LAST MODIFIED {{{
                            long   file_path_last_modified = file.lastModified();

//*STORAGE*/String file_age   = Settings.Get_time_elapsed(file_path_last_modified);
//*STORAGE*/String time_diff  = Settings.Get_time_elapsed(zip_entry_last_modified, file_path_last_modified);
//STORAGE//Settings.MOM(TAG_STORAGE, ".......... zip: "+ DateFormat.getDateTimeInstance().format(new Date( zip_entry_last_modified )));
//STORAGE//Settings.MOM(TAG_STORAGE, "..........file: "+ DateFormat.getDateTimeInstance().format(new Date( file_path_last_modified )));
                            //}}}
                            // ZIP OLDER {{{
                            if(zip_entry_last_modified <= file_path_last_modified)
                            {
//*STORAGE*/String time_stamp = String.format("[%10s] (%10s older)", file_age, time_diff);
//*STORAGE*/Settings.MOM(TAG_STORAGE, ".....ZIP-OLDER: "+ String.format("%s not overriding [%s]", time_stamp, entry_name));
                                continue;
                            }
                            //}}}
                            // ZIP NEWER {{{
                            else {
//*STORAGE*/String time_stamp = String.format("[%10s] (%10s newer)", file_age, time_diff);
//*STORAGE*/Settings.MOM(TAG_STORAGE, ".....ZIP-NEWER: "+ String.format("%s ......UPDATING [%s]", time_stamp, entry_name));
                            }
                            //}}}
                        }
                        //}}}
                        // INSTALL MISSING ZIP ENTRIES {{{
                        else {
//*STORAGE*/Settings.MOM(TAG_STORAGE, ".....ZIP-NEW..: ["+ entry_name +"] ("+ DateFormat.getDateTimeInstance().format(new Date( zip_entry_last_modified ))+") INSTALLING");
                        }
                        //}}}
                        // OPEN  FILE {{{
                        FileOutputStream fos = new FileOutputStream( file_path );
                        int file_size = 0;

                        // }}}
                        // WRITE FILE {{{
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte bytes[] = new byte[1024];
                        int count;
                        while((count = zis.read( bytes )) != -1) {
                            baos.write(bytes, 0, count);
                            file_size += count;
                        }
                        fos.write( baos.toByteArray() );

                        //}}}
                        // CLOSE FILE {{{
                        zis.closeEntry();
                        fos.close();

                        //}}}
//*STORAGE*/Settings.MOM(TAG_STORAGE, "....EXTRACTING: ["+entry_name+"] ("+file_size+"b)");
                    //}
                    //}}}
                }
            }
            catch(Exception ex) { // {{{
                Settings.log_ex(ex, "ProfileArchive.unzip_InputStream: entry_name=["+ entry_name +"]");
            }

            try { if(zis != null) zis.close(); } catch(Exception ignored) { }
            // }}}
        }
        //}}}
        // get_list {{{
        public static ArrayList<String> get_list(String zip_file_path)
        {
            ArrayList<String> al = new ArrayList<>();

        //  String     file_path = "";
            String    entry_name = "";
            try  {

                int idx = zip_file_path.lastIndexOf("/");
                String base_name
                    = (idx >= 0)
                    ?  zip_file_path.substring(idx+1)
                    :  zip_file_path;

//*STORAGE*/Settings.MOM(TAG_STORAGE, "@@@ ProfileArchive ["+ base_name +"]:");

                // IN (OPEN)
                FileInputStream fis = new FileInputStream( zip_file_path );
                ZipInputStream  zis = new ZipInputStream ( fis );
                ZipEntry         ze = null;

                // IN (READ)
                int count = 0;
                while((ze = zis.getNextEntry()) != null)
                {
                    entry_name = ze.getName();
                    if( !ze.isDirectory() )
                        al.add( entry_name );
                    ++count;
//*STORAGE*/Settings.MOM(TAG_STORAGE, String.format("@@@ %s %3d %s", Settings.SYMBOL_FOLDER, count, "["+entry_name+"]"));
                }
                // IN (CLOSE)
                zis.close();
            }
            catch(Exception ex) {
//*STORAGE*/Settings.log_ex(ex, "ProfileArchive.get_list: entry_name=["+ entry_name +"]");//TSTORAGE
            }

            return al;
        }
        //}}}
        // check_dir {{{
        @SuppressWarnings("ResultOfMethodCallIgnored")
        public static void check_dir(String dir, String sub_dir_or_file)
        {
            int idx = sub_dir_or_file.lastIndexOf("/");

            String sub_dir
                = (idx >= 0)
                ?  sub_dir_or_file.substring(0, idx)
                :  "";

            String dir_path
                = (sub_dir == "")
                ?  dir
                :  dir +"/"+ sub_dir;

            File f = new File( dir_path );

            if( !f.isDirectory() ) {
//*PROFILE*/Settings.MON(TAG_PROFILE, "check_dir", "mkdirs  dir_path=["+dir_path+"]  sub_dir ["+sub_dir+"]");

                f.mkdirs();
            }
        }
        //}}}
    }
    // }}}
    //}}}


}

/* // {{{

:let @p="PROFILE"
:let @p="STORAGE"

:let @p="\\w\\+"

" ACTIVATED
  :g/^\/\*p\w*\*\/.*p/t$
" ......... -> COMMENT
  :g/^\/\*p\w*\*\/.*p/s/^/\//

" commented
  :g/^\/\/\*p\w*\*\/.*p/t$
" ..........-> ACTIVATE
  :g/^\/\/\*p\w*\*\/.*p/s/^\///

*/ // }}}

