package ivanwfr.rtabs; // {{{

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

// }}}
// Comment {{{
/**
  * URL grouping support for the layout of 2 or 3 URLs.
  *
  * === ASSERTION ===================================================================================
  * - Only 3 web pages may be displayed side by side by the application.
  *
  * === CONFORMING RULES ============================================================================
  *
  * - LOOKING FOR AN URL'S GROUP MAY HAVE NO ASSOCIATED URLS
  *   ..(none of this profile's existing groups contains that URL).
  *
  * - ADDING TO A GROUP INVOLVES AT LEAST 2 URLS.
  *   - any of which will be used to identify an existing group to add the other to .. (if not there already).
  *   - if no group contains either, a new group will be created containing both URLs.
  *
  * - Adding to a group that already contains 3 entires:
  *   - has no effect.
  *
  * - REMOVING FROM A GROUP MAY RESULT IN:
  *   - EITHER: an URL pair entry .. a group containing the 2 remaining URLs.
  *   - ....OR: ..a deleted entry .. as no group may contain less than 2 associated URLs.
  *
  * === DEFINITIONS =================================================================================
  *
  * "profile_key" is to be taken as one of the entries of the list:
  * - profile  .. first profile's entry
  * - profile1 .. second one
  * - ...       (NOTE: deleted entrie will be reused)
  * - profileN .. current last
  *
  */ // }}}
public class WebGroup
{
    // SINGLETON
    // {{{

    // MONITORS
    private static       String TAG_WEBGROUP    = Settings.TAG_WEBGROUP;

    // RESULT OF CALLING ADD_PROFILE_URL_PAIR:
    public  static final int      BOTH_FOUND = 1;
    public  static final int      FULL_FOUND = 2; // i.e. one or both found but gourp already contains 3 URLS
    public  static final int      NONE_FOUND = 3;
    public  static final int      URL1_FOUND = 4;
    public  static final int      URL2_FOUND = 5;
    public  static final int    WRONG_CHOICE = 0; // for the sake of ?: one-liner below (fun is always good!)

    private static final int DIRECTION_LEFT  = 0;
    private static final int DIRECTION_RIGHT = 1;

    private  static final LinkedHashMap<String, ArrayList<String>> WebGroup_Map = new LinkedHashMap<>();

    private WebGroup() { } // private constructor :: no instance

    // }}}

    // SAVE RESTORE
    // ToCSV {{{
    public static String ToCSV()
    {
        String caller = "ToCSV";
//*WEBGROUP*/Settings.MOC(TAG_WEBGROUP, caller);

        StringBuilder sb = new StringBuilder();

        // PROFILE ARRAY
        int profile_count = 0;
        for(Map.Entry<String, ArrayList<String>> entry : WebGroup_Map.entrySet())
        {
            // GET  KEY
            String entry_profile_key = entry.getKey() ;

            // SAVE KEY
            if(profile_count > 0) sb.append("|");                   // "|" separated [entry_profile_key=(url){2,3}]
            sb.append(entry_profile_key).append(",");

            // COUNT KEY
            profile_count += 1;
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "profile"+profile_count+"::"+entry_profile_key);

            // URL ARRAY
            ArrayList<String> al = entry.getValue();
            for(int url_count = 0;  url_count < al.size(); ++url_count)
            {
                // GET URL
                String url = al.get(url_count);

                // SAVE URL
                if(url_count > 0) sb.append(",");                 // "," separated [(url){2,3}]
                sb.append( url );

//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "url"+(url_count+1)+"::"+url);
            }
        }

        String result = sb.toString();

