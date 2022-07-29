package ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.ActivityPlaceDetailsBinding;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import model.User;
import model.placedetailspojo.Result;
import ui.adapter.JoiningWorkmatesListAdapter;
import viewmodel.PlaceDetailsViewModel;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureActionBar();
        configureViewModel();
        getPlaceDetails();
    }

    //Configure UI
    private void initUi() {
        mBinding = ActivityPlaceDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initRecyclerView();
    }

    private void configureActionBar() {
        mBinding.settingsToolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        mBinding.settingsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

    private void configureViewModel() {
        mPlaceDetailsViewModel = new ViewModelProvider(this).get(PlaceDetailsViewModel.class);
    }

    private void getPlaceDetails() {
        Log.e("Anne", "getPlaceDetails");
        Intent intent = getIntent();
        mPlaceDetailsViewModel.fetchPlaceDetails(intent.getStringExtra("place id"));
        mPlaceDetailsViewModel.getPlaceDetailsLiveData().observe(this, this::initDataPlaceDetails);
    }

    private void initDataPlaceDetails(Result result) {
        if(result!=null){
            Log.e("Anne", "resultOk");
            //TODO complete with Glide for the photo
            //mBinding.imDetailPlace.setImageURI(result.getPhotos().get(0).getPhotoReference());
            mBinding.tvDetailName.setText(result.getName());
            //TODO comment récupérer le type de restaurant ? ?
            //mBinding.tvDetailType.setText(result.getTypes());
            mBinding.tvDetailAddress.setText(result.getFormattedAddress());
            //TODO ajouter listener sur call...x3
        }
        else {
            Log.e("Anne", "resultNull");
        }
    }

    //When click on action bar for back
    private void navigateToPlacesActivity() {
        Intent intent = new Intent(PlaceDetailsActivity.this, PlacesActivity.class);
        startActivity(intent);
        finish();
    }
}
