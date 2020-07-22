package ivanwfr.rtabs.sandbox; // {{{

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

// }}}
//  SEE DEBUG FLAVOR: app/src/debug/java/ivanwfr/rtabs/sandbox/SandBoxImpl.java
public class SandBox
{
    public static boolean is_sandBox_implemented()          { return false; }
    public static    void toggleSandBox         (Context context, ViewGroup container, String cmdLine) { return      ; }

    public static boolean is_sandBox_showing    ()          { return false; }
    public static boolean is_sandBox_container  (View view) { return false; }

    public static    void hideSandBox           ()          { }
    public static    void handle_MEMORY         ()          { }
}
/* // {{{
{
    private static Class impl;
    static {
        try {
            impl = Class.forName("ivanwfr.rtabs.sandbox.SandBoxImpl");
        } catch(Exception ignore) { }
    }

    public static boolean is_sandBox_implemented                           () { return (impl != null); }

    public static    void hideSandBox                                      () { if(impl == null) return      ;        SandBoxImpl.hideSandBox  (); }
    public static    void toggleSandBox(Context context, ViewGroup container) { if(impl == null) return      ;        SandBoxImpl.toggleSandBox(context, container); }
    public static boolean is_sandBox_container                    (View view) { if(impl == null) return false; return SandBoxImpl.is_sandBox_container(view); }
    public static boolean is_sandBox_showing                               () { if(impl == null) return false; return SandBoxImpl.is_sandBox_showing();       }
}
*/ // }}}

