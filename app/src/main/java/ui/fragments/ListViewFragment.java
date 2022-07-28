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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.FragmentListViewBinding;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import model.Booking;
import model.nearbysearchpojo.Result;
import ui.adapter.PlaceListAdapter;
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
    private int radius = 12000;
    private SharedPreferences mSharedPreferences;
    private UserViewModel mUserViewModel;
    private PlacesViewModel mPlacesViewModel;
    private Location mLocation;
    private String mLocationString;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //TODO manage with repo
    private List<FirebaseUser> mUserList = new ArrayList<>();
    private List<Booking> mBookingList = new ArrayList<>();

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
        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.e("Anne", "initRV");
        mUserList.add(mAuth.getCurrentUser());
        mBookingList.add(new Booking(0, mPlaceList.get(0).getPlaceId(), mUserList));
        mRecyclerView = mBinding.rvListView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL));
        mRecyclerView.setAdapter(new PlaceListAdapter(mPlaceList, mLocation, mBookingList));
    }

    private void configureViewModels() {
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mPlacesViewModel = new ViewModelProvider(requireActivity()).get(PlacesViewModel.class);
    }

    //Get the user location
    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        Log.e("Anne", "getLoc");
        mUserViewModel.getLivedataLocation().observe(requireActivity(), this::initLocation);
    }

    private void initLocation(Location location) {
        Log.e("Anne", "initLoc");
        mLocation = location;
        mLocationString = location.getLatitude() + "," + location.getLongitude();
    }

    private void initPlaceList() {
        mSharedPreferences = requireActivity().getSharedPreferences(getString(R.string.user_settings), Context.MODE_PRIVATE);
        /**if (mSharedPreferences != null) {
            radius = (int) mSharedPreferences.getFloat(getString(R.string.radius), radius);
        }
        Log.d("Anne", "initPlaceList");*/
        mPlacesViewModel.getNearbySearchResponseLiveData().observe(requireActivity(), this::placeList);
    }

    private void placeList(List<Result> results) {
        mPlaceList = results;
        if(mPlaceList.isEmpty()) {
            Log.e("Anne", "=nullList");
        }
        else {
            Log.e("Anne", "!=nullList");
        }
    }
}
