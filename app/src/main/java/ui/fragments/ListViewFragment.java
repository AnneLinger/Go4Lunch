package ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.FragmentListViewBinding;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

import model.Place;
import ui.adapter.PlaceListAdapter;

/**
*Fragment to display a list of places
*/
public class ListViewFragment extends Fragment {

    //For UI
    private FragmentListViewBinding mBinding;
    private RecyclerView mRecyclerView;

    //For data
    private static List<Place> mPlaceList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentListViewBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = mBinding.rvListView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL));
    }

    private void initPlaceList() {
        //mPlaceList = .getPlaceList;
        mRecyclerView.setAdapter(new PlaceListAdapter(mPlaceList));
    }
}
