package ivanwfr.rtabs.sandbox; // {{{

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import ivanwfr.rtabs.Settings;
import ivanwfr.rtabs.sandbox.widget.RotateZoomImageView;

// }}}
public class TouchView extends RelativeLayout
{
    // {{{
    private static       String TAG_SANDBOX    = Settings.TAG_SANDBOX;
    private static final String IMG_FILE_NAME = "DEV/images/screen_size.png";

    //}}}
    // Constructors {{{
    public TouchView(Context context) { super(context); _init(context); }

    // _init {{{
    private void _init(Context context)
    {
        setId( View.generateViewId() );

        // BACKGROUND
        setBackgroundColor( Color.TRANSPARENT );

        // LAYOUT
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(rlp);
    }
    //}}}

    //}}}

    // start_sample {{{

    public void start_sample(String sandBoxType)
    {
/*SANDBOX*/Settings.MOM(TAG_SANDBOX, "TouchView.start_sample("+sandBoxType+")");

        switch( sandBoxType )
        {
//          case SandBox.DELEGATE   : /* startActivity(new Intent(this, TouchDelegateActivity            .class)); */ break; // Touch Delegate
//          case SandBox.FORWARD    : /* startActivity(new Intent(this, TouchForwardActivity             .class)); */ break; // Touch Forwarding
//          case SandBox.INTERCEPT  : /* startActivity(new Intent(this, TouchInterceptActivity           .class)); */ break; // Disable Touch Intercept
//          case SandBox.LOGGER     : /* startActivity(new Intent(this, MoveLoggerActivity               .class)); */ break; // Move Logger View
//          case SandBox.PAN        : /* startActivity(new Intent(this, TwoDimensionScrollActivity       .class)); */ break; // PAN 2D Scrolling
//          case SandBox.PANGESTURE : /* startActivity(new Intent(this, TwoDimensionGestureScrollActivity.class)); */ break; // PAN 2D GestureDetector Scrolling
            case SandBox.ROTATE     :    showMultitouchSample();                                                      break; // Multi-Touch Image View
            case SandBox.TOUCH      :    showTouchListenerSample();                                                   break; // Touch Listener
        }
    }
    //}}}
    // showTouchListenerSample {{{
    private TouchListenerSample mTouchListenerSample;
    private void showTouchListenerSample()
    {
        if(mTouchListenerSample == null) {
            mTouchListenerSample = new TouchListenerSample(this.getContext());
            addView( mTouchListenerSample );
        }
        hideAllBut( mTouchListenerSample );
    }
    //}}}
    // showMultitouchSample {{{
    private MultitouchSample mMultitouchSample;
    private void showMultitouchSample()
    {
/*SANDBOX*/Settings.MOM(TAG_SANDBOX, "TouchView.showMultitouchSample");
        if(mMultitouchSample == null) {
            mMultitouchSample = new MultitouchSample(this.getContext());
            addView( mMultitouchSample );
        }
        mMultitouchSample.loadBitmap();
        hideAllBut( mMultitouchSample );
    }
    //}}}
    private void hideAllBut(View vis_view) // {{{
    {
        if(vis_view  == mTouchListenerSample) mTouchListenerSample.setVisibility( View.  VISIBLE );
        else if(null != mTouchListenerSample) mTouchListenerSample.setVisibility( View.INVISIBLE );

        if(vis_view  == mMultitouchSample   ) mMultitouchSample   .setVisibility( View.  VISIBLE );
        else if(null != mMultitouchSample   ) mMultitouchSample   .setVisibility( View.INVISIBLE );
    }
    // }}}

    // handle_MEMORY {{{
    public void handle_MEMORY()
    {
/*SANDBOX*/Settings.MOC(TAG_SANDBOX, "TouchView.handle_MEMORY");
        if(mMultitouchSample != null) mMultitouchSample.handle_MEMORY();
    }
    //}}}

