package viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import repositories.UserRepositoryImpl;

/**
*ViewModel for users
*/

@HiltViewModel
public class UserViewModel extends ViewModel {
    //For data
    private final UserRepositoryImpl mUserRepositoryImpl;

    //For threads
    //private final Executor mExecutor;

    //Constructor
    @Inject
    public UserViewModel(UserRepositoryImpl userRepositoryImpl) {
        mUserRepositoryImpl = userRepositoryImpl;
        //mExecutor = executor;
    }

    //..........................For authentication...............................................

    public FirebaseUser getCurrentUser() {
        return mUserRepositoryImpl.getCurrentUser();
    }

    public void createUser() {
        mUserRepositoryImpl.createUser();
    }

    public void logOut() {
        mUserRepositoryImpl.logOut();
    }
}
