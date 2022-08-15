package ui.fragments;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.FragmentListViewBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.Booking;
import model.autocompletepojo.Prediction;
import model.nearbysearchpojo.Result;
import ui.activities.PlacesActivity;
import ui.adapter.PlaceListAdapter;
import viewmodel.AutocompleteViewModel;
import viewmodel.PlacesViewModel;
import viewmodel.UserViewModel;

/**
*Fragment to display a list of places
*/
public class ListViewFragment extends Fragment {

    //For UI
    private FragmentListViewBinding mBinding;
    private RecyclerView mRecyclerView;

    //For data
    private static List<Result> mPlaceList;
    private List<Result> mPlaceListAutocomplete = new ArrayList<>();
    private UserViewModel mUserViewModel;
    private PlacesViewModel mPlacesViewModel;
    private AutocompleteViewModel mAutocompleteViewModel;
    private Location mLocation;
    private String mLocationString;
    private ListViewFragment mListViewFragment = this;

    //TODO manage with repo
    private String mUser;
    private List<Booking> mBookingList = new ArrayList<>();

    public static ListViewFragment newInstance() {
        return new ListViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentListViewBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureViewModels();
        observeLocation();
        observePlaces();
        initRecyclerView(mPlaceList);
        observeAutocomplete();
    }

    private void initRecyclerView(List<Result> list) {
        mUser = mUserViewModel.getCurrentUser().toString();
        mBookingList.add(new Booking("0", list.get(0).getPlaceId(), mUser));
        mRecyclerView = mBinding.rvListView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(new PlaceListAdapter(list, mLocation, mBookingList));
    }

    private void configureViewModels() {
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mPlacesViewModel = new ViewModelProvider(requireActivity()).get(PlacesViewModel.class);
        mAutocompleteViewModel = new ViewModelProvider(requireActivity()).get(AutocompleteViewModel.class);
    }

    //Get the user location
    @SuppressLint("MissingPermission")
    private void observeLocation() {
        mUserViewModel.getLivedataLocation().observe(requireActivity(), this::initLocation);
    }

    private void initLocation(Location location) {
        mLocation = location;
        mLocationString = location.getLatitude() + "," + location.getLongitude();
    }

    private void observePlaces() {
        mPlacesViewModel.getNearbySearchResponseLiveData().observe(requireActivity(), this::initPlaceList);
    }

    private void initPlaceList(List<Result> results) {
        mPlaceList = results;
    }

    private void observeAutocomplete() {
            mAutocompleteViewModel.getAutocompleteLiveData().observe(requireActivity(), this::initAutocomplete);
    }

    private void initAutocomplete(List<Prediction> predictions){
        //Clear previous predictions
        mPlaceListAutocomplete.clear();

        if(mListViewFragment.isVisible()){
            //If no search is done
            if(predictions.isEmpty()) {
                initRecyclerView(mPlaceList);
            }
            //If search occurs
            else {
                for (Result result : mPlaceList) {
                    for (Prediction prediction : predictions) {
                        if (prediction.getStructuredFormatting().getMainText().contains(result.getName())) {
                            mPlaceListAutocomplete.add(result);
                        }
                    }
                }
                //If search returns no result
                if (mPlaceListAutocomplete.isEmpty()) {
                    mBinding.rvListView.setVisibility(View.GONE);
                    Toast.makeText(requireActivity(), getString(R.string.no_search_result), Toast.LENGTH_LONG).show();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBinding.rvListView.setVisibility(View.VISIBLE);
                            initRecyclerView(mPlaceList);
                        }
                    }, 2000);
                }
                //If there are search results to display
                else {
                    initRecyclerView(mPlaceListAutocomplete);
                }
            }
        }
    }
}