        return result;
    }
    //}}}
    // FromCSV {{{
    public static void FromCSV(String saved_profiles)
    {
        String caller = "FromCSV("+saved_profiles+")";
//*WEBGROUP*/Settings.MOC(TAG_WEBGROUP, caller);

        String[] profiles = TextUtils.split(saved_profiles, "\\|");  // "|" separated "entry_profile_key,url1,url2[,url3]"

//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "profiles.length="+profiles.length);

        int                                        len = profiles.length;
        for(int profile_count = 0; profile_count < len; ++profile_count)
        {
            // PROFILE_KEY
            String[] csv = profiles[profile_count].split(",");     // "," separated "url1,url2[,url3]"
            if(csv.length < 2) continue;

            String entry_profile_key = csv[0];
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "profile"+profile_count+"::"+entry_profile_key);

            // URL1, URL2 [, URL3]
            ArrayList<String> al = new ArrayList<>();
            int                            csv_len = csv.length;
            for(int url_num = 1; url_num < csv_len; ++url_num)
            {
                String url = csv[url_num];
                al.add( url );

                // INSTANCIATE
                WebGroup_Map.put(entry_profile_key, al);

//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "url"+(url_num)+"::"+url);
            }
        }

    }
    //}}}
    // Dump {{{
    public static String Dump()
    {
        StringBuilder sb = new StringBuilder();
        int profile_count = 0;
        for(Map.Entry<String, ArrayList<String>> entry : WebGroup_Map.entrySet())
        {
            String entry_profile_key = entry.getKey();
            profile_count += 1;
            sb.append(String.format("%d %s\n", profile_count, entry_profile_key) );
            ArrayList<String> al = entry.getValue();
            int              al_size = al.size();
            for(int i=0; i < al_size; ++i)
            {
                String url = al.get(i);
                sb.append(String.format("  %d %s\n", i+1, url) );
            }
        }

        return sb.toString();
    }
    //}}}
    // Dump_profile_key {{{
    public static String Dump_profile_key(String profile_key)
    {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, ArrayList<String>> entry : WebGroup_Map.entrySet())
        {
            String entry_profile_key = entry.getKey();
            if(    entry_profile_key.equals( profile_key ) )
            {
                ArrayList<String> al = entry.getValue();
                int              al_size = al.size();
                sb.append(String.format("...[%s] (x%d URLs):\n", profile_key, al_size));
                for(int i=0; i < al_size; ++i)
                {
                    String url = al.get(i);
                    sb.append(String.format("  %d %s\n", i+1, url) );
                }
                break;
            }
        }
        return sb.toString();
    }
    //}}}

    // ADD DEL
    // add_profile_url_pair {{{
    // Given a [PROFILE] and an [URL], BUILD or UPDATE a [LIST] of 2 or 3 associated URLs
    public static int add_profile_url_pair(String profile, String url1, String url2, String caller)
    {
        caller += "] [add_profile_url_pair("+profile+")";
// MON {{{
//*WEBGROUP*/Settings.MOC(TAG_WEBGROUP, caller);
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "...url1=["+url1+"]");
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "...url2=["+url2+"]");
//}}}
        // search existing groups {{{

        ArrayList<String> al = get_profile_url_ArrayList(profile, url1, url2);

        boolean found_url1 = false;
        boolean found_url2 = false;
        // assert whether each URL is already part of the group
        if(al != null) {
            int              al_size = al.size();
            for(int i=0; i < al_size; ++i)
            {
                String url = al.get(i);
                if(    url.equals(url1) )   found_url1 = true;
                if(    url.equals(url2) )   found_url2 = true;
            }
        }

        int result
            =                 (  found_url1 &&  found_url2) ? BOTH_FOUND
            :                 ((!found_url1 && !found_url2) ? NONE_FOUND
                    :         (( found_url1               ) ? URL1_FOUND
                        :     (( found_url2               ) ? URL2_FOUND
                            :                                 WRONG_CHOICE))); // ...should not happen

        //}}}
        // add the (MISSING URL) to an (EXISTING WEBGROUP) or create a (NEW WEBGROUP) {{{
        switch( result )
        {
            case BOTH_FOUND:                        // both url1 and url2 are already part of an existing group
                break;                              // ...nothing todo

            case URL1_FOUND:                        // only url1 already part of an existing group
                if(al.size() < 3) al.add( url2 );   // ...add [MISSING URL2]
                else  result = FULL_FOUND;          // ...unless there is no room left
                break;

            case URL2_FOUND:                        // only url2 already part of an existing group
                if(al.size() < 3) al.add( url1 );   // ...add [MISSING URL1]
                else  result = FULL_FOUND;          // ...unless there is no room left
                break;

            case NONE_FOUND:                        // none of url1 or url2 is part of an existing group
            default:                                // ...create a [NEW WEBGROUP] i.e. (profile->ArrayList) entry

                // NEW ARRAY [URL{2,3}]
                al = new ArrayList<>();
                al.add( url1 );
                al.add( url2 );

                // NEW ENTRY [PROFILE @ URL{2,3}]
                String profile_key = _get_a_free_profile_key(profile);
                WebGroup_Map.put(profile_key, al);

                break;
        }
        // }}}
// MON {{{
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "............result=["+ result_toString(result) +"] FOR profile=["+profile+"]");
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, ".........LIST SIZE=["+               al.size() +"]");
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "...WEBGROUPS COUNT=["+     WebGroup_Map.size() +"]");
        return result; // ... one of BOTH_FOUND NONE_FOUND URL1_FOUND URL2_FOUND or FULL_FOUND
