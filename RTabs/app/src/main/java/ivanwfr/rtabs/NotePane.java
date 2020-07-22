package ivanwfr.rtabs; // {{{

//import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// }}}

class NotePane
{
    // CLASS
    public static        String NOTEPANE_JAVA_TAG = "NotePane (200716:15h:54)";
    // SHAPES {{{

    private static       String TAG_TAB  = Settings.TAG_TAB;

    // [     ROUND] TAG_CIRCLE TAG_RING   TAG_SATIN
    // [SEMI_ROUND] TAG_ONEDGE TAG_PADD_R
    // [ NOT_ROUND] TAG_SCROLL TAG_SQUARE TAG_TILE

    // ROUND .. (needs transparent bg) .. (see-through corners)
    public  static final String SHAPE_TAG_CIRCLE = "circle";
    public  static final String SHAPE_TAG_RING   =   "ring";
    public  static final String SHAPE_TAG_SATIN  =  "satin";

    // SEMI-ROUND
    public  static final String SHAPE_TAG_ONEDGE = "onedge";
    public  static final String SHAPE_TAG_PADD_R = "padd_r"; // MARGIN PAD RIGHT i.e. HIST, PROFILES
    public  static final String SHAPE_TAG_PADD_L = "padd_l";
    public  static final String SHAPE_TAG_MARK_L = "mark_l";
    public  static final String SHAPE_TAG_MARK_P = "mark_p";

    // NOT ROUND
    public  static final String SHAPE_TAG_SCROLL = "scroll"; // THUMB [fs_scroll]
    public  static final String SHAPE_TAG_SQUARE = "square";
    public  static final String SHAPE_TAG_TILE   =   "tile";

    //}}}
    //{{{
    public  static final int    NO_COLOR   = Color.parseColor("#11111111");   // i.e. very transparent - very black

    public  static final String TYPE_TOOL        =   "TOOL";
    public  static final String INFO_SEP         =  "\n...";
    public  static final String INFO_SEP_ENCODED = "\\n...";
    public  static final String TYPE_SHORTCUT    = "SHORTCUT";
    public  static final String TYPE_DASH        = "DASH";

    // }}}
    // Get_current_shape {{{
    public  static String Get_current_shape()
    {
        switch( Settings.GUI_STYLE )
        {
            case Settings.GUI_STYLE_ONEDGE: return SHAPE_TAG_ONEDGE;
            case Settings.GUI_STYLE_ROUND : return SHAPE_TAG_CIRCLE;
            case Settings.GUI_STYLE_SQUARE: return SHAPE_TAG_SQUARE;
            case Settings.GUI_STYLE_TILE  : return SHAPE_TAG_TILE  ;
            case Settings.GUI_STYLE_SATIN : return SHAPE_TAG_SATIN ;
            default:                        return SHAPE_TAG_SQUARE;
        }
    }
    //}}}
    // Get_next_shape {{{
    public  static String Get_next_shape(String shape)
    {
        switch(  shape.intern() ) {
            case SHAPE_TAG_SQUARE: return SHAPE_TAG_TILE  ;
            case SHAPE_TAG_TILE  : return SHAPE_TAG_CIRCLE;
            case SHAPE_TAG_CIRCLE: return SHAPE_TAG_ONEDGE;
            case SHAPE_TAG_ONEDGE: return SHAPE_TAG_SATIN ;
            case SHAPE_TAG_SATIN : return Settings.EMPTY_STRING;    // auto f(Settings)
            default              : return SHAPE_TAG_SQUARE;         // start over
        }
    }
    //}}}
    // Get_unicode_expanded_text {{{
    public static String Get_unicode_expanded_text(String text)
    {
        return Settings.ParseUnicode( text );
    }
    //}}}

    // POOL (GetInstance) .. (RecycleInstance)
    //{{{
    // {{{
    // Recycle instances from a [TO-BE-CLEARED-HASHMAP] (np_name, NotePane)
    private static final ArrayList<NotePane> Pool = new ArrayList<>();
    private static int NEW_count = 0; // New instances
    private static int STA_count = 0; // Stacked on the pool
    private static int REC_count = 0; // Recycled from the pool

