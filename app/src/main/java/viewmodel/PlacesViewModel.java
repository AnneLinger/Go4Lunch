package viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import model.nearbysearchpojo.Result;
import repositories.NearbySearchRepositoryImpl;

/**
*ViewModel for places
*/

@HiltViewModel
public class PlacesViewModel extends ViewModel {

    //For data
    private final NearbySearchRepositoryImpl mNearbySearchRepositoryImpl;

    //Constructor
    @Inject
    public PlacesViewModel(NearbySearchRepositoryImpl nearbySearchRepository) {
        mNearbySearchRepositoryImpl = nearbySearchRepository;
    }

    public void fetchNearbySearchPlaces(String location, int radius) {
        Log.d("Anne", "fetchVM");
        mNearbySearchRepositoryImpl.fetchNearbySearchPlaces(location, radius);
    }

    public LiveData<List<Result>> getNearbySearchResponseLiveData() {
        return mNearbySearchRepositoryImpl.getNearbySearchResponseLiveData();
    }
}
