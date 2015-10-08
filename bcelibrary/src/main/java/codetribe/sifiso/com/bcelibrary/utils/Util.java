package codetribe.sifiso.com.bcelibrary.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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
}
