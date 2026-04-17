package com.example.music;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music.Fragments.SongAlbumFragment;
import com.example.music.Models.Album;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context context;
    private List<Album> albums;

    public AlbumAdapter(Context context, List<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = albums.get(position);
        holder.name.setText(album.getName());

        // Load ảnh cover chính
        Glide.with(context)
                .load(album.getImageUrl())
                .placeholder(R.drawable.baseline_schedule_24)
                .into(holder.cover);

        // Load background mờ phía sau
        Glide.with(context)
                .load(album.getImageUrl())
                .placeholder(R.drawable.baseline_schedule_24)
                .into(holder.background);

        // 👉 Khi click vào album -> mở SongAlbumFragment
        holder.itemView.setOnClickListener(v -> {
            SongAlbumFragment fragment = new SongAlbumFragment();
            Bundle args = new Bundle();
            args.putSerializable("album", album); // truyền album sang fragment
            fragment.setArguments(args);

            ((AppCompatActivity) context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cover, background;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.playlist_cover);
            background = itemView.findViewById(R.id.playlist_background);
            name = itemView.findViewById(R.id.playlist_name);
        }
    }
}
