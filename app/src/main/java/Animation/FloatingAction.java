package Animation;

import android.animation.Animator;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public class FloatingAction implements Animator.AnimatorListener{
    private FloatingActionButton _fabAddUser;
    private final Interpolator interpolador ;
    public FloatingAction(Activity activity, FloatingActionButton fabAddUser){
        _fabAddUser = fabAddUser;
        interpolador = AnimationUtils.loadInterpolator(activity.getBaseContext(),
                android.R.interpolator.fast_out_slow_in);
        show();
    }
    public void show(){
        _fabAddUser.animate()
                .scaleX(1)
                .scaleY(1)
                .setInterpolator(interpolador)
                .setDuration(600)
                .setStartDelay(1000)
                .setListener(this);
    }
    public void hide(){
        _fabAddUser.animate()
                .scaleY(0)
                .scaleX(0)
                .setInterpolator(interpolador)
                .setDuration(600)
                .start();
    }
    @Override
    public void onAnimationStart(Animator animation, boolean isReverse) {

    }

    @Override
    public void onAnimationEnd(Animator animation, boolean isReverse) {
        hide();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
