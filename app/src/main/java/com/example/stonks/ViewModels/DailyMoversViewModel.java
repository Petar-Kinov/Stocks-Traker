package com.example.stonks.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.stonks.RequestClasses.FinantialModelingPrep.DailyMover;
import com.example.stonks.Repository.Repository;

import java.util.ArrayList;

public class DailyMoversViewModel extends ViewModel {

    private final String TAG = "tag";
    private final SavedStateHandle savedStateHandle;
    private final Repository repository = Repository.getInstance();
    private final LiveData<ArrayList<DailyMover>> dailyMoverLiveData;

    public DailyMoversViewModel(SavedStateHandle savedStateHandle){
        super();
        this.savedStateHandle = savedStateHandle;
        LiveData<String> moveLiveData = savedStateHandle.getLiveData("move");
        dailyMoverLiveData = Transformations.switchMap(moveLiveData,move -> repository.getDailyMovers(move));
    }

    public LiveData<ArrayList<DailyMover>> getDailyMoverLiveData() {
        return dailyMoverLiveData;
    }

    public void setSavedStateHandle(String move){
        savedStateHandle.set("move" , move);
    }
}
