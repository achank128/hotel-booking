package com.turquoise.hotelbookrecomendation.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.turquoise.hotelbookrecomendation.Activities.HotelInfoActivity;
import com.turquoise.hotelbookrecomendation.Activities.MainActivity;
import com.turquoise.hotelbookrecomendation.Adapters.RecommendationAdapter;
import com.turquoise.hotelbookrecomendation.R;
import com.turquoise.hotelbookrecomendation.api.ApiService;
import com.turquoise.hotelbookrecomendation.model.Booking;
import com.turquoise.hotelbookrecomendation.model.Hotel;
import com.turquoise.hotelbookrecomendation.model.HotelResult;
import com.turquoise.hotelbookrecomendation.model.UserHotel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Recommendation extends Fragment {
    private MainActivity mMainActivity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static Set<String> tagSet = new HashSet<>();
    RecommendationAdapter recommendationAdapter;
    TextView placeholderEmpty;
    RecyclerView recyclerView;


    private HotelResult hotelResult;
    private List<Booking> bookings;

    public Recommendation() {
    }

    // TODO: Rename and change types and number of parameters
    public static Recommendation newInstance(String param1, String param2) {
        Recommendation fragment = new Recommendation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        bookings = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_recommendation, container, false);
        placeholderEmpty = view.findViewById(R.id.emptyRecommendationPlaceHolder);
        recyclerView = view.findViewById(R.id.hotelList);
        callApiGetBooking();
        recommendationAdapter = new RecommendationAdapter(getContext());
        recommendationAdapter.setBookings(bookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recommendationAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        callApiGetBooking();
//        if (recommendationAdapter.getItemCount() == 0) {
//            placeholderEmpty.setVisibility(View.VISIBLE);
//        } else {
//            placeholderEmpty.setVisibility(View.INVISIBLE);
//        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("activityresult", "onActivityResult: " + resultCode);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        String sa = "";

        for (String sss : Recommendation.tagSet) {
            sa += sss;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void updateList() {
        if (recommendationAdapter != null) {
            recommendationAdapter.setBookings(bookings);
        }
    }


    private void callApiGetBooking() {
        ApiService.apiService.getBooking(mMainActivity.getUser().getUserID()).enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                bookings = response.body();
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
            }
        });
    }


}
