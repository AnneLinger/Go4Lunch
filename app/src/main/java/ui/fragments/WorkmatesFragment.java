package ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.anne.linger.go4lunch.databinding.FragmentWorkmatesBinding;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
*Fragment to display workmates
*/
public class WorkmatesFragment extends Fragment {

    //For UI
    private FragmentWorkmatesBinding mBinding;
    private RecyclerView mRecyclerView;

    //For data
    private static List<FirebaseUser> mUserList;

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
