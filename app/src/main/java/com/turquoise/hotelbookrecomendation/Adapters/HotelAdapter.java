package com.turquoise.hotelbookrecomendation.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.turquoise.hotelbookrecomendation.Activities.HotelInfoActivity;
import com.turquoise.hotelbookrecomendation.R;
import com.turquoise.hotelbookrecomendation.model.Hotel;
import com.turquoise.hotelbookrecomendation.model.HotelResult;
import com.turquoise.hotelbookrecomendation.model.User;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private View view;
    private HotelViewHolder hotelViewHolder;
    private HotelResult hotelResult = new HotelResult();

    private List<Hotel> hotels;
    private User user;

    public HotelAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setHotels(List<Hotel> lists) {
        this.hotels = lists;
        hotelResult.setHotels(hotels);
        HotelResult hotelResult = new HotelResult();
        hotelResult.setHotels(hotels);
        storeUpdates(hotelResult);
        notifyDataSetChanged();
    }

    public void storeUpdates(HotelResult hotelResult) {
        SharedPreferences.Editor spe = context.getSharedPreferences("hotel", Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        spe.putString("data", gson.toJson(hotelResult));
        spe.apply();

    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.hotelcard, parent, false);
        hotelViewHolder = new HotelViewHolder(view);

        return hotelViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, final int position) {
        Picasso
                .with(context)
                .load(Uri.parse(hotels.get(position).getImageUrl()))
                .into(holder.hotelImage);
        holder.hotelRatings.setText(hotels.get(position).getRatings());
        holder.tags.setText(hotels.get(position).getTags());
        holder.hotelName.setText(hotels.get(position).getName());
        holder.hotelViews.setText(hotels.get(position).getVisits() + "\nViews");
        holder.bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int vis = Integer.valueOf(hotels.get(position).getVisits());
                hotels.get(position).setVisits(String.valueOf(++vis));
                setHotels(hotels);
                Intent intent = new Intent(context, HotelInfoActivity.class);
                intent.putExtra("hotels", hotelResult);
                intent.putExtra("pos", position);
                intent.putExtra("data", hotels.get(position));

                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);

                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return hotels.size();
    }

    class HotelViewHolder extends RecyclerView.ViewHolder {

        ImageView hotelImage;
        TextView hotelRatings, hotelName, hotelViews;
        TextView tags;
        Button bookButton;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelImage = itemView.findViewById(R.id.hotelImage);
            hotelRatings = itemView.findViewById(R.id.ratings);
            bookButton = itemView.findViewById(R.id.hotelBookButton);
            tags = itemView.findViewById(R.id.tagsList);
            hotelName = itemView.findViewById(R.id.hotelName);
            hotelViews = itemView.findViewById(R.id.hotelCardViews);
        }
    }

    public void setUser(User user) {
        this.user = user;
    }
}
