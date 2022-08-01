package repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    FirebaseFirestore mFirestore;
    private final MutableLiveData<List<Booking>> mBookingList = new MutableLiveData<>();
    private static final String COLLECTION = "Bookings";

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
            assert value != null;
            List<Booking> bookingList = value.toObjects(Booking.class);
            mBookingList.setValue(bookingList);
        }));
    }

    @Override
    public void createBooking(int bookingId, String placeId, List<FirebaseUser> userList) {
        //Create a new booking
        Map<String, Object> newBooking = new HashMap<>();
        newBooking.put("bookingId", bookingId);
        newBooking.put("placeId", placeId);
        newBooking.put("userList", userList);

        //Create a new document
        mFirestore.collection(COLLECTION)
                .add(newBooking)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
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