//}}}
    }
    //}}}
    // del_profile_url {{{
    // Given a [PROFILE] and an [URL], DELETE one from the [LIST] of associated URLs
    public static int del_profile_url(String profile, String url, String caller)
    {
        caller += "] [del_profile_url";
//*WEBGROUP*/Settings.MOC(TAG_WEBGROUP, caller);
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "...url=["+url+"]");

        for(Map.Entry<String, ArrayList<String>> entry : WebGroup_Map.entrySet())
        {
            // SEARCH SELECTED PROFILE GROUPS
            String entry_profile_key = entry.getKey();
            if( entry_profile_key.contains( profile ) )
            {
                ArrayList<String> al = entry.getValue();
                int              al_size = al.size();
                for(int i=0; i < al_size; ++i)
                {
                    // REMOVING THE URL FROM THE CONTAINING [profile_key*]
                    // ...POSSIBLY REMOVING DEGENERATED [profile_key*] (i.e. SINGLE-URL) {{{
                    String alu = al.get(i);
                    if(    alu.equals(url) )
                    {
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "...REMOVING FOUND URL");
                        al.remove( alu );

                        if(al.size() < 2) {
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "...REMOVING DEGENERATED WEBGROUP ENTRY (i.e. size<2)");
                            WebGroup_Map.remove( entry_profile_key );
                        }

//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, ".........LIST SIZE=["+               al.size() +"]");
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "...WEBGROUPS COUNT=["+     WebGroup_Map.size() +"]");
                        return al.size();
                    }
                    //}}}
                }
            }
        }
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "...URL TO DELETE NOT FOUND IN ["+profile+"]");
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "...CURRENT GROUPS COUNT=["+     WebGroup_Map.size() +"]");
        return 0;
    }
    //}}}

    // MOVE
    // shift_profile_url_right {{{
    public static void shift_profile_url_right(String profile, String url, String caller)
    {
        caller += "] [shift_profile_url_right("+profile+", url)";
//*WEBGROUP*/Settings.MOC(TAG_WEBGROUP, caller);
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "...url=["+url+"]");

        _shift_profile_url(profile, url, DIRECTION_RIGHT);
    }
    //}}}
    // shift_profile_url_left {{{
    public static void shift_profile_url_left(String profile, String url, String caller)
    {
        caller += "] [shift_profile_url_left("+profile+", url)";
//*WEBGROUP*/Settings.MOC(TAG_WEBGROUP, caller);
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "...url=["+url+"]");

        _shift_profile_url(profile, url, DIRECTION_LEFT);
    }
    //}}}
    // _shift_profile_url {{{
    private static void _shift_profile_url(String profile, String url, int direction)
    {
        String url1 = null;
        String url2 = null;
        String url3 = null;
        ArrayList<String> al = get_profile_url_ArrayList(profile, url);
        if(al != null)
        {
            // GET THE CURRENT ORDERED LIST
            int              al_size = al.size();
            for(int i=0; i < al_size; ++i) {
                switch(i) {
                    case  0: url1 = al.get(i); break;
                    case  1: url2 = al.get(i); break;
                    case  2: url3 = al.get(i); break;
                    default: url3 = al.get(i); break;
                }
            }

            // DO THE LEFT-RIGHT SWITCHING
            String tmp;
            if     ( TextUtils.equals(url, url1) )  { /* ...CANNOT MOVE LEFT...........*/ { /*.................................*/ } if(  direction == DIRECTION_RIGHT)  { tmp  = url1; url1 = url2; url2 = tmp; } }
            else if( TextUtils.equals(url, url2) )  { if(  direction == DIRECTION_LEFT )  { tmp  = url1; url1 = url2; url2 = tmp; } else            /*DIRECTION_RIGHT*/ { tmp  = url3; url3 = url2; url2 = tmp; } }
            else if( TextUtils.equals(url, url3) )  { if(  direction == DIRECTION_LEFT )  { tmp  = url2; url2 = url3; url3 = tmp; } /*............. CANNOT MOVE RIGHT*/ { /*.................................*/ } }

            // REBUILD THE RE-ORDERED LIST
            al.clear();//al = new ArrayList<>();
            if(url1 != null) al.add( url1 );
            if(url2 != null) al.add( url2 );
            if(url3 != null) al.add( url3 );

//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "...WEBGROUP ENTRIES COUNT=["+ al.size() +"]");
        }
    }
    //}}}

    // SEARCH
    // get_profile_key {{{
    // Given a [PROFILE] and an [URL], look for THE CONTAINING ONE AMONGST [PROFILE[1-N]]
    public static String get_profile_key(String profile_key, String url, String caller)
    {
        caller += "] [get_profile_key("+profile_key+")";
//*WEBGROUP*/Settings.MOC(TAG_WEBGROUP, caller);
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "...url=["+url+"]");

        for(Map.Entry<String, ArrayList<String>> entry : WebGroup_Map.entrySet())
        {
            // SEARCH PROFILE's GROUPS
            String entry_profile_key = entry.getKey();
            if( entry_profile_key.contains( profile_key ) )
            {
                ArrayList<String> al = entry.getValue();
                int              al_size = al.size();
                for(int i=0; i < al_size; ++i)
                {
                    // RETURNING THE [profile_key*] CONTAINING THAT URL {{{
                    String alu = al.get(i);
                    if(    alu.equals(url) )
                    {
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "...FOUND URL IN WEGBROUP["+entry_profile_key+"...] (x"+al.size()+" URLs)");
                        return entry_profile_key; // ...the profile_key of the specified url
                    }
                    //}}}
                }
            }
        }
