package ivanwfr.rtabs; // {{{

import android.text.TextUtils;

// }}}
// Comment {{{

/**
  CmdParser (parse once .. access components when needed)
 */
// }}}
class CmdParser
{
    public  static boolean  D = Settings.D;
    public  static void Set_D(boolean state) { D = state; }

    // PARSE {{{
    public static void parse(String _cmdLine)
    {
        if(TextUtils.equals(_cmdLine, cmdLine)) return; // ... no use to reparse my own member

        cmdLine    = _cmdLine.trim();

        cmd        =                cmdLine;
        argLine    =                cmdLine;
        args       = new String[] { cmdLine };
        arg1       = "";

        if((cmdLine.indexOf(' ') >= 0) || (cmdLine.indexOf(',') >= 0))
        {
            args    = cmdLine.split("[ ,]");
            cmd     = args[0];
            arg1    = (args.length > 1) ? args[1] : "";
            argLine = cmdLine.substring(cmd.length()+1).trim();
        }
        if(D) log( ToString() );
    }
    //}}}
    // getArgValue {{{
    public static String getArgValue(String key, String default_value)
    {
        String key_toUpperCase
            =  key.toUpperCase();

        for(int i=0; i < args.length; ++i)
        {
            String[] kv = args[i].split("=");
            if(kv.length > 1)
                if( kv[0].toUpperCase().equals( key_toUpperCase ) )
                    return kv[1];
        }
        return default_value;
    }
    // }}}
    // GET CACHED FIELDS {{{
    public static String   cmdLine    = "";    // first      word
    public static String   cmd        = "";    // first      word
    public static String   argLine    = "";    // following  words
    public static String[] args       = {};    // individual words
    public static String   arg1       = "";    // past cmd   word

    //}}}
    // ToString {{{
    private static String ToString()
    {
        return "\n"
            +  "C......cmdLine=["+ cmdLine     +"]\n"
            +  "C..........cmd=["+ cmd         +"]\n"
            +  "C......argLine=["+ argLine     +"]\n"
            +  "C..args.length=["+ args.length +"]\n"
            +  "C.........arg1=["+ arg1        +"]\n"
            +  "\n"
            ;

    }
    //}}}
    // log {{{
    private static void log(String msg)
    {
        Settings.MOM("CmdParser", msg);
    }
    //}}}
}