    // }}}
    // GetInstance {{{
    public static NotePane GetInstance(String name, String type, int x, int y, int w, int h, float z, String tag, String text, int color, String shape, String tt)
    {
        int idx_last = Pool.size() - 1;
//idx_last = -1; // XXX
        if(idx_last < 0)
        {
            NEW_count += 1;
            return new NotePane(name, type, x, y, w, h, z, tag, text, color, shape, tt);
        }
        else {
            REC_count += 1;
            return
                Pool.remove( idx_last )
                .set(name, type, x, y, w, h, z, tag, text, color, shape, tt);
        }
    }
    // }}}
    // RecycleInstance {{{
    public  static  void RecycleInstance(NotePane np)
    {
        if(np.button != null) np.button.setTag( null ); // release [NbButton] instance's references to the GC
        /*.................*/ np       .setTag( null ); // ... and [NotePane] instance's references as well
        /*.......*/ Pool.add( np                     ); // .. (to be reused later)

        STA_count += 1; // track stats
    }
    // }}}
    // RecycleMap {{{
    public  static  void RecycleMap(HashMap<String, Object> hashMap)
    {
        for(Map.Entry<String, Object> entry : hashMap.entrySet())
        {
            Object                     o =  entry.getValue();
            if(                        o instanceof NotePane)
            RecycleInstance( (NotePane)o );
        }
    }
    // }}}
    // PoolStatus {{{
    public static String PoolStatus()
    {
    //  return "POOL [New Sav ReU Pool]\n"
        return "POOL: "
            +  String.format( "%3d", NEW_count)
            +  String.format(" %3d", STA_count)
            +  String.format(" %4d", REC_count)
            +  String.format(" %3d", Pool.size())
            ;
    }
    //}}}
    // PoolDescription {{{
    public static String PoolDescription()
    {
        return String.format("CREAT: %4d (number of times a new NotePane instance has been created)\n"             , NEW_count)
            +  String.format("STORE: %4d (number of times a NotePane has been pushed into the Pool to be saved)\n" , STA_count)
            +  String.format("REUSE: %4d (number of times a NotePane has been popped from the Pool to be reused)\n", REC_count)
            +  String.format("POOL=: %4d (number of currently available NotePane in the Pool)\n"                   , Pool.size())
            ;
    }
    //}}}
    //}}}

    // INSTANCE
    // NotePane {{{
    public  NotePane(String name, String type, int x, int y, int w, int h, float z, String tag, String text, int color, String shape, String tt)
    {
        set(name, type, x, y, w, h, z, tag, text, color, shape, tt);
    }
    //}}}
    // NotePane {{{
    public  NotePane()
    {
        String name   = "no-name";
        String tag    = "";
        String text   = "";
        String shape  = "";
        String tt     = "";
        String type   = NotePane.TYPE_SHORTCUT;
        int    color  = 0;
        int    x      = 0;
        int    y      = 0;
        float  z      = 0;
        int    h      = Settings.TAB_MIN_BTN_H;
        int    w      = Settings.TAB_MIN_BTN_W;
        set(name, type, 0, 0, w, h, z, tag, text, color, shape, tt);
    }
    //}}}
    //{{{

    // RESOURCES [name] [type] [tag] [text] [tt]
    String   name;
    String   type;
    String   tag;
    String   text = "";
    String   tt;

    // ATTRIBUTES [shape] [color] [bg_color] [fg_color]
    String   shape;
    int      color;
    int      bg_color;
    int      fg_color;

    // LAYOUT [x y w h] [z] [elevation] [weight]
    int      x, y, w, h;
    float    z;
    int      elevation;
    int      weight;

    // UI [button or textView] [needs_fitText]
    NpButton button;
    TextView textView;
    boolean  needs_fitText = false;

