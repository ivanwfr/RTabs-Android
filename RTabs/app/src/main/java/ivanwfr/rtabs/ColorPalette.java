package ivanwfr.rtabs; // {{{

import android.graphics.Color;

// }}}
class ColorPalette {

    // BRIGHTNESS
    public static int GetDarkestColor(int[] colors)// {{{
    {
        int color = Color.WHITE;

        for(int i=0; i < colors.length; ++i)
        {
            if(GetBrightness(colors[i]) < GetBrightness(color))
                color = colors[i];
        }

        return color;
    }
    //}}}
    public static int GetBrightestColor(int[] colors)// {{{
    {
        int color = Color.BLACK;

        for(int i=0; i < colors.length; ++i)
        {
            if(GetBrightness(colors[i]) > GetBrightness(color))
                color = colors[i];
        }

        return color;
    }
    //}}}

    public static int GetNextDarkColor(int[] colors, int index)// {{{
    {
        int color       = colors[index];
        int brightness  = GetBrightness(color);

        for(int i=index+1; i != index; ++i)
        {
            if(i == colors.length) { i = -1; continue; }

            if(GetBrightness(colors[i]) < brightness) {
                color       = colors[i];
                return color;
                //brightness  = GetBrightness(color);
            }
        }

        return color;
    }
    //}}}
    public static int GetNextLightColor(int[] colors, int index)// {{{
    {
        int color       = colors[index];
        int brightness  = GetBrightness(color);

        for(int i=index+1; i != index; ++i)
        {
            if(i == colors.length) { i = -1; continue; }

            if(GetBrightness(colors[i]) > brightness) {
                color       = colors[i];
                return color;
                //brightness  = GetBrightness(color);
            }
        }

        return color;
    }
    //}}}

    public static int GetColorDarker(int color, double factor)// {{{
    {
        if((factor < 0) || (factor > 1)) return color;
        int incr = (int)(255 * factor);

        int r = Math.max(Color.red  (color) - incr,   0);
        int g = Math.max(Color.green(color) - incr,   0);
        int b = Math.max(Color.blue (color) - incr,   0);
        return Color.rgb(r, g, b);
    }
    // }}}
    public static int GetColorLighter(int color, double factor)// {{{
    {
        if((factor < 0) || (factor > 1)) return color;
        int incr = (int)(255 * factor);

        int r = Math.min(Color.red  (color) + incr, 255);
        int g = Math.min(Color.green(color) + incr, 255);
        int b = Math.min(Color.blue (color) + incr, 255);
        return Color.rgb(r, g, b);
    }
    // }}}

    public static int GetBrightness(int color) //{{{
    {
        // http://www.nbdtech.com/Blog/archive/2008/04/27/Calculating-the-Perceived-Brightness-of-a-Color.aspx
        int c_R = Color.red  (color);
        int c_G = Color.green(color);
        int c_B = Color.blue (color);
        return (int)Math.sqrt(
                c_R * c_R * 0.241 +
                c_G * c_G * 0.691 +
                c_B * c_B * 0.068);
    }
    //}}}

    public static int GetColorLightnessTo(int color, int to)// {{{
    {
        int     brightness  = GetBrightness( color );                       // current color brightness
        boolean brightening = (to > brightness);                            // brighter or darker ?
    //  int    hit_rgb_mask = Get_first_impacted_mask(color, brightening);  // one of RGB component to turn ON or OFF in the process

        if( brightening )
        {
            while(brightness < to) {
                int unchanged  = brightness;
                color          = GetColorLighter(color, 0.15);
            //  if((color & hit_rgb_mask) == hit_rgb_mask) break;   // ONE OF RGB JUST TURNED [PLAIN ON]
                brightness     = GetBrightness(color);
                if(brightness == unchanged) break;                  // STOP WHEN MAXED OUT
            }
        }
        else {
            while(brightness > to) {
                int unchanged  = brightness;
                color          = GetColorDarker(color, 0.15);
            //  if((color & hit_rgb_mask) == 0x00) break;           // ONE OF RGB JUST TURNED [PLAIN OFF]
                brightness     = GetBrightness(color);
                if(brightness == unchanged) break;                  // STOP WHEN MAXED OUT
            }}
        return color;
    }
    // }}}
    private static final int R_MASK = 0xFF << 16;
    private static final int G_MASK = 0xFF <<  8;
    private static final int B_MASK = 0xFF      ;
    private static       int Get_first_impacted_mask(int color, boolean brightening) //{{{
    {
        int c_R = (color >> 16) & 0xFF; //Color.red  (color);
        int c_G = (color >>  8) & 0xFF; //Color.green(color);
        int c_B = (color      ) & 0xFF; //Color.blue (color);
        if( brightening )
        {
            // first color to turn 0xFF (PLAIN ON ) while incrementing RGB
            if     ((c_R >= c_G) && (c_R >= c_B))   return R_MASK;
            else if((c_G >= c_R) && (c_G >= c_B))   return G_MASK;
            else                                    return B_MASK;
        }
        else {
            // first color to turn 0x00 (PLAIN OFF) while decrementing RGB
            if     ((c_R <= c_G) && (c_R <= c_B))   return R_MASK;
            else if((c_G <= c_R) && (c_G <= c_B))   return G_MASK;
            else                                    return B_MASK;
        }
    }
    //}}}

}

