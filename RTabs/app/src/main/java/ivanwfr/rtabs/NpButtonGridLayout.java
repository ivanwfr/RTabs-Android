package ivanwfr.rtabs; // {{{

import android.graphics.Point;

// }}}
@SuppressWarnings("WeakerAccess")
class NpButtonGridLayout
{
    // {{{
    public  static final int    GRID_AUTO               = 0;
    public  static final int    GRID_MULTI_COL          = 1;
    public  static final int    GRID_SINGLE_COL         = 2;

    private static final int    AUTOPLACE_ROWS_DEFAULT  = 3;
    private static final int    AUTOPLACE_COLS_DEFAULT  = 3;

    private static final int    AUTOPLACE_W_MIN         = 3;
    private static final int    AUTOPLACE_H_MIN         = 2;

    private static final int    AutoPlace_SPACING       = 0;//1;

    private static       int   AutoPlace_COLS           = AUTOPLACE_COLS_DEFAULT;
    private static       int   AutoPlace_ROWS           = AUTOPLACE_ROWS_DEFAULT;
    private static       int   AutoPlace_WRAP_X         = 0;
    private static       int   AutoPlace_WRAP_Y         = 0;
    private static   boolean   AutoPlace_HAS_A_GAP      = false;

    private static       Point AutoPlacePoint           = new Point(0,0);
    private static       Point AutoPlaceSize            = new Point(1,1);