    // }}}
    // set {{{
    private NotePane set(String name, String type, int x, int y, int w, int h, float z, String tag, String text, int color, String shape, String tt)
    {
        // RESOURCES [name] [type] [tag] [text] [tt]
        this.name       = name;
        switch( type ) {
            case TYPE_SHORTCUT:          this.type = TYPE_SHORTCUT;  break;  // so that we can eval (type == NotePane.TYPE_SHORTCUT)
            case TYPE_TOOL:              this.type = TYPE_TOOL;      break;  // so that we can eval (type == NotePane.TYPE_SHORTCUT)
            default:                     this.type = type;           break;
        }
        if     ( tag.equals(TYPE_DASH )) this.tag  = TYPE_DASH;
        else if(TextUtils.isEmpty(tag) ) this.tag  = Settings.COMMENT_STRING;
        else                             this.tag  = tag;

        setTextAndInfo( text );
        this.tt         = tt;

        // ATTRIBUTES [shape] [color] [bg_color] [fg_color]
        set_shape       ( shape );
        this.color      = color;
        bg_color        = NO_COLOR;
        fg_color        = NO_COLOR;

        // LAYOUT [x y w h] [z] [elevation]
        this.x          = x;
        this.y          = y;
        this.w          = w;
        this.h          = h;
        this.z          = (z>0) ? z : 1;
        elevation       = 0;

        // UI [button or textView] [needs_fitText]
        button          = null;
        textView        = null;
        needs_fitText   = false;

        // instance returned for method call cascading
        return this;
    }
    //}}}

    // COMPONENTS
    // set_button {{{
    public void set_button(NpButton button)
    {
        this.button   = button;
        button.setText( getLabel() );

        this.textView =  null; // it's either one or the other .. (one may be attached to a recycled instance)
    }
    //}}}
    // set_textView {{{
    public void set_textView(TextView  textView)
    {
        this. textView =  textView;
        textView.setText( text );

        this. button   =  null; // it's either one or the other .. (one may be attached to a recycled instance)
    }
    //}}}
    // Get_view {{{
    public static View Get_view(NotePane np)
    {
        return (      np        != null)
            ?  ((     np.button != null)
                    ? np.button
                    : np.textView)
            :  null;
    }
    //}}}

    // LAYOUT
    // set_shape {{{
    public void set_shape(String shape)
    {
        this.shape = shape.intern(); // (allows identity check instead of equality)
        if(button != null)
            button.set_shape( this.shape );
    }
    //}}}
    // set_xy {{{
    public void set_xy(int x, int y)
    {
        this.x          = x;
        this.y          = y;
    }
    //}}}
    // set_xy_wh {{{
    public void set_xy_wh(String xy_wh)
    {
        try {
            String[] a = xy_wh.split(",");
            x          = Integer.parseInt( a[0].trim() );
            y          = Integer.parseInt( a[1].trim() );
            w          = Integer.parseInt( a[2].trim() );
            h          = Integer.parseInt( a[3].trim() );
        } catch(Exception ex) {
            throw new RuntimeException("*** NotePane.set_xy_wh("+xy_wh+"): "+ex.toString());
        }
    }
    //}}}
    // get_xy_wh {{{
    public String get_xy_wh()
    {
    //  return x+","+y+","+w+","+h;
        return String.format("%02d,%02d,%02d,%02d", x, y, w, h);
    }
    //}}}
    // set_weight {{{
    public void set_weight(int weight)
    {
        this.weight = weight;
    }
    //}}}
    // get_weight {{{
    public int get_weight()
    {
        return weight;
    }
    //}}}
    // printLayout {{{
    public String printLayout()
    {
        return String.format(" [%3d @ %3d] [%3d x %3d] %-12s @@@ %-32s @@@ [%s]"
                ,                 x,  y,   w,  h, name, tag, text.replace("\n","\\n"));
    }
    //}}}

