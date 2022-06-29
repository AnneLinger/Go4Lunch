package repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import model.User;

/**
*Repository for authentication of the users
*/

public class AuthenticationRepository {
    private final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    public AuthenticationRepository() {
    }

    public FirebaseUser getCurrentUser() {
        return mFirebaseAuth.getCurrentUser();
    }

    public void createUser() {
        String name = getCurrentUser().getDisplayName();
        String userId = getCurrentUser().getProviderId();
        String pictureUrl = getCurrentUser().getPhotoUrl().toString();
        User user = new User(userId, name, pictureUrl);
    }

    public void logOut() {
        mFirebaseAuth.signOut();
    }
}
