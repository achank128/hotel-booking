package com.turquoise.hotelbookrecomendation.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.turquoise.hotelbookrecomendation.Activities.HotelInfoActivity;
import com.turquoise.hotelbookrecomendation.Activities.MainActivity;
import com.turquoise.hotelbookrecomendation.R;
import com.turquoise.hotelbookrecomendation.model.Booking;
import com.turquoise.hotelbookrecomendation.model.Hotel;
import com.turquoise.hotelbookrecomendation.model.HotelResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.HotelViewHolder> implements Serializable {

    private final Context context;
    private final LayoutInflater inflater;
    private View view;
    private HotelViewHolder hotelViewHolder;

    private HotelResult hotelResult = new HotelResult();

    private List<Booking> bookings;

    public RecommendationAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setBookings(List<Booking> b) {
        this.bookings = b;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.hotelcard, parent, false);
        hotelViewHolder = new HotelViewHolder(view);

        return hotelViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HotelViewHolder holder, final int position) {

        Picasso
                .with(context)
                .load(Uri.parse(bookings.get(position).getHotel().getImageUrl()))
                .into(holder.hotelImage);

        holder.hotelRatings.setText(bookings.get(position).getHotel().getRatings());
        holder.hotelName.setText(bookings.get(position).getCheckIn().substring(0, 10));
        holder.tags.setText(bookings.get(position).getCheckOut().substring(0, 10));
        holder.hotelViews.setText(bookings.get(position).getHotel().getName());
        holder.bookButton.setText("View");
        holder.bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HotelInfoActivity.class);
                intent.putExtra("data", bookings.get(position).getHotel());
                intent.putExtra("checkIn", bookings.get(position).getCheckIn());
                intent.putExtra("checkOut", bookings.get(position).getCheckOut());
                intent.putExtra("total", String.valueOf(bookings.get(position).getTotal()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }


    public class HotelViewHolder extends RecyclerView.ViewHolder {

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

    public interface UpdateListener extends Serializable {
        void update();
    }


}
