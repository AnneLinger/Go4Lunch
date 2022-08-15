package model;

import com.google.firebase.auth.FirebaseUser;

import java.util.Date;
import java.util.List;

/**
*Model for bookings made by users
*/

public class Booking {
    private String bookingId;
    private String placeId;
    private String user;

    public Booking(){
    }

    public Booking(String bookingId, String placeId, String user) {
        this.bookingId = bookingId;
        this.placeId = placeId;
        this.user = user;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
