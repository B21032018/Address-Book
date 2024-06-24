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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

// 适配器类，继承自RecyclerView.Adapter
public class RecentlyViewedAdapter extends RecyclerView.Adapter<RecentlyViewedAdapter.RecentlyViewedViewHolder> {
    Context context; // 上下文对象
    // 存储RecentlyViewed对象的列表，每个对象代表列表中的一项
    List<RecentlyViewed> luntanRecentlyViewedList = new ArrayList<>();
    //用于临时存储加载的图片资源
    private Bitmap imgBitmap = null;

    // 构造方法,初始化适配器的上下文和数据列表
    public RecentlyViewedAdapter(Context context, List<RecentlyViewed> luntanRecentlyViewedList) {
        this.context = context;
        this.luntanRecentlyViewedList = luntanRecentlyViewedList;
    }

    // 创建ViewHolder,使用LayoutInflater从XML布局文件recently_viewed_items加载视图，并将其包裹在一个RecentlyViewedViewHolder实例中返回
    @NonNull
    @Override
    public RecentlyViewedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recently_viewed_items, parent, false);
        RecentlyViewedViewHolder viewHolder = new RecentlyViewedViewHolder(view);
        return new RecentlyViewedViewHolder(view);
    }

    // 将数据绑定到ViewHolder的视图上。
    // 这里包括设置TextView的内容、使用Glide库异步加载并显示图片、
    // 设置item的点击监听器以启动新的Activity并传递数据，以及根据数据动态调整字母索引和下划线的可见性
    @Override
    public void onBindViewHolder(@NonNull RecentlyViewedViewHolder holder, final int position) {
        final RecentlyViewed user = luntanRecentlyViewedList.get(position);
        holder.name.setText(luntanRecentlyViewedList.get(position).getName());
        String url = luntanRecentlyViewedList.get(position).getimg();
        Glide.with(holder.img.getContext()).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imgBitmap = resource;
                holder.img.setImageBitmap(imgBitmap);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                // 当图片被清除时的处理
            }
        });

        // 设置点击事件，点击后跳转到Perdet活动并传递数据
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Perdet.class);
                i.putExtra("name", luntanRecentlyViewedList.get(position).getName());
                i.putExtra("beiyong", luntanRecentlyViewedList.get(position).getbeiyong());
                i.putExtra("tel", luntanRecentlyViewedList.get(position).gettel());
                i.putExtra("img", luntanRecentlyViewedList.get(position).getimg());
                i.putExtra("fenzu", luntanRecentlyViewedList.get(position).getfenzu());
                context.startActivity(i);
            }
        });

        // 设置字母索引和下划线的可见性
        String mark = luntanRecentlyViewedList.get(position).getStart();
        if (position == getPosition(mark)) {
            holder.letterlayout.setVisibility(View.VISIBLE);
            holder.letter.setText(luntanRecentlyViewedList.get(position).getStart());
        } else {
            holder.letterlayout.setVisibility(View.GONE);
        }

        if (position != getItemCount() - 1 && luntanRecentlyViewedList.get(position).getStart().equalsIgnoreCase(luntanRecentlyViewedList.get(position + 1).getStart())) {
            holder.underline.setVisibility(View.VISIBLE);
        } else {
            holder.underline.setVisibility(View.GONE);
        }
    }

    // 获取指定字母索引的位置
    private int getPosition(String mark) {
        for (int i = 0; i < getItemCount(); i++) {
            if (luntanRecentlyViewedList.get(i).getStart().equalsIgnoreCase(mark)) {
                return i;
            }
        }
        return -1;
    }

    // 获取项目总数
    @Override
    public int getItemCount() {
        return luntanRecentlyViewedList.size();
    }

    // 自定义ViewHolder类
    public static class RecentlyViewedViewHolder extends RecyclerView.ViewHolder {
        LinearLayout letterlayout; // 字母布局
        TextView letter; // 字母文本
        TextView name; // 名称文本
        ImageView img; // 图片视图
        View view; // 项目视图
        View underline; // 下划线视图

        // ViewHolder的构造方法
        public RecentlyViewedViewHolder(@NonNull View itemView) {
            super(itemView);

            // 初始化视图
            name = itemView.findViewById(R.id.name);
            img = itemView.findViewById(R.id.img);
            letter = itemView.findViewById(R.id.letter);
            letterlayout = itemView.findViewById(R.id.letterlayout);
            view = itemView;
            underline = itemView.findViewById(R.id.underline);
        }
    }
}
