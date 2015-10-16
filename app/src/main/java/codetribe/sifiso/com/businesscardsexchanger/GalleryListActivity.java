package codetribe.sifiso.com.businesscardsexchanger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.VolleyError;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import codetribe.sifiso.com.bcelibrary.Adapters.GalleryListAdapter;
import codetribe.sifiso.com.bcelibrary.Models.CaptionModel;
import codetribe.sifiso.com.bcelibrary.activities.MapsActivity;
import codetribe.sifiso.com.bcelibrary.toolbox.BaseVolley;
import codetribe.sifiso.com.bcelibrary.utils.DataUtil;
import codetribe.sifiso.com.bcelibrary.utils.SpacesItemDecoration;
import codetribe.sifiso.com.bcelibrary.utils.Util;

public class GalleryListActivity extends AppCompatActivity {
    RecyclerView lsCards;
    private Context mCtx;
    private Integer locationID;
    private String accessToken;
    private Menu mMenu;
    private List<CaptionModel> mList = Collections.emptyList();
    GalleryListAdapter adapter;
    CaptionModel mCaptionModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCtx = getApplicationContext();

        lsCards = (RecyclerView) findViewById(R.id.lsCards);
        lsCards.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        lsCards.addItemDecoration(new SpacesItemDecoration(4));
        //lsCards.setHasFixedSize(true);
        lsCards.setItemAnimator(new DefaultItemAnimator());
        if (savedInstanceState != null) {
            mList = savedInstanceState.getParcelableArrayList("gallery");
            mCaptionModel = savedInstanceState.getParcelable("caption");
            setRecycleViewList();
        } else {
            locationID = getIntent().getIntExtra("locationID", 1);
            accessToken = getIntent().getStringExtra("accessToken");
            mCaptionModel = getIntent().getParcelableExtra("caption");
            getImageForTheLocation();
        }
        locationID = getIntent().getIntExtra("locationID", 1);
        getSupportActionBar().setTitle(mCaptionModel.fullName);
        Log.d(LOG, "Location ID : " + locationID);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("gallery", (ArrayList<? extends Parcelable>) mList);
        outState.putParcelable("caption", mCaptionModel);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery_list_menu, menu);

        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_map) {
            Intent intent = new Intent(GalleryListActivity.this, MapsActivity.class);
            intent.putParcelableArrayListExtra("caption", (ArrayList<? extends Parcelable>) mList);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getImageForTheLocation() {
        String url = Util.customFindByLocationID(locationID, accessToken).toString();
        setRefreshActionButtonState(true);
        try {
            BaseVolley.getRemoteData(url, mCtx, new BaseVolley.BohaVolleyListener() {
                @Override
                public void onResponseReceived(JSONObject response) {
                    try {
                        setRefreshActionButtonState(false);
                        if (response.getJSONObject("meta").getInt("code") <= 0) {

                            return;
                        }

                        mList = new ArrayList<CaptionModel>();
                        Log.d(LOG, "Array Length: " + response.optJSONArray("data").length());
                        for (int i = 0; i < response.optJSONArray("data").length(); i++) {

                            JSONObject ja = response.optJSONArray("data").getJSONObject(i);
                            if (DataUtil.captionModel(ja) == null) {
                                continue;
                            }
                            DateTime date = new DateTime(DataUtil.captionModel(ja).createdTime);


                            Log.d(LOG, date.toString("HH:mm"));

                            mList.add(DataUtil.captionModel(ja));
                        }
                        Log.d(LOG, "Array Length2: " + mList.size());
                        setRecycleViewList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onVolleyError(VolleyError error) {
                    setRefreshActionButtonState(false);
                }
            });
        } catch (Exception e) {

        }
    }

    private void setRecycleViewList() {
        adapter = new GalleryListAdapter(mCtx, mList, new GalleryListAdapter.GalleryListAdapterListener() {
            @Override
            public void onFullScaleView(CaptionModel captionModel) {
                Intent intent = new Intent(GalleryListActivity.this, FullViewActivity.class);
                intent.putExtra("caption", captionModel);
                startActivity(intent);
            }
        });
        lsCards.setAdapter(adapter);
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (mMenu != null) {
            final MenuItem refreshItem = mMenu.findItem(R.id.action_refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.action_bar_progess);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

    static String LOG = GalleryListActivity.class.getSimpleName();
}
