package repositories;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import model.Booking;
import model.User;
import ui.activities.PlacesActivity;
import ui.activities.SettingsActivity;

/**
*Implementation of UserRepository interface
*/

public class UserRepositoryImpl implements UserRepository {

    private final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mFirestore;
    private final MutableLiveData<List<User>> mUserList = new MutableLiveData<>();
    private static final String USER_COLLECTION = "Users";
    private static final String USER_ID = "userId";
    private static final String NAME = "name";
    private static final String PICTURE_URL = "pictureUrl";
    private static final String LIKED_PLACES = "LikedPlaces";


    @Inject
    public UserRepositoryImpl() {
    }

    @Override
    public void instanceFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return mFirebaseAuth.getCurrentUser();
    }

    @Override
    public LiveData<List<User>> getUserListLiveData() {
        return mUserList;
    }

    @Override
    public void getUserListFromFirestore() {
        mFirestore.collection(USER_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null) {
                    Log.e("Anne", "getUserCollectionError");
                    return;
                }

                List<User> users = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : value){
                    User user = new User();
                    if(documentSnapshot.get("UserId") != null) {
                        user.setUserId(documentSnapshot.getString("UserId"));
                    }
                    if(documentSnapshot.get("BookingId") != null) {
                        user.setName(documentSnapshot.getString("Name"));
                    }
                    if(documentSnapshot.get("PlaceId") != null) {
                        user.setPictureUrl(documentSnapshot.getString("PictureUrl"));
                    }
                    users.add(user);
                }
                Log.e("Anne", "queryListenerRepo : users : " + users.toString());
                mUserList.setValue(users);
            }
        });
    }

    //TODO Manage with LiveData and Firestore
    @Override
    public List<FirebaseUser> getAllUsers() {
        return null;
    }

    @Override
    public void createUser() {

        instanceFirestore();

        //Create a new user
        Map<String, Object> newUser = new HashMap<>();
        newUser.put(USER_ID, null);
        newUser.put(NAME, getCurrentUser().getDisplayName());
        newUser.put(PICTURE_URL, getCurrentUser().getPhotoUrl() != null ? getCurrentUser().getPhotoUrl().toString() : null);

        //Create a new document
        CollectionReference userCollection = mFirestore.collection(USER_COLLECTION);
        userCollection
                .add(newUser)
                .addOnSuccessListener(documentReference -> mFirestore.collection(USER_COLLECTION)
                        .document(documentReference.getId())
                        .update(USER_ID, documentReference.getId())
                        .addOnSuccessListener(unused -> {
                            Log.e("Anne", "setUserCollectionOnSuccess");
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Anne", "setUserCollectionOnFailure");
                            }
                        }));
        /**String name = getCurrentUser().getDisplayName();
        String userId = getCurrentUser().getProviderId();
        String pictureUrl = (getCurrentUser().getPhotoUrl() != null ? getCurrentUser().getPhotoUrl().toString() : null);
        User user = new User(userId, name, pictureUrl, null);*/
    }

    @Override
    public void logOut() {
        mFirebaseAuth.signOut();
    }

    @Override
    public void addALikedPlace(String placeId, String user) {
    }

    @Override
    public void removeALikedPlace(String placeId, String user) {

    }
}
