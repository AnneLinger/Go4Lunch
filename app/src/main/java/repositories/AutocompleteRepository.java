package repositories;

import androidx.lifecycle.LiveData;

import java.util.List;

import model.autocompletepojo.AutocompleteResponse;
import model.autocompletepojo.Prediction;

/**
 * Interface repository for autocomplete
 */

public interface AutocompleteRepository {

    LiveData<List<Prediction>> getAutocompleteLiveData();

    void fetchAutocomplete(String query, String location);

    void setAutocompleteToNull();
}
