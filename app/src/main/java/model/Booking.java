package model;

import java.util.Date;

/**
*Model for bookings made by users
*/

public class Booking {
    private String bookingId;
    private String placeId;
    private String placeName;
    private String user;
    private Date bookingDate;


    public Booking(){
    }

    public Booking(String bookingId, String placeId, String placeName, String user, Date bookingDate) {
        this.bookingId = bookingId;
        this.placeId = placeId;
        this.placeName = placeName;
        this.user = user;
        this.bookingDate = bookingDate;
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

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }
}
