package repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import model.User;

/**
 * Implementation of UserRepository interface
 */

public class UserRepositoryImpl implements UserRepository {

    private final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mFirestore;
    private final MutableLiveData<List<User>> mUserList = new MutableLiveData<>();
    private final MutableLiveData<User> mUser = new MutableLiveData<>();
    private static final String USER_COLLECTION = "Users";
    private static final String LIKED_PLACES = "likedPlaces";

    @Inject
    public UserRepositoryImpl() {
    }

    @Override
    public void instanceFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public CollectionReference getUserCollection() {
        return mFirestore.collection(USER_COLLECTION);
    }

    @Override
    public FirebaseUser getCurrentUserFromFirebase() {
        return mFirebaseAuth.getCurrentUser();
    }

    @Override
    public LiveData<User> getUserLiveData() {
        return mUser;
    }

    @Override
    public void getCurrentUserFromFirestore(String userId) {
        instanceFirestore();
        DocumentReference documentReference = mFirestore.collection(USER_COLLECTION).document(userId);

        documentReference.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Anne", "onEvent: Listen failed", error);
                return;
            }

            if (value != null && value.exists()) {
                mUser.setValue(value.toObject(User.class));

            } else {
                Log.e("Anne", "onEvent: data is null");
                createUserInFirestore();
            }
        });
    }

    @Override
    public LiveData<List<User>> getUserListLiveData() {
        return mUserList;
    }

    @Override
    public void getUserListFromFirestore() {
        instanceFirestore();
        mFirestore.collection(USER_COLLECTION).addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Anne", "collectionUserListError");
                return;
            }
            if (value != null) {
                List<User> userList = value.toObjects(User.class);
                mUserList.setValue(userList);
            } else {
                Log.e("Anne", "collectionUserListValueNull");
            }
        });
    }

    @Override
    public void createUserInFirestore() {
        instanceFirestore();

        FirebaseUser firebaseUser = getCurrentUserFromFirebase();

        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            String name = firebaseUser.getDisplayName();
            String pictureUrl = (firebaseUser.getPhotoUrl() != null) ? firebaseUser.getPhotoUrl().toString() : null;
            List<String> likedPlaces = new ArrayList<>();

            User newUser = new User(userId, name, pictureUrl, likedPlaces);

            Task<DocumentSnapshot> userData = getUserId();

            userData.addOnSuccessListener(documentSnapshot -> this.getUserCollection().document(userId).set(newUser));
        }
    }

    @Override
    public Task<DocumentSnapshot> getUserId() {
        String uId = this.getCurrentUserFromFirebase().getUid();
        return this.getUserCollection().document(uId).get();
    }

    @Override
    public void logOut() {
        mFirebaseAuth.signOut();
    }

    @Override
    public void deleteAccount(Context context) {
        FirebaseUser userToDelete = getCurrentUserFromFirebase();

        //Delete in Firebase auth
        AuthUI.getInstance().delete(context);

        //Delete in Firestore
        instanceFirestore();
        mFirestore.collection(USER_COLLECTION).document(userToDelete.getUid())
                .delete()
                .addOnSuccessListener(unused -> Log.e("Anne", "DocumentSnapshot User successfully deleted!"))
                .addOnFailureListener(e -> Log.e("Anne", "Error deleting User document", e));
    }

    @Override
    public void addALikedPlace(String placeId, String userId) {
        mFirestore.collection(USER_COLLECTION).document(userId)
                .update(LIKED_PLACES, FieldValue.arrayUnion(placeId))
                .addOnFailureListener(e -> Log.e("Anne", "AddALikedPlaceFailed", e));
    }

    @Override
    public void removeALikedPlace(String placeId, String userId) {
        mFirestore.collection(USER_COLLECTION).document(userId)
                .update(LIKED_PLACES, FieldValue.arrayRemove(placeId))
                .addOnFailureListener(e -> Log.e("Anne", "RemoveALikedPlaceFailed", e));
    }
}
