package codetribe.sifiso.com.bcelibrary.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
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
import com.nostra13.universalimageloader.core.ImageLoader;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import codetribe.sifiso.com.bcelibrary.Models.CaptionModel;
import codetribe.sifiso.com.bcelibrary.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<CaptionModel> mList = Collections.emptyList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.top);
        //setActionBar(toolbar);

        mMarkersHashMap = new HashMap<Marker, CaptionModel>();
        mList = getIntent().getParcelableArrayListExtra("caption");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        setEvaluationMarkers();
    }

    private void setEvaluationMarkers() {
        mMap.clear();

        for (CaptionModel cm : mList) {
            BitmapDescriptor desc = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(cm.latitude, cm.longitude)).icon(desc)
                    .snippet(cm.fullName);
            //markerOptions.
            final Marker m = mMap.addMarker(markerOptions);
            mMarkersHashMap.put(m, cm);
            markers.add(m);
            mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

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

    private HashMap<Marker, CaptionModel> mMarkersHashMap;
    List<Marker> markers = new ArrayList<Marker>();

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

            DateTime dateTime = new DateTime(myMarker.createdTime);
            FSC_time.setText(dateTime.toString("HH:mm"));
            FSC_name.setText(myMarker.fullName);

            FSC_message.setText(myMarker.textMessage);
            ImageLoader.getInstance().displayImage(myMarker.imageUrlStnd, FSC_image);


            return v;
        }
    }

}
