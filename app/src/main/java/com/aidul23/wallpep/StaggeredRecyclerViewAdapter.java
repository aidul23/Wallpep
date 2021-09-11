package com.aidul23.wallpep;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class StaggeredRecyclerViewAdapter extends RecyclerView.Adapter<StaggeredRecyclerViewAdapter.ImageViewHolder> {

    private static final String TAG = "StaggeredRecyclerViewAd";
    private ArrayList<ImageModel> mImages = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public StaggeredRecyclerViewAdapter( ArrayList<ImageModel> mImages) {
        this.mImages = mImages;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, final int position) {
        ImageModel imageModel = mImages.get(position);
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_picture_placeholder);
        Log.d(TAG, "onBindViewHolder: "+mImages.get(position).getImageUrl());
        Glide.with(holder.itemView).load(mImages.get(position).getImageUrl()).apply(requestOptions).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(imageModel);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        RelativeLayout parent_layout;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageId);
            parent_layout = itemView.findViewById(R.id.parent_layout);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