    //}}}
    // ResetAutoPlace {{{
    public static void ResetAutoPlace()
    {
        ResetAutoPlace_x_y_cols(0, 0, AUTOPLACE_COLS_DEFAULT);
    }
    //}}}
    // ResetAutoPlace(grid_type, cells) {{{
    public static void ResetAutoPlace(int grid_type, int cells) { ResetAutoPlace(grid_type, cells, 0); }
    public static void ResetAutoPlace(int grid_type, int cells, int cols_min)
    {
        int cols;
        switch( grid_type )
        {
            case GRID_AUTO:
                cols = 1 + (int)Math.sqrt( cells );
                NpButtonGridLayout.ResetAutoPlace_x_y_cols(1, 1, cols);
                break;

            case GRID_MULTI_COL:
                cols = 1+ (int)Math.sqrt( cells );

                if(cols < cols_min) cols = cols_min;

                if(Settings.DISPLAY_W > Settings.DISPLAY_H) {
                    NpButtonGridLayout.ResetAutoPlace_x_y_cols(1, 1, cols); // like AUTO
                }
                else {
                    cols = cols/2;
                    if(cols < cols_min) cols = cols_min;
                    NpButtonGridLayout.ResetAutoPlace_x_y_cols(1, 1, cols); // make wider than AUTO (cols/2)
                    int rows = 2 + cells/cols;
                    int    w = Settings.DISPLAY_W / cols / Settings.TAB_GRID_S;
                    int    h = Settings.DISPLAY_H / rows / Settings.TAB_GRID_S;
                    NpButtonGridLayout.SetAutoPlaceSize(3,2 , w,h);
                }
                break;

            case GRID_SINGLE_COL:
                NpButtonGridLayout.ResetAutoPlace_x_y_cols(0, 1, 1);
                int w = Get_SINGLE_COL_W();
                int h = Get_SINGLE_COL_H();
                NpButtonGridLayout.SetAutoPlaceSize(w,h , w,h);
                break;

            default:
                throw new RuntimeException("ResetAutoPlace: Wrong grid_type=["+grid_type+"].");
        }

    }
    //}}}
    // ResetAutoPlace_x_y_rows {{{
    public static void ResetAutoPlace_x_y_rows(int x, int y, int rows)
    {
        AutoPlace_WRAP_X = x;
        AutoPlace_WRAP_Y = y;
        AutoPlacePoint   = new Point(x,y);

        AutoPlace_ROWS   = (rows>=1) ? rows : 1;
        AutoPlace_COLS   = 0;

        SetAutoPlaceSize(AUTOPLACE_W_MIN, AUTOPLACE_H_MIN, 0, 0); // min no max
    }
    //}}}
    // ResetAutoPlace_x_y_cols {{{
    public static void ResetAutoPlace_x_y_cols(int x, int y, int cols)
    {
        AutoPlace_WRAP_X = x;
        AutoPlace_WRAP_Y = y;
        AutoPlacePoint   = new Point(x,y);

        AutoPlace_ROWS   = 0;
        AutoPlace_COLS   = (cols>=1) ? cols : 1;

        SetAutoPlaceSize(AUTOPLACE_W_MIN, AUTOPLACE_H_MIN, 0, 0); // min no max
    }
    //}}}
    // SetAutoPlaceSize {{{
/*
    public static void SetAutoPlaceSize(int w_min, int h_min, int w_max, int h_max)
    {
        int cells = 0;
        if(AutoPlace_ROWS > 0) cells = AutoPlace_ROWS;
        else                   cells = AutoPlace_COLS;
        if(cells >= 1)
        {
            int fit_w = (Settings.DISPLAY_W - 2*(AutoPlace_WRAP_X * Settings.TAB_GRID_S)) / (cells * Settings.TAB_GRID_S);
            int fit_h = (Settings.DISPLAY_H - 2*(AutoPlace_WRAP_Y * Settings.TAB_GRID_S)) / (cells * Settings.TAB_GRID_S);

            if((w_min>0) && (h_min>0)) { if((fit_w < w_min) || (fit_h < h_min)) { fit_w = w_min; fit_h = h_min; } }
            if((w_max>0) && (h_max>0)) { if((fit_w > w_max) || (fit_h > h_max)) { fit_w = w_max; fit_h = h_max; } }

            AutoPlaceSize = new Point(fit_w, fit_h);
        }
        else {
            AutoPlaceSize = new Point(2*Settings.TAB_MIN_BTN_W, Settings.TAB_MIN_BTN_H+1);
        }
    }
*/
    private static void SetAutoPlaceSize(int w_min, int h_min, int w_max, int h_max)
    {
        int cells = 0;
        if(AutoPlace_ROWS > 0) cells = AutoPlace_ROWS;
        else                   cells = AutoPlace_COLS;
        if(cells >= 1)
        {
            int fit_w = (Settings.DISPLAY_W - 2*(AutoPlace_WRAP_X * Settings.TAB_GRID_S)) / (cells * Settings.TAB_GRID_S);
            int fit_h = (Settings.DISPLAY_H - 2*(AutoPlace_WRAP_Y * Settings.TAB_GRID_S)) / (cells * Settings.TAB_GRID_S);


            if((w_min>0) && (h_min>0)) {
                if(fit_h < h_min) fit_h = h_min;
                if(fit_w < w_min) fit_w = w_min;
            }

            if((w_max>0) && (h_max>0)) {
                if(fit_h > h_max) fit_h = h_max;
                if(fit_w > w_max) fit_w = w_max;
            }

            AutoPlaceSize = new Point(fit_w, fit_h);
        }
        else {
            AutoPlaceSize = new Point(Settings.TAB_MIN_BTN_W, Settings.TAB_MIN_BTN_H);
        }
    }
    //}}}
    // Get_free_xy_wh {{{
    public static String Get_free_xy_wh()
    {
        return Get_free_xy_wh(AutoPlace_SPACING, 0);
    }
    public static String Get_free_xy_wh(int offset)
    {
        return Get_free_xy_wh(AutoPlace_SPACING, offset);
    }
    private static String Get_free_xy_wh(int spacing, int offset)
    {
        Point  location = _get_free_grid_xy(offset);
        return location.x+","+location.y+"," + (AutoPlaceSize.x-spacing)+","+(AutoPlaceSize.y-spacing);
    }
    //}}}
    // Get_SINGLE_COL_W {{{
    public  static int Get_SINGLE_COL_W()
    {
        int    display_min = Math.min(Settings.DISPLAY_W,    Settings.DISPLAY_H);
        int    col_width   = (int)(0.5 + display_min / ( 2 * Settings.TAB_GRID_S)); // 1920x1200: 1200 / ( 2*20) = 31 grid_cells per tab
        col_width          = Math.max(col_width , 12);                              //  800x480 :  480 / ( 2*20) = 12 grid_cells per tab
        return col_width;
    }
                //}}}
    // Get_SINGLE_COL_H {{{
    public  static int Get_SINGLE_COL_H()
    {
        int    display_min = Math.min(Settings.SCREEN_W,     Settings.SCREEN_H);
        int    col_height  = (int)(0.5 + display_min / (10 * Settings.TAB_GRID_S)); // 1920x1200: 1200 / (10*20) =  6 grid_cells per tab
        col_height         = Math.max(col_height, 3);                               //  800x480 :  480 / (10*20) =  3 grid_cells per tab
        return col_height;
    }
    //}}}
    // _get_free_grid_xy {{{
    private static final Point this_grid_point = new Point();
    private static Point _get_free_grid_xy(int offset)
    {
        // THIS STEP {{{
        this_grid_point.x       = AutoPlacePoint.x;
        this_grid_point.y       = AutoPlacePoint.y;

        //}}}
        // NEXT STEP {{{
        if(AutoPlace_ROWS > 0) {
            AutoPlacePoint.y += AutoPlaceSize.y;    // NEXT ROW-CELL
            if( !AutoPlace_HAS_A_GAP ) {
                AutoPlacePoint.x  += offset;
                AutoPlace_WRAP_X  += offset;
                this_grid_point.x += offset;
                //AutoPlace_HAS_A_GAP = true;
            }
        }
        else {
            AutoPlacePoint.x += AutoPlaceSize.x;    // NEXT COL-CELL
            if( !AutoPlace_HAS_A_GAP ) {
                AutoPlacePoint.y  += offset;
                AutoPlace_WRAP_Y  += offset;
                this_grid_point.y += offset;
                //AutoPlace_HAS_A_GAP = true;
            }
        }

        //}}}
        // WRAP {{{
        if(AutoPlace_ROWS > 0) {
            if( AutoPlacePoint.y >= (AutoPlace_WRAP_Y + AutoPlaceSize.y*AutoPlace_ROWS))
            {
                AutoPlacePoint.y    =  AutoPlace_WRAP_Y;    // TO FIRST ROW
                AutoPlace_WRAP_X   +=  AutoPlaceSize.x ;    // OF  NEXT COL
                AutoPlacePoint.x    =  AutoPlace_WRAP_X;
                AutoPlace_HAS_A_GAP = false;
            }
            else
                AutoPlace_WRAP_X += offset;                 // OR JUST FOLLOW OFFSET
        }
        else {
            if( AutoPlacePoint.x >= (AutoPlace_WRAP_X + AutoPlaceSize.x*AutoPlace_COLS))
            {
                AutoPlacePoint.x    =  AutoPlace_WRAP_X;    // TO FIRST COL
                AutoPlace_WRAP_Y   +=  AutoPlaceSize.y ;    // OF  NEXT ROW
                AutoPlacePoint.y    =  AutoPlace_WRAP_Y;
                AutoPlace_HAS_A_GAP = false;
            }
            else
                AutoPlace_WRAP_Y += offset;                 // OR JUST FOLLOW OFFSET
        }

        //}}}
        return  this_grid_point;
    }
    //}}}
}