    // TYPE
    //{{{
    public static boolean isATool    (NotePane np)  { return (np != null) && (np.type == NotePane.TYPE_TOOL);                                   }
    public static boolean isAToolHook(NotePane np)  { return (np != null) && (np.type == NotePane.TYPE_TOOL) && (np.tag.contains("TOOL=HOOK")); }
    public static boolean isAToolInfo(NotePane np)  { return (np != null) && (np.type == NotePane.TYPE_TOOL) && (np.tag.contains("TOOL=INFO")); }
    //}}}

    // TAG (String)
    // set_tag {{{
    public void set_tag(String tag)
    {
        this.tag   = tag;
        if(button != null)
            if(text.equals("") && !tag.equals(""))
                button.setText( getLabel() );
    }
    //}}}
    // is_a_PROFILE_button {{{
    public boolean is_a_PROFILE_button()
    {
        return tag.startsWith("PROFILE ");
    }
    //}}}

    // [setTag] [getTag] (Object)
    // {{{
    private       Object                    o = null;
    public   void setTag(Object o) {   this.o = o;    }
    public Object getTag(        ) { return o;        }
    // }}}

    // TT
    // set_tt {{{
    public void set_tt(String tt)
    {
        this.tt   = tt;
    }
    //}}}

    // TEXT
    // setText {{{
    public void setText(String text)
    {
//System.err.println("XXX NotePane.setText("+text+")");
        String info  = getTextInfo();
        if(    info == Settings.EMPTY_STRING) this.text = text;
        else                                  this.text = text + INFO_SEP + info;

        //if((this.text.indexOf("\n") >= 0) && !this.text.endsWith("\n")) this.text += "\n";

        if(button != null)
            button.setText( getLabel() );
    }
    //}}}
    // setTextAndInfo {{{
    public void setTextAndInfo(String textAndInfo)
    {
//System.err.println("XXX NotePane.setTextAndInfo("+textAndInfo+")");
        this.text  = textAndInfo;
        if(button != null)
            button.setText( getLabel() );
    }
    //}}}
    // getFullText (HEAD and TAIL) {{{
    public String getLabelWithInfo()
    {
        if(!text.equals(""))    return Get_unicode_expanded_text( text );
        else                    return getLabel(); // i.e. dynamically derived from tag displayed text
    }
    //}}}
    // getTextInfo (TAIL) {{{
    public String getTextInfo()
    {
        return GetTextInfo( text );
    }
    //}}}
    // GetTextInfo {{{
    private static String GetTextInfo(String text)
    {
        if(text.equals("")) return Settings.EMPTY_STRING;

        // EXPECTED FORMAT: [header][\n...][text]
        int idx = text.indexOf(INFO_SEP);
        if( idx < 0) return Settings.EMPTY_STRING;
        else         return text.substring( idx+INFO_SEP.length() ); // INFO_SEP excluded
    }
    //}}}
    // Text_contains_some_info {{{
    public static boolean Text_contains_some_info(String text)
    {
        return (GetTextInfo(text).length() > 0);
    }
    //}}}
    // getLabel (HEAD) {{{
    public String getLabel()
    {
        // 1/4 EXPLICIT [TEXT] .. NO! [INFO] [HTML] [PROFILE-ESCAPED-CHARS] // {{{
        if(!text.equals(""))
        {
            int      idx = text.indexOf  (INFO_SEP);            // [label][\n...info]
            String label = (idx > 0)
                ?           text.substring(0,   idx)            // DISCARD....: (\n...INFO.*)
                :           text;
            label        = Settings.ParseHtml       ( label );  // CLEANUP....: (<></> HTML formatting)
            label        = decode_text              ( label );  // SUBSTITUTE.: (PROFILE-LINE escaped chars)
            label        = Get_unicode_expanded_text( label );
            return label;
        }
        // }}}
        // 2/4 DEFAULT TO [NAME] // {{{
        if( tag.equals("") )
            return name;

        // }}}
        // 3/4 DEFAULT TO [TAG ARGS] // {{{
        if(tag.startsWith("SHELL") || tag.startsWith("RUN") || tag.startsWith("BROWSE")) {
            int pos = tag.indexOf(" ");
            if(pos > 0)
                return tag.substring(pos+1);
        }

        // }}}
        // 4/4 DEFAULT TO [TAG] // {{{
        return tag;
        // }}}
    }
    //}}}
    // has_empty_label {{{
    private static final String BLANK_CHARS = "1234567890 \n";    // to be considered blank: space or simply numbered labels
    public boolean has_empty_label()
    {
/* // {{{
        // FREE: HAS NO TEXT
        if(  text.equals(""))
            return true;

        // FREE: HAS THE BLANK MARKER TEXT
        if( text.equals( Settings.SYMBOL_EMPTY) )
            return true;
        else
            return false;
*/ // }}}
/* // {{{
        // NOT FREE: HAS SOME NON-BLANK TEXT
        for(int i=0; i<text.length(); ++i) {
            if(BLANK_CHARS.indexOf( text.charAt(i) ) < 0) // contains a NON-BLANCK character
                return false;
        }

        // FREE: HAS NO NON-BLANK TEXT
        return true;
*/ // }}}
        return Settings.LABEL_is_empty( text );
    }
    //}}}

