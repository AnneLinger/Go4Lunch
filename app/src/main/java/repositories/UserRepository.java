package repositories;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import model.User;

/**
 *Interface repository for users and their authentication
 */

public interface UserRepository {

    void instanceFirestore();

    FirebaseUser getCurrentUser();

    LiveData<List<User>> getUserListLiveData();

    void getUserListFromFirestore();

    List<FirebaseUser> getAllUsers();

    void createUser();

    void logOut();

    void addALikedPlace(String placeId, String user);

    void removeALikedPlace(String placeId, String user);

}
