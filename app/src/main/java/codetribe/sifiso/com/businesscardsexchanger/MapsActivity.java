package codetribe.sifiso.com.businesscardsexchanger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import codetribe.sifiso.com.bcelibrary.Models.CaptionModel;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<CaptionModel> mCaptionModels;
    private HashMap<Marker, CaptionModel> mMarkersHashMap;
    List<Marker> markers = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mMarkersHashMap = new HashMap<Marker, CaptionModel>();
        mCaptionModels = getIntent().getParcelableArrayListExtra("caption");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        setCaptionOnMap();
    }

    static String LOG = MapsActivity.class.getSimpleName();

    private void setCaptionOnMap() {
        mMap.clear();
        for (CaptionModel cm : mCaptionModels) {
            Log.d(LOG, new Gson().toJson(cm));
            BitmapDescriptor desc = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(cm.latitude, cm.longitude)).icon(desc)
                    .snippet(cm.textMessage);
            final Marker m = mMap.addMarker(markerOptions);
            mMarkersHashMap.put(m, cm);
            markers.add(m);
            mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    CaptionModel model = mMarkersHashMap.get(marker);

                    Intent intent = new Intent(MapsActivity.this, FullViewActivity.class);
                    intent.putExtra("caption", model);

                    startActivity(intent);
                }
            });
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    //ensure that all markers in bounds
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (Marker marker : markers) {
                        builder.include(marker.getPosition());
                    }

                    LatLngBounds bounds = builder.build();
                    int padding = 10; // offset from edges of the map in pixels

                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    //   txtCount.setText("" + markers.size());
                    //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 1.0f));
                    mMap.animateCamera(cu);

                }
            });
        }


    }

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        public MarkerInfoWindowAdapter() {

        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View v = getLayoutInflater().inflate(R.layout.map_info, null);

            final CaptionModel myMarker = mMarkersHashMap.get(marker);

            TextView FSC_name = (TextView) v.findViewById(R.id.FSC_name);
            TextView FSC_time = (TextView) v.findViewById(R.id.FSC_time);
            TextView FSC_message = (TextView) v.findViewById(R.id.FSC_message);

            ImageView FSC_image = (ImageView) v.findViewById(R.id.FSC_image);

            FSC_name.setText(myMarker.fullName);
            DateTime dateTime = new DateTime(myMarker.createdTime);
            FSC_time.setText(dateTime.toString("HH:mm"));
            FSC_message.setText(myMarker.textMessage);
            ImageLoader.getInstance().displayImage(myMarker.imageUrlStnd, FSC_image);

            return v;
        }
    }
}
