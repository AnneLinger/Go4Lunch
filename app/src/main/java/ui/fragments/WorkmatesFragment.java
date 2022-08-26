package ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anne.linger.go4lunch.databinding.FragmentWorkmatesBinding;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import model.Booking;
import model.User;
import ui.adapter.WorkmatesListAdapter;
import viewmodel.BookingViewModel;
import viewmodel.UserViewModel;

/**
*Fragment to display workmates
*/
public class WorkmatesFragment extends Fragment {

    //For UI
    private FragmentWorkmatesBinding mBinding;
    private RecyclerView mRecyclerView;

    //For data
    private List<User> mUserList = new ArrayList<>();
    private static List<Booking> mBookingList = new ArrayList<>();
    private UserViewModel mUserViewModel;
    private BookingViewModel mBookingViewModel;
    private WorkmatesFragment mWorkmatesFragment = this;

    public static WorkmatesFragment newInstance() {
        return new WorkmatesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentWorkmatesBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureViewModels();
        observeUsers();
        observeBookings();
    }

    private void configureViewModels() {
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mBookingViewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);
    }

    private void initRecyclerView() {
        mRecyclerView = mBinding.rvWorkmatesListView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(new WorkmatesListAdapter(mUserList, mBookingList));
    }


    private void observeUsers() {
        mUserViewModel.getUserListFromFirestore();
        mUserViewModel.getUserListLiveData().observe(requireActivity(), this::getUsers);
    }

    private void getUsers(List<User> users) {
        mUserList = users;
        Log.e("Anne", "getUserActivity : " + mUserList.toString());
        Log.e("Anne", mUserList.get(0).getName());
        Log.e("Anne", mUserList.get(0).toString());
    }

    private void observeBookings() {
        Log.e("Anne", "observeBookingsInPlaceDetails");
        mBookingViewModel.fetchBookingList();
        mBookingViewModel.getBookingListLiveData().observe(requireActivity(), this::getBookings);
    }

    private void getBookings(List<Booking> bookings) {
        Log.e("Anne", bookings.toString());
        mBookingList = bookings;
        initRecyclerView();
    }
}
