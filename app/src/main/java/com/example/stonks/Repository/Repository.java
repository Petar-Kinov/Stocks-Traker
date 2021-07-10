package com.example.stonks.Repository;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.stonks.RequestClasses.FinantialModelingPrep.CashFlowStatement;
import com.example.stonks.RequestClasses.FinantialModelingPrep.DailyMover;
import com.example.stonks.RequestClasses.FinantialModelingPrep.Growth;
import com.example.stonks.RequestClasses.FinantialModelingPrep.Profile;
import com.example.stonks.RequestClasses.FinantialModelingPrep.Quote;
import com.example.stonks.RequestClasses.FinantialModelingPrep.Sector;
import com.example.stonks.RequestClasses.YahooFinance.Analysis;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {

    private final String TAG = "tag";
    private APICall fmpApiCall;
    private APICall yahooApiCall;
    private MutableLiveData<CompanyData> companyDataMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<DailyMover>> dailyMoverMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Sector>> sectorMutableLiveData = new MutableLiveData<>();
    private static Retrofit retrofitFMP = null;
    private static Retrofit retrofitYahoo = null;
    private Bundle dataBundle;

    private int responses = 0;
    private final int responsesNeeded = 5;

    private static final Repository repository = new Repository();

    public static Repository getInstance(){
        return repository;
    }
    public static Retrofit getFMPApiClient() {
        if (retrofitFMP == null) {
            retrofitFMP = new Retrofit.Builder()
                    .baseUrl("https://financialmodelingprep.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitFMP;
    }
    public static Retrofit getYahooApiClient() {
        if (retrofitYahoo == null) {
            retrofitYahoo = new Retrofit.Builder()
                    .baseUrl("https://apidojo-yahoo-finance-v1.p.rapidapi.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitYahoo;
    }


    public LiveData<CompanyData> getCompanyData(String tickerSymbol){
        fmpApiCall = getFMPApiClient().create(APICall.class);
        yahooApiCall = getYahooApiClient().create(APICall.class);
        final CompanyData companyData = new CompanyData();

        Call<List<Profile>> getProfileData = fmpApiCall.getProfileData(tickerSymbol);
        getProfileData.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Log.d(TAG, "onResponse:  Get Profile Data Failed ");
                } else {
                    responses++;
                    companyData.setName(response.body().get(0).getCompanyName());
                    companyData.setIndustry(response.body().get(0).getIndustry());
                    companyData.setImage(response.body().get(0).getImage());
                    if (responses >= responsesNeeded){
                        companyDataMutableLiveData.setValue(companyData);
                        Log.d(TAG, "getCompanyData: created CompanyData instance with name " + companyDataMutableLiveData.getValue().getName());
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {
                Log.d(TAG, "onFailure: Profile Data Call failed" + "\n" + t.toString());
            }
        });

        Call<List<Growth>> getGrowthData = fmpApiCall.getGrowthData(tickerSymbol);
        getGrowthData.enqueue(new Callback<List<Growth>>() {
            @Override
            public void onResponse(Call<List<Growth>> call, Response<List<Growth>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.d(TAG, "onResponse:  Get Growth Data Failed ");
                } else {
                    responses++;
                    int growthPeriod = response.body().size();
                    double revenueGrowthSum = 0.0;
                    double freeCashFlowGrowthSum = 0.0;
                    double netIncomeGrowthSum = 0.0;
                    for (int i = 0; i < growthPeriod; i++){
                        revenueGrowthSum += response.body().get(i).getRevenueGrowth();
                        freeCashFlowGrowthSum += response.body().get(i).getFreeCashFlowGrowth();
                        netIncomeGrowthSum += response.body().get(i).getNetIncomeGrowth();
                    }
                    companyData.setRevenueGrowth((revenueGrowthSum / growthPeriod));
                    companyData.setFreeCashFlowGrowth((freeCashFlowGrowthSum / growthPeriod));
                    companyData.setNetIncomeGrowth((netIncomeGrowthSum / growthPeriod));
                    Log.d(TAG, "onResponse: set growth data");
                }
                if (responses >= responsesNeeded){
                    companyDataMutableLiveData.setValue(companyData);
                    Log.d(TAG, "getCompanyData: created CompanyData instance with name " + companyDataMutableLiveData.getValue().getName());
                }
            }

            @Override
            public void onFailure(Call<List<Growth>> call, Throwable t) {
                Log.d(TAG, "onFailure: Growth Data call failed : " + "\n" + t.toString());
            }
        });

        Call<List<CashFlowStatement>> getCashFlowStatementData = fmpApiCall.getCashFlowStatementData(tickerSymbol);
        getCashFlowStatementData.enqueue(new Callback<List<CashFlowStatement>>() {
            @Override
            public void onResponse(Call<List<CashFlowStatement>> call, Response<List<CashFlowStatement>> response) {
                if (!response.isSuccessful() || response.body() == null){
                    Log.d(TAG, "onResponse: Get Cash Flow Statement Data failed");
                } else {
                    responses++;
                    double freeCashFlow = 0.0;
                    int period = response.body().size();
                    for (int i = 0; i < period; i++){
                        freeCashFlow += response.body().get(i).getFreeCashFlow();
                    }
                    companyData.setAverageFreeCashFlow(freeCashFlow / period);
                    companyData.setPeriod(period);
                }
                if (responses >= responsesNeeded){
                    companyDataMutableLiveData.setValue(companyData);
                }
            }

            @Override
            public void onFailure(Call<List<CashFlowStatement>> call, Throwable t) {
                Log.d(TAG, "onFailure: Get Cash Flow Statement call failed : "  + "\n " + t.toString());
            }
        });

        Call<List<Quote>> getQuoteData = fmpApiCall.getQuoteData(tickerSymbol);
        getQuoteData.enqueue(new Callback<List<Quote>>() {
            @Override
            public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {
                if (!response.isSuccessful() || response.body() == null){
                    Log.d(TAG, "onResponse: Get Quote Data failed");
                } else {
                    responses++;
                    companyData.setPrice(response.body().get(0).getPrice());
                    companyData.setMarketCap(response.body().get(0).getMarketCap());
                    companyData.setPeRatio(response.body().get(0).getPe());
                }
                if (responses >= responsesNeeded){
                    companyDataMutableLiveData.setValue(companyData);
                }
            }
            @Override
            public void onFailure(Call<List<Quote>> call, Throwable t) {
                Log.d(TAG, "onFailure: Get Quote Data call failed : "  + "\n " + t.toString());
            }
        });

        Call<Analysis> getAnalysisData = yahooApiCall.getAnalysis(tickerSymbol);
        getAnalysisData.enqueue(new Callback<Analysis>() {
            @Override
            public void onResponse(Call<Analysis> call, Response<Analysis> response) {

                if (!response.isSuccessful() || response.body() == null){
                    Log.d(TAG, "onResponse: Get Analysis Data failed");
                } else {
                    String jsonResponse = new Gson().toJson(response.body());
                    responses++;
//                    JSONObject jsonObject = null;
//                    try {
//                        jsonObject = new JSONObject(jsonResponse );
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

                    // Financial Data
//                    String financial = jsonObject.optString("financialData");
//                    if (TextUtils.isEmpty(financial)) {
//                        Log.d(TAG, "onResponse: financial data does not exist");
//                    } else {
                        companyData.setProfitMargin(response.body().getFinancialData().getProfitMargins().getRaw());
                        companyData.setGrossMargin(response.body().getFinancialData().getGrossMargins().getRaw());
                        companyData.setReturnOnAssets(response.body().getFinancialData().getReturnOnAssets().getRaw());
                        companyData.setPriceTargetLow(response.body().getFinancialData().getTargetLowPrice().getRaw());
                        companyData.setPriceTargetMedian(response.body().getFinancialData().getTargetMedianPrice().getRaw());
                        companyData.setPriceTargetHigh(response.body().getFinancialData().getTargetHighPrice().getRaw());
                    }

                    // Earnings Trend Data
//                    String earnigns = jsonObject.optString("earningsTrend");
//                    String trend = jsonObject.optString("trend");
//                    if (TextUtils.isEmpty(earnigns) && TextUtils.isEmpty(trend)) {
//                        Log.d(TAG, "onResponse: earningsTrend does not exist in the response");
//                    } else {
//                        Log.d(TAG, "onResponse: earningsTrend exists in the response");
                Log.d(TAG, "onResponse: growth analysis is " + response.body().getEarningsTrend().getTrend().get(4).getGrowth().getRaw() );
                        companyData.setGrowthAnalysis(response.body().getEarningsTrend().getTrend().get(4).getGrowth().getRaw());
//                    }
//                }

                if (responses >= responsesNeeded){
                    companyDataMutableLiveData.setValue(companyData);
                }
            }

            @Override
            public void onFailure(Call<Analysis> call, Throwable t) {
                Log.d(TAG, "onFailure: Get Analysis Data call failed : "  + "\n " + t.toString());
            }
        });
        return companyDataMutableLiveData;
    }

    public LiveData<ArrayList<DailyMover>> getDailyMovers(String move) {
        fmpApiCall = getFMPApiClient().create(APICall.class);
        ArrayList<DailyMover> dailyMoverArrayList = new ArrayList<>();

        Call<List<DailyMover>> getGainer = fmpApiCall.getMovers(move);
        getGainer.enqueue(new Callback<List<DailyMover>>() {
            @Override
            public void onResponse(Call<List<DailyMover>> call, Response<List<DailyMover>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.d(TAG, "onResponse: Get Gainers Data failed");
                } else {
                    for (int i = 0; i < response.body().size(); i++) {

                        // TODO Replace this code with dailyMoverArrayList.add(response.body().get(i);
                        DailyMover dailyMover = new DailyMover();
                        dailyMover.setCompanyName(response.body().get(i).getCompanyName());
                        dailyMover.setPrice(response.body().get(i).getPrice());
                        dailyMover.setChangesPercentage(response.body().get(i).getChangesPercentage());
                        dailyMover.setTicker(response.body().get(i).getTicker());
                        dailyMoverArrayList.add(dailyMover);
                    }
                    dailyMoverMutableLiveData.setValue(dailyMoverArrayList);
                }
            }
            @Override
            public void onFailure(Call<List<DailyMover>> call, Throwable t) {
                Log.d(TAG, "onFailure: Get Gainer call failed " + t.toString());
            }
        });
        return dailyMoverMutableLiveData;
    }

    public LiveData<ArrayList<Sector>> getSectors() {
        fmpApiCall = getFMPApiClient().create(APICall.class);
        ArrayList<Sector> sectorArrayList = new ArrayList<>();

        Call<List<Sector>> getSectorData = fmpApiCall.getSectors();
        getSectorData.enqueue(new Callback<List<Sector>>() {
            @Override
            public void onResponse(Call<List<Sector>> call, Response<List<Sector>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.d(TAG, "onResponse: Get Sectors Data failed");
                } else {
                    for (int i = 0; i < response.body().size(); i++){
                        sectorArrayList.add(response.body().get(i));
                    }
                    sectorMutableLiveData.setValue(sectorArrayList);
                }
            }

            @Override
            public void onFailure(Call<List<Sector>> call, Throwable t) {
                Log.d(TAG, "onFailure: Get Sectors call failed");
            }
        });
        return sectorMutableLiveData;
    }
}