    // TouchListenerSample {{{
    // {{{
    private static final int HANDLE_EVENTS_COLOR = Color.parseColor("#ccffffff");
    private static final int IGNORE_EVENTS_COLOR = Color.parseColor("#44000000");

    // }}}
    public class TouchListenerSample extends RelativeLayout implements View.OnTouchListener
    {
        // {{{
        /* Views to display last seen touch event */
        private CheckBox mCheckBox;
        private TextView mTextView;
        private    Point moveFromPoint;

        // }}}
        // Constructor {{{
        public TouchListenerSample(Context context) { super(context); _init(context); }

        private void _init(Context context)
        {
            // CONTAINER {{{
            setId( View.generateViewId() );
            this.setBackgroundColor( HANDLE_EVENTS_COLOR );

            // layout
            RelativeLayout.LayoutParams rlp;
            rlp        = new RelativeLayout.LayoutParams(1,1);
            rlp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
            rlp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            this.setLayoutParams(rlp);

            //}}}
            // mTextView {{{
            mTextView = new TextView(context);
            mTextView.setId( View.generateViewId() );

            // layout
            rlp        = new RelativeLayout.LayoutParams(1,1);
            rlp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
            rlp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mTextView.setLayoutParams( rlp );
            mTextView.setTextSize(72F);

            addView( mTextView );
            //}}}
            // mCheckBox {{{
            mCheckBox = new CheckBox(context);
            mCheckBox.setId( View.generateViewId() );
            mCheckBox.setBackgroundColor( IGNORE_EVENTS_COLOR);
            mCheckBox.setChecked( true );
            mCheckBox.setOnCheckedChangeListener(mCheckBox_OnCheckedChangeListener);

            // layout
            rlp        = new RelativeLayout.LayoutParams(1,1);
            rlp.addRule( RelativeLayout.ALIGN_PARENT_BOTTOM );
            rlp.addRule( RelativeLayout.ALIGN_PARENT_RIGHT  );
            rlp.width  = 256;
            rlp.height = 256;
            mCheckBox.setLayoutParams( rlp );

            addView( mCheckBox );
            //}}}
            setOnTouchListener(this);
            moveFromPoint = new Point();
        }
        //}}}
        // mCheckBox_OnCheckedChangeListener {{{
        private final CompoundButton.OnCheckedChangeListener mCheckBox_OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                syncBackGroundColors( isChecked );
            }
        };
        //}}}
        // syncBackGroundColors {{{
        private void syncBackGroundColors(boolean isChecked)
        {
            this     .setBackgroundColor(isChecked ? HANDLE_EVENTS_COLOR : IGNORE_EVENTS_COLOR);
            mCheckBox.setBackgroundColor(isChecked ? IGNORE_EVENTS_COLOR : HANDLE_EVENTS_COLOR);
        }
        //}}}
        // onTouch {{{
        @Override public boolean onTouch(View view, MotionEvent event)
        {
            // {{{
            int         action = event.getAction();
            int              x = (int)event.getX();
            int              y = (int)event.getY();
            String action_name = Settings.Get_action_name( event );
            String     details = "";

            //}}}
            // [PointerCount] [PointerId] {{{
            int pointerCount = event.getPointerCount();
            for(int i=0; i< pointerCount; ++i)
            {
                int pointerId = event.getPointerId(i);
                details += "Id["+i+"]: "+pointerId+"\n";
            }
            //}}}
            if     (action == MotionEvent.ACTION_DOWN) // {{{
            {
                moveFromPoint.set(x, y);
                mTextView.setText( String.format("%s\n[%4d @ %4d]", action_name, x, y));
                mTextView.setText( String.format("%s\n[%4d @ %4d]", action_name, x, y));
            }
            // }}}
            else if(action == MotionEvent.ACTION_MOVE) // {{{
            {
                // IGNORE SLOPPY MOVES
                int dx   = Math.abs(x - moveFromPoint.x);
                int dy   = Math.abs(y - moveFromPoint.y);
                if (       dx < Settings.SCALED_TOUCH_SLOP
                        && dy < Settings.SCALED_TOUCH_SLOP
                   )
                    return mCheckBox.isChecked();

                details += String.format("SLOP=[%2d] dx=[%2d] dy=[%2d]\n", Settings.SCALED_TOUCH_SLOP, dx, dy);
                // Report move .. (and sense next from here)
                moveFromPoint.set(x, y);
            }
            // }}}
            // Report event
            mTextView.setText( String.format("%s\n[%4d @ %4d]\n%s", action_name, x, y, details));
            // Consume event .. f(CheckBox)
            return mCheckBox.isChecked();
        }
        //}}}
    }
    //}}}
    // MultitouchSample {{{

    public class MultitouchSample extends RelativeLayout
    {
        private RotateZoomImageView mRotateZoomImageView = null;
        private              Bitmap mBitmap              = null;
        // Constructor {{{
        public MultitouchSample(Context context) { super(context); _init(context); }

        private void _init(Context context)
        {
            // CONTAINER {{{
            setId( View.generateViewId() );
            this.setBackgroundColor( HANDLE_EVENTS_COLOR );

            // layout
            RelativeLayout.LayoutParams rlp;
            rlp        = new RelativeLayout.LayoutParams(1,1);
            rlp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
            rlp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            this.setLayoutParams(rlp);

            //}}}
            // mRotateZoomImageView {{{
            mRotateZoomImageView = new RotateZoomImageView(context);
            mRotateZoomImageView.setId( View.generateViewId() );

            loadBitmap();

            // layout
            rlp        = new RelativeLayout.LayoutParams(1,1);
            rlp.width  = ViewGroup.LayoutParams.MATCH_PARENT;
            rlp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mRotateZoomImageView.setLayoutParams( rlp );

            addView( mRotateZoomImageView );
            //}}}
        }
        //}}}
        // loadBitmap {{{
        private void loadBitmap()
        {
            if(mBitmap != null) return;

            // Bitmap (from file)
            String dir_path = Settings.Get_Profiles_dir().getPath();
            String img_path = dir_path +"/"+IMG_FILE_NAME;
/*SANDBOX*/Settings.MOM(TAG_SANDBOX, "getBitmap: img_path=["+img_path+"]");

            mBitmap = null;
            try {
                File      file = new File( img_path );
                InputStream is = new BufferedInputStream(new FileInputStream(file));
                mBitmap        = BitmapFactory.decodeStream(is);
                is.close();
            }
            catch (IOException e) {
                System.err.println( e.getMessage() );
            }
            mRotateZoomImageView.setImageBitmap( mBitmap );
        }
        //}}}
        // handle_MEMORY {{{
        public void handle_MEMORY()
        {
/*SANDBOX*/Settings.MOC(TAG_SANDBOX, "TouchView.handle_MEMORY");
            mRotateZoomImageView.setImageBitmap( null );
            mBitmap = null;
        }
        //}}}
    }
    //}}}

