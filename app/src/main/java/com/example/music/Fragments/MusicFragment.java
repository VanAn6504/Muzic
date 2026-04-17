package com.example.music.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music.Models.Album;
import com.example.music.Models.Playlist;
import com.example.music.Models.Song;
import com.example.music.PlaylistAdapter;
import com.example.music.SongAdapter;
import com.example.music.AlbumAdapter;
import com.example.music.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MusicFragment extends Fragment {

    private TextView tabAllSongs, tabPlaylists, tabAlbums;
    private RecyclerView recyclerViewMusic;

    private SongAdapter songAdapter;
    private PlaylistAdapter playlistAdapter;
    private AlbumAdapter albumAdapter;

    private List<Song> songList = new ArrayList<>();
    private List<Playlist> playlistList = new ArrayList<>();
    private List<Album> albumList = new ArrayList<>();

    private DatabaseReference dbRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);

        tabAllSongs = view.findViewById(R.id.tab_all_songs);
        tabPlaylists = view.findViewById(R.id.tab_playlists);
        tabAlbums = view.findViewById(R.id.tab_albums);
        recyclerViewMusic = view.findViewById(R.id.recyclerViewMusic);

        recyclerViewMusic.setLayoutManager(new LinearLayoutManager(getContext()));
        dbRef = FirebaseDatabase.getInstance().getReference();

        // Mặc định load All Songs
        loadAllSongs();
        highlightTab(tabAllSongs);

        tabAllSongs.setOnClickListener(v -> {
            highlightTab(tabAllSongs);
            loadAllSongs();
        });

        tabPlaylists.setOnClickListener(v -> {
            highlightTab(tabPlaylists);
            loadPlaylists();
        });

        tabAlbums.setOnClickListener(v -> {
            highlightTab(tabAlbums);
            loadAlbums(); // 👈 Thêm dòng này
        });

        return view;
    }

    private void highlightTab(TextView selectedTab) {
        tabAllSongs.setTextColor(getResources().getColor(R.color.gray));
        tabPlaylists.setTextColor(getResources().getColor(R.color.gray));
        tabAlbums.setTextColor(getResources().getColor(R.color.gray));

        selectedTab.setTextColor(getResources().getColor(R.color.pink));
    }

    private void loadAllSongs() {
        dbRef.child("songs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                songList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Song s = snap.getValue(Song.class);
                    if (s != null) songList.add(s);
                }
                songAdapter = new SongAdapter(getContext(), songList);
                recyclerViewMusic.setAdapter(songAdapter);
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadPlaylists() {
        dbRef.child("playlists").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                playlistList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Playlist p = snap.getValue(Playlist.class);
                    if (p != null) playlistList.add(p);
                }
                playlistAdapter = new PlaylistAdapter(getContext(), playlistList);
                recyclerViewMusic.setAdapter(playlistAdapter);
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadAlbums() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }

        dbRef.child("users").child(user.getUid()).child("albums")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        albumList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Album a = snap.getValue(Album.class);
                            if (a != null) albumList.add(a);
                        }

                        albumAdapter = new AlbumAdapter(getContext(), albumList);
                        recyclerViewMusic.setAdapter(albumAdapter);

                        if (albumList.isEmpty()) {
                            Toast.makeText(getContext(), "Chưa có album nào!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

}
