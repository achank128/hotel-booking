package com.turquoise.hotelbookrecomendation.model;

public class Book {
    private String msg;
    private Booking booking;

    public Book() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
