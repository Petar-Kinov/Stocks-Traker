package com.example.stonks;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.stonks.Repository.CompanyData;
import com.example.stonks.Repository.Repository;

public class CompanyViewModel extends ViewModel {

    private final String TAG = "tag";
    private SavedStateHandle savedStateHandle;
    private Repository repository = Repository.getInstance();
    private LiveData<CompanyData> companyLiveData;

    public CompanyViewModel(SavedStateHandle savedStateHandle) {
        super();
        this.savedStateHandle = savedStateHandle;
        LiveData<String> tickerSymbolLiveData = savedStateHandle.getLiveData("tickerSymbol");
        companyLiveData = Transformations.switchMap(tickerSymbolLiveData, tickerSymbol -> repository.getCompanyData(tickerSymbol));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<CompanyData> getCompanyLiveData(){
        return companyLiveData;
    }

    public void setTickerSymbol(String tickerSymbol){
        savedStateHandle.set("tickerSymbol", tickerSymbol);
        Log.d(TAG, "setTickerSymbol: savedStateHandle set to " + savedStateHandle.getLiveData("tickerSymbol").getValue());
    }
}
