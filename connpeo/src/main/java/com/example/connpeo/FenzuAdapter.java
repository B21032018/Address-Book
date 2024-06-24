package com.example.connpeo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

public class FenzuAdapter extends RecyclerView.Adapter<FenzuAdapter.RecentlyViewedViewHolder> {
    Context context;
    List<Fenzued> ConnpeoFenzuedList = new ArrayList<>();
    private Bitmap bitmap;
    private Bitmap imgBitmap = null;

    public FenzuAdapter(Context context, List<Fenzued> luntanFenzuedList) {
        this.context = context;
        this.ConnpeoFenzuedList = luntanFenzuedList;
    }

    @NonNull
    @Override
    public RecentlyViewedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recently_viewed_items1, parent, false);
        RecentlyViewedViewHolder viewHolder = new RecentlyViewedViewHolder(view);
        return new RecentlyViewedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentlyViewedViewHolder holder, final int position) {
        final Fenzued user = ConnpeoFenzuedList.get(position);
        holder.name.setText((CharSequence) ConnpeoFenzuedList.get(position).getName());
        String url = (String) ConnpeoFenzuedList.get(position).getimg();
        Glide.with(holder.img.getContext()).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imgBitmap = resource;
                holder.img.setImageBitmap(imgBitmap);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Perdet.class);
                i.putExtra("name", (CharSequence) ConnpeoFenzuedList.get(position).getName());
                i.putExtra("beiyong", (CharSequence) ConnpeoFenzuedList.get(position).getbeiyong());
                i.putExtra("tel", (String) ConnpeoFenzuedList.get(position).gettel());
                i.putExtra("img", (String) ConnpeoFenzuedList.get(position).getimg());
                i.putExtra("fenzu", (String) ConnpeoFenzuedList.get(position).getfenzu());
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return ConnpeoFenzuedList.size();
    }

    public static class RecentlyViewedViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView img;

        public RecentlyViewedViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            img = itemView.findViewById(R.id.img);
        }
    }

}