package ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anne.linger.go4lunch.databinding.FragmentWorkmatesBinding;

/**
*Fragment to display workmates
*/
public class WorkmatesFragment extends Fragment {

    //For UI
    private FragmentWorkmatesBinding mBinding;

    //For data

    public static WorkmatesFragment newInstance() {
        return new WorkmatesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentWorkmatesBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }
}
