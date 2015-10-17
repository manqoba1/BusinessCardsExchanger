package codetribe.sifiso.com.bcelibrary.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import codetribe.sifiso.com.bcelibrary.Models.ResponseModel;

/**
 * Created by sifiso on 2015-10-17.
 */
public class LocalStore {
    public interface LocalStoreListener {
        public void onFileDataDeserialized(ResponseModel response);

        public void onDataCached(ResponseModel response);

        public void onError();
    }

    static String LOG = LocalStore.class.getSimpleName();
    private static Context mCtx;
    private static LocalStoreListener localStoreListener;
    public static final int CACHE_DATA = 1, CACHE_BY_ID = 2;
    public static String JSON_DATA = "data.json";
    private static int locationID;
    private static ResponseModel response;
    private static int dataType;

    public static void cacheData(Context context, ResponseModel r, int type, LocalStoreListener cacheUtilListener) {
        dataType = type;
        response = r;
        response.setLastCacheDate(new Date().getTime());
        localStoreListener = cacheUtilListener;
        mCtx = context;
        new CacheTask().execute();
    }

    public static void getCachedData(Context context, int type, LocalStoreListener cacheUtilListener) {
        dataType = type;
        localStoreListener = cacheUtilListener;
        mCtx = context;
        new CacheRetrieveTask().execute();
    }

    public static void locationDataByID(Context context, int id, ResponseModel r, int type, LocalStoreListener cacheUtilListener) {
        dataType = type;
        response = r;
        response.setLastCacheDate(new Date().getTime());
        localStoreListener = cacheUtilListener;
        mCtx = context;
        locationID = id;
        new CacheTask().execute();
    }

    public static void getLocationDataByID(Context context, int id, int type, LocalStoreListener cacheUtilListener) {
        dataType = type;
        localStoreListener = cacheUtilListener;
        mCtx = context;
        locationID = id;
        new CacheRetrieveTask().execute();
    }

    static class CacheTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            String json = null;
            File file = null;
            FileOutputStream outputStream;
            try {
                switch (dataType) {

                    case CACHE_DATA:
                        Log.w(LOG, "## before caching request file, list: " + response.getCaptionModels().size());
                        json = gson.toJson(response);
                        outputStream = mCtx.openFileOutput(JSON_DATA, Context.MODE_PRIVATE);
                        write(outputStream, json);
                        file = mCtx.getFileStreamPath(JSON_DATA);
                        if (file != null) {
                            Log.e(LOG, "Request cache written, path: " + file.getAbsolutePath() +
                                    " - length: " + file.length());
                        }
                        break;

                    case CACHE_BY_ID:
                        Log.w(LOG, "## before caching request file, list: " + response.getCaptionModels().size());
                        json = gson.toJson(response);
                        outputStream = mCtx.openFileOutput("location_data_" + locationID + ".json", Context.MODE_PRIVATE);
                        write(outputStream, json);
                        file = mCtx.getFileStreamPath("location_data_" + locationID + ".json");
                        if (file != null) {
                            Log.e(LOG, "Data cache written, path: " + file.getAbsolutePath() +
                                    " - length: " + file.length());
                        }
                        break;

                    default:
                        Log.e(LOG, "######### NOTHING done ...");
                        break;
                }

            } catch (IOException e) {
                Log.e(LOG, "Failed to cache data", e);
                return 9;
            }
            return 0;
        }

        private void write(FileOutputStream outputStream, String json) throws IOException {
            outputStream.write(json.getBytes());
            outputStream.close();
        }

        @Override
        protected void onPostExecute(Integer v) {
            if (localStoreListener != null) {
                if (v > 0) {
                    localStoreListener.onError();
                } else
                    localStoreListener.onDataCached(response);
            }

        }
    }
    static class CacheRetrieveTask extends AsyncTask<Void, Void, ResponseModel> {

        private ResponseModel getData(FileInputStream stream) throws IOException {
            String json = getStringFromInputStream(stream);
            ResponseModel response = gson.fromJson(json, ResponseModel.class);
            return response;
        }

        @Override
        protected ResponseModel doInBackground(Void... voids) {
            ResponseModel resp = new ResponseModel();
            FileInputStream stream;
            try {
                switch (dataType) {

                    case CACHE_DATA:
                        stream = mCtx.openFileInput(JSON_DATA);
                        resp = getData(stream);
                        Log.i(LOG, "++ caption data cache retrieved");
                        break;
                    case CACHE_BY_ID:
                        stream = mCtx.openFileInput("location_data_" + locationID + ".json");
                        resp = getData(stream);
                        Log.i(LOG, "++ caption location cache retrieved");
                        break;

                }


            } catch (FileNotFoundException e) {
                Log.d(LOG, "#### cache file not found - returning a new response object, type = " + dataType);

            } catch (IOException e) {
                Log.v(LOG, "------------ Failed to retrieve cache", e);
            }
//            resp.setStatusCode(0);
            return resp;
        }

        @Override
        protected void onPostExecute(ResponseModel v) {
            if (localStoreListener == null) return;
            localStoreListener.onFileDataDeserialized(v);
        }
    }

    private static String getStringFromInputStream(InputStream is) throws IOException {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } finally {
            if (br != null) {
                br.close();
            }
        }
        String json = sb.toString();
        return json;

    }

    static final Gson gson = new Gson();
}
