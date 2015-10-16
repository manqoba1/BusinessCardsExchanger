package codetribe.sifiso.com.bcelibrary.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.Random;

import codetribe.sifiso.com.bcelibrary.R;

/**
 * Created by sifiso on 2015-09-30.
 */
public class Util {
    static final String LOG = Util.class.getSimpleName();
    static Random random;

    public static Drawable getRandomBackgroundImage(Context ctx) {
        random = new Random(System.currentTimeMillis());
        int index = random.nextInt(7);
        Log.e(LOG, "%%% getRandomBackgroundImage: " + index);
        switch (index) {

            case 0:
                return ContextCompat.getDrawable(ctx,
                        R.drawable.back8);
            case 1:
                return ContextCompat.getDrawable(ctx,
                        R.drawable.back10);
            case 2:
                return ContextCompat.getDrawable(ctx,
                        R.drawable.back6);
            case 3:
                return ContextCompat.getDrawable(ctx,
                        R.drawable.back12);
            case 4:
                return ContextCompat.getDrawable(ctx,
                        R.drawable.back13);
            case 5:
                return ContextCompat.getDrawable(ctx,
                        R.drawable.back14);
            case 6:
                return ContextCompat.getDrawable(ctx,
                        R.drawable.back15);
            default:
                return ContextCompat.getDrawable(ctx,
                        R.drawable.back10);

        }

    }

    public static StringBuilder customSearch(Double latitude, Double longitude, String accessToken, int radius) {
        // int radius = sharedPreferences.getInt("Rad", 30000);
        //Log.w("ADAPTER", radius + "");
        //istagram clientID=acb4a372539e4fdb840382b7d1c3dfd7
        //istagram client secret=893809c6f69147b3a42658ad1d95de22
        /*StringBuilder sb = new StringBuilder(
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");*/
        StringBuilder sb = new StringBuilder(
                "https://api.instagram.com/v1/media/search?");
        sb.append("lat=" + latitude);
        sb.append("&lng=" + longitude);
        if (radius > 0) {
            sb.append("&distance=" + radius);
        }
        sb.append("&access_token=" + accessToken);

        return sb;
    }

    public static StringBuilder customFindByLocationID(int id, String accessToken) {

        StringBuilder sb = new StringBuilder(
                "https://api.instagram.com/v1/locations/");
        sb.append(id + "/media/recent?");
        sb.append("access_token=" + accessToken);
//https://api.instagram.com/v1/locations/{location-id}/media/recent?access_token=ACCESS-TOKEN
        return sb;
    }

    public static void flashOnce(View view, long duration, final UtilAnimationListener listener) {
        ObjectAnimator an = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        an.setRepeatMode(ObjectAnimator.REVERSE);
        an.setDuration(duration);
        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null)
                    listener.onAnimationEnded();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        an.start();

    }

    public static void collapse(final View view, int duration, final UtilAnimationListener listener) {
        int finalHeight = view.getHeight();

        ValueAnimator mAnimator = slideAnimator(view, finalHeight, 0);
        mAnimator.setDuration(duration);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                if (listener != null)
                    listener.onAnimationEnded();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }


    public static void expand(View view, int duration, final UtilAnimationListener listener) {
        view.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(view, 0, (view.getMeasuredWidth()));
        mAnimator.setDuration(duration);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) listener.onAnimationEnded();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();

    }

    public static void expandDown(View view, int duration, final UtilAnimationListener listener) {
        view.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(view, 0, (view.getMeasuredHeight()));
        mAnimator.setDuration(duration);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) listener.onAnimationEnded();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();

    }

    private static ValueAnimator slideAnimator(final View view, int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public interface UtilAnimationListener {
        public void onAnimationEnded();
    }

}
