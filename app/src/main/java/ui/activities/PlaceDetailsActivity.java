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
    private String mPlaceId;
    private String mPlaceName;
    private final List<String> mJoiningWorkmatesString = new ArrayList<>();
    private final List<User> mJoiningWorkmatesUsers = new ArrayList<>();
    private Booking mUserBooking;
    private List<User> mUserList = new ArrayList<>();
    private List<String> mUserLikedPlacesList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("Anne", "onCreate");
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

    @Override
    protected void onResume() {
        Log.e("Anne", "onResume");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.e("Anne", "onStart");
        super.onStart();
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
        Log.e("Anne", "GetCurrentUser : " + mUser.getName());
        assert mUser.getLikedPlaces() != null;
        mUserLikedPlacesList.addAll(mUser.getLikedPlaces());
        if(!mUserLikedPlacesList.isEmpty()){
            Log.e("Anne", "GetCurrentUser : " + mUser.getLikedPlaces().toString());
        }
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
        mPlaceId = intent.getStringExtra("place id");
        mPlaceDetailsViewModel.fetchPlaceDetails(mPlaceId);
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
            mPlaceName = result.getName();
            mBinding.tvDetailName.setText(mPlaceName);
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
            if(mUserLikedPlacesList.contains(mPlaceId)) {
                mBinding.btDetailLike.setText(R.string.dislike);
            }
            mBinding.btDetailLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manageLikeButton();
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
        mBookingViewModel.getBookingListLiveData().observe(this, this::getBookings);
    }

    private void getBookings(List<Booking> bookings) {
        Log.e("Anne", bookings.toString());
        mBookingList = bookings;
        manageFABColor();
        updateRecyclerView();
    }

    private void manageFABColor() {
        if (isUserHasBooking()) {
            Log.e("Anne", mPlaceId);
            Log.e("Anne", mUserBooking.getPlaceId());
            if(mUserBooking.getPlaceId().equalsIgnoreCase(mPlaceId)) {
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

    private void updateRecyclerView(){
        Log.e("Anne", "updateRV");
        mJoiningWorkmatesString.clear();
        mJoiningWorkmatesUsers.clear();
        if(!mBookingList.isEmpty()){
            Log.e("Anne", "updateRV : bookingListIsNoEmpty");
            for(Booking booking : mBookingList) {
                if(mPlaceId.equals(booking.getPlaceId()))  {
                    Log.e("Anne", "updateRV : getUserBookingForThisPlace");
                    mJoiningWorkmatesString.add(booking.getUser());
                }
            }
        }
        if(!mJoiningWorkmatesString.isEmpty()) {
            Log.e("Anne", "updateRV : thereAreUserBookingForThisPlace");
            for(String userString : mJoiningWorkmatesString) {
                for(User user : mUserList) {
                    if(userString.equalsIgnoreCase(user.getName())){
                        Log.e("Anne", "updateRV : UserBookingAddToUserList");
                        mJoiningWorkmatesUsers.add(user);
                    }
                }
            }
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(new JoiningWorkmatesListAdapter(mJoiningWorkmatesUsers));
        }
        else {
            Log.e("Anne", "updateRV : NoUserBookingForThisPlace");
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    //To manage user booking wish
    private void manageBookingChoice() {
        Log.e("Anne", "manageBookingChoice");
        if(!(mUserBooking==null)) {
            Log.e("Anne", "manageBookingChoice : UserHasABooking");
            if(mUserBooking.getPlaceId().equalsIgnoreCase(mPlaceId)) {
                mBookingViewModel.deleteBooking(mUserBooking);
                Toast.makeText(PlaceDetailsActivity.this, R.string.booking_delete, Toast.LENGTH_SHORT).show();
                manageBookingFAB(false);
                mUserBooking = null;
                updateRecyclerView();
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
        mBookingViewModel.createBooking(mPlaceId, mPlaceName, mUser.getName(), this);
        Toast.makeText(PlaceDetailsActivity.this, R.string.booking_done, Toast.LENGTH_SHORT).show();
        manageBookingFAB(true);
        mRecyclerView.setVisibility(View.VISIBLE);
        updateRecyclerView();
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

    private void manageLikeButton() {
        Log.e("Anne", "manageLikeButton");
        if(mUserLikedPlacesList.contains(mPlaceId)) {
            Log.e("Anne", "mUserLPLContainsThisPlace");
            mBinding.btDetailLike.setText(R.string.like);
            mUserViewModel.removeALikedPlace(mPlaceId, mUser.getUserId());
        }
        else {
            Log.e("Anne", "mUserLPLisDoesNOTContainsThisPlace");
            mBinding.btDetailLike.setText(R.string.dislike);
            mUserViewModel.addALikedPlace(mPlaceId, mUser.getUserId());
        }
    }

    //When click on action bar for back
    private void navigateToPlacesActivity() {
        Intent intent = new Intent(PlaceDetailsActivity.this, PlacesActivity.class);
        startActivity(intent);
        finish();
    }
}
