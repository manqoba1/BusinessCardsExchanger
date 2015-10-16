package codetribe.sifiso.com.businesscardsexchanger;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.joda.time.DateTime;

import codetribe.sifiso.com.bcelibrary.Models.CaptionModel;
import codetribe.sifiso.com.bcelibrary.utils.GPSLocation;
import codetribe.sifiso.com.bcelibrary.utils.Util;

public class FullViewActivity extends AppCompatActivity {

    ImageView FSC_image;
    TextView FSC_name, FSC_time, FSC_message;
    RelativeLayout FSC_caption;
    FloatingActionButton fab;
    GPSLocation mGpsLocation;
    CaptionModel mCaption;
    private Context mCtx;
    boolean isDetailsShowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCtx = getApplicationContext();
        mGpsLocation = new GPSLocation(mCtx, FullViewActivity.this);

        if (savedInstanceState != null) {
            mCaption = savedInstanceState.getParcelable("caption");
        } else {
            mCaption = getIntent().getParcelableExtra("caption");
        }
        getSupportActionBar().setTitle(mCaption.fullName);
        setFields();
       // Flickr flickr =new Flickr("");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("caption", mCaption);
        super.onSaveInstanceState(outState);
    }

    private void setFields() {
        FSC_image = (ImageView) findViewById(R.id.FSC_image);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        ImageLoader.getInstance().displayImage(mCaption.imageUrlStnd, FSC_image);
        FSC_caption = (RelativeLayout) findViewById(R.id.FSC_caption);


        FSC_caption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG, "Show");

                isDetailsShowed = false;
                Util.collapse(FSC_caption, 200, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                    }
                });
                fab.hide();
            }
        });
        FSC_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(LOG, "Not Show");

                if (!isDetailsShowed) {
                    isDetailsShowed = true;
                    Util.expand(FSC_caption, 200, new Util.UtilAnimationListener() {
                        @Override
                        public void onAnimationEnded() {

                        }
                    });
                    fab.show();
                }


            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double sLatitude = mGpsLocation.getLocation().getLatitude();
                Double sLongitude = mGpsLocation.getLocation().getLongitude();
                setDirections(sLatitude, sLongitude, mCaption.latitude, mCaption.longitude);
            }
        });
        FSC_message = (TextView) findViewById(codetribe.sifiso.com.bcelibrary.R.id.FSC_message);
        FSC_message.setText(mCaption.textMessage);
        FSC_name = (TextView) findViewById(codetribe.sifiso.com.bcelibrary.R.id.FSC_name);
        FSC_name.setText(mCaption.fullName);
        FSC_time = (TextView) findViewById(codetribe.sifiso.com.bcelibrary.R.id.FSC_time);
        DateTime date = new DateTime(mCaption.createdTime);
        FSC_time.setText(date.toString("HH:mm"));
    }

    private void setDirections(Double sLatitude, Double sLongitude, Double dLatitude, Double dLongitude) {
        String url = "http://maps.google.com/maps?saddr="
                + sLatitude + "," + sLongitude
                + "&daddr=" + dLatitude + "," + dLongitude + "&mode=driving";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    static String LOG = FullViewActivity.class.getSimpleName();
}
