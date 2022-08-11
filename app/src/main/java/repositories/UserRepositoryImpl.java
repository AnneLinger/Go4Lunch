package repositories;

import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;
import javax.inject.Inject;

import model.User;
import ui.activities.PlacesActivity;
import ui.activities.SettingsActivity;

/**
*Implementation of UserRepository interface
*/

public class UserRepositoryImpl implements UserRepository {
    private final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    @Inject
    public UserRepositoryImpl() {
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return mFirebaseAuth.getCurrentUser();
    }

    //TODO Manage with LiveData and Firestore
    @Override
    public List<FirebaseUser> getAllUsers() {
        return null;
    }

    @Override
    public void createUser() {
        String name = getCurrentUser().getDisplayName();
        String userId = getCurrentUser().getProviderId();
        String pictureUrl = (getCurrentUser().getPhotoUrl() != null ? getCurrentUser().getPhotoUrl().toString() : null);
        User user = new User(userId, name, pictureUrl);
    }

    @Override
    public void logOut() {
        mFirebaseAuth.signOut();
    }
}
