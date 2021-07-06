package com.example.stonks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.example.stonks.Adapter.ViewPagerAdapter;
import com.example.stonks.Fragments.CompanyFragment;
import com.example.stonks.Fragments.OverviewFragment;
import com.example.stonks.RequestClasses.CashFlowStatement;
import com.example.stonks.RequestClasses.Growth;
import com.example.stonks.RequestClasses.Profile;
import com.example.stonks.RequestClasses.Quote;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    SearchView searchView;
    Button homeButton;
    private int responses = 0;
    private String companyName, companyIndustry, revenueGrowth, freeCashFlowGrowth, freeCashFlowAverage, image, price, marketCap, pe ;

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    private APICall apiCall;
    private final String TAG = "tag";
    FragmentManager fm;
    private int period;

    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://financialmodelingprep.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        viewPager = (ViewPager2) findViewById(R.id.viewPager);
        pagerAdapter = new ViewPagerAdapter(MainActivity.this);
        viewPager.setAdapter(pagerAdapter);



        fm = getSupportFragmentManager();
        createOverViewFragment();

        apiCall = retrofit.create(APICall.class);

        searchView = findViewById(R.id.searchView);

        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                responses = 0;
                Log.d(TAG, "onCreate: search view text is " + searchView.getQuery());

                String tickerSymbol = searchView.getQuery().toString().toUpperCase();

                try {
                    getGrowth(tickerSymbol);
                    getCashFlowStatement(tickerSymbol);
                    getProfileData(tickerSymbol);
                    getQuote(tickerSymbol);
                    searchView.setQuery("", false);
                    searchView.setIconified(true);
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

        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOverViewFragment();
            }
        });
    }

    private void getProfileData(String tickerSymbol) {
        Call<List<Profile>> callProfileData = apiCall.getProfileData(tickerSymbol);
        callProfileData.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Log.d(TAG, "onResponse:  Get Profile Failed ");
                } else {
                    responses++;
                    companyName = response.body().get(0).getCompanyName();
                    companyIndustry = response.body().get(0).getIndustry();
                    image = response.body().get(0).getImage();
                    if (responses >= 4) {
                        createCompanyFragment();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {

                Log.d(TAG, "onFailure: Profile Data Call" + "\n" + t.toString());
            }
        });