//*WEBGROUP*/Settings.MOM(TAG_WEBGROUP, "...URL NOT FOUND IN WEGBROUP["+profile_key+"...]");
        return null;
    }
    //}}}
    // get_profile_url_ArrayList .. f(profile) {{{
    // Given a [PROFILE] return the [LIST] of all associated URLs
    public static ArrayList<String> get_profile_url_ArrayList(String profile)
    {
        ArrayList<String> all = new ArrayList<>();
        for(Map.Entry<String, ArrayList<String>> entry : WebGroup_Map.entrySet())
        {
            // search all of this profile groups
            String entry_profile_key = entry.getKey();
            if(    entry_profile_key.contains( profile ) )
            {
                ArrayList<String> al = entry.getValue();
                all.addAll( al );
            }
        }
//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, "get_profile_url_ArrayList("+profile+")", "...return "+all.size()+" URLs");
        return (all.size() > 0) ? all : null;
    }
    //}}}
    // get_profile_url_ArrayList .. f(profile, url ) {{{
    // Given a [PROFILE] and an [URL], return the [LIST] of 2 or 3 associated URLs
    public static ArrayList<String> get_profile_url_ArrayList(String profile, String url)
    {
        return get_profile_url_ArrayList(profile, url, null); // no url2
    }
    //}}}
    // get_profile_url_ArrayList .. f(profile, url1, url2) {{{
    // Given a [PROFILE] and an [URL], return the [LIST] of 2 or 3 associated URLs
    public static ArrayList<String> get_profile_url_ArrayList(String profile, String url1, String url2)
    {
        for(Map.Entry<String, ArrayList<String>> entry : WebGroup_Map.entrySet())
        {
            // search all of this profile groups
            String entry_profile_key = entry.getKey();
            if( entry_profile_key.contains( profile ) )
            {
                // by adding URLs as pairs, an URL cannot be part of more than one group
                // .. i.e. the first group found is the one!
                ArrayList<String> al = entry.getValue();
                int              al_size = al.size();
                for(int i=0; i < al_size; ++i)
                {
                    // RETURNING THE LIST OF THE [profile_key*] CONTAINING [url1] OR [url2] {{{
                    String url = al.get(i);
                    if( TextUtils.equals(url, url1) ) return al;
                    if( TextUtils.equals(url, url2) ) return al;
                    //}}}
                }
            }
        }
        return null;
    }
    //}}}
    // get_group_size {{{
    // Given a [PROFILE] and an [URL], return the size of the [LIST] of 2 or 3 associated URLs
    public static int get_group_size(String profile, String url)
    {
        ArrayList<String> al = WebGroup.get_profile_url_ArrayList(Settings.Working_profile, url);
        int result = (al == null) ? 0 : al.size();

//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, "get_group_size", "...return "+result+": profile=["+profile+"] url=["+url+"]");
        return result;
    }
    //}}}

    // CHECK
    // result_toString {{{
    public static String result_toString(int result)
    {
        switch( result ) {
            case BOTH_FOUND: return   "BOTH_FOUND";
            case FULL_FOUND: return   "FULL_FOUND";
            case NONE_FOUND: return   "NONE_FOUND";
            case URL1_FOUND: return   "URL1_FOUND";
            case URL2_FOUND: return   "URL2_FOUND";
            default:         return "WRONG_CHOICE";
        }
    }
    //}}}
    // _get_a_free_profile_key {{{
    // Given a [PROFILE], return the first yet unused key [PROFILE[1-N]] (...may re-use deleted slots)
    private static String _get_a_free_profile_key(String profile)
    {
        // return one of "profile",  "profile1", ...
        int size  = WebGroup_Map.size();
        int i     = 0;
        String profile_key = profile;
        while((i  < size) && (WebGroup_Map.get(profile_key) != null))
            profile_key    = profile + (++i); // increment suffix

//*WEBGROUP*/Settings.MON(TAG_WEBGROUP, "_get_a_free_profile_key("+profile+")", "...return ["+profile_key+"]");
        return profile_key;
    }
    //}}}

}

/* // {{{

:let @p="WEBGROUP"
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