    // CORNERS
    // {{{
    private static int[][] NeighbourCorners = null;

    //}}}
    // get_tabs_perimeter_rect {{{
    public static Rect get_tabs_perimeter_rect(HashMap<String, Object> hashMap)
    {
        int x_min = 1024;
        int y_min = 1024;
        int x_max = 0;
        int y_max = 0;
        for(Map.Entry<String,Object> entry : hashMap.entrySet()) {
            if(                   !( entry.getValue() instanceof NotePane) ) continue;
            NotePane np = (NotePane) entry.getValue();

            if((np.x       ) < x_min) x_min = (np.x       );
            if((np.y       ) < y_min) y_min = (np.y       );
            if((np.x + np.w) > x_max) x_max = (np.x + np.w);
            if((np.y + np.h) > y_max) y_max = (np.y + np.h);
        }
        Rect r   = new Rect();
        r.left   = x_min;
        r.top    = y_min;
        r.right  = x_max;
        r.bottom = y_max;

        return r;
    }
    //}}}
    // check_neighbors {{{
    public  static void check_neighbors(HashMap<String, Object> hashMap)
    {
        check_neighbors(hashMap, get_tabs_perimeter_rect( hashMap ), 1);//cell_size
    }
    //}}}
    // check_neighbors {{{
    public  static void check_neighbors(HashMap<String, Object> hashMap, Rect r)
    {
        check_neighbors(hashMap, r, 1); //cell_size
    }
    //}}}
    // check_neighbors_with_cell_size {{{
    public  static void check_neighbors_with_cell_size(HashMap<String, Object> hashMap, int cell_size)
    {
        check_neighbors(hashMap, get_tabs_perimeter_rect( hashMap ), cell_size);
    }
    //}}}
    // check_neighbors {{{
    private static void check_neighbors(HashMap<String, Object> hashMap, Rect r, int cell_size)
    {
        int x_max = 1 + (r.right  / cell_size);
        int y_max = 1 + (r.bottom / cell_size);
//*TAB*/ String caller = "check_neighbors";//TAG_TAB
//*TAB*/Settings.MOC(TAG_TAB, caller, "hashMap=[x"+ hashMap.size()    +" entries] cell_size=["+ cell_size +"]");
//*TAB*/Settings.MOM(TAG_TAB,    "...perimeter=[" + r                 +"]");
//*TAB*/Settings.MOM(TAG_TAB,    "......xy_max=[" + x_max+" x "+y_max +"]");

        if((x_max < 2) || (y_max < 2)) return;

        NeighbourCorners = new int[x_max + 1][y_max + 1];

        for(int[] row : NeighbourCorners) Arrays.fill(row, 0); // all cells empty

        // TAB PERIMETERS {{{
        int cell_size_2 = cell_size / 2;
        for(Map.Entry<String,Object> entry : hashMap.entrySet()) {
            if(                   !( entry.getValue() instanceof NotePane) ) continue;
            NotePane         np = (NotePane) entry.getValue();
            if( isAToolHook( np )                                          ) continue; // invisible hook anchor
            if( isAToolInfo( np )                                          ) continue; // invisible hook anchor
            if(              np.shape == NotePane.SHAPE_TAG_CIRCLE         ) continue; // shape with no corner
            if(              np.shape == NotePane.SHAPE_TAG_RING           ) continue; // shape with no corner
            if(              np.shape == NotePane.SHAPE_TAG_SATIN          ) continue; // shape with no corner

            int    top = (np.y        + cell_size_2) / cell_size    ;
            int   left = (np.x        + cell_size_2) / cell_size    ;
            int  right = (np.x + np.w + cell_size_2) / cell_size - 1;
            int bottom = (np.y + np.h + cell_size_2) / cell_size - 1;

            for(    int x = left; x <= right ; ++x) {
                for(int y = top ; y <= bottom; ++y) {
                    if( x < 0                    ) continue; // out of view
                    if( y < 0                    ) continue; // out of view
                    if(        (x == left) || (x == right )  // on left or  right edge
                            || (y == top ) || (y == bottom)  // on  top or bottom edge
                      )
                        NeighbourCorners[x][y] = 1;          // mark cell as occupied by this np
                }
            }
        }
        //}}}
        // TOUCHING TABS PERIMETERS {{{
        boolean style_has_no_corner = false;//Settings.is_GUI_STYLE_ONEDGE();
        for(Map.Entry<String,Object> entry : hashMap.entrySet()) {
            if(                   !( entry.getValue() instanceof NotePane) ) continue;
            NotePane np = (NotePane) entry.getValue();

            // style (corners)
            if(        style_has_no_corner
                    || (np.shape == NotePane.SHAPE_TAG_CIRCLE)
                    || (np.shape == NotePane.SHAPE_TAG_RING  )
                    || (np.shape == NotePane.SHAPE_TAG_SATIN )
              ) {
                if(np.button != null) {
                    np.button.clear_round_corners();      /* System.err.println("...NO CORNERS STYLE FOR: "+ np.button.toStringCorners()); */
                    continue;
                }
              }

            // button (corners)
            if(np.button == null                      ) { /* System.err.println("(np.button == null) FOR: "+ np.button.toStringCorners()); */ continue; }
            np.button.tl = false; np.button.tr = false;
            np.button.lt = false; np.button.lb = false;
            np.button.bl = false; np.button.br = false;
            np.button.rt = false; np.button.rb = false;

            // np (layout)
            if(np.x       < 0                         ) { /* System.err.println("(np.x < 0         ) FOR: "+ np.button.toStringCorners()); */ continue;  }
            if(np.y       < 0                         ) { /* System.err.println("(np.y < 0         ) FOR: "+ np.button.toStringCorners()); */ continue;  }

            // np (edges)
            int    top = (np.y        + cell_size_2) / cell_size    ;
            int   left = (np.x        + cell_size_2) / cell_size    ;
            int  right = (np.x + np.w + cell_size_2) / cell_size - 1;
            int bottom = (np.y + np.h + cell_size_2) / cell_size - 1;

            // neighbour (edges)
            int l_side = left  -1;
            int r_side = right +1;
            int  above = top   -1;
            int  below = bottom+1;

/* // corners {{{

 /top\|left\|bottom\|right
 /above\|below\|l_side\|r_side

 *        tl       tr   <--above  
 *      lt-ttttttttt-rt          
 *        l---------r             
 *        l---------r             
 *        l---------r             
 *        l---------r             
 *      lb-bbbbbbbbb-rb          
 *        bl       br   <--below 
 *       ^           ^            
 *       |           |            
 *  l_side           r_side       

 */ //}}}

            // [ABOVE] [BELOW]
            if((   left >= 0     ) && ( above >= 0     )) np.button.tl = (NeighbourCorners[ left   ][ above ] != 0); // some neighbour [ABOVE ] [ left corner]
            if((  right <  x_max ) && ( above >= 0     )) np.button.tr = (NeighbourCorners[ right  ][ above ] != 0); // some neighbour [ABOVE ] [right corner]
            if((   left >= 0     ) && ( below <  y_max )) np.button.bl = (NeighbourCorners[ left   ][ below ] != 0); // some neighbour [BELOW ] [ left corner]
            if((  right <  x_max ) && ( below <  y_max )) np.button.br = (NeighbourCorners[ right  ][ below ] != 0); // some neighbour [BELOW ] [right corner]

            // [LEFT-SIDE] [RIGHT-SIDE]
            if(( l_side >= 0     ) && ( top    >= 0     )) np.button.lt = (NeighbourCorners[ l_side ][ top    ] != 0); // some neighbour [AT-LEFT ] [top    corner]
            if(( l_side >= 0     ) && ( bottom <  y_max )) np.button.lb = (NeighbourCorners[ l_side ][ bottom ] != 0); // some neighbour [AT-LEFT ] [bottom corner]
            if(( r_side <  x_max ) && ( top    >= 0     )) np.button.rt = (NeighbourCorners[ r_side ][ top    ] != 0); // some neighbour [AT-RIGHT] [top    corner]
            if(( r_side <  x_max ) && ( bottom <  y_max )) np.button.rb = (NeighbourCorners[ r_side ][ bottom ] != 0); // some neighbour [AT-RIGHT] [bottom corner]

//*TAB*/Settings.MOM(TAG_TAB, np.button.toStringCorners());//TAG_TAB
        }
        // }}}

        NeighbourCorners = null;
    }
    //}}}

