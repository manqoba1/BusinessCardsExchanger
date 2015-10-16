package codetribe.sifiso.com.businesscardsexchanger;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import codetribe.sifiso.com.bcelibrary.Models.CaptionModel;
import codetribe.sifiso.com.bcelibrary.fragment.ContactListFragment;
import codetribe.sifiso.com.bcelibrary.fragment.NewContactFragment;
import codetribe.sifiso.com.bcelibrary.utils.PagerFragment;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class MainActivity extends AppCompatActivity implements ContactListFragment.ContactListFragmentListener, NavigationDrawerFragment.NavigationDrawerFragmentListener, MaterialTabListener {
    Toolbar toolbar;
    ViewPager mPager;
    MaterialTabHost mTaps;
    NavigationDrawerFragment drawerFragment;
    List<PagerFragment> pagerFragmentList;
    ContactListFragment contactListFragment;
    NewContactFragment newContactFragment;
    PagerAdapter pagerAdapter;


    private Context mCtx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUpDrawer(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        mCtx = getApplicationContext();
        mPager = (ViewPager) findViewById(R.id.mPager);
        mTaps = (MaterialTabHost) this.findViewById(R.id.materialTabHost);
        //setLocationMap();
        build();

    }


    public void build() {
        pagerFragmentList = new ArrayList<>();
        newContactFragment = new NewContactFragment();
        contactListFragment = new ContactListFragment();
        // pagerFragmentList.add(newContactFragment);

        pagerFragmentList.add(contactListFragment);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                mTaps.setSelectedNavigationItem(position);
            }
        });
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            mTaps.addTab(
                    mTaps.newTab()
                            .setIcon(getIcon(i))
                            .setTabListener(this)
            );
        }
    }

    private Drawable getIcon(int i) {
        return ContextCompat.getDrawable(getApplicationContext(), pagerAdapter.icon[i]);
    }
   /* private void build() {
        pagerFragmentList = new ArrayList<>();
        newContactFragment = new NewContactFragment();
        contactListFragment = new ContactListFragment();
        pagerFragmentList.add(newContactFragment);
        pagerFragmentList.add(contactListFragment);
        pagerAdapter=new PagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
        mTaps.setDistributeEvenly(true);

        mTaps.setBackgroundColor(getResources().getColor(R.color.cyan_500));
        mTaps.setSelectedIndicatorColors(getResources().getColor(R.color.pink_300));
        mTaps.setCustomTabView(R.layout.custom_tab_view, R.id.tabText);
        mTaps.setViewPager(mPager);
        setTitle(pagerAdapter.tabText[0]);
        mTaps.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(pagerAdapter.tabText[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        mPager.setCurrentItem(position);
//        setTitle(pagerAdapter.tabText[position]);
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        mPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    @Override
    public void onPassingRadius(int radius) {

    }

    @Override
    public void onStartGalleryActivity(CaptionModel locationModel) {

    }


    class PagerAdapter extends FragmentStatePagerAdapter {
        public int icon[] = {android.R.drawable.ic_menu_gallery};
        public String[] tabText = {getString(R.string.instagram_list)};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return (Fragment) pagerFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return pagerFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable drawer = ContextCompat.getDrawable(getApplicationContext(), icon[position]);
            drawer.setBounds(0, 0, 72, 72);
            ImageSpan imageSpan = new ImageSpan(drawer);
            SpannableString spannableString = new SpannableString(" ");
            spannableString.setSpan(imageSpan, 0, spannableString.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }
    }

    boolean isBusy;


    static String LOG = MainActivity.class.getSimpleName();

}
