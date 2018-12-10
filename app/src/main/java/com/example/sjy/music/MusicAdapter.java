package com.example.sjy.music;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyMusicHolder> {
    public ArrayList<MusicBean> songs;

    public MusicAdapter(ArrayList<MusicBean> songs) {
        this.songs = songs;
    }

    @NonNull
    @Override
    public MyMusicHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        MyMusicHolder myMusicHolder=new MyMusicHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_music,viewGroup,false));
        return myMusicHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyMusicHolder holder, int position) {
        holder.setData(position);

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }



    class MyMusicHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvArtist;
        ImageView ivFaceSmall;
        public MyMusicHolder(@NonNull View view) {
            super(view);
            tvName=view.findViewById(R.id.music_name);
            tvArtist=view.findViewById(R.id.artist_name);
            ivFaceSmall=view.findViewById(R.id.face_small);
        }
        public void setData(int position){
            MusicBean song=songs.get(position);
            tvName.setText(song.getName());
            tvArtist.setText(song.getSinger());
            Glide.with(itemView).load(song.getPic()).into(ivFaceSmall);//将图片转换为二进制码

        }
    }

}
