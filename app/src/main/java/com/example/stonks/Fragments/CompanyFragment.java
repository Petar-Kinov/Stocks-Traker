package com.example.stonks.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.stonks.R;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link FirstFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class CompanyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String NAME = "name";
//    private static final String INDUSTRY = "industry";
//    private static final String REVENUE_GROWTH = "revenueGrowth";
//    private static final String FREE_CASH_FLOW_GROWTH = "freeCashFlowGrowth";
//    private static final String FREE_CASH_FLOW_AVERAGE = "freeCashFlowAverage";
//    private static final String IMAGE = "image";
//    private static final String PRICE = "price";
//    private static final String MARKET_CAP = "marketCap";
//    private static final String PE = "pe";

//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String companyName;
//    private String mParam2;

    private String TAG = "tag";

    public CompanyFragment() {
        // Required empty public constructor
    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment FirstFragment.
//     */
//    // TODO: Rename and change types and number of parameters
    public CompanyFragment newInstance(String companyName, String companyIndustry
                                        ,String revenueGrowth, String freeCashFlow
                                        ,String freeCashFLowAverage , String image,
                                        String price , String marketCap, String pe) {
        CompanyFragment fragment = new CompanyFragment();
//        Bundle args = new Bundle();
//        args.putString(NAME, companyName);
//        args.putString(INDUSTRY, companyIndustry);
//        args.putString(REVENUE_GROWTH, revenueGrowth);
//        args.putString(FREE_CASH_FLOW_GROWTH,freeCashFlow);
//        args.putString(FREE_CASH_FLOW_AVERAGE, freeCashFLowAverage);
//        args.putString(IMAGE, image);
//        args.putString(PRICE, price);
//        args.putString(MARKET_CAP, marketCap);
//        args.putString(PE, pe);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.company_fragment, container, false);
//        Log.d(TAG, "onCreateView: " + data.getCompanyName());

        try {
//            Log.d(TAG, "onCreateView: args is " + this.getArguments().getString(NAME, "nooooo "));
            TextView nameText = view.findViewById(R.id.companyNameValue);
            nameText.setSelected(true);
            nameText.setText(this.getArguments().getString("name"));
            TextView industryText = view.findViewById(R.id.companyIndustryValue);
            industryText.setSelected(true);
            industryText.setText(this.getArguments().getString("industry"));
            TextView revenueGrowthText = view.findViewById(R.id.revenueGrowthValue);
            revenueGrowthText.setText(this.getArguments().getString("revenueGrowth"));
            TextView freeCashFlowText = view.findViewById(R.id.freeCashFlowGrowthValue);
            freeCashFlowText.setText(this.getArguments().getString("freeCashFlowGrowth"));
            TextView freeCashFlowAverageText = view.findViewById(R.id.averageFreeCashFlowValue);
            freeCashFlowAverageText.setText(this.getArguments().getString("freeCashFlowAverage"));
            ImageView image = view.findViewById(R.id.companyImage);
            Glide.with(this)
                    .load(this.getArguments().getString("image"))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(image);
            TextView priceText = view.findViewById(R.id.priceValue);
            priceText.setText(this.getArguments().getString("price"));
            TextView marketCapText = view.findViewById(R.id.marketCapValue);
            marketCapText.setText(this.getArguments().getString("marketCap"));
            TextView peText = view.findViewById(R.id.priceToEarningsValue);
            peText.setText(this.getArguments().getString("pe"));
            TextView period = view.findViewById(R.id.period);
            period.setText(" Past " + getArguments().getString("period") + " Years " );
        } catch (Exception e){
            Log.d(TAG, "onCreateView: exeption" + e.getStackTrace());
        }

//        TextView name = view.findViewById(R.id.name);
//        name.setText("test text");
//
////        name.setText(data.getCompanyName());
//        TextView age = view.findViewById(R.id.age);
//        Log.d(TAG, "onCreateView: " + name.getText());

        return view;
    }

    public void setText(){

    }

}