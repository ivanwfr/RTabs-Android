package ivanwfr.rtabs; // {{{

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/*
:!start explorer "http://www.edumobile.org/android/clipboard-example-in-android-development/"
*/

// }}}
class Clipboard
{
    //{{{
    public static        String CLIPBOARD_JAVA_TAG = "CLIPBOARD (180511:22h)";
    private static String TAG_CLIPBOARD = Settings.TAG_CLIPBOARD;

    private static boolean  M = Settings.M;
    public  static void Set_M(boolean state) { M = state; }

    private static ClipboardManager mClipboardManager;
    // }}}
    // FOLLOW ACTIVITY LIFE CYCLE {{{
    // onPostCreate {{{
    public static void onPostCreate()
    {
        mClipboardManager = (ClipboardManager)RTabs.activity.getSystemService( Context.CLIPBOARD_SERVICE );
        mClipboardManager.addPrimaryClipChangedListener( mPrimaryChangeListener );
    }
    //}}}
    // onDestroy {{{
    public static void onDestroy()
    {
        mClipboardManager.removePrimaryClipChangedListener( mPrimaryChangeListener );
    }
    //}}}
    //}}}
    // OnPrimaryClipChangedListener {{{
    private static final ClipboardManager.OnPrimaryClipChangedListener mPrimaryChangeListener = new ClipboardManager.OnPrimaryClipChangedListener()
    {
        public void onPrimaryClipChanged()
        {
        String caller = "onPrimaryClipChanged";
//*CLIPBOARD*/Settings.MOC(TAG_CLIPBOARD, caller);
            FetchClipData();
        }
    };

    //}}}
    // FetchClipData {{{
    private static void FetchClipData()
    {
        String caller = "FetchClipData";
//*CLIPBOARD*/Settings.MOC(TAG_CLIPBOARD, caller);
        // MIME TYPES // {{{
        // MIME TYPES // {{{
        ClipData mClipData = mClipboardManager.getPrimaryClip();
        String[] mimeTypes
            = (mClipData != null)
            ? mClipData.getDescription().filterMimeTypes("*/*")
            : null;
        if(mimeTypes != null)
        {
            for(int i=0; i < mimeTypes.length; i++)
            {
                if(M) Settings.MON(TAG_CLIPBOARD, "FetchClipData", "Clipboard.FetchClipData: ...mimeType["+i+"]=["+ mimeTypes[i] +"]");
            }
        }

        // }}}
        // TEXT INTENT URI // {{{
        if(mClipData != null)
        {
            ClipData.Item item = mClipData.getItemAt(0);
        //  if     (item.getIntent() != null) { _store("INTENT", item.getIntent().toUri(0)  ); }
        //  else if(item.getUri()    != null) { _store("URI"   , item.getUri   ().toString()); }
        //  else if(item.getText()   != null) { _store("TEXT"  , item.getText  ().toString()); }
            if     (item.getIntent() != null) { _store("INTENT", item.coerceToText( RTabs.activity ).toString()); }
            else if(item.getUri()    != null) { _store("URI"   , item.coerceToText( RTabs.activity ).toString()); }
            else if(item.getText()   != null) { _store("TEXT"  , item.coerceToText( RTabs.activity ).toString()); }

        }
        // }}}
    }
    //}}}
    //}}}
    // PASTE TO {{{
    public void pastePlainText (String text) { mClipboardManager.setPrimaryClip(ClipData.newPlainText("Plain Text" , text)); }
    public void pasteStyledText(String text) { mClipboardManager.setPrimaryClip(ClipData.newPlainText("Styled Text", text)); }
    public void pasteUri       (String  uri) { mClipboardManager.setPrimaryClip(ClipData.newRawUri   ("URI"        ,                                Uri.parse( uri ) )); }
    public void pasteIntent    (String  uri) { mClipboardManager.setPrimaryClip(ClipData.newIntent   ("VIEW intent", new Intent(Intent.ACTION_VIEW, Uri.parse( uri )))); }

    //}}}
    // COPY FROM {{{
    // {{{
    private static String CB_Type = "";
    private static String CB_Text = "";

    // }}}
    // _store {{{
    private static void _store(String _type, String _text)
    {
        String caller = "_store";
//*CLIPBOARD*/Settings.MOC(TAG_CLIPBOARD, caller+"("+_type+" , "+Settings.ellipsis(_text,24)+")");
        if(_text.length() < 1) return;

        CB_Type = _type;
        CB_Text = _text;
        if(M) Settings.MON(TAG_CLIPBOARD, "_store", "Clipboard._store("+CB_Type+"=["+CB_Text+"])");

        // [NEW CLIPPED URL]
        if(        CB_Text.startsWith("https:")
                || CB_Text.startsWith("http:" )
                || CB_Text.startsWith("ftp:"  )
                || CB_Text.startsWith("file:" )
          ) {
            String url = Settings.Get_normalized_url( CB_Text );
//*CLIPBOARD*/Settings.MOC(TAG_CLIPBOARD, "url=["+url+"]");

            RTabs.bookmark_Clipboard_URL( url );
        }
        // [NEW CLIPPED TEXT]
        else {
            RTabs.new_Clipboard_TEXT( CB_Text );
        }

        // TODO [ADD APP-LEVEL-LISTENERS]
    }
    //}}}
    //}}}
    // getType {{{
    public static String getType()
    {
        return CB_Type;

    }
    //}}}
    // getText {{{
    public static String getText()
    {
        return CB_Text;

    }
    //}}}
}

/* // {{{

:let @p="CLIPBOARD"

:let @p="\\w\\+"

" activated
  :g/^\/\*p\w*\*\/.*p/t$
" ......... -> COMMENT
  :g/^\/\*p\w*\*\/.*p/s/^/\//
" commented
  :g/^\/\/\*p\w*\*\/.*p/t$
" ..........-> ACTIVATE
  :g/^\/\/\*p\w*\*\/.*p/s/^\///

*/ // }}}
