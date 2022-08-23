package repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import model.User;

/**
 *Interface repository for users and their authentication
 */

public interface UserRepository {

    void instanceFirestore();

    FirebaseUser getCurrentUserFromFirebase();

    CollectionReference getUserCollection();

    LiveData<User> getUserLiveData();

    void getCurrentUserFromFirestore(String userId);

    LiveData<List<User>> getUserListLiveData();

    void getUserListFromFirestore();

    void createUser();

    Task<DocumentSnapshot> getUserData();

    void logOut();

    void deleteAccount(Context context);

    void addALikedPlace(String placeId, String user);

    void removeALikedPlace(String placeId, String user);

}
