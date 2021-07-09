package com.example.stonks.Fragments;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.stonks.CompanyViewModel;
import com.example.stonks.R;
import com.example.stonks.Repository.CompanyData;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link FirstFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class CompanyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

    private CompanyViewModel companyViewModel;
    private final static DecimalFormat df2Percent = new DecimalFormat("#.##" + " %");
    private final static DecimalFormat df2 = new DecimalFormat("#.##");
//    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.ITALY);


    // TODO: Rename and change types of parameters
//    private final String NAME = "name";
//    private final String INDUSTRY = "industry";
//    private final String IMAGE = "image";
//    private final String PERIOD = "period";
//    private final String PRICE = "price";
//    private final String PE = "pe";
//    private final String REVENUE_GROWTH = "revenueGrowth";
//    private final String NET_INCOME_GROWTH = "netIncomeGrowth";
//    private final String MARKET_CAP = "marketCap";
//    private final String FREE_CASH_FLOW_GROWTH = "freeCashFlowGrowth";
//    private final String FREE_CASH_FLOW_AVERAGE = "freeCashFlowAverage";
//    private final String ESTIMATED_GROWTH = "estimatedGrowth";
//    private final String TARGET_PRICE_LOW = "priceTargetLow";
//    private final String TARGET_PRICE_MEDIAN = "priceTargetMedian";
//    private final String TARGET_PRICE_HIGH = "priceTargetHigh";
//    private final String PROFIT_MARGINS = "profitMargins";
//    private final String GROSS_MARGINS = "grossMargins";
//    private final String RETURN_ON_ASSETS = "returnOnAssets";

    private TextView name;
    private TextView industry;
    private ImageView image;
    private TextView price;
    private TextView marketCap;
    private TextView peRatio;
    private TextView profitMargin;
    private TextView grossMargins;
    private TextView returnOnAssets;
    private TextView period;
    private TextView revenueGrowth;
    private TextView netIncomeGrowth;
    private TextView freeCashFlowGrowth;
    private TextView averageFreeCashFlow;
    private TextView priceTargetLow;
    private TextView priceTargetMedian;
    private TextView priceTargetHigh;
    private TextView growthAnalysis;

    private final String TAG = "tag";

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
    public CompanyFragment newInstance() {
        return new CompanyFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.company_fragment, container, false);

//        currencyFormat.setMaximumFractionDigits(2);
//        currencyFormat.setCurrency(Currency.getInstance("USD"));

        name = view.findViewById(R.id.companyNameValue);
        industry = view.findViewById(R.id.companyIndustryValue);
        image = view.findViewById(R.id.companyImage);
        price = view.findViewById(R.id.priceValue);
        marketCap = view.findViewById(R.id.marketCapValue);
        peRatio = view.findViewById(R.id.priceToEarningsValue);
        profitMargin = view.findViewById(R.id.profitMarginsValue);
        grossMargins = view.findViewById(R.id.grossMarginsValue);
        returnOnAssets = view.findViewById(R.id.returnOnAssetsValue);
        revenueGrowth = view.findViewById(R.id.revenueGrowthValue);
        netIncomeGrowth = view.findViewById(R.id.netIncomeGrowthValue);
        freeCashFlowGrowth = view.findViewById(R.id.freeCashFlowGrowthValue);
        averageFreeCashFlow = view.findViewById(R.id.averageFreeCashFlowValue);
        priceTargetLow = view.findViewById(R.id.priceTargetLow);
        priceTargetMedian = view.findViewById(R.id.priceTargetMedian);
        priceTargetHigh = view.findViewById(R.id.priceTargetHigh);
        growthAnalysis = view.findViewById(R.id.growthAnalysis);

        companyViewModel = new ViewModelProvider(requireActivity()).get(CompanyViewModel.class);
        companyViewModel.getCompanyLiveData().observe(requireActivity(), new Observer<CompanyData>() {
            @Override
            public void onChanged(CompanyData companyData) {
                name.setText(companyData.getName());
                industry.setText(companyData.getIndustry());
                Glide.with(requireContext())
                        .load(companyData.getImage())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(image);
                setPercentText(revenueGrowth,companyData.getRevenueGrowth(),0);
                setPercentText(freeCashFlowGrowth,companyData.getFreeCashFlowGrowth(),0);
                setPercentText(netIncomeGrowth,companyData.getNetIncomeGrowth(),0);
                setNumberText(averageFreeCashFlow,companyData.getAverageFreeCashFlow(),0);
                price.setText(numberConverter(companyData.getPrice()));
                marketCap.setText(numberConverter(companyData.getMarketCap()));
                if (companyData.getPeRatio() != 0.0){
                    peRatio.setText(df2.format(companyData.getPeRatio()));
                } else {
                    peRatio.setText(getResources().getString(R.string.Dashes));
                }
                setPercentText(profitMargin,companyData.getProfitMargin(), 0.1);
                setPercentText(grossMargins,companyData.getGrossMargin(), 0.1);
                setPercentText(returnOnAssets,companyData.getReturnOnAssets(), 0.05);
                setNumberText(priceTargetLow,companyData.getPriceTargetLow(),companyData.getPrice());
                setNumberText(priceTargetMedian,companyData.getPriceTargetMedian(),companyData.getPrice());
                setNumberText(priceTargetHigh,companyData.getPriceTargetHigh(),companyData.getPrice());
                setPercentText(growthAnalysis,companyData.getGrowthAnalysis(),0);
            }
        });
