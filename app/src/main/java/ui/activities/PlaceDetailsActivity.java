package ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import ui.adapter.JoiningWorkmatesListAdapter;
import viewmodel.PlacesViewModel;
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
    private PlacesViewModel mPlacesViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureActionBar();
        configureViewModel();
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
        mPlacesViewModel = new ViewModelProvider(this).get(PlacesViewModel.class);
    }

    //When click on action bar for back
    private void navigateToPlacesActivity() {
        Intent intent = new Intent(PlaceDetailsActivity.this, PlacesActivity.class);
        startActivity(intent);
        finish();
    }
}
