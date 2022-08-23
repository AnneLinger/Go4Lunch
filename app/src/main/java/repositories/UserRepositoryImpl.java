package repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import model.User;

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
    private static final String LIKED_PLACES = "likedPlaces";


    @Inject
    public UserRepositoryImpl() {
    }

    @Override
    public void instanceFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public FirebaseUser getCurrentFirebaseUser() {
        return mFirebaseAuth.getCurrentUser();
    }

    public String getCurrentFirebaseUserUid() {
        FirebaseUser firebaseUser = getCurrentFirebaseUser();
        return firebaseUser.getUid();
    }

    public CollectionReference getUserCollection() {
        return mFirestore.collection(USER_COLLECTION);
    }

    @Override
    public User getCurrentUser() {
        return null;
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
        Log.e("Anne", "createUserRepo");

        instanceFirestore();

        FirebaseUser firebaseUser = getCurrentFirebaseUser();

        if(firebaseUser!= null) {
            String userId = firebaseUser.getUid();
            String name = firebaseUser.getDisplayName();
            String pictureUrl = (firebaseUser.getPhotoUrl() != null) ? firebaseUser.getPhotoUrl().toString() : null;

            User newUser = new User(userId, name, pictureUrl, null);

            Task<DocumentSnapshot> userData = getUserData();

            userData.addOnSuccessListener(documentSnapshot -> this.getUserCollection().document(userId).set(newUser));
        }

        //Create a new user
        /**Map<String, Object> newUser = new HashMap<>();
        newUser.put(USER_ID, null);
        newUser.put(NAME, getCurrentFirebaseUser().getDisplayName());
        newUser.put(PICTURE_URL, getCurrentFirebaseUser().getPhotoUrl() != null ? getCurrentFirebaseUser().getPhotoUrl().toString() : null);

        //Create a new document
        CollectionReference userCollection = mFirestore.collection(USER_COLLECTION);
        userCollection
                .add(newUser)
                .addOnSuccessListener(documentReference -> mFirestore.collection(USER_COLLECTION)
                        .document(documentReference.getId())
                        .update(USER_ID, getCurrentFirebaseUser().getUid())
                        .addOnSuccessListener(unused -> {
                            Log.e("Anne", "setUserCollectionOnSuccess");
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Anne", "setUserCollectionOnFailure");
                            }
                        }));*/
        /**String name = getCurrentUser().getDisplayName();
        String userId = getCurrentUser().getProviderId();
        String pictureUrl = (getCurrentUser().getPhotoUrl() != null ? getCurrentUser().getPhotoUrl().toString() : null);
        User user = new User(userId, name, pictureUrl, null);*/
    }

    public Task<DocumentSnapshot> getUserData() {
        //assert this.getCurrentUser() != null;
        String uId = this.getCurrentFirebaseUser().getUid();
        return this.getUserCollection().document(uId).get();
    }

    @Override
    public void logOut() {
        mFirebaseAuth.signOut();
    }

    @Override
    public void deleteAccount(Context context) {
        FirebaseUser userToDelete = getCurrentFirebaseUser();
        //Delete in Firebase auth
        AuthUI.getInstance().delete(context);
        //Delete in Firestore
        instanceFirestore();
        Log.e("Anne", userToDelete.getUid());
        mFirestore.collection(USER_COLLECTION).document(userToDelete.getUid())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.e("Anne", "DocumentSnapshot User successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Anne", "Error deleting User document", e);
                    }
                });
    }

    @Override
    public void addALikedPlace(String placeId, String user) {
    }

    @Override
    public void removeALikedPlace(String placeId, String user) {

    }
}
