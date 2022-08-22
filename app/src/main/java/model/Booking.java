package model;

/**
*Model for bookings made by users
*/

public class Booking {
    private String BookingId;
    private String PlaceId;
    private String User;


    public Booking(){
    }

    public Booking(String bookingId, String placeId, String user) {
        this.BookingId = bookingId;
        this.PlaceId = placeId;
        this.User = user;
    }

    public String getBookingId() {
        return BookingId;
    }

    public void setBookingId(String bookingId) {
        this.BookingId = bookingId;
    }

    public String getPlaceId() {
        return PlaceId;
    }

    public void setPlaceId(String placeId) {
        this.PlaceId = placeId;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        this.User = user;
    }
}
