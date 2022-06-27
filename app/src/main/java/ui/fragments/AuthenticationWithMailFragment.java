package ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.anne.linger.go4lunch.R;
import com.anne.linger.go4lunch.databinding.FragmentAuthenticationWithMailBinding;

/**
*DialogFragment for login with mail
*/
public class AuthenticationWithMailFragment extends DialogFragment {

    //For UI
    private FragmentAuthenticationWithMailBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_authentication_with_mail, container, false);
    }
}
