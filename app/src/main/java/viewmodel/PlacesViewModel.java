package viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import model.nearbysearchpojo.Result;
import repositories.NearbySearchRepositoryImpl;
import repositories.PlaceDetailsRepositoryImpl;

/**
*ViewModel for places
*/

@HiltViewModel
public class PlacesViewModel extends ViewModel {

    //For data
    private final NearbySearchRepositoryImpl mNearbySearchRepositoryImpl;
    private final PlaceDetailsRepositoryImpl mPlaceDetailsRepositoryImpl;

    //Constructor
    @Inject
    public PlacesViewModel(NearbySearchRepositoryImpl nearbySearchRepository, PlaceDetailsRepositoryImpl placeDetailsRepository) {
        mNearbySearchRepositoryImpl = nearbySearchRepository;
        mPlaceDetailsRepositoryImpl = placeDetailsRepository;
    }

    //..........................For nearbySearch....................................................

    public void fetchNearbySearchPlaces(String location, int radius) {
        Log.d("Anne", "fetchVM");
        mNearbySearchRepositoryImpl.fetchNearbySearchPlaces(location, radius);
    }

    public LiveData<List<Result>> getNearbySearchResponseLiveData() {
        return mNearbySearchRepositoryImpl.getNearbySearchResponseLiveData();
    }

    //..........................For placeDetails....................................................

    public void fetchPlaceDetails(String placeId) {
        mPlaceDetailsRepositoryImpl.fetchPlaceDetails(placeId);
    }

    public LiveData<model.placedetailspojo.Result> getPlaceDetailsLiveData() {
        return mPlaceDetailsRepositoryImpl.getPlaceDetailsLiveData();
    }
}