//        Log.d(TAG, "onCreateView: " + data.getCompanyName());

//        try {
//
//            TextView nameText = view.findViewById(R.id.companyNameValue);
//            nameText.setText(this.getArguments().getString(NAME));
//            nameText.setSelected(true);
//
//            TextView industryText = view.findViewById(R.id.companyIndustryValue);
//            industryText.setText(this.getArguments().getString(INDUSTRY));
//            industryText.setSelected(true);
//
//            // Current Data Panel
//
//             ImageView image = view.findViewById(R.id.companyImage);
//                        Glide.with(this)
//                                .load(this.getArguments().getString(IMAGE))
//                                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                                .into(image);
//
//            TextView period = view.findViewById(R.id.period);
//            period.setText(" Past " + getArguments().getString(PERIOD) + " Years " );
//
//            TextView priceText = view.findViewById(R.id.priceValue);
//            priceText.setText( "$ " + df2.format(getArguments().getDouble(PRICE)));
//
//            TextView marketCapText = view.findViewById(R.id.marketCapValue);
//            marketCapText.setText(this.getArguments().getString(MARKET_CAP));
//
//            TextView peText = view.findViewById(R.id.priceToEarningsValue);
//            if (this.getArguments().getDouble(PE) > 20 || this.getArguments().getDouble(PE) < 0){
//                peText.setTextColor(getResources().getColor(R.color.red));
//            } else if (getArguments().getDouble(PE) > 0){
//                peText.setTextColor(getResources().getColor(R.color.green));
//            }
//            if (getArguments().getDouble(PE) != 0){
//                peText.setText(df2.format(this.getArguments().getDouble(PE)));
//            }
//
//            TextView profitMargins = view.findViewById(R.id.profitMarginsValue);
//            colorChange(profitMargins,getArguments().getDouble(PROFIT_MARGINS),10);
//            profitMargins.setText(df2.format(getArguments().getDouble(PROFIT_MARGINS)) + " %");
//
//            TextView grossMargins = view.findViewById(R.id.grossMarginsValue);
//            colorChange(grossMargins,getArguments().getDouble(GROSS_MARGINS) , 10);
//            grossMargins.setText(df2.format(getArguments().getDouble(GROSS_MARGINS)) + " %");
//
//            TextView returnOnAssets = view.findViewById(R.id.returnOnAssetsValue);
//            colorChange(returnOnAssets,getArguments().getDouble(RETURN_ON_ASSETS), 5);
//            returnOnAssets.setText(df2.format(getArguments().getDouble(RETURN_ON_ASSETS)) + " %");
//
//            // Past Data Panel
//
//            TextView revenueGrowthText = view.findViewById(R.id.revenueGrowthValue);
//            colorChange(revenueGrowthText, this.getArguments().getDouble(REVENUE_GROWTH), 0);
//            revenueGrowthText.setText(df2.format(this.getArguments().getDouble(REVENUE_GROWTH)) + " %");
//
//            TextView netIncomeGrowth = view.findViewById(R.id.netIncomeGrowthValue);
//            colorChange(netIncomeGrowth,getArguments().getDouble(NET_INCOME_GROWTH), 0);
//            netIncomeGrowth.setText(df2.format(getArguments().getDouble(NET_INCOME_GROWTH)) + " %");
//
//            TextView freeCashFlowText = view.findViewById(R.id.freeCashFlowGrowthValue);
//            colorChange(freeCashFlowText, this.getArguments().getDouble(FREE_CASH_FLOW_GROWTH), 0);
//            freeCashFlowText.setText(df2.format(this.getArguments().getDouble(FREE_CASH_FLOW_GROWTH)) + " %");
//
//            TextView freeCashFlowAverageText = view.findViewById(R.id.averageFreeCashFlowValue);
//            colorChange(freeCashFlowAverageText, this.getArguments().getDouble(FREE_CASH_FLOW_AVERAGE),0);
//            freeCashFlowAverageText.setText(numberConverter(this.getArguments().getDouble(FREE_CASH_FLOW_AVERAGE)));
//
//            // Analysis panel
//            TextView next5YarsGrowth = view.findViewById(R.id.next5YearsGrowth);
//            colorChange(next5YarsGrowth,getArguments().getDouble(ESTIMATED_GROWTH),0);
//            next5YarsGrowth.setText(df2.format(getArguments().getDouble(ESTIMATED_GROWTH)) + " %");
//
//            TextView targetPriceLow = view.findViewById(R.id.priceTargetLow);
//            colorChange(targetPriceLow, getArguments().getDouble(TARGET_PRICE_LOW), getArguments().getDouble(PRICE));
//            targetPriceLow.setText("$ " + df2.format(getArguments().getDouble(TARGET_PRICE_LOW)));
//
//            TextView targetPriceMedian = view.findViewById(R.id.priceTargetMedian);
//            colorChange(targetPriceMedian, getArguments().getDouble(TARGET_PRICE_MEDIAN), getArguments().getDouble(PRICE));
//            targetPriceMedian.setText("$ " + df2.format(getArguments().getDouble(TARGET_PRICE_MEDIAN)));
//
//            TextView targetPriceHigh = view.findViewById(R.id.priceTargetHigh);
//            colorChange(targetPriceHigh, getArguments().getDouble(TARGET_PRICE_HIGH), getArguments().getDouble(PRICE));
//            targetPriceHigh.setText("$ " + df2.format(getArguments().getDouble(TARGET_PRICE_HIGH)));
//
//        } catch (Exception e){
//            Log.d(TAG, "onCreateView: exeption" + e.getStackTrace());
//        }
        return view;
    }

    public String numberConverter(Double number){
        if (number >= 1000000000) {
            return ("$ " + df2.format(number / 1000000000) + " B");
//                        freeCashFlowAverage = ("$ " + df2.format(freeCashFlowAverageData / 1000000000) + " B");
        } else if (number >= 1000000) {
            return ("$ " + df2.format(number / 1000000) + " M");
//                        freeCashFlowAverage = ("$ " + df2.format(freeCashFlowAverageData / 1000000) + " M");
        } else if (number <= -1000000000) {
            return ("$ " + df2.format(number / 1000000000) + " B");
//                        freeCashFlowAverage = ("$ " + df2.format(freeCashFlowAverageData / 1000000000) + " B");
        } else if (number <= -1000000) {
            return("$ " + df2.format(number / 1000000) + " M");
//                        freeCashFlowAverage = ("$ " + df2.format(freeCashFlowAverageData / 1000000) + " M");
        } else {
            return ("$ " + df2.format(number));
//                        freeCashFlowAverage = ("$ " + df2.format(freeCashFlowAverage));
        }
    }

    public void setPercentText(TextView textView, double companyNumber , double targetNumber){
        if (companyNumber > targetNumber){
            textView.setTextColor(getResources().getColor(R.color.green));
        } else if (companyNumber < targetNumber){
            textView.setTextColor(getResources().getColor(R.color.red));
        }
        Log.d(TAG, "setPercentText: company number is "  + companyNumber);
        textView.setText(String.format(getResources().getString(R.string.number_percent) , df2Percent.format(companyNumber)));
    }

    public void setNumberText(TextView textView, double companyNumber , double targetNumber){
        if (companyNumber > targetNumber){
            textView.setTextColor(getResources().getColor(R.color.green));
        } else if (companyNumber < targetNumber){
            textView.setTextColor(getResources().getColor(R.color.red));
        } else {
            textView.setTextColor(Color.WHITE);
        }
        textView.setText(numberConverter(companyNumber));
    }

}