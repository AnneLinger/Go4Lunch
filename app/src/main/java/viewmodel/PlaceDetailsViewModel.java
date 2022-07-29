package viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import model.placedetailspojo.Result;
import repositories.PlaceDetailsRepositoryImpl;

/**
*ViewModel for place details
*/

@HiltViewModel
public class PlaceDetailsViewModel extends ViewModel {

    //For data
    private final PlaceDetailsRepositoryImpl mPlaceDetailsRepositoryImpl;

    //Constructor
    @Inject
    public PlaceDetailsViewModel(PlaceDetailsRepositoryImpl placeDetailsRepositoryImpl){
        mPlaceDetailsRepositoryImpl = placeDetailsRepositoryImpl;
    }

    public void fetchPlaceDetails(String placeId) {
        Log.e("Anne", "fetchPDVM");
        mPlaceDetailsRepositoryImpl.fetchPlaceDetails(placeId);
    }

    public LiveData<Result> getPlaceDetailsLiveData() {
        Log.e("Anne", "getPDVM");
        return mPlaceDetailsRepositoryImpl.getPlaceDetailsLiveData();
    }
}
