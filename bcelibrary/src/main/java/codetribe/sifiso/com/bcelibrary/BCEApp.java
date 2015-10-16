package codetribe.sifiso.com.bcelibrary;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import codetribe.sifiso.com.bcelibrary.toolbox.BitmapLruCache;


/**
 * Created by sifiso on 2015-09-24.
 */
public class BCEApp extends Application {
    static String LOG = BCEApp.class.getSimpleName();
    RequestQueue requestQueue;
    BitmapLruCache bitmapLruCache;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeVolley(getApplicationContext());
        DisplayImageOptions defaultOptions =
                new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .showImageOnFail(getApplicationContext().getResources().getDrawable(R.drawable.back10))
                        .showImageOnLoading(getApplicationContext().getResources().getDrawable(R.drawable.back10))
                        .build();

        File cacheDir = StorageUtils.getCacheDirectory(this, true);
        if (cacheDir != null) {
            StorageUtils.getCacheDirectory(this, true).deleteOnExit();
        }
        Log.d(LOG, "## onCreate, ImageLoader cacheDir, files: " + cacheDir.listFiles().length);
        //
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .denyCacheImageMultipleSizesInMemory()
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .memoryCache(new LruMemoryCache(16 * 1024 * 1024))
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        if (ImageLoader.getInstance() == null) {
            ImageLoader.getInstance().init(config);
            L.writeDebugLogs(false);
            L.writeLogs(false);

            Log.w(LOG, "###### ImageLoaderConfiguration has been initialised");
        }
    }

    /**
     * Set up Volley Networking; create RequestQueue and ImageLoader
     *
     * @param context
     */
    public void initializeVolley(Context context) {
        Log.e(LOG, "initializing Volley Networking ...");
        requestQueue = Volley.newRequestQueue(context);
        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();

        // Use 1/8th of the available memory for this memory cache.
        int cacheSize = 1024 * 1024 * memClass / 8;
        bitmapLruCache = new BitmapLruCache(cacheSize);
        // imageLoader = new ImageLoader(requestQueue, bitmapLruCache);
        Log.i(LOG, "********** Yebo! Volley Networking has been initialized, cache size: " + (cacheSize / 1024) + " KB");

        // Create global configuration and initialize ImageLoader with this configuration
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        ImageLoader.getInstance().init(config);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
    public static Typeface getNeutonItalic(Context ctx) {
        Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "Neuton-Italic.ttf");
        return typeface;
    }
}