    // INSTANCE
    // clone {{{
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public NotePane clone()//Context context)
    {
        NotePane np = NotePane.GetInstance(name, type, x, y, w, h, z, tag, text, color, shape, tt);
    //  if(this.button != null) np.set_button( this.button.clone(context) );
        return np;
    }
    //}}}
    // toString {{{
    public String toString()
    {
    //  return "["+name+"]-["+Settings.ellipsis(text.replace("\n","") , 32)+"]";
    //  return full_description();
        return name;
    }
    //}}}
    // toLine {{{
    public String toLine()
    {
        // TAB.panel_usr3=type=SHORTCUT|tag=PROFILE ProfileDPI|zoom=1.5|xy_wh=13,6,16,5|text=PROFILE_ProfileDPI|color=1|shape=circle|tt=
        // TAB.tab2=type=SHORTCUT|xy_wh=01,03,12,02|shape=square|color=|zoom=1|tag=https://fr.m.wikipedia.org/|text=Wikipedia|tt=
        // ....name.type.........|xy_wh............|shape.......|color.|zoom..|tag............................|text..........|tt.
        String xy_wh = get_xy_wh();
        return    "TAB."+name                +"="  // i.e. Designer Registry Key
            +    "type="+type                +"|"
            +   "xy_wh="+xy_wh               +"|"
            +   "shape="+shape               +"|"
            +   "color="+color               +"|"
            +    "zoom="+z                   +"|"
            +     "tag="+encode_tag  (  tag) +"|"
            +    "text="+encode_label( text) +"|"
            +      "tt="+encode_str  (   tt)
            ;
    }
    //}}}
    // full_description {{{
    public String full_description()
    {
        String n_xywh = "";
        String l_xywh = "";
        String b_xywh = "";
        String t_xywh = "";
        if(this.button != null)
        {
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) button.getLayoutParams();
            n_xywh = String.format("\n-   TAB xy=[%4d %4d] wh=[%4d %4d]"  ,             x                ,             y                ,        w          ,        h          );
            l_xywh = String.format("\n-LAYOUT xy=[%4d %4d] wh=[%4d %4d]"  ,         rlp.leftMargin       ,         rlp.topMargin        ,    rlp.width      ,    rlp.height     );
            b_xywh = String.format("\n-BUTTON xy=[%4d %4d] wh=[%4d %4d]"  , (int)button.getX()           , (int)button.getY()           , button.getWidth() , button.getHeight());
            t_xywh = String.format("\n-TRANSL xy=[%4d %4d] SC=[%.2f %.2f]", (int)button.getTranslationX(), (int)button.getTranslationY(), button.getScaleX(), button.getScaleX());
        }

