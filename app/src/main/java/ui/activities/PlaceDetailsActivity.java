package ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anne.linger.go4lunch.BuildConfig;
import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.ActivityPlaceDetailsBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import model.Booking;
import model.User;
import model.placedetailspojo.Result;
import ui.adapter.JoiningWorkmatesListAdapter;
import viewmodel.BookingViewModel;
import viewmodel.PlaceDetailsViewModel;
import viewmodel.UserViewModel;

/**
 * Activity to display details of a place
 */

@RequiresApi(api = Build.VERSION_CODES.M)
@AndroidEntryPoint
public class PlaceDetailsActivity extends AppCompatActivity {

    //For ui
    private ActivityPlaceDetailsBinding mBinding;
    private RecyclerView mRecyclerView;

    //For data
    private PlaceDetailsViewModel mPlaceDetailsViewModel;
    private BookingViewModel mBookingViewModel;
    private UserViewModel mUserViewModel;
    private List<Booking> mBookingList = new ArrayList<>();
    private User mUser;
    private String mPlaceId;
    private String mPlaceName;
    private String mPlaceAddress;
    private final List<String> mJoiningWorkmatesString = new ArrayList<>();
    private final List<User> mJoiningWorkmatesUsers = new ArrayList<>();
    private Booking mUserBooking;
    private List<User> mUserList = new ArrayList<>();
    private final List<String> mUserLikedPlacesList = new ArrayList<>();
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureActionBar();
        configureViewModels();
        observeCurrentUser();
        observeUsers();
        getPlaceDetails();
        initRecyclerView();
        observeBookings();
    }

    private void initUi() {
        mBinding = ActivityPlaceDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    private void configureActionBar() {
        mBinding.toolbar.setNavigationOnClickListener(view -> navigateToPlacesActivity());
    }

    private void initRecyclerView() {
        mRecyclerView = mBinding.rvDetailWorkmates;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL));
        mRecyclerView.setAdapter(new JoiningWorkmatesListAdapter());
    }

    private void configureViewModels() {
        mPlaceDetailsViewModel = new ViewModelProvider(this).get(PlaceDetailsViewModel.class);
        mBookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void observeCurrentUser() {
        FirebaseUser firebaseUser = mUserViewModel.getCurrentUserFromFirebase();
        mUserViewModel.getCurrentUserFromFirestore(firebaseUser.getUid());
        mUserViewModel.getUserLiveData().observe(this, this::getCurrentUser);
    }

    private void getCurrentUser(User user) {
        mUser = user;
        assert mUser.getLikedPlaces() != null;
        mUserLikedPlacesList.addAll(mUser.getLikedPlaces());
    }

    private void observeUsers() {
        mUserViewModel.getUserListFromFirestore();
        mUserViewModel.getUserListLiveData().observe(this, this::getUsers);
    }

    private void getUsers(List<User> users) {
        mUserList = users;
    }

    private void getPlaceDetails() {
        Intent intent = getIntent();
        mPlaceId = intent.getStringExtra("place id");
        mPlaceDetailsViewModel.fetchPlaceDetails(mPlaceId);
        mPlaceDetailsViewModel.getPlaceDetailsLiveData().observe(this, this::initDataPlaceDetails);
    }

    private void initDataPlaceDetails(Result result) {
        //For place photo
        if (result.getPhotos() == null) {
            mBinding.imDetailPlace.setImageResource(R.drawable.drawer_header_background);
        } else {
            String placePhoto = result.getPhotos().get(0).getPhotoReference();
            Glide.with(this)
                    .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + placePhoto + "&key=" + BuildConfig.MAPS_API_KEY)
                    .into(mBinding.imDetailPlace);
        }
        //For place name
        mPlaceName = result.getName();
        mBinding.tvDetailName.setText(mPlaceName);
        //For place address
        mPlaceAddress = result.getFormattedAddress();
        mBinding.tvDetailAddress.setText(result.getFormattedAddress());
        //For place rating
        float rating = (float) ((result.getRating() / 5) * 3);
        mBinding.rbDetailRate.setRating(rating);
        //For booking
        mBinding.fabDetailChoice.setOnClickListener(view -> manageBookingChoice());
        //For place call
        mBinding.btDetailCall.setOnClickListener(view -> {
            if (result.getFormattedPhoneNumber() != null) {
                String phoneNumber = String.format(getString(R.string.phone_number), result.getInternationalPhoneNumber());
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse(phoneNumber));
                startActivity(callIntent);
            } else {
                Toast.makeText(PlaceDetailsActivity.this, R.string.phone_number_unknown, Toast.LENGTH_SHORT).show();
            }
        });
        //For place like
        if (mUserLikedPlacesList.contains(mPlaceId)) {
            mBinding.btDetailLike.setText(R.string.dislike);
        }
        mBinding.btDetailLike.setOnClickListener(view -> manageLikeButton());
        //For place website
        mBinding.btDetailWebsite.setOnClickListener(view -> {
            if (result.getWebsite() != null) {
                Uri uri = Uri.parse(result.getWebsite());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(websiteIntent);
            } else {
                Toast.makeText(PlaceDetailsActivity.this, R.string.unknown_website, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeBookings() {
        mBookingViewModel.fetchBookingList();
        mBookingViewModel.getBookingListLiveData().observe(this, this::getBookings);
    }

    private void getBookings(List<Booking> bookings) {
        mBookingList = bookings;
        manageFABColor();
        updateRecyclerView();
    }

    private void manageFABColor() {
        if (isUserHasBooking()) {
            manageBookingFAB(mUserBooking.getPlaceId().equalsIgnoreCase(mPlaceId));
        } else {
            manageBookingFAB(false);
        }
    }

    private boolean isUserHasBooking() {
        if (!mBookingList.isEmpty()) {
            for (Booking booking : mBookingList) {
                if (booking.getUser().equalsIgnoreCase(mUser.getName())) {
                    mUserBooking = booking;
                    return true;
                } else {
                    Log.e("Anne", "isUserHasBooking : returnFalseUserNOEquals");
                }
            }
        }
        return false;
    }

    private void updateRecyclerView() {
        mJoiningWorkmatesString.clear();
        mJoiningWorkmatesUsers.clear();
        if (!mBookingList.isEmpty()) {
            for (Booking booking : mBookingList) {
                if (mPlaceId.equals(booking.getPlaceId())) {
                    mJoiningWorkmatesString.add(booking.getUser());
                }
            }
        }
        if (!mJoiningWorkmatesString.isEmpty()) {
            for (String userString : mJoiningWorkmatesString) {
                for (User user : mUserList) {
                    if (userString.equalsIgnoreCase(user.getName())) {
                        mJoiningWorkmatesUsers.add(user);
                    }
                }
            }
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(new JoiningWorkmatesListAdapter(mJoiningWorkmatesUsers));
        } else {
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    private void manageBookingChoice() {
        if (!(mUserBooking == null)) {
            if (mUserBooking.getPlaceId().equalsIgnoreCase(mPlaceId)) {
                mBookingViewModel.deleteBooking(mUserBooking);
                Toast.makeText(PlaceDetailsActivity.this, R.string.booking_delete, Toast.LENGTH_SHORT).show();
                manageBookingFAB(false);
                mUserBooking = null;
                updateRecyclerView();
            } else {
                mBookingViewModel.deleteBooking(mUserBooking);
                createBooking();
            }
        } else {
            createBooking();
        }
    }

    private void createBooking() {
        mBookingViewModel.createBooking(mPlaceId, mPlaceName, mUser.getName(), this);
        Toast.makeText(PlaceDetailsActivity.this, R.string.booking_done, Toast.LENGTH_SHORT).show();
        manageBookingFAB(true);
        mRecyclerView.setVisibility(View.VISIBLE);
        updateRecyclerView();
        saveBookingDataForNotificationInSharedPreferences();
    }

    private void saveBookingDataForNotificationInSharedPreferences() {
        mSharedPreferences = PlaceDetailsActivity.this.getSharedPreferences(getString(R.string.user_booking), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(this.getString(R.string.notification_place_name_key), mPlaceName);
        editor.putString(this.getString(R.string.notification_place_address_key), mPlaceAddress);
        editor.putString(this.getString(R.string.notification_joining_workmates_key), manageJoiningWorkmatesForSharesPreferences());
        editor.apply();
    }

    private String manageJoiningWorkmatesForSharesPreferences() {
        List<User> joiningWorkmatesForNotification = mJoiningWorkmatesUsers;
        joiningWorkmatesForNotification.remove(mUser);
        List<String> joiningWorkmatesForNotificationListString = new ArrayList<>();
        for (User user : joiningWorkmatesForNotification) {
            joiningWorkmatesForNotificationListString.add(user.getName());
        }
        return Arrays.toString(joiningWorkmatesForNotificationListString.toArray()).replace("[", " ").replace("]", "  ");
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void manageBookingFAB(boolean isBooking) {
        if (isBooking) {
            mBinding.fabDetailChoice.setImageDrawable(getDrawable(R.drawable.ic_baseline_check_circle_24));
        } else {
            mBinding.fabDetailChoice.setImageDrawable(getDrawable(R.drawable.check_circle_grey));
        }
    }

    private void manageLikeButton() {
        if (mUserLikedPlacesList.contains(mPlaceId)) {
            mBinding.btDetailLike.setText(R.string.like);
            mUserViewModel.removeALikedPlace(mPlaceId, mUser.getUserId());
        } else {
            mBinding.btDetailLike.setText(R.string.dislike);
            mUserViewModel.addALikedPlace(mPlaceId, mUser.getUserId());
        }
    }

    private void navigateToPlacesActivity() {
        Intent intent = new Intent(PlaceDetailsActivity.this, PlacesActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mUserViewModel.getUserListLiveData().removeObservers(this);
        mPlaceDetailsViewModel.getPlaceDetailsLiveData().removeObservers(this);
        mBookingViewModel.getBookingListLiveData().removeObservers(this);
    }
}
