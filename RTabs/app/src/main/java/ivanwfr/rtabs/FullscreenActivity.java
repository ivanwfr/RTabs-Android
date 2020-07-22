package ivanwfr.rtabs; // {{{

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

// }}}
public class FullscreenActivity extends Activity
{
    // LOGGING {{{
    public  static boolean  D = Settings.D;
    public  static void Set_D(boolean state) { D = state; }
    // MONITOR
    private static boolean  M = Settings.M;
    public  static void Set_M(boolean state) { M = state; }

    public static        String FULLSCREENACTIVITY_JAVA_TAG = "(200702:19h:29)";
    // }}}

    // MODEL
    private RTabs mRTabs = null;

    // LIFE CYCLE
    // onCreate {{{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );

        requestWindowFeature( Window.FEATURE_NO_TITLE        );

        getIntent().addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if( mRTabs == null)
            mRTabs = RTabs.getInstance( this );
    }
    //}}}
    // onPostCreate {{{
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super .onPostCreate(savedInstanceState);
        mRTabs.onPostCreate(savedInstanceState);
    }
    //}}}
    // onStart {{{
    @Override protected void onStart()
    {
        super .onStart();
        mRTabs.onStart();
    }
    //}}}
    // onTrimMemory {{{
    @Override public void onTrimMemory(int level)
    {
        super .onTrimMemory(level);
        mRTabs.onTrimMemory(level);
    }
    //}}}
    // onPause {{{
    @Override protected void onPause()
    {
        super .onPause();
        mRTabs.onPause();
    }
    //}}}
    // onSaveInstanceState {{{
    @Override protected void onSaveInstanceState(Bundle outState)
    {
        super .onSaveInstanceState( outState );
        mRTabs.onSaveInstanceState( outState );
    }
    //}}}
    // onRestoreInstanceState {{{
    @Override protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super .onRestoreInstanceState( savedInstanceState );
        mRTabs.onRestoreInstanceState( savedInstanceState );
    }
    //}}}
    // onStop {{{
    @Override protected void onStop()
    {
        super .onStop();
        mRTabs.onStop();
    }
    //}}}
    // onRestart {{{
    @Override protected void onRestart()
    {
        super .onRestart();
        mRTabs.onRestart();
    }
    //}}}
    // onResume {{{
    @Override protected void onResume()
    {
        super .onResume();
        mRTabs.onResume();
    }
    //}}}
    // onDestroy {{{
    @Override protected void onDestroy()
    {
        super .onDestroy();
        mRTabs.onDestroy();
    }
    //}}}

    // CONFIG
    // onConfigurationChanged .. check_orientation {{{
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super .onConfigurationChanged( newConfig );
        mRTabs.onConfigurationChanged( newConfig );
    }
    //}}}

    // KEYBOARD
    //{{{
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent)
    {
        if( mRTabs.onKeyDown(keyCode, keyEvent) ) return true;
        else                                      return super.onKeyDown(keyCode, keyEvent);
    }

    @Override
    public void onBackPressed ()
    {
        mRTabs.onBackPressed();
    }
    //}}}

    // EVENTS
    // dispatchTouchEvent {{{
    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        //try {
            if( mRTabs.dispatchTouchEvent(event) ) return true;
            else                                   return super.dispatchTouchEvent(event);
        //}
        //catch(Exception ex) { System.err.println(ex); ex.printStackTrace(); }
        //return true;
    }
    //}}}

}

