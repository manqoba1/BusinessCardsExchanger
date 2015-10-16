package codetribe.sifiso.com.bcelibrary.utils;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import codetribe.sifiso.com.bcelibrary.Models.CaptionModel;
import codetribe.sifiso.com.bcelibrary.Models.LocationModel;

/**
 * Created by sifiso on 2015-10-12.
 */
public class DataUtil {
    public static LocationModel locationModel(JSONObject o) throws JSONException {
        LocationModel dto = new LocationModel();
        dto.id = o.getInt("id");
        dto.latitude = o.getDouble("latitude");
        dto.longitude = o.getDouble("longitude");
        dto.name = (o.getString("name"));

        return dto;
    }

    static String LOG = DataUtil.class.getSimpleName();

    public static CaptionModel captionModel(JSONObject o) throws JSONException {
        CaptionModel dto = null;
        Log.d(LOG, new Gson().toJson(o));
        dto = new CaptionModel();
        if(o.optJSONObject("caption") != null) {
            dto.createdTime = o.optJSONObject("caption").optLong("created_time");
            dto.textMessage = o.optJSONObject("caption").optString("text");
        }
        dto.id = o.optInt("id");

        dto.imageUrlLow = o.optJSONObject("images").optJSONObject("low_resolution").optString("url");
        dto.imageUrlThumb = o.optJSONObject("images").optJSONObject("thumbnail").optString("url");
        dto.imageUrlStnd = o.optJSONObject("images").optJSONObject("standard_resolution").optString("url");

        dto.locationID = o.optJSONObject("location").optInt("id");
        dto.fullName = o.optJSONObject("location").optString("name");
        dto.longitude = o.optJSONObject("location").optDouble("longitude");
        dto.latitude = o.optJSONObject("location").optDouble("latitude");

        return dto;
    }
}
