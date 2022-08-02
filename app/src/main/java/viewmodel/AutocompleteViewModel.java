package viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import model.autocompletepojo.AutocompleteResponse;
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

    public void fetchAutocomplete(String query, String location, int radius){
        mAutocompleteRepositoryImpl.fetchAutocomplete(query, location, radius);
    }

    public LiveData<AutocompleteResponse> getAutocompleteLiveData() {
        return mAutocompleteRepositoryImpl.getAutocompleteLiveData();
    }
}
