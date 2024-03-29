package ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.FragmentListViewBinding;

import java.util.ArrayList;
import java.util.List;

import model.Booking;
import model.autocompletepojo.Prediction;
import model.nearbysearchpojo.Result;
import ui.adapter.PlaceListAdapter;
import viewmodel.AutocompleteViewModel;
import viewmodel.BookingViewModel;
import viewmodel.PlacesViewModel;
import viewmodel.UserViewModel;

/**
 * Fragment to display a list of places
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class ListViewFragment extends Fragment {

    //For UI
    private FragmentListViewBinding mBinding;
    private RecyclerView mRecyclerView;

    //For data
    private static List<Result> mPlaceList;
    private final List<Result> mPlaceListAutocomplete = new ArrayList<>();
    private UserViewModel mUserViewModel;
    private PlacesViewModel mPlacesViewModel;
    private AutocompleteViewModel mAutocompleteViewModel;
    private BookingViewModel mBookingViewModel;
    private Location mLocation;
    private String mLocationString;
    private final ListViewFragment mListViewFragment = this;
    private List<Booking> mBookingList = new ArrayList<>();

    public static ListViewFragment newInstance() {
        return new ListViewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentListViewBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureViewModels();
        observeBookings();
        observeLocation();
        observePlaces();
        observeAutocomplete();
    }

    private void initRecyclerView(List<Result> list) {
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
        mBookingViewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);
    }

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

    private void observeBookings() {
        mBookingViewModel.fetchBookingList();
        mBookingViewModel.getBookingListLiveData().observe(getViewLifecycleOwner(), this::updateBookingList);
    }

    private void updateBookingList(List<Booking> bookings) {
        mBookingList = bookings;
        initRecyclerView(mPlaceList);
    }

    private void observeAutocomplete() {
        mAutocompleteViewModel.getAutocompleteLiveData().observe(requireActivity(), this::initAutocomplete);
    }

    private void initAutocomplete(List<Prediction> predictions) {
        //Clear previous predictions
        mPlaceListAutocomplete.clear();

        if (mListViewFragment.isVisible()) {
            //If no search is done
            if (predictions.isEmpty()) {
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
                    handler.postDelayed(() -> {
                        mBinding.rvListView.setVisibility(View.VISIBLE);
                        initRecyclerView(mPlaceList);
                    }, 2000);
                }
                //If there are search results to display
                else {
                    initRecyclerView(mPlaceListAutocomplete);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPlacesViewModel.getNearbySearchResponseLiveData().removeObservers(this);
        mAutocompleteViewModel.getAutocompleteLiveData().removeObservers(this);
        mBookingViewModel.getBookingListLiveData().removeObservers(this);
        mUserViewModel.stopLocationRequest();
    }
}
