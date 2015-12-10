package codetribe.sifiso.com.bcelibrary.utils;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import codetribe.sifiso.com.bcelibrary.Models.CaptionModel;
import codetribe.sifiso.com.bcelibrary.Models.CategoriesModel;
import codetribe.sifiso.com.bcelibrary.Models.LocationModel;
import codetribe.sifiso.com.bcelibrary.Models.SpecialItemModel;
import codetribe.sifiso.com.bcelibrary.Models.VenueModel;

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

    public static List<SpecialItemModel> specialItemModel(JSONObject o) {


        // System.out.println(o.optJSONObject("specials").optJSONArray("items"));
        List<SpecialItemModel> list = new ArrayList<>();
        JSONArray jsonArray = o.optJSONObject("specials").optJSONArray("items");

        for (int i = 0; i < jsonArray.length(); ++i) {
            // System.out.println(new Gson().toJson(i));
            JSONObject jo = jsonArray.optJSONObject(i);
            SpecialItemModel dto = new SpecialItemModel();
            dto.itemID = jo.optInt("id");
            dto.type = jo.optString("type");
            dto.title = jo.optString("title");
            dto.description = jo.optString("description");
            dto.message = jo.optString("message");
            dto.finePrint = jo.optString("finePrint");
            dto.entryUrl = jo.optJSONObject("interaction").optString("entryUrl");
            dto.venueModel = venueModel(jo.optJSONObject("venue"));
            if (jo.optJSONObject("photo") != null) {
                String size = jo.optJSONObject("photo").optString("width") + "x" + jo.optJSONObject("photo").optString("height");
                dto.height = jo.optJSONObject("photo").optString("height");
                dto.width = jo.optJSONObject("photo").optString("width");
                dto.icon = jo.optJSONObject("photo").optString("prefix") + size + jo.optJSONObject("photo").optString("suffix");
            }

            System.out.println(new Gson().toJson(dto.icon));
            list.add(dto);
        }
        return list;
    }

    static String LOG = DataUtil.class.getSimpleName();

    private static VenueModel venueModel(JSONObject o) {
        VenueModel dto = new VenueModel();
        dto.venueID = o.optInt("id");
        dto.venueName = o.optString("name");
        dto.formattedPhone = o.optJSONObject("contact").optString("formattedPhone");
        dto.url = o.optString("url");
        dto.checkinsCount = o.optJSONObject("stats").optInt("checkinsCount");
        dto.usersCount = o.optJSONObject("stats").optInt("usersCount");
        dto.tipCount = o.optJSONObject("stats").optInt("tipCount");
        //dto.venuePageID = o.optJSONObject("venuePage").optInt("id");

        LocationModel model = new LocationModel();
        model.address = o.optJSONObject("location").optString("address");
        model.crossStreet = o.optJSONObject("location").optString("crossStreet");
        model.latitude = o.optJSONObject("location").optDouble("lat");
        model.longitude = o.optJSONObject("location").optDouble("lng");

        model.distance = o.optJSONObject("location").optDouble("distance");
        model.postalCode = o.optJSONObject("location").optString("postalCode");
        model.cc = o.optJSONObject("location").optString("cc");
        model.city = o.optJSONObject("location").optString("city");

        JSONArray js = o.optJSONArray("categories");
        List<CategoriesModel> list = new ArrayList<>();
        for (int i = 0; i < js.length(); i++) {
            JSONObject jo = js.optJSONObject(i);
            CategoriesModel model1 = new CategoriesModel();
            model1.categoriesID = jo.optInt("id");
            model1.name = jo.optString("name");
            model1.pluralName = jo.optString("pluralName");
            model1.pluralName = jo.optString("shortName");
            model1.icon = jo.optJSONObject("icon").optString("prefix") + jo.optJSONObject("icon").optString("suffix");

            list.add(model1);
        }
        dto.categoriesModels = list;
        dto.locationModel = model;

        return dto;
    }

    public static CaptionModel captionModel(JSONObject o) throws JSONException {
        CaptionModel dto = null;
        Log.d(LOG, new Gson().toJson(o));
        dto = new CaptionModel();
        if (o.optJSONObject("caption") != null) {
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
