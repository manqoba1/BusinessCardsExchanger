package codetribe.sifiso.com.businesscardsexchanger.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import net.londatiga.android.instagram.Instagram;
import net.londatiga.android.instagram.InstagramSession;
import net.londatiga.android.instagram.InstagramUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import codetribe.sifiso.com.bcelibrary.Adapters.LocationAdapter;
import codetribe.sifiso.com.bcelibrary.Models.CaptionModel;
import codetribe.sifiso.com.bcelibrary.Models.ResponseModel;
import codetribe.sifiso.com.bcelibrary.R;
import codetribe.sifiso.com.bcelibrary.toolbox.BaseVolley;
import codetribe.sifiso.com.bcelibrary.toolbox.WebCheck;
import codetribe.sifiso.com.bcelibrary.toolbox.WebCheckResult;
import codetribe.sifiso.com.bcelibrary.utils.Constants;
import codetribe.sifiso.com.bcelibrary.utils.DataUtil;
import codetribe.sifiso.com.bcelibrary.utils.GPSTracker;
import codetribe.sifiso.com.bcelibrary.utils.LocalStore;
import codetribe.sifiso.com.bcelibrary.utils.PagerFragment;
import codetribe.sifiso.com.bcelibrary.utils.SpacesItemDecoration;
import codetribe.sifiso.com.bcelibrary.utils.Util;
import codetribe.sifiso.com.businesscardsexchanger.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactListFragment extends Fragment implements PagerFragment {

    private Context mCtx;
    private View view;
    private RecyclerView lsCards;
    LocationAdapter locationAdapter;
    List<CaptionModel> mList;
    Location mLocation;
    GPSTracker gpsTracker;

    ContactListFragmentListener mListener;

    InstagramSession mInstagramSession;
    Instagram mInstagram;
    InstagramUser mInstagramUser;

    public static ContactListFragment instanceFragment() {
        ContactListFragment fragment = new ContactListFragment();
        return fragment;
    }

    private void setFields() {

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

    public void getDataByRadius(final int distance) {
        getCacheData(distance, mLocation.getLatitude(), mLocation.getLongitude(), accessToken);
    }

    public ContactListFragment() {
        // Required empty public constructor
    }

    public void setList(List<CaptionModel> list) {
        mList = list;
        MainActivity.setCountField(mList.size());
        Log.d("LOG", new Gson().toJson(list));
        if (view != null) {
            Log.d("LOG", new Gson().toJson(list));

            locationAdapter = new LocationAdapter(mCtx, list, new LocationAdapter.LocationAdapterListeners() {
                @Override
                public void onMoreImages(CaptionModel locationModel) {
                    mListener.onStartGalleryActivity(locationModel);

                }

                @Override
                public void onMapImagesView(CaptionModel locationModel) {

                }
            });
            Log.d("LOG Check", new Gson().toJson(list));
            lsCards.setAdapter(locationAdapter);

        }
    }

    String accessToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mCtx = getActivity().getApplicationContext();
        mInstagram = new Instagram(getActivity(), Constants.INSTAGRAM_CLIENT_ID, Constants.INSTAGRAM_CLIENT_SECRET, Constants.REDIRECT_URI);
        mInstagramSession = mInstagram.getSession();


        if (mInstagramSession.isActive()) {
            Log.d(LOG, "Active");
            view = inflater.inflate(R.layout.fragment_contact_list, container, false);
            accessToken = mInstagramSession.getAccessToken();
            Log.d(LOG, "Active : " + mInstagramSession.getAccessToken());
            MainActivity.setToolbarVisibility(mInstagramSession.isActive());
            setFields();
        } else {
            Log.d(LOG, "Not Active");
            view = inflater.inflate(R.layout.content_not, container, false);
            ((ImageView) view.findViewById(codetribe.sifiso.com.businesscardsexchanger.R.id.imageView1)).setImageDrawable(mCtx.getDrawable(R.drawable.instagram_logo));
            ((Button) view.findViewById(codetribe.sifiso.com.businesscardsexchanger.R.id.btn_connect)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mInstagram.authorize(mAuthListener);
                }
            });
            MainActivity.setToolbarVisibility(mInstagramSession.isActive());
        }

        return view;
    }

    private void showToast(String text) {
        Toast.makeText(ContactListFragment.this.getActivity(), text, Toast.LENGTH_LONG).show();
    }

    private Instagram.InstagramAuthListener mAuthListener = new Instagram.InstagramAuthListener() {
        @Override
        public void onSuccess(InstagramUser user) {
            getActivity().finish();

            startActivity(new Intent(ContactListFragment.this.getActivity(), MainActivity.class));
        }

        @Override
        public void onError(String error) {
            showToast(error);
        }

        @Override
        public void onCancel() {

        }
    };

    public void getCacheData(final int distance, final double latitude, final double longitude, final String accessToken) {
        final WebCheckResult w = WebCheck.checkNetworkAvailability(mCtx);
        LocalStore.getCachedData(mCtx, LocalStore.CACHE_DATA, new LocalStore.LocalStoreListener() {
            @Override
            public void onFileDataDeserialized(final ResponseModel response) {

                if (response != null) {
                    mList = response.getCaptionModels();
                    if (response.getCaptionModels() != null) {
                        setList(mList);
                    }

                }

                if (w.isWifiConnected()) {
                    getLocations(distance, latitude, longitude, accessToken);
                    return;
                    // getData();
                } else if (w.isMobileConnected()) {
                    getLocations(distance, latitude, longitude, accessToken);
                    return;
                }


            }

            @Override
            public void onDataCached(ResponseModel response) {

            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void count() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ContactListFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ContactListFragmentListener");
        }
    }

    public interface ContactListFragmentListener {

        //public void onRadiusLocator();

        void onStartGalleryActivity(CaptionModel locationModel);
    }

    static String LOG = ContactListFragment.class.getSimpleName();

    public void getLocations(int radius, double latitude, double longitude, String accessToken) {
        MainActivity.setRefreshActionButtonState(true);
        String url = Util.customSearch(latitude, longitude, accessToken, radius).toString();
        try {
            BaseVolley.getRemoteData(url, mCtx, new BaseVolley.BohaVolleyListener() {
                @Override
                public void onResponseReceived(JSONObject r) {
                    try {
                        MainActivity.setRefreshActionButtonState(false);
                        if (r.getJSONObject("meta").getInt("code") <= 0) {

                            return;
                        }

                        mList = new ArrayList<CaptionModel>();
                        Log.d(LOG, "Array Length: " + r.getJSONArray("data").length());
                        for (int i = 0; i < r.getJSONArray("data").length(); i++) {
                            //Log.d(LOG, new Gson().toJson(response.getJSONArray("data").getJSONObject(i)));
                            JSONObject ja = r.getJSONArray("data").getJSONObject(i);
                            mList.add(DataUtil.captionModel(ja));
                        }
                        Log.d(LOG, "Array Length2: " + mList.size());
                        ResponseModel response = new ResponseModel();
                        response.setCaptionModels(new ArrayList<CaptionModel>());
                        response.setCaptionModels(mList);
                        LocalStore.cacheData(mCtx, response, LocalStore.CACHE_DATA, new LocalStore.LocalStoreListener() {
                            @Override
                            public void onFileDataDeserialized(ResponseModel response) {

                            }

                            @Override
                            public void onDataCached(ResponseModel response) {
                                mList = response.getCaptionModels();
                                if (mList != null) {
                                    setList(mList);
                                }

                                // setListOfLocation();
                            }

                            @Override
                            public void onError() {

                            }
                        });

                        //Log.d(LOG, new Gson().toJson(response));
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

}
