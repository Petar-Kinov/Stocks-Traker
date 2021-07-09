package com.example.stonks.Repository;

import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.stonks.APICall;
import com.example.stonks.Fragments.CompanyFragment;
import com.example.stonks.RequestClasses.FinantialModelingPrep.CashFlowStatement;
import com.example.stonks.RequestClasses.FinantialModelingPrep.Growth;
import com.example.stonks.RequestClasses.FinantialModelingPrep.Profile;
import com.example.stonks.RequestClasses.FinantialModelingPrep.Quote;
import com.example.stonks.RequestClasses.YahooFinance.Analysis;

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
                    responses++;
                    // Financial Data
                    companyData.setProfitMargin(response.body().getFinancialData().getProfitMargins().getRaw());
                    companyData.setGrossMargin(response.body().getFinancialData().getGrossMargins().getRaw());
                    companyData.setReturnOnAssets(response.body().getFinancialData().getReturnOnAssets().getRaw());
                    companyData.setPriceTargetLow(response.body().getFinancialData().getTargetLowPrice().getRaw());
                    companyData.setPriceTargetMedian(response.body().getFinancialData().getTargetMedianPrice().getRaw());
                    companyData.setPriceTargetHigh(response.body().getFinancialData().getTargetHighPrice().getRaw());
                    // Earnings Trend Data
                    companyData.setGrowthAnalysis(response.body().getEarningsTrend().getTrend().get(0).getGrowth().getRaw());
                }

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

}
