package com.example.crop;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crop.POJOs.CommunityAdapter;
import com.example.crop.POJOs.CommunityData;
import com.example.crop.POJOs.CommunityDataModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityFragment extends Fragment {

    private ArrayList<CommunityDataModel> datamodel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView.Adapter adapter;

    public CommunityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_community, container, false);
        recyclerView = view.findViewById(R.id.community_ques_list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        datamodel = new ArrayList<>();
        for (int i = 0; i < CommunityData.drawableArray.length; i++) {
            datamodel.add(new CommunityDataModel(
                    CommunityData.drawableArray[i]
            ));
        }
        adapter = new CommunityAdapter(datamodel);
        recyclerView.setAdapter(adapter);
        return view;
    }

}
