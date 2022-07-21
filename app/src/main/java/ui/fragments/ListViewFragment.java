package ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
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

import com.anne.linger.go4lunch.BuildConfig;
import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.FragmentListViewBinding;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

import model.Place;
import model.nearbysearchpojo.NearbySearchResponse;
import ui.activities.SettingsActivity;
import ui.adapter.PlaceListAdapter;
import utils.PlacesApiCalls;
import viewmodel.PlacesViewModel;
import viewmodel.UserViewModel;

/**
*Fragment to display a list of places
*/
public class ListViewFragment extends Fragment implements PlacesApiCalls.Callbacks {

    //For UI
    private FragmentListViewBinding mBinding;
    private RecyclerView mRecyclerView;

    //For data
    private static List<NearbySearchResponse> mPlaceList;
    //TODO change with user radius reference
    private float radius = 1000;
    private SharedPreferences mSharedPreferences;
    private UserViewModel mUserViewModel;
    private PlacesViewModel mPlacesViewModel;
    private Location mLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentListViewBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureViewModels();
        getUserLocation();
        initPlaceList();
        //initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d("Anne", "initRV");
        mRecyclerView = mBinding.rvListView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL));
        mRecyclerView.setAdapter(new PlaceListAdapter(mPlaceList));
    }

    private void configureViewModels() {
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mPlacesViewModel = new ViewModelProvider(requireActivity()).get(PlacesViewModel.class);
    }

    private void initPlaceList() {
        mSharedPreferences = requireActivity().getSharedPreferences(getString(R.string.user_settings), Context.MODE_PRIVATE);
        if (mSharedPreferences != null) {
            radius = mSharedPreferences.getFloat(getString(R.string.radius), radius);
        }
        Log.d("Anne", "initPlaceList");
        //PlacesApiCalls.fetchNearbySearchPlaces(this, mLocation, radius, "restaurant", BuildConfig.MAPS_API_KEY);
    }

    //Get the user location
    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        Log.d("Anne", "getLoc");
        mUserViewModel.getLivedataLocation().observe(requireActivity(), this::initLocation);
    }

    private void initLocation(Location location) {
        Log.d("Anne", "initLoc");
        mLocation = location;
    }

    @Override
    public void onResponse(@Nullable List<NearbySearchResponse> nearbySearchResponseList) {
        Log.d("Anne", "onResponse");
        //nearbySearchResponseList.getResults
        mPlaceList = nearbySearchResponseList;
        initRecyclerView();
    }

    @Override
    public void onFailure() {
        Log.d("Anne", "onFailure");
        Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
    }
}
