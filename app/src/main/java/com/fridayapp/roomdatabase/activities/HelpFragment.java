package com.fridayapp.roomdatabase.activities;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fridayapp.roomdatabase.R;

public class HelpFragment extends Fragment {


    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Help");
        return inflater.inflate(R.layout.fragment_help, container, false);
    }

}