        return toLine()
            +  n_xywh
            +  l_xywh
            +  b_xywh
            +  t_xywh
            ;
    }
    //}}}

    // ENCODE DECODE
    //{{{
//  private static final String SYMBOL_BACKSLASH = "{U+005C}";
//  private static final String SYMBOL_EQUALS    = "{U+003D}";
    private static final String SYMBOL_UNDERSCORE= "{U+005F}";
    private static final String SYMBOL_VBAR      = "{U+007C}";

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

    public  static String decode_label(String s)
    {
        int idx = s.indexOf(INFO_SEP_ENCODED);
        if( idx >= 0)
        {
            String info = s.substring(  idx);           // tail is info
            s           = s.substring(0,idx);           // head is label
        //  return decode_text(s) + decode_str (info);  // return decoded label + unchanged info
            return decode_text(s) + decode_text(info);  // return decoded label + unchanged info
        }
        else {
            return decode_text(s);                      // return decoded label
        }
    }

    public  static String encode_label(String s)
    {
        int idx = s.indexOf(INFO_SEP_ENCODED);
        if( idx >= 0)
        {
            String info = s.substring(  idx);           // tail is info
            s           = s.substring(0,idx);           // head is label
            return encode_text(s) + encode_str(info);   // return decoded label + unchanged info
        }
        else {
            return encode_text(s);                      // return decoded label
        }
    }
