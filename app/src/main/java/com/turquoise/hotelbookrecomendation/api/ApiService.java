package com.turquoise.hotelbookrecomendation.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.turquoise.hotelbookrecomendation.model.Book;
import com.turquoise.hotelbookrecomendation.model.Booking;
import com.turquoise.hotelbookrecomendation.model.Hotel;
import com.turquoise.hotelbookrecomendation.model.LoginInfo;
import com.turquoise.hotelbookrecomendation.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.1.25:5000/api/")
//            .baseUrl("https://hotel-booking-app-bevl.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @POST("auth/login")
    Call<LoginInfo> login(@Body User userLogin);

    @POST("auth/register")
    Call<LoginInfo> register(@Body User userLogin);

    @GET("hotels")
    Call<List<Hotel>> getHotels();

    @POST("booking")
    Call<Book> createBooing(@Body Booking booking);

    @GET("booking/user/{userId}")
    Call<List<Booking>> getBooking(@Path("userId") String userId);

    @GET("hotels/{id}")
    Call<Hotel> getHotelbyId(@Path("id") String id);
}
