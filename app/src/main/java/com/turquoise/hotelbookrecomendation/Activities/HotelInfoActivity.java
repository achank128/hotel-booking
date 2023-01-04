package com.turquoise.hotelbookrecomendation.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.turquoise.hotelbookrecomendation.Adapters.RecommendationAdapter;
import com.turquoise.hotelbookrecomendation.R;
import com.turquoise.hotelbookrecomendation.api.ApiService;
import com.turquoise.hotelbookrecomendation.model.Book;
import com.turquoise.hotelbookrecomendation.model.Booking;
import com.turquoise.hotelbookrecomendation.model.Hotel;
import com.turquoise.hotelbookrecomendation.model.HotelResult;
import com.turquoise.hotelbookrecomendation.model.User;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelInfoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView hotelImage;
    private TextView hotelName, hotelDesc, views, tag, completed;
    private Button book;
    private TextView checkIn, checkOut;
    private RecommendationAdapter.HotelViewHolder hotelViewHolder;
    private DatePickerDialog checkInDatePicker, checkOutDatePicker;
    Hotel hotel;
    User user;
    int pos;
    HotelResult hotelResult;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_info);

        toolbar = findViewById(R.id.toolbarInfo);
        hotelImage = findViewById(R.id.hotelImage);
        hotelName = findViewById(R.id.nameHotel);
        hotelDesc = findViewById(R.id.hotelDesc);
        book = findViewById(R.id.confirmBooking);
        views = findViewById(R.id.views);
        tag = findViewById(R.id.tagText);
        completed = findViewById(R.id.completedText);
        checkIn = findViewById(R.id.checkInDate);
        checkOut = findViewById(R.id.checkOutDate);
        initCkIDatePicker();
        initCkODatePicker();

        if(getIntent().getExtras() != null){
            user = (User) getIntent().getExtras().get("user");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        hotel = (Hotel) getIntent().getExtras().getSerializable("data");

        hotelResult = new Gson().fromJson(getHotels(), HotelResult.class);
        pos = getIntent().getExtras().getInt("pos");
        toolbar.setTitle(hotel.getName());

        setSupportActionBar(toolbar);
        Picasso
                .with(HotelInfoActivity.this)
                .load(Uri.parse(hotel.getImageUrl()))
                .into(hotelImage);
        hotelDesc.setText(hotel.getDescription());
        hotelName.setText(hotel.getName());
        views.setText(hotelResult.getHotels().get(pos).getVisits() + " views");
        tag.setText(hotelResult.getHotels().get(pos).getTags());
        completed.setText(hotelResult.getHotels().get(pos).getRoom() + " rooms");
        //Button
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBooking(hotelResult.getHotels().get(pos).getHotelId());
                finish();
            }
        });
        //date
        checkIn.setText(getTodaysDate());
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInDatePicker.show();
            }
        });

        checkOut.setText(getTodaysDate());
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOutDatePicker.show();
            }
        });

    }

    private void initCkIDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                checkIn.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        checkInDatePicker = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private void initCkODatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                checkOut.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        checkOutDatePicker = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }


    private String makeDateString (int day, int month, int year){
        return month + "/" + day + "/" + year;
    }

    public String getHotels() {
        SharedPreferences sp = getSharedPreferences("hotel", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        if (sp.contains("data")) {
            return sp.getString("data", null);
        } else {
            return null;
        }
    }

//    private void setBooking(Boolean complete) {
    private void setBooking(String hotelId) {

        Booking booking = new Booking();
        booking.setUserId(user.getUserID());
        booking.setHotelId(hotelId);
        booking.setCheckIn(checkIn.getText().toString());
        booking.setCheckOut(checkOut.getText().toString());
        callApiBookHotel(booking);
    }

    public void storeUpdates(HotelResult hotelResult) {
        SharedPreferences.Editor spe = getSharedPreferences("hotel", Context.MODE_PRIVATE).edit();
        String save = new Gson().toJson(hotelResult);
        spe.putString("data", save);
        spe.apply();
    }

    private void callApiBookHotel (Booking booking) {

        ApiService.apiService.createBooing(booking).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                Toast.makeText(HotelInfoActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Toast.makeText(HotelInfoActivity.this, "Book hotel fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
