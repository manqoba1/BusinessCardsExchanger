package codetribe.sifiso.com.businesscardsexchanger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import net.londatiga.android.instagram.Instagram;
import net.londatiga.android.instagram.InstagramSession;
import net.londatiga.android.instagram.InstagramUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import codetribe.sifiso.com.bcelibrary.Models.CaptionModel;
import codetribe.sifiso.com.bcelibrary.Models.ResponseModel;
import codetribe.sifiso.com.bcelibrary.toolbox.BaseVolley;
import codetribe.sifiso.com.bcelibrary.toolbox.WebCheck;
import codetribe.sifiso.com.bcelibrary.toolbox.WebCheckResult;
import codetribe.sifiso.com.bcelibrary.utils.Constants;
import codetribe.sifiso.com.bcelibrary.utils.DataUtil;
import codetribe.sifiso.com.bcelibrary.utils.LocalStore;
import codetribe.sifiso.com.bcelibrary.utils.Util;
import codetribe.sifiso.com.businesscardsexchanger.fragments.ContactListFragment;

public class MainLandActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ContactListFragment.ContactListFragmentListener {
    GoogleApiClient mGoogleApiClient;
    LocationRequest locationRequest;
    Location location;
    boolean mResolvingError;
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final String DIALOG_ERROR = "dialog_error";
    private Context mCtx;
    private Menu mMenu;

    ContactListFragment contactListFragment;

    InstagramSession mInstagramSession;
    Instagram mInstagram;
    InstagramUser mInstagramUser;
    TextView txtRadius, SI_count;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCtx = getApplicationContext();
        mInstagram = new Instagram(this, Constants.INSTAGRAM_CLIENT_ID, Constants.INSTAGRAM_CLIENT_SECRET, Constants.REDIRECT_URI);
        mInstagramSession = mInstagram.getSession();

        if (mInstagramSession.isActive()) {
            setContentView(R.layout.activity_main_land);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            mInstagramUser = mInstagramSession.getUser();

           // contactListFragment = (ContactListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_content);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            txtRadius = (TextView) findViewById(codetribe.sifiso.com.bcelibrary.R.id.SI_radius);
            seekBar = (SeekBar) findViewById(codetribe.sifiso.com.bcelibrary.R.id.SI_seekBar);
            SI_count = (TextView) findViewById(codetribe.sifiso.com.bcelibrary.R.id.SI_count);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    txtRadius.setText("" + seekBar.getProgress());
                    distance = seekBar.getProgress();
                    getCacheData();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            setLocationMap();
            if (savedInstanceState != null) {
                location = savedInstanceState.getParcelable("location");
                mList = savedInstanceState.getParcelableArrayList("captions");
                setListOfLocation();
                return;
            }

        } else {
            setContentView(R.layout.activity_main_land_not);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            ((Button) findViewById(R.id.btn_connect)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mInstagram.authorize(mAuthListener);
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("location", location);
        if (!mList.isEmpty()) {
            outState.putParcelableArrayList("captions", (ArrayList<? extends Parcelable>) mList);
        }
        super.onSaveInstanceState(outState);
    }

    private void showToast(String text) {
        Toast.makeText(MainLandActivity.this, text, Toast.LENGTH_LONG).show();
    }

    private Instagram.InstagramAuthListener mAuthListener = new Instagram.InstagramAuthListener() {
        @Override
        public void onSuccess(InstagramUser user) {
            finish();

            startActivity(new Intent(MainLandActivity.this, MainLandActivity.class));
        }

        @Override
        public void onError(String error) {
            showToast(error);
        }

        @Override
        public void onCancel() {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_land_main, menu);
        if (!mInstagramSession.isActive()) {
            menu.clear();
        }
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            getCacheData();
        }
        if (id == R.id.action_map) {
            Intent intent = new Intent(MainLandActivity.this, MapsActivity.class);
            intent.putParcelableArrayListExtra("caption", (ArrayList<? extends Parcelable>) mList);
            startActivity(intent);
        }
        if (id == R.id.action_logout) {
            mInstagramSession.reset();
            finish();

            startActivity(new Intent(MainLandActivity.this, MainLandActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    //map section start

    private void setLocationMap() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    static final int ACCURACY_LIMIT = 50;

    protected void stopLocationUpdates() {
        Log.e(LOG, "###### stopLocationUpdates - " + new Date().toString());
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.w(LOG, "## onConnected ");
        location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (location != null) {
            Log.w(LOG, "## onConnected location ....lastLocation: "
                    + location.getLatitude() + " "
                    + location.getLongitude() + " acc: "
                    + location.getAccuracy());
            getCacheData();
        }

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(1000);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void getCacheData() {
        final WebCheckResult w = WebCheck.checkNetworkAvailability(mCtx);
        LocalStore.getCachedData(mCtx, LocalStore.CACHE_DATA, new LocalStore.LocalStoreListener() {
            @Override
            public void onFileDataDeserialized(final ResponseModel response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response != null) {
                            mList = response.getCaptionModels();
                            setListOfLocation();
                        }

                        if (w.isWifiConnected()) {
                            getLocations(distance);
                            return;
                            // getData();
                        } else if (w.isMobileConnected()) {
                            getLocations(distance);
                            return;
                        }
                    }
                });

            }

            @Override
            public void onDataCached(ResponseModel response) {

            }

            @Override
            public void onError() {

            }
        });
    }

    public void getLocations(int radius) {
        setRefreshActionButtonState(true);
        String url = Util.customSearch(location.getLatitude(), location.getLongitude(), mInstagramUser.accessToken, radius).toString();
        try {
            BaseVolley.getRemoteData(url, mCtx, new BaseVolley.BohaVolleyListener() {
                @Override
                public void onResponseReceived(JSONObject r) {
                    try {
                        setRefreshActionButtonState(false);
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
                                setListOfLocation();
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
                    setRefreshActionButtonState(false);
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    protected void onStart() {
        Log.e(LOG, "################ onStart .... connect API and location clients ");
        if (mInstagramSession.isActive()) {
            if (!mResolvingError) {  // more about this later

                mGoogleApiClient.connect();

            }
        }
        super.onStart();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOG, "####### onLocationChanged " + location.getAccuracy());
        this.location = location;
        if (location == null) {
            //Util.showToast(ctx, "Please check your GPS connectivity, switch it on if off");
            showSettingDialog();
            return;
        }
        if (location.getAccuracy() <= ACCURACY_LIMIT) {
            stopLocationUpdates();
            getCacheData();
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.

            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    private int distance;

    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(), "errordialog");
    }

    //map section end
    public void showSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainLandActivity.this);

        builder.setTitle("GPS settings");
        builder.setMessage("Please check GPS enabled. Go to settings menu, to switch it on to search for location.");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void setListOfLocation() {
        if (contactListFragment != null) {
            if (mList != null) {
                SI_count.setText("" + mList.size());
                contactListFragment.setList(mList);
            }
        }
    }


    static String LOG = MainLandActivity.class.getSimpleName();
    List<CaptionModel> mList = Collections.emptyList();


    @Override
    public void onStartGalleryActivity(CaptionModel captionModel) {
        Intent intent = new Intent(MainLandActivity.this, GalleryListActivity.class);
        intent.putExtra("locationID", captionModel.locationID);
        intent.putExtra("accessToken", mInstagramUser.accessToken);
        intent.putExtra("caption", captionModel);
        startActivity(intent);
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
}
