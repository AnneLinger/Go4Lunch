package ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
*Activity to display details of a place
*/

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
    private String placeId;
    private List<String> mJoiningWorkmatesString = new ArrayList<>();
    private List<User> mJoiningWorkmatesUsers = new ArrayList<>();
    private Booking mUserBooking;
    private List<User> mUserList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureActionBar();
        configureViewModels();
        observeCurrentUser();
        observeUsers();
        getPlaceDetails();
        observeBookings();
    }

    //Configure UI
    private void initUi() {
        mBinding = ActivityPlaceDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    private void configureActionBar() {
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToPlacesActivity();
            }
        });
    }

    private void initRecyclerView() {
        Log.e("Anne", "initRV");
        mRecyclerView = mBinding.rvDetailWorkmates;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL));
        updateJoiningWorkmatesUsersList();
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
        Log.e("Anne", "GetCurrentUser : " + mUser.getName());
    }

    private void observeUsers() {
        mUserViewModel.getUserListFromFirestore();
        mUserViewModel.getUserListLiveData().observe(this, this::getUsers);
    }

    private void getUsers(List<User> users) {
        mUserList = users;
        Log.e("Anne", "getUserActivity : " + mUserList.toString());
        Log.e("Anne", mUserList.get(0).getName());
        Log.e("Anne", mUserList.get(0).toString());
    }

    private void getPlaceDetails() {
        Log.e("Anne", "getPlaceDetails");
        Intent intent = getIntent();
        placeId = intent.getStringExtra("place id");
        mPlaceDetailsViewModel.fetchPlaceDetails(placeId);
        mPlaceDetailsViewModel.getPlaceDetailsLiveData().observe(this, this::initDataPlaceDetails);
    }

    private void initDataPlaceDetails(Result result) {
            //For place photo
            if(result.getPhotos()==null){
                mBinding.imDetailPlace.setImageResource(R.drawable.drawer_header_background);
            }
            else {
                String placePhoto = result.getPhotos().get(0).getPhotoReference();
                Glide.with(this)
                        .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + placePhoto + "&key=" + BuildConfig.MAPS_API_KEY)
                        .into(mBinding.imDetailPlace);
            }
            //For place name
            mBinding.tvDetailName.setText(result.getName());
            //For place address
            mBinding.tvDetailAddress.setText(result.getFormattedAddress());
            //For place rating
            float rating = (float) ((result.getRating()/5)*3);
            mBinding.rbDetailRate.setRating(rating);
            //For booking
            mBinding.fabDetailChoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manageBookingChoice();
                }
            });
            //For place call
            mBinding.btDetailCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(result.getFormattedPhoneNumber()!=null) {
                        String phoneNumber = String.format(getString(R.string.phone_number), result.getInternationalPhoneNumber());
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse(phoneNumber));
                        startActivity(callIntent);
                    }
                    else{
                        Toast.makeText(PlaceDetailsActivity.this, R.string.phone_number_unknown, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //For place like
            mBinding.btDetailLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            //For place website
            mBinding.btDetailWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(result.getWebsite()!=null) {
                        Uri uri = Uri.parse(result.getWebsite());
                        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(websiteIntent);
                    }
                    else{
                        Toast.makeText(PlaceDetailsActivity.this, R.string.unknown_website, Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void observeBookings() {
        Log.e("Anne", "observeBookingsInPlaceDetails");
        mBookingViewModel.fetchBookingList();
        mBookingViewModel.getBookingListLiveData().observe(this, this::getUserBooking);
    }

    private void getUserBooking(List<Booking> bookings) {
        Log.e("Anne", bookings.toString());
        mBookingList = bookings;
        manageFABColor();
        initRecyclerView();
    }

    private void manageFABColor() {
        if (isUserHasBooking()) {
            Log.e("Anne", placeId);
            Log.e("Anne", mUserBooking.getPlaceId());
            if(mUserBooking.getPlaceId().equalsIgnoreCase(placeId)) {
                Log.e("Anne", "getUserBooking : userHasThisBooking");
                manageBookingFAB(true);
            }
            else {
                Log.e("Anne", "getUserBooking : userHasNOThisBooking");
                manageBookingFAB(false);
            }
        }
        else {
            Log.e("Anne", "getUserBooking : userHasNOBooking");
            manageBookingFAB(false);
        }
    }

    private boolean isUserHasBooking() {
        Log.e("Anne", "isUserHasBooking");
        if(!mBookingList.isEmpty()) {
            Log.e("Anne", "isUserHasBooking : listIsNOEmpty");
            for (Booking booking : mBookingList) {
                Log.e("Anne", "getUserBooking : " + booking.getUser());
                Log.e("Anne", "getUserBooking : " + mUser.getName());
                if (booking.getUser().equalsIgnoreCase(mUser.getName())) {
                    Log.e("Anne", "isUserHasBooking : userEqualsBookingUser");
                    mUserBooking = booking;
                    return true;
                }
                else {
                    Log.e("Anne", "isUserHasBooking : returnFalseUserNOEquals");
                    //return false;
                }
            }
        }
        Log.e("Anne", "isUserHasBooking : returnFalseListEmpty");
        return false;
    }

    //To manage user booking wish
    private void manageBookingChoice() {
        Log.e("Anne", "manageBookingChoice");
        if(!(mUserBooking==null)) {
            Log.e("Anne", "manageBookingChoice : UserHasABooking");
            if(mUserBooking.getPlaceId().equalsIgnoreCase(placeId)) {
                mBookingViewModel.deleteBooking(mUserBooking);
                Toast.makeText(PlaceDetailsActivity.this, R.string.booking_delete, Toast.LENGTH_SHORT).show();
                manageBookingFAB(false);
                mUserBooking = null;
                mJoiningWorkmatesString.remove(mUser.getName());
                updateJoiningWorkmatesUsersList();
            }
            else {
                mBookingViewModel.deleteBooking(mUserBooking);
                createBooking();
            }
        }
        else {
            Log.e("Anne", "manageBookingChoice : UserHasNOBooking");
            createBooking();
        }
    }

    private void createBooking() {
        Log.e("Anne", "CreateBooking");
        mBookingViewModel.createBooking(placeId, mUser.getName());
        Toast.makeText(PlaceDetailsActivity.this, R.string.booking_done, Toast.LENGTH_SHORT).show();
        manageBookingFAB(true);

        mJoiningWorkmatesString.add(mUser.getName());
        updateJoiningWorkmatesUsersList();
    }

    private void updateJoiningWorkmatesUsersList() {
        if(!isPlaceHasBooking()) {
            Log.e("Anne", "PlaceHasNOBooking");
            mRecyclerView.setAdapter(new JoiningWorkmatesListAdapter());
        }

        else {
            Log.e("Anne", "PlaceHasBooking");
            Log.e("Anne", mJoiningWorkmatesString.toString());
            Log.e("Anne", mUserList.get(0).getName());
            for(String userString : mJoiningWorkmatesString) {
                for(User user : mUserList) {
                    if(userString.equalsIgnoreCase(user.getName())) {
                        mJoiningWorkmatesUsers.add(user);
                    }
                }
            }
            mRecyclerView.setAdapter(new JoiningWorkmatesListAdapter(mJoiningWorkmatesUsers));
        }
    }

    private boolean isPlaceHasBooking() {
        if(!mBookingList.isEmpty()) {
            Log.e("Anne", "BookingIsNOEmpty");
            for (Booking booking : mBookingList) {
                if (booking.getPlaceId().equalsIgnoreCase(placeId)) {
                    Log.e("Anne", "BookingIsNOEmptySoAddAWorkmateJoining");
                    String joiningWorkmate = booking.getUser();
                    mJoiningWorkmatesString.add(joiningWorkmate);
                    return true;
                }
            }
        }
        Log.e("Anne", "BookingIsEmpty");
        return false;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void manageBookingFAB(boolean isBooking){
        if(isBooking){
            mBinding.fabDetailChoice.setImageDrawable(getDrawable(R.drawable.ic_baseline_check_circle_24));
        }
        else {
            mBinding.fabDetailChoice.setImageDrawable(getDrawable(R.drawable.check_circle_grey));
        }
    }

    //When click on action bar for back
    private void navigateToPlacesActivity() {
        Intent intent = new Intent(PlaceDetailsActivity.this, PlacesActivity.class);
        startActivity(intent);
        finish();
    }
}
