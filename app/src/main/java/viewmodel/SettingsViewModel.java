package viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import repositories.UserRepositoryImpl;

/**
*ViewModel for settings
*/

@HiltViewModel
public class SettingsViewModel extends ViewModel {
    //For data
    private final UserRepositoryImpl mUserRepositoryImpl;

    //Constructor
    @Inject
    public SettingsViewModel(UserRepositoryImpl userRepositoryImpl) {
        mUserRepositoryImpl = userRepositoryImpl;
    }

    public FirebaseUser getCurrentUser() {
        return mUserRepositoryImpl.getCurrentUserFromFirebase();
    }
}