/*
BACKGROUND_COLOR
...
SYNTAX:
- PROPERTY key val

SUPPORTED PROPERTIES (as of 160824):
- BAND_HIDE_WIDTH
- BG_VIEW_DEFAULT
- SOUND_DING

*/
    //.........................................................................(PRESERVING=============)        (EMBEDDING==============)
    public  static String encode_text (String s) { return encode_str(s).replace("_" , SYMBOL_UNDERSCORE).replace(              " ", "_"); }
    public  static String decode_text (String s) { return decode_str(s).replace("_", " "               ).replace(SYMBOL_UNDERSCORE, "_"); }
    //.........................................................................(EXPANDING==============)        (EXPANDING=============)

    public  static String encode_tag  (String s) { return encode_str(s);                    } // UNEXPAND SYMBOLS and CRLF
    public  static String decode_tag  (String s) { return decode_str(s);                    } //   EXPAND SYMBOLS and CRLF

    private static String encode_str(String s)
    {
        // [NOTE] REPLACE CHARACTERS USED WHILE PARSING A PROFILE-LINE [/NOTE]
        return s
            .replace( "|" , SYMBOL_VBAR     )
            .replace(" \n", "\\n"           )   // SPACE AT EOL(160926)
            .replace("\n" , "\\n"           )   // AFTER: single-backslache
            ;
        //  .replace("\r" , ""              )   // NOTE: what was this supposed to solve ?
        //  .replace( "=" , SYMBOL_EQUALS   )
        //  .replace("\\" , SYMBOL_BACKSLASH)
    }

    private static String decode_str(String s)
    {
        return s
            .replace(SYMBOL_VBAR     ,  "|")
            .replace(          "\\n" , "\n")   // BEFORE: single-backslache
            ;
        //  .replace(SYMBOL_EQUALS   ,  "=")
        //  .replace(SYMBOL_BACKSLASH, "\\")
    }

    //}}}

}

/* // {{{

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

