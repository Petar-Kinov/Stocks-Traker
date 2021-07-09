package com.example.stonks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SearchView;

import com.example.stonks.Adapters.ViewPagerAdapter;
import com.example.stonks.Repository.CompanyData;

import java.text.DecimalFormat;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    SearchView searchView;
    Button homeButton;
    private int responses = 0;
    private final int responsesNeeded = 5;
    private String companyName, companyIndustry, revenueGrowth, freeCashFlowGrowth, freeCashFlowAverage, image, price, marketCap, pe ;
    private Bundle dataBundle;

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    private APICall apiCallFMP;
    private APICall apiCallYahoo;
    private final String TAG = "tag";
    FragmentManager fm;
    private int period;

    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBundle = new Bundle();
        viewPager = (ViewPager2)findViewById(R.id.viewPager);
        pagerAdapter = new ViewPagerAdapter(MainActivity.this);
        viewPager.setAdapter(pagerAdapter);

        final CompanyViewModel companyViewModel =new ViewModelProvider(this).get(CompanyViewModel.class);


//        Retrofit retrofitFMP = new Retrofit.Builder()
//                .baseUrl("https://financialmodelingprep.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        Retrofit retrofitYahoo = new Retrofit.Builder()
//                .baseUrl("https://apidojo-yahoo-finance-v1.p.rapidapi.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

        // TODO: ViewPager2 Fragments
//        viewPager = (ViewPager2) findViewById(R.id.viewPager);
//        pagerAdapter = new ViewPagerAdapter(MainActivity.this);
//        viewPager.setAdapter(pagerAdapter);

//        fm = getSupportFragmentManager();
//        createOverViewFragment();

//        apiCallFMP = retrofitFMP.create(APICall.class);
//        apiCallYahoo = retrofitYahoo.create(APICall.class);

        searchView = findViewById(R.id.searchView);

        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                responses = 0;

                String tickerSymbol = searchView.getQuery().toString().toUpperCase();

                try {
                    companyViewModel.setTickerSymbol(searchView.getQuery().toString().toUpperCase());
                    searchView.setQuery("", false);
                    searchView.setIconified(true);
                    companyViewModel.getCompanyLiveData().observe(MainActivity.this, new Observer<CompanyData>() {
                        @Override
                        public void onChanged(CompanyData companyData) {
                            viewPager.setCurrentItem(1);
                        }
                    });
                } catch (Exception e) {
                    Log.d(TAG, "onQueryTextSubmit:  Exception" + Arrays.toString(e.getStackTrace()));
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

//        homeButton = findViewById(R.id.homeButton);
//        homeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                createOverViewFragment();
//            }
//        });
    }

