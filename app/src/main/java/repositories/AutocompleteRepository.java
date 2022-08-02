package repositories;

import androidx.lifecycle.LiveData;

import model.autocompletepojo.AutocompleteResponse;
import model.autocompletepojo.Prediction;

/**
 * Interface repository for autocomplete
 */

public interface AutocompleteRepository {

    LiveData<AutocompleteResponse> getAutocompleteLiveData();

    void fetchAutocomplete(String query, String location, int radius);
}
