package repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import model.Booking;

/**
*Implementation of BookingRepository interface
*/

public class BookingRepositoryImpl implements BookingRepository {

    //For data
    private FirebaseFirestore mFirestore;
    private final MutableLiveData<List<Booking>> mBookingList = new MutableLiveData<>();
    private static final String COLLECTION = "Bookings";
    private static final String BOOKING_ID = "BookingId";
    private static final String PLACE_ID = "PlaceId";
    private static final String USER = "User";

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
        /**mFirestore.collection(COLLECTION).addSnapshotListener((value, error) -> {
            if(error!=null){
                Log.e("Anne", "collectionError");
                return;
            }
            if (value!=null) {
                Log.e("Anne", "getCollectionOK");
                Log.e("Anne", value.toString());
                List<Booking> bookingList = value.toObjects(Booking.class);
                mBookingList.setValue(bookingList);
                Log.e("Anne", mBookingList.toString());
            }
            else{
                Log.e("Anne", "collectionValueNull");
            }
        });*/

        mFirestore.collection(COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null) {
                    Log.e("Anne", "getCollectionError");
                    return;
                }

                List<Booking> bookings = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : value){
                    Booking booking = new Booking();
                    if(documentSnapshot.get("User") != null) {
                        booking.setUser(documentSnapshot.getString("User"));
                    }
                    if(documentSnapshot.get("BookingId") != null) {
                        booking.setBookingId(documentSnapshot.getString("BookingId"));
                    }
                    if(documentSnapshot.get("PlaceId") != null) {
                        booking.setPlaceId(documentSnapshot.getString("PlaceId"));
                    }
                    bookings.add(booking);
                }
                Log.e("Anne", "queryListenerRepo : bookings : " + bookings.toString());
                mBookingList.setValue(bookings);
            }
        });
    }

    @Override
    public void createBooking(String bookingId, String placeId, String user) {
        instanceFirestore();
        //Create a new booking
        Map<String, Object> newBooking = new HashMap<>();
        newBooking.put(BOOKING_ID, bookingId);
        newBooking.put(PLACE_ID, placeId);
        newBooking.put(USER, user);

        //Create a new document
        CollectionReference bookingCollection = mFirestore.collection(COLLECTION);
        bookingCollection
                .add(newBooking)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e("Anne", "setCollectionOnSuccess");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Anne", "setCollectionOnFailure");
                    }
                });
    }

    @Override
    public void updateBooking() {
    }

    @Override
    public void deleteBooking(Booking booking) {
    }
}
