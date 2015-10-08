package codetribe.sifiso.com.bcelibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sifiso on 2015-09-24.
 */
public class SharedUtil {

    public static final String PREF_DRAWER_FILE = "drawerpref";
    public static final String KEY_FIRST_TIME_DRAWER_SHOW
            = "first_time_drawer_Show";
    public static void saveToPreference(Context context,
                                        String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(PREF_DRAWER_FILE,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context
            , String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_DRAWER_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }
}
