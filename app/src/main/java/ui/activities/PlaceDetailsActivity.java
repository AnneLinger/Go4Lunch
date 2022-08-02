package ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
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
    private final List<User> mJoiningWorkmatesList = new ArrayList<>();
    //TODO to replace with good data
    private final User fakeWorkmate = new User("1", "Peter", "https://fakeimg.pl/300/");
    private PlaceDetailsViewModel mPlaceDetailsViewModel;
    private BookingViewModel mBookingViewModel;
    private UserViewModel mUserViewModel;
    private List<Booking> mBookingList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureActionBar();
        configureViewModels();
        getPlaceDetails();
        observeBookings();
    }

    //Configure UI
    private void initUi() {
        mBinding = ActivityPlaceDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initRecyclerView();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void manageBookingFAB(boolean isBooking){
        if(isBooking){
            mBinding.fabDetailChoice.setImageDrawable(getDrawable(R.drawable.ic_baseline_check_circle_24));
        }
        else mBinding.fabDetailChoice.setImageDrawable(getDrawable(R.drawable.check_circle_grey));
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
        Log.d("Anne", "initRV");
        mJoiningWorkmatesList.add(fakeWorkmate);
        mRecyclerView = mBinding.rvDetailWorkmates;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL));
        mRecyclerView.setAdapter(new JoiningWorkmatesListAdapter(mJoiningWorkmatesList));
    }

    private void configureViewModels() {
        mPlaceDetailsViewModel = new ViewModelProvider(this).get(PlaceDetailsViewModel.class);
        mBookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void getPlaceDetails() {
        Log.e("Anne", "getPlaceDetails");
        Intent intent = getIntent();
        mPlaceDetailsViewModel.fetchPlaceDetails(intent.getStringExtra("place id"));
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
                    manageBooking(result.getPlaceId());
                }
            });
            //For place call
                mBinding.btDetailCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(result.getFormattedPhoneNumber()!=null) {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse(result.getFormattedPhoneNumber()));
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
        mBookingViewModel.getBookingListLiveData().observe(this, new Observer<List<Booking>>() {
            @Override
            public void onChanged(List<Booking> bookings) {
                mBookingList = bookings;
            }
        });
        if(!mBookingList.isEmpty()){
            for(Booking booking : mBookingList){
                for(FirebaseUser firebaseUser : booking.getUserList()){
                    if(firebaseUser==mUserViewModel.getCurrentUser()){
                        manageBookingFAB(true);
                    }
                }
            }
        }

    }

    //To manage user booking wish
    private void manageBooking(String placeId) {
        if(!mBookingList.isEmpty()) {
            for (Booking booking : mBookingList) {
                if (booking.getPlaceId() == placeId) {
                    booking.getUserList().add(mUserViewModel.getCurrentUser());
                    //TODO call updateBooking to also update in firestore
                }
            }
        }
        else {
            List<FirebaseUser> newUserList = new ArrayList<>();
            newUserList.add(mUserViewModel.getCurrentUser());
            mBookingViewModel.createBooking(mBookingList.size() + 1, placeId, newUserList);
        }
        Toast.makeText(PlaceDetailsActivity.this, R.string.booking_done, Toast.LENGTH_SHORT).show();
        manageBookingFAB(true);
    }

    //When click on action bar for back
    private void navigateToPlacesActivity() {
        Intent intent = new Intent(PlaceDetailsActivity.this, PlacesActivity.class);
        startActivity(intent);
        finish();
    }
}
