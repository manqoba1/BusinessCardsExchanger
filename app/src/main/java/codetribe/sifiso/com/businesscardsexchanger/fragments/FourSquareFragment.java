package codetribe.sifiso.com.businesscardsexchanger.fragments;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import net.londatiga.android.foursquare.FoursquareApp;
import net.londatiga.android.foursquare.FoursquareSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import codetribe.sifiso.com.bcelibrary.Adapters.ShopSpecialAdapter;
import codetribe.sifiso.com.bcelibrary.Models.LocationModel;
import codetribe.sifiso.com.bcelibrary.Models.SpecialItemModel;
import codetribe.sifiso.com.bcelibrary.toolbox.BaseVolley;
import codetribe.sifiso.com.bcelibrary.utils.Constants;
import codetribe.sifiso.com.bcelibrary.utils.DataUtil;
import codetribe.sifiso.com.bcelibrary.utils.GPSTracker;
import codetribe.sifiso.com.bcelibrary.utils.SpacesItemDecoration;
import codetribe.sifiso.com.bcelibrary.utils.Util;
import codetribe.sifiso.com.businesscardsexchanger.MainActivity;
import codetribe.sifiso.com.businesscardsexchanger.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FourSquareFragment extends Fragment {

    private View view;
    FoursquareApp foursquareApp;
    Context mCtx;
    ShopSpecialAdapter specialAdapter;
    Location mLocation;
    GPSTracker gpsTracker;
    String accessToken;
    List<SpecialItemModel> mSpecialList;
    RecyclerView lsCards;

    public FourSquareFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void setField() {
        gpsTracker = new GPSTracker(mCtx, new GPSTracker.GPSTrackerListener() {
            @Override
            public void onLocationFound(Location location) {
                mLocation = location;
                getDataByRadius(120);

            }
        });
        lsCards = (RecyclerView) view.findViewById(R.id.lsCards);
        lsCards.setLayoutManager(new LinearLayoutManager(mCtx));
        lsCards.addItemDecoration(new SpacesItemDecoration(4));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mCtx = getActivity();
        foursquareApp = new FoursquareApp(mCtx, Constants.FOURSQUARE_CLIENT_ID, Constants.FOURSQUARE_CLIENT_SECRET);

        if (foursquareApp.hasAccessToken()) {
            view = inflater.inflate(R.layout.fragment_four_square, container, false);
            foursquareApp.setListener(mAuthListener);
            FoursquareSession foursquareSession = new FoursquareSession(mCtx);
            Log.d(LOG, foursquareApp.getUserName() + " " + foursquareSession.getAccessToken());
            accessToken = foursquareSession.getAccessToken();
            setField();
            MainActivity.setToolbarVisibility(foursquareApp.hasAccessToken());

        } else {
            view = inflater.inflate(R.layout.content_not, container, false);
            ((ImageView) view.findViewById(codetribe.sifiso.com.businesscardsexchanger.R.id.imageView1)).setImageDrawable(mCtx.getDrawable(R.drawable.foursquare_icon));
            ((Button) view.findViewById(codetribe.sifiso.com.businesscardsexchanger.R.id.btn_connect)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    foursquareApp.authorize();
                }
            });
            foursquareApp.setListener(mAuthListener);
            MainActivity.setToolbarVisibility(foursquareApp.hasAccessToken());
        }

        return view;
    }

    FoursquareApp.FsqAuthListener mAuthListener = new FoursquareApp.FsqAuthListener() {
        @Override
        public void onSuccess() {
            getActivity().finish();

            startActivity(new Intent(FourSquareFragment.this.getActivity(), MainActivity.class));
        }

        @Override
        public void onFail(String error) {

        }
    };

    public void getDataByRadius(final int distance) {
        getLocations(distance, mLocation.getLatitude(), mLocation.getLongitude(), accessToken);
    }

    public void getLocations(int radius, double latitude, double longitude, String accessToken) {
        MainActivity.setRefreshActionButtonState(true);
        String url = Util.customSearchSpecials(latitude, longitude, accessToken, radius).toString();
        try {
            BaseVolley.getRemoteData(url, mCtx, new BaseVolley.BohaVolleyListener() {
                @Override
                public void onResponseReceived(JSONObject r) {
                    try {
                        MainActivity.setRefreshActionButtonState(false);
                        if (r.getJSONObject("meta").getInt("code") <= 0) {

                            return;
                        }


                        Log.d(LOG, new Gson().toJson(DataUtil.specialItemModel(r.optJSONObject("response"))));
                        mSpecialList = DataUtil.specialItemModel(r.optJSONObject("response"));
                        setList(mSpecialList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onVolleyError(VolleyError error) {
                    MainActivity.setRefreshActionButtonState(false);
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            getDataByRadius(120);
            return true;
        }
        return false;
    }

    public void setList(List<SpecialItemModel> list) {
        mSpecialList = list;
        MainActivity.setCountField(mSpecialList.size());
        Log.d("LOG", new Gson().toJson(list));
        if (view != null) {
            Log.d("LOG", new Gson().toJson(list));

            specialAdapter = new ShopSpecialAdapter(mCtx, list, new ShopSpecialAdapter.ShopSpecialAdapterListeners() {
                @Override
                public void onDirection(LocationModel model) {

                }
            });

            Log.d("LOG Check", new Gson().toJson(list));
            lsCards.setAdapter(specialAdapter);

        }
    }

    static String LOG = FourSquareFragment.class.getSimpleName();
}