//        try {
//            Response<List<Profile>> response = callProfileData.execute();
//            Profile prof = response.body().get(0);
//            Log.d(TAG, "getProfileData: "  + response.body().get(0).toString());
//            data = new Data(response.body().get(0).getCompanyName());
//
//        } catch (Exception e){
//            Log.d(TAG, "getProfileData: exception" + e.getStackTrace());
//        }
    }

    private void getGrowth(String tickerSymbol) {
        Call<List<Growth>> callGrowthData = apiCall.getGrowthData(tickerSymbol);

        callGrowthData.enqueue(new Callback<List<Growth>>() {
            @Override
            public void onResponse(Call<List<Growth>> call, Response<List<Growth>> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Log.d(TAG, "onResponse:  Get Growth Failed ");
                } else {
                    responses++;
                    Log.d(TAG, "onResponse: Growth response size is " + response.body().size());
                    double revenueGrowthValue = 0.0;
                    double freeCashFlowValue = 0.0;
                    int growthPeriod = Math.min(response.body().size(), 5);

                    for (int i = 0; i < growthPeriod; i++) {
                        revenueGrowthValue += (response.body().get(i).getRevenueGrowth());
                    }
                    for (int i = 0; i < growthPeriod; i++) {
                        freeCashFlowValue += response.body().get(i).getFreeCashFlowGrowth();
                    }
                    revenueGrowth = (df2.format((revenueGrowthValue * 100) / period) + " %");
                    freeCashFlowGrowth = (df2.format((freeCashFlowValue * 100) / period) + " %");

                    if (responses >= 4) {
                        createCompanyFragment();
                    }
//                    textColorChange(revenueGrowthTextView , revenueGrowthValue);
//                    textColorChange(freeCashFlowGrowthTextView, freeCashFlowValue);
//                    revenueGrowthTextView.setText();
//                    freeCashFlowGrowthTextView.setText();
                }
            }

            @Override
            public void onFailure(Call<List<Growth>> call, Throwable t) {
                Log.d(TAG, "onFailure: Growth Data call " + "\n" + t.toString());

            }
        });
    }

    private void getCashFlowStatement(String tickerSymbol) {
        Call<List<CashFlowStatement>> callCashFlowStatementData = apiCall.getCashFlowStatementData(tickerSymbol);

        callCashFlowStatementData.enqueue(new Callback<List<CashFlowStatement>>() {
            @Override
            public void onResponse(Call<List<CashFlowStatement>> call, Response<List<CashFlowStatement>> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Log.d(TAG, "onResponse:  Get Cash Flow Statement Failed ");
                } else {

                    responses++;

                    double freeCashFlowSum = 0;

                    period = Math.min(response.body().size(), 5);
//                    periodTextView.setText(" Data is baset on " + period + " Years  Period ");
                    for (int i = 0; i < period; i++) {
                        freeCashFlowSum += response.body().get(i).getFreeCashFlow();
                    }
                    double freeCashFlowAverageData = freeCashFlowSum / period;


//                    textColorChange(averageFreeCashFlowTextView, freeCashFlowAverage);

                    if (freeCashFlowAverageData > 1000000000) {
                        freeCashFlowAverage = ("$ " + df2.format(freeCashFlowAverageData / 1000000000) + " B");
                    } else if (freeCashFlowAverageData > 1000000) {
                        freeCashFlowAverage = ("$ " + df2.format(freeCashFlowAverageData / 1000000) + " M");
                    } else if (freeCashFlowAverageData < -1000000000) {
                        freeCashFlowAverage = ("$ " + df2.format(freeCashFlowAverageData / 1000000000) + " B");
                    } else if (freeCashFlowAverageData < -1000000) {
                        freeCashFlowAverage = ("$ " + df2.format(freeCashFlowAverageData / 1000000) + " M");
                    } else {
                        freeCashFlowAverage = ("$ " + df2.format(freeCashFlowAverage));
                    }
                    if (responses >= 4) {
                        createCompanyFragment();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CashFlowStatement>> call, Throwable t) {

                Log.d(TAG, "onFailure:  Cash Flow Statement call :" + "\n" + t.toString());
            }
        });
    }

    private void getQuote(String tickerSymbol) {
        Call<List<Quote>> callQuoteData = apiCall.getQuoteData(tickerSymbol);

        callQuoteData.enqueue(new Callback<List<Quote>>() {
            @Override
            public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Log.d(TAG, "onResponse:  Get Data Failed ");
                } else {
                    responses++;

                    price = ("$ " + df2.format(response.body().get(0).getPrice()));
                    pe = (df2.format(response.body().get(0).getPe()));
                    double marketCapDouble = response.body().get(0).getMarketCap();
                    if (marketCapDouble > 1000000000) {
                        marketCap = ("$ " + df2.format(marketCapDouble / 1000000000) + " B");
                    } else if (marketCapDouble > 1000000) {
                        marketCap = ("$ " + df2.format(marketCapDouble / 1000000) + " M");
                    } else {
                        marketCap = (df2.format(marketCapDouble));
                    }
                    if (responses >= 4) {
                        createCompanyFragment();
                    }

                }

            }

            @Override
            public void onFailure(Call<List<Quote>> call, Throwable t) {

                Log.d(TAG, "onFailure: Quote Data Call" + "\n" + t.toString());
            }
        });

    }

    private void createOverViewFragment() {
        OverviewFragment fragment = new OverviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TEST", "this is a test");
        fragment.setArguments(bundle);
        fm.beginTransaction()
                .add(R.id.fragment, fragment)
                .commit();

    }

    private void createCompanyFragment() {
        CompanyFragment fragment = new CompanyFragment();
//        CompanyFragment newFragment = fragment.newInstance(companyName, companyIndustry, revenueGrowth
////                , freeCashFlowGrowth, freeCashFlowAverage, image
////                , price, marketCap, pe);
        Bundle bundle = new Bundle();
        bundle.putString("name", companyName);
        bundle.putString("industry", companyIndustry);
        bundle.putString("revenueGrowth", revenueGrowth);
        bundle.putString("freeCashFlowGrowth", freeCashFlowGrowth);
        bundle.putString("freeCashFlowAverage", freeCashFlowAverage);
        bundle.putString("image", image);
        bundle.putString("price", price);
        bundle.putString("marketCap", marketCap);
        bundle.putString("pe", pe);
        bundle.putString("period", Integer.toString(period));
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, fragment)
                .commit();
    }

}