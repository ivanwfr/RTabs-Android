package ivanwfr.rtabs.sandbox; // {{{

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import ivanwfr.rtabs.Settings; // {{{

// }}}
public class SandBox
{
    // {{{
    private static       String TAG_SANDBOX = Settings.TAG_SANDBOX;

    public  static final String GRAPH      = "GRAPH"      ;
    public  static final String ROTATE     = "ROTATE"     ;
//  public  static final String DELEGATE   = "DELEGATE"   ;
//  public  static final String FORWARD    = "FORWARD"    ;
//  public  static final String INTERCEPT  = "INTERCEPT"  ;
//  public  static final String LOGGER     = "LOGGER"     ;
//  public  static final String PAN        = "PAN"        ;
//  public  static final String PANGESTURE = "PANGESTURE" ;
    public  static final String TOUCH      = "TOUCH"      ;

    @SuppressLint("StaticFieldLeak")
    private static       RelativeLayout sandBox_container = null;
    private static       GraphView      mGraphView        = null;
    private static       TouchView      mTouchView        = null;
    // }}}
    public static boolean is_sandBox_implemented() { return true; } // debug src only
    public static void toggleSandBox(Context context, ViewGroup container, String cmdLine) //{{{
    {
//System.err.println("SandBox.toggleSandBox("+cmdLine+")");

        if     ( cmdLine.contains( GRAPH  ) ) showSandBox(context, container, GRAPH  );
        else if( cmdLine.contains( ROTATE ) ) showSandBox(context, container, ROTATE );
        else if( cmdLine.contains( TOUCH  ) ) showSandBox(context, container, TOUCH  );
        else if( is_sandBox_showing()       ) hideSandBox();
    }
    //}}}

    public static boolean is_sandBox_showing() //{{{
    {
        return (sandBox_container                         != null)
            && (sandBox_container.getParent()             != null)
            && (sandBox_container.getVisibility() == View.VISIBLE)
            ;
    }
    //}}}
    public static boolean is_sandBox_container(View view) //{{{
    {
        return (view == sandBox_container);
    }
    //}}}

    public static void hideSandBox() //{{{
    {
//System.err.println("SandBox.hideSandBox()");

        // sandBox_container
        if(sandBox_container == null) return;

        // parent
        ViewGroup     parent  = (ViewGroup)sandBox_container.getParent();
        if(parent            == null) return;

        // remove
        parent.removeView     ( sandBox_container );
    }
    //}}}
        // handle_MEMORY {{{
        public static void handle_MEMORY()
        {
/*SANDBOX*/Settings.MOC(TAG_SANDBOX, "SandBox.handle_MEMORY");
            if(mTouchView != null) mTouchView.handle_MEMORY();
        }
        //}}}

    // showSandBox {{{
    private static void showSandBox(Context context, ViewGroup container, String sandBoxType)
    {
        // create
        if(sandBox_container ==  null) create_sandBox_container(context, container);

        // select
        switch( sandBoxType )
        {
            case GRAPH:
                // {{{
//System.err.println("SandBox.showSandBox("+sandBoxType+")");
                mGraphView .setVisibility( View.  VISIBLE );
                mTouchView .setVisibility( View.INVISIBLE );

                break;
                // }}}
            case ROTATE:
            case TOUCH:
                //{{{
//System.err.println("SandBox.showSandBox("+sandBoxType+")");
                mGraphView.setVisibility( View.INVISIBLE );
                mTouchView.setVisibility( View.  VISIBLE );
                mTouchView .start_sample( sandBoxType );

                break;
                // }}}
            default:
//System.err.println("SandBox.showSandBox("+sandBoxType+"): NOT IMPLEMENTED");
        }

        // add
        if(sandBox_container.getParent() == null)
            container.addView( sandBox_container );
    }
    //}}}
    // create_sandBox_container {{{
    private static void create_sandBox_container(Context context, ViewGroup container)
    {
        sandBox_container = new RelativeLayout( context );

        // GraphView {{{
        mGraphView = new GraphView(context);
        mGraphView.setBackgroundColor( Color.parseColor("#ff111122") );

        RelativeLayout.LayoutParams rlp;
        rlp        = new RelativeLayout.LayoutParams(1,1);
        rlp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        rlp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
        mGraphView.setLayoutParams( rlp );
        //}}}
        sandBox_container.addView( mGraphView );
        // TouchView {{{
        mTouchView = new TouchView(context);

        rlp        = new RelativeLayout.LayoutParams(1,1);
        rlp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        rlp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
        mTouchView.setLayoutParams( rlp );
        //}}}
        sandBox_container.addView( mTouchView );
    }
    //}}}
}

/* // {{{
:let @p="SANDBOX"
:let @p="\\w\\+"

" ACTIVATED
  :g/^\/\*p\w*\*\/.*p/t$
" ......... -> commented
  :g/^\/\*p\w*\*\/.*p/s/^/\//


" commented
  :g/^\/\/\*p\w*\*\/.*p/t$
" ..........-> ACTIVATED
  :g/^\/\/\*p\w*\*\/.*p/s/^\///


*/ // }}}

