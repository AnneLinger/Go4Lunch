package viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import data.PlacesApi;
import model.nearbysearchpojo.NearbySearchResponse;
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

    public LiveData<NearbySearchResponse> getNearbySearchResponseLiveData() {
        return mNearbySearchRepositoryImpl.getNearbySearchResponseLiveData();
    }

    public void fetchNearbySearchPlaces(String location, float radius) {
        mNearbySearchRepositoryImpl.fetchNearbySearchPlaces(location, radius);
    }
}
