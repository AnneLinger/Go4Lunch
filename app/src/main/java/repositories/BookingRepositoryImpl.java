package repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import model.Booking;

/**
*Implementation of BookingRepository interface
*/

public class BookingRepositoryImpl implements BookingRepository {

    //For data
    FirebaseFirestore mFirestore;
    private final MutableLiveData<List<Booking>> mBookingList = new MutableLiveData<>();
    private static final String COLLECTION = "Bookings";
    private static final String BOOKING_ID = "BookingId";
    private static final String PLACE_ID = "PlaceId";
    private static final String USER_LIST = "UserList";

    @Inject
    public BookingRepositoryImpl(){
    }

    @Override
    public void instanceFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public LiveData<List<Booking>> getBookingListLiveData() {
        return mBookingList;
    }

    @Override
    public void getBookingListFromFirestore() {
        mFirestore.collection(COLLECTION).addSnapshotListener(((value, error) -> {
            if(error!=null){
                Log.e("Anne", "collectionError");
            }
            if (value!=null) {
                List<Booking> bookingList = value.toObjects(Booking.class);
                mBookingList.setValue(bookingList);
            }
            else{
                Log.e("Anne", "collectionValueNull");
            }
        }));
    }

    @Override
    public void createBooking(int bookingId, String placeId, List<FirebaseUser> userList) {
        instanceFirestore();
        //Create a new booking
        Map<String, Object> newBooking = new HashMap<>();
        newBooking.put(BOOKING_ID, bookingId);
        newBooking.put(PLACE_ID, placeId);
        newBooking.put(USER_LIST, userList);

        CollectionReference bookingCollection = mFirestore.collection(COLLECTION);

         bookingCollection.get()
                .continueWithTask(new Continuation<QuerySnapshot, Task<List<QuerySnapshot>>>() {
                    @Override
                    public Task<List<QuerySnapshot>> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        List<Task<DocumentReference>> tasks = new ArrayList<>();
                        for(DocumentSnapshot documentSnapshot : task.getResult()) {
                            tasks.add(documentSnapshot.getReference().collection(COLLECTION).add(newBooking));
                        }
                        return Tasks.whenAllSuccess(tasks);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<List<QuerySnapshot>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<QuerySnapshot>> task) {
                        Log.e("Anne", "collectionOnSuccess");

                        List<QuerySnapshot> list = task.getResult();
                        for(QuerySnapshot querySnapshot : list) {
                            for(DocumentSnapshot documentSnapshot : querySnapshot) {
                                Booking booking = documentSnapshot.toObject(Booking.class);
                                List<Booking> bookings = new ArrayList<>();
                                bookings.add(booking);
                                mBookingList.setValue(bookings);
                            }
                        }
                    }
                });

        //Create a new document
        /**CollectionReference bookingCollection = mFirestore.collection(COLLECTION);
        bookingCollection
                .add(newBooking)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e("Anne", "collectionOnSuccess");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Anne", "collectionOnFailure");
                    }
                });
        /**mFirestore.collection(COLLECTION)
                .get()
                .addOnSuccessListener(documentReference -> mFirestore.collection(COLLECTION)
                        .document(documentReference.getId()))
                .addOnFailureListener(e -> Log.e("Anne", "collectionOnFailure" + e.getMessage()));*/

    }

    @Override
    public void updateBooking() {
    }

    @Override
    public void deleteBooking(Booking booking) {
    }
}
