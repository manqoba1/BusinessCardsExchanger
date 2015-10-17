package codetribe.sifiso.com.bcelibrary.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import codetribe.sifiso.com.bcelibrary.Adapters.LocationAdapter;
import codetribe.sifiso.com.bcelibrary.Models.CaptionModel;
import codetribe.sifiso.com.bcelibrary.R;
import codetribe.sifiso.com.bcelibrary.utils.PagerFragment;
import codetribe.sifiso.com.bcelibrary.utils.SpacesItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactListFragment extends Fragment implements PagerFragment {

    private Context mCtx;
    private View view;
    private RecyclerView lsCards;
    LocationAdapter locationAdapter;
    List<CaptionModel> mList;

    ContactListFragmentListener mListener;

    public static ContactListFragment instanceFragment() {
        ContactListFragment fragment = new ContactListFragment();
        return fragment;
    }

    private void setFields() {
        lsCards = (RecyclerView) view.findViewById(R.id.lsCards);
        lsCards.setLayoutManager(new LinearLayoutManager(mCtx));
        lsCards.addItemDecoration(new SpacesItemDecoration(4));


    }

    public ContactListFragment() {
        // Required empty public constructor
    }

    public void setList(List<CaptionModel> list) {
        mList = list;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mCtx = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        setFields();
        return view;
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
                    + " must implement MonitorListListener");
        }
    }

    public interface ContactListFragmentListener {
        public void onPassingRadius(int radius);

        void onStartGalleryActivity(CaptionModel locationModel);
    }

    static String LOG = ContactListFragment.class.getSimpleName();
}
