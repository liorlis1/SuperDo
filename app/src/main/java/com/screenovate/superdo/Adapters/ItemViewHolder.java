package com.screenovate.superdo.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.screenovate.superdo.R;


public class ItemViewHolder extends RecyclerView.ViewHolder{

    public ImageView colorImage;
    public TextView weight;
    public TextView name;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        colorImage = itemView.findViewById(R.id.colorImage);
        weight = itemView.findViewById(R.id.weight);
        name = itemView.findViewById(R.id.name);
    }

}
