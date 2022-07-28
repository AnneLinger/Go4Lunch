package model;

import com.google.firebase.auth.FirebaseUser;

import java.util.Date;
import java.util.List;

/**
*Model for bookings made by users
*/
public class Booking {
    private int bookingId;
    private String placeId;
    private List<FirebaseUser> userList;

    public Booking(int bookingId, String placeId, List<FirebaseUser> userList) {
        this.bookingId = bookingId;
        this.placeId = placeId;
        this.userList = userList;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public List<FirebaseUser> getUserList() {
        return userList;
    }

    public void setUserList(List<FirebaseUser> userList) {
        this.userList = userList;
    }
}