/* {{{

***
*** (161111) ISSUE SOLVED WITH: webSettings.setDisplayZoomControls( false );
***
// Friday 11 November 2016:
// FATAL:jni_android.cc(236)
// Fatal signal 6 (SIGABRT), code -6 in tid 5866 (ivanwfr.rtabs)

:!start explorer "http://stackoverflow.com/questions/5125851/enable-disable-zoom-in-android-webview"

W: android.view.WindowManager$BadTokenException:
    Unable to add window
    -- token android.view.ViewRootImpl$W@54cece4 is not valid
    ; is your activity running?

W:     at android.view.ViewRootImpl.setView(ViewRootImpl.java:571)

W:     at android.view.WindowManagerGlobal.addView(WindowManagerGlobal.java:310)

W:     at android.view.WindowManagerImpl.addView(WindowManagerImpl.java:86)

W:     at android.widget.ZoomButtonsController.setVisible(ZoomButtonsController.java:370)

W:     at org.chromium.android_webview.AwZoomControls.invokeZoomPicker(AwZoomControls.java:30)
W:     at org.chromium.content.browser.ContentViewCore.onScrollBeginEventAck(ContentViewCore.java:1145)
W:     at org.chromium.content.browser.ContentViewCore.nativeOnTouchEvent(Native Method)
W:     at org.chromium.content.browser.ContentViewCore.onTouchEventImpl(ContentViewCore.java:1066)
W:     at org.chromium.android_webview.AwContents$AwViewMethodsImpl.onTouchEvent(AwContents.java:43016)

W:     at com.android.webview.chromium.WebViewChromium.onTouchEvent(WebViewChromium.java:41368)
W:     at android.webkit.WebView.onTouchEvent(WebView.java:2378)

W:     at android.view.View.dispatchTouchEvent(View.java:9377)

W:     at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2548)
W:     at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2241)
W:     at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2554)
W:     at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2255)
W:     at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2554)
W:     at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2255)
W:     at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2554)
W:     at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2255)
W:     at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2554)
W:     at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2255)

W:     at com.android.internal.policy.PhoneWindow$DecorView.superDispatchTouchEvent(PhoneWindow.java:2410)
W:     at com.android.internal.policy.PhoneWindow.superDispatchTouchEvent(PhoneWindow.java:1744)

W:     at android.app.Activity.dispatchTouchEvent(Activity.java:2805)

W:     at ivanwfr.rtabs.FullscreenActivity.dispatchTouchEvent(FullscreenActivity.java:148)

W:     at com.android.internal.policy.PhoneWindow$DecorView.dispatchTouchEvent(PhoneWindow.java:2371)
W:     at android.view.View.dispatchPointerEvent(View.java:9597)
W:     at android.view.ViewRootImpl$ViewPostImeInputStage.processPointerEvent(ViewRootImpl.java:4234)
W:     at android.view.ViewRootImpl$ViewPostImeInputStage.onProcess(ViewRootImpl.java:4100)
W:     at android.view.ViewRootImpl$InputStage.deliver(ViewRootImpl.java:3646)
W:     at android.view.ViewRootImpl$InputStage.onDeliverToNext(ViewRootImpl.java:3699)
W:     at android.view.ViewRootImpl$InputStage.forward(ViewRootImpl.java:3665)
W:     at android.view.ViewRootImpl$AsyncInputStage.forward(ViewRootImpl.java:3791)
W:     at android.view.ViewRootImpl$InputStage.apply(ViewRootImpl.java:3673)
W:     at android.view.ViewRootImpl$AsyncInputStage.apply(ViewRootImpl.java:3848)
W:     at android.view.ViewRootImpl$InputStage.deliver(ViewRootImpl.java:3646)
W:     at android.view.ViewRootImpl$InputStage.onDeliverToNext(ViewRootImpl.java:3699)
W:     at android.view.ViewRootImpl$InputStage.forward(ViewRootImpl.java:3665)
W:     at android.view.ViewRootImpl$InputStage.apply(ViewRootImpl.java:3673)
W:     at android.view.ViewRootImpl$InputStage.deliver(ViewRootImpl.java:3646)
W:     at android.view.ViewRootImpl.deliverInputEvent(ViewRootImpl.java:5926)
W:     at android.view.ViewRootImpl.doProcessInputEvents(ViewRootImpl.java:5900)
W:     at android.view.ViewRootImpl.enqueueInputEvent(ViewRootImpl.java:5861)
W:     at android.view.ViewRootImpl$WindowInputEventReceiver.onInputEvent(ViewRootImpl.java:6029)
W:     at android.view.InputEventReceiver.dispatchInputEvent(InputEventReceiver.java:185)
W:     at android.view.InputEventReceiver.nativeConsumeBatchedInputEvents(Native Method)
W:     at android.view.InputEventReceiver.consumeBatchedInputEvents(InputEventReceiver.java:176)
W:     at android.view.ViewRootImpl.doConsumeBatchedInput(ViewRootImpl.java:6000)
W:     at android.view.ViewRootImpl$ConsumeBatchedInputRunnable.run(ViewRootImpl.java:6052)
W:     at android.view.Choreographer$CallbackRecord.run(Choreographer.java:858)
W:     at android.view.Choreographer.doCallbacks(Choreographer.java:670)
W:     at android.view.Choreographer.doFrame(Choreographer.java:600)
W:     at android.view.Choreographer$FrameDisplayEventReceiver.run(Choreographer.java:844)
W:     at android.os.Handler.handleCallback(Handler.java:739)
W:     at android.os.Handler.dispatchMessage(Handler.java:95)
W:     at android.os.Looper.loop(Looper.java:234)
W:     at android.app.ActivityThread.main(ActivityThread.java:5526)
W:     at java.lang.reflect.Method.invoke(Native Method)
W:     at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:726)
W:     at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:616)
A: [FATAL:jni_android.cc(236)] Please include Java exception stack in crash report
W: ### ### ### ### ### ### ### ### ### ### ### ### ###
W: Chrome build fingerprint:
W: 0.161011
W: 25
W: ### ### ### ### ### ### ### ### ### ### ### ### ###
A: Fatal signal 6 (SIGABRT), code -6 in tid 5866 (ivanwfr.rtabs)
}}} */
