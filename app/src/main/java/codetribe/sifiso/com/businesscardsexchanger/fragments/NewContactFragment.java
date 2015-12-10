package codetribe.sifiso.com.businesscardsexchanger.fragments;


import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import codetribe.sifiso.com.bcelibrary.BCEApp;
import codetribe.sifiso.com.bcelibrary.R;
import codetribe.sifiso.com.bcelibrary.utils.PagerFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewContactFragment extends Fragment implements PagerFragment {
    TextView txtName,txtCompanyName;
    Context mCtx;
    View view;
    public static NewContactFragment instanceFragment() {
        NewContactFragment fragment = new NewContactFragment();
        return fragment;
    }

    public NewContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCtx = getActivity();
       view = inflater.inflate(R.layout.fragment_new_contact, container, false);
        setFields();
        return view;
    }

    private void setFields(){
        txtCompanyName = (TextView) view.findViewById(R.id.txtCompanyName);
        txtCompanyName.setTypeface(BCEApp.getNeutonItalic(mCtx));
        txtName = (TextView) view.findViewById(R.id.txtName);
        txtName.setTypeface(BCEApp.getNeutonItalic(mCtx));
    }
    @Override
    public void count() {

    }
}