//// MoveLoggerActivity.java {{{
//public class MoveLoggerActivity extends Activity implements View.OnTouchListener
//{
// // {{{
//    public static final String TAG = "MoveLoggerActivity";
//
//    /* Slop constant for this device */
//    /* Initial touch point */
//    private Point touchPoint;
//
//// }}}
//    // onCreate {{{
//    @Override protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.move_logger);
//
//        findViewById(R.id.view_logall).setOnTouchListener(this);
//        findViewById(R.id.view_logslop).setOnTouchListener(this);
//
//        touchPoint = new Point();
//    }
//    //}}}
//    // onTouch {{{
//    @Override public boolean onTouch(View v, MotionEvent event)
//    {
//        if(event.getAction() == MotionEvent.ACTION_DOWN)
//        {
//            touchPoint.set((int)event.getX(), (int)event.getY());
//            return true; // Must declare interest to get more events
//        }
//        else if (event.getAction() == MotionEvent.ACTION_MOVE)
//        {
//            switch (v.getId())
//            {
//                case R.id.view_logall:
//                    Log.i(TAG, String.format("Top Move: %.1f,%.1f", event.getX(), event.getY()));
//                    break;
//
//                case R.id.view_logslop:
//                    if (       Math.abs(event.getX() - touchPoint.x) > Settings.SCALED_TOUCH_SLOP
//                            || Math.abs(event.getY() - touchPoint.y) > Settings.SCALED_TOUCH_SLOP
//                       ) {
//                        Log.i(TAG, String.format("Bottom Move: %.1f,%.1f", event.getX(), event.getY()));
//                       }
//                    break;
//
//                default:
//                    break;
//            }
//        }
//        return false; // Don't interefere when not necessary
//    }
//    //}}}
//}
//
////}}}
//// TouchDelegateActivity.java {{{
// // {{{
//
//
//
//
//// }}}
//public class TouchDelegateActivity extends Activity
//{
//    // onCreate {{{
//    @Override protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//
//        // ivanwfr.rtabs.sandbox.widget.TouchDelegateLayout;
//        TouchDelegateLayout layout = new TouchDelegateLayout(this);
//        setContentView(layout);
//    }
//    //}}}
//}
//
////}}}
//// TouchForwardActivity.java {{{
//public class TouchForwardActivity extends Activity
//{
//    // onCreate {{{
//    @Override protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate( savedInstanceState );
//
//        Button button = new Button(this);
//        button.setText("You Can't Miss Me!");
//
//        TouchForwardLayout layout = new TouchForwardLayout(this);
//
//        FrameLayout.LayoutParams flp
//            = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
//        layout.addView(button, flp);
//
//        setContentView(layout);
//    }
//    //}}}
//}
//
////}}}
//// TouchInterceptActivity.java {{{
//// {{{
//
//
///**
// * Created by Dave Smith
// * Double Encore, Inc.
// * Date: 1/19/13
// * TouchInterceptActivity
// */
//// }}}
//public class TouchInterceptActivity extends Activity implements ViewPager.OnPageChangeListener
//{
//    // {{{
//    private ViewPager mViewPager;
//    private ListView  mListView;
//
//    // }}}
//    // onCreate {{{
//    public void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.touch_intercept);
//
//        //ViewPager
//        mViewPager = new ViewPager(this);
//        mViewPager.setLayoutParams(
//                new ListView.LayoutParams( ListView.LayoutParams.MATCH_PARENT
//                    ,                      getResources().getDimensionPixelSize( R.dimen.header_height ));
//
//                // ListView
//                mListView = (ListView) findViewById(R.id.list);
//                mListView.addHeaderView( mViewPager );
//                mListView.setAdapter(new ItemsAdapter(this));
//
//                // ViewPager.OnPageChangeListener
//                mViewPager.setOnPageChangeListener(this);
//                mViewPager.setAdapter(new HeaderAdapter(this));
//    }
//    //}}}
//    // onPageScrolled onPageSelected onPageScrollStateChanged {{{
//    @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
//
//    @Override public void onPageSelected(int position) { }
//
//    @Override public void onPageScrollStateChanged(int state)
//    {
//        // While the ViewPager is scrolling,
//        // ...disable the ScrollView touch intercept
//        // ...so it cannot take over
//        // ...and try to vertical scroll
//        boolean isScrolling
//            = (state != ViewPager.SCROLL_STATE_IDLE);
//
//        mListView.requestDisallowInterceptTouchEvent( isScrolling );
//    }
//    //}}}
//
//    private static class ItemsAdapter extends BaseAdapter //{{{
//    {
//        // Simple ListAdapter that draws a series of row items
//
//        private LayoutInflater mInflater;
//
//        public ItemsAdapter(Context context)
//        {
//            mInflater = LayoutInflater.from(context);
//        }
//
//        @Override public    int getCount ()             { return   25; }
//
//        @Override public Object getItem  (int position) { return null; }
//
//        @Override public   long getItemId(int position) { return    0; }
//
//        @Override public   View getView  (int position, View convertView, ViewGroup parent)
//        {
//            if( convertView == null)
//                convertView = mInflater.inflate(R.layout.intercept_row, parent, false);
//
//            TextView v = (TextView) convertView.findViewById( R.id.text );
//
//            v.setText(String.format("Item Row %2d", position+1));
//
//            return convertView;
//        }
//    }
//    // }}}
//    private static class HeaderAdapter extends PagerAdapter // {{{
//    {
//        // Simple PagerAdapter that just draws a small group of colored views.
//        // {{{
//        private static final int[] COLORS = { 0xFF555500, 0xFF770077, 0xFF007777, 0xFF777777 };
//
//        private Context mContext;
//        // }}}
//        // HeaderAdapter {{{
//        public HeaderAdapter(Context context)
//        {
//            mContext = context;
//        }
//        //}}}
//        // instantiateItem {{{
//        @Override public Object instantiateItem(ViewGroup container, int position)
//        {
//            TextView v = new TextView(mContext);
//            v.setBackgroundColor(COLORS[position]);
//            v.setText(String.format("Header Card %d", position + 1));
//            v.setGravity(Gravity.CENTER);
//            container.addView(v);
//
//            return v;
//        }
//        //}}}
//        // destroyItem {{{
//        @Override public void destroyItem(ViewGroup container, int position, Object object)
//        {
//            container.removeView((View) object);
//        }
//        //}}}
//        // getCount {{{
//        @Override public int getCount()
//        {
//            return COLORS.length;
//        }
//        //}}}
//        // isViewFromObject {{{
//        @Override public boolean isViewFromObject(View view, Object object)
//        {
//            return (view == object);
//        }
//        //}}}
//    }
//    // }}}
//}
//
////}}}
//// TwoDimensionGestureScrollActivity.java {{{
//public class TwoDimensionGestureScrollActivity extends Activity
//{
//    // onCreate {{{
//    @Override protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//
//        setContentView( R.layout.gesture_scroll );
//
//        // Bitmap (from file)
//        Bitmap bitmap = null;
//        try {
//            InputStream in = getAssets().open("android.jpg");
//            bitmap         = BitmapFactory.decodeStream(in);
//            in.close();
//        }
//        catch (IOException e) {
//            System.err.println( e.getMessage() );
//        }
//
//
//        ImageView iv = (ImageView) findViewById(R.id.imageView);
//        iv.setImageBitmap( bitmap );
//
//    }
//    //}}}
//}
//
////}}}
//// TwoDimensionScrollActivity.java {{{
//public class TwoDimensionScrollActivity extends Activity
//{
//    // onCreate {{{
//    @Override protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//
//        TwoDimensionScrollView scrollView = new TwoDimensionScrollView(this);
//
//        LinearLayout layout = new LinearLayout(this);
//        layout.setOrientation(LinearLayout.VERTICAL);
//
//        for(int i=0; i<5; i++)
//        {
//            ImageButton iv = new ImageButton(this);
//            iv.setImageResource( R.drawable.ic_launcher         );
//            iv.setScaleType    ( ImageView.ScaleType.FIT_CENTER );
//
//            int  width = getResources().getDimensionPixelSize( R.dimen.pan_content_width  );
//            int height = getResources().getDimensionPixelSize( R.dimen.pan_content_height );
//
//            layout.addView(iv, new LinearLayout.LayoutParams(width, height));
//        }
//
//        scrollView.addView( layout );
//        setContentView( scrollView );
//    }
//    //}}}
//}
//
////}}}

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

