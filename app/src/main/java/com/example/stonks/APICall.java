package com.example.stonks;

//import com.example.stockanalyzer.ModelClasses.CashFlowStatement;
//import com.example.stockanalyzer.ModelClasses.Profile;
//import com.example.stockanalyzer.ModelClasses.Quote;

import com.example.stonks.RequestClasses.CashFlowStatement;
import com.example.stonks.RequestClasses.Growth;
import com.example.stonks.RequestClasses.Profile;
import com.example.stonks.RequestClasses.Quote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APICall {

//    @Headers({"x-rapidapi-key: c548129551msh9bf493672fc4ac8p12a26djsn706c78b9357a",
//              "x-rapidapi-host: apidojo-yahoo-finance-v1.p.rapidapi.com"})
//    @GET("stock/v2/get-summary?symbol=CSX")
//    Call<Stock> getData();

    @GET("api/v3/financial-growth/{TickerSymbol}?limit=20&apikey=3e0cc06500368e574687a38e40602c25")
    Call<List<Growth>> getGrowthData(@Path("TickerSymbol") String tickerSymbol);
//
    @GET("api/v3/cash-flow-statement/{TickerSymbol}?limit=120&apikey=024648deef14db595db1e044108e9cbd")
    Call<List<CashFlowStatement>> getCashFlowStatementData(@Path("TickerSymbol") String tickerSymbol);

    @GET("api/v3/profile/{TickerSymbol}?apikey=3e4a81bd59f5e8778d9cb253aac640ea")
    Call<List<Profile>> getProfileData(@Path("TickerSymbol") String tickerSymbol);

    @GET("api/v3/quote/{TickerSymbol}?apikey=2e6377dc045ea87d235e6edc6ea9f777")
    Call<List<Quote>> getQuoteData(@Path("TickerSymbol") String tickerSymbol);

//    @GET("api/v3/quote/{TickerSymbol}?apikey=2e6377dc045ea87d235e6edc6ea9f777")
//    Call<List<Quote>> getQuoteData(@Path("TickerSymbol") String tickerSymbol);

}
