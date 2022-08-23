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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import model.User;

/**
*Implementation of UserRepository interface
*/

public class UserRepositoryImpl implements UserRepository {

    private final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mFirestore;
    private final MutableLiveData<List<User>> mUserList = new MutableLiveData<>();
    private final MutableLiveData<User> mUser = new MutableLiveData<>();
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
                Log.w("Anne", "onEvent: Listen failed", error);
                return;
            }

            if (value != null && value.exists()) {
                mUser.setValue(value.toObject(User.class));
            } else {
                Log.i("Anne", "onEvent: data is null");
                createUser();
            }
        });
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

    @Override
    public void createUser() {
        Log.e("Anne", "createUserRepo");

        instanceFirestore();

        FirebaseUser firebaseUser = getCurrentUserFromFirebase();

        if(firebaseUser!= null) {
            String userId = firebaseUser.getUid();
            String name = firebaseUser.getDisplayName();
            String pictureUrl = (firebaseUser.getPhotoUrl() != null) ? firebaseUser.getPhotoUrl().toString() : null;

            User newUser = new User(userId, name, pictureUrl, null);

            Task<DocumentSnapshot> userData = getUserData();

            userData.addOnSuccessListener(documentSnapshot -> this.getUserCollection().document(userId).set(newUser));
        }
    }

    @Override
    public Task<DocumentSnapshot> getUserData() {
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
