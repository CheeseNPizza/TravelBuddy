package my.edu.utar.groupassignment.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import my.edu.utar.groupassignment.DetailActivity;
import my.edu.utar.groupassignment.R;
import my.edu.utar.groupassignment.structure.Place;

import java.io.Serializable;
import java.util.ArrayList;

public class PlaceViewAdapter extends RecyclerView.Adapter<PlaceViewAdapter.ViewHolder> {

    ArrayList<Place> placeArrayList;

    public PlaceViewAdapter(ArrayList<Place> placeArrayList){
        this.placeArrayList = placeArrayList;
    }

    @Override
    public PlaceViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(PlaceViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.text_name.setText(placeArrayList.get(position).getName());
        holder.text_rating.setText(placeArrayList.get(position).getRating());

        Glide.with(holder.itemView.getContext())
                .load(placeArrayList.get(position).getPhoto())
                .into(holder.photo);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("object", placeArrayList.get(position));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return placeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView text_name, text_rating;
        ImageView photo;
        public ViewHolder(View itemView) {
            super(itemView);

            text_name = itemView.findViewById(R.id.detail_name);
            text_rating = itemView.findViewById(R.id.place_rating);
            photo = itemView.findViewById(R.id.place_photo);
        }
    }
}
