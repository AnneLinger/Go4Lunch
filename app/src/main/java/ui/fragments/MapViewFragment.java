package ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anne.linger.go4lunch.databinding.FragmentMapViewBinding;

/**
*Fragment to display a map for places
*/
public class MapViewFragment extends Fragment {

    //For UI
    private FragmentMapViewBinding mBinding;

    //For data

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMapViewBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }
}
