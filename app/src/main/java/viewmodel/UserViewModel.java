package viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

import repository.AuthenticationRepository;

/**
*ViewModel for users
*/
public class UserViewModel extends ViewModel {
    //For data
    private final AuthenticationRepository mAuthenticationRepository;

    //For threads
    private final Executor mExecutor;

    //Constructor
    public UserViewModel(AuthenticationRepository authenticationRepository, Executor executor) {
        mAuthenticationRepository = authenticationRepository;
        mExecutor = executor;
    }

    //..........................For authentication...............................................

    public FirebaseUser getCurrentUser() {
        return mAuthenticationRepository.getCurrentUser();
    }

    public void createUser() {
        mAuthenticationRepository.createUser();
    }

    public void logOut() {
        mAuthenticationRepository.logOut();
    }
}
