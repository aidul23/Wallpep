package com.aidul23.wallpep;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ExampleViewHolder> {
    private ArrayList<CategoryItem> mList;
    private int selectedPosition = 0;
    private Context context;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public LinearLayout linearLayout;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.TextViewID1);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

    public CategoryAdapter(ArrayList<CategoryItem> examplelist) {
        mList = examplelist;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        context = parent.getContext();
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        CategoryItem currentitem = mList.get(position);
        holder.mTextView1.setText(currentitem.getCategory());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = position;
            }
        });

        if(selectedPosition == position) {
            holder.mTextView1.setBackground(ContextCompat.getDrawable(context,R.drawable.button_solid_background));
            holder.mTextView1.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.mTextView1.setBackground(ContextCompat.getDrawable(context,R.drawable.button_background));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