//    private void getProfileData(String tickerSymbol) {
//        Call<List<Profile>> callProfileData = apiCallFMP.getProfileData(tickerSymbol);
//        callProfileData.enqueue(new Callback<List<Profile>>() {
//            @Override
//            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
//
//                if (!response.isSuccessful() || response.body() == null) {
//                    Log.d(TAG, "onResponse:  Get Profile Failed ");
//                } else {
//                    responses++;
//                    dataBundle.putString("name", response.body().get(0).getCompanyName());
////                    companyName = response.body().get(0).getCompanyName();
//                    dataBundle.putString("industry", response.body().get(0).getIndustry());
////                    companyIndustry = response.body().get(0).getIndustry();
//                    dataBundle.putString("image", response.body().get(0).getImage());
////                    image = response.body().get(0).getImage();
//                    if (responses >= responsesNeeded) {
//                        createCompanyFragment();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Profile>> call, Throwable t) {
//
//                Log.d(TAG, "onFailure: Profile Data Call" + "\n" + t.toString());
//            }
//        });
////        try {
////            Response<List<Profile>> response = callProfileData.execute();
////            Profile prof = response.body().get(0);
////            Log.d(TAG, "getProfileData: "  + response.body().get(0).toString());
////            data = new Data(response.body().get(0).getCompanyName());
////
////        } catch (Exception e){
////            Log.d(TAG, "getProfileData: exception" + e.getStackTrace());
////        }
//    }
//
//    private void getGrowth(String tickerSymbol) {
//        Call<List<Growth>> callGrowthData = apiCallFMP.getGrowthData(tickerSymbol);
//
//        callGrowthData.enqueue(new Callback<List<Growth>>() {
//            @Override
//            public void onResponse(Call<List<Growth>> call, Response<List<Growth>> response) {
//
//                if (!response.isSuccessful() || response.body() == null) {
//                    Log.d(TAG, "onResponse:  Get Growth Failed ");
//                } else {
//                    responses++;
//                    Log.d(TAG, "onResponse: Growth response size is " + response.body().size());
//                    double revenueGrowthValue = 0.0;
//                    double netincomeGrowth = 0.0;
//                    double freeCashFlowValue = 0.0;
//
//                    int growthPeriod = Math.min(response.body().size(), 5);
//
//                    for (int i = 0; i < growthPeriod; i++) {
//                        revenueGrowthValue += response.body().get(i).getRevenueGrowth();
//                        netincomeGrowth += response.body().get(i).getNetIncomeGrowth();
//                        freeCashFlowValue += response.body().get(i).getFreeCashFlowGrowth();
//                    }
//
//                    dataBundle.putDouble("revenueGrowth", (revenueGrowthValue * 100) / growthPeriod);
//                    dataBundle.putDouble("netIncomeGrowth" , (netincomeGrowth * 100) / growthPeriod);
//                    dataBundle.putDouble("freeCashFlowGrowth" , (freeCashFlowValue * 100) / growthPeriod);
//
//                    if (responses >= responsesNeeded) {
//                        createCompanyFragment();
//                    }
////                    textColorChange(revenueGrowthTextView , revenueGrowthValue);
////                    textColorChange(freeCashFlowGrowthTextView, freeCashFlowValue);
////                    revenueGrowthTextView.setText();
////                    freeCashFlowGrowthTextView.setText();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Growth>> call, Throwable t) {
//                Log.d(TAG, "onFailure: Growth Data call " + "\n" + t.toString());
//
//            }
//        });
//    }
//
//    private void getCashFlowStatement(String tickerSymbol) {
//        Call<List<CashFlowStatement>> callCashFlowStatementData = apiCallFMP.getCashFlowStatementData(tickerSymbol);
//
//        callCashFlowStatementData.enqueue(new Callback<List<CashFlowStatement>>() {
//            @Override
//            public void onResponse(Call<List<CashFlowStatement>> call, Response<List<CashFlowStatement>> response) {
//
//                if (!response.isSuccessful() || response.body() == null) {
//                    Log.d(TAG, "onResponse:  Get Cash Flow Statement Failed ");
//                } else {
//                    responses++;
//                    double freeCashFlowSum = 0;
//                    period = Math.min(response.body().size(), 5);
//                    dataBundle.putString("period", Integer.toString(period));
////                    periodTextView.setText(" Data is baset on " + period + " Years  Period ");
//                    for (int i = 0; i < period; i++) {
//                        freeCashFlowSum += response.body().get(i).getFreeCashFlow();
//                    }
//                    double freeCashFlowAverageData = freeCashFlowSum / period;
//                    dataBundle.putDouble("freeCashFlowAverage" , freeCashFlowAverageData);
//
////                    if (freeCashFlowAverageData > 1000000000) {
////                        dataBundle.putString("freeCashFlowAverage", ("$ " + df2.format(freeCashFlowAverageData / 1000000000) + " B"));
//////                        freeCashFlowAverage = ("$ " + df2.format(freeCashFlowAverageData / 1000000000) + " B");
////                    } else if (freeCashFlowAverageData > 1000000) {
////                        dataBundle.putString("freeCashFlowAverage", ("$ " + df2.format(freeCashFlowAverageData / 1000000) + " M"));
//////                        freeCashFlowAverage = ("$ " + df2.format(freeCashFlowAverageData / 1000000) + " M");
////                    } else if (freeCashFlowAverageData < -1000000000) {
////                        dataBundle.putString("freeCashFlowAverage", ("$ " + df2.format(freeCashFlowAverageData / 1000000000) + " B"));
//////                        freeCashFlowAverage = ("$ " + df2.format(freeCashFlowAverageData / 1000000000) + " B");
////                    } else if (freeCashFlowAverageData < -1000000) {
////                        dataBundle.putString("freeCashFlowAverage", ("$ " + df2.format(freeCashFlowAverageData / 1000000) + " M"));
//////                        freeCashFlowAverage = ("$ " + df2.format(freeCashFlowAverageData / 1000000) + " M");
////                    } else {
////                        dataBundle.putString("freeCashFlowAverage", ("$ " + df2.format(freeCashFlowAverage)));
//////                        freeCashFlowAverage = ("$ " + df2.format(freeCashFlowAverage));
////                    }
//                    if (responses >= responsesNeeded) {
//                        createCompanyFragment();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<CashFlowStatement>> call, Throwable t) {
//
//                Log.d(TAG, "onFailure:  Cash Flow Statement call :" + "\n" + t.toString());
//            }
//        });
//    }
//
//    private void getQuote(String tickerSymbol) {
//        Call<List<Quote>> callQuoteData = apiCallFMP.getQuoteData(tickerSymbol);
//
//        callQuoteData.enqueue(new Callback<List<Quote>>() {
//            @Override
//            public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {
//
//                if (!response.isSuccessful() || response.body() == null) {
//                    Log.d(TAG, "onResponse:  Get Data Failed ");
//                } else {
//                    responses++;
//
//                    dataBundle.putDouble("price", response.body().get(0).getPrice());
//                    dataBundle.putDouble("pe", response.body().get(0).getPe());
//                    dataBundle.putDouble("marketCap", response.body().get(0).getMarketCap());
//
//                    double marketCapDouble = response.body().get(0).getMarketCap();
//                    if (marketCapDouble > 1000000000) {
//                        dataBundle.putString("marketCap", ("$ " + df2.format(marketCapDouble / 1000000000) + " B"));
////                        marketCap = ("$ " + df2.format(marketCapDouble / 1000000000) + " B");
//                    } else if (marketCapDouble > 1000000) {
//                        dataBundle.putString("marketCap", ("$ " + df2.format(marketCapDouble / 1000000) + " M"));
////                        marketCap = ("$ " + df2.format(marketCapDouble / 1000000) + " M");
//                    } else {
//                        dataBundle.putString("marketCap", (df2.format(marketCapDouble)));
////                        marketCap = (df2.format(marketCapDouble));
//                    }
//                    if (responses >= responsesNeeded) {
//                        createCompanyFragment();
//                    }
//                }
//            }
//            @Override
//            public void onFailure(Call<List<Quote>> call, Throwable t) {
//
//                Log.d(TAG, "onFailure: Quote Data Call" + "\n" + t.toString());
//            }
//        });
//
//    }
//
//    private void getAnalysis(String tickerSymbol){
//        Call<Analysis> analysisData = apiCallYahoo.getAnalysis(tickerSymbol);
//
//        analysisData.enqueue(new Callback<Analysis>() {
//            @Override
//            public void onResponse(Call<Analysis> call, Response<Analysis> response) {
//                if (!response.isSuccessful() || response.body() == null) {
//                    Log.d(TAG, "onResponse:  Get Analysis Data not successful ");
//                } else {
//                    responses++;
//                    dataBundle.putDouble("estimatedGrowth" , response.body().getEarningsTrend().getTrend().get(4).getGrowth().getRaw() * 100);
//                    dataBundle.putDouble("priceTargetLow" , response.body().getFinancialData().getTargetLowPrice().getRaw());
//                    dataBundle.putDouble("priceTargetMedian" , response.body().getFinancialData().getTargetMedianPrice().getRaw());
//                    dataBundle.putDouble("priceTargetHigh" , response.body().getFinancialData().getTargetHighPrice().getRaw());
//                    dataBundle.putDouble("profitMargins", response.body().getFinancialData().getProfitMargins().getRaw() * 100);
//                    dataBundle.putDouble("grossMargins", response.body().getFinancialData().getGrossMargins().getRaw() * 100);
//                    dataBundle.putDouble("returnOnAssets",response.body().getFinancialData().getReturnOnAssets().getRaw() * 100);
//                    if (responses >= responsesNeeded) {
//                        createCompanyFragment();
//                    }
//                }
//            }
//            @Override
//            public void onFailure(Call<Analysis> call, Throwable t) {
//                Log.d(TAG, "onFailure: Analysis Data Call failed" + "\n" + t.toString());
//            }
//        });
//    }
//
//
//    private void createOverViewFragment() {
//        OverviewFragment fragment = new OverviewFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("TEST", "this is a test");
//        fragment.setArguments(bundle);
//        fm.beginTransaction()
//                .replace(R.id.fragmentLayout, fragment)
//                .commit();
//
//    }
//
//    private void createCompanyFragment() {
//        CompanyFragment fragment = new CompanyFragment();
////        CompanyFragment newFragment = fragment.newInstance(companyName, companyIndustry, revenueGrowth
//////                , freeCashFlowGrowth, freeCashFlowAverage, image
//////                , price, marketCap, pe);
////        Bundle bundle = new Bundle();
////        bundle.putString("name", companyName);
////        bundle.putString("industry", companyIndustry);
////        bundle.putString("revenueGrowth", revenueGrowth);
////        bundle.putString("freeCashFlowGrowth", freeCashFlowGrowth);
////        bundle.putString("freeCashFlowAverage", freeCashFlowAverage);
////        bundle.putString("image", image);
////        bundle.putString("price", price);
////        bundle.putString("marketCap", marketCap);
////        bundle.putString("pe", pe);
////        bundle.putString("period", Integer.toString(period));
//        fragment.setArguments(dataBundle);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragmentLayout, fragment)
//                .commit();
//    }

}