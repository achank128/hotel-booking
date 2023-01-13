
package com.turquoise.hotelbookrecomendation.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Booking {
    private String userId;
    private String hotelId;
    private String checkIn;
    private String checkOut;
    private int total;

    private Hotel hotel;

    public Booking() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    //    @SerializedName("user_hotel")
//    @Expose
//    private List<UserHotel> userHotels = new ArrayList<>();
//    //todo aaa
//    public List<UserHotel> getUserHotels() {
//        return userHotels;
//    }
//
//    public void setUserHotels(List<UserHotel> userHotels) {
//        this.userHotels = userHotels;
//    }

}
