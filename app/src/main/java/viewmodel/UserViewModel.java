package viewmodel;

import androidx.lifecycle.ViewModel;

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

    public void loginWithMail(String mail, String password) {
        mAuthenticationRepository.loginWithMail(mail, password);
    }

    public void createUserWithMail(String name, String mail, String password) {
        mAuthenticationRepository.createUserWithMail(name, mail, password);
    }
}
