package model;

import java.util.Date;

/**
*Model for bookings made by users
*/
public class Booking {
    private int bookingId;
    private int restaurantId;
    private int userId;
    private Date date;

    public Booking(int bookingId, int restaurantId, int userId, Date date) {
        this.bookingId = bookingId;
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.date = date;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
