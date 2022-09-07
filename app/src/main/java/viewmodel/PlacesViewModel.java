package viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import model.nearbysearchpojo.Result;
import repositories.NearbySearchRepositoryImpl;

/**
 * ViewModel for places
 */

@HiltViewModel
public class PlacesViewModel extends ViewModel {

    //For data
    private final NearbySearchRepositoryImpl mNearbySearchRepositoryImpl;

    @Inject
    public PlacesViewModel(NearbySearchRepositoryImpl nearbySearchRepository) {
        mNearbySearchRepositoryImpl = nearbySearchRepository;
    }

    public void fetchNearbySearchPlaces(String location) {
        mNearbySearchRepositoryImpl.fetchNearbySearchPlaces(location);
    }

    public LiveData<List<Result>> getNearbySearchResponseLiveData() {
        return mNearbySearchRepositoryImpl.getNearbySearchResponseLiveData();
    }
}
