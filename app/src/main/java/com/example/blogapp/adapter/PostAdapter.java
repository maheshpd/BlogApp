package com.example.blogapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.blogapp.R;
import com.example.blogapp.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyPostViewHolder> {

    Context mContext;
    List<Post> mData;

    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyPostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_post_item,viewGroup,false);
        return new MyPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostViewHolder myPostViewHolder, int i) {
        myPostViewHolder.tvTitle.setText(mData.get(i).getTitle());
        Glide.with(mContext).load(mData.get(i).getPicture()).into(myPostViewHolder.imgPost);
        Glide.with(mContext).load(mData.get(i).getUserPhoto()).into(myPostViewHolder.imgPostProfile);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyPostViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView imgPost,imgPostProfile;

        public MyPostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.row_post_title);
            imgPost = itemView.findViewById(R.id.row_post_img);
            imgPostProfile = itemView.findViewById(R.id.row_post_profile);
        }
    }
}
