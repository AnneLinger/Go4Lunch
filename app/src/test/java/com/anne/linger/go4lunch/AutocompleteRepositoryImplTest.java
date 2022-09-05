package com.anne.linger.go4lunch;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import model.autocompletepojo.AutocompleteResponse;
import model.autocompletepojo.Prediction;
import repositories.AutocompleteRepositoryImpl;

/**
*Unit tests for Autocomplete repository
*/

@RunWith(JUnit4.class)
public class AutocompleteRepositoryImplTest {

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    //For data
    public String testQuery ="Le";
    public String testLocation = "48.4875332,-2.0271007";
    private final Prediction mPrediction = mock(Prediction.class);
    @Mock
    public AutocompleteRepositoryImpl mAutocompleteRepository = new AutocompleteRepositoryImpl();
    public List<Prediction> mPredictionList = new ArrayList<>();
    public Prediction testPrediction = mock(Prediction.class);
    public AutocompleteResponse mAutocompleteResponse = mock(AutocompleteResponse.class);

    @Before
    public void init() {
        mAutocompleteRepository.fetchAutocomplete(testQuery, testLocation);
        mAutocompleteRepository.getAutocompleteLiveData().observeForever(new Observer<List<Prediction>>() {
            @Override
            public void onChanged(List<Prediction> predictions) {
                mPredictionList = predictions;
            }
        });
    }

    @Test
    public void getAutocompleteData() {
        assertFalse(mPredictionList.isEmpty());
    }
}
