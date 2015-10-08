package codetribe.sifiso.com.bcelibrary;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import cat.ppicas.customtypeface.CustomTypefaceFactory;

/**
 * Created by sifiso on 2015-09-24.
 */
public class BCEApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        ///CustomTypeface.getInstance().registerTypeface("permanent-marker", typeface);
    }
    public static Typeface getNeutonItalic(Context ctx){
        Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "Neuton-Italic.ttf");
        return typeface;
    }
}
