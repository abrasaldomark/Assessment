package com.sociosoftware.myapplication.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sociosoftware.myapplication.R;
import com.sociosoftware.myapplication.model.ImageModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageListHistoryRecyclerViewAdapter extends RecyclerView.Adapter<ImageListHistoryRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ImageModel> imageList;






    public ImageListHistoryRecyclerViewAdapter(Context context, ArrayList<ImageModel> imageList) {
        mContext = context;
        this.imageList = imageList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView imageUrl;
        public TextView dateTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            imageUrl = itemView.findViewById(R.id.image_url);
            dateTime = itemView.findViewById(R.id.date_time);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.image_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ImageModel currentImageItem = imageList.get(position);

        String imageUrl = currentImageItem.getImageUrl();
        String imageUrlString = "<b>Image URL:</b> " + currentImageItem.getImageUrl();
        String date = currentImageItem.getCurrentDate();
        String time = currentImageItem.getCurrentTime();
        String dateTime ="<b>Date and Time:</b> " + date + " " + time;

        Picasso.get().load(imageUrl).fit().centerInside().into(holder.imageView);
        holder.imageUrl.setText(Html.fromHtml(imageUrlString));
        holder.dateTime.setText(Html.fromHtml(dateTime));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
