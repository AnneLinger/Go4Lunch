package viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import model.autocompletepojo.AutocompleteResponse;
import model.autocompletepojo.Prediction;
import repositories.AutocompleteRepositoryImpl;

/**
*ViewModel for autocomplete
*/

@HiltViewModel
public class AutocompleteViewModel extends ViewModel {

    //For data
    private final AutocompleteRepositoryImpl mAutocompleteRepositoryImpl;

    //Constructor
    @Inject
    public AutocompleteViewModel(AutocompleteRepositoryImpl autocompleteRepositoryImpl){
        mAutocompleteRepositoryImpl = autocompleteRepositoryImpl;
    }

    public void fetchAutocomplete(String query, String location){
        mAutocompleteRepositoryImpl.fetchAutocomplete(query, location);
    }

    public LiveData<List<Prediction>> getAutocompleteLiveData() {
        return mAutocompleteRepositoryImpl.getAutocompleteLiveData();
    }

    public void setAutocompleteToNull(){
        mAutocompleteRepositoryImpl.setAutocompleteToNull();
    }
}
