package com.example.music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.music.Fragments.NowPlayingFragment;
import com.example.music.Models.Song;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private Context context;
    private List<Song> songs;

    public SongAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.title.setText(song.getTitle());
        holder.artist.setText(song.getArtist());

        // Ảnh bìa nhỏ bên trái
        Glide.with(context)
                .load(song.getCoverUrl())
                .placeholder(R.drawable.baseline_schedule_24) // ảnh có sẵn trong drawable
                .into(holder.cover);


        // Background blur bên phải
        Glide.with(context)
                .load(song.getCoverUrl())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                .into(holder.bg);

        // 👉 sự kiện click mở NowPlaying
        holder.itemView.setOnClickListener(v -> {
            NowPlayingFragment fragment = NowPlayingFragment.newInstance(songs, position);

            ((AppCompatActivity) context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
    public void updateList(List<Song> newSongs) {
        songs.clear();
        songs.addAll(newSongs);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cover, bg;
        TextView title, artist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.song_cover);
            bg = itemView.findViewById(R.id.song_background);   // 👈 thêm background
            title = itemView.findViewById(R.id.song_title);
            artist = itemView.findViewById(R.id.song_artist);
        }
    }
}
