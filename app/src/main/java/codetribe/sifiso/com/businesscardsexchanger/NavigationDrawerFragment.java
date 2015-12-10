package codetribe.sifiso.com.businesscardsexchanger;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import codetribe.sifiso.com.bcelibrary.Adapters.DrawerItemAdapter;
import codetribe.sifiso.com.bcelibrary.BCEApp;
import codetribe.sifiso.com.bcelibrary.Models.DrawerItem;
import codetribe.sifiso.com.bcelibrary.utils.SharedUtil;
import codetribe.sifiso.com.bcelibrary.utils.Util;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    RecyclerView mDrawerList;
    public  ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    DrawerItemAdapter mDrawerItemAdapter;

    TextView txtName,txtCompanyName;

    private boolean mFirstTimeDrawerShow;
    private boolean mFromSavedInstanceState;
    View view, containerFragmentDrawer;
    static Context mCtx;
    NavigationDrawerFragmentListener listener;
    Activity activity;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    public static List<DrawerItem> getDrawerData() {
        List<DrawerItem> drawerItems = new ArrayList<>();
        int[] icons = {android.R.drawable.ic_menu_gallery,android.R.drawable.ic_menu_gallery};
        String[] titles = {mCtx.getString(R.string.instagram_list),mCtx.getString(R.string.four_square_specials)};
        for (int i = 0; i < icons.length && i < titles.length; i++) {
            DrawerItem item = new DrawerItem(icons[i], titles[i]);
            drawerItems.add(item);
        }
        return drawerItems;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirstTimeDrawerShow = Boolean.valueOf(SharedUtil.readFromPreferences(getActivity(),
                SharedUtil.KEY_FIRST_TIME_DRAWER_SHOW, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (NavigationDrawerFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MonitorListListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mCtx = getActivity();
        view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mDrawerItemAdapter = new DrawerItemAdapter(mCtx, getDrawerData(), new DrawerItemAdapter.DrawerItemAdapterListener() {
            @Override
            public void onDrawerItemClick(int position) {
                listener.onItemClick(position);
                mDrawerLayout.closeDrawers();
               // mDrawerLayout.sets

            }
        });
        ImageView drawerImage = (ImageView) view.findViewById(R.id.drawer_image);
        drawerImage.setImageDrawable(Util.getRandomBackgroundImage(mCtx));
        mDrawerList = (RecyclerView) view.findViewById(R.id.drawer_list);
        mDrawerList.setAdapter(mDrawerItemAdapter);
        mDrawerList.setLayoutManager(new LinearLayoutManager(mCtx));
        setFields();
        return view;
    }

    private void setFields(){
        txtCompanyName = (TextView) view.findViewById(R.id.txtCompanyName);
        txtCompanyName.setTypeface(BCEApp.getNeutonItalic(mCtx));
        txtName = (TextView) view.findViewById(R.id.txtName);
        txtName.setTypeface(BCEApp.getNeutonItalic(mCtx));
    }

    public void setUpDrawer(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerFragmentDrawer = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity()
                , drawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {

                getActivity().invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mFirstTimeDrawerShow) {
                    mFirstTimeDrawerShow = true;
                    SharedUtil.saveToPreference(
                            getActivity(), SharedUtil.KEY_FIRST_TIME_DRAWER_SHOW,
                            mFirstTimeDrawerShow + "");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset < 0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                }
            }
        };
        if (!mFirstTimeDrawerShow && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(containerFragmentDrawer);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public interface NavigationDrawerFragmentListener {
        public void onItemClick(int position);
    }
}
