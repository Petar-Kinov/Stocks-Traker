package com.example.stonks.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.stonks.Adapters.RecyclerAdapter;
import com.example.stonks.R;
import com.example.stonks.RequestClasses.FinantialModelingPrep.DailyMover;
import com.example.stonks.ViewModels.CompanyViewModel;
import com.example.stonks.ViewModels.DailyMoversViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends Fragment implements RecyclerAdapter.OnItemClickedListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private DailyMoversViewModel dailyMoversViewModel;
    private CompanyViewModel companyViewModel;
    private String TAG = "tag";
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DailyMover> dailyMoverArrayList ;

    Button upButton;
    Button downButton;

    public OverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OverviewFragment newInstance(String param1, String param2) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        upButton = view.findViewById(R.id.upButton);
        downButton = view.findViewById(R.id.downButton);
        dailyMoverArrayList = new ArrayList<>();

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dailyMoversViewModel.setSavedStateHandle("gainers");
                upButton.setClickable(false);
                downButton.setClickable(false);
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dailyMoversViewModel.setSavedStateHandle("losers");
            }
        });


        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        dailyMoversViewModel = new ViewModelProvider(requireActivity()).get(DailyMoversViewModel.class);
        companyViewModel = new ViewModelProvider(requireActivity()).get(CompanyViewModel.class);

        dailyMoversViewModel.setSavedStateHandle("gainers");

        dailyMoversViewModel.getDailyMoverLiveData().observe(requireActivity(), new Observer<ArrayList<DailyMover>>() {
            @Override
            public void onChanged(ArrayList<DailyMover> dailyMoverList) {
                updateUI(dailyMoverList);
                dailyMoverArrayList = dailyMoverList;
                upButton.setClickable(true);
                downButton.setClickable(true);
            }
        });
        return view;
    }

    public void updateUI(ArrayList<DailyMover> dailyMovers){
        recyclerAdapter = new RecyclerAdapter(dailyMovers , this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onItemClick(String tickerSymbol) {
        companyViewModel.setTickerSymbol(tickerSymbol);
        Log.d(TAG, "onItemClick: ticker is " + tickerSymbol);
    }
}