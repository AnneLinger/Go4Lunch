package viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import model.placedetailspojo.Result;
import repositories.PlaceDetailsRepositoryImpl;

/**
 * ViewModel for place details
 */

@HiltViewModel
public class PlaceDetailsViewModel extends ViewModel {

    //For data
    private final PlaceDetailsRepositoryImpl mPlaceDetailsRepositoryImpl;

    @Inject
    public PlaceDetailsViewModel(PlaceDetailsRepositoryImpl placeDetailsRepositoryImpl) {
        mPlaceDetailsRepositoryImpl = placeDetailsRepositoryImpl;
    }

    public void fetchPlaceDetails(String placeId) {
        mPlaceDetailsRepositoryImpl.fetchPlaceDetails(placeId);
    }

    public LiveData<Result> getPlaceDetailsLiveData() {
        return mPlaceDetailsRepositoryImpl.getPlaceDetailsLiveData();
    }
}
