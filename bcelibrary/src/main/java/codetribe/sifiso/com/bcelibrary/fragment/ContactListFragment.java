package codetribe.sifiso.com.bcelibrary.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import codetribe.sifiso.com.bcelibrary.R;
import codetribe.sifiso.com.bcelibrary.utils.PagerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactListFragment extends Fragment implements PagerFragment {


    public static ContactListFragment instanceFragment() {
        ContactListFragment fragment = new ContactListFragment();
        return fragment;
    }

    public ContactListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }


    @Override
    public void count() {

    }
}
