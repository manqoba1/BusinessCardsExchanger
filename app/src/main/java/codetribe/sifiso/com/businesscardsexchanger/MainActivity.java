package codetribe.sifiso.com.businesscardsexchanger;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import codetribe.sifiso.com.bcelibrary.Models.CaptionModel;
import codetribe.sifiso.com.businesscardsexchanger.fragments.ContactListFragment;
import codetribe.sifiso.com.businesscardsexchanger.fragments.FourSquareFragment;

public class MainActivity extends AppCompatActivity implements ContactListFragment.ContactListFragmentListener, NavigationDrawerFragment.NavigationDrawerFragmentListener {
    Toolbar toolbar;
    NavigationDrawerFragment drawerFragment;
    static Menu mMenu;

    static RelativeLayout top;
    ContactListFragment contactListFragment;
    FourSquareFragment fourSquareFragment;
    private Context mCtx;
    private int position;


    TextView txtRadius;
    static TextView SI_count;
    SeekBar seekBar;
    int distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        mCtx = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUpDrawer(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        top = (RelativeLayout) findViewById(R.id.top);
        mCtx = getApplicationContext();

        txtRadius = (TextView) findViewById(codetribe.sifiso.com.bcelibrary.R.id.SI_radius);
        seekBar = (SeekBar) findViewById(codetribe.sifiso.com.bcelibrary.R.id.SI_seekBar);
        SI_count = (TextView) findViewById(codetribe.sifiso.com.bcelibrary.R.id.SI_count);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtRadius.setText("" + seekBar.getProgress());


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                distance = seekBar.getProgress();
                if (contactListFragment != null) {
                    contactListFragment.getDataByRadius(distance);
                }
                if (fourSquareFragment != null) {
                    fourSquareFragment.getDataByRadius(distance);
                }

            }
        });
        // mPager = (ViewPager) findViewById(R.id.mPager);
        //mTaps = (MaterialTabHost) this.findViewById(R.id.materialTabHost);
        //setLocationMap();
        //  build();
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("Opened");
        } else {
            position = 0;
        }

        setFragment(position);
    }

    public static void setToolbarVisibility(boolean isOK) {
        if (isOK) {
            top.setVisibility(View.VISIBLE);
//            mMenu.clear();
        } else {

            top.setVisibility(View.GONE);
        }

    }

    public void setFragment(int position) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (position) {
            case ISTAGRAM_FRAGMENT:
                fourSquareFragment = null;
                contactListFragment = new ContactListFragment();

                ft.replace(R.id.content, contactListFragment);
                ft.commit();

                setTitle(mCtx.getString(R.string.instagram_list));
                break;
            case FOURSQUARE_SPECIALS_FRAGMENT:
                contactListFragment = null;
                fourSquareFragment = new FourSquareFragment();
                ft.replace(R.id.content, fourSquareFragment);
                ft.commit();

                setTitle(mCtx.getString(R.string.four_square_specials));
                break;
            default:

                break;
        }

       /* if (contactListFragment != null) {
            contactListFragment.getDataByRadius(distance);
        }
        if (fourSquareFragment != null) {

        }*/
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("Opened", position);
        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            //getLocations();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
        setFragment(position);
        //   mPager.setCurrentItem(position);
//        setTitle(pagerAdapter.tabText[position]);
    }


    boolean isBusy;


    private static final int ISTAGRAM_FRAGMENT = 0;
    private static final int FOURSQUARE_SPECIALS_FRAGMENT = 1;
    static String LOG = MainActivity.class.getSimpleName();

    public static void setCountField(int count) {

        SI_count.setText("" + count);

    }

    public static void setRefreshActionButtonState(final boolean refreshing) {
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


    @Override
    protected void onStart() {

        super.onStart();
    }


    @Override
    public void onStartGalleryActivity(CaptionModel locationModel) {

    }
}