/* // {{{
    // _get_free_grid_xy {{{
    private static Point _get_free_grid_xy(int offset)
    {
        Point thisAutoPlacePoint = new Point(AutoPlacePoint.x, AutoPlacePoint.y);
        // STEP {{{

        if(AutoPlace_ROWS > 0)
        {
            AutoPlacePoint.y         += AutoPlaceSize.y; // NEXT ROW-CELL
            if((offset != 0))// && !AutoPlace_HAS_A_GAP)  // COL GAP .. (only on first offset)
            {
                AutoPlacePoint.x     += offset;          // this cell offset
                AutoPlace_WRAP_X     += offset;
                AutoPlace_HAS_A_GAP   = true;            // this row stamped as having a gap offset
                thisAutoPlacePoint.x += offset * Settings.TAB_GRID_S;
            }
        }
        else {
            AutoPlacePoint.x         += AutoPlaceSize.x; // NEXT COL-CELL
            if((offset != 0))// && !AutoPlace_HAS_A_GAP)  // ROW GAP .. (only on first offset)
            {
                AutoPlacePoint.y     += offset;          // this cell offset
                AutoPlace_WRAP_Y     += offset;
                AutoPlace_HAS_A_GAP   = true;            // this col stamped as having a gap offset
                thisAutoPlacePoint.y += offset * Settings.TAB_GRID_S;
            }
        }

        //}}}
        // WRAP {{{
        if(AutoPlace_ROWS > 0)
        {
            if( AutoPlacePoint.y     >= (AutoPlace_WRAP_Y + AutoPlaceSize.y*AutoPlace_ROWS))
            {
                AutoPlacePoint.x      =  AutoPlace_WRAP_X;                                // next cell rol
                AutoPlacePoint.y      =  AutoPlace_WRAP_Y;                                // back to TOP
                AutoPlace_HAS_A_GAP   = false;                                            // new row (may receive a gap)
            }
        }
        else {
            if( AutoPlacePoint.x     >= (AutoPlace_WRAP_X + AutoPlaceSize.x*AutoPlace_COLS))
            {
                AutoPlacePoint.x      =  AutoPlace_WRAP_X;                               // back to left
                AutoPlacePoint.y      =  AutoPlace_WRAP_Y;                                // next cell row
                AutoPlace_HAS_A_GAP   = false;                                            // new row (may receive a gap)
            }
        }

        //}}}
        return  thisAutoPlacePoint;
    }
    //}}}
*/ // }}}
