package com.sociosoftware.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.sociosoftware.myapplication.R;
import com.sociosoftware.myapplication.model.ImageModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ImageModel> imageList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public RecyclerViewAdapter(Context context, ArrayList<ImageModel> imageList, OnItemClickListener mListener) {
        mContext = context;
        this.imageList = imageList;
        this.mListener = mListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            this.onItemClickListener = onItemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(getAbsoluteAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageModel currentItem = imageList.get(position);

        String imageUrl = currentItem.getImageUrl();
        Picasso.get().load(imageUrl).fit().centerInside().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

}
