package ivanwfr.rtabs; // {{{

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Property;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

// }}}

public class Magnet
{
    // Settings {{{
    private static       String TAG_MAGNET          = Settings.TAG_MAGNET;

    private Settings.MagnetListener magnetListener   = null; // to report to about:
    //..................................................... // - [magnet_1_progress]
    //..................................................... // - [magnet_2_tracked ]
    //..................................................... // - [magnet_3_reached ]
    //..................................................... // - [magnet_4_checked ]
    //}}}
    // Members [progress] [mAnimatorSet] {{{
    private       float progress     =    0;
    private AnimatorSet mAnimatorSet = null;

    //}}}
    // Constructor {{{
    public Magnet(Settings.MagnetListener l)
    {
        magnetListener = l;
    }
    //}}}
    // animate {{{
    public  void animateOvershoot(int duration) { _animate(duration,  true); }
    public  void animate         (int duration) { _animate(duration, false); }
    private void _animate(int duration, boolean overshoot)
    {
        // (mAnimatorSet cancel) {{{
//*MAGNET*/Settings.MOC(TAG_MAGNET, "_animate("+duration+", overshoot=["+overshoot+"])");

        if(mAnimatorSet != null)
            mAnimatorSet.cancel();

        //}}}
        // [mAnimatorSet] {{{
        if( !RTabs.ANIM_SUPPORTED ) return;

        mAnimatorSet = new AnimatorSet();

        mAnimatorSet.setDuration( duration );

        if( overshoot ) mAnimatorSet.setInterpolator( new OvershootInterpolator () );
        else            mAnimatorSet.setInterpolator( new DecelerateInterpolator() );

        mAnimatorSet.addListener( anim_endListener );

      //mAnimatorSet.play( ObjectAnimator.ofFloat(this, MAGNET_PROGRESS_PROPERTY, 0f, 1f) );

        progress = 0f; // (reported by property.get)
        mAnimatorSet.play( ObjectAnimator.ofFloat(this, MAGNET_PROGRESS_PROPERTY,     1f) );

        mAnimatorSet.start();
        //}}}
    }
    // }}}
    // is_animating {{{
    public boolean is_animating()
    {
        return (mAnimatorSet != null);
    }
    //}}}
    // cancel_animation {{{
    public void cancel_animation()
    {
        if(mAnimatorSet == null) return;

//*MAGNET*/Settings.MOC(TAG_MAGNET, "cancel_animation");
        mAnimatorSet.cancel();
    }
    //}}}
    // anim_endListener {{{
    private AnimatorListenerAdapter anim_endListener = new AnimatorListenerAdapter()
    {
        // onAnimationEnd {{{
        @Override public void onAnimationEnd(Animator animation)
        {
            // ANIMATION DONE RUNNING FLAG .. (mAnimatorSet = null) {{{
//*MAGNET*/   String caller = "onAnimationEnd";//TAG_MAGNET

            if(mAnimatorSet == null) return;

            mAnimatorSet = null;

            //}}}
            // [TRACKED] [REACHED] [CHECKED] {{{
//*MAGNET*/Settings.MOC(TAG_MAGNET, caller, "CALLING LISTENER (freezed->tracked->reached->checked)");
            if(        !magnetListener.magnet_freezed  () ) // 1 [PROGRESS]
                if(     magnetListener.magnet_2_tracked() ) // 2 [TRACKED]
                    if( magnetListener.magnet_3_reached() ) // 3 [REACHED]
                        magnetListener.magnet_4_checked() ; // 4 [SETTLED]

            //}}}
        }
        // }}}
        // onAnimationCancel {{{
        @Override public void onAnimationCancel(Animator animation)
        {
//*MAGNET*/Settings.MOC(TAG_MAGNET, "onAnimationCancel");

            mAnimatorSet = null; // (as a cancel flag)

            magnetListener.magnet_1_progress(1f); // (jump to conclusion)
        }
        //}}}
    };
    //}}}
    //{{{
    private Property<Magnet, Float> MAGNET_PROGRESS_PROPERTY =
        new Property<Magnet, Float>(Float.class, "Magnet")
        {
            @Override public void  set(Magnet m, Float progress)
            {
//*MAGNET*/Settings.MOM(TAG_MAGNET, "MAGNET_PROGRESS_PROPERTY.set("+progress+")");

                magnetListener.magnet_1_progress( progress );

                m.progress = progress;
            }
            @Override public Float get(Magnet m)
            {
//*MAGNET*/Settings.MOM(TAG_MAGNET, "MAGNET_PROGRESS_PROPERTY.get: return "+m.progress);
                return m.progress;
            }
        };
    //}}}
}

/* // {{{

:let @p="MAGNET"

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

